<%-- 
    Document   : catCrCredCliente_list
    Created on : 21/07/2016, 12:55:48 PM
    Author     : ISCesar
--%>


<%@page import="com.tsp.sct.util.Converter"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.bo.CrFormularioBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.dao.dto.CrCredCliente"%>
<%@page import="com.tsp.sct.bo.CrCredClienteBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String buscar_isMostrarSoloActivos = request.getParameter("inactivos")!=null?request.getParameter("inactivos"):"0";
    
    String filtroBusqueda = "";
    if (!buscar.trim().equals(""))
        filtroBusqueda += " AND (id_cliente_s_tercero LIKE '%" + buscar + "%' OR id_credito_s_tercero LIKE '%" +buscar+"%')";
    
    if (buscar_isMostrarSoloActivos.trim().equals("1")){
        filtroBusqueda += " AND ID_ESTATUS != 1 ";
    }else{
        filtroBusqueda += " AND ID_ESTATUS = 1 ";
    }
    
    int idCrCredCliente = -1;
    try{ idCrCredCliente = Integer.parseInt(request.getParameter("idCrCredCliente")); }catch(NumberFormatException e){}
    
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
    
     CrCredClienteBO crCredClienteBO = new CrCredClienteBO(user.getConn());
     CrCredCliente[] crCredClienteDto = new CrCredCliente[0];
     try{
         limiteRegistros = crCredClienteBO.findCantidadCrCredClientes(idCrCredCliente, idEmpresa , 0, 0, filtroBusqueda);
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        crCredClienteDto = crCredClienteBO.findCrCredClientes(idCrCredCliente, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catCrCredCliente/catCrCredCliente_form.jsp";
    String paramName = "idCrCredCliente";
    String parametrosPaginacion="idCrCredCliente="+idCrCredCliente+"&q="+buscar+"&inactivos="+buscar_isMostrarSoloActivos;
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
            
            $(function(){ 
                $('.modalbox_iframe_detalle').fancybox({
                    padding: 0, 
                    titleShow: false, 
                    overlayColor: '#333333', 
                    overlayOpacity: .5,
                    type: 'iframe',
                    autoScale:   true,
                    width: 1000,
                    height: 600
                });
            });
            
            /**
            * Para iniciar el FancyBox en el Parent (esta pagina debe ir dentro de un iframe)
            * @param {type} url Link que se abrira en el FancyBox
            * @returns {undefined}
            */
            function modalboxOnParent(url){
                parent.$.fancybox({
                    'padding'       : 0, 
                    'titleShow'     : false, 
                    'overlayColor'  : '#333333', 
                    'overlayOpacity': .5,
                    'type'          : 'iframe',
                    'autoScale'     : true,
                    'href'          : url
                });
            }
            
            function inactiv(){               
                if($("#inactivos").attr("checked")){
                    $("#inactivos").val("1");
                }else{
                     $("#inactivos").val("0");
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
                    <h1>Cobranza</h1>
                    
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
                            <form action="catCrCredCliente_list.jsp" id="search_form_advance" name="search_form_advance" method="post">                                  
                                <p>
                                    <input type="checkbox" class="checkbox" id="inactivos" name="inactivos" value="1"  onchange="inactiv();" > <label for="inactivos">Mostrar Inactivos</label>                                   
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
                                <img src="../../images/icon_crCredCliente.png" alt="icon"/>
                                Catálogo de Clientes Crédito
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td>
                                                <div id="search">
                                                <form action="catCrCredCliente_list.jsp" id="search_form" name="search_form" method="get">
                                                    <input type="text" id="q" name="q" title="Buscar por Nombre" class="" style="width: 300px; float: left; "
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
                                            <th>BP</th>
                                            <th>Contrato</th>
                                            <th>RFC</th>
                                            <th>Nombre</th>                                            
                                            <th>Prestámo</th>
                                            <th>Saldo</th>
                                            <th>Vencimiento Pago</th>
                                            <th>Vencimiento Fecha</th>
                                            <th>Estatus</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            for (CrCredCliente item : crCredClienteDto){
                                                try{
                                                    crCredClienteBO.setCrCredCliente(item);
                                                    String nombre = crCredClienteBO.getNombreCompleto(true);
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%>>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%= item.getIdCredCliente() %></td>
                                            <td><%= StringManage.getValidString(item.getIdClienteSTercero()) %></td>
                                            <td><%= StringManage.getValidString(item.getIdCreditoSTercero()) %></td>
                                            <td><%= StringManage.getValidString(item.getRfc()) %></td>
                                            <td><%= nombre %></td>
                                            <td><%= Converter.doubleToStringFormatMexico(item.getMontoPrestado()) %></td>
                                            <td><%= Converter.doubleToStringFormatMexico(item.getMontoAdeudado()) %></td>
                                            <td><%= Converter.doubleToStringFormatMexico(item.getImportePagarVencimiento()) %></td>
                                            <td><%= StringManage.getValidString(DateManage.formatDateToNormal(item.getFechaHoraAgenda())) %></td>
                                            <td><%= (item.getIdEstatus()==1)?"Activo":"Inactivo" %></td>
                                            <td>
                                                <%if(RolAutorizacionBO.permisoAccionesElemento(user.getUser().getIdRoles())){%>
                                                <!--<a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdCredCliente()%>&acc=1&pagina=<%=paginaActual%>"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>-->
                                                &nbsp;&nbsp;
                                                <%}%>
                                                <a href="catCrCredCliente_formMapa.jsp?acc=3&<%=paramName%>=<%=item.getIdCredCliente()%>" id="accion_ubicación" title="Ubicación" 
                                                    class="modalbox_iframe_detalle">
                                                    <img src="../../images/icon_mapa.png" alt="Ver Ubicación" class="help" title="Ver Ubicación"/>
                                                </a>
                                                &nbsp;&nbsp;
                                                <!-- Log Notas/Comentarios -->
                                                <a href="#" onclick="modalboxOnParent('../../jsp/notas/notas_list.jsp?<%=paramName%>=<%=item.getIdCredCliente()%>');" title="Log Notas">
                                                    <img src="../../images/icon_notas.png" alt="Log Notas" class="help" title="Log Notas"/>
                                                </a>
                                                &nbsp;&nbsp;
                                                <!--<a href=""><img src="images/icon_delete.png" alt="delete" class="help" title="Delete"/></a>-->
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
                            <!--
                            < jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                < jsp:param name="idReport" value="<%= ReportBO.CR_FORMULARIO_VALIDACIONES_REPORT %>" />
                                < jsp:param name="parametrosCustom" value="<%= filtroBusquedaEncoded %>" />
                            < /jsp:include>
                            -->
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