/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.bo;

import com.google.gson.Gson;
import com.tsp.sct.bo.AlmacenBO;
import com.tsp.sct.bo.BancoOperacionBO;
import com.tsp.sct.bo.BitacoraCreditosOperacionBO;
import com.tsp.sct.bo.ClienteBO;
import com.tsp.sct.bo.ClienteCampoAdicionalBO;
import com.tsp.sct.bo.EmpleadoBO;
import com.tsp.sct.bo.EmpresaBO;
import com.tsp.sct.bo.SGProspectoBO;
import com.tsp.sct.bo.UsuarioBO;
import com.tsp.sct.bo.EmpleadoInventarioRepartidorBO;
import com.tsp.sct.bo.SGPedidoBO;
import com.tsp.sct.bo.SGPedidoDevolucionesCambioBO;
import com.tsp.sct.bo.ZonaHorariaBO;
import com.tsp.sct.config.Configuration;
import com.tsp.sct.dao.dto.*;
import com.tsp.sct.dao.jdbc.*;
import com.tsp.sct.mail.TspMailBO;
import com.tsp.sct.util.Base64;
import com.tsp.sct.util.DateManage;
import com.tsp.sct.util.Encrypter;
import com.tsp.sct.util.FileManage;
import com.tsp.sct.util.GenericValidator;
import com.tsp.sct.util.RandomString;
import com.tsp.sct.util.StringManage;
import com.tsp.sgfens.sesion.FormatoMovimientosSesion;
import com.tsp.sgfens.sesion.MovimientoSesion;
import com.tsp.sgfens.ws.request.*;
import com.tsp.sgfens.ws.response.*;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 09-abr-2013 
 * 
 * Clase contenedora de métodos para inserción o actualización
 * de datos mediante web service
 */
public class InsertaActualizaWsBO {
    
    private final Gson gson = new Gson();
    private Connection conn = null;
    private final GenericValidator gc = new GenericValidator();

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

    public InsertaActualizaWsBO() {
    }
    
    public InsertaActualizaWsBO(Connection conn) {
        this.conn = conn;
    }
    
    

    
    /**
     * Actualiza la ubicacion de un empleado segun los datos de longitud y latitud otorgados
     * a tráves de un objeto 
     * @param empleadoDtoRequestJSON String Datos del empleado (móvil) en formato JSON (clase EmpleadoDtoRequest)
     * @return WSResponse Respuesta compuesta por objeto complejo con respuesta básica de exito
     */
    public WSResponse actualizarUbicacionEmpleado(String empleadoDtoRequestJSON) {
        WSResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            
            response = this.actualizarUbicacionEmpleado(empleadoDtoRequest);
        }catch(Exception ex){
            response = new WSResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }
    
    /**
     * Actualiza la ubicacion de un empleado segun los datos de longitud y latitud otorgados
     * @param empleadoDtoRequest EmpleadoDtoRequest Datos del empleado (móvil)
     * @return WSResponse Respuesta compuesta por objeto complejo con respuesta básica de exito
     */
    public WSResponse actualizarUbicacionEmpleado(EmpleadoDtoRequest empleadoDtoRequest) {
        WSResponse response = new WSResponse();
         
        try {
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                empleadoBO.getEmpleado().setLatitud(Double.parseDouble(empleadoDtoRequest.getUbicacionLatitud()));
                empleadoBO.getEmpleado().setLongitud(Double.parseDouble(empleadoDtoRequest.getUbicacionLongitud()));
                if (empleadoBO.updateBD()){
                    
                    EmpleadoBitacoraPosicion bitacoraPosicionDto = new EmpleadoBitacoraPosicion();
                    EmpleadoBitacoraPosicionDaoImpl bitacoraPosicionDao = new EmpleadoBitacoraPosicionDaoImpl(getConn());
                    
                    bitacoraPosicionDto.setIdEmpleado(empleadoBO.getEmpleado().getIdEmpleado());
                    try{
                        bitacoraPosicionDto.setFecha(empleadoDtoRequest.getFechaHora()!=null?empleadoDtoRequest.getFechaHora() : (ZonaHorariaBO.DateZonaHorariaByIdEmpresa(getConn(), new Date(), (int)empleadoBO.getEmpleado().getIdEmpresa()).getTime()) );
                    }catch(Exception e){
                        bitacoraPosicionDto.setFecha(empleadoDtoRequest.getFechaHora()!=null?empleadoDtoRequest.getFechaHora() : new Date());
                    }
                    bitacoraPosicionDto.setLatitud(Double.parseDouble(empleadoDtoRequest.getUbicacionLatitud()));
                    bitacoraPosicionDto.setLongitud(Double.parseDouble(empleadoDtoRequest.getUbicacionLongitud()));
                    bitacoraPosicionDto.setVelocidadMXSeg(empleadoDtoRequest.getVelocidadMetroXSegundo());
                    bitacoraPosicionDto.setDireccionAvance(empleadoDtoRequest.getDireccionAvance());
                    
                    try{
                        if (bitacoraPosicionDto.getLatitud()!=0
                                && bitacoraPosicionDto.getLongitud()!=0)
                            bitacoraPosicionDao.insert(bitacoraPosicionDto);                        
                    }catch(Exception ex){
                        response.setNumError(902);
                        response.setMsgError("No se pudo registrar la ubicación del empleado en la bitácora: " + ex.toString());
                        response.setError(true);
                        ex.printStackTrace();
                        return response;
                    }
                    
                    //Actualizamos porcentaje de bateria si fue enviado
                    if (empleadoDtoRequest.getPorcentajeBateria()>0){
                        try{
                            DispositivoMovilDaoImpl dmDao =  new DispositivoMovilDaoImpl(getConn());
                            DispositivoMovil dispositivoMovil = dmDao.findByPrimaryKey(empleadoBO.getEmpleado().getIdDispositivo());
                            if (dispositivoMovil!=null){
                                dispositivoMovil.setPctPila(empleadoDtoRequest.getPorcentajeBateria());
                                dmDao.update(dispositivoMovil.createPk(), dispositivoMovil);
                            }
                        }catch(Exception ex){
                            response.setNumError(902);
                            response.setMsgError("No se pudo actualizar el porcentaje de bateria del dispositivo: " + ex.toString());
                            response.setError(true);
                            ex.printStackTrace();
                            return response;
                        }
                    }
                    
                    response.setError(false);
                }else{
                    response.setNumError(902);
                    response.setMsgError("No se pudo actualizar la ubicacion del empleado.");
                }
            } else {
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        } catch (Exception e) {
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error al actualizar ubicacion del empleado. " + e.toString());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
         
        return response;
    }
    
    public SendMensajesMovilResponse insertaMensajeMovilByEmpleado(String empleadoDtoRequestJSON, String sendMensajesMovilRequestJSON){
        
        SendMensajesMovilResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest = null;
        //ItemMensajeMovilRequest crearMensajeMovilRequest = null;
        SendMensajesMovilRequest sendMensajesMovilRequest =null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            sendMensajesMovilRequest = gson.fromJson(sendMensajesMovilRequestJSON, SendMensajesMovilRequest.class);
            
            response = this.insertaMensajeMovilByEmpleado(empleadoDtoRequest,sendMensajesMovilRequest);
        }catch(Exception ex){
            response = new SendMensajesMovilResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }
    public SendMensajesMovilResponse insertaMensajeMovilByEmpleado(EmpleadoDtoRequest empleadoDtoRequest, SendMensajesMovilRequest sendMensajesMovilRequest){
        
        SendMensajesMovilResponse response = new SendMensajesMovilResponse();
        
        try{
            long idEmpresa = 0;
            
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())){
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
            }else{
                   
                
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                return response;
            }
        
            if (sendMensajesMovilRequest==null){
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("Objeto de datos de mensaje nulo.");
                return response;
            }
             
                
            for (ItemMensajeMovilRequest item:sendMensajesMovilRequest.getItemMensajeMovilRequest()){
                int idCreado=-1;
                WsItemSendMensajeResponse wsItemSendMensajeResponse = new WsItemSendMensajeResponse();
                if (item.getMensaje()==null){
                    wsItemSendMensajeResponse.setError(true);
                    wsItemSendMensajeResponse.setNumError(901);
                    wsItemSendMensajeResponse.setMsgError("Mensaje nulo.");
                    response.getWsItemSendMensajeResponse().add(wsItemSendMensajeResponse);
                    break;
                }else{
                    if (item.getMensaje().replaceAll(" ", "").trim().length()<=0){
                        wsItemSendMensajeResponse.setError(true);
                        wsItemSendMensajeResponse.setNumError(901);
                        wsItemSendMensajeResponse.setMsgError("Mensaje vacío.");
                        response.getWsItemSendMensajeResponse().add(wsItemSendMensajeResponse);
                        break;
                    }else if (item.getMensaje().length()>159){
                        wsItemSendMensajeResponse.setError(true);
                        wsItemSendMensajeResponse.setNumError(901);
                        wsItemSendMensajeResponse.setMsgError("Mensaje demasiado extenso. Se permite como máximo 160 carácteres.");
                        response.getWsItemSendMensajeResponse().add(wsItemSendMensajeResponse);
                        break;
                    }
                }
                
                try{
                    MovilMensaje movilMensajeDto = new MovilMensaje();

                    movilMensajeDto.setMensaje(item.getMensaje());
                    movilMensajeDto.setEmisorTipo(1);
                    movilMensajeDto.setIdEmpleadoEmisor((int)empleadoBO.getEmpleado().getIdEmpleado());
                    movilMensajeDto.setReceptorTipo(item.getReceptorTipo());
                    movilMensajeDto.setIdEmpleadoReceptor(item.getReceptorTipo()!=2?item.getIdEmpleadoReceptor():0);
                    try{
                        movilMensajeDto.setFechaEmision(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(getConn(), new Date(), (int)empleadoBO.getEmpleado().getIdEmpresa()).getTime());
                    }catch(Exception e){
                        movilMensajeDto.setFechaEmision(new Date());
                    }
                    movilMensajeDto.setRecibido(0);

                    MovilMensajePk movilMensajePk= new MovilMensajeDaoImpl(getConn()).insert(movilMensajeDto);

                    idCreado = movilMensajePk.getIdMovilMensaje();
                    wsItemSendMensajeResponse.setRastreadorMensaje(item.getRastreadorMensaje());
                    wsItemSendMensajeResponse.setIdObjetoCreado(idCreado);
                }catch(Exception ex){
                    wsItemSendMensajeResponse.setError(true);
                    wsItemSendMensajeResponse.setNumError(902);
                    wsItemSendMensajeResponse.setMsgError("Error al registrar mensaje en servidor: " + ex.toString());
                }
                
                response.getWsItemSendMensajeResponse().add(wsItemSendMensajeResponse);
            }
            
            //registramos ubicacion
            try{actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            
        }catch(Exception ex){
            ex.printStackTrace();
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al crear un mensaje. " + ex.getLocalizedMessage());
        }finally{                
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}              
        }
        
