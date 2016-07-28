/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Paquete;
import com.tsp.sct.dao.exceptions.PaqueteDaoException;
import com.tsp.sct.dao.jdbc.PaqueteDaoImpl;
import java.sql.Connection;

/**
 *
 * @author leonardo
 */
public class PaqueteBO {
    
    private Paquete paquete = null;

    public Paquete getPaquete() {
        return paquete;
    }

    public void setPaquete(Paquete paquete) {
        this.paquete = paquete;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public PaqueteBO(Connection conn){
        this.conn = conn;
    }
    
    public PaqueteBO(int idPaquete, Connection conn){
        this.conn = conn;
        try{
            PaqueteDaoImpl PaqueteDaoImpl = new PaqueteDaoImpl(this.conn);
            this.paquete = PaqueteDaoImpl.findByPrimaryKey(idPaquete);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Paquete findPaquetebyId(int idPaquete) throws Exception{
        Paquete Paquete = null;
        
        try{
            PaqueteDaoImpl PaqueteDaoImpl = new PaqueteDaoImpl(this.conn);
            Paquete = PaqueteDaoImpl.findByPrimaryKey(idPaquete);
            if (Paquete==null){
                throw new Exception("No se encontro ninguna Paquete que corresponda con los parámetros específicados.");
            }
            if (Paquete.getIdPaquete()<=0){
                throw new Exception("No se encontro ninguna Paquete que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la Paquete del usuario. Error: " + e.getMessage());
        }
        
        return Paquete;
    }
    
    public Paquete getPaqueteGenericoByEmpresa(int idEmpresa) throws Exception{
        Paquete paquete = null;
        
        try{
            PaqueteDaoImpl paqueteDaoImpl = new PaqueteDaoImpl(this.conn);
            paquete = paqueteDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (paquete==null){
                throw new Exception("La empresa no tiene creada alguna Paquete");
            }
        }catch(PaqueteDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada alguna Paquete");
        }
        
        return paquete;
    }
    
    /**
     * Realiza una búsqueda por ID Paquete en busca de
     * coincidencias
     * @param idPaquete ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar paquetes, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Paquete
     */
    public Paquete[] findPaquetes(long idPaquete, long idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        Paquete[] paqueteDto = new Paquete[0];
        PaqueteDaoImpl paqueteDao = new PaqueteDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idPaquete>0){
                sqlFiltro ="ID_PAQUETE=" + idPaquete + " AND ID_ESTATUS = 1 AND ";
            }else{
                sqlFiltro ="ID_PAQUETE>0 AND ID_ESTATUS = 1 AND ";
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
            
            paqueteDto = paqueteDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NOMBRE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return paqueteDto;
    }
    
    /**
     * Realiza una búsqueda por ID Paquete en busca de
     * coincidencias
     * @param idPaquete ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar paquetes, -1 para evitar filtro     
     * @return String de cada una de las paquetes
     */
    
        public String getPaquetesByIdHTMLCombo(long idEmpresa, long idSeleccionado){
        String strHTMLCombo ="";

        try{
            //Paquete[] paquetesDto = findPaquetes(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            Paquete[] paquetesDto = findPaquetes(-1, idEmpresa, 0, 0, "");
            
            for (Paquete paquete:paquetesDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==paquete.getIdPaquete())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+paquete.getIdPaquete()+"' "
                            + selectedStr
                            + "title='"+paquete.getDescripcion()+"'>"
                            + paquete.getNombre()
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
