/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.request;

import java.util.Date;

/**
 *
 * @author ISCesarMartinez
 */
public class GetMensajesMovilRequest {
 
    // formato dd/mm/yyyy
    private Date filtroFechaInicio;
    private Date filtroFechaFinal;
    
    private boolean filtroNoRecibidos;
    private boolean filtroComunicacionConsola;
    
    /**
     * Solo funciona cuando se consulta los 
     * mensajes enviados (emitidos)
     */
    private int filtroIdReceptor;
    
    /**
     * Solo funciona cuando se consulta los mensajes recibidos
     */
    private int filtroIdEmisor;

    public boolean isFiltroComunicacionConsola() {
        return filtroComunicacionConsola;
    }

    public void setFiltroComunicacionConsola(boolean filtroComunicacionConsola) {
        this.filtroComunicacionConsola = filtroComunicacionConsola;
    }

    public Date getFiltroFechaFinal() {
        return filtroFechaFinal;
    }

    public void setFiltroFechaFinal(Date filtroFechaFinal) {
        this.filtroFechaFinal = filtroFechaFinal;
    }

    public int getFiltroIdEmisor() {
        return filtroIdEmisor;
    }

    public void setFiltroIdEmisor(int filtroIdEmisor) {
        this.filtroIdEmisor = filtroIdEmisor;
    }

    public int getFiltroIdReceptor() {
        return filtroIdReceptor;
    }

    public void setFiltroIdReceptor(int filtroIdReceptor) {
        this.filtroIdReceptor = filtroIdReceptor;
    }

    public boolean isFiltroNoRecibidos() {
        return filtroNoRecibidos;
    }

    public void setFiltroNoRecibidos(boolean filtroNoRecibidos) {
        this.filtroNoRecibidos = filtroNoRecibidos;
    }

    public Date getFiltroFechaInicio() {
        return filtroFechaInicio;
    }

    public void setFiltroFechaInicio(Date filtrofechaInicio) {
        this.filtroFechaInicio = filtrofechaInicio;
    }
    
    
    
}
