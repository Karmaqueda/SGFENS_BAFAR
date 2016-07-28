
package com.sap.bafar.ws.aceptacontrato;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Bukrs_001" type="{urn:sap-com:document:sap:rfc:functions}char132" minOccurs="0"/>
 *         &lt;element name="Ctu" type="{urn:sap-com:document:sap:rfc:functions}char1" minOccurs="0"/>
 *         &lt;element name="Messtab" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBdcmsgcoll" minOccurs="0"/>
 *         &lt;element name="Mode" type="{urn:sap-com:document:sap:rfc:functions}char1" minOccurs="0"/>
 *         &lt;element name="Nodata" type="{urn:sap-com:document:sap:rfc:functions}char1" minOccurs="0"/>
 *         &lt;element name="Update" type="{urn:sap-com:document:sap:rfc:functions}char1" minOccurs="0"/>
 *         &lt;element name="Xranl_002" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "bukrs001",
    "ctu",
    "messtab",
    "mode",
    "nodata",
    "update",
    "xranl002"
})
@XmlRootElement(name = "ZfmtrAcceptContract")
public class ZfmtrAcceptContract {

    @XmlElement(name = "Bukrs_001")
    protected String bukrs001;
    @XmlElement(name = "Ctu")
    protected String ctu;
    @XmlElement(name = "Messtab")
    protected TableOfBdcmsgcoll messtab;
    @XmlElement(name = "Mode")
    protected String mode;
    @XmlElement(name = "Nodata")
    protected String nodata;
    @XmlElement(name = "Update")
    protected String update;
    @XmlElement(name = "Xranl_002", required = true)
    protected String xranl002;

    /**
     * Obtiene el valor de la propiedad bukrs001.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBukrs001() {
        return bukrs001;
    }

    /**
     * Define el valor de la propiedad bukrs001.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBukrs001(String value) {
        this.bukrs001 = value;
    }

    /**
     * Obtiene el valor de la propiedad ctu.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCtu() {
        return ctu;
    }

    /**
     * Define el valor de la propiedad ctu.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCtu(String value) {
        this.ctu = value;
    }

    /**
     * Obtiene el valor de la propiedad messtab.
     * 
     * @return
     *     possible object is
     *     {@link TableOfBdcmsgcoll }
     *     
     */
    public TableOfBdcmsgcoll getMesstab() {
        return messtab;
    }

    /**
     * Define el valor de la propiedad messtab.
     * 
     * @param value
     *     allowed object is
     *     {@link TableOfBdcmsgcoll }
     *     
     */
    public void setMesstab(TableOfBdcmsgcoll value) {
        this.messtab = value;
    }

    /**
     * Obtiene el valor de la propiedad mode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMode() {
        return mode;
    }

    /**
     * Define el valor de la propiedad mode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMode(String value) {
        this.mode = value;
    }

    /**
     * Obtiene el valor de la propiedad nodata.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodata() {
        return nodata;
    }

    /**
     * Define el valor de la propiedad nodata.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodata(String value) {
        this.nodata = value;
    }

    /**
     * Obtiene el valor de la propiedad update.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUpdate() {
        return update;
    }

    /**
     * Define el valor de la propiedad update.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpdate(String value) {
        this.update = value;
    }

    /**
     * Obtiene el valor de la propiedad xranl002.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXranl002() {
        return xranl002;
    }

    /**
     * Define el valor de la propiedad xranl002.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXranl002(String value) {
        this.xranl002 = value;
    }

}
