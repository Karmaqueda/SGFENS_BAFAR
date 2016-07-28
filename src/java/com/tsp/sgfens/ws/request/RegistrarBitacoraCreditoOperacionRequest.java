/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.request;

import java.util.Date;

/**
 *
 * @author ISCesar poseidon24@hotmail.com
 * @since 13/05/2015 13/05/2015 06:43:33 PM
 */
public class RegistrarBitacoraCreditoOperacionRequest {

    private int idEmpresa;
    private int idEstatus;
    private int tipo;
    private int idUserRegistra;
    private int cantidad;
    private Date fechaHora;
    private int idCliente;
    private int idProspecto;
    private double montoOperacion;
    private String comentarios;
    private String folioMovil;

    /**
     * Datos de la operación bancaria, en caso de no existir sera nulo.
     *
     * Dentro del objeto, los datos idEmpresa e idEstatus no se llenan
     */
    private WsBancoOperacionRequest bancoOperacion;

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public int getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(int idEstatus) {
        this.idEstatus = idEstatus;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getIdUserRegistra() {
        return idUserRegistra;
    }

    public void setIdUserRegistra(int idUserRegistra) {
        this.idUserRegistra = idUserRegistra;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdProspecto() {
        return idProspecto;
    }

    public void setIdProspecto(int idProspecto) {
        this.idProspecto = idProspecto;
    }

    public double getMontoOperacion() {
        return montoOperacion;
    }

    public void setMontoOperacion(double montoOperacion) {
        this.montoOperacion = montoOperacion;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getFolioMovil() {
        return folioMovil;
    }

    public void setFolioMovil(String folioMovil) {
        this.folioMovil = folioMovil;
    }

    public WsBancoOperacionRequest getBancoOperacion() {
        return bancoOperacion;
    }

    public void setBancoOperacion(WsBancoOperacionRequest bancoOperacion) {
        this.bancoOperacion = bancoOperacion;
    }

    
    
}
