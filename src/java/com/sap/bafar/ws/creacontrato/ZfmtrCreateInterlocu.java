
package com.sap.bafar.ws.creacontrato;

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
 *         &lt;element name="IAval" type="{urn:sap-com:document:sap:soap:functions:mc-style}ZstAvalPerson"/>
 *         &lt;element name="IContract" type="{urn:sap-com:document:sap:soap:functions:mc-style}ZstContract"/>
 *         &lt;element name="ICony" type="{urn:sap-com:document:sap:soap:functions:mc-style}ZstConyPerson"/>
 *         &lt;element name="IDatos" type="{urn:sap-com:document:sap:soap:functions:mc-style}ZstDatosPerson"/>
 *         &lt;element name="IDirec" type="{urn:sap-com:document:sap:soap:functions:mc-style}ZstDirecPerson"/>
 *         &lt;element name="IEntrev" type="{urn:sap-com:document:sap:soap:functions:mc-style}ZstEntrev"/>
 *         &lt;element name="IGasto" type="{urn:sap-com:document:sap:soap:functions:mc-style}ZstGastoPerson"/>
 *         &lt;element name="IIngreso" type="{urn:sap-com:document:sap:soap:functions:mc-style}ZstIngresoPerson"/>
 *         &lt;element name="ILiquidez" type="{urn:sap-com:document:sap:soap:functions:mc-style}ZstLiquidezPerson"/>
 *         &lt;element name="IPld" type="{urn:sap-com:document:sap:soap:functions:mc-style}ZstPldPerson"/>
 *         &lt;element name="Messtab" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBdcmsgcoll"/>
 *         &lt;element name="TAmort" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfZlmTablaAmort"/>
 *         &lt;element name="TContac" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfZstContPerson"/>
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
    "iAval",
    "iContract",
    "iCony",
    "iDatos",
    "iDirec",
    "iEntrev",
    "iGasto",
    "iIngreso",
    "iLiquidez",
    "iPld",
    "messtab",
    "tAmort",
    "tContac"
})
@XmlRootElement(name = "ZfmtrCreateInterlocu")
public class ZfmtrCreateInterlocu {

    @XmlElement(name = "IAval", required = true)
    protected ZstAvalPerson iAval;
    @XmlElement(name = "IContract", required = true)
    protected ZstContract iContract;
    @XmlElement(name = "ICony", required = true)
    protected ZstConyPerson iCony;
    @XmlElement(name = "IDatos", required = true)
    protected ZstDatosPerson iDatos;
    @XmlElement(name = "IDirec", required = true)
    protected ZstDirecPerson iDirec;
    @XmlElement(name = "IEntrev", required = true)
    protected ZstEntrev iEntrev;
    @XmlElement(name = "IGasto", required = true)
    protected ZstGastoPerson iGasto;
    @XmlElement(name = "IIngreso", required = true)
    protected ZstIngresoPerson iIngreso;
    @XmlElement(name = "ILiquidez", required = true)
    protected ZstLiquidezPerson iLiquidez;
    @XmlElement(name = "IPld", required = true)
    protected ZstPldPerson iPld;
    @XmlElement(name = "Messtab", required = true)
    protected TableOfBdcmsgcoll messtab;
    @XmlElement(name = "TAmort", required = true)
    protected TableOfZlmTablaAmort tAmort;
    @XmlElement(name = "TContac", required = true)
    protected TableOfZstContPerson tContac;

    /**
     * Obtiene el valor de la propiedad iAval.
     * 
     * @return
     *     possible object is
     *     {@link ZstAvalPerson }
     *     
     */
    public ZstAvalPerson getIAval() {
        return iAval;
    }

    /**
     * Define el valor de la propiedad iAval.
     * 
     * @param value
     *     allowed object is
     *     {@link ZstAvalPerson }
     *     
     */
    public void setIAval(ZstAvalPerson value) {
        this.iAval = value;
    }

    /**
     * Obtiene el valor de la propiedad iContract.
     * 
     * @return
     *     possible object is
     *     {@link ZstContract }
     *     
     */
    public ZstContract getIContract() {
        return iContract;
    }

    /**
     * Define el valor de la propiedad iContract.
     * 
     * @param value
     *     allowed object is
     *     {@link ZstContract }
     *     
     */
    public void setIContract(ZstContract value) {
        this.iContract = value;
    }

    /**
     * Obtiene el valor de la propiedad iCony.
     * 
     * @return
     *     possible object is
     *     {@link ZstConyPerson }
     *     
     */
    public ZstConyPerson getICony() {
        return iCony;
    }

    /**
     * Define el valor de la propiedad iCony.
     * 
     * @param value
     *     allowed object is
     *     {@link ZstConyPerson }
     *     
     */
    public void setICony(ZstConyPerson value) {
        this.iCony = value;
    }

