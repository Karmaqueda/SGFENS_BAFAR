<%-- 
    Document   : catCrFormulario_disenar_form
    Created on : 13/06/2016, 01:40:35 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.bo.CrFormularioValidacionBO"%>
<%@page import="com.tsp.sct.bo.CrFormularioCampoBO"%>
<%@page import="com.tsp.sct.dao.dto.CrFormularioCampo"%>
<%@page import="com.tsp.sct.dao.dto.CrTipoCampo"%>
<%@page import="com.tsp.sct.bo.CrTipoCampoBO"%>
<%@page import="com.tsp.sct.dao.dto.CrGrupoFormulario"%>
<%@page import="com.tsp.sct.dao.dto.CrFormulario"%>
<%@page import="com.tsp.sct.bo.CrGrupoFormularioBO"%>
<%@page import="com.tsp.sct.bo.CrFormularioBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="frmDiseno" scope="session" class="com.tsp.sgfens.sesion.CrFormularioDisenoSesion"/>
<%
//Verifica si el usuario tiene acceso a este topico
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
    int idCrFormulario = 0;
    try {
        idCrFormulario = Integer.parseInt(request.getParameter("idCrFormulario"));
    } catch (NumberFormatException e) {
    }
    
    /*
     *   0/"" = nuevo
     *   1 = editar/consultar
     *   2 = eliminar  
     */
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
    
    CrFormularioBO crFormularioBO = new CrFormularioBO(user.getConn());
    CrGrupoFormularioBO crGrupoFormularioBO = new CrGrupoFormularioBO(user.getConn());
    CrFormularioCampoBO crFormularioCampoBO = new CrFormularioCampoBO(user.getConn());
    CrFormulario crFormularioDto = null;
    CrGrupoFormulario crGrupoFormulario = null;
    CrFormularioCampo[] crFormularioCampos = new CrFormularioCampo[0];
    if (idCrFormulario > 0){
        crFormularioBO = new CrFormularioBO(idCrFormulario, user.getConn());
        crFormularioDto = crFormularioBO.getCrFormulario();
        
        if (crFormularioDto!=null){
            crGrupoFormulario = crGrupoFormularioBO.findCrGrupoFormulariobyId(crFormularioDto.getIdGrupoFormulario());
            crFormularioCampos = crFormularioCampoBO.findCrFormularioCampos(-1, idEmpresa, 0, 0, " AND id_formulario = " + crFormularioDto.getIdFormulario());
        }
    }
    
    CrTipoCampoBO crTipoCampoBO = new CrTipoCampoBO(user.getConn());
    CrTipoCampo[] crTipoCampos = crTipoCampoBO.findCrTipoCampos(-1, -1, 0, 0, " AND id_estatus=1 ");
    CrFormularioValidacionBO crFormularioValidacionBO = new CrFormularioValidacionBO(user.getConn());
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../include/keyWordSEO.jsp" />

        <title><jsp:include page="../include/titleApp.jsp" /></title>

        <jsp:include page="../include/skinCSS.jsp" />

        <jsp:include page="../include/jsFunctions2.jsp"/>
        
        <script type="text/javascript">
            
            var cantidadCampos = <%= crFormularioCampos.length %>
            var wrapper;
            
            $(document).ready(function() {
                wrapper =  $("#wrapper_fields");
                
                // comportamiento de campo para formulas, convierte todo a mayusculas
                $('#variable_formula').keyup(function(){
                    this.value = this.value.toUpperCase();
                });
            });
            
            function grabar(){
                if(validar()){
                    $.ajax({
                        type: "POST",
                        url: "catCrFormulario_disenar_ajax.jsp",
                        data: { mode : '5'}, //$("#frm_action").serialize(),
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
                                            location.href = "catCrFormulario_list.jsp?pagina="+"<%=paginaActual%>";
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
                /*
                if(jQuery.trim($("#nombre").val())==""){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "nombre de contacto" es requerido</center>',{'animate':true});
                    $("#nombre_contacto").focus();
                    return false;
                }
                */
                return true;
            }     
            
            function tabSwitch(control){
                
                //quitamos el active a los otros tab
                $(control).parent().parent().find('td input').removeClass('active');
                //agregamos active al tab seleccionado
		$(control).addClass('active');
            }
            
            function inicializarFormulario(idCrFormulario){
                $.ajax({
                    type: "POST",
                    url: "catCrFormulario_disenar_ajax.jsp",
                    data: { mode : '2', idCrFormulario : idCrFormulario },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#ajax_loading").fadeOut("slow");
                           
                           $(wrapper).html(datos);
                           
                           $("#action_buttons").fadeIn("slow");
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
            
            function agregaCampo(idTipoCampo){                
                var nombreCampo = 'campo_' + (cantidadCampos+1);
                $.ajax({
                    type: "POST",
                    url: "catCrFormulario_disenar_ajax.jsp",
                    data: { mode : '1', tipo_campo : idTipoCampo, nombre_id_campo : nombreCampo },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#ajax_loading").fadeOut("slow");
                            
                            cantidadCampos++;
                            //$(wrapper).append(datos + '<br/>');
                            $(wrapper).html(datos);
                            
                            $("#action_buttons").fadeIn("slow");
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
            
            /**
             * Limpiamos todos los controles existentes dentro de un Contenedor
             * como span, div, form... 
             * @param {type} idContenedor id html del contenedor
             * @returns {undefined}             
             */
            function limpiaControles(idContenedor){
                $('#'+idContenedor).find('input:text, input:password, input:file, input:hidden, select, textarea, input[type=date], input[type=time], input[type=number]')
                        .val('');
                $('#'+idContenedor).find('input:radio, input:checkbox')
                        .removeAttr('checked')
                        .removeAttr('selected');                
            }
            
            function muestraConfiguracionCampo(identificadorCampoSesion){
                limpiaControles('div_config_popup');
                $.ajax({
                    type: "POST",
                    url: "catCrFormulario_disenar_ajax.jsp",
                    data: { mode : '3', identificador_campo_sesion : identificadorCampoSesion },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){                           
                           //console.log('data: ' + datos);
                            var strJSON = $.trim( datos.replace('<!--EXITO-->','') );
                            var dto = eval('(' + strJSON + ')');
                            //Asignamos valores
                            $("#mode").val('1');
                            $("#is_requerido").val('true');
                            $("#identificador_campo_sesion").val(dto["identificadorCampoSesion"]);
                            $("#orden").val(dto["orden"]);
                            $("#etiqueta").val(dto["etiqueta"]);
                            $("#descripcion").val(dto["descripcion"]);
                            $("#variable_formula").val(dto["variableFormula"]);
                            $("#is_requerido").prop( "checked", dto["isRequerido"] );
                            var strIdTipoCampo = '' + dto["idTipoCampo"];
                            if (strIdTipoCampo.match(/^(1)$/)) { //solo para campo tipo Texto
                                $("#sugerencia").val(dto["sugerencia"]);
                                $("#id_formulario_validacion").val(dto["idFormularioValidacion"]).change(); // list
                                $("#n_div_text").fadeIn();
                            }else{
                                //ocultamos campos exclusivos
                                $("#n_div_text").fadeOut();
                            }
                            if (strIdTipoCampo.match(/^(2|3|4|10)$/)) { // solo para campos tipo Opcion multiple, unica, lista de opciones
                                $("#lbl_opciones").html('Opciones');
                                var opcionesTxt = ''; //creamos cadena con las opciones, separadas por salto de linea \n
                                $.each(dto["opciones"], function (index, elemento) { 
                                    opcionesTxt += elemento + "\n";
                                  });
                                $("#opciones").val(opcionesTxt); //array
                                $("#n_div_opciones").fadeIn();
                            }else{
                                $("#n_div_opciones").fadeOut();
                            }
                            if (strIdTipoCampo.match(/^(5|10)$/)) { //para campos de subtitulos y formulas
                                $("#n_div_requerido").fadeOut();
                            }else{
                                $("#n_div_requerido").fadeIn();
                            }
                            
                            if (strIdTipoCampo=='10') { //para campos de formulas
                                $("#lbl_opciones").html('Fórmula');
                            }
                            
                            // Mostramos pop-up con campos para editar
                            $("#div_config_popup").modal({
                                opacity:80,
                                overlayCss: {backgroundColor:"#fff"}
                            });                            
                           
                            $("#ajax_loading").fadeOut("slow");
                            $("#action_buttons").fadeIn("slow");
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
            
            function aplicarConfiguracionCampo(){
                $.ajax({
                    type: "POST",
                    url: "catCrFormulario_disenar_ajax.jsp",
                    data: $("#form_campo_configura").serialize(),
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#ajax_loading").fadeOut("slow");
                            
                            $(wrapper).html(datos);
                            
                            //cerramos ventana modal (pop-up)
                            $.modal.close();
                            
                            $("#action_buttons").fadeIn("slow");
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
            
            function quitaCampo(identificadorCampoSesion){
                $.ajax({
                    type: "POST",
                    url: "catCrFormulario_disenar_ajax.jsp",
                    data: { mode : '4', identificador_campo_sesion : identificadorCampoSesion },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#ajax_loading").fadeOut("slow");
                            
                            $(wrapper).html(datos);
                            
                            $("#action_buttons").fadeIn("slow");
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
            
        </script>
    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1><%= crGrupoFormulario!=null ? crGrupoFormulario.getNombre() : "-Sin grupo-" %></h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>
                    
                    <div class="twocolumn">
                        <div class="column_left" style="width: 30%">
                            <div class="header">
                                <span>
                                    Componentes
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th>Tipo</th>
                                            <th>Descripción</th>
                                            <th>Agregar a formulario</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% for (CrTipoCampo tipoCampo : crTipoCampos){ %>
                                        <tr>
                                            <td><%= tipoCampo.getNombre() %></td>
                                            <td><%= tipoCampo.getDescripcion()%></td>
                                            <td>
                                                <a href="#" onclick="agregaCampo(<%= tipoCampo.getIdTipoCampo()%>);">
                                                    <img src="../../images/icon_agregar.png"/>
                                                </a>
                                            </td>
                                        </tr>
                                        <% } %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    
                    <!-- <div class="onecolumn"> -->
                        <div class="column_right" style="width: 68%">
                            <div class="header">
                                <span>
                                    Diseñar Formulario '<%= crFormularioDto!=null ? crFormularioDto.getNombre() : "?" %>'
                                </span>
                                <div class="switch" style="width:50px">
                                    <table width="200px" cellpadding="0" cellspacing="0">
                                        <tbody>
                                            <tr>
                                                <td>
                                                    <input type="button" id="tab_frm_<%= crFormularioDto.getIdFormulario() %> " name="tab_frm_<%= crFormularioDto.getIdFormulario() %>" class="left_switch active" 
                                                           value="<%= crFormularioDto!=null ? crFormularioDto.getNombre() : "?" %>" 
                                                           onclick="tabSwitch(this);"
                                                           style="width:50px"/>
                                                </td>
                                                <!--
                                                <td>
                                                    <input type="button" id="chart_area" name="chart_area" class="middle_switch" value="Area" style="width:50px"/>
                                                </td>
                                                <td>
                                                    <input type="button" id="chart_pie" name="chart_pie" class="middle_switch" value="Pie" style="width:50px"/>
                                                </td>
                                                <td>
                                                    <input type="button" id="chart_line" name="chart_line" class="right_switch" value="Line" style="width:50px"/>
                                                </td>
                                                -->
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                
                                <div id="content_frm_<%= crFormularioDto.getIdFormulario() %>" style="width: 100%">

                                    <div id="wrapper_fields" style="width: 100%"></div>
                                    
                                </div>
                                
                            </div>
                        </div>
                    
                    </div>
                    
                    <br class="clear"/>
                    <br/>
                    <div id="action_buttons">
                        <p>
                            <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                        </p>
                    </div>
                                
                </div>
                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>

        <div id="div_config_popup" title="Configurar Campo" 
             style="display:none; width: 500px; padding: 10px;"
             class="threecolumn_each">
            <form action="" method="post" id="form_campo_configura" name="form_campo_configura">   
                <input type="hidden" id="mode" name="mode" value="1" />
                <input type="hidden" id="identificador_campo_sesion" name="identificador_campo_sesion" value="" />
                <p>
                    <label for="etiqueta">Orden:</label><br/>
                    <input type="number" id="orden" name="orden" value="" min="1" max="999" />
                </p>
                <br/>
                <p>
                    <label for="etiqueta">Etiqueta*:</label><br/>
                    <input type="text" id="etiqueta" name="etiqueta" value="Campo de Texto" maxlength="100"/>
                </p>
                <br/>
                <p>
                    <label for="descripcion">Descripción:</label><br/>
                    <input type="text" id="descripcion" name="descripcion" value="" maxlength="500"/>
                </p>
                <br/>
                <div id="n_div_requerido">
                    <p>
                        <input type="checkbox" id="is_requerido" name="is_requerido" value="true"/><label for="is_requerido">Requerido</label>
                    </p>
                    <br/>
                </div>
                <div id="n_div_text">
                    <p>
                        <label for="sugerencia">Sugerencia:</label><br/>
                        <input type="text" id="sugerencia" name="sugerencia" value="" maxlength="500"/>
                    </p>
                    <br/>
                    <p>
                        <label for="id_formulario_validacion">Validación:</label><br/>
                        <select id="id_formulario_validacion" name="id_formulario_validacion">
                            <option value="-1">-Ninguna-</option>
                            <%= crFormularioValidacionBO.getCrFormularioValidacionsByIdHTMLCombo(idEmpresa, -1, 0, 0, "") %>
                        </select>
                    </p>
                    <br/>
                </div>
                <div id="n_div_opciones" class="help" title="Separar con salto de línea">
                    <p>
                        <label id="lbl_opciones" for="opciones">Opciones*:</label><br/>
                        <textarea id="opciones" name="opciones" rows="3" cols="25"></textarea>
                    </p>
                    <br/>
                </div>
                <p class="help" title="Solo letras y guion bajo">
                    <label for="variable_formula">Variable:</label><br/>
                    <input type="text" id="variable_formula" name="variable_formula" value="" maxlength="50"
                           onchange="validateWithRegex(this, '^[a-zA-Z-_]*$');"/>
                </p>
                <br/>
                <p>
                    <button onclick="aplicarConfiguracionCampo(); return false;">Aplicar</button>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <button class="simplemodal-close">Cerrar</button>
                </p>
            </form>
        </div>
                
            
        <script>
            $("select.flexselect").flexselect();
            inicializarFormulario(<%= idCrFormulario %>);
        </script>
    </body>
</html>
<%}%>