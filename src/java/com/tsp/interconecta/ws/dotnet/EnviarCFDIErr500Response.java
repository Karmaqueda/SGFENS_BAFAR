
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
 *         &lt;element name="EnviarCFDI_Err500Result" type="{http://net.ws.facturacionelectronica.tspsoftware.com/}RespuestaEnvio" minOccurs="0"/>
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
    "enviarCFDIErr500Result"
})
@XmlRootElement(name = "EnviarCFDI_Err500Response")
public class EnviarCFDIErr500Response {

    @XmlElement(name = "EnviarCFDI_Err500Result")
    protected RespuestaEnvio enviarCFDIErr500Result;

    /**
     * Gets the value of the enviarCFDIErr500Result property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaEnvio }
     *     
     */
    public RespuestaEnvio getEnviarCFDIErr500Result() {
        return enviarCFDIErr500Result;
    }

    /**
     * Sets the value of the enviarCFDIErr500Result property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaEnvio }
     *     
     */
    public void setEnviarCFDIErr500Result(RespuestaEnvio value) {
        this.enviarCFDIErr500Result = value;
    }

}
