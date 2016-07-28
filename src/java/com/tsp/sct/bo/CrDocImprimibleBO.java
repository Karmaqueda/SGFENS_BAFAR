/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.google.gson.Gson;
import com.infosoft.fechas.FormateadorDeFechas;
import com.tsp.sct.config.Configuration;
import com.tsp.sct.cr.CrOrdenPago;
import com.tsp.sct.cr.CrTablaAmortizacion;
import com.tsp.sct.cr.CrUtilCalculos;
import com.tsp.sct.dao.dto.CrDocImpParametro;
import com.tsp.sct.dao.dto.CrDocImprimible;
import com.tsp.sct.dao.dto.CrFormulario;
import com.tsp.sct.dao.dto.CrFormularioCampo;
import com.tsp.sct.dao.dto.CrFormularioEvento;
import com.tsp.sct.dao.dto.CrFormularioRespuesta;
import com.tsp.sct.dao.dto.CrFrmEventoSolicitud;
import com.tsp.sct.dao.dto.CrProductoCredito;
import com.tsp.sct.dao.dto.CrProductoSeguro;
import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.dao.dto.Folios;
import com.tsp.sct.dao.jdbc.CrDocImprimibleDaoImpl;
import com.tsp.sct.dao.jdbc.CrFormularioRespuestaDaoImpl;
import com.tsp.sct.dao.jdbc.CrProductoCreditoDaoImpl;
import com.tsp.sct.dao.jdbc.CrProductoSeguroDaoImpl;
import com.tsp.sct.util.DateManage;
import com.tsp.sct.util.FacturacionUtil;
import com.tsp.sct.util.FormatUtil;
import com.tsp.sct.util.GenericMethods;
import com.tsp.sct.util.StringManage;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author ISCesar
 */
public class CrDocImprimibleBO {
    private CrDocImprimible crDocImprimible = null;
    private String orderBy = null;
    
    public final static String TIPO_SOLICITUD = "SOLICITUD";
    public final static String TIPO_CONTRATO = "CONTRATO";
    public final static String TIPO_TABLA_AMORTIZACION = "TABLA_AMORTIZACION";
    public final static String TIPO_AUT_BURO = "AUTORIZACION_BURO";
    public final static String TIPO_GARANTIA_PAGARE = "GARANTIA";
    public final static String TIPO_CODIGO_BARRAS = "CODIGO_BARRAS";
    public final static String TIPO_ORDEN_PAGO = "ORDEN_PAGO";
    public final static String TIPO_OTRO = "OTRO";
    public final static String[] listaTipoImprimible = new String[]{TIPO_SOLICITUD, TIPO_TABLA_AMORTIZACION, TIPO_CONTRATO, TIPO_AUT_BURO, TIPO_GARANTIA_PAGARE, TIPO_CODIGO_BARRAS, TIPO_ORDEN_PAGO, TIPO_OTRO};

    public CrDocImprimible getCrDocImprimible() {
        return crDocImprimible;
    }

