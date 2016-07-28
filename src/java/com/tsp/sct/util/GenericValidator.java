/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ISCesarMartinez
 */
public class GenericValidator {
    
    //metodo para validar si la fecha es correcta con formato dd/MM/yyyy
    public boolean isDate(String fechax) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            Date fecha = formatoFecha.parse(fechax);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    //metodo para validar correo electronio
    public boolean isEmail(String correo) {
        Pattern pat = null;
        Matcher mat = null;        
        pat = Pattern.compile("^[\\w\\-\\_]+(\\.[\\w\\-\\_]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$");
        mat = pat.matcher(correo);
        if (mat.find()) {
            System.out.println("[" + mat.group() + "]");
            return true;
        }else{
            return false;
        }        
    }
    
    /**
     * Método para validar Contraseñas/Passwords seguros
     * 
     * De esta forma comprobaremos:
     *      Contraseñas que contengan al menos una letra mayúscula.
     *      Contraseñas que contengan al menos una letra minúscula.
     *      Contraseñas que contengan al menos un número o caracter especial.
     *      Contraseñas cuya longitud sea como mínimo 8 caracteres.
     *      Contraseñas cuya longitud máxima no debe ser arbitrariamente limitada.
     * 
     * @param password
     * @return true en caso de ser contraseña segura, false en caso contrario
     */
    public boolean isPasswordSeguro(String password) {
        Pattern pat = null;
        Matcher mat = null;        
        pat = Pattern.compile("(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$");
        mat = pat.matcher(password);
        if (mat.find()) {
            System.out.println("[" + mat.group() + "]");
            return true;
        }else{
            return false;
        }        
    }
    
    /*Método que tiene la función de validar el curp*/
     public boolean isCURP(String curp){
             curp=curp.toUpperCase().trim();
             return curp.matches("[A-Z][A,E,I,O,U,X][A-Z]{2}[0-9]{2}[0-1][0-9][0-3][0-9][M,H][A-Z]{2}[B,C,D,F,G,H,J,K,L,M,N,Ñ,P,Q,R,S,T,V,W,X,Y,Z]{3}[0-9,A-Z][0-9]");//("[A-Z]{4}[0-9]{6}[H,M][A-Z]{5}[0-9]{2}");
     }//Cierra método validarCurp

     /*Método que tiene la función de validar el rfc*/
     public boolean isRFC(String rfc){
          rfc=rfc.toUpperCase().trim();
          if (rfc.trim().length()<12 || rfc.trim().length()>13){
              return false;
          }else{
            //return rfc.toUpperCase().matches("[A-Z,Ñ,&]{3,4}[0-9]{2}[0-1][0-9][0-3][0-9][A-Z,0-9]?[A-Z,0-9]?[0-9,A-Z]?");//("[A-Z]{4}[0-9]{6}[A-Z0-9]{3}");
            return rfc.toUpperCase().matches("[A-Z,Ñ,&]{3,4}[0-9]{2}[0-1][0-9][0-3][0-9][A-Z,0-9]?[A-Z,0-9]?[0-9,A-Z]?");//("[A-Z]{4}[0-9]{6}[A-Z0-9]{3}");
          }
      }//Cierra método validarRFC
     
     public boolean isCodigoPostal(String CP){
         CP = CP.trim();
         try{
             int test = Integer.parseInt(CP);
         }catch(Exception ex){
             return false;
         }
         if(CP.length()!=5){
             return false;
         }
         return true;
     }
     
     public boolean isNumeric(String cadena, int minLenght, int maxLenght){
         cadena = cadena.trim();
         try{
             long test = Long.parseLong(cadena);
         }catch(Exception ex){
             return false;
         }
         if(cadena.length()<minLenght){
             return false;
         }
         if(cadena.length()>maxLenght){
             return false;
         }
         return true;
     }
     
     /**
      * Verifica que sea una cadena valida según las especificaciones
      * @param cadena
      * @param minLenght longitud mínima
      * @param maxLenght longitud máxima
      * @return 
      */
     public boolean isValidString(String cadena, int minLenght, int maxLenght){
         cadena = cadena!=null?cadena.trim():"";
         if(cadena.length()<minLenght){
             return false;
         }
         if(cadena.length()>maxLenght){
             return false;
         }
         return true;
     }
     
     /*Método que tiene la función de validar un UUID de Timbre Fiscal Digital*/
     public boolean isUUID(String uuid){
        if (uuid==null)
            return false;
        
        if (uuid.length()!=36)
            return false;
         
        uuid=uuid.toUpperCase().trim();
        return uuid.matches("[a-f0-9A-F]{8}-[a-f0-9A-F]{4}-[a-f0-9A-F]{4}-[a-f0-9A-F]{4}-[a-f0-9A-F]{12}");
     }
     
