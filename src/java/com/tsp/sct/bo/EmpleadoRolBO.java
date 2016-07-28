/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Empleado;
import com.tsp.sct.dao.dto.Roles;
import com.tsp.sct.dao.exceptions.EmpleadoDaoException;
import com.tsp.sct.dao.jdbc.EmpleadoDaoImpl;
import com.tsp.sct.dao.jdbc.RolesDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISCesarMartinez
 */
public class EmpleadoRolBO {
    private Roles roles = null;

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }
        
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public EmpleadoRolBO(Connection conn){
        this.conn = conn;
    }
    
    public EmpleadoRolBO(int idRoles, Connection conn){
        this.conn = conn;
        try{
            RolesDaoImpl RolesDaoImpl = new RolesDaoImpl(this.conn);
            this.roles = RolesDaoImpl.findByPrimaryKey(idRoles);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Roles findTspRolebyId(int idRoles) throws Exception{
        Roles Roles = null;
        
        try{
            RolesDaoImpl RolesDaoImpl = new RolesDaoImpl(this.conn);
            Roles = RolesDaoImpl.findByPrimaryKey(idRoles);
            if (Roles==null){
                throw new Exception("No se encontro ningun Role de Empleado que corresponda con los parámetros específicados.");
            }
            if (Roles.getIdRoles()<=0){
                throw new Exception("No se encontro ningun Rol de Empleado que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Empleado del usuario. Error: " + e.getMessage());
        }
        
        return Roles;
    }
    
    public Roles getRolesGenericoByTspRole(int idRoles) throws Exception{
        Roles tspRole = null;
        
        try{
            RolesDaoImpl tspRoleDaoImpl = new RolesDaoImpl(this.conn);
            tspRole = tspRoleDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idRoles + " AND GENERICO = 1", new Object[0])[0];
            if (tspRole==null){
                throw new Exception("La empresa no tiene creado un Rol de Empleado Génerico");
            }
        }catch(EmpleadoDaoException e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creado un Rol de Empleado Génerico");
        }
        
        return tspRole;
    }
    
    /**
     * Realiza una búsqueda por ID Empleado en busca de
     * coincidencias
     * @param idTspRole ID Del TspRole para filtrar, -1 para mostrar todos los registros
     * @param idTspRole ID de la TspRole a filtrar TspRole, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Empleado
     */
    public Roles[] findRoles(int idRoles, int idEmplesa, int minLimit,int maxLimit, String filtroBusqueda) {
        Roles[] tspRoleDto = new Roles[0];
        RolesDaoImpl tspRoleDao = new RolesDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idRoles>0){
                sqlFiltro ="ID_ROLES=" + idRoles + " AND MOSTRAR_PRETORIANO = 1 ";
            }else{
                sqlFiltro ="ID_ROLES>0 AND MOSTRAR_PRETORIANO = 1 ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            tspRoleDto = tspRoleDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NOMBRE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return tspRoleDto;
    }
    
    public String getRolesByIdHTMLCombo(int idEmpresa, int idSeleccionado, long rolUsuario){
        String strHTMLCombo ="";

        try{
            
            //FILTRO PARA ID DE ROL QUE PUEDE ASIGNAR UN USUARIO DEPENDIENDO DE QUE ROL SE TRATE:
            //Administrador de Sucursal: este rol al crear un nuevo empleado no podrá asignar un rol de igual o mayor jerarquía que el propio.
            //Dirección de Ventas: Mismas funciones que el administrador, con la diferencia que podrá visualizar la información de todas las sucursales
            
            String filtro = "";
            if(rolUsuario==3||rolUsuario==4)
                filtro = filtro + " AND ID_ROLE < 3 OR ID_ROLE = 6 "; //SOLO PODRA CREAR ROLES DE TIPO PROMOTOR=1, CONDUTOR=2, GESTOR=6                        
            
            Roles[] empleadosDto = findRoles(-1, idEmpresa, 0, 0, filtro);
            
            for (Roles itemEmpleado:empleadosDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado==itemEmpleado.getIdRoles())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+itemEmpleado.getIdRoles()+"' "
                            + selectedStr
                            + "title='"+itemEmpleado.getDescripcion()+"'>"
                            + itemEmpleado.getNombre()
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
    
    
    public String getRolesByIdHTMLComboEVC(int idEmpresa, int idSeleccionado, long rolUsuario){
        String strHTMLCombo ="";

        try{
            
            //FILTRO PARA ID DE ROL QUE PUEDE ASIGNAR UN USUARIO DEPENDIENDO DE QUE ROL SE TRATE:
            //Administrador de Sucursal: este rol al crear un nuevo empleado no podrá asignar un rol de igual o mayor jerarquía que el propio.
            //Dirección de Ventas: Mismas funciones que el administrador, con la diferencia que podrá visualizar la información de todas las sucursales
            
            String filtro = "";
            if(rolUsuario!=1 && rolUsuario!=2){                
                filtro = filtro + " AND ID_ROLES > 2 "; //SOLO PODRA CREAR ROLES DE TIPO PROMOTOR=1, CONDUTOR=2, GESTOR=6 
            }
            
            
            Roles[] empleadosDto = findRoles(-1, idEmpresa, 0, 0, filtro);
            
            for (Roles itemEmpleado:empleadosDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado==itemEmpleado.getIdRoles())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+itemEmpleado.getIdRoles()+"' "
                            + selectedStr
                            + "title='"+itemEmpleado.getDescripcion()+"'>"
                            + itemEmpleado.getNombre()
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
