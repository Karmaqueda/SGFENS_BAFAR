<%-- 
    Document   : catConcepto_ajaxCnDscnto
    Created on : 29/09/2014, 01:50:58 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.dao.jdbc.ConceptoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idConcepto = -1;
    double descuentoPorcentajeConcepto = 0;
    double descuentoMontoConcepto = 0;
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idConcepto = Integer.parseInt(request.getParameter("idConcepto"));
    }catch(NumberFormatException ex){}
    try{
        descuentoPorcentajeConcepto = Double.parseDouble(request.getParameter("descuentoPorcentajeConcepto"));
    }catch(Exception ex){}
    try{
        descuentoMontoConcepto = Double.parseDouble(request.getParameter("descuentoMontoConcepto"));
    }catch(Exception ex){}
     
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    if(descuentoPorcentajeConcepto <= 0 && descuentoMontoConcepto <= 0 && (!mode.equals("2"))){
        msgError += "<ul>El dato del porcentaje o monto debe ser mayor de 0.0 ";        
    }
    if(idConcepto <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'Concepto' es requerido";
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idConcepto>0){
            if (mode.equals("1")){
            /*
            * Nuevo descuento (Editar concepto)
            */
                ConceptoBO conceptoBO = new ConceptoBO(idConcepto,user.getConn());
                Concepto conceptoDto = conceptoBO.getConcepto();                
                conceptoDto.setDescuentoPorcentaje(descuentoPorcentajeConcepto);
                conceptoDto.setDescuentoMonto(descuentoMontoConcepto); 
                try{
                    new ConceptoDaoImpl(user.getConn()).update(conceptoDto.createPk(), conceptoDto);
                    out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                    ex.printStackTrace();
                }
                
            }else if (mode.equals("2")){//eliminar
                ConceptoBO conceptoBO = new ConceptoBO(idConcepto,user.getConn());
                Concepto conceptoDto = conceptoBO.getConcepto();                
                conceptoDto.setDescuentoPorcentaje(0);
                conceptoDto.setDescuentoMonto(0);                
                try{
                    new ConceptoDaoImpl(user.getConn()).update(conceptoDto.createPk(), conceptoDto);
                    out.print("<!--EXITO-->Registro borrado satisfactoriamente");
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo borrar el registro. Informe del error al administrador del sistema: " + ex.toString());
                    ex.printStackTrace();
                }
            }if (mode.equals("3")){//Editar descuento
                ConceptoBO conceptoBO = new ConceptoBO(idConcepto,user.getConn());
                Concepto conceptoDto = conceptoBO.getConcepto();                
                conceptoDto.setDescuentoPorcentaje(descuentoPorcentajeConcepto);
                conceptoDto.setDescuentoMonto(descuentoMontoConcepto);               
                try{
                    new ConceptoDaoImpl(user.getConn()).update(conceptoDto.createPk(), conceptoDto);
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
            *  other
            */
            
        }
    }else{
        out.print("<!--ERROR-->"+msgError);
    }

%>