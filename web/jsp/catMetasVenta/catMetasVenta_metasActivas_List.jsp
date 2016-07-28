<%-- 
    Document   : catMetasVenta_metasActivas_List
    Created on : 8/09/2015, 11:08:25 AM
    Author     : HpPyme
--%>

<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.SGPedidoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedido"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.bo.SGVisitaClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensVisitaCliente"%>
<%@page import="com.tsp.sct.bo.SGProspectoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProspecto"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.dao.dto.MetasVenta"%>
<%@page import="com.tsp.sct.dao.jdbc.MetasVentaDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.MetasEmpleado"%>
<%@page import="com.tsp.sct.bo.MetasEmpleadoBO"%>
<%@page import="java.util.StringTokenizer"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    
    
    String buscar_idvendedor = request.getParameter("q_idvendedor")!=null?request.getParameter("q_idvendedor"):"";
    String buscar_tipoMeta = request.getParameter("tipoMeta")!=null?request.getParameter("tipoMeta"):"";
    String buscar_idEstatus = request.getParameter("q_idEstatus")!=null?request.getParameter("q_idEstatus"):"";
    String buscar_Cumplimiento = request.getParameter("q_cumplimiento")!=null?request.getParameter("q_cumplimiento"):"";
    
    String FechaInicioHisto = request.getParameter("q_fh_min")!=null? new String(request.getParameter("q_fh_min").getBytes("ISO-8859-1"),"UTF-8") :"";
    String FechaFinHisto = request.getParameter("q_fh_max")!=null? new String(request.getParameter("q_fh_max").getBytes("ISO-8859-1"),"UTF-8") :"";
    
    
    
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
    ////
    
    String cadenaParametrosPaginacion = "";  
    String strWhereRangoFechas="";
    String filtroBusqueda = "";
    
  
    
    //FILTRO POR FECHA
    if (!FechaInicioHisto.trim().equals("")&&!FechaFinHisto.trim().equals("")){
        filtroBusqueda += " AND (FECHA_INICIO BETWEEN '"+ FechaInicioHisto2 + "' AND '" + FechaFinHisto2 + " 23:59:59.99')";
        if(!cadenaParametrosPaginacion.equals(""))
                cadenaParametrosPaginacion+="&";
        cadenaParametrosPaginacion+="fechaInicioHistoria="+FechaInicioHisto+"&fechaFinHistoria="+FechaFinHisto;
    }else if (!FechaInicioHisto.trim().equals("")&&FechaFinHisto.trim().equals("")){
        filtroBusqueda += " AND FECHA_INICIO > '"+ FechaInicioHisto2 + "'";
        if(!cadenaParametrosPaginacion.equals(""))
                cadenaParametrosPaginacion+="&";
        cadenaParametrosPaginacion+="fechaInicioHistoria="+FechaInicioHisto;
    }else if (FechaInicioHisto.trim().equals("")&&!FechaFinHisto.trim().equals("")){
        filtroBusqueda += " AND FECHA_INICIO < '"+ FechaFinHisto2 + " 23:59:59.99'";
        if(!cadenaParametrosPaginacion.equals(""))
                cadenaParametrosPaginacion+="&";
        cadenaParametrosPaginacion+="fechaFinHistoria="+FechaFinHisto;
    }else{
        filtroBusqueda = filtroBusqueda; 
    }
    //////
        
    EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
    Empleado empleadoDto2 = null;
    if (!buscar_idvendedor.trim().equals("")){
        
         empleadoDto2 = new EmpleadoBO(user.getConn()).findEmpleadoByUsuario(Integer.parseInt(buscar_idvendedor));             
        
        filtroBusqueda += " AND ID_EMPLEADO =" + empleadoDto2.getIdEmpleado() +" ";
        if(!cadenaParametrosPaginacion.equals(""))
                    cadenaParametrosPaginacion+="&";
        cadenaParametrosPaginacion+="q_idvendedor="+buscar_idvendedor;
    }
    
    
    if (!buscar_tipoMeta.trim().equals("")){     
        
        filtroBusqueda += " AND ID_META_VENTA IN (SELECT ID_META_VENTA FROM metas_venta WHERE ID_COMPONENTE_META = "+ buscar_tipoMeta+" )  ";
        if(!cadenaParametrosPaginacion.equals(""))
                    cadenaParametrosPaginacion+="&";
        cadenaParametrosPaginacion+="tipoMeta="+buscar_tipoMeta;
    }
    
    if (!buscar_idEstatus.trim().equals("")){     
        
        filtroBusqueda += " AND ID_ESTATUS  = "+ buscar_idEstatus+"  ";
        if(!cadenaParametrosPaginacion.equals(""))
                    cadenaParametrosPaginacion+="&";
        cadenaParametrosPaginacion+="tipoMeta="+buscar_tipoMeta;
    }
    
        
        
        
    
    
    System.out.println("--------------------------------" + filtroBusqueda);
    
    long idEmpresa = user.getUser().getIdEmpresa();
    
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
    
     MetasEmpleadoBO metasEmpleadoBO = new MetasEmpleadoBO(user.getConn());
     MetasEmpleado[] metasEmpleadosDto = new MetasEmpleado[0];
     try{
         
         if (buscar_Cumplimiento.trim().equals("")){ 
            limiteRegistros = metasEmpleadoBO.findMetasEmpleado(-1,-1,idEmpresa , 0, 0, filtroBusqueda).length;
         }else{
            limiteRegistros = 1; 
         }
         
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        if (buscar_Cumplimiento.trim().equals("")){ 
            metasEmpleadosDto = metasEmpleadoBO.findMetasEmpleado(-1,-1, idEmpresa,
                    ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                    filtroBusqueda);
        }else {
            metasEmpleadosDto = metasEmpleadoBO.findMetasEmpleado(-1,-1, idEmpresa, -1, -1 ,   filtroBusqueda);
        }
         
     }catch(Exception ex){
         ex.printStackTrace();
     }                                               
     
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catMetasVenta/catMetasVenta_metasEmpleado_form.jsp";    
    String urlTo2 = "catCargaManual_logDescripcion.jsp";
    String paramName = "idCargaManual";
    String parametrosPaginacion=cadenaParametrosPaginacion;// "idEmpresa="+idEmpresa;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    
    SimpleDateFormat format  = new SimpleDateFormat("dd/MM/yyyy");
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
            
            function confirmarDeleteMeta(idMetaEmpleado){
                apprise('¿Esta seguro que desea eliminar la Meta del Empleado ?', {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, 
                    function(r){
                        if(r){
                            // Usuario dio click 'Yes'
                            deleteMeta(idMetaEmpleado);
                        }
                });

            }

            function deleteMeta(idMetaEmpleado){
                if(idMetaEmpleado>0){
                    $.ajax({
                        type: "POST",
                        url: "metasVenta_ajax.jsp",
                        data: { mode: 'deleteMetaEmpleado', idMetaEmpleado : idMetaEmpleado },
                        beforeSend: function(objeto){
                            //$("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style="">ESPERE, procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               location.href = "catMetasVenta_metasActivas_List.jsp?pagina="+"<%=paginaActual%>";
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               //$("#ajax_message").html(datos);
                               //$("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
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
                            <p>
                            <form action="catMetasVenta_metasActivas_List.jsp" id="search_form_advance" name="search_form_advance" method="post">                                                            
                                <p>
                                    Por Fecha de Inicio &raquo;&nbsp;&nbsp;
                                    <label>Desde:</label>
                                    <input maxlength="15" type="text" id="q_fh_min" name="q_fh_min" style="width:100px"
                                            value="" readonly/>
                                    &nbsp; &laquo; &mdash; &raquo; &nbsp;
                                    <label>Hasta:</label>
                                    <input maxlength="15" type="text" id="q_fh_max" name="q_fh_max" style="width:100px"
                                        value="" readonly/>
                                </p>
                                <br/> 
                                    <label>Tipo de Meta:</label>      
                                    <br/> 
                                    <select id="tipoMeta" name="tipoMeta" style="width: 150px;" class="flexselect">
                                        <option value=""></option>
                                        <option value="1">Monto de venta</option>
                                        <option value="2">Clientes</option>
                                        <option value="3">Visitas</option>
                                        <option value="4">Propsectos</option>
                                    </select>
                                </p>
                                <br/>
                                <% if (user.getUser().getIdRoles() != RolesBO.ROL_OPERADOR){%>
                                <p>
                                <label>Vendedor:</label><br/>
                                <select id="q_idvendedor" name="q_idvendedor" class="flexselect">
                                    <option></option>                 
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo((int)idEmpresa, RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL, 0) %> 
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo((int)idEmpresa, RolesBO.ROL_OPERADOR, 0) %>
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo((int)idEmpresa, RolesBO.ROL_CONDUCTOR, 0) %>
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo((int)idEmpresa, RolesBO.ROL_CONDUCTOR_VENDEDOR, 0) %>
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo((int)idEmpresa, RolesBO.ROL_COBRADOR, 0) %>
                                </select>
                                </p>   
                                <br/>
                                <% } %> 
                                <p>                                   
                                    <label>Estatus:</label>  
                                    <br/>
                                    <select id="q_idEstatus" name="q_idEstatus" style="width: 150px;" class="flexselect">
                                        <option value="1"></option>
                                        <option value="1">Activos</option>
                                        <option value="2">Inactivos</option>                                       
                                    </select>
                                </p>
                                <br/> 
                                <p>                                   
                                    <label>Cumplimiento:</label>      
                                    <br/>
                                    <select id="q_cumplimiento" name="q_cumplimiento" style="width: 150px;" class="flexselect">
                                        <option value=""></option>
                                        <option value="1">Cumplidas</option>
                                        <option value="2">No Cumplidas</option>                                       
                                    </select>
                                </p>
                                <br/>
                                
                                <div id="action_buttons">
                                    <p>
                                        <input type="button" id="buscar" value="Buscar" onclick="$('#search_form_advance').submit();"/>
                                    </p>
                                </div>
                            </form>
                            </p>  
                            <br/>
                        </div>
                   </div>
                    

                    <div class="onecolumn">  
                        <div class="header">
                            <span>
                                <img src="../../images/proposito.png" alt="icon"/>
                                Metas de Venta
                            </span>
                            <div class="switch" style="width:70%">                                
                            
                                <table width="100%" cellpadding="0" cellspacing="0">
                                        <tbody>
                                                <tr>  
                                                    <td>
                                                        <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Nueva Meta" 
                                                            style="float: right; width: 100px;" onclick="location.href='<%=urlTo%>'"/>
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
                                            <th>Nombre</th>
                                            <th>Empleado</th> 
                                            <th>Inicio</th>
                                            <th>Fin</th>
                                            <th>Tipo de Meta</th>
                                            <th>Meta</th>
                                            <th>Avance</th>
                                            <th>% Cumplimiento</th>
                                            <th>Acciones</th>                                           
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%                                         
                                           
                                       String nombreEmpleado ="" ;
                                       double avance = 0;
                                       double avancePorcentaje = 0;
                                       for (MetasEmpleado item:metasEmpleadosDto){
                                           avance = 0;
                                                try{


                                                MetasVenta metaPrincipal =  new MetasVentaDaoImpl(user.getConn()).findByPrimaryKey(item.getIdMetaVenta());
                                                
                                                Empleado empleadoDto = new EmpleadoBO(item.getIdEmpleado(),user.getConn()).getEmpleado();
                                                nombreEmpleado = empleadoDto.getNombre() +" "+ empleadoDto.getApellidoPaterno() +" "+ empleadoDto.getApellidoMaterno();
                                                
                                                
                                                //Metas de Venta
                                                if(metaPrincipal.getIdComponenteMeta()==1){
                                                    SgfensPedido[] pedidosLevantados = new SgfensPedido[0];
                                                    String filtroBusquedaPedidos = " AND FECHA_PEDIDO > '" + item.getFechaInicio() + "' AND FECHA_PEDIDO <= '" + item.getFechaFin() + "' AND ID_USUARIO_VENDEDOR = " + empleadoDto.getIdUsuarios();
                                                    pedidosLevantados = new SGPedidoBO(user.getConn()).findPedido(-1, (int) idEmpresa, -1, -1, filtroBusquedaPedidos);
                                                    
                                                    for(SgfensPedido pedido:pedidosLevantados){
                                                        avance += pedido.getTotal();
                                                    }
                                                    
                                                    
                                                    //avance = pedidosLevantados.length;
                                                    avancePorcentaje = (avance * 100)/ item.getObjetivoMeta(); //rgela 3 obtener % avance
                                                }
                                                
                                                //Fin Metas de Venta
                                                
                                                
                                                //Metas de Clientes
                                                if(metaPrincipal.getIdComponenteMeta()==2){
                                                    Cliente[] clientesLevantados = new Cliente[0];
                                                    String filtroBusquedaClientes = " AND FECHA_REGISTRO > '" + item.getFechaInicio() + "' AND FECHA_REGISTRO <= '" + item.getFechaFin() + "' AND ID_USUARIO_ALTA = " + empleadoDto.getIdUsuarios();
                                                    clientesLevantados = new ClienteBO(user.getConn()).findClientes(-1, (int) idEmpresa, -1, -1, filtroBusquedaClientes);
                                                    
                                                    avance = clientesLevantados.length;
                                                    avancePorcentaje = (avance * 100)/ item.getObjetivoMeta(); //rgela 3 obtener % avance
                                                }
                                                
                                                //Fin Metas de Clientes
                                                
                                                
                                                //Metas de Visitas
                                                if(metaPrincipal.getIdComponenteMeta()==3){
                                                    SgfensVisitaCliente[] visitasRealizadas = new SgfensVisitaCliente[0];
                                                    String filtroBusquedaVisitas = " AND FECHA_HORA > '" + item.getFechaInicio() + "' AND FECHA_HORA <= '" + item.getFechaFin() + "' AND ID_EMPLEADO_VENDEDOR = " + empleadoDto.getIdEmpleado();
                                                    visitasRealizadas = new SGVisitaClienteBO(user.getConn()).findSgfensVisitaClientes(-1, (int) idEmpresa, -1, -1, filtroBusquedaVisitas);
                                                    
                                                    avance = visitasRealizadas.length;
                                                    avancePorcentaje = (avance * 100)/ item.getObjetivoMeta(); //rgela 3 obtener % avance
                                                }
                                                
                                                //Fin Metas de Visitas 
                                                
                                                
                                                //Metas de Prospectos
                                                if(metaPrincipal.getIdComponenteMeta()==4){
                                                    SgfensProspecto[] prospectosLevantados = new SgfensProspecto[0];
                                                    String filtroBusquedaMeta = " AND FECHA_REGISTRO > '" + item.getFechaInicio() + "' AND FECHA_REGISTRO <= '" + item.getFechaFin() + "' AND ID_USUARIO_VENDEDOR = " + empleadoDto.getIdUsuarios();
                                                    prospectosLevantados = new SGProspectoBO(user.getConn()).findProspecto(-1, (int) idEmpresa, -1, -1, filtroBusquedaMeta);
                                                    
                                                    avance = prospectosLevantados.length;
                                                    avancePorcentaje = (avance * 100)/ item.getObjetivoMeta(); //rgela 3 obtener % avance
                                                }                                                
                                                //Fin Metas de Prospectos       
                                                
                                                
                                                //filtro cumplidas o no cumplidas
                                                if (buscar_Cumplimiento.trim().equals("1")){ //cumplidas
                                                    if(avancePorcentaje<100){
                                                        continue;
                                                    }
                                                }else if(buscar_Cumplimiento.trim().equals("2")){ // no cumplidas
                                                    if(avancePorcentaje>=100){
                                                        continue;
                                                    }
                                                }
                                                
                                                
                                                
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%> >                                           
                                            <td><%=item.getIdMetaEmpleado()%></td>
                                            <td><%=metaPrincipal!=null?metaPrincipal.getNombreMeta():""%></td>
                                            <td><%=nombreEmpleado%></td>
                                            <td><%=format.format(item.getFechaInicio())%></td>
                                            <td><%=format.format(item.getFechaFin())%></td>                                           
                                            <td><%=metaPrincipal.getIdComponenteMeta()==1?"Monto de venta":metaPrincipal.getIdComponenteMeta()==2?"Clientes":metaPrincipal.getIdComponenteMeta()==3?"Visitas":metaPrincipal.getIdComponenteMeta()==4?"Prospectos":"Otro"%></td>                                            
                                            <td><%=item.getObjetivoMeta()%></td>
                                            <td><%=avance%></td>
                                            <td><progress value="<%=avancePorcentaje%>" max="100"></progress>&nbsp;<%=avancePorcentaje%>%</td>
                                            <td>
                                                <a onclick="confirmarDeleteMeta(<%=item.getIdMetaEmpleado()%>);"><img src="../../images/icon_delete.png" alt="icon" alt="Eliminar" class="help" title="Eliminar"/></a></td>
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
                            
                            <div id="action_buttons">
                            <p>                                
                                <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                            </p>
                            </div>
                            
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