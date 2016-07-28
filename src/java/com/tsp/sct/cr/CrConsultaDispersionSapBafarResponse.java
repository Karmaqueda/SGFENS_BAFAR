/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.cr;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ISCesar
 */
public class CrConsultaDispersionSapBafarResponse extends CrSapBafarResponse {

    private List<CrOrdenPago> listaOrdenesPago = null;
    
    public CrConsultaDispersionSapBafarResponse() {
    }

    public List<CrOrdenPago> getListaOrdenesPago() {
        if (listaOrdenesPago==null)
            listaOrdenesPago = new ArrayList<CrOrdenPago>();
        return listaOrdenesPago;
    }

    public void setListaOrdenesPago(List<CrOrdenPago> listaOrdenesPago) {
        this.listaOrdenesPago = listaOrdenesPago;
    }
    
    
    
}
