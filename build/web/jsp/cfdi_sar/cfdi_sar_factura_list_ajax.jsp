<%-- 
    Document   : cfdi_sar_factura_list_ajax
    Created on : 23/03/2015, 01:30:20 PM
    Author     : ISCesar
--%>
<%@page import="com.tsp.sct.dao.jdbc.SarComprobanteDaoImpl"%>
<%@page import="com.tsp.workflow.ws.EditaComprobanteRequest"%>
<%@page import="com.tsp.workflow.ws.RegistraComprobanteResponse"%>
<%@page import="com.tsp.workflow.ws.AccesoRequest"%>
<%@page import="com.tsp.sct.dao.dto.SarUsuarioProveedor"%>
<%@page import="com.tsp.sct.bo.SarUsuarioProveedorBO"%>
<%@page import="com.tsp.sct.bo.ConexionSARBO"%>
<%@page import="com.tsp.sct.dao.dto.SarComprobante"%>
<%@page import="com.tsp.sct.bo.SarComprobanteBO"%>
<%@page import="com.tsp.sct.bo.EstatusComprobanteBO"%>
<%@page import="com.tsp.sct.bo.ComprobanteFiscalBO"%>
<%@page import="com.tsp.sgfens.sesion.ComprobanteFiscalSesion"%>
<%@page import="com.tsp.sct.dao.dto.SarAreaEntrega"%>
<%@page import="com.tsp.sct.bo.SarAreaEntregaBO"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page import="com.google.gson.Gson"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%

    String msgError="";
    /**
     * Modos:
     *  1: Recuperar Cliente  relacionado a Area de Entrega SAR
     *  2: Cancelar Comprobante (CFDI y Registro SAR)
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
        //1 = Recuperar Cliente  relacionado a Area de Entrega SAR
        
        int idSarAreaEntrega = 0;
        try{ idSarAreaEntrega = Integer.parseInt(request.getParameter("sar_id_area_entrega")); }catch(Exception e){}                
        
        SarAreaEntrega sarAreaEntregaDto = new SarAreaEntregaBO(idSarAreaEntrega, user.getConn()).getSarAreaEntrega();
        if (sarAreaEntregaDto!=null){
            idCliente = sarAreaEntregaDto.getIdCliente();
            
            Cliente clienteDto = new ClienteBO(idCliente,user.getConn()).getCliente();
        
            if (clienteDto!=null){
                
                String jsonOutput = gson.toJson(clienteDto);
                out.print("<!--EXITO-->"+jsonOutput);
            }else{
                msgError += "<ul>No hay un Cliente asignado al Área, configure primero. <ul>Si selecciona un cliente de forma manual esta aceptando responsabilidad en caso de error al entregar a SAR.";
            }
            
        }else{
            msgError += "<ul>Area de Entrega no válida, no se encontro en sistema.";
        }
        
    }else if (mode==2){
        //2 = Cancelar Comprobante (CFDI y Registro SAR)
        
        //Parametros
        int id_comprobanteFiscal = -1;
        try{ id_comprobanteFiscal = Integer.parseInt(request.getParameter("id_comprobanteFiscal")); }catch(Exception e){}
        
        //Procesamiento
        if (id_comprobanteFiscal>=0){
            ComprobanteFiscalBO comprobanteFiscalBO = new ComprobanteFiscalBO(id_comprobanteFiscal,user.getConn());
            SarComprobanteBO sarComprobanteBO = new SarComprobanteBO(id_comprobanteFiscal, user.getConn());
            SarComprobante sarComprobanteDto = sarComprobanteBO.getSarComprobante();
            
            boolean cancelaFiscal = false;
            boolean cancelaSAR = false;
            
            if (comprobanteFiscalBO.getComprobanteFiscal().getIdEstatus()==EstatusComprobanteBO.ESTATUS_ENTREGADA){
                try{
                    if (comprobanteFiscalBO.cancelaComprobanteFiscal()){
                        cancelaFiscal = true;
                    }else{
                        msgError += "<ul> La cancelación fiscal (SAT) no se ha completado correctamente. Intente de nuevo.";
                    }
                }catch(Exception ex){
                    msgError += "<ul> Error al cancelar Fiscalmente (SAT) comprobante. " + ex.toString();
                }
            }
            
            if (sarComprobanteDto.getExtSarIdEstatus()==SarComprobanteBO.FACTURA_EN_FLUJO
                    || sarComprobanteDto.getExtSarIdEstatus()==SarComprobanteBO.FACTURA_EN_FLUJO_REENVIADA){
                try{
                    SarUsuarioProveedorBO sarUsuarioProveedorBO = new SarUsuarioProveedorBO(sarComprobanteDto.getIdSarUsuario(), user.getConn());
                    SarUsuarioProveedor sarUsuarioProveedorDto = sarUsuarioProveedorBO.getSarUsuarioProveedor();
            
                    //Consultamos la bitacora de la factura
                    AccesoRequest accesoRequest = new AccesoRequest();
                    accesoRequest.setUsuario(sarUsuarioProveedorDto.getExtSarUsuario());
                    accesoRequest.setContrasena(sarUsuarioProveedorDto.getExtSarPass());
                    
                    EditaComprobanteRequest editaComprobanteRequest = new EditaComprobanteRequest();
                    editaComprobanteRequest.setIdFactura(sarComprobanteDto.getExtSarIdFactura());
                    editaComprobanteRequest.setComentarios("Cancelada por proveedor desde Pretoriano Soft.");
                    editaComprobanteRequest.setCancelarComprobante(true);
        
                    RegistraComprobanteResponse respWS = ConexionSARBO.editComprobante(accesoRequest, editaComprobanteRequest);
                    
                    if (respWS.isError()){
                        msgError += "<ul>El sistema SAR no permitio la cancelación: " + respWS.getNumError() + " - " + respWS.getMsgError();
                    }else{
                        //cancelacion exitosa en SAR
                        sarComprobanteDto.setExtSarIdEstatus((int)respWS.getIdEstatus());
                        sarComprobanteDto.setExtSarDescEstatus(respWS.getDescripcionEstatus());
                        sarComprobanteDto.setExtSarUltimoComentario(respWS.getUltimoComentarioBitacora());
                        sarComprobanteDto.setNombreArchivoAcuse(null);
                        
                        new SarComprobanteDaoImpl(user.getConn()).update(sarComprobanteDto.createPk(),sarComprobanteDto);
                        
                        cancelaSAR = true;
                    }
                    
                }catch(Exception ex){
                    msgError += "<ul> Error al cancelar en sistema SAR. " + ex.toString();
                }
            }
            
            if (cancelaFiscal || cancelaSAR){
                out.print("<!--EXITO-->");
                if (cancelaFiscal){
                    out.print("<b>Cancelación Fiscal realizada con éxito.</b><br/>");
                }
                if (cancelaSAR){
                    out.print("<b>Cancelación en sistema SAR realizada con éxito.</b><br/>");
                }
            }
        }else{
            msgError += "<ul> No se específico el identificador (ID) del Comprobante Fiscal a cancelar. Intente de nuevo.";
        }
        
    }
    
    if (msgError.equals("") && mode!=1
            && mode!=2){
        out.print("<!--EXITO-->");
    }
%>
<%
    if (!msgError.equals("")){
        out.print("<!--ERROR-->"+msgError);
    }
%>