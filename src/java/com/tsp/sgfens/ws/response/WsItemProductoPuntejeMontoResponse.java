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
public class WsItemProductoPuntejeMontoResponse {
    
    protected int idProductoPuntajeMonto;
    protected int idProductoCredito;
    protected double rangoMin;
    protected double rangoMax;
    protected double pctAutorizado;
    protected int idEmpresa;
    protected int idEstatus;

    /**
     * @return the idProductoPuntajeMonto
     */
    public int getIdProductoPuntajeMonto() {
        return idProductoPuntajeMonto;
    }

    /**
     * @param idProductoPuntajeMonto the idProductoPuntajeMonto to set
     */
    public void setIdProductoPuntajeMonto(int idProductoPuntajeMonto) {
        this.idProductoPuntajeMonto = idProductoPuntajeMonto;
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
     * @return the pctAutorizado
     */
    public double getPctAutorizado() {
        return pctAutorizado;
    }

    /**
     * @param pctAutorizado the pctAutorizado to set
     */
    public void setPctAutorizado(double pctAutorizado) {
        this.pctAutorizado = pctAutorizado;
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
