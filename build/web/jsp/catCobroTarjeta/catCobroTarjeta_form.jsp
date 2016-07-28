<%-- 
    Document   : catCobroTarjeta_form
    Created on : 2/04/2013, 05:38:24 PM
    Author     : Leonardo
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
            
           function grabar(){
                if(validar()){
                    $.ajax({
                        type: "POST",
                        url: "catCobroTarjeta_ajax.jsp",
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
                                            location.href = "catCobroTarjeta_list.jsp";
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
                /*
                if(jQuery.trim($("#nombre").val())==""){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "nombre de contacto" es requerido</center>',{'animate':true});
                    $("#nombre_contacto").focus();
                    return false;
                }
                */
                return true;
            }
            
        </script>
        <!-- Librerias para realizar la conexion a conekta-->
        <script type="text/javascript" src="https://conektaapi.s3.amazonaws.com/v0.3.2/js/conekta.js"></script>        
        <!-- Llave pública-->
        <script type="text/javascript">
            // Conekta Public Key
            // Conekta.setPublishableKey() permite a Conekta identificar tu cuenta cuando te comunicas con nuestros servidores. 
            // Recuerda usar tu llave de API pública en modo de producción cuando tu cuenta esté activa para que puedas crear tokens reales.
             //Conekta.setPublishableKey('key_KJysdbf6PotS2ut2');
             Conekta.setPublishableKey('<%=conf.getConektakeyPublica()%>');
        </script>
        <script type="text/javascript">
            /////Tokeniza la tarjeta
            jQuery(function($) {
                $("#frm_action").submit(function(event) {
                    
                    $("#action_buttons").fadeOut("slow");
                    $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                    $("#ajax_loading").fadeIn("slow");
                    
                  var $form;
                  $form = $(this);

              /* Previene hacer submit más de una vez */
                  //$form.find("button").prop("disabled", true);
                  Conekta.token.create($form, conektaSuccessResponseHandler, conektaErrorResponseHandler);

              /* Previene que la información de la forma sea enviada al servidor */
                  return false;
                });
              });
            /////
              
            //+++Envía el token a tu servidor              
            var conektaSuccessResponseHandler;
            conektaSuccessResponseHandler = function(token) {                             
              var $form;
              $form = $("#frm_action");
            /* Inserta el token_id en la forma para que se envíe al servidor */

              $form.append($("<input type=\"hidden\" name=\"conektaTokenId\" id=\"conektaTokenId\" />").val(token.id));
              //alert("Token id: "+token.id);

            /* and submit */
              //$form.get(0).submit();
              //alert("procesando el pago . . .");
              grabar();
            };
            //+++

            //---Para los errores encontrados enla captura y/o en la tarjeta
            var conektaErrorResponseHandler;
            conektaErrorResponseHandler = function(response) {
                
                //alert("error: "+response.message);
                /*$("#ajax_loading").fadeOut("slow");
                $("#ajax_message").html(response.message);
                $("#ajax_message").fadeIn("slow");
                $("#action_buttons").fadeIn("slow");
                */
                apprise('<center><img src=../../images/info.png> <br/>'+ response.message +'</center>',{'animate':true},
                    function(r){
                        //location.href = "catCobroTarjeta_list.jsp";
                    });
                
                $("#ajax_loading").fadeOut("slow");               
                $("#action_buttons").fadeIn("slow");
                
              //var $form;
              //$form = $("#frm_action");


            /* Muestra los errores en la forma */
              //$form.find(".card-errors").text(response.message);
              //$form.find("button").prop("disabled", false);
            };
            //---
            
            function ejecutaProceso(){
                if(checkDecimals()){
                    document.getElementById("submitLe").click();                
                }
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
    </head>
    <body>
        <div class="content_wrapper">

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
                                        <input maxlength="16" type="text" id="numTarjetaOperacion" name="numTarjetaOperacion" style="width:300px" data-conekta="card[number]"
                                               value="<%=bancoOperacionsDto!=null?bancoOperacionBO.getBancoOperacion().getNoTarjeta():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Nombre del Titular:</label><br/>
                                        <input maxlength="100" type="text" id="nombreTitularOperacion" name="nombreTitularOperacion" style="width:300px" data-conekta="card[name]"
                                               value="<%=bancoOperacionsDto!=null?bancoOperacionBO.getBancoOperacion().getNombreTitular():"" %>"/>
                                    </p>
                                    <br/>   
                                    <!--<p>
                                        <label>*Fecha de Vencimiento:</label><br/>
                                        <input maxlength="5" type="text" id="fechaVencimientoOperacion" name="fechaVencimientoOperacion" style="width:300px"
                                               value=""/> MM/AA
                                    </p>
                                    <br/> -->                                    
                                    <p>
                                        <label>*Fecha de Vencimiento:</label><br/>
                                        <select id="idMesOperacion" name="idMesOperacion" data-conekta="card[exp_month]">
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
                                        <select id="idAnoOperacion" name="idAnoOperacion" data-conekta="card[exp_year]">
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
                                    
                                    <p>
                                        <label>*CVV:</label><br/>
                                        <input maxlength="3" type="text" id="cvvOperacion" name="cvvOperacion" style="width:300px" data-conekta="card[cvc]"
                                               value=""/> 
                                    </p>
                                    <br/> 
                                    <p>
                                        <label>*Monto a pagar:</label><br/>
                                        <input maxlength="10" type="text" id="montoPagadoOperacion" name="montoPagadoOperacion" style="width:300px"
                                               value="<%=bancoOperacionsDto!=null?bancoOperacionBO.getBancoOperacion().getMonto():"" %>"/>
                                    </p>  
                                    
                                    <% if(bancoOperacionsDto!=null){%>
                                    <br/> 
                                    <p>
                                        <label>*Order ID:</label><br/>
                                        <input maxlength="20" type="text" id="orderIdOperacion" name="orderIdOperacion" style="width:300px"
                                               value="<%=bancoOperacionsDto!=null?bancoOperacionBO.getBancoOperacion().getBancoOrderId():"" %>" readonly/>
                                    </p>  
                                    <%}%> 
                                    
                                    <% if(bancoOperacionsDto==null){%>
                                    <!--<br/>
                                    <label>Para un mejor procesamiento en el cobro se recomienda</label><br/>
                                    <label>llenar los campos siguientes:</label><br/><br/>
                                    <p> 
                                        <label>Correo Electrónico del Titular de la Tarjeta:</label><br/>
                                        <input maxlength="80" type="text" id="correoTarjetaOperacion" name="correoTarjetaOperacion" style="width:300px"
                                               value=""/>
                                    </p>
                                    <br/>
                                    <p> 
                                        <label>Teléfono del Titular de la Tarjeta:</label><br/>
                                        <input maxlength="20" type="text" id="telefonoTarjetaOperacion" name="telefonoTarjetaOperacion" style="width:300px"
                                               value=""/>
                                    </p>-->
                                    <%}%>
                                    
                                    <br/><br/>
                                    
                                    <div id="action_buttons">
                                        <p>
                                            <% if(bancoOperacionsDto==null){%>
                                            <input type="button" id="enviar" value="Pagar" onclick="ejecutaProceso();"/>
                                            <%}else{%>
                                            <input type="button" id="enviar" value="Cancelar" onclick="grabar();"/>
                                            <%}%>
                                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                        </p>
                                    </div>
                                            <button type="submit" style="display: none" id="submitLe" name="submitLe" >Realizar Cobro</button>
                                    
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