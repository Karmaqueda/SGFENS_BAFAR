/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.util;

/**
 *
 * @author ISCesar
 */

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * @author 578
 */
public class GenericMethods {
    
     public String getFileExtension(String path) {
   
        try {
            return path.substring(path.lastIndexOf("."));

        } catch (Exception e) {
            return "";
        }

    }
    
    public static List<String> getListaCorreosValidos(String strListaCorreos, String correoCharSeparator){
        List<String> listaCorreosValidos = new ArrayList<String>();
        String[] arrayCorreos  = getArrayCorreos(strListaCorreos, correoCharSeparator);
        for (String correo :  arrayCorreos){
            if (!StringManage.getValidString(correo).equals("")){
                if (new GenericValidator().isEmail(StringManage.getValidString(correo)))
                    listaCorreosValidos.add(correo);
            }
        }
        return listaCorreosValidos;
    }
     
    public static String[] getArrayCorreos(String strListaCorreos, String correoCharSeparator){
        String[] arrayCorreos = new String[0];
        try{
            arrayCorreos = strListaCorreos.split(correoCharSeparator);
        }catch(Exception ex){}
        return arrayCorreos;
    }
    
    public static String exceptionStackTraceToString(Exception ex){
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
    
    public static String getDateHourString(Date date){
        if (date == null)
            return null;
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return dateFormat.format(date);
    }
    
    public static String concatInts(List<Integer> ints, char concatChar){
        String concat = "";
        
        for (Integer item : ints){
            concat += item + "" + concatChar;
        }
        
        if (concat.length()>0){
            if (concat.endsWith(""+concatChar)){
                concat = concat.substring(0, concat.length()-1);
            }
        }
        
        return concat;
    }
    
    public static boolean datoEnColeccion(int dato, Integer[] coleccion){
        boolean existe = false;
        
        try{
            List<Integer> coleccionList = Arrays.asList(coleccion);
            existe = coleccionList.contains(dato);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return existe;
    }
    
    /**
     * IMPORTANTE: SOLO FUNCIONA CON ATRIBUTOS CON ACCESIBILIDAD public
     * Transforma a cadena el contenido completo de un objeto,
     * incluyendo todos sus atributos y sus respectivos valores.
     * @param o Objeto del que se requiere el contenido en cadena
     * @return Cadena con la descripcion de atributos y valores del objeto
     */
    public static String objectToString(Object o) {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append(o.getClass().getName());
        result.append(" Object {");
        result.append(newLine);

        //determine fields declared in this class only (no fields of superclass)
        Field[] fields = o.getClass().getDeclaredFields();

        //print field names paired with their values
        for (Field field : fields) {
          result.append("  ");
          try {
            result.append(field.getName());
            result.append(": ");
            //requires access to private field:
            field.setAccessible(true);
            result.append(field.get(o));
          }catch (Exception ex) {
            System.out.println(ex);
          }
          result.append(newLine);
        }
        result.append("}");

        return result.toString();
      }
    
}
