<%-- 
    Document   : catEmpleados_ajax
    Created on : 11/12/2013, 05:52:37 PM
    Author     : Leonardo
--%>

<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.bo.NominaEmpleadoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.NominaEmpleadoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.NominaEmpleado"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idNominaEmpleado = -1;
    String nombreNominaEmpleado ="";
    String apePaterNominaEmpleado ="";
    String apeMaterNominaEmpleado = "";
    String rfcNominaEmpleado = "";
    String curpNominaEmpleado = "";
    String numNominaEmpleado = "", correoNominaEmpleado = "", telefonoNominaEmpleado = "";
    String numSeguroNominaEmpleado = "", clabeNominaEmpleado = "", contratoNominaEmpleado = "-1", jornadaNominaEmpleado = "-1", periodicidadPagoNominaEmpleado = "-1";
    Date fechaInicioRelaNominaEmpleado = null;
    double salarioBaseCotNominaEmpleado = 0;
    double salarioDiarioIntNominaEmpleado = 0;
    int idRegimenNominaEmpleado = -1, idBancoNominaEmpleado = -1, idPuestoNominaEmpleado = -1, idRiesgoPuestoNominaEmpleado = -1, idDepartamentoNominaEmpleado = -1;
    
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idNominaEmpleado = Integer.parseInt(request.getParameter("idNominaEmpleado"));
    }catch(NumberFormatException ex){}
    nombreNominaEmpleado = request.getParameter("nombreNominaEmpleado")!=null?new String(request.getParameter("nombreNominaEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";
    apePaterNominaEmpleado = request.getParameter("apePaterNominaEmpleado")!=null?new String(request.getParameter("apePaterNominaEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";    
    apeMaterNominaEmpleado = request.getParameter("apeMaterNominaEmpleado")!=null?new String(request.getParameter("apeMaterNominaEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";    
    rfcNominaEmpleado = request.getParameter("rfcNominaEmpleado")!=null?new String(request.getParameter("rfcNominaEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";    
    curpNominaEmpleado = request.getParameter("curpNominaEmpleado")!=null?new String(request.getParameter("curpNominaEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";    
    numNominaEmpleado = request.getParameter("numNominaEmpleado")!=null?new String(request.getParameter("numNominaEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";    
    numSeguroNominaEmpleado = request.getParameter("numSeguroNominaEmpleado")!=null?new String(request.getParameter("numSeguroNominaEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";        
    //try{ fechaInicioRelaNominaEmpleado = new SimpleDateFormat("yyyy/MM/dd").parse(request.getParameter("fechaInicioRelaNominaEmpleado"));}catch(Exception e){}
    try{ fechaInicioRelaNominaEmpleado = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fechaInicioRelaNominaEmpleado"));}catch(Exception e){}
    
    try{
        salarioBaseCotNominaEmpleado = Double.parseDouble(request.getParameter("salarioBaseCotNominaEmpleado"));
    }catch(NumberFormatException ex){}
    try{
        salarioDiarioIntNominaEmpleado = Double.parseDouble(request.getParameter("salarioDiarioIntNominaEmpleado"));
    }catch(NumberFormatException ex){}
    try{
        idRegimenNominaEmpleado = Integer.parseInt(request.getParameter("idRegimenNominaEmpleado"));
    }catch(NumberFormatException ex){}
    try{
        idBancoNominaEmpleado = Integer.parseInt(request.getParameter("idBancoNominaEmpleado"));
    }catch(NumberFormatException ex){} 
    try{
        idPuestoNominaEmpleado = Integer.parseInt(request.getParameter("idPuestoNominaEmpleado"));
    }catch(NumberFormatException ex){}
    try{
        idRiesgoPuestoNominaEmpleado = Integer.parseInt(request.getParameter("idRiesgoPuestoNominaEmpleado"));
    }catch(NumberFormatException ex){}
    try{
        idDepartamentoNominaEmpleado = Integer.parseInt(request.getParameter("idDepartamentoNominaEmpleado"));
    }catch(NumberFormatException ex){}
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    clabeNominaEmpleado = request.getParameter("clabeNominaEmpleado")!=null?new String(request.getParameter("clabeNominaEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";        
    contratoNominaEmpleado = request.getParameter("contratoNominaEmpleado")!=null?new String(request.getParameter("contratoNominaEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";        
    jornadaNominaEmpleado = request.getParameter("jornadaNominaEmpleado")!=null?new String(request.getParameter("jornadaNominaEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";        
    periodicidadPagoNominaEmpleado = request.getParameter("periodicidadPagoNominaEmpleado")!=null?new String(request.getParameter("periodicidadPagoNominaEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";        
    correoNominaEmpleado = request.getParameter("correoNominaEmpleado")!=null?new String(request.getParameter("correoNominaEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";        
    telefonoNominaEmpleado = request.getParameter("telefonoNominaEmpleado")!=null?new String(request.getParameter("telefonoNominaEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";        
    
    System.out.println("-------contratoNominaEmpleado: "+contratoNominaEmpleado);
    System.out.println("-------jornadaNominaEmpleado: "+jornadaNominaEmpleado);
    System.out.println("-------periodicidadPagoNominaEmpleado: "+periodicidadPagoNominaEmpleado);
    System.out.println("-------idRegimenNominaEmpleado: "+idRegimenNominaEmpleado);
    System.out.println("-------idBancoNominaEmpleado: "+idBancoNominaEmpleado);
    System.out.println("-------idPuestoNominaEmpleado: "+idPuestoNominaEmpleado);
    System.out.println("-------idRiesgoPuestoNominaEmpleado: "+idRiesgoPuestoNominaEmpleado);
    System.out.println("-------idDepartamentoNominaEmpleado: "+idDepartamentoNominaEmpleado);    
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombreNominaEmpleado, 1, 40))
        msgError += "<ul>El dato 'nombre' es requerido.";
    if(!gc.isValidString(apePaterNominaEmpleado, 1, 40))
        msgError += "<ul>El dato 'apellido paterno' es requerido";
    if(!gc.isValidString(curpNominaEmpleado, 1, 35))
        msgError += "<ul>El dato 'CURP' es requerido";
    if(!gc.isRFC(rfcNominaEmpleado))
        msgError += "<ul>El dato 'RFC' es requerido/no es valido";
    if(!gc.isValidString(numNominaEmpleado, 1, 15))
        msgError += "<ul>El dato 'numero empleado' es requerido";
    if(idRegimenNominaEmpleado <= 0)
        msgError += "<ul>El dato ID 'Regimen fiscal' es requerido";
    if(periodicidadPagoNominaEmpleado.equals("-1"))
        msgError += "<ul>El dato ID 'perioodicidad de pago' es requerido";
    if(idNominaEmpleado <= 0 && (!mode.equals("") && !mode.equals("3")))
        msgError += "<ul>El dato ID 'NominaEmpleado' es requerido";
    if(clabeNominaEmpleado.length()>0){
        if(clabeNominaEmpleado.length()<18)
            msgError += "<ul>El dato 'CLABE' es requerido/no cumple con el estandar";
    }
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idNominaEmpleado>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                NominaEmpleadoBO nominaEmpleadoBO = new NominaEmpleadoBO(user.getConn(),idNominaEmpleado);
                NominaEmpleado nominaEmpleadoDto = nominaEmpleadoBO.getNominaEmpleado();
                
                nominaEmpleadoDto.setIdEmpresa(user.getUser().getIdEmpresa());                            
                nominaEmpleadoDto.setIdEstatus(estatus);
                nominaEmpleadoDto.setRfc(rfcNominaEmpleado);
                nominaEmpleadoDto.setNombre(nombreNominaEmpleado);
                nominaEmpleadoDto.setApellidoPaterno(apePaterNominaEmpleado);
                nominaEmpleadoDto.setApellidoMaterno(apeMaterNominaEmpleado);
                nominaEmpleadoDto.setNumEmpleado(numNominaEmpleado);
                nominaEmpleadoDto.setCurp(curpNominaEmpleado);
                nominaEmpleadoDto.setIdNominaRegimenFiscal(idRegimenNominaEmpleado);
                nominaEmpleadoDto.setNumSeguroSocial(numSeguroNominaEmpleado);
                nominaEmpleadoDto.setIdPuesto(idPuestoNominaEmpleado);
                nominaEmpleadoDto.setIdDepartamento(idDepartamentoNominaEmpleado);
                nominaEmpleadoDto.setClabe(clabeNominaEmpleado);
                nominaEmpleadoDto.setIdNominaBanco(idBancoNominaEmpleado);
                nominaEmpleadoDto.setFechaInicioRelacionLaboral(fechaInicioRelaNominaEmpleado);
                nominaEmpleadoDto.setFechaAlta(new Date());
                nominaEmpleadoDto.setTipoContrato(contratoNominaEmpleado);
                nominaEmpleadoDto.setTipoJornada(jornadaNominaEmpleado);
                nominaEmpleadoDto.setPeriodicidadPago(periodicidadPagoNominaEmpleado);
                nominaEmpleadoDto.setSalarioBaseCotApor(salarioBaseCotNominaEmpleado);
                nominaEmpleadoDto.setIdRiesgoPuesto(idRiesgoPuestoNominaEmpleado);
                nominaEmpleadoDto.setSalarioDiarioIntegrado(salarioDiarioIntNominaEmpleado);
                //validamos si se da de baja:
                if(estatus==2)
                    nominaEmpleadoDto.setFechaBaja(new Date());
                nominaEmpleadoDto.setCorreo(correoNominaEmpleado);
                nominaEmpleadoDto.setTelefono(telefonoNominaEmpleado);
               
                
                try{
                    new NominaEmpleadoDaoImpl(user.getConn()).update(nominaEmpleadoDto.createPk(), nominaEmpleadoDto);

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
                NominaEmpleado nominaEmpleadoDto = new NominaEmpleado();
                NominaEmpleadoDaoImpl nominaEmpleadosDaoImpl = new NominaEmpleadoDaoImpl(user.getConn());

                nominaEmpleadoDto.setIdEmpresa(user.getUser().getIdEmpresa());                            
                nominaEmpleadoDto.setIdEstatus(estatus);
                nominaEmpleadoDto.setRfc(rfcNominaEmpleado);
                nominaEmpleadoDto.setNombre(nombreNominaEmpleado);
                nominaEmpleadoDto.setApellidoPaterno(apePaterNominaEmpleado);
                nominaEmpleadoDto.setApellidoMaterno(apeMaterNominaEmpleado);
                nominaEmpleadoDto.setNumEmpleado(numNominaEmpleado);
                nominaEmpleadoDto.setCurp(curpNominaEmpleado);
                nominaEmpleadoDto.setIdNominaRegimenFiscal(idRegimenNominaEmpleado);
                nominaEmpleadoDto.setNumSeguroSocial(numSeguroNominaEmpleado);
                nominaEmpleadoDto.setIdPuesto(idPuestoNominaEmpleado);
                nominaEmpleadoDto.setIdDepartamento(idDepartamentoNominaEmpleado);
                nominaEmpleadoDto.setClabe(clabeNominaEmpleado);
                nominaEmpleadoDto.setIdNominaBanco(idBancoNominaEmpleado);
                nominaEmpleadoDto.setFechaInicioRelacionLaboral(fechaInicioRelaNominaEmpleado);
                nominaEmpleadoDto.setFechaAlta(new Date());
                nominaEmpleadoDto.setTipoContrato(contratoNominaEmpleado);
                nominaEmpleadoDto.setTipoJornada(jornadaNominaEmpleado);
                nominaEmpleadoDto.setPeriodicidadPago(periodicidadPagoNominaEmpleado);
                nominaEmpleadoDto.setSalarioBaseCotApor(salarioBaseCotNominaEmpleado);
                nominaEmpleadoDto.setIdRiesgoPuesto(idRiesgoPuestoNominaEmpleado);
                nominaEmpleadoDto.setSalarioDiarioIntegrado(salarioDiarioIntNominaEmpleado);
                //nominaEmpleadoDto.setFechaBaja();
                nominaEmpleadoDto.setCorreo(correoNominaEmpleado);
                nominaEmpleadoDto.setTelefono(telefonoNominaEmpleado);
                

                /**
                 * Realizamos el insert
                 */
                nominaEmpleadosDaoImpl.insert(nominaEmpleadoDto);

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