/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Servicio;
import com.tsp.sct.dao.exceptions.ServicioDaoException;
import com.tsp.sct.dao.jdbc.ServicioDaoImpl;
import com.tsp.sct.util.Encrypter;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class ServicioBO {
    
    private Servicio servicio = null;

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public ServicioBO(Connection conn){
        this.conn = conn;
    }
    
    public ServicioBO(int idServicio, Connection conn){
        this.conn = conn;
         try{
            ServicioDaoImpl ServicioDaoImpl = new ServicioDaoImpl(this.conn);
            this.servicio = ServicioDaoImpl.findByPrimaryKey(idServicio);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Servicio findServiciobyId(int idServicio) throws Exception{
        Servicio servicio = null;
        
        try{
            ServicioDaoImpl servicioDaoImpl = new ServicioDaoImpl(this.conn);
            servicio = servicioDaoImpl.findByPrimaryKey(idServicio);
            if (servicio==null){
                throw new Exception("No se encontro ningun Servicio que corresponda con los parámetros específicados.");
            }
            if (servicio.getIdServicio()<=0){
                throw new Exception("No se encontro ningun Servicio que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de un Servicio. Error: " + e.getMessage());
        }
        
        return servicio;
    }
    
    public Servicio getServicioGenericoByEmpresa(int idEmpresa) throws Exception{
        Servicio servicio = null;
        
        try{
            ServicioDaoImpl servicioDaoImpl = new ServicioDaoImpl(this.conn);
            servicio = servicioDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (servicio==null){
                throw new Exception("La empresa no tiene creado algun Servicio");
            }
        }catch(ServicioDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creado algun Servicio");
        }
        
        return servicio;
    }
    
    /**
     * Realiza una búsqueda por ID Servicio en busca de
     * coincidencias
     * @param idServicio ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar Servicios, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Servicio
     */
    public Servicio[] findServicios(int idServicio, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        Servicio[] servicioDto = new Servicio[0];
        ServicioDaoImpl servicioDao = new ServicioDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idServicio>0){
                sqlFiltro ="ID_SERVICIO=" + idServicio + " AND ";
            }else{
                sqlFiltro ="ID_SERVICIO>0 AND";
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
            
            servicioDto = servicioDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NOMBRE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return servicioDto;
    }
    
    public String getServiciosByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";
        Encrypter encripDesencri = new Encrypter();

        try{
            Servicio[] serviciosDto = findServicios(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (Servicio servicioItem:serviciosDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==servicioItem.getIdServicio())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+servicioItem.getIdServicio()+"' "
                            + selectedStr
                            + "title='"+servicioItem.getNombre()+"'>"
                            + "[" + servicioItem.getIdServicio() + "] " +  servicioItem.getNombre()
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
