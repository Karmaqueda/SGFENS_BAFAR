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
public class CrearAbonoPedidoResponse extends WSResponseInsert implements Serializable{

    /**
     * ID de Banco Operación en Servidor en caso de ser un cobro con tarjeta
     */
    private int idBancoOperacionCreado = -1;
    
    /**
     * Flag booleano que indica en caso de error al registrar abono,
     * si el error corresponde a un pago excedido de la deuda actual del pedido.
     */
    private boolean abonoExcedido = false;
    
    private double saldoPagadoPedido = 0;

    public boolean isAbonoExcedido() {
        return abonoExcedido;
    }

    public void setAbonoExcedido(boolean abonoExcedido) {
        this.abonoExcedido = abonoExcedido;
    }

    public int getIdBancoOperacionCreado() {
        return idBancoOperacionCreado;
    }

    public void setIdBancoOperacionCreado(int idBancoOperacionCreado) {
        this.idBancoOperacionCreado = idBancoOperacionCreado;
    }

    public double getSaldoPagadoPedido() {
        return saldoPagadoPedido;
    }

    public void setSaldoPagadoPedido(double saldoPagadoPedido) {
        this.saldoPagadoPedido = saldoPagadoPedido;
    }

   
    
    
}
