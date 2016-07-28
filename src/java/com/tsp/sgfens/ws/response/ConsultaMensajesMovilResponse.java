/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.util.List;

/**
 *
 * @author ISCesarMartinez
 */
public class ConsultaMensajesMovilResponse extends WSResponse{
      
    private boolean reporteRobo = false;
    private List<WsItemMovilMensaje> wsItemMovilMensajes;
    private int creditosOperacionDisponibles;

    public List<WsItemMovilMensaje> getWsItemMovilMensajes() {
        return wsItemMovilMensajes;
    }

    public void setWsItemMovilMensajes(List<WsItemMovilMensaje> wsItemMovilMensajes) {
        this.wsItemMovilMensajes = wsItemMovilMensajes;
    }
    
    public boolean isReporteRobo() {
        return reporteRobo;
    }

    public void setReporteRobo(boolean reporteRobo) {
        this.reporteRobo = reporteRobo;
    }

    public int getCreditosOperacionDisponibles() {
        return creditosOperacionDisponibles;
    }

    public void setCreditosOperacionDisponibles(int creditosOperacionDisponibles) {
        this.creditosOperacionDisponibles = creditosOperacionDisponibles;
    }
    
}
