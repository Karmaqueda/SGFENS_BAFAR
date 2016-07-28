<%-- 
    Document   : catProveedorProductos_list
    Created on : 17/08/2015, 05:58:31 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.bo.SGProductoServicioBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProductoServicio"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProveedorProducto"%>
<%@page import="com.tsp.sct.bo.SGProveedorProductoBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    int idProveedor = -1;
     try{
        idProveedor = Integer.parseInt(request.getParameter("idProveedor"));
    }catch(Exception e){}
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String filtroBusqueda = " AND ID_SGFENS_PRODUCTO_PROVEEDOR = " + idProveedor ;
    if (!buscar.trim().equals(""))
        filtroBusqueda = " AND (NOMBRE LIKE '%" + buscar + "%' OR DESCRIPCION LIKE '%" +buscar+"%')";
    
    int idSgfensProveedorProducto = -1;
    try{ idSgfensProveedorProducto = Integer.parseInt(request.getParameter("idSgfensProveedorProducto")); }catch(NumberFormatException e){}
    
    
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
    
     SGProveedorProductoBO sgfensProveedorProductoBO = new SGProveedorProductoBO(user.getConn());
     SgfensProveedorProducto[] sgfensProveedorProductosDto = new SgfensProveedorProducto[0];
     try{
         limiteRegistros = sgfensProveedorProductoBO.findSgfensProveedorProductos(idSgfensProveedorProducto, idEmpresa , 0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        sgfensProveedorProductosDto = sgfensProveedorProductoBO.findSgfensProveedorProductos(idSgfensProveedorProducto, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catProveedores/catProveedorProductos_form.jsp";
    String paramName = "idSgfensProveedorProducto";
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
            function confirmarBorrar(idProveeProducto){
                apprise('¿Esta seguro que desea eliminar el Registro?', {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, 
                    function(r){
                        if(r){
                            // Usuario dio click 'Yes'
                            eliminarProductoDeProveedor(idProveeProducto);
                        }
                });
                
            }
            
            function eliminarProductoDeProveedor(idProveeProducto){
                if(idProveeProducto>=0){
                    $.ajax({
                        type: "POST",
                        url: "catProveedorProductos_ajax.jsp",
                        data: { mode: 'eliminarProductoDeProveedor', idProveeProducto : idProveeProducto },
                        beforeSend: function(objeto){
                            //$("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style="">ESPERE, procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                    function(r){
                                        location.href = "catProveedorProductos_list.jsp?idProveedor="+"<%=idProveedor%>";
                                });
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               //$("#ajax_message").html(datos);
                               //$("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                           }
                        }
                    });
                }            
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
                    <h1>Proveedor</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_producto.png" alt="icon"/>
                                Catálogo de Productos / Servicios del Proveedor con ID <%=idProveedor%>
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
                                                            <td>
                                                                <div id="search">
                                                                <form action="catProductosCompra_list.jsp" id="search_form" name="search_form" method="get">
                                                                        <input type="text" id="q" name="q" title="Buscar por Nombre / Descripción" class="" style="width: 300px; float: left; "
                                                                               value="<%=buscar%>"/>
                                                                        <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                                </form>
                                                                </div>
                                                            </td>
                                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                                            <%if(RolAutorizacionBO.permisoNuevoElemento(user.getUser().getIdRoles())){%>
                                                            <td>
                                                                <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Agregar Producto" 
                                                                       style="float: right; width: 100px;" onclick="location.href='<%=urlTo+"?idProveedor="+idProveedor%>'"/>
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
                                            <th>Nombre</th>                                            
                                            <th>Unidad</th>
                                            <th>Precio Menudeo</th>
                                            <th>Precio Medio Mayoreo</th>
                                            <th>Precio Mayoreo</th>
                                            <th>Caducidad</th>
                                            <th>Volumen Disponible</th>                                            
                                            <th>Fecha disponibilidad</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            for (SgfensProveedorProducto item:sgfensProveedorProductosDto){
                                                try{
                                                    SgfensProductoServicio concep = new SgfensProductoServicio();
                                                    concep = new SGProductoServicioBO(user.getConn()).findSgfensProductoServiciobyId(item.getIdSgfensProductoServicio());
                                                    
                                        %>
                                        <tr>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdProveedorProducto() %></td>
                                            <td><%=concep.getNombre() %></td>                                            
                                            <td><%=item.getUnidad() %></td>
                                            <td><%=item.getPrecioMenudeo() %></td>
                                            <td><%=item.getPrecioMedioMayoreo() %></td>
                                            <td><%=item.getPrecioMayoreo() %></td>
                                            <td><%=item.getCaducidad() %></td>
                                            <td><%=item.getVolumenDisponible() %></td>                                            
                                            <td><%=item.getFechaDisponibilidad() %></td>
                                            <td>
                                                <%if(RolAutorizacionBO.permisoAccionesElemento(user.getUser().getIdRoles())){%>
                                                <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdProveedorProducto()%>&acc=1&pagina=<%=paginaActual%>&idProveedor=<%=idProveedor%>"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>
                                                &nbsp;&nbsp;
                                                <a href="#" onclick="confirmarBorrar(<%=item.getIdProveedorProducto()%>);"><img src="../../images/icon_delete.png" alt="delete" class="help" title="Borrar"/></a>
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
                                <//jsp:param name="idReport" value="<//%= ReportBO.EMBALAJE_REPORT %>" />
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