/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.microsip.bean;

import com.tsp.sct.dao.dto.Movimiento;

/**
 *
 * @author leonardo
 */
public class MovimientosMicrosipBean {
    
    private int idMovimientoMicrosip = 0;    
    private String clave = "";
    private Movimiento movimiento = new Movimiento();
    
    private String claveEmpleadoSistemaTercero;
    private String claveConceptoSistemaTercero;

    /**
     * @return the idMovimientoMicrosip
     */
    public int getIdMovimientoMicrosip() {
        return idMovimientoMicrosip;
    }

    /**
     * @param idMovimientoMicrosip the idMovimientoMicrosip to set
     */
    public void setIdMovimientoMicrosip(int idMovimientoMicrosip) {
        this.idMovimientoMicrosip = idMovimientoMicrosip;
    }

    /**
     * @return the movimiento
     */
    public Movimiento getMovimiento() {
        return movimiento;
    }

    /**
     * @param movimiento the movimiento to set
     */
    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
    }

    /**
     * @return the clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * @param clave the clave to set
     */
    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getClaveEmpleadoSistemaTercero() {
        return claveEmpleadoSistemaTercero;
    }

    public void setClaveEmpleadoSistemaTercero(String claveEmpleadoSistemaTercero) {
        this.claveEmpleadoSistemaTercero = claveEmpleadoSistemaTercero;
    }

    public String getClaveConceptoSistemaTercero() {
        return claveConceptoSistemaTercero;
    }

    public void setClaveConceptoSistemaTercero(String claveConceptoSistemaTercero) {
        this.claveConceptoSistemaTercero = claveConceptoSistemaTercero;
    }
    
}