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
import com.tsp.sct.dao.exceptions.SgfensPedidoImpuestoDaoException;
import com.tsp.sct.dao.exceptions.SgfensPedidoProductoDaoException;
import com.tsp.sct.dao.exceptions.SgfensPedidoServicioDaoException;
import com.tsp.sct.dao.jdbc.*;
import com.tsp.sct.mail.TspMailBO;
import com.tsp.sct.pdf.PdfITextUtil;
import com.tsp.sct.pdf.Translator;
import com.tsp.sct.util.Base64;
import com.tsp.sct.util.Converter;
import com.tsp.sct.util.DateManage;
import com.tsp.sct.util.FileManage;
import com.tsp.sct.util.GenericValidator;
import com.tsp.sct.util.StringManage;
import com.tsp.sgfens.report.ReportBO;
import com.tsp.sgfens.report.pdf.EventPDF;
import com.tsp.sgfens.sesion.*;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author ISCesarMartinez
 */
public class SGPedidoBO {
    
    private SgfensPedido pedido = null;
    
    private boolean ordenPorRetrasoPagoCxC = false;

    public SgfensPedido getPedido() {
        return pedido;
    }

    public void setPedido(SgfensPedido pedido) {
        this.pedido = pedido;
    }
    
    private Connection conn = null;
    
    public SGPedidoBO(Connection conn) {
        this.conn = conn;
    }

    public boolean isOrdenPorRetrasoPagoCxC() {
        return ordenPorRetrasoPagoCxC;
    }

    public void setOrdenPorRetrasoPagoCxC(boolean ordenPorRetrasoPagoCxC) {
        this.ordenPorRetrasoPagoCxC = ordenPorRetrasoPagoCxC;
    }

