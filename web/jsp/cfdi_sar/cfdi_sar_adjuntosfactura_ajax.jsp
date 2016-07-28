<%-- 
    Document   : 
    Created on : 24/03/2015, 06:44:20 PM
    Author     : ISC Cesar Martinez poseidon24@hotmail.com
--%>
<%@page import="java.io.File"%>
<%@page import="com.tsp.sct.bo.SarComprobanteAdjuntoBO"%>
<%@page import="com.tsp.sct.dao.dto.SarComprobanteAdjunto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<jsp:include page="../../jsp/include/registraNavegacionBitacora.jsp"/>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="../../css/sar/style_flow_table.css" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Adjuntos de Factura</title>
    </head>
    <body scroll="no" style="background-color: white ; ">
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
        String urlDownloadXML ="/jsp/file/download.jsp";
        
        /*
         * Validacion de servidor
         */
        String msgError = "";
        String filtroBusqueda = "";
        if (idComprobanteFiscal <= 0) {
            msgError = "No se indico el comprobante.";
        }else{
            filtroBusqueda += " AND ID_COMPROBANTE_FISCAL = " + idComprobanteFiscal;
        }

        try {
            if (msgError.equals("")) {
                //Consultamos la usuarioBO de la factura
                SarComprobanteAdjunto[] adjuntos = new SarComprobanteAdjuntoBO(user.getConn()).findSarComprobanteAdjuntos(-1, 0, 0, filtroBusqueda);
            %>
            <div id="tituloArchivosAdjuntos">
                <table>
                    <thead>
                        <th class="headTableFlow">
                            Archivos Adjuntos de Factura ID: <%=idComprobanteFiscal %>
                        </th>
                    </thead>
                </table>
            </div>
        <br/>

        <br/>
        <%
                //Recorremos los registros
                for (SarComprobanteAdjunto archivoAdjunto : adjuntos) {
                    
                    File archivo = new SarComprobanteAdjuntoBO(user.getConn()).getArchivoDeRepositorioByEmpresa(archivoAdjunto.getNombreArchivo(), idEmpresa);
                    String rutaArchivoEnc = java.net.URLEncoder.encode(archivo.getAbsolutePath(), "UTF-8");
        %>

        <table>
            <thead>
            <th colspan="3" class="headTableFlow">
                <%=archivoAdjunto.getNombreArchivo()%> 
                <br/>
                <a href='<%=request.getContextPath() + urlDownloadXML + "?ruta_archivo="+rutaArchivoEnc %>'>
                    <img src="../../images/document_down.png" width="16" height="16" alt="Archivo"> [Descargar]
                </a>
            </th>
            </thead>
            <tbody>
            </tbody>
        </table>
                            
    <br/>
<%
                                }
                } else { 
                    out.print("<!--ERROR-->" + msgError);
                }
            } catch (Exception e) {
                out.print("<!--ERROR--> No se pudo completar la petición debido a un error: " + msgError + " " + e.getMessage());
            }
%>
</center>
</body>
</html>
