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
public class WsListaBitacoraCreditos {
    
    private ArrayList<WsItemBitacoraCreditos> lista;
    
    public WsListaBitacoraCreditos(){
        lista = new ArrayList<WsItemBitacoraCreditos>();
    }

    public void addItem(WsItemBitacoraCreditos data){
        this.lista.add(data);
    }

    public WsItemBitacoraCreditos getItem(int pos){
        return this.lista.get(pos);
    }
    
    public ArrayList<WsItemBitacoraCreditos> getLista() {
        return lista;
    }
    
}
