/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.cr;

/**
 *
 * @author ISCesar
 */
public class CrSapBafarResponse {
    
    private boolean error = false;
    private String msgError = "";
    private String numError = "0";
    private String msgExito = "";
    private String msgAdvertencia = "";

    public CrSapBafarResponse() {
    }
    
    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public String getNumError() {
        return numError;
    }

    public void setNumError(String numError) {
        this.numError = numError;
    }

    public String getMsgExito() {
        return msgExito;
    }

    public void setMsgExito(String msgExito) {
        this.msgExito = msgExito;
    }

    public String getMsgAdvertencia() {
        return msgAdvertencia;
    }

    public void setMsgAdvertencia(String msgAdvertencia) {
        this.msgAdvertencia = msgAdvertencia;
    }
    
    
    
    
}
