/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.pos.response;

import com.tsp.sgfens.ws.response.WSResponse;
import com.tsp.sgfens.ws.response.WsItemConcepto;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ISCesarMartinez
 */
public class ConsultaConceptosResponse extends WSResponse{
    
    private List<WsItemConcepto> listaConceptos;


    public List<WsItemConcepto> getListaConceptos() {
        if (listaConceptos==null)
            listaConceptos = new ArrayList<WsItemConcepto>();
        return listaConceptos;
    }

    public void setListaConceptos(List<WsItemConcepto> listaConceptos) {
        this.listaConceptos = listaConceptos;
    }
    
    
}
