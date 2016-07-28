<%-- 
    Document   : getImageConcepto
    Created on : 08-feb-2016, 18:52:14
    Author     : ISCesarMartinez
--%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="java.io.BufferedInputStream"%>
<%@page import="java.io.BufferedOutputStream"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    //ServletOutputStream strm = response.getOutputStream();
    final int DEFAULT_BUFFER_SIZE = 10240;
    try{
         //String fileName = request.getParameter("image"); 
        int idConcepto = -1;
        try{ idConcepto = Integer.parseInt(request.getParameter("idConcepto")); }catch(Exception e){}
         
         Configuration appConfig = new Configuration();
         
         Empresa empresaDto = new EmpresaBO(user.getConn()).getEmpresaMatriz(user.getUser().getIdEmpresa());
         String rfcEmpresaMatriz = empresaDto.getRfc();
         
         String ubicacionImagenes = appConfig.getApp_content_path() + rfcEmpresaMatriz +"/ImagenConcepto/";
         
        if (idConcepto>0){
            Concepto conceptoDto = new ConceptoBO(idConcepto, user.getConn()).getConcepto();
            if (conceptoDto!=null){
                
                String archivoImagen= ubicacionImagenes + conceptoDto.getImagenNombreArchivo();
                File fileImagen = new File(archivoImagen);

                if (fileImagen.exists()){
                   response.reset();
                   response.setBufferSize(DEFAULT_BUFFER_SIZE);
                   response.setContentType("image/jpeg");
                   response.setHeader("Content-Length", String.valueOf(fileImagen.length()));
                   response.setHeader("Content-Disposition", "inline; filename=\"" + fileImagen.getName() + "\"");


                   // Prepare streams.
                   BufferedInputStream input = null;
                   BufferedOutputStream output = null;

                   try {
                       // Open streams.
                       input = new BufferedInputStream(new FileInputStream(fileImagen), DEFAULT_BUFFER_SIZE);
                       output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

                       // Write file contents to response.
                       byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                       int length;
                       while ((length = input.read(buffer)) > 0) {
                           output.write(buffer, 0, length);
                       }
                   } finally {
                       // Gently close streams.
                       if (input!=null) 
                           input.close();
                       if (output!=null)
                           output.close();
                   }
               }else{
                    //
               }
            }
        }
                                    
    }
    catch(Exception e){
        e.printStackTrace();
    }finally{
        // close the streams
    }
%>