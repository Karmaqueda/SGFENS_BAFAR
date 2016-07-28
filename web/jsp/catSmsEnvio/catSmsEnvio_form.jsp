<%-- 
    Document   : catSmsEnvio_form
    Created on : 14/03/2016, 05:13:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.bo.SmsPlantillaBO"%>
<%@page import="com.tsp.sct.bo.PretoLicenciaBO"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.bo.SmsAgendaDestinatarioBO"%>
<%@page import="com.tsp.sct.bo.SGProspectoBO"%>
<%@page import="com.tsp.sgfens.ws.request.ProspectoDtoRequest"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.bo.SmsAgendaGrupoBO"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
//Verifica si el cliente tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {

    int idEmpresa = user.getUser().getIdEmpresa();

    /*
     * Parámetros
     */



    /*
     *   0/"" = nuevo
     *   1 = editar/consultar
     *   2 = eliminar  
     */
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";        
    
    EmpresaBO empresaBO = new EmpresaBO(user.getConn());
    EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa());     
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <link rel="shortcut icon" href="../../images/favicon.ico">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../include/keyWordSEO.jsp" />

        <title><jsp:include page="../include/titleApp.jsp" /></title>

        <jsp:include page="../include/skinCSS.jsp" />

        <jsp:include page="../include/jsFunctions.jsp"/>
                
        <script type="text/javascript">
            
            var limiteCaracteresSMS = 160;
            
            var postnameFlexSelect='_flexselect';
            function iniciarFlexSelect(){
                $("#select_cliente").flexselect({
                    jsFunction:  function() { agregarValorSelectYText('select_cliente','lista_clientes_seleccionados','ids_clientes_manual'); }
                });

                $("#select_prospecto").flexselect({
                    jsFunction:  function() { agregarValorSelectYText('select_prospecto','lista_prospectos_seleccionados','ids_prospectos_manual'); }
                });

                $("#select_smsdestinatario").flexselect({
                    jsFunction:  function() { agregarValorSelectYText('select_smsdestinatario','lista_smsdestinatarios_seleccionados','ids_smsdestinatarios_manual'); }
                });

                $("#id_plantilla").flexselect({
                    jsFunction:  function() { seleccionarPlantilla('id_plantilla'); }
                });
                
                $("select.flexselect").flexselect();
            }
            
            function grabar(){                
                $.ajax({
                    type: "POST",
                    url: "catSmsEnvio_ajax.jsp",
                    data: $("#frm_action").serialize(),
                    beforeSend: function(objeto){
                        $("#ajax_message").fadeOut("slow");
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           //$("#ajax_message").html(datos);
                           $("#ajax_loading").fadeOut("slow");
                           //$("#ajax_message").fadeIn("slow");
                           apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                    function(r){
                                        location.href = "catSmsEnvioConfirma_form.jsp";
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
            
            function seleccionarPlantilla(idSelect){
                var idPlantilla = $("#"+idSelect).val();
                console.log('idPlantilla: ' + idPlantilla);
                if (idPlantilla!=='-1'){
                    $.ajax({
                        type: "POST",
                        url: "catSmsEnvio_ajax.jsp",
                        data: {mode : '1', id_plantilla : idPlantilla},
                        beforeSend: function (objeto) {
                            $("#ajax_message_sms").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success:    function (datos) {
                            if(datos.indexOf("--EXITO-->", 0)>0){
                                //console.log('data: ' + datos);
                                var strJSON = $.trim(datos.replace('<!--EXITO-->',''));
                                var dto = eval('(' + strJSON + ')');
                    
                                //Asignamos valores
                                $("#asunto").val(dto["asunto"]);
                                $("#contenido").val(dto["contenido"]);
                                
                                //nos aseguramos que no sean modificables
                                $("#asunto").attr("readonly", true);
                                $("#contenido").attr("readonly", true);

                                //validamos y actualizamos informacion de contenido
                                revisarCaracteresMensaje(document.getElementById('contenido'));
                                limpiarEnter(document.getElementById('contenido'));
                                cuentaCaracteres(document.getElementById('contenido'));
                                
                                $("#ajax_loading").fadeOut("slow");
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message_sms").html(datos);
                               $("#ajax_message_sms").fadeIn("slow");
                               $.scrollTo('#inner',800);
                           }
                        }
                    }); 
                }else{
                    //vaciamos contenido
                    $("#asunto").val('');
                    $("#contenido").val('');
                    //nos aseguramos de que sean modificables
                    $("#asunto").attr("readonly", false);
                    $("#contenido").attr("readonly", false);
                }
            }

            function recuperarNombreArchivoXLS(nombreArchivo){
                $('#nombreArchivo').val('' + nombreArchivo);
            }
            
            function cambiaSelectProspectos(){
                var opcion = $('#prospectos').val();
                if (opcion==='3'){ //selecionar manualmente
                    $("#div_select_prospecto").fadeIn("slow");
                }else{
                    $("#ids_prospectos_manual").val('');
                    $("#div_select_prospecto").fadeOut("slow");
                }
            }
            
            function cambiaSelectClientes(){
                var opcion = $('#clientes').val();
                if (opcion==='3'){ //selecionar manualmente
                    $("#div_select_cliente").fadeIn("slow");
                }else{
                    $("#ids_clientes_manual").val('');
                    $("#div_select_cliente").fadeOut("slow");
                }
            }
            
            function cambiaSelectAgenda(){
                var opcion = $('#select_agenda').val();
                if (opcion==='1'){ //selecionar grupo
                    $("#div_select_grupo_dest_sms").fadeIn("slow");
                }else{
                    $("#div_select_grupo_dest_sms").fadeOut("slow");
                }
                
                if (opcion==='3'){ //selecionar manualmente
                    $("#div_select_destinatarios").fadeIn("slow");
                }else{
                    $("#ids_smsdestinatarios_manual").val('');
                    $("#div_select_destinatarios").fadeOut("slow");
                }
            }
            
            //Funciones Genericas -------------
            
            function blockEnter(event) {
                if (event.keyCode === 13) {
                    event.preventDefault();
                    return false;
                }
                return true;
            }
            
            function limpiarEnter(control){
                control.value = control.value.replace(/\r?\n/g, '');
            }
            
            var mensajeRevisionChar = '';
            function revisarCaracteresMensaje(control){
                var str = control.value;
                var res = str.match(/[ÁÉÍÓÚáéíóú!"·$%&'¡ºª|`^+*¨´{}ç;?¿()\/\[\]àèìòù]/g);
                if(res!==null){
                    mensajeRevisionChar = 'El texto contiene los siguientes caracteres que pueden afectar la entrega de tu mensaje (adicionalmente ocupan 3 espacios cada uno) : '+res;
                    $("#ajax_message_sms").html(mensajeRevisionChar);
                    $("#ajax_message_sms").fadeIn("slow");
                    return true;
                }else{
                    mensajeRevisionChar = '';
                    $("#ajax_message_sms").html('');
                    $("#ajax_message_sms").fadeOut("slow");
                    return false;
                }
           }
           
            function validar(){
                if ( revisarCaracteresMensaje(document.getElementById('contenido')) ){
                    apprise('<center>'+ mensajeRevisionChar +'</center>',{'animate':true, 'confirm':true, 'textOk':'Continuar', 'textCancel':'Cancelar'},
                            function(r){
                                if (r){
                                    grabar();
                                }
                            });
                    return false;
                }else{
                    grabar();
                }
                return true;
            }
           
            function cuentaCaracteres(control){                
                var smsCount = SmsCounter.count(control.value);
                console.log(smsCount);
                console.log(smsCount['encoding']);
                console.log(smsCount['length']);
                console.log(smsCount['per_message']);
                $("#caracteres_restantes").html(''+smsCount['remaining']);
                $("#partes").html(''+smsCount['messages']);
            }
            
            /*
            function cuentaCaracteresServer(control){
                $.ajax({
                    type: "POST",
                    url: "catSmsEnvio_ajax.jsp",
                    data: { mode: '2' },
                    beforeSend: function(objeto){
                        //$("#action_buttons").fadeOut("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                            strJSON = $.trim(datos.replace('<!--EXITO-->',''));
                            
                            var objeto = JSON.parse(strJSON);
                            
                            $("#caracteres_restantes").html(objeto[0]);
                            $("#partes").html(objeto[1]);
                            
                       }else{
                            
                       }
                    }
                });
            }
            */
            
            function agregarValorSelectYText(idSelectOrigen, idSelectDestino,idTextDestino){
                
                //Copiamos valor actual de texto
                var valorIDRegistro = $("#"+idSelectOrigen).val();
                //var textoRegistro =  $("#"+idSelectOrigen+" option:selected").text(); //<-- si es un select normal
                var textoRegistro =  $("#"+idSelectOrigen+postnameFlexSelect).val(); //<-- si usamos flexselect
                
                //alert("idElegido: " + valorIDRegistro + ", nombre: " + textoRegistro);
                console.log("idElegido: " + valorIDRegistro + ", nombre: " + textoRegistro);

                if (valorIDRegistro==='' || valorIDRegistro==='0' || valorIDRegistro==='-1'){
                    apprise('<center><img src=../../images/warning.png> <br/>No eligio ningun registro válido para agregar.</center>',{'animate':true});
                    return;
                }
                
                //Agregamos valor a campo+
                var contenidoActualIDs = $("#"+idTextDestino).val();
                
                //Primero buscamos si no esta repetido el valor
                var coincidenciaPrevia = false;
                var arrayContenidoActualIDs = contenidoActualIDs.split(',');
                arrayContenidoActualIDs.forEach(function(entry) {
                    if (entry===valorIDRegistro)
                        coincidenciaPrevia = true;
                });
                
                //Si no esta repetido, lo agregamos, en caso contrario mostramos advertencia
                if (!coincidenciaPrevia){
                    var resultadoIDs = (contenidoActualIDs.length>0 ? contenidoActualIDs + ',' : '') + valorIDRegistro;
                    //agregamos valor a campo oculto de IDs separados por coma
                    $("#"+idTextDestino).val(resultadoIDs);
                    //agregamos a lista de solo lectura
                    $("#"+idSelectDestino).append("<option value='" + valorIDRegistro + "'>" + textoRegistro + "</option>");
                }else{
                    apprise('<center><img src=../../images/warning.png> <br/>El registro elegido ya se encuentra en la lista.</center>',{'animate':true});
                }
                
                //limpiamos valor de list origen
                $("#"+idSelectOrigen).val('-1');
                $("#"+idSelectOrigen+postnameFlexSelect).val(''); //<-- solo en caso de usar flexselect en select
            }
           
            function quitarItemSeleccionado(idSelect, idInput){
                //Recuperamos valor actual
                var idDto = $("#"+idSelect).val();
                var contenidoTexto = $("#"+idInput).val();
                
                //Hacemos split, dividimos en arreglo
                var arrayContenido = contenidoTexto.split(',');
                
                //Encontramos en que posicion esta
                var indexEnArray = arrayContenido.indexOf(idDto);
                
                if (indexEnArray > -1) {
                    //Borramos elemento de string de ids separados por coma
                    arrayContenido.splice(indexEnArray, 1);
                    //Borramos elemento de select
                    $("#" + idSelect +" option:eq(" + indexEnArray + ")").remove()
                }
                
                //Generamos texto concatenado
                var resultado = '';
                arrayContenido.forEach(function(entry) {
                    resultado += entry + ',';
                });
                    //Quitamos ultima coma
                    if (resultado.slice(-1)===','){
                        resultado = resultado.substring(0, resultado.length - 1);
                    }
                    //Quitamos coma si esta en primer posición
                    if (resultado.substring(0,1)===','){
                        resultado = resultado.substring(1, resultado.length);
                    }
                //Asignamos valor a campo
                $("#"+idInput).val(resultado);
            }
            
            function mostrarCalendario(){
                $( "#fecha_envio" ).datepicker({
                        minDate: 0,
                        gotoCurrent: true,
                        changeMonth: true,
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 100);
                            }, 500)}
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
                    <h1>SMS Masivos</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                        <input type="hidden" id="mode" name="mode" value="3" />
                    <div class="twocolumn">
                        
                        
                        <div class="column_left" id="dest_pretoriano">
                            <div class="header">
                                <span>
                                    Destinatarios Pretoriano
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">                                    

                                <% 
                                 if (empresaPermisoAplicacionDto!=null &&
                                      (empresaPermisoAplicacionDto.getIdPretoLicencia()==PretoLicenciaBO.LICENCIA_PRETO_MOVIL
                                        || empresaPermisoAplicacionDto.getIdPretoLicencia()==PretoLicenciaBO.LICENCIA_DEMO_MOVIL)
                                    ){
                                %>
                                <fieldset style="border: 2px groove; padding: 1em;">
                                    <legend>Clientes (con Celular Registrado)</legend>
                                    <p>
                                        <select size="1" id="clientes" name="clientes" style="width:350px" 
                                                onchange="cambiaSelectClientes();">
                                            <option value="-1" selected>Ninguno</option>
                                            <option value="1">Todos</option>
                                            <option value="2">Con Adeudo</option>
                                            <option value="3">Seleccionar Manualmente</option>
                                        </select>
                                    </p>
                                    
                                    <div id="div_select_cliente" style="display: none;">
                                        <select size="1" id="select_cliente" name="select_cliente" style="width:150px" 
                                                class="flexselect" onchange="agregarValorSelectYText(this.id,'lista_clientes_seleccionados','ids_clientes_manual')">
                                            <option value="-1"></option>
                                            <%
                                                out.print(new ClienteBO(user.getConn()).getClientesByIdHTMLCombo(idEmpresa, -1, " AND id_estatus!=2 AND (CELULAR IS NOT NULL AND CELULAR!='' AND CELULAR!='0000000000' )"));
                                            %>
                                        </select>
                                        &rArr;
                                        <input value="" type="hidden" name="ids_clientes_manual" id="ids_clientes_manual" readonly/>
                                        <select size="4" id="lista_clientes_seleccionados" name="lista_clientes_seleccionados"
                                                style="min-width: 150px;">
                                        </select>
                                        <input type="button" name="btn_quitar_1" id="btn_quitar_1" value="Quitar" alt="Quitar seleccionado" title="Quitar seleccionado" onclick="quitarItemSeleccionado('lista_clientes_seleccionados','ids_clientes_manual');"/>
                                    </div>
                                        
                                </fieldset>
                                <br/>
                                
                                <fieldset style="border: 2px groove; padding: 1em;">
                                    <legend>Prospectos (con Celular Registrado)</legend>
                                    <p>
                                        <select size="1" id="prospectos" name="prospectos" style="width:350px" 
                                                onchange="cambiaSelectProspectos();">
                                            <option value="-1" selected>Ninguno</option>
                                            <option value="1">Todos</option>
                                            <option value="3">Seleccionar Manualmente</option>
                                        </select>
                                    </p>
                                    
                                    <div id="div_select_prospecto" style="display: none;">
                                        <select size="1" id="select_prospecto" name="select_prospecto" style="width:150px" 
                                                class="flexselect" onchange="agregarValorSelectYText(this.id,'lista_prospectos_seleccionados','ids_prospectos_manual')">
                                            <option value="-1"></option>
                                            <%
                                                out.print(new SGProspectoBO(user.getConn()).getProspectosByIdHTMLCombo(idEmpresa, -1));
                                            %>
                                        </select>
                                        &rArr;
                                        <input value="" type="hidden" name="ids_prospectos_manual" id="ids_prospectos_manual" readonly/>
                                        <select size="4" id="lista_prospectos_seleccionados" name="lista_prospectos_seleccionados"
                                                style="min-width: 150px;">
                                        </select>
                                        <input type="button" name="btn_quitar_2" id="btn_quitar_2" value="Quitar" alt="Quitar seleccionado" title="Quitar seleccionado" onclick="quitarItemSeleccionado('lista_prospectos_seleccionados','ids_prospectos_manual');"/>
                                    </div>
                                </fieldset>
                                
                                <% } else{ %>
                                    <div id="info_1" class="alert_info">
                                        Su cuenta es exclusiva para modulo SMS, no cuenta con otros modulos pretoriano.
                                    </div>
                                <% } %>
                            </div>
                        </div>
                                        
                        <div class="column_right">
                            <div class="header">
                                <span>
                                    Programación
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content"> 
                                
                                <input type="radio" name="tipo_envio" value="programado">
                                <fieldset style="border: 2px groove; padding: 1em; display: inline; ">
                                    <legend>Enviar Después</legend>
                                    <p>
                                        <label>Fecha de envío: </label>
                                        <input maxlength="10" type="text" id="fecha_envio" name="fecha_envio" style="width:100px" value="" readonly>
                                        <label>Hora de envío: </label>
                                        <input maxlength="5" type="text" id="hora_envio" name="hora_envio" style="width:100px" value="" 
                                               placeholder="hh:mm" onkeypress="return validateNumberAndChar(event,58);">
                                    </p>
                                </fieldset>
                                <br/><br/>
                                
                                <input type="radio" name="tipo_envio" value="inmediato" checked> Enviar de inmediato
                                <br/>
                            </div>
                        </div>    
                        
                        <br class="clear"/>
                        <br class="clear"/>
                                        
                        <div class="column_left" id="dest_libres">
                            <div class="header">
                                <span>
                                    Destinatarios Libres
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">                                    
                                
                                <fieldset style="border: 2px groove; padding: 1em;">
                                    <legend>Agenda SMS</legend>
                                    <p>
                                        <select size="1" id="select_agenda" name="select_agenda" style="width:350px" 
                                                onchange="cambiaSelectAgenda();">
                                            <option value="-1" selected>Ninguno</option>
                                            <option value="1">Grupo</option>
                                            <option value="3">Seleccionar Manualmente</option>
                                        </select>
                                    </p>
                                    
                                    <div id="div_select_destinatarios" style="display: none;">
                                        <select size="1" id="select_smsdestinatario" name="select_smsdestinatario" style="width:150px" 
                                                class="flexselect" onchange="agregarValorSelectYText(this.id,'lista_smsdestinatarios_seleccionados','ids_smsdestinatarios_manual')">
                                            <option value="-1"></option>
                                            <%
                                                out.print(new SmsAgendaDestinatarioBO(user.getConn()).getSmsAgendaDestinatariosByIdHTMLCombo(idEmpresa, -1,0,0,""));
                                            %>
                                        </select>
                                        &rArr;
                                        <input value="" type="hidden" name="ids_smsdestinatarios_manual" id="ids_smsdestinatarios_manual" readonly/>
                                        <select size="4" id="lista_smsdestinatarios_seleccionados" name="lista_smsdestinatarios_seleccionados"
                                                style="min-width: 150px;">
                                        </select>
                                        <input type="button" name="btn_quitar_3" id="btn_quitar_3" value="Quitar" alt="Quitar seleccionado" title="Quitar seleccionado" onclick="quitarItemSeleccionado('lista_smsdestinatarios_seleccionados','ids_smsdestinatarios_manual');"/>
                                    </div>
                                        
                                    <div id="div_select_grupo_dest_sms" style="display: none;">
                                        <br/>
                                        <select size="1" id="select_grupo_dest_sms" name="select_grupo_dest_sms" style="width:200px">
                                            <option value="-1">Selecciona un Grupo SMS</option>
                                            <%
                                                out.print(new SmsAgendaGrupoBO(user.getConn()).getSmsAgendaGruposByIdHTMLCombo(idEmpresa, -1,0,0,""));
                                            %>
                                        </select>
                                    </div>
                                        
                                </fieldset>

                                <fieldset style="border: 2px groove; padding: 1em;">
                                    <legend>Manual</legend>
                                    <div id="info_2" class="alert_info">
                                        Ej. 4491073763,David,generico 1,generico 2,generico 3,generico 4; 4457667777,Omar,generico 1,generico 2,generico 3,generico 4; o sin nombres 4491073763; 4465442343; 44987773662;
                                    </div>
                                    <p>
                                        <label>*Agregar Destinatarios:</label><br/>
                                        <textarea id="destinatarios_manual" name="destinatarios_manual"
                                            cols="55" rows="4"
                                            onKeyPress="return blockEnter(event);"
                                            onKeyUp="return textArea_maxLen(this,2000);"
                                            onChange="limpiarEnter(this);"
                                            ></textarea>
                                    </p>
                                    <br/>

                                </fieldset>
                                <br/>
                                <fieldset style="border: 2px groove; padding: 1em;">
                                    <legend>Layout Excel&reg;</legend>

                                    <div id="info_3" class="alert_info">
                                    Los renglones deberán tener siempre el campo TELEFONO a 10 dígitos y sin caracteres raros (!”#$%&/()=?¡)
                                    <br/>
                                    NOTA: Deben de ser bloques menores o igual a la cantidad de 10,000 registros.
                                    </div>
                                    <%
                                        //Si se esta en modo para agregar un nuevo registro se muestra el upload
                                        if (mode.equals("")){
                                    %>
                                    <p>
                                        <label>Subir Archivo (.xls)</label><br/>
                                        <iframe src="../file/uploadSingleForm.jsp?id=archivoLayout&validate=xls,XLS&queryCustom=isXlsSmsDestinatarios=1" 
                                                id="iframe_archivos_jpg" 
                                                name="iframe_archivos_jpg" 
                                                style="border: none" scrolling="no"
                                                height="80" width="400">
                                            <p>Tu navegador no soporta iframes y son necesarios para el buen funcionamiento del aplicativo</p>
                                        </iframe>
                                    </p>
                                    <% } %>

                                    <p>
                                        <label>Nombre Archivo:</label><br/>
                                        <input maxlength="30" type="text" id="nombreArchivo" name="nombreArchivo" style="width:300px"
                                               readonly />
                                    </p>                                   
                                    <p>                                                                                                          
                                        <img src="../../images/icon_excel.png"/>
                                        <a href="../recursos/Layout_SMS_Destinatarios.xls">Descargar Plantilla SMS Destinatarios</a>
                                    <p>
                                </fieldset>
                                <br/>

                            </div>
                        </div>
                                    
                        <div class="column_right">
                            <div class="header">
                                <span>
                                    Mensaje
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content"> 
                                <p>
                                    <label>Plantilla:</label>&nbsp;
                                    <select size="1" id="id_plantilla" name="id_plantilla" style="width:300px" 
                                            class="flexselect" onchange="seleccionarPlantilla(this.id);">
                                        <option value="-1"></option>
                                        <%
                                            out.print(new SmsPlantillaBO(user.getConn()).getSmsPlantillaesByIdHTMLCombo(idEmpresa, -1,0,0,""));
                                        %>
                                    </select>
                                </p>
                                <br/>
                                <p>
                                    <label>*Asunto:</label><br/>
                                    <input maxlength="50" type="text" id="asunto" name="asunto" style="width:360px"
                                           value=""/>
                                </p>
                                <br/>

                                <div id="info_sms" class="alert_info">
                                Algunos teléfonos no despliegan correctamente caracteres especiales (acentos y signos) o rechazan los mensajes que los contienen, te sugerimos utilizarlos lo menos posible para que tu mensaje no tenga problema.
                                </div>
                                <br/>
                                <p>
                                    <label>*Contenido:</label><br/>
                                    <textarea id="contenido" name="contenido"
                                        cols="55" rows="6"
                                        onKeyPress="return blockEnter(event);"
                                        onKeyUp="cuentaCaracteres(this);revisarCaracteresMensaje(this); return textArea_maxLen(this,480);"
                                        onKeyDown="cuentaCaracteres(this);"
                                        onChange="limpiarEnter(this);"
                                        ></textarea>
                                    <span id="caracteres_restantes">160</span> / <span id="partes">1</span>
                                </p>
                                <div id="ajax_message_sms" class="alert_warning" style="display: none;"></div>
                                <br/>
                                
                                <div id="action_buttons">
                                    <p>
                                        <% if (mode.equals("")){ %>
                                        <input type="button" id="enviar" value="Continuar" onclick="validar();"/>
                                       <%}%>
                                        <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                    </p>
                                </div>
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

        <script type="text/javascript">
            mostrarCalendario();
            iniciarFlexSelect();
        </script>
    </body>
</html>
<%}%>