<%-- 
    Document   : almacen_select_form
    Created on : 30/06/2015, 11:51:59 AM
    Author     : HpPyme
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
               
                if (idAlmacen>0){
                    parent.selectAlmacenFromPopup(idAlmacen,nombreAlmacen);
                    parent.$.fancybox.close();
                }
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
                                    <input type="button" name="Seleccionar" id="submit" value="Seleccionar" onclick="selectAlmacen();"/>
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