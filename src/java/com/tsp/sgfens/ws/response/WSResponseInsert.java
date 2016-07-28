/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 18-ene-2013 
 */
public class WSResponseInsert extends WSResponse{
    
    /**
     * ID creado al realizar insert de objeto, o
     * recuperación del ID en caso de update.
     */
    private long idObjetoCreado;

    /**
     * ID creado al realizar insert de objeto, o
     * recuperación del ID en caso de update.
     */
    public long getIdObjetoCreado() {
        return idObjetoCreado;
    }

    /**
     * ID creado al realizar insert de objeto, o
     * recuperación del ID en caso de update.
     */
    public void setIdObjetoCreado(long idObjetoCreado) {
        this.idObjetoCreado = idObjetoCreado;
    }

}
