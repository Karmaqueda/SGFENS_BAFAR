<%-- 
    Document   : cxp_comprobantes_list_ajax
    Created on : 20/04/2015, 20/04/2015 12:05:05 PM
    Author     : ISCesar
--%>
<%@page import="com.tsp.sct.dao.jdbc.CxpValeAzulDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CxpValeAzul"%>
<%@page import="com.tsp.sct.dao.jdbc.CxpComprobanteFiscalDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CxpComprobanteFiscal"%>
<%@page import="com.tsp.sct.bo.CxpValeAzulBO"%>
<%@page import="com.tsp.sct.bo.CxpComprobanteFiscalBO"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%

    String msgError="";
    /**
     * Modos:
     *  1: Marcar CxP Comprobante Fiscal Pagado
     *  2: Marcar Vale Azul Pagado
     *  3: Cancelar CxP Comprobante Fiscal
     *  4: Cancelar Vale Azul
     */
    int mode=-1;
    try{
        mode = Integer.parseInt(request.getParameter("mode"));
    }catch(NumberFormatException e){}
    
    int idCliente = -1;
    Gson gson = new Gson();
    
    GenericValidator genericValidator = new GenericValidator();
    
    Configuration appConfig = new Configuration();
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
     * Procesamiento
     */
    if (mode==-1){
        msgError = "No se eligio una acción correcta. Reintente el llenado de los datos.";
    }else if (mode==1){
        //1 = Marcar CxP Comprobante Fiscal Pagado
        
        int idCxPComprobanteFiscal = 0;
        try{ idCxPComprobanteFiscal = Integer.parseInt(request.getParameter("id_cxp_comprobante_fiscal")); }catch(Exception e){}
        
        CxpComprobanteFiscalBO cxpComprobanteFiscalBO = new CxpComprobanteFiscalBO(idCxPComprobanteFiscal, user.getConn());
        if (idCxPComprobanteFiscal>0){
        
            try{
                cxpComprobanteFiscalBO.marcarComprobantePagadoTotal();
            }catch(Exception ex){
                msgError += "<ul>Error al marcar Pagado: " + ex.toString();
            }
            
        }else{
            msgError += "<ul>No se específico que CxP Comprobante Fiscal se marcará como Pagado.";
        }
        
    }else if (mode==2){
        //2 = Marcar Vale Azul Pagado        
        
        int idCxValeAzul = 0;
        try{ idCxValeAzul = Integer.parseInt(request.getParameter("id_cxp_vale_azul")); }catch(Exception e){}
        
        CxpValeAzulBO cxpValeAzulBO = new CxpValeAzulBO(idCxValeAzul, user.getConn());
        if (idCxValeAzul>0){
        
            try{
                cxpValeAzulBO.marcarValeAzulPagadoTotal();
            }catch(Exception ex){
                msgError += "<ul>Error al marcar Pagado: " + ex.toString();
            }
            
        }else{
            msgError += "<ul>No se específico que CxP Vale Azul se marcará como Pagado.";
        }
        
    }else if (mode==3){
        //3 = Cancelar CxP Comprobante Fiscal
        
        int idCxPComprobanteFiscal = 0;
        try{ idCxPComprobanteFiscal = Integer.parseInt(request.getParameter("id_cxp_comprobante_fiscal")); }catch(Exception e){}
        
        CxpComprobanteFiscalBO cxpComprobanteFiscalBO = new CxpComprobanteFiscalBO(idCxPComprobanteFiscal, user.getConn());
        if (idCxPComprobanteFiscal>0){
            CxpComprobanteFiscal cxpComprobanteFiscalDto = cxpComprobanteFiscalBO.getCxpComprobanteFiscal();
            if (cxpComprobanteFiscalDto!=null){
                cxpComprobanteFiscalDto.setIdEstatus(2); //Cancelado/Inactivo
                try{
                    new CxpComprobanteFiscalDaoImpl(user.getConn()).update(cxpComprobanteFiscalDto.createPk(), cxpComprobanteFiscalDto);
                }catch(Exception ex){
                   msgError+= "Error inesperado al actualizar registro: " + ex.toString(); 
                }
            }else{
                msgError += "<ul>Error al marcar Cancelado. No se encontro en base de datos el registro.";
            }
            
        }else{
            msgError += "<ul>No se específico que CxP Comprobante Fiscal se marcará como Cancelado.";
        }
        
    }else if (mode==4){
        //4 = Cancelar Vale Azul
        
        int idCxValeAzul = 0;
        try{ idCxValeAzul = Integer.parseInt(request.getParameter("id_cxp_vale_azul")); }catch(Exception e){}
        
        CxpValeAzulBO cxpValeAzulBO = new CxpValeAzulBO(idCxValeAzul, user.getConn());
        if (idCxValeAzul>0){
            CxpValeAzul cxpValeAzulDto = cxpValeAzulBO.getCxpValeAzul();
            if (cxpValeAzulDto!=null){
                cxpValeAzulDto.setIdEstatus(2); //Cancelado/Inactivo
                try{
                    new CxpValeAzulDaoImpl(user.getConn()).update(cxpValeAzulDto.createPk(), cxpValeAzulDto);
                }catch(Exception ex){
                   msgError+= "Error inesperado al actualizar registro: " + ex.toString(); 
                }
            }else{
                msgError += "<ul>Error al marcar Cancelado. No se encontro en base de datos el registro.";
            }
            
        }else{
            msgError += "<ul>No se específico que CxP Vale Azul se marcará como Cancelado..";
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