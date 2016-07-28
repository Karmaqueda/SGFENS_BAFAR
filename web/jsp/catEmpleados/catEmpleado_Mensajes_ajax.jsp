<%-- 
    Document   : catEmpleado_Mensajes_ajax
    Created on : 13/02/2013, 04:28:04 PM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.bo.ZonaHorariaBO"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<!---------------<//%@page import="com.tsp.microfinancieras.jdbc.SgfensMovilMensajeVendedorDaoImpl"%>-->
<!---------------<//%@page import="com.tsp.microfinancieras.dto.SgfensMovilMensajeVendedor"%>-->
<!---------------<//%@page import="com.tsp.microfinancieras.bo.SGMovilMensajeVendedorBO"%>-->
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.MovilMensajeBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.MovilMensajeDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.MovilMensaje"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idMovilMensaje = -1;
    int idEmpleado = -1;    
    String mensajeMovil = "";
    int estatus = 2;//deshabilitado
    
    String msgError = "";
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    
    mensajeMovil = request.getParameter("mensajeMovil")!=null?new String(request.getParameter("mensajeMovil").getBytes("ISO-8859-1"),"UTF-8"):""; 
    
    if(mode.equals("mensajeMasivo")){//si es un mensaje de envio masivo a todos los empleados: 
        
        ///*validamos si se eligio a un empleado en particular o si es un mensaje masivo para todos los empleados:
        try{
            idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
        }catch(NumberFormatException ex){}
        String filtro = "";
        if(idEmpleado != -1){
            filtro = " AND ID_EMPLEADO = "+ idEmpleado;
        }
        ///*
        
        Empleado[] empleadosDto = new Empleado[0];
        EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
        //empleadosDto = empleadoBO.findEmpleados(0, idEmpresa, 0, 0, "");
        empleadosDto = empleadoBO.findEmpleados(0, idEmpresa, 0, 0, filtro);
        
        
        MovilMensaje mMovilMensajeDto = null;
        MovilMensajeDaoImpl mMovilMensajesDaoImpl = new MovilMensajeDaoImpl(user.getConn());
        try {
            for(Empleado emp : empleadosDto){
                          
                    /**
                     * Creamos el registro de MovilMensaje
                     */
                    mMovilMensajeDto = new MovilMensaje();
                    mMovilMensajeDto.setEmisorTipo(2);
                    mMovilMensajeDto.setIdEmpleadoEmisor(0);
                    mMovilMensajeDto.setReceptorTipo(1);
                    mMovilMensajeDto.setIdEmpleadoReceptor(emp.getIdEmpleado());
                    try{
                        mMovilMensajeDto.setFechaEmision(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                    }catch(Exception e){
                        mMovilMensajeDto.setFechaEmision(new Date());
                    }
                    mMovilMensajeDto.setFechaRecepcion(null);
                    mMovilMensajeDto.setMensaje(mensajeMovil);
                    mMovilMensajeDto.setRecibido(0);
                    /**
                     * Realizamos el insert
                     */
                    mMovilMensajesDaoImpl.insert(mMovilMensajeDto);                           
            }
            out.print("<!--EXITO-->Mensajes enviados satisfactoriamente.<br/>");
        }catch(Exception e){
            e.printStackTrace();
            msgError += "Ocurrio un error al enviar el mensaje " + e.toString() ;
            out.print("<!--ERROR-->"+msgError);
        }         
    }else{//si es un mensaje solo para un empleado en particular
        try{
            idMovilMensaje = Integer.parseInt(request.getParameter("idMovilMensaje"));
        }catch(NumberFormatException ex){}

        try{
            estatus = Integer.parseInt(request.getParameter("estatus"));
        }catch(NumberFormatException ex){}
        try{
            idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
        }catch(NumberFormatException ex){}


        /*
        * Validaciones del servidor
        */        
        GenericValidator gc = new GenericValidator();

        if(idMovilMensaje <= 0 && !mode.equals("") && !mode.equals("3"))
            msgError += "<ul>El dato ID 'mMovilMensaje' es requerido";
        if(idEmpleado<0)
            msgError += "<ul>El dato del 'promotor' es requerido";
        if(mensajeMovil.equals(""))
            msgError += "<ul>El dato del 'mensaje' es requerido";
        /*
        if(idVendedor<=0)
            msgError += "<ul>El dato 'Vendedor' es requerido";
     * */

        if(msgError.equals("")){
            if(idMovilMensaje>0){
                if (mode.equals("1")){
                /*
                * Editar
                */
                    MovilMensajeBO mMovilMensajeBO = new MovilMensajeBO(idMovilMensaje,user.getConn());
                    MovilMensaje mMovilMensajeDto = mMovilMensajeBO.getMovilMensaje();

                    //mMovilMensajeDto.setIdEstatus(estatus);                                
                    //mMovilMensajeDto.setIdEmpleadoPromotor(idEmpleado);                    

                    try{
                        new MovilMensajeDaoImpl(user.getConn()).update(mMovilMensajeDto.createPk(), mMovilMensajeDto);

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
                     * Creamos el registro de MovilMensaje
                     */
                    MovilMensaje mMovilMensajeDto = new MovilMensaje();
                    MovilMensajeDaoImpl mMovilMensajesDaoImpl = new MovilMensajeDaoImpl(user.getConn());

                    int idMovilMensajeNuevo;             

                    mMovilMensajeDto.setEmisorTipo(2);
                    mMovilMensajeDto.setIdEmpleadoEmisor(0);
                    mMovilMensajeDto.setReceptorTipo(1);
                    mMovilMensajeDto.setIdEmpleadoReceptor(idEmpleado);
                    
                    try{
                        mMovilMensajeDto.setFechaEmision(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                    }catch(Exception e){
                        mMovilMensajeDto.setFechaEmision(new Date());
                    }
                    mMovilMensajeDto.setFechaRecepcion(null);
                    mMovilMensajeDto.setMensaje(mensajeMovil);
                    mMovilMensajeDto.setRecibido(0);

                    /**
                     * Realizamos el insert
                     */
                    mMovilMensajesDaoImpl.insert(mMovilMensajeDto);
                    out.print("<!--EXITO-->Mensaje enviado satisfactoriamente.<br/>");

                }catch(Exception e){
                    e.printStackTrace();
                    msgError += "Ocurrio un error al enviar el mensaje " + e.toString() ;
                }
            }
        }else{
            out.print("<!--ERROR-->"+msgError);
        }
    }

%>