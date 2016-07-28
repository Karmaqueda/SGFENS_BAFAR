
package com.tsp.interconecta.ws.dotnet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="bytesXMLPeticionCancelacion" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
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
    "bytesXMLPeticionCancelacion"
})
@XmlRootElement(name = "CancelarCFDI_PeticionPreFormada")
public class CancelarCFDIPeticionPreFormada {

    protected byte[] bytesXMLPeticionCancelacion;

    /**
     * Gets the value of the bytesXMLPeticionCancelacion property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBytesXMLPeticionCancelacion() {
        return bytesXMLPeticionCancelacion;
    }

    /**
     * Sets the value of the bytesXMLPeticionCancelacion property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBytesXMLPeticionCancelacion(byte[] value) {
        this.bytesXMLPeticionCancelacion = ((byte[]) value);
    }

}
