
package com.tsp.sct.mail;

import com.tsp.sct.dao.jdbc.ParametrosDaoImpl;
import com.tsp.sct.util.DateManage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author luis morales
 *
 * Clase utilizada para el envío de correos desde el WF
 * 
 */
public class TspMailBO {

    private String SMTP = "";
    private String PORT = "";
    private String USER = "";
    private String PASSWORD = "";
    private String FROM_NAME = "";
    private Session SESSION;
    private MimeMessage MESSAGE;
    private Multipart BODY = new MimeMultipart();
    
    /**
     * Indica si el envío se hara en un hilo aparte para no retrasar la respuesta
     * del sistema
     */
    private boolean envioAsincrono= true;

    /**
     * Constructor
     *
     * @param boolean defaultConfig - Define si se utilizará la configuración por defecto
     *
     * @throws TspSmtpConfigDaoException - En caso de error con la conexión a BD
     */
    public TspMailBO(){
        
    }

    /**
     * Devuelve el valor de PASSWORD
     * @return String PASSWORD
     */
    public String getPASSWORD() {
        return PASSWORD;
    }

    /**
     * Establece el valor de PASSWORD
     * @param String PASSWORD
     */
    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    /**
     * Devuelve el valor de PORT
     * @return String PORT
     */
    public String getPORT() {
        return PORT;
    }

    /**
     * Establece el valor de PORT
     * @param String PORT
     */
    public void setPORT(String PORT) {
        this.PORT = PORT;
    }

    /**
     * Devuelve el valor de SMTP
     * @return String SMTP
     */
    public String getSMTP() {
        return SMTP;
    }

    /**
     * Establece el valor de SMTP
     * @param String SMTP
     */
    public void setSMTP(String SMTP) {
        this.SMTP = SMTP;
    }

    /**
     * Devuelve el valor de USER
     * @return String USER
     */
    public String getUSER() {
        return USER;
    }

    /**
     * Establece el valor de USER
     * @param String USER
     */
    public void setUSER(String USER) {
        this.USER = USER;
    }

    public String getFROM_NAME() {
        return FROM_NAME;
    }

    public void setFROM_NAME(String FROM_NAME) {
        this.FROM_NAME = FROM_NAME;
    }

    /**
     * Define el remitente del mensaje
     * @param String strMail - Correo del remitente
     * @param String strMail - Nombre del remitente
     */
    public void setFrom(String strMail, String strName) throws MessagingException,UnsupportedEncodingException{
        if(MESSAGE != null)
            MESSAGE.setFrom(new InternetAddress(strMail, strName));
    }

    /**
     * Agrega un destinatario
     * @param String strMail - Correo del destinatario
     * @param String strMail - Nombre del destinatario
     */
    public void addTo(String strMail, String strName) throws MessagingException,UnsupportedEncodingException{
        if(MESSAGE != null){
            Address address[] = new Address[1];
            address[0] = new InternetAddress(strMail, strName);
            MESSAGE.addRecipients(javax.mail.Message.RecipientType.TO, address);
        }
    }

    /**
     * Agrega un destinatario CC
     * @param String strMail - Correo del destinatario
     * @param String strMail - Nombre del destinatario
     */
    public void addCC(String strMail, String strName) throws MessagingException,UnsupportedEncodingException{
        if(MESSAGE != null){
            Address address[] = new Address[1];
            address[0] = new InternetAddress(strMail, strName);
            MESSAGE.addRecipients(javax.mail.Message.RecipientType.CC, address);
        }
    }

    /**
     * Agrega un destinatario BCC
     * @param String strMail - Correo del destinatario
     * @param String strMail - Nombre del destinatario
     */
    public void addBCC(String strMail, String strName) throws MessagingException,UnsupportedEncodingException{
        if(MESSAGE != null){
            Address address[] = new Address[1];
            address[0] = new InternetAddress(strMail, strName);
            MESSAGE.addRecipients(javax.mail.Message.RecipientType.BCC, address);
        }
    }

    /**
     * Agrega un archivo al correo
     * @param String urlFile - Ruta absoluta del archivo
     * @param String fileName - Nombre del archivo
     */
    public boolean addFile(String urlFile,String fileName) throws MessagingException{
        File file = new File(urlFile);
        if(file.exists()){
            MimeBodyPart attachmentFile = new MimeBodyPart();
            attachmentFile.setDataHandler(new DataHandler(new FileDataSource(urlFile)));
            attachmentFile.setFileName(fileName);
            BODY.addBodyPart(attachmentFile, BODY.getCount());
            return true;
        }else{
            return false;
        }
    }

    /**
     * Establece el texto del cuerpo del mensaje
     * @param String strMessage - Mensaje
     */
    public void addMessage(String strMessage)throws MessagingException{
        BodyPart bodyText = new MimeBodyPart();
        bodyText.setContent(strMessage, "text/html");
        BODY.addBodyPart(bodyText);
    }

