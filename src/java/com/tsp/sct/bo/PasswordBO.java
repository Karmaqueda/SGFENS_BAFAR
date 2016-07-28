/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.EmpresaPermisoAplicacion;
import com.tsp.sct.dao.dto.Usuarios;
import com.tsp.sct.dao.exceptions.LdapDaoException;
import com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl;
import com.tsp.sct.dao.jdbc.LdapDaoImpl;
import com.tsp.sct.dao.jdbc.UsuariosDaoImpl;
import com.tsp.sct.mail.TspMailBO;
import com.tsp.sct.util.Encrypter;
import java.sql.Connection;
import java.util.Random;

/**
 *
 * @author ISC César Ulises Martínez García
 */
public class PasswordBO {
    private String password = "";

    private static final int PASSWORD_MIN_LENGTH = 8;
    //private static final String PASSWORD_SPECIAL_CHARS = "-.,_¡!$%&/()=?¿*";
    private static final String PASSWORD_SPECIAL_CHARS = "-.,_!$%/()=*#";

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    private Connection conn = null;

    /**
     * Realiza el reestablecimiento de el password del login indicado
     * @param loginUser Cadena con el Login del usuario a reestablecer su contraseña
     * @return boolean true en caso de exito, false en caso contrario El password generado
     * se puede recuperar usando el método getPassword()
     */
    public boolean reestablecerPasswordByLogin(String loginUser){
        boolean exito = true;
        Usuarios userDto = new Usuarios();
        UsuariosDaoImpl userDao = new UsuariosDaoImpl(this.conn);
        UsuarioBO userBO = null;
        
        try {
            Usuarios[] usuarios = userDao.findWhereUserNameEquals(loginUser);
            if (usuarios.length>0){
                userDto = usuarios[0];
                userBO = new UsuarioBO(userDto.getIdUsuarios());
                
                //Calculamos password
                this.password = generateValidPassword();//getRandomPassword(8);
                
                Encrypter datoEnc =  new Encrypter();
                datoEnc.setMd5(true);////ESTA PARTE DE CODIGO ES PARA GENERAR EL md5
                String pwdCodificado = datoEnc.encodeString2(this.password);

                //
                userBO.getLdap().setPassword(pwdCodificado);
                userBO.getLdap().setFirmado(0);
                
                //Actualizamos registro
                new LdapDaoImpl(this.conn).update(userBO.getLdap().createPk(), userBO.getLdap());

                String contenidoMailRestablecimiento ="<b>Reestablecimiento de Password</b><br/><br/>Usuario: <h3>"+ userDto.getUserName()+"</h3><br/>Password Nuevo: <h3>"+ password+"</h3>";

                if (!enviarCorreoRestablecimiento(userBO, contenidoMailRestablecimiento)){
                    exito=false;
                }
            }else{
                exito = false;
            }
        } catch (Exception ex) {
            exito = false;
        }
        return exito;
    }
    
    /**
     * Realiza el reestablecimiento de el password del login indicado
     * @param loginUser Cadena con el Login del usuario a reestablecer su contraseña
     * @param passDefault Cadena con el password por defecto que se asignara
     * @return boolean true en caso de exito, false en caso contrario El password generado
     * se puede recuperar usando el método getPassword()
     */
    public boolean reestablecerPasswordByLogin(String loginUser, String passDefault){
        boolean exito = true;
        Usuarios userDto;
        UsuariosDaoImpl userDao = new UsuariosDaoImpl(this.conn);
        UsuarioBO userBO;
        
        try {
            Usuarios[] usuarios = userDao.findWhereUserNameEquals(loginUser);
            if (usuarios.length>0){
                userDto = usuarios[0];
                userBO = new UsuarioBO(userDto.getIdUsuarios());
                
                //Calculamos password
                this.password = passDefault;
                
                Encrypter datoEnc =  new Encrypter();
                datoEnc.setMd5(true);////ESTA PARTE DE CODIGO ES PARA GENERAR EL md5
                String pwdCodificado = datoEnc.encodeString2(this.password);

                //
                userBO.getLdap().setPassword(pwdCodificado);
                //userBO.getLdap().setFirmado(0);
                
                //Actualizamos registro
                new LdapDaoImpl(this.conn).update(userBO.getLdap().createPk(), userBO.getLdap());

                
            }else{
                exito = false;
            }
        } catch (Exception ex) {
            exito = false;
        }
        return exito;
    }


    /**
     * Envia un correo usando la plantilla básica para Reestablecimiento
     * @param usuario
     * @param contenidoMail
     * @return
     */
    private boolean enviarCorreoRestablecimiento(UsuarioBO usuario, String contenidoMail){
        //Validamos si es clienete de EVC
        int idEmpresa = usuario.getUser().getIdEmpresa();
        EmpresaPermisoAplicacion empresaPermisoDto = null;
        try{
            EmpresaPermisoAplicacionDaoImpl  empresaPermisoDao = new EmpresaPermisoAplicacionDaoImpl();
            empresaPermisoDto = empresaPermisoDao.findWhereIdEmpresaEquals(idEmpresa)[0];
        }catch(Exception e){e.printStackTrace();}
        
        boolean exito = false;
        try {
            TspMailBO mail = new TspMailBO();
            
            if(empresaPermisoDto.getAccesoSgfensPretomovil()==1){
               mail.setConfigurationMovilpyme(); 
               mail.addMessageMovilpyme(contenidoMail,1);
            } else{
                mail.setConfiguration();
                mail.addMessage(contenidoMail,1);
            }
            
            try {
                String correoContacto = usuario.getLdap().getEmail();
                mail.addTo(correoContacto, correoContacto);
            }catch(Exception e){}
            mail.setFrom(mail.getUSER(), mail.getFROM_NAME());            
            mail.send("Reestablecimiento de contraseña " + usuario.getUser().getUserName());
            exito=true;
        } catch (Exception ex) {
            System.out.println("No se pudo enviar el correo de Reestablecimiento. Error: "+ ex.getMessage());
            exito=false;
        }

        return exito;
    }


