/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.microsip.bean;


/**
 *
 * @author ISCesar poseidon24@hotmail.com
 * @since 4/05/2015 4/05/2015 11:53:10 AM
 */
public class ConceptoMicrosip {

    private int idMicrosip;
    private String clave = "";
    private Concepto concepto = null;

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

    public Concepto getConcepto() {
        return concepto;
    }

    public void setConcepto(Concepto concepto) {
        this.concepto = concepto;
    }
    
}
