<%-- 
    Document   : registraNotaAjax
    Created on : 7/04/2015, 06:50:02 PM
    Author     : ISCesar
--%>
<%@page import="com.tsp.sct.dao.jdbc.CxcNotaDaoImpl"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.dto.CxcNota"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page import="com.google.gson.Gson"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String msgError="";
    /**
     * Modos:
     *  1: Registrar Nota
     *  2: ...
     */
    int mode=-1;
    try{
        mode = Integer.parseInt(request.getParameter("mode"));
    }catch(NumberFormatException e){}
    
    //Gson gson = new Gson();
    
    GenericValidator genericValidator = new GenericValidator();
    
    //Configuration appConfig = new Configuration();
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
     * Procesamiento
     */
    if (mode==-1){
        msgError = "No se eligio una acción correcta. Reintente el llenado de los datos.";
    }else if (mode==1){
        //1 = Registrar Nota
    
        int idComprobanteFiscal = 0;
        int idPedido = 0;
        int idValeAzul = 0;
        int idCxpComprobanteFiscal = 0;
        int idCrCredCliente = 0;
        String nota = "";
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
        try {
            idCrCredCliente = Integer.parseInt(request.getParameter("idCrCredCliente"));
        } catch (NumberFormatException e) {}
        nota = request.getParameter("nota")!=null?new String(request.getParameter("nota").getBytes("ISO-8859-1"),"UTF-8"):"";
        
        if (idComprobanteFiscal>0 || idPedido>0 || idValeAzul>0 || idCxpComprobanteFiscal>0
                || idCrCredCliente>0 ){
            if (!genericValidator.isValidString(nota, 1, 500)){
                msgError += "<ul>La nota no puede estar vacia ni ser mayor a 500 caracteres.";
            }else{
                CxcNota notaDto = new CxcNota();
                notaDto.setIdEmpresa(idEmpresa);
                notaDto.setIdUsuario(user.getUser().getIdUsuarios());
                notaDto.setIdComprobanteFiscal(idComprobanteFiscal);
                notaDto.setIdPedido(idPedido);
                notaDto.setIdCxpValeAzul(idValeAzul);
                notaDto.setIdCxpComprobanteFiscal(idCxpComprobanteFiscal);
                notaDto.setIdCrCredCliente(idCrCredCliente);
                notaDto.setNota(nota);
                notaDto.setFechaHoraCaptura(new Date());
                notaDto.setIdEstatus(1);
                
                try{
                    new CxcNotaDaoImpl(user.getConn()).insert(notaDto);
                }catch(Exception ex){
                    msgError += "Error al registrar Nota : " + ex.toString();
                }
            }
        }else{
            msgError += "<ul> No se especifico a que comprobante se adjuntara la Nota de texto.";
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