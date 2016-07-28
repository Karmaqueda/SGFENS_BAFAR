<%-- 
    Document   : guiaDinamica_ajax
    Created on : 1/07/2015, 04:04:10 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%

    String msgError="";
    /*
    *   Modos:
    *       -1  =  No se eligio (ERROR)
    *        0  =  Vaciar datos de sesion
    *        1  =  Cliente nuevo
    *        2  =  Agregar Producto
    *        3  =  Guia Cerrada
    *        4  =  No volver a mostrar la Guia
    *        5  =  Omitir mensaje de bienvenida
    *        6  =  Asignar Inventario a empleado
    *        7  =  Se dio clic en la accion de inventario del vendedor
    */

    int mode = -1;

    try{
        mode = Integer.parseInt(request.getParameter("mode"));
    }catch(NumberFormatException e){}
    
    String modulo =  request.getParameter("modulo") != null ? request.getParameter("modulo") : "";
    
    System.out.println("------------ EL MODO ES::::::: "+mode);
    System.out.println("------------ EL MODULO ES::::::: "+modulo);
    try{System.out.println("------------ EL MODULO EN SESION ES::::::: "+(String)session.getAttribute("modulo"));}catch(Exception e){System.out.println(e.getMessage());}

    if (mode==-1){
        msgError = "No se eligio una acción correcta. Reintente el llenado de los datos.";
    }else if (mode == 0){
        session.setAttribute("mode", null);
        session.setAttribute("mesajeActual", null);
        session.setAttribute("sesionCerrada", null);
        session.setAttribute("mostrarBienvenida", null);
        session.setAttribute("modulo", null);
        session.setAttribute("concatenarMensajeX", null);
        System.out.println("------------ VACIADO DE SESION ! ! ! ! ! !");
    }else if (mode == 1){//Clic modulo -> ente -> nuevo; solicita dar clic en nuevo
        //if(modulo.equals("clienteNuevo")){
            session.setAttribute("modulo", modulo);
        //}else if(modulo.equals("conceptoNuevo")){}
        session.setAttribute("mode", mode);
        session.setAttribute("mesajeActual", "Da Clic en \"Crear Nuevo\" ");
        session.setAttribute("tamanoGlobo", "chico");
    }else if(mode == 2){//se dio clic en nuevo
        String mensajeArmadoMostrar = "Ingresa los datos solicitados, los marcados con simbolo * (asterisco) y remarcados con rojo son obligatorios. ";
        if(session.getAttribute("modulo")!= null){
            System.out.println("-------ENTRO AQUI 1");
            modulo = (String)session.getAttribute("modulo");
            if(modulo.equals("clienteNuevo"))
                mensajeArmadoMostrar = "<img src=\"../../images/guiaDinamica/crearCliente.png\" title=\"Crear un cliente\" style=\"height: 20px;\"/></br></br>" + mensajeArmadoMostrar;
            else if(modulo.equals("conceptoNuevo"))
                mensajeArmadoMostrar = "<img src=\"../../images/guiaDinamica/crearProducto.png\" title=\"Crear un producto\" style=\"height: 20px;\"/></br></br>" + mensajeArmadoMostrar;
            else if(modulo.equals("empleadoNuevo"))
                mensajeArmadoMostrar = "<img src=\"../../images/guiaDinamica/crearVendedorMovil.png\" title=\"Crear un vendedor móvil\" style=\"height: 20px;\"/></br></br>" + mensajeArmadoMostrar;
            else if(modulo.equals("empleadoInventarioNuevo")){
                mensajeArmadoMostrar = "Selecciona el Concepto y su respectivo Alamcen del que deseas agregar al inventario y la cantidad. ";//no concatenamos el mensaje original de la variable "mensajeArmadoMostrar" la sobreescribimos                
            }else if(modulo.equals("empleadoCrearRutaNueva"))
                mensajeArmadoMostrar = "<img src=\"../../images/guiaDinamica/crearRutaLibre.png\" title=\"Crear una ruta libre y asignarla a un empleado\" style=\"height: 20px;\"/></br></br>" + mensajeArmadoMostrar;
            else if(modulo.equals("consultaInventarioEmpleado"))
                mensajeArmadoMostrar = "<img src=\"../../images/guiaDinamica/AsignarProducto.png\" title=\"Asignar producto a un vendedor\" style=\"height: 20px\"/></br></br> " + mensajeArmadoMostrar;
        
        System.out.println("moooooooodulo: "+modulo);
        }               
        session.setAttribute("mode", mode);
        session.setAttribute("mesajeActual", mensajeArmadoMostrar);
        session.setAttribute("tamanoGlobo", "medianoGrande");
    }else if(mode == 3){//Cerrar guia
        session.setAttribute("sesionCerrada", "1");
        System.out.println("------------ CERRANDO GUIA !!!!!!!!!!!!!!!!!!!!!!");
    }else if(mode == 4){//No volver a mostrar la Guia
        
        EmpresaBO empresaBO = new EmpresaBO(user.getConn());
        EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa());
        empresaPermisoAplicacionDto.setMostrarGuia(0);
        new EmpresaPermisoAplicacionDaoImpl(user.getConn()).update(empresaPermisoAplicacionDto.createPk(), empresaPermisoAplicacionDto);
        System.out.println("------------ NUNCA MAS VOLVER A VER LA GUIA!!!!!!!!!!!!!!!!!!!!!!!11");
        session.setAttribute("mode", mode);
    }else if(mode == 5){// Omitir mensaje de bienvenida
        session.setAttribute("mostrarBienvenida", "0");
        session.setAttribute("mode", mode);
        System.out.println("------------ OCULTAR MENSAJE DE BIENVENIDA!!!!!!!");
    }else if(mode == 6){
        session.setAttribute("modulo", modulo);
        session.setAttribute("mode", mode);
        String mensajeArmadoMostrar = "";
        if(modulo.equals("empleadoInventarioNuevo"))// Asignar Inventario a empleado
            mensajeArmadoMostrar = "<img src=\"../../images/guiaDinamica/AsignarProducto.png\" title=\"Asignar producto a un vendedor\" style=\"height: 20px\"/></br></br>"+"Da clic en la acción Inventario, de algún empleado <img src=\"../../images/guiaInteractiva/guiaInventario.png\"/> ";
        else if(modulo.equals("empleadoGeolocalizarNew"))// Geolocalizar a empleado
            mensajeArmadoMostrar = "<img src=\"../../images/guiaDinamica/geolocalizarEmpleado.png\" title=\"Geo localizar a un empleado en el mapa\" style=\"height: 20px;\"/></br></br>"+"Da clic en la acción Localización, de algún empleado <img src=\"../../images/guiaInteractiva/guiaLocalizacion.png\"/> ";
        session.setAttribute("mesajeActual", mensajeArmadoMostrar);
        session.setAttribute("tamanoGlobo", "medianoGrande");
    }else if(mode == 7){
        String mensajeArmadoMostrar = "";
        session.setAttribute("modulo", modulo);
        if(modulo.equals("consultaInventarioEmpleado")){// se dio clic en la accion de inventario del vendedor
               mensajeArmadoMostrar = "<img src=\"../../images/guiaDinamica/AsignarProducto.png\" title=\"Asignar producto a un vendedor\" style=\"height: 20px\"/></br></br>"+"En esta sección se muestra el inventario actual del empleado.";
               session.setAttribute("concatenarMensajeX", "mensaje1");
        }else if(modulo.equals("consultaGeoLocalizacionEmpleado")){// se dio clic en la accion de geolocalizacion del vendedor
               mensajeArmadoMostrar = "<img src=\"../../images/guiaDinamica/geolocalizarEmpleado.png\" title=\"Geo localizar a un empleado en el mapa\" style=\"height: 20px;\"/></br></br>"+"En esta sección se muestra la geo localización del empleado.";
        }
        session.setAttribute("mode", mode);
        session.setAttribute("mesajeActual", mensajeArmadoMostrar);
        session.setAttribute("tamanoGlobo", "medianoGrande");
        
    }

    if (msgError.equals("") && mode!=1 
            && mode!=12 && mode!=13
            && mode!=21){
        out.print("<!--EXITO-->");
    }
    
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
    </body>
</html>
