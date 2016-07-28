<%-- 
    Document   : catLicencias_list
    Created on : 04/01/2013, 10:10:43 AM
    Author     : Leonardo
--%>



<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.dao.dto.Usuarios"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.dao.dto.DispositivoMovil"%>
<%@page import="com.tsp.sct.bo.DispositivoMovilBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
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
    
    int idLicencia = -1;
    try{ idLicencia = Integer.parseInt(request.getParameter("idLicencia")); }catch(NumberFormatException e){}
    
    long idEmpresa = user.getUser().getIdEmpresa();
    
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
    
   
    EmpresaBO empresaBO = new EmpresaBO(user.getConn());
    Empresa empresa = empresaBO.findEmpresabyId(user.getUser().getIdEmpresa());    
     
    
    EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa());   
    
    
    
    DispositivoMovilBO ubo = new DispositivoMovilBO(user.getConn());
    DispositivoMovil[] lista = new DispositivoMovil[0];
    lista = ubo.findDispositivosMoviles(0, empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa(), 0, 0, "");

    UsuariosBO usuariosBO = new UsuariosBO();
    Usuarios[] listaUsuarios = new Usuarios[0];
    listaUsuarios = usuariosBO.findUsuarios(-1, empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa(), 0, 0, " AND ID_ESTATUS <> 2 ");    
    

    int licenciasUsadas = 0;
    //licenciasUsadas = lista.length + listaUsuarios.length;
    licenciasUsadas = listaUsuarios.length;
     
     
     try{
         limiteRegistros = 2;  
         
         if (!buscar.trim().equals(""))
             registrosPagina = 1; 
         
         paginasTotales = 1; 

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;
        

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catLicencias/catLicencias_form.jsp";
    String paramName = "idLicencia";
    String parametrosPaginacion="";// "idEmpresa="+idEmpresa;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <link rel="shortcut icon" href="../../images/favicon.ico">
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
                    <h1>Administración</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/license.png" alt="icon"/>
                                Licencias
                            </span>                            
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <form id="form_data" name="form_data" action="" method="post">
                                <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Adquiridas</th>
                                            <th>Utilizadas</th>
                                            <th>Disponibles</th>
                                           
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%                                             
                                                try{
                                        %>
                                            <tr <%=(empresa.getIdEstatus()!=1)?"class='inactive'":""%>>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=empresa.getIdEmpresa() %></td>
                                            <td><%=empresaPermisoAplicacionDto.getMaxNumUsuarios() %></td>
                                            <td><%=licenciasUsadas%></td>
                                            <td><%=empresaPermisoAplicacionDto.getMaxNumUsuarios()- licenciasUsadas %></td>                                                                                        
                                        </tr>
                                        <%      }catch(Exception ex){
                                                    ex.printStackTrace();
                                                }                                            
                                        %>
                                    </tbody>
                                </table>                                    
                            </form>
                                    
                            <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                      <!--      <//jsp:include page="../include/reportExportOptions.jsp" flush="true">
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
                            
                            <div id="action_buttons">
                            <p>                                
                                <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                            </p>
                            </div>
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