
package com.tsp.workflow.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para wsItemArchivoRequest complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="wsItemArchivoRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="contenidoArchivo" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="nombreArchivo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsItemArchivoRequest", propOrder = {
    "contenidoArchivo",
    "nombreArchivo"
})
public class WsItemArchivoRequest {

    protected byte[] contenidoArchivo;
    protected String nombreArchivo;

    /**
     * Obtiene el valor de la propiedad contenidoArchivo.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getContenidoArchivo() {
        return contenidoArchivo;
    }

    /**
     * Define el valor de la propiedad contenidoArchivo.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setContenidoArchivo(byte[] value) {
        this.contenidoArchivo = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreArchivo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    /**
     * Define el valor de la propiedad nombreArchivo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreArchivo(String value) {
        this.nombreArchivo = value;
    }

}
