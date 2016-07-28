/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.sms.response;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ISCesar
 */
public class WsItemDispositivoMovilSms implements Serializable {

    private int idSmsDispositivoMovil;
    private int idEstatus;
    private String alias;
    private String numeroCelular;
    private String imei;
    private String marca;
    private String modelo;
    private double pctPila;
    private Date fechaHrUltimaCom;
    private Date fechaHrUltimoEnvio;

    public int getIdSmsDispositivoMovil() {
        return idSmsDispositivoMovil;
    }

    public void setIdSmsDispositivoMovil(int idSmsDispositivoMovil) {
        this.idSmsDispositivoMovil = idSmsDispositivoMovil;
    }

    public int getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(int idEstatus) {
        this.idEstatus = idEstatus;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public double getPctPila() {
        return pctPila;
    }

    public void setPctPila(double pctPila) {
        this.pctPila = pctPila;
    }

    public Date getFechaHrUltimaCom() {
        return fechaHrUltimaCom;
    }

    public void setFechaHrUltimaCom(Date fechaHrUltimaCom) {
        this.fechaHrUltimaCom = fechaHrUltimaCom;
    }

    public Date getFechaHrUltimoEnvio() {
        return fechaHrUltimoEnvio;
    }

    public void setFechaHrUltimoEnvio(Date fechaHrUltimoEnvio) {
        this.fechaHrUltimoEnvio = fechaHrUltimoEnvio;
    }
    
    

}
