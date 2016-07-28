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
import com.tsp.sct.dao.exceptions.SgfensCotizacionImpuestoDaoException;
import com.tsp.sct.dao.exceptions.SgfensCotizacionProductoDaoException;
import com.tsp.sct.dao.exceptions.SgfensCotizacionServicioDaoException;
import com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensCotizacionDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensCotizacionImpuestoDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensCotizacionProductoDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensCotizacionServicioDaoImpl;
import com.tsp.sgfens.sesion.CotizacionSesion;
import com.tsp.sgfens.sesion.ImpuestoSesion;
import com.tsp.sgfens.sesion.ProductoSesion;
import com.tsp.sgfens.sesion.ServicioSesion;
import com.tsp.sct.util.GenericValidator;
import com.tsp.sct.mail.TspMailBO;
import com.tsp.sct.pdf.PdfITextUtil;
import com.tsp.sct.pdf.Translator;
import com.tsp.sct.util.DateManage;
import com.tsp.sgfens.report.ReportBO;
import com.tsp.sgfens.report.pdf.EventPDF;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author ISCesarMartinez
 */
public class SGCotizacionBO {
    
    private SgfensCotizacion cotizacion = null;

    public SgfensCotizacion getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(SgfensCotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }

    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SGCotizacionBO(Connection conn) {
        this.conn = conn;
    }

