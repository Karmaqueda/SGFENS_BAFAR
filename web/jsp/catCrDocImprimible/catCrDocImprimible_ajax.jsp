<%-- 
    Document   : catCrDocImprimible_list
    Created on : 30/06/2016, 12:55:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.bo.CrDocImprimibleBO"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.CrDocImprimibleDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrDocImprimible"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    String tiposImprimiblesStr = "";
    int i = 1;
    for (String str :  CrDocImprimibleBO.listaTipoImprimible){
        tiposImprimiblesStr += str;
        if (i<CrDocImprimibleBO.listaTipoImprimible.length)
            tiposImprimiblesStr += "|";
        
        i++;
    }
    
    /*
    * Parámetros
    */
    int idCrDocImprimible = -1;
    String nombre = "";
    String descripcion = "";
    String tipoImprimible = "OTRO";
    String nombreArchivo = "";
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idCrDocImprimible = Integer.parseInt(request.getParameter("idCrDocImprimible"));
    }catch(Exception ex){}
    nombre = request.getParameter("nombre")!=null?new String(request.getParameter("nombre").getBytes("ISO-8859-1"),"UTF-8"):"";    
    descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";    
    tipoImprimible = request.getParameter("tipo_imprimible")!=null?new String(request.getParameter("tipo_imprimible").getBytes("ISO-8859-1"),"UTF-8"):"";
    nombreArchivo = request.getParameter("nombre_archivo")!=null?new String(request.getParameter("nombre_archivo").getBytes("ISO-8859-1"),"UTF-8"):"";
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(Exception ex){} 
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombre, 1, 50))
        msgError += "<ul>El dato 'nombre' es requerido, debe tener máximo 50 caracteres."; 
    if(!gc.isValidString(descripcion, 1, 500))
        msgError += "<ul>El dato 'descripción' es requerido, debe tener máximo 500 caracteres."; 
    if(!gc.validarConRegex(tiposImprimiblesStr, tipoImprimible)) //"CONTRATO|SOLICITUD|TABLA_AMORTIZACION|CODIGO_BARRAS|OTRO", tipoImprimible))
        msgError += "<ul>El dato 'Tipo Imprimible' no es válido."; 
    if(!gc.isValidString(nombreArchivo, 1, 500))
        msgError += "<ul>El dato 'Archivo Plantilla' es requerido.";
    
    if(idCrDocImprimible <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'CrDocImprimible' es requerido para ediciones";
    
    if(msgError.equals("")){
        if(idCrDocImprimible>0){
            if (mode.equals("1")){
                /*
                * Editar
                */
                CrDocImprimibleBO crDocImprimibleBO = new CrDocImprimibleBO(idCrDocImprimible,user.getConn());
                CrDocImprimible crDocImprimibleDto = crDocImprimibleBO.getCrDocImprimible();

                crDocImprimibleDto.setNombre(nombre);
                crDocImprimibleDto.setDescripcion(descripcion);
                crDocImprimibleDto.setTipoImprimible(tipoImprimible);
                crDocImprimibleDto.setNombreArchivoJasper(nombreArchivo);
                crDocImprimibleDto.setFechaHrUltimaEdicion(new Date());
                crDocImprimibleDto.setIdUsuarioEdicion(user.getUser().getIdUsuarios());
                crDocImprimibleDto.setIdEmpresa(idEmpresa);
                crDocImprimibleDto.setIdEstatus(estatus);

                try{
                    new CrDocImprimibleDaoImpl(user.getConn()).update(crDocImprimibleDto.createPk(), crDocImprimibleDto);

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
            CrDocImprimible crDocImprimibleDto = new CrDocImprimible();
            CrDocImprimibleDaoImpl crDocImprimibleDaoImpl = new CrDocImprimibleDaoImpl(user.getConn());

            crDocImprimibleDto.setNombre(nombre);
            crDocImprimibleDto.setDescripcion(descripcion);
            crDocImprimibleDto.setTipoImprimible(tipoImprimible);
            crDocImprimibleDto.setNombreArchivoJasper(nombreArchivo);
            crDocImprimibleDto.setFechaHrCreacion(new Date());
            crDocImprimibleDto.setIdUsuarioEdicion(user.getUser().getIdUsuarios());
            crDocImprimibleDto.setIdEmpresa(idEmpresa);
            crDocImprimibleDto.setIdEstatus(estatus);

            try{
                crDocImprimibleDaoImpl.insert(crDocImprimibleDto); 

                out.print("<!--EXITO-->Registro creado satisfactoriamente");
            }catch(Exception ex){
                out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                ex.printStackTrace();
            }

        }
    } else {
        out.print("<!--ERROR-->"+msgError);
    }

%>