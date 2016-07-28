<%-- 
    Document   : cfdi_factura_form
    Created on : 13-dic-2012, 19:18:28
    Author     : ISCesarMartinez
--%>

<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.DatosPersonalizadosBO"%>
<%@page import="com.tsp.sct.dao.jdbc.DatosPersonalizadosDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.DatosPersonalizados"%>
<%@page import="com.tsp.sct.dao.dto.SarAreaEntrega"%>
<%@page import="com.tsp.sct.bo.SarAreaEntregaBO"%>
<%@page import="com.tsp.sgfens.sesion.ComprobanteFiscalSesion"%>
<%@page import="com.tsp.sct.util.StringManage"%>
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

    String endAccName = request.getParameter("endAccName")!=null?new String(request.getParameter("endAccName").getBytes("ISO-8859-1"),"UTF-8"):"";
    //int sarIdAreaEntrega = 0;
    SarAreaEntrega[] sarAreas = new SarAreaEntrega[0];
    String filtroBusquedaAreasSAR = " AND id_cliente>0 AND id_sar_usuario IN (SELECT id_sar_usuario FROM sar_usuario_proveedor WHERE ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + "))";
    if (endAccName.equals("cfdi_sar")){
        //Consultamos Áreas Disponibles de entrega a SAR
        SarAreaEntregaBO sarAreaEntregaBO = new SarAreaEntregaBO(user.getConn());
        sarAreas = sarAreaEntregaBO.findSarAreaEntregas(-1, -1, 0, 0, filtroBusquedaAreasSAR);
    }

    ComprobanteFiscalBO comprobanteFiscalBO = new ComprobanteFiscalBO(user.getConn());
    ComprobanteFiscal comprobanteFiscalDto = null;
    TipoPago tipoPagoDto = null;

    Cliente clienteDto = null;
    if (idComprobanteFiscal > 0){
        comprobanteFiscalBO = new ComprobanteFiscalBO(idComprobanteFiscal,user.getConn());
        comprobanteFiscalDto = comprobanteFiscalBO.getComprobanteFiscal();

        clienteDto = new ClienteBO(comprobanteFiscalDto!=null?comprobanteFiscalDto.getIdCliente():-1,user.getConn()).getCliente();
        tipoPagoDto = new TipoPagoBO(comprobanteFiscalDto.getIdTipoPago(),user.getConn()).getTipoPago();
    }

    ConceptoBO conceptoBO = new ConceptoBO(user.getConn());

    DatosPersonalizados[] listdpDto = new DatosPersonalizados[0];
    Empresa empresaPadre = new EmpresaBO(user.getConn()).getEmpresaMatriz(idEmpresa);
    String parametrosDatosPersGuardar = "";
    try{
        listdpDto = new DatosPersonalizadosBO(user.getConn()).findDatosPersonalizados(-1, empresaPadre.getIdEmpresa() , 0, 0, "");
    }catch(Exception ex){}
    
    Empresa empresaActual =  new EmpresaBO(idEmpresa, user.getConn()).getEmpresa();
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
                
                <% if (endAccName.equals("cfdi_sar")){
                    out.print(sarAreas.length<=0?"alertaSinAreasConfiguradas();":"") ;
                   }
                %>
                        
            });
            
            function recuperarListados(){
                recargarListaProducto();
                recargarListaServicio();
                //recargarListaImpuestos();
                <%= listdpDto.length>0?"recargarListaDatosPersonalizados();":"" %>
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
                                 validarCreditoDisponible(r);
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
                
                var idFolios = $("#cfdi_serie").val();
                var tipoPagoDescripcion = $("#tipo_pago_descripcion").val();
                var tipoPagoNumCuenta = $("#tipo_pago_num_cuenta").val();
                var idFormaPago = $("#id_forma_pago").val();
                var metodoPago = $("#metodo_pago").val();
                var idTipoPago = $("#id_tipo_pago").val();
                
                var condicionesPagoDescripcion =$("#condiciones_pago_descripcion").val();
                
                var parcialidad ="";
                var parcialidadFolioOrig ="";
                var parcialidadSerieOrig = "";
                var parcialidadFechaOrig = "";
                var parcialidadMontoOrig = "";
                
                if (idFormaPago===2){
                    //Si la Forma de Pago es Parcialidades se recuperan los valores
                    parcialidad = $("#parcialidad").val();
                    parcialidadFolioOrig = $("#parcialidad_folio_orig").val();
                    parcialidadSerieOrig = $("#parcialidad_serie_orig").val();
                    parcialidadFechaOrig = $("#parcialidad_fecha_orig").val();
                    parcialidadMontoOrig = $("#parcialidad_monto_orig").val();
                }
                
                var sarIdAreaEntrega = $("#sar_id_area_entrega").val();
                
                //Campos Dinamicos de Datos Personalizados
                <% 
                for (DatosPersonalizados dpDto : listdpDto) {
                    String variable = "v_"+dpDto.getVariable();
                    String controlId = "dp_"+dpDto.getVariable();
                    String parametroPost = "dp_"+dpDto.getVariable();
                    //Forma:   var v_{variable} = $('#dp_{variable}').val();
                    out.print("var "+variable+" = $('#"+controlId+"').val();");
                    
                    //Forma:   , dp_{variable} : v_{variable}
                    parametrosDatosPersGuardar += ", " + parametroPost + " : " + variable  ;
                } 
                %>
                                        
                //Fin Campos Dinamicos de Datos Personalizados
                
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
                                condiciones_pago_descripcion : condicionesPagoDescripcion,
                                sar_id_area_entrega: sarIdAreaEntrega, 
                                metodo_pago : metodoPago, id_tipo_pago : idTipoPago
                                <%= parametrosDatosPersGuardar %>
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
                                        <% if (endAccName.equals("cfdi_sar")) { %>
                                        location.href="../cfdi_sar/cfdi_sar_factura_campos_extra_form.jsp";
                                        <% } else{ %>
                                        location.href="../cfdi_factura/cfdi_factura_list.jsp";
                                        <% } %>
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
                if(jQuery.trim($("#id_forma_pago").val())===2){
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
                
                <% if (endAccName.equals("cfdi_sar")){ %>
                if(jQuery.trim($("#sar_id_area_entrega").val())<=0 ){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "SAR Area Entrega"  es requerido</center>',{'animate':true});
                    return false;
                }       
                <%}%>
                
                //alert('cambiar validacion a true');
                return true;
            }
            
            //FUNCTION PARA MOSTRAR MENSAJE DE CREDITOS DE EMERGENCIA
            function validarCreditoDisponible(listaCorreos){
                
                var creditosDisponibles = $("#creditosDisponibles").val();
                
                if(creditosDisponibles <= 0){
                    apprise('Tus créditos contratados de prepago se han agotado, se ocuparan créditos de emergencia y se te facturan al siguiente día hábil con un costo de $20.00 mas IVA por crédito. Contacta a tu vendedor de Lunes a viernes de 9 a.m. a 7 p.m. para adquirir un paquete de creditos de prepago, ¿desea continuar?', {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, 
                        function(r){
                            if(r){
                                // Usuario dio click 'Yes'
                                grabar(listaCorreos);
                            }
                    });
                }else{
                    grabar(listaCorreos)
                }
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
                    if(idCliente>0){
                        $.ajax({
                            type: "POST",
                            url: "cfdi_factura_ajax.jsp",
                            data: { mode: '1', id_producto_select: idProducto,idCliente : idCliente },
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
                                   options_precio += '<option value="' + arrayRespuesta["precioMedioMayoreo"] + '">' + arrayRespuesta["precioMedioMayoreo"] + ' [med.]</option>';
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

                        //obtenemos existencia de alamcenes
                        $('#almacen_ref').attr('href','../../jsp/includeProductoSelect/almacen_select_form.jsp?mode=1&idProducto='+idProducto);
                    }else{
                     apprise('<center><img src=../../images/warning.png> <br/>'+ "Selecciona primero un Cliente" +'</center>',{'animate':true});
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
                almacenId = $('#almacen_select').val();
                if (validarAddProducto(productoId,productoCantidad,productoMonto,unidadId,almacenId)){
                    if(productoId>0){
                        $.ajax({
                            type: "POST",
                            url: "cfdi_factura_ajax.jsp",
                            data: { mode: '2', producto_id : productoId, 
                                producto_cantidad : productoCantidad, producto_monto : productoMonto,
                                producto_precio : productoPrecio, producto_unidad: unidadText, unidad_id: unidadId, id_tipo_comprobante : '2',
                                almacen_id : almacenId},
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
            function validarAddProducto(idProducto,cantidad,monto, idUnidad,almacenId){
                if(idProducto<=0 || cantidad<0 || monto<0 || idUnidad<=0 //|| almacenId <=0
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
            
            
            
            //****-----------------ACCIONES DE SELECCION CLIENTE
            
            //Recarga de BD dinámicamente el listado de clientes de la empresa
            function recargarSelectClientes(idCliente){
                if ( typeof(idCliente) !== "undefined" && idCliente !== null ) {
                    if (idCliente<=0)
                        idCliente = <%= comprobanteFiscalDto!=null?comprobanteFiscalDto.getIdCliente():(cfdiSesion.getCliente()!=null?cfdiSesion.getCliente().getIdCliente():-1) %>;
                }else {
                    idCliente = <%= comprobanteFiscalDto!=null?comprobanteFiscalDto.getIdCliente():(cfdiSesion.getCliente()!=null?cfdiSesion.getCliente().getIdCliente():-1) %>;
                }
                
                $.ajax({
                    type: "POST",
                    url: "cfdi_factura_ajax.jsp",
                    data: { mode: '20', id_cliente :  idCliente},
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
                               
                               $('#cliente_correo').val(arrayRespuestaCliente["correo"]);
                               
                               var direccionCompuesta = arrayRespuestaCliente["calle"] + ' ' +  arrayRespuestaCliente["numero"] + ' ' +  arrayRespuestaCliente["numeroInterior"] + ' ' +  arrayRespuestaCliente["colonia"];
                               $('#cliente_rfc').html('RFC: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>' + arrayRespuestaCliente["rfcCliente"] + '</b>');
                               $('#cliente_direccion').html('Dirección: <b>' + direccionCompuesta + '</b>');
                               $('#cliente_cp').html('C.P.: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>' + arrayRespuestaCliente["codigoPostal"] + '</b>');
                               
                               
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
            
            //****-----------------FIN ACCIONES DE SELECCION CLIENTE
            
            //***------------------ACCIONES CFDI SAR
            <% if (endAccName.equals("cfdi_sar")){ %>
        
            function alertaSinAreasConfiguradas(){
              apprise('<center><img src=../../images/info.png> <br/>No ha configurado ningún área de Entrega SAR válida. Proceda primero con la configuración.</center>',{'animate':true},
                    function(r){
                        location.href="../cfdi_sar/cfdi_sar_factura_list.jsp";
                });
            }
            
            function cambioAreaEntregaSAR(idAreaEntregaSAR){
                if(idAreaEntregaSAR>0){
                    $.ajax({
                        type: "POST",
                        url: "../cfdi_sar/cfdi_sar_factura_list_ajax.jsp",
                        data: { mode: '1', sar_id_area_entrega : idAreaEntregaSAR },
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
                               
                               //Seleccionamos de forma automatica al Cliente
                               selectCliente(arrayRespuestaCliente["idCliente"]);
                               //Para que se muestre el elegido en lista
                               recargarSelectClientes(arrayRespuestaCliente["idCliente"]);
                               
                               $("#ajax_loading").fadeOut("slow");
                               $("#action_buttons").fadeIn("slow");
                           }else{
                               $("#ajax_loading").html(datos);
                               $("#action_buttons").fadeIn("slow");
                           }
                        }
                    });
                }else{
                    recargarSelectClientes(-1);
                    $('#cliente_rfc').html('RFC: ');
                    $('#cliente_direccion').html('Dirección: ');
                    $('#cliente_cp').html('C.P.:');
                    apprise('<center><img src=../../images/warning.png> <br/>Seleccione un Area de Entrega SAR Valida</center>',{'animate':true});
                }
            }
            
            <%}%>
            //***------------------FIN ACCIONES CFDI SAR
            
            //----------- ACCIONES DATOS PERSONALIZADOS
            function recargarListaDatosPersonalizados(){
                $.ajax({
                    type: "POST",
                    url: "cfdi_factura_ajax.jsp",
                    data: { mode: '23' },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#div_datos_personalizados_ajax_loading").html('<div style=""><img src="../../images/ajax_loader4.gif" alt="Cargando.." /><br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /><br/>Procesando... </div>');
                        $("#div_datos_personalizados_ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#div_ajax_datos_personalizados").html(datos);
                           $("#div_datos_personalizados_ajax_loading").fadeOut("slow");
                           $("#action_buttons").fadeIn("slow");
                       }else{
                           $("#div_datos_personalizados_ajax_loading").html(datos);
                           $("#action_buttons").fadeIn("slow");
                       }
                    }
                });
            }
            //----------- FIN ACCIONES DATOS PERSONALIZADOS
            
            function mostrarCalendario(){
                $( "#cfdi_fecha_pago" ).datepicker({
                        minDate: 0,
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
            
                       
            function selectAlmacenFromPopup(idAlmacen, nombreAlmacen){
                
                $("#almacen_select").val(idAlmacen);
                $('#almacen_select'+postnameFlexSelect).val(nombreAlmacen);
                
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
                        Comprobante Fiscal <%= comprobanteFiscalDto!=null?"ID " +comprobanteFiscalDto.getIdComprobanteFiscal():"" %>
                        <%= endAccName.equals("cfdi_sar")? " SAR":"" %>
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
                                    <img src="../../images/icon_consultar.png" alt="Mostrar CFDI" class="help" title="Previsualizar CFDI"/><br/>
                                </a>
                                <% } %>
                                UUID: <b><i><%= comprobanteFiscalDto!=null?comprobanteFiscalDto.getUuid():"Sin Asignar" %></i></b>
                                &nbsp;&nbsp;
                            </div>
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <form action="cfdi_factura_form.jsp" id="select_cliente" name="select_cliente" method="post">
                                
                                <input type="hidden" id="creditosDisponibles" name="creditosDisponibles" value="<%=empresaActual.getFoliosDisponibles()<0?0:empresaActual.getFoliosDisponibles()%>" />
                                
                                <table class="data" width="100%" cellpadding="0" cellspacing="0" style="vertical-align: top;">
                                    <tbody>
                                        <tr>
                                            <% if (endAccName.equals("cfdi_sar")){ %>
                                            <td>
                                                Área Entrega SAR: *
                                                <select name="sar_id_area_entrega" id="sar_id_area_entrega"
                                                        onchange="cambioAreaEntregaSAR(this.value);">
                                                    <option value="-1">--Selecciona Área SAR--</option>
                                                    <%= new SarAreaEntregaBO(user.getConn()).getSarAreaEntregasByIdHTMLCombo(-1, -1, filtroBusquedaAreasSAR) %>
                                                </select>
                                            </td>
                                            <% } %>
                                            <td>
                                                Cliente: *
                                                <div id="div_select_cliente" name="div_select_cliente" style="display: inline;" >
                                                </div>
                                                &nbsp;&nbsp;&nbsp;
                                                <% if (!endAccName.equals("cfdi_sar")) {%>
                                                <a href="../../jsp/catClientes/catClientes_form.jsp?acc=3" id="btn_nuevo_cliente" title="Nuevo Cliente"
                                                class="modalbox_iframe">
                                                    <img src="../../images/icon_agregar.png" alt="nuevo cliente" class="help" title="Nuevo Cliente"/><br/>
                                                </a>
                                                <%}%>
                                                <br/>
                                                <input type="hidden" id="cliente_correo" name="cliente_correo" value="<%= clienteDto!=null?clienteDto.getCorreo():"" %>"/>
                                                <div id="div_datos_cliente" name="div_datos_cliente">
                                                    <div id="cliente_rfc">
                                                        RFC: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                        <%= clienteDto!=null?clienteDto.getRfcCliente():"" %>
                                                    </div>
                                                    <div id="cliente_direccion">
                                                        Dirección: 
                                                        <%= clienteDto!=null?clienteDto.getCalle() + " " + clienteDto.getNumero() + " " + clienteDto.getNumeroInterior() + " " +clienteDto.getColonia() :"" %>
                                                    </div>
                                                    <div id="cliente_cp">
                                                        C.P.: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                        <%= clienteDto!=null?clienteDto.getCodigoPostal():"" %>
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
                                                
                                            </td>
                                            <td>
                                                Fecha Pago: &nbsp;&nbsp;&nbsp;*
                                                <input type="text" name="cfdi_fecha_pago" id="cfdi_fecha_pago" readonly
                                                    value="<%= comprobanteFiscalDto!=null?DateManage.formatDateToNormal(comprobanteFiscalDto.getFechaPago()):(cfdiSesion.getFecha_pago()!=null?DateManage.formatDateToNormal(cfdiSesion.getFecha_pago()):"") %>"
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
                                                        maxlength="100" style="width: 238px;"
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
                                                Condiciones de Pago: &nbsp;&nbsp;&nbsp;
                                                <input type="text" name="condiciones_pago_descripcion" id="condiciones_pago_descripcion" 
                                                        maxlength="100" style="width: 200px;"
                                                        value="<%= comprobanteFiscalDto!=null?comprobanteFiscalDto.getCondicionesPago():"" %>"/>
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
                                            <td>Unidad</td>
                                            <td>Precio ($)</td>
                                            <td>Almacén</td>
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
                                                    <%out.print(conceptoBO.getConceptosByIdHTMLCombo(idEmpresa, -1,0,250," AND MATERIA_PRIMA=0 ")); %>
                                                </select>
                                                
                                                &nbsp;&nbsp;
                                                <a href="../../jsp/includeProductoSelect/producto_by_categoria_select_form.jsp?mode=factura" id="btn_select_producto_by_categoria" title="Busqueda Avanzada"
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
                                                <a id="almacen_ref" class="modalbox_iframe" href="../../jsp/includeProductoSelect/almacen_select_form.jsp?mode=100"> 
                                                <select id="almacen_select" name="almacen_select" class="flexselect"
                                                        style="width: 90px;">                                                        
                                                    <option value="-1"></option>
                                                </select>                                                                                                                                              
                                                </a>                                                
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
                            <form action="cfdi_factura_form.jsp" method="post" id="form_servicios" name="form_servicios">
                                                               
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
                                 
                    <% // Mostramos apartado solo si la empresa tiene al menos un dato personalizado 
                    if (listdpDto.length>0){
                    %> 
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                Datos Personalizados
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <center><div id="div_datos_personalizados_ajax_loading" class="alert_warning noshadow" style="display: none;"></div></center>
                            <div id="div_ajax_datos_personalizados" class="ajax_selected_box"></div>
                        </div>
                    </div>
                    <% } %>
                    
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
                        <div class="column_right">
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
                                        <% if (comprobanteFiscalDto==null){ %>
                                        <input type="button" id="guardar" value="Guardar Nueva" onclick="validarCreditoDisponible('');"/>
                                        &nbsp;&nbsp;&nbsp;
                                        <% if (!endAccName.equals("cfdi_sar")) { %>
                                        <input type="button" id="guardar_enviar" value="Guardar Nueva y Enviar" onclick="enviarYgrabar();"/>
                                        <% } %>
                                        <%}%>
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