/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.sms.response;

import com.tsp.sgfens.ws.response.WSResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ISCesar
 */
public class ConsultaSmsPorEnviarResponse extends WSResponse{
    
    private List<WsItemSmsEnvioDetalle> listaSms;

    public List<WsItemSmsEnvioDetalle> getListaSms() {
        if (listaSms==null)
            listaSms = new ArrayList<WsItemSmsEnvioDetalle>();
        return listaSms;
    }

    public void setListaSms(List<WsItemSmsEnvioDetalle> listaSms) {
        this.listaSms = listaSms;
    }
    
    
    
}
