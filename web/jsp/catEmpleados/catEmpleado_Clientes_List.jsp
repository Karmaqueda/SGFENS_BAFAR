<%-- 
    Document   : catEmpleado_Clientes_List
    Created on : 15/12/2014, 12:49:33 PM
    Author     : 578
--%>

<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.dao.dto.SgfensClienteVendedor"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensClienteVendedorDaoImpl"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.dao.dto.Usuarios"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sct.dao.dto.PretoCaracteristicasConsola"%>
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
    
      
    String buscar_idvendedor = request.getParameter("q_idvendedor")!=null?request.getParameter("q_idvendedor"):"";    
    String buscar_idcliente = request.getParameter("q_idcliente")!=null?request.getParameter("q_idcliente"):"";
    String buscar_dia = request.getParameter("q_dia")!=null?request.getParameter("q_dia"):"";
    String buscar_agenda = request.getParameter("agenda")!=null?request.getParameter("agenda"):"";   
   
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
                            
    
    
    int idEmpleado = -1;
    try{ idEmpleado = Integer.parseInt(request.getParameter("idEmpleado")); }catch(NumberFormatException e){}
    
    
    
    long idEmpresa = user.getUser().getIdEmpresa();
    
    String parametrosPaginacion="";
    
    
    //Si es vendedor, filtramos para solo mostrar sus pedidos
    if (!buscar_idvendedor.trim().equals("")){
        filtroBusqueda += " AND ID_USUARIOS='" + buscar_idvendedor +"' ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="q_idvendedor="+buscar_idvendedor;
    }
    
    
    if (!buscar_idcliente.trim().equals("")){
        filtroBusqueda += " AND (ID_USUARIOS IN (SELECT ID_USUARIO_VENDEDOR FROM sgfens_cliente_vendedor WHERE ID_CLIENTE = '" + buscar_idcliente +"' ) "
                + "OR ID_USUARIOS IN (SELECT ID_USUARIO_VENDEDOR_REASIGNADO FROM sgfens_cliente_vendedor WHERE ID_CLIENTE = '" + buscar_idcliente +"') ) ";        
      
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="buscar_idcliente="+buscar_idcliente;
    }
    
    
    if (!buscar_dia.trim().equals("")){
        filtroBusqueda += " AND ((ID_USUARIOS IN (SELECT ID_USUARIO_VENDEDOR FROM sgfens_cliente_vendedor WHERE ID_CLIENTE IN (SELECT ID_CLIENTE FROM cliente WHERE DIAS_VISITA LIKE '%"+buscar_dia+"%')) "
        + "OR  ID_USUARIOS IN (SELECT ID_USUARIO_VENDEDOR_REASIGNADO FROM sgfens_cliente_vendedor WHERE ID_CLIENTE IN (SELECT ID_CLIENTE FROM cliente WHERE DIAS_VISITA LIKE '%"+buscar_dia+"%'))))";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="q_dia="+buscar_dia;
    }
    
    // Con Agenda reasignada
    if(buscar_agenda.equals("1")){        
        filtroBusqueda += " AND ((SELECT COUNT(*) FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR_REASIGNADO = ID_USUARIOS ) > 0) ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="agenda="+buscar_agenda;
    }else if (buscar_agenda.equals("2")){//sin agneda reasignada
        filtroBusqueda += " AND ((SELECT COUNT(*) FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR_REASIGNADO = ID_USUARIOS ) <= 0) ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="agenda="+buscar_agenda;
    }
    
    
    
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
    String urlTo = "../catClientes/catClientes_list.jsp";
    String urlTo2 = "../catEmpleados/catEmpleado_CompartirClientes_form.jsp";
    String urlTo3 = "../catEmpleados/catEmpleados_formMapa.jsp";
    String urlTo4 = "../catEmpleados/catEmpleados_RutaDia.jsp"; 
    String urlToAgenda = "../catEmpleados/catEmpleado_Agenda_list.jsp";
    String paramName = "q_idvendedor";    
    //String parametrosPaginacion="";// "idEmpresa="+idEmpresa;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
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
                    
            
            function compartirAgenda(idEmpleado){
                if (idEmpleado>0){
                    $.ajax({
                        type: "POST",
                        url: "catEmpleados_ajax.jsp",
                        data: {mode: 'compartirAgenda', idEmpleado : idEmpleado},
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
                                            location.href = "catEmpleado_Clientes_List.jsp?pagina=<%=paginaActual%>";
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
            
            
            
            function eliminarClientesCompartidos(idEmpleado){
                if (idEmpleado>0){
                    $.ajax({
                        type: "POST",
                        url: "catEmpleados_ajax.jsp",
                        data: {mode: 'eliminarAgenda', idEmpleado : idEmpleado},
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
                                            location.href = "catEmpleado_Clientes_List.jsp?pagina=<%=paginaActual%>";
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
                    <h1>Pretoriano Móvil</h1>
                    
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
                            <form action="catEmpleado_Clientes_List.jsp" id="search_form_advance" name="search_form_advance" method="post">                                
                                <% if (user.getUser().getIdRoles() != RolesBO.ROL_OPERADOR){%>
                                <p>
                                <label>Vendedor:</label><br/>
                                <select id="q_idvendedor" name="q_idvendedor" class="flexselect">
                                    <option></option>                 
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo((int)idEmpresa, RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL, 0) %>                         
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo((int)idEmpresa, RolesBO.ROL_CONDUCTOR_VENDEDOR, 0) %>
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo((int)idEmpresa, RolesBO.ROL_COBRADOR, 0) %>
                                </select>
                                </p>
                                <br/>
                                <% } %>                                
                                <p>
                                <label>Cliente:</label><br/>
                                <select id="q_idcliente" name="q_idcliente" class="flexselect">
                                    <option></option>
                                    <%= new ClienteBO(user.getConn()).getClientesByIdHTMLCombo((int)idEmpresa, -1," AND ID_ESTATUS<>2 " + (user.getUser().getIdRoles() == RolesBO.ROL_CONDUCTOR_VENDEDOR? " AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM SGFENS_CLIENTE_VENDEDOR WHERE ID_USUARIO_VENDEDOR="+user.getUser().getIdUsuarios()+")" : "") ) %>
                                </select>
                                </p>
                                <br/>
                                
                                <label>Dia:</label><br/>
                                <select id="q_dia" name="q_dia" class="flexselect">
                                    <option value=""></option>
                                    <option value="DOM">Domingo</option>
                                    <option value="LUN">Lunes</option>
                                    <option value="MAR">Martes</option>
                                    <option value="MIE">Miércoles</option>
                                    <option value="JUE">Jueves</option>
                                    <option value="VIE">Viernes</option>  
                                    <option value="SAB">Sábado</option>
                                </select>
                                </p>
                                <br/>
                                <p>
                                    <input type="radio" class="checkbox" id="activos" name="agenda" value="1" style="float: left;" > <label for="agenda" style="float: left;">Con Agenda Reasignada</label>               
                                    <input type="radio" class="checkbox" id="inactivos" name="agenda" value="2" style="float: left;" > <label for="agenda"  style="float: left;">Sin Agenda Reasignada</label>                                                                                                                                      
                                </p>                                
                                <br/>                                
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
                                <img src="../../images/cliemp_16.png" alt="icon"/>
                                Empleados - Clientes
                            </span>
                            <div class="switch" style="width:750px">
                                <table width="750px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                            <tr>
                                                <td>
                                                    <div id="search">
                                                        <form action="catEmpleado_Clientes_List.jsp" id="search_form" name="search_form" method="get">                                                                                                                                                                    
                                                            <input type="text" id="q" name="q" title="Buscar por # Empleado/Nombre/Apellido Paterno/Materno/Rol/Sucursal/Estatus" class="" style="width: 300px; float: left; "
                                                                   value="<%=buscar%>"/>  
                                                            <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="width: 30px; height: 25px; float: left"/>
                                                    </form>
                                                    </div>
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
                                            <th>Número de Empleado</th>
                                            <th>Nombre</th>
                                            <th>Apellido Paterno</th>
                                            <th>Apellido Materno</th>
                                            <th>Clientes Asignados</th>   
                                            <th>Clientes Reasignados</th> 
                                            <th>Comparte Clientes de:</th> 
                                            <th>Hasta:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>  
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            for (Empleado item:empleadosDto){
                                                try{                         
                                                    
                                                   SgfensClienteVendedorDaoImpl clienteVendedorDaoImpl = new SgfensClienteVendedorDaoImpl();
                                                   //SgfensClienteVendedor[] clientesAsignados = clienteVendedorDaoImpl.findByDynamicWhere(" ID_USUARIO_VENDEDOR =" +item.getIdUsuarios(), null);
                                                   String filtroClientesAsignadosDia = "";                                                   
                                                   
                                                   if (!buscar_dia.trim().equals("")){//cargamos                                                         
                                                        //filtroClientesAsignadosDia = " AND ((ID_USUARIO_VENDEDOR IN (SELECT ID_USUARIO_VENDEDOR FROM sgfens_cliente_vendedor WHERE ID_CLIENTE IN (SELECT ID_CLIENTE FROM cliente WHERE DIAS_VISITA LIKE '%"+buscar_dia+"%')))) ";
                                                        filtroClientesAsignadosDia = "AND (ID_CLIENTE IN (SELECT ID_CLIENTE FROM CLIENTE WHERE DIAS_VISITA LIKE '%"+buscar_dia+"%' ))";                                                        
                                                   }
                                                   
                                                   SgfensClienteVendedor[] clientesAsignados = clienteVendedorDaoImpl.findByDynamicWhere(" ID_USUARIO_VENDEDOR =" +item.getIdUsuarios() + " AND (ID_CLIENTE IN (SELECT ID_CLIENTE FROM CLIENTE WHERE (ID_ESTATUS = 1 OR ID_ESTATUS = 3) )) " + filtroClientesAsignadosDia, null);
                                                   //SgfensClienteVendedor[] clientesReasignados = clienteVendedorDaoImpl.findByDynamicWhere(" ID_USUARIO_VENDEDOR_REASIGNADO =" +item.getIdUsuarios(), null);
                                                   SgfensClienteVendedor[] clientesReasignados = clienteVendedorDaoImpl.findByDynamicWhere(" ID_USUARIO_VENDEDOR_REASIGNADO =" +item.getIdUsuarios() + " AND (ID_CLIENTE IN (SELECT ID_CLIENTE FROM CLIENTE WHERE (ID_ESTATUS = 1 OR ID_ESTATUS = 3) )) " + filtroClientesAsignadosDia, null);
                                                   SgfensClienteVendedor[] clientesDe = clienteVendedorDaoImpl.findByDynamicWhere(" ID_USUARIO_VENDEDOR_REASIGNADO =" +item.getIdUsuarios() + "  GROUP BY ID_USUARIO_VENDEDOR " , null);
                                                   
                                                   String cliCompartidos = "";
                                                   String cliHasta = "";
                                                   for(SgfensClienteVendedor cli: clientesDe){
                                                       Empleado empleadoComparte = empleadoBO.findEmpleadoByUsuario(cli.getIdUsuarioVendedor());
                                                       
                                                       if(!cliCompartidos.equals("")){
                                                           cliCompartidos += ",";
                                                       }
                                                       cliCompartidos += empleadoComparte.getNombre()!=null?empleadoComparte.getNombre():"" + " " + empleadoComparte.getApellidoPaterno()!=null?empleadoComparte.getApellidoPaterno():"" + " " + empleadoComparte.getApellidoMaterno()!=null?empleadoComparte.getApellidoMaterno():"" ;
                                                       if(!cliHasta.equals("")){
                                                           cliHasta += ",";
                                                       }
                                                       cliHasta += cli.getFechaLimiteReasigancion()!=null? df.format(cli.getFechaLimiteReasigancion()):"";
                                                   }
                                                           
                                                   
                                        %>
                                        <tr <%=(item.getIdEstatus()==2)?"style='background: #B0B1B1'":""%> >
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdEmpleado() %></td>
                                            <td><%=item.getNumEmpleado()%></td>
                                            <td><%=item.getNombre() %></td>
                                            <td><%=item.getApellidoPaterno() %></td>
                                            <td><%=item.getApellidoMaterno() %></td> 
                                            <td><%=clientesAsignados.length%></td>                                             
                                            <td><%=clientesReasignados.length%></td>
                                            <td><%=cliCompartidos%></td> 
                                            <td><%=cliHasta%></td> 
                                            <td>     
                                                
                                                <%if(RolAutorizacionBO.permisoAccionesElemento(user.getUser().getIdRoles())){%> 
                                                    <%if(clientesAsignados.length > 0){%>
                                                    <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdUsuarios()%>&tipoClientes=0&q_dia=<%=buscar_dia%>"><img src="../../images/icon_consultar.png" alt="Clientes Asignados" class="help" title="Clientes Asignados"/></a>
                                                    &nbsp;&nbsp;
                                                    <%}%>
                                                    <%if(clientesReasignados.length > 0){%>
                                                    <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdUsuarios()%>&tipoClientes=1"><img src="../../images/icon_consultar2.png" alt="Clientes Compartidos" class="help" title="Clientes Compartidos, Todos"/></a>
                                                    &nbsp;&nbsp;
                                                    <%}%>             
                                                    <a href="<%=urlTo2%>?<%=paramName%>=<%=item.getIdUsuarios()%>"><img src="../../images/share.png" alt="Compartir Agenda" class="help" title="Compartir Agenda, Todos"/></a>                                                                                                        
                                                    &nbsp;&nbsp;
                                                    <a href="#" onclick="eliminarClientesCompartidos(<%=item.getIdUsuarios()%>);"><img src="../../images/icon_delete.png" alt="delete" class="help" title="Eliminar Compartidos, Todos"/></a>
                                                    &nbsp;&nbsp;                                                   
                                                                                                       
                                                    
                                                <%} %>  
                                                    
                                                    
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
