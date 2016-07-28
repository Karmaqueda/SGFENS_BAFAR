<%-- 
    Document   : catDispositivoMoviles_list
    Created on : 07/01/2013, 06:59:42 PM
    Author     : Leonardo Montes de Oca, leonarzeta@hotmail.com
--%>

<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoDaoImpl"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.dto.DispositivoMovil"%>
<%@page import="com.tsp.sct.bo.DispositivoMovilBO"%>
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
        filtroBusqueda = " AND (IMEI LIKE '%" + buscar + "%' OR MARCA LIKE '%" +buscar+"%' OR MODELO LIKE '%" +buscar+"%' OR NUMERO_SERIE LIKE '%" +buscar+"%')";
    
    
    
    
    
    int idDispositivoMovil = -1;
    try{ idDispositivoMovil = Integer.parseInt(request.getParameter("idDispositivoMovil")); }catch(NumberFormatException e){}
    
    int idEmpresa = -1;
    try{ idEmpresa = Integer.parseInt(request.getParameter("idEmpresa")); }catch(NumberFormatException e){}
    
    
    
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
    
     DispositivoMovilBO dispositivoMovilBO = new DispositivoMovilBO(user.getConn());
     DispositivoMovil[] dispositivosMovilesDto = new DispositivoMovil[0];
     try{
         limiteRegistros = dispositivoMovilBO.findDispositivosMoviles(idDispositivoMovil, idEmpresa , 0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        dispositivosMovilesDto = dispositivoMovilBO.findDispositivosMoviles(idDispositivoMovil, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catHistorialMovil/catHistorialMovil_logs.jsp";
    String paramName = "idDispositivoMovil";
    String paramName2 = "tipoLog"; 
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
                    <h1>Catálogos</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_phone.png" alt="icon"/>
                                Catálogo de Dispositivos Moviles
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
                                                            <td>
                                                                <div id="search">
                                                                <form action="catHistorialMovil_DispositivosMoviles_list.jsp" id="search_form" name="search_form" method="get">
                                                                        <input type="text" id="q" name="q" title="Buscar por IMEI / Marca / Modelo / # Serie" class="" style="width: 300px; float: left"
                                                                               value="<%=buscar%>"/>
                                                                        <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                                </form>
                                                                </div>
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
                                            <th>IMEI</th>
                                            <th>Marca</th>
                                            <th>Modelo</th>
                                            <th>Num. Serie</th>
                                            <th>Alias</th> 
                                            <th>Asignado a</th> 
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                        
                                            for (DispositivoMovil item:dispositivosMovilesDto){
                                                try{
                                                    String nombreEmpleado = "";
                                                    
                                                    try{
                                                        String filtro = " ID_DISPOSITIVO = " + item.getIdDispositivo() + " AND ID_EMPRESA = " + item.getIdEmpresa() ;
                                                        EmpleadoDaoImpl empleadoDaoImpl = new EmpleadoDaoImpl();                                                    
                                                        Empleado[] empleadosDto = empleadoDaoImpl.findByDynamicWhere(filtro, null);
                                                        if(empleadosDto.length>0)
                                                            nombreEmpleado = empleadosDto[0].getNombre() + " " + empleadosDto[0].getApellidoPaterno() + " " + empleadosDto[0].getApellidoMaterno();
                                                    }catch(Exception e){ 
                                                        e.printStackTrace();
                                                    }
                                                    
                                                   
                                                    
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%>>                                            
                                            <td><%=item.getIdDispositivo() %></td>                                            
                                            <td><%=item.getImei()%></td>
                                            <td><%=item.getMarca() %></td>
                                            <td><%=item.getModelo()%></td>                                            
                                            <td><%=item.getNumeroSerie()%></td>
                                            <td><%=item.getAliasTelefono()%></td>
                                            <td><%=nombreEmpleado%></td>  
                                            
                                            <td>                                                                                              
                                                <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdDispositivo()%>&<%=paramName2%>=1"><img src="../../images/log_gral.png" alt="Log General" class="help" title="Log General"/></a>                                                
                                                &nbsp;&nbsp;
                                                <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdDispositivo()%>&<%=paramName2%>=2"><img src="../../images/log_error.png" alt="Log Errores" class="help" title="Log Errores"/></a>                                                
                                                &nbsp;&nbsp;
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
        </div>


    </body>
</html>
<%}%>