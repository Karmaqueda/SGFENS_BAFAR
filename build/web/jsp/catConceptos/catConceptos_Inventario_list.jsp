<%-- 
    Document   : catConceptos_Inventario_list.jsp
    Created on : 16/05/2014, 01:01:31 PM
    Author     : leonardo
--%>


<%@page import="com.tsp.sct.bo.ExistenciaAlmacenBO"%>
<%@page import="java.math.RoundingMode"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoInventarioRepartidor"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoInventarioRepartidorDaoImpl"%>
<%@page import="com.tsp.sct.bo.EmpleadoInventarioRepartidorBO"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
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
    String buscar_idvendedor = request.getParameter("q_idvendedor")!=null?request.getParameter("q_idvendedor"):"";
    String buscar_idalmacen = request.getParameter("q_idalmacen")!=null?request.getParameter("q_idalmacen"):"";
   
    String buscar_isMostrarSoloActivos = request.getParameter("q_mostrar_solo_activos")!=null?request.getParameter("q_mostrar_solo_activos"):"1";
    boolean buscar_agruparPorConceptoPadre = true; //(request.getParameter("q_agrupar_padre")!=null? (request.getParameter("q_agrupar_padre").equals("1")) : false);
    String filtroBusqueda = "";
    if (!buscar.trim().equals("")){
        
        //para buscar por nombre
        Encrypter encrypter = new Encrypter();
        String nombreBusquedaEncriptado = "";
        try {
            nombreBusquedaEncriptado = encrypter.encodeString2(buscar);
        }catch(Exception ex){}
        
        filtroBusqueda += " AND (DESCRIPCION LIKE '%" + buscar + "%' OR NUM_ARTICULOS_DISPONIBLES LIKE '%" +buscar+"%'"
                    + " OR NOMBRE LIKE '%" + nombreBusquedaEncriptado + "%' "
                    + ")";
    }
        
    if (!buscar_idalmacen.trim().equals("")){
        filtroBusqueda += " AND ID_ALMACEN='" + buscar_idalmacen +"' ";
    }   
    if (buscar_isMostrarSoloActivos.trim().equals("1")){
        filtroBusqueda += " AND ID_ESTATUS = 1 ";
    }
    if (buscar_agruparPorConceptoPadre){
        filtroBusqueda += " AND (ID_CONCEPTO_PADRE<=0 OR ID_CONCEPTO_PADRE IS NULL) ";
    }
    
    //buscamos los conceptos que tiene el empledo repartidor:   
    if(!buscar_idvendedor.trim().equals("")){       
        filtroBusqueda += " AND (";
        EmpleadoInventarioRepartidor[] inventario = new EmpleadoInventarioRepartidorDaoImpl(user.getConn()).findByDynamicWhere(" ID_EMPLEADO = "+buscar_idvendedor+" AND ID_ESTATUS = 1 ", null);
        int cont = 0;
        for(EmpleadoInventarioRepartidor inv : inventario){
            if(cont > 0)
                filtroBusqueda += " OR ";
            if (!buscar_agruparPorConceptoPadre){
                filtroBusqueda += " ID_CONCEPTO = "+inv.getIdConcepto();
            }else{
                Concepto concepto = new ConceptoBO(inv.getIdConcepto(), user.getConn()).getConcepto();
                if (concepto.getIdConceptoPadre()>0){
                    //concepto hijo
                    filtroBusqueda += " ID_CONCEPTO = "+concepto.getIdConceptoPadre();
                }else{
                    //concepto padre
                    filtroBusqueda += " ID_CONCEPTO = "+inv.getIdConcepto();
                }
            }
            cont++;
        }
        filtroBusqueda += " ) ";
    }
    
    
    filtroBusqueda += " AND GENERICO!=1";
    
    int idConcepto = -1;
    try{ idConcepto = Integer.parseInt(request.getParameter("idConcepto")); }catch(NumberFormatException e){}
    
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
    
     ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
     Concepto[] conceptosDto = new Concepto[0];
     try{
         limiteRegistros = conceptoBO.findConceptos(idConcepto, idEmpresa , 0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        conceptosDto = conceptoBO.findConceptos(idConcepto, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catConceptos/catConceptos_form.jsp";
    String paramName = "idConcepto";
    String parametrosPaginacion="q="+buscar+"&q_idvendedor="+buscar_idvendedor+"&q_idalmacen="+buscar_idalmacen;// "idEmpresa="+idEmpresa;
    String urlToMovimiento = "../catMovimientos/catMovimientos_form.jsp";
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    //AL ENVIO DE FILTRO DE PARAMETROS PARA EL REPORTE LE ENVIAMOS DE QUE REPARTIDOR FILTRAMOS DE SER QUE SE HAYA FILTRADO:
    /*if(!buscar_idvendedor.trim().equals("")){ 
        filtroBusquedaEncoded += "**"+buscar_idvendedor;
    }*/
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
            
        </script>

    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Catálogos</h1>
                    
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
                            <form action="catConceptos_Inventario_list.jsp" id="search_form_advance" name="search_form_advance" method="post">
                                <br/>
                                
                                <p>
                                <label>Vendedor</label><br/>
                                <select id="q_idvendedor" name="q_idvendedor" class="flexselect">
                                    <option></option>
                                    <%= new EmpleadoBO(user.getConn()).getEmpleadosByIdHTMLCombo(idEmpresa, -1, " AND REPARTIDOR = 1 ") %>
                                </select>
                                </p>
                                
                                <p>
                                <label>Almacen</label><br/>
                                <select id="q_idalmacen" name="q_idalmacen" class="flexselect">
                                    <option></option>
                                    <%= new AlmacenBO(user.getConn()).getAlmacenesByIdHTMLCombo(idEmpresa, -1) %>
                                </select>
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
                                Productos en Almacén
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td>
                                                <div id="search">
                                                <form action="catConceptos_Inventario_list.jsp" id="search_form" name="search_form" method="get">
                                                        <input type="text" id="q" name="q" title="Buscar por Nombre/Descripción/Mínimo Stock" class="" style="width: 300px; float: left; "
                                                                value="<%=buscar%>"/>
                                                        <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                </form>
                                                </div>
                                            </td>
                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                            <td>
                                                <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Crear Nuevo" 
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
                                    <%if(!buscar_idvendedor.trim().equals("")){ 
                                    Empleado empleador = new EmpleadoBO(Integer.parseInt(buscar_idvendedor), user.getConn()).getEmpleado(); %>
                                        Inventario de Repartidor: <%=empleador.getNombre()+" "+empleador.getApellidoPaterno()+" "+empleador.getApellidoMaterno()%>    
                                        <br/>
                                    <%}%>
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Código</th>
                                            <th>Nombre</th>
                                            <th>Stock en Almacenes</th>
                                            <th>Stock en Repartidores</th>
                                            <th>Stock Total</th>
                                            <th>Descripción</th>                                             
                                            
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%     
                                            ConceptoBO concepto = new ConceptoBO(user.getConn());
                                            ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(user.getConn());
                                            EmpleadoInventarioRepartidorBO empleadoInventarioRepartidorBO = new EmpleadoInventarioRepartidorBO(user.getConn());
                                            double almGral = 0;
                                            boolean esConceptoGranel = false;
                                            
                                            for (Concepto item:conceptosDto){
                                                try{                                                  
                                                    esConceptoGranel =  (item.getPrecioUnitarioGranel()>0 || item.getPrecioMedioGranel() > 0 || item.getPrecioMayoreoGranel() > 0 || item.getPrecioEspecialGranel() > 0);
                                                    
                                                    almGral = exisAlmBO.getExistenciaGeneralByEmpresaProducto(item.getIdEmpresa(), item.getIdConcepto());
                                                    
                                                    //OBTENERMOS EL NUMERO DE ARTICULOS QUE TIENEN LOS REPARTIDORES
                                                    double cantidadTotalArticulos = 0;
                                                    double cantidadTotalPesoRepartidores = 0;
                                                    String filtroBusquedaInventarioRepartidor = " AND (ID_CONCEPTO = "+item.getIdConcepto() ;
                                                    if (buscar_agruparPorConceptoPadre) //si esta activado Agrupar por Padre, entonces tambien debemos seleccionar los conceptos hijos para que la sumatoria total sea correcta
                                                        filtroBusquedaInventarioRepartidor += " OR ID_CONCEPTO IN (SELECT ID_CONCEPTO FROM CONCEPTO WHERE ID_CONCEPTO_PADRE="+ item.getIdConcepto()+ " ) ";
                                                    filtroBusquedaInventarioRepartidor += ")";
                                                    //EmpleadoInventarioRepartidor[] eir = new EmpleadoInventarioRepartidorDaoImpl(user.getConn()).findByDynamicWhere(" ID_CONCEPTO = "+item.getIdConcepto()+" AND ID_ESTATUS = 1 ";
                                                    EmpleadoInventarioRepartidor[] eir = empleadoInventarioRepartidorBO.findEmpleadoInventarioRepartidors(-1, -1, 0, 0, filtroBusquedaInventarioRepartidor);
                                                    for(EmpleadoInventarioRepartidor em: eir){
                                                        cantidadTotalArticulos += em.getCantidad();
                                                        cantidadTotalPesoRepartidores += em.getExistenciaGranel();
                                                    }
                                                    
                                                    BigDecimal numArticulosDisponibles = (new BigDecimal(almGral)).setScale(2, RoundingMode.HALF_UP);
                                                    BigDecimal cantidadTotalArticulosBigd = (new BigDecimal(cantidadTotalArticulos)).setScale(2, RoundingMode.HALF_UP);
                                                    BigDecimal stockTotal = numArticulosDisponibles.add(cantidadTotalArticulosBigd);
                                                    
                                                    //Datos para Granel
                                                    String unidadGranel = "Kg";
                                                    BigDecimal stockPesoAlmacenesDisponible = BigDecimal.ZERO;
                                                    BigDecimal stockPesoRepartidoresDisponible = BigDecimal.ZERO;
                                                    {
                                                        double stockPesoAlmacenes = exisAlmBO.getExistenciaPesoGeneralByEmpresaProducto(item.getIdEmpresa(), item.getIdConcepto());
                                                        stockPesoAlmacenesDisponible = (new BigDecimal(stockPesoAlmacenes)).setScale(2, RoundingMode.HALF_UP);
                                                        stockPesoRepartidoresDisponible = (new BigDecimal( cantidadTotalPesoRepartidores )).setScale(2, RoundingMode.HALF_UP);
                                                    }
                                                    BigDecimal stockPesoTotal = stockPesoAlmacenesDisponible.add(stockPesoRepartidoresDisponible);
                                                    //Fin Datos para Granel
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%>>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdConcepto() %></td>
                                            <td><%=item.getIdentificacion() %></td>
                                            <td><%=concepto.desencripta(item.getNombre()) %></td>                                            
                                            <td><%=numArticulosDisponibles + (esConceptoGranel?" ("+stockPesoAlmacenesDisponible + unidadGranel+")":"")%></td>
                                            <td><%=cantidadTotalArticulosBigd + (esConceptoGranel?" ("+stockPesoRepartidoresDisponible + unidadGranel+")":"") %></td>
                                            <td><%=stockTotal.doubleValue() + (esConceptoGranel?" ("+stockPesoTotal + unidadGranel+")":"")%></td>
                                            <td><%=item.getDescripcion()%></td>                                            
                                            
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
                            <jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <jsp:param name="idReport" value="<%= ReportBO.PRODUCTO_INVENTARIO_REPORT %>" />
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
                $("select.flexselect").flexselect();
            </script>
    </body>
</html>
<%}%>