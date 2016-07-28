/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ogro.cron;

//mesografia: http://www.programandoconcafe.com/2011/02/java-automatizacion-de-procesos-o.html
/**
 *
 * @author ISCesarMartinez
 */
import com.tsp.sct.bo.ParametrosBO;
import com.tsp.sct.bo.SmsDispositivoMovilBO;
import com.tsp.sct.bo.SmsEnvioDetalleBO;
import com.tsp.sct.bo.SmsEnvioLoteBO;
import com.tsp.sct.dao.dto.SmsDispositivoMovil;
import com.tsp.sct.dao.dto.SmsEnvioDetalle;
import com.tsp.sct.dao.dto.SmsEnvioError;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.dao.jdbc.SmsDispositivoMovilDaoImpl;
import com.tsp.sct.dao.jdbc.SmsEnvioDetalleDaoImpl;
import com.tsp.sct.dao.jdbc.SmsEnvioErrorDaoImpl;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TareaInvocar5 implements Job {

    //Metodo que se ejecutara cada cierto tiempo
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        SimpleDateFormat formato = new SimpleDateFormat("hh:mm:ss");
        System.out.println("Tarea invocada a la hora: " + formato.format(new Date()));
        System.out.println("CRON DE MODULO SMS. . . " + new Date());
        
        verificaDispositivosSinConexion();
        verificaMensajesConErrores();
        
    }
    
    private void verificaDispositivosSinConexion(){
        //Buscamos todos los registros en sms_dispositivo_movil
        // que esten activos
        SmsDispositivoMovilBO smsDispositivoMovilBO = new SmsDispositivoMovilBO(getConn());
        SmsDispositivoMovil[] smsDispositivoMoviles = smsDispositivoMovilBO.findSmsDispositivoMovils(-1, 0, 0, " AND id_estatus=1 ");
        SmsDispositivoMovilDaoImpl smsDispositivoMovilDao = new SmsDispositivoMovilDaoImpl(getConn());
        SmsEnvioDetalleBO smsEnvioDetalleBO = new SmsEnvioDetalleBO(getConn());
        SmsEnvioDetalleDaoImpl smsEnvioDetalleDao = new SmsEnvioDetalleDaoImpl(getConn());
        SmsEnvioErrorDaoImpl smsEnvioErrorDao = new SmsEnvioErrorDaoImpl(getConn());
        
        ParametrosBO parametrosBO = new ParametrosBO(getConn());
        int minutosMaximoOcupado = parametrosBO.getParametroInt("app.sms.minutosMaximoOcupado");
        int minutosMaximoDesocupado = parametrosBO.getParametroInt("app.sms.minutosMaximoDesocupado");
        
        Date ahora = new Date();
        List<SmsDispositivoMovil> listaDispositivosSinConexion = new ArrayList<SmsDispositivoMovil>();
        for (SmsDispositivoMovil movil : smsDispositivoMoviles){
            
            //Si no tiene fecha y hr de ultima conexion, es posible que el movil apenas fue dado de alta
            // por lo cual no verificamos este registro
            if (movil.getFechaHrUltimaCom()==null)
                continue;
            
            // calculamos tiempos maximos de ultima conexion
            Calendar calUltimaComMaxLimiteOcupado = Calendar.getInstance();
            calUltimaComMaxLimiteOcupado.setTime(movil.getFechaHrUltimaCom());
            calUltimaComMaxLimiteOcupado.add(Calendar.MINUTE, minutosMaximoOcupado);
            Date dateUltimaComMaxLimiteOcupado = calUltimaComMaxLimiteOcupado.getTime();
            
            Calendar calUltimaComMaxLimiteDesocupado = Calendar.getInstance();
            calUltimaComMaxLimiteDesocupado.setTime(movil.getFechaHrUltimaCom());
            calUltimaComMaxLimiteDesocupado.add(Calendar.MINUTE, minutosMaximoDesocupado);
            Date dateUltimaComMaxLimiteDesocupado = calUltimaComMaxLimiteDesocupado.getTime();
            
            if (movil.getIsOcupado()==1){
            //En suposición, el movil esta ocupado enviando mensajes, con un lote asignado
                if (ahora.compareTo(dateUltimaComMaxLimiteOcupado)>=0){
                // si excede del tiempo establecido para comunicarse a servidor estando ocupado
                    if (movil.getIdSmsEnvioLoteActual()>0){
                    //verificamos si tiene lote asignado, para marcarlo como desocupado
                        {
                        // tambien marcamos todos los mensajes que tenia asignados, como NO enviados
                            SmsEnvioDetalle[] envioDetalles = smsEnvioDetalleBO.findSmsEnvioDetalles(-1, 0, 0, 0, 
                                    " AND enviado=2 AND id_estatus=1 AND id_sms_envio_lote=" + movil.getIdSmsEnvioLoteActual());
                            for (SmsEnvioDetalle envioDetalle : envioDetalles){
                                try{
                                    envioDetalle.setEnviado(0);
                                    envioDetalle.setIntentosFallidos(envioDetalle.getIntentosFallidos()+1);
                                
                                    smsEnvioDetalleDao.update(envioDetalle.createPk(), envioDetalle);
                                    
                                    //Creamos registro de error por consola.        
                                    SmsEnvioError smsEnvioErrorDto = new SmsEnvioError();
                                    smsEnvioErrorDto.setIdSmsEnvioDetalle(envioDetalle.getIdSmsEnvioDetalle());                                    
                                    smsEnvioErrorDto.setDescError("El dispositivo SMS '" + movil.getAlias() + "' excedió de " + minutosMaximoOcupado + " minutos en reportar si envío este mensaje, se ha des-asignado, para que sea enviado desde otro dispositivo.");
                                    smsEnvioErrorDto.setFechaHrError(new Date());
                                    smsEnvioErrorDto.setIdSmsDispositivo(0);//(movil.getIdSmsDispositivoMovil());
                                    smsEnvioErrorDto.setIdEmpresa(envioDetalle.getIdEmpresa());
                                    smsEnvioErrorDto.setIdEstatus(1);
                                    
                                    smsEnvioErrorDao.insert(smsEnvioErrorDto);
                                }catch(Exception ex){
                                    ex.printStackTrace();
                                }
                                
                            }
                        }
                        movil.setIsOcupado(0);
                        movil.setIdSmsEnvioLoteActual(0);
                    }
                    movil.setIdEstatus(SmsDispositivoMovilBO.ID_ESTATUS_DESCONECTADO); // 3 = desconectado
                    try{
                        //actualizamos registro
                        smsDispositivoMovilDao.update(movil.createPk(), movil);
                        //agregamos a lista para notificacion de correo
                        listaDispositivosSinConexion.add(movil);
                    }catch(Exception ex){
                        System.out.println(" Error inesperado actualizar registro de movil SMS o enviar correo de notificacion: " + ex.toString());
                        ex.printStackTrace();
                    }
                }
            }else{
            // el movil esta desocupado
                if (ahora.compareTo(dateUltimaComMaxLimiteDesocupado)>=0){
                // si excede del tiempo establecido para comunicarse a servidor estando Desocupado
                    movil.setIdEstatus(SmsDispositivoMovilBO.ID_ESTATUS_DESCONECTADO); // 3 = desconectado
                    try{
                        //actualizamos registro
                        smsDispositivoMovilDao.update(movil.createPk(), movil);
                        //agregamos a lista para notificacion de correo
                        listaDispositivosSinConexion.add(movil);
                    }catch(Exception ex){
                        System.out.println(" Error inesperado actualizar registro de movil SMS o enviar correo de notificacion: " + ex.toString());
                        ex.printStackTrace();
                    }
                }
            }
            
        }
        
        try{
            if (listaDispositivosSinConexion.size()>0){
            // Enviamos correo de notificacion en caso de que existe al menos un dispositivo sin conexion
                smsDispositivoMovilBO.enviarCorreoDispositivosSinConexion(listaDispositivosSinConexion);
            }
        }catch(Exception ex){
            System.out.println(" Error inesperado actualizar registro de movil SMS o enviar correo de notificacion: " + ex.toString());
            ex.printStackTrace();
        }
    }

    private void verificaMensajesConErrores(){
        ParametrosBO parametrosBO = new ParametrosBO(getConn());
        int maxIntentosFallidos = parametrosBO.getParametroInt("app.sms.maxIntentosFallidos");
        
        SmsEnvioDetalleDaoImpl smsEnvioDetalleDaoImpl = new SmsEnvioDetalleDaoImpl(getConn());
        SmsEnvioDetalleBO smsEnvioDetalleBO = new SmsEnvioDetalleBO(getConn());
        SmsEnvioLoteBO smsEnvioLoteBO = new SmsEnvioLoteBO(getConn());
        SmsEnvioDetalle[] smsEnvioDetalles = smsEnvioDetalleBO.findSmsEnvioDetalles(-1, -1, 0, 0, " AND enviado IN (0) AND id_estatus=1  AND intentos_fallidos>=" + maxIntentosFallidos);
        
        if (smsEnvioDetalles.length>0){
        //si hay al menos un envio_detalle, con intentos fallidos mayor al maximo permitido
            
            // agrupamos por lote para enviar notificaciones
            List<SmsEnvioLoteBO.AgrupacionLoteDetalles> listaAgrupacionLoteDetalles = smsEnvioLoteBO.agrupaDetallesPorLote(smsEnvioDetalles);
                
            for (SmsEnvioDetalle detalle :  smsEnvioDetalles){
                try{
                    // reiniciamos contador de intentos fallidos a 1
                    // para evitar que se este enviando el mismo error tras cada pase del cron
                    detalle.setIntentosFallidos(1);
                    smsEnvioDetalleDaoImpl.update(detalle.createPk(), detalle);
                }catch(Exception ex){
                    System.out.println(" Error inesperado al actualizar registro de sms_envio_detalle: " + ex.toString());
                    ex.printStackTrace();
                }
            }
            
            try{
                // enviamos notificacion por correo
                smsEnvioLoteBO.enviarCorreoLotesConError(listaAgrupacionLoteDetalles);
            }catch(Exception ex){
                System.out.println(" Error inesperado al enviar correo de notificacion de sms_envio_detalle con intentos fallidos: " + ex.toString());
                ex.printStackTrace();
            }
        }
    }
            
    private Connection conn = null;

    public Connection getConn() {
        if (this.conn == null) {
            try {
                this.conn = ResourceManager.getConnection();
            } catch (SQLException ex) {
            }
        } else {
            try {
                if (this.conn.isClosed()) {
                    this.conn = ResourceManager.getConnection();
                }
            } catch (SQLException ex) {
            }
        }
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

}
