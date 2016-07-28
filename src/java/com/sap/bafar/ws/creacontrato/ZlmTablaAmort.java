
package com.sap.bafar.ws.creacontrato;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ZlmTablaAmort complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ZlmTablaAmort">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NoPago" type="{urn:sap-com:document:sap:soap:functions:mc-style}numeric3"/>
 *         &lt;element name="Fecha" type="{urn:sap-com:document:sap:soap:functions:mc-style}date10"/>
 *         &lt;element name="SaldoCapIni" type="{urn:sap-com:document:sap:soap:functions:mc-style}curr13.2"/>
 *         &lt;element name="Intereses" type="{urn:sap-com:document:sap:soap:functions:mc-style}curr13.2"/>
 *         &lt;element name="Iva" type="{urn:sap-com:document:sap:soap:functions:mc-style}curr13.2"/>
 *         &lt;element name="PagoEspecial" type="{urn:sap-com:document:sap:soap:functions:mc-style}curr13.2"/>
 *         &lt;element name="PagoCapital" type="{urn:sap-com:document:sap:soap:functions:mc-style}curr13.2"/>
 *         &lt;element name="Comisiones" type="{urn:sap-com:document:sap:soap:functions:mc-style}curr13.2"/>
 *         &lt;element name="TotalPago" type="{urn:sap-com:document:sap:soap:functions:mc-style}curr13.2"/>
 *         &lt;element name="SaldoCapFin" type="{urn:sap-com:document:sap:soap:functions:mc-style}curr13.2"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZlmTablaAmort", propOrder = {
    "noPago",
    "fecha",
    "saldoCapIni",
    "intereses",
    "iva",
    "pagoEspecial",
    "pagoCapital",
    "comisiones",
    "totalPago",
    "saldoCapFin"
})
public class ZlmTablaAmort {

    @XmlElement(name = "NoPago", required = true)
    protected String noPago;
    @XmlElement(name = "Fecha", required = true)
    protected String fecha;
    @XmlElement(name = "SaldoCapIni", required = true)
    protected BigDecimal saldoCapIni;
    @XmlElement(name = "Intereses", required = true)
    protected BigDecimal intereses;
    @XmlElement(name = "Iva", required = true)
    protected BigDecimal iva;
    @XmlElement(name = "PagoEspecial", required = true)
    protected BigDecimal pagoEspecial;
    @XmlElement(name = "PagoCapital", required = true)
    protected BigDecimal pagoCapital;
    @XmlElement(name = "Comisiones", required = true)
    protected BigDecimal comisiones;
    @XmlElement(name = "TotalPago", required = true)
    protected BigDecimal totalPago;
    @XmlElement(name = "SaldoCapFin", required = true)
    protected BigDecimal saldoCapFin;

    /**
     * Obtiene el valor de la propiedad noPago.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoPago() {
        return noPago;
    }

    /**
     * Define el valor de la propiedad noPago.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoPago(String value) {
        this.noPago = value;
    }

    /**
     * Obtiene el valor de la propiedad fecha.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * Define el valor de la propiedad fecha.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFecha(String value) {
        this.fecha = value;
    }

    /**
     * Obtiene el valor de la propiedad saldoCapIni.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSaldoCapIni() {
        return saldoCapIni;
    }

    /**
     * Define el valor de la propiedad saldoCapIni.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSaldoCapIni(BigDecimal value) {
        this.saldoCapIni = value;
    }

    /**
     * Obtiene el valor de la propiedad intereses.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIntereses() {
        return intereses;
    }

    /**
     * Define el valor de la propiedad intereses.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIntereses(BigDecimal value) {
        this.intereses = value;
    }

    /**
     * Obtiene el valor de la propiedad iva.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIva() {
        return iva;
    }

    /**
     * Define el valor de la propiedad iva.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIva(BigDecimal value) {
        this.iva = value;
    }

    /**
     * Obtiene el valor de la propiedad pagoEspecial.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPagoEspecial() {
        return pagoEspecial;
    }

    /**
     * Define el valor de la propiedad pagoEspecial.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPagoEspecial(BigDecimal value) {
        this.pagoEspecial = value;
    }

    /**
     * Obtiene el valor de la propiedad pagoCapital.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPagoCapital() {
        return pagoCapital;
    }

    /**
     * Define el valor de la propiedad pagoCapital.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPagoCapital(BigDecimal value) {
        this.pagoCapital = value;
    }

    /**
     * Obtiene el valor de la propiedad comisiones.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getComisiones() {
        return comisiones;
    }

    /**
     * Define el valor de la propiedad comisiones.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setComisiones(BigDecimal value) {
        this.comisiones = value;
    }

    /**
     * Obtiene el valor de la propiedad totalPago.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalPago() {
        return totalPago;
    }

    /**
     * Define el valor de la propiedad totalPago.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalPago(BigDecimal value) {
        this.totalPago = value;
    }

    /**
     * Obtiene el valor de la propiedad saldoCapFin.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSaldoCapFin() {
        return saldoCapFin;
    }

    /**
     * Define el valor de la propiedad saldoCapFin.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSaldoCapFin(BigDecimal value) {
        this.saldoCapFin = value;
    }

}
