<%-- 
    Document   : catEmpleados_Asignados_ajax
    Created on : 13/04/2015, 01:54:37 PM
    Author     : leonardo
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensAsignacionEmpleadosDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SgfensAsignacionEmpleados"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
     int idUsuarioEmpleado = -1; //id del empleado al que se le asignara el empleado
     int idVendedorAsignado = -1;
    
    int idSgfensAsignacionEmpleados = -1;   
    int estatus = 1;//habilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    
    try{
        idUsuarioEmpleado = Integer.parseInt(request.getParameter("idUsuarioEmpleado"));
    }catch(NumberFormatException ex){}
    try{
        idVendedorAsignado = Integer.parseInt(request.getParameter("idVendedorAsignado"));
    }catch(NumberFormatException ex){}
    
    Date fechaLimite = null;
    try{
        fechaLimite = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("q_fh_min"));            
    }catch(Exception e){}
        
    int compartidoFecha = 0;//checbox para marcar si se comparte hasta una fecha
    try{ compartidoFecha = Integer.parseInt(request.getParameter("compartidoFecha")); }catch(Exception e){}
    
    try{
        idSgfensAsignacionEmpleados = Integer.parseInt(request.getParameter("idSgfensAsignacionEmpleados"));
    }catch(NumberFormatException ex){}
    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(idVendedorAsignado < 1)
        msgError += "<ul>El dato 'Vendedor' es requerido.";   
    if(idSgfensAsignacionEmpleados <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'SgfensAsignacionEmpleados' es requerido";
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idSgfensAsignacionEmpleados>0){
            if (mode.equals("borrarAsignacion")){
            /*
            * Editar
            */                  
                try{
                    SgfensAsignacionEmpleados asignacionDto = new SgfensAsignacionEmpleadosDaoImpl(user.getConn()).findByDynamicWhere(" ID_USUARIO_EMPLEADO = " + idUsuarioEmpleado + " AND ID_USUARIO_EMPLEADO_ASIGNADO = " + idVendedorAsignado + " AND ID_ESTATUS = 1 ", null)[0];
                    asignacionDto.setIdEstatus(2); 
                    new SgfensAsignacionEmpleadosDaoImpl(user.getConn()).update(asignacionDto.createPk(), asignacionDto);

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
                
                //verificamos si ya existe la relación
                boolean exiteAsignacion = false;
                try{
                    SgfensAsignacionEmpleados asignacion = new SgfensAsignacionEmpleadosDaoImpl(user.getConn()).findByDynamicWhere(" ID_USUARIO_EMPLEADO = " + idUsuarioEmpleado + " AND ID_USUARIO_EMPLEADO_ASIGNADO = " + idVendedorAsignado + " AND ID_ESTATUS = 1 ", null)[0];
                if(asignacion != null){
                    exiteAsignacion = true;
                }}catch(Exception e){}
                
                
                /**
                 * Creamos el registro de SgfensAsignacionEmpleados
                 */
                if(exiteAsignacion == false){
                    SgfensAsignacionEmpleados asignacionDto = new SgfensAsignacionEmpleados();
                    SgfensAsignacionEmpleadosDaoImpl asignacionDaoImpl = new SgfensAsignacionEmpleadosDaoImpl(user.getConn());

                    asignacionDto.setIdEstatus(estatus);
                    asignacionDto.setIdUsuarioEmpleado(idUsuarioEmpleado);
                    asignacionDto.setIdUsuarioEmpleadoAsignado(idVendedorAsignado);
                    asignacionDto.setIdEstatus(estatus);
                    asignacionDto.setFechaLimiteAsigancion(fechaLimite);

                    /**
                     * Realizamos el insert
                     */
                    asignacionDaoImpl.insert(asignacionDto);

                    out.print("<!--EXITO-->Registro Asignado satisfactoriamente.<br/>");
                }else{
                    out.print("<!--ERROR-->El vendedor ya ha sido asignado a este registro.");
                }
            
            }catch(Exception e){
                e.printStackTrace();
                msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
            }
        }
    }else{
        out.print("<!--ERROR-->"+msgError);
    }

%>