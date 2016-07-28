
package com.sap.bafar.ws.creacontrato;

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
 *         &lt;element name="Messtab" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBdcmsgcoll"/>
 *         &lt;element name="Subrc" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TAmort" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfZlmTablaAmort"/>
 *         &lt;element name="TContac" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfZstContPerson"/>
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
    "messtab",
    "subrc",
    "tAmort",
    "tContac"
})
@XmlRootElement(name = "ZfmtrCreateInterlocuResponse")
public class ZfmtrCreateInterlocuResponse {

    @XmlElement(name = "Messtab", required = true)
    protected TableOfBdcmsgcoll messtab;
    @XmlElement(name = "Subrc")
    protected int subrc;
    @XmlElement(name = "TAmort", required = true)
    protected TableOfZlmTablaAmort tAmort;
    @XmlElement(name = "TContac", required = true)
    protected TableOfZstContPerson tContac;

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
     * Obtiene el valor de la propiedad subrc.
     * 
     */
    public int getSubrc() {
        return subrc;
    }

    /**
     * Define el valor de la propiedad subrc.
     * 
     */
    public void setSubrc(int value) {
        this.subrc = value;
    }

    /**
     * Obtiene el valor de la propiedad tAmort.
     * 
     * @return
     *     possible object is
     *     {@link TableOfZlmTablaAmort }
     *     
     */
    public TableOfZlmTablaAmort getTAmort() {
        return tAmort;
    }

    /**
     * Define el valor de la propiedad tAmort.
     * 
     * @param value
     *     allowed object is
     *     {@link TableOfZlmTablaAmort }
     *     
     */
    public void setTAmort(TableOfZlmTablaAmort value) {
        this.tAmort = value;
    }

    /**
     * Obtiene el valor de la propiedad tContac.
     * 
     * @return
     *     possible object is
     *     {@link TableOfZstContPerson }
     *     
     */
    public TableOfZstContPerson getTContac() {
        return tContac;
    }

    /**
     * Define el valor de la propiedad tContac.
     * 
     * @param value
     *     allowed object is
     *     {@link TableOfZstContPerson }
     *     
     */
    public void setTContac(TableOfZstContPerson value) {
        this.tContac = value;
    }

}
