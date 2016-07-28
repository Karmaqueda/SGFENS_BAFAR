/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 *
 * @author ISC César Ulises Martínez García
 */
public class Configuration {
    Properties prop = null;
    String pathPropertiesFile ="/appConfig.properties";

    String jdbc_url;
    String jdbc_user;
    String jdbc_password;
    
    //String pacRutaCertificado;
    //String pacRutaLlavePrivada;
    //String pacPasswordLlavePrivada;
    
    //String rutaRepositorio_EnvioCFDI30;
    //String rutaRepositorio_EnvioCFDI32;
    
    String rutaRepositorio_CertificadosRaizSAT;
    
    String app_content_path;
    //String ws_uri;

    String bd_sct_idconceptogenerico;
    
    String numAfiliacionBanco;
    
    String pac_ws_timbrado_url;
    String pac_ws_timbrado_user;
    String pac_ws_timbrado_pass;
    
    int segundo;
    int minuto;
    int hora;
    
    int segundoCronReporte;
    int minutoCronReporte;
    int horaCronReporte;
        
    int segundoCronTareas;
    int minutoCronTareas;
    int horaCronTareas;
    
    private String correoNotificacionNuevoUsuario;
    
    //variables para el token Key's de los cobros con tarjeta por medio de "Conekta"    
    private String conektakeyPublica;
    
    //usuario de "Banwire", cobros con tarjeta
    private String banwireUsuario;
    private String banwireSandbox;
    
    //url donde llegaran las notificaciones por "Http Post" del servicio de Banwire.
    private String banwireUrlNotificaciones;
    //urls de las solicitudes success:
    private String banwireUrlSuccessPage;
    private String banwireUrlPendingPage;
    private String banwireUrlChallengePage;
    private String banwireUrlErrorPage;
    
    private String sarWsUrl;
    
    private String qpayWsGetNumeroSesionUrl;
    private String qpayWebSingleSignOnUrl;
    
    private int minutoCronModuloSms;
    
    private String sapWsBafarUser;
    private String sapWsBafarPass;
    private String sapWsBafarUrlCreaInterlocutor;
    private String sapWsBafarUrlAceptaContrato;
    private String sapWsBafarUrlValidaListasNegras;
    private String sapWsBafarUrlConsultaDispersion;
    
    private String googleApiKeyMapJs;
    
