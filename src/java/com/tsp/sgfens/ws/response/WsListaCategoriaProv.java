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
public class WsListaCategoriaProv {
    protected ArrayList<WsItemCategoriaProveedor> lista;
    
    public WsListaCategoriaProv(){
        lista = new ArrayList<WsItemCategoriaProveedor>();
    }

    /**
     * @return the lista
     */
    public ArrayList<WsItemCategoriaProveedor> getLista() {
        return lista;
    }

    /**
     * @param lista the lista to set
     */
    public void setLista(ArrayList<WsItemCategoriaProveedor> lista) {
        this.lista = lista;
    }
    
     public void addItem(WsItemCategoriaProveedor data){
         this.lista.add(data);
     }
     
     public WsItemCategoriaProveedor getItem(int pos){
        return this.lista.get(pos);
    }
}
