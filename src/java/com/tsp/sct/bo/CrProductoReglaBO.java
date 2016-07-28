/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CrProductoCredito;
import com.tsp.sct.dao.dto.CrProductoRegla;
import com.tsp.sct.dao.jdbc.CrProductoReglaDaoImpl;
import com.tsp.sct.util.StringManage;
import com.tsp.sgfens.sesion.CrProductoCreditoSesion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author ISCesar
 */
public class CrProductoReglaBO {
    private CrProductoRegla crProductoRegla = null;
    private String orderBy = null;
    
    public static final String AP_MONTO_MAYOR_IGUAL = "AP_MONTO_MAYOR_IGUAL";
    public static final String AP_MONTO_MENOR_IGUAL = "AP_MONTO_MENOR_IGUAL";
    public static final String AP_PLAZO_MAYOR_IGUAL = "AP_PLAZO_MAYOR_IGUAL";

    public static final String RE_EDAD_MAYOR_IGUAL = "RE_EDAD_MAYOR_IGUAL";
    public static final String RE_EDAD_MENOR_IGUAL = "RE_EDAD_MENOR_IGUAL";
    public static final String RE_ANTIGUEDAD_DOMICILIO_MENOR_IGUAL = "RE_ANTIGUEDAD_DOMICILIO_MENOR_IGUAL";
    public static final String RE_ANTIGUEDAD_EMPLEO_MENOR_IGUAL = "RE_ANTIGUEDAD_EMPLEO_MENOR_IGUAL";
    public static final String RE_INGRESOS_MENOR_IGUAL = "RE_INGRESOS_MENOR_IGUAL";
    
    public static final String[] reglas = {AP_MONTO_MAYOR_IGUAL, AP_MONTO_MENOR_IGUAL, AP_PLAZO_MAYOR_IGUAL,
                                        RE_EDAD_MAYOR_IGUAL, RE_EDAD_MENOR_IGUAL, RE_ANTIGUEDAD_DOMICILIO_MENOR_IGUAL,
                                        RE_ANTIGUEDAD_EMPLEO_MENOR_IGUAL, RE_INGRESOS_MENOR_IGUAL};

    public CrProductoRegla getCrProductoRegla() {
        return crProductoRegla;
    }

