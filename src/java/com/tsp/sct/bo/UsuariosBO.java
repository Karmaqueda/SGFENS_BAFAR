/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.DatosUsuario;
import com.tsp.sct.dao.dto.Usuarios;
import com.tsp.sct.dao.jdbc.DatosUsuarioDaoImpl;
import com.tsp.sct.dao.jdbc.UsuariosDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISCesarMartinez
 */
public class UsuariosBO {
    
    private Connection conn = null;
    
    private Usuarios usuario = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    
    /**
     * Constructor
     */
    public UsuariosBO(int idusuarios, Connection conn) {
        this.conn = conn;
        try {
            usuario = new UsuariosDaoImpl(this.conn).findByPrimaryKey(idusuarios);
        } catch (Exception ex) {
            //Logger.getLogger(UsuarioBO.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Se intento buscar un Usuario con el ID: " + idusuarios + " y no fue encontrado en la BD");
            ex.printStackTrace();
        }
    }

    public UsuariosBO(Connection conn) {
        this.conn = conn;
    }
    
    public UsuariosBO() {
    }
    
    
    
    
    
    /*
     * Ejecuta la busqueda de los usuarios por una empresa en particular
     */
    public Usuarios[] getUsuariosByEmpresa(int idEmpresa){
        Usuarios[] usuarios = new Usuarios[0];
        
        try{
            usuarios = new UsuariosDaoImpl(this.conn).findWhereIdEmpresaEquals(idEmpresa);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return usuarios;
    }
    
    /**
     * Realiza una búsqueda por ID Usuarios en busca de
     * coincidencias
     * @param idUsuarios ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar usuarios, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @return DTO Usuarios
     */
    public Usuarios[] findUsuarios(int idUsuarios, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        Usuarios[] usuariosDto = new Usuarios[0];
        UsuariosDaoImpl UsuariosDao = new UsuariosDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idUsuarios>0){
                sqlFiltro ="ID_USUARIOS=" + idUsuarios + " AND ";
            }else{
                sqlFiltro ="ID_USUARIOS>0 AND";
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
            
            usuariosDto = UsuariosDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_EMPRESA, USER_NAME ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return usuariosDto;
    }
    
    /**
     * Realiza la búsqueda de registros de Usuarios por empresa y cierto Rol
     * @param idEmpresa ID único de la empresa a la que pertenece el usuario, -1 para omitir filtro
     * @param idRol ID único del ROL que por el que se filtrara la búsqueda de usuarios, -1 para omitir filtro
     * @return Objeto DTO Usuarios
     */
    public Usuarios[] findUsuariosByRol(int idEmpresa, int idRol){
        Usuarios[] usuariosDto = new Usuarios[0];
       
        String sqlFiltro="";
        
        if (idEmpresa>0){
            //sqlFiltro +=" ID_EMPRESA=" + idEmpresa;
            sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ") AND ";
        }else{
            sqlFiltro +=" ID_EMPRESA>0 AND ";
        }
        
        if (idRol>0){
            sqlFiltro += " ID_ROLES=" + idRol;
        }else{
            sqlFiltro += " ID_ROLES>0";
        }
        
        if(sqlFiltro.equals("")){
            sqlFiltro += " ID_ESTATUS = 1 ";
        }else{
            sqlFiltro += " AND ID_ESTATUS = 1 ";
        }
        
        try{
            usuariosDto = new UsuariosDaoImpl(this.conn).findByDynamicWhere(sqlFiltro, null);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return usuariosDto;
    }
    
    public String getUsuariosByRolHTMLCombo(int idEmpresa, int idRol, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            Usuarios[] usuariosDto = findUsuariosByRol(idEmpresa, idRol);
            
            for (Usuarios usuario:usuariosDto){
                try{
                    DatosUsuario datosUsuario = new DatosUsuarioDaoImpl(this.conn).findByPrimaryKey(usuario.getIdDatosUsuario());
                    String selectedStr="";

                    if (idSeleccionado==usuario.getIdUsuarios())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+usuario.getIdUsuarios()+"' "
                            + selectedStr
                            + "title='"+usuario.getUserName()+"'>"
                            + datosUsuario.getNombre() + " " + (datosUsuario.getApellidoPat()!=null?datosUsuario.getApellidoPat():"")
                            +"</option>";
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return strHTMLCombo;
    }
    
}
