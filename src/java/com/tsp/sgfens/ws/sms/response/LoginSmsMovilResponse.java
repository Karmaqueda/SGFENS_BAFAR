/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.sms.response;

import com.tsp.sgfens.ws.response.*;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 30-mar-2016
 */
public class LoginSmsMovilResponse extends WSResponse{
    
    private WsItemDispositivoMovilSms wsItemDispositivoMovil;

    public WsItemDispositivoMovilSms getWsItemDispositivoMovil() {
        return wsItemDispositivoMovil;
    }

    public void setWsItemDispositivoMovil(WsItemDispositivoMovilSms wsItemDispositivoMovil) {
        this.wsItemDispositivoMovil = wsItemDispositivoMovil;
    }
    
    
    
    
}
