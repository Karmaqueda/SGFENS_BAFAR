/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.io.Serializable;

/**
 *
 * @author ISCesar poseidon24@hotmail.com
 * @since 7/10/2014 01:42:08 PM
 */
public class WsItemBancoOperacion implements Serializable{

    private int idOperacionBancaria;
    private String noTarjeta;
    private String nombreTitular;
    private double monto;
    private String bancoAuth;
    private String bancoOrderId;
    private String bancoOperFecha;
    private String bancoOperType;
    private String bancoOperIssuingBank;
    private int idEstatus;
    
    protected String dataArqc;
    protected String dataAid;
    protected String dataTsi;
    protected String dataRef;
    protected String dataExtra1;
    protected String dataExtra2;

    public int getIdOperacionBancaria() {
        return idOperacionBancaria;
    }

    public void setIdOperacionBancaria(int idOperacionBancaria) {
        this.idOperacionBancaria = idOperacionBancaria;
    }

    public String getNoTarjeta() {
        return noTarjeta;
    }

    public void setNoTarjeta(String noTarjeta) {
        this.noTarjeta = noTarjeta;
    }

    public String getNombreTitular() {
        return nombreTitular;
    }

    public void setNombreTitular(String nombreTitular) {
        this.nombreTitular = nombreTitular;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getBancoAuth() {
        return bancoAuth;
    }

    public void setBancoAuth(String bancoAuth) {
        this.bancoAuth = bancoAuth;
    }

    public String getBancoOrderId() {
        return bancoOrderId;
    }

    public void setBancoOrderId(String bancoOrderId) {
        this.bancoOrderId = bancoOrderId;
    }

    public String getBancoOperFecha() {
        return bancoOperFecha;
    }

    public void setBancoOperFecha(String bancoOperFecha) {
        this.bancoOperFecha = bancoOperFecha;
    }

    public String getBancoOperType() {
        return bancoOperType;
    }

    public void setBancoOperType(String bancoOperType) {
        this.bancoOperType = bancoOperType;
    }

    public String getBancoOperIssuingBank() {
        return bancoOperIssuingBank;
    }

    public void setBancoOperIssuingBank(String bancoOperIssuingBank) {
        this.bancoOperIssuingBank = bancoOperIssuingBank;
    }

    public int getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(int idEstatus) {
        this.idEstatus = idEstatus;
    }

    public String getDataArqc() {
        return dataArqc;
    }

    public void setDataArqc(String dataArqc) {
        this.dataArqc = dataArqc;
    }

    public String getDataAid() {
        return dataAid;
    }

    public void setDataAid(String dataAid) {
        this.dataAid = dataAid;
    }

    public String getDataTsi() {
        return dataTsi;
    }

    public void setDataTsi(String dataTsi) {
        this.dataTsi = dataTsi;
    }

    public String getDataRef() {
        return dataRef;
    }

    public void setDataRef(String dataRef) {
        this.dataRef = dataRef;
    }

    public String getDataExtra1() {
        return dataExtra1;
    }

    public void setDataExtra1(String dataExtra1) {
        this.dataExtra1 = dataExtra1;
    }

    public String getDataExtra2() {
        return dataExtra2;
    }

    public void setDataExtra2(String dataExtra2) {
        this.dataExtra2 = dataExtra2;
    }

}
