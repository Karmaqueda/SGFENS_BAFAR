/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.bo;

import com.google.gson.Gson;
import com.tsp.sct.bo.ClienteBO;
import com.tsp.sct.bo.EmpleadoBO;
import com.tsp.sct.bo.ImpuestoBO;
import com.tsp.sct.bo.SGCotizacionBO;
import com.tsp.sct.bo.SGProspectoBO;
import com.tsp.sct.bo.UsuarioBO;
import com.tsp.sct.dao.dto.Impuesto;
import com.tsp.sct.dao.dto.SgfensCotizacion;
import com.tsp.sct.dao.dto.SgfensCotizacionImpuesto;
import com.tsp.sct.dao.dto.SgfensCotizacionProducto;
import com.tsp.sct.dao.dto.SgfensPedidoImpuesto;
import com.tsp.sct.dao.jdbc.ImpuestoDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.dao.jdbc.SgfensCotizacionDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensCotizacionImpuestoDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensCotizacionProductoDaoImpl;
import com.tsp.sct.util.StringManage;
import com.tsp.sgfens.sesion.CotizacionSesion;
import com.tsp.sgfens.sesion.ImpuestoSesion;
import com.tsp.sgfens.sesion.ProductoSesion;
import com.tsp.sgfens.ws.request.ConsultaCotizacionesRequest;
import com.tsp.sgfens.ws.request.ConsultaPedidosRequest;
import com.tsp.sgfens.ws.request.CrearCotizacionRequest;
import com.tsp.sgfens.ws.request.EmpleadoDtoRequest;
import com.tsp.sgfens.ws.request.WsItemConceptoRequest;
import com.tsp.sgfens.ws.request.WsItemImpuestoRequest;
import com.tsp.sgfens.ws.response.ConsultaCotizacionesResponse;
import com.tsp.sgfens.ws.response.ConsultaPedidosResponse;
import com.tsp.sgfens.ws.response.CrearCotizacionResponse;
import com.tsp.sgfens.ws.response.WsItemCotizacion;
import com.tsp.sgfens.ws.response.WsItemPedidoConcepto;
import com.tsp.sgfens.ws.response.WsItemPedidoImpuesto;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * @author 578
 */
public class CotizacionWsBO {
        
    
    private final Gson gson = new Gson();
    private Connection conn = null;
    
