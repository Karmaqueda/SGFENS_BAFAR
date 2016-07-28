<%-- 
    Document   : catCrDocImprimible_byFrmEvento_list
    Created on : 5/07/2016, 12:42:46 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.bo.CrEstadoSolicitudBO"%>
<%@page import="com.tsp.sct.util.GenericMethods"%>
<%@page import="com.tsp.sct.bo.CrProductoCreditoBO"%>
<%@page import="com.tsp.sct.dao.dto.CrProductoCredito"%>
<%@page import="com.tsp.sct.bo.CrFormularioEventoBO"%>
<%@page import="com.tsp.sct.dao.dto.CrFrmEventoSolicitud"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.dao.dto.CrDocImprimible"%>
<%@page import="com.tsp.sct.bo.CrDocImprimibleBO"%>
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
        //filtroBusqueda += " AND (nombre LIKE '%" + buscar + "%' OR descripcion LIKE '%" +buscar+"%')";
    
    int idCrFormularioEvento = -1;
    try{ idCrFormularioEvento = Integer.parseInt(request.getParameter("idCrFormularioEvento")); }catch(NumberFormatException e){}
    
    CrFormularioEventoBO crFormularioEventoBO = new CrFormularioEventoBO(user.getConn());
    if (idCrFormularioEvento>0){
        CrFrmEventoSolicitud crFrmEventoSolicitud = crFormularioEventoBO.getFrmEventoSolicitud(idCrFormularioEvento);
        if (crFrmEventoSolicitud!=null){
            CrProductoCredito crProductoCredito = new CrProductoCreditoBO(crFrmEventoSolicitud.getIdProductoCredito(), user.getConn()).getCrProductoCredito();
            filtroBusqueda = " AND id_doc_imprimible IN (SELECT id_doc_imprimible FROM cr_producto_x_imprimible WHERE id_producto_credito = " + crProductoCredito.getIdProductoCreditoPadre()+ " )";
        
            if (!GenericMethods.datoEnColeccion(crFrmEventoSolicitud.getIdEstadoSolicitud(), new Integer[]{CrEstadoSolicitudBO.S_DISPERSADA} )){
            // Si la solicitud NO esta en el estatus  8 (Dispersado) no mostramos documentos de ORDEN DE PAGO
                filtroBusqueda += " AND tipo_imprimible<>'" + CrDocImprimibleBO.TIPO_ORDEN_PAGO + "' ";
            }
        }else{
            filtroBusqueda = " AND id_doc_imprimible < 0 "; //no mostrar nada si no hay un registro cr_frm_evento_solicitud asociado al Evento
        }
    }
    
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
    
     CrDocImprimibleBO crDocImprimibleBO = new CrDocImprimibleBO(user.getConn());
     CrDocImprimible[] crDocImprimibleDto = new CrDocImprimible[0];
     try{
         limiteRegistros = crDocImprimibleBO.findCantidadCrDocImprimibles(-1, idEmpresa , 0, 0, filtroBusqueda);
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        crDocImprimibleDto = crDocImprimibleBO.findCrDocImprimibles(-1, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catCrDocImprimible/catCrDocImprimible_form.jsp";
    String paramName = "idCrDocImprimible";
    String paramName2 = "idCrFormularioEvento";
    String parametrosPaginacion="q="+buscar+"&idCrFormularioEvento="+idCrFormularioEvento;
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
            
            function inicializarPopup(){
                $('#left_menu').hide();
                $('body').addClass('nobg');
		$('#content').css('marginLeft', 30);
            }
            
        </script>

    </head>
    <body class="nobg">
        <!--<div class="content_wrapper">-->

            <div id="left_menu">
            </div>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Solicitud</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>
                    
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_crDocImprimible.png" alt="icon"/>
                                Documentos Imprimibles de Solicitud
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td>
                                                
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
                                            <th>Tipo</th>
                                            <th>Nombre</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            for (CrDocImprimible item : crDocImprimibleDto){
                                                try{
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%>>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%= StringManage.getValidString(item.getTipoImprimible()) %></td>
                                            <td><%= StringManage.getValidString(item.getNombre()) %></td>
                                            <td>
                                                <a href="../../jsp/catCrDocImprimible/previewPDFImprimible.jsp?<%=paramName %>=<%=item.getIdDocImprimible()%>&<%=paramName2%>=<%= idCrFormularioEvento %>" title="PDF"
                                                    class="modalbox_iframe">
                                                    <img src="../../images/icon_consultar.png" alt="PDF" class="help" title="PDF"/>
                                                </a>
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
        
        <!--</div>-->

        <script>
            inicializarPopup();
            $("select.flexselect").flexselect();
        </script>
    </body>
</html>
<%}%>