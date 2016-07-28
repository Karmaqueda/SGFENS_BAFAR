/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.microsip.bean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class SgfensPedidosMicrosipBean {
    
    //private int idPedidosMicrosip = 0;    
    private String clave = "";
    private SgfensPedido pedidoDto = new SgfensPedido();
    private List<SgfensPedidoProducto> pedidoListProductos = new ArrayList<SgfensPedidoProducto>();
    private List<SgfensPedidoImpuesto> pedidoListImpuestos = new ArrayList<SgfensPedidoImpuesto>();

    /**
     * @return the idPedidosMicrosip
     */
/*    public int getIdPedidosMicrosip() {
        return idPedidosMicrosip;
    }
*/
    /**
     * @param idPedidosMicrosip the idPedidosMicrosip to set
     */
/*    public void setIdPedidosMicrosip(int idPedidosMicrosip) {
        this.idPedidosMicrosip = idPedidosMicrosip;
    }
*/
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

    /**
     * @return the pedidoDto
     */
    public SgfensPedido getPedidoDto() {
        return pedidoDto;
    }

    /**
     * @param pedidoDto the pedidoDto to set
     */
    public void setPedidoDto(SgfensPedido pedidoDto) {
        this.pedidoDto = pedidoDto;
    }

    /**
     * @return the pedidoListProductos
     */
    public List<SgfensPedidoProducto> getPedidoListProductos() {
        return pedidoListProductos;
    }

    /**
     * @param pedidoListProductos the pedidoListProductos to set
     */
    public void setPedidoListProductos(List<SgfensPedidoProducto> pedidoListProductos) {
        this.pedidoListProductos = pedidoListProductos;
    }

    /**
     * @return the pedidoListImpuestos
     */
    public List<SgfensPedidoImpuesto> getPedidoListImpuestos() {
        return pedidoListImpuestos;
    }

    /**
     * @param pedidoListImpuestos the pedidoListImpuestos to set
     */
    public void setPedidoListImpuestos(List<SgfensPedidoImpuesto> pedidoListImpuestos) {
        this.pedidoListImpuestos = pedidoListImpuestos;
    }
    
}
