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
public class ConsultaProveedoresResponse extends WSResponse {
     protected ArrayList<WsItemProveedor> listaProveedor;

    /**
     * @return the listaProveedor
     */
    public ArrayList<WsItemProveedor> getListaProveedor() {
        if(listaProveedor == null)
            listaProveedor = new ArrayList<WsItemProveedor>();
        return listaProveedor;
    }

    /**
     * @param listaProveedor the listaProveedor to set
     */
    public void setListaProveedor(ArrayList<WsItemProveedor> listaProveedor) {
        this.listaProveedor = listaProveedor;
    }
}
