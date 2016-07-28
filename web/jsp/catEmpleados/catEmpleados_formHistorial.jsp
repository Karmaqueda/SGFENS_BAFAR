<%-- 
    Document   : catEmpleados_form.jsp
    Created on : 08-01-2013, 12:13:49
    Author     : Leonardo 
--%>

<%@page import="java.util.Date"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="com.tsp.sct.bo.EmpleadoBitacoraPosicionBO"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoBitacoraPosicion"%>
<%@page import="com.tsp.sct.bo.EstadoBO"%>
<%@page import="com.tsp.sct.bo.DispositivoMovilBO"%>
<%@page import="com.tsp.sct.dao.dto.DispositivoMovil"%>
<%@page import="com.tsp.sct.bo.EmpleadoRolBO"%>
<!-----------------<//%@page import="com.tsp.microfinancieras.jdbc.SgfensProspectoDaoImpl"%>-->
<!-----------------<//%@page import="com.tsp.microfinancieras.dto.SgfensProspecto"%>-->
<!-----------------<//%@page import="com.tsp.microfinancieras.bo.SGProspectoBO"%>-->
<!-----------------<//%@page import="com.tsp.microfinancieras.dto.SgfensEmpleadoVendedor"%>-->
<!-----------------<//%@page import="com.tsp.microfinancieras.bo.SGEmpleadoVendedorBO"%>-->
<!-----------------<//%@page import="com.tsp.microfinancieras.bo.RolesBO"%>-->
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.bo.PasswordBO"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el Empleado tiene acceso a este topico
    if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
        response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
        response.flushBuffer();
    } else {

        int idEmpresa = user.getUser().getIdEmpresa();
        
        String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
        String FechaInicioHisto = request.getParameter("fechaInicioHistoria")!=null? new String(request.getParameter("fechaInicioHistoria").getBytes("ISO-8859-1"),"UTF-8") :"";
        String FechaFinHisto = request.getParameter("fechaFinHistoria")!=null? new String(request.getParameter("fechaFinHistoria").getBytes("ISO-8859-1"),"UTF-8") :"";
        
         String cadenaParametrosPaginacion = "";
         
        String dia = "";
        String mes = "";
        String ano = "";
        String FechaInicioHisto2 = "";
        String FechaFinHisto2 = "";
        StringTokenizer tokens = null;
        try{
            if(!FechaInicioHisto.equals("")){
                        tokens = new StringTokenizer(FechaInicioHisto,"/");
                                dia = tokens.nextToken().intern();
                                mes = tokens.nextToken().intern();
                                ano = tokens.nextToken().intern();
                                FechaInicioHisto2 = ano+"-"+mes+"-"+dia;
            }
        }catch(Exception e){
            FechaInicioHisto = "";
        }
        try{
            if(!FechaFinHisto.equals("")){
                tokens = new StringTokenizer(FechaFinHisto,"/");
                                dia = tokens.nextToken().intern();
                                mes = tokens.nextToken().intern();
                                ano = tokens.nextToken().intern();
                                FechaFinHisto2 = ano+"-"+mes+"-"+dia;
            }
        }catch(Exception e){
            FechaFinHisto = "";
        }    
        
        System.out.println("------");
        System.out.println("------");
        System.out.println("FECHAS INICIO: "+FechaInicioHisto);
        System.out.println("FECHAS FIN "+FechaFinHisto);
        System.out.println("------");
        System.out.println("------");
        
        String filtroBusqueda = "";

        if (!FechaInicioHisto.trim().equals("")&&!FechaFinHisto.trim().equals("")){
            filtroBusqueda += " AND (FECHA BETWEEN '"+ FechaInicioHisto2 + "' AND '" + FechaFinHisto2 + " 23:59:59.99')";
            if(!cadenaParametrosPaginacion.equals(""))
                cadenaParametrosPaginacion+="&";
            cadenaParametrosPaginacion+="fechaInicioHistoria="+FechaInicioHisto+"&fechaFinHistoria="+FechaFinHisto;
        }else if (!FechaInicioHisto.trim().equals("")&&FechaFinHisto.trim().equals("")){
            filtroBusqueda += " AND FECHA > '"+ FechaInicioHisto2 + "'";
            if(!cadenaParametrosPaginacion.equals(""))
                cadenaParametrosPaginacion+="&";
            cadenaParametrosPaginacion+="fechaInicioHistoria="+FechaInicioHisto;
        }else if (FechaInicioHisto.trim().equals("")&&!FechaFinHisto.trim().equals("")){
            filtroBusqueda += " AND FECHA < '"+ FechaFinHisto2 + " 23:59:59.99'";
            if(!cadenaParametrosPaginacion.equals(""))
                cadenaParametrosPaginacion+="&";
            cadenaParametrosPaginacion+="fechaFinHistoria="+FechaFinHisto;
        }else{
            filtroBusqueda = filtroBusqueda + " ORDER BY FECHA DESC , ID_BITACORA_POSICION DESC";
        }
               
        String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
        
        /*
         * Parámetros
         */
        int idEmpleado = 0;
        try {
            idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
            if(!cadenaParametrosPaginacion.equals(""))
                cadenaParametrosPaginacion+="&";
            cadenaParametrosPaginacion+="idEmpleado="+idEmpleado;
        } catch (NumberFormatException e) {
        }
        
        int idProspecto=-1;
        try {
            idProspecto = Integer.parseInt(request.getParameter("idProspecto"));
            if(!cadenaParametrosPaginacion.equals(""))
                cadenaParametrosPaginacion+="&";
            cadenaParametrosPaginacion+="idProspecto="+idProspecto;
        } catch (NumberFormatException e) {
        }
        

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         *   3 = nuevo (modalidad PopUp [cotizaciones, pedidos, facturas]) 
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        String newRandomPass = "";
        
        EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
        Empleado empleadosDto = null;
        if (idEmpleado > 0){
            empleadoBO = new EmpleadoBO(idEmpleado,user.getConn());
            empleadosDto = empleadoBO.getEmpleado();
        }else{
            newRandomPass = new PasswordBO().generateValidPassword();
        }
        
        
     String parametrosPaginacion=cadenaParametrosPaginacion;
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
        
        System.out.println("pag actual:" + paginaActual +"-----------"+ "registrosPagina" + registrosPagina );
        EmpleadoBitacoraPosicion[] empleadosHostorialDto = new EmpleadoBitacoraPosicion[0];
        EmpleadoBitacoraPosicionBO bO = new EmpleadoBitacoraPosicionBO(user.getConn());
        try{
            
            limiteRegistros = bO.findEmpleadoBitacoraPosicions( 0, idEmpleado , 0, 0, filtroBusqueda).length;         
            System.out.println("limit reg ..................................." + limiteRegistros);
            if (!buscar.trim().equals(""))
                registrosPagina = limiteRegistros;
            paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);
           if(paginaActual<0)
               paginaActual = 1;
           else if(paginaActual>paginasTotales)
                paginaActual = paginasTotales;   
           
           System.out.println("query ...................................");
            empleadosHostorialDto = bO.findEmpleadoBitacoraPosicions(0, idEmpleado, ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina, filtroBusqueda);
        }catch(Exception ex){
         ex.printStackTrace();
        }
        
      /*  SGEmpleadoVendedorBO empleadoVendedorBO = null;
        SgfensEmpleadoVendedor empleadoVendedorDto = null;
        if (empleadosDto!=null){
            empleadoVendedorBO = new SGEmpleadoVendedorBO(idEmpleado);
            empleadoVendedorDto = empleadoVendedorBO.getEmpleadoVendedor();
        }
        
        SgfensProspecto prospectoDto = null;
        if (idProspecto>0){
            prospectoDto = new SGProspectoBO(idProspecto).getSgfensProspecto();
            try{
                prospectoDto.setIdEstatus(2); //Deshabilitado
                new SgfensProspectoDaoImpl(user.getConn()).update(prospectoDto.createPk(),prospectoDto);
            }catch(Exception ex){ ex.printStackTrace();}
        }*/
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <link rel="shortcut icon" href="../../images/favicon.ico">
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
                                                location.href = "catEmpleados_list.jsp";
                                            <%}else{%>
                                                parent.recargarSelectEmpleados();
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
            
            function apareceAutomovil(){   
                var seleccionadoRol = document.getElementById("idRolEmpleado").value;
                if(seleccionadoRol==2){                    
                    document.getElementById("automovilDisplay").style.display="block";
                }else{                    
                    document.getElementById("automovilDisplay").style.display="catalogodnone";                    
                }
            }
        </script>
            
        <script type="text/javascript">
            
            function mostrarCalendarioHistorial(){
                $( "#fechaInicioHistoria" ).datepicker({                       
                        gotoCurrent: true,
                        changeMonth: true,
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 100);
                            }, 500)}
                });
                
                $( "#fechaFinHistoria" ).datepicker({                        
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
                    <h1>Historial de Ubicaciones</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    
                                    <%if(empleadosDto.getIdEstado()==1||empleadosDto.getIdEstado()==4||empleadosDto.getIdEstado()==5){ 
                                     long s2 = (new Date()).getTime();
                                        long d2 = 0; 
                                        try{
                                            d2 = empleadosDto.getFechaHora().getTime();
                                        }catch(Exception e){}
                                        long diferencia = s2 - d2;                                
                                        if(diferencia < 300000){%>
                                        <img src="../../images/estatusEmpleado/icon_activoTrabajando.png" alt="icon"/>
                                        <%}else{%>
                                            <img src="../../images/estatusEmpleado/icon_desactivado.png"  alt="icon"/>
                                            <%}%>  
                                    <%}else if(empleadosDto.getIdEstado()==2||empleadosDto.getIdEstado()==8){
                                    %>
                                    <img src="../../images/estatusEmpleado/icon_descansoInactividad.png" alt="icon"/>
                                    <%}else if(empleadosDto.getIdEstado()==3||empleadosDto.getIdEstado()==6){ 
                                    %>
                                    <img src="../../images/estatusEmpleado/icon_servidorFallido.png" alt="icon"/>                                                                        
                                    <%}else if(empleadosDto.getIdEstado()==7){
                                    %>
                                    <img src="../../images/estatusEmpleado/icon_descansoInactividad.png" alt="icon"/>                                    
                                    <%}else if(empleadosDto.getIdEstado()==9){
                                        long s = (new Date()).getTime();
                                        long d = 0; 
                                        try{
                                            d = empleadosDto.getFechaHora().getTime();
                                        }catch(Exception e){}
                                        long diferencia = s - d;                                
                                        if(diferencia < 300000){
                                        %>
                                            <img src="../../images/estatusEmpleado/icon_activoTrabajando.png" alt="icon"/>
                                        <%}else{%>
                                            <img src="../../images/estatusEmpleado/icon_desactivado.png"  alt="icon"/>
                                            <%}%>
                                    <%}else{
                                    %>                                    
                                    <img src="../../images/estatusEmpleado/icon_users.png" alt="icon"/>
                                    <%}%>
                                    
                                    <% if(empleadosDto!=null){%>
                                    Empleado con Número: <%=empleadosDto!=null?empleadosDto.getNumEmpleado():"" %>
                                    <%}else{%>
                                    Historial Empleado 
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            
                            
                            
                                                        
                                                       
                            <div class="switch" style="width:410px">
                                <table width="100%" cellpadding="0" cellspacing="0" style="position: relative">
						<tbody>
							<tr>
                                                            <td>
                                                                <div id="search">
                                                                <form action="catEmpleados_formHistorial.jsp" id="search_form" name="search_form" method="get">
                                                                    
                                                                        <label>Filtro por Fecha:</label><br/>
                                                                        <input type="text" title="Fecha Inicio" class="" style="width: 100px; float: left; "
                                                                               value="<%=FechaInicioHisto%>" id="fechaInicioHistoria" name="fechaInicioHistoria"/>
                                                                        <input type="text" title="Fecha Fin" class="" style="width: 100px; float: left; "
                                                                               value="<%=FechaFinHisto%>" id="fechaFinHistoria" name="fechaFinHistoria"/>
                                                                    
                                                                        <input type="submit"  id="eneviar" name="eneviar" class="right_switch" value="Buscar" 
                                                                       style="float: right; width: 100px;"/>
                                                                </form>
                                                                </div>
                                                            </td>                                                            
                                                        </tr>
                                                </tbody>
                                </table>                            
                            </div>
                        <br class="clear"/>
                            <div class="content">
                            <form id="form_data" name="form_data" action="" method="post">
                                <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th>ID</th>                                                                                        
                                            <th>Latitud</th>
                                            <th>Longitud</th>
                                            <th>Fecha Hora (YYYY-MM-DD HH:MM:SS)</th>                                            
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            for (EmpleadoBitacoraPosicion item:empleadosHostorialDto){
                                                try{
                                        %>  
                                            <td><%=item.getIdBitacoraPosicion() %></td>                                            
                                            <td><%=item.getLatitud() %></td>
                                            <td><%=item.getLongitud() %></td>
                                            <td><%=item.getFecha() %></td>                                            
                                            <td>
                                                <!--<a href="<//%=urlTo%>?<//%=paramName%>=<%=item.getIdEmpleado()%>&acc=1"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>-->
                                                &nbsp;&nbsp;
                                                <!--<a href="#" onclick="eliminar('<%=item.getIdEmpleado()%>');"><img src="../../images/icon_delete.png" alt="eliminar" class="help" title="Eliminar"/></a>-->
                                                <a href="#" onclick="ubicaMapaHistorico(<%=item.getLatitud()%>, <%=item.getLongitud()%>);"><img src="../../images/icon_movimiento.png" alt="ubicarEnMapa" class="help" title="Ubicar en Mapa"/></a>                                                
                                                &nbsp;&nbsp;                                                
                                            </td>
                                        </tr>
                                        <%      }catch(Exception ex){
                                                    ex.printStackTrace();
                                                }
                                            } 
                                        %>
                                    </tbody>
                                </table>
                            </form>
                                    
                            <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <!--<//jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <//jsp:param name="idReport" value="<//%= ReportBO.CLIENTE_REPORT %>" />
                                <//jsp:param name="parametrosCustom" value="<%=filtroBusquedaEncoded %>" />
                            <///jsp:include>-->
                            <!-- FIN INCLUDE OPCIONES DE EXPORTACIÓN-->

                            <jsp:include page="../include/listPagination.jsp">
                                <jsp:param name="paginaActual" value="<%=paginaActual%>" />
                                <jsp:param name="numeroPaginasAMostrar" value="<%=numeroPaginasAMostrar%>" />
                                <jsp:param name="paginasTotales" value="<%=paginasTotales%>" />
                                <jsp:param name="url" value="<%=request.getRequestURI() %>" />
                                <jsp:param name="parametrosAdicionales" value="<%=parametrosPaginacion%>" />
                            </jsp:include>
                            
                        </div>                              
                        </div>
                        <!-- End left column window -->
                        <!-- contenido de columna derecha Mapa-->
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_movimiento.png" alt="icon"/>                                    
                                    Mapa                                     
                                </span>
                            </div>
                            <br class="clear"/>
                            
                            <div class="content">                                                 
                                <jsp:include page="../include/Mapa_EmpleadoLocalizacion.jsp"/>     
                            </div>
                        </div>
                        <!-- fin de contenido de columna derecha Mapa-->
                    </div>
                    </form>
                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <div id="action_buttons">
                            <p>                                
                                <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                            </p>
                    </div>

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>

        <script>
            mostrarCalendarioHistorial();
        </script>
    </body>
</html>
<%}%>