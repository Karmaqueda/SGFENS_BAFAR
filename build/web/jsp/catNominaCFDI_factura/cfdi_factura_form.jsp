<%-- 
    Document   : cfdi_factura_form
    Created on : 13-dic-2012, 19:18:28
    Author     : ISCesarMartinez
--%>

<%@page import="com.tsp.sct.bo.NominaRegistroPatronalBO"%>
<%@page import="com.tsp.sct.dao.jdbc.NominaComprobanteDescripcionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.NominaComprobanteDescripcion"%>
<%@page import="com.tsp.sct.bo.NominaDeduccionBO"%>
<%@page import="com.tsp.sct.bo.NominaPercepcionBO"%>
<%@page import="com.tsp.sct.bo.NominaEmpleadoBO"%>
<%@page import="com.tsp.sct.dao.dto.NominaEmpleado"%>
<%@page import="com.tsp.sct.dao.dto.NominaEmpleado"%>
<%@page import="com.tsp.sct.bo.FoliosBO"%>
<%@page import="com.tsp.sct.bo.FormaPagoBO"%>
<%@page import="com.tsp.sct.bo.TipoPagoBO"%>
<%@page import="com.tsp.sct.dao.dto.TipoPago"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.bo.EstatusComprobanteBO"%>
<%@page import="com.tsp.sct.dao.dto.DatosUsuario"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.bo.ServicioBO"%>
<%@page import="com.tsp.sct.bo.ImpuestoBO"%>
<%@page import="com.tsp.sct.bo.UnidadBO"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.dao.dto.ComprobanteFiscal"%>
<%@page import="com.tsp.sct.bo.ComprobanteFiscalBO"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="cfdiSesion" scope="session" class="com.tsp.sgfens.sesion.ComprobanteFiscalSesion"/>
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
        int idComprobanteFiscal = 0;
        try {
            idComprobanteFiscal = Integer.parseInt(request.getParameter("idComprobanteFiscal"));
        } catch (NumberFormatException e) {
        }

        /*
         *   1 = nuevo
         *   2 = editar/consultar
         *   
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        
        ComprobanteFiscalBO comprobanteFiscalBO = new ComprobanteFiscalBO(user.getConn());
        ComprobanteFiscal comprobanteFiscalDto = null;
        TipoPago tipoPagoDto = null;
        NominaComprobanteDescripcion nominaComprobanteDescripcion = null;
        
        
        /*Cliente clienteDto = null;
        if (idComprobanteFiscal > 0){
            comprobanteFiscalBO = new ComprobanteFiscalBO(idComprobanteFiscal);
            comprobanteFiscalDto = comprobanteFiscalBO.getComprobanteFiscal();
            
            clienteDto = new ClienteBO(comprobanteFiscalDto!=null?comprobanteFiscalDto.getIdCliente():-1).getCliente();
            tipoPagoDto = new TipoPagoBO(comprobanteFiscalDto.getIdTipoPago()).getTipoPago();
        }*/
        
        NominaEmpleado empleadoNomina = null;
        if (idComprobanteFiscal > 0){
            comprobanteFiscalBO = new ComprobanteFiscalBO(idComprobanteFiscal,user.getConn());
            comprobanteFiscalDto = comprobanteFiscalBO.getComprobanteFiscal();
            
            empleadoNomina = new NominaEmpleadoBO(user.getConn() , comprobanteFiscalDto!=null?comprobanteFiscalDto.getIdCliente():-1).getNominaEmpleado();
            tipoPagoDto = new TipoPagoBO(comprobanteFiscalDto.getIdTipoPago(),user.getConn()).getTipoPago();
            
            NominaComprobanteDescripcion[] ncd = new NominaComprobanteDescripcion[0];
            ncd = new NominaComprobanteDescripcionDaoImpl().findWhereIdCromprobanteFiscalEquals(idComprobanteFiscal);
            nominaComprobanteDescripcion = ncd[0];
        }
        
        ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
        NominaPercepcionBO percepcionBO = new NominaPercepcionBO(user.getConn());
        NominaDeduccionBO deduccionBO = new NominaDeduccionBO(user.getConn());

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
                
                $("#producto_select").flexselect({
                    jsFunction:  function(id) { selectProducto(id); }
                });

                $("#percepcion_select").flexselect({
                    jsFunction:  function(id) { selectPercepcion(id); }
                });
                
                $("#deduccion_select").flexselect({
                    jsFunction:  function(id) { selectDeduccion(id); }
                });

                $("#servicio_select").flexselect({
                    jsFunction:  function(id) { selectServicio(id); }
                });

                $("select.flexselect").flexselect();
            }
            
            //****---------------------------------------INICIALIZACION
            
            $(document).ready(function() {
                recargarSelectClientes();
                
                recuperarListados();
                <%=(mode.equals("1")?"nuevaComprobanteFiscal();":"")%>
            });
            
            function recuperarListados(){
                recargarListaProducto();
                recargarListaServicio();
                recargarListaPercepciones();
                recargarListaDeducciones();                        
                //recargarListaImpuestos();
                recargaImpuestoISR();
            }
            
            function recalcularTotales(){
                recargarListaImpuestos();
                <% if (comprobanteFiscalDto==null) { %>
                calculaDescuento();
                <% } %>
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
            
            function nuevaComprobanteFiscal(){
                $.ajax({
                    type: "POST",
                    url: "cfdi_factura_ajax.jsp",
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
                    var correos = correoCliente;
                    
                    apprise("¿Correos electrónicos a los que se enviará el cfdi (separar por coma ,)?", 
                        {'input':correos, 'animate':true}, function(r){
                            
                            if(r) {
                                //alert (r);
                                 grabar(r);
                            }
                            
                    });
                }
            }
            
            function grabar(listaCorreos){
                
                var idCliente = $("#id_cliente").val();
                var descuentoMotivo = $("#descuento_motivo").val();
                var coment = $("#comentarios").val();
                var tipoMoneda = $("#tipo_moneda").val();
                var tipoCambio = $("#tipo_cambio").val();
                var fechaPago = $("#cfdi_fecha_pago").val();
                var fechaPagoInicial = $("#cfdi_fecha_pago_inicial").val();
                var fechaPagoFinal = $("#cfdi_fecha_pago_final").val();
                var numDiasPago = $("#cfdi_numDias_pago").val();
                
                var pocentajeISR = $("#pocentajeISR").val();
                var montoISR = $("#montoISR").val();
                
                var idFolios = $("#cfdi_serie").val();
                var tipoPagoDescripcion = $("#tipo_pago_descripcion").val();
                var tipoPagoNumCuenta = $("#tipo_pago_num_cuenta").val();
                var idFormaPago = $("#id_forma_pago").val();
                var metodoPago = $("#metodo_pago").val();
                var idTipoPago = $("#id_tipo_pago").val();  
                
                var parcialidad ="";
                var parcialidadFolioOrig ="";
                var parcialidadSerieOrig = "";
                var parcialidadFechaOrig = "";
                var parcialidadMontoOrig = "";
                
                var totalFactura = $("#total").val();
                
                var idRegistroPatronal = $("#id_registro_patronal").val();
                
                if (idFormaPago==2){
                    //Si la Forma de Pago es Parcialidades se recuperan los valores
                    parcialidad = $("#parcialidad").val();
                    parcialidadFolioOrig = $("#parcialidad_folio_orig").val();
                    parcialidadSerieOrig = $("#parcialidad_serie_orig").val();
                    parcialidadFechaOrig = $("#parcialidad_fecha_orig").val();
                    parcialidadMontoOrig = $("#parcialidad_monto_orig").val();
                }
                
                
                if(validar(0)){
                    $.ajax({
                        type: "POST",
                        url: "cfdi_factura_ajax.jsp",
                        data: { mode: '19' , id_cliente : idCliente, descuento_motivo : descuentoMotivo,
                                comentarios : coment, tipo_moneda : tipoMoneda , 
                                lista_correos : listaCorreos, fecha_pago : fechaPago,
                                id_folios : idFolios, tipo_pago_descripcion : tipoPagoDescripcion,
                                tipo_pago_num_cuenta : tipoPagoNumCuenta, id_forma_pago : idFormaPago,
                                parcialidad : parcialidad, parcialidad_folio_orig : parcialidadFolioOrig,
                                parcialidad_serie_orig : parcialidadSerieOrig, parcialidad_fecha_orig : parcialidadFechaOrig,
                                parcialidad_monto_orig : parcialidadMontoOrig, tipo_cambio : tipoCambio,
                                fechaPagoInicial : fechaPagoInicial, fechaPagoFinal : fechaPagoFinal, numDiasPago : numDiasPago,
                                pocentajeISR : pocentajeISR, montoISR : montoISR , total : totalFactura, 
                                id_registro_patronal : idRegistroPatronal,
                                metodo_pago : metodoPago, id_tipo_pago : idTipoPago
                              },
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
                                        location.href="../catNominaCFDI_factura/cfdi_factura_list.jsp";
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
                if(jQuery.trim($("#id_cliente").val())<=0 ){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "Cliente"  es requerido</center>',{'animate':true});
                    return false;
                }
                
                if(jQuery.trim($("#cfdi_fecha_pago").val()).length<=0 ){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "Fecha de Pago"  es requerido.</center>',{'animate':true});
                    return false;
                }
                
                if(jQuery.trim($("#cfdi_fecha_pago_inicial").val()).length<=0 ){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "Fecha Inicial de Pago"  es requerido.</center>',{'animate':true});
                    return false;
                }
                
                if(jQuery.trim($("#cfdi_fecha_pago_final").val()).length<=0 ){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "Fecha Final de Pago"  es requerido.</center>',{'animate':true});
                    return false;
                }
                
                if(jQuery.trim($("#cfdi_numDias_pago").val()).length<=0 ){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "número de Días Pagados"  es requerido.</center>',{'animate':true});
                    return false;
                }
                
                if(jQuery.trim($("#tipo_pago_descripcion").val()).length<=0 ){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "Método de Pago"  es requerido.</center>',{'animate':true});
                    return false;
                }
                
                //Validamos Numero de Cuenta, solo en caso de estar capturado
                if(jQuery.trim($("#tipo_pago_num_cuenta").val()).length>0){
                    getFloatFromInput($("#tipo_pago_num_cuenta"));
                    if (jQuery.trim($("#tipo_pago_num_cuenta").val()).length<4 ){
                        apprise('<center><img src=../../images/warning.png> <br/>El dato "Numero de Cuenta"  es opcional, pero si se usa deben ser 4 dígitos.</center>',{'animate':true});
                        return false;
                    }
                }
                
                //Validamos que este seleccionado una forma de pago
                if(jQuery.trim($("#id_forma_pago").val())<=0){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "Forma de Pago"  es requerido. Debe seleccionar alguno.</center>',{'animate':true});
                    return false;
                }
                
                //Validamos datos de parcialidad, solo en caso de estar seleccionado esa forma de pago
                if(jQuery.trim($("#id_forma_pago").val())==2){
                    //Validamos parcialidad
                    if ( !validarParcialidades( jQuery.trim($("#parcialidad").val()) ) ){
                        $("#parcialidad").focus();
                        return false;
                    }
                    
                    //Validamos Folio Fiscal Original (UUID), solo en caso de estar capturado
                    if(jQuery.trim($("#parcialidad_folio_orig").val()).length>0){
                        if ( !validarUUID( jQuery.trim($("#parcialidad_folio_orig").val()) ) ){
                            $("#parcialidad_folio_orig").focus();
                            return false;
                        }
                    }
                    
                    //Validamos Fecha de Folio Fiscal Original, solo en caso de estar capturado
                    if(jQuery.trim($("#parcialidad_fecha_orig").val()).length>0){
                        if ( !validarDateTimeXML( jQuery.trim($("#parcialidad_fecha_orig").val()) ) ){
                            $("#parcialidad_fecha_orig").focus();
                            return false;
                        }
                    }
                    
                }
                
                //Validamos que este seleccionado un Registro Patronal
                /*if(jQuery.trim($("#id_registro_patronal").val())<=0){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "Registro Patronal"  es requerido. Debe seleccionar alguno.</center>',{'animate':true});
                    return false;
                }*/
                
                /*if($("#id_registro_patronal").val() <= 0){                    
                    apprise('¿El comprobante se generará sin "Registro Patronal". Desea agregarlo?', {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, 
                        function(r){
                            if(r){
                                // Usuario dio click 'Yes'
                                return false;
                            }
                    });
                }*/
                
                //alert('cambiar validacion a true');
                return true;
            }
            
            
            
            //****---------------------------------FIN GUARDAR
            
            
            //****---------------------------------PRODUCTOS
            function selectPercepcionFromPopupCategoria(idPercepcion, nombrePercepcion){
                //alert('Percepcion seleccionado de categorias: ' + idPercepcion + " " + nombrePercepcion);
                selectPercepcion(idPercepcion);
                
                //Como se usa flexselect se usan las siguientes 2 opciones
                $("#percepcion_select").val(idPercepcion);
                $('#percepcion_select'+postnameFlexSelect).val(nombrePercepcion);
                //En caso de no usa flexselect, y ser un select básico se usa la siguiente opción
                //$("#percepcion_select option[value="+idPercepcion+"]").attr("selected",true);
            }
            
            function selectPercepcion(idPercepcion){
                //alert("idPercepcion: " + idPercepcion);
                if(idPercepcion>0){
                    $.ajax({
                        type: "POST",
                        url: "cfdi_factura_ajax.jsp",
                        data: { mode: '23', id_percepcion_select: idPercepcion },
                            //$("#frm_action").serialize(),
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#div_percepcion_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                            $("#div_percepcion_ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               strJSON = $.trim(datos.replace('<!--EXITO-->',''));                              
                               arrayRespuesta=eval('(' + strJSON + ')');
                               //$('#percepcion_unidad').val(arrayRespuesta["unidadPercepcionCompra"]);
                               //$('#percepcion_precio').val(arrayRespuesta["precio"]);                               
                               $('#percepcion_descripcion').val(arrayRespuesta["concepto"]);
                               
                               $("#percepcion_precio_gravado").val(arrayRespuesta["importeGravado"]);
                               $("#percepcion_precio_exento").val(arrayRespuesta["importeExcepto"]);
                               
                               
                               selectPercepcionLimpiar();
                               $("#div_percepcion_ajax_loading").fadeOut("slow");
                               $("#action_buttons").fadeIn("slow");
                              
                           }else{
                               $("#div_percepcion_ajax_loading").html(datos);
                               $("#action_buttons").fadeIn("slow");
                           }
                        }
                    });
                }
            }
            
            function selectDeduccion(idDeduccion){
                //alert("idDeduccion: " + idDeduccion);
                if(idDeduccion>0){
                    $.ajax({
                        type: "POST",
                        url: "cfdi_factura_ajax.jsp",
                        data: { mode: '26', id_deduccion_select: idDeduccion },
                            //$("#frm_action").serialize(),
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#div_deduccion_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                            $("#div_deduccion_ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               strJSON = $.trim(datos.replace('<!--EXITO-->',''));                              
                               arrayRespuesta=eval('(' + strJSON + ')');
                               //$('#deduccion_unidad').val(arrayRespuesta["unidadPercepcionCompra"]);
                               //$('#deduccion_precio').val(arrayRespuesta["precio"]);                               
                               $('#deduccion_descripcion').val(arrayRespuesta["concepto"]);
                               
                               $("#deduccion_precio_gravado").val(arrayRespuesta["importeGravado"]);
                               $("#deduccion_precio_exento").val(arrayRespuesta["importeExcepto"]);
                               
                               
                               selectPercepcionLimpiar();
                               $("#div_deduccion_ajax_loading").fadeOut("slow");
                               $("#action_buttons").fadeIn("slow");
                              
                           }else{
                               $("#div_deduccion_ajax_loading").html(datos);
                               $("#action_buttons").fadeIn("slow");
                           }
                        }
                    });
                }
            }
            
            function selectPercepcionLimpiar(){
                $('#percepcion_cantidad').val('');
                $('#percepcion_monto').val('');
                //$('#unidad_select'+postnameFlexSelect).val('');
                //$('#unidad_select'+postnameFlexSelect).focus();
            }
            function selectDeduccionLimpiar(){
                $('#deduccion_cantidad').val('');
                $('#deduccion_monto').val('');
                //$('#unidad_select'+postnameFlexSelect).val('');
                //$('#unidad_select'+postnameFlexSelect).focus();
            }
            function selectPercepcionCalcularMonto(){
                var cantidad = getFloatFromInput($('#percepcion_cantidad'));
                var precio = getFloatFromInput($('#percepcion_precio'));
                var monto = (precio * cantidad).toFixed(2);
                //alert('precio: ' + precio + ' cantidad: ' + cantidad + ' monto: ' + monto);
                $('#percepcion_monto').val(monto);
            }
            
            function addPercepcion(){
                percepcionId = $('#percepcion_select').val(); 
                percepcion_precio_gravado = $('#percepcion_precio_gravado').val();
                percepcion_precio_exento = $('#percepcion_precio_exento').val();                
                percepcionDescripcion = $('#percepcion_descripcion').val();
                if (validarAddPercepcion(percepcionId)){
                    if(percepcionId>0){
                        $.ajax({
                            type: "POST",
                            url: "cfdi_factura_ajax.jsp",
                            data: { mode: '24', percepcion_id : percepcionId, 
                                percepcion_precio_gravado : percepcion_precio_gravado, percepcion_precio_exento : percepcion_precio_exento,percepcionDescripcion : percepcionDescripcion},
                                //$("#frm_action").serialize(),
                            beforeSend: function(objeto){
                                $("#action_buttons").fadeOut("slow");
                                $("#div_percepcion_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                                $("#div_percepcion_ajax_loading").fadeIn("slow");
                            },
                            success: function(datos){
                                if(datos.indexOf("--EXITO-->", 0)>0){
                                   $("#div_ajax_percepcions").html(datos);
                                   selectPercepcionLimpiar();
                                   $("#div_percepcion_ajax_loading").fadeOut("slow");
                                   $("#action_buttons").fadeIn("slow");
                                   //recalcularTotales();
                               }else{
                                   $("#div_percepcion_ajax_loading").html(datos);
                                   $("#action_buttons").fadeIn("slow");
                               }
                            }
                        });
                    }
                }
            }
            
            function addDeduccion(){
                deduccionId = $('#deduccion_select').val(); 
                deduccion_precio_gravado = $('#deduccion_precio_gravado').val();
                deduccion_precio_exento = $('#deduccion_precio_exento').val();                
                deduccionDescripcion = $('#deduccion_descripcion').val();
                if (validarAddPercepcion(deduccionId)){
                    if(deduccionId>0){
                        $.ajax({
                            type: "POST",
                            url: "cfdi_factura_ajax.jsp",
                            data: { mode: '27', deduccion_id : deduccionId, 
                                deduccion_precio_gravado : deduccion_precio_gravado, deduccion_precio_exento : deduccion_precio_exento,deduccionDescripcion : deduccionDescripcion},
                                //$("#frm_action").serialize(),
                            beforeSend: function(objeto){
                                $("#action_buttons").fadeOut("slow");
                                $("#div_deduccion_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                                $("#div_deduccion_ajax_loading").fadeIn("slow");
                            },
                            success: function(datos){
                                if(datos.indexOf("--EXITO-->", 0)>0){
                                   $("#div_ajax_deduccions").html(datos);
                                   selectDeduccionLimpiar();
                                   $("#div_deduccion_ajax_loading").fadeOut("slow");
                                   $("#action_buttons").fadeIn("slow");
                                   //recalcularTotales();
                               }else{
                                   $("#div_deduccion_ajax_loading").html(datos);
                                   $("#action_buttons").fadeIn("slow");
                               }
                            }
                        });
                    }
                }
            }
            
            function recargarListaDeducciones(){
                $.ajax({
                    type: "POST",
                    url: "cfdi_factura_ajax.jsp",
                    data: { mode: '31' },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#div_deduccion_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                        $("#div_deduccion_ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#div_ajax_deduccions").html(datos);
                           selectProductoLimpiar();
                           $("#div_deduccion_ajax_loading").fadeOut("slow");
                           $("#action_buttons").fadeIn("slow");
                           recalcularTotales();
                       }else{
                           $("#div_deduccion_ajax_loading").html(datos);
                           $("#action_buttons").fadeIn("slow");
                       }
                    }
                });
            }
            
            function validarAddPercepcion(idPercepcion){
                if(idPercepcion<=0 ){
                    apprise('<center><img src=../../images/warning.png> <br/>Llene todos los datos requeridos para agregar el registro. Valores mayores a 0</center>',{'animate':true});
                    return false;
                }
                return true;
            }
            
            function subPercepcion(indexListaPercepcionsSesion){
                if(indexListaPercepcionsSesion>=0){
                    $.ajax({
                        type: "POST",
                        url: "cfdi_factura_ajax.jsp",
                        data: { mode: '4', index_lista_percepcion : indexListaPercepcionsSesion },
                            //$("#frm_action").serialize(),
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#div_percepcion_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                            $("#div_percepcion_ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#div_ajax_percepcions").html(datos);
                               //selectPercepcionLimpiar();
                               $("#div_percepcion_ajax_loading").fadeOut("slow");
                               $("#action_buttons").fadeIn("slow");
                               //recalcularTotales();
                           }else{
                               $("#div_percepcion_ajax_loading").html(datos);
                               $("#action_buttons").fadeIn("slow");
                           }
                        }
                    });
                }
            }
            
            function reiniciarListaPercepcion(){
                $.ajax({
                    type: "POST",
                    url: "cfdi_factura_ajax.jsp",
                    data: { mode: '3' },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#div_percepcion_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                        $("#div_percepcion_ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#div_ajax_percepcions").html(datos);
                           selectPercepcionLimpiar();
                           $("#div_percepcion_ajax_loading").fadeOut("slow");
                           $("#action_buttons").fadeIn("slow");
                           //recalcularTotales();
                       }else{
                           $("#div_percepcion_ajax_loading").html(datos);
                           $("#action_buttons").fadeIn("slow");
                       }
                    }
                });
            }
            
            function recargarListaPercepciones(){
                $.ajax({
                    type: "POST",
                    url: "cfdi_factura_ajax.jsp",
                    data: { mode: '30' },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#div_percepcion_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                        $("#div_percepcion_ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#div_ajax_percepcions").html(datos);
                           selectProductoLimpiar();
                           $("#div_percepcion_ajax_loading").fadeOut("slow");
                           $("#action_buttons").fadeIn("slow");
                           recalcularTotales();
                       }else{
                           $("#div_percepcion_ajax_loading").html(datos);
                           $("#action_buttons").fadeIn("slow");
                       }
                    }
                });
            }
            //****---------------------------------FIN PERCEPCIONES Y DEDUCCIONES
            
            //****---------------------------------PRODUCTOS
            
            function selectProducto(idProducto){
                //alert("idProducto: " + idProducto);
                if(idProducto>0){
                    $.ajax({
                        type: "POST",
                        url: "cfdi_factura_ajax.jsp",
                        data: { mode: '1', id_producto_select: idProducto },
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
                               /*options_precio += '<option value="' + arrayRespuesta["precio"] + '">' + arrayRespuesta["precio"] + ' [men.]</option>';
                               options_precio += '<option value="' + arrayRespuesta["precioMayoreo"] + '">' + arrayRespuesta["precioMayoreo"] + ' [may.]</option>';
                               options_precio += '<option value="' + arrayRespuesta["precioDocena"] + '">' + arrayRespuesta["precioDocena"] + ' [docena]</option>';
                               options_precio += '<option value="' + arrayRespuesta["precioEspecial"] + '">' + arrayRespuesta["precioEspecial"] + ' [especial]</option>';
                               */
                               //alert('-->' + options_precio);
                               
                               //$("#producto_precio").html(options_precio);
                               $("#producto_precio").val(arrayRespuesta["precio"]);
                               
                               selectProductoLimpiar();
                               $("#div_producto_ajax_loading").fadeOut("slow");
                               $("#action_buttons").fadeIn("slow");
                           }else{
                               $("#div_producto_ajax_loading").html(datos);
                               $("#action_buttons").fadeIn("slow");
                           }
                        }
                    });
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
                unidadId = 19;
                unidadText = "SERVICIO";
                //unidadText = $('#unidad_select option:selected').text();
                    //alert('unidad ' + unidadText);
                productoDescripcion = $('#producto_descripcion').val();                    
                productoPrecio = $('#producto_precio').val();
                productoCantidad = $('#producto_cantidad').val();
                productoMonto = $('#producto_monto').val();
                if (validarAddProducto(productoId,productoCantidad,productoMonto,unidadId)){
                    if(productoId>0){
                        $.ajax({
                            type: "POST",
                            url: "cfdi_factura_ajax.jsp",
                            data: { mode: '2', producto_id : productoId, 
                                producto_cantidad : productoCantidad, producto_monto : productoMonto, productoDescripcion : productoDescripcion,
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
                        url: "cfdi_factura_ajax.jsp",
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
                    url: "cfdi_factura_ajax.jsp",
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
                    url: "cfdi_factura_ajax.jsp",
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
                        url: "cfdi_factura_ajax.jsp",
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
                            url: "cfdi_factura_ajax.jsp",
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
                        url: "cfdi_factura_ajax.jsp",
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
                    url: "cfdi_factura_ajax.jsp",
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
                    url: "cfdi_factura_ajax.jsp",
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
                            url: "cfdi_factura_ajax.jsp",
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
            
            function addImpuestoISR(){
                var montoISR = $("#montoISR").val();
                var pocentajeISR = $("#pocentajeISR").val();
                
                //if (validarAddImpuestoISR(montoISR, pocentajeISR)){
                    //if(impuestoId>0){
                        $.ajax({
                            type: "POST",
                            url: "cfdi_factura_ajax.jsp",
                            data: { mode: '28', montoISR : montoISR, pocentajeISR : pocentajeISR},
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
                    //}
                //}
            }
            function validarAddImpuestoISR(montoISR, pocentajeISR){
                if(montoISR > 0){
                    if(pocentajeISR <= 0){
                        apprise('<center><img src=../../images/warning.png> <br/>Coloque el porcentaje de ISR.</center>',{'animate':true});
                        return false;
                    }
                }
                if(pocentajeISR > 0){
                    if(montoISR <= 0){
                        apprise('<center><img src=../../images/warning.png> <br/>Coloque el monto de ISR.</center>',{'animate':true});
                        return false;
                    }
                }
                
                if(montoISR > 0 && pocentajeISR > 0){
                    return true;
                }else{
                    return false;
                }
            }
            
            function subImpuesto(indexListaImpuestosSesion){
                if(indexListaImpuestosSesion>=0){
                    $.ajax({
                        type: "POST",
                        url: "cfdi_factura_ajax.jsp",
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
                    url: "cfdi_factura_ajax.jsp",
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
            
            function recargaImpuestoISR(){
                var montoISR = $("#montoISR").val();
                var pocentajeISR = $("#pocentajeISR").val();
                
                if (validarAddImpuestoISR(montoISR, pocentajeISR)){
                    //if(impuestoId>0){
                        $.ajax({
                            type: "POST",
                            url: "cfdi_factura_ajax.jsp",
                            data: { mode: '28', montoISR : montoISR, pocentajeISR : pocentajeISR, soloRecargar : 'soloRecargar'},
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
                    //}
                }
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
                        url: "cfdi_factura_ajax.jsp",
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
                               var dctoImporte = parseFloat(datos).toFixed(2);
                               //alert(dctoImporte);
                               $('#descuento_importe').val(dctoImporte);
                               //selectPercepcionLimpiar();
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
                
                //Calculamos subtotal (percepcions + servicios)
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
                //restamos el isr si existe:
                var montoISR = getFloatFromInput($('#montoISR'));
                //alert(montoISR);
                total = total - montoISR;
                        
                $('#total').val(total);
            }
            
            
            
            //****-----------------ACCIONES DE SELECCION CLIENTE
            
            //Recarga de BD dinámicamente el listado de clientes de la empresa
            function recargarSelectClientes(){
                $.ajax({
                    type: "POST",
                    url: "cfdi_factura_ajax.jsp",
                    data: { mode: '20', id_cliente : <%= comprobanteFiscalDto!=null?comprobanteFiscalDto.getIdCliente():(cfdiSesion.getCliente()!=null?cfdiSesion.getCliente().getIdCliente():-1) %> },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                            $("#div_select_cliente").html(datos);
                            //selectPercepcionLimpiar();
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
                        url: "cfdi_factura_ajax.jsp",
                        data: { mode: '21', id_cliente : idCliente },
                            //$("#frm_action").serialize(),
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               strJSONCliente = $.trim(datos.replace('<!--EXITO-->',''));
                               
                               //alert(strJSONCliente);
                               
                               arrayRespuestaCliente=eval('(' + strJSONCliente + ')');
                               
                               $('#empleadoNomina_correo').val(arrayRespuestaCliente["correo"]);
                               
                               //var direccionCompuesta = arrayRespuestaCliente["calle"] + ' ' +  arrayRespuestaCliente["numero"] + ' ' +  arrayRespuestaCliente["numeroInterior"] + ' ' +  arrayRespuestaCliente["colonia"];
                               $('#empleadoNomina_rfc').html('RFC: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>' + arrayRespuestaCliente["rfc"] + '</b>');
                               $('#empleadoNomina_numero').html('Núm. Empleado: <b>' + arrayRespuestaCliente["numEmpleado"] + '</b>');
                               $('#empleadoNomina_curp').html('CURP: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>' + arrayRespuestaCliente["curp"] + '</b>');
                               
                               
                               $("#ajax_loading").fadeOut("slow");
                               $("#action_buttons").fadeIn("slow");
                           }else{
                               $("#ajax_loading").html(datos);
                               $("#action_buttons").fadeIn("slow");
                           }
                        }
                    });
                }else{
                    $('#empleadoNomina_correo').val('');
                }
            }
            
            //****-----------------FIN ACCIONES DE SELECCION CLIENTE
            
            function mostrarCalendario(){
                $( "#cfdi_fecha_pago" ).datepicker({
                        
                        gotoCurrent: true,
                        changeMonth: true,
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 100);
                            }, 500)}
                });
                
                $( "#cfdi_fecha_pago_inicial" ).datepicker({
                        //minDate: 0,
                        gotoCurrent: true,
                        changeMonth: true,
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 100);
                            }, 500)}
                });
                
                $( "#cfdi_fecha_pago_final" ).datepicker({
                        //minDate: 0,
                        gotoCurrent: true,
                        changeMonth: true,
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 100);
                            }, 500)}
                });
            }
            
            function toggleDivFolio(idSerie){
                if (idSerie=="-1"){
                    $("#div_folio").hide();
                }else{
                    $("#div_folio").show();
                }
            }
            
            function toggleDivMoneda(idTipoMoneda){
                if (idTipoMoneda=="-1"){
                    $("#div_moneda").hide();
                }else{
                    $("#div_moneda").show();
                }
            }
            
            function toggleDivParcialidad(idFormaPago){
                if (idFormaPago=="2"){
                    //Si es forma de pago Parcialidades
                    $("#div_parcialidades").show();
                }else{
                    $("#div_parcialidades").hide();
                }
            }
                       
            function validarParcialidades(parcialidad){
                //var parcialidad = valorParcialidades;
                        
                if (parcialidad.length<=0){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "parcialidad" es requerido según la Forma de Pago elegida.</center>',{'animate':true});
                    return false;
                }

                var valor=parcialidad.split('/');

                if (valor.length!=2){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "parcialidad" es incorrecto. <br/> Debe tener formato <b>X/Y</b> <br/> X: parcialidad actual, Y: parcialidades totales</center>',{'animate':true});
                    return false;
                }

                var a = valor[0];  
                var b = valor[1]; 
                var aint = parseInt(a);
                var bint = parseInt(b);	

                if(aint > bint && aint > 0 && bint > 0){
                    apprise('<center><img src=../../images/warning.png> <br/>El numero de parcialidad debe ser menor que las parcialidades totales.</center>',{'animate':true});
                    return false;
                }
                if(a==0 || b==0){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "parcialidad" es incorrecto. No puede contener un valor 0.</center>',{'animate':true});
                    return false;
                }
                return true;
            }
            
            function validarUUID(uuid){
                //if(uuid.length>0){
                    var strCorrecta;
                    strCorrecta = uuid;		
                    var valid ='^([0-9a-fA-F]){8}-([0-9a-fA-F]){4}-([0-9a-fA-F]){4}-([0-9a-fA-F]){4}-([0-9a-fA-F]){12}';				
                    var validUuid=new RegExp(valid);
                    var matchArray=strCorrecta.match(validUuid);
                    if (matchArray==null||matchArray<36||matchArray>36) {
                            apprise('<center><img src=../../images/warning.png> <br/>El dato "Folio Fiscal Original" es incorrecto, no tiene un formato adecuado.</center> <br/> p. ej.: 0AB11101-111A-00A1-1000-01A1AA1AA1A1 ',{'animate':true});
                            return false;
                    }
                    else{
                            return true;
                    }
                //}
                //return true;
            }
            
            function validarDateTimeXML(dateTimeXML){
                var strCorrecta;
                strCorrecta = dateTimeXML;		
                //aaaa-mm-ddThh:mm:ss
		//var valid ='^([2]){1}([0]){1}([0-1]){1}([1-2]){1}-([0-1]){1}([0-9]){1}-([0-3]){1}([0-9]){1}([T]){1}([0-2]){1}([0-9]){1}:([0-5]){1}([0-9]){1}:([0-5]){1}([0-9]){1}';
                var valid ='^([2]){1}([0]){1}([0-2]){1}([0-9]){1}-([0-1]){1}([0-9]){1}-([0-3]){1}([0-9]){1}([T]){1}([0-2]){1}([0-9]){1}:([0-5]){1}([0-9]){1}:([0-5]){1}([0-9]){1}';
                var validUuid=new RegExp(valid);
                var matchArray=strCorrecta.match(validUuid);
                if (matchArray==null||matchArray<19||matchArray>19) {
                        apprise('<center><img src=../../images/warning.png> <br/>El dato "Fecha del Folio Fiscal Original" es incorrecto, no tiene un formato adecuado.<br/> p. ej: 2012-11-14T09:09:33 </center>',{'animate':true});
                        return false;
                }
                else{
                        return true;
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
                    <h1>
                        Comprobante Fiscal Nómina <%= comprobanteFiscalDto!=null?"ID " +comprobanteFiscalDto.getIdComprobanteFiscal():"" %>
                        <br/>
                        <%=cfdiSesion.getIdPedido()>0?"Del Pedido con ID: " +cfdiSesion.getIdPedido():""%>
                    </h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                Datos Generales
                            </span>
                            <div style="float: right; font-size: 20px; " >
                                <% if (comprobanteFiscalDto!=null) {%>
                                <a href="../../jsp/reportesExportar/previewCfdPdf.jsp?idComprobanteFiscal=<%=comprobanteFiscalDto.getIdComprobanteFiscal() %>&versionCfd=3.2" id="btn_show_cfdi" title="Previsualizar CFDI"
                                class="modalbox_iframe">
                                    <img src="../../images/icon_nominaCFDI.png" alt="Mostrar CFDI" class="help" title="Previsualizar CFDI"/><br/>
                                </a>
                                <% } %>
                                UUID: <b><i><%= comprobanteFiscalDto!=null?comprobanteFiscalDto.getUuid():"Sin Asignar" %></i></b>
                                &nbsp;&nbsp;
                            </div>
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <form action="cfdi_factura_form.jsp" id="select_cliente" name="select_cliente" method="post">
                                <table class="data" width="100%" cellpadding="0" cellspacing="0" style="vertical-align: top;">
                                    <tbody>
                                        <tr>
                                            <td>
                                                Empleado: *
                                                <div id="div_select_cliente" name="div_select_cliente" style="display: inline;" >
                                                </div>
                                                &nbsp;&nbsp;&nbsp;
                                                <a href="../../jsp/catNominaEmpleados/catEmpleados_form.jsp?acc=3" id="btn_nuevo_cliente" title="Nuevo Empleado"
                                                class="modalbox_iframe">
                                                    <img src="../../images/icon_agregar.png" alt="nuevo empleado" class="help" title="Nuevo Empleado"/><br/>
                                                </a>
                                                <br/>
                                                <input type="hidden" id="empleadoNomina_correo" name="empleadoNomina_correo" value="<%= empleadoNomina!=null?empleadoNomina.getCorreo():"" %>"/>
                                                <div id="div_datos_empleadoNomina" name="div_datos_empleadoNomina">
                                                    <div id="empleadoNomina_rfc">
                                                        RFC: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                        <%= empleadoNomina!=null?empleadoNomina.getRfc():"" %>
                                                    </div>
                                                    <div id="empleadoNomina_numero">
                                                        Núm. Empleado: 
                                                        <%= empleadoNomina!=null?empleadoNomina.getNumEmpleado():"" %>
                                                    </div>
                                                    <div id="empleadoNomina_curp">
                                                        Curp: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                        <%= empleadoNomina!=null?empleadoNomina.getCurp():"" %>
                                                    </div>
                                                </div>
                                            </td>
                                            <td>
                                                Serie: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                <select name="cfdi_serie" id="cfdi_serie" 
                                                        onchange="toggleDivFolio(this.value);">
                                                    <option value="-1">Sin Serie</option>
                                                    <%= new FoliosBO(user.getConn()).getFoliosByIdHTMLCombo(idEmpresa, (comprobanteFiscalDto!=null?comprobanteFiscalDto.getIdFolio():-1) ) %>
                                                </select>
                                                <br/>
                                                <br/>
                                                <div id="div_folio" name="div_folio" style="display: <%= comprobanteFiscalDto!=null?(comprobanteFiscalDto.getIdFolio()>0?"inline":"none"):"none" %>">
                                                Folio: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                <input type="text" name="cfdi_folio" id="cfdi_folio" readonly
                                                    value="<%= comprobanteFiscalDto!=null?comprobanteFiscalDto.getFolioGenerado():"Sin Asignar" %>"
                                                    style="width: 80px;"/>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                Registro Patronal: &nbsp;
                                                <select name="id_registro_patronal" id="id_registro_patronal" >
                                                    <option value="-1">Seleccione un Registro Patronal</option>
                                                    <%= new NominaRegistroPatronalBO(user.getConn()).getNominaRegistroPatronalByIdHTMLCombo(idEmpresa, (nominaComprobanteDescripcion!=null?nominaComprobanteDescripcion.getIdNominaRegPatronal():-1), "" ) %>
                                                </select>
                                                <br/>
                                                <br/>
                                                <label>Estatus: &nbsp;</label>
                                                <input type="text" name="estatus_cfdi" id="estatus_cfdi" readonly disabled
                                                    value="<%= EstatusComprobanteBO.getEstatusName((comprobanteFiscalDto!=null?comprobanteFiscalDto.getIdEstatus():1))%>"
                                                    style="width: 120px;"/>
                                            </td>
                                            <td>
                                                Fecha Captura: 
                                                <input type="text" name="cfdi_fecha_captura" id="cfdi_fecha_captura" readonly
                                                    value="<%= DateManage.formatDateToNormal(comprobanteFiscalDto!=null?comprobanteFiscalDto.getFechaCaptura():new Date()) %>"
                                                    style="width: 80px;"/>
                                            </td>
                                        </tr>
                                        <tr>                                           
                                            <td>
                                                Fecha Pago: &nbsp;&nbsp;&nbsp;*
                                                <input type="text" name="cfdi_fecha_pago" id="cfdi_fecha_pago" readonly
                                                    value="<%= comprobanteFiscalDto!=null?DateManage.formatDateToNormal(comprobanteFiscalDto.getFechaPago()):(cfdiSesion.getFecha_pago()!=null?DateManage.formatDateToNormal(cfdiSesion.getFecha_pago()):"") %>"
                                                    style="width: 80px;"/>
                                            </td>
                                            <td>
                                                Fecha Inicial de Pago: &nbsp;&nbsp;&nbsp;*
                                                <input type="text" name="cfdi_fecha_pago_inicial" id="cfdi_fecha_pago_inicial" readonly
                                                    value="<%= nominaComprobanteDescripcion!=null?DateManage.formatDateToNormal(nominaComprobanteDescripcion.getFechaInicialPago()):"" %>"
                                                    style="width: 80px;"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                Fecha Final de Pago: &nbsp;&nbsp;&nbsp;*
                                                <input type="text" name="cfdi_fecha_pago_final" id="cfdi_fecha_pago_final" readonly
                                                    value="<%= nominaComprobanteDescripcion!=null?DateManage.formatDateToNormal(nominaComprobanteDescripcion.getFechaFinPago()):"" %>"
                                                    style="width: 80px;"/>
                                            </td>
                                            <td>
                                                Número de Días Pagados: &nbsp;&nbsp;&nbsp;*
                                                <input type="text" name="cfdi_numDias_pago" id="cfdi_numDias_pago" 
                                                    value="<%= nominaComprobanteDescripcion!=null?(int)nominaComprobanteDescripcion.getNumDiasPagados():"" %>"
                                                    style="width: 80px;"/>
                                            </td>
                                            
                                        </tr>
                                        <tr>
                                            <td>
                                                Forma de Pago: *
                                                <select name="id_forma_pago" id="id_forma_pago"
                                                        onchange="toggleDivParcialidad(this.value);">
                                                    <%= new FormaPagoBO(user.getConn()).getFormaPagosByIdHTMLCombo(comprobanteFiscalDto!=null?comprobanteFiscalDto.getIdFormaPago():-1) %>
                                                </select>
                                                <br/><br/>
                                                <div id="div_parcialidades" name="div_parcialidades" 
                                                     style="display: <%= comprobanteFiscalDto!=null?(comprobanteFiscalDto.getParcialidad()!=null?(comprobanteFiscalDto.getParcialidad().trim().length()>0?"inline":"none"):"none"):"none" %>">
                                                    Parcialidad: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*
                                                    <input type="text" name="parcialidad" id="parcialidad" 
                                                        maxlength="5" style="width: 80px;"
                                                        onkeypress="return validateNumberAndChar(event,47);"
                                                        onchange="return validarParcialidades(this.value);"
                                                        value="<%= comprobanteFiscalDto!=null?comprobanteFiscalDto.getParcialidad():"" %>"/>
                                                    ** formato: x/y
                                                    <br/>
                                                    Folio Fiscal Original:
                                                    <input type="text" name="parcialidad_folio_orig" id="parcialidad_folio_orig" 
                                                        maxlength="36" style="width: 250px;"
                                                        onchange="return validarUUID(this.value);"
                                                        value=""/>
                                                    <br/>
                                                    Serie del Folio Fiscal Original: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <input type="text" name="parcialidad_serie_orig" id="parcialidad_serie_orig" 
                                                        maxlength="20" style="width: 120px;"
                                                        value=""/>
                                                    <br/>
                                                    Fecha del Folio Fiscal Original:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <input type="text" name="parcialidad_fecha_orig" id="parcialidad_fecha_orig" 
                                                        maxlength="19" style="width: 120px;"
                                                        onchange="return validarDateTimeXML(this.value);"
                                                        value=""/>
                                                    <br/>
                                                    Monto del Folio Fiscal Original: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <input type="text" name="parcialidad_monto_orig" id="parcialidad_monto_orig" 
                                                        maxlength="16" style="width: 120px;"
                                                        onkeypress="return validateNumber(event);"
                                                        value=""/>
                                                </div>
                                            </td>
                                            <td>
                                                Método Pago: &nbsp;&nbsp;&nbsp;*
                                                <!--
                                                <input type="text" name="tipo_pago_descripcion" id="tipo_pago_descripcion" 
                                                        maxlength="100" style="width: 200px;"
                                                        value="<%= tipoPagoDto!=null?tipoPagoDto.getDescTipoPago():"" %>"/>
                                                -->
                                                <jsp:include page="../include/metodosPagoSelect.jsp">
                                                    <jsp:param name="id_name_tipo_pago" value="id_tipo_pago" />
                                                    <jsp:param name="id_name_select_metodo_pago" value="metodo_pago" />
                                                    <jsp:param name="id_name_desc_metodo" value="tipo_pago_descripcion" />
                                                    <jsp:param name="value_id_tipo_pago" value="<%= tipoPagoDto!=null?tipoPagoDto.getIdTipoPago():-1 %>" />
                                                </jsp:include>
                                                <br/>
                                                <br/>
                                                No. Cuenta: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                <input type="text" name="tipo_pago_num_cuenta" id="tipo_pago_num_cuenta" 
                                                       maxlength="4" style="width: 80px;"
                                                       onkeypress="return validateNumberAndChar(event,0);"
                                                       value="<%= tipoPagoDto!=null?tipoPagoDto.getNumeroCuenta():"" %>"/>
                                                ** últimos 4 dígitos
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                                
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
                            <form action="cfdi_factura_form.jsp" method="post" id="form_productos" name="form_productos">
                                                               
                                <table class="data" width="100%" cellpadding="0" cellspacing="0" style="vertical-align: top;">
                                    <thead>
                                        <tr>
                                            <td>Producto</td>
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
                                                <select size="1" name="producto_select" id="producto_select"  style="width:120px;"
                                                                onchange="selectProducto(this.value);"
                                                                class="flexselect">
                                                    <option value="-1"></option>
                                                    <%out.print(conceptoBO.getConceptosByIdHTMLCombo(idEmpresa, -1,0,0," AND MATERIA_PRIMA=0 ")); %>
                                                </select>                                                
                                            </td>
                                            <td>
                                                <textarea name="producto_descripcion" id="producto_descripcion" 
                                                rows="3" cols="35" >Seleccione un producto...</textarea>
                                            </td>
                                            <td>
                                                <!--<select id="producto_precio" name="producto_precio" onchange="selectProductoCalcularMonto();"
                                                            style="width: 90px;">
                                                    <option value="0">0</option>
                                                </select>-->
                                                <input type="text" maxlength="12" name="producto_precio" id="producto_precio" onchange="selectProductoCalcularMonto();"
                                                    style="text-align: right;" size="10" onkeypress="return validateNumber(event);"/>
                                            </td>
                                            <td>
                                                <input type="text" maxlength="1" name="producto_cantidad" id="producto_cantidad" 
                                                        style="text-align: right;" size="8" onblur="selectProductoCalcularMonto();"
                                                        onkeypress="return validateNumber(event);" onfocus="this.value=1;" readonly/>
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
                                Percepciones
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <form action="cfdi_factura_form.jsp" method="post" id="form_percepciones" name="form_percepciones">
                                                               
                                <table class="data" width="100%" cellpadding="0" cellspacing="0" style="vertical-align: top;">
                                    <thead>
                                        <tr>
                                            <td>Clave</td>
                                            <td>Descripción/Concepto</td>
                                            <td>Importe Gravado ($)</td>
                                            <td>Importe Exento ($)</td>
                                            <td>Acciones</td>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <select size="1" name="percepcion_select" id="percepcion_select"  style="width:120px;"
                                                                onchange="selectPercepcion(this.value);"
                                                                class="flexselect">
                                                    <option value="-1"></option>
                                                    <%out.print(percepcionBO.getNominaPercepcionsByIdHTMLCombo(idEmpresa, -1)); %>
                                                </select>                                                
                                            </td>
                                            <td>
                                                <textarea name="percepcion_descripcion" id="percepcion_descripcion" 
                                                rows="3" cols="35" >Seleccione un percepcion...</textarea>
                                            </td>
                                            
                                            <td>
                                                <!--<select id="percepcion_precio" name="percepcion_precio" onchange="selectPercepcionCalcularMonto();"
                                                            style="width: 90px;">
                                                    <option value="0">0</option>
                                                </select>-->
                                                <input type="text" maxlength="12" name="percepcion_precio_gravado" id="percepcion_precio_gravado" 
                                                    style="text-align: right;" size="10" onkeypress="return validateNumber(event);"/>
                                            </td>
                                           
                                            <td>
                                                <input type="text" maxlength="12" name="percepcion_precio_exento" id="percepcion_precio_exento"  
                                                    style="text-align: right;" size="10" onkeypress="return validateNumber(event);"/>
                                            </td>
                                            <td>
                                                <a href="javascript:void(0);" name="add_percepcion_action" onclick="addPercepcion();" id="add_percepcion_action">
                                                    <img src="../../images/icon_agregar.png" alt="Agregar Percepcion" height="20" width="20" class="help" title="Agregar Percepcion"/>
                                                </a>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                                
                                <center><div id="div_percepcion_ajax_loading" class="alert_warning noshadow" style="display: none;"></div></center>
                                <div id="div_ajax_percepcions" class="ajax_selected_box"></div>
                                
                            </form>
                        </div>
                    </div>
                    
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                Deducciones
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <form action="cfdi_factura_form.jsp" method="post" id="form_deducciones" name="form_deducciones">
                                                               
                                <table class="data" width="100%" cellpadding="0" cellspacing="0" style="vertical-align: top;">
                                    <thead>
                                        <tr>
                                            <td>Clave</td>
                                            <td>Descripción/Concepto</td>
                                            <td>Importe Gravado ($)</td>
                                            <td>Importe Exento ($)</td>
                                            <td>Acciones</td>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <select size="1" name="deduccion_select" id="deduccion_select"  style="width:120px;"
                                                                onchange="selectDeduccion(this.value);"
                                                                class="flexselect">
                                                    <option value="-1"></option>
                                                    <%out.print(deduccionBO.getNominaDeduccionsByIdHTMLCombo(idEmpresa, -1)); %>
                                                </select>                                                
                                            </td>
                                            <td>
                                                <textarea name="deduccion_descripcion" id="deduccion_descripcion" 
                                                rows="3" cols="35" >Seleccione un deduccion...</textarea>
                                            </td>
                                            
                                            <td>
                                                <!--<select id="deduccion_precio" name="deduccion_precio" onchange="selectPercepcionCalcularMonto();"
                                                            style="width: 90px;">
                                                    <option value="0">0</option>
                                                </select>-->
                                                <input type="text" maxlength="12" name="deduccion_precio_gravado" id="deduccion_precio_gravado" 
                                                    style="text-align: right;" size="10" onkeypress="return validateNumber(event);"/>
                                            </td>
                                           
                                            <td>
                                                <input type="text" maxlength="12" name="deduccion_precio_exento" id="deduccion_precio_exento"  
                                                    style="text-align: right;" size="10" onkeypress="return validateNumber(event);"/>
                                            </td>
                                            <td>
                                                <a href="javascript:void(0);" name="add_deduccion_action" onclick="addDeduccion();" id="add_deduccion_action">
                                                    <img src="../../images/icon_agregar.png" alt="Agregar Percepcion" height="20" width="20" class="help" title="Agregar Percepcion"/>
                                                </a>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                                
                                <center><div id="div_deduccion_ajax_loading" class="alert_warning noshadow" style="display: none;"></div></center>
                                <div id="div_ajax_deduccions" class="ajax_selected_box"></div>
                                
                            </form>
                        </div>
                    </div>
                    
                    <div class="twocolumn">
                        
                        <!--Columna izquierda: Descuentos-->
                        <div class="column_left" style="display: none">
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
                                                <td>
                                                    <%if (comprobanteFiscalDto==null){ %>
                                                    Descuento (%)
                                                    <% } %>
                                                    &nbsp;
                                                </td>
                                                <td>Importe Descuento</td>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td>
                                                    <%if (comprobanteFiscalDto==null){ %>
                                                    <input type="text" maxlength="5" name="descuento_tasa" id="descuento_tasa"
                                                            style="text-align: right;" size="12" value="<%=cfdiSesion!=null?cfdiSesion.getDescuento_tasa():"0"%>"
                                                            onkeypress="return validateNumber(event);"
                                                            onchange="calculaDescuento();"/>
                                                    <% } %>
                                                    &nbsp;
                                                </td>
                                                <td>
                                                    <h3>- 
                                                    <input type="text" name="descuento_importe" id="descuento_importe"
                                                            style="text-align: right;" size="12"
                                                            onkeypress="return validateNumber(event);"
                                                            value="<%= comprobanteFiscalDto!=null?cfdiSesion.getDescuentoImporte():0%>"
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
                                                           style="width: 100%;" value="<%= comprobanteFiscalDto!=null?comprobanteFiscalDto.getMotivoDescuento():cfdiSesion.getDescuento_motivo() %>" maxlength="100"/>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                    <center><div id="div_descuento_ajax_loading" class="alert_warning noshadow" style="display: none;"></div></center>
                            </div>
                        </div>
                        <!--Fin columna izquierda: Descuentos-->
                         
                        <!--Columna derecha: Impuestos-->
                        <div class="column_right" style="height: 244px;">
                            <div class="header">
                                <span>
                                    Impuesto ISR
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                <form action="cfdi_factura_form.jsp" id="form_impuestos" name="form_impuestos" method="post">
                                    <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                        <thead>
                                            <tr>                                                 
                                                <td>
                                                    <h2>
                                                    Porcentaje de ISR:
                                                    <input type="text" maxlength="8" name="pocentajeISR" id="pocentajeISR"
                                                            style="text-align: right;" size="12" 
                                                            value="<%= nominaComprobanteDescripcion!=null?nominaComprobanteDescripcion.getIsrImpuestoPorcentaje():"0" %>"
                                                            onkeypress="return validateNumber(event);" onchange="addImpuestoISR();"/>
                                                    </h2>
                                                </td>
                                            </tr
                                            <tr>                                                 
                                                <td>
                                                    <h2>
                                                    Monto de ISR:
                                                    <input type="text" maxlength="9" name="montoISR" id="montoISR"
                                                            style="text-align: right;" size="12"
                                                            value="<%= nominaComprobanteDescripcion!=null?nominaComprobanteDescripcion.getIsrMontoImpuesto():"0" %>"
                                                            onkeypress="return validateNumber(event);" onchange="addImpuestoISR();"/>
                                                    </h2>
                                                </td>
                                            </tr>
                                        </thead>                                       
                                    </table>
                                 </form>                                
                            </div>
                        </div>
                        <!--<div class="column_right">
                            <div class="header">
                                <span>
                                    Impuestos
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                <form action="cfdi_factura_form.jsp" id="form_impuestos" name="form_impuestos" method="post">
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
                                                            <//%
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
                        <div class="column_left" id="div_comentarios" style="height: 181px; ">
                            <div class="header">
                                <span>
                                    Comentarios
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <textarea cols="60" rows="5" id="comentarios" name="comentarios"><%= comprobanteFiscalDto!=null?comprobanteFiscalDto.getComentarios():cfdiSesion.getComentarios() %></textarea>
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
                                <form action="cfdi_factura_form.jsp" id="form_acciones" name="form_acciones" method="post">                            
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
                                                    <select id="tipo_moneda" name="tipo_moneda" onchange="toggleDivMoneda(this.value);">
                                                        <option value="-1" selected>PESOS (MXN)</option>
                                                        <option value="1">DOLAR (USD)</option>
                                                        <option value="2">EUROS (EUR)</option>
                                                    </select>
                                                    <br class="clear"/>
                                                    <div id="div_moneda" name="div_moneda"
                                                         style="display: <%= comprobanteFiscalDto!=null?(comprobanteFiscalDto.getTipoDeCambio()>1?"inline":"none"):"none" %>">
                                                        Tipo de Cambio: *
                                                        <input type="text" name="tipo_cambio" id="tipo_cambio" 
                                                            maxlength="6" style="width: 80px;"
                                                            onkeypress="return validateNumber(event);"
                                                            value="<%= comprobanteFiscalDto!=null?comprobanteFiscalDto.getTipoDeCambio():"1" %>"/>
                                                    </div>
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
                            <form action="cfdi_factura_form.jsp" id="form_acciones" name="form_acciones" method="post">                            
                                <div>
                                    <p>
                                        <input type="button" id="regresar" value="Cancelar y Regresar" onclick="history.back();"/>
                                        &nbsp;&nbsp;&nbsp;
                                        <!--<//% if (comprobanteFiscalDto==null){ %>-->
                                        <input type="button" id="guardar" value="Guardar Nueva" onclick="grabar('');"/>
                                        <!--&nbsp;&nbsp;&nbsp;
                                        <input type="button" id="guardar_enviar" value="Guardar Nueva y Enviar" onclick="enviarYgrabar();"/>-->
                                        <!--<//%}%>-->
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
            mostrarCalendario();
           iniciarFlexSelect();
           <%if (cfdiSesion.getCliente()!=null){%>
               selectCliente(<%=cfdiSesion.getCliente().getIdCliente() %>);
           <%}%>
        </script>
    </body>
</html>
<%}%>