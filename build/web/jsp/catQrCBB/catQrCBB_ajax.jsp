<%-- 
    Document   : catQrCBB_ajax
    Created on : 27/05/2013, 04:22:09 PM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.CbbBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.CbbDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Cbb"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idCbb = -1;
    String nombreCbb ="";
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idCbb = Integer.parseInt(request.getParameter("idCbb"));
    }catch(NumberFormatException ex){}
    nombreCbb = request.getParameter("nombreArchivoImagen")!=null?new String(request.getParameter("nombreArchivoImagen").getBytes("ISO-8859-1"),"UTF-8"):"";
    
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombreCbb, 1, 300))
        msgError += "<ul>El dato 'Archivo QR' es requerido.";
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */                    

    if(msgError.equals("")){
        if(idCbb>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                /*CbbBO cbbBO = new CbbBO(idCbb);
                Cbb cbbDto = cbbBO.getCbb();
                
                //cbbDto.setIdEstatus(estatus);
                cbbDto.setNombreCer(nombreCbb);
                cbbDto.setNombreKey(nombreKeyDigital);
                cbbDto.setNoCbb(bo.getCsd().getNoCbb());
                cbbDto.setFechaCaducidad(bo.getCsd().getFechaCaducidad());
                cbbDto.setPassword(pass);
                
               
                
                try{
                    new CbbDaoImpl(user.getConn()).update(cbbDto.createPk(), cbbDto);

                    out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                    ex.printStackTrace();
                }*/
                out.print("<!--ERROR-->Acción no válida.");
            }else{
                out.print("<!--ERROR-->Acción no válida.");
            }
        }else{
            /*
            *  Nuevo
            */
            
            try {                
                
                /**
                 * Creamos el registro de Cbb
                 */
                Cbb cbbDto = new Cbb();
                CbbDaoImpl cbbesDigitalesDaoImpl = new CbbDaoImpl(user.getConn());
                
                int idCbbNuevo;
                Cbb ultimoRegistroCbb = cbbesDigitalesDaoImpl.findLast();
                idCbbNuevo = ultimoRegistroCbb.getIdCbb() + 1;
                
                //cbbDto.setIdEstatus(estatus);
                cbbDto.setIdCbb(idCbbNuevo);
                cbbDto.setNombreCbb(nombreCbb);
                cbbDto.setIdEmpresa(idEmpresa);                
                
                /**
                 * Realizamos el insert
                 */
                cbbesDigitalesDaoImpl.insert(cbbDto);

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
