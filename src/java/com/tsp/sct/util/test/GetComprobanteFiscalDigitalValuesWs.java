/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.util.test;

import com.tsp.sct.cfdi.Cfd32BO;
import com.tsp.sgfens.ws.bo.ComprobanteFiscalDigitalBO;
import com.tsp.sgfens.ws.response.comprobante.WSComprobanteFiscalDigital;
import com.tsp.sgfens.ws.response.comprobante.WSTimbreFiscalDigital;
import java.io.File;
import java.sql.Connection;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 16-jun-2013 
 */
public class GetComprobanteFiscalDigitalValuesWs {
    
    private Connection conn = null;
    
    public static void main(String[] args) throws Exception{
        
        GetComprobanteFiscalDigitalValuesWs test = new GetComprobanteFiscalDigitalValuesWs();
        
        File xmlFile = new File("C:/SystemaDeArchivos/TSP080724QW6/cfd/emitidos/facturas/XAXX010101000/CFD_TSP080724QW6_1371338516844.xml");
        
        test.test1(xmlFile);
    }
    
    private void test1(File XMLFile) throws Exception{
        Cfd32BO cfd32BO = new Cfd32BO(XMLFile);
        
        System.out.println(cfd32BO.getTimbreFiscalDigital().getVersion());
        System.out.println(cfd32BO.getTimbreFiscalDigital().getUUID());        
        System.out.println(cfd32BO.gettFDv1().getCadenaOriginal());
        System.out.println(cfd32BO.getComprobanteFiscal().getVersion());
        System.out.println(cfd32BO.getComprobanteFiscal().getEmisor().getRfc());
        System.out.println(cfd32BO.getCfd().getCadenaOriginal());
        System.out.println("");
        
        ComprobanteFiscalDigitalBO comprobanteFiscalDigitalBO = new ComprobanteFiscalDigitalBO(this.conn);
        WSTimbreFiscalDigital wSTimbreFiscalDigital = comprobanteFiscalDigitalBO.transformaTimbreAResponseType(cfd32BO.getTimbreFiscalDigital(), cfd32BO.gettFDv1());
        WSComprobanteFiscalDigital wSComprobanteFiscalDigital = comprobanteFiscalDigitalBO.transformaAWSComprobanteFiscalDigital(cfd32BO.getComprobanteFiscal(), cfd32BO.getCfd());
        
        System.out.println("----------------------------------------");
        System.out.println(wSTimbreFiscalDigital.getVersion());
        System.out.println(wSTimbreFiscalDigital.getUUID());
        System.out.println(wSTimbreFiscalDigital.getTimbreFiscalDigitalCadenaOriginal());
        System.out.println(wSComprobanteFiscalDigital.getVersion());
        System.out.println(wSComprobanteFiscalDigital.getEmisor().getRfc());
        System.out.println(wSComprobanteFiscalDigital.getComprobanteCadenaOriginal());
        for (WSComprobanteFiscalDigital.Conceptos.Concepto wsconcepto : wSComprobanteFiscalDigital.getConceptos().getConcepto()){
            System.out.println("\t" + wsconcepto.getDescripcion() + "  : "  + wsconcepto.getImporte());
        }
        for (WSComprobanteFiscalDigital.Impuestos.Traslados.Traslado wsTraslado : wSComprobanteFiscalDigital.getImpuestos().getTraslados().getTraslado()){
            System.out.println("\t" + wsTraslado.getImpuesto() + "  : "  + wsTraslado.getImporte());
        }
    }

}
