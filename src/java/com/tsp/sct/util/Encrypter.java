/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author ISCesarMartinez
 */
public class Encrypter {
    
    /**
     * Indica si se va  a encriptar usando el algoritmo de digestion MD5
     */
    private boolean md5 = false;
    
    public String decodeString(String encodedString) throws IOException{
        String decoded ="";
        BASE64Decoder b64 = new BASE64Decoder();
        
        byte[] abs=b64.decodeBuffer(encodedString);
        decoded = new String(abs);
       
        return decoded;
    }
    
    
    public String encodeString2(String stringToEncode) throws IOException{
            String stringEncoded = "";
            byte[] message = stringToEncode.getBytes();
            try {
                    if(isMd5()){
                            MessageDigest md5 = MessageDigest.getInstance("MD5");
                            md5.reset();
                            md5.update(message);
                            message = md5.digest();
                    }
                    BASE64Encoder b64 = new BASE64Encoder();
                    stringEncoded = b64.encode(message);

            } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
            }
            return stringEncoded;
    }
    
    public boolean isMd5() {
        return md5;
    }

    public void setMd5(boolean md5) {
        this.md5 = md5;
    }
    
}
