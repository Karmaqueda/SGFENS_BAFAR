/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.VistaResumenFacturasPorSucursal;
import com.tsp.sct.dao.exceptions.VistaResumenFacturasPorSucursalDaoException;
import com.tsp.sct.dao.jdbc.VistaResumenFacturasPorSucursalDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class VistaResumenFacturasPorSucursalBO {
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public VistaResumenFacturasPorSucursalBO(Connection conn){
        this.conn = conn;
    }
    
    /**
     * Realiza una búsqueda por ID VistaResumenFacturasPorSucursal en busca de
     * coincidencias
     * @param idEmpresa ID de la Empresa a filtrar VistaResumenFacturasPorSucursals, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO VistaResumenFacturasPorSucursal
     */
    public VistaResumenFacturasPorSucursal[] findVistaResumenFacturasPorSucursals(int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        VistaResumenFacturasPorSucursal[] vistaResumenFacturasPorSucursalDto = new VistaResumenFacturasPorSucursal[0];
        VistaResumenFacturasPorSucursalDaoImpl vistaResumenFacturasPorSucursalDao = new VistaResumenFacturasPorSucursalDaoImpl(this.conn);
        try {
            String sqlQuery = "SELECT ID_EMPRESA, COUNT(ID_COMPROBANTE_FISCAL) AS 'NUMERO_COMPROBANTES', SUM(IMPORTE_SUBTOTAL) AS 'SUMA_SUBTOTALES', SUM(IMPORTE_NETO) AS 'SUMA_TOTALES', ID_ESTATUS "
                    + " FROM COMPROBANTE_FISCAL ";
            
            
            String sqlFiltro="";
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
            
            /*
            vistaResumenFacturasPorSucursalDto = vistaResumenFacturasPorSucursalDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_EMPRESA, ID_ESTATUS DESC"
                    + sqlLimit
                    , new Object[0]);
            * 
            */
            sqlQuery += " WHERE " + sqlFiltro
                    + " GROUP BY ID_EMPRESA, ID_ESTATUS"
                    + sqlLimit;
            
            vistaResumenFacturasPorSucursalDto = vistaResumenFacturasPorSucursalDao.findByDynamicSelect(sqlQuery, null);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return vistaResumenFacturasPorSucursalDto;
    }
    
}
