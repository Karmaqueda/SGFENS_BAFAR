<%-- 
    Document   : catGeocercas_ajax
    Created on : 3/05/2013, 04:18:00 PM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.dao.jdbc.EmpleadoDaoImpl"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.GeocercaBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.GeocercaDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Geocerca"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idGeocerca = -1;
    String radio ="";
    String centro ="";  
    String coordenadasCuadrado = "";
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idGeocerca = Integer.parseInt(request.getParameter("idGeocerca"));
    }catch(NumberFormatException ex){}
    radio = request.getParameter("radio")!=null?new String(request.getParameter("radio").getBytes("ISO-8859-1"),"UTF-8"):"";
    centro = request.getParameter("centro")!=null?new String(request.getParameter("centro").getBytes("ISO-8859-1"),"UTF-8"):"";
    coordenadasCuadrado = request.getParameter("coordenadasCuadrado")!=null?new String(request.getParameter("coordenadasCuadrado").getBytes("ISO-8859-1"),"UTF-8"):"";
    
        
    centro = centro.replace("(", "");
    centro = centro.replace(")", "");
    coordenadasCuadrado = coordenadasCuadrado.replace("(", "");
    coordenadasCuadrado = coordenadasCuadrado.replace(")", "");
    
    System.out.println("COORDENADAS CENTRO: "+centro);
    System.out.println("COORDENADAS CUADRADO: "+coordenadasCuadrado);
    
    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    /*if(!gc.isValidString(nombreGeocerca, 1, 30))
        msgError += "<ul>El dato 'nombre' es requerido.";
    if(!gc.isValidString(descripcion, 1, 100))
        msgError += "<ul>El dato 'descripción' es requerido";
    if(idGeocerca <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'geocerca' es requerido";   
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idGeocerca>0){
            if(mode.equals("eliminarGeocerca")){
                GeocercaBO geocercaBO = new GeocercaBO(idGeocerca,user.getConn());
                Geocerca geocercaDto = geocercaBO.getGeocerca();                
                geocercaDto.setIdEstatus(2);
                
                try{
                    new GeocercaDaoImpl(user.getConn()).update(geocercaDto.createPk(), geocercaDto);
                    /////**TAMBIEN A TODOS LOS EMPLEADOS QUE TENIAN ESTA GEOCERCA DE LA DESASIGNAMOS
                    Empleado[] empls = new EmpleadoBO(user.getConn()).findEmpleados(0, idEmpresa, 0, 0, " AND ID_GEOCERCA = "+idGeocerca);
                    for(Empleado empleadoDto : empls){
                        empleadoDto.setIdGeocerca(0);
                        new EmpleadoDaoImpl(user.getConn()).update(empleadoDto.createPk(), empleadoDto);
                    }
                    /////**
                    
                    out.print("<!--EXITO-->Registro eliminado satisfactoriamente");
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo eliminado el registro. Informe del error al administrador del sistema: " + ex.toString());
                    ex.printStackTrace();
                }
            }else{
            /*
            * Editar
            */
                GeocercaBO geocercaBO = new GeocercaBO(idGeocerca,user.getConn());
                Geocerca geocercaDto = geocercaBO.getGeocerca(); 
                
                
                if(mode.equals("circulo")){
                    try {                

                        /**
                         * Creamos el registro de la logistica Circulo
                         */
                       

                        geocercaDto.setIdEstatus(1);
                        geocercaDto.setIdEmpresa(idEmpresa);
                        geocercaDto.setTipoGeocerca(1);
                        geocercaDto.setCoordenadas(radio+","+centro);

                        /**
                         * Realizamos el update
                         */
                        try{
                            new GeocercaDaoImpl(user.getConn()).update(geocercaDto.createPk(), geocercaDto);

                            out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
                        }catch(Exception ex){
                            out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                            ex.printStackTrace();
                        }

                     

                    }catch(Exception e){
                        e.printStackTrace();
                        msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
                    }
                }else if(mode.equals("cuadrado")){
                    try {                

                        /**
                         * Creamos el registro de la logistica Cuadrado
                         */


                        geocercaDto.setIdEstatus(1);
                        geocercaDto.setIdEmpresa(idEmpresa);
                        geocercaDto.setTipoGeocerca(2);//2 tipo cuadrado
                        geocercaDto.setCoordenadas(coordenadasCuadrado);//Coordenadas: A,B,C,D; las coordenadas de los puntos (A,B) representan el punto inferior Izquierdo, (C,D) superior derecho
                        /**
                         * Realizamos el update
                         */
                        try{
                            new GeocercaDaoImpl(user.getConn()).update(geocercaDto.createPk(), geocercaDto);

                            out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
                        }catch(Exception ex){
                            out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                            ex.printStackTrace();
                        }

                    }catch(Exception e){
                        e.printStackTrace();
                        msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
                    }
                }else if(mode.equals("poligono")){
                    try {                

                     String[] polygon = request.getParameterValues("coordenadasPoligono");
                     String puntosPoligono = "";


                     for(int i = 0; i< polygon.length; i++){
                         puntosPoligono += polygon[i];

                        if(!(polygon.length - 1 == i)){
                             puntosPoligono += ",";
                        }        

                     }
                     //formato String  Latitud Punto A, Longitud Punto A, Latitud Punto B, Longitud Punto B
                     puntosPoligono =  puntosPoligono.replaceAll("\\),\\(", ",");
                     puntosPoligono =  puntosPoligono.replaceAll("\\)", "");
                     puntosPoligono =  puntosPoligono.replaceAll("\\(", "");
                     //puntosPoligono =  puntosPoligono.replaceAll(" ", "");


                    /**
                     * Creamos el registro de la logistica Poligono
                     */
                    

                    geocercaDto.setIdEstatus(1);
                    geocercaDto.setIdEmpresa(idEmpresa);
                    geocercaDto.setTipoGeocerca(3);//3 tipo poligono
                    geocercaDto.setCoordenadas(puntosPoligono);//Coordenadas
                        /**
                         * Realizamos el update
                         */
                        try{
                            new GeocercaDaoImpl(user.getConn()).update(geocercaDto.createPk(), geocercaDto);

                            out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
                        }catch(Exception ex){
                            out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                            ex.printStackTrace();
                        }

                    }catch(Exception e){
                        e.printStackTrace();
                        msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
                    }
                }
                
                
                
                
            }
        }else{
            /*
            *  Nuevo
            */
            
            if(mode.equals("circulo")){
                try {                

                    /**
                     * Creamos el registro de la logistica Circulo
                     */
                    Geocerca geocercaDto = new Geocerca();
                    GeocercaDaoImpl geocercasDaoImpl = new GeocercaDaoImpl(user.getConn());

                    geocercaDto.setIdEstatus(1);
                    geocercaDto.setIdEmpresa(idEmpresa);
                    geocercaDto.setTipoGeocerca(1);
                    geocercaDto.setCoordenadas(radio+","+centro);

                    /**
                     * Realizamos el insert
                     */
                    geocercasDaoImpl.insert(geocercaDto);

                    out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");

                }catch(Exception e){
                    e.printStackTrace();
                    msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
                }
            }else if(mode.equals("cuadrado")){
                try {                
                
                /**
                 * Creamos el registro de la logistica Cuadrado
                 */
                Geocerca geocercaDto = new Geocerca();
                GeocercaDaoImpl geocercasDaoImpl = new GeocercaDaoImpl(user.getConn());
                
                geocercaDto.setIdEstatus(1);
                geocercaDto.setIdEmpresa(idEmpresa);
                geocercaDto.setTipoGeocerca(2);//2 tipo cuadrado
                geocercaDto.setCoordenadas(coordenadasCuadrado);//Coordenadas: A,B,C,D; las coordenadas de los puntos (A,B) representan el punto inferior Izquierdo, (C,D) superior derecho
                /**
                 * Realizamos el insert
                 */
                geocercasDaoImpl.insert(geocercaDto);

                out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
            
                }catch(Exception e){
                    e.printStackTrace();
                    msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
                }
            }else if(mode.equals("poligono")){
                try {                
                
                 String[] polygon = request.getParameterValues("coordenadasPoligono");
                 String puntosPoligono = "";
                 
                                  
                 for(int i = 0; i< polygon.length; i++){
                     puntosPoligono += polygon[i];
                     
                    if(!(polygon.length - 1 == i)){
                         puntosPoligono += ",";
                    }        
                     
                 }
                 //formato String  Latitud Punto A, Longitud Punto A, Latitud Punto B, Longitud Punto B
                 puntosPoligono =  puntosPoligono.replaceAll("\\),\\(", ",");
                 puntosPoligono =  puntosPoligono.replaceAll("\\)", "");
                 puntosPoligono =  puntosPoligono.replaceAll("\\(", "");
                 //puntosPoligono =  puntosPoligono.replaceAll(" ", "");
                 
                 
                /**
                 * Creamos el registro de la logistica Poligono
                 */
                Geocerca geocercaDto = new Geocerca();
                GeocercaDaoImpl geocercasDaoImpl = new GeocercaDaoImpl(user.getConn());
                
                geocercaDto.setIdEstatus(1);
                geocercaDto.setIdEmpresa(idEmpresa);
                geocercaDto.setTipoGeocerca(3);//3 tipo poligono
                geocercaDto.setCoordenadas(puntosPoligono);//Coordenadas
                /**
                 * Realizamos el insert
                 */
                 geocercasDaoImpl.insert(geocercaDto);
                

                out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
            
                }catch(Exception e){
                    e.printStackTrace();
                    msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
                }
            }
            
            
            
        }
    }else{
        out.print("<!--ERROR-->"+msgError);
    }

%>