<%-- 
    Document   : catCobranzas_ticket
    Created on : 7/03/2013, 06:06:05 PM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensCobranzaAbonoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SgfensCobranzaAbono"%>
<%@page import="com.tsp.sct.bo.UbicacionBO"%>
<%@page import="com.tsp.sct.dao.dto.Ubicacion"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.dao.dto.BancoOperacion"%>
<%@page import="com.tsp.sct.bo.BancoOperacionBO"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<!-------<//%@page import="com.tsp.sgfens.report.ReportBO"%>-->
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    int idBancoOperacion = -1;
    int copia = -1;
    try{ idBancoOperacion = Integer.parseInt(request.getParameter("idBancoOperacion")); }catch(NumberFormatException e){}
    try{ copia = Integer.parseInt(request.getParameter("copia")); }catch(NumberFormatException e){}
    // 3 = (modalidad PopUp) 
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
    
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Paginación
    */
    int paginaActual = 1;
    double registrosPagina = 10;
    double limiteRegistros = 0;
    int paginasTotales = 0;
    int numeroPaginasAMostrar = 5;

    try{
        paginaActual = Integer.parseInt(request.getParameter("pagina"));
    }catch(Exception e){}

    try{
        registrosPagina = Integer.parseInt(request.getParameter("registros_pagina"));
    }catch(Exception e){} 

    BancoOperacionBO bancoOperacionBO = new BancoOperacionBO(idBancoOperacion,user.getConn());
    BancoOperacion bancoOperacionDto = bancoOperacionBO.getBancoOperacion();   
    
    EmpresaBO empresaBO = new EmpresaBO(idEmpresa,user.getConn());
    Empresa empresa = empresaBO.getEmpresa();
    
    UbicacionBO ubicacionBO = new UbicacionBO(empresa.getIdUbicacionFiscal(),user.getConn());
    Ubicacion ubicacion = ubicacionBO.getUbicacion();
    
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <link rel="shortcut icon" href="../../images/favicon.ico">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../include/keyWordSEO.jsp" />

        <title><jsp:include page="../include/titleApp.jsp" /></title>

        <jsp:include page="../include/skinCSS.jsp" />

        <jsp:include page="../include/jsFunctions.jsp"/>
        
        <script language="Javascript">
                function imprSelec(nombre)
                {
                    var ficha = document.getElementById(nombre);
                    var ventimp = window.open(' ', 'popimpr');
                    ventimp.document.write( ficha.innerHTML );
                    ventimp.document.close();
                    ventimp.print( );
                    ventimp.close();
                } 
                                 
                function enviarPorCorreo(idOperacionBancaria){
                    var correoDestinoTicket = document.getElementById("correoDestinoTicket").value;                    
                    //if (ticket>0){                    
                        $.ajax({
                            type: "POST",
                            url: "catTicketCorreo_ajax.jsp",
                            data: {mode: 'ticketCorreo', correoDestinoTicket : correoDestinoTicket, idOperacionBancaria : idOperacionBancaria },
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
                                                //location.href = "catEmpleados_list.jsp";
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
                     //}
                }
                
                $(document).ready(function() {
                    //Si se recibio el parametro para que el modo sea en forma de popup
                    <%= mode.equals("popup")? "mostrarFormPopUpMode();":""%>
                });

                function mostrarFormPopUpMode(){
                    $('#left_menu').hide();
                    $('#header').hide();
                    //$('#show_menu').show();
                    $('body').addClass('nobg');
                    $('#content').css('marginLeft', 30);
                    $('#wysiwyg').css('width', '97%');
                    setNotifications();
                }
        </script>

    </head>
    <body>
        <div class="content_wrapper">

            <% if (!mode.equals("popup")) {%>
                <jsp:include page="../include/header.jsp" flush="true"/>
                <jsp:include page="../include/leftContent.jsp"/>
            <% } %>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Cobranza Ticket</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--<div class="onecolumn">-->
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_ventas1.png" alt="icon"/>
                                    Recibo
                                </span>
                                <div class="switch" style="width:410px">
                                    <table width="300px" cellpadding="0" cellspacing="0">
                                        <tbody>

                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <br class="clear"/>
                            <a href="javascript:imprSelec('impresion')" >Imprimir Recibo</a> &nbsp;&nbsp;&nbsp;&nbsp;  
                            <br/>
                            <input maxlength="100" type="text" id="correoDestinoTicket" name="correoDestinoTicket" style="width:300px"
                                value="" title="Correo destinatario" />
                            <a href="" onclick="enviarPorCorreo(<%=bancoOperacionDto.getIdOperacionBancaria()%>);" >Enviar por Correo el Recibo</a> 
                            <div class="content" id="impresion">
                                <form id="form_data" name="form_data" action="" method="post">                                
                                    <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                        <thead>
                                            <!-- <tr>
                                            <th>Folio del Recibo: </th>
                                                <th>Fecha del Pago: </th>
                                                <th>Nombre del Vendedor:</th>
                                                <th>Nombre del Cliente:</th>
                                                <th>Monto a Pagar:</th>
                                                <th>Monto Pagado:</th>
                                                <th>Firma:</th>
                                                <th>Acciones</th>
                                            </tr>-->
                                        </thead>
                                        <tbody>
                                            <% 
                                                //for (Region item:regionsDto){
                                                    try{
                                                        Configuration appConfig = new Configuration();
                                            %>
                                            <!------<tr <//%=(item.getIdEstatus()!=1)?"class='inactive'":""%>>-->
                                                <!--<td><input type="checkbox"/></td>--> 
                                                <div ALIGN=center style="font-size: 7px;font-family: Verdana;">
                                                <p>www.movilpyme.com</p>
                                                <p>VENTA
                                                <!--<br/>
                                                BANORTE (<%=appConfig.getNumAfiliacionBanco()%>)-->
                                                <br/>
                                                <%=empresa.getNombreComercial()%>
                                                <br/>
                                                <%if(ubicacion.getNumInt()!=null){
                                                    if(!ubicacion.getNumInt().equals("")){%>
                                                    <%=ubicacion.getCalle()+" "+ubicacion.getNumExt()+", INT. "+ubicacion.getNumInt()%>
                                                    <br/>
                                                    <%}else{%>
                                                    <%=ubicacion.getCalle()+" "+ubicacion.getNumExt()%>
                                                    <br/>
                                                    <%}
                                                }else{%>
                                                    <%=ubicacion.getCalle()+" "+ubicacion.getNumExt()%>
                                                    <br/>
                                                <%}%>
                                                COL. <%=ubicacion.getColonia()+" DEL. "+ubicacion.getMunicipio()%>
                                                <br/>
                                                <%=ubicacion.getPais()+", "+ubicacion.getEstado()+", CP. "+ubicacion.getCodigoPostal()%></p>
                                                <%if(copia==1){%>
                                                <p>-COPIA-</p>
                                                <%}else{%>
                                                <p>-ORIGINAL-</p>
                                                <%}%>
                                                </div>
                                        
                                                <div ALIGN=center style="font-size: 7px;font-family: Verdana;">
                                                    <p><b><%=bancoOperacionDto.getBancoOperFecha() %></b></p>
                                                </div>
                                                <br/>
                                                <div ALIGN=center style="font-size: 7px;font-family: Verdana;">
                                                    <%if(bancoOperacionDto.getIdEstatus()==1){%>
                                                    <p><b>APROBADA &nbsp;&nbsp;&nbsp; <%=bancoOperacionDto.getBancoAuth()%></b></p>
                                                    <%}else{%>
                                                    <p><b>CANCELACION APROBADA &nbsp;&nbsp;&nbsp; <%=bancoOperacionDto.getBancoAuth()%></b></p>
                                                    <%}%>
                                                </div> 
                                                <br/>
                                                <table style="font-size: 7px;font-family: Verdana;">
                                                    <tbody>
                                                        <tr> <!--No tarjeta -->
                                                            <td style="width: 50%">N&uacute;mero de tarjeta:</td>
                                                            <td style="width: 50%" align="right">************<%=bancoOperacionDto.getNoTarjeta().substring(12)%></td>
                                                        </tr>
                                                        <tr> <!--Importe -->
                                                            <td style="width: 50%"><b>Importe:</b></td>
                                                            <td style="width: 50%" align="right"><b>$<%=bancoOperacionDto.getMonto()%></b></td>
                                                        </tr>
                                                        <tr> <!--ARQC -->
                                                            <td style="width: 50%">ARQC:</td>
                                                            <td style="width: 50%" align="right"><%= StringManage.getValidString(bancoOperacionDto.getDataArqc()) %></td>
                                                        </tr>
                                                        <tr> <!--AID -->
                                                            <td style="width: 50%">AID:</td>
                                                            <td style="width: 50%" align="right"><%= StringManage.getValidString(bancoOperacionDto.getDataAid()) %></td>
                                                        </tr>
                                                        <tr> <!--TSI -->
                                                            <td style="width: 50%">TSI:</td>
                                                            <td style="width: 50%" align="right"><%= StringManage.getValidString(bancoOperacionDto.getDataTsi()) %></td>
                                                        </tr>
                                                        <tr> <!--REF -->
                                                            <td style="width: 50%">REF:</td>
                                                            <td style="width: 50%" align="right"><%= StringManage.getValidString(bancoOperacionDto.getDataRef()) %></td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                                
                                                <!--
                                                <div ALIGN=left style="font-size: 7px;font-family: Verdana;">
                                                <p>NUMERO DE TARJETA</p>
                                                <p>XXXX XXXX XXXX <%=bancoOperacionDto.getNoTarjeta().substring(12)%></p>
                                                <%if(bancoOperacionDto.getBancoOperIssuingBank()!=null && bancoOperacionDto.getBancoOperType()!= null){%>
                                                    <p><%=bancoOperacionDto.getBancoOperIssuingBank()+", "+bancoOperacionDto.getBancoOperType() %></p>
                                                <%}%>                                            
                                                </div>

                                                <div ALIGN=left style="font-size: 7px;font-family: Verdana;">
                                                <p><b>IMPORTE $<%=bancoOperacionDto.getMonto()%></b></p>
                                                </div>
                                                -->

                                                <div ALIGN=center style="font-size: 7px;font-family: Verdana;">
                                                <p>Firma: _________________________</p>                                            
                                                <p><%=bancoOperacionDto.getNombreTitular()%></p>
                                                </div>

                                                <div ALIGN=justify style="font-size: 7px;font-family: Verdana;">
                                                <p>
                                                Por este pagaré me obligo incondicionalmente a pagar a la orden del banco
                                                emisor el importe total de este título en los términos de contrato suscrito para
                                                el uso de esta tarjeta de crédito. En el caso de operaciones con tarjeta de
                                                débito, expresamente reconozco y acepto que este recibo es el comprobante
                                                de la operación realizada, misma que me consigna en el presente pagare y
                                                tendrá pleno valor probatorio y fuerza legal, en virtud de lo que firme
                                                personalmente y/o digite mi número de identificación personal como firma
                                                electrónica, el cual es exclusivo de mi responsabilidad manifestando plena
                                                conformidad al respecto. El presente pagaré es negociable únicamente con
                                                instituciones bancarias.
                                                </p>
                                                </div>
                                            </tr>
                                            <%      }catch(Exception ex){
                                                        ex.printStackTrace();
                                                    }
                                                //} 
                                            %>
                                        </tbody>
                                    </table>
                                </form>                           

                            </div>
                        </div>
                        <!-- End left column window -->
                        
                        <!-- Begin right column window -->
                        <div class="column_right">
                                <div class="header">
                                        <span>Adicionales</span>
                                </div>
                                <br class="clear"/>
                                <div class="content">
                                    <!--<h3>Elementos adicionales</h3>-->

                                    <!-- Begin media modal window -->
                                    <ul class="media_photos">
                                        <!--Firma-->
                                        <%
                                            SgfensCobranzaAbono cobranzaAbono = null;
                                            try{
                                                SgfensCobranzaAbonoDaoImpl cobranzaAbonoDaoImpl = new SgfensCobranzaAbonoDaoImpl(user.getConn());
                                                SgfensCobranzaAbono[] cobranzas = cobranzaAbonoDaoImpl.findWhereIdOperacionBancariaEquals(bancoOperacionDto.getIdOperacionBancaria());
                                                if (cobranzas.length>0)
                                                    cobranzaAbono = cobranzas[0];
                                            }catch(Exception ex){
                                                System.out.println("Error al intentar obtener nombre de imágen de firma.");
                                                ex.printStackTrace();
                                            }
                                            if (cobranzaAbono!=null){
                                                if (!StringManage.getValidString(cobranzaAbono.getNombreArchivoImgFirma()).equals("") ){
                                        %>
                                        <li>
                                            <a rel="slide" href="../catCobroTarjeta/showImageFirma.jsp?image=<%= cobranzaAbono.getNombreArchivoImgFirma() %>" title="Firma de conformidad del titular">
                                                <img src="../catCobroTarjeta/showImageFirma.jsp?image=<%= cobranzaAbono.getNombreArchivoImgFirma() %>" alt="Firma de conformidad del titular"/>
                                            </a>
                                        </li>
                                        <%
                                                }
                                            }
                                        %>

                                        <!--Imágen identificación (IFE)-->
                                        <% if (bancoOperacionDto.getNombreArchivoImgIfe()!=null) { %>
                                        <li>
                                            <a rel="slide" href="../catCobroTarjeta/showImageFirma.jsp?image=<%= bancoOperacionDto.getNombreArchivoImgIfe() %>" title="Foto Identificación Oficial titular">
                                                <img src="../catCobroTarjeta/showImageFirma.jsp?image=<%= bancoOperacionDto.getNombreArchivoImgIfe() %>" alt="Foto Identificación Oficial titular"/>
                                            </a>
                                        </li>
                                        <% } %>

                                        <!--Imágen tarjeta (TDC)-->
                                        <% if (bancoOperacionDto.getNombreArchivoImgTdc()!=null) { %>
                                        <li>
                                            <a rel="slide" href="../catCobroTarjeta/showImageFirma.jsp?image=<%= bancoOperacionDto.getNombreArchivoImgTdc() %>" title="Foto de Tarjeta">
                                                <img src="../catCobroTarjeta/showImageFirma.jsp?image=<%= bancoOperacionDto.getNombreArchivoImgTdc() %>" alt="Foto de Tarjeta"/>
                                            </a>
                                        </li>
                                        <% } %>
                                    </ul>
                                    <!-- End media modal window -->
                                </div>
                        </div>
                        <!-- End right column window -->
                                    
                    </div>

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>            
    </body>
</html>
<%}%>