<%-- 
    Document   : catCobroTarjetaBanwire_form
    Created on : 19/02/2015, 03:42:00 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.dao.dto.SgfensClienteVendedor"%>
<%@page import="com.tsp.sct.bo.SGClienteVendedorBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.bo.PasswordBO"%>
<%@page import="com.tsp.sct.bo.BancoOperacionBO"%>
<%@page import="com.tsp.sct.dao.jdbc.BancoOperacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.BancoOperacion"%>
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
        int idBancoOperacion = 0;
        try {
            idBancoOperacion = Integer.parseInt(request.getParameter("idBancoOperacion"));
        } catch (NumberFormatException e) {
        }

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        String newRandomPass = "";
        
        BancoOperacionBO bancoOperacionBO = new BancoOperacionBO(user.getConn());
        BancoOperacion bancoOperacionsDto = null;
        if (idBancoOperacion > 0){
            bancoOperacionBO = new BancoOperacionBO(idBancoOperacion,user.getConn());
            bancoOperacionsDto = bancoOperacionBO.getBancoOperacion();
        }else{
            newRandomPass = new PasswordBO().generateValidPassword();
        }
        Configuration conf = new Configuration();
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
            
           function grabar(idBanwire, authCodeBanwire, referenceBanwire, totalBanwire, eventBanwire){ 
                var numTarjetaOperacion = document.getElementById("numTarjetaOperacion").value;
                var nombreTitularOperacion = document.getElementById("nombreTitularOperacion").value;
                var montoPagadoOperacion = document.getElementById("montoPagadoOperacion").value;
                $.ajax({
                    type: "POST",
                    url: "catCobroTarjetaBanwire_ajax.jsp",
                    data: { mode: '' , idBanwire : idBanwire, authCodeBanwire : authCodeBanwire, referenceBanwire : referenceBanwire, 
                        totalBanwire : totalBanwire, eventBanwire : eventBanwire, 
                        nombreTitularOperacion : nombreTitularOperacion, numTarjetaOperacion : numTarjetaOperacion, montoPagadoOperacion : montoPagadoOperacion
                           },
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
                                        location.href = "../catCobroTarjeta/catCobroTarjeta_list.jsp";
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
                       
        </script>        
        
        <script type="text/javascript">
            function checkDecimals()
            {   
                var fieldValue = document.getElementById("montoPagadoOperacion").value;
                decallowed = 2;
                if (isNaN(fieldValue) || fieldValue == "")
                {                    
                    apprise('<center><img src=../../images/info.png> <br/>'+ 'El valor del Monto no es un número valido' +'</center>',{'animate':true},
                    function(r){
                        //location.href = "catCobroTarjeta_list.jsp";
                    });
                    return false;
                    //fieldName.select();
                    //fieldName.focus();
                }
                else
                {
                    if (fieldValue.indexOf('.') == -1) fieldValue += ".";
                        dectext = fieldValue.substring(fieldValue.indexOf('.')+1, fieldValue.length);
                    if (dectext.length > decallowed)
                    {                        
                        apprise('<center><img src=../../images/info.png> <br/>'+ 'Introduzca un Monto con un máximo de ' + decallowed + ' decimales.' +'</center>',{'animate':true},
                        function(r){
                            //location.href = "catCobroTarjeta_list.jsp";
                        });
                        return false;
                        //fieldName.select();
                        //fieldName.focus();
                    }else{
                        return true;
                    }                    
                }
            }        
        </script>
        
        
        <!-- Inicio de script de Banwire-->
        <script type="text/javascript" src="https://sw.banwire.com/checkout.js"></script>
        
        <script type="text/javascript">
            
            var SW = new BwGateway({
                // Quitar o establecer a false cuando pase a produccion
                sandbox: <%=conf.getBanwireSandbox()%>,
                // Nombre de usuario de Banwire
                user: '<%=conf.getBanwireUsuario()%>',
                //user: "pruebasbw",
                
                // Titulo de la entana
                title: "Cobro con Tarjeta",
                // Referencia
                reference: '',
                // Concepto
                concept: '',
                // Opcional: Moneda
                currency: 'MXN',
                // Total de la compra
                total: "0",                
                // Opcional: Meses sin intereses
                // months: [3,6],
                // Arreglo con los items de compra
                /*items: [
                    {
                        name: "Concepto uno",
                        qty: 1,
                        desc: "Articulo de prueba numero uno",
                        unitPrice: 10
                    }
                ],*/                
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
                /*ship: {
                    addr: "Direccion 440", //Dirección de envío
                    city: "Mexico", //Ciudad de envío
                    state: "DF", //Estado de envío (2 dígitos de acuerdo al formato ISO)
                    country: "MEX", //País de envío (3 dígitos de acuerdo al formato ISO)
                    zip: "14145" //Código de postal del envío
                },*/
                // Opciones de pago, por defecto es "all". Puede incluir varias opciones separadas por comas
                paymentOptions: 'all', // visa,mastercard,amex,oxxo,speifast,all
                // Mostrar o no pagina de resumen de compra
                reviewOrder: true,
                // Mostrar o no mostrar los campos de envio
                showShipping: false,
                // Solamente para pagos recurrentes o suscripciones
                /*recurring: {
                    // Cada cuanto se ejecutará el pago "month","year" o un entero representando numero de días
                    interval: "month",
                    // Opcional: Limitar el número de pagos (si no se pone entonces no tendrá limite)
                    limit: 10, 
                    // Opcional: Fecha del primer cargo (en caso de no especificar se ejecutará de inmediato)
                    start: "2014-01-01", // Formaro YYYY-MM-DD
                    // Opcional: En caso de que los pagos subsecuentes (después del primero)
                    // tengan un monto distinto al inicial
                    total: "50.00"
                },*/
                // URL donde se van a enviar todas las notificaciones por HTTP POST de manera asoncrónica
                notifyUrl: "<%=conf.getBanwireUrlNotificaciones()%>",
                // Handler en caso de exito en el pago
                //successPage: '<%=conf.getBanwireUrlSuccessPage()%>',
                onSuccess: function(data){
                    var infoBanwire = JSON.stringify(data);                    
                    var infoBanwireJson = JSON.parse(infoBanwire);
                    
                    apprise('<center><img src=../../images/info.png> <br/>'+ 'Transacción procesada exitosamente. <br/> Con Autorización: '+ infoBanwireJson.auth_code +'</center>',{'animate':true},
                                        function(r){
                                            //location.href = "../catCobroTarjeta/catCobroTarjeta_list.jsp";
                                            grabar(infoBanwireJson.id, infoBanwireJson.auth_code, infoBanwireJson.reference, infoBanwireJson.total, infoBanwireJson.event);
                                        });
                    
                    //alert("¡Gracias por tu pago!");                    
                },
                // Pago pendiente OXXO
                //pendingPage: '<%=conf.getBanwireUrlPendingPage()%>',
                onPending: function(data){
                    $("#action_buttonsBanco").fadeOut("slow");                    
                    //colocamos la referencia al identificador:                    
                    grabar(referenciaPagoOxxo, 'Pendiente Pago Oxxo', referenciaPagoOxxo, $("#montoPagadoOperacion").val(), 'pendingPage');
                    
                    apprise('<center><img src=../../images/info.png> <br/>'+ 'El pago está pendiente por ser efectuado.' +'</center>',{'animate':true},
                    function(r){
                            //location.href = "";
                            window.open('<%=conf.getBanwireUrlPendingPage()%>','_blank');
                        });
//                    alert("El pago está pendiente por ser efectuado");
                },
                // Pago challenge
                challengePage: '<%=conf.getBanwireUrlChallengePage()%>',
                onChallenge: function(data){
                    apprise('<center><img src=../../images/info.png> <br/>'+ 'Pago enviado a validaciones de seguridad.' +'</center>',{'animate':true},
                    function(r){
                            //location.href = "";
                            window.open('<%=conf.getBanwireUrlChallengePage()%>','_blank');
                        });
//                    alert("Pago enviado a validaciones de seguridad");
                },
                // Handler en caso de error en el pago
                errorPage: '<%=conf.getBanwireUrlErrorPage()%>',
                onError: function(data){
                    apprise('<center><img src=../../images/info.png> <br/>'+ 'Error en el pago.' +'</center>',{'animate':true},
                    function(r){
                            location.href = "<%=conf.getBanwireUrlErrorPage()%>";
                        });
//                    alert("Error en el pago");
                },
                // Cuando cierra el popup sin completar el proceso
                onCancel: function(data){
                    console.log("Se cancelo el proceso");
                }
            });

            function pagar() {                
                if(checkDecimals() && checkNumTarjetaTitular()){
                    // Podemos pagar con los valores por defecto
                    //SW.pay();

                    /* O podemos modificar los valores antes de efectuar el pago*/
                    SW.pay({
                        total: $("#montoPagadoOperacion").val(),
                        concept: $("#conceptoOperacion").val(),
                        reference: (new Date().getTime()),
                        items: [
                            {
                                name: ($("#conceptoOperacion").val().trim()!=""?$("#conceptoOperacion").val():"Concepto uno"),
                                qty: 1,
                                desc: $("#conceptoOperacion").val(),
                                unitPrice: $("#montoPagadoOperacion").val()
                            }
                            ],
                        cust: {                            
                            email: $("#correoTarjetaOperacion").val(), //Email del comprador
                            phone: $("#telefonoTarjetaOperacion").val(), //Número telefónico del comprador                           
                        }

                    });
                }
                
            }
            
            var referenciaPagoOxxo = "";
            function generaReference(){
                referenciaPagoOxxo = (new Date().getTime());
                return referenciaPagoOxxo;
            }
            
            function checkNumTarjetaTitular(){
                var numTarjetaOperacion = document.getElementById("numTarjetaOperacion").value;
                var nombreTitularOperacion = document.getElementById("nombreTitularOperacion").value;
                                
                if(numTarjetaOperacion == ""){
                    apprise('<center><img src=../../images/info.png> <br/>'+ 'El número de la tarjeta es inválido' +'</center>',{'animate':true},
                        function(r){
                            //location.href = "catCobroTarjeta_list.jsp";
                        });
                    return false;
                }
                if(nombreTitularOperacion == ""){
                    apprise('<center><img src=../../images/info.png> <br/>'+ 'El Nombre del titular no puede ir vacio.' +'</center>',{'animate':true},
                        function(r){
                            //location.href = "catCobroTarjeta_list.jsp";
                        });
                    return false;
                }
                return true;
            }
            
        </script>
        <!-- Fin de script de Banwire-->
        
    </head>
    <body>
        <div class="content_wrapper">p
            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Cobro con Tarjeta</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>
                    
                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_tarjetas.png" alt="icon"/>
                                    <% if(bancoOperacionsDto!=null){%>
                                    Cancelar Banco Operacion ID <%=bancoOperacionsDto!=null?bancoOperacionsDto.getIdOperacionBancaria():"" %>
                                    <%}else{%>
                                    Banco Operacion
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idBancoOperacion" name="idBancoOperacion" value="<%=bancoOperacionsDto!=null?bancoOperacionsDto.getIdOperacionBancaria():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />                                    
                                    <p> 
                                        <label>*Número de Tarjeta:</label><br/>
                                        <input maxlength="16" type="text" id="numTarjetaOperacion" name="numTarjetaOperacion" style="width:300px"
                                               value="<%=bancoOperacionsDto!=null?bancoOperacionBO.getBancoOperacion().getNoTarjeta():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Nombre del Titular:</label><br/>
                                        <input maxlength="100" type="text" id="nombreTitularOperacion" name="nombreTitularOperacion" style="width:300px"
                                               value="<%=bancoOperacionsDto!=null?bancoOperacionBO.getBancoOperacion().getNombreTitular():"" %>"/>
                                    </p>
                                    <br/>  
                                                                        
                                    <!--<p>
                                        <label>*Fecha de Vencimiento:</label><br/>
                                        <select id="idMesOperacion" name="idMesOperacion">
                                            <option value="-1">MES</option>
                                            <option value="01">01</option>
                                            <option value="02">02</option>
                                            <option value="03">03</option>
                                            <option value="04">04</option>
                                            <option value="05">05</option>
                                            <option value="06">06</option>
                                            <option value="07">07</option>
                                            <option value="08">08</option>
                                            <option value="09">09</option>
                                            <option value="10">10</option>
                                            <option value="11">11</option>
                                            <option value="12">12</option>                                            
                                        </select>
                                        /
                                        <select id="idAnoOperacion" name="idAnoOperacion" >
                                            <option value="-1">AÑO</option>
                                            <option value="13">2013</option>
                                            <option value="14">2014</option>
                                            <option value="15">2015</option>
                                            <option value="16">2016</option>
                                            <option value="17">2017</option>
                                            <option value="18">2018</option>
                                            <option value="19">2019</option>
                                            <option value="20">2020</option>
                                            <option value="21">2021</option>
                                            <option value="22">2022</option>
                                            <option value="23">2023</option>
                                            <option value="24">2024</option>
                                        </select>
                                        
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*CVV:</label><br/>
                                        <input maxlength="3" type="text" id="cvvOperacion" name="cvvOperacion" style="width:300px"
                                               value=""/> 
                                    </p>
                                    <br/>
                                    -->
                                    <p>
                                        <label>*Monto a pagar:</label><br/>
                                        <input maxlength="10" type="text" id="montoPagadoOperacion" name="montoPagadoOperacion" style="width:300px"
                                               value="<%=bancoOperacionsDto!=null?bancoOperacionBO.getBancoOperacion().getMonto():"" %>"/>
                                    </p>
                                    
                                    <% if(bancoOperacionsDto==null){%>
                                    <br/>
                                    <p>
                                        <label>*Concepto de Pago:</label><br/>
                                        <input maxlength="100" type="text" id="conceptoOperacion" name="conceptoOperacion" style="width:300px"
                                               value=""/>
                                    </p>
                                    <br/>
                                    <p> 
                                        <label>*Correo Electrónico del Titular de la Tarjeta:</label><br/>
                                        <input maxlength="80" type="text" id="correoTarjetaOperacion" name="correoTarjetaOperacion" style="width:300px"
                                               value=""/>
                                    </p>
                                    <br/>                                   
                                    <p> 
                                        <label>*Teléfono del Titular de la Tarjeta:</label><br/>
                                        <input maxlength="20" type="text" id="telefonoTarjetaOperacion" name="telefonoTarjetaOperacion" style="width:300px"
                                               value=""/>
                                    </p>
                                     <br/>
                                    <%}%>
                                    
                                    <% if(bancoOperacionsDto!=null){%>
                                    <br/> 
                                    <p>
                                        <label>*Order ID:</label><br/>
                                        <input maxlength="20" type="text" id="orderIdOperacion" name="orderIdOperacion" style="width:300px"
                                               value="<%=bancoOperacionsDto!=null?bancoOperacionBO.getBancoOperacion().getBancoOrderId():"" %>" readonly/>
                                    </p>  
                                    <%}%> 
                                                                        
                                    <br/><br/>
                                    
                                    <div id="action_buttons">
                                        <p>
                                            <% if(bancoOperacionsDto==null){%>
                                            
                                            <input type="button" id="enviar" value="Pagar" onclick="pagar();"/>                                            
                                            <%}else{%>
                                            <!--<input type="button" id="enviar" value="Cancelar" onclick="grabar();"/>-->
                                            <%}%>
                                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                        </p>
                                    </div>                                            
                                    
                            </div>
                        </div>
                        <!-- End left column window -->
                        
                        
                    </div>
                    </form>
                    <!--TODO EL CONTENIDO VA AQUÍ-->

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>


    </body>
</html>
<%}%>