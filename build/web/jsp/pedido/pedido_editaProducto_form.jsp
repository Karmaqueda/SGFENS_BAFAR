<%-- 
    Document   : pedido_editaProducto_form.jsp
    Created on : 13/12/2012, 02:21:14 PM
    Author     : ISC Cesar Martinez poseidon24@hotmail.com
--%>

<%@page import="com.tsp.sct.bo.UnidadBO"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sgfens.sesion.ProductoSesion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="pedidoSesion" scope="session" class="com.tsp.sgfens.sesion.PedidoSesion"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%

    String msgError ="";
    int index_lista_producto=-1;
    try{ index_lista_producto = Integer.parseInt(request.getParameter("index_lista_producto")); }catch(Exception e){}
    
    String almacenNombre ="";
    almacenNombre = request.getParameter("nomAlm") != null ? request.getParameter("nomAlm") : "";
    
    ProductoSesion productoSesion = null;
    
    if (pedidoSesion!=null){
        if (pedidoSesion.getListaProducto()!=null){
            if (pedidoSesion.getListaProducto().size()>0){
                try{
                    productoSesion = pedidoSesion.getListaProducto().get(index_lista_producto);
                }catch(Exception e){
                    msgError += "<ul>El producto que selecciono no existe en sesion. Intente recargar la página.";
                }
            }else{
                msgError += "<ul>La lista de Productos esta vacía. Intente recargar la página";
            }
        }else{
            msgError += "<ul>La lista de Productos esta vacía. Intente recargar la página";
        }
    }else{
        msgError += "<ul>No existe ninguna pedido en sesion";
    }

    Concepto conceptoDto = null;
    ConceptoBO conceptoBO = null;
    try{
        conceptoBO = new ConceptoBO(productoSesion.getIdProducto(),user.getConn());
        conceptoDto = conceptoBO.getConcepto();
    }catch(Exception e){
        msgError += "<ul>No se pudo recuperar información vital del Producto.";
    }
    
    if (conceptoDto==null)
        msgError += "<ul>El producto elegido ya no existe en la base de datos.";
    
