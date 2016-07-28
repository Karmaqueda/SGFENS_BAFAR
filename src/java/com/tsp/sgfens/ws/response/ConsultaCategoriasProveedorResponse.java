/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.util.ArrayList;

/**
 *
 * @author Ing. Roberto Trejo
 */
public class ConsultaCategoriasProveedorResponse extends WSResponse{
     protected ArrayList<WsItemCategoriaProveedor> listaCategorias;

    /**
     * @return the listaCategorias
     */
    public ArrayList<WsItemCategoriaProveedor> getListaCategorias() {
        if(listaCategorias == null)
            listaCategorias = new ArrayList<WsItemCategoriaProveedor>();
        return listaCategorias;
    }

    /**
     * @param listaCategorias the listaCategorias to set
     */
    public void setListaCategorias(ArrayList<WsItemCategoriaProveedor> listaCategorias) {
        this.listaCategorias = listaCategorias;
    }
}
