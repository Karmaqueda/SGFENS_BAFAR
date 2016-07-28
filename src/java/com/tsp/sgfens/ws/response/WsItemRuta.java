/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 28-mar-2013 
 */
public class WsItemRuta implements Serializable{

    protected int idRuta;
    protected int idTipoRuta;
    protected String nombreRuta;
    protected String comentarioRuta;
    protected Date fhRegRuta;
    protected String recorridoRuta;
    protected long idEmpleado;
    protected int paradasRuta;
    protected List<WsItemRutaMarcador> wsItemRutaMarcador;
    protected String diasSemanaRuta;

    public String getComentarioRuta() {
        return comentarioRuta;
    }

    public void setComentarioRuta(String comentarioRuta) {
        this.comentarioRuta = comentarioRuta;
    }

    public Date getFhRegRuta() {
        return fhRegRuta;
    }

    public void setFhRegRuta(Date fhRegRuta) {
        this.fhRegRuta = fhRegRuta;
    }

    public long getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
    }

    public int getIdTipoRuta() {
        return idTipoRuta;
    }

    public void setIdTipoRuta(int idTipoRuta) {
        this.idTipoRuta = idTipoRuta;
    }

    public String getNombreRuta() {
        return nombreRuta;
    }

    public void setNombreRuta(String nombreRuta) {
        this.nombreRuta = nombreRuta;
    }

    public int getParadasRuta() {
        return paradasRuta;
    }

    public void setParadasRuta(int paradasRuta) {
        this.paradasRuta = paradasRuta;
    }

    public String getRecorridoRuta() {
        return recorridoRuta;
    }

    public void setRecorridoRuta(String recorridoRuta) {
        this.recorridoRuta = recorridoRuta;
    }

    public List<WsItemRutaMarcador> getWsItemRutaMarcador() {
        if (wsItemRutaMarcador==null)
            wsItemRutaMarcador = new ArrayList<WsItemRutaMarcador>();
        return wsItemRutaMarcador;
    }

    public void setWsItemRutaMarcador(List<WsItemRutaMarcador> wsItemRutaMarcador) {
        this.wsItemRutaMarcador = wsItemRutaMarcador;
    }

    public String getDiasSemanaRuta() {
        return diasSemanaRuta;
    }

    public void setDiasSemanaRuta(String diasSemanaRuta) {
        this.diasSemanaRuta = diasSemanaRuta;
    }
    
    /**
    * Method 'toString'
    * 
    * @return String
    */
    public String toString(){
            StringBuffer ret = new StringBuffer();
            ret.append( "com.tsp.microfinancieras.dto.Ruta: " );
            ret.append( "idRuta=" + idRuta );
            ret.append( ", idTipoRuta=" + idTipoRuta );
            ret.append( ", nombreRuta=" + nombreRuta );
            ret.append( ", comentarioRuta=" + comentarioRuta );
            ret.append( ", fhRegRuta=" + fhRegRuta );
            ret.append( ", recorridoRuta=" + recorridoRuta );
            ret.append( ", idEmpleado=" + idEmpleado );
            ret.append( ", paradasRuta=" + paradasRuta );
            return ret.toString();
    }
    
}
