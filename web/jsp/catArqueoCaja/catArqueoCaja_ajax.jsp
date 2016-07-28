<%-- 
    Document   : catArqueoCaja_ajax
    Created on : 21/09/2014, 03:07:25 PM
    Author     : 578
--%>

<%@page import="com.tsp.sct.bo.ZonaHorariaBO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoArqueoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoArqueo"%>
<%@page import="com.tsp.sct.bo.EmpleadoArqueoBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    long idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idEmpleado = -1;
    int idArqueo = -1;    
    String fechaArqueo = "";
    double monto = 0;
    String referencia = "";
    int idMetodoPago =-1;
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    
    try{
        idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
   }catch(NumberFormatException ex){}        
    try{
        idArqueo = Integer.parseInt(request.getParameter("idArqueo"));
    }catch(NumberFormatException ex){}
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";    
    
    if (mode.equals("2")){
    /*
    * Borrar
    */
                
        EmpleadoArqueoBO arqueoBO = new EmpleadoArqueoBO(idArqueo,user.getConn());
        EmpleadoArqueo empleadoArqueoDto = arqueoBO.getEmpleadoArqueo();

        empleadoArqueoDto.setIdEstatus(2);

        try{
            new EmpleadoArqueoDaoImpl(user.getConn()).update(empleadoArqueoDto.createPk(), empleadoArqueoDto);

            out.print("<!--EXITO-->Registro borrado satisfactoriamente");
        }catch(Exception ex){
            out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
            ex.printStackTrace();
        }
    }else{
    fechaArqueo = request.getParameter("fecha")!=null?new String(request.getParameter("fecha").getBytes("ISO-8859-1"),"UTF-8"):"";
    try{ monto = Double.parseDouble(request.getParameter("monto")); }catch(Exception e){}
    referencia = request.getParameter("referencia")!=null?new String(request.getParameter("referencia").getBytes("ISO-8859-1"),"UTF-8"):"";
    try{ idMetodoPago = Integer.parseInt(request.getParameter("idMetodoPago")); }catch(Exception e){}

    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if (monto<=0)
            msgError = "<ul>El abono debe ser mayor a 0.";
    if(!gc.isValidString(referencia, 0, 30))
        msgError += "<ul>El dato 'Referencia' es requerido.";
    if (idMetodoPago<=0)
            msgError = "<ul>Debe seleccionar el Método de Pago, es obligatorio.";

    if(msgError.equals("")){
        if(idArqueo>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                EmpleadoArqueoBO arqueoBO = new EmpleadoArqueoBO(idArqueo,user.getConn());
                EmpleadoArqueo empleadoArqueoDto = arqueoBO.getEmpleadoArqueo();
                
                empleadoArqueoDto.setIdEmpleado(idEmpleado);
                empleadoArqueoDto.setMonto(monto);
                empleadoArqueoDto.setReferencia(referencia);
                empleadoArqueoDto.setIdCobranzaMetodoPago(idMetodoPago);
               
                
                try{
                    new EmpleadoArqueoDaoImpl(user.getConn()).update(empleadoArqueoDto.createPk(), empleadoArqueoDto);

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
                 * Creamos el registro de Arqueo
                 */
                Date fecha=new Date(); 
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                
                EmpleadoArqueo empleadoArqueoDto = new EmpleadoArqueo();
                EmpleadoArqueoDaoImpl empleadoArqueoDaoImpl = new EmpleadoArqueoDaoImpl(user.getConn());
                
                empleadoArqueoDto.setIdEmpleado(idEmpleado);
                try{
                    empleadoArqueoDto.setFecha(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                }catch(Exception e){
                    empleadoArqueoDto.setFecha(fecha);
                }
                empleadoArqueoDto.setMonto(monto);
                empleadoArqueoDto.setReferencia(referencia);
                empleadoArqueoDto.setIdCobranzaMetodoPago(idMetodoPago);
                empleadoArqueoDto.setIdEstatus(1);

                /**
                 * Realizamos el insert
                 */
                empleadoArqueoDaoImpl.insert(empleadoArqueoDto);

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