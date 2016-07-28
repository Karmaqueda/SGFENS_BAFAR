<%-- 
    Document   : catReportesConfigurables_form
    Created on : 22/05/2014, 12:03:55 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="com.tsp.sct.bo.TipoReporteBO"%>
<%@page import="com.tsp.sct.dao.dto.ReporteConfigurable"%>
<%@page import="com.tsp.sct.bo.ReporteConfigurableBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el cliente tiene acceso a este topico
    if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
        response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
        response.flushBuffer();
    } else {

        int idEmpresa = user.getUser().getIdEmpresa();
        
        /*
         * Parámetros
         */
        int idReporteConfigurable = 0;
        try {
            idReporteConfigurable = Integer.parseInt(request.getParameter("idReporteConfigurable"));
        } catch (NumberFormatException e) {
        }

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        String newRandomPass = "";
        
        int lunes = -1;
        int martes = -1;
        int miercoles = -1;
        int jueves = -1;
        int viernes = -1;
        int sabado = -1;
        int domingo = -1;
        int todos = -1;
        String fechaInicialObtenida = "";
        String fechaFinalObtenida = "";
        String idVendedorObtenida = "";
        String idClienteObtenida = "";
        //VARIABLE PARA MOSTRAR EN DISPLAY NONE O BLOCK
        String noneBlock = "none";
                        
        ReporteConfigurableBO reporteConfigurableBO = new ReporteConfigurableBO(user.getConn());
        ReporteConfigurable reporteConfigurablesDto = null;
        if (idReporteConfigurable > 0){
            System.out.println("_______idReporteConfigurable: "+idReporteConfigurable);
            reporteConfigurableBO = new ReporteConfigurableBO(idReporteConfigurable,user.getConn());
            reporteConfigurablesDto = reporteConfigurableBO.getReporteConfigurable();
            
            StringTokenizer tokens = new StringTokenizer(reporteConfigurablesDto.getIdDias(),",");
            String seleccion = "";
            while (tokens.hasMoreTokens()) {
                System.out.println("_______recupetando tokens");
                seleccion = "";
                seleccion = tokens.nextToken().intern().trim();
                if(seleccion.equals("1")){
                    domingo = Integer.parseInt(seleccion);                
                }else if(seleccion.equals("2")){
                    lunes = Integer.parseInt(seleccion);
                }else if(seleccion.equals("3")){
                    martes = Integer.parseInt(seleccion);
                }else if(seleccion.equals("4")){
                    miercoles = Integer.parseInt(seleccion);
                }else if(seleccion.equals("5")){
                    jueves = Integer.parseInt(seleccion);
                }else if(seleccion.equals("6")){
                    viernes = Integer.parseInt(seleccion);
                }else if(seleccion.equals("7")){
                    sabado = Integer.parseInt(seleccion);
                }else if(seleccion.equals("8")){
                    todos = Integer.parseInt(seleccion);
                }
            }
            
            if(reporteConfigurablesDto.getFiltros() != null){
                StringTokenizer tokensFiltro = new StringTokenizer(reporteConfigurablesDto.getFiltros(),",");                
                fechaInicialObtenida = tokensFiltro.nextToken().intern().trim();
                fechaFinalObtenida = tokensFiltro.nextToken().intern().trim();
                idVendedorObtenida = tokensFiltro.hasMoreTokens()!=false?tokensFiltro.nextToken().intern().trim():"-1";
                idClienteObtenida = tokensFiltro.hasMoreTokens()!=false?tokensFiltro.nextToken().intern().trim():"-1";
            }
            if(reporteConfigurablesDto.getIdTipoReporte() == 21
                    || reporteConfigurablesDto.getIdTipoReporte() == 27  || reporteConfigurablesDto.getIdTipoReporte() == 29
                    || reporteConfigurablesDto.getIdTipoReporte() == 30 || reporteConfigurablesDto.getIdTipoReporte() == 31){
                    noneBlock = "block";
            }
        }
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
            
            function grabar(){
                if(validar()){
                    $.ajax({
                        type: "POST",
                        url: "catReportesConfigurables_ajax.jsp",
                        data: $("#frm_action").serialize(),
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_message").html(datos);
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                        function(r){
                                            location.href = "catReportesConfigurables_list.jsp";
                                        });
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               $("#action_buttons").fadeIn("slow");
                               $.scrollTo('#inner',800);
                           }
                        }
                    });
                }
            }

            function validar(){                  
                
                var domingoReporte = "", lunesReporte = "", martesReporte = "", miercolesReporte = "", juevesReporte = "", viernesReporte = "", sabadoReporte = "", diarioReporte = "";
                
                if ($('#domingoReporte').attr('checked'))
                    domingoReporte = "1";
                
                if ($('#lunesReporte').attr('checked'))
                    lunesReporte = "2";
                
                if ($('#martesReporte').attr('checked'))
                    martesReporte = "3";
                
                if ($('#miercolesReporte').attr('checked'))
                    miercolesReporte = "4";
                
                if ($('#juevesReporte').attr('checked'))
                    juevesReporte = "5";
                
                if ($('#viernesReporte').attr('checked'))
                    viernesReporte = "6";
                
                if ($('#sabadoReporte').attr('checked'))
                    sabadoReporte = "7";
                
                if ($('#diarioReporte').attr('checked'))
                    diarioReporte = "8";                
                
                if(domingoReporte == "" && lunesReporte == "" && martesReporte == "" && miercolesReporte == "" && juevesReporte == "" && viernesReporte == "" && sabadoReporte == "" && diarioReporte == ""){                    
                    apprise('<center><img src=../../images/warning.png> <br/>Seleccione uno o mas días para el envio </center>',{'animate':true});                    
                    return false;                    
                }
                return true;
            }
            
            function todosDiasMarcado(){
                if ($('#diarioReporte').attr('checked')) {                    
                    //como esta marcado, desmarcamos todos los demas                
                    $('#domingoReporte').attr('checked', false);
                    $('#lunesReporte').attr('checked', false);
                    $('#martesReporte').attr('checked', false);
                    $('#miercolesReporte').attr('checked', false);
                    $('#juevesReporte').attr('checked', false);
                    $('#viernesReporte').attr('checked', false);
                    $('#sabadoReporte').attr('checked', false);                    
                }
                if ($('#domingoReporte').attr('checked') || $('#lunesReporte').attr('checked') || $('#martesReporte').attr('checked') || $('#miercolesReporte').attr('checked') || $('#juevesReporte').attr('checked') || $('#viernesReporte').attr('checked') || $('#sabadoReporte').attr('checked') )
                    $('#diarioReporte').attr('checked', false);                
            }
            
            function apareceRangoFechas(){
                var seleccionadoTipoReporte = document.getElementById("idTipoReporteSeleccion").value;   
                
                document.getElementById("fechaInicialfiltro").value="";
                document.getElementById("fechaFinalFiltro").value="";
                
                var mostrarOpcionesTiempoConsultaMaximo = false;
                if (seleccionadoTipoReporte==4 || seleccionadoTipoReporte==20
                        || seleccionadoTipoReporte==28){
                    mostrarOpcionesTiempoConsultaMaximo = true;
                }
                if(seleccionadoTipoReporte==30){
                    //document.getElementById("rangoFechasConsulta").style.display="block";
                    mostrarOpcionesTiempoConsultaMaximo = true;
                }else if(seleccionadoTipoReporte==27 || seleccionadoTipoReporte==21){
                    //document.getElementById("rangoFechasConsulta").style.display="block";
                    document.getElementById("pCliente").style.display="block";
                    document.getElementById("pVendedor").style.display="block";
                    mostrarOpcionesTiempoConsultaMaximo = true;
                }else if(seleccionadoTipoReporte==40){                    
                    document.getElementById("pVendedor").style.display="block";
                    document.getElementById("grupo_max_tiempo_atras").style.display="none";
                }else if(seleccionadoTipoReporte==45){ 
                    //document.getElementById("rangoFechasConsulta").style.display="block";
                    document.getElementById("pVendedor").style.display="block";
                    mostrarOpcionesTiempoConsultaMaximo = true;
                }else{
                    //document.getElementById("rangoFechasConsulta").style.display="none";
                    document.getElementById("pCliente").style.display="none";
                    document.getElementById("pVendedor").style.display="none";
                    document.getElementById("q_idvendedor").value="";
                    document.getElementById("q_idcliente").value="";                    
                    
                }
                
                if (mostrarOpcionesTiempoConsultaMaximo){
                    document.getElementById("grupo_max_tiempo_atras").style.display="block";
                }else{
                    document.getElementById("grupo_max_tiempo_atras").style.display="none";
                }
            }
            
            function mostrarCalendario(){
                $( "#fechaInicialfiltro" ).datepicker({                        
                        gotoCurrent: true,
                        changeMonth: true,
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 100);
                            }, 500)}
                });
                $( "#fechaFinalFiltro" ).datepicker({                       
                        gotoCurrent: true,
                        changeMonth: true,
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 100);
                            }, 500)}
                });
            }
            
            function limpiarCampo(campoLimpiar){
                document.getElementById(campoLimpiar).value="";
            }
            
            function cancelar(){             
                
                apprise('Se perderan los cambios que no han sido guardados', {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, 
                    function(r){
                        if(r){
                           history.back();
                        }
                });  
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
                    <h1>Reporte Configurable</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_reporte.png" alt="icon"/>
                                    <% if(reporteConfigurablesDto!=null){%>
                                    Editar ReporteConfigurable ID <%=reporteConfigurablesDto!=null?reporteConfigurablesDto.getIdConfiguracion():"" %>
                                    <%}else{%>
                                    ReporteConfigurable
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idReporteConfigurable" name="idReporteConfigurable" value="<%=reporteConfigurablesDto!=null?reporteConfigurablesDto.getIdConfiguracion():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />                                    
                                    <label>*Tipo de Reporte:</label><br/>
                                    <select size="1" id="idTipoReporteSeleccion" name="idTipoReporteSeleccion" onchange="apareceRangoFechas();">                                        
                                        <option value="-1">Selecciona un Tipo de Reporte</option>
                                            <%
                                                out.print(new TipoReporteBO(user.getConn()).getTipoReporteCbbByIdHTMLCombo(idEmpresa, (reporteConfigurablesDto!=null?reporteConfigurableBO.getReporteConfigurable().getIdTipoReporte():-1)));
                                            %>
                                    </select>
                                    <br/>
                                    <br/>
                                    <label>*Selecciona los días que se enviara el reporte:</label>
                                    <br/>
                                    
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=reporteConfigurablesDto!=null?(domingo==1?"checked":""):"" %> id="domingoReporte" name="domingoReporte" value="1" onclick="todosDiasMarcado();"> <label for="domingoReporte">Domingo</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" <%=reporteConfigurablesDto!=null?(lunes==2?"checked":""):"" %> id="lunesReporte" name="lunesReporte" value="2" onclick="todosDiasMarcado();"> <label for="lunesReporte">Lunes</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" <%=reporteConfigurablesDto!=null?(martes==3?"checked":""):"" %> id="martesReporte" name="martesReporte" value="3" onclick="todosDiasMarcado();"> <label for="martesReporte">Martes</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" <%=reporteConfigurablesDto!=null?(miercoles==4?"checked":""):"" %> id="miercolesReporte" name="miercolesReporte" value="4" onclick="todosDiasMarcado();"> <label for="miercolesReporte">Miércoles</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" <%=reporteConfigurablesDto!=null?(jueves==5?"checked":""):"" %> id="juevesReporte" name="juevesReporte" value="5" onclick="todosDiasMarcado();"> <label for="juevesReporte">Jueves</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" <%=reporteConfigurablesDto!=null?(viernes==6?"checked":""):"" %> id="viernesReporte" name="viernesReporte" value="6" onclick="todosDiasMarcado();"> <label for="viernesReporte">Viernes</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" <%=reporteConfigurablesDto!=null?(sabado==7?"checked":""):"" %> id="sabadoReporte" name="sabadoReporte" value="7" onclick="todosDiasMarcado();"> <label for="sabadoReporte">Sábado</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" <%=reporteConfigurablesDto!=null?(todos==8?"checked":""):"" %> id="diarioReporte" name="diarioReporte" value="8" onclick="todosDiasMarcado();"> <label for="diarioReporte">Todos los Días</label>
                                    </p>
                                    <br/>
                                    
                                    <div id="grupo_max_tiempo_atras">
                                        <fieldset style="border: 2px groove; padding: 1em; display: inline; ">
                                        <legend>Consultar desde:</legend>
                                            <input type="radio" name="max_tiempo_atras" value="D-1" checked>1 Día antes.<br>
                                            <input type="radio" name="max_tiempo_atras" value="S-1">1 Semana antes (7 días).<br>
                                            <input type="radio" name="max_tiempo_atras" value="M-1">1 Mes antes (30 días).<br>
                                        </fieldset>
                                        <br/>
                                    </div>
                                    <br/>
                                    
                                    <p>
                                        <label>*Correos destino(separados por coma):</label><br/>
                                        <input maxlength="150" type="text" id="correosReporteDestino" name="correosReporteDestino" style="width:97%;"
                                               value="<%=reporteConfigurablesDto!=null?reporteConfigurableBO.getReporteConfigurable().getCorreos():"" %>"/>
                                    </p>
                                    <br/>
                                    
                                    <div id="rangoFechasConsulta">
                                        <fieldset style="border: 2px groove; padding: 1em; display: inline; ">
                                        <legend>Enviarlo solo durante estas fechas:</legend>
                                            <p id="leyendaFechaInicialfiltro">
                                                <label>Fecha inicial:</label>
                                                <input maxlength="30" type="text" id="fechaInicialfiltro" name="fechaInicialfiltro" style="width:30%;" readonly
                                                       value="<%=reporteConfigurablesDto!=null?(!fechaInicialObtenida.equals("NA")?fechaInicialObtenida:""):"" %>"/>
                                                <img src="../../images/icon_delete.png" title="Limpiar" alt="Limpiar" onclick="limpiarCampo('fechaInicialfiltro');"/>
                                            </p>
                                            <br/>
                                            <p id="leyendaFechaFinalFiltro">
                                                <label>Fecha final:</label>
                                                <input maxlength="30" type="text" id="fechaFinalFiltro" name="fechaFinalFiltro" style="width:30%;" readonly
                                                       value="<%=reporteConfigurablesDto!=null?(!fechaFinalObtenida.equals("NA")?fechaFinalObtenida:""):"" %>"/>
                                                <img src="../../images/icon_delete.png" title="Limpiar" alt="Limpiar" onclick="limpiarCampo('fechaFinalFiltro');"/>
                                            </p>   
                                        </fieldset>
                                    </div>
                                    <br/>
                                            
                                    <p style="display: <%=noneBlock%>" id="pVendedor">
                                       <label>Vendedor:</label><br/>
                                        <select id="q_idvendedor" name="q_idvendedor" style="width:30%;">
                                            <option></option>
                                            <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL, reporteConfigurablesDto!=null?(!idVendedorObtenida.equals("NA")?Integer.parseInt(idVendedorObtenida):-1):-1) %> 
                                            <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_CONDUCTOR_VENDEDOR, reporteConfigurablesDto!=null?(!idVendedorObtenida.equals("NA")?Integer.parseInt(idVendedorObtenida):-1):-1) %>
                                            <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_COBRADOR, reporteConfigurablesDto!=null?(!idVendedorObtenida.equals("NA")?Integer.parseInt(idVendedorObtenida):-1):-1) %>
                                        </select>                                        
                                        <img src="../../images/icon_delete.png" title="Limpiar" alt="Limpiar" onclick="limpiarCampo('q_idvendedor');"/>
                                    </p>                                       
                                    <p style="display: <%=noneBlock%>" id="pCliente">
                                    <label>Cliente:</label><br/>
                                    <select id="q_idcliente" name="q_idcliente" style="width:30%;">
                                        <option></option>
                                        <%= new ClienteBO(user.getConn()).getClientesByIdHTMLCombo(idEmpresa,reporteConfigurablesDto!=null?(!idClienteObtenida.equals("NA")?Integer.parseInt(idClienteObtenida):-1):-1," AND ID_ESTATUS <> 2 " + (user.getUser().getIdRoles() == RolesBO.ROL_CONDUCTOR_VENDEDOR? " AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM SGFENS_CLIENTE_VENDEDOR WHERE ID_USUARIO_VENDEDOR="+user.getUser().getIdUsuarios()+")" : "") ) %>
                                        </select>                                        
                                        <img src="../../images/icon_delete.png" title="Limpiar" alt="Limpiar" onclick="limpiarCampo('q_idcliente');"/>
                                    </p>
                                    <br/>   
                                   
                                    <div id="action_buttons">
                                        <p>
                                            <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
                                            <input type="button" id="regresar" value="Cancelar" onclick="cancelar();"/>
                                        </p>
                                    </div>
                                    
                            </div>
                        </div>
                        <!-- End left column window -->
                        
                        
                    </div>
                    </form>
                    <!--TODO EL CONTENIDO VA AQUÍ-->

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>

        <script>
            mostrarCalendario();
        </script>
    </body>
</html>
<%}%>