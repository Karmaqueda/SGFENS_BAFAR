
package com.tsp.workflow.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para consultaComprobanteRequest complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="consultaComprobanteRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fechaRecepcionRangoMax" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaRecepcionRangoMin" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaSelladoRangoMax" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaSelladoRangoMin" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="folio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idClienteReceptor" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idEstatus" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="listaIdFactura" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="listaUUID" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="monto" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="ordenCompra" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="recuperarXml" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="rfcProveedor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="uuid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultaComprobanteRequest", propOrder = {
    "fechaRecepcionRangoMax",
    "fechaRecepcionRangoMin",
    "fechaSelladoRangoMax",
    "fechaSelladoRangoMin",
    "folio",
    "idClienteReceptor",
    "idEstatus",
    "listaIdFactura",
    "listaUUID",
    "monto",
    "ordenCompra",
    "recuperarXml",
    "rfcProveedor",
    "uuid"
})
public class ConsultaComprobanteRequest {

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaRecepcionRangoMax;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaRecepcionRangoMin;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaSelladoRangoMax;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaSelladoRangoMin;
    protected String folio;
    protected int idClienteReceptor;
    protected int idEstatus;
    @XmlElement(nillable = true)
    protected List<Long> listaIdFactura;
    @XmlElement(nillable = true)
    protected List<String> listaUUID;
    protected double monto;
    protected String ordenCompra;
    protected boolean recuperarXml;
    protected String rfcProveedor;
    protected String uuid;

    /**
     * Obtiene el valor de la propiedad fechaRecepcionRangoMax.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaRecepcionRangoMax() {
        return fechaRecepcionRangoMax;
    }

    /**
     * Define el valor de la propiedad fechaRecepcionRangoMax.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaRecepcionRangoMax(XMLGregorianCalendar value) {
        this.fechaRecepcionRangoMax = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaRecepcionRangoMin.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaRecepcionRangoMin() {
        return fechaRecepcionRangoMin;
    }

    /**
     * Define el valor de la propiedad fechaRecepcionRangoMin.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaRecepcionRangoMin(XMLGregorianCalendar value) {
        this.fechaRecepcionRangoMin = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaSelladoRangoMax.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaSelladoRangoMax() {
        return fechaSelladoRangoMax;
    }

    /**
     * Define el valor de la propiedad fechaSelladoRangoMax.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaSelladoRangoMax(XMLGregorianCalendar value) {
        this.fechaSelladoRangoMax = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaSelladoRangoMin.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaSelladoRangoMin() {
        return fechaSelladoRangoMin;
    }

    /**
     * Define el valor de la propiedad fechaSelladoRangoMin.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaSelladoRangoMin(XMLGregorianCalendar value) {
        this.fechaSelladoRangoMin = value;
    }

    /**
     * Obtiene el valor de la propiedad folio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFolio() {
        return folio;
    }

    /**
     * Define el valor de la propiedad folio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFolio(String value) {
        this.folio = value;
    }

    /**
     * Obtiene el valor de la propiedad idClienteReceptor.
     * 
     */
    public int getIdClienteReceptor() {
        return idClienteReceptor;
    }

    /**
     * Define el valor de la propiedad idClienteReceptor.
     * 
     */
    public void setIdClienteReceptor(int value) {
        this.idClienteReceptor = value;
    }

    /**
     * Obtiene el valor de la propiedad idEstatus.
     * 
     */
    public int getIdEstatus() {
        return idEstatus;
    }

    /**
     * Define el valor de la propiedad idEstatus.
     * 
     */
    public void setIdEstatus(int value) {
        this.idEstatus = value;
    }

    /**
     * Gets the value of the listaIdFactura property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaIdFactura property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaIdFactura().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getListaIdFactura() {
        if (listaIdFactura == null) {
            listaIdFactura = new ArrayList<Long>();
        }
        return this.listaIdFactura;
    }

    /**
     * Gets the value of the listaUUID property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaUUID property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaUUID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getListaUUID() {
        if (listaUUID == null) {
            listaUUID = new ArrayList<String>();
        }
        return this.listaUUID;
    }

    /**
     * Obtiene el valor de la propiedad monto.
     * 
     */
    public double getMonto() {
        return monto;
    }

    /**
     * Define el valor de la propiedad monto.
     * 
     */
    public void setMonto(double value) {
        this.monto = value;
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
     * Obtiene el valor de la propiedad recuperarXml.
     * 
     */
    public boolean isRecuperarXml() {
        return recuperarXml;
    }

    /**
     * Define el valor de la propiedad recuperarXml.
     * 
     */
    public void setRecuperarXml(boolean value) {
        this.recuperarXml = value;
    }

    /**
     * Obtiene el valor de la propiedad rfcProveedor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfcProveedor() {
        return rfcProveedor;
    }

    /**
     * Define el valor de la propiedad rfcProveedor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfcProveedor(String value) {
        this.rfcProveedor = value;
    }

    /**
     * Obtiene el valor de la propiedad uuid.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Define el valor de la propiedad uuid.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUuid(String value) {
        this.uuid = value;
    }

}
