/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

/**
 *
 * @author HpPyme
 */
public class WsItemCatalogoGastos {
    
     private int idCatalogoGastos;
     private int idEmpresa;
     private String nombre;
     private String descripcion;
     private int idEstatus;

    public int getIdCatalogoGastos() {
        return idCatalogoGastos;
    }

    public void setIdCatalogoGastos(int idCatalogoGastos) {
        this.idCatalogoGastos = idCatalogoGastos;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    

    public int getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(int idEstatus) {
        this.idEstatus = idEstatus;
    }
     
     
    
    
}
