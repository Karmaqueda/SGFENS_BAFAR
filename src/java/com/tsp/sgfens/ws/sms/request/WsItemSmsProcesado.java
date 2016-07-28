/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.sms.request;

import com.tsp.sgfens.ws.sms.response.WsItemSmsEnvioDetalle;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ISCesar
 */
public class WsItemSmsProcesado {

    private boolean enviado;
    private WsItemSmsEnvioDetalle wsItemSmsEnvioDetalle;
    private List<WsItemEnvioError> wsItemEnvioError;

    public boolean isEnviado() {
        return enviado;
    }

    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }

    
    public WsItemSmsEnvioDetalle getWsItemSmsEnvioDetalle() {
        return wsItemSmsEnvioDetalle;
    }

    public void setWsItemSmsEnvioDetalle(WsItemSmsEnvioDetalle wsItemSmsEnvioDetalle) {
        this.wsItemSmsEnvioDetalle = wsItemSmsEnvioDetalle;
    }

    public List<WsItemEnvioError> getWsItemEnvioError() {
        if (wsItemEnvioError==null)
            wsItemEnvioError = new ArrayList<WsItemEnvioError>();
        return wsItemEnvioError;
    }

    public void setWsItemEnvioError(List<WsItemEnvioError> wsItemEnvioError) {
        this.wsItemEnvioError = wsItemEnvioError;
    }

}
