/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.cr;

import com.tsp.sct.util.Converter;
import com.tsp.sct.util.StringManage;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author ISCesar
 */
public class CrUtilCalculos {

    /**
     * Calcula la cuota a pagar en cada vencimiento.
     * @param monto Monto del prestamo
     * @param interesAnual Interes anual con IVA
     * @param plazo Plazo (numero de veces a pagar)
     * @return 
     */
    public static double calcCuota(double monto, double interesAnual, int plazo) {
        // monto del prestamo
        double p = monto;
        // interes anual con IVA
        double iy = interesAnual;
        // numero de cuotas
        double nm = plazo;
        
        //interes mensual
        double im = (iy / 12) / 100;
        //cuota
        double mp;

        mp = ( p * ( im * Math.pow(1 + im, (double) nm) ) ) / (Math.pow(1 + im, (double) nm) - 1);
        
        mp = Converter.roundDouble(mp);

        return mp;
    }
    
    /**
     * 
     * @param monto Monto del prestamo
     * @param interesAnual Interes anual con IVA
     * @param plazo Plazo (numero de veces a pagar)
     * @param plazoEquivalenciaMes Mensual = 1, Quincenal = 2, Catorcenal = 2, Semanal = 4
     * @return 
     */
    public static double calcCuotaMensual(double monto, double interesAnual, int plazo, int plazoEquivalenciaMes) {
        //cuota
        double mp = calcCuota(monto, interesAnual, plazo);
        
        // cuota * equivalenciaMes 
        double cuotaMensual = mp * plazoEquivalenciaMes;

        return cuotaMensual;
    }
    
    public static Date calcFechaFinPeriodo(Date fechaInicio, int plazo, TipoPlazo tipoPlazo){
        Date fechaFin = null;
        
        try{
            Calendar calFin = Calendar.getInstance();
            calFin.setTime(fechaInicio);
            switch (tipoPlazo){
                case SEMANAL:
                    calFin.add(Calendar.WEEK_OF_MONTH, plazo);
                    break;
                case MENSUAL:
                    calFin.add(Calendar.MONTH, plazo);
                    break;
                case DECENAL:
                    calFin.add(Calendar.DAY_OF_MONTH, plazo * tipoPlazo.getNumDias());
                    break;
                case CATORCENAL:
                    calFin.add(Calendar.DAY_OF_MONTH, plazo * tipoPlazo.getNumDias());
                    break;
                case QUINCENAL:
                    calFin.add(Calendar.DAY_OF_MONTH, plazo * tipoPlazo.getNumDias());
                    break;
            }
            
            fechaFin = calFin.getTime();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return fechaFin;
    }
    
    public static Date calcUltimoDiaMes(Date fecha){
        Date ultimoDiaMes = null;
        
        try{
            Calendar c = Calendar.getInstance();
            c.setTime(fecha);
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            
            ultimoDiaMes = c.getTime();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return ultimoDiaMes;
    }
    
    public static String calculaCodigoBarrasOxxoBafar(String bp, String noContrato, BigDecimal montoBigD){
        String codigoBarras = "";
        
        try{
            String prefijoOxxoBafar = "90";
            String referencia;
            String monto;
            String digitoVerificador;
            
            // calculamos cadena referencia
            // Referencia, longitud: 23, posición en cadena 3 a la 25
            {
                // No. de BP (Business Partner), longitud: 10, posición en cadena: 3 a la 12
                String bpFormat = StringManage.getExactString(bp, 10, '0', StringManage.FILL_DIRECTION_LEFT);
                // No. de Contrato, longitud: 13, posición en cadena: 13 a la 25
                String contratoFormat = StringManage.getExactString(noContrato, 13, '0', StringManage.FILL_DIRECTION_LEFT);

                // referencia = bp + no contrato
                referencia = bpFormat + contratoFormat;
            }
            
            // calculamos cadena monto
            {
                // redondeamos a 2 decimales
                montoBigD = montoBigD.setScale(2, BigDecimal.ROUND_HALF_UP);
                monto = montoBigD.toString().replace(".", "");
                // Monto, longitud: 6, posición en cadena: 26 a la 31
                monto = StringManage.getExactString(monto, 6, '0', StringManage.FILL_DIRECTION_LEFT);
            }
            
            // calculamos Digito Verificador (DV)
            {
                String cadenaCalcularDV = prefijoOxxoBafar + referencia + monto;
                digitoVerificador = "" + calcularDigitoVerificadorOxxo137(cadenaCalcularDV);
            }
            
            codigoBarras = prefijoOxxoBafar + referencia + monto + digitoVerificador;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return codigoBarras;
    }
    
    /**
     * Calcula el digito verificador de una cadena
     * utilizando el algoritmo de Oxxo 1-3-7
     * 1. Se multiplica el primer número del código por 1, el siguiente por 3, el siguiente por 7, el
     *    siguiente por 1, el siguiente por 3, el siguiente por 7 y así sucesivamente.
     * 2. Se suman los resultados.
     * 3. Se obtiene el residuo de dividir la suma entre nueve.
     * 4. Se le suma 1 al residuo y el resultado es el DV.
     * @param cadenaCalculoDV
     * @return digito verificador
     */
    private static int calcularDigitoVerificadorOxxo137(String cadenaCalculoDV){
        int dv = -1;
        
        try{
            int divisorAlgoritmo = 9;
            int suma = 0;
            int residuo = 0;
            int i = 1;
            for (char c : cadenaCalculoDV.toCharArray()){
                int cInt = Integer.parseInt(""+c);
                suma += (cInt * i) ;
                
                switch (i){
                    case 1:
                        i = 3;
                        break;
                    case 3:
                        i = 7;
                        break;
                    case 7:
                        i = 1;
                        break;
                }
            }
            
            residuo = suma % divisorAlgoritmo;
            
            dv = residuo + 1;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return dv;
    }
    
    public static enum TipoPlazo{   
        SEMANAL (7, 4, "001", "07", "Semanal", "Semanas"),
        DECENAL (10, 3, "002", "10", "Decenal", "Decenas"),
        CATORCENAL (14, 2, "", "14", "Catorcenal", "Catorcenas"  ),
        QUINCENAL(15, 2, "003", "15", "Quincenal", "Quincenas"),
        MENSUAL(31, 1, "004", "30", "Mensual", "Meses");
        
        private final int numDias;
        private final int equivalenciaMes;
        private final String valorVarFormularioOcupacion;
        private final String valorVarWSCreaInterlocu;
        private final String nombre;
        private final String unidad;

        TipoPlazo(int numDias, int equivalenciaMes, String valorVarFormularioOcupacion, 
                String valorVarWSCreaInterlocu, String nombre, String unidad) {
            this.numDias = numDias;
            this.equivalenciaMes = equivalenciaMes;
            this.valorVarFormularioOcupacion = valorVarFormularioOcupacion;
            this.valorVarWSCreaInterlocu = valorVarWSCreaInterlocu;
            this.nombre = nombre;
            this.unidad = unidad;
        }

        public int getNumDias() {
            return numDias;
        }

        public int getEquivalenciaMes() {
            return equivalenciaMes;
        }

        public String getValorVarFormularioOcupacion() {
            return valorVarFormularioOcupacion;
        }

        public String getValorVarWSCreaInterlocu() {
            return valorVarWSCreaInterlocu;
        }

        public String getNombre() {
            return nombre;
        }

        public String getUnidad() {
            return unidad;
        }

    }

}
