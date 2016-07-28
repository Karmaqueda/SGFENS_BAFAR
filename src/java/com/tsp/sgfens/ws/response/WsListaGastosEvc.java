/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

import java.util.ArrayList;

/**
 *
 * @author HpPyme
 */
public class WsListaGastosEvc {
    
    
     private ArrayList<WsItemGastosEvc> lista;
     
     
     public WsListaGastosEvc(){
        lista = new ArrayList<WsItemGastosEvc>();
    }

    public void addItem(WsItemGastosEvc data){
        this.lista.add(data);
    }

    public WsItemGastosEvc getItem(int pos){
        return this.lista.get(pos);
    }
    
    public ArrayList<WsItemGastosEvc> getLista() {
        return lista;
    }
    
}
