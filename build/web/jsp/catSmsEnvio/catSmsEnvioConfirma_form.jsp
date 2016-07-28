<%-- 
    Document   : catSmsEnvioConfirma_form
    Created on : 28/03/2016, 11:47:03 AM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.util.SmsLengthCalculator"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sgfens.sesion.SmsEnvioDestinatariosSesion"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="smsEnvioSesion" scope="session" class="com.tsp.sgfens.sesion.SmsEnvioSesion"/>
<%
//Verifica si el cliente tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
     *   0/"" = nuevo
     *   1 = editar/consultar
     *   2 = eliminar  
     */
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
    
    SmsLengthCalculator calc = new SmsLengthCalculator();
    int tipoCodificacion = calc.getCharset(smsEnvioSesion.creaContenidoSMS());
    String codificacion = tipoCodificacion==SmsLengthCalculator.GSM_CHARSET_7BIT? "7-Bit" : "Unicode";
%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="shortcut icon" href="../../images/favicon.ico">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../include/keyWordSEO.jsp" />

        <title><jsp:include page="../include/titleApp.jsp" /></title>

        <jsp:include page="../include/skinCSS.jsp" />

        <jsp:include page="../include/jsFunctions.jsp"/>
        
        <script type="text/javascript">
            
            $(document).ready(function() {
                <%= (smsEnvioSesion==null?"sesionVacia()":"") %>
            });
            
            function sesionVacia(){
                apprise('<center><img src=../../images/warning.png> <br/><b>No hay datos en Sesion para Envio de SMS.</b></center>',{'animate':true},
                        function(r){
                            history.back();
                    });
            }
            
            function grabar(){                
                $.ajax({
                    type: "POST",
                    url: "catSmsEnvioConfirma_ajax.jsp",
                    data: $("#frm_action").serialize(),
                    beforeSend: function(objeto){
                        $("#ajax_message").fadeOut("slow");
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           //$("#ajax_message").html(datos);
                           $("#ajax_loading").fadeOut("slow");
                           //$("#ajax_message").fadeIn("slow");
                           apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                    function(r){
                                        location.href = "catSmsEnvio_list.jsp";
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
            
            function cuentaCaracteresDivPrevisualizacion(){                
                var smsCount = SmsCounter.count($("#prev_msg").html());
                console.log(smsCount);
                console.log(smsCount['encoding']);
                console.log(smsCount['length']);
                console.log(smsCount['per_message']);
                $("#caracteres_restantes").html(''+smsCount['remaining']);
                $("#partes").html(''+smsCount['messages']);
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
                    <h1>SMS Masivos</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                        <input type="hidden" id="mode" name="mode" value="1" />
                    <div class="twocolumn">
                        
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_smsAgendaEnvio.png" alt="icon"/>
                                    Confirmaci&oacute;n de Env&iacute;o
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                
                                <p>
                                    <label>Enviar:</label>&nbsp; <%=smsEnvioSesion!=null?(smsEnvioSesion.isEnvioInmediato()?"Ahora":DateManage.dateTimeToStringEspanol(smsEnvioSesion.getFechaHoraEnvioProgramado())):"" %>
                                </p>
                                <br/>
                                
                                <!-- tabla contadores -->
                                <table class="data" width="100%" cellpadding="5px" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th style="text-align : center; vertical-align : middle">
                                                Cr&eacute;ditos SMS 
                                            </th>
                                            <th style="text-align : center; vertical-align : middle">
                                                Destinatarios 
                                            </th>
                                            <th style="text-align : center; vertical-align : middle">
                                                Partes Mensaje 
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td style="text-align : center; vertical-align : middle; color: #ff0000;">
                                                <b><%= smsEnvioSesion!=null? ("" + smsEnvioSesion.calculaCreditosSMS()) : "" %></b>
                                            </td>
                                            <td style="text-align : center; vertical-align : middle">
                                                <%= smsEnvioSesion!=null? ("" + smsEnvioSesion.getListaDestinatarios().size()) : "" %>
                                            </td>
                                            <td style="text-align : center; vertical-align : middle">
                                                <%= smsEnvioSesion!=null? ("" + smsEnvioSesion.calculaPartesMensaje() ) : "" %>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                                <!-- FIN tabla contadores -->
                                <br/>
                                
                                <p>
                                    <label>Mensaje final:</label><br/>
                                    <div id="prev_msg" class="message from"><%= smsEnvioSesion!=null? smsEnvioSesion.creaContenidoSMS() : "" %></div>
                                    <div style="float: left; display: inline;">
                                        <span id="codificacion"><i>Tipo: <%= codificacion %></i></span>
                                    </div>
                                    <div style="float: right; display: inline;">
                                        <span id="caracteres_restantes">?</span> / <span id="partes">?</span>
                                    </div>
                                </p>
                                <br/>
                                <br/>

                                <div id="action_buttons">
                                    <p>
                                        <% if (smsEnvioSesion!=null && mode.equals("")){ %>
                                        <input type="button" id="enviar" value="<%= smsEnvioSesion.isEnvioInmediato()?"Enviar":"Programar" %>" onclick="grabar();"/>
                                        <% } %>
                                        <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                    </p>
                                </div>
                            </div>
                        </div>
                        
                        <div class="column_right">
                            <div class="header">
                                <span>
                                    Destinatarios
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content"> 
                                
                                <div id="table-wrapper">
                                    <div id="table-scroll">
                                        <table>
                                            <thead>
                                                <tr>
                                                    <th><span class="text">Nombre</span></th>
                                                    <th><span class="text">Celular</span></th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <% if (smsEnvioSesion!=null){
                                                    for (SmsEnvioDestinatariosSesion item : smsEnvioSesion.getListaDestinatarios()) { %>
                                                <tr>
                                                    <td>
                                                        <%= StringManage.getValidString(item.getNombre()) %>
                                                    </td>
                                                    <td>
                                                        <%= StringManage.getValidString(item.getNumCelular()) %>
                                                    </td>
                                                </tr>
                                                <% 
                                                    } 
                                                }
                                                %>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                
                            </div>
                        </div>    
                        
                        <br class="clear"/>
                        
                    </div>
                    </form>
                </div>
            
            <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>
            
        <script type="text/javascript">
            cuentaCaracteresDivPrevisualizacion();
        </script>
        
    </body>
</html>

<%
}
%>