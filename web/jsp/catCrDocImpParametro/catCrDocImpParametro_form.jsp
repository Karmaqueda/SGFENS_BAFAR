<%-- 
    Document   : catCrDocImpParametro_list
    Created on : 30/06/2016, 12:55:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.bo.CrFormularioCampoBO"%>
<%@page import="com.tsp.sct.bo.CrDocImprimibleBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.CrDocImpParametroBO"%>
<%@page import="com.tsp.sct.dao.jdbc.CrDocImpParametroDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrDocImpParametro"%>
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
    int idCrDocImpParametro = 0;
    try {
        idCrDocImpParametro = Integer.parseInt(request.getParameter("idCrDocImpParametro"));
    } catch (NumberFormatException e) {
    }
    
    int idCrDocImprimible = -1;
    try{ idCrDocImprimible = Integer.parseInt(request.getParameter("idCrDocImprimible")); }catch(NumberFormatException e){}

    /*
     *   0/"" = nuevo
     *   1 = editar/consultar
     *   2 = eliminar  
     */
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "0";

    CrDocImpParametroBO crDocImpParametroBO = new CrDocImpParametroBO(user.getConn());
    CrDocImpParametro crDocImpParametroDto = null;
    if (idCrDocImpParametro > 0){
        crDocImpParametroBO = new CrDocImpParametroBO(idCrDocImpParametro, user.getConn());
        crDocImpParametroDto = crDocImpParametroBO.getCrDocImpParametro();
        idCrDocImprimible = crDocImpParametroDto.getIdDocImprimible();
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
                        url: "catCrDocImpParametro_ajax.jsp",
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
                                            location.href = "catCrDocImpParametro_list.jsp?pagina=<%=paginaActual%>&idCrDocImprimible=<%= idCrDocImprimible %>";
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
                                    <% if(crDocImpParametroDto!=null){%>
                                    Editar Parámetro de Documento Imprimible ID <%=crDocImpParametroDto!=null?crDocImpParametroDto.getIdDocImpParametro():"" %>
                                    <%}else{%>
                                    Nuevo Parámetro de Documento Imprimible
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idCrDocImpParametro" name="idCrDocImpParametro" value="<%=crDocImpParametroDto!=null?crDocImpParametroDto.getIdDocImpParametro():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    
                                    <p>
                                        <label>*Documento Imprimible:</label><br/>
                                        <select required id="id_doc_imprimible" name="id_doc_imprimible" style="width: 350px;">
                                            <option value=""></option>
                                            <%= new CrDocImprimibleBO(user.getConn()).getCrDocImprimiblesByIdHTMLCombo(idEmpresa, idCrDocImprimible, 0, 0, (idCrDocImprimible>0?" AND id_doc_imprimible="+idCrDocImprimible:"") ) %>
                                        </select>
                                    </p>
                                    <br/>
                                    
                                    <p>
                                        <label>*Parámetro (enviado a Jasper):</label><br/>
                                        <input required maxlength="50" type="text" id="parametro_clave" name="parametro_clave" style="width:200px"
                                               value="<%=crDocImpParametroDto!=null?crDocImpParametroDto.getParametroClave(): "" %>"/>
                                    </p>
                                    <br/>
                                    
                                    <p>
                                        <label>Descripción:</label><br/>
                                        <input maxlength="500" type="text" id="descripcion" name="descripcion" style="width:350px"
                                               value="<%=crDocImpParametroDto!=null?crDocImpParametroDto.getDescripcion(): "" %>"/>
                                    </p>
                                    <br/>
                                    
                                    <p>
                                        <label>Valor por defecto:</label><br/>
                                        <input maxlength="500" type="text" id="valor_defecto" name="valor_defecto" style="width:350px"
                                               value="<%=crDocImpParametroDto!=null?crDocImpParametroDto.getValorDefecto(): "" %>"/>
                                    </p>
                                    <br/>
                                    
                                    <p>
                                    <label>Variable procedente de formulario:</label><br/>
                                    <input list="frm_campo_variables" maxlength="25" id="asocia_variable_formula" name="asocia_variable_formula" style="width:350px"
                                           value="<%=crDocImpParametroDto!=null?crDocImpParametroDto.getAsociaVariableFormula(): "" %>">
                                    <datalist id="frm_campo_variables" style="width: 350px;">
                                        <%= new CrFormularioCampoBO(user.getConn()).getCrFormularioCamposByIdHTMLComboVariable(idEmpresa, -1, 0, 0, "") %>
                                    </datalist>
                                    </p>
                                    <br/>
                                                                                                  
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=crDocImpParametroDto!=null?(crDocImpParametroDto.getIdEstatus()==1?"checked":""):"checked" %> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
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