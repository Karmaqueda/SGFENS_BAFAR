/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.request;

/**
 *
 * @author ISCesar
 */
public class WsItemCrFormularioRespuestaRequest {
    
    protected int idFormulario;
    protected int idFormularioCampo;
    protected String valor;
    protected String descripcion;
    protected String imagen;
    protected int revisar;
    protected String revisarComentario;

    public int getIdFormulario() {
        return idFormulario;
    }

    public void setIdFormulario(int idFormulario) {
        this.idFormulario = idFormulario;
    }

    public int getIdFormularioCampo() {
        return idFormularioCampo;
    }

    public void setIdFormularioCampo(int idFormularioCampo) {
        this.idFormularioCampo = idFormularioCampo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the imagen
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * @param imagen the imagen to set
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
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
     * @param revisarComentario the revisarComentario to set
     */
    public void setRevisarComentario(String revisarComentario) {
        this.revisarComentario = revisarComentario;
    }

    /**
     * @return the revisarComentario
     */
    public String getRevisarComentario() {
        return revisarComentario;
    }
    
}
