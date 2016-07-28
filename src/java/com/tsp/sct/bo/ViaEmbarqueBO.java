/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.ViaEmbarque;
import com.tsp.sct.dao.exceptions.ViaEmbarqueDaoException;
import com.tsp.sct.dao.jdbc.ViaEmbarqueDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Leonardo
 */
public class ViaEmbarqueBO {
    
    private ViaEmbarque viaEmbarque = null;

    public ViaEmbarque getViaEmbarque() {
        return viaEmbarque;
    }

    public void setViaEmbarque(ViaEmbarque viaEmbarque) {
        this.viaEmbarque = viaEmbarque;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public ViaEmbarqueBO(Connection conn){
        this.conn = conn;
    }
       
    
    public ViaEmbarqueBO(int idViaEmbarque, Connection conn){
        this.conn = conn;
        try{
            ViaEmbarqueDaoImpl ViaEmbarqueDaoImpl = new ViaEmbarqueDaoImpl(this.conn);
            this.viaEmbarque = ViaEmbarqueDaoImpl.findByPrimaryKey(idViaEmbarque);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public ViaEmbarque findViaEmbarquebyId(int idViaEmbarque) throws Exception{
        ViaEmbarque ViaEmbarque = null;
        
        try{
            ViaEmbarqueDaoImpl ViaEmbarqueDaoImpl = new ViaEmbarqueDaoImpl(this.conn);
            ViaEmbarque = ViaEmbarqueDaoImpl.findByPrimaryKey(idViaEmbarque);
            if (ViaEmbarque==null){
                throw new Exception("No se encontro ningun ViaEmbarque que corresponda con los parámetros específicados.");
            }
            if (ViaEmbarque.getIdViaEmbarque()<=0){
                throw new Exception("No se encontro ningun ViaEmbarque que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del ViaEmbarque del usuario. Error: " + e.getMessage());
        }
        
        return ViaEmbarque;
    }
    
    public ViaEmbarque getViaEmbarqueGenericoByEmpresa(int idEmpresa) throws Exception{
        ViaEmbarque viaEmbarque = null;
        
        try{
            ViaEmbarqueDaoImpl viaEmbarqueDaoImpl = new ViaEmbarqueDaoImpl(this.conn);
            viaEmbarque = viaEmbarqueDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (viaEmbarque==null){
                throw new Exception("La empresa no tiene creada algun ViaEmbarque");
            }
        }catch(ViaEmbarqueDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada algun ViaEmbarque");
        }
        
        return viaEmbarque;
    }
    
    /**
     * Realiza una búsqueda por ID ViaEmbarque en busca de
     * coincidencias
     * @param idMarca ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public ViaEmbarque[] findViaEmbarques(int idViaEmbarque, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        ViaEmbarque[] viaEmbarqueDto = new ViaEmbarque[0];
        ViaEmbarqueDaoImpl viaEmbarqueDao = new ViaEmbarqueDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idViaEmbarque>0){
                sqlFiltro ="ID_VIA_EMBARQUE=" + idViaEmbarque + " AND ";
            }else{
                sqlFiltro ="ID_VIA_EMBARQUE>0 AND";
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
            
            viaEmbarqueDto = viaEmbarqueDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NOMBRE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return viaEmbarqueDto;
    }
    
    /**
     * Realiza una búsqueda por ID ViaEmbarque en busca de
     * coincidencias
     * @param idViaEmbarque ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar viaEmbarques, -1 para evitar filtro     
     * @return String de cada uno de los viaEmbarques
     */
    
        public String getViaEmbarquesByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            ViaEmbarque[] viaEmbarquesDto = findViaEmbarques(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (ViaEmbarque viaEmbarque:viaEmbarquesDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==viaEmbarque.getIdViaEmbarque())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+viaEmbarque.getIdViaEmbarque()+"' "
                            + selectedStr
                            + "title='"+viaEmbarque.getNombre()+"'>"
                            + viaEmbarque.getNombre()
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
        
    public int findCantidadViaEmbarques(int idConcepto, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
    int cantidad = 0;
    try {
        String sqlFiltro="";
        if (idConcepto>0){
            sqlFiltro ="ID_VIA_EMBARQUE=" + idConcepto + " AND ";
        }else{
            sqlFiltro ="ID_VIA_EMBARQUE>0 AND";
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
        ResultSet rs = stmt.executeQuery("SELECT COUNT(ID_VIA_EMBARQUE) as cantidad FROM VIA_EMBARQUE WHERE " + 
                sqlFiltro
               // + " ORDER BY NOMBRE ASC"
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
    
}
    

