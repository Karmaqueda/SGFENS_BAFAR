<%-- 
    Document   : catFoliosMovil_form
    Created on : 21/12/2015, 04:13:14 PM
    Author     : ISC Cesar Martinez
--%>

<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.FoliosMovilEmpleadoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.FoliosMovilEmpleadoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.FoliosMovilEmpleado"%>
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
        int idFolios = 0;
        try {
            idFolios = Integer.parseInt(request.getParameter("idFolios"));
        } catch (NumberFormatException e) {
        }

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";       
        
        FoliosMovilEmpleadoBO foliosBO = new FoliosMovilEmpleadoBO(user.getConn());
        FoliosMovilEmpleado foliosDto = null;
        if (idFolios > 0){
            foliosBO = new FoliosMovilEmpleadoBO(idFolios,user.getConn());
            foliosDto = foliosBO.getFoliosMovilEmpleado();
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
                        url: "catFoliosMovil_ajax.jsp",
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
                                        location.href = "catFoliosMovil_list.jsp?pagina="+"<%=paginaActual%>";
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
                    <h1>Folios de Control Interno M&oacute;vil</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_folio_movil.png" alt="icon"/>
                                    <% if(foliosDto!=null &&  !mode.equals("")  ){%>
                                    Editar Serie Control Interno Movil ID: <%=foliosDto!=null?foliosDto.getIdFolioMovilEmpleado():"" %>
                                    <%}else{%>
                                    Nueva Serie
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                <input type="hidden" id="idFolios" name="idFolios" value="<%=foliosDto!=null?foliosDto.getIdFolioMovilEmpleado():"" %>" />
                                <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                <p>
                                    <label>Serie:</label><br/>
                                    <input maxlength="10" type="text" id="serieCInterno" name="serieCInterno" style="width:300px"
                                           value="<%=foliosDto!=null?foliosDto.getSerie():"" %>"/>
                                </p>
                                <br/>
                                <p>
                                    <label>*Rango Inicio:</label><br/>
                                    <input maxlength="100" type="text" id="rangoInicioCInterno" name="rangoInicioCInterno" style="width:300px"
                                           value="<%=foliosDto!=null?foliosDto.getFolioDesde():"" %>"/>
                                </p>
                                <br/>                                                                        
                                <!--<p>
                                    <label>*Rango Final:</label><br/>
                                    <input maxlength="100" type="text" id="rangoFinalCInterno" name="rangoFinalCInterno" style="width:300px"
                                           value="<%=foliosDto!=null?foliosDto.getFolioHasta():"" %>"/>
                                </p>
                                <br/>
                                -->
                                <p>
                                    <label>Folio Actual:</label><br/>
                                    <input maxlength="100" type="text" id="folioActualCInterno" name="folioActualCInterno" style="width:300px"
                                           value="<%=foliosDto!=null?foliosDto.getUltimoFolio():"" %>" readonly/>
                                </p>
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