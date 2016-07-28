
package com.tsp.interconecta.ws.dotnet;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfFolioCancelacion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfFolioCancelacion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FolioCancelacion" type="{http://net.ws.facturacionelectronica.tspsoftware.com/}FolioCancelacion" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfFolioCancelacion", propOrder = {
    "folioCancelacion"
})
public class ArrayOfFolioCancelacion {

    @XmlElement(name = "FolioCancelacion", nillable = true)
    protected List<FolioCancelacion> folioCancelacion;

    /**
     * Gets the value of the folioCancelacion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the folioCancelacion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFolioCancelacion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FolioCancelacion }
     * 
     * 
     */
    public List<FolioCancelacion> getFolioCancelacion() {
        if (folioCancelacion == null) {
            folioCancelacion = new ArrayList<FolioCancelacion>();
        }
        return this.folioCancelacion;
    }

}
