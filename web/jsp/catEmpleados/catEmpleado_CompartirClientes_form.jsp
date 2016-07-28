<%-- 
    Document   : catEmpleado_CompartirClientes_form
    Created on : 15/12/2014, 06:23:28 PM
    Author     : 578
--%>

<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el cliente tiene acceso a este topico
    if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
        response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
        response.flushBuffer();
    } else {

        int idEmpresa = user.getUser().getIdEmpresa();
                      
        String mode = "compartirAgenda";         
        
        
        int idEmpleadoOrigen = -1;
        try{ idEmpleadoOrigen = Integer.parseInt(request.getParameter("q_idvendedor")); }catch(NumberFormatException e){}
        
        
        
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
                                            location.href = "../catEmpleados/catEmpleado_Clientes_List.jsp";
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
            
            
            function todosDiasMarcados(){
                if ($('#dias').attr('checked')) {
                    //como esta marcado, desmarcamos todos los demas                
                    $('#domingo').attr('checked', true);
                    $('#lunes').attr('checked', true);
                    $('#martes').attr('checked', true);
                    $('#miercoles').attr('checked', true);
                    $('#jueves').attr('checked', true);
                    $('#viernes').attr('checked', true);
                    $('#sabado').attr('checked', true);                    
                }else{
                    //como esta marcado, desmarcamos todos los demas                
                    $('#domingo').attr('checked', false);
                    $('#lunes').attr('checked', false);
                    $('#martes').attr('checked', false);
                    $('#miercoles').attr('checked', false);
                    $('#jueves').attr('checked', false);
                    $('#viernes').attr('checked', false);
                    $('#sabado').attr('checked', false); 
                } 
                
                checks();
            }            
           
            
            function checks() {
                
                var cDomingo = $("#domingo").attr("checked");
                var cLunes = $("#lunes").attr("checked");
                var cMartes = $("#martes").attr("checked");
                var cMiercoles = $("#miercoles").attr("checked");
                var cJueves = $("#jueves").attr("checked");
                var cViernes = $("#viernes").attr("checked");
                var cSabado = $("#sabado").attr("checked");
                var cClientes = $("#clientes").attr("checked");
                
                
                if(!cDomingo){ $("#domingo").val(""); }else{ $("#domingo").val("DOM"); $('#clientes').attr('checked', false);}  
                if(!cLunes){ $("#lunes").val(""); }else{ $("#lunes").val("LUN"); $('#clientes').attr('checked', false);}
                if(!cMartes){ $("#martes").val(""); }else{ $("#martes").val("MAR");$('#clientes').attr('checked', false);}
                if(!cMiercoles){$("#miercoles").val("");}else{ $("#miercoles").val("MIE");$('#clientes').attr('checked', false);}
                if(!cJueves){ $("#jueves").val(""); }else{ $("#jueves").val("JUE"); $('#clientes').attr('checked', false);} 
                if(!cViernes){ $("#viernes").val("");}else{ $("#viernes").val("VIE");$('#clientes').attr('checked', false);}
                if(!cSabado){ $("#sabado").val(""); }else{  $("#sabado").val("SAB"); $('#clientes').attr('checked', false); }                
                if(!cClientes){ $("#clientes").val(""); }else{  $("#clientes").val("CLIENTES"); } 
                    
                   
            }         
            
            
            
            
            
            function mostrarCalendario(){                

                /*var dates = $('#q_fh_min').datepicker({
                        //minDate: 0,
			changeMonth: true,
			//numberOfMonths: 2,
                        //beforeShow: function() {$('#fh_min').css("z-index", 9999); },
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 998);
                            }, 500)},
			onSelect: function( selectedDate ) {
				var option = this.id == "q_fh_min" ? "minDate" : "maxDate",
					instance = $( this ).data( "datepicker" ),
					date = $.datepicker.parseDate(
						instance.settings.dateFormat ||
						$.datepicker._defaults.dateFormat,
						selectedDate, instance.settings );
				dates.not( this ).datepicker( "option", option, date );
			}
		});*/
                
                $( "#q_fh_min" ).datepicker({
                        minDate: 0,
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
                    <h1>Pretoriano Móvil</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/inventario_16.png" alt="icon"/>
                                    Compartir Clientes
                            </div>
                            <br class="clear"/>
                            <div class="content">                                    
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <input type="hidden" id="idEmpleadoOrigen" name="idEmpleadoOrigen" value="<%=idEmpleadoOrigen%>" />
                                    <p>
                                        <label>Clientes que desea compartir:</label><br/><br/>
                                        <input type="checkbox" class="checkbox" id="domingo" name="domingo" value="" onchange="checks();" > <label for="domingo">Domingo</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" id="lunes" name="lunes" value="" onchange="checks();"> <label for="lunes">Lunes</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" id="martes" name="martes" value="" onchange="checks();"> <label for="martes">Martes</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" id="miercoles" name="miercoles" value="" onchange="checks();" > <label for="miercoles">Miércoles</label>
                                        &nbsp;&nbsp;&nbsp;
                                    </p>
                                    <p>
                                        <input type="checkbox" class="checkbox" id="jueves" name="jueves" value="" onchange="checks();" > <label for="jueves">Jueves</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" id="viernes" name="viernes" value="" onchange="checks();" > <label for="viernes">Viernes</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" id="sabado" name="sabado" value="" onchange="checks();"> <label for="sabado">Sábado</label>  
                                    </p>
                                    <br/>                                    
                                    <br/>
                                    <p> 
                                        <input type="radio" class="checkbox" id="dias" value ="" name="tipoAsignacion" onclick="todosDiasMarcados();"> <label for="diarioReporte">Todos los días</label>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        <input type="radio" class="checkbox" id="clientes" value ="" name="tipoAsignacion" onclick="todosDiasMarcados();"> <label for="diarioReporte">Todos los Clientes</label>
                                    </p>
                                    <br/>    
                                    <p>
                                        <label>Compartir Hasta:</label><br/>
                                        <input type="text" id="q_fh_min" name="q_fh_min" 
                                               value="<%//=empresaConfigDto!=null?""+empresaConfigDto.getHoraCorte():"" %>"/>
                                    </p>  
                                    <br/>    
                                    <p>
                                        <label>Compartir con Vendedor:</label><br/>
                                        <select id="q_idvendedorDestino" name="q_idvendedorDestino" class="flexselect" title="Vendedor" style="float: left;">
                                        <option value =""></option>
                                        <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL, 0) %>                                         
                                        <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_CONDUCTOR_VENDEDOR, 0) %> 
                                        <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_CONDUCTOR, 0) %>
                                        <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_COBRADOR, 0) %>
                                        </select>
                                        
                                    </p>   
                                    <br/><br/><br/>

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