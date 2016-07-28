
package com.tsp.interconecta.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para wsGenericRespExt complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="wsGenericRespExt">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="acuse" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="arrayFoliosCancelacion" type="{http://ws.interconecta.tsp.com/}wsArregloCancelacion" minOccurs="0"/>
 *         &lt;element name="cadenaOriginal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaHoraTimbrado" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="folioCodCancelacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="folioUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isError" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="numError" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="selloDigitalEmisor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="selloDigitalTimbreSAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="timbreFiscalDigital" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="XML" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsGenericRespExt", propOrder = {
    "acuse",
    "arrayFoliosCancelacion",
    "cadenaOriginal",
    "errorMessage",
    "fechaHoraTimbrado",
    "folioCodCancelacion",
    "folioUUID",
    "isError",
    "numError",
    "selloDigitalEmisor",
    "selloDigitalTimbreSAT",
    "timbreFiscalDigital",
    "xml"
})
public class WsGenericRespExt {

    protected byte[] acuse;
    protected WsArregloCancelacion arrayFoliosCancelacion;
    protected String cadenaOriginal;
    protected String errorMessage;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaHoraTimbrado;
    protected String folioCodCancelacion;
    protected String folioUUID;
    protected Boolean isError;
    protected int numError;
    protected String selloDigitalEmisor;
    protected String selloDigitalTimbreSAT;
    protected String timbreFiscalDigital;
    @XmlElement(name = "XML")
    protected byte[] xml;

    /**
     * Obtiene el valor de la propiedad acuse.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getAcuse() {
        return acuse;
    }

    /**
     * Define el valor de la propiedad acuse.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setAcuse(byte[] value) {
        this.acuse = value;
    }

    /**
     * Obtiene el valor de la propiedad arrayFoliosCancelacion.
     * 
     * @return
     *     possible object is
     *     {@link WsArregloCancelacion }
     *     
     */
    public WsArregloCancelacion getArrayFoliosCancelacion() {
        return arrayFoliosCancelacion;
    }

    /**
     * Define el valor de la propiedad arrayFoliosCancelacion.
     * 
     * @param value
     *     allowed object is
     *     {@link WsArregloCancelacion }
     *     
     */
    public void setArrayFoliosCancelacion(WsArregloCancelacion value) {
        this.arrayFoliosCancelacion = value;
    }

    /**
     * Obtiene el valor de la propiedad cadenaOriginal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCadenaOriginal() {
        return cadenaOriginal;
    }

    /**
     * Define el valor de la propiedad cadenaOriginal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCadenaOriginal(String value) {
        this.cadenaOriginal = value;
    }

    /**
     * Obtiene el valor de la propiedad errorMessage.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Define el valor de la propiedad errorMessage.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorMessage(String value) {
        this.errorMessage = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaHoraTimbrado.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaHoraTimbrado() {
        return fechaHoraTimbrado;
    }

    /**
     * Define el valor de la propiedad fechaHoraTimbrado.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaHoraTimbrado(XMLGregorianCalendar value) {
        this.fechaHoraTimbrado = value;
    }

    /**
     * Obtiene el valor de la propiedad folioCodCancelacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFolioCodCancelacion() {
        return folioCodCancelacion;
    }

    /**
     * Define el valor de la propiedad folioCodCancelacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFolioCodCancelacion(String value) {
        this.folioCodCancelacion = value;
    }

    /**
     * Obtiene el valor de la propiedad folioUUID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFolioUUID() {
        return folioUUID;
    }

    /**
     * Define el valor de la propiedad folioUUID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFolioUUID(String value) {
        this.folioUUID = value;
    }

    /**
     * Obtiene el valor de la propiedad isError.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsError() {
        return isError;
    }

    /**
     * Define el valor de la propiedad isError.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsError(Boolean value) {
        this.isError = value;
    }

    /**
     * Obtiene el valor de la propiedad numError.
     * 
     */
    public int getNumError() {
        return numError;
    }

    /**
     * Define el valor de la propiedad numError.
     * 
     */
    public void setNumError(int value) {
        this.numError = value;
    }

    /**
     * Obtiene el valor de la propiedad selloDigitalEmisor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelloDigitalEmisor() {
        return selloDigitalEmisor;
    }

    /**
     * Define el valor de la propiedad selloDigitalEmisor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelloDigitalEmisor(String value) {
        this.selloDigitalEmisor = value;
    }

    /**
     * Obtiene el valor de la propiedad selloDigitalTimbreSAT.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelloDigitalTimbreSAT() {
        return selloDigitalTimbreSAT;
    }

    /**
     * Define el valor de la propiedad selloDigitalTimbreSAT.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelloDigitalTimbreSAT(String value) {
        this.selloDigitalTimbreSAT = value;
    }

    /**
     * Obtiene el valor de la propiedad timbreFiscalDigital.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimbreFiscalDigital() {
        return timbreFiscalDigital;
    }

    /**
     * Define el valor de la propiedad timbreFiscalDigital.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimbreFiscalDigital(String value) {
        this.timbreFiscalDigital = value;
    }

    /**
     * Obtiene el valor de la propiedad xml.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getXML() {
        return xml;
    }

    /**
     * Define el valor de la propiedad xml.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setXML(byte[] value) {
        this.xml = value;
    }

}