    /**
     * Inicializa los valores de Configuración de la aplicación segun el archivo de propiedades
     */
    public Configuration(){
        prop = new Properties();
        InputStream is = null;

        try {
            //is=new FileInputStream(pathPropertiesFile);
            is = this.getClass().getResourceAsStream(pathPropertiesFile);
            prop.load(is);

            for (Enumeration e = prop.keys(); e.hasMoreElements() ; ) {
                 // Obtenemos el objeto
                 Object obj = e.nextElement();
                 String propertyToken =obj.toString();
                 if (propertyToken.equals("jdbc.url")) {
                    this.jdbc_url = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("jdbc.user")) {
                    this.jdbc_user = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("jdbc.password")) {
                    this.jdbc_password = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("PAC.rutaCertificado")) {
                    //this.pacRutaCertificado = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("PAC.rutaLlavePrivada")) {
                    //this.pacRutaLlavePrivada = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("PAC.passwordLlavePrivada")) {
                    //this.pacPasswordLlavePrivada = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("rutaRepositorio.EnvioCFDI32")) {
                    //this.rutaRepositorio_EnvioCFDI32 = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("rutaRepositorio.CertificadosRaizSAT")) {
                    this.rutaRepositorio_CertificadosRaizSAT = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("app.content.path")) {
                    this.app_content_path = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("WS.uri")) {
                    //this.ws_uri = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("bd.sct.idconceptogenerico")) {
                    this.bd_sct_idconceptogenerico = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("numero.afiliacion.banorte")) {
                    this.numAfiliacionBanco = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("PAC.WS.timbrado.url")) {
                    this.pac_ws_timbrado_url = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("PAC.WS.timbrado.user")) {
                    this.pac_ws_timbrado_user = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("PAC.WS.timbrado.pass")) {
                    this.pac_ws_timbrado_pass = prop.getProperty(propertyToken);
                }else  if (propertyToken.equals("run.cron.segundo")) {
                    this.segundo = Integer.parseInt(prop.getProperty(propertyToken));
                }else if (propertyToken.equals("run.cron.minuto")) {
                    this.minuto = Integer.parseInt(prop.getProperty(propertyToken));
                }else if (propertyToken.equals("run.cron.hora")) {
                    this.hora = Integer.parseInt(prop.getProperty(propertyToken));
                }else  if (propertyToken.equals("run.cronReportes.segundo")) {
                    this.segundoCronReporte = Integer.parseInt(prop.getProperty(propertyToken));
                }else if (propertyToken.equals("run.cronReportes.minuto")) {
                    this.minutoCronReporte = Integer.parseInt(prop.getProperty(propertyToken));
                }else if (propertyToken.equals("run.cronReportes.hora")) {
                    this.horaCronReporte = Integer.parseInt(prop.getProperty(propertyToken));
                }else  if (propertyToken.equals("run.cronTareas.segundo")) {
                    this.segundoCronTareas = Integer.parseInt(prop.getProperty(propertyToken));
                }else if (propertyToken.equals("run.cronTareas.minuto")) {
                    this.minutoCronTareas = Integer.parseInt(prop.getProperty(propertyToken));
                }else if (propertyToken.equals("run.cronTareas.hora")) {
                    this.horaCronTareas = Integer.parseInt(prop.getProperty(propertyToken));
                }else if (propertyToken.equals("correo.notificacion.usuarioNuevo")) {
                    this.correoNotificacionNuevoUsuario = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("conekta.keyPublica")) {
                    this.conektakeyPublica = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("banwire.usuario")) {
                    this.banwireUsuario = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("banwire.sandbox")) {
                    this.banwireSandbox = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("banwire.urlNotificacionPago.HttpPost")) {
                    this.banwireUrlNotificaciones = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("banwire.urlNotificacionPago.successPage")) {
                    this.banwireUrlSuccessPage = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("banwire.urlNotificacionPago.pendingPage")) {
                    this.banwireUrlPendingPage = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("banwire.urlNotificacionPago.challengePage")) {
                    this.banwireUrlChallengePage = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("banwire.urlNotificacionPago.errorPage")) {
                    this.banwireUrlErrorPage = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("sar.ws.url")) {
                    this.sarWsUrl = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("qpay.ws.getNumeroSesion.url")) {
                    this.qpayWsGetNumeroSesionUrl = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("qpay.web.singleSignOn.url")) {
                    this.qpayWebSingleSignOnUrl = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("run.cronModuloSms.minuto")) {
                    this.minutoCronModuloSms = Integer.parseInt(prop.getProperty(propertyToken));
                }else if (propertyToken.equals("sap.ws.bafar.user")) {
                    this.sapWsBafarUser = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("sap.ws.bafar.pass")) {
                    this.sapWsBafarPass = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("sap.ws.bafar.url.creainterlocutor")) {
                    this.sapWsBafarUrlCreaInterlocutor = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("sap.ws.bafar.url.aceptacontrato")) {
                    this.sapWsBafarUrlAceptaContrato = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("sap.ws.bafar.url.validalistasnegras")) {
                    this.sapWsBafarUrlValidaListasNegras = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("sap.ws.bafar.url.consultadispersion")) {
                    this.sapWsBafarUrlConsultaDispersion = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("google.api.key.map.js")) {
                    this.googleApiKeyMapJs = prop.getProperty(propertyToken);
                }
            }
        } catch(IOException ioe) {
            System.out.println("No se puedo inicializar." + ioe.getMessage());
        }
    }

    

