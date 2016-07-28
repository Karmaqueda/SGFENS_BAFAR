<%-- 
    Document   : catDiccionarioIngresosEgresoss_ajax
    Created on : 24/06/2015, 11:32:52 AM
    Author     : Cesar Martinez
--%>

<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.DiccionarioIngresosEgresosBO"%>
<%@page import="com.tsp.sct.dao.jdbc.DiccionarioIngresosEgresosDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.DiccionarioIngresosEgresos"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idDiccionarioIngresosEgresos = -1;
    String nombre ="";
    String descripcion ="";  
    int estatus = 2;//deshabilitado
    int esIngreso = 0;//egreso
    int esGeneral = 0;//por defecto: no es general
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idDiccionarioIngresosEgresos = Integer.parseInt(request.getParameter("idDiccionarioIngresosEgresos"));
    }catch(NumberFormatException ex){}
    nombre = request.getParameter("nombre")!=null?new String(request.getParameter("nombre").getBytes("ISO-8859-1"),"UTF-8"):"";
    descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}
    try{
        esIngreso = Integer.parseInt(request.getParameter("es_ingreso"));
    }catch(NumberFormatException ex){}
    try{
        esGeneral = Integer.parseInt(request.getParameter("es_general"));
    }catch(NumberFormatException ex){}
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombre, 2, 50))
        msgError += "<ul>El dato 'nombre' es requerido, mínimo 2 caracteres, máximo 50.";
    if(!gc.isValidString(descripcion, 0, 50))
        msgError += "<ul>El dato 'descripcion' es opcional, máximo 250 caracteres.";   
    if(idDiccionarioIngresosEgresos <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'diccionarioIngresosEgresos' es requerido";

    if(msgError.equals("")){
        if(idDiccionarioIngresosEgresos>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                DiccionarioIngresosEgresosBO diccionarioIngresosEgresosBO = new DiccionarioIngresosEgresosBO(idDiccionarioIngresosEgresos,user.getConn());
                DiccionarioIngresosEgresos diccionarioIngresosEgresosDto = diccionarioIngresosEgresosBO.getDiccionarioIngresosEgresos();
                
                diccionarioIngresosEgresosDto.setIdEstatus(estatus);
                diccionarioIngresosEgresosDto.setNombre(nombre);
                diccionarioIngresosEgresosDto.setDescripcion(descripcion);
                diccionarioIngresosEgresosDto.setEsIngreso(esIngreso);
                if (user.getUser().getIdRoles()==RolesBO.ROL_DESARROLLO)
                    diccionarioIngresosEgresosDto.setEsGeneral(esGeneral);
               
                try{
                    new DiccionarioIngresosEgresosDaoImpl(user.getConn()).update(diccionarioIngresosEgresosDto.createPk(), diccionarioIngresosEgresosDto);

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
                 * Creamos el registro
                 */
                DiccionarioIngresosEgresos diccionarioIngresosEgresosDto = new DiccionarioIngresosEgresos();
                DiccionarioIngresosEgresosDaoImpl diccionarioIngresosEgresossDaoImpl = new DiccionarioIngresosEgresosDaoImpl(user.getConn());
                
                diccionarioIngresosEgresosDto.setIdEstatus(estatus);
                diccionarioIngresosEgresosDto.setNombre(nombre);
                diccionarioIngresosEgresosDto.setDescripcion(descripcion);
                diccionarioIngresosEgresosDto.setEsIngreso(esIngreso);
                if (user.getUser().getIdRoles()==RolesBO.ROL_DESARROLLO)
                    diccionarioIngresosEgresosDto.setEsGeneral(esGeneral);
                diccionarioIngresosEgresosDto.setIdEmpresa(idEmpresa);

                /**
                 * Realizamos el insert
                 */
                diccionarioIngresosEgresossDaoImpl.insert(diccionarioIngresosEgresosDto);

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