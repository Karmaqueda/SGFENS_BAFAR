/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.PosMovilEstatus;
import com.tsp.sct.dao.exceptions.PosMovilEstatusDaoException;
import com.tsp.sct.dao.jdbc.PosMovilEstatusDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */

public class PosMovilEstatusBO {
 private PosMovilEstatus posMovilEstatus = null;

    public PosMovilEstatus getPosMovilEstatus() {
        return posMovilEstatus;
    }

    public void setPosMovilEstatus(PosMovilEstatus posMovilEstatus) {
        this.posMovilEstatus = posMovilEstatus;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public PosMovilEstatusBO(Connection conn){
        this.conn = conn;
    }
    
    public PosMovilEstatusBO(int idPosMovilEstatus, Connection conn){        
        this.conn = conn;
        try{
            PosMovilEstatusDaoImpl PosMovilEstatusDaoImpl = new PosMovilEstatusDaoImpl(this.conn);
            this.posMovilEstatus = PosMovilEstatusDaoImpl.findByPrimaryKey(idPosMovilEstatus);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public PosMovilEstatus findPosMovilEstatusbyId(int idPosMovilEstatus) throws Exception{
        PosMovilEstatus PosMovilEstatus = null;
        
        try{
            PosMovilEstatusDaoImpl PosMovilEstatusDaoImpl = new PosMovilEstatusDaoImpl(this.conn);
            PosMovilEstatus = PosMovilEstatusDaoImpl.findByPrimaryKey(idPosMovilEstatus);
            if (PosMovilEstatus==null){
                throw new Exception("No se encontro ninguna PosMovilEstatus que corresponda con los parámetros específicados.");
            }
            if (PosMovilEstatus.getIdMovilEstatus()<=0){
                throw new Exception("No se encontro ninguna PosMovilEstatus que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la PosMovilEstatus del usuario. Error: " + e.getMessage());
        }
        
        return PosMovilEstatus;
    }
    
    /**
     * Realiza una búsqueda por ID PosMovilEstatus en busca de
     * coincidencias
     * @param idPosMovilEstatus ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar posMovilEstatuss, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO PosMovilEstatus
     */
    public PosMovilEstatus[] findPosMovilEstatuss(int idPosMovilEstatus, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        PosMovilEstatus[] posMovilEstatusDto = new PosMovilEstatus[0];
        PosMovilEstatusDaoImpl posMovilEstatusDao = new PosMovilEstatusDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idPosMovilEstatus>0){
                sqlFiltro ="ID_MOVIL_ESTATUS=" + idPosMovilEstatus + " AND ";
            }else{
                sqlFiltro ="ID_MOVIL_ESTATUS>0 AND";
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
            
            posMovilEstatusDto = posMovilEstatusDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_MOVIL_ESTATUS DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return posMovilEstatusDto;
    }
    
    /**
     * Realiza una búsqueda por ID PosMovilEstatus en busca de
     * coincidencias
     * @param idPosMovilEstatus ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar posMovilEstatuss, -1 para evitar filtro     
     * @return String de cada una de las posMovilEstatuss
     */
    
      /*  public String getPosMovilEstatussByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            PosMovilEstatus[] posMovilEstatussDto = findPosMovilEstatuss(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (PosMovilEstatus posMovilEstatus:posMovilEstatussDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==posMovilEstatus.getIdPosMovilEstatus())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+posMovilEstatus.getIdPosMovilEstatus()+"' "
                            + selectedStr
                            + "title='"+posMovilEstatus.getNombre()+"'>"
                            + posMovilEstatus.getNombre()
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
