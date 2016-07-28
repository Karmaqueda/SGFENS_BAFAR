<%-- 
    Document   : catNominas_ajax
    Created on : 5/12/2013, 03:45:22 PM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.dao.jdbc.EmpresaDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page import="java.util.regex.Matcher"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    /*
     * mode:
     * 1: Edición de numero de Registro patronal.
     * 
     */
    
    int idEmpresaPadre = -1;  
    String registroPatronalEmpresa = "";
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idEmpresaPadre = Integer.parseInt(request.getParameter("idEmpresaPadre"));
    }catch(NumberFormatException ex){}
    registroPatronalEmpresa = request.getParameter("registroPatronalEmpresa")!=null?new String(request.getParameter("registroPatronalEmpresa").getBytes("ISO-8859-1"),"UTF-8"):"";
        
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(registroPatronalEmpresa, 11, 11))
        msgError += "<ul>El dato 'Registro Patronal' no es de longitud valida.";
    if(!gc.isRegistroPatronal(registroPatronalEmpresa))
        msgError += "<ul>El Registro Patronal no cumple con el estantar.";
    

    if(msgError.equals("")){
        if(idEmpresaPadre>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                EmpresaBO empresaBO = new EmpresaBO(idEmpresaPadre,user.getConn());
                Empresa empresa = empresaBO.getEmpresa();
                
                empresa.setRegistroPatronal(registroPatronalEmpresa);
                try{
                    new EmpresaDaoImpl(user.getConn()).update(empresa.createPk(), empresa);
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
            
            try {                
                
                /**
                 * Creamos el registro de Cliente
                 */
              

                /**
                 * Realizamos el insert
                 */
                 //new EmpresaDaoImpl(user.getConn()).insert(empresa);

                out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
            
            }catch(Exception e){
                e.printStackTrace();
                msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
            }
        }
    }else{
        out.print("<!--ERROR-->"+msgError);
    }

%>
