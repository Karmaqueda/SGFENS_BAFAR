/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Impuesto;
import com.tsp.sct.dao.exceptions.ImpuestoDaoException;
import com.tsp.sct.dao.jdbc.ImpuestoDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesarMartinez
 */
public class ImpuestoBO {
    
    private Impuesto impuesto= null;

    public Impuesto getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(Impuesto impuesto) {
        this.impuesto = impuesto;
    }
        
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public ImpuestoBO(Connection conn){
        this.conn = conn;
    }
    
    public ImpuestoBO(int idImpuesto, Connection conn){        
        this.conn = conn;
        try{
            ImpuestoDaoImpl ImpuestoDaoImpl = new ImpuestoDaoImpl(this.conn);
            this.impuesto = ImpuestoDaoImpl.findByPrimaryKey(idImpuesto);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    
    public Impuesto findImpuestobyId(int idImpuesto) throws Exception{
        Impuesto impuesto = null;
        
        try{
            ImpuestoDaoImpl impuestoDaoImpl = new ImpuestoDaoImpl(this.conn);
            impuesto = impuestoDaoImpl.findByPrimaryKey(idImpuesto);
            if (impuesto==null){
                throw new Exception("No se encontro ningun Impuesto que corresponda con los parámetros específicados. ID Impuesto: " +  idImpuesto);
            }
            if (impuesto.getIdImpuesto()<=0){
                throw new Exception("No se encontro ningun Impuesto que corresponda con los parámetros específicados. ID Impuesto: " +  idImpuesto);
            }
        }catch(ImpuestoDaoException e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de Impuesto del usuario. Error: " + e.getMessage());
        }
        
        return impuesto;
    }
    
    public Impuesto findImpuestoIVA16byEmpresa(int idEmpresa) throws Exception{
        Impuesto impuesto = null;
        
        try{
            ImpuestoDaoImpl ImpuestoDaoImpl = new ImpuestoDaoImpl(this.conn);
            Impuesto[] impuestos = new Impuesto[0];
            impuestos = ImpuestoDaoImpl.findByDynamicWhere("NOMBRE='IVA' AND TRASLADADO=1 AND IMPUESTO_LOCAL=0 AND PORCENTAJE=16 AND ID_EMPRESA="+idEmpresa, new Object[0]);
            if (impuestos.length>0){
                impuesto = impuestos[0];
            }else{
                //throw new Exception("No se encontro ningun Impuesto de IVA 16 por defecto que corresponda con los parámetros específicados. ID Empresa: " +  idEmpresa);
                return crearImpuestoGenericoIVA16ByEmpresa(idEmpresa);
            }
        }catch(ImpuestoDaoException e){
            throw new Exception("Error inesperado mientras se intentaba recuperar la información de Impuesto IVA 16% de la empresa. Error: " + e.toString());
        }
        
        return impuesto;
    }
    
    /**
     * Realiza una búsqueda por ID Impuesto en busca de
     * coincidencias
     * @param idImpuesto ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar Impuestos, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Impuesto
     */
    public Impuesto[] findImpuestos(int idImpuesto, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        Impuesto[] impuestoDto = new Impuesto[0];
        ImpuestoDaoImpl impuestoDao = new ImpuestoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idImpuesto>0){
                sqlFiltro ="ID_IMPUESTO=" + idImpuesto + " AND ";
            }else{
                sqlFiltro ="ID_IMPUESTO>0 AND";
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
            
            impuestoDto = impuestoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NOMBRE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return impuestoDto;
    }
    
    /**
     * Realiza una búsqueda por ID Impuesto en busca de
     * coincidencias
     * @param idImpuesto ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar Impuesto, -1 para evitar filtro     
     * @return String de cada una de las Impuesto
     */
    
        public String getImpuestosByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            //Impuesto[] impuestosDto = findImpuestos(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
/****
* ATENCION:  POR EL MOMENTO SOLO IMPUESTOS FEDERALES, 
* YA QUE NO EXISTIRAN COMPLEMENTOS EN LA PRIMER ETAPA
*/
            Impuesto[] impuestosDto = findImpuestos(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (Impuesto marca:impuestosDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==marca.getIdImpuesto())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+marca.getIdImpuesto()+"' "
                            + selectedStr
                            + "title='"+marca.getNombre()+"'>"
                            + marca.getDescripcion() + " (" + marca.getPorcentaje() + "%)"
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
     * Crea el registro de un impuesto Genérico IVA tasa 16 para la empresa indicada.
     * 
     * @param idEmpresa ID de la empresa
     * @return Impuesto Objeto DTO de Impuesto
     * @throws Exception En caso de no poder crear el registro. 
     */
    public Impuesto crearImpuestoGenericoIVA16ByEmpresa(int idEmpresa) throws Exception{
        Impuesto impuestoDto = new Impuesto();

        try{
            ImpuestoDaoImpl impuestosDaoImpl = new ImpuestoDaoImpl(this.conn) ;
            
            //Calculamos ID siguiente en tabla de impuestos
            int idImpuestoNuevo;
            Impuesto ultimoRegistroImpuestos = impuestosDaoImpl.findLast();
            idImpuestoNuevo = ultimoRegistroImpuestos.getIdImpuesto() + 1;
            
            impuestoDto.setIdImpuesto(idImpuestoNuevo);
            impuestoDto.setIdEmpresa(idEmpresa);
            impuestoDto.setIdEstatus(2); //Inactivo, para no mostrarse en listados
            impuestoDto.setNombre("IVA");
            impuestoDto.setDescripcion("IMPUESTO AL VALOR AGREGADO");
            impuestoDto.setPorcentaje(16);
            impuestoDto.setTrasladado((short)1);
            impuestoDto.setImpuestoLocal((short)0);
            
            impuestosDaoImpl.insert(impuestoDto);
        }catch(Exception ex){
            impuestoDto = null;
            ex.printStackTrace();
            throw new Exception("No se pudo crear el registro de Impuesto Genérico de Tasa 16 para la empresa con ID: " + idEmpresa + ". " + ex.toString());
        }
        
        return impuestoDto;
    }
    
    public int getCantidadByImpuesto(String filtroBusqueda){
        int cantidad = 0;
        try{
            //Connection conne = ResourceManager.getConnection();
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(ID_IMPUESTO) as cantidad FROM IMPUESTO WHERE " + filtroBusqueda);
            if(rs.next()){
                cantidad = rs.getInt("cantidad");
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return cantidad;
    }
    
}
