
package com.sap.bafar.ws.creacontrato;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ZstAvalPerson complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ZstAvalPerson">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FechaNac" type="{urn:sap-com:document:sap:rfc:functions}date10"/>
 *         &lt;element name="Rfc" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="ApPat" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="ApMat" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="Nombre1" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="Nobre2" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="Relacion" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="Sexo" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="Identif" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="NoIden" type="{urn:sap-com:document:sap:soap:functions:mc-style}char30"/>
 *         &lt;element name="Celular" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="Calle" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="NoExt" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="NoInt" type="{urn:sap-com:document:sap:rfc:functions}char5"/>
 *         &lt;element name="Colonia" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="Pais" type="{urn:sap-com:document:sap:rfc:functions}char2"/>
 *         &lt;element name="Ciudad" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="Localid" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="Estado" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="Cp" type="{urn:sap-com:document:sap:rfc:functions}char5"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZstAvalPerson", propOrder = {
    "fechaNac",
    "rfc",
    "apPat",
    "apMat",
    "nombre1",
    "nobre2",
    "relacion",
    "sexo",
    "identif",
    "noIden",
    "celular",
    "calle",
    "noExt",
    "noInt",
    "colonia",
    "pais",
    "ciudad",
    "localid",
    "estado",
    "cp"
})
public class ZstAvalPerson {

    @XmlElement(name = "FechaNac", required = true)
    protected String fechaNac;
    @XmlElement(name = "Rfc", required = true)
    protected String rfc;
    @XmlElement(name = "ApPat", required = true)
    protected String apPat;
    @XmlElement(name = "ApMat", required = true)
    protected String apMat;
    @XmlElement(name = "Nombre1", required = true)
    protected String nombre1;
    @XmlElement(name = "Nobre2", required = true)
    protected String nobre2;
    @XmlElement(name = "Relacion", required = true)
    protected String relacion;
    @XmlElement(name = "Sexo", required = true)
    protected String sexo;
    @XmlElement(name = "Identif", required = true)
    protected String identif;
    @XmlElement(name = "NoIden", required = true)
    protected String noIden;
    @XmlElement(name = "Celular", required = true)
    protected String celular;
    @XmlElement(name = "Calle", required = true)
    protected String calle;
    @XmlElement(name = "NoExt", required = true)
    protected String noExt;
    @XmlElement(name = "NoInt", required = true)
    protected String noInt;
    @XmlElement(name = "Colonia", required = true)
    protected String colonia;
    @XmlElement(name = "Pais", required = true)
    protected String pais;
    @XmlElement(name = "Ciudad", required = true)
    protected String ciudad;
    @XmlElement(name = "Localid", required = true)
    protected String localid;
    @XmlElement(name = "Estado", required = true)
    protected String estado;
    @XmlElement(name = "Cp", required = true)
    protected String cp;

    /**
     * Obtiene el valor de la propiedad fechaNac.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaNac() {
        return fechaNac;
    }

    /**
     * Define el valor de la propiedad fechaNac.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaNac(String value) {
        this.fechaNac = value;
    }

    /**
     * Obtiene el valor de la propiedad rfc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfc() {
        return rfc;
    }

    /**
     * Define el valor de la propiedad rfc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfc(String value) {
        this.rfc = value;
    }

    /**
     * Obtiene el valor de la propiedad apPat.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApPat() {
        return apPat;
    }

    /**
     * Define el valor de la propiedad apPat.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApPat(String value) {
        this.apPat = value;
    }

    /**
     * Obtiene el valor de la propiedad apMat.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApMat() {
        return apMat;
    }

    /**
     * Define el valor de la propiedad apMat.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApMat(String value) {
        this.apMat = value;
    }

    /**
     * Obtiene el valor de la propiedad nombre1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre1() {
        return nombre1;
    }

    /**
     * Define el valor de la propiedad nombre1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre1(String value) {
        this.nombre1 = value;
    }

    /**
     * Obtiene el valor de la propiedad nobre2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNobre2() {
        return nobre2;
    }

    /**
     * Define el valor de la propiedad nobre2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNobre2(String value) {
        this.nobre2 = value;
    }

    /**
     * Obtiene el valor de la propiedad relacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelacion() {
        return relacion;
    }

    /**
     * Define el valor de la propiedad relacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelacion(String value) {
        this.relacion = value;
    }

    /**
     * Obtiene el valor de la propiedad sexo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSexo() {
        return sexo;
    }

    /**
     * Define el valor de la propiedad sexo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSexo(String value) {
        this.sexo = value;
    }

    /**
     * Obtiene el valor de la propiedad identif.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentif() {
        return identif;
    }

    /**
     * Define el valor de la propiedad identif.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentif(String value) {
        this.identif = value;
    }

    /**
     * Obtiene el valor de la propiedad noIden.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoIden() {
        return noIden;
    }

    /**
     * Define el valor de la propiedad noIden.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoIden(String value) {
        this.noIden = value;
    }

    /**
     * Obtiene el valor de la propiedad celular.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCelular() {
        return celular;
    }

    /**
     * Define el valor de la propiedad celular.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCelular(String value) {
        this.celular = value;
    }

    /**
     * Obtiene el valor de la propiedad calle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCalle() {
        return calle;
    }

    /**
     * Define el valor de la propiedad calle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCalle(String value) {
        this.calle = value;
    }

    /**
     * Obtiene el valor de la propiedad noExt.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoExt() {
        return noExt;
    }

    /**
     * Define el valor de la propiedad noExt.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoExt(String value) {
        this.noExt = value;
    }

    /**
     * Obtiene el valor de la propiedad noInt.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoInt() {
        return noInt;
    }

    /**
     * Define el valor de la propiedad noInt.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoInt(String value) {
        this.noInt = value;
    }

    /**
     * Obtiene el valor de la propiedad colonia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColonia() {
        return colonia;
    }

    /**
     * Define el valor de la propiedad colonia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColonia(String value) {
        this.colonia = value;
    }

    /**
     * Obtiene el valor de la propiedad pais.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPais() {
        return pais;
    }

    /**
     * Define el valor de la propiedad pais.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPais(String value) {
        this.pais = value;
    }

    /**
     * Obtiene el valor de la propiedad ciudad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCiudad() {
        return ciudad;
    }

    /**
     * Define el valor de la propiedad ciudad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCiudad(String value) {
        this.ciudad = value;
    }

    /**
     * Obtiene el valor de la propiedad localid.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalid() {
        return localid;
    }

    /**
     * Define el valor de la propiedad localid.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalid(String value) {
        this.localid = value;
    }

    /**
     * Obtiene el valor de la propiedad estado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Define el valor de la propiedad estado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstado(String value) {
        this.estado = value;
    }

    /**
     * Obtiene el valor de la propiedad cp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCp() {
        return cp;
    }

    /**
     * Define el valor de la propiedad cp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCp(String value) {
        this.cp = value;
    }

}
