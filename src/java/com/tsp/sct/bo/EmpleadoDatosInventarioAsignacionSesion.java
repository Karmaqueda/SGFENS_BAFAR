/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

/**
 *
 * @author leonardo
 */
public class EmpleadoDatosInventarioAsignacionSesion {
    
    //private int numeroProductoSesion = 0;
    private int idConceptoInventario = -1;
    private double numArticulosInventarioAsignar = -1;
    private double pesoArticulosInventarioAsignar = 0;
    private boolean prodExistenteEmpleado = false;
    private int idAlmacenOrigen = -1;
    private int idEmpleado = 0;

    /**
     * @return the idConceptoInventario
     */
    public int getIdConceptoInventario() {
        return idConceptoInventario;
    }

    /**
     * @param idConceptoInventario the idConceptoInventario to set
     */
    public void setIdConceptoInventario(int idConceptoInventario) {
        this.idConceptoInventario = idConceptoInventario;
    }

    /**
     * @return the numArticulosInventarioAsignar
     */
    public double getNumArticulosInventarioAsignar() {
        return numArticulosInventarioAsignar;
    }

    /**
     * @param numArticulosInventarioAsignar the numArticulosInventarioAsignar to set
     */
    public void setNumArticulosInventarioAsignar(double numArticulosInventarioAsignar) {
        this.numArticulosInventarioAsignar = numArticulosInventarioAsignar;
    }

    /**
     * @return the prodExistenteEmpleado
     */
    public boolean isProdExistenteEmpleado() {
        return prodExistenteEmpleado;
    }

    /**
     * @param prodExistenteEmpleado the prodExistenteEmpleado to set
     */
    public void setProdExistenteEmpleado(boolean prodExistenteEmpleado) {
        this.prodExistenteEmpleado = prodExistenteEmpleado;
    }

    /**
     * @return the idAlmacenOrigen
     */
    public int getIdAlmacenOrigen() {
        return idAlmacenOrigen;
    }

    /**
     * @param idAlmacenOrigen the idAlmacenOrigen to set
     */
    public void setIdAlmacenOrigen(int idAlmacenOrigen) {
        this.idAlmacenOrigen = idAlmacenOrigen;
    }

    /**
     * @return the idEmpleado
     */
    public int getIdEmpleado() {
        return idEmpleado;
    }

    /**
     * @param idEmpleado the idEmpleado to set
     */
    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    /**
     * @return the pesoArticulosInventarioAsignar
     */
    public double getPesoArticulosInventarioAsignar() {
        return pesoArticulosInventarioAsignar;
    }

    /**
     * @param pesoArticulosInventarioAsignar the pesoArticulosInventarioAsignar to set
     */
    public void setPesoArticulosInventarioAsignar(double pesoArticulosInventarioAsignar) {
        this.pesoArticulosInventarioAsignar = pesoArticulosInventarioAsignar;
    }
    
}
