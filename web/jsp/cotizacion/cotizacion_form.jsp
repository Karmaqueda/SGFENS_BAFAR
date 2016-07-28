<%-- 
    Document   : cotizacion_form
    Created on : 22-nov-2012, 19:18:28
    Author     : ISCesarMartinez
--%>

<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProspecto"%>
<%@page import="com.tsp.sct.bo.ServicioBO"%>
<%@page import="com.tsp.sct.bo.ImpuestoBO"%>
<%@page import="com.tsp.sct.bo.UnidadBO"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensCotizacion"%>
<%@page import="com.tsp.sct.bo.SGCotizacionBO"%>
<%@page import="com.tsp.sct.bo.SGProspectoBO"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="cotizacionSesion" scope="session" class="com.tsp.sgfens.sesion.CotizacionSesion"/>
<%
//Verifica si el proveedor tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {

        int idEmpresa = user.getUser().getIdEmpresa();
        
        /*
         * Parámetros
         */
        int idCotizacion = 0;
        try {
            idCotizacion = Integer.parseInt(request.getParameter("idCotizacion"));
        } catch (NumberFormatException e) {
        }

        /*
         *   1 = nuevo
         *   2 = editar/consultar
         *   
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        
        SGCotizacionBO cotizacionBO = new SGCotizacionBO(user.getConn());
        SgfensCotizacion cotizacionDto = null;
        
        Cliente clienteDto = null;
        SgfensProspecto prospectoDto = null;
        if (idCotizacion > 0){
            cotizacionBO = new SGCotizacionBO(idCotizacion,user.getConn());
            cotizacionDto = cotizacionBO.getCotizacion();
            
            clienteDto = new ClienteBO(cotizacionDto!=null?cotizacionDto.getIdCliente():-1,user.getConn()).getCliente();
            prospectoDto = new SGProspectoBO(cotizacionDto!=null?cotizacionDto.getIdProspecto():-1,user.getConn()).getSgfensProspecto();
        }
        
        ConceptoBO conceptoBO = new ConceptoBO(user.getConn());

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
            
            //Usar en caso de que los select usen libreria jquery flexselect para poder usar los eventos focus,etc.. 
            var postnameFlexSelect='_flexselect';
            
            function iniciarFlexSelect(){
                $("#id_cliente").flexselect({
                    jsFunction:  function(id) { selectCliente(id); }
                });
                
                $("#id_prospecto").flexselect({
                    jsFunction:  function(id) { selectProspecto(id); }
                });

                $("#producto_select").flexselect({
                    jsFunction:  function(id) { selectProducto(id); }
                });

                $("#servicio_select").flexselect({
                    jsFunction:  function(id) { selectServicio(id); }
                });

                $("select.flexselect").flexselect();
            }
            
            //****---------------------------------------INICIALIZACION
            
            $(document).ready(function() {
                recargarSelectClientes();
                recargarSelectProspectos();
                
                recuperarListados();
                <%=(mode.equals("1")?"nuevaCotizacion();":"")%>
            });
            
            function recuperarListados(){
                recargarListaProducto();
                recargarListaServicio();
                recargarListaImpuestos();
            }
            
            function recalcularTotales(){
                recargarListaImpuestos();
                calculaDescuento();
                //calculaTotal();
            }
            
            function activarFancyBox(){
                $('.modalbox_iframe').fancybox({
                        padding: 0, 
                        titleShow: false, 
                        overlayColor: '#333333', 
                        overlayOpacity: .5,
                        'type': 'iframe',
                        'autoScale':   true
                });
            }
            
            function nuevaCotizacion(){
                $.ajax({
                    type: "POST",
                    url: "cotizacion_ajax.jsp",
                    data: { mode: '6' },
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
            
            //****--------------------------------------- FIN INICIALIZACION
            
            //****---------------------------------GUARDAR
            function enviarYgrabar(){
                if(validar(0)){
                    var correoCliente = $("#cliente_correo").val();
                    var correoProspecto = $("#prospecto_correo").val();
                    var correos = correoCliente+','+correoProspecto;
                    
                    apprise("¿Correos electrónicos a los que se enviará la cotización (separar por coma ,)?", 
                        {'input':correos, 'animate':true}, function(r){
                            
                            if(r) {
                                //alert (r);
                                 grabar(r);
                            }
                            
                    });
                }
            }
            
            function grabar(listaCorreos){
                
                idCliente = $("#id_cliente").val();
                idProspecto = $("#id_prospecto").val();
                descuentoMotivo = $("#descuento_motivo").val();
                coment = $("#comentarios").val();
                tipoMoneda = $("#tipo_moneda").val();
                if(validar(0)){
                    $.ajax({
                        type: "POST",
                        url: "cotizacion_ajax.jsp",
                        data: { mode: '19' , id_cliente : idCliente,
                                id_prospecto :  idProspecto, descuento_motivo : descuentoMotivo,
                                comentarios : coment, tipo_moneda : tipoMoneda , 
                                lista_correos : listaCorreos},
                            //$("#frm_action").serialize(),
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<center><img src="../../images/ajax_loader2.gif" alt="Cargando.." /><br/>Procesando...</center>');
                            $("#ajax_loading").fadeIn("slow");
                            $.scrollTo('#inner',800);
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_message").html(datos);
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                    function(r){
                                        location.href="../cotizacion/cotizacion_list.jsp";
                                });
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
            
            function validar(mode){
                if(jQuery.trim($("#id_cliente").val())<=0 && jQuery.trim($("#id_prospecto").val())<=0){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "Cliente" o "Prospecto" es requerido</center>',{'animate':true});
                    return false;
                }
                if(jQuery.trim($("#id_cliente").val())>0 && jQuery.trim($("#id_prospecto").val())>0){
                    apprise('<center><img src=../../images/warning.png> <br/>Seleccione solo una de las 2 opciones: Cliente o Prospecto, no pueden ser los 2 al mismo tiempo.</center>',{'animate':true});
                    return false;
                }
                return true;
            }
            
            //****---------------------------------FIN GUARDAR
            
            
            //****---------------------------------PRODUCTOS
            
            function selectProductoFromPopupCategoria(idProducto, nombreProducto){
                //alert('Producto seleccionado de categorias: ' + idProducto + " " + nombreProducto);
                selectProducto(idProducto);
                
                //Como se usa flexselect se usan las siguientes 2 opciones
                $("#producto_select").val(idProducto);
                $('#producto_select'+postnameFlexSelect).val(nombreProducto);
                //En caso de no usa flexselect, y ser un select básico se usa la siguiente opción
                //$("#producto_select option[value="+idProducto+"]").attr("selected",true);
            }
            
            function selectProducto(idProducto){
                //alert("idProducto: " + idProducto);
                if(idProducto>0){
                    idCliente = $("#id_cliente").val();
                    idProspecto = $("#id_prospecto").val();
                    if(idCliente>0 || idProspecto>0 ){
                        $.ajax({
                            type: "POST",
                            url: "cotizacion_ajax.jsp",
                            data: { mode: '1', id_producto_select: idProducto,idCliente : idCliente,idProspecto:idProspecto },
                                //$("#frm_action").serialize(),
                            beforeSend: function(objeto){
                                $("#action_buttons").fadeOut("slow");
                                $("#div_producto_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                                $("#div_producto_ajax_loading").fadeIn("slow");
                            },
                            success: function(datos){
                                if(datos.indexOf("--EXITO-->", 0)>0){
                                   strJSON = $.trim(datos.replace('<!--EXITO-->',''));

                                   arrayRespuesta=eval('(' + strJSON + ')');
                                   //$('#producto_unidad').val(arrayRespuesta["unidadProductoCompra"]);
                                   //$('#producto_precio').val(arrayRespuesta["precio"]);
                                   $('#producto_descripcion').val(arrayRespuesta["descripcion"]);

                                   var options_precio = '';

                                   //creamos opciones para combo de precios
                                   //options_precio += '<option value="0"></option>';
                                   options_precio += '<option value="' + arrayRespuesta["precio"] + '">' + arrayRespuesta["precio"] + ' [men.]</option>';
                                   options_precio += '<option value="' + arrayRespuesta["precioMayoreo"] + '">' + arrayRespuesta["precioMayoreo"] + ' [may.]</option>';
                                   options_precio += '<option value="' + arrayRespuesta["precioDocena"] + '">' + arrayRespuesta["precioDocena"] + ' [docena]</option>';
                                   options_precio += '<option value="' + arrayRespuesta["precioEspecial"] + '">' + arrayRespuesta["precioEspecial"] + ' [especial]</option>';

                                   //alert('-->' + options_precio);

                                   $("#producto_precio").html(options_precio);

                                   selectProductoLimpiar();
                                   $("#div_producto_ajax_loading").fadeOut("slow");
                                   $("#action_buttons").fadeIn("slow");
                               }else{
                                   $("#div_producto_ajax_loading").html(datos);
                                   $("#action_buttons").fadeIn("slow");
                               }
                            }
                        });
                    }else{
                         apprise('<center><img src=../../images/warning.png> <br/>'+ "Selecciona primero un Cliente o Prospecto" +'</center>',{'animate':true});
                         $('#producto_select'+postnameFlexSelect).val('');
                    }
                }
            }
            function selectProductoLimpiar(){
                $('#producto_cantidad').val('');
                $('#producto_monto').val('');
                $('#unidad_select'+postnameFlexSelect).val('');
                //$('#unidad_select'+postnameFlexSelect).focus();
            }
            function selectProductoCalcularMonto(){
                var cantidad = getFloatFromInput($('#producto_cantidad'));
                var precio = getFloatFromInput($('#producto_precio'));
                var monto = (precio * cantidad).toFixed(2);
                //alert('precio: ' + precio + ' cantidad: ' + cantidad + ' monto: ' + monto);
                $('#producto_monto').val(monto);
            }
            
            function addProducto(){
                productoId = $('#producto_select').val();
                unidadId = $('#unidad_select').val();
                unidadText = $('#unidad_select'+postnameFlexSelect).val();
                //unidadText = $('#unidad_select option:selected').text();
                    //alert('unidad ' + unidadText);
                productoPrecio = $('#producto_precio').val();
                productoCantidad = $('#producto_cantidad').val();
                productoMonto = $('#producto_monto').val();
                if (validarAddProducto(productoId,productoCantidad,productoMonto,unidadId)){
                    if(productoId>0){
                        $.ajax({
                            type: "POST",
                            url: "cotizacion_ajax.jsp",
                            data: { mode: '2', producto_id : productoId, 
                                producto_cantidad : productoCantidad, producto_monto : productoMonto,
                                producto_precio : productoPrecio, producto_unidad: unidadText, unidad_id: unidadId},
                                //$("#frm_action").serialize(),
                            beforeSend: function(objeto){
                                $("#action_buttons").fadeOut("slow");
                                $("#div_producto_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                                $("#div_producto_ajax_loading").fadeIn("slow");
                            },
                            success: function(datos){
                                if(datos.indexOf("--EXITO-->", 0)>0){
                                   $("#div_ajax_productos").html(datos);
                                   selectProductoLimpiar();
                                   $("#div_producto_ajax_loading").fadeOut("slow");
                                   $("#action_buttons").fadeIn("slow");
                                   recalcularTotales();
                               }else{
                                   $("#div_producto_ajax_loading").html(datos);
                                   $("#action_buttons").fadeIn("slow");
                               }
                            }
                        });
                    }
                }
            }
            function validarAddProducto(idProducto,cantidad,monto, idUnidad){
                if(idProducto<=0 || cantidad<0 || monto<0 || idUnidad<=0
                    || cantidad=="" || monto==""){
                    apprise('<center><img src=../../images/warning.png> <br/>Llene todos los datos requeridos para agregar el producto. Valores mayores a 0</center>',{'animate':true});
                    return false;
                }
                return true;
            }
            
            function subProducto(indexListaProductosSesion){
                if(indexListaProductosSesion>=0){
                    $.ajax({
                        type: "POST",
                        url: "cotizacion_ajax.jsp",
                        data: { mode: '4', index_lista_producto : indexListaProductosSesion },
                            //$("#frm_action").serialize(),
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#div_producto_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                            $("#div_producto_ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#div_ajax_productos").html(datos);
                               //selectProductoLimpiar();
                               $("#div_producto_ajax_loading").fadeOut("slow");
                               $("#action_buttons").fadeIn("slow");
                               recalcularTotales();
                           }else{
                               $("#div_producto_ajax_loading").html(datos);
                               $("#action_buttons").fadeIn("slow");
                           }
                        }
                    });
                }
            }
            
            function reiniciarListaProducto(){
                $.ajax({
                    type: "POST",
                    url: "cotizacion_ajax.jsp",
                    data: { mode: '3' },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#div_producto_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                        $("#div_producto_ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#div_ajax_productos").html(datos);
                           selectProductoLimpiar();
                           $("#div_producto_ajax_loading").fadeOut("slow");
                           $("#action_buttons").fadeIn("slow");
                           recalcularTotales();
                       }else{
                           $("#div_producto_ajax_loading").html(datos);
                           $("#action_buttons").fadeIn("slow");
                       }
                    }
                });
            }
            
            function recargarListaProducto(){
                $.ajax({
                    type: "POST",
                    url: "cotizacion_ajax.jsp",
                    data: { mode: '5' },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#div_producto_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                        $("#div_producto_ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#div_ajax_productos").html(datos);
                           selectProductoLimpiar();
                           $("#div_producto_ajax_loading").fadeOut("slow");
                           $("#action_buttons").fadeIn("slow");
                           recalcularTotales();
                       }else{
                           $("#div_producto_ajax_loading").html(datos);
                           $("#action_buttons").fadeIn("slow");
                       }
                    }
                });
            }
            //****---------------------------------FIN PRODUCTOS
            
            //****---------------------------------SERVICIOS
            function selectServicio(idServicio){
                //alert("idServicio: " + idServicio);
                if(idServicio>0){
                    $.ajax({
                        type: "POST",
                        url: "cotizacion_ajax.jsp",
                        data: { mode: '13', id_servicio_select: idServicio },
                            //$("#frm_action").serialize(),
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#div_servicio_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                            $("#div_servicio_ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               strJSONServicio = $.trim(datos.replace('<!--EXITO-->',''));
                               
                               arrayRespuestaServicio=eval('(' + strJSONServicio + ')');
                               //$('#servicio_unidad').val(arrayRespuesta["unidadServicioCompra"]);
                               //$('#servicio_precio').val(arrayRespuesta["precio"]);
                               
                               $('#servicio_descripcion').val(arrayRespuestaServicio["descripcion"]);
                               
                               var options_precio = '';
                               
                               //creamos opciones para combo de precios
                               //options_precio += '<option value="0"></option>';
                               options_precio += '<option selected value="' + arrayRespuestaServicio["precio"] + '">' + arrayRespuestaServicio["precio"] + ' </option>';
                               //options_precio += '<option value="' + arrayRespuesta["precioMayoreo"] + '">' + arrayRespuesta["precioMayoreo"] + ' [may.]</option>';
                               //options_precio += '<option value="' + arrayRespuesta["precioDocena"] + '">' + arrayRespuesta["precioDocena"] + ' [docena]</option>';
                               //options_precio += '<option value="' + arrayRespuesta["precioEspecial"] + '">' + arrayRespuesta["precioEspecial"] + ' [especial]</option>';
                               
                               //alert('-->' + options_precio);
                               
                               $("#servicio_precio").html(options_precio);
                               
                               selectServicioLimpiar();
                               $("#div_servicio_ajax_loading").fadeOut("slow");
                               $("#action_buttons").fadeIn("slow");
                           }else{
                               $("#div_servicio_ajax_loading").html(datos);
                               $("#action_buttons").fadeIn("slow");
                           }
                        }
                    });
                }
            }
            function selectServicioLimpiar(){
                $('#servicio_cantidad').val('');
                $('#servicio_monto').val('');
                //$('#unidad_select'+postnameFlexSelect).val('');
            }
            function selectServicioCalcularMonto(){
                var cantidad = getFloatFromInput($('#servicio_cantidad'));
                var precio = getFloatFromInput($('#servicio_precio'));
                var monto = (precio * cantidad).toFixed(2);
                //alert('precio: ' + precio + ' cantidad: ' + cantidad + ' monto: ' + monto);
                $('#servicio_monto').val(monto);
            }
            
            function addServicio(){
                servicioId = $('#servicio_select').val();
                //unidadId = $('#unidad_select').val();
                //unidadText = $('#unidad_select'+postnameFlexSelect).val();
                //unidadText = $('#unidad_select option:selected').text();
                    //alert('unidad ' + unidadText);
                servicioPrecio = $('#servicio_precio').val();
                servicioCantidad = $('#servicio_cantidad').val();
                servicioMonto = $('#servicio_monto').val();
                if (validarAddServicio(servicioId,servicioCantidad,servicioMonto)){
                    if(servicioId>0){
                        $.ajax({
                            type: "POST",
                            url: "cotizacion_ajax.jsp",
                            data: { mode: '14', servicio_id : servicioId, 
                                servicio_cantidad : servicioCantidad, servicio_monto : servicioMonto,
                                servicio_precio : servicioPrecio},
                                //$("#frm_action").serialize(),
                            beforeSend: function(objeto){
                                $("#action_buttons").fadeOut("slow");
                                $("#div_servicio_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                                $("#div_servicio_ajax_loading").fadeIn("slow");
                            },
                            success: function(datos){
                                if(datos.indexOf("--EXITO-->", 0)>0){
                                   $("#div_ajax_servicios").html(datos);
                                   selectServicioLimpiar();
                                   $("#div_servicio_ajax_loading").fadeOut("slow");
                                   $("#action_buttons").fadeIn("slow");
                                   recalcularTotales();
                               }else{
                                   $("#div_servicio_ajax_loading").html(datos);
                                   $("#action_buttons").fadeIn("slow");
                               }
                            }
                        });
                    }
                }
            }
            function validarAddServicio(idServicio,cantidad,monto){
                if(idServicio<=0 || cantidad<0 || monto<0
                    || cantidad=="" || monto==""){
                    apprise('<center><img src=../../images/warning.png> <br/>Llene todos los datos requeridos para agregar el servicio. Valores mayores a 0</center>',{'animate':true});
                    return false;
                }
                return true;
            }
            
            function subServicio(indexListaServiciosSesion){
                if(indexListaServiciosSesion>=0){
                    $.ajax({
                        type: "POST",
                        url: "cotizacion_ajax.jsp",
                        data: { mode: '16', index_lista_servicio : indexListaServiciosSesion },
                            //$("#frm_action").serialize(),
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#div_servicio_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                            $("#div_servicio_ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#div_ajax_servicios").html(datos);
                               //selectServicioLimpiar();
                               $("#div_servicio_ajax_loading").fadeOut("slow");
                               $("#action_buttons").fadeIn("slow");
                               recalcularTotales();
                           }else{
                               $("#div_servicio_ajax_loading").html(datos);
                               $("#action_buttons").fadeIn("slow");
                           }
                        }
                    });
                }
            }
            
            function reiniciarListaServicio(){
                $.ajax({
                    type: "POST",
                    url: "cotizacion_ajax.jsp",
                    data: { mode: '15' },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#div_servicio_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                        $("#div_servicio_ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#div_ajax_servicios").html(datos);
                           selectServicioLimpiar();
                           $("#div_servicio_ajax_loading").fadeOut("slow");
                           $("#action_buttons").fadeIn("slow");
                           recalcularTotales();
                       }else{
                           $("#div_servicio_ajax_loading").html(datos);
                           $("#action_buttons").fadeIn("slow");
                       }
                    }
                });
            }
            
            function recargarListaServicio(){
                $.ajax({
                    type: "POST",
                    url: "cotizacion_ajax.jsp",
                    data: { mode: '17' },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#div_servicio_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                        $("#div_servicio_ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#div_ajax_servicios").html(datos);
                           selectServicioLimpiar();
                           $("#div_servicio_ajax_loading").fadeOut("slow");
                           $("#action_buttons").fadeIn("slow");
                           recalcularTotales();
                       }else{
                           $("#div_servicio_ajax_loading").html(datos);
                           $("#action_buttons").fadeIn("slow");
                       }
                    }
                });
            }
            //****---------------------------------FIN SERVICIOS
            
            
            //****----------------------------------IMPUESTOS
            function addImpuesto(){
                impuestoId = $('#impuesto_select').val();
                
                if (validarAddImpuesto(impuestoId)){
                    if(impuestoId>0){
                        $.ajax({
                            type: "POST",
                            url: "cotizacion_ajax.jsp",
                            data: { mode: '9', impuesto_id : impuestoId},
                            beforeSend: function(objeto){
                                $("#action_buttons").fadeOut("slow");
                                $("#div_impuesto_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                                $("#div_impuesto_ajax_loading").fadeIn("slow");
                            },
                            success: function(datos){
                                if(datos.indexOf("--EXITO-->", 0)>0){
                                   $("#div_ajax_impuesto").html(datos);
                                   $("#div_impuesto_ajax_loading").fadeOut("slow");
                                   $("#action_buttons").fadeIn("slow");
                                   calculaTotal();
                               }else{
                                   $("#div_impuesto_ajax_loading").html(datos);
                                   $("#action_buttons").fadeIn("slow");
                               }
                            }
                        });
                    }
                }
            }
            function validarAddImpuesto(idImpuesto){
                if(idImpuesto<=0){
                    apprise('<center><img src=../../images/warning.png> <br/>Seleccione un impuesto válido.</center>',{'animate':true});
                    return false;
                }
                return true;
            }
            
            function subImpuesto(indexListaImpuestosSesion){
                if(indexListaImpuestosSesion>=0){
                    $.ajax({
                        type: "POST",
                        url: "cotizacion_ajax.jsp",
                        data: { mode: '10', index_lista_impuesto : indexListaImpuestosSesion },
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#div_impuesto_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                            $("#div_impuesto_ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#div_ajax_impuesto").html(datos);
                               $("#div_impuesto_ajax_loading").fadeOut("slow");
                               $("#action_buttons").fadeIn("slow");
                               calculaTotal();
                           }else{
                               $("#div_impuesto_ajax_loading").html(datos);
                               $("#action_buttons").fadeIn("slow");
                           }
                        }
                    });
                }
            }            
            
            function recargarListaImpuestos(){
                $.ajax({
                    type: "POST",
                    url: "cotizacion_ajax.jsp",
                    data: { mode: '11' },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#div_impuesto_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                        $("#div_impuesto_ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#div_ajax_impuesto").html(datos);
                           $("#div_impuesto_ajax_loading").fadeOut("slow");
                           $("#action_buttons").fadeIn("slow");
                           calculaTotal();
                       }else{
                           $("#div_impuesto_ajax_loading").html(datos);
                           $("#action_buttons").fadeIn("slow");
                       }
                    }
                });
            }
            
            //****----------------------------------FIN IMPUESTOS
            
            function calculaDescuento(){
                var tasaDescuento = getFloatFromInput($('#descuento_tasa'));
                if (tasaDescuento>100){
                    tasaDescuento = 0;
                    $('#descuento_tasa').val(tasaDescuento);
                    alert('La tasa de descuento no puede ser mayor a 100%');
                }
                
                if(tasaDescuento>=0){
                    $.ajax({
                        type: "POST",
                        url: "cotizacion_ajax.jsp",
                        data: { mode: '12', descuento_tasa : tasaDescuento },
                            //$("#frm_action").serialize(),
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#div_descuento_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                            $("#div_descuento_ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               datos = $.trim(datos.replace('<!--EXITO-->',''));
                               //alert(datos);
                               $('#descuento_importe').val(datos);
                               //selectProductoLimpiar();
                               $("#div_descuento_ajax_loading").fadeOut("slow");
                               $("#action_buttons").fadeIn("slow");
                               recargarListaImpuestos();
                               calculaTotal();
                           }else{
                               $("#div_descuento_ajax_loading").html(datos);
                               $("#action_buttons").fadeIn("slow");
                           }
                        }
                    });
                }
            }
            
            
            function calculaTotal(){
                var total=0;             
                //var subtotalServicios = parseFloat(0);
                
                //Calculamos subtotal (productos + servicios)
                var subtotalProductos = getFloatFromInput($('#producto_subtotal'));
                var subtotalServicios = getFloatFromInput($('#servicio_subtotal'));
                
                var subtotal = (parseFloat(subtotalProductos) + parseFloat(subtotalServicios)).toFixed(2);
                
                var importeDescuento = getFloatFromInput($('#descuento_importe'));
                var subtotalImpuestos_traslados = getFloatFromInput($('#subtotalImpuestos_traslados'));
                var subtotalImpuestos_retenciones = getFloatFromInput($('#subtotalImpuestos_retenciones'));
                //alert('subtotal prod ' + subtotalProductos);
                
                //Total = Subtotal – Descuentos + Impuestos Trasladados – Impuestos Retenidos.
                total = ((subtotal) 
                    - parseFloat(importeDescuento) 
                    + parseFloat(subtotalImpuestos_traslados) 
                    - parseFloat(subtotalImpuestos_retenciones) ).toFixed(2);
                //alert('total ' + total);
                $('#total').val(total);
            }
            
            
            
            //****-----------------ACCIONES DE SELECCION CLIENTE Y PROSPECTO
            
            //Recarga de BD dinámicamente el listado de clientes de la empresa
            function recargarSelectClientes(){
                $.ajax({
                    type: "POST",
                    url: "cotizacion_ajax.jsp",
                    data: { mode: '20', id_cliente : <%= cotizacionDto!=null?cotizacionDto.getIdCliente():-1 %> },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                            $("#div_select_cliente").html(datos);
                            //selectProductoLimpiar();
                            $("#ajax_loading").fadeOut("slow");
                            $("#action_buttons").fadeIn("slow");
                            iniciarFlexSelect();
                        }else{
                            $("#ajax_loading").html(datos);
                            $("#action_buttons").fadeIn("slow");
                        }
                    }
                });
            }
            
            //Recarga de BD dinámicamente el listado de prospectos de la empresa
            function recargarSelectProspectos(){
                $.ajax({
                    type: "POST",
                    url: "cotizacion_ajax.jsp",
                    data: { mode: '21', id_prospecto : <%= cotizacionDto!=null?cotizacionDto.getIdProspecto():-1 %> },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                            $("#div_select_prospecto").html(datos);
                            //selectProductoLimpiar();
                            $("#ajax_loading").fadeOut("slow");
                            $("#action_buttons").fadeIn("slow");
                            iniciarFlexSelect();
                        }else{
                            $("#ajax_loading").html(datos);
                            $("#action_buttons").fadeIn("slow");
                        }
                    }
                });
            }
            
            function selectCliente(idCliente){
                //alert('ID cliente elegido: ' + idCliente);
                if(idCliente>0){
                    $.ajax({
                        type: "POST",
                        url: "cotizacion_ajax.jsp",
                        data: { mode: '22', id_cliente : idCliente },
                            //$("#frm_action").serialize(),
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               strJSONCliente = $.trim(datos.replace('<!--EXITO-->',''));
                               
                               arrayRespuestaCliente=eval('(' + strJSONCliente + ')');
                               
                               $('#cliente_correo').val(arrayRespuestaCliente["correo"]);
                               
                               $("#ajax_loading").fadeOut("slow");
                               $("#action_buttons").fadeIn("slow");
                           }else{
                               $("#ajax_loading").html(datos);
                               $("#action_buttons").fadeIn("slow");
                           }
                        }
                    });
                }else{
                    $('#cliente_correo').val('');
                }
            }
            
            function selectProspecto(idProspecto){
                //alert('ID prospecto elegido: ' + idProspecto);
                if(idProspecto>0){
                    $.ajax({
                        type: "POST",
                        url: "cotizacion_ajax.jsp",
                        data: { mode: '23', id_prospecto : idProspecto },
                            //$("#frm_action").serialize(),
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               strJSONProspecto = $.trim(datos.replace('<!--EXITO-->',''));
                               
                               arrayRespuestaProspecto=eval('(' + strJSONProspecto + ')');
                               
                               $('#prospecto_correo').val(arrayRespuestaProspecto["correo"]);
                               
                               $("#ajax_loading").fadeOut("slow");
                               $("#action_buttons").fadeIn("slow");
                           }else{
                               $("#ajax_loading").html(datos);
                               $("#action_buttons").fadeIn("slow");
                           }
                        }
                    });
                }else{
                    $('#prospecto_correo').val('');
                }
            }
            
            //****-----------------FIN ACCIONES DE SELECCION CLIENTE Y PROSPECTO
            
        </script>
    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Cotización</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                Datos Generales
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <form action="cotizacion_form.jsp" id="select_cliente" name="select_cliente" method="post">
                                                               
                                <p>
                                    <label>Cliente:</label><br/>
                                    <div id="div_select_cliente" name="div_select_cliente" style="display: inline;" >
                                        
                                    </div>&nbsp;&nbsp;&nbsp;
                                    <a href="../../jsp/catClientes/catClientes_form.jsp?acc=3" id="btn_nuevo_cliente" title="Nuevo Cliente"
                                    class="modalbox_iframe">
                                        <img src="../../images/icon_agregar.png" alt="nuevo cliente" class="help" title="Nuevo Cliente"/><br/>
                                    </a>
                                
                                    <input type="hidden" id="cliente_correo" name="cliente_correo" value="<%= clienteDto!=null?clienteDto.getCorreo():"" %>"/>
                                </p>
                                <br/>
                                
                                <p>
                                    <label>Prospecto:</label><br/>
                                    <div id="div_select_prospecto" name="div_select_prospecto" style="display: inline;">
                                        
                                    </div>
                                    &nbsp;&nbsp;&nbsp;
                                
                                    <a href="../../jsp/catProspectos/catProspectos_form.jsp?acc=3" id="btn_nuevo_prospecto" title="Nuevo Prospecto"
                                        class="modalbox_iframe">
                                        <img src="../../images/icon_agregar.png" alt="nuevo prospecto" class="help" title="Nuevo Prospecto"/><br/>
                                    </a>

                                    <input type="hidden" id="prospecto_correo" name="prospecto_correo" value="<%= prospectoDto!=null?prospectoDto.getCorreo():"" %>"/>
                                </p>
                                <br/>
                                
                            </form>
                        </div>
                    </div>
                    
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                Productos
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <form action="cotizacion_form.jsp" method="post" id="form_productos" name="form_productos">
                                                               
                                <table class="data" width="100%" cellpadding="0" cellspacing="0" style="vertical-align: top;">
                                    <thead>
                                        <tr>
                                            <td>Producto</td>
                                            <td>Descripción</td>
                                            <td>Unidad</td>
                                            <td>Precio ($)</td>
                                            <td>Cantidad</td>
                                            <td>Monto ($)</td>
                                            <td>Acciones</td>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <select size="1" name="producto_select" id="producto_select"  style="width:120px;"
                                                                onchange="selectProducto(this.value);"
                                                                class="flexselect">
                                                    <option value="-1"></option>
                                                     <%out.print(conceptoBO.getConceptosByIdHTMLCombo(idEmpresa, -1,0,300," AND MATERIA_PRIMA=0 ")); %>
                                                </select>
                                                
                                                &nbsp;&nbsp;
                                                <a href="../../jsp/includeProductoSelect/producto_by_categoria_select_form.jsp?mode=cotizacion" id="btn_select_producto_by_categoria" title="Buscar Avanzada"
                                                    class="modalbox_iframe">
                                                    <img src="../../images/Search_16.png" alt="Busqueda Avanzada" class="help" title="Busqueda Avanzada"/><br/>
                                                </a>
                                                
                                            </td>
                                            <td>
                                                <textarea name="producto_descripcion" id="producto_descripcion" 
                                                rows="3" cols="35" readonly disabled>Seleccione un producto...</textarea>
                                            </td>
                                            <td>
                                                <select size="1" name="unidad_select" id="unidad_select"  style="width:150px;"
                                                                class="flexselect">
                                                    <option value="-1"></option>
                                                    <%out.print(new UnidadBO(user.getConn()).getUnidadesHTMLCombo(-1)); %>
                                                </select>
                                            </td>
                                            <td>
                                                <select id="producto_precio" name="producto_precio" onchange="selectProductoCalcularMonto();"
                                                            style="width: 90px;">
                                                    <option value="0">0</option>
                                                </select>
                                            </td>
                                            <td>
                                                <input type="text" maxlength="8" name="producto_cantidad" id="producto_cantidad" 
                                                        style="text-align: right;" size="8" onchange="selectProductoCalcularMonto();"
                                                        onkeypress="return validateNumber(event);"/>
                                            </td>
                                            <td>
                                                <input type="text" maxlength="12" name="producto_monto" id="producto_monto" 
                                                    readonly style="text-align: right;" size="10" onkeypress="return validateNumber(event);"/>
                                            </td>
                                            <td>
                                                <a href="javascript:void(0);" name="add_producto_action" onclick="addProducto();" id="add_producto_action">
                                                    <img src="../../images/icon_agregar.png" alt="Agregar Producto" height="20" width="20" class="help" title="Agregar Producto"/>
                                                </a>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                                
                                <center><div id="div_producto_ajax_loading" class="alert_warning noshadow" style="display: none;"></div></center>
                                <div id="div_ajax_productos" class="ajax_selected_box"></div>
                                
                            </form>
                        </div>
                    </div>
                    
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                Servicios
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <form action="cotizacion_form.jsp" method="post" id="form_servicios" name="form_servicios">
                                                               
                                <table class="data" width="100%" cellpadding="0" cellspacing="0" style="vertical-align: top;">
                                    <thead>
                                        <tr>
                                            <td>Servicio</td>
                                            <td>Descripción</td>
                                            <td>Precio ($)</td>
                                            <td>Cantidad</td>
                                            <td>Monto ($)</td>
                                            <td>Acciones</td>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <select size="1" name="servicio_select" id="servicio_select"  style="width:140px;"
                                                                onchange="selectServicio(this.value);"
                                                                class="flexselect">
                                                    <option value="-1"></option>
                                                    <%out.print(new ServicioBO(user.getConn()).getServiciosByIdHTMLCombo(idEmpresa, -1)); %>
                                                </select>
                                            </td>
                                            <td>
                                                <textarea name="servicio_descripcion" id="servicio_descripcion" 
                                                rows="3" cols="35" readonly disabled>Seleccione un servicio...</textarea>
                                            </td>
                                            <td>
                                                <select id="servicio_precio" name="servicio_precio" onchange="selectServicioCalcularMonto();"
                                                            style="width: 90px;">
                                                    <option value="0">0</option>
                                                </select>
                                            </td>
                                            <td>
                                                <input type="text" maxlength="8" name="servicio_cantidad" id="servicio_cantidad" 
                                                        style="text-align: right;" size="8" onchange="selectServicioCalcularMonto();"
                                                        onkeypress="return validateNumber(event);"/>
                                            </td>
                                            <td>
                                                <input type="text" maxlength="12" name="servicio_monto" id="servicio_monto" 
                                                    readonly style="text-align: right;" size="10" onkeypress="return validateNumber(event);"/>
                                            </td>
                                            <td>
                                                <a href="javascript:void(0);" name="add_servicio_action" onclick="addServicio();" id="add_servicio_action">
                                                    <img src="../../images/icon_agregar.png" alt="Agregar Servicio" height="20" width="20" class="help" title="Agregar Servicio"/>
                                                </a>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                                
                                <center><div id="div_servicio_ajax_loading" class="alert_warning noshadow" style="display: none;"></div></center>
                                <div id="div_ajax_servicios" class="ajax_selected_box"></div>
                                
                            </form>
                        </div>
                    </div>
                    
                    <div class="twocolumn">
                        
                        <!--Columna izquierda: Descuentos-->
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    Descuentos
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                        <thead>
                                            <tr>
                                                <td>Descuento (%)</td>
                                                <td>Importe Descuento</td>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td>
                                                    <input type="text" maxlength="5" name="descuento_tasa" id="descuento_tasa"
                                                            style="text-align: right;" size="12" value="<%=cotizacionSesion!=null?cotizacionSesion.getDescuento_tasa():"0"%>"
                                                            onkeypress="return validateNumber(event);"
                                                            onchange="calculaDescuento();"/>
                                                </td>
                                                <td>
                                                    <h3>- 
                                                    <input type="text" name="descuento_importe" id="descuento_importe"
                                                            style="text-align: right;" size="12" value="0"
                                                            onkeypress="return validateNumber(event);"
                                                            readonly />
                                                    </h3>
                                                </td>
                                            </tr>
                                            
                                            <tr>
                                                <td colspan="2">
                                                    <br/>
                                                    Motivo Descuento:
                                                    <br/>
                                                    <input type="text" name="descuento_motivo" id="descuento_motivo"
                                                           style="width: 100%;" value="<%= cotizacionDto!=null?cotizacionDto.getDescuentoMotivo():"" %>" maxlength="100"/>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                    <center><div id="div_descuento_ajax_loading" class="alert_warning noshadow" style="display: none;"></div></center>
                            </div>
                        </div>
                        <!--Fin columna izquierda: Descuentos-->
                         
                        <!--Columna derecha: Impuestos-->
                        <div class="column_right">
                            <div class="header">
                                <span>
                                    Impuestos
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                <form action="cotizacion_form.jsp" id="form_impuestos" name="form_impuestos" method="post">
                                    <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                        <thead>
                                            <tr>
                                                <td>Impuesto</td>
                                                <td>Acciones</td>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td>
                                                    <select size="1" id="impuesto_select" name="impuesto_select" style="width: 400px;">
                                                        <option value="-1">Seleccione Impuesto</option>
                                                            <%
                                                                out.print(new ImpuestoBO(user.getConn()).getImpuestosByIdHTMLCombo(idEmpresa, -1 ));
                                                            %>
                                                    </select>
                                                </td>
                                                <td>
                                                    <a href="javascript:void(0);" onclick="addImpuesto();" id="add_impuesto_action" name="add_impuesto_action">
                                                        <img src="../../images/icon_agregar.png" alt="Agregar Impuesto" height="20" width="20" class="help" title="Agregar Impuesto"/>
                                                    </a>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                 </form>
                                
                                <center><div id="div_impuesto_ajax_loading" class="alert_warning noshadow" style="display: none;"></div></center>
                                <div id="div_ajax_impuesto" class="ajax_selected_box"></div>
                                 
                            </div>
                        </div>
                        <!--Fin Columna derecha: Impuestos-->                         
                    </div>
                    <br class="clear"/>
                    
                    
                    <div class="twocolumn">
                        <div class="column_left" id="div_comentarios">
                            <div class="header">
                                <span>
                                    Comentarios
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <textarea cols="60" rows="5" id="comentarios" name="comentarios"><%= cotizacionDto!=null?cotizacionDto.getComentarios():"N/A" %></textarea>
                            </div>
                        </div>
                        
                        <div class="column_right" id="div_totales">
                            <div class="header">
                                <span>
                                    Totales
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                <form action="cotizacion_form.jsp" id="form_acciones" name="form_acciones" method="post">                            
                                    <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                        <tbody style="text-align: right;">                                      
                                            <tr>
                                                <td>
                                                    <h2>
                                                    Total:
                                                    <input type="text" maxlength="16" name="total" id="total"
                                                            style="text-align: right;" size="12" value="0" readonly
                                                            onkeypress="return validateNumber(event);"/>
                                                    </h2>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    Tipo de Moneda:
                                                    <select id="tipo_moneda" name="tipo_moneda">
                                                        <option value="MXN" selected>PESOS (M.N.)</option>
                                                        <option value="USD">DOLAR (USD)</option>
                                                        <option value="EUR">EUROS (EUR)</option>
                                                    </select>
                                                </td>
                                            </tr> 
                                        </tbody>
                                    </table>
                                </form>
                            </div>
                        </div>
                    </div>
                    <br class="clear"/>
                                
                   <div class="onecolumn" id="action_buttons">
                        <div class="header">
                            <span>
                                Acciones
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <form action="cotizacion_form.jsp" id="form_acciones" name="form_acciones" method="post">                            
                                <div>
                                    <p>
                                        <input type="button" id="regresar" value="Cancelar y Regresar" onclick="history.back();"/>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="button" id="guardar" value="Guardar Nuevo" onclick="grabar('');"/>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="button" id="guardar_enviar" value="Guardar Nuevo y Enviar" onclick="enviarYgrabar();"/>
                                    </p>
                                </div>
                            </form>
                        </div>
                    </div>
                                
                    <!--TODO EL CONTENIDO VA AQUÍ-->

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>

        <script>
            //mostrarCalendario();
           iniciarFlexSelect();
        </script>
    </body>
</html>
<%}%>