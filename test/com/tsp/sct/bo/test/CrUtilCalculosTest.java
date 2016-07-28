/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo.test;

import com.tsp.sct.cr.CrUtilCalculos;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ISCesar
 */
public class CrUtilCalculosTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CrUtilCalculosTest test = new CrUtilCalculosTest();
        
        //test.test1();
        //test.test2();
        //test.test3();
        //test.test4();
        
        test.testCodigoBarrasOxxo();
    }
    
    private void test1(){
        // 60 meses, 10 mil pesos, interes 10% anual
        double monto = 10000;
        double interesAnual = 10;
        int plazo = 60;
        int plazoEquivalenciaMes = 1;
        
        System.out.println("Cuota segun plazo:" + CrUtilCalculos.calcCuota(monto, interesAnual, plazo));
        
        System.out.println("Cuota mensual:" + CrUtilCalculos.calcCuotaMensual(monto, interesAnual,plazo, plazoEquivalenciaMes));
        
        System.out.println("-------");
        
    }
    
    private void test2(){
        // 6 meses, 30 mil pesos, interes 32.48% anual
        double monto = 30000;
        double interesAnual = 32.48;
        int plazo = 6;
        int plazoEquivalenciaMes = 1;
        
        System.out.println("Cuota segun plazo:" + CrUtilCalculos.calcCuota(monto, interesAnual, plazo));
        System.out.println("Cuota mensual:" + CrUtilCalculos.calcCuotaMensual(monto, interesAnual,plazo, plazoEquivalenciaMes));
        System.out.println("-------");
    }
    
    private void test3(){
        // 16 semanas (6 meses), 10 mil pesos, interes 38.28% anual
        double monto = 10000;
        double interesAnual = 38.28;
        int plazo = 16;
        int plazoEquivalenciaMes = 4; //un mes tiene 4 semanas
        
        System.out.println("Cuota segun plazo:" + CrUtilCalculos.calcCuota(monto, interesAnual, plazo));
        System.out.println("Cuota mensual:" + CrUtilCalculos.calcCuotaMensual(monto, interesAnual,plazo, plazoEquivalenciaMes));
        System.out.println("-------");
    }

    private void test4() {
        Date fechaInicio = new Date() ;
        int plazo = 1;
        CrUtilCalculos.TipoPlazo tipoPlazo = CrUtilCalculos.TipoPlazo.QUINCENAL;
        
        System.out.println(CrUtilCalculos.calcFechaFinPeriodo(fechaInicio, plazo, tipoPlazo));
    }
    
    private void testCodigoBarrasOxxo(){
        String bp = "4512";
        String noContrato = "99678";
        BigDecimal monto = new BigDecimal(509.56);
        
        String codigoBarras = CrUtilCalculos.calculaCodigoBarrasOxxoBafar(bp, noContrato, monto);
        System.out.println("\nCodigo de barras generado: \n" + codigoBarras);
        
        
        bp = "0240000324";
        noContrato = "900000170";
        monto = new BigDecimal(486.17);
        String codigoBarras2 = CrUtilCalculos.calculaCodigoBarrasOxxoBafar(bp, noContrato, monto);
        System.out.println("\nCodigo de barras generado: \n" + codigoBarras2);
        
        
        bp = "0440000389";
        noContrato = "900000254";
        monto = new BigDecimal(2100.458744);
        String codigoBarras3 = CrUtilCalculos.calculaCodigoBarrasOxxoBafar(bp, noContrato, monto);
        System.out.println("\nCodigo de barras generado: \n" + codigoBarras3);
        
    }
    
}
