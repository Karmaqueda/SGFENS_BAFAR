/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ISCesarMartinez
 */
public class WsItemMovilMensaje implements Serializable{
    
    private int idMovilMensaje;
    //Fechas en formato cadena 'dd/MM/yyyy'
    private Date fechaEmision;
    private Date fechaRecepcion;
    private boolean recibido;
    private String mensaje;
    private int emisorTipo;
    private int idEmpleadoEmisor;
    private int receptorTipo;
    private int idEmpleadoReceptor;

    public int getEmisorTipo() {
        return emisorTipo;
    }

    public void setEmisorTipo(int emisorTipo) {
        this.emisorTipo = emisorTipo;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(Date fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public int getIdEmpleadoEmisor() {
        return idEmpleadoEmisor;
    }

    public void setIdEmpleadoEmisor(int idEmpleadoEmisor) {
        this.idEmpleadoEmisor = idEmpleadoEmisor;
    }

    public int getIdEmpleadoReceptor() {
        return idEmpleadoReceptor;
    }

    public void setIdEmpleadoReceptor(int idEmpleadoReceptor) {
        this.idEmpleadoReceptor = idEmpleadoReceptor;
    }

    public int getIdMovilMensaje() {
        return idMovilMensaje;
    }

    public void setIdMovilMensaje(int idMovilMensaje) {
        this.idMovilMensaje = idMovilMensaje;
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

    public boolean isRecibido() {
        return recibido;
    }

    public void setRecibido(boolean recibido) {
        this.recibido = recibido;
    }    
    
}
