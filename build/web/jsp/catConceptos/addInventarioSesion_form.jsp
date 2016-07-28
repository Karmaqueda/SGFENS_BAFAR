<%-- 
    Document   : addInventarioSesion_Form
    Created on : 9/02/2015, 12:20:27 PM
    Author     : 578
--%>

<%@page import="com.tsp.sct.dao.dto.ExistenciaAlmacen"%>
<%@page import="com.tsp.sct.bo.ExistenciaAlmacenBO"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.dao.jdbc.DatosUsuarioDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.DatosUsuario"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoDaoImpl"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoInventarioRepartidorDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoInventarioRepartidor"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<!DOCTYPE html>

<%
//Verifica si el cliente tiene acceso a este topico
    if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
        response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
        response.flushBuffer();
    } else {
        
        /*int idConcepto = -1;
        try {
            idConcepto = Integer.parseInt(request.getParameter("idConcepto"));
        } catch (NumberFormatException e) {
        }*/
        
        int buscar_idvendedor = -1;
        int idEmpleado = 0;
        String nombreVendedor = "";
        Empleado empleadoDto= null;
        
        try {          
            
            buscar_idvendedor = Integer.parseInt(request.getParameter("q_idvendedor"));
            empleadoDto = new EmpleadoDaoImpl().findWhereIdUsuariosEquals(buscar_idvendedor)[0];
            idEmpleado =  empleadoDto.getIdEmpleado();
            nombreVendedor = (empleadoDto.getNombre()!=null?empleadoDto.getNombre():"")+ " " + (empleadoDto.getApellidoPaterno()!=null?empleadoDto.getApellidoPaterno():"");
        } catch (NumberFormatException e) {
        }
        
        
        
        HttpSession sesion = request.getSession();
        List<Concepto> carrito = new ArrayList<Concepto>();

        if(session.getAttribute("carrito")!=null){
                carrito =(List<Concepto>)sesion.getAttribute("carrito");
        }
        
        int idEmpresa = user.getUser().getIdEmpresa();
        
        
        
 
 %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../include/keyWordSEO.jsp" />

        <title><jsp:include page="../include/titleApp.jsp" /></title>

        <jsp:include page="../include/skinCSS.jsp" />

        <jsp:include page="../include/jsFunctions.jsp"/>
        
        
        <script type="text/javascript">
            
            var postnameFlexSelect='_flexselect';
            function iniciarFlexSelect(){

                $("select.flexselect").flexselect();
            }
            
            function Save() {
                var par = $(this).parent().parent(); //tr                 
        
                var tdIdConcepto = par.children("td:nth-child(1)", this).text(); 
                var tdCantidadActual = par.children("td:nth-child(6)");
                var tdCantidadActual2 = par.children("td:nth-child(6)", this).text();//obtener valor numerico
                var tdCantidadAsignar = par.children("td:nth-child(7)");                 
                var tdButtons = par.children("td:nth-child(8)");
                var idAlmOrigen = $('#almacen_select_'+tdIdConcepto).val();
                
                var cantidadAgregar =  (tdCantidadAsignar.children("input[type = text]").val());
               
                //----
                    //if(validar(mode)){
                        $.ajax({
                            type: "POST",
                            url: "../catEmpleados/catEmpleados_ajax.jsp",
                            data: {mode:"agregarInventario",idEmpleado:"<%=idEmpleado%>",
                                idConceptoInventario:tdIdConcepto,numArticulosInventarioAsignar:cantidadAgregar,
                                idInventarioAsignado:tdIdConcepto, idAlmacen : idAlmOrigen }, 
                            beforeSend: function(objeto){
                                //$("#action_buttons").fadeOut("slow");
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
                                                //location.href = "catEmpleados_Inventario_list.jsp?idEmpleado=<%=idEmpleado%>";
                                            });
                                   $("#ajax_message").fadeOut("slow");
                                   
                                   removeToInventario(tdIdConcepto);
                                   $("#action_buttons").fadeIn("slow");
                               }else{
                                   $("#ajax_loading").fadeOut("slow");
                                   $("#ajax_message").html(datos);
                                   $("#ajax_message").fadeIn("slow");
                                   //$("#action_buttons").fadeIn("slow");
                                   $.scrollTo('#inner',800);
                                   $("#ajax_message").fadeOut("slow");
                               }
                            }
                        });
                    //}
               // -----

                    
                //si se guarda exitosamente actualizamos grid   
                //Sumamos totales
                var totalActual = Number(cantidadAgregar) + Number(tdCantidadActual2);
                
                tdCantidadActual.html(totalActual);
                tdCantidadAsignar.html("0");
                tdButtons.html ("<img src='../../images/icon_edit.png' class='btnEdit'/> <img src='../../images/icon_delete.png' class='btnDelete'/>"); 
                $(".btnEdit").bind("click", Edit); 
                $(".btnDelete").bind("click", Delete); 
                
            };
            
            
            function Edit(){
                <%if(idEmpleado>0){ %>
                var par = $(this).parent().parent(); //tr           
                
                var tdCantidadAsignar = par.children("td:nth-child(7)"); 
                var tdButtons = par.children("td:nth-child(8)"); 

                tdCantidadAsignar.html("<input type='text' id='txtCantidadAsignar' value='"+tdCantidadAsignar.html()+"'/>"); 
                tdButtons.html("<img src='../../images/disk.png' class='btnSave' style='cursor: pointer;'/>"); 
                
                $(".btnSave").bind("click", Save);
                $(".btnEdit").bind("click", Edit); 
                $(".btnDelete").bind("click", Delete); 
                <%}else{%>
                    apprise('<center><img src=../../images/info.png> <br/>Seleccione un vendedor.</center>',{'animate':true});
                <%}%>

            };
            
            
            function Delete(){ 
                var par = $(this).parent().parent(); //tr 
                var tdIdConcepto = par.children("td:nth-child(1)", this).text();
                removeToInventario(tdIdConcepto);
                
                par.remove();                 
            }; 
            
            
            $(function(){ 
                $(".btnEdit").bind("click", Edit); 
                $(".btnDelete").bind("click", Delete);
                //$("#btnAdd").bind("click", Add); });
            });
            
            
            
            
            function limpiaCarrito(){
                
                
                $.ajax({
                    type: "POST",
                    url: "../catEmpleados/catEmpleados_ajax.jsp",
                    data: {mode:"limpiaCarrito"},
                    beforeSend: function(objeto){
                        //$("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           //$("#ajax_message").html(datos);
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").fadeIn("slow");
                           /*apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                    function(r){
                                        location.href = "catConceptos_list.jsp";
                                    });*/
                           $("#ajax_message").fadeOut("slow");
                           location.href = "catConceptos_list.jsp";
                       }else{
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").html(datos);
                           $("#ajax_message").fadeIn("slow");
                           //$("#action_buttons").fadeIn("slow");
                           $.scrollTo('#inner',800);
                           $("#ajax_message").fadeOut("slow");
                       }
                    }
                });
                              
            }
            
            function selectAlmacenFromPopup(idAlmacen, nombreAlmacen,idProducto){
                
                $('#almacen_select_'+idProducto).val(idAlmacen);
                $('#almacen_select_'+idProducto+postnameFlexSelect).val(nombreAlmacen);
                
            }
            
            
            function removeToInventario(tdIdConcepto){    
               
                
                $.ajax({
                    type: "POST",
                    url: "catConceptos_ajax.jsp",
                    data: {mode: "removeConceptoSesion" ,idConcepto: tdIdConcepto},
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        var indice = datos.indexOf("--EXITO-->") + 10;
                        if(datos.indexOf("--EXITO-->", 0)>0){                           
                           $("#ajax_loading").fadeOut("slow");
                           /*$("#ajax_message").html(datos);
                           $("#ajax_message").fadeIn("slow");                           
                           $.scrollTo('#inner',800);
                           $("#ajax_message").fadeOut("slow");*/
                          //apprise('<center><img src=../../images/info.png> <br/>'+ "Concepto eliminado correctamente." +'</center>',{'animate':true});
                          $("#action_buttons").fadeIn("slow");
                       }else{
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").html(datos);
                           $("#ajax_message").fadeIn("slow");                           
                           $.scrollTo('#inner',800);
                           $("#ajax_message").fadeOut("slow");
                       }
                    }
                });                    
                
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
                <h1>Pretoriano Móvil</h1>

                <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                <div class="onecolumn">


                    <div class="header">
                        <span>
                            <img src="../../images/camion_icono_16.png" alt="icon"/>
                            Asignar Inventario a Vendedor &nbsp;&nbsp;&nbsp;<%=nombreVendedor%>
                        </span>  
                    <div class="switch" style="width:500px">
                            <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                            <tr>                                                
                                                <td>
                                                    <div id="search">                                                                
                                                        <form action="addInventarioSesion_form.jsp" id="search_form" name="search_form" method="post" onchange="$('#search_form').submit()">
                                                            <label>Asignar a Vendedor:</label>
                                                            <select id="q_idvendedor" name="q_idvendedor" class="">
                                                                <option></option>
                                                                <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL,  empleadoDto!=null?empleadoDto.getIdUsuarios():0 ) %> 
                                                                <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_CONDUCTOR_VENDEDOR, empleadoDto!=null?empleadoDto.getIdUsuarios():0 ) %>
                                                            </select>                                                        
                                                        </form>
                                                    </div>
                                                </td>                                                
                                            </tr>
                                    </tbody>
                            </table>
                        </div>                        
                        
                    </div>

                    <br class="clear"/>
                    <div class="content">
                        <form id="form_data" name="form_data" action="" method="post">
                            <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>SKU</th>
                                        <th>Nombre</th>
                                        <th>Descripción</th>  
                                        <th>Almacén</th>
                                        <th>Asignados Actualmente</th>  
                                        <th>Cantidad a Agregar</th>  
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%     
                                    Encrypter encripDesencri = new Encrypter();
                                                                       
                                    
                                    for (Concepto item:carrito){
                                        
                                         EmpleadoInventarioRepartidor[] inventarios = null;
                                         EmpleadoInventarioRepartidor inventario = null; 
                                         
                                         ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(user.getConn()); 
                                         ExistenciaAlmacen almPrincipal = exisAlmBO.getExistenciaProductoAlmacenPrincipal(item.getIdEmpresa(), item.getIdConcepto());
                                         
                                         if(idEmpleado > 0){
                                             try{
                                                inventarios = new EmpleadoInventarioRepartidorDaoImpl(user.getConn()).findByDynamicWhere("ID_EMPLEADO = "+idEmpleado+" AND ID_CONCEPTO = "+item.getIdConcepto() + " AND ID_ESTATUS = 1 ", null);
                                                inventario = inventarios[0];
                                             }catch(Exception e){}
                                         }
                                        
                                        
                                        try{        
                                            
                                            
                                            
                                    %>
                                    <tr>                                            
                                        <td><%=item.getIdConcepto() %></td>
                                        <td><%=item.getIdentificacion() %></td>
                                        <td><%=encripDesencri.decodeString(item.getNombre()) %></td>
                                        <td><%=item.getDescripcion()%></td>
                                        <td><a id="almacen_ref" class="modalbox_iframe" href="../../jsp/includeProductoSelect/almacen_select_empleado_form.jsp?idProducto=<%=item.getIdConcepto()%>" > 
                                                <select id="almacen_select_<%=item.getIdConcepto()%>" name="almacen_select_<%=item.getIdConcepto()%>" class="flexselect" 
                                                        style="width: 90px;">                                                        
                                                    <option value="-1"></option>
                                                </select>                                                                                                                                              
                                                </a>  
                                        </td>                                        
                                        <td><%=inventario!=null?inventario.getCantidad()<0?0:inventario.getCantidad():0%></td>
                                        <td>0</td>
                                        
                                        <td>
                                            <%//if(idEmpleado>0){ %>
                                            <img src="../../images/icon_edit.png" class="btnEdit" style="cursor: pointer;"/>
                                            <img src='../../images/icon_delete.png' class='btnDelete' style="cursor: pointer;"/>
                                            <%//} %>
                                        </td>  
                                        
                                    </tr>
                                    
                                    <% 
                                            }catch(Exception ex){
                                                ex.printStackTrace();
                                            }
    
                                        } %>
                                </tbody>
                            </table>
                        </form>              
                                
                        <br/><br/>

                        <div id="action_buttons">
                            <p>                                
                                <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                <input type="button" id="enviar" value="Salir" onclick="limpiaCarrito();"/>
                            </p>
                        </div>

                    </div>
                </div>
            </div> 
            <jsp:include page="../include/footer.jsp"/>                    
        </div>    
        <script>            
           iniciarFlexSelect();
        </script>
    </body>
</html>
<%}%>
