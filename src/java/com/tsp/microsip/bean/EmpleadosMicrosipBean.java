/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.microsip.bean;

/**
 *
 * @author leonardo
 */
public class EmpleadosMicrosipBean {
    private int idEmpleadosMicrosip = 0;    
    private String clave = "";
    private Empleado empleado = new Empleado();

    /**
     * @return the idEmpleadosMicrosip
     */
    public int getIdEmpleadosMicrosip() {
        return idEmpleadosMicrosip;
    }

    /**
     * @param idEmpleadosMicrosip the idEmpleadosMicrosip to set
     */
    public void setIdEmpleadosMicrosip(int idEmpleadosMicrosip) {
        this.idEmpleadosMicrosip = idEmpleadosMicrosip;
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

    /**
     * @return the empleado
     */
    public Empleado getEmpleado() {
        return empleado;
    }

    /**
     * @param empleado the empleado to set
     */
    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
}
