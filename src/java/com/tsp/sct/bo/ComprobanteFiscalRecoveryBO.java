/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.interconecta.ws.WsGenericResp;
import com.tsp.interconecta.ws.dotnet.ArrayOfString;
import com.tsp.sct.cfdi.Cfd32BO;
import com.tsp.sct.config.Configuration;
import com.tsp.sct.dao.dto.*;
import com.tsp.sct.dao.jdbc.ComprobanteDescripcionDaoImpl;
import com.tsp.sct.dao.jdbc.ComprobanteDescripcionPersonalizadaDaoImpl;
import com.tsp.sct.dao.jdbc.ComprobanteFiscalDaoImpl;
import com.tsp.sct.dao.jdbc.ComprobanteImpuestoDaoImpl;
import com.tsp.sct.dao.jdbc.ComprobanteImpuestoXConceptoDaoImpl;
import com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl;
import com.tsp.sct.dao.jdbc.NominaComprobanteDescripcionPercepcionDeduccionDaoImpl;
import com.tsp.sct.mail.TspMailBO;
import com.tsp.sct.util.Encrypter;
import com.tsp.sct.util.FileManage;
import com.tsp.sct.util.GenericValidator;
import com.tsp.sct.util.StringManage;
import com.tsp.sgfens.sesion.ComprobanteFiscalSesion;
import com.tsp.sgfens.sesion.DeduccionSesion;
import com.tsp.sgfens.sesion.ImpuestoSesion;
import com.tsp.sgfens.sesion.PercepcionSesion;
import com.tsp.sgfens.sesion.ProductoSesion;
import com.tsp.sgfens.sesion.ServicioSesion;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.PrivateKey;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import mx.bigdata.sat.security.KeyLoader;
import sun.misc.BASE64Encoder;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 15-dic-2012 
 */
public class ComprobanteFiscalRecoveryBO {
    
    private ComprobanteFiscal comprobanteFiscal  = null;
    
    private boolean ordenPorRetrasoPagoCxC =  false;

    public ComprobanteFiscal getComprobanteFiscal() {
        return comprobanteFiscal;
    }

