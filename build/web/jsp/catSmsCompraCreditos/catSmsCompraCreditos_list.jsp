<%-- 
    Document   : catSmsCompraCreditos_list
    Created on : 10/03/2016, 12:55:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.SmsPaquetePrecioBO"%>
<%@page import="com.tsp.sct.dao.dto.SmsPaquetePrecio"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.SmsCompraBitacora"%>
<%@page import="com.tsp.sct.bo.SmsCompraBitacoraBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String filtroBusqueda = "";
    
    int idSmsCompraBitacora = -1;
    try{ idSmsCompraBitacora = Integer.parseInt(request.getParameter("idSmsCompraBitacora")); }catch(NumberFormatException e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
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
    
     SmsCompraBitacoraBO smsCompraBitacoraBO = new SmsCompraBitacoraBO(user.getConn());
     SmsCompraBitacora[] smsCompraBitacorasDto = new SmsCompraBitacora[0];
     try{
         limiteRegistros = smsCompraBitacoraBO.findCantidadSmsCompraBitacoras(idSmsCompraBitacora, idEmpresa , 0, 0, filtroBusqueda);
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        smsCompraBitacorasDto = smsCompraBitacoraBO.findSmsCompraBitacoras(idSmsCompraBitacora, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     SmsPaquetePrecioBO smsPaquetePrecioBO = new SmsPaquetePrecioBO(user.getConn());
     
     Empresa empresaMatriz = new EmpresaBO(user.getConn()).getEmpresaMatriz(idEmpresa);
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catSmsCompraCreditos/catSmsCompraCreditos_form.jsp";
    String paramName = "idSmsCompraBitacora";
    String parametrosPaginacion="q="+buscar;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    
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
                    <h1>SMS Masivos</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>
                    
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_smsCompra.png" alt="icon"/>
                                Bitácora de Compra Créditos SMS
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td>
                                                <h2>
                                                <i>Créditos SMS: </i><b><%= empresaMatriz.getCreditosSms() %></b>
                                                </h2>
                                            </td>
                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                            <%if(RolAutorizacionBO.permisoNuevoElemento(user.getUser().getIdRoles())){%>
                                            <td>
                                                <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Adquirir Créditos" 
                                                       style="float: right; width: 100px;" onclick="location.href='<%=urlTo%>'"/>
                                            </td>
                                            <%}%>
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
                                            <th>ID</th>
                                            <th>Auto-Servicio</th>
                                            <th>Fecha Hora</th>                                            
                                            <th>Paquete</th>
                                            <th>Cantidad</th>
                                            <th>Observaciones</th>
                                            <th>Datos de Pago</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            for (SmsCompraBitacora item : smsCompraBitacorasDto){
                                                try{
                                                    String nombrePaquete = "-Libre-";
                                                    String cantidad =  "0";
                                                    String datosPago = "";
                                                    try{
                                                        if (item.getIdSmsPaquetePrecio()>0){
                                                            SmsPaquetePrecio paquete = smsPaquetePrecioBO.findSmsPaquetePreciobyId(item.getIdSmsPaquetePrecio());
                                                            nombrePaquete = paquete.getNombrePaquete();
                                                        }
                                                        if (item.getCantidadAgregada()>0){
                                                            cantidad = " <b>+</b> " + item.getCantidadAgregada();
                                                        }else if (item.getCantidadRestada()>0){
                                                            cantidad = " <b>-</b> " + item.getCantidadRestada();
                                                        }
                                                        datosPago = ""
                                                                + "Auth Code: " + StringManage.getValidString(item.getDatoPago1())
                                                                + "<br/>Referencia: " + StringManage.getValidString(item.getDatoPago2())
                                                                + "<br/>Id Banwire: " + StringManage.getValidString(item.getDatoPago3())
                                                                + "<br/>" + StringManage.getValidString(item.getDatoPago4())
                                                                ;
                                                    }catch(Exception e){}
                                        %>
                                        <tr>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%= item.getIdSmsCompraBitacora()%></td>
                                            <td><%= item.getIsAutoServicio()==1? "SI":"NO" %></td>
                                            <td><%= DateManage.formatDateTimeToNormalMinutes(item.getFechaHr()) %></td>
                                            <td><%= StringManage.getValidString(nombrePaquete) %></td>
                                            <td><%= StringManage.getValidString(cantidad) %></td>
                                            <td><%= StringManage.getValidString(item.getObservaciones()) %></td>
                                            <td><%= StringManage.getValidString(datosPago) %></td>
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
                            <!--
                            < jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                < jsp:param name="idReport" value="<%= ReportBO.SMS_AGENDA_DESTINATARIOS_REPORT %>" />
                                < jsp:param name="parametrosCustom" value="<%= filtroBusquedaEncoded %>" />
                            </ jsp:include>
                            -->
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

        <script>
            
        </script>
    </body>
</html>
<%}%>