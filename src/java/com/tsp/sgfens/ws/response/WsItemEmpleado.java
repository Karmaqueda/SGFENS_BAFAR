/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.io.Serializable;

/**
 *
 * @author ISCesarMartinez
 */
public class WsItemEmpleado implements Serializable{
    
    protected long idEmpleado;

    /** 
     * This attribute maps to the column ID_EMPRESA in the empleado table.
     */
    protected long idEmpresa;

    /** 
     * This attribute maps to the column ID_ESTATUS in the empleado table.
     */
    protected long idEstatus;

    /** 
     * This attribute maps to the column NOMBRE in the empleado table.
     */
    protected String nombre;

    /** 
     * This attribute maps to the column APELLIDO_PATERNO in the empleado table.
     */
    protected String apellidoPaterno;

    /** 
     * This attribute maps to the column APELLIDO_MATERNO in the empleado table.
     */
    protected String apellidoMaterno;

    /** 
     * This attribute maps to the column TELEFONO_LOCAL in the empleado table.
     */
    protected String telefonoLocal;

    /** 
     * This attribute maps to the column NUM_EMPLEADO in the empleado table.
     */
    protected String numEmpleado;

    /** 
     * This attribute maps to the column CORREO_ELECTRONICO in the empleado table.
     */
    protected String correoElectronico;

    /** 
     * This attribute maps to the column PASSWORD in the empleado table.
     */
    //protected String password;

    /** 
     * This attribute maps to the column ID_SUCURSAL in the empleado table.
     */
    protected long idSucursal;

    /** 
     * This attribute maps to the column ID_DISPOSITIVO in the empleado table.
     */
    protected long idDispositivo;

    /** 
     * This attribute maps to the column LATITUD in the empleado table.
     */
    protected double latitud;

    /** 
     * This attribute maps to the column LONGITUD in the empleado table.
     */
    protected double longitud;
    
    protected double idMovilEmpleadoRol;
    
    protected int permisoVentaRapida;
    
    protected int permisoVentaCredito;
    
    protected int trabajaFueraLinea;
    
    protected int clientesCodigoBarras;
    
    protected double distanciaObligatoria;
    
    protected int precioCompra;
    
    protected int permisoCrearClientes;
    
    protected int permisoAccionesClientes;
    
    protected int permisoVentaConsigna;
    
    protected double comisionConsigna;
 
    /** 
    * This attribute maps to the column PORCENTAJE_COMISION in the empleado table.
    */
    protected double porcentajeComision;
    
    protected int permisoDevoluciones;
    
    protected int permisoAutoServInventario;
    
    protected int permisoNoCobroParcial;
    
    protected int permisoVerProveedores;
    
    protected int intervaloUbicacionSegundos;

    public String getApellidoMaterno() {
        return apellidoMaterno;
}

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public long getIdDispositivo() {
        return idDispositivo;
    }

    public void setIdDispositivo(long idDispositivo) {
        this.idDispositivo = idDispositivo;
    }

    public long getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public long getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(long idEstatus) {
        this.idEstatus = idEstatus;
    }

