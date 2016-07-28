<%-- 
    Document   : catEmpleados_Asignados_form
    Created on : 13/04/2015, 01:14:23 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    
    int idEmpleado = -1;
    try{ idEmpleado = Integer.parseInt(request.getParameter("idEmpleado")); }catch(NumberFormatException e){}  
    
    Empleado empleadoDto = new EmpleadoBO(idEmpleado,user.getConn()).getEmpleado();    
        
    int idEmpresa = user.getUser().getIdEmpresa();
    
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
    
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
                
                    $.ajax({
                        type: "POST",
                        url: "catEmpleados_Asignados_ajax.jsp",
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
                                            //location.href = "empleado_AsignarEmpleado_form.jsp?idEmpleado=<%=idEmpleado%>";
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

            function mostrarCalendario(){ 
                $( "#q_fh_min" ).datepicker({
                        minDate: 0,
                        gotoCurrent: true,
                        changeMonth: true,
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 100);
                            }, 500)}
                });
            }
            
            
            function validarCompartidoFecha(){
                if ($('#compartidoFecha').attr('checked')) {                    
			document.getElementById("fechaCompartir").style.display="block"; //para setear una propiedad de css
                }
                else {                    
                    document.getElementById("fechaCompartir").style.display="none"; //para setear una propiedad de css
                }
            }   
            
        </script>
           
            
    </head>
    <body class="nobg">
            <!-- Inicio de Contenido -->           
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>                    
                   
                        <div class="column_left" style="width: 100%; height: 100%; ">
                            <form id="frm_action" name="frm_action" action="" method="post">
                                <div class="header">
                                        <span>
                                            <img src="../../images/zone2_16.png" alt="icon"/>
                                            <% if(empleadoDto!=null){%>
                                            Asignar Empleado ID <%=empleadoDto!=null?empleadoDto.getIdEmpleado():"" %>                                            
                                            <%}%>
                                        </span>
                                    </div>
                                    <br class="clear"/>
                                    <div class="content">
                                            <input type="hidden" id="idUsuarioEmpleado" name="idUsuarioEmpleado" value="<%=empleadoDto!=null?empleadoDto.getIdUsuarios():"" %>" />
                                            <input type="hidden" id="mode" name="mode" value="" />
                                            <p>
                                                <label>*Vendedor:</label><br/>
                                                <select id="idVendedorAsignado" name="idVendedorAsignado" class="flexselect">
                                                    <option></option>                                                                                                       
                                                        <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL, 0) %>                                                        
                                                        <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_CONDUCTOR_VENDEDOR, 0) %>
                                                </select>
                                            </p>
                                            <br/>                                            
                                            <p>
                                                <input type="checkbox" class="checkbox" id="compartidoFecha" name="compartidoFecha" value="1" onclick="validarCompartidoFecha();"> <label for="compartidoFecha">Limitar Asignación con Fecha</label>
                                            </p>
                                            <br/>
                                            <p id="fechaCompartir" style="display: none">
                                                <label>Asignar Hasta:</label><br/>
                                                <input type="text" id="q_fh_min" name="q_fh_min" 
                                                       value=""/>
                                            </p>  
                                            <br/>
                                            <br/>
                                            <div id="action_buttons">
                                                <p>
                                                    <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
                                                    <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                                </p>
                                            </div>

                                    </div>
                                
                           
                            </form>                                            
                        </div>
                    
            <!-- Fin de Contenido-->       
    <script>
        mostrarCalendario();    
     </script>
    </body>
</html>
<%}%>