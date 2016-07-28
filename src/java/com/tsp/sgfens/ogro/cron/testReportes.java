/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ogro.cron;

import com.tsp.sct.bo.ReporteConfigurableAutomatizacionBO;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author leonardo
 */
public class testReportes {
    
    public static void main (String args[]){
        ReporteConfigurableAutomatizacionBO reBO = new ReporteConfigurableAutomatizacionBO(null);
        reBO.verificadorDeEmpresaYReportesAEnviar();
                
        
        ///**PARA VALIDAR EL FUNCIONAMIENTO DEL LISTADO DE DIAS QUE VA A ENVIAR EL REPORTE:
        /*boolean envia = reBO.enviarReporteHoy("8,");        
        if(envia)
            System.out.println("Se va a enviar hoy");
        else
            System.out.println("NOOOOO Se va a enviar hoy");*/
        ///**
        
        
        ///***test de convertidos de fechas
        /*String inicialFec = "30/05/2014";
        String finalFec = "NA";//"15/07/2014";
        
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaIni = null;
        Date fechaFin = null;
        try {
            fechaIni = formatoDelTexto.parse(inicialFec);
            System.out.println("_____ fecha inicial es: "+fechaIni.toString());            
            fechaFin = formatoDelTexto.parse(finalFec);
            System.out.println("_____ fecha final es: "+fechaFin.toString());            
        }catch(Exception e){
            e.printStackTrace();
        }*/
        
        ///***
    }
    
}
