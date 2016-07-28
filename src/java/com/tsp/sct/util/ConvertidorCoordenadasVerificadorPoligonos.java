package com.tsp.sct.util;

import java.awt.Polygon;
import java.util.StringTokenizer;

/**
 *
 * @author leonardo
 * Clase que me convierte las coordenadas (latitud y longitud) a entero para poder hacer las comparaciones con las geocercas
 * Ademas la clase verifica si un punto esta dentro de un poligono
 */
public class ConvertidorCoordenadasVerificadorPoligonos {
      
    
    public static String parteEntera = "";
    public static String parteDecimal = "";
    public static int numDigitosParteEntera = 0;
    public static String valorFormado = "";
    public static int valorFormadoInt = 0;
    
    static Polygon polygon = new Polygon();
    
    //VARIABLES PARA TIPO CIRCULO
    public static String radioCirculo = "";
    public static String latitudCirculo = "";
    public static String longitudCirculo = "";
    public static double radioCirculoInt = 0;
    public static double latitudCirculoInt = 0;
    public static double longitudCirculoInt = 0;
    
    
    public static int numCaracteresParteEntera(String valor){
        
        //reiniciamos variables:
        parteEntera = "";
        parteDecimal = "";
        numDigitosParteEntera = 0;
        valorFormado = "";
        valorFormadoInt = 0;
        
        StringTokenizer tokens = new StringTokenizer(valor.trim(),".");
        String parteEnteraStr = tokens.nextToken().intern();
        parteEntera = parteEnteraStr;
        parteDecimal = tokens.nextToken().intern();
        numDigitosParteEntera = parteEnteraStr.length();
        //concatenamos mas valores para soportar el entero:
        concatenaParteEnteraDecimal();
        return valorFormadoInt;
    }
    
    public static void concatenaParteEnteraDecimal(){        
        if(numDigitosParteEntera == 0){
            if(parteDecimal.length()>9)//si es mayor a 10 digitos lo recortamos
                valorFormado = parteEntera + parteDecimal.substring(0, 9);
            else
                valorFormado = parteEntera + parteDecimal.substring(0);
        }else if(numDigitosParteEntera == 1){
            if(parteDecimal.length()>8)//si es mayor a 10 digitos lo recortamos
                valorFormado = parteEntera + parteDecimal.substring(0, 8);
            else
                valorFormado = parteEntera + parteDecimal.substring(0);            
        }else if(numDigitosParteEntera == 2){
            if(parteDecimal.length()>7)//si es mayor a 10 digitos lo recortamos
                valorFormado = parteEntera + parteDecimal.substring(0, 7);
            else
                valorFormado = parteEntera + parteDecimal.substring(0);
        }else if(numDigitosParteEntera == 3){        
            if(parteDecimal.length()>7)//si es mayor a 10 digitos lo recortamos
                valorFormado = parteEntera + parteDecimal.substring(0, 7);
            else
                valorFormado = parteEntera + parteDecimal.substring(0);
        }
        
        //si le faltan caracteres se los añadimos, deben ser de 9 digitos:
        if(Integer.parseInt(valorFormado)>0){
            while(valorFormado.length()<9){
                valorFormado += "0";
            }
        }else{ //si es un valor negativo, tiene que ser de 10 caracteres, porque tiene el signo de menos (-) y ese cuenta no cuenta 
           while(valorFormado.length()<10){
               valorFormado += "0";
           }   
        }
        
        valorFormadoInt = Integer.parseInt(valorFormado);        
        
    }
    
