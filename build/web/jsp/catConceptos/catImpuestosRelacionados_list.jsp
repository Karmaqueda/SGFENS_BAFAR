<%-- 
    Document   : catImpuestosRelacionados_list
    Created on : 4/04/2014, 12:22:41 PM
    Author     : leonardo
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.tsp.sct.bo.ImpuestoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.ImpuestoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Impuesto"%>
<%@page import="com.tsp.sct.dao.dto.ImpuestoPorConcepto"%>
<%@page import="com.tsp.sct.bo.ImpuestoPorConceptoBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    int idConcepto = -1;
    try{ idConcepto = Integer.parseInt(request.getParameter("idConcepto")); }catch(NumberFormatException e){}
    
    String filtroBusqueda = " AND ID_CONCEPTO = "+idConcepto + " AND ID_ESTATUS != 2 ";
    if (!buscar.trim().equals(""))
        filtroBusqueda = " AND (NOMBRE LIKE '%" + buscar + "%' OR DESCRIPCION LIKE '%" +buscar+"%')";
        
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
    
     ImpuestoPorConceptoBO impuestoPorConceptoBO = new ImpuestoPorConceptoBO(user.getConn());
     ImpuestoPorConcepto[] impuestoPorConceptosDto = new ImpuestoPorConcepto[0];
     ImpuestoPorConcepto[] impuestoPorConceptosDto2 = new ImpuestoPorConcepto[0];
     
     List<Impuesto> impuestoAdd = new ArrayList<Impuesto>();
     
     try{
         impuestoPorConceptosDto2 = impuestoPorConceptoBO.findImpuestoPorConceptos(0, idEmpresa , 0, 0, filtroBusqueda);
         limiteRegistros = impuestoPorConceptosDto2.length;
         
         ////-CARGAMOS LOS IMPUESTOS PROPIOS:
         //Impuesto[] impuestoAdd = new Impuesto[0];         
         ImpuestoBO impuestoBO = new ImpuestoBO(user.getConn());
         Impuesto impuesto = null;
         for(ImpuestoPorConcepto imp : impuestoPorConceptosDto2){             
             impuesto = new Impuesto();
             impuesto = impuestoBO.findImpuestobyId(imp.getIdImpuesto());
             impuestoAdd.add(impuesto);
         }
         ////-
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        impuestoPorConceptosDto = impuestoPorConceptoBO.findImpuestoPorConceptos(0, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);
        
        

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catConceptos/catImpuestoRelacionado_form.jsp";
    String paramName = "idImpuestoPorConcepto";
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
            function eliminarImpuestoRelacionado(idImpuesto, idConcepto){
                if (idImpuesto>0){
                    $.ajax({
                        type: "POST",
                        url: "catConceptos_ajax.jsp",
                        data: {mode: '3', idImpuesto : idImpuesto, idConcepto : idConcepto},
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
                                            location.href = "catImpuestosRelacionados_list.jsp?idConcepto=<%=idConcepto%>";
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
                                <img src="../../images/icon_impuesto.png" alt="icon"/>
                                Impuestos del Concepto
                            </span>
                            <div class="switch" style="width:300px">
                                <table width="300px" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>                                                            
                                                            <td>
                                                                <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Agregar Impuesto" 
                                                                       style="float: right; width: 100px;" onclick="location.href='<%=urlTo%>?idConcepto=<%=idConcepto%>'"/>
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
                                            <th>Porcentaje</th>
                                            <th>Trasladado/Retenido</th>
                                            <th>Federal/Local</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            for (Impuesto item:impuestoAdd){
                                                try{
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%>>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdImpuesto() %></td>
                                            <td><%=item.getNombre() %></td>
                                            <td><%=item.getDescripcion()%></td>                                            
                                            <td><%=item.getPorcentaje() %></td>
                                            <td><%=item.getTrasladado() == (short)1? "Trasladado" : "Retenido" %></td>
                                            <td><%=item.getImpuestoLocal() ==(short)1? "Local" : "Federal" %></td>
                                            <td>                                                
                                                <a href="#" onclick="eliminarImpuestoRelacionado(<%=item.getIdImpuesto()%>, <%=idConcepto%>);"><img src="../../images/icon_delete.png" alt="delete" class="help" title="Borrar"/></a>
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