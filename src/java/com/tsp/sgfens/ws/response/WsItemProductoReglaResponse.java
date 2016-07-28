/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

/**
 *
 * @author Ing. Roberto Trejo
 */
public class WsItemProductoReglaResponse {
    
    protected int idProductoRegla;
    protected int idProductoCredito;
    protected String etiqueta;
    protected double rangoMin;
    protected double rangoMax;
    protected double valorExacto;
    protected int idFormularioCampo;
    protected String etiquetaCampoRelacion;
    protected int isReglaAplicacionScore;
    protected int isReglaRechazo;
    protected String claveTipoRegla;
    protected int idEmpresa;
    protected int idEstatus;

    /**
     * @return the idProductoRegla
     */
    public int getIdProductoRegla() {
        return idProductoRegla;
    }

    /**
     * @param idProductoRegla the idProductoRegla to set
     */
    public void setIdProductoRegla(int idProductoRegla) {
        this.idProductoRegla = idProductoRegla;
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
     * @return the etiqueta
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * @param etiqueta the etiqueta to set
     */
    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /**
     * @return the rangoMin
     */
    public double getRangoMin() {
        return rangoMin;
    }

    /**
     * @param rangoMin the rangoMin to set
     */
    public void setRangoMin(double rangoMin) {
        this.rangoMin = rangoMin;
    }

    /**
     * @return the rangoMax
     */
    public double getRangoMax() {
        return rangoMax;
    }

    /**
     * @param rangoMax the rangoMax to set
     */
    public void setRangoMax(double rangoMax) {
        this.rangoMax = rangoMax;
    }

    /**
     * @return the valorExacto
     */
    public double getValorExacto() {
        return valorExacto;
    }

    /**
     * @param valorExacto the valorExacto to set
     */
    public void setValorExacto(double valorExacto) {
        this.valorExacto = valorExacto;
    }

    /**
     * @return the idFormularioCampo
     */
    public int getIdFormularioCampo() {
        return idFormularioCampo;
    }

    /**
     * @param idFormularioCampo the idFormularioCampo to set
     */
    public void setIdFormularioCampo(int idFormularioCampo) {
        this.idFormularioCampo = idFormularioCampo;
    }

    /**
     * @return the etiquetaCampoRelacion
     */
    public String getEtiquetaCampoRelacion() {
        return etiquetaCampoRelacion;
    }

    /**
     * @param etiquetaCampoRelacion the etiquetaCampoRelacion to set
     */
    public void setEtiquetaCampoRelacion(String etiquetaCampoRelacion) {
        this.etiquetaCampoRelacion = etiquetaCampoRelacion;
    }

    /**
     * @return the isReglaAplicacionScore
     */
    public int getIsReglaAplicacionScore() {
        return isReglaAplicacionScore;
    }

    /**
     * @param isReglaAplicacionScore the isReglaAplicacionScore to set
     */
    public void setIsReglaAplicacionScore(int isReglaAplicacionScore) {
        this.isReglaAplicacionScore = isReglaAplicacionScore;
    }

    /**
     * @return the isReglaRechazo
     */
    public int getIsReglaRechazo() {
        return isReglaRechazo;
    }

    /**
     * @param isReglaRechazo the isReglaRechazo to set
     */
    public void setIsReglaRechazo(int isReglaRechazo) {
        this.isReglaRechazo = isReglaRechazo;
    }

    /**
     * @return the claveTipoRegla
     */
    public String getClaveTipoRegla() {
        return claveTipoRegla;
    }

    /**
     * @param claveTipoRegla the claveTipoRegla to set
     */
    public void setClaveTipoRegla(String claveTipoRegla) {
        this.claveTipoRegla = claveTipoRegla;
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
    
}
