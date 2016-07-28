/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Leonardo
 */
public class WsItemSucursal implements Serializable{

    private int idEmpresa;
    private int idEmpresaPadre;
    private int idTipoEmpresa;
    private String rfc;
    private String razonSocial;
    private String nombreComercial;
    private WsItemUbicacionFiscal ubicacionFiscal;
    //private int numLicenciasCompradas;
    //private int numLicenciasDisponibles;
    private double latitud;
    private double longitud;
    private int foliosDisponibles;
    private String regimenFiscal;
    protected String mensajePersonalizadoVisita;
    private int printRazonSocial;
    private int printNombreComercial;
    private int factura_activa;
    private int cobro_tarjeta_activa;
    private List<WsItemSucursal> wsItemSucursal;
    private int tipoConsumoServicio; // 0 = normal (contrato), 1 = pre-pago (creditos de operacion)
    private int creditosOperacionDisponibles;
    private byte[] logo;
    
    private int rfcPorNipCodigo;
    private int printTkProds;
    
    private int tkMovilTipo;
    private int tkMovilPorMarca;
    private int tkMovilMostrarZona;
    private int tkMovilMostrarFolio;
    
    /**
     * Obtiene el id de la empresa.
     * <p/>
     * @return int con el id de la empresa.
     */
    public int getIdEmpresa() {
        return idEmpresa;
    }

    /**
     * Inicializa el id de la empresa.
     * <p/>
     * @param idEmpresa
     * int con el id de la empresa.
     */
    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    /**
     * Obtiene el id de la empresa padre.
     * <p/>
     * @return int con el id de la empresa padre.
     */
    public int getIdEmpresaPadre() {
        return idEmpresaPadre;
    }

    /**
     * Inicializa el id de la empresa padre
     * <p/>
     * @param idEmpresaPadre
     * Id de la empresa padre.
     */
    public void setIdEmpresaPadre(int idEmpresaPadre) {
        this.idEmpresaPadre = idEmpresaPadre;
    }

    /**
     * Obtiene el id del tipo de empresa.
     * <p/>
     * @return Id del tipo de empresa.
     */
    public int getIdTipoEmpresa() {
        return idTipoEmpresa;
    }

    /**
     * Inicializa el tipo de empresa.
     * <p/>
     * @param idTipoEmpresa
     * Id del tipo de empresa
     */
    public void setIdTipoEmpresa(int idTipoEmpresa) {
        this.idTipoEmpresa = idTipoEmpresa;
    }

    /**
     * Obtien el RFC de una empresa
     * <p/>
     * @return RFC
     */
    public String getRfc() {
        return rfc;
    }

    /**
     * Inicializa el RFC de una empresa
     * <p/>
     * @param rfc
     * RFC de la empresa
     */
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    /**
     * Obtiene la razon social de la empresa
     * <p/>
     * @return Razon social de la empresa
     */
    public String getRazonSocial() {
        return razonSocial;
    }

    /**
     * Inicializa la razon social de la emprea
     * <p/>
     * @param razonSocial
     */
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    /**
     * Obtiene el nombre comercial de una empresa
     * <p/>
     * @return Nombre comercial de la empresa.
     */
    public String getNombreComercial() {
        return nombreComercial;
    }

    /**
     * Inicializa el nombre comercial de la empresa.
     * <p/>
     * @param nombreComercial
     * Nombre comercial del ala empresa.
     */
    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    /**
     * Obtiene la ubicacion fiscal de la empresa.
     * <p/>
     * @return Ubicacion fiscal de la empresa.
     */
    public WsItemUbicacionFiscal getUbicacionFiscal() {
        return ubicacionFiscal;
    }

