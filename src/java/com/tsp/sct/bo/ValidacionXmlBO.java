/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.config.Configuration;
import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.dao.dto.ValidacionXml;
import com.tsp.sct.dao.jdbc.ValidacionXmlDaoImpl;
import java.io.File;
import java.sql.Connection;

/**
 *
 * @author leonardo
 */
public class ValidacionXmlBO {
    
    private ValidacionXml validacionXml = null;

    public ValidacionXml getValidacionXml() {
        return validacionXml;
    }

    public void setValidacionXml(ValidacionXml validacionXml) {
        this.validacionXml = validacionXml;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public ValidacionXmlBO(Connection conn){
        this.conn = conn;
    }
       
    
    public ValidacionXmlBO(int idValidacionXml, Connection conn){
        this.conn = conn;
        try{
            ValidacionXmlDaoImpl ValidacionXmlDaoImpl = new ValidacionXmlDaoImpl(this.conn);
            this.validacionXml = ValidacionXmlDaoImpl.findByPrimaryKey(idValidacionXml);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public ValidacionXml findMarcabyId(int idValidacionXml) throws Exception{
        ValidacionXml ValidacionXml = null;
        
        try{
            ValidacionXmlDaoImpl ValidacionXmlDaoImpl = new ValidacionXmlDaoImpl(this.conn);
            ValidacionXml = ValidacionXmlDaoImpl.findByPrimaryKey(idValidacionXml);
            if (ValidacionXml==null){
                throw new Exception("No se encontro ningun ValidacionXml que corresponda con los parámetros específicados.");
            }
            if (ValidacionXml.getIdValidacion()<=0){
                throw new Exception("No se encontro ningun ValidacionXml que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del ValidacionXml del usuario. Error: " + e.getMessage());
        }
        
        return ValidacionXml;
    }
  
    
    /**
     * Realiza una búsqueda por ID ValidacionXml en busca de
     * coincidencias
     * @param idValidacionXml ID De la validacion para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public ValidacionXml[] findValidacionXmls(int idValidacionXml, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        ValidacionXml[] validacionXmlDto = new ValidacionXml[0];
        ValidacionXmlDaoImpl validacionXmlDao = new ValidacionXmlDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idValidacionXml>0){
                sqlFiltro ="ID_VALIDACION=" + idValidacionXml + " AND ";
            }else{
                sqlFiltro ="ID_VALIDACION>0 AND";
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
            
            validacionXmlDto = validacionXmlDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_VALIDACION DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return validacionXmlDto;
    }
    
    public File getComprobanteFile() throws Exception{
        String rutaArchivo="";
        Configuration appConfig = new Configuration();

        Empresa empresaDto = new EmpresaBO(this.validacionXml.getIdEmpresa(), this.conn).getEmpresa();

        String archivoXML = this.validacionXml.getNombreArchivo();

        rutaArchivo = appConfig.getApp_content_path() 
                + empresaDto.getRfc() 
                + "/ValidacionXML/"
                +archivoXML;
        
        File fileXML = new File(rutaArchivo);
        
        if (!fileXML.exists()){
            throw new Exception("El archivo " + fileXML.getAbsolutePath() + " no existe en el repositorio del sistema.");
        }
        
        return fileXML;
    }
    
}
    

