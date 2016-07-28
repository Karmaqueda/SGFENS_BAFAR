/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.EmpleadoXPosEstacion;
import com.tsp.sct.dao.dto.PosEstacion;
import com.tsp.sct.dao.jdbc.EmpleadoXPosEstacionDaoImpl;
import com.tsp.sct.dao.jdbc.PosEstacionDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author ISCesarMartinez
 */
public class PosEstacionBO {
    private PosEstacion posEstacion  = null;

    public PosEstacion getPosEstacion() {
        return posEstacion;
    }

    public void setPosEstacion(PosEstacion posEstacion) {
        this.posEstacion = posEstacion;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public PosEstacionBO(Connection conn){
        this.conn = conn;
    }
    
    public PosEstacionBO(int idPosEstacion, Connection conn){        
        this.conn = conn;
        try{
            PosEstacionDaoImpl PosEstacionDaoImpl = new PosEstacionDaoImpl(this.conn);
            this.posEstacion = PosEstacionDaoImpl.findByPrimaryKey(idPosEstacion);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public PosEstacion findPosEstacionById(int idPosEstacion) throws Exception{
        PosEstacion posEstacion = null;
        
        try{
            PosEstacionDaoImpl posEstacionDaoImpl = new PosEstacionDaoImpl(this.conn);
            posEstacion = posEstacionDaoImpl.findByPrimaryKey(idPosEstacion);
            if (posEstacion==null){
                throw new Exception("No se encontro ningun posEstacion que corresponda según los parámetros específicados.");
            }
            if (posEstacion.getIdPosEstacion()<=0){
                throw new Exception("No se encontro ningun posEstacion que corresponda según los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de PosEstacion del usuario. Error: " + e.getMessage());
        }
        
        return posEstacion;
    }
    
    
    /**
     * Realiza una búsqueda por ID PosEstacion en busca de
     * coincidencias
     * @param idPosEstacion ID Del PosEstacion para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar posEstacion, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO PosEstacion
     */
    public PosEstacion[] findPosEstacion(int idPosEstacion, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        PosEstacion[] posEstacionDto = new PosEstacion[0];
        PosEstacionDaoImpl posEstacionDao = new PosEstacionDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idPosEstacion>0){
                sqlFiltro ="ID_POS_ESTACION=" + idPosEstacion + " AND ";
            }else{
                sqlFiltro ="ID_POS_ESTACION>0 AND";
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
            
            posEstacionDto = posEstacionDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_POS_ESTACION DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return posEstacionDto;
    }
    
    /**
     * Realiza una búsqueda de la cantidad de coincidencias por ID PosEstacion y otros filtros
     * @param idPosEstacion ID Del PosEstacion para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar posEstacion, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO PosEstacion
     */
    public int findCantidadPosEstacion(int idPosEstacion, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        try {
            String sqlFiltro="";
            if (idPosEstacion>0){
                sqlFiltro ="ID_POS_ESTACION=" + idPosEstacion + " AND ";
            }else{
                sqlFiltro ="ID_POS_ESTACION>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(ID_POS_ESTACION) as cantidad FROM pos_estacion WHERE " + 
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
    
    public String getPosEstacionesByIdHTMLCombo(int idEmpresa, int idSeleccionado, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            PosEstacion[] coincidencias = findPosEstacion(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (PosEstacion item:coincidencias){
                try{
                    String selectedStr="";

                    if (idSeleccionado==item.getIdPosEstacion()){
                        selectedStr = " selected ";
                    }
                        

                    strHTMLCombo += "<option value='"+item.getIdPosEstacion()+"' "
                            + selectedStr
                            + "title='"+item.getIdentificacion()+"'>"
                            + item.getIdentificacion() + " - " + item.getNombre()
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
    
    /**
     * Registra la relación de muchos a muchos de una estacion de punto de venta
     * con un empleado
     * @param idEmpleado
     * @throws Exception 
     */
    public void asignarEmpleadoAEstacion(int idEmpleado) throws Exception{
        if (posEstacion!=null){
            EmpleadoBO empleadoBO = new EmpleadoBO(idEmpleado, conn);
            if (empleadoBO.getEmpleado()!=null){
                EmpleadoXPosEstacionDaoImpl empleadoXPosEstacionDao = new EmpleadoXPosEstacionDaoImpl(conn);
                //Buscamos si no existe previamente la relación
                EmpleadoXPosEstacion empleadoXPosEstacion = empleadoXPosEstacionDao.findByPrimaryKey(idEmpleado, posEstacion.getIdPosEstacion());
                if (empleadoXPosEstacion == null){
                    //Solo si no existe previamente, creamos un nuevo registro de relacion
                    empleadoXPosEstacion = new EmpleadoXPosEstacion();
                    empleadoXPosEstacion.setFechaHrAlta(new Date());
                    empleadoXPosEstacion.setIdEmpleado(idEmpleado);
                    empleadoXPosEstacion.setIdPosEstacion(posEstacion.getIdPosEstacion());
                    empleadoXPosEstacionDao.insert(empleadoXPosEstacion);
                }
            }else
                throw new Exception("El empleado indicado no existe en base de datos");
        }else{
            throw new Exception("No ha seleccionado una Estacion");
        }
    }
    
    public EmpleadoXPosEstacion[] findEmpleadosXEstacion() throws Exception{
        EmpleadoXPosEstacion[] empleadosXEstacion = new EmpleadoXPosEstacion[0];
        if (posEstacion!=null){
            EmpleadoXPosEstacionDaoImpl empleadoXPosEstacionDao = new EmpleadoXPosEstacionDaoImpl(conn);
            try{
                empleadosXEstacion = empleadoXPosEstacionDao.findByDynamicWhere(" id_pos_estacion=" + posEstacion.getIdPosEstacion(), null);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }else{
            throw new Exception("No ha seleccionado una Estacion");
        }
        return empleadosXEstacion;
    }
    
}