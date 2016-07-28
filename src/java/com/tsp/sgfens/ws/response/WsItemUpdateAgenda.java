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
public class WsItemUpdateAgenda extends WSResponseInsert  {
    
    private int idAgenda;
    private int idEmpleado;
    private int idCliente;
    private int idMovilAgenda;

    /**
     * @return the idAgenda
     */
    public int getIdAgenda() {
        return idAgenda;
    }

    /**
     * @param idAgenda the idAgenda to set
     */
    public void setIdAgenda(int idAgenda) {
        this.idAgenda = idAgenda;
    }

    /**
     * @return the idEmpleado
     */
    public int getIdEmpleado() {
        return idEmpleado;
    }

    /**
     * @param idEmpleado the idEmpleado to set
     */
    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
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

    /**
     * @return the idMovilAgenda
     */
    public int getIdMovilAgenda() {
        return idMovilAgenda;
    }

    /**
     * @param idMovilAgenda the idMovilAgenda to set
     */
    public void setIdMovilAgenda(int idMovilAgenda) {
        this.idMovilAgenda = idMovilAgenda;
    }
    
}