    public void ordenadorCoordenadas(String cadenaCoordenadas, int tipoGeocerca){
        StringTokenizer tokens = new StringTokenizer(cadenaCoordenadas,",");
        if(tipoGeocerca == 1){//si es un circulo
            radioCirculo = tokens.nextToken().intern();
            latitudCirculo = tokens.nextToken().intern();
            longitudCirculo = tokens.nextToken().intern();
            latitudCirculoInt = Double.parseDouble(latitudCirculo);
            longitudCirculoInt = Double.parseDouble(longitudCirculo);
            radioCirculoInt = Double.parseDouble(radioCirculo);
                        
        }else if(tipoGeocerca ==2){//si es un cuadrado
            String latitudPuntoA = tokens.nextToken().intern();
            String longitudA = tokens.nextToken().intern();
            String latitudPuntoB = tokens.nextToken().intern();
            String longitudB = tokens.nextToken().intern();
            //para forma el cuadrado armamos las 4 coordenadas
            polygon = new Polygon();
            polygon.addPoint(numCaracteresParteEntera(longitudA), numCaracteresParteEntera(latitudPuntoA));//CREAMOS LA COORDENADA A PARA EL PUNTO DEL CUADRADO
            polygon.addPoint(numCaracteresParteEntera(longitudA), numCaracteresParteEntera(latitudPuntoB));//CREAMOS LA COORDENADA B PARA EL PUNTO DEL CUADRADO
            polygon.addPoint(numCaracteresParteEntera(longitudB), numCaracteresParteEntera(latitudPuntoA));//CREAMOS LA COORDENADA C PARA EL PUNTO DEL CUADRADO
            polygon.addPoint(numCaracteresParteEntera(longitudB), numCaracteresParteEntera(latitudPuntoB));//CREAMOS LA COORDENADA D PARA EL PUNTO DEL CUADRADO
        }else if(tipoGeocerca ==3){//si es un poligono
            polygon = new Polygon();
            while (tokens.hasMoreTokens()) {
                String latitud = tokens.nextToken().intern();
                String longitud = tokens.nextToken().intern();                               
                polygon.addPoint(numCaracteresParteEntera(longitud), numCaracteresParteEntera(latitud));//CREAMOS LA COORDENADA PARA EL PUNTO DEL POLIGONO
                //System.out.println("polygon.addPoint("+numCaracteresParteEntera(longitud)+", "+ numCaracteresParteEntera(latitud)+")");
            }
        }else{//no definido
        }
        
    }
    
    public boolean puntoContenidoEnPoligono(String latitud, String longitud, int tipoGeocerca){//enviar diferente de 1 cuando no es un circulo
        boolean coordenadaDentro = false;
        if(tipoGeocerca == 1){
            double puntoBlati = Double.parseDouble(latitud);
            double puntoBlong = Double.parseDouble(longitud);
            double distancia = distEntrePuntos(puntoBlati, puntoBlong, latitudCirculoInt, longitudCirculoInt);
            //System.out.println("////////DISTANCIA RADIO: "+radioCirculoInt);
            //System.out.println("////////DISTANCIA ENTRE LOS DOS PUNTOS: "+distancia);
            if(distancia <= radioCirculoInt){ //comparamos si esta dentro de la geocerca del circulo
                coordenadaDentro = true;
            }else{
                coordenadaDentro = false; //esta fuera de la geocerca circulo
            }            
            
        }else{            
            coordenadaDentro = polygon.contains(numCaracteresParteEntera(longitud), numCaracteresParteEntera(latitud));//regresa true si esta contenido, de lo contrario false
        }
        return coordenadaDentro;
    }
    
    public static double distEntrePuntos(double lat1, double lng1, double lat2, double lng2) {  
        //double earthRadius = 3958.75;//miles  
        //double earthRadius = 6371;//kilometers  
        double earthRadius = 6371000;//metros
        double dLat = Math.toRadians(lat2 - lat1);  
        double dLng = Math.toRadians(lng2 - lng1);  
        double sindLat = Math.sin(dLat / 2);  
        double sindLng = Math.sin(dLng / 2);  
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)  
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));  
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));  
        double dist = earthRadius * c;    
        return dist;  
    }
    
    /*funionamiento:
        ConvertidorCoordenadasVerificadorPoligonos validador = new ConvertidorCoordenadasVerificadorPoligonos();
        //poligono:
        //validador.ordenadorCoordenadas("19.374555269437256, -99.18424107134342,19.373947992532354, -99.17321182787418,19.369858935780677, -99.17651630938053,19.364190766654165, -99.17291142046452,19.36204490834074, -99.17870499193668,19.36471710494575, -99.18625809252262,19.370911574058333, -99.18694473803043", 3);
        //cuadrado
        validador.ordenadorCoordenadas("19.36467661744521, -99.1845703125, 19.371923719836367, -99.1741418838501", 2);        
        boolean contenido = validador.puntoContenidoEnPoligono("29.36887", "-99.18104", 0); //se envio diferente de 3 cuando no es circulo
    
    */
}