    public void setComprobanteFiscal(ComprobanteFiscal comprobanteFiscal) {
        this.comprobanteFiscal = comprobanteFiscal;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public boolean isOrdenPorRetrasoPagoCxC() {
        return ordenPorRetrasoPagoCxC;
    }

    public void setOrdenPorRetrasoPagoCxC(boolean ordenPorRetrasoPagoCxC) {
        this.ordenPorRetrasoPagoCxC = ordenPorRetrasoPagoCxC;
    }
    
    public ComprobanteFiscalRecoveryBO(Connection conn){
        this.conn = conn;
    }
    
    public ComprobanteFiscalRecoveryBO(long idComprobanteFiscal, Connection conn){
        this.conn = conn;
        try{
            ComprobanteFiscalDaoImpl ComprobanteFiscalDaoImpl = new ComprobanteFiscalDaoImpl(this.conn);
            this.comprobanteFiscal = ComprobanteFiscalDaoImpl.findByPrimaryKey((int)idComprobanteFiscal);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public ComprobanteFiscalRecoveryBO(int idComprobanteFiscal, Connection conn){
        this.conn = conn;
        try{
            ComprobanteFiscalDaoImpl ComprobanteFiscalDaoImpl = new ComprobanteFiscalDaoImpl(this.conn);
            this.comprobanteFiscal = ComprobanteFiscalDaoImpl.findByPrimaryKey(idComprobanteFiscal);
        }catch(Exception e){
            e.printStackTrace();
        }
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
            String sqlFiltro="";
            if (idComprobanteFiscal>0){
                sqlFiltro ="ID_COMPROBANTE_FISCAL=" + idComprobanteFiscal + " AND ";
            }else{
                sqlFiltro ="ID_COMPROBANTE_FISCAL>0 AND";
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
            
            String orderBy = " ORDER BY ID_COMPROBANTE_FISCAL DESC ";
            
            if (ordenPorRetrasoPagoCxC)
                orderBy = " ORDER BY DATEDIFF(FECHA_PAGO,NOW()) ASC ";
            
            comprobanteFiscalDto = comprobanteFiscalDao.findByDynamicWhere( 
                    sqlFiltro
                    + orderBy
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return comprobanteFiscalDto;
    }
    
    /**
     * Busca la cantidad de coincidencias de una búsqueda por ID ComprobanteFiscal y otros filtros
     * @param idComprobanteFiscal ID Del ComprobanteFiscal para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar comprobanteFiscal, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO ComprobanteFiscal
     */
    public int findCantidadComprobanteFiscal(int idComprobanteFiscal, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        try {
            String sqlFiltro="";
            if (idComprobanteFiscal>0){
                sqlFiltro ="ID_COMPROBANTE_FISCAL=" + idComprobanteFiscal + " AND ";
            }else{
                sqlFiltro ="ID_COMPROBANTE_FISCAL>0 AND";
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
            
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(ID_COMPROBANTE_FISCAL) as cantidad FROM comprobante_fiscal WHERE " + 
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
    
    /**
     * Busca la cantidad de coincidencias de una búsqueda por ID ComprobanteFiscal y otros filtros
     * @param idComprobanteFiscal ID Del ComprobanteFiscal para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar comprobanteFiscal, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO ComprobanteFiscal
     */
    public String findGroupValorUnicoComprobanteFiscal(int idComprobanteFiscal, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda,
                    String selectSql) {
        String valor = "";
        try {
            String sqlFiltro="";
            if (idComprobanteFiscal>0){
                sqlFiltro ="ID_COMPROBANTE_FISCAL=" + idComprobanteFiscal + " AND ";
            }else{
                sqlFiltro ="ID_COMPROBANTE_FISCAL>0 AND";
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
            
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + selectSql + " FROM comprobante_fiscal WHERE " + 
                    sqlFiltro
                    + sqlLimit);
            if(rs.next()){
                valor = rs.getString(1);
            }
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return valor;
    }
    
    
    /**
     * Recupera todos los datos de una comprobanteFiscal desde la base de datos
     * y lo convierte a un objeto legible en sesion.
     * @return objeto ComprobanteFiscalSesion
     */
    public ComprobanteFiscalSesion getSessionFromComprobanteFiscalDB(){
        ComprobanteFiscalSesion comprobanteFiscalSesion = null;
        Encrypter encrypter = new Encrypter();
        
        try{
            Cliente clienteDto = null;
            ArrayList<ProductoSesion> listaProductoDto = new ArrayList<ProductoSesion>();
            ArrayList<ServicioSesion> listaServicioDto = new ArrayList<ServicioSesion>();
            ArrayList<ImpuestoSesion> listaImpuestoDto = new ArrayList<ImpuestoSesion>();
            ArrayList<PercepcionSesion> listaPecepcionDto = new ArrayList<PercepcionSesion>();
            ArrayList<DeduccionSesion> listaDeduccionDto = new ArrayList<DeduccionSesion>();
            List<ComprobanteDescripcionPersonalizadaBO.CampoPersonalizado> listaCamposPersonalizadosSesion =  new ArrayList<ComprobanteDescripcionPersonalizadaBO.CampoPersonalizado>();
            
            /*recuperamos deatos de cliente / prospecto */
            clienteDto = new ClienteBO(this.comprobanteFiscal.getIdCliente(),this.conn).getCliente();
            
            
            /* Recuperamos listado de productos */
            ComprobanteDescripcion[] listaProductosCompra = getProductos_Datos();
            ProductoSesion productoSesion = null;
            for (ComprobanteDescripcion itemProducto:listaProductosCompra){
                try{
                    productoSesion = new ProductoSesion();
                    productoSesion.setCantidad(itemProducto.getCantidad());
                    productoSesion.setIdProducto(itemProducto.getIdConcepto());
                    productoSesion.setPrecio(itemProducto.getPrecioUnitario());
                    productoSesion.setUnidad(itemProducto.getUnidad());
                    
                    try{
                        BigDecimal precioUnitario = new BigDecimal(itemProducto.getPrecioUnitario()).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal cantidad = new BigDecimal(itemProducto.getCantidad()).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal monto =  precioUnitario.multiply(cantidad).setScale(2, RoundingMode.HALF_UP);
                        productoSesion.setMonto(monto.doubleValue());
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }

                    listaProductoDto.add(productoSesion);
                }catch(Exception ex){ex.printStackTrace();}
            }
            
            /* Recuperamos listado de servicios */
            ComprobanteDescripcion[] listaServiciosComprobanteFiscal = getServicios_Datos();
            ServicioSesion servicioSesion = null;
            for (ComprobanteDescripcion itemServicio : listaServiciosComprobanteFiscal){
                try{
                    servicioSesion = new ServicioSesion();
                    servicioSesion.setCantidad(itemServicio.getCantidad());
                    servicioSesion.setIdServicio(itemServicio.getIdServicio());
                    servicioSesion.setPrecio(itemServicio.getPrecioUnitario());
                    servicioSesion.setUnidad(itemServicio.getUnidad());
                    
                    try{
                        BigDecimal precioUnitario = new BigDecimal(itemServicio.getPrecioUnitario()).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal cantidad = new BigDecimal(itemServicio.getCantidad()).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal monto =  precioUnitario.multiply(cantidad).setScale(2, RoundingMode.HALF_UP);
                        servicioSesion.setMonto(monto.doubleValue());
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }

                    listaServicioDto.add(servicioSesion);
                }catch(Exception ex){ex.printStackTrace();}
            }
            
            /* Recuperamos listado de impuestos */
            ComprobanteImpuesto[] listaImpuestosComprobanteFiscal = getImpuestos_Datos();
            ImpuestoSesion impuestoSesion = null;
            Impuesto impuestoDto = null;
            for (ComprobanteImpuesto itemImpuesto : listaImpuestosComprobanteFiscal){
                try{
                    impuestoDto = new ImpuestoBO(itemImpuesto.getIdImpuesto(), this.conn).getImpuesto();
                    
                    impuestoSesion = new ImpuestoSesion();
                    impuestoSesion.setIdImpuesto(impuestoDto.getIdImpuesto());
                    impuestoSesion.setNombre(impuestoDto.getNombre());
                    impuestoSesion.setDescripcion(impuestoDto.getDescripcion());
                    impuestoSesion.setPorcentaje(impuestoDto.getPorcentaje());
                    impuestoSesion.setTrasladado(impuestoDto.getTrasladado()==(short)1? true : false);
                    impuestoSesion.setImplocal(impuestoDto.getImpuestoLocal()==(short)1? true : false);
                    

                    listaImpuestoDto.add(impuestoSesion);
                }catch(Exception ex){ex.printStackTrace();}
            }
            
            /* Recuperamos listado de percepciones y deducciones*/
            NominaComprobanteDescripcionPercepcionDeduccion[] percepcionDeduccions = getPercepcionesDeducciones_Datos();
            PercepcionSesion percepcionSesion = null;
            DeduccionSesion deduccionSesion = null;
            for(NominaComprobanteDescripcionPercepcionDeduccion ncdpd : percepcionDeduccions){
                if(ncdpd.getIdPercepcionDeduccion() == 1){
                    percepcionSesion = new PercepcionSesion();
                    //percepcionSesion.setIdPercepcion(idPercepcion);
                    percepcionSesion.setIdNominaTipoPercepcion(Integer.parseInt(ncdpd.getTipoClave()));
                    percepcionSesion.setClave(ncdpd.getClavePatron());
                    percepcionSesion.setDescipcionConcepto(ncdpd.getConceptoDescripcion());
                    percepcionSesion.setUnidad("SERVICIO");
                    percepcionSesion.setImporteGravado(ncdpd.getImporteGravado());
                    percepcionSesion.setImporteExento(ncdpd.getImporteExcepto());
                    percepcionSesion.setCantidad(1);
                    percepcionSesion.setDescripcionConceptoAlterna(ncdpd.getConceptoDescripcion());
                    percepcionSesion.setIdPercepcion(ncdpd.getIdDeLaPercepcionDeduccion());
                    listaPecepcionDto.add(percepcionSesion);
                }else if (ncdpd.getIdPercepcionDeduccion() == 2){
                    deduccionSesion = new DeduccionSesion();
                    deduccionSesion.setIdNominaTipoDeduccion(Integer.parseInt(ncdpd.getTipoClave()));
                    deduccionSesion.setClave(ncdpd.getClavePatron());
                    deduccionSesion.setDescipcionConcepto(ncdpd.getConceptoDescripcion());
                    deduccionSesion.setUnidad("SERVICIO");
                    deduccionSesion.setImporteGravado(ncdpd.getImporteGravado());
                    deduccionSesion.setImporteExento(ncdpd.getImporteExcepto());
                    deduccionSesion.setCantidad(1);
                    deduccionSesion.setDescripcionConceptoAlterna(ncdpd.getConceptoDescripcion());
                    deduccionSesion.setIdDeduccion(ncdpd.getIdDeLaPercepcionDeduccion());
                    listaDeduccionDto.add(deduccionSesion);                            
                }
            }
            
            // Recuperamos listado de Datos Personalizados
            ComprobanteDescripcionPersonalizada datosPersonalizadosComprobanteFiscal = getPersonalizada_Datos();
            if (datosPersonalizadosComprobanteFiscal!=null){
                String cadenaConTokensDP =  datosPersonalizadosComprobanteFiscal.getDatosDePersonalizacion();
                if (StringManage.getValidString(cadenaConTokensDP).length()>0){
                    String[] tokensdp = cadenaConTokensDP.split("\\|"); //Separamos token en varios
                    
                    Empresa empresaPadre = new EmpresaBO(this.conn).getEmpresaMatriz(comprobanteFiscal.getIdEmpresa());
                    DatosPersonalizados[] listdpDto = new DatosPersonalizadosBO(this.conn).findDatosPersonalizados(-1, empresaPadre.getIdEmpresa() , 0, 0, "");
                    int i = 0;
                    for (DatosPersonalizados dpDto : listdpDto) {
                        ComprobanteDescripcionPersonalizadaBO.CampoPersonalizado cp
                                = new ComprobanteDescripcionPersonalizadaBO.CampoPersonalizado();
                        try{                                    
                            cp.setVariable(dpDto.getVariable());
                            cp.setDescripcion(dpDto.getDescripcion());
                            switch (dpDto.getTipo()){
                                case 1:
                                case 2:
                                case 3:
                                    cp.setTexto(true);
                                    break;
                                case 4:
                                    cp.setDecimal(true);
                                    break;
                                case 5:
                                    cp.setFecha(true);
                                    break;
                            }

                            cp.setValor(tokensdp[i]);
                        }catch(Exception e){
                            cp.setValor(null);
                        }finally{
                            listaCamposPersonalizadosSesion.add(cp);
                            i++;
                        }
                    }
                }
            }
            
            
            //Asignamos valores a variable que se dispondrá en sesion
            comprobanteFiscalSesion = new ComprobanteFiscalSesion();
            comprobanteFiscalSesion.setIdComprobanteFiscal(this.comprobanteFiscal.getIdComprobanteFiscal());
            comprobanteFiscalSesion.setComentarios(this.comprobanteFiscal.getComentarios());
            //comprobanteFiscalSesion.setDescuento_tasa(this.comprobanteFiscal.getDescuento());
            comprobanteFiscalSesion.setDescuento_importe(this.comprobanteFiscal.getDescuento());
            comprobanteFiscalSesion.setDescuento_motivo(this.comprobanteFiscal.getMotivoDescuento());
            
            comprobanteFiscalSesion.setCliente(clienteDto);
            comprobanteFiscalSesion.setListaProducto(listaProductoDto);
            comprobanteFiscalSesion.setListaServicio(listaServicioDto);
            comprobanteFiscalSesion.setListaImpuesto(listaImpuestoDto);
            comprobanteFiscalSesion.setListaPercepcion(listaPecepcionDto);
            comprobanteFiscalSesion.setListaDeduccion(listaDeduccionDto);
            comprobanteFiscalSesion.setListaCamposPersonalizados(listaCamposPersonalizadosSesion);
            
            comprobanteFiscalSesion.setIdComprobanteFiscal(this.comprobanteFiscal.getIdComprobanteFiscal());
            comprobanteFiscalSesion.setId_tipo_comprobante(this.comprobanteFiscal.getIdTipoComprobante());
            comprobanteFiscalSesion.setId_estatus(this.comprobanteFiscal.getIdEstatus());
            comprobanteFiscalSesion.setId_folio(this.comprobanteFiscal.getIdFolio());
            comprobanteFiscalSesion.setFolio_generado(this.comprobanteFiscal.getFolioGenerado());
            comprobanteFiscalSesion.setId_forma_pago(this.comprobanteFiscal.getIdFormaPago());
            comprobanteFiscalSesion.setParcialidad(this.comprobanteFiscal.getParcialidad());
            
            comprobanteFiscalSesion.setTipo_pago(new TipoPagoBO(this.comprobanteFiscal.getIdTipoPago(),this.conn).getTipoPago());
            
            comprobanteFiscalSesion.setFecha_pago(this.comprobanteFiscal.getFechaPago());
            comprobanteFiscalSesion.setArchivo_cfd(this.comprobanteFiscal.getArchivoCfd());
            
            try{
                String cadenaDesencriptada =encrypter.decodeString(this.comprobanteFiscal.getCadenaOriginal());
                comprobanteFiscalSesion.setCadena_original(cadenaDesencriptada);
            }catch(Exception ex){
                throw new Exception("No se pudo desencriptar la cadena original del comprobante. " + ex.getMessage());
            }
            
            comprobanteFiscalSesion.setSello_digital(this.comprobanteFiscal.getSelloDigital());
            comprobanteFiscalSesion.setTipo_moneda_int(this.comprobanteFiscal.getIdTipoMoneda());
            comprobanteFiscalSesion.setTipo_moneda(new TipoDeMonedaBO(this.comprobanteFiscal.getIdTipoMoneda(),this.conn).getTipoMoneda().getClave());
            comprobanteFiscalSesion.setTipo_cambio(this.comprobanteFiscal.getTipoDeCambio());
            comprobanteFiscalSesion.setUuid(this.comprobanteFiscal.getUuid());
            comprobanteFiscalSesion.setSello_sat(this.comprobanteFiscal.getSelloSat());
            
        }catch(Exception ex){
            System.out.println("Ha ocurrido un error al intentar generar un "
                    + "objeto de sesion a partir de datos de Base de Datos de "
                    + "una comprobanteFiscal: " + ex.toString());
            ex.printStackTrace();
        }
        
        return comprobanteFiscalSesion;
    }
    
    public ComprobanteDescripcion[] getProductos_Datos(){
        ComprobanteDescripcion[] conceptos = new ComprobanteDescripcion[0];
        try{
            //conceptos = new ComprobanteDescripcionDaoImpl(this.conn).findWhereIdComprobanteFiscalEquals(this.comprobanteFiscal.getIdComprobanteFiscal());
            conceptos = new ComprobanteDescripcionDaoImpl(this.conn).findByDynamicWhere("(ID_SERVICIO<=0 || ID_SERVICIO IS NULL) AND ID_COMPROBANTE_FISCAL = " + this.comprobanteFiscal.getIdComprobanteFiscal(),null);
        }catch(Exception ex){
            System.out.println("Ocurrio un error al consultar de BD los conceptos (productos) de un Comprobante Fiscal." + ex.toString());
            ex.printStackTrace();
        }
        return conceptos;
    }
    
    public ComprobanteDescripcion[] getServicios_Datos(){
        ComprobanteDescripcion[] conceptos = new ComprobanteDescripcion[0];
        try{
            //conceptos = new ComprobanteDescripcionDaoImpl(this.conn).findWhereIdComprobanteFiscalEquals(this.comprobanteFiscal.getIdComprobanteFiscal());
            conceptos = new ComprobanteDescripcionDaoImpl(this.conn).findByDynamicWhere("ID_SERVICIO>0 AND ID_COMPROBANTE_FISCAL = " + this.comprobanteFiscal.getIdComprobanteFiscal(),null);
        }catch(Exception ex){
            System.out.println("Ocurrio un error al consultar de BD los conceptos (servicios) de un Comprobante Fiscal." + ex.toString());
            ex.printStackTrace();
        }
        return conceptos;
    }
    
    public ComprobanteImpuesto[] getImpuestos_Datos(){
        ComprobanteImpuesto[] impuestos = new ComprobanteImpuesto[0];
        try{
            impuestos = new ComprobanteImpuestoDaoImpl(this.conn).findWhereIdComprobanteFiscalEquals(this.comprobanteFiscal.getIdComprobanteFiscal());
        }catch(Exception ex){
            System.out.println("Ocurrio un error al consultar de BD los impuestos de un Comprobante Fiscal." + ex.toString());
            ex.printStackTrace();
        }
        return impuestos;
    }
    
    public NominaComprobanteDescripcionPercepcionDeduccion[] getPercepcionesDeducciones_Datos(){
        NominaComprobanteDescripcionPercepcionDeduccion[] percepcionDeduccions = new NominaComprobanteDescripcionPercepcionDeduccion[0];
        try{
            percepcionDeduccions = new NominaComprobanteDescripcionPercepcionDeduccionDaoImpl(this.conn).findWhereIdCromprobanteFiscalEquals(this.comprobanteFiscal.getIdComprobanteFiscal());
        }catch(Exception ex){
            System.out.println("Ocurrio un error al consultar de BD las percepciones y deducciones de un Comprobante Fiscal." + ex.toString());
            ex.printStackTrace();
        }
        return percepcionDeduccions;
    }
    
    public ComprobanteDescripcionPersonalizada getPersonalizada_Datos(){
        ComprobanteDescripcionPersonalizada data = null;
        try{
            ComprobanteDescripcionPersonalizada[] array = new ComprobanteDescripcionPersonalizadaDaoImpl(this.conn).findWhereIdComprobanteFiscalEquals(this.comprobanteFiscal.getIdComprobanteFiscal());
            if (array.length>0)
                data = array[0];
        }catch(Exception ex){
            System.out.println("Ocurrio un error al consultar de BD los campos Personalizados de un Comprobante Fiscal." + ex.toString());
            ex.printStackTrace();
        }
        return data;
    }
    
    /**
     * Realiza el proceso de almacenamiento de los datos correspondientes a un Comprobante Fiscal en Base de Datos, 
     * incluyendo sus conceptos, servicios e impuestos.
     * <p>
     * Adicionalmente hace la resta a los créditos de la sucursal del folio gastado al facturar.
     * 
     * @param comprobanteFiscalSesion Objeto ComprobanteFiscalSesion con todos los datos de una factura.
     * @param user UsuarioBO datos de credenciales del usuario en Sesion
     * @return ComprobanteFiscal generado, dto con los datos registrados en BD
     * @throws Exception 
     */
    public ComprobanteFiscal creaActualizaComprobanteFiscal(ComprobanteFiscalSesion comprobanteFiscalSesion, UsuarioBO user) throws Exception{
        Configuration appConfig = new Configuration();
        ComprobanteFiscal comprobanteFiscalDto = null;
        ComprobanteFiscalDaoImpl comprobanteFiscalDao = new ComprobanteFiscalDaoImpl(this.conn);
        ComprobanteFiscalPk comprobanteFiscalPk = null;
        
        EmpresaBO empresaBO = new EmpresaBO(user.getUser().getIdEmpresa(),this.conn);
        
        if (comprobanteFiscalSesion.getIdComprobanteFiscal()<=0){
        //Crear
            
            //Creamos registro principal de Solicitud de Compra
            comprobanteFiscalDto = new ComprobanteFiscal();
            comprobanteFiscalDto.setIdTipoComprobante(comprobanteFiscalSesion.getId_tipo_comprobante());
            comprobanteFiscalDto.setIdCliente(comprobanteFiscalSesion.getCliente().getIdCliente());
            comprobanteFiscalDto.setIdEmpresa(user.getUser().getIdEmpresa());
            comprobanteFiscalDto.setIdEstatus(comprobanteFiscalSesion.getId_estatus());
            comprobanteFiscalDto.setIdFolio(comprobanteFiscalSesion.getId_folio());
            comprobanteFiscalDto.setIdFormaPago(comprobanteFiscalSesion.getId_forma_pago());
            try{if(!comprobanteFiscalSesion.getCondicionesPagoDescripcion().trim().equals("")){
                comprobanteFiscalDto.setCondicionesPago(comprobanteFiscalSesion.getCondicionesPagoDescripcion().trim());
            }}catch(Exception e){}
            try{
                comprobanteFiscalDto.setIdTipoPago(comprobanteFiscalSesion.getTipo_pago().getIdTipoPago());
            }catch(Exception ex){
                throw new Exception("No se recupero información correcta sobre el tipo de pago. Verifique e intente de nuevo.");
            }
            comprobanteFiscalDto.setFolioGenerado(comprobanteFiscalSesion.getFolio_generado());
            comprobanteFiscalDto.setFechaImpresion(new Date());
            comprobanteFiscalDto.setFechaCaptura(new Date());
            comprobanteFiscalDto.setFechaPago(comprobanteFiscalSesion.getFecha_pago());
            comprobanteFiscalDto.setImporteSubtotal(comprobanteFiscalSesion.calculaSubTotal().floatValue());
            comprobanteFiscalDto.setImpuestos(0);
            if(comprobanteFiscalDto.getIdTipoComprobante()==TipoComprobanteBO.TIPO_NOMINA)
                comprobanteFiscalDto.setImporteNeto(comprobanteFiscalSesion.calculaTotalNomina(comprobanteFiscalSesion.getListaImpuesto()).floatValue());
            else{
                comprobanteFiscalDto.setImporteNeto(comprobanteFiscalSesion.calculaTotal().floatValue());                
                if(comprobanteFiscalSesion.isFacturaConConceptosConImpuestos())//PARA VALIDAR Y DE SER ASI ENVIAR EL VALOR CORRESPONDIENTE
                    comprobanteFiscalDto.setImporteNeto(comprobanteFiscalSesion.calculaTotalImpuestoEnConceptos().floatValue());
            }
            comprobanteFiscalDto.setParcialidad(comprobanteFiscalSesion.getParcialidad());
            comprobanteFiscalDto.setConcepto("");
            
            if (comprobanteFiscalSesion.getArchivo_cfd().trim().length()<=0)
                throw new Exception("No se ha especificado la ruta del archivo XML generado para almacenar el registro. Es posible que no haya sido creado.");
            comprobanteFiscalDto.setArchivoCfd(comprobanteFiscalSesion.getArchivo_cfd());
            
            try{
                Encrypter encrypter = new Encrypter();
                String cadenaEncriptada =encrypter.encodeString2(comprobanteFiscalSesion.getCadena_original());
                comprobanteFiscalDto.setCadenaOriginal(cadenaEncriptada);
            }catch(Exception ex){
                throw new Exception("No se pudo encriptar la cadena original del comprobante al almacenar.");
            }
            
            //Restamos Crédito/Folio al facturar, solo si es un nuevo comprobante fiscal
            empresaBO.comprobanteFiscalSesion = comprobanteFiscalSesion;//le pasamos el comprobante para poder obtener la info de los datos que del comprobantes generado con creditos de emergencia
            empresaBO.montoTotalComprobante = comprobanteFiscalDto.getImporteNeto();// para pasar el monto del comprobante y guardarlo como dato de crédito de emergencia
            empresaBO.restaFolioCredito();
            
            
            comprobanteFiscalDto.setSelloDigital(comprobanteFiscalSesion.getSello_digital());        
            comprobanteFiscalDto.setComentarios(comprobanteFiscalSesion.getComentarios());
            comprobanteFiscalDto.setIdDivisas(-1);
            comprobanteFiscalDto.setIdTipoMoneda(comprobanteFiscalSesion.getTipo_moneda_int());
            comprobanteFiscalDto.setTipoDeCambio((float)comprobanteFiscalSesion.getTipo_cambio());
            comprobanteFiscalDto.setUuid(comprobanteFiscalSesion.getUuid());
            comprobanteFiscalDto.setSelloSat(comprobanteFiscalSesion.getSello_sat());
            //comprobanteFiscalDto.setAcuseCancelacion(comprobanteFiscalSesion.getAc);
            //comprobanteFiscalDto.setDescuento(comprobanteFiscalSesion.getDescuentoImporte().floatValue());
            comprobanteFiscalDto.setDescuento(new BigDecimal(comprobanteFiscalSesion.getDescuento_tasa()).setScale(2, RoundingMode.HALF_UP).floatValue());
            comprobanteFiscalDto.setMotivoDescuento(comprobanteFiscalSesion.getDescuento_motivo());
            comprobanteFiscalDto.setNoCertificadoSat(comprobanteFiscalSesion.getNoCertificadoSAT());
            comprobanteFiscalDto.setFechaTimbrado(comprobanteFiscalSesion.getFechaTimbrado());
            
            
            int nextIdComprobanteFiscal = -1;
            try{
                ComprobanteFiscal lastComprobanteFiscal = comprobanteFiscalDao.findLast();
                if (lastComprobanteFiscal!=null){
                    nextIdComprobanteFiscal = comprobanteFiscalDao.findLast().getIdComprobanteFiscal() + 1;
                }else{
                    //Ocurre solamente cuando la base de datos no tiene ningun registro de Comprobante Fiscal
                    nextIdComprobanteFiscal = 1;
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }

            if (nextIdComprobanteFiscal<=0)
                throw new Exception("No se pudo calcular correctamente el siguiente ID para insertar el registro de ComprobanteFiscal");
            
            comprobanteFiscalDto.setIdComprobanteFiscal(nextIdComprobanteFiscal);
            
            try{
                comprobanteFiscalPk = comprobanteFiscalDao.insert(comprobanteFiscalDto);
            }catch(Exception ex){
                ex.printStackTrace();
                
                //En caso de error al insertar, se retorna el Crédito restado
                empresaBO.sumaFolioCredito();
                
                throw new Exception("Error al crear registro principal de ComprobanteFiscal. " + ex.toString());
            }
            
        }else{
        //Actualizar
            try{
                throw new Exception("No implementado: La actualización de datos de un Comprobante Fiscal no es permitido.");
                
                /*
                comprobanteFiscalDto = comprobanteFiscalDao.findByPrimaryKey(comprobanteFiscalSesion.getIdComprobanteFiscal());
                comprobanteFiscalDto.setComentarios(comprobanteFiscalSesion.getComentarios());
                //comprobanteFiscalDto.setConsecutivoComprobanteFiscal(0);
                comprobanteFiscalDto.setDescuentoMonto(comprobanteFiscalSesion.getDescuentoImporte().doubleValue());
                comprobanteFiscalDto.setDescuentoMotivo(comprobanteFiscalSesion.getDescuento_motivo());
                comprobanteFiscalDto.setDescuentoTasa(comprobanteFiscalSesion.getDescuento_tasa());
                comprobanteFiscalDto.setFechaComprobanteFiscal(new Date());
                //comprobanteFiscalDto.setFolioComprobanteFiscal("");
                comprobanteFiscalDto.setIdCliente(comprobanteFiscalSesion.getCliente()!=null?comprobanteFiscalSesion.getCliente().getIdCliente():0);
                comprobanteFiscalDto.setIdEmpresa(user.getUser().getIdEmpresa());
                comprobanteFiscalDto.setIdUsuarioVendedor(user.getUser().getIdUsuarios());
                comprobanteFiscalDto.setSubtotal(comprobanteFiscalSesion.calculaSubTotal().doubleValue());
                comprobanteFiscalDto.setTiempoEntregaDias(0);
                comprobanteFiscalDto.setTipoMoneda(comprobanteFiscalSesion.getTipo_moneda());
                comprobanteFiscalDto.setTotal(comprobanteFiscalSesion.calculaTotal().doubleValue());
                
                comprobanteFiscalDto.setFechaEntrega(comprobanteFiscalSesion.getFechaEntrega());
                comprobanteFiscalDto.setFechaTentativaPago(comprobanteFiscalSesion.getFechaTentativaPago());
                comprobanteFiscalDto.setIdComprobanteFiscal(comprobanteFiscalSesion.getIdComprobanteFiscal());
                comprobanteFiscalDto.setSaldoPagado(comprobanteFiscalSesion.getSaldoPagado());
                comprobanteFiscalDto.setAdelanto(comprobanteFiscalSesion.getAdelanto());
                comprobanteFiscalDto.setIdEstatusComprobanteFiscal((short)comprobanteFiscalSesion.getIdEstatus());
                
                comprobanteFiscalDao.update(comprobanteFiscalDto.createPk(), comprobanteFiscalDto);
                
                //Eliminamos de bd los elementos borrados en sesion, para que la información sea integra
                eliminaItemsComprobanteFiscal(comprobanteFiscalSesion);
                */
            }catch(Exception ex){
                ex.printStackTrace();
                throw new Exception("Error al actualizar registro principal de ComprobanteFiscal. " + ex.toString());
            }
        }
        
        ComprobanteDescripcionDaoImpl comprobanteDescripcionDao = new ComprobanteDescripcionDaoImpl(this.conn);
        int nextIdComprobanteDescripcion = -1;
        
        //Guardamos Productos Seleccionados
        for (ProductoSesion prodSesion:comprobanteFiscalSesion.getListaProducto()){
            ComprobanteDescripcion prodComprobanteFiscal = new ComprobanteDescripcion();
            
            try{
                Concepto conceptoDto = new ConceptoBO(prodSesion.getIdProducto(),this.conn).getConcepto();
                
                prodComprobanteFiscal.setIdComprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal());
                
                prodComprobanteFiscal.setCantidad((float)prodSesion.getCantidad());
                prodComprobanteFiscal.setIdConcepto(prodSesion.getIdProducto());
                prodComprobanteFiscal.setDescripcion(prodSesion.getDescripcionAlternativa()!=null?prodSesion.getDescripcionAlternativa():conceptoDto.getDescripcion());
                prodComprobanteFiscal.setIdentificacion(conceptoDto.getIdentificacion());
                prodComprobanteFiscal.setPrecioUnitario((float)prodSesion.getPrecio());
                prodComprobanteFiscal.setUnidad(prodSesion.getUnidad());
                prodComprobanteFiscal.setIdServicioNull(true); //Por valor default, la BD aplicara -1
                prodComprobanteFiscal.setIdAlmacenOrigen(prodSesion.getIdAlmacen());
                if(prodSesion.getDescuento_tasa() > 0){
                    BigDecimal calculoMontoDescuento = ( BigDecimal.valueOf(prodSesion.getDescuento_tasa()).multiply(BigDecimal.valueOf(0.01)).multiply(BigDecimal.valueOf(prodSesion.getPrecio())).multiply(BigDecimal.valueOf(prodSesion.getCantidad())) );                    
                    prodComprobanteFiscal.setDescuentoMonto(calculoMontoDescuento.floatValue());
                    prodComprobanteFiscal.setDescuentoPorcentaje((float)prodSesion.getDescuento_tasa());
                }
                if(prodSesion.getIdImpuesto()>0){                
                    Impuesto impues = new Impuesto();
                    ImpuestoBO impuesto = new ImpuestoBO(prodSesion.getIdImpuesto(),this.conn);
                    impues = impuesto.getImpuesto();
                    prodComprobanteFiscal.setIdImpuesto(prodSesion.getIdImpuesto());                    
                }
                try{
                    prodComprobanteFiscal.setNombreConcepto(new ConceptoBO(this.conn).desencripta(conceptoDto.getNombre()));
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                
                try{
                    ComprobanteDescripcion lastComprobanteDescripcion = comprobanteDescripcionDao.findLast();
                    if (lastComprobanteDescripcion!=null){
                        nextIdComprobanteDescripcion = comprobanteDescripcionDao.findLast().getIdComprobanteDescripcion() + 1;
                    }else{
                        nextIdComprobanteDescripcion = 1;
                    }
                        
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                
                if (nextIdComprobanteDescripcion<=0)
                    throw new Exception("No se pudo calcular correctamente el siguiente ID para insertar el registro de concepto en la tabla ComprobanteDescripcion");
                        
                prodComprobanteFiscal.setIdComprobanteDescripcion(nextIdComprobanteDescripcion);
                
                comprobanteDescripcionDao.insert(prodComprobanteFiscal);
            }catch(Exception ex){
                throw new Exception("Error al intentar insertar registro de concepto (producto): " +ex);
            }
        }
        
        //Guardamos Servicios Seleccionados
        int idConceptoGenerico = -1;
        
        try{
            idConceptoGenerico = Integer.parseInt(appConfig.getBd_sct_idconceptogenerico());
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        nextIdComprobanteDescripcion = -1;
        for (ServicioSesion servicioSesion:comprobanteFiscalSesion.getListaServicio()){
            ComprobanteDescripcion servicioComprobanteFiscal = new ComprobanteDescripcion();
            
            try{
                Servicio servicioDto = new ServicioBO(servicioSesion.getIdServicio(),this.conn).getServicio();
                
                
                servicioComprobanteFiscal.setIdComprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal());

                servicioComprobanteFiscal.setCantidad((float)servicioSesion.getCantidad());
                servicioComprobanteFiscal.setIdConcepto(idConceptoGenerico);
                servicioComprobanteFiscal.setIdServicio(servicioSesion.getIdServicio());
                servicioComprobanteFiscal.setDescripcion(servicioDto.getDescripcion());
                servicioComprobanteFiscal.setIdentificacion(servicioDto.getSku());
                servicioComprobanteFiscal.setPrecioUnitario((float)servicioSesion.getPrecio());
                servicioComprobanteFiscal.setUnidad(servicioSesion.getUnidad());
                servicioComprobanteFiscal.setNombreConcepto(servicioDto.getNombre());

                try{
                    nextIdComprobanteDescripcion = comprobanteDescripcionDao.findLast().getIdComprobanteDescripcion() + 1;
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                
                if (nextIdComprobanteDescripcion<=0)
                    throw new Exception("No se pudo calcular correctamente el siguiente ID para insertar el registro de concepto en la tabla ComprobanteDescripcion");
                        
                servicioComprobanteFiscal.setIdComprobanteDescripcion(nextIdComprobanteDescripcion);
                
                comprobanteDescripcionDao.insert(servicioComprobanteFiscal);
            }catch(Exception ex){
                throw new Exception("Error al intentar insertar registro de concepto (servicio): " +ex);
            }
        }
        
        //Guardamos Impuestos Seleccionados
        for (ImpuestoSesion impuestoSesion:comprobanteFiscalSesion.getListaImpuesto()){           
            
           if(comprobanteFiscalSesion.getGuardarEnImpuestosXconcepto() == 0 ){
            try{
                ComprobanteImpuesto impuestoComprobanteFiscal = new ComprobanteImpuesto();
                 ComprobanteImpuestoDaoImpl daoImpl = new ComprobanteImpuestoDaoImpl(this.conn);
                 ComprobanteImpuesto cimpuesto = daoImpl.findLast();
                 int idImpuesto = cimpuesto.getIdImpuesto() + 1;
                 impuestoComprobanteFiscal.setIdComprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal());
                 impuestoComprobanteFiscal.setIdImpuesto(impuestoSesion.getIdImpuesto());
                 impuestoComprobanteFiscal.setIdComprobanteImpuesto(idImpuesto);

                 new ComprobanteImpuestoDaoImpl(this.conn).insert(impuestoComprobanteFiscal);
             }catch(Exception ex){
                 ex.printStackTrace();
             }
           }else if(comprobanteFiscalSesion.getGuardarEnImpuestosXconcepto() == 1 ){
            try{
                ComprobanteImpuestoXConcepto impuestoComprobanteFiscal = new ComprobanteImpuestoXConcepto();
                 
                impuestoComprobanteFiscal.setIdComprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal());
                impuestoComprobanteFiscal.setIdImpuesto(impuestoSesion.getIdImpuesto());
                impuestoComprobanteFiscal.setNombre(impuestoSesion.getNombre());
                impuestoComprobanteFiscal.setDescripcion(impuestoSesion.getDescripcion());
                impuestoComprobanteFiscal.setPorcentaje((float) impuestoSesion.getPorcentaje());
                if(impuestoSesion.isTrasladado())
                    impuestoComprobanteFiscal.setTrasladado((short)1);
                else
                    impuestoComprobanteFiscal.setTrasladado((short)0);
                if(impuestoSesion.isImplocal())
                    impuestoComprobanteFiscal.setImpuestoLocal((short)1);
                else
                    impuestoComprobanteFiscal.setImpuestoLocal((short)0);

                 new ComprobanteImpuestoXConceptoDaoImpl(this.conn).insert(impuestoComprobanteFiscal);
            }catch(Exception ex){
                ex.printStackTrace();
            }
           }
        }
        
        this.comprobanteFiscal = comprobanteFiscalDto;
        
        return comprobanteFiscalDto;
    }
    
    public ComprobanteFiscal creaCBBActualizaComprobanteFiscal(ComprobanteFiscalSesion comprobanteFiscalSesion, UsuarioBO user) throws Exception{
        Configuration appConfig = new Configuration();
        ComprobanteFiscal comprobanteFiscalDto = null;
        ComprobanteFiscalDaoImpl comprobanteFiscalDao = new ComprobanteFiscalDaoImpl(this.conn);
        ComprobanteFiscalPk comprobanteFiscalPk = null;
        
        EmpresaBO empresaBO = new EmpresaBO(user.getUser().getIdEmpresa(),this.conn);
        
        if (comprobanteFiscalSesion.getIdComprobanteFiscal()<=0){
        //Crear
            
            //Restamos Crédito/Folio al facturar, solo si es un nuevo comprobante fiscal
            empresaBO.restaFolioCredito();
            
            //Creamos registro principal de Solicitud de Compra
            comprobanteFiscalDto = new ComprobanteFiscal();
            comprobanteFiscalDto.setIdTipoComprobante(comprobanteFiscalSesion.getId_tipo_comprobante());
            comprobanteFiscalDto.setIdCliente(comprobanteFiscalSesion.getCliente().getIdCliente());
            comprobanteFiscalDto.setIdEmpresa(user.getUser().getIdEmpresa());
            comprobanteFiscalDto.setIdEstatus(comprobanteFiscalSesion.getId_estatus());
            comprobanteFiscalDto.setIdFolio(comprobanteFiscalSesion.getId_folio());
            comprobanteFiscalDto.setIdFormaPago(comprobanteFiscalSesion.getId_forma_pago());
            try{
                comprobanteFiscalDto.setIdTipoPago(comprobanteFiscalSesion.getTipo_pago().getIdTipoPago());
            }catch(Exception ex){
                throw new Exception("No se recupero información correcta sobre el tipo de pago. Verifique e intente de nuevo.");
            }
            comprobanteFiscalDto.setFolioGenerado(comprobanteFiscalSesion.getFolio_generado());
            comprobanteFiscalDto.setFechaImpresion(new Date());
            comprobanteFiscalDto.setFechaCaptura(new Date());
            comprobanteFiscalDto.setFechaPago(comprobanteFiscalSesion.getFecha_pago());
            comprobanteFiscalDto.setImporteSubtotal(comprobanteFiscalSesion.calculaSubTotal().floatValue());
            comprobanteFiscalDto.setImpuestos(0);
            comprobanteFiscalDto.setImporteNeto(comprobanteFiscalSesion.calculaTotal().floatValue());
            comprobanteFiscalDto.setParcialidad(comprobanteFiscalSesion.getParcialidad());
            comprobanteFiscalDto.setConcepto("");
            
            /*if (comprobanteFiscalSesion.getArchivo_cfd().trim().length()<=0)
                throw new Exception("No se ha especificado la ruta del archivo XML generado para almacenar el registro. Es posible que no haya sido creado.");
            comprobanteFiscalDto.setArchivoCfd(comprobanteFiscalSesion.getArchivo_cfd());*/
            
            /*try{
                Encrypter encrypter = new Encrypter();
                String cadenaEncriptada =encrypter.encodeString2(comprobanteFiscalSesion.getCadena_original());
                comprobanteFiscalDto.setCadenaOriginal(cadenaEncriptada);
            }catch(Exception ex){
                throw new Exception("No se pudo encriptar la cadena original del comprobante al almacenar.");
            }*/
            
            
            //comprobanteFiscalDto.setSelloDigital(comprobanteFiscalSesion.getSello_digital());        
            comprobanteFiscalDto.setComentarios(comprobanteFiscalSesion.getComentarios());
            comprobanteFiscalDto.setIdDivisas(-1);
            comprobanteFiscalDto.setIdTipoMoneda(comprobanteFiscalSesion.getTipo_moneda_int());
            comprobanteFiscalDto.setTipoDeCambio((float)comprobanteFiscalSesion.getTipo_cambio());
            //comprobanteFiscalDto.setUuid(comprobanteFiscalSesion.getUuid());
            //comprobanteFiscalDto.setSelloSat(comprobanteFiscalSesion.getSello_sat());
            //comprobanteFiscalDto.setAcuseCancelacion(comprobanteFiscalSesion.getAc);
            comprobanteFiscalDto.setDescuento(comprobanteFiscalSesion.getDescuentoImporte().floatValue());
            comprobanteFiscalDto.setMotivoDescuento(comprobanteFiscalSesion.getDescuento_motivo());
            
            int nextIdComprobanteFiscal = -1;
            try{
                ComprobanteFiscal lastComprobanteFiscal = comprobanteFiscalDao.findLast();
                if (lastComprobanteFiscal!=null){
                    nextIdComprobanteFiscal = comprobanteFiscalDao.findLast().getIdComprobanteFiscal() + 1;
                }else{
                    //Ocurre solamente cuando la base de datos no tiene ningun registro de Comprobante Fiscal
                    nextIdComprobanteFiscal = 1;
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }

            if (nextIdComprobanteFiscal<=0)
                throw new Exception("No se pudo calcular correctamente el siguiente ID para insertar el registro de ComprobanteFiscal");
            
            comprobanteFiscalDto.setIdComprobanteFiscal(nextIdComprobanteFiscal);
            
            try{
                comprobanteFiscalPk = comprobanteFiscalDao.insert(comprobanteFiscalDto);
            }catch(Exception ex){
                ex.printStackTrace();
                
                //En caso de error al insertar, se retorna el Crédito restado
                empresaBO.sumaFolioCredito();
                
                throw new Exception("Error al crear registro principal de ComprobanteFiscal. " + ex.toString());
            }
            
        }else{
        //Actualizar
            try{
                throw new Exception("No implementado: La actualización de datos de un Comprobante Fiscal no es permitido.");
                
                /*
                comprobanteFiscalDto = comprobanteFiscalDao.findByPrimaryKey(comprobanteFiscalSesion.getIdComprobanteFiscal());
                comprobanteFiscalDto.setComentarios(comprobanteFiscalSesion.getComentarios());
                //comprobanteFiscalDto.setConsecutivoComprobanteFiscal(0);
                comprobanteFiscalDto.setDescuentoMonto(comprobanteFiscalSesion.getDescuentoImporte().doubleValue());
                comprobanteFiscalDto.setDescuentoMotivo(comprobanteFiscalSesion.getDescuento_motivo());
                comprobanteFiscalDto.setDescuentoTasa(comprobanteFiscalSesion.getDescuento_tasa());
                comprobanteFiscalDto.setFechaComprobanteFiscal(new Date());
                //comprobanteFiscalDto.setFolioComprobanteFiscal("");
                comprobanteFiscalDto.setIdCliente(comprobanteFiscalSesion.getCliente()!=null?comprobanteFiscalSesion.getCliente().getIdCliente():0);
                comprobanteFiscalDto.setIdEmpresa(user.getUser().getIdEmpresa());
                comprobanteFiscalDto.setIdUsuarioVendedor(user.getUser().getIdUsuarios());
                comprobanteFiscalDto.setSubtotal(comprobanteFiscalSesion.calculaSubTotal().doubleValue());
                comprobanteFiscalDto.setTiempoEntregaDias(0);
                comprobanteFiscalDto.setTipoMoneda(comprobanteFiscalSesion.getTipo_moneda());
                comprobanteFiscalDto.setTotal(comprobanteFiscalSesion.calculaTotal().doubleValue());
                
                comprobanteFiscalDto.setFechaEntrega(comprobanteFiscalSesion.getFechaEntrega());
                comprobanteFiscalDto.setFechaTentativaPago(comprobanteFiscalSesion.getFechaTentativaPago());
                comprobanteFiscalDto.setIdComprobanteFiscal(comprobanteFiscalSesion.getIdComprobanteFiscal());
                comprobanteFiscalDto.setSaldoPagado(comprobanteFiscalSesion.getSaldoPagado());
                comprobanteFiscalDto.setAdelanto(comprobanteFiscalSesion.getAdelanto());
                comprobanteFiscalDto.setIdEstatusComprobanteFiscal((short)comprobanteFiscalSesion.getIdEstatus());
                
                comprobanteFiscalDao.update(comprobanteFiscalDto.createPk(), comprobanteFiscalDto);
                
                //Eliminamos de bd los elementos borrados en sesion, para que la información sea integra
                eliminaItemsComprobanteFiscal(comprobanteFiscalSesion);
                */
            }catch(Exception ex){
                ex.printStackTrace();
                throw new Exception("Error al actualizar registro principal de ComprobanteFiscal. " + ex.toString());
            }
        }
        
        ComprobanteDescripcionDaoImpl comprobanteDescripcionDao = new ComprobanteDescripcionDaoImpl(this.conn);
        int nextIdComprobanteDescripcion = -1;
        
        //Guardamos Productos Seleccionados
        for (ProductoSesion prodSesion:comprobanteFiscalSesion.getListaProducto()){
            ComprobanteDescripcion prodComprobanteFiscal = new ComprobanteDescripcion();
            
            try{
                Concepto conceptoDto = new ConceptoBO(prodSesion.getIdProducto(), this.conn).getConcepto();
                
                prodComprobanteFiscal.setIdComprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal());
                
                prodComprobanteFiscal.setCantidad((float)prodSesion.getCantidad());
                prodComprobanteFiscal.setIdConcepto(prodSesion.getIdProducto());
                prodComprobanteFiscal.setDescripcion(prodSesion.getDescripcionAlternativa()!=null?prodSesion.getDescripcionAlternativa():conceptoDto.getDescripcion());
                prodComprobanteFiscal.setIdentificacion(conceptoDto.getIdentificacion());
                prodComprobanteFiscal.setPrecioUnitario((float)prodSesion.getPrecio());
                prodComprobanteFiscal.setUnidad(prodSesion.getUnidad());
                prodComprobanteFiscal.setIdServicioNull(true); //Por valor default, la BD aplicara -1
                prodComprobanteFiscal.setIdAlmacenOrigen(prodSesion.getIdAlmacen());
                try{
                    prodComprobanteFiscal.setNombreConcepto(new ConceptoBO(this.conn).desencripta(conceptoDto.getNombre()));
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                
                try{
                    ComprobanteDescripcion lastComprobanteDescripcion = comprobanteDescripcionDao.findLast();
                    if (lastComprobanteDescripcion!=null){
                        nextIdComprobanteDescripcion = comprobanteDescripcionDao.findLast().getIdComprobanteDescripcion() + 1;
                    }else{
                        nextIdComprobanteDescripcion = 1;
                    }
                        
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                
                if (nextIdComprobanteDescripcion<=0)
                    throw new Exception("No se pudo calcular correctamente el siguiente ID para insertar el registro de concepto en la tabla ComprobanteDescripcion");
                        
                prodComprobanteFiscal.setIdComprobanteDescripcion(nextIdComprobanteDescripcion);
                
                comprobanteDescripcionDao.insert(prodComprobanteFiscal);
            }catch(Exception ex){
                throw new Exception("Error al intentar insertar registro de concepto (producto): " +ex);
            }
        }
        
        //Guardamos Servicios Seleccionados
        int idConceptoGenerico = -1;
        
        try{
            idConceptoGenerico = Integer.parseInt(appConfig.getBd_sct_idconceptogenerico());
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        nextIdComprobanteDescripcion = -1;
        for (ServicioSesion servicioSesion:comprobanteFiscalSesion.getListaServicio()){
            ComprobanteDescripcion servicioComprobanteFiscal = new ComprobanteDescripcion();
            
            try{
                Servicio servicioDto = new ServicioBO(servicioSesion.getIdServicio(), this.conn).getServicio();
                
                
                servicioComprobanteFiscal.setIdComprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal());

                servicioComprobanteFiscal.setCantidad((float)servicioSesion.getCantidad());
                servicioComprobanteFiscal.setIdConcepto(idConceptoGenerico);
                servicioComprobanteFiscal.setIdServicio(servicioSesion.getIdServicio());
                servicioComprobanteFiscal.setDescripcion(servicioDto.getDescripcion());
                servicioComprobanteFiscal.setIdentificacion(servicioDto.getSku());
                servicioComprobanteFiscal.setPrecioUnitario((float)servicioSesion.getPrecio());
                servicioComprobanteFiscal.setUnidad(servicioSesion.getUnidad());
                servicioComprobanteFiscal.setNombreConcepto(servicioDto.getNombre());

                try{
                    nextIdComprobanteDescripcion = comprobanteDescripcionDao.findLast().getIdComprobanteDescripcion() + 1;
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                
                if (nextIdComprobanteDescripcion<=0)
                    throw new Exception("No se pudo calcular correctamente el siguiente ID para insertar el registro de concepto en la tabla ComprobanteDescripcion");
                        
                servicioComprobanteFiscal.setIdComprobanteDescripcion(nextIdComprobanteDescripcion);
                
                comprobanteDescripcionDao.insert(servicioComprobanteFiscal);
            }catch(Exception ex){
                throw new Exception("Error al intentar insertar registro de concepto (servicio): " +ex);
            }
        }
        
        //Guardamos Impuestos Seleccionados
        for (ImpuestoSesion impuestoSesion:comprobanteFiscalSesion.getListaImpuesto()){
            ComprobanteImpuesto impuestoComprobanteFiscal = new ComprobanteImpuesto();
            
            try{              
                ComprobanteImpuestoDaoImpl daoImpl = new ComprobanteImpuestoDaoImpl(this.conn);
                ComprobanteImpuesto cimpuesto = daoImpl.findLast();
                int idImpuesto = cimpuesto.getIdImpuesto() + 1;
                impuestoComprobanteFiscal.setIdComprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal());
                impuestoComprobanteFiscal.setIdImpuesto(impuestoSesion.getIdImpuesto());
                impuestoComprobanteFiscal.setIdComprobanteImpuesto(idImpuesto);

                new ComprobanteImpuestoDaoImpl(this.conn).insert(impuestoComprobanteFiscal);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
        this.comprobanteFiscal = comprobanteFiscalDto;
        
        return comprobanteFiscalDto;
    }
    
    
    
    /**
     * Elimina de BD todos los items de un Comprobante Fiscal para luego proceder
     * a volver a insertar, esto con la finalidad de cuando se Actualiza un Comprobante
     * y se quiere evitar inconsistencia en los datos (sesion <-> BD)
     */
    public void eliminaItemsComprobanteFiscal(ComprobanteFiscalSesion comprobanteFiscalSesion){
        if (comprobanteFiscalSesion!=null){
            int idComprobanteFiscal = comprobanteFiscalSesion.getIdComprobanteFiscal();
            
            if(idComprobanteFiscal>0){
                
                //Productos y Servicios (Conceptos)
                {
                    ComprobanteDescripcion[] comprobanteFiscalProdArray = new ComprobanteDescripcion[0];
                    ComprobanteDescripcionDaoImpl comprobanteFiscalProdDao = new ComprobanteDescripcionDaoImpl(this.conn);
                    try{
                        comprobanteFiscalProdArray = comprobanteFiscalProdDao.findWhereIdComprobanteFiscalEquals(idComprobanteFiscal);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    for (ComprobanteDescripcion itemProductoBD:comprobanteFiscalProdArray){
                        try {
                            comprobanteFiscalProdDao.delete(itemProductoBD.createPk());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }   
                }
        
                //Impuestos
                {
                    ComprobanteImpuesto[] comprobanteFiscalImpuestoArray = new ComprobanteImpuesto[0];
                    ComprobanteImpuestoDaoImpl comprobanteFiscalImpuestoDao = new ComprobanteImpuestoDaoImpl(this.conn);
                    try{
                        comprobanteFiscalImpuestoArray = comprobanteFiscalImpuestoDao.findWhereIdComprobanteFiscalEquals(idComprobanteFiscal);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    for (ComprobanteImpuesto itemImpuestoBD:comprobanteFiscalImpuestoArray){
                        try {
                            comprobanteFiscalImpuestoDao.delete(itemImpuestoBD.createPk());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                
            }
            
        }
    }
    
    
    /**
     * Envia un correo a los destinatarios específicados
     * con el comprobanteFiscal
     * @return Cadena en caso de error
     */
    public String enviaNotificacionNuevaComprobanteFiscal(List<String> destinatarios, File fileXML, File filePDF,int idEmpresa){
         //Validamos si es clienete de EVC
        EmpresaPermisoAplicacion empresaPermisoDto = null;
        try{
            EmpresaPermisoAplicacionDaoImpl  empresaPermisoDao = new EmpresaPermisoAplicacionDaoImpl();
            empresaPermisoDto = empresaPermisoDao.findWhereIdEmpresaEquals(idEmpresa)[0];
        }catch(Exception e){e.printStackTrace();}
        
        String respuesta ="";
        
        if (this.comprobanteFiscal!=null){
            try {
                TspMailBO mailBO = new TspMailBO();
                
                if(empresaPermisoDto.getAccesoSgfensPretomovil()==1){
                    mailBO.setConfigurationMovilpyme();
                } else{
                    mailBO.setConfiguration();              
                }
                
                GenericValidator genericValidator = new GenericValidator();
                //Agregamos todos los correos de los usuarios Cuentas Por Pagar
                for (String destinatario : destinatarios){
                    try{                           
                        if(genericValidator.isEmail(destinatario)){
                            try{
                                mailBO.addTo(destinatario, destinatario);
                            }catch(Exception ex){}
                        }
                    }catch(Exception ex){}
                }
                
                String rfcDestinatario = "";
                if(this.comprobanteFiscal.getIdTipoComprobante() == TipoComprobanteBO.TIPO_NOMINA){
                    NominaEmpleado nomEmpleadoDto = new NominaEmpleadoBO(this.conn, this.comprobanteFiscal.getIdCliente()).getNominaEmpleado();
                    rfcDestinatario = nomEmpleadoDto.getNombre() + " " + nomEmpleadoDto.getApellidoPaterno() + " " + nomEmpleadoDto.getApellidoMaterno();
                }else{
                    Cliente clienteDto = new ClienteBO(this.comprobanteFiscal.getIdCliente(),this.conn).getCliente();
                    rfcDestinatario = clienteDto.getRazonSocial();
                }
                
                if(empresaPermisoDto.getAccesoSgfensPretomovil()==1){
                    mailBO.addMessageMovilpyme("<b>¡Buen día!</b> "
                        + "<br/>¡Saludos!"
                        + "<br/>El presente correo contiene adjunto el Comprobante Fiscal Digital generado por Pretoriano Soft. "
                        + "de " + rfcDestinatario
                        //+ "<br/>Pretoriano Soft."
                        //+ "<br/>"+ DateManage.dateToStringEspanol(new Date())
                        //+ "<br/><br/> <i>Este es un mensaje autogenerado por sistemas informáticos, no es necesario que responda a este remitente.</i>"
                        ,1);
                } else{
                    mailBO.addMessage("<b>¡Buen día!</b> "
                        + "<br/>¡Saludos!"
                        + "<br/>El presente correo contiene adjunto el Comprobante Fiscal Digital generado por Pretoriano Soft. "
                        + "de " + rfcDestinatario
                        //+ "<br/>Pretoriano Soft."
                        //+ "<br/>"+ DateManage.dateToStringEspanol(new Date())
                        //+ "<br/><br/> <i>Este es un mensaje autogenerado por sistemas informáticos, no es necesario que responda a este remitente.</i>"
                        ,1);      
                }
                                
                
                //Agregar PDF de comprobanteFiscal
                if (fileXML!=null)
                    mailBO.addFile(fileXML.getAbsolutePath(), fileXML.getName());
                if (filePDF!=null)
                    mailBO.addFile(filePDF.getAbsolutePath(), filePDF.getName());
                
                mailBO.send("Comprobante Fiscal Digital");
            } catch (Exception ex) {
                respuesta += "Ocurrio un error al intentar enviar el correo: " + ex.toString();
            }            
        }
        
        return respuesta;
    }
    
    /**
     * Calcula los dias transcurridos desde la captura hasta Hoy
     * dias transcurridos =  Fecha Hoy - Fecha Captura
     * @return 
     */
    public long calculaDiasTranscurridosCredito(){
        long diasTranscurridosCreditos = 0;
        
        Date fechaHoy=new Date();
        Date fechaCaptura = this.comprobanteFiscal.getFechaCaptura();
        
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
            SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(this.conn);
            double adeudo = cobranzaAbonoBO.getSaldoAdeudoComprobanteFiscal(this.comprobanteFiscal.getIdComprobanteFiscal()).doubleValue();
            
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
        
        Date fechaPago=this.comprobanteFiscal.getFechaPago();
        Date fechaCaptura = this.comprobanteFiscal.getFechaCaptura();
        
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
        Date fechaPago=this.comprobanteFiscal.getFechaPago();
        
        try{
            SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(this.conn);
            double adeudo = cobranzaAbonoBO.getSaldoAdeudoComprobanteFiscal(this.comprobanteFiscal.getIdComprobanteFiscal()).doubleValue();
            
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
     * Cancela el comprobante fiscal ante el sat y en la base de datos del sistema
     * @return true en caso de exito, false en caso contrario
     */
    public boolean cancelaComprobanteFiscal() throws Exception{
        boolean exito = false;
        
        //Thread.currentThread().sleep(3000);
        /*
        throw new Exception("El comprobante no se ha podido cancelar por incidencias en los servidores del SAT. <br/>"
                + "000 - System.ServiceModel.EndpointNotFoundException: No había ningún extremo escuchando en https://cancelacion.facturaelectronica.sat.gob.mx/seguridad/Autenticacion.svc que pudiera aceptar el mensaje. La causa suele ser una dirección o una acción SOAP incorrecta. Consulte InnerException, si está presente, para obtener más información. ---> System.Net.WebException: No es posible conectar con el servidor remoto ---> System.Net.Sockets.SocketException: No se ha podido establecer conexión ya que el equipo de destino ha denegado activamente dicha conexión "); 
        */
            
        String rfcEmisor=null;
        ArrayOfString UUIDs = new ArrayOfString();
        Date fechaHoraCancelacion = null;
        byte[] bytesCer = null;
        byte[] bytesKey = null;
        String strBase64PrivateKey=null;
        
        Configuration appConfig = new Configuration();
            
        if (this.comprobanteFiscal==null){
            throw new Exception("No se específico el Comprobante Fiscal a cancelar. Objeto no existente."); 
        }
        
        //Datos de empresa
        Empresa empresaDto = new EmpresaBO(this.conn).findEmpresabyId(this.comprobanteFiscal.getIdEmpresa());
        
        //Intentamos cancelación primaria, borrando archivo en Cron de Envío
//Editado 02/10/2013, conexion a CMM
// no buscar en repositorio de envio
        if (false){//FileManage.findAndDeleteFileNameLike(appConfig.getRutaRepositorio_EnvioCFDI32(), this.comprobanteFiscal.getArchivoCfd())){
            exito = true;
            comprobanteFiscal.setComentarios("Cancelación interna. CFDI cancelado de Cron de Envío previo a ser enviado a SAT.");
        }else{
            //Datos de certificados de la empresa
            File cerEmisor = null;
            File keyEmisor = null;
            String passwordKeyEmisor = null;
            String rutaArchivosUsuario = null;
            {
                CertificadoDigitalBO certificadoDigitalBO = new CertificadoDigitalBO(this.conn);
                CertificadoDigital certificadoDigitalDto = certificadoDigitalBO.findCertificadoByEmpresa(empresaDto.getIdEmpresa());

                if (certificadoDigitalDto!=null){
                    rutaArchivosUsuario = appConfig.getApp_content_path() + empresaDto.getRfc() + File.separator;

                    String rutaCerEmisor = rutaArchivosUsuario + certificadoDigitalDto.getNombreCer();
                    String rutaKeyEmisor = rutaArchivosUsuario + certificadoDigitalDto.getNombreKey();
                    passwordKeyEmisor = certificadoDigitalDto.getPassword();

                    cerEmisor = new File(rutaCerEmisor);
                    keyEmisor = new File(rutaKeyEmisor);

                    if ( !cerEmisor.exists() || !keyEmisor.exists()){
                        throw new Exception("Alguno de los archivos de Certificados CSD (.cer, .key) del Emisor no existen en el directorio de archivos del cliente. Avise a su proveedor."); 
                    }

                }
            }

            if (cerEmisor==null || keyEmisor==null || passwordKeyEmisor==null){
                throw new Exception("No se pudo recuperar los Certificados CSD (.cer, .key) y/o password, posiblemente la sucursal no tenga alguno configurado."); 
            }

            /**
            * Recopilamos datos necesarios para cancelación
            */
            try{

                rfcEmisor = empresaDto.getRfc();

                fechaHoraCancelacion = new Date();

                UUIDs.getString().add(this.comprobanteFiscal.getUuid());

                bytesCer = FileManage.getBytesFromFile(cerEmisor);

                bytesKey = FileManage.getBytesFromFile(keyEmisor);
                {
                    PrivateKey privateKeyEmisor = KeyLoader.loadPKCS8PrivateKey(new ByteArrayInputStream(bytesKey), passwordKeyEmisor);
                    //-- Obtenemos el String en Base 64 del contenido del Private Key
                    //Obtenemos el contenido codificado de la llave
                    byte[] keyEncodedBytes = privateKeyEmisor.getEncoded();
                    //Convertir a Base 64
                    BASE64Encoder b64 = new BASE64Encoder();

                    strBase64PrivateKey = b64.encode(keyEncodedBytes);
                }

            }catch(Exception e){
                e.printStackTrace();
                throw new Exception("Error al recopilar los datos necesarios para invocar la cancelación. " + e.toString()); 
            }
            
//Agregado 02/10/2013, conexion a CMM
            String XMLStringContent=null;
            try{
                XMLStringContent = FileManage.getStringFromFile(this.getComprobanteFiscalXML());
            }catch(Exception ex){
                throw new Exception("No se pudo obtener el contenido del XML para enviar a servicio de cancelacion: " + ex.toString());
            }

            if (rfcEmisor==null || fechaHoraCancelacion==null || bytesCer==null || strBase64PrivateKey==null 
                    || XMLStringContent==null){
                throw new Exception("Los datos para invocar la cancelación no estan completos. Intente de nuevo."); 
            }

            /**
            * Consumimos Web Service
            */
            int resultadoCancelacion = 0;
            InvocaWsNetCancela invocacionWsNetCancela = null;
            //RespuestaCancelacion respuestaCancelacionNET = null;
            
            WsGenericResp respCancela;

            String envioNetMsgError = "";
            byte[] envioNetAcuse = null;
            //ArrayOfFolioCancelacion arraryFoliosCancelacion = null;
            String estatusCancelacionUnico="";
            int estatusCancelarUnicoInt=0;
            try {
                invocacionWsNetCancela = new InvocaWsNetCancela();
//Editado 02/10/2013, conexion a CMM
                //respuestaCancelacionNET = invocacionWsNetCancela.cancelar(rfcEmisor, fechaHoraCancelacion, UUIDs, bytesCer, strBase64PrivateKey);
                respCancela = invocacionWsNetCancela.cancelaCFDI32(
                        appConfig.getPac_ws_timbrado_user(),    //CMM usuario
                        appConfig.getPac_ws_timbrado_pass(),    //CMM password
                        bytesCer,                          //certificado emisor          
                        bytesKey,                          //llave privada emisor
                        passwordKeyEmisor,                      //password llave privada
                        XMLStringContent);                             //Contenido XML

//Editado 02/10/2013, conexion a CMM
                /*     
                envioNetMsgError = respuestaCancelacionNET.getError();
                envioNetAcuse = respuestaCancelacionNET.getAcuseCancelacion();
                arraryFoliosCancelacion = respuestaCancelacionNET.getFoliosCancelacion();
                */
                envioNetMsgError = respCancela.getErrorMessage();
                envioNetAcuse = respCancela.getAcuse();
                
//Editado 02/10/2013, conexion a CMM
                if (!respCancela.isIsError()){
                    //ruta a carpeta acuses cancelacion: p.ej. C:\SystemaDeArchivos\TSP080724QW6\cfd\emitidos
                    String rutaArchivosAcuse = rutaArchivosUsuario + "cfd"+ File.separator +"emitidos" + File.separator;
                    File acuse = FileManage.createFileFromByteArray(envioNetAcuse, rutaArchivosAcuse , "ACUSE_CANCELACION_" + rfcEmisor+ "_" +this.comprobanteFiscal.getUuid()+"_Pretoriano.xml");

                    comprobanteFiscal.setAcuseCancelacion(acuse.getName());

                    new ComprobanteFiscalDaoImpl(this.conn).update(this.comprobanteFiscal.createPk(), this.comprobanteFiscal);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new Exception("No se pudo invocar al WebService de Cancelación del SAT. Avise a su proveedor. " + ex.toString());
            }

//Agregado 02/10/2013, conexion a CMM
            if (respCancela.isIsError()){
                throw new Exception("Invocación de Cancelación no exitosa, respuesta SAT: " 
                        + respCancela.getNumError() + " - " 
                        + respCancela.getErrorMessage());
            }else{
                estatusCancelacionUnico = StringManage.getValidString(respCancela.getFolioCodCancelacion());
                try{
                    estatusCancelarUnicoInt = Integer.parseInt(estatusCancelacionUnico);
                }catch(Exception ex){
                    throw new Exception("El codigo de cancelacion no es exitoso: " + estatusCancelacionUnico);
                }
                
                if (estatusCancelarUnicoInt==201 || 
                        estatusCancelarUnicoInt==202){
                    exito=true;
                }else{
                    String errorMsg ="";
                    switch (estatusCancelarUnicoInt) {
                        case 203:
                            errorMsg = estatusCancelarUnicoInt + " - UUID no corresponde al emisor";
                            break;
                        case 204:
                            errorMsg = estatusCancelarUnicoInt + " - UUID no aplicable para cancelación";
                            break;
                        case 205:
                            errorMsg = estatusCancelarUnicoInt + " - UUID no existe. (O no ha sido presentado al SAT aún. Intente mas tarde)";
                            break;
                        default:
                            errorMsg = "Error de cancelación Folio (SAT): " + estatusCancelacionUnico;
                    }

                    exito=false;
                    throw new Exception("Cancelación de Folio no exitosa, estatus: " + errorMsg);
                }
            }
            
             /**
            * Verificamos la respuesta del WebService
            *      000 - Error interno
            *      111 - Se envío la petición de cancelación correctamente, revisar cada UUID y su estatus
            *      XXX - Error del SAT, en caso de que no se envíe la petición correctamente (certificados incorrectos)
            */
/*        
            if (envioNetMsgError.startsWith("111")) {
                //Petición de cancelación correcta
            } else {
                //Petición de cancelación con errores
                String errorMsg = "";
                int intError = -1;
                try {
                    intError = Integer.parseInt(envioNetMsgError.substring(0, 3));
                } catch (Exception e) {
                    errorMsg = "Error inesperado";
                }
                switch (intError) {
                    case 201:
                        errorMsg = intError + " - UUID cancelado";
                        break;
                    case 202:
                        errorMsg = intError + " - UUID previamente Cancelado";
                        break;
                    case 203:
                        errorMsg = intError + " - UUID no corresponde al emisor";
                        break;
                    case 204:
                        errorMsg = intError + " - UUID no aplicable para cancelación";
                        break;
                    case 205:
                        errorMsg = intError + " - UUID no existe";
                        break;
                    case 300:
                        errorMsg = intError + " - Usuario inválido, es necesario se autentiquen los nodos de timbrado.";
                        break;
                    case 301:
                        errorMsg = intError + " - XML mal formado";
                        break;
                    case 302:
                        errorMsg = intError + " - Sello mal formado o inválido";
                        break;
                    case 303:
                        errorMsg = intError + " - Sello no corresponde a emisor o caduco";
                        break;
                    case 304:
                        errorMsg = intError + " - Certificado revocado o caduco";
                        break;
                    case 305:
                        errorMsg = intError + " - La fecha de emisión no esta dentro de la vigencia del CSD del Emisor";
                        break;
                    case 306:
                        errorMsg = intError + " - EL certificado no es de tipo CSD";
                        break;
                    case 307:
                        errorMsg = intError + " - El CFDI contiene un timbre previo";
                        break;
                    case 308:
                        errorMsg = intError + " - Certificado no expedido por el SAT";
                        break;
                    default:
                        errorMsg = envioNetMsgError;
                        intError = 903;
                        break;
                }
                throw new Exception("Invocación de Cancelación no exitosa, respuesta SAT: " + errorMsg);
            }
            
            //Se recupera el arreglo con los UUID cancelados y sus estatus
            try {

                if (arraryFoliosCancelacion != null && arraryFoliosCancelacion.getFolioCancelacion() != null && arraryFoliosCancelacion.getFolioCancelacion().size() > 0) {

                    Iterator<FolioCancelacion> iteraFolios = arraryFoliosCancelacion.getFolioCancelacion().iterator();
                    FolioCancelacion folios = new FolioCancelacion();
                    String uuidCancelar = "";

                    while (iteraFolios.hasNext()) {
                        folios = iteraFolios.next();
                        uuidCancelar = folios.getUUID();
                        estatusCancelacionUnico = folios.getEstatusUUID();
                        estatusCancelarUnicoInt = Integer.parseInt(folios.getEstatusUUID());
                    }
                    
                    if (estatusCancelarUnicoInt==201         //201 UUID cancelado correctamente
                            || estatusCancelarUnicoInt==202){  //202 UUID cancelado previamente
                        exito = true;
                    }else{
                        String errorMsg ="";
                        switch (estatusCancelarUnicoInt) {
                            case 203:
                                errorMsg = estatusCancelarUnicoInt + " - UUID no corresponde al emisor";
                                break;
                            case 204:
                                errorMsg = estatusCancelarUnicoInt + " - UUID no aplicable para cancelación";
                                break;
                            case 205:
                                errorMsg = estatusCancelarUnicoInt + " - UUID no existe";
                                break;
                            default:
                                errorMsg = "Error de cancelación Folio (SAT): " + estatusCancelacionUnico;
                        }
                        
                        exito=false;
                        throw new Exception("Cancelación de Folio no exitosa, estatus: " + errorMsg);
                    }
                    
                }
            } catch (Exception ex) {
                throw new Exception("No se pudo completar la respuesta del WebService al llenar los Folios de Cancelación. " + ex.toString());
            }
*/
        }
        
        if (exito){
            //Actualizamos registro en BD
            this.comprobanteFiscal.setIdEstatus(EstatusComprobanteBO.ESTATUS_CANCELADA);
            
            new ComprobanteFiscalDaoImpl(this.conn).update(this.comprobanteFiscal.createPk(), this.comprobanteFiscal);
        }
        
        return exito;
    }
    
    public boolean cancelaComprobanteFiscalCBB() throws Exception{
        boolean exito = false;
        
        //Thread.currentThread().sleep(3000);
        /*
        throw new Exception("El comprobante no se ha podido cancelar por incidencias en los servidores del SAT. <br/>"
                + "000 - System.ServiceModel.EndpointNotFoundException: No había ningún extremo escuchando en https://cancelacion.facturaelectronica.sat.gob.mx/seguridad/Autenticacion.svc que pudiera aceptar el mensaje. La causa suele ser una dirección o una acción SOAP incorrecta. Consulte InnerException, si está presente, para obtener más información. ---> System.Net.WebException: No es posible conectar con el servidor remoto ---> System.Net.Sockets.SocketException: No se ha podido establecer conexión ya que el equipo de destino ha denegado activamente dicha conexión "); 
        */
            
        String rfcEmisor=null;
        ArrayOfString UUIDs = new ArrayOfString();
        Date fechaHoraCancelacion = null;
        byte[] bytesCer = null;
        String strBase64PrivateKey=null;
        
        Configuration appConfig = new Configuration();
            
        if (this.comprobanteFiscal==null){
            throw new Exception("No se específico el Comprobante Fiscal a cancelar. Objeto no existente."); 
        }
        
        //Datos de empresa
        Empresa empresaDto = new EmpresaBO(this.conn).findEmpresabyId(this.comprobanteFiscal.getIdEmpresa());
        
        //Intentamos cancelación primaria, borrando archivo en Cron de Envío
        /*if (FileManage.findAndDeleteFileNameLike(appConfig.getRutaRepositorio_EnvioCFDI32(), this.comprobanteFiscal.getArchivoCfd())){
            exito = true;
            comprobanteFiscal.setComentarios("Cancelación interna. CFDI cancelado de Cron de Envío previo a ser enviado a SAT.");
        }else{
            //Datos de certificados de la empresa
            File cerEmisor = null;
            File keyEmisor = null;
            String passwordKeyEmisor = null;
            String rutaArchivosUsuario = null;
            {
                CertificadoDigitalBO certificadoDigitalBO = new CertificadoDigitalBO(this.conn);
                CertificadoDigital certificadoDigitalDto = certificadoDigitalBO.findCertificadoByEmpresa(empresaDto.getIdEmpresa());

                if (certificadoDigitalDto!=null){
                    rutaArchivosUsuario = appConfig.getApp_content_path() + empresaDto.getRfc() + File.separator;

                    String rutaCerEmisor = rutaArchivosUsuario + certificadoDigitalDto.getNombreCer();
                    String rutaKeyEmisor = rutaArchivosUsuario + certificadoDigitalDto.getNombreKey();
                    passwordKeyEmisor = certificadoDigitalDto.getPassword();

                    cerEmisor = new File(rutaCerEmisor);
                    keyEmisor = new File(rutaKeyEmisor);

                    if ( !cerEmisor.exists() || !keyEmisor.exists()){
                        throw new Exception("Alguno de los archivos de Certificados CSD (.cer, .key) del Emisor no existen en el directorio de archivos del cliente. Avise a su proveedor."); 
                    }

                }
            }

            if (cerEmisor==null || keyEmisor==null || passwordKeyEmisor==null){
                throw new Exception("No se pudo recuperar los Certificados CSD (.cer, .key) y/o password, posiblemente la sucursal no tenga alguno configurado."); 
            }

            /**
            * Recopilamos datos necesarios para cancelación
            */
      /*      try{

                rfcEmisor = empresaDto.getRfc();

                fechaHoraCancelacion = new Date();

                UUIDs.getString().add(this.comprobanteFiscal.getUuid());

                bytesCer = FileManage.getBytesFromFile(cerEmisor);

                byte[] bytesKey = FileManage.getBytesFromFile(keyEmisor);
                {
                    PrivateKey privateKeyEmisor = KeyLoader.loadPKCS8PrivateKey(new ByteArrayInputStream(bytesKey), passwordKeyEmisor);
                    //-- Obtenemos el String en Base 64 del contenido del Private Key
                    //Obtenemos el contenido codificado de la llave
                    byte[] keyEncodedBytes = privateKeyEmisor.getEncoded();
                    //Convertir a Base 64
                    BASE64Encoder b64 = new BASE64Encoder();

                    strBase64PrivateKey = b64.encode(keyEncodedBytes);
                }

            }catch(Exception e){
                e.printStackTrace();
                throw new Exception("Error al recopilar los datos necesarios para invocar la cancelación. " + e.toString()); 
            }

            if (rfcEmisor==null || fechaHoraCancelacion==null || bytesCer==null || strBase64PrivateKey==null){
                throw new Exception("Los datos para invocar la cancelación no estan completos. Intente de nuevo."); 
            }

            /**
            * Consumimos Web Service
            */
    /*        int resultadoCancelacion = 0;
            InvocaWsNetCancela invocacionWsNetCancela = null;
            RespuestaCancelacion respuestaCancelacionNET = null;

            String envioNetMsgError = "";
            byte[] envioNetAcuse = null;
            ArrayOfFolioCancelacion arraryFoliosCancelacion = null;
            try {
                invocacionWsNetCancela = new InvocaWsNetCancela();
                respuestaCancelacionNET = invocacionWsNetCancela.cancelar(rfcEmisor, fechaHoraCancelacion, UUIDs, bytesCer, strBase64PrivateKey);

                envioNetMsgError = respuestaCancelacionNET.getError();
                envioNetAcuse = respuestaCancelacionNET.getAcuseCancelacion();
                arraryFoliosCancelacion = respuestaCancelacionNET.getFoliosCancelacion();
                
                //ruta a carpeta acuses cancelacion: p.ej. C:\SystemaDeArchivos\TSP080724QW6\cfd\emitidos
                String rutaArchivosAcuse = rutaArchivosUsuario + "cfd"+ File.separator +"emitidos" + File.separator;
                File acuse = FileManage.createFileFromByteArray(envioNetAcuse, rutaArchivosAcuse , "ACUSE_CANCELACION_" + rfcEmisor+ "_" +this.comprobanteFiscal.getUuid()+"_Pretoriano.xml");
                
                comprobanteFiscal.setAcuseCancelacion(acuse.getName());
                
                new ComprobanteFiscalDaoImpl(this.conn).update(this.comprobanteFiscal.createPk(), this.comprobanteFiscal);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new Exception("No se pudo invocar al WebService de Cancelación del SAT. Avise a su proveedor. " + ex.toString());
            }
            
             /**
            * Verificamos la respuesta del WebService
            *      000 - Error interno
            *      111 - Se envío la petición de cancelación correctamente, revisar cada UUID y su estatus
            *      XXX - Error del SAT, en caso de que no se envíe la petición correctamente (certificados incorrectos)
            */
    /*        if (envioNetMsgError.startsWith("111")) {
                //Petición de cancelación correcta
            } else {
                //Petición de cancelación con errores
                String errorMsg = "";
                int intError = -1;
                try {
                    intError = Integer.parseInt(envioNetMsgError.substring(0, 3));
                } catch (Exception e) {
                    errorMsg = "Error inesperado";
                }
                switch (intError) {
                    case 201:
                        errorMsg = intError + " - UUID cancelado";
                        break;
                    case 202:
                        errorMsg = intError + " - UUID previamente Cancelado";
                        break;
                    case 203:
                        errorMsg = intError + " - UUID no corresponde al emisor";
                        break;
                    case 204:
                        errorMsg = intError + " - UUID no aplicable para cancelación";
                        break;
                    case 205:
                        errorMsg = intError + " - UUID no existe";
                        break;
                    case 300:
                        errorMsg = intError + " - Usuario inválido, es necesario se autentiquen los nodos de timbrado.";
                        break;
                    case 301:
                        errorMsg = intError + " - XML mal formado";
                        break;
                    case 302:
                        errorMsg = intError + " - Sello mal formado o inválido";
                        break;
                    case 303:
                        errorMsg = intError + " - Sello no corresponde a emisor o caduco";
                        break;
                    case 304:
                        errorMsg = intError + " - Certificado revocado o caduco";
                        break;
                    case 305:
                        errorMsg = intError + " - La fecha de emisión no esta dentro de la vigencia del CSD del Emisor";
                        break;
                    case 306:
                        errorMsg = intError + " - EL certificado no es de tipo CSD";
                        break;
                    case 307:
                        errorMsg = intError + " - El CFDI contiene un timbre previo";
                        break;
                    case 308:
                        errorMsg = intError + " - Certificado no expedido por el SAT";
                        break;
                    default:
                        errorMsg = envioNetMsgError;
                        intError = 903;
                        break;
                }
                throw new Exception("Invocación de Cancelación no exitosa, respuesta SAT: " + errorMsg);
            }
            
            /*
            * Se recupera el arreglo con los UUID cancelados y sus estatus
            */
    /*        String estatusCancelacionUnico="";
            int estatusCancelarUnicoInt=0;
            try {

                if (arraryFoliosCancelacion != null && arraryFoliosCancelacion.getFolioCancelacion() != null && arraryFoliosCancelacion.getFolioCancelacion().size() > 0) {

                    Iterator<FolioCancelacion> iteraFolios = arraryFoliosCancelacion.getFolioCancelacion().iterator();
                    FolioCancelacion folios = new FolioCancelacion();
                    String uuidCancelar = "";

                    while (iteraFolios.hasNext()) {
                        folios = iteraFolios.next();
                        uuidCancelar = folios.getUUID();
                        estatusCancelacionUnico = folios.getEstatusUUID();
                        estatusCancelarUnicoInt = Integer.parseInt(folios.getEstatusUUID());
                    }
                    
                    if (estatusCancelarUnicoInt==201         //201 UUID cancelado correctamente
                            || estatusCancelarUnicoInt==202){  //202 UUID cancelado previamente
                        exito = true;
                    }else{
                        String errorMsg ="";
                        switch (estatusCancelarUnicoInt) {
                            case 203:
                                errorMsg = estatusCancelarUnicoInt + " - UUID no corresponde al emisor";
                                break;
                            case 204:
                                errorMsg = estatusCancelarUnicoInt + " - UUID no aplicable para cancelación";
                                break;
                            case 205:
                                errorMsg = estatusCancelarUnicoInt + " - UUID no existe";
                                break;
                            default:
                                errorMsg = "Error de cancelación Folio (SAT): " + estatusCancelacionUnico;
                        }
                        
                        exito=false;
                        throw new Exception("Cancelación de Folio no exitosa, estatus: " + errorMsg);
                    }
                    
                }
            } catch (Exception ex) {
                throw new Exception("No se pudo completar la respuesta del WebService al llenar los Folios de Cancelación. " + ex.toString());
            }
            
        }*/
        
        //if (exito){
            //Actualizamos registro en BD
            try{
            this.comprobanteFiscal.setIdEstatus(EstatusComprobanteBO.ESTATUS_CANCELADA);            
            new ComprobanteFiscalDaoImpl(this.conn).update(this.comprobanteFiscal.createPk(), this.comprobanteFiscal);
            exito = true;
            }catch(Exception e){e.printStackTrace();}
        //}
        
        return exito;
    }

    
    public File getComprobanteFiscalXML() throws Exception{
        String rutaArchivo="";
        Configuration appConfig = new Configuration();

        Empresa empresaDto = new EmpresaBO(this.comprobanteFiscal.getIdEmpresa(), this.conn).getEmpresa();
        
        
        String rfcDestinatario = "";
        if(this.comprobanteFiscal.getIdTipoComprobante()==TipoComprobanteBO.TIPO_NOMINA){
            NominaEmpleado nomEmpleadoDto = new NominaEmpleadoBO(this.conn, this.comprobanteFiscal.getIdCliente()).getNominaEmpleado();
            rfcDestinatario = nomEmpleadoDto.getRfc();
        }else{
            Cliente clienteDto = new ClienteBO(this.comprobanteFiscal.getIdCliente(), this.conn).getCliente();
            rfcDestinatario = clienteDto.getRfcCliente();
        }

        String archivoXML = this.comprobanteFiscal.getArchivoCfd();

        rutaArchivo = appConfig.getApp_content_path() + empresaDto.getRfc() + "/cfd/emitidos/"
                +TipoComprobanteBO.getTipoComprobanteNombreCarpeta(this.comprobanteFiscal.getIdTipoComprobante()) 
                +"/" + rfcDestinatario
                + "/" + archivoXML;
        
        File fileXML = new File(rutaArchivo);
        
        if (!fileXML.exists()){
            throw new Exception("El archivo " + fileXML.getAbsolutePath() + " no existe en el repositorio del sistema.");
        }
        
        return fileXML;
    }
    
    public File getComprobanteFiscalPDF() throws Exception{
        
        Empresa empresaDto = new EmpresaBO(this.comprobanteFiscal.getIdEmpresa(),this.conn).getEmpresa();
        Cliente clienteDto = new ClienteBO(this.comprobanteFiscal.getIdCliente(),this.conn).getCliente();
        
        Cfd32BO cfd3BO = null;
        try {
            cfd3BO = new Cfd32BO(this.getComprobanteFiscalXML());
            cfd3BO.setComprobanteFiscalDto(this.comprobanteFiscal);
        } catch (Exception ex) {
            throw new Exception("No se pudo reconstruir el objeto CFDI 3.2 a partir del XML almacenado. " + ex.toString());
        }
        
        ByteArrayOutputStream baos;
        File archivoRepresentacionImpresa;
        try {
            baos = cfd3BO.toPdf();
            
            Configuration appConfig = new Configuration();
            
            String rutaRepositorio = appConfig.getApp_content_path() + empresaDto.getRfc() + "/cfd/emitidos/"
                +TipoComprobanteBO.getTipoComprobanteNombreCarpeta(this.comprobanteFiscal.getIdTipoComprobante()) 
                +"/" + clienteDto.getRfcCliente();
            String nombreArchivo = "CFD_" + empresaDto.getRfc() + "_" + (new Date()).getTime() + ".pdf";
            
            File directorio = new File(rutaRepositorio);
            if (!directorio.exists())
                directorio.mkdirs();
            
            //Guardamos archivo PDF
            archivoRepresentacionImpresa = new File(rutaRepositorio + "/" + nombreArchivo);
            FileOutputStream fos = new FileOutputStream(archivoRepresentacionImpresa);
            baos.writeTo(fos);
            fos.flush();
            fos.close();
            
        } catch (Exception ex) {
            throw new Exception("No se pudo generar o almacenar representación impresa PDF. " + ex.toString());
        }
        
        return archivoRepresentacionImpresa;
    }
    
    public String generacionEnvioArhivosCorreo(){
        System.out.println("##################### GENERANDO ENVIO DE CORREO");
        //recuperamos a quienes se enciara por correo(cliente o remitente o destinatario):
        ArrayList<String> listCorreosValidos = new ArrayList<String>();
        /*if(this.comprobanteFiscal.getIdTipoComprobante()==TipoComprobanteBO.TIPO_CARTA_PORTE){ //si es de tipo carta porte, recuperamos destinatario y remitente
            Cliente remitente = new ClienteBO(this.comprobanteFiscal.getIdClienteRemitente(), conn).getCliente();
            Cliente consignatario = new ClienteBO(this.comprobanteFiscal.getIdClienteConsignatario(), conn).getCliente();
            
            if(remitente != null){
                if(!remitente.getCorreo().trim().equals(""))
                    listCorreosValidos.add(remitente.getCorreo().trim());
            }
            if(consignatario != null){
                if(!consignatario.getCorreo().trim().equals(""))
                    listCorreosValidos.add(consignatario.getCorreo().trim());
            }
            //armamos el lis de las direcciones de correo que se enviaran
        }*/
        String rfcDestinatario = "";
        if(this.comprobanteFiscal.getIdTipoComprobante() == TipoComprobanteBO.TIPO_NOMINA){ //si es de tipo carta porte, recuperamos destinatario y remitente
            NominaEmpleado nomEmpleadoDto = new NominaEmpleadoBO(this.conn, this.comprobanteFiscal.getIdCliente()).getNominaEmpleado();            
            if(nomEmpleadoDto != null){
                if(!nomEmpleadoDto.getCorreo().trim().equals(""))
                    listCorreosValidos.add(nomEmpleadoDto.getCorreo().trim());
                    rfcDestinatario = nomEmpleadoDto.getRfc();
            }
        }else{
            Cliente clienteDto = new ClienteBO(this.comprobanteFiscal.getIdCliente(),this.conn).getCliente();
            if(clienteDto != null){
                if(!clienteDto.getCorreo().trim().equals(""))
                    listCorreosValidos.add(clienteDto.getCorreo().trim());
                rfcDestinatario = clienteDto.getRfcCliente();
            }
        }
        
         if (listCorreosValidos.size()>0){
             
            //UsuarioBO ubo = new UsuarioBO();/
            Configuration appConfig = new Configuration();
            Empresa empresa = new EmpresaBO(this.comprobanteFiscal.getIdEmpresa(), this.conn).getEmpresa();

            String rutaArchivoEnc = appConfig.getApp_content_path() + empresa.getRfc() + "/cfd/emitidos/"
            +TipoComprobanteBO.getTipoComprobanteNombreCarpeta(this.comprobanteFiscal.getIdTipoComprobante()) 
            +"/" + rfcDestinatario
            + "/";
             
            String nombreArchivo = this.comprobanteFiscal.getArchivoCfd();
            String soloNombreArchivo;
            StringTokenizer tokens = new StringTokenizer(nombreArchivo,".");   
            soloNombreArchivo = tokens.nextToken().intern();
            
            File archivoXML = new File(rutaArchivoEnc + "/" + this.comprobanteFiscal.getArchivoCfd());
            File archivoPDF = new File(rutaArchivoEnc + "/" + soloNombreArchivo+".pdf");
            /*try {
                ubo.getConn().close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }*/
                return enviaNotificacionNuevaComprobanteFiscal(listCorreosValidos,archivoXML, archivoPDF,empresa.getIdEmpresa());
         }         
         else
             return "No hay correos para enviar";       
        
    }
    
    /**
     * Metodo para crer un ByteArrayOutputStream creando un zip
     * con los archivos que corresponden  al arreglo de idComprobanteFiscal que se recibe
     * @param idsComprobanteFiscal Arreglo de Strings con los ID de los File que se descargaran
     * @param xml Indica si se quiere descargar los archivos XML correspondientes
     * @param pdf Indica si se quiere descargar los archivos PDF correspondientes
     * @return ByteArrayOutputStream con el archivo zip generado
     */
    public ByteArrayOutputStream getZipFromFiles(String[] idsComprobanteFiscal, boolean xml, boolean pdf){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if(idsComprobanteFiscal.length>0){
            ComprobanteFiscalRecoveryBO compFBO;
            ZipOutputStream zipOut = new ZipOutputStream(baos);

            for (String itemId : idsComprobanteFiscal) {
                if (StringManage.getValidString(itemId).equals(""))
                    continue;
                try {
                    compFBO = new ComprobanteFiscalRecoveryBO(Integer.parseInt(itemId), conn);
                    if (xml){ //XML
                        //Obtenemos la ruta del archivo
                        File fileXML = compFBO.getComprobanteFiscalXML();

                        FileInputStream in = new FileInputStream(fileXML);

                        //Agregamos la referencia de una entrada al ZIP
                        ZipEntry entry = new ZipEntry(fileXML.getName());
                        zipOut.putNextEntry(entry);

                        //buffer de lectura
                        byte[] b = new byte[(int)fileXML.length()];

                        //Leeemos el archivo y lo copiamos al zip
                        int len = 0;
                        while ((len = in.read(b)) != -1) {
                                zipOut.write(b, 0, len);
                        }
                        //cerramos la entrada
                        zipOut.closeEntry();
                    }
                    if (pdf){ //XML
                        //Obtenemos la ruta del archivo
                        File filePDF = compFBO.getComprobanteFiscalPDF();

                        FileInputStream in = new FileInputStream(filePDF);

                        //Agregamos la referencia de una entrada al ZIP
                        ZipEntry entry = new ZipEntry(filePDF.getName());
                        zipOut.putNextEntry(entry);

                        //buffer de lectura
                        byte[] b = new byte[(int)filePDF.length()];

                        //Leeemos el archivo y lo copiamos al zip
                        int len = 0;
                        while ((len = in.read(b)) != -1) {
                                zipOut.write(b, 0, len);
                        }
                        //cerramos la entrada
                        zipOut.closeEntry();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            try {
                //cerramos el ZIP
                zipOut.close();
            } catch (IOException ex) {
            }
        }

        return baos;
    }
    
}
