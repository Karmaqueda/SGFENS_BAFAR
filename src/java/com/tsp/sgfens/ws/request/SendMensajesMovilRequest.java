/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.request;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 31-ene-2013 
 */
public class SendMensajesMovilRequest {

    List<ItemMensajeMovilRequest> itemMensajeMovilRequest = null;

    public SendMensajesMovilRequest() {
    }

    public List<ItemMensajeMovilRequest> getItemMensajeMovilRequest() {
        if (this.itemMensajeMovilRequest==null)
            this.itemMensajeMovilRequest = new ArrayList<ItemMensajeMovilRequest>();
        return this.itemMensajeMovilRequest;
    }

    public void setItemMensajeMovilRequest(List<ItemMensajeMovilRequest> itemMensajeMovilRequest) {
        this.itemMensajeMovilRequest = itemMensajeMovilRequest;
    }
    
}