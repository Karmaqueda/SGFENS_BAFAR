
package com.tsp.interconecta.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para wsArregloCancelacion complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="wsArregloCancelacion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arreglo" type="{http://ws.interconecta.tsp.com/}wsItemCancelacion" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsArregloCancelacion", propOrder = {
    "arreglo"
})
public class WsArregloCancelacion {

    @XmlElement(nillable = true)
    protected List<WsItemCancelacion> arreglo;

    /**
     * Gets the value of the arreglo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the arreglo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getArreglo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WsItemCancelacion }
     * 
     * 
     */
    public List<WsItemCancelacion> getArreglo() {
        if (arreglo == null) {
            arreglo = new ArrayList<WsItemCancelacion>();
        }
        return this.arreglo;
    }

}
