
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.DatosUsuario;
import com.tsp.sct.dao.dto.Ldap;
import com.tsp.sct.dao.dto.SgfensVendedorDatos;
import com.tsp.sct.dao.dto.Usuarios;
import com.tsp.sct.dao.exceptions.UsuariosDaoException;
import com.tsp.sct.dao.jdbc.DatosUsuarioDaoImpl;
import com.tsp.sct.dao.jdbc.LdapDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.dao.jdbc.SgfensVendedorDatosDaoImpl;
import com.tsp.sct.dao.jdbc.UsuariosDaoImpl;
import com.tsp.sct.util.Encrypter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java.util.logging.Level;
//import java.util.logging.Logger;

/**
 *
 * @author ISC César Ulises Martínez García
 */
public class UsuarioBO {

    private Usuarios user = null;
    private Ldap ldap = null;
    private DatosUsuario datosUsuario = null;
    
    private boolean loginByEmpleado = false;
    
    /**
     * No es obligatorio que todos los usuarios tengan estos datos,
     * por lo tanto puede tener valor NULO
     */
    private SgfensVendedorDatos datosVendedor = null;
    
    private Connection conn = null;
    
    //ID de Ultimo Registro Creado (puede ser generico, para cualquier tabla)
    private int ultimoRegistroID = 0;
    
    //Flag para almacenar en sesion la fecha de ultimo acceso del usuario, previo a la sesion actual
    private Date fechaAccesoAnterior = null;
    // Flag para indicar si el usuario tiene permiso a ver todo el menu lateral 
    // para acceder a los diferentes modulos y opciones
    // Por defecto cualquier usuario puede ver el menu
    private boolean permisoVerMenu = true;
    
    private QPayData qPayData = null;
    
    private ConceptoBO.Ordenamiento ordenamientoConceptos;

    public SgfensVendedorDatos getDatosVendedor() {
        return datosVendedor;
    }

    public void setDatosVendedor(SgfensVendedorDatos datosVendedor) {
        this.datosVendedor = datosVendedor;
    }    
    
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

    public QPayData getqPayData() {
        return qPayData;
    }

    public void setqPayData(QPayData qPayData) {
        this.qPayData = qPayData;
    }

    /**
     * Constructor vacío
     */
    public UsuarioBO() {

    }

    /**
     * Constructor, crea un objeto User a tráves de su ID almacenado en la Base de Datos
     * @param idUser Identificador único autoincrementable de usuario
     */
    public UsuarioBO(int idUsuarios) {
        try {
            user = new UsuariosDaoImpl(this.conn).findByPrimaryKey(idUsuarios);
            Ldap[] userValidated = new LdapDaoImpl(this.conn).findWhereUsuarioEquals(user.getUserName());
            ldap = userValidated[0];
            datosUsuario = new DatosUsuarioDaoImpl(this.conn).findByPrimaryKey(user.getIdDatosUsuario());
            
            datosVendedor = new SgfensVendedorDatosDaoImpl(this.conn).findByPrimaryKey(user.getIdUsuarios());
        } catch (Exception ex) {
            //Logger.getLogger(UsuarioBO.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Se intento buscar un usuario con el ID: " + idUsuarios + " y no fue encontrado en la BD");
        }
    }
    
    /**
     * Constructor, crea un objeto User a tráves de su ID almacenado en la Base de Datos
     * @param idUser Identificador único autoincrementable de usuario
     */
    public UsuarioBO(Connection conn, int idUsuarios) {
        this.conn = conn;
        try {
            user = new UsuariosDaoImpl(this.conn).findByPrimaryKey(idUsuarios);
            Ldap[] userValidated = new LdapDaoImpl(this.conn).findWhereUsuarioEquals(user.getUserName());
            ldap = userValidated[0];
            datosUsuario = new DatosUsuarioDaoImpl(this.conn).findByPrimaryKey(user.getIdDatosUsuario());
            
            datosVendedor = new SgfensVendedorDatosDaoImpl(this.conn).findByPrimaryKey(user.getIdUsuarios());
        } catch (Exception ex) {
            //Logger.getLogger(UsuarioBO.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Se intento buscar un usuario con el ID: " + idUsuarios + " y no fue encontrado en la BD");
        }
    }

