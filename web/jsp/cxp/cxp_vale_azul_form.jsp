<%-- 
    Document   : cxp_vale_azul_form
    Created on : 13/04/2015, 13/04/2015 04:58:22 PM
    Author     : ISCesar
--%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.bo.FoliosBO"%>
<%@page import="com.tsp.sct.dao.dto.CxpValeAzul"%>
<%@page import="com.tsp.sct.bo.CxpValeAzulBO"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
//Verifica si el cliente tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {

        int paginaActual = 1;
        try {
            paginaActual = Integer.parseInt(request.getParameter("pagina"));
        } catch (Exception e) {
        }

        int idEmpresa = user.getUser().getIdEmpresa();

        /*
         * Parámetros
         */
        int idCxPValeAzul = 0;
        try {
            idCxPValeAzul = Integer.parseInt(request.getParameter("id_cxp_vale_azul"));
        } catch (NumberFormatException e) {}

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";

        CxpValeAzulBO cxpValeAzulBO = new CxpValeAzulBO(user.getConn());
        CxpValeAzul cxpValeAzulDto = null;
        if (idCxPValeAzul > 0) {
            cxpValeAzulBO = new CxpValeAzulBO(idCxPValeAzul, user.getConn());
            cxpValeAzulDto = cxpValeAzulBO.getCxpValeAzul();
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

            function grabar() {
                if (validar()) {
                    $.ajax({
                        type: "POST",
                        url: "cxp_vale_azul_ajax.jsp",
                        data: $("#frm_action").serialize(),
                        beforeSend: function (objeto) {
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function (datos) {
                            if (datos.indexOf("--EXITO-->", 0) > 0) {
                                $("#ajax_message").html(datos);
                                $("#ajax_loading").fadeOut("slow");
                                $("#ajax_message").fadeIn("slow");
                                apprise('<center><img src=../../images/info.png> <br/>' + datos + '</center>', {'animate': true},
                                        function (r) {
                                            location.href = "cxp_comprobantes_list.jsp?pagina=" + "<%=paginaActual%>";
                                        });
                            } else {
                                $("#ajax_loading").fadeOut("slow");
                                $("#ajax_message").html(datos);
                                $("#ajax_message").fadeIn("slow");
                                $("#action_buttons").fadeIn("slow");
                                $.scrollTo('#inner', 800);
                            }
                        }
                    });
                }
            }

            function validar() {
                /*
                 if(jQuery.trim($("#nombre").val())==""){
                 apprise('<center><img src=../../images/warning.png> <br/>El dato "nombre de contacto" es requerido</center>',{'animate':true});
                 $("#nombre_contacto").focus();
                 return false;
                 }
                 */
                if(jQuery.trim($("#id_serie").val())<=0){
                    apprise('<center><img src=../../images/warning.png> <br/>Debe seleccionar una Serie, para generar un Folio único al comprobante.</center>',{'animate':true});
                    $("#id_serie").focus();
                    return false;
                 }
                return true;
            }
            
            function mostrarCalendario(){
                $( "#fecha_control" ).datepicker({
                        //minDate: 0,
                        gotoCurrent: true,
                        changeMonth: true,
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 100);
                            }, 500)}
                });
                $( "#fecha_pago" ).datepicker({
                        //minDate: 0,
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
                    <h1>Cuentas por Pagar</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                        <div class="twocolumn">
                            <div class="column_left">
                                <div class="header">
                                    <span>
                                        <img src="../../images/icon_marca.png" alt="icon"/>
                                        <% if (cxpValeAzulDto != null) {%>
                                        Editar Vale Azul ID <%=cxpValeAzulDto != null ? cxpValeAzulDto.getIdCxpValeAzul(): ""%>
                                        <%} else {%>
                                        Nuevo Vale Azul
                                        <%}%>
                                    </span>
                                </div>
                                <br class="clear"/>
                                <div class="content">
                                    <input type="hidden" id="id_cxp_vale_azul" name="id_cxp_vale_azul" value="<%=cxpValeAzulDto != null ? cxpValeAzulDto.getIdCxpValeAzul(): ""%>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>*Serie:</label><br/>
                                        <select name="id_serie" id="id_serie" 
                                                onchange="toggleDivFolio(this.value);">
                                            <option value="-1">Sin Serie</option>
                                            <%= new FoliosBO(user.getConn()).getFoliosByIdHTMLCombo(idEmpresa, (cxpValeAzulDto!=null?cxpValeAzulDto.getIdFolio():-1) ) %>
                                        </select>
                                    </p>
                                    <br/> 
                                    <p>
                                        <label>Folio:</label><br/>
                                        <input type="text" name="folio" id="folio" readonly
                                               value="<%= cxpValeAzulDto!=null?StringManage.getValidString(cxpValeAzulDto.getFolioGenerado()):"Sin Asignar" %>"
                                                style="width: 80px;"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Concepto:</label><br/>
                                        <textarea id="concepto" name="concepto" cols="40" rows="4"
                                                  onKeyUp="return textArea_maxLen(this,500);"><%=cxpValeAzulDto != null ? cxpValeAzulDto.getConcepto(): ""%></textarea>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Importe ($):</label><br/>
                                        <input maxlength="10" type="text" id="importe" name="importe" style="width:300px"
                                               value="<%=cxpValeAzulDto!=null?cxpValeAzulDto.getImporte():"0" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Fecha aplicación:</label><br/>
                                        <input type="text" name="fecha_control" id="fecha_control" readonly
                                            value="<%= DateManage.formatDateToNormal(cxpValeAzulDto!=null?cxpValeAzulDto.getFechaHoraControl():new Date()) %>"
                                            style="width: 80px;"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Fecha Pago:</label><br/>
                                        <input type="text" name="fecha_pago" id="fecha_pago" readonly
                                            value="<%= DateManage.formatDateToNormal(cxpValeAzulDto!=null?cxpValeAzulDto.getFechaTentativaPago():new Date()) %>"
                                            style="width: 80px;"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=cxpValeAzulDto != null ? (cxpValeAzulDto.getIdEstatus() == 1 ? "checked" : "") : "checked"%> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
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


        <script>
            mostrarCalendario();
        </script>
    </body>
</html>
<%}%>