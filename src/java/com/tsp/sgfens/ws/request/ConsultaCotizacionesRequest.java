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
 * @author 578
 */
public class ConsultaCotizacionesRequest {
    
    
    private List<Integer> listaIdCotizacionServerFiltro;
    private List<String> listaFolioCotizacionMovilFiltro;
    private boolean filtroModificadoConsola = true;

    public List<Integer> getListaIdCotizacionServerFiltro() {
        if(listaIdCotizacionServerFiltro==null)
            listaIdCotizacionServerFiltro = new ArrayList<Integer>();
        return listaIdCotizacionServerFiltro;
    }

    public void setListaIdCotizacionServerFiltro(List<Integer> listaIdCotizacionServerFiltro) {
        this.listaIdCotizacionServerFiltro = listaIdCotizacionServerFiltro;
    }

    public List<String> getListaFolioCotizacionMovilFiltro() {
        if(listaFolioCotizacionMovilFiltro==null)
            listaFolioCotizacionMovilFiltro = new ArrayList<String>(); 
        return listaFolioCotizacionMovilFiltro;
    }

    public void setListaFolioCotizacionMovilFiltro(List<String> listaFolioCotizacionMovilFiltro) {
        this.listaFolioCotizacionMovilFiltro = listaFolioCotizacionMovilFiltro;
    }

    public boolean isFiltroModificadoConsola() {
        return filtroModificadoConsola;
    }

    public void setFiltroModificadoConsola(boolean filtroModificadoConsola) {
        this.filtroModificadoConsola = filtroModificadoConsola;
    }
    
    
    
}
