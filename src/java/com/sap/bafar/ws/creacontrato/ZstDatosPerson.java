
package com.sap.bafar.ws.creacontrato;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ZstDatosPerson complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ZstDatosPerson">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FechaNac" type="{urn:sap-com:document:sap:rfc:functions}date10"/>
 *         &lt;element name="Depend" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="EdadDepen" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="RedSocial" type="{urn:sap-com:document:sap:soap:functions:mc-style}char20"/>
 *         &lt;element name="TipoViv" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="ValViv" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="Renta" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="TiempRes" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="Empresa" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="TipoEmpr" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="RelEmp" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="Contr" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="Sueldo" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="FrecPag" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="FechIngr" type="{urn:sap-com:document:sap:rfc:functions}date10"/>
 *         &lt;element name="Antig" type="{urn:sap-com:document:sap:rfc:functions}char2"/>
 *         &lt;element name="FormPag" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="Puesto" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="TelTrab" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="Ext" type="{urn:sap-com:document:sap:rfc:functions}char5"/>
 *         &lt;element name="TelAlt" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="MailTrab" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="CalleTrab" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="NoExt" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="NoInt" type="{urn:sap-com:document:sap:rfc:functions}char5"/>
 *         &lt;element name="Colonia" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="Cp" type="{urn:sap-com:document:sap:rfc:functions}char5"/>
 *         &lt;element name="Ciudad" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="Estado" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="NombreJefe" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="PuestoJefe" type="{urn:sap-com:document:sap:soap:functions:mc-style}char30"/>
 *         &lt;element name="TelOfic" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="Curp" type="{urn:sap-com:document:sap:soap:functions:mc-style}char20"/>
 *         &lt;element name="Rfc" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="Sexo" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="Ecivil" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="Nacion" type="{urn:sap-com:document:sap:rfc:functions}char2"/>
 *         &lt;element name="Prof" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="Lugnac" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="Pais" type="{urn:sap-com:document:sap:rfc:functions}char2"/>
 *         &lt;element name="Persf" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZstDatosPerson", propOrder = {
    "fechaNac",
    "depend",
    "edadDepen",
    "redSocial",
    "tipoViv",
    "valViv",
    "renta",
    "tiempRes",
    "empresa",
    "tipoEmpr",
    "relEmp",
    "contr",
    "sueldo",
    "frecPag",
    "fechIngr",
    "antig",
    "formPag",
    "puesto",
    "telTrab",
    "ext",
    "telAlt",
    "mailTrab",
    "calleTrab",
    "noExt",
    "noInt",
    "colonia",
    "cp",
    "ciudad",
    "estado",
    "nombreJefe",
    "puestoJefe",
    "telOfic",
    "curp",
    "rfc",
    "sexo",
    "ecivil",
    "nacion",
    "prof",
    "lugnac",
    "pais",
    "persf"
})
public class ZstDatosPerson {

