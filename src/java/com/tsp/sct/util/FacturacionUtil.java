package com.tsp.sct.util;

/**
 * La clase FacturacionUtil imprime en el reporte el importe con letra.<br/>
 * Fecha de creación: 19/08/2010
 * 
 * @author Factura en Segundos
 */
public class FacturacionUtil {
	         
	/**
	 * Inicializa el importe de tipo int 
	 * @param importe
	 * @return
	 */
	 public static String importeLetra(double importe,String moneda)
	 { 
		String  importeStr  =  FormatUtil.doubleToString(importe);
		if(moneda!=null){
			if(moneda.toLowerCase().equals("mxn")){
			   return obtieneCifras(Integer.parseInt(importeStr.substring(0,importeStr.indexOf("."))))+" PESOS "+(importeStr.substring(importeStr.indexOf(".")+1))+"/100 MN";	
			}else if(moneda.toLowerCase().equals("cop")){
			   return obtieneCifras(Integer.parseInt(importeStr.substring(0,importeStr.indexOf("."))))+" PESOS "+(importeStr.substring(importeStr.indexOf(".")+1))+"/100 COP";	
			}
			   return obtieneCifras(Integer.parseInt(importeStr.substring(0,importeStr.indexOf("."))))+" "+(importeStr.substring(importeStr.indexOf(".")+1))+"/100 "+ moneda;
		}
		  return obtieneCifras(Integer.parseInt(importeStr.substring(0,importeStr.indexOf("."))))+" PESOS "+(importeStr.substring(importeStr.indexOf(".")+1))+"/100 MN";
	 } 
	
	 /**
	  * Obtiene cifras en letra de un numero arabigo
	  * @param _numero
	  * @return
	  */
	private static String obtieneCifras(Integer _numero){ 
                if (_numero < 0) {
                    return "MENOS " + obtieneCifras(_numero * (-1));
                }
                
		switch(_numero){ 
			case 0: return "CERO"; 
			case 1: return "UN";  
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
		} 
	
		if(_numero<20){ 
			return "DIECI"+ obtieneCifras(_numero-10); 
		}if(_numero<30){ 
		return "VEINTI" + obtieneCifras(_numero-20); 
		}if(_numero<100){ 
			return obtieneCifras( (int)(_numero/10)*10 ) + " Y " + obtieneCifras(_numero%10); 
		}if(_numero<200){ 
			return "CIENTO " + obtieneCifras( _numero - 100 ); 
		}if(_numero<1000){ 
			return obtieneCifras( (int)(_numero/100)*100 ) + " " + obtieneCifras(_numero%100); 
		}if(_numero<2000){ 
			return "MIL " + obtieneCifras( _numero % 1000 ); 
		}if(_numero<1000000){ 
			String var=""; 
			var = obtieneCifras((int)(_numero/1000)) + " MIL" ; 
			if(_numero % 1000!=0){ 
			var += " " + obtieneCifras(_numero % 1000); 
			} 
			return var; 
		}if(_numero<2000000){ 
			return "UN MILLON " + obtieneCifras( _numero % 1000000 ); 
		}if(_numero<3000000){ 
			return "DOS MILLONES " + obtieneCifras( _numero % 2000000 ); 
		}if(_numero<4000000){ 
			return "TRES MILLONES " + obtieneCifras( _numero % 3000000 ); 
		}if(_numero<5000000){ 
			return "CUATRO MILLONES " + obtieneCifras( _numero % 4000000 ); 
		}if(_numero<6000000){ 
			return "CINCO MILLONES " + obtieneCifras( _numero % 5000000 ); 
		}if(_numero<7000000){ 
			return "SEIS MILLONES " + obtieneCifras( _numero % 6000000 ); 
		}if(_numero<8000000){ 
			return "SIETE MILLONES " + obtieneCifras( _numero % 7000000 ); 
		}if(_numero<9000000){ 
			return "OCHO MILLONES " + obtieneCifras( _numero % 8000000 ); 
		}if(_numero<10000000){ 
			return "NUEVE MILLONES " + obtieneCifras( _numero % 9000000 ); 
		}if(_numero<11000000){ 
			return "DIEZ MILLONES " + obtieneCifras( _numero % 10000000 ); 
		}if(_numero<12000000){ 
			return "ONCE MILLONES " + obtieneCifras( _numero % 11000000 ); 
		}if(_numero<13000000){ 
			return "DOCE MILLONES " + obtieneCifras( _numero % 12000000 ); 
		}if(_numero<14000000){ 
			return "TRECE MILLONES " + obtieneCifras( _numero % 13000000 ); 
		}if(_numero<15000000){ 
			return "CATORCE MILLONES " + obtieneCifras( _numero % 14000000 ); 
		}if(_numero<16000000){ 
			return "QUINCE MILLONES " + obtieneCifras( _numero % 15000000 ); 
		}if(_numero<17000000){ 
			return "DIECISEIS MILLONES " + obtieneCifras( _numero % 16000000 ); 
		}if(_numero<18000000){ 
			return "DIECISIETE MILLONES " + obtieneCifras( _numero % 17000000 ); 
		}if(_numero<19000000){ 
			return "DIECIOCHO MILLONES " + obtieneCifras( _numero % 18000000 ); 
		}if(_numero<20000000){ 
			return "DIECINUEVE MILLONES " + obtieneCifras( _numero % 19000000 ); 
		}if(_numero<21000000){ 
			return "VEINTE MILLONES " + obtieneCifras( _numero % 20000000 ); 
		}if(_numero<22000000){ 
			return "VEINTIUNO MILLONES " + obtieneCifras( _numero % 21000000 ); 
		}if(_numero<23000000){ 
			return "VEINTIDOS MILLONES " + obtieneCifras( _numero % 22000000 ); 
		}if(_numero<24000000){ 
			return "VEINTITRES MILLONES " + obtieneCifras( _numero % 23000000 ); 
		}if(_numero<25000000){ 
			return "VEINTICUATRO MILLONES " + obtieneCifras( _numero % 24000000 ); 
		}if(_numero<26000000){ 
			return "VEINTICINCO MILLONES " + obtieneCifras( _numero % 25000000 ); 
		}if(_numero<27000000){ 
			return "VEINTISEIS MILLONES " + obtieneCifras( _numero % 26000000 ); 
		}if(_numero<28000000){ 
			return "VEINTISIETE MILLONES " + obtieneCifras( _numero % 27000000 ); 
		}if(_numero<29000000){ 
			return "VEINTIOCHO MILLONES " + obtieneCifras( _numero % 28000000 ); 
		}if(_numero<30000000){ 
			return "VEINTINUEVE MILLONES " + obtieneCifras( _numero % 29000000 ); 
		}if(_numero<31000000){ 
			return "TREINTA MILLONES " + obtieneCifras( _numero % 30000000 ); 
		}   
		return ""; 
	}     
}
