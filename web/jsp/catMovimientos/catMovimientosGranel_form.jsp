<%-- 
    Document   : catMovimientosGranel_form
    Created on : 24/07/2015, 04:12:51 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.util.FormatUtil"%>
<%@page import="com.tsp.sct.bo.SGProveedorBO"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensClienteVendedor"%>
<%@page import="com.tsp.sct.bo.SGClienteVendedorBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.bo.PasswordBO"%>
<%@page import="com.tsp.sct.bo.MovimientoBO"%>
<%@page import="com.tsp.sct.bo.AlmacenBO"%>
<%@page import="com.tsp.sct.dao.jdbc.MovimientoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Movimiento"%>
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
        int idMovimiento = 0;
        try {
            idMovimiento = Integer.parseInt(request.getParameter("idMovimiento"));
        } catch (NumberFormatException e) {
        }
        
        int idConcepto = 0;
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
        String newRandomPass = "";
        
        MovimientoBO movimientoBO = new MovimientoBO(user.getConn());
        Movimiento movimientosDto = null;
        if (idMovimiento > 0){
            movimientoBO = new MovimientoBO(idMovimiento,user.getConn());
            movimientosDto = movimientoBO.getMovimiento();
        }else{
            newRandomPass = new PasswordBO().generateValidPassword();
        }
        
        ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
        Concepto conceptosDto = null;
        if (idConcepto > 0){
            conceptoBO = new ConceptoBO(idConcepto,user.getConn());
            conceptosDto = conceptoBO.getConcepto();
        }else{
            newRandomPass = new PasswordBO().generateValidPassword();
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
                        url: "catMovimientos_ajax.jsp",
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
                                            location.href = "catMovimientos_list.jsp";
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
            
            function valMov(){
                
                if($('#tipoMovimiento').val()== 'ENTRADA'){
                    $('#divCompras').css('display','block');
                    $('#almDestino').css('display','none');
                }else if($('#tipoMovimiento').val()== 'SALIDA'){
                    $('#divCompras').css('display','none');
                    $('#almDestino').css('display','none');
                }else if($('#tipoMovimiento').val()== 'TRASPASO'){
                    $('#almDestino').css('display','block');  
                    $('#divCompras').css('display','none');
                }
            }
            
            
            //Usar en caso de que los select usen libreria jquery flexselect para poder usar los eventos focus,etc.. 
            var postnameFlexSelect='_flexselect';
            
            
            function iniciarFlexSelect(){                
                
                $("select.flexselect").flexselect();
            }
            
            
            
            function recarga(){             
                var busca = $("#productoMovimiento_flexselect").val();
                location.href='catMovimientos_form.jsp?q='+busca;
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
                    <h1>Almacén</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_movimiento.png" alt="icon"/>
                                    <% if(movimientosDto!=null){%>
                                    Editar Movimiento ID <%=movimientosDto!=null?movimientosDto.getIdMovimiento():"" %>
                                    <%}else{%>
                                    Movimiento
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idMovimiento" name="idMovimiento" value="<%=movimientosDto!=null?movimientosDto.getIdMovimiento():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>*Tipo de Movimiento</label><br/>
                                        <select id="tipoMovimiento" name="tipoMovimiento" onchange="valMov();">
                                            <optgroup label="Tipo:">
                                                <option value="SELECCIONE" <%=movimientosDto!=null?(movimientoBO.getMovimiento().getTipoMovimiento()=="SELECCIONE"?"selected":""):"selected" %>>SELECCIONE</option>
                                                <option value="ENTRADA" <%=movimientosDto!=null?(movimientoBO.getMovimiento().getTipoMovimiento()=="ENTRADA"?"selected":""):"" %>>ENTRADA</option>
                                                <option value="SALIDA" <%=movimientosDto!=null?(movimientoBO.getMovimiento().getTipoMovimiento()=="SALIDA"?"selected":""):"" %>>SALIDA</option>
                                                <option value="TRASPASO" <%=movimientosDto!=null?(movimientoBO.getMovimiento().getTipoMovimiento()=="TRASPASO"?"selected":""):"" %>>TRASPASO</option>
                                            </optgroup>
                                        </select>
                                    </p>
                                    <br/>  
                                    <p>
                                        <label>Producto:</label><br/>                                       
                                        <select size="1" name="productoMovimiento" id="productoMovimiento"  style="width:300px;"
                                                            onchange="selectProducto(this.value);"
                                                            class="flexselect">
                                        <option value="-1"></option>
                                                <%
                                                    out.print(new ConceptoBO(user.getConn()).getConceptosGranelByIdHTMLComboReload(idEmpresa, (conceptosDto!=null?conceptoBO.getConcepto().getIdConcepto():-1),0,250,buscar, " AND ( PRECIO_UNITARIO_GRANEL > 0 OR PRECIO_MEDIO_GRANEL > 0 OR PRECIO_MAYOREO_GRANEL > 0 OR PRECIO_ESPECIAL_GRANEL > 0 )"));
                                                %>
                                        </select>    
                                        <button type="button" id="buscar" name="buscar"  value=""  onclick="recarga();"><img src="../../images/Search-32_2.png" alt="Devolver a Inventario" style="cursor: pointer; width: 30px; height: 25px;" /><br>Buscar</button>
                                    </p>
                                    <p>
                                        <label>Almacén:</label> <br/>
                                        <select size="1" id="idAlmacen" name="idAlmacen" >
                                        <option value="-1">Selecciona un Almacén</option>
                                                <%
                                                    out.print(new AlmacenBO(user.getConn()).getAlmacenesByIdHTMLCombo(idEmpresa, -1));
                                                %>
                                        </select>                                       
                                    </p>
                                    <br/>
                                    <div id="almDestino" name="almDestino" style="display:none;">
                                        <p>
                                            <label>Almacén Destino:</label> <br/>
                                            <select size="1" id="idAlmacenDestino" name="idAlmacenDestino" >
                                            <option value="-1">Selecciona un Almacén</option>
                                                    <%
                                                        out.print(new AlmacenBO(user.getConn()).getAlmacenesByIdHTMLCombo(idEmpresa, -1));
                                                    %>
                                            </select>                                       
                                        </p>
                                        <br/>
                                    </div>
                                    <br/>
                                    
                                    <p>
                                        <label>*Cantidad del Movimiento:</label><br/>
                                        <input maxlength="10" type="text" id="contabilidadMovimiento" name="contabilidadMovimiento" style="width:300px"
                                               value="<%=movimientosDto!=null?movimientoBO.getMovimiento().getContabilidad():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Peso (Kg):</label><br/>
                                        <input maxlength="50" type="text" id="pesoArticulosInventarioAsignar" name="pesoArticulosInventarioAsignar" style="width:300px"
                                               value="<%=movimientosDto!=null?FormatUtil.doubleToString(movimientoBO.getMovimiento().getPeso()):"0"%>"/>
                                    </p>
                                    <br/>
                                    <div id="divCompras" style="display:none;">
                                        <p>
                                            <label>Proveedor:</label><br/>
                                            <select size="1" id="proveedorMovimiento" name="proveedorMovimiento" >
                                            <option value="-1">Selecciona un Proveedor</option>
                                                    <%
                                                        out.print(new SGProveedorBO(user.getConn()).getProveedorsByIdHTMLCombo(idEmpresa, (movimientosDto!=null?movimientosDto.getIdProveedor():-1) ));
                                                    %>
                                            </select>                                       
                                        </p>
                                        <br/>


                                        <p>
                                            <label>Orden de Compra:</label><br/>
                                            <input maxlength="30" type="text" id="ordenCompraMovimiento" name="ordenCompraMovimiento" style="width:300px"
                                                   value="<%=movimientosDto!=null?movimientoBO.getMovimiento().getOrdenCompra():"" %>"/>
                                        </p>
                                        <br/>
                                        <p>
                                            <label>Numero de Guía:</label><br/>
                                            <input maxlength="30" type="text" id="numGiaMovimiento" name="numGiaMovimiento" style="width:300px"
                                                   value="<%=movimientosDto!=null?movimientoBO.getMovimiento().getIdProveedor():"" %>"/>
                                        </p>
                                        <br/>    
                                    </div>
                                    <p>
                                        <label>Concepto del Movimiento:</label><br/>
                                        <input maxlength="30" type="text" id="porQueMovimiento" name="porQueMovimiento" style="width:300px"
                                               value="<%=movimientosDto!=null?movimientoBO.getMovimiento().getConceptoMovimiento():"" %>"/>
                                    </p>
                                    <br/>
                                    
                                    
                                    <br/><br/>
                                    
                                    <div id="action_buttons">
                                        <p>
                                            <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
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

        <script>           
           iniciarFlexSelect();
        </script>
    </body>
</html>
<%}%>