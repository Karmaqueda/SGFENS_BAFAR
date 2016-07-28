<%-- 
    Document   : catCrFormulario_list
    Created on : 10/06/2016, 12:55:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.bo.CrFormularioBO"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.CrFormularioDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrFormulario"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idCrFormulario = -1;
    int idGrupoFormulario = -1;
    int ordenGrupo = -1;
    String nombre ="";
    String descripcion ="";
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idCrFormulario = Integer.parseInt(request.getParameter("idCrFormulario"));
    }catch(Exception ex){}
    try{
        idGrupoFormulario = Integer.parseInt(request.getParameter("id_grupo_formulario"));
    }catch(Exception ex){}
    try{
        ordenGrupo = Integer.parseInt(request.getParameter("orden_grupo"));
    }catch(Exception ex){}
    nombre = request.getParameter("nombre")!=null?new String(request.getParameter("nombre").getBytes("ISO-8859-1"),"UTF-8"):"";    
    descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";    
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
    if(!gc.isValidString(descripcion, 0, 500))
        msgError += "<ul>El dato 'descripción' es opcional, debe tener máximo 500 caracteres."; 
    if(idGrupoFormulario<=0)
        msgError += "<ul>El dato 'Grupo Formularo' es requerido."; 
    if(ordenGrupo<=0)
        msgError += "<ul>El dato 'Orden en Grupo' es requerido. Debe ser un numero entero positivo."; 
    
    if(idCrFormulario <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'CrFormulario' es requerido para ediciones";
    
    if(msgError.equals("")){
        if(idCrFormulario>0){
            if (mode.equals("1")){
                /*
                * Editar
                */
                CrFormularioBO crFormularioBO = new CrFormularioBO(idCrFormulario,user.getConn());
                CrFormulario crFormularioDto = crFormularioBO.getCrFormulario();

                crFormularioDto.setIdGrupoFormulario(idGrupoFormulario);
                crFormularioDto.setOrdenGrupo(ordenGrupo);
                crFormularioDto.setNombre(nombre);
                crFormularioDto.setDescripcion(descripcion);
                crFormularioDto.setFechaHrUltimaEdicion(new Date());
                crFormularioDto.setIdUsuarioEdicion(user.getUser().getIdUsuarios());
                crFormularioDto.setIdEmpresa(idEmpresa);
                crFormularioDto.setIdEstatus(estatus);

                try{
                    new CrFormularioDaoImpl(user.getConn()).update(crFormularioDto.createPk(), crFormularioDto);

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
            CrFormulario crFormularioDto = new CrFormulario();
            CrFormularioDaoImpl crFormularioDaoImpl = new CrFormularioDaoImpl(user.getConn());

            crFormularioDto.setIdGrupoFormulario(idGrupoFormulario);
            crFormularioDto.setOrdenGrupo(ordenGrupo);
            crFormularioDto.setNombre(nombre);
            crFormularioDto.setDescripcion(descripcion);
            crFormularioDto.setFechaHrCreacion(new Date());
            crFormularioDto.setIdUsuarioEdicion(user.getUser().getIdUsuarios());
            crFormularioDto.setIdEmpresa(idEmpresa);
            crFormularioDto.setIdEstatus(estatus);

            try{
                crFormularioDaoImpl.insert(crFormularioDto); 

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