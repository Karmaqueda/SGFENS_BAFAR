
package com.tsp.workflow.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para registraComprobanteResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="registraComprobanteResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ws.workflow.tsp.com/}wsResponse">
 *       &lt;sequence>
 *         &lt;element name="archivoAcuseRecepcion" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="descripcionEstatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaHoraRecepcion" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="idEstatus" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="idFactura" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="ultimoComentarioBitacora" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registraComprobanteResponse", propOrder = {
    "archivoAcuseRecepcion",
    "descripcionEstatus",
    "fechaHoraRecepcion",
    "idEstatus",
    "idFactura",
    "ultimoComentarioBitacora"
})
public class RegistraComprobanteResponse
    extends WsResponse
{

    protected byte[] archivoAcuseRecepcion;
    protected String descripcionEstatus;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaHoraRecepcion;
    protected long idEstatus;
    protected long idFactura;
    protected String ultimoComentarioBitacora;

    /**
     * Obtiene el valor de la propiedad archivoAcuseRecepcion.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getArchivoAcuseRecepcion() {
        return archivoAcuseRecepcion;
    }

    /**
     * Define el valor de la propiedad archivoAcuseRecepcion.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setArchivoAcuseRecepcion(byte[] value) {
        this.archivoAcuseRecepcion = value;
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
     * Obtiene el valor de la propiedad fechaHoraRecepcion.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaHoraRecepcion() {
        return fechaHoraRecepcion;
    }

    /**
     * Define el valor de la propiedad fechaHoraRecepcion.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaHoraRecepcion(XMLGregorianCalendar value) {
        this.fechaHoraRecepcion = value;
    }

    /**
     * Obtiene el valor de la propiedad idEstatus.
     * 
     */
    public long getIdEstatus() {
        return idEstatus;
    }

    /**
     * Define el valor de la propiedad idEstatus.
     * 
     */
    public void setIdEstatus(long value) {
        this.idEstatus = value;
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
     * Obtiene el valor de la propiedad ultimoComentarioBitacora.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUltimoComentarioBitacora() {
        return ultimoComentarioBitacora;
    }

    /**
     * Define el valor de la propiedad ultimoComentarioBitacora.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUltimoComentarioBitacora(String value) {
        this.ultimoComentarioBitacora = value;
    }

}
