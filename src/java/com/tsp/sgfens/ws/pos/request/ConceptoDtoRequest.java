/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.pos.request;

import com.tsp.sgfens.ws.request.*;

/**
 *
 * @author Ing. Roberto Trejo
 */
public class ConceptoDtoRequest {
    private int idConcepto;
    
    private int idConceptoMovil;

    /**
     * @return the idConcepto
     */
    public int getIdConcepto() {
        return idConcepto;
    }

    /**
     * @param idConcepto the idConcepto to set
     */
    public void setIdConcepto(int idConcepto) {
        this.idConcepto = idConcepto;
    }

    /**
     * @return the idConceptoMovil
     */
    public int getIdConceptoMovil() {
        return idConceptoMovil;
    }

    /**
     * @param idConceptoMovil the idConceptoMovil to set
     */
    public void setIdConceptoMovil(int idConceptoMovil) {
        this.idConceptoMovil = idConceptoMovil;
    }
}
