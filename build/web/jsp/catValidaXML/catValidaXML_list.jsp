<%-- 
    Document   : catValidaXML_list
    Created on : 14/03/2014, 05:43:46 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.dao.dto.ValidacionXml"%>
<%@page import="com.tsp.sct.bo.ValidacionXmlBO"%>
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
        filtroBusqueda = filtroBusqueda + " AND (TIMBRE_FISCAL LIKE '%" + buscar + "%')";
    
    int idValidacion = -1;
    try{ idValidacion = Integer.parseInt(request.getParameter("idValidacion")); }catch(NumberFormatException e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    ///**
    Empresa empresaPadre = new Empresa();
    EmpresaBO eBO = new EmpresaBO(user.getConn());
    empresaPadre = eBO.getEmpresaMatriz(idEmpresa);//extraemmos la empresa padre para ver si tiene creditos para validar XML
    EmpresaPermisoAplicacion ePermisoApp = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaPadre.getIdEmpresa());
    ///**
    
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
    
     ValidacionXmlBO validacionXmlBO = new ValidacionXmlBO(user.getConn());
     ValidacionXml[] validacionXml = new ValidacionXml[0];
     try{
         limiteRegistros = validacionXmlBO.findValidacionXmls(idValidacion, idEmpresa , 0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        validacionXml = validacionXmlBO.findValidacionXmls(idValidacion, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     String labelCreditosDisponibles = "Créditos disponibles de ";
     if (ePermisoApp!=null){
         if (ePermisoApp.getTipoConsumoServicio()==1){
             //On Demand - Por créditos de operación
             labelCreditosDisponibles += "Operación : " + empresaPadre.getCreditosOperacion();
         }else{
             //Normal - post-pago
            labelCreditosDisponibles += "Validación : " + empresaPadre.getCreditoValidaXml();
         }
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catValidaXML/catValidaXML_form.jsp";
    String paramName = "idValidacion";
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
            
        </script>

    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Validador</h1>
                    <div style="font-size: 14px;font-weight: bold;"><tr><td><img src="../../images/icon_campanita.png" alt="icon"/><%= labelCreditosDisponibles %></td></tr></div>
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_info.png" alt="icon"/>
                                Validaciones
                            </span>
                            <div class="switch" style="width:410px">
                                <table width="300px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td>
                                                <div id="search">
                                                <form action="catValidaXML_list.jsp" id="search_form" name="search_form" method="get">
                                                        <input type="text" id="q" name="q" title="Buscar por UUID" class="" style="width: 300px; float: left; "
                                                               value="<%=buscar%>"/>
                                                </form>
                                                </div>
                                            </td>
                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                            <td>
                                                <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Validar Nuevo" 
                                                       style="float: right; width: 100px;" onclick="location.href='<%=urlTo%>'"/>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <form id="form_data" name="form_data" action="" method="post">
                                <table class="data" width="100%" cellpadding="0" cellspacing="0"
                                       style="table-layout: fixed; width: 100%">
                                    <thead>
                                        <tr>               
                                            <th width="5%">ID</th>
                                            <th width="6%">Versión</th>
                                            <th width="8%">Comprobante Valido?</th>
                                            <th width="6%">Sello Emisor Valido?</th>
                                            <th width="30%">Cadena Original</th>
                                            <th width="30%">Timbre Fiscal</th>
                                            <th width="15%">Errores</th>                                            
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            for (ValidacionXml item:validacionXml){
                                                try{
                                        %>
                                        <tr>
                                            <!--<td><input type="checkbox"/></td>-->                                           
                                            <td><%=item.getIdValidacion() %></td>
                                            <td><%=item.getVersionComprobante() %></td>
                                            <%if(item.getComprobanteValido()==1){%>
                                                <td><img src="../../images/icon_accept.png" alt="icon"/></td>
                                            <%}else if(item.getComprobanteValido()==0){%>
                                                <td><img src="../../images/icon_error.png" alt="icon"/></td>
                                            <%}%>
                                            
                                            <%if(item.getSelloEmisorValido()==1){%>
                                                <td><img src="../../images/icon_accept.png" alt="icon"/></td>
                                            <%}else if(item.getSelloEmisorValido()==0){%>
                                                <td><img src="../../images/icon_error.png" alt="icon"/></td>
                                            <%}%>
                                                                                        
                                            <td style="word-wrap: break-word;">
                                                <textarea style="width: 90%" rows="5"><%=item.getCadenaOriginal() %></textarea>
                                            </td>                                            
                                            <td style="word-wrap: break-word;">
                                                <textarea style="width: 90%" rows="5"><%=(item.getTimbreFiscal()!=null?(item.getTimbreFiscal().replaceAll("\"", "&#34;").replaceAll("<", "&lt;").replaceAll("\\>", "&gt;")):"") %></textarea>
                                            </td>
                                            <td style="word-wrap: break-word;">
                                                <textarea style="width: 90%" rows="5"><%=item.getMensajesError() %></textarea>
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
                                <//jsp:param name="parametrosCustom" value="<%= filtroBusquedaEncoded %>" />
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