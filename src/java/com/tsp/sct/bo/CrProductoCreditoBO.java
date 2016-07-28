/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CrDocImprimible;
import com.tsp.sct.dao.dto.CrProductoCredito;
import com.tsp.sct.dao.dto.CrProductoPuntajeMonto;
import com.tsp.sct.dao.dto.CrProductoRegla;
import com.tsp.sct.dao.dto.CrProductoXImprimible;
import com.tsp.sct.dao.jdbc.CrProductoCreditoDaoImpl;
import com.tsp.sct.dao.jdbc.CrProductoPuntajeMontoDaoImpl;
import com.tsp.sct.dao.jdbc.CrProductoReglaDaoImpl;
import com.tsp.sct.dao.jdbc.CrProductoXImprimibleDaoImpl;
import com.tsp.sct.util.StringManage;
import com.tsp.sgfens.sesion.CrProductoCreditoSesion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author ISCesar
 */
public class CrProductoCreditoBO {
    private CrProductoCredito crProductoCredito = null;
    private String orderBy = null;

    public CrProductoCredito getCrProductoCredito() {
        return crProductoCredito;
    }

    public void setCrProductoCredito(CrProductoCredito crProductoCredito) {
        this.crProductoCredito = crProductoCredito;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CrProductoCreditoBO(Connection conn){
        this.conn = conn;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    
    public CrProductoCreditoBO(int idCrProductoCredito, Connection conn){        
        this.conn = conn; 
        try{
           CrProductoCreditoDaoImpl CrProductoCreditoDaoImpl = new CrProductoCreditoDaoImpl(this.conn);
            this.crProductoCredito = CrProductoCreditoDaoImpl.findByPrimaryKey(idCrProductoCredito);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public CrProductoCredito findCrProductoCreditobyId(int idCrProductoCredito) throws Exception{
        CrProductoCredito CrProductoCredito = null;
        
        try{
            CrProductoCreditoDaoImpl CrProductoCreditoDaoImpl = new CrProductoCreditoDaoImpl(this.conn);
            CrProductoCredito = CrProductoCreditoDaoImpl.findByPrimaryKey(idCrProductoCredito);
            if (CrProductoCredito==null){
                throw new Exception("No se encontro ningun CrProductoCredito que corresponda con los parámetros específicados.");
            }
            if (CrProductoCredito.getIdProductoCredito()<=0){
                throw new Exception("No se encontro ningun CrProductoCredito que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CrProductoCredito del usuario. Error: " + e.getMessage());
        }
        
        return CrProductoCredito;
    }
    
    /**
     * Realiza una búsqueda por ID CrProductoCredito en busca de
     * coincidencias
     * @param idCrProductoCredito ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CrProductoCredito
     */
    public CrProductoCredito[] findCrProductoCreditos(int idCrProductoCredito, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CrProductoCredito[] crProductoCreditoDto = new CrProductoCredito[0];
        CrProductoCreditoDaoImpl crProductoCreditoDao = new CrProductoCreditoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCrProductoCredito>0){
                sqlFiltro ="id_producto_credito=" + idCrProductoCredito + " AND ";
            }else{
                sqlFiltro ="id_producto_credito>0 AND";
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
            
            crProductoCreditoDto = crProductoCreditoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " " + (StringManage.getValidString(orderBy).length()>0? orderBy : "ORDER BY id_producto_credito desc")
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return crProductoCreditoDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID CrProductoCredito y otros filtros
     * @param idCrProductoCredito ID Del CrProductoCredito para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return int cantidad de registros coincidentes
     */
    public int findCantidadCrProductoCreditos(int idCrProductoCredito, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        CrProductoCreditoDaoImpl crProductoCreditoDao = new CrProductoCreditoDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrProductoCredito>0){
                sqlFiltro ="id_producto_credito=" + idCrProductoCredito + " AND ";
            }else{
                sqlFiltro ="id_producto_credito>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_producto_credito) as cantidad FROM " + crProductoCreditoDao.getTableName() +  " WHERE " + 
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
    
    public String getCrProductoCreditosByIdHTMLCombo(int idEmpresa, int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            CrProductoCredito[] crProductoCreditoesDto = findCrProductoCreditos(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (CrProductoCredito crProductoCredito : crProductoCreditoesDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado == crProductoCredito.getIdProductoCredito())
                        selectedStr = " selected ";

                    CrProductoCredito crProductoCreditoPadre = new CrProductoCreditoDaoImpl(this.conn).findByPrimaryKey(crProductoCredito.getIdProductoCreditoPadre());
                    strHTMLCombo += "<option value='"+crProductoCredito.getIdProductoCredito()+"' "
                            + selectedStr
                            + "title='"+crProductoCredito.getDescripcion()+"'>"
                            + (crProductoCreditoPadre!=null?crProductoCreditoPadre.getNombre()+ " - ":"") + crProductoCredito.getNombre()
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
    
    public String getCrProductoCreditosByIdGrupoFormularioHTMLCombo(int idEmpresa, int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            CrProductoCredito[] crProductoCreditoesDto = findCrProductoCreditos(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (CrProductoCredito crProductoCredito : crProductoCreditoesDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado == crProductoCredito.getIdProductoCredito())
                        selectedStr = " selected ";

                    CrProductoCredito crProductoCreditoPadre = new CrProductoCreditoDaoImpl(this.conn).findByPrimaryKey(crProductoCredito.getIdProductoCreditoPadre());
                    strHTMLCombo += "<option value='"+crProductoCredito.getIdGrupoFormularioSolic()+"' "
                            + selectedStr
                            + "title='"+crProductoCredito.getDescripcion()+"'>"
                            + (crProductoCreditoPadre!=null?crProductoCreditoPadre.getNombre()+ " - ":"") + crProductoCredito.getNombre()
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
    
    /**
     * Almacena de sesion hacia BD toda la información relacionada a un Producto de Credito
     * @param crProdCreditoSesion Objeto de Sesion para captura de Producto credito
     * @param user Objeto de sesion de usuario
     */
    public void guardarProductoCreditoPadreSesion(CrProductoCreditoSesion crProdCreditoSesion, UsuarioBO user) throws Exception{
        if (crProdCreditoSesion!=null){
            // Sesion --> CR_PRODUCTO_CREDITO
            boolean edicion = false;
            CrProductoCredito crProductoCredito = crProdCreditoSesion.getCrProductoCredito(); 
            if (crProductoCredito.getIdProductoCredito()>0){
                edicion = true;
                //buscamos el registro existente para asegurarnos de integridad en bd
                CrProductoCredito crProductoCreditoAux = findCrProductoCreditobyId(crProdCreditoSesion.getCrProductoCredito().getIdProductoCredito());
            }
            
            CrProductoCreditoDaoImpl crProductoCreditoDao = new CrProductoCreditoDaoImpl(this.conn);
            if (edicion){
                crProductoCreditoDao.update(crProductoCredito.createPk(), crProductoCredito);
            }else{
                crProductoCreditoDao.insert(crProductoCredito);
            }
            
            // Sesion --> CR_PRODUCTO_PUNTAJE_MONTO
            CrProductoPuntajeMontoDaoImpl crProductoPuntajeMontoDao = new CrProductoPuntajeMontoDaoImpl(this.conn);
            CrProductoPuntajeMonto[] crProductoPuntajeMontos = new CrProductoPuntajeMontoBO(this.conn).findCrProductoPuntajeMontos(-1, crProductoCredito.getIdEmpresa(), 0, 0, " AND id_producto_credito = " + crProductoCredito.getIdProductoCredito());
                // Primero eliminamos todas las relaciones de puntaje monto que existen en bd
            for (CrProductoPuntajeMonto crProductoPuntajeMonto : crProductoPuntajeMontos){
                crProductoPuntajeMontoDao.delete(crProductoPuntajeMonto.createPk());
            }
                // Ahora almacenamos las que tenemos en sesion
            for (CrProductoPuntajeMonto crProductoPuntajeMonto : crProdCreditoSesion.getListaProductoPuntajeMonto()){
                crProductoPuntajeMonto.setIdProductoCredito(crProductoCredito.getIdProductoCredito());
                crProductoPuntajeMonto.setIdEmpresa(crProductoCredito.getIdEmpresa());
                crProductoPuntajeMonto.setIdEstatus(1);
                crProductoPuntajeMontoDao.insert(crProductoPuntajeMonto);
            }
            
            // Sesion --> CR_PRODUCTO_REGLA
            CrProductoReglaDaoImpl crProductoReglaDao = new CrProductoReglaDaoImpl(this.conn);
            CrProductoRegla[] crProductoReglas = new CrProductoReglaBO(this.conn).findCrProductoReglas(-1, crProductoCredito.getIdEmpresa(), 0, 0, " AND id_producto_credito = " + crProductoCredito.getIdProductoCredito());
                // Primero eliminamos todas las relaciones de reglas existentes en bd
            for (CrProductoRegla crProductoRegla : crProductoReglas){
                crProductoReglaDao.delete(crProductoRegla.createPk());
            }
                // Ahora almacenamos las que tenemos en sesion
            for (CrProductoRegla crProductoRegla : crProdCreditoSesion.getListaReglas()){
                crProductoRegla.setIdProductoCredito(crProductoCredito.getIdProductoCredito());
                crProductoReglaDao.insert(crProductoRegla);
            }
            
            // Sesion --> CR_PRODUCTO_X_IMPRIMIBLE
            CrProductoXImprimibleDaoImpl pxiDao = new CrProductoXImprimibleDaoImpl(this.conn);
            CrProductoXImprimible[] crProductoXImprimibles = new CrProductoXImprimibleDaoImpl(this.conn).findWhereIdProductoCreditoEquals(crProductoCredito.getIdProductoCredito());
                // Primero eliminamos todas las relaciones de imprimibles existentes en bd
            for (CrProductoXImprimible pxi : crProductoXImprimibles){
                pxiDao.delete(pxi.createPk());
            }
                // Ahora almacenamos las que tenemos en sesion
            for (Integer idDocImprimible : crProdCreditoSesion.getListaIdDocImprimibles()){
                CrProductoXImprimible cpxi = new CrProductoXImprimible();
                cpxi.setIdDocImprimible(idDocImprimible);
                cpxi.setIdProductoCredito(crProductoCredito.getIdProductoCredito());
                pxiDao.insert(cpxi);
            }
            
        }else{
            throw new Exception("Producto Crédito en Sesión NULO, no se puede almacenar a base de datos.");
        }
    }
    
    /**
     * Recupera desde base de datos a objetos de sesion 
     * para captura y modificacion de Producto Credito
     * @param idProductoCredito ID del registro a recuperar
     * @return objeto de sesion CrProductoCreditoSesion
     * @throws Exception en caso de error al recuperar información desde base de datos
     */
    public CrProductoCreditoSesion recuperarProductoCreditoPadreSesion(int idProductoCredito) throws Exception{
        CrProductoCreditoSesion crProductoCreditoSesion = new CrProductoCreditoSesion(this.conn);
        
        if (idProductoCredito>0){
            CrProductoCredito crProductoCredito = findCrProductoCreditobyId(idProductoCredito);
            CrProductoPuntajeMontoBO cppmbo = new CrProductoPuntajeMontoBO(this.conn);
            cppmbo.setOrderBy(" ORDER BY pct_autorizado DESC");
            CrProductoPuntajeMonto[] crProductoPuntajeMontos  = cppmbo.findCrProductoPuntajeMontos(-1, crProductoCredito.getIdEmpresa(), 0, 0, " AND id_producto_credito = " + crProductoCredito.getIdProductoCredito());
            //CrProductoRegla[] crProductoReglas = new CrProductoReglaBO(this.conn).findCrProductoReglas(-1, crProductoCredito.getIdEmpresa(), 0, 0, " AND id_producto_credito = " + crProductoCredito.getIdProductoCredito());

            crProductoCreditoSesion.setCrProductoCredito(crProductoCredito);
            //crProductoCreditoSesion.setListaProductoPuntajeMonto(Arrays.asList(crProductoPuntajeMontos));
            for (CrProductoPuntajeMonto crProductoPuntajeMonto : crProductoPuntajeMontos ){
                crProductoPuntajeMonto.setRangoMaxModified(true);
                crProductoPuntajeMonto.setRangoMinModified(true);
                crProductoPuntajeMonto.setPctAutorizadoModified(true);
                crProductoCreditoSesion.getListaProductoPuntajeMonto().add(crProductoPuntajeMonto);
            }
            //crProductoCreditoSesion.setListaReglas(Arrays.asList(crProductoReglas));
            
            CrDocImprimible[] crDocImprimibles = consultaImprimiblesAsociados(idProductoCredito);
            for (CrDocImprimible docImprimible : crDocImprimibles){
                crProductoCreditoSesion.getListaIdDocImprimibles().add(docImprimible.getIdDocImprimible());
            }
        }
        
        return crProductoCreditoSesion;
    }
    
    public CrDocImprimible[] consultaImprimiblesAsociados(){
        if (this.crProductoCredito==null){
            return new CrDocImprimible[0];
        }else
            return consultaImprimiblesAsociados(this.crProductoCredito.getIdProductoCredito());
    }
    
    public CrDocImprimible[] consultaImprimiblesAsociados(int idProductoCredito){
        CrDocImprimible[] docImprimibles = new CrDocImprimible[0];
        CrProductoXImprimibleDaoImpl crProductoXImprimibleDao = new CrProductoXImprimibleDaoImpl(this.conn);
        
        try{
            CrDocImprimibleBO crDocImprimibleBO = new CrDocImprimibleBO(this.conn);
            CrProductoXImprimible[] crProductoXImprimibles = crProductoXImprimibleDao.findByDynamicWhere(" id_producto_credito="+ idProductoCredito, null);
            List<CrDocImprimible> listaDocImprimibles = new ArrayList<CrDocImprimible>();
            for (CrProductoXImprimible pxi : crProductoXImprimibles){
                listaDocImprimibles.add( crDocImprimibleBO.findCrDocImprimiblebyId(pxi.getIdDocImprimible()) );
            }
            docImprimibles = listaDocImprimibles.toArray(docImprimibles);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return docImprimibles;
    }
        
}
