/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ing. Roberto Trejo
 */
public class ConsultaCrFrmEventoSolicitudResponse extends WSResponse{
    
    protected List<WsItemCrFrmEventoSolicitud> listaSolicitudes;
    protected List<WsItemFormularioRespuesta> listaRespuestasPorRevisar;

    /**
     * @return the listaSolicitudes
     */
    public List<WsItemCrFrmEventoSolicitud> getListaSolicitudes() {
        if(listaSolicitudes == null)
            listaSolicitudes = new ArrayList<WsItemCrFrmEventoSolicitud>();
        return listaSolicitudes;
    }

    /**
     * @param listaSolicitudes the listaSolicitudes to set
     */
    public void setListaSolicitudes(List<WsItemCrFrmEventoSolicitud> listaSolicitudes) {
        this.listaSolicitudes = listaSolicitudes;
    }

    /**
     * @return the listaRespuestasPorRevisar
     */
    public List<WsItemFormularioRespuesta> getListaRespuestasPorRevisar() {
        if(listaRespuestasPorRevisar == null)
            listaRespuestasPorRevisar = new ArrayList<WsItemFormularioRespuesta>();
        return listaRespuestasPorRevisar;
    }

    /**
     * @param listaRespuestasPorRevisar the listaRespuestasPorRevisar to set
     */
    public void setListaRespuestasPorRevisar(List<WsItemFormularioRespuesta> listaRespuestasPorRevisar) {
        this.listaRespuestasPorRevisar = listaRespuestasPorRevisar;
    }
    
}
