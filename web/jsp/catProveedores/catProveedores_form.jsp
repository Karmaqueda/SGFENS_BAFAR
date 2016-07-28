<%-- 
    Document   : catProveedors_form.jsp
    Created on : 26-oct-2012, 12:13:49
    Author     : ISCesarMartinez poseidon24@hotmail.com
--%>

<%@page import="com.tsp.sct.bo.SGProveedorCategoriaBO"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProveedor"%>
<%@page import="com.tsp.sct.bo.SGProveedorBO"%>
<%@page import="com.tsp.sct.bo.PasswordBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.dao.jdbc.UsuariosDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Usuarios"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
//Verifica si el proveedor tiene acceso a este topico
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
        int idProveedor = 0;
        try {
            idProveedor = Integer.parseInt(request.getParameter("idProveedor"));
        } catch (NumberFormatException e) {
        }

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        
        SGProveedorBO proveedorBO = new SGProveedorBO(user.getConn());
        SgfensProveedor proveedorDto = null;
        if (idProveedor > 0){
            proveedorBO = new SGProveedorBO(idProveedor,user.getConn());
            proveedorDto = proveedorBO.getSgfensProveedor();
        }
        
        EmpresaBO empresaBO = new EmpresaBO(user.getConn());
        EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa());     
    
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
                        url: "catProveedores_ajax.jsp",
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
                                            location.href = "catProveedores_list.jsp?pagina="+"<%=paginaActual%>";
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

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_proveedor.png" alt="icon"/>
                                    <% if(proveedorDto!=null){%>
                                    Editar Proveedor ID <%=proveedorDto!=null?proveedorDto.getIdProveedor():"" %>
                                    <%}else{%>
                                    Proveedor
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idProveedor" name="idProveedor" value="<%=proveedorDto!=null?proveedorDto.getIdProveedor():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>*RFC<%=empresaPermisoAplicacionDto.getRfcPorNipCodigo() == 1?"/NIP":empresaPermisoAplicacionDto.getRfcPorNipCodigo() == 2?"/RUC":""%>:</label><br/>
                                        <input maxlength="13" type="text" id="rfc" name="rfc" style="width:300px"
                                               value="<%=proveedorDto!=null?proveedorBO.getSgfensProveedor().getRfc():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Razón Social:</label><br/>
                                        <input maxlength="200" type="text" id="razonSocial" name="razonSocial" style="width:300px"
                                               value="<%=proveedorDto!=null?proveedorBO.getSgfensProveedor().getRazonSocial():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Número interno Proveedor:</label><br/>
                                        <input maxlength="15" type="text" id="numeroProveedor" name="numeroProveedor" style="width:300px"
                                               value="<%=proveedorDto!=null?proveedorBO.getSgfensProveedor().getNumeroProveedor():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Descripción (p. ej.: productos mas comprados):</label><br/>
                                        <textarea rows="5" cols="35" id="descripcion" name="descripcion"><%=proveedorDto!=null?proveedorBO.getSgfensProveedor().getDescripcion():"" %></textarea>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Contacto (Nombre completo):</label><br/>
                                        <input maxlength="100" type="text" id="contacto" name="contacto" style="width:300px"
                                               value="<%=proveedorDto!=null?proveedorBO.getSgfensProveedor().getContacto():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Lada - *Teléfono:</label><br/>
                                        <input maxlength="3" type="text" id="lada" name="lada" style="width:25px"
                                               value="<%=proveedorDto!=null?proveedorBO.getSgfensProveedor().getLada():"" %>"
                                               onkeypress="return validateNumber(event);"/> -
                                        <input maxlength="8" type="text" id="telefono" name="telefono" style="width:255px"
                                               value="<%=proveedorDto!=null?proveedorBO.getSgfensProveedor().getTelefono():"" %>"
                                               onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Extensión:</label><br/>
                                        <input maxlength="5" type="text" id="extension" name="extension" style="width:40px"
                                               value="<%=proveedorDto!=null?proveedorBO.getSgfensProveedor().getExtension():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Celular:</label><br/>
                                        <input maxlength="11" type="text" id="celular" name="celular" style="width:300px"
                                               value="<%=proveedorDto!=null?proveedorBO.getSgfensProveedor().getCelular():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Correo Electrónico:</label><br/>
                                        <input maxlength="100" type="text" id="email" name="email" style="width:300px"
                                               value="<%=proveedorDto!=null?proveedorBO.getSgfensProveedor().getCorreo():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Nombre de la Empresa:</label><br/>
                                        <input maxlength="100" type="text" id="nombreEmpresaProveedor" name="nombreEmpresaProveedor" style="width:300px"
                                               value="<%=proveedorDto!=null?proveedorBO.getSgfensProveedor().getNombreEmpresa():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Categoría:</label><br/>
                                        <select size="1" id="idCategoriaSgfensProveedorProducto" name="idCategoriaSgfensProveedorProducto" >
                                            <option value="-1" >Selecciona una categoría</option>
                                                <%
                                                    out.print(new SGProveedorCategoriaBO(user.getConn()).getSgfensProveedorCategoriasByIdHTMLCombo(idEmpresa, (proveedorDto!=null?proveedorBO.getSgfensProveedor().getIdCategoriaProveedor():-1) ));                                                     
                                                %>
                                        </select>                                        
                                    </p>  
                                    <br/>
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=proveedorDto!=null?(proveedorDto.getIdEstatus()==1?"checked":""):"checked" %> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
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
                        
                        <!--Columna derecha-->
                        <div class="column_right">
                            <div class="header">
                                <span>
                                    Domicilio
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <p>
                                        <label>*Calle:</label><br/>
                                        <input maxlength="100" type="text" id="calle" name="calle" style="width:300px"
                                               value="<%=proveedorDto!=null?proveedorBO.getSgfensProveedor().getCalle():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Numero exterior:</label><br/>
                                        <input maxlength="30" type="text" id="numero" name="numero" style="width:300px"
                                               value="<%=proveedorDto!=null?proveedorBO.getSgfensProveedor().getNumero():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Numero interior:</label><br/>
                                        <input maxlength="30" type="text" id="numeroInt" name="numeroInt" style="width:300px"
                                               value="<%=proveedorDto!=null?proveedorBO.getSgfensProveedor().getNumeroInterior():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*<%=empresaPermisoAplicacionDto.getRfcPorNipCodigo() != 2?"Colonia":"Parroquia"%>:</label><br/>
                                        <input maxlength="100" type="text" id="colonia" name="colonia" style="width:300px"
                                               value="<%=proveedorDto!=null?proveedorBO.getSgfensProveedor().getColonia():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label><%=empresaPermisoAplicacionDto.getRfcPorNipCodigo() == 0?"*":""%>Código Postal:</label><br/>
                                        <input maxlength="5" type="text" id="cp" name="cp" style="width:300px"
                                               value="<%=proveedorDto!=null?proveedorBO.getSgfensProveedor().getCodigoPostal():"" %>"
                                               onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*<%=empresaPermisoAplicacionDto.getRfcPorNipCodigo() != 2?"Municipio/Delegación":"Cantón"%>:</label><br/>
                                        <input maxlength="100" type="text" id="municipio" name="municipio" style="width:300px"
                                               value="<%=proveedorDto!=null?proveedorBO.getSgfensProveedor().getMunicipio():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*<%=empresaPermisoAplicacionDto.getRfcPorNipCodigo() != 2?"Estado":"Provincia"%>:</label><br/>
                                        <input maxlength="100" type="text" id="estado" name="estado" style="width:300px"
                                               value="<%=proveedorDto!=null?proveedorBO.getSgfensProveedor().getEstado():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*País:</label><br/>
                                        <input maxlength="100" type="text" id="pais" name="pais" style="width:300px"
                                               value="<%=proveedorDto!=null?proveedorBO.getSgfensProveedor().getPais():"México" %>"/>
                                    </p>
                                    <br/>
                                    
                            </div>
                                          
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_imagenPersonal.png" alt="icon"/>                                    
                                    Imagen Del Proveedor                                    
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">                                   
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />                                                                                                       
                                    
                                    <%
                                        //Si se esta en modo para agregar un nuevo registro se muestra el upload
                                        //if (mode.equals("")){
                                    %>
                                    <p>
                                        <label>Subir Archivo (.jpg)</label><br/>
                                        <iframe src="../file/uploadSingleForm.jsp?id=archivoImagen&validate=jpg&queryCustom=isImagenProveedorAdjunto=1" 
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
                                        <input maxlength="30" type="text" id="nombreImagenSgfensProveedorProducto" name="nombreImagenSgfensProveedorProducto" style="width:300px"
                                               readonly value="<%=(proveedorDto!=null && proveedorDto.getNombreImagenProveedor()!=null)?proveedorDto.getNombreImagenProveedor():"" %>"/>
                                    </p>
                                    <br/>
                                    
                                    <% if (proveedorDto!=null && proveedorDto.getNombreImagenProveedor()!=null) {
                                        String rutaArchivoImagenPersonal = appConfig.getApp_content_path() + empresaDto.getRfc() +"/ImagenProveedor/" + proveedorDto.getNombreImagenProveedor();
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
                        <!--Fin Columna derecha-->
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