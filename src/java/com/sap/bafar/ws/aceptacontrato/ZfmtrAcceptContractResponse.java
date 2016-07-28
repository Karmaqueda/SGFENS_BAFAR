
package com.sap.bafar.ws.aceptacontrato;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Messtab" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBdcmsgcoll" minOccurs="0"/>
 *         &lt;element name="Subrc" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "messtab",
    "subrc"
})
@XmlRootElement(name = "ZfmtrAcceptContractResponse")
public class ZfmtrAcceptContractResponse {

    @XmlElement(name = "Messtab")
    protected TableOfBdcmsgcoll messtab;
    @XmlElement(name = "Subrc")
    protected int subrc;

    /**
     * Obtiene el valor de la propiedad messtab.
     * 
     * @return
     *     possible object is
     *     {@link TableOfBdcmsgcoll }
     *     
     */
    public TableOfBdcmsgcoll getMesstab() {
        return messtab;
    }

    /**
     * Define el valor de la propiedad messtab.
     * 
     * @param value
     *     allowed object is
     *     {@link TableOfBdcmsgcoll }
     *     
     */
    public void setMesstab(TableOfBdcmsgcoll value) {
        this.messtab = value;
    }

    /**
     * Obtiene el valor de la propiedad subrc.
     * 
     */
    public int getSubrc() {
        return subrc;
    }

    /**
     * Define el valor de la propiedad subrc.
     * 
     */
    public void setSubrc(int value) {
        this.subrc = value;
    }

}
