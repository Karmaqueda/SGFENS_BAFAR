
package com.tsp.workflow.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para wsItemBitacoraComprobante complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="wsItemBitacoraComprobante">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="comentario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcioNivel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionEstatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaHoraAccion" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="nombreAccion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombrePersonaAccion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsItemBitacoraComprobante", propOrder = {
    "comentario",
    "descripcioNivel",
    "descripcionEstatus",
    "fechaHoraAccion",
    "nombreAccion",
    "nombrePersonaAccion"
})
public class WsItemBitacoraComprobante {

    protected String comentario;
    protected String descripcioNivel;
    protected String descripcionEstatus;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaHoraAccion;
    protected String nombreAccion;
    protected String nombrePersonaAccion;

    /**
     * Obtiene el valor de la propiedad comentario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * Define el valor de la propiedad comentario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComentario(String value) {
        this.comentario = value;
    }

    /**
     * Obtiene el valor de la propiedad descripcioNivel.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcioNivel() {
        return descripcioNivel;
    }

    /**
     * Define el valor de la propiedad descripcioNivel.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcioNivel(String value) {
        this.descripcioNivel = value;
    }

    /**
     * Obtiene el valor de la propiedad descripcionEstatus.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionEstatus() {
        return descripcionEstatus;
    }

    /**
     * Define el valor de la propiedad descripcionEstatus.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionEstatus(String value) {
        this.descripcionEstatus = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaHoraAccion.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaHoraAccion() {
        return fechaHoraAccion;
    }

    /**
     * Define el valor de la propiedad fechaHoraAccion.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaHoraAccion(XMLGregorianCalendar value) {
        this.fechaHoraAccion = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreAccion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreAccion() {
        return nombreAccion;
    }

    /**
     * Define el valor de la propiedad nombreAccion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreAccion(String value) {
        this.nombreAccion = value;
    }

    /**
     * Obtiene el valor de la propiedad nombrePersonaAccion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombrePersonaAccion() {
        return nombrePersonaAccion;
    }

    /**
     * Define el valor de la propiedad nombrePersonaAccion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombrePersonaAccion(String value) {
        this.nombrePersonaAccion = value;
    }

}
