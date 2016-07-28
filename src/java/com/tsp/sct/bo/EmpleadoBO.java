
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.DispositivoMovil;
import com.tsp.sct.dao.dto.Empleado;
import com.tsp.sct.dao.exceptions.EmpleadoDaoException;
import com.tsp.sct.dao.jdbc.DispositivoMovilDaoImpl;
import com.tsp.sct.dao.jdbc.EmpleadoDaoImpl;
import com.tsp.sct.dao.jdbc.UsuariosDaoImpl;
import com.tsp.sct.util.Encrypter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author ISC César Ulises Martínez García
 */
public class EmpleadoBO {

    private Empleado empleado = null;
    

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
    
    protected UsuarioBO usuarioBO = null;
    public UsuarioBO getUsuarioBO() {
        return usuarioBO;
    }

    private Connection conn = null;
    /**
     * Constructor vacío
     */
    public EmpleadoBO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Constructor, crea un objeto User a tráves de su ID almacenado en la Base de Datos
     * @param idUser Identificador único autoincrementable de empleado
     */
    public EmpleadoBO(int idEmpleado, Connection conn) {
        this.conn = conn;
        try {
            empleado = new EmpleadoDaoImpl(this.conn).findByPrimaryKey(idEmpleado);
        } catch (Exception ex) {
            //Logger.getLogger(UsuarioBO.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Se intento buscar un empleado con el ID: " + idEmpleado + " y no fue encontrado en la BD");
            ex.printStackTrace();
        }
    }

    /**
     * Constructor, asigna a la variable User local el objeto del mismo tipo que recibe como párametro
     * @param user Objeto de tipo User con todos los datos requeridos
     */
    public EmpleadoBO(Empleado empleado, Connection conn) {
        this.empleado = empleado;
        this.conn = conn;
    }
    
