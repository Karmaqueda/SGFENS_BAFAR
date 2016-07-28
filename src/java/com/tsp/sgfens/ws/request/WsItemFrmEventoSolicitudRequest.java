/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.request;

import java.util.Date;

/**
 *
 * @author Ing. Roberto Trejo
 */
public class WsItemFrmEventoSolicitudRequest {
    protected int idEventoSolicitud;
    protected int idFormularioEvento;
    protected int idProductoCredito;
    protected int idEstadoSolicitud;
    protected Date fechaHrCreacion;
    protected String sapBp;
    protected String sapNoContratoM;
    protected Date sapFechaApertura;
    protected Date sapFechaAmortizacion;
    protected String sapInfPlazoContrato;
    protected String sapInfFechaCorte;
    protected String sapInfFechaPago;
    protected String sapTablaAmortizacion;

    /**
     * @return the idEventoSolicitud
     */
    public int getIdEventoSolicitud() {
        return idEventoSolicitud;
    }

    /**
     * @param idEventoSolicitud the idEventoSolicitud to set
     */
    public void setIdEventoSolicitud(int idEventoSolicitud) {
        this.idEventoSolicitud = idEventoSolicitud;
    }

    /**
     * @return the idFormularioEvento
     */
    public int getIdFormularioEvento() {
        return idFormularioEvento;
    }

    /**
     * @param idFormularioEvento the idFormularioEvento to set
     */
    public void setIdFormularioEvento(int idFormularioEvento) {
        this.idFormularioEvento = idFormularioEvento;
    }

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
     * @return the sapBp
     */
    public String getSapBp() {
        return sapBp;
    }

    /**
     * @param sapBp the sapBp to set
     */
    public void setSapBp(String sapBp) {
        this.sapBp = sapBp;
    }

    /**
     * @return the sapNoContratoM
     */
    public String getSapNoContratoM() {
        return sapNoContratoM;
    }

    /**
     * @param sapNoContratoM the sapNoContratoM to set
     */
    public void setSapNoContratoM(String sapNoContratoM) {
        this.sapNoContratoM = sapNoContratoM;
    }

    /**
     * @return the sapFechaApertura
     */
    public Date getSapFechaApertura() {
        return sapFechaApertura;
    }

    /**
     * @param sapFechaApertura the sapFechaApertura to set
     */
    public void setSapFechaApertura(Date sapFechaApertura) {
        this.sapFechaApertura = sapFechaApertura;
    }

    /**
     * @return the sapFechaAmortizacion
     */
    public Date getSapFechaAmortizacion() {
        return sapFechaAmortizacion;
    }

    /**
     * @param sapFechaAmortizacion the sapFechaAmortizacion to set
     */
    public void setSapFechaAmortizacion(Date sapFechaAmortizacion) {
        this.sapFechaAmortizacion = sapFechaAmortizacion;
    }

    /**
     * @return the sapInfPlazoContrato
     */
    public String getSapInfPlazoContrato() {
        return sapInfPlazoContrato;
    }

    /**
     * @param sapInfPlazoContrato the sapInfPlazoContrato to set
     */
    public void setSapInfPlazoContrato(String sapInfPlazoContrato) {
        this.sapInfPlazoContrato = sapInfPlazoContrato;
    }

    /**
     * @return the sapInfFechaCorte
     */
    public String getSapInfFechaCorte() {
        return sapInfFechaCorte;
    }

    /**
     * @param sapInfFechaCorte the sapInfFechaCorte to set
     */
    public void setSapInfFechaCorte(String sapInfFechaCorte) {
        this.sapInfFechaCorte = sapInfFechaCorte;
    }

    /**
     * @return the sapInfFechaPago
     */
    public String getSapInfFechaPago() {
        return sapInfFechaPago;
    }

    /**
     * @param sapInfFechaPago the sapInfFechaPago to set
     */
    public void setSapInfFechaPago(String sapInfFechaPago) {
        this.sapInfFechaPago = sapInfFechaPago;
    }

    /**
     * @return the sapTablaAmortizacion
     */
    public String getSapTablaAmortizacion() {
        return sapTablaAmortizacion;
    }

    /**
     * @param sapTablaAmortizacion the sapTablaAmortizacion to set
     */
    public void setSapTablaAmortizacion(String sapTablaAmortizacion) {
        this.sapTablaAmortizacion = sapTablaAmortizacion;
    }

    /**
     * @return the idEstadoSolicitud
     */
    public int getIdEstadoSolicitud() {
        return idEstadoSolicitud;
    }

    /**
     * @param idEstadoSolicitud the idEstadoSolicitud to set
     */
    public void setIdEstadoSolicitud(int idEstadoSolicitud) {
        this.idEstadoSolicitud = idEstadoSolicitud;
    }
}
