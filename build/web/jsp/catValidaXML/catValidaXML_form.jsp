<%-- 
    Document   : catValidaXML_form
    Created on : 6/03/2014, 12:27:41 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el cliente tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {

    int idEmpresa = user.getUser().getIdEmpresa();

    /*
     * Parámetros
     */
    /*
    int idArchivoXml = 0;
    try {
        idArchivoXml = Integer.parseInt(request.getParameter("idArchivoXml"));
    } catch (NumberFormatException e) {
    }*/

    /*
     *   0/"" = nueva validación XML
     *   1 =  nuevo comprobante CxP
     */
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
    
    String urlBack = "";
    if (mode.equals("") || mode.equals("0")){
        urlBack = "../catValidaXML/catValidaXML_list.jsp";
    }else if (mode.equals("1")){
        urlBack = "../cxp/cxp_comprobantes_list.jsp";
    }
    
    String urlContinueCxP = "../cxp/cxp_comprobante_fiscal_form.jsp";
    
    Empresa empresaPadre = new Empresa();
    EmpresaBO eBO = new EmpresaBO(user.getConn());
    empresaPadre = eBO.getEmpresaMatriz(idEmpresa);
    EmpresaPermisoAplicacion ePermisoApp = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaPadre.getIdEmpresa());
    
    String labelCreditosDisponibles = "Créditos disponibles de ";
     if (ePermisoApp!=null){
         if (ePermisoApp.getTipoConsumoServicio()==1){
             //On Demand - Por créditos de operación
             labelCreditosDisponibles += "Operación : " + empresaPadre.getCreditosOperacion();
         }else{
             //Normal - post-pago
            labelCreditosDisponibles += "Validación : " + empresaPadre.getCreditoValidaXml();
         }
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
                        url: "catValidaXML_ajax.jsp",
                        data: $("#frm_action").serialize(),
                        beforeSend: function(objeto){
                            $("#enviar").fadeOut("slow");
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos) {
                            <% if (mode.equals("") || mode.equals("0")){ %>
                                if(datos.indexOf("--EXITO-->", 0)>0){                                    
                                   $("#div_ajax_validaciones").html(datos);
                                   $("#div_validacion_ajax_loading").fadeOut("slow");
                                   $("#action_buttons").fadeIn("slow");
                                   $("#ajax_loading").fadeOut("slow");
                               }else{
                                   $("#div_validacion_ajax_loading").html(datos);
                                   $("#action_buttons").fadeIn("slow");
                                   $("#ajax_loading").fadeOut("slow");
                               }
                            <% } else if (mode.equals("1")){ %>
                                if(datos.indexOf("--EXITO-->", 0)>0){ 
                                    strJSONValidacionXml = $.trim(datos.replace('<!--EXITO-->',''));
                                    arrayValidacionXml=eval('(' + strJSONValidacionXml + ')');
                                    
                                    idValidacion = arrayValidacionXml["idValidacion"];
                                    comprobanteValido = arrayValidacionXml["comprobanteValido"];
                                    mensajesError = arrayValidacionXml["mensajesError"];
                                    
                                    if (comprobanteValido==1){
                                        urlCxP = "<%=urlContinueCxP%>?id_validacion=" + idValidacion;
                                        $("#continuar_cxp").attr("onClick","goToUrl('"+urlCxP +"');");
                                        
                                        apprise('<center><img src=../../images/info.png> <br/>Comprobante Fiscal Válido para Comprobar Gastos (Cuentas por Pagar)</center>',{'animate':true, 'textOk':'Continuar'},
                                            function(r){
                                                location.href = urlCxP;
                                        });
                                        
                                        $("#ajax_loading").fadeOut("slow");
                                        $("#action_buttons").fadeIn("slow");
                                        $("#continuar_cxp").fadeIn("slow");
                                        $("#regresar").fadeOut("slow");
                                    }else{
                                        $("#ajax_loading").html("<b>No se puede continuar. El comprobante es inválido </b> <br/>Validación: <br/>" + mensajesError);
                                        $("#action_buttons").fadeIn("slow");
                                    }
                                }else{
                                   $("#ajax_loading").html(datos);
                                   $("#action_buttons").fadeIn("slow");
                               }
                            <% } %>
                        }
                    });
                }
            }

            function validar(){
                
                if(jQuery.trim($("#nombreArchivoXml").val())===""){
                    apprise('<center><img src=../../images/warning.png> <br/>Suba un archivo</center>',{'animate':true});
                    //$("#nombreArchivoXml").focus();
                    return false;
                }
                
                return true;
            }
            
            function recuperarNombreArchivoXML(nombreImagen){
                $('#nombreArchivoXml').val('' + nombreImagen);
            }
            
            function goToUrl(url){
                location.href = url;
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
                    <h1>Validador</h1>
                    
                    <div style="font-size: 14px;font-weight: bold;"><tr><td><img src="../../images/icon_campanita.png" alt="icon"/><%= labelCreditosDisponibles %></td></tr></div>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <div id="info_addendas" class="alert_info" style="display: block;">
                        No se validan correctamente Comprobantes que contengan Addendas, ya que dicho uso solo es comercial y no Oficial para el SAT. 
                        <br/>
                        En caso de que su comprobante contenga alguna Addenda, antes de validarlo, debe eliminarlas manualmente del archivo XML.
                    </div>
                    
                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_validaXML.png" alt="icon"/>
                                   
                                    Carga de Archivo XML
                                   
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idArchivoXml" name="idArchivoXml" value="" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />                                                                                                       
                                    
                                    <p>
                                        <label>*Subir Archivo (.xml)</label><br/>
                                        <iframe src="../file/uploadSingleForm.jsp?id=archivoXML&validate=xml&queryCustom=isXMLAdjunto=1" 
                                                id="iframe_archivos_jpg" 
                                                name="iframe_archivos_jpg" 
                                                style="border: none" scrolling="no"
                                                height="80" width="400">
                                            <p>Tu navegador no soporta iframes y son necesarios para el buen funcionamiento del aplicativo</p>
                                        </iframe>
                                    </p>
                                    
                                    <p>
                                        <label>*Nombre Archivo:</label><br/>
                                        <input maxlength="30" type="text" id="nombreArchivoXml" name="nombreArchivoXml" style="width:300px"
                                               readonly value=""/>
                                    </p>
                                    <br/>
                                    <br/>
                                    
                                    <div id="action_buttons">
                                        <p>
                                            <input type="button" id="enviar" value="Validar" onclick="grabar();"/>
                                            <input type="button" id="regresar" value="Regresar" onclick="location.href='<%= urlBack %>';"/>
                                            <% if (mode.equals("1")){ %>
                                            <input type="button" id="continuar_cxp" value="Continuar CxP" style="display: none;"
                                                   onclick="location.href='#';"/>
                                            <%}%>
                                        </p>
                                    </div>
                                    
                            </div>
                        </div>
                        <!-- End left column window -->
                        
                        
                    </div>
                          
                    </br>
                    </br>
                    </br>
                    <div class="onecolumn" style="position: relative; margin-top: 280px; <%= mode.equals("1")?"display: none":"" %>;">
                        <div class="header">
                            <span>
                                Validaciones
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <center><div id="div_validacion_ajax_loading" class="alert_warning noshadow" style="display: none;"></div></center>
                            <div id="div_ajax_validaciones" class="ajax_selected_box"></div>
                        </div>
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