/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.request;

/**
 *
 * @author ISCesarMartinez
 */
public class ItemMensajeMovilRequest {
    
    /**
     * Variable para poder rastrear en la respuesta
     * que mensajes si fueron recibidos satisfactoriamente
     * por el servidor
     */
    private long rastreadorMensaje;
    private String mensaje;
    private int idEmpleadoReceptor;
    private int receptorTipo;

    public int getIdEmpleadoReceptor() {
        return idEmpleadoReceptor;
    }

    public void setIdEmpleadoReceptor(int idEmpleadoReceptor) {
        this.idEmpleadoReceptor = idEmpleadoReceptor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getReceptorTipo() {
        return receptorTipo;
    }

    public void setReceptorTipo(int receptorTipo) {
        this.receptorTipo = receptorTipo;
    }

    public long getRastreadorMensaje() {
        return rastreadorMensaje;
    }

    public void setRastreadorMensaje(long rastreadorMensaje) {
        this.rastreadorMensaje = rastreadorMensaje;
    }
    
    
    
}
