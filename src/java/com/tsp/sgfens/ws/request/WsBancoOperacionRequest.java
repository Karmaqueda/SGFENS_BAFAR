package com.tsp.sgfens.ws.request;

import java.io.Serializable;

public class WsBancoOperacionRequest implements Serializable {

    //protected int idOperacionBancaria;
    protected int idEmpresa;
    protected String noTarjeta;
    protected String nombreTitular;
    protected double monto;
    protected String bancoAuth;
    protected String bancoOrderId;
    protected String bancoOperFecha;
    protected String bancoOperType;
    protected String bancoOperIssuingBank;
    protected int idEstatus;
    protected String imagenIFEBytesBase64;
    protected String imagenTDCBytesBase64;
    
    //Atributos no aplicables para guardar en BD, solo temporales en transacción
    protected String mesExpiracion;
    protected String anioExpiracion;
    protected String cvv;
    
    protected String dataArqc;
    protected String dataAid;
    protected String dataTsi;
    protected String dataRef;
    protected String dataExtra1;
    protected String dataExtra2;

    public WsBancoOperacionRequest() {
    }

    public String getBancoAuth() {
        return bancoAuth;
    }

    public void setBancoAuth(String bancoAuth) {
        this.bancoAuth = bancoAuth;
    }

    public String getBancoOperFecha() {
        return bancoOperFecha;
    }

    public void setBancoOperFecha(String bancoOperFecha) {
        this.bancoOperFecha = bancoOperFecha;
    }

    public String getBancoOperIssuingBank() {
        return bancoOperIssuingBank;
    }

    public void setBancoOperIssuingBank(String bancoOperIssuingBank) {
        this.bancoOperIssuingBank = bancoOperIssuingBank;
    }

    public String getBancoOperType() {
        return bancoOperType;
    }

    public void setBancoOperType(String bancoOperType) {
        this.bancoOperType = bancoOperType;
    }

    public String getBancoOrderId() {
        return bancoOrderId;
    }

    public void setBancoOrderId(String bancoOrderId) {
        this.bancoOrderId = bancoOrderId;
    }

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

    /*
     * public int getIdOperacionBancaria() {
     * return idOperacionBancaria;
     * }
     *
     * public void setIdOperacionBancaria(int idOperacionBancaria) {
     * this.idOperacionBancaria = idOperacionBancaria;
     * }
     *
     */
    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
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

    public String getImagenIFEBytesBase64() {
        return imagenIFEBytesBase64;
    }

    public void setImagenIFEBytesBase64(String imagenIFEBytesBase64) {
        this.imagenIFEBytesBase64 = imagenIFEBytesBase64;
    }

    public String getImagenTDCBytesBase64() {
        return imagenTDCBytesBase64;
    }

    public void setImagenTDCBytesBase64(String imagenTDCBytesBase64) {
        this.imagenTDCBytesBase64 = imagenTDCBytesBase64;
    }

    public String getMesExpiracion() {
        return mesExpiracion;
    }

    public void setMesExpiracion(String mesExpiracion) {
        this.mesExpiracion = mesExpiracion;
    }

    public String getAnioExpiracion() {
        return anioExpiracion;
    }

    public void setAnioExpiracion(String anioExpiracion) {
        this.anioExpiracion = anioExpiracion;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
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
