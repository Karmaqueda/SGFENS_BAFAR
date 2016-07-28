<%-- 
    Document   : catFoliosCBB_form
    Created on : 24/05/2013, 06:18:26 PM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.FoliosBO"%>
<%@page import="com.tsp.sct.dao.jdbc.FoliosDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Folios"%>
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
        
        FoliosBO foliosBO = new FoliosBO(user.getConn());
        Folios foliossDto = null;
        if (idFolios > 0){
            foliosBO = new FoliosBO(idFolios,user.getConn());
            foliossDto = foliosBO.getFolios();
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
                        url: "catFoliosCBB_ajax.jsp",
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
                                            location.href = "catFoliosCBB_list.jsp?pagina="+"<%=paginaActual%>";
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
            
            function mostrarCalendario(){
                $( "#aprobacionFechaCBB" ).datepicker({
                        //minDate: +1,
                        
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
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <% if(foliossDto!=null){%>
                        <h1>Serie para facturar con movil</h1>
                    <%}else{%>
                        <h1>Catálogos</h1>
                    <%}%>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_info.png" alt="icon"/>
                                    <% if(foliossDto!=null){%>
                                    Esta empresa/sucursal, <%=foliossDto!=null?foliossDto.getSerie():"" %>
                                    <%}else{%>
                                    Folios
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idFolios" name="idFolios" value="<%=foliossDto!=null?foliossDto.getIdFolio():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                     <% if(foliossDto==null){%>
                                    <p>
                                        <label>Serie:</label><br/>
                                        <input maxlength="10" type="text" id="serieCBB" name="serieCBB" style="width:300px"
                                               value="<%=foliossDto!=null?foliosBO.getFolios().getSerie():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Rango Inicio:</label><br/>
                                        <input maxlength="100" type="text" id="rangoInicioCBB" name="rangoInicioCBB" style="width:300px"
                                               value="<%=foliossDto!=null?foliosBO.getFolios().getFolioDesde():"" %>"/>
                                    </p>
                                    <br/>                                                                        
                                    <p>
                                        <label>*Rango Final:</label><br/>
                                        <input maxlength="100" type="text" id="rangoFinalCBB" name="rangoFinalCBB" style="width:300px"
                                               value="<%=foliossDto!=null?foliosBO.getFolios().getFolioHasta():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Folio Actual:</label><br/>
                                        <input maxlength="100" type="text" id="folioActualCBB" name="folioActualCBB" style="width:300px"
                                               value="<%=foliossDto!=null?foliosBO.getFolios().getUltimoFolio():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*# Secofi:</label><br/>
                                        <input maxlength="100" type="text" id="secofiCBB" name="secofiCBB" style="width:300px"
                                               value="<%=foliossDto!=null?foliosBO.getFolios().getSecofi():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Fecha Aprobación:</label><br/>
                                        <input type="text" title="Fecha Aprobacion" class="" style="width: 99px; float: left; "
                                                                               value="<%=foliossDto!=null?foliosBO.getFolios().getFechaVigencia():""%>" id="aprobacionFechaCBB" name="aprobacionFechaCBB"/>
                                    </p>
                                    <br/><br/>
                                    <%}else{%>
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=foliossDto!=null?(foliossDto.getFacturacionMovil()==1?"checked":""):"checked" %> id="facturarMovil" name="facturarMovil" value="1"> <label for="facturarMovil">Activar para Movil</label>
                                    </p>                                    
                                    <br/><br/>
                                    <%}%>
                                    
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
            mostrarCalendario();
    </script>

    </body>
</html>
<%}%>