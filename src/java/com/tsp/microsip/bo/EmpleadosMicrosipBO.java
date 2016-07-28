/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.microsip.bo;

import com.tsp.microsip.bean.ControlBean;
import java.util.List;

import com.tsp.microsip.bean.EmpleadosMicrosipBean;
import com.tsp.sct.bo.EmpleadoBO;
import com.tsp.sct.bo.EmpresaBO;
import com.tsp.sct.bo.UsuariosBO;
import com.tsp.sct.dao.dto.DatosUsuario;
import com.tsp.sct.dao.dto.DatosUsuarioPk;
import com.tsp.sct.dao.dto.Empleado;
import com.tsp.sct.dao.dto.EmpleadoPk;
import com.tsp.sct.dao.dto.EmpresaPermisoAplicacion;
import com.tsp.sct.dao.dto.Ldap;
import com.tsp.sct.dao.dto.QuartzEmpleado;
import com.tsp.sct.dao.dto.Usuarios;
import com.tsp.sct.dao.dto.UsuariosPk;
import com.tsp.sct.dao.jdbc.DatosUsuarioDaoImpl;
import com.tsp.sct.dao.jdbc.EmpleadoDaoImpl;
import com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl;
import com.tsp.sct.dao.jdbc.LdapDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzEmpleadoDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.dao.jdbc.UsuariosDaoImpl;
import com.tsp.sct.util.Encrypter;
import com.tsp.sct.util.RandomString;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class EmpleadosMicrosipBO {
    
    public int obtenerCantidadRegistros(int tipo, int idEmpresa){
        try{
            //validamos si la busqueda es para empleados nuevos o actualizados        
            String filtroBusqueda = "";
            if(tipo == 1){//empleados nuevos
                filtroBusqueda = " SINCRONIZACION_MICROSIP = 0 ";
            }else if(tipo == 2){//empleados actualizados
                filtroBusqueda = " SINCRONIZACION_MICROSIP = 2 ";
            }

            if(idEmpresa > 0){
                filtroBusqueda += " AND ID_EMPRESA = " +idEmpresa;
            }
            
            filtroBusqueda += " AND ID_ESTATUS = 1 AND ID_EMPLEADO > 0 ";
            
            filtroBusqueda += " AND ( ID_USUARIOS IN (SELECT ID_USUARIOS FROM USUARIOS WHERE (ID_ROLES = 16 OR ID_ROLES = 25 OR ID_ROLES = 31 OR ID_ROLES = 32) ) ) ";
            
            Connection conn = ResourceManager.getConnection();
            EmpleadoBO empBO = new EmpleadoBO(conn);
            int cantidad = empBO.getCantidadByEmpleado(filtroBusqueda);
            conn.close();
            return cantidad;    
        }catch(Exception e){
            e.printStackTrace();
            return 0;            
        }
    }
    
    public List<EmpleadosMicrosipBean> obtenerEmpleados(int tipo, int idEmpresa){
        try{
            //validamos si la busqueda es para clientes nuevo o actualizados        
            String filtroBusqueda = " AND ";
            if(tipo == 1){//clientes nuevos
                filtroBusqueda += " SINCRONIZACION_MICROSIP = 0 ";
            }else if(tipo == 2){//clientes actualizados
                filtroBusqueda += " SINCRONIZACION_MICROSIP = 2 ";
            }
            
            filtroBusqueda += " AND ID_EMPRESA = " +idEmpresa;
                
            filtroBusqueda += " AND ( ID_USUARIOS IN (SELECT ID_USUARIOS FROM USUARIOS WHERE (ID_ROLES = 16 OR ID_ROLES = 25 OR ID_ROLES = 31 OR ID_ROLES = 32) ) ) ";
            
            Connection conn = ResourceManager.getConnection();
            EmpleadoBO empBO = new EmpleadoBO(conn);
            Empleado[] empleado = empBO.findEmpleados(0, idEmpresa, 0, 0, filtroBusqueda);

            List<EmpleadosMicrosipBean> empleadosCompartidos = new ArrayList<EmpleadosMicrosipBean>();

            for(Empleado emp : empleado ){
                                
                //--/-/-/
                com.tsp.microsip.bean.Empleado empleadoMicro = new com.tsp.microsip.bean.Empleado();
                empleadoMicro.setIdEmpleado(emp.getIdEmpleado());
                empleadoMicro.setIdEmpresa(emp.getIdEmpresa());
                empleadoMicro.setIdEstatus(emp.getIdEstatus());
                empleadoMicro.setNombre(emp.getNombre());
                empleadoMicro.setApellidoPaterno(emp.getApellidoPaterno());
                empleadoMicro.setApellidoMaterno(emp.getApellidoMaterno());
                empleadoMicro.setTelefonoLocal(emp.getTelefonoLocal());
                empleadoMicro.setNumEmpleado(emp.getNumEmpleado());
                empleadoMicro.setCorreoElectronico(emp.getCorreoElectronico());
                empleadoMicro.setIdSucursal(emp.getIdSucursal());
                empleadoMicro.setIdDispositivo(emp.getIdDispositivo());
                empleadoMicro.setLatitud(emp.getLatitud());
                empleadoMicro.setLongitud(emp.getLongitud());                
                empleadoMicro.setIdMovilEmpleadoRol(emp.getIdMovilEmpleadoRol());
                empleadoMicro.setIdUsuarios(emp.getIdUsuarios());
                empleadoMicro.setPassword(emp.getPassword());
                empleadoMicro.setIdEstado(emp.getIdEstado());
                empleadoMicro.setFechaHora(emp.getFechaHora());
                empleadoMicro.setIdGeocerca(emp.getIdGeocerca());
                empleadoMicro.setRepartidor(emp.getRepartidor());
                empleadoMicro.setIdRegion(emp.getIdRegion());
                empleadoMicro.setSueldo(emp.getSueldo());
                empleadoMicro.setPorcentajeComision(emp.getPorcentajeComision());
                empleadoMicro.setIdPeriodoPago(emp.getIdPeriodoPago());
                empleadoMicro.setPermisoVentaRapida(emp.getPermisoVentaRapida());
                empleadoMicro.setSincronizacionMicrosip(emp.getSincronizacionMicrosip());
                empleadoMicro.setVentaConsigna(emp.getVentaConsigna());
                //--/-/-/
                
                EmpleadosMicrosipBean empMiBO = new EmpleadosMicrosipBean();
                empMiBO.setEmpleado(empleadoMicro);
                if(tipo == 2){//si es una actualización, enviamos el id o clave del sistema de microsip (identificador)
                    QuartzEmpleado qi = null;
                    try{qi = new QuartzEmpleadoDaoImpl(conn).findByDynamicWhere(" ID_EMPLEADO_EVC = " + emp.getIdEmpleado()  + " AND ID_EMPRESA = " + idEmpresa+ " ORDER BY ID_QUARTZ DESC ", null)[0]; //buscamos el registro correspondiente
                        if(qi != null){
                            if(qi.getIdEmpleadoSistemTercero() > 0){
                                empMiBO.setIdEmpleadosMicrosip(qi.getIdEmpleadoSistemTercero());
                            }else if(qi.getClave() != null && !qi.getClave().trim().equals("")){
                                empMiBO.setClave(qi.getClave());
                            }
                        }
                    }catch(Exception e){/*e.printStackTrace();*/}                
                }            
                empleadosCompartidos.add(empMiBO);
            }        
            conn.close();
            return empleadosCompartidos; 
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public int setEmpleadosIdentificadores(List<QuartzEmpleado> quartzEmpleados, int idEmpresa){
        try{
            Connection conn = ResourceManager.getConnection();
            EmpleadoDaoImpl empleadoDaoImpl = new EmpleadoDaoImpl(conn);
            List<QuartzEmpleado> empleadosIdSincronizacion = new ArrayList<QuartzEmpleado>();
            QuartzEmpleadoDaoImpl quartzEmpleadoDaoImpl = new QuartzEmpleadoDaoImpl(conn);
            for(QuartzEmpleado quaI : quartzEmpleados){
                //Vemos si ya existe el registro:
                QuartzEmpleado quartzEmpleado = null;
                if(quaI.getIdEmpleadoSistemTercero() > 0){ //verificamos si se buscara por id o por clave
                    try{quartzEmpleado = quartzEmpleadoDaoImpl.findByDynamicWhere( " ID_EMPLEADO_EVC = " + quaI.getIdEmpleadoEvc() + " AND ID_EMPLEADO_SISTEM_TERCERO = " + quaI.getIdEmpleadoSistemTercero() + " AND ID_EMPRESA = " + idEmpresa , null)[0];}catch(Exception e){/*e.printStackTrace();*/}
                }else if(quaI.getClave() != null && !quaI.getClave().equals("")){
                    try{quartzEmpleado = quartzEmpleadoDaoImpl.findByDynamicWhere( " ID_EMPLEADO_EVC = " + quaI.getIdEmpleadoEvc() + " AND CLAVE = " + quaI.getClave() + " AND ID_EMPRESA = " + idEmpresa , null)[0];}catch(Exception e){/*e.printStackTrace();*/}
                }

                if(quartzEmpleado == null){//si no existe lo insertamos
                    quartzEmpleado = new QuartzEmpleado();
                    quartzEmpleado.setIdEmpleadoEvc(quaI.getIdEmpleadoEvc());
                    if(quaI.getIdEmpleadoSistemTercero() > 0){
                        quartzEmpleado.setIdEmpleadoSistemTercero(quaI.getIdEmpleadoSistemTercero());
                    }else{
                        quartzEmpleado.setIdEmpleadoSistemTercero(0);
                    }
                    if(quaI.getClave() != null && !quaI.getClave().equals("")){
                        quartzEmpleado.setClave(quaI.getClave());
                    }else{
                        quartzEmpleado.setClave("");
                    }
                    quartzEmpleado.setIdEmpresa(idEmpresa);
                    empleadosIdSincronizacion.add(quartzEmpleado);
                }else{//si existe, borramos e insertamos el registro para que sea el ultimo registro insertado y así al recuperar los ID sea el mas actual.
                    try{quartzEmpleadoDaoImpl.delete(quartzEmpleado.createPk());
                    QuartzEmpleado quartzEmpleadoClon = new QuartzEmpleado();
                    quartzEmpleadoClon.setIdEmpleadoEvc(quartzEmpleado.getIdEmpleadoEvc());
                    quartzEmpleadoClon.setIdEmpleadoSistemTercero(quartzEmpleado.getIdEmpleadoSistemTercero());
                    quartzEmpleadoClon.setClave(quartzEmpleado.getClave());
                    quartzEmpleadoClon.setIdSistemaTercero(quartzEmpleado.getIdSistemaTercero());
                    quartzEmpleadoClon.setIdEmpresa(quartzEmpleado.getIdEmpresa());
                    quartzEmpleadoDaoImpl.insert(quartzEmpleadoClon);
                    }catch(Exception e){System.out.println("------------ Error al actualizar Identificadores de quartz de empleado: "+e.getMessage());}
                }
                
                //actualizamos la bandera de empleados a sincronizado:
                try{
                    Empleado empleado = new EmpleadoBO(conn).findEmpleadobyId(quaI.getIdEmpleadoEvc());
                    empleado.setSincronizacionMicrosip(1);
                    empleadoDaoImpl.update(empleado.createPk(), empleado);
                }catch(Exception e){e.printStackTrace();}
                
            }
            int insetarActualizacion = 0;
            try{
                QuartzEmpleadoDaoImpl qiDaoImp = new QuartzEmpleadoDaoImpl(conn);
                for(QuartzEmpleado insetImp : empleadosIdSincronizacion){//insertamos los registros:
                    qiDaoImp.insert(insetImp);
                }
                insetarActualizacion = 1;
            }catch(Exception e){e.printStackTrace();}
            
            conn.close();
            return insetarActualizacion;    
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }
    
    public ControlBean setEmpleadoMicrosip(List<EmpleadosMicrosipBean> empleadosMicrosipBean, int idEmpresa){
        ControlBean controlBean = new ControlBean();
        String mensajeEmpleado = "";
        Connection conn = null;
        try{            
            conn = ResourceManager.getConnection();
            EmpleadoDaoImpl empleadosDaoImpl = new EmpleadoDaoImpl(conn);
            QuartzEmpleadoDaoImpl empleadoDaoImpl = new QuartzEmpleadoDaoImpl(conn);
            QuartzEmpleado empleadoExistente = null;
            
            int contadorRegistrosNuevos = 0; //variable auxiliar para el numero de registros insertados
            int contadorRegistrosActualizados = 0; //variable auxiliar para el numero de registros insertados
            
            ///****Para validación del numero de empleados:
            EmpresaBO empresaBO = new EmpresaBO(conn);
            EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(conn).findByPrimaryKey(empresaBO.getEmpresaMatriz(idEmpresa).getIdEmpresa());
            UsuariosBO usuariosBO = new UsuariosBO();
            Usuarios[] listaUsuarios = new Usuarios[0];
            listaUsuarios = usuariosBO.findUsuarios(-1, empresaBO.getEmpresaMatriz(idEmpresa).getIdEmpresa(), 0, 0, " AND ID_ESTATUS <> 2 ");
                
            int numeroLicenciasDisponibles = empresaPermisoAplicacionDto.getMaxNumUsuarios() - listaUsuarios.length;
               
            ///****
            
            for(EmpleadosMicrosipBean empleadoMicrosip : empleadosMicrosipBean){
                //validamos si el empleado existe o no en el evc
                empleadoExistente = null;
                try{
                    //empleadoExistente = empleadoDaoImpl.findByDynamicWhere("ID_EMPLEADO_SISTEM_TERCERO = " + empleadoMicrosip.getIdEmpleadosMicrosip() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                    String queryOpcional = "";
                    if (empleadoMicrosip.getIdEmpleadosMicrosip()>0){
                        queryOpcional = " ID_EMPLEADO_SISTEM_TERCERO = " + empleadoMicrosip.getIdEmpleadosMicrosip();
                    }else{
                        if (StringManage.getValidString(empleadoMicrosip.getClave()).length()>0)
                            queryOpcional = " CLAVE='" + StringManage.getValidString(empleadoMicrosip.getClave()) + "'";
                    }
                    empleadoExistente = empleadoDaoImpl.findByDynamicWhere(queryOpcional +" AND ID_EMPRESA = " + idEmpresa, null)[0];
                }catch(Exception e1){empleadoExistente = null;}
                if(empleadoExistente == null && numeroLicenciasDisponibles > 0){//empleado nuevo
                    try{
                        com.tsp.microsip.bean.Empleado empleadoMicroSip = empleadoMicrosip.getEmpleado();
                        //Empleado ultimoRegistroEmpleados = empleadosDaoImpl.findLast();
                        //int idEmpleadoNuevo = ultimoRegistroEmpleados.getIdEmpleado() + 1;            
                        //empleadoMicroSip.setIdEmpleado(idEmpleadoNuevo);
                        
                        empleadoMicroSip.setIdEstatus(1);
                        empleadoMicroSip.setSincronizacionMicrosip(1);
                        
                        //--/-/-/
                        Empleado empleadoEvc = new Empleado();
                        //if(empleadoMicroSip.getIdEmpleado() > 0)
                        //    empleadoEvc.setIdEmpleado(empleadoMicroSip.getIdEmpleado());                        
                        //if(empleadoMicroSip.getIdEmpresa() > 0)
                        empleadoEvc.setIdEmpresa(idEmpresa);   
                        empleadoEvc.setIdSucursal(idEmpresa);
                        empleadoEvc.setIdEstatus(empleadoMicroSip.getIdEstatus());
                        empleadoEvc.setIdDispositivo(-1);
                        empleadoEvc.setNombre(StringManage.getValidString(empleadoMicroSip.getNombre()));
                        if(empleadoMicroSip.getApellidoPaterno() != null)
                            empleadoEvc.setApellidoPaterno(empleadoMicroSip.getApellidoPaterno());
                        else
                            empleadoEvc.setApellidoPaterno("");
                        if(empleadoMicroSip.getApellidoMaterno() != null)
                            empleadoEvc.setApellidoMaterno(empleadoMicroSip.getApellidoMaterno());
                        else
                            empleadoEvc.setApellidoMaterno("");
                        empleadoEvc.setTelefonoLocal(empleadoMicroSip.getTelefonoLocal());
                        empleadoEvc.setNumEmpleado(StringManage.getValidString(empleadoMicroSip.getNumEmpleado()));
                        empleadoEvc.setCorreoElectronico(empleadoMicroSip.getCorreoElectronico());
                        //empleadoEvc.setIdSucursal(idEmpresa);
                        empleadoEvc.setIdMovilEmpleadoRol(31);//vendedor_conductor
                       
                        empleadoEvc.setSueldo(empleadoMicroSip.getSueldo());
                        empleadoEvc.setPorcentajeComision(empleadoMicroSip.getPorcentajeComision());
                        empleadoEvc.setSincronizacionMicrosip(empleadoMicroSip.getSincronizacionMicrosip());
                        
                        //////////////////////////////DATOS PARA LA TABLA DATOS_USUARIO
                        DatosUsuario datosUsuario = new DatosUsuario();
                        DatosUsuarioDaoImpl datosUsuarioDaoImpl = new DatosUsuarioDaoImpl(conn); 

                        DatosUsuario ultimoRegistroDatosUsuarios = datosUsuarioDaoImpl.findLast();
                        int idDatosUsuarioNuevo = ultimoRegistroDatosUsuarios.getIdDatosUsuario() + 1;

                        datosUsuario.setIdDatosUsuario(idDatosUsuarioNuevo);
                        datosUsuario.setNombre(empleadoEvc.getNombre());
                        datosUsuario.setApellidoPat(empleadoEvc.getApellidoPaterno());
                        datosUsuario.setApellidoMat(empleadoEvc.getApellidoMaterno());
                        datosUsuario.setDireccion("");
                        datosUsuario.setTelefono(empleadoEvc.getTelefonoLocal());

                        DatosUsuarioPk datosUsuarioPk = datosUsuarioDaoImpl.insert(datosUsuario);//INSERTAMOS EL REGISTRO DE DATOS USUARIO Y RECUPERAMOS EL OBJETO PARA TENER EL ID ASIGNADO               

                        //DATOS PARA LA TABLA USUARIO
                        Usuarios usuario = new Usuarios();
                        UsuariosDaoImpl usuariosDaoImpl = new UsuariosDaoImpl(conn);

                        Usuarios ultimoRegistroUsuarios = usuariosDaoImpl.findLast();
                        int idUsuariosNuevo = ultimoRegistroUsuarios.getIdUsuarios() + 1;

                        usuario.setIdUsuarios(idUsuariosNuevo);                        
                        usuario.setIdEmpresa(idEmpresa);
                        usuario.setIdDatosUsuario(datosUsuarioPk.getIdDatosUsuario());
                        usuario.setIdEstatus(1);
                        //usuario.setTspRoleIdRole(idRolEmpleado);
                        usuario.setIdRoles(31);//Rol de Vendedor Conductor
                        
                        String userName = "";
                        RandomString ram = new RandomString();
                        userName = empleadoEvc.getNombre().trim()+"_"+ram.randomString(4);

                        String password = ram.randomString(4);
                        password = password.trim();
                        Encrypter datoEnc =  new Encrypter();
                        datoEnc.setMd5(true);////ESTA PARTE DE CODIGO ES PARA GENERAR EL md5
                        String passEncriptado = datoEnc.encodeString2(password);
                        
                        usuario.setUserName(userName);
                        usuario.setFechaAlta(new Date());

                        UsuariosPk usuarioPk = usuariosDaoImpl.insert(usuario);//INSERTAMOS EL REGISTRO DE USAURIO Y RECUPERAMOS EL ID GENERADO DE USUARIO
                        
                        //DATOS PARA LA TABLA LDAP
                        Ldap ldap = new Ldap();
                        LdapDaoImpl ldapDaoImpl = new LdapDaoImpl(conn);
                        //ldap.setIdUsuarios(usuarioPk.getIdUsuarios());
                        ldap.setUsuario(userName);

                        Encrypter encriptacion = new Encrypter();//ENCRIPTAMOS EL PASS
                        encriptacion.setMd5(true);
                        ldap.setPassword(passEncriptado);
                        ldap.setEmail(empleadoEvc.getCorreoElectronico());
                        ldap.setFirmado(1);
                        //ldap.setRecordatorioContrasena(recordatorioEmpleado);                
                        ldapDaoImpl.insert(ldap);
                        
                        //////////////////////////////
                        
                        empleadoEvc.setIdUsuarios(usuarioPk.getIdUsuarios());
                        empleadoEvc.setUsuario(userName);
                        empleadoEvc.setPassword(passEncriptado);
                        
                        //--/-/-/ 
                        
                        EmpleadoPk empleadoPk = empleadosDaoImpl.insert(empleadoEvc);
                        
                        numeroLicenciasDisponibles--;

                        ////insertamos la relacion con los Id's
                        QuartzEmpleado qc = new QuartzEmpleado();
                        qc.setClave( (empleadoMicrosip.getClave()!=null?empleadoMicrosip.getClave():null) );
                        qc.setIdEmpleadoEvc(empleadoPk.getIdEmpleado());
                        qc.setIdEmpleadoSistemTercero(empleadoMicrosip.getIdEmpleadosMicrosip());
                        qc.setIdSistemaTercero(1);
                        qc.setIdEmpresa(idEmpresa);
                        empleadoDaoImpl.insert(qc);
                        contadorRegistrosNuevos++;
                    }catch(Exception e){
                        mensajeEmpleado += " Error al insertar empleado con ID: "+empleadoMicrosip.getIdEmpleadosMicrosip() + "; ERROR: " + e.getMessage() + " , ";
                    }  
                }else if(empleadoExistente != null){//actualizar
                    try{
                        com.tsp.microsip.bean.Empleado empleadoMicroSip = empleadoMicrosip.getEmpleado();                     
                        empleadoMicroSip.setIdEmpleado(empleadoExistente.getIdEmpleadoEvc());
                        empleadoMicroSip.setSincronizacionMicrosip(1);
                        empleadoMicroSip.setIdEstatus(1);
                        //--/-/-/
                        Empleado empleadoEvc = new Empleado();
                        if(empleadoMicroSip.getIdEmpleado() > 0)
                            empleadoEvc.setIdEmpleado(empleadoMicroSip.getIdEmpleado());                        
                        empleadoEvc.setIdEmpresa(idEmpresa);   
                        empleadoEvc.setIdEstatus(empleadoMicroSip.getIdEstatus());
                        
                        if(empleadoMicroSip.getNombre() != null)
                            empleadoEvc.setNombre(empleadoMicroSip.getNombre());
                        else
                            empleadoEvc.setNombre("");
                        if(empleadoMicroSip.getApellidoPaterno() != null)
                            empleadoEvc.setApellidoPaterno(empleadoMicroSip.getApellidoPaterno());
                        else
                            empleadoEvc.setApellidoPaterno("");
                        if(empleadoMicroSip.getApellidoMaterno() != null)
                            empleadoEvc.setApellidoMaterno(empleadoMicroSip.getApellidoMaterno());
                        else
                            empleadoEvc.setApellidoMaterno("");
                        empleadoEvc.setTelefonoLocal(empleadoMicroSip.getTelefonoLocal());
                        empleadoEvc.setNumEmpleado(empleadoMicroSip.getNumEmpleado());
                        empleadoEvc.setCorreoElectronico(empleadoMicroSip.getCorreoElectronico());
                        //empleadoEvc.setIdSucursal(idEmpresa);
                        empleadoEvc.setIdMovilEmpleadoRol(31);//vendedor_conductor
                        //empleadoEvc.setIdUsuarios(exxx);
                        //empleadoEvc.setUsuario(usuario);
                        //empleadoEvc.setPassword(pass);
                        empleadoEvc.setSueldo(empleadoMicroSip.getSueldo());
                        empleadoEvc.setPorcentajeComision(empleadoMicroSip.getPorcentajeComision());
                        empleadoEvc.setNombre(empleadoMicroSip.getNombre());
                        empleadoEvc.setSincronizacionMicrosip(empleadoMicroSip.getSincronizacionMicrosip());
                        //--/-/-/ 
                        empleadosDaoImpl.update(empleadoEvc.createPk(), empleadoEvc);
                        contadorRegistrosActualizados++;
                        
                        ///////actuallizamos los datos de usuario:
                        Usuarios usuario = null;
                        try{
                            Empleado emp = new EmpleadoBO(empleadoExistente.getIdEmpleadoEvc(), conn).getEmpleado();
                            usuario = new UsuariosDaoImpl(conn).findByPrimaryKey(emp.getIdUsuarios());
                            try{
                                DatosUsuario datUser = new DatosUsuarioDaoImpl(conn).findByPrimaryKey(usuario.getIdDatosUsuario());
                                datUser.setNombre(empleadoMicroSip.getNombre());
                                datUser.setApellidoPat(empleadoMicroSip.getApellidoPaterno());
                                datUser.setApellidoMat(empleadoMicroSip.getApellidoMaterno());
                                new DatosUsuarioDaoImpl(conn).update(datUser.createPk(), datUser);
                            }catch(Exception e1){};
                            try{
                                Ldap ldap = new LdapDaoImpl(conn).findWhereUsuarioEquals(usuario.getUserName())[0];
                                ldap.setEmail(empleadoMicroSip.getCorreoElectronico());
                                new LdapDaoImpl(conn).update(ldap.createPk(), ldap);
                            }catch(Exception e1){};
                        }catch(Exception e){}
                        ///////
                    }catch(Exception e2){
                         mensajeEmpleado += " Error al actualizar empleado con ID: "+empleadoMicrosip.getIdEmpleadosMicrosip() + "; ERROR: " + e2.getMessage() + " , ";
                    }
        
                }else{
                    mensajeEmpleado += " Ya no es posible agregar mas empleados, por el número de licencias limitadas.";
                }
            }
            
            if(mensajeEmpleado.trim().equals("")){
                controlBean.setExito(true);
            }
            controlBean.setMensajeError(mensajeEmpleado);
            controlBean.setRegistradosNuevos(contadorRegistrosNuevos);            
            controlBean.setRegistradosActualizados(contadorRegistrosActualizados);
            
        }catch(Exception e){
            controlBean.setExito(false);
            controlBean.setMensajeError(e.getMessage());
        }finally{
            try {
                conn.close();
            } catch (Exception ex) {}
        }
        
        return controlBean;
    }
    
}
