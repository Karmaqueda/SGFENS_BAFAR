<%-- 
    Document   : consultaNotas
    Created on : 7/04/2015, 06:28:31 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.dao.dto.CxcNota"%>
<%@page import="com.tsp.sct.bo.CxcNotaBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%

    int idComprobanteFiscal = 0;
    int idPedido = 0;
    int idValeAzul = 0;
    int idCxpComprobanteFiscal = 0;
    int idCrCredCliente = 0;
    try {
        idComprobanteFiscal = Integer.parseInt(request.getParameter("idComprobanteFiscal"));
    } catch (NumberFormatException e) {}
    try {
        idPedido = Integer.parseInt(request.getParameter("idPedido"));
    } catch (NumberFormatException e) {}
    try {
        idValeAzul = Integer.parseInt(request.getParameter("id_cxp_vale_azul"));
    } catch (NumberFormatException e) {}
    try {
        idCxpComprobanteFiscal = Integer.parseInt(request.getParameter("id_cxp_comprobante_fiscal"));
    } catch (NumberFormatException e) {}
    try {
        idCrCredCliente = Integer.parseInt(request.getParameter("idCrCredCliente"));
    } catch (NumberFormatException e) {}
    
    String filtroBusqueda =  "";
    
    if (idComprobanteFiscal>0){
        filtroBusqueda += " AND ID_COMPROBANTE_FISCAL = " + idComprobanteFiscal;
    }else if (idPedido>0){
        filtroBusqueda += " AND ID_PEDIDO = " + idPedido;
    }else if (idValeAzul>0){
        filtroBusqueda += " AND ID_CXP_VALE_AZUL = " + idValeAzul;
    }else if (idCxpComprobanteFiscal>0){
        filtroBusqueda += " AND ID_CXP_COMPROBANTE_FISCAL = " + idCxpComprobanteFiscal;
    }else if (idCrCredCliente>0){
        filtroBusqueda += " AND ID_CR_CRED_CLIENTE = " + idCrCredCliente;
    }else{
        //Filtro para evitar mostrar datos en caso de no recibir ningun parametro
        filtroBusqueda += " AND ID_COMPROBANTE_FISCAL = -1";
    }
    
    
    CxcNota[] notas = new CxcNotaBO(user.getConn()).findCxp(-1, -1, 0, 0, filtroBusqueda);
    
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../include/keyWordSEO.jsp" />
        <title><jsp:include page="../include/titleApp.jsp" /></title>
        <jsp:include page="../include/skinCSS.jsp" />
        <jsp:include page="../include/jsFunctions2.jsp"/>
        
        <script type="text/javascript">
            
             $(document).ready(function() {
                mostrarFormPopUpMode();
            });
            
            function mostrarFormPopUpMode(){
		$('body').addClass('nobg');
            }
            
            function grabar(){
                if(validar()){
                    $.ajax({
                        type: "POST",
                        url: "../notas/notas_list_ajax.jsp",
                        data: $("#frm_action").serialize(),
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><center>Registrando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_message").html(datos);
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/info.png> <br/>Nota registrada exitosamente</center>',{'animate':true},
                                        function(r){
                                            location.reload();
                                        });
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               $("#action_buttons").fadeIn("slow");
                               $.scrollTo('.inner',800);
                           }
                        }
                    });
                }
            }

            function validar(){
                
                if(jQuery.trim($("#nota").val())===""){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "Nota" es requerido</center>',{'animate':true});
                    $("#nota").focus();
                    return false;
                }
               
                return true;
            }
            
        </script>
        
    </head>
    <body>
        <div id="ajax_loading" class="alert_info" style="display: none;"></div>
        <div id="ajax_message" class="alert_warning" style="display: none;"></div>
        
        <br class="clear"/>
        <form id="frm_action" name="frm_action" method="post">
            <input type="hidden" id="mode" name="mode" value="1"/>
            <input type="hidden" id="idComprobanteFiscal" name="idComprobanteFiscal" value="<%= idComprobanteFiscal %>"/>
            <input type="hidden" id="idPedido" name="idPedido" value="<%= idPedido %>"/>
            <input type="hidden" id="id_cxp_vale_azul" name="id_cxp_vale_azul" value="<%= idValeAzul %>"/>
            <input type="hidden" id="id_cxp_comprobante_fiscal" name="id_cxp_comprobante_fiscal" value="<%= idCxpComprobanteFiscal %>"/>
            <input type="hidden" id="idCrCredCliente" name="idCrCredCliente" value="<%= idCrCredCliente %>"/>
            <textarea id="nota" name="nota" cols="70" rows="4" placeholder="Escribe una nota nueva..."
                      onKeyUp="return textArea_maxLen(this,500);"></textarea>
            <div id="action_buttons" name="action_buttons">
                <input type="button" id="btnCrear" name="btnCrear" class="right_switch" value="Registrar Nota" 
                    onclick="grabar();"/>
            </div>
        </form>
        <br class="clear"/>
        <table class="data" width="100%" cellpadding="0" cellspacing="0">
            <thead>
                <tr>
                    <th>Nota</th>
                    <th>Fecha/Hr</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (CxcNota nota : notas){
                %>
                <tr>
                    <td><%= nota.getNota() %></td>
                    <td><%= DateManage.dateTimeToStringEspanol(nota.getFechaHoraCaptura()) %></td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </body>
</html>
