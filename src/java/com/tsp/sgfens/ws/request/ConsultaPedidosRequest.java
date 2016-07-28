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
 * @author ISCesar poseidon24@hotmail.com
 * @since 7/10/2014 05:54:25 PM
 */
public class ConsultaPedidosRequest {

    private List<Integer> listaIdPedidoServerFiltro;
    private List<String> listaFolioPedidoMovilFiltro;
    private boolean filtroModificadoConsola = true;

    public List<Integer> getListaIdPedidoServerFiltro() {
        if (listaIdPedidoServerFiltro==null)
            listaIdPedidoServerFiltro = new ArrayList<Integer>();
        return listaIdPedidoServerFiltro;
    }

    public void setListaIdPedidoServerFiltro(List<Integer> listaIdPedidoServerFiltro) {
        this.listaIdPedidoServerFiltro = listaIdPedidoServerFiltro;
    }

    public List<String> getListaFolioPedidoMovilFiltro() {
        if (listaFolioPedidoMovilFiltro==null)
            listaFolioPedidoMovilFiltro = new ArrayList<String>();
        return listaFolioPedidoMovilFiltro;
    }

    public void setListaFolioPedidoMovilFiltro(List<String> listaFolioPedidoMovilFiltro) {
        this.listaFolioPedidoMovilFiltro = listaFolioPedidoMovilFiltro;
    }

    public boolean isFiltroModificadoConsola() {
        return filtroModificadoConsola;
    }

    public void setFiltroModificadoConsola(boolean filtroModificadoConsola) {
        this.filtroModificadoConsola = filtroModificadoConsola;
    }
    
}
