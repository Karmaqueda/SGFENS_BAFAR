/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.cfdi;

import com.tsp.sct.config.Configuration;
import com.tsp.sct.util.UtilSecurity;
import com.tsp.sct.util.CompareParentCertificate;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ISC César Ulises Martínez García
 *
 * Clase para validaciones y rutinas adicionales para comprobar el error 308
 * dictaminado por el SAT:
 *
 * 308 - Certificado no expedido por el SAT.
 * Que el CSD del emisor haya sido firmado por uno de los
 * Certificados de Autoridad del SAT
 */
public class ValidaError308 {
    Cfd3BO cfd3BO = null;
    
    public ValidaError308(Cfd3BO cfd3BO){
        this.cfd3BO = cfd3BO;
    }

    public boolean error308() throws Exception{
        boolean existError = false;
        java.security.cert.X509Certificate CertEmisorChild = null;

        /**
         * (Certificado Hijo)
         * Recuperamos el Certificado del Emisor del Comprobante recibido 
         */
        CertEmisorChild = cfd3BO.getCertificateEmisor();

        java.security.cert.X509Certificate[] certificadosRaiz = getArregloCertificadosRaiz();

        /**
         * Comparamos el certificado del Emisor (hijo) con cada uno de los certificados Raiz
         * Al menos uno debe ser valido y corresponder, de lo contrario
         * se lanza la excepcion de error 308
         */
        int validos = 0;
        for (java.security.cert.X509Certificate x509CertificateRaizSAT : certificadosRaiz){
            //Inicializamos un comparador de Certificados con parentezco
            CompareParentCertificate cParentCert = new CompareParentCertificate(x509CertificateRaizSAT, CertEmisorChild);
            if (cParentCert.isParentBasic()) {
                validos++;
            }
        }

        if (validos<=0){
                existError = true;
                throw new Exception("308 - Certificado no expedido por el SAT. Que el CSD del emisor haya sido firmado por uno de los Certificados de Autoridad del SAT");
        }

        return existError;
    }

    public java.security.cert.X509Certificate[] getArregloCertificadosRaiz(){
        java.security.cert.X509Certificate[] certificados = new java.security.cert.X509Certificate[0];

        /**
         * Recuperamos los datos de configuracion de la
         * aplicación definidos en archivo .properties
         */
        Configuration appConfig = new Configuration();

        /*
        //Obtenemos directorio de Certificados Raiz
        File directorio = new File(appConfig.getRutaRepositorio_CertificadosRaizSAT());
        if (directorio.isDirectory()){
            List<java.security.cert.X509Certificate> listaCertificados = new ArrayList<java.security.cert.X509Certificate>();

            String [] ficheros = directorio.list();
            //Recorremos cada uno de los archivos de certificados y lo agregamos a la lista
            for (String archivoCertificado:ficheros){
                try {
                    listaCertificados.add(UtilSecurity.openCertificateFile(appConfig.getRutaRepositorio_CertificadosRaizSAT() + archivoCertificado));
                }catch(Exception e){
                    //Damos salida de alerta a la consola del servidor
                    System.out.println("******************ATENCION********************************");
                    System.out.println("El certificado raiz del SAT usado para comparar error 308 con nombre: "+archivoCertificado+" no es valido.");
                    System.out.println("Descripción del Error: " + e.getMessage());
                    System.out.println("**********************************************************");
                }
            }

            if (listaCertificados.size()>0){
                //Si la lista tiene mas de un certificado valido entonces
                //transformamos la lista en un arreglo
                certificados = listaCertificados.toArray(certificados);
            }
        }
         */

        return certificados;
    }

}
