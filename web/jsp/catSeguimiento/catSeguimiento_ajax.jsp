<%-- 
    Document   : cat_Seguimiento_ajax
    Created on : 23/05/2013, 07:07:07 PM
    Author     : Leonardo
--%>

<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.PosMovilEstatusParametrosBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.jdbc.PosMovilEstatusParametrosDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.PosMovilEstatusParametros"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idPosMovilEstatusParametros = -1;   
    int minutosPosMovil = 0;
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idPosMovilEstatusParametros = Integer.parseInt(request.getParameter("idPosMovilEstatusParametros"));
    }catch(NumberFormatException ex){}
    
    try{
        minutosPosMovil = Integer.parseInt(request.getParameter("minutosPosMovil"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();        
    if(idPosMovilEstatusParametros <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'posMovilEstatusParametros' es requerido";
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idPosMovilEstatusParametros>0){
            //if (mode.equals("1")){
                System.out.println("-------------- Datos EDICION!!!!!!!!!!!!!");
            /*
            * Editar
            */
                PosMovilEstatusParametrosBO posMovilEstatusParametrosBO = new PosMovilEstatusParametrosBO(idPosMovilEstatusParametros,user.getConn());
                PosMovilEstatusParametros posMovilEstatusParametrosDto = posMovilEstatusParametrosBO.getPosMovilEstatusParametros();
                
                posMovilEstatusParametrosDto.setIdEmpresa(idEmpresa);
                posMovilEstatusParametrosDto.setIdEmpleado(0);
                posMovilEstatusParametrosDto.setTiempoMinutosActualiza(minutosPosMovil);               
                
                try{
                    new PosMovilEstatusParametrosDaoImpl(user.getConn()).update(posMovilEstatusParametrosDto.createPk(), posMovilEstatusParametrosDto);

                    out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                    ex.printStackTrace();
                }
                
            /*}else{
                out.print("<!--ERROR-->Acción no válida.");
            }*/
        }else{
            /*
            *  Nuevo
            */
            
            try {      
                System.out.println("-------------- Datos nuevos!!!!!!!!!");
                /**
                 * Creamos el registro de Cliente
                 */
                PosMovilEstatusParametros posMovilEstatusParametrosDto = new PosMovilEstatusParametros();
                PosMovilEstatusParametrosDaoImpl posMovilEstatusParametrossDaoImpl = new PosMovilEstatusParametrosDaoImpl(user.getConn());
                
                posMovilEstatusParametrosDto.setIdEmpresa(idEmpresa);
                posMovilEstatusParametrosDto.setIdEmpleado(0);
                posMovilEstatusParametrosDto.setTiempoMinutosActualiza(minutosPosMovil);

                /**
                 * Realizamos el insert
                 */
                posMovilEstatusParametrossDaoImpl.insert(posMovilEstatusParametrosDto);

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