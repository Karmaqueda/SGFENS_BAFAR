<%-- 
    Document   : catCobranzas_Referencias_list
    Created on : 15/05/2014, 05:38:44 PM
    Author     : 578
--%>

<%@page import="java.text.NumberFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.dto.VistaCobranzaReferenciaGrupos"%>
<%@page import="com.tsp.sct.bo.VistaCobranzaReferenciaGruposBO"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
  
    long idEmpresa = user.getUser().getIdEmpresa();
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";   
    String buscar_idvendedor = request.getParameter("q_idvendedor")!=null?request.getParameter("q_idvendedor"):"";
    
    String filtroBusqueda = "";
    String parametrosPaginacion = "";
    
    //Filtro tipo ref
    String refBancaria = "";
    String refEfectivo = "";    
    
    try{
        refBancaria = new String(request.getParameter("refBancaria").getBytes("ISO-8859-1"),"UTF-8");
    }catch(Exception e){System.out.println("----no hay combox refBancaria");}
    try{
        refEfectivo = new String(request.getParameter("refEfectivo").getBytes("ISO-8859-1"),"UTF-8");
    }catch(Exception e){System.out.println("----no hay combox refEfectivo");}
    
    if(!refBancaria.equals("")){
        filtroBusqueda = " AND (LENGTH(REFERENCIA) = 6 ) ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&"; 
        parametrosPaginacion+="refBancaria=refBancaria";
    }
    if(!refEfectivo.equals("")){
        filtroBusqueda = " AND (LENGTH(REFERENCIA) = 15 ) ";
       if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&"; 
        parametrosPaginacion+="refEfectivo=refEfectivo";
    }    
    
    
    
    if (!buscar_idvendedor.trim().equals("")){        
      
        filtroBusqueda += " AND ID_USUARIO_VENDEDOR=" + buscar_idvendedor +" ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="q_idvendedor="+buscar_idvendedor;
    }
    
    
    if (!buscar.trim().equals("")){                        
        
        filtroBusqueda = " AND (REFERENCIA lIKE '%" + buscar + "%')";                
    }
    
    
    
    
    
    
    String buscar_fechamin = "";
    String buscar_fechamax = "";
    Date fechaMin=null;
    Date fechaMax=null;
    String strWhereRangoFechas="";
    {
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
            strWhereRangoFechas="(CAST(FECHA_ABONO AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+" 23:59:59.0')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax)+"&q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechas="(CAST(FECHA_ABONO AS DATE)  >= '"+buscar_fechamin+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechas="(CAST(FECHA_ABONO AS DATE)  <= '"+buscar_fechamax+" 23:59:59.0')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax);
        }
    }
    
    if (!strWhereRangoFechas.trim().equals("")){
        filtroBusqueda += " AND " + strWhereRangoFechas;
    }
    
    
    if(!parametrosPaginacion.equals("")){
        parametrosPaginacion+="&";    
        parametrosPaginacion+="buscar="+buscar;
    }
    
    
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
    
     VistaCobranzaReferenciaGruposBO referenciaGruposBO = new VistaCobranzaReferenciaGruposBO(user.getConn());
     VistaCobranzaReferenciaGrupos[] referenciaGruposDto = new VistaCobranzaReferenciaGrupos[0];
     try{
         limiteRegistros = referenciaGruposBO.findVistaCobranzaReferenciaGrupos(-1, idEmpresa , 0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        referenciaGruposDto = referenciaGruposBO.findVistaCobranzaReferenciaGrupos(-1, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
    
     
     

    
   
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");      
    
    //Fancybox Emergente
    String urlTo = "catCobranzaAbono_list.jsp";
    String paramName = "referenciaPago";
    
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    NumberFormat formatMoneda = new DecimalFormat("###,###,###,##0.00");  
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
                
            
        </script>

    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Referencias</h1>
                                       
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <div class="onecolumn">
                        <div class="header">                            
                            <span>
                                Búsqueda Avanzada &dArr;
                            </span>                           
                        </div>  
                            <br class="clear"/>
                        <div class="content" style="display: none;">
                            <form action="catCobranzaAbono_Referencias_list.jsp" id="search_form_advance" name="search_form_advance" method="post">
                                
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

                                <p>
                                    <input type="checkbox" class="checkbox" id="refBancaria" name="refBancaria" value="refBancaria" > <label for="refBancaria">Referencia Bancaria</label>
                                    
                                    <input type="checkbox" class="checkbox" id="refEfectivo" name="refEfectivo" value="refEfectivo" > <label for="refEfectivo">Referencia Efectivo</label>              
                                </p>
                                <br/>
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
                                <img src="../../images/Tickets_16.png" alt="icon"/> 
                                Referencias
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                            <tr>
                                                <td>
                                                    <div id="search">
                                                        <form action="catCobranzaAbono_Referencias_list.jsp" id="search_form" name="search_form" method="get">
                                                            
                                                            <input type="text" id="q" name="q" title="Buscar por Referencia" class="" style="width: 300px; float: left; "
                                                                       value="<%=buscar%>"/>
                                                            <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>    
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
                                <table class="data" width="100%" cellpadding="0" cellspacing="0" >
                                    <thead>
                                        <tr>
                                            <th>Sucursal</th>
                                            <th>Vendedor</th>
                                            <th>Referencia</th>
                                            <th>Fecha</th>
                                            <th>Total</th>
                                            <th>Numero de Pagos</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                        <% 
                                        
                                            Empleado empleado = null;
                                            EmpleadoBO empleadoBO = null;
                                            Empresa empresa = null;
                                            EmpresaBO empresaBO = null;
                                        
                                            for (VistaCobranzaReferenciaGrupos item:referenciaGruposDto){                                               
                                                try{
                                                    
                                                    try{
                                                        //PARA BUSCAR EL EMPLEADO-PROMOTOR AL QUE PERTENECE
                                                        empleadoBO = new EmpleadoBO(user.getConn());
                                                        empleado = empleadoBO.findEmpleadoByUsuario(item.getIdUsuarioVendedor());
                                                    }catch(Exception ex){
                                                        empleado.setNombre("");
                                                        empleado.setApellidoPaterno("");
                                                        empleado.setApellidoMaterno(""); 
                                                    }
                                                    
                                                    try{
                                                        //PARA BUSCAR EL EMPLEADO-PROMOTOR AL QUE PERTENECE
                                                        empresaBO = new EmpresaBO(user.getConn());
                                                        empresa = empresaBO.findEmpresabyId(item.getIdEmpresa());
                                                    }catch(Exception ex){
                                                        empresa.setRazonSocial("");
                                                    }
                                        %>                                                                              
                                                    <td><%=empresa.getRazonSocial()%></td>                                            
                                                    <td><%=empleado.getNombre()+" "+empleado.getApellidoPaterno()+" "+empleado.getApellidoMaterno() %></td>
                                                    <td><%=item.getReferencia() %></td>
                                                    <td><%=format.format(item.getFechaAbono())%></td>
                                                    <td><%=formatMoneda.format(item.getTotal()) %></td>
                                                    <td><%=item.getNumPagos() %></td>
                                                    <td><a href="<%=urlTo%>?<%=paramName%>=<%=item.getReferencia()%>"><img src="../../images/icon_consultar.png" alt="Consultar" class="help" title="Detalle"/></a></td>
                                                                                        
                                        </tr>
                                        <%      }catch(Exception ex){
                                                    ex.printStackTrace();
                                                }
                                            } 
                                        %>
                                    </tbody>
                                </table>
                            </form>
                                    
                            <div id="action_buttons">
                            <p> 
                                <br>
                                <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                            </p>
                            </div>
                                                                
                           <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <jsp:include page="../include/reportExportOptions.jsp" flush="true">
                            <jsp:param name="idReport" value="<%= ReportBO.REFERENCIA_REPORT %>" />
                            <jsp:param name="parametrosCustom" value="<%= filtroBusquedaEncoded %>" />
                            </jsp:include>                                
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
        <script>
            mostrarCalendario();     
            $("select.flexselect").flexselect();
        </script>    
    </body>
</html>




<%}%>