    /**
     * Obtiene el valor de la propiedad iDatos.
     * 
     * @return
     *     possible object is
     *     {@link ZstDatosPerson }
     *     
     */
    public ZstDatosPerson getIDatos() {
        return iDatos;
    }

    /**
     * Define el valor de la propiedad iDatos.
     * 
     * @param value
     *     allowed object is
     *     {@link ZstDatosPerson }
     *     
     */
    public void setIDatos(ZstDatosPerson value) {
        this.iDatos = value;
    }

    /**
     * Obtiene el valor de la propiedad iDirec.
     * 
     * @return
     *     possible object is
     *     {@link ZstDirecPerson }
     *     
     */
    public ZstDirecPerson getIDirec() {
        return iDirec;
    }

    /**
     * Define el valor de la propiedad iDirec.
     * 
     * @param value
     *     allowed object is
     *     {@link ZstDirecPerson }
     *     
     */
    public void setIDirec(ZstDirecPerson value) {
        this.iDirec = value;
    }

    /**
     * Obtiene el valor de la propiedad iEntrev.
     * 
     * @return
     *     possible object is
     *     {@link ZstEntrev }
     *     
     */
    public ZstEntrev getIEntrev() {
        return iEntrev;
    }

    /**
     * Define el valor de la propiedad iEntrev.
     * 
     * @param value
     *     allowed object is
     *     {@link ZstEntrev }
     *     
     */
    public void setIEntrev(ZstEntrev value) {
        this.iEntrev = value;
    }

    /**
     * Obtiene el valor de la propiedad iGasto.
     * 
     * @return
     *     possible object is
     *     {@link ZstGastoPerson }
     *     
     */
    public ZstGastoPerson getIGasto() {
        return iGasto;
    }

    /**
     * Define el valor de la propiedad iGasto.
     * 
     * @param value
     *     allowed object is
     *     {@link ZstGastoPerson }
     *     
     */
    public void setIGasto(ZstGastoPerson value) {
        this.iGasto = value;
    }

    /**
     * Obtiene el valor de la propiedad iIngreso.
     * 
     * @return
     *     possible object is
     *     {@link ZstIngresoPerson }
     *     
     */
    public ZstIngresoPerson getIIngreso() {
        return iIngreso;
    }

    /**
     * Define el valor de la propiedad iIngreso.
     * 
     * @param value
     *     allowed object is
     *     {@link ZstIngresoPerson }
     *     
     */
    public void setIIngreso(ZstIngresoPerson value) {
        this.iIngreso = value;
    }

    /**
     * Obtiene el valor de la propiedad iLiquidez.
     * 
     * @return
     *     possible object is
     *     {@link ZstLiquidezPerson }
     *     
     */
    public ZstLiquidezPerson getILiquidez() {
        return iLiquidez;
    }

    /**
     * Define el valor de la propiedad iLiquidez.
     * 
     * @param value
     *     allowed object is
     *     {@link ZstLiquidezPerson }
     *     
     */
    public void setILiquidez(ZstLiquidezPerson value) {
        this.iLiquidez = value;
    }

    /**
     * Obtiene el valor de la propiedad iPld.
     * 
     * @return
     *     possible object is
     *     {@link ZstPldPerson }
     *     
     */
    public ZstPldPerson getIPld() {
        return iPld;
    }

    /**
     * Define el valor de la propiedad iPld.
     * 
     * @param value
     *     allowed object is
     *     {@link ZstPldPerson }
     *     
     */
    public void setIPld(ZstPldPerson value) {
        this.iPld = value;
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
     * Obtiene el valor de la propiedad tAmort.
     * 
     * @return
     *     possible object is
     *     {@link TableOfZlmTablaAmort }
     *     
     */
    public TableOfZlmTablaAmort getTAmort() {
        return tAmort;
    }

    /**
     * Define el valor de la propiedad tAmort.
     * 
     * @param value
     *     allowed object is
     *     {@link TableOfZlmTablaAmort }
     *     
     */
    public void setTAmort(TableOfZlmTablaAmort value) {
        this.tAmort = value;
    }

    /**
     * Obtiene el valor de la propiedad tContac.
     * 
     * @return
     *     possible object is
     *     {@link TableOfZstContPerson }
     *     
     */
    public TableOfZstContPerson getTContac() {
        return tContac;
    }

    /**
     * Define el valor de la propiedad tContac.
     * 
     * @param value
     *     allowed object is
     *     {@link TableOfZstContPerson }
     *     
     */
    public void setTContac(TableOfZstContPerson value) {
        this.tContac = value;
    }

}
