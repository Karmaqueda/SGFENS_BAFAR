<%-- 
    Document   : cfdi_sar_area_x_cliente
    Created on : 11/04/2015, 06:28:31 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.bo.SarAreaEntregaBO"%>
<%@page import="com.tsp.sct.dao.dto.SarAreaEntrega"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    int idSarUsuario = 0;
    try {
        idSarUsuario = Integer.parseInt(request.getParameter("id_sar_usuario"));
    } catch (NumberFormatException e) {}
    
    String filtroBusqueda =  " AND ID_ESTATUS=1 ";    
    
    SarAreaEntrega[] sarAreas = new SarAreaEntregaBO(user.getConn()).findSarAreaEntregas(-1, idSarUsuario, 0, 0, filtroBusqueda);
    int idEmpresa = user.getUser().getIdEmpresa();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../include/keyWordSEO.jsp" />
        <title><jsp:include page="../include/titleApp.jsp" /></title>
        <jsp:include page="../include/skinCSS.jsp" />
        <jsp:include page="../include/jsFunctions.jsp"/>
        
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
                        url: "../cfdi_sar/cfdi_sar_area_x_cliente_ajax.jsp",
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
                               apprise('<center><img src=../../images/info.png> <br/>Registro actualizado</center>',{'animate':true},
                                        function(r){
                                            location.reload();
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
                
                if(jQuery.trim($("#id_sar_area_entrega").val())<=0){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "Area" es requerido</center>',{'animate':true});
                    $("#id_sar_area_entrega").focus();
                    return false;
                }
                
                if(jQuery.trim($("#id_cliente").val())<=0){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "Cliente" es requerido</center>',{'animate':true});
                    $("#id_cliente").focus();
                    return false;
                }
               
                return true;
            }
            
        </script>
        
    </head>
    <body>
        <div id="ajax_loading" class="alert_info" style="display: none;"></div>
        <div id="ajax_message" class="alert_warning" style="display: none;"></div>
        <table class="data" width="100%" cellpadding="0" cellspacing="0">
            <thead>
                <tr>
                    <th>Área SAR</th>
                    <th>Cliente Pretoriano</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (SarAreaEntrega sarArea : sarAreas){
                        String dataCliente = "-No Asignado-";
                        if (sarArea.getIdCliente()>0){
                            Cliente clienteDto = new ClienteBO(sarArea.getIdCliente(), user.getConn()).getCliente();
                            if (clienteDto!=null){
                                dataCliente = clienteDto.getRfcCliente() + " - " + clienteDto.getRazonSocial();
                            }else{
                                dataCliente = "No encontrado";
                            }
                        }
                %>
                <tr>
                    <td><%= sarArea.getExtSarNombre()%></td>
                    <td><%= dataCliente %></td>
                </tr>
                <%
                    }
                %>
                <tr>
                    <td colspan="2">
                        <form id="frm_action" name="frm_action" method="post">
                            <input type="hidden" id="mode" name="mode" value="1"/>
                            <input type="hidden" id="id_sar_usuario" name="id_sar_usuario" value="<%= idSarUsuario %>"/>
                            <select id="id_sar_area_entrega" name="id_sar_area_entrega">
                                <option value="-1">--Selecciona un Área SAR--</option>
                                <%= new SarAreaEntregaBO(user.getConn()).getSarAreaEntregasByIdHTMLCombo(idSarUsuario, -1, filtroBusqueda) %>
                            </select>
                            <select id="id_cliente" name="id_cliente">
                                <option value="-1">--Selecciona un Cliente--</option>
                                <%= new ClienteBO(user.getConn()).getClientesByIdHTMLCombo(idEmpresa, -1, " AND ID_ESTATUS = 1 ") %>
                            </select>
                            <div id="action_buttons" name="action_buttons">
                                <input type="button" id="btnCrear" name="btnCrear" class="right_switch" value="Guardar Relación" 
                                    onclick="grabar();"/>
                            </div>
                        </form>
                    </td>
                </tr>
            </tbody>
        </table>
    </body>
</html>
