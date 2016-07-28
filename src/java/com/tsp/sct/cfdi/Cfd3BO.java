/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.cfdi;

import com.tsp.sct.config.Configuration;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.cert.X509Certificate;
import java.util.Date;
import mx.bigdata.sat.cfdi.CFDv3;
import mx.bigdata.sat.cfdi.TFDv1;
import mx.bigdata.sat.cfdi.schema.*;
import mx.bigdata.sat.security.KeyLoader;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author ISC César Ulises Martínez García
 */
public class Cfd3BO {

    Comprobante comprobanteFiscal = null;
    CFDv3 cfd = null;
    TimbreFiscalDigital timbreFiscalDigital =null;
    TFDv1 tfd=null;


    public Cfd3BO() {
    }

    /**
     * Constructor de un objeto Cfd3BO que recibe un objeto tipo File
     * lo convierte a tipo Comprobante e intenta obtener su correspondiente
     * CFDv3 para luego validarlo.
     * @param file Archivo de entrada XML
     * @throws Exception
     */
    public Cfd3BO(File fileCFD) throws Exception{
            Comprobante comp = CFDv3.newComprobante(new FileInputStream(fileCFD));
            CFDv3 cfd = new CFDv3(comp);
            //CFDv3 cfd = new CFDv3(new FileInputStream(fileCFD));

            //Asginamos a los valores locales
            this.comprobanteFiscal = comp;
            this.cfd = cfd;
    }

    /**
     * Método que genera el timbre Fiscal digital usando el certificado y llave privada
     * del PAC
     * @param certificadoCSDFile Objeto File con el archivo de Certificado CSD del PAC
     * @param privateKeyFile Objeto File con el archivo de Llave Privada del PAC
     * @param privateKeyPassword Password del archivo de Llave Privada del PAC
     * @throws FileNotFoundException
     * @throws Exception
     */
    public boolean timbrarCFD(File certificadoCSDFile, File privateKeyFile, String privateKeyPassword) throws FileNotFoundException, Exception{
        //Configuration appConfig = new Configuration();
        boolean exito = false;

        java.security.PrivateKey key = null;
        java.security.cert.X509Certificate cert = null;
        try {
            key = KeyLoader.loadPKCS8PrivateKey(new FileInputStream(privateKeyFile),  privateKeyPassword);
            cert = KeyLoader.loadX509Certificate(new FileInputStream(certificadoCSDFile));
        }
        catch (Exception e) {
            //return null;
        }

        TFDv1 tfd = new TFDv1(this.cfd,cert); // Crea un TDF a partir del CDF
        tfd.timbrar(key); // Timbra el CDF
        tfd.verificar(); // Verifica el TDF

        this.tfd = tfd;

        this.timbreFiscalDigital = tfd.getTimbre();

         // Almacenamos en un archivo temporal el CFDI timbrado
        /*
        File tempFile = new File(appConfig.getRutaRepositorio_ArchivosTemporales()
                                    + this.timbreFiscalDigital.getUUID()+".xml");
        FileOutputStream fos = new FileOutputStream(tempFile);

        tfd.guardar(fos);
         
        //Verificamos el timbre nuevamente
        if (validatePostTimbre(tempFile)) exito=true;
         */       
        return exito;

    }
    
    /**
     * Método que sella el CFD con el certificado y llave del Emisor del comprobante
     * @param certificadoCSDFile Objeto File con el archivo de Certificado CSD del Emisor
     * @param privateKeyFile Objeto File con el archivo de Llave Privada del Emisor
     * @param privateKeyPassword Password del archivo de Llave Privada del Emisor
     * @throws FileNotFoundException
     * @throws Exception
     */
    public boolean sellarCFD(File certificadoCSDFile, File privateKeyFile, String privateKeyPassword) throws FileNotFoundException, Exception{
        Configuration appConfig = new Configuration();
        boolean exito = false;

        java.security.PrivateKey key = null;
        java.security.cert.X509Certificate cert = null;
        try {
            key = KeyLoader.loadPKCS8PrivateKey(new FileInputStream(privateKeyFile),  privateKeyPassword);
            cert = KeyLoader.loadX509Certificate(new FileInputStream(certificadoCSDFile));
        }
        catch (Exception e) {
            //return null;
        }

        Comprobante sellado = cfd.sellarComprobante(key, cert); // Firma el CFD y obtiene un Comprobante sellado
        cfd.validar();
        cfd.verificar();

        this.comprobanteFiscal = sellado;
                
        return exito;

    }



