<%-- 
    Document   : catProspectos_form.jsp
    Created on : 06-nov-2012, 12:13:49
    Author     : ISCesarMartinez poseidon24@hotmail.com
--%>

<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProspecto"%>
<%@page import="com.tsp.sct.bo.SGProspectoBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
//Verifica si el prospecto tiene acceso a este topico
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
        int idProspecto = 0;
        try {
            idProspecto = Integer.parseInt(request.getParameter("idProspecto"));
        } catch (NumberFormatException e) {
        }

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         *   3 = nuevo (modalidad PopUp [cotizaciones, pedidos, facturas]) 
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        String newRandomPass = "";
        
        SGProspectoBO prospectoBO = new SGProspectoBO(user.getConn());
        SgfensProspecto prospectoDto = null;
        if (idProspecto > 0){
            prospectoBO = new SGProspectoBO(idProspecto,user.getConn());
            prospectoDto = prospectoBO.getSgfensProspecto();
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
                        url: "catProspectos_ajax.jsp",
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
                                            <% if (!mode.equals("3")) {%>
                                                location.href = "catProspectos_list.jsp?pagina="+"<%=paginaActual%>";
                                            <%}else{%>
                                                parent.recargarSelectProspectos();
                                                parent.$.fancybox.close();
                                            <%}%>
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
            
            $(document).ready(function() {
                //Si se recibio el parametro para que el modo sea en forma de popup
                <%= mode.equals("3")? "mostrarFormPopUpMode();":""%>
            });
            
            function mostrarFormPopUpMode(){
		$('#left_menu').hide();
                $('#header').hide();
		//$('#show_menu').show();
		$('body').addClass('nobg');
		$('#content').css('marginLeft', 30);
		$('#wysiwyg').css('width', '97%');
		setNotifications();
            }
            
            /// Mapa
            var coordenada;
            var marker;
            var map;
            
            var latitudProspecto = <%=prospectoDto!=null?prospectoDto.getLatitud():0%>;
            var longitudProspecto = <%=prospectoDto!=null?prospectoDto.getLongitud():0%>
            var idProspectoActualizar = <%=prospectoDto!=null?prospectoDto.getIdProspecto():0%>
            
            function initialize() {
                
                var coordenada = new google.maps.LatLng(latitudProspecto,longitudProspecto);
                
                var mapOptions = {
                    zoom: 14,
                    center: coordenada
                };

                 map = new google.maps.Map(document.getElementById('map_canvas'),
                          mapOptions);

                 marker = new google.maps.Marker({
                    map:map,
                    animation: google.maps.Animation.DROP,
                    position: coordenada,
                    title: "Prospecto ID: " + idProspectoActualizar
                 });
                
                 google.maps.event.addListener(marker, 'click', function() {
                    infowindow.open(map,marker);  
                });
                
                //VAMOS A RECUPERAR LAS COORDENADAS /////////////////////
                    //Creo un evento asociado a "mapa" cuando se hace "click" sobre el
                     google.maps.event.addListener(map, "dblclick", function(evento) {
                     latitudProspecto = evento.latLng.lat();
                     longitudProspecto = evento.latLng.lng();

                     //LIMPIAMOS EL MAPA Y LO MOSTRMOS DE NUEVO CON LAS NUEVAS COORDENADAS
                     initialize();
                     //ACTUALIZAMOS LAS COORDENADAS
                     actualizarCoordenadasProspecto(latitudProspecto,longitudProspecto,idProspectoActualizar);
                     //Puedo unirlas en una unica variable si asi lo prefiero
                     //var coordenadas = evento.latLng.lat() + ", " + evento.latLng.lng();

                    //Las muestro con un popup
                     //alert(coordenadas);

                     //Creo un marcador utilizando las coordenadas obtenidas y almacenadas por separado en "latitud" y "longitud"
                     //var coordenadas = new google.maps.LatLng(latitud, longitud); /* Debo crear un punto geografico utilizando google.maps.LatLng */
                     //var marcador = new google.maps.Marker({position: coordenadas,map: mapita, animation: google.maps.Animation.DROP, title:"Un marcador cualquiera"});
                     }); //Fin del evento
                /////////////////////
                
               
                                
            }
            
            function actualizarCoordenadasProspecto(latitudProspecto,longitudProspecto,idProspectoActualizar){
                if (idProspectoActualizar>0){
                    $.ajax({
                        type: "POST",
                        url: "../include/Mapa_ajax.jsp",
                        data: {mode : "2", latitudActualizada : latitudProspecto, longitudActualizada : longitudProspecto, idActualizar : idProspectoActualizar},
                        beforeSend: function(objeto){                            
                            //$("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               //$("#ajax_message").html(datos);
                               $("#ajax_loading").fadeOut("slow");
                               //$("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                        function(r){
                                            //location.href = "catEmpleados_list.jsp";
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
        
            function loadScript() {
                var script = document.createElement('script');
                script.type = 'text/javascript';
                script.src = 'https://maps.googleapis.com/maps/api/js?libraries=geometry,drawing&sensor=false&' +
                    'callback=initialize';
                document.body.appendChild(script);
            }
            
            window.onload = loadScript;
            
            
            /// Fin Mapa  
            
        </script>
    </head>
    <body>
        <div class="content_wrapper">

            <% if (!mode.equals("3")) {%>
                <jsp:include page="../include/header.jsp" flush="true"/>
                <jsp:include page="../include/leftContent.jsp"/>
            <%}%>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Ventas</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_prospecto.png" alt="icon"/>
                                    <% if(prospectoDto!=null){%>
                                    Editar Prospecto ID <%=prospectoDto!=null?prospectoDto.getIdProspecto():"" %>
                                    <%}else{%>
                                    Prospecto
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idProspecto" name="idProspecto" value="<%=prospectoDto!=null?prospectoDto.getIdProspecto():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>*Razón Social/Nombre empresa:</label><br/>
                                        <input maxlength="200" type="text" id="razonSocial" name="razonSocial" style="width:300px"
                                               value="<%=prospectoDto!=null?prospectoBO.getSgfensProspecto().getRazonSocial():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Contacto (Nombre completo):</label><br/>
                                        <input maxlength="100" type="text" id="contacto" name="contacto" style="width:300px"
                                               value="<%=prospectoDto!=null?prospectoBO.getSgfensProspecto().getContacto():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Lada - *Teléfono:</label><br/>
                                        <input maxlength="3" type="text" id="lada" name="lada" style="width:25px"
                                               value="<%=prospectoDto!=null?prospectoBO.getSgfensProspecto().getLada():"" %>"/> -
                                        <input maxlength="8" type="text" id="telefono" name="telefono" style="width:255px"
                                               value="<%=prospectoDto!=null?prospectoBO.getSgfensProspecto().getTelefono():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Celular:</label><br/>
                                        <input maxlength="11" type="text" id="celular" name="celular" style="width:300px"
                                               value="<%=prospectoDto!=null?prospectoBO.getSgfensProspecto().getCelular():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Correo Electrónico:</label><br/>
                                        <input maxlength="100" type="text" id="email" name="email" style="width:300px"
                                               value="<%=prospectoDto!=null?prospectoBO.getSgfensProspecto().getCorreo():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Descripción (p. ej.: intereses de compra):</label><br/>
                                        <textarea rows="5" cols="35" id="descripcion" name="descripcion"><%=prospectoDto!=null?prospectoBO.getSgfensProspecto().getDescripcion():"" %></textarea>
                                    </p>
                                    <br/>
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=prospectoDto!=null?(prospectoDto.getIdEstatus()==1?"checked":""):"checked" %> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
                                    </p>
                                    <br/>
                                    <br/>
                                    
                                    <% if (!mode.equals("3")) {%>
                                        <div id="action_buttons">
                                            <p>
                                                <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
                                                <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                            </p>
                                        </div>
                                    <%}else{
                                        //En caso de ser Formulario en modo PopUp
                                    %>
                                        <div id="action_buttons">
                                            <p>
                                                <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
                                                <input type="button" id="regresar" value="Cerrar" onclick="parent.$.fancybox.close();"/>
                                            </p>
                                        </div>
                                    <%}%>
                                    
                            </div>
                        </div>
                        <!-- End left column window -->
                        
                        <div class="column_right">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_prospecto.png" alt="icon"/>
                                    Imágenes del prospecto
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                <% if (prospectoDto!=null){ %>
                                <p>
                                    <label>Foto Prospecto:</label>
                                    <br/>
                                    <% if (!StringManage.getValidString(prospectoDto.getNombreImagenProspecto()).equals("")) { %>
                                        <img src='showImageProspecto.jsp?image=<%=prospectoBO.getSgfensProspecto().getNombreImagenProspecto()%>' alt="Foto Prospecto">
                                    <% } else{ %>
                                        <br/>
                                        <i>&lt;&lt; Sin imágen registrada &gt;&gt;</i>
                                    <% } %>
                                </p>
                                <%
                                    }
                                %>
                            </div>
                        </div>
                        <br><br><br><br><br><br><br><br>
                           
                            <div class="column_right">                            
                                <div class="header">
                                    <span>
                                        <img src="../../images/icon_mapa_1.png" alt="icon"/>
                                        Ubicación
                                    </span>
                                </div>
                                <div class="content">
                                    <div id="map_canvas" style="height: 230px; width: 100%">
                                        <img src="../../images/maps/ajax-loader.gif" alt="Cargando" style="margin: auto;"/>
                                    </div>     
                                </div>
                            </div>  
                               
                            
                        
                    </div>
                    </form>
                    <!--TODO EL CONTENIDO VA AQUÍ-->

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>


    </body>
</html>
<%}%>