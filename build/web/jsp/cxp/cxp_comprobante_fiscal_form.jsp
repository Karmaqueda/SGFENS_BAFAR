<%-- 
    Document   : cxp_comprobante_fiscal_form
    Created on : 15/04/2015, 15/04/2015 05:13:03 PM
    Author     : ISCesar
--%>

<%@page import="mx.bigdata.sat.cfdi.schema.v32.TimbreFiscalDigital"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="mx.bigdata.sat.cfdi.schema.v32.Comprobante"%>
<%@page import="com.tsp.sct.cfdi.Cfd32BO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.bo.CxpComprobanteFiscalBO"%>
<%@page import="com.tsp.sct.dao.dto.CxpComprobanteFiscal"%>
<%@page import="com.tsp.sct.dao.dto.ValidacionXml"%>
<%@page import="com.tsp.sct.bo.ValidacionXmlBO"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
//Verifica si el cliente tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String msgErrorInicial = "";
    int idValidacion = 0;
    int idCxpComprobanteFiscal = 0;
    try{
        idValidacion = Integer.parseInt(request.getParameter("id_validacion"));
    }catch(NumberFormatException ex){}
    try{
        idCxpComprobanteFiscal = Integer.parseInt(request.getParameter("id_cxp_comprobante_fiscal"));
    }catch(NumberFormatException ex){}
    
    /*
    *   0/"" = nuevo
    *   1 = editar/consultar
    */
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
    
    ValidacionXmlBO validacionXmlBO = new ValidacionXmlBO(idValidacion, user.getConn());
    ValidacionXml validacionXmlDto = validacionXmlBO.getValidacionXml();
    CxpComprobanteFiscal cxpComprobanteFiscalDto = null;
    if (idCxpComprobanteFiscal>0){
        cxpComprobanteFiscalDto = new CxpComprobanteFiscalBO(idCxpComprobanteFiscal, user.getConn()).getCxpComprobanteFiscal();
    }
    
    Comprobante compv32 = null;
    TimbreFiscalDigital timbrev32 = null;
    if (validacionXmlDto!=null){
        if (validacionXmlDto.getVersionComprobante().equals("3.2")){
            try{
                Cfd32BO cfd32BO = new Cfd32BO(validacionXmlBO.getComprobanteFile());
                compv32 = cfd32BO.getComprobanteFiscal();
                timbrev32 = cfd32BO.getTimbreFiscalDigital();
            }catch(Exception ex){
                msgErrorInicial += "Ocurrio un error al cargar Archivo como Objeto: " + ex.toString();
            }
        }
    }else{
        msgErrorInicial += "No se recibio el parametro ID Validación, para poder relacionar el proceso.";
    }
    
    NumberFormat formatMoneda = new DecimalFormat("$ ###,###,###,##0.00");
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
            function grabar(){
                if(validar()){
                    $.ajax({
                        type: "POST",
                        url: "cxp_comprobante_fiscal_ajax.jsp",
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
                                            location.href = "../cxp/cxp_comprobantes_list.jsp";
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
                if(jQuery.trim($("#id_comprobante_fiscal").val())===""){
                    apprise('<center><img src=../../images/warning.png> <br/>El Comprobante Fiscal a enviar no existe</center>',{'animate':true});
                    return false;
                }*/
                    
                return true;
            }
            
            function mostrarCalendario(){
                $( "#fecha_pago" ).datepicker({
                        minDate: 0,
                        gotoCurrent: true,
                        changeMonth: true,
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 100);
                            }, 500);}
                });
            }
            
            function errorInicial(){
                apprise('<center><img src=../../images/warning.png> <br/>Ocurrio un error que no permite continuar el proceso: <br/><%= msgErrorInicial %> </center>',{'animate':true},
                    function(r){
                        location.href = "../cxp/cxp_comprobantes_list.jsp";
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
                    <h1>Cuentas Por Pagar - Comprobante Fiscal</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/sar_enviar.png" alt="icon"/>
                                    <% if (mode.equals("") || mode.equals("0")) {%>
                                    Completar Datos registro CxP
                                    <% }else{ %>
                                    Editar Datos
                                    <% } %>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="id_cxp_comprobante_fiscal" name="id_cxp_comprobante_fiscal" value="<%=cxpComprobanteFiscalDto!=null?cxpComprobanteFiscalDto.getIdCxpComprobanteFiscal():"" %>" />
                                    <input type="hidden" id="id_validacion" name="id_validacion" value="<%=validacionXmlDto!=null?validacionXmlDto.getIdValidacion():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>*Fecha de Pago:</label><br/>
                                        <input type="text" name="fecha_pago" id="fecha_pago" readonly
                                            value="<%= DateManage.formatDateToNormal(cxpComprobanteFiscalDto!=null?cxpComprobanteFiscalDto.getFechaTentativaPago():new Date()) %>"
                                            style="width: 80px;"/>
                                    </p>
                                    </br>
                                    <br/>
                                    
                                    <div id="action_buttons">
                                        <p>
                                            <input type="button" id="enviar" value="Registrar CxP" onclick="grabar();"/>
                                            <input type="button" id="regresar" value="Regresar" onclick="location.href = '../cxp/cxp_comprobantes_list.jsp';"/>
                                        </p>
                                    </div>
                                    
                            </div>
                        </div>
                        <!-- End left column window -->
                        
                        <div class="column_right">
                            <div class="header">
                                <span>
                                    Información del Comprobante Fiscal
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <% if (compv32!=null) { %>
                                    <input type="hidden" id="serie" name="serie" value="<%=StringManage.getValidString(compv32.getSerie())%>" />
                                    <input type="hidden" id="folio" name="folio" value="<%=StringManage.getValidString(compv32.getFolio())%>" />
                                    <input type="hidden" id="emisor_rfc" name="emisor_rfc" value="<%=StringManage.getValidString(compv32.getEmisor().getRfc())%>" />
                                    <input type="hidden" id="emisor_nombre" name="emisor_nombre" value="<%=StringManage.getValidString(compv32.getEmisor().getNombre())%>" />
                                    <input type="hidden" id="total" name="total" value="<%=compv32.getTotal() %>" />
                                    <input type="hidden" id="fecha_hr_sello" name="fecha_hr_sello" value="<%=DateManage.dateToSQLDateTime(compv32.getFecha())%>" />
                                    <input type="hidden" id="sello_emisor" name="sello_emisor" value="<%=compv32.getSello()%>" />
                                    <input type="hidden" id="uuid" name="uuid" value="<%=timbrev32!=null?timbrev32.getUUID():"" %>" />
                                    <p>
                                        Serie y Folio: 
                                        <label>
                                            <%=compv32.getSerie()!=null?compv32.getSerie() : " [Sin Serie] " %>
                                            -
                                            <%=compv32.getFolio()!=null?compv32.getFolio() : " [Sin Folio] " %>
                                        </label>
                                    </p>
                                    <br/>
                                    <p>
                                        RFC Emisor: <label><%=compv32.getEmisor()!=null?compv32.getEmisor().getRfc(): "" %></label>
                                    </p>
                                    <br/>
                                    <p>
                                        Razon Social Emisor: <label><%=compv32.getEmisor()!=null?StringManage.getValidString(compv32.getEmisor().getNombre()): "" %></label>
                                    </p>
                                    <br/>
                                    <p>
                                        Total: <label><%= formatMoneda.format(compv32.getTotal().doubleValue()) %></label>
                                    </p>
                                    <br/>
                                    <p>
                                        Fecha Emisión: <label><%= DateManage.formatDateToNormal(compv32.getFecha()) %></label>
                                    </p>
                                    <br/>
                                    
                                    <% } else { %>
                                    <b>NO SE RECUPERO INFORMACIÓN DEL COMPROBANTE FISCAL</b>
                                    <% } %>
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


        <script>
            mostrarCalendario();
            <% if (!msgErrorInicial.equals("")) {%>
                errorInicial();
            <% } %>
        </script>
    </body>
</html>
<%
}
%>