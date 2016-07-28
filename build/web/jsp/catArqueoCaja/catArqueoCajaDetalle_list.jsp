

<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.SGCobranzaMetodoPagoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensCobranzaMetodoPago"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoArqueo"%>
<%@page import="com.tsp.sct.bo.EmpleadoArqueoBO"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    /*int idConcepto = -1;
    try{ idConcepto = Integer.parseInt(request.getParameter("idConcepto")); }catch(NumberFormatException e){}*/
    
    int idEmpleado = 0; 
    try{
        idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
        
    }catch(NumberFormatException ex){}
    
    String buscar_fechamin = "";
    String buscar_fechamax = "";
    Date fechaMin=null;
    Date fechaMax=null;
    String strWhereRangoFechas="";
    String filtroBusqueda = "";
    String parametrosPaginacion = "";
    
    if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
       parametrosPaginacion+="idEmpleado="+idEmpleado+"";
    
    
    
    try{
            fechaMin = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("q_fh_min"));
            buscar_fechamin = DateManage.formatDateToSQL(fechaMin);
        }catch(Exception e){}
        try{
            fechaMax = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("q_fh_max"));
            buscar_fechamax = DateManage.formatDateToSQL(fechaMax);
        }catch(Exception e){}
    
    
    /*Filtro por rango de fechas*/
        if (fechaMin!=null && fechaMax!=null){
            strWhereRangoFechas="(CAST(FECHA AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax)+"&q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechas="(FECHA  >= '"+buscar_fechamin+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechas="(FECHA  <= '"+buscar_fechamax+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax);
        }
    
    
    
        if (!strWhereRangoFechas.trim().equals("")){
            filtroBusqueda += " AND " + strWhereRangoFechas;
        }
    
       
        
    int idEmpresa = user.getUser().getIdEmpresa();
    /*
    * Paginación
    */      
    int paginaActual = 1;
    double registrosPagina = 10;
    double limiteRegistros = 0;
    int paginasTotales = 0;
    int numeroPaginasAMostrar = 5;

    try{
        paginaActual = Integer.parseInt(request.getParameter("pagina"));
    }catch(Exception e){}

    try{
        registrosPagina = Integer.parseInt(request.getParameter("registros_pagina"));
    }catch(Exception e){}
    
     EmpleadoArqueoBO empleadoArqueoBO = new EmpleadoArqueoBO(user.getConn());
     EmpleadoArqueo[] empleadoArqueoDtos = new EmpleadoArqueo[0];
     EmpleadoArqueo[] empleadoArqueoDtosTotales = new EmpleadoArqueo[0];
     try{
         limiteRegistros = empleadoArqueoBO.findEmpleadoArqueo( -1 , idEmpresa, idEmpleado , 0, 0, filtroBusqueda).length;
         empleadoArqueoDtosTotales = empleadoArqueoBO.findEmpleadoArqueo( -1 , idEmpresa, idEmpleado , 0, 0, filtroBusqueda);
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        empleadoArqueoDtos = empleadoArqueoBO.findEmpleadoArqueo(-1 , idEmpresa ,idEmpleado,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     
     //Obtenemos empleado
     EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());     
     Empleado empleadoDto = empleadoBO.findEmpleadobyId(idEmpleado);
     
     
     /* Totales */
     double totalAbonos = 0;
     
      for (EmpleadoArqueo abonos:empleadoArqueoDtosTotales){
          
          totalAbonos += abonos.getMonto();
      }
     
     
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catArqueoCaja/catArqueoCaja_form.jsp"; 
    String paramName = "idEmpleado";
    //String parametrosPaginacion="";// "idEmpresa="+idEmpresa;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    
    
     NumberFormat formatMoneda = new DecimalFormat("$ ###,###,###,##0.00");
    
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
            
            function mostrarCalendario(){
                //fh_min
                //fh_max

                var dates = $('#q_fh_min, #q_fh_max').datepicker({
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
		});

            }
            
            
            function eliminarArqueo(idArqueo){
                if (idArqueo>0){
                    $.ajax({
                        type: "POST",
                        url: "catArqueoCaja_ajax.jsp",
                        data: {mode: '2', idArqueo : idArqueo},
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
                                            location.href = "catArqueoCajaDetalle_list.jsp?idEmpleado=<%=empleadoDto.getIdEmpleado()%>";
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
            
            
               
        </script>

    </head>
    <body>
        
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Ventas</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>
        
         
                    <div class="onecolumn">
                        
                        <div class="header">
                            <span>
                                Busqueda Avanzada &dArr;
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content" style="display: none;">
                            <form action="catArqueoCajaDetalle_list.jsp" id="search_form_advance" name="search_form_advance" method="post"> 
                                <input type="hidden" id="idEmpleado" name="idEmpleado" value="<%=idEmpleado %>"/>
                                <p>
                                    Por Fecha &raquo;&nbsp;&nbsp;
                                    <label>Desde:</label>
                                    <input maxlength="15" type="text" id="q_fh_min" name="q_fh_min" style="width:100px"
                                            value="" readonly/>
                                    &nbsp; &laquo; &mdash; &raquo; &nbsp;
                                    <label>Hasta:</label>
                                    <input maxlength="15" type="text" id="q_fh_max" name="q_fh_max" style="width:100px"
                                        value="" readonly/>
                                </p>
                                <br/>                                  
                                <div id="action_buttons">
                                    <p>
                                        <input type="button" id="buscar" value="Buscar" onclick="$('#search_form_advance').submit();"/>
                                    </p>
                                </div>
                                
                            </form>
                        </div>
                   </div>
                                
                       <div class="onecolumn"> 
                        
                        <div class="header">
                            <span>
                                <img src="../../images/icon_impuesto.png" alt="icon"/>
                                Arqueo de Caja &nbsp;&nbsp;&nbsp;&nbsp;<%=empleadoDto.getNombre() +" "+ empleadoDto.getApellidoPaterno() +" "+ empleadoDto.getApellidoMaterno() %>
                            </span>
                            <div class="switch" style="width:300px">
                                <table width="300px" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>                                                            
                                                            <td>
                                                                <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Agregar Abono" 
                                                                       style="float: right; width: 110px;" onclick="location.href='<%=urlTo%>?idEmpleado=<%=idEmpleado%>'"/>
                                                            </td>
                                                        </tr>
                                                </tbody>
                                </table>
                            </div>
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <form id="form_data" name="form_data" action="" method="post">
                                <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Monto</th>                                            
                                            <th>Método de Pago</th> 
                                            <th>Referencia</th>
                                            <th>Fecha</th>
                                            <th>Estatus</th>                                            
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                        
                                            for (EmpleadoArqueo item:empleadoArqueoDtos){
                                                try{
                                                    
                                                SgfensCobranzaMetodoPago metodoPagoDto = new SGCobranzaMetodoPagoBO(item.getIdCobranzaMetodoPago(),user.getConn()).getCobranzaMetodoPago();
                                                    
                                        %>
                                        <tr <%=(item.getIdEstatus()==2)?"class='inactive'":""%>>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdArqueo() %></td>                                            
                                            <td><%=item.getMonto() %></td>
                                            <td><%=metodoPagoDto.getNombreMetodoPago()%></td>   
                                            <td><%=item.getReferencia()!=null?!item.getReferencia().equals("null")?item.getReferencia():"":"" %></td>
                                            <td><%=item.getFecha()%></td>     
                                            <td><%=item.getIdEstatus()==1?"Activo":item.getIdEstatus()==2?"Cancelado":"" %></td>
                                            <td>                                                
                                               <a href="<%=urlTo%>?idEmpleado=<%=item.getIdEmpleado()%>&idArqueo=<%=item.getIdArqueo()%>&acc=1&pagina=<%=paginaActual%>"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>
                                                &nbsp;&nbsp;
                                                <a href="#" onclick="eliminarArqueo(<%=item.getIdArqueo()%>);"><img src="../../images/icon_delete.png" alt="delete" class="help" title="Borrar"/></a>           
                                            </td>
                                        </tr>
                                        <%      }catch(Exception ex){
                                                    ex.printStackTrace();
                                                }
                                            } 
                                        %>
                                        
                                        <tr style="font-size: 14;">
                                            <td colspan="5" style="text-align: right;"><b>TOTAL:</b></td>
                                            <td style="color: #0000cc; text-align: left;"><%=formatMoneda.format(totalAbonos)%> </td>
                                            <td>&nbsp;</td>
                                        </tr>
                                        
                                    </tbody>
                                </table>
                            </form>
                                    
                            <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                           <!-- <//jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <//jsp:param name="idReport" value="<%//= ReportBO.VENTAS_ARQUEOCAJA_REPORT %>" />
                                <//jsp:param name="parametrosCustom" value="<%//= filtroBusquedaEncoded %>" />
                            <///jsp:include> -->
                            <!-- FIN INCLUDE OPCIONES DE EXPORTACIÓN-->
                            
                            </br>
                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                            
                            <jsp:include page="../include/listPagination.jsp">
                                <jsp:param name="paginaActual" value="<%=paginaActual%>" />
                                <jsp:param name="numeroPaginasAMostrar" value="<%=numeroPaginasAMostrar%>" />
                                <jsp:param name="paginasTotales" value="<%=paginasTotales%>" />
                                <jsp:param name="url" value="<%=request.getRequestURI() %>" />
                                <jsp:param name="parametrosAdicionales" value="<%=parametrosPaginacion%>" />
                            </jsp:include>
                            
                        </div>
                    </div>
                 <!-- Fin de Contenido-->
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