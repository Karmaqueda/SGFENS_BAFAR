<%-- 
    Document   : catCrFormularioCapturaInfo_form
    Created on : 27/06/2016, 05:52:21 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.cr.CrUtilCalculos"%>
<%@page import="com.tsp.sct.dao.jdbc.CrFrmEventoSolicitudDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrFrmEventoSolicitud"%>
<%@page import="com.tsp.sct.dao.jdbc.CrProductoCreditoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrProductoCredito"%>
<%@page import="com.tsp.sct.util.FormatUtil"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.dao.dto.CrFormularioRespuesta"%>
<%@page import="com.tsp.sct.dao.jdbc.CrFormularioRespuestaDaoImpl"%>
<%@page import="com.tsp.sct.bo.CrFormularioRespuestaBO"%>
<%@page import="com.tsp.sct.bo.CrFormularioCampoBO"%>
<%@page import="com.tsp.sct.dao.dto.CrFormularioCampo"%>
<%@page import="com.tsp.sct.bo.CrGrupoFormularioBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.CrFormularioBO"%>
<%@page import="com.tsp.sct.dao.jdbc.CrFormularioDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrFormulario"%>
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

    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
     * Parámetros
     */
    int idGrupoFormulario = 0;
    try {
        idGrupoFormulario = Integer.parseInt(request.getParameter("idGrupoFormulario"));
    } catch (NumberFormatException e) {}
    
    int idCrFormularioEvento = 0;
    try {
        idCrFormularioEvento = Integer.parseInt(request.getParameter("idCrFormularioEvento"));
    } catch (NumberFormatException e) {}
    
    int id_producto_solicitado = 0;
    try {
        id_producto_solicitado = Integer.parseInt(request.getParameter("id_producto_solicitado"));
    } catch (NumberFormatException e) {}
    
    int formularioVerificacion = 0;
    try {
        formularioVerificacion = Integer.parseInt(request.getParameter("formularioVerificacion"));
    } catch (NumberFormatException e) {}
    
    int soloConsulta = 0;
    try {
        soloConsulta = Integer.parseInt(request.getParameter("soloConsulta"));
    } catch (NumberFormatException e) {}
    
    int soloCamposRevision = 0;//variable para saber que un promotor va a revisar y hacer correcciones de los campos de la solicitud que se envio a revision
    try {
        soloCamposRevision = Integer.parseInt(request.getParameter("soloCamposRevision"));
    } catch (NumberFormatException e) {}
    
    CrProductoCredito crProductoCreditoDto = null;
    CrProductoCredito crProductoCreditoPadreDto = null;
    CrProductoCreditoDaoImpl crProductoCreditoDaoImpl = new CrProductoCreditoDaoImpl(user.getConn());

    try{
        crProductoCreditoDto = crProductoCreditoDaoImpl.findByPrimaryKey(id_producto_solicitado);
    }catch(Exception e){}

    if(crProductoCreditoDto != null){
        crProductoCreditoPadreDto = crProductoCreditoDaoImpl.findByPrimaryKey(crProductoCreditoDto.getIdProductoCreditoPadre()); 
        idGrupoFormulario = crProductoCreditoPadreDto.getIdGrupoFormularioSolic();
    }
    
    if(idCrFormularioEvento > 0){ //es una edición de evento, cargamos el producto que fue seleccionado
        CrFrmEventoSolicitud crFrmEventoSolicitud = null;
        try{
            System.out.println("+++++++++++++++++++++++++++++++++++++++++ a");        
            crFrmEventoSolicitud = new CrFrmEventoSolicitudDaoImpl(user.getConn()).findByDynamicWhere(" id_formulario_evento = " + idCrFormularioEvento + " ORDER BY fecha_hr_creacion DESC", null)[0];
            id_producto_solicitado = crFrmEventoSolicitud.getIdProductoCredito();
            System.out.println("+++++++++++++++++++++++++++++++++++++++++ B " +id_producto_solicitado);
            try{
                crProductoCreditoDto = crProductoCreditoDaoImpl.findByPrimaryKey(id_producto_solicitado);
            }catch(Exception e){}
        }catch(Exception e){e.printStackTrace();}    
         
    }

    double cuotaMensual = 0;
    try{
        if(crProductoCreditoDto != null){
            cuotaMensual = CrUtilCalculos.calcCuotaMensual(crProductoCreditoDto.getMonto(), crProductoCreditoDto.getTasaInteresAnual(), (int)crProductoCreditoDto.getPlazo(), 1);
        }
    }catch(Exception e){}
    
    /*
     *   0/"" = nuevo
     *   1 = editar/consultar
     *   2 = eliminar  
     */
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";

    //obtengo todos los formularios del grupo:
    CrFormularioBO crFormularioBO = new CrFormularioBO(user.getConn());
    CrFormulario[] crFormularioDto = new CrFormulario[0];
    if (idGrupoFormulario > 0){
        //crFormularioDto = new CrFormularioDaoImpl(user.getConn()).findByDynamicWhere(" id_grupo_formulario = " + idGrupoFormulario + " AND id_estatus = 1 " , null);
        crFormularioBO.setOrderBy("ORDER BY orden_grupo ASC");
        crFormularioDto = crFormularioBO.findCrFormularios(-1, idEmpresa, 0, 0, " AND id_grupo_formulario = " + idGrupoFormulario + " AND id_estatus = 1");
    }
    
    Empresa empresaDto = null;        
    empresaDto = new EmpresaBO(idEmpresa,user.getConn()).getEmpresa();
    Configuration appConfig = new Configuration();  
    String rfcEmpresaMatriz = empresaDto.getRfc();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../include/keyWordSEO.jsp" />

        <title><jsp:include page="../include/titleApp.jsp" /></title>

        <jsp:include page="../include/skinCSS.jsp" />

        <jsp:include page="../include/jsFunctions2.jsp"/>
        
        <script type="text/javascript">
            
            function grabar(){
                if(validar()){
                    $.ajax({
                        type: "POST",
                        url: "catCrFormularioCapturaInfo_ajax.jsp",
                        data: $("#frm_action").serialize(),
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                            $.scrollTo('.inner',800);
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_message").html(datos);
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                        function(r){
                                            location.href = "../catCrFormularioEvento/catCrFormularioEvento_list.jsp?pagina="+"<%=paginaActual%>";
                                        });
                           }else if(datos.indexOf("--ERROR_NO_APROBADO-->", 0)>0){
                               $("#ajax_message").html(datos);
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                        function(r){
                                            location.href = "../catCrFormularioEvento/catCrFormularioEvento_list.jsp?pagina="+"<%=paginaActual%>";
                                        });
                           }else if(datos.indexOf("--EXITO_OTRO_PRODUCTO-->", 0)>0){
                               $("#ajax_message").html(datos);
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                        function(r){
                                            location.href = "../catCrFormularioEvento/catCrFormularioEvento_list.jsp?pagina="+"<%=paginaActual%>";
                                        });
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               $("#action_buttons").fadeIn("slow");
                               $.scrollTo('.inner',800);
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
            
            function recuperarNombreArchivoImagen(nombreImagen, IdFormularioImagen){
                
                $('#id_formulario_campo_valor_'+IdFormularioImagen).val('' + nombreImagen);
            }
            
            
            function grabarEnviar(){
                $('#tipoTratoSolicitud').val('guardarEnviar');
                grabar();                
            }
            
            function grabarYEnviar(){
                $('#tipoTratoSolicitud').val('enviarMesaDeControl');
                grabar();                
            }
            
            function grabarBorrador(){
                $('#tipoTratoSolicitud').val('guardarSoloBorrador');
                grabar();                
            }
            
            function grabarMesaControl(){
                $('#tipoTratoSolicitud').val('guardarVerificacionMesaControl');
                grabar();
            }
            
            function grabarMesaControlRechazo(){
                $('#tipoTratoSolicitud').val('guardarRechazoMesaControl');
                grabar();
            }
            
            function grabarActualizacion(){
                $('#tipoTratoSolicitud').val('guardarCorreccionSolicitud');
                grabar();
            }
            
            function validaCheckBoxRevisar(IdFormularioImagen){                
                if ($('#revisar_'+IdFormularioImagen).prop('checked')) {                    
			document.getElementById("revisar_comen_"+IdFormularioImagen).style.display="block"; //para setear una propiedad de css
                }
                else {                    
                    document.getElementById("revisar_comen_"+IdFormularioImagen).style.display="none"; //para setear una propiedad de css
                }
            }
            
        </script>
                                        
        <script type='text/javascript'> 
            $(document).ready(function() {
                $(".contenido_tab").hide(); //Ocultar capas
                $("ul.tabs li:first").addClass("activa").show(); //Activar primera pestaña
                $(".contenido_tab:first").show(); //Mostrar contenido primera pestaña

                // Sucesos al hacer click en una pestaña
                $("ul.tabs li").click(function() {
                    $("ul.tabs li").removeClass("activa"); //Borrar todas las clases "activa"
                    $(this).addClass("activa"); //Añadir clase "activa" a la pestaña seleccionada
                    $(".contenido_tab").hide(); //Ocultar todo el contenido de la pestaña
                    var activatab = $(this).find("a").attr("href"); //Leer el valor de href para identificar la pestaña activa 
                    $(activatab).fadeIn(); //Visibilidad con efecto fade del contenido activo
                    return false;
                });
            });
        </script>
        <style type="text/css">
            /* CSS Tabs jQuery */
            .contenedor_tab{float:left;clear:both;width:1050px;padding:0px;margin:0 auto;display:block;background:#ccc;border:1px solid #333;-moz-border-radius:0 0 7px 7px;-webkit-border-radius:0 0 7pc 7px;border-radius: 0 0 7px 7px;}
            ul.tabs{float:left;margin:0;padding:0;list-style:none;height:32px;width:1050px;margin-top:-7px;}
            ul.tabs li{float:left;margin:0;padding:0;height:31px;line-height:31px;border:1px solid #333;margin-bottom:-1px;background:#333;overflow:hidden;position:relative;border:1px solid #333;-moz-border-radius:7px 7px 0 0;-webkit-border-radius:7px 7px 0 0;border-radius: 7px 7px 0 0;}
            ul.tabs li a{text-decoration:none;color:#fff;display:block;font-size:13px;padding:0 20px;border:1px solid #fff;outline:none;-moz-border-radius:7px 7px 0 0;-webkit-border-radius:7px 7px 0 0;border-radius: 7px 7px 0 0;}
            ul.tabs li a:hover{background:#666;}
            ul.tabs li.activa,ul.tabs li.activa a,ul.tabs li.activa a:hover {color:#333;font-weight:bold;background:#ccc;border-bottom:1px solid #ccc;}
            .contenido_tab{padding:10px;font-size:1.2em;width:1030px;}
            .contenido_tab img{margin:0 20px 20px 0;border:1px solid #ddd;padding:5px}
            
            .checkVerificador {
                color:#4242AE;
            }
        </style>
    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Datos a llenar</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                        <div>
                        <!--<div class="column_left" style="width: 99%;">-->
                            <!--<div class="header">
                                <span>
                                    <img src="../../images/icon_crFormulario.png" alt="icon"/>
                                    <% if(crFormularioDto!=null){%>
                                    Editar Formulario ID <%=crFormularioDto!=null?crFormularioDto.length:"" %>
                                    <%}else{%>
                                    Nuevo Formulario
                                    <%}%>
                                </span>
                            </div>-->
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idCrGrupoFormulario" name="idCrGrupoFormulario" value="<%=idGrupoFormulario%>" />
                                    <input type="hidden" id="idCrFormularioEvento" name="idCrFormularioEvento" value="<%=idCrFormularioEvento%>" />
                                    <input type="hidden" id="id_producto_solicitado" name="id_producto_solicitado" value="<%=id_producto_solicitado%>" />
                                    <input type="hidden" id="tipoTratoSolicitud" name="tipoTratoSolicitud" value="" />
                                    <input type="hidden" id="soloCamposRevision" name="soloCamposRevision" value="<%=soloCamposRevision%>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    
                                    <ul class="tabs">
                                        <%for(CrFormulario formularios : crFormularioDto){%>
                                            <li><a href="#tab<%=formularios.getIdFormulario()%>"><%=formularios.getNombre()%></a></li>
                                        <%}%>
                                    </ul>
                                    
                                    <div class="contenedor_tab">
                                        <br/><br/>
                                        <%
                                        double montoTotal = 0; 
                                        double primerTotal = 0;
                                        double segundoTotal = 0;
                                        double tercerTotal = 0;
                                        int contadorTotales= 0;//variable para saber cuantos totales hay, asi como para independizar cada total
                                        double montoMensualConyugue = 0;
                                        double montoMensualSueldo = 0;
                                        double montoMensualFamiliar = 0;
                                        
                                        String queryModificado = "";
                                        if(soloCamposRevision == 1){
                                            queryModificado = " AND id_formulario_campo IN (SELECT id_formulario_campo FROM cr_formulario_respuesta WHERE revisar = 1) ";
                                        }
                                                                        
                                        for(CrFormulario formularios : crFormularioDto){
                                                                        montoTotal=0;
                                        %>
                                        <div id="tab<%=formularios.getIdFormulario()%>" class="contenido_tab">                                    
                                            <%
                                                CrFormularioCampoBO crFormularioCampoBO = new CrFormularioCampoBO(user.getConn());
                                                crFormularioCampoBO.setOrderBy(" ORDER BY orden_formulario ASC ");
                                                CrFormularioCampo[] crFormularioCampo = crFormularioCampoBO.findCrFormularioCampos(0, idEmpresa, 0, 0, (" AND id_formulario = " + formularios.getIdFormulario() + " AND id_estatus = 1 " + queryModificado ));
                                                for(CrFormularioCampo formulario : crFormularioCampo){
                                                    CrFormularioRespuesta crFormularioRespuesta = null;
                                                    try{
                                                        crFormularioRespuesta = new CrFormularioRespuestaDaoImpl(user.getConn()).findByDynamicWhere(" id_formulario_evento = " + idCrFormularioEvento + " AND id_formulario_campo = " + formulario.getIdFormularioCampo(), null)[0];
                                                    }catch(Exception e){}
                                                    
                                                    if(crFormularioRespuesta!=null){
                                                        if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("LAB_SUELDO_NETO") > -1){
                                                            try{montoMensualSueldo = Double.parseDouble(crFormularioRespuesta.getValor());}catch(Exception e){}
                                                        }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("CNY_LAB_SUELDO") > -1){
                                                            try{montoMensualConyugue = Double.parseDouble(crFormularioRespuesta.getValor());}catch(Exception e){}
                                                        }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("OTIN_UTILIDAD_MENS") > -1){
                                                            try{montoMensualFamiliar = Double.parseDouble(crFormularioRespuesta.getValor());}catch(Exception e){}
                                                        }
                                                    }

                                        %>
                                                    <%if(formulario.getIdTipoCampo() == 1){
                                                        
                                                                        //if(formularios.getNombre().trim().toUpperCase().indexOf("BALANCE") > -1){//realizar las sumas de los montos cuando es un balance                                                                    
                                                                            try{montoTotal += (crFormularioRespuesta!=null?(Double.parseDouble(crFormularioRespuesta.getValor())):0.00);}catch(Exception e){}
                                                                        //}
                                                                        if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("GFAM_INGRESOS_MENOS_EGRESOS") > -1){%>
                                                                            <p>
                                                                                <label><%=(formulario.getIsRequerido()==1?"*":"")%> <%=formulario.getEtiqueta()%>:</label><br/>
                                                                                <input type="hidden" id="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" value="<%=formulario.getIdFormularioCampo()%>" />
                                                                                <input maxlength="1000" type="text" id="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" style="width:200px"
                                                                                       value="<%=FormatUtil.doubleToStringPuntoComas(((montoMensualSueldo + montoMensualConyugue + montoMensualFamiliar) - primerTotal))%>" readonly/>
                                                                            </p>
                                                                            <br/>
                                                                        <%}else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("GFAM_TOTAL_INGRESOS") > -1){%>
                                                                            <p>
                                                                                <label><%=(formulario.getIsRequerido()==1?"*":"")%> <%=formulario.getEtiqueta()%>:</label><br/>
                                                                                <input type="hidden" id="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" value="<%=formulario.getIdFormularioCampo()%>" />
                                                                                <input maxlength="1000" type="text" id="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" style="width:200px"
                                                                                       value="<%=FormatUtil.doubleToStringPuntoComas((montoMensualSueldo + montoMensualConyugue + montoMensualFamiliar))%>" readonly/>
                                                                            </p>
                                                                            <br/>
                                                                        <%}else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("GFAM_TOTAL_EGRESOS") > -1){%>
                                                                            <p>
                                                                                <label><%=(formulario.getIsRequerido()==1?"*":"")%> <%=formulario.getEtiqueta()%>:</label><br/>
                                                                                <input type="hidden" id="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" value="<%=formulario.getIdFormularioCampo()%>" />
                                                                                <input maxlength="1000" type="text" id="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" style="width:200px"
                                                                                       value="<%=FormatUtil.doubleToStringPuntoComas(montoTotal)%>" readonly/>
                                                                            </p>
                                                                            <br/> 
                                                                        <% System.out.println("-+-+-++-+-+-+-+-+-+- " + montoTotal);
                                                                            if(contadorTotales == 0){
                                                                                primerTotal = montoTotal;
                                                                            }else if(contadorTotales == 1){
                                                                                segundoTotal = (montoMensualSueldo + montoMensualConyugue + montoMensualFamiliar);                                                                                
                                                                            }else if(contadorTotales == 2){
                                                                                tercerTotal = montoTotal;
                                                                            }
                                                                            contadorTotales++;
                                                                            montoTotal=0;//reiniciamos el monto del total, para la sumatoria de los siguientes registros                                                                            
                                                                        }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("ING_TITULAR") > -1){%>
                                                                            <p>
                                                                                <label><%=(formulario.getIsRequerido()==1?"*":"")%> <%=formulario.getEtiqueta()%>:</label><br/>
                                                                                <input type="hidden" id="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" value="<%=formulario.getIdFormularioCampo()%>" />
                                                                                <input maxlength="1000" type="text" id="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" style="width:200px"
                                                                                       value="<%=FormatUtil.doubleToStringPuntoComas((montoMensualSueldo))%>" readonly/>
                                                                            </p>
                                                                            <br/>
                                                                        <%}else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("ING_CONYUGUE") > -1){%>
                                                                            <p>
                                                                                <label><%=(formulario.getIsRequerido()==1?"*":"")%> <%=formulario.getEtiqueta()%>:</label><br/>
                                                                                <input type="hidden" id="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" value="<%=formulario.getIdFormularioCampo()%>" />
                                                                                <input maxlength="1000" type="text" id="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" style="width:200px"
                                                                                       value="<%=FormatUtil.doubleToStringPuntoComas((montoMensualConyugue))%>" readonly/>
                                                                            </p>
                                                                            <br/>
                                                                        <%}else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("ING_FAMILIAR") > -1){%>
                                                                            <p>
                                                                                <label><%=(formulario.getIsRequerido()==1?"*":"")%> <%=formulario.getEtiqueta()%>:</label><br/>
                                                                                <input type="hidden" id="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" value="<%=formulario.getIdFormularioCampo()%>" />
                                                                                <input maxlength="1000" type="text" id="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" style="width:200px"
                                                                                       value="<%=FormatUtil.doubleToStringPuntoComas((montoMensualFamiliar))%>" readonly/>
                                                                            </p>
                                                                            <br/>
                                                                        <%}else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("GFAM_MONTO_SOLICITADO") > -1){%>
                                                                            <p>
                                                                                <label><%=(formulario.getIsRequerido()==1?"*":"")%> <%=formulario.getEtiqueta()%>:</label><br/>
                                                                                <input type="hidden" id="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" value="<%=formulario.getIdFormularioCampo()%>" />
                                                                                <input maxlength="1000" type="text" id="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" style="width:200px"
                                                                                       value="<%=FormatUtil.doubleToStringPuntoComas((crProductoCreditoDto!=null?crProductoCreditoDto.getMonto():0))%>" readonly/>
                                                                            </p>
                                                                            <br/>
                                                                        <%}else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("LIQ_PLAZO_PROP") > -1){%>
                                                                            <p>
                                                                                <label><%=(formulario.getIsRequerido()==1?"*":"")%> <%=formulario.getEtiqueta()%>:</label><br/>
                                                                                <input type="hidden" id="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" value="<%=formulario.getIdFormularioCampo()%>" />
                                                                                <input maxlength="1000" type="text" id="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" style="width:200px"
                                                                                       value="<%=(crProductoCreditoDto!=null?crProductoCreditoDto.getPlazo():0)%>" readonly/>
                                                                            </p>
                                                                            <br/>
                                                                        <%}else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("LIQ_MONTO_AUT") > -1){%>
                                                                            <p>
                                                                                <label><%=(formulario.getIsRequerido()==1?"*":"")%> <%=formulario.getEtiqueta()%>:</label><br/>
                                                                                <input type="hidden" id="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" value="<%=formulario.getIdFormularioCampo()%>" />
                                                                                <input maxlength="1000" type="text" id="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" style="width:200px"
                                                                                       value="<%=FormatUtil.doubleToStringPuntoComas((crProductoCreditoDto!=null?crProductoCreditoDto.getMonto():0))%>" readonly/>
                                                                            </p>
                                                                            <br/>
                                                                        <%}else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("LIQ_PLAZO_AUT") > -1){%>
                                                                            <p>
                                                                                <label><%=(formulario.getIsRequerido()==1?"*":"")%> <%=formulario.getEtiqueta()%>:</label><br/>
                                                                                <input type="hidden" id="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" value="<%=formulario.getIdFormularioCampo()%>" />
                                                                                <input maxlength="1000" type="text" id="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" style="width:200px"
                                                                                       value="<%=(crProductoCreditoDto!=null?crProductoCreditoDto.getPlazo():0)%>" readonly/>
                                                                            </p>
                                                                            <br/>
                                                                        <%}else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("LIQ_CAP_PAGO") > -1){%>
                                                                            <p>
                                                                                <label><%=(formulario.getIsRequerido()==1?"*":"")%> <%=formulario.getEtiqueta()%>:</label><br/>
                                                                                <input type="hidden" id="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" value="<%=formulario.getIdFormularioCampo()%>" />
                                                                                <input maxlength="1000" type="text" id="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" style="width:200px"
                                                                                       value="<%=cuotaMensual%>" readonly/>
                                                                            </p>
                                                                            <br/>
                                                                        <%}else{
                                                    %>
                                                        <p>
                                                            <label><%=(formulario.getIsRequerido()==1?"*":"")%> <%=formulario.getEtiqueta()%>:</label><br/>
                                                            <input type="hidden" id="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_ID_<%=formulario.getIdFormularioCampo()%>" value="<%=formulario.getIdFormularioCampo()%>" />
                                                            <input maxlength="1000" type="text" id="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" style="width:200px"
                                                               value="<%=crFormularioRespuesta!=null?crFormularioRespuesta.getValor(): "" %>"/>
                                                            <%if(formularioVerificacion ==1){%>
                                                            <input type="checkbox" class="checkbox" <%=crFormularioRespuesta!=null?(crFormularioRespuesta.getRevisar()==1?"checked":""):"" %> id="revisar_<%=formulario.getIdFormularioCampo()%>" name="revisar_<%=formulario.getIdFormularioCampo()%>" value="1" onclick="validaCheckBoxRevisar(<%=formulario.getIdFormularioCampo()%>);"> <label for="revisar_<%=formulario.getIdFormularioCampo()%>" class="checkVerificador">Verificar</label>                                                            
                                                            <%}%>
                                                            <%if(formularioVerificacion ==1 || (crFormularioRespuesta!=null && crFormularioRespuesta.getRevisar()==1)){%>
                                                            <input maxlength="200" type="text" id="revisar_comen_<%=formulario.getIdFormularioCampo()%>" name="revisar_comen_<%=formulario.getIdFormularioCampo()%>" style="width:120px; display: <%=crFormularioRespuesta!=null?(crFormularioRespuesta.getRevisar()==1?"block":"none"):"none"%>;"
                                                                       value="<%=crFormularioRespuesta!=null?crFormularioRespuesta.getRevisarComentario(): "" %>"/>
                                                            <%}%>
                                                            
                                                        </p>
                                                        <br/>  
                                                        
                                                                        <%}%>
                                                                        
                                                    <%}else if(formulario.getIdTipoCampo() == 3){%><!-- OPCION UNICA-->
                                                        <p>
                                                            <%String[] opciones = new String[0];
                                                            if (formulario!=null && !formulario.getOpciones().trim().equals("")){
                                                                opciones = formulario.getOpciones().split("\n");%>
                                                                <label><%=(formulario.getIsRequerido()==1?"*":"")%> <%=formulario.getEtiqueta()%>:</label><br/>
                                                                <%for (String opcion : opciones){%>
                                                                    <input type="radio" class="checkbox" id="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" value="<%=opcion.replaceAll("\n", "").replaceAll("\r", "")%>" <%=crFormularioRespuesta!=null?(crFormularioRespuesta.getValor().equals(opcion.replaceAll("\n", "").replaceAll("\r", ""))?"checked":""): "" %>> <label for="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" ><%=opcion%></label>
                                                                <%}
                                                                if(formularioVerificacion ==1){%>
                                                                    <input type="checkbox" class="checkbox" <%=crFormularioRespuesta!=null?(crFormularioRespuesta.getRevisar()==1?"checked":""):"" %> id="revisar_<%=formulario.getIdFormularioCampo()%>" name="revisar_<%=formulario.getIdFormularioCampo()%>" value="1" onclick="validaCheckBoxRevisar(<%=formulario.getIdFormularioCampo()%>);"> <label for="revisar_<%=formulario.getIdFormularioCampo()%>" class="checkVerificador">Verificar</label>
                                                                <%}
                                                                if(formularioVerificacion ==1 || (crFormularioRespuesta!=null && crFormularioRespuesta.getRevisar()==1)){%>
                                                                    <input maxlength="200" type="text" id="revisar_comen_<%=formulario.getIdFormularioCampo()%>" name="revisar_comen_<%=formulario.getIdFormularioCampo()%>" style="width:120px; display: <%=crFormularioRespuesta!=null?(crFormularioRespuesta.getRevisar()==1?"block":"none"):"none"%>;"
                                                                           value="<%=crFormularioRespuesta!=null?crFormularioRespuesta.getRevisarComentario(): "" %>"/>
                                                                <%}
                                                            }%>
                                                                                                              
                                                        </p>    
                                                        <br/>
                                                        
                                                    <%}else if(formulario.getIdTipoCampo() == 4){%><!-- OPCION UNICA-->
                                                        <p>
                                                            <%String[] opciones = new String[0];
                                                            if (formulario!=null && !formulario.getOpciones().trim().equals("")){
                                                                opciones = formulario.getOpciones().split("\n");
                                                                %>
                                                                <label><%=(formulario.getIsRequerido()==1?"*":"")%> <%=formulario.getEtiqueta()%>:</label><br/>
                                                                <select size="1" id="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>">
                                                                    <option value="" ></option>
                                                                    <%
                                                                    for (String opcion : opciones){%>
                                                                        <option value="<%=opcion.replaceAll("\n", "").replaceAll("\r", "")%>" <%=crFormularioRespuesta!=null?(crFormularioRespuesta.getValor().equals(opcion.replaceAll("\n", "").replaceAll("\r", ""))?"selected":""): "" %>><%=opcion%></option>                                                                    
                                                                    <%}%>
                                                                </select>
                                                                <%if(formularioVerificacion ==1){%>
                                                                    <input type="checkbox" class="checkbox" <%=crFormularioRespuesta!=null?(crFormularioRespuesta.getRevisar()==1?"checked":""):"" %> id="revisar_<%=formulario.getIdFormularioCampo()%>" name="revisar_<%=formulario.getIdFormularioCampo()%>" value="1" onclick="validaCheckBoxRevisar(<%=formulario.getIdFormularioCampo()%>);"> <label for="revisar_<%=formulario.getIdFormularioCampo()%>" class="checkVerificador">Verificar</label>
                                                                <%}%>
                                                                <%if(formularioVerificacion ==1 || (crFormularioRespuesta!=null && crFormularioRespuesta.getRevisar()==1)){%>
                                                                    <input maxlength="200" type="text" id="revisar_comen_<%=formulario.getIdFormularioCampo()%>" name="revisar_comen_<%=formulario.getIdFormularioCampo()%>" style="width:120px; display: <%=crFormularioRespuesta!=null?(crFormularioRespuesta.getRevisar()==1?"block":"none"):"none"%>;"
                                                                           value="<%=crFormularioRespuesta!=null?crFormularioRespuesta.getRevisarComentario(): "" %>"/>
                                                                <%}%>
                                                            <%}%>
                                                                                                              
                                                        </p>    
                                                        <br/>
                                                    <%}else if(formulario.getIdTipoCampo() == 5){%><!-- SUBTITULO-->
                                                        <h2><i><%=formulario.getEtiqueta()%></i></h2>                                                   
                                                    <%}else if(formulario.getIdTipoCampo() == 6 || formulario.getIdTipoCampo() ==7){%><!-- SI ES UNA IMAGEN: -->
                                                    
                                                    
                                                        <table>
                                                            <tr>
                                                                <td style="vertical-align: top;">
                                                                    <p>
                                                                        <label><%=(formulario.getIsRequerido()==1?"*":"")%>Subir Archivo (.jpg)</label><br/>
                                                                        <iframe src="../file/uploadSingleForm.jsp?id=archivoImagen&validate=jpg&queryCustom=isImagenCrFormularioAdjunto=1&IdCampoFormulario=<%=formulario.getIdFormularioCampo()%>" 
                                                                                id="iframe_archivos_jpg" 
                                                                                name="iframe_archivos_jpg" 
                                                                                style="border: none" scrolling="no"
                                                                                height="80" width="400">
                                                                            <p>Tu navegador no soporta iframes y son necesarios para el buen funcionamiento del aplicativo</p>
                                                                        </iframe>
                                                                    </p>                                                   

                                                                    <p>
                                                                        <label><%=(formulario.getIsRequerido()==1?"*":"")%> <%=formulario.getEtiqueta()%>:</label><br/>
                                                                        <input maxlength="30" type="text" id="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" name="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" style="width:300px"
                                                                               readonly value="<%=crFormularioRespuesta!=null?crFormularioRespuesta.getValor(): "" %>"/>
                                                                        <%if(formularioVerificacion ==1){%>
                                                                            <input type="checkbox" class="checkbox" <%=crFormularioRespuesta!=null?(crFormularioRespuesta.getRevisar()==1?"checked":""):"" %> id="revisar_<%=formulario.getIdFormularioCampo()%>" name="revisar_<%=formulario.getIdFormularioCampo()%>" value="1" onclick="validaCheckBoxRevisar(<%=formulario.getIdFormularioCampo()%>);"> <label for="revisar_<%=formulario.getIdFormularioCampo()%>" class="checkVerificador">Verificar</label>
                                                                        <%}%>
                                                                        <%if(formularioVerificacion ==1 || (crFormularioRespuesta!=null && crFormularioRespuesta.getRevisar()==1)){%>
                                                                            <input maxlength="200" type="text" id="revisar_comen_<%=formulario.getIdFormularioCampo()%>" name="revisar_comen_<%=formulario.getIdFormularioCampo()%>" style="width:120px; display: <%=crFormularioRespuesta!=null?(crFormularioRespuesta.getRevisar()==1?"block":"none"):"none"%>;"
                                                                                   value="<%=crFormularioRespuesta!=null?crFormularioRespuesta.getRevisarComentario(): "" %>"/>
                                                                        <%}%>
                                                                    </p>
                                                                    <br/>

                                                                    <% if (crFormularioRespuesta!=null && empresaDto!=null && !crFormularioRespuesta.getValor().trim().equals("")) {
                                                                        String rutaArchivoImagenPersonal = appConfig.getApp_content_path() + empresaDto.getRfc() + "/AdjuntosFormulario/" + crFormularioRespuesta.getValor();
                                                                        String rutaArchivoImgEncoded = java.net.URLEncoder.encode(rutaArchivoImagenPersonal, "UTF-8");
                                                                    %>
                                                                    <p>
                                                                        <label>Imagen</label>
                                                                        <div style="display: inline" >
                                                                            <a href="../file/download.jsp?ruta_archivo=<%=rutaArchivoImgEncoded%>">Descargar</a>
                                                                        </div>                                                        
                                                                    </p>
                                                                    <label>_________________________________________________</label><br/>

                                                                    <%}%>
                                                                </td>
                                                                <td>
                                                                    <p>
                                                                        <% if (!StringManage.getValidString(crFormularioRespuesta!=null?crFormularioRespuesta.getValor(): "").equals("")) { %>
                                                                        <img src='showImageConcepto.jsp?image=<%=crFormularioRespuesta.getValor()%>&rfc=<%=rfcEmpresaMatriz%>' alt="Foto" style="width: 125px">
                                                                        <% } else{ %>
                                                                            <br/>
                                                                            <i>&lt;&lt; Sin imágen registrada &gt;&gt;</i>
                                                                        <% } %>
                                                                    </p>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                                                                        
                                                    <%}else if(formulario.getIdTipoCampo() == 8){%>
                                                    <p>
                                                        <label><%=(formulario.getIsRequerido()==1?"*":"")%> <%=formulario.getEtiqueta()%>:</label><br/>
                                                        <input type="date" name="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>" id="id_formulario_campo_valor_<%=formulario.getIdFormularioCampo()%>"
                                                        value="<%= crFormularioRespuesta!=null?crFormularioRespuesta.getValor():"" %>"
                                                        style="width: 120px;"/>
                                                        <%if(formularioVerificacion ==1){%>
                                                            <input type="checkbox" class="checkbox" <%=crFormularioRespuesta!=null?(crFormularioRespuesta.getRevisar()==1?"checked":""):"" %> id="revisar_<%=formulario.getIdFormularioCampo()%>" name="revisar_<%=formulario.getIdFormularioCampo()%>" value="1" onclick="validaCheckBoxRevisar(<%=formulario.getIdFormularioCampo()%>);"> <label for="revisar_<%=formulario.getIdFormularioCampo()%>" class="checkVerificador">Verificar</label>
                                                        <%}%>
                                                        <%if(formularioVerificacion ==1 || (crFormularioRespuesta!=null && crFormularioRespuesta.getRevisar()==1)){%>
                                                            <input maxlength="200" type="text" id="revisar_comen_<%=formulario.getIdFormularioCampo()%>" name="revisar_comen_<%=formulario.getIdFormularioCampo()%>" style="width:120px; display: <%=crFormularioRespuesta!=null?(crFormularioRespuesta.getRevisar()==1?"block":"none"):"none"%>;"
                                                                value="<%=crFormularioRespuesta!=null?crFormularioRespuesta.getRevisarComentario(): "" %>"/>
                                                        <%}%>
                                                    </p>
                                                    <br/>
                                                    <%}%>
                                                    
                                                <%}%>
                                        </div>
                                        <%}%>
                                    </div>
                                    
                                    
                            </div>                                    
                        </div>
                        <!-- End left column window -->
                    </div>
                        
                        
                        <div id="action_buttons">                                
                            <p>
                                <h2><i><%="  "%></i></h2>
                                <%if(soloConsulta == 0){%>
                                    <%if(user.getUser().getIdRoles() != RolesBO.ROL_MESA_DE_CONTROL){%>
                                        <%if(soloCamposRevision == 1){%>
                                            <input type="button" id="enviar" value="Actualizar Datos" onclick="grabarActualizacion();"/>
                                        <%}else{%>
                                            <input type="button" id="enviar" value="Guardar como borrador" onclick="grabarBorrador();"/>
                                            <input type="button" id="enviar" value="Guardar" onclick="grabarEnviar();"/>
                                        <%}%>
                                    <%}else if(user.getUser().getIdRoles() == RolesBO.ROL_MESA_DE_CONTROL){%>
                                        <input type="button" id="enviar" value="Guardar" onclick="grabarMesaControl();"/>
                                        <input type="button" id="enviar" value="Rechazar" onclick="grabarMesaControlRechazo();"/>
                                    <%}%>
                                <%}%>
                                
                                <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                &nbsp;&nbsp;&nbsp;&nbsp;
                                <a href="https://consultas.curp.gob.mx/" target="_blank">
                                    <img src="../../images/icon_barras.png" alt="CURP" class="help" title="CURP"/>
                                </a>
                                
                            </p>
                        </div>
                    </form>
                    <!--TODO EL CONTENIDO VA AQUÍ-->

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>

        <script>
            $("select.flexselect").flexselect();
        </script>
    </body>
</html>
<%}%>
