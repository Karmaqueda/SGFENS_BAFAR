/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

/**
 *
 * @author leonardo
 * 
 * Clase para recuperar todo el contenido de la nomina; el concepto, las deducciones, las percepciones, las horas extras, incapacidades
 */
public class NominaComprobanteDescripcionConcepDeducPerceHorasIncapaBO {
        
    private int idTipoConcepto = 0; //1 si es concepto, 2 si es percepcion, 3 si es deduccion, 4 si es hora extra, 5 si es incapacidad
    
    //conceptos normales
    private int idComprobanteDescripcion = 0;    
    private String nombre = null;
    private String descripcion = null;
    private double precio = 0;
    private String identificacion = null;
    private double cantidad = 0;
    
    //las percepciones
    private int idComprobanteDdescripcionPercepcion = 0;
    private String nombreDescripcionPercepcion = null;
    private String clavePercepcion = null;
    private double importeGravadoPercepcion = 0;
    private double importeExentoPercepcion = 0;
    
    //las deducciones
    private int idComprobanteDdescripcionDeduccion = 0;
    private String nombreDescripcionDeduccion = null;
    private String claveDeduccion = null;
    private double importeGravadoDeduccion = 0;
    private double importeExentoDeduccion = 0;
    
    //las incapacidades
    private int idComprobanteDescripcionIncapacidad = 0;
    private int diasIncapacidad = 0;
    private String nombreDescripcionIncapacidad = null;
    private double descuentoIncapacidad = 0;
    
    //para las horas extra;
    private int idComprobanteDescripcionHorasExtra = 0;
    private int diasHorasExtra = 0;
    private String tipoHoras = null;
    private double importePagado = 0;

    /**
     * @return the idTipoConcepto
     */
    public int getIdTipoConcepto() {
        return idTipoConcepto;
    }

    /**
     * @param idTipoConcepto the idTipoConcepto to set
     */
    public void setIdTipoConcepto(int idTipoConcepto) {
        this.idTipoConcepto = idTipoConcepto;
    }

    /**
     * @return the idComprobanteDescripcion
     */
    public int getIdComprobanteDescripcion() {
        return idComprobanteDescripcion;
    }

    /**
     * @param idComprobanteDescripcion the idComprobanteDescripcion to set
     */
    public void setIdComprobanteDescripcion(int idComprobanteDescripcion) {
        this.idComprobanteDescripcion = idComprobanteDescripcion;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the precio
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * @param precio the precio to set
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * @return the identificacion
     */
    public String getIdentificacion() {
        return identificacion;
    }

    /**
     * @param identificacion the identificacion to set
     */
    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }
   
    /**
     * @return the idComprobanteDescripcionIncapacidad
     */
    public int getIdComprobanteDescripcionIncapacidad() {
        return idComprobanteDescripcionIncapacidad;
    }

    /**
     * @param idComprobanteDescripcionIncapacidad the idComprobanteDescripcionIncapacidad to set
     */
    public void setIdComprobanteDescripcionIncapacidad(int idComprobanteDescripcionIncapacidad) {
        this.idComprobanteDescripcionIncapacidad = idComprobanteDescripcionIncapacidad;
    }

    /**
     * @return the diasIncapacidad
     */
    public int getDiasIncapacidad() {
        return diasIncapacidad;
    }

    /**
     * @param diasIncapacidad the diasIncapacidad to set
     */
    public void setDiasIncapacidad(int diasIncapacidad) {
        this.diasIncapacidad = diasIncapacidad;
    }

    /**
     * @return the nombreDescripcionIncapacidad
     */
    public String getNombreDescripcionIncapacidad() {
        return nombreDescripcionIncapacidad;
    }

    /**
     * @param nombreDescripcionIncapacidad the nombreDescripcionIncapacidad to set
     */
    public void setNombreDescripcionIncapacidad(String nombreDescripcionIncapacidad) {
        this.nombreDescripcionIncapacidad = nombreDescripcionIncapacidad;
    }

    /**
     * @return the descuentoIncapacidad
     */
    public double getDescuentoIncapacidad() {
        return descuentoIncapacidad;
    }

    /**
     * @param descuentoIncapacidad the descuentoIncapacidad to set
     */
    public void setDescuentoIncapacidad(double descuentoIncapacidad) {
        this.descuentoIncapacidad = descuentoIncapacidad;
    }

    /**
     * @return the idComprobanteDescripcionHorasExtra
     */
    public int getIdComprobanteDescripcionHorasExtra() {
        return idComprobanteDescripcionHorasExtra;
    }

    /**
     * @param idComprobanteDescripcionHorasExtra the idComprobanteDescripcionHorasExtra to set
     */
    public void setIdComprobanteDescripcionHorasExtra(int idComprobanteDescripcionHorasExtra) {
        this.idComprobanteDescripcionHorasExtra = idComprobanteDescripcionHorasExtra;
    }

    /**
     * @return the diasHorasExtra
     */
    public int getDiasHorasExtra() {
        return diasHorasExtra;
    }