        return response;
    }
    
    
    public SendEstatusMensajesMovilResponse insertaEstatusMensajeMovilByEmpleado(String empleadoDtoRequestJSON, String sendEstatusMensajesMovilRequestJSON){
        System.out.println("MENSAJE:\n" + sendEstatusMensajesMovilRequestJSON);
        SendEstatusMensajesMovilResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest = null;
        //ItemMensajeMovilRequest crearMensajeMovilRequest = null;
        SendEstatusMensajesMovilRequest sendMensajesMovilRequest =null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            sendMensajesMovilRequest = gson.fromJson(sendEstatusMensajesMovilRequestJSON, SendEstatusMensajesMovilRequest.class);
            
            response = this.insertaEstatusMensajeMovilByEmpleado(empleadoDtoRequest,sendMensajesMovilRequest);
        }catch(Exception ex){
            response = new SendEstatusMensajesMovilResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }
    public SendEstatusMensajesMovilResponse insertaEstatusMensajeMovilByEmpleado(EmpleadoDtoRequest empleadoDtoRequest, SendEstatusMensajesMovilRequest sendEstatusMensajesMovilRequest){
        
        SendEstatusMensajesMovilResponse response = new SendEstatusMensajesMovilResponse();
        
        try{
            int idEmpresa = 0;
            
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())){
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
            }else{
               
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                return response;
            }
        
            if (sendEstatusMensajesMovilRequest==null){
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("Objeto de datos de estatus mensaje nulo.");
                return response;
            }
            
            //Matriz de la empresa
            String rfcEmpresaMatriz;
            Empresa empresaDto;
            try{
                empresaDto = new EmpresaBO(getConn()).getEmpresaMatriz(idEmpresa);
                rfcEmpresaMatriz = empresaDto.getRfc();
            }catch(Exception ex){
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("No se pudo recuperar la información de la empresa del empleado. Verifique con su administrador de sistema." + ex.toString());
                return response;
            }
            
            Configuration appConfig = new Configuration(); 
                
            for (ItemEstatusMensajeMovilRequest item:sendEstatusMensajesMovilRequest.getItemEstatusMensajeMovilRequest()){
                int idCreado=-1;
                WsItemSendMensajeResponse wsItemSendMensajeResponse = new WsItemSendMensajeResponse();
                if (item.getMensajeEstatus()==null){
                    wsItemSendMensajeResponse.setError(true);
                    wsItemSendMensajeResponse.setNumError(901);
                    wsItemSendMensajeResponse.setMsgError("Mensaje nulo.");
                    response.getWsItemSendMensajeResponse().add(wsItemSendMensajeResponse);
                    break;
                }else{
                    if (item.getMensajeEstatus().replaceAll(" ", "").trim().length()<=0){
                        wsItemSendMensajeResponse.setError(true);
                        wsItemSendMensajeResponse.setNumError(901);
                        wsItemSendMensajeResponse.setMsgError("Mensaje vacío.");
                        response.getWsItemSendMensajeResponse().add(wsItemSendMensajeResponse);
                        break;
                    }else if (item.getMensajeEstatus().length()>159){
                        wsItemSendMensajeResponse.setError(true);
                        wsItemSendMensajeResponse.setNumError(901);
                        wsItemSendMensajeResponse.setMsgError("Mensaje demasiado extenso. Se permite como máximo 160 carácteres.");
                        response.getWsItemSendMensajeResponse().add(wsItemSendMensajeResponse);
                        break;
                    }
                }
                
                try{
                    PosMovilEstatus movilEstatusDto = new PosMovilEstatus();

                    movilEstatusDto.setFechaEstatus(item.getFechaHoraEstatus());
                    movilEstatusDto.setIdEmpleado(empleadoBO.getEmpleado().getIdEmpleado());
                    movilEstatusDto.setIdEmpresa(idEmpresa);
                    movilEstatusDto.setLatitud(item.getLatitud());
                    movilEstatusDto.setLongitud(item.getLongitud());
                    movilEstatusDto.setMensajeEstatus(item.getMensajeEstatus());
                    
                    //Manejamos imagen de estatus
                    File archivoImagen = null;
                    if (item.getImagenEstatusBytesBase64()!=null){
                        if (item.getImagenEstatusBytesBase64().trim().length()>0){
                            try{
                                //Convertimos bytes en base64 a una imagen y la almacenamos en servidor
                                byte[] bytesImagen = Base64.decode(item.getImagenEstatusBytesBase64());

                                String ubicacionImagenesEstatus = appConfig.getApp_content_path() + rfcEmpresaMatriz +"/estatus/images/";
                                String nombreArchivoImagen = "img_estatus_"+DateManage.getDateHourString()+".jpg";

                                archivoImagen = FileManage.createFileFromByteArray(bytesImagen, ubicacionImagenesEstatus, nombreArchivoImagen);
                            }catch(Exception ex){
                                response.setError(true);
                                response.setNumError(901);
                                response.setMsgError("Los archivos de imagen no son correctos. " + ex.getLocalizedMessage());
                                System.out.println(response.getMsgError());
                                ex.printStackTrace();
                                return response;
                            }
                        }
                    }

                    if (archivoImagen!=null)
                        movilEstatusDto.setNombreImagen(archivoImagen.getName());

                    PosMovilEstatusPk movilEstatusPk= new PosMovilEstatusDaoImpl(getConn()).insert(movilEstatusDto);

                    idCreado = movilEstatusPk.getIdMovilEstatus();
                    wsItemSendMensajeResponse.setRastreadorMensaje(item.getRastreadorMensaje());
                    wsItemSendMensajeResponse.setIdObjetoCreado(idCreado);
                }catch(Exception ex){
                    wsItemSendMensajeResponse.setError(true);
                    wsItemSendMensajeResponse.setNumError(902);
                    wsItemSendMensajeResponse.setMsgError("Error al registrar mensaje de estatus en servidor: " + ex.toString());
                }finally{                
                    try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}            
                }
                
                response.getWsItemSendMensajeResponse().add(wsItemSendMensajeResponse);
            }
            
            try{
                //buscamos primero configuracion específica de empleado
                PosMovilEstatusParametros[] parametrosMovilEstatus = new PosMovilEstatusParametrosDaoImpl(getConn()).findWhereIdEmpleadoEquals(empleadoBO.getEmpleado().getIdEmpleado());
                //Si no encontramos conf especifica de empleado, buscamos la de la empresa a la q pertenece
                if (parametrosMovilEstatus.length<=0)
                    parametrosMovilEstatus = new PosMovilEstatusParametrosDaoImpl(getConn()).findWhereIdEmpresaEquals(idEmpresa);

                if (parametrosMovilEstatus.length>0){
                    response.setParametroTiempoMinutosActualiza(parametrosMovilEstatus[0].getTiempoMinutosActualiza());
                    response.setParametroTiempoMinutosRecordatorio(parametrosMovilEstatus[0].getTMinutosRecordatorio());
                }
            }catch(Exception ex){
            }
            
            //registramos ubicacion
            try{actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            
        }catch(Exception ex){
            ex.printStackTrace();
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al crear un mensaje. " + ex.getLocalizedMessage());
        }finally{                
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}              
        }
        
        return response;
    }
    
    public HistorialResponse insertaHistorialByEmpleado(String empleadoDtoRequestJSON, String insertaHistorialDtoRequestJson){
        HistorialResponse response;
        EmpleadoDtoRequest empleadoDtoRequest = null;
        InsertHistorialRequest insertHistorialDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            insertHistorialDtoRequest = gson.fromJson(insertaHistorialDtoRequestJson, InsertHistorialRequest.class);
            
            response = this.insertaHistorialByEmpleado(empleadoDtoRequest, insertHistorialDtoRequest);
        }catch(Exception ex){
            response = new HistorialResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        return response;
    }
    
     public HistorialResponse insertaHistorialByEmpleado(EmpleadoDtoRequest empleadoDtoRequest, InsertHistorialRequest insertaHistorialDtoRequest){
        HistorialResponse response = new HistorialResponse();
        int idCreado=-1;
        int idEmpresa = 0;
        HistorialMovil historialMovil = new HistorialMovil();
        HistorialMovilDaoImpl historialMovilDaoImpl = new HistorialMovilDaoImpl();
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())){
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
            }else{
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                return response;
            }
            
            UsuarioBO usuarioBO = empleadoBO.getUsuarioBO();
            if (usuarioBO==null){
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("No se pudo recuperar la información de Usuario del empleado para registrar el prospecto.");
                return response;
            }
            
            for(WsItemHistorial item : insertaHistorialDtoRequest.getWsItemHistorial()){
                HistorialResponse resp = new HistorialResponse();
                try{
                    historialMovil.setDescripcion(item.getDescripcion());
                    historialMovil.setFecha(item.getFecha());
                    historialMovil.setIdEmpresa(idEmpresa);
                    historialMovil.setImei(empleadoDtoRequest.getDispositivoIMEI());
                    historialMovil.setLocalizacion(item.getLocalizacion());
                    historialMovil.setMetodo(item.getMetodo());
                    historialMovil.setTipo(item.getTipo());
                    HistorialMovilPk historialPK = historialMovilDaoImpl.insert(historialMovil);
                    System.out.println("Id: " + historialPK.getIdHistorialMovil());
                    resp.setIdObjetoCreado(historialPK.getIdHistorialMovil());
                }catch(Exception ex){
                    resp.setError(true);
                    resp.setNumError(902);
                    resp.setMsgError("Error al realizar el historial: " + ex.toString());
                }
                response.getWsInsertResponse().add(resp);
            }
        }catch(Exception ex){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al actualizar el estatus de la tarea. " + ex.getLocalizedMessage());
            System.out.println("ERROR: \n" + ex.getMessage());
        }finally{   
                try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return response;
     }
    
        
    /**
     * Crea un nuevo registro de Cliente, a partir de los datos recabados
     * desde el formulario en aplicación móvil.
     * @param datosEmpleado String en formato JSON representando un objeto de tipo EmpleadoDtoRequest
     * @param datosCliente String en formato JSON representando un objeto de tipo CrearClienteRequest
     * @return Objeto de respuesta generico para inserciones vía Web Service, cadena en formato JSON representando un objeto WSResponseInsert
     */
    public WSResponseInsert insertaClienteByEmpleado(String empleadoDtoRequestJSON, String crearClienteRequestJSON){
        WSResponseInsert response;
                
        EmpleadoDtoRequest empleadoDtoRequest = null;
        CrearClienteRequest crearClienteRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            crearClienteRequest = gson.fromJson(crearClienteRequestJSON, CrearClienteRequest.class);
            
            response = this.insertaClienteByEmpleado(empleadoDtoRequest,crearClienteRequest);
        }catch(Exception ex){
            response = new WSResponseInsert();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }
    
    /**
     * Crea un nuevo registro de Cliente, a partir de los datos recabados
     * desde el formulario en aplicación móvil.
     * @param datosEmpleado EmpleadoDtoRequest Datos del empleado.
     * @param datosCliente CrearClienteRequest Datos del Cliente a crear.
     * @return Objeto de respuesta generico para inserciones vía Web Service
     */
    public WSResponseInsert insertaClienteByEmpleado(EmpleadoDtoRequest datosEmpleado, CrearClienteRequest datosCliente){
        WSResponseInsert response = new WSResponseInsert();
        
        int idCreado=-1;
        int idEmpresa = 0;
        
        Cliente clienteDto = new Cliente();
        ClienteDaoImpl clienteDaoImpl = new ClienteDaoImpl(getConn());
        
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(datosEmpleado.getEmpleadoUsuario(), datosEmpleado.getEmpleadoPassword())){
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
            }else{
//                try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                return response;
            }
            
            //Antes de procesar, verificamos si no fue registrado previamente,
            // de acuerdo al dato: Folio Cliente Movil
            try{
                Cliente[] clientesExistentes = new Cliente[0];
                if (!StringManage.getValidString(datosCliente.getFolioClienteMovil()).equals("")){
                    clientesExistentes = clienteDaoImpl.findWhereFolioClienteMovilEquals(datosCliente.getFolioClienteMovil());
                }
                    
                if (clientesExistentes.length>0){
                    if (clientesExistentes[0].getIdCliente()>0){
                        response.setIdObjetoCreado(clientesExistentes[0].getIdCliente());


                        //Actualizamos registro de Cliente con datos modificados
                        clientesExistentes[0].setNombreCliente(datosCliente.getNombreCliente());
                        clientesExistentes[0].setApellidoMaternoCliente(datosCliente.getApellidoMaternoCliente());
                        clientesExistentes[0].setApellidoPaternoCliente(datosCliente.getApellidoPaternoCliente());
                        clientesExistentes[0].setCalle(datosCliente.getCalle());
                        clientesExistentes[0].setCelular(datosCliente.getCelular());
                        clientesExistentes[0].setCodigoPostal(datosCliente.getCodigoPostal());
                        clientesExistentes[0].setColonia(datosCliente.getColonia());
                        clientesExistentes[0].setContacto(datosCliente.getContacto());
                        clientesExistentes[0].setCorreo(datosCliente.getCorreo());
                        clientesExistentes[0].setEstado(datosCliente.getEstado());
                        clientesExistentes[0].setExtension(datosCliente.getExtension());
                        clientesExistentes[0].setLada(datosCliente.getLada());
                        clientesExistentes[0].setMunicipio(datosCliente.getMunicipio());            
                        clientesExistentes[0].setNumero(datosCliente.getNumero());
                        clientesExistentes[0].setNumeroInterior(datosCliente.getNumeroInterior());
                        clientesExistentes[0].setPais(datosCliente.getPais());
                        clientesExistentes[0].setRazonSocial(datosCliente.getRazonSocial());
                        clientesExistentes[0].setRfcCliente(datosCliente.getRfcCliente());
                        clientesExistentes[0].setTelefono(datosCliente.getTelefono());
                        clientesExistentes[0].setLatitud(datosCliente.getLatitud());
                        clientesExistentes[0].setLongitud(datosCliente.getLongitud());
                        clientesExistentes[0].setDiasVisita(datosCliente.getDiasVisita());
                        clientesExistentes[0].setPerioricidad(datosCliente.getPerioricidad());
                        clientesExistentes[0].setNombreComercial(datosCliente.getNombreComercial());
                        clientesExistentes[0].setPermisoVentaCredito(datosCliente.getPermisoVentaCredito());
                        clientesExistentes[0].setIdClienteCategoria(datosCliente.getIdCategoria());
                        
                        ///---------------------------
                        //Borramos los campos personalizados para posteriormente insertarlos de new
                        try{
                            ClienteCampoAdicionalBO clienteCampoContenidoBO = new ClienteCampoAdicionalBO(getConn());
                            clienteCampoContenidoBO.deleteCamposAdicionalesCliente(clientesExistentes[0].getIdCliente());
                        }catch(Exception e){}
                        //insertamos de new los datos personalizados
                        ClienteCampoContenidoDaoImpl campoContenidoDaoImpl = new ClienteCampoContenidoDaoImpl(getConn());
                        for(WsItemClienteCampoContenidoRequest contenido : datosCliente.getListaContenido()){
                                ClienteCampoContenido clienteCampoContenido = new ClienteCampoContenido();
                                clienteCampoContenido.setIdClienteCampo(contenido.getIdClienteCampo());
                                clienteCampoContenido.setIdCliente(clientesExistentes[0].getIdCliente());
                                clienteCampoContenido.setValorLabel(contenido.getLabelValor());
                                campoContenidoDaoImpl.insert(clienteCampoContenido);
                        }                       
                        ///---------------------------

                        //Si tiene Estatus no disponible para facturar/mostrar 
                        if (clientesExistentes[0].getIdEstatus()==3){
                            //Verificamos si se le han asignado los datos básicos para facturar
                            if (gc.isRFC(clientesExistentes[0].getRfcCliente())
                                    && gc.isValidString(clientesExistentes[0].getPais(), 1, 200)){
                                clientesExistentes[0].setIdEstatus(1);
                            }
                        }

                        clienteDaoImpl.update(clientesExistentes[0].createPk(), clientesExistentes[0]);

                        response.setMsgError("actualizado:ok");

                        return response;
                    }
                }
            }catch(Exception ex){
                //ex.printStackTrace();
                throw new Exception("Error al intentar actualizar Cliente existente.");
            }
            
            Cliente lastCliente = clienteDaoImpl.findLast();
            idCreado = lastCliente.getIdCliente() + 1;
            
            clienteDto.setIdCliente(idCreado);
            clienteDto.setIdEmpresa(idEmpresa);
            
            clienteDto.setNombreCliente(datosCliente.getNombreCliente());
            clienteDto.setApellidoMaternoCliente(datosCliente.getApellidoMaternoCliente());
            clienteDto.setApellidoPaternoCliente(datosCliente.getApellidoPaternoCliente());
            clienteDto.setCalle(datosCliente.getCalle());
            clienteDto.setCelular(datosCliente.getCelular());
            clienteDto.setCodigoPostal(datosCliente.getCodigoPostal());
            clienteDto.setColonia(datosCliente.getColonia());
            clienteDto.setContacto(datosCliente.getContacto());
            clienteDto.setCorreo(datosCliente.getCorreo());
            clienteDto.setEstado(datosCliente.getEstado());
            clienteDto.setExtension(datosCliente.getExtension());
            clienteDto.setLada(datosCliente.getLada());
            clienteDto.setMunicipio(datosCliente.getMunicipio());            
            clienteDto.setNumero(datosCliente.getNumero());
            clienteDto.setNumeroInterior(datosCliente.getNumeroInterior());
            clienteDto.setPais(datosCliente.getPais());
            clienteDto.setRazonSocial(datosCliente.getRazonSocial());
            clienteDto.setRfcCliente(datosCliente.getRfcCliente());
            clienteDto.setTelefono(datosCliente.getTelefono());
            clienteDto.setLatitud(datosCliente.getLatitud());
            clienteDto.setLongitud(datosCliente.getLongitud());
            clienteDto.setFolioClienteMovil(datosCliente.getFolioClienteMovil());
            clienteDto.setDiasVisita(datosCliente.getDiasVisita());
            clienteDto.setPerioricidad(datosCliente.getPerioricidad());
            clienteDto.setNombreComercial(datosCliente.getNombreComercial());
            clienteDto.setPermisoVentaCredito(datosCliente.getPermisoVentaCredito());
            clienteDto.setIdClienteCategoria(datosCliente.getIdCategoria());
            try{
                clienteDto.setFechaRegistro(datosCliente.getFechaRegistro()!=null?datosCliente.getFechaRegistro() : (ZonaHorariaBO.DateZonaHorariaByIdEmpresa(getConn(), new Date(), (int)empleadoBO.getEmpleado().getIdEmpresa()).getTime()) );
            }catch(Exception e){
                clienteDto.setFechaRegistro(datosCliente.getFechaRegistro()!=null?datosCliente.getFechaRegistro() : new Date());
            }
            clienteDto.setIdUsuarioAlta(empleadoBO.getUsuarioBO().getUser().getIdUsuarios());
            
            if (datosCliente.getRfcCliente().equals("AAA010101")){
                clienteDto.setIdEstatus(3); //Estatus no disponible para facturar/mostrar
            }else{
                clienteDto.setIdEstatus(1); //Estatus activo
            }
                        
            ClientePk cliPk = clienteDaoImpl.insert(clienteDto);
            
            //Relacion Cliente-Vendedor 1-1
            SgfensClienteVendedor clienteVendedor = new SgfensClienteVendedor();
            //clienteVendedor.setIdCliente(idCreado);
            clienteVendedor.setIdCliente(cliPk.getIdCliente());
            clienteVendedor.setIdUsuarioVendedor(empleadoBO.getUsuarioBO().getUser().getIdUsuarios());
            new SgfensClienteVendedorDaoImpl(getConn()).insert(clienteVendedor);
            
            //idCreado = clienteDto.getIdCliente();
            idCreado = cliPk.getIdCliente();
            
            ///---------------------------
            //Borramos los campos personalizados para posteriormente insertarlos de new
            try{
                ClienteCampoAdicionalBO clienteCampoContenidoBO = new ClienteCampoAdicionalBO(getConn());
                clienteCampoContenidoBO.deleteCamposAdicionalesCliente(cliPk.getIdCliente());
            }catch(Exception e){}
            //insertamos de new los datos personalizados
            ClienteCampoContenidoDaoImpl campoContenidoDaoImpl = new ClienteCampoContenidoDaoImpl(getConn());
            for(WsItemClienteCampoContenidoRequest contenido : datosCliente.getListaContenido()){
                    ClienteCampoContenido clienteCampoContenido = new ClienteCampoContenido();
                    clienteCampoContenido.setIdClienteCampo(contenido.getIdClienteCampo());
                    clienteCampoContenido.setIdCliente(cliPk.getIdCliente());
                    clienteCampoContenido.setValorLabel(contenido.getLabelValor());
                    campoContenidoDaoImpl.insert(clienteCampoContenido);
            }                       
            ///---------------------------
            
            response.setIdObjetoCreado(idCreado);
            
            //registramos ubicacion
            try{actualizarUbicacionEmpleado(datosEmpleado);}catch(Exception ex){}
            
            //Consumo de Creditos Operacion
            try{
                BitacoraCreditosOperacionBO bcoBO = new BitacoraCreditosOperacionBO(getConn());
                bcoBO.registraDescuento(empleadoBO.getUsuarioBO().getUser(), 
                        BitacoraCreditosOperacionBO.CONSUMO_ACCION_WS_REGISTRO_CLIENTE, 
                        null, idCreado, 0, 0, 
                        "Registro Cliente WS Movil", null, true);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
        }catch(Exception e){
            e.printStackTrace();
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al crear un nuevo Cliente. " + e.getLocalizedMessage());
        }finally{   
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    public UpdateAgendaResponse updateAgendaEstatusResponse(String empleadoDtoRequestJson, String agendaDtoRequestJson){
        UpdateAgendaResponse response;
        EmpleadoDtoRequest empleadoDtoRequest = null;
        AgendaRequest agendaRequest = null;
        
        try{
             empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJson, EmpleadoDtoRequest.class);
             agendaRequest = gson.fromJson(agendaDtoRequestJson, AgendaRequest.class);
             
             response = this.updateAgendaEstatusResponse(empleadoDtoRequest, agendaRequest);
        }catch(Exception ex){
            response = new UpdateAgendaResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        return response;
    }
    
    public UpdateAgendaResponse updateAgendaEstatusResponse(EmpleadoDtoRequest empleadoDtoRequest, AgendaRequest agendaDtoRequest){
        UpdateAgendaResponse response = new UpdateAgendaResponse();
        int idEmpresa = 0 ;
        EmpleadoAgenda agendaDto = new EmpleadoAgenda();
        EmpleadoAgendaDaoImpl agendaDao = new EmpleadoAgendaDaoImpl();
        
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())){
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
            }else{
                try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                return response;
            }
            
            UsuarioBO usuarioBO = empleadoBO.getUsuarioBO();
            if (usuarioBO==null){
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("No se pudo recuperar la información de Usuario del empleado para registrar el prospecto.");
                return response;
            }
            
            for(WsItemAgenda task : agendaDtoRequest.getWsItemAgenda()){
                WsItemUpdateAgenda wsItemResponse = new WsItemUpdateAgenda();
                try{
                    
                    agendaDto.setIdEmpleado(task.getIdEmpleado());
                    agendaDto.setIdEstatus(task.getIdEstatus());
                    agendaDto.setFechaCreacion(task.getFechaCreacion());
                    agendaDto.setFechaProgramada(task.getFechaProgramada());
                    agendaDto.setFechaEjecucion(task.getFechaEjecucion());
                    agendaDto.setNombreTarea(task.getNombreTarea());
                    agendaDto.setDescripcionTarea(task.getDescripcionTarea());
                    
                    if(task.getIdAgenda() > 0){
                        //Se actualiza la tarea
                        agendaDto.setIdAgenda(task.getIdAgenda());
                        
                        agendaDao.update(agendaDto.createPk(), agendaDto);
                        wsItemResponse.setIdAgenda(agendaDto.getIdAgenda());
                        wsItemResponse.setIdEmpleado(empleadoBO.getEmpleado().getIdEmpleado());
                        wsItemResponse.setIdCliente(task.getIdCliente());
                        wsItemResponse.setIdMovilAgenda(task.getIdMovilAgenda());
                    }else{
                        //Nueva tarea en la agenda
                        EmpleadoAgendaPk agendaPk = agendaDao.insert(agendaDto);
                        System.out.println("Id: " + agendaPk.getIdAgenda());
                        wsItemResponse.setIdAgenda(agendaPk.getIdAgenda());
                        wsItemResponse.setIdEmpleado(agendaDto.getIdEmpleado());
                        wsItemResponse.setIdCliente(task.getIdCliente());
                        wsItemResponse.setIdMovilAgenda(task.getIdMovilAgenda());
                    }
                    
                    
                    
                }catch(Exception ex){
                    wsItemResponse.setError(true);
                    wsItemResponse.setNumError(902);
                    wsItemResponse.setMsgError("Error al realizar la visita: " + ex.toString());
                }finally{   
                    try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
                }
                
                response.getWsItemUpdateAgenda().add(wsItemResponse);
            }
            
        }catch(Exception ex){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al actualizar el estatus de la tarea. " + ex.getLocalizedMessage());
            System.out.println("ERROR: \n" + ex.getMessage());
        }
        return response;
    }
    
    public InsertRegistroVisitaClienteResponse insertRegistroVisitaClienteResponse(String empleadoDtoRequestJson, String registroVisitaDtoRequestJson){
        InsertRegistroVisitaClienteResponse response;
        
        EmpleadoDtoRequest empleadoDtoRequest = null;
        RegistroVisitaDtoRequest registroVisitaDtoRequest = null;
        
        try{
            
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJson, EmpleadoDtoRequest.class);
            registroVisitaDtoRequest = gson.fromJson(registroVisitaDtoRequestJson, RegistroVisitaDtoRequest.class);
            
            System.out.println("JSON:\n" + registroVisitaDtoRequest);
            
            response = this.insertRegistroVisitaClienteResponse(empleadoDtoRequest, registroVisitaDtoRequest);
            
        }catch(Exception ex){
            response = new InsertRegistroVisitaClienteResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        return response;
    }
    
    public InsertRegistroVisitaClienteResponse insertRegistroVisitaClienteResponse(EmpleadoDtoRequest empleadoData, RegistroVisitaDtoRequest registroVisitaData){
        
        InsertRegistroVisitaClienteResponse response = new InsertRegistroVisitaClienteResponse();
        int idEmpresa = 0 ;
        
        SgfensVisitaCliente visitaDto = new SgfensVisitaCliente();
        SgfensVisitaClienteDaoImpl visitaDao = new SgfensVisitaClienteDaoImpl();
        
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            
            if (empleadoBO.login(empleadoData.getEmpleadoUsuario(), empleadoData.getEmpleadoPassword())){
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
            }else{
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                return response;
            }
            
            UsuarioBO usuarioBO = empleadoBO.getUsuarioBO();
            if (usuarioBO==null){
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("No se pudo recuperar la información de Usuario del empleado para registrar el prospecto.");
                return response;
            }
            
            for(WsItemRegistroVisitaRequest visita : registroVisitaData.getWsItemRegistroVisita()){
                
                WsItemRegistroVisitaCliente wsItemResponse = new WsItemRegistroVisitaCliente();
                try{
                    visitaDto.setIdEmpresa(visita.getIdEmpresa());
                    visitaDto.setIdEmpleadoVendedor(visita.getIdEmpleado());
                    visitaDto.setIdCliente(visita.getIdCliente());
                    visitaDto.setLatitud(visita.getLatitud());
                    visitaDto.setLongitud(visita.getLongitud());
                    visitaDto.setFechaHora(visita.getFechaHora());
                    visitaDto.setIdOpcion(visita.getIdOpcion());
                    visitaDto.setComentarios(visita.getComentarios());

                    System.out.println("ID DE LA VISITA: \n" + visita.getIdRegistroVisita());

                    if(visita.getIdRegistroVisita() < 0){
                        SgfensVisitaClientePk visitaPk = visitaDao.insert(visitaDto);
                        System.out.println("Id: " + visitaPk.getIdVisita());
                        wsItemResponse.setIdRegistroVisita(visitaPk.getIdVisita());
                        wsItemResponse.setIdCliente(visitaDto.getIdCliente());
                        
                        //Consumo de Creditos Operacion
                        try{
                            BitacoraCreditosOperacionBO bcoBO = new BitacoraCreditosOperacionBO(getConn());
                            bcoBO.registraDescuento(empleadoBO.getUsuarioBO().getUser(), 
                                    BitacoraCreditosOperacionBO.CONSUMO_ACCION_WS_REGISTRO_VISITA, 
                                    visita.getFechaHora(), visita.getIdCliente(), 0, 0, 
                                    "Registro Visita WS Movil", null, true);
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }else{
                        visitaDto.setIdVisita(visita.getIdRegistroVisita());

                        visitaDao.update(visitaDto.createPk(), visitaDto);
                        wsItemResponse.setIdRegistroVisita(visitaDto.getIdVisita());
                    } 
                }catch(Exception ex){
                    wsItemResponse.setError(true);
                    wsItemResponse.setNumError(902);
                    wsItemResponse.setMsgError("Error al realizar la visita: " + ex.toString());
                }
                System.out.println("Antes de agregarlo");
                try{
                    response.getWsItemRegistroVisita().add(wsItemResponse);
                }catch(Exception ex){
                    System.out.println("El error: " + ex.getMessage());
                    ex.printStackTrace();
                }
                System.out.println("Después de agregarlo");
            }
             //registramos ubicacion
            try{actualizarUbicacionEmpleado(empleadoData);}catch(Exception ex){}
            
        }catch(Exception ex){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al realizar la visita. " + ex.getLocalizedMessage());
            System.out.println("ERROR: \n" + ex.getMessage());
        }finally{   
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return response;
    }
    
     public WSResponseInsert insertRegistroProveedorResponse(String empleadoDtoRequestJSON, String registroProveedorDtoRequestJson){
        WSResponseInsert response;
                
        EmpleadoDtoRequest empleadoDtoRequest = null;
        WsItemProveedor crearProveedorRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            crearProveedorRequest = gson.fromJson(registroProveedorDtoRequestJson, WsItemProveedor.class);
            
            response = this.insertaProveedor(empleadoDtoRequest,crearProveedorRequest);
        }catch(Exception ex){
            response = new WSResponseInsert();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }
        
    public WSResponseInsert insertaProveedor(EmpleadoDtoRequest datosEmpleado, WsItemProveedor datosProveedor){
        WSResponseInsert response = new WSResponseInsert();
        
        int idCreado=-1;
        int idEmpresa = 0;
        
        SgfensProveedor proveedorDto = new SgfensProveedor();
        SgfensProveedorDaoImpl sgfensProveedorDaoImpl = new SgfensProveedorDaoImpl(getConn());
        Configuration appConfig = new Configuration();
        
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(datosEmpleado.getEmpleadoUsuario(), datosEmpleado.getEmpleadoPassword())){
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
            }else{
//                try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                return response;
            }
            
            //Matriz de la empresa
            Empresa empresaDto;
            String rfcEmpresaMatriz = "";
            try{
                empresaDto = new EmpresaBO(getConn()).getEmpresaMatriz(idEmpresa);
                rfcEmpresaMatriz = empresaDto.getRfc();
            }catch(Exception ex){
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("No se pudo recuperar la información de la empresa del empleado. Verifique con su administrador de sistema." + ex.toString());
                return response;
            }
            
            //Antes de procesar, verificamos si no fue registrado previamente,
            // de acuerdo al dato: Numero de proveedor
            try{
                SgfensProveedor[] ProveedoresExistentes = new SgfensProveedor[0];
                if (!StringManage.getValidString(datosProveedor.getNumeroProveedor()).equals("")){
                    //ProveedoresExistentes = clienteDaoImpl.findWhereNumeroProveedorEquals(datosProveedor.getNumeroProveedor());
                    if(datosProveedor.getNumeroProveedor() != null && !datosProveedor.getNumeroProveedor().trim().equals("")){
                        ProveedoresExistentes = sgfensProveedorDaoImpl.findByDynamicWhere("NUMERO_PROVEEDOR = '" + datosProveedor.getNumeroProveedor() + "' AND ID_EMPRESA = " + idEmpresa, null);
                    }else{
                        ProveedoresExistentes = sgfensProveedorDaoImpl.findByDynamicWhere("RAZON_SOCIAL = '" + datosProveedor.getRazonSocial() + "' AND ID_EMPRESA = " + idEmpresa, null);
                    }
                }
                    
                if (ProveedoresExistentes.length>0){
                    if (ProveedoresExistentes[0].getIdProveedor()>0){
                        response.setIdObjetoCreado(ProveedoresExistentes[0].getIdProveedor());


                        //Actualizamos registro de proveedor con datos modificados
                        ProveedoresExistentes[0].setNumeroProveedor(datosProveedor.getNumeroProveedor());
                        ProveedoresExistentes[0].setRfc(datosProveedor.getRfcProveedor());
                        ProveedoresExistentes[0].setRazonSocial(datosProveedor.getRazonSocial());
                        ProveedoresExistentes[0].setCalle(datosProveedor.getCalle());
                        ProveedoresExistentes[0].setNumero(datosProveedor.getNumero());
                        ProveedoresExistentes[0].setNumeroInterior(datosProveedor.getNumeroInterior());
                        ProveedoresExistentes[0].setCodigoPostal(datosProveedor.getCodigoPostal());
                        ProveedoresExistentes[0].setColonia(datosProveedor.getColonia());
                        ProveedoresExistentes[0].setPais(datosProveedor.getPais());
                        ProveedoresExistentes[0].setEstado(datosProveedor.getEstado());
                        ProveedoresExistentes[0].setMunicipio(datosProveedor.getMunicipio());
                        ProveedoresExistentes[0].setLada(datosProveedor.getLada());
                        ProveedoresExistentes[0].setTelefono(datosProveedor.getTelefono());
                        ProveedoresExistentes[0].setExtension(datosProveedor.getExtension());                        
                        ProveedoresExistentes[0].setCelular(datosProveedor.getCelular());
                        ProveedoresExistentes[0].setCorreo(datosProveedor.getCorreo());
                        ProveedoresExistentes[0].setContacto(datosProveedor.getContacto());
                        ProveedoresExistentes[0].setIdEstatus(1);
                        ProveedoresExistentes[0].setDescripcion(datosProveedor.getDescripcion());
                        ProveedoresExistentes[0].setNombreEmpresa(datosProveedor.getNombreEmpresa());
                        ProveedoresExistentes[0].setLatitud(datosProveedor.getLatitud());
                        ProveedoresExistentes[0].setLongitud(datosProveedor.getLongitud());
                        ProveedoresExistentes[0].setIdCategoriaProveedor(datosProveedor.getIdCategoriaProveedor());
                        
                        File archivoImagenProveedor = null;

                        if (datosProveedor.getImagenProveedorBytesBase64()!=null){
                            if (datosProveedor.getImagenProveedorBytesBase64().trim().length()>0){
                                try{
                                    //Convertimos bytes en base64 a una imagen y la almacenamos en servidor
                                    byte[] bytesImagenProveedor = Base64.decode(datosProveedor.getImagenProveedorBytesBase64());

                                    String ubicacionImagenesProveedors = appConfig.getApp_content_path() + rfcEmpresaMatriz +"/ImagenProveedor/";
                                    File folder = new File(ubicacionImagenesProveedors);
                                    folder.mkdirs();
                                    String nombreArchivoImagenProveedor = "img_proveedor_"+DateManage.getDateHourString()+".jpg";

                                    archivoImagenProveedor = FileManage.createFileFromByteArray(bytesImagenProveedor, ubicacionImagenesProveedors, nombreArchivoImagenProveedor);
                                }catch(Exception ex){
                                    response.setError(true);
                                    response.setNumError(901);
                                    response.setMsgError("Los archivos de imagen no son correctos. " + ex.getLocalizedMessage());
                                    System.out.println(response.getMsgError());
                                    ex.printStackTrace();
                                    return response;
                                }
                            }
                        }

                        if (archivoImagenProveedor!=null)
                            ProveedoresExistentes[0].setNombreImagenProveedor(archivoImagenProveedor.getName());
                                                          

                        sgfensProveedorDaoImpl.update(ProveedoresExistentes[0].createPk(), ProveedoresExistentes[0]);

                        response.setMsgError("actualizado:ok");

                        return response;
                    }
                }
            }catch(Exception ex){
                ex.printStackTrace();
                throw new Exception("Error al intentar actualizar Proveedor existente.");
            }
            
            proveedorDto.setIdEmpresa(idEmpresa);
            proveedorDto.setNumeroProveedor(datosProveedor.getNumeroProveedor());
            proveedorDto.setRfc(datosProveedor.getRfcProveedor());
            proveedorDto.setRazonSocial(datosProveedor.getRazonSocial());
            proveedorDto.setCalle(datosProveedor.getCalle());
            proveedorDto.setNumero(datosProveedor.getNumero());
            proveedorDto.setNumeroInterior(datosProveedor.getNumeroInterior());
            proveedorDto.setCodigoPostal(datosProveedor.getCodigoPostal());
            proveedorDto.setColonia(datosProveedor.getColonia());
            proveedorDto.setPais(datosProveedor.getPais());
            proveedorDto.setEstado(datosProveedor.getEstado());
            proveedorDto.setMunicipio(datosProveedor.getMunicipio());
            proveedorDto.setLada(datosProveedor.getLada());
            proveedorDto.setTelefono(datosProveedor.getTelefono());
            proveedorDto.setExtension(datosProveedor.getExtension());                        
            proveedorDto.setCelular(datosProveedor.getCelular());
            proveedorDto.setCorreo(datosProveedor.getCorreo());
            proveedorDto.setContacto(datosProveedor.getContacto());
            proveedorDto.setIdEstatus(1);
            proveedorDto.setDescripcion(datosProveedor.getDescripcion());
            proveedorDto.setNombreEmpresa(datosProveedor.getNombreEmpresa());
            proveedorDto.setLatitud(datosProveedor.getLatitud());
            proveedorDto.setLongitud(datosProveedor.getLongitud());
            proveedorDto.setIdCategoriaProveedor(datosProveedor.getIdCategoriaProveedor());
            
            File archivoImagenProveedor = null;

                            if (datosProveedor.getImagenProveedorBytesBase64()!=null){
                                if (datosProveedor.getImagenProveedorBytesBase64().trim().length()>0){
                                    try{
                                        //Convertimos bytes en base64 a una imagen y la almacenamos en servidor
                                        byte[] bytesImagenProveedor = Base64.decode(datosProveedor.getImagenProveedorBytesBase64());

                                        String ubicacionImagenesProveedors = appConfig.getApp_content_path() + rfcEmpresaMatriz +"/ImagenProveedor/";
                                        String nombreArchivoImagenProveedor = "img_proveedor_"+DateManage.getDateHourString()+".jpg";
                                        File folder = new File(ubicacionImagenesProveedors);
                                        folder.mkdirs();        
                                        archivoImagenProveedor = FileManage.createFileFromByteArray(bytesImagenProveedor, ubicacionImagenesProveedors, nombreArchivoImagenProveedor);
                                    }catch(Exception ex){
                                        response.setError(true);
                                        response.setNumError(901);
                                        response.setMsgError("Los archivos de imagen no son correctos. " + ex.getLocalizedMessage());
                                        System.out.println(response.getMsgError());
                                        ex.printStackTrace();
                                        return response;
                                    }
                                }
                            }

                            if (archivoImagenProveedor!=null)
                                proveedorDto.setNombreImagenProveedor(archivoImagenProveedor.getName());
                        
            SgfensProveedorPk provPk = new SgfensProveedorDaoImpl(getConn()).insert(proveedorDto);
            
            idCreado = provPk.getIdProveedor();
            
            response.setIdObjetoCreado(idCreado);
            
            //registramos ubicacion
            try{actualizarUbicacionEmpleado(datosEmpleado);}catch(Exception ex){}
            
            //Consumo de Creditos Operacion
            try{
                BitacoraCreditosOperacionBO bcoBO = new BitacoraCreditosOperacionBO(getConn());
                bcoBO.registraDescuento(empleadoBO.getUsuarioBO().getUser(), 
                        BitacoraCreditosOperacionBO.CONSUMO_ACCION_WS_REGISTRO_CLIENTE, 
                        null, idCreado, 0, 0, 
                        "Registro Proveesor WS Movil", null, true);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
        }catch(Exception e){
            e.printStackTrace();
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al crear un nuevo Cliente. " + e.getLocalizedMessage());
        }finally{   
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    public WSResponseInsert insertRegistroProveedorProductoResponse(String empleadoDtoRequestJSON, String registroProveedorProductoDtoRequestJson){
        WSResponseInsert response;
                
        EmpleadoDtoRequest empleadoDtoRequest = null;
        WsItemProveedorConceptoServicio crearProveedorProductoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            crearProveedorProductoRequest = gson.fromJson(registroProveedorProductoDtoRequestJson, WsItemProveedorConceptoServicio.class);
            
            response = this.insertaProveedorProducto(empleadoDtoRequest,crearProveedorProductoRequest);
        }catch(Exception ex){
            response = new WSResponseInsert();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }
        
    public WSResponseInsert insertaProveedorProducto(EmpleadoDtoRequest datosEmpleado, WsItemProveedorConceptoServicio datosProveedorProducto){
        WSResponseInsert response = new WSResponseInsert();
        
        int idCreado=-1;
        int idEmpresa = 0;
        
        SgfensProveedorProducto proveedorProductoDto = new SgfensProveedorProducto();
        SgfensProveedorProductoDaoImpl sgfensProveedorDaoImpl = new SgfensProveedorProductoDaoImpl(getConn());
        Configuration appConfig = new Configuration();
        
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(datosEmpleado.getEmpleadoUsuario(), datosEmpleado.getEmpleadoPassword())){
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
            }else{
//                try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                return response;
            }
            
            //Matriz de la empresa
            Empresa empresaDto;
            String rfcEmpresaMatriz = "";
            try{
                empresaDto = new EmpresaBO(getConn()).getEmpresaMatriz(idEmpresa);
                rfcEmpresaMatriz = empresaDto.getRfc();
            }catch(Exception ex){
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("No se pudo recuperar la información de la empresa del empleado. Verifique con su administrador de sistema." + ex.toString());
                return response;
            }
            
            //Antes de procesar, verificamos si no fue registrado previamente,
            // de acuerdo al dato: Numero de proveedor
            try{
                SgfensProveedorProducto[] proveedoresProductosExistentes = new SgfensProveedorProducto[0];
                if (datosProveedorProducto.getIdProductoServicio() > 0 && datosProveedorProducto.getIdProveedor() > 0){                                        
                        proveedoresProductosExistentes = sgfensProveedorDaoImpl.findByDynamicWhere("ID_SGFENS_PRODUCTO_SERVICIO = " + datosProveedorProducto.getIdProductoServicio() + " AND ID_SGFENS_PRODUCTO_PROVEEDOR = " + datosProveedorProducto.getIdProveedor() , null);                    
                }
                    
                if (proveedoresProductosExistentes.length>0){
                    if (proveedoresProductosExistentes[0].getIdProveedorProducto()>0){
                        response.setIdObjetoCreado(proveedoresProductosExistentes[0].getIdProveedorProducto());


                        //Actualizamos registro de proveedor con datos modificados
                        proveedoresProductosExistentes[0].setVolumenDisponible(datosProveedorProducto.getVolumenDisponible());
                        proveedoresProductosExistentes[0].setCaducidad(datosProveedorProducto.getCaducidadDate());
                        proveedoresProductosExistentes[0].setFechaDisponibilidad(datosProveedorProducto.getFechaDisponibilidadDate());
                        proveedoresProductosExistentes[0].setUnidad(datosProveedorProducto.getUnidad());
                        proveedoresProductosExistentes[0].setPrecioMedioMayoreo( (new BigDecimal(datosProveedorProducto.getPrecioMedioMayoreo()).setScale(2, RoundingMode.HALF_UP)).doubleValue() );
                        proveedoresProductosExistentes[0].setMinMedioMayoreo((new BigDecimal(datosProveedorProducto.getMinMedioMayoreo()).setScale(2, RoundingMode.HALF_UP)).doubleValue());
                        proveedoresProductosExistentes[0].setMaxMedioMayoreo((new BigDecimal(datosProveedorProducto.getMaxMedioMayoreo()).setScale(2, RoundingMode.HALF_UP)).doubleValue());
                        proveedoresProductosExistentes[0].setPrecioMenudeo((new BigDecimal(datosProveedorProducto.getPrecioMenudeo()).setScale(2, RoundingMode.HALF_UP)).doubleValue());
                        proveedoresProductosExistentes[0].setMaxMenudeo((new BigDecimal(datosProveedorProducto.getMaxMenudeo()).setScale(2, RoundingMode.HALF_UP)).doubleValue());
                        proveedoresProductosExistentes[0].setPrecioMayoreo((new BigDecimal(datosProveedorProducto.getPrecioMayoreo()).setScale(2, RoundingMode.HALF_UP)).doubleValue());
                        proveedoresProductosExistentes[0].setMinMayoreo((new BigDecimal(datosProveedorProducto.getMinMayoreo()).setScale(2, RoundingMode.HALF_UP)).doubleValue());
                        proveedoresProductosExistentes[0].setIdCategoria(datosProveedorProducto.getIdCategoria());
                        
                        File archivoImagenProveedorProducto = null;

                        if (datosProveedorProducto.getImagenProveedorProductoBytesBase64()!=null){
                            if (datosProveedorProducto.getImagenProveedorProductoBytesBase64().trim().length()>0){
                                try{
                                    //Convertimos bytes en base64 a una imagen y la almacenamos en servidor
                                    byte[] bytesImagenProveedor = Base64.decode(datosProveedorProducto.getImagenProveedorProductoBytesBase64());

                                    String ubicacionImagenesProveedors = appConfig.getApp_content_path() + rfcEmpresaMatriz +"/ImagenConcepto/";
                                    String nombreArchivoImagenProveedor = "img_concepto_"+DateManage.getDateHourString()+".jpg";

                                    archivoImagenProveedorProducto = FileManage.createFileFromByteArray(bytesImagenProveedor, ubicacionImagenesProveedors, nombreArchivoImagenProveedor);
                                }catch(Exception ex){
                                    response.setError(true);
                                    response.setNumError(901);
                                    response.setMsgError("Los archivos de imagen no son correctos. " + ex.getLocalizedMessage());
                                    System.out.println(response.getMsgError());
                                    ex.printStackTrace();
                                    return response;
                                }
                            }
                        }

                        if (archivoImagenProveedorProducto!=null)
                            proveedoresProductosExistentes[0].setNombreImagenProductoServicio(archivoImagenProveedorProducto.getName());
                                                          

                        sgfensProveedorDaoImpl.update(proveedoresProductosExistentes[0].createPk(), proveedoresProductosExistentes[0]);

                        response.setMsgError("actualizado:ok");

                        return response;
                    }
                }
            }catch(Exception ex){
                //ex.printStackTrace();
                throw new Exception("Error al intentar actualizar Producto del Proveedor existente.");
            }
            
            proveedorProductoDto.setIdSgfensProductoServicio(datosProveedorProducto.getIdProductoServicio());
            proveedorProductoDto.setIdSgfensProductoProveedor(datosProveedorProducto.getIdProveedor());
            proveedorProductoDto.setVolumenDisponible(datosProveedorProducto.getVolumenDisponible());
            proveedorProductoDto.setCaducidad(datosProveedorProducto.getCaducidadDate());
            proveedorProductoDto.setFechaDisponibilidad(datosProveedorProducto.getFechaDisponibilidadDate());
            proveedorProductoDto.setUnidad(datosProveedorProducto.getUnidad());
            proveedorProductoDto.setPrecioMedioMayoreo( (new BigDecimal(datosProveedorProducto.getPrecioMedioMayoreo()).setScale(2, RoundingMode.HALF_UP)).doubleValue() );
            proveedorProductoDto.setMinMedioMayoreo((new BigDecimal(datosProveedorProducto.getMinMedioMayoreo()).setScale(2, RoundingMode.HALF_UP)).doubleValue());
            proveedorProductoDto.setMaxMedioMayoreo((new BigDecimal(datosProveedorProducto.getMaxMedioMayoreo()).setScale(2, RoundingMode.HALF_UP)).doubleValue());
            proveedorProductoDto.setPrecioMenudeo((new BigDecimal(datosProveedorProducto.getPrecioMenudeo()).setScale(2, RoundingMode.HALF_UP)).doubleValue());
            proveedorProductoDto.setMaxMenudeo((new BigDecimal(datosProveedorProducto.getMaxMenudeo()).setScale(2, RoundingMode.HALF_UP)).doubleValue());
            proveedorProductoDto.setPrecioMayoreo((new BigDecimal(datosProveedorProducto.getPrecioMayoreo()).setScale(2, RoundingMode.HALF_UP)).doubleValue());
            proveedorProductoDto.setMinMayoreo((new BigDecimal(datosProveedorProducto.getMinMayoreo()).setScale(2, RoundingMode.HALF_UP)).doubleValue());
            proveedorProductoDto.setIdCategoria(datosProveedorProducto.getIdCategoria());
                        
                File archivoImagenProveedorProducto = null;

                if (datosProveedorProducto.getImagenProveedorProductoBytesBase64()!=null){
                    if (datosProveedorProducto.getImagenProveedorProductoBytesBase64().trim().length()>0){
                        try{
                            //Convertimos bytes en base64 a una imagen y la almacenamos en servidor
                            byte[] bytesImagenProveedor = Base64.decode(datosProveedorProducto.getImagenProveedorProductoBytesBase64());

                            String ubicacionImagenesProveedors = appConfig.getApp_content_path() + rfcEmpresaMatriz +"/ImagenConcepto/";
                            String nombreArchivoImagenProveedor = "img_concepto_"+DateManage.getDateHourString()+".jpg";

                            archivoImagenProveedorProducto = FileManage.createFileFromByteArray(bytesImagenProveedor, ubicacionImagenesProveedors, nombreArchivoImagenProveedor);
                        }catch(Exception ex){
                            response.setError(true);
                            response.setNumError(901);
                            response.setMsgError("Los archivos de imagen no son correctos. " + ex.getLocalizedMessage());
                            System.out.println(response.getMsgError());
                            ex.printStackTrace();
                            return response;
                        }
                    }
                }

                if (archivoImagenProveedorProducto!=null)
                    proveedorProductoDto.setNombreImagenProductoServicio(archivoImagenProveedorProducto.getName());
                        
            SgfensProveedorProductoPk provPk = new SgfensProveedorProductoDaoImpl(getConn()).insert(proveedorProductoDto);
            
            idCreado = provPk.getIdProveedorProducto();
            
            response.setIdObjetoCreado(idCreado);
            
            //registramos ubicacion
            try{actualizarUbicacionEmpleado(datosEmpleado);}catch(Exception ex){}
            
            //Consumo de Creditos Operacion
            try{
                BitacoraCreditosOperacionBO bcoBO = new BitacoraCreditosOperacionBO(getConn());
                bcoBO.registraDescuento(empleadoBO.getUsuarioBO().getUser(), 
                        BitacoraCreditosOperacionBO.CONSUMO_ACCION_WS_REGISTRO_CLIENTE, 
                        null, idCreado, 0, 0, 
                        "Registro Proveesor WS Movil", null, true);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
        }catch(Exception e){
            e.printStackTrace();
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al crear un nuevo Cliente. " + e.getLocalizedMessage());
        }finally{   
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    /**
     * Inserta una devolucion o cambio
     * procedente de la app movil
     * @param empleadoDtoRequestJson
     * @param devolucionesDtoRequestJson
     * @param empleadoDtoRequestJSON
     * @param devolucionDtoRequestJSON
     * @return 
     */
    public InsertPedidoDevolucionesResponse insertPedidosDevolucionesVentas(String empleadoDtoRequestJson, String devolucionesDtoRequestJson){
        
        InsertPedidoDevolucionesResponse response;
        
        EmpleadoDtoRequest empleadoDtoRequest = null;
        DevolucionesDtoRequest devolucionesDtoRequest = null;
        try
        {
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJson, EmpleadoDtoRequest.class);
            
            devolucionesDtoRequest = gson.fromJson(devolucionesDtoRequestJson, DevolucionesDtoRequest.class);
            
            System.out.println("JSON:\n" + devolucionesDtoRequest);
            
            response = this.insertPedidosDevolucionesVentas(empleadoDtoRequest, devolucionesDtoRequest);
        }catch(Exception ex){
            response = new InsertPedidoDevolucionesResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }
    
    public InsertPedidoDevolucionesResponse insertPedidosDevolucionesVentas(EmpleadoDtoRequest datosEmpleado, DevolucionesDtoRequest devolucionDtoRequest){
        
        InsertPedidoDevolucionesResponse response = new InsertPedidoDevolucionesResponse();
          
        int idEmpresa = 0 ;
        
        SgfensPedidoDevolucionCambio devolucionDto;
        SgfensPedidoDevolucionCambioDaoImpl devolucionDao = new SgfensPedidoDevolucionCambioDaoImpl(getConn());
        
        try{
            
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(datosEmpleado.getEmpleadoUsuario(), datosEmpleado.getEmpleadoPassword())){
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
            }else{
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                return response;
            }
            
            UsuarioBO usuarioBO = empleadoBO.getUsuarioBO();
            if (usuarioBO==null){
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("No se pudo recuperar la información de Usuario del empleado para registrar el prospecto.");
                return response;
            }
            
            int idPedidoUltimo = 0;
            for(WsItemPedidoDevolucionCambioRequest item:devolucionDtoRequest.getWsITemDevolucion()){
                
                WsItemPedidosDevoluciones wsItemResponse = new WsItemPedidosDevoluciones();
                devolucionDto = new SgfensPedidoDevolucionCambio();
                
                try{
                    
                    //Buscamos primero alguna coincidencia por Folio Movil
                    SgfensPedidoDevolucionCambio[] devolucionesExistentes = new SgfensPedidoDevolucionCambio[0];
                    if (!StringManage.getValidString(item.getFolioMovil()).equals("")){
                        devolucionesExistentes = devolucionDao.findWhereFolioMovilEquals(item.getFolioMovil());
                    }

                    if (devolucionesExistentes.length>0){
                        if (devolucionesExistentes[0].getIdPedidoDevolCambio()>0){
                            devolucionDto = devolucionesExistentes[0];
                        }
                    }
                    
                    if(devolucionDto.getIdPedidoDevolCambio()<= 0){
                        //Nuevo
                        devolucionDto.setIdEstatus(1);
                        devolucionDto.setIdEmpresa(idEmpresa);
                        devolucionDto.setIdEmpleado(empleadoBO.getEmpleado().getIdEmpleado());
                        devolucionDto.setIdConcepto(item.getIdConcepto());
                        devolucionDto.setIdTipo(item.getIdTipo());
                        devolucionDto.setAptoParaVenta(item.getAptoParaVenta());
                        devolucionDto.setNoAptoParaVenta(item.getNoAptoParaVenta());
                        devolucionDto.setIdClasificacion(item.getIdClasificacion());
                        devolucionDto.setDescripcionClasificacion(item.getDescripcionClasificacion());
                        devolucionDto.setIdConcepto(item.getIdConcepto());
                        devolucionDto.setFecha(item.getFecha());
                        devolucionDto.setIdConceptoEntregado(item.getIdConceptoEntregado());
                        devolucionDto.setIdPedido(item.getIdPedido());
                        devolucionDto.setCantidadDevuelta(item.getCantidadDevuelta());
                        devolucionDto.setFolioMovil(item.getFolioMovil());
                        devolucionDto.setMontoResultante(item.getMontoResultante());
                        devolucionDto.setDiferenciaFavor(item.getDiferenciaFavor());
                        devolucionDto.setDevolucionEfctivo(item.getDevolucionEfectivo());
                        
                        
                        //Actualizamos saldo cliente 
                        
                        try{
                            
                            // Casos 0 y 3 total = 0 , no se aplica ninguna accion.
                            double saldo = 0;
                            SgfensPedido pedidoDto = new SGPedidoBO(item.getIdPedido(), this.conn).getPedido();                            
                            Cliente clienteDto = new ClienteBO(pedidoDto.getIdCliente(),this.conn).getCliente();
                            if(item.getDiferenciaFavor()==1){//favor cliente (Bonificado)                                
                                saldo = clienteDto.getSaldoCliente() + devolucionDto.getMontoResultante();                           
                            }else if(item.getDiferenciaFavor()==2){//en contra del cliente                              
                                saldo = clienteDto.getSaldoCliente() - devolucionDto.getMontoResultante();                                 
                            }else if(item.getDiferenciaFavor()==4){//pago parcial                           
                                saldo = clienteDto.getSaldoCliente() - (devolucionDto.getMontoResultante()-devolucionDto.getDevolucionEfctivo());                                 
                            }                           
                            clienteDto.setSaldoCliente(saldo);  
                            
                            ClienteDaoImpl clienteDao = new ClienteDaoImpl(this.conn);
                            clienteDao.update(clienteDto.createPk(), clienteDto);                            
                            
                        }catch(Exception e){
                            System.out.println("No se pudo actualizar el saldo del cliente");
                        }    
                        
                        
                        
                        /***modificacion 
                         * */
                         idPedidoUltimo = devolucionDto.getIdPedido();
                    
                    
                            double cantidadAptos = item.getAptoParaVenta();
                            int idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();

                            System.out.println("Datos para devolución que se envia, Cantidad devuelta (Apto para venta):\n " + cantidadAptos + "Clave concepto devuelto:\n" + item.getIdConcepto() + "Clave concepto entregado:\n" + idEmpleado);
                            //Se ingresan los conceptos devueltos como aptos para venta
                            if(cantidadAptos > 0){
                                try{
                                    EmpleadoInventarioRepartidorBO emInventarioRepartidorBO = new EmpleadoInventarioRepartidorBO(this.conn);
                                    EmpleadoInventarioRepartidor emInventarioRepartidorDto;
                                    EmpleadoInventarioRepartidorDaoImpl emInventarioRepartidorDao = new EmpleadoInventarioRepartidorDaoImpl(this.conn);

                                    EmpleadoInventarioRepartidor[] emInventarios = 
                                        emInventarioRepartidorBO.findEmpleadoInventarioRepartidors(-1, idEmpleado, 0, 0, " AND ID_CONCEPTO = " + item.getIdConcepto());

                                    if (emInventarios.length>0){
                                        emInventarioRepartidorDto = emInventarios[0];
                                        double nuevoStock = emInventarioRepartidorDto.getCantidad() + cantidadAptos;
                                        emInventarioRepartidorDto.setCantidad(nuevoStock);
                                        emInventarioRepartidorDao.update(emInventarioRepartidorDto.createPk(), emInventarioRepartidorDto);
                                    }
                                }catch(Exception ex){
                                    System.out.println("Error al actualizar el inventario del vendedor:\n" + ex.getMessage());
                                }
                            }

                            //Se verifica si es cambio o devolución
                            if(item.getIdTipo() == SGPedidoDevolucionesCambioBO.ID_TIPO_CAMBIO 
                                    && item.getIdConceptoEntregado() > 0){
                                //Cambio
                                EmpleadoInventarioRepartidorBO emInventarioRepartidorBO = new EmpleadoInventarioRepartidorBO(this.conn);
                                EmpleadoInventarioRepartidor emInventarioRepartidorDto;
                                EmpleadoInventarioRepartidorDaoImpl emInventarioRepartidorDao = new EmpleadoInventarioRepartidorDaoImpl(this.conn);
                                EmpleadoInventarioRepartidor[] emInventarios = 
                                        emInventarioRepartidorBO.findEmpleadoInventarioRepartidors(-1, idEmpleado, 0, 0, " AND ID_CONCEPTO = " + item.getIdConceptoEntregado());
                                 if (emInventarios.length>0){
                                    emInventarioRepartidorDto = emInventarios[0];
                                    double nuevoStock = emInventarioRepartidorDto.getCantidad() - item.getCantidadDevuelta();
                                    emInventarioRepartidorDto.setCantidad(nuevoStock);
                                    emInventarioRepartidorDao.update(emInventarioRepartidorDto.createPk(), emInventarioRepartidorDto);
                                 }
                            } 
                         /* ---
                         */
                        
                        
                        
                        // Termina saldo
                        SgfensPedidoDevolucionCambioPk devolucionPK = devolucionDao.insert(devolucionDto);
                        wsItemResponse.setIdObjetoCreado(devolucionPK.getIdPedidoDevolCambio());
                        wsItemResponse.setIdConcepto(devolucionDto.getIdConcepto());
                        wsItemResponse.setFolioMovil(devolucionDto.getFolioMovil());
                        
                    }else{     
                        
                        //Actualización
                        
                        
                        
                        
                        //Actualizamos saldo cliente 
                        
                        try{
                            
                            // Casos 0 y 3 total = 0 , no se aplica ninguna accion.
                            double saldo = 0;
                            SgfensPedido pedidoDto = new SGPedidoBO(item.getIdPedido(), this.conn).getPedido();                            
                            Cliente clienteDto = new ClienteBO(pedidoDto.getIdCliente(),this.conn).getCliente();
                            
                            if(devolucionDto.getDiferenciaFavor()== 1 && item.getDiferenciaFavor() == 3  ){//de bonificado a liquidado                                
                                saldo = clienteDto.getSaldoCliente() - devolucionDto.getDevolucionEfctivo();                           
                            }else if(devolucionDto.getDiferenciaFavor()== 1 && item.getDiferenciaFavor() == 4  ){//de bonificado a parcial                              
                                saldo = clienteDto.getSaldoCliente() - devolucionDto.getDevolucionEfctivo();                                 
                            }else if(devolucionDto.getDiferenciaFavor()== 4 && item.getDiferenciaFavor() == 3  ){//de parcial  a liquidado
                                saldo = clienteDto.getSaldoCliente() - (item.getDevolucionEfectivo() - devolucionDto.getDevolucionEfctivo());                                                              
                            }                           
                            clienteDto.setSaldoCliente(saldo);  
                            
                            ClienteDaoImpl clienteDao = new ClienteDaoImpl(this.conn);
                            clienteDao.update(clienteDto.createPk(), clienteDto);                            
                            
                        }catch(Exception e){
                            System.out.println("No se pudo actualizar el saldo del cliente");
                        }
                        
                        
                        devolucionDto.setDiferenciaFavor(item.getDiferenciaFavor());
                        devolucionDto.setDevolucionEfctivo(item.getDevolucionEfectivo());
                        
                        devolucionDao.update(devolucionDto.createPk(), devolucionDto);
                        
                        wsItemResponse.setIdObjetoCreado(devolucionDto.getIdPedidoDevolCambio());
                        wsItemResponse.setIdConcepto(devolucionDto.getIdConcepto());
                        wsItemResponse.setFolioMovil(devolucionDto.getFolioMovil());
                        wsItemResponse.setFolioMovil(devolucionDto.getFolioMovil());
                    }
                    
                    
                   
                }catch(Exception ex){
                    ex.printStackTrace();
                    wsItemResponse.setError(true);
                    wsItemResponse.setNumError(902);
                    wsItemResponse.setMsgError("Error al realizar la devolución: " + ex.toString());
                }
                
                 response.getWsItemPedidoDevoluciones().add(wsItemResponse);
  
            }
            
            //Consumo de Creditos Operacion
            try{
                SgfensPedido pedidoDto = new SGPedidoBO(idPedidoUltimo, this.conn).getPedido();
                BitacoraCreditosOperacionBO bcoBO = new BitacoraCreditosOperacionBO(getConn());
                bcoBO.registraDescuento(empleadoBO.getUsuarioBO().getUser(), 
                        BitacoraCreditosOperacionBO.CONSUMO_ACCION_WS_REGISTRO_DEVOLUCION, 
                        null, pedidoDto.getIdCliente(), 0, 0, 
                        "Registro Devolucion WS Movil", null, true);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
            //registramos ubicacion
            try{actualizarUbicacionEmpleado(datosEmpleado);}catch(Exception ex){}
        }
        catch(Exception ex){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al realizar la devolución. " + ex.getLocalizedMessage());
            System.out.println("ERROR: \n" + ex.getMessage());
        }finally{   
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    public WSResponseInsert insertaActualizaGastoByEmpleado(String empleadoDtoRequestJson, String gastoDtoRequestJson){
        WSResponseInsert response;
        EmpleadoDtoRequest empleadoDtoRequest = null;
        GastoDtoRequest gastoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJson, EmpleadoDtoRequest.class);
            gastoDtoRequest = gson.fromJson(gastoDtoRequestJson, GastoDtoRequest.class);
            response = this.insertaActualizaGastoByEmpleado(empleadoDtoRequest,gastoDtoRequest);
        }catch(Exception ex){
            response = new WSResponseInsert();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        return response;
    }
    
    public WSResponseInsert insertaActualizaGastoByEmpleado(EmpleadoDtoRequest empleadoDtoRequest, GastoDtoRequest gastoDtoRequest){
        WSResponseInsert response = new WSResponseInsert();
        Configuration appConfig = new Configuration();
        
        int idEmpresa = 0 ;
        String rfcEmpresaMatriz ="";
        
        GastosEvc gastosEvc = new GastosEvc();
        GastosEvcDaoImpl gastosEvcDaoImpl = new GastosEvcDaoImpl(getConn());
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            Empresa empresaDto;
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())){
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
            }else{
                try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
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
            
            UsuarioBO usuarioBO = empleadoBO.getUsuarioBO();
            if (usuarioBO==null){
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("No se pudo recuperar la información de Usuario del empleado para registrar el prospecto.");
                return response;
            }
            
            if(gastoDtoRequest.getIdGasto() > 0){
                gastosEvc = gastosEvcDaoImpl.findByPrimaryKey(gastoDtoRequest.getIdGasto());
            }
            
            gastosEvc.setIdEmpleado(empleadoBO.getEmpleado().getIdEmpleado());
            gastosEvc.setComentario(gastoDtoRequest.getComentarios());
            gastosEvc.setFecha(gastoDtoRequest.getFecha());
            gastosEvc.setIdConcepto(gastoDtoRequest.getIdConcepto());
            gastosEvc.setIdEstatus(gastoDtoRequest.getIdEstatus());
            gastosEvc.setLatitud(gastoDtoRequest.getLatitud());
            gastosEvc.setLongitud(gastoDtoRequest.getLongitud());
            gastosEvc.setMonto(gastoDtoRequest.getMonto());
            gastosEvc.setIdEstatus(1);
            gastosEvc.setIdEmpresa(idEmpresa);
            
            if(gastoDtoRequest.getIdGasto() <= 0){
                //Insert
                GastosEvcPk gastoPk = gastosEvcDaoImpl.insert(gastosEvc);
                response.setIdObjetoCreado(gastoPk.getIdGastos());
            }else{
                //update
                gastosEvcDaoImpl.update(gastosEvc.createPk(), gastosEvc);
                response.setIdObjetoCreado(gastosEvc.getIdGastos());
            }
            
            //registramos ubicacion
            try{actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
        }catch(Exception ex){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al crear o actualizar Gasto de Empleado. " + ex.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return response;
    }
    
    /**
     * Crea o actualiza el registro de un Prospecto
     * procedente de la app móvil
     * @param prospectoDtoRequestJSON String Datos del prospecto en formato JSON 
     * @return WSResponseInsert respuesta básica de inserción
     */
    public WSResponseInsert insertaActualizaProspecto(String empleadoDtoRequestJSON, String prospectoDtoRequestJSON){
        WSResponseInsert response;
                
        EmpleadoDtoRequest empleadoDtoRequest = null;
        ProspectoDtoRequest prospectoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            //Transformamos de formato JSON a objeto
            prospectoDtoRequest = gson.fromJson(prospectoDtoRequestJSON, ProspectoDtoRequest.class);
            
            response = this.insertaActualizaProspecto(empleadoDtoRequest, prospectoDtoRequest);
        }catch(Exception ex){
            response = new WSResponseInsert();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }
    
    /**
     * Crea o actualiza el registro de un Prospecto
     * procedente de la app móvil
     * @param datosEmpleado
     * @param prospectoDtoRequest ProspectoDtoRequest Datos del prospecto
     * @return WSResponseInsert respuesta básica de inserción
     */
    public WSResponseInsert insertaActualizaProspecto(EmpleadoDtoRequest datosEmpleado, ProspectoDtoRequest prospectoDtoRequest){
        
        WSResponseInsert response = new WSResponseInsert();
        Configuration appConfig = new Configuration();
        
        int idEmpresa = 0 ;
        String rfcEmpresaMatriz ="";
        
        SgfensProspecto prospectoDto = new SgfensProspecto();
        SgfensProspectoDaoImpl prospectoDao = new SgfensProspectoDaoImpl(getConn());
        
        try{
            
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            Empresa empresaDto;
            if (empleadoBO.login(datosEmpleado.getEmpleadoUsuario(), datosEmpleado.getEmpleadoPassword())){
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
            }else{
                try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
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
            
            UsuarioBO usuarioBO = empleadoBO.getUsuarioBO();
            if (usuarioBO==null){
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("No se pudo recuperar la información de Usuario del empleado para registrar el prospecto.");
                return response;
            }
            if (prospectoDtoRequest.getIdProspecto()>0){
                //Si es edición recuperamos el objeto correspondiente de BD
                prospectoDto = new SGProspectoBO(getConn()).findProspectobyId(prospectoDtoRequest.getIdProspecto());
            }
            
            prospectoDto.setIdEmpresa(idEmpresa);
            prospectoDto.setRazonSocial(prospectoDtoRequest.getRazonSocial());
            prospectoDto.setLada(prospectoDtoRequest.getLada());
            prospectoDto.setTelefono(prospectoDtoRequest.getTelefono());
            prospectoDto.setCelular(prospectoDtoRequest.getCelular());
            prospectoDto.setCorreo(prospectoDtoRequest.getCorreo());
            prospectoDto.setContacto(prospectoDtoRequest.getContacto());
            prospectoDto.setIdEstatus(1);
            prospectoDto.setDescripcion(prospectoDtoRequest.getDescripcion());
            prospectoDto.setLatitud(prospectoDtoRequest.getLatitud());
            prospectoDto.setLongitud(prospectoDtoRequest.getLongitud());
            prospectoDto.setDireccion(prospectoDtoRequest.getDireccion());
            prospectoDto.setIdUsuarioVendedor(usuarioBO.getUser().getIdUsuarios());
            try{
                prospectoDto.setFechaRegistro(prospectoDtoRequest.getFechaRegistro()!=null?prospectoDtoRequest.getFechaRegistro() : (ZonaHorariaBO.DateZonaHorariaByIdEmpresa(getConn(), new Date(), (int)empleadoBO.getEmpleado().getIdEmpresa()).getTime()) );
            }catch(Exception e){
                prospectoDto.setFechaRegistro(prospectoDtoRequest.getFechaRegistro()!=null?prospectoDtoRequest.getFechaRegistro() : new Date());
            }
            prospectoDto.setDirNumeroExterior(prospectoDtoRequest.getDirNumeroExterior());
            prospectoDto.setDirNumeroInterior(prospectoDtoRequest.getDirNumeroInterior());
            prospectoDto.setDirCodigoPostal(prospectoDtoRequest.getDirCodigoPostal());
            prospectoDto.setDirColonia(prospectoDtoRequest.getDirColonia());
            
            File archivoImagenProspecto = null;

            if (prospectoDtoRequest.getImagenProspectoBytesBase64()!=null){
                if (prospectoDtoRequest.getImagenProspectoBytesBase64().trim().length()>0){
                    try{
                        //Convertimos bytes en base64 a una imagen y la almacenamos en servidor
                        byte[] bytesImagenProspecto = Base64.decode(prospectoDtoRequest.getImagenProspectoBytesBase64());

                        String ubicacionImagenesProspectos = appConfig.getApp_content_path() + rfcEmpresaMatriz +"/prospectos/images/";
                        String nombreArchivoImagenProspecto = "img_prospecto_"+DateManage.getDateHourString()+".jpg";

                        archivoImagenProspecto = FileManage.createFileFromByteArray(bytesImagenProspecto, ubicacionImagenesProspectos, nombreArchivoImagenProspecto);
                    }catch(Exception ex){
                        response.setError(true);
                        response.setNumError(901);
                        response.setMsgError("Los archivos de imagen no son correctos. " + ex.getLocalizedMessage());
                        System.out.println(response.getMsgError());
                        ex.printStackTrace();
                        return response;
                    }
                }
            }
            
            if (archivoImagenProspecto!=null)
                prospectoDto.setNombreImagenProspecto(archivoImagenProspecto.getName());
            
            boolean edicion = false;
            if (prospectoDtoRequest.getIdProspecto()<=0){
                //Registro nuevo (INSERT)
                SgfensProspectoPk prospectoPk = prospectoDao.insert(prospectoDto);
                response.setIdObjetoCreado(prospectoPk.getIdProspecto());
            }else{
                //Registro existente (UPDATE)
                prospectoDao.update(prospectoDto.createPk(), prospectoDto);
                response.setIdObjetoCreado(prospectoDto.getIdProspecto());
                edicion = true;
            }
            
            //registramos ubicacion
            try{actualizarUbicacionEmpleado(datosEmpleado);}catch(Exception ex){}
            
            if (!edicion){
                //Consumo de Creditos Operacion
                try{
                    BitacoraCreditosOperacionBO bcoBO = new BitacoraCreditosOperacionBO(getConn());
                    bcoBO.registraDescuento(empleadoBO.getUsuarioBO().getUser(), 
                            BitacoraCreditosOperacionBO.CONSUMO_ACCION_WS_REGISTRO_PROSPECTO, 
                            null, 0, (int)response.getIdObjetoCreado(), 0, 
                            "Registro Cliente WS Movil", null, true);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            
        }catch(Exception ex){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al crear o actualizar Prospecto. " + ex.getLocalizedMessage());
        }finally{   
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    public InsertRegistroVisitaClienteResponse registrarRutaMarcadoresVisitados(String empleadoDtoRequestJSON, int[] rutaMarcadoresIDs){
        InsertRegistroVisitaClienteResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            
            response = this.registrarRutaMarcadoresVisitados(empleadoDtoRequest, rutaMarcadoresIDs);
        }catch(Exception ex){
            response = new InsertRegistroVisitaClienteResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
     }
     
     public InsertRegistroVisitaClienteResponse registrarRutaMarcadoresVisitados(EmpleadoDtoRequest empleadoDtoRequest, int[] rutaMarcadoresIDs){
         InsertRegistroVisitaClienteResponse response = new InsertRegistroVisitaClienteResponse();
         
         try{
             //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                
                if (rutaMarcadoresIDs!=null){
                    if (rutaMarcadoresIDs.length>0){
                        for (int marcadorVisitado : rutaMarcadoresIDs){
                            RutaMarcadorDaoImpl rutaMarcadorDao = new RutaMarcadorDaoImpl(getConn());
                            RutaMarcador rutaMarcador = rutaMarcadorDao.findByPrimaryKey(marcadorVisitado);
                            if (rutaMarcador!=null){
                                rutaMarcador.setIsVisitado((short)1);

                                rutaMarcadorDao.update(rutaMarcador.createPk(), rutaMarcador);
                            }
                        }
                    }
                }
                
            }else {
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
         } catch (Exception e) {
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error al actualizar estatus de Marcadores visitados. " + e.toString());
            e.printStackTrace();
        }finally{   
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
         
        return response;
     }
     
     /**
     * Crea un nuevo registro de Usuario, a partir de los datos recabados
     * desde el formulario en aplicación móvil.
     * @param datosEmpleado String en formato JSON representando un objeto de tipo EmpleadoDtoRequest
     
     * @return Objeto de respuesta generico para inserciones vía Web Service, cadena en formato JSON representando un objeto WSResponseInsert
     */
    public UsuarioNuevoDtoRequest insertaUsuarioNuevo(String usuarioDtoRequestJSON){
        //WSResponseInsert response;
                
        UsuarioNuevoDtoRequest usuarioNuevoDtoRequest = null;       
        try{
            //Transformamos de formato JSON a objeto
            usuarioNuevoDtoRequest = gson.fromJson(usuarioDtoRequestJSON, UsuarioNuevoDtoRequest.class);            
            
            usuarioNuevoDtoRequest = this.insertaUsuarioNuevo(usuarioNuevoDtoRequest);
        }catch(Exception ex){
            usuarioNuevoDtoRequest = new UsuarioNuevoDtoRequest();
            usuarioNuevoDtoRequest.setError(true);
            usuarioNuevoDtoRequest.setNumError(901);
            usuarioNuevoDtoRequest.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return usuarioNuevoDtoRequest;
    }
    
    /**
     * Crea un nuevo registro de Usuario, a partir de los datos recabados
     * desde el formulario en aplicación móvil.
     * @param datosEmpleado EmpleadoDtoRequest Datos del empleado.
     * @param datosCliente CrearClienteRequest Datos del Cliente a crear.
     * @return Objeto de respuesta generico para inserciones vía Web Service
     */
    public UsuarioNuevoDtoRequest insertaUsuarioNuevo(UsuarioNuevoDtoRequest datosUsuarioCargados){
        //WSResponseInsert response = new WSResponseInsert();
        
        int idCreado=-1;
        
        String nombre = datosUsuarioCargados.getNombre();
        String aPaterno = datosUsuarioCargados.getaPaterno();
        String aMaterno = datosUsuarioCargados.getaMaterno();
        String nombreEmpresa = datosUsuarioCargados.getNombreEmpresa();
        String eMail = datosUsuarioCargados.geteMail();        
        String telefono = datosUsuarioCargados.getTelefono();
        String imei = datosUsuarioCargados.getImei();       
        String marcaCel = datosUsuarioCargados.getMarcaCel();
        String modeloCel = datosUsuarioCargados.getModeloCel();
        
        telefono = telefono.trim().replace(" ", "");
        telefono = telefono.trim().replace("  ", "");
        telefono = telefono.trim().replace("   ", "");
        telefono = telefono.trim().replace("-", "");
        
        try{
            UsuariosdemoPk usuariosdemoPk = null;
            try{
                //Insatanciamos objs
                Usuariosdemo userDemoDto = new Usuariosdemo();
                UsuariosdemoDaoImpl userDemoDaoImpl = new UsuariosdemoDaoImpl(getConn());                        
                //Seteamos obj
                userDemoDto.setNombre(nombre);
                userDemoDto.setApellidoPaterno(aPaterno);
                userDemoDto.setApellidoMaterno(aMaterno);
                userDemoDto.setNombreEmpresa(nombreEmpresa);
                userDemoDto.setEmail(eMail);
                try{
                    userDemoDto.setTelefono(telefono);
                    userDemoDto.setImei(imei);
                }catch(Exception e){}            
                userDemoDto.setMarcaCelular(marcaCel);
                userDemoDto.setModeloCelular(modeloCel);
                
                userDemoDto.setFechaRegistro((new Date()));
               
                userDemoDto.setIdEstatus(1);            

                //Insertamos obj            
                usuariosdemoPk = userDemoDaoImpl.insert(userDemoDto);
            }catch(Exception e){
            }
            
            //VALIDAMO A QUÉ EMPRESA LO ASIGNAMOS AL USUARIO:
            int idEmpresaAsignado = -1; 
            EmpresaBO empresaBO = new EmpresaBO(getConn());
                idEmpresaAsignado = empresaBO.cuentaUsuariosEmpresa();
            //si es menos a cero es que no hay empresa alguna en donde meter al nuevo usuario
            if(idEmpresaAsignado < 0){
                idEmpresaAsignado = empresaBO.creaEmpresa();
            }
            
            
            
            
            
            
            //GENERAMOS EL USUARIO DEL SISTEMA:
             //**realizamos la creacion del usuario:
                int idDatosUsuarioNuevo;
                DatosUsuarioDaoImpl datosUsuarioDaoImpl = new DatosUsuarioDaoImpl(getConn());
                DatosUsuario ultimoRegistroDatosUsuarios = datosUsuarioDaoImpl.findLast();
                idDatosUsuarioNuevo = ultimoRegistroDatosUsuarios.getIdDatosUsuario() + 1;
                
                DatosUsuario datosUsuario = new DatosUsuario();
                datosUsuario.setIdDatosUsuario(idDatosUsuarioNuevo);
                datosUsuario.setNombre(nombre);
                datosUsuario.setApellidoPat(aPaterno);
                datosUsuario.setApellidoMat(aMaterno);
                datosUsuario.setDireccion("");
                datosUsuario.setTelefono(telefono);
                datosUsuario.setCorreo(eMail);
                
                DatosUsuarioPk datosUsuarioPk = datosUsuarioDaoImpl.insert(datosUsuario);
                UsuariosDaoImpl usuariosDaoImpl = new UsuariosDaoImpl(getConn());
                
                Usuarios ultimoRegistroUsuarios = usuariosDaoImpl.findLast();
                int idUsuariosNuevo = ultimoRegistroUsuarios.getIdUsuarios() + 1;
                
                Usuarios usuarios = new Usuarios();
                usuarios.setIdUsuarios(idUsuariosNuevo);
                usuarios.setIdDatosUsuario(datosUsuarioPk.getIdDatosUsuario());
                usuarios.setIdEmpresa(idEmpresaAsignado);
                usuarios.setIdEstatus(1);                
                usuarios.setIdRoles(2);
                String userName = "";
                RandomString ram = new RandomString();
                userName = nombre.trim()+"_"+ram.randomString(3);
                datosUsuarioCargados.setUserAcceso(userName);////////////
                usuarios.setUserName(userName);
                usuarios.setFechaAlta(new Date());
                    Calendar fechaVigenciaCal = Calendar.getInstance(); 
                    fechaVigenciaCal.setTime(new Date()); 
                    fechaVigenciaCal.add(Calendar.YEAR, 1 ); 
                    Date fechaVigencia = fechaVigenciaCal.getTime();
                usuarios.setFechaVigencia(fechaVigencia);
                UsuariosPk usuariosPk =  new UsuariosDaoImpl(getConn()).insert(usuarios);
                
                datosUsuarioCargados.setIdUsuario(usuariosPk.getIdUsuarios());
                
                /**
                * Creamos el registro de login
                */
                Ldap ldapDto = new Ldap();
                LdapDaoImpl ldapDaoImpl = new LdapDaoImpl(getConn());

                int idLDapNuevo;
                String password = ram.randomString(4);
                password = password.trim();
                
                datosUsuarioCargados.setPassAcceso(password);////////////
                
                Encrypter datoEnc =  new Encrypter();
                datoEnc.setMd5(true);////ESTA PARTE DE CODIGO ES PARA GENERAR EL md5
                String passEncriptado = datoEnc.encodeString2(password);
                
                Ldap ultimoRegistroLdap = ldapDaoImpl.findLast();
                idLDapNuevo = ultimoRegistroLdap.getIdLdap() + 1;
                
                ldapDto.setIdLdap(idLDapNuevo);
                ldapDto.setUsuario(userName);
                ldapDto.setPassword(passEncriptado);
                ldapDto.setContrasenas("a,b,c,");
                ldapDto.setFirmado(0);
                ldapDto.setEmail(eMail);
                ldapDaoImpl.insert(ldapDto);
                //**
                
                /**
                 * Creamos el registro de DispositivoMovil
                 */
                DispositivoMovil dispositivoMovilDto = new DispositivoMovil();
                DispositivoMovilDaoImpl dispositivosMovilesDaoImpl = new DispositivoMovilDaoImpl(getConn());             
                                
                dispositivoMovilDto.setIdEstatus(1);
                dispositivoMovilDto.setImei(imei.trim());
                dispositivoMovilDto.setMarca(marcaCel);
                dispositivoMovilDto.setModelo(modeloCel);
                dispositivoMovilDto.setNumeroSerie("");
                dispositivoMovilDto.setAliasTelefono(userName);
                //dispositivoMovilDto.setAsignado(asignadoDispositivoMovil);
                //dispositivoMovilDto.setIdEmpresa(idEmpresa);
                dispositivoMovilDto.setIdEmpresa(idEmpresaAsignado);
                
                /**
                 * Realizamos el insert
                 */
                DispositivoMovilPk movilPk = dispositivosMovilesDaoImpl.insert(dispositivoMovilDto);
                
                /*
                *Creamos el registro de empleado
                */
                Empleado empleadoDto = new Empleado();
                EmpleadoDaoImpl empleadosDaoImpl = new EmpleadoDaoImpl(getConn());                               
                empleadoDto.setIdEstatus(1);
                empleadoDto.setRepartidor(1);
                empleadoDto.setIdEmpresa(idEmpresaAsignado);
                empleadoDto.setIdEstado(9);
                empleadoDto.setIdDispositivo(movilPk.getIdDispositivo());
                empleadoDto.setIdMovilEmpleadoRol(7);
                empleadoDto.setIdSucursal(idEmpresaAsignado);
                empleadoDto.setNombre(nombre);                
                empleadoDto.setApellidoPaterno(aPaterno);
                empleadoDto.setApellidoMaterno(aMaterno);                                                                
                empleadoDto.setTelefonoLocal(telefono);                
                empleadoDto.setNumEmpleado((new Date().getTime()) + "");
                empleadoDto.setCorreoElectronico(eMail);
                empleadoDto.setLatitud(0);
                empleadoDto.setLongitud(0);
                empleadoDto.setUsuario(userName);
                empleadoDto.setPassword(passEncriptado);
                empleadoDto.setIdUsuarios(usuarios.getIdUsuarios());                
                empleadosDaoImpl.insert(empleadoDto);
                
                try{
                    /*
                    * Creamos el registro de 
                    */
                    RelDemopruebas rd = new RelDemopruebas();
                    rd.setIdUsuarioDemo(usuariosdemoPk.getIdUsuarioDemo());
                    rd.setIdUsuarioPruebas(usuariosPk.getIdUsuarios());                
                    new RelDemopruebasDaoImpl(getConn()).insert(rd);
                }catch(Exception e){
                }
                
                ///inicio de envio por correo
                try{
                    TspMailBO mail = new TspMailBO();
                    mail.addMessageMovilpyme(aMaterno, idLDapNuevo);
                    try {
                        mail.addTo(eMail, eMail);
                    }catch(Exception e){}                    
                    mail.setFrom(mail.getUSER(), mail.getFROM_NAME());
                    mail.addMessageMovilpyme(""
                            + "<h2>Nuevo Usuarios Creado, para demo de Equipo de Ventas en Campo</h2><br/>"
                            + "<b>Recuerda que para poder iniciar tu demo, primero debes de instalar la aplicación de &quot;Equipo de Ventas en Campo&quot; en el  celular que registraste</b></h2><br/>"
                            + "Acceso a la consola de pretoriano móvil: <br/>"
                            + "Liga de Acceso: <b><a href='http://pos.from-la.net:8082/SGFENS/'>" + "http://pos.from-la.net:8082/SGFENS/" + "</a></b><br/>"
                            + "Usuario: <b> "+userName+"</b><br/>"
                            + "Password: <b> "+ Matcher.quoteReplacement(password) +"</b><br/>"
                            
                            + "Acceso a la APP de EVC: <br/>"
                            + "Usuario: <b> "+userName+"</b><br/>"
                            + "Password: <b> "+ Matcher.quoteReplacement(password) +"</b><br/>"
                            + "Fecha de registro: <b> "+ ( DateManage.formatDateToNormal(new Date()) ) +"</b><br/>"                            
                            ,1);                    
                        mail.send("Credenciales de Acceso MovilPyme");
                                       
                    //out.print("<br/>Un correo de notificaci&oacute;n fue enviado al usuario.");
               }catch(Exception ex){
                   System.out.println("No se pudo enviar el correo de notificacion. Copie la informacion y notifiquela al usuario" + ex.toString());                    
               }
               ///fin de envio por correo 
            
                ////--Notificamos por correo a los administradores:
                try{
                    Configuration configuration = new Configuration();
                    GenericValidator genericValidator = new GenericValidator();
                    String[] correos = new String[0];
                    ArrayList<String> listCorreosValidos = new ArrayList<String>();
                    if (!configuration.getCorreoNotificacionNuevoUsuario().trim().equals("")){
                        correos = configuration.getCorreoNotificacionNuevoUsuario().split(",");
                        for (String itemCorreo: correos){
                            try { 
                                itemCorreo = itemCorreo.trim(); 
                            } catch(Exception ex){}
                            if (genericValidator.isEmail(itemCorreo)){
                                listCorreosValidos.add(itemCorreo);
                            }
                        }                    
                    }                
                    try{
                        TspMailBO mail = new TspMailBO();
                        mail.addMessageMovilpyme(aMaterno, idLDapNuevo);
                        /*try {
                            mail.addTo(eMail, eMail);
                        }catch(Exception e){}*/
                        //agregamos los demas destinatarios:
                        for(String correo : listCorreosValidos){
                            try {
                                mail.addTo(correo.trim(), correo.trim());
                            }catch(Exception e){}
                        }
                        mail.setFrom(mail.getUSER(), mail.getFROM_NAME());
                        mail.addMessageMovilpyme(""
                                + "<h2>Nuevo Usuario Creado desde el Movil.</h2><br/>"
                                + "Los datos de acceso al Sistema del nuevo Usuario son: <br/>"
                                + "Usuario: <b> "+userName+"</b><br/>"
                                + "Password: <b> "+ Matcher.quoteReplacement(password) +"</b><br/>"
                                + "Los datos del Usuarios son:<br/>"
                                + "Nombre: <b> "+nombre +"</b><br/>"
                                + "Apellido Paterno: <b> "+aPaterno +"</b><br/>"
                                + "Apellido Materno: <b> "+aMaterno +"</b><br/>"
                                + "Nombre de la Empresa: <b> "+nombreEmpresa +"</b><br/>"
                                + "Correo e-mail: <b> "+eMail +"</b><br/>"
                                + "Teléfono: <b> "+telefono +"</b><br/>"
                                + "IMEI: <b> "+imei +"</b><br/>"
                                + "Marca: <b> "+marcaCel +"</b><br/>"
                                + "Modelo: <b> "+modeloCel +"</b><br/>"
                                + "La Fecha del registro es: <b> "+ ( DateManage.formatDateToNormal(new Date()) ) +"</b><br/>"
                                + "Liga de Acceso: <b><a href='http://pos.from-la.net:8082/SGFENS/'>" + "http://pos.from-la.net:8082/SGFENS/" + "</a></b><br/>",1);                    
                            mail.send("Credenciales de Acceso MovilPyme"); 
                   }catch(Exception ex){
                       System.out.print("No se pudo enviar el correo de notificacion. Copie la informacion y notifiquela al usuario. " + ex.toString()); 
                       ex.printStackTrace();
                   }
               }catch(Exception e){e.printStackTrace();}
               ////-- Fin de Notifir por correo a los administradores.
            
        }catch(Exception e){
            e.printStackTrace();
            datosUsuarioCargados.setError(true);
            datosUsuarioCargados.setNumError(902);
            datosUsuarioCargados.setMsgError("Error inesperado al crear un nuevo Usuario. " + e.getLocalizedMessage());
        }finally{   
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return datosUsuarioCargados;
    }
    
        public RegistrarBitacoraCreditoOperacionResponse insertRegistroBitacoraCreditoOperacion(String empleadoDtoRequestJSON, String registroBCODataJSON){
        RegistrarBitacoraCreditoOperacionResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest;
        RegistrarBitacoraCreditoOperacionRequest registrarBCORequest;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            registrarBCORequest = gson.fromJson(registroBCODataJSON, RegistrarBitacoraCreditoOperacionRequest.class);
            
            response = this.insertRegistroBitacoraCreditoOperacion(empleadoDtoRequest, registrarBCORequest);
        }catch(Exception ex){
            response = new RegistrarBitacoraCreditoOperacionResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }
    public RegistrarBitacoraCreditoOperacionResponse insertRegistroBitacoraCreditoOperacion(EmpleadoDtoRequest empleadoData, RegistrarBitacoraCreditoOperacionRequest registroBCOData){
        
        RegistrarBitacoraCreditoOperacionResponse response = new RegistrarBitacoraCreditoOperacionResponse();
        int idEmpresa = 0 ;
        
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            
            if (empleadoBO.login(empleadoData.getEmpleadoUsuario(), empleadoData.getEmpleadoPassword())){
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
            }else{
                try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                return response;
            }
            
            UsuarioBO usuarioBO = empleadoBO.getUsuarioBO();
            if (usuarioBO==null){
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("No se pudo recuperar la información de Usuario del empleado para registrar los datos.");
                return response;
            }
            
            //Buscamos si no se registro previamente
            BitacoraCreditosOperacionDaoImpl bcoDao = new BitacoraCreditosOperacionDaoImpl(getConn());
            BitacoraCreditosOperacion[] bcoPrevios = bcoDao.findWhereFolioMovilEquals(registroBCOData.getFolioMovil());
            if (bcoPrevios.length>0){
                Empresa empresaMatrizDto = new EmpresaBO(getConn()).getEmpresaMatriz(idEmpresa);
                
                response.setCreditosOperacionDisponibles(empresaMatrizDto.getCreditosOperacion());
                response.setIdObjetoCreado(bcoPrevios[0].getIdBitacoraCreditosOperacion());
                response.setIdBancoOperacionCreado(bcoPrevios[0].getIdOperacionBancaria());
                
                return response;
            }
            
            //Registramos Bitacora Operacion
            BitacoraCreditosOperacionBO bcoBO = new BitacoraCreditosOperacionBO(getConn());
            BitacoraCreditosOperacion bcoDto;
            
            if  (registroBCOData.getTipo()==2){
                //Descuento
                bcoDto = bcoBO.registraDescuento(usuarioBO.getUser(), registroBCOData.getCantidad(), 
                    registroBCOData.getFechaHora(), registroBCOData.getIdCliente(), 
                    registroBCOData.getIdProspecto(), registroBCOData.getMontoOperacion(), 
                    "Compra Creditos WS Movil", registroBCOData.getFolioMovil(), true);
            }else {
                //Compra
                bcoDto = bcoBO.registraAbono(usuarioBO.getUser(), registroBCOData.getCantidad(), 
                    registroBCOData.getFechaHora(), registroBCOData.getMontoOperacion(), 
                    "Compra Creditos WS Movil", registroBCOData.getFolioMovil(), true);
            }
            
            //Registramos Datos de cobro con tarjeta
            if (registroBCOData.getBancoOperacion()!=null){
                try{
                    BancoOperacionDaoImpl bancoOperacionDao = new BancoOperacionDaoImpl(getConn());
                    BancoOperacion bancoOperacionDto = new BancoOperacion();
                    WsBancoOperacionRequest wsBancoOperacionRequest = registroBCOData.getBancoOperacion();

                    bancoOperacionDto.setBancoAuth(wsBancoOperacionRequest.getBancoAuth());
                    bancoOperacionDto.setBancoOperFecha(wsBancoOperacionRequest.getBancoOperFecha());
                    bancoOperacionDto.setBancoOperIssuingBank(wsBancoOperacionRequest.getBancoOperIssuingBank());
                    bancoOperacionDto.setBancoOperType(wsBancoOperacionRequest.getBancoOperType());
                    bancoOperacionDto.setBancoOrderId(wsBancoOperacionRequest.getBancoOrderId());
                    bancoOperacionDto.setIdEmpresa(idEmpresa);
                    bancoOperacionDto.setIdEstatus(wsBancoOperacionRequest.getIdEstatus());
                    //bancoOperacionDto.setIdOperacionBancaria(wsBancoOperacionRequest.getIDO);
                    bancoOperacionDto.setMonto(wsBancoOperacionRequest.getMonto());
                    bancoOperacionDto.setNoTarjeta(wsBancoOperacionRequest.getNoTarjeta());
                    bancoOperacionDto.setNombreTitular(wsBancoOperacionRequest.getNombreTitular());
                    bancoOperacionDto.setDataArqc(wsBancoOperacionRequest.getDataArqc());
                    bancoOperacionDto.setDataAid(wsBancoOperacionRequest.getDataAid());
                    bancoOperacionDto.setDataTsi(wsBancoOperacionRequest.getDataTsi());
                    bancoOperacionDto.setDataRef(wsBancoOperacionRequest.getDataRef());
                    bancoOperacionDto.setDataExtra1(wsBancoOperacionRequest.getDataExtra1());
                    bancoOperacionDto.setDataExtra2(wsBancoOperacionRequest.getDataExtra2());

                    BancoOperacionBO bancoOperacionBO = new BancoOperacionBO(getConn());
                    File archivoImagenIFE = bancoOperacionBO.crearArchivoImagenExtraFromBase64(idEmpresa, wsBancoOperacionRequest.getImagenIFEBytesBase64(),true);
                    File archivoImagenTDC = bancoOperacionBO.crearArchivoImagenExtraFromBase64(idEmpresa, wsBancoOperacionRequest.getImagenTDCBytesBase64(),false);

                    if (archivoImagenIFE!=null)
                        bancoOperacionDto.setNombreArchivoImgIfe(archivoImagenIFE.getName());
                    if (archivoImagenTDC!=null)
                        bancoOperacionDto.setNombreArchivoImgTdc(archivoImagenTDC.getName());

                    bancoOperacionDao.insert(bancoOperacionDto);

                    int idOperacionBancaria = bancoOperacionDto.getIdOperacionBancaria();
                    bcoDto.setIdOperacionBancaria(idOperacionBancaria);
                    bcoDao.update(bcoDto.createPk(), bcoDto);
                    
                    Empresa empresaMatrizDto = new EmpresaBO(getConn()).getEmpresaMatriz(idEmpresa);
                    response.setCreditosOperacionDisponibles(empresaMatrizDto.getCreditosOperacion());
                    response.setIdBancoOperacionCreado(idOperacionBancaria);
                    response.setIdObjetoCreado(bcoDto.getIdBitacoraCreditosOperacion());
                    
                    //Enviamos correo electronico con datos para cobrar manualmente
                    try{
                        Parametros[] parametrosDto = new ParametrosDaoImpl(getConn()).findWhereNombreEquals("app.mail.cobrotdc.destFijos");
                        if (parametrosDto.length>0){
                            GenericValidator genericValidator = new GenericValidator();
                            String[] correos;
                            ArrayList<String> listCorreosValidos = new ArrayList<String>();
                            if (StringManage.getValidString(parametrosDto[0].getDescripcion()).length()>0){
                                correos = parametrosDto[0].getDescripcion().split(",");
                                for (String itemCorreo: correos){
                                    try { 
                                        itemCorreo = itemCorreo.trim(); 
                                    } catch(Exception ex){}
                                    if (genericValidator.isEmail(itemCorreo)){
                                        listCorreosValidos.add(itemCorreo);
                                    }
                                }                    
                            }                
                            try{
                                TspMailBO mail = new TspMailBO();
                                mail.setConfigurationMovilpyme();
                                //agregamos los demas destinatarios:
                                for(String correo : listCorreosValidos){
                                    try {
                                        mail.addTo(correo.trim(), correo.trim());
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }
                                mail.addMessageMovilpyme(""
                                        + "<h2>Nueva Petición de Compra de Créditos de Operación desde el Movil.</h2><br/>"
                                        + "Los datos de compra son: <br/>"
                                        + "Empleado: <b> "+empleadoBO.getEmpleado().getNombre()+ " " + empleadoBO.getEmpleado().getApellidoPaterno() + "</b><br/>"
                                        + "Empresa: <b> ("+empresaMatrizDto.getRfc() + ")" + empresaMatrizDto.getRazonSocial() +"</b><br/>"
                                        + "Cantidad Creditos: <b>" + registroBCOData.getCantidad() + "</b><br/>"
                                        + "Folio Movil: <b>" + registroBCOData.getFolioMovil() + "</b><br/>"
                                        + "Fecha y Hr de Compra: <b>" + registroBCOData.getFechaHora()+ "</b><br/>"
                                        + "Comentarios: <b>" + registroBCOData.getComentarios() + "</b><br/>"
                                        + "<br/>Los datos de la Tarjeta son:<br/>"
                                        + "Nombre del Titular: <b> "+wsBancoOperacionRequest.getNombreTitular() +"</b><br/>"
                                        + "Banco: <b> "+wsBancoOperacionRequest.getBancoOperIssuingBank() +"</b><br/>"
                                        + "Numero de tarjeta: <b> "+wsBancoOperacionRequest.getNoTarjeta() +"</b><br/>"
                                        + "Mes Expira: <b> "+wsBancoOperacionRequest.getMesExpiracion() +"</b><br/>"
                                        + "Año Expira: <b> "+wsBancoOperacionRequest.getAnioExpiracion() +"</b><br/>"
                                        + "CVV: <b> "+wsBancoOperacionRequest.getCvv() +"</b><br/>"
                                        + "Monto a Cobrar: <b> "+registroBCOData.getMontoOperacion()+"</b><br/>"
                                        + "<br/><b>Los creditos ya fueron aplicados a la cuenta del cliente, favor de cobrar manualmente.</b><br/>"
                                        ,1);                    
                                    mail.send("Peticion de Cobro Compra Creditos Operacion"); 
                            }catch(Exception ex){
                               System.out.print("No se pudo enviar el correo de notificacion . Copie la informacion y notifiquela al usuario. " + ex.toString()); 
                               ex.printStackTrace();
                            }
                        }else{
                            response.setError(true);
                            response.setNumError(901);
                            response.setMsgError("No se pudo almacenar operación bancaria en servidor (Correo de compra no enviado para aplicacion, correos dest no configurados). Reportelo a su proveedor " );
                        }
                   }catch(Exception e){e.printStackTrace();}
                }catch(Exception ex){
                    response.setError(true);
                    response.setNumError(901);
                    response.setMsgError("No se pudo almacenar operación bancaria en servidor. Reportelo a su proveedor " + ex.toString());
                    System.out.println("***No se pudo almacenar operación bancaria****" + ex.toString());
                    return response;
                }
            }
            
             //registramos ubicacion
            try{actualizarUbicacionEmpleado(empleadoData);}catch(Exception ex){}
            
        }catch(Exception ex){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al registrar la bitacora de creditos operacion. " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }finally{   
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }    

    public WsItemCuentaDineroResponse insertaCuentaDinero(String empleadoDtoRequestJson, String cuentaDineroDtoRequestJson) {
        WsItemCuentaDineroResponse response;
        
        EmpleadoDtoRequest empleadoDtoRequest = null;
        WsItemCuentaDinero cuentaDineroDtoRequest = null;
        
        try{
            
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJson, EmpleadoDtoRequest.class);
            cuentaDineroDtoRequest = gson.fromJson(cuentaDineroDtoRequestJson, WsItemCuentaDinero.class);
            
            System.out.println("JSON:\n" + cuentaDineroDtoRequest);
            
            response = this.insertCuentaDineroResponse(empleadoDtoRequest, cuentaDineroDtoRequest);
            
        }catch(Exception ex){
            response = new WsItemCuentaDineroResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        return response;
        
    }

    private WsItemCuentaDineroResponse insertCuentaDineroResponse(EmpleadoDtoRequest empleadoDtoRequest, WsItemCuentaDinero cuentaDineroDtoRequest) {
        
        WsItemCuentaDineroResponse response = new WsItemCuentaDineroResponse();
        int idEmpresa = 0;
        CuentaEfectivo cuentaEfectivoDto = new CuentaEfectivo();
        CuentaEfectivoDaoImpl cuentaEfectivoDaoImpl = new CuentaEfectivoDaoImpl(getConn());
        
        try{
            
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())){
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
            }else{
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                return response;
            }
            
            
            UsuarioBO usuarioBO = empleadoBO.getUsuarioBO();
            if (usuarioBO==null){
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("No se pudo recuperar la información de Usuario del empleado para registrar el conteo de Dinero.");
                return response;
            }
            
            
            
            WsItemCuentaDineroResponse wsItemResponse = new WsItemCuentaDineroResponse();
            
            
            cuentaEfectivoDto.setIdEmpleado(empleadoBO.getEmpleado().getIdEmpleado());
            try{
                cuentaEfectivoDto.setFechaHora(cuentaDineroDtoRequest.getFechaHora() != null?cuentaDineroDtoRequest.getFechaHora() : (ZonaHorariaBO.DateZonaHorariaByIdEmpresa(getConn(), new Date(), (int)empleadoBO.getEmpleado().getIdEmpresa()).getTime()) );
            }catch(Exception e){
                cuentaEfectivoDto.setFechaHora(cuentaDineroDtoRequest.getFechaHora() != null?cuentaDineroDtoRequest.getFechaHora() : new Date());
            }
            
            cuentaEfectivoDto.setBillete1000(cuentaDineroDtoRequest.getBillete1000());
            cuentaEfectivoDto.setBillete500(cuentaDineroDtoRequest.getBillete500());
            cuentaEfectivoDto.setBillete200(cuentaDineroDtoRequest.getBillete200());
            cuentaEfectivoDto.setBillete100(cuentaDineroDtoRequest.getBillete100());
            cuentaEfectivoDto.setBillete50(cuentaDineroDtoRequest.getBillete50());
            cuentaEfectivoDto.setBillete20(cuentaDineroDtoRequest.getBillete20());
            cuentaEfectivoDto.setMoneda20(cuentaDineroDtoRequest.getMoneda20());
            cuentaEfectivoDto.setMoneda10(cuentaDineroDtoRequest.getMoneda10());
            cuentaEfectivoDto.setMoneda5(cuentaDineroDtoRequest.getMoneda5());
            cuentaEfectivoDto.setMoneda2(cuentaDineroDtoRequest.getMoneda2());
            cuentaEfectivoDto.setMoneda1(cuentaDineroDtoRequest.getMoneda1());
            cuentaEfectivoDto.setMoneda050(cuentaDineroDtoRequest.getMoneda0_50());
            cuentaEfectivoDto.setMoneda020(cuentaDineroDtoRequest.getMoneda0_20());
            cuentaEfectivoDto.setMoneda010(cuentaDineroDtoRequest.getMoneda0_10());            
            
            
            CuentaEfectivoPk cuentaEfectivoPk = cuentaEfectivoDaoImpl.insert(cuentaEfectivoDto);            
            response.setIdObjetoCreado(cuentaEfectivoPk.getIdCuentaEfectivo());
            
            //registramos ubicacion
            try{actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            
            
        }catch(Exception ex){
            ex.printStackTrace();
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al insertar la Cuenta de Dinero. " + ex.getLocalizedMessage());
        }finally{                
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}              
        }
        
        return response;
        
    }
    
    
    
    public WSResponseInsert insertaActualizaCronometroByEmpleado(String empleadoDtoRequestJson, String cronometroDtoRequestJson){
        WSResponseInsert response;
        EmpleadoDtoRequest empleadoDtoRequest = null;
        CronometroDtoRequest cronometroDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJson, EmpleadoDtoRequest.class);
            cronometroDtoRequest = gson.fromJson(cronometroDtoRequestJson, CronometroDtoRequest.class);
            response = this.insertaActualizaCronometroByEmpleado(empleadoDtoRequest,cronometroDtoRequest);
        }catch(Exception ex){
            response = new WSResponseInsert();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        return response;
    }
    
    
    public WSResponseInsert insertaActualizaCronometroByEmpleado(EmpleadoDtoRequest empleadoDtoRequest, CronometroDtoRequest cronometroDtoRequest){
        WSResponseInsert response = new WSResponseInsert();
        Configuration appConfig = new Configuration();
        
        int idEmpresa = 0 ;
        String rfcEmpresaMatriz ="";
        
        Cronometro cronometroEmp = new Cronometro();
        CronometroDaoImpl cronometroDaoImpl = new CronometroDaoImpl(getConn());
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            Empresa empresaDto;
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())){
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
            }else{
                try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
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
            
            UsuarioBO usuarioBO = empleadoBO.getUsuarioBO();
            if (usuarioBO==null){
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("No se pudo recuperar la información de Usuario del empleado para registrar el prospecto.");
                return response;
            }
            
            if(cronometroDtoRequest.getIdCronometro()> 0){
                cronometroEmp = cronometroDaoImpl.findByPrimaryKey(cronometroDtoRequest.getIdCronometro());
            }
            
            cronometroEmp.setIdEmpleado(empleadoBO.getEmpleado().getIdEmpleado());           
            cronometroEmp.setIdEmpresa(idEmpresa);
            cronometroEmp.setIdCliente(cronometroDtoRequest.getIdCliente());
            cronometroEmp.setFechaInicio(cronometroDtoRequest.getFechaInicio());   
            cronometroEmp.setFechaFin(cronometroDtoRequest.getFechaFin());   
            cronometroEmp.setLatitud(cronometroDtoRequest.getLatitud());
            cronometroEmp.setLongitud(cronometroDtoRequest.getLongitud());         
            cronometroEmp.setIdEstatus(cronometroDtoRequest.getIdEstatus());
            
            
            if(cronometroDtoRequest.getIdCronometro() <= 0){
                //Insert
                CronometroPk cronometroPk = cronometroDaoImpl.insert(cronometroEmp);
                response.setIdObjetoCreado(cronometroPk.getIdCronometro());
            }else{
                //update
                cronometroDaoImpl.update(cronometroEmp.createPk(), cronometroEmp);
                response.setIdObjetoCreado(cronometroEmp.getIdCronometro());
            }
            
            //registramos ubicacion
            try{actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
        }catch(Exception ex){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al crear o actualizar Cronometro e Empleado. " + ex.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return response;
    }
    
    public WSResponseInsert insertaActualizaPuntoInteres(String empleadoDtoRequestJson, String puntoInteresDtoRequestJson){
        WSResponseInsert response;
        EmpleadoDtoRequest empleadoDtoRequest = null;
        PuntoInteresDtoRequest puntoInteresDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJson, EmpleadoDtoRequest.class);
            puntoInteresDtoRequest = gson.fromJson(puntoInteresDtoRequestJson, PuntoInteresDtoRequest.class);
            response = this.insertaActualizaPuntoInteres(empleadoDtoRequest, puntoInteresDtoRequest);
        }catch(Exception ex){
            response = new WSResponseInsert();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        return response;
    }
    
    public WSResponseInsert insertaActualizaPuntoInteres(EmpleadoDtoRequest empleadoDtoRequest, PuntoInteresDtoRequest puntoInteresDtoRequest){
        WSResponseInsert response = new WSResponseInsert();
        Configuration appConfig = new Configuration();
        int idEmpresa = 0 ;
        
        PuntosInteres puntoEmp = new PuntosInteres();
        PuntosInteresDaoImpl puntosDao = new PuntosInteresDaoImpl(getConn());
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            Empresa empresaDto;
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())){
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
            }else{
                try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                return response;
            }
            
            //Matriz de la empresa
            try{
                empresaDto = new EmpresaBO(getConn()).getEmpresaMatriz(idEmpresa);
            }catch(Exception ex){
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("No se pudo recuperar la información de la empresa del empleado. Verifique con su administrador de sistema." + ex.toString());
                return response;
            }
            
            UsuarioBO usuarioBO = empleadoBO.getUsuarioBO();
            if (usuarioBO==null){
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("No se pudo recuperar la información de Usuario del empleado para registrar el prospecto.");
                return response;
            }
            
            if(puntoInteresDtoRequest.getIdPunto() > 0){
                puntoEmp = puntosDao.findByPrimaryKey(puntoInteresDtoRequest.getIdPunto());
            }
            
            puntoEmp.setIdEmpresa(idEmpresa);
            puntoEmp.setNombre(puntoInteresDtoRequest.getNombre());
            puntoEmp.setDescripcion(puntoInteresDtoRequest.getDescripcion());
            puntoEmp.setLatitud(puntoInteresDtoRequest.getLatitud());
            puntoEmp.setLongitud(puntoInteresDtoRequest.getLongitud());
            puntoEmp.setIdTipoPunto(puntoInteresDtoRequest.getIdTipoPunto());
            puntoEmp.setDireccion(puntoInteresDtoRequest.getDireccion());
            puntoEmp.setImagen("");
            
            if(puntoEmp.getIdPunto() <= 0){
                //Insert
                PuntosInteresPk interesPk = puntosDao.insert(puntoEmp);
                response.setIdObjetoCreado(interesPk.getIdPunto());
            }else{
                //Update
                puntosDao.update(puntoEmp.createPk(), puntoEmp);
                response.setIdObjetoCreado(puntoEmp.getIdPunto());
            }
            
            //registramos ubicacion
            try{actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
        }catch(Exception e){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al crear o actualizar Cronometro e Empleado. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return response;
    }
    
    public WSResponse insertaAutoServicioInventarioByEmpleado(String empleadoDtoRequestJson, String autoServicioInventarioDtoRequestJson){
        WSResponse response;
        EmpleadoDtoRequest empleadoDtoRequest = null;
        AutoServicioInventarioRequest autoServicioInventarioRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJson, EmpleadoDtoRequest.class);
            autoServicioInventarioRequest = gson.fromJson(autoServicioInventarioDtoRequestJson, AutoServicioInventarioRequest.class);
            response = this.insertaAutoServicioInventarioByEmpleado(empleadoDtoRequest, autoServicioInventarioRequest);
        }catch(Exception ex){
            response = new WSResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        return response;
    }
    
    public WSResponse insertaAutoServicioInventarioByEmpleado(EmpleadoDtoRequest empleadoDtoRequest, AutoServicioInventarioRequest autoServicioInventarioDtoRequest){
        WSResponse response = new WSResponse();
        
        int idEmpresa;
        
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())){
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
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
            
            //validaciones previas
            String msgError = "";
            if (autoServicioInventarioDtoRequest.getTipoMovimiento()!=1
                    && autoServicioInventarioDtoRequest.getTipoMovimiento()!=2
                    && autoServicioInventarioDtoRequest.getTipoMovimiento()!=3){
                msgError += "El tipo de Movimiento no es válido, solo se permiten los valores: 1 - CARGA, 2 - DEVOLUCION, 3 - MERMA.\n";
            }
            if (autoServicioInventarioDtoRequest.getWsItemConceptoRequest() == null
                    || autoServicioInventarioDtoRequest.getWsItemConceptoRequest().length<=0)
                msgError += "No ha agregado ningún producto.\n";
            if(autoServicioInventarioDtoRequest.getIdAlmacen() < 0 )
                msgError += "El dato 'Almácen' es requerido\n";
        
            if (!msgError.equals("")){
                try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
                response.setError(true);
                response.setNumError(901);
                response.setMsgError(msgError);
                return response;
            }else{
                
                Almacen almacen = new AlmacenBO(autoServicioInventarioDtoRequest.getIdAlmacen(), getConn()).getAlmacen();
                
                FormatoMovimientosSesion formatoMovimientosSesion = new FormatoMovimientosSesion();
                formatoMovimientosSesion.setAlmacen(almacen);
                if (autoServicioInventarioDtoRequest.getTipoMovimiento()==1){
                    formatoMovimientosSesion.setTipoMovimiento(FormatoMovimientosSesion.TIPO_MOV_EMP_CARGA);
                }else if (autoServicioInventarioDtoRequest.getTipoMovimiento()==2){
                    formatoMovimientosSesion.setTipoMovimiento(FormatoMovimientosSesion.TIPO_MOV_EMP_DEVOLUCION);
                }else if (autoServicioInventarioDtoRequest.getTipoMovimiento()==3){
                    formatoMovimientosSesion.setTipoMovimiento(FormatoMovimientosSesion.TIPO_MOV_EMP_MERMA);
                }
                
                //Agregamos los productos al formato de Sesion
                for (WsItemConceptoRequest itemConceptoReq : autoServicioInventarioDtoRequest.getWsItemConceptoRequest()){
                    MovimientoSesion movSesion = new MovimientoSesion();
                    movSesion.setIdProducto(itemConceptoReq.getIdConcepto());
                    movSesion.setCantidad(itemConceptoReq.getCantidad());
                    
                    formatoMovimientosSesion.getListaMovimiento().add(movSesion);
                }
                
                //Procesamos Asignacion o Devolucion
                // en caso de error se recibira un Exception
                EmpleadoInventarioRepartidorBO empleadoInventarioRepartidorBO = new EmpleadoInventarioRepartidorBO(getConn());
                empleadoInventarioRepartidorBO.asignarDevolverInventarioRepartidor(
                        idEmpresa, empleadoBO.getEmpleado().getIdEmpleado(), 
                        formatoMovimientosSesion, autoServicioInventarioDtoRequest.isActivarInventarioInicial());
                
            }
            
            //registramos ubicacion
            try{actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
        }catch(Exception ex){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error al procesar Auto Servicio de Inventario de Empleado: " + ex.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return response;
    }
}
