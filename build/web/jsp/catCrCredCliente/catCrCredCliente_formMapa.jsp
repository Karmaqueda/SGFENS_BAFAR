<%-- 
    Document   : catCrCredCliente_formMapa
    Created on : 22/07/2016, 12:13:17 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.dao.dto.CrCredCliente"%>
<%@page import="com.tsp.sct.bo.CrCredClienteBO"%>
<%@page import="java.util.StringTokenizer"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el crCredCliente tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {

        int idEmpresa = user.getUser().getIdEmpresa();
        
        /*
         * Parámetros
         */
        int idCrCredCliente = 0;
        try {
            idCrCredCliente = Integer.parseInt(request.getParameter("idCrCredCliente"));
        } catch (NumberFormatException e) {
        }

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         *   3 = nuevo (modalidad PopUp [cotizaciones, pedidos, facturas]) 
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        
        CrCredClienteBO crCredClienteBO = new CrCredClienteBO(user.getConn());
        CrCredCliente crCredClientesDto = null;
        if (idCrCredCliente > 0){
            crCredClienteBO = new CrCredClienteBO(idCrCredCliente, user.getConn());
            crCredClientesDto = crCredClienteBO.getCrCredCliente();
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
                    <h1>Ubicación</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_crCredCliente.png" alt="icon"/>
                                    <% if(crCredClientesDto!=null){%>
                                    Localización de Cliente Credito ID <%=crCredClientesDto!=null?crCredClientesDto.getIdCredCliente():"" %>
                                    <%}else{%>
                                    Cliente Credito  Localización
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idCrCredCliente" name="idCrCredCliente" value="<%=crCredClientesDto!=null?crCredClientesDto.getIdCredCliente():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="dirMapa" />
                                    <p>
                                        <label>RFC:</label>
                                        <%=crCredClientesDto!=null?crCredClienteBO.getCrCredCliente().getRfc():"" %>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Nombre:</label>
                                        <%= crCredClienteBO.getNombreCompleto(true) %>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Latitud:</label>
                                        <%=crCredClientesDto!=null?(crCredClientesDto.getLatitud()!=0?"" + crCredClientesDto.getLatitud():"Sin Ubicación Asignada"):"" %>                                   
                                        &nbsp;&nbsp;&nbsp;
                                        <label>Longitud:</label>
                                        <%=crCredClientesDto!=null?(crCredClientesDto.getLongitud()!=0?"" + crCredClientesDto.getLongitud():"Sin Ubicación Asignada"):"" %>                                   
                                    </p>
                                    
                                    
                            </div>
                                    </div>
                            <!------------->
                        
                        
                        <!-- contenido de columna derecha Mapa-->
                        <div class="column_right">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_movimiento.png" alt="icon"/>                                    
                                    Mapa                                     
                                </span>
                            </div>
                            <br class="clear"/>
                            
                            <div class="content">                                                                                 
                                <jsp:include page="../include/Mapa_CredClienteLocalizacion.jsp"/>  
                                <br/>   
                                    <p>
                                    <label>Dirección Seleccionada*:</label><br/>
                                    <input maxlength="100" type="text" id="direccion" name="direccion" style="width:470px" readonly/>
                                    *Si desea usar esta dirección, Copie y Pegue en los campos correspondientes.
                                </p>
                                <br/> 
                            </div>
                        </div>
                        <!-- fin de contenido de columna derecha Mapa-->
                        
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