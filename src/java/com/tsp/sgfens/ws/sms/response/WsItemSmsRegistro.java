/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.sms.response;

/**
 *
 * @author ISCesar
 */
public class WsItemSmsRegistro {
    
    private int idSmsEnvioDetalle;
    private boolean registrado = true;
    private String errorRegistro;

    public int getIdSmsEnvioDetalle() {
        return idSmsEnvioDetalle;
    }

    public void setIdSmsEnvioDetalle(int idSmsEnvioDetalle) {
        this.idSmsEnvioDetalle = idSmsEnvioDetalle;
    }

    public boolean isRegistrado() {
        return registrado;
    }

    public void setRegistrado(boolean registrado) {
        this.registrado = registrado;
    }

    public String getErrorRegistro() {
        return errorRegistro;
    }

    public void setErrorRegistro(String errorRegistro) {
        this.errorRegistro = errorRegistro;
    }
    
}
