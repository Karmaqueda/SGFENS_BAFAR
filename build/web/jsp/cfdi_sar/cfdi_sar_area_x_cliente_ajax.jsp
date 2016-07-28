<%-- 
    Document   : cfdi_sar_area_x_cliente_ajax
    Created on : 11/04/2015, 06:50:02 PM
    Author     : ISCesar
--%>
<%@page import="com.tsp.sct.dao.jdbc.SarAreaEntregaDaoImpl"%>
<%@page import="com.tsp.sct.bo.SarAreaEntregaBO"%>
<%@page import="com.tsp.sct.dao.dto.SarAreaEntrega"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page import="com.google.gson.Gson"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String msgError="";
    /**
     * Modos:
     *  1: Registrar Relacion Area x Cliente
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
    
        int idSarAreaEntrega = 0;
        int idCliente = 0;
        try {
            idSarAreaEntrega = Integer.parseInt(request.getParameter("id_sar_area_entrega"));
        } catch (NumberFormatException e) {}
        try {
            idCliente = Integer.parseInt(request.getParameter("id_cliente"));
        } catch (NumberFormatException e) {}
        
        if (idSarAreaEntrega>0 && idCliente>0){
            SarAreaEntrega sarAreaDto = new SarAreaEntregaBO(idSarAreaEntrega, user.getConn()).getSarAreaEntrega();
            if (sarAreaDto == null){
                msgError += "<ul> El área indicada no existe en BD.";
            }else{
                sarAreaDto.setIdCliente(idCliente);
                try{
                    new SarAreaEntregaDaoImpl(user.getConn()).update(sarAreaDto.createPk(), sarAreaDto);
                }catch(Exception ex){
                    msgError += "<ul>Error al actualizar Area : " + ex.toString();
                }
            }
        }else{
            msgError += "<ul> No se especifico que área o cliente se desea relacionar.";
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