/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.microsip.bean;



/**
 *
 * @author leonardo
 */
public class ClientesMicrosipBean{
    
    private int idClienteMicrosip = 0;    
    private String clave = "";
    private Cliente cliente = new Cliente();

    /**
     * @return the idClienteMicrosip
     */
    public int getIdClienteMicrosip() {
        return idClienteMicrosip;
    }

    /**
     * @param idClienteMicrosip the idClienteMicrosip to set
     */
    public void setIdClienteMicrosip(int idClienteMicrosip) {
        this.idClienteMicrosip = idClienteMicrosip;
    }

    /**
     * @return the cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * @return the clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * @param clave the clave to set
     */
    public void setClave(String clave) {
        this.clave = clave;
    }
    
    
    
}
