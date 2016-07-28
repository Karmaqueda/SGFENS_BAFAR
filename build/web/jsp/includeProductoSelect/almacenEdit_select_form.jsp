<%-- 
    Document   : almacenEdit_select_form
    Created on : 27/04/2016, 01:46:19 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.AlmacenBO"%>
<%@page import="com.tsp.sct.bo.ExistenciaAlmacenBO"%>
<%@page import="com.tsp.sct.dao.dto.Almacen"%>
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
    int idProducto = -1;
    
    String mode= request.getParameter("mode") != null ? request.getParameter("mode") : "";
    
    try{
        idProducto = Integer.parseInt(request.getParameter("idProducto"));
    }catch(NumberFormatException ex){}
    
    String identificadorIdAlmacen = "";
    identificadorIdAlmacen= request.getParameter("identificadorIdAlmacen") != null ? request.getParameter("identificadorIdAlmacen") : "";
    
    String identificadorNombreAlmacen = "";
    identificadorNombreAlmacen= request.getParameter("identificadorNombreAlmacen") != null ? request.getParameter("identificadorNombreAlmacen") : "";
    
    int index_lista_producto=-1;    
    try{ index_lista_producto = Integer.parseInt(request.getParameter("index_lista_producto")); }catch(Exception e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    Almacen[] almacenDtos = new Almacen[0] ;
    AlmacenBO almBO =   new AlmacenBO(user.getConn());
    ExistenciaAlmacenBO almacenExistenciaBO = new ExistenciaAlmacenBO(user.getConn());
    
    String qry = " AND ID_ALMACEN IN (SELECT ID_ALMACEN FROM existencia_almacen WHERE ID_CONCEPTO = " + idProducto //+ " AND EXISTENCIA >0"
            + " AND ESTATUS = 1 ) AND ID_ESTATUS = 1 ";
    almacenDtos = almBO.findAlmacens(-1, idEmpresa, -1, -1, qry);
    
%>
<jsp:include page="../../jsp/include/registraNavegacionBitacora.jsp"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Seleccionar Almacén</title>
        
        <jsp:include page="../include/skinCSS.jsp" />
        <jsp:include page="../../jsp/include/jsFunctions.jsp"/>
        <script language="javascript">
            
            function selectAlmacen(){
                var idAlmacen = $('#select_almacen').val();
                var nombreAlmacen = $("#select_almacen option[value='"+idAlmacen+"']").text();
                //alert(idAlmacen+","+nombreAlmacen);
                //variables auxilidares para ver a que producto se le modificara el almacen.
                var identificadorIdAlmacen = $('#identificadorIdAlmacen').val();
                var identificadorNombreAlmacen = $('#identificadorNombreAlmacen').val();
                var index_lista_producto = $('#index_lista_producto').val();
                
                window.parent.actualizarValoresAlmacen(idAlmacen,nombreAlmacen, identificadorIdAlmacen, identificadorNombreAlmacen);
               
                //modificamos el valor del almacen del product en sesion del pedido:
                modificarProductoAlmacenSalida(idAlmacen, index_lista_producto);                
               
                if (idAlmacen>0){
                 //   parent.selectAlmacenFromPopup(idAlmacen,nombreAlmacen);
                    parent.$.fancybox.close();
                }
            }
            
            function modificarProductoAlmacenSalida(idAlmacen, index_lista_producto){
                $.ajax({
                    type: "POST",
                    url: "almacenEdit_select_ajax.jsp",
                    data: { mode: '1', index_lista_producto: index_lista_producto, idAlmacen : idAlmacen},
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
                <% if (idProducto>0) {%>
                <div>
                    <form action="" method="post" id="frm_action">
                      
                        <fieldset>                                                        
                            <br class="clear"/>
                            <h3>Seleccione el almacen de donde saldra el producto.</h3>
                            <br>
                            <input type="hidden" name="identificadorIdAlmacen" id="identificadorIdAlmacen" readonly value="<%=identificadorIdAlmacen%>"/>
                            <input type="hidden" name="identificadorNombreAlmacen" id="identificadorNombreAlmacen" readonly value="<%=identificadorNombreAlmacen%>"/>
                            <input type="hidden" name="index_lista_producto" id="index_lista_producto" readonly value="<%=index_lista_producto%>"/>
                            <p> 
                                <label>Almacenes</label>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;                            
                                <label>Existencia</label><br/>      
                                
                                <select id="select_almacen" name="select_almacen" style="width: 300px;" size="5">
                                    <%
                                        for(Almacen item:almacenDtos){
                                            %>
                                            <option value="<%=item.getIdAlmacen()%>"><%=item.getNombre()%></option>
                                            <%
                                        }
                                    %>
                                </select>
                                <select id="existencia" name="existencia" style="width: 100px;" size="5" readonly>
                                    <%
                                        for(Almacen item:almacenDtos){
                                            double exist = 0;
                                            try{
                                                exist = almacenExistenciaBO.getExistenciaProductoAlmacenDouble(item.getIdAlmacen(), idProducto);                                                
                                            }catch(Exception e){}
                                            
                                            %>
                                            <option value="<%=item.getIdAlmacen()%>"><%=exist%></option>
                                            <%
                                        }
                                    %>
                                </select>
                            </p>

                            <br class="clear"/>
                            <dl class="submit">
                                <div id="action_buttons">
                                    <input type="button" name="Seleccionar" id="submit" value="Seleccionar y Actualizar" onclick="selectAlmacen();"/>
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
                  <script>
                    apprise('<center><img src=../../images/info.png> <br/>'+ "Seleccione primero un producto." +'</center>',{'animate':true});
                  </script>
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