/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.cfdi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ISC César Ulises Martínez García
 * Clase general de validaciones requeridas por el SAT
 */
public class ValidaCfdSAT {
    //Objeto Cfd3BO

    Cfd3BO cfd3BO = null;
    /**
     * Mensaje de Error que típica el SAT para mostrar a los usuarios
     */
    String msgErrorSAT = "";
    /**
     * Mensaje de Error con la descripción de los problemas durante la ejecución
     */
    String msgErrorJava = "";
    /**
     * Numero de Error estándar del SAT
     */
    String numErrorSAT ="";


    /**
     * Flag para indicar si la validación tuvo éxito
     */
    Boolean exitoValidacion = false;

    public ValidaCfdSAT(Cfd3BO cfd3) {
        this.cfd3BO = cfd3;
    }

    /**
     * Método general para invocar las validaciones al comprobante
     * @return boolean en true si supero todas las validaciones, false en caso contrario
     * En caso de no superar la validación, las causas se pueden consultar usando las propiedades:
     * msgErrorSAT, msgErrorJava y numErrorSAT a tráves de sus métodos get publicos
     */
    public boolean validar() {
        exitoValidacion = true;
        try {
            System.out.println(cfd3BO.cfd.getCadenaOriginal());
        } catch (Exception ex) {
            //Logger.getLogger(ValidaCfdSAT.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (error301())  { numErrorSAT = "301"; exitoValidacion =false;}
        else if (error307()) { numErrorSAT = "307"; exitoValidacion = false;}
        else if (error403()) { numErrorSAT = "403"; exitoValidacion = false;}
        else if (error401()) { numErrorSAT = "401"; exitoValidacion = false;}
        else if (error305()) { numErrorSAT = "305"; exitoValidacion = false;} //LCO
        else if (error308()) { numErrorSAT = "308"; exitoValidacion = false;}
        else if (error306()) { numErrorSAT = "306"; exitoValidacion = false;}
        else if (error402()) { numErrorSAT = "402"; exitoValidacion = false;} //LCO
        else if (error303()) { numErrorSAT = "303"; exitoValidacion = false;}
        else if (error304()) { numErrorSAT = "304"; exitoValidacion = false;} //LCO
        else if (error302()) { numErrorSAT = "302"; exitoValidacion = false;}
        
        return exitoValidacion;
    }

    /**
     * Válida el error típificado por el SAT como:
     * 301
     * XML mal formado
     * Que cumpla con el estándar de XML (Conforme al W3C) y con la estructura XML (XSD y complementos aplicables).
     * @return Boolean en Flag true si se detecto el error
     */
    private boolean error301() {
        boolean existError = false;
        try {
            // Valida el XML, que todos los elementos estén presentes
            cfd3BO.getCfd().validar();
        } catch (Exception ex) {
            existError = true;
            msgErrorSAT = "301 - XML mal formado. Que cumpla con el estándar de XML (Conforme al W3C) y con la estructura XML (XSD y complementos aplicables).";
            msgErrorJava = ex.getLocalizedMessage();
        }

        return existError;
    }

    /**
     * Válida el error típificado por el SAT como:
     * 302
     * Sello mal formado o inválido
     * Que el sello del emisor sea válido.
     * @return Boolean en Flag true si se detecto el error
     */
    private boolean error302() {
        boolean existError = false;
        try {
            // Verifica que el sello del emisor sea válido
            cfd3BO.getCfd().verificar();
        } catch (Exception ex) {
            existError = true;
            msgErrorSAT = "302 - Sello mal formado o inválido. Que el sello del emisor sea válido.";
            msgErrorJava = ex.getMessage();
        }

        return existError;
    }

    /**
     * Válida el error típificado por el SAT como:
     * 303
     * Sello no corresponde a emisor o caduco
     * Que el CSD del emisor corresponda al RFC que viene como emisor en el comprobante.
     * @return Boolean en Flag true si se detecto el error
     */
    private boolean error303() {
        boolean existError = false;
        try {
            // Verifica que el sello del emisor sea válido
            if (!cfd3BO.getCertificateEmisor().getSubjectDN().getName().toUpperCase().contains(cfd3BO.getComprobanteFiscal().getEmisor().getRfc().toUpperCase())) {
                existError = true;
                msgErrorSAT = "303 - Sello no corresponde a emisor o caduco. Que el CSD del emisor corresponda al RFC que viene como emisor en el comprobante.";
            }
        } catch (Exception ex) {
            existError = true;
            msgErrorSAT = "303 - Sello no corresponde a emisor o caduco. Que el CSD del emisor corresponda al RFC que viene como emisor en el comprobante.";
            msgErrorJava = ex.getMessage();
        }

        return existError;
    }

    /**
     * Válida el error típificado por el SAT como:
     * 304
     * Certificado revocado o caduco
     * Que el CSD del Emisor no haya sido revocado utilizando la lista de LCO
     * @return Boolean en Flag true si se detecto el error
     */
    private boolean error304() {
        boolean existError = false;
        try {
            String numeroSerieCert = String.valueOf(cfd3BO.getCertificateEmisor().getSerialNumber());

            //Buscamos en la BD/WebService/Lista LCO por numero de serie del certificado
            /* Leo
             * resultadoConsulta = dao.findWhereNumeroCertificado(numeroSerieCert);
             * if (!resultadoConsulta.getEstatusCertificado == "A") {
             *      msgErrorSAT ="304 - Certificado revocado o caduco. Que el CSD del Emisor no haya sido revocado utilizando la lista de LCO.";
             *      existError = true;
             * }
             */
        } catch (Exception ex) {
            existError = true;
            msgErrorSAT = "304 - Certificado revocado o caduco. Que el CSD del Emisor no haya sido revocado utilizando la lista de LCO.";
            msgErrorJava = ex.getMessage();
        }

        return existError;
    }

    /**
     * Válida el error típificado por el SAT como:
     * 305
     * La fecha de emisión no esta dentro de la vigencia del CSD del Emisor
     * @return Boolean en Flag true si se detecto el error
     */
    private boolean error305() {
        boolean existError = false;
        try {

            String numeroSerieCert = String.valueOf(cfd3BO.getCertificateEmisor().getSerialNumber());
            cfd3BO.getCertificateEmisor().checkValidity(cfd3BO.getComprobanteFiscal().getFecha());
            //Buscamos en la BD/WebService/Lista LCO por numero de serie del certificado
            /* Leo
             * resultadoConsulta = dao.findWhereNumeroCertificado(numeroSerieCert);
             * if (((resultadoConsulta.getFechaInicio().getTime()) >= (cfd3BO.getComprobanteFiscal().getFecha().getTime()))
            || ((cfd3BO.getComprobanteFiscal().getFecha().getTime()) >= (resultadoConsulta.getFechaFinal().getTime()))) {
             *      msgErrorSAT ="305 - La fecha de emisión no esta dentro de la vigencia del CSD del Emisor.";
             *      existError = true;
             * }
             */
        } catch (Exception ex) {
            existError = true;
            msgErrorSAT = "305 - La fecha de emisión no esta dentro de la vigencia del CSD del Emisor.";
            msgErrorJava = ex.getMessage();
        }

        return existError;
    }

    /**
     * Válida el error típificado por el SAT como:
     * 306
     * El certificado no es de tipo CSD
     * Que la llave utilizada para sellar corresponda a un CSD (no de FIEL)
     * @return Boolean en Flag true si se detecto el error
     */
    private boolean error306() {
        boolean existError = false;
        try {
            if (!cfd3BO.getCertificateEmisor().getSubjectDN().toString().contains("OU=")) {
                msgErrorSAT = "306 - El certificado no es de tipo CSD. Que la llave utilizada para sellar corresponda a un CSD (no de FIEL).";
                existError = true;
            }
        } catch (Exception ex) {
            existError = true;
            msgErrorSAT = "306 - El certificado no es de tipo CSD. Que la llave utilizada para sellar corresponda a un CSD (no de FIEL)";
            msgErrorJava = ex.getMessage();
        }

        return existError;
    }

    /**
     * Válida el error típificado por el SAT como:
     * 307
     * El CFDI contiene un timbre previo
     * Que no tenga un timbre previo
     * @return Boolean en Flag true si se detecto el error
     */
    private boolean error307() {
        boolean existError = false;
        try {
            if (cfd3BO.extractTFD(cfd3BO.getComprobanteFiscal())!=null || cfd3BO.getTimbreFiscalDigital() != null) {
                msgErrorSAT = "307 - El CFDI contiene un timbre previo. Que no tenga un timbre previo";
                existError = true;
            }
        } catch (Exception ex) {
            existError = true;
            msgErrorSAT = "307 - El CFDI contiene un timbre previo. Que no tenga un timbre previo";
            msgErrorJava = ex.getMessage();
        }

        return existError;
    }

    /**
     * Válida el error típificado por el SAT como:
     * 308
     * Certificado no expedido por el SAT
     * Que el CSD del emisor haya sido firmado por uno de los Certificados de Autoridad del SAT
     * @return Boolean en Flag true si se detecto el error
     */
    private boolean error308() {
        boolean existError = false;
        
        /*Codigo para pruebas PAC*/
        /*
        java.security.cert.X509Certificate CertSATParent = null;
        java.security.cert.X509Certificate CertEmisor = null;

        try {
            //Recuperamos los datos de configuracion de la aplicación definidos en archivo .properties
            Configuration appConfig = new Configuration();
            //Recuperamos el Certificado Padre del SAT del servidor
            CertSATParent = UtilSecurity.openCertificateFile(appConfig.getRutaCertificadoPadreSAT());
            //Recuperamos el Certificado del Emisor del Comprobante recibido
            CertEmisor = cfd3BO.getCertificateEmisor();

            //Inicializamos un comparador de Certificados con parentezco
            compareParentCertificate cParentCert = new compareParentCertificate(CertSATParent, CertEmisor);
            if (!cParentCert.isParent()) {
                msgErrorSAT = "308 - Certificado no expedido por el SAT. Que el CSD del emisor haya sido firmado por uno de los Certificados de Autoridad del SAT";
                existError = true;
            }
        } catch (Exception ex) {
            existError = true;
            msgErrorSAT = "308 - Certificado no expedido por el SAT. Que el CSD del emisor haya sido firmado por uno de los Certificados de Autoridad del SAT";
            msgErrorJava = ex.getMessage();
        }
         */

        ValidaError308 validaError308 = new ValidaError308(cfd3BO);
        try{
            if (validaError308.error308()){
                existError = true;
                msgErrorSAT = "308 - Certificado no expedido por el SAT. Que el CSD del emisor haya sido firmado por uno de los Certificados de Autoridad del SAT";
            }
        } catch (Exception ex) {
            existError = true;
            msgErrorSAT = "308 - Certificado no expedido por el SAT. Que el CSD del emisor haya sido firmado por uno de los Certificados de Autoridad del SAT";
            msgErrorJava = ex.getMessage();
        }

        return existError;
    }

    /**
     * Válida el error típificado por el SAT como:
     * 401
     * Fecha y hora de generación fuera de rango
     * Que el rango de la fecha de generación no sea mayor a 72 horas
     * para la emisión del timbre
     * @return Boolean en Flag true si se detecto el error
     */
    private boolean error401() {
        boolean existError = false;
        //Obtenemos la diferencia en segundos
        long dateNow = new Date().getTime();
        long fechaComprobante = cfd3BO.getComprobanteFiscal().getFecha().getTime();
        long diferencia = (dateNow - fechaComprobante) / (1000 * 60 * 60);


        if (diferencia > 72) {
            existError = true;
            msgErrorSAT = "401 - Fecha y hora de generación fuera de rango. Que el rango de la fecha de generación no sea mayor a 72 horas.";
        }
        return existError;
    }

    /**
     * Válida el error típificado por el SAT como:
     * 402
     * RFC del emisor no se encuentra en el régimen de contribuyentes
     * Que exista el RFC del emisor conforme al régimen autorizado
     * (Listado de validación de régimen) LCO
     * @return Boolean en Flag true si se detecto el error
     */
    private boolean error402() {
        boolean existError = false;
        try {
            String rfcEmisor = cfd3BO.comprobanteFiscal.getEmisor().getRfc();
            //Buscamos en la BD/WebService/Lista LCO por emisor
            /* Leo
             * resultadoConsulta = dao.findWhereRFCCertificado(rfcEmisor);
             * if (resultadoConsulta == null){
             *      msgErrorSAT ="402 - RFC del emisor no se encuentra en el régimen de contribuyentes. Que exista el RFC del emisor conforme al régimen autorizado (Listado de validación de régimen) LCO.";
             *      existError = true;
             * }
             */
        } catch (Exception ex) {
            existError = true;
            msgErrorSAT = "402 - RFC del emisor no se encuentra en el régimen de contribuyentes. Que exista el RFC del emisor conforme al régimen autorizado (Listado de validación de régimen) LCO.";
            msgErrorJava = ex.getMessage();
        }
        return existError;
    }

    /**
     * Válida el error típificado por el SAT como:
     * 403
     * La fecha de emisión no es posterior al 01 de enero 2011
     * Que la fecha de emisión sea posterior al 01 de enero 2011
     * @return Boolean en Flag true si se detecto el error
     */
    private boolean error403() {
        boolean existError = false;
        try {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaLimite = df.parse("01/01/2011");

            if (cfd3BO.getComprobanteFiscal().getFecha().before(fechaLimite)) {
                msgErrorSAT = "403 - La fecha de emisión no es posterior al 01 de enero 2011. Que la fecha de emisión sea posterior al 01 de enero 2011.";
                existError = true;
            }
        } catch (Exception ex) {
            existError = true;
            msgErrorSAT = "403 - La fecha de emisión no es posterior al 01 de enero 2011. Que la fecha de emisión sea posterior al 01 de enero 2011.";
            msgErrorJava = ex.getMessage();
        }
        return existError;
    }

    public String getMsgErrorJava() {
        return msgErrorJava;
    }

    public void setMsgErrorJava(String msgError) {
        this.msgErrorJava = msgError;
    }

    public Cfd3BO getCfd3BO() {
        return cfd3BO;
    }

    public void setCfd3BO(Cfd3BO cfd3BO) {
        this.cfd3BO = cfd3BO;
    }

    public String getMsgErrorSAT() {
        return msgErrorSAT;
    }

    public void setMsgErrorSAT(String msgErrorSAT) {
        this.msgErrorSAT = msgErrorSAT;
    }

    public Boolean getExitoValidacion() {
        return exitoValidacion;
    }

    public void setExitoValidacion(Boolean exitoValidacion) {
        this.exitoValidacion = exitoValidacion;
    }

    public String getNumErrorSAT() {
        return numErrorSAT;
    }

    public void setNumErrorSAT(String numErrorSAT) {
        this.numErrorSAT = numErrorSAT;
    }
}
