<%-- 
    Document   : catPuestos_form
    Created on : 9/12/2013, 06:54:07 PM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.bo.NominaPuestoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.NominaPuestoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.NominaPuesto"%>
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
        int idNominaPuesto = 0;
        try {
            idNominaPuesto = Integer.parseInt(request.getParameter("idNominaPuesto"));
        } catch (NumberFormatException e) {
        }

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        String newRandomPass = "";
        
        NominaPuestoBO nominaPuestoBO = new NominaPuestoBO(user.getConn());
        NominaPuesto nominaPuestosDto = null;
        if (idNominaPuesto > 0){
            nominaPuestoBO = new NominaPuestoBO(idNominaPuesto,user.getConn());
            nominaPuestosDto = nominaPuestoBO.getNominaPuesto();
        }else{
            System.out.println("-------ID DE PUESTO ES MENOR O IGUAL A 0");
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
                        url: "catPuestos_ajax.jsp",
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
                                            location.href = "catPuestos_list.jsp?pagina="+"<%=paginaActual%>";
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
    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Puesto</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_puesto.png" alt="icon"/>
                                    <% if(nominaPuestosDto!=null){%>
                                    Editar Puesto ID <%=nominaPuestosDto!=null?nominaPuestosDto.getIdPuesto():"" %>
                                    <%}else{%>
                                    Puesto
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idNominaPuesto" name="idNominaPuesto" value="<%=nominaPuestosDto!=null?nominaPuestosDto.getIdPuesto():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>*Nombre:</label><br/>
                                        <input maxlength="30" type="text" id="nombreNominaPuesto" name="nombreNominaPuesto" style="width:300px"
                                               value="<%=nominaPuestosDto!=null?nominaPuestoBO.getNominaPuesto().getNombre():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Descripción:</label><br/>
                                        <input maxlength="450" type="text" id="descripcionNominaPuesto" name="descripcionNominaPuesto" style="width:300px"
                                               value="<%=nominaPuestosDto!=null?nominaPuestoBO.getNominaPuesto().getDescripcion():"" %>"/>
                                    </p>
                                    <br/>   
                                    <p>
                                        <label>*Sueldo:</label><br/>
                                        <input maxlength="15" type="text" id="sueldoNominaPuesto" name="sueldoNominaPuesto" style="width:300px"
                                               value="<%=nominaPuestosDto!=null?nominaPuestoBO.getNominaPuesto().getSueldoDiarioIntegrado():"" %>"/>
                                    </p>
                                    <br/>   
                                    
                                    
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=nominaPuestosDto!=null?(nominaPuestosDto.getIdEstatus()==1?"checked":""):"checked" %> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
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

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>


    </body>
</html>
<%}%>
