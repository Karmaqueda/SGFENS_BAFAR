/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.sms.bo;

import com.google.gson.Gson;
import com.tsp.sct.bo.ParametrosBO;
import com.tsp.sct.bo.SmsDispositivoMovilBO;
import com.tsp.sct.bo.SmsEnvioDetalleBO;
import com.tsp.sct.dao.dto.SmsDispositivoMovil;
import com.tsp.sct.dao.dto.SmsEnvioDetalle;
import com.tsp.sct.dao.dto.SmsEnvioLote;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.dao.jdbc.SmsDispositivoMovilDaoImpl;
import com.tsp.sct.dao.jdbc.SmsEnvioDetalleDaoImpl;
import com.tsp.sct.dao.jdbc.SmsEnvioLoteDaoImpl;
import com.tsp.sct.util.StringManage;
import com.tsp.sgfens.ws.response.WSResponse;
import com.tsp.sgfens.ws.sms.request.*;
import com.tsp.sgfens.ws.sms.response.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ISCesar
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
    
    public ConsultaWsBO(){}
    
    public ConsultaWsBO(Connection conn){
        this.conn = conn;
    }
    
    /**
     * Verifica si los datos de acceso del empleado son válidos
     * @param loginSmsMovilRequestJSON
     * @return AccionCatalogoResponse Respuesta compuesta por objeto complejo
     */
    public LoginSmsMovilResponse loginByDispositivoMovilSMS(String loginSmsMovilRequestJSON) {
        LoginSmsMovilResponse response;
        
        LoginSmsMovilRequest smsLoginDtoRequest;
        try{
            //Transformamos de formato JSON a objeto
            smsLoginDtoRequest = gson.fromJson(loginSmsMovilRequestJSON, LoginSmsMovilRequest.class);
            
            response = this.loginByDispositivoMovilSMS(smsLoginDtoRequest);
        }catch(Exception ex){
            response = new LoginSmsMovilResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }
    
    /**
     * Verifica si los datos de acceso del empleado son válidos
     * @param loginSmsMovilRequest
     * @return AccionCatalogoResponse Respuesta compuesta por objeto complejo
     */
    public LoginSmsMovilResponse loginByDispositivoMovilSMS(LoginSmsMovilRequest loginSmsMovilRequest) {
        LoginSmsMovilResponse response = new LoginSmsMovilResponse();
        try {
            //Consultamos
            SmsDispositivoMovilBO smsDispositivoMovilBO = new SmsDispositivoMovilBO(getConn());
            if (smsDispositivoMovilBO.login(loginSmsMovilRequest.getUsuario(), loginSmsMovilRequest.getPassword())) {
                if (smsDispositivoMovilBO.getSmsDispositivoMovil().getImei().equals(loginSmsMovilRequest.getDispositivoIMEI())){
                    
                    SmsDispositivoMovil smsDispositivoMovilDto = smsDispositivoMovilBO.getSmsDispositivoMovil();
                    if (smsDispositivoMovilDto.getIdEstatus()==SmsDispositivoMovilBO.ID_ESTATUS_ACTIVO 
                            || smsDispositivoMovilDto.getIdEstatus()==SmsDispositivoMovilBO.ID_ESTATUS_DESCONECTADO){

                        WsItemDispositivoMovilSms wsItemDispositivoMovil= new WsItemDispositivoMovilSms();
                        wsItemDispositivoMovil.setIdSmsDispositivoMovil(smsDispositivoMovilDto.getIdSmsDispositivoMovil());
                        wsItemDispositivoMovil.setIdEstatus(smsDispositivoMovilDto.getIdEstatus());
                        wsItemDispositivoMovil.setAlias(smsDispositivoMovilDto.getAlias());
                        wsItemDispositivoMovil.setNumeroCelular(smsDispositivoMovilDto.getNumeroCelular());
                        wsItemDispositivoMovil.setImei(smsDispositivoMovilDto.getImei());
                        wsItemDispositivoMovil.setMarca(smsDispositivoMovilDto.getMarca());
                        wsItemDispositivoMovil.setModelo(smsDispositivoMovilDto.getModelo());
                        wsItemDispositivoMovil.setPctPila(smsDispositivoMovilDto.getPctPila());
                        wsItemDispositivoMovil.setFechaHrUltimaCom(smsDispositivoMovilDto.getFechaHrUltimaCom());
                        wsItemDispositivoMovil.setFechaHrUltimoEnvio(smsDispositivoMovilDto.getFechaHrUltimoEnvio());
                        response.setWsItemDispositivoMovil(wsItemDispositivoMovil);

                        response.setError(false);
                        
                        //registramos ubicacion y pila
                        try{new InsertaActualizaWsBO(getConn()).actualizarInfoConexionDispositivoMovilSMS(loginSmsMovilRequest);}catch(Exception ex){}
                    }else{
                        response.setError(true);
                        response.setNumError(901);
                        response.setMsgError("El dispositivo esta marcado como Desactivado por Consola.");
                    }
                }else{
                    response.setError(true);
                    response.setNumError(901);
                    response.setMsgError("El dispositivo no corresponde en datos registrados. IMEI distinto.");
                }
            } else {
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("Datos de acceso inválidos.");
            }
        } catch (Exception e) {
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error al verificar login del Dispositivo Movil SMS: " + e.toString());
            e.printStackTrace();
        }finally{
            try{ if (this.conn!=null) ResourceManager.close(this.conn); }catch(Exception ex){}
        }
         
        return response;
    }
    
    
    /**
     * Consulta los SMS que estan activos y pendientes por enviar.
     * @param loginSmsMovilRequestJSON
     * @return Objeto DTO con respuesta
     */
    public ConsultaSmsPorEnviarResponse getSmsPorEnviar(String loginSmsMovilRequestJSON) {
        ConsultaSmsPorEnviarResponse response;
        
        try{
            //Transformamos de formato JSON a objeto
            LoginSmsMovilRequest smsLoginDtoRequest = gson.fromJson(loginSmsMovilRequestJSON, LoginSmsMovilRequest.class);
            
            response = this.getSmsPorEnviar(smsLoginDtoRequest);
        }catch(Exception ex){
            response = new ConsultaSmsPorEnviarResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }
    
    /**
     * Consulta los SMS que estan activos y pendientes por enviar.
     * @param loginSmsMovilRequest
     * @return Objeto DTO con respuesta
     */
    public ConsultaSmsPorEnviarResponse getSmsPorEnviar(LoginSmsMovilRequest loginSmsMovilRequest) {
        ConsultaSmsPorEnviarResponse response = new ConsultaSmsPorEnviarResponse();
        try {
            //Consultamos
            SmsDispositivoMovilBO smsDispositivoMovilBO = new SmsDispositivoMovilBO(getConn());
            if (smsDispositivoMovilBO.login(loginSmsMovilRequest.getUsuario(), loginSmsMovilRequest.getPassword())) {
                    
                    response.setListaSms(
                            getListaSmsEnvioDetalles(smsDispositivoMovilBO.getSmsDispositivoMovil(), 
                                    " AND id_estatus=1 AND enviado=0 "));
                    response.setError(false);
                        
                    //registramos ubicacion y pila
                    try{new InsertaActualizaWsBO(getConn()).actualizarInfoConexionDispositivoMovilSMS(loginSmsMovilRequest);}catch(Exception ex){}
            } else {
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("Credenciales de acceso inválidas.");
            }
        } catch (Exception e) {
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado en Servidor, WS SMS: " + e.toString());
            e.printStackTrace();
        }finally{
            try{ if (this.conn!=null) ResourceManager.close(this.conn); }catch(Exception ex){}
        }
         
        return response;
    }
    
    private List<WsItemSmsEnvioDetalle> getListaSmsEnvioDetalles(SmsDispositivoMovil smsDispositivoMovil, String filtroBusqueda){
        List<WsItemSmsEnvioDetalle> listaWsItemSmsEnvioDetalles = new ArrayList<WsItemSmsEnvioDetalle>();
        
        SmsEnvioDetalleBO smsEnvioDetalleBo = new SmsEnvioDetalleBO(getConn());
        try{
            SmsEnvioDetalle[] smsEnvioDetalleLoteUnico = new SmsEnvioDetalle[0];
            //primero solo buscamos un registro, para obtener de ahi UN SOLO LOTE
            if (smsDispositivoMovil!=null){
                // intentamos buscar solo asignados al dispositivo especifico
                smsEnvioDetalleLoteUnico = smsEnvioDetalleBo.findSmsEnvioDetalles(-1, 0, 0, 1, 
                     filtroBusqueda + " AND (SELECT COUNT(id_sms_envio_lote) FROM sms_envio_lote b WHERE b.id_sms_envio_lote = sms_envio_detalle.id_sms_envio_lote AND b.id_sms_dispositivo_movil=" +smsDispositivoMovil.getIdSmsDispositivoMovil() + ""
                             + " AND (b.fecha_hr_programa_envio IS NULL OR fecha_hr_programa_envio<=NOW() ) )>0");
            }
            //si no se encontro ninguno asignado especificamente a ese dispositivo
            // volvemos a buscar pero ahora en general
            if (smsEnvioDetalleLoteUnico.length<=0){
                smsEnvioDetalleLoteUnico = smsEnvioDetalleBo.findSmsEnvioDetalles(-1, 0, 0, 1, 
                        filtroBusqueda + " AND (SELECT COUNT(id_sms_envio_lote) FROM sms_envio_lote b WHERE b.id_sms_envio_lote = sms_envio_detalle.id_sms_envio_lote "
                             + " AND (b.fecha_hr_programa_envio IS NULL OR fecha_hr_programa_envio<=NOW() ) )>0");
            }
            
            if (smsEnvioDetalleLoteUnico.length>0){
                ParametrosBO parametrosBO = new ParametrosBO(getConn());
                int maxSmsPorEnvio = parametrosBO.getParametroInt("app.sms.maxSmsPorEnvio");
                
                SmsEnvioDetalle[] smsEnvioDetalles = smsEnvioDetalleBo.findSmsEnvioDetalles(-1, 0, 0, maxSmsPorEnvio, 
                        " AND id_sms_envio_lote=" + smsEnvioDetalleLoteUnico[0].getIdSmsEnvioLote() + filtroBusqueda);
                if (smsEnvioDetalles.length>0){
                    SmsEnvioLote smsEnvioLote = null;
                    SmsEnvioLoteDaoImpl smsEnvioLoteDao = new SmsEnvioLoteDaoImpl(getConn());
                    SmsEnvioDetalleDaoImpl smsEnvioDetalleDao = new SmsEnvioDetalleDaoImpl(getConn());
                    for (SmsEnvioDetalle item : smsEnvioDetalles){
                        try{
                            if (item.getIdEstatus()!=2){ //solo si el registro esta activo
                                WsItemSmsEnvioDetalle wsItemSmsEnvioDetalle = new WsItemSmsEnvioDetalle();
                                wsItemSmsEnvioDetalle.setDestIdCliente(item.getDestIdCliente());
                                wsItemSmsEnvioDetalle.setDestIdEmpleado(item.getDestIdEmpleado());
                                wsItemSmsEnvioDetalle.setDestIdEmpresa(item.getDestIdEmpresa());
                                wsItemSmsEnvioDetalle.setDestIdProspecto(item.getDestIdProspecto());
                                wsItemSmsEnvioDetalle.setDestIdSmsAgendaDest(item.getDestIdSmsAgendaDest());
                                wsItemSmsEnvioDetalle.setEnviado(item.getEnviado());
                                wsItemSmsEnvioDetalle.setFechaHrCreacion(item.getFechaHrCreacion());
                                wsItemSmsEnvioDetalle.setFechaHrEnvio(item.getFechaHrEnvio());
                                wsItemSmsEnvioDetalle.setHeredarMensajeLote(item.getHeredarMensajeLote());
                                wsItemSmsEnvioDetalle.setIdEmpresa(item.getIdEmpresa());
                                wsItemSmsEnvioDetalle.setIdEstatus(item.getIdEstatus());
                                wsItemSmsEnvioDetalle.setIdSmsEnvioDetalle(item.getIdSmsEnvioDetalle());
                                wsItemSmsEnvioDetalle.setIdSmsEnvioLote(item.getIdSmsEnvioLote());
                                wsItemSmsEnvioDetalle.setIntentosFallidos(item.getIntentosFallidos());

                                wsItemSmsEnvioDetalle.setNumeroCelular(item.getNumeroCelular());
                                wsItemSmsEnvioDetalle.setNumPartesSms(item.getNumPartesSms());
                                wsItemSmsEnvioDetalle.setAsunto(item.getAsunto());
                                wsItemSmsEnvioDetalle.setMensaje(item.getMensaje());  

                                //si se hereda el mensaje del lote
                                if (item.getHeredarMensajeLote()==1 &&
                                        StringManage.getValidString(item.getMensaje()).length()<=0){
                                    //verificamos si podemos reciclar el lote buscado anteriormente
                                    // si no tenemos anterior o es diferente al requerido, lo buscamos
                                    if (smsEnvioLote==null 
                                            || smsEnvioLote.getIdSmsEnvioLote()!=item.getIdSmsEnvioLote()){
                                        smsEnvioLote = smsEnvioLoteDao.findByPrimaryKey(item.getIdSmsEnvioLote());
                                    }

                                    if (smsEnvioLote!=null)
                                        wsItemSmsEnvioDetalle.setMensaje(smsEnvioLote.getMensaje());
                                }

                                //actualizamos registros en bd local
                                //  Este mensaje lo marcamos como Procesando
                                item.setEnviado(SmsEnvioDetalleBO.ENV_PROCESANDO);
                                smsEnvioDetalleDao.update(item.createPk(), item);
                                
                                //agregamos a lista de respuesta
                                listaWsItemSmsEnvioDetalles.add(wsItemSmsEnvioDetalle);
                            }
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                    
                    if (smsDispositivoMovil!=null){
                        //Actualizamos Lote actual asignado a movil SMS
                        if (smsEnvioLote!=null){
                            smsDispositivoMovil.setIdSmsEnvioLoteActual(smsEnvioLote.getIdSmsEnvioLote());
                        }else{
                            smsDispositivoMovil.setIdSmsEnvioLoteActual(0);
                        }
                        smsDispositivoMovil.setIsOcupado(1);
                        new SmsDispositivoMovilDaoImpl(getConn()).update(smsDispositivoMovil.createPk(), smsDispositivoMovil);
                    }
                }
                
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return listaWsItemSmsEnvioDetalles;
    }
    
}
