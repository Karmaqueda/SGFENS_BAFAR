/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.config.Configuration;
import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.dao.dto.SarComprobanteAdjunto;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.dao.jdbc.SarComprobanteAdjuntoDaoImpl;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesar poseidon24@hotmail.com
 * @since 23/03/2015 12:22:16 PM
 */
public class SarComprobanteAdjuntoBO {

    private SarComprobanteAdjunto sarComprobanteAdjunto = null;

    public SarComprobanteAdjunto getSarComprobanteAdjunto() {
        return sarComprobanteAdjunto;
    }

    public void setSarComprobanteAdjunto(SarComprobanteAdjunto sarComprobanteAdjunto) {
        this.sarComprobanteAdjunto = sarComprobanteAdjunto;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        try{
            if (this.conn == null)
                this.conn = ResourceManager.getConnection();
        }catch(Exception ex){}
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SarComprobanteAdjuntoBO(Connection conn){
        this.conn = conn;
    }
    
    public SarComprobanteAdjuntoBO(int idSarComprobanteAdjunto, Connection conn){
        this.conn = conn;
         try{
            SarComprobanteAdjuntoDaoImpl SarComprobanteAdjuntoDaoImpl = new SarComprobanteAdjuntoDaoImpl(this.conn);
            this.sarComprobanteAdjunto = SarComprobanteAdjuntoDaoImpl.findByPrimaryKey(idSarComprobanteAdjunto);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * Realiza una búsqueda por ID SarComprobanteAdjunto en busca de
     * coincidencias
     * @param idSarComprobanteAdjunto ID Clave principal para filtrar, -1 para mostrar todos los registros
     * @param idComprobanteFiscal ID Comprobante Fiscal, -1 para no aplicar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SarComprobanteAdjunto
     */
    public SarComprobanteAdjunto[] findSarComprobanteAdjuntos(int idSarComprobanteAdjunto, int minLimit,int maxLimit, String filtroBusqueda) {
        SarComprobanteAdjunto[] sarComprobanteAdjuntoDto = new SarComprobanteAdjunto[0];
        SarComprobanteAdjuntoDaoImpl sarComprobanteAdjuntoDao = new SarComprobanteAdjuntoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSarComprobanteAdjunto>0){
                sqlFiltro ="ID_SAR_COMPROBANTE_ADJUNTO=" + idSarComprobanteAdjunto + " ";
            }else{
                sqlFiltro ="ID_SAR_COMPROBANTE_ADJUNTO>0 ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            sarComprobanteAdjuntoDto = sarComprobanteAdjuntoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_SAR_COMPROBANTE_ADJUNTO ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return sarComprobanteAdjuntoDto;
    }
    
    public int getCantidadAdjuntosByComprobanteFiscal(int idComprobanteFiscal){
        int cantidad = 0;
        try{
            Statement stmt = getConn().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(ID_SAR_COMPROBANTE_ADJUNTO) as cantidad FROM sar_comprobante_adjunto WHERE ID_COMPROBANTE_FISCAL ="+idComprobanteFiscal);
            if(rs.next()){
                cantidad = rs.getInt("cantidad");
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return cantidad;
    }
    
    /**
     * Realiza la busqueda de un archivo en el repositorio de Archivos SAR de la empresa indicada
     * @param nombreArchivo Nombre a buscar
     * @param idEmpresa ID de empresa a la que pertenece el usuario
     * @return File encontrado
     * @throws Exception En caso de no existir el archivo
     */
    public File getArchivoDeRepositorioByEmpresa(String nombreArchivo, int idEmpresa) throws Exception{
        String rutaArchivo="";
        Configuration appConfig = new Configuration();

        Empresa empresaDto = new EmpresaBO(idEmpresa, this.conn).getEmpresa();

        String archivoXML = nombreArchivo;//this.sarComprobanteAdjunto.getNombreArchivo();

        rutaArchivo = appConfig.getApp_content_path() + empresaDto.getRfc() 
                + "/ArchivosSAR/"
                + archivoXML;
        
        File fileXML = new File(rutaArchivo);
        
        if (!fileXML.exists()){
            throw new Exception("El archivo " + fileXML.getAbsolutePath() + " no existe en el repositorio del sistema.");
        }
        
        return fileXML;
    }
    
}