    public Connection getConn() {
        if (this.conn==null){
            try {
                this.conn = ResourceManager.getConnection();
            } catch (SQLException ex) {}
        }else{
            try {
                if (this.conn.isClosed()){
                    this.conn = ResourceManager.getConnection();
                }
            } catch (SQLException ex) {}
        }
        return this.conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    
    
    public CrearCotizacionResponse crearCotizacionByEmpleado(String empleadoDtoRequestJSON, String crearPedidoRequestJSON){
        CrearCotizacionResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest;
        CrearCotizacionRequest crearCotizacionRequest;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            crearCotizacionRequest = gson.fromJson(crearPedidoRequestJSON, CrearCotizacionRequest.class);
            
            response = this.crearCotizacionByEmpleado(empleadoDtoRequest,crearCotizacionRequest);
        }catch(Exception ex){
            response = new CrearCotizacionResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }

    private CrearCotizacionResponse crearCotizacionByEmpleado(EmpleadoDtoRequest empleadoDtoRequest, CrearCotizacionRequest crearCotizacionRequest) {
        
        CrearCotizacionResponse response = new CrearCotizacionResponse();
        
        try{           
            
             String validacion = validaDatosRequest(empleadoDtoRequest, crearCotizacionRequest);             
             
             if (validacion.equals("")){
                 
                EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
                if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(),empleadoDtoRequest.getEmpleadoPassword())) {
                    int idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();     
                    
                    //Antes de procesar, verificamos si no fue registrado previamente,
                    // de acuerdo al dato: Folio Cotizacion Movil
                    SgfensCotizacionDaoImpl cotizacionDao = new SgfensCotizacionDaoImpl(getConn());
                    
                    try{
                        SgfensCotizacion[] cotizacionesExistentes = cotizacionDao.findWhereFolioCotizacionEquals(crearCotizacionRequest.getFolioCotizacionMovil());
                        
                        if (cotizacionesExistentes.length<=0){//si no existe la cotización                            
                             int idCliente = 0;
                             int idProspecto = 0;
                             
                             
                             idCliente = crearCotizacionRequest.getIdClienteReceptor();
                             idProspecto = crearCotizacionRequest.getIdProspectoReceptor();
                             
                             UsuarioBO usuarioBO = empleadoBO.getUsuarioBO();                                
                             CotizacionSesion cotizacionSesion = new CotizacionSesion();    
                             
                            //Para generar una nueva cotizacion en cada Guardar
                            //Deshabilitar en caso de querer actualizar en lugar de generar una nueva
                            cotizacionSesion.setIdCotizacion(0);
                             
                             try{
                                if (idCliente>0){
                                    cotizacionSesion.setCliente(new ClienteBO(idCliente,getConn()).getCliente());
                                }
                            }catch(Exception ex){ ex.printStackTrace();}

                            try{
                                if (idProspecto>0){
                                    cotizacionSesion.setProspecto(new SGProspectoBO(idProspecto,getConn()).getSgfensProspecto());
                                }
                            }catch(Exception ex){ ex.printStackTrace();}
                                                                        
                            
                            crearCotizacionRequest.setFechaCotizacion(crearCotizacionRequest.getFechaCotizacion());                            
                            cotizacionSesion.setComentarios("Cotizacion registrada desde aplicación móvil, Folio Cotización Movil: "+ crearCotizacionRequest.getFolioCotizacionMovil()
                            +". Registrado en servidor: " + new Date());        
                            /*cotizacionSesion.setTipo_moneda("MXN"); 
                            cotizacionSesion.setDescuento_tasa(0);
                            cotizacionSesion.setDescuento_motivo(""); */ 
                            cotizacionSesion.setFolioCotizacionMovil(crearCotizacionRequest.getFolioCotizacionMovil());
                            
                            
                            double latitud =0;
                            double longitud =0;
                            try{
                                latitud = Double.parseDouble(empleadoDtoRequest.getUbicacionLatitud());
                                longitud =  Double.parseDouble(empleadoDtoRequest.getUbicacionLongitud());
                            }catch(Exception ex){}

                            cotizacionSesion.setLatitud(latitud);
                            cotizacionSesion.setLongitud(longitud);
                            
                            //asignamos todos los conceptos
                            for (WsItemConceptoRequest wsConcepto : crearCotizacionRequest.getWsItemConceptoRequest()){
                                ProductoSesion nuevoProductoSesion = new ProductoSesion();
                                nuevoProductoSesion.setIdProducto(wsConcepto.getIdConcepto());
                                nuevoProductoSesion.setCantidad(wsConcepto.getCantidad()); 
                                nuevoProductoSesion.setMonto(wsConcepto.getMonto());
                                nuevoProductoSesion.setPrecio(wsConcepto.getPrecioUnitario());
                                nuevoProductoSesion.setUnidad(wsConcepto.getUnidad());
                                

                                cotizacionSesion.getListaProducto().add(nuevoProductoSesion);
                            }
                                                        
                            
                            //Verificamos si hay impuestos por agregar
                            if (crearCotizacionRequest.getListaImpuestos()!=null){

                                /**
                                * BORRAR CUANDO SE IMPLEMENTE CATALOGO DE IMPUESTOS EN MOVIL
                                */
                                if (crearCotizacionRequest.getListaImpuestos().length>0){
                                    ImpuestoBO impuestoBO = new ImpuestoBO(getConn());
                                    Impuesto impuestoIVA16 = impuestoBO.findImpuestoIVA16byEmpresa(idEmpresa);
                                    WsItemImpuestoRequest[] wsItemImpuestoRequestArrayTemp = new WsItemImpuestoRequest[1];
                                    wsItemImpuestoRequestArrayTemp[0] = new WsItemImpuestoRequest();
                                    wsItemImpuestoRequestArrayTemp[0].setDescripcion(impuestoIVA16.getDescripcion());
                                    wsItemImpuestoRequestArrayTemp[0].setIdImpuesto(impuestoIVA16.getIdImpuesto());
                                    wsItemImpuestoRequestArrayTemp[0].setImplocal(impuestoIVA16.getImpuestoLocal()==(short)1);
                                    wsItemImpuestoRequestArrayTemp[0].setNombre(impuestoIVA16.getNombre());
                                    wsItemImpuestoRequestArrayTemp[0].setPorcentaje(impuestoIVA16.getPorcentaje());
                                    wsItemImpuestoRequestArrayTemp[0].setTrasladado(impuestoIVA16.getTrasladado()==(short)1);

                                    crearCotizacionRequest.setListaImpuestos(wsItemImpuestoRequestArrayTemp);
                                }
                                //------------BORRAR HASTA AQUI

                                //asignamos todos los impuestos
                                for (WsItemImpuestoRequest wsImpuesto : crearCotizacionRequest.getListaImpuestos()){
                                    ImpuestoSesion nuevoImpuestSesion = new ImpuestoSesion();
                                    nuevoImpuestSesion.setIdImpuesto(wsImpuesto.getIdImpuesto());
                                    nuevoImpuestSesion.setNombre(wsImpuesto.getNombre());
                                    nuevoImpuestSesion.setDescripcion(wsImpuesto.getDescripcion());
                                    nuevoImpuestSesion.setPorcentaje(wsImpuesto.getPorcentaje());
                                    nuevoImpuestSesion.setTrasladado(wsImpuesto.isTrasladado());
                                    nuevoImpuestSesion.setImplocal(wsImpuesto.isImplocal());

                                    cotizacionSesion.getListaImpuesto().add(nuevoImpuestSesion);
                                }
                            }                            
                            
                            //Insertamos cotización -> reutilizamos metodo BO
                            SGCotizacionBO cotizacionBO = new SGCotizacionBO(getConn());
                            SgfensCotizacion cotizacionDto = cotizacionBO.creaActualizaCotizacion(cotizacionSesion, usuarioBO);
                            
                            //seteamos id cotizacion creada en respuesta
                            response.setFolioCotizacionCreado(cotizacionDto.getFolioCotizacion());
                            response.setIdObjetoCreado(cotizacionDto.getIdCotizacion());
                            
                            //Enviar notificación con PDF de cotizacion adjunto
                            if (crearCotizacionRequest.isEnviarCorreoCotizacion()){
                                cotizacionBO = new SGCotizacionBO(cotizacionDto,getConn());
                                try{
                                    File cotizacionPDF = cotizacionBO.guardarRepresentacionImpresa(usuarioBO);
                                    File[] adjuntos = new File[1];
                                    adjuntos[0] = cotizacionPDF;

                                    ArrayList<String> listaCorreos = new ArrayList<String>();
                                    listaCorreos.addAll(Arrays.asList(crearCotizacionRequest.getListaCorreosDestinatarios()));
                                    
                                    if(!listaCorreos.isEmpty()){
                                        cotizacionBO.enviaNotificacionNuevaCotizacion(listaCorreos,adjuntos,idEmpresa);
                                    }
                                    
                                }catch(Exception ex){
                                    System.out.print("No se pudo adjuntar y/o enviar la representación impresa de la cotización." + ex.toString());
                                    ex.printStackTrace();
                                }
                            }

                        }// if cotizaciones existentes                   

                    }catch(Exception ex){
                        //ex.printStackTrace();
                        throw new Exception("Error al intentar actualizar Cotizacion existente.");
                    }
                    
                } //login
             }else{ //validacion             
                response.setError(true);
                response.setNumError(901);
                response.setMsgError(validacion);
             }           
            
            
        }catch (Exception e) {
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error al intentar almacenar la cotización. La descripción del error es: " + e.toString());
            e.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        
        return response;
        
    }
    
