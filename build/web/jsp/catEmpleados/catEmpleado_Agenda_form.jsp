<%-- 
    Document   : catEmpleado_Agenda_form
    Created on : 27/11/2014, 01:37:00 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoAgenda"%>
<%@page import="com.tsp.sct.bo.EmpleadoAgendaBO"%>
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
        
        int idEmpleado = -1;
        try {
            idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
        } catch (NumberFormatException e) {
        }
        
        int idEmpleadoAgenda = 0;
        try {
            idEmpleadoAgenda = Integer.parseInt(request.getParameter("idEmpleadoAgenda"));
        } catch (NumberFormatException e) {
        }

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        String newRandomPass = "";
        
        EmpleadoAgendaBO empleadoAgendaBO = new EmpleadoAgendaBO(user.getConn());
        EmpleadoAgenda empleadoAgendasDto = null;
        if (idEmpleadoAgenda > 0){
            empleadoAgendaBO = new EmpleadoAgendaBO(idEmpleadoAgenda,user.getConn());
            empleadoAgendasDto = empleadoAgendaBO.getEmpleadoAgenda();
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
            
            
            var map;
            var marker;
            var infoBubble;
            
	  function initialize() {
	    var latlng = new google.maps.LatLng(0,0);
	    var myOptions = {
	      zoom: 15,
	      center: latlng,
        	//mapTypeControl: true,
		//mapTypeControlOptions: {style: google.maps.MapTypeControlStyle.DROPDOWN_MENU},
      		//navigationControl: true,
      		//navigationControlOptions: {style: google.maps.NavigationControlStyle.SMALL},
	        mapTypeId: google.maps.MapTypeId.ROADMAP
	    };
	    map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
            
            map.controls[google.maps.ControlPosition.TOP_RIGHT].push(
                FullScreenControl(map, 'Pantalla Completa',
                'Salir Pantalla Completa'));  
                
                
            var newLatLng = new google.maps.LatLng(0,0);            
            
            marker = new google.maps.Marker({
                position: newLatLng,
                //map: map,
                //draggable: true,
                icon: "../../images/maps/map_marker_cte.png"                
            });
            
            infoBubble = new InfoBubble({
                  map: map,
                  //content: '<div class="infoBubble">'+nCliente+'</div>',
                  //position: new google.maps.LatLng(-32.0, 149.0),             
                  shadowStyle: 1,
                  padding: 2,
                  backgroundColor: '#CCFFFF',
                  borderRadius: 7,
                  arrowSize: 10,
                  borderWidth: 2,
                  borderColor: '#319AF6',
                  disableAutoPan: false,
                  hideCloseButton: false,
                  arrowPosition: 30,
                  backgroundClassName: 'transparent',
                  arrowStyle: 1
                });
            
            
            }
            
            function loadScript() {
                var script = document.createElement('script');
                script.type = 'text/javascript';
                script.src = 'https://maps.googleapis.com/maps/api/js?libraries=geometry,drawing&sensor=false&' +
                    'callback=initialize';
                document.body.appendChild(script);
            }

            //window.onload = loadScript;
            
            
            function grabar(){
                if(validar()){
                    $.ajax({
                        type: "POST",
                        url: "catEmpleados_ajax.jsp",
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
                                            location.href = "catEmpleado_Agenda_list.jsp?pagina="+"<%=paginaActual%>"+"&idEmpleado="+<%=idEmpleado%>;
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
                /*
                if(jQuery.trim($("#nombre").val())==""){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "nombre de contacto" es requerido</center>',{'animate':true});
                    $("#nombre_contacto").focus();
                    return false;
                }
                */
                return true;
            }
            
            function mostrarCalendario(){
                $( "#agenda_fecha_realiza" ).datepicker({
                        minDate: 0,
                        gotoCurrent: true,
                        changeMonth: true,
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 100);
                            }, 500)}
                });
            }
            
            var llamadasMap = false;
            function muestraClientes(){ 
                $("#clienteDiv").toggle("slow");
                
                
                if ($("#left_mapa").css("display") == "none"){
                     $("#left_mapa").show();
                     
                     if(!llamadasMap){
                        loadScript();
                        llamadasMap = true;
                     }
                     
                }else{
                     $("#left_mapa").css("display","none");
                 }
            } 
            
            function llenaDatos(){
                
                marker.setMap(null);
                
                var nCliente  = $("#idCliente option:selected").text();
                $("#nombreTareaEmpleadoAgenda").val("Visita a Cliente");
                $("#descripcionEmpleadoAgenda").val("Realizar Visita al Cliente:  " + nCliente);    
                
                var idClie = $("#idCliente option:selected").val();
               
                var str = obtenerCliente(idClie);
               
                str = str.trim().split(',');
                //alert(str);
                
                var lat = (str[0]=="null"?0:str[0]);
                var lng = (str[1]=="null"?0:str[1]);
                
                newLatLng = new google.maps.LatLng(lat,lng);
                marker.setPosition(newLatLng);
                marker.setTitle(nCliente);
                marker.setMap(map);
               
                
                infoBubble.setContent('<div class="infoBubble">'+nCliente+'</div>');
                infoBubble.open(map, marker);
                google.maps.event.addListener(marker, 'click', function() {
                    infoBubble.open(map, marker);
                });
               
                  
            }
            
            
            function obtenerCliente(idCliente){
                var result;
                $.ajax({
                        type: "POST",
                        url: "../catClientes/catClientes_ajax.jsp",
                        data: {mode:"latlng",idCliente:idCliente},
                        /*beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },*/
                        success: function(datos){                            
                            result = datos;                          
                        },
                        async: false
                 });
                 
                 return result;                    
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
                    <h1>Agenda</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left" style="height:500px;">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_agenda.png" alt="icon"/>
                                    <% if(empleadoAgendasDto!=null){%>
                                    Editar Agenda ID <%=empleadoAgendasDto!=null?empleadoAgendasDto.getIdAgenda():"" %>
                                    <%}else{%>
                                    Agenda del Empleado con ID <%=idEmpleado%>
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idEmpleadoAgenda" name="idEmpleadoAgenda" value="<%=empleadoAgendasDto!=null?empleadoAgendasDto.getIdAgenda():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <input type="hidden" id="idEmpleado" name="idEmpleado" value="<%=idEmpleado%>" />
                                    <p>
                                        <input type="checkbox" class="checkbox" id="tipoTarea" name="tipoTarea"  onclick="muestraClientes();" ><label for="tipoTarea">Visita a Cliente</label>
                                    </p>
                                    <br/>
                                    <div id="clienteDiv" style="display: none">
                                    <p>
                                        <label>Cliente:</label><br/>
                                        <select id="idCliente" name="idCliente" class="flexselect" onchange="llenaDatos();">
                                            <option></option>
                                            <%= new ClienteBO(user.getConn()).getClientesByIdHTMLCombo((int)idEmpresa, -1," AND ID_ESTATUS<>2 " + (user.getUser().getIdRoles() == RolesBO.ROL_CONDUCTOR_VENDEDOR? " AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM SGFENS_CLIENTE_VENDEDOR WHERE ID_USUARIO_VENDEDOR="+user.getUser().getIdUsuarios()+")" : "") ) %>                                            
                                        </select>
                                    </p>
                                    <br/>
                                    </div>
                                    <p>
                                        <label>*Nombre Tarea:</label><br/>
                                        <input maxlength="100" type="text" id="nombreTareaEmpleadoAgenda" name="nombreTareaEmpleadoAgenda" style="width:300px"
                                               value="<%=empleadoAgendasDto!=null?empleadoAgendaBO.getEmpleadoAgenda().getNombreTarea():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Descripción:</label><br/>
                                        <input maxlength="650" type="text" id="descripcionEmpleadoAgenda" name="descripcionEmpleadoAgenda" style="width:300px"
                                               value="<%=empleadoAgendasDto!=null?empleadoAgendaBO.getEmpleadoAgenda().getDescripcionTarea():"" %>"/>
                                    </p>
                                    <br/>
                                    
                                    <td>
                                        <label>*Fecha a Realizar/Límite:</label><br/>
                                        <input type="text" name="agenda_fecha_realiza" id="agenda_fecha_realiza" readonly
                                            value="<%= empleadoAgendasDto!=null?DateManage.formatDateToNormal(empleadoAgendaBO.getEmpleadoAgenda().getFechaProgramada()):"" %>"
                                            style="width:300px;"/>
                                    </td>
                                    <br/>
                                    <br/>
                                    
                                    <!--<p>   No hay modificación porque se entiende que es una tarea nueva al crearse siempre estara activa
                                        <input type="checkbox" class="checkbox" <%//=empleadoAgendasDto!=null?(empleadoAgendasDto.getIdEstatus()==1?"checked":""):"checked" %> id="estatusAgenda" name="estatusAgenda" value="1"> <label for="estatus">Activo</label>
                                    </p>-->
                                    <br/><br/>
                                    
                                    <div id="action_buttons">
                                        <p>
                                            <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
                                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                        </p>
                                    </div>
                                    
                            </div>
                        </div>
                        <!-- End left column window -->
                        
                        <div class="column_left" style="display: none; height:500px;" id="left_mapa">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_agenda.png" alt="icon"/>
                                    Ubicación                                    
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                <div id="map_canvas" class="formDiv" style="width:500px;height:400px;border:2px solid green; margin: 0 auto;"></div>
                            </div>
                        </div>
                        <!-- End right column window -->
                        
                        
                        
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