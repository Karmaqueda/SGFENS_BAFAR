/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.microsip.bean;

/**
 *
 * @author leonardo
 */
public class ImpuestosMicrosipBean {
    
    private int idImpuestoMicrosip = 0;    
    private String clave = "";
    private Impuesto impuesto = new Impuesto();

    /**
     * @return the idImpuestoMicrosip
     */
    public int getIdImpuestoMicrosip() {
        return idImpuestoMicrosip;
    }

    /**
     * @param idImpuestoMicrosip the idImpuestoMicrosip to set
     */
    public void setIdImpuestoMicrosip(int idImpuestoMicrosip) {
        this.idImpuestoMicrosip = idImpuestoMicrosip;
    }

    /**
     * @return the clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * @param clave the clave to set
     */
    public void setClave(String clave) {
        this.clave = clave;
    }

    /**
     * @return the impuesto
     */
    public Impuesto getImpuesto() {
        return impuesto;
    }

    /**
     * @param impuesto the impuesto to set
     */
    public void setImpuesto(Impuesto impuesto) {
        this.impuesto = impuesto;
    }
    
}
