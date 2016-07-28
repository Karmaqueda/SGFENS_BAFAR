
package com.sap.bafar.ws.creacontrato;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ZstLiquidezPerson complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ZstLiquidezPerson">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GastoFam" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="IngresoFam" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="GranTot" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="MontoSolic" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="PlazoProp" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="MontoAut" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="PlazoAut" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="CapPago" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZstLiquidezPerson", propOrder = {
    "gastoFam",
    "ingresoFam",
    "granTot",
    "montoSolic",
    "plazoProp",
    "montoAut",
    "plazoAut",
    "capPago"
})
public class ZstLiquidezPerson {

    @XmlElement(name = "GastoFam", required = true)
    protected String gastoFam;
    @XmlElement(name = "IngresoFam", required = true)
    protected String ingresoFam;
    @XmlElement(name = "GranTot", required = true)
    protected String granTot;
    @XmlElement(name = "MontoSolic", required = true)
    protected String montoSolic;
    @XmlElement(name = "PlazoProp", required = true)
    protected String plazoProp;
    @XmlElement(name = "MontoAut", required = true)
    protected String montoAut;
    @XmlElement(name = "PlazoAut", required = true)
    protected String plazoAut;
    @XmlElement(name = "CapPago", required = true)
    protected String capPago;

    /**
     * Obtiene el valor de la propiedad gastoFam.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGastoFam() {
        return gastoFam;
    }

    /**
     * Define el valor de la propiedad gastoFam.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGastoFam(String value) {
        this.gastoFam = value;
    }

    /**
     * Obtiene el valor de la propiedad ingresoFam.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIngresoFam() {
        return ingresoFam;
    }

    /**
     * Define el valor de la propiedad ingresoFam.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIngresoFam(String value) {
        this.ingresoFam = value;
    }

    /**
     * Obtiene el valor de la propiedad granTot.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGranTot() {
        return granTot;
    }

    /**
     * Define el valor de la propiedad granTot.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGranTot(String value) {
        this.granTot = value;
    }

    /**
     * Obtiene el valor de la propiedad montoSolic.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMontoSolic() {
        return montoSolic;
    }

    /**
     * Define el valor de la propiedad montoSolic.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMontoSolic(String value) {
        this.montoSolic = value;
    }

    /**
     * Obtiene el valor de la propiedad plazoProp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlazoProp() {
        return plazoProp;
    }

    /**
     * Define el valor de la propiedad plazoProp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlazoProp(String value) {
        this.plazoProp = value;
    }

    /**
     * Obtiene el valor de la propiedad montoAut.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMontoAut() {
        return montoAut;
    }

    /**
     * Define el valor de la propiedad montoAut.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMontoAut(String value) {
        this.montoAut = value;
    }

    /**
     * Obtiene el valor de la propiedad plazoAut.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlazoAut() {
        return plazoAut;
    }

    /**
     * Define el valor de la propiedad plazoAut.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlazoAut(String value) {
        this.plazoAut = value;
    }

    /**
     * Obtiene el valor de la propiedad capPago.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCapPago() {
        return capPago;
    }

    /**
     * Define el valor de la propiedad capPago.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCapPago(String value) {
        this.capPago = value;
    }

}
