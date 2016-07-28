/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.cr;

import java.util.Date;

/**
 *
 * @author ISCesar
 */
public class CrCreaInterlocutorSapBafarResponse extends CrSapBafarResponse {
    
    private String businessPartner;
    private String noContrato;
    private CrTablaAmortizacion crTablaAmortizacion;
    
    private Date fechaSolicitud;
    private Date fechaSuscripcion;
    private Date fechaInicioCredito;
    private double montoSolicitado; 
    private double montoAprobado;
    private int noCuotasPlazo;
    private CrUtilCalculos.TipoPlazo tipoPlazo;
    private Date fechaPrimerPago;
    private Date fechaFinCredito;
    private double porcentajeTasaOrdinaria;
    private double importePrimerCuota;
    private int plazoMeses;
    private double cuotaRegular;
    private double montoTotalPagar;
    
    public String getBusinessPartner() {
        return businessPartner;
    }

    public void setBusinessPartner(String businessPartner) {
        this.businessPartner = businessPartner;
    }

    public String getNoContrato() {
        return noContrato;
    }

    public void setNoContrato(String noContrato) {
        this.noContrato = noContrato;
    }

    public CrTablaAmortizacion getCrTablaAmortizacion() {
        return crTablaAmortizacion;
    }

    public void setCrTablaAmortizacion(CrTablaAmortizacion crTablaAmortizacion) {
        this.crTablaAmortizacion = crTablaAmortizacion;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Date getFechaSuscripcion() {
        return fechaSuscripcion;
    }

    public void setFechaSuscripcion(Date fechaSuscripcion) {
        this.fechaSuscripcion = fechaSuscripcion;
    }

    public Date getFechaInicioCredito() {
        return fechaInicioCredito;
    }

    public void setFechaInicioCredito(Date fechaInicioCredito) {
        this.fechaInicioCredito = fechaInicioCredito;
    }

    public double getMontoSolicitado() {
        return montoSolicitado;
    }

    public void setMontoSolicitado(double montoSolicitado) {
        this.montoSolicitado = montoSolicitado;
    }

    public double getMontoAprobado() {
        return montoAprobado;
    }

    public void setMontoAprobado(double montoAprobado) {
        this.montoAprobado = montoAprobado;
    }

    public int getNoCuotasPlazo() {
        return noCuotasPlazo;
    }

    public void setNoCuotasPlazo(int noCuotasPlazo) {
        this.noCuotasPlazo = noCuotasPlazo;
    }

    public CrUtilCalculos.TipoPlazo getTipoPlazo() {
        return tipoPlazo;
    }

    public void setTipoPlazo(CrUtilCalculos.TipoPlazo tipoPlazo) {
        this.tipoPlazo = tipoPlazo;
    }

    public Date getFechaPrimerPago() {
        return fechaPrimerPago;
    }

    public void setFechaPrimerPago(Date fechaPrimerPago) {
        this.fechaPrimerPago = fechaPrimerPago;
    }

    public Date getFechaFinCredito() {
        return fechaFinCredito;
    }

    public void setFechaFinCredito(Date fechaFinCredito) {
        this.fechaFinCredito = fechaFinCredito;
    }

    public double getPorcentajeTasaOrdinaria() {
        return porcentajeTasaOrdinaria;
    }

    public void setPorcentajeTasaOrdinaria(double porcentajeTasaOrdinaria) {
        this.porcentajeTasaOrdinaria = porcentajeTasaOrdinaria;
    }

    public double getImportePrimerCuota() {
        return importePrimerCuota;
    }

    public void setImportePrimerCuota(double importePrimerCuota) {
        this.importePrimerCuota = importePrimerCuota;
    }

    public int getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(int plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public double getCuotaRegular() {
        return cuotaRegular;
    }

    public void setCuotaRegular(double cuotaRegular) {
        this.cuotaRegular = cuotaRegular;
    }

    public double getMontoTotalPagar() {
        return montoTotalPagar;
    }

    public void setMontoTotalPagar(double montoTotalPagar) {
        this.montoTotalPagar = montoTotalPagar;
    }
    
}
