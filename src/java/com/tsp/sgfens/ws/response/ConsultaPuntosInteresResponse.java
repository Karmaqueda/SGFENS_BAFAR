/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ing. Roberto Trejo
 */
public class ConsultaPuntosInteresResponse extends WSResponse{
    protected List<WsItemPuntoInteresEvc> listaPuntos;

    /**
     * @return the listaPuntos
     */
    public List<WsItemPuntoInteresEvc> getListaPuntos() {
        if(listaPuntos == null)
            listaPuntos = new ArrayList<WsItemPuntoInteresEvc>();
        return listaPuntos;
    }

    /**
     * @param listaPuntos the listaPuntos to set
     */
    public void setListaPuntos(List<WsItemPuntoInteresEvc> listaPuntos) {
        this.listaPuntos = listaPuntos;
    }
}
