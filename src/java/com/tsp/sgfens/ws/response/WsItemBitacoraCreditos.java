/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Ing. Roberto Trejo
 */
public class WsItemBitacoraCreditos implements Serializable{
    
    private int idBitacoraCreditosOperacion;
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
     * @return the idBitacoraCreditosOperacion
     */
    public int getIdBitacoraCreditosOperacion() {
        return idBitacoraCreditosOperacion;
    }

    /**
     * @param idBitacoraCreditosOperacion the idBitacoraCreditosOperacion to set
     */
    public void setIdBitacoraCreditosOperacion(int idBitacoraCreditosOperacion) {
        this.idBitacoraCreditosOperacion = idBitacoraCreditosOperacion;
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
     * @return the idEstatus
     */
    public int getIdEstatus() {
        return idEstatus;
    }

    /**
     * @param idEstatus the idEstatus to set
     */
    public void setIdEstatus(int idEstatus) {
        this.idEstatus = idEstatus;
    }

    /**
     * @return the tipo
     */
    public int getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the idUserRegistra
     */
    public int getIdUserRegistra() {
        return idUserRegistra;
    }

    /**
     * @param idUserRegistra the idUserRegistra to set
     */
    public void setIdUserRegistra(int idUserRegistra) {
        this.idUserRegistra = idUserRegistra;
    }

    /**
     * @return the cantidad
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
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
     * @return the idProspecto
     */
    public int getIdProspecto() {
        return idProspecto;
    }

    /**
     * @param idProspecto the idProspecto to set
     */
    public void setIdProspecto(int idProspecto) {
        this.idProspecto = idProspecto;
    }

    /**
     * @return the montoOperacion
     */
    public double getMontoOperacion() {
        return montoOperacion;
    }

    /**
     * @param montoOperacion the montoOperacion to set
     */
    public void setMontoOperacion(double montoOperacion) {
        this.montoOperacion = montoOperacion;
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
     * @return the folioMovil
     */
    public String getFolioMovil() {
        return folioMovil;
    }

    /**
     * @param folioMovil the folioMovil to set
     */
    public void setFolioMovil(String folioMovil) {
        this.folioMovil = folioMovil;
    }
    
}
