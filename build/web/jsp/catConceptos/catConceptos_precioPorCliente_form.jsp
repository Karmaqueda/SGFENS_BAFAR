<%-- 
    Document   : catConceptos_precioPorCliente
    Created on : 2/07/2015, 06:16:58 PM
    Author     : HpPyme
--%>

<%@page import="com.tsp.sct.bo.ClientePrecioConceptoBO"%>
<%@page import="com.tsp.sct.dao.dto.ClientePrecioConcepto"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<!DOCTYPE html>
<%
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp");
    response.flushBuffer();
} else {
    
    String msgError ="";
    int idProducto = -1;      
    String mode= request.getParameter("mode") != null ? request.getParameter("mode") : "";       
    String tipoPrecio= request.getParameter("tipoPrecio") != null ? request.getParameter("tipoPrecio") : "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    
    int idConcepto = 0;
    try {
        idConcepto = Integer.parseInt(request.getParameter("idConcepto"));
    } catch (NumberFormatException e) {
    }
    
    ClienteBO clienteBO = new ClienteBO(user.getConn());   
    ClientePrecioConceptoBO clientPrecioEspecialBO = new ClientePrecioConceptoBO(user.getConn());
    Cliente[] clientesDto = new Cliente[0];
    Cliente[] clientesACtivosDto = new Cliente[0];
     
    clientesDto = clienteBO.findClientes(-1, idEmpresa , 0, 0, " AND ID_ESTATUS <> 2 ");
    
    ClientePrecioConcepto[] preciosEspecialesDto = new ClientePrecioConcepto[0];
    
    
    
    
%>
<jsp:include page="../../jsp/include/registraNavegacionBitacora.jsp"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Precios Por Cliente</title>
        
        <jsp:include page="../include/skinCSS.jsp" />
        <jsp:include page="../../jsp/include/jsFunctions.jsp"/>
        <script language="javascript">
                        
                                   
            var postnameFlexSelect='_flexselect';
            
            
            function iniciarFlexSelect(){
               $("#precio_cliente").flexselect({
                    jsFunction:  function(id) { cargaClientesPrecio(id); }
                });
                

                $("select.flexselect").flexselect();
            }
            
            
            
            function inicializarPopup(){
                $('#left_menu').hide();
                $('body').addClass('nobg');
		$('#content').css('marginLeft', 10);         
                
            }
            
            $(document).ready(function(){
                $('#todos_clientes').dblclick(function() {
                assignList();
                });

                $('#select_clientes').dblclick(function() {
                unassignList();
                 });

                $('#to2').click(function()
                {
                assignList();
                });

                $('#to1').click(function()
                {
                unassignList();
                });
                
                $('#busca').keyup(function()
                {
                    var searchArea = $('#todos_clientes'); // Select Box ID
                    searchFirstList($(this).val(), searchArea);
                });
                
            });
            

            // function: UnAssignment
            function assignList()
            {
                // loop through first listbox and append to second listbox
                $('#todos_clientes :selected').each(function(i, selected){
                // append to second list box
                $('#select_clientes').append('<option value="'+selected.value+'">'+ selected.text+'</option>');
                // remove from first list box
                $("#todos_clientes option[value='"+ selected.value +"']").remove();
                });
            }
            // function: UnAssignment
            function unassignList()
            {
                // loop through second listbox and append to first listbox
                $('#select_clientes :selected').each(function(i, selected){
                // append to first list box
                $('#todos_clientes').append('<option value="'+selected.value+'">'+ selected.text+'</option>');
                // remove from second list box
                $("#select_clientes option[value='"+ selected.value +"']").remove();
                });
            }
            
            
            
            
            // Function for Filtering
            function searchFirstList(inputVal, searchArea)
            {
                var allCells = $(searchArea).find('option');
                if(allCells.length > 0)
                {
                    var found = false;
                    allCells.each(function(index, option)
                {
                var regExp = new RegExp(inputVal, 'i');
                if(regExp.test($(option).text()))
                    $(option).show();
                else
                    $(option).hide();
                });
                }
            }
            
            
            function grabar(){
                
                var clientes_precio = []; 
                var idProducto = $('#idProducto').val();
                var precio_cliente = $('#precio_cliente'+postnameFlexSelect).val();  
                var tipoPrecio = $('#tipoPrecio').val();  
                var listaClientes = "";
                $.each($("#select_clientes option"), function(){            
                    clientes_precio.push($(this).val());
                });                
                
                listaClientes = clientes_precio.join(",");
                
                $.ajax({
                    type: "POST",
                    url: "catConceptos_ajax.jsp",
                    data: {mode: 'precioEspecialCliente',idConcepto:idProducto,precioEspecialCliente:precio_cliente,
                            listaClientes: listaClientes, tipoPrecio : tipoPrecio},
                    beforeSend: function(objeto) {
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos) {
                        if (datos.indexOf("--EXITO-->", 0) > 0) {
                            $("#ajax_message").html(datos);
                            $("#ajax_loading").fadeOut("slow");
                            $("#ajax_message").fadeIn("slow");
                            apprise('<center><img src=../../images/info.png> <br/>' + datos + '</center>', {'animate': true},
                            function(r){   
                                 window.location.reload();
                             } );    
                            
                            
                        } else {
                            $("#ajax_loading").fadeOut("slow");
                            $("#ajax_message").html(datos);
                            $("#ajax_message").fadeIn("slow");
                            $("#action_buttons").fadeIn("slow");
                            $.scrollTo('#inner', 800);
                        }
                    }
                });
                
                
            }
            
            
            function cargaClientesPrecio(prod,tipoPrecio){
                var idProducto = $('#idProducto').val();
                var tipoPrecio = $('#tipoPrecio').val();               
                var precio_cliente = $('#precio_cliente'+postnameFlexSelect).val();
                if(precio_cliente>0){
                    $.ajax({
                        type: "POST",
                        url: "catConceptos_ajax.jsp",
                        data: { mode: 'selectClientesPrecio', idConcepto : idProducto, precio : precio_cliente,tipoPrecio : tipoPrecio},                            
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
                                $("#select_clientes").html(datos);  
                                $("#ajax_message").fadeOut("slow");
                            }else{
                                $("#ajax_loading").fadeOut("slow");
                                $("#ajax_message").html(datos);
                                $("#ajax_message").fadeIn("slow");
                                $("#action_buttons").fadeIn("slow");
                                //apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                            }
                        }
                    });
                    
                }else{
                    //$("#ajax_message").html("Selecciona un precio de la lista.");
                    //$("#ajax_message").fadeOut("slow");
                }
                    
                    
            }
            
            
            
            
        </script>        
        
    </head>
    <body scroll="no">
        <div id="left_menu">
        </div>
        <div id="content">
            <div class="inner">
                <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                <div id="ajax_message" class="alert_warning" style="display: none;"></div>
                <div>
                    <% if(idConcepto>0){%>
                    <form action="" method="post" id="frm_action">
                      
                        <fieldset>                                                        
                            
                            <h4>Seleccione los clientes para precio <%=tipoPrecio%>:</h4>                            
                            <p>
                                <input type="hidden" id="idProducto" name="idProducto" value="<%=idConcepto%>"/>
                                <input type="hidden" id="tipoPrecio" name="tipoPrecio" value="<%=tipoPrecio%>"/>
                                <input type="text" id="busca" name="busca" title="Busqueda por Nombre"  style="width: 210px; float: left; "/>
                                       
                                <input disabled type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 20px; height: 18px; float: left"/>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <label>*Precio: </label>
                                <!--<input type="text" id="precio_cliente" name="precio_cliente" title="$$$$"  style="width: 30;"
                                       onkeypress="return validateNumber(event);" />-->
                                <select size="1" name="precio_cliente" id="precio_cliente"  style="width:120px;"
                                            onchange="cargaClientesPrecio(this.value,'<%=tipoPrecio%>');"
                                            class="flexselect">
                                <option value="-1"></option>
                                <%out.print(clientPrecioEspecialBO.getPreciosEspecialesByIdHTMLCombo(-1, idConcepto, idEmpresa, -1, -1, "",tipoPrecio));%>
                                </select>
                                
                            </p>
                            <br/>
                            <p> 
                                <label>Clientes:</label>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;            
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <label>Clientes Con Precio especial:</label>                                    
                                <br/>
                                <select id="todos_clientes" name="todos_clientes" style="width: 260px; height: 150px;" size="5" multiple="multiple">
                                    <%
                                        for(Cliente item : clientesDto){
                                            String nombreCliente = ""; 
                                            
                                            if(nombreCliente.equals("")){
                                                nombreCliente = item.getRazonSocial();
                                            }
                                            if(nombreCliente.equals("")){
                                                nombreCliente = item.getNombreComercial();
                                            }
                                            
                                            if(nombreCliente.equals("")){
                                                if(item.getNombreCliente()!=null){
                                                    nombreCliente += item.getNombreCliente();
                                                }
                                                if((item.getApellidoPaternoCliente()!=null)&&(!item.getApellidoPaternoCliente().toUpperCase().equals("NULL"))&&(!item.getApellidoPaternoCliente().toUpperCase().equals("CAMPO POR LLENAR"))){
                                                    nombreCliente += " " + item.getApellidoPaternoCliente();
                                                }
                                                if((item.getApellidoMaternoCliente()!=null)&&(!item.getApellidoMaternoCliente().toUpperCase().equals("NULL"))&&(!item.getApellidoMaternoCliente().toUpperCase().equals("CAMPO POR LLENAR"))){
                                                    nombreCliente += " " + item.getApellidoPaternoCliente();
                                                }
                                            }
                                            
                                            if(item.getClave() !=null && !item.getClave().trim().equals("")){
                                                nombreCliente += ", Clave: "+item.getClave();
                                            }  

                                            %>
                                            <option value="<%=item.getIdCliente()%>"><%=nombreCliente%></option>
                                            <%
                                        }
                                    %>
                                </select>
                                <input id="to2" type="button" name="to2"  title='assign' value=">>" />
                                <input id="to1" type="button" name="to1" title='unassign' value="<<">                                
                                <select id="select_clientes" name="select_clientes" style="width: 260px;height: 150px;" size="5" readonly  multiple="multiple">                                    
                                </select>                             
                                <!--<div id="div_clientes_precios" name="div_clientes_precios" 
                                        style="display: inline-block; ">
                                </div>-->
                            </p>

                            <dl class="submit">
                            <br class="clear"/>
                                <div id="action_buttons">
                                    <input type="button" name="Seleccionar" id="submit" value="Guardar" onclick="grabar();"/>
                                    <input type="button" name="Cancelar" id="submit" value="Cancelar" onclick="parent.$.fancybox.close();"/>
                                </div>
                                <div id="ajax_loading"></div>
                                <div id="ajax_message" class="info_box" style="display: none;"></div>
                            </dl>
                        </fieldset>
                    </form>
                 <% }else{%>                 
                 <script>apprise('<center><img src=../../images/info.png> <br/>Primero guarde el Concpeto<br>\n\
                                  para agregar precios por Cliente.</center>', {'animate': true});</script>
                 <% }%>
                </div>
                
            </div>
        </div>
            
        <script>
            inicializarPopup();
            iniciarFlexSelect();
        </script>
            
    </body>
</html>
<%
}
%>
