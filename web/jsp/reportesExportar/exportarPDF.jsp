<%-- 
    Document   : exportarPDF
    Created on : 19-sep-2011, 22:10:52
    Author     : luismorales
--%>

<%@page import="com.tsp.sct.bo.ImagenPersonalBO"%>
<%@page import="com.tsp.sgfens.report.pdf.ReportExportablePDF"%>
<%@page import="java.io.File"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    int i = 0;
    String parametrosCustom="";
    String parametrosExtra="";
    String infoTitle="";
    
    try{
        i = Integer.parseInt(request.getParameter("i"));
    }catch(Exception e){}
    parametrosCustom = request.getParameter("parametrosCustom")!=null?new String(request.getParameter("parametrosCustom").getBytes("ISO-8859-1"),"UTF-8"):"";
    parametrosExtra = request.getParameter("parametrosExtra")!=null?new String(request.getParameter("parametrosExtra").getBytes("ISO-8859-1"),"UTF-8"):"";
    infoTitle = request.getParameter("infoTitle")!=null?new String(request.getParameter("infoTitle").getBytes("ISO-8859-1"),"UTF-8"):"";

    if(i > 0){

        ByteArrayOutputStream bPDF = new ByteArrayOutputStream();

        ReportExportablePDF toPdf = new ReportExportablePDF(user.getConn());
        toPdf.setUser(user);
        String params = "";
        
        if (!"".equals(params)){
            params = params + " AND " + parametrosCustom;
        }else{
            params = parametrosCustom;
        }
        
        /*
        * Imagen Logo para reporte
        */
//Recuperar archivo de imagen logo de la empresa (usuario en sesion)
		try{
        File fileImagenPersonal = new ImagenPersonalBO(user.getConn()).getFileImagenPersonalByEmpresa(user.getUser().getIdEmpresa());
        if (fileImagenPersonal!=null)
            toPdf.setFileImageLogo(fileImagenPersonal);
        //File logoImage = new File("/C:/la_florida.png");
        //toPdf.setFileImageLogo(logoImage);
        //
		}catch(Exception eIm){toPdf.setFileImageLogo(null);}
        
        bPDF = toPdf.generarReporte(i, params, parametrosExtra, infoTitle);

        if (bPDF!=null){
            response.setContentType("application/pdf");
            response.setContentLength(bPDF.size());
            response.setHeader("Content-Disposition", "attachment; filename=reporte.pdf");
            ServletOutputStream outputStream = response.getOutputStream();
            bPDF.writeTo(outputStream);
            outputStream.flush();
        }else{
            out.println("<script> alert('El reporte que se intenta descargar esta vacío. Intente con otro filtro de busqueda.'); "
                    + " window.close(); </script>");
            out.println("<h1>El reporte que se intenta descargar esta vacío. Intente de nuevo.</h1>");
        }

    }else{
        out.print("No se seleccionó algún reporte");
    }
%>