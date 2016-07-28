package com.tsp.sct.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

public class Converter {

	public static String doubleToStringFormatMexico(double valor){
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator('.');
		decimalFormatSymbols.setGroupingSeparator(',');
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", decimalFormatSymbols);
		return decimalFormat.format(valor);
	}
	
	public static String doubleToStringFormatWebService(double valor){
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator('.');
		DecimalFormat decimalFormat = new DecimalFormat("#0.00", decimalFormatSymbols);
		return decimalFormat.format(valor);
	}
	
	public static double roundDouble(double value){
            return Math.round(value * 100.0)/100.0;
        }
        
        public static double meterSecToKmHr(double metersPerSecond){
            return ((metersPerSecond*3600)/1000);
        }
        
        public static double stringToDoubleFormatMexico(String valor) throws ParseException{
            DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
            decimalFormatSymbols.setDecimalSeparator('.');
            decimalFormatSymbols.setGroupingSeparator(',');
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", decimalFormatSymbols);
            return decimalFormat.parse(valor).doubleValue();
	}
}
