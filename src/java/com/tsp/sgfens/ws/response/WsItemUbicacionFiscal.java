/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

import java.io.Serializable;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 18-ene-2013 
 */
public class WsItemUbicacionFiscal implements Serializable{

        /** 
	 * This attribute maps to the column ID_UBICACION_FISCAL in the ubicacion_fiscal table.
	 */
	protected long idUbicacionFiscal;


	/** 
	 * This attribute maps to the column CALLE in the ubicacion_fiscal table.
	 */
	protected String calle;


	/** 
	 * This attribute maps to the column NUM_INT in the ubicacion_fiscal table.
	 */
	protected String numInt;


	/** 
	 * This attribute maps to the column NUM_EXT in the ubicacion_fiscal table.
	 */
	protected String numExt;


	/** 
	 * This attribute maps to the column COLONIA in the ubicacion_fiscal table.
	 */
	protected String colonia;


	/** 
	 * This attribute maps to the column CODIGO_POSTAL in the ubicacion_fiscal table.
	 */
	protected String codigoPostal;


	/** 
	 * This attribute maps to the column PAIS in the ubicacion_fiscal table.
	 */
	protected String pais;


	/** 
	 * This attribute maps to the column ESTADO in the ubicacion_fiscal table.
	 */
	protected String estado;



	/** 
	 * This attribute maps to the column MUNICIPIO in the ubicacion_fiscal table.
	 */
	protected String municipio;


	/** 
	 * This attribute maps to the column IDENTIFICADOR in the ubicacion_fiscal table.
	 */
	protected String identificador;

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public long getIdUbicacionFiscal() {
        return idUbicacionFiscal;
    }

    public void setIdUbicacionFiscal(long idUbicacionFiscal) {
        this.idUbicacionFiscal = idUbicacionFiscal;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getNumExt() {
        return numExt;
    }

    public void setNumExt(String numExt) {
        this.numExt = numExt;
    }

    public String getNumInt() {
        return numInt;
    }

    public void setNumInt(String numInt) {
        this.numInt = numInt;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
    
}
