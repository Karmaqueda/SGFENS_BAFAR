
package com.tsp.workflow.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para getFlujoComprobantes complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="getFlujoComprobantes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accesoRequest" type="{http://ws.workflow.tsp.com/}accesoRequest" minOccurs="0"/>
 *         &lt;element name="consultaComprobanteRequest" type="{http://ws.workflow.tsp.com/}consultaComprobanteRequest" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getFlujoComprobantes", propOrder = {
    "accesoRequest",
    "consultaComprobanteRequest"
})
public class GetFlujoComprobantes {

    protected AccesoRequest accesoRequest;
    protected ConsultaComprobanteRequest consultaComprobanteRequest;

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
     * Obtiene el valor de la propiedad consultaComprobanteRequest.
     * 
     * @return
     *     possible object is
     *     {@link ConsultaComprobanteRequest }
     *     
     */
    public ConsultaComprobanteRequest getConsultaComprobanteRequest() {
        return consultaComprobanteRequest;
    }

    /**
     * Define el valor de la propiedad consultaComprobanteRequest.
     * 
     * @param value
     *     allowed object is
     *     {@link ConsultaComprobanteRequest }
     *     
     */
    public void setConsultaComprobanteRequest(ConsultaComprobanteRequest value) {
        this.consultaComprobanteRequest = value;
    }

}
