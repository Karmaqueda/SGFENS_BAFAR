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
public class WsListaCatalogoGastos {

    private ArrayList<WsItemCatalogoGastos> lista;

    public WsListaCatalogoGastos(){
        lista = new ArrayList<WsItemCatalogoGastos>();
    }

    public void addItem(WsItemCatalogoGastos data){
        this.lista.add(data);
    }

    public WsItemCatalogoGastos getItem(int pos){
        return this.lista.get(pos);
    }
    
    public ArrayList<WsItemCatalogoGastos> getLista() {
        return lista;
    }

}
