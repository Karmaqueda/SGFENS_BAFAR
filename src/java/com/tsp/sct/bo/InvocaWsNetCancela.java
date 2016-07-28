/*
 * To change this template, choose DateManage | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.interconecta.ws.WsGenericResp;
import com.tsp.interconecta.ws.dotnet.ArrayOfString;
import com.tsp.interconecta.ws.dotnet.EnviaCFDI;
import com.tsp.interconecta.ws.dotnet.EnviaCFDISoap;
import com.tsp.interconecta.ws.dotnet.RespuestaCancelacion;
import com.tsp.sct.util.DateManage;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author ISC César Ulises Martínez García
 */
public class InvocaWsNetCancela {

    /**
     * Constrcutor vacío
     */
    public InvocaWsNetCancela() {
    }

    /**
     * M&eacute;todo que realiza la cancelaci&oacute;n de un XML realizando la
     * implementaci&oacute;n del WebService de .NET
     * @param rfcEmisor Es el rfc del Emisor de las facturas a cancelar.
     * @param fechaHora fecha y hora en que se realiza la llamada a cancelación, será reemplazada por el sistema del SAT
     * @param listaUUID Es la lista de UUID a cancelar y que pertenecen al mismo Emisor
     * @param certificadoCSD  Arreglo de bytes del archivo .cer del Emisor que esta solicitando la cancelaci&oacute;n
     * @param privateKeyBase64  Es la llave privada .key del emisor codificada en base64 (Ver clase ...util.UtilSecurity.java)
     * @return RespuestaCancelacion
     */
    /*
    public RespuestaCancelacion cancelar(String rfcEmisor, java.util.Date fechaHora, ArrayOfString listaUUID, byte[] certificadoCSD, String privateKeyBase64) throws DatatypeConfigurationException {
        RespuestaCancelacion respCancelaNet = null;

        XMLGregorianCalendar fechaHoraCancela = DateManage.dateToXMLGregorianCalendar(fechaHora);

        respCancelaNet = cancelarCFDICerKey(rfcEmisor, fechaHoraCancela, listaUUID, certificadoCSD, privateKeyBase64);


        return respCancelaNet;
    }

    private static RespuestaCancelacion cancelarCFDICerKey(java.lang.String rfcEmisor, javax.xml.datatype.XMLGregorianCalendar fechaHoraTimbre, com.tsp.interconecta.ws.dotnet.ArrayOfString uuid, byte[] bytesCer, java.lang.String keyBase64) {
        EnviaCFDI service = new EnviaCFDI();
        EnviaCFDISoap port = service.getEnviaCFDISoap();
        return port.cancelarCFDICerKey(rfcEmisor, fechaHoraTimbre, uuid, bytesCer, keyBase64);
    }
    
     public RespuestaCancelacion cancelarCFDIPeticionPreFormada(byte[] bytesXMLPeticionCancelacion) {
        EnviaCFDI service = new EnviaCFDI();
        EnviaCFDISoap port = service.getEnviaCFDISoap();
        return port.cancelarCFDIPeticionPreFormada(bytesXMLPeticionCancelacion);
    }
    * 
    */
    
    
    public WsGenericResp cancelaCFDI32(java.lang.String user, java.lang.String userPassword, byte[] certificadoEmisor, byte[] llavePrivadaEmisor, java.lang.String llavePrivadaEmisorPassword, java.lang.String xmlCFDI) {
        com.tsp.interconecta.ws.InterconectaWsService service = new com.tsp.interconecta.ws.InterconectaWsService();
        com.tsp.interconecta.ws.InterconectaWs port = service.getInterconectaWsPort();
        return port.cancelaCFDI32(user, userPassword, certificadoEmisor, llavePrivadaEmisor, llavePrivadaEmisorPassword, xmlCFDI);
    }

    public WsGenericResp cancelaCFDIxp(java.lang.String user, java.lang.String userPassword, java.lang.String xmlPeticionCancelacionSellada) {
        com.tsp.interconecta.ws.InterconectaWsService service = new com.tsp.interconecta.ws.InterconectaWsService();
        com.tsp.interconecta.ws.InterconectaWs port = service.getInterconectaWsPort();
        return port.cancelaCFDIxp(user, userPassword, xmlPeticionCancelacionSellada);
    }
}
