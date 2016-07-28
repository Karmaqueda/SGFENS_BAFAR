/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Almacen;
import com.tsp.sct.dao.dto.Concepto;
import com.tsp.sct.dao.dto.EmpAsignacionInventario;
import com.tsp.sct.dao.dto.EmpAsignacionInventarioDetalle;
import com.tsp.sct.dao.dto.Empleado;
import com.tsp.sct.dao.dto.EmpleadoInventarioRepartidor;
import com.tsp.sct.dao.dto.EmpresaPermisoAplicacion;
import com.tsp.sct.dao.dto.ExistenciaAlmacen;
import com.tsp.sct.dao.dto.Movimiento;
import com.tsp.sct.dao.dto.Usuarios;
import com.tsp.sct.dao.jdbc.EmpAsignacionInventarioDaoImpl;
import com.tsp.sct.dao.jdbc.EmpAsignacionInventarioDetalleDaoImpl;
import com.tsp.sct.dao.jdbc.EmpleadoInventarioRepartidorDaoImpl;
import com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl;
import com.tsp.sct.dao.jdbc.MovimientoDaoImpl;
import com.tsp.sct.mail.TspMailBO;
import com.tsp.sct.util.GenericMethods;
import com.tsp.sct.util.GenericValidator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author ISCesar
 */
public class EmpAsignacionInventarioBO {
    private EmpAsignacionInventario empAsignacionInventario = null;

    public EmpAsignacionInventario getEmpAsignacionInventario() {
        return empAsignacionInventario;
    }

