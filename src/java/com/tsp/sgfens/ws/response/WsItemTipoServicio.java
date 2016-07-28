/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

import java.io.Serializable;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 29-abr-2013 
 */
public class WsItemTipoServicio implements Serializable{
    
    private int idTipoServicio;
    private String nombreServicio;
    private double montoMaximoCobroTDC;

    public int getIdTipoServicio() {
        return idTipoServicio;
    }

    public void setIdTipoServicio(int idTipoServicio) {
        this.idTipoServicio = idTipoServicio;
    }

    public double getMontoMaximoCobroTDC() {
        return montoMaximoCobroTDC;
    }

    public void setMontoMaximoCobroTDC(double montoMaximoCobroTDC) {
        this.montoMaximoCobroTDC = montoMaximoCobroTDC;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

}
