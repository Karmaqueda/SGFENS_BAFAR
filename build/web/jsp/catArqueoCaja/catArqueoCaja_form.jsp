<%-- 
    Document   : catArqueoCaja_form
    Created on : 19/09/2014, 05:55:41 PM
    Author     : 578
--%>


<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.bo.SGCobranzaMetodoPagoBO"%>
<%@page import="com.tsp.sct.bo.EmpleadoArqueoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoArqueoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoArqueo"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el cliente tiene acceso a este topico
    if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
        response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
        response.flushBuffer();
    } else {
        
        int paginaActual = 1;
        try{
            paginaActual = Integer.parseInt(request.getParameter("pagina"));
        }catch(Exception e){}
        
        Date fecha=new Date(); 
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
    

        int idEmpresa = user.getUser().getIdEmpresa();
        
        /*
         * Parámetros
         */
        int idEmpleado = 0;
        try {
            idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
        } catch (NumberFormatException e) {
        }
        
        int idArqueo = -1;
        try {
            idArqueo = Integer.parseInt(request.getParameter("idArqueo"));
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
        
        EmpleadoArqueo[] arqueos = null;
        EmpleadoArqueo arqueo = null;
        EmpleadoArqueo arqueoDto = null;
        
        
        if(idArqueo > 0){
            arqueos = new EmpleadoArqueoDaoImpl(user.getConn()).findByDynamicWhere("ID_EMPLEADO = "+idEmpleado+" AND ID_ARQUEO = "+idArqueo, null);
            arqueo = arqueos[0];
            
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
           
            
          </script>        
        
        <script type="text/javascript">
            
            function grabar(){
                
                    $.ajax({
                        type: "POST",
                        url: "catArqueoCaja_ajax.jsp",
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
                                            location.href = "catArqueoCajaDetalle_list.jsp?idEmpleado=<%=idEmpleado%>&pagina="+"<%=paginaActual%>";
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

                        
            
              
            
            
            
        </script>
    </head>
    <body>
        
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">
        
                <div class="inner">
                    <h1>Arqueo de Caja</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <div class="twocolumn">
                    <form action="" method="post" id="frm_action">
                        
                        <div class="column_left" >
                            <div class="header">
                                <span>
                                    <img src="../../images/coins_icon_16.png" alt="icon"/>
                                    <% if(empleadoDto!=null){%>
                                    Empleado <%=empleadoDto!=null?empleadoDto.getNombre() + empleadoDto.getApellidoPaterno() :"" %>
                                    <%}else{%>
                                    Empleado
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idEmpleado" name="idEmpleado" value="<%=empleadoDto!=null?empleadoDto.getIdEmpleado():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <input type="hidden" id="idArqueo" name="idArqueo" value="<%=arqueo!=null?arqueo.getIdArqueo():null%>" />                                    
                                    <br/>                                                               
                                        
                                        
                                        <p>
                                            <label>Fecha:</label><br/>
                                            <input readonly="true" maxlength="100" type="text" id="fecha" name="fecha" style="width:300px"
                                                   value="<%=arqueo!=null?arqueo.getFecha():sdf.format(fecha)%>" />
                                        </p>
                                        <br>
                                        
                                                                        
                                   
                                        <p>
                                            <label>*Monto:</label><br/>
                                            <input  maxlength="100" type="text" id="monto" name="monto" style="width:300px"
                                                   value="<%=arqueo!=null?arqueo.getMonto():"0"%>" />
                                        </p>  
                                        <br>
                                        <p>
                                            <label>Referencia:</label><br/>
                                            <input maxlength="100" type="text" id="referencia" name="referencia" style="width:300px"
                                                   value="<%=arqueo!=null?arqueo.getReferencia()!=null?arqueo.getReferencia():"":""%>" />
                                        </p>
                                        
                                        <br>
                                        <p>
                                        <label>Método Pago:</label><br/>
                                        <select size="1" id="idMetodoPago" name="idMetodoPago">
                                            <option value="-1">Selecciona...</option>
                                            <%
                                                if(arqueo!=null){
                                                    out.print(new SGCobranzaMetodoPagoBO(user.getConn()).getMetodoPagoHTMLCombo(arqueo.getIdCobranzaMetodoPago()));
                                                }else{
                                                    out.print(new SGCobranzaMetodoPagoBO(user.getConn()).getTodoMetodoPagoByHTMLCombo());
                                                }
                                            %>
                                               
                                        </select>
                                        </p> 
                                        
                                    
                                    
                                                            
                                    <br/>
                                                                        
                                     <div id="action_buttons">
                                        <p>                                           
                                            <button type="button" id="enviar" value="Agregar" onclick="grabar();"><img src="../../images/agregar_dinero_32.png" alt="Guardar" /><br>Guardar</button> 
                                            <!--<span  id="enviar" value="Agregar" onclick="grabar();"><img src="../../images/agregar_dinero_32.png" alt="Guardar" /><br>Guardar</span>  -->                                          
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
                                    
           </div> 
       </div> 
    </body>
</html>
<%}%>