    /**
     * Constructor, asigna a la variable User local el objeto del mismo tipo que recibe como párametro
     * @param user Objeto de tipo User con todos los datos requeridos
     */
    public UsuarioBO(Usuarios usuario) {
        this.user = usuario;
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
            Ldap[] userValidated = new LdapDaoImpl(getConn()).findWhereUsuarioEquals(userName);
            if (userValidated.length==0) {
                user = null;
            }else {
                for(int i=0;i<userValidated.length;i++){
                    Encrypter encriptacion =  new Encrypter();
                    encriptacion.setMd5(true);
                    //if(userValidated[i].getPassword().equals(new TspUtilBO().getHash(pwdUser))){
                    if(userValidated[i].getPassword().equals(encriptacion.encodeString2(pwdUser))){
                        Usuarios[] userValido = new UsuariosDaoImpl(getConn()).findWhereUserNameEquals(userName);
                        user = userValido[0];
                        
                        if (user.getIdEstatus()==2){
                            //Si el usuario esta inhabilitado
                            throw  new Exception("El Usuario esta deshabilitado.");
                        }
                        
                        if ( !(new EmpresaBO(getConn()).haveAccessApp(user.getIdEmpresa())) ){
                            //Si la empresa no tiene permisos para usar el aplicativo
                            throw  new Exception("La empresa no tiene permisos para utilizar esta aplicación.");
                        }
                        
                        //Actualizamos ultima fecha de acceso
                        try{
                            //dato para sesion
                           setFechaAccesoAnterior(user.getFechaUltimoAcceso()!=null?user.getFechaUltimoAcceso() : new Date() );
                           //dato para base de datos
                           //user.setFechaUltimoAcceso(new Date());
                           new UsuariosDaoImpl(this.conn).update(user.createPk(), user);
                        }catch(Exception ex){ ex.printStackTrace(); }
                        
                        ldap = userValidated[i];
                        datosUsuario = new DatosUsuarioDaoImpl(getConn()).findByPrimaryKey(user.getIdDatosUsuario());
                        
                        datosVendedor = new SgfensVendedorDatosDaoImpl(getConn()).findByPrimaryKey(user.getIdUsuarios());
                        
                        qPayData = new QPayData();
                        qPayData.setUser(userName);
                        qPayData.setPass(pwdUser);
                        
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

    public boolean isLoginByEmpleado() {
        return loginByEmpleado;
    }

    public void setLoginByEmpleado(boolean loginByEmpleado) {
        this.loginByEmpleado = loginByEmpleado;
    }    

    public Connection getConn() {
        if (this.conn==null){
            try {
                this.conn = ResourceManager.getConnection();
            } catch (SQLException ex) {}
        }else{
            try {
                if (this.conn.isClosed()){
                    this.conn = ResourceManager.getConnection();
                }
            } catch (SQLException ex) {}
        }
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public int getUltimoRegistroID() {
        return ultimoRegistroID;
    }

    public void setUltimoRegistroID(int ultimoRegistroID) {
        this.ultimoRegistroID = ultimoRegistroID;
    }

    public Date getFechaAccesoAnterior() {
        return fechaAccesoAnterior;
    }

    public void setFechaAccesoAnterior(Date fechaAccesoAnterior) {
        this.fechaAccesoAnterior = fechaAccesoAnterior;
    }

    public boolean isPermisoVerMenu() {
        return permisoVerMenu;
    }

    public void setPermisoVerMenu(boolean permisoVerMenu) {
        this.permisoVerMenu = permisoVerMenu;
    }

    public ConceptoBO.Ordenamiento getOrdenamientoConceptos() {
        if (ordenamientoConceptos==null)
            ordenamientoConceptos = ConceptoBO.Ordenamiento.ALFABETICO_A_Z;
        return ordenamientoConceptos;
    }

    public void setOrdenamientoConceptos(ConceptoBO.Ordenamiento ordenamientoConceptos) {
        this.ordenamientoConceptos = ordenamientoConceptos;
    }
    
    /**
     * Método para validar el Login de un usuario comparando el nick y password
     * contra los almacenados en la base de datos
     * @param loginUser Login, nick del usuario
     * @param pwdUser Password, contraseña del usuario
     * @return Boolean que indica si fue exitoso o no
     */
    public boolean login(String userName, String pwdUser, int fromSCT) throws Exception {

        boolean validate = false;

        try {
            //Usuarios[] userValidated = new UsuariosDaoImpl(this.conn).findWhereUserNameEquals(userName);
            Ldap[] userValidated = new LdapDaoImpl(getConn()).findWhereUsuarioEquals(userName);
            if (userValidated.length==0) {
                user = null;
            }else {
                for(int i=0;i<userValidated.length;i++){
                    Encrypter encriptacion =  new Encrypter();
                    encriptacion.setMd5(true);
                    //if(userValidated[i].getPassword().equals(new TspUtilBO().getHash(pwdUser))){
                    if(userValidated[i].getPassword().equals(pwdUser)){
                        Usuarios[] userValido = new UsuariosDaoImpl(getConn()).findWhereUserNameEquals(userName);
                        user = userValido[0];
                        
                        if (user.getIdEstatus()==2){
                            //Si el usuario esta inhabilitado
                            throw  new Exception("El Usuario esta deshabilitado.");
                        }
                        
                        if ( !(new EmpresaBO(getConn()).haveAccessApp(user.getIdEmpresa())) ){
                            //Si la empresa no tiene permisos para usar el aplicativo
                            throw  new Exception("La empresa no tiene permisos para utilizar esta aplicación.");
                        }
                        
                        //Actualizamos ultima fecha de acceso
                        try{
                            //dato para sesion
                           setFechaAccesoAnterior(user.getFechaUltimoAcceso()!=null?user.getFechaUltimoAcceso() : new Date() );
                           //dato para base de datos
                           user.setFechaUltimoAcceso(new Date());
                           new UsuariosDaoImpl(this.conn).update(user.createPk(), user);
                        }catch(Exception ex){ ex.printStackTrace(); }
                        
                        ldap = userValidated[i];
                        datosUsuario = new DatosUsuarioDaoImpl(getConn()).findByPrimaryKey(user.getIdDatosUsuario());
                        
                        datosVendedor = new SgfensVendedorDatosDaoImpl(getConn()).findByPrimaryKey(user.getIdUsuarios());
                        
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
    
    
    public class QPayData {
        private String user = "";
        private String pass = "";

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }
        
    }
    
}
