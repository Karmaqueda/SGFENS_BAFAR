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
public class WsItemFormularioCampo implements Serializable {
    
    protected int idFormularioCampo;
    protected int idFormulario;
    protected int idTipoCampo;
    protected int ordenFormulario;
    protected int noSeccion;
    protected String etiqueta;
    protected String descripcion;
    protected String valorDefecto;
    protected String valorSugerencia;
    protected String opciones;
    protected int isRequerido;
    protected int idFormularioValidacion;
    protected String variableFormula;
    protected int idEstatus;
    protected int idEmpresa;

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
     * @return the idFormulario
     */
    public int getIdFormulario() {
        return idFormulario;
    }

    /**
     * @param idFormulario the idFormulario to set
     */
    public void setIdFormulario(int idFormulario) {
        this.idFormulario = idFormulario;
    }

    /**
     * @return the idTipoCampo
     */
    public int getIdTipoCampo() {
        return idTipoCampo;
    }

    /**
     * @param idTipoCampo the idTipoCampo to set
     */
    public void setIdTipoCampo(int idTipoCampo) {
        this.idTipoCampo = idTipoCampo;
    }

    /**
     * @return the ordenFormulario
     */
    public int getOrdenFormulario() {
        return ordenFormulario;
    }

    /**
     * @param ordenFormulario the ordenFormulario to set
     */
    public void setOrdenFormulario(int ordenFormulario) {
        this.ordenFormulario = ordenFormulario;
    }

    /**
     * @return the noSeccion
     */
    public int getNoSeccion() {
        return noSeccion;
    }

    /**
     * @param noSeccion the noSeccion to set
     */
    public void setNoSeccion(int noSeccion) {
        this.noSeccion = noSeccion;
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
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the valorDefecto
     */
    public String getValorDefecto() {
        return valorDefecto;
    }

    /**
     * @param valorDefecto the valorDefecto to set
     */
    public void setValorDefecto(String valorDefecto) {
        this.valorDefecto = valorDefecto;
    }

    /**
     * @return the valorSugerencia
     */
    public String getValorSugerencia() {
        return valorSugerencia;
    }

    /**
     * @param valorSugerencia the valorSugerencia to set
     */
    public void setValorSugerencia(String valorSugerencia) {
        this.valorSugerencia = valorSugerencia;
    }

    /**
     * @return the opciones
     */
    public String getOpciones() {
        return opciones;
    }

    /**
     * @param opciones the opciones to set
     */
    public void setOpciones(String opciones) {
        this.opciones = opciones;
    }

    /**
     * @return the isRequerido
     */
    public int getIsRequerido() {
        return isRequerido;
    }

    /**
     * @param isRequerido the isRequerido to set
     */
    public void setIsRequerido(int isRequerido) {
        this.isRequerido = isRequerido;
    }

    /**
     * @return the idFormularioValidacion
     */
    public int getIdFormularioValidacion() {
        return idFormularioValidacion;
    }

    /**
     * @param idFormularioValidacion the idFormularioValidacion to set
     */
    public void setIdFormularioValidacion(int idFormularioValidacion) {
        this.idFormularioValidacion = idFormularioValidacion;
    }

    /**
     * @return the variableFormula
     */
    public String getVariableFormula() {
        return variableFormula;
    }

    /**
     * @param variableFormula the variableFormula to set
     */
    public void setVariableFormula(String variableFormula) {
        this.variableFormula = variableFormula;
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
    
}
