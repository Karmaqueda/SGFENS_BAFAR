/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CuentaEfectivo;
import com.tsp.sct.dao.jdbc.CuentaEfectivoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author HpPyme
 */
public class CuentaEfectivoBO {
    
     private Connection conn = null;
     private CuentaEfectivo cuentaEfectivo = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public CuentaEfectivo getCuentaEfectivo() {
        return cuentaEfectivo;
    }

    public void setCuentaEfectivo(CuentaEfectivo cuentaEfectivo) {
        this.cuentaEfectivo = cuentaEfectivo;
    }

    public CuentaEfectivoBO(Connection conn) {
        this.conn = conn;
    }
    
    public CuentaEfectivoBO(int idCuentaEfectivo, Connection conn){ 
        this.conn = conn;
        try{
            CuentaEfectivoDaoImpl cuentaEfectivoDaoImpl = new CuentaEfectivoDaoImpl(this.conn);
            this.cuentaEfectivo = cuentaEfectivoDaoImpl.findByPrimaryKey(idCuentaEfectivo);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    /**
     * Realiza una búsqueda por ID Usuario en busca de
     * coincidencias
     * @param idUsuario ID Del Usuario para filtrar, -1 para mostrar todos los registros     
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CuentaEfectivo
     */
    public CuentaEfectivo[] findCuentaEfectivo(int idEmpleado, int minLimit,int maxLimit, String filtroBusqueda) {
        CuentaEfectivo[] cuentaEfectivoDto = new CuentaEfectivo[0];
        CuentaEfectivoDaoImpl cuentaEfectivoDao = new CuentaEfectivoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idEmpleado>0){
                sqlFiltro =" ID_EMPLEADO=" + idEmpleado + " ";
            }else{
                sqlFiltro =" ID_EMPLEADO > 0 ";
            }            
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            cuentaEfectivoDto = cuentaEfectivoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_CUENTA_EFECTIVO DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return cuentaEfectivoDto;
    }
    
    
    public double cuentaEfectivoTipo(int idCuentaEfectivo, String tipo){
        double montoTotal = 0;
        double montoBilletes = 0;
        double montoMonedas = 0;
         
        
        try{
           CuentaEfectivoDaoImpl cuentaDaoImpl = new CuentaEfectivoDaoImpl(this.conn);
            this.cuentaEfectivo = cuentaDaoImpl.findByPrimaryKey(idCuentaEfectivo);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        if(cuentaEfectivo!=null){
            montoBilletes = (cuentaEfectivo.getBillete1000() * 1000)
                        + (cuentaEfectivo.getBillete500() * 500)
                        + (cuentaEfectivo.getBillete200() * 200)
                        + (cuentaEfectivo.getBillete100() * 100)
                        + (cuentaEfectivo.getBillete50() * 50)
                        + (cuentaEfectivo.getBillete20()* 20);
            
            montoMonedas = (cuentaEfectivo.getMoneda20() * 20)
                        + (cuentaEfectivo.getMoneda10() * 10)
                        + (cuentaEfectivo.getMoneda5() * 5)
                        + (cuentaEfectivo.getMoneda2() * 2)
                        + (cuentaEfectivo.getMoneda1() * 1)
                        + (cuentaEfectivo.getMoneda050() * 0.50)
                        + (cuentaEfectivo.getMoneda020() * 0.20)
                        + (cuentaEfectivo.getMoneda010() * 0.10);
            
        

            if(tipo.equals("billetes")){
                return montoBilletes;
            }else if(tipo.equals("monedas")){
                return montoMonedas;
            }else if(tipo.equals("total")){
                return (montoBilletes + montoMonedas );
            }
        }
        
         return 0;
        
    }
     
     
}
