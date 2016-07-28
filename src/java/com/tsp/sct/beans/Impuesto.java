/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.beans;

import com.tsp.sct.util.FormatUtil;

/**
 *
 * @author Leonardo
 */
public class Impuesto {

	public int idImpuesto;
	public int idEmpresa;
	public String nombre;
	public String descripcion;
	public double porcentaje;
	public boolean trasladado;
	private double total;
	private int estatus;
	private int idEstatus;
	private boolean impuestoLocal;	
	
	/**
	 *Obtiene el Id del Estatus.
	 */
	public int getIdEstatus() 
	{
		return idEstatus;
	}

	/**
	 * Inicializa el Id del Estatus.
	 */
	public void setIdEstatus(int idEstatus) {
		this.idEstatus = idEstatus;
	}


	/**
	 * Constructor por defecto.
	 */
	public Impuesto() 
	{
	}

	/**
	 * Constructor que inicializa todos los parametros de un impuesto.
	 */
	public Impuesto(Impuesto impuesto) {
		this.idImpuesto = impuesto.getIdImpuesto();
		this.idEmpresa = impuesto.getIdEmpresa();
		this.nombre = impuesto.getNombre();
		this.descripcion = impuesto.getDescripcion();
		this.porcentaje = impuesto.getPorcentaje();
		this.trasladado = impuesto.isTrasladado();
		this.estatus = impuesto.getEstatus();
		this.impuestoLocal = impuesto.isImpuestoLocal();
	}

	/**
	 *Obtiene el Id del Impuesto.
	 */
	public int getIdImpuesto() {
		return idImpuesto;
	}

	/**
	 * Inicializa el Id del Impuesto.
	 */
	public void setIdImpuesto(int idImpuesto) {
		this.idImpuesto = idImpuesto;
	}

	/**
	 *Obtiene el Id de la empresa.
	 */
	public int getIdEmpresa() {
		return idEmpresa;
	}

	/**
	 * Inicializa el Id de la empresa.
	 */
	public void setIdEmpresa(int idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	/**
	 *Obtiene el nombre del Impuesto.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Inicializa el nombre del Impuesto.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 *Obtiene la descripcion del Impuesto.
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Inicializa la descripcion del Impuesto.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 *Obtiene el Porcentaje del Impuesto.
	 */
	public double getPorcentaje() {
		return porcentaje;
	}

	/**
	 * Inicializa el porcentaje del Impuesto.
	 */
	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}

	/**
	 * Metodo que nos indica si el impuesto es Trasladado.
	 * 
	 * return booleano.
	 */
	public boolean isTrasladado() {
		return trasladado;
	}

	/**
	 * Metodo que inicializa un impuesto Local.
	 */
	public void setTrasladado(boolean trasladado) {
		this.trasladado = trasladado;
	}

	/**
	 *Obtiene el total del Impuesto.
	 */
	public double getTotal() {
		System.out.println("TOTAL IVA: "+ total);
		return Double.parseDouble(FormatUtil.doubleToString(total));
	}


	/**
	 *Obtiene el total del Impuesto como cadena.
	 */
	public String getTotalStr() {
		return FormatUtil.doubleToStringPuntoComas(total);
	}
	/**
	 * Inicializa el total del Impuesto.
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	/**
	 *Obtiene el Estatus del impuesto.
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * Inicializa el Estatus del Impuesto.
	 */
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}	

	/**
	 * Obtiene el id del impuesto.
	 */
	public int getId() {
		return idImpuesto;
	}

	/**
	 * Metodo no implementado.
	 */
	public String getMensaje() {
		// TODO Auto-generated method stub
		return null;
	}	

	/**
	 * Metodo que nos indica si el impuesto es Local.
	 * 
	 * return booleano.
	 */
	public boolean isImpuestoLocal() {
		return impuestoLocal;
	}

	/**
	 * Metodo que inicializa un impuesto Local.
	 */
	public void setImpuestoLocal(boolean impuestoLocal) {
		this.impuestoLocal = impuestoLocal;
	}

}
