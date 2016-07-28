/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ing. Roberto Trejo
 */
public class WsItemProductoCredito {
    
    protected int idProductoCredito;
    protected int idProductoCreditoPadre;
    protected String nombre;
    protected String descripcion;
    protected Date fechaHrCreacion;
    protected Date fechaHrUltimaEdicion;
    protected int idUsuarioEdicion;
    protected int idScore;
    protected int idGrupoFormularioSolic;
    protected int idGrupoFormularioVerif;
    protected String tipoAmortizacion;
    protected double monto;
    protected double plazo;
    protected double tazaInteresAnual;
    protected double tazaInteresMora;
    protected double gatosCobranza;
    protected double costoAnualTotal;
    protected String garantiasDescripcion;
    protected int idEmpresa;
    protected int idEstatus;
    protected int idGrupoFormularioFotos;
    
    protected List<WsItemProductoReglaResponse> wsItemProductoRegla;
    protected List<WsItemProductoPuntejeMontoResponse> wsItemProductoPuntajeMonto;

    /**
     * @return the idProductoCredito
     */
    public int getIdProductoCredito() {
        return idProductoCredito;
    }

    /**
     * @param idProductoCredito the idProductoCredito to set
     */
    public void setIdProductoCredito(int idProductoCredito) {
        this.idProductoCredito = idProductoCredito;
    }

    /**
     * @return the idProductoCreditoPadre
     */
    public int getIdProductoCreditoPadre() {
        return idProductoCreditoPadre;
    }

