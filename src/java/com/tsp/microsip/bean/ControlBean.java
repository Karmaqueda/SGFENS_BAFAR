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
public class ControlBean {
    
    private String mensajeError = "";
    private boolean exito = false;
    private int registradosNuevos = 0;
    private int registradosActualizados = 0;

    /**
     * @return the mensajeError
     */
    public String getMensajeError() {
        return mensajeError;
    }

    /**
     * @param mensajeError the mensajeError to set
     */
    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    /**
     * @return the exito
     */
    public boolean isExito() {
        return exito;
    }

    /**
     * @param exito the exito to set
     */
    public void setExito(boolean exito) {
        this.exito = exito;
    }

    /**
     * @return the registradosNuevos
     */
    public int getRegistradosNuevos() {
        return registradosNuevos;
    }

    /**
     * @param registradosNuevos the registradosNuevos to set
     */
    public void setRegistradosNuevos(int registradosNuevos) {
        this.registradosNuevos = registradosNuevos;
    }

    /**
     * @return the registradosActualizados
     */
    public int getRegistradosActualizados() {
        return registradosActualizados;
    }

    /**
     * @param registradosActualizados the registradosActualizados to set
     */
    public void setRegistradosActualizados(int registradosActualizados) {
        this.registradosActualizados = registradosActualizados;
    }

        
    
}
