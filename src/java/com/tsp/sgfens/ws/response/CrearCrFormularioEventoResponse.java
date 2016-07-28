/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

/**
 *
 * @author Ing. Roberto Trejo
 */
public class CrearCrFormularioEventoResponse extends WSResponseInsert {
    protected int idEventoSolicitud;

    /**
     * @return the idEventoSolicitud
     */
    public int getIdEventoSolicitud() {
        return idEventoSolicitud;
    }

    /**
     * @param idEventoSolicitud the idEventoSolicitud to set
     */
    public void setIdEventoSolicitud(int idEventoSolicitud) {
        this.idEventoSolicitud = idEventoSolicitud;
    }
}
