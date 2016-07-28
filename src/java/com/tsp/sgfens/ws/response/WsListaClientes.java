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
public class WsListaClientes {
    
    private ArrayList<WsItemCliente> lista;

    public WsListaClientes(){
        lista = new ArrayList<WsItemCliente>();
    }

    public void addItem(WsItemCliente data){
        this.lista.add(data);
    }

    public WsItemCliente getItem(int pos){
        return this.lista.get(pos);
    }
    
    public ArrayList<WsItemCliente> getLista() {
        return lista;
    }
}
