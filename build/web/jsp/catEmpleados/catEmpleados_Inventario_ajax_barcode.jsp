<%-- 
    Document   : catEmpleados_Inventario_ajax_barcode
    Created on : 15/02/2016, 01:10:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.bo.EmpleadoInventarioRepartidorBO"%>
<%@page import="com.tsp.sct.dao.dto.EmpAsignacionInventarioDetalle"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpAsignacionInventarioDetalleDaoImpl"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpAsignacionInventarioDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpAsignacionInventario"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoInventarioRepartidor"%>
<%@page import="com.tsp.sct.dao.dto.Movimiento"%>
<%@page import="com.tsp.sct.dao.jdbc.MovimientoDaoImpl"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoInventarioRepartidorDaoImpl"%>
<%@page import="com.tsp.sct.util.Converter"%>
<%@page import="com.tsp.sct.dao.jdbc.ExistenciaAlmacenDaoImpl"%>
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
    *       ...
    *       15 = Guardar Asignaciones
    *       16 = Guardar Sesion de Asignación para automatización
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
                                  + " AND (ID_CONCEPTO_PADRE IS NULL OR ID_CONCEPTO_PADRE<=0)"
                                  + " AND MATERIA_PRIMA=0 ";
            
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
                msgError += "<ul>Producto no encontrado, inexistente, deshabilitado o Materia Prima."
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
    }else if (mode==15){
        //15 = Guardar Asignaciones
        if (formatoMovimientoSesion == null)
            formatoMovimientoSesion = new FormatoMovimientosSesion();
        
        //Parametros
        int idAlmacen = -1;
        int idEmpleado = -1;
        
        try{
            idAlmacen = Integer.parseInt(request.getParameter("idAlmacen"));
        }catch(Exception ex){}  
        try{
            idEmpleado = Integer.parseInt(request.getParameter("id_empleado"));
        }catch(Exception ex){}  
        
        //validaciones iniciales
        if (idEmpleado<=0)
            msgError += "<ul>No se específico a que empleado se asignara el producto.";
        if (formatoMovimientoSesion.getListaMovimiento().size()<=0)
            msgError += "<ul>No ha agregado ningún producto.";
        if(idAlmacen < 0 )
            msgError += "<ul>El dato 'Almácen' es requerido"; 
        
        //si no hay errores de validacion basicos
        if (msgError.equals("")){
            
            ExistenciaAlmacenBO existenciaAlmacenBO = new ExistenciaAlmacenBO(user.getConn());
            Almacen almacen = new AlmacenBO(idAlmacen, user.getConn()).getAlmacen();
            
            formatoMovimientoSesion.setAlmacen(almacen);
            formatoMovimientoSesion.setTipoMovimiento(FormatoMovimientosSesion.TIPO_MOV_EMP_CARGA);
            
            EmpleadoInventarioRepartidorBO empleadoInventarioRepartidorBO = new EmpleadoInventarioRepartidorBO(user.getConn());
            try{
                empleadoInventarioRepartidorBO.asignarDevolverInventarioRepartidor(idEmpresa, idEmpleado, formatoMovimientoSesion, false);
            }catch(Exception ex){
                msgError += ex.toString();
            }
            
            /*
            //validacion de existencias en almacen para SALIDA
            for (MovimientoSesion movSesion : formatoMovimientoSesion.getListaMovimiento()){
                try{
                    ExistenciaAlmacen existenciaAlmacen  = existenciaAlmacenBO.getExistenciaProductoAlmacen(idAlmacen, movSesion.getIdProducto());
                    Concepto conceptoDto = conceptoBO.findConceptobyId(movSesion.getIdProducto());

                    if(existenciaAlmacen==null){ 
                        //no hay relacion de almacen - concepto
                        msgError += "<ul>El almacen '" + almacen.getNombre() + "' no tiene existencia del producto '" + conceptoDto.getNombreDesencriptado() + "'.";
                    }else if (existenciaAlmacen.getExistencia() < movSesion.getCantidad()){
                        //no hay existencia suficiente en almacen para salida
                        msgError += "<ul>El almacen '" + almacen.getNombre() + "' no tiene existencia suficiente del producto '" + conceptoDto.getNombreDesencriptado() + "'. "
                                + " Existencia en almacen: " + existenciaAlmacen.getExistencia();
                    }

                }catch(Exception e){                    
                    msgError += "No se pudo consultar un registro. Informe del error al administrador del sistema: " + GenericMethods.exceptionStackTraceToString(e);
                    e.printStackTrace();
                }
            }
            
            //procedemos a guardar si no hubo error
            if (msgError.equals("")){
            
                EmpleadoInventarioRepartidorDaoImpl empleadoInventarioRepartidorDao = new EmpleadoInventarioRepartidorDaoImpl(user.getConn());
                MovimientoDaoImpl movimientosDao = new MovimientoDaoImpl(user.getConn());
                
                Date fechaRegistroMovimientos = new Date();
                
                for (MovimientoSesion movSesion : formatoMovimientoSesion.getListaMovimiento()){
                    try{
                        Concepto conceptoDto = conceptoBO.findConceptobyId(movSesion.getIdProducto());

                        // 1 - Creamos o Actualizamos el registro de relacion Empleado-Concepto (inventario empleado)
                        EmpleadoInventarioRepartidor[] empleadoInventarios = empleadoInventarioRepartidorDao.findByDynamicWhere("ID_EMPLEADO = "+idEmpleado+" AND ID_CONCEPTO = "+conceptoDto.getIdConcepto(), null);
                        EmpleadoInventarioRepartidor empleadoInventario = null;
                        boolean edicion = false;
                        if (empleadoInventarios.length>0){
                            //Ya existe relacion
                            empleadoInventario = empleadoInventarios[0];
                            edicion = true;
                        }else{
                            //No existe relacion empleado-concepto, creamos nueva
                            empleadoInventario = new EmpleadoInventarioRepartidor();
                            empleadoInventario.setIdEmpleado(idEmpleado);
                            empleadoInventario.setIdConcepto(conceptoDto.getIdConcepto());
                            empleadoInventario.setTipoProductoServicio(1);//0 es servicio, 1 concepto.
                        }
                        empleadoInventario.setCantidad(empleadoInventario.getCantidad() + movSesion.getCantidad());
                        empleadoInventario.setIdEstatus(1);
                        //empleadoInventario.setPeso(movSesion.getPeso());
                        empleadoInventario.setExistenciaGranel(empleadoInventario.getCantidad() * empleadoInventario.getPeso());
                        
                        if (!edicion){
                            empleadoInventarioRepartidorDao.insert(empleadoInventario);
                        }else{
                            empleadoInventarioRepartidorDao.update(empleadoInventario.createPk(), empleadoInventario);
                        }

                        // 2 - Creamos movimiento de almacen
                        Movimiento movimientoDto = new Movimiento(); 
                        movimientoDto.setIdEmpresa(idEmpresa);
                        movimientoDto.setTipoMovimiento("SALIDA");
                        movimientoDto.setNombreProducto(conceptoDto.getNombreDesencriptado());
                        movimientoDto.setContabilidad(movSesion.getCantidad());
                        movimientoDto.setIdProveedor(-1);
                        movimientoDto.setOrdenCompra("");
                        movimientoDto.setNumeroGuia("");                             
                        movimientoDto.setIdAlmacen(idAlmacen);                
                        movimientoDto.setConceptoMovimiento("Asignación de Producto a Vendedor");                
                        movimientoDto.setFechaRegistro(fechaRegistroMovimientos);
                        movimientoDto.setIdConcepto(conceptoDto.getIdConcepto());
                        movimientoDto.setIdEmpleado(idEmpleado);                                    
                            //Insertamos registro
                        movimientosDao.insert(movimientoDto);

                        // 3 - Actualizamos existencia en Almacen
                        ExistenciaAlmacen existenciaAlmacen  = existenciaAlmacenBO.getExistenciaProductoAlmacen(idAlmacen, movSesion.getIdProducto()); 
                        BigDecimal numArticulosDisponibles = (new BigDecimal(existenciaAlmacen!=null?existenciaAlmacen.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal contaMovimiento = (new BigDecimal(movSesion.getCantidad())).setScale(2, RoundingMode.HALF_UP);
                            //restamos
                        BigDecimal nuevoStockBigD = numArticulosDisponibles.subtract(contaMovimiento);
                        double nuevoStock = nuevoStockBigD.doubleValue();
                        existenciaAlmacen.setExistencia(nuevoStock);
                        //solo hacemos update, por que debido a validacion previa, la relacion almacen-concepto SI debe existir
                        existenciaAlmacenBO.updateBD(existenciaAlmacen);
                        
                    }catch(Exception ex){
                        msgError += "Error inesperado al asignar Producto con codigo barras '"+movSesion.getCodigoBarras()+"': " + GenericMethods.exceptionStackTraceToString(ex);
                        ex.printStackTrace();
                    }
                }
                        
                //Si no hubo ningun error, anexamos mensaje de Éxito
                if (msgError.equals(""))
                    msgExitoExtra += "<ul>Asignaciones almacenadas satisfactoriamente."
                                    + "<ul>Existencias en almacenes actualizadas éxitosamente.";
                
            }
            */
            
            //Si no hubo ningun error, anexamos mensaje de Éxito
            if (msgError.equals(""))
                msgExitoExtra += "<ul>Asignaciones almacenadas satisfactoriamente."
                                + "<ul>Existencias en almacenes actualizadas éxitosamente.";
            
        }
        
    }else if(mode==16){
        //16 = Guardar Sesion de Asignación para automatización
        int numDias = 0;
        int idAlmacen = -1;
        int idEmpleado = -1;
        
        try{
            idAlmacen = Integer.parseInt(request.getParameter("idAlmacen"));
        }catch(Exception ex){}  
        try{
            idEmpleado = Integer.parseInt(request.getParameter("id_empleado"));
        }catch(Exception ex){}
        try{
            numDias = Integer.parseInt(request.getParameter("num_dias"));
        }catch(Exception ex){}
        
        //validaciones iniciales
        if (idEmpleado<=0)
            msgError += "<ul>No se específico a que empleado se asignara el producto.";
        if (formatoMovimientoSesion.getListaMovimiento().size()<=0)
            msgError += "<ul>No ha agregado ningún producto.";
        if(idAlmacen < 0 )
            msgError += "<ul>El dato 'Almácen' es requerido"; 
        if (numDias<=0)
            msgError += "<ul>El numéro de días específicado no es un valor Entero positivo, no puede ser menor o igual a 0.";
        
        if (msgError.equals("")){
            EmpAsignacionInventario empAsignacionInventarioDto = null;
            try{
                EmpAsignacionInventarioDaoImpl empAsignacionInventarioDao = new EmpAsignacionInventarioDaoImpl(user.getConn());
                
                empAsignacionInventarioDto = new EmpAsignacionInventario();
                empAsignacionInventarioDto.setIdEmpresa(idEmpresa);
                empAsignacionInventarioDto.setIdEmpleado(idEmpleado);
                empAsignacionInventarioDto.setIdEstatus(1);
                empAsignacionInventarioDto.setNumDiasRepeticion(numDias);
                empAsignacionInventarioDto.setUltimaRepFechaHr(new Date());
                empAsignacionInventarioDto.setUltimaRepExito(1);
                empAsignacionInventarioDto.setUltimaRepObservacion("Creación de Asignación Automatizada");
                
                empAsignacionInventarioDao.insert(empAsignacionInventarioDto);
            }catch(Exception ex){
                msgError += "<ul>Error inesperado al almacenar registro de Asignación Inventario: " + GenericMethods.exceptionStackTraceToString(ex);
                ex.printStackTrace();
            }
            
            if (msgError.equals("") && empAsignacionInventarioDto!=null){
                EmpAsignacionInventarioDetalleDaoImpl empAsignacionInventarioDetalleDao = new EmpAsignacionInventarioDetalleDaoImpl(user.getConn());
                
                for (MovimientoSesion movSesion : formatoMovimientoSesion.getListaMovimiento()){
                    try{
                        EmpAsignacionInventarioDetalle empAsignacionInventarioDetalleDto = new EmpAsignacionInventarioDetalle();

                        empAsignacionInventarioDetalleDto.setIdAsignacionInventario(empAsignacionInventarioDto.getIdAsignacionInventario());
                        empAsignacionInventarioDetalleDto.setIdConcepto(movSesion.getIdProducto());
                        empAsignacionInventarioDetalleDto.setCantidad(movSesion.getCantidad());
                        empAsignacionInventarioDetalleDto.setIdEstatus(1);
                        empAsignacionInventarioDetalleDto.setPeso(0);//movSesion.getPeso
                        empAsignacionInventarioDetalleDto.setExistenciaGranel(empAsignacionInventarioDetalleDto.getCantidad() * empAsignacionInventarioDetalleDto.getPeso());
                        empAsignacionInventarioDetalleDto.setIdAlmacen(idAlmacen);

                        empAsignacionInventarioDetalleDao.insert(empAsignacionInventarioDetalleDto);
                    }catch(Exception ex){
                        msgError += "<ul>Error inesperado al almacenar registro de Detalle de Asignación Inventario: " + GenericMethods.exceptionStackTraceToString(ex);
                        ex.printStackTrace();
                    }
                }
                
            }
        }
        
        //Si no hubo ningun error, anexamos mensaje de Éxito
        if (msgError.equals(""))
            msgExitoExtra += "<ul>Asignación de Inventario a Empleado Automatizada cada " + numDias + " días."
                    + "<br/><b>NOTA: Debe asegurarse de que el/los almacenes tengan existencia suficiente para cumplir con las asignaciones automáticas.</b>";
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