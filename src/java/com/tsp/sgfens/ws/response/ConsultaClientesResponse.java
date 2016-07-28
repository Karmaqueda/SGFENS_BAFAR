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
public class ConsultaClientesResponse extends WSResponse {
    
    private ArrayList<WsItemCliente> listaClientes; 
    private ArrayList<WsItemClienteCampoAdicional> listaCampos;
    
    public ArrayList<WsItemCliente> getListaClientes() {
        if (listaClientes==null)
            listaClientes = new ArrayList<WsItemCliente>();
        return listaClientes;
    }

    public void setListaClientes(ArrayList<WsItemCliente> listaClientes) {
        this.listaClientes = listaClientes;
    }
    
    public ArrayList<WsItemClienteCampoAdicional> getListaCampos(){
        if(listaCampos == null)
            listaCampos = new ArrayList<WsItemClienteCampoAdicional>();
        return listaCampos;
    }
    
    public void setListaCampos(ArrayList<WsItemClienteCampoAdicional> listaCampos){
        this.listaCampos = listaCampos;
    }
}
