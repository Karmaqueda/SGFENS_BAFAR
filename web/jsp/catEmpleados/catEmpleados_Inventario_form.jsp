<%-- 
    Document   : catEmpleados_Inventario_form
    Created on : 14/05/2014, 12:12:31 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
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
        
        if(idConcepto > 0){
            inventarios = new EmpleadoInventarioRepartidorDaoImpl(user.getConn()).findByDynamicWhere("ID_EMPLEADO = "+idEmpleado+" AND ID_CONCEPTO = "+idConcepto, null);
            inventario = inventarios[0];
            
        }
        
        
        EmpresaBO empresaBO = new EmpresaBO(user.getConn());
        EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa());
        
        String verificadorSesionGuiaCerrada = "0";
        String cssDatosObligatorios = "border:3px solid red;";//variable para colocar el css del recuadro que encierra al input para la guia interactiva
        try{
            if(session.getAttribute("sesionCerrada")!= null){
                verificadorSesionGuiaCerrada = (String)session.getAttribute("sesionCerrada");
            }
        }catch(Exception e){}
        
        boolean modoAdicionInventarioInicial = session.getAttribute("modoAdicionInventarioInicial")!=null? (Boolean) session.getAttribute("modoAdicionInventarioInicial") : false;
        
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
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_message").html(datos);
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                        function(r){
                                            location.href = "catEmpleados_Inventario_list.jsp?idEmpleado=<%=idEmpleado%>";
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

            function validar(mode){
            
                if(mode==='agregar'){
                    $('#mode').val("agregarInventario");
                }else if(mode==='eliminar'){                    
                    $('#mode').val("devolverInventario");
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
                     
                    
            }
            
            function recarga(){             
                var busca = $("#idConceptoInventario_flexselect").val();
                location.href='catEmpleados_Inventario_form.jsp?idEmpleado='+<%=idEmpleado%>+'&q='+busca;
            }   
            
            function selectAlmacenFromPopup(idAlmacen, nombreAlmacen){
                
                $("#idAlmacen").val(idAlmacen);
                $('#idAlmacen'+postnameFlexSelect).val(nombreAlmacen);
                
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
                    
                    <% if (modoAdicionInventarioInicial){ %>
                    <div id="info_extra" class="alert_info" style="display: block;">
                        <img src="../../images/inventario_agregar_32.png" alt="Adición a Inventario Inicial" />
                        &nbsp;&nbsp;Modo Adición a Inventario Inicial
                        <br/>
                        Todo lo que agregue a inventario de Empleado en este modo, se agregara tambien al Inventario Inicial existente.
                    </div>
                    <% } %>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <div class="twocolumn <%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?"expose":""%>" id="leito">
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
                                    <p>
                                        <label>*Nombre:</label><br/>
                                        <input maxlength="100" type="text" id="nombreEmpleadoAsignarInventario" name="nombreEmpleadoAsignarInventario" style="width:300px"
                                               value="<%=empleadoDto!=null?empleadoBO.getEmpleado().getNombre()+" "+empleadoBO.getEmpleado().getApellidoPaterno():"" %>"
                                               <%=empleadoDto!=null?"readonly":""%>/>
                                    </p>
                                    <br/> 
                                    <p>
                                        <label>Concepto:</label><br/>
                                        <select size="1" name="idConceptoInventario" id="idConceptoInventario"  style="width:300px;<%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?cssDatosObligatorios:""%>"
                                                                onchange="selectProducto(this.value);"
                                                                class="flexselect">
                                                    <option value="-1"></option>
                                                    <%if(inventario!=null){
                                                        out.print(new ConceptoBO(user.getConn()).getConceptoByIdEspecificoHTMLComboReload(idEmpresa, idConcepto));
                                                    }else{
                                                        out.print(new ConceptoBO(user.getConn()).getConceptosByIdHTMLComboReload(idEmpresa, idConcepto,0,250,buscar, " AND MATERIA_PRIMA=0 "));
                                                    }%>
                                        
                                        </select>
                                        <%if(inventario==null){%>
                                        <button type="button" id="buscar" name="buscar"  value=""  onclick="recarga();"><img src="../../images/Search-32_2.png" alt="Devolver a Inventario" style="cursor: pointer; width: 30px; height: 25px;" /><br>Buscar</button>
                                        <% } %>
                                    </p>   
                                    <br/>
                                    <p>
                                        <label>Almacén:</label> <br/>
                                        <a id="almacen_ref" class="modalbox_iframe" href="../../jsp/includeProductoSelect/almacen_select_form.jsp?mode=100">                                            
                                            <select size="1" id="idAlmacen" name="idAlmacen" onchange="selectProducto();" class="flexselect" style="<%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?cssDatosObligatorios:""%>">
                                            <option value="-1">Selecciona un Almacén</option>                                                    
                                            </select>                                                                                                                                             
                                        </a>
                                    </p>
                                    
                                    <%
                                                if(inventario!=null){
                                                     ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
                                                     conceptoDto = conceptoBO.findConceptobyId(idConcepto);    
                                                        
                                                     ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(user.getConn()); 
                                                     ExistenciaAlmacen almPrincipal = exisAlmBO.getExistenciaProductoAlmacenPrincipal(conceptoDto.getIdEmpresa(), conceptoDto.getIdConcepto());
                                    %>
                                                    
                                                    <p>
                                                    <label>Código:</label><br/>
                                                    <input type='text' id='sku' style='width:300px' name='sku' value='<%=conceptoDto.getIdentificacion()!=null?!conceptoDto.getIdentificacion().equals("")?conceptoDto.getIdentificacion():"Sin código":"Sin código"%>' readonly/>
                                                    </p><br>
                                                    <!--<p>
                                                    <label>Disponibles en Almacén:</label><br/>
                                                    <input type='text' id='diponibles' style='width:300px' name='disponibles' value='<%=almPrincipal!=null?almPrincipal.getExistencia():0%>' />
                                                    </p>
                                                    <br>-->
                                                <%  
                                                }else{%>
                                                    <div id="divDetalle" name="divDetalle"></div>    <!--Div detalle producto -->
                                                <%
                                                }
                                                %>
                                    
                                    
                                    <%if(idConcepto > 0){%>
                                        <p>
                                            <label>Cantidad Actual Asignada:</label><br/>
                                            <input readonly="true" maxlength="100" type="text" id="numArticulosInventarioActual" name="numArticulosInventarioActual" style="width:300px"
                                                   value="<%=inventario!=null?(int)inventario.getCantidad():"0"%>" />
                                        </p>
                                        
                                        
                                    <%}%>
                                    <br/>
                                    <p>
                                        <label>Cantidad a Asignar  /  Devolver:</label><br/>
                                        <input maxlength="100" type="text" id="numArticulosInventarioAsignar" name="numArticulosInventarioAsignar" style="width:300px;<%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?cssDatosObligatorios:""%>"
                                               value="0" />
                                        <br><br>                                    
                                    </p>
                                                            
                                    <br/>
                                                                        
                                     <div id="action_buttons">
                                        <p>                                           
                                            <button class="expose" type="button" id="enviar" value="Agregar" onclick="grabar('agregar');"><img src="../../images/anadir_camion_32.png" alt="Agregar a Inventario" /><br>Agregar</button>
                                            <% if(idConcepto > 0 && !modoAdicionInventarioInicial){ %>  
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            <button type="button" id="enviar" value="Eliminar" onclick="grabar('eliminar');"><img src="../../images/eliminar_camion_32.png" alt="Devolver a Inventario" /><br>Devolver</button>
                                            <br>                                            
                                            <% } %>
                                            <br><br>
                                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>                                            
                                            &nbsp;&nbsp;
                                            <% if (!modoAdicionInventarioInicial){ %>
                                            <input type="button" id="captura_multi_barcode" value="Captura con Códigos de Barra" onclick="location.href='catEmpleados_Inventario_form_barcode.jsp?idEmpleado=<%=idEmpleado%>';"/>
                                            <input type="button" id="consultar_almacenadas" value="Consultar Almacenadas" onclick="location.href='../catEmpAsignacionInventario/catEmpAsignacionInventario_list.jsp?idEmpleado=<%=idEmpleado%>';"/>
                                            <% } %>
                                        </p>
                                    </div>  
                             </div>
                                    
                        </div>
                        <!-- End left column window -->
                        
                    
                    </form>
                    
                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    
                    

                </div>   
                  <jsp:include page="../include/footer.jsp"/>                  
           </div> 
       </div> 
         <script>           
           iniciarFlexSelect();
        </script>
    </body>
</html>
<%}%>