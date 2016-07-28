<%-- 
    Document   : cfdi_sar_factura_campos_extra_form
    Created on : 18/03/2015, 01:55:01 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.dao.dto.SarCamposAdicionales"%>
<%@page import="com.tsp.sct.bo.SarAreaEntregaBO"%>
<%@page import="com.tsp.sct.dao.dto.SarAreaEntrega"%>
<%@page import="com.tsp.sct.dao.dto.SarClienteEntrega"%>
<%@page import="com.tsp.sct.bo.SarUsuarioProveedorBO"%>
<%@page import="com.tsp.sct.dao.dto.SarUsuarioProveedor"%>
<%@page import="com.tsp.sct.dao.dto.SarComprobante"%>
<%@page import="com.tsp.sct.dao.dto.ComprobanteFiscal"%>
<%@page import="com.tsp.sct.bo.SarComprobanteBO"%>
<%@page import="com.tsp.sct.bo.ComprobanteFiscalBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
//Verifica si el cliente tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    int idComprobanteFiscal=0;
    try{
        idComprobanteFiscal = Integer.parseInt(request.getParameter("idComprobanteFiscal"));
    }catch(NumberFormatException ex){}
    if (idComprobanteFiscal<=0){
        //Si no se mando como parametro GET/POST, se intenta recuperar de sesion
        idComprobanteFiscal = user.getUltimoRegistroID();
    }
    
    /*
    *   0/""/1 = nuevo
    *   2 = editar/consultar
    */
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
    
    ComprobanteFiscalBO comprobanteFiscalBO = new ComprobanteFiscalBO(idComprobanteFiscal, user.getConn());
    SarComprobanteBO sarComprobanteBO = new SarComprobanteBO(idComprobanteFiscal, user.getConn());
    ComprobanteFiscal comprobanteFiscalDto = comprobanteFiscalBO.getComprobanteFiscal();
    SarComprobante sarComprobanteDto = sarComprobanteBO.getSarComprobante();
    
    SarUsuarioProveedor sarUsuarioProveedorDto = null;
    SarClienteEntrega sarClienteEntrega = null;
    SarCamposAdicionales sarCamposAdicionales = null;
    SarAreaEntrega sarAreaEntrega = null;
    if (sarComprobanteDto!=null){
        SarUsuarioProveedorBO sarUsuarioProveedorBO = new SarUsuarioProveedorBO(sarComprobanteDto.getIdSarUsuario(), user.getConn());
        sarUsuarioProveedorDto = sarUsuarioProveedorBO.getSarUsuarioProveedor();
        sarClienteEntrega = sarUsuarioProveedorBO.getSarClienteEntrega();
        sarCamposAdicionales = sarUsuarioProveedorBO.getSarCamposAdicionales();
                
        sarAreaEntrega = new SarAreaEntregaBO(sarComprobanteDto.getIdSarAreaEntrega(), user.getConn()).getSarAreaEntrega();
        
        if (sarComprobanteDto.getExtSarIdFactura()<=0){
            //nuevo
            mode = "1";
        }else {
            //edicion
            mode = "2";
        }
    }
        
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../include/keyWordSEO.jsp" />

        <title><jsp:include page="../include/titleApp.jsp" /></title>

        <jsp:include page="../include/skinCSS.jsp" />

        <jsp:include page="../include/jsFunctions.jsp"/>
        
        <!-- SCRIPT CHAT SOPORTE ZOPIM -->
        <script type="text/javascript" src="../../js/zopim/widgetZopimSAR.js"></script>
        
        <script type="text/javascript">
            function grabar(){
                if(validar()){
                    $.ajax({
                        type: "POST",
                        url: "cfdi_sar_factura_campos_extra_ajax.jsp",
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
                                            location.href = "../cfdi_sar/cfdi_sar_factura_list.jsp";
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
                if(jQuery.trim($("#id_comprobante_fiscal").val())===""){
                    apprise('<center><img src=../../images/warning.png> <br/>El Comprobante Fiscal a enviar no existe</center>',{'animate':true});
                    return false;
                }
                
                <% if (sarCamposAdicionales!=null 
                                            && sarCamposAdicionales.getExtReqOrdenCompra()==(short)1){ %>
                if(jQuery.trim($("#sar_orden_compra").val())===""){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "Orden de Compra" es requerido por tu Cliente en el sistema SAR</center>',{'animate':true});
                    $("#sar_orden_compra").focus();
                    return false;
                }
                <% } %>
                    
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
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Conexión SAR - Comprobante con ID: <%= idComprobanteFiscal %></h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/sar_enviar.png" alt="icon"/>
                                    <% if (mode.equals("1")) {%>
                                    Completar Datos para Envío SAR
                                    <%}else{%>
                                    Editar Datos y Adjuntar Archivos
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="id_comprobante_fiscal" name="id_comprobante_fiscal" value="<%=sarComprobanteDto!=null?sarComprobanteDto.getIdComprobanteFiscal():"" %>" />
                                    <input type="hidden" id="id_sar_usuario" name="id_sar_usuario" value="<%=sarComprobanteDto!=null?sarComprobanteDto.getIdSarUsuario():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <% if (sarCamposAdicionales!=null 
                                            && sarCamposAdicionales.getExtReqOrdenCompra()==(short)1){ %>
                                    <p>
                                        <label>Orden de Compra:</label><br/>
                                        <input maxlength="100" type="text" id="sar_orden_compra" name="sar_orden_compra" style="width:300px"
                                               value="<%=sarComprobanteDto!=null?StringManage.getValidString(sarComprobanteDto.getOrdenCompra()): "" %>" />
                                    </p>
                                    </br>
                                    <% } %>
                                    <p>
                                        <label>Adjuntos:</label><br/>
                                        <iframe style="border: none" scrolling="no" id="iframe_archivo_adjunto" name="iframe_archivo_adjunto" src="../file/uploadForm.jsp?id=archivo_adjunto&fn=enviarArchivo&div=div_archivos_adjuntos&validate=any"></iframe>
                                        <div id="div_archivos_adjuntos"></div>
                                    </p>
                                    <br/><br/>
                                    
                                    <div id="action_buttons">
                                        <p>
                                            <input type="button" id="enviar" value="Entregar en SAR" onclick="grabar();"/>
                                            <input type="button" id="regresar" value="Regresar" onclick="location.href = '../cfdi_sar/cfdi_sar_factura_list.jsp';"/>
                                        </p>
                                    </div>
                                    
                            </div>
                        </div>
                        <!-- End left column window -->
                        
                        <div class="column_right">
                            <div class="header">
                                <span>
                                    Información General
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <p>
                                        Usuario SAR: <label><%=sarUsuarioProveedorDto!=null?sarUsuarioProveedorDto.getExtSarUsuario() : "" %></label>
                                    </p>
                                    <br/>
                                    <p>
                                        Cliente Receptor SAR: <label><%=sarClienteEntrega!=null?sarClienteEntrega.getExtSarRazonSocial(): "" %></label>
                                    </p>
                                    <br/>
                                    <p>
                                        &Aacute;rea de Entrega SAR: <label><%=sarAreaEntrega!=null?sarAreaEntrega.getExtSarNombre() : "" %></label>
                                    </p>
                                    <br/>
                                    <p>
                                        C&oacute;digo de Proveedor Área SAR: <label><%=sarAreaEntrega!=null?StringManage.getValidString(sarAreaEntrega.getExtSarCodprovArea()): "" %></label>
                                    </p>
                                    <br/>
                                    <p>
                                        UUID Comprobante: <label><%=comprobanteFiscalDto!=null?comprobanteFiscalDto.getUuid(): "" %></label>
                                    </p>
                                    <br/>
                                    <p>
                                        Monto Total Comprobante: <label>$ <%=comprobanteFiscalDto!=null?comprobanteFiscalDto.getImporteNeto(): "" %></label>
                                    </p>
                                    <br/>
                            </div>
                        </div>
                        <!-- End right column window -->
                        
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
<%
}
%>