
package com.tsp.workflow.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para editComprobante complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="editComprobante">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accesoRequest" type="{http://ws.workflow.tsp.com/}accesoRequest" minOccurs="0"/>
 *         &lt;element name="editaComprobanteRequest" type="{http://ws.workflow.tsp.com/}editaComprobanteRequest" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "editComprobante", propOrder = {
    "accesoRequest",
    "editaComprobanteRequest"
})
public class EditComprobante {

    protected AccesoRequest accesoRequest;
    protected EditaComprobanteRequest editaComprobanteRequest;

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
     * Obtiene el valor de la propiedad editaComprobanteRequest.
     * 
     * @return
     *     possible object is
     *     {@link EditaComprobanteRequest }
     *     
     */
    public EditaComprobanteRequest getEditaComprobanteRequest() {
        return editaComprobanteRequest;
    }

    /**
     * Define el valor de la propiedad editaComprobanteRequest.
     * 
     * @param value
     *     allowed object is
     *     {@link EditaComprobanteRequest }
     *     
     */
    public void setEditaComprobanteRequest(EditaComprobanteRequest value) {
        this.editaComprobanteRequest = value;
    }

}
