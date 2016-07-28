/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.ComprobanteFiscal;
import com.tsp.sct.dao.dto.SarComprobante;
import com.tsp.sct.dao.dto.SarUsuarioProveedor;
import com.tsp.sct.dao.jdbc.ComprobanteFiscalDaoImpl;
import com.tsp.sct.dao.jdbc.SarComprobanteDaoImpl;
import com.tsp.sct.util.StringManage;
import com.tsp.workflow.ws.AccesoRequest;
import com.tsp.workflow.ws.ConsultaComprobanteRequest;
import com.tsp.workflow.ws.ConsultaComprobanteResponse;
import com.tsp.workflow.ws.WsItemComprobante;
import java.sql.Connection;

/**
 *
 * @author ISCesar poseidon24@hotmail.com
 * @since 19/03/2015 12:22:16 PM
 */
public class SarComprobanteBO {

    private SarComprobante sarComprobante = null;
    
    /**
     * Factura Invalida o Cancelada
     */
    public static final int FACTURA_INVALIDA = 1;
    
    /**
     * Estatus de Factura En Flujo <br/>
     * Factura Recibida y en flujo de Aprobación
     */
    public static final int FACTURA_EN_FLUJO = 2;
    /**
     * Estatus de Factura En Flujo y Reenviada <br/>
     * Factura Recibida, en flujo de Aprobación y Reenviada
     */
    public static final int FACTURA_EN_FLUJO_REENVIADA = 3;
    /**
     * Estatus de Factura Rechazada <br/>
     * Factura Rechazada por un Aprobador del Sistema
     */
    public static final int FACTURA_RECHAZADA = 4;
    /**
     * Estatus de Factura Aprobada <br/>
     * Factura Aprobada por Aprobador
     */
    public static final int FACTURA_APROBADA = 5;
    /**
     * Estatus de Factura Rechazada Cuentas por Pagar <br/>
     * Factura Rechazada por Cuentas x Pagar, aun no esta pagada
     */
    public static final int FACTURA_RECHAZADA_CUENTAS_POR_PAGAR = 6;
    /**
     * Estatus de Factura Aprobada Cuentas por Pagar <br/>
     * Factura Aprobada por Cuentas x Pagar, aun no esta pagada
     */
    public static final int FACTURA_APROBADA_CUENTAS_POR_PAGAR = 7;
    /**
     * Estatus de Factura Pagada <br/>
     * Factura Señalada como Pagada por Cuentas x Pagar
     */
    public static final int FACTURA_PAGADA = 8;

    public SarComprobante getSarComprobante() {
        return sarComprobante;
    }

