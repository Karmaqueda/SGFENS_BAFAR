<%-- 
    Document   : catSeguimiento_list
    Created on : 23/05/2013, 07:03:59 PM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.PosMovilEstatus"%>
<%@page import="com.tsp.sct.bo.PosMovilEstatusBO"%>
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
//        filtroBusqueda = " AND (NUM_EMPLEADO LIKE '%" + buscar + "%' OR DESCRIPCION LIKE '%" +buscar+"%' OR APELLIDO_PATERNO LIKE '%"+buscar+" %' OR APELLIDO_MATERNO LIKE '%"+buscar+" %')";
          filtroBusqueda =  " AND (ID_EMPLEADO IN (SELECT ID_EMPLEADO FROM EMPLEADO E WHERE E.NOMBRE LIKE '%"+buscar+ 
                            "%')) OR (ID_EMPLEADO IN (SELECT ID_EMPLEADO FROM EMPLEADO EM WHERE EM.APELLIDO_PATERNO LIKE '%"+buscar+
                            "%')) OR (ID_EMPLEADO IN (SELECT ID_EMPLEADO FROM EMPLEADO EMP WHERE EMP.APELLIDO_MATERNO LIKE '%"+buscar+
                            "%')) OR (ID_EMPLEADO IN (SELECT ID_EMPLEADO FROM EMPLEADO EMN WHERE EMN.NUM_EMPLEADO LIKE '%"+buscar+
                            "%'))";
    
    int idPosMovilEstatus = -1;
    try{ idPosMovilEstatus = Integer.parseInt(request.getParameter("idPosMovilEstatus")); }catch(NumberFormatException e){}
    
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
    
     PosMovilEstatusBO posMovilEstatusBO = new PosMovilEstatusBO(user.getConn());
     PosMovilEstatus[] posMovilEstatussDto = new PosMovilEstatus[0];
     try{
         limiteRegistros = posMovilEstatusBO.findPosMovilEstatuss(idPosMovilEstatus, idEmpresa , 0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        posMovilEstatussDto = posMovilEstatusBO.findPosMovilEstatuss(idPosMovilEstatus, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catSeguimiento/catSeguimiento_form.jsp";
    String urlTo2 = "MapaSeguimiento.jsp";
    String paramName = "idPosMovilEstatus";
    String parametrosPaginacion="";// "idEmpresa="+idEmpresa;
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
                                <img src="../../images/icon_seguimiento.png" alt="icon"/>
                                Seguimiento Movil Estatus
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                            <tr>
                                                <td>
                                                    <div id="search">
                                                    <form action="catSeguimiento_list.jsp" id="search_form" name="search_form" method="get">
                                                            <input type="text" id="q" name="q" title="Buscar por Nombre/ # Empleado" class="" style="width: 300px; float: left; "
                                                                    value="<%=buscar%>"/>
                                                            <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                    </form>
                                                    </div>
                                                </td>
                                                <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                                <%if(RolAutorizacionBO.permisoNuevoElemento(user.getUser().getIdRoles())){%>
                                                <td>
                                                    <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Tiempo" title="Tiempo para Solicitar estatus" 
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
                                            <th># Empleado</th>
                                            <th>Empleado</th>
                                            <th>Fecha</th>
                                            <th>Descripción</th>
                                            <th>Foto</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                        Empleado empleado = new Empleado();
                                        EmpleadoBO eBO = new EmpleadoBO(user.getConn());
                                            for (PosMovilEstatus item:posMovilEstatussDto){
                                                try{
                                                    empleado = eBO.findEmpleadobyId(item.getIdEmpleado());
                                                    String linkImagen = "";
                                                    if (StringManage.getValidString(item.getNombreImagen()).length()>0){
                                                        linkImagen = "<a href='showImageEstatus.jsp?image=" + item.getNombreImagen() + "' title='Ver Foto' class='modalbox_iframe'>"
                                                             + "<img src='../../images/icon_consultar.png' alt='Ver Foto' class='help' title='Ver Foto'/>"
                                                             + "</a>";
                                                    }
                                        %>
                                        <tr>   
                                            <td><%=empleado.getNumEmpleado() %></td>
                                            <td><%=empleado.getNombre()+" "+empleado.getApellidoPaterno()+" "+empleado.getApellidoMaterno() %></td>
                                            <td><%=item.getFechaEstatus() %></td>
                                            <td><%=item.getMensajeEstatus()%></td>
                                            <td><%= linkImagen %>
                                            </td>
                                            <td>
                                                <%if(RolAutorizacionBO.permisoAccionesElemento(user.getUser().getIdRoles())){%>
                                                <a href="<%=urlTo2%>?<%=paramName%>=<%=item.getIdMovilEstatus()%>" id="consultaForma" title="Consultar Estatus" name="Consultar Estatus" class="modalbox_iframe" >
                                                    <img src="../../images/icon_agregar.png" alt="Consultar estatus" class="help" title="Consultar Estatus"/><br/>
                                                </a>
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

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>


    </body>
</html>
<%}%>