/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.io.Serializable;

/**
 *
 * @author ISCesar
 */
public class WsItemAlmacen implements Serializable {

    private int idAlmacen;
    private int idEmpresa;
    private int idEstatus;
    private String nombre;
    private String direccion;
    private double areaAlmacen;
    private String responsable;
    private String puesto;
    private String telefono;
    private String correo;
    private int isPrincipal;
    private int excluirMoviles;

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getAreaAlmacen() {
        return areaAlmacen;
    }

    public void setAreaAlmacen(double areaAlmacen) {
        this.areaAlmacen = areaAlmacen;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getIsPrincipal() {
        return isPrincipal;
    }

    public void setIsPrincipal(int isPrincipal) {
        this.isPrincipal = isPrincipal;
    }

    public int getExcluirMoviles() {
        return excluirMoviles;
    }

    public void setExcluirMoviles(int excluirMoviles) {
        this.excluirMoviles = excluirMoviles;
    }
    
    
}
