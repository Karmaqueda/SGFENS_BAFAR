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
public class CrTablaAmortizacion {
 
    private List<CrTablaAmortizacionDetalle> listaTablaAmortizacionDetalles;

    public List<CrTablaAmortizacionDetalle> getListaTablaAmortizacionDetalles() {
        if (listaTablaAmortizacionDetalles==null)
            listaTablaAmortizacionDetalles = new ArrayList<CrTablaAmortizacionDetalle>();
        return listaTablaAmortizacionDetalles;
    }

    public void setListaTablaAmortizacionDetalles(List<CrTablaAmortizacionDetalle> listaTablaAmortizacionDetalles) {
        this.listaTablaAmortizacionDetalles = listaTablaAmortizacionDetalles;
    }


    
}
