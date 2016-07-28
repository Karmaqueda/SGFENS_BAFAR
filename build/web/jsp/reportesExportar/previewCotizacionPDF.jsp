<%-- 
    Document   : viewPdf
    Created on : 7/09/2011, 02:21:14 PM
    Author     : ISC Cesar Martinez poseidon24@hotmail.com
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
    int idCotizacion=-1;
    try { idCotizacion = Integer.parseInt( request.getParameter("idCotizacion")); }catch(Exception ex){}

    String urlResponse = "/ServletCotizacionPDF";
%>
<jsp:include page="../../jsp/include/registraNavegacionBitacora.jsp"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Vista Previa de Cotizacion</title>
    </head>
    <body scroll="no" style="background-color: white ; ">
            <center>
                Descarga de archivos:&nbsp;&nbsp;&nbsp;
                <a href='<%=request.getContextPath()+urlResponse+"?idCotizacion="+idCotizacion %>'>
                    <img src="../../images/icon_pdf.png" alt="Archivo PDF">
                    PDF
                </a>
                <br/>
                <object id='pdfObject' type='application/pdf' data='<%=request.getContextPath()+urlResponse+"?idCotizacion="+idCotizacion %>' width='100%' height='300px'>
                No cuenta con la instalación de Adobe Reader
            </object>
            </center>
    </body>
</html>
