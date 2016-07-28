/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.util.Date;

/**
 *
 * @author ISCesar
 */
public class WsItemFoliosMovilEmpleado {

    private int idFolioMovilEmpleado;

    private int idEmpresa;

    private int folioDesde;

    private int folioHasta;

    private int ultimoFolio;

    private String serie;

    private int idEstatus;

    private Date fechaGeneracion;

    private int tipoFolioMovil;

    public int getIdFolioMovilEmpleado() {
        return idFolioMovilEmpleado;
    }

    public void setIdFolioMovilEmpleado(int idFolioMovilEmpleado) {
        this.idFolioMovilEmpleado = idFolioMovilEmpleado;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public int getFolioDesde() {
        return folioDesde;
    }

    public void setFolioDesde(int folioDesde) {
        this.folioDesde = folioDesde;
    }

    public int getFolioHasta() {
        return folioHasta;
    }

    public void setFolioHasta(int folioHasta) {
        this.folioHasta = folioHasta;
    }

    public int getUltimoFolio() {
        return ultimoFolio;
    }

    public void setUltimoFolio(int ultimoFolio) {
        this.ultimoFolio = ultimoFolio;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public int getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(int idEstatus) {
        this.idEstatus = idEstatus;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public int getTipoFolioMovil() {
        return tipoFolioMovil;
    }

    public void setTipoFolioMovil(int tipoFolioMovil) {
        this.tipoFolioMovil = tipoFolioMovil;
    }
    
    
}
