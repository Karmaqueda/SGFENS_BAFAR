/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.PosMovilEstatusParametros;
import com.tsp.sct.dao.jdbc.PosMovilEstatusParametrosDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class PosMovilEstatusParametrosBO {

 private PosMovilEstatusParametros posMovilEstatusParametros = null;

    public PosMovilEstatusParametros getPosMovilEstatusParametros() {
        return posMovilEstatusParametros;
    }

    public void setPosMovilEstatusParametros(PosMovilEstatusParametros posMovilEstatusParametros) {
        this.posMovilEstatusParametros = posMovilEstatusParametros;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public PosMovilEstatusParametrosBO(Connection conn){
        this.conn = conn;
    }
    
    public PosMovilEstatusParametrosBO(int idPosMovilEstatusParametros, Connection conn){        
        this.conn = conn;
        try{
            PosMovilEstatusParametrosDaoImpl PosMovilEstatusParametrosDaoImpl = new PosMovilEstatusParametrosDaoImpl(this.conn);
            this.posMovilEstatusParametros = PosMovilEstatusParametrosDaoImpl.findByPrimaryKey(idPosMovilEstatusParametros);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public PosMovilEstatusParametros findPosMovilEstatusParametrosbyId(int idPosMovilEstatusParametros) throws Exception{
        PosMovilEstatusParametros PosMovilEstatusParametros = null;
        
        try{
            PosMovilEstatusParametrosDaoImpl PosMovilEstatusParametrosDaoImpl = new PosMovilEstatusParametrosDaoImpl(this.conn);
            PosMovilEstatusParametros = PosMovilEstatusParametrosDaoImpl.findByPrimaryKey(idPosMovilEstatusParametros);
            if (PosMovilEstatusParametros==null){
                throw new Exception("No se encontro ninguna PosMovilEstatusParametros que corresponda con los parámetros específicados.");
            }
            if (PosMovilEstatusParametros.getIdEstatusParametro()<=0){
                throw new Exception("No se encontro ninguna PosMovilEstatusParametros que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la PosMovilEstatusParametros del usuario. Error: " + e.getMessage());
        }
        
        return PosMovilEstatusParametros;
    }
    
    /**
     * Realiza una búsqueda por ID PosMovilEstatusParametros en busca de
     * coincidencias
     * @param idPosMovilEstatusParametros ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar posMovilEstatusParametross, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO PosMovilEstatusParametros
     */
    public PosMovilEstatusParametros[] findPosMovilEstatusParametross(int idPosMovilEstatusParametros, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        PosMovilEstatusParametros[] posMovilEstatusParametrosDto = new PosMovilEstatusParametros[0];
        PosMovilEstatusParametrosDaoImpl posMovilEstatusParametrosDao = new PosMovilEstatusParametrosDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idPosMovilEstatusParametros>0){
                sqlFiltro ="ID_ESTATUS_PARAMETRO=" + idPosMovilEstatusParametros + " AND ";
            }else{
                sqlFiltro ="ID_ESTATUS_PARAMETRO>0 AND";
            }
            //if (idEmpresa>0){                
              //  sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            //}else{
                sqlFiltro +=" ID_EMPRESA= " + idEmpresa;
            //}
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            posMovilEstatusParametrosDto = posMovilEstatusParametrosDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_ESTATUS_PARAMETRO DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return posMovilEstatusParametrosDto;
    }
    
    /**
     * Realiza una búsqueda por ID PosMovilEstatusParametros en busca de
     * coincidencias
     * @param idPosMovilEstatusParametros ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar posMovilEstatusParametross, -1 para evitar filtro     
     * @return String de cada una de las posMovilEstatusParametross
     */
    
      /*  public String getPosMovilEstatusParametrossByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            PosMovilEstatusParametros[] posMovilEstatusParametrossDto = findPosMovilEstatusParametross(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (PosMovilEstatusParametros posMovilEstatusParametros:posMovilEstatusParametrossDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==posMovilEstatusParametros.getIdPosMovilEstatusParametros())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+posMovilEstatusParametros.getIdPosMovilEstatusParametros()+"' "
                            + selectedStr
                            + "title='"+posMovilEstatusParametros.getNombre()+"'>"
                            + posMovilEstatusParametros.getNombre()
                            +"</option>";
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return strHTMLCombo;
    }*/
}
