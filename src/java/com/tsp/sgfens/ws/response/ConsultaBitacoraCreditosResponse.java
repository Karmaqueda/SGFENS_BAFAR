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
public class ConsultaBitacoraCreditosResponse extends WSResponse {
    
    private ArrayList<WsItemBitacoraCreditos> listaBitacora;
    
    public ArrayList<WsItemBitacoraCreditos> getListaBitacora() {
        if (listaBitacora==null)
            listaBitacora = new ArrayList<WsItemBitacoraCreditos>();
        return listaBitacora;
    }

    public void setListaBitacoras(ArrayList<WsItemBitacoraCreditos> listaBitacora) {
        this.listaBitacora = listaBitacora;
    }
    
}
