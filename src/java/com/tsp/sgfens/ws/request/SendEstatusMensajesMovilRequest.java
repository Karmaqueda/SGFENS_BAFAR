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
public class SendEstatusMensajesMovilRequest {

    List<ItemEstatusMensajeMovilRequest> itemEstatusMensajeMovilRequest = null;

    public List<ItemEstatusMensajeMovilRequest> getItemEstatusMensajeMovilRequest() {
        if (this.itemEstatusMensajeMovilRequest==null)
            this.itemEstatusMensajeMovilRequest = new ArrayList<ItemEstatusMensajeMovilRequest>();
        return itemEstatusMensajeMovilRequest;
    }

    public void setItemEstatusMensajeMovilRequest(List<ItemEstatusMensajeMovilRequest> itemEstatusMensajeMovilRequest) {
        this.itemEstatusMensajeMovilRequest = itemEstatusMensajeMovilRequest;
    }
    
}
