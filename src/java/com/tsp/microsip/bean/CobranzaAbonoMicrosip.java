/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.microsip.bean;

//import com.tsp.sct.dao.dto.SgfensCobranzaMetodoPago;

/**
 *
 * @author ISCesar poseidon24@hotmail.com
 * @since 4/05/2015 4/05/2015 11:53:10 AM
 */
public class CobranzaAbonoMicrosip {

    private int idMicrosip;
    private String clave = "";
    private SgfensCobranzaAbono cobranzaAbono = null;
    
    private SgfensCobranzaMetodoPago metodoPago = null;

    public SgfensCobranzaMetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(SgfensCobranzaMetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

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

    public SgfensCobranzaAbono getCobranzaAbono() {
        return cobranzaAbono;
    }

    public void setCobranzaAbono(SgfensCobranzaAbono cobranzaAbono) {
        this.cobranzaAbono = cobranzaAbono;
    }    
    
}
