/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.interconecta.ws.WsInformaValidacion;
import com.tsp.interconecta.ws.WsValidaResp;
import com.tsp.sct.config.Configuration;
import com.tsp.sct.util.FileManage;
import java.io.File;

/**
 *
 * @author leonardo
 */
public class ValidaXmlBO {
    
    public String hayProblema = "";
    
    public WsValidaResp validar(String rutaXML){
        System.out.println("_______ENTRO A VALIDAR !!!");
        
        Configuration config = new Configuration();
       
        //String versionCFDI="3.2";
        //File fileXmlCFDI = new File("C:\\SystemaDeArchivos\\TSP080724QW6\\validaciones\\CFD_TSP080724QW6_1394065630582.xml");
        File fileXmlCFDI = new File(rutaXML);
        WsValidaResp resp = null;
        try{
            String xmlCFDIContenido = FileManage.getStringFromFile(fileXmlCFDI);
            byte[] bytesXMLContenido = FileManage.getBytesFromFile(fileXmlCFDI);
            
            //extraemos la version del comprobante:
            int indiceComprobante = xmlCFDIContenido.indexOf("Comprobante");
            int datoComprobante = xmlCFDIContenido.indexOf("version=",indiceComprobante);              
            String dato = xmlCFDIContenido.substring(datoComprobante);
            String versionCFDI = xmlCFDIContenido.substring(datoComprobante+9, datoComprobante+12);
            
            resp = validaComprobante(config.getPac_ws_timbrado_user(), config.getPac_ws_timbrado_pass(), xmlCFDIContenido, bytesXMLContenido, versionCFDI);
            
            /*System.out.println("Error proceso? : " + resp.isIsError());
            System.out.println("Num Error : " + resp.getNumError());
            System.out.println("Error : " + resp.getErrorMessage());
            
            System.out.println("Comprobante Valido? : " + resp.isComprobanteValido());
            System.out.println("Version : " + resp.getVersionComprobante());
            System.out.println("Tipo Comprobante : " + resp.getTipoComprobante());
            System.out.println("Cadena original : " + resp.getCadenaOriginalComprobante());
            System.out.println("Sello emisor Valido? : " + resp.isSelloEmisorValido());
            System.out.println("Timbre Fiscal : " + resp.getTimbreFiscalDigital());
            
            System.out.println("\n======== INFO =======");
            for (WsInformaValidacion item : resp.getListaInformacion()){
                System.out.println("(" + item.getCodigo() + ") " + item.getNombre() + " : " + item.getValor());
            }
            
            System.out.println("\n======== ADVERTENCIAS =======");
            for (WsInformaValidacion item : resp.getListaAdvertencias()){
                System.out.println("(" + item.getCodigo() + ") " + item.getNombre() + " : " + item.getValor());
            }
            
            System.out.println("\n======== ERRORES =======");
            for (WsInformaValidacion item : resp.getListaErrores()){
                System.out.println("(" + item.getCodigo() + ") " + item.getNombre() + " : " + item.getValor());
            }*/
            hayProblema = "";
        }catch(Exception ex){
            System.out.println("Ocurrio un error " + ex);
            hayProblema = ex.toString();
        }
        
        return resp;
    }
    
    
     private static WsValidaResp validaComprobante(java.lang.String user, java.lang.String userPassword, java.lang.String xmlComprobante, byte[] bytesXmlComprobante, java.lang.String versionComprobante) {
        com.tsp.interconecta.ws.InterconectaWsService service = new com.tsp.interconecta.ws.InterconectaWsService();
        com.tsp.interconecta.ws.InterconectaWs port = service.getInterconectaWsPort();
        return port.validaComprobante(user, userPassword, xmlComprobante, bytesXmlComprobante, versionComprobante);
    }
    
}
