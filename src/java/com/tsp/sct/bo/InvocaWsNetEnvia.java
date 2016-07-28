/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.interconecta.ws.WsGenericResp;
import com.tsp.interconecta.ws.WsInsertaClienteResp;

/**
 *
 * @author ISC César Ulises Martínez García
 */
public class InvocaWsNetEnvia {
    
    private String versionCFDI = "3.2";

    /**
     * Método para enviar hacia el WebService desarrollado en .NET el CFDI
     * timbrado para que sea presentado ante el SAT por medio de sus
     * Servicios Web y el Comprobante sea totalmente válido ante el SAT
     * @param rfcEmisor  RFC del Emisor del Comprobante
     * @param hashCadenaOriginal   Hash con método SHA-1 de la cadena Original del Comprobante (NO la del Timbre Fiscal)
     * @param numeroCertificadoEmisor Cadena que contiene el numero de certificado CSD del Emisor del Comprobante
     * @param uuid  UUID contenido en el timbre fiscal del comprobante
     * @param fechaHoraTimbre Fecha y Hora exacta del timbrado que corresponde al denotado en el Comprobante en formato XML
     * @param bytesCFDITimbrado Arreglo de bytes que contiene el archivo CFDI ya timbrado por el PAC
     * @return RespuestaEnvio
     */
/*
    public RespuestaEnvio enviar(java.lang.String rfcEmisor,java.lang.String hashCadenaOriginal, java.lang.String numeroCertificadoEmisor, java.lang.String uuid, javax.xml.datatype.XMLGregorianCalendar fechaHoraTimbre, byte[] bytesCFDITimbrado) {

        RespuestaEnvio respEnviarNet = null;

        respEnviarNet = enviarCFDI(rfcEmisor, hashCadenaOriginal, numeroCertificadoEmisor, uuid, fechaHoraTimbre, bytesCFDITimbrado,"3.0");

        return respEnviarNet;
    }
*/
    
    /**
     * Método para enviar hacia el WebService desarrollado en .NET el CFDI 3.2
     * timbrado para que sea presentado ante el SAT por medio de sus
     * Servicios Web y el Comprobante sea totalmente válido ante el SAT
     * @param rfcEmisor  RFC del Emisor del Comprobante
     * @param hashCadenaOriginal   Hash con método SHA-1 de la cadena Original del Comprobante (NO la del Timbre Fiscal)
     * @param numeroCertificadoEmisor Cadena que contiene el numero de certificado CSD del Emisor del Comprobante
     * @param uuid  UUID contenido en el timbre fiscal del comprobante
     * @param fechaHoraTimbre Fecha y Hora exacta del timbrado que corresponde al denotado en el Comprobante en formato XML
     * @param bytesCFDITimbrado Arreglo de bytes que contiene el archivo CFDI ya timbrado por el PAC
     * @return RespuestaEnvio
     */
/*
    public RespuestaEnvio enviar32(java.lang.String rfcEmisor,java.lang.String hashCadenaOriginal, java.lang.String numeroCertificadoEmisor, java.lang.String uuid, javax.xml.datatype.XMLGregorianCalendar fechaHoraTimbre, byte[] bytesCFDITimbrado) {

        RespuestaEnvio respEnviarNet = null;

        respEnviarNet = enviarCFDI(rfcEmisor, hashCadenaOriginal, numeroCertificadoEmisor, uuid, fechaHoraTimbre, bytesCFDITimbrado,"3.2");

        return respEnviarNet;
    }

    private static RespuestaEnvio enviarCFDI(java.lang.String rfcEmisor,java.lang.String hashCadenaOriginal, java.lang.String numeroCertificadoEmisor, java.lang.String uuid, javax.xml.datatype.XMLGregorianCalendar fechaHoraTimbre, byte[] bytesCFDITimbrado, String versionCFDI) {
        EnviaCFDI service = new EnviaCFDI();
        EnviaCFDISoap port = service.getEnviaCFDISoap();
        return port.enviarCFDI(rfcEmisor, hashCadenaOriginal, numeroCertificadoEmisor, uuid, fechaHoraTimbre, bytesCFDITimbrado,versionCFDI);
    }
*/
    
    public WsGenericResp enviarWS(java.lang.String user, java.lang.String userPassword, byte[] certificadoEmisor, byte[] llavePrivadaEmisor, java.lang.String llavePrivadaEmisorPassword, byte[] bytesXmlCFDI) {
        com.tsp.interconecta.ws.InterconectaWsService service = new com.tsp.interconecta.ws.InterconectaWsService();
        com.tsp.interconecta.ws.InterconectaWs port = service.getInterconectaWsPort();
        return port.timbraEnviaCFDIBytes(user, userPassword, certificadoEmisor, llavePrivadaEmisor, llavePrivadaEmisorPassword, bytesXmlCFDI,versionCFDI);
    }
    
    public WsGenericResp enviarWS(java.lang.String user, java.lang.String userPassword, byte[] certificadoEmisor, byte[] llavePrivadaEmisor, java.lang.String llavePrivadaEmisorPassword, java.lang.String xmlCFDI) {
        com.tsp.interconecta.ws.InterconectaWsService service = new com.tsp.interconecta.ws.InterconectaWsService();
        com.tsp.interconecta.ws.InterconectaWs port = service.getInterconectaWsPort();
        return port.timbraEnviaCFDIxp(user, userPassword, certificadoEmisor, llavePrivadaEmisor, llavePrivadaEmisorPassword, xmlCFDI,versionCFDI);
    }
    
    public WsGenericResp enviarWSSPrimario(java.lang.String user, java.lang.String userPassword, byte[] certificadoSPrimario, byte[] llavePrivadaSPrimario, java.lang.String llavePrivadaPassword, byte[] bytesXmlCFDI) {
        /*
        com.tsp.interconecta.ws.InterconectaWsService service = new com.tsp.interconecta.ws.InterconectaWsService();
        com.tsp.interconecta.ws.InterconectaWs port = service.getInterconectaWsPort();
       
        return port.timbraEnviaCFDIBytesSPrimario(user, userPassword, certificadoSPrimario, llavePrivadaSPrimario, llavePrivadaPassword, bytesXmlCFDI,versionCFDI);
        */
        WsGenericResp resp = new WsGenericResp();
        resp.setIsError(true);
        resp.setNumError(902);
        resp.setErrorMessage("Método no implementado.");
        
        return resp;
    }
    
    public WsInsertaClienteResp otorgarAccesoContribuyente(java.lang.String user, java.lang.String userPassword, java.lang.String contribuyenteRFC, java.lang.String contribuyenteRazonSocial) {
        com.tsp.interconecta.ws.InterconectaWsService service = new com.tsp.interconecta.ws.InterconectaWsService();
        com.tsp.interconecta.ws.InterconectaWs port = service.getInterconectaWsPort();
        return port.otorgarAccesoContribuyente(user, userPassword, contribuyenteRFC, contribuyenteRazonSocial);
    }
}
