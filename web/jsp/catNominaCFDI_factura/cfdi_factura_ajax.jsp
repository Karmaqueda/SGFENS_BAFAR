<%-- 
    Document   : cfdi_factura_ajax
    Created on : 21-dic-2012, 16:27:45
    Author     : ISCesarMartinez
--%>
<%@page import="com.tsp.sct.bo.NominaRegistroPatronalBO"%>
<%@page import="com.tsp.sct.dao.dto.NominaRegistroPatronal"%>
<%@page import="java.util.List"%>
<%@page import="com.tsp.sct.dao.dao.NominaComprobanteDescripcionDao"%>
<%@page import="com.tsp.sct.bo.NominaAutomaticaGenerarComprobanteFiscaBO"%>
<%@page import="com.tsp.sgfens.sesion.NominaComprobanteIdsSesion"%>
<%@page import="com.tsp.sct.dao.jdbc.NominaComprobanteDescripcionPercepcionDeduccionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.NominaComprobanteDescripcionPercepcionDeduccion"%>
<%@page import="com.tsp.sct.dao.jdbc.NominaComprobanteDescripcionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.NominaComprobanteDescripcion"%>
<%@page import="com.tsp.sct.bo.NominaTipoDeduccionBO"%>
<%@page import="com.tsp.sct.dao.dto.NominaTipoDeduccion"%>
<%@page import="com.tsp.sct.bo.NominaTipoPercepcionBO"%>
<%@page import="com.tsp.sct.dao.dto.NominaTipoPercepcion"%>
<%@page import="com.tsp.sct.bo.NominaDeduccionBO"%>
<%@page import="com.tsp.sgfens.sesion.DeduccionSesion"%>
<%@page import="com.tsp.sct.dao.jdbc.NominaDeduccionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.NominaDeduccion"%>
<%@page import="com.tsp.sct.bo.NominaPercepcionBO"%>
<%@page import="com.tsp.sct.dao.jdbc.NominaPercepcionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.NominaPercepcion"%>
<%@page import="com.tsp.sgfens.sesion.PercepcionSesion"%>
<%@page import="com.tsp.sct.dao.dto.NominaEmpleado"%>
<%@page import="com.tsp.sct.bo.NominaEmpleadoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.bo.TipoComprobanteBO"%>
<%@page import="com.tsp.sct.dao.jdbc.ComprobanteFiscalDaoImpl"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensPedidoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedido"%>
<%@page import="com.tsp.sct.bo.SGPedidoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.FoliosDaoImpl"%>
<%@page import="java.io.File"%>
<%@page import="com.tsp.sct.cfdi.Cfd32BO"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.dao.dto.CertificadoDigital"%>
<%@page import="com.tsp.sct.bo.CertificadoDigitalBO"%>
<%@page import="com.tsp.sct.dao.dto.Ubicacion"%>
<%@page import="com.tsp.sct.bo.UbicacionBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.FormaPago"%>
<%@page import="com.tsp.sct.cfdi.CFDI32Factory"%>
<%@page import="mx.bigdata.sat.cfdi.schema.v32.Comprobante"%>
<%@page import="com.tsp.sct.dao.dto.TipoDeMoneda"%>
<%@page import="com.tsp.sct.bo.TipoDeMonedaBO"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.tsp.sct.dao.dto.Folios"%>
<%@page import="com.tsp.sct.bo.FoliosBO"%>
<%@page import="com.tsp.sct.bo.FormaPagoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.TipoPagoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.TipoPago"%>
<%@page import="com.tsp.sct.bo.EstatusComprobanteBO"%>
<%@page import="com.tsp.sgfens.sesion.FormatoSesion"%>
<%@page import="com.tsp.sct.dao.dto.ComprobanteFiscal"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.bo.ServicioBO"%>
<%@page import="com.tsp.sgfens.sesion.ServicioSesion"%>
<%@page import="com.tsp.sct.dao.jdbc.ServicioDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Servicio"%>
<%@page import="java.math.RoundingMode"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.tsp.sct.dao.jdbc.ImpuestoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Impuesto"%>
<%@page import="com.tsp.sgfens.sesion.ImpuestoSesion"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.bo.ComprobanteFiscalBO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.tsp.sgfens.sesion.ProductoSesion"%>
<%@page import="com.tsp.sct.dao.jdbc.ConceptoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.tsp.sgfens.sesion.ComprobanteFiscalSesion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="cfdiSesion" scope="session" class="com.tsp.sgfens.sesion.ComprobanteFiscalSesion"/>
<jsp:useBean id="nominaIDsSesion" scope="session" class="com.tsp.sgfens.sesion.NominaComprobanteIdsSesion"/>

