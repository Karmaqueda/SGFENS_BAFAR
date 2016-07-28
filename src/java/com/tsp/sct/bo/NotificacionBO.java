/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.NotificacionesUsuarios;
import com.tsp.sct.dao.jdbc.MarcaDaoImpl;
import com.tsp.sct.dao.jdbc.NotificacionesUsuariosDaoImpl;
import java.sql.Connection;

/**
 *
 * @author HpPyme
 */
public class NotificacionBO {
    
    
    private Connection conn = null;
    private NotificacionesUsuarios notificacion = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public NotificacionesUsuarios getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(NotificacionesUsuarios notificacion) {
        this.notificacion = notificacion;
    }

    public NotificacionBO(Connection conn) {
         this.conn = conn;
    }
    
    
    public NotificacionBO(Connection conn, int idNotificacion) {
        this.conn = conn;
        try{
            NotificacionesUsuariosDaoImpl notificacionesUsuariosDaoImpl = new NotificacionesUsuariosDaoImpl(this.conn);
            this.notificacion = notificacionesUsuariosDaoImpl.findByPrimaryKey(idNotificacion);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public NotificacionesUsuarios findMarcabyId(int idNotificacion) throws Exception{
        NotificacionesUsuarios notificacionesUsuarios = null;
        
        try{
            NotificacionesUsuariosDaoImpl notificacionesUsuariosDaoImpl = new NotificacionesUsuariosDaoImpl(this.conn);
            notificacionesUsuarios = notificacionesUsuariosDaoImpl.findByPrimaryKey(idNotificacion);
            if (notificacionesUsuarios==null){
                throw new Exception("No se encontro ninguna notificación que corresponda con los parámetros específicados.");
            }
            if (notificacionesUsuarios.getIdNotificacion()<=0){
                throw new Exception("No se encontro ninguna notificación que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la notificación del usuario. Error: " + e.getMessage());
        }
        
        return notificacionesUsuarios;
    }
    
    
    /**
     * Realiza una búsqueda por ID Notificacion en busca de
     * coincidencias
     * @param idMarca ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public NotificacionesUsuarios[] findNotificaciones(int idNotificacion, int idUsuario, int minLimit,int maxLimit, String filtroBusqueda) {
        NotificacionesUsuarios[] notificacionesDto = new NotificacionesUsuarios[0];
        NotificacionesUsuariosDaoImpl NotificacionesUsuariosDao = new NotificacionesUsuariosDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idNotificacion>0){
                sqlFiltro ="ID_NOTIFICACION=" + idNotificacion + " AND ";
            }else{
                sqlFiltro ="ID_NOTIFICACION>0 AND ";
            }
            if (idUsuario>0){
                sqlFiltro +=" ID_USUARIO=" + idUsuario + " ";
            }else{
                sqlFiltro +=" ID_USUARIO>0 ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            notificacionesDto = NotificacionesUsuariosDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_NOTIFICACION ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return notificacionesDto;
    }
    
    
}