    /**
     * @param idProductoCreditoPadre the idProductoCreditoPadre to set
     */
    public void setIdProductoCreditoPadre(int idProductoCreditoPadre) {
        this.idProductoCreditoPadre = idProductoCreditoPadre;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the fechaHrCreacion
     */
    public Date getFechaHrCreacion() {
        return fechaHrCreacion;
    }

    /**
     * @param fechaHrCreacion the fechaHrCreacion to set
     */
    public void setFechaHrCreacion(Date fechaHrCreacion) {
        this.fechaHrCreacion = fechaHrCreacion;
    }

    /**
     * @return the fechaHrUltimaEdicion
     */
    public Date getFechaHrUltimaEdicion() {
        return fechaHrUltimaEdicion;
    }

    /**
     * @param fechaHrUltimaEdicion the fechaHrUltimaEdicion to set
     */
    public void setFechaHrUltimaEdicion(Date fechaHrUltimaEdicion) {
        this.fechaHrUltimaEdicion = fechaHrUltimaEdicion;
    }

    /**
     * @return the idUsuarioEdicion
     */
    public int getIdUsuarioEdicion() {
        return idUsuarioEdicion;
    }

    /**
     * @param idUsuarioEdicion the idUsuarioEdicion to set
     */
    public void setIdUsuarioEdicion(int idUsuarioEdicion) {
        this.idUsuarioEdicion = idUsuarioEdicion;
    }

    /**
     * @return the idScore
     */
    public int getIdScore() {
        return idScore;
    }

    /**
     * @param idScore the idScore to set
     */
    public void setIdScore(int idScore) {
        this.idScore = idScore;
    }

    /**
     * @return the idGrupoFormularioSolic
     */
    public int getIdGrupoFormularioSolic() {
        return idGrupoFormularioSolic;
    }

    /**
     * @param idGrupoFormularioSolic the idGrupoFormularioSolic to set
     */
    public void setIdGrupoFormularioSolic(int idGrupoFormularioSolic) {
        this.idGrupoFormularioSolic = idGrupoFormularioSolic;
    }

    /**
     * @return the idGrupoFormularioVerif
     */
    public int getIdGrupoFormularioVerif() {
        return idGrupoFormularioVerif;
    }

    /**
     * @param idGrupoFormularioVerif the idGrupoFormularioVerif to set
     */
    public void setIdGrupoFormularioVerif(int idGrupoFormularioVerif) {
        this.idGrupoFormularioVerif = idGrupoFormularioVerif;
    }

    /**
     * @return the tipoAmortizacion
     */
    public String getTipoAmortizacion() {
        return tipoAmortizacion;
    }

    /**
     * @param tipoAmortizacion the tipoAmortizacion to set
     */
    public void setTipoAmortizacion(String tipoAmortizacion) {
        this.tipoAmortizacion = tipoAmortizacion;
    }

    /**
     * @return the monto
     */
    public double getMonto() {
        return monto;
    }

    /**
     * @param monto the monto to set
     */
    public void setMonto(double monto) {
        this.monto = monto;
    }

    /**
     * @return the plazo
     */
    public double getPlazo() {
        return plazo;
    }

    /**
     * @param plazo the plazo to set
     */
    public void setPlazo(double plazo) {
        this.plazo = plazo;
    }

    /**
     * @return the tazaInteresAnual
     */
    public double getTazaInteresAnual() {
        return tazaInteresAnual;
    }

    /**
     * @param tazaInteresAnual the tazaInteresAnual to set
     */
    public void setTazaInteresAnual(double tazaInteresAnual) {
        this.tazaInteresAnual = tazaInteresAnual;
    }

    /**
     * @return the tazaInteresMora
     */
    public double getTazaInteresMora() {
        return tazaInteresMora;
    }

    /**
     * @param tazaInteresMora the tazaInteresMora to set
     */
    public void setTazaInteresMora(double tazaInteresMora) {
        this.tazaInteresMora = tazaInteresMora;
    }

    /**
     * @return the gatosCobranza
     */
    public double getGatosCobranza() {
        return gatosCobranza;
    }

    /**
     * @param gatosCobranza the gatosCobranza to set
     */
    public void setGatosCobranza(double gatosCobranza) {
        this.gatosCobranza = gatosCobranza;
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
     * @return the wsItemProductoRegla
     */
    public List<WsItemProductoReglaResponse> getWsItemProductoRegla() {
        if(wsItemProductoRegla == null)
            wsItemProductoRegla = new ArrayList<WsItemProductoReglaResponse>();
        return wsItemProductoRegla;
    }

    /**
     * @param wsItemProductoRegla the wsItemProductoRegla to set
     */
    public void setWsItemProductoRegla(List<WsItemProductoReglaResponse> wsItemProductoRegla) {
        this.wsItemProductoRegla = wsItemProductoRegla;
    }

    /**
     * @return the wsItemProductoPuntajeMonto
     */
    public List<WsItemProductoPuntejeMontoResponse> getWsItemProductoPuntajeMonto() {
        if(wsItemProductoPuntajeMonto == null)
            wsItemProductoPuntajeMonto = new ArrayList<WsItemProductoPuntejeMontoResponse>();
        return wsItemProductoPuntajeMonto;
    }

    /**
     * @param wsItemProductoPuntajeMonto the wsItemProductoPuntajeMonto to set
     */
    public void setWsItemProductoPuntajeMonto(List<WsItemProductoPuntejeMontoResponse> wsItemProductoPuntajeMonto) {
        this.wsItemProductoPuntajeMonto = wsItemProductoPuntajeMonto;
    }

    /**
     * @return the costoAnualTotal
     */
    public double getCostoAnualTotal() {
        return costoAnualTotal;
    }

    /**
     * @param costoAnualTotal the costoAnualTotal to set
     */
    public void setCostoAnualTotal(double costoAnualTotal) {
        this.costoAnualTotal = costoAnualTotal;
    }

    /**
     * @return the garantiasDescripcion
     */
    public String getGarantiasDescripcion() {
        return garantiasDescripcion;
    }

    /**
     * @param garantiasDescripcion the garantiasDescripcion to set
     */
    public void setGarantiasDescripcion(String garantiasDescripcion) {
        this.garantiasDescripcion = garantiasDescripcion;
    }

    /**
     * @return the idGrupoFormularioFotos
     */
    public int getIdGrupoFormularioFotos() {
        return idGrupoFormularioFotos;
    }

    /**
     * @param idGrupoFormularioFotos the idGrupoFormularioFotos to set
     */
    public void setIdGrupoFormularioFotos(int idGrupoFormularioFotos) {
        this.idGrupoFormularioFotos = idGrupoFormularioFotos;
    }
    
}
