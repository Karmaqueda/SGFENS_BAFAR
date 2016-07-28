/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.bo;

import com.google.gson.Gson;
import com.tsp.sct.bo.AlmacenBO;
import com.tsp.sct.bo.CrDocImprimibleBO;
import com.tsp.sct.bo.CrEstadoSolicitudBO;
import com.tsp.sct.bo.CrFormularioBO;
import com.tsp.sct.bo.CrFormularioCampoBO;
import com.tsp.sct.bo.CrFormularioEventoBO;
import com.tsp.sct.bo.CrGrupoFormularioBO;
import com.tsp.sct.bo.EmpleadoBO;
import com.tsp.sct.bo.EmpresaBO;
import com.tsp.sct.bo.UsuarioBO;
import com.tsp.sct.config.Configuration;
import com.tsp.sct.cr.RevisionesCr;
import com.tsp.sct.dao.dao.CrFrmEventoSolicitudDao;
import com.tsp.sct.dao.dto.CrFormularioCampo;
import com.tsp.sct.dao.dto.CrFormularioEvento;
import com.tsp.sct.dao.dto.CrFormularioRespuesta;
import com.tsp.sct.dao.dto.CrFrmEventoSolicitud;
import com.tsp.sct.dao.dto.CrGrupoFormulario;
import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.dao.jdbc.CrFormularioEventoDaoImpl;
import com.tsp.sct.dao.jdbc.CrFormularioRespuestaDaoImpl;
import com.tsp.sct.dao.jdbc.CrFrmEventoSolicitudDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.util.Base64;
import com.tsp.sct.util.DateManage;
import com.tsp.sct.util.FileManage;
import com.tsp.sct.util.GenericMethods;
import com.tsp.sct.util.StringManage;
import com.tsp.sgfens.sesion.FormatoMovimientosSesion;
import com.tsp.sgfens.sesion.MovimientoSesion;
import com.tsp.sgfens.ws.request.ConsultaImprimiblesRequest;
import com.tsp.sgfens.ws.request.CrearCrFormularioEventoRequest;
import com.tsp.sgfens.ws.request.EmpleadoDtoRequest;
import com.tsp.sgfens.ws.request.WsItemConceptoRequest;
import com.tsp.sgfens.ws.request.WsItemCrFormularioRespuestaRequest;
import com.tsp.sgfens.ws.response.ConsultaCrFrmEventoSolicitudResponse;
import com.tsp.sgfens.ws.response.ConsultaImprimiblesResponse;
import com.tsp.sgfens.ws.response.CrearCrFormularioEventoResponse;
import com.tsp.sgfens.ws.response.WSResponseInsert;
import com.tsp.sgfens.ws.response.WsItemCrFrmEventoSolicitud;
import com.tsp.sgfens.ws.response.WsItemFormularioRespuesta;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author ISCesar
 */
public class ModuloCrWsBO {
    private final Gson gson = new Gson();
    private Connection conn = null;
    
    public Connection getConn() {
        if (this.conn==null){
            try {
                this.conn = ResourceManager.getConnection();
            } catch (SQLException ex) {}
        }else{
            try {
                if (this.conn.isClosed()){
                    this.conn = ResourceManager.getConnection();
                }
            } catch (SQLException ex) {}
        }
        return this.conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public ConsultaCrFrmEventoSolicitudResponse actualizaEventoSolicitud(String empleadoDtoRequestJson){
        ConsultaCrFrmEventoSolicitudResponse response;
         EmpleadoDtoRequest empleadoDtoRequest;
         try{
             empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJson, EmpleadoDtoRequest.class);
             
             response = this.actualizaEventoSolicitud(empleadoDtoRequest);
         }catch(Exception ex){
            response = new ConsultaCrFrmEventoSolicitudResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
         }
        return response;
    }
    
