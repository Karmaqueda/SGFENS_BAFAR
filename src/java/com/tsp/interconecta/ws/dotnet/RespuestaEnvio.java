
package com.tsp.interconecta.ws.dotnet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RespuestaEnvio complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RespuestaEnvio">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AcuseEnvio" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="Error" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RespuestaEnvio", propOrder = {
    "acuseEnvio",
    "error"
})
public class RespuestaEnvio {

    @XmlElement(name = "AcuseEnvio")
    protected byte[] acuseEnvio;
    @XmlElement(name = "Error")
    protected String error;

    /**
     * Gets the value of the acuseEnvio property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getAcuseEnvio() {
        return acuseEnvio;
    }

    /**
     * Sets the value of the acuseEnvio property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setAcuseEnvio(byte[] value) {
        this.acuseEnvio = ((byte[]) value);
    }

    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setError(String value) {
        this.error = value;
    }

}
