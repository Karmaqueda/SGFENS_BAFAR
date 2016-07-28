
package com.sap.bafar.ws.creacontrato;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ZstContract complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ZstContract">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Bukrs001" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="Gsart002" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="Ssuch003" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="Santwhr008" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="Xdantrag009" type="{urn:sap-com:document:sap:rfc:functions}date10"/>
 *         &lt;element name="Xbantrag010" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="Sfrist011" type="{urn:sap-com:document:sap:rfc:functions}char2"/>
 *         &lt;element name="Sbilk012" type="{urn:sap-com:document:sap:rfc:functions}char2"/>
 *         &lt;element name="Sgr2014" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="Sgrp3015" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="Xalkz016" type="{urn:sap-com:document:sap:rfc:functions}char132"/>
 *         &lt;element name="Xallb017" type="{urn:sap-com:document:sap:rfc:functions}char132"/>
 *         &lt;element name="SchemeId018" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="Xbzusage019" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="Sdisein020" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="Szbmeth021" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="XdguelKk022" type="{urn:sap-com:document:sap:rfc:functions}date10"/>
 *         &lt;element name="Seffmeth023" type="{urn:sap-com:document:sap:rfc:functions}char132"/>
 *         &lt;element name="Xdefsz024" type="{urn:sap-com:document:sap:rfc:functions}date10"/>
 *         &lt;element name="Xdelfz026" type="{urn:sap-com:document:sap:rfc:functions}date10"/>
 *         &lt;element name="DguelKp01036" type="{urn:sap-com:document:sap:rfc:functions}date10"/>
 *         &lt;element name="DguelKp02037" type="{urn:sap-com:document:sap:rfc:functions}date10"/>
 *         &lt;element name="Pkond01038" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="Pkond02039" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="Bkond01" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="Bkond02" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="Ammrhy01041" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="Ammrhy02042" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="Dfaell01043" type="{urn:sap-com:document:sap:rfc:functions}date10"/>
 *         &lt;element name="Dfaell02044" type="{urn:sap-com:document:sap:rfc:functions}date10"/>
 *         &lt;element name="Dvalut01045" type="{urn:sap-com:document:sap:rfc:functions}date10"/>
 *         &lt;element name="Dvalut02046" type="{urn:sap-com:document:sap:rfc:functions}date10"/>
 *         &lt;element name="Sfult01047" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="Sfult02048" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="Svult01049" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="Svult02050" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="DguelKp060" type="{urn:sap-com:document:sap:rfc:functions}date10"/>
 *         &lt;element name="Zlsch066" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="Mwskz067" type="{urn:sap-com:document:sap:rfc:functions}char2"/>
 *         &lt;element name="Sbust068" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="Seffmeth087" type="{urn:sap-com:document:sap:rfc:functions}char132"/>
 *         &lt;element name="Role091" type="{urn:sap-com:document:sap:rfc:functions}char132"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZstContract", propOrder = {
    "bukrs001",
    "gsart002",
    "ssuch003",
    "santwhr008",
    "xdantrag009",
    "xbantrag010",
    "sfrist011",
    "sbilk012",
    "sgr2014",
    "sgrp3015",
    "xalkz016",
    "xallb017",
    "schemeId018",
    "xbzusage019",
    "sdisein020",
    "szbmeth021",
    "xdguelKk022",
    "seffmeth023",
    "xdefsz024",
    "xdelfz026",
    "dguelKp01036",
    "dguelKp02037",
    "pkond01038",
    "pkond02039",
    "bkond01",
    "bkond02",
    "ammrhy01041",
    "ammrhy02042",
    "dfaell01043",
    "dfaell02044",
    "dvalut01045",
    "dvalut02046",
    "sfult01047",
    "sfult02048",
    "svult01049",
    "svult02050",
    "dguelKp060",
    "zlsch066",
    "mwskz067",
    "sbust068",
    "seffmeth087",
    "role091"
})
public class ZstContract {