    public ConsultaCrFrmEventoSolicitudResponse actualizaEventoSolicitud(EmpleadoDtoRequest empleadoDtoRequest){
        ConsultaCrFrmEventoSolicitudResponse response = new ConsultaCrFrmEventoSolicitudResponse();
        int idEmpresa;
        int idEmpleado;
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())){
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
            }else{
                 try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                return response;
            }
            
            UsuarioBO usuarioBO = empleadoBO.getUsuarioBO();
            if (usuarioBO==null){
                try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("No se pudo recuperar la información de Usuario del empleado para registrar el prospecto.");
                return response;
            }
            
            CrFrmEventoSolicitudDaoImpl eventoSolicitudDaoImpl = new CrFrmEventoSolicitudDaoImpl(getConn());
            
            CrFrmEventoSolicitud[] eventosSolicitud = eventoSolicitudDaoImpl.findAll();
            for(CrFrmEventoSolicitud item : eventosSolicitud){
                WsItemCrFrmEventoSolicitud itemSolicitud = new WsItemCrFrmEventoSolicitud();
                itemSolicitud.setFechaHrCreacion(item.getFechaHrCreacion());
                itemSolicitud.setIdEstadoSolicitud(item.getIdEstadoSolicitud());
                itemSolicitud.setIdFormularioEvento(item.getIdFormularioEvento());
                itemSolicitud.setIdFrmEventoSolicitud(item.getIdFrmEventoSolicitud());
                itemSolicitud.setIdProductoCredito(item.getIdProductoCredito());
                itemSolicitud.setIdUsuarioEdicion(item.getIdUsuarioEdicion());
                itemSolicitud.setIdUsuarioVerificador(item.getIdUsuarioVerificador());
                itemSolicitud.setSapBp(item.getSapBp());
                itemSolicitud.setSapFechaAmortizacion(item.getSapFechaAmortizacion());
                itemSolicitud.setSapFechaApertura(item.getSapFechaApertura());
                itemSolicitud.setSapInfFechaCorte(item.getSapInfFechaCorte());
                itemSolicitud.setSapInfFechaPago(item.getSapInfFechaPago());
                itemSolicitud.setSapInfPlazoContrato(item.getSapInfPlazoContrato());
                itemSolicitud.setSapNoContrato(item.getSapNoContrato());
                itemSolicitud.setSapTablaAmortizacion(item.getSapTablaAmortizacion());
                
                response.getListaSolicitudes().add(itemSolicitud);
            }
            
            CrFormularioRespuestaDaoImpl respuestaDaoImpl = new CrFormularioRespuestaDaoImpl(getConn());
            CrFormularioRespuesta[] respuestasRevisar = respuestaDaoImpl.findWhereRevisarEquals(1);
            for(CrFormularioRespuesta frmResp : respuestasRevisar){
                WsItemFormularioRespuesta wifr = new WsItemFormularioRespuesta();
                wifr.setDescripcion(frmResp.getDescripcion());
                wifr.setIdEmpresa(frmResp.getIdEmpresa());
                wifr.setIdEstatus(frmResp.getIdEstatus());
                wifr.setIdFormulario(frmResp.getIdFormulario());
                wifr.setIdFormularioCampo(frmResp.getIdFormularioCampo());
                wifr.setIdFormularioEvento(frmResp.getIdFormularioEvento());
                wifr.setIdFormularioRespuesta(frmResp.getIdFormularioEvento());
                wifr.setRevisar(frmResp.getRevisar());
                wifr.setRevisarComentario(frmResp.getRevisarComentario());
                wifr.setValor(frmResp.getValor());
                
                response.getListaRespuestasPorRevisar().add(wifr);
            }
        }catch(Exception ex){
             ex.printStackTrace();
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error al procesar Evento Solicitud. Errores: " + GenericMethods.exceptionStackTraceToString(ex));
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return response;
    }
    
    public CrearCrFormularioEventoResponse insertaCrFormularioEvento(String empleadoDtoRequestJson, String crearCrFormularioEventoRequestJson){
        CrearCrFormularioEventoResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest;
        CrearCrFormularioEventoRequest crearCrFormularioEventoRequest;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJson, EmpleadoDtoRequest.class);
            crearCrFormularioEventoRequest = gson.fromJson(crearCrFormularioEventoRequestJson, CrearCrFormularioEventoRequest.class);
            
            response = this.insertaCrFormularioEvento(empleadoDtoRequest,crearCrFormularioEventoRequest);
        }catch(Exception ex){
            response = new CrearCrFormularioEventoResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;        
    }
    
    public CrearCrFormularioEventoResponse insertaCrFormularioEvento(EmpleadoDtoRequest empleadoDtoRequest, CrearCrFormularioEventoRequest crearCrFormularioEventoRequest){
        CrearCrFormularioEventoResponse response = new CrearCrFormularioEventoResponse();
        Configuration appConfig = new Configuration();
        
        int idEmpresa;
        int idEmpleado;
        String rfcEmpresaMatriz ="";
        
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())){
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
            }else{
                try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                return response;
            }
            
            UsuarioBO usuarioBO = empleadoBO.getUsuarioBO();
            Empresa empresaDto;
            if (usuarioBO==null){
                try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("No se pudo recuperar la información de Usuario del empleado para registrar el prospecto.");
                return response;
            }
            
            //Matriz de la empresa
            try{
                empresaDto = new EmpresaBO(getConn()).getEmpresaMatriz(idEmpresa);
                rfcEmpresaMatriz = empresaDto.getRfc();
            }catch(Exception ex){
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("No se pudo recuperar la información de la empresa del empleado. Verifique con su administrador de sistema." + ex.toString());
                return response;
            }
            
            //validaciones previas
            String msgError = "";
            if (crearCrFormularioEventoRequest.getIdGrupoFormulario()<=0){
                msgError += "El Grupo Formulario utilizado para el evento, no es válido. ";
            }
            if (StringManage.getValidString(crearCrFormularioEventoRequest.getFolioMovil()).length()<=0){
                msgError += "El Folio Movil enviado para el Evento Formulario, no puede ser vacío o nulo, es requerido. ";
            }
        
            if (!msgError.equals("")){
                try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
                response.setError(true);
                response.setNumError(901);
                response.setMsgError(msgError);
                return response;
            }else{
                
                CrFormularioEventoDaoImpl crFormularioEventoDao = new CrFormularioEventoDaoImpl(getConn());
                CrFormularioRespuestaDaoImpl crFormularioRespuestaDao = new CrFormularioRespuestaDaoImpl(getConn());
                CrGrupoFormularioBO crGrupoFormularioBO = new CrGrupoFormularioBO(getConn());
                CrFormularioCampoBO crFormularioCampoBO = new CrFormularioCampoBO(getConn());
                
                CrFormularioEvento[] crFormularioEventos = crFormularioEventoDao.findWhereFolioMovilEquals(crearCrFormularioEventoRequest.getFolioMovil());
                if (crFormularioEventos.length>0){
                    // reenviado por posible desconexion en primer envio                    
                    CrFormularioEvento crFormularioEvento = crFormularioEventos[0];
                    CrGrupoFormulario crGrupoFormulario = crGrupoFormularioBO.findCrGrupoFormulariobyId(crearCrFormularioEventoRequest.getIdGrupoFormulario());
                    
                    crFormularioEvento.setIdGrupoFormulario(crGrupoFormulario.getIdGrupoFormulario());
                    crFormularioEvento.setFechaHrCreacion(crearCrFormularioEventoRequest.getFechaHrCreacion());
                    crFormularioEvento.setIdUsuarioCapturo(usuarioBO.getUser().getIdUsuarios());
                    crFormularioEvento.setTipoEntidadRespondio("PROSPECTO");
                    crFormularioEvento.setIdEntidadRespondio(crearCrFormularioEventoRequest.getIdEntidad());
                    crFormularioEvento.setLatitud(crearCrFormularioEventoRequest.getLatitud());
                    crFormularioEvento.setLongitud(crearCrFormularioEventoRequest.getLongitud());
                    crFormularioEvento.setFolioMovil(crearCrFormularioEventoRequest.getFolioMovil());
                    crFormularioEvento.setIdEmpresa(usuarioBO.getUser().getIdEmpresa());
                    crFormularioEvento.setIdEstatus(1);
                    
                    crFormularioEventoDao.update(crFormularioEvento.createPk(), crFormularioEvento);
                    
                    //Actualizamos el eventosolicitud
                    if(crearCrFormularioEventoRequest.getEventoSolicitud() != null){
                        RevisionesCr revisionesCr = new RevisionesCr();            
                        CrFrmEventoSolicitudDaoImpl crFrmEventoSolicitudDaoImpl = new CrFrmEventoSolicitudDaoImpl(getConn());
                        CrFrmEventoSolicitud[] crFrmEventoSolicitudes = crFrmEventoSolicitudDaoImpl.findWhereIdFrmEventoSolicitudEquals(crearCrFormularioEventoRequest.getEventoSolicitud().getIdEventoSolicitud());//.getIdEstadoSolicitud());
                        if(crFrmEventoSolicitudes.length > 0){
                            CrFrmEventoSolicitud crFrmEventoSolicitud = crFrmEventoSolicitudes[0];
                            /*
                            crFrmEventoSolicitud.setFechaHrCreacion(crearCrFormularioEventoRequest.getEventoSolicitud().getFechaHrCreacion());
                            crFrmEventoSolicitud.setIdEstadoSolicitud(crearCrFormularioEventoRequest.getEventoSolicitud().getIdEstadoSolicitud());
                            crFrmEventoSolicitud.setIdFormularioEvento(crFormularioEvento.getIdFormularioEvento());
                            crFrmEventoSolicitud.setIdFrmEventoSolicitud(crearCrFormularioEventoRequest.getEventoSolicitud().getIdEventoSolicitud());
                            crFrmEventoSolicitud.setIdProductoCredito(crearCrFormularioEventoRequest.getEventoSolicitud().getIdProductoCredito());
                            crFrmEventoSolicitud.setIdUsuarioEdicion(idEmpleado);
                            crFrmEventoSolicitud.setIdUsuarioVerificador(idEmpleado);
                            
                            crFrmEventoSolicitudDaoImpl.update(crFrmEventoSolicitud.createPk(), crFrmEventoSolicitud);
                            */
                            int idEstatusDestino = crearCrFormularioEventoRequest.getEventoSolicitud().getIdEstadoSolicitud(); //CrEstadoSolicitudBO.S_POR_REVISAR;
                            revisionesCr.almacenaRegistroSolicitudBitacora(getConn(), crFormularioEvento.getIdFormularioEvento(), crearCrFormularioEventoRequest.getEventoSolicitud().getIdProductoCredito(), idEmpresa, usuarioBO.getUser().getIdUsuarios(), idEstatusDestino, "Actualizada desde WS Móvil.", false);
                            
                            response.setIdEventoSolicitud(crFrmEventoSolicitud.getIdFrmEventoSolicitud());
                        }else{
                            /*
                            CrFrmEventoSolicitud crFrmEventoSolicitud = new CrFrmEventoSolicitud();
                            crFrmEventoSolicitud.setFechaHrCreacion(crearCrFormularioEventoRequest.getEventoSolicitud().getFechaHrCreacion());
                            crFrmEventoSolicitud.setIdEstadoSolicitud(crearCrFormularioEventoRequest.getEventoSolicitud().getIdEstadoSolicitud());
                            crFrmEventoSolicitud.setIdFormularioEvento(crFormularioEvento.getIdFormularioEvento());
                            crFrmEventoSolicitud.setIdFrmEventoSolicitud(crearCrFormularioEventoRequest.getEventoSolicitud().getIdEventoSolicitud());
                            crFrmEventoSolicitud.setIdProductoCredito(crearCrFormularioEventoRequest.getEventoSolicitud().getIdProductoCredito());
                            crFrmEventoSolicitud.setIdUsuarioEdicion(idEmpleado);
                            crFrmEventoSolicitud.setIdUsuarioVerificador(idEmpleado);
                            
                            crFrmEventoSolicitudDaoImpl.insert(crFrmEventoSolicitud);
                            
                            */
                            int idEstatusDestino = crearCrFormularioEventoRequest.getEventoSolicitud().getIdEstadoSolicitud(); //CrEstadoSolicitudBO.S_POR_REVISAR;
                            revisionesCr.almacenaRegistroSolicitudBitacora(getConn(), crFormularioEvento.getIdFormularioEvento(), crearCrFormularioEventoRequest.getEventoSolicitud().getIdProductoCredito(), idEmpresa, usuarioBO.getUser().getIdUsuarios(), idEstatusDestino, "Actualizada desde WS Móvil.", false);
                            CrFrmEventoSolicitud crFrmEventoSolicitud = revisionesCr.getCrFrmEventoSolicitud();
                            response.setIdEventoSolicitud(crFrmEventoSolicitud.getIdFrmEventoSolicitud());
                        }
                    }
                    
                    //Agregamos los productos al formato de Sesion
                    for (WsItemCrFormularioRespuestaRequest itemRespuestaRequest : crearCrFormularioEventoRequest.getListaCrFormularioRespuestaRequest()){
                        //CrFormularioCampo crFormularioCampo = crFormularioCampoBO.findCrFormularioCampobyId(itemRespuestaRequest.getIdFormularioCampo());
                        CrFormularioRespuesta crFormularioRespuesta = new CrFormularioRespuesta();
                        CrFormularioRespuesta[] crFormularioRespuestas = crFormularioRespuestaDao.findByDynamicWhere(" id_formulario_evento=" + crFormularioEvento.getIdFormularioEvento() + " AND id_formulario_campo=" + itemRespuestaRequest.getIdFormularioCampo(), null);
                        if(crFormularioRespuestas.length > 0){
                            //Modificar
                            crFormularioRespuesta = crFormularioRespuestas[0];
                            crFormularioRespuesta.setIdFormularioEvento(crFormularioEvento.getIdFormularioEvento());
                            crFormularioRespuesta.setIdFormulario(itemRespuestaRequest.getIdFormulario());
                            crFormularioRespuesta.setIdFormularioCampo(itemRespuestaRequest.getIdFormularioCampo());
                            crFormularioRespuesta.setValor(itemRespuestaRequest.getValor());
                            crFormularioRespuesta.setDescripcion(itemRespuestaRequest.getDescripcion());
                            crFormularioRespuesta.setIdEmpresa(crFormularioEvento.getIdEmpresa());
                            crFormularioRespuesta.setIdEstatus(1);
                            crFormularioRespuesta.setRevisar(itemRespuestaRequest.getRevisar());
                            crFormularioRespuesta.setRevisarComentario(itemRespuestaRequest.getRevisarComentario());

                            if(!StringManage.getValidString(itemRespuestaRequest.getImagen()).equals("")){
                                File archivoImagenProspecto = null;
                                try{
                                    byte[] bytesImagenProspecto = Base64.decode(itemRespuestaRequest.getImagen());
                                    String ubicacionImagenesProspectos = appConfig.getApp_content_path() + rfcEmpresaMatriz +"/AdjuntosFormulario/";
                                    String nombreArchivoImagenProspecto = "img_resp_"+DateManage.getDateHourString()+".jpg";
                                    archivoImagenProspecto = FileManage.createFileFromByteArray(bytesImagenProspecto, ubicacionImagenesProspectos, nombreArchivoImagenProspecto);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }

                                if (archivoImagenProspecto!=null)
                                    crFormularioRespuesta.setValor(archivoImagenProspecto.getName());
                            }
                            crFormularioRespuestaDao.update(crFormularioRespuesta.createPk(), crFormularioRespuesta);
                        }else{
                            //Insertar
                            
                            crFormularioRespuesta.setIdFormularioEvento(crFormularioEvento.getIdFormularioEvento());
                            crFormularioRespuesta.setIdFormulario(itemRespuestaRequest.getIdFormulario());
                            crFormularioRespuesta.setIdFormularioCampo(itemRespuestaRequest.getIdFormularioCampo());
                            crFormularioRespuesta.setValor(itemRespuestaRequest.getValor());
                            crFormularioRespuesta.setDescripcion(itemRespuestaRequest.getDescripcion());
                            crFormularioRespuesta.setIdEmpresa(crFormularioEvento.getIdEmpresa());
                            crFormularioRespuesta.setIdEstatus(1);
                            crFormularioRespuesta.setRevisar(itemRespuestaRequest.getRevisar());
                            crFormularioRespuesta.setRevisarComentario(itemRespuestaRequest.getRevisarComentario());

                            if(!StringManage.getValidString(itemRespuestaRequest.getImagen()).equals("")){
                                File archivoImagenProspecto = null;
                                try{
                                    byte[] bytesImagenProspecto = Base64.decode(itemRespuestaRequest.getImagen());
                                    String ubicacionImagenesProspectos = appConfig.getApp_content_path() + rfcEmpresaMatriz +"/AdjuntosFormulario/";
                                    String nombreArchivoImagenProspecto = "img_resp_"+DateManage.getDateHourString()+".jpg";
                                    archivoImagenProspecto = FileManage.createFileFromByteArray(bytesImagenProspecto, ubicacionImagenesProspectos, nombreArchivoImagenProspecto);
                                }catch(Exception e){

                                }

                                if (archivoImagenProspecto!=null)
                                    crFormularioRespuesta.setValor(archivoImagenProspecto.getName());
                            }

                            crFormularioRespuestaDao.insert(crFormularioRespuesta);
                        }
                    }
                    
                    response.setIdObjetoCreado(crFormularioEvento.getIdFormularioEvento());
                    
                }else{
                    //nuevo
                    CrFormularioEvento crFormularioEvento = new CrFormularioEvento();
                    
                    CrGrupoFormulario crGrupoFormulario = crGrupoFormularioBO.findCrGrupoFormulariobyId(crearCrFormularioEventoRequest.getIdGrupoFormulario());
                
                    crFormularioEvento.setIdGrupoFormulario(crGrupoFormulario.getIdGrupoFormulario());
                    crFormularioEvento.setFechaHrCreacion(crearCrFormularioEventoRequest.getFechaHrCreacion());
                    crFormularioEvento.setIdUsuarioCapturo(usuarioBO.getUser().getIdUsuarios());
                    crFormularioEvento.setTipoEntidadRespondio("PROSPECTO");
                    crFormularioEvento.setIdEntidadRespondio(crearCrFormularioEventoRequest.getIdEntidad());
                    crFormularioEvento.setLatitud(crearCrFormularioEventoRequest.getLatitud());
                    crFormularioEvento.setLongitud(crearCrFormularioEventoRequest.getLongitud());
                    crFormularioEvento.setFolioMovil(crearCrFormularioEventoRequest.getFolioMovil());
                    crFormularioEvento.setIdEmpresa(usuarioBO.getUser().getIdEmpresa());
                    crFormularioEvento.setIdEstatus(1);

                    crFormularioEventoDao.insert(crFormularioEvento);
                    
                    //Insertamos el eventosolicitud
                    if(crearCrFormularioEventoRequest.getEventoSolicitud() != null){
                        /*
                        CrFrmEventoSolicitud crFrmEventoSolicitud = new CrFrmEventoSolicitud();
                        crFrmEventoSolicitud.setFechaHrCreacion(crearCrFormularioEventoRequest.getEventoSolicitud().getFechaHrCreacion());
                        crFrmEventoSolicitud.setIdEstadoSolicitud(crearCrFormularioEventoRequest.getEventoSolicitud().getIdEstadoSolicitud());
                        crFrmEventoSolicitud.setIdFormularioEvento(crFormularioEvento.getIdFormularioEvento());
                        crFrmEventoSolicitud.setIdFrmEventoSolicitud(crearCrFormularioEventoRequest.getEventoSolicitud().getIdEventoSolicitud());
                        crFrmEventoSolicitud.setIdProductoCredito(crearCrFormularioEventoRequest.getEventoSolicitud().getIdProductoCredito());
                        crFrmEventoSolicitud.setIdUsuarioEdicion(idEmpleado);
                        crFrmEventoSolicitud.setIdUsuarioVerificador(idEmpleado);
                        
                        new CrFrmEventoSolicitudDaoImpl(getConn()).insert(crFrmEventoSolicitud);
                        */
                        RevisionesCr revisionesCr = new RevisionesCr();
                        int idEstatusDestino = crearCrFormularioEventoRequest.getEventoSolicitud().getIdEstadoSolicitud(); //CrEstadoSolicitudBO.S_POR_REVISAR;
                        revisionesCr.almacenaRegistroSolicitudBitacora(getConn(), crFormularioEvento.getIdFormularioEvento(), crearCrFormularioEventoRequest.getEventoSolicitud().getIdProductoCredito(), idEmpresa, usuarioBO.getUser().getIdUsuarios(), idEstatusDestino, "Creada desde WS Móvil.", true);
                        CrFrmEventoSolicitud crFrmEventoSolicitud = revisionesCr.getCrFrmEventoSolicitud();
                        response.setIdEventoSolicitud(crFrmEventoSolicitud.getIdFrmEventoSolicitud());
                    }

                    //Agregamos los productos al formato de Sesion
                    for (WsItemCrFormularioRespuestaRequest itemRespuestaRequest : crearCrFormularioEventoRequest.getListaCrFormularioRespuestaRequest()){
                        CrFormularioCampo crFormularioCampo = crFormularioCampoBO.findCrFormularioCampobyId(itemRespuestaRequest.getIdFormularioCampo());
                        //if (crFormularioCampo.getIdTipoCampo()==1)
                        //posibles validaciones por tipo de campo para imagenes y firma
                        // para procesar el valor (base64) y crear un archivo en el sistema
                        // y asignar en bd de consola en valor, el nombre del archivo
                        
                        CrFormularioRespuesta crFormularioRespuesta = new CrFormularioRespuesta();
                        crFormularioRespuesta.setIdFormularioEvento(crFormularioEvento.getIdFormularioEvento());
                        crFormularioRespuesta.setIdFormulario(itemRespuestaRequest.getIdFormulario());
                        crFormularioRespuesta.setIdFormularioCampo(itemRespuestaRequest.getIdFormularioCampo());
                        crFormularioRespuesta.setValor(itemRespuestaRequest.getValor());
                        crFormularioRespuesta.setDescripcion(itemRespuestaRequest.getDescripcion());
                        crFormularioRespuesta.setIdEmpresa(crFormularioEvento.getIdEmpresa());
                        crFormularioRespuesta.setIdEstatus(1);
                        crFormularioRespuesta.setRevisar(itemRespuestaRequest.getRevisar());
                        crFormularioRespuesta.setRevisarComentario(itemRespuestaRequest.getRevisarComentario());
                        
                        if(!StringManage.getValidString(itemRespuestaRequest.getImagen()).trim().equals("")){
                            File archivoImagenProspecto = null;
                            try{
                                byte[] bytesImagenProspecto = Base64.decode(itemRespuestaRequest.getImagen());
                                String ubicacionImagenesProspectos = appConfig.getApp_content_path() + rfcEmpresaMatriz +"/AdjuntosFormulario/";
                                String nombreArchivoImagenProspecto = "img_resp_"+DateManage.getDateHourString()+".jpg";
                                archivoImagenProspecto = FileManage.createFileFromByteArray(bytesImagenProspecto, ubicacionImagenesProspectos, nombreArchivoImagenProspecto);
                            }catch(Exception e){
                                
                            }
                            
                            if (archivoImagenProspecto!=null)
                                crFormularioRespuesta.setValor(archivoImagenProspecto.getName());
                        }
                                
                        crFormularioRespuestaDao.insert(crFormularioRespuesta);
                    }
                    
                    response.setIdObjetoCreado(crFormularioEvento.getIdFormularioEvento());
                }
                
            }
            
            //registramos ubicacion
            try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
        }catch(Exception ex){
            ex.printStackTrace();
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error al procesar Formulario Evento. Errores: " + GenericMethods.exceptionStackTraceToString(ex));
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return response;
    }
    
    
    public ConsultaImprimiblesResponse consultaImprimibles(EmpleadoDtoRequest empleadoDtoRequest, ConsultaImprimiblesRequest consultaImprimiblesRequest){
        ConsultaImprimiblesResponse response = new ConsultaImprimiblesResponse();
        Configuration appConfig = new Configuration();
        
        int idEmpresa;
        int idEmpleado;
        String rfcEmpresaMatriz ="";
        
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())){
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
            }else{
                try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                return response;
            }
            
            UsuarioBO usuarioBO = empleadoBO.getUsuarioBO();
            Empresa empresaDto;
            if (usuarioBO==null){
                try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("No se pudo recuperar la información de Usuario del empleado.");
                return response;
            }
            
            //Matriz de la empresa
            try{
                empresaDto = new EmpresaBO(getConn()).getEmpresaMatriz(idEmpresa);
                rfcEmpresaMatriz = empresaDto.getRfc();
            }catch(Exception ex){
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("No se pudo recuperar la información de la empresa del empleado. Verifique con su administrador de sistema." + ex.toString());
                return response;
            }
            
            //validaciones previas
            String msgError = "";
            if (consultaImprimiblesRequest.getIdCrFormularioEvento()<=0){
                msgError += "El Formulario Evento indicado, no es válido. Valor enviado: " + consultaImprimiblesRequest.getIdCrFormularioEvento();
            }
        
            if (!msgError.equals("")){
                try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
                response.setError(true);
                response.setNumError(901);
                response.setMsgError(msgError);
                return response;
            }else{
                
                // Recuperamos imprimibles
                CrDocImprimibleBO crDocImprimibleBO = new CrDocImprimibleBO(getConn());
                CrFormularioEventoBO crFormularioEventoBO = new CrFormularioEventoBO(consultaImprimiblesRequest.getIdCrFormularioEvento(), getConn());
                CrFormularioEvento crFormularioEvento = crFormularioEventoBO.getCrFormularioEvento();
                
                // TODO : recuperar imprimibles
                
                // Se descargara el objeto DTO correspondiente a CrDocImprimible
                // y ademas una URL para obtener el imprimible desde android
                
                
                
            }
            
            //registramos ubicacion
            try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
        }catch(Exception ex){
            ex.printStackTrace();
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error al procesar Formulario Evento. Errores: " + GenericMethods.exceptionStackTraceToString(ex));
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return response;
    }
    
}
