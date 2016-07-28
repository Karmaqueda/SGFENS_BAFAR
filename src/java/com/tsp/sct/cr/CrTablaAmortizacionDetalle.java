/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.cr;

import java.math.BigDecimal;

/**
 *
 * @author ISCesar
 */
public class CrTablaAmortizacionDetalle {

    private String noPago;
    private String fecha;
    private BigDecimal saldoCapIni;
    private BigDecimal intereses;
    private BigDecimal iva;
    private BigDecimal pagoEspecial;
    private BigDecimal pagoCapital;
    private BigDecimal comisiones;
    private BigDecimal totalPago;
    private BigDecimal saldoCapFin;

    public String getNoPago() {
        return noPago;
    }

    public void setNoPago(String noPago) {
        this.noPago = noPago;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getSaldoCapIni() {
        return saldoCapIni;
    }

    public void setSaldoCapIni(BigDecimal saldoCapIni) {
        this.saldoCapIni = saldoCapIni;
    }

    public BigDecimal getIntereses() {
        return intereses;
    }

    public void setIntereses(BigDecimal intereses) {
        this.intereses = intereses;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getPagoEspecial() {
        return pagoEspecial;
    }

    public void setPagoEspecial(BigDecimal pagoEspecial) {
        this.pagoEspecial = pagoEspecial;
    }

    public BigDecimal getPagoCapital() {
        return pagoCapital;
    }

    public void setPagoCapital(BigDecimal pagoCapital) {
        this.pagoCapital = pagoCapital;
    }

    public BigDecimal getComisiones() {
        return comisiones;
    }

    public void setComisiones(BigDecimal comisiones) {
        this.comisiones = comisiones;
    }

    public BigDecimal getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(BigDecimal totalPago) {
        this.totalPago = totalPago;
    }

    public BigDecimal getSaldoCapFin() {
        return saldoCapFin;
    }

    public void setSaldoCapFin(BigDecimal saldoCapFin) {
        this.saldoCapFin = saldoCapFin;
    }

}
