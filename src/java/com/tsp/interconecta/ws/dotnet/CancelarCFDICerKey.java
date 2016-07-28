
package com.tsp.interconecta.ws.dotnet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="rfcEmisor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaHoraTimbre" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="UUID" type="{http://net.ws.facturacionelectronica.tspsoftware.com/}ArrayOfString" minOccurs="0"/>
 *         &lt;element name="bytesCer" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="keyBase64" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "rfcEmisor",
    "fechaHoraTimbre",
    "uuid",
    "bytesCer",
    "keyBase64"
})
@XmlRootElement(name = "CancelarCFDICerKey")
public class CancelarCFDICerKey {

    protected String rfcEmisor;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaHoraTimbre;
    @XmlElement(name = "UUID")
    protected ArrayOfString uuid;
    protected byte[] bytesCer;
    protected String keyBase64;

    /**
     * Gets the value of the rfcEmisor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfcEmisor() {
        return rfcEmisor;
    }

    /**
     * Sets the value of the rfcEmisor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfcEmisor(String value) {
        this.rfcEmisor = value;
    }

    /**
     * Gets the value of the fechaHoraTimbre property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaHoraTimbre() {
        return fechaHoraTimbre;
    }

    /**
     * Sets the value of the fechaHoraTimbre property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaHoraTimbre(XMLGregorianCalendar value) {
        this.fechaHoraTimbre = value;
    }

    /**
     * Gets the value of the uuid property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getUUID() {
        return uuid;
    }

    /**
     * Sets the value of the uuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setUUID(ArrayOfString value) {
        this.uuid = value;
    }

    /**
     * Gets the value of the bytesCer property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBytesCer() {
        return bytesCer;
    }

    /**
     * Sets the value of the bytesCer property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBytesCer(byte[] value) {
        this.bytesCer = ((byte[]) value);
    }

    /**
     * Gets the value of the keyBase64 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyBase64() {
        return keyBase64;
    }

    /**
     * Sets the value of the keyBase64 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyBase64(String value) {
        this.keyBase64 = value;
    }

}
