
package com.tsp.interconecta.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para timbraEnviaCFDIBytesSPrimario complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="timbraEnviaCFDIBytesSPrimario">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="user" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certificadoSPrimario" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="llavePrivadaSPrimario" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="llavePrivadaPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bytesXmlCFDI" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
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
@XmlType(name = "timbraEnviaCFDIBytesSPrimario", propOrder = {
    "user",
    "userPassword",
    "certificadoSPrimario",
    "llavePrivadaSPrimario",
    "llavePrivadaPassword",
    "bytesXmlCFDI",
    "versionCFDI"
})
public class TimbraEnviaCFDIBytesSPrimario {

    protected String user;
    protected String userPassword;
    @XmlElementRef(name = "certificadoSPrimario", type = JAXBElement.class, required = false)
    protected JAXBElement<byte[]> certificadoSPrimario;
    @XmlElementRef(name = "llavePrivadaSPrimario", type = JAXBElement.class, required = false)
    protected JAXBElement<byte[]> llavePrivadaSPrimario;
    protected String llavePrivadaPassword;
    @XmlElementRef(name = "bytesXmlCFDI", type = JAXBElement.class, required = false)
    protected JAXBElement<byte[]> bytesXmlCFDI;
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
     * Obtiene el valor de la propiedad certificadoSPrimario.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public JAXBElement<byte[]> getCertificadoSPrimario() {
        return certificadoSPrimario;
    }

    /**
     * Define el valor de la propiedad certificadoSPrimario.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public void setCertificadoSPrimario(JAXBElement<byte[]> value) {
        this.certificadoSPrimario = value;
    }

    /**
     * Obtiene el valor de la propiedad llavePrivadaSPrimario.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public JAXBElement<byte[]> getLlavePrivadaSPrimario() {
        return llavePrivadaSPrimario;
    }

    /**
     * Define el valor de la propiedad llavePrivadaSPrimario.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public void setLlavePrivadaSPrimario(JAXBElement<byte[]> value) {
        this.llavePrivadaSPrimario = value;
    }

    /**
     * Obtiene el valor de la propiedad llavePrivadaPassword.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLlavePrivadaPassword() {
        return llavePrivadaPassword;
    }

    /**
     * Define el valor de la propiedad llavePrivadaPassword.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLlavePrivadaPassword(String value) {
        this.llavePrivadaPassword = value;
    }

    /**
     * Obtiene el valor de la propiedad bytesXmlCFDI.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public JAXBElement<byte[]> getBytesXmlCFDI() {
        return bytesXmlCFDI;
    }

    /**
     * Define el valor de la propiedad bytesXmlCFDI.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public void setBytesXmlCFDI(JAXBElement<byte[]> value) {
        this.bytesXmlCFDI = value;
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
