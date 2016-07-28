/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ISCesarMartinez
 */
public class ConsultaEmpleadosResponse extends WSResponse{
    
    private List<WsItemEmpleado> wsItemEmpleado;

    public List<WsItemEmpleado> getWsItemEmpleado() {
        if (wsItemEmpleado==null)
            wsItemEmpleado = new ArrayList<WsItemEmpleado>();
        return wsItemEmpleado;
    }

    public void setWsItemEmpleado(List<WsItemEmpleado> wsItemEmpleado) {
        this.wsItemEmpleado = wsItemEmpleado;
    }    
    
}
