/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.microsip.bean;

/**
 *
 * @author HpPyme
 */
public class ExistenciaAlmacenMicrosip {
    
    private int idMicrosip;
    private String clave = "";
    private ExistenciaAlmacenBean existenciaAlmacen = null;

    public int getIdMicrosip() {
        return idMicrosip;
    }

    public void setIdMicrosip(int idMicrosip) {
        this.idMicrosip = idMicrosip;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public ExistenciaAlmacenBean getExistenciaAlmacen() {
        return existenciaAlmacen;
    }

    public void setExistenciaAlmacen(ExistenciaAlmacenBean existenciaAlmacen) {
        this.existenciaAlmacen = existenciaAlmacen;
    }

    public ExistenciaAlmacenMicrosip() {
    }
    
    
    
    
}
