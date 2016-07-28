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
    String rutaNombreArchivo = "";
    String versionCfd;
    rutaNombreArchivo = request.getParameter("ruta_archivo")!=null?java.net.URLDecoder.decode(request.getParameter("ruta_archivo"), "UTF-8"):"";
    versionCfd = request.getParameter("versionCfd");

    double versionCfdDouble = Double.parseDouble(versionCfd);

    String urlResponse = "";
    if (versionCfdDouble==3.2){
         urlResponse = "/ServletCfdi32Pdf"; //Para CFDI v3.2
    }

    String urlDownloadXML ="/jsp/file/download.jsp";
    String archivoXML = null;
    String rutaArchivoEnc = null;
    File fileXML = null;
    try{
        fileXML = new File(rutaNombreArchivo);
        
        if (fileXML!=null
                && fileXML.exists()){
            archivoXML = fileXML.getName();
        
            rutaArchivoEnc = java.net.URLEncoder.encode(rutaNombreArchivo, "UTF-8");
        }
        
    }catch(Exception e){
        e.printStackTrace();
    }
%>
<jsp:include page="../../jsp/include/registraNavegacionBitacora.jsp"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Vista Previa de Comprobante Fiscal Digital</title>
    </head>
    <body scroll="no" style="background-color: white ; ">
            <center>
                Descarga de archivos:&nbsp;&nbsp;&nbsp;
                <a href='<%=request.getContextPath()+urlResponse+"?ruta_archivo="+rutaArchivoEnc %>'>
                    <img src="../../images/icon_pdf.png" alt="Archivo PDF">
                    PDF
                </a>
                    &nbsp;&nbsp;&nbsp;
                <% if (fileXML!=null && fileXML.exists() && rutaArchivoEnc!=null) {%>
                <a href='<%=request.getContextPath()+urlDownloadXML+"?ruta_archivo="+rutaArchivoEnc %>'>
                    <img src="../../images/icon_xml.png" alt="Archivo XML">
                    XML
                </a>
                <%}%>
                    &nbsp;&nbsp;&nbsp;
                <br/>
                <object id='pdfObject' type='application/pdf' data='<%=request.getContextPath()+urlResponse+"?ruta_archivo="+rutaArchivoEnc %>' width='100%' height='300px'>
                No cuenta con la instalación de Adobe Reader
            </object>
            </center>
    </body>
</html>