    public long getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(long idSucursal) {
        this.idSucursal = idSucursal;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumEmpleado() {
        return numEmpleado;
    }

    public void setNumEmpleado(String numEmpleado) {
        this.numEmpleado = numEmpleado;
    }

    public String getTelefonoLocal() {
        return telefonoLocal;
    }

    public void setTelefonoLocal(String telefonoLocal) {
        this.telefonoLocal = telefonoLocal;
    }

    public double getIdMovilEmpleadoRol() {
        return idMovilEmpleadoRol;
    }

    public void setIdMovilEmpleadoRol(double idMovilEmpleadoRol) {
        this.idMovilEmpleadoRol = idMovilEmpleadoRol;
    }

    /**
     * @return the permisoVentaRapida
     */
    public int getPermisoVentaRapida() {
        return permisoVentaRapida;
    }

    /**
     * @param permisoVentaRapida the permisoVentaRapida to set
     */
    public void setPermisoVentaRapida(int permisoVentaRapida) {
        this.permisoVentaRapida = permisoVentaRapida;
    }

    
    public double getPorcentajeComision() {
        return porcentajeComision;
    }

    public void setPorcentajeComision(double porcentajeComision) {
        this.porcentajeComision = porcentajeComision;
    }

    /**
     * @return the permisoVentaCredito
     */
    public int getPermisoVentaCredito() {
        return permisoVentaCredito;
    }

    /**
     * @param permisoVentaCredito the permisoVentaCredito to set
     */
    public void setPermisoVentaCredito(int permisoVentaCredito) {
        this.permisoVentaCredito = permisoVentaCredito;
    }

    /**
     * @return the trabajaFueraLinea
     */
    public int getTrabajaFueraLinea() {
        return trabajaFueraLinea;
    }

    /**
     * @param trabajaFueraLinea the trabajaFueraLinea to set
     */
    public void setTrabajaFueraLinea(int trabajaFueraLinea) {
        this.trabajaFueraLinea = trabajaFueraLinea;
    }

    /**
     * @return the clientesCodigoBarras
     */
    public int getClientesCodigoBarras() {
        return clientesCodigoBarras;
    }

    /**
     * @param clientesCodigoBarras the clientesCodigoBarras to set
     */
    public void setClientesCodigoBarras(int clientesCodigoBarras) {
        this.clientesCodigoBarras = clientesCodigoBarras;
    }

    /**
     * @return the distanciaObligatoria
     */
    public double getDistanciaObligatoria() {
        return distanciaObligatoria;
    }

    /**
     * @param distanciaObligatoria the distanciaObligatoria to set
     */
    public void setDistanciaObligatoria(double distanciaObligatoria) {
        this.distanciaObligatoria = distanciaObligatoria;
    }

    /**
     * @return the precioCompra
     */
    public int getPrecioCompra() {
        return precioCompra;
    }

    /**
     * @param precioCompra the precioCompra to set
     */
    public void setPrecioCompra(int precioCompra) {
        this.precioCompra = precioCompra;
    }

    /**
     * @return the permisoCrearClientes
     */
    public int getPermisoCrearClientes() {
        return permisoCrearClientes;
    }

    /**
     * @param permisoCrearClientes the permisoCrearClientes to set
     */
    public void setPermisoCrearClientes(int permisoCrearClientes) {
        this.permisoCrearClientes = permisoCrearClientes;
    }

    public int getPermisoAccionesClientes() {
        return permisoAccionesClientes;
    }

    public void setPermisoAccionesClientes(int permisoAccionesClientes) {
        this.permisoAccionesClientes = permisoAccionesClientes;
    }

    /**
     * @return the permisoVentaConsigna
     */
    public int getPermisoVentaConsigna() {
        return permisoVentaConsigna;
    }

    /**
     * @param permisoVentaConsigna the permisoVentaConsigna to set
     */
    public void setPermisoVentaConsigna(int permisoVentaConsigna) {
        this.permisoVentaConsigna = permisoVentaConsigna;
    }

    /**
     * @return the comisionConsigna
     */
    public double getComisionConsigna() {
        return comisionConsigna;
    }

    /**
     * @param comisionConsigna the comisionConsigna to set
     */
    public void setComisionConsigna(double comisionConsigna) {
        this.comisionConsigna = comisionConsigna;
    }

    public int getPermisoDevoluciones() {
        return permisoDevoluciones;
    }

    public void setPermisoDevoluciones(int permisoDevoluciones) {
        this.permisoDevoluciones = permisoDevoluciones;
    }

    public int getPermisoAutoServInventario() {
        return permisoAutoServInventario;
    }

    public void setPermisoAutoServInventario(int permisoAutoServInventario) {
        this.permisoAutoServInventario = permisoAutoServInventario;
    }

    /**
     * @return the permisoNoCobroParcial
     */
    public int getPermisoNoCobroParcial() {
        return permisoNoCobroParcial;
    }

    /**
     * @param permisoNoCobroParcial the permisoNoCobroParcial to set
     */
    public void setPermisoNoCobroParcial(int permisoNoCobroParcial) {
        this.permisoNoCobroParcial = permisoNoCobroParcial;
    }

    /**
     * @return the permisoVerProveedores
     */
    public int getPermisoVerProveedores() {
        return permisoVerProveedores;
    }

    /**
     * @param permisoVerProveedores the permisoVerProveedores to set
     */
    public void setPermisoVerProveedores(int permisoVerProveedores) {
        this.permisoVerProveedores = permisoVerProveedores;
    }
    
    public int getIntervaloUbicacionSegundos() {
        return intervaloUbicacionSegundos;
    }

    public void setIntervaloUbicacionSegundos(int intervaloUbicacionSegundos) {
        this.intervaloUbicacionSegundos = intervaloUbicacionSegundos;
    }

}
