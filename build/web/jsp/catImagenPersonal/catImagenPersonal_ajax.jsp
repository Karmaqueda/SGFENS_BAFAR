<%-- 
    Document   : catImagenPersonal_ajax
    Created on : 29/11/2012, 06:27:30 PM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.ImagenPersonalBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.ImagenPersonalDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.ImagenPersonal"%>
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
    int idImagenPersonal = -1;
    String nombreImagenPersonal ="";
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idImagenPersonal = Integer.parseInt(request.getParameter("idImagenPersonal"));
    }catch(NumberFormatException ex){}
    nombreImagenPersonal = request.getParameter("nombreArchivoImagen")!=null?new String(request.getParameter("nombreArchivoImagen").getBytes("ISO-8859-1"),"UTF-8"):"";
    
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombreImagenPersonal, 1, 300))
        msgError += "<ul>El dato 'Archivo Imagen Personal' es requerido.";
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */                    

    if(msgError.equals("")){
        if(idImagenPersonal>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                /*ImagenPersonalBO imagenPersonalBO = new ImagenPersonalBO(idImagenPersonal);
                ImagenPersonal imagenPersonalDto = imagenPersonalBO.getImagenPersonal();
                
                //imagenPersonalDto.setIdEstatus(estatus);
                imagenPersonalDto.setNombreCer(nombreImagenPersonal);
                imagenPersonalDto.setNombreKey(nombreKeyDigital);
                imagenPersonalDto.setNoImagenPersonal(bo.getCsd().getNoImagenPersonal());
                imagenPersonalDto.setFechaCaducidad(bo.getCsd().getFechaCaducidad());
                imagenPersonalDto.setPassword(pass);
                
               
                
                try{
                    new ImagenPersonalDaoImpl(user.getConn()).update(imagenPersonalDto.createPk(), imagenPersonalDto);

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
                 * Creamos el registro de ImagenPersonal
                 */
                ImagenPersonal imagenPersonalDto = new ImagenPersonal();
                ImagenPersonalDaoImpl imagenPersonalesDigitalesDaoImpl = new ImagenPersonalDaoImpl(user.getConn());
                
                int idImagenPersonalNuevo;
                ImagenPersonal ultimoRegistroImagenPersonal = imagenPersonalesDigitalesDaoImpl.findLast();
                idImagenPersonalNuevo = ultimoRegistroImagenPersonal.getIdImagenPersonal() + 1;
                
                //imagenPersonalDto.setIdEstatus(estatus);
                imagenPersonalDto.setIdImagenPersonal(idImagenPersonalNuevo);
                imagenPersonalDto.setNombreImagen(nombreImagenPersonal);
                imagenPersonalDto.setIdEmpresa(idEmpresa);                
                
                /**
                 * Realizamos el insert
                 */
                imagenPersonalesDigitalesDaoImpl.insert(imagenPersonalDto);

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
