/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

/**
 *
 * @author MOVILPYME
 */
public class WsItemRegistroVisitaCliente extends WSResponseInsert {
    protected int idRegistroVisita;
    protected int idCliente;

    /**
     * @return the idRegistroVisita
     */
    public int getIdRegistroVisita() {
        return idRegistroVisita;
    }

    /**
     * @param idRegistroVisita the idRegistroVisita to set
     */
    public void setIdRegistroVisita(int idRegistroVisita) {
        this.idRegistroVisita = idRegistroVisita;
    }

    /**
     * @return the idCliente
     */
    public int getIdCliente() {
        return idCliente;
    }

    /**
     * @param idCliente the idCliente to set
     */
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
}
