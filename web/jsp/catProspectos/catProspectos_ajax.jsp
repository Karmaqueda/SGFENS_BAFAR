<%-- 
    Document   : catProspectos_ajax
    Created on : 06-nov-2012, 13:48:45
    Author     : ISCesarMartinez poseidon24@hotmail.com
--%>

<%@page import="com.tsp.sct.bo.BitacoraCreditosOperacionBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensProspectoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProspecto"%>
<%@page import="com.tsp.sct.bo.SGProspectoBO"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idProspecto = -1;
    String razonSocial ="";
    String descripcion="";
    
    String lada="";
    String telefono="";
    String celular="";
    String email="";
    String contacto="";
    
    int idVendedor = -1;
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idProspecto = Integer.parseInt(request.getParameter("idProspecto"));
    }catch(NumberFormatException ex){}
    razonSocial = request.getParameter("razonSocial")!=null?new String(request.getParameter("razonSocial").getBytes("ISO-8859-1"),"UTF-8"):"";
    descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";
    lada = request.getParameter("lada")!=null?new String(request.getParameter("lada").getBytes("ISO-8859-1"),"UTF-8"):"";
    telefono = request.getParameter("telefono")!=null?new String(request.getParameter("telefono").getBytes("ISO-8859-1"),"UTF-8"):"";
    celular = request.getParameter("celular")!=null?new String(request.getParameter("celular").getBytes("ISO-8859-1"),"UTF-8"):"";
    email = request.getParameter("email")!=null?new String(request.getParameter("email").getBytes("ISO-8859-1"),"UTF-8"):"";
    contacto = request.getParameter("contacto")!=null?new String(request.getParameter("contacto").getBytes("ISO-8859-1"),"UTF-8"):"";
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}
    try{
        idVendedor = Integer.parseInt(request.getParameter("idVendedor"));
    }catch(NumberFormatException ex){}
    
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();
    if(!gc.isValidString(razonSocial, 1, 200))
        msgError += "<ul>El dato 'razon Social' es requerido.";
    /*if(!gc.isValidString(numeroProspecto, 1, 15))
        msgError += "<ul>El dato 'nombre' es requerido";*/
    if(!gc.isValidString(descripcion, 1, 250))
        msgError += "<ul>El dato 'descripcion' es inválido. Mínimo 1 y máximo 250 carácteres.";
    if(!gc.isNumeric(lada, 2, 3))
        msgError += "<ul>El dato 'lada' es inválido. Mínimo 2 y máximo 3 números.";
    if(!gc.isNumeric(telefono, 7, 8))
        msgError += "<ul>El dato 'Telefono' es incorrecto. Minimo 7 y maximo 8 numeros.";
    if(!gc.isValidString(celular, 0, 11))
        msgError += "<ul>El dato 'celular' es requerido. Minimo 10 y maximo 11 numeros.";
    if(!gc.isValidString(contacto, 1, 100))
        msgError += "<ul>El dato 'nombre de contacto' es requerido";
    if(email.equals(""))
        msgError += "<ul>El dato 'correo' es requerido. <br/>";
    if(!gc.isEmail(email))
        msgError += "<ul>El dato 'Correo electr&oacute;nico' es incorrecto. <br/>";
    if(idProspecto <= 0 && !mode.equals("") && !mode.equals("3"))
        msgError += "<ul>El dato ID 'prospecto' es requerido";
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idProspecto>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                SGProspectoBO prospectoBO = new SGProspectoBO(idProspecto,user.getConn());
                SgfensProspecto prospectoDto = prospectoBO.getSgfensProspecto();
                
                prospectoDto.setIdEstatus(estatus);
                prospectoDto.setRazonSocial(razonSocial);
                prospectoDto.setDescripcion(descripcion);
                
                prospectoDto.setLada(lada);
                prospectoDto.setTelefono(telefono);
                prospectoDto.setCelular(celular);
                prospectoDto.setCorreo(email);
                prospectoDto.setContacto(contacto);
                
                try{
                    new SgfensProspectoDaoImpl(user.getConn()).update(prospectoDto.createPk(), prospectoDto);

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
                 * Creamos el registro de Prospecto
                 */
                SgfensProspecto prospectoDto = new SgfensProspecto();
                SgfensProspectoDaoImpl prospectosDaoImpl = new SgfensProspectoDaoImpl(user.getConn());
                
                prospectoDto.setIdEstatus(estatus);
                prospectoDto.setRazonSocial(razonSocial);
                prospectoDto.setDescripcion(descripcion);
                
                prospectoDto.setLada(lada);
                prospectoDto.setTelefono(telefono);
                prospectoDto.setCelular(celular);
                prospectoDto.setCorreo(email);
                prospectoDto.setContacto(contacto);
                
                prospectoDto.setIdEmpresa(idEmpresa);
                
                prospectoDto.setFechaRegistro(new Date());
                prospectoDto.setIdUsuarioVendedor(user.getUser().getIdUsuarios());

                /**
                 * Realizamos el insert
                 */
                prospectosDaoImpl.insert(prospectoDto);
                
                //Consumo de Creditos Operacion
                try{
                    BitacoraCreditosOperacionBO bcoBO = new BitacoraCreditosOperacionBO(user.getConn());
                    bcoBO.registraDescuento(user.getUser(), 
                            BitacoraCreditosOperacionBO.CONSUMO_ACCION_REGISTRO_PROSPECTO, 
                            null, 0, prospectoDto.getIdProspecto(), 0, 
                            "Registro de Prospecto", null, true);
                }catch(Exception ex){
                    ex.printStackTrace();
                }

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