package com.tsp.sgfens.ws.response;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 13-jun-2013 
 */
public class ModificarPedidoResponse extends WSResponse {
    
    private double saldoPagadoPedido = 0;
    private boolean pedidoActualizado = false;

    public double getSaldoPagadoPedido() {
        return saldoPagadoPedido;
    }

    public void setSaldoPagadoPedido(double saldoPagadoPedido) {
        this.saldoPagadoPedido = saldoPagadoPedido;
    }

    public boolean isPedidoActualizado() {
        return pedidoActualizado;
    }

    public void setPedidoActualizado(boolean pedidoActualizado) {
        this.pedidoActualizado = pedidoActualizado;
    }
    
    

}
