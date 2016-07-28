<%-- 
    Document   : catProveedorProductos_form
    Created on : 17/08/2015, 06:23:18 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.bo.SGProductoServicioBO"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.bo.SGProveedorCategoriaBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensClienteVendedor"%>
<%@page import="com.tsp.sct.bo.SGClienteVendedorBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.bo.PasswordBO"%>
<%@page import="com.tsp.sct.bo.SGProveedorProductoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensProveedorProductoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProveedorProducto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el cliente tiene acceso a este topico
    if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
        response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
        response.flushBuffer();
    } else {
        
        
        int paginaActual = 1;
        try{
            paginaActual = Integer.parseInt(request.getParameter("pagina"));
        }catch(Exception e){}

        int idEmpresa = user.getUser().getIdEmpresa();
        
        int idProveedor = -1;
        try{
           idProveedor = Integer.parseInt(request.getParameter("idProveedor"));
       }catch(Exception e){}

        /*
         * Parámetros
         */
        int idSgfensProveedorProducto = 0;
        try {
            idSgfensProveedorProducto = Integer.parseInt(request.getParameter("idSgfensProveedorProducto"));
        } catch (NumberFormatException e) {
        }

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
                
        SGProveedorProductoBO sgfensProveedorProductoBO = new SGProveedorProductoBO(user.getConn());
        SgfensProveedorProducto sgfensProveedorProductosDto = null;
        if (idSgfensProveedorProducto > 0){
            sgfensProveedorProductoBO = new SGProveedorProductoBO(idSgfensProveedorProducto,user.getConn());
            sgfensProveedorProductosDto = sgfensProveedorProductoBO.getSgfensProveedorProducto();
        }
        
        Empresa empresaDto = null;        
        empresaDto = new EmpresaBO(idEmpresa,user.getConn()).getEmpresa();
        
        
        Configuration appConfig = new Configuration();
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
            
            function grabar(){
                if(validar()){
                    $.ajax({
                        type: "POST",
                        url: "catProveedorProductos_ajax.jsp",
                        data: $("#frm_action").serialize(),
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
                                            location.href = "catProveedorProductos_list.jsp?idProveedor="+"<%=idProveedor%>";
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

            function validar(){
                /*
                if(jQuery.trim($("#nombre").val())==""){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "nombre de contacto" es requerido</center>',{'animate':true});
                    $("#nombre_contacto").focus();
                    return false;
                }
                */
                return true;
            }
            
            function recuperarNombreArchivoImagen(nombreImagen){
                $('#nombreImagenSgfensProveedorProducto').val('' + nombreImagen);
            }
            
            function cargaInfoConcepto(idConceptoSeleccionado){
                $.ajax({
                    type: "POST",
                    url: "catProveedorProductos_ajax.jsp",
                    data: { mode: 'cargaDatosConcepto' , idConceptoSeleccionado : idConceptoSeleccionado},
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<center><img src="../../images/ajax_loader2.gif" alt="Cargando.." /><br/>Procesando...</center>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           //$("#ajax_loading").html(datos);
                           $("#ajax_loading").fadeOut("slow");
                           $("#action_buttons").fadeIn("slow");
                           recuperarListados();
                           $('#descuento_tasa').val(0);
                       }else{
                           $("#ajax_loading").html(datos);
                           $("#action_buttons").fadeIn("slow");
                       }
                    }
                });
            }
            
            function mostrarCalendario() {
                $("#fecha_caducidad").datepicker({
                    minDate: +1,
                    gotoCurrent: true,
                    changeMonth: true,
                    beforeShow: function(input, datepicker) {
                        setTimeout(function() {
                            $(datepicker.dpDiv).css('zIndex', 100);
                        }, 500)
                    }
                });
                $("#fecha_disponibilidad").datepicker({
                    minDate: +1,
                    gotoCurrent: true,
                    changeMonth: true,
                    beforeShow: function(input, datepicker) {
                        setTimeout(function() {
                            $(datepicker.dpDiv).css('zIndex', 100);
                        }, 500)
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
                    <h1>Proveedor</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_producto.png" alt="icon"/>
                                    <% if(sgfensProveedorProductosDto!=null){%>
                                    Editar Producto / Servicio ID <%=sgfensProveedorProductosDto!=null?sgfensProveedorProductosDto.getIdProveedorProducto():"" %> del Proveedor con ID <%=idProveedor%>
                                    <%}else{%>
                                    Producto / Servicio del Proveedor con ID <%=idProveedor%>
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idSgfensProveedorProducto" name="idSgfensProveedorProducto" value="<%=sgfensProveedorProductosDto!=null?sgfensProveedorProductosDto.getIdProveedorProducto():"" %>" />
                                    <input type="hidden" id="idProveedor" name="idProveedor" value="<%=idProveedor%>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>*Nombre:</label><br/>
                                        <!--<input maxlength="30" type="text" id="nombreSgfensProveedorProducto" name="nombreSgfensProveedorProducto" style="width:300px"
                                               value="<//%=sgfensProveedorProductosDto!=null?sgfensProveedorProductoBO.getSgfensProveedorProducto().getNombre():"" %>"/>-->
                                        <select size="1" id="idConceptoSeleccionado" name="idConceptoSeleccionado" onchange="cargaInfoConcepto(this.value);">
                                            <option value="-1">Selecciona un producto/servicio</option>
                                                <%
                                                    out.print(new SGProductoServicioBO(user.getConn()).getSgfensProductoServiciosByIdHTMLCombo(idEmpresa, (sgfensProveedorProductosDto!=null?sgfensProveedorProductoBO.getSgfensProveedorProducto().getIdSgfensProductoServicio():-1) ));                                                     
                                                %>
                                        </select>
                                    </p>
                                    <br/>
                                    <!--<p>
                                        <label>*Descripción:</label><br/>
                                        <input maxlength="100" type="text" id="descripcion" name="descripcion" style="width:300px"
                                               value="<//%=sgfensProveedorProductosDto!=null?sgfensProveedorProductoBO.getSgfensProveedorProducto().getDescripcion():"" %>"/>
                                    </p>
                                    <br/>--> 
                                    <p>
                                        <label>Unidad:</label><br/>
                                        <input maxlength="30" type="text" id="unidadSgfensProveedorProducto" name="unidadSgfensProveedorProducto" style="width:300px"
                                               value="<%=sgfensProveedorProductosDto!=null?sgfensProveedorProductoBO.getSgfensProveedorProducto().getUnidad():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Precio Menudeo:</label><br/>
                                        <input maxlength="30" type="text" id="precioMenudeoSgfensProveedorProducto" name="precioMenudeoSgfensProveedorProducto" style="width:300px"
                                               value="<%=sgfensProveedorProductosDto!=null?sgfensProveedorProductoBO.getSgfensProveedorProducto().getPrecioMenudeo():"0" %>"
                                               onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Menudeo:</label><br/>
                                        Hasta: <input maxlength="30" type="text" id="hastaMenudeoSgfensProveedorProducto" name="hastaMenudeoSgfensProveedorProducto" style="width:60px"
                                               value="<%=sgfensProveedorProductosDto!=null?sgfensProveedorProductoBO.getSgfensProveedorProducto().getMaxMenudeo():"0" %>"
                                               onkeypress="return validateNumber(event);"/> unidades.
                                    </p>
                                    <br/>                                    
                                    <p>
                                        <label>Precio Medio Mayoreo:</label><br/>
                                        <input maxlength="30" type="text" id="precioMedioMayoreoSgfensProveedorProducto" name="precioMedioMayoreoSgfensProveedorProducto" style="width:300px"
                                               value="<%=sgfensProveedorProductosDto!=null?sgfensProveedorProductoBO.getSgfensProveedorProducto().getPrecioMedioMayoreo():"0" %>"
                                               onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Medio Mayoreo:</label><br/>
                                        Desde: <input maxlength="30" type="text" id="desdeMedioMayoreoSgfensProveedorProducto" name="desdeMedioMayoreoSgfensProveedorProducto" style="width:60px"
                                               value="<%=sgfensProveedorProductosDto!=null?sgfensProveedorProductoBO.getSgfensProveedorProducto().getMinMedioMayoreo():"0" %>" 
                                               onkeypress="return validateNumber(event);"/> unidades.
                                        Hasta: <input maxlength="30" type="text" id="hastaMedioMayoreoSgfensProveedorProducto" name="hastaMedioMayoreoSgfensProveedorProducto" style="width:60px"
                                               value="<%=sgfensProveedorProductosDto!=null?sgfensProveedorProductoBO.getSgfensProveedorProducto().getMaxMedioMayoreo():"0" %>" 
                                               onkeypress="return validateNumber(event);"/> unidades.
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Precio Mayoreo:</label><br/>
                                        <input maxlength="30" type="text" id="precioMayoreoSgfensProveedorProducto" name="precioMayoreoSgfensProveedorProducto" style="width:300px"
                                               value="<%=sgfensProveedorProductosDto!=null?sgfensProveedorProductoBO.getSgfensProveedorProducto().getPrecioMayoreo():"0" %>"
                                               onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Medio Mayoreo:</label><br/>
                                        Desde: <input maxlength="30" type="text" id="desdeMayoreoSgfensProveedorProducto" name="desdeMayoreoSgfensProveedorProducto" style="width:60px"
                                               value="<%=sgfensProveedorProductosDto!=null?sgfensProveedorProductoBO.getSgfensProveedorProducto().getMinMayoreo():"0" %>" 
                                               onkeypress="return validateNumber(event);"/> unidades.                                        
                                    </p>
                                    <br/>                                    
                                    <p>
                                        <label>Categoría:</label><br/>
                                        <select size="1" id="idCategoriaSgfensProveedorProducto" name="idCategoriaSgfensProveedorProducto" >
                                            <option value="-1" >Selecciona una categoría</option>
                                                <%
                                                    out.print(new SGProveedorCategoriaBO(user.getConn()).getSgfensProveedorCategoriasByIdHTMLCombo(idEmpresa, (sgfensProveedorProductosDto!=null?sgfensProveedorProductoBO.getSgfensProveedorProducto().getIdCategoria():-1) ));                                                     
                                                %>
                                        </select>                                        
                                    </p>  
                                    <br/>
                                    <p>
                                        <label>Fecha Caducidad:</label><br/>
                                        <input type="text" name="fecha_caducidad" id="fecha_caducidad" size="30" readonly
                                               value="<%=sgfensProveedorProductosDto != null ? DateManage.formatDateToNormal(sgfensProveedorProductoBO.getSgfensProveedorProducto().getCaducidad()) : ""%>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Volumen Disponible:</label><br/>
                                        <input maxlength="30" type="text" id="volumenSgfensProveedorProducto" name="volumenSgfensProveedorProducto" style="width:300px"
                                               value="<%=sgfensProveedorProductosDto!=null?sgfensProveedorProductoBO.getSgfensProveedorProducto().getVolumenDisponible():"0" %>"
                                               onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Fecha Disponibilidad:</label><br/>
                                        <input type="text" name="fecha_disponibilidad" id="fecha_disponibilidad" size="30" readonly
                                               value="<%=sgfensProveedorProductosDto != null ? DateManage.formatDateToNormal(sgfensProveedorProductoBO.getSgfensProveedorProducto().getFechaDisponibilidad()) : ""%>"/>
                                    </p>
                                    <br/>
                                    <br/><br/>
                                    
                                    <div id="action_buttons">
                                        <p>
                                            <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
                                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                        </p>
                                    </div>
                                    
                            </div>
                        </div>
                        <!-- End left column window -->
                        <div class="column_right">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_imagenPersonal.png" alt="icon"/>                                    
                                    Imagen Del Producto                                    
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">                                   
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />                                                                                                       
                                    
                                    <%
                                        //Si se esta en modo para agregar un nuevo registro se muestra el upload
                                        if (mode.equals("")){
                                    %>
                                    <p>
                                        <label>Subir Archivo (.jpg)</label><br/>
                                        <iframe src="../file/uploadSingleForm.jsp?id=archivoImagen&validate=jpg&queryCustom=isImagenConceptoAdjunto=1" 
                                                id="iframe_archivos_jpg" 
                                                name="iframe_archivos_jpg" 
                                                style="border: none" scrolling="no"
                                                height="80" width="400">
                                            <p>Tu navegador no soporta iframes y son necesarios para el buen funcionamiento del aplicativo</p>
                                        </iframe>
                                    </p>
                                    <% } %>
                                    
                                    <p>
                                        <label>*Nombre Archivo:</label><br/>
                                        <input maxlength="30" type="text" id="nombreImagenSgfensProveedorProducto" name="nombreImagenSgfensProveedorProducto" style="width:300px"
                                               readonly value="<%=sgfensProveedorProductosDto!=null?sgfensProveedorProductosDto.getNombreImagenProductoServicio():"" %>"/>
                                    </p>
                                    <br/>
                                    
                                    <% if (sgfensProveedorProductosDto!=null && empresaDto!=null) {
                                        String rutaArchivoImagenPersonal = appConfig.getApp_content_path() + empresaDto.getRfc() +"/ImagenConcepto/" + sgfensProveedorProductosDto.getNombreImagenProductoServicio();
                                        String rutaArchivoImgEncoded = java.net.URLEncoder.encode(rutaArchivoImagenPersonal, "UTF-8");
                                    %>
                                    <p>
                                        <label>Imagen</label>
                                        <div style="display: inline" >
                                            <a href="../file/download.jsp?ruta_archivo=<%=rutaArchivoImgEncoded%>">Descargar</a>
                                        </div>
                                    </p>
                                    <%}%>
                                    
                                    <br/>
                                    
                                    
                            </div>
                        </div>
                        
                        
                    </div>
                    </form>
                    <!--TODO EL CONTENIDO VA AQUÍ-->

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>

        <script>
            mostrarCalendario();
        </script>
    </body>
</html>
<%}%>