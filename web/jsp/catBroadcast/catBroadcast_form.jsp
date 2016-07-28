<%-- 
    Document   : catBroadcast
    Created on : 21/10/2015, 01:44:09 PM
    Author     : HpPyme
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el cliente tiene acceso a este topico
    if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
        response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
        response.flushBuffer();
    } else {

        long idEmpresa = user.getUser().getIdEmpresa();

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";

        
        
       
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

            function grabar() {

                $.ajax({
                    type: "POST",
                    url: "catBroadcast_ajax.jsp",
                    data: $("#frm_action").serialize(),
                    beforeSend: function(objeto) {
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos) {
                        if (datos.indexOf("--EXITO-->", 0) > 0) {
                            $("#ajax_message").html(datos);
                            $("#ajax_loading").fadeOut("slow");
                            $("#ajax_message").fadeIn("slow");
                            apprise('<center><img src=../../images/info.png> <br/>' + datos + '</center>', {'animate': true},
                            function(r) {
                                location.href = "catEmpleados_list.jsp";
                            });
                        } else {
                            $("#ajax_loading").fadeOut("slow");
                            $("#ajax_message").html(datos);
                            $("#ajax_message").fadeIn("slow");
                            $("#action_buttons").fadeIn("slow");
                            $.scrollTo('#inner', 800);
                        }
                    }
                });

            }
            
            function mostrarCalendario(){              
                
                var dates = $('#fechaLiberacion').datepicker({
                        //minDate: 0,
			changeMonth: true,
			//numberOfMonths: 2,
                        //beforeShow: function() {$('#fh_min').css("z-index", 9999); },
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 998);
                            }, 500)},
			onSelect: function( selectedDate ) {
				var option = this.id == "q_fh_min" ? "minDate" : "maxDate",
					instance = $( this ).data( "datepicker" ),
					date = $.datepicker.parseDate(
						instance.settings.dateFormat ||
						$.datepicker._defaults.dateFormat,
						selectedDate, instance.settings );
				dates.not( this ).datepicker( "option", option, date );
			}
		});
                
            }
            
            function tipoAlert(sel){
                if(sel.value==1){
                    $('.column_right').show('slow');
                }else{
                    $('.column_right').hide('slow');
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
                    <h1>Administración</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                        <div class="twocolumn">
                            <div class="column_left">
                                <div class="header">
                                    <span>
                                        <img src="../../images/notification-message.png" alt="icon"/>
                                        <%// if(movilMensajesDto!=null){*/%>
                                        Editar Broadcast ID <%//=movilMensajesDto!=null?movilMensajesDto.getIdMovilMensaje():"" %>
                                        <%//}else{%>
                                        Mensaje
                                        <%//}%>
                                    </span>
                                </div>
                                <br class="clear"/>
                                <div class="content">
                                    <input type="hidden" id="idNotif" name="idNotif" value="<%//=movilMensajesDto!=null?movilMensajesDto.getIdMovilMensaje():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />

                                    <p>
                                        <label>*Titulo:</label><br/>
                                        <input maxlength="160" type="text" id="titulo" name="titulo" style="width:350px;"
                                               value="<%//=movilMensajesDto!=null?movilMensajeBO.getMovilMensaje().getMensaje():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Mensaje(HTML):</label><br/>
                                        <textarea id="texto" name="texto" style="width:350px; height: 100px;"
                                                  value="<%//=movilMensajesDto!=null?movilMensajeBO.getMovilMensaje().getMensaje():"" %>">
                                        </textarea>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Imagen:</label><br/>
                                        <select id="path" name="path" style="width:350px;" onchange="tipoAlert(this);">
                                            <option value="0"></option>
                                            <option value="1">EVC</option>
                                            <option value="2">Alerta Verde</option>
                                            <option value="3">Alerta Amarilla</option>
                                            <option value="4">Alerta Roja</option>
                                            <option value="5">Alerta Azul</option>
                                        </select>
                                    </p>
                                    <br/>                                    
                                    <div id="action_buttons">
                                        <p>
                                            <input type="button" id="enviar" value="Enviar" onclick="grabar();"/>
                                            <input type="button" id="regresar" value="Cerrar" onclick="history.back();"/>
                                        </p>
                                    </div>


                                </div>
                            </div>
                            <!-- End left column window -->

                            <div class="column_right">
                                <div class="header">
                                    <span>
                                        <img src="../../images/notification-message.png" alt="icon"/>
                                        Información de Apk
                                    </span>
                                </div>
                                <br class="clear"/>
                                <div class="content">       
                                    <p>
                                        <label>Version Code:</label><br/>
                                        <input maxlength="160" type="text" id="versionCode" name="versionCode" style="width:350px;"
                                               value="<%//=movilMensajesDto!=null?movilMensajeBO.getMovilMensaje().getMensaje():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Número de Versión:</label><br/>
                                        <input maxlength="160" type="text" id="numVersion" name="numVersion" style="width:350px;"
                                               value="<%//=movilMensajesDto!=null?movilMensajeBO.getMovilMensaje().getMensaje():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Fecha Liberación:</label><br/>
                                        <input maxlength="160" type="text" id="fechaLiberacion" name="fechaLiberacion" style="width:350px;"
                                               value="<%//=movilMensajesDto!=null?movilMensajeBO.getMovilMensaje().getMensaje():"" %>"/>
                                    </p>
                                    <br/>     
                                    <p>
                                        <label>Comentarios:</label><br/>
                                        <textarea id="comentarios" name="comentarios" style="width:350px; height: 100px;"
                                                  value="<%//=movilMensajesDto!=null?movilMensajeBO.getMovilMensaje().getMensaje():"" %>">
                                        </textarea>
                                    <br/> 
                                    <p>
                                        <label>Plataforma:</label><br/>
                                        <select id="idPlataforma" name="idPlataforma" style="width:350px;">
                                            <option value=""></option>
                                            <option value="1">Pruebas</option>
                                            <option value="2">Producción</option>
                                            <option value="3">Pruebas y Producción</option>
                                        </select>
                                    </p>
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
     <script>
            mostrarCalendario();
     </script>       

    </body>
</html>
<%}%>