<%-- 
    Document   : pedido_editaServicio_form.jsp
    Created on : 13/12/2012, 02:21:14 PM
    Author     : ISC Cesar Martinez poseidon24@hotmail.com
--%>

<%@page import="com.tsp.sct.bo.UnidadBO"%>
<%@page import="com.tsp.sct.dao.dto.Servicio"%>
<%@page import="com.tsp.sct.bo.ServicioBO"%>
<%@page import="com.tsp.sgfens.sesion.ServicioSesion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="pedidoSesion" scope="session" class="com.tsp.sgfens.sesion.PedidoSesion"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%

    String msgError ="";
    int index_lista_servicio=-1;
    try{ index_lista_servicio = Integer.parseInt(request.getParameter("index_lista_servicio")); }catch(Exception e){}
    
    ServicioSesion servicioSesion = null;
    
    if (pedidoSesion!=null){
        if (pedidoSesion.getListaServicio()!=null){
            if (pedidoSesion.getListaServicio().size()>0){
                try{
                    servicioSesion = pedidoSesion.getListaServicio().get(index_lista_servicio);
                }catch(Exception e){
                    msgError += "<ul>El servicio que selecciono no existe en sesion. Intente recargar la página.";
                }
            }else{
                msgError += "<ul>La lista de Servicios esta vacía. Intente recargar la página";
            }
        }else{
            msgError += "<ul>La lista de Servicios esta vacía. Intente recargar la página";
        }
    }else{
        msgError += "<ul>No existe ninguna pedido en sesion";
    }

    Servicio servicioDto = null;
    ServicioBO servicioBO = null;
    try{
        servicioBO = new ServicioBO(servicioSesion.getIdServicio(),user.getConn());
        servicioDto = servicioBO.getServicio();
    }catch(Exception e){
        msgError += "<ul>No se pudo recuperar información vital del Servicio.";
    }
    
    if (servicioDto==null)
        msgError += "<ul>El servicio elegido ya no existe en la base de datos.";
    
