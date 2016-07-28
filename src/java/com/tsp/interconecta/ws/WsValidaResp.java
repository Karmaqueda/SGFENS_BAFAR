
package com.tsp.interconecta.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para wsValidaResp complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="wsValidaResp">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cadenaOriginalComprobante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="comprobanteValido" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="errorMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isError" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="listaAdvertencias" type="{http://ws.interconecta.tsp.com/}wsInformaValidacion" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="listaErrores" type="{http://ws.interconecta.tsp.com/}wsInformaValidacion" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="listaInformacion" type="{http://ws.interconecta.tsp.com/}wsInformaValidacion" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="numError" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="selloEmisorValido" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="timbreFiscalDigital" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoComprobante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "wsValidaResp", propOrder = {
    "cadenaOriginalComprobante",
    "comprobanteValido",
    "errorMessage",
    "isError",
    "listaAdvertencias",
    "listaErrores",
    "listaInformacion",
    "numError",
    "selloEmisorValido",
    "timbreFiscalDigital",
    "tipoComprobante",
    "versionComprobante"
})
public class WsValidaResp {

    protected String cadenaOriginalComprobante;
    protected Boolean comprobanteValido;
    protected String errorMessage;
    protected Boolean isError;
    @XmlElement(nillable = true)
    protected List<WsInformaValidacion> listaAdvertencias;
    @XmlElement(nillable = true)
    protected List<WsInformaValidacion> listaErrores;
    @XmlElement(nillable = true)
    protected List<WsInformaValidacion> listaInformacion;
    protected int numError;
    protected Boolean selloEmisorValido;
    protected String timbreFiscalDigital;
    protected String tipoComprobante;
    protected String versionComprobante;

    /**
     * Obtiene el valor de la propiedad cadenaOriginalComprobante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCadenaOriginalComprobante() {
        return cadenaOriginalComprobante;
    }

    /**
     * Define el valor de la propiedad cadenaOriginalComprobante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCadenaOriginalComprobante(String value) {
        this.cadenaOriginalComprobante = value;
    }

    /**
     * Obtiene el valor de la propiedad comprobanteValido.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isComprobanteValido() {
        return comprobanteValido;
    }

    /**
     * Define el valor de la propiedad comprobanteValido.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setComprobanteValido(Boolean value) {
        this.comprobanteValido = value;
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
     * Gets the value of the listaAdvertencias property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaAdvertencias property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaAdvertencias().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WsInformaValidacion }
     * 
     * 
     */
    public List<WsInformaValidacion> getListaAdvertencias() {
        if (listaAdvertencias == null) {
            listaAdvertencias = new ArrayList<WsInformaValidacion>();
        }
        return this.listaAdvertencias;
    }

    /**
     * Gets the value of the listaErrores property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaErrores property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaErrores().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WsInformaValidacion }
     * 
     * 
     */
    public List<WsInformaValidacion> getListaErrores() {
        if (listaErrores == null) {
            listaErrores = new ArrayList<WsInformaValidacion>();
        }
        return this.listaErrores;
    }

    /**
     * Gets the value of the listaInformacion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaInformacion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaInformacion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WsInformaValidacion }
     * 
     * 
     */
    public List<WsInformaValidacion> getListaInformacion() {
        if (listaInformacion == null) {
            listaInformacion = new ArrayList<WsInformaValidacion>();
        }
        return this.listaInformacion;
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
     * Obtiene el valor de la propiedad selloEmisorValido.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSelloEmisorValido() {
        return selloEmisorValido;
    }

    /**
     * Define el valor de la propiedad selloEmisorValido.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSelloEmisorValido(Boolean value) {
        this.selloEmisorValido = value;
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
     * Obtiene el valor de la propiedad tipoComprobante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoComprobante() {
        return tipoComprobante;
    }

    /**
     * Define el valor de la propiedad tipoComprobante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoComprobante(String value) {
        this.tipoComprobante = value;
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
