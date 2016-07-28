<%-- 
    Document   : product_by_parametro_select_ajax
    Created on : 29/06/2015, 01:20:35 PM
    Author     : HpPyme
--%>

<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String msgError="";
    
    /*
    *   Modos:
    *       -1  =  No se eligio (ERROR)
    *        1  =  Recuperar Listado de Productos
    *        
    *
    */
    
    int mode=-1;
    try{
        mode = Integer.parseInt(request.getParameter("mode"));
    }catch(NumberFormatException e){}
    int idEmpresa = user.getUser().getIdEmpresa();
    
    boolean recargarListaProductos=false;   
    Concepto[] productos = new Concepto[0];    
    Encrypter encrypter = new Encrypter();
    
    /*
     * Procesamiento
     */
    if (mode==-1){
        msgError = "No se eligio una acción correcta. Reintente el llenado de los datos.";
    }else if (mode==1){
        //1  =  Recuperar Listado de Productos de Categoría
        
        
        String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
        
        
        
        if (msgError.equals("")){
            if(!buscar.trim().equals("")){
                String qry = " AND ID_ESTATUS!=2 AND  ( NOMBRE_DESENCRIPTADO LIKE '%" + buscar + "%' "
                        + " OR CLAVE LIKE '%" + buscar + "%' OR ID_MARCA IN (SELECT ID_MARCA FROM marca WHERE NOMBRE LIKE '%" + buscar + "%' ) )"
                        + " AND MATERIA_PRIMA=0 ";
                productos = new ConceptoBO(user.getConn()).findConceptos(-1, idEmpresa, 0,300, qry);           
            }else{
                productos = new ConceptoBO(user.getConn()).findConceptos(-1, idEmpresa, 0,300, " AND ID_ESTATUS!=2 " );
            }
        }
        
        if (msgError.equals(""))
            recargarListaProductos = true;
        
    }
    
    if (msgError.equals("")){
        out.print("<!--EXITO-->");
    }
    
    if(recargarListaProductos){
        if (msgError.equals("")){
            for (Concepto item : productos){
                String nombreConcepto = encrypter.decodeString(item.getNombre());
%>
                <option value="<%=item.getIdConcepto() %>"><%=nombreConcepto%></option>
<%
           }
        }
    }
    
  
    
%>

<%
    if (!msgError.equals("")){
        out.print("<!--ERROR-->"+msgError);
    }
%>
