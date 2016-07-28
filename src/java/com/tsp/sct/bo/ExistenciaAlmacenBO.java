/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;


import com.tsp.sct.dao.dto.Concepto;
import com.tsp.sct.dao.dto.ExistenciaAlmacen;
import com.tsp.sct.dao.jdbc.ConceptoDaoImpl;
import com.tsp.sct.dao.jdbc.ExistenciaAlmacenDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author HpPyme
 */
public class ExistenciaAlmacenBO {
    
    private ExistenciaAlmacen existenciaAlmacen = null;
    Connection conn = null;

    public ExistenciaAlmacen getExistenciaAlmacen() {
        return existenciaAlmacen;
    }

    public void setExistenciaAlmacen(ExistenciaAlmacen existenciaAlmacen) {
        this.existenciaAlmacen = existenciaAlmacen;
    }


    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public ExistenciaAlmacenBO(Connection conn) {
        this.conn = conn;
    }
    
    
    public ExistenciaAlmacenBO(int idAlmacen, int idConcepto ,Connection conn){        
        this.conn = conn; 
        try{
           ExistenciaAlmacenDaoImpl existenciaAlmacenDaoImpl = new ExistenciaAlmacenDaoImpl(this.conn);
           this.existenciaAlmacen = existenciaAlmacenDaoImpl.findByPrimaryKey(idAlmacen,idConcepto);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * Obtiene la existencia de un producto en todos los almacenes
     * @param idEmpresa Empresa a la que pertenece el producto
     * @param idProducto Producto del que deseamos conocer la existencia
     * @return double stock genetal producto
     */
    public double getExistenciaGeneralByEmpresaProducto(int idEmpresa, int idProducto){
        double existenciaGral = 0;
        ExistenciaAlmacen[] existenciaAlmacen = new ExistenciaAlmacen[0];
        
        try{
           ExistenciaAlmacenDaoImpl existenciaAlmacenDaoImpl = new ExistenciaAlmacenDaoImpl(this.conn);
           existenciaAlmacen = existenciaAlmacenDaoImpl.findByDynamicWhere(" ESTATUS <> 2 AND ID_CONCEPTO = " + idProducto +  " AND ID_ALMACEN IN (SELECT ID_ALMACEN FROM ALMACEN WHERE ID_ESTATUS = 1 AND ID_EMPRESA = " + idEmpresa + " ) "  , null);
           
           for(ExistenciaAlmacen item:existenciaAlmacen){
               
               existenciaGral += item.getExistencia();
           }           
           
        }catch(Exception e){
            e.printStackTrace();
        }       
        
        
        return existenciaGral;
    
    }
    
    /**
     * Obtiene la existencia de Peso (granel) de un producto en todos los almacenes
     * @param idEmpresa Empresa a la que pertenece el producto
     * @param idProducto Producto del que deseamos conocer la existencia
     * @return double stock general en PESO del producto
     */
    public double getExistenciaPesoGeneralByEmpresaProducto(int idEmpresa, int idProducto){
        double existenciaGral = 0;
        
        try{
           //existenciaAlmacen = existenciaAlmacenDaoImpl.findByDynamicWhere(" ESTATUS <> 2 AND ID_CONCEPTO = " + idProducto +  " AND ID_ALMACEN IN (SELECT ID_ALMACEN FROM ALMACEN WHERE ID_ESTATUS = 1 AND ID_EMPRESA = " + idEmpresa + " ) "  , null);
           
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SUM(EXISTENCIA_PESO) as suma FROM existencia_almacen WHERE " 
                    + " ESTATUS <> 2 AND ID_CONCEPTO = " + idProducto +  " AND ID_ALMACEN IN (SELECT ID_ALMACEN FROM ALMACEN WHERE ID_ESTATUS = 1 AND ID_EMPRESA = " + idEmpresa + " ) ");
            if(rs.next()){
                existenciaGral = rs.getDouble("suma");
            }
           
        }catch(Exception e){
            e.printStackTrace();
        }       
        
        
        return existenciaGral;
    
    }
    
    /**
     * Obtiene la existencia de un producto en un almacen especifico
     * @param idAlmacen
     * @param idProducto Producto del que deseamos conocer la existencia
     * @return double Cantidad de productos en el almacen elegido
     */
    
    public double getExistenciaProductoAlmacenDouble(int idAlmacen, int idProducto){
        double existencia = 0;
        ExistenciaAlmacen[] existenciaAlmacen = new ExistenciaAlmacen[0];
        
        try{
           ExistenciaAlmacenDaoImpl existenciaAlmacenDaoImpl = new ExistenciaAlmacenDaoImpl(this.conn);
           existenciaAlmacen = existenciaAlmacenDaoImpl.findByDynamicWhere(" ID_ALMACEN = " + idAlmacen + " AND ID_CONCEPTO = " + idProducto, null);
           
           if(existenciaAlmacen.length > 0){
               for(ExistenciaAlmacen item:existenciaAlmacen){
                   existencia += item.getExistencia();
               } 
           }
           
        }catch(Exception e){
            e.printStackTrace();
        }               
        
        return existencia;
    
    }
    
    /**
     * Obtiene la existencia de un producto en un almacen especifico
     * @param idAlmacen
     * @param idProducto
     * @return ExistenciaAlmacen
     */
    public ExistenciaAlmacen getExistenciaProductoAlmacen(int idAlmacen, int idProducto){
        ExistenciaAlmacen existencia = null;
        ExistenciaAlmacen[] existenciaAlmacen = new ExistenciaAlmacen[0];
        
        try{
           ExistenciaAlmacenDaoImpl existenciaAlmacenDaoImpl = new ExistenciaAlmacenDaoImpl(this.conn);
           existenciaAlmacen = existenciaAlmacenDaoImpl.findByDynamicWhere(" ID_ALMACEN = " + idAlmacen + " AND ID_CONCEPTO = " + idProducto, null);           
           
           if(existenciaAlmacen.length>0){
            existencia = existenciaAlmacen[0];          
           }
           
        }catch(Exception e){
            e.printStackTrace();
        }               
        
        return existencia;
    
    }
    
    
    /**
     * Obtiene la existencia de un producto en el almacen principal
     * @param idEmpresa
     * @param idProducto
     * @return ExistenciaAlmacen
     */
    public ExistenciaAlmacen getExistenciaProductoAlmacenPrincipal(int idEmpresa, int idProducto){
        
        ExistenciaAlmacen existencia = null;
        ExistenciaAlmacen[] existenciaAlmacen = new ExistenciaAlmacen[0];
        
        try{
           ExistenciaAlmacenDaoImpl existenciaAlmacenDaoImpl = new ExistenciaAlmacenDaoImpl(this.conn);
           existenciaAlmacen = existenciaAlmacenDaoImpl.findByDynamicWhere(" ESTATUS <> 2 AND ID_CONCEPTO = " + idProducto +  " AND ID_ALMACEN IN (SELECT ID_ALMACEN FROM ALMACEN WHERE ID_ESTATUS = 1 AND ISPRINCIPAL = 1 AND ID_EMPRESA = " + idEmpresa + " ) "  , null);
           
           if(existenciaAlmacen.length>0){
               existencia = existenciaAlmacen[0];          
           }
           
        }catch(Exception e){
            e.printStackTrace();
        }               
        
       
        return existencia;
    
    }
    
   
    public void updateBD(ExistenciaAlmacen existAlmacen){
        //Checamos primero si se ha alcanzado el stock minimo
        if (existAlmacen !=null){
            
            Concepto concepto =  new ConceptoBO(existAlmacen.getIdConcepto(),this.conn).getConcepto();            
            double stockNuevo = new ExistenciaAlmacenBO(this.conn).getExistenciaGeneralByEmpresaProducto(concepto.getIdEmpresa(), concepto.getIdConcepto());            
            double stockMinimo = concepto.getStockMinimo();
            if (concepto.getStockAvisoMin()==(short)1){
                if (stockNuevo<=stockMinimo)
                    
                    new ConceptoBO(this.conn).enviaCorreoNotificacionStockMinimo(concepto);
            }
            
            try{
                new ExistenciaAlmacenDaoImpl(this.conn).update(existAlmacen.createPk(), existAlmacen);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
        }
        
    }
    
    
    
    /**
     * Realiza una búsqueda por ID Empleado en busca de
     * coincidencias
     * @param idAlmacen ID Del Almacén para filtrar, -1 para mostrar todos los registros
     * @param idConcepto ID del concepto a filtrar , -1 para evitar filtro
     * @param idEmpresa ID de la empresa a filtrar , -1 para evitar filtro
     * @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Empleado
     */
    public ExistenciaAlmacen[] findExistencias(int idAlmacen, int idConcepto, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        ExistenciaAlmacen[] existenciasDto = new ExistenciaAlmacen[0];
        ExistenciaAlmacenDaoImpl existenciaDao = new ExistenciaAlmacenDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idAlmacen>0){
             
                sqlFiltro +=" ID_ALMACEN = " + idAlmacen + " AND ";
            }else{               
                sqlFiltro +=" ID_ALMACEN > 0  AND ";
            }
            if (idConcepto>0){
             
                sqlFiltro +=" ID_CONCEPTO =" + idConcepto + " AND ";
            }else{               
                sqlFiltro +=" ID_CONCEPTO > 0  AND ";
            }            
            
            if (idEmpresa>0){               
                sqlFiltro += " ID_ALMACEN IN (SELECT ID_ALMACEN FROM almacen WHERE  ID_ESTATUS = 1 AND ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_ESTATUS = 1 AND ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + " ) ) ";
            }else{
                sqlFiltro += " ID_ALMACEN IN (SELECT ID_ALMACEN FROM almacen WHERE  ID_ESTATUS = 1 AND ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_ESTATUS = 1 AND ID_EMPRESA > 0 ) ) ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            existenciasDto = existenciaDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_CONCEPTO,ID_ALMACEN ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return existenciasDto;
    }
    
    
    
    
}
