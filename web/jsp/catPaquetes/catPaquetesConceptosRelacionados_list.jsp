<%-- 
    Document   : catPaquetesConceptosRelacionados_list
    Created on : 25/09/2014, 11:56:11 AM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.dao.dto.Paquete"%>
<%@page import="com.tsp.sct.bo.PaqueteBO"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.tsp.sct.dao.dto.PaqueteRelacionConcepto"%>
<%@page import="com.tsp.sct.bo.PaqueteRelacionConceptoBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    int idPaquete = -1;
    try{ idPaquete = Integer.parseInt(request.getParameter("idPaquete")); }catch(NumberFormatException e){}
    
    Paquete paqueteDto = null;
    try{
        PaqueteBO paqueteBO = new PaqueteBO(user.getConn());
        if(idPaquete > 0){
            paqueteDto = paqueteBO.findPaquetebyId(idPaquete);
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    
    String filtroBusqueda = " AND ID_PAQUETE = "+idPaquete + " AND ID_ESTATUS != 2 ";
    if (!buscar.trim().equals(""))
        filtroBusqueda = " AND (NOMBRE LIKE '%" + buscar + "%' OR DESCRIPCION LIKE '%" +buscar+"%')";
        
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
    
     PaqueteRelacionConceptoBO paqueteRelacionConceptoBO = new PaqueteRelacionConceptoBO(user.getConn());
     PaqueteRelacionConcepto[] paqueteRelacionConceptosDto = new PaqueteRelacionConcepto[0];
     PaqueteRelacionConcepto[] paqueteRelacionConceptosDto2 = new PaqueteRelacionConcepto[0];
     
     List<Concepto> conceptoAdd = new ArrayList<Concepto>();
     ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
     
     ArrayList<PaqueteRelacionConcepto> listaProducto = (ArrayList<PaqueteRelacionConcepto>)session.getAttribute("listaProducto");
     if(listaProducto == null){
        try{
            paqueteRelacionConceptosDto2 = paqueteRelacionConceptoBO.findPaqueteRelacionConceptos(0, 0 , 0, 0, filtroBusqueda);
            limiteRegistros = paqueteRelacionConceptosDto2.length;

            ////-CARGAMOS LOS IMPUESTOS PROPIOS:
            //Concepto[] conceptoAdd = new Concepto[0];

            Concepto concepto = null;
            for(PaqueteRelacionConcepto imp : paqueteRelacionConceptosDto2){
                concepto = new Concepto();
                concepto = conceptoBO.findConceptobyId(imp.getIdConcepto());
                concepto.setIdConcepto(imp.getIdPaqueteRelacionConcepto()); //sobreescribimos el ID para que sea el ID de la tabla de PAQUETE_RELACION_CONCEPTO
                concepto.setNumArticulosDisponibles(imp.getCantidad());
                concepto.setPrecio((float)imp.getPrecio());
                conceptoAdd.add(concepto);
            }
            ////-

            if (!buscar.trim().equals(""))
                registrosPagina = limiteRegistros;

            paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

           if(paginaActual<0)
               paginaActual = 1;
           else if(paginaActual>paginasTotales)
               paginaActual = paginasTotales;

           paqueteRelacionConceptosDto = paqueteRelacionConceptoBO.findPaqueteRelacionConceptos(0, idEmpresa,
                   ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                   filtroBusqueda);



        }catch(Exception ex){
            ex.printStackTrace();
        }
     
    }else{
         Concepto concepto = null;
         for(PaqueteRelacionConcepto imp : listaProducto){
                concepto = new Concepto();
                concepto = conceptoBO.findConceptobyId(imp.getIdConcepto());
                concepto.setIdConcepto(imp.getIdPaqueteRelacionConcepto()); //sobreescribimos el ID para que sea el ID de la tabla de PAQUETE_RELACION_CONCEPTO
                concepto.setNumArticulosDisponibles((int)imp.getCantidad());
                concepto.setPrecio((float)imp.getPrecio());
                conceptoAdd.add(concepto);
            }         
    }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catPaquetes/catPaquetesConceptosRelacionados_form.jsp";
    String paramName = "idPaqueteRelacionConcepto";
    String parametrosPaginacion="";// "idEmpresa="+idEmpresa;
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
            function eliminarConceptoRelacionado(idPaqueteRelacionConcepto, idPaquete){
                if (idPaqueteRelacionConcepto>0){
                    $.ajax({
                        type: "POST",
                        url: "catPaquetes_ajax.jsp",
                        data: {mode: '3', idPaqueteRelacionConcepto : idPaqueteRelacionConcepto, idPaquete : idPaquete},
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
                                            location.href = "catPaquetesConceptosRelacionados_list.jsp?idPaquete=<%=idPaquete%>";
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
    <body class="nobg">
            <!-- Inicio de Contenido -->           
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>
                    
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_producto.png" alt="icon"/>
                                Conceptos del Paquete  <%=paqueteDto!=null?paqueteDto.getNombre():""%>
                            </span>
                            <div class="switch" style="width:100px">
                                <table width="300px" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>                                                            
                                                            <td>
                                                                <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Agregar Concepto" 
                                                                       style="float: left; width: 100px;" onclick="location.href='<%=urlTo%>?idPaquete=<%=idPaquete%>'"/>
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
                                            <th>Cantidad</th>
                                            <th>Precio</th>                                           
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            for (Concepto item:conceptoAdd){
                                                try{
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%>>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdConcepto() %></td>
                                            <td><%=conceptoBO.desencripta(item.getNombre()) %></td>
                                            <td><%=item.getDescripcion()%></td>                                            
                                            <td><%=item.getNumArticulosDisponibles() %></td><!--Aqui esta bien se reusa obj Concepto -->  
                                            <td><%=item.getPrecio()%></td>
                                            <td>                                                
                                                <a href="#" onclick="eliminarConceptoRelacionado(<%=item.getIdConcepto()%>, <%=idPaquete%>);"><img src="../../images/icon_delete.png" alt="delete" class="help" title="Borrar"/></a>
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
                                <//jsp:param name="idReport" value="<//%= ReportBO.IMPUESTO_REPORT %>" />
                                <//jsp:param name="parametrosCustom" value="<//%= filtroBusquedaEncoded %>" />
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
            <!-- Fin de Contenido-->
       


    </body>
</html>
<%}%>