<%-- 
    Document   : catCallCenterSeguimiento_list
    Created on : 24/02/2016, 04:04:45 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.dao.jdbc.DatosUsuarioDaoImpl"%>
<%@page import="com.tsp.sct.dao.jdbc.UsuariosDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.DatosUsuario"%>
<%@page import="com.tsp.sct.dao.dto.Usuarios"%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.CallCenterSeguimiento"%>
<%@page import="com.tsp.sct.bo.CallCenterSeguimientoBO"%>
<%@page import="com.tsp.sct.bo.CallCenterBO"%>
<%@page import="com.tsp.sct.dao.jdbc.CallCenterDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CallCenter"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
        
    int idCallCenter = -1;
    try{ idCallCenter = Integer.parseInt(request.getParameter("idCallCenter")); }catch(NumberFormatException e){}
    
    String filtroBusqueda = " AND ID_CALL_CENTER = " + idCallCenter;
    //if (!buscar.trim().equals(""))
    //    filtroBusqueda = " AND (NOMBRE LIKE '%" + buscar + "%' OR DESCRIPCION LIKE '%" +buscar+"%')";    
    
    int idCallCenterSeguimiento = -1;
    try{ idCallCenterSeguimiento = Integer.parseInt(request.getParameter("idCallCenterSeguimiento")); }catch(NumberFormatException e){}
    
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
    
     CallCenterSeguimientoBO callCenterSeguimientoBO = new CallCenterSeguimientoBO(user.getConn());
     CallCenterSeguimiento[] callCenterSeguimientosDto = new CallCenterSeguimiento[0];
     try{
         limiteRegistros = callCenterSeguimientoBO.findCallCenterSeguimientos(idCallCenterSeguimiento, 0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        callCenterSeguimientosDto = callCenterSeguimientoBO.findCallCenterSeguimientos(idCallCenterSeguimiento, 
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
    CallCenterBO callCenterBO = new CallCenterBO(user.getConn());
    CallCenter callCentersDto = null;
    if (idCallCenter > 0){
        callCenterBO = new CallCenterBO(idCallCenter,user.getConn());
        callCentersDto = callCenterBO.getCallCenter();
    }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catCallCenter/catCallCenterSeguimiento_form.jsp";
    String paramName = "idCallCenterSeguimiento";
    String parametrosPaginacion="idCallCenter="+idCallCenter;
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
                    <h1>Seguimiento</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_callCenterSeguimiento.png" alt="icon"/>
                               Seguimiento de Call Center<%=callCentersDto!=null?" del ticket " + callCentersDto.getNumeroTicket():"" %>
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                            <tr>
                                                <td>
                                                    <div id="search">
                                                    <!--<form action="catCallCenterSeguimientos_list.jsp" id="search_form" name="search_form" method="get">
                                                            <input type="text" id="q" name="q" title="Buscar por Nombre / Descripción" class="" style="width: 300px; float: left; "
                                                                    value="<%=buscar%>"/>
                                                            <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                    </form>-->
                                                    </div>
                                                </td>
                                                <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                                
                                                <td>
                                                    <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Crear Nuevo" 
                                                            style="float: right; width: 100px;" onclick="location.href='<%=urlTo+"?idCallCenter="+idCallCenter+"&acc=1"%>'"/>
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
                                            <th>ID</th>
                                            <th>Usuario que Registro</th>
                                            <th>Fecha/Hora</th>
                                            <th>Descripción</th>                                            
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            UsuariosDaoImpl usuariosDaoImpl = new UsuariosDaoImpl(user.getConn());
                                            DatosUsuarioDaoImpl datosUsuarioDaoImpl = new DatosUsuarioDaoImpl(user.getConn());
                                            for (CallCenterSeguimiento item : callCenterSeguimientosDto){
                                                Usuarios usuario = null;
                                                DatosUsuario datos = null;
                                                String nombreUsuarioAlias = "";
                                                try{
                                                    usuario = usuariosDaoImpl.findByPrimaryKey(item.getIdUsuario());
                                                    datos = datosUsuarioDaoImpl.findByPrimaryKey(usuario.getIdDatosUsuario());
                                                    nombreUsuarioAlias = usuario.getUserName() + ", " + (datos.getNombre()!=null?datos.getNombre():"") + " " + (datos.getApellidoPat()!=null?datos.getApellidoPat():"") + " " + (datos.getApellidoMat()!=null?datos.getApellidoMat():"");
                                                }catch(Exception e){}
                                                
                                                try{
                                        %>
                                        <tr>                                            
                                            <td><%=item.getIdCallCenterSeguimiento() %></td>
                                            <td><%=nombreUsuarioAlias %></td>
                                            <td><%=DateManage.formatDateTimeToNormalMinutes(item.getFechaCreacion())%></td>
                                            <td><%=item.getDescripcion()%></td>                                            
                                            
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
                            <!--<//jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <//jsp:param name="idReport" value="<//%= ReportBO.MARCA_REPORT %>" />
                                <//jsp:param name="parametrosCustom" value="<//%= filtroBusquedaEncoded %>" />
                            <///jsp:include>-->
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
                                    
                                    <div id="action_buttons">
                                        <p>                                            
                                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                        </p>
                                    </div>

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>


    </body>
</html>
<%}%>