    /**
     * Válida los datos Request para solicitar la creación de una cotizacion
     * @param empleadoDtoRequest Datos de acceso de empleado
     * @param crearPedidoRequest Datos e información de pedido y abono inicial
     * @return 
     */
    private String validaDatosRequest(EmpleadoDtoRequest empleadoDtoRequest, CrearCotizacionRequest crearCotizacionRequest){
        String msgError="";
        
        if (empleadoDtoRequest==null)
            msgError = "Datos de empleado nulos. Se requieren datos para autenticar operación.";
        
        if (crearCotizacionRequest.getIdClienteReceptor()<=0 && crearCotizacionRequest.getIdProspectoReceptor()<=0)
            msgError = "El cliente o prospecto específicado no es válido";
        
        if (crearCotizacionRequest.getWsItemConceptoRequest()==null){            
                msgError = "La lista de Productos/Conceptos es nula";
        }else{
            if (crearCotizacionRequest.getWsItemConceptoRequest().length<=0)
                msgError =  "La lista de Productos/Conceptos esta vacía. Al menos debe haber un producto.";
        }       
        
        return msgError;
    }   

    public ConsultaCotizacionesResponse getCotizacionesByEmpleado(String empleadoDtoRequestJson, String consultaCotizacionesRequestJson) { 
        
        ConsultaCotizacionesResponse response;
        
        EmpleadoDtoRequest empleadoDtoRequest = null;
        ConsultaCotizacionesRequest consultaCotizacionesRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJson, EmpleadoDtoRequest.class);
            consultaCotizacionesRequest = gson.fromJson(consultaCotizacionesRequestJson, ConsultaCotizacionesRequest.class);
            
