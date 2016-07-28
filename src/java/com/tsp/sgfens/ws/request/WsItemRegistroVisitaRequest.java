/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.request;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author MOVILPYME
 */
    public class WsItemRegistroVisitaRequest implements Serializable {
    protected int idRegistroVisita;
    protected int idRegistroVisitaServer;
    protected int idEmpresa;
    protected int idEmpleado;
    protected int idCliente;
    protected double longitud;
    protected double latitud;
    protected Date fechaHora;
    protected int idOpcion;
    protected String comentarios;

    /**
     * @return the idRegistroVisita
     */
    public int getIdRegistroVisita() {
        return idRegistroVisita;
    }

    /**
     * @param idRegistroVisita the idRegistroVisita to set
     */
    public void setIdRegistroVisita(int idRegistroVisita) {
        this.idRegistroVisita = idRegistroVisita;
    }

    /**
     * @return the idEmpresa
     */
    public int getIdEmpresa() {
        return idEmpresa;
    }

    /**
     * @param idEmpresa the idEmpresa to set
     */
    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    /**
     * @return the idEmpleado
     */
    public int getIdEmpleado() {
        return idEmpleado;
    }

    /**
     * @param idEmpleado the idEmpleado to set
     */
    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    /**
     * @return the idCliente
     */
    public int getIdCliente() {
        return idCliente;
    }

    /**
     * @param idCliente the idCliente to set
     */
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * @return the longitud
     */
    public double getLongitud() {
        return longitud;
    }

    /**
     * @param longitud the longitud to set
     */
    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    /**
     * @return the latitud
     */
    public double getLatitud() {
        return latitud;
    }

    /**
     * @param latitud the latitud to set
     */
    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    /**
     * @return the fechaHora
     */
    public Date getFechaHora() {
        return fechaHora;
    }

    /**
     * @param fechaHora the fechaHora to set
     */
    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    /**
     * @return the idOpcion
     */
    public int getIdOpcion() {
        return idOpcion;
    }

    /**
     * @param idOpcion the idOpcion to set
     */
    public void setIdOpcion(int idOpcion) {
        this.idOpcion = idOpcion;
    }

    /**
     * @return the comentarios
     */
    public String getComentarios() {
        return comentarios;
    }

    /**
     * @param comentarios the comentarios to set
     */
    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    /**
     * @return the idRegistroVisitaServer
     */
    public int getIdRegistroVisitaServer() {
        return idRegistroVisitaServer;
    }

    /**
     * @param idRegistroVisitaServer the idRegistroVisitaServer to set
     */
    public void setIdRegistroVisitaServer(int idRegistroVisitaServer) {
        this.idRegistroVisitaServer = idRegistroVisitaServer;
    }
    
}
