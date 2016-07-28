/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.config.Configuration;
import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.dao.dto.ImagenPersonal;
import com.tsp.sct.dao.exceptions.ImagenPersonalDaoException;
import com.tsp.sct.dao.jdbc.ImagenPersonalDaoImpl;
import java.io.File;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ISCesarMartinez
 */
public class ImagenPersonalBO {  
    
    private ImagenPersonal imagenPersonal = null;

    public ImagenPersonal getImagenPersonal() {
        return imagenPersonal;
    }

    public void setImagenPersonal(ImagenPersonal imagenPersonal) {
        this.imagenPersonal = imagenPersonal;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public ImagenPersonalBO(Connection conn){
        this.conn = conn;
    }
    
    public ImagenPersonalBO(int idImagenPersonal, Connection conn){
        this.conn = conn;
        try{
            ImagenPersonalDaoImpl imagenPersonalDaoImpl = new ImagenPersonalDaoImpl(this.conn);
            this.imagenPersonal = imagenPersonalDaoImpl.findByPrimaryKey(idImagenPersonal);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public ImagenPersonal findImagenPersonalbyId(int idImagenPersonal) throws Exception{
        ImagenPersonal ImagenPersonal = null;
        
        try{
            ImagenPersonalDaoImpl imagenPersonalDaoImpl = new ImagenPersonalDaoImpl(this.conn);
            ImagenPersonal = imagenPersonalDaoImpl.findByPrimaryKey(idImagenPersonal);
            if (ImagenPersonal==null){
                throw new Exception("No se encontro ningun ImagenPersonal que corresponda con los parámetros específicados.");
            }
            if (ImagenPersonal.getIdImagenPersonal()<=0){
                throw new Exception("No se encontro ningun ImagenPersonal que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del ImagenPersonal del usuario. Error: " + e.getMessage());
        }
        
        return ImagenPersonal;
    }
    
    public ImagenPersonal findImagenPersonalByEmpresa(int idEmpresa) throws Exception {
        ImagenPersonal ImagenPersonal = null;
        
        try{
            ImagenPersonalDaoImpl ImagenPersonalDaoImpl = new ImagenPersonalDaoImpl(this.conn);
            //ImagenPersonal = imagenPersonalDaoImpl.findByEmpresa(idEmpresa)[0];
            ImagenPersonal = ImagenPersonalDaoImpl.findByDynamicWhere("id_empresa=" + idEmpresa + " ORDER BY ID_IMAGEN_PERSONAL DESC", null)[0];
            if (ImagenPersonal==null){
                throw new Exception("No se encontro ningun ImagenPersonal que corresponda con los parámetros específicados.");
            }
            if (ImagenPersonal.getIdImagenPersonal()<=0){
                throw new Exception("No se encontro ningun ImagenPersonal que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del ImagenPersonal del usuario. Error: " + e.getMessage());
        }
        
        return ImagenPersonal;
    }
    
    public File getFileImagenPersonalByEmpresa(int idEmpresa){
        Configuration appConfig = new Configuration();
        File archivoImagenPersonal = null;
        ImagenPersonalDaoImpl ImagenPersonalDaoImpl = new ImagenPersonalDaoImpl(this.conn);
        Empresa empresaDto = new EmpresaBO(idEmpresa, this.conn).getEmpresa();
        try {
            ImagenPersonal imagenPersonal = ImagenPersonalDaoImpl.findByDynamicWhere("id_empresa=" + idEmpresa + " ORDER BY ID_IMAGEN_PERSONAL DESC", null)[0];
            String rutaArchivoImagenPersonal = appConfig.getApp_content_path() + empresaDto.getRfc() + "/" + imagenPersonal.getNombreImagen();
            archivoImagenPersonal = new File(rutaArchivoImagenPersonal);
        } catch (ImagenPersonalDaoException ex) {
            ex.printStackTrace();
        }
        return archivoImagenPersonal;
    }
    
    /**
     * Realiza una búsqueda por ID Marca en busca de
     * coincidencias
     * @param idImagenPersonal ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public ImagenPersonal[] findImagenPersonales(int idImagenPersonal, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        ImagenPersonal[] imagenPersonalDto = new ImagenPersonal[0];
        ImagenPersonalDaoImpl imagenPersonalDao = new ImagenPersonalDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idImagenPersonal>0){
                sqlFiltro ="ID_IMAGEN_PERSONAL=" + idImagenPersonal + " AND ";
            }else{
                sqlFiltro ="ID_IMAGEN_PERSONAL>0 AND";
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
            
            imagenPersonalDto = imagenPersonalDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_IMAGEN_PERSONAL DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return imagenPersonalDto;
    }
    
}