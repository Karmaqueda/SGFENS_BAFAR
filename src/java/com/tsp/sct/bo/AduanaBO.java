/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;
    
import com.tsp.sct.dao.dto.Aduana;
import com.tsp.sct.dao.exceptions.AduanaDaoException;
import com.tsp.sct.dao.jdbc.AduanaDaoImpl;
import java.sql.Connection;

public class AduanaBO {
 private Aduana aduana = null;

    public Aduana getAduana() {
        return aduana;
    }

    public void setAduana(Aduana aduana) {
        this.aduana = aduana;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public AduanaBO(Connection conn){
        this.conn = conn;
    }
    
    public AduanaBO(int idAduana, Connection conn){        
        this.conn = conn;
        try{
            AduanaDaoImpl AduanaDaoImpl = new AduanaDaoImpl(this.conn);
            this.aduana = AduanaDaoImpl.findByPrimaryKey(idAduana);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Aduana findAduanabyId(int idAduana) throws Exception{
        Aduana Aduana = null;
        
        try{
            AduanaDaoImpl AduanaDaoImpl = new AduanaDaoImpl(this.conn);
            Aduana = AduanaDaoImpl.findByPrimaryKey(idAduana);
            if (Aduana==null){
                throw new Exception("No se encontro ninguna Aduana que corresponda con los parámetros específicados.");
            }
            if (Aduana.getIdAduana()<=0){
                throw new Exception("No se encontro ninguna Aduana que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la Aduana del usuario. Error: " + e.getMessage());
        }
        
        return Aduana;
    }
    
    
    /**
     * Realiza una búsqueda por ID Aduana en busca de
     * coincidencias
     * @param idAduana ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar aduanas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Aduana
     */
    public Aduana[] findAduanas(int idAduana, int idComprobanteDescripcion, int idComprobanteFiscal, int minLimit,int maxLimit, String filtroBusqueda) {
        Aduana[] aduanaDto = new Aduana[0];
        AduanaDaoImpl aduanaDao = new AduanaDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idAduana>0){
                sqlFiltro ="ID_ADUANA=" + idAduana + " AND ";
            }else{
                sqlFiltro ="ID_ADUANA>0 AND";
            }
            if (idComprobanteDescripcion>0){                
                sqlFiltro += " ID_COMPROBANTE_DESCRIPCION = " + idComprobanteDescripcion + " AND ";
            }else{
                sqlFiltro +=" ID_COMPROBANTE_DESCRIPCION>0 AND ";
            }
            
             if (idComprobanteFiscal>0){                
                sqlFiltro += " ID_COMPROBANTE_DESCRIPCION = " + idComprobanteFiscal + " ";
            }else{
                sqlFiltro +=" ID_COMPROBANTE_DESCRIPCION>0 ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            aduanaDto = aduanaDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_ADUANA ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return aduanaDto;
    }
        
}
