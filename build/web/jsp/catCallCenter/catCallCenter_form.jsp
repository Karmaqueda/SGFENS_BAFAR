<%-- 
    Document   : catCallCenter_form
    Created on : 23/02/2016, 05:02:01 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensClienteVendedor"%>
<%@page import="com.tsp.sct.bo.SGClienteVendedorBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.bo.PasswordBO"%>
<%@page import="com.tsp.sct.bo.CallCenterBO"%>
<%@page import="com.tsp.sct.dao.jdbc.CallCenterDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CallCenter"%>
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
        int idCallCenter = 0;
        try {
            idCallCenter = Integer.parseInt(request.getParameter("idCallCenter"));
        } catch (NumberFormatException e) {
        }

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        
        CallCenterBO callCenterBO = new CallCenterBO(user.getConn());
        CallCenter callCentersDto = null;
        if (idCallCenter > 0){
            callCenterBO = new CallCenterBO(idCallCenter,user.getConn());
            callCentersDto = callCenterBO.getCallCenter();
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
                        url: "catCallCenter_ajax.jsp",
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
                                            location.href = "catCallCenter_list.jsp?pagina="+"<%=paginaActual%>";
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
            
            function muestraOcultaNoCliente(){
                if ($('#noEsCliente').attr('checked')) {                    
                    document.getElementById("divDatosNoCliente").style.display="block"; //para setear una propiedad de css
                }
                else {                    
                    document.getElementById("divDatosNoCliente").style.display="none"; //para setear una propiedad de css
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
                    <h1>Call Center</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_callCenter.png" alt="icon"/>
                                    <% if(callCentersDto!=null){%>
                                    Editar Call Center <%=callCentersDto!=null?" del ticket " + callCentersDto.getNumeroTicket():"" %>
                                    <%}else{%>
                                    Call Center
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idCallCenter" name="idCallCenter" value="<%=callCentersDto!=null?callCentersDto.getIdCallCenter():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    
                                    <%if(callCentersDto==null){%>
                                    <p>
                                        <label>Cliente:</label><br/>
                                        <select id="q_idcliente" name="q_idcliente" class="flexselect">
                                            <option value="-1" ></option>
                                            <%= new ClienteBO(user.getConn()).getClientesByIdHTMLCombo(idEmpresa, -1," AND ID_ESTATUS <> 2 " ) %>
                                        </select>
                                    </p>
                                    <br/>
                                    <p>
                                        <input type="checkbox" class="checkbox" id="noEsCliente" name="noEsCliente" value="1" onclick="muestraOcultaNoCliente();"> <label for="noEsCliente">No es Cliente</label>
                                    </p>
                                    <br/>
                                    
                                    <div id="divDatosNoCliente" style="display: none;">                                        
                                        <p>
                                            <label>*Nombre:</label><br/>
                                            <input maxlength="40" type="text" id="nombreCallCenter" name="nombreCallCenter" style="width:300px"
                                                   value="<%=callCentersDto!=null?callCenterBO.getCallCenter().getNombre():"" %>"/>
                                        </p>
                                        <br/>
                                        <p>
                                            <label>*Apellido Paterno:</label><br/>
                                            <input maxlength="50" type="text" id="aPaternoCallCenter" name="aPaternoCallCenter" style="width:300px"
                                                   value="<%=callCentersDto!=null?callCenterBO.getCallCenter().getApellidoPaterno():"" %>"/>
                                        </p>
                                        <br/>
                                        <p>
                                            <label>Apellido Materno:</label><br/>
                                            <input maxlength="50" type="text" id="aMaternoCallCenter" name="aMaternoCallCenter" style="width:300px"
                                                   value="<%=callCentersDto!=null?callCenterBO.getCallCenter().getApellidoMaterno():"" %>"/>
                                        </p>
                                        <br/>
                                        <p>
                                            <label>*Teléfono:</label><br/>
                                            <input maxlength="50" type="text" id="telefonoCallCenter" name="telefonoCallCenter" style="width:300px"
                                                   value="<%=callCentersDto!=null?callCenterBO.getCallCenter().getTelefono():"" %>"/>
                                        </p>
                                        <br/>
                                        <p>
                                            <label>Correo:</label><br/>
                                            <input maxlength="60" type="text" id="correoCallCenter" name="correoCallCenter" style="width:300px"
                                                   value="<%=callCentersDto!=null?callCenterBO.getCallCenter().getCorreo():"" %>"/>
                                        </p>
                                        <br/>
                                    </div>
                                    
                                    <p>
                                        <label>Tipo:</label><br/>
                                        <select size="1" id="idTipoCallCenter" name="idTipoCallCenter" >                                            
                                            <option value="1" >Solicitud</option>
                                            <option value="2" >Queja</option>
                                            <option value="3" >Sugerencia</option>                                           
                                        </select>                                        
                                    </p>
                                    <br/>
                                    <%}%>
                                    <p>
                                        <label>*Descripción:</label><br/>
                                        <input maxlength="450" type="text" id="descripcionCallCenter" name="descripcionCallCenter" style="width:300px"
                                               value="<%=callCentersDto!=null?callCenterBO.getCallCenter().getDescripcion():"" %>"/>
                                    </p>
                                    <br/>                                                                        
                                    
                                    
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

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>

        <script>
            //mostrarCalendario();
            $("select.flexselect").flexselect();
        </script>
    </body>
</html>
<%}%>