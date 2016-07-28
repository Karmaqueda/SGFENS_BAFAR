/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.sms.bo;

import com.google.gson.Gson;
import com.tsp.sct.bo.SmsDispositivoMovilBO;
import com.tsp.sct.bo.SmsEnvioDetalleBO;
import com.tsp.sct.bo.SmsEnvioLoteBO;
import com.tsp.sct.bo.SmsPlantillaBO;
import com.tsp.sct.dao.dto.SmsDispositivoMovil;
import com.tsp.sct.dao.dto.SmsEnvioDetalle;
import com.tsp.sct.dao.dto.SmsEnvioError;
import com.tsp.sct.dao.dto.SmsEnvioLote;
import com.tsp.sct.dao.dto.SmsPlantilla;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.dao.jdbc.SmsDispositivoMovilDaoImpl;
import com.tsp.sct.dao.jdbc.SmsEnvioDetalleDaoImpl;
import com.tsp.sct.dao.jdbc.SmsEnvioErrorDaoImpl;
import com.tsp.sct.dao.jdbc.SmsEnvioLoteDaoImpl;
import com.tsp.sct.util.GenericValidator;
import com.tsp.sct.util.StringManage;
import com.tsp.sgfens.sesion.SmsEnvioDestinatariosSesion;
import com.tsp.sgfens.sesion.SmsEnvioSesion;
import com.tsp.sgfens.ws.response.WSResponse;
import com.tsp.sgfens.ws.response.WSResponseInsert;
import com.tsp.sgfens.ws.sms.request.LoginSmsMovilRequest;
import com.tsp.sgfens.ws.sms.request.RegistroSmsProcesoRequest;
import com.tsp.sgfens.ws.sms.request.WsItemEnvioError;
import com.tsp.sgfens.ws.sms.request.WsItemSmsProcesado;
import com.tsp.sgfens.ws.sms.response.RegistroSmsProcesoResponse;
import com.tsp.sgfens.ws.sms.response.WsItemSmsEnvioDetalle;
import com.tsp.sgfens.ws.sms.response.WsItemSmsRegistro;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ISCesar
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
     * Actualiza la ubicacion de un empleado segun los datos otorgados
     * a tráves de un objeto 
     * @param loginSmsMovilRequestJSON String Datos del empleado (móvil) en formato JSON (clase EmpleadoDtoRequest)
     * @return WSResponse Respuesta compuesta por objeto complejo con respuesta básica de exito
     */
    public WSResponse actualizarInfoConexionDispositivoMovilSMS(String loginSmsMovilRequestJSON) {
        WSResponse response;
                
        LoginSmsMovilRequest empleadoDtoRequest;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(loginSmsMovilRequestJSON, LoginSmsMovilRequest.class);
            
            response = this.actualizarInfoConexionDispositivoMovilSMS(empleadoDtoRequest);
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
     * Actualiza la ubicacion de un dispositivo SMS segun los datos otorgados
     * @param loginSmsMovilRequest LoginSmsMovilRequest Datos del dispositivo movil sms
     * @return WSResponse Respuesta compuesta por objeto complejo con respuesta básica de exito
     */
    public WSResponse actualizarInfoConexionDispositivoMovilSMS(LoginSmsMovilRequest loginSmsMovilRequest) {
        WSResponse response = new WSResponse();
         
        try {
            SmsDispositivoMovilBO smsDispositivoMovilBO = new SmsDispositivoMovilBO(getConn());
            if (smsDispositivoMovilBO.login(loginSmsMovilRequest.getUsuario(), loginSmsMovilRequest.getPassword())) {
                try{
                    SmsDispositivoMovil smsDispositivoMovilDto = smsDispositivoMovilBO.getSmsDispositivoMovil();
                    //smsDispositivoMovilDto.setLatitud(loginSmsMovilRequest.getUbicacionLatitud());
                    //smsDispositivoMovilDto.setLongitud(loginSmsMovilRequest.getUbicacionLongitud());
                    smsDispositivoMovilDto.setPctPila(loginSmsMovilRequest.getPorcentajeBateria());
                    smsDispositivoMovilDto.setFechaHrUltimaCom(new Date());
                    if (smsDispositivoMovilDto.getIdEstatus()==3) //si esta como desconectado, lo cambiamos a Activo
                        smsDispositivoMovilDto.setIdEstatus(1);
                    
                    new SmsDispositivoMovilDaoImpl(getConn()).update(smsDispositivoMovilDto.createPk(),smsDispositivoMovilDto);
                    
                }catch(Exception ex){
                    response.setNumError(902);
                    response.setMsgError("No se pudo registrar la información de conexión del dispositivo SMS ");
                    response.setError(true);
                    ex.printStackTrace();
                    return response;
                }                
            } else {
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Dispositivo SMS son inválidos.");
            }
        } catch (Exception e) {
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error al actualizar ubicacion del empleado. " + e.toString());
        }finally{
            try{ if (this.conn!=null) ResourceManager.close(this.conn); }catch(Exception ex){}
        }
         
        return response;
    }
    
    public RegistroSmsProcesoResponse registrarSmsProcesados(String loginSmsMovilRequestJSON, String registroSmsProcesoRequestJSON) {
        RegistroSmsProcesoResponse response;
                
        LoginSmsMovilRequest empleadoDtoRequest;
        RegistroSmsProcesoRequest registroSmsProcesoRequest;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(loginSmsMovilRequestJSON, LoginSmsMovilRequest.class);
            registroSmsProcesoRequest = gson.fromJson(registroSmsProcesoRequestJSON, RegistroSmsProcesoRequest.class);
            
            response = this.registrarSmsProcesados(empleadoDtoRequest, registroSmsProcesoRequest);
        }catch(Exception ex){
            response = new RegistroSmsProcesoResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }
    
    public RegistroSmsProcesoResponse registrarSmsProcesados(LoginSmsMovilRequest loginSmsMovilRequest, RegistroSmsProcesoRequest registroSmsProcesoRequest) {
        RegistroSmsProcesoResponse response = new RegistroSmsProcesoResponse();
        
        try{
            SmsDispositivoMovilBO smsDispositivoMovilBO = new SmsDispositivoMovilBO(getConn());
            SmsDispositivoMovil smsDispositivoMovil;
            if (smsDispositivoMovilBO.login(loginSmsMovilRequest.getUsuario(), loginSmsMovilRequest.getPassword())) {
                smsDispositivoMovil = smsDispositivoMovilBO.getSmsDispositivoMovil();
            }else{
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Dispositivo SMS son inválidos.");
                return response;
            }
            
            //registramos proceso de los SMS
            Date fechaHrEnvioMasReciente = smsDispositivoMovil.getFechaHrUltimoEnvio();
            SmsEnvioDetalleDaoImpl smsEnvioDetalleDao = new SmsEnvioDetalleDaoImpl(getConn());
            SmsEnvioErrorDaoImpl smsEnvioErrorDaoImpl = new SmsEnvioErrorDaoImpl(getConn());
            SmsEnvioLoteBO smsEnvioLoteBO = new SmsEnvioLoteBO(getConn());
            int idLoteProgramadoEnviado = 0;
            int idPlantillaUsada = 0;
            int cantidadDetallesEnviadosExito = 0;
            for (WsItemSmsProcesado itemSmsProcesado : registroSmsProcesoRequest.getListaWsItemSmsProcesados()){
                WsItemSmsRegistro wsItemSmsRegistro = new WsItemSmsRegistro(); //item response
                wsItemSmsRegistro.setIdSmsEnvioDetalle(itemSmsProcesado.getWsItemSmsEnvioDetalle().getIdSmsEnvioDetalle());
                
                try{
                    //simplificamos objetos de request por item
                    WsItemSmsEnvioDetalle wsItemSmsEnvioDetalle = itemSmsProcesado.getWsItemSmsEnvioDetalle();
                    List<WsItemEnvioError> listaWsItemEnvioErrors = itemSmsProcesado.getWsItemEnvioError();

                    //buscamos en base de datos local el registro del mensaje
                    SmsEnvioDetalle smsEnvioDetalleDto = smsEnvioDetalleDao.findByPrimaryKey(wsItemSmsEnvioDetalle.getIdSmsEnvioDetalle());

                    if (smsEnvioDetalleDto == null){
                        //registro no encontrado
                        continue;
                    }

                    //asignamos datos de enviado segun lo reportado
                    if (itemSmsProcesado.isEnviado()){
                    //Si fue enviado exitosamente
                        smsEnvioDetalleDto.setFechaHrEnvio(wsItemSmsEnvioDetalle.getFechaHrEnvio());
                        smsEnvioDetalleDto.setEnviado(SmsEnvioDetalleBO.ENV_ENVIADO);
                        smsEnvioDetalleDto.setIdSmsDispositivoEjecuto(smsDispositivoMovil.getIdSmsDispositivoMovil());

                        //revisamos si la fecha de envio de este mensaje fue el mas reciente, para asignarlo al dispositivo sms
                        if (fechaHrEnvioMasReciente==null){
                            fechaHrEnvioMasReciente = wsItemSmsEnvioDetalle.getFechaHrEnvio();
                        }else if (wsItemSmsEnvioDetalle.getFechaHrEnvio().after(fechaHrEnvioMasReciente)){
                            fechaHrEnvioMasReciente = wsItemSmsEnvioDetalle.getFechaHrEnvio();
                        }
                        
                        SmsEnvioLote smsEnvioLote = smsEnvioLoteBO.findSmsEnvioLotebyId(smsEnvioDetalleDto.getIdSmsEnvioLote());
                        if (smsEnvioLote!=null){
                            if (smsEnvioLote.getIdSmsPlantilla()>0)
                                idPlantillaUsada = smsEnvioLote.getIdSmsPlantilla();
                            if (smsEnvioLote.getFechaHrProgramaEnvio()!=null){
                                idLoteProgramadoEnviado = smsEnvioLote.getIdSmsEnvioLote();
                            }
                        }
                        cantidadDetallesEnviadosExito++;
                    }else{
                    //Si NO fue enviado
                        smsEnvioDetalleDto.setIntentosFallidos(smsEnvioDetalleDto.getIntentosFallidos() + 1);
                        //si esta como PROCESANDO, entonces lo modificamos a NO ENVIADO
                        if (smsEnvioDetalleDto.getEnviado()==SmsEnvioDetalleBO.ENV_PROCESANDO)
                            smsEnvioDetalleDto.setEnviado(SmsEnvioDetalleBO.ENV_NO_ENVIADO);
                    }
                    //actualizamos registro de mensaje en BD
                    smsEnvioDetalleDao.update(smsEnvioDetalleDto.createPk(), smsEnvioDetalleDto);

                    //ahora si hubo errores/advertencias los almacenamos, relacionados al mismo mensaje
                    if (listaWsItemEnvioErrors.size()>0){
                        for (WsItemEnvioError itemError : listaWsItemEnvioErrors){
                            SmsEnvioError smsEnvioErrorDto = new SmsEnvioError();
                            smsEnvioErrorDto.setIdSmsEnvioDetalle(smsEnvioDetalleDto.getIdSmsEnvioDetalle());
                            smsEnvioErrorDto.setDescError(itemError.getDescError());
                            smsEnvioErrorDto.setFechaHrError(itemError.getFechaHrError());
                            smsEnvioErrorDto.setIdSmsDispositivo(smsDispositivoMovil.getIdSmsDispositivoMovil());
                            smsEnvioErrorDto.setIdEmpresa(smsEnvioDetalleDto.getIdEmpresa());
                            smsEnvioErrorDto.setIdEstatus(1);
                            //insertamos registro de error al intentar enviar mensaje
                            smsEnvioErrorDaoImpl.insert(smsEnvioErrorDto);
                        }
                    }
                    
                    wsItemSmsRegistro.setRegistrado(true);
                    wsItemSmsRegistro.setErrorRegistro("");
                    
                }catch(Exception ex){
                    wsItemSmsRegistro.setRegistrado(false);
                    wsItemSmsRegistro.setErrorRegistro(ex.toString());
                }
                
                //agregamos a listado de respuesta
                response.getListaItemSmsRegistros().add(wsItemSmsRegistro);
                
            }
            
            //Actualizamos informacion de Dispositivo SMS, lo marcamos como desocupado
            smsDispositivoMovil.setIdSmsEnvioLoteActual(0);
            smsDispositivoMovil.setIsOcupado(0);
            if (fechaHrEnvioMasReciente!=null)
                smsDispositivoMovil.setFechaHrUltimoEnvio(fechaHrEnvioMasReciente);
            new SmsDispositivoMovilDaoImpl(getConn()).update(smsDispositivoMovil.createPk(), smsDispositivoMovil);
            
            //enviamos notificacion en caso de ser un envio programado
            try{
                if (idLoteProgramadoEnviado>0)
                    smsEnvioLoteBO.enviarCorreoLoteProgramadoEnviado(idLoteProgramadoEnviado, idPlantillaUsada, cantidadDetallesEnviadosExito);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
            //registramos ubicacion y pila
            try{ actualizarInfoConexionDispositivoMovilSMS(loginSmsMovilRequest);}catch(Exception ex){}
        }catch (Exception e) {
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al registrar SMS Procesados. " + e.toString());
        }finally{
            try{ if (this.conn!=null) ResourceManager.close(this.conn); }catch(Exception ex){}
        }
         
        return response;

    }
    
    public WSResponseInsert crearSms(boolean demo, String numeroCelularDestinatario, String mensaje, String asunto, String nombreDestinatario){
        WSResponseInsert response = new WSResponseInsert();
        
        if (demo && StringManage.getValidString(mensaje).length()>35){
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("El mensaje excede del tamaño permitido para demostración.");
            return response;
        }
        GenericValidator gc = new GenericValidator();
        String errorValidacion = "";
        if (!gc.isCelularMexico(numeroCelularDestinatario))
            errorValidacion += "Numero Celular incorrecto, debe tener 10 digitos sin guiones ni espacios.";
        if (!gc.isValidString(mensaje,1,480))
            errorValidacion += "Mensaje incorrecto, debe tener al menos un caracter, máximo 480.";
            
        if (StringManage.getValidString(errorValidacion).length()>0){
            response.setError(true);
            response.setNumError(901);
            response.setMsgError(errorValidacion);
            return response;
        }
        
        asunto = !demo ? asunto : "Demo Gratuito Sms";
        String contenidoSMS = demo ? "MovilPyme: " + mensaje + ". http://goo.gl/9DUzD1" : StringManage.getValidString(mensaje);
        
        SmsEnvioSesion smsEnvioSesion = new SmsEnvioSesion();
        smsEnvioSesion.setConn(conn);
        smsEnvioSesion.setAsunto(asunto);
        smsEnvioSesion.setContenido(contenidoSMS);
        smsEnvioSesion.setEnvioInmediato(true);
        smsEnvioSesion.setFechaHoraEnvioProgramado(null);
        SmsEnvioDestinatariosSesion destinatariosSesion = new SmsEnvioDestinatariosSesion();
            destinatariosSesion.setNombre(nombreDestinatario);
            destinatariosSesion.setNumCelular(numeroCelularDestinatario);
        smsEnvioSesion.getListaDestinatarios().add(destinatariosSesion);

        int creditosRequeridos = smsEnvioSesion.calculaCreditosSMS();

        SmsEnvioLote smsEnvioLoteResultado = null;
        {
            SmsEnvioLoteDaoImpl smsEnvioLoteDao = new SmsEnvioLoteDaoImpl(conn);
            SmsEnvioDetalleDaoImpl smsEnvioDetalleDao = new SmsEnvioDetalleDaoImpl(conn);

            smsEnvioLoteResultado = new SmsEnvioLote();
            smsEnvioLoteResultado.setIdEmpresa(0);
            smsEnvioLoteResultado.setIdEstatus(1);
            smsEnvioLoteResultado.setIdSmsPlantilla(smsEnvioSesion.getIdPlantilla());
            smsEnvioLoteResultado.setFechaHrCaptura(new Date());
            //smsEnvioLoteResultado.setFechaHrProgramaEnvio(null);
            smsEnvioLoteResultado.setCantidadDestinatarios(smsEnvioSesion.getListaDestinatarios().size());
            smsEnvioLoteResultado.setCantidadCreditosSms(creditosRequeridos);
            smsEnvioLoteResultado.setEnvioInmediato(1);
            smsEnvioLoteResultado.setIsSmsSistema(1);//Si es auto-creado (ventas, cobranza, factura, etc.)
            smsEnvioLoteResultado.setIdUsuarioPretoriano(0);
            smsEnvioLoteResultado.setIdUsuarioVentas(0);//Solo para sistema de Ventas
            // se usara un Dispositivo Movil especifico si la Empresa matriz tiene uno configurado
            smsEnvioLoteResultado.setIdSmsDispositivoMovil(0);
            smsEnvioLoteResultado.setAsunto(smsEnvioSesion.getAsunto());
            smsEnvioLoteResultado.setMensaje(smsEnvioSesion.creaContenidoSMS());//contenidoSMS);

            try{
                smsEnvioLoteDao.insert(smsEnvioLoteResultado);

                //Después creamos los Detalles (mensajes especificos para cada destinatario) del lote
                try{
                    SmsEnvioDetalle smsEnvioDetalleDto = new SmsEnvioDetalle();
                    smsEnvioDetalleDto.setIdSmsEnvioLote(smsEnvioLoteResultado.getIdSmsEnvioLote());
                    smsEnvioDetalleDto.setFechaHrCreacion(smsEnvioLoteResultado.getFechaHrCaptura());
                    //smsEnvioDetalleDto.setFechaHrEnvio(null);//no se ha enviado, por lo tanto se deja en null
                    smsEnvioDetalleDto.setEnviado(0);
                    smsEnvioDetalleDto.setIntentosFallidos(0);
                    smsEnvioDetalleDto.setAsunto(smsEnvioLoteResultado.getAsunto());
                    //En lugar de guardar el mismo mensaje en todos los detalles, marcamos que lo herede del Lote padre
                    //smsEnvioDetalleDto.setMensaje(smsEnvioSesion.creaContenidoSMS());
                    smsEnvioDetalleDto.setHeredarMensajeLote(1);
                    smsEnvioDetalleDto.setNumPartesSms(smsEnvioSesion.calculaPartesMensaje());
                    smsEnvioDetalleDto.setNumeroCelular(numeroCelularDestinatario);
                    //smsEnvioDetalleDto.setDestIdCliente(idCliente);
                    //smsEnvioDetalleDto.setDestIdProspecto();
                    //smsEnvioDetalleDto.setDestIdEmpleado(idEmpleado);
                    //smsEnvioDetalleDto.setDestIdEmpresa(idEmpresaPretoriano);
                    //smsEnvioDetalleDto.setDestIdSmsAgendaDest();
                    smsEnvioDetalleDto.setIdEmpresa(0);
                    smsEnvioDetalleDto.setIdEstatus(1);

                    smsEnvioDetalleDao.insert(smsEnvioDetalleDto);
                 
                    response.setIdObjetoCreado(smsEnvioLoteResultado.getIdSmsEnvioLote());
                }catch(Exception ex){
                    response.setError(true);
                    response.setNumError(902);
                    response.setMsgError("Error al insertar registro de Detalle SMS: " + ex.toString());
                    ex.printStackTrace();
                }
                
            }catch(Exception ex){
                response.setError(true);
                response.setNumError(902);
                response.setMsgError("Error al insertar registro de Lote SMS: " + ex.toString());
                ex.printStackTrace();
            }
        }
        return response;
    }
    
}
