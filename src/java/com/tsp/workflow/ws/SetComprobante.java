
package com.tsp.workflow.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para setComprobante complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="setComprobante">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accesoRequest" type="{http://ws.workflow.tsp.com/}accesoRequest" minOccurs="0"/>
 *         &lt;element name="registraComprobanteRequest" type="{http://ws.workflow.tsp.com/}registraComprobanteRequest" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setComprobante", propOrder = {
    "accesoRequest",
    "registraComprobanteRequest"
})
public class SetComprobante {

    protected AccesoRequest accesoRequest;
    protected RegistraComprobanteRequest registraComprobanteRequest;

    /**
     * Obtiene el valor de la propiedad accesoRequest.
     * 
     * @return
     *     possible object is
     *     {@link AccesoRequest }
     *     
     */
    public AccesoRequest getAccesoRequest() {
        return accesoRequest;
    }

    /**
     * Define el valor de la propiedad accesoRequest.
     * 
     * @param value
     *     allowed object is
     *     {@link AccesoRequest }
     *     
     */
    public void setAccesoRequest(AccesoRequest value) {
        this.accesoRequest = value;
    }

    /**
     * Obtiene el valor de la propiedad registraComprobanteRequest.
     * 
     * @return
     *     possible object is
     *     {@link RegistraComprobanteRequest }
     *     
     */
    public RegistraComprobanteRequest getRegistraComprobanteRequest() {
        return registraComprobanteRequest;
    }

    /**
     * Define el valor de la propiedad registraComprobanteRequest.
     * 
     * @param value
     *     allowed object is
     *     {@link RegistraComprobanteRequest }
     *     
     */
    public void setRegistraComprobanteRequest(RegistraComprobanteRequest value) {
        this.registraComprobanteRequest = value;
    }

}
