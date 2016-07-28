
package com.sap.bafar.ws.creacontrato;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ZstIngresoPerson complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ZstIngresoPerson">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Titular" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="Familiar" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="Conyugue" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZstIngresoPerson", propOrder = {
    "titular",
    "familiar",
    "conyugue"
})
public class ZstIngresoPerson {

    @XmlElement(name = "Titular", required = true)
    protected String titular;
    @XmlElement(name = "Familiar", required = true)
    protected String familiar;
    @XmlElement(name = "Conyugue", required = true)
    protected String conyugue;

    /**
     * Obtiene el valor de la propiedad titular.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitular() {
        return titular;
    }

    /**
     * Define el valor de la propiedad titular.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitular(String value) {
        this.titular = value;
    }

    /**
     * Obtiene el valor de la propiedad familiar.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFamiliar() {
        return familiar;
    }

    /**
     * Define el valor de la propiedad familiar.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFamiliar(String value) {
        this.familiar = value;
    }

    /**
     * Obtiene el valor de la propiedad conyugue.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConyugue() {
        return conyugue;
    }

    /**
     * Define el valor de la propiedad conyugue.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConyugue(String value) {
        this.conyugue = value;
    }

}
