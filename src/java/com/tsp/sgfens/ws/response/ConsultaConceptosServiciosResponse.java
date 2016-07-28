/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

import java.util.ArrayList;

/**
 *
 * @author leonardo
 */
public class ConsultaConceptosServiciosResponse extends WSResponse{
    
    private ArrayList<WsItemConceptoServicio> listaConceptosServicios;
    private int totalRegistros = 0;


    public ArrayList<WsItemConceptoServicio> getListaConceptos() {
        if (listaConceptosServicios==null)
            listaConceptosServicios = new ArrayList<WsItemConceptoServicio>();
        return listaConceptosServicios;
    }

    public void setListaConceptos(ArrayList<WsItemConceptoServicio> listaConceptosServicios) {
        this.listaConceptosServicios = listaConceptosServicios;
    }

    public int getTotalRegistros() {
        return totalRegistros;
    }

    public void setTotalRegistros(int totalRegistros) {
        this.totalRegistros = totalRegistros;
    }
    
}