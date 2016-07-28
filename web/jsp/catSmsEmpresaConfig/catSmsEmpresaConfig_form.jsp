<%-- 
    Document   : catSmsEmpresaConfig_list
    Created on : 10/03/2016, 12:55:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.SmsEmpresaConfigBO"%>
<%@page import="com.tsp.sct.dao.jdbc.SmsEmpresaConfigDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SmsEmpresaConfig"%>
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
    /*
    int idSmsEmpresaConfig = 0;
    try {
        idSmsEmpresaConfig = Integer.parseInt(request.getParameter("idSmsEmpresaConfig"));
    } catch (NumberFormatException e) {
    }
    */

    /*
     *   0/"" = nuevo
     *   1 = editar/consultar
     *   2 = eliminar  
     */
    //String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";

    Empresa empresaMatrizDto = new EmpresaBO(user.getConn()).getEmpresaMatriz(idEmpresa);
    SmsEmpresaConfigBO smsEmpresaConfigBO = new SmsEmpresaConfigBO(user.getConn());
    SmsEmpresaConfig smsEmpresaConfigDto = null;
    if (empresaMatrizDto !=null ){
        smsEmpresaConfigBO = new SmsEmpresaConfigBO(user.getConn());
        SmsEmpresaConfig[] smsEmpresaConfigDtos = smsEmpresaConfigBO.findSmsEmpresaConfigs(-1, empresaMatrizDto.getIdEmpresa(), 0, 0, "");
        if (smsEmpresaConfigDtos.length>0)
            smsEmpresaConfigDto = smsEmpresaConfigDtos[0];
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
                        url: "catSmsEmpresaConfig_ajax.jsp",
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
                                            location.href = "../catSmsEnvio/catSmsEnvio_list.jsp";
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
            
            var m_venta_fijo = 'Se ha registrado una venta por un monto de 0.00 correspondiente a la compra de 0 productos, se adeuda un total de 0.00.';
            var m_abono_fijo = 'Se ha registrado un pago por un monto de 0.00, se adeuda un total de 0.00 de un monto inicial de 0.00.';
            var m_factura_fijo = 'Se ha creado la factura ID 0000. UUID xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx y se ha enviado una notificación al correo registrado de cliente.';
            var m_cancela_fac_fijo = 'Se ha cancelado la factura ID 0000. UUID xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx y se ha enviado una notificación al correo registrado de cliente.';
            var m_callcenter_fijo = 'Se ha {Registrado/Actualizado/Cerrado} el ticket de servicio folio 00000, referente a XXXXXXXXXXXXX.';
            
            function actualizaPrev(idDivDestino, fijo, textoFijo, textoLibre){
                if (fijo=='0'){ //libre
                    $("#"+idDivDestino).html(""+textoLibre);
                }else if (fijo=='1'){ //Fijo
                    $("#"+idDivDestino).html(""+textoFijo);
                }else if (fijo=='2'){ //fijo + libre
                    $("#"+idDivDestino).html(""+textoFijo + textoLibre);
                }
            }
            
            function actualizarVista(){
                actualizaPrev('prev_msg_venta', $('#mensaje_venta_fijo').val(), m_venta_fijo, $('#mensaje_venta_libre').val());
                actualizaPrev('prev_msg_abono', $('#mensaje_abono_fijo').val(), m_abono_fijo, $('#mensaje_abono_libre').val());
                actualizaPrev('prev_msg_factura', $('#mensaje_factura_fijo').val(), m_factura_fijo, $('#mensaje_factura_libre').val());
                actualizaPrev('prev_msg_cancela_fac', $('#mensaje_cancela_fac_fijo').val(), m_cancela_fac_fijo, $('#mensaje_cancela_fac_libre').val());
                actualizaPrev('prev_msg_callcenter', $('#mensaje_callcenter_fijo').val(), m_callcenter_fijo, $('#mensaje_callcenter_libre').val());
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
                    <h1>SMS Masivos</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_smsConfiguracion.png" alt="icon"/>
                                    Editar Configuración SMS de '<%= empresaMatrizDto!=null?empresaMatrizDto.getRazonSocial():"" %>'
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="mode" name="mode" value="1" />
                                    
                                    <fieldset style="border: 2px groove; padding: 1em;">
                                        <legend>Nuevo Pedido</legend>
                                        <p>
                                            <input type="checkbox" id="mensaje_venta" name="mensaje_venta" value="<%= smsEmpresaConfigDto!=null?(smsEmpresaConfigDto.getMensajeVenta()>0?smsEmpresaConfigDto.getMensajeVenta():1):1 %>" 
                                                   <%= smsEmpresaConfigDto!=null?(smsEmpresaConfigDto.getMensajeVenta()==1 || smsEmpresaConfigDto.getMensajeVenta()==3?"checked":"" ):"" %> 
                                                   <%= smsEmpresaConfigDto!=null?(smsEmpresaConfigDto.getMensajeVenta()>1?"disabled":"" ):"" %>><label for="chk_msg_venta">Activar</label>
                                        </p>
                                        <p>
                                            <label>Tipo Mensaje:</label><br/>
                                            <select size="1" id="mensaje_venta_fijo" name="mensaje_venta_fijo" style="width:350px" onchange="actualizarVista();">
                                                <option value="1" <%= smsEmpresaConfigDto!=null? (smsEmpresaConfigDto.getMensajeVentaFijo()==1?"selected":""):"selected" %> >Fijo</option>
                                                <option value="0" <%= smsEmpresaConfigDto!=null? (smsEmpresaConfigDto.getMensajeVentaFijo()==0?"selected":""):"" %> >Libre</option>
                                                <option value="2" <%= smsEmpresaConfigDto!=null? (smsEmpresaConfigDto.getMensajeVentaFijo()==2?"selected":""):"" %> >Fijo + Libre</option>
                                            </select>
                                        </p>
                                        <p>
                                            <label>Texto Libre:</label><br/>
                                            <input maxlength="50" type="text" id="mensaje_venta_libre" name="mensaje_venta_libre" style="width:360px" onchange="actualizarVista();" value="<%= smsEmpresaConfigDto!=null? StringManage.getValidString(smsEmpresaConfigDto.getMensajeVentaLibre()) : "" %>"/>
                                        </p>
                                        <p>
                                            <label>Vista Previa:</label><br/>
                                            <div id="prev_msg_venta" class="message from"></div>
                                        </p>
                                    </fieldset>
                                    <br/>
                                    
                                    <fieldset style="border: 2px groove; padding: 1em;">
                                        <legend>Nuevo Abono</legend>
                                        <p>
                                            <input type="checkbox" id="mensaje_abono" name="mensaje_abono" value="<%= smsEmpresaConfigDto!=null?(smsEmpresaConfigDto.getMensajeAbono()>0?smsEmpresaConfigDto.getMensajeAbono():1):1 %>" 
                                                   <%= smsEmpresaConfigDto!=null?(smsEmpresaConfigDto.getMensajeAbono()==1 || smsEmpresaConfigDto.getMensajeAbono()==3?"checked":"" ):"" %> 
                                                   <%= smsEmpresaConfigDto!=null?(smsEmpresaConfigDto.getMensajeAbono()>1?"disabled":"" ):"" %>><label for="chk_msg_abono">Activar</label>
                                        </p>
                                        
                                        <p>
                                            <label>Tipo Mensaje:</label><br/>
                                            <select size="1" id="mensaje_abono_fijo" name="mensaje_abono_fijo" style="width:350px" onchange="actualizarVista();">
                                                <option value="1" <%= smsEmpresaConfigDto!=null? (smsEmpresaConfigDto.getMensajeAbonoFijo()==1?"selected":""):"selected" %> >Fijo</option>
                                                <option value="0" <%= smsEmpresaConfigDto!=null? (smsEmpresaConfigDto.getMensajeAbonoFijo()==0?"selected":""):"" %> >Libre</option>
                                                <option value="2" <%= smsEmpresaConfigDto!=null? (smsEmpresaConfigDto.getMensajeAbonoFijo()==2?"selected":""):"" %> >Fijo + Libre</option>
                                            </select>
                                        </p>
                                        <p>
                                            <label>Texto Libre:</label><br/>
                                            <input maxlength="50" type="text" id="mensaje_abono_libre" name="mensaje_abono_libre" style="width:360px" onchange="actualizarVista();" value="<%= smsEmpresaConfigDto!=null? StringManage.getValidString(smsEmpresaConfigDto.getMensajeAbonoLibre()) : "" %>"/>
                                        </p>
                                        <p>
                                            <label>Vista Previa:</label><br/>
                                            <div id="prev_msg_abono" class="message from"></div>
                                        </p>
                                    </fieldset>
                                    <br/>
                                                                                                          
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=smsEmpresaConfigDto!=null?(smsEmpresaConfigDto.getPermisoSmsEmpleados()==1?"checked":""):"" %> id="permiso_sms_empleados" name="permiso_sms_empleados" value="1"><label for="permiso_sms_empleados">SMS a Empleados</label>
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
                        
                        <div class="column_right">
                            <div class="header">
                                <span>
                                    
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                <fieldset style="border: 2px groove; padding: 1em;">
                                    <legend>Nueva Factura</legend>
                                    <p>
                                        <input type="checkbox" id="mensaje_factura" name="mensaje_factura" value="<%= smsEmpresaConfigDto!=null?(smsEmpresaConfigDto.getMensajeFactura()>0?smsEmpresaConfigDto.getMensajeFactura():1):1 %>" 
                                               <%= smsEmpresaConfigDto!=null?(smsEmpresaConfigDto.getMensajeFactura()==1 || smsEmpresaConfigDto.getMensajeFactura()==3?"checked":"" ):"" %> 
                                               <%= smsEmpresaConfigDto!=null?(smsEmpresaConfigDto.getMensajeFactura()>1?"disabled":"" ):"" %>><label for="chk_msg_factura">Activar</label>
                                    </p>

                                    <p>
                                        <label>Tipo Mensaje:</label><br/>
                                        <select size="1" id="mensaje_factura_fijo" name="mensaje_factura_fijo" style="width:350px" onchange="actualizarVista();">
                                            <option value="1" <%= smsEmpresaConfigDto!=null? (smsEmpresaConfigDto.getMensajeFacturaFijo()==1?"selected":""):"selected" %> >Fijo</option>
                                            <option value="0" <%= smsEmpresaConfigDto!=null? (smsEmpresaConfigDto.getMensajeFacturaFijo()==0?"selected":""):"" %> >Libre</option>
                                            <option value="2" <%= smsEmpresaConfigDto!=null? (smsEmpresaConfigDto.getMensajeFacturaFijo()==2?"selected":""):"" %> >Fijo + Libre</option>
                                        </select>
                                    </p>
                                    <p>
                                        <label>Texto Libre:</label><br/>
                                        <input maxlength="50" type="text" id="mensaje_factura_libre" name="mensaje_factura_libre" style="width:360px" onchange="actualizarVista();" value="<%= smsEmpresaConfigDto!=null? StringManage.getValidString(smsEmpresaConfigDto.getMensajeFacturaLibre()) : "" %>"/>
                                    </p>
                                    <p>
                                        <label>Vista Previa:</label><br/>
                                        <div id="prev_msg_factura" class="message from"></div>
                                    </p>
                                </fieldset>
                                <br/>
                                
                                <fieldset style="border: 2px groove; padding: 1em;">
                                    <legend>Cancelar Factura</legend>
                                    <p>
                                        <input type="checkbox" id="mensaje_cancela_fac" name="mensaje_cancela_fac" value="<%= smsEmpresaConfigDto!=null?(smsEmpresaConfigDto.getMensajeCancelaFac()>0?smsEmpresaConfigDto.getMensajeCancelaFac():1):1 %>" 
                                               <%= smsEmpresaConfigDto!=null?(smsEmpresaConfigDto.getMensajeCancelaFac()==1 || smsEmpresaConfigDto.getMensajeCancelaFac()==3?"checked":"" ):"" %> 
                                               <%= smsEmpresaConfigDto!=null?(smsEmpresaConfigDto.getMensajeCancelaFac()>1?"disabled":"" ):"" %>><label for="chk_msg_cancela_fac">Activar</label>
                                    </p>
                                    
                                    <p>
                                        <label>Tipo Mensaje:</label><br/>
                                        <select size="1" id="mensaje_cancela_fac_fijo" name="mensaje_cancela_fac_fijo" style="width:350px" onchange="actualizarVista();">
                                            <option value="1" <%= smsEmpresaConfigDto!=null? (smsEmpresaConfigDto.getMensajeCancelaFacFijo()==1?"selected":""):"selected" %> >Fijo</option>
                                            <option value="0" <%= smsEmpresaConfigDto!=null? (smsEmpresaConfigDto.getMensajeCancelaFacFijo()==0?"selected":""):"" %> >Libre</option>
                                            <option value="2" <%= smsEmpresaConfigDto!=null? (smsEmpresaConfigDto.getMensajeCancelaFacFijo()==2?"selected":""):"" %> >Fijo + Libre</option>
                                        </select>
                                    </p>
                                    <p>
                                        <label>Texto Libre:</label><br/>
                                        <input maxlength="50" type="text" id="mensaje_cancela_fac_libre" name="mensaje_cancela_fac_libre" style="width:360px" onchange="actualizarVista();" value="<%= smsEmpresaConfigDto!=null? StringManage.getValidString(smsEmpresaConfigDto.getMensajeCancelaFacLibre()) : "" %>"/>
                                    </p>
                                    <p>
                                        <label>Vista Previa:</label><br/>
                                        <div id="prev_msg_cancela_fac" class="message from"></div>
                                    </p>
                                </fieldset>
                                <br/>

                                <fieldset style="border: 2px groove; padding: 1em;">
                                    <legend>Acciones Call Center</legend>
                                    <p>
                                        <input type="checkbox" id="mensaje_callcenter" name="mensaje_callcenter" value="<%= smsEmpresaConfigDto!=null?(smsEmpresaConfigDto.getMensajeCallcenter()>0?smsEmpresaConfigDto.getMensajeCallcenter():1):1 %>" 
                                               <%= smsEmpresaConfigDto!=null?(smsEmpresaConfigDto.getMensajeCallcenter()==1 || smsEmpresaConfigDto.getMensajeCallcenter()==3?"checked":"" ):"" %> 
                                               <%= smsEmpresaConfigDto!=null?(smsEmpresaConfigDto.getMensajeCallcenter()>1?"disabled":"" ):"" %>><label for="chk_msg_callcenter">Activar</label>
                                    </p>
                                    <p>
                                        <label>Tipo Mensaje:</label><br/>
                                        <select size="1" id="mensaje_callcenter_fijo" name="mensaje_callcenter_fijo" style="width:350px" onchange="actualizarVista();">
                                            <option value="1" <%= smsEmpresaConfigDto!=null? (smsEmpresaConfigDto.getMensajeCallcenterFijo()==1?"selected":""):"selected" %> >Fijo</option>
                                            <option value="0" <%= smsEmpresaConfigDto!=null? (smsEmpresaConfigDto.getMensajeCallcenterFijo()==0?"selected":""):"" %> >Libre</option>
                                            <option value="2" <%= smsEmpresaConfigDto!=null? (smsEmpresaConfigDto.getMensajeCallcenterFijo()==2?"selected":""):"" %> >Fijo + Libre</option>
                                        </select>
                                    </p>
                                    <p>
                                        <label>Texto Libre:</label><br/>
                                        <input maxlength="50" type="text" id="mensaje_callcenter_libre" name="mensaje_callcenter_libre" style="width:360px" onchange="actualizarVista();" value="<%= smsEmpresaConfigDto!=null? StringManage.getValidString(smsEmpresaConfigDto.getMensajeCallcenterLibre()) : "" %>"/>
                                    </p>
                                    <p>
                                        <label>Vista Previa:</label><br/>
                                        <div id="prev_msg_callcenter" class="message from"></div>
                                    </p>
                                </fieldset>
                                <br/>
                            </div>
                        </div>
                        
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
            actualizarVista();
        </script>
    </body>
</html>
<%}%>