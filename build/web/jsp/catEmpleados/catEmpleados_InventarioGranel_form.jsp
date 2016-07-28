<%-- 
    Document   : catEmpleados_InventarioGranel_form
    Created on : 8/07/2015, 04:16:41 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.util.FormatUtil"%>
<%@page import="com.tsp.sct.bo.AlmacenBO"%>
<%@page import="com.tsp.sct.dao.dto.ExistenciaAlmacen"%>
<%@page import="com.tsp.sct.bo.ExistenciaAlmacenBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoInventarioRepartidorDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoInventarioRepartidor"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el cliente tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {


    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";

    int idEmpresa = user.getUser().getIdEmpresa();

    /*
     * Parámetros
     */
    int idEmpleado = 0;
    try {
        idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
    } catch (NumberFormatException e) {
    }

    int idConcepto = -1;
    try {
        idConcepto = Integer.parseInt(request.getParameter("idConcepto"));
    } catch (NumberFormatException e) {
    }

    /*
     *   0/"" = nuevo
     *   1 = editar/consultar
     *   2 = eliminar  
     */
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";

    EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
    Empleado empleadoDto = null;
    if (idEmpleado > 0){
        empleadoBO = new EmpleadoBO(idEmpleado, user.getConn());
        empleadoDto = empleadoBO.getEmpleado();
    }       
    String parameter1 = "idEmpleado";

    EmpleadoInventarioRepartidor[] inventarios = null;
    EmpleadoInventarioRepartidor inventario = null;
    Concepto conceptoDto = null;
    Concepto subProductoGranel = null;

    if(idConcepto > 0){
        inventarios = new EmpleadoInventarioRepartidorDaoImpl(user.getConn()).findByDynamicWhere("ID_EMPLEADO = "+idEmpleado+" AND ID_CONCEPTO = "+idConcepto, null);
        inventario = inventarios[0];
        
        subProductoGranel = new ConceptoBO(idConcepto, user.getConn()).getConcepto();
    }


    //si hay datos en sesion de la lista en sesion de inventario del repartidor la vaciamos
    session.setAttribute("empleadoDatosInventarioSesion", null);
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
           
           var postnameFlexSelect='_flexselect';
           
            function grabar(mode){
                if(validarPeso()){
                    if(validar(mode)){
                        $.ajax({
                            type: "POST",
                            url: "catEmpleados_ajax.jsp",
                            data: $("#frm_action").serialize(),
                            beforeSend: function(objeto){
                                $("#action_buttons").fadeOut("slow");
                                $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                                $("#ajax_loading").fadeIn("slow");
                            },
                            success: function(datos){
                                if(datos.indexOf("--EXISESION-->", 0)>0){
                                    //alert("agregando");
                                   $("#div_ajax_productos").html(datos);
                                   //$("#ajax_message").html(datos);
                                   $("#ajax_loading").fadeOut("slow");
                                   $("#action_buttons").fadeIn("slow");
                                   //$("#ajax_message").fadeIn("slow");
                                   /*apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                            function(r){
                                                location.href = "catEmpleados_Inventario_list.jsp?idEmpleado=<%=idEmpleado%>";
                                            });*/
                               }else if(datos.indexOf("--EXITO-->", 0)>0){
                                   //alert("retornando");
                                   $("#ajax_message").html(datos);
                                   $("#ajax_loading").fadeOut("slow");
                                   $("#action_buttons").fadeIn("slow");
                                   $("#ajax_message").fadeIn("slow");
                                   apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                            function(r){
                                                location.href = "catEmpleados_Inventario_list.jsp?idEmpleado=<%=idEmpleado%>";
                                            });
                               }else{
                               //alert("nada....");
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
            }

            function validarPeso(){
                var pesoCantidad = $('#pesoArticulosInventarioAsignar').val();
                if(pesoCantidad.trim() === "" || pesoCantidad <= 0){
                    apprise('<center><img src=../../images/info.png> <br/>'+ "El Peso debe ser mayor a 0" +'</center>',{'animate':true},
                        function(r){});
                    return false;
                }else{
                    return true;
                }
                
            }

            function validar(mode){
            
                if(mode==='agregar'){
                    $('#mode').val("agregarInventarioGranel");
                }else if(mode==='eliminar'){                    
                    $('#mode').val("devolverInventarioGranel");
                }else if(mode==='agregarInventarioSesionTemporal'){                    
                    $('#mode').val("agregarInventarioSesionTemporal");
                }                
            
                /*
                if(jQuery.trim($("#nombre").val())==""){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "nombre de contacto" es requerido</center>',{'animate':true});
                    $("#nombre_contacto").focus();
                    return false;
                }
                */
                return true;
            }
            
            
            
            //Usar en caso de que los select usen libreria jquery flexselect para poder usar los eventos focus,etc.. 
            var postnameFlexSelect='_flexselect';            
            
            function iniciarFlexSelect(){
                
                $("#idConceptoInventario").flexselect({
                    jsFunction:  function(id) { selectProducto(id); }
                });
                
                $("select.flexselect").flexselect();
                
                selectProducto();
            }
            
            
            function selectProducto(idProducto){   
                <% if (idConcepto<=0){ //Nuevo, creacion de subproducto a granel %>
                    var idProducto= $('#idConceptoInventario').val();                    
                    var idAlm = $('#idAlmacen').val();   
                    if(idAlm>0 && idProducto>0 ){
                        $("#divDetalle").load('catEmpleados_ajax.jsp?mode=creaInputs&idConcep='+idProducto+"&idAlmacen="+idAlm);
                    }else{
                       /*$("#ajax_loading").fadeOut("slow");
                       $("#ajax_message").html('Selecciona un Almacén y/o Producto ');
                       $("#ajax_message").fadeIn("slow");
                       $("#action_buttons").fadeIn("slow");
                       $.scrollTo('#inner',800);
                       $("#ajax_message").fadeOut("slow");*/
                    }

                    //obtenemos existencia de alamcenes
                    $('#almacen_ref').attr('href','../../jsp/includeProductoSelect/almacen_select_form.jsp?mode=1&idProducto='+idProducto);
                <% } else { //Edicion, devolucion de subproducto --> Producto Padre%>
                    //obtenemos almacenes de PRODUCTO PADRE, no del subproducto
                    $('#almacen_ref').attr('href','../../jsp/includeProductoSelect/almacen_select_form.jsp?mode=1&idProducto=<%= subProductoGranel.getIdConceptoPadre() %>');     
                <% } %>
                    
            }
            
            function recarga(){             
                var busca = $("#idConceptoInventario_flexselect").val();
                if (busca.trim().length>0){
                    location.href='catEmpleados_InventarioGranel_form.jsp?idEmpleado='+<%=idEmpleado%>+'&q='+busca;
                }else
                    location.href='catEmpleados_InventarioGranel_form.jsp?idEmpleado='+<%=idEmpleado%>;
            }   
            
            function selectAlmacenFromPopup(idAlmacen, nombreAlmacen){
                
                $("#idAlmacen").val(idAlmacen);
                $('#idAlmacen'+postnameFlexSelect).val(nombreAlmacen);
                
            } 
            
            function subProducto(indexListaProductosSesion){
                if(indexListaProductosSesion>=0){
                    $.ajax({
                        type: "POST",
                        url: "catEmpleados_ajax.jsp",
                        data: { mode: 'quitarInventarioSesionTemporal', index_lista_producto : indexListaProductosSesion },
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
                        
        </script>
    </head>
    <body>
        
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">
        
                <div class="inner">
                    <h1>Catálogo</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <div class="twocolumn">
                    <form action="" method="post" id="frm_action">
                        
                        <div class="column_left" >
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_producto.png" alt="icon"/>
                                    <% if(empleadoDto!=null){%>
                                    Repartidor ID <%=empleadoDto!=null?empleadoDto.getIdEmpleado():"" %>
                                    <%}else{%>
                                    Repartidor
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idEmpleado" name="idEmpleado" value="<%=empleadoDto!=null?empleadoDto.getIdEmpleado():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="" />
                                    <input type="hidden" id="idInventarioAsignado" name="idInventarioAsignado" value="<%=inventario!=null?inventario.getIdInventario():null%>" />
                                    <input type="hidden" id="aGranel" name="aGranel" value="aGranel" />
                                    <p>
                                        <label>*Nombre:</label><br/>
                                        <input maxlength="100" type="text" id="nombreEmpleadoAsignarInventario" name="nombreEmpleadoAsignarInventario" style="width:300px"
                                               value="<%=empleadoDto!=null?empleadoBO.getEmpleado().getNombre()+" "+empleadoBO.getEmpleado().getApellidoPaterno():"" %>"
                                               <%=empleadoDto!=null?"readonly":""%>/>
                                    </p>
                                    <br/> 
                                    <p>
                                        <label>Concepto:</label><br/>
                                        <% if(idConcepto <=0 ){ //Nuevo (Creacion de subproducto a granel a partir de producto padre)%>
                                            <select size="1" name="idConceptoInventario" id="idConceptoInventario"  style="width:300px;"
                                                                    onchange="selectProducto(this.value);"
                                                                    class="flexselect">
                                                <option value="-1"></option>
                                                <%
                                                    out.print(new ConceptoBO(user.getConn()).getConceptosGranelByIdHTMLComboReload(idEmpresa, idConcepto,0,250,buscar, " AND ( PRECIO_UNITARIO_GRANEL > 0 OR PRECIO_MEDIO_GRANEL > 0 OR PRECIO_MAYOREO_GRANEL > 0 OR PRECIO_ESPECIAL_GRANEL > 0 ) AND (ID_CONCEPTO_PADRE<=0 OR ID_CONCEPTO_PADRE IS NULL) "));
                                                %>
                                            </select>
                                            <%if(inventario==null){%>
                                            <button type="button" id="buscar" name="buscar"  value=""  onclick="recarga();"><img src="../../images/Search-32_2.png" alt="Buscar" style="cursor: pointer; width: 30px; height: 25px;" /><br>Buscar</button>
                                            <% } %>
                                        <% }else{ //Edicion (Solo para devolucion de subproducto hacia producto padre 
                                        %>
                                            <input type="hidden" id="idConceptoInventario" name="idConceptoInventario" value="<%= idConcepto %>"/>
                                            <input type="text" style="width:300px;" readonly value="<%= subProductoGranel!=null?subProductoGranel.getNombreDesencriptado() : "" %>" />
                                        <% } %>
                                    </p>   
                                    <br/>
                                    <p>
                                        <label>Almacén:</label> <br/>
                                        <a id="almacen_ref" class="modalbox_iframe" href="../../jsp/includeProductoSelect/almacen_select_form.jsp?mode=100">                                            
                                            <select size="1" id="idAlmacen" name="idAlmacen" onchange="selectProducto();" class="flexselect" >
                                                <option value="-1">Selecciona un Almacén</option>                                                    
                                            </select>                                                                                                                                             
                                        </a>
                                    </p>
                                    <br/>
                                    
                                    <%
                                        if(inventario!=null){
                                            ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
                                            conceptoDto = conceptoBO.findConceptobyId(idConcepto);    

                                            ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(user.getConn()); 
                                            ExistenciaAlmacen almPrincipal = exisAlmBO.getExistenciaProductoAlmacenPrincipal(conceptoDto.getIdEmpresa(), conceptoDto.getIdConcepto());
                                    %>
                                        <br/>
                                        <p>
                                            <label>Código:</label><br/>
                                            <input type='text' id='sku' style='width:300px' name='sku' value='<%=conceptoDto.getIdentificacion()!=null?!conceptoDto.getIdentificacion().equals("")?conceptoDto.getIdentificacion():"Sin código":"Sin código"%>' readonly/>
                                        </p>
                                        <br/>
                                        <!--<p>
                                            <label>Disponibles en Almacén:</label><br/>
                                            <input type='text' id='diponibles' style='width:300px' name='disponibles' value='<%=almPrincipal!=null?almPrincipal.getExistencia():0%>' />
                                        </p>
                                        <br>-->
                                    <%  
                                        }else{
                                    %>
                                        <div id="divDetalle" name="divDetalle"></div>    <!--Div detalle producto -->
                                    <%
                                        }
                                    %>
                                    
                                    <% if(idConcepto > 0){ //Edicion, campos de solo lectura %>
                                        <fieldset style="border: 2px groove; padding: 1em;">
                                        <legend>Asignado Actualmente</legend>
                                            <p>
                                                <label>Cantidad Piezas:</label><br/>
                                                <input readonly="true" maxlength="10" type="text" id="numArticulosInventarioActual" name="numArticulosInventarioActual" style="width:130px"
                                                       value="<%=inventario!=null?inventario.getCantidad():"0"%>" />
                                                &nbsp; de &nbsp;
                                                <input readonly="true" maxlength="10" type="text" id="pesoArticulosInventarioActual" name="pesoArticulosInventarioActual" style="width:130px"
                                                       value="<%=inventario!=null?FormatUtil.doubleToString(inventario.getPeso()):"0"%>" />
                                                &nbsp; Kg.
                                            </p>
                                            <br/>
                                            <p>
                                                <label>Granel Total:</label><br/>
                                                <input readonly="true" maxlength="10" type="text" id="pesoTotalInventarioActual" name="pesoTotalInventarioActual" style="width:270px"
                                                       value="<%=inventario!=null?FormatUtil.doubleToString(inventario.getExistenciaGranel()):"0"%>" />
                                                &nbsp; Kg.
                                            </p>
                                        </fieldset>
                                        <br/>
                                    <%}%>
                                    
                                    <p>
                                        <label>Piezas (Unidad) a <%= idConcepto <= 0? "Asignar": "Devolver" %>:</label><br/>
                                        <input maxlength="10" type="text" id="numArticulosInventarioAsignar" name="numArticulosInventarioAsignar" style="width:300px"
                                               value="" onkeypress="return validateNumberInteger(event);"/>
                                    </p>
                                    <br/>
                                    <% //if(idConcepto <=0 ){ //Nuevo - Asignar, crear un subproducto con X peso unitario%>
                                    <p>
                                        <label>*Peso <%= idConcepto <= 0? "Unitario (Kg)": " de cada Pieza a Devolver (kg)" %>:</label><br/>
                                        <input maxlength="10" type="text" id="pesoArticulosInventarioAsignar" name="pesoArticulosInventarioAsignar" style="width:300px"
                                               value="<%=inventario!=null?FormatUtil.doubleToString(inventario.getPeso()):""%>" onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                                    <% //}else{ //Edicion - Solo para devolver hacia Producto Padre original usando cantidad o Peso total%>
                                    <!--
                                    <h2>Ó</h2>
                                    <br/>
                                    <p>
                                        <label>*Peso <b>Total</b> a Devolver (Kg):</label><br/>
                                        <input maxlength="10" type="text" id="pesoTotalInventarioDevolver" name="pesoTotalInventarioDevolver" style="width:300px"
                                               value="" onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                                    -->
                                    <% //} %>
                                    
                                    <br/>
                                     <div id="action_buttons">
                                        <p>                 
                                            <% if(idConcepto <=0 ){ //Nuevo - Creacion de subproducto a granel%>
                                            <button type="button" id="enviar" value="Agregar" onclick="grabar('agregarInventarioSesionTemporal');"><img src="../../images/anadir_camion_32.png" alt="Agregar a Inventario" /><br>Crear sub-producto y Agregar</button>
                                            <%}else{ //Edicion - Devolver de subproducto a Producto Padre original %>  
                                            <button type="button" id="devolver" value="Eliminar" onclick="grabar('eliminar');"><img src="../../images/eliminar_camion_32.png" alt="Devolver a Inventario" /><br>Devolver a Producto Original</button>
                                            <br>                                            
                                            <% } %>
                                            <br><br>
                                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>                                            
                                        </p>
                                    </div>  
                             </div>
                                    
                        </div>
                        <!-- End left column window -->
                        
                    
                    </form>
                    
                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    
                    

                </div>   
                    <div class="content">                                    
                        <div id="div_producto_ajax_loading" class="alert_warning noshadow" style="display: none;"></div>
                        <div id="div_ajax_productos" class="ajax_selected_box"></div>
                    </div>                
           </div> 
       </div> 
                                
                                
                                
         <script>           
           iniciarFlexSelect();
        </script>
    </body>
</html>
<%}%>