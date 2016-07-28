/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

import com.tsp.sgfens.ws.response.comprobante.WSComprobanteFiscalDigital;
import com.tsp.sgfens.ws.response.comprobante.WSTimbreFiscalDigital;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 17-may-2013 
 */
public class CrearFacturaResponse extends WSResponseInsert{
    
    private boolean isCFDI32 = true;
    private boolean isCBB = false;
    
    /**
     * Los siguientes 3 elementos solo estaran completos en caso de 
     * no existir errores en el proceso y si es una facturación
     * CFDI.  No aplican para CBB.
     */
    
    private byte[] xmlCFDITimbrado;
    private WSComprobanteFiscalDigital comprobanteFiscalDigital;
    private WSTimbreFiscalDigital timbreFiscalDigitalResponse;

    public WSComprobanteFiscalDigital getComprobanteFiscalDigital() {
        return comprobanteFiscalDigital;
    }

    public void setComprobanteFiscalDigital(WSComprobanteFiscalDigital comprobanteFiscalDigital) {
        this.comprobanteFiscalDigital = comprobanteFiscalDigital;
    }

    public WSTimbreFiscalDigital getTimbreFiscalDigitalResponse() {
        return timbreFiscalDigitalResponse;
    }

    public void setTimbreFiscalDigitalResponse(WSTimbreFiscalDigital timbreFiscalDigitalResponse) {
        this.timbreFiscalDigitalResponse = timbreFiscalDigitalResponse;
    }

    public byte[] getXmlCFDITimbrado() {
        return xmlCFDITimbrado;
    }

    public void setXmlCFDITimbrado(byte[] xmlCFDITimbrado) {
        this.xmlCFDITimbrado = xmlCFDITimbrado;
    }

    public boolean isIsCBB() {
        return isCBB;
    }

    public void setIsCBB(boolean isCBB) {
        if (isCBB)
            this.isCFDI32 = false;
        this.isCBB = isCBB;
    }

    public boolean isIsCFDI32() {
        return isCFDI32;
    }

    public void setIsCFDI32(boolean isCFDI32) {
        if (isCFDI32)
            this.isCBB = false;
        this.isCFDI32 = isCFDI32;
    }

    
}
