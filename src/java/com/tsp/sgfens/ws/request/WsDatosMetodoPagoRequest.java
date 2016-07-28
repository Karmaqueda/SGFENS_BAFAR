/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.request;

import java.util.Date;

/**
 *
 * @author ISCesarMartinez
 */
public class WsDatosMetodoPagoRequest {
    
    private int metodoPagoId;
    private String metodoPagoDescripcion;

    private Date metodoPagoCreditoFecha;
    private String metodoPagoDocumentoNumero;
    
    protected String imagenFirmaBytesBase64;

    public String getMetodoPagoDescripcion() {
        return metodoPagoDescripcion;
    }

    public void setMetodoPagoDescripcion(String metodoPagoDescripcion) {
        this.metodoPagoDescripcion = metodoPagoDescripcion;
    }

    public String getMetodoPagoDocumentoNumero() {
        return metodoPagoDocumentoNumero;
    }

    public void setMetodoPagoDocumentoNumero(String metodoPagoDocumentoNumero) {
        this.metodoPagoDocumentoNumero = metodoPagoDocumentoNumero;
    }

    public int getMetodoPagoId() {
        return metodoPagoId;
    }

    public void setMetodoPagoId(int metodoPagoId) {
        this.metodoPagoId = metodoPagoId;
    }

    public Date getMetodoPagoCreditoFecha() {
        return metodoPagoCreditoFecha;
    }

    public void setMetodoPagoCreditoFecha(Date metodoPagoCreditoFecha) {
        this.metodoPagoCreditoFecha = metodoPagoCreditoFecha;
    }

    public String getImagenFirmaBytesBase64() {
        return imagenFirmaBytesBase64;
    }

    public void setImagenFirmaBytesBase64(String imagenFirmaBytesBase64) {
        this.imagenFirmaBytesBase64 = imagenFirmaBytesBase64;
    }
    
}
