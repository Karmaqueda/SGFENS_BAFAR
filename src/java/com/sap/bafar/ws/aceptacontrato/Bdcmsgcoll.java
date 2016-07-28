
package com.sap.bafar.ws.aceptacontrato;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Bdcmsgcoll complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Bdcmsgcoll">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Tcode" type="{urn:sap-com:document:sap:rfc:functions}char20"/>
 *         &lt;element name="Dyname" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="Dynumb" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="Msgtyp" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="Msgspra" type="{urn:sap-com:document:sap:rfc:functions}lang"/>
 *         &lt;element name="Msgid" type="{urn:sap-com:document:sap:rfc:functions}char20"/>
 *         &lt;element name="Msgnr" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="Msgv1" type="{urn:sap-com:document:sap:rfc:functions}char100"/>
 *         &lt;element name="Msgv2" type="{urn:sap-com:document:sap:rfc:functions}char100"/>
 *         &lt;element name="Msgv3" type="{urn:sap-com:document:sap:rfc:functions}char100"/>
 *         &lt;element name="Msgv4" type="{urn:sap-com:document:sap:rfc:functions}char100"/>
 *         &lt;element name="Env" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="Fldname" type="{urn:sap-com:document:sap:rfc:functions}char132"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bdcmsgcoll", propOrder = {
    "tcode",
    "dyname",
    "dynumb",
    "msgtyp",
    "msgspra",
    "msgid",
    "msgnr",
    "msgv1",
    "msgv2",
    "msgv3",
    "msgv4",
    "env",
    "fldname"
})
public class Bdcmsgcoll {

    @XmlElement(name = "Tcode", required = true)
    protected String tcode;
    @XmlElement(name = "Dyname", required = true)
    protected String dyname;
    @XmlElement(name = "Dynumb", required = true)
    protected String dynumb;
    @XmlElement(name = "Msgtyp", required = true)
    protected String msgtyp;
    @XmlElement(name = "Msgspra", required = true)
    protected String msgspra;
    @XmlElement(name = "Msgid", required = true)
    protected String msgid;
    @XmlElement(name = "Msgnr", required = true)
    protected String msgnr;
    @XmlElement(name = "Msgv1", required = true)
    protected String msgv1;
    @XmlElement(name = "Msgv2", required = true)
    protected String msgv2;
    @XmlElement(name = "Msgv3", required = true)
    protected String msgv3;
    @XmlElement(name = "Msgv4", required = true)
    protected String msgv4;
    @XmlElement(name = "Env", required = true)
    protected String env;
    @XmlElement(name = "Fldname", required = true)
    protected String fldname;

    /**
     * Obtiene el valor de la propiedad tcode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTcode() {
        return tcode;
    }

    /**
     * Define el valor de la propiedad tcode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTcode(String value) {
        this.tcode = value;
    }

    /**
     * Obtiene el valor de la propiedad dyname.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDyname() {
        return dyname;
    }

    /**
     * Define el valor de la propiedad dyname.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDyname(String value) {
        this.dyname = value;
    }

    /**
     * Obtiene el valor de la propiedad dynumb.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDynumb() {
        return dynumb;
    }

    /**
     * Define el valor de la propiedad dynumb.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDynumb(String value) {
        this.dynumb = value;
    }

    /**
     * Obtiene el valor de la propiedad msgtyp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgtyp() {
        return msgtyp;
    }

    /**
     * Define el valor de la propiedad msgtyp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgtyp(String value) {
        this.msgtyp = value;
    }

    /**
     * Obtiene el valor de la propiedad msgspra.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgspra() {
        return msgspra;
    }

    /**
     * Define el valor de la propiedad msgspra.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgspra(String value) {
        this.msgspra = value;
    }

    /**
     * Obtiene el valor de la propiedad msgid.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgid() {
        return msgid;
    }

    /**
     * Define el valor de la propiedad msgid.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgid(String value) {
        this.msgid = value;
    }

    /**
     * Obtiene el valor de la propiedad msgnr.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgnr() {
        return msgnr;
    }

    /**
     * Define el valor de la propiedad msgnr.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgnr(String value) {
        this.msgnr = value;
    }

    /**
     * Obtiene el valor de la propiedad msgv1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgv1() {
        return msgv1;
    }

    /**
     * Define el valor de la propiedad msgv1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgv1(String value) {
        this.msgv1 = value;
    }

    /**
     * Obtiene el valor de la propiedad msgv2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgv2() {
        return msgv2;
    }

    /**
     * Define el valor de la propiedad msgv2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgv2(String value) {
        this.msgv2 = value;
    }

    /**
     * Obtiene el valor de la propiedad msgv3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgv3() {
        return msgv3;
    }

    /**
     * Define el valor de la propiedad msgv3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgv3(String value) {
        this.msgv3 = value;
    }

    /**
     * Obtiene el valor de la propiedad msgv4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgv4() {
        return msgv4;
    }

    /**
     * Define el valor de la propiedad msgv4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgv4(String value) {
        this.msgv4 = value;
    }

    /**
     * Obtiene el valor de la propiedad env.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnv() {
        return env;
    }

    /**
     * Define el valor de la propiedad env.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnv(String value) {
        this.env = value;
    }

    /**
     * Obtiene el valor de la propiedad fldname.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFldname() {
        return fldname;
    }

    /**
     * Define el valor de la propiedad fldname.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFldname(String value) {
        this.fldname = value;
    }

}