    public void setEmpAsignacionInventario(EmpAsignacionInventario empAsignacionInventario) {
        this.empAsignacionInventario = empAsignacionInventario;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public EmpAsignacionInventarioBO(Connection conn){
        this.conn = conn;
    }
    
    public EmpAsignacionInventarioBO(int idEmpAsignacionInventario, Connection conn){        
        this.conn = conn;
        try{
            EmpAsignacionInventarioDaoImpl empAsignacionInventarioDaoImpl = new EmpAsignacionInventarioDaoImpl(this.conn);
            this.empAsignacionInventario = empAsignacionInventarioDaoImpl.findByPrimaryKey(idEmpAsignacionInventario);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public EmpAsignacionInventario findEmpAsignacionInventariobyId(int idEmpAsignacionInventario) throws Exception{
        EmpAsignacionInventario empAsignacionInventario = null;
        
        try{
            EmpAsignacionInventarioDaoImpl pedidoEmpAsignacionInventarioDao = new EmpAsignacionInventarioDaoImpl(this.conn);
            empAsignacionInventario = pedidoEmpAsignacionInventarioDao.findByPrimaryKey(idEmpAsignacionInventario);
            if(empAsignacionInventario == null){
                throw new Exception("No se encontro ninguna empAsignacionInventario que corresponda según los parámetros específicados.");
            }
            if(empAsignacionInventario.getIdAsignacionInventario()<= 0){
                throw new Exception("No se encontro ninguna empAsignacionInventario que corresponda según los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de EmpAsignacionInventario del usuario. Error: " + e.getMessage());
        }
        
        return empAsignacionInventario;
    }
    
    /**
     * Realiza una búsqueda por ID EmpAsignacionInventario en busca de
     * coincidencias
     * @param idEmpAsignacionInventario ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar empAsignacionInventarios, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO EmpAsignacionInventario
     */
    public EmpAsignacionInventario[] findEmpAsignacionInventarios(int idEmpAsignacionInventario, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        EmpAsignacionInventario[] empAsignacionInventarioDto = new EmpAsignacionInventario[0];
        EmpAsignacionInventarioDaoImpl empAsignacionInventarioDao = new EmpAsignacionInventarioDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idEmpAsignacionInventario>0){
                sqlFiltro ="id_asignacion_inventario=" + idEmpAsignacionInventario + " AND ";
            }else{
                sqlFiltro ="id_asignacion_inventario>0 AND";
            }
            if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            }else{
                sqlFiltro +=" ID_EMPRESA>0";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            empAsignacionInventarioDto = empAsignacionInventarioDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY id_asignacion_inventario DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return empAsignacionInventarioDto;
    }
    
    /**
     * Realiza una búsqueda por ID EmpAsignacionInventario en busca de
     * coincidencias
     * @param idEmpAsignacionInventario ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar empAsignacionInventarios, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO EmpAsignacionInventario
     */
    public int findCantidadEmpAsignacionInventarios(int idEmpAsignacionInventario, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        try {
            String sqlFiltro="";
            if (idEmpAsignacionInventario>0){
                sqlFiltro ="id_asignacion_inventario=" + idEmpAsignacionInventario + " AND ";
            }else{
                sqlFiltro ="id_asignacion_inventario>0 AND";
            }
            if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            }else{
                sqlFiltro +=" ID_EMPRESA>0";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_asignacion_inventario) as cantidad FROM emp_asignacion_inventario WHERE " + 
                    sqlFiltro
                    + sqlLimit);
            if(rs.next()){
                cantidad = rs.getInt("cantidad");
            }
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return cantidad;
    }
    
    public void repiteAsignacion() throws Exception {
        if (this.empAsignacionInventario==null)
            return;
        
        EmpAsignacionInventarioDetalle[] detallesAsignacion 
                = new EmpAsignacionInventarioDetalleDaoImpl(this.conn).
                        findByDynamicWhere(" id_estatus= 1 AND id_asignacion_inventario = "+this.empAsignacionInventario.getIdAsignacionInventario(), null);
        
        ExistenciaAlmacenBO existenciaAlmacenBO = new ExistenciaAlmacenBO(this.conn);
        ConceptoBO conceptoBO = new ConceptoBO(this.conn);
        String msgError = "";
        
        //validacion de existencias en almacen para SALIDA
        for (EmpAsignacionInventarioDetalle detalle : detallesAsignacion){       
            try{
                Almacen almacen = new AlmacenBO(detalle.getIdAlmacen(), this.conn).getAlmacen();
                ExistenciaAlmacen existenciaAlmacen  = existenciaAlmacenBO.getExistenciaProductoAlmacen(detalle.getIdAlmacen(), detalle.getIdConcepto());
                Concepto conceptoDto = conceptoBO.findConceptobyId(detalle.getIdConcepto());

                if(existenciaAlmacen==null){ 
                    //no hay relacion de almacen - concepto
                    msgError += "<ul>El almacen '" + almacen.getNombre() + "' no tiene existencia del producto '" + conceptoDto.getNombreDesencriptado() + "'.";
                }else if (existenciaAlmacen.getExistencia() < detalle.getCantidad()){
                    //no hay existencia suficiente en almacen para salida
                    msgError += "<ul>El almacen '" + almacen.getNombre() + "' no tiene existencia suficiente del producto '" + conceptoDto.getNombreDesencriptado() + "'. "
                            + " Existencia en almacen: " + existenciaAlmacen.getExistencia();
                }

            }catch(Exception e){                    
                msgError += "<ul>No se pudo consultar un registro. Informe del error al administrador del sistema: " + GenericMethods.exceptionStackTraceToString(e);
                e.printStackTrace();
            }            
        }
        
        //procedemos a guardar si no hubo error
        Date fechaRegistroMovimientos = new Date();
        if (msgError.equals("")){
            EmpleadoInventarioRepartidorDaoImpl empleadoInventarioRepartidorDao = new EmpleadoInventarioRepartidorDaoImpl(this.conn);
            MovimientoDaoImpl movimientosDao = new MovimientoDaoImpl(this.conn);

            for (EmpAsignacionInventarioDetalle detalle : detallesAsignacion){
                try{
                    Concepto conceptoDto = conceptoBO.findConceptobyId(detalle.getIdConcepto());
                    int idEmpleado = this.empAsignacionInventario.getIdEmpleado();
                    int idEmpresa = this.empAsignacionInventario.getIdEmpresa();
                    int idAlmacen = detalle.getIdAlmacen();

                    // 1 - Creamos o Actualizamos el registro de relacion Empleado-Concepto (inventario empleado)
                    EmpleadoInventarioRepartidor[] empleadoInventarios = empleadoInventarioRepartidorDao.findByDynamicWhere("ID_EMPLEADO = "+idEmpleado+" AND ID_CONCEPTO = "+conceptoDto.getIdConcepto(), null);
                    EmpleadoInventarioRepartidor empleadoInventario = null;
                    boolean edicion = false;
                    if (empleadoInventarios.length>0){
                        //Ya existe relacion
                        empleadoInventario = empleadoInventarios[0];
                        edicion = true;
                    }else{
                        //No existe relacion empleado-concepto, creamos nueva
                        empleadoInventario = new EmpleadoInventarioRepartidor();
                        empleadoInventario.setIdEmpleado(idEmpleado);
                        empleadoInventario.setIdConcepto(conceptoDto.getIdConcepto());
                        empleadoInventario.setTipoProductoServicio(1);//0 es servicio, 1 concepto.
                    }
                    empleadoInventario.setCantidad(empleadoInventario.getCantidad() + detalle.getCantidad());
                    empleadoInventario.setIdEstatus(1);
                    //empleadoInventario.setPeso(movSesion.getPeso());
                    empleadoInventario.setExistenciaGranel(empleadoInventario.getCantidad() * empleadoInventario.getPeso());

                    if (!edicion){
                        empleadoInventarioRepartidorDao.insert(empleadoInventario);
                    }else{
                        empleadoInventarioRepartidorDao.update(empleadoInventario.createPk(), empleadoInventario);
                    }

                    // 2 - Creamos movimiento de almacen
                    Movimiento movimientoDto = new Movimiento(); 
                    movimientoDto.setIdEmpresa(idEmpresa);
                    movimientoDto.setTipoMovimiento("SALIDA");
                    movimientoDto.setNombreProducto(conceptoDto.getNombreDesencriptado());
                    movimientoDto.setContabilidad(detalle.getCantidad());
                    movimientoDto.setIdProveedor(-1);
                    movimientoDto.setOrdenCompra("");
                    movimientoDto.setNumeroGuia("");                             
                    movimientoDto.setIdAlmacen(idAlmacen);                
                    movimientoDto.setConceptoMovimiento("Asignación de Producto a Vendedor (Automática)");                
                    movimientoDto.setFechaRegistro(fechaRegistroMovimientos);
                    movimientoDto.setIdConcepto(conceptoDto.getIdConcepto());
                    movimientoDto.setIdEmpleado(idEmpleado);                                    
                        //Insertamos registro
                    movimientosDao.insert(movimientoDto);

                    // 3 - Actualizamos existencia en Almacen
                    ExistenciaAlmacen existenciaAlmacen  = existenciaAlmacenBO.getExistenciaProductoAlmacen(idAlmacen, detalle.getIdConcepto()); 
                    BigDecimal numArticulosDisponibles = (new BigDecimal(existenciaAlmacen!=null?existenciaAlmacen.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal contaMovimiento = (new BigDecimal(detalle.getCantidad())).setScale(2, RoundingMode.HALF_UP);
                        //restamos
                    BigDecimal nuevoStockBigD = numArticulosDisponibles.subtract(contaMovimiento);
                    double nuevoStock = nuevoStockBigD.doubleValue();
                    existenciaAlmacen.setExistencia(nuevoStock);
                    //solo hacemos update, por que debido a validacion previa, la relacion almacen-concepto SI debe existir
                    existenciaAlmacenBO.updateBD(existenciaAlmacen);

                }catch(Exception ex){
                    msgError += "<ul>Error inesperado al asignar Producto con ID '"+detalle.getIdConcepto()+"': " + GenericMethods.exceptionStackTraceToString(ex);
                    ex.printStackTrace();
                }
            }
        }
        
        //enviamos correo de notificacion
        try{
            notificaCorreoAsignacionAutomatica( msgError.equals("") , msgError, detallesAsignacion);          
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        //Por último actualizamos registro de Asignacion
        this.empAsignacionInventario.setUltimaRepFechaHr(fechaRegistroMovimientos);
        this.empAsignacionInventario.setUltimaRepExito(msgError.equals("")? 1 : 0);
        this.empAsignacionInventario.setUltimaRepObservacion(msgError);
        
        EmpAsignacionInventarioDaoImpl empAsignacionInventarioDao = new EmpAsignacionInventarioDaoImpl(this.conn);
        empAsignacionInventarioDao.update(empAsignacionInventario.createPk(), empAsignacionInventario);
        
    }
    
    private boolean notificaCorreoAsignacionAutomatica(boolean exito, String msgError, EmpAsignacionInventarioDetalle[] detallesAsignacion){
        boolean enviado;
        int idEmpresa = this.empAsignacionInventario.getIdEmpresa();
        EmpresaPermisoAplicacion empresaPermisoDto = null;
        
        try{
            EmpresaPermisoAplicacionDaoImpl  empresaPermisoDao = new EmpresaPermisoAplicacionDaoImpl();
            empresaPermisoDto = empresaPermisoDao.findWhereIdEmpresaEquals(idEmpresa)[0];
        }catch(Exception e){e.printStackTrace();}

        try {
            TspMailBO mailBO = new TspMailBO();

            if(empresaPermisoDto!=null && empresaPermisoDto.getAccesoSgfensPretomovil()==1){
                mailBO.setConfigurationMovilpyme();
            } else{
                mailBO.setConfiguration();              
            }

            GenericValidator genericValidator = new GenericValidator();
            ArrayList<String> destinatarios = new ArrayList<String>();
            ConceptoBO conceptoBO = new ConceptoBO(this.conn);
            EmpleadoBO empleadoBO = new EmpleadoBO(this.conn);
            
            Empleado empleadoDto = empleadoBO.findEmpleadobyId(this.empAsignacionInventario.getIdEmpleado());

            try{
                UsuariosBO usuariosBO = new UsuariosBO(conn);
                Usuarios[] desarrolladores = usuariosBO.findUsuariosByRol(idEmpresa, RolesBO.ROL_DESARROLLO);
                Usuarios[] administradores = usuariosBO.findUsuariosByRol(idEmpresa, RolesBO.ROL_ADMINISTRADOR);

                for (Usuarios usuario:desarrolladores){
                    UsuarioBO usuarioBO = new UsuarioBO(usuario.getIdUsuarios());
                    try{destinatarios.add(usuarioBO.getLdap().getEmail());}catch(Exception ex){}
                }
                for (Usuarios usuario:administradores){
                    UsuarioBO usuarioBO = new UsuarioBO(usuario.getIdUsuarios());
                    try{destinatarios.add(usuarioBO.getLdap().getEmail());}catch(Exception ex){}
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }

            //Agregamos todos los correos de los usuarios Administradores de la empresa correspondiente
            for (String destinatario : destinatarios){
                try{                           
                    if(genericValidator.isEmail(destinatario)){
                        try{
                            mailBO.addTo(destinatario, "Administradores");
                        }catch(Exception ex){}
                    }
                }catch(Exception ex){}
            }

            String msg = "<b>Buen día!</b><br/> ";
            
            msg += "<br/>La asignación automática de inventario a vendedor, con ID: " + this.empAsignacionInventario.getIdAsignacionInventario() + ","
                    + (exito?"":"NO")+  " ha sido ejecutada exitosamente.<br/>"
                    + "<br/> Empleado: <i>"+(empleadoDto!=null?empleadoDto.getNombre()+" "+empleadoDto.getApellidoPaterno():"ERROR: NO INGRESADO")+"</i>"
                    + "<br/> El detalle de asignación es:<br/>";
            
            //detalles de la asignación
            msg+= "<table>";
            msg+= "<tr>";
            msg+= " <td>Concepto</td><td>Cantidad</td><td>Almacen</td>";
            msg+= "</tr>";
            for (EmpAsignacionInventarioDetalle detalle : detallesAsignacion){
                Concepto conceptoDto = conceptoBO.findConceptobyId(detalle.getIdConcepto());
                Almacen almacen = new AlmacenBO(detalle.getIdAlmacen(), this.conn).getAlmacen();
                
                msg+= "<tr>";
                msg+= " <td>" + conceptoDto.getNombreDesencriptado() + "</td><td>" + detalle.getCantidad() + "</td><td>" + almacen.getNombre() + "</td>";
                msg+= "</tr>";
            }
            msg+= "</table>";
            
            if (!exito){
                msg += "<br/><br/><b>Detalle del error: </b><br/>" + msgError;
            }

            if(empresaPermisoDto!=null && empresaPermisoDto.getAccesoSgfensPretomovil()==1){
                mailBO.addMessageMovilpyme(msg ,1);
            } else{
                mailBO.addMessage(msg ,1);
             }

            mailBO.send((exito?"EXITO":"ERROR") + " en Asignacion Automatica de Inventario a Vendedor");

            enviado = true;
        } catch (Exception ex) {
            enviado = false;
            System.out.print("Ocurrio un error al intentar enviar el correo: " + ex.toString());
            ex.printStackTrace();
        }            
        
        return enviado;
    }
    
}
