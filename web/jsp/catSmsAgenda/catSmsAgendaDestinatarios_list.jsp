<%-- 
    Document   : catSmsAgendaDestinatarios_list
    Created on : 10/03/2016, 12:55:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.bo.SmsAgendaGrupoBO"%>
<%@page import="com.tsp.sct.dao.dto.SmsAgendaGrupo"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.SmsAgendaDestinatario"%>
<%@page import="com.tsp.sct.bo.SmsAgendaDestinatarioBO"%>
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
    String q_campo_extra1 = request.getParameter("q_campo_extra1")!=null? new String(request.getParameter("q_campo_extra1").getBytes("ISO-8859-1"),"UTF-8") :"";
    String q_campo_extra2 = request.getParameter("q_campo_extra2")!=null? new String(request.getParameter("q_campo_extra2").getBytes("ISO-8859-1"),"UTF-8") :"";
    String q_campo_extra3 = request.getParameter("q_campo_extra3")!=null? new String(request.getParameter("q_campo_extra3").getBytes("ISO-8859-1"),"UTF-8") :"";
    String q_campo_extra4 = request.getParameter("q_campo_extra4")!=null? new String(request.getParameter("q_campo_extra4").getBytes("ISO-8859-1"),"UTF-8") :"";
    String q_grupo = request.getParameter("q_grupo")!=null? new String(request.getParameter("q_grupo").getBytes("ISO-8859-1"),"UTF-8") :"";
    
    String filtroBusqueda = "";
    if (!buscar.trim().equals(""))
        filtroBusqueda += " AND (nombre LIKE '%" + buscar + "%' OR numero_celular LIKE '%" +buscar+"%')";
    
    if (buscar_isMostrarSoloActivos.trim().equals("1")){
        filtroBusqueda += " AND ID_ESTATUS != 1 ";
    }else{
        filtroBusqueda += " AND ID_ESTATUS = 1 ";
    }
    
    if (!q_campo_extra1.trim().equals("")){
        filtroBusqueda += " AND campo_extra_1 LIKE '%" +q_campo_extra1+"%'";
    }
    if (!q_campo_extra2.trim().equals("")){
        filtroBusqueda += " AND campo_extra_2 LIKE '%" +q_campo_extra2+"%'";
    }
    if (!q_campo_extra3.trim().equals("")){
        filtroBusqueda += " AND campo_extra_3 LIKE '%" +q_campo_extra3+"%'";
    }
    if (!q_campo_extra4.trim().equals("")){
        filtroBusqueda += " AND campo_extra_4 LIKE '%" +q_campo_extra4+"%'";
    }
    
    int int_q_grupo = -1;
    try{ int_q_grupo = Integer.parseInt(q_grupo); }catch(Exception ex){}
    if (int_q_grupo>0){
        filtroBusqueda += " AND id_sms_agenda_grupo = " + int_q_grupo;
    }
    
    int idSmsAgendaDestinatario = -1;
    try{ idSmsAgendaDestinatario = Integer.parseInt(request.getParameter("idSmsAgendaDestinatario")); }catch(NumberFormatException e){}
    
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
    
     SmsAgendaDestinatarioBO smsAgendaDestinatarioBO = new SmsAgendaDestinatarioBO(user.getConn());
     SmsAgendaDestinatario[] smsAgendaDestinatariosDto = new SmsAgendaDestinatario[0];
     try{
         limiteRegistros = smsAgendaDestinatarioBO.findCantidadSmsAgendaDestinatarios(idSmsAgendaDestinatario, idEmpresa , 0, 0, filtroBusqueda);
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        smsAgendaDestinatariosDto = smsAgendaDestinatarioBO.findSmsAgendaDestinatarios(idSmsAgendaDestinatario, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     SmsAgendaGrupoBO smsAgendaGrupoBO = new SmsAgendaGrupoBO(user.getConn());
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catSmsAgenda/catSmsAgendaDestinatarios_form.jsp";
    String paramName = "idSmsAgendaDestinatario";
    String parametrosPaginacion="q="+buscar+"&inactivos="+buscar_isMostrarSoloActivos+"&q_campo_extra1="+q_campo_extra1+"&q_campo_extra2="+q_campo_extra2+"&q_campo_extra3="+q_campo_extra3+"&q_campo_extra4="+q_campo_extra4;
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
                    <h1>SMS Masivos</h1>
                    
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
                            <form action="catSmsAgendaDestinatarios_list.jsp" id="search_form_advance" name="search_form_advance" method="post">  
                                
                                <p>
                                    <label>Campo 1:</label>
                                    <input maxlength="100" type="text" id="q_campo_extra1" name="q_campo_extra1" style="width:250px"
                                            value="<%= q_campo_extra1 %>"/>
                                </p>
                                <br/>
                                <p>
                                    <label>Campo 2:</label>
                                    <input maxlength="100" type="text" id="q_campo_extra2" name="q_campo_extra2" style="width:250px"
                                            value="<%= q_campo_extra2 %>"/>
                                </p>
                                <br/>
                                <p>
                                    <label>Campo 3:</label>
                                    <input maxlength="100" type="text" id="q_campo_extra3" name="q_campo_extra3" style="width:250px"
                                            value="<%= q_campo_extra3 %>"/>
                                </p>
                                <br/>
                                <p>
                                    <label>Campo 4:</label>
                                    <input maxlength="100" type="text" id="q_campo_extra4" name="q_campo_extra4" style="width:250px"
                                            value="<%= q_campo_extra4 %>"/>
                                </p>
                                <br/>
                                
                                <p>
                                    <label>Grupo:</label><br/>
                                    <select size="1" id="q_grupo" name="q_grupo" style="width:350px" 
                                            class="flexselect">
                                        <option value="-1"></option>
                                        <%
                                            out.print(new SmsAgendaGrupoBO(user.getConn()).getSmsAgendaGruposByIdHTMLCombo(idEmpresa, (int_q_grupo > 0? int_q_grupo : -1), 0,0, ""));
                                        %>
                                    </select>
                                </p>
                                <br/>
                                
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
                                <img src="../../images/icon_smsAgendaDestinatario.png" alt="icon"/>
                                Catálogo de Agenda Destinatarios SMS
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td>
                                                <div id="search">
                                                <form action="catSmsAgendaDestinatarios_list.jsp" id="search_form" name="search_form" method="get">
                                                    <input type="text" id="q" name="q" title="Buscar por Nombre/Numero Celular" class="" style="width: 300px; float: left; "
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
                                            <th>Nombre</th>
                                            <th>Numero Celular</th>                                            
                                            <th>Campo 1</th>
                                            <th>Campo 2</th>
                                            <th>Campo 3</th>
                                            <th>Campo 4</th>
                                            <th>Grupo</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            for (SmsAgendaDestinatario item : smsAgendaDestinatariosDto){
                                                try{
                                                    String nombreGrupo = "";
                                                    try{
                                                        if (item.getIdSmsAgendaGrupo()>0){
                                                            SmsAgendaGrupo grupo = smsAgendaGrupoBO.findSmsAgendaGrupobyId(item.getIdSmsAgendaGrupo());
                                                            nombreGrupo = grupo.getNombreGrupo();
                                                        }
                                                    }catch(Exception e){}
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%>>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%= item.getIdSmsAgendaDest() %></td>
                                            <td><%= StringManage.getValidString(item.getNombre()) %></td>
                                            <td><%= StringManage.getValidString(item.getNumeroCelular()) %></td>
                                            <td><%= StringManage.getValidString(item.getCampoExtra1()) %></td>
                                            <td><%= StringManage.getValidString(item.getCampoExtra2()) %></td>
                                            <td><%= StringManage.getValidString(item.getCampoExtra3()) %></td>
                                            <td><%= StringManage.getValidString(item.getCampoExtra4()) %></td>
                                            <td><%= nombreGrupo %></td>
                                            <td>
                                                <%if(RolAutorizacionBO.permisoAccionesElemento(user.getUser().getIdRoles())){%>
                                                <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdSmsAgendaDest()%>&acc=1&pagina=<%=paginaActual%>"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>
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
                            
                            <br/>
                            
                            <center>
                                <a href="../../jsp/catSmsAgendaGrupos/catSmsAgendaGrupos_list.jsp" id="acciones_ir_a_grupos" title="Grupos"
                                   style="font-size: 18px; font-variant: small-caps; font-weight: bolder;" >
                                    <img src="../../images/icon_smsAgendaGrupo.png" alt="Grupos de Destinatarios" title="Grupos de Destinatarios" class="help"/>
                                    Grupos
                                </a>
                                <span style="width: 100px; min-width: 100px; display: inline-block;"></span>
                                <a href="../../jsp/catSmsAgenda/catSmsAgendaCargaMasiva_form.jsp" id="acciones_ir_a_carga" title="XLS"
                                   style="font-size: 18px; font-variant: small-caps; font-weight: bolder;" >
                                    <img src="../../images/icon_excel.png" alt="Importar XLS" title="Importar XLS" class="help"/>
                                    Importar Xls
                                </a>
                            </center>
                                    
                            <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <jsp:param name="idReport" value="<%= ReportBO.SMS_AGENDA_DESTINATARIOS_REPORT %>" />
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