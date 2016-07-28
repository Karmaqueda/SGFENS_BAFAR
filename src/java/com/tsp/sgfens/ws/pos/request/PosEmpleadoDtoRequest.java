/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.pos.request;

import java.util.Date;

/**
 *
 * @author ISCesarMartinez
 * 
 * Clase POJO contenedora de datos básicos del usuario/empleado 
 * en sesión móvil que accede al web service
 */
public class PosEmpleadoDtoRequest {
    
    private String empleadoUsuario;
    private String empleadoPassword;
    
    private Date fechaHora;
    

    public PosEmpleadoDtoRequest() {
    }

    public String getEmpleadoPassword() {
        return empleadoPassword;
    }

    public void setEmpleadoPassword(String empleadoPassword) {
        this.empleadoPassword = empleadoPassword;
    }

    public String getEmpleadoUsuario() {
        return empleadoUsuario;
    }

    public void setEmpleadoUsuario(String empleadoUsuario) {
        this.empleadoUsuario = empleadoUsuario;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }
    
}
