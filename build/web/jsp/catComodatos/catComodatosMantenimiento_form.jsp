<%-- 
    Document   : catComodatoMantenimientosMantenimiento_form
    Created on : 4/03/2016, 06:38:15 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.ComodatoBO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.bo.AlmacenBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensClienteVendedor"%>
<%@page import="com.tsp.sct.bo.SGClienteVendedorBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.bo.ComodatoMantenimientoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.ComodatoMantenimientoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.ComodatoMantenimiento"%>
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
        int idComodatoMantenimiento = 0;
        try {
            idComodatoMantenimiento = Integer.parseInt(request.getParameter("idComodatoMantenimiento"));
        } catch (NumberFormatException e) {
        }
        
        /*
         * Parámetros
         */
        int idComodato = -1;
        try {
            idComodato = Integer.parseInt(request.getParameter("idComodato"));
        } catch (NumberFormatException e) {
        }

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        String newRandomPass = "";
        
        ComodatoMantenimientoBO comodatoMantenimientoBO = new ComodatoMantenimientoBO(user.getConn());
        ComodatoMantenimiento comodatoMantenimientosDto = null;
        if (idComodatoMantenimiento > 0){
            comodatoMantenimientoBO = new ComodatoMantenimientoBO(idComodatoMantenimiento,user.getConn());
            comodatoMantenimientosDto = comodatoMantenimientoBO.getComodatoMantenimiento();
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
                
                var idComodatoStr = document.getElementById("idComodato").value;
                        
                if(validar(idComodatoStr)){
                    $.ajax({
                        type: "POST",
                        url: "catComodatos_ajax.jsp",
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
                                            location.href = "catComodatosMantenimiento_list.jsp?idComodato="+idComodatoStr+"&acc=<%=mode%>&pagina=<%=paginaActual%>";
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

            function validar(idComodatoStr){
                
                if(idComodatoStr<0){
                    apprise('<center><img src=../../images/warning.png> <br/>El comodato es requerido</center>',{'animate':true});                    
                    return false;
                }
                
                return true;
            }
            
            function mostrarCalendario(){
                $( "#fechaMantenimiento" ).datepicker({
//                        minDate: 0,
                        gotoCurrent: true,
                        changeMonth: true,
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 100);
                            }, 500)}
                });
                $( "#fechaProxMantenimiento" ).datepicker({
//                        minDate: 0,
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
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>ComodatoMantenimientos</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_comodatoMantenimiento.png" alt="icon"/>
                                    <% if(comodatoMantenimientosDto!=null){%>
                                    Editar Comodato Mantenimiento ID <%=comodatoMantenimientosDto!=null?comodatoMantenimientosDto.getIdComodatoMantenimiento():"" %>
                                    <%}else{%>
                                    Comodato Mantenimiento
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idComodatoMantenimiento" name="idComodatoMantenimiento" value="<%=comodatoMantenimientosDto!=null?comodatoMantenimientosDto.getIdComodatoMantenimiento():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <input type="hidden" id="idComodato" name="idComodato" value="<%=idComodato%>" />
                                    
                                    <p>
                                        <label>*Comodato:</label><br/>
                                        <select id="idComodato" name="idComodato" class="flexselect">
                                            <option value="-1" ></option>
                                            <%= new ComodatoBO(user.getConn()).getComodatosByIdHTMLCombo(idEmpresa, idComodato ) %>
                                        </select>
                                    </p>
                                    <br/>
                                    
                                    <p>
                                        <label>Técnico:</label><br/>
                                        <input maxlength="120" type="text" id="tecnicoComodatoMantenimiento" name="tecnicoComodatoMantenimiento" style="width:300px"
                                               value="<%=comodatoMantenimientosDto!=null?comodatoMantenimientoBO.getComodatoMantenimiento().getTecnico():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Descripción:</label><br/>
                                        <input maxlength="950" type="text" id="descripcionComodatoMantenimiento" name="descripcionComodatoMantenimiento" style="width:300px"
                                               value="<%=comodatoMantenimientosDto!=null?comodatoMantenimientoBO.getComodatoMantenimiento().getDescripcion():"" %>"/>
                                    </p>
                                    <br/>                                   
                                    <p>
                                        <label>Estatus:</label><br/>
                                        <select size="1" id="estatusComodatoMantenimiento" name="estatusComodatoMantenimiento">                                            
                                            <option value="1" <%=comodatoMantenimientosDto!=null?(comodatoMantenimientoBO.getComodatoMantenimiento().getEstatus()==1?"selected":""):"selected" %> >Mantenimiento Exitoso</option>
                                            <option value="2" <%=comodatoMantenimientosDto!=null?(comodatoMantenimientoBO.getComodatoMantenimiento().getEstatus()==2?"selected":""):"selected" %> >Descompuesto</option>
                                            <option value="3" <%=comodatoMantenimientosDto!=null?(comodatoMantenimientoBO.getComodatoMantenimiento().getEstatus()==3?"selected":""):"selected" %> >Pendiente</option>
                                        </select>                                        
                                    </p>
                                    <br/>                                    
                                    <p>
                                        <label>Nombre de la Persona que Atendió:</label> <br/>
                                        <input maxlength="120" type="text" id="nombreAtendioComodatoMantenimiento" name="nombreAtendioComodatoMantenimiento" style="width:300px"
                                               value="<%=comodatoMantenimientosDto!=null?comodatoMantenimientoBO.getComodatoMantenimiento().getNombreAtendio():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>                                    
                                        <label>Fecha del Mantenimiento:</label>
                                        <input maxlength="15" type="text" id="fechaMantenimiento" name="fechaMantenimiento" style="width:100px"                                                
                                        value="<%=comodatoMantenimientosDto!=null?(comodatoMantenimientoBO.getComodatoMantenimiento().getFechaRealizacionMantenimiento()!=null&&!comodatoMantenimientoBO.getComodatoMantenimiento().getFechaRealizacionMantenimiento().toString().trim().equals("")?DateManage.formatDateToNormal(comodatoMantenimientoBO.getComodatoMantenimiento().getFechaRealizacionMantenimiento()):""):""%>" readonly/>
                                    </p>
                                    <br/>
                                    <p>                                    
                                        <label>Fecha Próximo Mantenimiento:</label>
                                        <input maxlength="15" type="text" id="fechaProxMantenimiento" name="fechaProxMantenimiento" style="width:100px"                                                
                                                value="<%=comodatoMantenimientosDto!=null?(comodatoMantenimientoBO.getComodatoMantenimiento().getFechaProxMantenimiento()!=null&&!comodatoMantenimientoBO.getComodatoMantenimiento().getFechaProxMantenimiento().toString().trim().equals("")?DateManage.formatDateToNormal(comodatoMantenimientoBO.getComodatoMantenimiento().getFechaProxMantenimiento()):""):""%>" readonly/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Costo:</label> <br/>
                                        <input maxlength="10" type="text" id="costoComodatoMantenimiento" name="costoComodatoMantenimiento" style="width:300px"
                                               value="<%=comodatoMantenimientosDto!=null?comodatoMantenimientoBO.getComodatoMantenimiento().getCosto():"" %>"
                                               onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                                    
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

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>

        <script>
            mostrarCalendario();
            $("select.flexselect").flexselect();
        </script>
    </body>
</html>
<%}%>
