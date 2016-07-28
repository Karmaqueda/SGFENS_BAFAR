<%-- 
    Document   : catProveedors_list.jsp
    Created on : 256-oct-2012, 12:13:49
    Author     : ISCesarMartinez poseidon24@hotmail.com
--%>


<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProveedorCategoria"%>
<%@page import="com.tsp.sct.bo.SGProveedorCategoriaBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProveedor"%>
<%@page import="com.tsp.sct.bo.SGProveedorBO"%>
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
        filtroBusqueda = " AND (RFC LIKE '%" + buscar + "%' OR RAZON_SOCIAL LIKE '%" + buscar +"%' OR CONTACTO LIKE '%"+buscar+"%' OR NUMERO_PROVEEDOR LIKE '%" + buscar +"%')";
    
    int idProveedor = -1;
    try{ idProveedor = Integer.parseInt(request.getParameter("idProveedor")); }catch(NumberFormatException e){}
    
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
    
     SGProveedorBO proveedorBO = new SGProveedorBO(user.getConn());
     SgfensProveedor[] proveedorsDto = new SgfensProveedor[0];
     try{
         limiteRegistros = proveedorBO.findProveedor(idProveedor, idEmpresa , 0, 0, filtroBusqueda).length;
         
          if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        proveedorsDto = proveedorBO.findProveedor(idProveedor, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catProveedores/catProveedores_form.jsp";
    String urlTo2 = "../catProveedores/catProveedorProductos_list.jsp";
    String urlTo3 = "../catProveedores/catProveedores_formMapa.jsp";
    String urlTo4 = "../catCodigoBarras/catCodigoBarras_form.jsp";
    String paramName = "idProveedor";
    String parametrosPaginacion="";// "idEmpresa="+idEmpresa;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    
    EmpresaBO empresaBO = new EmpresaBO(user.getConn());
    EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa());     
    
    
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
            
            $(document).ready(function(){
                asignarFuncionCheckboxGeneral();
            });
            
            /**
            * Asigna una funcion para el checkbox maestro que hara
            * que un conjunto de checkbox se active o desactive
            */
            function asignarFuncionCheckboxGeneral(){
                //Checkbox
                $("input[name=check_master_id]").change(function(){
                    $('input[name=check_id]').each( function() {
                        if($("input[name=check_master_id]:checked").length === 1){
                            this.checked = true;
                        } else {
                            this.checked = false;
                        }
                    });
                });
            }
            
            /**
            * Funcion para invocar una acción para varios registros a la ves
            * Recorre todos los checkbox con nombre: check_id
            * y toma su valor para enviarlos como parametros GET separados por coma
            * a la JSP correspondiente
            */
            function muestraMultiplesCodigosBarra(){
                //alert("Se descarga zip");

                var query_string = '../../jsp/catCodigoBarras/catCodigoBarras_form.jsp?tipoCodigo=bar&idsProveedores=';

                var idString ="";

                $("input[name=check_id]").each(function(){
                    if(this.checked){
                        idString += this.value + ",";
                    }
                });
                if (idString!==""){
                    query_string += idString;
                    //alert("cadena: "+query_string);

                    //window.open(query_string, "Múltiples Códigos de Barra", "location=0,status=0,scrollbars=0,menubar=0,resizable=0,width=800,height=600");
                    window.open(query_string);
                    
                }else{
                    apprise('No se selecciono ningun registro para generar su Codigo de Barras',{
                        'warning':true,
                        'animate':true
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
                                <img src="../../images/icon_proveedor.png" alt="icon"/>
                                Catálogo de Proveedor
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                            <tr>
                                                <td>
                                                    <div id="search">
                                                    <form action="catProveedores_list.jsp" id="search_form" name="search_form" method="get">
                                                            <input type="text" id="q" name="q" title="Buscar por RFC/ Razon Social/ Contacto / No. Proveedor" class="" style="width: 300px; float: left; "
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
                                            <th><input type="checkbox" name="check_master_id" value="" /></th>
                                            <th>ID</th>
                                            <th>RFC<%=empresaPermisoAplicacionDto.getRfcPorNipCodigo() == 1?"/NIP":empresaPermisoAplicacionDto.getRfcPorNipCodigo() == 2?"/RUC":""%></th>
                                            <th>Razón Social</th>
                                            <th>Contacto</th>
                                            <th>Numero Proveedor</th>
                                            <th>Categoría</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            for (SgfensProveedor item:proveedorsDto){
                                                String categoria = "";
                                                try{
                                                    try{
                                                    if(item.getIdCategoriaProveedor() > 0){
                                                        SgfensProveedorCategoria proveedorCategoria = new SGProveedorCategoriaBO(item.getIdCategoriaProveedor(), user.getConn()).getSgfensProveedorCategoria();
                                                        categoria = proveedorCategoria.getNombre();
                                                    }}catch(Exception e){}
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%>>
                                            <td><input type="checkbox" name="check_id" id="check_id" value="<%= item.getIdProveedor() %>"/></td>
                                            <td><%=item.getIdProveedor() %></td>
                                            <td><%=item.getRfc() %></td>
                                            <td><%=item.getRazonSocial() %></td>
                                            <td><%=item.getContacto() %></td>
                                            <td><%=item.getNumeroProveedor() %></td>
                                            <td><%=categoria %></td>
                                            <td>
                                                <%if(RolAutorizacionBO.permisoAccionesElemento(user.getUser().getIdRoles())){%>
                                                <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdProveedor()%>&acc=1&pagina=<%=paginaActual%>"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>
                                                &nbsp;&nbsp;
                                                <a href="<%=urlTo2%>?<%=paramName%>=<%=item.getIdProveedor()%>"><img src="../../images/icon_ventas3.png" alt="productos" class="help" title="Productos/Servicios"/></a>
                                                &nbsp;&nbsp;
                                                <!--<a href=""><img src="images/icon_delete.png" alt="delete" class="help" title="Delete"/></a>-->
                                                <%}%>
                                                <a href="<%=urlTo3%>?<%=paramName%>=<%=item.getIdProveedor()%>&acc=Mapa"><img src="../../images/icon_movimiento.png" alt="localización" class="help" title="Localización"/></a>
                                                &nbsp;&nbsp;
                                                <a href="<%=urlTo4%>?tipoCodigo=bar&<%=paramName%>=<%=item.getIdProveedor()%>&acc=CodigoBarras" target="_blank"><img src="../../images/icon_barras.png" alt="codigoBarras" class="help" title="Código Barras"/></a>
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
                                    
                                    <br><br>
                                    <tbody>
                                        <tr>
                                        <td>
                                            <center>

                                            <a href="../../jsp/catProveedores/catCategoriaProveedor_list.jsp" id="idCatalogosLeftContentCategoriaProveedor" title="Categorías">
                                                <img src="../../images/icon_categoria.png" alt="Categoría Proveedores" title="Categoría Proveedores" class="help"/>
                                                Categoría de Proveedores
                                            </a>
                                            
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            <a href="../../jsp/catProveedores/catProductosCompra_list.jsp" id="idCatalogosLeftContentProdcutosCompra" title="Productos">
                                                <img src="../../images/icon_producto.png" alt="Productos de Compra" title="Productos de Compra" class="help"/>
                                                Productos de Compra
                                            </a>
                                            
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            <a href="#" onclick="muestraMultiplesCodigosBarra()" id="idCodigoBarraMultiple" title="Codigos de Barra">
                                                <img src="../../images/icon_barras.png" alt="Codigos de Barra" title="Códigos de Barra de Registros Seleccionados" class="help" />
                                                Códigos de Barra
                                            </a>
                                            
                                            </center>
                                        </td>
                                        </tr>
                                    </tbody>
                            <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <jsp:param name="idReport" value="<%= ReportBO.PROVEEDOR_REPORT %>" />
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


    </body>
</html>
<%}%>