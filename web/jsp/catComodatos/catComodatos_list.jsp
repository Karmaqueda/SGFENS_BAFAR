<%-- 
    Document   : catComodatos_list
    Created on : 3/03/2016, 04:08:54 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.dao.jdbc.AlmacenDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Almacen"%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.Comodato"%>
<%@page import="com.tsp.sct.bo.ComodatoBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String filtroBusqueda = " AND ESTATUS != 3 ";
    if (!buscar.trim().equals(""))
        filtroBusqueda = " AND (NOMBRE LIKE '%" + buscar + "%' OR NUMERO_SERIE LIKE '%" +buscar+"%' OR MARCA LIKE '%" +buscar+"%')";
    
    int idComodato = -1;
    try{ idComodato = Integer.parseInt(request.getParameter("idComodato")); }catch(NumberFormatException e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    
    //busqueda avanzada:
    String parametrosPaginacion="";
    String estadoNuevoReuso= request.getParameter("estadoNuevoReuso")!=null? new String(request.getParameter("estadoNuevoReuso").getBytes("ISO-8859-1"),"UTF-8") :"";

    String estatusComodato = request.getParameter("estatusComodato")!=null? new String(request.getParameter("estatusComodato").getBytes("ISO-8859-1"),"UTF-8") :"";
    String estatusDisponible = request.getParameter("estatusDisponible")!=null? new String(request.getParameter("estatusDisponible").getBytes("ISO-8859-1"),"UTF-8") :"";
    String estatusInactivo = request.getParameter("estatusInactivo")!=null? new String(request.getParameter("estatusInactivo").getBytes("ISO-8859-1"),"UTF-8") :"";
    String estatusDescompuesto = request.getParameter("estatusDescompuesto")!=null? new String(request.getParameter("estatusDescompuesto").getBytes("ISO-8859-1"),"UTF-8") :"";
    
    if (!estadoNuevoReuso.trim().equals("")){       
        filtroBusqueda += " AND ESTADO = " + estadoNuevoReuso + " ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="estadoNuevoReuso="+estadoNuevoReuso;
    }
    
    String filtroBusquedaEstatus = "";
    
    if (estatusComodato.trim().equals("1")){
        if(!filtroBusquedaEstatus.trim().equals("")){
            filtroBusquedaEstatus += " OR ";
        }
        filtroBusquedaEstatus += " ESTATUS = " + estatusComodato + " ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="estatusComodato="+estatusComodato;
    }
    if (estatusDisponible.trim().equals("2")){
        if(!filtroBusquedaEstatus.trim().equals("")){
            filtroBusquedaEstatus += " OR ";
        }
        filtroBusquedaEstatus += " ESTATUS = " + estatusDisponible + " ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="estatusDisponible="+estatusDisponible;
    }
    if (estatusInactivo.trim().equals("3")){
        if(!filtroBusquedaEstatus.trim().equals("")){
            filtroBusquedaEstatus += " OR ";
        }
        filtroBusquedaEstatus += " ESTATUS = " + estatusInactivo + " ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="estatusInactivo="+estatusInactivo;
    }
    if (estatusDescompuesto.trim().equals("4")){
        if(!filtroBusquedaEstatus.trim().equals("")){
            filtroBusquedaEstatus += " OR ";
        }
        filtroBusquedaEstatus += " ESTATUS = " + estatusDescompuesto + " ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="estatusDescompuesto="+estatusDescompuesto;
    }
    
    
    if(!filtroBusquedaEstatus.trim().equals("")){
        filtroBusqueda += " AND ( " + filtroBusquedaEstatus + " ) ";
    }
    
    /*
    * Paginación
    */
    int paginaActual = 1;
    double registrosPagina = 20;
    double limiteRegistros = 0;
    int paginasTotales = 0;
    int numeroPaginasAMostrar = 5;

    try{
        paginaActual = Integer.parseInt(request.getParameter("pagina"));
    }catch(Exception e){}

    try{
        registrosPagina = Integer.parseInt(request.getParameter("registros_pagina"));
    }catch(Exception e){}
    
     ComodatoBO comodatoBO = new ComodatoBO(user.getConn());
     Comodato[] comodatosDto = new Comodato[0];
     try{
         limiteRegistros = comodatoBO.findComodatos(idComodato, idEmpresa , 0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        comodatosDto = comodatoBO.findComodatos(idComodato, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catComodatos/catComodatos_form.jsp";
    String urlTo2 = "../catComodatos/catComodatosAsignarCliente_form.jsp";
    String urlTo3 = "../catComodatos/catComodatosMantenimiento_list.jsp";
    String urlTo4 = "../catComodatos/catComodatosContrato_form.jsp";
    String urlTo5 = "../catComodatos/catComodatosProductos_list.jsp";
    String urlTo6 = "../catComodatos/catComodatosInformacion_form.jsp";
    
    String paramName = "idComodato";
    //String parametrosPaginacion="";// "idEmpresa="+idEmpresa;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    
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
            
            function confirmarEliminarComodato(idComodato){
                apprise('¿Esta seguro que desea eliminar el Comodato?', {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, 
                    function(r){
                        if(r){
                            // Usuario dio click 'Yes'
                            confirmarEliminar(idComodato);
                        }
                });
                
            }
            
            function confirmarEliminar(idComodato){
                if(idComodato>=0){
                    $.ajax({
                        type: "POST",
                        url: "catComodatos_ajax.jsp",
                        data: { mode: 'eliminarComodato', idComodato : idComodato },
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
                               apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                    function(r){
                                        location.href = "catComodatos_list.jsp";
                                });
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
                    <h1>Comodato</h1>
                    
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
                            <form action="catComodatos_list.jsp" id="search_form_advance" name="search_form_advance" method="post">
                                
                                <p>
                                    <label>Estado:</label><br/>
                                    <input type="radio" class="checkbox" id="estadoNuevo" name="estadoNuevoReuso" value="1" > <label for="estadoNuevo">Nuevo</label>
                                    <input type="radio" class="checkbox" id="estadoReuso" name="estadoNuevoReuso" value="2" > <label for="estadoReuso">Re-uso</label>                                                   
                                </p>    
                                <br/>
                                <p>
                                    <label>Estatus:</label><br/>
                                    <input type="checkbox" class="checkbox" id="estatusComodato" name="estatusComodato" value="1" > <label for="estatusComodato">Comodato</label>
                                    <input type="checkbox" class="checkbox" id="estatusDisponible" name="estatusDisponible" value="2" > <label for="estatusDisponible">Disponible</label>
                                    <!--<input type="checkbox" class="checkbox" id="estatusInactivo" name="estatusInactivo" value="3" > <label for="estatusInactivo">Inactivo</label>-->
                                    <input type="checkbox" class="checkbox" id="estatusDescompuesto" name="estatusDescompuesto" value="4" > <label for="estatusDescompuesto">Descompuesto</label>
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
                                <img src="../../images/icon_comodato.png" alt="icon"/>
                                Comodatos
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
                                                            <td>
                                                                <div id="search">
                                                                <form action="catComodatos_list.jsp" id="search_form" name="search_form" method="get">
                                                                        <input type="text" id="q" name="q" title="Buscar por Nombre/Serial/Marca" class="" style="width: 300px; float: left; "
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
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Nombre</th>
                                            <th>Número de Serie</th>
                                            <th>Marca</th>
                                            <th>Modelo</th>
                                            <th>Tipo</th>
                                            <th>Capacidad</th>
                                            <th>Almacén</th>
                                            <th>Estatus</th>                                           
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            Almacen almacen = null;
                                            AlmacenDaoImpl almacenDaoImpl = new AlmacenDaoImpl(user.getConn());
                                            String nombreAlamcen = "";
                                            for (Comodato item:comodatosDto){
                                                almacen = null;
                                                nombreAlamcen = "";
                                                try{
                                                    almacen = almacenDaoImpl.findByPrimaryKey(item.getIdAlmacen());
                                                    nombreAlamcen = almacen.getNombre();
                                                }catch(Exception e){}
                                                try{
                                        %>
                                        <tr>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdComodato() %></td>
                                            <td><%=item.getNombre() %></td>
                                            <td><%=item.getNumeroSerie()%></td>
                                            <td><%=item.getMarca()%></td>
                                            <td><%=item.getModelo()%></td>
                                            <td><%=item.getTipo()%></td>
                                            <td><%=item.getCapacidad()%></td>
                                            <td><%=nombreAlamcen%></td>
                                            <td><%=item.getEstatus()==1?"Comodato":item.getEstatus()==2?"Disponible":item.getEstatus()==3?"Inactivo":item.getEstatus()==4?"Descompuesto":"No Definido"%></td>
                                            <td>
                                                <%if(RolAutorizacionBO.permisoAccionesElemento(user.getUser().getIdRoles())){%>
                                                <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdComodato()%>&acc=1&pagina=<%=paginaActual%>"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>
                                                &nbsp;&nbsp;
                                                <a href="<%=urlTo2%>?<%=paramName%>=<%=item.getIdComodato()%>&acc=asignar&pagina=<%=paginaActual%>"><img src="../../images/icon_comodatoCliente.png" alt="asignarCliente" class="help" title="Asignar Cliente"/></a>
                                                &nbsp;&nbsp;
                                                <a href="<%=urlTo3%>?<%=paramName%>=<%=item.getIdComodato()%>&acc=mantenimiento&pagina=<%=paginaActual%>"><img src="../../images/icon_comodatoMantenimiento.png" alt="agregarMantenimiento" class="help" title="Agregar Mantenimiento"/></a>
                                                &nbsp;&nbsp;
                                                <a href="<%=urlTo5%>?<%=paramName%>=<%=item.getIdComodato()%>&acc=producto&pagina=<%=paginaActual%>"><img src="../../images/icon_comodatoProducto.png" alt="agregarMantenimiento" class="help" title="Producto Relacionado"/></a>
                                                &nbsp;&nbsp;
                                                <a href="<%=urlTo4%>?<%=paramName%>=<%=item.getIdComodato()%>&acc=contrato&pagina=<%=paginaActual%>"><img src="../../images/icon_comodatoContrato.png" alt="agregarContrato" class="help" title="Contrato"/></a>
                                                &nbsp;&nbsp;
                                                <a href="#" onclick="confirmarEliminarComodato(<%=item.getIdComodato()%>);"><img src="../../images/icon_delete.png" alt="delete" class="help" title="Delete"/></a>
                                                &nbsp;&nbsp;
                                                <a href="<%=urlTo6%>?<%=paramName%>=<%=item.getIdComodato()%>&acc=1&pagina=<%=paginaActual%>"><img src="../../images/icon_comodato.png" alt="ver" class="help" title="Ver"/></a>
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
                            <jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <jsp:param name="idReport" value="<%= ReportBO.COMODATO_REPORT %>" />
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


    </body>
</html>
<%}%>