/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.util.test;

import java.awt.Polygon;
import java.lang.Object;
//import org.jhotdraw.geom.Polygon2D;

/**
 *
 * @author leonardo
 */
public class verificadorPoligonos {
    
    public int npoints;
    public float xpoints[];
    public float ypoints[];
    
    public static void main(String []args){
        
        /*final Polygon polygon = new Polygon();
    polygon.addPoint(-10, -10);
    polygon.addPoint(-10, 10);
    polygon.addPoint(10, 10);
    polygon.addPoint(10, -10);

    System.out.println(polygon.contains(10, 10.5));*/
        
        /*final Polygon polygon = new Polygon();
    polygon.addPoint(-9917414, 1936467);
    polygon.addPoint(-9918457, 1936467);
    polygon.addPoint(-9918457, 1937192);
    polygon.addPoint(-9917814, 1936992);
    polygon.addPoint(-9917414, 1937192);*/
        
        final Polygon polygon = new Polygon();
    polygon.addPoint(-991842410, 193745552);
    polygon.addPoint(-991732118, 193739479);
    polygon.addPoint(-991765163, 193698589);
    polygon.addPoint(-991729114, 193641907);
    polygon.addPoint(-991787049, 193620449);
    polygon.addPoint(-991862580, 193647171);
    polygon.addPoint(-991869447, 193709115);   
    
    
        
            
    

    System.out.println(polygon.contains(-991810400, 193688700));
    
        //-----------------------
        
        /*verificadorPoligonos verifica = new verificadorPoligonos();
        verifica.npoints = 4;
        float xpoint[] = {19.3646, 19.3646, 19.3719 19.3719};
        float ypoint[] = {-99.1845703125, -99.1741418838501, -99.1845703125, -99.1741418838501};       
        verifica.xpoints = xpoint;
        verifica.ypoints = ypoint;
        
        double puntoX = 19.43626;
        double puntoY = -99.06962;
        
        boolean esta = verifica.valida(puntoX, puntoY, verifica.npoints, verifica.xpoints, verifica.ypoints);
        System.out.println("Punto se encuentra o no: "+esta);*/
        
        //System.out.println(valida());
    }
    
/*   public static boolean valida(){
        boolean dentro = false;
        //float x[] = {1.5,4.2,0.3,6.2};
        //float y[] = new float [4];
        
        Polygon2D.Float p = new Polygon2D.Float(5);//D.Float( new float[] {0.1,0.3,0.1,0.2}, new float[] {20,40,40,20}, npoints);
        
        /*p.addPoint(new Float(-99.1741418838501), new Float(19.36467661744521));
        p.addPoint(new Float(-99.1845703125), new Float(19.36467661744521));
        p.addPoint(new Float(-99.1845703125), new Float(19.371923719836367));
        p.addPoint(new Float(-99.1741418838501), new Float(19.371923719836367));*/
        
        /*p.addPoint(new Float(-10), new Float(-10));
        p.addPoint(new Float(-10), new Float(10));
        p.addPoint(new Float(10), new Float(10));
        p.addPoint(new Float(10), new Float(-10));*/
        
        //19.36467661744521, -99.1845703125, 19.371923719836367, -99.1741418838501
        
/*        System.out.println(p.getBounds().contains(19.36887, -99.18104));
        
        dentro = p.contains(-99.18104, 19.36887);
        return dentro;
    }*/
    
    /*public static void main(String []args){
        verificadorPoligonos verifica = new verificadorPoligonos();
        verifica.npoints = 4;
        double xpoint[] = {19.36467661744521, 19.36467661744521, 19.371923719836367, 19.371923719836367};
        double ypoint[] = {-99.1845703125, -99.1741418838501, -99.1845703125, -99.1741418838501};       
        verifica.xpoints = xpoint;
        verifica.ypoints = ypoint;
        
        double puntoX = 19.43626;
        double puntoY = -99.06962;
        
        boolean esta = verifica.valida(puntoX, puntoY, verifica.npoints, verifica.xpoints, verifica.ypoints);
        System.out.println("Punto se encuentra o no: "+esta);
    }*/
    
    /*public boolean valida(double puntoX, double puntoY, int npoints, double xpoints[], double ypoints[]){
        boolean dentro = false;        
                
        //Polygon p = new Polygon(xpoints, ypoints, npoints);
        //Poligon (El array de las coordenadas en X, El array de las coordenadas en Y, el numero total de puntos);
        Polygon p = new Polygon(xpoints, ypoints, npoints);
        //Polygon p = new Polygon();
        dentro = p.contains(puntoX, puntoY);        
        return dentro;
    }*/
    
}
