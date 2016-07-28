<%-- 
    Document   : catProveedors_ajax
    Created on : 26-oct-2012, 13:48:45
    Author     : ISCesarMartinez poseidon24@hotmail.com
--%>

<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensProveedorDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProveedor"%>
<%@page import="com.tsp.sct.bo.SGProveedorBO"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idProveedor = -1;
    String rfc ="";
    String razonSocial ="";
    String numeroProveedor ="";
    String descripcion="";
    
    String calle="";
    String numero="";
    String numeroInt="";
    String colonia="";
    String cp="";
    String municipio="";
    String estado="";
    String pais="";
    
    String lada="";
    String telefono="";
    String extension="";
    String celular="";
    String email="";
    String contacto="";
    
    int idVendedor = -1;
    int estatus = 2;//deshabilitado
    
    String nombreImagenSgfensProveedorProducto = "";
    String nombreEmpresaProveedor = "";
    int idCategoriaSgfensProveedorProducto = -1;
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idProveedor = Integer.parseInt(request.getParameter("idProveedor"));
    }catch(NumberFormatException ex){}
    rfc = request.getParameter("rfc")!=null?new String(request.getParameter("rfc").getBytes("ISO-8859-1"),"UTF-8"):"";
    razonSocial = request.getParameter("razonSocial")!=null?new String(request.getParameter("razonSocial").getBytes("ISO-8859-1"),"UTF-8"):"";
    numeroProveedor = request.getParameter("numeroProveedor")!=null?new String(request.getParameter("numeroProveedor").getBytes("ISO-8859-1"),"UTF-8"):"";
    descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";
    calle = request.getParameter("calle")!=null?new String(request.getParameter("calle").getBytes("ISO-8859-1"),"UTF-8"):"";
    numero = request.getParameter("numero")!=null?new String(request.getParameter("numero").getBytes("ISO-8859-1"),"UTF-8"):"";
    numeroInt = request.getParameter("numeroInt")!=null?new String(request.getParameter("numeroInt").getBytes("ISO-8859-1"),"UTF-8"):"";
    colonia = request.getParameter("colonia")!=null?new String(request.getParameter("colonia").getBytes("ISO-8859-1"),"UTF-8"):"";
    municipio = request.getParameter("municipio")!=null?new String(request.getParameter("municipio").getBytes("ISO-8859-1"),"UTF-8"):"";
    estado = request.getParameter("estado")!=null?new String(request.getParameter("estado").getBytes("ISO-8859-1"),"UTF-8"):"";
    pais = request.getParameter("pais")!=null?new String(request.getParameter("pais").getBytes("ISO-8859-1"),"UTF-8"):"";
    cp = request.getParameter("cp")!=null?new String(request.getParameter("cp").getBytes("ISO-8859-1"),"UTF-8"):"";
    lada = request.getParameter("lada")!=null?new String(request.getParameter("lada").getBytes("ISO-8859-1"),"UTF-8"):"";
    telefono = request.getParameter("telefono")!=null?new String(request.getParameter("telefono").getBytes("ISO-8859-1"),"UTF-8"):"";
    extension = request.getParameter("extension")!=null?new String(request.getParameter("extension").getBytes("ISO-8859-1"),"UTF-8"):"";
    celular = request.getParameter("celular")!=null?new String(request.getParameter("celular").getBytes("ISO-8859-1"),"UTF-8"):"";
    email = request.getParameter("email")!=null?new String(request.getParameter("email").getBytes("ISO-8859-1"),"UTF-8"):"";
    contacto = request.getParameter("contacto")!=null?new String(request.getParameter("contacto").getBytes("ISO-8859-1"),"UTF-8"):"";
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}
    try{
        idVendedor = Integer.parseInt(request.getParameter("idVendedor"));
    }catch(NumberFormatException ex){}
    
    nombreImagenSgfensProveedorProducto = request.getParameter("nombreImagenSgfensProveedorProducto")!=null?new String(request.getParameter("nombreImagenSgfensProveedorProducto").getBytes("ISO-8859-1"),"UTF-8"):"";
    nombreEmpresaProveedor = request.getParameter("nombreEmpresaProveedor")!=null?new String(request.getParameter("nombreEmpresaProveedor").getBytes("ISO-8859-1"),"UTF-8"):"";
    
    try{
        idCategoriaSgfensProveedorProducto = Integer.parseInt(request.getParameter("idCategoriaSgfensProveedorProducto"));
    }catch(NumberFormatException ex){}
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();
    
    EmpresaBO empresaBO = new EmpresaBO(user.getConn());
    EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa());     
        
    if(empresaPermisoAplicacionDto.getRfcPorNipCodigo() == 1 || empresaPermisoAplicacionDto.getRfcPorNipCodigo() == 2){
        if( (rfc.length() <= 10 && rfc.length() >= 6)){
            
        }else{
            if(!gc.isRFC(rfc) && !gc.isRuc(rfc))
                msgError += "<ul>El dato 'RFC"+(empresaPermisoAplicacionDto.getRfcPorNipCodigo() == 1?"/NIP":empresaPermisoAplicacionDto.getRfcPorNipCodigo() == 2?"/RUC":"")+"' no es válido.";
        }
    }else{
        if(!gc.isRFC(rfc))
        msgError += "<ul>El dato 'rfc' no es válido.";
    }
    if(!gc.isValidString(razonSocial, 1, 200))
        msgError += "<ul>El dato 'razon Social' es requerido.";
    /*if(!gc.isValidString(numeroProveedor, 1, 15))
        msgError += "<ul>El dato 'nombre' es requerido";*/
    if(!gc.isValidString(descripcion, 1, 250))
        msgError += "<ul>El dato 'descripcion' es inválido. Mínimo 1 y máximo 250 carácteres.";
    if(!gc.isValidString(calle, 1, 100))
        msgError += "<ul>El dato 'calle' es requerido";
    if(!gc.isValidString(numero, 1, 30))
        msgError += "<ul>El dato 'numero' es requerido";
    if(!gc.isValidString(numeroInt, 0, 30))
        msgError += "<ul>El dato 'numero Interior' es requerido";
    if(empresaPermisoAplicacionDto.getRfcPorNipCodigo() == 0){
        if(!gc.isCodigoPostal(cp))
            msgError += "<ul>El dato 'Codigo Postal' no es válido.";
    }
    if(!gc.isValidString(colonia, 1, 100))
        msgError += "<ul>El dato 'colonia' es requerido";
    if(!gc.isValidString(municipio, 1, 100))
        msgError += "<ul>El dato 'municipio' es requerido";
    if(!gc.isValidString(estado, 1, 100))
        msgError += "<ul>El dato 'estado' es requerido";
    if(!gc.isValidString(pais, 1, 100))
        msgError += "<ul>El dato 'pais' es requerido";
    if(!gc.isNumeric(lada, 2, 3))
        msgError += "<ul>El dato 'lada' es inválido. Mínimo 2 y máximo 3 números.";
    if(!gc.isNumeric(telefono, 7, 8))
        msgError += "<ul>El dato 'Telefono' es incorrecto. Minimo 7 y maximo 8 numeros.";
    if(!gc.isValidString(extension, 0, 5))
        msgError += "<ul>El dato 'extension' es inválido. ";
    if(!gc.isValidString(celular, 0, 11))
        msgError += "<ul>El dato 'celular' es requerido. Minimo 10 y maximo 11 numeros.";
    if(!gc.isValidString(contacto, 1, 100))
        msgError += "<ul>El dato 'nombre de contacto' es requerido";
    if(email.equals(""))
        msgError += "<ul>El dato 'correo' es requerido. <br/>";
    if(!gc.isEmail(email))
        msgError += "<ul>El dato 'Correo electr&oacute;nico' es incorrecto. <br/>";
    if(idProveedor <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'proveedor' es requerido";
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idProveedor>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                SGProveedorBO proveedorBO = new SGProveedorBO(idProveedor,user.getConn());
                SgfensProveedor proveedorDto = proveedorBO.getSgfensProveedor();
                
                proveedorDto.setIdEstatus(estatus);
                proveedorDto.setRfc(rfc);
                proveedorDto.setRazonSocial(razonSocial);
                proveedorDto.setNumeroProveedor(numeroProveedor);
                proveedorDto.setDescripcion(descripcion);
                
                proveedorDto.setCalle(calle);
                proveedorDto.setNumero(numero);
                proveedorDto.setNumeroInterior(numeroInt);
                proveedorDto.setCodigoPostal(cp);
                proveedorDto.setColonia(colonia);
                proveedorDto.setMunicipio(municipio);
                proveedorDto.setEstado(estado);
                proveedorDto.setPais(pais);
                
                proveedorDto.setLada(lada);
                proveedorDto.setTelefono(telefono);
                proveedorDto.setExtension(extension);
                proveedorDto.setCelular(celular);
                proveedorDto.setCorreo(email);
                proveedorDto.setContacto(contacto);
                proveedorDto.setNombreImagenProveedor(nombreImagenSgfensProveedorProducto);
                proveedorDto.setIdCategoriaProveedor(idCategoriaSgfensProveedorProducto);
                proveedorDto.setNombreEmpresa(nombreEmpresaProveedor);
                
                try{
                    new SgfensProveedorDaoImpl(user.getConn()).update(proveedorDto.createPk(), proveedorDto);

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
                 * Creamos el registro de Proveedor
                 */
                SgfensProveedor proveedorDto = new SgfensProveedor();
                SgfensProveedorDaoImpl proveedorsDaoImpl = new SgfensProveedorDaoImpl(user.getConn());
                
                proveedorDto.setIdEstatus(estatus);
                proveedorDto.setRfc(rfc);
                proveedorDto.setRazonSocial(razonSocial);
                proveedorDto.setNumeroProveedor(numeroProveedor);
                proveedorDto.setDescripcion(descripcion);
                
                proveedorDto.setCalle(calle);
                proveedorDto.setNumero(numero);
                proveedorDto.setNumeroInterior(numeroInt);
                proveedorDto.setCodigoPostal(cp);
                proveedorDto.setColonia(colonia);
                proveedorDto.setMunicipio(municipio);
                proveedorDto.setEstado(estado);
                proveedorDto.setPais(pais);
                
                proveedorDto.setLada(lada);
                proveedorDto.setTelefono(telefono);
                proveedorDto.setExtension(extension);
                proveedorDto.setCelular(celular);
                proveedorDto.setCorreo(email);
                proveedorDto.setContacto(contacto);
                
                proveedorDto.setIdEmpresa(idEmpresa);
                proveedorDto.setNombreImagenProveedor(nombreImagenSgfensProveedorProducto);
                proveedorDto.setIdCategoriaProveedor(idCategoriaSgfensProveedorProducto);
                proveedorDto.setNombreEmpresa(nombreEmpresaProveedor);
                /**
                 * Realizamos el insert
                 */
                proveedorsDaoImpl.insert(proveedorDto);

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