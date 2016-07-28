/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.sesion;

import com.tsp.sct.bo.EmpresaBO;
import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.util.SmsLengthCalculator;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ISCesar
 */
public class SmsEnvioSesion {
    
    private Connection conn = null;
    private List<SmsEnvioDestinatariosSesion> listaDestinatarios = null;
    private String asunto = "";
    private String contenido = "";
    
    private int idPlantilla;
    private boolean envioInmediato = true;
    private Date fechaHoraEnvioProgramado = null;
    
    private int idEmpresaRemitente = -1;

    public SmsEnvioSesion() {
    }
    
    public SmsEnvioSesion(Connection conn) {
        this.conn = conn;
    }
    
    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public List<SmsEnvioDestinatariosSesion> getListaDestinatarios() {
        if (listaDestinatarios==null)
            listaDestinatarios = new ArrayList<SmsEnvioDestinatariosSesion>();
        return listaDestinatarios;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public int getIdPlantilla() {
        return idPlantilla;
    }

    public void setIdPlantilla(int idPlantilla) {
        this.idPlantilla = idPlantilla;
    }

    public boolean isEnvioInmediato() {
        return envioInmediato;
    }

    public void setEnvioInmediato(boolean envioInmediato) {
        this.envioInmediato = envioInmediato;
    }

    public Date getFechaHoraEnvioProgramado() {
        return fechaHoraEnvioProgramado;
    }

    public void setFechaHoraEnvioProgramado(Date fechaHoraEnvioProgramado) {
        this.fechaHoraEnvioProgramado = fechaHoraEnvioProgramado;
    }

    public int getIdEmpresaRemitente() {
        return idEmpresaRemitente;
    }

    public void setIdEmpresaRemitente(int idEmpresaRemitente) {
        this.idEmpresaRemitente = idEmpresaRemitente;
    }
    
    public String creaContenidoSMS(){
        String contenidoSMS;
        
        try{
            if (idEmpresaRemitente>0){
                EmpresaBO empresaBO = new EmpresaBO(conn);
                Empresa empresaMatriz = empresaBO.getEmpresaMatriz(idEmpresaRemitente);

                String nombreEmpresaRemitente = StringManage.getValidString(empresaMatriz.getNombreComercial());
                if (nombreEmpresaRemitente.length()<=0)
                    nombreEmpresaRemitente = StringManage.getValidString(empresaMatriz.getRazonSocial());

                contenidoSMS = nombreEmpresaRemitente.toUpperCase() + " le informa: " + StringManage.getValidString(this.contenido);
            }else{
                contenidoSMS = StringManage.getValidString(this.contenido);
            }
        }catch(Exception ex){
            contenidoSMS = StringManage.getValidString(this.contenido);
            ex.printStackTrace();
        }
        
        return contenidoSMS;
    }
    
    /**
     * Calcula cuantas partes de SMS se requieren para el contenido del mensaje
     * @return Entero con la cantidad de Partes
     */
    public int calculaPartesMensaje(){
        int partes = 0;
        
        try{
            SmsLengthCalculator calc = new SmsLengthCalculator();
            partes = calc.getPartCount(creaContenidoSMS());
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return partes;
    }
    
    /**
     * Calcula los creditos SMS necesarios para el envio de todos los mensajes
     *  a todos los destinatarios de esta sesion.
     * <p>
     * creditos sms = No. Destinatarios * No. Partes Mensaje
     * </p>
     * @return Numero de creditos SMS necesarios para enviar todos los mensajes 
     * a todos los destinatarios.
     */
    public int calculaCreditosSMS(){
        int creditos = -1;
        
        try{
            //creditos sms = No. Destinatarios * No. Partes Mensaje
            creditos = listaDestinatarios.size() * calculaPartesMensaje();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return creditos;
    }
    
}
