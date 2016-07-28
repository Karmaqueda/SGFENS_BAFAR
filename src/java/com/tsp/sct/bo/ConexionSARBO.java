/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.workflow.ws.ConsultaComprobanteResponse;
import com.tsp.workflow.ws.ConsultaDatosGralProveedorResponse;
import com.tsp.workflow.ws.ConsultaFlujoComprobanteResponse;
import com.tsp.workflow.ws.RegistraComprobanteResponse;
import com.tsp.workflow.ws.WsItemArea;

/**
 *
 * @author ISCesar poseidon24@hotmail.com
 * @since 19/03/2015 06:55:15 PM
 */
public class ConexionSARBO {

    public static ConsultaDatosGralProveedorResponse getDatosGeneralProveedor(com.tsp.workflow.ws.AccesoRequest accesoRequest) {
        com.tsp.workflow.ws.SarWS_Service service = new com.tsp.workflow.ws.SarWS_Service();
        com.tsp.workflow.ws.SarWS port = service.getSarWSPort();
        return port.getDatosGeneralProveedor(accesoRequest);
    }
    
    public static RegistraComprobanteResponse setComprobante(com.tsp.workflow.ws.AccesoRequest accesoRequest, com.tsp.workflow.ws.RegistraComprobanteRequest registraComprobanteRequest) {
        com.tsp.workflow.ws.SarWS_Service service = new com.tsp.workflow.ws.SarWS_Service();
        com.tsp.workflow.ws.SarWS port = service.getSarWSPort();
        return port.setComprobante(accesoRequest, registraComprobanteRequest);
    }
    
    public static RegistraComprobanteResponse editComprobante(com.tsp.workflow.ws.AccesoRequest accesoRequest, com.tsp.workflow.ws.EditaComprobanteRequest editaComprobanteRequest) {
        com.tsp.workflow.ws.SarWS_Service service = new com.tsp.workflow.ws.SarWS_Service();
        com.tsp.workflow.ws.SarWS port = service.getSarWSPort();
        return port.editComprobante(accesoRequest, editaComprobanteRequest);
    }
    
    public static ConsultaComprobanteResponse getComprobantes(com.tsp.workflow.ws.AccesoRequest accesoRequest, com.tsp.workflow.ws.ConsultaComprobanteRequest consultaComprobanteRequest) {
        com.tsp.workflow.ws.SarWS_Service service = new com.tsp.workflow.ws.SarWS_Service();
        com.tsp.workflow.ws.SarWS port = service.getSarWSPort();
        return port.getComprobantes(accesoRequest, consultaComprobanteRequest);
    }
    
    public static ConsultaFlujoComprobanteResponse getFlujoComprobantes(com.tsp.workflow.ws.AccesoRequest accesoRequest, com.tsp.workflow.ws.ConsultaComprobanteRequest consultaComprobanteRequest) {
        com.tsp.workflow.ws.SarWS_Service service = new com.tsp.workflow.ws.SarWS_Service();
        com.tsp.workflow.ws.SarWS port = service.getSarWSPort();
        return port.getFlujoComprobantes(accesoRequest, consultaComprobanteRequest);
    }
    
    public static String parseHTMLDatosGralProveedorResponse(ConsultaDatosGralProveedorResponse response){
        String data = "";
        
        try{
            data += "<br/>" + "----Datos Proveedor:";
            data += "<br/>" + ("SAR ID: " + response.getWsItemProveedor().getId());
            data += ", " + ("RFC: " + response.getWsItemProveedor().getRfc());
            data += ", " + ("Razon Social: " + response.getWsItemProveedor().getRazonSocial());
            //data += "<br/>" + ("Estatus: " + response.getWsItemProveedor().getIdEstatus());
            data += ", " + ("Requerir OC: " + (response.getWsItemProveedor().isRequerirOC()?"SI":"NO"));
            
            data += "<br/>" + ("----Datos Cliente Receptor:");
            data += "<br/>" + ("SAR ID: " + response.getWsItemCliente().getId());
            data += ", " + ("RFC: " + response.getWsItemCliente().getRfc());
            data += ", " + ("Razon Social: " + response.getWsItemCliente().getRazonSocial());
            data += ", " + ("Relacionar Prox X Area: " + (response.getWsItemCliente().isRelacionCodigoProvXArea()?"SI":"NO"));
            
            data += "<br/>" + ("----Datos Areas de entrega:");
            for (WsItemArea area : response.getListaWsItemArea()){
                data += "<br/>" + ("SAR ID: " + area.getId());
                data += ", " + ("Nombre: " + area.getNombre());
                
                if (response.getWsItemCliente().isRelacionCodigoProvXArea())
                    data += ", " + ("Codigo Prov X Area: " + area.getCodigoProvXArea());
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return data;
    }
}