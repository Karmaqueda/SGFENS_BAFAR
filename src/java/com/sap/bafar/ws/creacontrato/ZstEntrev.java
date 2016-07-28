
package com.sap.bafar.ws.creacontrato;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ZstEntrev complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ZstEntrev">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Polit" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="PoliNombre" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="PoliCargo" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="OtraPer" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="OtrpNombre" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="OtrpRelac" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="OtrpJust" type="{urn:sap-com:document:sap:soap:functions:mc-style}char100"/>
 *         &lt;element name="ProfNeg" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="PrngJust" type="{urn:sap-com:document:sap:soap:functions:mc-style}char100"/>
 *         &lt;element name="FuncPubl" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="FamFunc" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="IngAdic" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="MontoAdic" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="Origen" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZstEntrev", propOrder = {
    "polit",
    "poliNombre",
    "poliCargo",
    "otraPer",
    "otrpNombre",
    "otrpRelac",
    "otrpJust",
    "profNeg",
    "prngJust",
    "funcPubl",
    "famFunc",
    "ingAdic",
    "montoAdic",
    "origen"
})
public class ZstEntrev {

    @XmlElement(name = "Polit", required = true)
    protected String polit;
    @XmlElement(name = "PoliNombre", required = true)
    protected String poliNombre;
    @XmlElement(name = "PoliCargo", required = true)
    protected String poliCargo;
    @XmlElement(name = "OtraPer", required = true)
    protected String otraPer;
    @XmlElement(name = "OtrpNombre", required = true)
    protected String otrpNombre;
    @XmlElement(name = "OtrpRelac", required = true)
    protected String otrpRelac;
    @XmlElement(name = "OtrpJust", required = true)
    protected String otrpJust;
    @XmlElement(name = "ProfNeg", required = true)
    protected String profNeg;
    @XmlElement(name = "PrngJust", required = true)
    protected String prngJust;
    @XmlElement(name = "FuncPubl", required = true)
    protected String funcPubl;
    @XmlElement(name = "FamFunc", required = true)
    protected String famFunc;
    @XmlElement(name = "IngAdic", required = true)
    protected String ingAdic;
    @XmlElement(name = "MontoAdic", required = true)
    protected String montoAdic;
    @XmlElement(name = "Origen", required = true)
    protected String origen;

    /**
     * Obtiene el valor de la propiedad polit.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPolit() {
        return polit;
    }

    /**
     * Define el valor de la propiedad polit.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPolit(String value) {
        this.polit = value;
    }

    /**
     * Obtiene el valor de la propiedad poliNombre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoliNombre() {
        return poliNombre;
    }

    /**
     * Define el valor de la propiedad poliNombre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoliNombre(String value) {
        this.poliNombre = value;
    }

    /**
     * Obtiene el valor de la propiedad poliCargo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoliCargo() {
        return poliCargo;
    }

    /**
     * Define el valor de la propiedad poliCargo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoliCargo(String value) {
        this.poliCargo = value;
    }

    /**
     * Obtiene el valor de la propiedad otraPer.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOtraPer() {
        return otraPer;
    }

    /**
     * Define el valor de la propiedad otraPer.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOtraPer(String value) {
        this.otraPer = value;
    }

    /**
     * Obtiene el valor de la propiedad otrpNombre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOtrpNombre() {
        return otrpNombre;
    }

    /**
     * Define el valor de la propiedad otrpNombre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOtrpNombre(String value) {
        this.otrpNombre = value;
    }

    /**
     * Obtiene el valor de la propiedad otrpRelac.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOtrpRelac() {
        return otrpRelac;
    }

    /**
     * Define el valor de la propiedad otrpRelac.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOtrpRelac(String value) {
        this.otrpRelac = value;
    }

    /**
     * Obtiene el valor de la propiedad otrpJust.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOtrpJust() {
        return otrpJust;
    }

    /**
     * Define el valor de la propiedad otrpJust.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOtrpJust(String value) {
        this.otrpJust = value;
    }

    /**
     * Obtiene el valor de la propiedad profNeg.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfNeg() {
        return profNeg;
    }

    /**
     * Define el valor de la propiedad profNeg.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfNeg(String value) {
        this.profNeg = value;
    }

    /**
     * Obtiene el valor de la propiedad prngJust.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrngJust() {
        return prngJust;
    }

    /**
     * Define el valor de la propiedad prngJust.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrngJust(String value) {
        this.prngJust = value;
    }

    /**
     * Obtiene el valor de la propiedad funcPubl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFuncPubl() {
        return funcPubl;
    }

    /**
     * Define el valor de la propiedad funcPubl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFuncPubl(String value) {
        this.funcPubl = value;
    }

    /**
     * Obtiene el valor de la propiedad famFunc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFamFunc() {
        return famFunc;
    }

    /**
     * Define el valor de la propiedad famFunc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFamFunc(String value) {
        this.famFunc = value;
    }

    /**
     * Obtiene el valor de la propiedad ingAdic.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIngAdic() {
        return ingAdic;
    }

    /**
     * Define el valor de la propiedad ingAdic.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIngAdic(String value) {
        this.ingAdic = value;
    }

    /**
     * Obtiene el valor de la propiedad montoAdic.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMontoAdic() {
        return montoAdic;
    }

    /**
     * Define el valor de la propiedad montoAdic.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMontoAdic(String value) {
        this.montoAdic = value;
    }

    /**
     * Obtiene el valor de la propiedad origen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * Define el valor de la propiedad origen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigen(String value) {
        this.origen = value;
    }

}
