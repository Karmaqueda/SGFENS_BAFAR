/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.sms.request;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ISCesar
 */
public class RegistroSmsProcesoRequest {
    
    private List<WsItemSmsProcesado> listaWsItemSmsProcesados;

    public List<WsItemSmsProcesado> getListaWsItemSmsProcesados() {
        if (listaWsItemSmsProcesados==null)
            listaWsItemSmsProcesados = new ArrayList<WsItemSmsProcesado>();
        return listaWsItemSmsProcesados;
    }

    public void setListaWsItemSmsProcesados(List<WsItemSmsProcesado> listaWsItemSmsProcesados) {
        this.listaWsItemSmsProcesados = listaWsItemSmsProcesados;
    }
    
}
