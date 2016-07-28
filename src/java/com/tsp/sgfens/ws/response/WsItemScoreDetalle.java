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
public class WsItemScoreDetalle {
    
    protected int idScoreDetalle;
    protected int idFormularioCampo;
    protected int idScore;
    protected String valorExacto;
    protected double rangoMin;
    protected double rangoMax;
    protected int puntosScore;
    protected int idEmpresa;
    protected int idEstatus;

    /**
     * @return the idScoreDetalle
     */
    public int getIdScoreDetalle() {
        return idScoreDetalle;
    }

    /**
     * @param idScoreDetalle the idScoreDetalle to set
     */
    public void setIdScoreDetalle(int idScoreDetalle) {
        this.idScoreDetalle = idScoreDetalle;
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
     * @return the valorExacto
     */
    public String getValorExacto() {
        return valorExacto;
    }

    /**
     * @param valorExacto the valorExacto to set
     */
    public void setValorExacto(String valorExacto) {
        this.valorExacto = valorExacto;
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
     * @return the puntosScore
     */
    public int getPuntosScore() {
        return puntosScore;
    }

    /**
     * @param puntosScore the puntosScore to set
     */
    public void setPuntosScore(int puntosScore) {
        this.puntosScore = puntosScore;
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
