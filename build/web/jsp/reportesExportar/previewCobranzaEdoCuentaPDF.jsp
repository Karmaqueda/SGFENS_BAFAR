<%-- 
    Document   : viewPdf
    Created on : 08/01/2013, 02:21:14 PM
    Author     : ISC Cesar Martinez poseidon24@hotmail.com
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
    int idPedido=-1;
    try { idPedido = Integer.parseInt( request.getParameter("idPedido")); }catch(Exception ex){}
    
    int idComprobanteFiscal=-1;
    try { idComprobanteFiscal = Integer.parseInt( request.getParameter("idComprobanteFiscal")); }catch(Exception ex){}

    String urlResponse = "/ServletCobranzaEdoCuentaPDF";
%>
<jsp:include page="../../jsp/include/registraNavegacionBitacora.jsp"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Vista Previa de Estado de Cuenta</title>
    </head>
    <body scroll="no" style="background-color: white ; ">
            <center>
                Descarga de archivos:&nbsp;&nbsp;&nbsp;
                <a href='<%=request.getContextPath()+urlResponse+"?idPedido="+idPedido+"&idComprobanteFiscal="+idComprobanteFiscal %>'>
                    <img src="../../images/icon_pdf.png" alt="Archivo PDF">
                    PDF
                </a>
                <br/>
                <object id='pdfObject' type='application/pdf' data='<%=request.getContextPath()+urlResponse+"?idPedido="+idPedido+"&idComprobanteFiscal="+idComprobanteFiscal %>' width='100%' height='300px'>
                No cuenta con la instalación de Adobe Reader
            </object>
            </center>
    </body>
</html>