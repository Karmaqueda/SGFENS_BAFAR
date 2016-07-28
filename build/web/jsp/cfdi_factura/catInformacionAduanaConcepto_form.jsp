<%-- 
    Document   : catInformacionAduanaConcepto_form
    Created on : 10/02/2016, 01:16:55 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.bo.AduanaBO"%>
<%@page import="com.tsp.sgfens.sesion.ComprobanteFiscalSesion"%>
<%@page import="com.tsp.sct.dao.dto.Aduana"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="cfdiSesion" scope="session" class="com.tsp.sgfens.sesion.ComprobanteFiscalSesion"/>

<%
//Verifica si el cliente tiene acceso a este topico
    if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
        response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
        response.flushBuffer();
    } else {

        int idEmpresa = user.getUser().getIdEmpresa();
        
        /*
         * Parámetros
         */
        int index_lista_producto = 0;
        try {
            index_lista_producto = Integer.parseInt(request.getParameter("index_lista_producto"));
        } catch (NumberFormatException e) {
        }
        
        
        int idInfoAduana = 0;
        try {
            idInfoAduana = Integer.parseInt(request.getParameter("idInfoAduana"));
        } catch (NumberFormatException e) {
        }

        List<Aduana> aduanas = new ArrayList<Aduana>();
    
        if (cfdiSesion==null){
            cfdiSesion = new ComprobanteFiscalSesion();
        }
        String msgError="";
        if (msgError.equals("")){
            try{
                aduanas = cfdiSesion.getListaProducto().get(index_lista_producto).getAduanas();
            }catch(Exception ex){
                msgError+="El producto especificado no se encontro en el listado en sesion. Intente de nuevo.";
            }
            //request.getSession().setAttribute("cfdiSesion", cfdiSesion);

        }
        
        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        
        AduanaBO aduanaBO = new AduanaBO(user.getConn());
        Aduana aduanasDto = null;
        if (idInfoAduana > 0){
            aduanaBO = new AduanaBO(idInfoAduana, user.getConn());
            aduanasDto = aduanaBO.getAduana();
        }
        String parameter1 = "idConcepto";
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
                        url: "catInformacionAduanaConcepto_ajax.jsp",
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
                                            location.href = "catInformacionAduanaConcepto_list.jsp?index_lista_producto=<%=index_lista_producto%>";
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
        </script>
        <script type="text/javascript">
            
            function mostrarCalendario(){
                $( "#fechaAduana" ).datepicker({
                        //minDate: +1,
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
    <body class="nobg">
                <div class="inner">
                    <h1>Catálogo</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                        <div class="onecolumn">
                        <div class="column_left" style="width: 543px;">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_bandera.png" alt="icon"/>                                    
                                    Información Aduana
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="index_lista_producto" name="index_lista_producto" value="<%=index_lista_producto%>" />
                                    <input type="hidden" id="idInfoAduana" name="idInfoAduana" value="<%=idInfoAduana%>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=aduanasDto!=null?"2":"1"%>" />
                                    <p>
                                        <label>*Nombre:</label><br/>
                                        <input maxlength="100" type="text" id="nombreAduana" name="nombreAduana" style="width:180px"
                                               value="<%=aduanasDto!=null?aduanasDto.getAduana():"" %>"
                                               <%=aduanasDto!=null?"readonly":""%>/>
                                    </p>
                                    <br/>                                    
                                    
                                    <p>
                                        <label>*Número:</label><br/>
                                        <input maxlength="100" type="text" id="numeroAduana" name="numeroAduana" style="width:180px"
                                               value="<%=aduanasDto!=null?aduanasDto.getNumDocumento():"" %>"
                                               <%=aduanasDto!=null?"readonly":""%>/>
                                    </p>
                                    <br/>
                                    
                                    <p>
                                        <label>*Fecha:</label><br/>                                        
                                        <input type="text" name="fechaAduana" id="fechaAduana" readonly
                                            value="<%= aduanasDto!=null?DateManage.formatDateToNormal(aduanasDto.getFechaExpedicion()):"" %>"
                                            style="width: 80px;"/>
                                    </p>
                                                            
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
        <script>
            mostrarCalendario();
        </script>
    </body>
</html>
<%}%>