%>
<jsp:include page="../../jsp/include/registraNavegacionBitacora.jsp"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Editar Campo de Pedido</title>
        
        <jsp:include page="../include/skinCSS.jsp" />
        <jsp:include page="../../jsp/include/jsFunctions.jsp"/>
        <script language="javascript">
            function modificarProducto(){
                indexproducto = $('#producto_index').val();
                productoId = $('#id_producto_compra').val();
                productoCantidad = $('#producto_cantidad').val();
                productoMonto = $('#producto_monto').val();
                
                unidadId = $('#unidad_select').val();
                unidadText = $('#unidad_select option:selected').text();
                productoPrecio = $('#producto_precio').val();
                almacenId = $('#id_almacen_compra').val();
                
                if (validarAddProducto(indexproducto,productoId,productoCantidad,productoMonto, unidadId,almacenId,'<%=almacenNombre%>')){
                    if(productoId>0){
                        $.ajax({
                            type: "POST",
                            url: "pedido_ajax.jsp",
                            data: { mode: '8', index_lista_producto: indexproducto, producto_id : productoId, 
                                producto_cantidad : productoCantidad, producto_monto : productoMonto,
                                producto_precio : productoPrecio, producto_unidad : unidadText, almacen_id: almacenId, almacenNombre : '<%=almacenNombre%>'},
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
                                   //$("#ajax_message").html(datos);
                                   //$("#ajax_message").fadeIn("slow");
                                   $("#action_buttons").fadeIn("slow");
                                   apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                               }
                            }
                        });
                    }
                }
            }
            function validarAddProducto(indexProducto,idProducto,cantidad,monto,idUnidad,almacenId,almacenNombre){
            //alert(almacenNombre);
                if(indexProducto<0 || idProducto<=0 || cantidad<0 || monto<0 || idUnidad<=0 || (almacenId <=0 && almacenNombre !='Almacen Movil' )
                    || cantidad=="" || monto==""){
                    apprise('<center><img src=../../images/warning.png> <br/>Llene todos los datos requeridos para modificar el producto.</center>',{'animate':true});
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
                var cantidad = getFloatFromInput($('#producto_cantidad'));
                var precio = getFloatFromInput($('#producto_precio'));
                var monto = (precio * cantidad).toFixed(2);
                //alert('precio: ' + precio + ' cantidad: ' + cantidad + ' monto: ' + monto);
                $('#producto_monto').val(monto);
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
                        <input type="hidden" id="id_producto_compra" name="id_producto_compra" value="<%=productoSesion.getIdProducto()%>" />
                        <input type="hidden" id="producto_index" name="producto_index" value="<%=index_lista_producto%>" />
                        <fieldset>
                            <p>
                                <label>*Nombre: </label><br/>
                                <input maxlength="5" readonly type="text" name="producto_nombre" id="producto_nombre" size="54" value="<%=conceptoBO!=null?conceptoBO.getNombreConceptoLegible():"" %>"/>
                            </p>

                            <p>
                                <label>*Unidad: </label><br/>
                                <select size="1" name="unidad_select" id="unidad_select"  style="width:150px;">
                                    <option value="-1"></option>
                                    <%out.print(new UnidadBO(user.getConn()).getUnidadesHTMLCombo(-1)); %>
                                </select>
                            </p>

                            <p>
                                <label>*Precio: </label><br/>                              
                                <select id="producto_precio" name="producto_precio" 
                                        onchange="reCalcularMonto();" style="width: 150px;">
                                    <option <%=conceptoDto.getPrecio()==productoSesion.getPrecio()?"selected":""%> value="<%=conceptoDto.getPrecio()%>">
                                        <%=conceptoDto.getPrecio()%>&nbsp;[men.]
                                    </option>
                                    <option <%=conceptoDto.getPrecioMayoreo()==productoSesion.getPrecio()?"selected":""%> value="<%=conceptoDto.getPrecioMayoreo()%>">
                                        <%=conceptoDto.getPrecioMayoreo()%>&nbsp;[may.]
                                    </option>
                                    <option <%=conceptoDto.getPrecioDocena()==productoSesion.getPrecio()?"selected":""%> value="<%=conceptoDto.getPrecioDocena()%>">
                                        <%=conceptoDto.getPrecioDocena()%>&nbsp;[docena]
                                    </option>
                                    <option <%=conceptoDto.getPrecioEspecial()==productoSesion.getPrecio()?"selected":""%> value="<%=conceptoDto.getPrecioEspecial()%>">
                                        <%=conceptoDto.getPrecioEspecial()%>&nbsp;[especial]
                                    </option>
                                </select>
                            </p>
                            <p>                                 
                                <input type="hidden" maxlength="8" name="id_almacen_compra" id="id_almacen_compra" 
                                    style="text-align: right;" size="12"  
                                    value="<%=productoSesion.getIdAlmacen()%>"
                                    readonly/>
                            </p>
                            <p>
                                <label>*Cantidad: </label><br/> 
                                <input type="text" maxlength="8" name="producto_cantidad" id="producto_cantidad" 
                                    style="text-align: right;" size="12"
                                    onkeypress="return validateNumber(event);"
                                    onchange="reCalcularMonto()"
                                    value="<%=productoSesion.getCantidad() %>"/>
                            </p>

                            <p>
                                <label>Monto: </label><br/> 
                                    <input type="text" maxlength="12" name="producto_monto" id="producto_monto" 
                                    style="text-align: right;" size="12" onkeypress="return validateNumber(event);"
                                    readonly value="<%=productoSesion.getMonto() %>"/>
                            </p>
                            <br class="clear"/>
                            <dl class="submit">
                                <div id="action_buttons">
                                    <input type="button" name="Grabar" id="submit" value="Guardar" onclick="modificarProducto();"/>
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
