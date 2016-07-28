<%-- 
    Document   : catConceptosVendidos_list
    Created on : 3/10/2014, 05:37:11 PM
    Author     : 578
--%>


<%@page import="com.tsp.sct.dao.dto.ClienteCategoria"%>
<%@page import="com.tsp.sct.bo.ClienteCategoriaBO"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.dao.dto.DatosUsuario"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedido"%>
<%@page import="com.tsp.sct.bo.SGPedidoBO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedidoProducto"%>
<%@page import="com.tsp.sct.bo.SGPedidoProductoBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.Marca"%>
<%@page import="com.tsp.sct.dao.dto.Categoria"%>
<%@page import="com.tsp.sct.bo.CategoriaBO"%>
<%@page import="com.tsp.sct.bo.AlmacenBO"%>
<%@page import="com.tsp.sct.bo.MarcaBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";    
    String buscar_idcliente = request.getParameter("q_idcliente")!=null?request.getParameter("q_idcliente"):"";
    int idClienteCategoria = -1;
    try{
        idClienteCategoria = Integer.parseInt(request.getParameter("idClienteCategoria"));
    }catch(NumberFormatException ex){}
    String buscar_idvendedor = request.getParameter("q_idvendedor")!=null?request.getParameter("q_idvendedor"):"";
    String masMenosVendidos = request.getParameter("q_masMenosVendidos")!=null? new String(request.getParameter("q_masMenosVendidos").getBytes("ISO-8859-1"),"UTF-8") :"";
    
    String buscar_fechamin = "";
    String buscar_fechamax = "";
    Date fechaMin=null;
    Date fechaMax=null;
    String strWhereRangoFechas="";
    String filtroBusqueda = "";
    String parametrosPaginacion = "";
       
    
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
            strWhereRangoFechas = " ID_PEDIDO IN (SELECT ID_PEDIDO FROM SGFENS_PEDIDO WHERE CAST(FECHA_PEDIDO AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"') ";            
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax)+"&q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechas=" ID_PEDIDO IN (SELECT ID_PEDIDO FROM SGFENS_PEDIDO WHERE CAST(FECHA_PEDIDO AS DATE) >= '"+buscar_fechamin+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechas=" ID_PEDIDO IN (SELECT ID_PEDIDO FROM SGFENS_PEDIDO WHERE CAST(FECHA_PEDIDO AS DATE)  <= '"+buscar_fechamax+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax);
        }
    }
    
    
        
    if (!buscar.trim().equals("")){    //para buscar por descripcion    
        filtroBusqueda += " AND (DESCRIPCION LIKE '%" + buscar + "%')" ;
        
    if(!parametrosPaginacion.equals(""))
                        parametrosPaginacion+="&";
        parametrosPaginacion+="q="+buscar;        
    }
    
    
    if (!buscar_idcliente.trim().equals("")){
        filtroBusqueda += " AND ID_PEDIDO IN (SELECT ID_PEDIDO FROM SGFENS_PEDIDO WHERE ID_CLIENTE ='" + buscar_idcliente +"') ";        
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="buscar_idcliente="+buscar_idcliente;
    }else{
        filtroBusqueda += " AND ID_PEDIDO IN (SELECT ID_PEDIDO FROM SGFENS_PEDIDO WHERE ID_CLIENTE > 0 ) "; 
    }
        
    if (!buscar_idvendedor.trim().equals("")){
        filtroBusqueda += " AND ID_PEDIDO IN (SELECT ID_PEDIDO FROM SGFENS_PEDIDO WHERE ID_USUARIO_VENDEDOR ='" + buscar_idvendedor +"') ";          
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="q_idvendedor="+buscar_idvendedor;
    }
    
    
    if (!strWhereRangoFechas.trim().equals("")){
        filtroBusqueda += " AND " + strWhereRangoFechas;
    }
    
    if(idClienteCategoria > 0){ //hay que ver como recuperar solo los clientes aplicados del filtro, porque no hay relacion directa, primero se consulta el pedido al que pertenece el concepto y luego al cliente
        filtroBusqueda += " AND ID_PEDIDO IN (SELECT ID_PEDIDO FROM SGFENS_PEDIDO WHERE ID_CLIENTE IN (SELECT ID_CLIENTE FROM CLIENTE WHERE ID_CLIENTE_CATEGORIA = " + idClienteCategoria + " )) ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="idClienteCategoria="+idClienteCategoria;
    }
    
    ///**
    //FILTRO PARA LOS 10 MAS VENDIDOS
    /*
    SELECT ID_COMPROBANTE_DESCRIPCION, ID_CONCEPTO, DESCRIPCION, CANTIDAD, PRECIO_UNITARIO,
            count(*) AS 'CONTA' 
            FROM comprobante_descripcion 
            GROUP BY ID_CONCEPTO 
            ORDER BY CONTA DESC LIMIT 0,10;
    */
    
    ///**
    String ascDesc = "";
    int identificadorMasMenosVendidos = 0;
    if (masMenosVendidos.trim().equals("1")){
        System.out.println("....ES MAS VENDIDOS");
        //filtroBusqueda += " AND ID_TIPO = 1 ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="q_masMenosVendidos="+masMenosVendidos;
        ascDesc = "DESC";
        identificadorMasMenosVendidos = 1;
    }else if(masMenosVendidos.trim().equals("2")){       
        System.out.println("....ES MENOS VENDIDOS");
        //filtroBusqueda += " AND ID_TIPO = 2 ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="q_masMenosVendidos="+masMenosVendidos;
        ascDesc = "ASC";
        identificadorMasMenosVendidos = 2;
    }
    
    ///* para cuando filtra los pedidos que han sido cancelados:
    String noCancelados = request.getParameter("noCancelados")!=null? new String(request.getParameter("noCancelados").getBytes("ISO-8859-1"),"UTF-8") :"";
    
    if (noCancelados.trim().equals("3")){
        filtroBusqueda += " AND ID_PEDIDO IN (SELECT ID_PEDIDO FROM SGFENS_PEDIDO WHERE ID_ESTATUS_PEDIDO != 3 ) ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="noCancelados="+noCancelados;
    }
    ///*
   
    
    /*int idConcepto = -1;
    try{ idConcepto = Integer.parseInt(request.getParameter("idConcepto")); }catch(NumberFormatException e){}*/
    
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
    
     SGPedidoProductoBO pedidoProductoBO = new SGPedidoProductoBO(user.getConn());
     SgfensPedidoProducto[] pedidoProductoDto = new SgfensPedidoProducto[0];
     
     SgfensPedidoProducto[] pedidoProductoDtoAcums = pedidoProductoBO.findByIdPedido(-1, idEmpresa , 0, 0, filtroBusqueda, identificadorMasMenosVendidos, ascDesc);
     try{
         System.out.println("---------------------query conceptos-----");
         limiteRegistros = pedidoProductoDtoAcums.length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        pedidoProductoDto = pedidoProductoBO.findByIdPedido(-1, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda, identificadorMasMenosVendidos, ascDesc);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     
     
     
     
     /*Ciclo para obtener totales acumulados
     
     */         
     double unidadesVendidasSuma = 0;
     double costoUnitarioSuma = 0;
     double costoTotalSuma = 0;
     double precioUnitarioSuma = 0;
     double precioTotalSuma = 0;
     double utilidadTotalSuma = 0;
     
     //SgfensPedidoProducto[] pedidoProductoDtoAcums = pedidoProductoBO.findByIdPedido(-1, idEmpresa , 0, 0, filtroBusqueda);
     for (SgfensPedidoProducto item: pedidoProductoDtoAcums){
         
         try{
             
            Concepto conceptoDto = null;
            try{
                ConceptoBO  conceptoBO = new ConceptoBO(user.getConn());
                conceptoDto =conceptoBO.findConceptobyId(item.getIdConcepto());
            }catch(Exception e){} 
            
            double costoTotal = 0;
            double precioTotal = 0;
            
                     
            
            if(conceptoDto!=null){
                //si es a granel tomamos el peso
                if(conceptoDto.getPrecioUnitarioGranel()>0 || conceptoDto.getPrecioMedioGranel()>0
                        || conceptoDto.getPrecioMayoreoGranel()>0 || conceptoDto.getPrecioEspecialGranel()>0){

                    costoTotal = item.getCostoUnitario() * item.getCantidadPeso();
                    precioTotal = item.getPrecioUnitarioGranel() * item.getCantidadPeso();
                    costoTotalSuma += item.getCostoUnitario() * item.getCantidadPeso();
                    precioTotalSuma +=  item.getPrecioUnitarioGranel() * item.getCantidadPeso();
                    precioUnitarioSuma +=item.getPrecioUnitarioGranel();
                }else{
                    costoTotal  = item.getCostoUnitario() * item.getCantidad();
                    precioTotal = item.getPrecioUnitario() * item.getCantidad();
                    costoTotalSuma += item.getCostoUnitario() * item.getCantidad();
                    precioTotalSuma += item.getPrecioUnitario() * item.getCantidad();
                    precioUnitarioSuma += item.getPrecioUnitario();
                }

            }else{
                costoTotal  = item.getCostoUnitario() * item.getCantidad();
                precioTotal = item.getPrecioUnitario() * item.getCantidad();
                costoTotalSuma += item.getCostoUnitario() * item.getCantidad();
                precioTotalSuma +=  item.getPrecioUnitario() * item.getCantidad();
                precioUnitarioSuma += item.getPrecioUnitario();
            }
            
             
            
            unidadesVendidasSuma +=  item.getCantidad();
            costoUnitarioSuma += item.getCostoUnitario();
            
            
            
            
            if(costoTotal>0){
               utilidadTotalSuma += precioTotal - costoTotal;
            }
               
        
        }catch(Exception ex){
            ex.printStackTrace();
        }
     }
     
     ClienteCategoriaBO clienteCategoriaBO = new ClienteCategoriaBO(user.getConn());
     ClienteCategoria[] clientesCategorias = clienteCategoriaBO.findClienteCategorias(0, idEmpresa, 0, 0, " AND ID_ESTATUS = 1 ");
     
     /*
    * Datos de catálogo
    */
    String urlTo = "";
    String paramName = "";
    //String parametrosPaginacion="";// "idEmpresa="+idEmpresa;    
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    String parametrosExtraEncoded = java.net.URLEncoder.encode(strWhereRangoFechas , "UTF-8");
        
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
                            <form action="catConceptosVendidos_list.jsp" id="search_form_advance" name="search_form_advance" method="post">
                               
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
                                <label>Cliente:</label><br/>
                                <select id="q_idcliente" name="q_idcliente" class="flexselect">
                                    <option></option>
                                    <%= new ClienteBO(user.getConn()).getClientesByIdHTMLCombo(idEmpresa, -1," AND ID_ESTATUS<> 2 " + (user.getUser().getIdRoles() == RolesBO.ROL_CONDUCTOR_VENDEDOR? " AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM SGFENS_CLIENTE_VENDEDOR WHERE ID_USUARIO_VENDEDOR="+user.getUser().getIdUsuarios()+")" : "") ) %>
                                </select>
                                </p>
                                <br/>
                                
                                <%if(clientesCategorias.length > 0){%>
                                    <p>
                                        <label>Tipo:</label><br/>
                                        <select id="idClienteCategoria" name="idClienteCategoria">
                                            <option value="0">Selecciona Tipo de Categoría de Cliente</option>
                                            <%
                                                out.print(clienteCategoriaBO.getClienteCategoriasByIdHTMLCombo(idEmpresa, -1));
                                            %>
                                        </select>
                                    </p>
                                    <br/>
                                <%}%>
                                
                                <% if (user.getUser().getIdRoles() != RolesBO.ROL_OPERADOR || user.getUser().getIdRoles() != RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL){%>
                                <p>
                                <label>Vendedor:</label><br/>
                                <select id="q_idvendedor" name="q_idvendedor" class="flexselect">
                                    <option></option>                                    
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL, 0) %>
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_OPERADOR, 0) %>
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_CONDUCTOR_VENDEDOR, 0) %>
                                </select>
                                </p>
                                <br/>
                                <% } %>
                                <br/>
                                
                                <p>                                    
                                    <input type="radio" class="checkbox" id="masVendidos" name="q_masMenosVendidos" value="1" > <label for="mas">Mas Vendidos</label>
                                    <input type="radio" class="checkbox" id="menosVendidos" name="q_masMenosVendidos" value="2" > <label for="menos">Menos Vendidos</label>
                                    <input type="checkbox" class="checkbox" id="noCancelados" name="noCancelados" value="3" > <label for="noCancelados">Omitir de Pedidos Cancelados</label>
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
                                <img src="../../images/icon_producto.png" alt="icon"/>
                                Ventas de Productos
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td>
                                                <div id="search">
                                                <form action="catConceptosVendidos_list.jsp" id="search_form" name="search_form" method="get">                                                        
                                                        <input type="text" id="q" name="q" title="Buscar por Nombre" class="" style="width: 300px; float: left; "
                                                                value="<%=buscar%>"/>
                                                        <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                </form>
                                                </div>
                                            </td>
                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                            
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
                                            <%if(identificadorMasMenosVendidos == 0){%>
                                            <th>ID Pedido</th>
                                            <%}else{%>
                                            <th>Pedidos que Tienen el Producto</th>
                                            <%}%>
                                            <th>Vendedor</th>
                                            <th>Cliente</th>
                                            <th>Fecha</th>
                                            <th>Código</th>
                                            <th>Producto</th>
                                            <th>Unidades<br>Vendidas</th>  
                                            <th>Precio Compra Unitario ($)</th>
                                            <th>Precio Compra Total ($)</th>                                             
                                            <th>Precio Venta Unitario ($)</th> 
                                            <th>Precio VentaTotal ($)</th>   
                                            <th>Utilidad ($)</th>  
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%     
                                            
                                        
                                            //ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
                                            for (SgfensPedidoProducto item:pedidoProductoDto){
                                                String fechaPedido = "";
                                                DatosUsuario datosUsuarioVendedor =  null;
                                                Cliente cliente = null;
                                                SgfensPedido pedido = null;
                                                try{
                                                    
                                                    Concepto conceptoDto = null;
                                                    try{
                                                        ConceptoBO  conceptoBO = new ConceptoBO(user.getConn());
                                                        conceptoDto =conceptoBO.findConceptobyId(item.getIdConcepto());
                                                    }catch(Exception e){}   
                                                    
                                                    /*Marca marcaDto = new MarcaBO(conceptoDto.getIdMarca(),user.getConn()).getMarca();
                                                    Categoria categoriaDto = new CategoriaBO(conceptoDto.getIdCategoria(),user.getConn()).getCategoria();
                                                    Categoria subcategoriaDto = new CategoriaBO(conceptoDto.getIdSubcategoria(),user.getConn()).getCategoria();*/
                                        
                                                    double costoTotal = 0;
                                                    double precioTotal = 0;
                                                    double precioUnitario = 0;
                                                    
                                                    if(conceptoDto!=null){
                                                        //si es a granel tomamos el peso
                                                        if(conceptoDto.getPrecioUnitarioGranel()>0 || conceptoDto.getPrecioMedioGranel()>0
                                                                || conceptoDto.getPrecioMayoreoGranel()>0 || conceptoDto.getPrecioEspecialGranel()>0){
                                                            
                                                            costoTotal = item.getCostoUnitario() * item.getCantidadPeso();
                                                            precioTotal = item.getPrecioUnitarioGranel() * item.getCantidadPeso();
                                                            precioUnitario =item.getPrecioUnitarioGranel();
                                                        }else{
                                                            costoTotal = item.getCostoUnitario() * item.getCantidad();
                                                            precioTotal = item.getPrecioUnitario() * item.getCantidad();
                                                            precioUnitario = item.getPrecioUnitario();
                                                        }
                                                    
                                                    }else{
                                                        costoTotal = item.getCostoUnitario() * item.getCantidad();
                                                        precioTotal = item.getPrecioUnitario() * item.getCantidad();
                                                        precioUnitario = item.getPrecioUnitario();
                                                    }
                                                    
                                                    
                                                    
                                                    double utilidad = 0;
                                                    
                                                    if(costoTotal==0){
                                                        utilidad = 0;
                                                    }else{
                                                        utilidad = precioTotal - costoTotal;
                                                    }
                                                    
                                                    if(identificadorMasMenosVendidos == 0){
                                                        ////**Recuperamos los datos del pedido:
                                                        pedido = new SGPedidoBO(item.getIdPedido(), user.getConn()).getPedido();                                                    
                                                        //PARA RECUPERAR LOS DATOS DE CLIENTE Y VENDEDOR
                                                        datosUsuarioVendedor = new UsuarioBO(pedido.getIdUsuarioVendedor()).getDatosUsuario();                                                    
                                                        try{ cliente = new ClienteBO(pedido.getIdCliente(),user.getConn()).getCliente(); }catch(Exception ex){}
                                                        try{fechaPedido = DateManage.formatDateToNormal(pedido.getFechaPedido()).toString();}catch(Exception e){}                                                    
                                                    }
                                                    ////**
                                        %>
                                                    <tr>
                                                        <%if(identificadorMasMenosVendidos == 0){%>
                                                            <td><%=item.getIdPedido()%></td>
                                                        <%}else{%>
                                                            <td><%=item.getIdConcepto()%></td>
                                                        <%}%>
                                                        <td><%= datosUsuarioVendedor!=null? (datosUsuarioVendedor.getNombre() +" " + datosUsuarioVendedor.getApellidoPat()) :"Multiples Vendedores" %></td>
                                                        <td>
                                                            <%
                                                                if (pedido!=null && cliente!=null){
                                                                 //Cliente
                                                                    out.print(cliente.getRazonSocial() + (cliente.getClave()!=null&&!cliente.getClave().trim().equals("")?(", Clave: "+cliente.getClave()):"") + " <i>[Cliente]</i>");
                                                                }else{
                                                                    out.print("Multiples Clientes");
                                                                }
                                                            %>
                                                        </td>
                                                        <td><%=!fechaPedido.equals("")?fechaPedido:"Multiples Fechas"%></td>
                                                        <td><%=item.getIdentificacion()!=null?item.getIdentificacion():"" %></td>
                                                        <td><%=item.getDescripcion() %></td> 
                                                        <td><%=item.getCantidad()%></td> 
                                                        <td><%=formatMoneda.format(item.getCostoUnitario())%></td> 
                                                        <td><%=formatMoneda.format(costoTotal)%></td>  
                                                        <td><%=formatMoneda.format(precioUnitario)%></td>
                                                        <td><%=formatMoneda.format(precioTotal)%></td>  
                                                        <td><%=formatMoneda.format(utilidad)%></td> 
                                                    </tr>
                                        <%      
                                                
                                                }catch(Exception ex){
                                                    ex.printStackTrace();
                                                }
                                            } 
                                        %>
                                        <tr style="font-size: 14;">
                                            <td style="text-align: right;"></td>
                                            <td style="text-align: right;"></td>
                                            <td style="text-align: right;"></td>
                                            <td style="text-align: right;"></td>
                                            <td style="text-align: right;"></td>                                            
                                            <td style="text-align: left;"><b>Totales:</b></td>
                                            <td style="text-align: left;color: #0000cc;"><%=unidadesVendidasSuma%></td>                                            
                                            <td style="text-align: left;color: #0000cc;"><%=formatMoneda.format(costoUnitarioSuma)%></td>
                                            <td style="text-align: left;color: #0000cc;"><%=formatMoneda.format(costoTotalSuma)%></td>
                                            <td style="text-align: left;color: #0000cc;"><%=formatMoneda.format(precioUnitarioSuma)%></td>
                                            <td style="text-align: left;color: #0000cc;"><%=formatMoneda.format(precioTotalSuma)%></td>
                                            <td style="text-align: left;color: #0000cc;"><%=formatMoneda.format(utilidadTotalSuma)%></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </form>

                           <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <jsp:param name="idReport" value="<%= identificadorMasMenosVendidos==0?ReportBO.PRODUCTOS_VENTAS_REPORT:identificadorMasMenosVendidos==1?ReportBO.PRODUCTOS_MAS_VENTAS_REPORT:identificadorMasMenosVendidos==2?ReportBO.PRODUCTOS_MENOS_VENTAS_REPORT:ReportBO.PRODUCTOS_VENTAS_REPORT%>" />
                                <jsp:param name="parametrosCustom" value="<%=filtroBusquedaEncoded %>" />
                                <jsp:param name="parametrosExtra" value="<%=parametrosExtraEncoded%>" />
                            </jsp:include>
                            <!-- FIN INCLUDE OPCIONES DE EXPORTACIÓN-->
                            
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