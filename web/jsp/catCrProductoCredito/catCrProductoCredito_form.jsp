<%-- 
    Document   : catCrProductoCredito_form
    Created on : 24/06/2016, 12:55:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.bo.CrProductoSeguroBO"%>
<%@page import="com.tsp.sct.dao.dto.CrProductoSeguro"%>
<%@page import="com.tsp.sct.bo.CrDocImprimibleBO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.tsp.sct.bo.CrProductoReglaBO"%>
<%@page import="com.tsp.sct.bo.CrGrupoFormularioBO"%>
<%@page import="com.tsp.sct.bo.CrScoreBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.CrProductoCreditoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.CrProductoCreditoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrProductoCredito"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="crProdCredito" scope="session" class="com.tsp.sgfens.sesion.CrProductoCreditoSesion"/>

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
    int idCrProductoCredito = 0;
    try {
        idCrProductoCredito = Integer.parseInt(request.getParameter("idCrProductoCredito"));
    } catch (NumberFormatException e) {
    }

    /*
     *   0/"" = nuevo
     *   1 = editar/consultar
     *   2 = eliminar  
     */
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";

    CrProductoCreditoBO crProductoCreditoBO = new CrProductoCreditoBO(user.getConn());
    CrProductoReglaBO crProductoReglaBO = new CrProductoReglaBO(user.getConn());
    CrProductoSeguroBO crProductoSeguroBO = new CrProductoSeguroBO(user.getConn());
    //CrProductoCredito crProductoCreditoDto = null;
    CrProductoCredito crProductoCreditoPadre = null;
    CrProductoCredito crProductoCreditoSub = null;
    CrProductoSeguro crProductoSeguro = null;
    int idCrProductoPadre = 0;
    int idCrProductoSub = 0;
    if (idCrProductoCredito > 0){
        crProductoCreditoBO = new CrProductoCreditoBO(idCrProductoCredito, user.getConn());
        CrProductoCredito crProductoCreditoAux = crProductoCreditoBO.getCrProductoCredito();
        if (crProductoCreditoAux.getIdProductoCreditoPadre()>0){
            //es un sub-producto
            crProductoCreditoSub = crProductoCreditoAux;
            crProductoCreditoPadre = new CrProductoCreditoBO(crProductoCreditoAux.getIdProductoCreditoPadre(), user.getConn()).getCrProductoCredito();
            idCrProductoSub = crProductoCreditoSub.getIdProductoCredito();
            idCrProductoPadre = crProductoCreditoPadre.getIdProductoCredito();
        }else{
            //es un producto padre
            crProductoCreditoPadre = crProductoCreditoAux;
            idCrProductoPadre = crProductoCreditoPadre.getIdProductoCredito();
        }
        if (idCrProductoSub>0){
            try{
                CrProductoSeguro[] crProductoSeguros = crProductoSeguroBO.findCrProductoSeguros(-1, idEmpresa, 0, 0, " AND id_producto_credito = " + idCrProductoSub);
                if (crProductoSeguros.length>0)
                    crProductoSeguro = crProductoSeguros[0];
            }catch(Exception ex){

            }
        }
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
            var id_producto_padre = -1;
            var nombre_producto_padre = '';
            
            $(document).ready(function(){
                defineAccionBotones();
            });
            
            /**
             * Muestra pop up inicial para preguntar el tipo de Producto a crear
             * Padre o sub producto
             * @returns {undefined}
             */
            function iniciaCaptura(){
                // Mostramos pop-up con campos para editar
                $("#div_popup_1").modal({
                    opacity:80,
                    overlayCss: {backgroundColor:"#fff"}
                });
            }
            
            function comenzarCapturaPorTipo(tipo){
                var msgError = '';
                if (tipo=='padre'){
                    // eligio crear un producto padre
                    // mostramos campos de captura padre
                    $("#label_cr_sub").hide();
                    $("#label_cr_padre").show();
                    $("#action_buttons").show();
                    $("#div_data_sub").hide();
                    cargaSesionDesdeBDProductoPadre(0);
                }else if (tipo=='sub'){
                    // eligio crear un sub-producto
                    $("#label_cr_sub").show();
                    $("#label_cr_padre").hide();
                    $("#action_buttons").hide();
                    id_producto_padre = $("#id_cr_producto_padre").val();
                    nombre_producto_padre = $("#id_cr_producto_padre option:selected").text();
                    if (id_producto_padre<=0){
                        msgError = 'Debe seleccionar un producto padre válido.';
                    }else{
                        // mostramos campos de captura sub-producto
                        $("#div_data_sub").show();
                        $("#span_header_right").html('Nuevo sub-producto de ' + nombre_producto_padre);
                        $("#sub_idProductoPadre").val(id_producto_padre);
                        cargaSesionDesdeBDProductoPadre(id_producto_padre);
                        $("#action_buttons").hide();
                        $("#div_data_padre").hide();
                    }
                }
                
                if (msgError==''){ //sin errores
                    //cerramos ventana modal (pop-up)
                    $.modal.close();
                }else{
                    // con error
                    apprise('<center>' + msgError + '</center>', {animate: 'true', info:'true'});
                }
            }
            
            function iniciaEdicion(){
                <% if (crProductoCreditoSub==null) { %>
                //edicion de producto padre
                $("#div_data_sub").hide();
                cargaSesionDesdeBDProductoPadre('<%= idCrProductoPadre %>');
                <% }else { %>
                //edicion de sub producto
                $("#action_buttons").hide(); //botones de accion de padre
                $("#div_data_sub").show(); // div para campos sub
                cargaSesionDesdeBDProductoPadre('<%= idCrProductoPadre %>');
                <% } %>
            }
            
            /*function divEncima(){
                $('<div>New Element</div>').css({
                    "position" : "absolute",
                    "left"     : $foo.position().left,
                    "top"      : $foo.position().top
                }).appendTo($container);
            }*/
            
            function cargaSesionDesdeBDProductoPadre(idProductoPadre){
                $.ajax({
                    type: "POST",
                    url: "catCrProductoCredito_ajax.jsp",
                    data: { mode: '1', idCrProductoCredito : idProductoPadre},
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#ajax_loading").fadeOut("slow");
                           if (id_producto_padre<=0)
                                $("#action_buttons").fadeIn("slow");
                           cargaRelacionPuntajeMonto();
                           cargaRelacionDocImprimibles();
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
            
            function cargaRelacionPuntajeMonto(){
                $.ajax({
                    type: "POST",
                    url: "catCrProductoCredito_ajax.jsp",
                    data: { mode: '2'},
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#ajax_loading").fadeOut("slow");
                           if (id_producto_padre<=0)
                                $("#action_buttons").fadeIn("slow");
                           $("#tbody_relacion_puntaje_monto").html(datos);
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
            
            function capturaRelacionPuntajeMonto(){
                //limpiamos controles
                cleanControls("div_popup_2", false);
                // Mostramos pop-up con campos para captura de puntaje monto
                $("#div_popup_2").modal({
                    opacity: 80,
                    overlayCss: {backgroundColor:"#fff"}
                });
            }
            
            function agregaRelacionPuntajeMonto(){
                var data = $("#form_puntaje_monto").serializeArray();
                data.push({mode: '3'});
                
                $.ajax({
                    type: "POST",
                    url: "catCrProductoCredito_ajax.jsp",
                    data: data, //$("#form_puntaje_monto").serialize(),
                    beforeSend: function(objeto){
                        $.modal.close(); // ceramos pop-up
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#ajax_loading").fadeOut("slow");
                           $("#action_buttons").fadeIn("slow");
                           $("#tbody_relacion_puntaje_monto").html(datos);
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
            
            function eliminaRelacionPuntajeMonto(indexListaSesion){
                $.ajax({
                    type: "POST",
                    url: "catCrProductoCredito_ajax.jsp",
                    data: { mode: '4', index_lista_puntaje_monto : indexListaSesion},
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#ajax_loading").fadeOut("slow");
                           $("#action_buttons").fadeIn("slow");
                           $("#tbody_relacion_puntaje_monto").html(datos);
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
            
            function defineAccionBotones(){
                //boton grabar de formulario principal
                $('#frm_action').on('submit', function(e){
                    e.preventDefault();
                    grabar();
                });
                //boton grabar de formulario secundario (sub-producto)
                $('#frm_data_sub').on('submit', function(e){
                    e.preventDefault();
                    grabarSub();
                });
            }
            
            function quitaDecimales(){
                $(this).val($(this).val().replace(/[^\d].+/, ""));
            }

            function grabar(){
                if(validar()){
                    
                    var data = $("#frm_action").serializeArray();
                    console.log(data);
                    
                    $.ajax({
                        type: "POST",
                        url: "catCrProductoCredito_ajax.jsp",
                        data: data, //$("#frm_action").serialize(),
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
                                            location.href = "catCrProductoCredito_list.jsp?pagina="+"<%=paginaActual%>";
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
            
            function grabarSub(){
                if($('#frm_data_sub')[0].checkValidity()){
                    
                    $.ajax({
                        type: "POST",
                        url: "catCrProductoCredito_ajax.jsp",
                        data: $("#frm_data_sub").serialize(),
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
                                            location.href = "catCrProductoCredito_list.jsp?pagina="+"<%=paginaActual%>";
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
            
            
            // Inicia manejo de Documentos Imprimibles ------------------
            
            function cargaRelacionDocImprimibles(){
                $.ajax({
                    type: "POST",
                    url: "catCrProductoCredito_ajax.jsp",
                    data: { mode: '7'},
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#ajax_loading").fadeOut("slow");
                           if (id_producto_padre<=0)
                                $("#action_buttons").fadeIn("slow");
                           $("#tbody_relacion_doc_imprimible").html(datos);
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
            
            function capturaAgregaDocumentoImprimible(){
                //limpiamos controles
                cleanControls("div_popup_3", false);
                // Mostramos pop-up con campos para captura de puntaje monto
                $("#div_popup_3").modal({
                    opacity: 80,
                    overlayCss: {backgroundColor:"#fff"}
                });
            }
            
            function agregaImprimible(){
                var data = $("#form_agrega_imprimible").serializeArray();
                //data.push({mode: '8'});
                
                $.ajax({
                    type: "POST",
                    url: "catCrProductoCredito_ajax.jsp",
                    data: data, //$("#form_puntaje_monto").serialize(),
                    beforeSend: function(objeto){
                        $.modal.close(); // ceramos pop-up
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#ajax_loading").fadeOut("slow");
                           $("#action_buttons").fadeIn("slow");
                           $("#tbody_relacion_doc_imprimible").html(datos);
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
            
            function eliminaRelacionImprimible(indexListaSesion){
                $.ajax({
                    type: "POST",
                    url: "catCrProductoCredito_ajax.jsp",
                    data: { mode: '9', index_lista_doc_imprimible : indexListaSesion},
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#ajax_loading").fadeOut("slow");
                           $("#action_buttons").fadeIn("slow");
                           $("#tbody_relacion_doc_imprimible").html(datos);
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
            // Finaliza manejo de Documentos Imprimibles ---------------
            
        </script>
    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Producto Crédito</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <div class="twocolumn">
                        
                        <form action="" id="frm_action">
                        <div class="column_left" id="div_data_padre">
                            <div class="header">
                                <span id="span_header_left">
                                    <img src="../../images/icon_crProductoCredito.png" alt="icon"/>
                                    <% if(crProductoCreditoPadre!=null){%>
                                    Editar <%= crProductoCreditoSub!=null?"Sub-Producto Crédito":"Producto Crédito Padre" %> ID <%=crProductoCreditoSub!=null?crProductoCreditoSub.getIdProductoCredito() : crProductoCreditoPadre.getIdProductoCredito() %>
                                    <%}else{%>
                                    <n id="label_cr_padre">Nuevo Producto Crédito Padre</n>
                                    <n id="label_cr_sub">Datos Crédito Padre (solo lectura)</n>
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idCrProductoCredito" name="idCrProductoSub" value="<%=crProductoCreditoSub!=null?crProductoCreditoSub.getIdProductoCredito():"" %>" />
                                    <input type="hidden" id="idProductoCrPadre" name="idProductoCrPadre" value="<%=crProductoCreditoPadre!=null?crProductoCreditoPadre.getIdProductoCredito():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="5" />
                                    <p>
                                        <label>*Nombre:</label><br/>
                                        <input required= "required" maxlength="50" type="text" id="nombre" name="nombre" style="width:200px"
                                               value="<%=crProductoCreditoPadre!=null?crProductoCreditoPadre.getNombre(): "" %>"/>
                                    </p>
                                    <br/>
                                    
                                    <p>
                                        <label>*Descripción:</label><br/>
                                        <input required= "required" maxlength="500" type="text" id="descripcion" name="descripcion" style="width:350px"
                                               value="<%=crProductoCreditoPadre!=null?crProductoCreditoPadre.getDescripcion(): "" %>"/>
                                    </p>
                                    <br/>
                                    
                                    <p>
                                        <label>*Tipo amortización:</label><br/>
                                        <input type="radio" id="tipo_amortizacion_1" name="tipo_amortizacion" value="CUOTA_FIJA"
                                               <%=crProductoCreditoPadre!=null? (crProductoCreditoPadre.getTipoAmortizacion().equals("CUOTA_FIJA")?"checked":"" ): "" %>/>
                                        <label for="tipo_amortizacion_1">Cuota Fija</label>
                                        <br/>
                                        <input type="radio" id="tipo_amortizacion_2" name="tipo_amortizacion" value="AMORTIZACION_FIJA" 
                                               <%=crProductoCreditoPadre!=null? (crProductoCreditoPadre.getTipoAmortizacion().equals("AMORTIZACION_FIJA")?"checked":"" ): "" %>/>
                                        <label for="tipo_amortizacion_2">Amortización Fija</label>
                                    </p>
                                    <br/>

                                    <p>
                                        <label>*Grupo Formulario Solicitud:</label><br/>
                                        <select required= "required" id="id_grupo_formulario_solic" name="id_grupo_formulario_solic" style="width: 350px;">
                                            <option value="-1">Selecciona</option>
                                            <%= new CrGrupoFormularioBO(user.getConn()).getCrGrupoFormulariosByIdHTMLCombo(idEmpresa, (crProductoCreditoPadre!=null?crProductoCreditoPadre.getIdGrupoFormularioSolic(): -1), 0, 0, "") %>
                                        </select>
                                    </p>
                                    <br/>
                                    
                                    <p>
                                        <label>*Grupo Formulario Verificación:</label><br/>
                                        <select required= "required" id="id_grupo_formulario_verif" name="id_grupo_formulario_verif" style="width: 350px;">
                                            <option value="-1">Selecciona</option>
                                            <%= new CrGrupoFormularioBO(user.getConn()).getCrGrupoFormulariosByIdHTMLCombo(idEmpresa, (crProductoCreditoPadre!=null?crProductoCreditoPadre.getIdGrupoFormularioVerif(): -1), 0, 0, "") %>
                                        </select>
                                    </p>
                                    <br/>
                                    
                                    <!--
                                    <p>
                                        <label>*Grupo Formulario Evidencia Fotográfica:</label><br/>
                                        <select required= "required" id="id_grupo_formulario_fotos" name="id_grupo_formulario_fotos" style="width: 350px;">
                                            <option value="-1">Selecciona</option>
                                            <%= new CrGrupoFormularioBO(user.getConn()).getCrGrupoFormulariosByIdHTMLCombo(idEmpresa, (crProductoCreditoPadre!=null?crProductoCreditoPadre.getIdGrupoFormularioFotos(): -1), 0, 0, "") %>
                                        </select>
                                    </p>
                                    <br/>
                                    -->
                                    
                                    <p>
                                        <label>Garantías:</label><br/>
                                        <input maxlength="500" type="text" id="garantias_descripcion" name="garantias_descripcion" style="width:350px"
                                               value="<%=crProductoCreditoPadre!=null?crProductoCreditoPadre.getGarantiasDescripcion(): "" %>"/>
                                    </p>
                                    <br/>
                                    
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=crProductoCreditoPadre!=null?(crProductoCreditoPadre.getIdEstatus()==1?"checked":""):"checked" %> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
                                    </p>
                                    <br/>
                                    
                                    <fieldset style="border: 2px groove; padding: 1em;">
                                        <legend>Reglas de Aplicación Score:</legend>
                                        
                                        <p>
                                            <label>*Score:</label><br/>
                                            <select id="id_score" name="id_score" style="width: 350px;">
                                                <option value="-1">Selecciona</option>
                                                <%= new CrScoreBO(user.getConn()).getCrScoresByIdHTMLCombo(idEmpresa, (crProductoCreditoPadre!=null?crProductoCreditoPadre.getIdScore() : -1), 0, 0, "") %>
                                            </select>
                                        </p>
                                        <br/>
                                        
                                        <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                            <thead>
                                                <tr>
                                                    <th>Criterio</th>
                                                    <th>Valor</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr>
                                                    <td>Monto mayor o igual a. >=</td>
                                                    <td>
                                                        $<input type="number" id="<%= CrProductoReglaBO.AP_MONTO_MAYOR_IGUAL %>" name="<%= CrProductoReglaBO.AP_MONTO_MAYOR_IGUAL %>" style="width:50px"
                                                                min="0" max="250000" step="1" value="<%= crProductoReglaBO.getValorEspecificoReglaStr(idCrProductoPadre, CrProductoReglaBO.AP_MONTO_MAYOR_IGUAL) %>"/>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>Monto menor o igual a. <=</td>
                                                    <td>
                                                        $<input type="number" id="<%= CrProductoReglaBO.AP_MONTO_MENOR_IGUAL %>" name="<%= CrProductoReglaBO.AP_MONTO_MENOR_IGUAL %>" style="width:50px"
                                                                min="0" max="250000" step="1" value="<%= crProductoReglaBO.getValorEspecificoReglaStr(idCrProductoPadre, CrProductoReglaBO.AP_MONTO_MENOR_IGUAL) %>"/>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>Periodo Pago mayor o igual a. >=</td>
                                                    <td>
                                                        <input type="number" id="<%= CrProductoReglaBO.AP_PLAZO_MAYOR_IGUAL %>" name="<%= CrProductoReglaBO.AP_PLAZO_MAYOR_IGUAL %>" style="width:50px"
                                                                min="0" max="250000" step="1" value="<%= crProductoReglaBO.getValorEspecificoReglaStr(idCrProductoPadre, CrProductoReglaBO.AP_PLAZO_MAYOR_IGUAL) %>"/> meses
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </fieldset>
                                    <br/>
                                    
                                    <fieldset style="border: 2px groove; padding: 1em;">
                                        <legend>Reglas de Rechazo directo:</legend>
                                        
                                        <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                            <thead>
                                                <tr>
                                                    <th>Criterio</th>
                                                    <th>Valor</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr>
                                                    <td>Edad mayor o igual a. >=</td>
                                                    <td>
                                                        <input type="number" id="<%= CrProductoReglaBO.RE_EDAD_MAYOR_IGUAL %>" name="<%= CrProductoReglaBO.RE_EDAD_MAYOR_IGUAL %>" style="width:50px"
                                                                min="0" max="90" step="1" value="<%= crProductoReglaBO.getValorEspecificoReglaStr(idCrProductoPadre, CrProductoReglaBO.RE_EDAD_MAYOR_IGUAL) %>"/> años
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>Edad menor o igual a. <=</td>
                                                    <td>
                                                        <input type="number" id="<%= CrProductoReglaBO.RE_EDAD_MENOR_IGUAL %>" name="<%= CrProductoReglaBO.RE_EDAD_MENOR_IGUAL %>" style="width:50px"
                                                                min="0" max="90" step="1" value="<%= crProductoReglaBO.getValorEspecificoReglaStr(idCrProductoPadre, CrProductoReglaBO.RE_EDAD_MENOR_IGUAL) %>"/> años
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>Antig&uuml;edad en Domicilio menor o igual a. <=</td>
                                                    <td>
                                                        <input type="number" id="<%= CrProductoReglaBO.RE_ANTIGUEDAD_DOMICILIO_MENOR_IGUAL %>" name="<%= CrProductoReglaBO.RE_ANTIGUEDAD_DOMICILIO_MENOR_IGUAL %>" style="width:50px"
                                                                min="0" max="25" step="1" value="<%= crProductoReglaBO.getValorEspecificoReglaStr(idCrProductoPadre, CrProductoReglaBO.RE_ANTIGUEDAD_DOMICILIO_MENOR_IGUAL) %>"/> años
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>Antig&uuml;edad en Empleo menor o igual a. <=</td>
                                                    <td>
                                                        <input type="number" id="<%= CrProductoReglaBO.RE_ANTIGUEDAD_EMPLEO_MENOR_IGUAL %>" name="<%= CrProductoReglaBO.RE_ANTIGUEDAD_EMPLEO_MENOR_IGUAL %>" style="width:50px"
                                                                min="0" max="25" step="1" value="<%= crProductoReglaBO.getValorEspecificoReglaStr(idCrProductoPadre, CrProductoReglaBO.RE_ANTIGUEDAD_EMPLEO_MENOR_IGUAL) %>"/> años
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>Ingresos menor o igual a. <=</td>
                                                    <td>
                                                        $<input type="number" id="<%= CrProductoReglaBO.RE_INGRESOS_MENOR_IGUAL %>" name="<%= CrProductoReglaBO.RE_INGRESOS_MENOR_IGUAL %>" style="width:50px"
                                                                min="0" max="250000" step="1" value="<%= crProductoReglaBO.getValorEspecificoReglaStr(idCrProductoPadre, CrProductoReglaBO.RE_INGRESOS_MENOR_IGUAL) %>"/>
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </fieldset>
                                    <br/>
                                    
                                    <fieldset style="border: 2px groove; padding: 1em;">
                                        <legend>Relación Puntaje - Monto:</legend>
                                        <% if (crProductoCreditoSub==null){ %>
                                        <input type="button" id="btn_agregar_relacion_puntaje_monto" value="Agregar" onclick="capturaRelacionPuntajeMonto();"/>
                                        <% } %>
                                        <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                            <thead>
                                                <tr>
                                                    <th>Puntaje Mínimo</th>
                                                    <th>Puntaje Máximo</th>
                                                    <th>&permil; Autorizado</th>
                                                    <th></th>
                                                </tr>
                                            </thead>
                                            <tbody id="tbody_relacion_puntaje_monto">
                                                <tr>
                                                    <td>?</td>
                                                    <td>?</td>
                                                    <td>?</td>
                                                    <td></td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </fieldset>
                                    <br/>
                                    
                                    <fieldset style="border: 2px groove; padding: 1em;">
                                        <legend>Documentos Imprimibles:</legend>
                                        <% if (crProductoCreditoSub==null){ %>
                                        <input type="button" id="btn_agregar_documento_imprimible" value="Agregar" onclick="capturaAgregaDocumentoImprimible();"/>
                                        <% } %>
                                        <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                            <thead>
                                                <tr>
                                                    <th>Tipo</th>
                                                    <th>Nombre</th>
                                                    <th></th>
                                                </tr>
                                            </thead>
                                            <tbody id="tbody_relacion_doc_imprimible">
                                                
                                            </tbody>
                                        </table>
                                    </fieldset>
                                    <br/>
                                    <br/>
                                    
                                    <div id="action_buttons">
                                        <% if (crProductoCreditoSub==null){ %>
                                        <p>
                                            <input type="submit" value="Guardar" />
                                            <!--<input type="button" id="enviar" value="Guardar" onclick="grabar();"/>-->
                                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                        </p>
                                        <% } %>
                                    </div>
                                    
                            </div>   
                        </div>
                        </form>
                              
                        <form action="" id="frm_data_sub">                  
                        <div class="column_right" id="div_data_sub">
                            <div class="header">
                                <span id="span_header_right">
                                    <% if(crProductoCreditoSub!=null){%>
                                    Editar Sub-Producto
                                    <%}else{%>
                                    Nuevo Sub-producto
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                <input type="hidden" id="sub_mode" name="mode" value="6"/>
                                <input type="hidden" id="sub_idProductoSub" name="idProductoSub" value="<%=crProductoCreditoSub!=null?crProductoCreditoSub.getIdProductoCredito():"" %>" />
                                <input type="hidden" id="sub_idProductoPadre" name="idProductoPadre" value="<%=crProductoCreditoSub!=null?crProductoCreditoSub.getIdProductoCreditoPadre():"" %>" />
                                <p>
                                    <label>*Nombre:</label><br/>
                                    <input required= "required" maxlength="50" type="text" id="sub_nombre" name="sub_nombre" style="width:200px"
                                           value="<%=crProductoCreditoSub!=null?crProductoCreditoSub.getNombre(): "" %>"/>
                                </p>
                                <br/>

                                <p>
                                    <label>*Descripción:</label><br/>
                                    <input required= "required" maxlength="500" type="text" id="sub_descripcion" name="sub_descripcion" style="width:350px"
                                           value="<%=crProductoCreditoSub!=null?crProductoCreditoSub.getDescripcion(): "" %>"/>
                                </p>
                                <br/>
                                <p>
                                    <label for="sub_monto">*Monto:</label><br/>
                                    <input type="number" required="required" id="sub_monto" name="sub_monto" style="width:80px"
                                        min="1" max="250000" step="1" value="<%= crProductoCreditoSub!=null? crProductoCreditoSub.getMonto() : 0 %>"/>
                                </p>
                                <br/>
                                <p>
                                    <label for="sub_plazo">*Plazo (en Meses):</label><br/>
                                    <input type="number" required="required" id="sub_plazo" name="sub_plazo" style="width:50px"
                                        min="1" max="120" step="1" value="<%= crProductoCreditoSub!=null? crProductoCreditoSub.getPlazo(): 0 %>"/>
                                </p>
                                <br/>
                                <p>
                                    <label for="sub_tasa_interes_anual">*Tasa intéres Anual (con IVA):</label><br/>
                                    <input type="number" required="required" id="sub_tasa_interes_anual" name="sub_tasa_interes_anual" style="width:50px"
                                        min="1" max="100" step="any" value="<%= crProductoCreditoSub!=null? crProductoCreditoSub.getTasaInteresAnual(): 0 %>"/>
                                </p>
                                <br/>
                                <p>
                                    <label for="sub_tasa_interes_mora">*Tasa intéres Moratoria (con IVA):</label><br/>
                                    <input type="number" required="required" id="sub_tasa_interes_mora" name="sub_tasa_interes_mora" style="width:50px"
                                        min="1" max="100" step="any" value="<%= crProductoCreditoSub!=null? crProductoCreditoSub.getTasaInteresMora(): 0 %>"/>
                                </p>
                                <br/>
                                <p>
                                    <label for="sub_cat">*Costo Anual Total (CAT %):</label><br/>
                                    <input type="number" required="required" id="sub_cat" name="sub_cat" style="width:50px"
                                        min="1" max="100" step="any" value="<%= crProductoCreditoSub!=null? crProductoCreditoSub.getCostoAnualTotal() : 0 %>"/>
                                </p>
                                <br/>
                                <fieldset style="border: 2px groove; padding: 1em;">
                                    <legend>Seguro:</legend>
                                    <p>
                                        <label>Obligatorio:</label>
                                        <input type="radio" name="seguro_obligatorio" id="seguro_obligatorio_si" value="1" <%= crProductoSeguro!=null?(crProductoSeguro.getIsObligatorio()==1?"checked":""):""%> /><label for="seguro_obligatorio_si">Si</label>
                                        <input type="radio" name="seguro_obligatorio" id="seguro_obligatorio_no" value="0" <%= crProductoSeguro!=null?(crProductoSeguro.getIsObligatorio()!=1?"checked":""):"checked"%>/><label for="seguro_obligatorio_no">No</label>
                                    </p>
                                    <br/>
                                    <p>
                                        <label for="seguro_tipo">Tipo:</label><br/>
                                        <input maxlength="100" type="text" id="seguro_tipo" name="seguro_tipo" style="width:200px"
                                               value="<%=crProductoSeguro!=null?crProductoSeguro.getTipoSeguro(): "" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label for="seguro_aseguradora">Aseguradora:</label><br/>
                                        <input maxlength="100" type="text" id="seguro_aseguradora" name="seguro_aseguradora" style="width:200px"
                                               value="<%=crProductoSeguro!=null?crProductoSeguro.getAseguradoraNombre(): "" %>"/>
                                    </p>
                                    <br/>
                                </fieldset>
                                <br/>
                                <p>
                                    <label for="sub_gastos_cobranza">*Gastos de Cobranza:</label><br/>
                                    <input type="number" required="required" id="sub_gastos_cobranza" name="sub_gastos_cobranza" style="width:50px"
                                        min="0" max="2500" step="1" value="<%= crProductoCreditoSub!=null? crProductoCreditoSub.getGastosCobranza(): 0 %>"/>
                                </p>
                                <br/>
                                <p>
                                    <input type="checkbox" class="checkbox" <%=crProductoCreditoSub!=null?(crProductoCreditoSub.getIdEstatus()==1?"checked":""):"checked" %> id="sub_estatus" name="sub_estatus" value="1"> <label for="sub_estatus">Activo</label>
                                </p>
                                <br/>
                                
                                <div id="action_buttons_sub">
                                    <p>
                                        <input type="submit" value="Guardar" />
                                        <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                    </p>
                                </div>
                            </div>
                        </div>   
                        </form>
                                                    
                    </div>
                    <!--TODO EL CONTENIDO VA AQUÍ-->

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>

        <div id="div_popup_1" title="Iniciar captura" 
             style="display:none; width: 500px; padding: 10px;"
             class="threecolumn_each">
            <form action="" method="post" id="form_init_crear_sub" name="form_init_crear_sub">   
                <input type="hidden" id="mode" name="mode" value="-1" />
                
                <h2>¿Deseas crear un sub-producto?</h2>
                <p>
                    <label for="id_cr_producto_padre">Padre:</label>
                    <select id="id_cr_producto_padre" name="id_cr_producto_padre" style="width: 350px;">
                        <option value="-1">-Ninguna-</option>
                        <%= crProductoCreditoBO.getCrProductoCreditosByIdHTMLCombo(idEmpresa, -1, 0, 0, " AND (id_producto_credito_padre IS NULL OR id_producto_credito_padre<=0)") %>
                    </select>
                </p>
                <br/>
                
                <p>
                    <button onclick="comenzarCapturaPorTipo('sub'); return false;">Si, sub-producto</button>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <button onclick="comenzarCapturaPorTipo('padre'); return false;">No, crear Padre</button>
                    <!--<button class="simplemodal-close">Cerrar</button>-->
                </p>
            </form>
        </div>
                    
        <div id="div_popup_2" title="Captura Puntaje-Monto" 
             style="display:none; width: 500px; padding: 10px;"
             class="threecolumn_each">
            <form action="" method="post" id="form_puntaje_monto" name="form_puntaje_monto">   
                <input type="hidden" id="mode" name="mode" value="3" />
                
                <p>
                    <label for="puntaje_min">Puntaje mínimo:</label>
                    <input maxlength="5" type="number" id="puntaje_min" name="puntaje_min" style="width:50px"
                            min="1" max="9999" step="1" value=""/>
                </p>
                <br/>
                <p>
                    <label for="puntaje_max">Puntaje máximo:</label>
                    <input maxlength="5" type="number" id="puntaje_max" name="puntaje_max" style="width:50px"
                            min="1" max="9999" step="1" value=""/>
                </p>
                <br/>
                <p>
                    <label for="pct_monto">Porcentaje a otorgar:</label>
                    <input maxlength="5" type="number" id="pct_monto" name="pct_monto" style="width:50px"
                            min="1" max="100" step="any" value=""/>
                </p>
                <br/>
                
                <p>
                    <button onclick="agregaRelacionPuntajeMonto(); return false;">Agregar</button>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <button class="simplemodal-close">Cerrar</button>
                </p>
            </form>
        </div>
                    
        <div id="div_popup_3" title="Agrega Imprimible" 
             style="display:none; width: 500px; padding: 10px;"
             class="threecolumn_each">
            <form action="" method="post" id="form_agrega_imprimible" name="form_agrega_imprimible">   
                <input type="hidden" id="mode" name="mode" value="8" />
                
                <p>
                    <label for="id_doc_imprimible">*Documento Imprimible:</label>
                    <select required id="id_doc_imprimible" name="id_doc_imprimible" style="width: 350px;">
                        <%= new CrDocImprimibleBO(user.getConn()).getCrDocImprimiblesByIdHTMLCombo(idEmpresa, -1, 0, 0, "") %>
                    </select>
                </p>
                <br/>
                
                <p>
                    <button onclick="agregaImprimible(); return false;">Agregar</button>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <button class="simplemodal-close">Cerrar</button>
                </p>
            </form>
        </div>
            
        <script>
            $("select.flexselect").flexselect();
            <% if (idCrProductoCredito<=0){ %>
            iniciaCaptura();
            <% } else if (idCrProductoPadre>0){ %>
            iniciaEdicion();
            <% } %>
        </script>
    </body>
</html>
<%}%>