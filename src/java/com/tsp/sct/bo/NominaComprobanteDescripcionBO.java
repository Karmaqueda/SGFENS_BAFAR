/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.NominaComprobanteDescripcion;
import com.tsp.sct.dao.jdbc.NominaComprobanteDescripcionDaoImpl;
import java.sql.Connection;

/**
 *
 * @author leonardo
 */
public class NominaComprobanteDescripcionBO {
    
    NominaComprobanteDescripcion nominaComprobanteDescripcion = null;

    public NominaComprobanteDescripcion getNominaComprobanteDescripcion() {
        return nominaComprobanteDescripcion;
    }

    public void setNominaComprobanteDescripcion(NominaComprobanteDescripcion nominaComprobanteDescripcion) {
        this.nominaComprobanteDescripcion = nominaComprobanteDescripcion;
    }

    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public NominaComprobanteDescripcionBO(Connection cobb) {
        this.conn = conn;
    }
    
    public NominaComprobanteDescripcionBO(int idNominaComprobanteDescripcion, Connection conn) {
        this.conn = conn;
        try{
            this.nominaComprobanteDescripcion = new NominaComprobanteDescripcionDaoImpl(this.conn).findByPrimaryKey(idNominaComprobanteDescripcion);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * Realiza una búsqueda por ID NominaComprobanteDescripcion en busca de
     * coincidencias
     * @param idNominaComprobanteDescripcion ID de la NominaComprobanteDescripcion para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar nominaComprobanteDescripcion, -1 para evitar filtro
     * @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public NominaComprobanteDescripcion[] findNominaComprobanteDescripcion(int idNominaComprobanteDescripcion, int minLimit,int maxLimit, String filtroBusqueda) {
        NominaComprobanteDescripcion[] nominaComprobanteDescripcionDto = new NominaComprobanteDescripcion[0];
        NominaComprobanteDescripcionDaoImpl nominaComprobanteDescripcionDao = new NominaComprobanteDescripcionDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idNominaComprobanteDescripcion>0){
                sqlFiltro ="ID_NOMINA_COMPROBANTE_DESCRIPCION=" + idNominaComprobanteDescripcion + " ";
            }else{
                sqlFiltro ="ID_NOMINA_COMPROBANTE_DESCRIPCION>0 ";
            }
            /*if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            }else{
                sqlFiltro +=" ID_EMPRESA>0";
            }*/
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            nominaComprobanteDescripcionDto = nominaComprobanteDescripcionDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_NOMINA_COMPROBANTE_DESCRIPCION DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return nominaComprobanteDescripcionDto;
    }
    
    public String getNominaComprobanteDescripcionByIdHTMLComboFechaInicial(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            NominaComprobanteDescripcion[] nominaComprobanteDescripcionDto = findNominaComprobanteDescripcion(-1, 0, 0, " GROUP BY FECHA_INICIAL_PAGO ");
            
            for (NominaComprobanteDescripcion nominaComprobanteDescripcion:nominaComprobanteDescripcionDto){
                try{
                    //NominaComprobanteDescripcion datosNominaComprobanteDescripcion = new NominaComprobanteDescripcionDaoImpl(this.conn).findByPrimaryKey(nominaComprobanteDescripcion.getIdNominaComprobanteDescripcion());
                    String selectedStr="";

                    if (idSeleccionado==nominaComprobanteDescripcion.getIdNominaComprobanteDescripcion())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+nominaComprobanteDescripcion.getFechaInicialPago()+"' "
                            + selectedStr
                            + "title='"+nominaComprobanteDescripcion.getFechaInicialPago()+"'>"
                            + nominaComprobanteDescripcion.getFechaInicialPago()
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
    
    public String getNominaComprobanteDescripcionByIdHTMLComboFechaFinal(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            NominaComprobanteDescripcion[] nominaComprobanteDescripcionDto = findNominaComprobanteDescripcion(-1, 0, 0, " GROUP BY FECHA_FIN_PAGO ");
            
            for (NominaComprobanteDescripcion nominaComprobanteDescripcion:nominaComprobanteDescripcionDto){
                try{
                    //NominaComprobanteDescripcion datosNominaComprobanteDescripcion = new NominaComprobanteDescripcionDaoImpl(this.conn).findByPrimaryKey(nominaComprobanteDescripcion.getIdNominaComprobanteDescripcion());
                    String selectedStr="";

                    if (idSeleccionado==nominaComprobanteDescripcion.getIdNominaComprobanteDescripcion())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+nominaComprobanteDescripcion.getFechaFinPago()+"' "
                            + selectedStr
                            + "title='"+nominaComprobanteDescripcion.getFechaFinPago()+"'>"
                            + nominaComprobanteDescripcion.getFechaFinPago()
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
    
    public String getNominaComprobanteDescripcionByIdHTMLComboFechaInicialFinal(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            NominaComprobanteDescripcion[] nominaComprobanteDescripcionDto = findNominaComprobanteDescripcion(-1, 0, 0, " GROUP BY FECHA_INICIAL_PAGO, FECHA_FIN_PAGO ");
            
            for (NominaComprobanteDescripcion nominaComprobanteDescripcion:nominaComprobanteDescripcionDto){
                try{
                    //NominaComprobanteDescripcion datosNominaComprobanteDescripcion = new NominaComprobanteDescripcionDaoImpl(this.conn).findByPrimaryKey(nominaComprobanteDescripcion.getIdNominaComprobanteDescripcion());
                    String selectedStr="";

                    if (idSeleccionado==nominaComprobanteDescripcion.getIdNominaComprobanteDescripcion())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+nominaComprobanteDescripcion.getFechaInicialPago()+" al "+nominaComprobanteDescripcion.getFechaFinPago()+ "'"
                            + selectedStr
                            + "title='"+nominaComprobanteDescripcion.getFechaInicialPago()+" al "+nominaComprobanteDescripcion.getFechaFinPago()+ "'>"
                            + nominaComprobanteDescripcion.getFechaInicialPago() + " al " + nominaComprobanteDescripcion.getFechaFinPago()
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
