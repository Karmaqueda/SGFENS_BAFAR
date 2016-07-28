<%-- 
    Document   : catClientesVisitasMensaje_ajax
    Created on : 4/11/2014, 11:35:27 AM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.dao.jdbc.EmpresaDaoImpl"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */    
    String mensajePersonalizadoTicketEmpresa ="";
   
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";   
    mensajePersonalizadoTicketEmpresa = request.getParameter("mensajePersonalizadoTicketEmpresa")!=null?new String(request.getParameter("mensajePersonalizadoTicketEmpresa").getBytes("ISO-8859-1"),"UTF-8"):"";
        
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(mensajePersonalizadoTicketEmpresa, 1, 300))
        msgError += "<ul>El dato 'mensaje personalizado' es requerido.";
    
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idEmpresa>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                EmpresaBO empresaBO = new EmpresaBO(idEmpresa,user.getConn());
                Empresa empresaDto = empresaBO.getEmpresa();
                
                empresaDto.setMensajePersonalizadoVisita(mensajePersonalizadoTicketEmpresa);
                
                try{
                    new EmpresaDaoImpl(user.getConn()).update(empresaDto.createPk(), empresaDto);

                    out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                    ex.printStackTrace();
                }
                
            }else{
                out.print("<!--ERROR-->Acción no válida.");
            }
        }else{
            /*
            *  Nuevo
            */            
        }
    }else{
        out.print("<!--ERROR-->"+msgError);
    }

%>