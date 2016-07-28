/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

import java.io.Serializable;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 12-jun-2013 
 */
public class CancelarAbonoPedidoResponse extends WSResponse implements Serializable{
    
    private double saldoPagadoPedido = 0;

    public double getSaldoPagadoPedido() {
        return saldoPagadoPedido;
    }

    public void setSaldoPagadoPedido(double saldoPagadoPedido) {
        this.saldoPagadoPedido = saldoPagadoPedido;
    }

}
