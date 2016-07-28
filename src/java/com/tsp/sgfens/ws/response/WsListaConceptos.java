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
public class WsListaConceptos {
    
    private ArrayList<WsItemConcepto> lista;

    public WsListaConceptos(){
        lista = new ArrayList<WsItemConcepto>();
    }

    public void addItem(WsItemConcepto data){
        this.lista.add(data);
    }

    public WsItemConcepto getItem(int pos){
        return this.lista.get(pos);
    }
    
    public ArrayList<WsItemConcepto> getLista() {
        return lista;
    }
}
