/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.sesion;

import com.tsp.sct.dao.dto.EmpleadoMetas;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author HpPyme
 */
public class MetaEmpleadoSesion {
    
    private String nombre = "";
    private int tipo = -1;
    private int periodo = -1;
    private int duracion = 0;        
    private ArrayList<EmpleadoMetas> listaMetas = new ArrayList<EmpleadoMetas>();
    private Date fechaIni = null;        
    private Date fechaFin = null;        

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }    

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public ArrayList<EmpleadoMetas> getListaMetas() {
        return listaMetas;
    }

    public void setListaMetas(ArrayList<EmpleadoMetas> listaMetas) {
        this.listaMetas = listaMetas;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

   
   
    
        
}
