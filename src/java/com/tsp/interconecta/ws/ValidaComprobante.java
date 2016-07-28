
package com.tsp.interconecta.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para validaComprobante complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="validaComprobante">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="user" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="xmlComprobante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bytesXmlComprobante" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="versionComprobante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "validaComprobante", propOrder = {
    "user",
    "userPassword",
    "xmlComprobante",
    "bytesXmlComprobante",
    "versionComprobante"
})
public class ValidaComprobante {

    protected String user;
    protected String userPassword;
    protected String xmlComprobante;
    @XmlElementRef(name = "bytesXmlComprobante", type = JAXBElement.class, required = false)
    protected JAXBElement<byte[]> bytesXmlComprobante;
    protected String versionComprobante;

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
     * Obtiene el valor de la propiedad xmlComprobante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXmlComprobante() {
        return xmlComprobante;
    }

    /**
     * Define el valor de la propiedad xmlComprobante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXmlComprobante(String value) {
        this.xmlComprobante = value;
    }

    /**
     * Obtiene el valor de la propiedad bytesXmlComprobante.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public JAXBElement<byte[]> getBytesXmlComprobante() {
        return bytesXmlComprobante;
    }

    /**
     * Define el valor de la propiedad bytesXmlComprobante.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public void setBytesXmlComprobante(JAXBElement<byte[]> value) {
        this.bytesXmlComprobante = value;
    }

    /**
     * Obtiene el valor de la propiedad versionComprobante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionComprobante() {
        return versionComprobante;
    }

    /**
     * Define el valor de la propiedad versionComprobante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionComprobante(String value) {
        this.versionComprobante = value;
    }

}
