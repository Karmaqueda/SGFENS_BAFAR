/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 30-abr-2013 
 */
public class CrearPedidoResponse extends WSResponseInsert {

    private String folioPedidoCreado;

    private int idAbonoCreado = -1;
    
    private int idBancoOperacionCreado = -1;

    public String getFolioPedidoCreado() {
        return folioPedidoCreado;
    }

    public void setFolioPedidoCreado(String folioPedidoCreado) {
        this.folioPedidoCreado = folioPedidoCreado;
    }

    public int getIdAbonoCreado() {
        return idAbonoCreado;
    }

    public void setIdAbonoCreado(int idAbonoCreado) {
        this.idAbonoCreado = idAbonoCreado;
    }

    public int getIdBancoOperacionCreado() {
        return idBancoOperacionCreado;
    }

    public void setIdBancoOperacionCreado(int idBancoOperacionCreado) {
        this.idBancoOperacionCreado = idBancoOperacionCreado;
    }
    
}
