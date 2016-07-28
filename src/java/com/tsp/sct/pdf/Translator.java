package com.tsp.sct.pdf;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author ISC César Ulises Martínez García
 */
public class Translator {

        private Integer counter=0;
        private String value="";
        private String simboloMoneda;
        private String nombreDeMoneda;
        /**
         * Representacion ISO4217 de la moneda
         */
        private String sufijoMoneda;

        public Translator(){
            simboloMoneda = "$";
            nombreDeMoneda = "PESOS";
            sufijoMoneda = "M.N.";
        }
        public String getStringOfNumber(Integer $num){
            this.counter = $num;
            return doThings($num);
        }

        public void setNombreMoneda(String nombre){
            nombreDeMoneda = nombre;
            if (nombre.trim().toUpperCase().contains("PESO") 
                    || nombre.trim().toUpperCase().contains("MXN")
                    || nombre.trim().toUpperCase().contains("NACIONAL")
                    || nombre.trim().toUpperCase().contains("M.N.")
                ){
                nombreDeMoneda = "PESOS";
                simboloMoneda = "$";
                sufijoMoneda = "M.N.";
            }
            if (nombre.trim().toUpperCase().contains("DOLAR") 
                    || nombre.trim().toUpperCase().contains("USD")
                    || nombre.trim().toUpperCase().contains("DOLLAR")
                    || nombre.trim().toUpperCase().contains("DÓLAR")
                    || nombre.trim().toUpperCase().contains("DÓLL")
                ){
                nombreDeMoneda = "DOLARES";
                simboloMoneda = "$";
                sufijoMoneda = "USD";
            }
            if (nombre.trim().toUpperCase().contains("EUR")){
                nombreDeMoneda = "EUROS";
                simboloMoneda = "€";
                sufijoMoneda = "EUR";
            }
        }
        
         /** Con formato centavos/100MN **/
        public String getStringOfNumber(BigDecimal $num){
            BigDecimal aux = $num.setScale(2, RoundingMode.HALF_UP);
            //float auxF = aux.floatValue();
            int _counter = aux.intValue();
            //float resto = auxF - _counter; //Almaceno la parte decimal
            float resto = aux.remainder(BigDecimal.ONE).floatValue();
            //Redondeo y convierto a entero puedo tener problemas
            //int fraccion = Math.round(resto * 100);
            int fraccion = Math.round(resto * 100);

            return "(" + doThings(_counter) + " " + nombreDeMoneda + " CON " + (fraccion>9?""+fraccion:"0"+fraccion) + "/100 "+sufijoMoneda+")";
        }
        

        /** Con formato centavos/100MN **/
        public String getStringOfNumber(float $num){
            int _counter = (int) $num;
            float resto = $num - _counter; //Almaceno la parte decimal
            //Redondeo y convierto a entero puedo tener problemas
            int fraccion = Math.round(resto * 100);

            return "(" + doThings(_counter) + " " + nombreDeMoneda + " CON " + (fraccion>9?""+fraccion:"0"+fraccion) + "/100 "+sufijoMoneda+")";
        }
        
        /** Con formato centavos/100MN **/
        public String getStringOfNumber(double $num){
            int _counter = (int) $num;
            double resto = $num - _counter; //Almaceno la parte decimal
            //Redondeo y convierto a entero puedo tener problemas
            int fraccion = (int)Math.round(resto * 100);

            return "(" + doThings(_counter) + " " + nombreDeMoneda + " CON " + (fraccion>9?""+fraccion:"0"+fraccion) + "/100 "+sufijoMoneda+")";
        }

        private String doThings(Integer _counter){
            //Limite
//            if(_counter >5000000)
//                return "CINCO MILLONES";

            switch(_counter){
                case 0: return "CERO";
                case 1: return "UN"; //UNO
                case 2: return "DOS";
                case 3: return "TRES";
                case 4: return "CUATRO";
                case 5: return "CINCO";
                case 6: return "SEIS";
                case 7: return "SIETE";
                case 8: return "OCHO";
                case 9: return "NUEVE";
                case 10: return "DIEZ";
                case 11: return "ONCE";
                case 12: return "DOCE";
                case 13: return "TRECE";
                case 14: return "CATORCE";
                case 15: return "QUINCE";
                case 20: return "VEINTE";
                case 30: return "TREINTA";
                case 40: return "CUARENTA";
                case 50: return "CINCUENTA";
                case 60: return "SESENTA";
                case 70: return "SETENTA";
                case 80: return "OCHENTA";
                case 90: return "NOVENTA";
                case 100: return "CIEN";

                case 200: return "DOSCIENTOS";
                case 300: return "TRESCIENTOS";
                case 400: return "CUATROCIENTOS";
                case 500: return "QUINIENTOS";
                case 600: return "SEISCIENTOS";
                case 700: return "SETECIENTOS";
                case 800: return "OCHOCIENTOS";
                case 900: return "NOVECIENTOS";

                case 1000: return "MIL";

                case 1000000: return "UN MILLON";
                case 2000000: return "DOS MILLONES";
                case 3000000: return "TRES MILLONES";
                case 4000000: return "CUATRO MILLONES";
                case 5000000: return "CINCO MILLONES";
                case 6000000: return "SEIS MILLONES";
                case 7000000: return "SIETE MILLONES";
                case 8000000: return "OCHO MILLONES";
                case 9000000: return "NUEVE MILLONES";
                case 10000000: return "DIEZ MILLONES";
            }
            if(_counter<20){
                return "DIECI"+ doThings(_counter-10);
            }
            if(_counter<30){
                return "VEINTI" + doThings(_counter-20);
            }
            if(_counter<100){
                return doThings( (int)(_counter/10)*10 ) + " Y " + doThings(_counter%10);
            }
            if(_counter<200){
                return "CIENTO " + doThings( _counter - 100 );
            }
            if(_counter<1000){
                return doThings( (int)(_counter/100)*100 ) + " " + doThings(_counter%100);
            }
            if(_counter<2000){
                return "MIL " + doThings( _counter % 1000 );
            }
            if(_counter<1000000){
                String var="";
                var = doThings((int)(_counter/1000)) + " MIL" ;
                if(_counter % 1000!=0){
                    var += " " + doThings(_counter % 1000);
                }
                return var;
            }
            if(_counter<2000000){
                return "UN MILLON " + doThings( _counter % 1000000 );
            }
            if(_counter<3000000){
                return "DOS MILLONES " + doThings( _counter % 1000000 );
            }
            if(_counter<4000000){
                return "TRES MILLONES " + doThings( _counter % 1000000 );
            }
            if(_counter<5000000){
                return "CUATRO MILLONES " + doThings( _counter % 1000000 );
            }
            if(_counter<6000000){
                return "CINCO MILLONES " + doThings( _counter % 1000000 );
            }
            if(_counter<7000000){
                return "SEIS MILLONES " + doThings( _counter % 1000000 );
            }
            if(_counter<8000000){
                return "SIETE MILLONES " + doThings( _counter % 1000000 );
            }
            if(_counter<9000000){
                return "OCHO MILLONES " + doThings( _counter % 1000000 );
            }
            if(_counter<10000000){
                return "NUEVE MILLONES " + doThings( _counter % 1000000 );
            }
            if(_counter<11000000){
                return "DIEZ MILLONES " + doThings( _counter % 1000000 );
            }
            return "";
        }

        

}
