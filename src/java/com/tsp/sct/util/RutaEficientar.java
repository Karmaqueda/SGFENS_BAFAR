/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.util;

import com.tsp.sct.dao.dto.RutaMarcador;
import com.tsp.sct.dao.factory.RutaMarcadorDaoFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class RutaEficientar {
    
    public ArrayList<RutaMarcador> eficientarRuta(List<RutaMarcador> rutaMarcadores){
    //obtenermos los puntos marcadores de la ruta:
        
        System.out.println("# MARCADORES: "+ rutaMarcadores.size());
        
        //Creamos una lista de los marcadores obteniendo la distancia entre el primer punto:        
        ArrayList<RutaMarcador> marcadoresDistancia = new ArrayList<RutaMarcador>();
        
        //Array que contiene los puntos mas cercanos entre el punto referencia
        ArrayList<RutaMarcador> marcadoresDistanciaReferenciados = new ArrayList<RutaMarcador>();
        
        //Obtenemos el primero punto quee s con quien se comparara las distancias;
        RutaMarcador marcador1 = rutaMarcadores.get(0);
        
        //Obtenemos las distancia  
        while(rutaMarcadores.size() > 0){///---
            
            int marcadorNumero = 0; //variable que nos ayudara a que no comparemos contra el primer marcador
            for(RutaMarcador marcadorRuta : rutaMarcadores){

                //recuperamos el primer marcador, que es el origen y asi vamos a ir recuperando el primero de cada lista:
                marcador1 = rutaMarcadores.get(0);///---

                //System.out.println("Coordenadas : " + marcadorRuta.getLatitudMarcador() + ", " + marcadorRuta.getLongitudMarcador());

                //if(marcadorNumero > 0){
                    marcadorRuta.setDistanciaPunto(ConvertidorCoordenadasVerificadorPoligonos.distEntrePuntos(Double.parseDouble(marcador1.getLatitudMarcador()), Double.parseDouble(marcador1.getLongitudMarcador()), Double.parseDouble(marcadorRuta.getLatitudMarcador()), Double.parseDouble(marcadorRuta.getLongitudMarcador())));                        
                //}
                System.out.println("ID: " + marcadorRuta.getIdRutaMarcador() + ", Distancia del marcador " + marcadorNumero + " es: " +marcadorRuta.getDistanciaPunto());
                marcadorNumero++;
                //mapeamos los marcadores a un objeto de tipo lista:
                marcadoresDistancia.add(marcadorRuta);
            }
            
                
    //        //removemos el primer punto que es el que ya recuperamos y que vamos a comparar los demas puntos:
    //        marcadoresDistancia.remove(0);

            Collections.sort(marcadoresDistancia, new Comparator<RutaMarcador>() {
                @Override
                public int compare(RutaMarcador p1, RutaMarcador p2) {
                            //return Double(p1.getDistanciaPunto()).compareTo(new Double(p2.getDistanciaPunto()));
                            return Double.compare(p1.getDistanciaPunto(), p2.getDistanciaPunto());
                    }
            });

            System.out.println("-----------------------------------------");
            System.out.println("YA ORDENADOS POR DISTANCIA");

            for(RutaMarcador mar : marcadoresDistancia){
                System.out.println("Marcados ID: " + mar.getIdRutaMarcador() + " Distancia: " + mar.getDistanciaPunto());
            }
            //enviamos el primer punto, que es el que esta mas cerca del punto de referencia
            marcadoresDistanciaReferenciados.add(marcadoresDistancia.get(0));
            //quitamos el primero punto, que es el que esta mas cerca del punto de referencia y ya no es necesario:
            marcadoresDistancia.remove(0);
            //
            rutaMarcadores = marcadoresDistancia;
            marcadoresDistancia = new ArrayList<RutaMarcador>();
        }///---
        
        
        System.out.println("++++++++++++++++++++++++++++++++");
        System.out.println("YA ORDENADOS POR DISTANCIA Y POR REFERENCIA A PUNTO MAS CERCANO: ");
        for(RutaMarcador mar : marcadoresDistanciaReferenciados){
                System.out.println("Marcados ID: " + mar.getIdRutaMarcador() + " Distancia: " + mar.getDistanciaPunto());
        }

        return marcadoresDistanciaReferenciados;
    }
    
}
