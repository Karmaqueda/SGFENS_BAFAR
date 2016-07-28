<%-- 
    Document   : cotizacion_ajax
    Created on : 26-nov-2012, 16:27:45
    Author     : ISCesarMartinez
--%>
<%@page import="com.tsp.sct.dao.dto.ClientePrecioConcepto"%>
<%@page import="com.tsp.sct.bo.ClientePrecioConceptoBO"%>
<%@page import="com.tsp.sgfens.sesion.PedidoSesion"%>
<%@page import="java.io.File"%>
<%@page import="com.tsp.sgfens.sesion.ComprobanteFiscalSesion"%>
<%@page import="com.tsp.sgfens.sesion.FormatoSesion"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProspecto"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.bo.SGProspectoBO"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensCotizacion"%>
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
<%@page import="com.tsp.sct.bo.SGCotizacionBO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.tsp.sgfens.sesion.ProductoSesion"%>
<%@page import="com.tsp.sct.dao.jdbc.ConceptoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.tsp.sgfens.sesion.CotizacionSesion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="cotizacionSesion" scope="session" class="com.tsp.sgfens.sesion.CotizacionSesion"/>
<jsp:useBean id="cfdiSesion" scope="session" class="com.tsp.sgfens.sesion.ComprobanteFiscalSesion"/>
<jsp:useBean id="pedidoSesion" scope="session" class="com.tsp.sgfens.sesion.PedidoSesion"/>
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
    *        6  =  Reiniciar Cotizacion (Borrar todo)
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
    *        19 =  Guardar Cotización (Sesion -> BD)  
    * 
    *        20 =  Recargar Select Clientes
    *        21 =  Recargar Select Prospectos
    *        22 =  Seleccionar Cliente (obtener datos)
    *        23 =  Seleccionar Prospecto (obtener datos)
    * 
    *        24 =  Convertir a Factura (sesion cotizacion -> sesion comprobante fiscal)
    *        25 =  Convertir a Pedido (sesion cotizacion -> sesion pedido)
    */
    int mode=-1;
    try{
        mode = Integer.parseInt(request.getParameter("mode"));
    }catch(NumberFormatException e){}
    boolean recargarListaProductos=false;
    boolean recargarListaServicios=false;
    boolean recargarListaImpuestos=false;
    
    boolean recargarSelectClientes=false;
    boolean recargarSelectProspectos=false;
    int idCliente = -1;
    int idProspecto = -1;
    
    CotizacionSesion cotizacionAux = cotizacionSesion;
    
    Gson gson = new Gson();
    
    GenericValidator genericValidator = new GenericValidator();
    
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
        idCliente = -1;
        try{
            idCliente = Integer.parseInt(request.getParameter("idCliente"));
        }catch(NumberFormatException e){}
        idProspecto = -1;
        try{
            idProspecto = Integer.parseInt(request.getParameter("idProspecto"));
        }catch(NumberFormatException e){}
        
        //Validacion
        if (idCliente>0 || idProspecto>0){
            if (idProducto>0){
                Concepto producto = new Concepto();
                ConceptoDaoImpl conceptoDaoImpl = new ConceptoDaoImpl(user.getConn());

                try{
                   producto = conceptoDaoImpl.findByPrimaryKey(idProducto);
                   
                   if (idCliente>0){
                       //Revisamos si tiene precios especiales
                       ClientePrecioConceptoBO clientePrecioBO = new ClientePrecioConceptoBO(user.getConn());
                       ClientePrecioConcepto[] clientePrecioDto = clientePrecioBO.findClientePrecioEspecial(idCliente, idProducto, -1, -1, "");

                       if(clientePrecioDto.length>0){
                           if(clientePrecioDto[0].getPrecioUnitarioCliente()>0){
                               producto.setPrecio((float)clientePrecioDto[0].getPrecioUnitarioCliente());
                           }
                           if(clientePrecioDto[0].getPrecioMedioCliente()>0){
                               producto.setPrecioMedioMayoreo(clientePrecioDto[0].getPrecioMedioCliente());
                           }
                           if(clientePrecioDto[0].getPrecioMayoreoCliente()>0){
                               producto.setPrecioMayoreo(clientePrecioDto[0].getPrecioMayoreoCliente());
                           }
                           if(clientePrecioDto[0].getPrecioDocenaCliente()>0){
                               producto.setPrecioDocena(clientePrecioDto[0].getPrecioDocenaCliente());
                           }
                           if(clientePrecioDto[0].getPrecioEspecialCliente()>0){
                               producto.setPrecioEspecial(clientePrecioDto[0].getPrecioEspecialCliente());
                           }

                       }
                   }
                   
                   
                   
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
        }else{
            msgError="No se selecciono un Cliente o Prospecto válido.";
        }
        
    }else if (mode==2){
        //Modo 2 = agregar producto
        
        //Parametros
        int idProducto = -1;
        double cantidad =-1;
        double precio =-1;
        double monto=-1;
        String unidad = request.getParameter("producto_unidad") != null ? request.getParameter("producto_unidad") : "No aplica";
        try{ idProducto = Integer.parseInt(request.getParameter("producto_id")); }catch(Exception e){}
        try{ cantidad = Double.parseDouble(request.getParameter("producto_cantidad")); }catch(Exception e){}
        try{ precio = Double.parseDouble(request.getParameter("producto_precio")); }catch(Exception e){}
        try{ monto = Double.parseDouble(request.getParameter("producto_monto")); }catch(Exception e){}
        
        //Validaciones
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
        
        if (msgError.equals("")){
            Concepto productoCompra = null;
            ConceptoDaoImpl conceptoDaoImpl = new ConceptoDaoImpl(user.getConn());

            try{
               productoCompra = conceptoDaoImpl.findByPrimaryKey(idProducto);
            }catch(Exception ex){
                msgError = "Ocurrio un error al consultar el producto elegido. " + ex.toString();
            }
            
            try{
                if (cotizacionSesion==null){
                    cotizacionSesion = new CotizacionSesion();
                }

                ProductoSesion nuevoProductoCompraSesion = new ProductoSesion();
                nuevoProductoCompraSesion.setCantidad(cantidad);
                nuevoProductoCompraSesion.setIdProducto(idProducto);
                nuevoProductoCompraSesion.setMonto(monto);
                nuevoProductoCompraSesion.setPrecio(precio);
                nuevoProductoCompraSesion.setUnidad(unidad);
                
                if (productoCompra!=null){
                    boolean coincidenciaPrevia=false;
                    for (ProductoSesion itemProducto:cotizacionSesion.getListaProducto()){
                        if (itemProducto.getIdProducto()==productoCompra.getIdConcepto()){
                            //Ya se había seleccionado este producto
                            coincidenciaPrevia=true;
                            break;
                        }
                    }
                    if (!coincidenciaPrevia){
                        cotizacionSesion.getListaProducto().add(nuevoProductoCompraSesion);
                    }else{
                        msgError +="<ul> El producto ya fue seleccionado previamente. Modifique los valores o elimine el producto del listado.";
                    }
                        
                }
                
                request.getSession().setAttribute("cotizacionSesion", cotizacionSesion);
            }catch(Exception ex){
                msgError += "Ocurrio un error al recuperar los productos elegidos previamente en la sesion. " + ex.toString();
            }
            
            recargarListaProductos=true;
        }
        
    }else if (mode==3){
        //Reiniciar lista de productos
        if (cotizacionSesion==null)
            cotizacionSesion = new CotizacionSesion();
        cotizacionSesion.setListaProducto(new ArrayList<ProductoSesion>());
        request.getSession().setAttribute("cotizacionSesion", cotizacionSesion);
        
        recargarListaProductos=true;
    }else if (mode==4){
        //Quitar producto de lista
        if (cotizacionSesion==null)
            cotizacionSesion = new CotizacionSesion();
        int index_lista_producto=-1;
        try{ index_lista_producto = Integer.parseInt(request.getParameter("index_lista_producto")); }catch(Exception e){}
        if (index_lista_producto<0)
            msgError = "<ul>Producto inválido. Si persiste el problema intente recargar esta página.";
        
        if (msgError.equals("")){
            try{
                cotizacionSesion.getListaProducto().remove(index_lista_producto);
            }catch(Exception ex){
                msgError+="El producto especificado no se encontro en el listado en sesion. Intente de nuevo.";
            }
            request.getSession().setAttribute("cotizacionSesion", cotizacionSesion);

            recargarListaProductos=true;
        }
    }else if (mode==5){
        //Recargar Lista de Productos
        if (cotizacionSesion==null)
            cotizacionSesion = new CotizacionSesion();
        request.getSession().setAttribute("cotizacionSesion", cotizacionSesion);
        recargarListaProductos=true;
    }else if (mode==6){
        //6 = Reiniciar Cotizacion (Borrar todo)
        cotizacionSesion = new CotizacionSesion();
        request.getSession().setAttribute("cotizacionSesion", cotizacionSesion);
    }else if (mode==7){
        //Reconstruir Sesion desde BD
        
        //Parametros
        int id_cotizacion = -1;
        try{ id_cotizacion = Integer.parseInt(request.getParameter("id_cotizacion")); }catch(Exception e){}
        
        //Procesamiento
        if (id_cotizacion>=0){
            SGCotizacionBO cotizacionSesionBO = new SGCotizacionBO(id_cotizacion,user.getConn());
            cotizacionSesion = cotizacionSesionBO.getSessionFromCotizacionDB();
            request.getSession().setAttribute("cotizacionSesion", cotizacionSesion);
        }else{
            msgError += "<ul> No se específico el identificador (ID) de la Cotizacion a recuperar. Intente de nuevo.";
        }
        
    }else if(mode==8){
        //Modo 8 = Modificar Producto
        if (cotizacionSesion==null)
            cotizacionSesion = new CotizacionSesion();
        
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
        if (!genericValidator.isValidString(unidad, 1, 100))
            msgError = "<ul>Unidad no especificada";
        
        if (msgError.equals("")){
            
            monto = precio * cantidad;
            
            try{
                ProductoSesion nuevoProductoSesion = new ProductoSesion();
                nuevoProductoSesion.setCantidad(cantidad);
                nuevoProductoSesion.setPrecio(precio);
                nuevoProductoSesion.setIdProducto(idProducto);
                nuevoProductoSesion.setMonto(monto);
                nuevoProductoSesion.setUnidad(unidad);
                
                cotizacionSesion.getListaProducto().set(index_lista_producto, nuevoProductoSesion);
            }catch(Exception ex){
                msgError+="El producto especificado no se pudo actualizar. Intente de nuevo.";
            }
            request.getSession().setAttribute("cotizacionSesion", cotizacionSesion);
            
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
                if (cotizacionSesion==null){
                    cotizacionSesion = new CotizacionSesion();
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
                    for (ImpuestoSesion itemImpuesto:cotizacionSesion.getListaImpuesto()){
                        if (itemImpuesto.getIdImpuesto()==impuestoDto.getIdImpuesto()){
                            //Ya se había seleccionado este impuesto
                            coincidenciaPrevia=true;
                            break;
                        }
                    }
                    if (!coincidenciaPrevia){
                        cotizacionSesion.getListaImpuesto().add(nuevoImpuestoCompraSesion);
                    }else{
                        msgError +="<ul> El impuesto ya fue seleccionado previamente.";
                    }
                        
                }
                
                request.getSession().setAttribute("cotizacionSesion", cotizacionSesion);
            }catch(Exception ex){
                msgError += "Ocurrio un error al recuperar los impuestos elegidos previamente en la sesion. " + ex.toString();
            }
            
            recargarListaImpuestos=true;
        }
        
    }else if(mode==10){
        //Modo 10 = Quitar Impuesto
        if (cotizacionSesion==null)
            cotizacionSesion = new CotizacionSesion();
        int index_lista_impuesto=-1;
        try{ index_lista_impuesto = Integer.parseInt(request.getParameter("index_lista_impuesto")); }catch(Exception e){}
        if (index_lista_impuesto<0)
            msgError = "<ul>Impuesto inválido. Si persiste el problema intente recargar esta página.";
        
        if (msgError.equals("")){
            try{
                cotizacionSesion.getListaImpuesto().remove(index_lista_impuesto);
            }catch(Exception ex){
                msgError+="El impuesto especificado no se encontro en el listado en sesion. Intente de nuevo.";
            }
            request.getSession().setAttribute("cotizacionSesion", cotizacionSesion);

            recargarListaImpuestos=true;
        }
        
    }else if(mode==11){
        //Modo 11 = Recargar Lista Impuestos
        if (cotizacionSesion==null)
            cotizacionSesion = new CotizacionSesion();
        request.getSession().setAttribute("cotizacionSesion", cotizacionSesion);
        recargarListaImpuestos=true;
    }else if(mode==12){
        //Modo 12 = Calcular/Aplicar Descuento
        if (cotizacionSesion==null)
            cotizacionSesion = new CotizacionSesion();
        String descuento_tasa = request.getParameter("descuento_tasa") != null ? request.getParameter("descuento_tasa") : "0";
        
        BigDecimal descuento_tasa_bigd = new BigDecimal(descuento_tasa).setScale(2, RoundingMode.HALF_UP);
        if (descuento_tasa_bigd.doubleValue()<0)
            msgError = "<ul>Descuento inválido. El descuento debe ser mayor o igual a 0 en porcentaje;";
        
        if (msgError.equals("")){
            try{
                cotizacionSesion.setDescuento_tasa(descuento_tasa_bigd.doubleValue());
            }catch(Exception ex){
                msgError+="Ocurrio un error al aplicar el descuento. Intente de nuevo.";
            }
            request.getSession().setAttribute("cotizacionSesion", cotizacionSesion);
        }
        
        if (msgError.equals("")){
            out.print("<!--EXITO-->"+cotizacionSesion.getDescuentoImporte());
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
        String unidad = request.getParameter("servicio_unidad") != null ? request.getParameter("servicio_unidad") : "No Aplica";
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
                if (cotizacionSesion==null){
                    cotizacionSesion = new CotizacionSesion();
                }

                ServicioSesion nuevoServicioCompraSesion = new ServicioSesion();
                nuevoServicioCompraSesion.setCantidad(cantidad);
                nuevoServicioCompraSesion.setIdServicio(idServicio);
                nuevoServicioCompraSesion.setMonto(monto);
                nuevoServicioCompraSesion.setPrecio(precio);
                nuevoServicioCompraSesion.setUnidad(unidad);
                
                if (servicioCompra!=null){
                    boolean coincidenciaPrevia=false;
                    for (ServicioSesion itemServicio:cotizacionSesion.getListaServicio()){
                        if (itemServicio.getIdServicio()==servicioCompra.getIdServicio()){
                            //Ya se había seleccionado este servicio
                            coincidenciaPrevia=true;
                            break;
                        }
                    }
                    if (!coincidenciaPrevia){
                        cotizacionSesion.getListaServicio().add(nuevoServicioCompraSesion);
                    }else{
                        msgError +="<ul> El servicio ya fue seleccionado previamente. Modifique los valores o elimine el servicio del listado.";
                    }
                        
                }
                
                request.getSession().setAttribute("cotizacionSesion", cotizacionSesion);
            }catch(Exception ex){
                msgError += "Ocurrio un error al recuperar los servicios elegidos previamente en la sesion. " + ex.toString();
            }
            
            recargarListaServicios=true;
        }
        
    }else if(mode==15){
        //Modo 15 = Reiniciar lista Servicio (borrar todos)
        if (cotizacionSesion==null)
            cotizacionSesion = new CotizacionSesion();
        cotizacionSesion.setListaServicio(new ArrayList<ServicioSesion>());
        request.getSession().setAttribute("cotizacionSesion", cotizacionSesion);
        
        recargarListaServicios=true;
    }else if(mode==16){
        //Modo 16 = Quitar Servicio de lista (borrar 1)
        
        if (cotizacionSesion==null)
            cotizacionSesion = new CotizacionSesion();
        int index_lista_servicio=-1;
        try{ index_lista_servicio = Integer.parseInt(request.getParameter("index_lista_servicio")); }catch(Exception e){}
        if (index_lista_servicio<0)
            msgError = "<ul>Servicio inválido. Si persiste el problema intente recargar esta página.";
        
        if (msgError.equals("")){
            try{
                cotizacionSesion.getListaServicio().remove(index_lista_servicio);
            }catch(Exception ex){
                msgError+="El servicio especificado no se encontro en el listado en sesion. Intente de nuevo.";
            }
            request.getSession().setAttribute("cotizacionSesion", cotizacionSesion);

            recargarListaServicios=true;
        }
        
    }else if(mode==17){
        //Modo 17 = Recargar lista de Servicios
        
        if (cotizacionSesion==null)
            cotizacionSesion = new CotizacionSesion();
        request.getSession().setAttribute("cotizacionSesion", cotizacionSesion);
        recargarListaServicios=true;
        
    }else if(mode==18){
        //Modo 18 = Modificar Servicio
        
        if (cotizacionSesion==null)
            cotizacionSesion = new CotizacionSesion();
        
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
                
                cotizacionSesion.getListaServicio().set(index_lista_servicio, nuevoServicioSesion);
            }catch(Exception ex){
                msgError+="El servicio especificado no se pudo actualizar. Intente de nuevo.";
            }
            request.getSession().setAttribute("cotizacionSesion", cotizacionSesion);
            
            if (msgError.equals(""))
                out.print("Servicio actualizado exitosamente");
        }
        
    }else if (mode==19){
        //19 =  Guardar Cotización (Sesion -> BD)  
        
        boolean enviarCorreo = false;
        
        //Parametros
        String comentarios = request.getParameter("comentarios")!=null?new String(request.getParameter("comentarios").getBytes("ISO-8859-1"),"UTF-8"):"";
        String tipo_moneda = request.getParameter("tipo_moneda")!=null?new String(request.getParameter("tipo_moneda").getBytes("ISO-8859-1"),"UTF-8"):"MXN";
        String descuento_motivo = request.getParameter("descuento_motivo")!=null?new String(request.getParameter("descuento_motivo").getBytes("ISO-8859-1"),"UTF-8"):"";
        String lista_correos = request.getParameter("lista_correos")!=null?new String(request.getParameter("lista_correos").getBytes("ISO-8859-1"),"UTF-8"):"";
        idCliente = -1;
        idProspecto = -1;
        
        try{ idCliente = Integer.parseInt(request.getParameter("id_cliente")); }catch(Exception e){}
        try{ idProspecto = Integer.parseInt(request.getParameter("id_prospecto")); }catch(Exception e){}
        
        String[] correos = new String[0];
        ArrayList<String> listCorreosValidos = new ArrayList<String>();
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
            
        //Validacion        
        if (!genericValidator.isValidString(comentarios, 1, 500))
            msgError = "<ul>El campo comentarios del pedido es obligatorio. Máximo 500 caracteres. ";
        
        if (idCliente<=0 && idProspecto<=0)
            msgError+="<ul> Debe seleccionar al menos un cliente o prospecto para generar la cotizacion. ";
        
        if (msgError.equals("")){
            if (cotizacionSesion==null){
                msgError += "<ul>No se ha inicializado la Cotizacion, esta vacía. ";
            }else{
                
                //Validaciones de la Solicitud de Compra
                if (cotizacionSesion.getListaProducto().size()<=0 && cotizacionSesion.getListaServicio().size()<=0)
                    msgError+="<ul> Debe seleccionar al menos un producto o servicio para generar la cotizacion. Lista de Productos y Servicios vacia. ";
                
                if (cotizacionSesion.getDescuento_tasa()>0 && !genericValidator.isValidString(descuento_motivo, 1, 200))
                    msgError+="<ul> Debe expresar el/los motivo(s) del descuento. Máximo 200 caracteres.";
                
                SgfensCotizacion cotizacionDto = null;
                File[] adjuntos = new File[0];
                if (msgError.equals("")){
                    cotizacionSesion.setComentarios(comentarios);
                    cotizacionSesion.setTipo_moneda(tipo_moneda);
                    cotizacionSesion.setDescuento_motivo(descuento_motivo);
                    
                    try{
                        if (idCliente>0){
                            cotizacionSesion.setCliente(new ClienteBO(idCliente,user.getConn()).getCliente());
                        }
                    }catch(Exception ex){ ex.printStackTrace();}
                    
                    try{
                        if (idProspecto>0){
                            cotizacionSesion.setProspecto(new SGProspectoBO(idProspecto,user.getConn()).getSgfensProspecto());
                        }
                    }catch(Exception ex){ ex.printStackTrace();}
                    
                    //Para generar una nueva cotizacion en cada Guardar
                    //Deshabilitar en caso de querer actualizar en lugar de generar una nueva
                    cotizacionSesion.setIdCotizacion(0);
                    
                    request.getSession().setAttribute("cotizacionSesion", cotizacionSesion);
                    cotizacionAux = cotizacionSesion;
                    
                    SGCotizacionBO cotizacionBO = new SGCotizacionBO(user.getConn());
                    try{
                        cotizacionDto = cotizacionBO.creaActualizaCotizacion(cotizacionSesion, user);
                    }catch(Exception ex){
                        msgError+= "<ul>Ocurrio un error al almacenar la cotizacion: <br/>" + ex.toString();
                    }

                    if (enviarCorreo){
                        cotizacionBO = new SGCotizacionBO(cotizacionDto,user.getConn());
                        try{
                            File cotizacionPDF = cotizacionBO.guardarRepresentacionImpresa(user);
                            adjuntos = new File[1];
                            adjuntos[0] = cotizacionPDF;
                        }catch(Exception ex){
                            out.print("<ul> No se pudo crear y adjuntar la representación impresa de la cotización.");
                            ex.printStackTrace();
                        }
                    }
                }
                
                if (msgError.equals("")){
                    out.print("Cotización almacenada satisfactoriamente. <br/><br/><b> Folio de Cotización: " + cotizacionDto.getFolioCotizacion() + "</b>");
                    
                    cotizacionSesion = new CotizacionSesion();
                    request.getSession().setAttribute("cotizacionSesion", cotizacionSesion);
                }
                
                if (msgError.equals("")){
                    try{
                        if (enviarCorreo){
                            SGCotizacionBO cotizacionBO = new SGCotizacionBO(cotizacionDto.getIdCotizacion(),user.getConn());
                            out.print("<ul>"+ cotizacionBO.enviaNotificacionNuevaCotizacion(listCorreosValidos, adjuntos,user.getUser().getIdEmpresa()));
                        }
                    }catch(Exception ex){
                        out.print("<ul> No se pudo enviar el correo de notificación.");
                    }
                }
                
            }
        }
        
    }else if (mode==20){
        //20 =  Recargar Select Clientes
        
        idCliente = -1;
        try{ idCliente = Integer.parseInt(request.getParameter("id_cliente")); }catch(Exception e){}
        recargarSelectClientes = true;
        
    }else if (mode==21){
        //21 =  Recargar Select Prospectos
        
        idProspecto = -1;
        try{ idProspecto = Integer.parseInt(request.getParameter("id_prospecto")); }catch(Exception e){}        
        recargarSelectProspectos = true;
        
    }else if (mode==22){
        //22 = Seleccionar Cliente (obtener datos)
        
        idCliente = -1;
        try{ idCliente = Integer.parseInt(request.getParameter("id_cliente")); }catch(Exception e){}
        
        Cliente clienteDto = new ClienteBO(idCliente,user.getConn()).getCliente();
        
        String jsonOutput = gson.toJson(clienteDto);
        out.print("<!--EXITO-->"+jsonOutput);
        
    }else if (mode==23){
        //23 = Seleccionar Prospecto (obtener datos)
        
        idProspecto = -1;
        try{ idProspecto = Integer.parseInt(request.getParameter("id_prospecto")); }catch(Exception e){}
        
        SgfensProspecto prospectoDto = new SGProspectoBO(idProspecto,user.getConn()).getSgfensProspecto();
        
        String jsonOutput = gson.toJson(prospectoDto);
        out.print("<!--EXITO-->"+jsonOutput);
        
    }else if (mode==24){
        //Convertir Cotizacion sesion a ComprobanteFiscalSesion
        
        //Parametros
        int id_cotizacion = -1;
        try{ id_cotizacion = Integer.parseInt(request.getParameter("id_cotizacion")); }catch(Exception e){}
        
        //Procesamiento
        if (id_cotizacion>=0){
            SGCotizacionBO cotizacionSesionBO = new SGCotizacionBO(id_cotizacion,user.getConn());
            cotizacionSesion = cotizacionSesionBO.getSessionFromCotizacionDB();
            
            FormatoSesion formatoSesion = (FormatoSesion) cotizacionSesion;
            ComprobanteFiscalSesion comprobanteFiscalSesion = new ComprobanteFiscalSesion();
            comprobanteFiscalSesion.setCliente(formatoSesion.getCliente());
            comprobanteFiscalSesion.setComentarios(formatoSesion.getComentarios());
            comprobanteFiscalSesion.setDescuento_motivo(formatoSesion.getDescuento_motivo());
            comprobanteFiscalSesion.setDescuento_tasa(formatoSesion.getDescuento_tasa());
            comprobanteFiscalSesion.setListaImpuesto(formatoSesion.getListaImpuesto());
            comprobanteFiscalSesion.setListaProducto(formatoSesion.getListaProducto());
            comprobanteFiscalSesion.setListaServicio(formatoSesion.getListaServicio());
            comprobanteFiscalSesion.setTipo_moneda(formatoSesion.getTipo_moneda());
            
            request.getSession().setAttribute("cfdiSesion", comprobanteFiscalSesion);
            
        }else{
            msgError += "<ul> No se específico el identificador (ID) de la Cotizacion a recuperar. Intente de nuevo.";
        }
        
    }else if (mode==25){
        //25 = Convertir Cotizacion sesion a PedidoSesion
        
        //Parametros
        int id_cotizacion = -1;
        try{ id_cotizacion = Integer.parseInt(request.getParameter("id_cotizacion")); }catch(Exception e){}
        
        //Procesamiento
        if (id_cotizacion>=0){
            SGCotizacionBO cotizacionSesionBO = new SGCotizacionBO(id_cotizacion,user.getConn());
            cotizacionSesion = cotizacionSesionBO.getSessionFromCotizacionDB();
            
            FormatoSesion formatoSesion = (FormatoSesion) cotizacionSesion;
            PedidoSesion pedidoSesionAux = new PedidoSesion();
            pedidoSesionAux.setCliente(formatoSesion.getCliente());
            pedidoSesionAux.setComentarios(formatoSesion.getComentarios());
            pedidoSesionAux.setDescuento_motivo(formatoSesion.getDescuento_motivo());
            pedidoSesionAux.setDescuento_tasa(formatoSesion.getDescuento_tasa());
            pedidoSesionAux.setListaImpuesto(formatoSesion.getListaImpuesto());
            pedidoSesionAux.setListaProducto(formatoSesion.getListaProducto());
            pedidoSesionAux.setListaServicio(formatoSesion.getListaServicio());
            pedidoSesionAux.setTipo_moneda(formatoSesion.getTipo_moneda());
            pedidoSesionAux.setIdCotizacionOrigen(cotizacionSesion.getIdCotizacion());
            pedidoSesionAux.setComentarios("Cotización ID "+cotizacionSesion.getIdCotizacion());
            pedidoSesionAux.setIdUsuarioVendedor(cotizacionSesion.getEmpleadoVendedor().getIdUsuarios());
                    
            request.getSession().setAttribute("pedidoSesion", pedidoSesionAux);
            
        }else{
            msgError += "<ul> No se específico el identificador (ID) de la Cotizacion a recuperar. Intente de nuevo.";
        }
        
    }
    
        
    if (msgError.equals("") && mode!=1 
            && mode!=12 && mode!=13
            && mode!=22 && mode!=23){
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
                for (ProductoSesion itemProductoSesion : cotizacionSesion.getListaProducto()){
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
                                       disabled><%=conceptoBO.getConcepto().getDescripcion() %></textarea>
                            </td>
                            <td>
                                <input type="text" maxlength="100" name="producto_unidad_<%=id%>" id="producto_unidad_<%=id%>" size="18" readonly
                                       disabled value="<%=itemProductoSesion.getUnidad() %>"/>
                            </td>
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
                                <a class="modalbox_iframe" href="../cotizacion/cotizacion_editaProducto_form.jsp?type=cotizacion&index_lista_producto=<%=id%>"
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
                                   readonly value="<%= cotizacionSesion.getSubTotalProductos() %>"/>
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
                for (ServicioSesion itemServicioSesion : cotizacionSesion.getListaServicio()){
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
                                <a class="modalbox_iframe" href="../cotizacion/cotizacion_editaServicio_form.jsp?type=cotizacion&index_lista_servicio=<%=id%>"
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
                                   readonly value="<%= cotizacionSesion.getSubTotalServicios() %>"/>
                            </h3>
                        </td>
                    </tr>
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
                for (ImpuestoSesion item : cotizacionSesion.getListaImpuesto()){
                    if (item!=null){
                        //double montoImpuesto = (item.getPorcentaje()/100) * cotizacionSesion.getSubTotalProductos().doubleValue();
                        double montoImpuesto =  cotizacionSesion.getMontoImpuesto(j).doubleValue();
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
                            <td colspan="3"><h4>Selecciona algunos impuestos para la cotización.</h4></td>
                        </tr>
<%              }%>
                        <tr style="text-align: right;">
                            <td colspan="3">
                                <h3>Subtotal Traslados (+):
                                <input type="text" maxlength="16" name="subtotalImpuestos_traslados" id="subtotalImpuestos_traslados" style="text-align: right;" size="10"
                                    readonly value="<%= cotizacionSesion.getSubTotalImpuestos_Traslados() %>"/>
                                </h3>
                            </td>
                        </tr>
                        <tr style="text-align: right;">
                            <td colspan="3">
                                <h3>Subtotal Retenciones (-):
                                <input type="text" maxlength="16" name="subtotalImpuestos_retenciones" id="subtotalImpuestos_retenciones" style="text-align: right;" size="10"
                                    readonly value="<%= cotizacionSesion.getSubTotalImpuestos_Retenciones() %>"/>
                                </h3>
                            </td>
                        </tr>
                </tbody>
            </table>
<%
            }
    }
    
    if (recargarSelectClientes){
%>
        <select size="1" id="id_cliente" name="id_cliente" class="flexselect" 
            onchange="selectCliente(this.value)"
            style="width: 300px;">
            <option value="-1"></option>
            <%= new ClienteBO(user.getConn()).getClientesByIdHTMLCombo(user.getUser().getIdEmpresa(), idCliente, " AND (ID_ESTATUS=1 OR ID_ESTATUS=3) " + (user.getUser().getIdRoles() == RolesBO.ROL_OPERADOR? " AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM SGFENS_CLIENTE_VENDEDOR WHERE ID_USUARIO_VENDEDOR="+user.getUser().getIdUsuarios()+")" : "") ) %>
        </select>
<%
    }
    
    if (recargarSelectProspectos){
%>
        <select size="1" id="id_prospecto" name="id_prospecto" class="flexselect" 
            onchange="selectProspecto(this.value);"
            style="width: 300px;">
            <option value="-1"></option>
            <%= new SGProspectoBO(user.getConn()).getProspectosByIdHTMLCombo(user.getUser().getIdEmpresa(), idProspecto) %>
        </select>
<%
    }
%>

 <%
    if (!msgError.equals("")){
        out.print("<!--ERROR-->"+msgError);
    }
%>
