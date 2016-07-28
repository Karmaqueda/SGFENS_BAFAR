<%-- 
    Document   : catConceptos_formCnDscto
    Created on : 29/09/2014, 12:59:53 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.dao.dto.SgfensClienteVendedor"%>
<%@page import="com.tsp.sct.bo.SGClienteVendedorBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.bo.PasswordBO"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.bo.CategoriaBO"%>
<%@page import="com.tsp.sct.bo.EmbalajeBO"%>
<%@page import="com.tsp.sct.bo.MarcaBO"%>
<%@page import="com.tsp.sct.bo.ImpuestoBO"%>
<%@page import="com.tsp.sct.bo.AlmacenBO"%>
<%@page import="com.tsp.sct.dao.jdbc.ConceptoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
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
        int idConcepto = 0;
        try {
            idConcepto = Integer.parseInt(request.getParameter("idConcepto"));
        } catch (NumberFormatException e) {
        }

        /*
         *  
         *   1 = Nuevo
         *   2 = Eliminar  
         *   3 =  Editar
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        
        ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
        Concepto conceptosDto = null;
        if (idConcepto > 0){
            conceptoBO = new ConceptoBO(idConcepto,user.getConn());
            conceptosDto = conceptoBO.getConcepto();
        }       
        
        String parameter1 = "idConcepto";
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
                        url: "catConcepto_ajaxCnDscnto.jsp",
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
                                            location.href = "catConceptos_listCnDscnto.jsp?pagina="+"<%=paginaActual%>";
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
        <script type="text/javascript">
            
            function mostrarCalendario(){
                $( "#fecha_caducidad" ).datepicker({
                        minDate: +1,
                        gotoCurrent: true,
                        changeMonth: true,
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 100);
                            }, 500)}
                });
            }
            
            function validaDescuento(dscnto){
                if(dscnto == 1){
                    $("#descuentoMontoConcepto").val("0.0");
                }else if(dscnto == 2){
                    $("#descuentoPorcentajeConcepto").val("0.0");
                }
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
                    <h1>Almácen</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_producto.png" alt="icon"/>
                                    <% if(conceptosDto!=null){%>
                                    Editar Concepto ID <%=conceptosDto!=null?conceptosDto.getIdConcepto():"" %>
                                    <%}else{%>
                                    Concepto
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idConcepto" name="idConcepto" value="<%=conceptosDto!=null?conceptosDto.getIdConcepto():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>*Nombre:</label><br/>
                                        <input maxlength="100" type="text" id="nombreConcepto" name="nombreConcepto" style="width:300px"
                                               value="<%=conceptosDto!=null?conceptoBO.getNombreConceptoLegible():"" %>"
                                               <%=conceptosDto!=null?"readonly":""%>/>
                                    </p>                                    
                                    <br/> 
                                    <p>
                                        <label>*Precio Menudeo/Unitario:</label><br/>
                                        <input maxlength="16" type="text" id="precioConcepto" name="precioConcepto" style="width:300px"
                                               value="<%=conceptosDto!=null?conceptoBO.getConcepto().getPrecio():"0" %>"
                                               onkeypress="return validateNumber(event);" <%=conceptosDto!=null?"readonly":""%>/>
                                    </p>
                                    <br/>                                    
                                                                       
                                     <div id="action_buttons">
                                        <p>
                                            <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
                                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                        </p>
                                    </div>                                    
                               
                             </div>
                        </div>
                        <!-- End left column window -->
                        
                        <!--Columna derecha-->
                        <div class="column_right" style="height: 237px;">
                            <div class="header">
                                <span>
                                   LLene un solo dato 
                                </span>
                            </div>  
                            <br class="clear"/>
                            <div class="content">
                                    <p>
                                        <label>Monto Porcentaje:</label><br/>
                                        <input maxlength="10" type="text" id="descuentoPorcentajeConcepto" name="descuentoPorcentajeConcepto" style="width:300px"
                                               value="<%=conceptosDto!=null?conceptoBO.getConcepto().getDescuentoPorcentaje():"0" %>"
                                               onkeypress="return validateNumber(event);" onchange="validaDescuento(1);"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Monto Descuento:</label><br/>
                                        <input maxlength="50" type="text" id="descuentoMontoConcepto" name="descuentoMontoConcepto" style="width:300px"
                                               value="<%=conceptosDto!=null?conceptoBO.getConcepto().getDescuentoMonto():"0" %>"
                                               onkeypress="return validateNumber(event);" onchange="validaDescuento(2);"/>
                                    </p>
                                                                       
                                    <br/><br/>
                            </div>
                        </div>
                        <!--Fin Columna derecha-->
                        
                        
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