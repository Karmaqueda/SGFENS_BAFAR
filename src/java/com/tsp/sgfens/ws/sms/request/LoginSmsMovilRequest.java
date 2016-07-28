/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.sms.request;

import java.util.Date;

/**
 *
 * @author ISCesarMartinez
 * 
 * Clase POJO contenedora de datos básicos del usuario
 * en sesión móvil que accede al web service
 */
public class LoginSmsMovilRequest {
    
    private String usuario;
    private String password;
    private String dispositivoIMEI;
    private String ubicacionLatitud;
    private String ubicacionLongitud;
    private Date fechaHora;
    private double porcentajeBateria;

    public LoginSmsMovilRequest() {
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDispositivoIMEI() {
        return dispositivoIMEI;
    }

    public void setDispositivoIMEI(String dispositivoIMEI) {
        this.dispositivoIMEI = dispositivoIMEI;
    }

    public String getUbicacionLatitud() {
        return ubicacionLatitud;
    }

    public void setUbicacionLatitud(String ubicacionLatitud) {
        this.ubicacionLatitud = ubicacionLatitud;
    }

    public String getUbicacionLongitud() {
        return ubicacionLongitud;
    }

    public void setUbicacionLongitud(String ubicacionLongitud) {
        this.ubicacionLongitud = ubicacionLongitud;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public double getPorcentajeBateria() {
        return porcentajeBateria;
    }

    public void setPorcentajeBateria(double porcentajeBateria) {
        this.porcentajeBateria = porcentajeBateria;
    }

    
    
}
