<%-- 
    Document   : catEmpleadosXEstacionPos_ajax
    Created on : 24/06/2015, 06:50:02 PM
    Author     : ISCesar
--%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoXPosEstacion"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoXPosEstacionDaoImpl"%>
<%@page import="com.tsp.sct.bo.PosEstacionBO"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String msgError="";
    /**
     * Modos:
     *  1: Registrar empleado en estacion
     *  2: Eliminar Empleado de estacion
     */
    int mode=-1;
    try{
        mode = Integer.parseInt(request.getParameter("mode"));
    }catch(NumberFormatException e){}
    
    //Gson gson = new Gson();
    
    GenericValidator genericValidator = new GenericValidator();
    
    //Configuration appConfig = new Configuration();
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
     * Procesamiento
     */
    if (mode==-1){
        msgError = "No se eligio una acci�n correcta. Reintente el llenado de los datos.";
    }else if (mode==1){
        //1 = Registrar Empleado en estaci�n
    
        int idPosEstacion = 0;
        int idEmpleado = 0;
        try {
            idPosEstacion = Integer.parseInt(request.getParameter("idPosEstacion"));
        } catch (NumberFormatException e) {}
        try {
            idEmpleado = Integer.parseInt(request.getParameter("id_empleado"));
        } catch (NumberFormatException e) {}
        
        if (idPosEstacion>0){
            if (idEmpleado<=0){
                msgError += "<ul>No se espec�fico un empleado v�lido.";
            }else{
                PosEstacionBO posEstacionBO = new PosEstacionBO(idPosEstacion, user.getConn());
                
                try{
                    posEstacionBO.asignarEmpleadoAEstacion(idEmpleado);
                }catch(Exception ex){
                    msgError += "Error al asignar Empleado en Estaci�n Punto de venta : " + ex.getMessage();
                }
            }
        }else{
            msgError += "<ul> No se especifico a que Estacion de Punto de Venta se agregara el Empleado.";
        }
    
    }else if (mode==2){
        //1 = Eliminar Empleado de estaci�n
    
        int idPosEstacion = 0;
        int idEmpleado = 0;
        try {
            idPosEstacion = Integer.parseInt(request.getParameter("idPosEstacion"));
        } catch (NumberFormatException e) {}
        try {
            idEmpleado = Integer.parseInt(request.getParameter("id_empleado"));
        } catch (NumberFormatException e) {}
        
        if (idPosEstacion>0){
            if (idEmpleado<=0){
                msgError += "<ul>No se espec�fico un empleado v�lido.";
            }else{
                EmpleadoXPosEstacionDaoImpl empleadoXEstacionDao = new EmpleadoXPosEstacionDaoImpl(user.getConn());
                EmpleadoXPosEstacion empleadoXPosEstacionDto = empleadoXEstacionDao.findByPrimaryKey(idEmpleado, idPosEstacion);
                if (empleadoXPosEstacionDto!=null){
                    try{
                        empleadoXEstacionDao.delete(empleadoXPosEstacionDto.createPk());
                    }catch(Exception ex){
                        msgError += "Error al quitar Empleado de Estaci�n Punto de venta : " + ex.getMessage();
                    }
                }else{
                    msgError += "<ul> No existe en base de datos una relaci�n existente entre la Estacion y el Empleado.";
                }
            }
        }else{
            msgError += "<ul> No se especifico a que Estacion de Punto de Venta se agregara el Empleado.";
        }
    
    }
    
    if (msgError.equals("")){
        out.print("<!--EXITO-->Informaci�n actualizada");
    }
%>
<%
    if (!msgError.equals("")){
        out.print("<!--ERROR-->"+msgError);
    }
%>