    /**
     * Inicializa la ubicacion fiscal de la empresa.
     * <p/>
     * @param ubicacionFiscal Ubicacion fiscal de la empresa.
     */
    public void setUbicacionFiscal(WsItemUbicacionFiscal ubicacionFiscal) {
        this.ubicacionFiscal = ubicacionFiscal;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    /*
    public int getNumLicenciasCompradas() {
        return numLicenciasCompradas;
    }

    public void setNumLicenciasCompradas(int numLicenciasCompradas) {
        this.numLicenciasCompradas = numLicenciasCompradas;
    }

    public int getNumLicenciasDisponibles() {
        return numLicenciasDisponibles;
    }

    public void setNumLicenciasDisponibles(int numLicenciasDisponibles) {
        this.numLicenciasDisponibles = numLicenciasDisponibles;
    }
    * 
    */

    public List<WsItemSucursal> getWsItemSucursal() {
        return wsItemSucursal;
    }

    public void setWsItemSucursal(List<WsItemSucursal> wsItemSucursal) {
        this.wsItemSucursal = wsItemSucursal;
    }

    public int getFoliosDisponibles() {
        return foliosDisponibles;
    }

    public void setFoliosDisponibles(int foliosDisponibles) {
        this.foliosDisponibles = foliosDisponibles;
    }

    public String getRegimenFiscal() {
        return regimenFiscal;
    }

    public void setRegimenFiscal(String regimenFiscal) {
        this.regimenFiscal = regimenFiscal;
    }

    /**
     * @return the mensajePersonalizadoVisita
     */
    public String getMensajePersonalizadoVisita() {
        return mensajePersonalizadoVisita;
    }

    /**
     * @param mensajePersonalizadoVisita the mensajePersonalizadoVisita to set
     */
    public void setMensajePersonalizadoVisita(String mensajePersonalizadoVisita) {
        this.mensajePersonalizadoVisita = mensajePersonalizadoVisita;
    }

    /**
     * @return the printRazonSocial
     */
    public int getPrintRazonSocial() {
        return printRazonSocial;
    }

    /**
     * @param printRazonSocial the printRazonSocial to set
     */
    public void setPrintRazonSocial(int printRazonSocial) {
        this.printRazonSocial = printRazonSocial;
    }

    /**
     * @return the printNombreComercial
     */
    public int getPrintNombreComercial() {
        return printNombreComercial;
    }

    /**
     * @param printNombreComercial the printNombreComercial to set
     */
    public void setPrintNombreComercial(int printNombreComercial) {
        this.printNombreComercial = printNombreComercial;
    }

    /**
     * @return the factura_activa
     */
    public int getFactura_activa() {
        return factura_activa;
    }

    /**
     * @param factura_activa the factura_activa to set
     */
    public void setFactura_activa(int factura_activa) {
        this.factura_activa = factura_activa;
    }

    /**
     * @return the cobro_tarjeta_activa
     */
    public int getCobro_tarjeta_activa() {
        return cobro_tarjeta_activa;
    }

    /**
     * @param cobro_tarjeta_activa the cobro_tarjeta_activa to set
     */
    public void setCobro_tarjeta_activa(int cobro_tarjeta_activa) {
        this.cobro_tarjeta_activa = cobro_tarjeta_activa;
    }

    public int getTipoConsumoServicio() {
        return tipoConsumoServicio;
    }

    public void setTipoConsumoServicio(int tipoConsumoServicio) {
        this.tipoConsumoServicio = tipoConsumoServicio;
    }

    public int getCreditosOperacionDisponibles() {
        return creditosOperacionDisponibles;
    }

    public void setCreditosOperacionDisponibles(int creditosOperacionDisponibles) {
        this.creditosOperacionDisponibles = creditosOperacionDisponibles;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    /**
     * @return the rfcPorNipCodigo
     */
    public int getRfcPorNipCodigo() {
        return rfcPorNipCodigo;
    }

    /**
     * @param rfcPorNipCodigo the rfcPorNipCodigo to set
     */
    public void setRfcPorNipCodigo(int rfcPorNipCodigo) {
        this.rfcPorNipCodigo = rfcPorNipCodigo;
    }

    public int getPrintTkProds() {
        return printTkProds;
    }

    public void setPrintTkProds(int printTkProds) {
        this.printTkProds = printTkProds;
    }    

    public int getTkMovilTipo() {
        return tkMovilTipo;
    }

    public void setTkMovilTipo(int tkMovilTipo) {
        this.tkMovilTipo = tkMovilTipo;
    }

    public int getTkMovilPorMarca() {
        return tkMovilPorMarca;
    }

    public void setTkMovilPorMarca(int tkMovilPorMarca) {
        this.tkMovilPorMarca = tkMovilPorMarca;
    }

    public int getTkMovilMostrarZona() {
        return tkMovilMostrarZona;
    }

    public void setTkMovilMostrarZona(int tkMovilMostrarZona) {
        this.tkMovilMostrarZona = tkMovilMostrarZona;
    }

    public int getTkMovilMostrarFolio() {
        return tkMovilMostrarFolio;
    }

    public void setTkMovilMostrarFolio(int tkMovilMostrarFolio) {
        this.tkMovilMostrarFolio = tkMovilMostrarFolio;
    }
       
}