    public Empleado findEmpleadobyId(int idEmpleado) throws Exception{
        Empleado Empleado = null;
        
        try{
            EmpleadoDaoImpl empleadoDaoImpl = new EmpleadoDaoImpl(this.conn);
            Empleado = empleadoDaoImpl.findByPrimaryKey(idEmpleado);
            if (Empleado==null){
                throw new Exception("No se encontro ningun Empleado que corresponda con los parámetros específicados.");
            }
            if (Empleado.getIdEmpleado()<=0){
                throw new Exception("No se encontro ningun Empleado que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Empleado del usuario. Error: " + e.getMessage());
        }
        
        return Empleado;
    }
    
    public Empleado findEmpleadoByUsuario(int idUsuario) throws Exception{
        Empleado empleado = null;
        
        try{
            EmpleadoDaoImpl empleadoDaoImpl = new EmpleadoDaoImpl(this.conn);
            Empleado[] empleados = empleadoDaoImpl.findWhereIdUsuariosEquals(idUsuario);
            if (empleados.length>0)
                empleado = empleados[0];
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return empleado;
    }
    
    /**
     * Realiza una búsqueda por ID Empleado en busca de
     * coincidencias
     * @param idEmpleado ID Del Empleado para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar empleado, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Empleado
     */
    public Empleado[] findEmpleados(long idEmpleado, long idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        Empleado[] empleadoDto = new Empleado[0];
        EmpleadoDaoImpl empleadoDao = new EmpleadoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idEmpleado>0){
               //sqlFiltro ="ID_EMPLEADO=" + idEmpleado + " AND ID_ESTATUS = 1 AND ";
                sqlFiltro ="ID_EMPLEADO=" + idEmpleado + " AND ";
            }else{
                //sqlFiltro ="ID_EMPLEADO>0 AND ID_ESTATUS = 1 AND ";
                sqlFiltro ="ID_EMPLEADO>0  AND ";
            }
            if (idEmpresa>0){
                //sqlFiltro +=" ID_EMPRESA=" + idEmpresa;
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
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
            
            empleadoDto = empleadoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_EMPLEADO ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return empleadoDto;
    }
    
    /**
     * Realiza una búsqueda por ID Empleado en busca de
     * coincidencias
     * @param idEmpleado ID Del Empleado para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar empleado, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Empleado
     */
    public int findCantidadEmpleados(long idEmpleado, long idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        try {
            String sqlFiltro;
            if (idEmpleado>0){
                sqlFiltro ="ID_EMPLEADO=" + idEmpleado + " AND ";
            }else{
                sqlFiltro ="ID_EMPLEADO>0  AND ";
            }
            if (idEmpresa>0){
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
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
            
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(ID_EMPLEADO) as cantidad FROM empleado WHERE " + 
                    sqlFiltro
                    + sqlLimit);
            if(rs.next()){
                cantidad = rs.getInt("cantidad");
            }
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return cantidad;
    }
    
    /**
     * Realiza una búsqueda por ID Empleado en busca de
     * coincidencias
     * @param idEmpleado ID Del Empleado para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar empleado, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Empleado
     */
    public Empleado[] findEmpleadosActivos(long idEmpleado, long idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        Empleado[] empleadoDto = new Empleado[0];
        EmpleadoDaoImpl empleadoDao = new EmpleadoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idEmpleado>0){
               sqlFiltro ="ID_EMPLEADO=" + idEmpleado + " AND ID_ESTATUS = 1 AND ";
               
            }else{
               sqlFiltro ="ID_EMPLEADO>0 AND ID_ESTATUS = 1 AND ";
                
            }
            if (idEmpresa>0){
                //sqlFiltro +=" ID_EMPRESA=" + idEmpresa;
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
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
            
            empleadoDto = empleadoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_EMPLEADO ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return empleadoDto;
    }
    
    
    public String getEmpleadosByIdHTMLCombo(long idEmpresa, long idSeleccionado, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            Empleado[] empleadosDto = findEmpleados(-1, idEmpresa, 0, 0, filtroBusqueda);
            
            for (Empleado itemEmpleado:empleadosDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado==itemEmpleado.getIdEmpleado())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+itemEmpleado.getIdEmpleado()+"' "
                            + selectedStr
                            + "title='"+itemEmpleado.getNumEmpleado()+"'>"
                            + itemEmpleado.getNumEmpleado() + " - "
                            + (itemEmpleado.getNombre()!=null?itemEmpleado.getNombre():"") + " "
                            + (itemEmpleado.getApellidoPaterno()!=null?itemEmpleado.getApellidoPaterno():"") + " "
                            + (itemEmpleado.getApellidoMaterno()!=null?itemEmpleado.getApellidoMaterno():"") + " "
                            + "</option>";
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return strHTMLCombo;
    }

    /**
     * Método para validar el Login de un empleado comparando el nick y password
     * contra los almacenados en la base de datos
     * @param loginUser Login, nick del empleado
     * @param pwdUser Password, contraseña del empleado
     * @return Boolean que indica si fue exitoso o no
     */
    /*
    public boolean login(String userName, String pwdUser) throws Exception {

        boolean validate = false;
        
        
        //UsuarioBO usuarioBO = new UsuarioBO();
        usuarioBO = new UsuarioBO();
        usuarioBO.setLoginByEmpleado(true);
        if (usuarioBO.login(userName, pwdUser)){
            int idUsuario =  usuarioBO.getUser().getIdUsuarios();
            try {

                Empleado[] empleadoValidated = new EmpleadoDaoImpl(this.conn).findWhereIdUsuariosEquals(idUsuario);
                if (empleadoValidated.length==0) {
                    empleado = null;
                }else {
                    empleado = empleadoValidated[0];
                    validate = true;
                }

            } catch (Exception ex) {
                //Logger.getLogger(UsuarioBO.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error al intentar validar el acceso de un empleado");
                ex.printStackTrace();
                return false;
            }
        }
        
        return validate;
    }
    */
    
    /**
     * Método para validar el Login de un empleado comparando el nick y password
     * contra los almacenados en la base de datos
     * @param loginUser Login, nick del empleado
     * @param pwdUser Password, contraseña del empleado
     * @return Boolean que indica si fue exitoso o no
     */
    public boolean login(String userName, String pwdUser) throws Exception {

        boolean validate = false;
        
        try {

            Empleado[] empleadoValidated = new EmpleadoDaoImpl(this.conn).findWhereUsuarioEquals(userName);
            if (empleadoValidated.length==0) {
                empleado = null;
            }else {
                for (Empleado empleadoValidated1 : empleadoValidated) {
                    Encrypter encriptacion =  new Encrypter();
                    encriptacion.setMd5(true);
                    if (empleadoValidated1.getPassword().equals(encriptacion.encodeString2(pwdUser))) {
                        empleado = empleadoValidated1;
                        
                        usuarioBO = new UsuarioBO(this.conn, empleado.getIdUsuarios());
                        
                        if (empleado.getIdEstatus()==2){
                            //Si el usuario esta inhabilitado
                            throw  new Exception("El Registro de Empleado esta deshabilitado.");
                        }
                        
                        if ( !(new EmpresaBO(this.conn).haveAccessApp(empleado.getIdEmpresa())) ){
                            //Si la empresa no tiene permisos para usar el aplicativo
                            throw  new Exception("La empresa matriz a la que pertenece el Empleado no tiene permisos para utilizar esta aplicación.");
                        }
                        
                        //Actualizamos ultima fecha de acceso
                        try{
                            //dato para sesion
                           usuarioBO.setFechaAccesoAnterior(usuarioBO.getUser().getFechaUltimoAcceso()!=null?usuarioBO.getUser().getFechaUltimoAcceso() : new Date() );
                           //dato para base de datos
                           usuarioBO.getUser().setFechaUltimoAcceso(new Date());
                           new UsuariosDaoImpl(this.conn).update(usuarioBO.getUser().createPk(), usuarioBO.getUser());
                        }catch(Exception ex){ ex.printStackTrace(); }
                        
                        validate = true;
                        break;
                    }
                }
            }

        } catch (Exception ex) {
            System.out.println("Error al intentar validar el acceso de un empleado" + ex.getMessage());
            ex.printStackTrace();
            //return false;
            throw  new Exception(ex.getMessage());
        }
        
        return validate;
    }
    
    /**
     * Verifica si el IMEI del dispositivo corresponde al asignado al empleado
     */
    public boolean dispositivoAsignado(String IMEI){
        boolean asignado = false;
        
        try{
            DispositivoMovil dispositivo = new DispositivoMovilDaoImpl(this.conn).findByPrimaryKey(this.empleado.getIdDispositivo());
            if (dispositivo!=null){
                //El empleado tiene un dispositivo asignado
                if (IMEI.equals(dispositivo.getImei())){
                    //El IMEI del disposivo asignado corresponde al enviado
                    asignado = true;
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return asignado;
    }

    /**
     * Actualiza el registro en base de datos
     * @return boolean True en caso de exito, false en caso contrario
     */
    public boolean updateBD(){
        boolean exito= false;
        try{
            new EmpleadoDaoImpl(this.conn).update(this.empleado.createPk(), this.empleado);
            exito = true;
        }catch(Exception ex){
            ex.printStackTrace();
        }        
                
       return exito;
    }
    
    public int getCantidadByEmpleado(String filtroBusqueda){
        int cantidad = 0;
        try{
            //Connection conne = ResourceManager.getConnection();
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(ID_EMPLEADO) as cantidad FROM EMPLEADO WHERE " + filtroBusqueda);
            if(rs.next()){
                cantidad = rs.getInt("cantidad");
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return cantidad;
    }
    
    
    public Empleado getEmpleadoByNombre(String nombreEmpleado) throws Exception {
        Empleado empleado = null;

        try {
            EmpleadoDaoImpl empleadoDaoImpl = new EmpleadoDaoImpl(this.conn);
            empleado = empleadoDaoImpl.findByDynamicWhere(" NOMBRE LIKE '%" + nombreEmpleado + "%'", new Object[0])[0];
            if (empleado == null) {
                throw new Exception("No se encontraron datos del Empleado");
            }
        } catch (EmpleadoDaoException e) {
            e.printStackTrace();
            throw new Exception("No se encontraron datos del Empleado");
        }

        return empleado;
    }
    
    
    public String getPermisosEmpleadosByIdHTMLCombo(int idSeleccionado){
        String strHTMLCombo ="";
           
        
               
            String selectedStr="";
            
            strHTMLCombo += ""
                    + "<option value='0'" + (idSeleccionado==0?"selected":"") +  " title='Sin Permisos'>Sin Permisos</option>"
                    + "<option value='1'" + (idSeleccionado==1?"selected":"") +  " title='Crear'>Crear</option>"
                    + "<option value='2'" + (idSeleccionado==2?"selected":"") +  " title='Editar'>Editar</option>"
                    + "<option value='3'" + (idSeleccionado==3?"selected":"") +  " title='Crear y Editar'>Crear y Editar</option>";  

        return strHTMLCombo;
    }
    
    
}
