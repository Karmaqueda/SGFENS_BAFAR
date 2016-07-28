package com.tsp.sct.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.StringTokenizer;
import org.apache.commons.io.IOUtils;
import com.sun.org.apache.xerces.internal.impl.dtd.models.DFAContentModel;

public class FormatUtil {

	public static String doubleToString(double numero) {
		DecimalFormat formateador = new DecimalFormat("#0.00;-#0.00");
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		formateador.setDecimalFormatSymbols(dfs);
		return formateador.format(numero);
	}
	
	public static String doubleToStringPuntoComas(double numero){		
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		simbolos.setGroupingSeparator(',');
		//DecimalFormat formateador = new DecimalFormat("###,###,###,###.000",simbolos);
		//DecimalFormat formateador = new DecimalFormat("#,##0.00;(#,##0.00)",simbolos);
		DecimalFormat formateador = new DecimalFormat("#,##0.00;-#,##0.00",simbolos);
		return formateador.format(numero);
	}

	public static String formatoCadenaOriginal(String cadena) {		
		if (cadena == null || "".equals(cadena)) {			
			return "";
		}
		if(cadena != null){
			cadena = cadena.trim();
			if("".equals(cadena)){
				return "";
			}
		}
		cadena = cadena.replaceAll(String.valueOf('\n'), " ");
		cadena = cadena.replaceAll(String.valueOf('\u0009'), " ");
		cadena = cadena.replaceAll(String.valueOf('\r'), " ");
		while (cadena.contains("  ")) {
			cadena = cadena.replace("  ", " ");
		}
		cadena = cadena.trim();
		return cadena + "|";
	}

	public static String formatoSelloDigitalXML(String selloDigital) {
		if (selloDigital == null || "".equals(selloDigital)) {
			return "";
		}
		selloDigital = selloDigital.replaceAll(String.valueOf('\n'), "");
		selloDigital = selloDigital.replaceAll(String.valueOf('\r'), "");
		selloDigital.trim();
		return selloDigital;
	}

	public static String formatoCadenaOriginal(int cadena) {
		return formatoCadenaOriginal(String.valueOf(cadena));
	}

	public static String formatoCadenaOriginal(double cadena) {
		if (cadena != 0) {
			return formatoCadenaOriginal(doubleToString(cadena));
		}
		return "";		
	}
	
	public static String formatoCadenaOriginalConcepto(double cadena) {
		//if (cadena != 0) {
			return formatoCadenaOriginal(doubleToString(cadena));
		//}
		//return "";		
	}
	
	public static String formatoCadenaOriginalImpuesto(double cadena) {
		//if (cadena != 0) {
			return formatoCadenaOriginal(doubleToString(cadena));
		//}
		//return "";		
	}
	
	public static String formatoCadenaOriginalTasaCero(double cadena) {
		return formatoCadenaOriginal(doubleToString(cadena));		
	}
        
	public static String formatoNumero(int numero, int digitos) {
		String respuesta = String.valueOf(numero);
		for (int i = 0; i < digitos; i++) {
			if (respuesta.length() < digitos) {
				respuesta = "0" + respuesta;
			}
		}
		return respuesta;
	}

	/**
	 * Metodo que convierte un input stream con la llave privada o publica a un
	 * array de bytes
	 * 
	 * @param is
	 *            InputSteam con la clave privada
	 * @return Arreglo de bytes con la clave privada
	 */
	public static byte[] getBytes(InputStream is) {
		try {
			return IOUtils.toByteArray(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String formatRounded(double numero, int decimales) {
		String decimalesStr = "";
		for (int i = 0; i < decimales; i++) {
			decimalesStr += "#";
		}
		DecimalFormat format = new DecimalFormat("###." + decimalesStr);
		return format.format(numero);
	}


	public static String noCertificadoToString(BigInteger noCertificadoHex) {
		String hexa = noCertificadoHex.toString(16);
		String resultado = "";
		while (hexa.length() > 0) {
			resultado += String.valueOf((char) Integer.parseInt(hexa.substring(0, 2), 16));
			hexa = hexa.substring(2, hexa.length());
		}
		return resultado;
	}
	
	public static String formatoCadenaOriginalSAT(String cadena) {
		if (cadena != null) {
			String cadOrFormat = "";
			cadOrFormat = "|";
			StringTokenizer st = new StringTokenizer(cadena, "|");
			while (st.hasMoreTokens()) {
				cadOrFormat += "|" + st.nextToken();
			}
			return cadOrFormat+"||";
		}
		return cadena;
	}
	
	public static String formatoCadenaOriginalCero(double cadena){		
			return formatoCadenaOriginal(doubleToString(cadena));		
	}
}
