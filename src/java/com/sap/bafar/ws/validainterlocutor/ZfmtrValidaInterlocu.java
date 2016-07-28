
package com.sap.bafar.ws.validainterlocutor;

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
 *         &lt;element name="IApellidom" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="IApellidop" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="ICurp" type="{urn:sap-com:document:sap:rfc:functions}char20"/>
 *         &lt;element name="INombre" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="INombre2" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="IRfc" type="{urn:sap-com:document:sap:rfc:functions}char13"/>
 *         &lt;element name="Messtab" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBdcmsgcoll"/>
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
    "iApellidom",
    "iApellidop",
    "iCurp",
    "iNombre",
    "iNombre2",
    "iRfc",
    "messtab"
})
@XmlRootElement(name = "ZfmtrValidaInterlocu")
public class ZfmtrValidaInterlocu {

    @XmlElement(name = "IApellidom", required = true)
    protected String iApellidom;
    @XmlElement(name = "IApellidop", required = true)
    protected String iApellidop;
    @XmlElement(name = "ICurp", required = true)
    protected String iCurp;
    @XmlElement(name = "INombre", required = true)
    protected String iNombre;
    @XmlElement(name = "INombre2", required = true)
    protected String iNombre2;
    @XmlElement(name = "IRfc", required = true)
    protected String iRfc;
    @XmlElement(name = "Messtab", required = true)
    protected TableOfBdcmsgcoll messtab;

    /**
     * Obtiene el valor de la propiedad iApellidom.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIApellidom() {
        return iApellidom;
    }

    /**
     * Define el valor de la propiedad iApellidom.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIApellidom(String value) {
        this.iApellidom = value;
    }

    /**
     * Obtiene el valor de la propiedad iApellidop.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIApellidop() {
        return iApellidop;
    }

    /**
     * Define el valor de la propiedad iApellidop.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIApellidop(String value) {
        this.iApellidop = value;
    }

    /**
     * Obtiene el valor de la propiedad iCurp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getICurp() {
        return iCurp;
    }

    /**
     * Define el valor de la propiedad iCurp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setICurp(String value) {
        this.iCurp = value;
    }

    /**
     * Obtiene el valor de la propiedad iNombre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINombre() {
        return iNombre;
    }

    /**
     * Define el valor de la propiedad iNombre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINombre(String value) {
        this.iNombre = value;
    }

    /**
     * Obtiene el valor de la propiedad iNombre2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINombre2() {
        return iNombre2;
    }

    /**
     * Define el valor de la propiedad iNombre2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINombre2(String value) {
        this.iNombre2 = value;
    }

    /**
     * Obtiene el valor de la propiedad iRfc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIRfc() {
        return iRfc;
    }

    /**
     * Define el valor de la propiedad iRfc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIRfc(String value) {
        this.iRfc = value;
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

}
