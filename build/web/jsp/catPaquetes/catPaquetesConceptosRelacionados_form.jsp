<%-- 
    Document   : catPaquetesPaquetesRelacionados_form
    Created on : 25/09/2014, 11:56:32 AM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.dao.dto.Paquete"%>
<%@page import="com.tsp.sct.bo.PaqueteBO"%>
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
        int idPaquete = 0;
        try {
            idPaquete = Integer.parseInt(request.getParameter("idPaquete"));
        } catch (NumberFormatException e) {
        }

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        
        PaqueteBO paqueteBO = new PaqueteBO(user.getConn());
        Paquete paquetesDto = null;
        if (idPaquete > 0){
            paqueteBO = new PaqueteBO(idPaquete, user.getConn());
            paquetesDto = paqueteBO.getPaquete();
        }       
        String parameter1 = "idPaquete";
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
                        url: "catPaquetes_ajax.jsp",
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
                                            location.href = "catPaquetesConceptosRelacionados_list.jsp?idPaquete=<%=idPaquete%>";
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
            
            function iniciarFlexSelect(){
                $("#producto_select").flexselect({
                    jsFunction:  function(id) {  }
                });
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
            
        </script>
    </head>
    <body class="nobg">
                <div class="inner">
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                        <div class="onecolumn">
                        <div class="column_left" style="width: 543px;">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_producto.png" alt="icon"/>
                                    <% if(paquetesDto!=null){%>
                                    Editar Paquete ID <%=paquetesDto!=null?paquetesDto.getNombre():"" %>
                                    <%}else{%>
                                    Paquete
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idPaquete" name="idPaquete" value="<%=paquetesDto!=null?paquetesDto.getIdPaquete():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=2%>" />
                                    <p>
                                        <label>*Paquete:</label><br/>
                                        <input maxlength="100" type="text" id="nombrePaquete" name="nombrePaquete" style="width:180px"
                                               value="<%=paquetesDto!=null?paqueteBO.getPaquete().getNombre():"Nuevo Paquete" %>"
                                               readonly />
                                    </p>
                                    <br/>                                    
                                                                        
                                    <p>
                                        <label>Producto:</label><br/>
                                        <select size="1" name="producto_select" id="producto_select"  style="width:180px;"                                                        
                                                        class="flexselect">
                                            <option value="-1"></option>
                                            <%out.print(new ConceptoBO(user.getConn()).getConceptosByIdHTMLCombo(idEmpresa, -1 )); %>
                                        </select>
                                    </p>
                                    <br/>
                                    
                                    <p>
                                        <label>*Cantidad</label><br/>
                                        <input maxlength="100" type="text" id="cantidadConceptoPaquete" name="cantidadConceptoPaquete" style="width:180px"
                                               value="0" onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Precio</label><br/>
                                        <input maxlength="100" type="text" id="precioConceptoPaquete" name="precioConceptoPaquete" style="width:180px"
                                               value="0" onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                        
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=paquetesDto!=null?(paquetesDto.getIdEstatus()==1?"checked":""):"checked" %> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
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
        <script>
            mostrarCalendario();
            iniciarFlexSelect();
        </script>
    </body>
</html>
<%}%>