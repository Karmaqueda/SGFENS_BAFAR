<%-- 
    Document   : consultaAdjuntos
    Created on : 8/04/2015, 06:28:31 PM
    Author     : ISCesar
--%>

<%@page import="java.io.File"%>
<%@page import="com.tsp.sct.bo.SarComprobanteAdjuntoBO"%>
<%@page import="com.tsp.sct.dao.dto.SarComprobanteAdjunto"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%

    int idComprobanteFiscal = 0;
    int idPedido = 0;
    int idValeAzul = 0;
    int idCxpComprobanteFiscal = 0;
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
    
    String filtroBusqueda =  "";
    
    if (idComprobanteFiscal>0){
        filtroBusqueda += " AND ID_COMPROBANTE_FISCAL = " + idComprobanteFiscal;
    }else if (idPedido>0){
        filtroBusqueda += " AND ID_PEDIDO = " + idPedido;
    }else if (idValeAzul>0){
        filtroBusqueda += " AND ID_CXP_VALE_AZUL = " + idValeAzul;
    }else if (idCxpComprobanteFiscal>0){
        filtroBusqueda += " AND ID_CXP_COMPROBANTE_FISCAL = " + idCxpComprobanteFiscal;
    }else{
        //Filtro para evitar mostrar datos en caso de no recibir ningun parametro
        filtroBusqueda += " AND ID_COMPROBANTE_FISCAL = -1";
    }
    
    
    SarComprobanteAdjunto[] adjuntos = new SarComprobanteAdjuntoBO(user.getConn()).findSarComprobanteAdjuntos(-1, 0, 0, filtroBusqueda);
    
    String urlDownloadXML ="/jsp/file/download.jsp";
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
                        url: "../adjuntos/registraAdjuntoAjax.jsp",
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
                               apprise('<center><img src=../../images/info.png> <br/>Adjunto registrado exitosamente</center>',{'animate':true},
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
                
                /*if(jQuery.trim($("#nota").val())===""){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "Adjunto" es requerido</center>',{'animate':true});
                    $("#nota").focus();
                    return false;
                }*/
               
                return true;
            }
            
            // --------- FUNCIONES PARA CARGA DE ARCHIVOS 
            function recuperaId(nombre,id,div,idControl){
                $("#"+div).html(
                    $("#"+div).html() + 
                    "<div id=\"div_"+id+"\"><input type=\"hidden\" id=\"archivo_adjunto\" name=\"archivo_adjunto\" value=\""+id+"\"/>" + 
                    "<img style=\"cursor: pointer\" src=\"../../images/error.png\" alt=\"remover\" onclick=\"remover('"+id+"');\"/>"+nombre+"</div>"
                );
            }
            
            function remover(id){
                apprise('¿Esta seguro que deseas remover el archivo cargado?', {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, function(r)
                {
                    if(r){
                        //borrarArchivo(id);
                        $('div[id="div_' + id + '"]').fadeOut("slow");
                        $('div[id="div_' + id + '"]').html('');
                        
                    }
                });
            }
            
            function enviarArchivo(form){
                window.frames['iframe_archivo_adjunto'].document.forms[form].submit();
            }

            // --------- FIN FUNCIONES PARA CARGA DE ARCHIVOS
        </script>
        
    </head>
    <body>
        <div id="ajax_loading" class="alert_info" style="display: none;"></div>
        <div id="ajax_message" class="alert_warning" style="display: none;"></div>
        <table class="data" width="100%" cellpadding="0" cellspacing="0">
            <thead>
                <tr>
                    <th>Nombre Archivo</th>
                    <th>Descargar</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (SarComprobanteAdjunto adjunto : adjuntos){
                        File archivo = new SarComprobanteAdjuntoBO(user.getConn()).getArchivoDeRepositorioByEmpresa(adjunto.getNombreArchivo(), idEmpresa);
                        String rutaArchivoEnc = java.net.URLEncoder.encode(archivo.getAbsolutePath(), "UTF-8");
                %>
                <tr>
                    <td><%= adjunto.getNombreArchivo()%></td>
                    <td>
                        <a href='<%=request.getContextPath() + urlDownloadXML + "?ruta_archivo="+rutaArchivoEnc %>'>
                            <img src="../../images/document_down.png" width="16" height="16" alt="Archivo"> [Descargar]
                        </a>
                    </td>
                </tr>
                <%
                    }
                %>
                <tr>
                    <td colspan="2">
                        <form id="frm_action" name="frm_action" method="post">
                            <input type="hidden" id="mode" name="mode" value="1"/>
                            <input type="hidden" id="idComprobanteFiscal" name="idComprobanteFiscal" value="<%= idComprobanteFiscal %>"/>
                            <input type="hidden" id="idPedido" name="idPedido" value="<%= idPedido %>"/>
                            <input type="hidden" id="id_cxp_vale_azul" name="id_cxp_vale_azul" value="<%= idValeAzul %>"/>
                            <input type="hidden" id="id_cxp_comprobante_fiscal" name="id_cxp_comprobante_fiscal" value="<%= idCxpComprobanteFiscal %>"/>
                            <p>
                                <label>Adjuntar Nuevo:</label><br/>
                                <iframe style="border: none" scrolling="no" id="iframe_archivo_adjunto" name="iframe_archivo_adjunto" src="../file/uploadForm.jsp?id=archivo_adjunto&fn=enviarArchivo&div=div_archivos_adjuntos&validate=any"></iframe>
                                <div id="div_archivos_adjuntos"></div>
                            </p>
                            
                            <div id="action_buttons" name="action_buttons">
                                <input type="button" id="btnCrear" name="btnCrear" class="right_switch" value="Registrar Adjunto" 
                                    onclick="grabar();"/>
                            </div>
                        </form>
                    </td>
                </tr>
            </tbody>
        </table>
    </body>
</html>
