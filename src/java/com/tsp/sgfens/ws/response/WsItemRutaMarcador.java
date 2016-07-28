/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

import java.io.Serializable;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 28-mar-2013 
 */
public class WsItemRutaMarcador implements Serializable{
    
    protected int idRutaMarcador;
    protected int idRuta;
    protected String informacionMarcador;
    protected String latitudMarcador;
    protected String longitudMarcador;
    protected long idProspecto;
    protected long idCliente;
    protected short isVisitado;

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }

    public long getIdProspecto() {
        return idProspecto;
    }

    public void setIdProspecto(long idProspecto) {
        this.idProspecto = idProspecto;
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
    }

    public int getIdRutaMarcador() {
        return idRutaMarcador;
    }

    public void setIdRutaMarcador(int idRutaMarcador) {
        this.idRutaMarcador = idRutaMarcador;
    }

    public String getInformacionMarcador() {
        return informacionMarcador;
    }

    public void setInformacionMarcador(String informacionMarcador) {
        this.informacionMarcador = informacionMarcador;
    }

    public String getLatitudMarcador() {
        return latitudMarcador;
    }

    public void setLatitudMarcador(String latitudMarcador) {
        this.latitudMarcador = latitudMarcador;
    }

    public String getLongitudMarcador() {
        return longitudMarcador;
    }

    public void setLongitudMarcador(String longitudMarcador) {
        this.longitudMarcador = longitudMarcador;
    }

    public short getIsVisitado() {
        return isVisitado;
    }

    public void setIsVisitado(short isVisitado) {
        this.isVisitado = isVisitado;
    }

    /**
    * Method 'toString'
    * 
    * @return String
    */
    public String toString(){
            StringBuffer ret = new StringBuffer();
            ret.append( "com.tsp.microfinancieras.dto.RutaMarcador: " );
            ret.append( "idRutaMarcador=" + idRutaMarcador );
            ret.append( ", idRuta=" + idRuta );
            ret.append( ", informacionMarcador=" + informacionMarcador );
            ret.append( ", latitudMarcador=" + latitudMarcador );
            ret.append( ", longitudMarcador=" + longitudMarcador );
            ret.append( ", idProspecto=" + idProspecto );
            ret.append( ", idCliente=" + idCliente );
            ret.append( ", isVisitado=" + isVisitado );
            return ret.toString();
    }
}