     /**
	 * Verifica si es un numero de tarjeta de credito valido
	 * Solo para VISA o Mastercard
	 * @param creditCardNumber
	 * @return true en caso de ser un numero de tarjeta válido, false en caso contrario
	 */
	public boolean isCreditCardNumber(String creditCardNumber){
		boolean isValid = false;
		// VISA:  		^4\\d{3}-?\\d{4}-?\\d{4}-?\\d{4}$ 
		// MASTERCARD: 	^5[1-5]\\d{2}-?\\d{4}-?\\d{4}-?\\d{4}$
		
		Pattern patVisa = Pattern.compile("^4\\d{3}-?\\d{4}-?\\d{4}-?\\d{4}$");
		Matcher matVisa = patVisa.matcher(creditCardNumber);
		if (matVisa.find()) {
			System.out.println("[" + matVisa.group() + "]");
			return true;
		} else {
			isValid = false;
		}
		
		Pattern patMasterCard = Pattern.compile("^5[1-5]\\d{2}-?\\d{4}-?\\d{4}-?\\d{4}$");
		Matcher matMasterCard = patMasterCard.matcher(creditCardNumber);
		if (matMasterCard.find()) {
			System.out.println("[" + matMasterCard.group() + "]");
			return true;
		} else {
			isValid = false;
		}
		
		
		return isValid;
	}
	
	/**
	 * Verifica la fecha de expiración de una tarjeta de crédito
	 * Debe cumplir con el formato 'MM/YY'
	 * y ser una fecha válida   
	 * @param creditCardExpirationDate
	 * @return true en caso de ser aprobado, false en caso contrario
	 */
	public boolean isCreditCardExpirationDate(String creditCardExpirationDate){
		// FECHA DE EXPIRACION:  ^((0[1-9])|(1[0-2]))\\/(([1-2][0-9]))$
		
		boolean isValid = false;
		
		Pattern pat = Pattern.compile("^((0[1-9])|(1[0-2]))\\/(([1-2][0-9]))$");
		Matcher mat = pat.matcher(creditCardExpirationDate);
		if (mat.find()) {
			System.out.println("[" + mat.group() + "]");
			return true;
		} else {
			isValid = false;
		}
		
		return isValid;
	}
        
    public boolean isRegistroPatronal(String registro){
        if (registro==null)
            return false;
        
        if (registro.length()!=11)
            return false;
         
        registro=registro.toUpperCase().trim();
        return registro.matches("[a-z0-9A-Z]{3}[1-9]{1}[0-9]{4}[0-9]{2}[0-9]{1}");
     }
        
     /*
        Valida hora formato 24 hrs HH:MM:SS
        */
     public boolean isTime(String hora){
        if (hora==null)
            return false;
        
        if (hora.length()!=8)
            return false;
         
        hora=hora.trim();
        return hora.matches("([0-1][0-9]|[0-2][0-3]):[0-5][0-9]:[0-5][0-9]");
     }
     
     public boolean validarConRegex(String regex, String cadena){
        if (cadena==null)
            return false;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cadena);
        return matcher.find();
     }
     
     
    public static boolean isRuc(String  ruc){
        final int NUM_PROVINCIAS = 24;
        int[] coeficientes = { 4, 3, 2, 7, 6, 5, 4, 3, 2 };
        int constante = 11;
        boolean resp_dato = false;
        final int prov = Integer.parseInt(ruc.substring(0, 2));
        
        if (!((prov > 0) && (prov <= NUM_PROVINCIAS))) {
            resp_dato = false;
        }

        int[] d = new int[10];
        int suma = 0;

        for (int i = 0; i < d.length; i++) {
            d[i] = Integer.parseInt(ruc.charAt(i) + "");
        }

        for (int i = 0; i < d.length - 1; i++) {
            d[i] = d[i] * coeficientes[i];
            suma += d[i];
        }

        int aux, resp;

        aux = suma % constante;
        resp = constante - aux;

        resp = (aux == 0) ? 0 : resp;

        if (resp == d[9]) {
            resp_dato = true;
        } else {
            resp_dato = false;
        }
        return resp_dato;
    }
    
    public boolean isCelularMexico(String celular){
        return validarConRegex("^[0-9]{10}$", celular);
    }
    
    public boolean isClaveMetodoPagoSAT(String clave){
        return validarConRegex(clave, "01|02|03|04|05|06|07|08|09|10|11|12|13|14|15|16|17|98|99");
    }
     
}
