/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.util.test;

import com.tsp.sct.util.ConvertidorCoordenadasVerificadorPoligonos;

/**
 *
 * @author leonardo
 */
public class testConvertidorCoordenadasIntegerVerificadorPoligonos {
    
    public static void main(String [] args){
        
        ConvertidorCoordenadasVerificadorPoligonos validador = new ConvertidorCoordenadasVerificadorPoligonos();
        //poligono:
        //validador.ordenadorCoordenadas("19.374555269437256, -99.18424107134342,19.373947992532354, -99.17321182787418,19.369858935780677, -99.17651630938053,19.364190766654165, -99.17291142046452,19.36204490834074, -99.17870499193668,19.36471710494575, -99.18625809252262,19.370911574058333, -99.18694473803043", 3);
        //cuadrado
        //validador.ordenadorCoordenadas("19.36467661744521, -99.1845703125, 19.371923719836367, -99.1741418838501", 2);
        //circulo 1
        //validador.ordenadorCoordenadas("789.05,19.36872531772422, -99.18182373046875", 1);
        //circulo 2
        validador.ordenadorCoordenadas("2479.59,19.436161855911614, -99.06972885131836", 1);
        
        
        boolean contenido = validador.puntoContenidoEnPoligono("29.36887", "-99.18104", 1);
        
        
        //convertimos a entero:
        System.out.println("Punto contenido: "+contenido);
        
    }
    
}
