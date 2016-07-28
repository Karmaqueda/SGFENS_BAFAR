<%-- 
    Document   : 
    Created on : 18/02/2016, 12:01:41 PM
    Author     : ISCesarMartinez
--%>

<%@page import="com.tsp.sct.bo.AlmacenBO"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpAsignacionInventarioDetalleDaoImpl"%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.EmpAsignacionInventarioDetalle"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String filtroBusqueda = "";
    //if (!buscar.trim().equals(""))
    //    filtroBusqueda = " AND (NOMBRE LIKE '%" + buscar + "%' OR DESCRIPCION LIKE '%" +buscar+"%')";
    
    int idAsignacionInventario = -1;
    try{ idAsignacionInventario = Integer.parseInt(request.getParameter("idAsignacionInventario")); }catch(NumberFormatException e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    filtroBusqueda += " id_asignacion_inventario = " + idAsignacionInventario;
    
    /*
    * Paginación
    */
    
     EmpAsignacionInventarioDetalleDaoImpl empAsignacionInventarioDetalleDao = new EmpAsignacionInventarioDetalleDaoImpl(user.getConn());
     EmpAsignacionInventarioDetalle[] empAsignacionInventarioDetallesDto = new EmpAsignacionInventarioDetalle[0];
     try{

        empAsignacionInventarioDetallesDto = empAsignacionInventarioDetalleDao.findByDynamicWhere(filtroBusqueda, null);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    //String urlTo = "../catEmpAsignacionInventarioDetalles/catEmpAsignacionInventarioDetalles_form.jsp";
    //String paramName = "idEmpAsignacionInventarioDetalle";
    String parametrosPaginacion="";// "idEmpresa="+idEmpresa;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    
    ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
    AlmacenBO almacenBO = new AlmacenBO(user.getConn());
    
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
                    <h1>Almácen</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_producto.png" alt="icon"/>
                                Detalle de Asignación de Inventario ID <%= idAsignacionInventario %>
                            </span>
                            <div class="switch" style="width:500px">
                            </div>
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <form id="form_data" name="form_data" action="" method="post">
                                <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th>ID Detalle</th>
                                            <th>Producto</th>
                                            <th>Cantidad</th> 
                                            <th>Almacen Origen</th>
                                            <!--<th>Acciones</th>-->
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            for (EmpAsignacionInventarioDetalle item : empAsignacionInventarioDetallesDto){
                                                try{
                                                    String nombreConcepto = conceptoBO.findConceptobyId(item.getIdConcepto()).getNombreDesencriptado();
                                                    String nombreAlmacen =  almacenBO.findAlmacenbyId(item.getIdAlmacen()).getNombre();
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%>>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdAsignacionInventarioDetalle()%></td>
                                            <td><%= nombreConcepto %></td>
                                            <td><%= item.getCantidad() %></td>                                            
                                            <td><%= nombreAlmacen %></td>
                                            <!--
                                            <td>
                                                <%if(RolAutorizacionBO.permisoAccionesElemento(user.getUser().getIdRoles())){%>
                                                
                                                <%}%>
                                            </td>
                                            -->
                                        </tr>
                                        <%      }catch(Exception ex){
                                                    ex.printStackTrace();
                                                }
                                            } 
                                        %>
                                    </tbody>
                                </table>
                            </form>
                            
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