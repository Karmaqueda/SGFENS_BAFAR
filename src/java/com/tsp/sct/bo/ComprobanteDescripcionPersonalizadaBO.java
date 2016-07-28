/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.ComprobanteDescripcionPersonalizada;
import com.tsp.sct.dao.jdbc.ComprobanteDescripcionPersonalizadaDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 19-dic-2012 
 */
public class ComprobanteDescripcionPersonalizadaBO {
    private ComprobanteDescripcionPersonalizada comprobanteDescripcionPersonalizada = null;

    public ComprobanteDescripcionPersonalizada getComprobanteDescripcionPersonalizada() {
        return comprobanteDescripcionPersonalizada;
    }

    public void setComprobanteDescripcionPersonalizada(ComprobanteDescripcionPersonalizada comprobanteDescripcionPersonalizada) {
        this.comprobanteDescripcionPersonalizada = comprobanteDescripcionPersonalizada;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public ComprobanteDescripcionPersonalizadaBO(Connection conn){
        this.conn = conn;
    }
    
    public ComprobanteDescripcionPersonalizadaBO(int idComprobanteDescripcionPersonalizada, Connection conn){
        this.conn = conn;
        try{
            ComprobanteDescripcionPersonalizadaDaoImpl ComprobanteDescripcionPersonalizadaDaoImpl = new ComprobanteDescripcionPersonalizadaDaoImpl(this.conn);
            this.comprobanteDescripcionPersonalizada = ComprobanteDescripcionPersonalizadaDaoImpl.findByPrimaryKey(idComprobanteDescripcionPersonalizada);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public ComprobanteDescripcionPersonalizada findComprobanteDescripcionPersonalizadabyId(int idComprobanteDescripcionPersonalizada) throws Exception{
        ComprobanteDescripcionPersonalizada ComprobanteDescripcionPersonalizada = null;
        
        try{
            ComprobanteDescripcionPersonalizadaDaoImpl ComprobanteDescripcionPersonalizadaDaoImpl = new ComprobanteDescripcionPersonalizadaDaoImpl(this.conn);
            ComprobanteDescripcionPersonalizada = ComprobanteDescripcionPersonalizadaDaoImpl.findByPrimaryKey(idComprobanteDescripcionPersonalizada);
            if (ComprobanteDescripcionPersonalizada==null){
                throw new Exception("No se encontro ninguna ComprobanteDescripcionPersonalizada que corresponda con los parámetros específicados.");
            }
            if (ComprobanteDescripcionPersonalizada.getIdComprobanteDescripcionPersonalizada()<=0){
                throw new Exception("No se encontro ninguna ComprobanteDescripcionPersonalizada que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la ComprobanteDescripcionPersonalizada del usuario. Error: " + e.getMessage());
        }
        
        return ComprobanteDescripcionPersonalizada;
    }
    
    /**
     * Realiza una búsqueda por ID ComprobanteDescripcionPersonalizada en busca de
     * coincidencias
     * @param idComprobanteDescripcionPersonalizada ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idComprobanteFiscal ID del Comprobante Fiscal a filtrar comprobanteDescripcionPersonalizadas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO ComprobanteDescripcionPersonalizada
     */
    public ComprobanteDescripcionPersonalizada[] findComprobanteDescripcionPersonalizadas(long idComprobanteDescripcionPersonalizada, long idComprobanteFiscal, int minLimit,int maxLimit, String filtroBusqueda) {
        ComprobanteDescripcionPersonalizada[] comprobanteDescripcionPersonalizadaDto = new ComprobanteDescripcionPersonalizada[0];
        ComprobanteDescripcionPersonalizadaDaoImpl comprobanteDescripcionPersonalizadaDao = new ComprobanteDescripcionPersonalizadaDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idComprobanteDescripcionPersonalizada>0){
                sqlFiltro ="ID_COMPROBANTE_DESCRIPCION_PERSONALIZADA=" + idComprobanteDescripcionPersonalizada + " AND ";
            }else{
                sqlFiltro ="ID_COMPROBANTE_DESCRIPCION_PERSONALIZADA>0 AND ";
            }
            if (idComprobanteFiscal>0){                
                sqlFiltro += " ID_COMPROBANTE_FISCAL = " + idComprobanteFiscal;
            }else{
                sqlFiltro +=" ID_COMPROBANTE_FISCAL>0 ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            comprobanteDescripcionPersonalizadaDto = comprobanteDescripcionPersonalizadaDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_COMPROBANTE_DESCRIPCION_PERSONALIZADA "
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return comprobanteDescripcionPersonalizadaDto;
    }
    
    public static class CampoPersonalizado {
        
        private String variable;
        private String valor = null;
        private boolean texto = false;
        private boolean decimal = false;
        private boolean fecha = false;
        private boolean textoLargo = false;
        private String descripcion;

        public CampoPersonalizado() {
        }

        public String getVariable() {
            return variable;
        }

        public void setVariable(String variable) {
            this.variable = variable;
        }

        public String getValor() {
            return valor;
        }

        public void setValor(String valor) {
            this.valor = valor;
        }

        public boolean isTexto() {
            return texto;
        }

        public void setTexto(boolean texto) {
            this.texto = texto;
        }

        public boolean isDecimal() {
            return decimal;
        }

        public void setDecimal(boolean decimal) {
            this.decimal = decimal;
        }

        public boolean isFecha() {
            return fecha;
        }

        public void setFecha(boolean fecha) {
            this.fecha = fecha;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public boolean isTextoLargo() {
            return textoLargo;
        }

        public void setTextoLargo(boolean textoLargo) {
            this.textoLargo = textoLargo;
        }
        
    }
    
    
}
