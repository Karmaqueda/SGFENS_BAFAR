/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tsp.sct.config.Configuration;
import com.tsp.sct.dao.dto.*;
import com.tsp.sct.dao.exceptions.SgfensCobranzaAbonoDaoException;
import com.tsp.sct.dao.exceptions.SgfensPedidoDaoException;
import com.tsp.sct.dao.jdbc.BancoOperacionDaoImpl;
import com.tsp.sct.dao.jdbc.ComprobanteFiscalDaoImpl;
import com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensCobranzaAbonoDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensPedidoDaoImpl;
import com.tsp.sct.mail.TspMailBO;
import com.tsp.sct.pdf.PdfITextUtil;
import com.tsp.sct.pdf.Translator;
import com.tsp.sct.util.DateManage;
import com.tsp.sct.util.GenericValidator;
import com.tsp.sgfens.report.ReportBO;
import com.tsp.sgfens.report.pdf.EventPDF;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 07-ene-2013 
 */
public class SGCobranzaAbonoBO {
    
    public int sincronizacionMicrosip = 0;
    
    private SgfensCobranzaAbono cobranzaAbono = null;

    public SgfensCobranzaAbono getCobranzaAbono() {
        return cobranzaAbono;
    }

    public void setCobranzaAbono(SgfensCobranzaAbono cobranzaAbono) {
        this.cobranzaAbono = cobranzaAbono;
    }

    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SGCobranzaAbonoBO(Connection conn) {
        this.conn = conn;
    }

