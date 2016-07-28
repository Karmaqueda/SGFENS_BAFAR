
package com.tsp.ws.lco;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for lco complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lco">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="estatusCertificado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estatusCertificadoModified" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="fechaFinal" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaFinalModified" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="fechaInicio" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaInicioModified" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="idContribuyentesObligados" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idContribuyentesObligadosModified" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="noCertificado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="noCertificadoModified" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="rfcContribuyente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rfcContribuyenteModified" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="validezObligaciones" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="validezObligacionesModified" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="validezObligacionesNull" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lco", propOrder = {
    "estatusCertificado",
    "estatusCertificadoModified",
    "fechaFinal",
    "fechaFinalModified",
    "fechaInicio",
    "fechaInicioModified",
    "idContribuyentesObligados",
    "idContribuyentesObligadosModified",
    "noCertificado",
    "noCertificadoModified",
    "rfcContribuyente",
    "rfcContribuyenteModified",
    "validezObligaciones",
    "validezObligacionesModified",
    "validezObligacionesNull"
})
public class Lco {

    protected String estatusCertificado;
    protected boolean estatusCertificadoModified;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaFinal;
    protected boolean fechaFinalModified;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaInicio;
    protected boolean fechaInicioModified;
    protected int idContribuyentesObligados;
    protected boolean idContribuyentesObligadosModified;
    protected String noCertificado;
    protected boolean noCertificadoModified;
    protected String rfcContribuyente;
    protected boolean rfcContribuyenteModified;
    protected int validezObligaciones;
    protected boolean validezObligacionesModified;
    protected boolean validezObligacionesNull;

    /**
     * Gets the value of the estatusCertificado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstatusCertificado() {
        return estatusCertificado;
    }

    /**
     * Sets the value of the estatusCertificado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstatusCertificado(String value) {
        this.estatusCertificado = value;
    }

    /**
     * Gets the value of the estatusCertificadoModified property.
     * 
     */
    public boolean isEstatusCertificadoModified() {
        return estatusCertificadoModified;
    }

    /**
     * Sets the value of the estatusCertificadoModified property.
     * 
     */
    public void setEstatusCertificadoModified(boolean value) {
        this.estatusCertificadoModified = value;
    }

    /**
     * Gets the value of the fechaFinal property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaFinal() {
        return fechaFinal;
    }

    /**
     * Sets the value of the fechaFinal property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaFinal(XMLGregorianCalendar value) {
        this.fechaFinal = value;
    }

    /**
     * Gets the value of the fechaFinalModified property.
     * 
     */
    public boolean isFechaFinalModified() {
        return fechaFinalModified;
    }

    /**
     * Sets the value of the fechaFinalModified property.
     * 
     */
    public void setFechaFinalModified(boolean value) {
        this.fechaFinalModified = value;
    }

    /**
     * Gets the value of the fechaInicio property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Sets the value of the fechaInicio property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaInicio(XMLGregorianCalendar value) {
        this.fechaInicio = value;
    }

    /**
     * Gets the value of the fechaInicioModified property.
     * 
     */
    public boolean isFechaInicioModified() {
        return fechaInicioModified;
    }

    /**
     * Sets the value of the fechaInicioModified property.
     * 
     */
    public void setFechaInicioModified(boolean value) {
        this.fechaInicioModified = value;
    }

    /**
     * Gets the value of the idContribuyentesObligados property.
     * 
     */
    public int getIdContribuyentesObligados() {
        return idContribuyentesObligados;
    }

    /**
     * Sets the value of the idContribuyentesObligados property.
     * 
     */
    public void setIdContribuyentesObligados(int value) {
        this.idContribuyentesObligados = value;
    }

    /**
     * Gets the value of the idContribuyentesObligadosModified property.
     * 
     */
    public boolean isIdContribuyentesObligadosModified() {
        return idContribuyentesObligadosModified;
    }

    /**
     * Sets the value of the idContribuyentesObligadosModified property.
     * 
     */
    public void setIdContribuyentesObligadosModified(boolean value) {
        this.idContribuyentesObligadosModified = value;
    }

    /**
     * Gets the value of the noCertificado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoCertificado() {
        return noCertificado;
    }

    /**
     * Sets the value of the noCertificado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoCertificado(String value) {
        this.noCertificado = value;
    }

    /**
     * Gets the value of the noCertificadoModified property.
     * 
     */
    public boolean isNoCertificadoModified() {
        return noCertificadoModified;
    }

    /**
     * Sets the value of the noCertificadoModified property.
     * 
     */
    public void setNoCertificadoModified(boolean value) {
        this.noCertificadoModified = value;
    }

    /**
     * Gets the value of the rfcContribuyente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfcContribuyente() {
        return rfcContribuyente;
    }

    /**
     * Sets the value of the rfcContribuyente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfcContribuyente(String value) {
        this.rfcContribuyente = value;
    }

    /**
     * Gets the value of the rfcContribuyenteModified property.
     * 
     */
    public boolean isRfcContribuyenteModified() {
        return rfcContribuyenteModified;
    }

    /**
     * Sets the value of the rfcContribuyenteModified property.
     * 
     */
    public void setRfcContribuyenteModified(boolean value) {
        this.rfcContribuyenteModified = value;
    }

    /**
     * Gets the value of the validezObligaciones property.
     * 
     */
    public int getValidezObligaciones() {
        return validezObligaciones;
    }

    /**
     * Sets the value of the validezObligaciones property.
     * 
     */
    public void setValidezObligaciones(int value) {
        this.validezObligaciones = value;
    }

    /**
     * Gets the value of the validezObligacionesModified property.
     * 
     */
    public boolean isValidezObligacionesModified() {
        return validezObligacionesModified;
    }

    /**
     * Sets the value of the validezObligacionesModified property.
     * 
     */
    public void setValidezObligacionesModified(boolean value) {
        this.validezObligacionesModified = value;
    }

    /**
     * Gets the value of the validezObligacionesNull property.
     * 
     */
    public boolean isValidezObligacionesNull() {
        return validezObligacionesNull;
    }

    /**
     * Sets the value of the validezObligacionesNull property.
     * 
     */
    public void setValidezObligacionesNull(boolean value) {
        this.validezObligacionesNull = value;
    }

}
