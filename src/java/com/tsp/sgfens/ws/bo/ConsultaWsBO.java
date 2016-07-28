/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.bo;

import com.google.gson.Gson;
import com.tsp.sct.bo.*;
import com.tsp.sct.config.Configuration;
import com.tsp.sct.dao.dto.*;
import com.tsp.sct.dao.jdbc.*;
import com.tsp.sct.util.DateManage;
import com.tsp.sct.util.Encrypter;
import com.tsp.sct.util.FileManage;
import com.tsp.sct.util.StringManage;
import com.tsp.sgfens.ws.request.ConceptoDtoRequest;
import com.tsp.sgfens.ws.request.EmpleadoDtoRequest;
import com.tsp.sgfens.ws.request.GetMensajesMovilRequest;
import com.tsp.sgfens.ws.response.*;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 09-abr-2013 
 * 
 * Clase contenedora de métodos apropiados para 
 * hacer consultas al sistema mediante web service
 */
public class ConsultaWsBO {
    
    private final Gson gson = new Gson();
    private Connection conn = null;

    public Connection getConn() {
        if (this.conn==null){
            try {
                this.conn= ResourceManager.getConnection();
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
    
    
    /**
     * Verifica si los datos de acceso del empleado son válidos
     * @param empleadoDtoRequest
     * @return AccionCatalogoResponse Respuesta compuesta por objeto complejo
     */
    public LoginEmpleadoMovilResponse loginByEmpleado(String empleadoDtoRequestJSON) {
        LoginEmpleadoMovilResponse response;
        
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            
            response = this.loginByEmpleado(empleadoDtoRequest);
        }catch(Exception ex){
            response = new LoginEmpleadoMovilResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }
    
    /**
     * Verifica si los datos de acceso del empleado son válidos
     * @param empleadoDtoRequest
     * @return AccionCatalogoResponse Respuesta compuesta por objeto complejo
     */
    public LoginEmpleadoMovilResponse loginByEmpleado(EmpleadoDtoRequest empleadoDtoRequest) {
        LoginEmpleadoMovilResponse response = new LoginEmpleadoMovilResponse();
         int idEmpresa = 0;
        try {
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                if (empleadoBO.dispositivoAsignado(empleadoDtoRequest.getDispositivoIMEI())){
                    
                    DispositivoMovilBO dispositivoMovilBO = new DispositivoMovilBO(empleadoBO.getEmpleado().getIdDispositivo(), getConn());
                    if (dispositivoMovilBO.getDispositivoMovil().getReporteRobo()==0){

                        //response.setIdEmpleado((int)empleadoBO.getEmpleado().getIdEmpleado());
                        Empleado empleadoDto = empleadoBO.getEmpleado();
                        WsItemEmpleado wsItemEmpleado= new WsItemEmpleado();
                        wsItemEmpleado.setApellidoMaterno(empleadoDto.getApellidoMaterno());
                        wsItemEmpleado.setApellidoPaterno(empleadoDto.getApellidoPaterno());
                        wsItemEmpleado.setCorreoElectronico(empleadoDto.getCorreoElectronico());
                        wsItemEmpleado.setIdDispositivo(empleadoDto.getIdDispositivo());
                        wsItemEmpleado.setIdEmpleado(empleadoDto.getIdEmpleado());
                        wsItemEmpleado.setIdEmpresa(empleadoDto.getIdEmpresa());
                        wsItemEmpleado.setIdEstatus(empleadoDto.getIdEstatus());
                        wsItemEmpleado.setIdSucursal(empleadoDto.getIdSucursal());
                        wsItemEmpleado.setLatitud(empleadoDto.getLatitud());
                        wsItemEmpleado.setLongitud(empleadoDto.getLongitud());
                        wsItemEmpleado.setNombre(empleadoDto.getNombre());
                        wsItemEmpleado.setNumEmpleado(empleadoDto.getNumEmpleado());
                        wsItemEmpleado.setTelefonoLocal(empleadoDto.getTelefonoLocal());
                        wsItemEmpleado.setIdMovilEmpleadoRol(empleadoDto.getIdMovilEmpleadoRol());
                        wsItemEmpleado.setPermisoVentaRapida(empleadoDto.getPermisoVentaRapida());
                        wsItemEmpleado.setPorcentajeComision(empleadoDto.getPorcentajeComision());
                        wsItemEmpleado.setPermisoVentaCredito(empleadoDto.getPermisoVentaCredito());
                        wsItemEmpleado.setTrabajaFueraLinea(empleadoDto.getTrabajarFueraLinea());
                        wsItemEmpleado.setClientesCodigoBarras(empleadoDto.getClientesCodigoBarras());
                        wsItemEmpleado.setDistanciaObligatoria(empleadoDto.getDistanciaObligatorio());
                        wsItemEmpleado.setPrecioCompra(empleadoDto.getPrecioDeCompra());
                        wsItemEmpleado.setPermisoCrearClientes(empleadoDto.getPermisoCrearCliente());
                        wsItemEmpleado.setPermisoAccionesClientes(empleadoDto.getPermisoAccionesCliente());
                        wsItemEmpleado.setPermisoVentaConsigna(empleadoDto.getVentaConsigna());
                        wsItemEmpleado.setComisionConsigna(empleadoDto.getPorcentajeComision());
                        wsItemEmpleado.setPermisoDevoluciones(empleadoDto.getPermisoDevoluciones());
                        wsItemEmpleado.setPermisoAutoServInventario(empleadoDto.getPermisoAutoServInventario());
                        wsItemEmpleado.setPermisoNoCobroParcial(empleadoDto.getPermisoNoCobroParcial());
                        wsItemEmpleado.setPermisoVerProveedores(empleadoDto.getPermisoVerProveedores());
                        wsItemEmpleado.setIntervaloUbicacionSegundos(empleadoDto.getIntervaloUbicacionSeg());
                        response.setWsItemEmpleado(wsItemEmpleado);
                        
                        //**Se valida el ultimo inventario del vendedor para ver cual es la hora:
                        InventarioInicialVendedor inventarioInicial = null;
                        try{
                            inventarioInicial =  new InventarioInicialVendedorDaoImpl(getConn()).findWhereIdEmpleadoEquals(empleadoDto.getIdEmpleado())[0];
                        }catch(Exception e){}
                        if(inventarioInicial != null){
                            response.setFechaHoraUltimoInventarioInicial(inventarioInicial.getFechaRegistro());
                        }else{
                            try{
                                response.setFechaHoraUltimoInventarioInicial(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(getConn(), new Date(), (int)empleadoBO.getEmpleado().getIdEmpresa()).getTime());
                            }catch(Exception e){
                                response.setFechaHoraUltimoInventarioInicial(new Date());
                            }
                        }
                        //**
                        
                        //recuperamos datos de la empresa a la q pertenece el empleado
                        EmpresaBO empresaBO = new EmpresaBO(getConn());
                        Empresa empresaDto = empresaBO.findEmpresabyId(empleadoDto.getIdEmpresa());

                        WsItemSucursal wsItemSucursal = new WsItemSucursal();
                        WsItemUbicacionFiscal wsItemUbicacionFiscal = null;
                        Ubicacion wsUbicacion = null;

                        wsItemSucursal.setIdEmpresa((int)empresaDto.getIdEmpresa());
                        idEmpresa = (int)empresaDto.getIdEmpresa();
                        wsItemSucursal.setIdEmpresaPadre((int)empresaDto.getIdEmpresaPadre());
                        wsItemSucursal.setIdTipoEmpresa((int)empresaDto.getIdTipoEmpresa());
                        wsItemSucursal.setRfc(empresaDto.getRfc());
                        wsItemSucursal.setRazonSocial(empresaDto.getRazonSocial());
                        wsItemSucursal.setNombreComercial(empresaDto.getNombreComercial()); 
                        //wsItemSucursal.setLatitud(empresaDto.getLatitud());
                        //wsItemSucursal.setLongitud(empresaDto.getLongitud());
                        wsItemSucursal.setFoliosDisponibles(empresaDto.getFoliosDisponibles());
                        wsItemSucursal.setRegimenFiscal(empresaDto.getRegimenFiscal());
                        wsItemSucursal.setMensajePersonalizadoVisita(empresaDto.getMensajePersonalizadoVisita());
                        wsItemSucursal.setPrintNombreComercial(empresaDto.getPrintNombreComercial());
                        wsItemSucursal.setPrintRazonSocial(empresaDto.getPrintRazonSocial());
                        wsItemSucursal.setPrintTkProds(empresaDto.getPrintTkProds());
                        wsItemSucursal.setTkMovilTipo(empresaDto.getTkMovilTipo());
                        wsItemSucursal.setTkMovilPorMarca(empresaDto.getTkMovilPorMarca());
                        wsItemSucursal.setTkMovilMostrarZona(empresaDto.getTkMovilMostrarZona());
                        wsItemSucursal.setTkMovilMostrarFolio(empresaDto.getTkMovilMostrarFolio());
                        
                        ImagenPersonal logoDto = null;
                        try{
                            ImagenPersonalBO imgLogoBO = new ImagenPersonalBO(this.getConn());
                            logoDto = imgLogoBO.findImagenPersonalByEmpresa((int)empresaDto.getIdEmpresa());
                            
                            if(logoDto!=null){
                                if(logoDto.getNombreImagen()!=null && !logoDto.getNombreImagen().equals("")){
                                    wsItemSucursal.setLogo(FileManage.getBytesFromFile(new File("C:/SystemaDeArchivos/"+ empresaDto.getRfc()+ "/"+logoDto.getNombreImagen())));
                                }
                            }   
                               
                        }catch(Exception ex){
                            response.setError(true);
                            response.setNumError(905);
                            response.setMsgError("Error al convertir la imagen. " + ex.getLocalizedMessage());
                        }
                        
                        
                        //Facturacion y Cobro tarjeta 
                        try{
                            EmpresaPermisoAplicacionDaoImpl aplicacionDaoImpl = new EmpresaPermisoAplicacionDaoImpl(getConn());
                            EmpresaPermisoAplicacion permisosEmpresa = aplicacionDaoImpl.findByPrimaryKey(empleadoDto.getIdEmpresa());
                            if(permisosEmpresa.getAccesoSgfensFacturacion() == 1){
                                wsItemSucursal.setFactura_activa(1);
                            }else{
                                wsItemSucursal.setFactura_activa(2);
                            }
                            if(permisosEmpresa.getAccesoSgfensCobrotarjeta()== 1){
                                wsItemSucursal.setCobro_tarjeta_activa(1);
                            }else{
                                wsItemSucursal.setCobro_tarjeta_activa(2);
                            }
                            wsItemSucursal.setTipoConsumoServicio(permisosEmpresa.getTipoConsumoServicio());
                            wsItemSucursal.setRfcPorNipCodigo(permisosEmpresa.getRfcPorNipCodigo());
                        }catch(Exception ex){
                            wsItemSucursal.setFactura_activa(1);
                            wsItemSucursal.setCobro_tarjeta_activa(1);
                        }
                        
                        //cargamos la ubicacion fiscal de esa sucursal                            
                        wsUbicacion = new UbicacionDaoImpl(getConn()).findByPrimaryKey(empresaDto.getIdUbicacionFiscal());

                        if (wsUbicacion!=null){
                            wsItemUbicacionFiscal = new WsItemUbicacionFiscal();

                            wsItemUbicacionFiscal.setCalle(wsUbicacion.getCalle());
                            wsItemUbicacionFiscal.setCodigoPostal(wsUbicacion.getCodigoPostal());
                            wsItemUbicacionFiscal.setColonia(wsUbicacion.getColonia());
                            wsItemUbicacionFiscal.setEstado(wsUbicacion.getEstado());
                            wsItemUbicacionFiscal.setIdUbicacionFiscal(wsUbicacion.getIdUbicacion());
                            wsItemUbicacionFiscal.setMunicipio(wsUbicacion.getMunicipio());
                            wsItemUbicacionFiscal.setNumExt(wsUbicacion.getNumExt());
                            wsItemUbicacionFiscal.setNumInt(wsUbicacion.getNumInt());
                            wsItemUbicacionFiscal.setPais(wsUbicacion.getPais());
                        }

                        wsItemSucursal.setUbicacionFiscal(wsItemUbicacionFiscal);


                        //Datos de TPV tipo servicio
                        {
                            TpvTipoServicio tpvTipoServicio = empresaBO.getTPVTipoServicio(empresaDto.getIdEmpresa());

                            if (tpvTipoServicio!=null){
                                WsItemTipoServicio wsItemTipoServicio = new WsItemTipoServicio();
                                wsItemTipoServicio.setIdTipoServicio(tpvTipoServicio.getIdTpvTipoServicio());
                                wsItemTipoServicio.setMontoMaximoCobroTDC(tpvTipoServicio.getMontoMaximoCobroTdc());
                                wsItemTipoServicio.setNombreServicio(tpvTipoServicio.getNombreServicio());

                                response.setWsItemTipoServicio(wsItemTipoServicio);
                            }/*else{
                                response.setError(true);
                                response.setNumError(902);
                                response.setMsgError("La empresa matriz del empleado no tiene asignado un TPV Tipo Servicio, contacte con su proveedor.");
                            }*/
                        }
                        
                        //Consumo de Creditos Operacion
                        try{
                            // Si el ultimo acceso del usuario fue en día distinto
                            // se le descuentan creditos por login
                            // Si ya hizo login el mismo día no se le descuenta
                            if (!DateManage.isOnDate(new Date(), empleadoBO.getUsuarioBO().getFechaAccesoAnterior())){
                                BitacoraCreditosOperacionBO bcoBO = new BitacoraCreditosOperacionBO(getConn());
                                bcoBO.registraDescuento(empleadoBO.getUsuarioBO().getUser(), 
                                        BitacoraCreditosOperacionBO.CONSUMO_ACCION_WS_LOGIN, 
                                        null, 0, 0, 0, 
                                        "Login WS Movil", null, true);
                            }
                            Empresa empresaMatriz = empresaBO.getEmpresaMatriz(empleadoDto.getIdEmpresa());
                            wsItemSucursal.setCreditosOperacionDisponibles(empresaMatriz.getCreditosOperacion());
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                        
                        try{
                            //buscamos primero configuracion específica de empleado
                            PosMovilEstatusParametros[] parametrosMovilEstatus = new PosMovilEstatusParametrosDaoImpl(getConn()).findWhereIdEmpleadoEquals(empleadoDto.getIdEmpleado());
                            //Si no encontramos conf especifica de empleado, buscamos la de la empresa a la q pertenece
                            if (parametrosMovilEstatus.length<=0)
                                parametrosMovilEstatus = new PosMovilEstatusParametrosDaoImpl(getConn()).findWhereIdEmpresaEquals(idEmpresa);
                            
                            if (parametrosMovilEstatus.length>0){
                                response.setTiempoEstatus(parametrosMovilEstatus[0].getTiempoMinutosActualiza());
                                response.setTiempoRecordatorio(parametrosMovilEstatus[0].getTMinutosRecordatorio());
                            }
                        }catch(Exception ex){
                        }
                        
                        //Horario asignado a empleado
                        try{
                            if (empleadoDto.getIdHorario()>0){
                                HorarioDetalle[] horarioDetalles =  new HorarioDetalleDaoImpl(getConn()).findWhereIdHorarioEquals(empleadoDto.getIdHorario());
                                for (HorarioDetalle hd : horarioDetalles){
                                    WsItemHorarioDetalle wsHorarioDetalle = new WsItemHorarioDetalle();
                                    
                                    wsHorarioDetalle.setIdDetalleHorario(hd.getIdDetalleHorario());
                                    wsHorarioDetalle.setIdHorario(hd.getIdHorario());
                                    wsHorarioDetalle.setDia(hd.getDia());
                                    wsHorarioDetalle.setHoraEntrada(asignarDiaATime(hd.getHoraEntrada()));
                                    wsHorarioDetalle.setHoraSalida(asignarDiaATime(hd.getHoraSalida()));
                                    wsHorarioDetalle.setDiaDescanso(hd.getDiaDescanso());
                                    wsHorarioDetalle.setComidaSalida(asignarDiaATime(hd.getComidaSalida()));
                                    wsHorarioDetalle.setComidaEntrada(asignarDiaATime(hd.getComidaEntrada()));
                                    wsHorarioDetalle.setPeriodoComida(hd.getPeriodoComida());
                                    wsHorarioDetalle.setTolerancia(hd.getTolerancia());
                                    
                                    response.getWsItemHorarioDetalle().add(wsHorarioDetalle);
                                }
                            }
                        }catch(Exception ex){
                        }
                        
                        //Serie para Pedidos moviles asignada a empleado
                        try{
                            if (empleadoDto.getIdFolioMovilEmpleado()>0){
                                FoliosMovilEmpleado foliosMovilEmpleado = new FoliosMovilEmpleadoBO(empleadoDto.getIdFolioMovilEmpleado(), conn).getFoliosMovilEmpleado();
                                if (foliosMovilEmpleado!=null){
                                    WsItemFoliosMovilEmpleado wsItemFoliosMovilEmpleado = new WsItemFoliosMovilEmpleado();
                                    wsItemFoliosMovilEmpleado.setFechaGeneracion(foliosMovilEmpleado.getFechaGeneracion());
                                    wsItemFoliosMovilEmpleado.setFolioDesde(foliosMovilEmpleado.getFolioDesde());
                                    wsItemFoliosMovilEmpleado.setFolioHasta(foliosMovilEmpleado.getFolioHasta());
                                    wsItemFoliosMovilEmpleado.setIdEmpresa(foliosMovilEmpleado.getIdEmpresa());
                                    wsItemFoliosMovilEmpleado.setIdEstatus(foliosMovilEmpleado.getIdEstatus());
                                    wsItemFoliosMovilEmpleado.setIdFolioMovilEmpleado(foliosMovilEmpleado.getIdFolioMovilEmpleado());
                                    wsItemFoliosMovilEmpleado.setSerie(foliosMovilEmpleado.getSerie());
                                    wsItemFoliosMovilEmpleado.setTipoFolioMovil(foliosMovilEmpleado.getTipoFolioMovil());
                                    wsItemFoliosMovilEmpleado.setUltimoFolio(foliosMovilEmpleado.getUltimoFolio());
                                    
                                    response.getWsItemFoliosMovilEmpleado().add(wsItemFoliosMovilEmpleado);
                                }
                            }
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                        
                        //Region, Zona o Area del empleado
                        try{
                            if (empleadoDto.getIdRegion()>0){
                                Region regionDto =  new RegionBO(empleadoDto.getIdRegion(), conn).getRegion();
                                if (regionDto!=null){
                                    WsItemRegion wsItemRegion = new WsItemRegion();
                                    wsItemRegion.setIdRegion(regionDto.getIdRegion());
                                    wsItemRegion.setNombre(regionDto.getNombre());
                                    wsItemRegion.setDescripcion(regionDto.getDescripcion());
                                    
                                    response.setWsItemRegion(wsItemRegion);
                                }
                            }
                        }catch(Exception ex){
                            
                        }
                        
                        response.setWsItemSucursal(wsItemSucursal);
                        response.setIntervaloUbicacionSegundos(empleadoDto.getIntervaloUbicacionSeg());

                        response.setError(false);
                    }else{
                        response.setError(true);
                        response.setNumError(901);
                        response.setMsgError("El dispositivo tiene reporte de robo.");
                    }
                }else{
                    response.setError(true);
                    response.setNumError(901);
                    response.setMsgError("El dispositivo no esta asignado al empleado.");
                }
            } else {
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("Datos de acceso inválidos.");
            }
        } catch (Exception e) {
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error al verificar login del empleado: " + e.toString());
            e.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
         
        return response;
    }
    
    
    public  ConsultaMensajesMovilResponse getMensajesMovilRecibidos(String empleadoDtoRequestJSON, String getMensajesMovilRequestJSON){
        
        ConsultaMensajesMovilResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest = null;
        GetMensajesMovilRequest getMensajesMovilRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            getMensajesMovilRequest = gson.fromJson(getMensajesMovilRequestJSON, GetMensajesMovilRequest.class);
            
            response = this.getMensajesMovilRecibidos(empleadoDtoRequest,getMensajesMovilRequest);
        }catch(Exception ex){
            response = new ConsultaMensajesMovilResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
        
    }
    
    public  ConsultaMensajesMovilResponse getMensajesMovilRecibidos(EmpleadoDtoRequest empleadoDtoRequest, GetMensajesMovilRequest getMensajesMovilRequest){
        ConsultaMensajesMovilResponse response = new ConsultaMensajesMovilResponse();
        
        try {
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                //int idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                long idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                
                /*if (idEmpresa > 0) {
                    
                }*/
                MovilMensaje[] movilMensajeDtoA = new MovilMensajeBO(getConn()).getMovilMensajesByFilter(
                        true, 
                        getMensajesMovilRequest.getFiltroFechaInicio(), 
                        getMensajesMovilRequest.getFiltroFechaFinal(), 
                        getMensajesMovilRequest.isFiltroNoRecibidos(),
                        getMensajesMovilRequest.isFiltroComunicacionConsola(), 
                        getMensajesMovilRequest.getFiltroIdReceptor(), 
                        getMensajesMovilRequest.getFiltroIdEmisor());
                
                response.setWsItemMovilMensajes(new ArrayList<WsItemMovilMensaje>());
                for (MovilMensaje msg:movilMensajeDtoA){
                    WsItemMovilMensaje wsItemMovilMensaje = new WsItemMovilMensaje();
                    
                    wsItemMovilMensaje.setEmisorTipo(msg.getEmisorTipo());
                    wsItemMovilMensaje.setFechaEmision(msg.getFechaEmision());
                    wsItemMovilMensaje.setFechaRecepcion(msg.getFechaRecepcion());
                    wsItemMovilMensaje.setIdEmpleadoEmisor(msg.getIdEmpleadoEmisor());
                    wsItemMovilMensaje.setIdEmpleadoReceptor(msg.getIdEmpleadoReceptor());
                    wsItemMovilMensaje.setIdMovilMensaje(msg.getIdMovilMensaje());
                    wsItemMovilMensaje.setMensaje(msg.getMensaje());
                    wsItemMovilMensaje.setReceptorTipo(msg.getReceptorTipo());
                    wsItemMovilMensaje.setRecibido(msg.getRecibido()==1);
                    
                    
                    response.getWsItemMovilMensajes().add(wsItemMovilMensaje);
                    
                    //Actualizamos registro a leido
                    msg.setRecibido(1);
                    try{
                        msg.setFechaRecepcion(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(getConn(), new Date(), (int)empleadoBO.getEmpleado().getIdEmpresa()).getTime());
                    }catch(Exception e){
                        msg.setFechaRecepcion(new Date());
                    }
                    new MovilMensajeDaoImpl(getConn()).update(msg.createPk(), msg);
                }
                        
                response.setError(false);
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
                
                //Recuperamos información sobre reporte de robo
                try{
                    DispositivoMovilBO dispositivoMovilBO = new DispositivoMovilBO(empleadoBO.getEmpleado().getIdDispositivo(), getConn());
                    if (dispositivoMovilBO.getDispositivoMovil()!=null){
                        response.setReporteRobo( dispositivoMovilBO.getDispositivoMovil().getReporteRobo()!=0 );
                    }
                }catch(Exception ex){}
                
                //Recuperamos información de Créditos de Operación de la empresa
                try{
                    Empresa empresaMatriz = new EmpresaBO(getConn()).getEmpresaMatriz(empleadoBO.getEmpleado().getIdEmpresa());
                    response.setCreditosOperacionDisponibles(empresaMatriz.getCreditosOperacion());
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                
            } else {
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al consultar mensajes del empleado. " + e.toString());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    public  ConsultaMensajesMovilResponse getMensajesMovilEnviados(String empleadoDtoRequestJSON, String getMensajesMovilRequestJSON){
        
        ConsultaMensajesMovilResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest = null;
        GetMensajesMovilRequest getMensajesMovilRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            getMensajesMovilRequest = gson.fromJson(getMensajesMovilRequestJSON, GetMensajesMovilRequest.class);
            
            response = this.getMensajesMovilEnviados(empleadoDtoRequest,getMensajesMovilRequest);
        }catch(Exception ex){
            response = new ConsultaMensajesMovilResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
        
    }
    
    public  ConsultaMensajesMovilResponse getMensajesMovilEnviados(EmpleadoDtoRequest empleadoDtoRequest, GetMensajesMovilRequest getMensajesMovilRequest){
        ConsultaMensajesMovilResponse response = new ConsultaMensajesMovilResponse();
        
        try {
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                //int idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                long idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                
                /*if (idEmpresa > 0) {
                    
                }*/
                MovilMensaje[] movilMensajeDtoA = new MovilMensajeBO(getConn()).getMovilMensajesByFilter(
                        false, 
                        getMensajesMovilRequest.getFiltroFechaInicio(), 
                        getMensajesMovilRequest.getFiltroFechaFinal(), 
                        getMensajesMovilRequest.isFiltroNoRecibidos(),
                        getMensajesMovilRequest.isFiltroComunicacionConsola(), 
                        getMensajesMovilRequest.getFiltroIdReceptor(), 
                        getMensajesMovilRequest.getFiltroIdEmisor());
                
                response.setWsItemMovilMensajes(new ArrayList<WsItemMovilMensaje>());
                for (MovilMensaje msg:movilMensajeDtoA){
                    WsItemMovilMensaje wsItemMovilMensaje = new WsItemMovilMensaje();
                    
                    wsItemMovilMensaje.setEmisorTipo(msg.getEmisorTipo());
                    wsItemMovilMensaje.setFechaEmision(msg.getFechaEmision());
                    wsItemMovilMensaje.setFechaRecepcion(msg.getFechaRecepcion());
                    wsItemMovilMensaje.setIdEmpleadoEmisor(msg.getIdEmpleadoEmisor());
                    wsItemMovilMensaje.setIdEmpleadoReceptor(msg.getIdEmpleadoReceptor());
                    wsItemMovilMensaje.setIdMovilMensaje(msg.getIdMovilMensaje());
                    wsItemMovilMensaje.setMensaje(msg.getMensaje());
                    wsItemMovilMensaje.setReceptorTipo(msg.getReceptorTipo());
                    wsItemMovilMensaje.setRecibido(msg.getRecibido()==1?true:false);
                    
                    
                    response.getWsItemMovilMensajes().add(wsItemMovilMensaje);
                }
                        
                response.setError(false);
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
                
            } else {
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        } catch (Exception e) {
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al consultar ventas del empleado. " + e.toString());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    /**
     * Consulta los empleados que laboran en la misma empresa 
     * que el empleado que solicita los datos y que tienen un dispositivo asignado
     * @param empleadoDtoRequestJSON Cadena en formato JSON representando objeto de tipo EmpleadoDtoRequest
     * @return Objeto ConsultaEmpleadoResponse con listado de empleados recuperados
     */
    public ConsultaEmpleadosResponse getEmpleadosByEmpleado(String empleadoDtoRequestJSON){
        ConsultaEmpleadosResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            
            response = this.getEmpleadosByEmpleado(empleadoDtoRequest);
        }catch(Exception ex){
            response = new ConsultaEmpleadosResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }
    
    /**
     * Consulta los empleados que laboran en la misma empresa 
     * que el empleado que solicita los datos y que tienen un dispositivo asignado
     * @param empleadoDtoRequest
     * @return 
     */
    public ConsultaEmpleadosResponse getEmpleadosByEmpleado(EmpleadoDtoRequest empleadoDtoRequest){
        int idEmpresa = 0;
        ConsultaEmpleadosResponse consultaResponse = new ConsultaEmpleadosResponse();
        try {
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                //Si se encontro el registro buscamos su catalogo de clientes
                if (idEmpresa > 0) {
                    consultaResponse.setError(false);
                    consultaResponse.setWsItemEmpleado(this.getListaEmpleados(idEmpresa, " AND ID_ESTATUS=1 AND ID_DISPOSITIVO>0"));
                }
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            } else {
                consultaResponse.setError(true);
                consultaResponse.setNumError(901);
                consultaResponse.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        } catch (Exception e) {
            consultaResponse.setError(true);
            consultaResponse.setNumError(902);
            consultaResponse.setMsgError("Error inesperado al consultar catalogos del usuario. " + e.getLocalizedMessage());
        } finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }

        return consultaResponse;
    }
    
    public ConsultaConceptosResponse getMediaByConcepto(String empleadoDtoRequestJSON, String conceptoDtoRequestJSON){
        ConsultaConceptosResponse response;
        EmpleadoDtoRequest empleadoDtoRequest = null;
        ConceptoDtoRequest conceptoDtoRequest = null; 
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            conceptoDtoRequest = gson.fromJson(conceptoDtoRequestJSON, ConceptoDtoRequest.class);
            
            response = this.getMediaByConcepto(empleadoDtoRequest, conceptoDtoRequest);
        }catch(Exception ex){
            response = new ConsultaConceptosResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        return response;
    }
    
    public ConsultaConceptosResponse getMediaByConcepto(EmpleadoDtoRequest empleadoDtoRequest, ConceptoDtoRequest conceptoDtoRequest){
        int idEmpresa = 0;
        int idEmpleado = 0;
        ConsultaConceptosResponse consultaConceptosResponse = new ConsultaConceptosResponse();
        try{
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                //Si se encontro el registro buscamos su catalogo de productos
                if (idEmpresa > 0) {
                    consultaConceptosResponse.setError(false);
                    ConceptoBO conceptoBO = new ConceptoBO(getConn());
                    Concepto concepto = conceptoBO.findConceptobyId(conceptoDtoRequest.getIdConcepto());
                    WsItemConcepto itemConcepto = new WsItemConcepto();
                    ArrayList<WsItemConcepto> list = new ArrayList<WsItemConcepto>();
                    boolean isImagen = false;
                    boolean isVideo = false;
                    itemConcepto.setIdConcepto(concepto.getIdConcepto());
                    if(concepto.getRutaImagen() != null){
                        if(!concepto.getRutaImagen().trim().equals("")){
                            try{
                                itemConcepto.setImagen(FileManage.getBytesFromFile(new File(concepto.getRutaImagen())));
                                isImagen = true;
                            }catch(Exception ex){
                                consultaConceptosResponse.setError(true);
                                consultaConceptosResponse.setNumError(905);
                                consultaConceptosResponse.setMsgError("Error al convertir la imagen. " + ex.getLocalizedMessage());
                            }
                        }
                    }
                    if(concepto.getRutaVideo() != null){
                        if(!concepto.getRutaVideo().trim().equals("")){
                            try{
                                itemConcepto.setVideo(FileManage.getBytesFromFile(new File(concepto.getRutaVideo())));
                                isVideo = true;
                            }catch(Exception ex){
                                consultaConceptosResponse.setError(true);
                                consultaConceptosResponse.setNumError(906);
                                consultaConceptosResponse.setMsgError("Error al convertir el video. " + ex.getLocalizedMessage());
                            }
                        }
                    }
                    
                    list.add(itemConcepto);
                    consultaConceptosResponse.setListaConceptos(list);
                    if(list.size() <= 0){
                        consultaConceptosResponse.setError(true);
                        consultaConceptosResponse.setNumError(907);
                        consultaConceptosResponse.setMsgError("El concepto no cuenta con información multimedia");
                    }
                }
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            }else{
                consultaConceptosResponse.setError(true);
                consultaConceptosResponse.setNumError(901);
                consultaConceptosResponse.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        }catch(Exception ex){
            consultaConceptosResponse.setError(true);
            consultaConceptosResponse.setNumError(902);
            consultaConceptosResponse.setMsgError("Error inesperado al consultar catalogos del usuario. " + ex.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return consultaConceptosResponse;
    }
    
    public ConsultaBitacoraCreditosResponse getListaBitacoraCreditosByEmpleado(String empleadoDtoRequestJSON){
        ConsultaBitacoraCreditosResponse response;
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            
            response = this.getListaBitacoraCreditosByEmpleado(empleadoDtoRequest);
            
        }catch(Exception ex){
            response = new ConsultaBitacoraCreditosResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        return response;
    }
    
    public ConsultaBitacoraCreditosResponse getListaBitacoraCreditosByEmpleado(EmpleadoDtoRequest empleadoDtoRequest){
        int idEmpresa = 0;
        int idEmpleado = 0;
        ConsultaBitacoraCreditosResponse response = new ConsultaBitacoraCreditosResponse();
        try{
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdUsuarios();
                //Si se encontro el registro buscamos su catalogo de productos
                if (idEmpresa > 0) {
                    response.setError(false);
                    response.setListaBitacoras(this.getBitacoraCreditos(idEmpresa, idEmpleado));
                }
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            } else {
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
            
        }catch(Exception ex){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al consultar la bitacora de creditos del usuario. " + ex.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return response;
    }
    
    /**
     * Obtiene el catalogo de Conceptos de una empresa, haciendo login por empleado
     * @param empleadoDtoRequestJSON Datos de autenticacion en formato JSON, correspondientes a objeto EmpleadoDtoRequest
     */
    public ConsultaConceptosResponse getCatalogoConceptosByEmpleado(String empleadoDtoRequestJSON) {
        ConsultaConceptosResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            
            response = this.getCatalogoConceptosByEmpleado(empleadoDtoRequest);
        }catch(Exception ex){
            response = new ConsultaConceptosResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }
    
    /**
     * Obtiene el catalogo de Conceptos/servicios de proveedores de una empresa
     * @param conceptoServicioDtoRequestJSON Datos de autenticacion en formato JSON, correspondientes a objeto EmpleadoDtoRequest
     */
    public ConsultaConceptosServiciosResponse getCatalogoConceptosServiciosProveedores(String empleadoDtoRequestJson) {
        ConsultaConceptosServiciosResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJson, EmpleadoDtoRequest.class);
            
            response = this.getCatalogoConceptosServiciosProveedores(empleadoDtoRequest);
        }catch(Exception ex){
            response = new ConsultaConceptosServiciosResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }
    
    /**
     * Obtiene el catalogo de Conceptos/servicios de proveedores
     * @param conceptoServicioDtoRequestJSON Datos de autenticacion en formato JSON, correspondientes a objeto EmpleadoDtoRequest
     */
    public ConsultaProveedorConceptosServiciosResponse getCatalogoConceptosServiciosByProveedor(String empleadoDtoRequestJson) {
        ConsultaProveedorConceptosServiciosResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJson, EmpleadoDtoRequest.class);
            
            response = this.getCatalogoConceptosServiciosProveedoresByProveedor(empleadoDtoRequest);
        }catch(Exception ex){
            response = new ConsultaProveedorConceptosServiciosResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }
    
    /**
     * Obtiene el catalogo de Conceptos de una empresa, haciendo login por empleado
     * @param empleadoDtoRequest Datos de autenticacion
     */
    public ConsultaConceptosResponse getCatalogoConceptosByEmpleado(EmpleadoDtoRequest empleadoDtoRequest) {
        int idEmpresa = 0;
        int idEmpleado = 0;
        ConsultaConceptosResponse consultaConceptosResponse = new ConsultaConceptosResponse();
        try {
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                //Si se encontro el registro buscamos su catalogo de productos
                if (idEmpresa > 0) {
                    consultaConceptosResponse.setError(false);
                    consultaConceptosResponse.setListaConceptos(this.getListaConceptos(idEmpresa, idEmpleado,-1,-1));
                }
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            } else {
                consultaConceptosResponse.setError(true);
                consultaConceptosResponse.setNumError(901);
                consultaConceptosResponse.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        } catch (Exception e) {
            consultaConceptosResponse.setError(true);
            consultaConceptosResponse.setNumError(902);
            consultaConceptosResponse.setMsgError("Error inesperado al consultar catalogos del usuario. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }

        return consultaConceptosResponse;
    }
    
    /**
     * Obtiene el catalogo de Conceptos/servicio de una empresa, haciendo login por empleado
     * @param empleadoDtoRequest Datos de autenticacion
     */
    public ConsultaConceptosServiciosResponse getCatalogoConceptosServiciosProveedores(EmpleadoDtoRequest empleadoDtoRequest) {
        int idEmpresa = 0;
        int idEmpleado = 0;
        ConsultaConceptosServiciosResponse consultaConceptosResponse = new ConsultaConceptosServiciosResponse();
        try {
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                //Si se encontro el registro buscamos su catalogo de productos
                if (idEmpresa > 0) {
                    consultaConceptosResponse.setError(false);
                    consultaConceptosResponse.setListaConceptos(this.getListaConceptosServiciosProveedores(idEmpresa, idEmpleado,-1,-1, 0));
                }
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            } else {
                consultaConceptosResponse.setError(true);
                consultaConceptosResponse.setNumError(901);
                consultaConceptosResponse.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        } catch (Exception e) {
            consultaConceptosResponse.setError(true);
            consultaConceptosResponse.setNumError(902);
            consultaConceptosResponse.setMsgError("Error inesperado al consultar catalogos del usuario. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }

        return consultaConceptosResponse;
    }
    
    /**
     * Obtiene el catalogo de Conceptos/servicio de proveedores, haciendo login por empleado
     * @param empleadoDtoRequest Datos de autenticacion
     */
    public ConsultaProveedorConceptosServiciosResponse getCatalogoConceptosServiciosProveedoresByProveedor(EmpleadoDtoRequest empleadoDtoRequest) {
        int idEmpresa = 0;
        int idEmpleado = 0;
        ConsultaProveedorConceptosServiciosResponse consultaConceptosResponse = new ConsultaProveedorConceptosServiciosResponse();
        try {
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                //Si se encontro el registro buscamos su catalogo de productos
                if (idEmpresa > 0) {
                    consultaConceptosResponse.setError(false);
                    consultaConceptosResponse.setListaConceptos(this.getListaConceptosServiciosProveedoresByProveedor(idEmpresa, idEmpleado,-1,-1, 0));
                }
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            } else {
                consultaConceptosResponse.setError(true);
                consultaConceptosResponse.setNumError(901);
                consultaConceptosResponse.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        } catch (Exception e) {
            consultaConceptosResponse.setError(true);
            consultaConceptosResponse.setNumError(902);
            consultaConceptosResponse.setMsgError("Error inesperado al consultar catalogos del usuario. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }

        return consultaConceptosResponse;
    }
    
    
    /**
     * Obtiene el catalogo de Conceptos de una empresa, haciendo login por empleado
     * @param empleadoDtoRequestJSON Datos de autenticacion en formato JSON, correspondientes a objeto EmpleadoDtoRequest
     */
    public ConsultaConceptosResponse getCatalogoConceptosByEmpleadoWiFi(String empleadoDtoRequestJSON) {
        ConsultaConceptosResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            
            response = this.getCatalogoConceptosByEmpleadoWiFi(empleadoDtoRequest);
        }catch(Exception ex){
            response = new ConsultaConceptosResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }
    
    /**
     * Obtiene el catalogo de Conceptos de una empresa, haciendo login por empleado
     * @param empleadoDtoRequest Datos de autenticacion
     */
    public ConsultaConceptosResponse getCatalogoConceptosByEmpleadoWiFi(EmpleadoDtoRequest empleadoDtoRequest) {
        int idEmpresa = 0;
        int idEmpleado = 0;
        ConsultaConceptosResponse consultaConceptosResponse = new ConsultaConceptosResponse();
        try {
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                //Si se encontro el registro buscamos su catalogo de productos
                if (idEmpresa > 0) {
                    consultaConceptosResponse.setError(false);
                    consultaConceptosResponse.setListaConceptos(this.getListaConceptosWiFi(idEmpresa, idEmpleado,-1,-1));
                }
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            } else {
                consultaConceptosResponse.setError(true);
                consultaConceptosResponse.setNumError(901);
                consultaConceptosResponse.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        } catch (Exception e) {
            consultaConceptosResponse.setError(true);
            consultaConceptosResponse.setNumError(902);
            consultaConceptosResponse.setMsgError("Error inesperado al consultar catalogos del usuario. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }

        return consultaConceptosResponse;
    }
    
    public ConsultaProveedoresResponse getCatalogoProveedores(String empleadoDtoRequestJSON){
        ConsultaProveedoresResponse response;
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            response = this.getCatalogoProveedores(empleadoDtoRequest);
        }catch(Exception ex){
            response = new ConsultaProveedoresResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        return response;
    }
    
    public ConsultaProveedoresResponse getCatalogoProveedores(EmpleadoDtoRequest empleadoDtoRequest){
        int idEmpresa = 0;
        int idUsuario = 0;
        ConsultaProveedoresResponse response = new ConsultaProveedoresResponse();
        try{
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idUsuario = empleadoBO.getUsuarioBO().getUser().getIdUsuarios();
                //Si se encontro el registro buscamos su catalogo de clientes
                if (idEmpresa > 0) {
                    response.setError(false);
                    response.setListaProveedor(this.getListaProveedores(idEmpresa, " AND ID_ESTATUS = 1"));
                }
                 //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            }else {
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        }catch(Exception e){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al consultar catalogos del usuario. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return response;
    }
    
    /**
     * Obtiene el catalogo de Clientes por usuario
     * @param userName 
     */
    public ConsultaClientesResponse getCatalogoClientesByEmpleado(String empleadoDtoRequestJSON) {
        ConsultaClientesResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            
            response = this.getCatalogoClientesByEmpleado(empleadoDtoRequest);
        }catch(Exception ex){
            response = new ConsultaClientesResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }
    
    /**
     * Obtiene el catalogo de Clientes por usuario
     * @param empleadoDtoRequest
     * @param userName 
     */
    public ConsultaClientesResponse getCatalogoClientesByEmpleado(EmpleadoDtoRequest empleadoDtoRequest) {
        int idEmpresa = 0;
        int idUsuario = 0;
        ConsultaClientesResponse consultaResponse = new ConsultaClientesResponse();
        try {
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idUsuario = empleadoBO.getUsuarioBO().getUser().getIdUsuarios();
                //Si se encontro el registro buscamos su catalogo de clientes
                if (idEmpresa > 0) {
                    consultaResponse.setError(false);
                    //Filtramos con Estatus Activo y que esten asignados a este empleado
                    consultaResponse.setListaClientes(this.getListaClientes(idEmpresa, 
                            " AND (ID_ESTATUS = 1 OR ID_ESTATUS = 3)  "
                          + " AND ( ID_CLIENTE IN (SELECT ID_CLIENTE FROM SGFENS_CLIENTE_VENDEDOR WHERE ID_USUARIO_VENDEDOR=" + idUsuario + " OR ID_USUARIO_VENDEDOR_REASIGNADO=" + idUsuario + " )"
                          + " OR ID_CLIENTE IN (SELECT ID_CLIENTE FROM SGFENS_PEDIDO WHERE ID_USUARIO_VENDEDOR_REASIGNADO=" + idUsuario + ")"
                          +" )"));
                    
                    //Asignamos la lista de campos
                    consultaResponse.setListaCampos(this.getListaCamposAdicionales(idEmpresa, " AND (ID_ESTATUS = 1) "));
                }
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            } else {
                consultaResponse.setError(true);
                consultaResponse.setNumError(901);
                consultaResponse.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        } catch (Exception e) {
            consultaResponse.setError(true);
            consultaResponse.setNumError(902);
            consultaResponse.setMsgError("Error inesperado al consultar catalogos del usuario. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }

        return consultaResponse;
    }
    
    /**
     * Recupera el listado de Empleados de una empresa, implementando opcionalmente
     * un filtro de busqueda avanzado
     * @param idEmpresa ID unico de la empresa
     * @param filtroBusqueda Filtro adicional en formato de sentencia SQL (despues de WHERE)
     * @return Lista de objetos WsItemEmpleado
     */
    private List<WsItemEmpleado> getListaEmpleados(int idEmpresa, String filtroBusqueda) {
         
         List<WsItemEmpleado> listaWsItemEmpleado = new ArrayList<WsItemEmpleado>();
        //Buscamos los clientes definidos para el usuario/empresa
        EmpleadoDaoImpl empleadoDao = new EmpleadoDaoImpl(getConn());
        Empleado[] arrayEmpleadoDto;

        try {
            //arrayEmpleadoDto = empleadoDao.findWhereIdEmpresaEquals(idEmpresa);
            arrayEmpleadoDto = empleadoDao.findByDynamicWhere(" ID_EMPRESA="+idEmpresa + " " + filtroBusqueda, null);
            if (arrayEmpleadoDto.length > 0) {
                //Llenamos la lista de clientes del objeto respuesta
                for (Empleado item : arrayEmpleadoDto) {
                    try {
                        if (item.getIdEstatus()!=2){
                            WsItemEmpleado wsItemEmpleado = new WsItemEmpleado();

                            wsItemEmpleado.setApellidoMaterno(item.getApellidoMaterno());
                            wsItemEmpleado.setApellidoPaterno(item.getApellidoPaterno());
                            wsItemEmpleado.setCorreoElectronico(item.getCorreoElectronico());
                            wsItemEmpleado.setIdDispositivo(item.getIdDispositivo());
                            wsItemEmpleado.setIdEmpleado(item.getIdEmpleado());
                            wsItemEmpleado.setIdEmpresa(item.getIdEmpresa());
                            wsItemEmpleado.setIdEstatus(item.getIdEstatus());
                            wsItemEmpleado.setIdSucursal(item.getIdSucursal());
                            wsItemEmpleado.setLatitud(item.getLatitud());
                            wsItemEmpleado.setLongitud(idEmpresa);
                            wsItemEmpleado.setNombre(item.getNombre());
                            wsItemEmpleado.setNumEmpleado(item.getNumEmpleado());
                            wsItemEmpleado.setTelefonoLocal(item.getTelefonoLocal());
                            //wsItemEmpleado.setUsuario(item.getUsuario());
                            wsItemEmpleado.setPorcentajeComision(item.getPorcentajeComision());

                            listaWsItemEmpleado.add(wsItemEmpleado);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
        }

        return listaWsItemEmpleado;
    }
    
    public ArrayList<WsItemBitacoraCreditos> getBitacoraCreditos(int idEmpresa, int idEmpleado){
        WsListaBitacoraCreditos wsBitacoraCreditos = new WsListaBitacoraCreditos();
        BitacoraCreditosOperacion[] arrayBitacoraDto;
        BitacoraCreditosOperacionBO bitacoraBO = new BitacoraCreditosOperacionBO(getConn());
        try{
            arrayBitacoraDto = bitacoraBO.findBitacoraCreditosOperacion(-1, idEmpresa, 0, 0, " AND ID_USER_REGISTRA=" + idEmpleado);
            if(arrayBitacoraDto.length >0){
                for(BitacoraCreditosOperacion item : arrayBitacoraDto){
                    WsItemBitacoraCreditos wsItemBitacoraCreditos = new WsItemBitacoraCreditos();
                    wsItemBitacoraCreditos.setCantidad(item.getCantidad());
                    wsItemBitacoraCreditos.setComentarios(item.getComentarios());
                    wsItemBitacoraCreditos.setFechaHora(item.getFechaHora());
                    wsItemBitacoraCreditos.setFolioMovil(item.getFolioMovil());
                    wsItemBitacoraCreditos.setIdBitacoraCreditosOperacion(item.getIdBitacoraCreditosOperacion());
                    wsItemBitacoraCreditos.setIdCliente(item.getIdCliente());
                    wsItemBitacoraCreditos.setIdEmpresa(item.getIdEmpresa());
                    wsItemBitacoraCreditos.setIdEstatus(item.getIdEstatus());
                    wsItemBitacoraCreditos.setIdProspecto(item.getIdProspecto());
                    wsItemBitacoraCreditos.setIdUserRegistra(item.getIdUserRegistra());
                    wsItemBitacoraCreditos.setMontoOperacion(item.getMontoOperacion());
                    wsItemBitacoraCreditos.setTipo(item.getTipo());
                    
                    wsBitacoraCreditos.addItem(wsItemBitacoraCreditos);
                }
            }
        }catch(Exception ex){
        }
        return wsBitacoraCreditos.getLista();
    }
    
    public ArrayList<WsItemCategoriaProveedor> getListaCategoriasProv(int idEmpresa){
        WsListaCategoriaProv wsListaCategoriaProv = new WsListaCategoriaProv();
        SgfensProveedorCategoria[] arrayCategoriaProv;
        SGProveedorCategoriaBO categoriaPRovBO = new SGProveedorCategoriaBO(getConn());
        try{
            arrayCategoriaProv = categoriaPRovBO.findSgfensProveedorCategorias(-1, idEmpresa, 0, 0, " AND ID_ESTATUS = 1");
            if(arrayCategoriaProv.length > 0){
                for(SgfensProveedorCategoria item : arrayCategoriaProv){
                    WsItemCategoriaProveedor wsItemCategoriaProveedor = new WsItemCategoriaProveedor();
                    wsItemCategoriaProveedor.setIdCategoria(item.getIdCategoria());
                    wsItemCategoriaProveedor.setIdEstatus(item.getIdEstatus());
                    wsItemCategoriaProveedor.setNombre(item.getNombre());
                    wsItemCategoriaProveedor.setDescripcion(item.getDescripcion());
                    wsListaCategoriaProv.addItem(wsItemCategoriaProveedor);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return wsListaCategoriaProv.getLista();
    }
    
    public ArrayList<WsItemClienteCategoria> getListaCategorias(int idEmpresa){
        WsListaCategorias wsListaCategorias = new WsListaCategorias();
        ClienteCategoria[] arrayCategoriaDto;
        ClienteCategoriaBO categoriaBO = new ClienteCategoriaBO(getConn());
        try{
            arrayCategoriaDto = categoriaBO.findClienteCategorias(-1, idEmpresa, 0, 0, " AND ID_ESTATUS = 1");
            
            if(arrayCategoriaDto.length > 0){
                for(ClienteCategoria item : arrayCategoriaDto){
                    WsItemClienteCategoria wsItemClienteCategoria = new WsItemClienteCategoria();
                    wsItemClienteCategoria.setIdCategoria(item.getIdCategoria());
                    wsItemClienteCategoria.setNombreClasificacion(item.getNombreClasificacion());
                    wsItemClienteCategoria.setDescripcion(item.getDescripcion());
                    wsItemClienteCategoria.setIdEstatus(item.getIdEstatus());
                    
                    wsListaCategorias.addItem(wsItemClienteCategoria);
                }
            }
        }catch(Exception e){
            
        }
        return wsListaCategorias.getLista();
    }
    
    /**
     * Consulta un listado de Conceptos por empresa
     * @param idEmpresa ID de la empresa que se va a consultar, en caso de ser sucursal se recuperan tambien lo correspondiente a la empresa padre
     * @return Lista de objetos WsItemConcepto
     */
    public ArrayList<WsItemConcepto> getListaConceptos(int idEmpresa, int idEmpleado , int limMin , int limMax) {

        WsListaConceptos wsListaConceptos = new WsListaConceptos();
        Encrypter encriptacion = new Encrypter();        
        ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(this.conn);
        double stockGral = 0;

        //Buscamos todos los Conceptos de la Empresa
        //ConceptoDaoImpl conceptoDao = new ConceptoDaoImpl(getConn());
        Concepto[] arrayConceptoDto;
        List<Concepto> arrayConceptoDto2 = new ArrayList<Concepto>();
        ConceptoBO conceptoBO = new ConceptoBO(getConn());

        try {
            //arrayConceptoDto = conceptoDao.findByDynamicWhere("ID_EMPRESA = "+idEmpresa+" AND ID_ESTATUS = 1", new Object[0]);
            arrayConceptoDto = conceptoBO.findConceptos(-1, idEmpresa, limMin, limMax, " AND ID_ESTATUS = 1 AND MATERIA_PRIMA=0 ");
            
            /*if (arrayConceptoDto.length > 300) {
                String qry =" AND ID_ESTATUS = 1 AND ID_CONCEPTO IN ( SELECT ID_CONCEPTO FROM EMPLEADO_INVENTARIO_REPARTIDOR WHERE ID_ESTATUS = 1 AND ID_EMPLEADO = " + idEmpleado +" ) ";
                arrayConceptoDto = conceptoBO.findConceptos(-1, idEmpresa, 0, 0, qry );
            }*/
            
            /////********************************** RECUPERAMOS NUEVAMENTE EL CONCEPTO SI ES QUE EXISTE EN EL INVENTARIO DEL EMPLEADO PARA MOSRARLO MAS DE UNA VES SI ES A GRANEL
            boolean insertaArray = false;
            for (Concepto itemConcepto : arrayConceptoDto) {
                insertaArray = true;
                //Buscamos inventario específico de empleado
                    double cantidadInventarioEmpleado = 0;
                    double pesoStock = 0;
                    EmpleadoInventarioRepartidorBO empleadoInventarioRepartidorBO = new EmpleadoInventarioRepartidorBO(getConn());
                    EmpleadoInventarioRepartidor[] empleadoInventarios =  empleadoInventarioRepartidorBO.findEmpleadoInventarioRepartidors(-1, idEmpleado, 0, 0, " AND ID_CONCEPTO = "+itemConcepto.getIdConcepto());
                    for(EmpleadoInventarioRepartidor ei: empleadoInventarios){
                        //Nos ayudamos de estas variables para que controlen el inventario del repartidor a granel
                        Concepto concep = new ConceptoDaoImpl(getConn()).findByPrimaryKey(ei.getIdConcepto());
                        concep.setStockMinimo(ei.getCantidad());
                        concep.setPeso(ei.getPeso()); //peso unitario (por pieza)
                        concep.setIdSubcategoria4(ei.getIdInventario());
                        concep.setVolumen(ei.getExistenciaGranel()); //peso total restante (al inicio: piezas * precio unitario)
                        arrayConceptoDto2.add(concep);
                        insertaArray = false;
                    }
                
                if(insertaArray){
                    itemConcepto.setStockMinimo(0);
                    arrayConceptoDto2.add(itemConcepto);
                }
            }
            /////**********************************
            
            /////**********************************if (arrayConceptoDto.length > 0) {
            if (arrayConceptoDto2.size() > 0) {
                                
                //Llenamos la lista de conceptos del objeto respuesta
                for (Concepto itemConcepto : arrayConceptoDto2) {
                    WsItemConcepto wsItemConcepto = new WsItemConcepto();
                    String nombreConcepto = "";
                    stockGral = 0;
                    
                    //Buscamos inventario específico de empleado
                    double cantidadInventarioEmpleado = 0;
                    double pesoStock = 0;
                    /////**********************************EmpleadoInventarioRepartidorBO empleadoInventarioRepartidorBO = new EmpleadoInventarioRepartidorBO(getConn());
                    /////**********************************EmpleadoInventarioRepartidor[] empleadoInventarios 
                    /////**********************************        =  empleadoInventarioRepartidorBO.findEmpleadoInventarioRepartidors(-1, idEmpleado, 0, 0, " AND ID_CONCEPTO = "+itemConcepto.getIdConcepto());
                    /////**********************************for(EmpleadoInventarioRepartidor ei: empleadoInventarios){
                        cantidadInventarioEmpleado = itemConcepto.getStockMinimo();
                        //Si tiene asignado un Peso y un valor en IdSubCategoria4, indica que fue un producto a granel
                        if (itemConcepto.getPeso()>0 && itemConcepto.getIdSubcategoria4()>0)
                            pesoStock  = itemConcepto.getVolumen();//getPeso();
                    /////**********************************}

                    wsItemConcepto.setIdConcepto(itemConcepto.getIdConcepto());
                    try {
                        nombreConcepto = encriptacion.decodeString(itemConcepto.getNombre());
                    } catch (Exception e) {
                        System.out.println("############ Error en el sistema al intentar desencriptar el nombre de un concepto." + e.getMessage());
                    }
                    
                    //Buscamos la marca a la que pertenece
                    String nombreMarca = "";
                    try{
                        if (itemConcepto.getIdMarca()>0){
                            MarcaBO marcaBO = new MarcaBO(itemConcepto.getIdMarca(), getConn());
                            nombreMarca = marcaBO.getMarca()!=null?StringManage.getValidString(marcaBO.getMarca().getNombre()):"";
                        }
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    
                    try{
                        stockGral = exisAlmBO.getExistenciaGeneralByEmpresaProducto(itemConcepto.getIdEmpresa(), itemConcepto.getIdConcepto());
                    }catch(Exception e){
                        System.out.println("############ Error en el sistema al intentar buscar existencia de un concepto." + e.getMessage());
                    }
                    
                    BigDecimal precioBigD = (new BigDecimal(itemConcepto.getPrecio())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal stockBigD = (new BigDecimal(stockGral)).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal precioCompraBigD = (new BigDecimal(itemConcepto.getPrecioCompra())).setScale(2, RoundingMode.HALF_UP);
                    
                    BigDecimal precioDocenaBigD = (new BigDecimal(itemConcepto.getPrecioDocena())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal precioMedioMayoreoBigD = (new BigDecimal(itemConcepto.getPrecioMedioMayoreo())).setScale(2, RoundingMode.HALF_UP);                    
                    BigDecimal precioMayoreoBigD = (new BigDecimal(itemConcepto.getPrecioMayoreo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal precioEspecialBigD = (new BigDecimal(itemConcepto.getPrecioEspecial())).setScale(2, RoundingMode.HALF_UP);
                    
                    BigDecimal precioUnitarioGranel = (new BigDecimal(itemConcepto.getPrecioUnitarioGranel())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal precioMedioGranel = (new BigDecimal(itemConcepto.getPrecioMedioGranel())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal precioMayoreoGranel = (new BigDecimal(itemConcepto.getPrecioMayoreoGranel())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal precioEspecialGranel = (new BigDecimal(itemConcepto.getPrecioEspecialGranel())).setScale(2, RoundingMode.HALF_UP);
                    
                    BigDecimal maxMenudeoBigD = (new BigDecimal(itemConcepto.getMaxMenudeo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal minMedioMayoreoBigD = (new BigDecimal(itemConcepto.getMinMedioMayoreo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal maxMedioMayoreoBigD = (new BigDecimal(itemConcepto.getMaxMedioMayoreo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal minMayoreoBigD = (new BigDecimal(itemConcepto.getMinMayoreo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal descuentoMontoBigD = (new BigDecimal(itemConcepto.getDescuentoMonto())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal descuentoPorcentajeBigD = (new BigDecimal(itemConcepto.getDescuentoPorcentaje())).setScale(2, RoundingMode.HALF_UP);
                    
                    BigDecimal precioMinimoVentaBigD = (new BigDecimal(itemConcepto.getPrecioMinimoVenta())).setScale(2, RoundingMode.HALF_UP);
                    
                    wsItemConcepto.setNombre(nombreConcepto);
                    wsItemConcepto.setDescripcion(itemConcepto.getDescripcion());
                    wsItemConcepto.setPrecio(precioBigD.doubleValue());
                    wsItemConcepto.setIdentificacion(itemConcepto.getIdentificacion());
                    wsItemConcepto.setIdCategoria(itemConcepto.getIdCategoria());
                    wsItemConcepto.setIdAlmacen(itemConcepto.getIdAlmacen());
                    //Stock de empleado repartidor (empleado movil-camioneta)
                    wsItemConcepto.setStock(cantidadInventarioEmpleado);
                    //Stock de almacen general
                    wsItemConcepto.setStockAlmacen(stockBigD.doubleValue());
                    
                    wsItemConcepto.setMarca(nombreMarca);
                    wsItemConcepto.setPrecioCompra(precioCompraBigD.doubleValue());
                    
                    wsItemConcepto.setPrecioDocena(precioDocenaBigD.doubleValue());
                    wsItemConcepto.setPrecioMedioMayoreo(precioMedioMayoreoBigD.doubleValue());
                    wsItemConcepto.setPrecioMayoreo(precioMayoreoBigD.doubleValue());
                    wsItemConcepto.setPrecioEspecial(precioEspecialBigD.doubleValue());
                    wsItemConcepto.setMaxMenudeo(maxMenudeoBigD.doubleValue());
                    wsItemConcepto.setMinMedioMayoreo(minMedioMayoreoBigD.doubleValue());
                    wsItemConcepto.setMaxMedioMayoreo(maxMedioMayoreoBigD.doubleValue());
                    wsItemConcepto.setMinMayoreo(minMayoreoBigD.doubleValue());
                    
                    wsItemConcepto.setDescuentoMonto(descuentoMontoBigD.doubleValue());
                    wsItemConcepto.setDescuentoPorcentaje(descuentoPorcentajeBigD.doubleValue());
                    
                    wsItemConcepto.setPrecioMinimoVenta(precioMinimoVentaBigD.doubleValue());
                    
                    if(itemConcepto.getCaracteristiscas() != null){
                        wsItemConcepto.setCaracteristicas(itemConcepto.getCaracteristiscas());
                    }
                    
                    BigDecimal montoComision = (new BigDecimal(itemConcepto.getComisionMonto())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal porcentajeComision = (new BigDecimal(itemConcepto.getComisionPorcentaje())).setScale(2, RoundingMode.HALF_UP);                            
                    wsItemConcepto.setMontoComision(montoComision.doubleValue());
                    wsItemConcepto.setPorcentajeComision(porcentajeComision.doubleValue());
                    
                    BigDecimal deglosePiezas = (new BigDecimal(itemConcepto.getDesglosePiezas())).setScale(2, RoundingMode.HALF_UP);
                    
                    wsItemConcepto.setClave(itemConcepto.getClave());
                    wsItemConcepto.setDesglose_piezas(deglosePiezas.doubleValue());
                    
                    wsItemConcepto.setPrecioUnitarioGranel(precioUnitarioGranel.doubleValue());
                    wsItemConcepto.setPrecioMedioGranel(precioMedioGranel.doubleValue());
                    wsItemConcepto.setPrecioMayoreoGranel(precioMayoreoGranel.doubleValue());
                    wsItemConcepto.setPrecioEspecialGranel(precioEspecialGranel.doubleValue());
                    wsItemConcepto.setPesoProducto(itemConcepto.getPeso());
                    wsItemConcepto.setPesoStock(pesoStock);
                    wsItemConcepto.setIdInventarioEmpleado(itemConcepto.getIdSubcategoria4());                    
                    
                    wsListaConceptos.addItem(wsItemConcepto);
                    
                    
                }
            }
        } catch (Exception e) {
        }
        return wsListaConceptos.getLista();
    }
    
    /**
     * Consulta un listado de Conceptos/servicios por empresa de los proveedores
     * @param idEmpresa ID de la empresa que se va a consultar, en caso de ser sucursal se recuperan tambien lo correspondiente a la empresa padre
     * @return Lista de objetos WsItemConceptoServicio
     */
    public ArrayList<WsItemConceptoServicio> getListaConceptosServiciosProveedores(int idEmpresa, int idEmpleado , int limMin , int limMax , int enviarImagen) {

        WsListaConceptosServiciosProveedores wsListaConceptos = new WsListaConceptosServiciosProveedores();
               
        //Buscamos todos los Conceptos de la Empresa
        //ConceptoDaoImpl conceptoDao = new ConceptoDaoImpl(getConn());
        SgfensProductoServicio[] arrayConceptoDto;
        List<SgfensProductoServicio> arrayConceptoDto2 = new ArrayList<SgfensProductoServicio>();
        SGProductoServicioBO conceptoBO = new SGProductoServicioBO(getConn());
        Configuration appConfig = new Configuration();
        Empresa empresaDto = new EmpresaBO(idEmpresa,getConn()).getEmpresa();
        
        try {            
            arrayConceptoDto = conceptoBO.findSgfensProductoServicios(-1, idEmpresa, limMin, limMax, " AND ID_ESTATUS = 1");
            
            /////********************************** RECUPERAMOS NUEVAMENTE EL CONCEPTO SI ES QUE EXISTE EN EL INVENTARIO DEL EMPLEADO PARA MOSRARLO MAS DE UNA VES SI ES A GRANEL
            boolean insertaArray = false;
            for (SgfensProductoServicio itemConcepto : arrayConceptoDto) {               
                    arrayConceptoDto2.add(itemConcepto);               
            }
            /////**********************************
            
            /////**********************************if (arrayConceptoDto.length > 0) {
            if (arrayConceptoDto2.size() > 0) {
                                
                //Llenamos la lista de conceptos del objeto respuesta
                for (SgfensProductoServicio itemConcepto : arrayConceptoDto2) {
                    WsItemConceptoServicio wsItemConcepto = new WsItemConceptoServicio();
                                        
                    wsItemConcepto.setIdProductoServicio(itemConcepto.getIdProductoServicio());
                    wsItemConcepto.setNombre(itemConcepto.getNombre());
                    wsItemConcepto.setDescripcion(itemConcepto.getDescripcion());
                    wsItemConcepto.setUnidad(itemConcepto.getUnidad());
                                    
                    BigDecimal precioMedioMayoreoBigD = (new BigDecimal(itemConcepto.getPrecioMedioMayoreo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal minMedioMayoreoBigD = (new BigDecimal(itemConcepto.getMinMedioMayoreo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal maxMedioMayoreoBigD = (new BigDecimal(itemConcepto.getMaxMedioMayoreo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal precioMenudeoBigD = (new BigDecimal(itemConcepto.getPrecioMenudeo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal maxMenudeoBigD = (new BigDecimal(itemConcepto.getMaxMenudeo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal precioMayoreoBigD = (new BigDecimal(itemConcepto.getPrecioMayoreo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal minMayoreoBigD = (new BigDecimal(itemConcepto.getMinMayoreo())).setScale(2, RoundingMode.HALF_UP);
                    
                    
                    wsItemConcepto.setPrecioMedioMayoreo(precioMedioMayoreoBigD.doubleValue());
                    wsItemConcepto.setMinMedioMayoreo(minMedioMayoreoBigD.doubleValue());
                    wsItemConcepto.setMaxMedioMayoreo(maxMedioMayoreoBigD.doubleValue());
                    wsItemConcepto.setPrecioMenudeo(precioMenudeoBigD.doubleValue());
                    wsItemConcepto.setMaxMenudeo(maxMenudeoBigD.doubleValue());
                    wsItemConcepto.setPrecioMayoreo(precioMayoreoBigD.doubleValue());
                    wsItemConcepto.setMinMayoreo(minMayoreoBigD.doubleValue());
                    wsItemConcepto.setIdCategoria(itemConcepto.getIdCategoria());
                    
                    
                    if(enviarImagen == 1){
                        if(itemConcepto.getNombreImagenProductoServicio() != null){
                            if(!itemConcepto.getNombreImagenProductoServicio().trim().equals("")){
                                try{
                                    wsItemConcepto.setImagen(FileManage.getBytesFromFile(new File(appConfig.getApp_content_path() + empresaDto.getRfc() +"/ImagenConcepto/" + itemConcepto.getNombreImagenProductoServicio())));
                                }catch(Exception ex){}
                            }
                        }
                    }
                    
                    wsListaConceptos.addItem(wsItemConcepto);
                    
                    
                }
            }
        } catch (Exception e) {
        }
        return wsListaConceptos.getLista();
    }
    
    /**
     * Consulta un listado de Conceptos/servicios por empresa de los proveedores
     * @param idEmpresa ID de la empresa que se va a consultar, en caso de ser sucursal se recuperan tambien lo correspondiente a la empresa padre
     * @return Lista de objetos WsItemConceptoServicio
     */
    public ArrayList<WsItemProveedorConceptoServicio> getListaConceptosServiciosProveedoresByProveedor(int idEmpresa, int idEmpleado , int limMin , int limMax , int enviarImagen) {

        WsListaProveedorConceptosServiciosProveedores wsListaConceptos = new WsListaProveedorConceptosServiciosProveedores();
               
        //Buscamos todos los Conceptos de la Empresa
        //ConceptoDaoImpl conceptoDao = new ConceptoDaoImpl(getConn());
        SgfensProveedorProducto[] arrayConceptoDto;
        List<SgfensProveedorProducto> arrayConceptoDto2 = new ArrayList<SgfensProveedorProducto>();
        SGProveedorProductoBO conceptoBO = new SGProveedorProductoBO(getConn());
        Configuration appConfig = new Configuration();
        Empresa empresaDto = new EmpresaBO(idEmpresa,getConn()).getEmpresa();
        
        try {            
            arrayConceptoDto = conceptoBO.findSgfensProveedorProductos(-1, idEmpresa, limMin, limMax, " ");
            
            /////********************************** RECUPERAMOS NUEVAMENTE EL CONCEPTO SI ES QUE EXISTE EN EL INVENTARIO DEL EMPLEADO PARA MOSRARLO MAS DE UNA VES SI ES A GRANEL
            boolean insertaArray = false;
            for (SgfensProveedorProducto itemConcepto : arrayConceptoDto) {               
                    arrayConceptoDto2.add(itemConcepto);               
            }
            /////**********************************
            
            /////**********************************if (arrayConceptoDto.length > 0) {
            if (arrayConceptoDto2.size() > 0) {
                                
                //Llenamos la lista de conceptos del objeto respuesta
                for (SgfensProveedorProducto itemConcepto : arrayConceptoDto2) {
                    WsItemProveedorConceptoServicio wsItemConcepto = new WsItemProveedorConceptoServicio();
                                        
                    wsItemConcepto.setIdProveedorProducto(itemConcepto.getIdProveedorProducto());
                    wsItemConcepto.setIdProductoServicio(itemConcepto.getIdSgfensProductoServicio());
                    wsItemConcepto.setIdProveedor(itemConcepto.getIdSgfensProductoProveedor());
                    wsItemConcepto.setVolumenDisponible(itemConcepto.getVolumenDisponible());
                    wsItemConcepto.setCaducidad(itemConcepto.getCaducidad());
                    wsItemConcepto.setFechaDisponibilidad(itemConcepto.getFechaDisponibilidad());
                    wsItemConcepto.setUnidad(itemConcepto.getUnidad());
                                    
                    BigDecimal precioMedioMayoreoBigD = (new BigDecimal(itemConcepto.getPrecioMedioMayoreo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal minMedioMayoreoBigD = (new BigDecimal(itemConcepto.getMinMedioMayoreo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal maxMedioMayoreoBigD = (new BigDecimal(itemConcepto.getMaxMedioMayoreo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal precioMenudeoBigD = (new BigDecimal(itemConcepto.getPrecioMenudeo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal maxMenudeoBigD = (new BigDecimal(itemConcepto.getMaxMenudeo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal precioMayoreoBigD = (new BigDecimal(itemConcepto.getPrecioMayoreo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal minMayoreoBigD = (new BigDecimal(itemConcepto.getMinMayoreo())).setScale(2, RoundingMode.HALF_UP);
                    
                    
                    wsItemConcepto.setPrecioMedioMayoreo(precioMedioMayoreoBigD.doubleValue());
                    wsItemConcepto.setMinMedioMayoreo(minMedioMayoreoBigD.doubleValue());
                    wsItemConcepto.setMaxMedioMayoreo(maxMedioMayoreoBigD.doubleValue());
                    wsItemConcepto.setPrecioMenudeo(precioMenudeoBigD.doubleValue());
                    wsItemConcepto.setMaxMenudeo(maxMenudeoBigD.doubleValue());
                    wsItemConcepto.setPrecioMayoreo(precioMayoreoBigD.doubleValue());
                    wsItemConcepto.setMinMayoreo(minMayoreoBigD.doubleValue());
                    wsItemConcepto.setIdCategoria(itemConcepto.getIdCategoria());
                    
                    
                    if(enviarImagen == 1){
                        if(itemConcepto.getNombreImagenProductoServicio() != null){
                            if(!itemConcepto.getNombreImagenProductoServicio().trim().equals("")){
                                try{
                                    wsItemConcepto.setImagen(FileManage.getBytesFromFile(new File(appConfig.getApp_content_path() + empresaDto.getRfc() +"/ImagenConcepto/" + itemConcepto.getNombreImagenProductoServicio())));
                                }catch(Exception ex){}
                            }
                        }
                    }
                    
                    wsListaConceptos.addItem(wsItemConcepto);
                    
                    
                }
            }
        } catch (Exception e) {
        }
        return wsListaConceptos.getLista();
    }
    
    /**
     * Consulta un listado de Conceptos por empresa
     * @param idEmpresa ID de la empresa que se va a consultar, en caso de ser sucursal se recuperan tambien lo correspondiente a la empresa padre
     * @return Lista de objetos WsItemConcepto
     */
    public ArrayList<WsItemConcepto> getListaConceptosWiFi(int idEmpresa, int idEmpleado , int limMin , int limMax) {

        WsListaConceptos wsListaConceptos = new WsListaConceptos();
        Encrypter encriptacion = new Encrypter();
        ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(this.conn);
        double stockGral = 0;

        //Buscamos todos los Conceptos de la Empresa
        ConceptoDaoImpl conceptoDao = new ConceptoDaoImpl(getConn());
        Concepto[] arrayConceptoDto;
        ConceptoBO conceptoBO = new ConceptoBO(getConn());

        try {
            //arrayConceptoDto = conceptoDao.findByDynamicWhere("ID_EMPRESA = "+idEmpresa+" AND ID_ESTATUS = 1", new Object[0]);
            arrayConceptoDto = conceptoBO.findConceptos(-1, idEmpresa, limMin, limMax, " AND ID_ESTATUS = 1 AND MATERIA_PRIMA=0 ");
            
            /*if (arrayConceptoDto.length > 300) {
                String qry =" AND ID_ESTATUS = 1 AND ID_CONCEPTO IN ( SELECT ID_CONCEPTO FROM EMPLEADO_INVENTARIO_REPARTIDOR WHERE ID_ESTATUS = 1 AND ID_EMPLEADO = " + idEmpleado +" ) ";
                arrayConceptoDto = conceptoBO.findConceptos(-1, idEmpresa, 0, 0, qry );
            }*/
            
            if (arrayConceptoDto.length > 0) {
                //Llenamos la lista de conceptos del objeto respuesta
                for (Concepto itemConcepto : arrayConceptoDto) {
                    WsItemConcepto wsItemConcepto = new WsItemConcepto();
                    String nombreConcepto = "";
                    stockGral = 0;
                    
                    //Buscamos inventario específico de empleado
                    double cantidadInventarioEmpleado = 0;
                    EmpleadoInventarioRepartidorBO empleadoInventarioRepartidorBO = new EmpleadoInventarioRepartidorBO(getConn());
                    EmpleadoInventarioRepartidor[] empleadoInventarios 
                            =  empleadoInventarioRepartidorBO.findEmpleadoInventarioRepartidors(-1, idEmpleado, 0, 0, " AND ID_CONCEPTO = "+itemConcepto.getIdConcepto());
                    for(EmpleadoInventarioRepartidor ei: empleadoInventarios){
                        cantidadInventarioEmpleado += ei.getCantidad();
                    }

                    wsItemConcepto.setIdConcepto(itemConcepto.getIdConcepto());
                    try {
                        nombreConcepto = encriptacion.decodeString(itemConcepto.getNombre());
                    } catch (Exception e) {
                        System.out.println("############ Error en el sistema al intentar desencriptar el nombre de un concepto." + e.getMessage());
                    }
                    
                    //Buscamos la marca a la que pertenece
                    String nombreMarca = "";
                    try{
                        if (itemConcepto.getIdMarca()>0){
                            MarcaBO marcaBO = new MarcaBO(itemConcepto.getIdMarca(), getConn());
                            nombreMarca = marcaBO.getMarca()!=null?StringManage.getValidString(marcaBO.getMarca().getNombre()):"";
                        }
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    
                    try{
                        stockGral = exisAlmBO.getExistenciaGeneralByEmpresaProducto(itemConcepto.getIdEmpresa(), itemConcepto.getIdConcepto());
                    }catch(Exception e){
                        System.out.println("############ Error en el sistema al intentar buscar existencia de un concepto." + e.getMessage());
                    }
                    
                    BigDecimal precioBigD = (new BigDecimal(itemConcepto.getPrecio())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal stockBigD = (new BigDecimal(stockGral)).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal precioCompraBigD = (new BigDecimal(itemConcepto.getPrecioCompra())).setScale(2, RoundingMode.HALF_UP);
                    
                    BigDecimal precioDocenaBigD = (new BigDecimal(itemConcepto.getPrecioDocena())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal precioMedioMayoreoBigD = (new BigDecimal(itemConcepto.getPrecioMedioMayoreo())).setScale(2, RoundingMode.HALF_UP);                    
                    BigDecimal precioMayoreoBigD = (new BigDecimal(itemConcepto.getPrecioMayoreo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal precioEspecialBigD = (new BigDecimal(itemConcepto.getPrecioEspecial())).setScale(2, RoundingMode.HALF_UP);
                    
                    BigDecimal precioUnitarioGranel = (new BigDecimal(itemConcepto.getPrecioUnitarioGranel())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal precioMedioGranel = (new BigDecimal(itemConcepto.getPrecioMedioGranel())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal precioMayoreoGranel = (new BigDecimal(itemConcepto.getPrecioMayoreoGranel())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal precioEspecialGranel = (new BigDecimal(itemConcepto.getPrecioEspecialGranel())).setScale(2, RoundingMode.HALF_UP);
                    
                    BigDecimal maxMenudeoBigD = (new BigDecimal(itemConcepto.getMaxMenudeo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal minMedioMayoreoBigD = (new BigDecimal(itemConcepto.getMinMedioMayoreo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal maxMedioMayoreoBigD = (new BigDecimal(itemConcepto.getMaxMedioMayoreo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal minMayoreoBigD = (new BigDecimal(itemConcepto.getMinMayoreo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal descuentoMontoBigD = (new BigDecimal(itemConcepto.getDescuentoMonto())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal descuentoPorcentajeBigD = (new BigDecimal(itemConcepto.getDescuentoPorcentaje())).setScale(2, RoundingMode.HALF_UP);
                    
                    BigDecimal precioMinimoVentaBigD = (new BigDecimal(itemConcepto.getPrecioMinimoVenta())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal deglosePiezas = (new BigDecimal(itemConcepto.getDesglosePiezas())).setScale(2, RoundingMode.HALF_UP);
                    
                    wsItemConcepto.setNombre(nombreConcepto);
                    wsItemConcepto.setDescripcion(itemConcepto.getDescripcion());
                    wsItemConcepto.setPrecio(precioBigD.doubleValue());
                    wsItemConcepto.setIdentificacion(itemConcepto.getIdentificacion());
                    wsItemConcepto.setIdCategoria(itemConcepto.getIdCategoria());
                    wsItemConcepto.setIdAlmacen(itemConcepto.getIdAlmacen());
                    //Stock de empleado repartidor (empleado movil-camioneta)
                    wsItemConcepto.setStock(cantidadInventarioEmpleado);
                    //Stock de almacen general
                    wsItemConcepto.setStockAlmacen(stockBigD.doubleValue());
                    
                    wsItemConcepto.setMarca(nombreMarca);
                    wsItemConcepto.setPrecioCompra(precioCompraBigD.doubleValue());
                    
                    wsItemConcepto.setPrecioDocena(precioDocenaBigD.doubleValue());
                    wsItemConcepto.setPrecioMedioMayoreo(precioMedioMayoreoBigD.doubleValue());
                    wsItemConcepto.setPrecioMayoreo(precioMayoreoBigD.doubleValue());
                    wsItemConcepto.setPrecioEspecial(precioEspecialBigD.doubleValue());
                    wsItemConcepto.setMaxMenudeo(maxMenudeoBigD.doubleValue());
                    wsItemConcepto.setMinMedioMayoreo(minMedioMayoreoBigD.doubleValue());
                    wsItemConcepto.setMaxMedioMayoreo(maxMedioMayoreoBigD.doubleValue());
                    wsItemConcepto.setMinMayoreo(minMayoreoBigD.doubleValue());
                    
                    wsItemConcepto.setDescuentoMonto(descuentoMontoBigD.doubleValue());
                    wsItemConcepto.setDescuentoPorcentaje(descuentoPorcentajeBigD.doubleValue());
                    
                    wsItemConcepto.setPrecioMinimoVenta(precioMinimoVentaBigD.doubleValue());
                    
                    if(itemConcepto.getCaracteristiscas() != null){
                        wsItemConcepto.setCaracteristicas(itemConcepto.getCaracteristiscas());
                    }
                    
                    if(itemConcepto.getRutaImagen() != null){
                        if(!itemConcepto.getRutaImagen().trim().equals("")){
                            try{
                                wsItemConcepto.setImagen(FileManage.getBytesFromFile(new File(itemConcepto.getRutaImagen())));
                            }catch(Exception ex){}
                        }
                    }
                    
                    if(itemConcepto.getRutaVideo() != null){
                        if(!itemConcepto.getRutaVideo().trim().equals("")){
                            try{
                                wsItemConcepto.setVideo(FileManage.getBytesFromFile(new File(itemConcepto.getRutaVideo())));
                            }catch(Exception ex){}
                        }
                    }
                    
                    BigDecimal montoComision = (new BigDecimal(itemConcepto.getComisionMonto())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal porcentajeComision = (new BigDecimal(itemConcepto.getComisionPorcentaje())).setScale(2, RoundingMode.HALF_UP);                            
                    wsItemConcepto.setMontoComision(montoComision.doubleValue());
                    wsItemConcepto.setPorcentajeComision(porcentajeComision.doubleValue());
                    
                    wsItemConcepto.setClave(itemConcepto.getClave());
                    wsItemConcepto.setDesglose_piezas(deglosePiezas.doubleValue());
                    
                    wsItemConcepto.setPrecioUnitarioGranel(precioUnitarioGranel.doubleValue());
                    wsItemConcepto.setPrecioMedioGranel(precioMedioGranel.doubleValue());
                    wsItemConcepto.setPrecioMayoreoGranel(precioMayoreoGranel.doubleValue());
                    wsItemConcepto.setPrecioEspecialGranel(precioEspecialGranel.doubleValue());
                    
                    wsListaConceptos.addItem(wsItemConcepto);
                    
                    
                }
            }
        } catch (Exception e) {
        }
        return wsListaConceptos.getLista();
    }
    
    /**
     * Consulta los valores de los campos adicionales de la empresa
     * @param idEmpresa
     * @reurn
     */
    private ArrayList<WsItemClienteCampoContenido> getListaValoresCamposAdicionales(int idEmpresa, String filtroBusqueda){
        WsListaClienteCampoContenido listaContenido = new WsListaClienteCampoContenido();
        ClienteCampoContenidoDaoImpl contenidoDao = new ClienteCampoContenidoDaoImpl(getConn());
        ClienteCampoContenido[] arrayContenidoDto;
        try{
            
        }catch(Exception ex){}
        return listaContenido.getLista();
    }
    
    /**
     * Consulta los Campos adicionales de la empresa
     * @param idEmpresa
     * @reurn
     */
    private ArrayList<WsItemClienteCampoAdicional> getListaCamposAdicionales(int idEmpresa, String filtroBusqueda){
        WsListaClientesCamposAdicionales listaCampos = new WsListaClientesCamposAdicionales();
        ClienteCampoAdicionalDaoImpl campoDao = new ClienteCampoAdicionalDaoImpl(getConn());
        ClienteCampoAdicionalBO campoBO = new ClienteCampoAdicionalBO(getConn());
        ClienteCampoAdicional[] arrayCampoDto;
        try{
            arrayCampoDto = campoBO.findClienteCampoAdicionals(-1, idEmpresa, 0, 0, filtroBusqueda);
            if(arrayCampoDto.length > 0){
                for(ClienteCampoAdicional item : arrayCampoDto){
                    try{
                        if(item.getIdEstatus() != 2){
                            WsItemClienteCampoAdicional wsItemClienteCampoAdicional = new WsItemClienteCampoAdicional();
                            
                            wsItemClienteCampoAdicional.setIdClienteCampo(item.getIdClienteCampo());
                            wsItemClienteCampoAdicional.setIdEmpresa(item.getIdEmpresa());
                            wsItemClienteCampoAdicional.setIdEstatus(item.getIdEstatus());
                            wsItemClienteCampoAdicional.setLabelNombre(item.getLabelNombre());
                            wsItemClienteCampoAdicional.setLabelTipo(item.getTipoLabel());
                            listaCampos.addItem(wsItemClienteCampoAdicional);
                        }
                    }catch(Exception ex){}
                }
            }
        }catch(Exception ex){}
        return listaCampos.getLista();
    }
    
    private ArrayList<WsItemProveedor> getListaProveedores(int idEmpresa, String filtroBusqueda){
        WsListaProveedores listaProveedores = new WsListaProveedores();
        ProveedoresDaoImpl proveedorDao = new ProveedoresDaoImpl(getConn());
        SGProveedorBO proveedorBO = new SGProveedorBO(getConn());
        SgfensProveedor[] arrayProveedorDto;
        try{
            arrayProveedorDto = proveedorBO.findProveedor(-1, idEmpresa, 0, 0, filtroBusqueda);
            if(arrayProveedorDto.length > 0){
                for(SgfensProveedor item : arrayProveedorDto){
                    try{
                        if(item.getIdEstatus() != 2){
                            WsItemProveedor wsItemProveedor = new WsItemProveedor();
                            wsItemProveedor.setIdProveedor(item.getIdProveedor());
                            wsItemProveedor.setNumeroProveedor(item.getNumeroProveedor());
                            wsItemProveedor.setRfcProveedor(item.getRfc());
                            wsItemProveedor.setRazonSocial(item.getRazonSocial());
                            wsItemProveedor.setCalle(item.getCalle());
                            wsItemProveedor.setNumero(item.getNumero());
                            wsItemProveedor.setNumeroInterior(item.getNumeroInterior());
                            wsItemProveedor.setColonia(item.getColonia());
                            wsItemProveedor.setCodigoPostal(item.getCodigoPostal());
                            wsItemProveedor.setPais(item.getPais());
                            wsItemProveedor.setEstado(item.getEstado());
                            wsItemProveedor.setMunicipio(item.getMunicipio());
                            wsItemProveedor.setLada(item.getLada());
                            wsItemProveedor.setTelefono(item.getTelefono());
                            wsItemProveedor.setExtension(item.getExtension());
                            wsItemProveedor.setCelular(item.getCelular());
                            wsItemProveedor.setCorreo(item.getCorreo());
                            wsItemProveedor.setContacto(item.getContacto());
                            wsItemProveedor.setIdEstatus(item.getIdEstatus());
                            wsItemProveedor.setDescripcion(item.getDescripcion());
                            wsItemProveedor.setNombreEmpresa(item.getNombreEmpresa());
                            wsItemProveedor.setLatitud(item.getLatitud());
                            wsItemProveedor.setLongitud(item.getLongitud());
                            wsItemProveedor.setIdCategoriaProveedor(item.getIdCategoriaProveedor());
                            listaProveedores.addItem(wsItemProveedor);
                        }
                    }catch(Exception ex){
                        
                    }
                }
            }
        }catch(Exception ex){
            
        }
        return listaProveedores.getLista();
    }
    
    /**
     * Consulta los Clientes de una empresa.
     * @param idEmpresa
     * @return 
     */
    private ArrayList<WsItemCliente> getListaClientes(int idEmpresa, String filtroBusqueda){//int idEmpresa) {
        WsListaClientes listaClientes = new WsListaClientes();
        //Buscamos los clientes definidos para el usuario/empresa
        ClienteDaoImpl clienteDao = new ClienteDaoImpl(getConn());
        ClienteBO clienteBO = new ClienteBO(getConn());
        Cliente[] arrayClienteDto;
        ClienteCampoContenidoBO campoContenidoBO = new ClienteCampoContenidoBO(getConn());
        ClientePrecioConceptoBO clientePrecioBO = new ClientePrecioConceptoBO(getConn());
        ClienteCategoriaBO clienteCategoriaBO = new ClienteCategoriaBO(getConn());
        ClientePrecioConcepto[] preciosDto;
        ClienteCampoContenido[] campoContenidoDto;
        WsListaPreciosCliente listaPreciosCliente = null;
        WsListaClienteCampoContenido listaCampoContenido = null;
        
        try {
            //arrayClienteDto = clienteDao.findWhereIdEmpresaEquals(idEmpresa);
            arrayClienteDto = clienteBO.findClientes(-1, idEmpresa, 0, 0, filtroBusqueda);
            if (arrayClienteDto.length > 0) {
                //Llenamos la lista de clientes del objeto respuesta
                for (Cliente item : arrayClienteDto) {
                    try {
                        if (item.getIdEstatus()!=2){
                            WsItemCliente wsItemCliente = new WsItemCliente();

                            wsItemCliente.setIdCliente(item.getIdCliente());
                            wsItemCliente.setRfcCliente(item.getRfcCliente());
                            wsItemCliente.setNombreCliente(item.getNombreCliente());
                            wsItemCliente.setApellidoPaternoCliente(item.getApellidoPaternoCliente());
                            wsItemCliente.setApellidoMaternoCliente(item.getApellidoMaternoCliente());
                            wsItemCliente.setRazonSocial(item.getRazonSocial());
                            wsItemCliente.setCalle(item.getCalle());
                            wsItemCliente.setNumero(item.getNumero());
                            wsItemCliente.setNumeroInterior(item.getNumeroInterior());
                            wsItemCliente.setColonia(item.getColonia());
                            wsItemCliente.setCodigoPostal(item.getCodigoPostal());
                            wsItemCliente.setPais(item.getPais());
                            wsItemCliente.setEstado(item.getEstado());
                            wsItemCliente.setMunicipio(item.getMunicipio());
                            wsItemCliente.setLada(item.getLada());
                            wsItemCliente.setTelefono(item.getTelefono());
                            wsItemCliente.setExtension(item.getExtension());
                            wsItemCliente.setCelular(item.getCelular());
                            wsItemCliente.setCorreo(item.getCorreo());
                            wsItemCliente.setContacto(item.getContacto());
                            wsItemCliente.setLatitud(item.getLatitud());
                            wsItemCliente.setLongitud(item.getLongitud());
                            if (StringManage.getValidString(item.getFolioClienteMovil()).length()>0){
                                wsItemCliente.setFolioClienteMovil(item.getFolioClienteMovil());
                            }else{
                                //Si no tiene un folio Movil, indica que fue creado en consola
                                // entonces asignamos uno, para que funcione adecuadamente el update desde movil
                                String folioMovilConsolaAsignado = ClienteBO.generaFolioMovil(idEmpresa);
                                try{
                                    //Actualizamos en BD servidor
                                    item.setFolioClienteMovil(folioMovilConsolaAsignado);
                                    clienteDao.update(item.createPk(), item);
                                    //Asignamos a objeto de respuesta
                                    wsItemCliente.setFolioClienteMovil(folioMovilConsolaAsignado);
                                }catch(Exception ex){
                                    ex.printStackTrace();
                                }
                            }
                            wsItemCliente.setDiasVisita(item.getDiasVisita());
                            wsItemCliente.setSaldoCliente(item.getSaldoCliente());
                            
                            //Consultamos la fecha limite de la reasignacion
                            SGClienteVendedorBO clienteVendedorBO = new SGClienteVendedorBO(item.getIdCliente(), getConn());
                            if(clienteVendedorBO.getClienteVendedor().getIdUsuarioVendedorReasignado() > 0){
                                wsItemCliente.setFechaLimiteReasignacion(clienteVendedorBO.getClienteVendedor().getFechaLimiteReasigancion());
                            }
                            
                            wsItemCliente.setPerioricidad(item.getPerioricidad());
                            wsItemCliente.setFechaUltimaVisita(item.getFechaUltimaVisita());
                            
                            wsItemCliente.setNombreComercial(item.getNombreComercial());
                            wsItemCliente.setPermisoVentaCredito(item.getPermisoVentaCredito());
                            
                            wsItemCliente.setIdCategoria(item.getIdClienteCategoria());
                            wsItemCliente.setCategoria("");
                            
                            wsItemCliente.setVentaConsigna(item.getPermisoVentaCredito());
                            wsItemCliente.setComisionConsigna(item.getComisionConsigna());
                            wsItemCliente.setCreditoMontoMax(item.getCreditoMontoMax());
                            wsItemCliente.setCreditoDiasMax(item.getCreditoDiasMax());
                            if(item.getIdClienteCategoria() > 0){
                                try{
                                    wsItemCliente.setCategoria(clienteCategoriaBO.findClienteCategoriaById(item.getIdClienteCategoria()).getNombreClasificacion());
                                }catch(Exception e){
                                    
                                }
                            }
                            
                            
                            /* Precios Especiales */
                            try{              
                                
                                WsItemPrecioCliente wsItemPrecioCliente = null;
                                preciosDto = clientePrecioBO.findClienteConceptos(item.getIdCliente(), -1, idEmpresa, -1, -1, "");
                                listaPreciosCliente = new WsListaPreciosCliente();
                                if(preciosDto.length>0){
                                    for(ClientePrecioConcepto ItemPrecio:preciosDto ){
                                         wsItemPrecioCliente = new WsItemPrecioCliente();
                                         wsItemPrecioCliente.setIdConcepto(ItemPrecio.getIdConcepto());
                                         wsItemPrecioCliente.setPrecioClienteUnitario(ItemPrecio.getPrecioUnitarioCliente());
                                         wsItemPrecioCliente.setPrecioClienteMedio(ItemPrecio.getPrecioMedioCliente());
                                         wsItemPrecioCliente.setPrecioClienteMayoreo(ItemPrecio.getPrecioMayoreoCliente());
                                         wsItemPrecioCliente.setPrecioClienteDocena(ItemPrecio.getPrecioDocenaCliente());
                                         wsItemPrecioCliente.setPrecioClienteEspecial(ItemPrecio.getPrecioEspecialCliente());
                                         wsItemPrecioCliente.setEstatus(ItemPrecio.getIdEstatus());
                                         
                                         wsItemPrecioCliente.setPrecioUnitarioGranelCliente(ItemPrecio.getPrecioUnitarioGranelCliente());
                                         wsItemPrecioCliente.setPrecioMedioGranelCliente(ItemPrecio.getPrecioMedioGranelCliente());
                                         wsItemPrecioCliente.setPrecioMayoreoGranelCliente(ItemPrecio.getPrecioMayoreoGranelCliente());
                                         wsItemPrecioCliente.setPrecioEspecialGranelCliente(ItemPrecio.getPrecioEspecialGranelCliente());
                                         
                                         listaPreciosCliente.addItem(wsItemPrecioCliente);
                                    }
                                    wsItemCliente.setListaPreciosEspeciales(listaPreciosCliente);
                                }                                
                                
                                

                            }catch(Exception ex){
                                ex.printStackTrace();
                            }
                            
                             /* Fin Precios Especiales */
                            
                            /* Valores de campos adicionales*/
                            try{
                                WsItemClienteCampoContenido wsItemClienteCampoContenido = null;
                                campoContenidoDto = campoContenidoBO.findClienteCampoContenido(item.getIdCliente(), -1, -1, -1, "");
                                listaCampoContenido = new WsListaClienteCampoContenido();
                                if(campoContenidoDto.length > 0){
                                    for(ClienteCampoContenido ItemContenido : campoContenidoDto){
                                        wsItemClienteCampoContenido = new WsItemClienteCampoContenido();
                                        wsItemClienteCampoContenido.setIdCliente(ItemContenido.getIdCliente());
                                        wsItemClienteCampoContenido.setIdClienteCampo(ItemContenido.getIdClienteCampo());
                                        wsItemClienteCampoContenido.setIdContenido(ItemContenido.getIdContenido());
                                        wsItemClienteCampoContenido.setLabelValor(ItemContenido.getValorLabel());
                                        listaCampoContenido.addItem(wsItemClienteCampoContenido);
                                    }
                                    wsItemCliente.setListaCampoContenido(listaCampoContenido);
                                }
                            }catch(Exception ex){
                                ex.printStackTrace();
                            }
                            /*Fin de valores de ampos adicionales*/
                            
                            
                            listaClientes.addItem(wsItemCliente);//Add elemento a list
                                                    
                            
                        }
                        
                        
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
        }

        return listaClientes.getLista();
    }
    
    
    /**
     * Consulta las Rutas que corresponden a un empleado promotor
     * que solicita los datos
     * @param empleadoDtoRequest
     * @return 
     */
    public ConsultaRutasResponse getRutasByEmpleado(String empleadoDtoRequestJSON){
        ConsultaRutasResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            
            response = this.getRutasByEmpleado(empleadoDtoRequest);
        }catch(Exception ex){
            response = new ConsultaRutasResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }
    
    /**
     * Consulta las Rutas que corresponden a un empleado promotor
     * que solicita los datos
     * @param empleadoDtoRequest
     * @return 
     */
    public ConsultaRutasResponse getRutasByEmpleado(EmpleadoDtoRequest empleadoDtoRequest){
        long idEmpresa = 0;
        long idEmpleado = 0;
        ConsultaRutasResponse consultaResponse = new ConsultaRutasResponse();
        try {
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                
                if (idEmpresa > 0) {
                    
                    Ruta[] rutas = new RutaDaoImpl(getConn()).findByDynamicWhere(
                            " ID_EMPLEADO=" + idEmpleado
                            , null);
                    
                    if (rutas.length<=0){
                        //No hay mas registros o actualizaciones pendientes
                        
                        consultaResponse.setError(false);
                        consultaResponse.setWsItemRuta(new ArrayList<WsItemRuta>());
                        return consultaResponse;
                    }else{
                        
                        for (Ruta ruta : rutas){
                            WsItemRuta wsItemRuta = new WsItemRuta();
                            wsItemRuta.setComentarioRuta(ruta.getComentarioRuta());
                            wsItemRuta.setFhRegRuta(ruta.getFhRegRuta());
                            wsItemRuta.setIdEmpleado(ruta.getIdEmpleado());
                            wsItemRuta.setIdRuta(ruta.getIdRuta());
                            wsItemRuta.setIdTipoRuta(ruta.getIdTipoRuta());
                            wsItemRuta.setNombreRuta(ruta.getNombreRuta());
                            wsItemRuta.setParadasRuta(ruta.getParadasRuta());
                            //wsItemRuta.setRecorridoRuta(ruta.getRecorridoRuta());
                            wsItemRuta.setDiasSemanaRuta(ruta.getDiasSemanaRuta());
                            
                            RutaMarcador[] rutaMarcadoresDto = new RutaMarcadorDaoImpl(getConn()).findByDynamicWhere(
                                    " ID_RUTA=" + ruta.getIdRuta()
                                    , null);
                            List<WsItemRutaMarcador> wsItemRutaMarcadores = new ArrayList<WsItemRutaMarcador>();
                            for (RutaMarcador marcadorDto : rutaMarcadoresDto){
                                WsItemRutaMarcador wsItemRutaMarcador = new WsItemRutaMarcador();
                                wsItemRutaMarcador.setIdCliente(marcadorDto.getIdCliente());
                                wsItemRutaMarcador.setIdProspecto(marcadorDto.getIdProspecto());
                                wsItemRutaMarcador.setIdRuta(marcadorDto.getIdRuta());
                                wsItemRutaMarcador.setIdRutaMarcador(marcadorDto.getIdRutaMarcador());
                                wsItemRutaMarcador.setInformacionMarcador(marcadorDto.getInformacionMarcador());
                                wsItemRutaMarcador.setIsVisitado(marcadorDto.getIsVisitado());
                                wsItemRutaMarcador.setLatitudMarcador(marcadorDto.getLatitudMarcador());
                                wsItemRutaMarcador.setLongitudMarcador(marcadorDto.getLongitudMarcador());
                                
                                wsItemRutaMarcadores.add(wsItemRutaMarcador);   
                            }
                            
                            wsItemRuta.setWsItemRutaMarcador(wsItemRutaMarcadores);
                            
                            consultaResponse.getWsItemRuta().add(wsItemRuta);
                        }
                        
                        consultaResponse.setError(false);
                        return consultaResponse;
                        
                    }
                    
                }
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            } else {
                consultaResponse.setError(true);
                consultaResponse.setNumError(901);
                consultaResponse.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        } catch (Exception e) {
            consultaResponse.setError(true);
            consultaResponse.setNumError(902);
            consultaResponse.setMsgError("Error inesperado al consultar Rutas. " + e.getLocalizedMessage());
        } finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }

        return consultaResponse;
    }
    
    public ConsultaInventarioInicialResponse getInventarioInicialByEmpleado(String empleadoDtoRequestJSON){
        ConsultaInventarioInicialResponse response;
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            
            response = this.getInventarioInicialByEmpleado(empleadoDtoRequest);
        }catch(Exception ex){
            response = new ConsultaInventarioInicialResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        return response;
    }
    
    public ConsultaInventarioInicialResponse getInventarioInicialByEmpleado(EmpleadoDtoRequest empleadoDtoRequest){
        ConsultaInventarioInicialResponse response = new ConsultaInventarioInicialResponse();
        long idEmpresa = 0;
        long idEmpleado = 0;
        
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                
                if (idEmpresa > 0) {
                    InventarioInicialVendedor[] inventario = new InventarioInicialVendedorDaoImpl(getConn()).findByDynamicWhere(" ID_EMPLEADO=" + idEmpleado, null);
                    
                    for(InventarioInicialVendedor item : inventario){
                        WsItemInventarioInicialResponse wsInv = new WsItemInventarioInicialResponse();
                        wsInv.setIdEmpleado(item.getIdEmpleado());
                        wsInv.setIdConcepto(item.getIdConcepto());
                        wsInv.setCantidad(item.getCantidad());
                        wsInv.setFechaRegistro(item.getFechaRegistro());
                        
                        response.getWsItemInventarioInicial().add(wsInv);
                    }
                }
                
            }else{
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
              }
        }catch(Exception ex){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al consultar Inventario inicial. " + ex.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
          
        return response;
    }
    
    public ConsultaAgendaResponse getAgendaByEmpleado(String empleadoDtoRequestJSON){
        ConsultaAgendaResponse response;
        
        EmpleadoDtoRequest empleadoDtoRequest = null;
        
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            
            response = this.getAgendaByEmpleado(empleadoDtoRequest);
            
        }catch(Exception ex){
            response = new ConsultaAgendaResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        return response;
    }
    
    public ConsultaAgendaResponse getAgendaByEmpleado(EmpleadoDtoRequest empleadoDtoRequest){
        ConsultaAgendaResponse consultaAgenda = new ConsultaAgendaResponse();
        long idEmpresa = 0;
        long idEmpleado = 0;
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());

            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
              idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
              idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();

              if (idEmpresa > 0) {

                  EmpleadoAgenda[] tareasAgenda = new EmpleadoAgendaDaoImpl(getConn()).findByDynamicWhere(" ID_EMPLEADO=" + idEmpleado + " AND FECHA_PROGRAMADA > DATE_SUB(NOW(), INTERVAL 7 DAY)", null);



                  for(EmpleadoAgenda tarea : tareasAgenda){
                      WsItemAgendaResponse wsAgenda = new WsItemAgendaResponse();
                      wsAgenda.setIdAgenda(tarea.getIdAgenda());
                      wsAgenda.setIdEmpleado(tarea.getIdEmpleado());
                      wsAgenda.setIdEstatus(tarea.getIdEstatus());
                      wsAgenda.setFechaCreacion(tarea.getFechaCreacion());
                      wsAgenda.setFechaProgramada(tarea.getFechaProgramada());
                      wsAgenda.setFechaEjecucion(tarea.getFechaEjecucion());
                      wsAgenda.setNombreTarea(tarea.getNombreTarea());
                      wsAgenda.setDescripcionTarea(tarea.getDescripcionTarea());
                      wsAgenda.setIdCliente(tarea.getIdCliente());

                      consultaAgenda.getWsItemAgenda().add(wsAgenda);
                  }

              }

              //registramos ubicacion
              try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            }else{
              consultaAgenda.setError(true);
              consultaAgenda.setNumError(901);
              consultaAgenda.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        }catch(Exception ex){
          consultaAgenda.setError(true);
          consultaAgenda.setNumError(902);
          consultaAgenda.setMsgError("Error inesperado al consultar Agendas. " + ex.getLocalizedMessage());
        }finally{
          try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return consultaAgenda;
    }
      
    public Date opeFechas(Date fecha,int dias){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fecha);
            calendar.add(Calendar.DAY_OF_YEAR, dias);

            return calendar.getTime();
    }
    
    private Date asignarDiaATime(Date time){
        if (time ==null)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        
        calendar.set(2000,01, 01);
        
        return calendar.getTime();
    }
    
    /**
     * Consulta las Geocercas que corresponden a un empleado promotor
     * que solicita los datos
     * @param empleadoDtoRequest
     * @return 
     */
    public ConsultaGeocercasResponse getGeocercasByEmpleado(String empleadoDtoRequestJSON){
        ConsultaGeocercasResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            
            response = this.getGeocercasByEmpleado(empleadoDtoRequest);
        }catch(Exception ex){
            response = new ConsultaGeocercasResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }
    
    /**
     * Consulta las Geocercas que corresponden a un empleado promotor
     * que solicita los datos
     * @param empleadoDtoRequest
     * @return 
     */
    public ConsultaGeocercasResponse getGeocercasByEmpleado(EmpleadoDtoRequest empleadoDtoRequest){
        long idEmpresa = 0;
        long idEmpleado = 0;
        ConsultaGeocercasResponse consultaResponse = new ConsultaGeocercasResponse();
        try {
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                
                if (idEmpresa > 0) {
                    
                    /*
                    Geocerca[] geocercas = new GeocercaDaoImpl(getConn()).findByDynamicWhere(
                            " ID_EMPLEADO=" + idEmpleado
                            , null);
                    */
                    //Consultamos geocerca
                    int idGeocerca = empleadoBO.getEmpleado().getIdGeocerca();
                    
                    //if (geocercas.length<=0){
                    if (idGeocerca<=0){
                        //No hay mas registros o actualizaciones pendientes
                        
                        consultaResponse.setError(false);
                        consultaResponse.setWsItemGeocerca(new ArrayList<WsItemGeocerca>());
                        return consultaResponse;
                    }else{
                        
                        //for (Geocerca geocerca : geocercas){
                        Geocerca geocerca =  new GeocercaBO(getConn()).findGeocercabyId(idGeocerca);
                            WsItemGeocerca wsItemGeocerca = new WsItemGeocerca();
                            wsItemGeocerca.setCoordenadas(geocerca.getCoordenadas());
                            wsItemGeocerca.setIdEmpresa(geocerca.getIdEmpresa());
                            wsItemGeocerca.setIdEstatus(geocerca.getIdEstatus());
                            wsItemGeocerca.setIdGeocerca(geocerca.getIdGeocerca());
                            wsItemGeocerca.setTipoGeocerca(geocerca.getTipoGeocerca());
                            
                            consultaResponse.getWsItemGeocerca().add(wsItemGeocerca);
                        //}
                        
                        consultaResponse.setError(false);
                        return consultaResponse;
                        
                    }
                    
                }
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            } else {
                consultaResponse.setError(true);
                consultaResponse.setNumError(901);
                consultaResponse.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        } catch (Exception e) {
            consultaResponse.setError(true);
            consultaResponse.setNumError(902);
            consultaResponse.setMsgError("Error inesperado al consultar Geocercas. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }

        return consultaResponse;
    }
    
    
    
    /**
     * Obtiene el catalogo de Conceptos de una empresa, haciendo login por empleado
     * @param empleadoDtoRequestJSON Datos de autenticacion en formato JSON, correspondientes a objeto EmpleadoDtoRequest
     */
    public ConsultaConceptosResponse getTotalCatalogoConceptosByEmpleado(String empleadoDtoRequestJSON) {
        ConsultaConceptosResponse response;                
        
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            
            response = this.getTotalCatalogoConceptosByEmpleado(empleadoDtoRequest);
        }catch(Exception ex){
            response = new ConsultaConceptosResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
            
        }
        
        return response;
    }
    
    /**
     * Obtiene el catalogo de Conceptos de una empresa, haciendo login por empleado
     * @param empleadoDtoRequest Datos de autenticacion
     */
    public ConsultaConceptosResponse getTotalCatalogoConceptosByEmpleado(EmpleadoDtoRequest empleadoDtoRequest) {
        int idEmpresa = 0;
        int idEmpleado = 0;
        ConsultaConceptosResponse consultaConceptosResponse = new ConsultaConceptosResponse();
        try {
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                //Si se encontro el registro buscamos su catalogo de productos
                if (idEmpresa > 0) {
                    consultaConceptosResponse.setError(false);
                    //consultaConceptosResponse.setListaConceptos(this.getListaConceptos(idEmpresa, idEmpleado));
                    consultaConceptosResponse.setTotalRegistros(this.getTotalListaConceptos(idEmpresa, idEmpleado,-1,-1));
                }
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            } else {
                consultaConceptosResponse.setError(true);
                consultaConceptosResponse.setNumError(901);
                consultaConceptosResponse.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        } catch (Exception e) {
            consultaConceptosResponse.setError(true);
            consultaConceptosResponse.setNumError(902);
            consultaConceptosResponse.setMsgError("Error inesperado al consultar catalogos del usuario. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }

        return consultaConceptosResponse;
    }
    
    public ConsultaCategoriasProveedorResponse getCategoriasProveedores(String empleadoDtoRequestJSON){
        ConsultaCategoriasProveedorResponse response;
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
             //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            response = this.getCategoriasProveedores(empleadoDtoRequest);
        }catch(Exception ex){
            response = new ConsultaCategoriasProveedorResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        return response;
    }
    
    public ConsultaCategoriasProveedorResponse getCategoriasProveedores(EmpleadoDtoRequest empleadoDtoRequest){
        int idEmpresa = 0;
        int idEmpleado = 0;
        
        ConsultaCategoriasProveedorResponse categoriasProveedor = new ConsultaCategoriasProveedorResponse();
        try{
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                if(idEmpresa > 0){
                    categoriasProveedor.setError(false);
                    categoriasProveedor.setListaCategorias(this.getListaCategoriasProv(idEmpresa));
                }
            }else {
                categoriasProveedor.setError(true);
                categoriasProveedor.setNumError(901);
                categoriasProveedor.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        }catch(Exception ex){
            categoriasProveedor.setError(true);
            categoriasProveedor.setNumError(902);
            categoriasProveedor.setMsgError("Error inesperado al consultar catalogos del usuario. " + ex.getLocalizedMessage());
            
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return categoriasProveedor;
    }
    
    public ConsultaClienteCategoriaResponse getClienteCategoriaByEmpleado(String empleadoDtoRequestJSON){
        ConsultaClienteCategoriaResponse response;
        
        EmpleadoDtoRequest empleadoDtoRequest = null;
        
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            
            response = this.getClienteCategoriaByEmpleado(empleadoDtoRequest);
        }catch(Exception ex){
            response = new ConsultaClienteCategoriaResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        return response;
    }
    
    public ConsultaClienteCategoriaResponse getClienteCategoriaByEmpleado(EmpleadoDtoRequest empleadoDtoRequest){
        int idEmpresa = 0;
        int idEmpleado = 0;
        
        ConsultaClienteCategoriaResponse consultaClienteCategoriaResponse = new ConsultaClienteCategoriaResponse();
        try{
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                
                if (idEmpresa > 0) {
                    consultaClienteCategoriaResponse.setError(false);
                    consultaClienteCategoriaResponse.setListaCategorias(this.getListaCategorias(idEmpresa));
                }
            }else {
                consultaClienteCategoriaResponse.setError(true);
                consultaClienteCategoriaResponse.setNumError(901);
                consultaClienteCategoriaResponse.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        }catch(Exception e){
            consultaClienteCategoriaResponse.setError(true);
            consultaClienteCategoriaResponse.setNumError(902);
            consultaClienteCategoriaResponse.setMsgError("Error inesperado al consultar catalogos del usuario. " + e.getLocalizedMessage());
        }
        finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return consultaClienteCategoriaResponse;
    }
    
    /**
     * Obtiene el catalogo de Conceptos de una empresa, haciendo login por empleado
     * @param empleadoDtoRequestJSON Datos de autenticacion en formato JSON, correspondientes a objeto EmpleadoDtoRequest
     */
    public ConsultaConceptosResponse getCatalogoConceptosByEmpleadoWithLimits(String empleadoDtoRequestJSON,int limitMin, int limitMax) {
        ConsultaConceptosResponse response;                
        
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            
            response = this.getCatalogoConceptosByEmpleadoWithLimits(empleadoDtoRequest,limitMin,limitMax);
        }catch(Exception ex){
            response = new ConsultaConceptosResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
            
        }
        
        return response;
    }
    
    /**
     * Obtiene el catalogo de Conceptos de una empresa, haciendo login por empleado
     * @param empleadoDtoRequestJSON Datos de autenticacion en formato JSON, correspondientes a objeto EmpleadoDtoRequest
     */
    public ConsultaConceptosServiciosResponse getCatalogoConceptosServiciosProveedoresWifiWithLimits(String empleadoDtoRequestJSON,int limitMin, int limitMax) {
        ConsultaConceptosServiciosResponse response;                
        
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            
            response = this.getCatalogoConceptosServiciosProveedoresWifiWithLimits(empleadoDtoRequest,limitMin,limitMax);
        }catch(Exception ex){
            response = new ConsultaConceptosServiciosResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
            
        }
        
        return response;
    }
    
    /**
     * Obtiene el catalogo de Conceptos de proveedores por proveedores, haciendo login por empleado
     * @param empleadoDtoRequestJSON Datos de autenticacion en formato JSON, correspondientes a objeto EmpleadoDtoRequest
     */
    public ConsultaProveedorConceptosServiciosResponse getCatalogoConceptosServiciosProveedoresByProveedorWifiWithLimits(String empleadoDtoRequestJSON,int limitMin, int limitMax) {
        ConsultaProveedorConceptosServiciosResponse response;                
        
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            
            response = this.getCatalogoConceptosServiciosProveedoresByProveedorWifiWithLimits(empleadoDtoRequest,limitMin,limitMax);
        }catch(Exception ex){
            response = new ConsultaProveedorConceptosServiciosResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
            
        }
        
        return response;
    }
    
    /**
     * Obtiene el catalogo de Conceptos de una empresa, haciendo login por empleado
     * @param empleadoDtoRequest Datos de autenticacion
     */
    public ConsultaConceptosResponse getCatalogoConceptosByEmpleadoWithLimits(EmpleadoDtoRequest empleadoDtoRequest,int limitMin, int limitMax) {
        int idEmpresa = 0;
        int idEmpleado = 0;
        ConsultaConceptosResponse consultaConceptosResponse = new ConsultaConceptosResponse();
        try {
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                //Si se encontro el registro buscamos su catalogo de productos
                if (idEmpresa > 0) {
                    consultaConceptosResponse.setError(false);
                    consultaConceptosResponse.setListaConceptos(this.getListaConceptos(idEmpresa, idEmpleado,limitMin,limitMax)); 
                    consultaConceptosResponse.setTotalRegistros(this.getTotalListaConceptos(idEmpresa, idEmpleado,limitMin,limitMax));  
                }
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            } else {
                consultaConceptosResponse.setError(true);
                consultaConceptosResponse.setNumError(901);
                consultaConceptosResponse.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        } catch (Exception e) {
            consultaConceptosResponse.setError(true);
            consultaConceptosResponse.setNumError(902);
            consultaConceptosResponse.setMsgError("Error inesperado al consultar catalogos del usuario. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }

        return consultaConceptosResponse;
    }
    
    /**
     * Obtiene el catalogo de Conceptos de proveedores de una empresa, haciendo login por empleado
     * @param empleadoDtoRequest Datos de autenticacion
     */
    public ConsultaConceptosServiciosResponse getCatalogoConceptosServiciosProveedoresWifiWithLimits(EmpleadoDtoRequest empleadoDtoRequest,int limitMin, int limitMax) {
        int idEmpresa = 0;
        int idEmpleado = 0;
        ConsultaConceptosServiciosResponse consultaConceptosResponse = new ConsultaConceptosServiciosResponse();
        try {
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                //Si se encontro el registro buscamos su catalogo de productos
                if (idEmpresa > 0) {
                    consultaConceptosResponse.setError(false);
                    consultaConceptosResponse.setListaConceptos(this.getListaConceptosServiciosProveedores(idEmpresa, idEmpleado,limitMin,limitMax, 1)); 
                    consultaConceptosResponse.setTotalRegistros(this.getTotalListaConceptosServiciosProveedor(idEmpresa, idEmpleado,limitMin,limitMax));  
                }
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            } else {
                consultaConceptosResponse.setError(true);
                consultaConceptosResponse.setNumError(901);
                consultaConceptosResponse.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        } catch (Exception e) {
            consultaConceptosResponse.setError(true);
            consultaConceptosResponse.setNumError(902);
            consultaConceptosResponse.setMsgError("Error inesperado al consultar catalogos del usuario. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }

        return consultaConceptosResponse;
    }
    
    /**
     * Obtiene el catalogo de Conceptos de proveedores de una empresa, haciendo login por empleado
     * @param empleadoDtoRequest Datos de autenticacion
     */
    public ConsultaProveedorConceptosServiciosResponse getCatalogoConceptosServiciosProveedoresByProveedorWifiWithLimits(EmpleadoDtoRequest empleadoDtoRequest,int limitMin, int limitMax) {
        int idEmpresa = 0;
        int idEmpleado = 0;
        ConsultaProveedorConceptosServiciosResponse consultaConceptosResponse = new ConsultaProveedorConceptosServiciosResponse();
        try {
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                //Si se encontro el registro buscamos su catalogo de productos
                if (idEmpresa > 0) {
                    consultaConceptosResponse.setError(false);
                    consultaConceptosResponse.setListaConceptos(this.getListaConceptosServiciosProveedoresByProveedor(idEmpresa, idEmpleado,limitMin,limitMax, 1));                     
                    consultaConceptosResponse.setTotalRegistros(this.getTotalListaProveedorConceptosServiciosProveedor(idEmpresa, idEmpleado,limitMin,limitMax));  
                }
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            } else {
                consultaConceptosResponse.setError(true);
                consultaConceptosResponse.setNumError(901);
                consultaConceptosResponse.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        } catch (Exception e) {
            consultaConceptosResponse.setError(true);
            consultaConceptosResponse.setNumError(902);
            consultaConceptosResponse.setMsgError("Error inesperado al consultar catalogos del usuario. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }

        return consultaConceptosResponse;
    }
    
    
    /**
     * Obtiene el catalogo de Conceptos de una empresa, haciendo login por empleado
     * @param empleadoDtoRequestJSON Datos de autenticacion en formato JSON, correspondientes a objeto EmpleadoDtoRequest
     */
    public ConsultaConceptosResponse getCatalogoConceptosByEmpleadoWiFiWithLimits(String empleadoDtoRequestJSON , int limMin , int limMax ) {
        ConsultaConceptosResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            
            response = this.getCatalogoConceptosByEmpleadoWiFiWithLimits(empleadoDtoRequest , limMin , limMax);
        }catch(Exception ex){
            response = new ConsultaConceptosResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }
    
    /**
     * Obtiene el catalogo de Conceptos de una empresa, haciendo login por empleado
     * @param empleadoDtoRequest Datos de autenticacion
     */
    public ConsultaConceptosResponse getCatalogoConceptosByEmpleadoWiFiWithLimits(EmpleadoDtoRequest empleadoDtoRequest , int limMin , int limMax) {
        int idEmpresa = 0;
        int idEmpleado = 0;
        ConsultaConceptosResponse consultaConceptosResponse = new ConsultaConceptosResponse();
        try {
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                //Si se encontro el registro buscamos su catalogo de productos
                if (idEmpresa > 0) {
                    consultaConceptosResponse.setError(false);
                    consultaConceptosResponse.setListaConceptos(this.getListaConceptosWiFi(idEmpresa, idEmpleado, limMin ,limMax));
                    consultaConceptosResponse.setTotalRegistros(this.getTotalListaConceptos(idEmpresa, idEmpleado, limMin, limMax));
                }
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            } else {
                consultaConceptosResponse.setError(true);
                consultaConceptosResponse.setNumError(901);
                consultaConceptosResponse.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        } catch (Exception e) {
            consultaConceptosResponse.setError(true);
            consultaConceptosResponse.setNumError(902);
            consultaConceptosResponse.setMsgError("Error inesperado al consultar catalogos del usuario. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }

        return consultaConceptosResponse;
    }
    
    
    
    
    /**
     * Consulta un listado de Conceptos por empresa
     * @param idEmpresa ID de la empresa que se va a consultar, en caso de ser sucursal se recuperan tambien lo correspondiente a la empresa padre
     * @return Lista de objetos WsItemConcepto
     */
    public int getTotalListaConceptos(int idEmpresa, int idEmpleado , int limMin , int limMax) {

        WsListaConceptos wsListaConceptos = new WsListaConceptos();
        Encrypter encriptacion = new Encrypter();
        int totalConceptos = 0;

        //Buscamos todos los Conceptos de la Empresa
        //ConceptoDaoImpl conceptoDao = new ConceptoDaoImpl(getConn());
        Concepto[] arrayConceptoDto;
        ConceptoBO conceptoBO = new ConceptoBO(getConn());

        try {
            
            arrayConceptoDto = conceptoBO.findConceptos(-1, idEmpresa, limMin, limMax, " AND ID_ESTATUS = 1 AND MATERIA_PRIMA=0 ");           
                        
            if (arrayConceptoDto!=null){                
                return arrayConceptoDto.length;
            }
                
                
        } catch (Exception e) {}
        
        return totalConceptos;
    }
    
    /**
     * Consulta un listado de Conceptos de proveedores por empresa
     * @param idEmpresa ID de la empresa que se va a consultar, en caso de ser sucursal se recuperan tambien lo correspondiente a la empresa padre
     * @return Lista de objetos WsItemConcepto
     */
    public int getTotalListaConceptosServiciosProveedor(int idEmpresa, int idEmpleado , int limMin , int limMax) {

        WsListaConceptosServiciosProveedores wsListaConceptos = new WsListaConceptosServiciosProveedores();
        
        int totalConceptos = 0;

        //Buscamos todos los Conceptos de la Empresa
        //ConceptoDaoImpl conceptoDao = new ConceptoDaoImpl(getConn());
        SgfensProductoServicio[] arrayConceptoDto;
        SGProductoServicioBO conceptoBO = new SGProductoServicioBO(getConn());

        try {
            
            arrayConceptoDto = conceptoBO.findSgfensProductoServicios(-1, idEmpresa, limMin, limMax, " AND ID_ESTATUS != 2 ");           
                        
            if (arrayConceptoDto!=null){                
                return arrayConceptoDto.length;
            }
                
                
        } catch (Exception e) {}
        
        return totalConceptos;
    }
    
    /**
     * Consulta un listado de Conceptos de proveedores
     * @param idEmpresa ID de la empresa que se va a consultar, en caso de ser sucursal se recuperan tambien lo correspondiente a la empresa padre
     * @return Lista de objetos WsItemConcepto
     */
    public int getTotalListaProveedorConceptosServiciosProveedor(int idEmpresa, int idEmpleado , int limMin , int limMax) {

        WsListaProveedorConceptosServiciosProveedores wsListaConceptos = new WsListaProveedorConceptosServiciosProveedores();
        
        int totalConceptos = 0;

        //Buscamos todos los Conceptos de la Empresa
        //ConceptoDaoImpl conceptoDao = new ConceptoDaoImpl(getConn());
        SgfensProveedorProducto[] arrayConceptoDto;
        SGProveedorProductoBO conceptoBO = new SGProveedorProductoBO(getConn());

        try {
            
            arrayConceptoDto = conceptoBO.findSgfensProveedorProductos(-1, idEmpresa, limMin, limMax, " ");
                        
            if (arrayConceptoDto!=null){                
                return arrayConceptoDto.length;
            }
                
                
        } catch (Exception e) {}
        
        return totalConceptos;
    }
    
    
    /**
     * Obtiene el catalogo de gastos de una empresa, haciendo login por empleado
     * @param empleadoDtoRequestJSON Datos de autenticacion en formato JSON, correspondientes a objeto EmpleadoDtoRequest
     */
    public ConsultaCatalogoGastosResponse getCatalogoGastos(String empleadoDtoRequestJSON) {
        ConsultaCatalogoGastosResponse response;                
        
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            
            response = this.getCatalogoGastos(empleadoDtoRequest);
        }catch(Exception ex){
            response = new ConsultaCatalogoGastosResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
            
        }
        
        return response;
    }
    
    
    public ConsultaCatalogoGastosResponse getCatalogoGastos(EmpleadoDtoRequest empleadoDtoRequest){
        int idEmpresa = 0;
        int idUsuario = 0;
        ConsultaCatalogoGastosResponse response = new ConsultaCatalogoGastosResponse();
        try{
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idUsuario = empleadoBO.getUsuarioBO().getUser().getIdUsuarios();
                //Si se encontro el registro buscamos su catalogo de gastos de la empresa
                if (idEmpresa > 0) {
                    response.setError(false);
                    response.setWsItemCatalogoGastos(this.getListaCatalogoGastos(idEmpresa, " AND ID_ESTATUS = 1"));
                }
                 //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            }else {
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        }catch(Exception e){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al consultar catalogos del usuario. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return response;
    }
    
    
    private ArrayList<WsItemCatalogoGastos> getListaCatalogoGastos(int idEmpresa, String filtroBusqueda){
        WsListaCatalogoGastos listaCatalogoGastos = new WsListaCatalogoGastos();
        CatalogoGastosDaoImpl catalogoGastosDao = new CatalogoGastosDaoImpl(getConn());
        CatalogoGastosBO catalogoGastosBO = new CatalogoGastosBO(getConn());
        CatalogoGastos[] arrayCatalogoGastosDto;
        try{
            arrayCatalogoGastosDto = catalogoGastosBO.findGastoMotivo(-1, idEmpresa, 0, 0, filtroBusqueda);
            if(arrayCatalogoGastosDto.length > 0){
                for(CatalogoGastos item : arrayCatalogoGastosDto){
                    try{
                        if(item.getIdEstatus() != 2){
                            WsItemCatalogoGastos wsItemCatalogoGastos = new WsItemCatalogoGastos();
                            
                            wsItemCatalogoGastos.setIdCatalogoGastos(item.getIdGastos());
                            wsItemCatalogoGastos.setIdEmpresa(item.getIdEmpresa());
                            wsItemCatalogoGastos.setNombre(item.getNombre());
                            wsItemCatalogoGastos.setDescripcion(item.getDescripcion());
                            wsItemCatalogoGastos.setIdEstatus(item.getIdEstatus());
                            
                            listaCatalogoGastos.addItem(wsItemCatalogoGastos);
                        }
                    }catch(Exception ex){
                        
                    }
                }
            }
        }catch(Exception ex){
            
        }
        return listaCatalogoGastos.getLista();
    }
    
    
    /**
     * Obtiene el catalogo de gastos de un empleado, haciendo login por empleado
     * @param empleadoDtoRequestJSON Datos de autenticacion en formato JSON, correspondientes a objeto EmpleadoDtoRequest
     */

    public ConsultaGastosResponse getGastosByEmpleado(String empleadoDtoRequestJson) {
        ConsultaGastosResponse response;                
        
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJson, EmpleadoDtoRequest.class);
            
            response = this.getGastosByEmpleado(empleadoDtoRequest);
        }catch(Exception ex){
            response = new ConsultaGastosResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
            
        }
        
        return response;
    }
    
    public ConsultaPuntosInteresResponse getPuntosByEmpleado(String empleadoDtoRequestJson){
        ConsultaPuntosInteresResponse response;
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
             //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJson, EmpleadoDtoRequest.class);
            
            response = this.getPuntosByEmpleado(empleadoDtoRequest);
        }catch(Exception ex){
            response = new ConsultaPuntosInteresResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        return response;
    }
    
    public ConsultaPuntosInteresResponse getPuntosByEmpleado(EmpleadoDtoRequest empleadoDtoRequest){
        int idEmpresa = 0;
        int idEmpleado = 0;
        ConsultaPuntosInteresResponse consultaPuntosInteresResponse = new ConsultaPuntosInteresResponse();
        try{
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                //Si se encontro el registro buscamos sus gastos
                if (idEmpresa > 0) {
                    consultaPuntosInteresResponse.setError(false);
                    consultaPuntosInteresResponse.setListaPuntos(this.getListaPuntos(idEmpresa, idEmpleado, -1, -1, ""));
                }
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            }else{
                consultaPuntosInteresResponse.setError(true);
                consultaPuntosInteresResponse.setNumError(901);
                consultaPuntosInteresResponse.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");   
            }
        }catch(Exception e){
            consultaPuntosInteresResponse.setError(true);
            consultaPuntosInteresResponse.setNumError(902);
            consultaPuntosInteresResponse.setMsgError("Error inesperado al consultar los puntos de interes");
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return consultaPuntosInteresResponse;
    }

    private ConsultaGastosResponse getGastosByEmpleado(EmpleadoDtoRequest empleadoDtoRequest) {
        int idEmpresa = 0;
        int idEmpleado = 0;
        ConsultaGastosResponse consultaGastosResponse = new ConsultaGastosResponse();
        try {
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                //Si se encontro el registro buscamos sus gastos
                if (idEmpresa > 0) {
                    consultaGastosResponse.setError(false);
                    consultaGastosResponse.setListaGastos(this.getListaGastos(idEmpresa, idEmpleado,-1,-1,""));
                }
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            } else {
                consultaGastosResponse.setError(true);
                consultaGastosResponse.setNumError(901);
                consultaGastosResponse.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        } catch (Exception e) {
            consultaGastosResponse.setError(true);
            consultaGastosResponse.setNumError(902);
            consultaGastosResponse.setMsgError("Error inesperado al consultar catalogos del usuario. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }

        return consultaGastosResponse;
        
    }
    
    public ConsultaAlmacenesResponse getAlmacenesByEmpleado(String empleadoDtoRequestJson){
        ConsultaAlmacenesResponse response;
        EmpleadoDtoRequest empleadoDtoRequest;
        try{
             //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJson, EmpleadoDtoRequest.class);
            
            response = this.getAlmacenesByEmpleado(empleadoDtoRequest);
        }catch(Exception ex){
            response = new ConsultaAlmacenesResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        return response;
    }
    
    /**
     * Consulta los almacenes que pertenecen a la empresa del Empleado
     * @param empleadoDtoRequest
     * @return 
     */
    private ConsultaAlmacenesResponse getAlmacenesByEmpleado(EmpleadoDtoRequest empleadoDtoRequest) {
        int idEmpresa;
        int idEmpleado;
        ConsultaAlmacenesResponse response = new ConsultaAlmacenesResponse();
        try {
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                //Si se encontro el registro buscamos sus gastos
                if (idEmpresa > 0) {
                    response.setError(false);
                    response.setListaAlmacenes(this.getListaAlmacen(idEmpresa, idEmpleado,-1,-1,""));
                }
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            } else {
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        } catch (Exception e) {
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al consultar Almacenes de empresa. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }

        return response;
        
    }
    
    private List<WsItemPuntoInteresEvc> getListaPuntos(int idEmpresa, int idEmpleado, int limitMin, int limitMax, String filtroBusqueda){
        WsListaPuntosEvc listaPuntosEvc = new WsListaPuntosEvc();
        PuntosInteresDaoImpl puntosEvcDao = new PuntosInteresDaoImpl(getConn());
        PuntosInteresBO puntosBO = new PuntosInteresBO(getConn());
        PuntosInteres[] arrayPuntosInteresDto;
        try{
            arrayPuntosInteresDto = puntosBO.findPuntos(-1, idEmpresa, limitMin, limitMax, filtroBusqueda);
            if(arrayPuntosInteresDto.length > 0){
                for(PuntosInteres item : arrayPuntosInteresDto){
                    try{
                        WsItemPuntoInteresEvc wsItemPunto = new WsItemPuntoInteresEvc();
                        wsItemPunto.setIdPunto(item.getIdPunto());
                        wsItemPunto.setNombre(item.getNombre());
                        wsItemPunto.setDescripcion(item.getDescripcion());
                        wsItemPunto.setLatitud(item.getLatitud());
                        wsItemPunto.setLongitud(item.getLongitud());
                        wsItemPunto.setIdTipoPunto(item.getIdTipoPunto());
                        wsItemPunto.setDireccion(item.getDireccion());
                        listaPuntosEvc.addItem(wsItemPunto);
                    }catch(Exception e){
                        
                    }
                }
            }
        }catch(Exception e){
            
        }
        return listaPuntosEvc.getLista();
    }

    private List<WsItemGastosEvc> getListaGastos(int idEmpresa, int idEmpleado, int limitMin , int limitMax, String filtroBusqueda) {
        
        WsListaGastosEvc listaGastosEvc = new WsListaGastosEvc();
        GastosEvcDaoImpl gastosEvcDao = new GastosEvcDaoImpl(getConn());
        GastosEvcBO gastosEvcBO = new GastosEvcBO(getConn());
        GastosEvc[] arrayGastosEvcDto;
        try{
            arrayGastosEvcDto = gastosEvcBO.findGastosEvc(-1, idEmpresa, limitMin, limitMax, filtroBusqueda + " AND ID_ESTATUS != 2 AND ID_EMPLEADO = " + idEmpleado + " " );
            if(arrayGastosEvcDto.length > 0){ 
                for(GastosEvc item : arrayGastosEvcDto){
                    try{
                        if(item.getIdEstatus() != 2){
                            WsItemGastosEvc wsItemGastosEvc = new WsItemGastosEvc();
                            
                            wsItemGastosEvc.setIdGastos(item.getIdGastos());
                            wsItemGastosEvc.setIdConcepto(item.getIdConcepto());
                            wsItemGastosEvc.setIdEmpleado(item.getIdEmpleado());
                            wsItemGastosEvc.setComentario(item.getComentario());
                            wsItemGastosEvc.setFecha(item.getFecha());
                            wsItemGastosEvc.setMonto(item.getMonto());
                            wsItemGastosEvc.setLatitud(item.getLatitud());
                            wsItemGastosEvc.setLongitud(item.getLongitud());
                            wsItemGastosEvc.setIdEstatus(item.getIdEstatus());
                            wsItemGastosEvc.setReferencia(item.getReferencia());
                            wsItemGastosEvc.setIdEmpresa(item.getIdEmpresa());
                            wsItemGastosEvc.setValidacion(item.getValidacion());
                            
                            listaGastosEvc.addItem(wsItemGastosEvc);
                        }
                    }catch(Exception ex){
                        
                    }
                }
            }
        }catch(Exception ex){
            
        }
        return listaGastosEvc.getLista();
        
    }

    private List<WsItemAlmacen> getListaAlmacen(int idEmpresa, int idEmpleado, int limitMin , int limitMax, String filtroBusqueda) {
        
        WsListaAlmacenes listaAlmacen = new WsListaAlmacenes();
        AlmacenBO almacenBO = new AlmacenBO(getConn());
        Almacen[] arrayAlmacenDto;
        
        try{
            arrayAlmacenDto = almacenBO.findAlmacens(-1, idEmpresa, limitMin, limitMax, filtroBusqueda + " AND ID_ESTATUS != 2 " );
            if(arrayAlmacenDto.length > 0){ 
                for(Almacen item : arrayAlmacenDto){
                    try{
                        WsItemAlmacen wsItemAlmacen = new WsItemAlmacen();

                        wsItemAlmacen.setIdAlmacen(item.getIdAlmacen());
                        wsItemAlmacen.setIdEmpresa(item.getIdEmpresa());
                        wsItemAlmacen.setIdEstatus(item.getIdEstatus());
                        wsItemAlmacen.setNombre(item.getNombre());
                        wsItemAlmacen.setDireccion(item.getDireccion());
                        wsItemAlmacen.setAreaAlmacen(item.getAreaAlmacen());
                        wsItemAlmacen.setResponsable(item.getResponsable());
                        wsItemAlmacen.setPuesto(item.getPuesto());
                        wsItemAlmacen.setTelefono(item.getTelefono());
                        wsItemAlmacen.setCorreo(item.getCorreo());
                        wsItemAlmacen.setIsPrincipal(item.getIsPrincipal());
                        wsItemAlmacen.setExcluirMoviles(item.getExcluirMoviles());

                        listaAlmacen.addItem(wsItemAlmacen);
                    }catch(Exception ex){
                        
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return listaAlmacen.getLista();
        
    }
    
    public ConsultaProductosCreditoResponse getProductoCreditoByEmpleado(String empleadoDtoRequestJSON){
        ConsultaProductosCreditoResponse response;
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            response = this.getProductoCreditoByEmpleado(empleadoDtoRequest);
        }catch(Exception e){
            response = new ConsultaProductosCreditoResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + e.toString());
            e.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return response;
    }
    
    public ConsultaProductosCreditoResponse getProductoCreditoByEmpleado(EmpleadoDtoRequest empleadoDtoRequest){
        ConsultaProductosCreditoResponse response = new ConsultaProductosCreditoResponse();
        try{
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                int idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                int idUsuario = empleadoBO.getEmpleado().getIdUsuarios();
                if (idEmpresa > 0) {
                    response.setWsItemProductoCredito(this.getListProdcutoCredito(idEmpresa, idUsuario));
                    response.setError(false);
                }
            }else{
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        }catch(Exception e){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al consultar prodcutos del usuario. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return response;
    }
    
    public ConsultaScoreResponse getScoreByEmpleado(String empleadoDtoRequestJSON){
        ConsultaScoreResponse response;
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            response = this.getScoreByEmpleado(empleadoDtoRequest);
        }catch(Exception e){
            response = new ConsultaScoreResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + e.toString());
            e.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return response;
    }
    
    public ConsultaScoreResponse getScoreByEmpleado(EmpleadoDtoRequest empleadoDtoRequest){
        ConsultaScoreResponse response = new ConsultaScoreResponse();
        try{
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                int idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                int idUsuario = empleadoBO.getEmpleado().getIdUsuarios();
                if (idEmpresa > 0) {
                    response.setWsItemScore(getListScore(idEmpresa, idUsuario));
                    response.setError(false);
                }
            }else{
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        }catch(Exception e){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al consultar score del usuario. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return response;
    }
  
    public ConsultaFormulariosResponse getFormulariosByEmpleado(String empleadoDtoRequestJSON){
        ConsultaFormulariosResponse response;
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            response = this.getFormulariosByEmpleado(empleadoDtoRequest);
        }catch(Exception e){
            response = new ConsultaFormulariosResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + e.toString());
            e.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return response;
    }
    
    public ConsultaFormulariosResponse getFormulariosByEmpleado(EmpleadoDtoRequest empleadoDtoRequest){
        ConsultaFormulariosResponse response = new ConsultaFormulariosResponse();
        try{
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                int idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                int idUsuario = empleadoBO.getEmpleado().getIdUsuarios();
                if (idEmpresa > 0) {
                    String filtroAdicional = "";
                    response.setWsItemTipoCampo(this.getListaTipoCampo(idEmpresa, idUsuario));
                    response.setWsItemFormularioValidacion(this.getListaValidacion(idEmpresa, idUsuario));
                    response.setWsItemGrupoFormulario(this.getGrupoFormulario(idEmpresa, idUsuario));
                    response.setError(false);
                }
            }else{
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        }catch(Exception e){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al consultar formularios del usuario. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return response;
        
    }
    
    private List<WsItemProductoCredito> getListProdcutoCredito(int idEmpresa, int idUsuario){
        List<WsItemProductoCredito> lstProductos = new ArrayList<WsItemProductoCredito>();
        CrProductoCreditoBO productoBO = new CrProductoCreditoBO(getConn());
        try{
            CrProductoCredito[] productosArray = productoBO.findCrProductoCreditos(-1, idEmpresa, 0, 0, " AND id_estatus=1 ");
            for(CrProductoCredito item : productosArray){
                WsItemProductoCredito wipc = new WsItemProductoCredito();
                wipc.setIdProductoCredito(item.getIdProductoCredito());
                wipc.setIdProductoCreditoPadre(item.getIdProductoCreditoPadre());
                wipc.setNombre(item.getNombre());
                wipc.setDescripcion(item.getDescripcion());
                wipc.setFechaHrCreacion(item.getFechaHrCreacion());
                wipc.setFechaHrUltimaEdicion(item.getFechaHrUltimaEdicion());
                wipc.setIdUsuarioEdicion(item.getIdUsuarioEdicion());
                wipc.setIdScore(item.getIdScore());
                wipc.setIdGrupoFormularioSolic(item.getIdGrupoFormularioSolic());
                wipc.setIdGrupoFormularioVerif(item.getIdGrupoFormularioVerif());
                wipc.setTipoAmortizacion(item.getTipoAmortizacion());
                wipc.setMonto(item.getMonto());
                wipc.setPlazo(item.getPlazo());
                wipc.setTazaInteresAnual(item.getTasaInteresAnual());
                wipc.setTazaInteresMora(item.getTasaInteresMora());
                wipc.setGatosCobranza(item.getGastosCobranza());
                wipc.setCostoAnualTotal(item.getCostoAnualTotal());
                wipc.setGarantiasDescripcion(item.getGarantiasDescripcion());
                wipc.setIdEmpresa(item.getIdEmpresa());
                wipc.setIdEstatus(item.getIdEstatus());
                wipc.setIdGrupoFormularioFotos(item.getIdGrupoFormularioFotos());
                
                //Producto regla
                {
                    CrProductoReglaDaoImpl reglaDaoImpl = new CrProductoReglaDaoImpl(getConn());
                    CrProductoRegla[] reglasArray = reglaDaoImpl.findWhereIdProductoCreditoEquals(item.getIdProductoCredito());
                    for(CrProductoRegla regla : reglasArray){
                        if(regla.getIdEstatus() == 1){
                            WsItemProductoReglaResponse wipr = new WsItemProductoReglaResponse();
                            wipr.setIdProductoRegla(regla.getIdProductoRegla());
                            wipr.setIdProductoCredito(regla.getIdProductoCredito());
                            wipr.setEtiqueta(regla.getEtiqueta());
                            wipr.setRangoMin(regla.getRangoMin());
                            wipr.setRangoMax(regla.getRangoMax());
                            wipr.setValorExacto(regla.getValorExacto());
                            wipr.setIdFormularioCampo(regla.getIdFormularioCampo());
                            wipr.setEtiquetaCampoRelacion(regla.getEtiquetaCampoRelacion());
                            wipr.setIsReglaAplicacionScore(regla.getIsReglaAplicacionScore());
                            wipr.setIsReglaRechazo(regla.getIsReglaRechazo());
                            wipr.setClaveTipoRegla(regla.getClaveTipoRegla());
                            wipr.setIdEmpresa(regla.getIdEmpresa());
                            wipr.setIdEstatus(regla.getIdEstatus());

                            wipc.getWsItemProductoRegla().add(wipr);
                        }
                    }
                }
                
                //Producto puntaje monto
                {
                    CrProductoPuntajeMontoDaoImpl puntajeMontoDaoImpl = new CrProductoPuntajeMontoDaoImpl(getConn());
                    CrProductoPuntajeMonto[] puntajeArray = puntajeMontoDaoImpl.findWhereIdProductoCreditoEquals(item.getIdProductoCredito());
                    for(CrProductoPuntajeMonto puntaje : puntajeArray){
                        if(puntaje.getIdEstatus() == 1){
                            WsItemProductoPuntejeMontoResponse wipp = new WsItemProductoPuntejeMontoResponse();
                            wipp.setIdProductoPuntajeMonto(puntaje.getIdProductoPuntajeMonto());
                            wipp.setIdProductoCredito(puntaje.getIdProductoCredito());
                            wipp.setRangoMin(puntaje.getRangoMin());
                            wipp.setRangoMax(puntaje.getRangoMax());
                            wipp.setPctAutorizado(puntaje.getPctAutorizado());
                            wipp.setIdEmpresa(puntaje.getIdEmpresa());
                            wipp.setIdEstatus(puntaje.getIdEstatus());
                            
                            wipc.getWsItemProductoPuntajeMonto().add(wipp);
                        }
                    }
                }
                lstProductos.add(wipc);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return lstProductos;
    }
    
    private List<WsItemScore> getListScore(int idEmpresa, int idUsuario){
        List<WsItemScore> listScore = new ArrayList<WsItemScore>();
        CrScoreBO scoreBO = new CrScoreBO(getConn());
        try{
            CrScore[] scoreArray = scoreBO.findCrScores(-1, idEmpresa, 0, 0, " AND id_estatus=1 ");
            for(CrScore item : scoreArray){
                WsItemScore wis = new WsItemScore();
                wis.setIdScore(item.getIdScore());
                wis.setNombre(item.getNombre());
                wis.setDescripcion(item.getDescripcion());
                wis.setFechaHrCreacion(item.getFechaHrCreacion());
                wis.setFechaHrUltimaEdicion(item.getFechaHrUltimaEdicion());
                wis.setIdUsuarioEdicion(item.getIdUsuarioEdicion());
                wis.setIdEmpresa(item.getIdEmpresa());
                wis.setIdEstatus(item.getIdEstatus());
                
                //Detalle de score
                {
                    CrScoreDetalleDaoImpl daoImpl = new CrScoreDetalleDaoImpl(getConn());
                    CrScoreDetalle[] detalleArray = daoImpl.findWhereIdScoreEquals(item.getIdScore());
                    for(CrScoreDetalle detalle : detalleArray){
                        if(detalle.getIdEstatus() == 1){
                            WsItemScoreDetalle wisd = new WsItemScoreDetalle();
                            
                            wisd.setIdScoreDetalle(detalle.getIdScoreDetalle());
                            wisd.setIdFormularioCampo(detalle.getIdFormularioCampo());
                            wisd.setIdScore(detalle.getIdScore());
                            wisd.setValorExacto(detalle.getValorExacto());
                            wisd.setRangoMin(detalle.getRangoMin());
                            wisd.setRangoMax(detalle.getRangoMax());
                            wisd.setPuntosScore(detalle.getPuntosScore());
                            wisd.setIdEmpresa(detalle.getIdEmpresa());
                            wisd.setIdEstatus(detalle.getIdEstatus());
                            
                            wis.getWsItemScore().add(wisd);
                        }
                    }
                }
                listScore.add(wis);
            }
        }catch(Exception e){
            e.printStackTrace();;
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return listScore;
    }
    
    private List<WsItemGrupoFormulario> getGrupoFormulario(int idEmpresa, int idUsuario){
        List<WsItemGrupoFormulario> listaGrupoForm = new ArrayList<WsItemGrupoFormulario>();
        CrGrupoFormularioBO grupoFormularioBO = new CrGrupoFormularioBO(getConn());
        try{
            CrGrupoFormulario[] grupoArray = grupoFormularioBO.findCrGrupoFormularios(-1, idEmpresa, 0, 0, " AND id_estatus=1 ");
            for(CrGrupoFormulario item : grupoArray){
                WsItemGrupoFormulario wigf = new WsItemGrupoFormulario();
                wigf.setDescripcion(item.getDescripcion());
                wigf.setFechaHrCreacion(item.getFechaHrCreacion());
                wigf.setIdEmpresa(item.getIdEmpresa());
                wigf.setIdEstatus(item.getIdEstatus());
                wigf.setIdGrupoFormulario(item.getIdGrupoFormulario());
                wigf.setNombre(item.getNombre());
                
                //Formulario evento
                {
                    CrFormularioEventoDaoImpl eventoDaoImpl = new CrFormularioEventoDaoImpl(getConn());
                    CrFormularioEvento[] eventosArray = eventoDaoImpl.findWhereIdGrupoFormularioEquals(item.getIdGrupoFormulario());
                    for(CrFormularioEvento ev : eventosArray){
                        if(ev.getIdEstatus() == 1){
                            WsItemFormularioEvento eventosItem = new WsItemFormularioEvento();

                            eventosItem.setFechaHrCreacion(ev.getFechaHrCreacion());
                            eventosItem.setFechaHrEdicion(ev.getFechaHrEdicion());
                            eventosItem.setIdEmpresa(ev.getIdEmpresa());
                            eventosItem.setIdEntidadRespondio(ev.getIdEntidadRespondio());
                            eventosItem.setIdEstatus(ev.getIdEstatus());
                            eventosItem.setIdFormularioEvento(ev.getIdFormularioEvento());
                            eventosItem.setIdGrupoFormulario(ev.getIdGrupoFormulario());
                            eventosItem.setIdUsuarioCapturo(ev.getIdUsuarioCapturo());
                            eventosItem.setTipoEntidadRespondio(ev.getTipoEntidadRespondio());
                            eventosItem.setLatitud(ev.getLatitud());
                            eventosItem.setLongitud(ev.getLongitud());
                            if (StringManage.getValidString(ev.getFolioMovil()).length()<=0){
                                eventosItem.setFolioMovil(CrFormularioEventoBO.generaFolioMovil(idEmpresa));
                            }else{
                                eventosItem.setFolioMovil(ev.getFolioMovil());
                            }
                            
                            WsItemCrFrmEventoSolicitud wicfes = new WsItemCrFrmEventoSolicitud();
                            CrFrmEventoSolicitudDaoImpl eventoSolicitud = new CrFrmEventoSolicitudDaoImpl(getConn());
                            CrFrmEventoSolicitud[] evSol = eventoSolicitud.findWhereIdFormularioEventoEquals(eventosItem.getIdFormularioEvento());
                            for(CrFrmEventoSolicitud eS : evSol){
                                wicfes = new WsItemCrFrmEventoSolicitud();
                                wicfes.setFechaHrCreacion(eS.getFechaHrCreacion());
                                wicfes.setIdEstadoSolicitud(eS.getIdEstadoSolicitud());
                                wicfes.setIdFormularioEvento(eS.getIdFormularioEvento());
                                wicfes.setIdFrmEventoSolicitud(eS.getIdFrmEventoSolicitud());
                                wicfes.setIdProductoCredito(eS.getIdProductoCredito());
                                wicfes.setIdUsuarioEdicion(eS.getIdUsuarioEdicion());
                                wicfes.setIdUsuarioVerificador(eS.getIdUsuarioVerificador());
                                wicfes.setSapBp(eS.getSapBp());
                                wicfes.setSapFechaAmortizacion(eS.getSapFechaAmortizacion());
                                wicfes.setSapFechaApertura(eS.getSapFechaApertura());
                                wicfes.setSapInfFechaCorte(eS.getSapInfFechaCorte());
                                wicfes.setSapInfFechaPago(eS.getSapInfFechaPago());
                                wicfes.setSapInfPlazoContrato(eS.getSapInfPlazoContrato());
                                wicfes.setSapNoContrato(eS.getSapNoContrato());
                                wicfes.setSapTablaAmortizacion(eS.getSapTablaAmortizacion());
                            }
                            eventosItem.setEventoSolicitud(wicfes);
                            
                            wigf.getWsItemFormularioEvento().add(eventosItem);
                        }
                    }
                }
                
                //Formularios
                {
                    CrFormularioDaoImpl formularioDaoImpl = new CrFormularioDaoImpl(getConn());
                    CrFormulario[] formArray = formularioDaoImpl.findWhereIdGrupoFormularioEquals(item.getIdGrupoFormulario());
                    for(CrFormulario form : formArray){
                        if(form.getIdEstatus() == 1){
                            WsItemFormulario itemForm = new WsItemFormulario();
                        
                            itemForm.setDescripcion(form.getDescripcion());
                            itemForm.setFechaHrCreacion(form.getFechaHrCreacion());
                            itemForm.setFechaHrUltimaEdicion(form.getFechaHrUltimaEdicion());
                            itemForm.setIdEmpresa(form.getIdEmpresa());
                            itemForm.setIdEstatus(form.getIdEstatus());
                            itemForm.setIdFormulario(form.getIdFormulario());
                            itemForm.setIdGrupoFormulario(form.getIdGrupoFormulario());
                            itemForm.setNombre(form.getNombre());
                            itemForm.setOrdenGrupo(form.getOrdenGrupo());
                            
                            //Formulario campo
                            {
                                CrFormularioCampoDaoImpl formularioCampoDaoImpl = new CrFormularioCampoDaoImpl(getConn());
                                CrFormularioCampo[] camposArray = formularioCampoDaoImpl.findWhereIdFormularioEquals(itemForm.getIdFormulario());
                                for(CrFormularioCampo campo : camposArray){
                                    if(campo.getIdEstatus() == 1){
                                        WsItemFormularioCampo wifc = new WsItemFormularioCampo();

                                        wifc.setDescripcion(campo.getDescripcion());
                                        wifc.setEtiqueta(campo.getEtiqueta());
                                        wifc.setIdEmpresa(campo.getIdEmpresa());
                                        wifc.setIdEstatus(campo.getIdEstatus());
                                        wifc.setIdFormulario(campo.getIdFormulario());
                                        wifc.setIdFormularioCampo(campo.getIdFormularioCampo());
                                        wifc.setIdFormularioValidacion(campo.getIdFormularioValidacion());
                                        wifc.setIdTipoCampo(campo.getIdTipoCampo());
                                        wifc.setIsRequerido(campo.getIsRequerido());
                                        wifc.setNoSeccion(campo.getNoSeccion());
                                        wifc.setOpciones(campo.getOpciones());
                                        wifc.setOrdenFormulario(campo.getOrdenFormulario());
                                        wifc.setValorDefecto(campo.getValorDefecto());
                                        wifc.setValorSugerencia(campo.getValorSugerencia());
                                        wifc.setVariableFormula(campo.getVariableFormula());

                                        itemForm.getWsItemFormularioCampo().add(wifc);
                                    }
                                }
                            }

                            //Formulario respuesta
                            {
                                CrFormularioRespuestaDaoImpl respuestaDaoImpl = new CrFormularioRespuestaDaoImpl(getConn());
                                CrFormularioRespuesta[] respuestasArray = respuestaDaoImpl.findWhereIdFormularioEquals(itemForm.getIdFormulario());
                                for(CrFormularioRespuesta respuesta : respuestasArray){
                                    if(respuesta.getIdEstatus() == 1){
                                        WsItemFormularioRespuesta wifr = new WsItemFormularioRespuesta();

                                        wifr.setDescripcion(respuesta.getDescripcion());
                                        wifr.setIdEmpresa(respuesta.getIdEmpresa());
                                        wifr.setIdEstatus(respuesta.getIdEstatus());
                                        wifr.setIdFormulario(respuesta.getIdFormulario());
                                        wifr.setIdFormularioCampo(respuesta.getIdFormularioCampo());
                                        wifr.setIdFormularioEvento(respuesta.getIdFormularioEvento());
                                        wifr.setIdFormularioRespuesta(respuesta.getIdFormularioRespuesta());
                                        wifr.setValor(respuesta.getValor());

                                        itemForm.getWsItemFormularioRespuesta().add(wifr);
                                    }
                                }
                            }
                            wigf.getListaWsItemFormulario().add(itemForm); //getWsItemFormulario().add(itemForm);
                        }
                    }
                }
                listaGrupoForm.add(wigf);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return listaGrupoForm;
    }
    
    private List<WsItemFormularioValidacion> getListaValidacion(int idEmpresa, int idUsuario){
        List<WsItemFormularioValidacion> listaValidacion = new ArrayList<WsItemFormularioValidacion>();
        CrFormularioValidacionBO validacionBO = new CrFormularioValidacionBO(getConn());
        try{
            CrFormularioValidacion[] validacionArray = validacionBO.findCrFormularioValidacions(-1, idEmpresa, 0, 0, " AND id_estatus=1 ");
            for(CrFormularioValidacion item : validacionArray){
                WsItemFormularioValidacion wifv = new WsItemFormularioValidacion();
                
                wifv.setDescripcion(item.getDescripcion());
                wifv.setIdEmpresa(item.getIdEmpresa());
                wifv.setIdEstatus(item.getIdEstatus());
                wifv.setIdFormularioValidacion(item.getIdFormularioValidacion());
                wifv.setIsCreadoSistema(item.getIsCreadoSistema());
                wifv.setNombre(item.getNombre());
                wifv.setRegexJava(item.getRegexJava());
                wifv.setRegexLenguajeExt(item.getRegexLenguajeExt());
                
                listaValidacion.add(wifv);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return listaValidacion;
    }
    
    private List<WsItemTipoCampo> getListaTipoCampo(int idEmpresa, int idUsuario){
        List<WsItemTipoCampo> listadoTiposCampo = new ArrayList<WsItemTipoCampo>();
        
        CrTipoCampoBO campoBO = new CrTipoCampoBO(getConn());
        try{
            CrTipoCampo[] camposArray = campoBO.findCrTipoCampos(-1, idEmpresa, 0, 0, " OR id_empresa=0 AND id_estatus=1 ");
            for(CrTipoCampo item : camposArray){
                WsItemTipoCampo witc = new WsItemTipoCampo();
                witc.setDescripcion(item.getDescripcion());
                witc.setIdEmpresa(item.getIdEmpresa());
                witc.setIdEstatus(item.getIdEstatus());
                witc.setIdTipoCampo(item.getIdTipoCampo());
                witc.setIsCreadoSistema(item.getIsCreadoSistema());
                witc.setNombre(item.getNombre());
                listadoTiposCampo.add(witc);
            }
        }catch(Exception e){
            e.printStackTrace();;
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return listadoTiposCampo;
    }
    
}
