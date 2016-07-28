/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SarCamposAdicionales;
import com.tsp.sct.dao.dto.SarClienteEntrega;
import com.tsp.sct.dao.dto.SarUsuarioProveedor;
import com.tsp.sct.dao.jdbc.SarCamposAdicionalesDaoImpl;
import com.tsp.sct.dao.jdbc.SarClienteEntregaDaoImpl;
import com.tsp.sct.dao.jdbc.SarUsuarioProveedorDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class SarUsuarioProveedorBO {
    
    private SarUsuarioProveedor sarUsuarioProveedor = null;

    public SarUsuarioProveedor getSarUsuarioProveedor() {
        return sarUsuarioProveedor;
    }

    public void setSarUsuarioProveedor(SarUsuarioProveedor sarUsuarioProveedor) {
        this.sarUsuarioProveedor = sarUsuarioProveedor;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SarUsuarioProveedorBO(Connection conn){
        this.conn = conn;
    }
    
    public SarUsuarioProveedorBO(int idSarUsuarioProveedor, Connection conn){
        this.conn = conn;
         try{
            SarUsuarioProveedorDaoImpl SarUsuarioProveedorDaoImpl = new SarUsuarioProveedorDaoImpl(this.conn);
            this.sarUsuarioProveedor = SarUsuarioProveedorDaoImpl.findByPrimaryKey(idSarUsuarioProveedor);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * Realiza una búsqueda por ID SarUsuarioProveedor en busca de
     * coincidencias
     * @param idSarUsuarioProveedor ID Clave principal para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar SarUsuarioProveedors, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SarUsuarioProveedor
     */
    public SarUsuarioProveedor[] findSarUsuarioProveedors(int idSarUsuarioProveedor, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SarUsuarioProveedor[] sarUsuarioProveedorDto = new SarUsuarioProveedor[0];
        SarUsuarioProveedorDaoImpl sarUsuarioProveedorDao = new SarUsuarioProveedorDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSarUsuarioProveedor>0){
                sqlFiltro ="ID_SAR_USUARIO=" + idSarUsuarioProveedor + " AND ";
            }else{
                sqlFiltro ="ID_SAR_USUARIO>0 AND";
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
            
            sarUsuarioProveedorDto = sarUsuarioProveedorDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_SAR_USUARIO ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return sarUsuarioProveedorDto;
    }
    
    public String getSarUsuarioProveedorsByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            SarUsuarioProveedor[] sarUsuarioProveedorsDto = findSarUsuarioProveedors(-1, idEmpresa, 0, 0, " AND  ");
            
            for (SarUsuarioProveedor sarUsuarioProveedorItem:sarUsuarioProveedorsDto){
                try{
                    SarClienteEntrega sarClienteEntrega = getSarClienteEntrega(sarUsuarioProveedorItem.getIdSarUsuario());
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==sarUsuarioProveedorItem.getIdSarUsuario())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+sarUsuarioProveedorItem.getIdSarUsuario()+"' "
                            + selectedStr
                            + "title='"+sarUsuarioProveedorItem.getExtSarUsuario()+"'>"
                            + "[" + sarUsuarioProveedorItem.getExtSarUsuario()+ "] " +  (sarClienteEntrega!=null?sarClienteEntrega.getExtSarRazonSocial():"SIN CLIENTE ASOCIADO")
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
    
    public SarClienteEntrega getSarClienteEntrega(){
        if (this.sarUsuarioProveedor==null)
            return null;
        return getSarClienteEntrega(this.sarUsuarioProveedor.getIdSarUsuario());
    }
    
    public SarClienteEntrega getSarClienteEntrega(int idSarUsuario){
        SarClienteEntrega sarClienteEntregaDto = null;
        try{
            SarClienteEntregaDaoImpl sarClienteEntregaDaoImpl = new SarClienteEntregaDaoImpl(conn);
            sarClienteEntregaDto = sarClienteEntregaDaoImpl.findByPrimaryKey(idSarUsuario);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return sarClienteEntregaDto;
    }
    
    public SarCamposAdicionales getSarCamposAdicionales(){
        if (this.sarUsuarioProveedor==null)
            return null;
        return getSarCamposAdicionales(this.sarUsuarioProveedor.getIdSarUsuario());
    }
    
    public SarCamposAdicionales getSarCamposAdicionales(int idSarUsuario){
        SarCamposAdicionales sarCamposAdicionalesDto = null;
        try{
            SarCamposAdicionalesDaoImpl sarCamposAdicionalesDaoImpl = new SarCamposAdicionalesDaoImpl(conn);
            sarCamposAdicionalesDto = sarCamposAdicionalesDaoImpl.findByPrimaryKey(idSarUsuario);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return sarCamposAdicionalesDto;
    }
            
}
