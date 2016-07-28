/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.pdf.cfd.util;

/**
 *
 * @author ISC César Ulises Martínez García
 */
public class CfdTransform {

    public String formatoPdfSAT(String cadena) {
        if (cadena!=null) {

            cadena = cadena.replaceAll("\t", " ");
            while (cadena.indexOf("  ")>=0) {
                cadena = cadena.replaceAll("  ", " ");
            }


            //cadena = new String(cadena.getBytes(java.nio.charset.Charset.forName("utf-8")));

            cadena = cadena.replaceAll("&lt;","<");
            cadena = cadena.replaceAll("&gt;",">");
            cadena = cadena.replaceAll("&quot;","\"");
            cadena = cadena.replaceAll("&apos;","\'");
            cadena = cadena.replaceAll("&amp;","&");

        }
        return cadena;
    }

    public String formatoXmlSAT(String nombreAtributo, String cadena, int longitudMinima) {
        if (cadena!=null) {

            cadena = cadena.replaceAll("\t", " ");
            while (cadena.indexOf("  ")>=0) {
                cadena = cadena.replaceAll("  ", " ");
            }


            //cadena = new String(cadena.getBytes(java.nio.charset.Charset.forName("utf-8")));

            cadena = cadena.replaceAll("&", "&amp;");
            cadena = cadena.replaceAll("<", "&lt;");
            cadena = cadena.replaceAll(">", "&gt;");
            cadena = cadena.replaceAll("\"", "&quot;");
            cadena = cadena.replaceAll("'", "&apos;");
            if (cadena.trim().length()>=longitudMinima) {
                cadena = " " + nombreAtributo + "=\"" + cadena + "\" ";
            }
            else {
                cadena = "";
            }
        }
        else {
            cadena = "";
        }
        return cadena;
    }

    public String formatoCadenaSAT(String cadena) {
        while (cadena.indexOf("|")>=0) {
            cadena = cadena.substring(0, cadena.indexOf("|")) + cadena.substring(cadena.indexOf("|")+1);
        }
        cadena = cadena.replaceAll("\t", " ");
        while (cadena.indexOf("  ")>=0) {
            cadena = cadena.replaceAll("  ", " ");
        }

        cadena = cadena.replaceAll("\n", " ");
        if (cadena.equals("")) {
            return "";
        }
        else {
            return "|" + cadena;
        }
    }

}
