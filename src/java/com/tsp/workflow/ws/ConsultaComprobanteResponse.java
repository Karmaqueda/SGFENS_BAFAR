
package com.tsp.workflow.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para consultaComprobanteResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="consultaComprobanteResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ws.workflow.tsp.com/}wsResponse">
 *       &lt;sequence>
 *         &lt;element name="listaComprobantes" type="{http://ws.workflow.tsp.com/}wsItemComprobante" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultaComprobanteResponse", propOrder = {
    "listaComprobantes"
})
public class ConsultaComprobanteResponse
    extends WsResponse
{

    @XmlElement(nillable = true)
    protected List<WsItemComprobante> listaComprobantes;

    /**
     * Gets the value of the listaComprobantes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaComprobantes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaComprobantes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WsItemComprobante }
     * 
     * 
     */
    public List<WsItemComprobante> getListaComprobantes() {
        if (listaComprobantes == null) {
            listaComprobantes = new ArrayList<WsItemComprobante>();
        }
        return this.listaComprobantes;
    }

}
