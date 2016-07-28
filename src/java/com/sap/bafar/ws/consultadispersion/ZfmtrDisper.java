
package com.sap.bafar.ws.consultadispersion;

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
 *         &lt;element name="ICredi" type="{urn:sap-com:document:sap:rfc:functions}char16" minOccurs="0"/>
 *         &lt;element name="IFecfn" type="{urn:sap-com:document:sap:rfc:functions}date10" minOccurs="0"/>
 *         &lt;element name="IFecin" type="{urn:sap-com:document:sap:rfc:functions}date10" minOccurs="0"/>
 *         &lt;element name="ISocie" type="{urn:sap-com:document:sap:rfc:functions}char4" minOccurs="0"/>
 *         &lt;element name="Messtab" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBdcmsgcoll"/>
 *         &lt;element name="TDisp" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfZcmlOrdPago"/>
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
    "iCredi",
    "iFecfn",
    "iFecin",
    "iSocie",
    "messtab",
    "tDisp"
})
@XmlRootElement(name = "ZfmtrDisper")
public class ZfmtrDisper {

    @XmlElement(name = "ICredi")
    protected String iCredi;
    @XmlElement(name = "IFecfn")
    protected String iFecfn;
    @XmlElement(name = "IFecin")
    protected String iFecin;
    @XmlElement(name = "ISocie")
    protected String iSocie;
    @XmlElement(name = "Messtab", required = true)
    protected TableOfBdcmsgcoll messtab;
    @XmlElement(name = "TDisp", required = true)
    protected TableOfZcmlOrdPago tDisp;

    /**
     * Obtiene el valor de la propiedad iCredi.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getICredi() {
        return iCredi;
    }

    /**
     * Define el valor de la propiedad iCredi.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setICredi(String value) {
        this.iCredi = value;
    }

    /**
     * Obtiene el valor de la propiedad iFecfn.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIFecfn() {
        return iFecfn;
    }

    /**
     * Define el valor de la propiedad iFecfn.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIFecfn(String value) {
        this.iFecfn = value;
    }

    /**
     * Obtiene el valor de la propiedad iFecin.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIFecin() {
        return iFecin;
    }

    /**
     * Define el valor de la propiedad iFecin.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIFecin(String value) {
        this.iFecin = value;
    }

    /**
     * Obtiene el valor de la propiedad iSocie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISocie() {
        return iSocie;
    }

    /**
     * Define el valor de la propiedad iSocie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISocie(String value) {
        this.iSocie = value;
    }

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
     * Obtiene el valor de la propiedad tDisp.
     * 
     * @return
     *     possible object is
     *     {@link TableOfZcmlOrdPago }
     *     
     */
    public TableOfZcmlOrdPago getTDisp() {
        return tDisp;
    }

    /**
     * Define el valor de la propiedad tDisp.
     * 
     * @param value
     *     allowed object is
     *     {@link TableOfZcmlOrdPago }
     *     
     */
    public void setTDisp(TableOfZcmlOrdPago value) {
        this.tDisp = value;
    }

}
