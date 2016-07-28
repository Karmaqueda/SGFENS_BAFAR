<%-- 
    Document   : catMetasVenta_metaEmpleado_form
    Created on : 8/09/2015, 05:27:13 PM
    Author     : HpPyme
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="metasEmpleadosSesion" scope="session" class="com.tsp.sgfens.sesion.MetaEmpleadoSesion"/>
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

        int idEmpresa = user.getUser().getIdEmpresa();
        
        /*
         * Parámetros
         */
        /*int idSgfensBanco = 0;
        try {
            idSgfensBanco = Integer.parseInt(request.getParameter("idSgfensBanco"));
        } catch (NumberFormatException e) {
        }*/

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        String newRandomPass = "";
        
        /* SGBancoBO sgfensBancoBO = new SGBancoBO(user.getConn());
        SgfensBanco sgfensBancosDto = null;        
        if (idSgfensBanco > 0){
            sgfensBancoBO = new SGBancoBO(idSgfensBanco,user.getConn());
            sgfensBancosDto = sgfensBancoBO.getSgfensBanco();           
        }*/
        
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        
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
            
            function iniciarFlexSelect(){                

                $("select.flexselect").flexselect();
            }
            
            
            function cargaPeriodosyEmpl(){   
                                
                $.ajax({
                        type: "POST",
                        url: "metasVenta_ajax.jsp",
                        data: $("#frm_action").serialize()+"&mode=cargaPeriodos&fromFormulario=1",
                        beforeSend: function(objeto){                            
                            $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                            $("#action_buttons").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#MetasEmpleado").html(datos);        
                               $("#ajax_loading").fadeOut("slow");   
                               $("#action_buttons_periodo").fadeOut("slow");
                               iniciarFlexSelect();
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
            
            
            function addMetaEmpleadoSesion(){
                $.ajax({
                        type: "POST",
                        url: "metasVenta_ajax.jsp",
                        data: $("#frm_action").serialize()+"&mode=addMetaEmpleado",
                        beforeSend: function(objeto){                           
                            $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                            $("#action_buttons").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#MetasEmpleado").html(datos);        
                               $("#ajax_loading").fadeOut("slow");
                               iniciarFlexSelect();
                           }else{                               
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               $("#action_buttons").fadeIn("slow");
                               $.scrollTo('#inner',800);
                           }
                        }
                    });
            }
            
            
            function addMetaEmpleadoSesion(){
                $.ajax({
                        type: "POST",
                        url: "metasVenta_ajax.jsp",
                        data: $("#frm_action").serialize()+"&mode=addMetaEmpleado",
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                            $("#action_buttons").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#MetasEmpleado").html(datos);        
                               $("#ajax_loading").fadeOut("slow");
                               iniciarFlexSelect();
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
            
            function activaEditMeta(indexlist,idItem){
                
                $.ajax({
                        type: "POST",
                        url: "metasVenta_ajax.jsp",
                        data: $("#frm_action").serialize()+"&mode=activaEditarMeta&index_lista_metas="+indexlist+"&idUserMeta="+idItem,
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#MetasEmpleado").html(datos);        
                               $("#ajax_loading").fadeOut("slow");
                               iniciarFlexSelect();
                               $("#action_buttons").fadeIn("slow");
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");                               
                               $.scrollTo('#inner',800);
                           }
                        }
                    });
                    
            }
            
            function editMeta(indexlist,idItem){
                
                    $.ajax({
                        type: "POST",
                        url: "metasVenta_ajax.jsp",
                        data: $("#frm_action").serialize()+"&mode=editarMeta&idUserMeta="+idItem+"&index_lista_metas="+indexlist,
                        beforeSend: function(objeto){                         
                            $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#MetasEmpleado").html(datos);        
                               $("#ajax_loading").fadeOut("slow");
                               iniciarFlexSelect();
                           }else{                               
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");                            
                               $.scrollTo('#inner',800);
                           }
                        }
                    });
                
            }

            function deleteMeta(idItem){
               
                    $.ajax({
                        type: "POST",
                        url: "metasVenta_ajax.jsp",
                        data: $("#frm_action").serialize()+"&mode=borrarMeta&idUserMeta="+idItem,
                        beforeSend: function(objeto){                           
                            $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#MetasEmpleado").html(datos);        
                               $("#ajax_loading").fadeOut("slow");
                               iniciarFlexSelect();
                           }else{                              
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");                               
                               $.scrollTo('#inner',800);
                           }
                        }
                    });
                
            }
            
            
            
            function grabar(){
               
                    $.ajax({
                        type: "POST",
                        url: "metasVenta_ajax.jsp",
                        data: $("#frm_action").serialize()+"&mode=guardarMeta",
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#MetasEmpleado").html(datos);        
                               $("#ajax_loading").fadeOut("slow");
                               iniciarFlexSelect();
                               $("#action_buttons_periodo").fadeIn("slow");
                               apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                        function(r){
                                            location.href = "catMetasVenta_metasActivas_List.jsp?pagina="+"<%=paginaActual%>";
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
            
            function recargaLista(){
                
                $.ajax({
                        type: "POST",
                        url: "metasVenta_ajax.jsp",
                        data: $("#frm_action").serialize()+"&mode=recargaLista",
                        beforeSend: function(objeto){                          
                            $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#MetasEmpleado").html(datos);        
                               $("#ajax_loading").fadeOut("slow");
                               iniciarFlexSelect();
                               $("#action_buttons_periodo").fadeIn("slow");
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               $("#action_buttons").fadeIn("slow");
                               $.scrollTo('#inner',800);
                           }
                        }
                    });
                
            };
            
            
            function mostrarCalendario(){
                $( "#fechaIni" ).datepicker({
                        minDate: 0,
                        gotoCurrent: true,
                        changeMonth: true,
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 100);
                            }, 500)}
                });
                
                $( "#fechaFin" ).datepicker({
                        minDate: 0,
                        gotoCurrent: true,
                        changeMonth: true,
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 100);
                            }, 500)}
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
                    <h1>Metas de Venta</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    
                        <div class="onecolumn">
                            <div class="header">
                                <span>
                                    <img src="../../images/proposito.png" alt="icon"/>
                                    Agregar Meta
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                
                                    <input type="hidden" id="idMeta" name="idMeta" value="" />                                    
                                    <p>
                                        <label>*Nombre Meta:</label>
                                        <input maxlength="100" type="text" id="nombreMeta" name="nombreMeta" style="width:300px"
                                               value="<%=metasEmpleadosSesion.getNombre()!=null?metasEmpleadosSesion.getNombre():""%>"/>
                                        &nbsp;&nbsp;&nbsp;
                                        <label>*Tipo Meta:</label>                                        
                                        <select id="tipoMeta" name="tipoMeta" style="width: 90px;" class="flexselect">
                                            <option value="0"></option>
                                            <option value="1" <%=metasEmpleadosSesion.getTipo()==1?"selected":""%> >Monto de venta</option>
                                            <option value="2" <%=metasEmpleadosSesion.getTipo()==2?"selected":""%> >Clientes</option>
                                            <option value="3" <%=metasEmpleadosSesion.getTipo()==3?"selected":""%> >Visitas</option>
                                            <option value="4" <%=metasEmpleadosSesion.getTipo()==4?"selected":""%> >Propsectos</option>
                                        </select>
                                        &nbsp;&nbsp;&nbsp;
                                        <label>*Periodo:</label>
                                        <select id="periodo" name="periodo" style="width: 90px;" class="flexselect">
                                            <option value="0"></option>   daos periodo ponerlo int en qry y daos
                                            <option value="1" <%=metasEmpleadosSesion.getPeriodo()==1?"selected":""%> >Periodo</option>
                                            <option value="2" <%=metasEmpleadosSesion.getPeriodo()==2?"selected":""%> >Diario</option>
                                            <option value="3" <%=metasEmpleadosSesion.getPeriodo()==3?"selected":""%> >Semanal</option>
                                            <option value="4" <%=metasEmpleadosSesion.getPeriodo()==4?"selected":""%> >Quincenal</option>
                                            <option value="5" <%=metasEmpleadosSesion.getPeriodo()==5?"selected":""%> >Mensual</option>
                                            <option value="6" <%=metasEmpleadosSesion.getPeriodo()==6?"selected":""%> >Bimestral</option>
                                            <option value="7" <%=metasEmpleadosSesion.getPeriodo()==7?"selected":""%> >Trimestral</option>
                                            <option value="8" <%=metasEmpleadosSesion.getPeriodo()==8?"selected":""%> >Anual</option>
                                        </select>
                                    </p>
                                    <br/><br/>
                                    <p>
                                        <label>*Fecha Inicio:</label>
                                        <input maxlength="20" type="text" id="fechaIni" name="fechaIni" style="width:300px"
                                               value="<%=metasEmpleadosSesion.getFechaIni()!=null?format.format(metasEmpleadosSesion.getFechaIni()):""%>"/>
                                        &nbsp;&nbsp;&nbsp;                                            
                                        <label>Fecha Fin:</label>
                                        <input maxlength="20" type="text" id="fechaFin" name="fechaFin" style="width:300px"
                                               />
                                        &nbsp;&nbsp;&nbsp;                              
                                        <label>*Duración:</label>
                                        <input maxlength="20" type="text" id="duracion" name="duracion" style="width:300px;"
                                               value="<%=metasEmpleadosSesion.getDuracion()>0?metasEmpleadosSesion.getDuracion():""%>" <%=metasEmpleadosSesion.getListaMetas().size()>0?"readonly":""%> onkeypress="return validateNumber(event);"/>
                                    </p>
                                                                                  
                                    <br/><br/>
                                    
                                    <div id="action_buttons_periodo">
                                        <p>
                                            <a onclick="cargaPeriodosyEmpl();"><img src="../../images/icon_agregar.png" alt="icon"/>&nbsp;Agregar Empleados</a>                                            
                                        </p>
                                    </div>
                                </div>
                        </div>
                        <!-- End left column window -->
                        
                        
                        <div class="onecolumn">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_empleadoNomina.png" alt="icon"/>
                                    Empleados
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">                                     
                                    <div id="MetasEmpleado" name="MetasEmpleado">                                        
                                    </div>
                            </div>
                        </div>
                        <!-- End right column window -->
                        
                        
                    
                    </form>
                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    
                    
                    
                    <div id="action_buttons">
                        <p>
                            <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                        </p>
                    </div>
                    

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>
        <script>
           mostrarCalendario();
           iniciarFlexSelect();
           //recargaLista();
           
        </script>

    </body>    
</html>
<%}%>