    /**
     * Establece el texto del cuerpo del mensaje con una plantilla
     * @param String strMessage - Mensaje
     */
    public void addMessage(String content, int idPlantilla)throws MessagingException{
        String strMessage ="";
        String plantillaStr ="";

        //De prueba
        plantillaStr ="<b>Pretoriano Soft. te informa:</b><br/><br/> %content%";

        
         //Invocación a DAO de Plantillas de Correos
         
        /*
         try{
            TspPlantillaDaoImpl plantillaCorreoDao = new TspPlantillaDaoImpl();
            TspPlantilla plantillaCorreoDto = plantillaCorreoDao.findByPrimaryKey(idPlantilla);
            plantillaStr = plantillaCorreoDto.getCuerpoPlantilla();
         }catch(Exception e){
         }
         */
        
        strMessage = plantillaStr.replaceAll("%content%", content);
        
        strMessage += "<br/><br/>Pretoriano Soft."
                     + "<br/>"+ DateManage.dateToStringEspanol(new Date())
                     + "<br/><br/> <i>Este es un mensaje autogenerado por sistemas informáticos, no es necesario que responda a este remitente. Favor de no responder a este correo.</i>"
                     + "<br/><hr/> Visita <a href='http://www.facturaensegundos.com'>Factura en Segundos</a> para obtener información comercial y beneficios a tu medida en Facturación Electrónica.";

        BodyPart bodyText = new MimeBodyPart();
        bodyText.setContent(strMessage, "text/html");
        BODY.addBodyPart(bodyText);
    }

    /**
     * Envía el correo a los destinatarios
     * @param String strSubject - Asunto del correo
     */
    public void send(String strSubject) throws MessagingException, Exception{
        if ("".equals(SMTP))
            setConfiguration();
        MESSAGE.setSubject(strSubject);
        MESSAGE.setContent(BODY);
        Transport transport = SESSION.getTransport("smtp");
        if (envioAsincrono){
           //Envío en hilo aparte, respuesta inmediata sin retraso
            envioAsincrono(transport);
        }else{
            //Envío en línea, (puede retrasarse de 5 segundos hasta varios minutos dependiendo del tamaño de 
            //los adjuntos)
            System.out.println("Conectando a Servidor de Correos... " + new Date());
            transport.connect(SMTP, USER, PASSWORD);
            System.out.println("Conectado a Servidor de Correos [" + SMTP + "] " + new Date());
            transport.sendMessage(MESSAGE, MESSAGE.getAllRecipients());
            System.out.println("Correo enviado. " + new Date());
            transport.close();
        }
    }
    
    /**
     * Realiza el envío asíncrono de un correo, para obtener la respuesta inmediata
     * en el sistema y no retrasar los procesos.
     * @throws MessagingException
     * @throws Exception 
     */
    public void envioAsincrono(final Transport transport) throws MessagingException, Exception{
        try{
            Runnable r1 = new Runnable() {
                public void run() {
                    try {
                        System.out.println("[Asíncrono] Conectando a Servidor de Correos... " + new Date());
                        transport.connect(SMTP, USER, PASSWORD);
                        System.out.println("[Asíncrono] Conectado a Servidor de Correos [" + SMTP + "] " + new Date());
                        transport.sendMessage(MESSAGE, MESSAGE.getAllRecipients());
                        System.out.println("[Asíncrono] Correo enviado. " + new Date());
                        transport.close();
                    } catch (MessagingException ex) {
                        System.out.println("[Asíncrono] Error al intentar enviar correo. " + ex.toString());
                        ex.printStackTrace();
                        //POR HACER: Codigo para almacenar correo no enviado y en cron aparte intentar renvío mas tarde
                    }
                }
            };
            
            Thread thr1 = new Thread(r1);
            thr1.start();
        }catch(Exception ex){
            System.out.println("Error al intentar envío asíncrono de correo. " + ex.toString());
            ex.printStackTrace();
            //POR HACER: Codigo para almacenar correo no enviado y en cron aparte intentar renvío mas tarde
            throw ex;
        }
    }