    public void setCrDocImprimible(CrDocImprimible crDocImprimible) {
        this.crDocImprimible = crDocImprimible;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CrDocImprimibleBO(Connection conn){
        this.conn = conn;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    
    public CrDocImprimibleBO(int idCrDocImprimible, Connection conn){        
        this.conn = conn; 
        try{
           CrDocImprimibleDaoImpl CrDocImprimibleDaoImpl = new CrDocImprimibleDaoImpl(this.conn);
            this.crDocImprimible = CrDocImprimibleDaoImpl.findByPrimaryKey(idCrDocImprimible);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public CrDocImprimible findCrDocImprimiblebyId(int idCrDocImprimible) throws Exception{
        CrDocImprimible CrDocImprimible = null;
        
        try{
            CrDocImprimibleDaoImpl CrDocImprimibleDaoImpl = new CrDocImprimibleDaoImpl(this.conn);
            CrDocImprimible = CrDocImprimibleDaoImpl.findByPrimaryKey(idCrDocImprimible);
            if (CrDocImprimible==null){
                throw new Exception("No se encontro ningun CrDocImprimible que corresponda con los parámetros específicados.");
            }
            if (CrDocImprimible.getIdDocImprimible()<=0){
                throw new Exception("No se encontro ningun CrDocImprimible que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CrDocImprimible del usuario. Error: " + e.getMessage());
        }
        
        return CrDocImprimible;
    }
    
    /**
     * Realiza una búsqueda por ID CrDocImprimible en busca de
     * coincidencias
     * @param idCrDocImprimible ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CrDocImprimible
     */
    public CrDocImprimible[] findCrDocImprimibles(int idCrDocImprimible, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CrDocImprimible[] crDocImprimibleDto = new CrDocImprimible[0];
        CrDocImprimibleDaoImpl crDocImprimibleDao = new CrDocImprimibleDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrDocImprimible>0){
                sqlFiltro ="id_doc_imprimible=" + idCrDocImprimible + " AND ";
            }else{
                sqlFiltro ="id_doc_imprimible>0 AND";
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
            
            crDocImprimibleDto = crDocImprimibleDao.findByDynamicWhere( 
                    sqlFiltro
                    + " " + (StringManage.getValidString(orderBy).length()>0? orderBy : "ORDER BY id_doc_imprimible desc")
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return crDocImprimibleDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID CrDocImprimible y otros filtros
     * @param idCrDocImprimible ID Del CrDocImprimible para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return int cantidad de registros coincidentes
     */
    public int findCantidadCrDocImprimibles(int idCrDocImprimible, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        CrDocImprimibleDaoImpl crDocImprimibleDao = new CrDocImprimibleDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrDocImprimible>0){
                sqlFiltro ="id_doc_imprimible=" + idCrDocImprimible + " AND ";
            }else{
                sqlFiltro ="id_doc_imprimible>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_doc_imprimible) as cantidad FROM " + crDocImprimibleDao.getTableName() +  " WHERE " + 
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
        
    public String getCrDocImprimiblesByIdHTMLCombo(int idEmpresa, int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            CrDocImprimible[] crDocImprimibleesDto = findCrDocImprimibles(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (CrDocImprimible crDocImprimible : crDocImprimibleesDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado == crDocImprimible.getIdDocImprimible())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+crDocImprimible.getIdDocImprimible()+"' "
                            + selectedStr
                            + "title='"+crDocImprimible.getDescripcion()+"'>"
                            + crDocImprimible.getNombre()
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

    public ByteArrayOutputStream compilaPDF(int idCrFormularioEvento) throws Exception {
        return compilaPDF(this.crDocImprimible, idCrFormularioEvento);
    }
    
    public ByteArrayOutputStream compilaPDF(CrDocImprimible crDocImprimible, int idCrFormularioEvento) throws Exception {
        ByteArrayOutputStream baos = null;
        
        Map<String, Object> parametros = new HashMap<String, Object>();
        try{
            //Recuperamos datos
            int idEmpresa = crDocImprimible.getIdEmpresa();
            Empresa empresaDto = new EmpresaBO(this.conn).getEmpresaMatriz(idEmpresa);
            CrDocImpParametroBO crDocImpParametroBO = new CrDocImpParametroBO(this.conn);
            CrDocImpParametro[] docImpParametros = crDocImpParametroBO.findCrDocImpParametros(-1, idEmpresa, 0, 0, " AND id_doc_imprimible = " + crDocImprimible.getIdDocImprimible());
            
            CrFormularioEventoBO crFormularioEventoBO = new CrFormularioEventoBO(idCrFormularioEvento, this.conn);
            CrFormularioEvento crFormularioEvento = crFormularioEventoBO.getCrFormularioEvento();
            CrFrmEventoSolicitud  crFrmEventoSolicitud = crFormularioEventoBO.getFrmEventoSolicitud();
            CrFormularioCampoBO crFormularioCampoBO = new CrFormularioCampoBO(this.conn);
            CrFormularioRespuestaBO crFormularioRespuestaBO = new CrFormularioRespuestaBO(this.conn);
            
            //Asignamos parametros
            for (CrDocImpParametro dip :  docImpParametros){
                try{
                    String parametroNombre = dip.getParametroClave();
                    String parametroValor = "";
                    
                    CrFormularioCampo[] crFormularioCampos = crFormularioCampoBO.findCrFormularioCampos(-1, idEmpresa, 0, 1, " AND variable_formula = '" + dip.getAsociaVariableFormula() + "' AND id_formulario IN (SELECT id_formulario FROM cr_formulario WHERE id_grupo_formulario = "+crFormularioEvento.getIdGrupoFormulario()+")");
                    if (crFormularioCampos.length>0){
                        CrFormularioRespuesta frmRespuesta = crFormularioRespuestaBO.findCrFormularioRespuestas(-1, idEmpresa, 0, 0, " AND id_formulario_evento=" + idCrFormularioEvento + " AND id_formulario_campo=" + crFormularioCampos[0].getIdFormularioCampo())[0];
                        parametroValor = frmRespuesta.getValor();
                    }else{
                        System.out.println("Advertencia: Valor para parametro jasper '"+dip.getParametroClave()+"' no encontrado.");
                    }
                    
                    parametros.put(parametroNombre, parametroValor);
                }catch(Exception ex){
                    throw new Exception("Error al buscar valor para parametro jasper '"+dip.getParametroClave()+"'. " + GenericMethods.exceptionStackTraceToString(ex));
                }
            }
            
            //parametros compartidos (estos son los que se encuentran en la tabla cr_frm_evento_solicitud como atributos):
            parametros.put("SIN_DEFINICION_JASPER", crFrmEventoSolicitud.getMontoSolicitado());
            parametros.put("SIN_DEFINICION_JASPER", crFrmEventoSolicitud.getMontoAprobado());
            parametros.put("SIN_DEFINICION_JASPER", crFrmEventoSolicitud.getFechaSolicitado());
            parametros.put("SIN_DEFINICION_JASPER", crFrmEventoSolicitud.getFechaAprobado());
            parametros.put("SIN_DEFINICION_JASPER", crFrmEventoSolicitud.getPlazoMeses());
            parametros.put("SIN_DEFINICION_JASPER", crFrmEventoSolicitud.getPlazoVencimiento());
            parametros.put("SIN_DEFINICION_JASPER", crFrmEventoSolicitud.getTipoVencimiento());
            
            //mapeamos datos de la tabla cr_frm_evento_solicitud al jasper correspondiente en base al tipo:
            if(crDocImprimible.getTipoImprimible().equals(TIPO_SOLICITUD)){
                
            }else if(crDocImprimible.getTipoImprimible().equals(TIPO_CONTRATO)){
                
                //Recuperamos el producto:                
                CrProductoCredito crProductoCreditoDto = null;
                CrProductoCredito crProductoCreditoPadreDto = null;
                CrProductoCreditoDaoImpl crProductoCreditoDaoImpl = new CrProductoCreditoDaoImpl(this.conn);
                CrProductoSeguro crProductoSeguro = new CrProductoSeguro();
                try{
                    crProductoCreditoDto = crProductoCreditoDaoImpl.findByPrimaryKey(crFrmEventoSolicitud.getIdProductoCredito());
                }catch(Exception e){}
                if(crProductoCreditoDto != null){
                    crProductoCreditoPadreDto = crProductoCreditoDaoImpl.findByPrimaryKey(crProductoCreditoDto.getIdProductoCreditoPadre());                     
                }
                try{
                    crProductoSeguro = new CrProductoSeguroDaoImpl(this.conn).findWhereIdProductoCreditoEquals(crProductoCreditoDto.getIdProductoCredito())[0];
                }catch(Exception e){e.printStackTrace();}
                
                parametros.put("CRED_MONTO", crFrmEventoSolicitud.getMontoAprobado());
                parametros.put("CRED_NUMERO", crFrmEventoSolicitud.getSapNoContrato());
                
                parametros.put("CRED_TASA_ANUAL", crProductoCreditoDto.getTasaInteresAnual());
                parametros.put("CRED_CAT", crProductoCreditoDto.getCostoAnualTotal());
                parametros.put("CRED_TASA_ANUAL_MOR", crProductoCreditoDto.getTasaInteresMora());
                parametros.put("CRED_IMP_TOT", crFrmEventoSolicitud.getSapMontoTotalPagar());
                parametros.put("CRED_FECHA_PAGO", crFrmEventoSolicitud.getSapInfFechaPago());
                parametros.put("CRED_DESCRP_GAR", crProductoCreditoPadreDto.getGarantiasDescripcion());
                parametros.put("CRED_COMISIONES", crProductoCreditoDto.getGastosCobranza());
                parametros.put("CRED_TIPO_SEGURO", crProductoSeguro.getTipoSeguro());
                parametros.put("CRED_SEGURO_OBLIG", crProductoSeguro.getIsObligatorio()==0?"No":"Si");
                parametros.put("CRED_SEGURO", "De Vida");
                parametros.put("CRED_SEGURO_ASEGU", crProductoSeguro.getAseguradoraNombre());
                parametros.put("CRED_SEGURO_POL_NUM", "");
                parametros.put("CRED_FECHA_CORTE", crFrmEventoSolicitud.getSapInfFechaCorte());
                parametros.put("CRED_CONT_ADSN", "XXXXXXX-XXXX");
                parametros.put("CAR_AUTO_PLAZO", crProductoCreditoPadreDto.getPlazo());
                parametros.put("CRED_FECHA_CONTRATO", crFrmEventoSolicitud.getSapFechaApertura());
                parametros.put("CRED_FECHA_FINALIZACION", crFrmEventoSolicitud.getSapFechaFinCredito());
                
            }else if(crDocImprimible.getTipoImprimible().equals(TIPO_TABLA_AMORTIZACION)){
                
                //Recuperamos el producto:                
                CrProductoCredito crProductoCreditoDto = null;
                CrProductoCredito crProductoCreditoPadreDto = null;
                CrProductoCreditoDaoImpl crProductoCreditoDaoImpl = new CrProductoCreditoDaoImpl(this.conn);
                CrProductoSeguro crProductoSeguro = new CrProductoSeguro();
                try{
                    crProductoCreditoDto = crProductoCreditoDaoImpl.findByPrimaryKey(crFrmEventoSolicitud.getIdProductoCredito());
                }catch(Exception e){}
                if(crProductoCreditoDto != null){
                    crProductoCreditoPadreDto = crProductoCreditoDaoImpl.findByPrimaryKey(crProductoCreditoDto.getIdProductoCreditoPadre());                     
                }
                
                parametros.put("CRED_MONTO", crFrmEventoSolicitud.getMontoAprobado());
                parametros.put("CRED_NUMERO", crFrmEventoSolicitud.getSapNoContrato());
                parametros.put("CRED_TAZA_ANUAL", crProductoCreditoDto.getTasaInteresAnual());
                parametros.put("CRED_FECHA_OPERACION", crFrmEventoSolicitud.getFechaAprobado());
                parametros.put("CRED_FECHA_FINALIZACION", crFrmEventoSolicitud.getSapFechaFinCredito());
                parametros.put("CRED_PERIODICIDAD", crFrmEventoSolicitud.getTipoVencimiento());
                parametros.put("CRED_PRODUCTO", crProductoCreditoDto.getNombre());
                parametros.put("CRED_FECHA_ELABORACION", crFrmEventoSolicitud.getFechaAprobado());
                parametros.put("CRED_FECHA_CONTRATO", crFrmEventoSolicitud.getFechaAprobado());
                parametros.put("CRED_TIPO", "SIMPLE");
                parametros.put("CRED_MONEDA", "MXN");
                parametros.put("CRED_IMPORTE_LETRA", FacturacionUtil.importeLetra(crFrmEventoSolicitud.getMontoAprobado(), "mxn"));
                
            }else if(crDocImprimible.getTipoImprimible().equals(TIPO_AUT_BURO)){
                                
                double montoMensualConyugue = 0;
                double montoMensualSueldo = 0;
                double montoMensualFamiliar = 0;
                                        
                //Recuperamos el producto:                
                int idGrupoFormulario = 0;
                CrProductoCredito crProductoCreditoDto = null;
                CrProductoCredito crProductoCreditoPadreDto = null;
                CrProductoCreditoDaoImpl crProductoCreditoDaoImpl = new CrProductoCreditoDaoImpl(this.conn);
                CrProductoSeguro crProductoSeguro = new CrProductoSeguro();
                try{
                    crProductoCreditoDto = crProductoCreditoDaoImpl.findByPrimaryKey(crFrmEventoSolicitud.getIdProductoCredito());
                }catch(Exception e){}
                if(crProductoCreditoDto != null){
                    crProductoCreditoPadreDto = crProductoCreditoDaoImpl.findByPrimaryKey(crProductoCreditoDto.getIdProductoCreditoPadre());                     
                    idGrupoFormulario = crProductoCreditoPadreDto.getIdGrupoFormularioSolic();
                }
                //recuperamos variables de formulario:
                CrFormulario[] crFormularioDto = new CrFormulario[0];
                CrFormularioBO crFormularioBO = new CrFormularioBO(this.conn);
                if (idGrupoFormulario > 0){
                    //crFormularioDto = new CrFormularioDaoImpl(user.getConn()).findByDynamicWhere(" id_grupo_formulario = " + idGrupoFormulario + " AND id_estatus = 1 " , null);
                    crFormularioBO.setOrderBy("ORDER BY orden_grupo ASC");
                    crFormularioDto = crFormularioBO.findCrFormularios(-1, idEmpresa, 0, 0, " AND id_grupo_formulario = " + idGrupoFormulario + " AND id_estatus = 1");
                }
                CrFormularioRespuestaDaoImpl crFormularioRespuestaDaoImpl = new CrFormularioRespuestaDaoImpl(this.conn);
                for(CrFormulario formularios : crFormularioDto){
                    CrFormularioCampoBO crFormularioCampoBO2 = new CrFormularioCampoBO(this.conn);
                    crFormularioCampoBO2.setOrderBy(" ORDER BY orden_formulario ASC ");
                    CrFormularioCampo[] crFormularioCampo = crFormularioCampoBO2.findCrFormularioCampos(0, idEmpresa, 0, 0, (" AND id_formulario = " + formularios.getIdFormulario() + " AND id_estatus = 1 " + "" ));
                    for(CrFormularioCampo formulario : crFormularioCampo){
                        CrFormularioRespuesta crFormularioRespuesta = null;
                        try{
                            crFormularioRespuesta = crFormularioRespuestaDaoImpl.findByDynamicWhere(" id_formulario_evento = " + idCrFormularioEvento + " AND id_formulario_campo = " + formulario.getIdFormularioCampo(), null)[0];
                        }catch(Exception e){}

                        if(crFormularioRespuesta!=null){
                            if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("LAB_SUELDO_NETO") > -1){
                                try{montoMensualSueldo = Double.parseDouble(crFormularioRespuesta.getValor());}catch(Exception e){}
                            }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("CNY_LAB_SUELDO") > -1){
                                try{montoMensualConyugue = Double.parseDouble(crFormularioRespuesta.getValor());}catch(Exception e){}
                            }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("OTIN_UTILIDAD_MENS") > -1){
                                try{montoMensualFamiliar = Double.parseDouble(crFormularioRespuesta.getValor());}catch(Exception e){}
                            }
                        }
                    }
                }
                
                parametros.put("CAR_AUTO_IMPORTE", crFrmEventoSolicitud.getMontoAprobado());
                parametros.put("CAR_AUTO_PLAZO", crFrmEventoSolicitud.getSapInfPlazoContrato());
                parametros.put("CAR_AUTO_PQUINCENAL", crFrmEventoSolicitud.getCuotaRegular());
                parametros.put("CAR_AUTO_ADEUDO_INST", "0");
                parametros.put("CAR_AUTO_ADEUDO_VIG", "0");
                parametros.put("CAR_AUTO_ADEUDO_TOT_V", "0");//suma de las 2 anteriores
                parametros.put("CAR_AUTO_NUE_CRED", crFrmEventoSolicitud.getCuotaRegular());
                parametros.put("CAR_AUTO_ADEUDO_TOT_C", (0+crFrmEventoSolicitud.getCuotaRegular()));//suma de las 2 anteriores
                parametros.put("CAR_AUTO_SUE_BRU", montoMensualSueldo);   
                parametros.put("CAR_AUTO_SUE_NETO", montoMensualSueldo);
                parametros.put("CAR_AUTO_NIV_ENDEU", "0");
                parametros.put("CAR_AUTO_POR_PAGO", ( (crFrmEventoSolicitud.getCuotaRegular()*100)/montoMensualSueldo ));
                
            }else if(crDocImprimible.getTipoImprimible().equals(TIPO_GARANTIA_PAGARE)){                
                parametros.put("CRED_NUMERO", crFrmEventoSolicitud.getSapNoContrato());
                parametros.put("CRED_TIPO", "SIMPLE");
                parametros.put("CRED_MONEDA", "MXN");
                parametros.put("CRED_IMPORTE_LETRA", FacturacionUtil.importeLetra(crFrmEventoSolicitud.getMontoAprobado(), "mxn"));
                parametros.put("CRED_FECHA_CONTRATO", crFrmEventoSolicitud.getSapFechaApertura());
                parametros.put("CRED_MONTO", crFrmEventoSolicitud.getMontoAprobado());
            }else if(crDocImprimible.getTipoImprimible().equals(TIPO_CODIGO_BARRAS)){
                CrUtilCalculos crUtilCalculos = new CrUtilCalculos();
                String codigoBarras = crUtilCalculos.calculaCodigoBarrasOxxoBafar(crFrmEventoSolicitud.getSapBp(), crFrmEventoSolicitud.getSapNoContrato(), new BigDecimal(crFrmEventoSolicitud.getCuotaRegular()));
                parametros.put("FECHA_ELABORACION", DateManage.formatDateToNormal((ZonaHorariaBO.DateZonaHorariaByIdEmpresa(this.conn, new Date(), (int)idEmpresa).getTime())));
                parametros.put("CODE_BARRAS", codigoBarras);
            }else if(crDocImprimible.getTipoImprimible().equals(TIPO_ORDEN_PAGO)){
                if(crFrmEventoSolicitud.getSapOrdenPago() != null){
                    Gson gson = new Gson();
                    CrOrdenPago crOrdenPago = gson.fromJson(crFrmEventoSolicitud.getSapOrdenPago(), CrOrdenPago.class);
                    if(crOrdenPago != null){
                        parametros.put("PAGO_REF", crOrdenPago.getReferencia());
                        parametros.put("PAGO_EMISOR", crOrdenPago.getEmisor());
                        parametros.put("PAGO_FECHA", crOrdenPago.getFecha());
                        parametros.put("PAGO_ORDEN", crOrdenPago.getOrdenDePago());
                        parametros.put("PAGO_PROCESO", crOrdenPago.getProceso());
                        parametros.put("PAGO_CANTIDAD_MN", crOrdenPago.getImporte());
                        parametros.put("PAGO_CANTIDAD_LETRA", crOrdenPago.getCantidad());
                        parametros.put("NUM_CLIENTE", crFrmEventoSolicitud.getSapBp());
                    }
                }
            }else if(crDocImprimible.getTipoImprimible().equals(TIPO_OTRO)){
                String avalPais = "";
                String avalNacionalidad = "";
                String avalEdoCivil = "";
                String avalRegConyugal = "";
                String avalCurp = "";
                String avalCorreo = "";
                String avalNumEmpleado = "";
                String avalLaboraEmpleado = "";
                String avalLaboraPuesto = "";
                String avalFechaIngreso = "";
                
                //Recuperamos el producto:                
                int idGrupoFormulario = 0;
                CrProductoCredito crProductoCreditoDto = null;
                CrProductoCredito crProductoCreditoPadreDto = null;
                CrProductoCreditoDaoImpl crProductoCreditoDaoImpl = new CrProductoCreditoDaoImpl(this.conn);
                CrProductoSeguro crProductoSeguro = new CrProductoSeguro();
                try{
                    crProductoCreditoDto = crProductoCreditoDaoImpl.findByPrimaryKey(crFrmEventoSolicitud.getIdProductoCredito());
                }catch(Exception e){}
                if(crProductoCreditoDto != null){
                    crProductoCreditoPadreDto = crProductoCreditoDaoImpl.findByPrimaryKey(crProductoCreditoDto.getIdProductoCreditoPadre());                     
                    idGrupoFormulario = crProductoCreditoPadreDto.getIdGrupoFormularioSolic();
                }
                //recuperamos variables de formulario:
                CrFormulario[] crFormularioDto = new CrFormulario[0];
                CrFormularioBO crFormularioBO = new CrFormularioBO(this.conn);
                if (idGrupoFormulario > 0){
                    //crFormularioDto = new CrFormularioDaoImpl(user.getConn()).findByDynamicWhere(" id_grupo_formulario = " + idGrupoFormulario + " AND id_estatus = 1 " , null);
                    crFormularioBO.setOrderBy("ORDER BY orden_grupo ASC");
                    crFormularioDto = crFormularioBO.findCrFormularios(-1, idEmpresa, 0, 0, " AND id_grupo_formulario = " + idGrupoFormulario + " AND id_estatus = 1");
                }
                CrFormularioRespuestaDaoImpl crFormularioRespuestaDaoImpl = new CrFormularioRespuestaDaoImpl(this.conn);
                for(CrFormulario formularios : crFormularioDto){
                    CrFormularioCampoBO crFormularioCampoBO2 = new CrFormularioCampoBO(this.conn);
                    crFormularioCampoBO2.setOrderBy(" ORDER BY orden_formulario ASC ");
                    CrFormularioCampo[] crFormularioCampo = crFormularioCampoBO2.findCrFormularioCampos(0, idEmpresa, 0, 0, (" AND id_formulario = " + formularios.getIdFormulario() + " AND id_estatus = 1 " + "" ));
                    for(CrFormularioCampo formulario : crFormularioCampo){
                        CrFormularioRespuesta crFormularioRespuesta = null;
                        try{
                            crFormularioRespuesta = crFormularioRespuestaDaoImpl.findByDynamicWhere(" id_formulario_evento = " + idCrFormularioEvento + " AND id_formulario_campo = " + formulario.getIdFormularioCampo(), null)[0];
                        }catch(Exception e){}

                        if(crFormularioRespuesta!=null){
                            if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("AVAL_PAIS_NAC") > -1){
                                try{avalPais  = crFormularioRespuesta.getValor();}catch(Exception e){}
                            }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("AVAL_NACIONALIDAD") > -1){
                                try{avalNacionalidad  = crFormularioRespuesta.getValor();}catch(Exception e){}
                            }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("AVAL_EDO_CIVIL") > -1){
                                try{avalEdoCivil  = crFormularioRespuesta.getValor();}catch(Exception e){}
                            }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("AVAL_REG_CONYUGAL") > -1){
                                try{avalRegConyugal  = crFormularioRespuesta.getValor();}catch(Exception e){}
                            }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("AVAL_CURP") > -1){
                                try{avalCurp  = crFormularioRespuesta.getValor();}catch(Exception e){}
                            }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("AVAL_CORREO") > -1){
                                try{avalCorreo  = crFormularioRespuesta.getValor();}catch(Exception e){}
                            }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("AVAL_NUM_EMP") > -1){
                                try{avalNumEmpleado  = crFormularioRespuesta.getValor();}catch(Exception e){}
                            }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("AVAL_LAB_EMP") > -1){
                                try{avalLaboraEmpleado  = crFormularioRespuesta.getValor();}catch(Exception e){}
                            }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("AVAL_LAB_PUE") > -1){
                                try{avalLaboraPuesto  = crFormularioRespuesta.getValor();}catch(Exception e){}
                            }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("AVAL_LAB_INGRESO") > -1){
                                try{avalFechaIngreso  = crFormularioRespuesta.getValor();}catch(Exception e){}
                            }
                        }
                    }
                }
                
