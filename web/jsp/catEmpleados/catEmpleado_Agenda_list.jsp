<%-- 
    Document   : catEmpleado_Agenda_list
    Created on : 27/11/2014, 11:39:12 AM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.bo.EmpleadoAgendaBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoAgenda"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String filtroBusqueda = " AND ID_ESTATUS != 2 ";
    if (!buscar.trim().equals(""))
        filtroBusqueda = " AND (NOMBRE_TAREA LIKE '%" + buscar + "%' OR DESCRIPCION_TAREA LIKE '%" +buscar+"%')";
    
    int idEmpleado = -1;
    try{ idEmpleado = Integer.parseInt(request.getParameter("idEmpleado")); }catch(NumberFormatException e){}
    
    int idEmpleadoAgenda = -1;
    try{ idEmpleadoAgenda = Integer.parseInt(request.getParameter("idEmpleadoAgenda")); }catch(NumberFormatException e){}
    
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
    
     EmpleadoAgendaBO empleadoAgendaBO = new EmpleadoAgendaBO(user.getConn());
     EmpleadoAgenda[] empleadoAgendasDto = new EmpleadoAgenda[0];
     try{
         limiteRegistros = empleadoAgendaBO.findEmpleadoAgendas(idEmpleadoAgenda, idEmpleado , 0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        empleadoAgendasDto = empleadoAgendaBO.findEmpleadoAgendas(idEmpleadoAgenda, idEmpleado,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catEmpleados/catEmpleado_Agenda_form.jsp";
    String paramName = "idEmpleadoAgenda";
    String parametrosPaginacion="idEmpleado="+idEmpleado;
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
            
            function borrarAgendaRefistro(idAgenda){
                if(idAgenda>=0){
                    $.ajax({
                        type: "POST",
                        url: "catEmpleados_ajax.jsp",
                        data: { mode: 'agendaBorrarTarea', idAgenda : idAgenda },
                        beforeSend: function(objeto){
                            //$("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style="">ESPERE, procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                    function(r){
                                        location.href = "../catEmpleados/catEmpleado_Agenda_list.jsp?idEmpleado="+<%=idEmpleado%>;
                                    });
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               //$("#ajax_message").html(datos);
                               //$("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
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
                    <h1>Empleados</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_agenda.png" alt="icon"/>
                                Agenda del empleado con ID <%=idEmpleado%>
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                            <tr>
                                                <td>
                                                    <div id="search">
                                                        <form action="catEmpleado_Agenda_list.jsp?idEmpleado=<%=idEmpleado%>" id="search_form" name="search_form" method="get">
                                                            <input type="text" id="q" name="q" title="Buscar por Nombre / Descripción" class="" style="width: 300px; float: left; "
                                                                    value="<%=buscar%>"/>
                                                            <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                    </form>
                                                    </div>
                                                </td>
                                                <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                                <%if(RolAutorizacionBO.permisoNuevoElemento(user.getUser().getIdRoles())){%>
                                                <td>
                                                    <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Nueva Tarea" 
                                                            style="float: right; width: 100px;" onclick="location.href='<%=urlTo%>?idEmpleado=<%=idEmpleado%>&acc=<%="agendaNuevaTarea"%>'"/>
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
                                            <th>Semáforo</th>
                                            <th>Fecha Creación</th>
                                            <th>Fecha Realizar/Límite</th>
                                            <th>Fecha Realizada</th>
                                            <th>Nombre</th>
                                            <th>Descripción</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            final long MILLSECS_POR_DAY = 24 * 60 * 60 * 1000; //Milisegundos al día
                                            Date hoy = new Date();//fecha de hoy
                                            hoy.setHours(0);
                                            hoy.setMinutes(0);
                                            hoy.setSeconds(0);
                                             System.out.println(":: :: :: :: FECHA HOY: "+hoy); 
                                             System.out.println(":: :: :: :: FECHA HOY: "+hoy.getTime()); 
                                            
                                            
                                            for (EmpleadoAgenda item : empleadoAgendasDto){
                                                try{
                                                    int semaforo = 0;
                                                    long diasDeDiferencia = 0;//DIAS DE DIFERENCIA
                                                    if(item.getIdEstatus() == 3){
                                                        semaforo = 4;                                                    
                                                    }else{
                                                        diasDeDiferencia = ((item.getFechaProgramada().getTime()+1000) - hoy.getTime())/MILLSECS_POR_DAY;
                                                        System.out.println(":: :: :: :: fecha programada: "+item.getFechaProgramada().getTime());
                                                        System.out.println(":: :: :: :: Diferencia en días: "+diasDeDiferencia);
                                                        
                                                        if(diasDeDiferencia == 0){
                                                            semaforo = 2;
                                                        }else if(diasDeDiferencia >= 1 ){ //si es menos es porque aun a fecha no se cumple
                                                            semaforo = 1;
                                                        }else if(diasDeDiferencia < 0){
                                                            semaforo = 3;
                                                        }
                                                    }
                                                    
                                        %>
                                        <tr <%=(item.getIdEstatus()==2)?"class='inactive'":""%>>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdAgenda() %></td>
                                            <%if(semaforo == 1){%>
                                                <td><img src="../../images/icon_verde.png" alt="tarea" class="help" title="Tarea en tiempo para cumplirse" /></td>
                                            <%}else if(semaforo == 2){%>
                                                <td><img src="../../images/icon_amarillo.png" alt="tarea" class="help" title="Tarea a vencerse en la fecha actual" /></td>
                                            <%}else if(semaforo == 3){%>
                                                <td><img src="../../images/icon_rojo.png" alt="tarea" class="help" title="Tarea vencida" /></td>
                                            <%}else if(semaforo == 4){%>
                                                <td><img src="../../images/icon_azul.png" alt="tarea" class="help" title="Tarea completada" /></td>
                                            <%}else{%>
                                                <td></td>
                                            <%}%>
                                            <td><%=item.getFechaCreacion()!=null?DateManage.formatDateToNormal(item.getFechaCreacion()):"" %></td>
                                            <td><%=item.getFechaProgramada()!=null?DateManage.formatDateToNormal(item.getFechaProgramada()):"" %></td>
                                            <td><%=item.getFechaEjecucion()!=null?DateManage.formatDateToNormal(item.getFechaEjecucion()):"" %></td>
                                            <td><%=item.getNombreTarea() %></td>
                                            <td><%=item.getDescripcionTarea()%></td>                                            
                                            <td>
                                                <a href="#" onclick="borrarAgendaRefistro(<%=item.getIdAgenda()%>);"><img src="../../images/icon_delete.png" alt="borrar" class="help" title="Borrar" /></a>
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
                                <//jsp:param name="idReport" value="<//%= ReportBO.MARCA_REPORT %>" />
                                <//jsp:param name="parametrosCustom" value="<//%= filtroBusquedaEncoded %>" />
                            <///jsp:include>-->
                            <!-- FIN INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                            
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