<%-- 
    Document   : catImpuestoRelacionado_form
    Created on : 4/04/2014, 12:22:07 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.ImpuestoBO"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
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
        int idConcepto = 0;
        try {
            idConcepto = Integer.parseInt(request.getParameter("idConcepto"));
        } catch (NumberFormatException e) {
        }

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        
        ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
        Concepto conceptosDto = null;
        if (idConcepto > 0){
            conceptoBO = new ConceptoBO(idConcepto, user.getConn());
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
                        url: "catConceptos_ajax.jsp",
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
                                            location.href = "catImpuestosRelacionados_list.jsp?idConcepto=<%=idConcepto%>";
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
            
        </script>
    </head>
    <body class="nobg">
                <div class="inner">
                    <h1>Catálogo</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                        <div class="onecolumn">
                        <div class="column_left" style="width: 543px;">
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
                                    <input type="hidden" id="mode" name="mode" value="<%=2%>" />
                                    <p>
                                        <label>*Nombre:</label><br/>
                                        <input maxlength="100" type="text" id="nombreConcepto" name="nombreConcepto" style="width:180px"
                                               value="<%=conceptosDto!=null?conceptoBO.getNombreConceptoLegible():"" %>"
                                               <%=conceptosDto!=null?"readonly":""%>/>
                                    </p>
                                    <br/>                                    
                                    
                                    <p>
                                        <label>Impuesto:</label><br/>
                                        <select size="1" id="idImpuestoConceptoB" name="idImpuestoConceptoB" >
                                            <option value="-1" >Exento de Impuesto</option>
                                                <%
                                                    out.print(new ImpuestoBO(user.getConn()).getImpuestosByIdHTMLCombo(idEmpresa, (conceptosDto!=null?((conceptoBO.getConcepto().getIdImpuesto()>0)?conceptoBO.getConcepto().getIdImpuesto():-1):-1) ));
                                                %>
                                        </select>
                                    </p>
                                    <br/>
                        
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=conceptosDto!=null?(conceptosDto.getIdEstatus()==1?"checked":""):"checked" %> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
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
        </script>
    </body>
</html>
<%}%>