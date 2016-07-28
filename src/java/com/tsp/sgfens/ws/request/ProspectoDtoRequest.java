/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.request;

import java.util.Date;

/**
 *
 * @author ISCesarMartinez poseidon24@hotmail.com
 * @date 18-ene-2013
 */
public class ProspectoDtoRequest {

    /** 
    * This attribute maps to the column ID_PROSPECTO in the SGFENS_PROSPECTO table.
    */
    protected int idProspecto;
    protected String razonSocial;
    protected String lada;
    protected String telefono;
    protected String celular;
    protected String correo;
    protected String contacto;
    protected int idEstatus;
    protected String descripcion;
    protected double latitud;
    protected double longitud;
    protected String direccion;
    protected String imagenProspectoBytesBase64;
    protected String dirNumeroExterior;
    protected String dirNumeroInterior;
    protected String dirCodigoPostal;
    protected String dirColonia;
    protected Date fechaRegistro;

    /**
    * Method 'toString'
    * 
    * @return String
    */
    public String toString()
    {
            StringBuffer ret = new StringBuffer();
            ret.append( "com.tsp.sct.dao.dto.SgfensProspecto: " );
            ret.append( "idProspecto=" + idProspecto );
            ret.append( ", razonSocial=" + razonSocial );
            ret.append( ", lada=" + lada );
            ret.append( ", telefono=" + telefono );
            ret.append( ", celular=" + celular );
            ret.append( ", correo=" + correo );
            ret.append( ", contacto=" + contacto );
            ret.append( ", idEstatus=" + idEstatus );
            ret.append( ", descripcion=" + descripcion );
            ret.append( ", latitud=" + latitud );
            ret.append( ", longitud=" + longitud );
            ret.append( ", direccion=" + direccion );
            ret.append( ", imagenProspectoBytesBase64=" + imagenProspectoBytesBase64 );
            ret.append( ", dirNumeroExterior=" + dirNumeroExterior );
            ret.append( ", dirNumeroInterior=" + dirNumeroInterior );
            ret.append( ", dirCodigoPostal=" + dirCodigoPostal );
            ret.append( ", dirColonia=" + dirColonia );
            return ret.toString();
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(int idEstatus) {
        this.idEstatus = idEstatus;
    }

    public int getIdProspecto() {
        return idProspecto;
    }

    public void setIdProspecto(int idProspecto) {
        this.idProspecto = idProspecto;
    }

    public String getImagenProspectoBytesBase64() {
        return imagenProspectoBytesBase64;
    }

    public void setImagenProspectoBytesBase64(String imagenProspectoBytesBase64) {
        this.imagenProspectoBytesBase64 = imagenProspectoBytesBase64;
    }

    public String getLada() {
        return lada;
    }

    public void setLada(String lada) {
        this.lada = lada;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDirNumeroExterior() {
        return dirNumeroExterior;
    }

    public void setDirNumeroExterior(String dirNumeroExterior) {
        this.dirNumeroExterior = dirNumeroExterior;
    }

    public String getDirNumeroInterior() {
        return dirNumeroInterior;
    }

    public void setDirNumeroInterior(String dirNumeroInterior) {
        this.dirNumeroInterior = dirNumeroInterior;
    }

    public String getDirCodigoPostal() {
        return dirCodigoPostal;
    }

    public void setDirCodigoPostal(String dirCodigoPostal) {
        this.dirCodigoPostal = dirCodigoPostal;
    }

    public String getDirColonia() {
        return dirColonia;
    }

    public void setDirColonia(String dirColonia) {
        this.dirColonia = dirColonia;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    
}
