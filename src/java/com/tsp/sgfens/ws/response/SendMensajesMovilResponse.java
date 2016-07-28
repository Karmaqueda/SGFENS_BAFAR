/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 31-ene-2013 
 */
public class SendMensajesMovilResponse extends WSResponse {

    List<WsItemSendMensajeResponse> wsItemSendMensajeResponse = null;

    public List<WsItemSendMensajeResponse> getWsItemSendMensajeResponse() {
        if (wsItemSendMensajeResponse==null)
            wsItemSendMensajeResponse = new ArrayList<WsItemSendMensajeResponse>();
        return wsItemSendMensajeResponse;
    }

    public void setWsItemSendMensajeResponse(List<WsItemSendMensajeResponse> wsItemSendMensajeResponse) {
        this.wsItemSendMensajeResponse = wsItemSendMensajeResponse;
    }
            
}
