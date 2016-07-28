/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CertificadoDigital;
import com.tsp.sct.dao.jdbc.CertificadoDigitalDaoImpl;
import com.tsp.sct.util.FileManage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.sql.Connection;
import javax.security.cert.X509Certificate;
import mx.bigdata.sat.security.KeyLoader;

/**
 *
 * @author ISCesarMartinez
 */
public class CertificadoDigitalBO {
    
    public CertificadoDigital csd = new CertificadoDigital();    
    public void setCsd(CertificadoDigital csd) {
        this.csd = csd;
    }
    public CertificadoDigital getCsd() {
        return csd;
    }
    public String mensajeError = "";
    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }
    public String getMensajeError() {
        return mensajeError;
    }
    
    
    private CertificadoDigital certificadoDigital = null;

    public CertificadoDigital getCertificadoDigital() {
        return certificadoDigital;
    }

    public void setCertificadoDigital(CertificadoDigital certificadoDigital) {
        this.certificadoDigital = certificadoDigital;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CertificadoDigitalBO(Connection conn){
        this.conn = conn;
    }
    
    public CertificadoDigitalBO(int idCertificadoDigital, Connection conn){
        this.conn = conn;
        try{
            CertificadoDigitalDaoImpl CertificadoDigitalDaoImpl = new CertificadoDigitalDaoImpl(this.conn);
            this.certificadoDigital = CertificadoDigitalDaoImpl.findByPrimaryKey(idCertificadoDigital);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public CertificadoDigital findCertificadoDigitalbyId(int idCertificadoDigital) throws Exception{
        CertificadoDigital CertificadoDigital = null;
        
        try{
            CertificadoDigitalDaoImpl CertificadoDigitalDaoImpl = new CertificadoDigitalDaoImpl(this.conn);
            CertificadoDigital = CertificadoDigitalDaoImpl.findByPrimaryKey(idCertificadoDigital);
            if (CertificadoDigital==null){
                throw new Exception("No se encontro ningun CertificadoDigital que corresponda con los parámetros específicados.");
            }
            if (CertificadoDigital.getIdCertificadoDigital()<=0){
                throw new Exception("No se encontro ningun CertificadoDigital que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CertificadoDigital del usuario. Error: " + e.getMessage());
        }
        
        return CertificadoDigital;
    }
    
    public CertificadoDigital findCertificadoByEmpresa(int idEmpresa) throws Exception {
        CertificadoDigital certificadoDigital = null;
        
        try{
            CertificadoDigitalDaoImpl CertificadoDigitalDaoImpl = new CertificadoDigitalDaoImpl(this.conn);
            //CertificadoDigital = CertificadoDigitalDaoImpl.findByEmpresa(idEmpresa)[0];
            certificadoDigital = CertificadoDigitalDaoImpl.findByDynamicWhere("id_empresa=" + idEmpresa + " ORDER BY ID_CERTIFICADO_DIGITAL DESC", null)[0];
            if (certificadoDigital==null){
                throw new Exception("No se encontro ningun CertificadoDigital que corresponda con los parámetros específicados.");
            }
            if (certificadoDigital.getIdCertificadoDigital()<=0){
                throw new Exception("No se encontro ningun CertificadoDigital que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CertificadoDigital del usuario. Error: " + e.getMessage());
        }
        
        return certificadoDigital;
    }
    
    /**
     * Realiza una búsqueda por ID Marca en busca de
     * coincidencias
     * @param idCertificadoDigital ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public CertificadoDigital[] findCertificadosDigitales(int idCertificadoDigital, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CertificadoDigital[] certificadoDigitalDto = new CertificadoDigital[0];
        CertificadoDigitalDaoImpl certificadoDigitalDao = new CertificadoDigitalDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCertificadoDigital>0){
                sqlFiltro ="ID_CERTIFICADO_DIGITAL=" + idCertificadoDigital + " AND ";
            }else{
                sqlFiltro ="ID_CERTIFICADO_DIGITAL>0 AND";
            }
            if (idEmpresa>0){                
                sqlFiltro +=" ID_EMPRESA=" + idEmpresa;
                //sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            }else{
                sqlFiltro +=" ID_EMPRESA>0";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            certificadoDigitalDto = certificadoDigitalDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_CERTIFICADO_DIGITAL DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return certificadoDigitalDto;
    }
    
    
    public void CerificadoKeyValidacion(String repositorio, String rfcEmpresa, String certificado, String key, String pass) throws FileNotFoundException{
        byte[] bytesCer = null;
        File fileCer = null;
        try{
            fileCer = new File(repositorio+rfcEmpresa+"/"+certificado);            
            bytesCer = FileManage.getBytesFromFile(fileCer);
            java.security.cert.X509Certificate cert = KeyLoader.loadX509Certificate(new ByteArrayInputStream(bytesCer));            
            csd.setFechaCaducidad(cert.getNotAfter()); //FECHA DE CADUCIDAD
            csd.setNoCertificado(noCertificadoToString(cert.getSerialNumber()));//NUMERO DE CERTIFICADO
                        
            //PARA VALIDAR EL ERROR 306 DEL SAT, DEBE SER CSD NO FIEL
            //boolean fiel = error306(cert);
            boolean fiel = false;
            if(fiel==true){
                System.out.println("ES UNA FIEL, DEBE SER UN CSD");
                mensajeError = mensajeError + "Su certificado es una FIEL, debe ser un CSD. ";
            }
        }catch (Exception io){
            System.out.println("ERROR EN CERTIFICADO: "+io.getMessage());
        }
            
            //PARA VALIDAR ERRORES POSIBLES DEL CERTIFICADO, KEY Y PASS:        
        //PARA EL CER
         InputStream inputCer = new FileInputStream (fileCer);
        
        //PARA LA KEY
        InputStream inputKey = null;
        byte[] bytesKey = null;
        try{
        File fileKey = new File(repositorio+rfcEmpresa+"/"+key);
        bytesKey = FileManage.getBytesFromFile(fileKey);
        }catch(Exception e){
            System.out.println("ERROR EN CONVERTIR BYTES A INPUTSTREAM KEY: "+e.getMessage());
        }
        

        //CertificateValidator validator = new CertificateValidator(inputKey,inputCer, pass);
        CertificateValidator validator = new CertificateValidator(bytesKey,bytesCer, pass);
	int validacion = validator.validate();
        StringBuffer msje = new StringBuffer();
        //ESTE IF SI CUMPLE CON TODO REALIZAMOS EL INSERT DE LOS DATOS EN LA BASE
        if (validacion == CertificateValidator.SUCCESS || validacion == CertificateValidator.ERROR_PUBLIC_KEY) {                       
            CertificadoDigital certific = new CertificadoDigital();
            //PASARON TODAS LAS VALIDACIONES            
	} else if (validacion == CertificateValidator.ERROR_PASSWORD) { 
            mensajeError = mensajeError + "El password de la llave privada (.key) es incorrecto. ";
            msje.append("El password es incorrecto\n");
	} else if (validacion == CertificateValidator.ERROR_PRIVATE_KEY) {        
            mensajeError = mensajeError + "La llave privada no tiene el formato correcto";
            msje.append("La clave privada no tiene el formato correcto\n");
	} else if (validacion == CertificateValidator.ERROR_PRIVATE_KEY) {            
            mensajeError = mensajeError + "El certificado no tiene el formtato correcto. ";
            msje.append("El certificado no tiene el formtato correcto\n");
	} else if (validacion == CertificateValidator.ERROR_PUBLIC_PRIVATE_KEY) {            
            mensajeError = mensajeError + "La llave privada no corresponde al certificado. ";
            msje.append("La clave privada no corresponde al certificado\n");
	} else if (validacion == CertificateValidator.ERROR_PUBLIC_PRIVATE_KEY_EXPIRIED) {            
            mensajeError = mensajeError + "EL Certificado ha caducado. ";
            msje.append("La clave privada ha caducado\n");
	}
        System.out.println("ERRORES ENCONTRADOS EN LA CARGA DEL CERTIFICADO Y KEY: "+msje);        
        
    }
    
    /**
     * Realiza el formato del numero de certificado
     * 
     * @return String
     */
    public static String noCertificadoToString(BigInteger noCertificadoHex) {
        String hexa = noCertificadoHex.toString(16);
	String resultado = "";
	while (hexa.length() > 0) {
            resultado += String.valueOf((char) Integer.parseInt(hexa.substring(0, 2), 16));
            hexa = hexa.substring(2, hexa.length());
	}
        return resultado;
    }
    
    /**
     * Válida el error típificado por el SAT como:
     * 306
     * El certificado no es de tipo CSD
     * Que la llave utilizada para sellar corresponda a un CSD (no de FIEL)
     * @return Boolean en Flag true si se detecto el error
     */
    private boolean error306(java.security.cert.X509Certificate cert) {
        boolean existError = false;
        String msgErrorSAT;
		try {
            if (!cert.getSubjectDN().toString().contains("OU=")) {
                msgErrorSAT = "306 - El certificado no es de tipo CSD. Que la llave utilizada para sellar corresponda a un CSD (no de FIEL).";                
                existError = true;
            }
        } catch (Exception ex) {
            existError = true;
            msgErrorSAT = "306 - El certificado no es de tipo CSD. Que la llave utilizada para sellar corresponda a un CSD (no de FIEL)";            
        }
        return existError;
    }
    
}