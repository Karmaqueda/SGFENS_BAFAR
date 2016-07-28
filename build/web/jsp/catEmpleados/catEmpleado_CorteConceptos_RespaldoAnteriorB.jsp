
<%@page import="java.math.BigDecimal"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensPedidoProductoDaoImpl"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedidoDevolucionCambio"%>
<%@page import="com.tsp.sct.bo.SGPedidoDevolucionesCambioBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedidoProducto"%>
<%@page import="com.tsp.sct.bo.SGPedidoProductoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedido"%>
<%@page import="com.tsp.sct.bo.SGPedidoBO"%>
<%@page import="com.tsp.sct.dao.dto.InventarioInicialVendedor"%>
<%@page import="com.tsp.sct.bo.InventarioInicialVendedorBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoInventarioRepartidor"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.tsp.sct.bo.EmpleadoInventarioRepartidorBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    NumberFormat formatMoneda = new DecimalFormat("###,###,###,##0.00");
    
    int idEmpleado = 0; 
    try{
        idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
    }catch(NumberFormatException ex){}
    
        
    String filtroBusqueda = " AND ID_EMPLEADO = "+idEmpleado + " ";
    
        
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Paginación
    */
    int paginaActual = 1;
    double registrosPagina = 30;
    double limiteRegistros = 0;
    int paginasTotales = 0;
    int numeroPaginasAMostrar = 5;

    try{
        paginaActual = Integer.parseInt(request.getParameter("pagina"));
    }catch(Exception e){}

    try{
        registrosPagina = Integer.parseInt(request.getParameter("registros_pagina"));
    }catch(Exception e){}
    
     InventarioInicialVendedorBO empleadoInventarioInicialBO = new InventarioInicialVendedorBO(user.getConn());
     InventarioInicialVendedor[] empleadoInventarioRepartidorDto = new InventarioInicialVendedor[0];
          
   
     
     try{
                 
         limiteRegistros = empleadoInventarioInicialBO.findInventarioInicialByIdEmpleado(idEmpleado , 0, 0, filtroBusqueda).length;
                  
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        empleadoInventarioRepartidorDto = empleadoInventarioInicialBO.findInventarioInicialByIdEmpleado(idEmpleado,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);        
        

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     
     //Obtenemos empleado
     Empleado empleadoDto = new Empleado();
     try{
         EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());     
         empleadoDto = empleadoBO.findEmpleadobyId(idEmpleado);
     }catch(Exception e){} 
     
     /*
    * Datos de catálogo
    */       
    String parametrosPaginacion="idEmpleado="+idEmpleado;// "idEmpresa="+idEmpresa;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    String strParamsExtra  =""+idEmpleado;
    String parametrosExtraEncoded = java.net.URLEncoder.encode(strParamsExtra , "UTF-8");
    String infoTitle  ="Empleado: " + empleadoDto.getNombre() +" "+ empleadoDto.getApellidoPaterno() +" "+ empleadoDto.getApellidoMaterno();
    String infoTitleEncoded = java.net.URLEncoder.encode(infoTitle , "UTF-8");
    
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../include/keyWordSEO.jsp" />

        <title><jsp:include page="../include/titleApp.jsp" /></title>

        <jsp:include page="../include/skinCSS.jsp" />

        <jsp:include page="../include/jsFunctions.jsp"/>
        
        <script type="text/javascript">           
          
            
        </script>

    </head>
    <body>
        
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Ventas</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>
        
         
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/inventario_16.png" alt="icon"/>
                                Corte De Productos &nbsp;&nbsp;&nbsp;&nbsp;<%=empleadoDto.getNombre() +" "+ empleadoDto.getApellidoPaterno() +" "+ empleadoDto.getApellidoMaterno() %>
                            </span>
                            
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <form id="form_data" name="form_data" action="" method="post">
                                <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Nombre</th>                                            
                                            <th>Descripción</th>                                             
                                            <th>Cantidad Asignada</th>
                                            <th>Cantidad Vendida</th>
                                            <!--<th>Entregado por Pedidos</th>-->
                                            <th>Cantidad Devuelta</th>
                                            <!--<th>Cantidad a Cambio</th>-->
                                            <th>Cambio Mismo Producto</th>
                                            <th>Cambio Producto Distinto</th>
                                            <th>Cantidad Merma</th>
                                            <th>Inventario Final</th>  
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                        Encrypter encripDesencri = new Encrypter();
                                        SGPedidoBO pedidosBO = new SGPedidoBO(user.getConn()); 
                                        SGPedidoProductoBO partidasPedidoBO = new SGPedidoProductoBO(user.getConn());
                                        SGPedidoDevolucionesCambioBO camDevBO = new SGPedidoDevolucionesCambioBO(user.getConn());
                                         
                                        double cantidadVendida = 0;
                                        double cantidadDevoluciones = 0;
                                        double cantidadMerma = 0;
                                        double cantidadCambios = 0;
                                        double invFinal = 0;
                                        double totalProdsVendidos = 0;
                                        double totalProdsMerma = 0;
                                        double totalProdsVendidosDinero = 0;
                                        double totalProdsMermaDinero = 0;
                                        String fechaInicial = "";
                                        
                                        BigDecimal cantidadEntregadoPorPedidos = new BigDecimal("0.0");////
                                        SgfensPedidoProducto[] sgfensPedidoProducto = null;////
                                        SgfensPedidoProductoDaoImpl sgfensPedidoProductoDaoImpl = new SgfensPedidoProductoDaoImpl(user.getConn());////                                        
                                        double cantidadMismoProducto = 0;////
                                        double cantidadDistitoProducto = 0;////
                                        
                                                                                
                                            for (InventarioInicialVendedor item:empleadoInventarioRepartidorDto){
                                                //Limpiamos variables
                                                cantidadVendida = 0 ;
                                                cantidadDevoluciones = 0;
                                                cantidadMerma = 0;
                                                cantidadCambios = 0;
                                                invFinal = 0;
                                                fechaInicial = item.getFechaRegistro().toString();
                                                
                                                cantidadEntregadoPorPedidos = new BigDecimal("0.0");////
                                                sgfensPedidoProducto = null;////
                                                cantidadMismoProducto = 0;////
                                                cantidadDistitoProducto = 0;////
                                                
                                                try{////
                                                    sgfensPedidoProducto = sgfensPedidoProductoDaoImpl.findWhereIdConceptoEquals(item.getIdConcepto());}catch(Exception e){}////
                                                if(sgfensPedidoProducto != null){////
                                                    for(SgfensPedidoProducto productos : sgfensPedidoProducto){////
                                                        cantidadEntregadoPorPedidos = cantidadEntregadoPorPedidos.add(new BigDecimal(productos.getCantidadEntregada()));////
                                                    }////
                                                }////
                                                
                                                
                                                
                                                try{                                                    
                                                    Concepto concepto = new ConceptoBO(item.getIdConcepto(),user.getConn()).getConcepto();
                                                    
                                                   
                                                    //Ventas
                                                    //Obtenemos Pedidos del vendedor
                                                    System.out.println("******PEDIDOS*******");
                                                    String filtroPedidosVendedor = " AND ID_USUARIO_VENDEDOR ='" + empleadoDto.getIdUsuarios() + "'  AND FECHA_PEDIDO >= '" +item.getFechaRegistro() + "' AND (ID_ESTATUS_PEDIDO <> 3 AND ID_ESTATUS_PEDIDO <> 1) ";                                                                                                                                                        
                                                    SgfensPedido[] pedidos = pedidosBO.findPedido(-1, idEmpresa, -1, -1, filtroPedidosVendedor);
                                                    
                                                    if(pedidos.length>0){
                                                        
                                                        for(SgfensPedido pedido : pedidos){
                                                            //Obtenemos partidas del pedido
                                                            System.out.println("******PRODUCTOS*******");
                                                            SgfensPedidoProducto[] partidasPedido = partidasPedidoBO.findByIdConcepto(item.getIdConcepto(), -1, -1," ID_PEDIDO="+pedido.getIdPedido() );
                                                            if(partidasPedido.length>0){
                                                                for(SgfensPedidoProducto partida:partidasPedido ){
                                                                    //Sumamos cantidades
                                                                    
                                                                    cantidadVendida += partida.getCantidadEntregada();
                                                                    totalProdsVendidosDinero += (partida.getCantidadEntregada()* partida.getPrecioUnitario());
                                                                }
                                                            }  
                                                        }                                                       
                                                                                                               
                                                    }   
                                                    
                                                    
                                                    //Obtenemos devoluciones y cambios del pedido
                                                  
                                                    System.out.println("******DEVOLUCIONES Y CAMBIOS*******");
                                                    String filtroDevs  = " AND FECHA  >= '"+ item.getFechaRegistro()+"' ";
                                                    SgfensPedidoDevolucionCambio[] devoluciones = camDevBO.findCambioDevByEmpleado(user.getConn(), empleadoDto.getIdEmpleado(), filtroDevs);
                                                    
                                                    if(devoluciones.length>0){
                                                        for(SgfensPedidoDevolucionCambio dev:devoluciones){
                                                            
                                                             //Precio de venta del pedido 
                                                            SgfensPedidoProducto[] conceptoPedidoDto = partidasPedidoBO.findByIdPedido(dev.getIdPedido(), -1, -1, -1, " AND ID_CONCEPTO="+dev.getIdConcepto());
                                                                
                                                            //if(conceptoPedidoDto.length>0){
                                                                
                                                                if(item.getIdConcepto()==dev.getIdConcepto()){

                                                                    //cantidadDevoluciones += dev.getAptoParaVenta();
                                                                    cantidadMerma += dev.getNoAptoParaVenta();
                                                                    try{
                                                                        totalProdsMermaDinero += ( conceptoPedidoDto[0].getPrecioUnitario());
                                                                    }catch(Exception e){
                                                                        totalProdsMermaDinero += 0;
                                                                    }
                                                                    if(dev.getIdTipo() == 2){////cambio
                                                                        if(item.getIdConcepto() == dev.getIdConceptoEntregado()){////
                                                                            cantidadMismoProducto += (dev.getAptoParaVenta() + dev.getNoAptoParaVenta());////
                                                                        }else{////
                                                                            cantidadDistitoProducto += (dev.getAptoParaVenta() + dev.getNoAptoParaVenta());////
                                                                        }///
                                                                    }
                                                                }     

                                                                if(item.getIdConcepto()==dev.getIdConceptoEntregado()){
                                                                    if(dev.getCantidadDevuelta()>0){
                                                                        cantidadCambios += dev.getCantidadDevuelta();
                                                                    }
                                                                }
                                                           // }

                                                        }
                                                    }
                                                    
                                                    //cantidadVendida += cantidadMismoProducto;////
                                                    
                                                   
                                                    //invFinal = item.getCantidad() - cantidadVendida + cantidadDevoluciones - cantidadCambios + cantidadMerma;
                                                    //invFinal = item.getCantidad() - cantidadVendida + cantidadDevoluciones - cantidadCambios;
                                                    //invFinal = item.getCantidad() - cantidadVendida + (cantidadDevoluciones !=0? cantidadDevoluciones : cantidadMerma) - cantidadMismoProducto + cantidadDistitoProducto;
                                                    if(cantidadMerma != 0){
                                                        invFinal = item.getCantidad() - cantidadVendida + (cantidadMerma) - cantidadCambios;
                                                    }else{
                                                        //invFinal = item.getCantidad() - cantidadVendida - cantidadMismoProducto + cantidadDistitoProducto - cantidadCambios;
                                                        invFinal = item.getCantidad() - cantidadVendida - cantidadCambios + cantidadMismoProducto;                                                        
                                                    }
                                                    
                                                    totalProdsVendidos += cantidadVendida + cantidadCambios;
                                                    totalProdsMerma += cantidadMerma;
                                                    
                                                    
                                                    
                                        %>
                                                <tr>                                            
                                                    <td><%=item.getIdConcepto()%></td>                                            
                                                    <td><%=encripDesencri.decodeString(concepto.getNombre())%></td>
                                                    <td><%=concepto.getDescripcion()%></td>    
                                                    <td><%=item.getCantidad()%></td> 
                                                    <td><%=(cantidadVendida + cantidadCambios)%></td> 
                                                    <!--<td><//%=cantidadEntregadoPorPedidos%></td> -->
                                                    <td><%=(cantidadDevoluciones !=0? cantidadDevoluciones : cantidadMerma)%></td> 
                                                    <!--<td><//%=cantidadCambios%></td> -->
                                                    <td><%=cantidadMismoProducto%></td> 
                                                    <td><%=cantidadDistitoProducto%></td> 
                                                    <td><%=cantidadMerma%></td> 
                                                    <td><%=invFinal>=0?invFinal:0%></td> 
                                                </tr>
                                                
                                        <%     
                                                }catch(Exception ex){
                                                    ex.printStackTrace();
                                                }
                                            } 
                                        %>
                                            <tr>
                                                <td colspan="4" style="text-align: center;"><b>Articulos Vendidos:</b></td>
                                                <td style="color: #0000cc; text-align: left;"><%=totalProdsVendidos%></td>
                                                <td style="text-align: left;"><b>Total:</b></td>
                                                <td style="color: #0000cc; text-align: left;">$ <%=formatMoneda.format(totalProdsVendidosDinero)%></td>                                                    
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                
                                            </tr>        
                                            <tr>
                                                <td colspan="4" style="text-align: center;"><b>Articulos en Merma:</b></td>
                                                <td style="color: #0000cc; text-align: left;"><%=totalProdsMerma%></td>
                                                <td style="text-align: left;"><b>Total:</b></td>
                                                <td style="color: #0000cc; text-align: left;">$ <%=formatMoneda.format(totalProdsMermaDinero)%></td>   
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                            </tr> 
                                            <tr>
                                                <td colspan="2" style="text-align: left;">Inicio de Inventario: </br><%=fechaInicial%></td> 
                                                <td colspan="10"></td>
                                                
                                            </tr> 
                                    </tbody>
                                </table>
                            </form>
                                    
                                    
                                    
                            <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <jsp:param name="idReport" value="<%= ReportBO.EMPLEADO_CORTE_INVENTARIO_REPORT %>" />
                                <jsp:param name="parametrosCustom" value="<%= filtroBusquedaEncoded %>" />
                                <jsp:param name="parametrosExtra" value="<%=parametrosExtraEncoded%>" />
                                <jsp:param name="infoTitle" value="<%=infoTitleEncoded%>" />
                            </jsp:include>
                            <!-- FIN INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                            
                            <jsp:include page="../include/listPagination.jsp">
                                <jsp:param name="paginaActual" value="<%=paginaActual%>" />
                                <jsp:param name="numeroPaginasAMostrar" value="<%=numeroPaginasAMostrar%>" />
                                <jsp:param name="paginasTotales" value="<%=paginasTotales%>" />
                                <jsp:param name="url" value="<%=request.getRequestURI() %>" />
                                <jsp:param name="parametrosAdicionales" value="<%=parametrosPaginacion%>" />
                            </jsp:include>
                            
                        </div>
                    </div>
                 <!-- Fin de Contenido-->
                 </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>
       


    </body>
</html>
<%}%>