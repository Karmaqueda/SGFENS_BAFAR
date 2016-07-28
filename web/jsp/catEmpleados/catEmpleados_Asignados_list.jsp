<%-- 
    Document   : catEmpleados_Asignados_list
    Created on : 13/04/2015, 01:14:31 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    int idEmpleado = -1;
    try{ idEmpleado = Integer.parseInt(request.getParameter("idEmpleado")); }catch(NumberFormatException e){}
    
    Empleado empleadoDto = null;
    try{
        EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
        if(idEmpleado > 0){
            empleadoDto = empleadoBO.findEmpleadobyId(idEmpleado);
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    
    String filtroBusqueda = " AND (ID_USUARIOS IN (SELECT ID_USUARIO_EMPLEADO_ASIGNADO FROM SGFENS_ASIGNACION_EMPLEADOS WHERE ID_ESTATUS = 1 AND  ID_USUARIO_EMPLEADO = "+ empleadoDto.getIdUsuarios() +"))";
    if (!buscar.trim().equals(""))
        filtroBusqueda += " AND (NOMBRE LIKE '%" + buscar + "%' OR DESCRIPCION LIKE '%" +buscar+"%')";
        
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
    
    EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
    Empleado[] empleadosDto = new Empleado[0];
    
        try{
            limiteRegistros = empleadoBO.findEmpleados(0, 0 , 0, 0, filtroBusqueda).length;
            

            if (!buscar.trim().equals(""))
                registrosPagina = limiteRegistros;

            paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

           if(paginaActual<0)
               paginaActual = 1;
           else if(paginaActual>paginasTotales)
               paginaActual = paginasTotales;

           empleadosDto = empleadoBO.findEmpleados(0, idEmpresa,
                   ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                   filtroBusqueda);



        }catch(Exception ex){
            ex.printStackTrace();
        }
    
     /*
    * Datos de catálogo
    */
    String urlTo = "../catEmpleados/catEmpleadosConceptosRelacionados_form.jsp";
    String paramName = "idEmpleadoRelacionConcepto";
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
            function eliminarVendedorRelacionado(idUsuarioEmpleadoAsignado, idUsuarioEmpleado){
                if (idUsuarioEmpleadoAsignado>0){
                    $.ajax({
                        type: "POST",
                        url: "catEmpleados_Asignados_ajax.jsp",
                        data: {mode: 'borrarAsignacion', idVendedorAsignado : idUsuarioEmpleadoAsignado, idUsuarioEmpleado : idUsuarioEmpleado, idSgfensAsignacionEmpleados : 1},
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_message").html(datos);
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                        function(r){
                                            location.href = "catEmpleados_Asignados_list.jsp?idEmpleado=<%=idEmpleado%>";
                                        });
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               $("#action_buttons").fadeIn("slow");
                               $.scrollTo('#inner',800);
                           }
                        }
                    });
                 }
            }            
        </script>

    </head>
    <body class="nobg">
            <!-- Inicio de Contenido -->           
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>
                    
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_producto.png" alt="icon"/>
                                Vendedores del Empleado  <%=empleadoDto!=null?empleadoDto.getNombre():""%>
                            </span>
                            <div class="switch" style="width:100px">
                                <table width="300px" cellpadding="0" cellspacing="0">
						
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
                                            <th>Número Empleado</th>
                                            <th>Nombre</th>
                                            <th>Apellido Paterno</th> 
                                            <th>Apellido Materno</th>                                          
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            for (Empleado item: empleadosDto){
                                                try{
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%>>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdEmpleado() %></td>
                                            <td><%=item.getNumEmpleado()%></td>
                                            <td><%=item.getNombre() %></td>
                                            <td><%=item.getApellidoPaterno() %></td>
                                            <td><%=item.getApellidoMaterno() %></td>
                                            <td>                                                
                                                <a href="#" onclick="eliminarVendedorRelacionado(<%=item.getIdUsuarios()%>, <%=empleadoDto.getIdUsuarios()%>);"><img src="../../images/icon_delete.png" alt="delete" class="help" title="Borrar"/></a>
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
                                <//jsp:param name="idReport" value="<//%= ReportBO.IMPUESTO_REPORT %>" />
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
            <!-- Fin de Contenido-->
       


    </body>
</html>
<%}%>