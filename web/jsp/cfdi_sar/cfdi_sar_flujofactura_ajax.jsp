<%-- 
    Document   : 
    Created on : 25/03/2015, 06:44:20 PM
    Author     : ISC Cesar Martinez poseidon24@hotmail.com
--%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.bo.SarComprobanteAdjuntoBO"%>
<%@page import="java.io.File"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.workflow.ws.WsItemBitacoraComprobante"%>
<%@page import="com.tsp.workflow.ws.WsItemComprobante"%>
<%@page import="com.tsp.workflow.ws.ConsultaFlujoComprobanteResponse"%>
<%@page import="com.tsp.workflow.ws.ConsultaComprobanteRequest"%>
<%@page import="com.tsp.sct.dao.dto.SarUsuarioProveedor"%>
<%@page import="com.tsp.sct.bo.SarUsuarioProveedorBO"%>
<%@page import="com.tsp.workflow.ws.AccesoRequest"%>
<%@page import="com.tsp.sct.bo.ConexionSARBO"%>
<%@page import="com.tsp.sct.dao.dto.SarComprobante"%>
<%@page import="com.tsp.sct.bo.SarComprobanteBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<jsp:include page="../../jsp/include/registraNavegacionBitacora.jsp"/>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="../../css/sar/style_flow_table.css" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Flujo de Factura en SAR</title>
        <jsp:include page="../include/jsFunctions.jsp"/>
        <script type="text/javascript">
            $(document).ready(function() {
                jQuery().scrollTo('#initFlow', 2500);
            });
        </script>
    </head>
    <body style="background-color: white ; ">
        <center>
            <%
                /*
                 * Parámetros
                 */
                int idComprobanteFiscal = 0;
                String mode = "";
                int idEmpresa = user.getUser().getIdEmpresa();

                /*
                 * Recepción de valores
                 */
                try {
                    idComprobanteFiscal = Integer.parseInt(request.getParameter("idComprobanteFiscal"));
                } catch (NumberFormatException ex) {
                }

                mode = request.getParameter("mode") != null ? request.getParameter("mode") : "";

                /*
                 * Validacion de servidor
                 */
                String msgError = "";
                if (idComprobanteFiscal <= 0) {
                    msgError = "No se indico la factura de la cuál se obtendrá su flujo.";
                }

                try {
                    if (msgError.equals("")) {
                        String urlDownloadXML ="/jsp/file/download.jsp";
                        
                        SarComprobanteBO sarComprobanteBO = new SarComprobanteBO(idComprobanteFiscal, user.getConn());
                        SarComprobante sarComprobanteDto = sarComprobanteBO.getSarComprobante();
                        
                        SarUsuarioProveedorBO sarUsuarioProveedorBO = new SarUsuarioProveedorBO(sarComprobanteDto.getIdSarUsuario(), user.getConn());
                        SarUsuarioProveedor sarUsuarioProveedorDto = sarUsuarioProveedorBO.getSarUsuarioProveedor();
            
                        //Consultamos la bitacora de la factura
                        AccesoRequest accesoRequest = new AccesoRequest();
                        accesoRequest.setUsuario(sarUsuarioProveedorDto.getExtSarUsuario());
                        accesoRequest.setContrasena(sarUsuarioProveedorDto.getExtSarPass());

                        ConsultaComprobanteRequest consultaComprobanteRequest = new ConsultaComprobanteRequest();
                        consultaComprobanteRequest.getListaIdFactura().add((long)sarComprobanteDto.getExtSarIdFactura());
                        
                        ConsultaFlujoComprobanteResponse wsResponse = ConexionSARBO.getFlujoComprobantes(accesoRequest, consultaComprobanteRequest);
                        
                        String rutaArchivoAcuseEnc = "";
                        try {
                            File archivoAcuse = new SarComprobanteAdjuntoBO(user.getConn()).getArchivoDeRepositorioByEmpresa(sarComprobanteDto.getNombreArchivoAcuse(), idEmpresa);
                            rutaArchivoAcuseEnc = java.net.URLEncoder.encode(archivoAcuse.getAbsolutePath(), "UTF-8");
                        }catch(Exception ex){}
                    
                        if (wsResponse.isError()){
                            msgError += "" + wsResponse.getNumError() + " - " + wsResponse.getMsgError();
                            out.print("<!--ERROR-->Error retornado por servicio al consultar Flujo en sistema SAR: " +   msgError);
                        }else if (wsResponse.getListaComprobantes().size()<=0){
                            out.print("<!--ERROR-->El comprobante no fue encontrado en el sistema SAR. Verifique información. (SAR ID factura " + sarComprobanteDto.getExtSarIdFactura() + ")");
                        }else{
                            WsItemComprobante wsComprobante = wsResponse.getListaComprobantes().get(0);
            %>
            <div id="endFlow">
                <table>
                    <thead>
                    <th class="headTableFlow">
                        Fin de Flujo de Factura SAR-ID: <%=sarComprobanteDto.getExtSarIdFactura() %>
                    </th>
                    </thead>
                </table>
            </div>
            <br/>

            <%
                                            //out.println("<h2>Flujo de la factura con ID " + idComprobanteFiscal + "</h2>");
                //Recorremos los registros de bitacora de la factura
                int i = 0;
                for (WsItemBitacoraComprobante registroBitacora : wsComprobante.getListaBitacora()){
            %>

            <% if (i == 0) {
            %>
            <div id="remind">
                <table>
                    <thead>
                        <th class="headTableFlow">
                            Acuse Recepción: <b>
                                <% if (StringManage.getValidString(rutaArchivoAcuseEnc).length()>0) { %>
                                <a href='<%=request.getContextPath() + urlDownloadXML + "?ruta_archivo="+rutaArchivoAcuseEnc %>'>
                                    <img src="../../images/document_down.png" width="16" height="16" alt="Archivo"> [Descargar]
                                </a>
                                <% }else{ %>
                                <i>No existe acuse de recepción para el comprobante</i>
                                <% } %>
                            </b>
                        </th>
                    </thead>
                </table>
            </div>
            <br/>
            <%  }%>

            <table>
                <thead>
                <th colspan="3" class="headTableFlow">
                    <%=registroBitacora.getNombreAccion() %>
                </th>
                </thead>
                <tbody>
                    <tr>
                        <td class="icon">
                            <img src="../../images/sar/clock.png" alt="Fecha"/>
                        </td>
                        <td class="label">
                            ¿Cuándo?
                        </td>
                        <td class="content">
                            <b>
                                <%=DateManage.dateTimeToStringEspanol(registroBitacora.getFechaHoraAccion().toGregorianCalendar().getTime()) %>
                            </b>
                        </td>
                    </tr>
                    <tr>
                        <td class="icon">
                            <img src="../../images/sar/info.png" alt="Usuario"/>
                        </td>
                        <td class="label">
                            ¿Quién?
                        </td>
                        <td class="content">
                            <b>
                                <%=registroBitacora.getNombrePersonaAccion() %>
                            </b>
                        </td>
                    </tr>
                    <tr>
                        <td class="icon">
                            <img src="../../images/sar/status.png" alt="Estatus"/>
                        </td>
                        <td class="label">
                            Estatus
                        </td>
                        <td class="content">
                            <b>
                                <%=registroBitacora.getDescripcionEstatus() %>
                            </b>
                        </td>
                    </tr>
                    <tr>
                        <td class="icon">
                            <img src="../../images/sar/forward.png" alt="Nivel"/>
                        </td>
                        <td class="label">
                            Nivel
                        </td>
                        <td class="content">
                            <b>
                                <%=registroBitacora.getDescripcioNivel() %>
                            </b>
                        </td>
                    </tr>
                    <tr>
                        <td class="icon">
                            <img src="../../images/sar/comment.png" alt="Nivel"/>
                        </td>
                        <td class="label">
                            Info
                        </td>
                        <td class="content">
                            <b>
                                <%=registroBitacora.getComentario() %>
                            </b>
                        </td>
                    </tr>
                </tbody>
            </table>

            <img src="../../images/sar/flecha_blue.png" alt="Flujo"/>

            <%
                    i++;
                }
            %>
            <div id="initFlow">
                <table>
                    <thead>
                    <th class="headTableFlow">
                        Inicio de Flujo de Factura SAR-ID: <%=sarComprobanteDto.getExtSarIdFactura() %>
                    </th>
                    </thead>
                </table>
            </div>
            <%
                        }
                    } else {
                        out.print("<!--ERROR-->" + msgError);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    out.print("<!--ERROR--> No se pudo completar la petición debido a un error: " + msgError + " " + e.getMessage());
                }
            %>
        </center>
    </body>
</html>
