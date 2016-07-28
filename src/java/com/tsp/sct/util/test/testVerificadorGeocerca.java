/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.util.test;

    
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


/**
 *
 * @author leonardo
 */
public class testVerificadorGeocerca {

    public static void main(String[] args) throws ScriptException, NoSuchMethodException {
    ScriptEngineManager script = new ScriptEngineManager();
        ScriptEngine js = script.getEngineByName("JavaScript");
        
        // Añanidimos al string codigo JavaScript
        String codigo = "var contentCenter = '<span class=\"infowin\">Center Marker (draggable)</span>'," +
"        contentA = '<span class=\"infowin\">Marker A (draggable)</span>'," +
"        contentB = '<span class=\"infowin\">Marker B (draggable)</span>';" +
"    var " +
"        latLngCenter = new google.maps.LatLng(37.081476, -94.510574)," +
"        latLngCMarker = new google.maps.LatLng(37.0814, -94.5105)," +
"        latLngA = new google.maps.LatLng(37.2, -94.1)," +
"        latLngB = new google.maps.LatLng(38, -93)," +
"        map = new google.maps.Map(document.getElementById('map'), {" +
"            zoom: 7," +
"            center: latLngCenter," +
"            mapTypeId: google.maps.MapTypeId.ROADMAP," +
"            mapTypeControl: false" +
"        })," +
"        markerCenter = new google.maps.Marker({" +
"            position: latLngCMarker," +
"            title: 'Location'," +
"            map: map," +
"            draggable: true" +
"        })," +
"        infoCenter = new google.maps.InfoWindow({" +
"            content: contentCenter" +
"        })," +
"        markerA = new google.maps.Marker({" +
"            position: latLngA," +
"            title: 'Location'," +
"            map: map," +
"            draggable: true" +
"        })," +
"        infoA = new google.maps.InfoWindow({" +
"            content: contentA" +
"        })," +
"        markerB = new google.maps.Marker({" +
"            position: latLngB," +
"            title: 'Location'," +
"            map: map," +
"            draggable: true" +
"        })," +
"        infoB = new google.maps.InfoWindow({" +
"            content: contentB" +
"        })," +
"        circle = new google.maps.Circle({" +
"            map: map," +
"            clickable: false,         " +
"            radius: 100000," +
"            fillColor: '#fff'," +
"            fillOpacity: .6," +
"            strokeColor: '#313131'," +
"            strokeOpacity: .4," +
"            strokeWeight: .8" +
"        });    " +
"    circle.bindTo('center', markerCenter, 'position');" +
"    var bounds = circle.getBounds()," +
"        noteA = jQuery('.bool#a')," +
"        noteB = jQuery('.bool#b');" +
"    noteA.text(bounds.contains(latLngA));" +
"    noteB.text(bounds.contains(latLngB));    " +
"    google.maps.event.addListener(markerCenter, 'dragend', function() {" +
"        latLngCenter = new google.maps.LatLng(markerCenter.position.lat(), markerCenter.position.lng());" +
"        bounds = circle.getBounds();" +
"        noteA.text(bounds.contains(latLngA));" +
"        noteB.text(bounds.contains(latLngB));" +
"    });" +
"    google.maps.event.addListener(markerA, 'dragend', function() {" +
"        latLngA = new google.maps.LatLng(markerA.position.lat(), markerA.position.lng());" +
"        noteA.text(bounds.contains(latLngA));" +
"    });" +
"    google.maps.event.addListener(markerB, 'dragend', function() {" +
"        latLngB = new google.maps.LatLng(markerB.position.lat(), markerB.position.lng());" +
"        noteB.text(bounds.contains(latLngB));" +
"    });" +
"    google.maps.event.addListener(markerCenter, 'click', function() {" +
"        infoCenter.open(map, markerCenter);" +
"    });" +
"    google.maps.event.addListener(markerA, 'click', function() {" +
"        infoA.open(map, markerA);" +
"    });" +
"    google.maps.event.addListener(markerB, 'click', function() {" +
"        infoB.open(map, markerB);" +
"    });" +
"    google.maps.event.addListener(markerCenter, 'drag', function() {" +
"        infoCenter.close();" +
"        noteA.html(\"draggin&hellip;\");" +
"        noteB.html(\"draggin&hellip;\");" +
"    });" +
"    google.maps.event.addListener(markerA, 'drag', function() {" +
"        infoA.close();" +
"        noteA.html(\"draggin&hellip;\");" +
"    });" +
"    google.maps.event.addListener(markerB, 'drag', function() {" +
"        infoB.close();" +
"        noteB.html(\"draggin&hellip;\");" +
"    });" +
"};";
                
                
                /*"function suma(a,b){"+
                "var resultado= a+b;"+
                "return resultado;"+
                "}"+
              "function resta(a,b){"+
                "var resultado= a-b;"+
                "return resultado;"+
                "}"+
              "function multiplicacion(a,b){"+
                "var resultado= a*b;"+
                "return resultado;"+
                "}"+
              "function division(a,b){"+
                "var resultado= a/b;"+
                "return resultado;"+
                "}" +
                "function SumaYMultiplica(){" +
                "var resultado=suma(2, 10)+multiplicacion(2, 4);" +
                "return resultado;" +
                "}" ;*/
        js.eval(codigo);
        Invocable inv = (Invocable) js;
        System.out.println(inv.invokeFunction("suma",1,4));
        System.out.println(inv.invokeFunction("resta",12,4));
        System.out.println(inv.invokeFunction("multiplicacion",16,34));
        System.out.println(inv.invokeFunction("division",100,4));
        System.out.println(inv.invokeFunction("SumaYMultiplica"));
        try {
            java.lang.Runtime.getRuntime().exec("C:\\Users\\leonardo\\Documents\\NetBeansProjects\\SGFENS\\web\\jsp\\catGeocerca\\mapaGeocercasCron.jsp");
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
    