    @XmlElement(name = "FechaNac", required = true)
    protected String fechaNac;
    @XmlElement(name = "Depend", required = true)
    protected String depend;
    @XmlElement(name = "EdadDepen", required = true)
    protected String edadDepen;
    @XmlElement(name = "RedSocial", required = true)
    protected String redSocial;
    @XmlElement(name = "TipoViv", required = true)
    protected String tipoViv;
    @XmlElement(name = "ValViv", required = true)
    protected String valViv;
    @XmlElement(name = "Renta", required = true)
    protected String renta;
    @XmlElement(name = "TiempRes", required = true)
    protected String tiempRes;
    @XmlElement(name = "Empresa", required = true)
    protected String empresa;
    @XmlElement(name = "TipoEmpr", required = true)
    protected String tipoEmpr;
    @XmlElement(name = "RelEmp", required = true)
    protected String relEmp;
    @XmlElement(name = "Contr", required = true)
    protected String contr;
    @XmlElement(name = "Sueldo", required = true)
    protected String sueldo;
    @XmlElement(name = "FrecPag", required = true)
    protected String frecPag;
    @XmlElement(name = "FechIngr", required = true)
    protected String fechIngr;
    @XmlElement(name = "Antig", required = true)
    protected String antig;
    @XmlElement(name = "FormPag", required = true)
    protected String formPag;
    @XmlElement(name = "Puesto", required = true)
    protected String puesto;
    @XmlElement(name = "TelTrab", required = true)
    protected String telTrab;
    @XmlElement(name = "Ext", required = true)
    protected String ext;
    @XmlElement(name = "TelAlt", required = true)
    protected String telAlt;
    @XmlElement(name = "MailTrab", required = true)
    protected String mailTrab;
    @XmlElement(name = "CalleTrab", required = true)
    protected String calleTrab;
    @XmlElement(name = "NoExt", required = true)
    protected String noExt;
    @XmlElement(name = "NoInt", required = true)
    protected String noInt;
    @XmlElement(name = "Colonia", required = true)
    protected String colonia;
    @XmlElement(name = "Cp", required = true)
    protected String cp;
    @XmlElement(name = "Ciudad", required = true)
    protected String ciudad;
    @XmlElement(name = "Estado", required = true)
    protected String estado;
    @XmlElement(name = "NombreJefe", required = true)
    protected String nombreJefe;
    @XmlElement(name = "PuestoJefe", required = true)
    protected String puestoJefe;
    @XmlElement(name = "TelOfic", required = true)
    protected String telOfic;
    @XmlElement(name = "Curp", required = true)
    protected String curp;
    @XmlElement(name = "Rfc", required = true)
    protected String rfc;
    @XmlElement(name = "Sexo", required = true)
    protected String sexo;
    @XmlElement(name = "Ecivil", required = true)
    protected String ecivil;
    @XmlElement(name = "Nacion", required = true)
    protected String nacion;
    @XmlElement(name = "Prof", required = true)
    protected String prof;
    @XmlElement(name = "Lugnac", required = true)
    protected String lugnac;
    @XmlElement(name = "Pais", required = true)
    protected String pais;
    @XmlElement(name = "Persf", required = true)
    protected String persf;

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
     * Obtiene el valor de la propiedad depend.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepend() {
        return depend;
    }