    /**
     * Obtiene una cadena aleatoria de longitud específicada
     * @param length Longitud de la cadena aleatoria a generar
     * @return Cadena aleatoria
     */
    public String getRandomPassword(int length){
        if (length<=0) length = 8;
        Random rdm = new Random();
        int rl = rdm.nextInt();
        String hash1 = Integer.toHexString(rl);
        String capstr = hash1.substring(0, length);

        return capstr;
    }

    /**
     * Se debe ejecutar cuando el usuario ha realizado el cambio de su contraseña
     * luego de un reestablecimiento realizado a tráves del sistema
     */
    public void actualizarCambioContrasenia(Usuarios user){
        UsuarioBO usuarioBO = new UsuarioBO(user.getIdUsuarios());
        usuarioBO.getLdap().setFirmado(1);
        try {
            new LdapDaoImpl(this.conn).update(usuarioBO.getLdap().createPk(), usuarioBO.getLdap());
        } catch (LdapDaoException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Genera una contraseña válida según las específicaciones de la matriz
     * de seguridad del SAT para PACs para generación de accesos seguros
     * @return
     */
    public String generateValidPassword() {
        String passwordGenerated = "";

        int rango = 0;
        int contador = 0;

        for (int j=0; j<PASSWORD_MIN_LENGTH; j++) {
            if (contador==0) {
                /*
                 * Generate uppercase
                 */
                rango = (int)'Z' - (int)'A';
                char randomChar = (char)((int)'A' + new Random().nextInt(rango));
                passwordGenerated += randomChar;
            }
            else if (contador==1) {
                /*
                 * Generate lowercase
                 */
                rango = (int)'z' - (int)'a';
                char randomChar = (char)((int)'a' + new Random().nextInt(rango));
                passwordGenerated += randomChar;
            }
            else if (contador==2) {
                /*
                 * Generate number
                 */
                rango = (int)'9' - (int)'0';
                char randomChar = (char)((int)'0' + new Random().nextInt(rango));
                passwordGenerated += randomChar;
            }
            else if (contador==3 && PASSWORD_SPECIAL_CHARS.length()>0) {
                /*
                 * Generate special
                 */
                rango = PASSWORD_SPECIAL_CHARS.length();
                try  {
                    char randomChar = PASSWORD_SPECIAL_CHARS.charAt(new Random().nextInt(rango));
                    passwordGenerated += randomChar;
                }
                catch (Exception e) {}
            }
            contador ++;
            if (contador>3) {
                contador = 0;
            }
        }

        return passwordGenerated;
    }

    /**
     * Verifica que una contraseña sea válida según las específicaciones de la matriz
     * de seguridad del SAT para PACs para generación de accesos seguros
     * @return true en caso de ser una contraseña válida
     * @throws Exception en caso de que la contraseña sea inválida
     */
    public boolean isValidPassword(String login, String password) throws Exception {
        /*
         * Validate password rules
         */
        boolean validPassword = false;
        boolean upperCaseLetter = false;
        boolean lowerCaseLetter = false;
        boolean numericCharacter = false;
        boolean specialCharacter = false;
        String messageError = "La contraseña debe ser de "  + PASSWORD_MIN_LENGTH + " caracteres o más de longitud, contener al menos una letra mayúscula, una letra minúscula, un número y al menos uno de los siguientes caracteres: " + PASSWORD_SPECIAL_CHARS;

        if (password!=null && password.length()>=PASSWORD_MIN_LENGTH) {

            if (login.equalsIgnoreCase(password)){
                throw new Exception ("La contraseña no puede ser igual al login");
            }

            /*
             * search for a letter
             */
            for (int j=0; j<password.length(); j++) {
                if ((int)password.charAt(j) >= (int)'A' && (int)password.charAt(j) <= (int)'Z') {
                    // has at least one UPPERCASE LETTER
                    upperCaseLetter = true;
                }
                else if ((int)password.charAt(j) >= (int)'a' && (int)password.charAt(j) <= (int)'z') {
                    // has at least one LOWERCASE LETTER
                    lowerCaseLetter = true;
                }
                else if ((int)password.charAt(j) >= (int)'0' && (int)password.charAt(j) <= (int)'9') {
                    // has at least one NUMBER
                    numericCharacter = true;
                }
                else if (PASSWORD_SPECIAL_CHARS!=null && PASSWORD_SPECIAL_CHARS.length()>0 && PASSWORD_SPECIAL_CHARS.indexOf((int)password.charAt(j)) >= 0) {
                    // has at least one SPECIAL CHAR
                    specialCharacter = true;
                }
                else {
                    throw new Exception(messageError);
                }
            }

            if (upperCaseLetter && lowerCaseLetter && numericCharacter && specialCharacter) {
                validPassword = true;
            }
            else {
                throw new Exception(messageError);
            }
        }
        if (!validPassword) {
            throw new Exception(messageError);
        }

        return validPassword;
    }

}
