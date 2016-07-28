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
public class WsListaPuntosEvc {
    private ArrayList<WsItemPuntoInteresEvc> lista;
    
    public WsListaPuntosEvc(){
        lista = new ArrayList<WsItemPuntoInteresEvc>();
    }
    
    public void addItem(WsItemPuntoInteresEvc data){
        this.lista.add(data);
    }
    
    public WsItemPuntoInteresEvc getItem(int position){
        return this.lista.get(position);
    }
    
    public ArrayList<WsItemPuntoInteresEvc> getLista(){
        return lista;
    }
}
