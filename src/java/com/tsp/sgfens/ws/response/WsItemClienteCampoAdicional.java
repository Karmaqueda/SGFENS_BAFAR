/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.io.Serializable;

/**
 *
 * @author Ing. Roberto Trejo
 */
public class WsItemClienteCampoAdicional implements Serializable {
    protected int idClienteCampo;
    protected int idEmpresa;
    protected int idEstatus;
    protected String labelNombre;
    protected int labelTipo;

    /**
     * @return the idClienteCampo
     */
    public int getIdClienteCampo() {
        return idClienteCampo;
    }

    /**
     * @param idClienteCampo the idClienteCampo to set
     */
    public void setIdClienteCampo(int idClienteCampo) {
        this.idClienteCampo = idClienteCampo;
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

    /**
     * @return the labelNombre
     */
    public String getLabelNombre() {
        return labelNombre;
    }

    /**
     * @param labelNombre the labelNombre to set
     */
    public void setLabelNombre(String labelNombre) {
        this.labelNombre = labelNombre;
    }

    /**
     * @return the labelTipo
     */
    public int getLabelTipo() {
        return labelTipo;
    }

    /**
     * @param labelTipo the labelTipo to set
     */
    public void setLabelTipo(int labelTipo) {
        this.labelTipo = labelTipo;
    }
}