            response = this.getCotizacionesByEmpleado(empleadoDtoRequest, consultaCotizacionesRequest);
        }catch(Exception ex){
            response = new ConsultaCotizacionesResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
          
    }

    private ConsultaCotizacionesResponse getCotizacionesByEmpleado(EmpleadoDtoRequest empleadoDtoRequest, ConsultaCotizacionesRequest consultaCotizacionesRequest) {
        
        ConsultaCotizacionesResponse response = new ConsultaCotizacionesResponse();
        
        try{
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                int idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();                
                int idUsuario = empleadoBO.getEmpleado().getIdUsuarios();
                
                //Si se encontro el registro buscamos su catalogo de cotizaciones
                if (idEmpresa > 0) {
                    String filtroAdicional = "";
                    if (consultaCotizacionesRequest.getListaIdCotizacionServerFiltro().size()>0){
                        filtroAdicional += " AND ID_COTIZACION IN (";
                        
                        for (int item : consultaCotizacionesRequest.getListaIdCotizacionServerFiltro())
                            filtroAdicional += item + ",";
                        
                        if (filtroAdicional.endsWith(","))
                            filtroAdicional =  filtroAdicional.substring(0, filtroAdicional.length()-1);
                        
                        filtroAdicional += " )";
                    }
                    if (consultaCotizacionesRequest.getListaFolioCotizacionMovilFiltro().size()>0){
                        filtroAdicional += " AND FOLIO_COTIZACION_MOVIL IN (";
                        
                        for (String item : consultaCotizacionesRequest.getListaFolioCotizacionMovilFiltro())
                            filtroAdicional += "'" + item + "',";
                        
                        if (filtroAdicional.endsWith(","))
                            filtroAdicional =  filtroAdicional.substring(0, filtroAdicional.length()-1);
                        
                        filtroAdicional += " )";
                    }
                    if (consultaCotizacionesRequest.isFiltroModificadoConsola()){
                        //Filtra los Pedidos del Empleado que fueron modificados en consola
                        // para hacer menor el tamaño de descarga
                        filtroAdicional += " AND IS_MODIFICADO_CONSOLA=1 ";
                    }//Si no se filtra por modificados, entonces se descargaran todos (o aplicando los filtros por ID y folio movil)
                    response.setError(false);
                    response.setWsItemCotizacion(this.getListaCotizaciones(idEmpresa, idUsuario, filtroAdicional));
                }
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            } else {
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        }catch (Exception e) {
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al consultar cotizaciones del usuario. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }      
        
        
        
        return response;
    }
    
    
    
    
    
    private List<WsItemCotizacion> getListaCotizaciones(int idEmpresa, int idUsuario, String filtroAdicional) {
        List<WsItemCotizacion> listaCotizaciones = new ArrayList<WsItemCotizacion>();
        
        SGCotizacionBO sgCotizacionBO = new SGCotizacionBO(getConn());
        try{
            String filtroBusqueda = "";
            
            filtroBusqueda += " AND ID_USUARIO_VENDEDOR = " + idUsuario;
            
            if (StringManage.getValidString(filtroAdicional).length()>0)
                filtroBusqueda += " " + filtroAdicional;
            SgfensCotizacion[] cotizacionesDtoArray = sgCotizacionBO.findCotizacion(-1, idEmpresa, 0, 0, filtroBusqueda);
            
            SgfensCotizacionDaoImpl cotizacionDao = new SgfensCotizacionDaoImpl(getConn());
            SgfensCotizacionProductoDaoImpl cotizacionProductoDao = new SgfensCotizacionProductoDaoImpl(getConn());
            SgfensCotizacionImpuestoDaoImpl cotizacionImpuestoDao = new SgfensCotizacionImpuestoDaoImpl(getConn());           
            ImpuestoDaoImpl impuestoDao = new ImpuestoDaoImpl(getConn());
            
            
            for (SgfensCotizacion cotizacionDto : cotizacionesDtoArray){
                WsItemCotizacion wsItemCotizacion = new WsItemCotizacion();
                                              
                //Asignamos valores directos 
                
                wsItemCotizacion.setIdCotizacion(cotizacionDto.getIdCotizacion());
                wsItemCotizacion.setIdEmpresa(cotizacionDto.getIdEmpresa());
                wsItemCotizacion.setIdCliente(cotizacionDto.getIdCliente());
                wsItemCotizacion.setIdProspecto(cotizacionDto.getIdProspecto());
                wsItemCotizacion.setConsecutivoCotizacion(cotizacionDto.getConsecutivoCotizacion());
                wsItemCotizacion.setFolioCotizacion(cotizacionDto.getFolioCotizacion());
                wsItemCotizacion.setFechaCotizacion(cotizacionDto.getFechaCotizacion());
                wsItemCotizacion.setTipoMoneda(cotizacionDto.getTipoMoneda());
                wsItemCotizacion.setTiempoEntregaDias(cotizacionDto.getTiempoEntregaDias());
                wsItemCotizacion.setComentarios(cotizacionDto.getComentarios());
                wsItemCotizacion.setDescuentoTasa(cotizacionDto.getDescuentoTasa());
                wsItemCotizacion.setDescuentoMonto(cotizacionDto.getDescuentoMonto());
                wsItemCotizacion.setSubtotal(cotizacionDto.getSubtotal());
                wsItemCotizacion.setTotal(cotizacionDto.getTotal());
                wsItemCotizacion.setDescuentoMotivo(cotizacionDto.getDescuentoMotivo());
                wsItemCotizacion.setIdEstatusCotizacion(cotizacionDto.getIdEstatusCotizacion());
                wsItemCotizacion.setFolioCotizacionMovil(cotizacionDto.getFolioCotizacionMovil());
                wsItemCotizacion.setLatitud(cotizacionDto.getLatitud());
                wsItemCotizacion.setLongitud(cotizacionDto.getLongitud());   
                
                
                //Asignamos objetos relacionados a la cotizacion
                { //Conceptos (productos / servicios)  
                    
                    SgfensCotizacionProducto[] cotizacionProductosDto = cotizacionProductoDao.findWhereIdCotizacionEquals(cotizacionDto.getIdCotizacion());
                    
                    for (SgfensCotizacionProducto cotizacionProdDto : cotizacionProductosDto) {
                        
                        WsItemPedidoConcepto wsCotConcepto = new WsItemPedidoConcepto();  //uso mismas clases que pedido
                        
                        wsCotConcepto.setCantidad(cotizacionProdDto.getCantidad());
                        wsCotConcepto.setDescripcion(cotizacionProdDto.getDescripcion());
                        wsCotConcepto.setDescuentoMonto(cotizacionProdDto.getDescuentoMonto());
                        wsCotConcepto.setDescuentoPorcentaje(cotizacionProdDto.getDescuentoPorcentaje());
                        wsCotConcepto.setIdConcepto(cotizacionProdDto.getIdConcepto());
                        wsCotConcepto.setIdentificacion(cotizacionProdDto.getIdentificacion());
                        wsCotConcepto.setPrecioUnitario(cotizacionProdDto.getPrecioUnitario());
                        wsCotConcepto.setSubtotal(cotizacionProdDto.getSubtotal());
                        wsCotConcepto.setUnidad(cotizacionProdDto.getUnidad());
                        /*
                        wsCotConcepto.setCostoUnitario(0);
                        wsCotConcepto.setPorcentajeComisionEmpleado(0);*/
                        
                        wsItemCotizacion.getWsItemCotizacionConcepto().add(wsCotConcepto);
                    }
                }
                
                { //Impuestos
                    SgfensCotizacionImpuesto[] cotizacionImpuestosDto = cotizacionImpuestoDao.findWhereIdCotizacionEquals(cotizacionDto.getIdCotizacion());
                    for (SgfensCotizacionImpuesto cotizacionImpDto : cotizacionImpuestosDto ){
                        
                        WsItemPedidoImpuesto wsCotImpuesto = new WsItemPedidoImpuesto(); //uso mismas clases que pedido
                        
                        Impuesto impuestoDto = impuestoDao.findByPrimaryKey(cotizacionImpDto.getIdImpuesto());
                        if (impuestoDto!=null){
                            wsCotImpuesto.setDescripcion(impuestoDto.getDescripcion());
                            wsCotImpuesto.setIdEstatus(impuestoDto.getIdEstatus());
                            wsCotImpuesto.setImpuestoLocal(impuestoDto.getImpuestoLocal());
                            wsCotImpuesto.setNombre(impuestoDto.getNombre());
                            wsCotImpuesto.setPorcentaje(impuestoDto.getPorcentaje());
                            wsCotImpuesto.setTraslado(impuestoDto.getTrasladado());

                            wsItemCotizacion.getWsItemCotizacionImpuesto().add(wsCotImpuesto);
                        }
                    }                    
                }
                
                
                try{
                    //Después de que se consulta, deja de marcarse como modificado 
                    cotizacionDto.setIsModificadoConsola((short)0);
                    cotizacionDao.update(cotizacionDto.createPk(), cotizacionDto);
                }catch(Exception ex){
                    
                }
                
                listaCotizaciones.add(wsItemCotizacion);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return listaCotizaciones;
    }
    
}
