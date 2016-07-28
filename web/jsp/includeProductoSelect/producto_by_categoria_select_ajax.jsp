<%-- 
    Document   : producto_by_categoria_select_ajax
    Created on : 04-ene-2013, 10:40:30
    Author     : ISCesarMartinez
--%>

<%@page import="com.tsp.sct.bo.CategoriaBO"%>
<%@page import="com.tsp.sct.dao.dto.Categoria"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String msgError="";
    
    /*
    *   Modos:
    *       -1  =  No se eligio (ERROR)
    *        1  =  Recuperar Listado de Productos de Categoría
    *        2  =  Recuperar Listado de SubCategorias de una Categoría Padre
    *        
    *
    */
    
    int mode=-1;
    try{
        mode = Integer.parseInt(request.getParameter("mode"));
    }catch(NumberFormatException e){}
    int idEmpresa = user.getUser().getIdEmpresa();
    
    boolean recargarListaProductos=false;
    boolean crearSelectSubCategoria=false;
    
    Concepto[] productos = new Concepto[0];
    Categoria[] categorias = new Categoria[0];
    
    int nivelSubCategorias = 0;
    
    Encrypter encrypter = new Encrypter();
    
    /*
     * Procesamiento
     */
    if (mode==-1){
        msgError = "No se eligio una acción correcta. Reintente el llenado de los datos.";
    }else if (mode==1){
        //1  =  Recuperar Listado de Productos de Categoría
        
        int idCategoria = -1;
        try{ idCategoria = Integer.parseInt(request.getParameter("idCategoria")); }catch(Exception e){}
        
        if (idCategoria<=-1)
            msgError = "<ul>No se selecciono una categoría válida";
        
        if (msgError.equals("")){
            if (idCategoria>0){
                productos = new ConceptoBO(user.getConn()).findConceptos(-1, idEmpresa, 0, 0, " AND ID_ESTATUS=1 AND (ID_SUBCATEGORIA="+idCategoria+
                        " OR ID_SUBCATEGORIA2="+idCategoria+" OR  ID_SUBCATEGORIA3="+idCategoria+" OR ID_SUBCATEGORIA4="+idCategoria+" )"
                        + " AND MATERIA_PRIMA=0 ");
            }else{
                productos = new ConceptoBO(user.getConn()).findConceptos(-1, idEmpresa, 0, 0, " AND ID_ESTATUS=1 AND (ID_SUBCATEGORIA<=0 OR ID_SUBCATEGORIA IS NULL) ");
            }
        }
        
        if (msgError.equals(""))
            recargarListaProductos = true;
        
    }else if (mode==2){
        //2  =  Recuperar Listado de SubCategorias de una Categoría Padre
        int idCategoriaPadre = -1;
        try{ idCategoriaPadre = Integer.parseInt(request.getParameter("idCategoriaPadre")); }catch(Exception e){}
        
        nivelSubCategorias = -1;
        try{ nivelSubCategorias = Integer.parseInt(request.getParameter("nivelSubCategorias")); }catch(Exception e){}
        
        if (idCategoriaPadre<=-1)
            msgError = "<ul>No se selecciono una categoría válida";
        
        if (nivelSubCategorias<=-1)
            msgError = "<ul>No se asigno un valor correcto del nivel de SubCategoria.";
        
        if (msgError.equals("")){
            if (idCategoriaPadre>0){
                categorias = new CategoriaBO(user.getConn()).findCategorias(-1, idEmpresa, 0, 0, " AND id_categoria_padre = " + idCategoriaPadre);
            }else{
                categorias = new Categoria[0];
                //crearSelectSubCategoria = false;
            }
            nivelSubCategorias = nivelSubCategorias + 1;
        }
        
        if (msgError.equals(""))
            crearSelectSubCategoria = true;
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
    
    if(crearSelectSubCategoria){
        if (msgError.equals("")){
            if (categorias.length>0){
%>
                &raquo;
                <select id="select_categoria_<%=nivelSubCategorias%>" name="select_categoria_<%=nivelSubCategorias%>" 
                    onchange="cargaSubCategorias(this.value, this.options[this.selectedIndex].innerHTML, <%=nivelSubCategorias%>);" style="width: 120px;"
                    size="5">
                <%
                for (Categoria item : categorias){
                %>
                    <option value="<%=item.getIdCategoria() %>"><%=item.getNombreCategoria() %></option>
                <%
            }
                %>
                </select>
                <div id="div_subcategorias_nivel_<%=nivelSubCategorias%>" name="div_subcategorias_nivel_<%=nivelSubCategorias%>" 
                    style="display: inline-block; "></div>
<%
           }else{
%>
            &nbsp;&nbsp; &laquo; SIN Subcategorias &raquo;
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