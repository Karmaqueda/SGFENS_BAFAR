<%-- 
    Document   : catComodatoProductosProductos_list
    Created on : 8/03/2016, 05:15:55 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.dao.dto.Comodato"%>
<%@page import="com.tsp.sct.bo.ComodatoBO"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.tsp.sct.dao.jdbc.ConceptoDaoImpl"%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.ComodatoProducto"%>
<%@page import="com.tsp.sct.bo.ComodatoProductoBO"%>
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
        filtroBusqueda = " AND (NOMBRE LIKE '%" + buscar + "%' OR DESCRIPCION LIKE '%" +buscar+"%')";
    
    int idComodatoProducto = -1;
    try{ idComodatoProducto = Integer.parseInt(request.getParameter("idComodatoProducto")); }catch(NumberFormatException e){}
    
    int idComodato = -1;
    try{ idComodato = Integer.parseInt(request.getParameter("idComodato")); }catch(NumberFormatException e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Paginación
    */
    int paginaActual = 1;
    double registrosPagina = 20;
    double limiteRegistros = 0;
    int paginasTotales = 0;
    int numeroPaginasAMostrar = 5;

    try{
        paginaActual = Integer.parseInt(request.getParameter("pagina"));
    }catch(Exception e){}

    try{
        registrosPagina = Integer.parseInt(request.getParameter("registros_pagina"));
    }catch(Exception e){}
    
     ComodatoProductoBO comodatoProductoBO = new ComodatoProductoBO(user.getConn());
     ComodatoProducto[] comodatoProductosDto = new ComodatoProducto[0];
     try{
         limiteRegistros = comodatoProductoBO.findComodatoProductos(idComodatoProducto, idComodato, 0, 0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        comodatoProductosDto = comodatoProductoBO.findComodatoProductos(idComodatoProducto, idComodato, 0,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
     
    ComodatoBO comodatoBO = new ComodatoBO(user.getConn());
    Comodato comodatosDto = null;
    if (idComodato > 0){
        comodatoBO = new ComodatoBO(idComodato,user.getConn());
        comodatosDto = comodatoBO.getComodato();
    }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catComodatos/catComodatosProductos_form.jsp";    
    String paramName = "idComodatoProducto";
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
            
            function confirmarEliminarComodatoProducto(idComodatoProducto, idComodato){
                apprise('¿Esta seguro que desea eliminar el Producto?', {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, 
                    function(r){
                        if(r){
                            // Usuario dio click 'Yes'
                            confirmarEliminarProducto(idComodatoProducto, idComodato);
                        }
                });
                
            }
            
            function confirmarEliminarProducto(idComodatoProducto, idComodato){
                if(idComodatoProducto>=0){
                    $.ajax({
                        type: "POST",
                        url: "catComodatos_ajax.jsp",
                        data: { mode: 'eliminarComodatoProducto', idComodatoProducto : idComodatoProducto, idComodato : idComodato },
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
                                        location.href = "catComodatosProductos_list.jsp?idComodato="+idComodato+"&acc=producto";
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
                    <h1>Comodato Producto</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_comodatoProducto.png" alt="icon"/>
                                Comodato, Productos <%=comodatosDto!=null?"del Equipo " + comodatosDto.getNombre():""%>
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
                                                            <td>
                                                                <div id="search">
                                                                <form action="catComodatoProductos_list.jsp" id="search_form" name="search_form" method="get">
                                                                        <input type="text" id="q" name="q" title="Buscar por Nombre / Descripción" class="" style="width: 300px; float: left; "
                                                                               value="<%=buscar%>"/>
                                                                        <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                                </form>
                                                                </div>
                                                            </td>
                                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                                            
                                                            <td>
                                                                <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Agregar Nuevo" 
                                                                       style="float: right; width: 100px;" onclick="location.href='<%=urlTo%>?idComodato=<%=idComodato%>&acc=<%=mode%>'"/>
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
                                            <th>Producto</th>                                                                                  
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            
                                            Concepto concepto = null;
                                            ConceptoDaoImpl conceptoDaoImpl = new ConceptoDaoImpl(user.getConn());
                                            String nombreConcepto = "";
                                            ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
                                            for (ComodatoProducto item:comodatoProductosDto){
                                                try{
                                                    nombreConcepto = "";
                                                    concepto = null;
                                                    concepto = conceptoDaoImpl.findByPrimaryKey(item.getIdConcepto());
                                                    nombreConcepto = conceptoBO.desencripta(concepto.getNombre());
                                                }catch(Exception e){}
                                                try{
                                        %>
                                        <tr>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdComodatoProducto() %></td>
                                            <td><%=nombreConcepto %></td>                                            
                                            <td>
                                                <%if(RolAutorizacionBO.permisoAccionesElemento(user.getUser().getIdRoles())){%>
                                                <!--<a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdComodatoProducto()%>&acc=1&pagina=<%=paginaActual%>"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>-->
                                                &nbsp;&nbsp;                                                
                                                <a href="#" onclick="confirmarEliminarComodatoProducto(<%=item.getIdComodatoProducto()%>, <%=item.getIdComodato()%>);"><img src="../../images/icon_delete.png" alt="delete" class="help" title="Delete"/></a> 
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
                            
                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                            
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