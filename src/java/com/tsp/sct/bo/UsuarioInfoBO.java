
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.DatosUsuario;
import com.tsp.sct.dao.dto.Ldap;
//import com.tsp.microfinancieras.dto.SgfensVendedorDatos;
import com.tsp.sct.dao.dto.Usuarios;
import com.tsp.sct.dao.exceptions.UsuariosDaoException;
import com.tsp.sct.dao.jdbc.DatosUsuarioDaoImpl;
import com.tsp.sct.dao.jdbc.LdapDaoImpl;
//import com.tsp.microfinancieras.jdbc.SgfensVendedorDatosDaoImpl;
import com.tsp.sct.dao.jdbc.UsuariosDaoImpl;
import com.tsp.sct.util.Encrypter;
import java.sql.Connection;
//import java.util.logging.Level;
//import java.util.logging.Logger;

/**
 *
 * @author ISC César Ulises Martínez García
 */
public class UsuarioInfoBO {

    private Usuarios user = null;
    private Ldap ldap = null;
    private DatosUsuario datosUsuario = null;
    
    /**
     * No es obligatorio que todos los usuarios tengan estos datos,
     * por lo tanto puede tener valor NULO
     */
    /*private SgfensVendedorDatos datosVendedor = null;

    public SgfensVendedorDatos getDatosVendedor() {
        return datosVendedor;
    }

    public void setDatosVendedor(SgfensVendedorDatos datosVendedor) {
        this.datosVendedor = datosVendedor;
    } */   
    
    public Ldap getLdap() {
        return ldap;
    }

    public void setLdap(Ldap ldap) {
        this.ldap = ldap;
    }

    /**
     * Retorna el valor de la variable user
     * @return Objeto tipo User con todos los datos obtenidos desde BD
     */
    public Usuarios getUser() {
        return user;
    }

    /**
     * Asigna valor a la variable user
     * @param user Objeto de tipo User
     */
    public void setUser(Usuarios usuario) {
        this.user = usuario;
    }

    public DatosUsuario getDatosUsuario() {
        return datosUsuario;
    }

    public void setDatosUsuario(DatosUsuario datosUsuario) {
        this.datosUsuario = datosUsuario;
    }

    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    /**
     * Constructor vacío
     */
    public UsuarioInfoBO(Connection conn) {
        this.conn = conn;

    }

    /**
     * Constructor, crea un objeto User a tráves de su ID almacenado en la Base de Datos
     * @param idUser Identificador único autoincrementable de usuario
     */
    public UsuarioInfoBO(int idUsuarios, Connection conn) {
        this.conn = conn;
        try {
            user = new UsuariosDaoImpl(this.conn).findByPrimaryKey(idUsuarios);
            Ldap[] userValidated = new LdapDaoImpl(this.conn).findWhereUsuarioEquals(user.getUserName());
            ldap = userValidated[0];
            datosUsuario = new DatosUsuarioDaoImpl(this.conn).findByPrimaryKey(user.getIdDatosUsuario());
            
            //datosVendedor = new SgfensVendedorDatosDaoImpl(this.conn).findByPrimaryKey(user.getIdUsuarios());
        } catch (Exception ex) {
            //Logger.getLogger(UsuarioBO.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Se intento buscar un usuario con el ID: " + idUsuarios + " y no fue encontrado en la BD");
        }
    }

    /**
     * Constructor, asigna a la variable User local el objeto del mismo tipo que recibe como párametro
     * @param user Objeto de tipo User con todos los datos requeridos
     */
    public UsuarioInfoBO(Usuarios usuario, Connection conn) {
        this.user = usuario;
        this.conn = conn;
    }

    /**
     * Método para validar el Login de un usuario comparando el nick y password
     * contra los almacenados en la base de datos
     * @param loginUser Login, nick del usuario
     * @param pwdUser Password, contraseña del usuario
     * @return Boolean que indica si fue exitoso o no
     */
    public boolean login(String userName, String pwdUser) throws Exception {

        boolean validate = false;

        try {
            //Usuarios[] userValidated = new UsuariosDaoImpl(this.conn).findWhereUserNameEquals(userName);
            Ldap[] userValidated = new LdapDaoImpl(this.conn).findWhereUsuarioEquals(userName);
            if (userValidated.length==0) {
                user = null;
            }else {
                for(int i=0;i<userValidated.length;i++){
                    Encrypter encriptacion =  new Encrypter();
                    encriptacion.setMd5(true);
                    //if(userValidated[i].getPassword().equals(new TspUtilBO().getHash(pwdUser))){
                    if(userValidated[i].getPassword().equals(encriptacion.encodeString2(pwdUser))){
                        Usuarios[] userValido = new UsuariosDaoImpl(this.conn).findWhereUserNameEquals(userName);
                        user = userValido[0];
                        
                        if (user.getIdEstatus()==2){
                            //Si el usuario esta inhabilitado
                            throw  new Exception("El Usuario esta deshabilitado.");
                        }
                        
                        ldap = userValidated[i];
                        datosUsuario = new DatosUsuarioDaoImpl(this.conn).findByPrimaryKey(user.getIdDatosUsuario());
                        
                        //datosVendedor = new SgfensVendedorDatosDaoImpl(this.conn).findByPrimaryKey(user.getIdUsuarios());
                        
                        validate = true;
                    }
                }
            }

        } catch (Exception ex) {
            System.out.println("Error al intentar validar el acceso de un usuario");
            ex.printStackTrace();
            throw  new Exception(ex.getMessage());
            //return false;
        }
        return validate;
    }

    public boolean requirePasswordChange(){
        boolean requireChange = false;
        try {
            if (ldap.getFirmado()==0)
                requireChange = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return requireChange;
    }
    
    /**
     * Método para verificar si el usuario que esta en sesion
     * tiene acceso al topico (página) segun los permisos
     * configurados por su rol y por el administrador
     * @param url La URL de donde proviene la llamada, se puede usar el siguiente
     * código para obtener la URL correcta:
     * request.getRequestURI().replace(request.getContextPath(), "")
     * @return boolean que indica si el usuario tiene acceso o no al tópico
     */
    public boolean permissionToTopicByURL(String url) {

        /*
        TspTopic[] topic = new TspTopic[0];
        try {
            topic = new TspTopicDaoImpl(this.conn).findWhereUrlTopicEquals(url);

        } catch (TspTopicDaoException ex) {
            Logger.getLogger(UserBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (topic.length>0) {
            return permissionToTopic(topic[0].getIdTopic());
        }
        else {
            //Si no se encontro el tópico en la base de datos
                //return false;
            //le damos acceso al usuario
            if (this.getUser()==null) {
                return false;
            }
            return true;
        }
         * */
        if (this.getUser()==null) {
                return false;
        }
        
        return true;
    }
}