%>
<jsp:include page="../../jsp/include/registraNavegacionBitacora.jsp"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Editar Campo de Pedido</title>
        
        <jsp:include page="../include/skinCSS.jsp" />
        <jsp:include page="../../jsp/include/jsFunctions.jsp"/>
        <script language="javascript">
            function modificarServicio(){
                indexservicio = $('#servicio_index').val();
                servicioId = $('#id_servicio_compra').val();
                servicioCantidad = $('#servicio_cantidad').val();
                servicioMonto = $('#servicio_monto').val();
                
                //unidadId = $('#unidad_select').val();
                //unidadText = $('#unidad_select option:selected').text();
                servicioPrecio = $('#servicio_precio').val();
                
                if (validarAddServicio(indexservicio,servicioId,servicioCantidad,servicioMonto)){
                    if(servicioId>0){
                        $.ajax({
                            type: "POST",
                            url: "pedido_ajax.jsp",
                            data: { mode: '18', index_lista_servicio: indexservicio, servicio_id : servicioId, 
                                servicio_cantidad : servicioCantidad, servicio_monto : servicioMonto,
                                servicio_precio : servicioPrecio},
                                //$("#frm_action").serialize(),
                            beforeSend: function(objeto){
                                $("#action_buttons").fadeOut("slow");
                                $("#ajax_loading").html('<div style=""><img src="../../images/ajax_loader.gif" alt="Cargando.." />Procesando... </div>');
                                $("#ajax_loading").fadeIn("slow");
                            },
                            success: function(datos){
                                if(datos.indexOf("--EXITO-->", 0)>0){
                                   $("#ajax_message").html(datos);
                                   $("#ajax_loading").fadeOut("slow");
                                   $("#action_buttons").fadeIn("slow");
                                   $("#ajax_message").fadeIn("slow");
                                   /*apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                    function(r){*/
                                        //cerramos ventana modal fancybox
                                        parent.recuperarListados();
                                        parent.$.fancybox.close();
                                   //});
                               }else{
                                   $("#ajax_loading").fadeOut("slow");
                                   $("#ajax_message").html(datos);
                                   $("#ajax_message").fadeIn("slow");
                                   $("#action_buttons").fadeIn("slow");
                                   //apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                               }
                            }
                        });
                    }
                }
            }
            function validarAddServicio(indexServicio,idServicio,cantidad,monto){
                if(indexServicio<0 || idServicio<=0 || cantidad<0 || monto<0 
                    || cantidad=="" || monto==""){
                    apprise('<center><img src=../../images/warning.png> <br/>Llene todos los datos requeridos para modificar el servicio.</center>',{'animate':true});
                    return false;
                }
                return true;
            }
            
            function validateNumber(event) {
                var key = window.event ? event.keyCode : event.which;

                if (event.keyCode == 8 || event.keyCode == 46
                 || event.keyCode == 37 || event.keyCode == 39) {
                    return true;
                }
                else if ( key < 48 || key > 57 ) {
                    return false;
                }
                else return true;
            };
            
            function reCalcularMonto(){
                var cantidad = getFloatFromInput($('#servicio_cantidad'));
                var precio = getFloatFromInput($('#servicio_precio'));
                var monto = (precio * cantidad).toFixed(2);
                //alert('precio: ' + precio + ' cantidad: ' + cantidad + ' monto: ' + monto);
                $('#servicio_monto').val(monto);
            }
            
            function inicializarPopup(){
                $('#left_menu').hide();
                $('body').addClass('nobg');
		$('#content').css('marginLeft', 30);
            }
        </script>
    </head>
    <body scroll="no">
        <div id="left_menu">
        </div>
        <div id="content">
            <div class="inner">
            
                <% if (msgError.equals("")) {%>
                <div>
                    <form action="" method="post" id="frm_action">
                        <input type="hidden" id="id_servicio_compra" name="id_servicio_compra" value="<%=servicioSesion.getIdServicio()%>" />
                        <input type="hidden" id="servicio_index" name="servicio_index" value="<%=index_lista_servicio%>" />
                        <fieldset>
                            <p>
                                <label>*Nombre: </label><br/>
                                <input maxlength="5" readonly disabled type="text" name="servicio_nombre" id="servicio_nombre" size="54" value="<%=servicioBO!=null?servicioBO.getServicio().getNombre():"" %>"/>
                            </p>

                            <!--
                            <p>
                                <label>*Unidad: </label>
                                <select size="1" name="unidad_select" id="unidad_select"  style="width:150px;">
                                    <option value="-1"></option>
                                    <%out.print(new UnidadBO(user.getConn()).getUnidadesHTMLCombo(-1)); %>
                                </select>
                            </p>-->

                            <p>
                                <label>*Precio: </label><br/>                            
                                <select id="servicio_precio" name="servicio_precio" 
                                        onchange="reCalcularMonto();" style="width: 150px;">
                                    <option selected value="<%=servicioDto.getPrecio()%>">
                                        <%=servicioDto.getPrecio()%>
                                    </option>
                                </select>
                            </p>

                            <p>
                                <label>*Cantidad: </label> <br/>
                                <input type="text" maxlength="8" name="servicio_cantidad" id="servicio_cantidad" 
                                    style="text-align: right;" size="12"
                                    onkeypress="return validateNumber(event);"
                                    onchange="reCalcularMonto()"
                                    value="<%=servicioSesion.getCantidad() %>"/>
                            </p>

                            <p>
                                <label>Monto: </label><br/> 
                                    <input type="text" maxlength="12" name="servicio_monto" id="servicio_monto" 
                                    style="text-align: right;" size="12" onkeypress="return validateNumber(event);"
                                    readonly value="<%=servicioSesion.getMonto() %>"/>
                            </p>
                            <br class="clear"/>
                            <dl class="submit">
                                <div id="action_buttons">
                                    <input type="button" name="Grabar" id="submit" value="Guardar" onclick="modificarServicio();"/>
                                    <input type="button" name="Cancelar" id="submit" value="Cancelar" onclick="parent.$.fancybox.close();"/>
                                </div>
                                <div id="ajax_loading"></div>
                                <div id="ajax_message" class="info_box" style="display: none;"></div>
                            </dl>
                        </fieldset>
                    </form>
                </div>
                <%}else{%>
                <center>
                    <%=msgError%>
                </center>
                <%}%>
            </div>
        </div>
        
        <script>
            inicializarPopup();
        </script>
    </body>
</html>
