<%-- 
    Document   : cfdi_factura_proceso_automatico
    Created on : 19/02/2014, 01:10:28 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.EstatusComprobanteBO"%>
<%@page import="com.tsp.sct.dao.dto.ComprobanteFiscal"%>
<%@page import="com.tsp.sct.dao.dto.NominaComprobanteDescripcion"%>
<%@page import="com.tsp.sct.bo.FormaPagoBO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="java.util.Date"%>
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
        
        
        ComprobanteFiscal comprobanteFiscalDto = null;
        
        NominaComprobanteDescripcion nominaComprobanteDescripcion = null;
        
        
        /*Cliente clienteDto = null;
        if (idComprobanteFiscal > 0){
            comprobanteFiscalBO = new ComprobanteFiscalBO(idComprobanteFiscal);
            comprobanteFiscalDto = comprobanteFiscalBO.getComprobanteFiscal();
            
            clienteDto = new ClienteBO(comprobanteFiscalDto!=null?comprobanteFiscalDto.getIdCliente():-1).getCliente();
            tipoPagoDto = new TipoPagoBO(comprobanteFiscalDto.getIdTipoPago()).getTipoPago();
        }*/        

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
                              
                var fechaPago = $("#cfdi_fecha_pago").val();
                var fechaPagoInicial = $("#cfdi_fecha_pago_inicial").val();
                var fechaPagoFinal = $("#cfdi_fecha_pago_final").val();
                //var numDiasPago = $("#cfdi_numDias_pago").val();
                
                if(validar(0)){
                    $.ajax({
                        type: "POST",
                        url: "cfdi_factura_ajax.jsp",
                        data: { mode: '34' , fecha_pago : fechaPago, fechaPagoInicial : fechaPagoInicial, fechaPagoFinal : fechaPagoFinal},
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
                               apprise2('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                    function(r){
                                        //location.href="../catNominaCFDI_factura/cfdi_factura_list.jsp";
                                        parent.parent.location.href="../catNominaCFDI_factura/cfdi_factura_list.jsp";
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
                //alert('cambiar validacion a true');
                return true;
            }
            
            
            
            //****---------------------------------FIN GUARDAR
                        
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
    <body class="nobg">
        <div class="content_wrapper" style="height: 334px;">
            <!-- Inicio de Contenido -->
            <div id="content" style="margin-left: 0px;">

                <div class="inner">
                    <!--<h1>
                        Comprobante Fiscal Nómina <//%= comprobanteFiscalDto!=null?"ID " +comprobanteFiscalDto.getIdComprobanteFiscal():"" %>                                                
                    </h1>-->

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;top: 100px;"></div>

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
                                <table class="data" width="100%" cellpadding="0" cellspacing="0" style="vertical-align: top;">
                                    <tbody>                                        
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
                                                <!--Forma de Pago: *-->
                                                <select name="id_forma_pago" id="id_forma_pago" style="display: none;"
                                                        onchange="toggleDivParcialidad(this.value);">
                                                    <%= new FormaPagoBO(user.getConn()).getFormaPagosByIdHTMLCombo(comprobanteFiscalDto!=null?comprobanteFiscalDto.getIdFormaPago():1) %>
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
                                        </tr>
                                    </tbody>
                                </table>
                                
                            </form>
                        </div>
                    </div>                   
                                      
                    
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
                                        <input type="button" id="guardar" value="Generar Nóminas" onclick="grabar('');"/>
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