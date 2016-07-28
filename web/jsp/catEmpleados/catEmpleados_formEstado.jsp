<%-- 
    Document   : catEmpleados_form.jsp
    Created on : 08-01-2013, 12:13:49
    Author     : Leonardo 
--%>

<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.bo.EstadoBO"%>
<%@page import="com.tsp.sct.bo.DispositivoMovilBO"%>
<%@page import="com.tsp.sct.dao.dto.DispositivoMovil"%>
<%@page import="com.tsp.sct.bo.EmpleadoRolBO"%>
<!-----------------<//%@page import="com.tsp.microfinancieras.jdbc.SgfensProspectoDaoImpl"%>-->
<!-----------------<//%@page import="com.tsp.microfinancieras.dto.SgfensProspecto"%>-->
<!-----------------<//%@page import="com.tsp.microfinancieras.bo.SGProspectoBO"%>-->
<!-----------------<//%@page import="com.tsp.microfinancieras.dto.SgfensEmpleadoVendedor"%>-->
<!-----------------<//%@page import="com.tsp.microfinancieras.bo.SGEmpleadoVendedorBO"%>-->
<!-----------------<//%@page import="com.tsp.microfinancieras.bo.RolesBO"%>-->
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.bo.PasswordBO"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el Empleado tiene acceso a este topico
    if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
        response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
        response.flushBuffer();
    } else {

        int idEmpresa = user.getUser().getIdEmpresa();
        
        /*
         * Parámetros
         */
        int idEmpleado = 0;
        try {
            idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
        } catch (NumberFormatException e) {
        }
        
        int idProspecto=-1;
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
        
        EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
        Empleado empleadosDto = null;
        if (idEmpleado > 0){
            empleadoBO = new EmpleadoBO(idEmpleado,user.getConn());
            empleadosDto = empleadoBO.getEmpleado();
        }else{
            newRandomPass = new PasswordBO().generateValidPassword();
        }
        
      /*  SGEmpleadoVendedorBO empleadoVendedorBO = null;
        SgfensEmpleadoVendedor empleadoVendedorDto = null;
        if (empleadosDto!=null){
            empleadoVendedorBO = new SGEmpleadoVendedorBO(idEmpleado);
            empleadoVendedorDto = empleadoVendedorBO.getEmpleadoVendedor();
        }
        
        SgfensProspecto prospectoDto = null;
        if (idProspecto>0){
            prospectoDto = new SGProspectoBO(idProspecto).getSgfensProspecto();
            try{
                prospectoDto.setIdEstatus(2); //Deshabilitado
                new SgfensProspectoDaoImpl(user.getConn()).update(prospectoDto.createPk(),prospectoDto);
            }catch(Exception ex){ ex.printStackTrace();}
        }*/
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
                                            <% if (!mode.equals("3")) {%>
                                                location.href = "catEmpleados_list.jsp";
                                            <%}else{%>
                                                parent.recargarSelectEmpleados();
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
            
            function apareceAutomovil(){   
                var seleccionadoRol = document.getElementById("idRolEmpleado").value;
                if(seleccionadoRol==2){                    
                    document.getElementById("automovilDisplay").style.display="block";
                }else{                    
                    document.getElementById("automovilDisplay").style.display="none";                    
                }
            }
            
        </script>
    </head>
    <body>
        <div class="content_wrapper">

            <% if (!mode.equals("3")) {%>
                <jsp:include page="../include/header.jsp" flush="true"/>
                <jsp:include page="../include/leftContent.jsp"/>
            <% } %>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Catálogos</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    
                                    <%if(empleadosDto.getIdEstado()==1||empleadosDto.getIdEstado()==4||empleadosDto.getIdEstado()==5){ 
                                     long s2 = (new Date()).getTime();
                                        long d2 = 0; 
                                        try{
                                            d2 = empleadosDto.getFechaHora().getTime();
                                        }catch(Exception e){}
                                        long diferencia = s2 - d2;                                
                                        if(diferencia < 300000){%>
                                        <img src="../../images/estatusEmpleado/icon_activoTrabajando.png" alt="icon"/>
                                        <%}else{%>
                                            <img src="../../images/estatusEmpleado/icon_desactivado.png"  alt="icon"/>
                                            <%}%>  
                                    <%}else if(empleadosDto.getIdEstado()==2||empleadosDto.getIdEstado()==8){
                                    %>
                                    <img src="../../images/estatusEmpleado/icon_descansoInactividad.png" alt="icon"/>
                                    <%}else if(empleadosDto.getIdEstado()==3||empleadosDto.getIdEstado()==6){ 
                                    %>
                                    <img src="../../images/estatusEmpleado/icon_servidorFallido.png" alt="icon"/>                                                                        
                                    <%}else if(empleadosDto.getIdEstado()==7){
                                    %>
                                    <img src="../../images/estatusEmpleado/icon_descansoInactividad.png" alt="icon"/>                                    
                                    <%}else if(empleadosDto.getIdEstado()==9){
                                        long s = (new Date()).getTime();
                                        long d = 0; 
                                        try{
                                            d = empleadosDto.getFechaHora().getTime();
                                        }catch(Exception e){}
                                        long diferencia = s - d;                                
                                        if(diferencia < 300000){
                                        %>
                                            <img src="../../images/estatusEmpleado/icon_activoTrabajando.png" alt="icon"/>
                                        <%}else{%>
                                            <img src="../../images/estatusEmpleado/icon_desactivado.png"  alt="icon"/>
                                            <%}%>
                                    <%}else{
                                    %>                                    
                                    <img src="../../images/estatusEmpleado/icon_users.png" alt="icon"/>
                                    <%}%>
                                    
                                    <% if(empleadosDto!=null){%>
                                    Cambiar Estado de Empleado ID <%=empleadosDto!=null?empleadosDto.getIdEmpleado():"" %>
                                    <%}else{%>
                                    Estado Empleado 
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idEmpleado" name="idEmpleado" value="<%=empleadosDto!=null?empleadosDto.getIdEmpleado():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>Número de Empleado:</label><br/>
                                        <input maxlength="50" type="text" id="numEmpleado" name="numEmpleado" style="width:300px"
                                               value="<%=empleadosDto!=null?empleadoBO.getEmpleado().getNumEmpleado():"" %>" readonly="true"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Nombre:</label><br/>
                                        <input maxlength="70" type="text" id="nombreEmpleado" name="nombreEmpleado" style="width:300px"
                                               value="<%=empleadosDto!=null?empleadoBO.getEmpleado().getNombre():"" %>" readonly="true"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Apellido Paterno:</label><br/>
                                        <input maxlength="70" type="text" id="apellidoPaternoEmpleado" name="apellidoPaternoEmpleado" style="width:300px"
                                               value="<%=empleadosDto!=null?empleadoBO.getEmpleado().getApellidoPaterno():"" %>" readonly="true"/>
                                    </p>
                                    <br/>                                    
                                    <p>
                                        <label>Apellido Materno:</label><br/>
                                        <input maxlength="70" type="text" id="apellidoMaternoEmpleado" name="apellidoMaternoEmpleado" style="width:300px"
                                               value="<%=empleadosDto!=null?empleadoBO.getEmpleado().getApellidoMaterno():"" %>" readonly="true"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Estado:</label><br/>
                                        <select size="1" id="idEstadoEmpleado" name="idEstadoEmpleado">
                                            <option value="-1">Selecciona un Estado</option>
                                                <%
                                                    out.print(new EstadoBO(user.getConn()).getEstadosByIdHTMLCombo(idEmpresa, (empleadosDto!=null?empleadoBO.getEmpleado().getIdEstado():-1) ));                                                    
                                                %>
                                        </select>                                        
                                    </p> 
                                    <br/> 
                                                                        
                                    <br/><br/>
                                    
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