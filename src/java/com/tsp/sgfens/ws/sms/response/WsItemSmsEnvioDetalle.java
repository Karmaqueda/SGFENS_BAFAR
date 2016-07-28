/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.sms.response;

import java.util.Date;

/**
 *
 * @author ISCesar
 */
public class WsItemSmsEnvioDetalle {

    private int idSmsEnvioDetalle;
    private int idSmsEnvioLote;
    private Date fechaHrCreacion;
    private Date fechaHrEnvio;
    private int enviado;
    private int intentosFallidos;
    private String asunto;
    private String mensaje;
    private int heredarMensajeLote;
    private int numPartesSms;
    private String numeroCelular;
    private int destIdCliente;
    private int destIdProspecto;
    private int destIdEmpleado;
    private int destIdEmpresa;
    private int destIdSmsAgendaDest;
    private int idEstatus;
    private int idEmpresa;

    public int getIdSmsEnvioDetalle() {
        return idSmsEnvioDetalle;
    }

    public void setIdSmsEnvioDetalle(int idSmsEnvioDetalle) {
        this.idSmsEnvioDetalle = idSmsEnvioDetalle;
    }

    public int getIdSmsEnvioLote() {
        return idSmsEnvioLote;
    }

    public void setIdSmsEnvioLote(int idSmsEnvioLote) {
        this.idSmsEnvioLote = idSmsEnvioLote;
    }

    public Date getFechaHrCreacion() {
        return fechaHrCreacion;
    }

    public void setFechaHrCreacion(Date fechaHrCreacion) {
        this.fechaHrCreacion = fechaHrCreacion;
    }

    public Date getFechaHrEnvio() {
        return fechaHrEnvio;
    }

    public void setFechaHrEnvio(Date fechaHrEnvio) {
        this.fechaHrEnvio = fechaHrEnvio;
    }

    public int getEnviado() {
        return enviado;
    }

    public void setEnviado(int enviado) {
        this.enviado = enviado;
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getHeredarMensajeLote() {
        return heredarMensajeLote;
    }

    public void setHeredarMensajeLote(int heredarMensajeLote) {
        this.heredarMensajeLote = heredarMensajeLote;
    }

    public int getNumPartesSms() {
        return numPartesSms;
    }

    public void setNumPartesSms(int numPartesSms) {
        this.numPartesSms = numPartesSms;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
    }

    public int getDestIdCliente() {
        return destIdCliente;
    }

    public void setDestIdCliente(int destIdCliente) {
        this.destIdCliente = destIdCliente;
    }

    public int getDestIdProspecto() {
        return destIdProspecto;
    }

    public void setDestIdProspecto(int destIdProspecto) {
        this.destIdProspecto = destIdProspecto;
    }

    public int getDestIdEmpleado() {
        return destIdEmpleado;
    }

    public void setDestIdEmpleado(int destIdEmpleado) {
        this.destIdEmpleado = destIdEmpleado;
    }

    public int getDestIdEmpresa() {
        return destIdEmpresa;
    }

    public void setDestIdEmpresa(int destIdEmpresa) {
        this.destIdEmpresa = destIdEmpresa;
    }

    public int getDestIdSmsAgendaDest() {
        return destIdSmsAgendaDest;
    }

    public void setDestIdSmsAgendaDest(int destIdSmsAgendaDest) {
        this.destIdSmsAgendaDest = destIdSmsAgendaDest;
    }

    public int getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(int idEstatus) {
        this.idEstatus = idEstatus;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

}
