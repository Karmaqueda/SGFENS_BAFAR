
package com.tsp.workflow.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para wsItemCliente complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="wsItemCliente">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="razonSocial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="relacionCodigoProvXArea" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="requerirFolioProveedores" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="requerirMonedaProveedores" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="rfc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsItemCliente", propOrder = {
    "id",
    "razonSocial",
    "relacionCodigoProvXArea",
    "requerirFolioProveedores",
    "requerirMonedaProveedores",
    "rfc"
})
public class WsItemCliente {

    protected int id;
    protected String razonSocial;
    protected boolean relacionCodigoProvXArea;
    protected boolean requerirFolioProveedores;
    protected boolean requerirMonedaProveedores;
    protected String rfc;

    /**
     * Obtiene el valor de la propiedad id.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Define el valor de la propiedad id.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Obtiene el valor de la propiedad razonSocial.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRazonSocial() {
        return razonSocial;
    }

    /**
     * Define el valor de la propiedad razonSocial.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRazonSocial(String value) {
        this.razonSocial = value;
    }

    /**
     * Obtiene el valor de la propiedad relacionCodigoProvXArea.
     * 
     */
    public boolean isRelacionCodigoProvXArea() {
        return relacionCodigoProvXArea;
    }

    /**
     * Define el valor de la propiedad relacionCodigoProvXArea.
     * 
     */
    public void setRelacionCodigoProvXArea(boolean value) {
        this.relacionCodigoProvXArea = value;
    }

    /**
     * Obtiene el valor de la propiedad requerirFolioProveedores.
     * 
     */
    public boolean isRequerirFolioProveedores() {
        return requerirFolioProveedores;
    }

    /**
     * Define el valor de la propiedad requerirFolioProveedores.
     * 
     */
    public void setRequerirFolioProveedores(boolean value) {
        this.requerirFolioProveedores = value;
    }

    /**
     * Obtiene el valor de la propiedad requerirMonedaProveedores.
     * 
     */
    public boolean isRequerirMonedaProveedores() {
        return requerirMonedaProveedores;
    }

    /**
     * Define el valor de la propiedad requerirMonedaProveedores.
     * 
     */
    public void setRequerirMonedaProveedores(boolean value) {
        this.requerirMonedaProveedores = value;
    }

    /**
     * Obtiene el valor de la propiedad rfc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfc() {
        return rfc;
    }

    /**
     * Define el valor de la propiedad rfc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfc(String value) {
        this.rfc = value;
    }

}
