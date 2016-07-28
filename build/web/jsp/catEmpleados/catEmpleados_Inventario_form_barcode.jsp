<%-- 
    Document   : catEmpleados_Inventario_form_barcode
    Created on : 15/02/2016, 12:20:23 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.bo.SGProveedorBO"%>
<%@page import="com.tsp.sct.bo.AlmacenBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="formatoMovimientoSesion" scope="session" class="com.tsp.sgfens.sesion.FormatoMovimientosSesion"/>
<%
//Verifica si el cliente tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    //Parametros
    int idEmpresa = user.getUser().getIdEmpresa();
    int idEmpleado = 0;
    try {
        idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
    } catch (NumberFormatException e) {
    }
    
    EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
    Empleado empleadoDto = null;
    if (idEmpleado > 0){
        empleadoBO = new EmpleadoBO(idEmpleado, user.getConn());
        empleadoDto = empleadoBO.getEmpleado();
    }
    
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../include/keyWordSEO.jsp" />

        <title><jsp:include page="../include/titleApp.jsp" /></title>

        <jsp:include page="../include/skinCSS.jsp" />

        <jsp:include page="../include/jsFunctions.jsp"/>
        
        <script type="text/javascript">
            
            //-------------- INICIALES
            //Usar en caso de que los select usen libreria jquery flexselect para poder usar los eventos focus,etc.. 
            var postnameFlexSelect='_flexselect';
            
            function iniciarFlexSelect(){                    
                $("select.flexselect").flexselect();
            }
            
            $(document).ready(function() {
                asignaFuncionCampoCodigoBarras();
                nuevoFormatoMovimientos();
            });
            
            function asignaFuncionCampoCodigoBarras(){
                $('#producto_codigo_barras').keypress(function (e) {
                    var key = e.which;
                    if(key === 13) { // Codigo de pulsación ENTER
                       searchAndAddProducto(0);
                       return false;  
                     }
                   }
                );  
        
                $('#producto_codigo_barras_validacion').keypress(function (e) {
                    var key = e.which;
                    if(key === 13) { // Codigo de pulsación ENTER
                       validaProducto();
                       return false;  
                     }
                   }
                );  
        
            }
            //-------------- FIN INICIALES
            
            //---------------------------------PRODUCTOS
            function searchAndAddProducto(idProducto){
                codigoBarras = $('#producto_codigo_barras').val();
                if(codigoBarras.length>0 || idProducto>0){
                    $.ajax({
                        type: "POST",
                        url: "catEmpleados_Inventario_ajax_barcode.jsp",
                        data: { mode: '1', codigo_barras : codigoBarras, id_producto : idProducto},
                            //$("#frm_action").serialize(),
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_message").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#div_ajax_productos").html(datos);
                               selectProductoLimpiar();
                               $("#ajax_loading").fadeOut("slow");
                               $("#action_buttons").fadeIn("slow");
                               recalcularTotales();
                               refrescarImagenUltimoProducto(1);
                            }else if (datos.indexOf("--MULTI-->", 0)>0){
                                selectProductoLimpiar();
                                $("#ajax_loading").fadeOut("slow");
                                $("#action_buttons").fadeIn("slow");
                                refrescarImagenUltimoProducto(0);
                                
                                seleccionaDeCoincidencias(datos);
                            }else{
                               selectProductoLimpiar();
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               $("#action_buttons").fadeIn("slow");
                               apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                               refrescarImagenUltimoProducto(0);
                           }
                        }
                    });
                }else{
                    apprise('<center><img src=../../images/warning.png> <br/><b>Código de Barras vacío.</b> <br/>Intente de nuevo o ingrese dato manualmente</center>',{'animate':true});
                }
            }
            
            var idProducto="";
            
            function seleccionaDeCoincidencias(selectOptions){
                var content= "<select id='select_coincidencias_codebar' onchange='idProducto=this.value;' style='width: 80%'>";
                content+= "<option value='-1'>Seleccione Uno...</option>";
                content += selectOptions;
                content += "</select>";

                apprise("<center>Existe mas de un producto con el mismo código de barras, seleccione uno:<br/>"+content+"</center>", {'question':true,'animate':true}, 
                    function(r) {
                        if(r) {
                            if (validaSelect(idProducto)){
                                searchAndAddProducto(idProducto);
                            }else{
                                apprise("Debe seleccionar un producto válido.", {'warning':true, 'animate':true});
                            }
                        }
                });
            }
            
            function validaSelect(valSelect){
                return valSelect>0;
            }
            
            function subProducto(indexListaProductosSesion){
                if(indexListaProductosSesion>=0){
                    $.ajax({
                        type: "POST",
                        url: "catEmpleados_Inventario_ajax_barcode.jsp",
                        data: { mode: '3', index_lista_producto : indexListaProductosSesion },
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#div_ajax_productos").html(datos);
                               $("#ajax_loading").fadeOut("slow");
                               $("#action_buttons").fadeIn("slow");
                               recalcularTotales();
                           }else{                               
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               $("#action_buttons").fadeIn("slow");
                               apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                           }
                        }
                    });
                }
            }
            
            function reiniciarListaProducto(){
                $.ajax({
                    type: "POST",
                    url: "catEmpleados_Inventario_ajax_barcode.jsp",
                    data: { mode: '2' },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#div_ajax_productos").html(datos);
                           selectProductoLimpiar();
                           $("#ajax_loading").fadeOut("slow");
                           $("#action_buttons").fadeIn("slow");
                           recalcularTotales();
                       }else{
                            $("#ajax_loading").fadeOut("slow");
                            $("#ajax_message").html(datos);
                            $("#ajax_message").fadeIn("slow");
                            $("#action_buttons").fadeIn("slow");
                            apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                       }
                    }
                });
            }
            
            function recargarListaProducto(esValidacion){
                $.ajax({
                    type: "POST",
                    url: "catEmpleados_Inventario_ajax_barcode.jsp",
                    data: { mode: '4' },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#div_ajax_productos").html(datos);
                           selectProductoLimpiar();
                           if (esValidacion){
                               selectProductoLimpiarValidacion();
                           }
                           $("#ajax_loading").fadeOut("slow");
                           $("#action_buttons").fadeIn("slow");
                           recalcularTotales();
                       }else{
                            $("#ajax_loading").fadeOut("slow");
                            $("#ajax_message").html(datos);
                            $("#ajax_message").fadeIn("slow");
                            $("#action_buttons").fadeIn("slow");
                            apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                       }
                    }
                });
            }
            
            function refrescarImagenUltimoProducto(exito){
                $.ajax({
                    type: "POST",
                    url: "catEmpleados_Inventario_ajax_barcode.jsp",
                    data: { mode: '6', exito_ultimo_producto: exito },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        //$("#ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                        //$("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                            $("#div_img_producto").html(datos);
                           //$("#ajax_loading").fadeOut("slow");
                           $("#action_buttons").fadeIn("slow");
                       }else{
                            //$("#ajax_loading").fadeOut("slow");
                            //$("#ajax_message").html(datos);
                            //$("#ajax_message").fadeIn("slow");
                            $("#action_buttons").fadeIn("slow");
                       }
                    }
                });
            }
            
            function recalcularTotales(){
                $.ajax({
                    type: "POST",
                    url: "catEmpleados_Inventario_ajax_barcode.jsp",
                    data: { mode: '7' },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                            strJSON = $.trim(datos.replace('<!--EXITO-->',''));
                            
                            //alert('respuestaJson: ' + strJSON);
                            var objeto = JSON.parse(strJSON);
                            
                            $("#txt_total_productos").val(objeto[0]);
                            $("#txt_total_precio").val(objeto[1]);
                            
                            $("#action_buttons").fadeIn("slow");
                       }else{
                            $("#action_buttons").fadeIn("slow");
                       }
                    }
                });
            }
            //---------------------------------FIN PRODUCTOS
            
            //-------------- AJUSTES
            function permitirAjuste(){
                $('input[ajuste]').each(function() { //buscamos todos los inputs con atributo ' ajuste=" '
                    $(this).attr("readonly", false);
                });
                
                $("#btn_ajuste").fadeOut("slow");
                $("#btn_aplicar_ajuste").fadeIn("slow");
                
                apprise('<center><img src=../../images/info.png> <br/> Campos de Cantidad desbloqueados para Ajuste. <br/><b>Al finalizar de click en el boton "Aplicar Ajuste"</b> </center>',{'animate':true},
                    function(r){
                        //pasamos el foco al primer input de cantidad
                        $('input[ajuste]').eq(0).focus();
                    }
                );
            }
            
            function bloquearAjuste(){
                $('input[ajuste]').each(function() { //buscamos todos los inputs con atributo ' ajuste=" '
                    $(this).attr("readonly", true);
                });
                $("#btn_ajuste").fadeIn("slow");
                $("#btn_aplicar_ajuste").fadeOut("slow");
            }
            
            function aplicarAjuste(){
                var data = $("#form_productos").serializeArray(); //serializamos Formulario
                //agregamos a datos de formulario otros manualmente
                data.push({name: 'mode', value: '8'});

                $.ajax({
                    type: "POST",
                    url: "catEmpleados_Inventario_ajax_barcode.jsp",
                    data: data,
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
                            bloquearAjuste();
                            apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                function (r){
                                    recargarListaProducto();
                                }
                            );
                        }else{
                            $("#ajax_loading").fadeOut("slow");
                            $("#ajax_message").html(datos);
                            $("#ajax_message").fadeIn("slow");
                            $("#action_buttons").fadeIn("slow");
                            apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                        }
                    }
                });
            }
            //-------------- FIN AJUSTES
            
            //-------------- VALIDACION
            
            function confirmarValidacion(){
                apprise('<center>¿Esta seguro que desea Iniciar la validación?<br/><i>En caso de validación previa, se reiniciaran los contadores</i>.</center>',
                    {'animate':true, 'confirm':true, 'textOk':'Si, Iniciar', 'textCancel':'No, Cancelar'},
                    function (r){
                        if (r){ //Boton Ok
                            iniciarValidacion();
                        }else{
                            //Boton Cancel
                        }
                    }
                );
            }
            
            function iniciarValidacion(){                
                $.ajax({
                    type: "POST",
                    url: "catEmpleados_Inventario_ajax_barcode.jsp",
                    data: { mode: '9' },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<center><img src="../../images/ajax_loader2.gif" alt="Cargando.." /><br/>Procesando...</center>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#ajax_loading").fadeOut("slow");
                           $("#action_buttons").fadeIn("slow");
                           recargarListaProducto(true);
                           $("#producto_codigo_barras").fadeOut("slow");
                           $("#producto_codigo_barras_validacion").fadeIn("slow");
                           //$("#producto_codigo_barras_validacion").focus();
                       }else{
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").fadeIn("slow");
                           $("#ajax_message").html(datos);
                           $("#action_buttons").fadeIn("slow");
                       }
                    }
                });
            }
            
            function terminarValidacion(){                
                $.ajax({
                    type: "POST",
                    url: "catEmpleados_Inventario_ajax_barcode.jsp",
                    data: { mode: '11' },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<center><img src="../../images/ajax_loader2.gif" alt="Cargando.." /><br/>Procesando...</center>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#ajax_loading").fadeOut("slow");
                           $("#action_buttons").fadeIn("slow");
                           recargarListaProducto();
                           $("#producto_codigo_barras_validacion").fadeOut("slow");
                           $("#producto_codigo_barras").fadeIn("slow");
                       }else{
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").fadeIn("slow");
                           $("#ajax_message").html(datos);
                           $("#action_buttons").fadeIn("slow");
                       }
                    }
                });
                
            }
            
            function validaProducto(){
                codigoBarras = $('#producto_codigo_barras_validacion').val();
                if(codigoBarras.length>0){
                    $.ajax({
                        type: "POST",
                        url: "catEmpleados_Inventario_ajax_barcode.jsp",
                        data: { mode: '10', codigo_barras : codigoBarras},
                            //$("#frm_action").serialize(),
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_message").fadeOut("slow");
                            //$("#ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                            //$("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#div_ajax_productos").html(datos);
                               selectProductoLimpiarValidacion();
                               //$("#ajax_loading").fadeOut("slow");
                               $("#action_buttons").fadeIn("slow");
                               refrescarImagenUltimoProducto(1);
                           }else{
                               selectProductoLimpiarValidacion();
                               //$("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               $("#action_buttons").fadeIn("slow");
                               apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                               refrescarImagenUltimoProducto(0);
                           }
                        }
                    });
                }else{
                    apprise('<center><img src=../../images/warning.png> <br/><b>Código de Barras vacío.</b> <br/>Intente de nuevo o ingrese dato manualmente</center>',{'animate':true});
                }
            }
            
            //-------------- FIN VALIDACION
            
            //----------- GENERAL
            function selectProductoLimpiar(){                
                $('#producto_codigo_barras').val('');
                $('#producto_codigo_barras').focus();
            }
            
            function selectProductoLimpiarValidacion(){                
                $('#producto_codigo_barras_validacion').val('');
                $('#producto_codigo_barras_validacion').focus();
            }
            
            function nuevoFormatoMovimientos(){
                $.ajax({
                    type: "POST",
                    url: "catEmpleados_Inventario_ajax_barcode.jsp",
                    data: { mode: '5' },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<center><img src="../../images/ajax_loader2.gif" alt="Cargando.." /><br/>Procesando...</center>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#ajax_loading").fadeOut("slow");
                           $("#action_buttons").fadeIn("slow");
                           recargarListaProducto();
                       }else{
                           $("#ajax_loading").html(datos);
                           $("#action_buttons").fadeIn("slow");
                       }
                    }
                });
            }
            
            function guardaMovimientos(){
                
                var data = $("#form_generales").serializeArray(); //serializamos Formulario
                //agregamos a datos de formulario otros manualmente
                data.push({name: 'mode', value: '15'});
                
                $.ajax({
                    type: "POST",
                    url: "catEmpleados_Inventario_ajax_barcode.jsp",
                    data: data,
                    beforeSend: function(objeto){
                        $("#ajax_message").fadeOut("slow");
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<center><img src="../../images/ajax_loader2.gif" alt="Cargando.." /><br/>Procesando...</center>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                            $("#ajax_message").html(datos);
                            $("#ajax_loading").fadeOut("slow");
                            $("#ajax_message").fadeIn("slow");
                            
                            apprise('<center>' + datos + '<br/><br/> ¿Desea guardar y repetir posteriormente de forma automática, esta Sesión de Asignación?.</center>',
                                {'animate':true, 'confirm':true, 'textOk':'No, continuar', 'textCancel':'Si, guardar'},
                                function (r){
                                    if (r){ //Boton Ok / Tecla Enter
                                        location.href = "catEmpleados_Inventario_list.jsp?idEmpleado=<%= idEmpleado %>";
                                    }else{
                                        //Boton Cancel - Tecla ESC
                                        preguntaDiasAutomatizacion();
                                    }
                                }
                            );
                    
                        }else{
                            $("#ajax_loading").fadeOut("slow");
                            $("#ajax_message").html(datos);
                            $("#ajax_message").fadeIn("slow");
                            $("#action_buttons").fadeIn("slow");
                            $.scrollTo('#inner',800);
                            //apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                        }
                    }
                });
            }
            
            var numDias = 0;
            function preguntaDiasAutomatizacion(){
                var content= "<input type='text' id='num_dias_input' onchange='numDias=this.value;' onkeypress='return validateNumber(event);' style='width: 50px' >";

                apprise("<center>Indique cada cuantos días se debe repetir la asignación (número entero):<br/>"+content+"</center>", {'question':true,'animate':true}, 
                    function(r) {
                        if(r) {
                            if (numDias>0){
                                guardaSesionAsignacion(numDias);
                            }else{
                                apprise('<center>No se específico un valor válido, No se almacenará la Sesión.</center>',{'animate':true, 'warning':true},
                                    function(r){
                                        location.href = "catEmpleados_Inventario_list.jsp?idEmpleado=<%= idEmpleado %>";
                                });
                            }
                        }
                });
            }
            
            function guardaSesionAsignacion(numDias){
                
                var data = $("#form_generales").serializeArray(); //serializamos Formulario
                //agregamos a datos de formulario otros manualmente
                data.push({name: 'mode', value: '16'});
                data.push({name: 'num_dias', value: numDias});
                
                $.ajax({
                    type: "POST",
                    url: "catEmpleados_Inventario_ajax_barcode.jsp",
                    data: data,
                    beforeSend: function(objeto){
                        $("#ajax_message").fadeOut("slow");
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<center><img src="../../images/ajax_loader2.gif" alt="Cargando.." /><br/>Procesando...</center>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                            $("#ajax_message").html(datos);
                            $("#ajax_loading").fadeOut("slow");
                            $("#ajax_message").fadeIn("slow");
                            apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                function(r){
                                    location.href = "catEmpleados_Inventario_list.jsp?idEmpleado=<%= idEmpleado %>";
                                });
                        }else{
                            $("#ajax_loading").fadeOut("slow");
                            $("#ajax_message").html(datos);
                            $("#ajax_message").fadeIn("slow");
                            //$("#action_buttons").fadeIn("slow");
                            //$.scrollTo('#inner',800);
                            apprise('<center>ERROR, no se pudo guardar la sesión. <br/><br/>'+ datos +'</center>',{'animate':true, 'warning':true},
                                function(r){
                                    location.href = "catEmpleados_Inventario_list.jsp?idEmpleado=<%= idEmpleado %>";
                                });
                        }
                    }
                });
            }
            //----------- FIN GENERAL
            
        </script>
    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Pretoriano Móvil - Asignación a Empleado</h1>
                    
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                Captura de Producto
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <p>
                                <label>Código de Barras:</label>
                                <input type="text" name="producto_codigo_barras" id="producto_codigo_barras"
                                       value=""
                                       style="width: 300px;"/>
                                <input type="text" name="producto_codigo_barras_validacion" id="producto_codigo_barras_validacion"
                                       value=""
                                       style="width: 300px; display: none;"/>
                                <img src="../../images/icon_barras.png" alt="Lea Código de Barras" title="Compatible con Lector Código de Barras" class="help" />
                            </p>
                        </div>
                    </div>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>
                    
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                Productos por Asignar
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            
                            <form action="" method="post" id="form_productos" name="form_productos">
                            <!-- Imagen y Listado de productos -->
                            <div id="wrapper" style="overflow-y: auto; height: 250px; max-height:250px; display: block; width: 100%;">
                                <!-- imagen de producto -->
                                <div id="div_img_producto" class="column-1" style="width: 20%">
                                    <img src="../../images/icon_producto_no_encontrado.png" width="200" height="200" 
                                        id="img_producto" name="img_producto" />
                                </div>
                                <!-- contenido de Productos leídos  -->
                                <div id="div_ajax_productos" style="width: 95%; vertical-align: top;" class="column-2">
                                    <table class="data" width="100%" cellpadding="0" cellspacing="0" style="vertical-align: top;">
                                        <thead>
                                            <tr>
                                                <td>Cantidad</td>
                                                <td>Artículo</td>
                                                <td>Descripción</td>
                                                <td>Precio Venta</td>
                                                <td>Total</td>
                                                <td>Código</td>
                                                <td>Validación</td>
                                                <td>Acciones</td>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <!-- FIN contenido de Productos leídos  -->
                                
                            </div>
                            <!-- FIN Imagen y Listado de productos -->
                            </form>
                            
                            <br class="clear"/>
                            
                            <!-- Acciones y Totales -->
                            <form action="" method="post" id="form_generales" name="form_generales">
                            <input type="hidden" id="id_empleado" name="id_empleado" value="<%= empleadoDto!=null?empleadoDto.getIdEmpleado():"-1" %>" />
                            <table style="width: 100%; ">
                                <tbody>
                                    <tr>
                                        <td style="text-align: right; padding: 5px; vertical-align: bottom;">
                                            <input type="button" id="btn_ajuste" name="btn_ajuste" value="Ajuste" 
                                                style="width: 100px;" onclick="permitirAjuste();"/>
                                            <input type="button" id="btn_aplicar_ajuste" name="btn_aplicar_ajuste" value="Aplicar Ajuste" 
                                                style="width: 150px; display: none;" onclick="aplicarAjuste();"/>
                                        </td>
                                        <td style="text-align: right; padding: 5px; vertical-align: bottom;">
                                            <input type="button" id="btn_validacion" name="btn_validacion" value="Validación" 
                                                style="width: 100px;" onclick="confirmarValidacion();"/>
                                        </td>
                                        <td style="text-align: right; padding: 5px; vertical-align: bottom;">
                                            <label>Cantidad de Productos:</label>
                                            <input type="text" maxlength="16" name="txt_total_productos" id="txt_total_productos"
                                                style="text-align: right;" size="8" value="0" readonly/>
                                        </td>
                                        <td style="text-align: right; padding: 5px;  vertical-align: bottom;">
                                            <label>Total Precio Venta: $</label>
                                            <input type="text" maxlength="16" name="txt_total_precio" id="txt_total_precio"
                                                style="text-align: right;" size="8" value="0" readonly/>
                                        </td>
                                    </tr>
                                    <tr id="tr_datos_mov_general">
                                        <td colspan="2" style="padding: 5px;">
                                            <label>*Empleado:</label><br/>
                                            <input type="text" id="nombreEmpleadoAsignarInventario" name="nombreEmpleadoAsignarInventario" style="width:98%"
                                               value="<%=empleadoDto!=null?empleadoDto.getNombre()+" "+empleadoDto.getApellidoPaterno():"ERROR: NO INGRESADO" %>"
                                               readonly />
                                        </td>
                                        <td colspan="2" style="padding: 5px; ">
                                            <label>*Almacén:</label> <br/>
                                            <select size="1" id="idAlmacen" name="idAlmacen" style="width: 100%" >
                                                <option value="-1">Selecciona un Almacén</option>
                                                    <%
                                                        out.print(new AlmacenBO(user.getConn()).getAlmacenesByIdHTMLCombo(idEmpresa, -1));
                                                    %>
                                            </select>   
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="text-align: left;">
                                            <br/><br/>
                                            <input type="button" id="btn_guardar" name="btn_guardar" value="Guardar" 
                                                style="width: 90%; height: 40px;" onclick="guardaMovimientos();"/>
                                        </td>
                                        <td colspan="5"></td>
                                    </tr>
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
            
        <script>           
           iniciarFlexSelect();  
           selectProductoLimpiar();
        </script>
    </body>
</html>
<%
}
%>