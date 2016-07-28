<%-- 
    Document   : previewExternoCfdPdf
    Created on : 20/04/2015, 02:21:14 PM
    Author     : ISC Cesar Martinez poseidon24@hotmail.com
--%>
<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
    int idValeAzul =-1;
    try { idValeAzul = Integer.parseInt( request.getParameter("id_cxp_vale_azul")); }catch(Exception ex){}


    String urlResponse = "/ServletValeAzulPDF";
%>
<jsp:include page="../../jsp/include/registraNavegacionBitacora.jsp"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Vista Previa de Vale Azul</title>
    </head>
    <body scroll="no" style="background-color: white ; ">
            <center>
                Descarga de archivos:&nbsp;&nbsp;&nbsp;
                <a href='<%=request.getContextPath()+urlResponse+"?id_cxp_vale_azul="+idValeAzul %>'>
                    <img src="../../images/icon_pdf.png" alt="Archivo PDF">
                    PDF
                </a>
                    &nbsp;&nbsp;&nbsp;
                <br/>
                <object id='pdfObject' type='application/pdf' data='<%=request.getContextPath()+urlResponse+"?id_cxp_vale_azul="+idValeAzul %>' width='100%' height='300px'>
                No cuenta con la instalación de Adobe Reader
            </object>
            </center>
    </body>
</html>