    /**
     * Define el valor de la propiedad depend.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepend(String value) {
        this.depend = value;
    }

    /**
     * Obtiene el valor de la propiedad edadDepen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEdadDepen() {
        return edadDepen;
    }

    /**
     * Define el valor de la propiedad edadDepen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEdadDepen(String value) {
        this.edadDepen = value;
    }

    /**
     * Obtiene el valor de la propiedad redSocial.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRedSocial() {
        return redSocial;
    }

    /**
     * Define el valor de la propiedad redSocial.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRedSocial(String value) {
        this.redSocial = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoViv.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoViv() {
        return tipoViv;
    }

    /**
     * Define el valor de la propiedad tipoViv.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoViv(String value) {
        this.tipoViv = value;
    }

    /**
     * Obtiene el valor de la propiedad valViv.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValViv() {
        return valViv;
    }

    /**
     * Define el valor de la propiedad valViv.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValViv(String value) {
        this.valViv = value;
    }

    /**
     * Obtiene el valor de la propiedad renta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRenta() {
        return renta;
    }

    /**
     * Define el valor de la propiedad renta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRenta(String value) {
        this.renta = value;
    }

    /**
     * Obtiene el valor de la propiedad tiempRes.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTiempRes() {
        return tiempRes;
    }

    /**
     * Define el valor de la propiedad tiempRes.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTiempRes(String value) {
        this.tiempRes = value;
    }

    /**
     * Obtiene el valor de la propiedad empresa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmpresa() {
        return empresa;
    }

    /**
     * Define el valor de la propiedad empresa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmpresa(String value) {
        this.empresa = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoEmpr.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoEmpr() {
        return tipoEmpr;
    }

    /**
     * Define el valor de la propiedad tipoEmpr.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoEmpr(String value) {
        this.tipoEmpr = value;
    }

    /**
     * Obtiene el valor de la propiedad relEmp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelEmp() {
        return relEmp;
    }

    /**
     * Define el valor de la propiedad relEmp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelEmp(String value) {
        this.relEmp = value;
    }

    /**
     * Obtiene el valor de la propiedad contr.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContr() {
        return contr;
    }

    /**
     * Define el valor de la propiedad contr.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContr(String value) {
        this.contr = value;
    }

    /**
     * Obtiene el valor de la propiedad sueldo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSueldo() {
        return sueldo;
    }

    /**
     * Define el valor de la propiedad sueldo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSueldo(String value) {
        this.sueldo = value;
    }

    /**
     * Obtiene el valor de la propiedad frecPag.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFrecPag() {
        return frecPag;
    }

    /**
     * Define el valor de la propiedad frecPag.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFrecPag(String value) {
        this.frecPag = value;
    }

    /**
     * Obtiene el valor de la propiedad fechIngr.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechIngr() {
        return fechIngr;
    }

    /**
     * Define el valor de la propiedad fechIngr.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechIngr(String value) {
        this.fechIngr = value;
    }

    /**
     * Obtiene el valor de la propiedad antig.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAntig() {
        return antig;
    }

    /**
     * Define el valor de la propiedad antig.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAntig(String value) {
        this.antig = value;
    }

    /**
     * Obtiene el valor de la propiedad formPag.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormPag() {
        return formPag;
    }

    /**
     * Define el valor de la propiedad formPag.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormPag(String value) {
        this.formPag = value;
    }

    /**
     * Obtiene el valor de la propiedad puesto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPuesto() {
        return puesto;
    }

    /**
     * Define el valor de la propiedad puesto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPuesto(String value) {
        this.puesto = value;
    }

    /**
     * Obtiene el valor de la propiedad telTrab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelTrab() {
        return telTrab;
    }

    /**
     * Define el valor de la propiedad telTrab.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelTrab(String value) {
        this.telTrab = value;
    }

    /**
     * Obtiene el valor de la propiedad ext.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExt() {
        return ext;
    }

    /**
     * Define el valor de la propiedad ext.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExt(String value) {
        this.ext = value;
    }

    /**
     * Obtiene el valor de la propiedad telAlt.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelAlt() {
        return telAlt;
    }

    /**
     * Define el valor de la propiedad telAlt.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelAlt(String value) {
        this.telAlt = value;
    }

    /**
     * Obtiene el valor de la propiedad mailTrab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMailTrab() {
        return mailTrab;
    }

    /**
     * Define el valor de la propiedad mailTrab.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMailTrab(String value) {
        this.mailTrab = value;
    }

    /**
     * Obtiene el valor de la propiedad calleTrab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCalleTrab() {
        return calleTrab;
    }

    /**
     * Define el valor de la propiedad calleTrab.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCalleTrab(String value) {
        this.calleTrab = value;
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
     * Obtiene el valor de la propiedad nombreJefe.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreJefe() {
        return nombreJefe;
    }

    /**
     * Define el valor de la propiedad nombreJefe.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreJefe(String value) {
        this.nombreJefe = value;
    }

    /**
     * Obtiene el valor de la propiedad puestoJefe.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPuestoJefe() {
        return puestoJefe;
    }

    /**
     * Define el valor de la propiedad puestoJefe.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPuestoJefe(String value) {
        this.puestoJefe = value;
    }

    /**
     * Obtiene el valor de la propiedad telOfic.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelOfic() {
        return telOfic;
    }

    /**
     * Define el valor de la propiedad telOfic.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelOfic(String value) {
        this.telOfic = value;
    }

    /**
     * Obtiene el valor de la propiedad curp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurp() {
        return curp;
    }

    /**
     * Define el valor de la propiedad curp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurp(String value) {
        this.curp = value;
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
     * Obtiene el valor de la propiedad ecivil.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEcivil() {
        return ecivil;
    }

    /**
     * Define el valor de la propiedad ecivil.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEcivil(String value) {
        this.ecivil = value;
    }

    /**
     * Obtiene el valor de la propiedad nacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNacion() {
        return nacion;
    }

    /**
     * Define el valor de la propiedad nacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNacion(String value) {
        this.nacion = value;
    }

    /**
     * Obtiene el valor de la propiedad prof.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProf() {
        return prof;
    }

    /**
     * Define el valor de la propiedad prof.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProf(String value) {
        this.prof = value;
    }

    /**
     * Obtiene el valor de la propiedad lugnac.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLugnac() {
        return lugnac;
    }

    /**
     * Define el valor de la propiedad lugnac.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLugnac(String value) {
        this.lugnac = value;
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
     * Obtiene el valor de la propiedad persf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersf() {
        return persf;
    }

    /**
     * Define el valor de la propiedad persf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersf(String value) {
        this.persf = value;
    }

}
