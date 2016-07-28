<%-- 
    Document   : registraAdjuntoAjax
    Created on : 8/04/2015, 06:50:02 PM
    Author     : ISCesar
--%>
<%@page import="com.tsp.sct.dao.jdbc.SarComprobanteAdjuntoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SarComprobanteAdjunto"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page import="com.google.gson.Gson"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String msgError="";
    /**
     * Modos:
     *  1: Registrar Adjunto
     *  2: ...
     */
    int mode=-1;
    try{
        mode = Integer.parseInt(request.getParameter("mode"));
    }catch(NumberFormatException e){}
    
    //Gson gson = new Gson();
    
    //GenericValidator genericValidator = new GenericValidator();
    
    //Configuration appConfig = new Configuration();
    
    //int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
     * Procesamiento
     */
    if (mode==-1){
        msgError = "No se eligio una acción correcta. Reintente el llenado de los datos.";
    }else if (mode==1){
        //1 = Registrar Adjunto
    
        int idComprobanteFiscal = 0;
        int idPedido = 0;
        int idValeAzul = 0;
        int idCxpComprobanteFiscal = 0;
        String[] archivosAdjuntos = new String[0];
        try {
            idComprobanteFiscal = Integer.parseInt(request.getParameter("idComprobanteFiscal"));
        } catch (NumberFormatException e) {}
        try {
            idPedido = Integer.parseInt(request.getParameter("idPedido"));
        } catch (NumberFormatException e) {}
        try {
            idValeAzul = Integer.parseInt(request.getParameter("id_cxp_vale_azul"));
        } catch (NumberFormatException e) {}
        try {
            idCxpComprobanteFiscal = Integer.parseInt(request.getParameter("id_cxp_comprobante_fiscal"));
        } catch (NumberFormatException e) {}
        archivosAdjuntos = request.getParameterValues("archivo_adjunto");
        
        if (idComprobanteFiscal>0 || idPedido>0 || idValeAzul>0 || idCxpComprobanteFiscal>0){
            if (archivosAdjuntos==null){
                msgError += "<ul>No se eligio ningun archivo para adjuntar.";
            }else if (archivosAdjuntos.length<=0){
                msgError += "<ul>No se eligio ningun archivo para adjuntar.";
            }else{
                SarComprobanteAdjunto sarAdjunto;
                for(int i=0;i<archivosAdjuntos.length;i++){
                    sarAdjunto = new SarComprobanteAdjunto();
                    sarAdjunto.setIdComprobanteFiscal(idComprobanteFiscal);
                    sarAdjunto.setIdPedido(idPedido);
                    sarAdjunto.setIdCxpValeAzul(idValeAzul);
                    sarAdjunto.setIdCxpComprobanteFiscal(idCxpComprobanteFiscal);
                    sarAdjunto.setNombreArchivo(archivosAdjuntos[i]);

                    try{
                        new SarComprobanteAdjuntoDaoImpl().insert(sarAdjunto);
                    }catch(Exception ex){
                        ex.printStackTrace();
                        msgError += "<ul>Un Archivo adjunto no ha sido almacenado en base de datos exitosamente: " + ex.toString();
                    }
                }
            }
        }else{
            msgError += "<ul> No se especifico a que comprobante se adjuntara el Archivo.";
        }
    
    }
    
    if (msgError.equals("")){
        out.print("<!--EXITO-->");
    }
%>
<%
    if (!msgError.equals("")){
        out.print("<!--ERROR-->"+msgError);
    }
%>