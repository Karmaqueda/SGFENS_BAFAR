
package com.tsp.interconecta.ws.dotnet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EnviarCFDIResult" type="{http://net.ws.facturacionelectronica.tspsoftware.com/}RespuestaEnvio" minOccurs="0"/>
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
    "enviarCFDIResult"
})
@XmlRootElement(name = "EnviarCFDIResponse")
public class EnviarCFDIResponse {

    @XmlElement(name = "EnviarCFDIResult")
    protected RespuestaEnvio enviarCFDIResult;

    /**
     * Gets the value of the enviarCFDIResult property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaEnvio }
     *     
     */
    public RespuestaEnvio getEnviarCFDIResult() {
        return enviarCFDIResult;
    }

    /**
     * Sets the value of the enviarCFDIResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaEnvio }
     *     
     */
    public void setEnviarCFDIResult(RespuestaEnvio value) {
        this.enviarCFDIResult = value;
    }

}