    public SGCobranzaAbonoBO(long idCobranzaAbono, Connection conn) {
        this.conn = conn;
        try{
            this.cobranzaAbono = new SgfensCobranzaAbonoDaoImpl(this.conn).findByPrimaryKey((int)idCobranzaAbono);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public SGCobranzaAbonoBO(int idCobranzaAbono, Connection conn) {
        this.conn = conn;
        try{
            this.cobranzaAbono = new SgfensCobranzaAbonoDaoImpl(this.conn).findByPrimaryKey(idCobranzaAbono);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public SGCobranzaAbonoBO(SgfensCobranzaAbono cobranzaAbono, Connection conn) {
        this.cobranzaAbono = cobranzaAbono;
        this.conn = conn;
    }
    
    /**
     * Realiza una búsqueda por ID SgfensCobranzaAbono en busca de
     * coincidencias
     * @param idSgfensCobranzaAbono ID De la cobranzaAbono para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar cobranzaAbono, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensCobranzaAbono
     */
    public SgfensCobranzaAbono[] findCobranzaAbono(int idSgfensCobranzaAbono, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SgfensCobranzaAbono[] cobranzaAbonoDto = new SgfensCobranzaAbono[0];
        SgfensCobranzaAbonoDaoImpl cobranzaAbonoDao = new SgfensCobranzaAbonoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSgfensCobranzaAbono>0){
                sqlFiltro ="ID_COBRANZA_ABONO=" + idSgfensCobranzaAbono + " AND ";
            }else{
                sqlFiltro ="ID_COBRANZA_ABONO>0 AND";
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
            
//NO MODIFICAR EL ORDER BY DE ID_COBRANZA_ABONO DESC, YA QUE SE OCUPA PARA OBTENER REGISTROS COMO ULTIMO ABONO REALIZADO, PARA CALCULAR DIAS DE CREDITO OTORGADO (EN LIST DE PEDIDOS)

            cobranzaAbonoDto = cobranzaAbonoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY FECHA_ABONO DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return cobranzaAbonoDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID SgfensCobranzaAbono y otros filtros
     * @param idSgfensCobranzaAbono ID De la cobranzaAbono para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar cobranzaAbono, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensCobranzaAbono
     */
    public int findCantidadCobranzaAbono(int idSgfensCobranzaAbono, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        try {
            String sqlFiltro="";
            if (idSgfensCobranzaAbono>0){
                sqlFiltro ="ID_COBRANZA_ABONO=" + idSgfensCobranzaAbono + " AND ";
            }else{
                sqlFiltro ="ID_COBRANZA_ABONO>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(ID_COBRANZA_ABONO) as cantidad FROM sgfens_cobranza_abono WHERE " + 
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
     * Busca el numero de coincidencias por ID SgfensCobranzaAbono y otros filtros
     * @param idSgfensCobranzaAbono ID De la cobranzaAbono para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar cobranzaAbono, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensCobranzaAbono
     */
    public String findGroupValorUnicoCobranzaAbono(int idSgfensCobranzaAbono, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda, 
            String selectSql) {
        String valor = "";
        try {
            String sqlFiltro="";
            if (idSgfensCobranzaAbono>0){
                sqlFiltro ="ID_COBRANZA_ABONO=" + idSgfensCobranzaAbono + " AND ";
            }else{
                sqlFiltro ="ID_COBRANZA_ABONO>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT " + selectSql + " FROM sgfens_cobranza_abono WHERE " + 
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
       
    public SgfensCobranzaAbono getPrimerAbonoPedido(int idPedido){
        SgfensCobranzaAbono cobranzaAbono = null;
        
        try{            
            //SgfensCobranzaAbono[] abonos = new SgfensCobranzaAbonoDaoImpl(this.conn).findWhereIdPedidoEquals(idPedido);
            SgfensCobranzaAbono[] abonos = new SgfensCobranzaAbonoDaoImpl(this.conn).findByDynamicWhere(
                                    " ID_PEDIDO=" + idPedido
                                    + " ORDER BY ID_COBRANZA_ABONO ASC LIMIT 0,1 ", null);
            if (abonos.length>0)
                cobranzaAbono = abonos[0];
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return cobranzaAbono;
    }
    
    public BigDecimal getSaldoPagadoPedido(int idPedido){
        BigDecimal saldoPagado = BigDecimal.ZERO;
        
        try{
            SgfensPedido pedidoDto = new SGPedidoBO(idPedido,this.conn).getPedido();
            BigDecimal adelanto = new BigDecimal(pedidoDto.getAdelanto()).setScale(2, RoundingMode.HALF_UP);
            saldoPagado = saldoPagado.add(adelanto).setScale(2, RoundingMode.HALF_UP);
            
            SgfensCobranzaAbono[] abonos = new SgfensCobranzaAbonoDaoImpl(this.conn).findWhereIdPedidoEquals(idPedido);
            for (SgfensCobranzaAbono abono : abonos){
                if (abono.getIdEstatus()==1){//Solo se suma, si el abono no tiene estatus de Cancelado (2)
                    if (abono.getIdCobranzaMetodoPago()!= VentaMetodoPagoBO.METODO_PAGO_DEVOLUCION_EFE){//Y que no sea devolucion de Efectivo                    
                        BigDecimal abonoBigD = new BigDecimal(abono.getMontoAbono()).setScale(2, RoundingMode.HALF_UP);
                        saldoPagado = saldoPagado.add(abonoBigD).setScale(2, RoundingMode.HALF_UP);
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return saldoPagado;
    }
    
    public BigDecimal getSaldoPagadoComprobanteFiscal(int idComprobanteFiscal){
        BigDecimal saldoPagado = BigDecimal.ZERO;
        
        try{
            SgfensCobranzaAbono[] abonos = new SgfensCobranzaAbonoDaoImpl(this.conn).findWhereIdComprobanteFiscalEquals(idComprobanteFiscal);
            for (SgfensCobranzaAbono abono : abonos){
                if (abono.getIdEstatus()==1){
                    BigDecimal abonoBigD = new BigDecimal(abono.getMontoAbono()).setScale(2, RoundingMode.HALF_UP);
                    saldoPagado = saldoPagado.add(abonoBigD).setScale(2, RoundingMode.HALF_UP);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return saldoPagado;
    }
    
    public BigDecimal getSaldoAdeudoPedido(int idPedido){
        BigDecimal saldo = BigDecimal.ZERO;
        
        try{
            SgfensPedido pedidoDto = new SGPedidoBO(idPedido,this.conn).getPedido();
            
            if (pedidoDto!=null){
                BigDecimal total = new BigDecimal(pedidoDto.getTotal()).setScale(2, RoundingMode.HALF_UP);
                BigDecimal saldoPagado = this.getSaldoPagadoPedido(idPedido);
                
                saldo = total;
                saldo = saldo.subtract(saldoPagado).setScale(2, RoundingMode.HALF_UP);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return saldo;
    }
    
    public BigDecimal getSaldoAdeudoComprobanteFiscal(int idComprobanteFiscal){
        BigDecimal saldo = BigDecimal.ZERO;
        
        try{
            ComprobanteFiscal comprobanteFiscalDto = new ComprobanteFiscalBO(idComprobanteFiscal,this.conn).getComprobanteFiscal();
            
            if (comprobanteFiscalDto!=null){
                BigDecimal total = new BigDecimal(comprobanteFiscalDto.getImporteNeto()).setScale(2, RoundingMode.HALF_UP);
                BigDecimal saldoPagado = this.getSaldoPagadoComprobanteFiscal(idComprobanteFiscal);
                
                saldo = total;
                saldo = saldo.subtract(saldoPagado).setScale(2, RoundingMode.HALF_UP);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return saldo;
    }
    
    /**
     * Representación impresa PDF
     */
     public ByteArrayOutputStream toPdf(UsuarioBO user, long idPedido, int idComprobanteFiscal) throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfITextUtil obj = new PdfITextUtil();
        
        //Tamaño de documento (Tamaño Carta)
        com.lowagie.text.Document doc = new com.lowagie.text.Document(PageSize.LETTER);
        
        //Definición de Fuentes a usar
        Font letraOcho = new Font(Font.HELVETICA, 8, Font.NORMAL);
        Font letraOchoBold = new Font(Font.HELVETICA, 8, Font.BOLD);
        Font letraNueve = new Font(Font.HELVETICA, 9, Font.NORMAL);
        Font letraNueveBold = new Font(Font.HELVETICA, 9, Font.BOLD);
        Font letraNueveBoldRojo = new Font(Font.TIMES_ROMAN, 9, Font.BOLD, Color.red);
        Font letraNueveBoldAzul = new Font(Font.TIMES_ROMAN, 9, Font.BOLD, Color.blue);
        Font letra14Bold = new Font(Font.HELVETICA, 14, Font.BOLD);

        String msgError = "";
        
        File fileImageLogo = null;
        try{
            fileImageLogo = new ImagenPersonalBO(this.conn).getFileImagenPersonalByEmpresa(user.getUser().getIdEmpresa());
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        try{            
            //Preparamos writer de PDF
            PdfWriter writer = PdfWriter.getInstance(doc, baos);
            EventPDF eventPDF = new EventPDF(doc, user, ReportBO.COBRANZA_EDO_CUENTA,fileImageLogo);
            eventPDF.setConn(this.conn);
            writer.setPageEvent(eventPDF);
            
            //Ajustamos margenes de página
            //doc.setMargins(50, 50, 120, 50);
            //doc.setMargins(20, 20, 80, 20);
            doc.setMargins(10, 10, 150, 40);
            
            //Iniciamos documento
            doc.open();
            
            //Creamos tabla principal
            PdfPTable mainTable = new PdfPTable(1);
            mainTable.setTotalWidth(550);
            mainTable.setLockedWidth(true);
            
            SgfensPedido pedidoDto = null;
            ComprobanteFiscal comprobanteFiscalDto = null;
            SGPedidoBO pedidoBO = null;
            ComprobanteFiscalBO comprobanteFiscalBO = null;

            DatosUsuario datosUsuarioVendedor = null;
            Cliente clienteDto = null;
            try{
                if (idPedido>0){
                    pedidoBO = new SGPedidoBO(idPedido,this.conn);
                    pedidoDto = pedidoBO.getPedido();

                    datosUsuarioVendedor = new UsuarioBO(pedidoDto.getIdUsuarioVendedor()).getDatosUsuario();
                    clienteDto = new ClienteBO(pedidoDto.getIdCliente(),this.conn).getCliente();
                }
                if (idComprobanteFiscal>0){
                    comprobanteFiscalBO = new ComprobanteFiscalBO(idComprobanteFiscal, this.conn);
                    comprobanteFiscalDto = comprobanteFiscalBO.getComprobanteFiscal();
                    
                    clienteDto = new ClienteBO(comprobanteFiscalDto.getIdCliente(), this.conn).getCliente();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
            SgfensCobranzaAbono[] cobranzaAbonos = this.findCobranzaAbono(-1, user.getUser().getIdEmpresa(), 0, 0, pedidoDto!=null?" AND ID_PEDIDO="+idPedido:(comprobanteFiscalDto!=null?" AND ID_COMPROBANTE_FISCAL="+idComprobanteFiscal: ""));
            
            //CONTENIDO -------------
            
                //SALTO DE LÍNEA
                obj.agregaCelda(mainTable,letraOcho, " ", new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                    
                //Cabecera (Datos Generales)---------------------
                    PdfPTable tAux = new PdfPTable(1);
                    
                    
                    //Datos informativos generales
                        PdfPTable tInfoGeneral = new PdfPTable(1);
                        tInfoGeneral.setTotalWidth(160);
                        tInfoGeneral.setLockedWidth(true);

                        if (pedidoDto!=null){
                            obj.agregaCelda(tInfoGeneral,letraNueveBold,Color.lightGray, "Folio Pedido", new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                            obj.agregaCelda(tInfoGeneral,letraNueveBoldRojo, pedidoDto.getFolioPedido(), new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                            obj.agregaCelda(tInfoGeneral,letraNueveBold,Color.lightGray, "Fecha Captura", new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                            obj.agregaCelda(tInfoGeneral,letraNueve, DateManage.dateToStringEspanol(pedidoDto.getFechaPedido()), new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                            obj.agregaCelda(tInfoGeneral,letraNueveBold,Color.lightGray, "Vendedor", new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                            obj.agregaCelda(tInfoGeneral,letraNueve, datosUsuarioVendedor!=null?(datosUsuarioVendedor.getNombre() +" " + datosUsuarioVendedor.getApellidoPat()) :"Sin vendedor asignado" , new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        }else if (comprobanteFiscalDto!=null){
                            obj.agregaCelda(tInfoGeneral,letraNueveBold,Color.lightGray, "UUID Factura", new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                            obj.agregaCelda(tInfoGeneral,letraNueveBoldRojo, comprobanteFiscalDto.getUuid(), new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                            obj.agregaCelda(tInfoGeneral,letraNueveBold,Color.lightGray, "Fecha Captura", new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                            obj.agregaCelda(tInfoGeneral,letraNueve, DateManage.dateToStringEspanol(comprobanteFiscalDto.getFechaCaptura()), new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        }
                    
                        //Pintamos datos informativos Generales en la esquina superior derecha
                        PdfContentByte cb = writer.getDirectContent();
                        tInfoGeneral.writeSelectedRows(0, -1,doc.right()-180, doc.top() + 100, cb);
                    
                    //Datos de Pedido/CFDI
                    PdfPTable tInfoPedido = new PdfPTable(2);
                    tInfoPedido.setTotalWidth(550);
                    tInfoPedido.setWidths(new int[]{100,450});
                    tInfoPedido.setLockedWidth(true);
                    
                    obj.agregaCelda(tInfoPedido,letraNueveBold,Color.lightGray, "Monto Total", new boolean[]{true,true,true,true}, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 0, new int[0],1);
                    obj.agregaCelda(tInfoPedido,letraNueve, ""+new DecimalFormat("$ ###,###,###,##0.00").format(pedidoDto!=null?(pedidoDto.getBonificacionDevolucion()>0?pedidoDto.getTotal():pedidoDto.getTotal()+Math.abs(pedidoDto.getBonificacionDevolucion())):(comprobanteFiscalDto!=null?comprobanteFiscalDto.getImporteNeto():0)), new boolean[]{true,true,true,true}, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 0, new int[0],1);
                    obj.agregaCelda(tInfoPedido,letraNueveBold,Color.lightGray, "Fecha límite de Pago", new boolean[]{true,true,true,true}, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 0, new int[0],1);
                    obj.agregaCelda(tInfoPedido,letraNueve, DateManage.dateToStringEspanol( pedidoDto!=null?pedidoDto.getFechaTentativaPago():(comprobanteFiscalDto!=null?comprobanteFiscalDto.getFechaPago():null) ), new boolean[]{true,true,true,true}, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 0, new int[0],1);
                    obj.agregaCelda(tInfoPedido,letraNueveBold,Color.lightGray, "Días de credito", new boolean[]{true,true,true,true}, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 0, new int[0],1);
                    if (pedidoDto!=null)
                        obj.agregaCelda(tInfoPedido,letraNueve, ""+pedidoBO.calculaDiasCredito(), new boolean[]{true,true,true,true}, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 0, new int[0],1);
                    if (comprobanteFiscalDto!=null)
                        obj.agregaCelda(tInfoPedido,letraNueve, ""+comprobanteFiscalBO.calculaDiasCredito(), new boolean[]{true,true,true,true}, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 0, new int[0],1);
                    obj.agregaCelda(tInfoPedido,letraNueveBold,Color.lightGray, "Días de mora (atraso)", new boolean[]{true,true,true,true}, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 0, new int[0],1);
                    if (pedidoDto!=null)
                        obj.agregaCelda(tInfoPedido,letraNueve, ""+pedidoBO.calculaDiasMora(), new boolean[]{true,true,true,true}, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 0, new int[0],1);
                    if (comprobanteFiscalDto!=null)
                        obj.agregaCelda(tInfoPedido,letraNueve, ""+comprobanteFiscalBO.calculaDiasMora(), new boolean[]{true,true,true,true}, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 0, new int[0],1);
                    
                        
                    obj.agregaTabla(mainTable, tInfoPedido, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                    
                    //DOBLE SALTO DE LÍNEA
                    obj.agregaCelda(mainTable, letraOcho, null, " ",
                            new boolean[]{false, false, false, false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0], 1);
                    obj.agregaCelda(mainTable, letraOcho, null, " ",
                            new boolean[]{false, false, false, false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0], 1);
                    
                    //Datos de Cliente/Prospecto
                        PdfPTable tCliente = new PdfPTable(2);
                        tCliente.setTotalWidth(550);
                        tCliente.setLockedWidth(true);

                        obj.agregaCelda(tCliente,letraNueveBold,Color.lightGray, "Cliente", new boolean[]{false,false,false,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],2);

                        tAux = new PdfPTable(1);
                        tAux.setTotalWidth(275);
                        tAux.setLockedWidth(true);

                        try{
                            obj.agregaCelda(tAux,letraOcho, "Nombre o razón social: " + (clienteDto!=null?clienteDto.getRazonSocial():""), new boolean[]{false,true,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                            obj.agregaCelda(tAux,letraOcho, "R.F.C.: " + (clienteDto!=null?clienteDto.getRfcCliente():""), new boolean[]{false,true,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                            obj.agregaCelda(tAux,letraOcho, "Contacto: " + (clienteDto!=null?(clienteDto.getContacto()!=null?clienteDto.getContacto():"") :""), new boolean[]{false,true,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                            obj.agregaCelda(tAux,letraOcho, "\t\t\t" + (clienteDto!=null?clienteDto.getCorreo() + " \t, " + clienteDto.getTelefono():""), new boolean[]{false,true,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }

                        obj.agregaTabla(tCliente, tAux, new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);

                        obj.agregaCelda(tCliente,letraOcho, ""+
                                "DOMICILIO: \n" + (clienteDto!=null?"Calle: "+clienteDto.getCalle():"") + " " +
                                (clienteDto!=null?clienteDto.getNumero():"") + " " + (clienteDto!=null?clienteDto.getNumeroInterior():"") +
                                (clienteDto!=null?" Col: "+clienteDto.getColonia():"") +
                                (clienteDto!=null?" \nDeleg./Municipio: " +clienteDto.getMunicipio():"") + 
                                (clienteDto!=null?" Estado: " + clienteDto.getEstado():"") +
                                (clienteDto!=null?" \nC.P. " + clienteDto.getCodigoPostal():""), new boolean[]{false,true,false,true}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);

                        obj.agregaTabla(mainTable, tCliente, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                //FIN Cabecera (Datos Generales)-----------------
            
                //DOBLE SALTO DE LÍNEA
                obj.agregaCelda(mainTable, letraOcho, null, " ",
                        new boolean[]{false, false, false, false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0], 1);
                obj.agregaCelda(mainTable, letraOcho, null, " ",
                        new boolean[]{false, false, false, false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0], 1);
                    
                //Datos de Abonos (Cobranza)--------------------
                
                    int colsDetalles = 5;
                    PdfPTable tDetalles = new PdfPTable(colsDetalles);//6);
                    tDetalles.setTotalWidth(550);
                    tDetalles.setWidths(new int[]{70,120,120,120,120});
                    tDetalles.setLockedWidth(true);



                    /*CABECERA*/
                    obj.agregaCelda(tDetalles, letraNueveBoldAzul, Color.lightGray, "Pagos Registrados",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{1, 1, 1, 1}, colsDetalles);
                    
                    obj.agregaCelda(tDetalles, letraNueveBold, Color.lightGray, "ID",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                    obj.agregaCelda(tDetalles, letraNueveBold, Color.lightGray, "Fecha",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                    obj.agregaCelda(tDetalles, letraNueveBold, Color.lightGray, "Método de Pago",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                    obj.agregaCelda(tDetalles, letraNueveBold, Color.lightGray, "Identificador Operación",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                    obj.agregaCelda(tDetalles, letraNueveBold, Color.lightGray, "Monto",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                    /*FIN DE CABECERA*/
                    

                    //Listado de Productos
                    for (SgfensCobranzaAbono item:cobranzaAbonos){
                        String metodoPagoStr="No identificado";
                        SgfensCobranzaMetodoPago metodoPagoDto = new SGCobranzaMetodoPagoBO(item.getIdCobranzaMetodoPago(),this.conn).getCobranzaMetodoPago();
                        if (metodoPagoDto!=null)
                                metodoPagoStr = metodoPagoDto.getNombreMetodoPago();
                        
                        if (item!=null){

                            //ID
                            obj.agregaCelda(tDetalles, letraOcho, null, ""+item.getIdCobranzaAbono(),
                                        new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);

                            //Fecha
                            obj.agregaCelda(tDetalles, letraOcho, null, DateManage.dateTimeToStringEspanol(item.getFechaAbono()),
                                        new boolean[]{true, true, true, true}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);

                            //Método de Pago
                            obj.agregaCelda(tDetalles, letraOcho, null, metodoPagoStr,
                                        new boolean[]{true, true, true, true}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);

                            //Identificador Operación
                            obj.agregaCelda(tDetalles, letraOcho, null, ""+item.getIdentificadorOperacion(),
                                        new boolean[]{true, true, true, true}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);

                            String estatusCancelado = "";
                            if (item.getIdEstatus()!=1)
                                estatusCancelado = "\nC A N C E L A D O";
                                
                            //Monto
                            obj.agregaCelda(tDetalles, letraOcho, null, " "+new DecimalFormat("$ ###,###,###,##0.00").format(item.getMontoAbono()) + estatusCancelado,
                                        new boolean[]{true, true, true, true}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);

                            //totalMonto+=item.getMonto();
                        }
                    }
                    
                    obj.agregaTabla(mainTable, tDetalles, new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
            
                //FIN Datos de Pedido (Productos/Servicios)----------------
            
                //DOBLE SALTO DE LÍNEA
                        obj.agregaCelda(mainTable, letraOcho, null, " ",
                                new boolean[]{false, false, false, false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0], 1);
                        obj.agregaCelda(mainTable, letraOcho, null, " ",
                                new boolean[]{false, false, false, false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0], 1);
            
                //Datos de Totales ------------------------------
                    /*
                    Translator numALetra = new Translator();
                    String totalConLetra = "";
                    String moneda = "MXN";
                    if (pedidoDto!=null)
                        moneda = pedidoDto.getTipoMoneda();
                    if (comprobanteFiscalDto!=null)
                        moneda = new TipoDeMonedaBO(comprobanteFiscalDto.getIdTipoMoneda()).getTipoMoneda().getClave();
                    try{
                        if (moneda!=null){
                            if (!"".equals(moneda)){
                                numALetra.setNombreMoneda(moneda);
                            }
                        }

                        if (pedidoDto!=null)
                            totalConLetra = numALetra.getStringOfNumber(pedidoDto.getTotal());
                    }catch(Exception e){}                  
                   */
                   
                    PdfPTable tTotal = new PdfPTable(2);
                    tTotal.setTotalWidth(550);
                    tTotal.setWidths(new int[]{400,150});
                    tTotal.setLockedWidth(true);
                    
                    //SALDOS----------     
                    double saldoPendiente =0;
                    double saldoPagado =0;
                    try{
                        if (pedidoDto!=null){
                            saldoPendiente = this.getSaldoAdeudoPedido(pedidoDto.getIdPedido()).doubleValue();
                            saldoPagado = this.getSaldoPagadoPedido(pedidoDto.getIdPedido()).doubleValue();
                        }else if (comprobanteFiscalDto!=null){
                            saldoPendiente = this.getSaldoAdeudoComprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal()).doubleValue();
                            saldoPagado = this.getSaldoPagadoComprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal()).doubleValue();
                        }
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    obj.agregaCelda(tTotal,letraNueve, "", new boolean[]{false,true,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                    obj.agregaCelda(tTotal,letraOchoBold,Color.lightGray, "SALDOS", new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                    
                    obj.agregaCelda(tTotal,letraNueve, "", new boolean[]{false,true,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                        //Tabla auxiliar para saldos
                        tAux = new PdfPTable(2);
                        tAux.setTotalWidth(150);

                        if (pedidoDto!=null){
                            obj.agregaCelda(tAux,letraOcho, "ADELANTO" , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                            obj.agregaCelda(tAux,letraOcho, "" + new DecimalFormat("$###,###,###,##0.00").format(pedidoDto.getAdelanto()), new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 0, new int[0],1);
                        }

                        obj.agregaCelda(tAux,letraOchoBold, "PAGADO" , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                        obj.agregaCelda(tAux,letraNueveBoldAzul, "" + new DecimalFormat("$###,###,###,##0.00").format(saldoPagado) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 0, new int[0],1);
                        
                        obj.agregaCelda(tAux,letraNueveBold, "" , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM, 10, new int[0],2);
                        obj.agregaCelda(tAux,letraNueveBold, "" , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM, 10, new int[0],2);
                        
                        obj.agregaCelda(tAux,letraNueveBold, "PENDIENTE" , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM, 10, new int[0],1);
                        obj.agregaCelda(tAux,letraNueveBoldRojo, "" + new DecimalFormat("$###,###,###,##0.00").format(pedidoDto.getBonificacionDevolucion()>0?pedidoDto.getTotal()- pedidoDto.getSaldoPagado():(pedidoDto.getTotal() + Math.abs(pedidoDto.getBonificacionDevolucion())) - pedidoDto.getSaldoPagado()), new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_BOTTOM, 10, new int[0],1);

                        obj.agregaTabla(tTotal, tAux, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);

                    obj.agregaTabla(mainTable, tTotal, new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                   
                   //FIN Datos de Totales --------------------------
            //FIN DE CONTENIDO --------
            
            //Añadimos tabla principal construida al documento
            doc.add(mainTable);
            mainTable.flushContent();
            
        }catch(Exception ex){
           msgError = "No se ha podido generar el estado de Cuenta en formato PDF:<br/>" + ex.toString();
        }finally{
            if(doc.isOpen())
                doc.close();
        }
        
        if (!msgError.equals("")) {
            throw new Exception(msgError);
        }
         
        return baos;
     }
     
     /**
      * Registra el abono a un Pedido o Factura
      * @param usuarioDto Objeto con los datos del usuario ejecutor del abono
      * @param idPedido ID unico del pedido
      * @param idComprobanteFiscal ID unico de la factura
      * @param monto Monto del abono
      * @param idMetodoPago ID del metodo de pago utilizado
      * @param identificadorOperacion Datos de la operación en caso de transferencia o pago con documento
      * @param comentarios Comentarios adicionales al abono
      * @param idOperacionBancaria ID unico del registro de Banco Operacion para pagos con TDC
      * @param latitud Latitud de geoposición, 0 en caso de no tener
      * @param longitud Longitud de geoposición, 0 en caso de no tener
      * @param archivoImagenFirma File, archivo con imagen de firma, null en caso de no tener
     * @param folioCobranzaMovil String, cadena con el Folio de Cobranza Movil, null en caso de ser de consola o no aplicar
     * @param fechaCobranza Date, Fecha en que se registro el cobro originalmente
     * @param isConsola boolean, Indica si el registro se capturo en consola, false en caso de captura en movil
     * @return 
     * @throws java.lang.Exception 
      */
     public int registrarAbono(Usuarios usuarioDto, int idPedido, 
             int idComprobanteFiscal, double monto, int idMetodoPago, 
             String identificadorOperacion, String comentarios, 
             int idOperacionBancaria,
             double latitud, double longitud,
             File archivoImagenFirma,
             String folioCobranzaMovil,
             Date fechaCobranza,
             boolean isConsola, String referencia,Date fechaReferencia) throws ArithmeticException, Exception{
         
        int idAbonoCobranza = -1;
        int idCliente=-1;

        BigDecimal montoBigD = new BigDecimal(monto).setScale(2, RoundingMode.HALF_UP);

        SgfensPedido pedidoDto = null;
        ComprobanteFiscal comprobanteFiscalDto = null;

        SGPedidoBO pedidoBO;
        ComprobanteFiscalBO comprobanteFiscalBO;
        try{
            if (idPedido>0){
                pedidoBO = new SGPedidoBO(idPedido,this.conn);
                pedidoDto = pedidoBO.getPedido();
                idCliente = pedidoDto.getIdCliente();
            }
            if (idComprobanteFiscal>0){
                comprobanteFiscalBO = new ComprobanteFiscalBO(idComprobanteFiscal, this.conn);
                comprobanteFiscalDto = comprobanteFiscalBO.getComprobanteFiscal();
                idCliente = comprobanteFiscalDto.getIdCliente();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        BigDecimal saldoAdeudo = BigDecimal.ZERO;
        BigDecimal saldoPagado = BigDecimal.ZERO;
        
        
        if (pedidoDto!=null){
            saldoAdeudo = getSaldoAdeudoPedido(pedidoDto.getIdPedido());
            saldoPagado = getSaldoPagadoPedido(pedidoDto.getIdPedido());
           
        }else if (comprobanteFiscalDto!=null){
            saldoAdeudo = getSaldoAdeudoComprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal());
            saldoPagado = getSaldoPagadoComprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal());
        }
        
        double saldoAdeudadoTotal = 0;
        if (pedidoDto!=null){
            if(pedidoDto.getBonificacionDevolucion()<0){
                saldoAdeudadoTotal = saldoAdeudo.doubleValue() + Math.abs(pedidoDto.getBonificacionDevolucion());
            }else{
                saldoAdeudadoTotal = saldoAdeudo.doubleValue();
            }
         }else if (comprobanteFiscalDto!=null){
             saldoAdeudadoTotal = saldoAdeudo.doubleValue();
         }
        
        //Si el metodo de pago es diferente a devolucion  de EFE          
        if(idMetodoPago != VentaMetodoPagoBO.METODO_PAGO_DEVOLUCION_EFE){
        
            if ( monto <= saldoAdeudadoTotal ){
                SgfensCobranzaAbono cobranzaAbonoDto = new SgfensCobranzaAbono();
                cobranzaAbonoDto.setSincronizacionMicrosip(sincronizacionMicrosip);///////
                cobranzaAbonoDto.setIdEmpresa(usuarioDto.getIdEmpresa());
                if (pedidoDto!=null)
                    cobranzaAbonoDto.setIdPedido(idPedido);
                if (comprobanteFiscalDto!=null)
                    cobranzaAbonoDto.setIdComprobanteFiscal(idComprobanteFiscal);
                cobranzaAbonoDto.setIdUsuarioVendedor(usuarioDto.getIdUsuarios());
                cobranzaAbonoDto.setIdCliente(idCliente);
                cobranzaAbonoDto.setFechaAbono(fechaCobranza!=null?fechaCobranza:new Date());
                cobranzaAbonoDto.setMontoAbono(monto);
                cobranzaAbonoDto.setIdCobranzaMetodoPago(idMetodoPago);
                cobranzaAbonoDto.setIdentificadorOperacion(identificadorOperacion);
                cobranzaAbonoDto.setComentarios(comentarios);
                cobranzaAbonoDto.setIdEstatus(1);//Activo
                if (idOperacionBancaria>0){
                    cobranzaAbonoDto.setIdOperacionBancaria(idOperacionBancaria);
                    String identificadorOperacionTDC;
                    try{
                        BancoOperacionBO bancoOperacionBO = new BancoOperacionBO(idOperacionBancaria, this.conn);
                        if (bancoOperacionBO.getBancoOperacion()!=null){
                            identificadorOperacionTDC = "Tarjeta:" + bancoOperacionBO.getPrintableCardNumber() + "."
                                    + " Aut.: " + bancoOperacionBO.getBancoOperacion().getBancoAuth();
                            cobranzaAbonoDto.setIdentificadorOperacion(identificadorOperacionTDC);
                        }else{
                            System.out.println("No se encontro información de Banco Operación con ID: " + idOperacionBancaria);
                        }
                    }catch(Exception ex){
                        System.out.println("No se pudo recuperar información de Banco Operación con ID: " + idOperacionBancaria);
                        ex.printStackTrace();
                    }

                }else{
                    cobranzaAbonoDto.setIdOperacionBancariaNull(true);
                }

                cobranzaAbonoDto.setLatitud(latitud);
                cobranzaAbonoDto.setLongitud(longitud);
                if (archivoImagenFirma!=null)
                    cobranzaAbonoDto.setNombreArchivoImgFirma(archivoImagenFirma.getName());

                cobranzaAbonoDto.setFolioCobranzaMovil(folioCobranzaMovil);
                cobranzaAbonoDto.setReferencia(referencia);
                cobranzaAbonoDto.setFechaActualizaReferencia(fechaReferencia);

                try{
                    idAbonoCobranza = new SgfensCobranzaAbonoDaoImpl(this.conn).insert(cobranzaAbonoDto).getIdCobranzaAbono();
                }catch(SgfensCobranzaAbonoDaoException ex){
                    ex.printStackTrace();
                    throw new  Exception("Error al insertar registro de Cobranza a BD: " + ex.toString());
                }

                //Si es pedido, actualizamos el registro con el campo SALDO_PAGADO
                if (pedidoDto!=null){
                    try{
                        BigDecimal nuevoSaldoPagado = saldoPagado.add(montoBigD).setScale(2, RoundingMode.HALF_UP);
                        pedidoDto.setSaldoPagado(nuevoSaldoPagado.doubleValue());
                        pedidoDto.setIsModificadoConsola(isConsola?(short)1:(short)0);

                        new SgfensPedidoDaoImpl(this.conn).update(pedidoDto.createPk(), pedidoDto);
                    }catch(SgfensPedidoDaoException ex){
                        ex.printStackTrace();
                    }
                }else if (comprobanteFiscalDto!=null){
                    //Si es factura, actualizamos el registro con SALDO_PAGADO
                    try{
                        BigDecimal nuevoSaldoPagado = saldoPagado.add(montoBigD).setScale(2, RoundingMode.HALF_UP);
                        comprobanteFiscalDto.setSaldoPagado(nuevoSaldoPagado.doubleValue());
                        //comprobanteFiscalDto.setIsModificadoConsola(isConsola?(short)1:(short)0);

                        new ComprobanteFiscalDaoImpl(this.conn).update(comprobanteFiscalDto.createPk(), comprobanteFiscalDto);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }

                //Registro de Creditos Operacion
                try{
                    BitacoraCreditosOperacionBO bcoBO = new BitacoraCreditosOperacionBO(getConn());
                    bcoBO.registraDescuento(usuarioDto, 
                            BitacoraCreditosOperacionBO.CONSUMO_ACCION_WS_REGISTRO_COBRANZA, 
                            null, idCliente, 0, monto, 
                            "Registro Cobranza", null, true);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                
                //Crear Notificacion SMS - solo en caso de Nuevo Abono
                try{
                    EmpresaPermisoAplicacion empresaPermisoAplicacion 
                        = new EmpresaPermisoAplicacionDaoImpl(conn).findByPrimaryKey(new EmpresaBO(conn).getEmpresaMatriz(cobranzaAbonoDto.getIdEmpresa()).getIdEmpresa()); 
                    if (empresaPermisoAplicacion!=null && empresaPermisoAplicacion.getAccesoModuloSms()==1){
                    // Solo si la empresa matriz tiene contratado el modulo SMS
                        SmsEnvioLoteBO smsEnvioLoteBO = new SmsEnvioLoteBO(conn);
                        smsEnvioLoteBO.crearSmsNotificacionSistema(SmsEnvioLoteBO.TipoNotificaSistema.ABONO_NUEVO, cobranzaAbonoDto.getIdEmpresa(), cobranzaAbonoDto.getIdCobranzaAbono());
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }            

                return idAbonoCobranza;
            }else{
                throw new  ArithmeticException("<ul>El monto del abono no puede exceder al saldo restante de la deuda.");
            }
        
        }else if(idMetodoPago == VentaMetodoPagoBO.METODO_PAGO_DEVOLUCION_EFE ){
            
            if ( monto <= pedidoDto.getTotal()){
                SgfensCobranzaAbono cobranzaAbonoDto = new SgfensCobranzaAbono();
                cobranzaAbonoDto.setSincronizacionMicrosip(1);///////No deseamos sincronizar estos abonos , ya que son negativos
                cobranzaAbonoDto.setIdEmpresa(usuarioDto.getIdEmpresa());
                if (pedidoDto!=null)
                    cobranzaAbonoDto.setIdPedido(idPedido);
                if (comprobanteFiscalDto!=null)
                    cobranzaAbonoDto.setIdComprobanteFiscal(idComprobanteFiscal);
                cobranzaAbonoDto.setIdUsuarioVendedor(usuarioDto.getIdUsuarios());
                cobranzaAbonoDto.setIdCliente(idCliente);
                cobranzaAbonoDto.setFechaAbono(fechaCobranza!=null?fechaCobranza:new Date());
                cobranzaAbonoDto.setMontoAbono(monto);
                cobranzaAbonoDto.setIdCobranzaMetodoPago(idMetodoPago);
                cobranzaAbonoDto.setIdentificadorOperacion(identificadorOperacion);
                cobranzaAbonoDto.setComentarios(comentarios);
                cobranzaAbonoDto.setIdEstatus(1);//Activo
                if (idOperacionBancaria>0){
                    cobranzaAbonoDto.setIdOperacionBancaria(idOperacionBancaria);
                    String identificadorOperacionTDC;
                    try{
                        BancoOperacionBO bancoOperacionBO = new BancoOperacionBO(idOperacionBancaria, this.conn);
                        if (bancoOperacionBO.getBancoOperacion()!=null){
                            identificadorOperacionTDC = "Tarjeta:" + bancoOperacionBO.getPrintableCardNumber() + "."
                                    + " Aut.: " + bancoOperacionBO.getBancoOperacion().getBancoAuth();
                            cobranzaAbonoDto.setIdentificadorOperacion(identificadorOperacionTDC);
                        }else{
                            System.out.println("No se encontro información de Banco Operación con ID: " + idOperacionBancaria);
                        }
                    }catch(Exception ex){
                        System.out.println("No se pudo recuperar información de Banco Operación con ID: " + idOperacionBancaria);
                        ex.printStackTrace();
                    }

                }else{
                    cobranzaAbonoDto.setIdOperacionBancariaNull(true);
                }

                cobranzaAbonoDto.setLatitud(latitud);
                cobranzaAbonoDto.setLongitud(longitud);
                if (archivoImagenFirma!=null)
                    cobranzaAbonoDto.setNombreArchivoImgFirma(archivoImagenFirma.getName());

                cobranzaAbonoDto.setFolioCobranzaMovil(folioCobranzaMovil);
                cobranzaAbonoDto.setReferencia(referencia);
                cobranzaAbonoDto.setFechaActualizaReferencia(fechaReferencia);

                try{
                    idAbonoCobranza = new SgfensCobranzaAbonoDaoImpl(this.conn).insert(cobranzaAbonoDto).getIdCobranzaAbono();
                }catch(SgfensCobranzaAbonoDaoException ex){
                    ex.printStackTrace();
                    throw new  Exception("Error al insertar registro de Cobranza a BD: " + ex.toString());
                }
                
                //Registro de Creditos Operacion
                try{
                    BitacoraCreditosOperacionBO bcoBO = new BitacoraCreditosOperacionBO(getConn());
                    bcoBO.registraDescuento(usuarioDto, 
                            BitacoraCreditosOperacionBO.CONSUMO_ACCION_WS_REGISTRO_COBRANZA, 
                            null, idCliente, 0, monto, 
                            "Registro Cobranza", null, true);
                }catch(Exception ex){
                    ex.printStackTrace();
                }

                return idAbonoCobranza;
            }else{
                throw new  ArithmeticException("<ul>El monto del abono no puede exceder al Monto Total de la venta.");
            }
            
        }else{
            throw new  ArithmeticException("<ul>Error al intentar Sincronizar Abono. Método de pago incorrecto.");         
        }
        
     }

     public boolean cancelarAbono(int idCobranzaAbono, int idOperacionBancaria, String newDateOperacionBancaria) throws Exception{
         boolean exito = false;
         
         
         //Registramos cancelación de Banco Operación
        if (idOperacionBancaria>0){
            BancoOperacionBO bancoOperacionBO = new BancoOperacionBO(idOperacionBancaria, this.conn);
            if (bancoOperacionBO.getBancoOperacion()!=null){
                
                bancoOperacionBO.getBancoOperacion().setIdEstatus(2);// Cancelada/Inactiva
                bancoOperacionBO.getBancoOperacion().setBancoOperFecha(newDateOperacionBancaria);
                
                new BancoOperacionDaoImpl(this.conn).update(bancoOperacionBO.getBancoOperacion().createPk(), bancoOperacionBO.getBancoOperacion());
                
            }else{
                throw new Exception("El ID de Operacion Bancaria indicado, no existe en el servidor. Verifique que previamente el pago haya sido registrado.");
            }
            
        }
        
         //Registramos cancelación de Cobranza Abono
         if (idCobranzaAbono>0){
            SGCobranzaAbonoBO sGCobranzaAbonoBO = new SGCobranzaAbonoBO(idCobranzaAbono,this.conn);
            if (sGCobranzaAbonoBO.getCobranzaAbono()!=null){

                //Restamos de Pedido o Comprobante Fiscal
                int idPedido = sGCobranzaAbonoBO.getCobranzaAbono().getIdPedido();
                int idComprobanteFiscal = sGCobranzaAbonoBO.getCobranzaAbono().getIdComprobanteFiscal();
                BigDecimal montoAbonoRestar = new BigDecimal(sGCobranzaAbonoBO.getCobranzaAbono().getMontoAbono());
                if (idPedido>0){
                    //Corresponde a Pedido
                    BigDecimal pagadoActual = this.getSaldoPagadoPedido(idPedido);
                    BigDecimal nuevoSaldo = pagadoActual.subtract(montoAbonoRestar);

                    if (nuevoSaldo.doubleValue()<0)
                        nuevoSaldo = BigDecimal.ZERO;

                    SGPedidoBO pedidoBO = new SGPedidoBO(idPedido,this.conn);
                    if (pedidoBO.getPedido()!=null){

                        pedidoBO.getPedido().setSaldoPagado(nuevoSaldo.doubleValue());

                        new SgfensPedidoDaoImpl(this.conn).update(pedidoBO.getPedido().createPk(), pedidoBO.getPedido());
                    }else{
                        throw new Exception("El Pedido asociado al abono (" + idPedido + "), no existe, por lo tanto no se puede actualizar información con integridad.");
                    }

                }else if (idComprobanteFiscal>0){
                    //Corresponde a un Comprobante Fiscal

                    BigDecimal pagadoActual = this.getSaldoPagadoComprobanteFiscal(idComprobanteFiscal);
                    BigDecimal nuevoSaldo = pagadoActual.subtract(montoAbonoRestar);

                    if (nuevoSaldo.doubleValue()<0)
                        nuevoSaldo = BigDecimal.ZERO;

                    ComprobanteFiscalBO comprobanteFiscalBO = new ComprobanteFiscalBO(idComprobanteFiscal, this.conn);
                    if (comprobanteFiscalBO.getComprobanteFiscal()!=null){
                        //Comprobante Fiscal no tiene dato de Saldo Pagado, por lo cual no es necesario actualizarlo a menos que se agregue
                        comprobanteFiscalBO.getComprobanteFiscal().setSaldoPagado(nuevoSaldo.doubleValue());
                        new ComprobanteFiscalDaoImpl(this.conn).update(comprobanteFiscalBO.getComprobanteFiscal().createPk(), comprobanteFiscalBO.getComprobanteFiscal());
                    }else{
                        throw new Exception("El Comprobante Fiscal asociado al abono (" + idPedido + "), no existe, por lo tanto no se puede actualizar información con integridad.");
                    }

                }

                sGCobranzaAbonoBO.getCobranzaAbono().setIdEstatus(2); // Cancelado/Inactivo
                sGCobranzaAbonoBO.getCobranzaAbono().setSincronizacionMicrosip(2); // modificado (para sincronizacion microsip)
                SgfensCobranzaAbonoDaoImpl cobranzaAbonoDao = new SgfensCobranzaAbonoDaoImpl(this.conn);
                
                //Actualizamos registro de abono
                cobranzaAbonoDao.update(sGCobranzaAbonoBO.getCobranzaAbono().createPk(), sGCobranzaAbonoBO.getCobranzaAbono());

            }else{
                throw new Exception("El ID de Cobranza Abono indicado (" + idCobranzaAbono + "), no existe en el servidor. Verifique que previamente el abono haya sido registrado.");
            }
        }
         
        return exito;
     }

     
     public File getFileEstadoCuentaPDF(UsuarioBO user) throws Exception{
         File fileEstadoCuentaPDF = null;
         
         if (this.cobranzaAbono==null){
             throw new Exception("Valor de objeto CobranzaAbono nulo");
         }else{
            Configuration appConfig = new Configuration();
            ByteArrayOutputStream baos;

            Empresa empresaDto = new EmpresaBO(this.cobranzaAbono.getIdEmpresa(), this.conn).getEmpresa();
            Cliente clienteDto = new ClienteBO(this.cobranzaAbono.getIdCliente(), this.conn).getCliente();
            
            baos = this.toPdf(user, this.cobranzaAbono.getIdPedido(), this.cobranzaAbono.getIdComprobanteFiscal());

            String rutaRepositorio = appConfig.getApp_content_path() + empresaDto.getRfc() + "/pedidos"
                                    +"/" + clienteDto.getRfcCliente()+ "/temp";
            String nombreArchivo = "EdoCuenta_" + clienteDto.getRfcCliente() + "_" + (new Date()).getTime() + ".pdf";
            
            File directorio = new File(rutaRepositorio);
            if (!directorio.exists())
                directorio.mkdirs();

            //Guardamos archivo PDF
            fileEstadoCuentaPDF = new File(rutaRepositorio + "/" + nombreArchivo);
            FileOutputStream fos = new FileOutputStream(fileEstadoCuentaPDF);
            baos.writeTo(fos);
            fos.flush();
            fos.close();

            if (!fileEstadoCuentaPDF.exists()){
                throw new Exception("El archivo " + fileEstadoCuentaPDF.getAbsolutePath() + " no existe en el repositorio del sistema.");
            }
         }      
         
         return fileEstadoCuentaPDF;
     }
     
     
     /**
     * Envia un correo a los destinatarios específicados
     * con el estado de cuenta
     * @return Cadena en caso de error
     */
    public String enviaNotificacionEstadoCuenta(ArrayList<String> destinatarios, File[] adjuntos,int idEmpresa){
        
        EmpresaPermisoAplicacion empresaPermisoDto = null;
        try{
            EmpresaPermisoAplicacionDaoImpl  empresaPermisoDao = new EmpresaPermisoAplicacionDaoImpl();
            empresaPermisoDto = empresaPermisoDao.findWhereIdEmpresaEquals(idEmpresa)[0];
        }catch(Exception e){e.printStackTrace();}
        
        String respuesta ="";
        
        if (this.cobranzaAbono!=null){
            try {
                TspMailBO mailBO = new TspMailBO();
                
                
                if(empresaPermisoDto.getAccesoSgfensPretomovil()==1){
                    mailBO.setConfigurationMovilpyme();
                    mailBO.addMessageMovilpyme("<b>Buen día!</b> "
                        + "<br/> El presente, contiene adjunto el Estado de Cuenta solicitado. ",1);
                } else{
                    mailBO.setConfiguration();            
                    mailBO.addMessage("<b>Buen día!</b> "
                        + "<br/> El presente, contiene adjunto el Estado de Cuenta solicitado. ",1);
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

                
                
                //Agregar PDF de cotizacion y otros adjuntos
                for (File item:adjuntos){
                    if (item!=null)
                        mailBO.addFile(item.getAbsolutePath(), item.getName());
                }
                
                mailBO.send("Estado de Cuenta de Pedido o Factura");
                
                respuesta += "Correo enviado exitosamente.";
            } catch (Exception ex) {
                respuesta += "Ocurrio un error al intentar enviar el correo: " + ex.toString();
            }            
        }
        
        return respuesta;
    }
}