    /**
     * @param diasHorasExtra the diasHorasExtra to set
     */
    public void setDiasHorasExtra(int diasHorasExtra) {
        this.diasHorasExtra = diasHorasExtra;
    }

    /**
     * @return the tipoHoras
     */
    public String getTipoHoras() {
        return tipoHoras;
    }

    /**
     * @param tipoHoras the tipoHoras to set
     */
    public void setTipoHoras(String tipoHoras) {
        this.tipoHoras = tipoHoras;
    }

    /**
     * @return the importePagado
     */
    public double getImportePagado() {
        return importePagado;
    }

    /**
     * @param importePagado the importePagado to set
     */
    public void setImportePagado(double importePagado) {
        this.importePagado = importePagado;
    }

    /**
     * @return the idComprobanteDdescripcionPercepcion
     */
    public int getIdComprobanteDdescripcionPercepcion() {
        return idComprobanteDdescripcionPercepcion;
    }

    /**
     * @param idComprobanteDdescripcionPercepcion the idComprobanteDdescripcionPercepcion to set
     */
    public void setIdComprobanteDdescripcionPercepcion(int idComprobanteDdescripcionPercepcion) {
        this.idComprobanteDdescripcionPercepcion = idComprobanteDdescripcionPercepcion;
    }

    /**
     * @return the nombreDescripcionPercepcion
     */
    public String getNombreDescripcionPercepcion() {
        return nombreDescripcionPercepcion;
    }

    /**
     * @param nombreDescripcionPercepcion the nombreDescripcionPercepcion to set
     */
    public void setNombreDescripcionPercepcion(String nombreDescripcionPercepcion) {
        this.nombreDescripcionPercepcion = nombreDescripcionPercepcion;
    }

    /**
     * @return the clavePercepcion
     */
    public String getClavePercepcion() {
        return clavePercepcion;
    }

    /**
     * @param clavePercepcion the clavePercepcion to set
     */
    public void setClavePercepcion(String clavePercepcion) {
        this.clavePercepcion = clavePercepcion;
    }

    /**
     * @return the importeGravadoPercepcion
     */
    public double getImporteGravadoPercepcion() {
        return importeGravadoPercepcion;
    }

    /**
     * @param importeGravadoPercepcion the importeGravadoPercepcion to set
     */
    public void setImporteGravadoPercepcion(double importeGravadoPercepcion) {
        this.importeGravadoPercepcion = importeGravadoPercepcion;
    }

    /**
     * @return the importeExentoPercepcion
     */
    public double getImporteExentoPercepcion() {
        return importeExentoPercepcion;
    }

    /**
     * @param importeExentoPercepcion the importeExentoPercepcion to set
     */
    public void setImporteExentoPercepcion(double importeExentoPercepcion) {
        this.importeExentoPercepcion = importeExentoPercepcion;
    }

    /**
     * @return the idComprobanteDdescripcionDeduccion
     */
    public int getIdComprobanteDdescripcionDeduccion() {
        return idComprobanteDdescripcionDeduccion;
    }

    /**
     * @param idComprobanteDdescripcionDeduccion the idComprobanteDdescripcionDeduccion to set
     */
    public void setIdComprobanteDdescripcionDeduccion(int idComprobanteDdescripcionDeduccion) {
        this.idComprobanteDdescripcionDeduccion = idComprobanteDdescripcionDeduccion;
    }

    /**
     * @return the nombreDescripcionDeduccion
     */
    public String getNombreDescripcionDeduccion() {
        return nombreDescripcionDeduccion;
    }

    /**
     * @param nombreDescripcionDeduccion the nombreDescripcionDeduccion to set
     */
    public void setNombreDescripcionDeduccion(String nombreDescripcionDeduccion) {
        this.nombreDescripcionDeduccion = nombreDescripcionDeduccion;
    }

    /**
     * @return the claveDeduccion
     */
    public String getClaveDeduccion() {
        return claveDeduccion;
    }

    /**
     * @param claveDeduccion the claveDeduccion to set
     */
    public void setClaveDeduccion(String claveDeduccion) {
        this.claveDeduccion = claveDeduccion;
    }

    /**
     * @return the importeGravadoDeduccion
     */
    public double getImporteGravadoDeduccion() {
        return importeGravadoDeduccion;
    }

    /**
     * @param importeGravadoDeduccion the importeGravadoDeduccion to set
     */
    public void setImporteGravadoDeduccion(double importeGravadoDeduccion) {
        this.importeGravadoDeduccion = importeGravadoDeduccion;
    }

    /**
     * @return the importeExentoDeduccion
     */
    public double getImporteExentoDeduccion() {
        return importeExentoDeduccion;
    }

    /**
     * @param importeExentoDeduccion the importeExentoDeduccion to set
     */
    public void setImporteExentoDeduccion(double importeExentoDeduccion) {
        this.importeExentoDeduccion = importeExentoDeduccion;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    
}