    public void setCrProductoRegla(CrProductoRegla crProductoRegla) {
        this.crProductoRegla = crProductoRegla;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CrProductoReglaBO(Connection conn){
        this.conn = conn;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    
    public CrProductoReglaBO(int idCrProductoRegla, Connection conn){        
        this.conn = conn; 
        try{
           CrProductoReglaDaoImpl CrProductoReglaDaoImpl = new CrProductoReglaDaoImpl(this.conn);
            this.crProductoRegla = CrProductoReglaDaoImpl.findByPrimaryKey(idCrProductoRegla);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public CrProductoRegla findCrProductoReglabyId(int idCrProductoRegla) throws Exception{
        CrProductoRegla CrProductoRegla = null;
        
        try{
            CrProductoReglaDaoImpl CrProductoReglaDaoImpl = new CrProductoReglaDaoImpl(this.conn);
            CrProductoRegla = CrProductoReglaDaoImpl.findByPrimaryKey(idCrProductoRegla);
            if (CrProductoRegla==null){
                throw new Exception("No se encontro ningun CrProductoRegla que corresponda con los parámetros específicados.");
            }
            if (CrProductoRegla.getIdProductoRegla()<=0){
                throw new Exception("No se encontro ningun CrProductoRegla que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CrProductoRegla del usuario. Error: " + e.getMessage());
        }
        
        return CrProductoRegla;
    }
    
    /**
     * Realiza una búsqueda por ID CrProductoRegla en busca de
     * coincidencias
     * @param idCrProductoRegla ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CrProductoRegla
     */
    public CrProductoRegla[] findCrProductoReglas(int idCrProductoRegla, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CrProductoRegla[] crProductoReglaDto = new CrProductoRegla[0];
        CrProductoReglaDaoImpl crProductoReglaDao = new CrProductoReglaDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCrProductoRegla>0){
                sqlFiltro ="id_producto_regla=" + idCrProductoRegla + " AND ";
            }else{
                sqlFiltro ="id_producto_regla>0 AND";
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
            
            crProductoReglaDto = crProductoReglaDao.findByDynamicWhere( 
                    sqlFiltro
                    + " " + (StringManage.getValidString(orderBy).length()>0? orderBy : "ORDER BY id_producto_regla desc")
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return crProductoReglaDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID CrProductoRegla y otros filtros
     * @param idCrProductoRegla ID Del CrProductoRegla para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return int cantidad de registros coincidentes
     */
    public int findCantidadCrProductoReglas(int idCrProductoRegla, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        CrProductoReglaDaoImpl crProductoReglaDao = new CrProductoReglaDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrProductoRegla>0){
                sqlFiltro ="id_producto_regla=" + idCrProductoRegla + " AND ";
            }else{
                sqlFiltro ="id_producto_regla>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_producto_regla) as cantidad FROM " + crProductoReglaDao.getTableName() +  " WHERE " + 
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
    
    public double getValorEspecificoRegla(int idProductoCredito, String claveTipoRegla){
        double valor = 0;
        if (idProductoCredito<=0)
            return valor;
        
        CrProductoRegla[] productoReglas = findCrProductoReglas(-1, -1, 0, 0, " AND id_producto_credito=" + idProductoCredito + " AND clave_tipo_regla='" + claveTipoRegla +"'");
        if (productoReglas.length>0){
            switch (productoReglas[0].getClaveTipoRegla()){
                    case AP_MONTO_MAYOR_IGUAL:
                    case AP_PLAZO_MAYOR_IGUAL:
                    case RE_EDAD_MAYOR_IGUAL:
                        valor = productoReglas[0].getRangoMin();
                        break;
                    case AP_MONTO_MENOR_IGUAL:
                    case RE_EDAD_MENOR_IGUAL:
                    case RE_ANTIGUEDAD_DOMICILIO_MENOR_IGUAL:
                    case RE_ANTIGUEDAD_EMPLEO_MENOR_IGUAL:
                    case RE_INGRESOS_MENOR_IGUAL:
                        valor = productoReglas[0].getRangoMax();
                        break;
            }
        }
        return valor;
    }
    
    public String getValorEspecificoReglaStr(int idProductoCredito, String claveTipoRegla){
        String valor = "";
        if (idProductoCredito<=0)
            return valor;
        
        double valorD = 0;
        CrProductoRegla[] productoReglas = findCrProductoReglas(-1, -1, 0, 0, " AND id_producto_credito=" + idProductoCredito + " AND clave_tipo_regla='" + claveTipoRegla +"'");
        if (productoReglas.length>0){
            switch (productoReglas[0].getClaveTipoRegla()){
                    case AP_MONTO_MAYOR_IGUAL:
                    case AP_PLAZO_MAYOR_IGUAL:
                    case RE_EDAD_MAYOR_IGUAL:
                        valorD = productoReglas[0].getRangoMin();
                        break;
                    case AP_MONTO_MENOR_IGUAL:
                    case RE_EDAD_MENOR_IGUAL:
                    case RE_ANTIGUEDAD_DOMICILIO_MENOR_IGUAL:
                    case RE_ANTIGUEDAD_EMPLEO_MENOR_IGUAL:
                    case RE_INGRESOS_MENOR_IGUAL:
                        valorD = productoReglas[0].getRangoMax();
                        break;
            }
        }
        if (valorD == Math.floor(valorD)) {
            // valor entero
            valor = "" + new Double(valorD).intValue();
        }else{
            //decimal
            valor = "" + valorD;
        }
        
        return valor;
    }
    
    public static AuxReglaGenerica getValorReglaGenerica(String claveTipoRegla){
        switch (claveTipoRegla){
            case AP_MONTO_MAYOR_IGUAL:
                return new AuxReglaGenerica(AP_MONTO_MAYOR_IGUAL, "Monto mayor o igual a. >=", ReglaTipo.APLICACION_SCORE, ValorTipo.RANGO_MIN);
            case AP_PLAZO_MAYOR_IGUAL:
                return new AuxReglaGenerica(AP_PLAZO_MAYOR_IGUAL, "Monto menor o igual a. <=", ReglaTipo.APLICACION_SCORE, ValorTipo.RANGO_MIN);
            case AP_MONTO_MENOR_IGUAL:
                return new AuxReglaGenerica(AP_MONTO_MENOR_IGUAL, "Periodo Pago mayor o igual a. >=", ReglaTipo.APLICACION_SCORE, ValorTipo.RANGO_MAX);
            case RE_EDAD_MAYOR_IGUAL:
                return new AuxReglaGenerica(RE_EDAD_MAYOR_IGUAL, "Edad mayor o igual a. >=", ReglaTipo.RECHAZO, ValorTipo.RANGO_MIN);
            case RE_EDAD_MENOR_IGUAL:
                return new AuxReglaGenerica(RE_EDAD_MENOR_IGUAL, "Edad menor o igual a. <=", ReglaTipo.RECHAZO, ValorTipo.RANGO_MAX);
            case RE_ANTIGUEDAD_DOMICILIO_MENOR_IGUAL:
                return new AuxReglaGenerica(RE_ANTIGUEDAD_DOMICILIO_MENOR_IGUAL, "Antiguedad en Domicilio menor o igual a. <=", ReglaTipo.RECHAZO, ValorTipo.RANGO_MAX);
            case RE_ANTIGUEDAD_EMPLEO_MENOR_IGUAL:
                return new AuxReglaGenerica(RE_ANTIGUEDAD_EMPLEO_MENOR_IGUAL, "Antiguedad en Empleo menor o igual a. <=", ReglaTipo.RECHAZO, ValorTipo.RANGO_MAX);
            case RE_INGRESOS_MENOR_IGUAL:
                return new AuxReglaGenerica(RE_INGRESOS_MENOR_IGUAL, "Ingresos menor o igual a. <=", ReglaTipo.RECHAZO, ValorTipo.RANGO_MAX);
            default:
                return null;
        }
    }
    
    public static class AuxReglaGenerica{
        private String clave;
        private String etiqueta;
        private ReglaTipo reglaTipo;
        private ValorTipo valorTipo;

        public AuxReglaGenerica(String clave, String etiqueta, ReglaTipo reglaTipo, ValorTipo valorTipo) {
            this.clave = clave;
            this.etiqueta = etiqueta;
            this.reglaTipo = reglaTipo;
            this.valorTipo = valorTipo;
        }
        
        public String getClave() {
            return clave;
        }

        public void setClave(String clave) {
            this.clave = clave;
        }

        public String getEtiqueta() {
            return etiqueta;
        }

        public void setEtiqueta(String etiqueta) {
            this.etiqueta = etiqueta;
        }

        public ReglaTipo getReglaTipo() {
            return reglaTipo;
        }

        public void setReglaTipo(ReglaTipo reglaTipo) {
            this.reglaTipo = reglaTipo;
        }

        public ValorTipo getValorTipo() {
            return valorTipo;
        }

        public void setValorTipo(ValorTipo valorTipo) {
            this.valorTipo = valorTipo;
        }
        
    }
    
    public static enum ReglaTipo {
        APLICACION_SCORE,
        RECHAZO
    }
    
    public static enum ValorTipo {
        RANGO_MIN,
        RANGO_MAX,
        VALOR_EXACTO
    }
        
}
