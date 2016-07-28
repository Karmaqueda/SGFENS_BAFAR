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
public class WsListaClientesCamposAdicionales {
    private ArrayList<WsItemClienteCampoAdicional> lista;
    
    public WsListaClientesCamposAdicionales(){
        lista = new ArrayList<WsItemClienteCampoAdicional>();
    }
    
    public void addItem(WsItemClienteCampoAdicional data){
        this.lista.add(data);
    }
    
    public WsItemClienteCampoAdicional getItem(int pos){
        return this.lista.get(pos);
    }
    
    public ArrayList<WsItemClienteCampoAdicional> getLista(){
        return lista;
    }
}
