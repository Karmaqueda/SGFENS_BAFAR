/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

import java.io.Serializable;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 12-may-2013 
 */
public class WsItemGeocerca implements Serializable{
    
    private int idGeocerca;
    private int idEmpresa;
    
    /**
     * Indica el tipo de Geocerca
     * <p>
     *  1- Círculo,
     *  2- Rectángulo,
     *  3- Polígono
     */
    private int tipoGeocerca;
    
    /**
     * Indica el conjunto de coordenadas o datos 
     * necesarios para señalar una geocerca.
     * <p>
     * Si el tipo de Geocerca es 1 (Círculo), los datos serán:
     *  radio, latitud de centro, longitud de centro
     * <p>
     * Si el tipo de Geocerca es 2 (Rectángulo), los datos serán:
     *   latitud vértice inferior izquierdo, longitud vértice inferior izquierdo,
     *   latitud vértice superior derecho, longitud vértice superior derecho.
     * <p>
     * Si el tipo de Geocerca es 3 (Polígono), los datos serán:
     *    Múltiples parejas de coordenadas para cada vértice[latitud vértice, longitud vértice]
     * 
     */
    private String coordenadas;
    private int idEstatus;

    
    /**
     * Indica el conjunto de coordenadas o datos 
     * necesarios para señalar una geocerca.
     * <p>
     * Si el tipo de Geocerca es 1 (Círculo), los datos serán:
     *  radio, latitud de centro, longitud de centro
     * <p>
     * Si el tipo de Geocerca es 2 (Rectángulo), los datos serán:
     *   latitud vértice inferior izquierdo, longitud vértice inferior izquierdo,
     *   latitud vértice superior derecho, longitud vértice superior derecho.
     * <p>
     * Si el tipo de Geocerca es 3 (Polígono), los datos serán:
     *    Múltiples parejas de coordenadas para cada vértice[latitud vértice, longitud vértice]
     * 
     */
    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
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

    public int getIdGeocerca() {
        return idGeocerca;
    }

    public void setIdGeocerca(int idGeocerca) {
        this.idGeocerca = idGeocerca;
    }

     /**
     * Indica el tipo de Geocerca
     * <p>
     *  1- Círculo,
     *  2- Rectángulo,
     *  3- Polígono
     */
    public int getTipoGeocerca() {
        return tipoGeocerca;
    }

    public void setTipoGeocerca(int tipoGeocerca) {
        this.tipoGeocerca = tipoGeocerca;
    }
    

}
