/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.NominaEmpleado;
import com.tsp.sct.dao.jdbc.NominaEmpleadoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class NominaEmpleadoBO {
    
    private Connection conn = null;
    
    private NominaEmpleado nominaEmpleado  = null;

    public NominaEmpleado getNominaEmpleado() {
        return nominaEmpleado;
    }

    public void setNominaEmpleado(NominaEmpleado nominaEmpleado) {
        this.nominaEmpleado = nominaEmpleado;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    
    
    public NominaEmpleadoBO(Connection conn){
        this.conn = conn;
    }
    
    public NominaEmpleadoBO(Connection conn, int idNominaEmpleado){        
        this.conn = conn;
        try{
            NominaEmpleadoDaoImpl NominaEmpleadoDaoImpl = new NominaEmpleadoDaoImpl(this.conn);
            this.nominaEmpleado = NominaEmpleadoDaoImpl.findByPrimaryKey(idNominaEmpleado);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public NominaEmpleado findNominaEmpleadobyId(int idNominaEmpleado) throws Exception{
        NominaEmpleado NominaEmpleado = null;
        
        try{
            NominaEmpleadoDaoImpl NominaEmpleadoDaoImpl = new NominaEmpleadoDaoImpl(this.conn);
            NominaEmpleado = NominaEmpleadoDaoImpl.findByPrimaryKey(idNominaEmpleado);
            if (NominaEmpleado==null){
                throw new Exception("No se encontro ningun Empleado que corresponda con los parámetros específicados.");
            }
            if (NominaEmpleado.getIdEmpleado()<=0){
                throw new Exception("No se encontro ningun Empleado que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Empleado del usuario. Error: " + e.getMessage());
        }
        
        return NominaEmpleado;
    }   
  
    
    /**
     * Realiza una búsqueda por ID NominaEmpleado en busca de
     * coincidencias
     * @param idNominaEmpleado ID Del NominaEmpleado para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar nominaEmpleadoDto, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO NominaEmpleado
     */
    public NominaEmpleado[] findNominaEmpleados(int idNominaEmpleado, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        NominaEmpleado[] nominaEmpleadoDto = new NominaEmpleado[0];
        NominaEmpleadoDaoImpl nominaEmpleadoDao = new NominaEmpleadoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idNominaEmpleado>0){
                sqlFiltro ="ID_EMPLEADO=" + idNominaEmpleado + " AND ";
            }else{
                sqlFiltro ="ID_EMPLEADO>0 AND";
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
            
            nominaEmpleadoDto = nominaEmpleadoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NUM_EMPLEADO ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return nominaEmpleadoDto;
    }
    
    public String getNominaEmpleadosByIdHTMLCombo(int idEmpresa, int idSeleccionado, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            NominaEmpleado[] nominaEmpleadosDto = findNominaEmpleados(-1, idEmpresa, 0, 0, filtroBusqueda);
            
            for (NominaEmpleado itemNominaEmpleado:nominaEmpleadosDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado==itemNominaEmpleado.getIdEmpleado())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+itemNominaEmpleado.getIdEmpleado()+"' "
                            + selectedStr
                            + "title='"+itemNominaEmpleado.getNumEmpleado()+"'>"
                            + itemNominaEmpleado.getNombre() +" " +itemNominaEmpleado.getApellidoPaterno() +" "+itemNominaEmpleado.getApellidoMaterno()
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
