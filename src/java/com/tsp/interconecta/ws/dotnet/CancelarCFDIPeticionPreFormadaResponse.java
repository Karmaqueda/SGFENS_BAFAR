
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
 *         &lt;element name="CancelarCFDI_PeticionPreFormadaResult" type="{http://net.ws.facturacionelectronica.tspsoftware.com/}RespuestaCancelacion" minOccurs="0"/>
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
    "cancelarCFDIPeticionPreFormadaResult"
})
@XmlRootElement(name = "CancelarCFDI_PeticionPreFormadaResponse")
public class CancelarCFDIPeticionPreFormadaResponse {

    @XmlElement(name = "CancelarCFDI_PeticionPreFormadaResult")
    protected RespuestaCancelacion cancelarCFDIPeticionPreFormadaResult;

    /**
     * Gets the value of the cancelarCFDIPeticionPreFormadaResult property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaCancelacion }
     *     
     */
    public RespuestaCancelacion getCancelarCFDIPeticionPreFormadaResult() {
        return cancelarCFDIPeticionPreFormadaResult;
    }

    /**
     * Sets the value of the cancelarCFDIPeticionPreFormadaResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaCancelacion }
     *     
     */
    public void setCancelarCFDIPeticionPreFormadaResult(RespuestaCancelacion value) {
        this.cancelarCFDIPeticionPreFormadaResult = value;
    }

}
