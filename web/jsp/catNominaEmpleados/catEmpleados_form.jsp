<%-- 
    Document   : catEmpleados_form
    Created on : 11/12/2013, 05:52:53 PM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.bo.NominaRiesgoPuestoBO"%>
<%@page import="com.tsp.sct.bo.NominaPeriodicidadPagoBO"%>
<%@page import="com.tsp.sct.bo.NominaTipoJornadaBO"%>
<%@page import="com.tsp.sct.bo.NominaTipoContratoBO"%>
<%@page import="com.tsp.sct.bo.NominaDepartamentoBO"%>
<%@page import="com.tsp.sct.bo.NominaPuestoBO"%>
<%@page import="com.tsp.sct.bo.NominaBancoBO"%>
<%@page import="com.tsp.sct.bo.NominaRegimenFiscalBO"%>
<%@page import="com.tsp.sct.bo.NominaEmpleadoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.NominaEmpleadoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.NominaEmpleado"%>
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
        int idNominaEmpleado = 0;
        try {
            idNominaEmpleado = Integer.parseInt(request.getParameter("idNominaEmpleado"));
        } catch (NumberFormatException e) {
        }

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        String newRandomPass = "";
        
        NominaEmpleadoBO nominaEmpleadoBO = new NominaEmpleadoBO(user.getConn());
        NominaEmpleado nominaEmpleadosDto = null;
        if (idNominaEmpleado > 0){
            nominaEmpleadoBO = new NominaEmpleadoBO(user.getConn(),idNominaEmpleado);
            nominaEmpleadosDto = nominaEmpleadoBO.getNominaEmpleado();
        }else{
            System.out.println("----id nomina empleado menor a 0");
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
                        url: "catEmpleados_ajax.jsp",
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
                                            <% if (!mode.equals("3")) {%>
                                                location.href = "catEmpleados_list.jsp?pagina="+"<%=paginaActual%>";
                                            <%}else{%>
                                                parent.recargarSelectClientes();
                                                parent.$.fancybox.close();
                                            <%}%>
                                            
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
            
             $(document).ready(function() {
                //Si se recibio el parametro para que el modo sea en forma de popup
                <%= mode.equals("3")? "mostrarFormPopUpMode();":""%>
            });
            
            function mostrarFormPopUpMode(){
		$('#left_menu').hide();
                $('#header').hide();
		//$('#show_menu').show();
		$('body').addClass('nobg');
		$('#content').css('marginLeft', 30);
		$('#wysiwyg').css('width', '97%');
		setNotifications();
            }
            
            function mostrarCalendario(){
                $( "#fechaInicioRelaNominaEmpleado" ).datepicker({
                        //minDate: 0,
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
            
             <% if (!mode.equals("3")) {%>
                <jsp:include page="../include/header.jsp" flush="true"/>
                <jsp:include page="../include/leftContent.jsp"/>
            <% } %>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Empleado</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_empleadoNomina.png" alt="icon"/>
                                    <% if(nominaEmpleadosDto!=null){%>
                                    Editar Empleado ID <%=nominaEmpleadosDto!=null?nominaEmpleadosDto.getIdEmpleado():"" %>
                                    <%}else{%>
                                    Empleado
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idNominaEmpleado" name="idNominaEmpleado" value="<%=nominaEmpleadosDto!=null?nominaEmpleadosDto.getIdEmpleado():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>*Nombre:</label><br/>
                                        <input maxlength="40" type="text" id="nombreNominaEmpleado" name="nombreNominaEmpleado" style="width:300px"
                                               value="<%=nominaEmpleadosDto!=null?nominaEmpleadoBO.getNominaEmpleado().getNombre():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Apellido Paterno:</label><br/>
                                        <input maxlength="40" type="text" id="apePaterNominaEmpleado" name="apePaterNominaEmpleado" style="width:300px"
                                               value="<%=nominaEmpleadosDto!=null?nominaEmpleadoBO.getNominaEmpleado().getApellidoPaterno():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Apellido Materno:</label><br/>
                                        <input maxlength="40" type="text" id="apeMaterNominaEmpleado" name="apeMaterNominaEmpleado" style="width:300px"
                                               value="<%=nominaEmpleadosDto!=null?nominaEmpleadoBO.getNominaEmpleado().getApellidoMaterno():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*RFC:</label><br/>
                                        <input maxlength="13" type="text" id="rfcNominaEmpleado" name="rfcNominaEmpleado" style="width:300px"
                                               value="<%=nominaEmpleadosDto!=null?nominaEmpleadoBO.getNominaEmpleado().getRfc():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*CURP:</label><br/>
                                        <input maxlength="35" type="text" id="curpNominaEmpleado" name="curpNominaEmpleado" style="width:300px"
                                               value="<%=nominaEmpleadosDto!=null?nominaEmpleadoBO.getNominaEmpleado().getCurp():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Número Empleado:</label><br/>
                                        <input maxlength="15" type="text" id="numNominaEmpleado" name="numNominaEmpleado" style="width:300px"
                                               value="<%=nominaEmpleadosDto!=null?nominaEmpleadoBO.getNominaEmpleado().getNumEmpleado():"" %>"/>
                                    </p>
                                    <br/>
                                    
                                     <p>
                                        <label>Número Seguro Social:</label><br/>
                                        <input maxlength="20" type="text" id="numSeguroNominaEmpleado" name="numSeguroNominaEmpleado" style="width:300px"
                                               value="<%=nominaEmpleadosDto!=null?nominaEmpleadoBO.getNominaEmpleado().getNumSeguroSocial():"" %>"/>
                                    </p>
                                    <br/>
                                    
                                    <p>
                                    <label>Fecha Inicio Relación Laboral:</label><br/>
                                    <input type="text" name="fechaInicioRelaNominaEmpleado" id="fechaInicioRelaNominaEmpleado" readonly style="width:300px"
                                           value="<%= nominaEmpleadosDto!=null?DateManage.formatDateToNormal(nominaEmpleadoBO.getNominaEmpleado().getFechaInicioRelacionLaboral()):"" %>"
                                           style="width: 80px;"/>
                                    </p>
                                    <br/>
                                    
                                    <p>
                                        <label>Salario Base:</label><br/>
                                        <input maxlength="20" type="text" id="salarioBaseCotNominaEmpleado" name="salarioBaseCotNominaEmpleado" style="width:300px"
                                               value="<%=nominaEmpleadosDto!=null?nominaEmpleadoBO.getNominaEmpleado().getSalarioBaseCotApor():"" %>"/>
                                    </p>
                                    <br/>
                                    
                                    <p>
                                        <label>Salario Diario Integrado:</label><br/>
                                        <input maxlength="20" type="text" id="salarioDiarioIntNominaEmpleado" name="salarioDiarioIntNominaEmpleado" style="width:300px"
                                               value="<%=nominaEmpleadosDto!=null?nominaEmpleadoBO.getNominaEmpleado().getSalarioDiarioIntegrado():"" %>"/>
                                    </p>
                                    <br/>
                                    
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=nominaEmpleadosDto!=null?(nominaEmpleadosDto.getIdEstatus()==1?"checked":""):"checked" %> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
                                    </p>
                                    <br/><br/>
                                    
                                    <% if (!mode.equals("3")) {%>
                                        <div id="action_buttons">
                                            <p>
                                                <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
                                                <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                            </p>
                                        </div>
                                    <%}else{
                                        //En caso de ser Formulario en modo PopUp
                                    %>
                                        <div id="action_buttons">
                                            <p>
                                                <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
                                                <input type="button" id="regresar" value="Cerrar" onclick="parent.$.fancybox.close();"/>
                                            </p>
                                        </div>
                                    <%}%>
                                    
                            </div>
                        </div>
                        <!-- End left column window -->
                        <div class="column_right">
                            <div class="header">
                                <span>                                    
                                    Datos
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                        
                                    <p>
                                        <label>*Régimen:</label><br/>
                                        <select size="1" id="idRegimenNominaEmpleado" name="idRegimenNominaEmpleado" style="width:311px">
                                            <option value="-1">Selecciona un Régimen</option>
                                                <%
                                                    out.print(new NominaRegimenFiscalBO(user.getConn()).getNominaRegimenFiscalsByIdHTMLCombo((nominaEmpleadosDto!=null?nominaEmpleadoBO.getNominaEmpleado().getIdNominaRegimenFiscal():-1) ));
                                                %>
                                        </select>                                        
                                    </p> 
                                    <br/>
                                    
                                    <p>
                                        <label>Banco:</label><br/>
                                        <select size="1" id="idBancoNominaEmpleado" name="idBancoNominaEmpleado" style="width:311px">
                                            <option value="-1">Selecciona un Banco</option>
                                                <%
                                                    out.print(new NominaBancoBO(user.getConn()).getNominaBancosByIdHTMLCombo((nominaEmpleadosDto!=null?nominaEmpleadoBO.getNominaEmpleado().getIdNominaBanco():-1) ));
                                                %>
                                        </select>                                        
                                    </p> 
                                    <br/>
                                    <p>
                                        <label>CLABE Interbancaria</label><br/>
                                        <input maxlength="18" type="text" id="clabeNominaEmpleado" name="clabeNominaEmpleado" style="width:300px"
                                               value="<%=nominaEmpleadosDto!=null?nominaEmpleadoBO.getNominaEmpleado().getClabe():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Puesto:</label><br/>
                                        <select size="1" id="idPuestoNominaEmpleado" name="idPuestoNominaEmpleado" style="width:311px">
                                            <option value="-1">Selecciona un Puesto</option>
                                                <%
                                                    out.print(new NominaPuestoBO(user.getConn()).getNominaPuestosByIdHTMLCombo(idEmpresa,(nominaEmpleadosDto!=null?nominaEmpleadoBO.getNominaEmpleado().getIdPuesto():-1) ));
                                                %>
                                        </select>                                        
                                    </p> 
                                    <br/>
                                    
                                    <p>
                                        <label>Riesgo Puesto:</label><br/>
                                        <select size="1" id="idRiesgoPuestoNominaEmpleado" name="idRiesgoPuestoNominaEmpleado" style="width:311px">
                                            <option value="-1">Selecciona Riesgo Puesto</option>
                                                <%
                                                    out.print(new NominaRiesgoPuestoBO(user.getConn()).getNominaRiesgoPuestosByIdHTMLCombo(idEmpresa,(nominaEmpleadosDto!=null?nominaEmpleadoBO.getNominaEmpleado().getIdRiesgoPuesto():-1) ));
                                                %>
                                        </select>                                        
                                    </p> 
                                    <br/>
                                    
                                    <p>
                                        <label>Contrato:</label><br/>
                                        <select size="1" id="contratoNominaEmpleado" name="contratoNominaEmpleado" style="width:311px">
                                            <option value="-1">Selecciona un Tipo de Contrato</option>
                                                <%
                                                    out.print(new NominaTipoContratoBO(user.getConn()).getNominaTipoContratosByIdHTMLCombo(idEmpresa,(nominaEmpleadosDto!=null?nominaEmpleadoBO.getNominaEmpleado().getTipoContrato():"-1") ));
                                                %>
                                        </select>                                        
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Departamento:</label><br/>
                                        <select size="1" id="idDepartamentoNominaEmpleado" name="idDepartamentoNominaEmpleado" style="width:311px">
                                            <option value="-1">Selecciona un Departamento</option>
                                                <%
                                                    out.print(new NominaDepartamentoBO(user.getConn()).getNominaDepartamentosByIdHTMLCombo(idEmpresa,(nominaEmpleadosDto!=null?nominaEmpleadoBO.getNominaEmpleado().getIdDepartamento():-1) ));
                                                %>
                                        </select>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Jornada:</label><br/>
                                        <select size="1" id="jornadaNominaEmpleado" name="jornadaNominaEmpleado" style="width:311px">
                                            <option value="-1">Selecciona Tipo de Jornada</option>
                                                <%
                                                    out.print(new NominaTipoJornadaBO(user.getConn()).getNominaTipoJornadasByIdHTMLCombo(idEmpresa,(nominaEmpleadosDto!=null?nominaEmpleadoBO.getNominaEmpleado().getTipoJornada():"-1") ));
                                                %>
                                        </select>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Periodicidad Pago:</label><br/>
                                        <select size="1" id="periodicidadPagoNominaEmpleado" name="periodicidadPagoNominaEmpleado" style="width:311px">
                                            <option value="-1">Selecciona Tipo de Periodicidad</option>
                                                <%
                                                    out.print(new NominaPeriodicidadPagoBO(user.getConn()).getNominaPeriodicidadPagosByIdHTMLCombo(idEmpresa,(nominaEmpleadosDto!=null?nominaEmpleadoBO.getNominaEmpleado().getPeriodicidadPago():"-1") ));
                                                %>
                                        </select>
                                    </p> 
                                    <br/>
                                    <p>
                                        <label>Correo:</label><br/>
                                        <input maxlength="80" type="text" id="correoNominaEmpleado" name="correoNominaEmpleado" style="width:300px"
                                               value="<%=nominaEmpleadosDto!=null?nominaEmpleadoBO.getNominaEmpleado().getCorreo():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Teléfono:</label><br/>
                                        <input maxlength="20" type="text" id="telefonoNominaEmpleado" name="telefonoNominaEmpleado" style="width:300px"
                                               value="<%=nominaEmpleadosDto!=null?nominaEmpleadoBO.getNominaEmpleado().getTelefono():"" %>"/>
                                    </p>
                                    <br/>
                                    
                            </div>
                        </div>
                        
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