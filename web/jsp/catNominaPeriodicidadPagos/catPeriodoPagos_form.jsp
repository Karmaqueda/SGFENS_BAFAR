<%-- 
    Document   : catPeriodoPagos_form
    Created on : 11/12/2013, 10:58:43 AM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.bo.NominaPeriodicidadPagoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.NominaPeriodicidadPagoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.NominaPeriodicidadPago"%>
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
        int idNominaPeriodicidadPago = 0;
        try {
            idNominaPeriodicidadPago = Integer.parseInt(request.getParameter("idNominaPeriodicidadPago"));
        } catch (NumberFormatException e) {
        }

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        String newRandomPass = "";
        
        NominaPeriodicidadPagoBO nominaPeriodicidadPagoBO = new NominaPeriodicidadPagoBO(user.getConn());
        NominaPeriodicidadPago nominaPeriodicidadPagosDto = null;
        if (idNominaPeriodicidadPago > 0){
            nominaPeriodicidadPagoBO = new NominaPeriodicidadPagoBO(idNominaPeriodicidadPago,user.getConn());
            nominaPeriodicidadPagosDto = nominaPeriodicidadPagoBO.getNominaPeriodicidadPago();
        }else{
            System.out.println("-----ID de pagos no es mayor a 0");
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
                        url: "catPeriodoPagos_ajax.jsp",
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
                                            location.href = "catPeriodoPagos_list.jsp?pagina="+"<%=paginaActual%>";
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
                    <h1>Tipos de Pagos</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_periodicidad.png" alt="icon"/>
                                    <% if(nominaPeriodicidadPagosDto!=null){%>
                                    Editar NominaPeriodicidadPago ID <%=nominaPeriodicidadPagosDto!=null?nominaPeriodicidadPagosDto.getIdPeriodicidadPago():"" %>
                                    <%}else{%>
                                    NominaPeriodicidadPago
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idNominaPeriodicidadPago" name="idNominaPeriodicidadPago" value="<%=nominaPeriodicidadPagosDto!=null?nominaPeriodicidadPagosDto.getIdPeriodicidadPago():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>*Nombre:</label><br/>
                                        <input maxlength="30" type="text" id="nombreNominaPeriodicidadPago" name="nombreNominaPeriodicidadPago" style="width:300px"
                                               value="<%=nominaPeriodicidadPagosDto!=null?nominaPeriodicidadPagoBO.getNominaPeriodicidadPago().getNombre():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Descripción:</label><br/>
                                        <input maxlength="100" type="text" id="descripcionNominaPeriodicidadPago" name="descripcionNominaPeriodicidadPago" style="width:300px"
                                               value="<%=nominaPeriodicidadPagosDto!=null?nominaPeriodicidadPagoBO.getNominaPeriodicidadPago().getDescripcion():"" %>"/>
                                    </p>
                                    <br/>                                                                        
                                    
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=nominaPeriodicidadPagosDto!=null?(nominaPeriodicidadPagosDto.getIdEstatus()==1?"checked":""):"checked" %> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
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