
package com.tsp.workflow.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para editaComprobanteRequest complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="editaComprobanteRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cancelarComprobante" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="comentarios" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idFactura" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="listaAdjuntos" type="{http://ws.workflow.tsp.com/}wsItemArchivoRequest" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ordenCompra" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="uuidFactura" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "editaComprobanteRequest", propOrder = {
    "cancelarComprobante",
    "comentarios",
    "idFactura",
    "listaAdjuntos",
    "ordenCompra",
    "uuidFactura"
})
public class EditaComprobanteRequest {

    protected boolean cancelarComprobante;
    protected String comentarios;
    protected long idFactura;
    @XmlElement(nillable = true)
    protected List<WsItemArchivoRequest> listaAdjuntos;
    protected String ordenCompra;
    protected String uuidFactura;

    /**
     * Obtiene el valor de la propiedad cancelarComprobante.
     * 
     */
    public boolean isCancelarComprobante() {
        return cancelarComprobante;
    }

    /**
     * Define el valor de la propiedad cancelarComprobante.
     * 
     */
    public void setCancelarComprobante(boolean value) {
        this.cancelarComprobante = value;
    }

    /**
     * Obtiene el valor de la propiedad comentarios.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComentarios() {
        return comentarios;
    }

    /**
     * Define el valor de la propiedad comentarios.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComentarios(String value) {
        this.comentarios = value;
    }

    /**
     * Obtiene el valor de la propiedad idFactura.
     * 
     */
    public long getIdFactura() {
        return idFactura;
    }

    /**
     * Define el valor de la propiedad idFactura.
     * 
     */
    public void setIdFactura(long value) {
        this.idFactura = value;
    }

    /**
     * Gets the value of the listaAdjuntos property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaAdjuntos property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaAdjuntos().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WsItemArchivoRequest }
     * 
     * 
     */
    public List<WsItemArchivoRequest> getListaAdjuntos() {
        if (listaAdjuntos == null) {
            listaAdjuntos = new ArrayList<WsItemArchivoRequest>();
        }
        return this.listaAdjuntos;
    }

    /**
     * Obtiene el valor de la propiedad ordenCompra.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrdenCompra() {
        return ordenCompra;
    }

    /**
     * Define el valor de la propiedad ordenCompra.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrdenCompra(String value) {
        this.ordenCompra = value;
    }

    /**
     * Obtiene el valor de la propiedad uuidFactura.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUuidFactura() {
        return uuidFactura;
    }

    /**
     * Define el valor de la propiedad uuidFactura.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUuidFactura(String value) {
        this.uuidFactura = value;
    }

}
