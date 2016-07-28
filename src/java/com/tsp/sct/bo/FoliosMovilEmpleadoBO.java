/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.FoliosMovilEmpleado;
import com.tsp.sct.dao.jdbc.FoliosMovilEmpleadoDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 21-dic-2015 
 */
public class FoliosMovilEmpleadoBO {
    
    FoliosMovilEmpleado folios = null;

    public FoliosMovilEmpleado getFoliosMovilEmpleado() {
        return folios;
    }

    public void setFoliosMovilEmpleado(FoliosMovilEmpleado folios) {
        this.folios = folios;
    }

    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public FoliosMovilEmpleadoBO(Connection conn) {
        this.conn = conn;
    }
    
    public FoliosMovilEmpleadoBO(int idFoliosMovilEmpleado, Connection conn) {
        this.conn = conn;
        try{
            this.folios = new FoliosMovilEmpleadoDaoImpl(this.conn).findByPrimaryKey(idFoliosMovilEmpleado);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * Realiza una búsqueda por ID FoliosMovilEmpleado en busca de
     * coincidencias
     * @param idFoliosMovilEmpleado ID de la FoliosMovilEmpleado para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     * @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public FoliosMovilEmpleado[] findFoliosMovilEmpleado(int idFoliosMovilEmpleado, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        FoliosMovilEmpleado[] foliosDto = new FoliosMovilEmpleado[0];
        FoliosMovilEmpleadoDaoImpl foliosDao = new FoliosMovilEmpleadoDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idFoliosMovilEmpleado>0){
                sqlFiltro ="ID_FOLIO_MOVIL_EMPLEADO=" + idFoliosMovilEmpleado + " AND ";
            }else{
                sqlFiltro ="ID_FOLIO_MOVIL_EMPLEADO>0 AND";
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
                    + " ORDER BY ID_FOLIO_MOVIL_EMPLEADO DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return foliosDto;
    }
    
    /**
     * Realiza una búsqueda por ID FoliosMovilEmpleado en busca del numero de
     * coincidencias
     * @param idFoliosMovilEmpleado ID de la FoliosMovilEmpleado para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     * @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public int findCantidadFoliosMovilEmpleado(int idFoliosMovilEmpleado, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        FoliosMovilEmpleadoDaoImpl foliosDao = new FoliosMovilEmpleadoDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idFoliosMovilEmpleado>0){
                sqlFiltro ="ID_FOLIO_MOVIL_EMPLEADO=" + idFoliosMovilEmpleado + " AND ";
            }else{
                sqlFiltro ="ID_FOLIO_MOVIL_EMPLEADO>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(ID_FOLIO_MOVIL_EMPLEADO) as cantidad FROM " + foliosDao.getTableName() + " WHERE " + 
                    sqlFiltro
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
    
    public String getFoliosMovilEmpleadoByIdHTMLCombo(int idEmpresa, int idSeleccionado, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            FoliosMovilEmpleado[] foliosDto = findFoliosMovilEmpleado(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (FoliosMovilEmpleado folioMovilEmpleado : foliosDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado==folioMovilEmpleado.getIdFolioMovilEmpleado())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+folioMovilEmpleado.getIdFolioMovilEmpleado()+"' "
                            + selectedStr
                            + "title='"+folioMovilEmpleado.getSerie()+"'>"
                            + folioMovilEmpleado.getSerie()
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
