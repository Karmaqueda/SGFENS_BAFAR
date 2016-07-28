
package com.sap.bafar.ws.creacontrato;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ZstGastoPerson complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ZstGastoPerson">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Aliment" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="Renta" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="Servicios" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="Salud" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="Educac" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="Deudas" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="Otros" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZstGastoPerson", propOrder = {
    "aliment",
    "renta",
    "servicios",
    "salud",
    "educac",
    "deudas",
    "otros"
})
public class ZstGastoPerson {

    @XmlElement(name = "Aliment", required = true)
    protected String aliment;
    @XmlElement(name = "Renta", required = true)
    protected String renta;
    @XmlElement(name = "Servicios", required = true)
    protected String servicios;
    @XmlElement(name = "Salud", required = true)
    protected String salud;
    @XmlElement(name = "Educac", required = true)
    protected String educac;
    @XmlElement(name = "Deudas", required = true)
    protected String deudas;
    @XmlElement(name = "Otros", required = true)
    protected String otros;

    /**
     * Obtiene el valor de la propiedad aliment.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAliment() {
        return aliment;
    }

    /**
     * Define el valor de la propiedad aliment.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAliment(String value) {
        this.aliment = value;
    }

    /**
     * Obtiene el valor de la propiedad renta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRenta() {
        return renta;
    }

    /**
     * Define el valor de la propiedad renta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRenta(String value) {
        this.renta = value;
    }

    /**
     * Obtiene el valor de la propiedad servicios.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServicios() {
        return servicios;
    }

    /**
     * Define el valor de la propiedad servicios.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServicios(String value) {
        this.servicios = value;
    }

    /**
     * Obtiene el valor de la propiedad salud.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSalud() {
        return salud;
    }

    /**
     * Define el valor de la propiedad salud.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSalud(String value) {
        this.salud = value;
    }

    /**
     * Obtiene el valor de la propiedad educac.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEducac() {
        return educac;
    }

    /**
     * Define el valor de la propiedad educac.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEducac(String value) {
        this.educac = value;
    }

    /**
     * Obtiene el valor de la propiedad deudas.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeudas() {
        return deudas;
    }

    /**
     * Define el valor de la propiedad deudas.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeudas(String value) {
        this.deudas = value;
    }

    /**
     * Obtiene el valor de la propiedad otros.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOtros() {
        return otros;
    }

    /**
     * Define el valor de la propiedad otros.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOtros(String value) {
        this.otros = value;
    }

}
