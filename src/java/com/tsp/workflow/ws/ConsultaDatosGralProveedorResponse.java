
package com.tsp.workflow.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para consultaDatosGralProveedorResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="consultaDatosGralProveedorResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ws.workflow.tsp.com/}wsResponse">
 *       &lt;sequence>
 *         &lt;element name="listaWsItemArea" type="{http://ws.workflow.tsp.com/}wsItemArea" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="wsItemCliente" type="{http://ws.workflow.tsp.com/}wsItemCliente" minOccurs="0"/>
 *         &lt;element name="wsItemProveedor" type="{http://ws.workflow.tsp.com/}wsItemProveedor" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultaDatosGralProveedorResponse", propOrder = {
    "listaWsItemArea",
    "wsItemCliente",
    "wsItemProveedor"
})
public class ConsultaDatosGralProveedorResponse
    extends WsResponse
{

    @XmlElement(nillable = true)
    protected List<WsItemArea> listaWsItemArea;
    protected WsItemCliente wsItemCliente;
    protected WsItemProveedor wsItemProveedor;

    /**
     * Gets the value of the listaWsItemArea property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaWsItemArea property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaWsItemArea().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WsItemArea }
     * 
     * 
     */
    public List<WsItemArea> getListaWsItemArea() {
        if (listaWsItemArea == null) {
            listaWsItemArea = new ArrayList<WsItemArea>();
        }
        return this.listaWsItemArea;
    }

    /**
     * Obtiene el valor de la propiedad wsItemCliente.
     * 
     * @return
     *     possible object is
     *     {@link WsItemCliente }
     *     
     */
    public WsItemCliente getWsItemCliente() {
        return wsItemCliente;
    }

    /**
     * Define el valor de la propiedad wsItemCliente.
     * 
     * @param value
     *     allowed object is
     *     {@link WsItemCliente }
     *     
     */
    public void setWsItemCliente(WsItemCliente value) {
        this.wsItemCliente = value;
    }

    /**
     * Obtiene el valor de la propiedad wsItemProveedor.
     * 
     * @return
     *     possible object is
     *     {@link WsItemProveedor }
     *     
     */
    public WsItemProveedor getWsItemProveedor() {
        return wsItemProveedor;
    }

    /**
     * Define el valor de la propiedad wsItemProveedor.
     * 
     * @param value
     *     allowed object is
     *     {@link WsItemProveedor }
     *     
     */
    public void setWsItemProveedor(WsItemProveedor value) {
        this.wsItemProveedor = value;
    }

}