    public SGCotizacionBO(int idCotizacion, Connection conn) {
        this.conn = conn;
        try{
            this.cotizacion = new SgfensCotizacionDaoImpl(this.conn).findByPrimaryKey(idCotizacion);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public SGCotizacionBO(SgfensCotizacion cotizacion, Connection conn) {
        this.cotizacion = cotizacion;
        this.conn = conn;
    }
    
    /**
     * Realiza una búsqueda por ID SgfensCotizacion en busca de
     * coincidencias
     * @param idSgfensCotizacion ID De la cotizacion para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar cotizacion, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensCotizacion
     */
    public SgfensCotizacion[] findCotizacion(int idSgfensCotizacion, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SgfensCotizacion[] cotizacionDto = new SgfensCotizacion[0];
        SgfensCotizacionDaoImpl cotizacionDao = new SgfensCotizacionDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSgfensCotizacion>0){
                sqlFiltro ="ID_COTIZACION=" + idSgfensCotizacion + " AND ";
            }else{
                sqlFiltro ="ID_COTIZACION>0 AND";
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
            
            cotizacionDto = cotizacionDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_COTIZACION DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return cotizacionDto;
    }
    
    /**
     * Recupera todos los datos de una cotización desde la base de datos
     * y lo convierte a un objeto legible en sesion.
     * @return objeto CotizacionSesion
     */
    public CotizacionSesion getSessionFromCotizacionDB(){
        CotizacionSesion cotizacionSesion = null;
        
        try{
            Cliente clienteDto = null;
            SgfensProspecto prospectoDto =null;
            Empleado vendedorDto = null;
            ArrayList<ProductoSesion> listaProductoDto = new ArrayList<ProductoSesion>();
            ArrayList<ServicioSesion> listaServicioDto = new ArrayList<ServicioSesion>();
            ArrayList<ImpuestoSesion> listaImpuestoDto = new ArrayList<ImpuestoSesion>();
            
            /*recuperamos deatos de cliente / prospecto */
            clienteDto = new ClienteBO(this.cotizacion.getIdCliente(),this.conn).getCliente();
            prospectoDto = new SGProspectoBO(this.cotizacion.getIdProspecto(),this.conn).getSgfensProspecto();
            vendedorDto = new EmpleadoBO(this.conn).findEmpleadoByUsuario(this.cotizacion.getIdUsuarioVendedor());
            
            /* Recuperamos listado de productos */
            SgfensCotizacionProducto[] listaProductosCompra = getProductos_Datos();
            ProductoSesion productoSesion = null;
            for (SgfensCotizacionProducto itemProducto:listaProductosCompra){
                try{
                    productoSesion = new ProductoSesion();
                    productoSesion.setCantidad(itemProducto.getCantidad());
                    productoSesion.setIdProducto(itemProducto.getIdConcepto());
                    productoSesion.setPrecio(itemProducto.getPrecioUnitario());
                    productoSesion.setMonto(itemProducto.getSubtotal());
                    productoSesion.setUnidad(itemProducto.getUnidad());

                    listaProductoDto.add(productoSesion);
                }catch(Exception ex){ex.printStackTrace();}
            }
            
            /* Recuperamos listado de servicios */
            SgfensCotizacionServicio[] listaServiciosCotizacion = getServicios_Datos();
            ServicioSesion servicioSesion = null;
            for (SgfensCotizacionServicio itemServicio : listaServiciosCotizacion){
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
            SgfensCotizacionImpuesto[] listaImpuestosCotizacion = getImpuestos_Datos();
            ImpuestoSesion impuestoSesion = null;
            Impuesto impuestoDto = null;
            for (SgfensCotizacionImpuesto itemImpuesto : listaImpuestosCotizacion){
                try{
                    impuestoDto = new ImpuestoBO(itemImpuesto.getIdImpuesto(),this.conn).getImpuesto();
                    
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
            cotizacionSesion = new CotizacionSesion();
            cotizacionSesion.setIdCotizacion(this.cotizacion.getIdCotizacion());
            cotizacionSesion.setComentarios(this.cotizacion.getComentarios());
            cotizacionSesion.setDescuento_tasa(this.cotizacion.getDescuentoTasa());
            cotizacionSesion.setCliente(clienteDto);
            cotizacionSesion.setProspecto(prospectoDto);
            cotizacionSesion.setListaProducto(listaProductoDto);
            cotizacionSesion.setListaServicio(listaServicioDto);
            cotizacionSesion.setListaImpuesto(listaImpuestoDto);
            cotizacionSesion.setEmpleadoVendedor(vendedorDto);
            
        }catch(Exception ex){
            System.out.println("Ha ocurrido un error al intentar generar un "
                    + "objeto de sesion a partir de datos de Base de Datos de "
                    + "una cotización: " + ex.toString());
            ex.printStackTrace();
        }
        
        return cotizacionSesion;
    }
    
    public SgfensCotizacionProducto[] getProductos_Datos(){
        SgfensCotizacionProducto[] prods = new SgfensCotizacionProducto[0];
        try{
            prods = new SgfensCotizacionProductoDaoImpl(this.conn).findWhereIdCotizacionEquals(this.cotizacion.getIdCotizacion());
        }catch(Exception ex){
            System.out.println("Ocurrio un error al consultar de BD los productos de una Cotizacion." + ex.toString());
            ex.printStackTrace();
        }
        return prods;
    }
    
    public SgfensCotizacionServicio[] getServicios_Datos(){
        SgfensCotizacionServicio[] servicios = new SgfensCotizacionServicio[0];
        try{
            servicios = new SgfensCotizacionServicioDaoImpl(this.conn).findWhereIdCotizacionEquals(this.cotizacion.getIdCotizacion());
        }catch(Exception ex){
            System.out.println("Ocurrio un error al consultar de BD los servicios de una Cotizacion." + ex.toString());
            ex.printStackTrace();
        }
        return servicios;
    }
    
    public SgfensCotizacionImpuesto[] getImpuestos_Datos(){
        SgfensCotizacionImpuesto[] impuestos = new SgfensCotizacionImpuesto[0];
        try{
            impuestos = new SgfensCotizacionImpuestoDaoImpl(this.conn).findWhereIdCotizacionEquals(this.cotizacion.getIdCotizacion());
        }catch(Exception ex){
            System.out.println("Ocurrio un error al consultar de BD los impuestos de una Cotizacion." + ex.toString());
            ex.printStackTrace();
        }
        return impuestos;
    }
    
    
    public SgfensCotizacion creaActualizaCotizacion(CotizacionSesion cotizacionSesion, UsuarioBO user) throws Exception{
        SgfensCotizacion cotizacionDto = null;
        SgfensCotizacionDaoImpl cotizacionDao = new SgfensCotizacionDaoImpl(this.conn);
        SgfensCotizacionPk cotizacionPk = null;
        
        boolean edicion = false;
        if (cotizacionSesion.getIdCotizacion()<=0){
        //Crear
            
            //Creamos registro principal de Solicitud de Compra
            cotizacionDto = new SgfensCotizacion();
            cotizacionDto.setComentarios(cotizacionSesion.getComentarios());
            //cotizacionDto.setConsecutivoCotizacion(0);
            cotizacionDto.setDescuentoMonto(cotizacionSesion.getDescuentoImporte().doubleValue());
            cotizacionDto.setDescuentoMotivo(cotizacionSesion.getDescuento_motivo());
            cotizacionDto.setDescuentoTasa(cotizacionSesion.getDescuento_tasa());
            cotizacionDto.setFechaCotizacion(new Date());
            //cotizacionDto.setFolioCotizacion("");
            cotizacionDto.setIdCliente(cotizacionSesion.getCliente()!=null?cotizacionSesion.getCliente().getIdCliente():0);
            cotizacionDto.setIdEmpresa(user.getUser().getIdEmpresa());
            cotizacionDto.setIdProspecto(cotizacionSesion.getProspecto()!=null?cotizacionSesion.getProspecto().getIdProspecto():0);
            cotizacionDto.setIdUsuarioVendedor(user.getUser().getIdUsuarios());
            cotizacionDto.setSubtotal(cotizacionSesion.calculaSubTotal().doubleValue());
            cotizacionDto.setTiempoEntregaDias(0);
            cotizacionDto.setTipoMoneda(cotizacionSesion.getTipo_moneda());
            cotizacionDto.setTotal(cotizacionSesion.calculaTotal().doubleValue());
            cotizacionDto.setLatitud(cotizacionSesion.getLatitud());
            cotizacionDto.setLongitud(cotizacionSesion.getLongitud());
            cotizacionDto.setFolioCotizacionMovil(cotizacionSesion.getFolioCotizacionMovil());
            
            //Buscamos el último consecutivo de cotización para la empresa y aumentamos 1
            int consecutivoCotizacionEmpresa=1;
            try{
                SgfensCotizacion lastCotizacionConsec = cotizacionDao.findByDynamicWhere("ID_EMPRESA=" + user.getUser().getIdEmpresa() + " ORDER BY CONSECUTIVO_COTIZACION DESC ", null)[0];
                consecutivoCotizacionEmpresa = lastCotizacionConsec.getConsecutivoCotizacion() + 1;
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
            cotizacionDto.setConsecutivoCotizacion(consecutivoCotizacionEmpresa);
            cotizacionDto.setFolioCotizacion("C" + consecutivoCotizacionEmpresa);
            
            try{
                cotizacionPk = cotizacionDao.insert(cotizacionDto);
            }catch(Exception ex){
                ex.printStackTrace();
                throw new Exception("Error al crear registro principal de Cotización. " + ex.toString());
            }
            
        }else{
        //Actualizar
            try{
                cotizacionDto = cotizacionDao.findByPrimaryKey(cotizacionSesion.getIdCotizacion());
                cotizacionDto.setComentarios(cotizacionSesion.getComentarios());
                //cotizacionDto.setConsecutivoCotizacion(0);
                cotizacionDto.setDescuentoMonto(cotizacionSesion.getDescuentoImporte().doubleValue());
                cotizacionDto.setDescuentoMotivo(cotizacionSesion.getDescuento_motivo());
                cotizacionDto.setDescuentoTasa(cotizacionSesion.getDescuento_tasa());
                cotizacionDto.setFechaCotizacion(new Date());
                //cotizacionDto.setFolioCotizacion("");
                cotizacionDto.setIdCliente(cotizacionSesion.getCliente()!=null?cotizacionSesion.getCliente().getIdCliente():0);
                cotizacionDto.setIdEmpresa(user.getUser().getIdEmpresa());
                cotizacionDto.setIdProspecto(cotizacionSesion.getProspecto()!=null?cotizacionSesion.getProspecto().getIdProspecto():0);
                cotizacionDto.setIdUsuarioVendedor(user.getUser().getIdUsuarios());
                cotizacionDto.setSubtotal(cotizacionSesion.calculaSubTotal().doubleValue());
                cotizacionDto.setTiempoEntregaDias(0);
                cotizacionDto.setTipoMoneda(cotizacionSesion.getTipo_moneda());
                cotizacionDto.setTotal(cotizacionSesion.calculaTotal().doubleValue());
                
                cotizacionDao.update(cotizacionDto.createPk(), cotizacionDto);
                edicion = true;
                
                //Eliminamos de bd los elementos borrados en sesion, para que la información sea integra
                eliminaItemsCotizacion(cotizacionSesion);
                
            }catch(Exception ex){
                ex.printStackTrace();
                throw new Exception("Error al actualizar registro principal de Cotización. " + ex.toString());
            }
        }
        
        //Eliminamos de la BD todos los elementos que correspondan a la Cotización para insertar los mas actuales
        
        //Guardamos Productos Seleccionados
        for (ProductoSesion prodSesion:cotizacionSesion.getListaProducto()){
            SgfensCotizacionProducto prodCotizacion = new SgfensCotizacionProducto();
            
            try{
                Concepto conceptoDto = new ConceptoBO(prodSesion.getIdProducto(), this.conn).getConcepto();
                
                prodCotizacion.setIdCotizacion(cotizacionDto.getIdCotizacion());

                prodCotizacion.setCantidad(prodSesion.getCantidad());
                prodCotizacion.setIdConcepto(prodSesion.getIdProducto());
                prodCotizacion.setDescripcion(conceptoDto.getDescripcion());
                prodCotizacion.setDescuentoMonto(0);
                prodCotizacion.setDescuentoPorcentaje(0);
                prodCotizacion.setIdentificacion(conceptoDto.getIdentificacion());
                prodCotizacion.setPrecioUnitario(prodSesion.getPrecio());
                prodCotizacion.setSubtotal(prodSesion.getMonto());
                prodCotizacion.setUnidad(prodSesion.getUnidad());

                new SgfensCotizacionProductoDaoImpl(this.conn).insert(prodCotizacion);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
        //Guardamos Servicios Seleccionados
        for (ServicioSesion servicioSesion:cotizacionSesion.getListaServicio()){
            SgfensCotizacionServicio servicioCotizacion = new SgfensCotizacionServicio();
            
            try{
                Servicio servicioDto = new ServicioBO(servicioSesion.getIdServicio(),this.conn).getServicio();
                
                
                servicioCotizacion.setIdCotizacion(cotizacionDto.getIdCotizacion());

                servicioCotizacion.setCantidad(servicioSesion.getCantidad());
                servicioCotizacion.setIdServicio(servicioSesion.getIdServicio());
                servicioCotizacion.setDescripcion(servicioDto.getDescripcion());
                servicioCotizacion.setDescuentoMonto(0);
                servicioCotizacion.setDescuentoPorcentaje(0);
                servicioCotizacion.setIdentificacion(servicioDto.getSku());
                servicioCotizacion.setPrecioUnitario(servicioSesion.getPrecio());
                servicioCotizacion.setSubtotal(servicioSesion.getMonto());
                servicioCotizacion.setUnidad(servicioSesion.getUnidad());

                new SgfensCotizacionServicioDaoImpl(this.conn).insert(servicioCotizacion);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
        //Guardamos Impuestos Seleccionados
        for (ImpuestoSesion impuestoSesion:cotizacionSesion.getListaImpuesto()){
            SgfensCotizacionImpuesto impuestoCotizacion = new SgfensCotizacionImpuesto();
            
            try{              
                impuestoCotizacion.setIdCotizacion(cotizacionDto.getIdCotizacion());
                impuestoCotizacion.setIdImpuesto(impuestoSesion.getIdImpuesto());

                new SgfensCotizacionImpuestoDaoImpl(this.conn).insert(impuestoCotizacion);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
        //Consumo de Creditos Operacion
        if (!edicion){ //solo si es un registro nuevo
            try{
                BitacoraCreditosOperacionBO bcoBO = new BitacoraCreditosOperacionBO(getConn());
                bcoBO.registraDescuento(user.getUser(), 
                        BitacoraCreditosOperacionBO.CONSUMO_ACCION_WS_REGISTRO_COTIZACION, 
                        null, cotizacionDto.getIdCliente(), cotizacionDto.getIdProspecto(), cotizacionDto.getTotal(), 
                        "Registro Cotizacion", null, true);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
        return cotizacionDto;
    }
    
    
    
    /**
     * Elimina de BD todos los items de una Solicitud de Compra que ya no existen
     * por que fueron eliminados en la sesion actual
     */
    public void eliminaItemsCotizacion(CotizacionSesion cotizacionSesion){
        if (cotizacionSesion!=null){
            int idCotizacion = cotizacionSesion.getIdCotizacion();
            
            if(idCotizacion>0){
                
                //Productos
                {
                    SgfensCotizacionProducto[] cotizacionProdArray = new SgfensCotizacionProducto[0];
                    SgfensCotizacionProductoDaoImpl cotizacionProdDao = new SgfensCotizacionProductoDaoImpl(this.conn);
                    try{
                        cotizacionProdArray = cotizacionProdDao.findWhereIdCotizacionEquals(idCotizacion);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    for (SgfensCotizacionProducto itemProductoBD:cotizacionProdArray){
                        try {
                            cotizacionProdDao.delete(itemProductoBD.createPk());
                        } catch (SgfensCotizacionProductoDaoException ex) {
                            ex.printStackTrace();
                        }
                    }   
                }
            
                //Servicios
                {
                    SgfensCotizacionServicio[] cotizacionServicioArray = new SgfensCotizacionServicio[0];
                    SgfensCotizacionServicioDaoImpl cotizacionServicioDao = new SgfensCotizacionServicioDaoImpl(this.conn);
                    try{
                        cotizacionServicioArray = cotizacionServicioDao.findWhereIdCotizacionEquals(idCotizacion);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    for (SgfensCotizacionServicio itemServicioBD:cotizacionServicioArray){
                        try {
                            cotizacionServicioDao.delete(itemServicioBD.createPk());
                        } catch (SgfensCotizacionServicioDaoException ex) {
                            ex.printStackTrace();
                        }
                    }   
                }
        
                //Impuestos
                {
                    SgfensCotizacionImpuesto[] cotizacionImpuestoArray = new SgfensCotizacionImpuesto[0];
                    SgfensCotizacionImpuestoDaoImpl cotizacionImpuestoDao = new SgfensCotizacionImpuestoDaoImpl(this.conn);
                    try{
                        cotizacionImpuestoArray = cotizacionImpuestoDao.findWhereIdCotizacionEquals(idCotizacion);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    for (SgfensCotizacionImpuesto itemImpuestoBD:cotizacionImpuestoArray){
                        try {
                            cotizacionImpuestoDao.delete(itemImpuestoBD.createPk());
                        } catch (SgfensCotizacionImpuestoDaoException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                
            }
            
        }
    }
    
    
    /**
     * Envia un correo a los destinatarios específicados
     * con la cotización
     * @return Cadena en caso de error
     */
    public String enviaNotificacionNuevaCotizacion(ArrayList<String> destinatarios, File[] adjuntos, int idEmpresa){
        
        EmpresaPermisoAplicacion empresaPermisoDto = null;
        try{
            EmpresaPermisoAplicacionDaoImpl  empresaPermisoDao = new EmpresaPermisoAplicacionDaoImpl();
            empresaPermisoDto = empresaPermisoDao.findWhereIdEmpresaEquals(idEmpresa)[0];
        }catch(Exception e){e.printStackTrace();}
        
        String respuesta ="";
        
        if (this.cotizacion!=null){
            try {
                TspMailBO mailBO = new TspMailBO();
                
                if(empresaPermisoDto.getAccesoSgfensPretomovil()==1){
                    mailBO.setConfigurationMovilpyme();
                    mailBO.addMessageMovilpyme("<b>Buen día!</b> "
                        + "<br/> Se ha capturado o actualizado la  Cotización con folio: " + this.cotizacion.getFolioCotizacion(),1);
                } else{
                    mailBO.setConfiguration();            
                    mailBO.addMessage("<b>Buen día!</b> "
                        + "<br/> Se ha capturado o actualizado la  Cotización con folio: " + this.cotizacion.getFolioCotizacion(),1);
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
                
                mailBO.send("Cotizaciones");
                
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
            EventPDF eventPDF = new EventPDF(doc, user, ReportBO.COTIZACION_REPRESENTACION_IMPRESA,fileImageLogo);
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
                    SgfensProspecto prospectoDto = null;
                    DatosUsuario datosUsuarioVendedor = null;
                    
                    try{
                        if (this.cotizacion.getIdCliente()>0)
                            clienteDto = new ClienteBO(this.cotizacion.getIdCliente(),this.conn).getCliente();
                        if (this.cotizacion.getIdProspecto()>0)
                            prospectoDto = new SGProspectoBO(this.cotizacion.getIdProspecto(),this.conn).getSgfensProspecto();
                        if (this.cotizacion.getIdUsuarioVendedor()>0)
                            datosUsuarioVendedor = new UsuarioBO(this.cotizacion.getIdUsuarioVendedor()).getDatosUsuario();
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    
                    
                    //Datos informativos generales
                        PdfPTable tInfoGeneral = new PdfPTable(1);
                        tInfoGeneral.setTotalWidth(160);
                        tInfoGeneral.setLockedWidth(true);


                        obj.agregaCelda(tInfoGeneral,letraNueveBold,Color.lightGray, "Folio Cotización", new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tInfoGeneral,letraNueveBoldRojo, this.cotizacion.getFolioCotizacion(), new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                    
                        //Doble salto de línea
                        obj.agregaCelda(tInfoGeneral,letra14Bold,"", new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tInfoGeneral,letra14Bold,"", new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                    
                        obj.agregaCelda(tInfoGeneral,letraNueveBold,Color.lightGray, "Fecha Cotización", new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tInfoGeneral,letraNueve, DateManage.dateToStringEspanol(this.cotizacion.getFechaCotizacion()), new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tInfoGeneral,letraNueveBold,Color.lightGray, "Vendedor", new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tInfoGeneral,letraNueve, datosUsuarioVendedor!=null?(datosUsuarioVendedor.getNombre() +" " + datosUsuarioVendedor.getApellidoPat()) :"Sin vendedor asignado" , new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                    
                        //Pintamos datos informativos Generales en la esquina superior derecha
                        PdfContentByte cb = writer.getDirectContent();
                        tInfoGeneral.writeSelectedRows(0, -1,doc.right()-180, doc.top() + 100, cb);
                    
                    //Datos de Cliente/Prospecto
                        PdfPTable tCliente = new PdfPTable(2);
                        tCliente.setTotalWidth(550);
                        tCliente.setLockedWidth(true);

                        obj.agregaCelda(tCliente,letraNueveBold,Color.lightGray, this.cotizacion.getIdCliente()>0?"Cliente":"Prospecto", new boolean[]{false,false,false,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],2);

                        tAux = new PdfPTable(1);
                        tAux.setTotalWidth(275);
                        tAux.setLockedWidth(true);

                        try{
                            obj.agregaCelda(tAux,letraOcho, "Nombre o razón social: " + (clienteDto!=null?clienteDto.getRazonSocial():(prospectoDto!=null?prospectoDto.getRazonSocial():"")), new boolean[]{false,true,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                            obj.agregaCelda(tAux,letraOcho, "R.F.C.: " + (clienteDto!=null?clienteDto.getRfcCliente():""), new boolean[]{false,true,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                            obj.agregaCelda(tAux,letraOcho, "Contacto: " + (clienteDto!=null?(clienteDto.getContacto()!=null?clienteDto.getContacto():"") :(prospectoDto!=null?(prospectoDto.getContacto()!=null?prospectoDto.getContacto():"") :"")), new boolean[]{false,true,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                            obj.agregaCelda(tAux,letraOcho, "\t\t\t" + (clienteDto!=null?clienteDto.getCorreo() + " \t, " + clienteDto.getTelefono():(prospectoDto!=null?prospectoDto.getCorreo() + " \t, " + prospectoDto.getTelefono():"")), new boolean[]{false,true,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
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
                        obj.agregaCelda(tInfoGeneral,letraOcho, "Folio Cotización: " + (this.cotizacion.getFolioCotizacion()), new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);

                        obj.agregaTabla(mainTable, tInfoGeneral, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                        */
                //FIN Cabecera (Datos Generales)-----------------
            
                //DOBLE SALTO DE LÍNEA
                obj.agregaCelda(mainTable, letraOcho, null, " ",
                        new boolean[]{false, false, false, false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0], 1);
                obj.agregaCelda(mainTable, letraOcho, null, " ",
                        new boolean[]{false, false, false, false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0], 1);
                    
                //Datos de Cotizacion (Productos/Servicios)--------------------
                
                    int colsDetalles = 5;
                    PdfPTable tDetalles = new PdfPTable(colsDetalles);//6);
                    tDetalles.setTotalWidth(550);
                    tDetalles.setWidths(new int[]{190,90,90,90,90});
                    tDetalles.setLockedWidth(true);



                    /*CABECERA*/
                    /*obj.agregaCelda(tDetalles, letraNueveBoldAzul, null, "Estado de Cuenta del Cliente con ID:" + idCliente,
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{1, 1, 1, 1}, tableSize);*/

                    obj.agregaCelda(tDetalles, letraNueveBold, Color.lightGray, "Producto/Servicio",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                    obj.agregaCelda(tDetalles, letraNueveBold, Color.lightGray, "Unidad",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                    obj.agregaCelda(tDetalles, letraNueveBold, Color.lightGray, "Cantidad",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                    obj.agregaCelda(tDetalles, letraNueveBold, Color.lightGray, "Precio Unitario",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                    /*obj.agregaCelda(tDetalles, letraNueveBold, null, "Descuento (%)",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);*/
                    obj.agregaCelda(tDetalles, letraNueveBold, Color.lightGray, "Importe",
                            new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 1);
                    /*FIN DE CABECERA*/
                    

                    //Listado de Productos
                    for (SgfensCotizacionProducto item:this.getProductos_Datos()){

                        if (item!=null){

                            //Producto
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

                    //Listado de Servicios
                    for (SgfensCotizacionServicio item:this.getServicios_Datos()){

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
            
                //FIN Datos de Cotizacion (Productos/Servicios)----------------
            
                //DOBLE SALTO DE LÍNEA
                        obj.agregaCelda(mainTable, letraOcho, null, " ",
                                new boolean[]{false, false, false, false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0], 1);
                        obj.agregaCelda(mainTable, letraOcho, null, " ",
                                new boolean[]{false, false, false, false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0], 1);
            
                //Datos de Totales ------------------------------
                    
                    Translator numALetra = new Translator();
                    String totalConLetra = "";
                    String moneda = this.cotizacion.getTipoMoneda();
                    try{
                        if (moneda!=null){
                            if (!"".equals(moneda)){
                                numALetra.setNombreMoneda(moneda);
                            }
                        }

                        totalConLetra = numALetra.getStringOfNumber(this.cotizacion.getTotal());
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

                    obj.agregaCelda(tTipoMoneda, letraOcho, null, this.cotizacion.getTipoMoneda(),
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
                    obj.agregaCelda(tAux,letraOcho, "" + new DecimalFormat("$###,###,###,##0.00").format(this.cotizacion.getSubtotal()), new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 0, new int[0],1);

                    obj.agregaCelda(tAux,letraOchoBold, "DESCUENTO" , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                    obj.agregaCelda(tAux,letraOcho, "" + new DecimalFormat("$###,###,###,##0.00").format(this.cotizacion.getDescuentoMonto()) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 0, new int[0],1);

                    boolean impuestosLocales =false;
                    for (SgfensCotizacionImpuesto item:this.getImpuestos_Datos()){
                        impuestoDto = new ImpuestoBO(item.getIdImpuesto(), this.conn).getImpuesto();

                        if (impuestoDto!=null){
                            //Verificamos que no sea un impuesto local, esos se detallaran mas adelante
                            if (impuestoDto.getImpuestoLocal()!=(short)1){
                                double montoImpuesto = new BigDecimal((this.getCotizacion().getSubtotal()-this.getCotizacion().getDescuentoMonto())*(impuestoDto.getPorcentaje()/100)).setScale(2, RoundingMode.HALF_UP).doubleValue();

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

                        for (SgfensCotizacionImpuesto item:this.getImpuestos_Datos()){
                            impuestoDto = new ImpuestoBO(item.getIdImpuesto(), this.conn).getImpuesto();

                            if (impuestoDto!=null){
                                //Verificamos que no sea un impuesto local, esos se detallaran mas adelante
                                if (impuestoDto.getImpuestoLocal()==(short)1){
                                    double montoImpuesto = new BigDecimal((this.getCotizacion().getSubtotal()-this.getCotizacion().getDescuentoMonto())*(impuestoDto.getPorcentaje()/100)).setScale(2, RoundingMode.HALF_UP).doubleValue();

                                    obj.agregaCelda(tAux,letraOchoBold, (impuestoDto.getTrasladado()!=(short)1?"RETENCIÓN ":"") + impuestoDto.getNombre() + " " + impuestoDto.getPorcentaje()+"%", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                                    obj.agregaCelda(tAux,letraOcho, "" + new DecimalFormat("$###,###,###,##0.00").format(montoImpuesto) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 0, new int[0],1);
                                }
                            }
                        }

                    }
                            
                    obj.agregaCelda(tAux,letraNueveBold, "TOTAL" , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM, 50, new int[0],1);
                    obj.agregaCelda(tAux,letraNueveBoldAzul, "" + new DecimalFormat("$###,###,###,##0.00").format(this.cotizacion.getTotal()) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_BOTTOM, 50, new int[0],1);

                    obj.agregaTabla(tTotal, tAux, new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);

                    /*Comentarios Adicionales*/
                    obj.agregaCelda(tTotal, letraNueveBold, Color.lightGray, "Comentarios cotización",
                                new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 2);    
                    obj.agregaCelda(tTotal, letraOcho, null, this.cotizacion.getComentarios(),
                                new boolean[]{true, true, true, true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{5, 5, 5, 5}, 2);

                    obj.agregaTabla(mainTable, tTotal, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                   
                   //FIN Datos de Totales --------------------------
            //FIN DE CONTENIDO --------
            
            //Añadimos tabla principal construida al documento
            doc.add(mainTable);
            mainTable.flushContent();
            
        }catch(Exception ex){
           msgError = "No se ha podido generar la representación impresa de la Cotización en formato PDF:<br/>" + ex.toString();
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
        
        Empresa empresaDto = new EmpresaBO(this.cotizacion.getIdEmpresa(),this.conn).getEmpresa(); 
        Cliente clienteDto = new ClienteBO(this.cotizacion.getIdCliente(),this.conn).getCliente(); 
        SgfensProspecto prospectoDto =  new SGProspectoBO(this.cotizacion.getIdProspecto(),this.conn).getSgfensProspecto();
                
        String rutaRepositorio = "";
        if (clienteDto!=null)
            rutaRepositorio = configuration.getApp_content_path() + empresaDto.getRfc() + "/cotizaciones/"+ clienteDto.getRfcCliente();
        if (prospectoDto!=null)
            rutaRepositorio = configuration.getApp_content_path() + empresaDto.getRfc() + "/cotizaciones/prospectos";
        
        String nombreArchivo = "Cotizacion_" + empresaDto.getRfc() + "_" + this.cotizacion.getFolioCotizacion() + "_" + (new Date()).getTime() + ".pdf";

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
    
}