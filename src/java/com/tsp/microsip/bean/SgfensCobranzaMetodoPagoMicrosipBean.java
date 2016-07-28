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
public class SgfensCobranzaMetodoPagoMicrosipBean {
    
    private int idMetodosPagoMicrosip = 0;    
    private String clave = "";
    private SgfensCobranzaMetodoPago metodosPago = new SgfensCobranzaMetodoPago();

    /**
     * @return the idMetodosPagoMicrosip
     */
    public int getIdMetodosPagoMicrosip() {
        return idMetodosPagoMicrosip;
    }

    /**
     * @param idMetodosPagoMicrosip the idMetodosPagoMicrosip to set
     */
    public void setIdMetodosPagoMicrosip(int idMetodosPagoMicrosip) {
        this.idMetodosPagoMicrosip = idMetodosPagoMicrosip;
    }

    /**
     * @return the metodosPago
     */
    public SgfensCobranzaMetodoPago getMetodosPago() {
        return metodosPago;
    }

    /**
     * @param metodosPago the metodosPago to set
     */
    public void setMetodosPago(SgfensCobranzaMetodoPago metodosPago) {
        this.metodosPago = metodosPago;
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
      
}