    public void setSarComprobante(SarComprobante sarComprobante) {
        this.sarComprobante = sarComprobante;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SarComprobanteBO(Connection conn){
        this.conn = conn;
    }
    
    public SarComprobanteBO(int idComprobanteFiscal, Connection conn){
        this.conn = conn;
         try{
            SarComprobanteDaoImpl SarComprobanteDaoImpl = new SarComprobanteDaoImpl(this.conn);
            this.sarComprobante = SarComprobanteDaoImpl.findByPrimaryKey(idComprobanteFiscal);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * Realiza una búsqueda por ID ComprobanteFiscal en busca de
     * coincidencias
     * @param idComprobanteFiscal ID Clave principal para filtrar, -1 para mostrar todos los registros
     * @param idSarUsuario ID Sar Usuario 
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SarComprobante
     */
    public SarComprobante[] findSarComprobantes(int idComprobanteFiscal, int idSarUsuario, int minLimit,int maxLimit, String filtroBusqueda) {
        SarComprobante[] sarComprobanteDto = new SarComprobante[0];
        SarComprobanteDaoImpl sarComprobanteDao = new SarComprobanteDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idComprobanteFiscal>0){
                sqlFiltro ="ID_COMPROBANTE_FISCAL=" + idComprobanteFiscal + " AND ";
            }else{
                sqlFiltro ="ID_COMPROBANTE_FISCAL>0 AND";
            }
            if (idSarUsuario>0){                
                sqlFiltro += " ID_SAR_USUARIO = " + idSarUsuario;
            }else{
                sqlFiltro +=" ID_SAR_USUARIO>0";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            sarComprobanteDto = sarComprobanteDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_COMPROBANTE_FISCAL DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return sarComprobanteDto;
    }
    
    /**
     * Realiza una búsqueda por ID ComprobanteFiscal en busca de
     * coincidencias
     * @param idComprobanteFiscal ID Del ComprobanteFiscal para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar comprobanteFiscal, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO ComprobanteFiscal
     */
    public ComprobanteFiscal[] findComprobanteFiscal(int idComprobanteFiscal, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        ComprobanteFiscal[] comprobanteFiscalDto = new ComprobanteFiscal[0];
        ComprobanteFiscalDaoImpl comprobanteFiscalDao = new ComprobanteFiscalDaoImpl(this.conn);
        try {
            //Filtro Comprobantes entregados a SAR
            String sqlFiltro=" comprobante_fiscal.ID_COMPROBANTE_FISCAL = sar_comprobante.ID_COMPROBANTE_FISCAL ";
            if (idComprobanteFiscal>0){
                sqlFiltro +=" AND comprobante_fiscal.ID_COMPROBANTE_FISCAL=" + idComprobanteFiscal + " AND ";
            }else{
                sqlFiltro +=" AND comprobante_fiscal.ID_COMPROBANTE_FISCAL>0 AND ";
            }
            if (idEmpresa>0){
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ") ";
            }else{
                sqlFiltro +=" ID_EMPRESA>0 ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            comprobanteFiscalDto = comprobanteFiscalDao.findByDynamicSelect( 
                    "SELECT comprobante_fiscal.ID_COMPROBANTE_FISCAL, ID_TIPO_COMPROBANTE, ID_EMPRESA, ID_CLIENTE, ID_ESTATUS, ID_FOLIO, ID_FORMA_PAGO, ID_TIPO_PAGO, CONDICIONES_PAGO, FOLIO_GENERADO, FECHA_IMPRESION, FECHA_CAPTURA, FECHA_PAGO, IMPORTE_SUBTOTAL, IMPUESTOS, IMPORTE_NETO, PARCIALIDAD, CONCEPTO, ARCHIVO_CFD, CADENA_ORIGINAL, SELLO_DIGITAL, COMENTARIOS, ID_DIVISAS, ID_TIPO_MONEDA, TIPO_DE_CAMBIO, UUID, SELLO_SAT, ACUSE_CANCELACION, DESCUENTO, MOTIVO_DESCUENTO, FECHA_VIGENCIA, SECOFI, NUMERO_FACTURA, ID_PRODUCTOR, UNIDAD, NO_CERTIFICADO_SAT, FECHA_TIMBRADO, CREADO_AUTOMATICAMENTE"
                    + " FROM comprobante_fiscal, sar_comprobante WHERE"
                    + sqlFiltro
                    + " ORDER BY comprobante_fiscal.ID_COMPROBANTE_FISCAL DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return comprobanteFiscalDto;
    }
    
    public SarComprobante actualizarComprobanteFromSAR() throws Exception{
        return actualizarComprobanteFromSAR(this.sarComprobante);
    }
    
    public SarComprobante actualizarComprobanteFromSAR(SarComprobante sarComprobante) throws Exception{
        //Solamente intentamos actualizar las que estan conectadas a SAR y
        // que esten en flujo hasta antes de Pagada
        if (sarComprobante!=null && sarComprobante.getExtSarIdFactura()>0
                && sarComprobante.getExtSarIdEstatus()>=FACTURA_EN_FLUJO
                && sarComprobante.getExtSarIdEstatus()<=FACTURA_APROBADA_CUENTAS_POR_PAGAR){
            
            SarUsuarioProveedorBO sarUsuarioProveedorBO = new SarUsuarioProveedorBO(sarComprobante.getIdSarUsuario(), getConn());
            SarUsuarioProveedor sarUsuarioProveedorDto = sarUsuarioProveedorBO.getSarUsuarioProveedor();

            //Consultamos la bitacora de la factura
            AccesoRequest accesoRequest = new AccesoRequest();
            accesoRequest.setUsuario(sarUsuarioProveedorDto.getExtSarUsuario());
            accesoRequest.setContrasena(sarUsuarioProveedorDto.getExtSarPass());

            ConsultaComprobanteRequest consultaRequest = new ConsultaComprobanteRequest();
            consultaRequest.getListaIdFactura().add((long)sarComprobante.getExtSarIdFactura());

            ConsultaComprobanteResponse wsResponse = ConexionSARBO.getComprobantes(accesoRequest, consultaRequest);
            if (wsResponse.isError()){
                throw new Exception("Error al consultar comprobante en sistema SAR: " + wsResponse.getNumError() + " - " + wsResponse.getMsgError());
            }else{
                if (wsResponse.getListaComprobantes().size()>0){
                    WsItemComprobante wsItemComprobante = wsResponse.getListaComprobantes().get(0);
                    sarComprobante.setExtSarIdEstatus(wsItemComprobante.getIdEstatus());
                    sarComprobante.setExtSarDescEstatus(wsItemComprobante.getDescripcionEstatus());
                    new SarComprobanteDaoImpl(getConn()).update(sarComprobante.createPk(), sarComprobante);
                    this.sarComprobante = sarComprobante;
                }else{
                    throw new Exception("No se encontro el comprobante en el sistema SAR" +  wsResponse.getMsgError());
                }
            }
            
        }    
        
        return sarComprobante;
    }
    
}
