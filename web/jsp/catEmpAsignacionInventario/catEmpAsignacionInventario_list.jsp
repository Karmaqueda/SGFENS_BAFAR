<%-- 
    Document   : 
    Created on : 18/02/2016, 12:01:41 PM
    Author     : ISCesarMartinez
--%>

<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.EmpAsignacionInventario"%>
<%@page import="com.tsp.sct.bo.EmpAsignacionInventarioBO"%>
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
    //if (!buscar.trim().equals(""))
    //    filtroBusqueda = " AND (NOMBRE LIKE '%" + buscar + "%' OR DESCRIPCION LIKE '%" +buscar+"%')";
    
    int idEmpAsignacionInventario = -1;
    try{ idEmpAsignacionInventario = Integer.parseInt(request.getParameter("idAsignacionInventario")); }catch(NumberFormatException e){}
    
    int idEmpleado = -1;
    try{ idEmpleado = Integer.parseInt(request.getParameter("idEmpleado")); }catch(NumberFormatException e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    if (idEmpleado>0)
        filtroBusqueda += " AND id_empleado= " + idEmpleado;
    
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
    
     EmpAsignacionInventarioBO empAsignacionInventarioBO = new EmpAsignacionInventarioBO(user.getConn());
     EmpAsignacionInventario[] empAsignacionInventariosDto = new EmpAsignacionInventario[0];
     try{
         limiteRegistros = empAsignacionInventarioBO.findCantidadEmpAsignacionInventarios(idEmpAsignacionInventario, idEmpresa , 0, 0, filtroBusqueda);
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        empAsignacionInventariosDto = empAsignacionInventarioBO.findEmpAsignacionInventarios(idEmpAsignacionInventario, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catEmpAsignacionInventario/catEmpAsignacionInventarioDetalle_list.jsp";
    String paramName = "idAsignacionInventario";
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
            
            function muestraMensajeUltimaEjecucion(idDivMsg){
                apprise('' + $('#'+idDivMsg).html() + '',{'animate':true, 'warning':true});
            }
            
            function activarDesactivarAsignacion(idAsignacionInventario){
                $.ajax({
                    type: "POST",
                    url: "catEmpAsignacionInventario_ajax.jsp",
                    data: { mode: '1', id_asignacion_inventario : idAsignacionInventario },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#ajax_message").html(datos);
                            $("#ajax_loading").fadeOut("slow");
                            $("#ajax_message").fadeIn("slow");
                            apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                function(r){
                                    location.reload();
                                });
                       }else{                               
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").html(datos);
                           $("#ajax_message").fadeIn("slow");
                           $("#action_buttons").fadeIn("slow");
                           apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                       }
                    }
                });
            }
            
        </script>

    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Almácen</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_producto.png" alt="icon"/>
                                Asignaciones de Inventario Almacenadas
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <!--
                                            <td>
                                                <div id="search">
                                                <form action="catEmpAsignacionInventario_list.jsp" id="search_form" name="search_form" method="get">
                                                        <input type="text" id="q" name="q" title="Buscar por ???" class="" style="width: 300px; float: left; "
                                                               value="<%=buscar%>"/>
                                                        <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                </form>
                                                </div>
                                            </td>
                                            -->
                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
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
                                            <th>Empleado</th>
                                            <th>Activo</th> 
                                            <th>Ultima Repetición</th>
                                            <th>Repetir cada</th>
                                            <th>Próxima Repetición</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
                                            for (EmpAsignacionInventario item : empAsignacionInventariosDto){
                                                try{
                                                    Empleado empleadoDto = empleadoBO.findEmpleadobyId(item.getIdEmpleado());
                                                    String nombreEmpleado = StringManage.getValidString(empleadoDto.getNombre())
                                                                + " " + StringManage.getValidString(empleadoDto.getApellidoPaterno())
                                                                + " " + StringManage.getValidString(empleadoDto.getApellidoMaterno());
                                                    
                                                    Calendar calSiguienteEjecucion = Calendar.getInstance();
                                                    calSiguienteEjecucion.setTime(item.getUltimaRepFechaHr());
                                                    calSiguienteEjecucion.add(Calendar.DAY_OF_MONTH, item.getNumDiasRepeticion());
                                                    Date dateSiguienteEjecucion = calSiguienteEjecucion.getTime();
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%>>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%= item.getIdAsignacionInventario()%></td>
                                            <td><%= nombreEmpleado %></td>
                                            <td><%= (item.getIdEstatus()==1?"SI":"NO")  %></td>                                            
                                            <td>
                                                <b>Éxito:</b> <%= item.getUltimaRepExito()==1?"SI":"NO" %>, <b>Fecha:</b> <%= DateManage.formatDateTimeToNormalMinutes(item.getUltimaRepFechaHr()) %>
                                                <% if (item.getUltimaRepExito()!=1){%>
                                                <a href="#" onclick="muestraMensajeUltimaEjecucion('error_<%= item.getIdAsignacionInventario()%>');">
                                                    &nbsp;&nbsp;&nbsp;
                                                    <img src="../../images/icon_warning.png" alt="error" class="help" title="Error"/>
                                                    Ver Detalle Error
                                                </a>
                                                <div id="error_<%= item.getIdAsignacionInventario()%>" style="display: none; "><%= item.getUltimaRepObservacion() %></div>
                                                <% } %>
                                            </td>
                                            <td><%= item.getNumDiasRepeticion() %> días</td>
                                            <td><%= DateManage.formatDateTimeToNormalMinutes(dateSiguienteEjecucion) %></td>
                                            <td>
                                                <%if(RolAutorizacionBO.permisoAccionesElemento(user.getUser().getIdRoles())){%>
                                                <% if (item.getIdEstatus()==1){%>
                                                <a href="#" onclick="activarDesactivarAsignacion(<%=item.getIdAsignacionInventario()%>);" ><img src="../../images/icon_delete.png" alt="desactivar" class="help" title="Desactivar"/></a>
                                                <% } else {%>
                                                <a href="#" onclick="activarDesactivarAsignacion(<%=item.getIdAsignacionInventario()%>);" ><img src="../../images/icon_accept.png" alt="activar" class="help" title="Activar"/></a>
                                                <% } %>
                                                &nbsp;&nbsp;
                                                <%}%>
                                                <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdAsignacionInventario()%>&acc=1&pagina=<%=paginaActual%>" target="_blank"><img src="../../images/icon_consultar.png" alt="consultar" class="help" title="Ver Detalle"/></a>
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
                                    
                            <div id="action_buttons">

                            </div>
                                    
                            <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                            
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