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
public class WsListaProveedores {
    private ArrayList<WsItemProveedor> lista;
    
    public WsListaProveedores(){
        lista = new ArrayList<WsItemProveedor>();
    }
    
    public void addItem(WsItemProveedor data){
        this.lista.add(data);
    }
    
    public WsItemProveedor  getItem(int pos){
        return this.lista.get(pos);
    }
    
    public ArrayList<WsItemProveedor> getLista() {
        return lista;
    }
}
