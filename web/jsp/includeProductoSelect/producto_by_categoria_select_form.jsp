<%-- 
    Document   : producto_by_categoria_select_form.jsp
    Created on : 04/01/2012, 09:21:14 AM
    Author     : ISC Cesar Martinez poseidon24@hotmail.com
--%>

<%@page import="com.tsp.sct.bo.CategoriaBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp");
    response.flushBuffer();
} else {
    String msgError ="";
    String mode= request.getParameter("mode") != null ? request.getParameter("mode") : "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
%>
<jsp:include page="../../jsp/include/registraNavegacionBitacora.jsp"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Seleccionar Producto por Categoría</title>
        
        <jsp:include page="../include/skinCSS.jsp" />
        <jsp:include page="../../jsp/include/jsFunctions.jsp"/>
        <script language="javascript">
            
            function selectProducto(){
                var idProducto = $('#select_producto').val();
                var nombreProducto = $("#select_producto option[value='"+idProducto+"']").text();
                if (idProducto>0){
                    parent.selectProductoFromPopupCategoria(idProducto,nombreProducto);
                    parent.$.fancybox.close();
                }
            }
            
            /**
             * Carga en un nuevo div las subcategorias correspondientes 
             * a la categoria padre seleccionada.
             * 
             * Admite múltiples niveles.
             */
            function cargaSubCategorias(idCategoriaPadre,nombreCategoria, nivelSubCategorias){
                //alert("idCategoriaPadRE: " + idCategoriaPadre + " ,nombreCat: " + nombreCategoria + " , nivel: " + nivelSubCategorias);
                //if (idCategoriaPadre>0){
                    $.ajax({
                        type: "POST",
                        url: "producto_by_categoria_select_ajax.jsp",
                        data: { mode: '2', idCategoriaPadre : idCategoriaPadre,
                                nivelSubCategorias : nivelSubCategorias},
                            //$("#frm_action").serialize(),
                        beforeSend: function(objeto){
                            //Dejamos vacío el div correspondiente
                            //$("div_subcategorias_nivel_"+nivelSubCategorias).html('XXXX');
                            
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><img src="../../images/ajax_loader.gif" alt="Cargando.." />Procesando... </div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                                //$("#ajax_message").html(datos);
                                $("#ajax_loading").fadeOut("slow");
                                $("#action_buttons").fadeIn("slow");
                                $("#ajax_message").fadeIn("slow");
                                
                                //alert(datos);
                                datos = $.trim(datos.replace('<!--EXITO-->',''));
                                $("#div_subcategorias_nivel_"+nivelSubCategorias+"").html(datos);                               
                            }else{
                                $("#ajax_loading").fadeOut("slow");
                                $("#ajax_message").html(datos);
                                $("#ajax_message").fadeIn("slow");
                                $("#action_buttons").fadeIn("slow");
                                //apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                            }
                        }
                    });
                    
                    cargaProductosCategoria(idCategoriaPadre);
                /*
                }else{
                    //Se eligio la opción de "Sin Categoría", 
                    //se mostraran todos los productos sin categoría
                    cargaProductosCategoria(0);
                }*/
                
                $('#nombre_categoria').html(nombreCategoria);
            }
            
            /**
             *  Carga los productos correspondientes a la categoría seleccionada
             */
            function cargaProductosCategoria(idCategoria){
                if (idCategoria>=0){
                    $.ajax({
                            type: "POST",
                            url: "producto_by_categoria_select_ajax.jsp",
                            data: { mode: '1', idCategoria : idCategoria},
                                //$("#frm_action").serialize(),
                            beforeSend: function(objeto){
                                $("#action_buttons").fadeOut("slow");
                                $("#ajax_loading").html('<div style=""><img src="../../images/ajax_loader.gif" alt="Cargando.." />Procesando... </div>');
                                $("#ajax_loading").fadeIn("slow");
                            },
                            success: function(datos){
                                if(datos.indexOf("--EXITO-->", 0)>0){
                                   //$("#ajax_message").html(datos);
                                   $("#ajax_loading").fadeOut("slow");
                                   $("#action_buttons").fadeIn("slow");
                                   $("#ajax_message").fadeIn("slow");
                                    
                                    //alert(datos);
                                    datos = $.trim(datos.replace('<!--EXITO-->',''));
                                    $("#select_producto").html(datos);
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
            
            
            
            function buscaProds(){
                var nameProd = $("#productoMovimiento").val();                
                
                $.ajax({
                    type: "POST",
                    url: "product_by_parametro_select_ajax.jsp",
                    data: { mode: '1', q : nameProd },
                        //$("#frm_action").serialize(),
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><img src="../../images/ajax_loader.gif" alt="Cargando.." />Procesando... </div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                               //$("#ajax_message").html(datos);
                               $("#ajax_loading").fadeOut("slow");
                               $("#action_buttons").fadeIn("slow");
                               $("#ajax_message").fadeIn("slow");

                                //alert(datos);
                                datos = $.trim(datos.replace('<!--EXITO-->',''));
                                $("#select_producto").html(datos);
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
            
            
            function inicializarPopup(){
                $('#left_menu').hide();
                $('body').addClass('nobg');
		$('#content').css('marginLeft', 30);
                
                cargaProductosCategoria(0);
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
                        <!--<input type="hidden" id="id_producto" name="id_producto" value="0" />-->
                        <fieldset>
                            <p>
                                <label>Busqueda por Nombre , Marca  o Clave</label><br>
                                <input type="text" name="productoMovimiento" id="productoMovimiento"  style="width:300px;"/>
                                <img src="../../images/Search-32_2.png" alt="Busca Productos" style="cursor: pointer; width: 30px; height: 25px;" onclick="buscaProds(); " />
                            </p>
                            <br>
                            <p style="display: block;">
                                
                                <label>Navegar por categorías </label><br/>
                                <div style="display: block;">
                                    <div style="display: inline; float: left;">                              
                                        <select id="select_categoria" name="select_categoria" 
                                                onchange="cargaSubCategorias(this.value, this.options[this.selectedIndex].innerHTML, 0);" style="width: 120px;"
                                                size="5">
                                            <option selected value="0">Sin Categoría</option>
                                            <%= new CategoriaBO(user.getConn()).getCategoriasByIdHTMLCombo(idEmpresa, -1," AND (id_categoria_padre<=0 OR id_categoria_padre IS NULL) ") %>
                                        </select>
                                    </div>
                                    <div id="div_subcategorias_nivel_0" name="div_subcategorias_nivel_0" 
                                        style="display: inline-block; ">
                                    </div>
                                </div>
                            </p>
                            <br class="clear"/>
                            <p>
                                <label>Productos 
                                    "<span id="nombre_categoria" name="nombre_categoria" style="display: inline;">Sin Categoría</span>" 
                                </label><br/>                              
                                <select id="select_producto" name="select_producto" 
                                        style="width: 400px;" size="5">
                                    <!--<option selected value="-1"></option>-->
                                </select>
                            </p>

                            <br class="clear"/>
                            <dl class="submit">
                                <div id="action_buttons">
                                    <input type="button" name="Seleccionar" id="submit" value="Seleccionar" onclick="selectProducto();"/>
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
<%
}
%>