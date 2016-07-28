/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo.test;

import com.tsp.sct.util.DateManage;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ISCesar
 */
public class DateManageTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DateManageTest test = new DateManageTest();
        //test.test1();
        //test.test2();
        //test.test3();
        test.test4();
    }
    
    public void test1(){
        Date fechaIni = DateManage.stringToDate("28/04/2015");
        Date fechaFin = DateManage.stringToDate("31/05/2015");
        List<DateManage.ParDate> fechas = DateManage.obtenPrimerUltimoDiaSemanas(fechaIni, fechaFin);
        for (DateManage.ParDate par : fechas){
            System.out.println(par.getDiaA() + "  -  " + par.getDiaB() );
        }
    }
    
    public void test2(){
        Date fechaBase = DateManage.stringToDate("21/05/2015");
        int semanasAtras = 4;
        int semanasAdelante = 12;
        List<DateManage.ParDate> fechas = DateManage.obtenPrimerUltimoDiaSemanas(fechaBase, semanasAtras, semanasAdelante);
        for (DateManage.ParDate par : fechas){
            System.out.println(par.getDiaA() + "  -  " + par.getDiaB() );
        }
    }
    
    public void test3(){
        String tiempo = "16:57:25";
        Date tiempoD = DateManage.parseTimeSQL(tiempo);
        
        System.out.println("Date: " + tiempoD);
        
        String t2 = DateManage.timeToSQLDateTime(tiempoD);
        System.out.println("Time: " + t2);
        
        
    }
    
    public void test4(){
        String fechaHr = "1/6/2016 8:11:24 AM";
        
        Date date = DateManage.parseDate(fechaHr, "M/d/yyyy h:mm:ss a");
        
        System.out.println(date);
        
        
        String fecha = DateManage.formatDate(date, "M/d/yyyy");
        
        String hr = DateManage.formatDate(date, "h:mm:ss a");
        
        
        System.out.println("fecha: " + fecha);
        System.out.println("hr: " + hr);
    }
    
}
