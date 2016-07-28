/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Region;
import com.tsp.sct.dao.exceptions.RegionDaoException;
import com.tsp.sct.dao.jdbc.RegionDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 19-dic-2012 
 */
public class RegionBO {
 private Region region = null;

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public RegionBO(Connection conn){
        this.conn = conn;
    }
    
    public RegionBO(int idRegion, Connection conn){
        this.conn = conn;
        try{
            RegionDaoImpl RegionDaoImpl = new RegionDaoImpl(this.conn);
            this.region = RegionDaoImpl.findByPrimaryKey(idRegion);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Region findRegionbyId(int idRegion) throws Exception{
        Region Region = null;
        
        try{
            RegionDaoImpl RegionDaoImpl = new RegionDaoImpl(this.conn);
            Region = RegionDaoImpl.findByPrimaryKey(idRegion);
            if (Region==null){
                throw new Exception("No se encontro ninguna Region que corresponda con los parámetros específicados.");
            }
            if (Region.getIdRegion()<=0){
                throw new Exception("No se encontro ninguna Region que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la Region del usuario. Error: " + e.getMessage());
        }
        
        return Region;
    }
    
    public Region getRegionGenericoByEmpresa(int idEmpresa) throws Exception{
        Region region = null;
        
        try{
            RegionDaoImpl regionDaoImpl = new RegionDaoImpl(this.conn);
            region = regionDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (region==null){
                throw new Exception("La empresa no tiene creada alguna Region");
            }
        }catch(RegionDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada alguna Region");
        }
        
        return region;
    }
    
    /**
     * Realiza una búsqueda por ID Region en busca de
     * coincidencias
     * @param idRegion ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar regions, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Region
     */
    public Region[] findRegions(long idRegion, long idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        Region[] regionDto = new Region[0];
        RegionDaoImpl regionDao = new RegionDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idRegion>0){
                sqlFiltro ="ID_REGION=" + idRegion + " AND ID_ESTATUS = 1 AND ";
            }else{
                sqlFiltro ="ID_REGION>0 AND ID_ESTATUS = 1 AND ";
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
            
            regionDto = regionDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NOMBRE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return regionDto;
    }
    
    /**
     * Realiza una búsqueda por ID Region en busca de
     * coincidencias
     * @param idRegion ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar regions, -1 para evitar filtro     
     * @return String de cada una de las regions
     */
    
        public String getRegionsByIdHTMLCombo(long idEmpresa, long idSeleccionado){
        String strHTMLCombo ="";

        try{
            //Region[] regionsDto = findRegions(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            Region[] regionsDto = findRegions(-1, idEmpresa, 0, 0, "");
            
            for (Region region:regionsDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==region.getIdRegion())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+region.getIdRegion()+"' "
                            + selectedStr
                            + "title='"+region.getDescripcion()+"'>"
                            + region.getNombre()
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
