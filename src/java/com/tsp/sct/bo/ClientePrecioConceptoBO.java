/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.ClientePrecioConcepto;
import com.tsp.sct.dao.jdbc.ClientePrecioConceptoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author HpPyme
 */
public class ClientePrecioConceptoBO {
    
    
    private ClientePrecioConcepto clientePrecioConcepto = null;
    private Connection conn = null;

    public ClientePrecioConcepto getClientePrecioConcepto() {
        return clientePrecioConcepto;
    }

    public void setClientePrecioConcepto(ClientePrecioConcepto clientePrecioConcepto) {
        this.clientePrecioConcepto = clientePrecioConcepto;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

      
    public ClientePrecioConceptoBO(Connection conn) {
        this.conn = conn;
    }
    
    public ClientePrecioConceptoBO(int idCliente, int idConcepto ,Connection conn){        
        this.conn = conn; 
        try{
           ClientePrecioConceptoDaoImpl clientePrecioConceptoDaoImpl = new ClientePrecioConceptoDaoImpl(this.conn);
           this.clientePrecioConcepto = clientePrecioConceptoDaoImpl.findByPrimaryKey(idCliente,idConcepto);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    
    
    /**
     * Realiza una búsqueda por ID Concepto y otros filtros en busca de
     * coincidencias
     * @param idCliente ID Del Cliente para filtrar, -1 para mostrar todos los registros
     * @param idConcepto ID Del Concepto para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     * @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public ClientePrecioConcepto[] findClienteConceptos(int idCliente ,int idConcepto, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        ClientePrecioConcepto[] preciosEspecialesDto = new ClientePrecioConcepto[0];
        ClientePrecioConceptoDaoImpl preciosEspecialesDao = new ClientePrecioConceptoDaoImpl(this.conn);
        
        
        try {
            String sqlFiltro="";
            if (idCliente>0){
                sqlFiltro +=" ID_CLIENTE=" + idCliente + " AND ";
            }else{
                sqlFiltro +=" ID_CLIENTE>0 AND ";
            }
            if (idConcepto>0){
                sqlFiltro +=" ID_CONCEPTO=" + idConcepto + " AND ";
            }else{
                sqlFiltro +=" ID_CONCEPTO>0 AND ";
            }
            if (idEmpresa>0){                
                sqlFiltro += " ID_CONCEPTO IN (SELECT ID_CONCEPTO FROM concepto WHERE ID_EMPRESA IN (SELECT ID_EMPRESA FROM empresa WHERE ID_EMPRESA_PADRE =  " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + " ))";
            }else{
                sqlFiltro +=" ID_CONCEPTO IN (SELECT ID_CONCEPTO FROM concepto WHERE ID_EMPRESA IN (SELECT ID_EMPRESA FROM empresa WHERE ID_EMPRESA > 0 ))";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            preciosEspecialesDto = preciosEspecialesDao.findByDynamicWhere( 
                    sqlFiltro
                   // + " ORDER BY NOMBRE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return preciosEspecialesDto;
    }
    
    
    
    public String getPreciosEspecialesByIdHTMLCombo(int idCliente ,int idConcepto, int idEmpresa, int minLimit , int maxLimit , String filtro, String tipoPrecio){
        String strHTMLCombo ="";
        
        ClientePrecioConceptoBO clientePrecioConceptoBO = new ClientePrecioConceptoBO(this.conn);
       
        try{
            
            String filtroTipoPrecio = "";
            String agrupar = "";
            
            if(tipoPrecio.equals("Especial")){
                filtroTipoPrecio = " AND PRECIO_ESPECIAL_CLIENTE > 0 " ;
                agrupar = " GROUP BY PRECIO_ESPECIAL_CLIENTE ";
            }else if(tipoPrecio.equals("Unitario")){
                filtroTipoPrecio = " AND PRECIO_UNITARIO_CLIENTE > 0 " ;
                agrupar = " GROUP BY PRECIO_UNITARIO_CLIENTE ";
            }else if(tipoPrecio.equals("Medio Mayoreo")){
                filtroTipoPrecio = " AND PRECIO_MEDIO_CLIENTE > 0 " ;
                agrupar = " GROUP BY PRECIO_MEDIO_CLIENTE ";
            }else if(tipoPrecio.equals("Mayoreo")){
                filtroTipoPrecio = " AND PRECIO_MAYOREO_CLIENTE > 0 " ;
                agrupar = " GROUP BY PRECIO_MAYOREO_CLIENTE ";
            }else if(tipoPrecio.equals("Docena")){
                filtroTipoPrecio = " AND PRECIO_DOCENA_CLIENTE > 0 " ;
                agrupar = " GROUP BY PRECIO_DOCENA_CLIENTE ";
            }
            
                    
            ClientePrecioConcepto[] clientePrecioConceptoDtos = findClienteConceptos(idCliente,idConcepto, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " 
                    + filtroTipoPrecio +" "+ filtro + agrupar  );
            
            for (ClientePrecioConcepto conceptoPrecio:clientePrecioConceptoDtos){
                try{
                    double precio = 0;
                    if(tipoPrecio.equals("Especial")){
                        precio = conceptoPrecio.getPrecioEspecialCliente();
                    }else if(tipoPrecio.equals("Unitario")){
                        precio = conceptoPrecio.getPrecioUnitarioCliente();
                    }else if(tipoPrecio.equals("Medio Mayoreo")){
                        precio = conceptoPrecio.getPrecioMedioCliente();
                    }else if(tipoPrecio.equals("Mayoreo")){
                        precio = conceptoPrecio.getPrecioMayoreoCliente();
                    }else if(tipoPrecio.equals("Docena")){
                        precio = conceptoPrecio.getPrecioDocenaCliente();
                    }
                
                    
                    strHTMLCombo += "<option value='"+conceptoPrecio+"' "                            
                            + "title='" + precio + "'/>"                          
                            + precio
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
    
    
    public ClientePrecioConcepto[] findClientePrecioEspecial(int idCliente ,int idConcepto,int minLimit, int maxLimit,String filtroBusqueda) {
        ClientePrecioConcepto[] preciosEspecialesDto = new ClientePrecioConcepto[0];
        ClientePrecioConceptoDaoImpl preciosEspecialesDao = new ClientePrecioConceptoDaoImpl(this.conn);
        
        
        try {
            String sqlFiltro="";
            
            if (idCliente>0){
                sqlFiltro +=" ID_CLIENTE=" + idCliente + " AND ";
            }
            
            if (idConcepto>0){
                sqlFiltro +=" ID_CONCEPTO=" + idConcepto + " ";
            }
            
                       
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            preciosEspecialesDto = preciosEspecialesDao.findByDynamicWhere( 
                    sqlFiltro
                   // + " ORDER BY NOMBRE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return preciosEspecialesDto;
    }
}
