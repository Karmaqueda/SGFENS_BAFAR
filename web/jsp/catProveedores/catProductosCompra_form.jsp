<%-- 
    Document   : catProductosCompra_form
    Created on : 11/08/2015, 05:46:18 PM
    Author     : leonardo
--%>

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
<%@page import="com.tsp.sct.bo.SGProductoServicioBO"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensProductoServicioDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProductoServicio"%>
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
        
        /*
         * Parámetros
         */
        int idSgfensProductoServicio = 0;
        try {
            idSgfensProductoServicio = Integer.parseInt(request.getParameter("idSgfensProductoServicio"));
        } catch (NumberFormatException e) {
        }

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
                
        SGProductoServicioBO sgfensProductoServicioBO = new SGProductoServicioBO(user.getConn());
        SgfensProductoServicio sgfensProductoServiciosDto = null;
        if (idSgfensProductoServicio > 0){
            sgfensProductoServicioBO = new SGProductoServicioBO(idSgfensProductoServicio,user.getConn());
            sgfensProductoServiciosDto = sgfensProductoServicioBO.getSgfensProductoServicio();
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
                        url: "catProductosCompra_ajax.jsp",
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
                                            location.href = "catProductosCompra_list.jsp?pagina="+"<%=paginaActual%>";
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
                $('#nombreImagenSgfensProductoServicio').val('' + nombreImagen);
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
                                    <% if(sgfensProductoServiciosDto!=null){%>
                                    Editar Producto / Servicio ID <%=sgfensProductoServiciosDto!=null?sgfensProductoServiciosDto.getIdProductoServicio():"" %>
                                    <%}else{%>
                                    Producto / Servicio
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idSgfensProductoServicio" name="idSgfensProductoServicio" value="<%=sgfensProductoServiciosDto!=null?sgfensProductoServiciosDto.getIdProductoServicio():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>*Nombre:</label><br/>
                                        <input maxlength="30" type="text" id="nombreSgfensProductoServicio" name="nombreSgfensProductoServicio" style="width:300px"
                                               value="<%=sgfensProductoServiciosDto!=null?sgfensProductoServicioBO.getSgfensProductoServicio().getNombre():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Descripción:</label><br/>
                                        <input maxlength="100" type="text" id="descripcion" name="descripcion" style="width:300px"
                                               value="<%=sgfensProductoServiciosDto!=null?sgfensProductoServicioBO.getSgfensProductoServicio().getDescripcion():"" %>"/>
                                    </p>
                                    <br/> 
                                    <p>
                                        <label>Unidad:</label><br/>
                                        <input maxlength="30" type="text" id="unidadSgfensProductoServicio" name="unidadSgfensProductoServicio" style="width:300px"
                                               value="<%=sgfensProductoServiciosDto!=null?sgfensProductoServicioBO.getSgfensProductoServicio().getUnidad():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Precio Menudeo:</label><br/>
                                        <input maxlength="30" type="text" id="precioMenudeoSgfensProductoServicio" name="precioMenudeoSgfensProductoServicio" style="width:300px"
                                               value="<%=sgfensProductoServiciosDto!=null?sgfensProductoServicioBO.getSgfensProductoServicio().getPrecioMenudeo():"0" %>"
                                               onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Menudeo:</label><br/>
                                        Hasta: <input maxlength="30" type="text" id="hastaMenudeoSgfensProductoServicio" name="hastaMenudeoSgfensProductoServicio" style="width:60px"
                                               value="<%=sgfensProductoServiciosDto!=null?sgfensProductoServicioBO.getSgfensProductoServicio().getMaxMenudeo():"0" %>"
                                               onkeypress="return validateNumber(event);"/> unidades.
                                    </p>
                                    <br/>                                    
                                    <p>
                                        <label>Precio Medio Mayoreo:</label><br/>
                                        <input maxlength="30" type="text" id="precioMedioMayoreoSgfensProductoServicio" name="precioMedioMayoreoSgfensProductoServicio" style="width:300px"
                                               value="<%=sgfensProductoServiciosDto!=null?sgfensProductoServicioBO.getSgfensProductoServicio().getPrecioMedioMayoreo():"0" %>"
                                               onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Medio Mayoreo:</label><br/>
                                        Desde: <input maxlength="30" type="text" id="desdeMedioMayoreoSgfensProductoServicio" name="desdeMedioMayoreoSgfensProductoServicio" style="width:60px"
                                               value="<%=sgfensProductoServiciosDto!=null?sgfensProductoServicioBO.getSgfensProductoServicio().getMinMedioMayoreo():"0" %>" 
                                               onkeypress="return validateNumber(event);"/> unidades.
                                        Hasta: <input maxlength="30" type="text" id="hastaMedioMayoreoSgfensProductoServicio" name="hastaMedioMayoreoSgfensProductoServicio" style="width:60px"
                                               value="<%=sgfensProductoServiciosDto!=null?sgfensProductoServicioBO.getSgfensProductoServicio().getMaxMedioMayoreo():"0" %>" 
                                               onkeypress="return validateNumber(event);"/> unidades.
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Precio Mayoreo:</label><br/>
                                        <input maxlength="30" type="text" id="precioMayoreoSgfensProductoServicio" name="precioMayoreoSgfensProductoServicio" style="width:300px"
                                               value="<%=sgfensProductoServiciosDto!=null?sgfensProductoServicioBO.getSgfensProductoServicio().getPrecioMayoreo():"0" %>"
                                               onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Medio Mayoreo:</label><br/>
                                        Desde: <input maxlength="30" type="text" id="desdeMayoreoSgfensProductoServicio" name="desdeMayoreoSgfensProductoServicio" style="width:60px"
                                               value="<%=sgfensProductoServiciosDto!=null?sgfensProductoServicioBO.getSgfensProductoServicio().getMinMayoreo():"0" %>" 
                                               onkeypress="return validateNumber(event);"/> unidades.                                        
                                    </p>
                                    <br/>                                    
                                    <p>
                                        <label>Categoría:</label><br/>
                                        <select size="1" id="idCategoriaSgfensProductoServicio" name="idCategoriaSgfensProductoServicio" >
                                            <option value="-1" >Selecciona una categoría</option>
                                                <%
                                                    out.print(new SGProveedorCategoriaBO(user.getConn()).getSgfensProveedorCategoriasByIdHTMLCombo(idEmpresa, (sgfensProductoServiciosDto!=null?sgfensProductoServicioBO.getSgfensProductoServicio().getIdCategoria():-1) ));                                                     
                                                %>
                                        </select>                                        
                                    </p>  
                                    <br/>
                                    
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=sgfensProductoServiciosDto!=null?(sgfensProductoServiciosDto.getIdEstatus()==1?"checked":""):"checked" %> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
                                    </p>
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
                                    Imagen Del Prodcuto                                    
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">                                   
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />                                                                                                       
                                    
                                    <%
                                        //Si se esta en modo para agregar un nuevo registro se muestra el upload
                                        //if (mode.equals("") || (sgfensProductoServiciosDto!=null && sgfensProductoServiciosDto.getNombreImagenProductoServicio()!=null && sgfensProductoServiciosDto.getNombreImagenProductoServicio().trim().equals(""))){
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
                                    <% //} %>
                                    
                                    <p>
                                        <label>*Nombre Archivo:</label><br/>
                                        <input maxlength="30" type="text" id="nombreImagenSgfensProductoServicio" name="nombreImagenSgfensProductoServicio" style="width:300px"
                                               readonly value="<%=sgfensProductoServiciosDto!=null?sgfensProductoServiciosDto.getNombreImagenProductoServicio():"" %>"/>
                                    </p>
                                    <br/>
                                    
                                    <% if (( empresaDto!=null && sgfensProductoServiciosDto!=null && sgfensProductoServiciosDto.getNombreImagenProductoServicio()!=null && !sgfensProductoServiciosDto.getNombreImagenProductoServicio().trim().equals(""))   ) {
                                        String rutaArchivoImagenPersonal = appConfig.getApp_content_path() + empresaDto.getRfc() + "/" +"/ImagenConcepto/" + sgfensProductoServiciosDto.getNombreImagenProductoServicio();
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


    </body>
</html>
<%}%>