                ///---datos de aval:
                parametros.put("AVAL_PAIS_NAC", avalPais);
                parametros.put("AVAL_NACIONALIDAD", avalNacionalidad);
                parametros.put("AVAL_EDO_CIVIL", avalEdoCivil);
                parametros.put("AVAL_REG_CONYUGAL", avalRegConyugal);
                parametros.put("AVAL_CURP", avalCurp);
                parametros.put("AVAL_CORREO", avalCorreo);
                parametros.put("AVAL_NUM_EMP", avalNumEmpleado);
                parametros.put("AVAL_LAB_EMP", avalLaboraEmpleado);
                parametros.put("AVAL_LAB_PUE", avalLaboraPuesto);
                parametros.put("AVAL_LAB_INGRESO", avalFechaIngreso);
                ///--- fin datos de aval
                
            }
            
            //envio de un data source, en este caso para una tabla de amortización
            JRBeanCollectionDataSource ds = null;
            if(crFrmEventoSolicitud.getSapTablaAmortizacion() != null && crDocImprimible.getTipoImprimible().equals(TIPO_TABLA_AMORTIZACION)){                
                Gson gson = new Gson();
                CrTablaAmortizacion amor = gson.fromJson(crFrmEventoSolicitud.getSapTablaAmortizacion(), CrTablaAmortizacion.class);
                 
                ds = new JRBeanCollectionDataSource(amor.getListaTablaAmortizacionDetalles());
                
            }
            
            //System.setProperty("jasper.reports.compile.class.path",context.getRealPath("/WEB-INF/lib/jasperreports-3.7.4.jar") + System.getProperty("path.separator") + context.getRealPath("/WEB-INF/"));
            //System.setProperty("jasper.reports.compile.class.path","/web/WEB-INF/lib/jasperreports-3.7.4.jar");
            
            Configuration configuration = new Configuration();                                    
            String rutaDocumentoJasper = configuration.getApp_content_path()+empresaDto.getRfc()+"/CrDocumentos/" + crDocImprimible.getNombreArchivoJasper();
            
            if(ds != null){
                JasperPrint print = JasperFillManager.fillReport( 
                    rutaDocumentoJasper, // ruta a jasper
                    parametros, //parametros
                    //new JREmptyDataSource()); //datasource vacio 
                    ds);
                baos = new ByteArrayOutputStream();
                JasperExportManager.exportReportToPdfStream(print, baos);
            }else{
                JasperPrint print = JasperFillManager.fillReport( 
                    rutaDocumentoJasper, // ruta a jasper
                    parametros, //parametros
                    new JREmptyDataSource()); //datasource vacio 
                baos = new ByteArrayOutputStream();
                JasperExportManager.exportReportToPdfStream(print, baos);
            }            
            
            
            
        }catch(Exception ex){
            ex.printStackTrace();
            throw new Exception("No se ha podido generar el archivo PDF. " + GenericMethods.exceptionStackTraceToString(ex));
        }
        
        return baos;
    }
        
}
