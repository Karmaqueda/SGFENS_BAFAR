<%-- 
    Document   : catEmpleados_list
    Created on : 9/01/2013, 11:12:43 AM
    Author     : Leonardo Montes de Oca, leonarzeta@hotmail.com
--%>

<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoBitacoraPosicion"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoBitacoraPosicionDaoImpl"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sct.dao.dto.Ruta"%>
<%@page import="com.tsp.sct.dao.jdbc.RutaDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Geocerca"%>
<%@page import="com.tsp.sct.bo.GeocercaBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    
    String buscar_isMostrarSoloActivos = request.getParameter("inactivos")!=null?request.getParameter("inactivos"):"1";
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";    
    
    String filtroBusqueda = ""; //"AND ID_ESTATUS=1 ";
    if (!buscar.trim().equals(""))
        filtroBusqueda += " AND (NOMBRE LIKE '%" + buscar + 
                            "%' OR APELLIDO_PATERNO LIKE '%" + buscar +
                            "%' OR APELLIDO_MATERNO LIKE '%"+buscar+
                            "%' OR NUM_EMPLEADO LIKE '%"+buscar+                                                       
                            "%' OR (ID_MOVIL_EMPLEADO_ROL IN (SELECT ID_ROLES FROM ROLES WHERE ROLES.NOMBRE LIKE '%"+buscar+
                            "%')) OR (ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE EMPRESA.NOMBRE_COMERCIAL LIKE '%"+buscar+
                            "%')) OR (ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE EMPRESA.RAZON_SOCIAL LIKE '%"+buscar+
                            "%'))) OR (ID_ESTATUS IN (SELECT ID_ESTATUS FROM ESTATUS WHERE NOMBRE LIKE '"+buscar+"')) ";
                            
    if (buscar_isMostrarSoloActivos.trim().equals("1")){
        
        filtroBusqueda += " AND (ID_ESTATUS = 1) ";
    }
    
    //Si es usuario con rol vendedor entonces aplicamos filtro el listado de Empleados
    //if (user.getUser().getIdRoles() == RolesBO.ROL_OPERADOR){
    //    filtroBusqueda += " AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM SGFENS_CLIENTE_VENDEDOR WHERE ID_USUARIO_VENDEDOR="+user.getUser().getIdUsuarios()+")";
    //}
    
    int idEmpleado = -1;
    try{ idEmpleado = Integer.parseInt(request.getParameter("idEmpleado")); }catch(NumberFormatException e){}
    
    long idEmpresa = user.getUser().getIdEmpresa();
    
    //**PARA FILTRAR LOS EMPLEADOS QUE PERTENECEN A CIERTA ZONA
    int idRegion = -1;
    try{ idRegion = Integer.parseInt(request.getParameter("idRegion"));
        if(idRegion > 0){
            filtroBusqueda += " AND ID_REGION = "+idRegion + " ";
        }
    }catch(NumberFormatException e){}
    //**PARA FILTRAR LOS EMPLEADOS QUE PERTENECEN A CIERTA GEOCERCA
    int idGeocerca = -1;
    try{ idGeocerca = Integer.parseInt(request.getParameter("idGeocerca"));
        if(idRegion > 0){
            filtroBusqueda += " AND ID_GEOCERCA = "+idGeocerca + " ";
        }
    }catch(NumberFormatException e){}
    
    
    /*
    * Paginación
    */      
    int paginaActual = 1;
    double registrosPagina = new ParametrosBO(user.getConn()).getParametroDouble("app.preto.paginacion.registrosPorPagina"); //10;
    double limiteRegistros = 0;
    int paginasTotales = 0;
    int numeroPaginasAMostrar = 5;

    try{
        paginaActual = Integer.parseInt(request.getParameter("pagina"));
    }catch(Exception e){}

    try{
        registrosPagina = Integer.parseInt(request.getParameter("registros_pagina"));
    }catch(Exception e){}
    
     EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
     Empleado[] empleadosDto = new Empleado[0];
     try{
         limiteRegistros = empleadoBO.findEmpleados(idEmpleado, idEmpresa , 0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        empleadosDto = empleadoBO.findEmpleados(idEmpleado, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catEmpleados/catEmpleados_form.jsp";
    String urlTo2 = "../catEmpleados/catEmpleados_formEstado.jsp";
    String urlTo3 = "../catEmpleados/catEmpleados_formMapa.jsp";
    String urlTo4 = "../catEmpleados/catEmpleados_RutaDia.jsp";//../catEmpleados/catEmpleados_formHistorial.jsp
    String urlTo5 = "../catEmpleados/catEmpleados_Mensajes_list.jsp"; 
    String urlTo6 = "../catGeocerca/mapaFiguras.jsp";  
    String urlTo8 = "../mapa/cobranzaPromotor_ComparaRutas.jsp";
    String urlToAgenda = "../catEmpleados/catEmpleado_Agenda_list.jsp";
    String urlToCuentaEfe = "../catEmpleados/catEmpleado_CuentaEfectivo_list.jsp";

    String paramName = "idEmpleado";
    String paramName2 = "idGeocerca";
    String parametrosPaginacion="inactivos="+buscar_isMostrarSoloActivos+"&idRegion="+idRegion+"&idGeocerca="+idGeocerca;// "idEmpresa="+idEmpresa;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    
    
    EmpresaBO empresaBO = new EmpresaBO(user.getConn());
    EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa());

    String verificadorSesionGuiaCerrada = "0";
    String cssDatosObligatorios = "border:3px solid red;";//variable para colocar el css del recuadro que encierra al input para la guia interactiva
    try{
        if(session.getAttribute("sesionCerrada")!= null){
            verificadorSesionGuiaCerrada = (String)session.getAttribute("sesionCerrada");
        }
    }catch(Exception e){}
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
            function eliminarEmpleado(idEmpleado){
                if (idEmpleado>0){
                    $.ajax({
                        type: "POST",
                        url: "catEmpleados_ajax.jsp",
                        data: {mode: '2', idEmpleado : idEmpleado},
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
                                            location.href = "catEmpleados_list.jsp";
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
            
            
            
            function eliminar(idEmpleado){              
                apprise('¿Estas seguro de eliminar (cambiar de estatus) el registro del empleado?', {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'Cancelar'}, function(r)
                {
                    if(r){
                        ajaxReestablecer(idEmpleado);
                    }
                });
            }
            
            
            function inactiv(){               
                if($("#inactivos").attr("checked")){
                    $("#inactivos").val("2");
                }else{
                     $("#inactivos").val("1");
                }
            }
            
            function eliminarRegion(idEmpleado, idRegion){
                if (idEmpleado>0){
                    $.ajax({
                        type: "POST",
                        url: "catEmpleados_ajax.jsp",
                        data: {mode: 'quitarZona', idEmpleado : idEmpleado},
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
                                            location.href = "catEmpleados_list.jsp?idRegion="+idRegion;
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
            
            function eliminarGeocerca(idEmpleado, idGeocerca){
                if (idEmpleado>0){
                    $.ajax({
                        type: "POST",
                        url: "catEmpleados_ajax.jsp",
                        data: {mode: 'quitarGeocerca', idEmpleado : idEmpleado},
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
                                            location.href = "catEmpleados_list.jsp?idGeocerca="+idGeocerca;
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
                    <h1>Administración</h1>
                    
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
                            <form action="catEmpleados_list.jsp" id="search_form_advance" name="search_form_advance" method="post">
                                                                    
                                <p>
                                    <input type="checkbox" class="checkbox" id="inactivos" name="inactivos" value="1"  onchange="inactiv();" > <label for="inactivos">Mostrar Inactivos</label>                                   
                                </p>
                                <br/><br/>  
                            
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
                                <img src="../../images/icon_users.png" alt="icon"/>
                                Administración Empleados
                            </span>
                            <div class="switch" style="width:750px">
                                <table width="750px" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
                                                            <td>
                                                                <div id="search">
                                                                    <form action="catEmpleados_list.jsp?idRegion=<%=idRegion%>" id="search_form" name="search_form" method="get"> 
                                                                                                                                    
                                                                        
                                                                        <input type="text" id="q" name="q" title="Buscar por # Empleado/Nombre/Apellido Paterno/Materno/Rol/Sucursal/Estatus" class="" style="width: 300px; float: left; "
                                                                               value="<%=buscar%>"/>                                                                        
                                                                        <!--<li> <a title="Buscar" type="submit" id="search" class="right_switch" onclick="search_form.submit(); q.value=''">Buscar </a> </li>-->
                                                                        <!--<input title="Buscar" type="submit" id="search" class="right_switch" style="font-size: 9px;"/>-->
                                                                        <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="width: 30px; height: 25px; float: left"/>
                                                                </form>
                                                                </div>
                                                            </td>
                                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                                            <%if(RolAutorizacionBO.permisoNuevoElemento(user.getUser().getIdRoles())){%>
                                                            <td>
                                                                <input type="button" id="nuevo" name="nuevo" class="right_switch expose" value="Crear Nuevo" 
                                                                       style="float: right; width: 100px;" onclick="location.href='<%=urlTo%>'"/>
                                                            </td>
                                                            <%}%>
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
                                            <th>Número de Empleado</th>
                                            <th>Nombre</th>
                                            <th>Apellido Paterno</th>
                                            <th>Apellido Materno</th>
                                            <th>Rol</th>
                                            <th>Estado</th>
                                            <th>Repartidor?</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            for (Empleado item:empleadosDto){
                                                try{
                                        %>
                                        <tr <%=(item.getIdEstatus()==2)?"style='background: #B0B1B1'":""%> >
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdEmpleado() %></td>
                                            <td><%=item.getNumEmpleado()%></td>
                                            <td><%=item.getNombre() %></td>
                                            <td><%=item.getApellidoPaterno() %></td>
                                            <td><%=item.getApellidoMaterno() %></td>
                                            <!--<td><//%=item.getContacto()!=null?item.getContacto():""%></td>-->
                                            <td><%=(item.getIdMovilEmpleadoRol()==2)?"ADMINISTRADOR":(item.getIdMovilEmpleadoRol()==3)?"AUDITOR":(item.getIdMovilEmpleadoRol()==4)?"OPERADOR":(item.getIdMovilEmpleadoRol()==25)?"VENDEDOR MOVIL":
                                                    (item.getIdMovilEmpleadoRol()==16)?"FACTURADOR":(item.getIdMovilEmpleadoRol()==17)?"PROMOTOR":(item.getIdMovilEmpleadoRol()==18)?"CONDUCTOR":(item.getIdMovilEmpleadoRol()==19)?"VENTAS":
                                                    (item.getIdMovilEmpleadoRol()==20)?"FACTURISTA":(item.getIdMovilEmpleadoRol()==21)?"GESTOR":(item.getIdMovilEmpleadoRol()==22)?"GERENTE":(item.getIdMovilEmpleadoRol()==23)?"CAJERO":
                                                    (item.getIdMovilEmpleadoRol()==26)?"CONDUCTOR":(item.getIdMovilEmpleadoRol()==27)?"ADMINISTRADOR DE SUCURSAL":(item.getIdMovilEmpleadoRol()==28)?"DIRECCION COMERCIAL":(item.getIdMovilEmpleadoRol()==29)?"GESTOR":
                                                    (item.getIdMovilEmpleadoRol()==30)?"ALMACENISTA":(item.getIdMovilEmpleadoRol()==31)?"CONDUCTOR VENDEDOR":(item.getIdMovilEmpleadoRol()==32)?"COBRADOR":"OTRO ROL"%></td>
                                            
                                            <%
                                            
                                                //if(item.getIdEstado()==9){
                                                long s = (new Date()).getTime();
                                                long d = 0; 
                                                try{
                                                    EmpleadoBitacoraPosicionDaoImpl bitacoraDao = new EmpleadoBitacoraPosicionDaoImpl();
                                                    String filtro = " ID_EMPLEADO = "+ item.getIdEmpleado() + " ORDER BY ID_BITACORA_POSICION DESC LIMIT 0,1";
                                                    EmpleadoBitacoraPosicion bitEmp = bitacoraDao.findByDynamicWhere(filtro,null)[0];

                                                    d = bitEmp.getFecha().getTime();
                                                    //d = empleado.getFechaHora().getTime();
                                                }catch(Exception e){
                                                d = 0;
                                                }
                                                long diferencia = s - d;                                
                                                if(diferencia < 300000){ //5 minutos
                                                %>
                                                    <td><%="TRABAJANDO"%></td>
                                                <%}else{%>                                                    
                                                    <td><%="INACTIVO"%></td>
                                                <%}%>
                                            
                                            <!--<td></%=(item.getIdEstado()==1)?"TRABAJANDO":(item.getIdEstado()==2)?"INACTIVO":(item.getIdEstado()==3)?"DESCONECTADO":(item.getIdEstado()==4)?"TRABAJANDO":(item.getIdEstado()==5)?"TRABAJANDO":(item.getIdEstado()==6)?"DESCONECTADO":(item.getIdEstado()==7)?"EN DESCANSO":(item.getIdEstado()==8)?"INACTIVO":(item.getIdEstado()==9)?"CREADO RECIENTEMENTE":"OTRO ESTATUS"  %></td>-->
                                            <td><%=(item.getRepartidor() == 1)?"REPARTIDOR":"" %></td>
                                            <td>     
                                                
                                                <%if(RolAutorizacionBO.permisoAccionesElemento(user.getUser().getIdRoles())){%>
                                                
                                                    <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdEmpleado()%>&acc=1&pagina=<%=paginaActual%>"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>
                                                    &nbsp;&nbsp;
                                                    <!--<a href="#" onclick="eliminar('<%=item.getIdEmpleado()%>');"><img src="../../images/icon_delete.png" alt="eliminar" class="help" title="Eliminar"/></a>-->
                                                    <a href="#" onclick="eliminarEmpleado(<%=item.getIdEmpleado()%>);"><img src="../../images/icon_delete.png" alt="delete" class="help" title="Borrar"/></a>
                                                    <!--&nbsp;&nbsp;
                                                    <a href="<%=urlTo2%>?<%=paramName%>=<%=item.getIdEmpleado()%>&acc=Estado"><img src="../../images/icon_almacen.png" alt="cambiarEstado" class="help" title="Cambiar Estado"/></a>-->
                                                    &nbsp;&nbsp;
                                                    <a href="<%=urlTo3%>?<%=paramName%>=<%=item.getIdEmpleado()%>&acc=Mapa" id="consultaGeoLocalizacionEmpleado" <%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?"class='expose'":""%>><img src="../../images/icon_movimiento.png" alt="localización" class="help" title="Localización"/></a>
                                                    &nbsp;&nbsp;
                                                    <a href="<%=urlTo4%>?<%=paramName%>=<%=item.getIdEmpleado()%>&acc=Historial&menu=1"><img src="../../images/icon_calendar.png" alt="historial" class="help" title="Historial"/></a>                                                
                                                    &nbsp;&nbsp;
                                                    <a href="<%=urlTo5%>?<%=paramName%>=<%=item.getIdEmpleado()%>&acc=Mensaje"><img src="../../images/icon_mensajes.png" alt="mensaje" class="help" title="Mensaje"/></a>
                                                    &nbsp;&nbsp;
                                                    <%
                                                    if(item.getIdGeocerca()>0){
                                                        GeocercaBO geocercaBO = new GeocercaBO(item.getIdGeocerca(),user.getConn());
                                                        Geocerca geocerca = geocercaBO.getGeocerca();
                                                    %>
                                                    <a href="<%=urlTo6%>?acc=consulta&<%=paramName2%>=<%=item.getIdGeocerca()%>&forma=<%=geocerca.getTipoGeocerca()%>&<%=paramName%>=<%=item.getIdEmpleado()%>" id="consultaForma" title="<%=item.getHoraInicio()!=null&&item.getHoraFin()!=null?("De " + item.getHoraInicio() + " a " + item.getHoraFin() + " Hrs."):"Consultar Geocerca"%>" class="modalbox_iframe">
                                                        <img src="../../images/icon_mapa_1.png" alt="consulta geocerca" class="help" title="Consultar Geocerca"/>
                                                    </a>
                                                    &nbsp;&nbsp;
                                                    <a href="#" onclick="eliminarGeocerca(<%=item.getIdEmpleado()%>,<%=item.getIdGeocerca()%>);"><img src="../../images/icon_mapa_1_delete.png" alt="delete" class="help" title="Quitar Geocerca"/></a>
                                                    &nbsp;&nbsp;
                                                    <%} %>  
                                                    
                                                    <%
                                                      
                                                       //Revisamos si el pomotor tiene asignada al menos una ruta 
                                                        RutaDaoImpl rutasDaoImpl = new RutaDaoImpl(user.getConn());
                                                        Ruta[] rutas = rutasDaoImpl.findWhereIdEmpleadoEquals(item.getIdEmpleado());

                                                    if(rutas.length>=1){
                                                    %>
                                                        &nbsp;&nbsp;
                                                        <a href="<%=urlTo8%>?<%=paramName%>=<%=item.getIdEmpleado()%>&acc=CompararRutas"><img src="../../images/icon_logistica_antes.png" alt="CompararRutas" class="help" title="Comparar Rutas"/></a>
                                                        &nbsp;&nbsp;
                                                    <% } %>
                                                <%}%>
                                                
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
                                <//jsp:param name="parametrosCustom" value="<%= filtroBusquedaEncoded %>" />
                            <//jsp:include>-->
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

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>


    </body>
</html>
<%}%>