    @XmlElement(name = "Bukrs001", required = true)
    protected String bukrs001;
    @XmlElement(name = "Gsart002", required = true)
    protected String gsart002;
    @XmlElement(name = "Ssuch003", required = true)
    protected String ssuch003;
    @XmlElement(name = "Santwhr008", required = true)
    protected String santwhr008;
    @XmlElement(name = "Xdantrag009", required = true)
    protected String xdantrag009;
    @XmlElement(name = "Xbantrag010", required = true)
    protected String xbantrag010;
    @XmlElement(name = "Sfrist011", required = true)
    protected String sfrist011;
    @XmlElement(name = "Sbilk012", required = true)
    protected String sbilk012;
    @XmlElement(name = "Sgr2014", required = true)
    protected String sgr2014;
    @XmlElement(name = "Sgrp3015", required = true)
    protected String sgrp3015;
    @XmlElement(name = "Xalkz016", required = true)
    protected String xalkz016;
    @XmlElement(name = "Xallb017", required = true)
    protected String xallb017;
    @XmlElement(name = "SchemeId018", required = true)
    protected String schemeId018;
    @XmlElement(name = "Xbzusage019", required = true)
    protected String xbzusage019;
    @XmlElement(name = "Sdisein020", required = true)
    protected String sdisein020;
    @XmlElement(name = "Szbmeth021", required = true)
    protected String szbmeth021;
    @XmlElement(name = "XdguelKk022", required = true)
    protected String xdguelKk022;
    @XmlElement(name = "Seffmeth023", required = true)
    protected String seffmeth023;
    @XmlElement(name = "Xdefsz024", required = true)
    protected String xdefsz024;
    @XmlElement(name = "Xdelfz026", required = true)
    protected String xdelfz026;
    @XmlElement(name = "DguelKp01036", required = true)
    protected String dguelKp01036;
    @XmlElement(name = "DguelKp02037", required = true)
    protected String dguelKp02037;
    @XmlElement(name = "Pkond01038", required = true)
    protected String pkond01038;
    @XmlElement(name = "Pkond02039", required = true)
    protected String pkond02039;
    @XmlElement(name = "Bkond01", required = true)
    protected String bkond01;
    @XmlElement(name = "Bkond02", required = true)
    protected String bkond02;
    @XmlElement(name = "Ammrhy01041", required = true)
    protected String ammrhy01041;
    @XmlElement(name = "Ammrhy02042", required = true)
    protected String ammrhy02042;
    @XmlElement(name = "Dfaell01043", required = true)
    protected String dfaell01043;
    @XmlElement(name = "Dfaell02044", required = true)
    protected String dfaell02044;
    @XmlElement(name = "Dvalut01045", required = true)
    protected String dvalut01045;
    @XmlElement(name = "Dvalut02046", required = true)
    protected String dvalut02046;
    @XmlElement(name = "Sfult01047", required = true)
    protected String sfult01047;
    @XmlElement(name = "Sfult02048", required = true)
    protected String sfult02048;
    @XmlElement(name = "Svult01049", required = true)
    protected String svult01049;
    @XmlElement(name = "Svult02050", required = true)
    protected String svult02050;
    @XmlElement(name = "DguelKp060", required = true)
    protected String dguelKp060;
    @XmlElement(name = "Zlsch066", required = true)
    protected String zlsch066;
    @XmlElement(name = "Mwskz067", required = true)
    protected String mwskz067;
    @XmlElement(name = "Sbust068", required = true)
    protected String sbust068;
    @XmlElement(name = "Seffmeth087", required = true)
    protected String seffmeth087;
    @XmlElement(name = "Role091", required = true)
    protected String role091;

