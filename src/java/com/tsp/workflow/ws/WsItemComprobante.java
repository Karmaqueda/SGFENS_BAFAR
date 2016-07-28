
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
 * <p>Clase Java para wsItemComprobante complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="wsItemComprobante">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="archivoXml" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="codigoProveedorXArea" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionEstatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaHoraRecepcion" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaHoraSellado" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="folio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idEstatus" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idFactura" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="listaBitacora" type="{http://ws.workflow.tsp.com/}wsItemBitacoraComprobante" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="moneda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreAreaEntrega" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ordenCompra" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="razonSocialProveedor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rfcProveedor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subtotal" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="total" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="uuid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="versionCfd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsItemComprobante", propOrder = {
    "archivoXml",
    "codigoProveedorXArea",
    "descripcionEstatus",
    "fechaHoraRecepcion",
    "fechaHoraSellado",
    "folio",
    "idEstatus",
    "idFactura",
    "listaBitacora",
    "moneda",
    "nombreAreaEntrega",
    "ordenCompra",
    "razonSocialProveedor",
    "rfcProveedor",
    "serie",
    "subtotal",
    "total",
    "uuid",
    "versionCfd"
})
public class WsItemComprobante {

    protected byte[] archivoXml;
    protected String codigoProveedorXArea;
    protected String descripcionEstatus;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaHoraRecepcion;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaHoraSellado;
    protected String folio;
    protected int idEstatus;
    protected long idFactura;
    @XmlElement(nillable = true)
    protected List<WsItemBitacoraComprobante> listaBitacora;
    protected String moneda;
    protected String nombreAreaEntrega;
    protected String ordenCompra;
    protected String razonSocialProveedor;
    protected String rfcProveedor;
    protected String serie;
    protected double subtotal;
    protected double total;
    protected String uuid;
    protected String versionCfd;

    /**
     * Obtiene el valor de la propiedad archivoXml.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getArchivoXml() {
        return archivoXml;
    }

    /**
     * Define el valor de la propiedad archivoXml.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setArchivoXml(byte[] value) {
        this.archivoXml = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoProveedorXArea.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoProveedorXArea() {
        return codigoProveedorXArea;
    }

    /**
     * Define el valor de la propiedad codigoProveedorXArea.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoProveedorXArea(String value) {
        this.codigoProveedorXArea = value;
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
     * Obtiene el valor de la propiedad fechaHoraSellado.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaHoraSellado() {
        return fechaHoraSellado;
    }

    /**
     * Define el valor de la propiedad fechaHoraSellado.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaHoraSellado(XMLGregorianCalendar value) {
        this.fechaHoraSellado = value;
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
     * Gets the value of the listaBitacora property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaBitacora property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaBitacora().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WsItemBitacoraComprobante }
     * 
     * 
     */
    public List<WsItemBitacoraComprobante> getListaBitacora() {
        if (listaBitacora == null) {
            listaBitacora = new ArrayList<WsItemBitacoraComprobante>();
        }
        return this.listaBitacora;
    }

    /**
     * Obtiene el valor de la propiedad moneda.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoneda() {
        return moneda;
    }

    /**
     * Define el valor de la propiedad moneda.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoneda(String value) {
        this.moneda = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreAreaEntrega.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreAreaEntrega() {
        return nombreAreaEntrega;
    }

    /**
     * Define el valor de la propiedad nombreAreaEntrega.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreAreaEntrega(String value) {
        this.nombreAreaEntrega = value;
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
     * Obtiene el valor de la propiedad razonSocialProveedor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRazonSocialProveedor() {
        return razonSocialProveedor;
    }

    /**
     * Define el valor de la propiedad razonSocialProveedor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRazonSocialProveedor(String value) {
        this.razonSocialProveedor = value;
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
     * Obtiene el valor de la propiedad serie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSerie() {
        return serie;
    }

    /**
     * Define el valor de la propiedad serie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSerie(String value) {
        this.serie = value;
    }

    /**
     * Obtiene el valor de la propiedad subtotal.
     * 
     */
    public double getSubtotal() {
        return subtotal;
    }

    /**
     * Define el valor de la propiedad subtotal.
     * 
     */
    public void setSubtotal(double value) {
        this.subtotal = value;
    }

    /**
     * Obtiene el valor de la propiedad total.
     * 
     */
    public double getTotal() {
        return total;
    }

    /**
     * Define el valor de la propiedad total.
     * 
     */
    public void setTotal(double value) {
        this.total = value;
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

    /**
     * Obtiene el valor de la propiedad versionCfd.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionCfd() {
        return versionCfd;
    }

    /**
     * Define el valor de la propiedad versionCfd.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionCfd(String value) {
        this.versionCfd = value;
    }

}
