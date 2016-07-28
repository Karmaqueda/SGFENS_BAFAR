
package com.sap.bafar.ws.creacontrato;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ZstDirecPerson complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ZstDirecPerson">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Nombre" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="Nombre2" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="Apellidop" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="Apellidom" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="Calle" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="Numero" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="Cp" type="{urn:sap-com:document:sap:rfc:functions}char5"/>
 *         &lt;element name="Poblac" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="Pais" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="Region" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="Telef" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="Ext" type="{urn:sap-com:document:sap:rfc:functions}char5"/>
 *         &lt;element name="Email" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="Celular" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZstDirecPerson", propOrder = {
    "nombre",
    "nombre2",
    "apellidop",
    "apellidom",
    "calle",
    "numero",
    "cp",
    "poblac",
    "pais",
    "region",
    "telef",
    "ext",
    "email",
    "celular"
})
public class ZstDirecPerson {

    @XmlElement(name = "Nombre", required = true)
    protected String nombre;
    @XmlElement(name = "Nombre2", required = true)
    protected String nombre2;
    @XmlElement(name = "Apellidop", required = true)
    protected String apellidop;
    @XmlElement(name = "Apellidom", required = true)
    protected String apellidom;
    @XmlElement(name = "Calle", required = true)
    protected String calle;
    @XmlElement(name = "Numero", required = true)
    protected String numero;
    @XmlElement(name = "Cp", required = true)
    protected String cp;
    @XmlElement(name = "Poblac", required = true)
    protected String poblac;
    @XmlElement(name = "Pais", required = true)
    protected String pais;
    @XmlElement(name = "Region", required = true)
    protected String region;
    @XmlElement(name = "Telef", required = true)
    protected String telef;
    @XmlElement(name = "Ext", required = true)
    protected String ext;
    @XmlElement(name = "Email", required = true)
    protected String email;
    @XmlElement(name = "Celular", required = true)
    protected String celular;

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
     * Obtiene el valor de la propiedad nombre2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre2() {
        return nombre2;
    }

    /**
     * Define el valor de la propiedad nombre2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre2(String value) {
        this.nombre2 = value;
    }

    /**
     * Obtiene el valor de la propiedad apellidop.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApellidop() {
        return apellidop;
    }

    /**
     * Define el valor de la propiedad apellidop.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApellidop(String value) {
        this.apellidop = value;
    }

    /**
     * Obtiene el valor de la propiedad apellidom.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApellidom() {
        return apellidom;
    }

    /**
     * Define el valor de la propiedad apellidom.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApellidom(String value) {
        this.apellidom = value;
    }

    /**
     * Obtiene el valor de la propiedad calle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCalle() {
        return calle;
    }

    /**
     * Define el valor de la propiedad calle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCalle(String value) {
        this.calle = value;
    }

    /**
     * Obtiene el valor de la propiedad numero.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Define el valor de la propiedad numero.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumero(String value) {
        this.numero = value;
    }

    /**
     * Obtiene el valor de la propiedad cp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCp() {
        return cp;
    }

    /**
     * Define el valor de la propiedad cp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCp(String value) {
        this.cp = value;
    }

    /**
     * Obtiene el valor de la propiedad poblac.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoblac() {
        return poblac;
    }

    /**
     * Define el valor de la propiedad poblac.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoblac(String value) {
        this.poblac = value;
    }

    /**
     * Obtiene el valor de la propiedad pais.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPais() {
        return pais;
    }

    /**
     * Define el valor de la propiedad pais.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPais(String value) {
        this.pais = value;
    }

    /**
     * Obtiene el valor de la propiedad region.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegion() {
        return region;
    }

    /**
     * Define el valor de la propiedad region.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegion(String value) {
        this.region = value;
    }

    /**
     * Obtiene el valor de la propiedad telef.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelef() {
        return telef;
    }

    /**
     * Define el valor de la propiedad telef.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelef(String value) {
        this.telef = value;
    }

    /**
     * Obtiene el valor de la propiedad ext.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExt() {
        return ext;
    }

    /**
     * Define el valor de la propiedad ext.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExt(String value) {
        this.ext = value;
    }

    /**
     * Obtiene el valor de la propiedad email.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define el valor de la propiedad email.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Obtiene el valor de la propiedad celular.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCelular() {
        return celular;
    }

    /**
     * Define el valor de la propiedad celular.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCelular(String value) {
        this.celular = value;
    }

}
