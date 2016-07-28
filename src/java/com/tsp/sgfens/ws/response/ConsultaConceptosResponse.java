/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.util.ArrayList;

/**
 *
 * @author ISCesarMartinez
 */
public class ConsultaConceptosResponse extends WSResponse{
    
    private ArrayList<WsItemConcepto> listaConceptos;
    private int totalRegistros = 0;


    public ArrayList<WsItemConcepto> getListaConceptos() {
        if (listaConceptos==null)
            listaConceptos = new ArrayList<WsItemConcepto>();
        return listaConceptos;
    }

    public void setListaConceptos(ArrayList<WsItemConcepto> listaConceptos) {
        this.listaConceptos = listaConceptos;
    }

    public int getTotalRegistros() {
        return totalRegistros;
    }

    public void setTotalRegistros(int totalRegistros) {
        this.totalRegistros = totalRegistros;
    }
    
}
