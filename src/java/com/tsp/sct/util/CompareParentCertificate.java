package com.tsp.sct.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.X509Certificate;
import org.bouncycastle.asn1.*;

/**
 *
 * @author ISC Cesar Martinez poseidon24@hotmail.com
 * Clase que realiza comparacion de certificados padres e hijos
 */
public class CompareParentCertificate {

    X509Certificate certFather;
    X509Certificate certSon;
    final String OIDsubjectKeyIdentifier = "2.5.29.14";
    final String OIDAuthorityKeyIdentifier = "2.5.29.35";

    public CompareParentCertificate(X509Certificate certFather, X509Certificate certSon) {
        this.certFather = certFather;
        this.certSon = certSon;
    }

    /**
     * Método que verifica si los certificados proporcionados corresponden
     * y uno es padre de otro, hace una validacion Basica
     * @return
     */
    public boolean isParentBasic() {
        boolean success = false;

            /*
             * Verificamos que el Issuer (Emisor) del certificado del emisor,
             * corresponda al Subject (Asunto) del certificado raíz (padre) del SAT
             */
            if (certFather.getSubjectDN().equals(certSon.getIssuerDN())) {
                success = true;
            }

        return success;
    }

    /**
     * Método que indica si los certificados proporcionados corresponden
     * y uno es padre de otro
     * @return
     */
    public boolean isParent() {
        boolean success = false;

        try {
            /*
             * Verificamos que el Issuer del certificado del emisor,
             * corresponda al Subject del certificado raíz (padre) del SAT
             */
            if (certFather.getSubjectDN().equals(certSon.getIssuerDN())) {

                //Del Certificado Padre obtenemos su ID de Asunto(Subject)
                String subjectKeyIDDecoded = getExtensionValue(certFather, OIDsubjectKeyIdentifier).toUpperCase().trim();
                //out.print("<br/> Subject Padre: " + subjectKeyIDDecoded + " ---- "); //For debug

                //Del Certificado hijo obtenemos el ID de su Autoridad Emisora
                String authorityKeyIDDecoded = getExtensionValue(certSon, OIDAuthorityKeyIdentifier).toUpperCase().trim();
                //out.print("<br/> Autoridad Hijo: " + authorityKeyIDDecoded); //For debug

                String authorityKeyDecodedChar40 = authorityKeyIDDecoded.substring(authorityKeyIDDecoded.length() - 40);
                //out.print("<br/> Autoridad Hijo ultimos 40: " + authorityKeyDecodedChar40); //For Debug


                /*
                 *  Comparamos si el ID de Entidad Emisora del certificado Padre corresponde
                 *  al ID del certificado Hijo
                 */
                if (subjectKeyIDDecoded.endsWith(authorityKeyDecodedChar40)) {
                    success = true;
                    //out.print("Certificado Hijo (por Claves)"); //for debug
                } else {
                    //out.print("Certificado no es Hijo, no corresponde (por Claves)"); //for debug
                    success = false;
                }

            } else {
                //out.print("Certificado no es Hijo, no corresponde"); //for debug
                success = false;
            }
        } catch (Exception e) {
        }

        return success;
    }

    /**
     * M&eacute;todo que obtiene datos del certificado del Emisor o del certificado del SAT.
     * @param X509Certificate Es el certificado que se va a utilizar.
     * @param oid Es la llave key del certificado.
     * @return String
     * @throws IOException
     */
    private String getExtensionValue(X509Certificate X509Certificate, String oid) throws IOException {
        String decoded = null;
        byte[] extensionValue = X509Certificate.getExtensionValue(oid);

        if (extensionValue != null) {
            DERObject derObject = toDERObject(extensionValue);
            if (derObject instanceof DEROctetString) {
                DEROctetString derOctetString = (DEROctetString) derObject;

                derObject = toDERObject(derOctetString.getOctets());

                try {
                    DERUniversalString s = DERUniversalString.getInstance(derObject);
                    decoded = s.getString();
                } catch (Exception e) {
                    decoded = derOctetString.toString();
                }

            }
        }
        return decoded;
    }

    /**
     * M&eacute;todo que obtiene el DERObject de un certificado
     * @param data Son lo bytes que se obtienen del certificado
     * @return DERObject
     * @throws IOException
     */
    private DERObject toDERObject(byte[] data) throws IOException {
        ByteArrayInputStream inStream = new ByteArrayInputStream(data);
        ASN1InputStream asnInputStream = new ASN1InputStream(inStream);

        return asnInputStream.readObject();
    }
}
