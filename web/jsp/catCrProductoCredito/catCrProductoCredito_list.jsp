<%-- 
    Document   : catCrProductoCredito_list
    Created on : 23/06/2016, 12:55:48 PM
    Author     : ISCesar
--%>


<%@page import="com.tsp.sct.dao.dto.CrDocImprimible"%>
<%@page import="com.tsp.sct.dao.dto.CrScore"%>
<%@page import="com.tsp.sct.bo.CrScoreBO"%>
<%@page import="com.tsp.sct.bo.CrGrupoFormularioBO"%>
<%@page import="com.tsp.sct.dao.dto.CrGrupoFormulario"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.dao.dto.CrProductoCredito"%>
<%@page import="com.tsp.sct.bo.CrProductoCreditoBO"%>
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
        filtroBusqueda += " AND (nombre LIKE '%" + buscar + "%' OR descripcion LIKE '%" +buscar+"%')";
    
    if (buscar_isMostrarSoloActivos.trim().equals("1")){
        filtroBusqueda += " AND ID_ESTATUS != 1 ";
    }else{
        filtroBusqueda += " AND ID_ESTATUS = 1 ";
    }
    
    int idCrProductoCredito = -1;
    try{ idCrProductoCredito = Integer.parseInt(request.getParameter("idCrProductoCredito")); }catch(NumberFormatException e){}
    
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
    
     CrProductoCreditoBO crProductoCreditoBO = new CrProductoCreditoBO(user.getConn());
     crProductoCreditoBO.setOrderBy(" ORDER BY id_producto_credito_padre ASC, nombre ASC ");
     CrProductoCredito[] crProductoCreditoDto = new CrProductoCredito[0];
     try{
         limiteRegistros = crProductoCreditoBO.findCantidadCrProductoCreditos(idCrProductoCredito, idEmpresa , 0, 0, filtroBusqueda);
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        crProductoCreditoDto = crProductoCreditoBO.findCrProductoCreditos(idCrProductoCredito, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catCrProductoCredito/catCrProductoCredito_form.jsp";
    String paramName = "idCrProductoCredito";
    String parametrosPaginacion="q="+buscar+"&inactivos="+buscar_isMostrarSoloActivos;
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

        <jsp:include page="../include/jsFunctions2.jsp"/>
        
        <script type="text/javascript">
            
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
                    <h1>Producto Crédito</h1>
                    
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
                            <form action="catCrProductoCredito_list.jsp" id="search_form_advance" name="search_form_advance" method="post">                                  
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
                                <img src="../../images/icon_crProductoCredito.png" alt="icon"/>
                                Catálogo de Producto Crédito
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td>
                                                <div id="search">
                                                <form action="catCrProductoCredito_list.jsp" id="search_form" name="search_form" method="get">
                                                    <input type="text" id="q" name="q" title="Buscar por Nombre/Descripción" class="" style="width: 300px; float: left; "
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
                                            <th>Padre</th>
                                            <th>Nombre</th> 
                                            <th>Formularios</th>
                                            <th>Score</th>
                                            <th>Imprimibles</th>
                                            <th>Creado</th>
                                            <th>Editado</th>
                                            <th>Estatus</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
                                            CrGrupoFormularioBO crGrupoFormularioBO = new CrGrupoFormularioBO(user.getConn());
                                            CrScoreBO crScoreBO = new CrScoreBO(user.getConn());
                                            for (CrProductoCredito item : crProductoCreditoDto){
                                                try{
                                                    String nombreEmpleadoEdito = " - ";
                                                    String nombreProductoPadre = " - ";
                                                    String formularioSolicitud = " - ";
                                                    String formularioVerificación = " - ";
                                                    String score = " ";
                                                    String nombresImprimibles = " ";
                                                    CrProductoCredito itemPadre = null;
                                                    try{
                                                        Empleado empleadoDto = empleadoBO.findEmpleadoByUsuario(item.getIdUsuarioEdicion());
                                                        nombreEmpleadoEdito = StringManage.getValidString(empleadoDto.getNombre()) + " " + StringManage.getValidString(empleadoDto.getApellidoPaterno());
                                                    }catch(Exception ex){}
                                                    if (item.getIdProductoCreditoPadre()>0){
                                                        // sub-producto
                                                        try{
                                                            CrProductoCredito productoCreditoPadre = crProductoCreditoBO.findCrProductoCreditobyId(item.getIdProductoCreditoPadre());
                                                            itemPadre = productoCreditoPadre;
                                                            nombreProductoPadre = productoCreditoPadre.getNombre();
                                                        }catch(Exception ex){}
                                                    }else{
                                                        itemPadre = item; //el mismo producto es Padre (no es un sub-producto)
                                                    }
                                                    if (itemPadre!=null){
                                                        if (itemPadre.getIdGrupoFormularioSolic()>0){
                                                            try{
                                                                CrGrupoFormulario grupoFormulario = crGrupoFormularioBO.findCrGrupoFormulariobyId(itemPadre.getIdGrupoFormularioSolic());
                                                                formularioSolicitud = grupoFormulario.getNombre();
                                                            }catch(Exception ex){}
                                                        }
                                                        if (itemPadre.getIdGrupoFormularioVerif()>0){
                                                            try{
                                                                CrGrupoFormulario grupoFormulario = crGrupoFormularioBO.findCrGrupoFormulariobyId(itemPadre.getIdGrupoFormularioVerif());
                                                                formularioVerificación = grupoFormulario.getNombre();
                                                            }catch(Exception ex){}
                                                        }
                                                        if (itemPadre.getIdScore()>0){
                                                            try{
                                                                CrScore crScore = crScoreBO.findCrScorebyId(itemPadre.getIdScore());
                                                                score = crScore.getNombre();
                                                            }catch(Exception ex){}
                                                        }
                                                        crProductoCreditoBO.setCrProductoCredito(itemPadre);
                                                        CrDocImprimible[] docImprimibles = crProductoCreditoBO.consultaImprimiblesAsociados();
                                                        if (docImprimibles.length>0){
                                                            for (CrDocImprimible docImprimible : docImprimibles){
                                                                nombresImprimibles += "<ul>"+docImprimible.getNombre();
                                                            }
                                                        }
                                                    }
                                                    
                                                    
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%>>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%= item.getIdProductoCredito() %></td>
                                            <td><b><%= nombreProductoPadre %></b></td>
                                            <td><%= StringManage.getValidString(item.getNombre()) %></td>
                                            <td>
                                                <% if (itemPadre!=null){ %>
                                                Solicitud: <i><%= formularioSolicitud %></i>
                                                <br/>
                                                Verificación: <i><%= formularioVerificación %></i>
                                                <% } %>
                                            </td>
                                            <td><%= score %></td>
                                            <td><%= nombresImprimibles %></td>
                                            <td><%= DateManage.formatDateTimeToNormalMinutes(item.getFechaHrCreacion()) %></td>
                                            <td><%= StringManage.getValidString( DateManage.formatDateTimeToNormalMinutes(item.getFechaHrUltimaEdicion()) ) %><br/><i><%= nombreEmpleadoEdito %></i></td>
                                            <td><%= (item.getIdEstatus()==1)?"Activo":"Inactivo" %></td>
                                            <td>
                                                <%if(RolAutorizacionBO.permisoAccionesElemento(user.getUser().getIdRoles())){%>
                                                <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdProductoCredito()%>&acc=1&pagina=<%=paginaActual%>"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>
                                                &nbsp;&nbsp;
                                                <%}%>
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
                            <jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <jsp:param name="idReport" value="<%= ReportBO.CR_PRODUCTO_CREDITO_REPORT %>" />
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