<%
    String msgError="";
    /*
    *   Modos:
    *       -1  =  No se eligio (ERROR)
    *        1  =  Seleccionar y Recuperar datos de Producto (recuperar info de 1 producto)
    *        2  =  Agregar Producto
    *        3  =  Reiniciar lista Productos (borrar todos)
    *        4  =  Quitar producto de lista (borrar 1)
    *        5  =  Recargar Lista de Productos
    *        6  =  Reiniciar ComprobanteFiscal (Borrar todo)
    *        7  =  Reconstruir Sesion desde BD (Sesion <- BD)
    *        8  =  Modificar Producto
    *
    *        9  =  Agregar Impuesto
    *        10 =  Quitar Impuesto de lista (borrar 1)
    *        11 =  Recargar Lista de Impuestos
    * 
    *        12 =  Calcular/Aplicar Descuento
    * 
    *        13 =  Seleccionar y Recuperar datos de Servicio
    *        14 =  Agregar Servicio
    *        15 =  Reiniciar lista Servicio (borrar todos)
    *        16 =  Quitar Servicio de lista (borrar 1)
    *        17 =  Recargar lista de Servicios
    *        18 =  Modificar Servicio 
    *        
    *        19 =  Guardar ComprobanteFiscal (Sesion -> BD)  
    * 
    *        20 =  Recargar Select Clientes
    *        21 = Seleccionar Cliente (obtener datos)
    * 
    *        22 = Cancelar CFDI
    
             23 = Seleccionar y Recuperar datos de Percepcion
             24 = agregar percepcion    
             25 = Quitar percepcion de lista (borrar 1)
    
             26 =  Seleccionar y Recuperar datos de Dediccion
             27 = agregar deduccion
             28 = Agregar Impuesto ISR para nómina
             29 = Reconstruir Nomina en Sesion desde BD (Sesion <- BD)
             30 = Recargar Lista de Percepciones
             31 = Recargar Lista de Deduccion
             
             32 = Carga ID's comprobantes a generar nómina automática
             33 = Quitar ID's comprobantes a generar nómina automática
    
    */
    int mode=-1;
    try{
        mode = Integer.parseInt(request.getParameter("mode"));
    }catch(NumberFormatException e){}
    boolean recargarListaProductos=false;
    boolean recargarListaPercepcions=false;
    boolean recargarListaDeduccions= false;
    boolean recargarListaServicios=false;
    boolean recargarListaImpuestos=false;
    
    boolean recargarSelectEmpleados=false;
    int idCliente = -1;
    
    ComprobanteFiscalSesion comprobanteFiscalAux = cfdiSesion;
    
    Gson gson = new Gson();
    
    GenericValidator genericValidator = new GenericValidator();
    
    Configuration appConfig = new Configuration();
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
     * Procesamiento
     */
    if (mode==-1){
        msgError = "No se eligio una acción correcta. Reintente el llenado de los datos.";
    }else if (mode==1){
        //MODO  1  =  Seleccionar y Recuperar datos de Producto
        
        //Parametros
        int idProducto = -1;
        try{
            idProducto = Integer.parseInt(request.getParameter("id_producto_select"));
        }catch(NumberFormatException e){}
        
        //Validacion
        if (idProducto>0){
            Concepto producto = new Concepto();
            ConceptoDaoImpl conceptoDaoImpl = new ConceptoDaoImpl(user.getConn());

            try{
               producto = conceptoDaoImpl.findByPrimaryKey(idProducto);
            }catch(Exception ex){
                msgError = "Ocurrio un error al consultar el producto elegido. " + ex.toString();
            }

            if (msgError.equals("")){
                //Usamos JSON para retornar el objeto respuesta
                String jsonOutput = gson.toJson(producto);
                out.print("<!--EXITO-->"+jsonOutput);
            }
        }else{
            msgError="No se selecciono un producto válido.";
        }
        
    }else if (mode==2){
        //Modo 2 = agregar producto
        
        //Parametros
        int idProducto = -1;
        double cantidad =-1;
        double precio =-1;
        double monto=-1;
        String unidad = request.getParameter("producto_unidad") != null ? request.getParameter("producto_unidad") : "SERVICIO";
        String productoDescripcion = request.getParameter("productoDescripcion") != null ? request.getParameter("productoDescripcion") : "";
        try{ idProducto = Integer.parseInt(request.getParameter("producto_id")); }catch(Exception e){}
        try{ cantidad = Double.parseDouble(request.getParameter("producto_cantidad")); }catch(Exception e){}
        try{ precio = Double.parseDouble(request.getParameter("producto_precio")); }catch(Exception e){}
        try{ monto = Double.parseDouble(request.getParameter("producto_monto")); }catch(Exception e){}
        
        //Validaciones
        GenericValidator gc = new GenericValidator();
        if (idProducto<=0)
            msgError = "<ul>Producto no específicado o incorrecto";
        if (cantidad<0)
            msgError = "<ul>Cantidad invalida";
        if (monto<0)
            msgError = "<ul>Monto invalido";
        if (precio<0)
            msgError = "<ul>Precio invalido";
        if (unidad.trim().length()<=0)
            msgError = "<ul>Seleccione Unidad valida";
        if(!gc.isValidString(productoDescripcion, 1, 300))
            msgError = "<ul>El dato 'descripción' es requerido. Máximo 300 caracteres.";
        
        if (msgError.equals("")){
            Concepto productoCompra = null;
            ConceptoDaoImpl conceptoDaoImpl = new ConceptoDaoImpl(user.getConn());

            try{
               productoCompra = conceptoDaoImpl.findByPrimaryKey(idProducto);
            }catch(Exception ex){
                msgError = "Ocurrio un error al consultar el producto elegido. " + ex.toString();
            }
            
            try{
                if (cfdiSesion==null){
                    cfdiSesion = new ComprobanteFiscalSesion();
                }

                ProductoSesion nuevoProductoCompraSesion = new ProductoSesion();
                nuevoProductoCompraSesion.setCantidad(cantidad);
                nuevoProductoCompraSesion.setIdProducto(idProducto);
                nuevoProductoCompraSesion.setMonto(monto);
                nuevoProductoCompraSesion.setPrecio(precio);
                nuevoProductoCompraSesion.setUnidad(unidad);
                if(!productoDescripcion.trim().equals(""))
                    nuevoProductoCompraSesion.setDescripcionAlternativa(productoDescripcion);
                
                //Verificamos si se consulta el stock o no
                /*EmpresaBO empresaBO = new EmpresaBO(user.getConn());
                EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa());     
                if (empresaPermisoAplicacionDto!=null){
                    if(empresaPermisoAplicacionDto.getRevisionCantidadProducto() == 1){                        
                        if (productoCompra!=null){
                             BigDecimal numArticulosDisponibles = (new BigDecimal(productoCompra.getNumArticulosDisponibles())).setScale(2, RoundingMode.HALF_UP);
                             BigDecimal cantidad2 = (new BigDecimal(cantidad)).setScale(2, RoundingMode.HALF_UP);
                             BigDecimal stockTotal = numArticulosDisponibles.subtract(cantidad2);
                            
                            double nuevoStock = stockTotal.doubleValue();                
                            if (nuevoStock<0){
                                msgError+="<ul>El stock del articulo es insuficiente para cubrir la operación.<br/>No. de Articulos disponibles: " + productoCompra.getNumArticulosDisponibles();
                            }
                        }
                    }
                }*/
                
                if (productoCompra!=null && msgError.equals("") ){
                    boolean coincidenciaPrevia=false;
                    for (ProductoSesion itemProducto:cfdiSesion.getListaProducto()){
                        if (itemProducto.getIdProducto()==productoCompra.getIdConcepto()){
                            //Ya se había seleccionado este producto
                            coincidenciaPrevia=true;
                            break;
                        }
                    }
                    if (!coincidenciaPrevia){
                        cfdiSesion.getListaProducto().add(nuevoProductoCompraSesion);
                    }else{
                        msgError +="<ul> El producto ya fue seleccionado previamente. Modifique los valores o elimine el producto del listado.";
                    }
                        
                }
                
                request.getSession().setAttribute("cfdiSesion", cfdiSesion);
            }catch(Exception ex){
                msgError += "Ocurrio un error al recuperar los productos elegidos previamente en la sesion. " + ex.toString();
            }
            
            recargarListaProductos=true;
        }
        
    }else if (mode==3){
        //Reiniciar lista de productos
        if (cfdiSesion==null)
            cfdiSesion = new ComprobanteFiscalSesion();
        cfdiSesion.setListaProducto(new ArrayList<ProductoSesion>());
        request.getSession().setAttribute("cfdiSesion", cfdiSesion);
        
        recargarListaProductos=true;
    }else if (mode==4){
        //Quitar producto de lista
        if (cfdiSesion==null)
            cfdiSesion = new ComprobanteFiscalSesion();
        int index_lista_producto=-1;
        try{ index_lista_producto = Integer.parseInt(request.getParameter("index_lista_producto")); }catch(Exception e){}
        if (index_lista_producto<0)
            msgError = "<ul>Producto inválido. Si persiste el problema intente recargar esta página.";
        
        if (msgError.equals("")){
            try{
                cfdiSesion.getListaProducto().remove(index_lista_producto);
            }catch(Exception ex){
                msgError+="El producto especificado no se encontro en el listado en sesion. Intente de nuevo.";
            }
            request.getSession().setAttribute("cfdiSesion", cfdiSesion);

            recargarListaProductos=true;
        }
    }else if (mode==5){
        //Recargar Lista de Productos
        if (cfdiSesion==null)
            cfdiSesion = new ComprobanteFiscalSesion();
        request.getSession().setAttribute("cfdiSesion", cfdiSesion);
        recargarListaProductos=true;
    }else if (mode==6){
        //6 = Reiniciar ComprobanteFiscal (Borrar todo)
        cfdiSesion = new ComprobanteFiscalSesion();
        request.getSession().setAttribute("cfdiSesion", cfdiSesion);
    }else if (mode==7){
        //Reconstruir Sesion desde BD
        
        //Parametros
        int id_comprobanteFiscal = -1;
        try{ id_comprobanteFiscal = Integer.parseInt(request.getParameter("id_comprobanteFiscal")); }catch(Exception e){}
        
        //Procesamiento
        if (id_comprobanteFiscal>=0){
            ComprobanteFiscalBO cfdiSesionBO = new ComprobanteFiscalBO(id_comprobanteFiscal,user.getConn());
            cfdiSesion = cfdiSesionBO.getSessionFromComprobanteFiscalDB();
            request.getSession().setAttribute("cfdiSesion", cfdiSesion);
        }else{
            msgError += "<ul> No se específico el identificador (ID) del Comprobante Fiscal a recuperar. Intente de nuevo.";
        }
        
    }else if(mode==8){
        //Modo 8 = Modificar Producto
        if (cfdiSesion==null)
            cfdiSesion = new ComprobanteFiscalSesion();
        
        //Parametros
        int idProducto = -1;
        double cantidad =-1;
        double monto=-1;
        double precio = -1;
        int index_lista_producto=-1;
        
        try{ idProducto = Integer.parseInt(request.getParameter("producto_id")); }catch(Exception e){}
        try{ cantidad = Double.parseDouble(request.getParameter("producto_cantidad")); }catch(Exception e){}
        try{ monto = Double.parseDouble(request.getParameter("producto_monto")); }catch(Exception e){}
        try{ precio = Double.parseDouble(request.getParameter("producto_precio")); }catch(Exception e){}
        try{ index_lista_producto = Integer.parseInt(request.getParameter("index_lista_producto")); }catch(Exception e){}
        String unidad = request.getParameter("producto_unidad") != null ? request.getParameter("producto_unidad") : "No Aplica";
        
        
        //Validaciones
        if (idProducto<=0)
            msgError = "<ul>Producto no específicado o incorrecto";
        if (cantidad<0)
            msgError = "<ul>Cantidad invalida";
        if (monto<0)
            msgError = "<ul>Monto invalido";
        if (precio<0)
            msgError = "<ul>Precio no específicado o incorrecto";
        if (index_lista_producto<0)
            msgError = "<ul>Producto inválido. Si persiste el problema intente recargar esta página.";
        
        if (msgError.equals("")){
            
            monto = precio * cantidad;
            
            try{
                ProductoSesion nuevoProductoSesion = new ProductoSesion();
                nuevoProductoSesion.setCantidad(cantidad);
                nuevoProductoSesion.setPrecio(precio);
                nuevoProductoSesion.setIdProducto(idProducto);
                nuevoProductoSesion.setMonto(monto);
                nuevoProductoSesion.setUnidad(unidad);
                
                cfdiSesion.getListaProducto().set(index_lista_producto, nuevoProductoSesion);
            }catch(Exception ex){
                msgError+="El producto especificado no se pudo actualizar. Intente de nuevo.";
            }
            request.getSession().setAttribute("cfdiSesion", cfdiSesion);
            
            if (msgError.equals(""))
                out.print("Producto actualizado exitosamente");
        }
    }else if(mode==9){
        //Modo 9 = Agregar Impuesto
        
        //Parametros
        int idImpuesto = -1;
        try{ idImpuesto = Integer.parseInt(request.getParameter("impuesto_id")); }catch(Exception e){}
        
        //Validaciones
        if (idImpuesto<=0)
            msgError = "<ul>Impuesto no específicado o incorrecto";
        
        if (msgError.equals("")){
            Impuesto impuestoDto = null;
            ImpuestoDaoImpl impuestoDaoImpl = new ImpuestoDaoImpl(user.getConn());

            try{
               impuestoDto = impuestoDaoImpl.findByPrimaryKey(idImpuesto);
            }catch(Exception ex){
                msgError = "Ocurrio un error al consultar el impuesto elegido. " + ex.toString();
            }
            
            try{
                if (cfdiSesion==null){
                    cfdiSesion = new ComprobanteFiscalSesion();
                }

                ImpuestoSesion nuevoImpuestoCompraSesion = new ImpuestoSesion();
                nuevoImpuestoCompraSesion.setIdImpuesto(idImpuesto);
                nuevoImpuestoCompraSesion.setNombre(impuestoDto.getNombre());
                nuevoImpuestoCompraSesion.setDescripcion(impuestoDto.getDescripcion());
                nuevoImpuestoCompraSesion.setPorcentaje(impuestoDto.getPorcentaje());
                nuevoImpuestoCompraSesion.setTrasladado(impuestoDto.getTrasladado()==(short)1?true:false);
                nuevoImpuestoCompraSesion.setImplocal(impuestoDto.getImpuestoLocal()==(short)1?true:false);
                
                if (impuestoDto!=null){
                    boolean coincidenciaPrevia=false;
                    for (ImpuestoSesion itemImpuesto:cfdiSesion.getListaImpuesto()){
                        if (itemImpuesto.getIdImpuesto()==impuestoDto.getIdImpuesto()){
                            //Ya se había seleccionado este impuesto
                            coincidenciaPrevia=true;
                            break;
                        }
                    }
                    if (!coincidenciaPrevia){
                        cfdiSesion.getListaImpuesto().add(nuevoImpuestoCompraSesion);
                    }else{
                        msgError +="<ul> El impuesto ya fue seleccionado previamente.";
                    }
                        
                }
                
                request.getSession().setAttribute("cfdiSesion", cfdiSesion);
            }catch(Exception ex){
                msgError += "Ocurrio un error al recuperar los impuestos elegidos previamente en la sesion. " + ex.toString();
            }
            
            recargarListaImpuestos=true;
        }
        
    }else if(mode==10){
        //Modo 10 = Quitar Impuesto
        if (cfdiSesion==null)
            cfdiSesion = new ComprobanteFiscalSesion();
        int index_lista_impuesto=-1;
        try{ index_lista_impuesto = Integer.parseInt(request.getParameter("index_lista_impuesto")); }catch(Exception e){}
        if (index_lista_impuesto<0)
            msgError = "<ul>Impuesto inválido. Si persiste el problema intente recargar esta página.";
        
        if (msgError.equals("")){
            try{
                cfdiSesion.getListaImpuesto().remove(index_lista_impuesto);
            }catch(Exception ex){
                msgError+="El impuesto especificado no se encontro en el listado en sesion. Intente de nuevo.";
            }
            request.getSession().setAttribute("cfdiSesion", cfdiSesion);

            recargarListaImpuestos=true;
        }
        
    }else if(mode==11){
        //Modo 11 = Recargar Lista Impuestos
        if (cfdiSesion==null)
            cfdiSesion = new ComprobanteFiscalSesion();
        request.getSession().setAttribute("cfdiSesion", cfdiSesion);
        recargarListaImpuestos=true;
    }else if(mode==12){
        //Modo 12 = Calcular/Aplicar Descuento
        if (cfdiSesion==null)
            cfdiSesion = new ComprobanteFiscalSesion();
        String descuento_tasa = request.getParameter("descuento_tasa") != null ? request.getParameter("descuento_tasa") : "0";
        
        BigDecimal descuento_tasa_bigd = new BigDecimal(descuento_tasa).setScale(2, RoundingMode.HALF_UP);
        if (descuento_tasa_bigd.doubleValue()<0)
            msgError = "<ul>Descuento inválido. El descuento debe ser mayor o igual a 0 en porcentaje;";
        
        if (msgError.equals("")){
            try{                
                cfdiSesion.setDescuento_tasa(descuento_tasa_bigd.doubleValue());
                
                BigDecimal subtotal = cfdiSesion.calculaSubTotal();
                BigDecimal descuentoPorcentaje = new BigDecimal( cfdiSesion.getDescuento_tasa() / 100 ).setScale(2, RoundingMode.HALF_UP);

                BigDecimal descuentoImporte = subtotal.multiply(descuentoPorcentaje).setScale(2, RoundingMode.HALF_UP);
                
                cfdiSesion.setDescuento_importe(descuentoImporte.doubleValue());
            }catch(Exception ex){
                msgError+="Ocurrio un error al aplicar el descuento. Intente de nuevo.";
            }
            request.getSession().setAttribute("cfdiSesion", cfdiSesion);
        }
        
        if (msgError.equals("")){
            out.print("<!--EXITO-->"+cfdiSesion.getDescuentoImporte());
        }
    }else if(mode==13){
        //Modo 13 = Seleccionar y Recuperar datos de Servicio
        
        //Parametros
        int idServicio = -1;
        try{
            idServicio = Integer.parseInt(request.getParameter("id_servicio_select"));
        }catch(NumberFormatException e){}
        
        //Validacion
        if (idServicio>0){
            Servicio servicio = new Servicio();
            ServicioDaoImpl servicioDaoImpl = new ServicioDaoImpl(user.getConn());

            try{
               servicio = servicioDaoImpl.findByPrimaryKey(idServicio);
            }catch(Exception ex){
                msgError = "Ocurrio un error al consultar el servicio elegido. " + ex.toString();
            }

            if (msgError.equals("")){
                //Usamos JSON para retornar el objeto respuesta
                String jsonOutput = gson.toJson(servicio);
                out.print("<!--EXITO-->"+jsonOutput);
                //System.out.println("\n\n"+jsonOutput);
            }
        }else{
            msgError="No se selecciono un servicio válido.";
        }
        
    }else if(mode==14){
        //Modo 14 = Agregar Servicio
        
        //Parametros
        int idServicio = -1;
        double cantidad =-1;
        double precio =-1;
        double monto=-1;
        String unidad = request.getParameter("servicio_unidad") != null ? request.getParameter("servicio_unidad") : "SERVICIO";
        String servicioDescripcion = request.getParameter("servicioDescripcion") != null ? request.getParameter("servicioDescripcion") : "";
        try{ idServicio = Integer.parseInt(request.getParameter("servicio_id")); }catch(Exception e){}
        try{ cantidad = Double.parseDouble(request.getParameter("servicio_cantidad")); }catch(Exception e){}
        try{ precio = Double.parseDouble(request.getParameter("servicio_precio")); }catch(Exception e){}
        try{ monto = Double.parseDouble(request.getParameter("servicio_monto")); }catch(Exception e){}
        
        //Validaciones
        if (idServicio<=0)
            msgError = "<ul>Servicio no específicado o incorrecto";
        if (cantidad<0)
            msgError = "<ul>Cantidad invalida";
        if (monto<0)
            msgError = "<ul>Monto invalido";
        if (precio<0)
            msgError = "<ul>Precio invalido";
        
        if (msgError.equals("")){
            Servicio servicioCompra = null;
            ServicioDaoImpl servicioDaoImpl = new ServicioDaoImpl(user.getConn());

            try{
               servicioCompra = servicioDaoImpl.findByPrimaryKey(idServicio);
            }catch(Exception ex){
                msgError = "Ocurrio un error al consultar el servicio elegido. " + ex.toString();
            }
            
            try{
                if (cfdiSesion==null){
                    cfdiSesion = new ComprobanteFiscalSesion();
                }

                ServicioSesion nuevoServicioCompraSesion = new ServicioSesion();
                nuevoServicioCompraSesion.setCantidad(cantidad);
                nuevoServicioCompraSesion.setIdServicio(idServicio);
                nuevoServicioCompraSesion.setMonto(monto);
                nuevoServicioCompraSesion.setPrecio(precio);
                nuevoServicioCompraSesion.setUnidad(unidad);
                //nuevoServicioCompraSesion.setDescripcionAlternativa(servicioDescripcion);
                
                if (servicioCompra!=null){
                    boolean coincidenciaPrevia=false;
                    for (ServicioSesion itemServicio:cfdiSesion.getListaServicio()){
                        if (itemServicio.getIdServicio()==servicioCompra.getIdServicio()){
                            //Ya se había seleccionado este servicio
                            coincidenciaPrevia=true;
                            break;
                        }
                    }
                    if (!coincidenciaPrevia){
                        cfdiSesion.getListaServicio().add(nuevoServicioCompraSesion);
                    }else{
                        msgError +="<ul> El servicio ya fue seleccionado previamente. Modifique los valores o elimine el servicio del listado.";
                    }
                        
                }
                
                request.getSession().setAttribute("cfdiSesion", cfdiSesion);
            }catch(Exception ex){
                msgError += "Ocurrio un error al recuperar los servicios elegidos previamente en la sesion. " + ex.toString();
            }
            
            recargarListaServicios=true;
        }
        
    }else if(mode==15){
        //Modo 15 = Reiniciar lista Servicio (borrar todos)
        if (cfdiSesion==null)
            cfdiSesion = new ComprobanteFiscalSesion();
        cfdiSesion.setListaServicio(new ArrayList<ServicioSesion>());
        request.getSession().setAttribute("cfdiSesion", cfdiSesion);
        
        recargarListaServicios=true;
    }else if(mode==16){
        //Modo 16 = Quitar Servicio de lista (borrar 1)
        
        if (cfdiSesion==null)
            cfdiSesion = new ComprobanteFiscalSesion();
        int index_lista_servicio=-1;
        try{ index_lista_servicio = Integer.parseInt(request.getParameter("index_lista_servicio")); }catch(Exception e){}
        if (index_lista_servicio<0)
            msgError = "<ul>Servicio inválido. Si persiste el problema intente recargar esta página.";
        
        if (msgError.equals("")){
            try{
                cfdiSesion.getListaServicio().remove(index_lista_servicio);
            }catch(Exception ex){
                msgError+="El servicio especificado no se encontro en el listado en sesion. Intente de nuevo.";
            }
            request.getSession().setAttribute("cfdiSesion", cfdiSesion);

            recargarListaServicios=true;
        }
        
    }else if(mode==17){
        //Modo 17 = Recargar lista de Servicios
        
        if (cfdiSesion==null)
            cfdiSesion = new ComprobanteFiscalSesion();
        request.getSession().setAttribute("cfdiSesion", cfdiSesion);
        recargarListaServicios=true;
        
    }else if(mode==18){
        //Modo 18 = Modificar Servicio
        
        if (cfdiSesion==null)
            cfdiSesion = new ComprobanteFiscalSesion();
        
        //Parametros
        int idServicio = -1;
        double cantidad =-1;
        double monto=-1;
        double precio = -1;
        int index_lista_servicio=-1;
        
        try{ idServicio = Integer.parseInt(request.getParameter("servicio_id")); }catch(Exception e){}
        try{ cantidad = Double.parseDouble(request.getParameter("servicio_cantidad")); }catch(Exception e){}
        try{ monto = Double.parseDouble(request.getParameter("servicio_monto")); }catch(Exception e){}
        try{ precio = Double.parseDouble(request.getParameter("servicio_precio")); }catch(Exception e){}
        try{ index_lista_servicio = Integer.parseInt(request.getParameter("index_lista_servicio")); }catch(Exception e){}
        String unidad = request.getParameter("servicio_unidad") != null ? request.getParameter("servicio_unidad") : "No Aplica";
        
        
        //Validaciones
        if (idServicio<=0)
            msgError = "<ul>Servicio no específicado o incorrecto";
        if (cantidad<0)
            msgError = "<ul>Cantidad invalida";
        if (monto<0)
            msgError = "<ul>Monto invalido";
        if (precio<0)
            msgError = "<ul>Precio no específicado o incorrecto";
        if (index_lista_servicio<0)
            msgError = "<ul>Servicio inválido. Si persiste el problema intente recargar esta página.";
        
        if (msgError.equals("")){
            
            monto = precio * cantidad;
            
            try{
                ServicioSesion nuevoServicioSesion = new ServicioSesion();
                nuevoServicioSesion.setCantidad(cantidad);
                nuevoServicioSesion.setPrecio(precio);
                nuevoServicioSesion.setIdServicio(idServicio);
                nuevoServicioSesion.setMonto(monto);
                nuevoServicioSesion.setUnidad(unidad);
                
                cfdiSesion.getListaServicio().set(index_lista_servicio, nuevoServicioSesion);
            }catch(Exception ex){
                msgError+="El servicio especificado no se pudo actualizar. Intente de nuevo.";
            }
            request.getSession().setAttribute("cfdiSesion", cfdiSesion);
            
            if (msgError.equals(""))
                out.print("Servicio actualizado exitosamente");
        }
        
    }else if (mode==19){
        //19 =  Guardar ComprobanteFiscal (Sesion -> BD)  
        
        boolean enviarCorreo = false;
        
        //variable de control de que se incremento el folio
        boolean incrementoFolio = false;
        
        //Parametros
        String tipoComprobante = "egreso"; //ingreso = factura normal, egreso = salida de dinero.s
        int idTipoComprobante = 40; // 2 = factura
        
        Date fecha_pago = null;  
        idCliente = -1;
        int idFolios = -1;
        int idFormaPago = -1;
        Date parcialidadFechaOrig = null;
        int tipoMonedaInt = -1;
        double tipoCambio = 1;
        String parcialidadFormat = null; //Parcialidad 1 de X
        int idTipoPago = -1;
        
        double total = 0;
        
        int idRegistroPatronal = -1;
        
        //tipoComprobante = request.getParameter("tipo_comprobante")!=null?new String(request.getParameter("tipo_comprobante").getBytes("ISO-8859-1"),"UTF-8"):"ingreso";
        try{ idTipoComprobante = Integer.parseInt(request.getParameter("id_tipo_comprobante")); }catch(Exception e){}
        String comentarios = request.getParameter("comentarios")!=null?new String(request.getParameter("comentarios").getBytes("ISO-8859-1"),"UTF-8"):"";
        //String tipo_moneda = request.getParameter("tipo_moneda")!=null?new String(request.getParameter("tipo_moneda").getBytes("ISO-8859-1"),"UTF-8"):"MXN";
        try{ tipoMonedaInt = Integer.parseInt(request.getParameter("tipo_moneda")); }catch(Exception e){}
        String descuento_motivo = request.getParameter("descuento_motivo")!=null?new String(request.getParameter("descuento_motivo").getBytes("ISO-8859-1"),"UTF-8"):"";
        String lista_correos = request.getParameter("lista_correos")!=null?new String(request.getParameter("lista_correos").getBytes("ISO-8859-1"),"UTF-8"):"";
        try{ idCliente = Integer.parseInt(request.getParameter("id_cliente")); }catch(Exception e){}
        try{ fecha_pago = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fecha_pago"));}catch(Exception e){}
        
        try{ idFolios = Integer.parseInt(request.getParameter("id_folios")); }catch(Exception e){}
        String tipoPagoDescripcion = request.getParameter("tipo_pago_descripcion")!=null?new String(request.getParameter("tipo_pago_descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";
        String tipoPagoNumCuenta = request.getParameter("tipo_pago_num_cuenta")!=null?new String(request.getParameter("tipo_pago_num_cuenta").getBytes("ISO-8859-1"),"UTF-8"):"";
        String metodoPago = request.getParameter("metodo_pago")!=null?new String(request.getParameter("metodo_pago").getBytes("ISO-8859-1"),"UTF-8"):"";
        try{ idTipoPago = Integer.parseInt(request.getParameter("id_tipo_pago")); }catch(Exception e){}
        try{ idFormaPago = Integer.parseInt(request.getParameter("id_forma_pago")); }catch(Exception e){}
        String parcialidad = request.getParameter("parcialidad")!=null?new String(request.getParameter("parcialidad").getBytes("ISO-8859-1"),"UTF-8"):"";
        String parcialidadFolioOrig = request.getParameter("parcialidad_folio_orig")!=null?new String(request.getParameter("parcialidad_folio_orig").getBytes("ISO-8859-1"),"UTF-8"):"";
        String parcialidadSerieOrig = request.getParameter("parcialidad_serie_orig")!=null?new String(request.getParameter("parcialidad_serie_orig").getBytes("ISO-8859-1"),"UTF-8"):"";
        String parcialidadFechaOrigStr = request.getParameter("parcialidad_fecha_orig")!=null?new String(request.getParameter("parcialidad_fecha_orig").getBytes("ISO-8859-1"),"UTF-8"):"";
        try{ parcialidadFechaOrig = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(request.getParameter("parcialidad_fecha_orig"));}catch(Exception e){}
        String parcialidadMontoOrig = request.getParameter("parcialidad_monto_orig")!=null?new String(request.getParameter("parcialidad_monto_orig").getBytes("ISO-8859-1"),"UTF-8"):"";
        try{ tipoCambio = Double.parseDouble(request.getParameter("tipo_cambio")); }catch(Exception e){}
        
        try{ total = Double.parseDouble(request.getParameter("total")); }catch(Exception e){}
        try{ idRegistroPatronal = Integer.parseInt(request.getParameter("id_registro_patronal")); }catch(Exception e){}
        
        //*DATOS NECESARIOS PARA LA NOMINA:
        Date fechaPago2 = null;
        Date fechaPagoInicial = null;
        Date fechaPagoFinal = null;
        int numDiasPago = 0;
        
        try{ fechaPago2 = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fecha_pago"));}catch(Exception e){e.printStackTrace();}
        try{ fechaPagoInicial = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fechaPagoInicial"));}catch(Exception e){e.printStackTrace();}
        try{ fechaPagoFinal = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fechaPagoFinal"));}catch(Exception e){e.printStackTrace();}
        try{ numDiasPago = Integer.parseInt(request.getParameter("numDiasPago")); }catch(Exception e){}
        
        double montoISR = 0;
        double pocentajeISR = 0;
        
        try{ montoISR = Double.parseDouble(request.getParameter("montoISR")); }catch(Exception e){}
        try{ pocentajeISR = Double.parseDouble(request.getParameter("pocentajeISR")); }catch(Exception e){}
        //**validamos si trae datos los impuestos:
        if(montoISR > 0){
            if(pocentajeISR <= 0){
                msgError += "Coloque el porcentaje de ISR.";                        
            }
        }
        if(pocentajeISR > 0){
            if(montoISR <= 0){
                msgError += "Coloque el monto de ISR.";
            }
        }        
        //**
         
        System.out.println("_________________FECHA DE PAGO: "+fecha_pago);
        System.out.println("_________________FECHA DE PAGO: "+fechaPago2);
        //*
        
        String[] correos = new String[0];
        List<String> listCorreosValidos = new ArrayList<String>();
        if (!lista_correos.trim().equals("")){
            correos = lista_correos.split(",");
            
            for (String itemCorreo: correos){
                try { itemCorreo = itemCorreo.trim(); } catch(Exception ex){}
                if (genericValidator.isEmail(itemCorreo)){
                    listCorreosValidos.add(itemCorreo);
                }
            }
            
            if (listCorreosValidos.size()<=0)
                msgError += "Ninguno de los correos proporcionados es válido";
                
            if (listCorreosValidos.size()>0)
                enviarCorreo = true;
        }
            
        //Validaciones
        EmpresaBO empresaBO = new EmpresaBO(user.getConn());
        EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa());
        if(empresaPermisoAplicacionDto.getAccesoSgfensNomina() == 0)//validamos si tiene acceso para generar nóminas
            msgError+="<ul> No tiene permiso para usar este modulo. ";
            
        if (!genericValidator.isValidString(comentarios, 0, 500))
            msgError = "<ul>El campo comentarios del comprobante Fiscal es incorrecto. Máximo 500 caracteres. ";
        
        if (idCliente<=0)
            msgError+="<ul> Debe seleccionar un empleado para generar el comprobanteFiscal. ";
        
        if (fecha_pago==null)
            msgError+="<ul> Debe seleccionar una fecha de pago. ";
        
        //validar tipo pago descripcion
        if (!genericValidator.isValidString(tipoPagoDescripcion, 1, 100))
            msgError+="<ul> El campo de descripcion del 'Método de Pago' es incorrecto. Mínimo 1 caracter, máximo 100.";
        if (!genericValidator.isClaveMetodoPagoSAT(metodoPago))
            msgError+="<ul>El campo 'Método de Pago' es incorrecto, debe ser una clave del catalogo del SAT con 2 dígitos.";
        
        //validar tipo_pago_num_cuenta
        if (tipoPagoNumCuenta.trim().length()>0){
            if (!genericValidator.isValidString(tipoPagoNumCuenta, 1, 4))
                msgError+="<ul> El campo 'Numero de Cuenta' es opcional, pero es incorrecto. Mínimo 1 caracter, máximo 4.<br/> Puede dejarlo vacío.";
        }
        
        //validar id_forma_pago
        if (idFormaPago<=0)
            msgError+="<ul> El campo Forma de Pago es requerido, favor de seleccionar uno de la lista.";
            
        //Si forma pago = parcialidades:
        if (idFormaPago == 2){
            //validar parcialidad
            if (parcialidad.trim().equals(""))
                msgError+="<ul> El dato Parcialidad es requerido en esta forma de pago. Con formato X/Y.";
            
            {
                String[] valoresParcialidad = parcialidad.split("/");
                if (valoresParcialidad.length!=2){
                    msgError+="<ul>El dato parcialidad es incorrecto. Debe tener formato 1/X.";
                }else{
                    //Recuperamos valores a enteros
                    int x = 0;
                    int y = 0;
                    
                    try{ x = Integer.parseInt(valoresParcialidad[0]); }catch(Exception ex){}
                    try{ y = Integer.parseInt(valoresParcialidad[1]); }catch(Exception ex){}
                    
                    if(x > y){
                        msgError +="<ul> El valor parcialidad es incorrecto: El numero de parcialidad debe ser menor que las parcialidades totales.";
                    }
                    
                    if (x<=0 || y<=0){
                        msgError += "<ul> El valor de parcialidad es incorrecto. No se permiten valores negativos, ni 0.";
                    }
                    
                    if (msgError.equals(""))
                        parcialidadFormat = "Parcialidad " + x +" de " + y;
                    
                }
            }
            
            //validar parcialidad_fecha_orig
            //Si parcialidadFechaOrig, esta vacío o fue incorrecto
            if (parcialidadFechaOrigStr.trim().length()>0){
                if (parcialidadFechaOrig==null)
                    msgError+="<ul> Parcialidades: El dato Fecha de Folio original es incorrecto. Debe tener formato yyyy-MM-dd'T'HH:mm:ss ";
            }
            
            //validar parcialidad_folio_orig
            if (parcialidadFolioOrig.trim().length()>0){
                if (!genericValidator.isUUID(parcialidadFolioOrig))
                    msgError+="<ul> Parcialidades: El dato Folio Original en incorrecto. Debe ser el UUID del CFDI original.";
            }
            
        }
        
        if (tipoMonedaInt>0){
            //Si el tipo de moneda es diferente a PESOS(-1) se solicita el tipo de cambio
            if (tipoCambio<=0)
                msgError += "<ul> El dato 'tipo de Cambio' es requerido cuando el tipo de moneda no es PESOS MXN.";
        }
        
        /*if (idRegistroPatronal<=0){
            msgError += "<ul> El dato 'Registro Patronal' es requerido, debe elegir uno del listado o bien crear antes alguno.";
        }*/
        
        
        if (msgError.equals("")){
            if (cfdiSesion==null){
                msgError += "<ul>No se ha inicializado el Comprobante Fiscal, esta vacío. ";
            }else{
                
                //Validaciones de la Factura
                if (cfdiSesion.getListaProducto().size()<=0 && cfdiSesion.getListaServicio().size()<=0)
                    msgError+="<ul> Debe seleccionar al menos un producto o servicio para generar el Comprobante Fiscal. Lista de Productos y Servicios vacia. ";
                
                if (cfdiSesion.getDescuento_importe()>0 && !genericValidator.isValidString(descuento_motivo, 1, 200))
                    msgError+="<ul> Debe expresar el/los motivo(s) del descuento. Máximo 200 caracteres.";
                
                //Si no es dependiente de un pedido, verificamos que los productos
                //  tengan el stock necesario para dar salida de almacen.
                /*if (cfdiSesion.getIdPedido()<=0){
                    try{
                        cfdiSesion.verificaExistenciaProductos();
                    }catch(Exception ex){
                        msgError += ex.getMessage();
                    }
                }*/
                
                String tipoMonedaStr=null;
                int folioGenerado = -1;
                String folioGeneradoStr = null;
                String serie = null;
                String registroPatronal=null;
                
                Cfd32BO cfd32BO = null;
                File archivoXML = null;
                File archivoPDF = null;

                
                
                if (msgError.equals("")){
                    //Almacenar Nuevo Tipo de Pago--------------
                    TipoPagoDaoImpl tipoPagoDao = new TipoPagoDaoImpl(user.getConn());
                    int nextIdTipoPago = tipoPagoDao.findLast().getIdTipoPago() + 1;

                    TipoPago tipoPagoDto = new TipoPago();
                    try{
                        tipoPagoDto.setClaveMetodoSat(metodoPago);
                        tipoPagoDto.setDescTipoPago(tipoPagoDescripcion);
                        tipoPagoDto.setNumeroCuenta(tipoPagoNumCuenta);
                        tipoPagoDto.setIdTipoPago(nextIdTipoPago);
                        
                        //Insertamos registro
                        tipoPagoDao.insert(tipoPagoDto);
                        
                        cfdiSesion.setTipo_pago(tipoPagoDto);
                    }catch(Exception ex){
                        msgError+="<ul> No se pudo almacenar los datos del Tipo de Pago en un nuevo registro: " + ex.toString();
                    }
                    //Fin Almacenar Nuevo Tipo de Pago----------
                    
                    //Series - Folios-------------------------
                    //En caso de estar seleccionada una Serie, calcular nuevo folio--------
                    if (idFolios>0){
                        try{
                            Folios foliosDto = new FoliosBO(idFolios,user.getConn()).getFolios();
                            if (foliosDto!=null){
                                folioGenerado = foliosDto.getUltimoFolio() + 1;
                                //Revisamos que el nuevo folio cumpla con el minimo y el maximo establecido en la serie
                                if (folioGenerado > foliosDto.getFolioHasta()){
                                    msgError += "La serie elegida a llegado a su folio máximo ("+foliosDto.getFolioHasta()+"). Elija otra o genera una nueva.";
                                }
                                if (folioGenerado < foliosDto.getFolioDesde()){
                                    msgError += "La serie elegida esta mal configurada, tiene establecido un valor minimo (desde) mayor al ultimo folio generado.";
                                }
                                
                                if (msgError.trim().equals("")){
                                    folioGeneradoStr = new DecimalFormat("#0000").format(folioGenerado);
                                    serie = foliosDto.getSerie();
                                    
                                    foliosDto.setUltimoFolio(folioGenerado);
                                    try{
                                        new FoliosDaoImpl(user.getConn()).update(foliosDto.createPk(), foliosDto);
                                        incrementoFolio = true;
                                    }catch(Exception ex){
                                        
                                    }
                                }
                            }else{
                                msgError += "<ul> No se pudo recuperar la información de la Serie elegida, compruebe los datos e intente de nuevo. ";
                            }
                        }catch(Exception ex){
                            msgError += "<ul> Ocurrio un error al recuperar los datos de la Serie elegida y calculo del nuevo folio: " + ex.toString();
                        }
                    }
                    //Fin Series - Folios-------------------------
                    
                    //tipo Moneda
                    try{
                        TipoDeMoneda tipoDeMonedaDto = new TipoDeMonedaBO(tipoMonedaInt,user.getConn()).getTipoMoneda();
                        tipoMonedaStr = tipoDeMonedaDto.getClave();
                    }catch(Exception ex){
                        msgError += "No se pudo recuperar los datos del tipo de moneda elegido: " + ex.toString();
                    }
                    //fin tipo Moneda
                    
                    //Registro patronal
                    try{
                        NominaRegistroPatronal nominaRegistroPatronal = new NominaRegistroPatronalBO(idRegistroPatronal, user.getConn()).getNominaRegistroPatronal();
                        registroPatronal = nominaRegistroPatronal.getRegistroPatronal();
                    }catch(Exception ex){
                        //msgError += "No se pudo recuperar los datos del Registro Patronal elegido: " + ex.toString();
                    }
                    //Fin Registro Patronal
                }
                
                if (msgError.equals("")){
                    //---------GENERACION DE XML

                        try{
                        
                            FormaPago formaPagoDto = new FormaPagoBO(user.getConn()).findFormaPagoById(idFormaPago);
                            Empresa empresaDto = new EmpresaBO(user.getConn()).findEmpresabyId(user.getUser().getIdEmpresa());
                            Ubicacion ubicacionDto = new UbicacionBO(user.getConn()).findUbicacionbyId(empresaDto.getIdUbicacionFiscal());
                            NominaEmpleado empleadoDto = new NominaEmpleadoBO(user.getConn()).findNominaEmpleadobyId(idCliente);
                            
                            //convertimos el empleado en cliente:
                            Cliente clienteDto = new Cliente();
                            clienteDto.setRfcCliente(empleadoDto.getRfc());
                            clienteDto.setNombreCliente(empleadoDto.getNombre());
                            clienteDto.setApellidoPaternoCliente(empleadoDto.getApellidoPaterno());
                            clienteDto.setApellidoMaternoCliente(empleadoDto.getApellidoMaterno());
                            clienteDto.setRazonSocial(empleadoDto.getNombre().trim() +" "+empleadoDto.getApellidoPaterno().trim() +" "+ empleadoDto.getApellidoMaterno().trim());                            
                            clienteDto.setPais("MEXICO");
                            
                            ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Conceptos.Concepto> listaConceptos = cfdiSesion.getSchemaConceptos();
                            ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado> listaTraslados = cfdiSesion.getSchemaImpuestosTraslados();
                            //ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion> listaRetenciones = cfdiSesion.getSchemaImpuestosRetenidos();
                            ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion> listaRetenciones = cfdiSesion.getSchemaImpuestosRetenidosNomina();
                            
                            ////*PARA EL COMPLEMENTO DE NOMINA:
                            //LLAMAMOS AL METODO QUE NOS GENERA PERCEPCIONES Y DEDUCCIONES:
                            cfdiSesion.getSchemaNomina();
                            
                            ArrayList<mx.bigdata.sat.complementos.schema.nomina.Nomina.Percepciones.Percepcion> percepciones = cfdiSesion.getPercepciones();
                            ArrayList<mx.bigdata.sat.complementos.schema.nomina.Nomina.Deducciones.Deduccion> deducciones = cfdiSesion.getDeducciones();
                            
                            ////*

                            Comprobante comprobanteFiscal = CFDI32Factory.createComprobante(formaPagoDto, 
                                    cfdiSesion.getTipo_pago(), 
                                    empresaDto, ubicacionDto ,  
                                    clienteDto, 
                                    listaConceptos, 
                                    listaTraslados, 
                                    listaRetenciones, 
                                    cfdiSesion.calculaSubTotal().doubleValue(), 
                                    total, 
                                    tipoComprobante,
                                    parcialidadFormat,
                                    percepciones,
                                    deducciones,
                                    empleadoDto,
                                    fechaPago2,
                                    fechaPagoInicial,
                                    fechaPagoFinal,
                                    numDiasPago,
                                    registroPatronal);

                            if (comprobanteFiscal!=null){
                                if (idFormaPago==2){
                                    //Parcialidades
                                    if (parcialidadFolioOrig.trim().length()>0)
                                        comprobanteFiscal.setFolioFiscalOrig(parcialidadFolioOrig);
                                    if (parcialidadSerieOrig.trim().length()>0)
                                        comprobanteFiscal.setSerieFolioFiscalOrig(parcialidadSerieOrig);
                                    if (parcialidadFechaOrig != null)
                                        comprobanteFiscal.setFechaFolioFiscalOrig(parcialidadFechaOrig);
                                    if (parcialidadMontoOrig.trim().length()>0)
                                        comprobanteFiscal.setMontoFolioFiscalOrig(new BigDecimal(parcialidadMontoOrig));
                                }
                                //Descuento
                                if (cfdiSesion.getDescuento_importe() >0){
                                    comprobanteFiscal.setDescuento(cfdiSesion.getDescuentoImporte());
                                    comprobanteFiscal.setMotivoDescuento(descuento_motivo);
                                }
                                if (idFolios>0 && serie!=null){
                                    comprobanteFiscal.setSerie(serie);
                                    comprobanteFiscal.setFolio(folioGeneradoStr);
                                }
                                comprobanteFiscal.setMoneda(tipoMonedaStr);
                                if (tipoCambio!=1)
                                    comprobanteFiscal.setTipoCambio(new BigDecimal(tipoCambio).setScale(2, RoundingMode.HALF_UP).toString());
                            }else{
                                msgError +="<ul>No se pudo generar el Comprobante Fiscal (XML)";
                            }
                            
                            if (msgError.equals("")){
                                CertificadoDigitalBO certificadoDigitalBO = new CertificadoDigitalBO(user.getConn());
                                CertificadoDigital certificadoDigitalDto = certificadoDigitalBO.findCertificadoByEmpresa(empresaDto.getIdEmpresa());

                                String rutaArchivosUsuario = appConfig.getApp_content_path() + empresaDto.getRfc() + "/";
                                //String rutaArchivosFacturaCliente = rutaArchivosUsuario + "cfd/emitidos/facturas/" + clienteDto.getRfcCliente() + "/";

                                String rutaCerEmisor = rutaArchivosUsuario + certificadoDigitalDto.getNombreCer();
                                String rutaKeyEmisor = rutaArchivosUsuario + certificadoDigitalDto.getNombreKey();
                                String passwordKeyEmisor = certificadoDigitalDto.getPassword();
                                
                                cfd32BO = new Cfd32BO(comprobanteFiscal);
                                //Sellamos comprobante y validamos
                                cfd32BO.sellarComprobante(rutaCerEmisor, rutaKeyEmisor, passwordKeyEmisor);
                                //Timbramos comprobante
                                System.out.println("*-*-*-*-*-*--*-*-*-*--*-*-*-*-*--*-**--AAAAAAAAAAAA IDENTIFICADOR: "+idTipoComprobante+", -*-*-*--: "+tipoComprobante);
                                
                                //verificamos el numero de créditos o si tiene para emergencia:
                                boolean foliosDisponiblesParaTimbrar = false;
                                if(empresaDto.getFoliosDisponibles() > 0){
                                    foliosDisponiblesParaTimbrar = true;
                                }else{
                                    EmpresaPermisoAplicacion empresaPermiso = null;
                                    empresaPermiso = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaDto.getIdEmpresaPadre());
                                    if(empresaPermiso != null){                                        
                                        if(empresaPermiso.getAccesoCreditosEmergencia() == 1){
                                                foliosDisponiblesParaTimbrar = true;                                                
                                        }else{
                                                foliosDisponiblesParaTimbrar = false;                                                
                                        }
                                    }else{                                            
                                            foliosDisponiblesParaTimbrar = false;
                                    }
                                }
                                if(foliosDisponiblesParaTimbrar){
                                    cfd32BO.timbrarComprobante();
                                    System.out.println("*-*-*-*-*-*--*-*-*-*--*-*-*-*-*--*-**--BBBBBBBBBBBB IDENTIFICADOR: "+idTipoComprobante+", -*-*-*--: "+tipoComprobante);
                                    //Asignamos id Empresa para poder obtener el logo en el PDF
                                    cfd32BO.setComprobanteFiscalDto(new ComprobanteFiscal());
                                    cfd32BO.getComprobanteFiscalDto().setIdEmpresa(idEmpresa);

                                    archivoXML = cfd32BO.guardarComprobante(empresaDto, clienteDto, TipoComprobanteBO.getTipoComprobanteNombreCarpeta(idTipoComprobante) );//"facturas");

                                    //archivoPDF = cfd32BO.guardarRepresentacionImpresa(empresaDto, clienteDto, TipoComprobanteBO.getTipoComprobanteNombreCarpeta(idTipoComprobante) );//"facturas");

                                    //Copiado de XML a cron de envío, lo hacemos hasta que todo haya concluido correctamente
                                    //cfd32BO.copiarCFDIenvioAsincrono(archivoXML);
                                }else{
                                    msgError +="<ul> La empresa/sucursal no cuenta con Créditos Disponibles para facturar. Contacte a su administrador para adquirir un nuevo paquete de Créditos.";
                                }                                
                            }
                            
                        }catch(Exception ex){
                            ex.printStackTrace();
                            /*try{//regresamos el folio, si es que se escogio uno
                                if(idFolios>0){
                                    Folios foliosDto = new FoliosBO(idFolios,user.getConn()).getFolios();
                                    folioGenerado = foliosDto.getUltimoFolio() - 1;
                                    foliosDto.setUltimoFolio(folioGenerado);
                                    new FoliosDaoImpl(user.getConn()).update(foliosDto.createPk(), foliosDto);
                                }
                            }catch(Exception ex2){}*/
                            msgError+= "<ul> <br/>" + ex.toString();
                            if (ex.getCause()!=null){
                                msgError +="<ul>Ocurrio un error al generar XML: " + ex.getCause().toString();
                            }else{
                                msgError +="<ul>Ocurrio un error al generar XML: " + ex.toString();
                            }
                        }
                            
                    //---------FIN GENERACION XML
                }
                
                //Si no es dependiente de un pedido, entonces se da salida de almacen
                // a todos los productos seleccionados en la factura (restar stock)
                /*if (msgError.trim().equals("")){
                    if (cfdiSesion.getIdPedido()<=0){
                        try{
                            cfdiSesion.salidaAlmacenProductos();
                        }catch(Exception ex){
                            try{
                                for (int k=0;k<10;k++)
                                    archivoXML.delete();
                            }catch(Exception e2){}
                            msgError += ex.toString();
                        }
                    }
                }*/
                
                
                ComprobanteFiscal comprobanteFiscalDto = null;
                ComprobanteFiscalBO comprobanteFiscalBO = new ComprobanteFiscalBO(user.getConn());
                if (msgError.equals("") && archivoXML!=null && cfd32BO!=null){
                    cfdiSesion.setComentarios(comentarios);
                    cfdiSesion.setDescuento_motivo(descuento_motivo);
                    cfdiSesion.setFecha_pago(fecha_pago);
                    cfdiSesion.setId_estatus(EstatusComprobanteBO.ESTATUS_ENTREGADA);//Entregado
                    
                    //si se selecciono folio
                    if (idFolios>0 && folioGenerado>0 && folioGeneradoStr!=null){
                        cfdiSesion.setId_folio(idFolios);
                        cfdiSesion.setFolio_generado(folioGeneradoStr);
                    }
                    
                    cfdiSesion.setId_forma_pago(idFormaPago);
                    if (idFormaPago==2 && parcialidadFormat!=null){
                        cfdiSesion.setParcialidad(parcialidadFormat);
                    }
                    
                    cfdiSesion.setId_tipo_comprobante(idTipoComprobante);
                    cfdiSesion.setArchivo_cfd(archivoXML.getName());
                    //*****CADENA SIN ENCRIPTAR******
                    cfdiSesion.setCadena_original(cfd32BO.getCfd().getCadenaOriginal());
                    cfdiSesion.setSello_digital(cfd32BO.getComprobanteFiscal().getSello());
                    cfdiSesion.setTipo_moneda_int(tipoMonedaInt);
                    cfdiSesion.setTipo_moneda(tipoMonedaStr);
                    cfdiSesion.setTipo_cambio(tipoCambio);
                    cfdiSesion.setUuid(cfd32BO.getTimbreFiscalDigital().getUUID());
                    cfdiSesion.setSello_sat(cfd32BO.getTimbreFiscalDigital().getSelloSAT());
                    cfdiSesion.setNoCertificadoSAT(cfd32BO.getTimbreFiscalDigital().getNoCertificadoSAT());
                    cfdiSesion.setFechaTimbrado(cfd32BO.getTimbreFiscalDigital().getFechaTimbrado());                    
                    
                    try{
                        if (idCliente>0){
                            cfdiSesion.setCliente(new ClienteBO(idCliente,user.getConn()).getCliente());
                        }
                    }catch(Exception ex){ ex.printStackTrace();}
                    
                    //Para generar un nuevo comprobanteFiscal en cada Guardar
                    //Deshabilitar en caso de querer actualizar en lugar de generar una nueva
                    cfdiSesion.setIdComprobanteFiscal(0);
                    
                    if (msgError.equals("")){
                        request.getSession().setAttribute("cfdiSesion", cfdiSesion);
                        comprobanteFiscalAux = cfdiSesion;
                        try{
                            comprobanteFiscalDto = comprobanteFiscalBO.creaActualizaComprobanteFiscal(cfdiSesion, user);
                            
                            cfd32BO.copiarCFDIenvioAsincrono(archivoXML);
                        }catch(Exception ex){
                            //En caso de error, borramos archivo XML ya que no se registrara en BD
                            try{
                                for (int k=0;k<10;k++)
                                    archivoXML.delete();
                            }catch(Exception e2){}
                            /*try{
                                if(idFolios>0){
                                    Folios foliosDto = new FoliosBO(idFolios,user.getConn()).getFolios();
                                    folioGenerado = foliosDto.getUltimoFolio() - 1;
                                    foliosDto.setUltimoFolio(folioGenerado);
                                    new FoliosDaoImpl(user.getConn()).update(foliosDto.createPk(), foliosDto);
                                }
                            }catch(Exception ex2){}*/
                            msgError+= "<ul>Ocurrio un error al almacenar el registro de comprobanteFiscal: <br/>" + ex.toString();
                            
                        }
                        
                        //ALMACENAMOS LOS DATOS DE NOMINA_COMPROBANTE_DESCRIPCION
                        try{
                            NominaComprobanteDescripcion ncd = new NominaComprobanteDescripcion();
                            ncd.setIdCromprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal());
                            ncd.setFechaPago(fechaPago2);
                            ncd.setFechaInicialPago(fechaPagoInicial);
                            ncd.setFechaFinPago(fechaPagoFinal);
                            ncd.setNumDiasPagados(numDiasPago);
                            ncd.setIsrImpuestoPorcentaje(pocentajeISR);
                            ncd.setIsrMontoImpuesto(montoISR);
                            ncd.setIdNominaRegPatronal(idRegistroPatronal);
                            new NominaComprobanteDescripcionDaoImpl(user.getConn()).insert(ncd);
                        }catch(Exception e){}
                        //ALMACENAMOS LOS DATOS DE DESCRIPCION_HORAS_EXTRA
                        
                        //ALMACENAMOS LOS DATOS DE COMPROBANTE_DESCRIPCION_INCAPACIDAD
                        
                        //ALMACENAMOS LOS DATOS DE COMPROBANTE_DESCRIPCION_PERCEPCION_DEDUCCION
                        try{
                            NominaComprobanteDescripcionPercepcionDeduccion ncdpd = new NominaComprobanteDescripcionPercepcionDeduccion();
                            if(cfdiSesion.getListaDeduccion()!=null && cfdiSesion.getListaDeduccion().size()>0){
                                for(DeduccionSesion itemDeduccion : cfdiSesion.getListaDeduccion()){
                                    ncdpd.setIdCromprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal());
                                    ncdpd.setIdPercepcionDeduccion(2);
                                    if(String.valueOf(itemDeduccion.getIdNominaTipoDeduccion()).length()==1)
                                        ncdpd.setTipoClave("00"+itemDeduccion.getIdNominaTipoDeduccion());
                                    else if(String.valueOf(itemDeduccion.getIdNominaTipoDeduccion()).length()==2)
                                        ncdpd.setTipoClave("0"+itemDeduccion.getIdNominaTipoDeduccion());
                                    else if(String.valueOf(itemDeduccion.getIdNominaTipoDeduccion()).length()==3)
                                        ncdpd.setTipoClave(""+itemDeduccion.getIdNominaTipoDeduccion());                                        
                                    ncdpd.setClavePatron(itemDeduccion.getClave());
                                    ncdpd.setConceptoDescripcion(itemDeduccion.getDescripcionConceptoAlterna());
                                    ncdpd.setImporteGravado(itemDeduccion.getImporteGravado());
                                    ncdpd.setImporteExcepto(itemDeduccion.getImporteExento());
                                    ncdpd.setIdDeLaPercepcionDeduccion(itemDeduccion.getIdDeduccion());
                                    new NominaComprobanteDescripcionPercepcionDeduccionDaoImpl(user.getConn()).insert(ncdpd);
                                }
                            }
                        }catch(Exception e){}
                        try{
                            NominaComprobanteDescripcionPercepcionDeduccion ncdpd2 = new NominaComprobanteDescripcionPercepcionDeduccion();
                            if(cfdiSesion.getListaPercepcion()!=null && cfdiSesion.getListaPercepcion().size()>0){
                                for(PercepcionSesion itemPercepcion : cfdiSesion.getListaPercepcion()){
                                    ncdpd2.setIdCromprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal());
                                    ncdpd2.setIdPercepcionDeduccion(1);
                                    if(String.valueOf(itemPercepcion.getIdNominaTipoPercepcion()).length()==1)
                                        ncdpd2.setTipoClave("00"+itemPercepcion.getIdNominaTipoPercepcion());
                                    else if(String.valueOf(itemPercepcion.getIdNominaTipoPercepcion()).length()==2)
                                        ncdpd2.setTipoClave("0"+itemPercepcion.getIdNominaTipoPercepcion());
                                    else if(String.valueOf(itemPercepcion.getIdNominaTipoPercepcion()).length()==3)
                                        ncdpd2.setTipoClave(""+itemPercepcion.getIdNominaTipoPercepcion());                                        
                                    ncdpd2.setClavePatron(itemPercepcion.getClave());
                                    ncdpd2.setConceptoDescripcion(itemPercepcion.getDescripcionConceptoAlterna());
                                    ncdpd2.setImporteGravado(itemPercepcion.getImporteGravado());
                                    ncdpd2.setImporteExcepto(itemPercepcion.getImporteExento());
                                    ncdpd2.setIdDeLaPercepcionDeduccion(itemPercepcion.getIdPercepcion());
                                    new NominaComprobanteDescripcionPercepcionDeduccionDaoImpl(user.getConn()).insert(ncdpd2);
                                }
                            }
                        }catch(Exception e){}
                        
                    }

                }
                
                if (msgError.equals("")){
                    //Si la factura se genero desde un pedido, se actualiza el pedido
                    if (cfdiSesion.getIdPedido()>0){
                        try{
                                SgfensPedido pedidoDto = new SGPedidoBO(cfdiSesion.getIdPedido(),user.getConn()).getPedido();
                                if (pedidoDto!=null){
                                    pedidoDto.setIdComprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal());

                                    new SgfensPedidoDaoImpl(user.getConn()).update(pedidoDto.createPk(), pedidoDto);
                                }else{
                                    msgError += "Comprobante generado con incidencias: No se pudo recuperar la información del Pedido asociado a la Factura que se desea generar. Avise a su proveedor.";
                                }

                        }catch(Exception ex){
                            msgError+= "Comprobante generado con incidencias: No se pudo actualizar el registro de Pedido asociado a la factura." + ex.getCause().toString();
                        }
                    }
                }
                
                if (msgError.equals("")){
                    out.print("ComprobanteFiscal almacenado satisfactoriamente. <br/><br/><b> UUID de ComprobanteFiscal: " + comprobanteFiscalDto.getUuid() + "</b>");
                    
                    //cfdiSesion = new ComprobanteFiscalSesion();
                    request.getSession().setAttribute("cfdiSesion", cfdiSesion);
                }
                
                if (msgError.equals("")){
                    try{
                        if (enviarCorreo){
                            //ComprobanteFiscalBO comprobanteFiscalBO = new ComprobanteFiscalBO(comprobanteFiscalDto.getIdComprobanteFiscal());
                            out.print("<ul>"+ comprobanteFiscalBO.enviaNotificacionNuevaComprobanteFiscal(listCorreosValidos,archivoXML, archivoPDF, idEmpresa));
                        }
                    }catch(Exception ex){
                        out.print("<ul> No se pudo enviar el correo de notificación.");
                        ex.printStackTrace();
                    }
                }
                if(!msgError.equals("")){    
                    System.out.println(":::::: ERROR AL INTENTAR GENERAR-ALMACENAR LA FACTURA: "+msgError);
                    //regresamos el contador de folios:
                    if(incrementoFolio){ //si hubo un error y ademas ya se habia incrementado el folio, lo decrementamos:
                        try{
                            Folios foliosDto = new FoliosDaoImpl(user.getConn()).findByPrimaryKey(idFolios);
                            folioGenerado = foliosDto.getUltimoFolio() - 1;
                            foliosDto.setUltimoFolio(folioGenerado);
                            new FoliosDaoImpl(user.getConn()).update(foliosDto.createPk(), foliosDto);
                        }catch(Exception ex2){ex2.printStackTrace();}
                   }
               }
            }
        }
        
    }else if (mode==20){
        //20 =  Recargar Select Clientes
        
        idCliente = -1;
        try{ idCliente = Integer.parseInt(request.getParameter("id_cliente")); }catch(Exception e){}
        recargarSelectEmpleados = true;
        
    }else if (mode==21){
        //21 = Seleccionar Cliente (obtener datos)
        
        idCliente = -1;
        try{ idCliente = Integer.parseInt(request.getParameter("id_cliente")); }catch(Exception e){}
        
        NominaEmpleado clienteDto = new NominaEmpleadoBO(user.getConn(),idCliente).getNominaEmpleado();
        
        String jsonOutput = gson.toJson(clienteDto);
        out.print("<!--EXITO-->"+jsonOutput);
        
    }else if (mode==22){
        //Cancelar CFDI
        
        //Parametros
        int id_comprobanteFiscal = -1;
        try{ id_comprobanteFiscal = Integer.parseInt(request.getParameter("id_comprobanteFiscal")); }catch(Exception e){}
        
        //Procesamiento
        if (id_comprobanteFiscal>=0){
            ComprobanteFiscalBO cfdiSesionBO = new ComprobanteFiscalBO(id_comprobanteFiscal,user.getConn());
            
            try{
                if (cfdiSesionBO.cancelaComprobanteFiscal()){
                    out.print("<!--EXITO-->Cancelación realizada con éxito.");
                }else{
                    msgError += " La cancelación no se ha completado correctamente. Intente de nuevo.";
                }
            }catch(Exception ex){
                msgError += "Error al cancelar comprobante. " + ex.toString();
            }
        }else{
            msgError += "<ul> No se específico el identificador (ID) del Comprobante Fiscal a cancelar. Intente de nuevo.";
        }
        
    }
    
    ///////////////PERCEPCIONES INICIO
    else if (mode==23){
        //MODO  23  =  Seleccionar y Recuperar datos de Percepcion
        
        //Parametros
        int idPercepcion = -1;
        try{
            idPercepcion = Integer.parseInt(request.getParameter("id_percepcion_select"));
        }catch(NumberFormatException e){}
        
        //Validacion
        if (idPercepcion>0){
            NominaPercepcion nominaPercepcion = new NominaPercepcion();
            NominaPercepcionDaoImpl conceptoDaoImpl = new NominaPercepcionDaoImpl(user.getConn());

            try{
               nominaPercepcion = conceptoDaoImpl.findByPrimaryKey(idPercepcion);
            }catch(Exception ex){
                msgError = "Ocurrio un error al consultar la percepción elegida. " + ex.toString();           }

            if (msgError.equals("")){
                //Usamos JSON para retornar el objeto respuesta
                String jsonOutput = gson.toJson(nominaPercepcion);
                System.out.println("________________AGREGO A G_SON");
                System.out.println("____________: "+jsonOutput.intern());
                out.print("<!--EXITO-->"+jsonOutput);
            }
            
        }else{
            msgError="No se selecciono una percepcion válido.";
        }
        System.out.println("______________________TODO BIEN");
    }else if (mode==24){
        //Modo 24 = agregar percepcion
        
        //Parametros
        int idPercepcion = -1;
        double cantidad =1;
        double precioGravado =-1;
        double precioExento =-1;
        //double monto=-1;
        String unidad = request.getParameter("percepcion_unidad") != null ? request.getParameter("percepcion_unidad") : "SERVICIO";
        String productoDescripcion = request.getParameter("percepcionDescripcion") != null ? request.getParameter("percepcionDescripcion") : "";
        try{ idPercepcion = Integer.parseInt(request.getParameter("percepcion_id")); }catch(Exception e){}
        //try{ cantidad = Double.parseDouble(request.getParameter("producto_cantidad")); }catch(Exception e){}
        try{ precioGravado = Double.parseDouble(request.getParameter("percepcion_precio_gravado")); }catch(Exception e){}
        try{ precioExento = Double.parseDouble(request.getParameter("percepcion_precio_exento")); }catch(Exception e){}
        //try{ monto = Double.parseDouble(request.getParameter("producto_monto")); }catch(Exception e){}
        
        //Validaciones
        GenericValidator gc = new GenericValidator();
        if (idPercepcion<=0)
            msgError = "<ul>Percepcion no específicado o incorrecto";
        
        System.out.println("_________________________DESCRIPCION: "+productoDescripcion);
        
        if(!gc.isValidString(productoDescripcion, 1, 300))
            msgError = "<ul>El dato 'descripción' es requerido. Máximo 300 caracteres.";        
        if (msgError.equals("")){
            NominaPercepcion percepcionSeleccionada = null;
            NominaPercepcionDaoImpl nominaPercepcionDaoImpl = new NominaPercepcionDaoImpl(user.getConn());

            try{
               percepcionSeleccionada = nominaPercepcionDaoImpl.findByPrimaryKey(idPercepcion);
            }catch(Exception ex){
                msgError = "Ocurrio un error al consultar el percepcion elegido. " + ex.toString();
            }
            
            try{
                if (cfdiSesion==null){
                    cfdiSesion = new ComprobanteFiscalSesion();
                }
                
                //recuperamos la clave del catalogo de percepcion:
                NominaTipoPercepcion ntp = new NominaTipoPercepcionBO(percepcionSeleccionada.getIdNominaTipoPercepcion(), user.getConn()).getNominaTipoPercepcion();

                PercepcionSesion nuevoPercepcionCompraSesion = new PercepcionSesion();
                nuevoPercepcionCompraSesion.setIdPercepcion(idPercepcion);
                nuevoPercepcionCompraSesion.setIdNominaTipoPercepcion(Integer.parseInt(ntp.getClave()));
                nuevoPercepcionCompraSesion.setClave(percepcionSeleccionada.getClave());
                nuevoPercepcionCompraSesion.setDescipcionConcepto(productoDescripcion);
                nuevoPercepcionCompraSesion.setUnidad(unidad);
                nuevoPercepcionCompraSesion.setImporteGravado(precioGravado);
                nuevoPercepcionCompraSesion.setImporteExento(precioExento);
                nuevoPercepcionCompraSesion.setCantidad(cantidad);                
                
                if(!productoDescripcion.trim().equals(""))
                    nuevoPercepcionCompraSesion.setDescripcionConceptoAlterna(productoDescripcion);
                
                //Verificamos si se consulta el stock o no
                /*EmpresaBO empresaBO = new EmpresaBO(user.getConn());
                EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa());     
                if (empresaPermisoAplicacionDto!=null){
                    if(empresaPermisoAplicacionDto.getRevisionCantidadPercepcion() == 1){                        
                        if (percepcionSeleccionada!=null){
                            int nuevoStock = (int)(percepcionSeleccionada.getNumArticulosDisponibles() - cantidad);                
                            if (nuevoStock<0){
                                msgError+="<ul>El stock del articulo es insuficiente para cubrir la operación.<br/>No. de Articulos disponibles: " + percepcionSeleccionada.getNumArticulosDisponibles();
                            }
                        }
                    }
                }*/
                
                if (percepcionSeleccionada!=null && msgError.equals("") ){
                    boolean coincidenciaPrevia=false;
                    for (PercepcionSesion itemPercepcion:cfdiSesion.getListaPercepcion()){
                        if (itemPercepcion.getIdPercepcion()==percepcionSeleccionada.getIdNominaPercepcion()){
                            //Ya se había seleccionado este producto
                            coincidenciaPrevia=true;
                            break;
                        }
                    }
                    if (!coincidenciaPrevia){
                        cfdiSesion.getListaPercepcion().add(nuevoPercepcionCompraSesion);
                    }else{
                        msgError +="<ul> La percepcion ya fue seleccionado previamente. Modifique los valores o elimine la percepcion del listado.";
                    }
                        
                }
                
                request.getSession().setAttribute("cfdiSesion", cfdiSesion);
            }catch(Exception ex){
                msgError += "Ocurrio un error al recuperar las percepciones elegidas previamente en la sesion. " + ex.toString();
            }
            
            recargarListaPercepcions=true;
        }
        
    }
    else if (mode==25){
        //Quitar percepcion de lista
        if (cfdiSesion==null)
            cfdiSesion = new ComprobanteFiscalSesion();
        int index_lista_percepcion=-1;
        try{ index_lista_percepcion = Integer.parseInt(request.getParameter("index_lista_percepcion")); }catch(Exception e){}
        if (index_lista_percepcion<0)
            msgError = "<ul>Percepción inválida. Si persiste el problema intente recargar esta página.";
        
        if (msgError.equals("")){
            try{
                cfdiSesion.getListaProducto().remove(index_lista_percepcion);
            }catch(Exception ex){
                msgError+="La percepcion especificada no se encontro en el listado en sesion. Intente de nuevo.";
            }
            request.getSession().setAttribute("cfdiSesion", cfdiSesion);

            recargarListaPercepcions=true;
        }
    }
    
    
    ///////////////PERCEPCIONES FIN
    
    ///////////////DEDUCCIONES INICIO
    else if (mode==26){
        //MODO  26  =  Seleccionar y Recuperar datos de Dediccion
        
        //Parametros
        int idDeduccion = -1;
        try{
            idDeduccion = Integer.parseInt(request.getParameter("id_deduccion_select"));
        }catch(NumberFormatException e){}
        
        //Validacion
        if (idDeduccion>0){
            NominaDeduccion nominaDeduccion = new NominaDeduccion();
            NominaDeduccionDaoImpl conceptoDaoImpl = new NominaDeduccionDaoImpl(user.getConn());

            try{
               nominaDeduccion = conceptoDaoImpl.findByPrimaryKey(idDeduccion);
            }catch(Exception ex){
                msgError = "Ocurrio un error al consultar la deducción elegida. " + ex.toString();           }

            if (msgError.equals("")){
                //Usamos JSON para retornar el objeto respuesta
                String jsonOutput = gson.toJson(nominaDeduccion);
                System.out.println("________________AGREGO A G_SON");
                System.out.println("____________: "+jsonOutput.intern());
                out.print("<!--EXITO-->"+jsonOutput);
            }
            
        }else{
            msgError="No se selecciono una deducción válido.";
        }
        System.out.println("______________________TODO BIEN");
    }else if (mode==27){
        //Modo 27 = agregar deduccion
        
        //Parametros
        int idDeduccion = -1;
        double cantidad =1;
        double precioGravado =-1;
        double precioExento =-1;
        //double monto=-1;
        String unidad = request.getParameter("deduccion_unidad") != null ? request.getParameter("deduccion_unidad") : "SERVICIO";
        String productoDescripcion = request.getParameter("deduccionDescripcion") != null ? request.getParameter("deduccionDescripcion") : "";
        try{ idDeduccion = Integer.parseInt(request.getParameter("deduccion_id")); }catch(Exception e){}
        //try{ cantidad = Double.parseDouble(request.getParameter("producto_cantidad")); }catch(Exception e){}
        try{ precioGravado = Double.parseDouble(request.getParameter("deduccion_precio_gravado")); }catch(Exception e){}
        try{ precioExento = Double.parseDouble(request.getParameter("deduccion_precio_exento")); }catch(Exception e){}
        //try{ monto = Double.parseDouble(request.getParameter("producto_monto")); }catch(Exception e){}
        
        //Validaciones
        GenericValidator gc = new GenericValidator();
        if (idDeduccion<=0)
            msgError = "<ul>Deduccion no específicado o incorrecto";
        
        System.out.println("_________________________DESCRIPCION: "+productoDescripcion);
        
        if(!gc.isValidString(productoDescripcion, 1, 300))
            msgError = "<ul>El dato 'descripción' es requerido. Máximo 300 caracteres.";        
        if (msgError.equals("")){
            NominaDeduccion deduccionSeleccionada = null;
            NominaDeduccionDaoImpl nominaDeduccionDaoImpl = new NominaDeduccionDaoImpl(user.getConn());

            try{
               deduccionSeleccionada = nominaDeduccionDaoImpl.findByPrimaryKey(idDeduccion);
            }catch(Exception ex){
                msgError = "Ocurrio un error al consultar el deduccion elegido. " + ex.toString();
            }
            
            try{
                if (cfdiSesion==null){
                    cfdiSesion = new ComprobanteFiscalSesion();
                }
                
                //buscamos del catalogo del sat la clave de la deduccion:
                NominaTipoDeduccion ntd = new NominaTipoDeduccionBO(deduccionSeleccionada.getIdNominaTipoDeduccion(), user.getConn()).getNominaTipoDeduccion();

                DeduccionSesion nuevoDeduccionCompraSesion = new DeduccionSesion();
                nuevoDeduccionCompraSesion.setIdDeduccion(idDeduccion);
                nuevoDeduccionCompraSesion.setIdNominaTipoDeduccion(Integer.parseInt(ntd.getClave()));
                nuevoDeduccionCompraSesion.setClave(deduccionSeleccionada.getClave());
                nuevoDeduccionCompraSesion.setDescipcionConcepto(productoDescripcion);
                nuevoDeduccionCompraSesion.setUnidad(unidad);
                nuevoDeduccionCompraSesion.setImporteGravado(precioGravado);
                nuevoDeduccionCompraSesion.setImporteExento(precioExento);
                nuevoDeduccionCompraSesion.setCantidad(cantidad);                
                
                if(!productoDescripcion.trim().equals(""))
                    nuevoDeduccionCompraSesion.setDescripcionConceptoAlterna(productoDescripcion);
                
                //Verificamos si se consulta el stock o no
                /*EmpresaBO empresaBO = new EmpresaBO(user.getConn());
                EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa());     
                if (empresaPermisoAplicacionDto!=null){
                    if(empresaPermisoAplicacionDto.getRevisionCantidadDeduccion() == 1){                        
                        if (deduccionSeleccionada!=null){
                            int nuevoStock = (int)(deduccionSeleccionada.getNumArticulosDisponibles() - cantidad);                
                            if (nuevoStock<0){
                                msgError+="<ul>El stock del articulo es insuficiente para cubrir la operación.<br/>No. de Articulos disponibles: " + deduccionSeleccionada.getNumArticulosDisponibles();
                            }
                        }
                    }
                }*/
                
                if (deduccionSeleccionada!=null && msgError.equals("") ){
                    boolean coincidenciaPrevia=false;
                    for (DeduccionSesion itemDeduccion:cfdiSesion.getListaDeduccion()){
                        if (itemDeduccion.getIdDeduccion()==deduccionSeleccionada.getIdNominaDeduccion()){
                            //Ya se había seleccionado este producto
                            coincidenciaPrevia=true;
                            break;
                        }
                    }
                    if (!coincidenciaPrevia){
                        cfdiSesion.getListaDeduccion().add(nuevoDeduccionCompraSesion);
                    }else{
                        msgError +="<ul> La deduccion ya fue seleccionado previamente. Modifique los valores o elimine la deduccion del listado.";
                    }
                        
                }
                
                request.getSession().setAttribute("cfdiSesion", cfdiSesion);
            }catch(Exception ex){
                msgError += "Ocurrio un error al recuperar las deducciones elegidas previamente en la sesion. " + ex.toString();
            }
            
            recargarListaDeduccions=true;
        }
        
    }else if(mode==28){
        //Modo 28 = Agregar Impuesto ISR para nómina
        
        //Parametros
        /*int idImpuesto = -1;
        try{ idImpuesto = Integer.parseInt(request.getParameter("impuesto_id")); }catch(Exception e){}*/
        
        double montoISR = 0;
        try{ montoISR = Double.parseDouble(request.getParameter("montoISR")); }catch(Exception e){}
        double pocentajeISR = 0;
        try{ pocentajeISR = Double.parseDouble(request.getParameter("pocentajeISR")); }catch(Exception e){}
        
        String soloRecargar = request.getParameter("soloRecargar") != null ? request.getParameter("soloRecargar") : "";
        
        //Validaciones
  //      if (montoISR<=0)
  //          msgError = "<ul>Impuesto no específicado o incorrecto";
        
        if (msgError.equals("")){
            /*Impuesto impuestoDto = null;
            ImpuestoDaoImpl impuestoDaoImpl = new ImpuestoDaoImpl(user.getConn());

            try{
               impuestoDto = impuestoDaoImpl.findByPrimaryKey(idImpuesto);
            }catch(Exception ex){
                msgError = "Ocurrio un error al consultar el impuesto elegido. " + ex.toString();
            }*/
            
            if(soloRecargar.equals("soloRecargar")){//vemos si solo recargamos el impuesto; esto al ser un recibo de nomina hecho de una plantilla
                for (ImpuestoSesion itemImpuesto : cfdiSesion.getListaImpuesto()){
                    System.out.println("____________________RECARGANDO VALORES DE IMPUESTO ISR");
                    itemImpuesto.setIdImpuesto(0);
                    itemImpuesto.setNombre("ISR");
                    itemImpuesto.setDescripcion("IMPUESTO SOBRE LA RENTA");
                    itemImpuesto.setPorcentaje(pocentajeISR);
                    itemImpuesto.setTrasladado(false);
                    itemImpuesto.setImplocal(false);
                    itemImpuesto.setMonto(montoISR);
                }
            }else{//si es un recibo de nomina totalmente nuevo:
                try{
                    if (cfdiSesion==null){
                        cfdiSesion = new ComprobanteFiscalSesion();
                    }
                    
                    if(cfdiSesion.getListaImpuesto().size()>0){//si ya existe el impuesto, solo le agregamos el valor del porcentaje y/o monto que cambio
                        for (ImpuestoSesion itemImpuesto : cfdiSesion.getListaImpuesto()){
                            System.out.println("____________________RECARGANDO VALORES DE IMPUESTO ISR");
                            itemImpuesto.setIdImpuesto(0);
                            itemImpuesto.setNombre("ISR");
                            itemImpuesto.setDescripcion("IMPUESTO SOBRE LA RENTA");
                            itemImpuesto.setPorcentaje(pocentajeISR);
                            itemImpuesto.setTrasladado(false);
                            itemImpuesto.setImplocal(false);
                            itemImpuesto.setMonto(montoISR);
                        }
                    }else{
                        ImpuestoSesion nuevoImpuestoCompraSesion = new ImpuestoSesion();
                        nuevoImpuestoCompraSesion.setIdImpuesto(0);
                        nuevoImpuestoCompraSesion.setNombre("ISR");
                        nuevoImpuestoCompraSesion.setDescripcion("IMPUESTO SOBRE LA RENTA");
                        nuevoImpuestoCompraSesion.setPorcentaje(pocentajeISR);
                        nuevoImpuestoCompraSesion.setTrasladado(false);
                        nuevoImpuestoCompraSesion.setImplocal(false);
                        nuevoImpuestoCompraSesion.setMonto(montoISR);

                        boolean coincidenciaPrevia=false;                       
                        if (!coincidenciaPrevia){
                            cfdiSesion.getListaImpuesto().add(nuevoImpuestoCompraSesion);
                        }else{
                            msgError +="<ul> El impuesto ya fue seleccionado previamente.";
                        }

                    }            
                    request.getSession().setAttribute("cfdiSesion", cfdiSesion);                
                }catch(Exception ex){
                    msgError += "Ocurrio un error al recuperar los impuestos elegidos previamente en la sesion. " + ex.toString();
                }
            }
            
            //recargarListaImpuestos=true;
        }
        
    }else if (mode==29){
        //Reconstruir Sesion desde BD
        
        //Parametros
        int id_comprobanteFiscal = -1;
        try{ id_comprobanteFiscal = Integer.parseInt(request.getParameter("id_comprobanteFiscal")); }catch(Exception e){}
        
        //Procesamiento
        if (id_comprobanteFiscal>=0){
            ComprobanteFiscalBO cfdiSesionBO = new ComprobanteFiscalBO(id_comprobanteFiscal,user.getConn());
            cfdiSesion = cfdiSesionBO.getSessionFromComprobanteFiscalDB();
            request.getSession().setAttribute("cfdiSesion", cfdiSesion);
            System.out.println("_____________-RECARGANDO TODO.....");
        }else{
            msgError += "<ul> No se específico el identificador (ID) del Comprobante Fiscal a recuperar. Intente de nuevo.";
        }
        
    }else if (mode==30){
        // 30 = Recargar Lista de Percepciones
        if (cfdiSesion==null)
            cfdiSesion = new ComprobanteFiscalSesion();
        request.getSession().setAttribute("cfdiSesion", cfdiSesion);
        recargarListaPercepcions=true;
    }else if (mode==31){
        //31 = Recargar Lista de Deduccion
        if (cfdiSesion==null)
            cfdiSesion = new ComprobanteFiscalSesion();
        request.getSession().setAttribute("cfdiSesion", cfdiSesion);
        recargarListaDeduccions=true;
    }
    ///////////////DEDUCCIONES FIN
    
    //*controla para ver que comprobantes nómina se les generará autonómina:
    else if (mode==32){
        //32 = Agregar ID's comprobantes a generar nómina automática
        System.out.println("________AGREGAR");
        if (nominaIDsSesion==null){
            nominaIDsSesion = new NominaComprobanteIdsSesion();
            System.out.println("________CREANDO NUEVO OBJETO EN AGREGAR");
        }
        int idAgregar = 0;
        try{ idAgregar = Integer.parseInt(request.getParameter("idAgregarQuitar")); }catch(Exception e){}
        
        nominaIDsSesion.agregarIDs(idAgregar);
        
        request.getSession().setAttribute("nominaIDsSesion", nominaIDsSesion);
        
    }else if (mode==33){
        //33 = Quitar ID's comprobantes a generar nómina automática
        System.out.println("________QUITAR");
        if (nominaIDsSesion==null){
            nominaIDsSesion = new NominaComprobanteIdsSesion();
            System.out.println("________CREANDO NUEVO OBJETO EN QUITAR");
        }
        
        int idQuitar = 0;
        try{ idQuitar = Integer.parseInt(request.getParameter("idAgregarQuitar")); }catch(Exception e){}
        
        nominaIDsSesion.quitarIDs(idQuitar);
        
        request.getSession().setAttribute("nominaIDsSesion", nominaIDsSesion);
        
    }else if(mode==34){
        //34 = procesamiento de nómina automática:
        boolean procesadoExitosamente = false;
        int contadorDeExitosos = 0;
        int contadorDeFallidos = 0;
        String foliosFallidos = "ID's de Comprobante Fallidos:";
        //Reconstruir Sesion desde BD        
        //Parametros
        request.getSession().setAttribute("iDsComproNuevos", null); //cagamos como null el listado de ID's de nóminas nuevas generadas en autonomina
        List<Integer> iDsComproNuevos = new ArrayList<Integer>(); //lista para meter los ID's de los comprobantes nuevos generados.
        for(int idCopiaNomina : nominaIDsSesion.getIdsComprobantes()){
            //Procesamiento
            if (idCopiaNomina>=0){
                Date fechaPago = null;
                Date fechaPago_Inicial = null;
                Date fechaPago_Final = null;
                try{ fechaPago = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fecha_pago"));}catch(Exception e){}                
                try{ fechaPago_Inicial = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fechaPagoInicial"));}catch(Exception e){e.printStackTrace();}
                try{ fechaPago_Final = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fechaPagoFinal"));}catch(Exception e){e.printStackTrace();}
                
                int tipoComprobanteNomina = TipoComprobanteBO.TIPO_NOMINA;
                ComprobanteFiscalBO cfdiSesionBO = new ComprobanteFiscalBO(idCopiaNomina,user.getConn());
                cfdiSesion = cfdiSesionBO.getSessionFromComprobanteFiscalDB();
                //---recargamos el impuesto de ISR:
                //vemos si solo recargamos el impuesto; esto al ser un recibo de nomina hecho de una plantilla
                NominaComprobanteDescripcion[] ncdList = new NominaComprobanteDescripcionDaoImpl(user.getConn()).findWhereIdCromprobanteFiscalEquals(cfdiSesion.getIdComprobanteFiscal());
                NominaComprobanteDescripcion ncd = null;
                if(ncdList != null){
                    ncd = ncdList[0];
                }
                
                if(ncd != null){
                    for (ImpuestoSesion itemImpuesto : cfdiSesion.getListaImpuesto()){
                        System.out.println("____________________RECARGANDO VALORES DE IMPUESTO ISR");
                        itemImpuesto.setIdImpuesto(0);
                        itemImpuesto.setNombre("ISR");
                        itemImpuesto.setDescripcion("IMPUESTO SOBRE LA RENTA");
                        itemImpuesto.setPorcentaje(ncd.getIsrImpuestoPorcentaje());
                        itemImpuesto.setTrasladado(false);
                        itemImpuesto.setImplocal(false);
                        itemImpuesto.setMonto(ncd.getIsrMontoImpuesto());
                    }
                }
                //---
                request.getSession().setAttribute("cfdiSesion", cfdiSesion);
                System.out.println("_____________-RECARGANDO TODO.....");
                NominaAutomaticaGenerarComprobanteFiscaBO generarComprobanteFiscaBO = new NominaAutomaticaGenerarComprobanteFiscaBO();
                procesadoExitosamente = generarComprobanteFiscaBO.generarComprobante(cfdiSesion, tipoComprobanteNomina, "egreso", user.getConn(), fechaPago, fechaPago_Inicial, fechaPago_Final);
                if(procesadoExitosamente){
                    contadorDeExitosos++;
                    iDsComproNuevos.add(generarComprobanteFiscaBO.idComprobanteGenerado); //recuperamos el id de comprobante generado para almacenarlo en sesion y listar unicamente los comprobantes nuevos generados                
                }else{
                    contadorDeFallidos++;
                    foliosFallidos += ", "+idCopiaNomina;
                }
            }else{
                msgError += "<ul> No se específico el identificador (ID) del Comprobante Fiscal a recuperar. Intente de nuevo.";
            }
        }
        if(contadorDeExitosos > 0){//si hay comprobantes generados exitosamente cargamos en sesion los ID's 
            request.getSession().setAttribute("iDsComproNuevos", iDsComproNuevos);//Cargamos en sesion el listado de ID's de comprobantes generados exitosamente
        }
        String mensaje = "Número de comprobante generados satisfactoriamente: <b> " + contadorDeExitosos + "</b>";
        if(contadorDeFallidos>0)
            mensaje += "<br/> Número de comprobante no Generados : <b> " + contadorDeFallidos + "</b><br/>"+foliosFallidos;
        
            out.print(mensaje);  
    }
    //*fin de carga de IDs
    
    
    
    if (msgError.equals("") && mode!=1 
            && mode!=12 && mode!=13
            && mode!=21 && mode!=23
            && mode!=26){
        out.print("<!--EXITO-->");
    }
    
       
    if(recargarListaProductos){
        if (msgError.equals("")){
            Concepto concepto = null;
            ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
%>
                <table class="data" width="100%" cellpadding="0" cellspacing="0">
                    <thead>
                        <tr>
                            <td colspan="7"><center>Seleccionados</center></td>
                        </tr>
                    </thead>
                    <tbody>
<%
                int j=0;
                for (ProductoSesion itemProductoSesion : cfdiSesion.getListaProducto()){
                    concepto = null;

                    try{
                       conceptoBO = new ConceptoBO(itemProductoSesion.getIdProducto(),user.getConn());
                       concepto = conceptoBO.getConcepto();
                    }catch(Exception ex){
                        msgError += "Ocurrio un error al consultar el producto elegido con ID: "+itemProductoSesion.getIdProducto()+". " + ex.toString();
                    }
                    if (concepto!=null){
                        //int id = concepto.getIdconcepto();
                        int id = j;
%>
                        <tr>
                            <td>
                                <input type="hidden" name="producto_id_<%=id%>" id="producto_id_<%=id%>" readonly value="<%=concepto.getIdConcepto()%>"/>                                       
                                <input type="text" maxlength="100" name="producto_nombre_<%=id%>" id="producto_nombre_<%=id%>" size="18" readonly
                                       disabled value="<%=conceptoBO.getNombreConceptoLegible() %>"/>
                                
                            </td>
                            <td>
                                <textarea name="producto_descripcion_<%=id%>" id="producto_descripcion_<%=id%>" 
                                       rows="3" cols="35" readonly
                                       disabled><%= itemProductoSesion.getDescripcionAlternativa()!=null?(itemProductoSesion.getDescripcionAlternativa().trim().length()>0?itemProductoSesion.getDescripcionAlternativa():conceptoBO.getConcepto().getDescripcion()) :conceptoBO.getConcepto().getDescripcion() %></textarea>
                            </td>
                            <!--<td>
                                <input type="text" maxlength="100" name="producto_unidad_<%=id%>" id="producto_unidad_<%=id%>" size="18" readonly
                                       disabled value="<%=itemProductoSesion.getUnidad() %>"/>
                            </td>-->
                            <td>
                                <input type="text" maxlength="10" name="producto_precio_<%=id%>" id="producto_precio_<%=id%>" size="10"  style="text-align: right;" readonly
                                       disabled value="<%=itemProductoSesion.getPrecio() %>"/>
                            </td>
                            <td>
                                <input type="text" maxlength="8" name="producto_cantidad_<%=id%>" id="producto_cantidad_<%=id%>" style="text-align: right;" size="8"
                                       disabled value="<%=itemProductoSesion.getCantidad() %>"/>
                            </td>
                            <td>
                                <input type="text" maxlength="16" name="producto_monto_<%=id%>" id="producto_monto_<%=id%>" style="text-align: right;" size="10"
                                       disabled value="<%=itemProductoSesion.getMonto() %>"/>
                            </td>
                            <td>
                                <a href="javascript:void(0);" onclick="subProducto(<%=id%>);" id="sub_producto_action_<%=id%>" title="Quitar Producto">
                                    <img src="../../images/minus.png" alt="Quitar Producto" height="20" width="20" title="Quitar Producto" class="help"/>
                                </a>
                                &nbsp;&nbsp;&nbsp;&nbsp;
                                <a class="modalbox_iframe" href="../cfdi_factura/cfdi_factura_editaProducto_form.jsp?type=comprobanteFiscal&index_lista_producto=<%=id%>"
                                    title="Editar campo" >
                                    <img src="../../images/icon_edit.png" alt="Modificar Producto" title="Modificar Producto" class="help" height="20" width="20"/>
                                </a>
                            </td>
                        </tr>
                        <script>
                            activarFancyBox();
                        </script>
<%
                    }
                    j++;
                }
                if(j<=0){
%>
                    <tr>
                        <td colspan="7"><h4>Agregue un producto para comenzar</h4></td>
                    </tr>
                <%}else{%>
                    <tr>
                        <td colspan="7">
                            <a href="javascript:void(0);" onclick="reiniciarListaProducto();" id="reiniciar_productos_action" style="text-align: right; float: right;">
                                    <img src="../../images/icon_cancel.png" alt="Quitar Producto" title="Quitar Todos los Producto" class="help" height="20" width="20"/> Quitar Todos 
                            </a>
                        </td>
                    </tr>
                <%}%>
                    
                    <tr style="text-align: right;">
                        <td colspan="7">
                            <h3>Subtotal:
                            <input type="text" maxlength="16" name="producto_subtotal" id="producto_subtotal" style="text-align: right;" size="10"
                                   readonly value="<%= cfdiSesion.getSubTotalProductos() %>"/>
                            </h3>
                        </td>
                    </tr>
                </tbody>
            </table>
<%
            }
    }
    

    if(recargarListaServicios){
        if (msgError.equals("")){
            Servicio servicio = null;
            ServicioBO servicioBO = new ServicioBO(user.getConn());
%>
                <table class="data" width="100%" cellpadding="0" cellspacing="0">
                    <thead>
                        <tr>
                            <td colspan="7"><center>Seleccionados</center></td>
                        </tr>
                    </thead>
                    <tbody>
<%
                int j=0;
                for (ServicioSesion itemServicioSesion : cfdiSesion.getListaServicio()){
                    servicio = null;

                    try{
                       servicioBO = new ServicioBO(itemServicioSesion.getIdServicio(),user.getConn());
                       servicio = servicioBO.getServicio();
                    }catch(Exception ex){
                        msgError += "Ocurrio un error al consultar el servicio elegido con ID: "+itemServicioSesion.getIdServicio()+". " + ex.toString();
                    }
                    if (servicio!=null){
                        //int id = servicio.getIdservicio();
                        int id = j;
%>
                        <tr>
                            <td>
                                <input type="hidden" name="servicio_id_<%=id%>" id="servicio_id_<%=id%>" readonly value="<%=servicio.getIdServicio()%>"/>                                       
                                <input type="text" maxlength="100" name="servicio_nombre_<%=id%>" id="servicio_nombre_<%=id%>" size="18" readonly
                                       disabled value="<%=servicioBO.getServicio().getNombre() %>"/>
                                
                            </td>
                            <td>
                                <textarea name="servicio_descripcion_<%=id%>" id="servicio_descripcion_<%=id%>" 
                                       rows="3" cols="35" readonly
                                       disabled><%=servicioBO.getServicio().getDescripcion() %></textarea>
                            </td>
                            <td>
                                <input type="text" maxlength="10" name="servicio_precio_<%=id%>" id="servicio_precio_<%=id%>" size="10"  style="text-align: right;" readonly
                                       disabled value="<%=itemServicioSesion.getPrecio() %>"/>
                            </td>
                            <td>
                                <input type="text" maxlength="8" name="servicio_cantidad_<%=id%>" id="servicio_cantidad_<%=id%>" style="text-align: right;" size="8"
                                       disabled value="<%=itemServicioSesion.getCantidad() %>"/>
                            </td>
                            <td>
                                <input type="text" maxlength="16" name="servicio_monto_<%=id%>" id="servicio_monto_<%=id%>" style="text-align: right;" size="10"
                                       disabled value="<%=itemServicioSesion.getMonto() %>"/>
                            </td>
                            <td>
                                <a href="javascript:void(0);" onclick="subServicio(<%=id%>);" id="sub_servicio_action_<%=id%>" title="Quitar Servicio">
                                    <img src="../../images/minus.png" alt="Quitar Servicio" height="20" width="20" title="Quitar Servicio" class="help"/>
                                </a>
                                &nbsp;&nbsp;&nbsp;&nbsp;
                                <a class="modalbox_iframe" href="../cfdi_factura/cfdi_factura_editaServicio_form.jsp?type=comprobanteFiscal&index_lista_servicio=<%=id%>"
                                    title="Editar campo" >
                                    <img src="../../images/icon_edit.png" alt="Modificar Servicio" title="Modificar Servicio" class="help" height="20" width="20"/>
                                </a>
                            </td>
                        </tr>
                        <script>
                            activarFancyBox();
                        </script>
<%
                    }
                    j++;
                }
                if(j<=0){
%>
                    <tr>
                        <td colspan="7"><h4>Agregue un servicio para comenzar</h4></td>
                    </tr>
                <%}else{%>
                    <tr>
                        <td colspan="7">
                            <a href="javascript:void(0);" onclick="reiniciarListaServicio();" id="reiniciar_servicios_action" style="text-align: right; float: right;">
                                    <img src="../../images/icon_cancel.png" alt="Quitar Servicio" title="Quitar Todos los Servicio" class="help" height="20" width="20"/> Quitar Todos 
                            </a>
                        </td>
                    </tr>
                <%}%>
                    
                    <tr style="text-align: right;">
                        <td colspan="7">
                            <h3>Subtotal:
                            <input type="text" maxlength="16" name="servicio_subtotal" id="servicio_subtotal" style="text-align: right;" size="10"
                                   readonly value="<%= cfdiSesion.getSubTotalServicios() %>"/>
                            </h3>
                        </td>
                    </tr>
                </tbody>
            </table>
<%
            }
    }
    
    if(recargarListaPercepcions){
        if (msgError.equals("")){
            System.out.println("_____________-RECARGANDO PERCEPCIONES.....");
            NominaPercepcion percepcion = null;
            NominaPercepcionBO percepcionBO = new NominaPercepcionBO(user.getConn());
%>
                <table class="data" width="100%" cellpadding="0" cellspacing="0">
                    <thead>
                        <tr>
                            <td colspan="7"><center>Seleccionados</center></td>
                        </tr>
                    </thead>
                    <tbody>
<%
                int j=0;
                for (PercepcionSesion itemPercepcionSesion : cfdiSesion.getListaPercepcion()){
                    percepcion = null;
System.out.println("_____________-RECARGANDO PERCEPCIONES ID A BUSCAR:...."+itemPercepcionSesion.getIdPercepcion());
System.out.println("_____________-RECARGANDO PERCEPCIONES ID A BUSCAR:...."+itemPercepcionSesion.getDescipcionConcepto());
                    try{
                       percepcionBO = new NominaPercepcionBO(itemPercepcionSesion.getIdPercepcion(),user.getConn());
                       percepcion = percepcionBO.getNominaPercepcion();
                    }catch(Exception ex){
                        msgError += "Ocurrio un error al consultar la percepcion elegida con ID: "+itemPercepcionSesion.getIdPercepcion()+". " + ex.toString();
                    }
                    if (percepcion!=null){
                        //int id = concepto.getIdconcepto();
                        int id = j;
                         System.out.println("_____________-RECARGANDO PERCEPCIONES en if.....");
%>
                        <tr>
                            <td>
                                <input type="hidden" name="percepcion_id_<%=id%>" id="percepcion_id_<%=id%>" readonly value="<%=percepcion.getIdNominaPercepcion()%>"/>                                       
                                <input type="text" maxlength="100" name="percepcion_nombre_<%=id%>" id="percepcion_nombre_<%=id%>" size="18" readonly
                                       disabled value="<%=percepcionBO.getNominaPercepcion().getClave() %>"/>
                                
                            </td>
                            <td>
                                <textarea name="percepcion_descripcion_<%=id%>" id="percepcion_descripcion_<%=id%>" 
                                       rows="3" cols="35" readonly
                                       disabled><%= itemPercepcionSesion.getDescripcionConceptoAlterna()!=null?(itemPercepcionSesion.getDescripcionConceptoAlterna().trim().length()>0?itemPercepcionSesion.getDescripcionConceptoAlterna():percepcionBO.getNominaPercepcion().getConcepto()) :percepcionBO.getNominaPercepcion().getConcepto() %></textarea>
                            </td>
                            <td>
                                <input type="text" maxlength="10" name="percepcion_precio_gravado_<%=id%>" id="percepcion_precio_gravado_<%=id%>" size="10"  style="text-align: right;" readonly
                                       disabled value="<%=itemPercepcionSesion.getImporteGravado() %>"/>
                            </td>
                            <td>
                                <input type="text" maxlength="10" name="percepcion_precio_exento_<%=id%>" id="percepcion_precio_exento_<%=id%>" size="10"  style="text-align: right;" readonly
                                       disabled value="<%=itemPercepcionSesion.getImporteExento() %>"/>
                            </td>
                            <!--<td>
                                <input type="text" maxlength="8" name="percepcion_cantidad_<%=id%>" id="percepcion_cantidad_<%=id%>" style="text-align: right;" size="8"
                                       disabled value="<%=itemPercepcionSesion.getCantidad() %>"/>
                            </td>
                            <td>
                                <input type="text" maxlength="16" name="percepcion_monto_<%=id%>" id="percepcion_monto_<%=id%>" style="text-align: right;" size="10"
                                       disabled value="<%=itemPercepcionSesion.getImporteExento() %>"/>
                            </td>-->
                            <td>
                                <!--<a href="javascript:void(0);" onclick="subPercepcion(<%=id%>);" id="sub_percepcion_action_<%=id%>" title="Quitar Percepción">
                                    <img src="../../images/minus.png" alt="Quitar Percepción" height="20" width="20" title="Quitar Percepción" class="help"/>
                                </a>-->
                                &nbsp;&nbsp;&nbsp;&nbsp;
                                <!--<a class="modalbox_iframe" href="../cfdi_factura/cfdi_factura_editaProducto_form.jsp?type=comprobanteFiscal&index_lista_percepcion=<%=id%>"
                                    title="Editar campo" >
                                    <img src="../../images/icon_edit.png" alt="Modificar Producto" title="Modificar Producto" class="help" height="20" width="20"/>
                                </a>-->
                            </td>
                        </tr>
                        <script>
                            activarFancyBox();
                        </script>
<%
                    }
                    j++;
                }
                if(j<=0){
%>
                    <tr>
                        <td colspan="7"><h4>Agregue una percepción para comenzar</h4></td>
                    </tr>
                <%}else{%>
                    <tr>
                        <!--<td colspan="7">
                            <a href="javascript:void(0);" onclick="reiniciarListaProducto();" id="reiniciar_percepcions_action" style="text-align: right; float: right;">
                                    <img src="../../images/icon_cancel.png" alt="Quitar Percepcion" title="Quitar Todas las Percepciones" class="help" height="20" width="20"/> Quitar Todos 
                            </a>
                        </td>
                    </tr>-->
                <%}%>
                    
                    <!--<tr style="text-align: right;">
                        <td colspan="7">
                            <h3>Subtotal:
                            <input type="text" maxlength="16" name="percepcion_subtotal" id="percepcion_subtotal" style="text-align: right;" size="10"
                                   readonly value="<//%= cfdiSesion.getSubTotalProductos() %>"/>
                            </h3>
                        </td>
                    </tr>-->
                </tbody>
            </table>
<%
            }
    }
    
    if(recargarListaDeduccions){
        if (msgError.equals("")){
            NominaDeduccion deduccion = null;
            NominaDeduccionBO deduccionBO = new NominaDeduccionBO(user.getConn());
%>
                <table class="data" width="100%" cellpadding="0" cellspacing="0">
                    <thead>
                        <tr>
                            <td colspan="7"><center>Seleccionados</center></td>
                        </tr>
                    </thead>
                    <tbody>
<%
                int j=0;
                for (DeduccionSesion itemDeduccionSesion : cfdiSesion.getListaDeduccion()){
                    deduccion = null;

                    try{
                       deduccionBO = new NominaDeduccionBO(itemDeduccionSesion.getIdDeduccion(),user.getConn());
                       deduccion = deduccionBO.getNominaDeduccion();
                    }catch(Exception ex){
                        msgError += "Ocurrio un error al consultar la deduccion elegida con ID: "+itemDeduccionSesion.getIdDeduccion()+". " + ex.toString();
                    }
                    if (deduccion!=null){
                        //int id = concepto.getIdconcepto();
                        int id = j;
%>
                        <tr>
                            <td>
                                <input type="hidden" name="deduccion_id_<%=id%>" id="deduccion_id_<%=id%>" readonly value="<%=deduccion.getIdNominaDeduccion()%>"/>                                       
                                <input type="text" maxlength="100" name="deduccion_nombre_<%=id%>" id="deduccion_nombre_<%=id%>" size="18" readonly
                                       disabled value="<%=deduccionBO.getNominaDeduccion().getClave() %>"/>
                                
                            </td>
                            <td>
                                <textarea name="deduccion_descripcion_<%=id%>" id="deduccion_descripcion_<%=id%>" 
                                       rows="3" cols="35" readonly
                                       disabled><%= itemDeduccionSesion.getDescripcionConceptoAlterna()!=null?(itemDeduccionSesion.getDescripcionConceptoAlterna().trim().length()>0?itemDeduccionSesion.getDescripcionConceptoAlterna():deduccionBO.getNominaDeduccion().getConcepto()) :deduccionBO.getNominaDeduccion().getConcepto() %></textarea>
                            </td>
                            <td>
                                <input type="text" maxlength="10" name="deduccion_precio_gravado_<%=id%>" id="deduccion_precio_gravado_<%=id%>" size="10"  style="text-align: right;" readonly
                                       disabled value="<%=itemDeduccionSesion.getImporteGravado() %>"/>
                            </td>
                            <td>
                                <input type="text" maxlength="10" name="deduccion_precio_exento_<%=id%>" id="deduccion_precio_exento_<%=id%>" size="10"  style="text-align: right;" readonly
                                       disabled value="<%=itemDeduccionSesion.getImporteExento() %>"/>
                            </td>
                            <!--<td>
                                <input type="text" maxlength="8" name="deduccion_cantidad_<%=id%>" id="deduccion_cantidad_<%=id%>" style="text-align: right;" size="8"
                                       disabled value="<%=itemDeduccionSesion.getCantidad() %>"/>
                            </td>
                            <td>
                                <input type="text" maxlength="16" name="deduccion_monto_<%=id%>" id="deduccion_monto_<%=id%>" style="text-align: right;" size="10"
                                       disabled value="<%=itemDeduccionSesion.getImporteExento() %>"/>
                            </td>-->
                            <td>
                                <!--<a href="javascript:void(0);" onclick="subDeduccion(<%=id%>);" id="sub_deduccion_action_<%=id%>" title="Quitar Percepción">
                                    <img src="../../images/minus.png" alt="Quitar Percepción" height="20" width="20" title="Quitar Percepción" class="help"/>
                                </a>-->
                                &nbsp;&nbsp;&nbsp;&nbsp;
                                <!--<a class="modalbox_iframe" href="../cfdi_factura/cfdi_factura_editaProducto_form.jsp?type=comprobanteFiscal&index_lista_deduccion=<%=id%>"
                                    title="Editar campo" >
                                    <img src="../../images/icon_edit.png" alt="Modificar Producto" title="Modificar Producto" class="help" height="20" width="20"/>
                                </a>-->
                            </td>
                        </tr>
                        <script>
                            activarFancyBox();
                        </script>
<%
                    }
                    j++;
                }
                if(j<=0){
%>
                    <tr>
                        <td colspan="7"><h4>Agregue una deducción para comenzar</h4></td>
                    </tr>
                <%}else{%>
                    <tr>
                        <!--<td colspan="7">
                            <a href="javascript:void(0);" onclick="reiniciarListaProducto();" id="reiniciar_deduccions_action" style="text-align: right; float: right;">
                                    <img src="../../images/icon_cancel.png" alt="Quitar Deduccion" title="Quitar Todas las Deducciones" class="help" height="20" width="20"/> Quitar Todos 
                            </a>
                        </td>
                    </tr>-->
                <%}%>
                    
                    <tr style="text-align: right;">
                        <!--<td colspan="7">
                            <h3>Subtotal:
                            <input type="text" maxlength="16" name="deduccion_subtotal" id="deduccion_subtotal" style="text-align: right;" size="10"
                                   readonly value="<//%= cfdiSesion.getSubTotalProductos() %>"/>
                            </h3>
                        </td>
                    </tr>-->
                </tbody>
            </table>
<%
            }
    }

    if(recargarListaImpuestos){
        if (msgError.equals("")){
%>
                <table class="data" width="100%" cellpadding="0" cellspacing="0">
                    <thead>
                        <tr>
                            <td colspan="3"><center>Seleccionados</center></td>
                        </tr>
                        <tr>
                            <td>Impuesto</td>
                            <td>Monto</td>
                            <td>Acciones</td>
                        </tr>
                    </thead>
                    <tbody>
<%
                int j=0;
                for (ImpuestoSesion item : cfdiSesion.getListaImpuesto()){
                    if (item!=null){
                        //double montoImpuesto = (item.getPorcentaje()/100) * cfdiSesion.getSubTotalProductos().doubleValue();
                        double montoImpuesto =  cfdiSesion.getMontoImpuesto(j).doubleValue();
%>
                        <tr>
                            <td>
                                <input type="hidden" name="impuesto_id_<%=j%>" id="impuesto_id_<%=j%>" value="<%=item.getIdImpuesto()%>" readonly/>
                                <input type="text" name="impuesto_nombre_<%=j%>" id="impuesto_nombre_<%=j%>" 
                                       style="width: 250px" readonly disabled value="<%=item.getNombre() + " (" + item.getPorcentaje() +" %)" %>"/>
                            </td>
                            <td>
                                <input type="text" name="impuesto_monto_<%=j%>" id="impuesto_monto_<%=j%>" 
                                       style="width: 100px; text-align: right;" readonly disabled value="<%=montoImpuesto%>"/>
                            </td>
                            <td>
                                <a href="javascript:void(0);" onclick="subImpuesto(<%=j%>);" id="sub_impuesto_action_<%=j%>" title="Quitar Impuesto">
                                    <img src="../../images/minus.png" alt="Quitar Impuesto" height="20" width="20"/>
                                </a>
                            </td>
                        </tr>
<%
                    }
                    j++;
                }
                if(j<=0){
%>
                        <tr>
                            <td colspan="3"><h4>Selecciona algunos impuestos para el Comprobante Fiscal.</h4></td>
                        </tr>
<%              }%>
                        <tr style="text-align: right;">
                            <td colspan="3">
                                <h3>Subtotal Traslados (+):
                                <input type="text" maxlength="16" name="subtotalImpuestos_traslados" id="subtotalImpuestos_traslados" style="text-align: right;" size="10"
                                    readonly value="<%= cfdiSesion.getSubTotalImpuestos_Traslados() %>"/>
                                </h3>
                            </td>
                        </tr>
                        <tr style="text-align: right;">
                            <td colspan="3">
                                <h3>Subtotal Retenciones (-):
                                <input type="text" maxlength="16" name="subtotalImpuestos_retenciones" id="subtotalImpuestos_retenciones" style="text-align: right;" size="10"
                                    readonly value="<%= cfdiSesion.getSubTotalImpuestos_Retenciones() %>"/>
                                </h3>
                            </td>
                        </tr>
                </tbody>
            </table>
<%
            }
    }
    
    if (recargarSelectEmpleados){
%>
        <select size="1" id="id_cliente" name="id_cliente" class="flexselect"
            onchange="selectCliente(this.value)"
            style="width: 300px;">
            <option value="-1"></option>
            <%= new NominaEmpleadoBO(user.getConn()).getNominaEmpleadosByIdHTMLCombo(user.getUser().getIdEmpresa(), idCliente, " AND ID_ESTATUS=1 ")%>
        </select>
<%
    }
    
%>

 <%
    if (!msgError.equals("")){
        out.print("<!--ERROR-->"+msgError);
    }
%>