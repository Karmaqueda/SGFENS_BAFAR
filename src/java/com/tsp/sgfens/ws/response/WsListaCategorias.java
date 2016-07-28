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
public class WsListaCategorias {
    private ArrayList<WsItemClienteCategoria> lista;
    
    public WsListaCategorias(){
        lista = new ArrayList<WsItemClienteCategoria>();
    }

    /**
     * @return the lista
     */
    public ArrayList<WsItemClienteCategoria> getLista() {
        return lista;
    }

    /**
     * @param lista the lista to set
     */
    public void setLista(ArrayList<WsItemClienteCategoria> lista) {
        this.lista = lista;
    }
    
    public void addItem(WsItemClienteCategoria data){
        this.lista.add(data);
    }
    
    public WsItemClienteCategoria getItem(int pos){
        return this.lista.get(pos);
    }
    
}
