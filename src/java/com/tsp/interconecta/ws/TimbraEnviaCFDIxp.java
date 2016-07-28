
package com.tsp.interconecta.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para timbraEnviaCFDIxp complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="timbraEnviaCFDIxp">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="user" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certificadoEmisor" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="llavePrivadaEmisor" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="llavePrivadaEmisorPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="xmlCFDI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="versionCFDI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "timbraEnviaCFDIxp", propOrder = {
    "user",
    "userPassword",
    "certificadoEmisor",
    "llavePrivadaEmisor",
    "llavePrivadaEmisorPassword",
    "xmlCFDI",
    "versionCFDI"
})
public class TimbraEnviaCFDIxp {

    protected String user;
    protected String userPassword;
    @XmlElementRef(name = "certificadoEmisor", type = JAXBElement.class, required = false)
    protected JAXBElement<byte[]> certificadoEmisor;
    @XmlElementRef(name = "llavePrivadaEmisor", type = JAXBElement.class, required = false)
    protected JAXBElement<byte[]> llavePrivadaEmisor;
    protected String llavePrivadaEmisorPassword;
    protected String xmlCFDI;
    protected String versionCFDI;

    /**
     * Obtiene el valor de la propiedad user.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUser() {
        return user;
    }

    /**
     * Define el valor de la propiedad user.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUser(String value) {
        this.user = value;
    }

    /**
     * Obtiene el valor de la propiedad userPassword.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * Define el valor de la propiedad userPassword.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserPassword(String value) {
        this.userPassword = value;
    }

    /**
     * Obtiene el valor de la propiedad certificadoEmisor.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public JAXBElement<byte[]> getCertificadoEmisor() {
        return certificadoEmisor;
    }

    /**
     * Define el valor de la propiedad certificadoEmisor.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public void setCertificadoEmisor(JAXBElement<byte[]> value) {
        this.certificadoEmisor = value;
    }

    /**
     * Obtiene el valor de la propiedad llavePrivadaEmisor.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public JAXBElement<byte[]> getLlavePrivadaEmisor() {
        return llavePrivadaEmisor;
    }

    /**
     * Define el valor de la propiedad llavePrivadaEmisor.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public void setLlavePrivadaEmisor(JAXBElement<byte[]> value) {
        this.llavePrivadaEmisor = value;
    }

    /**
     * Obtiene el valor de la propiedad llavePrivadaEmisorPassword.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLlavePrivadaEmisorPassword() {
        return llavePrivadaEmisorPassword;
    }

    /**
     * Define el valor de la propiedad llavePrivadaEmisorPassword.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLlavePrivadaEmisorPassword(String value) {
        this.llavePrivadaEmisorPassword = value;
    }

    /**
     * Obtiene el valor de la propiedad xmlCFDI.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXmlCFDI() {
        return xmlCFDI;
    }

    /**
     * Define el valor de la propiedad xmlCFDI.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXmlCFDI(String value) {
        this.xmlCFDI = value;
    }

    /**
     * Obtiene el valor de la propiedad versionCFDI.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionCFDI() {
        return versionCFDI;
    }

    /**
     * Define el valor de la propiedad versionCFDI.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionCFDI(String value) {
        this.versionCFDI = value;
    }

}
