/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 31-ene-2013 
 */
public class WsItemSendMensajeResponse extends WSResponseInsert {

    private long rastreadorMensaje;

    public long getRastreadorMensaje() {
        return rastreadorMensaje;
    }

    public void setRastreadorMensaje(long rastreadorMensaje) {
        this.rastreadorMensaje = rastreadorMensaje;
    }
    
}