    public String getJdbc_password() {
        return jdbc_password;
    }

    public void setJdbc_password(String jdbc_password) {
        this.jdbc_password = jdbc_password;
    }

    public String getJdbc_url() {
        return jdbc_url;
    }

    public void setJdbc_url(String jdbc_url) {
        this.jdbc_url = jdbc_url;
    }

    public String getJdbc_user() {
        return jdbc_user;
    }

    public void setJdbc_user(String jdbc_user) {
        this.jdbc_user = jdbc_user;
    }
    
    public String getApp_content_path() {
        return app_content_path;
    }

    public void setApp_content_path(String app_content_path) {
        this.app_content_path = app_content_path;
    }

    /*
    public String getPacPasswordLlavePrivada() {
        return pacPasswordLlavePrivada;
    }

    public void setPacPasswordLlavePrivada(String pacPasswordLlavePrivada) {
        this.pacPasswordLlavePrivada = pacPasswordLlavePrivada;
    }

    public String getPacRutaCertificado() {
        return pacRutaCertificado;
    }

    public void setPacRutaCertificado(String pacRutaCertificado) {
        this.pacRutaCertificado = pacRutaCertificado;
    }

    public String getPacRutaLlavePrivada() {
        return pacRutaLlavePrivada;
    }

    public void setPacRutaLlavePrivada(String pacRutaLlavePrivada) {
        this.pacRutaLlavePrivada = pacRutaLlavePrivada;
    }
    public String getWs_uri() {
        return ws_uri;
    }

    public void setWs_uri(String ws_uri) {
        this.ws_uri = ws_uri;
    }  

    public String getRutaRepositorio_EnvioCFDI32() {
        return rutaRepositorio_EnvioCFDI32;
    }

    public void setRutaRepositorio_EnvioCFDI32(String rutaRepositorio_EnvioCFDI32) {
        this.rutaRepositorio_EnvioCFDI32 = rutaRepositorio_EnvioCFDI32;
    }
*/   
    public String getRutaRepositorio_CertificadosRaizSAT() {
        return rutaRepositorio_CertificadosRaizSAT;
    }

    public void setRutaRepositorio_CertificadosRaizSAT(String rutaRepositorio_CertificadosRaizSAT) {
        this.rutaRepositorio_CertificadosRaizSAT = rutaRepositorio_CertificadosRaizSAT;
    }

    public String getBd_sct_idconceptogenerico() {
        return bd_sct_idconceptogenerico;
    }

    public void setBd_sct_idconceptogenerico(String bd_sct_idconceptogenerico) {
        this.bd_sct_idconceptogenerico = bd_sct_idconceptogenerico;
    }

    public String getNumAfiliacionBanco() {
        return numAfiliacionBanco;
    }

    public void setNumAfiliacionBanco(String numAfiliacionBanco) {
        this.numAfiliacionBanco = numAfiliacionBanco;
    }

    public String getPac_ws_timbrado_pass() {
        return pac_ws_timbrado_pass;
    }

    public void setPac_ws_timbrado_pass(String pac_ws_timbrado_pass) {
        this.pac_ws_timbrado_pass = pac_ws_timbrado_pass;
    }

    public String getPac_ws_timbrado_url() {
        return pac_ws_timbrado_url;
    }

    public void setPac_ws_timbrado_url(String pac_ws_timbrado_url) {
        this.pac_ws_timbrado_url = pac_ws_timbrado_url;
    }

    public String getPac_ws_timbrado_user() {
        return pac_ws_timbrado_user;
    }

