<%-- 
    Document   : catCobranzaAbono_form
    Created on : 07-ene-2013, 13:28:33
    Author     : ISCesarMartinez
--%>

<%@page import="java.text.NumberFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.tsp.sct.bo.VentaMetodoPagoBO"%>
<%@page import="com.tsp.sct.dao.dto.VentaMetodoPago"%>
<%@page import="com.tsp.sct.bo.VentaBO"%>
<%@page import="com.tsp.sct.bo.ComprobanteFiscalBO"%>
<%@page import="com.tsp.sct.bo.SGPedidoBO"%>
<%@page import="com.tsp.sct.dao.dto.ComprobanteFiscal"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedido"%>
<%@page import="com.tsp.sct.bo.SGCobranzaMetodoPagoBO"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.bo.SGCobranzaAbonoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensCobranzaAbono"%>
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
         *   1 = nuevo
         *   2 = consultar
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        
        /*
         * Parámetros
         */
        int idCobranzaAbono = 0;
        try {
            idCobranzaAbono = Integer.parseInt(request.getParameter("idCobranzaAbono"));
        } catch (NumberFormatException e) {
        }
        
        int idComprobanteFiscal = -1;
        int idPedido = -1;
        try{ idComprobanteFiscal = Integer.parseInt(request.getParameter("idComprobanteFiscal")); }catch(NumberFormatException e){}
        try{ idPedido = Integer.parseInt(request.getParameter("idPedido")); }catch(NumberFormatException e){}
        
        
        SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(user.getConn());
        SgfensCobranzaAbono cobranzaAbonoDto = null;
        if (idCobranzaAbono > 0){
            cobranzaAbonoBO = new SGCobranzaAbonoBO(idCobranzaAbono, user.getConn());
            cobranzaAbonoDto = cobranzaAbonoBO.getCobranzaAbono();
        }
        
        if (mode.equals("2") && cobranzaAbonoDto!=null){
            idPedido = cobranzaAbonoDto.getIdPedido();
            idComprobanteFiscal = cobranzaAbonoDto.getIdComprobanteFiscal();
        }
        
        SgfensPedido pedidoDto = null;
        ComprobanteFiscal comprobanteFiscalDto = null;

        SGPedidoBO pedidoBO = null;
        ComprobanteFiscalBO comprobanteFiscalBO = null;
        try{
            if (idPedido>0){
                pedidoBO = new SGPedidoBO(idPedido,user.getConn());
                pedidoDto = pedidoBO.getPedido();
            }
            if (idComprobanteFiscal>0){
                comprobanteFiscalBO = new ComprobanteFiscalBO(idComprobanteFiscal,user.getConn());
                comprobanteFiscalDto = comprobanteFiscalBO.getComprobanteFiscal();
            }
        }catch(Exception ex){
            ex.printStackTrace();
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

        <jsp:include page="../include/jsFunctions.jsp"/>
        
        <script type="text/javascript">
            
            function grabar(){
                if(validar()){
                    $.ajax({
                        type: "POST",
                        url: "catCobranzaAbono_ajax.jsp",
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
                                            location.href = "catCobranzaAbono_list.jsp?idPedido="+<%=idPedido%>+"&idComprobanteFiscal="+<%=idComprobanteFiscal %>;
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
            }

            function validar(){
                if($("#id_metodo_pago").val()<=0){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "método de pago" es requerido</center>',{'animate':true});
                    $("#id_metodo_pago").focus();
                    return false;
                }
                
                return verificaMontoAbono();
                
                //return true;
            }
            
            function verificaMontoAbono(){
                
                var key = window.event ? event.keyCode : event.which;

                if (event.keyCode != 13 && event.keyCode != 127) {//en caso de que de borrar o enter no valide el monto
                
                    var montoAbono = getFloatFromInput($('#monto'));
                    //$('#monto').val(montoAbono);

                    if (montoAbono<=0){
                        apprise('<center><img src=../../images/warning.png>El monto del abono debe ser mayor a 0.</center>',{'animate':true},
                            function(r){
                                $('#monto').val("");
                                return false;
                        });
                        return false;
                    }

                    var saldoAdeudo = getFloatFromInput($('#saldo_adeudo'));                
                    if (saldoAdeudo<=0){
                        apprise('<center><img src=../../images/warning.png>La deuda tiene un saldo de 0, no es necesario realizar mas abonos.</center>',{'animate':true},
                            function(r){
                                $('#monto').val(0);
                                return false;
                        });
                        return false;
                    }

                    //alert('monto abono: ' +montoAbono + '  :::: saldo adeudo: ' + saldoAdeudo);
                    if (parseFloat(montoAbono)<=parseFloat(saldoAdeudo)){
                        var nuevoSaldoAdeudo = (saldoAdeudo - montoAbono).toFixed(2);

                        $('#nuevo_saldo_adeudo').val(nuevoSaldoAdeudo);
                        return true;
                    }else{
                        apprise('<center><img src=../../images/warning.png>El monto del abono, no puede superar el saldo restante del adeudo.</center>',{'animate':true},
                            function(r){
                                //$('#monto').val(saldoAdeudo);
                                $('#nuevo_saldo_adeudo').val('?');
                                return false;
                        });
                        return false;
                    }
                }
            }
            
            function verDetalleOperacionBancaria(idBancoOperacion){
                //$.fancybox("test");
                //location.href="../../jsp/catCobroTarjeta/catCobroTarjeta_ticket.jsp?idBancoOperacion=" + idBancoOperacion + "&acc=ticketImprime";
                
                $.fancybox({
			'title'		: 'Detalles de Cobro TDC',
			'href'		: '../../jsp/catCobroTarjeta/catCobroTarjeta_ticket.jsp?idBancoOperacion=' + idBancoOperacion + '&acc=popup',
			'type'		: 'iframe'
		});
                
                return;
            }
            
            
            function metodoPago(idPedido){
                var idMetodoPago = $(id_metodo_pago).val();
                if(idMetodoPago == 6){                   
                    
                    $.ajax({
                        type: "POST",
                        url: "catCobranzaAbono_ajax.jsp",
                        data: {mode:"2",idPedido:idPedido},                       
                        success: function(datos){                            
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#respuestaBonificacion").html(datos);
                               
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               $("#respuestaBonificacion").html(datos);
                               $("#respuestaBonificacion").fadeIn("slow");                              
                               
                           }
                        }
                    });                        
                }                
            }
            
            function mostrarCalendario(){
                $( "#cobranza_fecha_generacion" ).datepicker({
                            //minDate: 0,
                            gotoCurrent: true,
                            changeMonth: true,
                            beforeShow: function(input, datepicker) {
                                setTimeout(function() {
                                        $(datepicker.dpDiv).css('zIndex', 100);
                                }, 500)}
                    });
            }
        
           /// Mapa
            var coordenada;
            var marker;
            var map;
            
            
            function initialize() {
                
                var coordenada = new google.maps.LatLng(<%=cobranzaAbonoDto!=null?cobranzaAbonoDto.getLatitud():0 %>,<%=cobranzaAbonoDto!=null?cobranzaAbonoDto.getLongitud():0%>);
                
                var mapOptions = {
                    zoom: 14,
                    center: coordenada
                };

                 map = new google.maps.Map(document.getElementById('map_canvas'),
                          mapOptions);
                          
                 map.controls[google.maps.ControlPosition.TOP_RIGHT].push(
                FullScreenControl(map, 'Pantalla Completa',
                'Salir Pantalla Completa'));

                 marker = new google.maps.Marker({
                    map:map,
                    animation: google.maps.Animation.DROP,
                    position: coordenada,
                    title: "Abono ID: " + <%=cobranzaAbonoDto!=null?cobranzaAbonoDto.getIdCobranzaAbono():0 %>
                 });
                
                 google.maps.event.addListener(marker, 'click', function() {
                    infowindow.open(map,marker);  
                });
                
               
                                
            }
        
            function loadScript() {
                var script = document.createElement('script');
                script.type = 'text/javascript';
                script.src = 'https://maps.googleapis.com/maps/api/js?libraries=geometry,drawing&sensor=false&' +
                    'callback=initialize';
                document.body.appendChild(script);
            }
            
            window.onload = loadScript;
            
            
            /// Fin Mapa           
                    
            </script>
                                        
           
    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Ventas - Cobranza</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_ventas1.png" alt="icon"/>
                                    <% if(cobranzaAbonoDto!=null){%>
                                    Consultar Abono ID <%=cobranzaAbonoDto!=null?cobranzaAbonoDto.getIdCobranzaAbono()+ (cobranzaAbonoDto.getIdEstatus()==2?" - CANCELADO":""):"" %>
                                    <%}else{%>
                                    Registrar Abono 
                                        <% if (pedidoDto!=null){%>
                                            de Pedido con Folio <%=pedidoDto.getFolioPedido()%>
                                        <%}%>
                                        <% if (comprobanteFiscalDto!=null){%>
                                            de CFDI con ID <%=comprobanteFiscalDto.getIdComprobanteFiscal()%>
                                        <%}%>
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <input type="hidden" id="idCobranzaAbono" name="idCobranzaAbono" value="<%=cobranzaAbonoDto!=null?cobranzaAbonoDto.getIdCobranzaAbono():"" %>" />
                                    <input type="hidden" id="idPedido" name="idPedido" value="<%=cobranzaAbonoDto!=null?cobranzaAbonoDto.getIdPedido():idPedido %>" />
                                    <input type="hidden" id="idComprobanteFiscal" name="idComprobanteFiscal" value="<%=cobranzaAbonoDto!=null?cobranzaAbonoDto.getIdComprobanteFiscal():idComprobanteFiscal %>" />
                                    <% if (cobranzaAbonoDto!=null){ %>
                                    <p>
                                        <label>*Fecha/Hora:</label><br/>
                                        <input maxlength="100" type="text" id="fecha" name="fecha" style="width:300px"
                                               readonly value="<%=cobranzaAbonoDto!=null?DateManage.dateTimeToStringEspanol(cobranzaAbonoDto.getFechaAbono()):DateManage.dateTimeToStringEspanol(new Date()) %>"/>
                                    </p>
                                    <br/>
                                    <% } %>
                                    <p>
                                        <label>*Monto ($):</label><br/>
                                        <input maxlength="14" type="text" id="monto" name="monto" style="width:300px"
                                               value="<%=cobranzaAbonoDto!=null?cobranzaAbonoDto.getMontoAbono():"0" %>"
                                               onkeypress="return validateNumber(event);"
                                               onkeyup="return verificaMontoAbono();"
                                               <%=cobranzaAbonoDto!=null?"readonly":""%> />
                                    </p>
                                    <br/>  
                                    <p>
                                        <label>*Método de Pago:</label><br/>
                                        <select size="1" id="id_metodo_pago" name="id_metodo_pago" onchange="metodoPago(<%=idPedido%>)" <%=cobranzaAbonoDto!=null?"disabled":""%>>
                                            <option value="-1">--Seleccione Método--</option>
                                            <%if(cobranzaAbonoDto!=null){%>
                                                <%=new SGCobranzaMetodoPagoBO(user.getConn()).getMetodoPagoHTMLCombo(cobranzaAbonoDto!=null?cobranzaAbonoDto.getIdCobranzaMetodoPago():-1)%>
                                             <%}else{%>
                                               <%= new SGCobranzaMetodoPagoBO(user.getConn()).getMetodoPagoHTMLCombo(-1,-1, " AND ID_COBRANZA_METODO_PAGO IN (1,2,3,4,5,6) ")%>
                                             <%}%>
                                        </select>
                                        <% if (cobranzaAbonoDto!=null) {
                                                if (cobranzaAbonoDto.getIdCobranzaMetodoPago()==VentaMetodoPagoBO.METODO_PAGO_TDC 
                                                        && cobranzaAbonoDto.getIdOperacionBancaria()>0){
                                        %>
                                            <input type="button" id="btn_datos_tdc" name="btn_datos_tdc" class="switch" value="Ver Datos de Cobro" 
                                                   style="float: right; " onclick="verDetalleOperacionBancaria(<%=cobranzaAbonoDto.getIdOperacionBancaria() %>)"
                                                   <%=cobranzaAbonoDto!=null?"readonly":""%> />
                                        <%
                                                }
                                            }
                                        %>
                                    </p>
                                    <br/>
                                    <div id="respuestaBonificacion"></div>
                                    <p>
                                        <label>Identificador operación (No. transacción/No. Documento/No. Referencia):</label><br/>
                                        <input maxlength="100" type="text" id="identificador_operacion" name="identificador_operacion" style="width:300px"
                                               value="<%=cobranzaAbonoDto!=null?cobranzaAbonoDto.getIdentificadorOperacion():"" %>"
                                               <%=cobranzaAbonoDto!=null?"readonly":""%>/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Comentarios adicionales:</label><br/>
                                        <textarea id="comentarios" name="comentarios"
                                                  rows="3" cols="40"
                                                  onKeyUp="return textArea_maxLen(this,240);" <%=cobranzaAbonoDto!=null?"readonly":""%>><%=cobranzaAbonoDto!=null?cobranzaAbonoDto.getComentarios():"" %>
                                        </textarea>
                                    </p>
                                    <br/>
                                    
                                    <p>
                                        <label>Fecha Abono de Cobranza: </label>
                                        <input type="text" name="cobranza_fecha_generacion" id="cobranza_fecha_generacion" readonly
                                               value="<%= cobranzaAbonoDto!=null?DateManage.formatDateToNormal(cobranzaAbonoDto.getFechaAbono()):DateManage.formatDateToNormal(new Date()) %>"
                                               style="width: 80px;"/>
                                    </p>
                                    
                                    <br/><br/>
                                    
                                    <div id="action_buttons">
                                        <p>
                                            <% if (idCobranzaAbono<=0 && mode.equals("1")){ %>
                                            <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
                                            <% } %>
                                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                        </p>
                                    </div>
                            </div>
                        </div>
                        <!-- End left column window -->
                        
                        <div class="column_right">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_ventas1.png" alt="icon"/>
                                    Saldos
                                    <% if (pedidoDto!=null){%>
                                        de Pedido con
                                        <a href="../pedido/pedido_list.jsp?idPedido=<%=pedidoDto.getIdPedido()%>">
                                            Folio <%=pedidoDto.getFolioPedido()%>
                                        </a>
                                    <%}else if (comprobanteFiscalDto!=null){%>
                                        de CFDI con
                                        <a href="../cfdi_factura/cfdi_factura_list.jsp?idComprobanteFiscal=<%=comprobanteFiscalDto.getIdComprobanteFiscal()%>">
                                            ID <%=comprobanteFiscalDto.getIdComprobanteFiscal()%>
                                        </a>
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <p>
                                        <label>Total ($):</label><br/>
                                        <% 
                                        SGCobranzaAbonoBO cobranzaAbonoBO1 = new SGCobranzaAbonoBO(user.getConn());
                                        if (pedidoDto!=null){
                                        %>
                                        
                                        <input maxlength="14" type="text" id="total" name="total" style="width:200px"
                                               style="color: #0000cc;"
                                               value="<%=pedidoDto.getBonificacionDevolucion()>0?pedidoDto.getTotal():pedidoDto.getTotal()+ Math.abs(pedidoDto.getBonificacionDevolucion())%>"
                                               readonly />
                                        <%}else if (comprobanteFiscalDto!=null){%>
                                        <input maxlength="14" type="text" id="total" name="total" style="width:200px"
                                               style="color: #0000cc;"
                                               value="<%=comprobanteFiscalDto.getImporteNeto() %>"
                                               readonly  />
                                        <%}%>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Saldo Pagado ($):</label><br/>
                                        <% 
                                        if (pedidoDto!=null){
                                        %>
                                        
                                        <input maxlength="14" type="text" id="saldo_pagado" name="saldo_pagado" style="width:200px"
                                               value="<%=cobranzaAbonoBO1.getSaldoPagadoPedido(pedidoDto.getIdPedido()) %>"
                                               readonly />
                                        <%}else if (comprobanteFiscalDto!=null){%>
                                        <input maxlength="14" type="text" id="saldo_pagado" name="saldo_pagado" style="width:200px"
                                               value="<%=cobranzaAbonoBO1.getSaldoPagadoComprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal()) %>"
                                                readonly />
                                        <%}%>
                                    </p>
                                    <br/>  
                                    <p>
                                        <label>Saldo Adeudo ($):</label><br/>
                                        <% 
                                        if (pedidoDto!=null){
                                        %>
                                        
                                        <input maxlength="14" type="text" id="saldo_adeudo" name="saldo_adeudo" style="width:200px"
                                               style="color: #ff0000;"                                               
                                               value="<%=pedidoDto.getBonificacionDevolucion()>0?cobranzaAbonoBO1.getSaldoAdeudoPedido(pedidoDto.getIdPedido()):cobranzaAbonoBO1.getSaldoAdeudoPedido(pedidoDto.getIdPedido()).doubleValue() + Math.abs(pedidoDto.getBonificacionDevolucion())%>"                                               
                                               readonly/>
                                        <%}else if (comprobanteFiscalDto!=null){%>
                                        <input maxlength="14" type="text" id="saldo_adeudo" name="saldo_adeudo" style="width:200px"
                                               style="color: #ff0000;"
                                               value="<%=cobranzaAbonoBO1.getSaldoAdeudoComprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal()) %>"
                                               readonly />
                                        <%}%>
                                    </p>
                                    <br/>
                                    <%if (cobranzaAbonoDto==null){%>
                                    <br/>
                                    <br/>
                                    <p>
                                        <label>Nuevo Saldo Calculado ($):</label><br/>                                        
                                        <% 
                                        if (pedidoDto!=null){
                                        %>
                                        <input maxlength="14" type="text" id="nuevo_saldo_adeudo" name="nuevo_saldo_adeudo" style="width:200px"
                                               value="<%=pedidoDto.getBonificacionDevolucion()>0?cobranzaAbonoBO1.getSaldoAdeudoPedido(pedidoDto.getIdPedido()):cobranzaAbonoBO1.getSaldoAdeudoPedido(pedidoDto.getIdPedido()).doubleValue() + Math.abs(pedidoDto.getBonificacionDevolucion())%>"
                                               <%=cobranzaAbonoDto!=null?"readonly":"readonly"%> />
                                        <%}else if (comprobanteFiscalDto!=null){%>
                                        <input maxlength="14" type="text" id="nuevo_saldo_adeudo" name="nuevo_saldo_adeudo" style="width:200px"
                                               value="<%=cobranzaAbonoBO1.getSaldoAdeudoComprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal()) %>"
                                               readonly/>
                                        <%}%>
                                    </p>
                                    <br/>  
                                    <%}%>
                            </div>
                            
                        </div>
                        <br><br><br><br><br><br><br><br><br><br><br>
                        <%
                        if (cobranzaAbonoDto!=null){
                            if((cobranzaAbonoDto.getLatitud() != 0)&& (cobranzaAbonoDto.getLongitud() != 0) )
                            {          
                            %>    
                            <div class="column_right">                            
                                <div class="header">
                                    <span>
                                        <img src="../../images/icon_mapa_1.png" alt="icon"/>
                                        Ubicación
                                    </span>
                                </div>
                                <div class="content">
                                    <div id="map_canvas" style="height: 230px; width: 100%">
                                        <img src="../../images/maps/ajax-loader.gif" alt="Cargando" style="margin: auto;"/>
                                    </div>     
                                </div>
                            </div>  
                            <% 
                            }   
                        }
                        %>
                    </div>
                    </form>
                    <!--TODO EL CONTENIDO VA AQUÍ-->

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>

        <script>
            mostrarCalendario();           
        </script>
    </body>
</html>
<%}%>