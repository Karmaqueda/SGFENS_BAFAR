
package com.sap.bafar.ws.consultadispersion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ZcmlOrdPago complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ZcmlOrdPago">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Mandt" type="{urn:sap-com:document:sap:rfc:functions}clnt3"/>
 *         &lt;element name="Bukrs" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="Zlaufd" type="{urn:sap-com:document:sap:rfc:functions}date10"/>
 *         &lt;element name="Zlaufi" type="{urn:sap-com:document:sap:rfc:functions}char6"/>
 *         &lt;element name="Credito" type="{urn:sap-com:document:sap:rfc:functions}char16"/>
 *         &lt;element name="Emisor" type="{urn:sap-com:document:sap:rfc:functions}char24"/>
 *         &lt;element name="Fecha" type="{urn:sap-com:document:sap:rfc:functions}date10"/>
 *         &lt;element name="Acreditado" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="Nombre" type="{urn:sap-com:document:sap:rfc:functions}char55"/>
 *         &lt;element name="Importe" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="Status" type="{urn:sap-com:document:sap:rfc:functions}numeric1"/>
 *         &lt;element name="Cantidad" type="{urn:sap-com:document:sap:rfc:functions}char200"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZcmlOrdPago", propOrder = {
    "mandt",
    "bukrs",
    "zlaufd",
    "zlaufi",
    "credito",
    "emisor",
    "fecha",
    "acreditado",
    "nombre",
    "importe",
    "status",
    "cantidad"
})
public class ZcmlOrdPago {

    @XmlElement(name = "Mandt", required = true)
    protected String mandt;
    @XmlElement(name = "Bukrs", required = true)
    protected String bukrs;
    @XmlElement(name = "Zlaufd", required = true)
    protected String zlaufd;
    @XmlElement(name = "Zlaufi", required = true)
    protected String zlaufi;
    @XmlElement(name = "Credito", required = true)
    protected String credito;
    @XmlElement(name = "Emisor", required = true)
    protected String emisor;
    @XmlElement(name = "Fecha", required = true)
    protected String fecha;
    @XmlElement(name = "Acreditado", required = true)
    protected String acreditado;
    @XmlElement(name = "Nombre", required = true)
    protected String nombre;
    @XmlElement(name = "Importe", required = true)
    protected String importe;
    @XmlElement(name = "Status", required = true)
    protected String status;
    @XmlElement(name = "Cantidad", required = true)
    protected String cantidad;

    /**
     * Obtiene el valor de la propiedad mandt.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMandt() {
        return mandt;
    }

    /**
     * Define el valor de la propiedad mandt.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMandt(String value) {
        this.mandt = value;
    }

    /**
     * Obtiene el valor de la propiedad bukrs.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBukrs() {
        return bukrs;
    }

    /**
     * Define el valor de la propiedad bukrs.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBukrs(String value) {
        this.bukrs = value;
    }

    /**
     * Obtiene el valor de la propiedad zlaufd.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZlaufd() {
        return zlaufd;
    }

    /**
     * Define el valor de la propiedad zlaufd.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZlaufd(String value) {
        this.zlaufd = value;
    }

    /**
     * Obtiene el valor de la propiedad zlaufi.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZlaufi() {
        return zlaufi;
    }

    /**
     * Define el valor de la propiedad zlaufi.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZlaufi(String value) {
        this.zlaufi = value;
    }

    /**
     * Obtiene el valor de la propiedad credito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCredito() {
        return credito;
    }

    /**
     * Define el valor de la propiedad credito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCredito(String value) {
        this.credito = value;
    }

    /**
     * Obtiene el valor de la propiedad emisor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmisor() {
        return emisor;
    }

    /**
     * Define el valor de la propiedad emisor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmisor(String value) {
        this.emisor = value;
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
     * Obtiene el valor de la propiedad acreditado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAcreditado() {
        return acreditado;
    }

    /**
     * Define el valor de la propiedad acreditado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAcreditado(String value) {
        this.acreditado = value;
    }

    /**
     * Obtiene el valor de la propiedad nombre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el valor de la propiedad nombre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Obtiene el valor de la propiedad importe.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImporte() {
        return importe;
    }

    /**
     * Define el valor de la propiedad importe.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImporte(String value) {
        this.importe = value;
    }

    /**
     * Obtiene el valor de la propiedad status.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Define el valor de la propiedad status.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Obtiene el valor de la propiedad cantidad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCantidad() {
        return cantidad;
    }

    /**
     * Define el valor de la propiedad cantidad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCantidad(String value) {
        this.cantidad = value;
    }

}
