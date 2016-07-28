<%-- 
    Document   : catMovimientos_ajax
    Created on : 23/11/2012, 06:06:14 PM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.bo.ZonaHorariaBO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.dao.jdbc.ExistenciaAlmacenDaoImpl"%>
<%@page import="com.tsp.sct.bo.ExistenciaAlmacenBO"%>
<%@page import="com.tsp.sct.dao.dto.ExistenciaAlmacen"%>
<%@page import="java.math.RoundingMode"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.ConceptoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.MovimientoBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.MovimientoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Movimiento"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
try{    
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idMovimiento = -1;
    String tipoMovimiento ="";
    int productoMovimiento = -1;
    double contabilidadMovimiento =-1;    
    int estatus = 2;//deshabilitado
    double contabilidadPeso = 0;
    
    int proveedorMovimiento = -1;
    String ordenCompraMovimiento = "";
    String numGiaMovimiento = "";       
    String porQueMovimiento = "";
    int idAlmacen = -1;
    int idAlmacenDestino = -1;
    
    boolean productoAGranel = false;
    Date fechaCaducidad = null;
    String numeroLote = "";
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idMovimiento = Integer.parseInt(request.getParameter("idMovimiento"));
    }catch(NumberFormatException ex){}
    tipoMovimiento = request.getParameter("tipoMovimiento")!=null?new String(request.getParameter("tipoMovimiento").getBytes("ISO-8859-1"),"UTF-8"):"";
    //productoMovimiento = request.getParameter("productoMovimiento")!=null?new String(request.getParameter("productoMovimiento").getBytes("ISO-8859-1"),"UTF-8"):"";    
    productoMovimiento = Integer.parseInt(request.getParameter("productoMovimiento"));    
    ordenCompraMovimiento = request.getParameter("ordenCompraMovimiento")!=null?new String(request.getParameter("ordenCompraMovimiento").getBytes("ISO-8859-1"),"UTF-8"):"";    
    numGiaMovimiento = request.getParameter("numGiaMovimiento")!=null?new String(request.getParameter("numGiaMovimiento").getBytes("ISO-8859-1"),"UTF-8"):"";    
    porQueMovimiento = request.getParameter("porQueMovimiento")!=null?new String(request.getParameter("porQueMovimiento").getBytes("ISO-8859-1"),"UTF-8"):"";    
    numeroLote = request.getParameter("numero_lote")!=null?new String(request.getParameter("numero_lote").getBytes("ISO-8859-1"),"UTF-8"):"";
    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(Exception ex){}      
     try{
        contabilidadMovimiento = Double.parseDouble(request.getParameter("contabilidadMovimiento"));
    }catch(Exception ex){}
    try{
        proveedorMovimiento = Integer.parseInt(request.getParameter("proveedorMovimiento"));
    }catch(Exception ex){} 
    try{
        idAlmacen = Integer.parseInt(request.getParameter("idAlmacen"));
    }catch(Exception ex){}  
    try{
        idAlmacenDestino = Integer.parseInt(request.getParameter("idAlmacenDestino"));
    }catch(Exception ex){}
    try{
        contabilidadPeso = Double.parseDouble(request.getParameter("contabilidadPeso"));
    }catch(Exception ex){}
    try {
        fechaCaducidad = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fecha_caducidad"));
    } catch (Exception e) {
    }
    
    System.out.println("TIPO DE MOVIMIENTO: "+tipoMovimiento);
    System.out.println("PRODUCTO PARA MOVIMIENTO: "+productoMovimiento);
    
    ////HACEMOS LA CONSULTA DEL PRODUCTO QUE VAMOS A MODIFICAR, PARA PODER REALIZAR LA OPERACIO Y SABES Q NOMRE DEL PRODUCTO ES
     ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
     Concepto conceptosDto = new Concepto();
     Encrypter encryDesen = new Encrypter();
     try{
         conceptosDto = conceptoBO.findConceptobyId(productoMovimiento);
         conceptosDto.setNombre(encryDesen.decodeString(conceptosDto.getNombre()));         
     }catch(Exception ex){
         ex.printStackTrace();
     }
         
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();
    if (conceptosDto==null)
        msgError += "<ul>El dato 'Producto' es requerido.";
    if(!gc.isValidString(tipoMovimiento, 1, 30))
        msgError += "<ul>El dato 'tipo de movimiento' es requerido.";
    if(idAlmacen < 0 )
        msgError += "<ul>El dato 'Alamcén' es requerido";
    if(tipoMovimiento.equals("SELECCIONE"))        
        msgError += "<ul>El dato 'tipo de movimiento' es requerido.";
    if(conceptosDto!=null && !gc.isValidString(conceptosDto.getNombre(), 1, 350))
        msgError += "<ul>El dato 'Producto' es requerido";    
    //if(contabilidadMovimiento <= 0 && (!mode.equals("")))
    if(contabilidadMovimiento <= 0 )
        msgError += "<ul>El dato 'cantidad de movimiento' es requerido.";
    if (conceptosDto!=null && (conceptosDto.getPrecioUnitarioGranel()>0 || conceptosDto.getPrecioMedioGranel() > 0 || conceptosDto.getPrecioMayoreoGranel() > 0 || conceptosDto.getPrecioEspecialGranel() > 0)) {
        //producto a Granel
        productoAGranel = true;
        if (contabilidadPeso <= 0)
            msgError += "<ul>El dato 'Total en Peso del movimiento' es requerido.";
    }
    if(idMovimiento <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'Movimiento' es requerido.";    
    if(tipoMovimiento.equals("TRASPASO")){
        if(idAlmacen > 0  && idAlmacenDestino > 0){
            if(idAlmacenDestino==idAlmacen){
                msgError += "<ul>El 'Almacén Destino' no puede ser igual al 'Almacén Origen' .";  
            }
        }
    }
    
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        
        if(idMovimiento>0){
            
            
            
            if (mode.equals("1")){
            /*
            * Editar
            */
                MovimientoBO movimientoBO = new MovimientoBO(idMovimiento,user.getConn());
                Movimiento movimientoDto = movimientoBO.getMovimiento();
                               
                //movimientoDto.setIdEstatus(estatus);
                movimientoDto.setTipoMovimiento(tipoMovimiento);
                movimientoDto.setNombreProducto(conceptosDto.getNombre());
                movimientoDto.setContabilidad(contabilidadMovimiento);
                movimientoDto.setIdProveedor(proveedorMovimiento);
                movimientoDto.setOrdenCompra(ordenCompraMovimiento);
                movimientoDto.setNumeroGuia(numGiaMovimiento);                
                movimientoDto.setIdAlmacen(idAlmacen);                    
                movimientoDto.setConceptoMovimiento(porQueMovimiento);
                movimientoDto.setIdConcepto(conceptosDto.getIdConcepto());
                movimientoDto.setNumeroLote(numeroLote);
                movimientoDto.setFechaCaducidad(fechaCaducidad);
                
                try{
                    new MovimientoDaoImpl(user.getConn()).update(movimientoDto.createPk(), movimientoDto);

                    out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                    ex.printStackTrace();
                }
                
                
            }else{
                out.print("<!--ERROR-->Acción no válida.");
            }
            
            
        }else{
            /*
            *  Nuevo
            */
            
            try {                
                
                
                /**
                 * Creamos el registro de Movimiento
                 */
                Movimiento movimientoDto = new Movimiento();
                Movimiento movimientoDto2 = new Movimiento();
                 
                MovimientoDaoImpl movimientosDaoImpl = new MovimientoDaoImpl(user.getConn());
                
                movimientoDto.setIdEmpresa(idEmpresa);
                movimientoDto.setTipoMovimiento(tipoMovimiento);
                movimientoDto.setNombreProducto(conceptosDto.getNombre());
                movimientoDto.setContabilidad(contabilidadMovimiento);
                movimientoDto.setIdProveedor(proveedorMovimiento);
                movimientoDto.setOrdenCompra(ordenCompraMovimiento);
                movimientoDto.setNumeroGuia(numGiaMovimiento);                             
                movimientoDto.setIdAlmacen(idAlmacen);                
                movimientoDto.setConceptoMovimiento(porQueMovimiento);                
                
                try{
                    movimientoDto.setFechaRegistro(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                }catch(Exception e){
                    movimientoDto.setFechaRegistro(new Date());
                }
                movimientoDto.setIdConcepto(conceptosDto.getIdConcepto());
                movimientoDto.setContabilidadPeso(contabilidadPeso);
                movimientoDto.setNumeroLote(numeroLote);
                movimientoDto.setFechaCaducidad(fechaCaducidad);
               
                
                /////////
                /**
                * REALIZAMOS LAS OPERACIONES DE SUMA O RESTA, SEGUN CORRESPONDA AL TIPO DE MOVIMIENTO, PARA ACTUALIZAR LA CANTIDAD DE STOCK QUE EXISTE
                **/               
                boolean nuevo = false;
                boolean nuevo2 = false;
                
                ExistenciaAlmacen almacenExists = null;
                ExistenciaAlmacen almacenExists2 = null;
                try{
                    almacenExists  = new ExistenciaAlmacenBO(user.getConn()).getExistenciaProductoAlmacen(idAlmacen, conceptosDto.getIdConcepto());
                    
                    if(almacenExists==null){
                        nuevo = true;
                        almacenExists = new ExistenciaAlmacen();
                        almacenExists.setIdAlmacen(idAlmacen);
                        almacenExists.setIdConcepto(conceptosDto.getIdConcepto());
                        almacenExists.setExistencia(0);
                        almacenExists.setEstatus(1);
                    }
                                        
                }catch(Exception e){                    
                    out.print("<!--ERROR-->No se pudo consultar el registro. Informe del error al administrador del sistema: " + e.toString());
                    System.out.println("Almacen o concepto no encontrado");
                    e.printStackTrace();
                }
                                
                if(tipoMovimiento.equals("SALIDA")){
                    
                    BigDecimal numArticulosDisponibles = (new BigDecimal(almacenExists!=null?almacenExists.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal contaMovimiento = (new BigDecimal(contabilidadMovimiento)).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal stockTotal = numArticulosDisponibles.subtract(contaMovimiento);
                    double nuevoStock = stockTotal.doubleValue();
                    
                    double nuevoStockPeso = 0;
                    if (productoAGranel){
                        BigDecimal pesoDisponibles = (new BigDecimal(almacenExists!=null?almacenExists.getExistenciaPeso():0)).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal contaPeso = (new BigDecimal(contabilidadPeso)).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal stockPesoTotal = pesoDisponibles.subtract(contaPeso);
                        nuevoStockPeso = stockPesoTotal.doubleValue();
                    }
                    
                    //if (nuevoStock<0){
                    if (nuevoStock<0 || nuevoStockPeso<0){
                        msgError+="<ul>El numero de articulos (o peso) disponibles no son suficientes para efectuar la operación.<br/>No. de Articulos disponibles: " + (almacenExists!=null?almacenExists.getExistencia():0);
                    }else{
                        almacenExists.setExistencia(nuevoStock);
                        almacenExists.setExistenciaPeso(nuevoStockPeso);
                    }
                    
                }
                if(tipoMovimiento.equals("ENTRADA")){
                    
                    BigDecimal numArticulosDisponibles = (new BigDecimal(almacenExists!=null?almacenExists.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal contaMovimiento = (new BigDecimal(contabilidadMovimiento)).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal stockTotal = numArticulosDisponibles.add(contaMovimiento);
                    
                    almacenExists.setExistencia(stockTotal.doubleValue());
                    
                    if (productoAGranel){
                        BigDecimal pesoDisponibles = (new BigDecimal(almacenExists!=null?almacenExists.getExistenciaPeso():0)).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal contaPeso = (new BigDecimal(contabilidadPeso)).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal stockPesoTotal = pesoDisponibles.add(contaPeso);
                        almacenExists.setExistenciaPeso(stockPesoTotal.doubleValue());
                    }
                }
                

                if(tipoMovimiento.equals("TRASPASO")){// Creamos 2 Movimiento 1 salida- origen y 1 entrada  destino
                    
                    
                    //Almacén 1 Salida
                    movimientoDto.setConceptoMovimiento("(SALIDA) "+ porQueMovimiento);
                    movimientoDto.setIdAlmacenDestino(idAlmacenDestino);                    
                                           
                    //
                    BigDecimal numArticulosDisponibles = (new BigDecimal(almacenExists!=null?almacenExists.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal contaMovimiento = (new BigDecimal(contabilidadMovimiento)).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal stockTotal = numArticulosDisponibles.subtract(contaMovimiento);
                    double nuevoStock = stockTotal.doubleValue();                
                    
                    double nuevoStockPeso = 0;
                    if (productoAGranel){
                        BigDecimal pesoDisponibles = (new BigDecimal(almacenExists!=null?almacenExists.getExistenciaPeso():0)).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal contaPeso = (new BigDecimal(contabilidadPeso)).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal stockPesoTotal = pesoDisponibles.subtract(contaPeso);
                        nuevoStockPeso = stockPesoTotal.doubleValue();
                    }
                    
                    //if (nuevoStock<0){
                    if (nuevoStock<0 || nuevoStockPeso<0){
                        msgError+="<ul>El numero de articulos (o peso) disponibles no son suficientes para efectuar la operación.<br/>No. de Articulos disponibles: " + (almacenExists!=null?almacenExists.getExistencia():0);
                    }else{
                        almacenExists.setExistencia(nuevoStock);
                        almacenExists.setExistenciaPeso(nuevoStockPeso);
                    }
                    
                    
                    //Almacén 2 Entrada
                    
                    if (msgError.equals("")){
                        //Busca concepto en almacén destino                    
                        
                        try{
                            almacenExists2  = new ExistenciaAlmacenBO(user.getConn()).getExistenciaProductoAlmacen(idAlmacenDestino, conceptosDto.getIdConcepto());

                            if(almacenExists2==null){
                                nuevo2 = true;
                                almacenExists2 = new ExistenciaAlmacen();
                                almacenExists2.setIdAlmacen(idAlmacenDestino);
                                almacenExists2.setIdConcepto(conceptosDto.getIdConcepto());
                                almacenExists2.setExistencia(0);
                                almacenExists2.setEstatus(1);
                            }

                        }catch(Exception e){                    
                            out.print("<!--ERROR-->No se pudo consultar el registro. Informe del error al administrador del sistema: " + e.toString());
                            System.out.println("Almacen o concepto no encontrado");
                            e.printStackTrace();
                        }                    


                        BigDecimal numArticulosDisponiblesDestino = (new BigDecimal(almacenExists2!=null?almacenExists2.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal contaMovimientoDestino = (new BigDecimal(contabilidadMovimiento)).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal stockTotalDestino = numArticulosDisponiblesDestino.add(contaMovimientoDestino);

                        almacenExists2.setExistencia(stockTotalDestino.doubleValue());

                        if (productoAGranel){
                            BigDecimal pesoDisponiblesDestino = (new BigDecimal(almacenExists2!=null?almacenExists2.getExistenciaPeso():0)).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal contaPesoDestino = (new BigDecimal(contabilidadPeso)).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal stockPesoTotalDestino = pesoDisponiblesDestino.add(contaPesoDestino);
                            almacenExists2.setExistenciaPeso(stockPesoTotalDestino.doubleValue());
                        }

                        //Registra Mov                      
                        movimientoDto2.setIdEmpresa(idEmpresa);
                        movimientoDto2.setTipoMovimiento(tipoMovimiento);
                        movimientoDto2.setNombreProducto(conceptosDto.getNombre());
                        movimientoDto2.setContabilidad(contabilidadMovimiento); 
                        movimientoDto2.setIdAlmacen(idAlmacen);                
                        movimientoDto2.setConceptoMovimiento("(ENTRADA) "+ porQueMovimiento);                
                        try{
                            movimientoDto.setFechaRegistro(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                        }catch(Exception e){
                            movimientoDto.setFechaRegistro(new Date());
                        }
                        movimientoDto2.setIdConcepto(conceptosDto.getIdConcepto());
                        movimientoDto2.setIdAlmacenDestino(idAlmacenDestino); 
                        movimientoDto2.setIdProveedor(proveedorMovimiento);
                        movimientoDto2.setOrdenCompra(ordenCompraMovimiento);
                        movimientoDto2.setNumeroGuia(numGiaMovimiento);
                        movimientoDto2.setContabilidadPeso(contabilidadPeso);
                    }
                }
                
                if (msgError.equals("")){
                    
                    try{
                        /* Actualizamos existencia en almacen*/
                        if(nuevo){
                            new ExistenciaAlmacenDaoImpl().insert(almacenExists); 
                        }else{
                            new ExistenciaAlmacenDaoImpl().update(almacenExists.createPk(), almacenExists);
                        }
                        
                        /**
                        * Realizamos el insert de movimiento despues de modificar existencia
                        */
                       movimientosDaoImpl.insert(movimientoDto);
                       
                       if(tipoMovimiento.equals("TRASPASO")){
                           
                           if(nuevo2){
                                new ExistenciaAlmacenDaoImpl().insert(almacenExists2); 
                           }else{
                                new ExistenciaAlmacenDaoImpl().update(almacenExists2.createPk(), almacenExists2);
                           }
                           
                           movimientosDaoImpl.insert(movimientoDto2);
                       }
                        
                        
                        out.print("<!--EXITO-->Registro guardado satisfactoriamente");
                    }catch(Exception ex){
                        out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                        ex.printStackTrace();
                    } 
                    ////////

                    //out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
                }else{
                    out.print("<!--ERROR-->"+msgError);
                }
            
            }catch(Exception e){
                e.printStackTrace();
                msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
            }
        }
    }else{
        out.print("<!--ERROR-->"+msgError);
    }
}catch(Exception ex){
    out.print("<!--ERROR-->Error inesperado: "+ex.toString());
    ex.printStackTrace();
}
%>