    public void setPac_ws_timbrado_user(String pac_ws_timbrado_user) {
        this.pac_ws_timbrado_user = pac_ws_timbrado_user;
    }
    public int getSegundo() {
        return segundo;
    }
    public void setSegundo(int segundo) {
        this.segundo = segundo;
    }
    public int getMinuto() {
        return minuto;
    }
    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }
    public int getHora() {
        return hora;
    }
    public void setHora(int hora) {
        this.hora = hora;
    }
    public int getSegundoCronReporte() {
        return segundoCronReporte;
    }
    public void setSegundoCronReporte(int segundoCronReporte) {
        this.segundoCronReporte = segundoCronReporte;
    }
    public int getMinutoCronReporte() {
        return minutoCronReporte;
    }
    public void setMinutoCronReporte(int minutoCronReporte) {
        this.minutoCronReporte = minutoCronReporte;
    }
    public void setHoraCronReporte(int horaCronReporte) {
        this.horaCronReporte = horaCronReporte;
    }
    public int getHoraCronReporte() {
        return horaCronReporte;
    }

    public int getSegundoCronTareas() {
        return segundoCronTareas;
    }

    public void setSegundoCronTareas(int segundoCronTareas) {
        this.segundoCronTareas = segundoCronTareas;
    }

    public int getMinutoCronTareas() {
        return minutoCronTareas;
    }

    public void setMinutoCronTareas(int minutoCronTareas) {
        this.minutoCronTareas = minutoCronTareas;
    }

    public int getHoraCronTareas() {
        return horaCronTareas;
    }

    public void setHoraCronTareas(int horaCronTareas) {
        this.horaCronTareas = horaCronTareas;
    }
    
    public String getCorreoNotificacionNuevoUsuario() {
        return correoNotificacionNuevoUsuario;
    }

    public void setCorreoNotificacionNuevoUsuario(String correoNotificacionNuevoUsuario) {
        this.correoNotificacionNuevoUsuario = correoNotificacionNuevoUsuario;
    }  
    /**
     * @return the conektakeyPublica
     */
    public String getConektakeyPublica() {
        return conektakeyPublica;
    }

    /**
     * @param conektakeyPublica the conektakeyPublica to set
     */
    public void setConektakeyPublica(String conektakeyPublica) {
        this.conektakeyPublica = conektakeyPublica;
    }

    /**
     * @return the banwireUsuario
     */
    public String getBanwireUsuario() {
        return banwireUsuario;
    }
    /**
     * @param banwireUsuario the banwireUsuario to set
     */
    public void setBanwireUsuario(String banwireUsuario) {
        this.banwireUsuario = banwireUsuario;
    }

    /**
     * @return the banwireSandbox
     */
    public String getBanwireSandbox() {
        return banwireSandbox;
    }

    /**
     * @param banwireSandbox the banwireSandbox to set
     */
    public void setBanwireSandbox(String banwireSandbox) {
        this.banwireSandbox = banwireSandbox;
    }

    /**
     * @return the banwireUrlNotificaciones
     */
    public String getBanwireUrlNotificaciones() {
        return banwireUrlNotificaciones;
    }

    /**
     * @param banwireUrlNotificaciones the banwireUrlNotificaciones to set
     */
    public void setBanwireUrlNotificaciones(String banwireUrlNotificaciones) {
        this.banwireUrlNotificaciones = banwireUrlNotificaciones;
    }

    /**
     * @return the banwireUrlSuccessPage
     */
    public String getBanwireUrlSuccessPage() {
        return banwireUrlSuccessPage;
    }

    /**
     * @param banwireUrlSuccessPage the banwireUrlSuccessPage to set
     */
    public void setBanwireUrlSuccessPage(String banwireUrlSuccessPage) {
        this.banwireUrlSuccessPage = banwireUrlSuccessPage;
    }

    /**
     * @return the banwireUrlPendingPage
     */
    public String getBanwireUrlPendingPage() {
        return banwireUrlPendingPage;
    }

    /**
     * @param banwireUrlPendingPage the banwireUrlPendingPage to set
     */
    public void setBanwireUrlPendingPage(String banwireUrlPendingPage) {
        this.banwireUrlPendingPage = banwireUrlPendingPage;
    }

    /**
     * @return the banwireUrlChallengePage
     */
    public String getBanwireUrlChallengePage() {
        return banwireUrlChallengePage;
    }

    /**
     * @param banwireUrlChallengePage the banwireUrlChallengePage to set
     */
    public void setBanwireUrlChallengePage(String banwireUrlChallengePage) {
        this.banwireUrlChallengePage = banwireUrlChallengePage;
    }

    /**
     * @return the banwireUrlErrorPage
     */
    public String getBanwireUrlErrorPage() {
        return banwireUrlErrorPage;
    }

    /**
     * @param banwireUrlErrorPage the banwireUrlErrorPage to set
     */
    public void setBanwireUrlErrorPage(String banwireUrlErrorPage) {
        this.banwireUrlErrorPage = banwireUrlErrorPage;
    }

    public String getSarWsUrl() {
        return sarWsUrl;
    }

    public void setSarWsUrl(String sarWsUrl) {
        this.sarWsUrl = sarWsUrl;
    }

    public String getQpayWsGetNumeroSesionUrl() {
        return qpayWsGetNumeroSesionUrl;
    }

    public void setQpayWsGetNumeroSesionUrl(String qpayWsGetNumeroSesionUrl) {
        this.qpayWsGetNumeroSesionUrl = qpayWsGetNumeroSesionUrl;
    }

    public String getQpayWebSingleSignOnUrl() {
        return qpayWebSingleSignOnUrl;
    }

    public void setQpayWebSingleSignOnUrl(String qpayWebSingleSignOnUrl) {
        this.qpayWebSingleSignOnUrl = qpayWebSingleSignOnUrl;
    }

    public int getMinutoCronModuloSms() {
        return minutoCronModuloSms;
    }

    public void setMinutoCronModuloSms(int minutoCronModuloSms) {
        this.minutoCronModuloSms = minutoCronModuloSms;
    }

    public String getSapWsBafarUser() {
        return sapWsBafarUser;
    }

    public void setSapWsBafarUser(String sapWsBafarUser) {
        this.sapWsBafarUser = sapWsBafarUser;
    }

    public String getSapWsBafarPass() {
        return sapWsBafarPass;
    }

    public void setSapWsBafarPass(String sapWsBafarPass) {
        this.sapWsBafarPass = sapWsBafarPass;
    }

    public String getSapWsBafarUrlCreaInterlocutor() {
        return sapWsBafarUrlCreaInterlocutor;
    }

    public void setSapWsBafarUrlCreaInterlocutor(String sapWsBafarUrlCreaInterlocutor) {
        this.sapWsBafarUrlCreaInterlocutor = sapWsBafarUrlCreaInterlocutor;
    }

    public String getSapWsBafarUrlAceptaContrato() {
        return sapWsBafarUrlAceptaContrato;
    }

    public void setSapWsBafarUrlAceptaContrato(String sapWsBafarUrlAceptaContrato) {
        this.sapWsBafarUrlAceptaContrato = sapWsBafarUrlAceptaContrato;
    }

    public String getSapWsBafarUrlValidaListasNegras() {
        return sapWsBafarUrlValidaListasNegras;
    }

    public void setSapWsBafarUrlValidaListasNegras(String sapWsBafarUrlValidaListasNegras) {
        this.sapWsBafarUrlValidaListasNegras = sapWsBafarUrlValidaListasNegras;
    }

    public String getSapWsBafarUrlConsultaDispersion() {
        return sapWsBafarUrlConsultaDispersion;
    }

    public void setSapWsBafarUrlConsultaDispersion(String sapWsBafarUrlConsultaDispersion) {
        this.sapWsBafarUrlConsultaDispersion = sapWsBafarUrlConsultaDispersion;
    }    

    public String getGoogleApiKeyMapJs() {
        return googleApiKeyMapJs;
    }

    public void setGoogleApiKeyMapJs(String googleApiKeyMapJs) {
        this.googleApiKeyMapJs = googleApiKeyMapJs;
    }
 
}
