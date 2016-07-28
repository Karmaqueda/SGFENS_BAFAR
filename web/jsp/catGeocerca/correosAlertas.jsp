<%-- 
    Document   : correosAlertas
    Created on : 8/05/2014, 11:03:05 AM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.dao.dto.GeocercasNotificaciones"%>
<%@page import="com.tsp.sct.bo.GeocercaNotificacionBO"%>
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
        int idGeoNotificacion = 0;
        try {
            idGeoNotificacion = Integer.parseInt(request.getParameter("idGeoNotificacion"));
        } catch (NumberFormatException e) {
        }

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        
        
        GeocercaNotificacionBO geoNotificacionBO = new GeocercaNotificacionBO(user.getConn());
//        GeocercasNotificaciones geoNotificacionesDto = new GeocercasNotificaciones();
        GeocercasNotificaciones geoNotificacionesDto = null;
        //if (idGeoNotificacion > 0){
            //geoNotificacionBO = new GeocercaNotificacionBO(idGeoNotificacion, user.getConn());
        try{
            geoNotificacionesDto = geoNotificacionBO.getGeocercasNotificacionesGenericoByEmpresa2(idEmpresa);
        }catch(Exception e){}
            
        //}       
        //String parameter1 = "idGeoNotificacion";
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
                        url: "correosAlertas_ajax.jsp",
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
                                            //location.href = "catGeocercas_list.jsp";
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
        </script>
        <script type="text/javascript">
            
            function mostrarCalendario(){
                $( "#fecha_caducidad" ).datepicker({
                        minDate: +1,
                        gotoCurrent: true,
                        changeMonth: true,
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 100);
                            }, 500)}
                });
            }
            
        </script>
    </head>
    <body class="nobg">
                
                    <h1>Notificaciones</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                        <div class="onecolumn">
                        <div class="column_left" style="width: 99%; overflow-x:hidden; ">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_email.png" alt="icon"/>
                                    <% if(geoNotificacionesDto!=null){%>
                                    Editar Correos ID <%=geoNotificacionesDto!=null?geoNotificacionesDto.getIdGeoNotificacion():"" %>
                                    <%}else{%>
                                    Correos
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idGeoNotificacion" name="idGeoNotificacion" value="<%=geoNotificacionesDto!=null?geoNotificacionesDto.getIdGeoNotificacion():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=2%>" />
                                    <p>
                                        <label>*Correos(separados por coma):</label><br/>
                                        <input maxlength="500" type="text" id="correosGeoNotificacion" name="correosGeoNotificacion" style="width:500px"
                                               value="<%=geoNotificacionesDto!=null?geoNotificacionesDto.getCorreos():"" %>"
                                               />
                                    </p>
                                    <br/>                                    
                                                            
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=geoNotificacionesDto!=null?(geoNotificacionesDto.getIdEstatus()==1?"checked":""):"checked" %> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
                                    </p>
                                    
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
                        
                    </div>
                    </form>
                    <!--TODO EL CONTENIDO VA AQUÍ-->

                               
        <script>
            mostrarCalendario();
        </script>
    </body>
</html>
<%}%>
