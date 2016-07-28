/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.pos.response;

import com.tsp.sgfens.ws.response.WSResponse;
import com.tsp.sgfens.ws.response.WsItemEmpleado;
import com.tsp.sgfens.ws.response.WsItemSucursal;

/**
 *
 * @author ISCesar poseidon24@hotmail.com
 * @since 5/06/2015 5/06/2015 10:21:53 AM
 */
public class LoginEmpleadoResponse extends WSResponse{

    /**
     * Objeto con Datos de Empleado
     */
    private WsItemEmpleado wsItemEmpleado;
    
    /**
     * Objeto con datos de empresa-sucursal a la que pertenece el empleado
     */
    private WsItemSucursal wsItemSucursal;

    public WsItemEmpleado getWsItemEmpleado() {
        return wsItemEmpleado;
    }

    public void setWsItemEmpleado(WsItemEmpleado wsItemEmpleado) {
        this.wsItemEmpleado = wsItemEmpleado;
    }

    public WsItemSucursal getWsItemSucursal() {
        return wsItemSucursal;
    }

    public void setWsItemSucursal(WsItemSucursal wsItemSucursal) {
        this.wsItemSucursal = wsItemSucursal;
    }
    
}
