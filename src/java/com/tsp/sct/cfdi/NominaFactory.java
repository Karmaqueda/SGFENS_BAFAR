/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.cfdi;

import java.math.BigDecimal;
import java.math.BigInteger;
import mx.bigdata.sat.cfdi.schema.v32.Comprobante;
import mx.bigdata.sat.complementos.schema.nomina.Nomina;
import mx.bigdata.sat.complementos.schema.nomina.ObjectFactory;

/**
 *
 * @author Leonardo
 */
public class NominaFactory {
    
    public static Comprobante createComplentoNomina(Comprobante comprobante){
        ObjectFactory of = new ObjectFactory();
        Nomina nomina = of.createNomina();
        nomina.setAntiguedad(5);
        nomina.setBanco(3);
        nomina.setNumDiasPagados(new BigDecimal(15));
        comprobante.getComplemento().getAny().add(nomina);        
        return comprobante;
        
    }
    
}
