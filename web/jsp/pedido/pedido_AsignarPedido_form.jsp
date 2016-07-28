<%-- 
    Document   : pedido_AsignarPedido
    Created on : 6/11/2014, 06:13:59 PM
    Author     : 578
--%>


<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.SGPedidoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedido"%>
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
    
    
    int idPedido = -1;
    try{ idPedido = Integer.parseInt(request.getParameter("idPedido")); }catch(NumberFormatException e){}  
    
    SgfensPedido pedidoDto = new SGPedidoBO(idPedido,user.getConn()).getPedido();    
        
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
                        url: "pedido_ajax.jsp",
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
                                            //location.href = "pedido_AsignarPedido_form.jsp?idPedido=<%=idPedido%>";
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
                                            <% if(pedidoDto!=null){%>
                                            Asignar Pedido ID <%=pedidoDto!=null?pedidoDto.getIdPedido():"" %>                                            
                                            <%}%>
                                        </span>
                                    </div>
                                    <br class="clear"/>
                                    <div class="content">
                                            <input type="hidden" id="id_pedido" name="id_pedido" value="<%=pedidoDto!=null?pedidoDto.getIdPedido():"" %>" />
                                            <input type="hidden" id="mode" name="mode" value="<%=26%>" />
                                            <p>
                                                <label>*Vendedor Asignado</label><br/>
                                                <select id="idVendedorAsignado" name="idVendedorAsignado" class="flexselect">
                                                    <option></option>
                                                    <% if(pedidoDto.getIdUsuarioVendedorAsignado()<= 0){ %>                                                    
                                                        <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL, 0) %> 
                                                        <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_CONDUCTOR_VENDEDOR, 0) %>
                                                        <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_COBRADOR, 0) %>
                                                    <%}else{%>
                                                        <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL, pedidoDto.getIdUsuarioVendedorAsignado()) %> 
                                                        <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_CONDUCTOR_VENDEDOR, pedidoDto.getIdUsuarioVendedorAsignado()) %>
                                                        <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_COBRADOR, pedidoDto.getIdUsuarioVendedorAsignado()) %>
                                                    <%}%>
                                                </select>
                                            </p>
                                            <br/>
                                            <p>
                                                <label>*Conductor Asignado</label><br/>      
                                                <select id="idConductorAsignado" name="idConductorAsignado" class="flexselect">
                                                <option></option>                                                
                                                    <% if(pedidoDto.getIdUsuarioConductorAsignado() <= 0){ %>
                                                         <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_CONDUCTOR, 0) %>
                                                         <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_CONDUCTOR_VENDEDOR, 0) %>
                                                    <%}else{%>
                                                         <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_CONDUCTOR, pedidoDto.getIdUsuarioConductorAsignado()) %>
                                                         <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_CONDUCTOR_VENDEDOR, pedidoDto.getIdUsuarioConductorAsignado()) %>
                                                    <%}%>
                                                </select>
                                            </p>                                           
                                            <br/>
                                            <p>
                                                <label>*Compartir Pedido Con:</label><br/>
                                                <label>(Asegurate que el vendor al que se comparte el pedido tenga los productos en existencia)</label><br/>
                                                <select id="idVendedorCompartido" name="idVendedorCompartido" class="flexselect">
                                                    <option></option>
                                                    <% if(pedidoDto.getIdUsuarioVendedorAsignado()<= 0){ %>                                                    
                                                        <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL, 0) %> 
                                                        <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_CONDUCTOR_VENDEDOR, 0) %>
                                                        <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_COBRADOR, 0) %>
                                                    <%}else{%>
                                                        <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL, pedidoDto.getIdUsuarioVendedorAsignado()) %> 
                                                        <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_CONDUCTOR_VENDEDOR, pedidoDto.getIdUsuarioVendedorAsignado()) %>
                                                        <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_COBRADOR, pedidoDto.getIdUsuarioVendedorAsignado()) %>
                                                    <%}%>
                                                </select>
                                            </p>
                                            <!--<br/>
                                            <p>
                                                <label>Asegurate que el vendor al que se comparte el pedido tenga los productos en existencia</label>
                                            </p>-->
                                            <br/>
                                            <p>
                                                <input type="checkbox" class="checkbox" id="compartidoFecha" name="compartidoFecha" value="1" onclick="validarCompartidoFecha();"> <label for="compartidoFecha">Limitar Compartido con Fecha</label>
                                            </p>
                                            <br/>
                                            <p id="fechaCompartir" style="display: none">
                                                <label>Compartir Hasta:</label><br/>
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