    public SGPedidoBO(long idPedido, Connection conn) {
        this.conn = conn;
        try{
            this.pedido = new SgfensPedidoDaoImpl(this.conn).findByPrimaryKey((int)idPedido);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public SGPedidoBO(int idPedido, Connection conn) {
        this.conn = conn;
        try{
            this.pedido = new SgfensPedidoDaoImpl(this.conn).findByPrimaryKey(idPedido);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public SGPedidoBO(SgfensPedido pedido, Connection conn) {
        this.pedido = pedido;
        this.conn = conn;
    }
    
    public SgfensPedido findPedidobyId(int idPedido) throws Exception{
        SgfensPedido sgfensPedido = null;
        
        try{
            sgfensPedido = new SgfensPedidoDaoImpl(this.conn).findByPrimaryKey(idPedido);
            if (sgfensPedido==null){
                throw new Exception("No se encontro ningun pedido que corresponda según los parámetros específicados.");
            }
            if (sgfensPedido.getIdPedido()<=0){
                throw new Exception("No se encontro ningun pedido que corresponda según los parámetros específicados.");
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Pedido. Error: " + e.toString());
        }
        
        return sgfensPedido;
    }
    
    /**
     * Realiza una búsqueda por ID SgfensPedido en busca de
     * coincidencias
     * @param idSgfensPedido ID De la pedido para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar pedido, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensPedido
     */
    public SgfensPedido[] findPedido(int idSgfensPedido, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SgfensPedido[] pedidoDto = new SgfensPedido[0];
        SgfensPedidoDaoImpl pedidoDao = new SgfensPedidoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSgfensPedido>0){
                sqlFiltro ="ID_PEDIDO=" + idSgfensPedido + " AND ";
            }else{
                sqlFiltro ="ID_PEDIDO>0 AND ";
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
            
            String orderBy = " ORDER BY FECHA_PEDIDO DESC ";
            if (ordenPorRetrasoPagoCxC)
                orderBy = " ORDER BY DATEDIFF(FECHA_TENTATIVA_PAGO,NOW()) ASC ";
            
            pedidoDto = pedidoDao.findByDynamicWhere( 
                    sqlFiltro
                    + orderBy
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return pedidoDto;
    }
    
    /**
     *Busca la cantidad de coincidencias de una búsqueda por ID SgfensPedido y otros filtos
     * @param idSgfensPedido ID De la pedido para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar pedido, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensPedido
     */
    public int findCantidadPedido(int idSgfensPedido, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        try {
            String sqlFiltro="";
            if (idSgfensPedido>0){
                sqlFiltro ="ID_PEDIDO=" + idSgfensPedido + " AND ";
            }else{
                sqlFiltro ="ID_PEDIDO>0 AND ";
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
            
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(ID_PEDIDO) as cantidad FROM SGFENS_PEDIDO WHERE " + 
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
    
    public String findGroupValorUnicoPedido(int idSgfensPedido, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda,
                    String selectSql) {
        String valor = "";
        try {
            String sqlFiltro="";
            if (idSgfensPedido>0){
                sqlFiltro ="ID_PEDIDO=" + idSgfensPedido + " AND ";
            }else{
                sqlFiltro ="ID_PEDIDO>0 AND ";
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
            
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + selectSql + " FROM SGFENS_PEDIDO WHERE " + 
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
     * Recupera todos los datos de una pedido desde la base de datos
     * y lo convierte a un objeto legible en sesion.
     * @return objeto PedidoSesion
     */
    public PedidoSesion getSessionFromPedidoDB(){
        PedidoSesion pedidoSesion = null;
        
        try{
            Cliente clienteDto = null;
            ArrayList<ProductoSesion> listaProductoDto = new ArrayList<ProductoSesion>();
            ArrayList<ServicioSesion> listaServicioDto = new ArrayList<ServicioSesion>();
            ArrayList<ImpuestoSesion> listaImpuestoDto = new ArrayList<ImpuestoSesion>();
            
            /*recuperamos deatos de cliente / prospecto */
            clienteDto = new ClienteBO(this.pedido.getIdCliente(), this.conn).getCliente();
            
            
            /* Recuperamos listado de productos */
            SgfensPedidoProducto[] listaProductosCompra = getProductos_Datos();
            ProductoSesion productoSesion = null;
            for (SgfensPedidoProducto itemProducto:listaProductosCompra){
                try{
                    productoSesion = new ProductoSesion();
                    productoSesion.setCantidad(itemProducto.getCantidad());
                    productoSesion.setIdProducto(itemProducto.getIdConcepto());
                    productoSesion.setPrecio(itemProducto.getPrecioUnitario());
                    productoSesion.setMonto(itemProducto.getSubtotal());
                    productoSesion.setUnidad(itemProducto.getUnidad());
                    if (itemProducto.getDescripcion()!=null){
                        if (itemProducto.getDescripcion().trim().length()>0)
                            productoSesion.setDescripcionAlternativa(itemProducto.getDescripcion());
                    }
                    productoSesion.setCantidadEntregada(itemProducto.getCantidadEntregada());
                    productoSesion.setFechaEntrega(itemProducto.getFechaEntrega());
                    productoSesion.setEstatus(itemProducto.getEstatus());
                    productoSesion.setIdAlmacen(itemProducto.getIdAlmacenOrigen());
                    productoSesion.setCantidadPeso(itemProducto.getCantidadPeso());
                    productoSesion.setCantidadEntregadaPeso(itemProducto.getCantidadEntregadaPeso());
                    //productoSesion.setIdInventarioEmpleado(itemProducto.getIdInventarioEmpleado());
                    
                    listaProductoDto.add(productoSesion);
                }catch(Exception ex){ex.printStackTrace();}
            }
            
            /* Recuperamos listado de servicios */
            SgfensPedidoServicio[] listaServiciosPedido = getServicios_Datos();
            ServicioSesion servicioSesion = null;
            for (SgfensPedidoServicio itemServicio : listaServiciosPedido){
                try{
                    servicioSesion = new ServicioSesion();
                    servicioSesion.setCantidad(itemServicio.getCantidad());
                    servicioSesion.setIdServicio(itemServicio.getIdServicio());
                    servicioSesion.setPrecio(itemServicio.getPrecioUnitario());
                    servicioSesion.setMonto(itemServicio.getSubtotal());
                    servicioSesion.setUnidad(itemServicio.getUnidad());

                    listaServicioDto.add(servicioSesion);
                }catch(Exception ex){ex.printStackTrace();}
            }
            
            /* Recuperamos listado de impuestos */
            SgfensPedidoImpuesto[] listaImpuestosPedido = getImpuestos_Datos();
            ImpuestoSesion impuestoSesion = null;
            Impuesto impuestoDto = null;
            for (SgfensPedidoImpuesto itemImpuesto : listaImpuestosPedido){
                try{
                    impuestoDto = new ImpuestoBO(itemImpuesto.getIdImpuesto(), this. conn).getImpuesto();
                    
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
            
            //Asignamos valores a variable que se dispondrá en sesion
            pedidoSesion = new PedidoSesion();
            pedidoSesion.setIdPedido(this.pedido.getIdPedido());
            pedidoSesion.setComentarios(this.pedido.getComentarios());
            pedidoSesion.setDescuento_tasa(this.pedido.getDescuentoTasa());
            pedidoSesion.setDescuento_motivo(this.pedido.getDescuentoMotivo());
            pedidoSesion.setCliente(clienteDto);
            pedidoSesion.setListaProducto(listaProductoDto);
            pedidoSesion.setListaServicio(listaServicioDto);
            pedidoSesion.setListaImpuesto(listaImpuestoDto);
            
            pedidoSesion.setAdelanto(this.pedido.getAdelanto());
            pedidoSesion.setIdComprobanteFiscal(this.pedido.getIdComprobanteFiscal());
            pedidoSesion.setSaldoPagado(this.pedido.getSaldoPagado());
            pedidoSesion.setFechaEntrega(this.pedido.getFechaEntrega());
            pedidoSesion.setFechaTentativaPago(this.pedido.getFechaTentativaPago());
            pedidoSesion.setIdEstatus(this.pedido.getIdEstatusPedido());
            
        }catch(Exception ex){
            System.out.println("Ha ocurrido un error al intentar generar un "
                    + "objeto de sesion a partir de datos de Base de Datos de "
                    + "una pedido: " + ex.toString());
            ex.printStackTrace();
        }
        
        return pedidoSesion;
    }
    
    public SgfensPedidoProducto[] getProductos_Datos(){
        SgfensPedidoProducto[] prods = new SgfensPedidoProducto[0];
        try{
            prods = new SgfensPedidoProductoDaoImpl(this.conn).findWhereIdPedidoEquals(this.pedido.getIdPedido());
        }catch(Exception ex){
            System.out.println("Ocurrio un error al consultar de BD los productos de una Pedido." + ex.toString());
            ex.printStackTrace();
        }
        return prods;
    }
    
    public SgfensPedidoServicio[] getServicios_Datos(){
        SgfensPedidoServicio[] servicios = new SgfensPedidoServicio[0];
        try{
            servicios = new SgfensPedidoServicioDaoImpl(this.conn).findWhereIdPedidoEquals(this.pedido.getIdPedido());
        }catch(Exception ex){
            System.out.println("Ocurrio un error al consultar de BD los servicios de una Pedido." + ex.toString());
            ex.printStackTrace();
        }
        return servicios;
    }
    
    public SgfensPedidoImpuesto[] getImpuestos_Datos(){
        SgfensPedidoImpuesto[] impuestos = new SgfensPedidoImpuesto[0];
        try{
            impuestos = new SgfensPedidoImpuestoDaoImpl(this.conn).findWhereIdPedidoEquals(this.pedido.getIdPedido());
        }catch(Exception ex){
            System.out.println("Ocurrio un error al consultar de BD los impuestos de una Pedido." + ex.toString());
            ex.printStackTrace();
        }
        return impuestos;
    }
    
    /**
     * Crea o Actualiza un Pedido en base de datos a partir de datos de Sesion
     * @param pedidoSesion Pedido en sesion a insertar o modificar
     * @param user Usuario en sesion que creó o modificó el pedido
     * @param fechaPedido Fecha de creación del Pedido (solo aplica para creación)
     * @param isConsola boolean para indicar si la captura fue en consola, false en caso de captura desde movil
     * @return
     * @throws Exception 
     */
    
    public int idViaEmbarquePedido = 0;
    
    public SgfensPedido creaActualizaPedido(PedidoSesion pedidoSesion, UsuarioBO user, Date fechaPedido, boolean isConsola) throws Exception{
        SgfensPedido pedidoDto = null;
        SgfensPedidoDaoImpl pedidoDao = new SgfensPedidoDaoImpl(this.conn);
        SgfensPedidoPk pedidoPk = null;
        
        boolean edicion = false;
        if (pedidoSesion.getIdPedido()<=0){
        //Crear
            
            //Creamos registro principal de Solicitud de Compra
            pedidoDto = new SgfensPedido();
            pedidoDto.setComentarios(pedidoSesion.getComentarios());
            //pedidoDto.setConsecutivoPedido(0);
            pedidoDto.setDescuentoMonto(pedidoSesion.getDescuentoImporte().doubleValue());
            pedidoDto.setDescuentoMotivo(pedidoSesion.getDescuento_motivo());
            pedidoDto.setDescuentoTasa(pedidoSesion.getDescuento_tasa());
            pedidoDto.setFechaPedido(fechaPedido!=null? fechaPedido : new Date());
            //pedidoDto.setFolioPedido("");
            pedidoDto.setIdCliente(pedidoSesion.getCliente()!=null?pedidoSesion.getCliente().getIdCliente():0);
            pedidoDto.setIdEmpresa(user.getUser().getIdEmpresa());
            if(pedidoSesion.getIdCotizacionOrigen()<=0){
                pedidoDto.setIdUsuarioVendedor(user.getUser().getIdUsuarios());
            }else{
                pedidoDto.setIdUsuarioVendedor(pedidoSesion.getIdUsuarioVendedor());
            }
            pedidoDto.setIdUsuarioVendedorAsignado(user.getUser().getIdUsuarios());
            /*if(user.getUser().getIdRoles() == RolesBO.ROL_CONDUCTOR_VENDEDOR){
                pedidoDto.setIdUsuarioConductorAsignado(user.getUser().getIdUsuarios());
            }*/
            pedidoDto.setSubtotal(pedidoSesion.calculaSubTotal().doubleValue());
            pedidoDto.setTiempoEntregaDias(0);
            pedidoDto.setTipoMoneda(pedidoSesion.getTipo_moneda());
            pedidoDto.setTotal(pedidoSesion.calculaTotal().doubleValue());
            pedidoDto.setNombreImagenFirma(pedidoSesion.getRutaImagen());            
            
            //Buscamos el último consecutivo de pedido para la empresa y aumentamos 1
            int consecutivoPedidoEmpresa=1;
            try{
                SgfensPedido lastPedidoConsec = pedidoDao.findByDynamicWhere("ID_EMPRESA=" + user.getUser().getIdEmpresa() + " ORDER BY CONSECUTIVO_PEDIDO DESC LIMIT 0,1 ", null)[0];
                consecutivoPedidoEmpresa = lastPedidoConsec.getConsecutivoPedido() + 1;
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
            pedidoDto.setConsecutivoPedido(consecutivoPedidoEmpresa);
            pedidoDto.setFolioPedido("P" + consecutivoPedidoEmpresa);
            
            pedidoDto.setFechaEntrega(pedidoSesion.getFechaEntrega());
            pedidoDto.setFechaTentativaPago(pedidoSesion.getFechaTentativaPago());
            pedidoDto.setIdComprobanteFiscal(pedidoSesion.getIdComprobanteFiscal());
            pedidoDto.setSaldoPagado(pedidoSesion.getSaldoPagado());
            pedidoDto.setAdelanto(pedidoSesion.getAdelanto());
            pedidoDto.setIdEstatusPedido((short)pedidoSesion.getIdEstatus());
            
            pedidoDto.setLatitud(pedidoSesion.getLatitud());
            pedidoDto.setLongitud(pedidoSesion.getLongitud());
            
            pedidoDto.setFolioPedidoMovil(pedidoSesion.getFolioPedidoMovil());
            
            pedidoDto.setIsModificadoConsola(isConsola?(short)1:(short)0);
            
            pedidoDto.setConsigna(pedidoSesion.getConsigna());
            
            pedidoDto.setIdViaEmbarque(idViaEmbarquePedido);
            
            try{
                pedidoPk = pedidoDao.insert(pedidoDto);
            }catch(Exception ex){
                ex.printStackTrace();
                throw new Exception("Error al crear registro principal de Pedido. " + ex.toString());
            }
            
        }else{
        //Actualizar
            try{
                pedidoDto = pedidoDao.findByPrimaryKey(pedidoSesion.getIdPedido());
                pedidoDto.setComentarios(pedidoSesion.getComentarios());
                //pedidoDto.setConsecutivoPedido(0);
                pedidoDto.setDescuentoMonto(pedidoSesion.getDescuentoImporte().doubleValue());
                pedidoDto.setDescuentoMotivo(pedidoSesion.getDescuento_motivo());
                pedidoDto.setDescuentoTasa(pedidoSesion.getDescuento_tasa());
                //pedidoDto.setFechaPedido(new Date());
                //pedidoDto.setFolioPedido("");
                pedidoDto.setIdCliente(pedidoSesion.getCliente()!=null?pedidoSesion.getCliente().getIdCliente():0);
                pedidoDto.setIdEmpresa(user.getUser().getIdEmpresa());
                //pedidoDto.setIdUsuarioVendedor(user.getUser().getIdUsuarios());
                pedidoDto.setSubtotal(pedidoSesion.calculaSubTotal().doubleValue());
                pedidoDto.setTiempoEntregaDias(0);
                pedidoDto.setTipoMoneda(pedidoSesion.getTipo_moneda());
                pedidoDto.setTotal(pedidoSesion.calculaTotal().doubleValue());
                
                pedidoDto.setFechaEntrega(pedidoSesion.getFechaEntrega());
                pedidoDto.setFechaTentativaPago(pedidoSesion.getFechaTentativaPago());
                pedidoDto.setIdComprobanteFiscal(pedidoSesion.getIdComprobanteFiscal());
                pedidoDto.setSaldoPagado(pedidoSesion.getSaldoPagado());
                pedidoDto.setAdelanto(pedidoSesion.getAdelanto());
                pedidoDto.setIdEstatusPedido((short)pedidoSesion.getIdEstatus());
                
                pedidoDto.setIsModificadoConsola(isConsola?(short)1:(short)0);
                
                pedidoDto.setIdViaEmbarque(idViaEmbarquePedido);
                
                pedidoDao.update(pedidoDto.createPk(), pedidoDto);
                edicion = true;
                
                //Eliminamos de bd los elementos borrados en sesion, para que la información sea integra
                eliminaItemsPedido(pedidoSesion);
                
            }catch(Exception ex){
                ex.printStackTrace();
                throw new Exception("Error al actualizar registro principal de Pedido. " + ex.toString());
            }
        }
        
        //Eliminamos de la BD todos los elementos que correspondan a la Pedido para insertar los mas actuales
        
        //Guardamos Productos Seleccionados
        for (ProductoSesion prodSesion:pedidoSesion.getListaProducto()){
            SgfensPedidoProducto prodPedido = new SgfensPedidoProducto();
            
            try{
                Concepto conceptoDto = new ConceptoBO(prodSesion.getIdProducto(), this.conn).getConcepto();
                
                prodPedido.setIdPedido(pedidoDto.getIdPedido());

                prodPedido.setCantidad(prodSesion.getCantidad());
                prodPedido.setIdConcepto(prodSesion.getIdProducto());
                prodPedido.setDescripcion(conceptoDto.getDescripcion());
                if (prodSesion.getDescripcionAlternativa()!=null){
                    if (prodSesion.getDescripcionAlternativa().trim().length()>0)
                        prodPedido.setDescripcion(prodSesion.getDescripcionAlternativa());
                }
                prodPedido.setDescuentoMonto(0);
                prodPedido.setDescuentoPorcentaje(0);
                prodPedido.setIdentificacion(conceptoDto.getIdentificacion());
                prodPedido.setPrecioUnitario(prodSesion.getPrecio());
                prodPedido.setSubtotal(prodSesion.getMonto());
                prodPedido.setUnidad(prodSesion.getUnidad());                
                prodPedido.setCostoUnitario(conceptoDto.getPrecioCompra());
                try{//Obtenemos tasa de comisión de empleado
                    EmpleadoBO empleadoBO = new EmpleadoBO(this.conn);
                    Empleado empleadoDto = empleadoBO.findEmpleadoByUsuario(user.getUser().getIdUsuarios());
                    prodPedido.setPorcentajeComisionEmpleado(empleadoDto.getPorcentajeComision());
                }catch(Exception e){
                    prodPedido.setPorcentajeComisionEmpleado(0);
                    System.out.println("Usuario, no tiene acceso como Empleado");                                       
                }
                prodPedido.setCantidadEntregada(prodSesion.getCantidadEntregada());
                prodPedido.setFechaEntrega(prodSesion.getFechaEntrega());
                prodPedido.setEstatus((short) prodSesion.getEstatus());
                prodPedido.setIdAlmacenOrigen(prodSesion.getIdAlmacen());
                
                if (prodSesion.getCantidadPeso()>0){
                //Datos para Granel
                    EmpleadoInventarioRepartidor empleadoInventarioRepartidorDto = new EmpleadoInventarioRepartidorBO( prodSesion.getIdInventarioEmpleado(), this.conn).getEmpleadoInventarioRepartidor();
                    if (empleadoInventarioRepartidorDto!=null)
                        prodPedido.setPesoUnitario(empleadoInventarioRepartidorDto.getPeso());
                    prodPedido.setCantidadPeso(prodSesion.getCantidadPeso());
                    prodPedido.setCantidadEntregadaPeso(prodSesion.getCantidadEntregadaPeso());
                    prodPedido.setPrecioUnitarioGranel(prodSesion.getPrecio());//El precio a Granel al que fue vendido
                    //prodPedido.setPrecioUnitario(conceptoDto.getPrecio());//El precio Normal, fijado al producto cuando se creo
                }                

                new SgfensPedidoProductoDaoImpl(this.conn).insert(prodPedido);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
        //Guardamos Servicios Seleccionados
        for (ServicioSesion servicioSesion:pedidoSesion.getListaServicio()){
            SgfensPedidoServicio servicioPedido = new SgfensPedidoServicio();
            
            try{
                Servicio servicioDto = new ServicioBO(servicioSesion.getIdServicio(),this.conn).getServicio();
                
                
                servicioPedido.setIdPedido(pedidoDto.getIdPedido());

                servicioPedido.setCantidad(servicioSesion.getCantidad());
                servicioPedido.setIdServicio(servicioSesion.getIdServicio());
                servicioPedido.setDescripcion(servicioDto.getDescripcion());
                servicioPedido.setDescuentoMonto(0);
                servicioPedido.setDescuentoPorcentaje(0);
                servicioPedido.setIdentificacion(servicioDto.getSku());
                servicioPedido.setPrecioUnitario(servicioSesion.getPrecio());
                servicioPedido.setSubtotal(servicioSesion.getMonto());
                servicioPedido.setUnidad(servicioSesion.getUnidad());

                new SgfensPedidoServicioDaoImpl(this.conn).insert(servicioPedido);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
        //Guardamos Impuestos Seleccionados
        for (ImpuestoSesion impuestoSesion:pedidoSesion.getListaImpuesto()){
            SgfensPedidoImpuesto impuestoPedido = new SgfensPedidoImpuesto();
            
            try{              
                impuestoPedido.setIdPedido(pedidoDto.getIdPedido());
                impuestoPedido.setIdImpuesto(impuestoSesion.getIdImpuesto());

                new SgfensPedidoImpuestoDaoImpl(this.conn).insert(impuestoPedido);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
        //Consumo de Creditos Operacion
        if (!edicion){
            try{
                BitacoraCreditosOperacionBO bcoBO = new BitacoraCreditosOperacionBO(this.conn);
                bcoBO.registraDescuento(user.getUser(), 
                        BitacoraCreditosOperacionBO.CONSUMO_ACCION_WS_REGISTRO_PEDIDO_LEVANTAR, 
                        null, pedidoDto.getIdCliente(), 0, pedidoDto.getTotal(), 
                        "Registro Pedido", null, true);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
        //Crear Notificacion SMS - solo en caso de Nuevo Pedido
        if (!edicion){
            try{
                EmpresaPermisoAplicacion empresaPermisoAplicacion 
                    = new EmpresaPermisoAplicacionDaoImpl(conn).findByPrimaryKey(new EmpresaBO(conn).getEmpresaMatriz(pedidoDto.getIdEmpresa()).getIdEmpresa()); 
                if (empresaPermisoAplicacion!=null && empresaPermisoAplicacion.getAccesoModuloSms()==1){
                // Solo si la empresa matriz tiene contratado el modulo SMS
                    SmsEnvioLoteBO smsEnvioLoteBO = new SmsEnvioLoteBO(conn);
                    smsEnvioLoteBO.crearSmsNotificacionSistema(SmsEnvioLoteBO.TipoNotificaSistema.PEDIDO_NUEVO, pedidoDto.getIdEmpresa(), pedidoDto.getIdPedido());
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }            
        }
        
        return pedidoDto;
    }
    
    
    
    /**
     * Elimina de BD todos los items de una Solicitud de Compra que ya no existen
     * por que fueron eliminados en la sesion actual
     */
    public void eliminaItemsPedido(PedidoSesion pedidoSesion){
        if (pedidoSesion!=null){
            int idPedido = pedidoSesion.getIdPedido();
            
            if(idPedido>0){
                
                //Productos
                {
                    SgfensPedidoProducto[] pedidoProdArray = new SgfensPedidoProducto[0];
                    SgfensPedidoProductoDaoImpl pedidoProdDao = new SgfensPedidoProductoDaoImpl(this.conn);
                    try{
                        pedidoProdArray = pedidoProdDao.findWhereIdPedidoEquals(idPedido);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    for (SgfensPedidoProducto itemProductoBD:pedidoProdArray){
                        try {
                            pedidoProdDao.delete(itemProductoBD.createPk());
                        } catch (SgfensPedidoProductoDaoException ex) {
                            ex.printStackTrace();
                        }
                    }   
                }
            
                //Servicios
                {
                    SgfensPedidoServicio[] pedidoServicioArray = new SgfensPedidoServicio[0];
                    SgfensPedidoServicioDaoImpl pedidoServicioDao = new SgfensPedidoServicioDaoImpl(this.conn);
                    try{
                        pedidoServicioArray = pedidoServicioDao.findWhereIdPedidoEquals(idPedido);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    for (SgfensPedidoServicio itemServicioBD:pedidoServicioArray){
                        try {
                            pedidoServicioDao.delete(itemServicioBD.createPk());
                        } catch (SgfensPedidoServicioDaoException ex) {
                            ex.printStackTrace();
                        }
                    }   
                }
        
                //Impuestos
                {
                    SgfensPedidoImpuesto[] pedidoImpuestoArray = new SgfensPedidoImpuesto[0];
                    SgfensPedidoImpuestoDaoImpl pedidoImpuestoDao = new SgfensPedidoImpuestoDaoImpl(this.conn);
                    try{
                        pedidoImpuestoArray = pedidoImpuestoDao.findWhereIdPedidoEquals(idPedido);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    for (SgfensPedidoImpuesto itemImpuestoBD:pedidoImpuestoArray){
                        try {
                            pedidoImpuestoDao.delete(itemImpuestoBD.createPk());
                        } catch (SgfensPedidoImpuestoDaoException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                
            }
            
        }
    }
    
    
    /**
     * Envia un correo a los destinatarios específicados
     * con la pedido
     * @return Cadena en caso de error
     */
    public String enviaNotificacionNuevaPedido(ArrayList<String> destinatarios, File[] adjuntos,int idEmpresa){
        
        EmpresaPermisoAplicacion empresaPermisoDto = null;
        try{
            EmpresaPermisoAplicacionDaoImpl  empresaPermisoDao = new EmpresaPermisoAplicacionDaoImpl();
            empresaPermisoDto = empresaPermisoDao.findWhereIdEmpresaEquals(idEmpresa)[0];
        }catch(Exception e){e.printStackTrace();}
        
        String respuesta ="";
        
        if (this.pedido!=null){
            try {
                TspMailBO mailBO = new TspMailBO();
                
                
                if(empresaPermisoDto.getAccesoSgfensPretomovil()==1){
                    mailBO.setConfigurationMovilpyme();
                    mailBO.addMessageMovilpyme("<b>Buen día!</b> "
                        + "<br/> El presente, contiene adjunto la representación impresa del  Pedido con folio: " + this.pedido.getFolioPedido(),1);
                } else{
                    mailBO.setConfiguration();            
                    mailBO.addMessage("<b>Buen día!</b> "
                        + "<br/> El presente, contiene adjunto la representación impresa del  Pedido con folio: " + this.pedido.getFolioPedido(),1);
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
                
                mailBO.send("Nuevo Pedido");
                
                respuesta += "Correo enviado exitosamente.";
            } catch (Exception ex) {
                respuesta += "Ocurrio un error al intentar enviar el correo: " + ex.toString();
            }            
        }
        
        return respuesta;
    }
    
    /**
     * Representación impresa PDF
     */
     public ByteArrayOutputStream toPdf(UsuarioBO user) throws Exception{
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
        Font letraOchoVerde = new Font(Font.TIMES_ROMAN, 8, Font.NORMAL, Color.green);
        Font letra14Bold = new Font(Font.HELVETICA, 14, Font.BOLD);

        String msgError = "";
        
        File fileImageLogo = null;
        try{
            if (user!=null)
                fileImageLogo = new ImagenPersonalBO(this.conn).getFileImagenPersonalByEmpresa(user.getUser().getIdEmpresa());
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        try{            
            //Preparamos writer de PDF
            PdfWriter writer = PdfWriter.getInstance(doc, baos);
            EventPDF eventPDF = new EventPDF(doc, user, ReportBO.PEDIDO_REPRESENTACION_IMPRESA,fileImageLogo);
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
            
            //CONTENIDO -------------
            
                //SALTO DE LÍNEA
                obj.agregaCelda(mainTable,letraOcho, " ", new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                    
                //Cabecera (Datos Generales)---------------------
                    PdfPTable tAux = new PdfPTable(1);
                    
                    Cliente clienteDto = null;
                    DatosUsuario datosUsuarioVendedor = null;
                    
                    try{
                        if (this.pedido.getIdCliente()>0)
                            clienteDto = new ClienteBO(this.pedido.getIdCliente(), this.conn).getCliente();
                        if (this.pedido.getIdUsuarioVendedor()>0)
                            datosUsuarioVendedor = new UsuarioBO(this.pedido.getIdUsuarioVendedor()).getDatosUsuario();
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    
                    
                    //Datos informativos generales
                        PdfPTable tInfoGeneral = new PdfPTable(1);
                        tInfoGeneral.setTotalWidth(160);
                        tInfoGeneral.setLockedWidth(true);


                        obj.agregaCelda(tInfoGeneral,letraNueveBold,Color.lightGray, "Folio Pedido", new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tInfoGeneral,letraNueveBoldRojo, this.pedido.getFolioPedido(), new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                                        
                        obj.agregaCelda(tInfoGeneral,letraNueveBold,Color.lightGray, "Fecha Pedido", new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tInfoGeneral,letraNueve, DateManage.dateToStringEspanol(this.pedido.getFechaPedido()), new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tInfoGeneral,letraNueveBold,Color.lightGray, "Vendedor", new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tInfoGeneral,letraNueve, datosUsuarioVendedor!=null?(datosUsuarioVendedor.getNombre() +" " + datosUsuarioVendedor.getApellidoPat()) :"Sin vendedor asignado" , new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                    
                        //Pintamos datos informativos Generales en la esquina superior derecha
                        PdfContentByte cb = writer.getDirectContent();
                        tInfoGeneral.writeSelectedRows(0, -1,doc.right()-180, doc.top() + 100, cb);
                    
                    //Datos de Pedido
                    PdfPTable tInfoPedido = new PdfPTable(2);
                    tInfoPedido.setTotalWidth(550);
                    tInfoPedido.setWidths(new int[]{100,450});
                    tInfoPedido.setLockedWidth(true);
                    
                    obj.agregaCelda(tInfoPedido,letraNueveBold,Color.lightGray, "Fecha de Entrega", new boolean[]{true,true,true,true}, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 0, new int[0],1);
                    obj.agregaCelda(tInfoPedido,letraNueve, DateManage.dateToStringEspanol(this.pedido.getFechaEntrega()), new boolean[]{true,true,true,true}, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 0, new int[0],1);
                    obj.agregaCelda(tInfoPedido,letraNueveBold,Color.lightGray, "Fecha límite de Pago", new boolean[]{true,true,true,true}, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 0, new int[0],1);
                    obj.agregaCelda(tInfoPedido,letraNueve, DateManage.dateToStringEspanol(this.pedido.getFechaTentativaPago()), new boolean[]{true,true,true,true}, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        
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

                        /*
                        obj.agregaCelda(tInfoGeneral,letraOcho, "Folio Pedido: " + (this.pedido.getFolioPedido()), new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);

                        obj.agregaTabla(mainTable, tInfoGeneral, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                        */
                //FIN Cabecera (Datos Generales)-----------------
            
                //DOBLE SALTO DE LÍNEA
                obj.agregaCelda(mainTable, letraOcho, null, " ",
                        new boolean[]{false, false, false, false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0], 1);
                obj.agregaCelda(mainTable, letraOcho, null, " ",
                        new boolean[]{false, false, false, false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0], 1);
                    
                //Datos de Pedido (Productos/Servicios)--------------------
                
                
                    int colsDetalles = 6;
                    PdfPTable tDetalles = new PdfPTable(colsDetalles);//6);
                    tDetalles.setTotalWidth(550);
                    tDetalles.setWidths(new int[]{190,72,72,72,72,72});
                    tDetalles.setLockedWidth(true);



                    /*CABECERA*/
                    /*obj.agregaCelda(tDetalles, letraNueveBoldAzul, null, "Estado de Cuenta del Cliente con ID:" + idCliente,
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{1, 1, 1, 1}, tableSize);*/

                    obj.agregaCelda(tDetalles, letraNueveBold, Color.lightGray, "Producto/Servicio",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                    obj.agregaCelda(tDetalles, letraNueveBold, Color.lightGray, "Unidad",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                    obj.agregaCelda(tDetalles, letraNueveBold, Color.lightGray, "Cantidad Vendida",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                    obj.agregaCelda(tDetalles, letraNueveBold, Color.lightGray, "Cantidad Entregada",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                    obj.agregaCelda(tDetalles, letraNueveBold, Color.lightGray, "Precio Unitario",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                    /*obj.agregaCelda(tDetalles, letraNueveBold, null, "Descuento (%)",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);*/
                    obj.agregaCelda(tDetalles, letraNueveBold, Color.lightGray, "Importe",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                    /*FIN DE CABECERA*/
                    
                    //Listado de Productos
                    for (SgfensPedidoProducto item:this.getProductos_Datos()){

                        if (item!=null){

                            String unidadGranel = "Kg";
                            boolean esGranel = (item.getCantidadPeso()>0);
                            double auxGranel = item.getCantidadPeso() / item.getPesoUnitario();
                            boolean esEnteroGranel = ((int)auxGranel == auxGranel);
                            String descripcionGranel = "";
                            if (esGranel){
                                if (esEnteroGranel) // 0.0 x 0.0Kg x $0.00
                                    descripcionGranel = "\nGranel: " + (int)item.getCantidad()+" x " + item.getPesoUnitario() + unidadGranel + " x $" + Converter.doubleToStringFormatMexico(item.getPrecioUnitarioGranel());
                                else //0.0Kg a 0.00/Kg
                                    descripcionGranel = "\nGranel: " + Converter.doubleToStringFormatMexico(item.getCantidadPeso()) + unidadGranel + " a $" + Converter.doubleToStringFormatMexico(item.getPrecioUnitarioGranel()) + "/" + unidadGranel;
                            }
                            
                            //Producto
                            obj.agregaCelda(tDetalles, letraOcho, null, 
                                        (item.getIdentificacion()!=null?(!item.getIdentificacion().trim().equals("")?item.getIdentificacion()+" - ":""):"")
                                        + item.getDescripcion()
                                        + descripcionGranel,
                                        new boolean[]{true, true, true, true}, Element.ALIGN_JUSTIFIED, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);

                            //Unidad
                            obj.agregaCelda(tDetalles, letraOcho, null, item.getUnidad(),
                                        new boolean[]{true, true, true, true}, Element.ALIGN_JUSTIFIED, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);

                            //Cantidad
                            obj.agregaCelda(tDetalles, letraOcho, null, !esGranel?""+item.getCantidad():""+item.getCantidadPeso(),
                                        new boolean[]{true, true, true, true}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);
                            
                             //Cantidad Entregada
                            obj.agregaCelda(tDetalles, letraOcho, null, !esGranel?""+item.getCantidadEntregada():""+item.getCantidadEntregadaPeso(),
                                        new boolean[]{true, true, true, true}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);

                            //Precio Unitario
                            obj.agregaCelda(tDetalles, letraOcho, null, !esGranel?""+item.getPrecioUnitario():""+item.getPrecioUnitarioGranel(),
                                        new boolean[]{true, true, true, true}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);

                            //Descuento (%)
                            /*
                            obj.agregaCelda(tDetalles, letraOcho, null, ""+item.getDescuentoPorcentaje(),
                                        new boolean[]{true, true, true, true}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);
                            */

                            //Subtotal
                            obj.agregaCelda(tDetalles, letraOcho, null, " "+new DecimalFormat("###,###,###,##0.00").format(item.getSubtotal()),
                                        new boolean[]{true, true, true, true}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);

                            //totalMonto+=item.getMonto();
                        }
                    }

                    //Listado de Servicios
                    for (SgfensPedidoServicio item:this.getServicios_Datos()){

                        if (item!=null){

                            //Servicio
                            obj.agregaCelda(tDetalles, letraOcho, null, 
                                        (item.getIdentificacion()!=null?(!item.getIdentificacion().trim().equals("")?item.getIdentificacion()+" - ":""):"")
                                        +  item.getDescripcion(),
                                        new boolean[]{true, true, true, true}, Element.ALIGN_JUSTIFIED, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);

                            //Unidad
                            obj.agregaCelda(tDetalles, letraOcho, null, item.getUnidad(),
                                        new boolean[]{true, true, true, true}, Element.ALIGN_JUSTIFIED, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);

                            //Cantidad
                            obj.agregaCelda(tDetalles, letraOcho, null, ""+item.getCantidad(),
                                        new boolean[]{true, true, true, true}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);
                            //Cantidad Entregada (No aplica))
                            obj.agregaCelda(tDetalles, letraOcho, null, "NA",
                                        new boolean[]{true, true, true, true}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);

                            //Precio Unitario
                            obj.agregaCelda(tDetalles, letraOcho, null, ""+item.getPrecioUnitario(),
                                        new boolean[]{true, true, true, true}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);

                            //Descuento (%)
                            /*
                            obj.agregaCelda(tDetalles, letraOcho, null, ""+item.getDescuentoPorcentaje(),
                                        new boolean[]{true, true, true, true}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);
                            */

                            //Subtotal
                            obj.agregaCelda(tDetalles, letraOcho, null, " "+new DecimalFormat("###,###,###,##0.00").format(item.getSubtotal()),
                                        new boolean[]{true, true, true, true}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);

                            //totalMonto+=item.getMonto();
                        }
                    }                    
                    
                    obj.agregaTabla(mainTable, tDetalles, new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
            
                //FIN Datos de Pedido (Productos/Servicios)----------------
                    
                // Devoluciones -----------                     
                    
                    
                    SGPedidoDevolucionesCambioBO cambioDev = new SGPedidoDevolucionesCambioBO(this.conn);
                    SgfensPedidoDevolucionCambio[] devoluciones = cambioDev.findCambioDevByIdPedido(this.pedido.getIdPedido(),1,this.conn);//1 devoluciones

                    if(devoluciones.length>0){
                        
                        //DOBLE SALTO DE LÍNEA                        
                        obj.agregaCelda(mainTable, letraOcho, null, " ",
                                new boolean[]{false, false, false, false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0], 1);
                        
                        
                        PdfPTable tDetallesDev = new PdfPTable(colsDetalles);//6);
                        tDetallesDev.setTotalWidth(550);
                        tDetallesDev.setWidths(new int[]{190,72,72,72,72,72});
                        tDetallesDev.setLockedWidth(true);
                        
                        
                       /*CABECERA*/                        

                        obj.agregaCelda(tDetallesDev, letraNueveBold, Color.lightGray, "Devoluciones",
                                new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 6);                        
                        
                        
                        obj.agregaCelda(tDetallesDev, letraNueveBold, Color.lightGray, "Producto/Servicio",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 2);
                        obj.agregaCelda(tDetallesDev, letraNueveBold, Color.lightGray, "Unidad",
                                new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                        obj.agregaCelda(tDetallesDev, letraNueveBold, Color.lightGray, "Cantidad",
                                new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                        obj.agregaCelda(tDetallesDev, letraNueveBold, Color.lightGray, "Precio Unitario",
                                new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);                        
                        obj.agregaCelda(tDetallesDev, letraNueveBold, Color.lightGray, "Importe",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                        /*FIN DE CABECERA*/
                        
                        for (SgfensPedidoDevolucionCambio dev : devoluciones){

                            if (dev!=null){
                                System.out.println("Dev --->" + dev.getIdPedidoDevolCambio());
                                Concepto conceptoDevDto = new ConceptoBO(dev.getIdConcepto(),this.conn).getConcepto();
                                SGPedidoProductoBO pedidoProductoBO = new SGPedidoProductoBO(this.conn);
                                SgfensPedidoProducto[] conceptoPedidoDto = pedidoProductoBO.findByIdPedido(this.pedido.getIdPedido(), -1, -1, -1, " AND ID_CONCEPTO="+conceptoDevDto.getIdConcepto());

                                //Producto
                                obj.agregaCelda(tDetallesDev, letraOcho, null, (conceptoDevDto.getIdentificacion()!=null?(!conceptoDevDto.getIdentificacion().trim().equals("")?conceptoDevDto.getIdentificacion()+" - ":""):"")
                                        +  conceptoDevDto.getDescripcion() ,
                                            new boolean[]{true, true, true, true}, Element.ALIGN_JUSTIFIED, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 2);

                                //Unidad
                                obj.agregaCelda(tDetallesDev, letraOcho, null, conceptoPedidoDto[0].getUnidad(),
                                            new boolean[]{true, true, true, true}, Element.ALIGN_JUSTIFIED, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);

                                //Cantidad
                                obj.agregaCelda(tDetallesDev, letraOcho, null, ""+(dev.getAptoParaVenta()+dev.getNoAptoParaVenta()) ,
                                            new boolean[]{true, true, true, true}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);

                                //Precio Unitario
                                obj.agregaCelda(tDetallesDev, letraOcho, null, ""+conceptoPedidoDto[0].getPrecioUnitario(),
                                            new boolean[]{true, true, true, true}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);
                                
                               //Subtotal
                               obj.agregaCelda(tDetallesDev, letraOcho, null, ""+((dev.getAptoParaVenta()+dev.getNoAptoParaVenta())*conceptoPedidoDto[0].getPrecioUnitario()),
                                        new boolean[]{true, true, true, true}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1); 

                            
                            }
                        }

                        obj.agregaTabla(mainTable, tDetallesDev, new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                    }
                    
                 
                    
                //FIN Devoluciones ----------    
            
                    // Cambios -----------                          
                    SgfensPedidoDevolucionCambio[] cambios = cambioDev.findCambioDevByIdPedido(this.pedido.getIdPedido(),2,this.conn);//1 cambios

                    if(cambios.length>0){
                        
                        //DOBLE SALTO DE LÍNEA                        
                        obj.agregaCelda(mainTable, letraOcho, null, " ",
                                new boolean[]{false, false, false, false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0], 1);
                        
                        
                        PdfPTable tDetallesCambios = new PdfPTable(colsDetalles);//6);
                        tDetallesCambios.setTotalWidth(550);
                        tDetallesCambios.setWidths(new int[]{190,72,72,72,72,72});
                        tDetallesCambios.setLockedWidth(true);
                        
                        
                       /*CABECERA*/                        

                        obj.agregaCelda(tDetallesCambios, letraNueveBold, Color.lightGray, "Cambios",
                                new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 6);                        
                        
                        
                        obj.agregaCelda(tDetallesCambios, letraNueveBold, Color.lightGray, "Producto/Servicio",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 2);
                        obj.agregaCelda(tDetallesCambios, letraNueveBold, Color.lightGray, "Unidad",
                                new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                        obj.agregaCelda(tDetallesCambios, letraNueveBold, Color.lightGray, "Cantidad",
                                new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                        obj.agregaCelda(tDetallesCambios, letraNueveBold, Color.lightGray, "Precio Unitario",
                                new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);                        
                        obj.agregaCelda(tDetallesCambios, letraNueveBold, Color.lightGray, "Importe",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                        /*FIN DE CABECERA*/
                        
                        for (SgfensPedidoDevolucionCambio cambio : cambios){

                            if (cambio!=null){
                                System.out.println("Cambio --->" + cambio.getIdPedidoDevolCambio());
                                Concepto conceptoDevDto = new ConceptoBO(cambio.getIdConcepto(),this.conn).getConcepto();
                                SGPedidoProductoBO pedidoProductoBO = new SGPedidoProductoBO(this.conn);
                                SgfensPedidoProducto[] conceptoPedidoDto = pedidoProductoBO.findByIdPedido(this.pedido.getIdPedido(), -1, -1, -1, " AND ID_CONCEPTO="+conceptoDevDto.getIdConcepto());

                                //Devuleto 
                                 
                                //Producto
                                obj.agregaCelda(tDetallesCambios, letraOcho, null, (conceptoDevDto.getIdentificacion()!=null?(!conceptoDevDto.getIdentificacion().trim().equals("")?conceptoDevDto.getIdentificacion()+" - ":""):"")
                                        +  conceptoDevDto.getDescripcion() + "  [Devuelto]" ,
                                            new boolean[]{true, true, true, true}, Element.ALIGN_JUSTIFIED, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 2);

                                //Unidad
                                obj.agregaCelda(tDetallesCambios, letraOcho, null, conceptoPedidoDto[0].getUnidad(),
                                            new boolean[]{true, true, true, true}, Element.ALIGN_JUSTIFIED, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);

                                //Cantidad
                                obj.agregaCelda(tDetallesCambios, letraOcho, null, ""+(cambio.getAptoParaVenta()+cambio.getNoAptoParaVenta()) ,
                                            new boolean[]{true, true, true, true}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);

                                //Precio Unitario
                                obj.agregaCelda(tDetallesCambios, letraOcho, null, ""+conceptoPedidoDto[0].getPrecioUnitario(),
                                            new boolean[]{true, true, true, true}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);
                                
                               //Subtotal
                               obj.agregaCelda(tDetallesCambios, letraOcho, null, ""+((cambio.getAptoParaVenta()+cambio.getNoAptoParaVenta())*conceptoPedidoDto[0].getPrecioUnitario()),
                                        new boolean[]{true, true, true, true}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1); 
                               
                               // Entregado
                               
                               Concepto conceptoEntregadoDto = new ConceptoBO(cambio.getIdConceptoEntregado(),this.conn).getConcepto();
                               
                               //Producto
                                obj.agregaCelda(tDetallesCambios, letraOcho, null, (conceptoEntregadoDto.getIdentificacion()!=null?(!conceptoEntregadoDto.getIdentificacion().trim().equals("")?conceptoEntregadoDto.getIdentificacion()+" - ":""):"")
                                        +  conceptoEntregadoDto.getDescripcion() + "  [Entregado]" ,
                                            new boolean[]{true, true, true, true}, Element.ALIGN_JUSTIFIED, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);

                                //Unidad
                                obj.agregaCelda(tDetallesCambios, letraOcho, null, "PIEZA",
                                            new boolean[]{true, true, true, true}, Element.ALIGN_JUSTIFIED, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);

                                //Cantidad
                                obj.agregaCelda(tDetallesCambios, letraOcho, null, ""+cambio.getCantidadDevuelta() ,
                                            new boolean[]{true, true, true, true}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);

                                //Precio Unitario
                                obj.agregaCelda(tDetallesCambios, letraOcho, null, ""+(((cambio.getAptoParaVenta()*conceptoPedidoDto[0].getPrecioUnitario())+cambio.getMontoResultante())/cambio.getCantidadDevuelta()),
                                            new boolean[]{true, true, true, true}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1);
                                
                               //Subtotal
                               obj.agregaCelda(tDetallesCambios, letraOcho, null, ""+(cambio.getCantidadDevuelta()*(((cambio.getAptoParaVenta()*conceptoPedidoDto[0].getPrecioUnitario())+cambio.getMontoResultante())/cambio.getCantidadDevuelta())),
                                        new boolean[]{true, true, true, true}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 15, new int[]{5, 5, 5, 5}, 1); 

                            
                            }
                        }

                        obj.agregaTabla(mainTable, tDetallesCambios, new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                    }
                    
                 
                    
                //FIN Cambios ----------    
                    
                    
                //DOBLE SALTO DE LÍNEA
                        obj.agregaCelda(mainTable, letraOcho, null, " ",
                                new boolean[]{false, false, false, false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0], 1);
                        obj.agregaCelda(mainTable, letraOcho, null, " ",
                                new boolean[]{false, false, false, false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0], 1);
            
                //Datos de Totales ------------------------------
                    
                    Translator numALetra = new Translator();
                    String totalConLetra = "";
                    String moneda = this.pedido.getTipoMoneda();
                    try{
                        if (moneda!=null){
                            if (!"".equals(moneda)){
                                numALetra.setNombreMoneda(moneda);
                            }
                        }

                        totalConLetra = numALetra.getStringOfNumber(this.pedido.getBonificacionDevolucion()>0?this.pedido.getTotal():this.pedido.getTotal()+Math.abs(this.pedido.getBonificacionDevolucion()));
                    }catch(Exception e){}
                    
                    Impuesto impuestoDto = null;
                    
                    /*Tipo de Moneda*/
                    /*
                    PdfPTable tTipoMoneda = new PdfPTable(5);
                    tTipoMoneda.setTotalWidth(550);
                    tTipoMoneda.setLockedWidth(true);
                    
                    obj.agregaCelda(tTipoMoneda, letraNueveBold, null, " ",
                                new boolean[]{false, false, false, false}, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 3);
                    obj.agregaCelda(tTipoMoneda, letraNueveBold, Color.lightGray, "Tipo de Moneda",
                                new boolean[]{true, true, true, true}, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);    

                    obj.agregaCelda(tTipoMoneda, letraOcho, null, this.pedido.getTipoMoneda(),
                                new boolean[]{true, true, true, true}, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                    
                    
                   obj.agregaTabla(mainTable, tTipoMoneda, new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);           
                   */
                   
                   
                    PdfPTable tTotal = new PdfPTable(2);
                    tTotal.setTotalWidth(550);
                    tTotal.setWidths(new int[]{400,150});
                    tTotal.setLockedWidth(true);

                    obj.agregaCelda(tTotal,letraOchoBold,Color.lightGray, "TOTAL CON LETRA", new boolean[]{false,true,false,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                    obj.agregaCelda(tTotal,letraOchoBold,Color.lightGray, "TOTAL", new boolean[]{true,false,false,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);

                    obj.agregaCelda(tTotal,letraNueve, "" + totalConLetra, new boolean[]{false,true,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                    tAux = new PdfPTable(2);
                    tAux.setTotalWidth(150);

                    obj.agregaCelda(tAux,letraOchoBold, "SUBTOTAL" , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                    obj.agregaCelda(tAux,letraOcho, "" + new DecimalFormat("$###,###,###,##0.00").format(this.pedido.getSubtotal()), new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 0, new int[0],1);

                    obj.agregaCelda(tAux,letraOchoBold, "DESCUENTO" , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                    obj.agregaCelda(tAux,letraOcho, "" + new DecimalFormat("$###,###,###,##0.00").format(this.pedido.getDescuentoMonto()) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 0, new int[0],1);

                    boolean impuestosLocales =false;
                    for (SgfensPedidoImpuesto item:this.getImpuestos_Datos()){
                        impuestoDto = new ImpuestoBO(item.getIdImpuesto(),this.conn).getImpuesto();

                        if (impuestoDto!=null){
                            //Verificamos que no sea un impuesto local, esos se detallaran mas adelante
                            if (impuestoDto.getImpuestoLocal()!=(short)1){
                                double montoImpuesto = new BigDecimal((this.getPedido().getSubtotal()-this.getPedido().getDescuentoMonto())*(impuestoDto.getPorcentaje()/100)).setScale(2, RoundingMode.HALF_UP).doubleValue();

                                obj.agregaCelda(tAux,letraOchoBold, (impuestoDto.getTrasladado()!=(short)1?"RETENCIÓN ":"") + impuestoDto.getNombre() + " " + impuestoDto.getPorcentaje()+"%", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                                obj.agregaCelda(tAux,letraOcho, "" + new DecimalFormat("$###,###,###,##0.00").format(montoImpuesto) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 0, new int[0],1);
                            }else{
                                impuestosLocales=true;
                            }
                        }
                    }

                    /*
                    * Impuestos Locales
                    */
                    
                    if (impuestosLocales){

                        obj.agregaCelda(tAux,letraOchoBold, ">>Impuestos Locales<<", new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],2);

                        for (SgfensPedidoImpuesto item:this.getImpuestos_Datos()){
                            impuestoDto = new ImpuestoBO(item.getIdImpuesto(),this.conn).getImpuesto();

                            if (impuestoDto!=null){
                                //Verificamos que no sea un impuesto local, esos se detallaran mas adelante
                                if (impuestoDto.getImpuestoLocal()==(short)1){
                                    double montoImpuesto = new BigDecimal((this.getPedido().getSubtotal()-this.getPedido().getDescuentoMonto())*(impuestoDto.getPorcentaje()/100)).setScale(2, RoundingMode.HALF_UP).doubleValue();

                                    obj.agregaCelda(tAux,letraOchoBold, (impuestoDto.getTrasladado()!=(short)1?"RETENCIÓN ":"") + impuestoDto.getNombre() + " " + impuestoDto.getPorcentaje()+"%", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                                    obj.agregaCelda(tAux,letraOcho, "" + new DecimalFormat("$###,###,###,##0.00").format(montoImpuesto) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 0, new int[0],1);
                                }
                            }
                        }

                    }
                            
                    obj.agregaCelda(tAux,letraNueveBold, "TOTAL" , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM, 50, new int[0],1);
                    obj.agregaCelda(tAux,letraNueveBoldAzul, "" + new DecimalFormat("$###,###,###,##0.00").format(this.pedido.getBonificacionDevolucion()>0?this.pedido.getTotal():this.pedido.getTotal()+Math.abs(this.pedido.getBonificacionDevolucion())) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_BOTTOM, 50, new int[0],1);

                    obj.agregaTabla(tTotal, tAux, new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                    
                    //SALDOS----------     
                    double saldoPendiente =0;
                    try{
                        if(this.pedido.getBonificacionDevolucion()>0){
                            saldoPendiente = new BigDecimal(this.pedido.getTotal()-this.pedido.getSaldoPagado()).setScale(2, RoundingMode.HALF_UP).doubleValue();
                        }else{
                            saldoPendiente = new BigDecimal((this.pedido.getTotal() + Math.abs(this.pedido.getBonificacionDevolucion()))-this.pedido.getSaldoPagado()).setScale(2, RoundingMode.HALF_UP).doubleValue();
                        }                         
                        
                        
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    
                    //Devolución Efectivo----------
                    double totalDevolucionEfectivo = 0;
                    try{
                        SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(user.getConn());
                        SgfensCobranzaAbono[] cobranzaAbonoDtos = new SgfensCobranzaAbono[0];

                        String filtroBusquedaCobros = " AND ID_PEDIDO = "  + this.pedido.getIdPedido() +  " ";                                                                
                        cobranzaAbonoDtos = cobranzaAbonoBO.findCobranzaAbono(-1, user.getUser().getIdEmpresa() , 0, 0, filtroBusquedaCobros);

                        for (SgfensCobranzaAbono itemCob:cobranzaAbonoDtos){       


                            if (itemCob.getIdEstatus()==1){                                                                                    
                                if(itemCob.getIdCobranzaMetodoPago() == VentaMetodoPagoBO.METODO_PAGO_DEVOLUCION_EFE){
                                    totalDevolucionEfectivo += itemCob.getMontoAbono();
                                }
                            }
                        }


                    }catch(Exception e){
                        System.out.println("Problema al recuperar Devoluciones de EFECTIVO");
                    }
                    
                    
                    
                    obj.agregaCelda(tTotal,letraNueve, "", new boolean[]{false,true,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                    obj.agregaCelda(tTotal,letraOchoBold,Color.lightGray, "SALDOS", new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                    
                    obj.agregaCelda(tTotal,letraNueve, "", new boolean[]{false,true,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                        //Tabla auxiliar para saldos
                        tAux = new PdfPTable(2);
                        tAux.setTotalWidth(150);

                        obj.agregaCelda(tAux,letraOchoBold, "ADELANTO" , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                        obj.agregaCelda(tAux,letraOcho, "" + new DecimalFormat("$###,###,###,##0.00").format(this.pedido.getAdelanto()), new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 0, new int[0],1);

                        obj.agregaCelda(tAux,letraOchoBold, "PAGADO" , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                        obj.agregaCelda(tAux,letraOcho, "" + new DecimalFormat("$###,###,###,##0.00").format(this.pedido.getSaldoPagado()) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 0, new int[0],1);
                        
                        obj.agregaCelda(tAux,letraNueveBold, "PENDIENTE" , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM, 10, new int[0],1);
                        obj.agregaCelda(tAux,letraNueveBoldRojo, "" + new DecimalFormat("$###,###,###,##0.00").format(saldoPendiente) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_BOTTOM, 10, new int[0],1);
                        
                        if(this.pedido.getBonificacionDevolucion()>0){
                            obj.agregaCelda(tAux,letraOchoBold, "BONIFICACION" , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM, 10, new int[0],1);
                            obj.agregaCelda(tAux,letraOchoVerde, "" + new DecimalFormat("$###,###,###,##0.00").format(Math.abs(this.pedido.getBonificacionDevolucion())) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_BOTTOM, 10, new int[0],1);
                        }
                        if(totalDevolucionEfectivo>0){
                            obj.agregaCelda(tAux,letraOchoBold, "DEVOLUCION EFE" , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM, 10, new int[0],1);
                            obj.agregaCelda(tAux,letraNueveBoldRojo, "" + new DecimalFormat("$###,###,###,##0.00").format(Math.abs(totalDevolucionEfectivo)) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_BOTTOM, 10, new int[0],1);
                        }
                        
                        
                        obj.agregaTabla(tTotal, tAux, new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                    
                    /*Comentarios Adicionales*/
                    obj.agregaCelda(tTotal, letraNueveBold, Color.lightGray, "Comentarios pedido",
                                new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 2);    
                    obj.agregaCelda(tTotal, letraOcho, null, this.pedido.getComentarios(),
                                new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 2);

                    obj.agregaTabla(mainTable, tTotal, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                   
                   //FIN Datos de Totales --------------------------
            //FIN DE CONTENIDO --------
            
            //Añadimos tabla principal construida al documento
            doc.add(mainTable);
            mainTable.flushContent();
            
        }catch(Exception ex){
           msgError = "No se ha podido generar la representación impresa del Pedido en formato PDF:<br/>" + ex.toString();
        }finally{
            if(doc.isOpen())
                doc.close();
        }
        
        if (!msgError.equals("")) {
            throw new Exception(msgError);
        }
         
        return baos;
     }
     
     public File guardarRepresentacionImpresa(UsuarioBO userSesion) throws FileNotFoundException, Exception {
        Configuration configuration = new Configuration();
         
        File  archivoRepresentacionImpresa = null;
        
        Empresa empresaDto = new EmpresaBO(this.pedido.getIdEmpresa(),this.conn).getEmpresa(); 
        Cliente clienteDto = new ClienteBO(this.pedido.getIdCliente(),this.conn).getCliente(); 
                
        String rutaRepositorio = "";
        rutaRepositorio = configuration.getApp_content_path() + empresaDto.getRfc() + "/pedidos/"+ clienteDto.getRfcCliente();
        
        String nombreArchivo = "Pedido_" + empresaDto.getRfc() + "_" + this.pedido.getFolioPedido() + "_" + (new Date()).getTime() + ".pdf";

        //Nos aseguramos que el Folder Exista
        File carpetaRepositorio = new File(rutaRepositorio);
        carpetaRepositorio.mkdirs();


        //Guardamos archivo PDF
        archivoRepresentacionImpresa = new File(rutaRepositorio + "/" + nombreArchivo);
        FileOutputStream fos = new FileOutputStream(archivoRepresentacionImpresa);

        this.toPdf(userSesion).writeTo(fos);
        
        fos.flush();
        fos.close();

        return archivoRepresentacionImpresa;
    }
     
     /**
     * Calcula los dias transcurridos desde la captura hasta Hoy
     * dias transcurridos =  Fecha Hoy - Fecha Captura
     * @return 
     */
    public long calculaDiasTranscurridosCredito(){
        long diasTranscurridosCreditos = 0;
        
        Date fechaHoy=new Date();
        Date fechaCaptura = this.pedido.getFechaPedido();
        
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
            double adeudo = cobranzaAbonoBO.getSaldoAdeudoPedido(this.pedido.getIdPedido()).doubleValue();
            
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
        
        Date fechaPago=this.pedido.getFechaTentativaPago();
        Date fechaCaptura = this.pedido.getFechaPedido();
        
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
        Date fechaPago=this.pedido.getFechaTentativaPago();
        
        try{
            SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(this.conn);
            double adeudo = cobranzaAbonoBO.getSaldoAdeudoPedido(this.pedido.getIdPedido()).doubleValue();
            
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
    
    public ComprobanteFiscalSesion convertirAComprobanteFiscalSesion(PedidoSesion pedidoSesion){
        ComprobanteFiscalSesion comprobanteFiscalSesion = null;
        
        if (pedidoSesion!=null){
            FormatoSesion formatoSesion = (FormatoSesion) pedidoSesion;

            comprobanteFiscalSesion = new ComprobanteFiscalSesion();
            comprobanteFiscalSesion.setCliente(formatoSesion.getCliente());
            comprobanteFiscalSesion.setComentarios("Pedido ID " + pedidoSesion.getIdPedido() + " . "  + formatoSesion.getComentarios());
            comprobanteFiscalSesion.setDescuento_motivo(formatoSesion.getDescuento_motivo());
            comprobanteFiscalSesion.setDescuento_tasa(formatoSesion.getDescuento_tasa());
            comprobanteFiscalSesion.setListaImpuesto(formatoSesion.getListaImpuesto());
            comprobanteFiscalSesion.setListaProducto(formatoSesion.getListaProducto());
            comprobanteFiscalSesion.setListaServicio(formatoSesion.getListaServicio());
            comprobanteFiscalSesion.setTipo_moneda(formatoSesion.getTipo_moneda());
            comprobanteFiscalSesion.setFecha_pago(pedidoSesion.getFechaTentativaPago());

            comprobanteFiscalSesion.setIdPedido(pedidoSesion.getIdPedido());
        }
        
        return comprobanteFiscalSesion;
        
    }
    
    /**
     * Cancela el pedido y si se específica en el parametro adecuado tambien los abonos realizados a este.
     * 
     * <p>
     * IMPORTANTE: Los cobros mediante tarjeta de crédito/débito no son cancelados, 
     * debiendo ser cancelados manualmente por un operador desde consola o desde el sistema movil.
     * 
     * @param cancelarAbonos boolean, true en caso de requerir que sean cancelados cada uno de los abonos realizados, false en caso contrario
     * @param isConsola boolean, true en caso de ser una accion desde consola, false en caso de proceder desde web service movil
     * @throws Exception 
     */
    public void cancelarPedido(boolean cancelarAbonos, boolean isConsola) throws Exception{
    
        if (this.pedido!=null){
            
            //Si tiene un Comprobante Fiscal ligado, lo cancelamos primero
            if (this.pedido.getIdComprobanteFiscal()>0){
                //Existe un CFDI relacionado al pedido
                ComprobanteFiscalBO comprobanteFiscalBO = new ComprobanteFiscalBO(this.pedido.getIdComprobanteFiscal(),this.conn);
                try{
                    comprobanteFiscalBO.cancelaComprobanteFiscal();
                }catch(Exception ex){
                    throw new Exception("<ul>El pedido tiene una factura relacionada. Ocurrio un error al cancelar la factura (CFDI).<br/>" + ex.toString());
                }
            }
            
            //Cancelamos pedido
            try{
                this.pedido.setIdEstatusPedido((short)SGEstatusPedidoBO.ESTATUS_CANCELADO);
                this.pedido.setIsModificadoConsola(isConsola?(short)1:(short)0);

                new SgfensPedidoDaoImpl(this.conn).update(this.pedido.createPk(), this.pedido);
            }catch(Exception ex){
                throw new Exception("<ul>Error al actualizar estatus de pedido a cancelado. " +ex.toString());
            }

            //Cancelamos abonos en caso de ser requerido
            if (cancelarAbonos){
                SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(this.conn);
                SgfensCobranzaAbono[] abonosPedido = cobranzaAbonoBO.findCobranzaAbono(-1, this.pedido.getIdEmpresa() ,0 , 0, 
                        " AND ID_ESTATUS=1 AND ID_PEDIDO=" + this.pedido.getIdPedido() );
                
                SgfensCobranzaAbonoDaoImpl cobranzaAbonoDao = new SgfensCobranzaAbonoDaoImpl(this.conn);
                for (SgfensCobranzaAbono abono : abonosPedido){
                    abono.setIdEstatus(2);
                    abono.setSincronizacionMicrosip(2); // modificado (para sincronizacion microsip)
                    cobranzaAbonoDao.update(abono.createPk(), abono);
                }
                
            }
            
        }else{
            throw new Exception("Los datos del pedido no han sido inicializados. Pedido nulo.");
        }
        
    }
    
    
    
    public String getPedidosByIdHTMLCombo(int idEmpresa, int idSeleccionado, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            SgfensPedido[] sgfensPedido = findPedido(-1, idEmpresa, 0, 0, filtroBusqueda);
            
            for (SgfensPedido itemPedido:sgfensPedido){
                try{
                    String selectedStr="";

                    if (idSeleccionado==itemPedido.getIdPedido())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+itemPedido.getIdPedido()+"' "
                            + selectedStr +">"               
                            + itemPedido.getIdPedido()
                          /*  + " - " 
                            + itemPedido.getFolioPedido()*/
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
    
    /*
    *
    *
    */
    public File crearArchivoImagenExtraFromBase64(long idEmpresa, String imagenBase64) {
        File archivoImagen = null;

        if (imagenBase64 != null) {
            if (imagenBase64.trim().length() > 0) {

                String rfcEmpresaMatriz = "";
                try {
                    Empresa empresaMatrizDto = new EmpresaBO(this.conn).getEmpresaMatriz(idEmpresa);
                    rfcEmpresaMatriz = empresaMatrizDto.getRfc();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }

                try {
                    Configuration appConfig = new Configuration();
                    String ubicacionImagenesAbonos = appConfig.getApp_content_path() + rfcEmpresaMatriz + "/firma/images/";
                    System.out.println("PATH: " + ubicacionImagenesAbonos);

                    //Convertimos bytes en base64 a una imagen y la almacenamos en servidor
                    byte[] bytesImagen = Base64.decode(imagenBase64);

                    String nombreArchivoImagen = "";
                    
                    nombreArchivoImagen = "img_firma_" + DateManage.getDateHourString() + ".bmp";

                    archivoImagen = FileManage.createFileFromByteArray(bytesImagen, ubicacionImagenesAbonos, nombreArchivoImagen);

                    if (!archivoImagen.exists()) {
                        archivoImagen = null;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("ERROR: " + ex.getMessage());
                    archivoImagen = null;
                }
            }
        }

        return archivoImagen;
    }
    
    
    public SgfensPedido[] findByPedidosPorVendedor(int idPedido, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda, String ascDesc) {
        SgfensPedido[] pedidoDto = new SgfensPedido[0];
        SgfensPedidoDaoImpl pedidoDao = new  SgfensPedidoDaoImpl(this.conn);
                
            //EL VALOR DEL CONTADOR, QUE RETORNA LA BASE DE DATOS, DE NUMERO DE PEDIDOS QUE TIENE EL VENDEDOR VA A CAER EN EL ATRIBUTO DE ID_CLIENTE
        
             try {
                String sqlFiltro="SELECT ID_PEDIDO, ID_USUARIO_VENDEDOR, ID_EMPRESA, COUNT(ID_USUARIO_VENDEDOR) AS 'ID_CLIENTE', CONSECUTIVO_PEDIDO, FOLIO_PEDIDO, FECHA_PEDIDO, TIPO_MONEDA, TIEMPO_ENTREGA_DIAS, COMENTARIOS, DESCUENTO_TASA, DESCUENTO_MONTO, SUBTOTAL, SUM(TOTAL) AS 'TOTAL', DESCUENTO_MOTIVO, FECHA_ENTREGA, FECHA_TENTATIVA_PAGO, SUM(SALDO_PAGADO + ADELANTO) AS SALDO_PAGADO, ADELANTO, ID_COMPROBANTE_FISCAL, ID_ESTATUS_PEDIDO, LATITUD, LONGITUD, FOLIO_PEDIDO_MOVIL, NOMBRE_IMAGEN_FIRMA, IS_MODIFICADO_CONSOLA, BONIFICACION_DEVOLUCION, ID_USUARIO_CONDUCTOR_ASIGNADO, ID_USUARIO_VENDEDOR_ASIGNADO,ID_USUARIO_VENDEDOR_REASIGNADO,FECHA_LIMITE_REASIGANCION,CONSIGNA,SINCRONIZACION_MICROSIP,ID_VIA_EMBARQUE,ID_FOLIO_MOVIL_EMPLEADO,FOLIO_MOVIL_EMPLEADO_GENERADO,ID_ESTATUS_PEDIDO_SISTEMA_TERCERO "
                        + " FROM sgfens_pedido WHERE ";

                if (idPedido>0){
                    sqlFiltro +="ID_PEDIDO =" + idPedido + " AND ";
                }else{
                    sqlFiltro +="ID_PEDIDO>0 AND ";
                }            
                if (idEmpresa>0){
                    sqlFiltro +="ID_PEDIDO IN (SELECT ID_PEDIDO FROM SGFENS_PEDIDO WHERE ID_EMPRESA IN  (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA=" + idEmpresa + " ))";
                }else{
                    sqlFiltro +="ID_PEDIDO IN (SELECT ID_PEDIDO FROM SGFENS_PEDIDO WHERE ID_EMPRESA IN  (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE > 0 OR ID_EMPRESA > 0 ))";
                }

                if (!filtroBusqueda.trim().equals("")){
                    sqlFiltro += filtroBusqueda;
                }

                if (minLimit<0)
                    minLimit=0;

                String sqlLimit="";
                if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                    sqlLimit = " LIMIT " + minLimit + "," + maxLimit;


                pedidoDto = pedidoDao.findByDynamicSelect(sqlFiltro
                        + " GROUP BY ID_USUARIO_VENDEDOR "
                        + "ORDER BY ID_CLIENTE " + ascDesc
                        //+ "LIMIT 0,10"
                        +sqlLimit
                        , new Object[0]);

                /*pedidoProductoDto = pedidoProductoDao.findByDynamicWhere( 
                        sqlFiltro
                        + " ORDER BY ID_PEDIDO DESC"
                        + sqlLimit
                        , new Object[0]);*/

            } catch (Exception ex) {
                System.out.println("Error de consulta a Base de Datos: " + ex.toString());
                ex.printStackTrace();
            }
        
        return pedidoDto;
    }
    
    public SgfensPedido[] findByPedidosPorZona(int idPedido, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda, String ascDesc) {
        SgfensPedido[] pedidoDto = new SgfensPedido[0];
        SgfensPedidoDaoImpl pedidoDao = new  SgfensPedidoDaoImpl(this.conn);
                
            //EL VALOR DEL CONTADOR, QUE RETORNA LA BASE DE DATOS, DE NUMERO DE PEDIDOS QUE TIENE EL VENDEDOR VA A CAER EN EL ATRIBUTO DE ID_CLIENTE
        
             try {
                String sqlFiltro="SELECT ID_PEDIDO, ID_USUARIO_VENDEDOR, sgp.ID_EMPRESA, COUNT(emp.ID_REGION) AS 'ID_CLIENTE', CONSECUTIVO_PEDIDO, FOLIO_PEDIDO, FECHA_PEDIDO, TIPO_MONEDA, TIEMPO_ENTREGA_DIAS, COMENTARIOS, DESCUENTO_TASA, DESCUENTO_MONTO, SUBTOTAL, SUM(TOTAL) AS 'TOTAL', DESCUENTO_MOTIVO, FECHA_ENTREGA, FECHA_TENTATIVA_PAGO, SUM(SALDO_PAGADO + ADELANTO) AS SALDO_PAGADO, ADELANTO, ID_COMPROBANTE_FISCAL, ID_ESTATUS_PEDIDO, sgp.LATITUD, sgp.LONGITUD, FOLIO_PEDIDO_MOVIL, NOMBRE_IMAGEN_FIRMA, IS_MODIFICADO_CONSOLA, BONIFICACION_DEVOLUCION, ID_USUARIO_CONDUCTOR_ASIGNADO, emp.ID_REGION AS ID_USUARIO_VENDEDOR_ASIGNADO ,ID_USUARIO_VENDEDOR_REASIGNADO,FECHA_LIMITE_REASIGANCION,CONSIGNA,sgp.SINCRONIZACION_MICROSIP,ID_VIA_EMBARQUE,emp.ID_FOLIO_MOVIL_EMPLEADO,FOLIO_MOVIL_EMPLEADO_GENERADO,ID_ESTATUS_PEDIDO_SISTEMA_TERCERO "
                        + " FROM sgfens_pedido sgp, empleado emp WHERE ";

                if (idPedido>0){
                    sqlFiltro +="ID_PEDIDO =" + idPedido + " AND ";
                }else{
                    sqlFiltro +="ID_PEDIDO>0 AND ";
                }            
                if (idEmpresa>0){
                    sqlFiltro +="ID_PEDIDO IN (SELECT ID_PEDIDO FROM SGFENS_PEDIDO WHERE sgp.ID_EMPRESA IN  (SELECT EMPRESA.ID_EMPRESA FROM EMPRESA WHERE EMPRESA.ID_EMPRESA_PADRE = " + idEmpresa + " OR EMPRESA.ID_EMPRESA=" + idEmpresa + " )) "
                            + " AND emp.ID_REGION IN (SELECT ID_REGION FROM region WHERE ID_EMPRESA = " + idEmpresa + " AND ID_ESTATUS = 1)";
                }else{
                    sqlFiltro +="ID_PEDIDO IN (SELECT ID_PEDIDO FROM SGFENS_PEDIDO WHERE sgp.ID_EMPRESA IN  (SELECT EMPRESA.ID_EMPRESA FROM EMPRESA WHERE EMPRESA.ID_EMPRESA_PADRE > 0 OR EMPRESA.ID_EMPRESA > 0 )) "
                            + " AND emp.ID_REGION IN (SELECT ID_REGION FROM region WHERE ID_EMPRESA > 0 AND ID_ESTATUS = 1)";
                }

                if (!filtroBusqueda.trim().equals("")){
                    sqlFiltro += filtroBusqueda;
                }

                filtroBusqueda += " AND (sgp.ID_USUARIO_VENDEDOR = emp.ID_USUARIOS) ";
                
                if (minLimit<0)
                    minLimit=0;

                String sqlLimit="";
                if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                    sqlLimit = " LIMIT " + minLimit + "," + maxLimit;


                pedidoDto = pedidoDao.findByDynamicSelect(sqlFiltro
                        + " GROUP BY emp.ID_REGION "
                        + "ORDER BY ID_CLIENTE " + ascDesc
                        //+ "LIMIT 0,10"
                        +sqlLimit
                        , new Object[0]);

                /*pedidoProductoDto = pedidoProductoDao.findByDynamicWhere( 
                        sqlFiltro
                        + " ORDER BY ID_PEDIDO DESC"
                        + sqlLimit
                        , new Object[0]);*/

            } catch (Exception ex) {
                System.out.println("Error de consulta a Base de Datos: " + ex.toString());
                ex.printStackTrace();
            }
        
        return pedidoDto;
    }
    
    
    /**
     * Da salida del inventario personalizado del Empleado Vendedor/Repartidor
     * los productos solicitados
     * @return
     * @throws Exception 
     */
    public boolean actualizaSalidaAlmacenProductosInventarioEmpleado(int idEmpleado , ArrayList<SgfensPedidoProducto> listaProducto) throws Exception{
        boolean exito = false;
        
        String msgError = "";
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(idEmpleado, this.conn);
            EmpleadoInventarioRepartidorBO emInventarioRepartidorBO = new EmpleadoInventarioRepartidorBO(this.conn);
            EmpleadoInventarioRepartidor emInventarioRepartidorDto;
            EmpleadoInventarioRepartidorDaoImpl emInventarioRepartidorDao = new EmpleadoInventarioRepartidorDaoImpl(this.conn);
            ConceptoBO conceptoBO;
            
            int idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
            int productosEncontrados = 0;
            
            //Recorremos por primera vez para asegurar integridad
            //Solo en caso de que todos los productos tengan stock suficiente se daran de baja TODOS
            //  de lo contrario, no se dara de baja ninguno.
            for (SgfensPedidoProducto prodDto : listaProducto){
                EmpleadoInventarioRepartidor[] emInventarios = 
                        emInventarioRepartidorBO.findEmpleadoInventarioRepartidors(-1, idEmpleado, 0, 0, " AND ID_CONCEPTO = " + prodDto.getIdConcepto());
                conceptoBO = new ConceptoBO(prodDto.getIdConcepto(), this.conn);
                
                if (emInventarios.length>0){
                    emInventarioRepartidorDto = emInventarios[0];
                    
                     //Verificamos si se verifica el stock o no
                    EmpresaBO empresaBO = new EmpresaBO(this.conn);
                    EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl().findByPrimaryKey(empresaBO.getEmpresaMatriz(idEmpresa).getIdEmpresa());     
                    if (empresaPermisoAplicacionDto != null) {
                        if (empresaPermisoAplicacionDto.getRevisionCantidadProducto() == 1) {

                            double nuevoStock = emInventarioRepartidorDto.getCantidad() - prodDto.getCantidadEntregada();
                            if (nuevoStock < 0) {
                                msgError += "<ul>El stock del empleado, del articulo '" + conceptoBO.getNombreConceptoLegible() + "' es insuficiente para cubrir la operación."
                                        + "<br/>No. de Articulos disponibles del repartidor: " + emInventarioRepartidorDto.getCantidad();
                            } else {
                                productosEncontrados++;
                            }
                            
                        }
                    }
                }
            }
            
            //Se recorre por segunda ocasion
            //  Una vez que aseguramos la integridad y que todos los productos tienen stock suficiente
            //  procedemos a darlos de baja del inventario del empleado.
            if (productosEncontrados>0 && msgError.equals("") ){
                for (SgfensPedidoProducto prodDto : listaProducto){
                    
                    EmpleadoInventarioRepartidor[] emInventarios = 
                        emInventarioRepartidorBO.findEmpleadoInventarioRepartidors(-1, idEmpleado, 0, 0, " AND ID_CONCEPTO = " + prodDto.getIdConcepto());
                    conceptoBO = new ConceptoBO(prodDto.getIdConcepto(), this.conn);

                    if (emInventarios.length>0){
                        emInventarioRepartidorDto = emInventarios[0];
                        System.out.println("**" + emInventarioRepartidorDto.getCantidad());
                        System.out.println("**" + prodDto.getCantidadEntregada() );
                        double nuevoStock = emInventarioRepartidorDto.getCantidad() - prodDto.getCantidadEntregada();
                        if (nuevoStock<0){
                            msgError += "<ul>El stock del empleado, del articulo '" + conceptoBO.getNombreConceptoLegible() + "' es insuficiente para cubrir la operación."
                                        + "<br/>No. de Articulos disponibles del repartidor: " + emInventarioRepartidorDto.getCantidad();
                        }else{
                            //--Creamos registro de movimiento de almacen
                            //--Para inventario de empleado NO aplica
                            //...
                            
                            //Actualizamos registro único de inventario de empleado
                            emInventarioRepartidorDto.setCantidad(nuevoStock);
                            emInventarioRepartidorDao.update(emInventarioRepartidorDto.createPk(), emInventarioRepartidorDto);
                        }
                        
                    }else{
                        msgError += "<ul>El producto con id " + prodDto.getIdConcepto()+ "no existe en el stock del empleado repartidor, posiblemente fue restado en una sesion alterna.";
                    }

                }
            }
            
            if (msgError.trim().equals("")){
                exito = true;
            }else{
                exito = false;
                throw new Exception(msgError);
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        
        return exito;
    }
    
    
    
    public void actualizaProductosPedido(PedidoSesion pedidoSesion, UsuarioBO user){
        
         SgfensPedido pedidoDto = null;
         
         SgfensPedidoDaoImpl pedidoDao = new SgfensPedidoDaoImpl(this.conn);
        //Guardamos Productos Seleccionados
        for (ProductoSesion prodSesion:pedidoSesion.getListaProducto()){
            SgfensPedidoProducto prodPedido = new SgfensPedidoProducto();
            
            try{
                pedidoDto = pedidoDao.findByPrimaryKey(pedidoSesion.getIdPedido());
                
                
                Concepto conceptoDto = new ConceptoBO(prodSesion.getIdProducto(), this.conn).getConcepto();
                SGPedidoProductoBO prodPedidoBO = new SGPedidoProductoBO(this.conn);
                SgfensPedidoProducto conceptoOriginalDto = prodPedidoBO.findByIdConcepto(conceptoDto.getIdConcepto(), -1, -1, " ID_PEDIDO = " + pedidoSesion.getIdPedido())[0];
                
                prodPedido.setIdPedido(pedidoDto.getIdPedido());

                prodPedido.setCantidad(prodSesion.getCantidad());
                prodPedido.setIdConcepto(prodSesion.getIdProducto());
                prodPedido.setDescripcion(conceptoDto.getDescripcion());
                if (prodSesion.getDescripcionAlternativa()!=null){
                    if (prodSesion.getDescripcionAlternativa().trim().length()>0)
                        prodPedido.setDescripcion(prodSesion.getDescripcionAlternativa());
                }
                prodPedido.setDescuentoMonto(0);
                prodPedido.setDescuentoPorcentaje(0);
                prodPedido.setIdentificacion(conceptoDto.getIdentificacion());
                prodPedido.setPrecioUnitario(prodSesion.getPrecio());
                prodPedido.setSubtotal(prodSesion.getMonto());
                prodPedido.setUnidad(prodSesion.getUnidad());                
                prodPedido.setCostoUnitario(conceptoDto.getPrecioCompra());
                try{//Obtenemos tasa de comisión de empleado
                    EmpleadoBO empleadoBO = new EmpleadoBO(this.conn);
                    Empleado empleadoDto = empleadoBO.findEmpleadoByUsuario(user.getUser().getIdUsuarios());
                    prodPedido.setPorcentajeComisionEmpleado(empleadoDto.getPorcentajeComision());
                }catch(Exception e){
                    prodPedido.setPorcentajeComisionEmpleado(0);
                    System.out.println("Usuario, no tiene acceso como Empleado");                                       
                }
                prodPedido.setCantidadEntregada(conceptoOriginalDto.getCantidadEntregada() + prodSesion.getCantidadEntregada());
                prodPedido.setFechaEntrega(prodSesion.getFechaEntrega());
                prodPedido.setEstatus((short) prodSesion.getEstatus());
                
                if (prodSesion.getCantidadPeso()>0){
                //Datos para Granel
                    EmpleadoInventarioRepartidor empleadoInventarioRepartidorDto = new EmpleadoInventarioRepartidorBO( prodSesion.getIdInventarioEmpleado(), this.conn).getEmpleadoInventarioRepartidor();
                    if (empleadoInventarioRepartidorDto!=null)
                        prodPedido.setPesoUnitario(empleadoInventarioRepartidorDto.getPeso());
                    prodPedido.setCantidadPeso(prodSesion.getCantidadPeso());
                    prodPedido.setCantidadEntregadaPeso(conceptoOriginalDto.getCantidadEntregadaPeso() + prodSesion.getCantidadEntregadaPeso());
                    prodPedido.setPrecioUnitarioGranel(prodSesion.getPrecio());//El precio a Granel al que fue vendido
                    //prodPedido.setPrecioUnitario(conceptoDto.getPrecio());//El precio Normal, fijado al producto cuando se creo
                }

                new SgfensPedidoProductoDaoImpl(this.conn).update(prodPedido.createPk(),prodPedido);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    
    }   
    
    public int getCantidadBySgfensPedido(String filtroBusqueda){
        int cantidad = 0;
        try{
            //Connection conne = ResourceManager.getConnection();
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(ID_PEDIDO) as cantidad FROM SGFENS_PEDIDO WHERE " + filtroBusqueda);
            if(rs.next()){
                cantidad = rs.getInt("cantidad");
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return cantidad;
    }
    
    public static String generaFolioMovil(int idEmpresa){
        String folio = "";
        String empresa = StringManage.getExactString(""+idEmpresa, 3, '0', StringManage.FILL_DIRECTION_LEFT);
        try{Thread.sleep(100);}catch(Exception ex){}
        String folioConsecutivo = DateManage.getDateHourString();
        folio = "CL" + empresa + "-CON-" + folioConsecutivo;
        return folio;
    }
    
}