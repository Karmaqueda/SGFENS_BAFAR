<%-- 
    Document   : catConceptos_listCnDscnto
    Created on : 29/09/2014, 12:49:02 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.bo.ExistenciaAlmacenBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.Marca"%>
<%@page import="com.tsp.sct.dao.dto.Categoria"%>
<%@page import="com.tsp.sct.bo.CategoriaBO"%>
<%@page import="com.tsp.sct.bo.AlmacenBO"%>
<%@page import="com.tsp.sct.bo.MarcaBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String buscar_idmarca = request.getParameter("q_idmarca")!=null?request.getParameter("q_idmarca"):"";
    String buscar_idalmacen = request.getParameter("q_idalmacen")!=null?request.getParameter("q_idalmacen"):"";
    String buscar_idcategoria = request.getParameter("q_idcategoria")!=null?request.getParameter("q_idcategoria"):"";
    String buscar_idsubcategoria = request.getParameter("q_idsubcategoria")!=null?request.getParameter("q_idsubcategoria"):"";
    String buscar_isNivelMinimoStock = request.getParameter("q_nivel_min_stock")!=null?request.getParameter("q_nivel_min_stock"):"";
    String buscar_isMostrarSoloActivos = request.getParameter("q_mostrar_solo_activos")!=null?request.getParameter("q_mostrar_solo_activos"):"1";
    
    
    String filtroBusqueda = " AND (DESCUENTO_PORCENTAJE != 0 OR DESCUENTO_MONTO != 0) ";
    String parametrosPaginacion = "";  
        
    if (!buscar.trim().equals("")){
        
        //para buscar por nombre
        Encrypter encrypter = new Encrypter();
        String nombreBusquedaEncriptado = "";
        try {
            nombreBusquedaEncriptado = encrypter.encodeString2(buscar);
        }catch(Exception ex){}
        
        filtroBusqueda += " AND (DESCRIPCION LIKE '%" + buscar + "%' OR NUM_ARTICULOS_DISPONIBLES LIKE '%" +buscar+"%'"
                    + " OR NOMBRE LIKE '%" + nombreBusquedaEncriptado + "%' "
                    + ")";
        
        if(!parametrosPaginacion.equals(""))
                        parametrosPaginacion+="&";
        parametrosPaginacion+="q="+buscar;
        
    }
    
    if (!buscar_idmarca.trim().equals("")){
        filtroBusqueda += " AND ID_MARCA='" + buscar_idmarca +"' ";
        if(!parametrosPaginacion.equals(""))
                        parametrosPaginacion+="&";
        parametrosPaginacion+="q_idmarca="+buscar_idmarca;
        
    }
    if (!buscar_idalmacen.trim().equals("")){
        filtroBusqueda += " AND ID_ALMACEN='" + buscar_idalmacen +"' ";
        
        if(!parametrosPaginacion.equals(""))
                        parametrosPaginacion+="&";
        parametrosPaginacion+="q_idalmacen="+buscar_idalmacen;
        
    }
    if (!buscar_idcategoria.trim().equals("")){
        filtroBusqueda += " AND ID_CATEGORIA='" + buscar_idcategoria +"' ";
        
        if(!parametrosPaginacion.equals(""))
                        parametrosPaginacion+="&";
        parametrosPaginacion+="q_idcategoria="+buscar_idcategoria;
    }
    if (!buscar_idsubcategoria.trim().equals("")){
        filtroBusqueda += " AND ID_SUBCATEGORIA='" + buscar_idsubcategoria +"' ";
        
        if(!parametrosPaginacion.equals(""))
                        parametrosPaginacion+="&";
        parametrosPaginacion+="q_idsubcategoria="+buscar_idsubcategoria;
    }
    if (buscar_isNivelMinimoStock.trim().equals("1")){
        filtroBusqueda += " AND NUM_ARTICULOS_DISPONIBLES <= STOCK_MINIMO ";
        
        if(!parametrosPaginacion.equals(""))
                        parametrosPaginacion+="&";
        parametrosPaginacion+="q_nivel_min_stock="+buscar_isNivelMinimoStock;
        
    }
    if (buscar_isMostrarSoloActivos.trim().equals("1")){
        filtroBusqueda += " AND ID_ESTATUS = 1 ";
        if(!parametrosPaginacion.equals(""))
                        parametrosPaginacion+="&";
        parametrosPaginacion+="q_mostrar_solo_activos="+buscar_isMostrarSoloActivos;
    }
    
    filtroBusqueda += " AND GENERICO!=1";
    
    int idConcepto = -1;
    try{ idConcepto = Integer.parseInt(request.getParameter("idConcepto")); }catch(NumberFormatException e){}
    
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
    
     ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
     Concepto[] conceptosDto = new Concepto[0];
     try{
         limiteRegistros = conceptoBO.findConceptos(idConcepto, idEmpresa , 0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        conceptosDto = conceptoBO.findConceptos(idConcepto, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catConceptos/catConceptos_list.jsp";
    String urlTo2 = "../catConceptosOferta/catConceptos_formCnDscto.jsp";
    String paramName = "idConcepto";
    //String parametrosPaginacion="";// "idEmpresa="+idEmpresa;
    String urlToMovimiento = "../catMovimientos/catMovimientos_form.jsp";
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
            function eliminarDescuento(idConcepto){
                if (idConcepto>0){
                    $.ajax({
                        type: "POST",
                        url: "catConcepto_ajaxCnDscnto.jsp",
                        data: {mode: '2', idConcepto : idConcepto},
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
                                            location.href = "catConceptos_listCnDscnto.jsp";
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

                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                Busqueda Avanzada &dArr;
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content" style="display: none;">
                            <form action="catConceptos_listCnDscnto.jsp" id="search_form_advance" name="search_form_advance" method="post">
                                <br/>
                                
                                <p>
                                <label>Marca</label><br/>
                                <select id="q_idmarca" name="q_idmarca" class="flexselect">
                                    <option></option>
                                    <%= new MarcaBO(user.getConn()).getMarcasByIdHTMLCombo(idEmpresa, -1) %>
                                </select>
                                </p>
                                
                                <p>
                                <label>Almacen</label><br/>
                                <select id="q_idalmacen" name="q_idalmacen" class="flexselect">
                                    <option></option>
                                    <%= new AlmacenBO(user.getConn()).getAlmacenesByIdHTMLCombo(idEmpresa, -1) %>
                                </select>
                                </p>
                                
                                <p>
                                <label>Categoria</label><br/>
                                <select id="q_idcategoria" name="q_idcategoria" class="flexselect">
                                    <option></option>
                                    <%= new CategoriaBO(user.getConn()).getCategoriasByIdHTMLCombo(idEmpresa, -1,"") %>
                                </select>
                                </p>
                                
                                <p>
                                <label>Subcategoria</label><br/>
                                <select id="q_idsubcategoria" name="q_idsubcategoria" class="flexselect">
                                    <option></option>
                                    <%= new CategoriaBO(user.getConn()).getCategoriasByIdHTMLCombo(idEmpresa, -1,"") %>
                                </select>
                                </p>
                                <br/>
                                
                                <p>
                                <input id="q_nivel_min_stock" name="q_nivel_min_stock" type="checkbox" class="checkbox" value="1"> 
                                <label for="q_nivel_min_stock">Nivel mínimo de stock</label>
                                </p>
                                
                                <br/>
                                <div id="action_buttons">
                                    <p>
                                        <input type="button" id="buscar" value="Buscar" onclick="$('#search_form_advance').submit();"/>
                                    </p>
                                </div>
                                
                            </form>
                        </div>
                    </div>
                    
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_producto.png" alt="icon"/>
                                Catálogo de Productos con descuento
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td>
                                                <div id="search">
                                                <form action="catConceptos_list.jsp" id="search_form" name="search_form" method="get">
                                                        <input type="text" id="q" name="q" title="Buscar por Nombre/Descripción/Mínimo Stock" class="" style="width: 300px; float: left; "
                                                                value="<%=buscar%>"/>
                                                        <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                </form>
                                                </div>
                                            </td>
                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                            <%if(RolAutorizacionBO.permisoNuevoElemento(user.getUser().getIdRoles())){%>
                                            <td>
                                                <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Crear Nuevo" 
                                                        style="float: right; width: 100px;" onclick="location.href='<%=urlTo%>?descuentoConcepto=1'"/>
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
                                            <th>Código</th>
                                            <th>Nombre</th>
                                            <th>Marca</th>
                                            <th>Categoria</th>
                                            <th>Sub Categoria</th>
                                            <th>Stock</th>
                                            <th>Precio</th>
                                            <th>Descuento</th>                                            
                                            <th>Descripción</th> 
                                            
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%     
                                            ConceptoBO concepto = new ConceptoBO(user.getConn());
                                            double existTotal = 0;
                                            ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(user.getConn());
                                            
                                            for (Concepto item:conceptosDto){
                                                try{
                                                    
                                                    existTotal = exisAlmBO.getExistenciaGeneralByEmpresaProducto(item.getIdEmpresa(), item.getIdConcepto());
                                                    Marca marcaDto = new MarcaBO(item.getIdMarca(),user.getConn()).getMarca();
                                                    Categoria categoriaDto = new CategoriaBO(item.getIdCategoria(),user.getConn()).getCategoria();
                                                    Categoria subcategoriaDto = new CategoriaBO(item.getIdSubcategoria(),user.getConn()).getCategoria();
                                                    
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%>>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdConcepto() %></td>
                                            <td><%=item.getIdentificacion() %></td>
                                            <td><%=concepto.desencripta(item.getNombre()) %></td>
                                            <td><!--[<%=item.getIdMarca() %>]--> <%= marcaDto!=null? marcaDto.getNombre():""%></td>
                                            <td><!--[<%=item.getIdCategoria() %>]--> <%= categoriaDto!=null? categoriaDto.getNombreCategoria():""%></td>
                                            <td><!--[<%=item.getIdSubcategoria() %>]--> <%= subcategoriaDto!=null? subcategoriaDto.getNombreCategoria():""%></td>
                                            <td><%=existTotal%></td>
                                            <td><%=item.getPrecio() %></td>
                                            <td><%=(item.getDescuentoMonto()!=0)?item.getDescuentoMonto():(item.getDescuentoPorcentaje()+" %") %></td>
                                            <td><%=item.getDescripcion()%></td>    
                                            
                                            <td>
                                                <%if(RolAutorizacionBO.permisoAccionesElementoConceptoServicio(user.getUser().getIdRoles())){%>
                                                <a href="#" onclick="eliminarDescuento(<%=item.getIdConcepto()%>);"><img src="../../images/icon_delete.png" alt="eliminarDecuento" class="help" title="Eliminar Descuento" /></a>
                                                &nbsp;&nbsp; 
                                                <a href="<%=urlTo2%>?<%=paramName%>=<%=item.getIdConcepto()%>&acc=3&pagina=<%=paginaActual%>"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>
                                                &nbsp;&nbsp;                                               
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
                                <jsp:param name="idReport" value="<%= ReportBO.PRODUCTO_REPORT %>" />
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

            <script>
                $("select.flexselect").flexselect();
            </script>
    </body>
</html>
<%}%>