    /**
     * Establece los parámetros de configuración
     * @param TspSmtpConfig smtpConfig - Datos de configuración
     */
    public void setConfiguration() throws Exception{
            ParametrosDaoImpl parametrosDaoImpl = new ParametrosDaoImpl();
            
            this.SMTP = parametrosDaoImpl.findWhereNombreEquals("app.mail.server")[0].getDescripcion();
            this.PORT = parametrosDaoImpl.findWhereNombreEquals("app.mail.port")[0].getDescripcion();
            this.FROM_NAME = parametrosDaoImpl.findWhereNombreEquals("app.mail.from")[0].getDescripcion();
            this.USER = parametrosDaoImpl.findWhereNombreEquals("app.mail.user")[0].getDescripcion();
            this.PASSWORD = parametrosDaoImpl.findWhereNombreEquals("app.mail.password")[0].getDescripcion();

            Properties props = new Properties();
            props.put("mail.smtp.user", USER);
            props.put("mail.smtp.host", SMTP);
            props.put("mail.smtp.port", PORT);
            //if (smtpConfig.getServerOutIssmtpauthConfig()==1){
            if (false){
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.port", PORT);
                props.put("mail.smtp.socketFactory.fallback", "false");
            }else {
                props.put("mail.smtp.starttls.enable", "false");
                props.put("mail.smtp.auth", "true");
            }
            
            props.put("mail.smtp.sendpartial", "true");
            
            SESSION = Session.getInstance(props, null);
            //SESSION.setDebug(true);
            
            MESSAGE = new MimeMessage(SESSION);
            BODY = new MimeMultipart();
            
            this.setFrom(this.USER, this.FROM_NAME);
    }

    /**
     * Indica si el envío se hara en un hilo aparte para no retrasar la respuesta
     * del sistema
     */
    public boolean isEnvioAsincrono() {
        return envioAsincrono;
    }

    /**
     * Indica si el envío se hara en un hilo aparte para no retrasar la respuesta
     * del sistema
     */
    public void setEnvioAsincrono(boolean envioAsincrono) {
        this.envioAsincrono = envioAsincrono;
    }
    
    
    /**
     * Establece los parámetros de configuración para enviar correo desde movilpyme
     * @param TspSmtpConfig smtpConfig - Datos de configuración
     */
    public void setConfigurationMovilpyme() throws Exception{
            ParametrosDaoImpl parametrosDaoImpl = new ParametrosDaoImpl();
            
            this.SMTP = parametrosDaoImpl.findWhereNombreEquals("app.mail.server.movilpyme")[0].getDescripcion();
            this.PORT = parametrosDaoImpl.findWhereNombreEquals("app.mail.port.movilpyme")[0].getDescripcion();
            this.FROM_NAME = parametrosDaoImpl.findWhereNombreEquals("app.mail.from.movilpyme")[0].getDescripcion();
            this.USER = parametrosDaoImpl.findWhereNombreEquals("app.mail.user.movilpyme")[0].getDescripcion();
            this.PASSWORD = parametrosDaoImpl.findWhereNombreEquals("app.mail.password.movilpyme")[0].getDescripcion();

            Properties props = new Properties();
            props.put("mail.smtp.user", USER);
            props.put("mail.smtp.host", SMTP);
            props.put("mail.smtp.port", PORT);
            //if (smtpConfig.getServerOutIssmtpauthConfig()==1){
            if (false){
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.port", PORT);
                props.put("mail.smtp.socketFactory.fallback", "false");
            }else {
                props.put("mail.smtp.starttls.enable", "false");
                props.put("mail.smtp.auth", "true");
            }
            
            props.put("mail.smtp.sendpartial", "true");
            
            SESSION = Session.getInstance(props, null);
            //SESSION.setDebug(true);
            
            MESSAGE = new MimeMessage(SESSION);
            BODY = new MimeMultipart();
            
            this.setFrom(this.USER, this.FROM_NAME);
    }
    
    
    
    /**
     * Establece el texto del cuerpo del mensaje con una plantilla
     * @param String strMessage - Mensaje
     */
    public void addMessageMovilpyme(String content, int idPlantilla)throws MessagingException{
        String strMessage ="";
        String plantillaStr ="";

        //De prueba
        plantillaStr ="<b>Pretoriano Soft. te informa:</b><br/><br/> %content%";

        
         //Invocación a DAO de Plantillas de Correos
         
        /*
         try{
            TspPlantillaDaoImpl plantillaCorreoDao = new TspPlantillaDaoImpl();
            TspPlantilla plantillaCorreoDto = plantillaCorreoDao.findByPrimaryKey(idPlantilla);
            plantillaStr = plantillaCorreoDto.getCuerpoPlantilla();
         }catch(Exception e){
         }
         */
        
        strMessage = plantillaStr.replaceAll("%content%", content);
        
        strMessage += "<br/><br/>Pretoriano Soft."
                     + "<br/>"+ DateManage.dateToStringEspanol(new Date())
                     + "<br/><br/> <i>Este es un mensaje autogenerado por sistemas informáticos, no es necesario que responda a este remitente. Favor de no responder a este correo.</i>"
                     + "<br/><hr/> Visita <a href='http://www.movilpyme.com'>MovilPyme</a> para obtener información comercial y beneficios a tu medida en soluciones móviles que aumentaran tu productividad y ventas.";

        BodyPart bodyText = new MimeBodyPart();
        bodyText.setContent(strMessage, "text/html");
        BODY.addBodyPart(bodyText);
    }
    

}
