
package com.sap.bafar.ws.creacontrato;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ZstPldPerson complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ZstPldPerson">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MaxDiar" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="MaxMens" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="PagMax" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="Riesgo" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="EntId" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="Monedas" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="Vias" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="Bancos" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="OrigenRec" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="DestCred" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="DestOtro" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZstPldPerson", propOrder = {
    "maxDiar",
    "maxMens",
    "pagMax",
    "riesgo",
    "entId",
    "monedas",
    "vias",
    "bancos",
    "origenRec",
    "destCred",
    "destOtro"
})
public class ZstPldPerson {

    @XmlElement(name = "MaxDiar", required = true)
    protected String maxDiar;
    @XmlElement(name = "MaxMens", required = true)
    protected String maxMens;
    @XmlElement(name = "PagMax", required = true)
    protected String pagMax;
    @XmlElement(name = "Riesgo", required = true)
    protected String riesgo;
    @XmlElement(name = "EntId", required = true)
    protected String entId;
    @XmlElement(name = "Monedas", required = true)
    protected String monedas;
    @XmlElement(name = "Vias", required = true)
    protected String vias;
    @XmlElement(name = "Bancos", required = true)
    protected String bancos;
    @XmlElement(name = "OrigenRec", required = true)
    protected String origenRec;
    @XmlElement(name = "DestCred", required = true)
    protected String destCred;
    @XmlElement(name = "DestOtro", required = true)
    protected String destOtro;

    /**
     * Obtiene el valor de la propiedad maxDiar.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxDiar() {
        return maxDiar;
    }

    /**
     * Define el valor de la propiedad maxDiar.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxDiar(String value) {
        this.maxDiar = value;
    }

    /**
     * Obtiene el valor de la propiedad maxMens.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxMens() {
        return maxMens;
    }

    /**
     * Define el valor de la propiedad maxMens.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxMens(String value) {
        this.maxMens = value;
    }

    /**
     * Obtiene el valor de la propiedad pagMax.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPagMax() {
        return pagMax;
    }

    /**
     * Define el valor de la propiedad pagMax.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPagMax(String value) {
        this.pagMax = value;
    }

    /**
     * Obtiene el valor de la propiedad riesgo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRiesgo() {
        return riesgo;
    }

    /**
     * Define el valor de la propiedad riesgo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRiesgo(String value) {
        this.riesgo = value;
    }

    /**
     * Obtiene el valor de la propiedad entId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntId() {
        return entId;
    }

    /**
     * Define el valor de la propiedad entId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntId(String value) {
        this.entId = value;
    }

    /**
     * Obtiene el valor de la propiedad monedas.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMonedas() {
        return monedas;
    }

    /**
     * Define el valor de la propiedad monedas.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMonedas(String value) {
        this.monedas = value;
    }

    /**
     * Obtiene el valor de la propiedad vias.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVias() {
        return vias;
    }

    /**
     * Define el valor de la propiedad vias.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVias(String value) {
        this.vias = value;
    }

    /**
     * Obtiene el valor de la propiedad bancos.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBancos() {
        return bancos;
    }

    /**
     * Define el valor de la propiedad bancos.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBancos(String value) {
        this.bancos = value;
    }

    /**
     * Obtiene el valor de la propiedad origenRec.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigenRec() {
        return origenRec;
    }

    /**
     * Define el valor de la propiedad origenRec.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigenRec(String value) {
        this.origenRec = value;
    }

    /**
     * Obtiene el valor de la propiedad destCred.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestCred() {
        return destCred;
    }

    /**
     * Define el valor de la propiedad destCred.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestCred(String value) {
        this.destCred = value;
    }

    /**
     * Obtiene el valor de la propiedad destOtro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestOtro() {
        return destOtro;
    }

    /**
     * Define el valor de la propiedad destOtro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestOtro(String value) {
        this.destOtro = value;
    }

}