    /**
     * Método para validar y verificar el timbre de un archivo XML CFDI
     * @param CfdTimbrado
     * @return Boolean indicando si el proceso fue exitoso
     */
    private boolean validatePostTimbre(File CfdTimbrado){
            boolean exito = false;
            try {
                Comprobante comp = CFDv3.newComprobante(new FileInputStream(CfdTimbrado));
                CFDv3 cfd = new CFDv3(comp);
                cfd.validar(); // Valida el XML, que todos los elementos estén presentes
                cfd.verificar(); // Verifica un CFD ya firmado

                this.comprobanteFiscal = comp;
                this.cfd = cfd;

                exito = true;
             }catch(Exception e){
                 exito = false;
            }
            return exito;
    }


    /**
     * Método para extraer el TimbreFiscalDigital de un Comprobante version CFDI 3
     * @param comp Comprobante
     * @return TimbreFiscalDigital obtenido del Comprobante
     */
    public TimbreFiscalDigital extractTFD(Comprobante comp){
        TimbreFiscalDigital tf = null;
        //Datos de TimbreFiscalDigital (Complemento CFDv3)
        for (Object itemComplemento : comp.getComplemento().getAny()){
            try{
                //Intentamos convertir el objeto que se itera a Timbre Fiscal
                tf = (TimbreFiscalDigital) itemComplemento;
                /* Una vez que obtuvimos todos los datos necesarios del
                   timbre fiscal terminamos el ciclo */
                break;
            }catch(Exception e){}
        }
        if (this.timbreFiscalDigital==null) this.timbreFiscalDigital = tf;
        return tf;
    }

    /**
     * Obtiene el certificado del emisor en formato X509Certificate
     * Funciona si el objeto Cfd3BO fue inicializado con un archivo XML válido
     * y que corresponda a un CFDI correcto
     * @return X509Certificate Certificado CSD del Emisor del Comprobante
     */
    public java.security.cert.X509Certificate getCertificateEmisor() throws Exception{
        return getCertificateEmisor(this.comprobanteFiscal);
    }

    /**
     * Obtiene el certificado del emisor en formato X509Certificate
     * @param comp Comprobante
     * @return X509Certificate Certificado CSD del Emisor del Comprobante
     */
    public java.security.cert.X509Certificate getCertificateEmisor(Comprobante comprobante) throws Exception{
        String certStr = comprobante.getCertificado();
        Base64 b64 = new Base64();
        byte[] cbs = b64.decode(certStr);
        X509Certificate cert = KeyLoader
            .loadX509Certificate(new ByteArrayInputStream(cbs));
        return cert;
    }

    public CFDv3 getCfd() {
        return cfd;
    }

    public void setCfd(CFDv3 cfd) {
        this.cfd = cfd;
    }

    public Comprobante getComprobanteFiscal() {
        return comprobanteFiscal;
    }

    public void setComprobanteFiscal(Comprobante comprobanteFiscal) {
        this.comprobanteFiscal = comprobanteFiscal;
    }

    public TimbreFiscalDigital getTimbreFiscalDigital() {
        return timbreFiscalDigital;
    }

    public void setTimbreFiscalDigital(TimbreFiscalDigital timbreFiscalDigital) {
        this.timbreFiscalDigital = timbreFiscalDigital;
    }

        public TFDv1 getTfd() {
        return tfd;
    }

}
