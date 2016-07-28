<%-- 
    Document   : catServicios_list
    Created on : 9/11/2012, 04:14:42 PM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.dao.dto.SarClienteEntrega"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sct.dao.dto.SarUsuarioProveedor"%>
<%@page import="com.tsp.sct.bo.SarUsuarioProveedorBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.Servicio"%>
<%@page import="com.tsp.sct.bo.ServicioBO"%>
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
    if (!buscar.trim().equals(""))
        filtroBusqueda = " AND (EXT_SAR_USUARIO LIKE '%" + buscar + "%' OR EXT_SAR_RFC_PROVEEDOR LIKE '%" +buscar+"%')";
    
    int idSarUsuario = -1;
    try{ idSarUsuario = Integer.parseInt(request.getParameter("id_sar_usuario")); }catch(NumberFormatException e){}
    
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
    
     SarUsuarioProveedorBO sarUsuarioProveedorBO = new SarUsuarioProveedorBO(user.getConn());
     SarUsuarioProveedor[] sarUsuariosProveedorDto = new SarUsuarioProveedor[0];
     try{
         limiteRegistros = sarUsuarioProveedorBO.findSarUsuarioProveedors(idSarUsuario, idEmpresa , 0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        sarUsuariosProveedorDto = sarUsuarioProveedorBO.findSarUsuarioProveedors(idSarUsuario, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../cfdi_sar/cfdi_sar_config_user_form.jsp";
    String paramName = "id_sar_usuario";
    String parametrosPaginacion="q="+buscar;// "idEmpresa="+idEmpresa;
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
        
        <!-- SCRIPT CHAT SOPORTE ZOPIM -->
        <script type="text/javascript" src="../../js/zopim/widgetZopimSAR.js"></script>
        
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
                    <h1>Listado de Usuarios SAR</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_sar_config.png" alt="icon"/>
                                Usuarios SAR Configurados
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                            <tr>
                                                <td>
                                                    <div id="search">
                                                    <form action="catServicios_list.jsp" id="search_form" name="search_form" method="get">
                                                            <input type="text" id="q" name="q" title="Buscar por Nombre Usuario / RFC Proveedor" class="" style="width: 300px; float: left; "
                                                                   value="<%=buscar%>"/>
                                                            <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                    </form>
                                                    </div>
                                                </td>
                                                <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                                <%if(RolAutorizacionBO.permisoNuevoElemento(user.getUser().getIdRoles())){%>
                                                <td>
                                                    <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Crear Nuevo" 
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
                                            <th>Usuario SAR</th>
                                            <th>Mi RFC SAR</th>
                                            <th>RFC Cliente SAR</th>                                            
                                            <th>Razon Social Cliente</th>
                                            <th>Rel Cod-Area?</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            for (SarUsuarioProveedor item : sarUsuariosProveedorDto){
                                                sarUsuarioProveedorBO.setSarUsuarioProveedor(item);
                                                SarClienteEntrega clienteEntrega = sarUsuarioProveedorBO.getSarClienteEntrega();
                                                try{
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%>>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdSarUsuario() %></td>
                                            <td><%=item.getExtSarUsuario() %></td>
                                            <td><%=item.getExtSarRfcProveedor() %></td>
                                            <td><%=clienteEntrega.getExtSarRfc()%></td>
                                            <td><%=clienteEntrega.getExtSarRazonSocial()%></td>                                            
                                            <td><%= (clienteEntrega.getExtSarRelacionCodprovArea()==1?"SI":"NO") %></td>
                                            <td>
                                                <!-- Relacion Area X Cliente -->
                                                <a href="../../jsp/cfdi_sar/cfdi_sar_area_x_cliente.jsp?id_sar_usuario=<%=item.getIdSarUsuario()%>" title="Relación Áreas - Cliente"
                                                    class="modalbox_iframe">
                                                    <img src="../../images/sar_area_x_cliente.png" alt="Relación Áreas - Cliente" class="help" title="Relación Áreas - Cliente"/>
                                                </a>
                                                &nbsp;&nbsp;
                                                <%if(RolAutorizacionBO.permisoAccionesElemento(user.getUser().getIdRoles())){%>
                                                <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdSarUsuario()%>&acc=1"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>
                                                &nbsp;&nbsp;
                                                <!--<a href=""><img src="images/icon_delete.png" alt="delete" class="help" title="Delete"/></a>-->
                                                <%}%>
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
                                    
                            <!-- Include de reportes aquí -->

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