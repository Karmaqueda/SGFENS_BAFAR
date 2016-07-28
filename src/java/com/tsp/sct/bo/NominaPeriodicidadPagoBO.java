/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.NominaPeriodicidadPago;
import com.tsp.sct.dao.exceptions.NominaPeriodicidadPagoDaoException;
import com.tsp.sct.dao.jdbc.NominaPeriodicidadPagoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class NominaPeriodicidadPagoBO {
    
    private NominaPeriodicidadPago nominaPeriodicidadPago = null;

    public NominaPeriodicidadPago getNominaPeriodicidadPago() {
        return nominaPeriodicidadPago;
    }

    public void setNominaPeriodicidadPago(NominaPeriodicidadPago nominaPeriodicidadPago) {
        this.nominaPeriodicidadPago = nominaPeriodicidadPago;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public NominaPeriodicidadPagoBO(Connection conn){
        this.conn = conn;
    }
       
    
    public NominaPeriodicidadPagoBO(int idNominaPeriodicidadPago, Connection conn){
        this.conn = conn;
        try{
            NominaPeriodicidadPagoDaoImpl NominaPeriodicidadPagoDaoImpl = new NominaPeriodicidadPagoDaoImpl(this.conn);
            this.nominaPeriodicidadPago = NominaPeriodicidadPagoDaoImpl.findByPrimaryKey(idNominaPeriodicidadPago);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public NominaPeriodicidadPago findMarcabyId(int idNominaPeriodicidadPago) throws Exception{
        NominaPeriodicidadPago NominaPeriodicidadPago = null;
        
        try{
            NominaPeriodicidadPagoDaoImpl NominaPeriodicidadPagoDaoImpl = new NominaPeriodicidadPagoDaoImpl(this.conn);
            NominaPeriodicidadPago = NominaPeriodicidadPagoDaoImpl.findByPrimaryKey(idNominaPeriodicidadPago);
            if (NominaPeriodicidadPago==null){
                throw new Exception("No se encontro ninguna Periodicidad Pago que corresponda con los parámetros específicados.");
            }
            if (NominaPeriodicidadPago.getIdPeriodicidadPago()<=0){
                throw new Exception("No se encontro ninguna Periodicidad Pago que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del NominaPeriodicidadPago del usuario. Error: " + e.getMessage());
        }
        
        return NominaPeriodicidadPago;
    }
    
    public NominaPeriodicidadPago getNominaPeriodicidadPagoGenericoByEmpresa(int idEmpresa) throws Exception{
        NominaPeriodicidadPago nominaPeriodicidadPago = null;
        
        try{
            NominaPeriodicidadPagoDaoImpl nominaPeriodicidadPagoDaoImpl = new NominaPeriodicidadPagoDaoImpl(this.conn);
            nominaPeriodicidadPago = nominaPeriodicidadPagoDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (nominaPeriodicidadPago==null){
                throw new Exception("La empresa no tiene creada alguna Periodicidad Pago");
            }
        }catch(NominaPeriodicidadPagoDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada alguna Periodicidad Pago");
        }
        
        return nominaPeriodicidadPago;
    }
    
    /**
     * Realiza una búsqueda por ID NominaPeriodicidadPago en busca de
     * coincidencias
     * @param idPago ID Del Pago para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public NominaPeriodicidadPago[] findNominaPeriodicidadPagos(int idNominaPeriodicidadPago, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        NominaPeriodicidadPago[] nominaPeriodicidadPagoDto = new NominaPeriodicidadPago[0];
        NominaPeriodicidadPagoDaoImpl nominaPeriodicidadPagoDao = new NominaPeriodicidadPagoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idNominaPeriodicidadPago>0){
                sqlFiltro ="ID_PERIODICIDAD_PAGO=" + idNominaPeriodicidadPago + " AND ";
            }else{
                sqlFiltro ="ID_PERIODICIDAD_PAGO>0 AND";
            }
            if (idEmpresa>0){                
                sqlFiltro += " ((ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")) OR ID_EMPRESA = 0 )";
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
            
            nominaPeriodicidadPagoDto = nominaPeriodicidadPagoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NOMBRE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return nominaPeriodicidadPagoDto;
    }
    
    /**
     * Realiza una búsqueda por ID NominaPeriodicidadPago en busca de
     * coincidencias
     * @param idNominaPeriodicidadPago ID Del Pago para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar nominaPeriodicidadPagos, -1 para evitar filtro     
     * @return String de cada uno de los nominaPeriodicidadPagos
     */
    
        public String getNominaPeriodicidadPagosByIdHTMLCombo(int idEmpresa, String idSeleccionado){
        String strHTMLCombo ="";

        try{
            NominaPeriodicidadPago[] nominaPeriodicidadPagosDto = findNominaPeriodicidadPagos(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (NominaPeriodicidadPago nominaPeriodicidadPago:nominaPeriodicidadPagosDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado.equals(nominaPeriodicidadPago.getNombre()))
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+nominaPeriodicidadPago.getNombre()+"' "
                            + selectedStr
                            + "title='"+nominaPeriodicidadPago.getDescripcion()+"'>"
                            + nominaPeriodicidadPago.getNombre()
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
