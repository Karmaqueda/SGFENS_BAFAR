/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

import java.io.Serializable;

/**
 *
 * @author HpPyme
 */
public class WsItemPrecioCliente implements Serializable {
    
    int idConcepto;
    double precioClienteUnitario;
    double precioClienteMedio;
    double precioClienteMayoreo;
    double precioClienteDocena;
    double precioClienteEspecial;
    int  estatus;
    
    private double precioUnitarioGranelCliente;
    private double precioMedioGranelCliente;
    private double precioMayoreoGranelCliente;
    private double precioEspecialGranelCliente;

    public int getIdConcepto() {
        return idConcepto;
    }

    public void setIdConcepto(int idConcepto) {
        this.idConcepto = idConcepto;
    }

    public double getPrecioClienteUnitario() {
        return precioClienteUnitario;
    }

    public void setPrecioClienteUnitario(double precioClienteUnitario) {
        this.precioClienteUnitario = precioClienteUnitario;
    }

    public double getPrecioClienteMedio() {
        return precioClienteMedio;
    }

    public void setPrecioClienteMedio(double precioClienteMedio) {
        this.precioClienteMedio = precioClienteMedio;
    }

    public double getPrecioClienteMayoreo() {
        return precioClienteMayoreo;
    }

    public void setPrecioClienteMayoreo(double precioClienteMayoreo) {
        this.precioClienteMayoreo = precioClienteMayoreo;
    }

    public double getPrecioClienteDocena() {
        return precioClienteDocena;
    }

    public void setPrecioClienteDocena(double precioClienteDocena) {
        this.precioClienteDocena = precioClienteDocena;
    }

    public double getPrecioClienteEspecial() {
        return precioClienteEspecial;
    }

    public void setPrecioClienteEspecial(double precioClienteEspecial) {
        this.precioClienteEspecial = precioClienteEspecial;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    /**
     * @return the precioUnitarioGranelCliente
     */
    public double getPrecioUnitarioGranelCliente() {
        return precioUnitarioGranelCliente;
    }

    /**
     * @param precioUnitarioGranelCliente the precioUnitarioGranelCliente to set
     */
    public void setPrecioUnitarioGranelCliente(double precioUnitarioGranelCliente) {
        this.precioUnitarioGranelCliente = precioUnitarioGranelCliente;
    }

    /**
     * @return the precioMedioGranelCliente
     */
    public double getPrecioMedioGranelCliente() {
        return precioMedioGranelCliente;
    }

    /**
     * @param precioMedioGranelCliente the precioMedioGranelCliente to set
     */
    public void setPrecioMedioGranelCliente(double precioMedioGranelCliente) {
        this.precioMedioGranelCliente = precioMedioGranelCliente;
    }

    /**
     * @return the precioMayoreoGranelCliente
     */
    public double getPrecioMayoreoGranelCliente() {
        return precioMayoreoGranelCliente;
    }

    /**
     * @param precioMayoreoGranelCliente the precioMayoreoGranelCliente to set
     */
    public void setPrecioMayoreoGranelCliente(double precioMayoreoGranelCliente) {
        this.precioMayoreoGranelCliente = precioMayoreoGranelCliente;
    }

    /**
     * @return the precioEspecialGranelCliente
     */
    public double getPrecioEspecialGranelCliente() {
        return precioEspecialGranelCliente;
    }

    /**
     * @param precioEspecialGranelCliente the precioEspecialGranelCliente to set
     */
    public void setPrecioEspecialGranelCliente(double precioEspecialGranelCliente) {
        this.precioEspecialGranelCliente = precioEspecialGranelCliente;
    }
    
    
    
}
