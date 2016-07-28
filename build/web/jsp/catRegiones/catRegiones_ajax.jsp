<%-- 
    Document   : catRegiones_ajax
    Created on : 03/01/2013, 06:02:28 PM
    Author     : Leonardo
--%>


<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.RegionBO"%>
<%@page import="com.tsp.sct.dao.jdbc.RegionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Region"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    long idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idRegion = -1;
    String nombreRegion ="";
    String descripcion ="";  
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    try{
        idRegion = Integer.parseInt(request.getParameter("idRegion"));
    }catch(NumberFormatException ex){}
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";    
    if (mode.equals("2")){
    /*
    * Borrar
    */
                
        RegionBO regionBO = new RegionBO(idRegion,user.getConn());
        Region regionDto = regionBO.getRegion();

        regionDto.setIdEstatus(2);

        try{
            new RegionDaoImpl(user.getConn()).update(regionDto.createPk(), regionDto);

            out.print("<!--EXITO-->Registro borrado satisfactoriamente");
        }catch(Exception ex){
            out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
            ex.printStackTrace();
        }
    }else{
    nombreRegion = request.getParameter("nombreRegion")!=null?new String(request.getParameter("nombreRegion").getBytes("ISO-8859-1"),"UTF-8"):"";
    descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombreRegion, 1, 30))
        msgError += "<ul>El dato 'Nombre' es requerido.";
    if(!gc.isValidString(descripcion, 1, 100))
        msgError += "<ul>El dato 'Descripción' es requerido";   
    if(idRegion <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'Zona' es requerido";
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idRegion>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                RegionBO regionBO = new RegionBO(idRegion,user.getConn());
                Region regionDto = regionBO.getRegion();
                
                //regionDto.setIdEstatus(estatus);
                regionDto.setNombre(nombreRegion);
                regionDto.setDescripcion(descripcion);
               
                
                try{
                    new RegionDaoImpl(user.getConn()).update(regionDto.createPk(), regionDto);

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
                Region regionDto = new Region();
                RegionDaoImpl regionsDaoImpl = new RegionDaoImpl(user.getConn());
                
                //regionDto.setIdEstatus(estatus);
                regionDto.setNombre(nombreRegion);
                regionDto.setDescripcion(descripcion);                              
                regionDto.setIdEmpresa(idEmpresa);
                regionDto.setIdEstatus(1);

                /**
                 * Realizamos el insert
                 */
                regionsDaoImpl.insert(regionDto);

                out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
            
            }catch(Exception e){
                e.printStackTrace();
                msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
            }
        }
    }else{
        out.print("<!--ERROR-->"+msgError);
    }
    }

%>