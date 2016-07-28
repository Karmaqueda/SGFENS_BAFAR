<%-- 
    Document   : catMovimientos_ajax_barcode
    Created on : 8/02/2016, 01:10:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.util.Converter"%>
<%@page import="com.tsp.sct.bo.SGProveedorBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProveedor"%>
<%@page import="com.tsp.sct.dao.jdbc.ExistenciaAlmacenDaoImpl"%>
<%@page import="com.tsp.sct.dao.jdbc.MovimientoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Movimiento"%>
<%@page import="java.math.RoundingMode"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.tsp.sct.util.GenericMethods"%>
<%@page import="com.tsp.sct.bo.AlmacenBO"%>
<%@page import="com.tsp.sct.dao.dto.Almacen"%>
<%@page import="com.tsp.sct.dao.dto.ExistenciaAlmacen"%>
<%@page import="com.tsp.sct.bo.ExistenciaAlmacenBO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.tsp.sgfens.sesion.FormatoMovimientosSesion"%>
<%@page import="com.tsp.sgfens.sesion.MovimientoSesion"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="formatoMovimientoSesion" scope="session" class="com.tsp.sgfens.sesion.FormatoMovimientosSesion"/>
<%
    String msgError="";
    String msgExitoExtra="";
    
    /*
    *   Modos:
    *       -1  =  No se eligio (ERROR)
    * 
    *        1 = Agregar Producto
    *        2 = Reiniciar Lista Productos (borrar todo)
    *        3 = Quitar una línea de Producto
    *        4 = Recargar Lista Productos
    *        5 = Reiniciar Formato (borrar todo)
    *        6 = Mostrar imagen Producto
    *        7 = Calcula totales
    *        8 = Aplicar Ajustes (cambio de cantidades)
    *        9 = Iniciar/Re-iniciar Validación
    *       10 = Validar Producto
    *       11 = Terminar Validación
    *       12 = Buscar Proveedor por Codigo Barras
    *       ...
    *       15 = Guardar Movimientos
    */
    
    int mode=-1;
    try{
        mode = Integer.parseInt(request.getParameter("mode"));
    }catch(NumberFormatException e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
     
    GenericValidator gc = new GenericValidator();
    Gson gson = new Gson();
    ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
    
    boolean recargarListaProductos = false;
    boolean esValidacion = false;
    boolean mostrarImagenProducto = false;
    int exitoUltimoProducto = 0;
    
    if (formatoMovimientoSesion!=null){
        esValidacion = formatoMovimientoSesion.isModoValidacion();
    }
    
    /*
     * Procesamiento
     */
    if (mode==-1){
        msgError = "No se eligio una acción correcta. Reintente el llenado de los datos.";
    }else if (mode==1 && !esValidacion){
        //1 =  Agregar Producto
        if (formatoMovimientoSesion == null)
            formatoMovimientoSesion = new FormatoMovimientosSesion();
            
        //parametros
        int cantidad = 1;
        int idProducto = -1;
        String codigoBarras = request.getParameter("codigo_barras")!=null?new String(request.getParameter("codigo_barras").getBytes("ISO-8859-1"),"UTF-8"):"";
        try{ idProducto = Integer.parseInt(request.getParameter("id_producto")); }catch(Exception e){}
        
        //Procesamiento
        if (StringManage.getValidString(codigoBarras).length()>0
                || idProducto>0 ){
            String filtroBusqueda = " AND ID_ESTATUS=1 " 
                                  + " AND (ID_CONCEPTO_PADRE IS NULL OR ID_CONCEPTO_PADRE<=0)";
            
            if (StringManage.getValidString(codigoBarras).length()>0)
                filtroBusqueda+= " AND IDENTIFICACION='" + codigoBarras + "'";
            
            Concepto[] coincidencias = conceptoBO.findConceptos(idProducto, idEmpresa, 0, 0, filtroBusqueda);
            if (coincidencias.length>0){
                if (coincidencias.length == 1){
                    Concepto conceptoDto = coincidencias[0];
                    
                    boolean coincidenciaSesionPrevia = false;
                    for (MovimientoSesion movSesion : formatoMovimientoSesion.getListaMovimiento() ){
                        if (movSesion.getIdProducto() == conceptoDto.getIdConcepto()){
                            //coincidencia previa, sumamos cantidad
                            coincidenciaSesionPrevia = true;
                            movSesion.setCantidad(movSesion.getCantidad() + cantidad);
                            movSesion.calculaTotal();//calcula y asigna el total al objeto
                            
                            formatoMovimientoSesion.setIdConceptoUltimoAgregado(movSesion.getIdProducto());
                        } 
                    }                        
                    
                    if (!coincidenciaSesionPrevia){
                        //no existe coincidencia previa en sesion, agregamos un nuevo "renglon"
                        MovimientoSesion movSesion = new MovimientoSesion();
                        movSesion.setIdProducto(conceptoDto.getIdConcepto());
                        movSesion.setCantidad(cantidad);
                        movSesion.setCodigoBarras(conceptoDto.getIdentificacion());//codigoBarras);
                        movSesion.setPrecioVenta(conceptoDto.getPrecio());
                        movSesion.calculaTotal();//calcula y asigna el total al objeto
                        
                        formatoMovimientoSesion.getListaMovimiento().add(movSesion);
                        formatoMovimientoSesion.setIdConceptoUltimoAgregado(movSesion.getIdProducto());
                    }
                    
                }else{
                    //msgError += "<ul>Existe mas de un producto con el mismo código de barras.";
                    String selectOptions = conceptoBO.getConceptosByIdHTMLCombo(idEmpresa, -1, 0, 0, filtroBusqueda);
                    out.print("<!--MULTI-->" + selectOptions);
                    return;
                }
            }else{
                msgError += "<ul>Producto no encontrado, inexistente o deshabilitado."
                        + "<br/>"
                        + "<a target='_blank' href='../catConceptos/catConceptos_express_form.jsp'>Crear Registro en modo Exprés</a>";
            }    
        }else{
            msgError += "<ul>Código de barras vacío. Intente de nuevo.";
        }
        
        if (msgError.equals("")){
            request.getSession().setAttribute("formatoMovimientoSesion", formatoMovimientoSesion);
            recargarListaProductos = true ;
        }
        
    }else if (mode==2){
        // 2 = Reiniciar Lista Productos (borrar todo)
        if (formatoMovimientoSesion == null)
            formatoMovimientoSesion = new FormatoMovimientosSesion();
        
        formatoMovimientoSesion.setListaMovimiento(new ArrayList<MovimientoSesion>());
        request.getSession().setAttribute("formatoMovimientoSesion", formatoMovimientoSesion);
        
        recargarListaProductos = true;
    }else if (mode==3){
        // 3 = Quitar una línea de Producto
        if (formatoMovimientoSesion == null)
            formatoMovimientoSesion = new FormatoMovimientosSesion();
        
        int index_lista_producto=-1;
        try{ index_lista_producto = Integer.parseInt(request.getParameter("index_lista_producto")); }catch(Exception e){}
        if (index_lista_producto<0)
            msgError = "<ul>Producto inválido. Si persiste el problema intente recargar esta página.";
        
        if (msgError.equals("")){
            try{
                formatoMovimientoSesion.getListaMovimiento().remove(index_lista_producto);
            }catch(Exception ex){
                msgError+="El producto especificado no se encontro en el listado en sesion. Intente de nuevo.";
            }
            request.getSession().setAttribute("formatoMovimientoSesion", formatoMovimientoSesion);

            recargarListaProductos=true;
        }
        
    }else if (mode==4){
        //4 = Recargar Lista Productos
        if (formatoMovimientoSesion == null)
            formatoMovimientoSesion = new FormatoMovimientosSesion();
                
        request.getSession().setAttribute("formatoMovimientoSesion", formatoMovimientoSesion);
        recargarListaProductos=true;
    }else if (mode==5){
        //5 = Reiniciar Formato (borrar todo)
        formatoMovimientoSesion = new FormatoMovimientosSesion();
        request.getSession().setAttribute("formatoMovimientoSesion", formatoMovimientoSesion);
    }else if (mode==6) {
        //6 = Mostrar imagen ultimo Producto
        try{ exitoUltimoProducto = Integer.parseInt(request.getParameter("exito_ultimo_producto")); }catch(Exception e){}
        
        mostrarImagenProducto = true;
    }else if (mode==7) {
        //7 = Calcular Totales
        if (formatoMovimientoSesion == null)
            formatoMovimientoSesion = new FormatoMovimientosSesion();
        
        double cantidadProductos = formatoMovimientoSesion.calculaTotalCantidad().doubleValue();
        double totalPrevioVenta = formatoMovimientoSesion.calculaTotalPrecioVenta().doubleValue();
        
        double[] totales = new double[2];
        totales[0] =  cantidadProductos;
        totales[1] =  totalPrevioVenta;
        
        //Usamos JSON para retornar el objeto respuesta
        String jsonOutput = gson.toJson(totales);
        msgExitoExtra = jsonOutput;
    }else if (mode==8) {
        //8 = Aplicar Ajustes
        if (formatoMovimientoSesion == null)
            formatoMovimientoSesion = new FormatoMovimientosSesion();
        
        //Recorremos todo el listado de movimientos, y por cada uno leemos el parametro correspondiente
        int j = 0;
        for (MovimientoSesion movSesion : formatoMovimientoSesion.getListaMovimiento()){
            String parametroCantidad = "cantidad_concepto_" + j;
            
            double nuevaCantidad = movSesion.getCantidad();//asignamos valor anterior por si no se puede recuperar el nuevo asignado
            try{ nuevaCantidad = Double.parseDouble(request.getParameter(parametroCantidad)); }catch(Exception e){}
            
            movSesion.setCantidad(nuevaCantidad);
            movSesion.calculaTotal();//calcula y asigna el total al objeto
            //formatoMovimientoSesion.getListaMovimiento().get(j).setCantidad(nuevaCantidad);
            
            j++;
        }
        
        request.getSession().setAttribute("formatoMovimientoSesion", formatoMovimientoSesion);
        
        msgExitoExtra+="Ajustes aplicados.";
    }else if (mode==9){
        //9 = Iniciar/Re-iniciar Validación
        if (formatoMovimientoSesion == null)
            formatoMovimientoSesion = new FormatoMovimientosSesion();
        
        if (formatoMovimientoSesion.getListaMovimiento().size()>0){
            formatoMovimientoSesion.setModoValidacion(true);
            for (MovimientoSesion movSesion : formatoMovimientoSesion.getListaMovimiento()){
                movSesion.setCantidadValidacion(-1 * movSesion.getCantidad());
            }
            request.getSession().setAttribute("formatoMovimientoSesion", formatoMovimientoSesion);

            msgExitoExtra += "Validación Iniciada.";
        }else{
            formatoMovimientoSesion.setModoValidacion(false);
            request.getSession().setAttribute("formatoMovimientoSesion", formatoMovimientoSesion);
            msgError += "No ha capturado aún ningún Producto, por lo tanto no hay información por Validar.";
        }
    }else if (mode==10 && esValidacion){
        //10 =  Validacion
        if (formatoMovimientoSesion == null)
            formatoMovimientoSesion = new FormatoMovimientosSesion();
            
        //parametros
        int cantidad = 1;
        String codigoBarras = request.getParameter("codigo_barras")!=null?new String(request.getParameter("codigo_barras").getBytes("ISO-8859-1"),"UTF-8"):"";
        
        //Procesamiento
        if (StringManage.getValidString(codigoBarras).length()>0){           
            
            boolean coincidencia = false;
            for (MovimientoSesion movSesion : formatoMovimientoSesion.getListaMovimiento()){
                if (movSesion.getCodigoBarras().equalsIgnoreCase(codigoBarras)){
                    movSesion.setCantidadValidacion(movSesion.getCantidadValidacion() + cantidad);
                    coincidencia = true;
                }
            }
            
            if (!coincidencia)
                msgError += "<ul>Producto no ingresado previamente.";
              
        }else{
            msgError += "<ul>Código de barras vacío. Intente de nuevo.";
        }
        
        request.getSession().setAttribute("formatoMovimientoSesion", formatoMovimientoSesion);
        recargarListaProductos = true ;
        
    }else if (mode==11){
        //11 = Terminar Validación
        if (formatoMovimientoSesion == null)
            formatoMovimientoSesion = new FormatoMovimientosSesion();
        
        formatoMovimientoSesion.setModoValidacion(false);
        request.getSession().setAttribute("formatoMovimientoSesion", formatoMovimientoSesion);
        msgExitoExtra += "Validación Concluida.";
    }else if (mode==12){
        //12 = Buscar Proveedor por Codigo de Barras
        String codigoBarrasProveedor = request.getParameter("codigo_barras_proveedor")!=null?new String(request.getParameter("codigo_barras_proveedor").getBytes("ISO-8859-1"),"UTF-8"):"";
        
        if (StringManage.getValidString(codigoBarrasProveedor).length()>0){
            //Código de barras de proveedor tiene el formato: [IDProveedor]-[IDEmpresa]
            String[] partesCodigoBarras = codigoBarrasProveedor.split("-"); 
            if (partesCodigoBarras.length==2){
                int provIdProveedor = 0;
                int provIdEmpresa = 0;
                try{
                    provIdProveedor = Integer.parseInt(partesCodigoBarras[0]);
                    provIdEmpresa = Integer.parseInt(partesCodigoBarras[1]);
                }catch(Exception ex){
                    msgError += "<ul>Código de Barras de Proveedor formato incorrecto, ingrese un valor válido de sistema Pretoriano.";
                }
                if (provIdProveedor>0 && provIdEmpresa>0){
                    SgfensProveedor proveedorDto = new SGProveedorBO(provIdProveedor, user.getConn()).getSgfensProveedor();
                    
                    if (proveedorDto!=null && (proveedorDto.getIdEmpresa() == provIdEmpresa)){
                        //En caso de que todo corresponda, solo retornamos el ID del Proveedor
                        // para seleccionarlo automaticamente del SELECT en el form
                        msgExitoExtra += "" + proveedorDto.getIdProveedor() ;
                    }else{
                        msgError += "<ul>Proveedor no encontrado en registros de empresa y/o sucursal.";
                    }
                }
            }else{
                msgError += "<ul>Código de Barras para Proveedor con formato incorrecto, ingrese un valor válido.";
            }
        }else
            msgError += "<ul>Código de Barras de Proveedor vacío, ingrese un valor válido.";
        
    }else if (mode==15){
        //15 = Guardar Movimientos
        if (formatoMovimientoSesion == null)
            formatoMovimientoSesion = new FormatoMovimientosSesion();
        
        //Parametros
        String tipoMovimiento ="";   
        int proveedorMovimiento = -1;
        String ordenCompraMovimiento = "";
        String numGiaMovimiento = "";       
        String porQueMovimiento = "";
        int idAlmacen = -1;
        int idAlmacenDestino = -1;
        Date fechaCaducidad = null;
        String numeroLote = "";
        
        tipoMovimiento = request.getParameter("tipoMovimiento")!=null?new String(request.getParameter("tipoMovimiento").getBytes("ISO-8859-1"),"UTF-8"):"";
        ordenCompraMovimiento = request.getParameter("ordenCompraMovimiento")!=null?new String(request.getParameter("ordenCompraMovimiento").getBytes("ISO-8859-1"),"UTF-8"):"";    
        numGiaMovimiento = request.getParameter("numGiaMovimiento")!=null?new String(request.getParameter("numGiaMovimiento").getBytes("ISO-8859-1"),"UTF-8"):"";    
        porQueMovimiento = request.getParameter("porQueMovimiento")!=null?new String(request.getParameter("porQueMovimiento").getBytes("ISO-8859-1"),"UTF-8"):"";    
        numeroLote = request.getParameter("numero_lote")!=null?new String(request.getParameter("numero_lote").getBytes("ISO-8859-1"),"UTF-8"):"";
        try{
            proveedorMovimiento = Integer.parseInt(request.getParameter("proveedorMovimiento"));
        }catch(Exception ex){} 
        try{
            idAlmacen = Integer.parseInt(request.getParameter("idAlmacen"));
        }catch(Exception ex){}  
        try{
            idAlmacenDestino = Integer.parseInt(request.getParameter("idAlmacenDestino"));
        }catch(Exception ex){}
        try {
            fechaCaducidad = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fecha_caducidad"));
        } catch (Exception e) {
        }
        
        //validaciones iniciales
        if (formatoMovimientoSesion.getListaMovimiento().size()<=0)
            msgError += "<ul>No ha agregado ningún producto.";
        if(!gc.isValidString(tipoMovimiento, 1, 30))
            msgError += "<ul>El dato 'Tipo de movimiento' es requerido.";
        if(idAlmacen < 0 )
            msgError += "<ul>El dato 'Almácen' es requerido";
        if(tipoMovimiento.equals("SELECCIONE"))        
            msgError += "<ul>El dato 'Tipo de movimiento' es requerido.";    
        if(tipoMovimiento.equals("TRASPASO")){
            if(idAlmacen > 0  && idAlmacenDestino > 0){
                if(idAlmacenDestino==idAlmacen){
                    msgError += "<ul>El 'Almacén Destino' no puede ser igual al 'Almacén Origen' .";  
                }
            }else if (idAlmacenDestino<=0){
                msgError += "<ul>Para movimiento de TRASPASO el 'Almacén Destino' es requerido .";  
            }
        }
        
        //si no hay errores de validacion basicos
        if (msgError.equals("")){
            
            ExistenciaAlmacenBO existenciaAlmacenBO = new ExistenciaAlmacenBO(user.getConn());
            Almacen almacen = new AlmacenBO(idAlmacen, user.getConn()).getAlmacen();
            Almacen almacenDestino = null;
            if (idAlmacenDestino>0){
                almacenDestino = new AlmacenBO(idAlmacenDestino, user.getConn()).getAlmacen();
            }
                    
            //validacion de existencias en almacen para SALIDA o TRASPASO SALIDA
            if(tipoMovimiento.equals("SALIDA") || tipoMovimiento.equals("TRASPASO")){
                
                for (MovimientoSesion movSesion : formatoMovimientoSesion.getListaMovimiento()){
                    try{
                        ExistenciaAlmacen existenciaAlmacenSalida  = existenciaAlmacenBO.getExistenciaProductoAlmacen(idAlmacen, movSesion.getIdProducto());
                        Concepto conceptoDto = conceptoBO.findConceptobyId(movSesion.getIdProducto());
                        
                        if(existenciaAlmacenSalida==null){ 
                            //no hay relacion de almacen - concepto
                            msgError += "<ul>El almacen '" + almacen.getNombre() + "' no tiene existencia del producto '" + conceptoDto.getNombreDesencriptado() + "'.";
                        }else if (existenciaAlmacenSalida.getExistencia() < movSesion.getCantidad()){
                            //no hay existencia suficiente en almacen para salida
                            msgError += "<ul>El almacen '" + almacen.getNombre() + "' no tiene existencia suficiente del producto '" + conceptoDto.getNombreDesencriptado() + "'. "
                                    + " Existencia en almacen: " + existenciaAlmacenSalida.getExistencia();
                        }

                    }catch(Exception e){                    
                        msgError += "<!--ERROR-->No se pudo consultar un registro. Informe del error al administrador del sistema: " + GenericMethods.exceptionStackTraceToString(e);
                        e.printStackTrace();
                    }
                }
                
            }
            
            //procedemos a guardar si no hubo error
            if (msgError.equals("")){
            
                MovimientoDaoImpl movimientosDao = new MovimientoDaoImpl(user.getConn());
                ExistenciaAlmacenDaoImpl existenciaAlmacenDao = new ExistenciaAlmacenDaoImpl(user.getConn());
                Date fechaRegistroMovimientos = new Date();
                
                for (MovimientoSesion movSesion : formatoMovimientoSesion.getListaMovimiento()){
                    Concepto conceptoDto = conceptoBO.findConceptobyId(movSesion.getIdProducto());
                    
                    //primero aplicamos salidas
                    if (tipoMovimiento.equals("SALIDA") || tipoMovimiento.equals("TRASPASO")){
                        try{
                            ExistenciaAlmacen existenciaAlmacenSalida  = existenciaAlmacenBO.getExistenciaProductoAlmacen(idAlmacen, movSesion.getIdProducto());
                            
                            BigDecimal numArticulosDisponibles = (new BigDecimal(existenciaAlmacenSalida!=null?existenciaAlmacenSalida.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal contaMovimiento = (new BigDecimal(movSesion.getCantidad())).setScale(2, RoundingMode.HALF_UP);
                            //restamos
                            BigDecimal nuevoStockBigD = numArticulosDisponibles.subtract(contaMovimiento);
                            double nuevoStock = nuevoStockBigD.doubleValue();
                            
                            existenciaAlmacenSalida.setExistencia(nuevoStock);
                            
                            Movimiento movimientoSalidaDto = new Movimiento();
                            movimientoSalidaDto.setIdEmpresa(idEmpresa);
                            movimientoSalidaDto.setTipoMovimiento(tipoMovimiento);
                            movimientoSalidaDto.setNombreProducto(conceptoDto.getNombreDesencriptado());
                            movimientoSalidaDto.setContabilidad(movSesion.getCantidad());
                            movimientoSalidaDto.setIdProveedor(proveedorMovimiento);
                            movimientoSalidaDto.setOrdenCompra(ordenCompraMovimiento);
                            movimientoSalidaDto.setNumeroGuia(numGiaMovimiento);                             
                            movimientoSalidaDto.setFechaRegistro(fechaRegistroMovimientos);
                            movimientoSalidaDto.setIdConcepto(conceptoDto.getIdConcepto());
                            //movimientoSalidaDto.setContabilidadPeso(0);
                            movimientoSalidaDto.setNumeroLote(numeroLote);
                            movimientoSalidaDto.setFechaCaducidad(fechaCaducidad);
                            movimientoSalidaDto.setIdAlmacen(idAlmacen);
                            
                            if (tipoMovimiento.equals("TRASPASO")){
                                movimientoSalidaDto.setConceptoMovimiento("(SALIDA) " + porQueMovimiento);
                                movimientoSalidaDto.setIdAlmacenDestino(idAlmacenDestino);
                            }else{               
                                movimientoSalidaDto.setConceptoMovimiento(porQueMovimiento);
                            }
                            
                            //solo hacemos update, por que debido a validacion previa, la relacion almacen-concepto SI debe existir
                            //existenciaAlmacenDao.update(existenciaAlmacenSalida.createPk(), existenciaAlmacenSalida);
                            existenciaAlmacenBO.updateBD(existenciaAlmacenSalida);
                            //registramos movimiento
                            movimientosDao.insert(movimientoSalidaDto);
                        }catch(Exception ex){
                            msgError += "Error inesperado al dar salida de producto con codigo barras '"+movSesion.getCodigoBarras()+"': " + GenericMethods.exceptionStackTraceToString(ex);
                            ex.printStackTrace();
                        }
                    }
                    
                    //después las entradas
                    if (tipoMovimiento.equals("ENTRADA") || tipoMovimiento.equals("TRASPASO")){
                        //Si es ENTRADA el almacen a ingresar es "idAlmacen", si es TRASPASO-entrada las entradas serian a "idAlmacenDestino"
                        int idAlmacenEntrada = tipoMovimiento.equals("ENTRADA")? idAlmacen : idAlmacenDestino;
                        
                        try{
                            ExistenciaAlmacen existenciaAlmacenEntrada  = existenciaAlmacenBO.getExistenciaProductoAlmacen(idAlmacenEntrada, movSesion.getIdProducto());
                            
                            boolean nuevaExistenciaAlmacen = false;
                            if(existenciaAlmacenEntrada==null){
                                existenciaAlmacenEntrada = new ExistenciaAlmacen();
                                existenciaAlmacenEntrada.setIdAlmacen(idAlmacenEntrada);
                                existenciaAlmacenEntrada.setIdConcepto(conceptoDto.getIdConcepto());
                                existenciaAlmacenEntrada.setExistencia(0);
                                existenciaAlmacenEntrada.setEstatus(1);
                                nuevaExistenciaAlmacen = true;
                            }
                            
                            BigDecimal numArticulosDisponibles = (new BigDecimal(existenciaAlmacenEntrada.getExistencia())).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal contaMovimiento = (new BigDecimal(movSesion.getCantidad())).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal nuevoStockBigD = numArticulosDisponibles.add(contaMovimiento);
                            double nuevoStock = nuevoStockBigD.doubleValue();
                            
                            existenciaAlmacenEntrada.setExistencia(nuevoStock);
                            
                            Movimiento movimientoEntradaDto = new Movimiento();
                            movimientoEntradaDto.setIdEmpresa(idEmpresa);
                            movimientoEntradaDto.setTipoMovimiento(tipoMovimiento);
                            movimientoEntradaDto.setNombreProducto(conceptoDto.getNombreDesencriptado());
                            movimientoEntradaDto.setContabilidad(movSesion.getCantidad());
                            movimientoEntradaDto.setIdProveedor(proveedorMovimiento);
                            movimientoEntradaDto.setOrdenCompra(ordenCompraMovimiento);
                            movimientoEntradaDto.setNumeroGuia(numGiaMovimiento);                                            
                            movimientoEntradaDto.setConceptoMovimiento(porQueMovimiento);                
                            movimientoEntradaDto.setFechaRegistro(fechaRegistroMovimientos);
                            movimientoEntradaDto.setIdConcepto(conceptoDto.getIdConcepto());
                            //movimientoEntradaDto.setContabilidadPeso(0);
                            movimientoEntradaDto.setNumeroLote(numeroLote);
                            movimientoEntradaDto.setFechaCaducidad(fechaCaducidad);
                            movimientoEntradaDto.setIdAlmacen(idAlmacen); 
                            if (tipoMovimiento.equals("TRASPASO")){
                                movimientoEntradaDto.setConceptoMovimiento("(ENTRADA) " + porQueMovimiento);
                                movimientoEntradaDto.setIdAlmacenDestino(idAlmacenDestino);
                            }else{               
                                movimientoEntradaDto.setConceptoMovimiento(porQueMovimiento);
                            }
                            
                            if (nuevaExistenciaAlmacen){
                                //nueva relacion almacen-concepto
                                existenciaAlmacenDao.insert(existenciaAlmacenEntrada);
                            }else{
                                //ya existia relacion, solo actualizamos existencia
                                existenciaAlmacenDao.update(existenciaAlmacenEntrada.createPk(), existenciaAlmacenEntrada);
                            }
                            
                            //registramos movimiento
                            movimientosDao.insert(movimientoEntradaDto);
                        }catch(Exception ex){
                            msgError += "Error inesperado al registrar entrada de producto con codigo barras '"+movSesion.getCodigoBarras()+"': " + GenericMethods.exceptionStackTraceToString(ex);
                            ex.printStackTrace();
                        }
                    }
                    
                }
                
                //Si no hubo ningun error, anexamos mensaje de Éxito
                if (msgError.equals(""))
                    msgExitoExtra += "<ul>Movimientos almacenados satisfactoriamente."
                                    + "<ul>Existencias en almacenes actualizadas éxitosamente.";
                
            }
            
        }
        
    }
    
    if (msgError.equals("") && mode!=0){
        out.print("<!--EXITO-->" + msgExitoExtra);
    }
%>
<%
    if (recargarListaProductos && msgError.equals("")){
%>
        <table class="data" width="100%" cellpadding="0" cellspacing="0" style="vertical-align: top;">
            <thead>
                <tr>
                    <th>Cantidad</th>
                    <th>Artículo</th>
                    <th>Descripción</th>
                    <th>Precio Venta</th>
                    <th>Total</th>
                    <th>Código</th>
                    <th>Validación</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
<%
        int j=0;
        for (MovimientoSesion movSesion : formatoMovimientoSesion.getListaMovimiento() ){
            Concepto conceptoDto = conceptoBO.findConceptobyId(movSesion.getIdProducto());
            int id =j;
%>
                <tr>
                    <td>
                        <input type="text" id="cantidad_concepto_<%= id %>" name="cantidad_concepto_<%= id %>"
                               style="width: 50px" 
                               ajuste=""
                               onkeypress="return validateNumber(event);"
                               value="<%= movSesion.getCantidad() %>" readonly />
                    </td>
                    <td><%= conceptoDto.getNombreDesencriptado() %></td>
                    <td><%= conceptoDto.getDescripcion()%></td>
                    <td style="text-align: right;"><%= Converter.doubleToStringFormatMexico(movSesion.getPrecioVenta()) %></td>
                    <td style="text-align: right;"><%= Converter.doubleToStringFormatMexico(movSesion.getTotal()) %></td>
                    <td><%= movSesion.getCodigoBarras() %></td>
                    <td style="text-align: center; color: black; text-shadow: none; background-color:<%= esValidacion?( movSesion.getCantidadValidacion()<0?"red":(movSesion.getCantidadValidacion()==0?"inherit":"gold")):"#6f7072" %>" >
                        <%= esValidacion? ""+(movSesion.getCantidadValidacion()>0?"&plus; "+movSesion.getCantidadValidacion():movSesion.getCantidadValidacion()) : "" %>
                    </td>
                    <td>
                        <a href="javascript:void(0);" onclick="subProducto(<%=id%>);" id="sub_producto_action_<%=id%>" title="Quitar Producto">
                            <img src="../../images/minus.png" alt="Quitar Producto" height="20" width="20" title="Quitar Producto" class="help"/>
                        </a>
                    </td>
                </tr>
<%
            j++;
        }
        if(j<=0){
%>
                <tr>
                    <td colspan="8"><h4>Agregue un producto para comenzar</h4></td>
                </tr>
        <%}else{%>
                <tr>
                    <td colspan="8">
                        <a href="javascript:void(0);" onclick="reiniciarListaProducto();" id="reiniciar_productos_action" style="text-align: right; float: right;">
                            <img src="../../images/icon_cancel.png" alt="Quitar Todos" title="Quitar Todos los Producto" class="help" height="20" width="20"/> Quitar Todos 
                        </a>
                    </td>
                </tr>
        <%}%>
            </tbody>
        </table>
<%
    }
%>
<% if (mostrarImagenProducto && msgError.equals("")) { 
        if (exitoUltimoProducto==1){
            //buscamos nombre de imagen, del producto del ultimo movimiento
            Concepto conceptoDto = conceptoBO.findConceptobyId(formatoMovimientoSesion.getIdConceptoUltimoAgregado());
            if (StringManage.getValidString(conceptoDto.getImagenNombreArchivo()).length()>0
                    || StringManage.getValidString(conceptoDto.getRutaImagen()).length()>0){
%>
                <img src="../catConceptos/getImageConcepto.jsp?idConcepto=<%= conceptoDto.getIdConcepto() %>" width="200" height="200" 
                 id="img_producto" name="img_producto" />
<%  
            }else{
%>
                <img src="../../images/icon_producto_no_encontrado.png" width="200" height="200" 
                 id="img_producto" name="img_producto" />
<%
            }    
        }else{

%>
            <img src="../../images/icon_producto_no_encontrado.png" width="200" height="200" 
                 id="img_producto" name="img_producto" />
<%      }
    } 
%>
<%
    if (!msgError.equals("")){
        out.print("<!--ERROR-->"+msgError);
    }
%>