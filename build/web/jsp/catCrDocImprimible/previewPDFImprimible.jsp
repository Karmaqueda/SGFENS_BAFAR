<%-- 
    Document   : previewPDFImprimible
    Created on : 5/07/2016, 02:32:14 PM
    Author     : ISCesar
--%>
<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
    int idCrDocImprimible =-1;
    try { idCrDocImprimible = Integer.parseInt( request.getParameter("idCrDocImprimible")); }catch(Exception ex){}

    int idCrFormularioEvento =-1;
    try { idCrFormularioEvento = Integer.parseInt( request.getParameter("idCrFormularioEvento")); }catch(Exception ex){}

    String urlResponse = "/ServletCrImprimiblePDF";
%>
<jsp:include page="../../jsp/include/registraNavegacionBitacora.jsp"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Vista Previa de Imprimible</title>
    </head>
    <body scroll="no" style="background-color: white ; ">
        <center>
            Descarga de archivos:&nbsp;&nbsp;&nbsp;
            <a href='<%=request.getContextPath()+urlResponse+"?idCrDocImprimible="+idCrDocImprimible+"&idCrFormularioEvento="+idCrFormularioEvento %>'>
                <img src="../../images/icon_pdf.png" alt="Archivo PDF">
                PDF
            </a>
                &nbsp;&nbsp;&nbsp;
            <br/>
            <object id='pdfObject' type='application/pdf' data='<%=request.getContextPath()+urlResponse+"?idCrDocImprimible="+idCrDocImprimible+"&idCrFormularioEvento="+idCrFormularioEvento %>' width='100%' height='300px'>
            No cuenta con la instalación del plugin de Adobe Reader para su Navegador o alguno compatible con archivos PDF.
        </object>
        </center>
    </body>
</html>