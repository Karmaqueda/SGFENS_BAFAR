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
public class ConsultaProveedorConceptosServiciosResponse  extends WSResponse{
    
    private ArrayList<WsItemProveedorConceptoServicio> listaConceptosServicios;
    private int totalRegistros = 0;


    public ArrayList<WsItemProveedorConceptoServicio> getListaConceptos() {
        if (listaConceptosServicios==null)
            listaConceptosServicios = new ArrayList<WsItemProveedorConceptoServicio>();
        return listaConceptosServicios;
    }

    public void setListaConceptos(ArrayList<WsItemProveedorConceptoServicio> listaConceptosServicios) {
        this.listaConceptosServicios = listaConceptosServicios;
    }

    public int getTotalRegistros() {
        return totalRegistros;
    }

    public void setTotalRegistros(int totalRegistros) {
        this.totalRegistros = totalRegistros;
    }
    
}