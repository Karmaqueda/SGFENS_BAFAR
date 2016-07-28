/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import org.apache.commons.ssl.PKCS8Key;
import sun.misc.BASE64Encoder;
import sun.security.x509.CertificateX509Key;

/**
 *
 * @author ISC César Ulises Martínez García
 */
public class UtilSecurity {

    /**
     * Método para obtener la cadena codificada en Base64 de un archivo de llave privada
     * @param privateKey Arreglo de byte del Archivo de Llave Privada (.Key)
     * @param passwordPrivateKey String que corresponde al password de la Llave Privada
     * @return String con la cadena codificada en Base64 de la Llave Privada
     * @throws GeneralSecurityException
     */
    public static String privateKeyToBase64(byte[] privateKey, String passwordPrivateKey) throws GeneralSecurityException{

        PKCS8Key pkcs8 = new PKCS8Key(privateKey, passwordPrivateKey.toCharArray());

        PrivateKey pk = pkcs8.getPrivateKey();

        /*OBTENEMOS EL STRING EN BASE 64 DEL PRIVATE KEY*/
        byte[] keyBytes = pk.getEncoded();

        // Convert key to BASE64 encoded string
        BASE64Encoder b64 = new BASE64Encoder();
        java.lang.String strPrivateKey = b64.encode(keyBytes);

        return strPrivateKey;
    }

    /**
     * M&eacute;todo que genera un hash de la cadena usando una digestión
     * con el algoritmo SHA-1
     * @param message Es el String al que se le va aplicar el hash
     * @return String
     * @throws NoSuchAlgorithmException
     */
    public static String getHash(String message) throws NoSuchAlgorithmException {
        String hash = "";
        byte[] buffer = message.getBytes();
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(buffer);
        byte[] digest = md.digest();
        for (byte aux : digest) {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) {
                hash += "0";
            }
            hash += Integer.toHexString(b);
        }
        return hash;
    }

    /**
     * Método que abre un archivo de tipo certificado y obtiene su representación
     * en objetos reconocidos por el lenguaje java
     * @param pathFile Ruta al archivo de Certificado. P. ej.: "/C:/certificados/acsat1.cer"
     * @return X509Certificate
     */
    public static java.security.cert.X509Certificate openCertificateFile(String pathFile){
        java.security.cert.X509Certificate cert = null;
        
        try {
            //Abrimos el certificado
            File cerFile = new File(pathFile);
            FileInputStream fis = new FileInputStream(cerFile);
            byte[] bytesCert = new byte[(int)cerFile.length()];
            fis.read(bytesCert);
            fis.close();

            //Creamos el objeto X509Certificate
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            cert = (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(bytesCert));
        } catch (Exception e) {
            cert = null;
        }

        return cert;
    }
    
    /**
     * M&eacute;todo que genera un hash de la cadena usando una digestión
     * con el algoritmo SHA-256
     * @param message Es el String al que se le va aplicar el hash
     * @return String
     * @throws NoSuchAlgorithmException
     */
    public static String getHashSHA256(String message) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String hash = "";
        byte[] buffer = message.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(buffer);
        byte[] digest = md.digest();
        for (byte aux : digest) {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) {
                hash += "0";
            }
            hash += Integer.toHexString(b);
        }
        return hash;
    }

}
