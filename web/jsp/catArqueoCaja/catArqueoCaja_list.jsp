<%-- 
    Document   : catArqueoCaja_list
    Created on : 17/09/2014, 05:44:11 PM
    Author     : 578
--%>



<%@page import="com.tsp.sct.bo.VentaMetodoPagoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensCobranzaAbono"%>
<%@page import="com.tsp.sct.bo.SGCobranzaAbonoBO"%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.dao.dto.GastosEvc"%>
<%@page import="com.tsp.sct.bo.GastosEvcBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.tsp.sct.util.Converter"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoArqueo"%>
<%@page import="com.tsp.sct.bo.EmpleadoArqueoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedido"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensPedidoDaoImpl"%>
<%@page import="com.tsp.sct.bo.SGPedidoBO"%>
<%@page import="com.tsp.sct.bo.RegionBO"%>
<%@page import="com.tsp.sct.dao.dto.Region"%>
<%@page import="com.tsp.sct.dao.dto.Ruta"%>
<%@page import="com.tsp.sct.dao.jdbc.RutaDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Geocerca"%>
<%@page import="com.tsp.sct.bo.GeocercaBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    NumberFormat formatMoneda = new DecimalFormat("###,###,###,##0.00");
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String filtroBusqueda = ""; //"AND ID_ESTATUS=1 ";
    if (!buscar.trim().equals(""))
        filtroBusqueda += " AND (NOMBRE LIKE '%" + buscar + 
                            "%' OR APELLIDO_PATERNO LIKE '%" + buscar +
                            "%' OR APELLIDO_MATERNO LIKE '%"+buscar+
                            "%' OR NUM_EMPLEADO LIKE '%"+buscar+                                                       
                            "%' OR (ID_MOVIL_EMPLEADO_ROL IN (SELECT ID_ROLES FROM ROLES WHERE ROLES.NOMBRE LIKE '%"+buscar+
                            "%')) OR (ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE EMPRESA.NOMBRE_COMERCIAL LIKE '%"+buscar+
                            "%')) OR (ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE EMPRESA.RAZON_SOCIAL LIKE '%"+buscar+
                            "%'))) OR (ID_ESTATUS IN (SELECT ID_ESTATUS FROM ESTATUS WHERE NOMBRE LIKE '"+buscar+"')) ";
                            
    
   
    int idEmpleado = -1;
    try{ idEmpleado = Integer.parseInt(request.getParameter("idEmpleado")); }catch(NumberFormatException e){}
    
    long idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Paginación
    */      
    int paginaActual = 1;
    double registrosPagina = new ParametrosBO(user.getConn()).getParametroDouble("app.preto.paginacion.registrosPorPagina"); //10;
    double limiteRegistros = 0;
    int paginasTotales = 0;
    int numeroPaginasAMostrar = 5;

    try{
        paginaActual = Integer.parseInt(request.getParameter("pagina"));
    }catch(Exception e){}

    try{
        registrosPagina = Integer.parseInt(request.getParameter("registros_pagina"));
    }catch(Exception e){}
    
     EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
     Empleado[] empleadosDto = new Empleado[0];
     try{
         limiteRegistros = empleadoBO.findEmpleadosActivos(idEmpleado, idEmpresa , 0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        empleadosDto = empleadoBO.findEmpleadosActivos(idEmpleado, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catArqueoCaja/catArqueoCajaDetalle_list.jsp";   
    String paramName = "idEmpleado";
    
    String parametrosPaginacion="";// "idEmpresa="+idEmpresa;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <link rel="shortcut icon" href="../../images/favicon.ico">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../include/keyWordSEO.jsp" />

        <title><jsp:include page="../include/titleApp.jsp" /></title>

        <jsp:include page="../include/skinCSS.jsp" />

        <jsp:include page="../include/jsFunctions.jsp"/>        
        

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
                                <img src="../../images/icon_users.png" alt="icon"/>
                                Arqueo de Caja - Vendedores
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
                                                            <td>
                                                                <div id="search">
                                                                <form action="catArqueoCaja_list.jsp" id="search_form" name="search_form" method="get">                                                                                                                                               
                                                                        <input type="text" id="q" name="q" title="Buscar por # Empleado/Nombre/Apellido Paterno/Materno/Rol/Sucursal/Estatus" class="" style="width: 300px; float: left; "
                                                                               value="<%=buscar%>"/>                                                                        
                                                                        <!--<li> <a title="Buscar" type="submit" id="search" class="right_switch" onclick="search_form.submit(); q.value=''">Buscar </a> </li>-->
                                                                        <!--<input title="Buscar" type="submit" id="search" class="right_switch" style="font-size: 9px;"/>-->
                                                                        <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                                </form>
                                                                </div>
                                                            </td>
                                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                                            <td>
                                                              <!--  <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Crear Nuevo" 
                                                                       style="float: right; width: 100px;" onclick="location.href='<%=urlTo%>'"/> -->
                                                            </td>
                                                        </tr>
                                                </tbody>
                                </table>
                            </div>
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <form id="form_data" name="form_data" action="" method="post">
                                <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th>Número Empleado</th>
                                            <th>Nombre</th>
                                            <th>Zona</th>
                                            <th>Total Vendido</th>
                                            <th>Monto Cobrado</th>
                                            <th>Monto Pendiente<br>por Cobrar</th>
                                            <th>Gastos</th>
                                            <th>Devolución a Cliente</th>
                                            <th>Monto Entregado<br>a Empresa</th>
                                            <th>Adeudo a Empresa</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            for (Empleado item:empleadosDto){
                                                try{
                                                    
                                                    //Obtenemos Region
                                                    Region regionDto = new RegionBO(item.getIdRegion(),user.getConn()).getRegion() ;
                                                    
                                                    SGPedidoBO pedidoBO= new SGPedidoBO(user.getConn());
                                                    SgfensPedido[] pedidosDts = pedidoBO.findPedido(-1,(int)(long)idEmpresa , -1, -1 , " AND ID_USUARIO_VENDEDOR ="+item.getIdUsuarios() + " AND ID_ESTATUS_PEDIDO != 3 ");
                                                    
                                                    EmpleadoArqueoBO empleadoArqueoBO= new EmpleadoArqueoBO(user.getConn());
                                                    EmpleadoArqueo[] arqueosDts = empleadoArqueoBO.findEmpleadoArqueo(-1, (int) (long)idEmpresa ,item.getIdEmpleado() , -1, -1 , "");
                                                    
                                                    double totalVendido = 0;
                                                    double totalCobrado = 0;
                                                    double totalAdeudo = 0;
                                                    double totalPagadoEmpresa = 0;
                                                    double totalDevolucionEfectivo = 0;
                                                    
                                                    if(pedidosDts!=null){
                                                        for(SgfensPedido pedido : pedidosDts ){
                                                            totalVendido += pedido.getTotal();
                                                            
                                                            if(pedido.getBonificacionDevolucion()<0){//Cuando la diferencia es en contra del cliente (este campo es negativo)
                                                                totalVendido += Math.abs(pedido.getBonificacionDevolucion());
                                                            }
                                                            
                                                            totalCobrado += pedido.getSaldoPagado();
                                                            
                                                            if(pedido.getBonificacionDevolucion()>0){
                                                                totalAdeudo += (pedido.getTotal() - pedido.getSaldoPagado());
                                                            }else{
                                                                totalAdeudo += (pedido.getTotal() + Math.abs(pedido.getBonificacionDevolucion())) - pedido.getSaldoPagado();
                                                            }
                                                            
                                                        
                                                        
                                                            try{
                                                                SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(user.getConn());
                                                                SgfensCobranzaAbono[] cobranzaAbonoDtos = new SgfensCobranzaAbono[0];

                                                                String filtroBusquedaCobros = " AND ID_PEDIDO = "  + pedido.getIdPedido() +  " ";                                                                
                                                                cobranzaAbonoDtos = cobranzaAbonoBO.findCobranzaAbono(-1, (int)idEmpresa , 0, 0, filtroBusquedaCobros);
                                                                
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
                                                        
                                                        }
                                                    }
                                                    
                                                    for(EmpleadoArqueo arqueo : arqueosDts ){
                                                        totalPagadoEmpresa += arqueo.getMonto(); 
                                                    }
                                                        
                                                    //Obtenemos gastos
                                                    
                                                    double totalGastos =0;
                                                    try{
                                                        
                                                        GastosEvcBO gastosEvcBO = new GastosEvcBO(user.getConn());
                                                        GastosEvc[] gastosEvcDtos = new GastosEvc[0];
                                                        String filtroGastos =  " AND ID_EMPLEADO=" + item.getIdEmpleado() +" AND VALIDACION = 1 " ;
                                                        gastosEvcDtos = gastosEvcBO.findGastosEvc(-1,item.getIdEmpresa(),-1,-1 , filtroGastos);
                                                        for(GastosEvc gastos:gastosEvcDtos){
                                                            
                                                            totalGastos += gastos.getMonto() ;
                                                        }
                                                        
                                                    }catch(Exception e){e.printStackTrace();}
                                                    
                                                    double totalGralEmp = totalCobrado-totalPagadoEmpresa-totalGastos-totalDevolucionEfectivo;
                                                    
                                        %>
                                        <tr <%=(item.getIdEstatus()==2)?"style='background: #B0B1B1'":""%> >                                            
                                            <td><%=item.getNumEmpleado()%></td>
                                            <td><%=item.getNombre()%> <%=item.getApellidoPaterno()%> <%=item.getApellidoMaterno()%></td>
                                            <td><%=regionDto!=null?regionDto.getNombre():""%></td>
                                            <td><%=formatMoneda.format(totalVendido)%></td>
                                            <td><%=formatMoneda.format(totalCobrado)%></td>                                              
                                            <td><%=formatMoneda.format(totalAdeudo)%></td>
                                            <td><%=formatMoneda.format(totalGastos)%></td>
                                            <td><%=formatMoneda.format(totalDevolucionEfectivo)%></td>
                                            <td><%=formatMoneda.format(totalPagadoEmpresa)%></td>
                                            <td><%=formatMoneda.format(Converter.roundDouble(totalGralEmp))%></td>
                                            <td>                                                
                                                <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdEmpleado()%>"><img src="../../images/icon_ventas1.png" alt="Arqueo de Caja" class="help" title="Arqueo de Caja"/></a>
                                            </td>                                              
                                        </tr>
                                        <%      }catch(Exception ex){
                                                    ex.printStackTrace();
                                                }
                                            } 
                                        %>
                                    </tbody>
                                </table>
                            </form>
                                    
                            <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                                <jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <jsp:param name="idReport" value="<%= ReportBO.ARQUEO_DE_CAJA_REPORT%>" />
                                <jsp:param name="parametrosCustom" value="<%= filtroBusquedaEncoded %>" />
                            </jsp:include>
                            <!-- FIN INCLUDE OPCIONES DE EXPORTACIÓN-->

                            <jsp:include page="../include/listPagination.jsp">
                                <jsp:param name="paginaActual" value="<%=paginaActual%>" />
                                <jsp:param name="numeroPaginasAMostrar" value="<%=numeroPaginasAMostrar%>" />
                                <jsp:param name="paginasTotales" value="<%=paginasTotales%>" />
                                <jsp:param name="url" value="<%=request.getRequestURI() %>" />
                                <jsp:param name="parametrosAdicionales" value="<%=parametrosPaginacion%>" />
                            </jsp:include>
                            
                        </div>
                    </div>

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>


    </body>
</html>
<%}%>
