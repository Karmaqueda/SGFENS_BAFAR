<%-- 
    Document   : catUsuarios_ajax
    Created on : 26-oct-2012, 13:48:45
    Author     : ISCesarMartinez poseidon24@hotmail.com
--%>

<%@page import="com.tsp.sct.bo.ZonaHorariaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.BitacoraCreditosOperacionBO"%>
<%@page import="com.tsp.sct.bo.BitacoraCreditosOperacionBO"%>
<%@page import="com.tsp.sct.dao.dto.DispositivoMovil"%>
<%@page import="com.tsp.sct.bo.DispositivoMovilBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.PretoCaracteristicasConsolaBO"%>
<%@page import="com.tsp.sct.dao.dto.PretoCaracteristicasConsola"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.bo.PasswordBO"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensVendedorDatosDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SgfensVendedorDatos"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.UsuariosDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Usuarios"%>
<%@page import="com.tsp.sct.dao.jdbc.DatosUsuarioDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.DatosUsuario"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idUsuarios = -1;
    String usuario ="";
    String password ="";
    String nombre ="";
    String apaterno="";
    String amaterno="";
    String direccion="";
    String lada ="";
    String telefono="";
    String celular="";
    String email="";
    int idRol = -1;
    int estatus = 2;//deshabilitado
    int firmado = -1; //Cambiar a 1 cuando el usuario en cuestion cambia su contraseña
    int idSucursal = idEmpresa;
    
    double porcentajeComisiones = 0;
    double sueldoMensual = 0;
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idUsuarios = Integer.parseInt(request.getParameter("idUsuarios"));
    }catch(NumberFormatException ex){}
    usuario = request.getParameter("usuario")!=null?new String(request.getParameter("usuario").getBytes("ISO-8859-1"),"UTF-8"):"";
    password = request.getParameter("password")!=null?new String(request.getParameter("password").getBytes("ISO-8859-1"),"UTF-8"):"";
    nombre = request.getParameter("nombre")!=null?new String(request.getParameter("nombre").getBytes("ISO-8859-1"),"UTF-8"):"";
    apaterno = request.getParameter("apaterno")!=null?new String(request.getParameter("apaterno").getBytes("ISO-8859-1"),"UTF-8"):"";
    amaterno = request.getParameter("amaterno")!=null?new String(request.getParameter("amaterno").getBytes("ISO-8859-1"),"UTF-8"):"";
    direccion = request.getParameter("direccion")!=null?new String(request.getParameter("direccion").getBytes("ISO-8859-1"),"UTF-8"):"";
    lada = request.getParameter("lada")!=null?new String(request.getParameter("lada").getBytes("ISO-8859-1"),"UTF-8"):"";
    telefono = request.getParameter("telefono")!=null?new String(request.getParameter("telefono").getBytes("ISO-8859-1"),"UTF-8"):"";
    celular = request.getParameter("celular")!=null?new String(request.getParameter("celular").getBytes("ISO-8859-1"),"UTF-8"):"";
    email = request.getParameter("email")!=null?new String(request.getParameter("email").getBytes("ISO-8859-1"),"UTF-8"):"";
    try{ estatus = Integer.parseInt(request.getParameter("estatus")); }catch(NumberFormatException ex){}
    try{ idRol = Integer.parseInt(request.getParameter("idRol")); }catch(NumberFormatException ex){}
    try{ firmado = Integer.parseInt(request.getParameter("firmado")); }catch(NumberFormatException ex){}
    try{ idSucursal = Integer.parseInt(request.getParameter("idSucursal")); }catch(NumberFormatException ex){}
    
    try{
        porcentajeComisiones = Double.parseDouble(request.getParameter("porcentaje_comisiones"));
    }catch(NumberFormatException ex){}
    try{
        sueldoMensual = Double.parseDouble(request.getParameter("sueldo_mensual"));
    }catch(NumberFormatException ex){}
    
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();
    PasswordBO passwordBO = new PasswordBO();
    if(!gc.isValidString(usuario, 5, 30) && (mode.equals("")))
        msgError += "<ul>El dato 'usuario' no es válido, al menos 5 caracteres, máximo 30.";
    if(password.equals("") && (mode.equals("")))
        msgError += "<ul>El dato 'Contraseña' es requerido";
    //if(!gc.isPasswordSeguro(password) && (mode.equals("")))
        //msgError += "<ul>La contraseña es incorrecta ya que no cumple con los requerimientos de seguridad mínimos. <br/>";
    if(!gc.isValidString(nombre, 1, 100))
        msgError += "<ul>El dato 'nombre' es requerido";
    if(!gc.isValidString(apaterno, 1, 100))
        msgError += "<ul>El dato 'apellido paterno' es requerido";
    if(!gc.isValidString(amaterno, 1, 100))
        msgError += "<ul>El dato 'apellido materno' es requerido";
    if(!gc.isValidString(direccion, 1, 100))
        msgError += "<ul>El dato 'dirección' es requerido";
    if(!gc.isNumeric(lada, 2, 3))
        msgError += "<ul>El dato 'lada' es inválido. Mínimo 2 y máximo 3 números.";
    if(!gc.isNumeric(telefono, 7, 8))
        msgError += "<ul>El dato 'Telefono' es incorrecto. Minimo 7 y maximo 8 numeros.";
    if(celular.trim().length()>0 && !gc.isNumeric(celular, 10, 11))
        msgError += "<ul>El dato 'celular' es incorrecto. Minimo 10 y maximo 11 numeros. O puede dejarlo vacío (opcional)";
    if(email.equals(""))
        msgError += "<ul>El dato 'correo' es requerido. <br/>";
    if(!gc.isEmail(email))
        msgError += "<ul>El dato 'Correo electr&oacute;nico' es incorrecto. <br/>";
    if(idUsuarios <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'usuario' es requerido";
    if(idRol<=0)
        msgError += "<ul>El dato 'Rol' es requerido";
    if(mode.equals("") && new LdapDaoImpl(user.getConn()).findWhereUsuarioEquals(usuario).length>0)
        msgError += "<ul>El nombre de usuario '"+ usuario +"' ya esta siendo utilizado en el sistema, elija otro.";

    //Si se cambio la contraseña se verifica que sea una válida
    if (password.trim().length()>0){
        try{
            passwordBO.isValidPassword(usuario, password);
        }catch(Exception ex){
            msgError += "<ul>"+ex.getMessage();
        }
    }
    
    if(msgError.equals("")){
        if(idUsuarios>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                UsuarioBO usuarioBO = new UsuarioBO(idUsuarios);
                
                DatosUsuario datosUsuarioDto = usuarioBO.getDatosUsuario();
                Usuarios usuarioDto = usuarioBO.getUser();
                Ldap ldapDto = usuarioBO.getLdap();
                SgfensVendedorDatos datosVendedorDto = usuarioBO.getDatosVendedor();
                
                SgfensVendedorDatosDaoImpl datosVendedorDao = new SgfensVendedorDatosDaoImpl(user.getConn());
                
                usuarioDto.setIdEstatus(estatus);
                usuarioDto.setIdRoles(idRol);
                usuarioDto.setIdEmpresa(idSucursal);
                
                ldapDto.setEmail(email);
                if (firmado==1)
                    ldapDto.setFirmado(1);
                
                datosUsuarioDto.setApellidoMat(amaterno);
                datosUsuarioDto.setApellidoPat(apaterno);
                datosUsuarioDto.setNombre(nombre);
                datosUsuarioDto.setDireccion(direccion);
                datosUsuarioDto.setLada(lada);
                datosUsuarioDto.setTelefono(telefono);
                datosUsuarioDto.setCelular(celular);
                datosUsuarioDto.setCorreo(email);
                
                try{
                    if (datosVendedorDto==null){
                        //Cuando no existe registro de datos como vendedor
                        datosVendedorDto = new SgfensVendedorDatos();
                        datosVendedorDto.setIdUsuario(usuarioDto.getIdUsuarios());
                        datosVendedorDto.setPorcentajeComisiones(porcentajeComisiones);
                        datosVendedorDto.setSueldoMensual(sueldoMensual);
                        datosVendedorDao.insert(datosVendedorDto);
                    }else{
                        datosVendedorDto.setPorcentajeComisiones(porcentajeComisiones);
                        datosVendedorDto.setSueldoMensual(sueldoMensual);
                        datosVendedorDao.update(datosVendedorDto.createPk(), datosVendedorDto);
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }   
                
                
                
                try{
                    new UsuariosDaoImpl(user.getConn()).update(usuarioDto.createPk(), usuarioDto);
                    new LdapDaoImpl(user.getConn()).update(ldapDto.createPk(), ldapDto);
                    new DatosUsuarioDaoImpl(user.getConn()).update(datosUsuarioDto.createPk(), datosUsuarioDto);
                    
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
            
            //////////VALIDAMOS CUANTOS REGISTROS TIENE PARA VER SI LO DEJAMOS CREAR O NO, DEPENDIENDO DEL PAQUETE:
            EmpresaBO empresaBO = new EmpresaBO(user.getConn());
            EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa());     
            /*
            PretoCaracteristicasConsola consola = new PretoCaracteristicasConsola();
            PretoCaracteristicasConsolaBO caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO(user.getConn());
            caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("USUARIOS",user.getConn());//RECUPERAMOS EL OBJETO DE LOS PERMISOS
            consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
            //SI HAY UN NUMERO LO CONVERTIMOS A INT PARA HACER LA VALIDACION PORTERIOR EN EL IF
            try{
                caracteristicasConsolaBO.setTpv_gratis(Integer.parseInt(consola.getTpvGratis()));
            }catch(Exception e){}
            try{
                caracteristicasConsolaBO.setTpv_emprendedor(Integer.parseInt(consola.getTpvEmprendedor()));
            }catch(Exception e){}
            try{
                caracteristicasConsolaBO.setTpv_comerciante(Integer.parseInt(consola.getTpvComerciante()));
            }catch(Exception e){}
            try{
                caracteristicasConsolaBO.setTpv_mipyme(Integer.parseInt(consola.getTpvMipyme()));
            }catch(Exception e){}
            try{
                caracteristicasConsolaBO.setEvc(Integer.parseInt(consola.getEvc()));
            }catch(Exception e){}
            try{
                caracteristicasConsolaBO.setPretoriano_erp(Integer.parseInt(consola.getPretorianoErp()));
            }catch(Exception e){}
            try{
                caracteristicasConsolaBO.setCbb(Integer.parseInt(consola.getCbb()));
            }catch(Exception e){}
            */
            //RECUPERAMOS LA LONGITUD DEL NUMERO DE RESGITROS QUE SE ENCUANTRAN CREADOS CON ESTATUS ACTIVO
            Empresa empresaMatriz = empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa());
            
            /*DispositivoMovilBO ubo = new DispositivoMovilBO(user.getConn());
            DispositivoMovil[] lista = new DispositivoMovil[0];
            lista = ubo.findDispositivosMoviles(0, empresaMatriz.getIdEmpresa(), 0, 0, "");*/

            UsuariosBO usuariosBO = new UsuariosBO();
            Usuarios[] listaUsuarios = new Usuarios[0];
            listaUsuarios = usuariosBO.findUsuarios(-1, empresaMatriz.getIdEmpresa(), 0, 0, " AND ID_ESTATUS <> 2 ");

            //System.out.println("---------------DISPOSITIVOS MOVILES: "+lista.length);
            System.out.println("---------------USUARIOS: "+listaUsuarios.length);

            /*int licenciasUsadas = 0;
            licenciasUsadas = lista.length + listaUsuarios.length;


            System.out.println("---------------LICENCIAS USADAS: "+licenciasUsadas);
            
            if(licenciasUsadas < empresaPermisoAplicacionDto.getAccesoSgfensNumLicenciasMoviles()){*/
                
                //if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && ("ILIMITADO".equals(consola.getTpvGratis())||lista.length<caracteristicasConsolaBO.getTpv_gratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && ("ILIMITADO".equals(consola.getTpvEmprendedor())||lista.length<caracteristicasConsolaBO.getTpv_emprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && ("ILIMITADO".equals(consola.getTpvComerciante())||lista.length<caracteristicasConsolaBO.getTpv_comerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && ("ILIMITADO".equals(consola.getTpvMipyme())||lista.length<caracteristicasConsolaBO.getTpv_mipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && ("ILIMITADO".equals(consola.getEvc())||lista.length<caracteristicasConsolaBO.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && ("ILIMITADO".equals(consola.getPretorianoErp())||lista.length<caracteristicasConsolaBO.getPretoriano_erp())) || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && ("ILIMITADO".equals(consola.getCbb())||lista.length<caracteristicasConsolaBO.getCbb())) ){
                if (listaUsuarios.length < empresaPermisoAplicacionDto.getMaxNumUsuarios()){
                        
                    /**
                     * Creamos el registro de Datos de usuario
                     */
                    DatosUsuario datosUsuarioDto = new DatosUsuario();
                    DatosUsuarioDaoImpl datosUsuarioDaoImpl = new DatosUsuarioDaoImpl(user.getConn());

                    int idDatosUsuarioNuevo;

                    try {                
                        DatosUsuario ultimoRegistroDatosUsuarios = datosUsuarioDaoImpl.findLast();
                        idDatosUsuarioNuevo = ultimoRegistroDatosUsuarios.getIdDatosUsuario() + 1;

                        datosUsuarioDto.setIdDatosUsuario(idDatosUsuarioNuevo);
                        datosUsuarioDto.setApellidoMat(amaterno);
                        datosUsuarioDto.setApellidoPat(apaterno);
                        datosUsuarioDto.setNombre(nombre);
                        datosUsuarioDto.setDireccion(direccion);
                        datosUsuarioDto.setLada(lada);
                        datosUsuarioDto.setTelefono(telefono);
                        datosUsuarioDto.setCelular(celular);
                        datosUsuarioDto.setCorreo(email);

                        /**
                        * Creamos el registro de Usuario
                        */
                        Usuarios usuariosDto = new Usuarios();
                        UsuariosDaoImpl usuariosDaoImpl = new UsuariosDaoImpl(user.getConn());

                        int idUsuariosNuevo;

                            Usuarios ultimoRegistroUsuarios = usuariosDaoImpl.findLast();
                            idUsuariosNuevo = ultimoRegistroUsuarios.getIdUsuarios() + 1;

                            usuariosDto.setIdUsuarios(idUsuariosNuevo);
                            usuariosDto.setIdDatosUsuario(idDatosUsuarioNuevo);
                            usuariosDto.setIdEmpresa(idSucursal);
                            usuariosDto.setIdEstatus(estatus);
                            usuariosDto.setIdRoles(idRol);
                            usuariosDto.setUserName(usuario);
                            try{
                                usuariosDto.setFechaAlta(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                            }catch(Exception e){
                                usuariosDto.setFechaAlta(new Date());
                            }

                            Calendar fechaVigenciaCal = Calendar.getInstance(); 
                            fechaVigenciaCal.setTime(new Date()); 
                            fechaVigenciaCal.add(Calendar.YEAR, 1 ); 
                            Date fechaVigencia = fechaVigenciaCal.getTime();

                            usuariosDto.setFechaVigencia(fechaVigencia);

                        /**
                        * Creamos el registro de login
                        */
                        Ldap ldapDto = new Ldap();
                        LdapDaoImpl ldapDaoImpl = new LdapDaoImpl(user.getConn());

                        int idLDapNuevo;

                        Encrypter datoEnc =  new Encrypter();
                        datoEnc.setMd5(true);////ESTA PARTE DE CODIGO ES PARA GENERAR EL md5
                        String passEncriptado = datoEnc.encodeString2(password);

                        Ldap ultimoRegistroLdap = ldapDaoImpl.findLast();
                        idLDapNuevo = ultimoRegistroLdap.getIdLdap() + 1;

                        ldapDto.setIdLdap(idLDapNuevo);
                        ldapDto.setUsuario(usuario);
                        ldapDto.setPassword(passEncriptado);
                        ldapDto.setContrasenas("a,b,c,");
                        ldapDto.setFirmado(0);
                        ldapDto.setEmail(email);

                        /*
                         * Creamos el registro de datos vendedor   
                        */
                        SgfensVendedorDatos datosVendedorDto = new SgfensVendedorDatos();
                        SgfensVendedorDatosDaoImpl datosVendedorDao = new SgfensVendedorDatosDaoImpl(user.getConn());

                        datosVendedorDto.setIdUsuario(usuariosDto.getIdUsuarios());
                        datosVendedorDto.setPorcentajeComisiones(porcentajeComisiones);
                        datosVendedorDto.setSueldoMensual(sueldoMensual);

                        /**
                         * Realizamos el insert en orden de los objetos creados
                         */
                        datosUsuarioDaoImpl.insert(datosUsuarioDto);
                        usuariosDaoImpl.insert(usuariosDto);
                        ldapDaoImpl.insert(ldapDto);
                        datosVendedorDao.insert(datosVendedorDto);


                        out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>"
                                + "El usuario es: <b>" + usuario
                                + " </b> y el password es: <b>" + password
                                + " </b>.");


                        //Validamos si es clienete de EVC
                        EmpresaPermisoAplicacion empresaPermisoDto = null;
                        try{
                            EmpresaPermisoAplicacionDaoImpl  empresaPermisoDao = new EmpresaPermisoAplicacionDaoImpl();
                            empresaPermisoDto = empresaPermisoDao.findWhereIdEmpresaEquals(idEmpresa)[0];
                        }catch(Exception e){e.printStackTrace();}

                        try{
                            TspMailBO mail = new TspMailBO();
                            if(empresaPermisoDto.getAccesoSgfensPretomovil()==1){
                               mail.setConfigurationMovilpyme(); 
                               mail.addMessageMovilpyme(""
                                    + "<h2>BIENVENIDO</h2><br/>"
                                    + "Los datos de acceso al Sistema del nuevo Usuario son: <br/>"
                                    + "Usuario: <b> "+usuario+"</b><br/>"
                                    + "Password: <b> "+ Matcher.quoteReplacement(password) +"</b><br/>"
                                    + "Rol: <b>" + RolesBO.getRolName(idRol) + "</b><br/>",1);

                            } else{
                                mail.setConfiguration();
                                mail.addMessage(""
                                    + "<h2>BIENVENIDO</h2><br/>"
                                    + "Los datos de acceso al Sistema del nuevo Usuario son: <br/>"
                                    + "Usuario: <b> "+usuario+"</b><br/>"
                                    + "Password: <b> "+ Matcher.quoteReplacement(password) +"</b><br/>"
                                    + "Rol: <b>" + RolesBO.getRolName(idRol) + "</b><br/>",1);
                            }

                            try {
                                mail.addTo(email, email);
                            }catch(Exception e){}
                            mail.setFrom(mail.getUSER(), mail.getFROM_NAME());                        
                            mail.send("Datos de Acceso Pretoriano Soft.");

                            out.print("<br/>Un correo de notificaci&oacute;n fue enviado al usuario.");
                       }catch(Exception ex){
                           out.print("<br/>No se pudo enviar el correo de notificaci&oacute;n. "
                                       + "<br/><b>Copie la informacion y notifiquela al usuario</b>"
                                       +"</br>" + ex.toString()); 
                           ex.printStackTrace();
                       }

                        //Consumo de Creditos Operacion
                        try{
                            BitacoraCreditosOperacionBO bcoBO = new BitacoraCreditosOperacionBO(user.getConn());
                            bcoBO.registraDescuento(user.getUser(), 
                                    BitacoraCreditosOperacionBO.CONSUMO_ACCION_REGISTRO_USUARIO_CONSOLA, 
                                    null, 0, 0, 0, 
                                    "Registro de Usuario Consola", null, true);
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }


                    }catch(Exception e){
                        e.printStackTrace();
                        msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
                    }
                
                }else{                             
                    out.print("<!--ERROR-->No se permite crear mas usuarios. El límite de tu empresa Matriz son: " + empresaPermisoAplicacionDto.getMaxNumUsuarios() + " usuarios.");
                }
            /*}else{
                    out.print("<!--ERROR-->No se permite crear mas usuarios. Ya no tiene licencias disponibles.");
            }*/
        }
        
    }else{
        out.print("<!--ERROR-->"+msgError);
    }

%>