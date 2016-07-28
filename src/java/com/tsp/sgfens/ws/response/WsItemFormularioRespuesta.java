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
public class WsItemFormularioRespuesta implements Serializable {
    
    protected int idFormularioRespuesta;
    protected int idFormularioEvento;
    protected int idFormulario;
    protected int idFormularioCampo;
    protected String valor;
    protected String descripcion;
    protected int idEmpresa;
    protected int idEstatus;
    protected int revisar;
    protected String revisarComentario;

    /**
     * @return the idFormularioRespuesta
     */
    public int getIdFormularioRespuesta() {
        return idFormularioRespuesta;
    }

    /**
     * @param idFormularioRespuesta the idFormularioRespuesta to set
     */
    public void setIdFormularioRespuesta(int idFormularioRespuesta) {
        this.idFormularioRespuesta = idFormularioRespuesta;
    }

    /**
     * @return the idFormularioEvento
     */
    public int getIdFormularioEvento() {
        return idFormularioEvento;
    }

    /**
     * @param idFormularioEvento the idFormularioEvento to set
     */
    public void setIdFormularioEvento(int idFormularioEvento) {
        this.idFormularioEvento = idFormularioEvento;
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
     * @return the valor
     */
    public String getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(String valor) {
        this.valor = valor;
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
     * @return the revisar
     */
    public int getRevisar() {
        return revisar;
    }

    /**
     * @param revisar the revisar to set
     */
    public void setRevisar(int revisar) {
        this.revisar = revisar;
    }

    /**
     * @return the revisarComentario
     */
    public String getRevisarComentario() {
        return revisarComentario;
    }

    /**
     * @param revisarComentario the revisarComentario to set
     */
    public void setRevisarComentario(String revisarComentario) {
        this.revisarComentario = revisarComentario;
    }
    
}
