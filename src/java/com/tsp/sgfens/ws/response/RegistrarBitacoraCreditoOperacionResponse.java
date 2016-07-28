/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

/**
 *
 * @author ISCesar poseidon24@hotmail.com
 * @since 14/05/2015 14/05/2015 11:58:20 AM
 */
public class RegistrarBitacoraCreditoOperacionResponse extends WSResponseInsert {
    
    private int idBancoOperacionCreado;
    private int creditosOperacionDisponibles;

    public int getIdBancoOperacionCreado() {
        return idBancoOperacionCreado;
    }

    public void setIdBancoOperacionCreado(int idBancoOperacionCreado) {
        this.idBancoOperacionCreado = idBancoOperacionCreado;
    }

    public int getCreditosOperacionDisponibles() {
        return creditosOperacionDisponibles;
    }

    public void setCreditosOperacionDisponibles(int creditosOperacionDisponibles) {
        this.creditosOperacionDisponibles = creditosOperacionDisponibles;
    }

}
