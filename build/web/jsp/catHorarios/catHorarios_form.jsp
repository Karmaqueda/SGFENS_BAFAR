<%-- 
    Document   : catHorarios_form
    Created on : 02/12/2015, 11:33:07 AM
    Author     : Cesar Martinez
--%>

<%@page import="com.tsp.sct.bo.HorarioDetalleBO"%>
<%@page import="com.tsp.sct.dao.dto.HorarioDetalle"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="com.tsp.sct.dao.dto.Horario"%>
<%@page import="com.tsp.sct.bo.HorarioBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
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
    int idHorario = 0;
    try {
        idHorario = Integer.parseInt(request.getParameter("idHorario"));
        
    } catch (NumberFormatException e) {
    }


    /*
     *   0/"" = nuevo
     *   1 = editar/consultar
     *   2 = eliminar  
     */
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
   

    HorarioBO horarioBO = new HorarioBO(user.getConn());
    Horario horarioDto = null;    
    HorarioDetalleBO horarioDetalleBO =  new HorarioDetalleBO(user.getConn());
   
    
    if (idHorario > 0){
        horarioBO = new HorarioBO(idHorario,user.getConn());
        horarioDto = horarioBO.getHorario();
                
    }
    
    
%>

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
                        url: "catHorarios_ajax.jsp",
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
                                            location.href = "catHorarios_list.jsp?pagina="+"<%=paginaActual%>";
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

            
  
  $(function() {	
        $.timepicker.regional['es'] = {
    		timeOnlyTitle: 'Elegir Hora',
        	timeText: '',
        	hourText: 'Horas',
        	minuteText: 'Minutos',
        	secondText: 'Segundos',
        	currentText: 'Ahora',
        	closeText: 'Aceptar'
	    };
	 $.timepicker.setDefaults($.timepicker.regional['es']);  
    
	$('.time').timepicker({
		 hourMin: 0,
                 hourMax: 23,
		 addSliderAccess: true,
		 sliderAccessArgs: { touchonly: false },
		
	 });

  });
  
  
  </script>
        
    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Horarios</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    
                        <div class="onecolumn">
                            <div class="header">
                                <span>
                                    <img src="../../images/clock.png" alt="icon"/>
                                    <% if(horarioDto!=null){%>
                                    Editar Horario ID <%=horarioDto!=null?horarioDto.getIdHorario():"" %>
                                    <%}else{%>
                                    Horario
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                <input type="hidden" id="idHorario" name="idHorario" value="<%=horarioDto!=null?horarioDto.getIdHorario():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>*Nombre:</label><br/>
                                        <input maxlength="30" type="text" id="nombreHorario" name="nombreHorario" style="width:300px"
                                               value="<%=horarioDto!=null?horarioDto.getNombreHorario():"" %>"/>
                                    </p>
                                    <br/>                                                                                                            
                                    <br/>
                                    <p>
                                        <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                            <thead>
                                                <tr>
                                                    <th>Dia</th>
                                                    <th>Entrada</th>
                                                    <th>Salida</th>
                                                    <th>Tolerancia Entrada (min)</th>
                                                    <th>Comida Salida</th>  
                                                    <th>Comida Entrada</th>
                                                    <th>Periodo Comida (min)</th>  
                                                    <th>Descanso</th>                                                    
                                                </tr>
                                            </thead>
                                            <tbody>       
                                                <%
                                                
                                                
                                                HorarioDetalle horarioDetalleDto = null;
                                                DateFormat sdf = new SimpleDateFormat("HH:mm");
                                                
                                                for(int i= 1;i<=7;i++){  
                                                    String dia ="";
                                                    
                                                    if(i==1)
                                                        dia = "LUNES";
                                                    else if(i==2)
                                                        dia = "MARTES";
                                                    else if(i==3)
                                                        dia = "MIERCOLES";
                                                    else if(i==4)
                                                        dia = "JUEVES";
                                                    else if(i==5)
                                                        dia = "VIERNES";
                                                    else if(i==6)
                                                        dia = "SABADO";
                                                    else if(i==7)
                                                        dia = "DOMINGO";
                                                    
                                                    
                                                    
                                                    if (idHorario > 0){ 
                                                        
                                                        try{                                                    
                                                            String filtroDia =" AND DIA = '" + dia + "' " ;
                                                            horarioDetalleDto = horarioDetalleBO.findHorarioDetalle(idHorario,-1,-1,filtroDia)[0];
                                                        }catch(Exception e){}
                                                    }
                                                    
                                                    
                                                %>
                                                <tr>                     
                                                    <input type="hidden" id="idHorarioDetalle_<%=dia%>" name="idHorarioDetalle_<%=dia%>" value="<%=horarioDetalleDto!=null?horarioDetalleDto.getIdDetalleHorario() : "" %>" />
                                                    <td>
                                                        <input maxlength="30" type="text" id="dia_<%=dia%>" name="dia_<%=dia%>" style="width:100px"
                                                               value="<%=dia%>" readonly=""/>
                                                    </td>
                                                    <td>
                                                        <input maxlength="30" type="text" id="entrada_<%=dia%>" name="entrada_<%=dia%>" style="width:100px"
                                                               class="time" value="<%=horarioDetalleDto!=null?horarioDetalleDto.getHoraEntrada()!=null?sdf.format(horarioDetalleDto.getHoraEntrada()):"":"" %>" placeholder="hh:mm" readonly />                                                        
                                                    </td>
                                                    <td>
                                                        <input maxlength="30" type="text" id="salida_<%=dia%>" name="salida_<%=dia%>" style="width:100px"
                                                               class="time" value="<%=horarioDetalleDto!=null?horarioDetalleDto.getHoraSalida()!=null?sdf.format(horarioDetalleDto.getHoraSalida()):"":"" %>" placeholder="hh:mm" readonly />
                                                    </td>
                                                    <td>
                                                        <input maxlength="30" type="text" id="tolerancia_<%=dia%>" name="tolerancia_<%=dia%>" style="width:100px"
                                                               value="<%=horarioDetalleDto!=null?horarioDetalleDto.getTolerancia():"" %>" onkeypress="return validateNumber(event);"/>
                                                    </td>
                                                    <td>
                                                        <input maxlength="30" type="text" id="salidaComida_<%=dia%>" name="salidaComida_<%=dia%>" style="width:100px"
                                                               class="time" value="<%=horarioDetalleDto!=null?horarioDetalleDto.getComidaSalida()!=null?sdf.format(horarioDetalleDto.getComidaSalida()):"":"" %>" placeholder="hh:mm" readonly />
                                                    </td>                                        
                                                    <td>
                                                        <input maxlength="30" type="text" id="entradaComida_<%=dia%>" name="entradaComida_<%=dia%>" style="width:100px"
                                                               class="time" value="<%=horarioDetalleDto!=null?horarioDetalleDto.getComidaEntrada()!=null?sdf.format(horarioDetalleDto.getComidaEntrada()):"":"" %>" placeholder="hh:mm" readonly />
                                                    </td>
                                                    <td>
                                                        <input maxlength="30" type="text" id="periodo_<%=dia%>" name="periodo_<%=dia%>" style="width:100px"
                                                               value="<%=horarioDetalleDto!=null?horarioDetalleDto.getPeriodoComida():"" %>" onkeypress="return validateNumber(event);"/>
                                                    </td>
                                                    <td>                                                        
                                                        <input type="checkbox" class="checkbox" <%=horarioDetalleDto!=null?(horarioDetalleDto.getDiaDescanso()==1?"checked":""):"" %> id="descanso_<%=dia%>" name="descanso_<%=dia%>" value="1">
                                                    </td>
                                                </tr>
                                                 <%}%>
                                            </tbody>
                                        </table>
                                    <br/>
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=horarioDto!=null?(horarioDto.getIdEstatus()==1?"checked":""):"checked" %> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
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