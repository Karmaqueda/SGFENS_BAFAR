/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CxpComprobanteFiscal;
import com.tsp.sct.dao.jdbc.CxpComprobanteFiscalDaoImpl;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author ISCesarMartinez
 */
public class CxpComprobanteFiscalBO {
    private CxpComprobanteFiscal cxpComprobanteFiscal  = null;

    public CxpComprobanteFiscal getCxpComprobanteFiscal() {
        return cxpComprobanteFiscal;
    }

    public void setCxpComprobanteFiscal(CxpComprobanteFiscal cxpComprobanteFiscal) {
        this.cxpComprobanteFiscal = cxpComprobanteFiscal;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CxpComprobanteFiscalBO(Connection conn){
        this.conn = conn;
    }
    
    public CxpComprobanteFiscalBO(int idCxpComprobanteFiscal, Connection conn){        
        this.conn = conn;
        try{
            CxpComprobanteFiscalDaoImpl CxpComprobanteFiscalDaoImpl = new CxpComprobanteFiscalDaoImpl(this.conn);
            this.cxpComprobanteFiscal = CxpComprobanteFiscalDaoImpl.findByPrimaryKey(idCxpComprobanteFiscal);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public CxpComprobanteFiscal findCxcbyId(int idCxc) throws Exception{
        CxpComprobanteFiscal cxpComprobanteFiscal = null;
        
        try{
            CxpComprobanteFiscalDaoImpl cxcDaoImpl = new CxpComprobanteFiscalDaoImpl(this.conn);
            cxpComprobanteFiscal = cxcDaoImpl.findByPrimaryKey(idCxc);
            if (cxpComprobanteFiscal==null){
                throw new Exception("No se encontro ningun cxpComprobanteFiscal que corresponda según los parámetros específicados.");
            }
            if (cxpComprobanteFiscal.getIdCxpComprobanteFiscal()<=0){
                throw new Exception("No se encontro ningun cxpComprobanteFiscal que corresponda según los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de CxpComprobanteFiscal del usuario. Error: " + e.getMessage());
        }
        
        return cxpComprobanteFiscal;
    }
    
    
    /**
     * Realiza una búsqueda por ID CxpComprobanteFiscal en busca de
     * coincidencias
     * @param idCxpComprobanteFiscal ID Del CxpComprobanteFiscal para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar cxpComprobanteFiscal, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CxpComprobanteFiscal
     */
    public CxpComprobanteFiscal[] findCxp(int idCxpComprobanteFiscal, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CxpComprobanteFiscal[] cxcDto = new CxpComprobanteFiscal[0];
        CxpComprobanteFiscalDaoImpl cxcDao = new CxpComprobanteFiscalDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCxpComprobanteFiscal>0){
                sqlFiltro ="ID_CXP_COMPROBANTE_FISCAL=" + idCxpComprobanteFiscal + " AND ";
            }else{
                sqlFiltro ="ID_CXP_COMPROBANTE_FISCAL>0 AND";
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
            
            cxcDto = cxcDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_CXP_COMPROBANTE_FISCAL DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return cxcDto;
    }
    
    /**
     * Calcula los dias transcurridos desde la captura hasta Hoy
     * dias transcurridos =  Fecha Hoy - Fecha Captura
     * @return 
     */
    public long calculaDiasTranscurridosCredito(){
        long diasTranscurridosCreditos = 0;
        
        Date fechaHoy=new Date();
        Date fechaCaptura = this.cxpComprobanteFiscal.getFechaHoraSello();
        
        try{
            // Crear 2 instancias de Calendar
            Calendar calFechaHoy = Calendar.getInstance();
            Calendar calFechaCaptura = Calendar.getInstance();

            // Establecer las fechas
            calFechaHoy.setTime(fechaHoy);
            calFechaCaptura.setTime(fechaCaptura);

            // conseguir la representacion de la fecha en milisegundos
            long milisFechaHoy = calFechaHoy.getTimeInMillis();
            long milisFechaCaptura = calFechaCaptura.getTimeInMillis();
            
            // calcular la diferencia en milisengundos
            long diff = milisFechaHoy - milisFechaCaptura;

            // calcular la diferencia en dias
            diasTranscurridosCreditos = diff / (24 * 60 * 60 * 1000);
            
            if (diasTranscurridosCreditos<0)
                diasTranscurridosCreditos=0;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return diasTranscurridosCreditos;
    }
    
    /**
     * Calcula el porcentaje de tiempo transcurrido (dias)
     * de un crédito otorgado. 
     * Si ya fue pagado, retorna -1
     * Retornado en base 100 ( 0 - 100 %)
     * @return 
     */
    public double calculaPorcentajeTranscurridoCredito(){
        double porcentajeTranscurrido = 0;
        
        try{
            double adeudo = this.cxpComprobanteFiscal.getTotal() - this.cxpComprobanteFiscal.getImportePagado();
            
            if (adeudo<=0){
                //Si el adeudo es de 0, es decir, si ya se cubrio, el porcentaje transcurrido no importaria
                porcentajeTranscurrido = -1;
                return porcentajeTranscurrido;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        long diasCredito = calculaDiasCredito();
        long diasTranscurridos = calculaDiasTranscurridosCredito();
        
        if (diasCredito>0){
            porcentajeTranscurrido = (diasTranscurridos * 100) / diasCredito;
        }else{
            porcentajeTranscurrido = 100;
        }
        
        if (porcentajeTranscurrido>100)
            porcentajeTranscurrido = 100;
        
        return porcentajeTranscurrido;
    }
    
     /**
      * Calcula los días de crédito otorgados
      *  dias de crédito = Fecha de pago - Fecha Captura
      * @return 
      */
    public long calculaDiasCredito(){
        long diasCredito =0;
        
        Date fechaPago = this.cxpComprobanteFiscal.getFechaTentativaPago();
        Date fechaCaptura = this.cxpComprobanteFiscal.getFechaHoraSello();
        
        try{
            // Crear 2 instancias de Calendar
            Calendar calFechaPago = Calendar.getInstance();
            Calendar calFechaCaptura = Calendar.getInstance();

            // Establecer las fechas
            calFechaPago.setTime(fechaPago);
            calFechaCaptura.setTime(fechaCaptura);

            // conseguir la representacion de la fecha en milisegundos
            long milisFechaPago = calFechaPago.getTimeInMillis();
            long milisFechaCaptura = calFechaCaptura.getTimeInMillis();
            
            // calcular la diferencia en milisengundos
            long diff = milisFechaPago - milisFechaCaptura;

            // calcular la diferencia en dias
            diasCredito = diff / (24 * 60 * 60 * 1000);
            
            if (diasCredito<0)
                diasCredito=0;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return diasCredito;
    }
    
    /**
      * Calcula los días de mora (atraso) a la fecha actual
      *  dias de mora = Fecha actual - Fecha de Pago
      * @return 
      */
    public long calculaDiasMora(){
        long diasMora=0;
        
        Date fechaActual = new Date();
        Date fechaPago=this.cxpComprobanteFiscal.getFechaTentativaPago();
        
        try{
            double adeudo = this.cxpComprobanteFiscal.getTotal() - this.cxpComprobanteFiscal.getImportePagado();
            
            if (adeudo<=0){
                //Si el adeudo es de 0, es decir, si ya se cubrio, no existen dias de atraso
                diasMora=0;
                return diasMora;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        try{
            // Crear 2 instancias de Calendar
            Calendar calFechaPago = Calendar.getInstance();
            Calendar calFechaActual = Calendar.getInstance();

            // Establecer las fechas
            calFechaPago.setTime(fechaPago);
            calFechaActual.setTime(fechaActual);

            // conseguir la representacion de la fecha en milisegundos
            long milisFechaPago = calFechaPago.getTimeInMillis();
            long milisFechaActual = calFechaActual.getTimeInMillis();
            
            // calcular la diferencia en milisengundos
            long diff = milisFechaActual - milisFechaPago;

            // calcular la diferencia en dias
            diasMora = diff / (24 * 60 * 60 * 1000);
            
            if (diasMora<0)
                diasMora=0;
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return diasMora;
    }
    
    /**
     * Actualiza el Saldo Pagado del Comprobante, igual al Importe Total
     * Es decir, marca como Pagado el comprobante
     * @throws Exception En caso de no existir el comprobante
     */
    public void marcarComprobantePagadoTotal() throws Exception{
        if (this.cxpComprobanteFiscal!=null){
            this.cxpComprobanteFiscal.setImportePagado(this.cxpComprobanteFiscal.getTotal());
            
            CxpComprobanteFiscalDaoImpl cxpDaoImpl = new CxpComprobanteFiscalDaoImpl(this.conn);
            cxpDaoImpl.update(this.cxpComprobanteFiscal.createPk(), this.cxpComprobanteFiscal);
        }else{
            throw new Exception("No se específico a que CxP Comprobante Fiscal se aplicará la acción.");
        }
    }
    
}
