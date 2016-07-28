<%-- 
    Document   : catCategorias_list
    Created on : 21/11/2012, 11:21:05 AM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.Categoria"%>
<%@page import="com.tsp.sct.bo.CategoriaBO"%>
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
        filtroBusqueda = " AND (NOMBRE_CATEGORIA LIKE '%" + buscar + "%' OR DESCRIPCION_CATEGORIA LIKE '%" +buscar+"%')";
    
    int idCategoria = -1;
    try{ idCategoria = Integer.parseInt(request.getParameter("idCategoria")); }catch(NumberFormatException e){}
    
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
    
     CategoriaBO categoriaBO = new CategoriaBO(user.getConn());
     Categoria[] categoriasDto = new Categoria[0];
     try{
         limiteRegistros = categoriaBO.findCategorias(idCategoria, idEmpresa , 0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        categoriasDto = categoriaBO.findCategorias(idCategoria, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catCategorias/catCategorias_form.jsp";
    String paramName = "idCategoria";
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
            function prepareList() {                
                $('#expList').find('li:has(ul)') 
                .addClass('el_collapsed')
                .children('ul').hide();
                $('#expList').find('.expandIcon')
                .click( function(evt) {
                    if (this == evt.target) {
                        $(this).toggleClass('el_expanded');                        
                        $(this).children('ul').toggle("slow");                        
                    }
                    // Impedir la propagación de eventos
                    if (!evt) var evt = window.event;
                    evt.cancelBubble = true; // in IE
                    if (evt.stopPropagation) evt.stopPropagation(); 
                });
            }
            
            
            $(document).ready( function() {
                prepareList()
            });

            
        </script>

      
    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Catálogos</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <div class="twocolumn">
                        <div class="column_left">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_categoria.png" alt="icon"/>
                                Catálogo de Categorias
                            </span>
                            <div class="switch" style="width:100%">
                                <table width="500px" cellpadding="0" cellspacing="0" style="margin: 1% 5%;" >
                                        <tbody>
                                                <tr>
                                                    <td>

                                                        <div id="search">
                                                        <form action="catCategorias_list.jsp" id="search_form" name="search_form" method="get">
                                                                 <p>
                                                                     <input type="text" id="q" name="q" title="Buscar por Nombre / Descripción" class="" style="width: 300px; float: left; padding: 5px; "
                                                                       value="<%=buscar%>"/>

                                                                     <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                                </p>
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
                            <br>
                            <form id="form_data" name="form_data" action="" method="post">
                                <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Nombre</th>
                                            <th>Descripción</th>                                            
                                            <th>Categoría Padre</th>                                            
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            for (Categoria item:categoriasDto){
                                                try{
                                                    Categoria categoriaPadre = new CategoriaBO(item.getIdCategoriaPadre(),user.getConn()).getCategoria();
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%>>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdCategoria() %></td>
                                            <td><%=item.getNombreCategoria() %></td>
                                            <td><%=item.getDescripcionCategoria()%></td>                                            
                                            <td><%=categoriaPadre!=null?categoriaPadre.getNombreCategoria():""%></td>
                                            <td>
                                                <%if(RolAutorizacionBO.permisoAccionesElemento(user.getUser().getIdRoles())){%>
                                                <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdCategoria()%>&acc=1&pagina=<%=paginaActual%>"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>
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

                            <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <jsp:param name="idReport" value="<%= ReportBO.CATEGORIA_REPORT %>" />
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
                                    
                    <div class="column_right">
                    <div class="header">                
                        <span>
                            <img src="../../images/icon_categoria.png" alt="icon"/>
                            Árbol de Categorias
                        </span> 
                    </div>
                    
                    <br class="clear"/>
                    <div class="content">                         
                        <ul id="expList">                            
                                <%
                                    CategoriaBO catsBO = new CategoriaBO(user.getConn());
                                    
                                    System.out.println("----------->   Level 1");
                                    Categoria[] level1 = catsBO.getCategoriasByLevelList(idEmpresa , " AND id_categoria_padre = -1  OR ID_EMPRESA = 0" );
                                    for(Categoria cats : level1){
                                        out.print("<li class='expandIcon'>" + cats.getNombreCategoria() + "");
                                            out.print("<ul>");             
                                                System.out.println("----------->   Level 2");
                                                Categoria[] level2 = catsBO.getCategoriasByLevelList(idEmpresa , " AND id_categoria_padre = " + cats.getIdCategoria() );
                                                for(Categoria cats2 : level2){
                                                    out.print("<li class='expandIcon'>" + cats2.getNombreCategoria() + "");
                                                        out.print("<ul>");
                                                        System.out.println("----------->   Level 3");
                                                        Categoria[] level3 = catsBO.getCategoriasByLevelList(idEmpresa , " AND id_categoria_padre = " + cats2.getIdCategoria() );
                                                        for(Categoria cats3 : level3){
                                                            out.print("<li class='expandIcon'>" + cats3.getNombreCategoria() + "");
                                                                out.print("<ul>");
                                                                    System.out.println("----------->   Level 4");
                                                                    Categoria[] level4 = catsBO.getCategoriasByLevelList(idEmpresa , " AND id_categoria_padre = " + cats3.getIdCategoria() );
                                                                    for(Categoria cats4 : level4){
                                                                        out.print("<li class='expandIcon'>" + cats4.getNombreCategoria() + "");
                                                                            out.print("<ul>");
                                                                                System.out.println("----------->   Level 5");
                                                                                Categoria[] level5 = catsBO.getCategoriasByLevelList(idEmpresa , " AND id_categoria_padre = " + cats4.getIdCategoria() );
                                                                                for(Categoria cats5 : level5){
                                                                                    out.print("<li>" + cats5.getNombreCategoria() + "</li>");
                                                                                }                                                                                
                                                                            out.print("</ul>");
                                                                        out.print("</li>");
                                                                    }
                                                                out.print("</ul>");
                                                            out.print("</li>");
                                                        }
                                                        out.print("</ul>");
                                                    out.print("</li>");
                                                }
                                            out.print("</ul>");
                                        out.print("</li>");
                                    }
                                    
                                %>                                                                
                        </ul>         
                        
                   </div>
                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>


    </body>
</html>
<%}%>