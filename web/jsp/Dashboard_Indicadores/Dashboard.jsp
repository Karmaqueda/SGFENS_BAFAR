<%-- 
    Document   : Dashboard
    Created on : 27/06/2016, 12:39:56 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.CrFormularioEventoBO"%>
<%@page import="com.tsp.sct.dao.dto.CrFormularioEvento"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.bo.PasswordBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el cliente tiene acceso a este topico
    if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
        response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
        response.flushBuffer();
    } else {
        
        int paginaActual = 1;
        try{
            paginaActual = Integer.parseInt(request.getParameter("pagina"));
        }catch(Exception e){}

        int idEmpresa = user.getUser().getIdEmpresa();
        
        /*
         * Parámetros
         */
        
        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
                
        CrFormularioEvento[] crFormularioEventos = null;
        CrFormularioEventoBO crFormularioEventoBO = new CrFormularioEventoBO(user.getConn());
        int porRevisar = 0;
        int maxMarcadorporRevisar = 100;
        int yellowFromporRevisar = 75;
        int yellowToporRevisar = 90;
        int redFromporRevisar = 90;
        int redToporRevisar = maxMarcadorporRevisar;
        
        int enRevision = 0;
        int maxMarcadorenRevision = 100;
        int yellowFromenRevision = 75;
        int yellowToenRevision = 90;
        int redFromenRevision = 90;
        int redToenRevision = maxMarcadorenRevision;
        
        int impresiones = 0;
        int maxMarcadorimpresiones = 100;
        int yellowFromimpresiones = 75;
        int yellowToimpresiones = 90;
        int redFromimpresiones = 90;
        int redToimpresiones = maxMarcadorimpresiones;
        
        int aprobados = 0;
        int maxMarcadoraprobados = 100;
        int yellowFromaprobados = 75;
        int yellowToaprobados = 90;
        int redFromaprobados = 90;
        int redToaprobados = maxMarcadoraprobados;
        
        int dispersados = 0;
        int maxMarcadordispersados = 100;
        int yellowFromdispersados = 75;
        int yellowTodispersados = 90;
        int redFromdispersados = 90;
        int redTodispersados = maxMarcadordispersados;
        
        int cancelados = 0;
        int maxMarcadorcancelados = 100;
        int yellowFromcancelados = 75;
        int yellowTocancelados = 90;
        int redFromcancelados = 90;
        int redTocancelados = maxMarcadorcancelados;
                
        String filtroparte1 = " AND id_formulario_evento IN (SELECT id_formulario_evento FROM cr_frm_evento_solicitud WHERE id_estado_solicitud in(";
        String filtroParte2 = ")) ";
        try{
            if(user.getUser().getIdRoles() == RolesBO.ROL_MESA_DE_CONTROL){
                porRevisar = crFormularioEventoBO.findCantidadCrFormularioEventos(0, idEmpresa, 0, 0, filtroparte1 + "2,5" + filtroParte2);
            }else if(user.getUser().getIdRoles() == RolesBO.ROL_VERIFICADOR){
                porRevisar = crFormularioEventoBO.findCantidadCrFormularioEventos(0, idEmpresa, 0, 0, filtroparte1 + "2,4" + filtroParte2);
            }else{
                porRevisar = crFormularioEventoBO.findCantidadCrFormularioEventos(0, idEmpresa, 0, 0, filtroparte1 + "2,4,5" + filtroParte2);
            }
            //maxMarcadorporRevisar += porRevisar;
            yellowFromporRevisar += porRevisar;
            yellowToporRevisar += porRevisar;
            redFromporRevisar += porRevisar;
            redToporRevisar = maxMarcadorporRevisar;
        }catch(Exception e){}
        try{
            enRevision = crFormularioEventoBO.findCantidadCrFormularioEventos(0, idEmpresa, 0, 0, filtroparte1 + " 3 " + filtroParte2);
            //maxMarcadorenRevision += enRevision;
            yellowFromenRevision += enRevision;
            yellowToenRevision += enRevision;
            redFromenRevision += enRevision;
            redToenRevision = maxMarcadorenRevision;
        }catch(Exception e){}
        try{
            impresiones = crFormularioEventoBO.findCantidadCrFormularioEventos(0, idEmpresa, 0, 0, filtroparte1 + " 7 " + filtroParte2);
            //maxMarcadorimpresiones += impresiones;
            yellowFromimpresiones += impresiones;
            yellowToimpresiones += impresiones;
            redFromimpresiones += impresiones;
            redToimpresiones = maxMarcadorimpresiones;
        }catch(Exception e){}
        try{
            aprobados = crFormularioEventoBO.findCantidadCrFormularioEventos(0, idEmpresa, 0, 0, filtroparte1 + " 6 " + filtroParte2);
            //maxMarcadoraprobados += aprobados;
            yellowFromaprobados += aprobados;
            yellowToaprobados += aprobados;
            redFromaprobados += aprobados;
            redToaprobados = maxMarcadoraprobados;
        }catch(Exception e){}
        try{
            dispersados = crFormularioEventoBO.findCantidadCrFormularioEventos(0, idEmpresa, 0, 0, filtroparte1 + " 8 " + filtroParte2);
            //maxMarcadordispersados += dispersados;
            yellowFromdispersados += dispersados;
            yellowTodispersados += dispersados;
            redFromdispersados += dispersados;
            redTodispersados = maxMarcadordispersados;
        }catch(Exception e){}
        try{
            cancelados = crFormularioEventoBO.findCantidadCrFormularioEventos(0, idEmpresa, 0, 0, filtroparte1 + " 1,9 " + filtroParte2);
            //maxMarcadorcancelados += cancelados;
            yellowFromcancelados += cancelados;
            yellowTocancelados += cancelados;
            redFromcancelados += cancelados;
            redTocancelados = maxMarcadorcancelados;
        }catch(Exception e){}
        
        
        
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
        
        <!--Mesografia de los graficos-->
        <!--https://google-developers.appspot.com/chart/interactive/docs/gallery/gauge-->
        
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
               
        <script type="text/javascript">
            google.charts.load('current', {'packages':['gauge']});
            google.charts.setOnLoadCallback(drawChart);
            function drawChart() {

                var dataPorRevisar = google.visualization.arrayToDataTable([
                  ['Label', 'Value'],
                  ['Por revisar', <%=porRevisar%>]
                ]);
                var optionsPorRevisar = {
                  width: 400, height: 220,
                  redFrom: <%=redFromporRevisar%>, redTo: <%=redToporRevisar%>,
                  yellowFrom: <%=yellowFromporRevisar%>, yellowTo: <%=yellowToporRevisar%>,
                  minorTicks: 5, max: <%=maxMarcadorporRevisar%>
                };
                var chartPorRevisar = new google.visualization.Gauge(document.getElementById('divPorRevisar'));
                chartPorRevisar.draw(dataPorRevisar, optionsPorRevisar);
                
                /////
                
                var dataEnRevision = google.visualization.arrayToDataTable([
                  ['Label', 'Value'],
                  ['En Revisión', <%=enRevision%>]
                ]);
                var optionsEnRevision = {
                  width: 400, height: 220,
                  redFrom: <%=redFromenRevision%>, redTo: <%=redToenRevision%>,
                  yellowFrom: <%=yellowFromenRevision%>, yellowTo: <%=yellowToenRevision%>,
                  minorTicks: 5, max: <%=maxMarcadorenRevision%>
                };
                var chartEnRevision = new google.visualization.Gauge(document.getElementById('divEnRevision'));
                chartEnRevision.draw(dataEnRevision, optionsEnRevision);
                
                /////
                
                var dataImpresionLiberada = google.visualization.arrayToDataTable([
                  ['Label', 'Value'],
                  ['Impresiones', <%=impresiones%>]
                ]);
                var optionsImpresionLiberada = {
                  width: 400, height: 220,
                  redFrom: <%=redFromimpresiones%>, redTo: <%=redToimpresiones%>,
                  yellowFrom: <%=yellowFromimpresiones%>, yellowTo: <%=yellowToimpresiones%>,
                  minorTicks: 5, max: <%=maxMarcadorimpresiones%>
                };
                var chartImpresionLiberada = new google.visualization.Gauge(document.getElementById('divImpresionLiberada'));
                chartImpresionLiberada.draw(dataImpresionLiberada, optionsImpresionLiberada);
                
                /////
                
                var dataAprobados = google.visualization.arrayToDataTable([
                  ['Label', 'Value'],
                  ['Aprobados', <%=aprobados%>]
                ]);
                var optionsAprobados = {
                  width: 400, height: 220,
                  redFrom: <%=redFromaprobados%>, redTo: <%=redToaprobados%>,
                  yellowFrom: <%=yellowFromaprobados%>, yellowTo: <%=yellowToaprobados%>,
                  minorTicks: 5, max: <%=maxMarcadoraprobados%>
                };
                var chartAprobados = new google.visualization.Gauge(document.getElementById('divAprobados'));
                chartAprobados.draw(dataAprobados, optionsAprobados);
                
                /////
                
                var dataDispersados = google.visualization.arrayToDataTable([
                  ['Label', 'Value'],
                  ['Dispersados', <%=dispersados%>]
                ]);
                var optionsDispersados = {
                  width: 400, height: 220,
                  redFrom: <%=redFromdispersados%>, redTo: <%=redTodispersados%>,
                  yellowFrom: <%=yellowFromdispersados%>, yellowTo: <%=yellowTodispersados%>,
                  minorTicks: 5, max: <%=maxMarcadordispersados%>
                };
                var chartDispersados = new google.visualization.Gauge(document.getElementById('divDispersados'));
                chartDispersados.draw(dataDispersados, optionsDispersados);
                
                /////
                
                var dataCanceladosRechazados = google.visualization.arrayToDataTable([
                  ['Label', 'Value'],
                  ['Cancelados', <%=cancelados%>]
                ]);
                var optionsCanceladosRechazados = {
                  width: 400, height: 220,
                  redFrom: <%=redFromcancelados%>, redTo: <%=redTocancelados%>,
                  yellowFrom: <%=yellowFromcancelados%>, yellowTo: <%=yellowTocancelados%>,
                  minorTicks: 5, max: <%=maxMarcadorcancelados%>
                };
                var chartCanceladosRechazados = new google.visualization.Gauge(document.getElementById('divCanceladosRechazados'));
                chartCanceladosRechazados.draw(dataCanceladosRechazados, optionsCanceladosRechazados);
                
                
            } 

        </script>
    </head>
    <body>
        <div>

         
            <!-- Inicio de Contenido -->
            <div>

                <div class="inner">
                    <h1><%=user.getUser().getIdRoles()==1?"Desarollo":user.getUser().getIdRoles()==2?"Administrador":user.getUser().getIdRoles()==17?"Promotor":user.getUser().getIdRoles()==29?"Gestor de Crédito":user.getUser().getIdRoles()==40?"Gerente":user.getUser().getIdRoles()==41?"Verificador":user.getUser().getIdRoles()==42?"Mesa de control":user.getUser().getIdRoles()==43?"Direccion":""%></h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="onecolumn">
                        <table>
                            <tr>
                                <% if (user.getUser().getIdRoles() == RolesBO.ROL_DESARROLLO || user.getUser().getIdRoles() == RolesBO.ROL_ADMINISTRADOR
                                        || user.getUser().getIdRoles() == RolesBO.ROL_MESA_DE_CONTROL || user.getUser().getIdRoles() == RolesBO.ROL_PROMOTOR
                                        || user.getUser().getIdRoles() == RolesBO.ROL_VERIFICADOR
                                        || user.getUser().getIdRoles() == RolesBO.ROL_GERENTE){%>
                                    <td>
                                        <div id="divPorRevisar" title="Por Revisar" onclick="location.href='../catCrFormularioEvento/catCrFormularioEvento_list.jsp?solicitud=2'" style="cursor:pointer"></div>
                                    </td>
                                <%}%>
                                <% if (user.getUser().getIdRoles() == RolesBO.ROL_DESARROLLO || user.getUser().getIdRoles() == RolesBO.ROL_ADMINISTRADOR
                                        || user.getUser().getIdRoles() == RolesBO.ROL_MESA_DE_CONTROL || user.getUser().getIdRoles() == RolesBO.ROL_PROMOTOR
                                        || user.getUser().getIdRoles() == RolesBO.ROL_GERENTE){%>
                                    <td>
                                        <div id="divEnRevision" title="En Revisión" onclick="location.href='../catCrFormularioEvento/catCrFormularioEvento_list.jsp?solicitud=3'" style="cursor:pointer"></div>
                                    </td>
                                <%}%>
                                <% if (user.getUser().getIdRoles() == RolesBO.ROL_DESARROLLO || user.getUser().getIdRoles() == RolesBO.ROL_ADMINISTRADOR
                                        || user.getUser().getIdRoles() == RolesBO.ROL_MESA_DE_CONTROL || user.getUser().getIdRoles() == RolesBO.ROL_PROMOTOR
                                        || user.getUser().getIdRoles() == RolesBO.ROL_GERENTE){%>
                                    <td>
                                        <div id="divImpresionLiberada" title="Impresiones Liberadas" onclick="location.href='../catCrFormularioEvento/catCrFormularioEvento_list.jsp?solicitud=7'" style="cursor:pointer"></div>
                                    </td>
                                <%}%>
                            </tr>
                            
                            <tr>
                                <% if (user.getUser().getIdRoles() == RolesBO.ROL_DESARROLLO || user.getUser().getIdRoles() == RolesBO.ROL_ADMINISTRADOR
                                        || user.getUser().getIdRoles() == RolesBO.ROL_MESA_DE_CONTROL || user.getUser().getIdRoles() == RolesBO.ROL_PROMOTOR
                                        || user.getUser().getIdRoles() == RolesBO.ROL_GERENTE ){%>
                                    <td>
                                        <div id="divAprobados" title="Aprobados" onclick="location.href='../catCrFormularioEvento/catCrFormularioEvento_list.jsp?solicitud=6'" style="cursor:pointer"></div>
                                    </td>
                                <%}%>
                                <% if (user.getUser().getIdRoles() == RolesBO.ROL_DESARROLLO || user.getUser().getIdRoles() == RolesBO.ROL_ADMINISTRADOR
                                        || user.getUser().getIdRoles() == RolesBO.ROL_MESA_DE_CONTROL
                                        || user.getUser().getIdRoles() == RolesBO.ROL_GERENTE){%>
                                    <td>
                                        <div id="divDispersados" title="Dispersados" onclick="location.href='../catCrFormularioEvento/catCrFormularioEvento_list.jsp?solicitud=8'" style="cursor:pointer"></div>
                                    </td>
                                <%}%>
                                <% if (user.getUser().getIdRoles() == RolesBO.ROL_DESARROLLO || user.getUser().getIdRoles() == RolesBO.ROL_ADMINISTRADOR
                                        || user.getUser().getIdRoles() == RolesBO.ROL_MESA_DE_CONTROL
                                        || user.getUser().getIdRoles() == RolesBO.ROL_GERENTE){%>
                                    <td>
                                        <div id="divCanceladosRechazados" title="Cancelados y Rechazados" onclick="location.href='../catCrFormularioEvento/catCrFormularioEvento_list.jsp?solicitud=9'" style="cursor:pointer"></div>
                                    </td>
                                <%}%>
                            </tr>
                            
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_marca.png" alt="icon"/>
                                    Indicadores
                                </span>
                            </div>
                                
                        </table>
                            <br class="clear"/>
                            
                        
                        <!-- End left column window -->
                        
                        
                    </div>
                    </form>
                    <!--TODO EL CONTENIDO VA AQUÍ-->

                </div>

            </div>
            <!-- Fin de Contenido-->
        </div>


    </body>
</html>
<%}%>