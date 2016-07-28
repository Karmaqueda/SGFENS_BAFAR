<%-- 
    Document   : catProductos_list
    Created on : 22/11/2012, 10:16:20 AM
    Author     : Leonardo
--%>

<%@page import="java.util.Arrays"%>
<%@page import="com.tsp.sct.bo.ConceptoBO.Ordenamiento"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.bo.ExistenciaAlmacenBO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
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
    String buscar_idmarca = request.getParameter("q_idmarca")!=null?request.getParameter("q_idmarca"):"";
    String buscar_idalmacen = request.getParameter("q_idalmacen")!=null?request.getParameter("q_idalmacen"):"";
    String buscar_idcategoria = request.getParameter("q_idcategoria")!=null?request.getParameter("q_idcategoria"):"";
    String buscar_idsubcategoria = request.getParameter("q_idsubcategoria")!=null?request.getParameter("q_idsubcategoria"):"";
    String buscar_isNivelMinimoStock = request.getParameter("q_nivel_min_stock")!=null?request.getParameter("q_nivel_min_stock"):"";
    String buscar_isMostrarSoloActivos = request.getParameter("inactivos")!=null?request.getParameter("inactivos"):"1";
    String buscar_materiaPrima = request.getParameter("q_materia_prima")!=null?request.getParameter("q_materia_prima"):"0";
    
    
    String filtroBusqueda = "";
    String parametrosPaginacion = "";  
        
    if (!buscar.trim().equals("")){
        
        //para buscar por nombre
        Encrypter encrypter = new Encrypter();
        String nombreBusquedaEncriptado = "";
        try {
            nombreBusquedaEncriptado = encrypter.encodeString2(buscar);
        }catch(Exception ex){}
        
        filtroBusqueda += " AND (DESCRIPCION LIKE '%" + buscar + "%' OR NUM_ARTICULOS_DISPONIBLES LIKE '%" +buscar+"%'"
                    + " OR NOMBRE LIKE '%" + nombreBusquedaEncriptado + "%' OR NOMBRE_DESENCRIPTADO LIKE '%" +buscar+"%' "
                    + " OR CLAVE LIKE '%" + buscar + "%' OR IDENTIFICACION LIKE '%" + buscar+"%')";
        
        if(!parametrosPaginacion.equals(""))
                        parametrosPaginacion+="&";
        parametrosPaginacion+="q="+buscar;
        
    }
    
    if (!buscar_idmarca.trim().equals("")){
        filtroBusqueda += " AND ID_MARCA='" + buscar_idmarca +"' ";
        if(!parametrosPaginacion.equals(""))
                        parametrosPaginacion+="&";
        parametrosPaginacion+="q_idmarca="+buscar_idmarca;
        
    }
    if (!buscar_idalmacen.trim().equals("")){
        filtroBusqueda += " AND ID_ALMACEN='" + buscar_idalmacen +"' ";
        
        if(!parametrosPaginacion.equals(""))
                        parametrosPaginacion+="&";
        parametrosPaginacion+="q_idalmacen="+buscar_idalmacen;
        
    }
    if (!buscar_idcategoria.trim().equals("")){
        filtroBusqueda += " AND ID_CATEGORIA='" + buscar_idcategoria +"' ";
        
        if(!parametrosPaginacion.equals(""))
                        parametrosPaginacion+="&";
        parametrosPaginacion+="q_idcategoria="+buscar_idcategoria;
    }
    if (!buscar_idsubcategoria.trim().equals("")){
        filtroBusqueda += " AND ID_SUBCATEGORIA='" + buscar_idsubcategoria +"' ";
        
        if(!parametrosPaginacion.equals(""))
                        parametrosPaginacion+="&";
        parametrosPaginacion+="q_idsubcategoria="+buscar_idsubcategoria;
    }
    if (buscar_isNivelMinimoStock.trim().equals("1")){
        filtroBusqueda += " AND NUM_ARTICULOS_DISPONIBLES <= STOCK_MINIMO ";
        
        if(!parametrosPaginacion.equals(""))
                        parametrosPaginacion+="&";
        parametrosPaginacion+="q_nivel_min_stock="+buscar_isNivelMinimoStock;
        
    }
    if (buscar_isMostrarSoloActivos.trim().equals("1")){
        filtroBusqueda += " AND ID_ESTATUS = 1 ";
        if(!parametrosPaginacion.equals(""))
                        parametrosPaginacion+="&";
        parametrosPaginacion+="inactivos="+buscar_isMostrarSoloActivos;
    }else{
        if(!parametrosPaginacion.equals(""))
                        parametrosPaginacion+="&";
        parametrosPaginacion+="inactivos="+buscar_isMostrarSoloActivos;
    }
    
    if (buscar_materiaPrima.trim().equals("1")){
        filtroBusqueda += " AND MATERIA_PRIMA = 1 ";
        
        if(!parametrosPaginacion.equals(""))
            parametrosPaginacion+="&";
        parametrosPaginacion+="q_materia_prima="+buscar_materiaPrima;
        
    }
    
    filtroBusqueda += " AND GENERICO!=1";
    
    int idConcepto = -1;
    try{ idConcepto = Integer.parseInt(request.getParameter("idConcepto")); }catch(NumberFormatException e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
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
    
     ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
     //Concepto[] conceptosDto = new Concepto[0];
     List<Concepto> conceptosDto = new ArrayList<Concepto>();
     try{
         if (user.getOrdenamientoConceptos().getSqlOrderBy()==null){
             // si no tiene una sentencia de Ordenamiento, usamos la de defecto
             user.setOrdenamientoConceptos(Ordenamiento.ALFABETICO_A_Z);
         }
         /*    
         if (user.getOrdenamientoConceptos()!=Ordenamiento.ALFABETICO_A_Z
                 && user.getOrdenamientoConceptos()!=Ordenamiento.ALFABETICO_Z_A
                 && user.getOrdenamientoConceptos().getSqlOrderBy()!=null){
         */
            
            filtroBusqueda += " ORDER BY " + user.getOrdenamientoConceptos().getSqlOrderBy();
            limiteRegistros = conceptoBO.findCantidadConceptos(idConcepto, idEmpresa , 0, 0, filtroBusqueda);

            if (!buscar.trim().equals(""))
                registrosPagina = limiteRegistros;

            paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

            if(paginaActual<0)
                paginaActual = 1;
            else if(paginaActual>paginasTotales)
                paginaActual = paginasTotales;

            Concepto[] conceptosDto2 = conceptoBO.findConceptos(idConcepto, idEmpresa,
                   ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                   filtroBusqueda);
            //conceptoBO.conceptoDesencriptarNombre(conceptosDto2);
            conceptosDto = Arrays.asList(conceptosDto2);
 
        /*
         }else{
            Concepto[] conceptosDto2 = new Concepto[0];
            conceptosDto2 = conceptoBO.findConceptos(idConcepto, idEmpresa , 0, 0, filtroBusqueda);
            limiteRegistros = conceptosDto2.length;

            if (!buscar.trim().equals(""))
                registrosPagina = limiteRegistros;

            paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

           if(paginaActual<0)
               paginaActual = 1;
           else if(paginaActual>paginasTotales)
               paginaActual = paginasTotales;

           //DESENCRIPTAMOS EL NOMBRE:
           conceptosDto2 = conceptoBO.conceptoDesencriptarNombre(conceptosDto2);            
           //ORDENAMOS LA LISTA:
           conceptosDto2 = conceptoBO.conceptoOrdenaListaEnBaseNombre(conceptosDto2);

           for(int x = ((paginaActual - 1) * (int)registrosPagina); x < ((paginaActual) * (int)registrosPagina); x++ ){            
               if (x<limiteRegistros)
                   conceptosDto.add(conceptosDto2[x]);
           }
         }
        */
     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catConceptos/catConceptos_form.jsp";
    String urlTo2 = "../catConceptos/catConceptos_express_form.jsp";
    String urlTo4 = "../catCodigoBarras/catCodigoBarras_form.jsp";
    String paramName = "idConcepto";
    //String parametrosPaginacion="";// "idEmpresa="+idEmpresa;
    String urlToMovimiento = "../catMovimientos/catMovimientos_form.jsp";
    String urlToAddConcepto = "addInventarioSesion_form.jsp";
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    
    //variable para mostrar la accion de crear descuento; este atributo viene del jsp "catConceptos_listCnDscto.jsp":
    int descuentoConcepto = 0;
    try{
        descuentoConcepto = Integer.parseInt(request.getParameter("descuentoConcepto"));
    }catch(Exception e){}
    parametrosPaginacion+= "&descuentoConcepto="+descuentoConcepto;
    
    int idVendedor = 0;
    try{
        idVendedor = Integer.parseInt(request.getParameter("idVendedor"));
    }catch(Exception e){}
    
    
    HttpSession sesion = request.getSession();
    List<Concepto> carrito = new ArrayList<Concepto>();
    
    if(session.getAttribute("carrito")!=null){
            carrito =(List<Concepto>)sesion.getAttribute("carrito");
    }
    int carSize = 0;
    try{ carSize = carrito.size();}catch(Exception e){ e.printStackTrace();}
     
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
            
            $(document).ready(function(){
                asignarFuncionCheckboxGeneral();
            });
            
            function inactiv(){               
                if($("#inactivos").attr("checked")){
                    $("#inactivos").val("2");
                }else{
                     $("#inactivos").val("1");
                }
            }
            
            
            function addToInventario(idConcepto){                
                 
                $.ajax({
                    type: "POST",
                    url: "catConceptos_ajax.jsp",
                    data: {mode: "addConceptoSesion" ,idConcepto: idConcepto},
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        var indice = datos.indexOf("--EXITO-->") + 10;
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#elementosCar").html("<a onclick=asignaInventario("+datos.substr(indice)+");>"
                             +"<span style='float: right; padding-right: 30px;'>"+datos.substr(indice)+" Concepto(s)</span>"
                             +"<span style='float: right;'><img  src='../../images/camion_icono_32.png' title='Asignar a Vendedor' class='help'/></span>"
                             +"</a>");               
                           //$.scrollTo('#inner',800);
                           $("#ajax_loading").fadeOut("slow");
                           //$("#ajax_message").fadeIn("slow");
                          apprise('<center><img src=../../images/info.png> <br/>'+ "Concepto añadido correctamente." +'</center>',{'animate':true},
                            function(r){                    
                            });
                       }else{
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").html(datos);
                           $("#ajax_message").fadeIn("slow");                           
                           $.scrollTo('#inner',800);
                           $("#ajax_message").fadeOut("slow");
                       }
                    }
                });                    
                
            }           
            
            function asignaInventario(noProds){
                
                if(noProds>0){
                    location.href = "addInventarioSesion_form.jsp";
                }
                else{
                    apprise('<center><img src=../../images/info.png> <br/>'+ "No se han agregado conceptos al Inventario." +'</center>',{'animate':true},
                            function(r){                    
                            });                    
                }
                
            }
            
            
            function confirmarDesactivarConcepto(idConcepto, nombreConcepto){
                apprise('¿Esta seguro que desea Desactivar el Concepto: ' + nombreConcepto + ' ?',{'verify':true, 'animate':true, 'textYes':'Si, Desactivar', 'textNo':'Cancelar'},
                    function(r){
                        if(r){
                            desactivarConcepto(idConcepto);
                        }
                    }
                );
            }
            
            function desactivarConcepto(id_concepto){
                $.ajax({
                    type: "POST",
                    url: "../catConceptos/catConceptos_ajax.jsp",
                    data: {mode: 'desactivarConcepto', idConcepto : id_concepto},
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
                           apprise('<center><img src=../../images/info.png> <br/>Concepto Desactivado Exitosamente</center>',{'animate':true},
                                function(r){
                                    location.reload();
                                }
                            );
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
            
            /**
            * Asigna una funcion para el checkbox maestro que hara
            * que un conjunto de checkbox se active o desactive
            */
            function asignarFuncionCheckboxGeneral(){
                //Checkbox
                $("input[name=check_master_id]").change(function(){
                    $('input[name=check_id]').each( function() {
                        if($("input[name=check_master_id]:checked").length === 1){
                            this.checked = true;
                        } else {
                            this.checked = false;
                        }
                    });
                });
            }
            
            /**
            * Funcion para invocar una acción para varios registros a la ves
            * Recorre todos los checkbox con nombre: check_id
            * y toma su valor para enviarlos como parametros GET separados por coma
            * a la JSP correspondiente
            */
            function muestraMultiplesCodigosBarra(){
                var query_string = '../../jsp/catCodigoBarras/catCodigoBarras_form.jsp?tipoCodigo=bar&idsConceptos=';

                var idString ="";

                $("input[name=check_id]").each(function(){
                    if(this.checked){
                        idString += this.value + ",";
                    }
                });
                if (idString!==""){
                    query_string += idString;

                    window.open(query_string);
                    
                }else{
                    apprise('No se selecciono ningun registro para generar su Codigo de Barras',{
                        'warning':true,
                        'animate':true
                    });
                }
            }
            
            function cambiarOrdenamiento(id_ordenamiento){
                $.ajax({
                    type: "POST",
                    url: "../catConceptos/catConceptos_ajax.jsp",
                    data: {mode: 'cambiarOrdenamiento', idOrdenamiento : id_ordenamiento},
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
                           //location.reload();
                           location.href = "catConceptos_list.jsp";
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
            
            
            function desencriptaActualizaNombreConceptos(){
                $.ajax({
                    type: "POST",
                    url: "../catConceptos/catConceptos_ajax.jsp",
                    data: { mode: 'desencriptarConceptos', todos : 'true' },
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
                           location.reload();
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
                            <div  id="elementosCar" >   
                            <a onclick="asignaInventario(<%=carSize%>);">
                                <span style="float: right; padding-right: 30px;" ><%=carSize%> Concepto(s)</span>
                                <span style="float: right;"><img  src="../../images/camion_icono_32.png" title="Asignar a Vendedor" class="help"/></span>                            
                            </a>
                            </div>
                        </div>
                        <br class="clear"/>
                        <div class="content" style="display: none;">
                            <form action="catConceptos_list.jsp" id="search_form_advance" name="search_form_advance" method="post">
                                
                                <div id="wrapper">
                                    <div class="column-1" style="width: 50%">
                                        <input type="hidden" id="descuentoConcepto" name="descuentoConcepto" value="<%=descuentoConcepto%>" />
                                        <p>
                                        <label>Marca</label><br/>
                                        <select id="q_idmarca" name="q_idmarca" class="flexselect">
                                            <option></option>
                                            <%= new MarcaBO(user.getConn()).getMarcasByIdHTMLCombo(idEmpresa, -1) %>
                                        </select>
                                        </p>

                                        <p>
                                        <label>Almacen</label><br/>
                                        <select id="q_idalmacen" name="q_idalmacen" class="flexselect">
                                            <option></option>
                                            <%= new AlmacenBO(user.getConn()).getAlmacenesByIdHTMLCombo(idEmpresa, -1) %>
                                        </select>
                                        </p>

                                        <p>
                                        <label>Categoria</label><br/>
                                        <select id="q_idcategoria" name="q_idcategoria" class="flexselect">
                                            <option></option>
                                            <%= new CategoriaBO(user.getConn()).getCategoriasByIdHTMLCombo(idEmpresa, -1,"") %>
                                        </select>
                                        </p>

                                        <p>
                                        <label>Subcategoria</label><br/>
                                        <select id="q_idsubcategoria" name="q_idsubcategoria" class="flexselect">
                                            <option></option>
                                            <%= new CategoriaBO(user.getConn()).getCategoriasByIdHTMLCombo(idEmpresa, -1,"") %>
                                        </select>
                                        </p>
                                        <br/>

                                        <p>
                                        <input id="q_nivel_min_stock" name="q_nivel_min_stock" type="checkbox" class="checkbox" value="1"> 
                                        <label for="q_nivel_min_stock">Nivel mínimo de stock</label>
                                        </p>
                                        <br/>    
                                        <p>
                                            <input type="checkbox" class="checkbox" id="inactivos" name="inactivos" value="1"  onchange="inactiv();"  <%= !buscar_isMostrarSoloActivos.equals("1")?"checked":"" %> > <label for="inactivos">Mostrar Inactivos</label>                                   
                                        </p>                                
                                        <br/>
                                        <p>
                                            <input type="checkbox" class="checkbox" id="q_materia_prima" name="q_materia_prima" value="1" <%= buscar_materiaPrima.equals("1")?"checked":"" %> > <label for="q_materia_prima">Materia Prima</label>                                   
                                        </p>                                
                                        <br/>
                                        
                                        <div id="action_buttons">
                                        <p>
                                            <input type="button" id="buscar" value="Buscar" onclick="$('#search_form_advance').submit();"/>
                                        </p>
                                        </div>
                                    </div>

                                    <div class="column-2" style="width: 50%">
                                        <p>
                                            <label for="ordenamiento">Ordenar por: </label><br/>
                                            <select id="ordenamiento" name="ordenamiento" onchange="cambiarOrdenamiento(this.value);">
                                                <option value="<%= Ordenamiento.ALFABETICO_A_Z.getId() %>" <%= user.getOrdenamientoConceptos()==Ordenamiento.ALFABETICO_A_Z?"selected":"" %> >Alfabetico A-Z</option>
                                                <option value="<%= Ordenamiento.ALFABETICO_Z_A.getId() %>" <%= user.getOrdenamientoConceptos()==Ordenamiento.ALFABETICO_Z_A?"selected":"" %>>Alfabetico Z-A</option>
                                                <option value="<%= Ordenamiento.CODIGO_MAYOR_MENOR.getId() %>" <%= user.getOrdenamientoConceptos()==Ordenamiento.CODIGO_MAYOR_MENOR?"selected":"" %>>Código Descendente</option>
                                                <option value="<%= Ordenamiento.ID_MAYOR_MENOR.getId() %>" <%= user.getOrdenamientoConceptos()==Ordenamiento.ID_MAYOR_MENOR?"selected":"" %> >ID Descendente</option>
                                                <option value="<%= Ordenamiento.FECHA_CREACION_MAS_RECIENTE.getId() %>" <%= user.getOrdenamientoConceptos()==Ordenamiento.FECHA_CREACION_MAS_RECIENTE?"selected":"" %> >Fecha creación producto Descendente</option>
                                            </select>
                                        </p>
                                        <br/>
                                    </div>
                                
                                </div>
                                
                            </form>
                        </div>
                    </div>
                    
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_producto.png" alt="icon"/>
                                Catálogo de Productos
                            </span>
                            <div class="switch" style="width:600px">
                                <table width="600px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td>
                                                <div id="search">
                                                <form action="catConceptos_list.jsp" id="search_form" name="search_form" method="get">
                                                        <input type="hidden" id="descuentoConcepto" name="descuentoConcepto" value="<%=descuentoConcepto%>" />
                                                        <input type="text" id="q" name="q" title="Buscar por Nombre/Descripción/Código/Clave" class="" style="width: 300px; float: left; "
                                                                value="<%=buscar%>"/>
                                                        <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                </form>
                                                </div>
                                            </td>
                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                            <%if(RolAutorizacionBO.permisoNuevoElemento(user.getUser().getIdRoles())){%>
                                            <td>
                                                <input type="button" id="nuevo" name="nuevo" class="right_switch expose" value="Nuevo Normal" 
                                                        style="float: right; width: 100px;" onclick="location.href='<%=urlTo%>'"/>
                                            </td>
                                            <td>
                                                <input type="button" id="nuevo" name="nuevo" class="right_switch expose" value="Nuevo Express" 
                                                        style="float: right; width: 100px;" onclick="location.href='<%=urlTo2%>'"/>
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
                                            <th><input type="checkbox" name="check_master_id" value="" /></th>
                                            <th>ID</th>
                                            <th>Código</th>
                                            <th>Nombre</th>
                                            <th>Marca</th>
                                            <th>Categoria</th>
                                            <th>Peso</th>
                                            <th>Stock</th>
                                            <th>Descripción</th> 
                                            <th>Con o Sin Impuesto</th>
                                            <th>Mat. Prima</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%     
                                            ConceptoBO concepto = new ConceptoBO(user.getConn());
                                            ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(user.getConn());      
                                            
                                            for (Concepto item:conceptosDto){
                                                double stockGral = 0;
                                                try{
                                                    Marca marcaDto = new MarcaBO(item.getIdMarca(),user.getConn()).getMarca();
                                                    Categoria categoriaDto = new CategoriaBO(item.getIdCategoria(),user.getConn()).getCategoria();
                                                    Categoria subcategoriaDto = new CategoriaBO(item.getIdSubcategoria(),user.getConn()).getCategoria();
                                                    stockGral = exisAlmBO.getExistenciaGeneralByEmpresaProducto(idEmpresa, item.getIdConcepto());
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%>>
                                            <td><input type="checkbox" name="check_id" id="check_id" value="<%= item.getIdConcepto()%>"/></td>
                                            <td><%=item.getIdConcepto() %></td>
                                            <td><%=item.getIdentificacion() %></td>
                                            <td><%=item.getNombreDesencriptado()%></td> <!--<td><%=item.getNombre() %></td>-->
                                            <td><!--[<%=item.getIdMarca() %>]--> <%= marcaDto!=null? marcaDto.getNombre():""%></td>
                                            <td><!--[<%=item.getIdCategoria() %>]--> <%= categoriaDto!=null? categoriaDto.getNombreCategoria():""%></td>
                                            <td><%= item.getPeso()>0? item.getPeso(): ""%></td>
                                            <td><%=stockGral%></td>
                                            <td><%=item.getDescripcion()%></td>    
                                            <td><%=(item.getImpuestoXConcepto()==0)?"Exento de Impuesto":"Con Impuesto" %></td>
                                            <td><%=(item.getMateriaPrima()==1)?"Si":"-" %></td>
                                            <td>
                                                <%if(RolAutorizacionBO.permisoAccionesElementoConceptoServicio(user.getUser().getIdRoles())
                                                        && item.getIdConceptoPadre()<=0){%>
                                                <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdConcepto()%>&acc=1&pagina=<%=paginaActual%>"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>
                                                &nbsp;&nbsp;
                                                <%  if (item.getIdEstatus()==1){ %>
                                                <a href="<%=urlToMovimiento%>?<%=paramName%>=<%=item.getIdConcepto()%>"><img src="../../images/icon_movimiento.png" alt="Nuevo Movimiento" class="help" title="Nuevo Movimiento"/></a>                                                
                                                &nbsp;&nbsp;                                               
                                                <%      if (item.getMateriaPrima()==0){ %>
                                                    <a class="" onclick="addToInventario(<%=item.getIdConcepto()%>);"><img src="../../images/anadir_prod_16.png" alt="Agregar a Camión" class="help" title="Agregar a Camión"/></a>                                               
                                                <% 
                                                        }
                                                } 
                                                %>
                                                <%if(descuentoConcepto == 1){%>
                                                    &nbsp;&nbsp;                                               
                                                    <a href="<%="../catConceptosOferta/catConceptos_formCnDscto.jsp"%>?<%=paramName%>=<%=item.getIdConcepto()%>&acc=1"><img src="../../images/icon_producto2.png" alt="Añadir Descuento" class="help" title="Añadir Descuento"/></a>
                                                <%}%>
                                                <!--<a href=""><img src="images/icon_delete.png" alt="delete" class="help" title="Delete"/></a>-->
                                                <%} else if (item.getIdConceptoPadre()>0){%>
                                                <i>-A Granel-</i>
                                                <%} %>
                                                <%  if (item.getIdEstatus()==1){ %>
                                                &nbsp;&nbsp;
                                                <a href="#" onclick="confirmarDesactivarConcepto('<%= item.getIdConcepto() %>', '<%= item.getNombre() %>');" ><img src="../../images/icon_delete.png" alt="eliminar" class="help" title="Desactivar"/></a>                                           
                                                <% } %>
                                                &nbsp;&nbsp;
                                                <% if (StringManage.getValidString(item.getIdentificacion()).length()>0){ %>
                                                <a href="<%=urlTo4%>?tipoCodigo=bar&<%=paramName%>=<%=item.getIdConcepto()%>&acc=CodigoBarras" target="_blank"><img src="../../images/icon_barras.png" alt="codigoBarras" class="help" title="Código Barras"/></a>
                                                <% } %>
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
                                    
                            <br><br>
                            
                            <center>
                                <a href="#" onclick="muestraMultiplesCodigosBarra()" id="idCodigoBarraMultiple" title="Codigos de Barra"
                                    style="font-size: 18px; font-variant: small-caps; font-weight: bolder;" >
                                    <img src="../../images/icon_barras.png" alt="Codigos de Barra" title="Códigos de Barra de Registros Seleccionados" class="help" />
                                    Códigos de Barra
                                </a>
                                
                                <span style="min-width: 30px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                
                                <a href="#" onclick="desencriptaActualizaNombreConceptos()" id="actualizaNombresConceptos" title="Actualizar Nombres Conceptos"
                                    style="font-size: 18px; font-variant: small-caps; font-weight: bolder;" >
                                    <img src="../../images/icon_producto.png" alt="Actualizar Nombres Conceptos" title="Actualizar Nombres Conceptos" class="help" />
                                    Actualizar Nombres Conceptos
                                </a>
                            </center>

                            <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <jsp:param name="idReport" value="<%= ReportBO.PRODUCTO_REPORT %>" />
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