
package com.tsp.workflow.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para registraComprobanteRequest complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="registraComprobanteRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="archivoComprobante" type="{http://ws.workflow.tsp.com/}wsItemArchivoRequest" minOccurs="0"/>
 *         &lt;element name="comentarios" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idArea" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="listaAdjuntos" type="{http://ws.workflow.tsp.com/}wsItemArchivoRequest" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ordenCompra" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registraComprobanteRequest", propOrder = {
    "archivoComprobante",
    "comentarios",
    "idArea",
    "listaAdjuntos",
    "ordenCompra"
})
public class RegistraComprobanteRequest {

    protected WsItemArchivoRequest archivoComprobante;
    protected String comentarios;
    protected int idArea;
    @XmlElement(nillable = true)
    protected List<WsItemArchivoRequest> listaAdjuntos;
    protected String ordenCompra;

    /**
     * Obtiene el valor de la propiedad archivoComprobante.
     * 
     * @return
     *     possible object is
     *     {@link WsItemArchivoRequest }
     *     
     */
    public WsItemArchivoRequest getArchivoComprobante() {
        return archivoComprobante;
    }

    /**
     * Define el valor de la propiedad archivoComprobante.
     * 
     * @param value
     *     allowed object is
     *     {@link WsItemArchivoRequest }
     *     
     */
    public void setArchivoComprobante(WsItemArchivoRequest value) {
        this.archivoComprobante = value;
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
     * Obtiene el valor de la propiedad idArea.
     * 
     */
    public int getIdArea() {
        return idArea;
    }

    /**
     * Define el valor de la propiedad idArea.
     * 
     */
    public void setIdArea(int value) {
        this.idArea = value;
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

}
