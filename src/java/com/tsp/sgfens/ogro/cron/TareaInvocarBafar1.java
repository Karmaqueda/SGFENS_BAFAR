/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ogro.cron;

/**
 *
 * @author ISCesarMartinez
 */
import com.google.gson.Gson;
import com.tsp.sct.bo.CrCredClienteBO;
import com.tsp.sct.bo.CrEstadoSolicitudBO;
import com.tsp.sct.bo.CrFormularioEventoBO;
import com.tsp.sct.bo.CrFrmEventoSolicitudBO;
import com.tsp.sct.bo.UsuarioBO;
import com.tsp.sct.cr.CrConsultaDispersionSapBafarResponse;
import com.tsp.sct.cr.CrOrdenPago;
import com.tsp.sct.cr.CrUtilConectaSAPBafar;
import com.tsp.sct.cr.RevisionesCr;
import com.tsp.sct.dao.dto.CrFormularioEvento;
import com.tsp.sct.dao.dto.CrFrmEventoSolicitud;
import com.tsp.sct.dao.jdbc.CrFrmEventoSolicitudDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.util.GenericMethods;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TareaInvocarBafar1 implements Job {

    //Metodo que se ejecutara cada cierto tiempo que lo programemos despues
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        SimpleDateFormat formato = new SimpleDateFormat("hh:mm:ss");
        System.out.println("Tarea invocada a la hora: " + formato.format(new Date()));
        System.out.println("TAREAS BAFAR SAP 1. . . " + new Date());

        // Consulta de dispersiones dia anterior
        System.out.println("--- CONSULTA DE DISPERSIONES DIA ANTERIOR" + new Date());
        
        
        CrUtilConectaSAPBafar crUtilConectaSAPBafar = new CrUtilConectaSAPBafar(getConn());
        try{
            // en teoria, se van a consultar todos los registros dispersados
            // desde hace 2 dias al dia actual
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, -2); // restamos uno al dia de hoy
            Date rangoFechaInicio = cal.getTime();
            Date rangoFechaFin = new Date();
            
            CrConsultaDispersionSapBafarResponse resp = crUtilConectaSAPBafar.consultaDispersionSAP(null, rangoFechaInicio, rangoFechaFin);
            
            if (!resp.isError()) {
                try {
                    
                    if (resp.getListaOrdenesPago().size()>0){
                        CrFrmEventoSolicitudBO crFrmEventoSolicitudBO = new CrFrmEventoSolicitudBO(getConn());
                        CrFormularioEventoBO crFormularioEventoBO;
                        CrFrmEventoSolicitudDaoImpl crFrmEventoSolicitudDao = new CrFrmEventoSolicitudDaoImpl(getConn());
                        
                        // Por cada item de Orden de Pago recibido, buscamos el registro en bd y lo actualizamos
                        for (CrOrdenPago crOrdenPago : resp.getListaOrdenesPago()){
                            
                            // el dato Credito viene en el siguiente formato: CT0{no_contrato},
                            //   p. ej: "CT0900000143", asi que quitamos el prefijo para obtener el no_contrato
                            String noContrato = StringManage.getValidString(crOrdenPago.getCredito()).replaceFirst("CT0", "");
                            try{
                                CrFrmEventoSolicitud[] evSolicitudes = crFrmEventoSolicitudBO.findCrFrmEventoSolicituds(-1, -1, 0, 1, " AND sap_no_contrato = '" + noContrato  + "'");
                                CrFormularioEvento crFormularioEvento = null;
                                CrFrmEventoSolicitud crFrmEventoSolicitud = null;
                                if (evSolicitudes.length>0){
                                    crFrmEventoSolicitud = evSolicitudes[0];

                                    crFormularioEventoBO = new CrFormularioEventoBO(crFrmEventoSolicitud.getIdFormularioEvento(), getConn());
                                    crFormularioEvento = crFormularioEventoBO.getCrFormularioEvento();

                                }

                                // Si el Formulario Evento y el registro FrmEventoSolicitud Existen
                                // y ademas no ha sido registrado una Orden de Pago
                                // procedemos
                                if (crFormularioEvento!=null && crFrmEventoSolicitud!=null
                                        && StringManage.getValidString(crFrmEventoSolicitud.getSapOrdenPago()).length()<=0 ){
                                    //Guardamos objeto de Orden de Pago proveniente de SAP
                                    // en una cadena con formato JSON
                                    Gson gson = new Gson();  
                                    String ordenPagoJson = gson.toJson(crOrdenPago);

                                    crFrmEventoSolicitud.setSapOrdenPago(ordenPagoJson);
                                    crFrmEventoSolicitudDao.update(crFrmEventoSolicitud.createPk(), crFrmEventoSolicitud);

                                    // Como auxiliar por el momento usaremos la cuenta del usuario que edito por ultima vez la solicitud (mesa de control, verificdor o gerente)
                                    UsuarioBO usuarioBO = new UsuarioBO(getConn(), crFrmEventoSolicitud.getIdUsuarioEdicion());

                                    // Creamos registro de Cliente Credito Final en BD
                                    CrCredClienteBO crCredClienteBO = new CrCredClienteBO(conn);
                                    crCredClienteBO.crearCrClienteDeSolicitudCredito(crFormularioEvento, usuarioBO);

                                    
                                    // Cambiamos de estatus
                                    int idEstatusDestino = CrEstadoSolicitudBO.S_DISPERSADA;// 8 = Dispersada
                                    int idUsuarioCron = 0;
                                    RevisionesCr revisionesCr = new RevisionesCr();
                                    revisionesCr.almacenaRegistroSolicitudBitacora(this.conn, crFormularioEvento.getIdFormularioEvento(), 0, usuarioBO.getUser().getIdEmpresa(), idUsuarioCron, idEstatusDestino, "Dispersion reportada como ejecutada por parte de SAP.", false);

                                    
                                } 
                            } catch (Exception ex) {
                                throw new Exception("Error durante la aplicacion de la Orden de Pago para el No. de Contrato SAP "  + noContrato + " . Detalle: " + GenericMethods.exceptionStackTraceToString(ex));
                            }
                        } // End FOR Ordenes de Pago
                    }
                    
                } catch (Exception ex) {
                    throw new Exception("Error durante la actualización de la solicitud y/o creación del cliente final en BD."
                            + " Error: " + GenericMethods.exceptionStackTraceToString(ex));
                }
            } else {
                throw new Exception("Error en SAP durante la consulta de dispersión:"
                        + " Num Error: " + resp.getNumError() + " ."
                        + " Error: " + resp.getMsgError());
            }
        }catch(Exception ex){
            System.out.println("Error al consultar dispersiones en SAP: " + ex.toString());
            ex.printStackTrace();
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
