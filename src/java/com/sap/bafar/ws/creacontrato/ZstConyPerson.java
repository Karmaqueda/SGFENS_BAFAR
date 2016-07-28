
package com.sap.bafar.ws.creacontrato;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ZstConyPerson complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ZstConyPerson">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Nombre1" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="Nombre2" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="ApPat" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="ApMat" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="Empresa" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="Puesto" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="Antig" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="Sueldo" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="CalleEmpr" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="NoExt" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="NoInt" type="{urn:sap-com:document:sap:rfc:functions}char5"/>
 *         &lt;element name="Colonia" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="Cp" type="{urn:sap-com:document:sap:rfc:functions}char5"/>
 *         &lt;element name="Ciudad" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="Estado" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="TelOf" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="Celular" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZstConyPerson", propOrder = {
    "nombre1",
    "nombre2",
    "apPat",
    "apMat",
    "empresa",
    "puesto",
    "antig",
    "sueldo",
    "calleEmpr",
    "noExt",
    "noInt",
    "colonia",
    "cp",
    "ciudad",
    "estado",
    "telOf",
    "celular"
})
public class ZstConyPerson {

    @XmlElement(name = "Nombre1", required = true)
    protected String nombre1;
    @XmlElement(name = "Nombre2", required = true)
    protected String nombre2;
    @XmlElement(name = "ApPat", required = true)
    protected String apPat;
    @XmlElement(name = "ApMat", required = true)
    protected String apMat;
    @XmlElement(name = "Empresa", required = true)
    protected String empresa;
    @XmlElement(name = "Puesto", required = true)
    protected String puesto;
    @XmlElement(name = "Antig", required = true)
    protected String antig;
    @XmlElement(name = "Sueldo", required = true)
    protected String sueldo;
    @XmlElement(name = "CalleEmpr", required = true)
    protected String calleEmpr;
    @XmlElement(name = "NoExt", required = true)
    protected String noExt;
    @XmlElement(name = "NoInt", required = true)
    protected String noInt;
    @XmlElement(name = "Colonia", required = true)
    protected String colonia;
    @XmlElement(name = "Cp", required = true)
    protected String cp;
    @XmlElement(name = "Ciudad", required = true)
    protected String ciudad;
    @XmlElement(name = "Estado", required = true)
    protected String estado;
    @XmlElement(name = "TelOf", required = true)
    protected String telOf;
    @XmlElement(name = "Celular", required = true)
    protected String celular;

    /**
     * Obtiene el valor de la propiedad nombre1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre1() {
        return nombre1;
    }

    /**
     * Define el valor de la propiedad nombre1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre1(String value) {
        this.nombre1 = value;
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
     * Obtiene el valor de la propiedad apPat.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApPat() {
        return apPat;
    }

    /**
     * Define el valor de la propiedad apPat.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApPat(String value) {
        this.apPat = value;
    }

    /**
     * Obtiene el valor de la propiedad apMat.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApMat() {
        return apMat;
    }

    /**
     * Define el valor de la propiedad apMat.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApMat(String value) {
        this.apMat = value;
    }

    /**
     * Obtiene el valor de la propiedad empresa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmpresa() {
        return empresa;
    }

    /**
     * Define el valor de la propiedad empresa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmpresa(String value) {
        this.empresa = value;
    }

    /**
     * Obtiene el valor de la propiedad puesto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPuesto() {
        return puesto;
    }

    /**
     * Define el valor de la propiedad puesto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPuesto(String value) {
        this.puesto = value;
    }

    /**
     * Obtiene el valor de la propiedad antig.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAntig() {
        return antig;
    }

    /**
     * Define el valor de la propiedad antig.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAntig(String value) {
        this.antig = value;
    }

    /**
     * Obtiene el valor de la propiedad sueldo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSueldo() {
        return sueldo;
    }

    /**
     * Define el valor de la propiedad sueldo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSueldo(String value) {
        this.sueldo = value;
    }

    /**
     * Obtiene el valor de la propiedad calleEmpr.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCalleEmpr() {
        return calleEmpr;
    }

    /**
     * Define el valor de la propiedad calleEmpr.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCalleEmpr(String value) {
        this.calleEmpr = value;
    }

    /**
     * Obtiene el valor de la propiedad noExt.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoExt() {
        return noExt;
    }

    /**
     * Define el valor de la propiedad noExt.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoExt(String value) {
        this.noExt = value;
    }

    /**
     * Obtiene el valor de la propiedad noInt.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoInt() {
        return noInt;
    }

    /**
     * Define el valor de la propiedad noInt.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoInt(String value) {
        this.noInt = value;
    }

    /**
     * Obtiene el valor de la propiedad colonia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColonia() {
        return colonia;
    }

    /**
     * Define el valor de la propiedad colonia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColonia(String value) {
        this.colonia = value;
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
     * Obtiene el valor de la propiedad ciudad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCiudad() {
        return ciudad;
    }

    /**
     * Define el valor de la propiedad ciudad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCiudad(String value) {
        this.ciudad = value;
    }

    /**
     * Obtiene el valor de la propiedad estado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Define el valor de la propiedad estado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstado(String value) {
        this.estado = value;
    }

    /**
     * Obtiene el valor de la propiedad telOf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelOf() {
        return telOf;
    }

    /**
     * Define el valor de la propiedad telOf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelOf(String value) {
        this.telOf = value;
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
