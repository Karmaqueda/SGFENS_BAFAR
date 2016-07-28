<%-- 
    Document   : catEmpAsignacionInventario_ajax
    Created on : 18/02/2016, 01:36:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.dao.jdbc.EmpAsignacionInventarioDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpAsignacionInventario"%>
<%@page import="com.tsp.sct.bo.EmpAsignacionInventarioBO"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String msgError="";
    String msgExitoExtra="";
    
    /*
    *   Modos:
    *       -1  =  No se eligio (ERROR)
    * 
    *        1 = Activar/Desactivar registro Asignacion
    */
    
    int mode=-1;
    try{
        mode = Integer.parseInt(request.getParameter("mode"));
    }catch(NumberFormatException e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
     
    GenericValidator gc = new GenericValidator();
    Gson gson = new Gson();
    
    EmpAsignacionInventarioBO empAsignacionInventarioBO = new EmpAsignacionInventarioBO(user.getConn());
    
    /*
     * Procesamiento
     */
    if (mode==-1){
        msgError = "No se eligio una acción correcta. Reintente el llenado de los datos.";
    }else if(mode==1){
        //1 = Activar/Desactivar registro Asignacion
        
        //parametros
        int idAsignacionInventario = -1;
        //String codigoBarras = request.getParameter("codigo_barras")!=null?new String(request.getParameter("codigo_barras").getBytes("ISO-8859-1"),"UTF-8"):"";
        try{ idAsignacionInventario = Integer.parseInt(request.getParameter("id_asignacion_inventario")); }catch(Exception e){}
        
        if (idAsignacionInventario<=0)
            msgError += "<ul>No se indico un registro válido para activar/desactivar.";
        
        if (msgError.equals("")){
            try{
                EmpAsignacionInventario empAsignacionInventarioDto = empAsignacionInventarioBO.findEmpAsignacionInventariobyId(idAsignacionInventario);
                
                //activamos o desactivamos el registro
                int estatusActual = empAsignacionInventarioDto.getIdEstatus();
                empAsignacionInventarioDto.setIdEstatus(estatusActual==1? 2 : 1 );
                
                new EmpAsignacionInventarioDaoImpl(user.getConn()).update(empAsignacionInventarioDto.createPk(), empAsignacionInventarioDto);
                
                msgExitoExtra += "Registro actualizado satisfactoriamente";
            }catch(Exception ex){
                msgError += "<ul> Error al actualizar registro: " + ex.toString();
                ex.printStackTrace();
            }
        }
        
    }
    
    if (msgError.equals("") && mode!=0){
        out.print("<!--EXITO-->" + msgExitoExtra);
    }
    
%>
<%
    if (!msgError.equals("")){
        out.print("<!--ERROR-->"+msgError);
    }
%>