    /**
     * Obtiene el valor de la propiedad bukrs001.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBukrs001() {
        return bukrs001;
    }

    /**
     * Define el valor de la propiedad bukrs001.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBukrs001(String value) {
        this.bukrs001 = value;
    }

    /**
     * Obtiene el valor de la propiedad gsart002.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGsart002() {
        return gsart002;
    }

    /**
     * Define el valor de la propiedad gsart002.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGsart002(String value) {
        this.gsart002 = value;
    }

    /**
     * Obtiene el valor de la propiedad ssuch003.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSsuch003() {
        return ssuch003;
    }

    /**
     * Define el valor de la propiedad ssuch003.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSsuch003(String value) {
        this.ssuch003 = value;
    }

    /**
     * Obtiene el valor de la propiedad santwhr008.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSantwhr008() {
        return santwhr008;
    }

    /**
     * Define el valor de la propiedad santwhr008.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSantwhr008(String value) {
        this.santwhr008 = value;
    }

    /**
     * Obtiene el valor de la propiedad xdantrag009.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXdantrag009() {
        return xdantrag009;
    }

    /**
     * Define el valor de la propiedad xdantrag009.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXdantrag009(String value) {
        this.xdantrag009 = value;
    }

    /**
     * Obtiene el valor de la propiedad xbantrag010.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXbantrag010() {
        return xbantrag010;
    }

    /**
     * Define el valor de la propiedad xbantrag010.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXbantrag010(String value) {
        this.xbantrag010 = value;
    }

    /**
     * Obtiene el valor de la propiedad sfrist011.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSfrist011() {
        return sfrist011;
    }

    /**
     * Define el valor de la propiedad sfrist011.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSfrist011(String value) {
        this.sfrist011 = value;
    }

    /**
     * Obtiene el valor de la propiedad sbilk012.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSbilk012() {
        return sbilk012;
    }

    /**
     * Define el valor de la propiedad sbilk012.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSbilk012(String value) {
        this.sbilk012 = value;
    }

    /**
     * Obtiene el valor de la propiedad sgr2014.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSgr2014() {
        return sgr2014;
    }

    /**
     * Define el valor de la propiedad sgr2014.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSgr2014(String value) {
        this.sgr2014 = value;
    }

    /**
     * Obtiene el valor de la propiedad sgrp3015.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSgrp3015() {
        return sgrp3015;
    }

    /**
     * Define el valor de la propiedad sgrp3015.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSgrp3015(String value) {
        this.sgrp3015 = value;
    }

    /**
     * Obtiene el valor de la propiedad xalkz016.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXalkz016() {
        return xalkz016;
    }

    /**
     * Define el valor de la propiedad xalkz016.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXalkz016(String value) {
        this.xalkz016 = value;
    }

    /**
     * Obtiene el valor de la propiedad xallb017.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXallb017() {
        return xallb017;
    }

    /**
     * Define el valor de la propiedad xallb017.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXallb017(String value) {
        this.xallb017 = value;
    }

    /**
     * Obtiene el valor de la propiedad schemeId018.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchemeId018() {
        return schemeId018;
    }

    /**
     * Define el valor de la propiedad schemeId018.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchemeId018(String value) {
        this.schemeId018 = value;
    }

    /**
     * Obtiene el valor de la propiedad xbzusage019.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXbzusage019() {
        return xbzusage019;
    }

    /**
     * Define el valor de la propiedad xbzusage019.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXbzusage019(String value) {
        this.xbzusage019 = value;
    }

    /**
     * Obtiene el valor de la propiedad sdisein020.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSdisein020() {
        return sdisein020;
    }

    /**
     * Define el valor de la propiedad sdisein020.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSdisein020(String value) {
        this.sdisein020 = value;
    }

    /**
     * Obtiene el valor de la propiedad szbmeth021.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSzbmeth021() {
        return szbmeth021;
    }

    /**
     * Define el valor de la propiedad szbmeth021.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSzbmeth021(String value) {
        this.szbmeth021 = value;
    }

    /**
     * Obtiene el valor de la propiedad xdguelKk022.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXdguelKk022() {
        return xdguelKk022;
    }

    /**
     * Define el valor de la propiedad xdguelKk022.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXdguelKk022(String value) {
        this.xdguelKk022 = value;
    }

    /**
     * Obtiene el valor de la propiedad seffmeth023.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSeffmeth023() {
        return seffmeth023;
    }

    /**
     * Define el valor de la propiedad seffmeth023.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSeffmeth023(String value) {
        this.seffmeth023 = value;
    }

    /**
     * Obtiene el valor de la propiedad xdefsz024.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXdefsz024() {
        return xdefsz024;
    }

    /**
     * Define el valor de la propiedad xdefsz024.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXdefsz024(String value) {
        this.xdefsz024 = value;
    }

    /**
     * Obtiene el valor de la propiedad xdelfz026.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXdelfz026() {
        return xdelfz026;
    }

    /**
     * Define el valor de la propiedad xdelfz026.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXdelfz026(String value) {
        this.xdelfz026 = value;
    }

    /**
     * Obtiene el valor de la propiedad dguelKp01036.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDguelKp01036() {
        return dguelKp01036;
    }

    /**
     * Define el valor de la propiedad dguelKp01036.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDguelKp01036(String value) {
        this.dguelKp01036 = value;
    }

    /**
     * Obtiene el valor de la propiedad dguelKp02037.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDguelKp02037() {
        return dguelKp02037;
    }

    /**
     * Define el valor de la propiedad dguelKp02037.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDguelKp02037(String value) {
        this.dguelKp02037 = value;
    }

    /**
     * Obtiene el valor de la propiedad pkond01038.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPkond01038() {
        return pkond01038;
    }

    /**
     * Define el valor de la propiedad pkond01038.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPkond01038(String value) {
        this.pkond01038 = value;
    }

    /**
     * Obtiene el valor de la propiedad pkond02039.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPkond02039() {
        return pkond02039;
    }

    /**
     * Define el valor de la propiedad pkond02039.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPkond02039(String value) {
        this.pkond02039 = value;
    }

    /**
     * Obtiene el valor de la propiedad bkond01.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBkond01() {
        return bkond01;
    }

    /**
     * Define el valor de la propiedad bkond01.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBkond01(String value) {
        this.bkond01 = value;
    }

    /**
     * Obtiene el valor de la propiedad bkond02.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBkond02() {
        return bkond02;
    }

    /**
     * Define el valor de la propiedad bkond02.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBkond02(String value) {
        this.bkond02 = value;
    }

    /**
     * Obtiene el valor de la propiedad ammrhy01041.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmmrhy01041() {
        return ammrhy01041;
    }

    /**
     * Define el valor de la propiedad ammrhy01041.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmmrhy01041(String value) {
        this.ammrhy01041 = value;
    }

    /**
     * Obtiene el valor de la propiedad ammrhy02042.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmmrhy02042() {
        return ammrhy02042;
    }

    /**
     * Define el valor de la propiedad ammrhy02042.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmmrhy02042(String value) {
        this.ammrhy02042 = value;
    }

    /**
     * Obtiene el valor de la propiedad dfaell01043.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDfaell01043() {
        return dfaell01043;
    }

    /**
     * Define el valor de la propiedad dfaell01043.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDfaell01043(String value) {
        this.dfaell01043 = value;
    }

    /**
     * Obtiene el valor de la propiedad dfaell02044.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDfaell02044() {
        return dfaell02044;
    }

    /**
     * Define el valor de la propiedad dfaell02044.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDfaell02044(String value) {
        this.dfaell02044 = value;
    }

    /**
     * Obtiene el valor de la propiedad dvalut01045.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDvalut01045() {
        return dvalut01045;
    }

    /**
     * Define el valor de la propiedad dvalut01045.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDvalut01045(String value) {
        this.dvalut01045 = value;
    }

    /**
     * Obtiene el valor de la propiedad dvalut02046.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDvalut02046() {
        return dvalut02046;
    }

    /**
     * Define el valor de la propiedad dvalut02046.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDvalut02046(String value) {
        this.dvalut02046 = value;
    }

    /**
     * Obtiene el valor de la propiedad sfult01047.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSfult01047() {
        return sfult01047;
    }

    /**
     * Define el valor de la propiedad sfult01047.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSfult01047(String value) {
        this.sfult01047 = value;
    }

    /**
     * Obtiene el valor de la propiedad sfult02048.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSfult02048() {
        return sfult02048;
    }

    /**
     * Define el valor de la propiedad sfult02048.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSfult02048(String value) {
        this.sfult02048 = value;
    }

    /**
     * Obtiene el valor de la propiedad svult01049.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSvult01049() {
        return svult01049;
    }

    /**
     * Define el valor de la propiedad svult01049.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSvult01049(String value) {
        this.svult01049 = value;
    }

    /**
     * Obtiene el valor de la propiedad svult02050.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSvult02050() {
        return svult02050;
    }

    /**
     * Define el valor de la propiedad svult02050.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSvult02050(String value) {
        this.svult02050 = value;
    }

    /**
     * Obtiene el valor de la propiedad dguelKp060.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDguelKp060() {
        return dguelKp060;
    }

    /**
     * Define el valor de la propiedad dguelKp060.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDguelKp060(String value) {
        this.dguelKp060 = value;
    }

    /**
     * Obtiene el valor de la propiedad zlsch066.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZlsch066() {
        return zlsch066;
    }

    /**
     * Define el valor de la propiedad zlsch066.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZlsch066(String value) {
        this.zlsch066 = value;
    }

    /**
     * Obtiene el valor de la propiedad mwskz067.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMwskz067() {
        return mwskz067;
    }

    /**
     * Define el valor de la propiedad mwskz067.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMwskz067(String value) {
        this.mwskz067 = value;
    }

    /**
     * Obtiene el valor de la propiedad sbust068.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSbust068() {
        return sbust068;
    }

    /**
     * Define el valor de la propiedad sbust068.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSbust068(String value) {
        this.sbust068 = value;
    }

    /**
     * Obtiene el valor de la propiedad seffmeth087.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSeffmeth087() {
        return seffmeth087;
    }

    /**
     * Define el valor de la propiedad seffmeth087.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSeffmeth087(String value) {
        this.seffmeth087 = value;
    }

    /**
     * Obtiene el valor de la propiedad role091.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRole091() {
        return role091;
    }

    /**
     * Define el valor de la propiedad role091.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRole091(String value) {
        this.role091 = value;
    }

}
