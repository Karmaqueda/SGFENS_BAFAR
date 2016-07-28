
package com.tsp.interconecta.ws.dotnet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RespuestaCancelacion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RespuestaCancelacion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AcuseCancelacion" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="Error" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FoliosCancelacion" type="{http://net.ws.facturacionelectronica.tspsoftware.com/}ArrayOfFolioCancelacion" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RespuestaCancelacion", propOrder = {
    "acuseCancelacion",
    "error",
    "foliosCancelacion"
})
public class RespuestaCancelacion {

    @XmlElement(name = "AcuseCancelacion")
    protected byte[] acuseCancelacion;
    @XmlElement(name = "Error")
    protected String error;
    @XmlElement(name = "FoliosCancelacion")
    protected ArrayOfFolioCancelacion foliosCancelacion;

    /**
     * Gets the value of the acuseCancelacion property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getAcuseCancelacion() {
        return acuseCancelacion;
    }

    /**
     * Sets the value of the acuseCancelacion property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setAcuseCancelacion(byte[] value) {
        this.acuseCancelacion = ((byte[]) value);
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

    /**
     * Gets the value of the foliosCancelacion property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfFolioCancelacion }
     *     
     */
    public ArrayOfFolioCancelacion getFoliosCancelacion() {
        return foliosCancelacion;
    }

    /**
     * Sets the value of the foliosCancelacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfFolioCancelacion }
     *     
     */
    public void setFoliosCancelacion(ArrayOfFolioCancelacion value) {
        this.foliosCancelacion = value;
    }

}
