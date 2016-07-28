/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SgfensProveedor;
import com.tsp.sct.dao.jdbc.SgfensProveedorDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISCesarMartinez
 */
public class SGProveedorBO {
    private SgfensProveedor proveedor  = null;

    public SgfensProveedor getSgfensProveedor() {
        return proveedor;
    }

    public void setSgfensProveedor(SgfensProveedor proveedor) {
        this.proveedor = proveedor;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SGProveedorBO(Connection conn){
        this.conn = conn;
    }
    
    public SGProveedorBO(int idSgfensProveedor, Connection conn){
        this.conn = conn;
        try{
            SgfensProveedorDaoImpl SgfensProveedorDaoImpl = new SgfensProveedorDaoImpl(this.conn);
            this.proveedor = SgfensProveedorDaoImpl.findByPrimaryKey(idSgfensProveedor);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * Realiza una búsqueda por ID SgfensProveedor en busca de
     * coincidencias
     * @param idSgfensProveedor ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar proveedor, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensProveedor
     */
    public SgfensProveedor[] findProveedor(int idSgfensProveedor, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SgfensProveedor[] proveedorDto = new SgfensProveedor[0];
        SgfensProveedorDaoImpl proveedorDao = new SgfensProveedorDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSgfensProveedor>0){
                sqlFiltro ="ID_PROVEEDOR=" + idSgfensProveedor + " AND ";
            }else{
                sqlFiltro ="ID_PROVEEDOR>0 AND";
            }
            if (idEmpresa>0){
                //sqlFiltro +=" ID_EMPRESA=" + idEmpresa;
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
            
            proveedorDto = proveedorDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY RAZON_SOCIAL ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return proveedorDto;
    }
    
    public String getProveedorsByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            SgfensProveedor[] proveedorsDto = findProveedor(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (SgfensProveedor itemProveedor:proveedorsDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado==itemProveedor.getIdProveedor())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+itemProveedor.getIdProveedor()+"' "
                            + selectedStr
                            + "title='"+itemProveedor.getRfc()+"'>"
                            + itemProveedor.getRazonSocial()
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
