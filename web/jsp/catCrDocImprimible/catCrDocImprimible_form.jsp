<%-- 
    Document   : catCrDocImprimible_list
    Created on : 30/06/2016, 12:55:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.CrDocImprimibleBO"%>
<%@page import="com.tsp.sct.dao.jdbc.CrDocImprimibleDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrDocImprimible"%>
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
    int idCrDocImprimible = 0;
    try {
        idCrDocImprimible = Integer.parseInt(request.getParameter("idCrDocImprimible"));
    } catch (NumberFormatException e) {
    }

    /*
     *   0/"" = nuevo
     *   1 = editar/consultar
     *   2 = eliminar  
     */
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";

    CrDocImprimibleBO crDocImprimibleBO = new CrDocImprimibleBO(user.getConn());
    CrDocImprimible crDocImprimibleDto = null;
    if (idCrDocImprimible > 0){
        crDocImprimibleBO = new CrDocImprimibleBO(idCrDocImprimible, user.getConn());
        crDocImprimibleDto = crDocImprimibleBO.getCrDocImprimible();
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

        <jsp:include page="../include/jsFunctions2.jsp"/>
        
        <script type="text/javascript">
            
            $(document).ready(function(){
                defineAccionBotones();
            });
            
            function defineAccionBotones(){
                //boton grabar de formulario principal
                $('#frm_action').on('submit', function(e){
                    e.preventDefault();
                    grabar();
                });
            }
            
            function grabar(){
                if(validar()){
                    $.ajax({
                        type: "POST",
                        url: "catCrDocImprimible_ajax.jsp",
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
                                            location.href = "catCrDocImprimible_list.jsp?pagina="+"<%=paginaActual%>";
                                        });
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               $("#action_buttons").fadeIn("slow");
                               $.scrollTo('.inner',800);
                           }
                        }
                    });
                }
            }

            function validar(){
                return $('#frm_action')[0].checkValidity();
            }
            
            function recuperarNombreArchivo(nombreArchivo, numArchivo){
                if (numArchivo==1){
                    $('#nombre_archivo').val('' + nombreArchivo);
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
                    <h1>Documentos Imprimibles</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_crDocImprimible.png" alt="icon"/>
                                    <% if(crDocImprimibleDto!=null){%>
                                    Editar Documento Imprimible ID <%=crDocImprimibleDto!=null?crDocImprimibleDto.getIdDocImprimible():"" %>
                                    <%}else{%>
                                    Nuevo Documento Imprimible
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idCrDocImprimible" name="idCrDocImprimible" value="<%=crDocImprimibleDto!=null?crDocImprimibleDto.getIdDocImprimible():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>*Nombre:</label><br/>
                                        <input required maxlength="50" type="text" id="nombre" name="nombre" style="width:200px"
                                               value="<%=crDocImprimibleDto!=null?crDocImprimibleDto.getNombre(): "" %>"/>
                                    </p>
                                    <br/>
                                    
                                    <p>
                                        <label>*Descripción:</label><br/>
                                        <input required maxlength="500" type="text" id="descripcion" name="descripcion" style="width:350px"
                                               value="<%=crDocImprimibleDto!=null?crDocImprimibleDto.getDescripcion(): "" %>"/>
                                    </p>
                                    <br/>
                                    
                                    <p>
                                        <label>*Tipo:</label><br/>
                                        <select required id="tipo_imprimible" name="tipo_imprimible" style="width:350px">
                                            <option value="">Elige</option>
                                            <% for (String tipo: CrDocImprimibleBO.listaTipoImprimible) { %>
                                            <option value="<%=tipo%>" <%= crDocImprimibleDto!=null? (tipo.equals(crDocImprimibleDto.getTipoImprimible())?"selected":"") : "" %> ><%=tipo%></option>
                                            <% } %>
                                        </select>
                                    </p>
                                    <br/>
                                    
                                    <fieldset style="border: 2px groove; padding: 1em;">
                                        <legend>Archivo Plantilla</legend>
                                        <p>
                                            <label>Subir Archivo (.jasper)</label><br/>
                                            <iframe src="../file/uploadSingleForm.jsp?id=archivo&validate=jasper&queryCustom=isDocGenericoModuloCR=1" 
                                                    id="iframe_archivo_jasper" 
                                                    name="iframe_archivo_jasper" 
                                                    style="border: none" scrolling="no"
                                                    height="80" width="400">
                                                <p>Tu navegador no soporta iframes y son necesarios para el buen funcionamiento del aplicativo</p>
                                            </iframe>
                                        </p>
                                        <p>
                                            <label>*Nombre Archivo:</label><br/>
                                            <input type="text" id="nombre_archivo" name="nombre_archivo" style="width:350px"
                                                   readonly required value="<%=crDocImprimibleDto!=null?crDocImprimibleDto.getNombreArchivoJasper():"" %>"/>
                                        </p>
                                    </fieldset>
                                    <br/>
                                                                                                          
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=crDocImprimibleDto!=null?(crDocImprimibleDto.getIdEstatus()==1?"checked":""):"checked" %> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
                                    </p>
                                    <br/><br/>
                                    
                                    <div id="action_buttons">
                                        <p>
                                            <input type="submit" value="Guardar" />
                                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                        </p>
                                    </div>
                                    
                            </div>
                        </div>
                        <!-- End left column window -->
                        
                        
                    </div>
                    </form>
                    <!--TODO EL CONTENIDO VA AQUÍ-->

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>

        <script>
            $("select.flexselect").flexselect();
        </script>
    </body>
</html>
<%}%>