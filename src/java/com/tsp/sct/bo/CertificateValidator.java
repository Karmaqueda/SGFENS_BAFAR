package com.tsp.sct.bo;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;

import javax.security.cert.CertificateException;
import javax.security.cert.CertificateExpiredException;
import javax.security.cert.X509Certificate;

import org.apache.commons.io.IOUtils;
import org.apache.commons.ssl.PKCS8Key;
import org.apache.commons.ssl.ProbablyNotPKCS8Exception;

public class CertificateValidator {

	public final static int SUCCESS = 0;
	public final static int ERROR_PASSWORD = 1;
	public final static int ERROR_PRIVATE_KEY = 2;
	public final static int ERROR_PUBLIC_KEY = 3;
	public final static int ERROR_PUBLIC_PRIVATE_KEY = 4;
	public final static int ERROR_PUBLIC_PRIVATE_KEY_EXPIRIED = 5;

	private byte[] clavePrivada;
	private byte[] clavePublica;
	private String password;

        public CertificateValidator(byte[] archivoClavePrivada,
			byte[] archivoClavePublica, String password) {
		this.clavePrivada = archivoClavePrivada;
		this.clavePublica = archivoClavePublica;
		this.password = password;
	}
	/*public CertificateValidator(InputStream archivoClavePrivada,
			InputStream archivoClavePublica, String password) {
		this.clavePrivada = getBytes(archivoClavePrivada);
		this.clavePublica = getBytes(archivoClavePublica);
		this.password = password;
	/*}

	/**
	 * Valida el certificado digital, la clave privada, asi como su relacion
	 * 
	 * @return - 0 si todo esta correcto - 1 si el password de la clave privada
	 *         no es correcto - 2 si la clave privada no es un archivo que
	 *         corresponda al estandar PKCS8 - 3 si el certificado no es un
	 *         archivo que corresponda al estandar PKCS10 - 4 si el certificado
	 *         no corresponde a la clave privada - 5 si el certificado ha
	 *         expirado
	 */

	public int validate() {
		String textoAFirmar = "||2.0|FDF|28125|2007-09-12T12:47:31|11160|2007|ingreso|Pago en una sola exhibicion|"
				+ "TERMINOS CONTADO ESP|3674.13|4225.25|IMM9304016Z4|Ingram Micro Mexico S.A. de C.V.|Laguna de Terminos|249|"
				+ "Anahuac|Miguel Hidalgo|Distrito Federal|Mexico|11320|Av. 16 de Septiembre|225|San MartinXochinahuac|"
				+ "Azcapotzalco|Distrito Federal|Mexico|02140|CAOG8406274R0|CHAVEZ OCHOA GABRIEL|HDA. DE CORLOME NO. 51|"
				+ "COL. FLORESTA COYOACAN|DELG. TLALPAN|MEXICO, D.F. MX 14310|MX|3.00|"
				+ "TONER NEGRO P/LASERJET SUPL 2420 (6,000 PAG )|1189.04|3567.12|1.00|COMISION TARJETA DECREDITO|"
				+ "107.01|107.01|IVA|15.00|551.12|551.12||";
		try {
			
			PKCS8Key pkcs8 = new PKCS8Key(clavePrivada, password.toCharArray());
			PrivateKey pk = pkcs8.getPrivateKey();
			Signature firma = Signature.getInstance("SHA1withRSA");
			firma.initSign(pk);
			firma.update(textoAFirmar.getBytes("UTF-8"));
			byte[] firmado = firma.sign();
			X509Certificate cert = X509Certificate.getInstance(clavePublica);
			cert.checkValidity();
			firma.initVerify(cert.getPublicKey());
			firma.update(textoAFirmar.getBytes("UTF-8"));
			if (firma.verify(firmado)) {
				return SUCCESS;
			} else {
				return ERROR_PUBLIC_PRIVATE_KEY;
			}
		} catch (ProbablyNotPKCS8Exception e) {
			e.printStackTrace();
			return ERROR_PRIVATE_KEY;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return ERROR_PRIVATE_KEY;
		} catch (SignatureException e) {
			e.printStackTrace();
			return ERROR_PRIVATE_KEY;
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			return ERROR_PASSWORD;
		} catch (CertificateExpiredException e) {
			e.printStackTrace();
			return ERROR_PUBLIC_PRIVATE_KEY_EXPIRIED;
		} catch (CertificateException e) {
			e.printStackTrace();
			return ERROR_PUBLIC_KEY;
		} catch (IOException e) {
			e.printStackTrace();
			return ERROR_PUBLIC_KEY;
		}
	}
        
        /**
	 * Metodo que convierte un input stream con la llave privada o publica a un
	 * array de bytes
	 * 
	 * @param is
	 *            InputSteam con la clave privada
	 * @return Arreglo de bytes con la clave privada
	 */
	public static byte[] getBytes(InputStream is) {
		try {
			return IOUtils.toByteArray(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
