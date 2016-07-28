/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.sms.request;

import java.util.Date;

/**
 *
 * @author ISCesar
 */
public class WsItemEnvioError {

    private String descError;
    private Date fechaHrError;

    public String getDescError() {
        return descError;
    }

    public void setDescError(String descError) {
        this.descError = descError;
    }

    public Date getFechaHrError() {
        return fechaHrError;
    }

    public void setFechaHrError(Date fechaHrError) {
        this.fechaHrError = fechaHrError;
    }

}
