/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Folios;
import com.tsp.sct.dao.jdbc.FoliosDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 21-dic-2012 
 */
public class FoliosBO {
    
    Folios folios = null;

    public Folios getFolios() {
        return folios;
    }

    public void setFolios(Folios folios) {
        this.folios = folios;
    }

    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public FoliosBO(Connection cobb) {
        this.conn = conn;
    }
    
    public FoliosBO(int idFolios, Connection conn) {
        this.conn = conn;
        try{
            this.folios = new FoliosDaoImpl(this.conn).findByPrimaryKey(idFolios);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * Realiza una búsqueda por ID Folios en busca de
     * coincidencias
     * @param idFolios ID de la Folios para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public Folios[] findFolios(int idFolios, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        Folios[] foliosDto = new Folios[0];
        FoliosDaoImpl foliosDao = new FoliosDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idFolios>0){
                sqlFiltro ="ID_FOLIO=" + idFolios + " AND ";
            }else{
                sqlFiltro ="ID_FOLIO>0 AND";
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
            
            foliosDto = foliosDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_FOLIO DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return foliosDto;
    }
    
    public String getFoliosByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            Folios[] foliosDto = findFolios(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 AND ID_TIPO_COMPROBANTE != 13 ");
            
            for (Folios folios:foliosDto){
                try{
                    //Folios datosFolios = new FoliosDaoImpl(this.conn).findByPrimaryKey(folios.getIdFolios());
                    String selectedStr="";

                    if (idSeleccionado==folios.getIdFolio())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+folios.getIdFolio()+"' "
                            + selectedStr
                            + "title='"+folios.getSerie()+"'>"
                            + folios.getSerie()
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
    
    public String getFoliosCbbByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            Folios[] foliosDto = findFolios(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 AND ID_TIPO_COMPROBANTE = 13 ");
            
            for (Folios folios:foliosDto){
                try{
                    //Folios datosFolios = new FoliosDaoImpl(this.conn).findByPrimaryKey(folios.getIdFolios());
                    String selectedStr="";

                    if (idSeleccionado==folios.getIdFolio())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+folios.getIdFolio()+"' "
                            + selectedStr
                            + "title='"+folios.getSerie()+"'>"
                            + folios.getSerie()
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
