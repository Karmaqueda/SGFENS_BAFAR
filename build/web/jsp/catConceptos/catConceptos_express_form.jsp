<%-- 
    Document   : catProductos_form
    Created on : 22/11/2012, 10:13:30 AM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.bo.ExistenciaAlmacenBO"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.dao.dto.SgfensClienteVendedor"%>
<%@page import="com.tsp.sct.bo.SGClienteVendedorBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.bo.PasswordBO"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.bo.CategoriaBO"%>
<%@page import="com.tsp.sct.bo.EmbalajeBO"%>
<%@page import="com.tsp.sct.bo.MarcaBO"%>
<%@page import="com.tsp.sct.bo.ImpuestoBO"%>
<%@page import="com.tsp.sct.bo.AlmacenBO"%>
<%@page import="com.tsp.sct.dao.jdbc.ConceptoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el cliente tiene acceso a este topico
    if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
        response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
        response.flushBuffer();
    } else {

        int paginaActual = 1;
        try {
            paginaActual = Integer.parseInt(request.getParameter("pagina"));
        } catch (Exception e) {
        }

        int idEmpresa = user.getUser().getIdEmpresa();

        /*
         * Parámetros
         */
        int idConcepto = 0;
        try {
            idConcepto = Integer.parseInt(request.getParameter("idConcepto"));
        } catch (NumberFormatException e) {
        }

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = "express";//request.getParameter("acc") != null ? request.getParameter("acc") : "";

        ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
        Concepto conceptosDto = null;
        ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(user.getConn());
        double stockGral = 0;

        String codigoBarrasDefectoEdicion = "";
        if (idConcepto > 0) {
            conceptoBO = new ConceptoBO(idConcepto, user.getConn());
            conceptosDto = conceptoBO.getConcepto();
            //[IDConcepto]-[IDEmpresa]
            codigoBarrasDefectoEdicion = conceptosDto.getIdConcepto() + "-" + conceptosDto.getIdEmpresa();
            //Obtenemos stock gral del prod
            stockGral = exisAlmBO.getExistenciaGeneralByEmpresaProducto(idEmpresa, conceptosDto.getIdConcepto());
        }

        String parameter1 = "idConcepto";

        Configuration appConfig = new Configuration();

        Empresa empresaDto = new EmpresaBO(idEmpresa, user.getConn()).getEmpresa();
        
        EmpresaBO empresaBO = new EmpresaBO(user.getConn());
        EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa());
        
        String verificadorSesionGuiaCerrada = "0";
        String cssDatosObligatorios = "border:3px solid red;";//variable para colocar el css del recuadro que encierra al input para la guia interactiva
        try{
            if(session.getAttribute("sesionCerrada")!= null){
                verificadorSesionGuiaCerrada = (String)session.getAttribute("sesionCerrada");
            }
        }catch(Exception e){}
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

            function grabar() {
                if (validar()) {
                    $.ajax({
                        type: "POST",
                        url: "catConceptos_ajax.jsp",
                        data: $("#frm_action").serialize(),
                        beforeSend: function(objeto) {
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos) {
                            if (datos.indexOf("--EXITO-->", 0) > 0) {
                                $("#ajax_message").html(datos);
                                $("#ajax_loading").fadeOut("slow");
                                $("#ajax_message").fadeIn("slow");
                                $.scrollTo('#inner', 800);
                                apprise('<center><img src=../../images/info.png> <br/>' + datos + '</center>', {'animate': true},
                                function(r) {
                                    location.href = "catConceptos_list.jsp?pagina=" + "<%=paginaActual%>";
                                });
                            } else {
                                $("#ajax_loading").fadeOut("slow");
                                $("#ajax_message").html(datos);
                                $("#ajax_message").fadeIn("slow");
                                $("#action_buttons").fadeIn("slow");
                                $.scrollTo('#inner', 800);
                            }
                        }
                    });
                }
            }

            function validar() {
                /*
                 if(jQuery.trim($("#nombre").val())==""){
                 apprise('<center><img src=../../images/warning.png> <br/>El dato "nombre de contacto" es requerido</center>',{'animate':true});
                 $("#nombre_contacto").focus();
                 return false;
                 }
                 */
                return true;
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
            }


            

            function recuperarNombreArchivoImagen(nombreImagen) {
                $('#nombreArchivoImagen').val('' + nombreImagen);
            }

            function capturaCodigoBarras(){
                if(! $('#chk_codigob_defecto').is(":checked")) {
                    apprise("<center><b>Capture el Código de Producto (identificación/código de barras):</b>"
                        + "<br/><br/><i>*Si cuenta con Lector de Código de Barras, lea el valor:</i></center>", 
                        {'input':true, 'animate':true}, 
                        function(r){
                            if(r) {
                                $("#skuConcepto").val(r);
                            }
                        }
                    );       
                }
            }
            
            function cambioChkCodigoBarrasDefecto(){
                if($('#chk_codigob_defecto').is(":checked")) {
                    $('#skuConcepto').val('<%= codigoBarrasDefectoEdicion %>');
                    $('#skuConcepto').attr('readonly', true);
                }else{
                    $('#skuConcepto').val('');
                    $('#skuConcepto').attr('readonly', false);
                    $('#skuConcepto').focus();
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

                <div class="inner <%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?" expose":""%>" id="leito">
                    <h1>Almácen</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                        <div class="twocolumn">


                            <!-- Start left column window -->
                            <div class="column_left">
                                <div class="header">
                                    <span>
                                        <img src="../../images/icon_producto.png" alt="icon"/>
                                        <% if (conceptosDto != null) {%>
                                        Editar Concepto ID <%=conceptosDto != null ? conceptosDto.getIdConcepto() : ""%>
                                        <%} else {%>
                                        Concepto
                                        <%}%>
                                    </span>
                                </div>
                                <br class="clear"/>
                                <div class="content">
                                    <input type="hidden" id="idConcepto" name="idConcepto" value="<%=conceptosDto != null ? conceptosDto.getIdConcepto() : ""%>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <input type="hidden" id="rfcEmpresa" name="rfcEmpresa" value="<%=empresaDto.getRfc()%>" />

                                    <p>
                                        <label>*Nombre:</label><br/>
                                        <input maxlength="100" type="text" id="nombreConcepto" name="nombreConcepto" style="width:300px;<%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?cssDatosObligatorios:""%>"
                                               value="<%=conceptosDto != null ? conceptoBO.getNombreConceptoLegible() : ""%>"
                                               <%=conceptosDto != null ? "readonly" : ""%>/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Código:</label><br/>
                                        <input maxlength="50" type="text" id="skuConcepto" name="skuConcepto" style="width:300px;"
                                               value="<%=conceptosDto != null ? conceptosDto.getIdentificacion() : ""%>"
                                               onfocus="capturaCodigoBarras();" readonly />
                                        <input type="checkbox" class="checkbox" <%=conceptosDto != null ? ( StringManage.getValidString(conceptosDto.getIdentificacion()).equals(codigoBarrasDefectoEdicion) ? "checked" : "") : "checked"%> 
                                               id="chk_codigob_defecto" name="chk_codigob_defecto" value="1"
                                               onchange="cambioChkCodigoBarrasDefecto();" ><label for="chk_codigob_defecto">Auto-generado</label>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Precio Menudeo/Unitario:</label><br/>
                                        <input maxlength="16" type="text" id="precioConcepto" name="precioConcepto" style="width:300px;<%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?cssDatosObligatorios:""%>"
                                               value="<%=conceptosDto != null ? conceptoBO.getConcepto().getPrecio() : "0"%>"
                                               onkeypress="return validateNumber(event);"/>                                        
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Precio de Compra:</label><br/>
                                        <input maxlength="16" type="text" id="precioCompraConcepto" name="precioCompraConcepto" style="width:300px"
                                               value="<%=conceptosDto != null ? conceptoBO.getConcepto().getPrecioCompra() : "0"%>"
                                               onkeypress="return validateNumber(event);"/>
                                    </p>                                    
                                    <br/>                                     
                                    <p>
                                        <label>Marca:</label><br/>
                                        <select size="1" id="marcaConcepto" name="marcaConcepto" style="width:300px" >
                                            <option value="-1">Selecciona una Marca</option>
                                            <%
                                                out.print(new MarcaBO(user.getConn()).getMarcasByIdHTMLCombo(idEmpresa, (conceptosDto != null ? conceptosDto.getIdMarca() : -1)));
                                            %>
                                        </select>
                                        <!-- <input maxlength="200" type="text" id="marcaConcepto" name="marcaConcepto" style="width:300px"
                                               value="<%=conceptosDto != null ? conceptoBO.getConcepto().getIdMarca() : ""%>"/> -->
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Embalaje:</label><br/>
                                        <select size="1" id="embalajeConcepto" name="embalajeConcepto" style="width:300px" >
                                            <option value="-1">Seleccione un Embalaje</option>
                                            <%
                                                out.print(new EmbalajeBO(user.getConn()).getEmbalajesByIdHTMLCombo(idEmpresa, (conceptosDto != null ? conceptosDto.getIdEmbalaje() : -1)));
                                            %>
                                        </select>
                                        <!-- <input maxlength="200" type="text" id="embalajeConcepto" name="embalajeConcepto" style="width:300px"
                                               value="<%=conceptosDto != null ? conceptoBO.getConcepto().getIdEmbalaje() : ""%>"/> -->
                                    </p>
                                    <br/>
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=conceptosDto != null ? (conceptosDto.getIdEstatus() == 1 ? "checked" : "") : "checked"%> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
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

                                    </span>
                                </div>  
                                <br class="clear"/>
                                <div class="content">
                                    <p>
                                        <label>Stock General:</label><br/>
                                        <input maxlength="10" type="text" id="stockConcepto" name="stockConcepto" style="width:300px"
                                               value="<%=stockGral%>" readonly/>
                                    </p>
                                    <br/>
                                    <% if (conceptosDto == null) {%>
                                    <p>
                                        <label>Stock Inicial:</label><br/>
                                        <input maxlength="10" type="text" id="stockInicial" name="stockInicial" style="width:300px"
                                               value="<%=stockGral%>" />
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Almacén:</label> <br/>
                                        <select size="1" id="idAlmacen" name="idAlmacen" >                                            
                                            <%
                                                out.print(new AlmacenBO(user.getConn()).getAlmacenesByIdHTMLComboPrincipal(idEmpresa, -1,-1,-1,""));
                                            %>
                                        </select>
                                    </p>
                                    <br/>
                                    <% }%>                                  
                                                                       
                                </div>
                            </div>
                            <!--Fin Columna derecha-->
                           
                            
                            <div class="column_right">
                                <div class="header">
                                    <span>                                                                  
                                        Imagen del Producto                                   
                                    </span>
                                </div>
                                <br class="clear"/>
                                <div class="content">                                    
                                    <p>
                                    <div>
                                        <label>*Subir Archivo (.jpg)</label><br/>
                                        <iframe src="../file/uploadSingleForm.jsp?id=archivoImagen&validate=jpg&queryCustom=isImagenConceptoAdjunto=1" 
                                                id="iframe_archivos_jpg" 
                                                name="iframe_archivos_jpg" 
                                                style="border: none; float: top;" scrolling="no" 
                                                height="40" width="100%"  >
                                            <p>Tu navegador no soporta iframes y son necesarios para el buen funcionamiento del aplicativo</p>
                                        </iframe>
                                    </div>
                                    </p>
                                    
                                    <p>
                                        <label>*Nombre Archivo:</label><br/>
                                        <input maxlength="30" type="text" id="nombreArchivoImagen" name="nombreArchivoImagen" style="width:300px"
                                               readonly value="<%=conceptosDto!=null? (conceptosDto.getImagenNombreArchivo()!=null?conceptosDto.getImagenNombreArchivo():""):"" %>"/>
                                    </p>
                                    <br/>
                                    
                                    <% if (conceptosDto!=null && empresaDto!=null) {
                                        if(conceptosDto.getImagenNombreArchivo()!=null){
                                        String rutaArchivoImagenPersonal = appConfig.getApp_content_path() + empresaDto.getRfc() + "/ImagenConcepto/" + conceptosDto.getImagenNombreArchivo();
                                        String rutaArchivoImgEncoded = java.net.URLEncoder.encode(rutaArchivoImagenPersonal, "UTF-8");
                                    %>
                                    <p>
                                        <label>Imagen</label>
                                        <div style="display: inline" >
                                            <a href="../file/download.jsp?ruta_archivo=<%=rutaArchivoImgEncoded%>">Descargar</a>
                                        </div>
                                    </p>
                                    <%}}%>
                                    

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