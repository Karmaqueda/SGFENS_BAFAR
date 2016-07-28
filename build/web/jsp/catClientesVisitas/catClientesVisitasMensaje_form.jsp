<%-- 
    Document   : catClientesVisitasMensaje_form
    Created on : 4/11/2014, 11:28:04 AM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
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
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
                
        EmpresaBO empresaBO = new EmpresaBO(user.getConn());
        Empresa empresasDto = null;
        if (idEmpresa > 0){
            empresaBO = new EmpresaBO(idEmpresa,user.getConn());
            empresasDto = empresaBO.getEmpresa();
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
                        url: "catClientesVisitasMensaje_ajax.jsp",
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
                                            location.href = "catClientesVisitas_list.jsp?pagina="+"<%=paginaActual%>";
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
                    <h1>Mensaje</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_empresa.png" alt="icon"/>
                                    <% if(empresasDto!=null){%>
                                    Editar Mensaje en Ticket, Empresa ID <%=empresasDto!=null?empresasDto.getIdEmpresa():"" %>
                                    <%}else{%>
                                    Mensaje Ticket
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idEmpresa" name="idEmpresa" value="<%=empresasDto!=null?empresasDto.getIdEmpresa():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=1%>" />
                                    <p>
                                        <label>*RFC:</label><br/>
                                        <input maxlength="30" type="text" id="rfcEmpresa" name="rfcEmpresa" style="width:300px"
                                               value="<%=empresasDto!=null?empresaBO.getEmpresa().getRfc():"" %>" readonly/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Nombre Comercial:</label><br/>
                                        <input maxlength="100" type="text" id="nombreComercialEmpresa" name="nombreComercialEmpresa" style="width:300px"
                                               value="<%=empresasDto!=null?empresaBO.getEmpresa().getNombreComercial():"" %>" readonly/>
                                    </p>
                                    <br/> 
                                    <p>
                                        <label>*Mensaje Personalizado:</label><br/>
                                        <input maxlength="300" type="text" id="mensajePersonalizadoTicketEmpresa" name="mensajePersonalizadoTicketEmpresa" style="width:300px"
                                               value="<%=empresasDto!=null?empresaBO.getEmpresa().getMensajePersonalizadoVisita():"" %>"/>
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