/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.request;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ISCesar
 */
public class ConsultaImprimiblesRequest {
    
    private int idCrFormularioEvento;
    private List<Integer> filtroIdDocImprimible;
    private List<String> filtroTipoImprimible;    
    

    public int getIdCrFormularioEvento() {
        return idCrFormularioEvento;
    }

    public void setIdCrFormularioEvento(int idCrFormularioEvento) {
        this.idCrFormularioEvento = idCrFormularioEvento;
    }

    public List<Integer> getFiltroIdDocImprimible() {
        if (filtroIdDocImprimible==null)
            filtroIdDocImprimible = new ArrayList<Integer>();
        return filtroIdDocImprimible;
    }

    public void setFiltroIdDocImprimible(List<Integer> filtroIdDocImprimible) {
        this.filtroIdDocImprimible = filtroIdDocImprimible;
    }

    public List<String> getFiltroTipoImprimible() {
        if (filtroTipoImprimible==null)
            filtroTipoImprimible = new ArrayList<String>();
        return filtroTipoImprimible;
    }

    public void setFiltroTipoImprimible(List<String> filtroTipoImprimible) {
        this.filtroTipoImprimible = filtroTipoImprimible;
    }
    
    
}
