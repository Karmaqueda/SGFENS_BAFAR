
package com.sap.bafar.ws.creacontrato;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ZstContPerson complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ZstContPerson">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ApPat" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="ApMat" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="Nombre1" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="Nobre2" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="Telef" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="Relac" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="Calle" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="Colonia" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="NoExt" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="NoInt" type="{urn:sap-com:document:sap:rfc:functions}char5"/>
 *         &lt;element name="Cp" type="{urn:sap-com:document:sap:rfc:functions}char5"/>
 *         &lt;element name="Ciudad" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZstContPerson", propOrder = {
    "apPat",
    "apMat",
    "nombre1",
    "nobre2",
    "telef",
    "relac",
    "calle",
    "colonia",
    "noExt",
    "noInt",
    "cp",
    "ciudad"
})
public class ZstContPerson {

    @XmlElement(name = "ApPat", required = true)
    protected String apPat;
    @XmlElement(name = "ApMat", required = true)
    protected String apMat;
    @XmlElement(name = "Nombre1", required = true)
    protected String nombre1;
    @XmlElement(name = "Nobre2", required = true)
    protected String nobre2;
    @XmlElement(name = "Telef", required = true)
    protected String telef;
    @XmlElement(name = "Relac", required = true)
    protected String relac;
    @XmlElement(name = "Calle", required = true)
    protected String calle;
    @XmlElement(name = "Colonia", required = true)
    protected String colonia;
    @XmlElement(name = "NoExt", required = true)
    protected String noExt;
    @XmlElement(name = "NoInt", required = true)
    protected String noInt;
    @XmlElement(name = "Cp", required = true)
    protected String cp;
    @XmlElement(name = "Ciudad", required = true)
    protected String ciudad;

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
     * Obtiene el valor de la propiedad nobre2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNobre2() {
        return nobre2;
    }

    /**
     * Define el valor de la propiedad nobre2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNobre2(String value) {
        this.nobre2 = value;
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
     * Obtiene el valor de la propiedad relac.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelac() {
        return relac;
    }

    /**
     * Define el valor de la propiedad relac.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelac(String value) {
        this.relac = value;
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

}
