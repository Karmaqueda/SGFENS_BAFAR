<%-- 
    Document   : catEmpleados_Inventario_list
    Created on : 14/05/2014, 11:25:42 AM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.ConceptoBO.Ordenamiento"%>
<%@page import="com.tsp.sct.dao.jdbc.InventarioInicialVendedorDaoImpl"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.bo.InventarioInicialVendedorBO"%>
<%@page import="com.tsp.sct.dao.dto.InventarioInicialVendedor"%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.util.Converter"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoInventarioRepartidor"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.tsp.sct.bo.EmpleadoInventarioRepartidorBO"%>
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
    
    
    

    //para buscar por nombre
    Encrypter encrypter = new Encrypter();
    String nombreBusquedaEncriptado = "";
    try {
        nombreBusquedaEncriptado = encrypter.encodeString2(buscar);
    }catch(Exception ex){}

    
    String filtroBusqueda = " AND empleado_inventario_repartidor.ID_EMPLEADO = "+idEmpleado + " AND empleado_inventario_repartidor.ID_ESTATUS != 2 ";
    if (!buscar.trim().equals("")){
        ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
        filtroBusqueda = " AND (empleado_inventario_repartidor.ID_CONCEPTO like '%" + buscar + "%' OR (empleado_inventario_repartidor.ID_CONCEPTO IN (SELECT ID_CONCEPTO FROM CONCEPTO WHERE NOMBRE_DESENCRIPTADO LIKE '%"+buscar+"%' OR DESCRIPCION LIKE '%"+buscar+"%')))";
    }
    
    
    
        
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
    
     EmpleadoInventarioRepartidorBO empleadoInventarioRepartidorBO = new EmpleadoInventarioRepartidorBO(user.getConn());
     EmpleadoInventarioRepartidor[] empleadoInventarioRepartidorDto = new EmpleadoInventarioRepartidor[0];
     
     List<Concepto> conceptoAdd = new ArrayList<Concepto>();
     
     try{
         if (user.getOrdenamientoConceptos().getSqlOrderByCortes()==null){
             // si no tiene una sentencia de Ordenamiento para Cortes, usamos la de defecto
             user.setOrdenamientoConceptos(ConceptoBO.Ordenamiento.CANTIDAD_ASIGNADA_MAYOR_MENOR);
         }
                 
         limiteRegistros = empleadoInventarioRepartidorBO.findCantidadEmpleadoInventarioRepartidors(0, idEmpleado , 0, 0, filtroBusqueda);
                  
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        // modificado 29-04-2016 para permitir ordenamiento personalizado
        /*
        empleadoInventarioRepartidorDto = empleadoInventarioRepartidorBO.findEmpleadoInventarioRepartidors(0, idEmpleado,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);
        */
        empleadoInventarioRepartidorDto = empleadoInventarioRepartidorBO.findEmpleadoInventarioRepartidorsOrderBy(0, idEmpleado,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda, user.getOrdenamientoConceptos().getSqlOrderByCortes());
        ////-CARGAMOS LOS CONCEPTOS PROPIOS:
         
         ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
         Concepto concepto = null;
         for(EmpleadoInventarioRepartidor inventario : empleadoInventarioRepartidorDto){
             concepto = new Concepto();
             concepto = conceptoBO.findConceptobyId(inventario.getIdConcepto());
             concepto.setNumArticulosDisponibles(inventario.getCantidad());//se reusa obj
             concepto.setPeso(inventario.getPeso());//Aqui colocamos el Peso Unitario (por pieza)
             concepto.setVolumen(inventario.getExistenciaGranel());//aquí colocamos el Peso Total Neto Disponible (stock)
             conceptoAdd.add(concepto);
         }
         ////-
        

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     
     //Obtenemos empleado
     Empleado empleadoDto = new Empleado();
     try{
         EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());     
         empleadoDto = empleadoBO.findEmpleadobyId(idEmpleado);
     }catch(Exception e){} 
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catEmpleados/catEmpleados_Inventario_form.jsp";
    String urlTo2 = "../catEmpleados/catEmpleados_InventarioGranel_form.jsp";
    String paramName = "idEmpleadoInventarioRepartidor";
    String parametrosPaginacion="idEmpleado="+idEmpleado;// "idEmpresa="+idEmpresa;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    String strParamsExtra  =""+idEmpleado ;
    String parametrosExtraEncoded = java.net.URLEncoder.encode(strParamsExtra , "UTF-8");
    String infoTitle  ="Empleado: " + empleadoDto.getNombre() +" "+ empleadoDto.getApellidoPaterno() +" "+ empleadoDto.getApellidoMaterno();
    String infoTitleEncoded = java.net.URLEncoder.encode(infoTitle , "UTF-8");
    
    
    //recuperamos de sesion si estamos en modo Adicion a Inventario Inicial
    boolean modoAdicionInventarioInicial = session.getAttribute("modoAdicionInventarioInicial")!=null? (Boolean) session.getAttribute("modoAdicionInventarioInicial") : false;
    InventarioInicialVendedor inventarioInicialVendedorAux = null;
    if (idEmpleado>0){
        InventarioInicialVendedor[] inventarioInicialVendedor = new InventarioInicialVendedorDaoImpl(user.getConn()).findByDynamicWhere(" ID_EMPLEADO=" + idEmpleado + " ORDER BY FECHA_HR_ULT_ADICION DESC, fecha_registro DESC  ", null);
        if (inventarioInicialVendedor.length>0)
            inventarioInicialVendedorAux = inventarioInicialVendedor[0];
    }
    
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
            function eliminarInventarioRepartidorRelacionado(idConcepto, idEmpleado){
                if (idConcepto>0){
                    $.ajax({
                        type: "POST",
                        url: "catEmpleados_ajax.jsp",
                        data: {mode: 'quitarInventario', idConcepto : idConcepto, idEmpleado : idEmpleado},
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
                                            location.href = "catEmpleados_Inventario_list.jsp?idEmpleado=<%=idEmpleado%>";
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
            
            
            
            function invInicial(idEmpleado){
                if (idEmpleado>0){
                    $.ajax({
                        type: "POST",
                        url: "catEmpleados_ajax.jsp",
                        data: {mode: 'inventarioInicial', idEmpleado : idEmpleado},
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
                                            location.href = "catEmpleados_Inventario_list.jsp?idEmpleado=<%=idEmpleado%>";
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
            
            function activarDesactivarAdicionInvInicial(idEmpleado, modo){
                var modoAdicion = 'activarAdicionInvInicial';
                if (modo<=0){
                    modoAdicion = 'desactivarAdicionInvInicial'
                }
                if (idEmpleado>0){
                    $.ajax({
                        type: "POST",
                        url: "catEmpleados_ajax.jsp",
                        data: {mode: modoAdicion, idEmpleado : idEmpleado},
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
                                            location.href = "catEmpleados_Inventario_list.jsp?idEmpleado=<%=idEmpleado%>";
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
                    <h1>Pretoriano Móvil</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>
                    
                    <% if (modoAdicionInventarioInicial){ %>
                    <div id="info_extra" class="alert_info" style="display: block;">
                        <img src="../../images/inventario_agregar_32.png" alt="Adición a Inventario Inicial" />
                        &nbsp;&nbsp;Modo Adición a Inventario Inicial. 
                        <br/>De click en 'Agregar Concepto' o en 'Modificar' alguno de la lista.
                    </div>
                    <% } %>
                    
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                Ordenamiento
                            </span> 
                        </div>
                        <br class="clear"/>
                        <div class="content" style="display: none;">
                            
                            <p>
                                <label for="ordenamiento">Ordenar por: </label><br/>
                                <select id="ordenamiento" name="ordenamiento" onchange="cambiarOrdenamiento(this.value);">
                                    <option value="<%= Ordenamiento.CANTIDAD_ASIGNADA_MAYOR_MENOR.getId() %>" <%= user.getOrdenamientoConceptos()==Ordenamiento.CANTIDAD_ASIGNADA_MAYOR_MENOR?"selected":"" %> >Cantidad Asignada Descendente</option>
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
         
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/camion_icono_16.png" alt="icon"/> Inventario del Repartidor &nbsp;&nbsp;&nbsp;&nbsp;<%=empleadoDto.getNombre() +" "+ empleadoDto.getApellidoPaterno() +" "+ empleadoDto.getApellidoMaterno() %>
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>     
                                            <td>
                                                <div id="search">

                                                <form action="catEmpleados_Inventario_list.jsp" id="search_form" name="search_form" method="get">                                                                                                                                               
                                                        <input type="text" id="q" name="q" title="Buscar por ID/Nombre/Descripción" class="" style="width: 200px; float: left; "
                                                               value="<%=buscar%>"/>           
                                                        <input type="hidden" id="idEmpleado" name="idEmpleado" value="<%=idEmpleado%>"/> 
                                                        <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                </form>
                                                </div>
                                            </td>
                                            <td>
                                                <input type="button" id="nuevo" name="nuevo" class="right_switch expose" value="Agregar Concepto" 
                                                       style="float: right; width: 110px;" onclick="location.href='<%=urlTo%>?idEmpleado=<%=idEmpleado%>'"/>
                                            </td>
                                            <td>
                                                <input type="button" id="nuevo2" name="nuevo2" class="right_switch expose" value="Venta a Granel" 
                                                       style="float: right; width: 110px;" onclick="location.href='<%=urlTo2%>?idEmpleado=<%=idEmpleado%>'"/>
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
                                            <th>Descripción</th> 
                                            <th>Código</th>
                                            <th>Cantidad Disponible</th>                                            
                                            <th>Peso Neto</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                        Encrypter encripDesencri = new Encrypter();
                                            for (Concepto item : conceptoAdd){
                                                try{
                                                    String unidadGranel = "Kg";
                                                    //String datoAdicionalProductoGranel = item.getPeso()>0 ? (" "+item.getPeso() + " " + unidadGranel): "";
                                        %>
                                        <tr <%//=(item.getIdEstatus()!=1)?"class='inactive'":""%>>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdConcepto() %></td>                                            
                                            <td><%=encripDesencri.decodeString(item.getNombre()) %></td>
                                            <td><%=item.getDescripcion()%></td>   
                                            <td><%=item.getIdentificacion()!=null?!item.getIdentificacion().equals("null")?item.getIdentificacion():"":"" %></td>
                                            <td><%=item.getNumArticulosDisponibles()%></td><!--Aqui esta bien se reusa obj Concepto -->                                            
                                            <td><%= item.getPeso()>0?""+Converter.doubleToStringFormatMexico(item.getVolumen())+unidadGranel:"-" %></td><!--Aqui esta bien se reusan algunos atributos del obj Concepto -->
                                            <td>                                                
                                                <a href="#" onclick="eliminarInventarioRepartidorRelacionado(<%=item.getIdConcepto()%>, <%=idEmpleado%>);"><img src="../../images/icon_delete.png" alt="delete" class="help" title="Borrar"/></a>
                                                &nbsp;&nbsp;
                                                <% if(item.getPeso() <= 0 || item.getIdConceptoPadre()<=0){%>
                                                    <a href="#" onclick="location.href='<%=urlTo%>?idEmpleado=<%=idEmpleado%>&idConcepto=<%=item.getIdConcepto()%>'"><img src="../../images/icon_edit.png" alt="modificar" class="help" title="Modificar"/></a>
                                                <%}else{%>
                                                    <a href="#" onclick="location.href='<%=urlTo2%>?idEmpleado=<%=idEmpleado%>&idConcepto=<%=item.getIdConcepto()%>'"><img src="../../images/icon_edit.png" alt="modificar" class="help" title="Modificar"/></a>
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
                                    
                            <div id="action_buttons">
                                <br/>
                                <p> 
                                    <% if (inventarioInicialVendedorAux!=null){ %>
                                    <span style="float: left;">
                                        Inicio de Inventario Inicial: <%= DateManage.formatDateTimeToNormalMinutes(inventarioInicialVendedorAux.getFechaRegistro()) %>
                                        <br/>
                                        Última Adición: <%= StringManage.getValidString(DateManage.formatDateTimeToNormalMinutes(inventarioInicialVendedorAux.getFechaHrUltAdicion())) %>
                                    </span>
                                    <% } %>
                                    
                                    <button type="button" id="inv" value="Inventario Inicial" style="float:right;" onclick="invInicial(<%=idEmpleado%>);"><img src="../../images/inventario_32.png" alt="Registrar Inventario" /><br>Inventario Inicial</button>
                                    
                                    <% if (inventarioInicialVendedorAux!=null && !modoAdicionInventarioInicial){ %>
                                    <span style="float: right; min-width: 15px;">&nbsp;</span>
                                    <button type="button" id="inv" value="Adición a Inv. Inicial" style="float:right;" onclick="activarDesactivarAdicionInvInicial(<%=idEmpleado%>,1);"><img src="../../images/inventario_agregar_32.png" alt="Adición a Inventario Inicial" /><br>Adición a Inv. Inicial</button>
                                    <% } %>
                                    
                                    <% if (inventarioInicialVendedorAux!=null && modoAdicionInventarioInicial){ %>
                                    <span style="float: right; min-width: 15px;">&nbsp;</span>
                                    <button type="button" id="inv" value="Concluir Adición a Inv. Inicial" style="float:right;" onclick="activarDesactivarAdicionInvInicial(<%=idEmpleado%>,0);"><img src="../../images/icon_ok_hand.png" alt="Concluir Adición a Inventario Inicial" /><br>Concluir Adición a Inv. Inicial</button>
                                    <% } %>
                                    
                                </p>
                                <br/><br/><br/>
                            </div>          
                                    
                                    
                                    
                            <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <jsp:param name="idReport" value="<%= ReportBO.EMPLEADO_INVENTARIO_REPORT %>" />
                                <jsp:param name="parametrosCustom" value="<%= filtroBusquedaEncoded %>" />
                                <jsp:param name="parametrosExtra" value="<%=parametrosExtraEncoded%>" />
                                <jsp:param name="infoTitle" value="<%=infoTitleEncoded%>" />
                            </jsp:include>
                            <!-- FIN INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <input type="button" id="regresar" value="Regresar" onclick="location.href = 'catEmpleados_list.jsp' "/>
                            
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
       


    </body>
</html>
<%}%>