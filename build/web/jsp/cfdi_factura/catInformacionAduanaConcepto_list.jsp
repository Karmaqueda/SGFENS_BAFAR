<%-- 
    Document   : catInformacionAduanaConcepto_list
    Created on : 10/02/2016, 01:16:27 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.tsp.sct.dao.dto.Aduana"%>
<%@page import="com.tsp.sgfens.sesion.ProductoSesion"%>
<%@page import="com.tsp.sgfens.sesion.ComprobanteFiscalSesion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="cfdiSesion" scope="session" class="com.tsp.sgfens.sesion.ComprobanteFiscalSesion"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String msgError="";
    
    if (cfdiSesion==null){
            cfdiSesion = new ComprobanteFiscalSesion();
    }
    int index_lista_producto=-1;
    try{ index_lista_producto = Integer.parseInt(request.getParameter("index_lista_producto")); }catch(Exception e){}
    if (index_lista_producto<0)
        msgError = "<ul>Producto inválido. Si persiste el problema intente recargar esta página.";

    List<Aduana> aduanas = new ArrayList<Aduana>();
    
    if (msgError.equals("")){
        try{
            aduanas = cfdiSesion.getListaProducto().get(index_lista_producto).getAduanas();
        }catch(Exception ex){
            msgError+="El producto especificado no se encontro en el listado en sesion. Intente de nuevo.";
        }
        //request.getSession().setAttribute("cfdiSesion", cfdiSesion);

    }
           
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Paginación
    */
    int paginaActual = 1;
    double registrosPagina = 50;
    double limiteRegistros = 0;
    int paginasTotales = 0;
    int numeroPaginasAMostrar = 5;

    try{
        paginaActual = Integer.parseInt(request.getParameter("pagina"));
    }catch(Exception e){}

    try{
        registrosPagina = Integer.parseInt(request.getParameter("registros_pagina"));
    }catch(Exception e){}
    
     /*AduanaBO aduanaBO = new AduanaBO(user.getConn());
     Aduana[] aduanasDto = new Aduana[0];
     Aduana[] aduanasDto2 = new Aduana[0];
     
     List<Impuesto> impuestoAdd = new ArrayList<Impuesto>();
     */
     try{
/*         aduanasDto2 = aduanaBO.findAduanas(0, idEmpresa , 0, 0, filtroBusqueda);
         limiteRegistros = aduanasDto2.length;
         
         ////-CARGAMOS LOS IMPUESTOS PROPIOS:
         //Impuesto[] impuestoAdd = new Impuesto[0];         
         ImpuestoBO impuestoBO = new ImpuestoBO(user.getConn());
         Impuesto impuesto = null;
         for(Aduana imp : aduanasDto2){             
             impuesto = new Impuesto();
             impuesto = impuestoBO.findImpuestobyId(imp.getIdImpuesto());
             impuestoAdd.add(impuesto);
         }
         ////-
*/         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

/*        aduanasDto = aduanaBO.findAduanas(0, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);
*/        
        

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "catInformacionAduanaConcepto_form.jsp";
    String paramName = "idAduana";
    String parametrosPaginacion="";// "idEmpresa="+idEmpresa;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode("", "UTF-8");
    
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
            function eliminarAduanaRelacionada(idAduana, index_lista_producto, mode){                
                //if (idAduana>0){
                    $.ajax({
                        type: "POST",
                        url: "catInformacionAduanaConcepto_ajax.jsp",
                        data: {mode: mode, idInfoAduana : idAduana, index_lista_producto : index_lista_producto},
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
                                            location.href = "catInformacionAduanaConcepto_list.jsp?index_lista_producto=<%=index_lista_producto%>";
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
                 //}
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
                                <img src="../../images/icon_bandera.png" alt="icon"/>
                                Aduanas del Concepto
                            </span>
                            <div class="switch" style="width:300px">
                                <table width="300px" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>                                                            
                                                            <td>
                                                                <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Agregar Aduana" 
                                                                       style="float: right; width: 100px;" onclick="location.href='<%=urlTo%>?index_lista_producto=<%=index_lista_producto%>'"/>
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
                                            <th>Nombre Aduana</th>
                                            <th>Número</th> 
                                            <th>Fecha</th>                                            
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            int indexAduana = 1;
                                            for (Aduana item: aduanas){
                                                try{
                                        %>
                                        <tr>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdAduana() %></td>
                                            <td><%=item.getAduana() %></td>
                                            <td><%=item.getNumDocumento() %></td>
                                            <td><%=DateManage.formatDateToNormal(item.getFechaExpedicion())%></td>
                                            
                                            <td>                                                
                                                <a href="#" onclick="eliminarAduanaRelacionada(<%=item.getIdAduana()>0?item.getIdAduana():indexAduana%>, <%=index_lista_producto%>, <%=item.getIdAduana()>0?"":"3"%>);"><img src="../../images/icon_delete.png" alt="delete" class="help" title="Borrar"/></a>
                                            </td>
                                        </tr>
                                        <%  indexAduana++;}catch(Exception ex){
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