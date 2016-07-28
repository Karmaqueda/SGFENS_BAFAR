<%-- 
    Document   : catBanco_ajax
    Created on : 12/12/2014, 11:42:49 AM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.dao.jdbc.SgfensBancoDaoImpl"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page import="com.tsp.sct.bo.SGBancoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensBanco"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idSgfensBanco = -1;
    String nombreSgfensBanco ="";
    String numSucursalBanco ="";
    String numCuentaBanco = "";
    String clabeBanco = "";
    String comentariosBanco = "";
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idSgfensBanco = Integer.parseInt(request.getParameter("idSgfensBanco"));
    }catch(NumberFormatException ex){}
    nombreSgfensBanco = request.getParameter("nombreSgfensBanco")!=null?new String(request.getParameter("nombreSgfensBanco").getBytes("ISO-8859-1"),"UTF-8"):"";
    numSucursalBanco = request.getParameter("numSucursalBanco")!=null?new String(request.getParameter("numSucursalBanco").getBytes("ISO-8859-1"),"UTF-8"):"";    
    numCuentaBanco = request.getParameter("numCuentaBanco")!=null?new String(request.getParameter("numCuentaBanco").getBytes("ISO-8859-1"),"UTF-8"):"";    
    clabeBanco = request.getParameter("clabeBanco")!=null?new String(request.getParameter("clabeBanco").getBytes("ISO-8859-1"),"UTF-8"):"";    
    comentariosBanco = request.getParameter("comentariosBanco")!=null?new String(request.getParameter("comentariosBanco").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombreSgfensBanco, 1, 100))
        msgError += "<ul>El dato 'nombre' es requerido.";    
    if(idSgfensBanco <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'sgfensBanco' es requerido";
   
    
    if(msgError.equals("")){
        if(idSgfensBanco>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                SGBancoBO sgfensBancoBO = new SGBancoBO(idSgfensBanco,user.getConn());
                SgfensBanco sgfensBancoDto = sgfensBancoBO.getSgfensBanco();
                
                sgfensBancoDto.setIdEstatus(estatus);
                sgfensBancoDto.setNombreBanco(nombreSgfensBanco);
                sgfensBancoDto.setNumeroSucursal(numSucursalBanco);
                sgfensBancoDto.setNumeroCuenta(numCuentaBanco);
                sgfensBancoDto.setClabe(clabeBanco);
                sgfensBancoDto.setComentarios(comentariosBanco);
                
                try{
                    new SgfensBancoDaoImpl(user.getConn()).update(sgfensBancoDto.createPk(), sgfensBancoDto);

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
                SgfensBanco sgfensBancoDto = new SgfensBanco();
                SgfensBancoDaoImpl sgfensBancosDaoImpl = new SgfensBancoDaoImpl(user.getConn());
                
                sgfensBancoDto.setIdEstatus(estatus);
                sgfensBancoDto.setNombreBanco(nombreSgfensBanco);
                sgfensBancoDto.setNumeroSucursal(numSucursalBanco);
                sgfensBancoDto.setNumeroCuenta(numCuentaBanco);
                sgfensBancoDto.setClabe(clabeBanco);
                sgfensBancoDto.setComentarios(comentariosBanco);                              
                sgfensBancoDto.setIdEmpresa(idEmpresa);

                /**
                 * Realizamos el insert
                 */
                sgfensBancosDaoImpl.insert(sgfensBancoDto);

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