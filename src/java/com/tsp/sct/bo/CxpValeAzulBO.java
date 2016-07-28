/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.infosoft.fechas.FormateadorDeFechas;
import com.tsp.sct.dao.dto.CxpValeAzul;
import com.tsp.sct.dao.dto.Folios;
import com.tsp.sct.dao.jdbc.CxpValeAzulDaoImpl;
import com.tsp.sct.util.FacturacionUtil;
import com.tsp.sct.util.FormatUtil;
import com.tsp.sct.util.StringManage;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author ISCesarMartinez
 */
public class CxpValeAzulBO {
    private CxpValeAzul cxpValeAzul  = null;

    public CxpValeAzul getCxpValeAzul() {
        return cxpValeAzul;
    }

    public void setCxpValeAzul(CxpValeAzul cxpValeAzul) {
        this.cxpValeAzul = cxpValeAzul;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CxpValeAzulBO(Connection conn){
        this.conn = conn;
    }
    
    public CxpValeAzulBO(int idCxpValeAzul, Connection conn){        
        this.conn = conn;
        try{
            CxpValeAzulDaoImpl CxpValeAzulDaoImpl = new CxpValeAzulDaoImpl(this.conn);
            this.cxpValeAzul = CxpValeAzulDaoImpl.findByPrimaryKey(idCxpValeAzul);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public CxpValeAzul findCxpbyId(int idCxc) throws Exception{
        CxpValeAzul cxpValeAzul = null;
        
        try{
            CxpValeAzulDaoImpl cxcDaoImpl = new CxpValeAzulDaoImpl(this.conn);
            cxpValeAzul = cxcDaoImpl.findByPrimaryKey(idCxc);
            if (cxpValeAzul==null){
                throw new Exception("No se encontro ningun cxpValeAzul que corresponda según los parámetros específicados.");
            }
            if (cxpValeAzul.getIdCxpValeAzul()<=0){
                throw new Exception("No se encontro ningun cxpValeAzul que corresponda según los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de CxpValeAzul del usuario. Error: " + e.getMessage());
        }
        
        return cxpValeAzul;
    }
    
    
    /**
     * Realiza una búsqueda por ID CxpValeAzul en busca de
     * coincidencias
     * @param idCxpValeAzul ID Del CxpValeAzul para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar cxpValeAzul, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CxpValeAzul
     */
    public CxpValeAzul[] findCxp(int idCxpValeAzul, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CxpValeAzul[] cxcDto = new CxpValeAzul[0];
        CxpValeAzulDaoImpl cxcDao = new CxpValeAzulDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCxpValeAzul>0){
                sqlFiltro ="ID_CXP_VALE_AZUL=" + idCxpValeAzul + " AND ";
            }else{
                sqlFiltro ="ID_CXP_VALE_AZUL>0 AND";
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
                    + " ORDER BY ID_CXP_VALE_AZUL DESC"
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
        Date fechaCaptura = this.cxpValeAzul.getFechaHoraControl();
        
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
            double adeudo = this.cxpValeAzul.getImporte() - this.cxpValeAzul.getImportePagado();
            
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
        
        Date fechaPago = this.cxpValeAzul.getFechaTentativaPago();
        Date fechaCaptura = this.cxpValeAzul.getFechaHoraControl();
        
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
        Date fechaPago=this.cxpValeAzul.getFechaTentativaPago();
        
        try{
            double adeudo = this.cxpValeAzul.getImporte() - this.cxpValeAzul.getImportePagado();
            
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
     * Actualiza el Saldo Pagado del Vale Azul, igual al Importe Total
     * Es decir, marca como Pagado el comprobante
     * @throws Exception En caso de no existir el Vale Azul
     */
    public void marcarValeAzulPagadoTotal() throws Exception{
        if (this.cxpValeAzul!=null){
            this.cxpValeAzul.setImportePagado(this.cxpValeAzul.getImporte());
            
            CxpValeAzulDaoImpl cxpValeAzulDaoImpl = new CxpValeAzulDaoImpl(this.conn);
            cxpValeAzulDaoImpl.update(this.cxpValeAzul.createPk(), this.cxpValeAzul);
        }else{
            throw new Exception("No se específico a que CxP Vale Azul se aplicará la acción.");
        }
    }
    
    public ByteArrayOutputStream toPdf() throws Exception{
        return toPdf(this.cxpValeAzul);
    }

    private ByteArrayOutputStream toPdf(CxpValeAzul cxpValeAzul) throws Exception {
        ByteArrayOutputStream baos = null;
        
        Map<String, Object> parametros = new HashMap<String, Object>();
        try{
            //Recuperamos datos
            String serie = null;
            String importe = FormatUtil.doubleToStringPuntoComas(cxpValeAzul.getImporte());
            String importeLetra = FacturacionUtil.importeLetra(cxpValeAzul.getImporte(), null);
            String fechaControl = FormateadorDeFechas.getFecha(cxpValeAzul.getFechaHoraControl().getTime(),FormateadorDeFechas.FORMATO_DDMMYYYY);
            String fechaPago = FormateadorDeFechas.getFecha(cxpValeAzul.getFechaTentativaPago().getTime(),FormateadorDeFechas.FORMATO_DDMMYYYY);
            
            //Buscamos información de Serie
            if (cxpValeAzul.getIdFolio()>0){
                Folios foliosDto = new FoliosBO(cxpValeAzul.getIdFolio(), conn).getFolios();
                if (foliosDto!=null)
                    serie = StringManage.getValidString(foliosDto.getSerie());
            }
            
            //Asignamos parametros
            parametros.put("importe", importe);
            parametros.put("importe_letra", importeLetra);
            parametros.put("concepto", cxpValeAzul.getConcepto());
            parametros.put("fecha", fechaControl);
            parametros.put("fecha_pago", fechaPago);
            parametros.put("serie", serie);
            parametros.put("folio", cxpValeAzul.getFolioGenerado());
            
            //System.setProperty("jasper.reports.compile.class.path",context.getRealPath("/WEB-INF/lib/jasperreports-3.7.4.jar") + System.getProperty("path.separator") + context.getRealPath("/WEB-INF/"));
            //System.setProperty("jasper.reports.compile.class.path","/web/WEB-INF/lib/jasperreports-3.7.4.jar");
            
            JasperPrint print = JasperFillManager.fillReport( 
                    "C:/SystemaDeArchivos/plantilla_vale_azul.jasper",
                    parametros);
            
            baos = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(print, baos);
            
        }catch(Exception ex){
            ex.printStackTrace();
            throw new Exception("No se ha podido generar el archivo PDF. " + ex.toString());
        }
        
        return baos;
    }
    
}
