<%-- 
    Document   : catCamposAdicionales_ajax
    Created on : 27/07/2015, 02:02:38 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.util.test.qryAlmacenProductos"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.ClienteCampoAdicionalBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.ClienteCampoAdicionalDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.ClienteCampoAdicional"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idClienteCampoAdicional = -1;
    String nombreClienteCampoAdicional ="";
    int idTipoCampo = -1;
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idClienteCampoAdicional = Integer.parseInt(request.getParameter("idClienteCampoAdicional"));
    }catch(NumberFormatException ex){}
    nombreClienteCampoAdicional = request.getParameter("nombreClienteCampoAdicional")!=null?new String(request.getParameter("nombreClienteCampoAdicional").getBytes("ISO-8859-1"),"UTF-8"):"";
    try{
        idTipoCampo = Integer.parseInt(request.getParameter("idTipoCampo"));
    }catch(NumberFormatException ex){} 
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombreClienteCampoAdicional, 1, 50))
        msgError += "<ul>El dato 'nombre' es requerido.";
    if(idTipoCampo < 0)
        msgError += "<ul>El dato 'Tipo' es requerido";   
    if(idClienteCampoAdicional <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'ClienteCampoAdicional' es requerido";
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */
    
    ///validamos que no sean mas de 3 atributos dados de alta:
    if(idClienteCampoAdicional < 0){
     ClienteCampoAdicional[] clienteCampoAdicionalsDto = new ClienteCampoAdicional[0];
     ClienteCampoAdicionalBO clienteCampoAdicionalBO1 = new ClienteCampoAdicionalBO(user.getConn());
     try{
         clienteCampoAdicionalsDto = clienteCampoAdicionalBO1.findClienteCampoAdicionals(0, idEmpresa , 0, 0, " AND ID_ESTATUS = 1 ");
         if(clienteCampoAdicionalsDto.length >= 3){
              msgError += "<ul>No puedes crear mas de 3 campos personalizados.";
         }
     }catch(Exception e){}     
    }

    if(msgError.equals("")){
        if(idClienteCampoAdicional>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                ClienteCampoAdicionalBO clienteCampoAdicionalBO = new ClienteCampoAdicionalBO(idClienteCampoAdicional,user.getConn());
                ClienteCampoAdicional clienteCampoAdicionalDto = clienteCampoAdicionalBO.getClienteCampoAdicional();
                
                clienteCampoAdicionalDto.setIdEstatus(estatus);
                clienteCampoAdicionalDto.setLabelNombre(nombreClienteCampoAdicional);
                clienteCampoAdicionalDto.setTipoLabel(idTipoCampo);
                clienteCampoAdicionalDto.setIdEmpresa(idEmpresa);
               
                
                try{
                    new ClienteCampoAdicionalDaoImpl(user.getConn()).update(clienteCampoAdicionalDto.createPk(), clienteCampoAdicionalDto);

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
                
                // Peligro!!!
                // No quitar comentario , esto se ejecutara solamente una vez
                //cuando se libere lo de almacenes, posteriormente se elmininara    02/07/2015
                
                //qryAlmacenProductos.queryprods();
                
                //------------------------------------
                
                
                /**
                 * Creamos el registro de Cliente
                 */
                ClienteCampoAdicional clienteCampoAdicionalDto = new ClienteCampoAdicional();
                ClienteCampoAdicionalDaoImpl clienteCampoAdicionalsDaoImpl = new ClienteCampoAdicionalDaoImpl(user.getConn());
                
                clienteCampoAdicionalDto.setIdEstatus(estatus);
                clienteCampoAdicionalDto.setLabelNombre(nombreClienteCampoAdicional);
                clienteCampoAdicionalDto.setTipoLabel(idTipoCampo);
                clienteCampoAdicionalDto.setIdEmpresa(idEmpresa);

                /**
                 * Realizamos el insert
                 */
                clienteCampoAdicionalsDaoImpl.insert(clienteCampoAdicionalDto);

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