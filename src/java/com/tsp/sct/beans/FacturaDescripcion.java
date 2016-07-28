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
public class FacturaDescripcion {    

	private int idFacturaDescripcion;
	private int idFactura;
	//private int cantidad;
	private double cantidad;
	private int idConcepto;
	private String descripcion;
	private double precio;
	//protected List<Campo> camposExtra;
	private String unidad;
	private String identificacion;
	private String nombreConcepto;
        
        private double porcentajeDescuento = 0;
        private double montoDescuento = 0;

	public String getNombreConcepto() {
		return nombreConcepto;
	}

	public void setNombreConcepto(String nombreConcepto) {
		this.nombreConcepto = nombreConcepto;
	}

        public double getPorcentajeDescuento() {
            return porcentajeDescuento;
        }

        public void setPorcentajeDescuento(double porcentajeDescuento) {
            this.porcentajeDescuento = porcentajeDescuento;
        }

        public double getMontoDescuento() {
            return montoDescuento;
        }

        public void setMontoDescuento(double montoDescuento) {
            this.montoDescuento = montoDescuento;
        }        

	/**
	 *Constructor por defecto.
	 */
	public FacturaDescripcion() 
	{
	}

	/**
	 * Obtiene la cantidad de articulos.
	 * 
	 * @return Cantidad de articulos.
	 */
	public double getCantidad() {
		System.out.println("/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-"+cantidad);
		//return Double.parseDouble(FormatUtil.doubleToStringPuntoComas(cantidad));
		return cantidad;
	}

	/**
	 * Inicializa la cantidad de articulos.
	 * 
	 * @param cantidad
	 *            Cantidad de articulos.
	 */
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	
	public String getPrecioStr(){
		System.out.print("-*/-*/*-/ haber que imprimes 2-*/-*/-*/-*/"+FormatUtil.doubleToStringPuntoComas(precio));
		return FormatUtil.doubleToStringPuntoComas(precio);
	}
	
	/**
	 * Obtiene la descripcion de los articulos.
	 * 
	 * @return Descripcion de articulos.
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Obtiene el precio unitario.
	 * 
	 * @return Precio unitario.
	 */
	public double getPrecio() {
		return precio;
	}

	/**
	 * Inicializa el precio .
	 * 
	 * @param precio
	 *            por articulo
	 */
	public void setPrecio(double precio) {
		this.precio = precio;
	}

	/**
	 * Inicializa la descripcion de los articulos.
	 * 
	 * @param descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Obtiene el id de la descripcion de la factura.
	 * 
	 * @return Id de la descripcion.
	 */
	public int getIdFacturaDescripcion() {
		return idFacturaDescripcion;
	}

	/**
	 * Inicializa el id de la descripcion de la factura.
	 * 
	 * @param idFacturaDescripcion
	 *            Id de la descripcion de la factura.
	 */
	public void setIdFacturaDescripcion(int idFacturaDescripcion) {
		this.idFacturaDescripcion = idFacturaDescripcion;
	}

	/**
	 * Obtiene el id de la factura.
	 * 
	 * @return Id de la factura.
	 */
	public int getIdFactura() {
		return idFactura;
	}

	/**
	 * Inicializa el id de la factura.
	 * 
	 * @param idFactura
	 *            Id de la factura.
	 */
	public void setIdFactura(int idFactura) {
		this.idFactura = idFactura;
	}

	/**
	 * Obtiene el id del concepto
	 * 
	 * @return Id del concepto.
	 */
	public int getIdConcepto() {
		return idConcepto;
	}

	/**
	 * Inicializa el id del concepto.
	 * 
	 * @param idConcepto
	 *            Id del concepto.
	 */
	public void setIdConcepto(int idConcepto) {
		this.idConcepto = idConcepto;
	}

	/**
	 * Obtiene los campos extra de la descripcion de la factura, definidos en la
	 * addenda.
	 * 
	 * @return Lista de campos.
	 */
	/*public List<Campo> getCamposExtra() {
		return camposExtra;
	}*/

	/**
	 * Inicializa los campos extra de la descripcion e la factura.
	 * 
	 * @param camposExtra
	 *            Lista de campos extra.
	 */
	/*public void setCamposExtra(List<Campo> camposExtra) {
		this.camposExtra = camposExtra;
	}*/


	
	/**
	 * Obtiene el precio total del detalle.
	 * 
	 * @return cantidad por precio.
	 */
	/*public double getTotal() {
		return cantidad * precio;
	}*/
        
        public double getTotal() {
		return ((cantidad * precio) - montoDescuento);
	}

	/*public String getTotalStr(){
		return FormatUtil.doubleToStringPuntoComas(cantidad * precio);
	}*/
        
        public String getTotalStr(){
		return FormatUtil.doubleToStringPuntoComas(((cantidad * precio)-montoDescuento));
	}
	
	
	/**
	 * Obtiene las aduanas.
	 * 
	 * @return Aduanas.
	 */ 
	/*public List<Aduana> getAduanas() {
		return aduanas;
	}*/
	
	/**
	 * Inicializa las aduanas.
	 * 
	 */
	/*public void setAduanas(List<Aduana> aduanas) {
		this.aduanas = aduanas;
	}*/

	/**
	 * Obtiene los conceptos por cuenta de terceros
	 * 
	 * @return CuentaDeTerceros.
	 */
	/*public CuentaDeTerceros getCuentaTerceros() {
		return cuentaTerceros;
	}*/

	/**
	 * Inicializa los conceptos por cuenta de terceros
	 * 
	 */
	/*public void setCuentaTerceros(CuentaDeTerceros cuentaTerceros) {
		this.cuentaTerceros = cuentaTerceros;
	}*/
	
		/**
	 * Obtieneel tipo de unidads.
	 * 
	 * @return Aduanas.
	 */
	public String getUnidad() {
		return unidad;
	}
	/**
	 * Inicializa el tipo de unidad
	 * 
	 */
	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}
	/**
	 * Obtiene el numero de identificacion.
	 * 
	 * @return Aduanas.
	 */
	public String getIdentificacion() {
		return identificacion;
	}
	
	/**
	 * Inicializa el numero de identificacion
	 * 
	 */

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}
	
}
