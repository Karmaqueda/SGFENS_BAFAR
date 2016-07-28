<%-- 
    Document   : catSmsCompraCreditos_list
    Created on : 10/03/2016, 12:55:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.util.Converter"%>
<%@page import="com.tsp.sct.bo.SmsPaquetePrecioBO"%>
<%@page import="com.tsp.sct.dao.dto.SmsPaquetePrecio"%>
<%@page import="com.tsp.sct.bo.SmsAgendaGrupoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensClienteVendedor"%>
<%@page import="com.tsp.sct.bo.SGClienteVendedorBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.bo.SmsCompraBitacoraBO"%>
<%@page import="com.tsp.sct.dao.jdbc.SmsCompraBitacoraDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SmsCompraBitacora"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="smsCompraSesion" scope="session" class="com.tsp.sgfens.sesion.SmsCompraCreditosSesion"/>
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
    /*
    int idSmsCompraBitacora = 0;
    try {
        idSmsCompraBitacora = Integer.parseInt(request.getParameter("idSmsCompraBitacora"));
    } catch (NumberFormatException e) {
    }
    */

    /*
     *   0/"" = nuevo
     *   1 = editar/consultar
     *   2 = eliminar  
     */
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
    
    
    SmsPaquetePrecio[] smsPaquetePrecios = new SmsPaquetePrecioBO(user.getConn()).findSmsPaquetePrecios(-1, 0, 0, " AND ID_ESTATUS=1 ");
    
    Configuration conf = new Configuration();
    
    String emailEmpleado = StringManage.getValidString(user.getLdap().getEmail());
    String telefonoEmpleado = StringManage.getValidString(user.getDatosUsuario().getTelefono());

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
        
        <!-- Inicio de script de Banwire-->
        <script type="text/javascript" src="https://sw.banwire.com/checkout.js"></script>
        
        <script type="text/javascript">
            
            var totalCobro = 0;
            var respuestaExitoBanwire;
            
            function grabar(){
                if (typeof respuestaExitoBanwire !== "undefined" && respuestaExitoBanwire !== null) {
                    $.ajax({
                        type: "POST",
                        url: "catSmsCompraCreditos_ajax.jsp",
                        data: { mode:'3',
                                bw_auth_code : respuestaExitoBanwire.auth_code, bw_reference : respuestaExitoBanwire.reference, 
                                bw_id : respuestaExitoBanwire.id, bw_total : respuestaExitoBanwire.total},
                        //data: $("#frm_action").serialize(),
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
                                            //aqui llamar a funcion para compra en BanWire
                                            location.href = "catSmsCompraCreditos_list.jsp?pagina=<%=paginaActual%>";
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
                if( jQuery.trim($("#cantidad_compra").val())==='' || jQuery.trim($("#cantidad_compra").val())==='0'){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "Cantidad" es requerido.</center>',{'animate':true});
                    $("#cantidad_compra").focus();
                    return false;
                }
                return true;
            } 
            
            function calculaPorCantidadRango(cantidadCreditos, accionComprar){
                if(validar()){
                    $.ajax({
                        type: "POST",
                        url: "catSmsCompraCreditos_ajax.jsp",
                        data: { mode:'2', cantidad_creditos : cantidadCreditos },
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut();//"slow");
                            $("#ajax_message").fadeOut("slow");
                            //$("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            //$("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                                strJSON = $.trim(datos.replace("<!--EXITO-->",""));
                            
                                var objeto = JSON.parse(strJSON);

                                $("#subtotal").html(objeto[0]);
                                $("#iva").html(objeto[1]);
                                $("#total").html(objeto[2]);
                                totalCobro = objeto[3];
                                
                                $("#action_buttons").fadeIn();//"slow");
                                
                                //Si la accion provino del boton de accion "adquirir creditos"
                                if (accionComprar==1){
                                    pagar();
                                }
                                
                           }else{
                                $("#subtotal").html('');
                                $("#iva").html('');
                                $("#total").html('');
                                
                               //$("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               $("#action_buttons").fadeIn();//"slow");
                               $.scrollTo('#inner',800);
                           }
                        }
                    });
                }
            }
            
            
            
            
            
            var SW = new BwGateway({
                // Quitar o establecer a false cuando pase a produccion
                sandbox: <%=conf.getBanwireSandbox()%>,
                // Nombre de usuario de Banwire
                user: '<%=conf.getBanwireUsuario()%>',
                // Titulo de la ventana
                title: "Compra de Cr&eacute;ditos SMS",
                // Referencia
                reference: '',
                // Concepto
                concept: '',
                // Opcional: Moneda
                currency: 'MXN',
                // Total de la compra
                total: "0",                               
                cust: {
                    fname: "", //Nombre del comprador
                    mname: "", //Apellido paterno del comprador
                    lname: "", //Apeliido materno del comprador
                    email: "", //Email del comprador
                    phone: "", //Número telefónico del comprador
                    addr: "", //Dirección del comprador (calle y número)
                    city: "", //Ciudad del comprador
                    state: "", //Estado del comprador (2 dígitos de acuerdo al formato ISO)
                    country: "", //País del comprador (3 dígitos de acuerdo al formato ISO)
                    zip: "" //Código de postal del comprador
                },
                // Opciones de pago, por defecto es "all". Puede incluir varias opciones separadas por comas
                paymentOptions: 'visa,mastercard,amex',//'all', // visa,mastercard,amex,oxxo,speifast,all
                // Mostrar o no pagina de resumen de compra
                reviewOrder: true,
                // Mostrar o no mostrar los campos de envio
                showShipping: false,
                // URL donde se van a enviar todas las notificaciones por HTTP POST de manera asoncrónica
                notifyUrl: "<%=conf.getBanwireUrlNotificaciones()%>",
                // Handler en caso de exito en el pago
                onSuccess: function(data){
                    //alert("¡Gracias por tu pago!");
                    var infoBanwire = JSON.stringify(data);     
                    var infoBanwireJson = JSON.parse(infoBanwire);
                    //console.log(data);
                    
                    apprise('<center><img src=../../images/info.png> <br/>'+ 'Transacción procesada exitosamente. <br/> Con Autorización: '+ infoBanwireJson.auth_code +'</center>',{'animate':true},
                                function(r){
                                    respuestaExitoBanwire = infoBanwireJson;
                                    grabar();
                                });
                },
                // Pago pendiente OXXO
                onPending: function(data){
                    alert("No se permiten pagos pendientes (Oxxo, transferencias).");
                },
                // Pago challenge
                onChallenge: function(data){
                    //alert("Pago enviado a validaciones de seguridad");
                    apprise('<center><img src=../../images/info.png> <br/>'+ 'Pago enviado a validaciones de seguridad.' +'</center>',{'animate':true},
                    function(r){
                            window.open('<%=conf.getBanwireUrlChallengePage()%>','_blank');
                        });
                },
                // Handler en caso de error en el pago
                onError: function(data){
                    //alert("Error en el pago");
                    apprise('<center><img src=../../images/info.png> <br/>'+ 'Error en el pago.' +'</center>',{'animate':true},
                    function(r){
                            window.open('<%=conf.getBanwireUrlErrorPage()%>','_blank');
                        });
                },
                // Cuando cierra el popup sin completar el proceso
                onCancel: function(data){
                    console.log("El usuario cancelo el proceso");
                }
            });

            function pagar() {
                if(checkDecimals()){
                    // Modificar los valores antes de efectuar el pago
                    SW.pay({
                        total: totalCobro,
                        concept: 'Compra Creditos SMS',
                        reference: (generaReference()),
                        items: [
                            {
                                name: $("#cantidad_compra").val() + ' Creditos SMS',
                                qty: 1,
                                desc: $("#cantidad_compra").val() + ' Creditos SMS',
                                unitPrice: totalCobro
                            }
                            ],
                        cust: {
                            <% if (emailEmpleado.length()>0){ %>
                            email: "<%= emailEmpleado %>", //Email del comprador
                            <% } %>
                            <% if (telefonoEmpleado.length()>0){ %>    
                            phone: "<%= telefonoEmpleado %>", //Número telefónico del comprador                           
                            <% } %>
                        }

                    });
                }
                
            }
            
            var referenciaPago = "";
            function generaReference(){
                referenciaPago = "<%= DateManage.getDateHourString() %>";
                return referenciaPago;
            }
            
            
            function checkDecimals() {   
                var fieldValue = "" + totalCobro;
                decallowed = 2;
                if (isNaN(fieldValue) || fieldValue == "") {                    
                    apprise('<center><img src=../../images/info.png> <br/>'+ 'El valor del Monto no es un número valido' +'</center>',{'animate':true});
                    return false;
                } else {
                    if (fieldValue.indexOf('.') == -1) fieldValue += ".";
                        dectext = fieldValue.substring(fieldValue.indexOf('.')+1, fieldValue.length);
                    if (dectext.length > decallowed)
                    {                        
                        apprise('<center><img src=../../images/info.png> <br/>'+ 'Introduzca un Monto con un máximo de ' + decallowed + ' decimales.' +'</center>',{'animate':true});
                        return false;
                    }else{
                        return true;
                    }                    
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
                    <h1>SMS Masivos</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="onecolumn">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_smsCompra.png" alt="icon"/>
                                    Adquirir Créditos SMS
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    
                                    <p>
                                    El costo del bloque de mensajes está tabulado como se muestra a continuación:
                                    <br/>
                                    <b>* Los precios no incluyen impuestos</b>
                                    </p>
                                    <br/>
                                    <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                        <thead>
                                            <tr>
                                                <th></th>
                                                <th>Número de Mensajes</th>
                                                <th>Costo por Mensaje MX*</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% for (SmsPaquetePrecio paquetePrecio : smsPaquetePrecios) { 
                                                String cantidadMensajes = StringManage.getValidString(paquetePrecio.getDescripcionPaquete());
                                                /*
                                                if (paquetePrecio.getRangoMin()>0){
                                                    cantidadMensajes += "" + paquetePrecio.getRangoMin() + " - ";
                                                }
                                                if (paquetePrecio.getRangoMax()>0){
                                                    cantidadMensajes += "" + paquetePrecio.getRangoMax();
                                                }else if (paquetePrecio.getRangoMin()>0 && paquetePrecio.getRangoMax()<=0){
                                                    cantidadMensajes += " en adelante";
                                                }

                                                if (cantidadMensajes.equals("")){
                                                    if (paquetePrecio.getPaqueteCantidad()>0){
                                                        cantidadMensajes = "" + paquetePrecio.getPaqueteCantidad();
                                                    }
                                                }
                                                */
                                            %>
                                            <tr>
                                                <td><%= paquetePrecio.getNombrePaquete() %></td>
                                                <td><%= cantidadMensajes %></td>
                                                <td><%= Converter.doubleToStringFormatMexico(paquetePrecio.getRangoPrecioUnitario()) %></td>
                                            </tr>
                                            <% } %>
                                        </tbody>
                                    </table>
                                             
                                    <br/>
                                    <p>
                                    Para comprar créditos ingrese la cantidad de mensajes que desea adquirir en el campo de mensajes y presione 'Adquirir'.
                                    <br/>
                                    El bloque mínimo de compra es de 100 mensajes.
                                    </p>
                                    <br/>
                                    
                                    <table cellspacing="10px">
                                        <tr>
                                            <td>Cantidad de mensajes:</td>
                                            <td>
                                                <input maxlength="6" type="text" id="cantidad_compra" name="cantidad_compra" style="width:80px"
                                                    onKeyPress="return validateNumberInteger(event);"
                                                    onChange="calculaPorCantidadRango(this.value);"
                                                    onKeyUp="calculaPorCantidadRango(this.value);"
                                                    value="100"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>Subtotal:</td>
                                            <td>
                                                <span id="subtotal"></span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>IVA (16%):</td>
                                            <td>
                                                <span id="iva"></span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>Total:</td>
                                            <td>
                                                <span id="total"></span>
                                            </td>
                                        </tr>
                                    </table>
                                    
                                    <br/>
                                    
                                    <div id="action_buttons" style="width: 300px; margin:0 auto;">
                                        <p>
                                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                            &nbsp;&nbsp;&nbsp;&nbsp;
                                            <input type="button" id="enviar" value="Adquirir Créditos" onclick="calculaPorCantidadRango($('#cantidad_compra').val(), 1);"/>
                                        </p>
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

        <script>
            calculaPorCantidadRango($("#cantidad_compra").val());
        </script>
    </body>
</html>
<%}%>