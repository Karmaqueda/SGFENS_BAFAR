/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.microsip.bo;

import com.tsp.microsip.bean.ControlBean;
import com.tsp.microsip.bean.MovimientosMicrosipBean;
import com.tsp.sct.bo.ExistenciaAlmacenBO;
import com.tsp.sct.bo.MovimientoBO;
import com.tsp.sct.dao.dto.Empleado;
import com.tsp.sct.dao.dto.ExistenciaAlmacen;
import com.tsp.sct.dao.dto.Movimiento;
import com.tsp.sct.dao.dto.MovimientoPk;
import com.tsp.sct.dao.dto.QuartzAlmacen;
import com.tsp.sct.dao.dto.QuartzConcepto;
import com.tsp.sct.dao.dto.QuartzEmpleado;
import com.tsp.sct.dao.dto.QuartzMovimiento;
import com.tsp.sct.dao.jdbc.EmpleadoDaoImpl;
import com.tsp.sct.dao.jdbc.ExistenciaAlmacenDaoImpl;
import com.tsp.sct.dao.jdbc.MovimientoDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzAlmacenDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzConceptoDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzEmpleadoDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzMovimientoDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.util.StringManage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class MovimientosMicrosipBO {
    
    public int obtenerCantidadRegistros(int tipo, int idEmpresa, int tipoMovimiento){
        try{
            //validamos si la busqueda es para movimientos nuevo o actualizados        
            String filtroBusqueda = "";
            if(tipo == 1){//movimientos nuevos
                filtroBusqueda = " SINCRONIZACION_MICROSIP = 0 ";
            }else if(tipo == 2){//movimientos actualizados
                filtroBusqueda = " SINCRONIZACION_MICROSIP = 2 ";
            }

            if(idEmpresa > 0){
                filtroBusqueda += " AND ID_EMPRESA = " +idEmpresa;
            }
            
            filtroBusqueda += " AND ID_MOVIMIENTO > 0 ";
            
            if(tipoMovimiento > 0){
                if(tipoMovimiento == 1)//
                    filtroBusqueda += " AND TIPO_MOVIMIENTO = 'ENTRADA' "; 
                else if(tipoMovimiento == 2)//
                    filtroBusqueda += " AND TIPO_MOVIMIENTO = 'SALIDA' "; 
                else if(tipoMovimiento == 3)//
                    filtroBusqueda += " AND TIPO_MOVIMIENTO = 'TRASPASO' ";
            }
            
            Connection conn = ResourceManager.getConnection();
            MovimientoBO movBO = new MovimientoBO(conn);
            int cantidad = movBO.getCantidadByMovimiento(filtroBusqueda);
            conn.close();            
            return cantidad;    
        }catch(Exception e){
            e.printStackTrace();
            return 0;            
        }
    }
    
    public List<MovimientosMicrosipBean> obtenerMovimientos(int tipo, int idEmpresa, int tipoMovimiento){
        try{
            //validamos si la busqueda es para movimientos nuevo o actualizados        
            String filtroBusqueda = " AND ";
            if(tipo == 1){//movimientos nuevos
                filtroBusqueda += " SINCRONIZACION_MICROSIP = 0 ";
            }else if(tipo == 2){//movimientos actualizados
                filtroBusqueda += " SINCRONIZACION_MICROSIP = 2 ";
            }
            
            if(tipoMovimiento > 0){
                if(tipoMovimiento == 1)//
                    filtroBusqueda += " AND TIPO_MOVIMIENTO = 'ENTRADA' "; 
                else if(tipoMovimiento == 2)//
                    filtroBusqueda += " AND TIPO_MOVIMIENTO = 'SALIDA' "; 
                else if(tipoMovimiento == 3)//
                    filtroBusqueda += " AND TIPO_MOVIMIENTO = 'TRASPASO' ";
            }
            
            Connection conn = ResourceManager.getConnection();
            MovimientoBO movBO = new MovimientoBO(conn);
            Movimiento[] movimiento = movBO.findMovimientos(0, idEmpresa, 0, 0, filtroBusqueda);

            QuartzMovimientoDaoImpl quartzMovimientoDaoImpl = new QuartzMovimientoDaoImpl(conn);
                    
            List<MovimientosMicrosipBean> movimientosCompartidos = new ArrayList<MovimientosMicrosipBean>();
            QuartzAlmacenDaoImpl quartzAlmacenDaoImpl = new QuartzAlmacenDaoImpl(conn);
            QuartzConceptoDaoImpl quartzConceptoDaoImpl = new QuartzConceptoDaoImpl(conn);
            QuartzEmpleadoDaoImpl qedi = new QuartzEmpleadoDaoImpl(conn);
            
            for(Movimiento mov : movimiento ){
                //--/-/-/ACTUALIZAMOS LOS ID POR LOS CORRESPONDIENTES:
                MovimientosMicrosipBean movMiBO = new MovimientosMicrosipBean();
                try{                    
                    QuartzAlmacen qa = quartzAlmacenDaoImpl.findByDynamicWhere("ID_ALMACEN_EVC = " + mov.getIdAlmacen() + " ORDER BY ID_QUARTZ DESC ", null)[0];
                    mov.setIdAlmacen(qa.getIdAlmacenSistemTercero());
                }catch(Exception e){
                    mov.setIdAlmacen(-1);
                }      
                try{                    
                    QuartzAlmacen qa = quartzAlmacenDaoImpl.findByDynamicWhere("ID_ALMACEN_EVC = " + mov.getIdAlmacenDestino() + " ORDER BY ID_QUARTZ DESC ", null)[0];
                    mov.setIdAlmacenDestino(qa.getIdAlmacenSistemTercero());
                }catch(Exception e){
                    mov.setIdAlmacenDestino(-1);
                }
                try{                    
                    QuartzConcepto quartzConcepto = quartzConceptoDaoImpl.findByDynamicWhere("ID_CONCEPTO_EVC = " + mov.getIdConcepto() + " ORDER BY ID_QUARTZ DESC ", null)[0];
                    mov.setIdConcepto(quartzConcepto.getIdConceptoSistemTercero());
                    movMiBO.setClaveConceptoSistemaTercero(quartzConcepto.getClave());
                }catch(Exception e){
                    mov.setIdConcepto(-1);
                }
                try{
                    if (mov.getIdEmpleado()>0){
                        QuartzEmpleado quartzEmpleado = qedi.findByDynamicWhere(" ID_EMPLEADO_EVC = " + mov.getIdEmpleado() + " ORDER BY ID_QUARTZ DESC ", null)[0];
                        mov.setIdEmpleado(quartzEmpleado.getIdEmpleadoSistemTercero());
                        movMiBO.setClaveEmpleadoSistemaTercero(StringManage.getValidString(quartzEmpleado.getClave()));
                    }
                }catch(Exception e){
                    //e.printStackTrace();
                    System.out.println("El Empleado con ID Empleado en pretoriano " + mov.getIdEmpleado()+ " no ha sido sincronizado en el sistema de terceros.\n" + e.toString());
                    mov.setIdEmpleado(-1);//Asignamos por defecto -1 por si no encontramos el correspondiente del sistema tercero
                }
                //--/-/-/                
                movMiBO.setMovimiento(mov);
                if(tipo == 2){//si es una actualización, envamos el id o clave del sistema de microsip (identificador)
                    QuartzMovimiento qc = null;
                    try{qc = quartzMovimientoDaoImpl.findByDynamicWhere(" ID_MOVIMIENTO_EVC = " + mov.getIdMovimiento() + " AND ID_EMPRESA = " + idEmpresa+ " ORDER BY ID_QUARTZ DESC ", null)[0]; //buscamos el registro correspondiente
                        if(qc != null){
                            if(qc.getIdMovimientoSistemTercero() > 0){
                                movMiBO.setIdMovimientoMicrosip(qc.getIdMovimientoSistemTercero());
                            }else if(qc.getClave() != null && !qc.getClave().trim().equals("")){
                                movMiBO.setClave(qc.getClave());
                            }
                        }
                    }catch(Exception e){/*e.printStackTrace();*/
                        movMiBO.setIdMovimientoMicrosip(-1);
                    }                
                }            
                movimientosCompartidos.add(movMiBO);
            }        
            conn.close();
            return movimientosCompartidos; 
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public int setMovimientosIdentificadores(List<QuartzMovimiento> quartzMovimientos, int idEmpresa){
        try{
            Connection conn = ResourceManager.getConnection();
            MovimientoDaoImpl movimientoDaoImpl = new MovimientoDaoImpl(conn);
            List<QuartzMovimiento> movimientosIdSincronizacion = new ArrayList<QuartzMovimiento>();
            QuartzMovimientoDaoImpl quartzMovimientoDaoImpl = new QuartzMovimientoDaoImpl(conn);
            for(QuartzMovimiento quaC : quartzMovimientos){
                //Vemos si ya existe el registro:
                QuartzMovimiento quartzMovimiento = null;
                if(quaC.getIdMovimientoSistemTercero() > 0){//verificamos si se buscara por id o por clave
                    try{quartzMovimiento = quartzMovimientoDaoImpl.findByDynamicWhere( " ID_MOVIMIENTO_EVC = " + quaC.getIdMovimientoEvc() + " AND ID_MOVIMIENTO_SISTEM_TERCERO = " + quaC.getIdMovimientoSistemTercero() + " AND ID_EMPRESA = " + idEmpresa , null)[0];}catch(Exception e){/*e.printStackTrace();*/}
                }else if(quaC.getClave() != null && !quaC.getClave().equals("")){
                    try{quartzMovimiento = quartzMovimientoDaoImpl.findByDynamicWhere( " ID_MOVIMIENTO_EVC = " + quaC.getIdMovimientoEvc() + " AND CLAVE = '" + quaC.getClave()  + "' AND ID_EMPRESA = " + idEmpresa , null)[0];}catch(Exception e){/*e.printStackTrace();*/}
                }

                if(quartzMovimiento == null){//si no existe lo insertamos                    
                    quartzMovimiento = new QuartzMovimiento();
                    quartzMovimiento.setIdMovimientoEvc(quaC.getIdMovimientoEvc());
                    if(quaC.getIdMovimientoSistemTercero() > 0){
                        quartzMovimiento.setIdMovimientoSistemTercero(quaC.getIdMovimientoSistemTercero());
                    }else{
                        quartzMovimiento.setIdMovimientoSistemTercero(0);
                    }
                    if(quaC.getClave() != null && !quaC.getClave().equals("")){
                        quartzMovimiento.setClave(quaC.getClave());
                    }else{
                        quartzMovimiento.setClave("");
                    }
                    quartzMovimiento.setIdEmpresa(idEmpresa);
                    movimientosIdSincronizacion.add(quartzMovimiento);
                }else{//si existe, borramos e insertamos el registro para que sea el ultimo registro insertado y así al recuperar los ID sea el mas actual.
                    try{quartzMovimientoDaoImpl.delete(quartzMovimiento.createPk());
                    QuartzMovimiento quartzMovimientoClon = new QuartzMovimiento();
                    quartzMovimientoClon.setIdMovimientoEvc(quartzMovimiento.getIdMovimientoEvc());
                    quartzMovimientoClon.setIdMovimientoSistemTercero(quartzMovimiento.getIdMovimientoSistemTercero());
                    quartzMovimientoClon.setClave(quartzMovimiento.getClave());
                    quartzMovimientoClon.setIdSistemaTercero(quartzMovimiento.getIdSistemaTercero());
                    quartzMovimientoClon.setIdEmpresa(quartzMovimiento.getIdEmpresa());
                    quartzMovimiento.setIdQuartz(idEmpresa);
                    quartzMovimientoDaoImpl.insert(quartzMovimientoClon);
                    }catch(Exception e){System.out.println("------------ Error al actualizar Identificadores de quartz de movimiento: "+e.getMessage());}
                    
                }
                
                //actualizamos la bandera de movimientos a sincronizado:
                try{
                    Movimiento movimiento = new MovimientoBO(conn).findMovimientobyId(quaC.getIdMovimientoEvc());
                    movimiento.setSincronizacionMicrosip(1);
                    movimientoDaoImpl.update(movimiento.createPk(), movimiento);                    
                }catch(Exception e){e.printStackTrace();}
                
            }
            int insetarActualizacion = 0;
            try{
                QuartzMovimientoDaoImpl qcDaoImp = new QuartzMovimientoDaoImpl(conn);                
                for(QuartzMovimiento insetCli : movimientosIdSincronizacion){//insertamos los registros:
                    qcDaoImp.insert(insetCli);                    
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
    
    public ControlBean setMovimientoMicrosip(List<MovimientosMicrosipBean> movimientosMicrosipBean, int idEmpresa){
        ControlBean controlBean = new ControlBean();
        String mensajeMovimiento = "";
        Connection conn = null;
        try{            
            conn = ResourceManager.getConnection();
            MovimientoDaoImpl movimientosDaoImpl = new MovimientoDaoImpl(conn);
            QuartzMovimientoDaoImpl movimientoDaoImpl = new QuartzMovimientoDaoImpl(conn);
            QuartzMovimiento movimientoExistente = null;
            QuartzAlmacenDaoImpl quartzAlmacenDaoImpl = new QuartzAlmacenDaoImpl(conn);
            QuartzConceptoDaoImpl quartzConceptoDaoImpl = new QuartzConceptoDaoImpl(conn);
            QuartzEmpleadoDaoImpl quartzEmpleadoDaoImpl = new QuartzEmpleadoDaoImpl(conn);
            int contadorRegistrosNuevos = 0; //variable auxiliar para el numero de registros insertados
            int contadorRegistrosActualizados = 0; //variable auxiliar para el numero de registros insertados
            
            for(MovimientosMicrosipBean movimientoMicrosip : movimientosMicrosipBean){
                //validamos si el movimiento existe o no en el evc
                movimientoExistente = null;
                try{
                    //movimientoExistente = movimientoDaoImpl.findByDynamicWhere(" ID_MOVIMIENTO_SISTEM_TERCERO = " + movimientoMicrosip.getIdMovimientoMicrosip()  + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                    String queryOpcional = "";
                    if (movimientoMicrosip.getIdMovimientoMicrosip()>0){
                        queryOpcional = " ID_MOVIMIENTO_SISTEM_TERCERO = " + movimientoMicrosip.getIdMovimientoMicrosip();
                    }else{
                        if (StringManage.getValidString(movimientoMicrosip.getClave()).length()>0)
                            queryOpcional = " CLAVE='" + StringManage.getValidString(movimientoMicrosip.getClave()) + "'";
                    }
                    movimientoExistente = movimientoDaoImpl.findByDynamicWhere(queryOpcional +" AND ID_EMPRESA = " + idEmpresa, null)[0];
                }catch(Exception e1){movimientoExistente = null;}
                if(movimientoExistente == null){//movimiento nuevo
                    try{
                        Movimiento movimientoMicroSip = movimientoMicrosip.getMovimiento();
                        //Movimiento ultimoRegistroMovimientos = movimientosDaoImpl.findLast();
                        //int idMovimientoNuevo = ultimoRegistroMovimientos.getIdMovimiento() + 1;            
                        //movimientoMicroSip.setIdMovimiento(idMovimientoNuevo);                        
                        //movimientoMicroSip.setIdEstatus(1);
                        movimientoMicroSip.setSincronizacionMicrosip(1);
                        movimientoMicroSip.setIdEmpresa(idEmpresa);
                        //--/-/-/
                        //--/-/-/ 
                        
                        try{
                            QuartzAlmacen qa = quartzAlmacenDaoImpl.findByDynamicWhere("ID_ALMACEN_SISTEM_TERCERO = " + movimientoMicroSip.getIdAlmacen() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                            movimientoMicroSip.setIdAlmacen(qa.getIdAlmacenEvc());
                        }catch(Exception e){
                            //mensajeMovimiento += " Error al relacionar el almacen con ID: "+movimientoMicroSip.getIdAlmacen()+" (puede que no exista o este sincronizado), relacionado al movimiento con ID: "+movimientoMicrosip.getIdMovimientoMicrosip() + "; ERROR: " + e.getMessage() + " , ";
                            //controlBean.setExito(false);
                        }
                        
                        try{
                            if (movimientoMicroSip.getIdAlmacenDestino()>0){
                                QuartzAlmacen qa = quartzAlmacenDaoImpl.findByDynamicWhere("ID_ALMACEN_SISTEM_TERCERO = " + movimientoMicroSip.getIdAlmacenDestino() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                                movimientoMicroSip.setIdAlmacenDestino(qa.getIdAlmacenEvc());
                            }
                        }catch(Exception e){
                            mensajeMovimiento += " Error al relacionar el almacen destino con ID: "+movimientoMicroSip.getIdAlmacenDestino()+" (puede que no exista o este sincronizado), relacionado al movimiento con ID: "+movimientoMicrosip.getIdMovimientoMicrosip() + "; ERROR: " + e.getMessage() + " , ";
                            controlBean.setExito(false);
                            break;
                        }
                        try{
                            QuartzConcepto quartzConcepto = null;
                            if (movimientoMicroSip.getIdConcepto()>0){
                                quartzConcepto = quartzConceptoDaoImpl.findByDynamicWhere("ID_CONCEPTO_SISTEM_TERCERO = " + movimientoMicroSip.getIdConcepto() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                            }else if (StringManage.getValidString(movimientoMicrosip.getClaveConceptoSistemaTercero()).length()>0){
                                quartzConcepto = quartzConceptoDaoImpl.findByDynamicWhere( "CLAVE ='" + movimientoMicrosip.getClaveConceptoSistemaTercero() + "' AND ID_EMPRESA = " + idEmpresa, null )[0];
                            }
                            movimientoMicroSip.setIdConcepto(quartzConcepto.getIdConceptoEvc());
                        }catch(Exception e){
                            mensajeMovimiento += " Error al relacionar el concepto con ID: "+movimientoMicroSip.getIdConcepto()+" (puede que no exista o este sincronizado), relacionado al movimiento con ID: "+movimientoMicrosip.getIdMovimientoMicrosip() + "; ERROR: " + e.getMessage() + " , ";
                            controlBean.setExito(false);
                            break;
                        }
                        try{
                            if (movimientoMicroSip.getIdEmpleado()>0){
                                QuartzEmpleado quartzEmpleado = null;
                                if (movimientoMicroSip.getIdEmpleado()>0){
                                    quartzEmpleado = quartzEmpleadoDaoImpl.findByDynamicWhere("ID_EMPLEADO_SISTEM_TERCERO = " + movimientoMicroSip.getIdEmpleado()+ " AND ID_EMPRESA = " + idEmpresa, null)[0];
                                }else if (StringManage.getValidString(movimientoMicrosip.getClaveEmpleadoSistemaTercero()).length()>0){
                                    quartzEmpleado = quartzEmpleadoDaoImpl.findByDynamicWhere( "CLAVE ='" + movimientoMicrosip.getClaveEmpleadoSistemaTercero() + "' AND ID_EMPRESA = " + idEmpresa, null )[0];
                                }
                                movimientoMicroSip.setIdEmpleado(quartzEmpleado.getIdEmpleadoEvc());
                            }
                        }catch(Exception e){
                            mensajeMovimiento += " Error al relacionar el empleado con ID: "+movimientoMicroSip.getIdEmpleado()+" (puede que no exista o este sincronizado), relacionado al movimiento con ID: "+movimientoMicrosip.getIdMovimientoMicrosip() + "; ERROR: " + e.getMessage() + " , ";
                            controlBean.setExito(false);
                            break;
                        }
                        movimientoMicroSip.setTipoMovimiento(movimientoMicroSip.getTipoMovimiento());
                        movimientoMicroSip.setNombreProducto(movimientoMicroSip.getNombreProducto());
                        movimientoMicroSip.setContabilidad(movimientoMicroSip.getContabilidad());
                        movimientoMicroSip.setFechaRegistro(movimientoMicroSip.getFechaRegistro());
                        movimientoMicroSip.setIdProveedor(movimientoMicroSip.getIdProveedor());
                        movimientoMicroSip.setOrdenCompra(movimientoMicroSip.getOrdenCompra());
                        movimientoMicroSip.setNumeroGuia(movimientoMicroSip.getNumeroGuia());
                        movimientoMicroSip.setConceptoMovimiento(movimientoMicroSip.getConceptoMovimiento());
                        
                        
                        
                        /////////
                        /**
                        * REALIZAMOS LAS OPERACIONES DE SUMA O RESTA, SEGUN CORRESPONDA AL TIPO DE MOVIMIENTO, PARA ACTUALIZAR LA CANTIDAD DE STOCK QUE EXISTE
                        **/               
                        
                        Movimiento movimientoDto2 = new Movimiento();
                        
                        boolean nuevo = false;
                        boolean nuevo2 = false;

                        ExistenciaAlmacen almacenExists = null;
                        ExistenciaAlmacen almacenExists2 = null;
                        try{
                            almacenExists  = new ExistenciaAlmacenBO(conn).getExistenciaProductoAlmacen(movimientoMicroSip.getIdAlmacen(), movimientoMicroSip.getIdConcepto());
                            if(almacenExists==null){
                                nuevo = true;
                                almacenExists = new ExistenciaAlmacen();
                                almacenExists.setIdAlmacen(movimientoMicroSip.getIdAlmacen());
                                almacenExists.setIdConcepto(movimientoMicroSip.getIdConcepto());
                                almacenExists.setExistencia(0);
                                almacenExists.setEstatus(1);
                            }
                        }catch(Exception e){                            
                            System.out.println("Almacen o concepto no encontrado");
                            e.printStackTrace();
                        }

                        if(movimientoMicroSip.getTipoMovimiento().equals("SALIDA")){
                            BigDecimal numArticulosDisponibles = (new BigDecimal(almacenExists!=null?almacenExists.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal contaMovimiento = (new BigDecimal(movimientoMicroSip.getContabilidad())).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal stockTotal = numArticulosDisponibles.subtract(contaMovimiento);
                            double nuevoStock = stockTotal.doubleValue();  
                            //no validamos el stock, no importando que lleve valores negativos.
                            //if (nuevoStock<0){                                
                                //mensajeMovimiento += "El numero de articulos disponibles no son suficientes para efectuar la operación. No. de Articulos disponibles: " + (almacenExists!=null?almacenExists.getExistencia():0) +", relacionado al movimiento con ID: "+movimientoMicrosip.getIdMovimientoMicrosip() + ", ";
                                //controlBean.setExito(false);
                            //}else{
                                almacenExists.setExistencia(nuevoStock);
                            //}
                        }
                        if(movimientoMicroSip.getTipoMovimiento().equals("ENTRADA")){
                            BigDecimal numArticulosDisponibles = (new BigDecimal(almacenExists!=null?almacenExists.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal contaMovimiento = (new BigDecimal(movimientoMicroSip.getContabilidad())).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal stockTotal = numArticulosDisponibles.add(contaMovimiento);
                            almacenExists.setExistencia(stockTotal.doubleValue());
                        }
                        if(movimientoMicroSip.getTipoMovimiento().equals("TRASPASO")){// Creamos 2 Movimiento 1 salida- origen y 1 entrada  destino
                            String conceptoMovOrigen = StringManage.getValidString(movimientoMicroSip.getConceptoMovimiento());
                            //Almacén 1 Salida
                            movimientoMicroSip.setConceptoMovimiento("(SALIDA) "+ conceptoMovOrigen);
                            movimientoMicroSip.setIdAlmacenDestino(movimientoMicroSip.getIdAlmacenDestino());                    

                            //
                            BigDecimal numArticulosDisponibles = (new BigDecimal(almacenExists!=null?almacenExists.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal contaMovimiento = (new BigDecimal(movimientoMicroSip.getContabilidad())).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal stockTotal = numArticulosDisponibles.subtract(contaMovimiento);

                            double nuevoStock = stockTotal.doubleValue();  
                            //no validamos el stock, no importando que lleve valores negativos.
                            //if (nuevoStock<0){
                                //msgError+="<ul>El numero de articulos disponibles no son suficientes para efectuar la operación.<br/>No. de Articulos disponibles: " + (almacenExists!=null?almacenExists.getExistencia():0);
                            //}else{
                                almacenExists.setExistencia(nuevoStock);
                            //}

                            //Almacén 2 Entrada

                            //if (msgError.equals("")){
                                //Busca concepto en almacén destino                    

                                try{
                                    almacenExists2  = new ExistenciaAlmacenBO(conn).getExistenciaProductoAlmacen(movimientoMicroSip.getIdAlmacenDestino(), movimientoMicroSip.getIdConcepto());
                                    if(almacenExists2==null){
                                        nuevo2 = true;
                                        almacenExists2 = new ExistenciaAlmacen();
                                        almacenExists2.setIdAlmacen(movimientoMicroSip.getIdAlmacenDestino());
                                        almacenExists2.setIdConcepto(movimientoMicroSip.getIdConcepto());
                                        almacenExists2.setExistencia(0);
                                        almacenExists2.setEstatus(1);
                                    }
                                }catch(Exception e){                    
                                    //out.print("<!--ERROR-->No se pudo consultar el registro. Informe del error al administrador del sistema: " + e.toString());
                                    System.out.println("Almacen o concepto no encontrado");
                                    e.printStackTrace();
                                }   
                                BigDecimal numArticulosDisponiblesDestino = (new BigDecimal(almacenExists2!=null?almacenExists2.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                                BigDecimal contaMovimientoDestino = (new BigDecimal(movimientoMicroSip.getContabilidad())).setScale(2, RoundingMode.HALF_UP);
                                BigDecimal stockTotalDestino = numArticulosDisponiblesDestino.add(contaMovimientoDestino);

                                almacenExists2.setExistencia(stockTotalDestino.doubleValue());

                                //Registra Mov                      
                                movimientoDto2.setIdEmpresa(idEmpresa);
                                movimientoDto2.setTipoMovimiento(movimientoMicroSip.getTipoMovimiento());
                                movimientoDto2.setNombreProducto(movimientoMicroSip.getNombreProducto());
                                movimientoDto2.setContabilidad(movimientoMicroSip.getContabilidad()); 
                                movimientoDto2.setIdAlmacen(movimientoMicroSip.getIdAlmacen());                
                                movimientoDto2.setConceptoMovimiento("(ENTRADA) "+ conceptoMovOrigen);                
                                movimientoDto2.setFechaRegistro(movimientoMicroSip.getFechaRegistro());
                                movimientoDto2.setIdConcepto(movimientoMicroSip.getIdConcepto());
                                movimientoDto2.setIdAlmacenDestino(movimientoMicroSip.getIdAlmacenDestino()); 
                                movimientoDto2.setIdProveedor(movimientoMicroSip.getIdProveedor());
                                movimientoDto2.setOrdenCompra(movimientoMicroSip.getOrdenCompra());
                                movimientoDto2.setNumeroGuia(movimientoMicroSip.getNumeroGuia());
                                movimientoDto2.setSincronizacionMicrosip(1);
                                movimientoDto2.setIdEmpleado(movimientoMicroSip.getIdEmpleado());
                            //}
                        }
                        MovimientoPk movimientoPk = new MovimientoPk();
                        movimientoPk.setIdMovimiento(0);
                        //if (msgError.equals("")){
                            try{
                                /* Actualizamos existencia en almacen*/
                                if(nuevo){
                                    new ExistenciaAlmacenDaoImpl().insert(almacenExists); 
                                }else{
                                    new ExistenciaAlmacenDaoImpl().update(almacenExists.createPk(), almacenExists);
                                }

                                /**
                                * Realizamos el insert de movimiento despues de modificar existencia
                                */
                               movimientoPk = movimientosDaoImpl.insert(movimientoMicroSip);

                               if(movimientoMicroSip.getTipoMovimiento().equals("TRASPASO")){
                                   if(nuevo2){
                                        new ExistenciaAlmacenDaoImpl().insert(almacenExists2); 
                                   }else{
                                        new ExistenciaAlmacenDaoImpl().update(almacenExists2.createPk(), almacenExists2);
                                   }
                                   movimientosDaoImpl.insert(movimientoDto2);
                               }
                               //out.print("<!--EXITO-->Registro guardado satisfactoriamente");
                            }catch(Exception ex){
                                //out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                                ex.printStackTrace();
                            } 
                        /*}else{
                            out.print("<!--ERROR-->"+msgError);
                        }*/
                            /////////
                            /**
                            * FIN
                            **/ 
                        
                        
                        
                        
                        //MovimientoPk movimientoPk = movimientosDaoImpl.insert(movimientoMicroSip); -------------------------

                        ////insertamos la relacion con los Id's
                        QuartzMovimiento qc = new QuartzMovimiento();
                        qc.setClave( (movimientoMicrosip.getClave()!=null?movimientoMicrosip.getClave():null) );
                        qc.setIdMovimientoEvc(movimientoPk.getIdMovimiento());
                        qc.setIdMovimientoSistemTercero(movimientoMicrosip.getIdMovimientoMicrosip());
                        qc.setIdSistemaTercero(1);
                        qc.setIdEmpresa(idEmpresa);
                        movimientoDaoImpl.insert(qc);
                        contadorRegistrosNuevos++;
                    }catch(Exception e){
                        mensajeMovimiento += " Error al insertar movimiento con ID: "+movimientoMicrosip.getIdMovimientoMicrosip() + "; ERROR: " + e.getMessage() + " , ";
                        e.printStackTrace();
                    }  
                }else{//actualizar
                    try{
                        Movimiento movimientoMicroSip = movimientoMicrosip.getMovimiento();                     
                        movimientoMicroSip.setIdMovimiento(movimientoExistente.getIdMovimientoEvc());
                        movimientoMicroSip.setSincronizacionMicrosip(1);
                        
                        try{
                            //QuartzAlmacen qa = quartzAlmacenDaoImpl.findWhereIdAlmacenSistemTerceroEquals(movimientoMicroSip.getIdAlmacen())[0];
                            QuartzAlmacen qa = quartzAlmacenDaoImpl.findByDynamicWhere("ID_ALMACEN_SISTEM_TERCERO = " + movimientoMicroSip.getIdAlmacen() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                            movimientoMicroSip.setIdAlmacen(qa.getIdAlmacenEvc());
                        }catch(Exception e){
                            //mensajeMovimiento += " Error al relacionar el almacen con ID: "+movimientoMicroSip.getIdAlmacen()+" (puede que no exista o este sincronizado), relacionado al movimiento con ID: "+movimientoMicrosip.getIdMovimientoMicrosip() + "; ERROR: " + e.getMessage() + " , ";
                            //controlBean.setExito(false);
                        }
                        
                        try{
                            if (movimientoMicroSip.getIdAlmacenDestino()>0){
                                QuartzAlmacen qa = quartzAlmacenDaoImpl.findByDynamicWhere("ID_ALMACEN_SISTEM_TERCERO = " + movimientoMicroSip.getIdAlmacenDestino() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                                movimientoMicroSip.setIdAlmacenDestino(qa.getIdAlmacenEvc());
                            }
                        }catch(Exception e){
                            mensajeMovimiento += " Error al relacionar el almacen destino con ID: "+movimientoMicroSip.getIdAlmacenDestino()+" (puede que no exista o este sincronizado), relacionado al movimiento con ID: "+movimientoMicrosip.getIdMovimientoMicrosip() + "; ERROR: " + e.getMessage() + " , ";
                            controlBean.setExito(false);
                            break;
                        }
                        try{
                            QuartzConcepto quartzConcepto = null;
                            if (movimientoMicroSip.getIdConcepto()>0){
                                quartzConcepto = quartzConceptoDaoImpl.findByDynamicWhere("ID_CONCEPTO_SISTEM_TERCERO = " + movimientoMicroSip.getIdConcepto() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                            }else if (StringManage.getValidString(movimientoMicrosip.getClaveConceptoSistemaTercero()).length()>0){
                                quartzConcepto = quartzConceptoDaoImpl.findByDynamicWhere( "CLAVE ='" + movimientoMicrosip.getClaveConceptoSistemaTercero() + "' AND ID_EMPRESA = " + idEmpresa, null )[0];
                            }
                            movimientoMicroSip.setIdConcepto(quartzConcepto.getIdConceptoEvc());
                        }catch(Exception e){
                            mensajeMovimiento += " Error al relacionar el concepto con ID: "+movimientoMicroSip.getIdConcepto()+" (puede que no exista o este sincronizado), relacionado al movimiento con ID: "+movimientoMicrosip.getIdMovimientoMicrosip() + "; ERROR: " + e.getMessage() + " , ";
                            controlBean.setExito(false);
                            break;
                        }
                        try{
                            if (movimientoMicroSip.getIdEmpleado()>0){
                                QuartzEmpleado quartzEmpleado = null;
                                if (movimientoMicroSip.getIdEmpleado()>0){
                                    quartzEmpleado = quartzEmpleadoDaoImpl.findByDynamicWhere("ID_EMPLEADO_SISTEM_TERCERO = " + movimientoMicroSip.getIdEmpleado()+ " AND ID_EMPRESA = " + idEmpresa, null)[0];
                                }else if (StringManage.getValidString(movimientoMicrosip.getClaveEmpleadoSistemaTercero()).length()>0){
                                    quartzEmpleado = quartzEmpleadoDaoImpl.findByDynamicWhere( "CLAVE ='" + movimientoMicrosip.getClaveEmpleadoSistemaTercero() + "' AND ID_EMPRESA = " + idEmpresa, null )[0];
                                }
                                movimientoMicroSip.setIdEmpleado(quartzEmpleado.getIdEmpleadoEvc());
                            }
                        }catch(Exception e){
                            mensajeMovimiento += " Error al relacionar el empleado con ID: "+movimientoMicroSip.getIdEmpleado()+" (puede que no exista o este sincronizado), relacionado al movimiento con ID: "+movimientoMicrosip.getIdMovimientoMicrosip() + "; ERROR: " + e.getMessage() + " , ";
                            controlBean.setExito(false);
                            break;
                        }
                        movimientoMicroSip.setTipoMovimiento(movimientoMicroSip.getTipoMovimiento());
                        movimientoMicroSip.setNombreProducto(movimientoMicroSip.getNombreProducto());
                        movimientoMicroSip.setContabilidad(movimientoMicroSip.getContabilidad());
                        movimientoMicroSip.setFechaRegistro(movimientoMicroSip.getFechaRegistro());
                        movimientoMicroSip.setIdProveedor(movimientoMicroSip.getIdProveedor());
                        movimientoMicroSip.setOrdenCompra(movimientoMicroSip.getOrdenCompra());
                        movimientoMicroSip.setNumeroGuia(movimientoMicroSip.getNumeroGuia());
                        movimientoMicroSip.setConceptoMovimiento(movimientoMicroSip.getConceptoMovimiento());
                        //--/-/-/
                        
                        /////////
                        /**
                        * REALIZAMOS LAS OPERACIONES DE SUMA O RESTA, SEGUN CORRESPONDA AL TIPO DE MOVIMIENTO, PARA ACTUALIZAR LA CANTIDAD DE STOCK QUE EXISTE
                        **/               
                        
/*                        Movimiento movimientoDto2 = new Movimiento();
                        
                        boolean nuevo = false;
                        boolean nuevo2 = false;

                        ExistenciaAlmacen almacenExists = null;
                        ExistenciaAlmacen almacenExists2 = null;
                        try{
                            almacenExists  = new ExistenciaAlmacenBO(conn).getExistenciaProductoAlmacen(movimientoMicroSip.getIdAlmacen(), movimientoMicroSip.getIdConcepto());
                            if(almacenExists==null){
                                nuevo = true;
                                almacenExists = new ExistenciaAlmacen();
                                almacenExists.setIdAlmacen(movimientoMicroSip.getIdAlmacen());
                                almacenExists.setIdConcepto(movimientoMicroSip.getIdConcepto());
                                almacenExists.setExistencia(0);
                                almacenExists.setEstatus(1);
                            }
                        }catch(Exception e){                            
                            System.out.println("Almacen o concepto no encontrado");
                            e.printStackTrace();
                        }

                        if(movimientoMicroSip.getTipoMovimiento().equals("SALIDA")){
                            BigDecimal numArticulosDisponibles = (new BigDecimal(almacenExists!=null?almacenExists.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal contaMovimiento = (new BigDecimal(movimientoMicroSip.getContabilidad())).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal stockTotal = numArticulosDisponibles.subtract(contaMovimiento);
                            double nuevoStock = stockTotal.doubleValue();  
                            //no validamos el stock, no importando que lleve valores negativos.
                            //if (nuevoStock<0){                                
                                //mensajeMovimiento += "El numero de articulos disponibles no son suficientes para efectuar la operación. No. de Articulos disponibles: " + (almacenExists!=null?almacenExists.getExistencia():0) +", relacionado al movimiento con ID: "+movimientoMicrosip.getIdMovimientoMicrosip() + ", ";
                                //controlBean.setExito(false);
                            //}else{
                                almacenExists.setExistencia(nuevoStock);
                            //}
                        }
                        if(movimientoMicroSip.getTipoMovimiento().equals("ENTRADA")){
                            BigDecimal numArticulosDisponibles = (new BigDecimal(almacenExists!=null?almacenExists.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal contaMovimiento = (new BigDecimal(movimientoMicroSip.getContabilidad())).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal stockTotal = numArticulosDisponibles.add(contaMovimiento);
                            almacenExists.setExistencia(stockTotal.doubleValue());
                        }
                        if(movimientoMicroSip.getTipoMovimiento().equals("TRASPASO")){// Creamos 2 Movimiento 1 salida- origen y 1 entrada  destino
                            //Almacén 1 Salida
                            movimientoMicroSip.setConceptoMovimiento("(SALIDA) "+ movimientoMicroSip.getConceptoMovimiento());
                            movimientoMicroSip.setIdAlmacenDestino(movimientoMicroSip.getIdAlmacenDestino());                    

                            //
                            BigDecimal numArticulosDisponibles = (new BigDecimal(almacenExists!=null?almacenExists.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal contaMovimiento = (new BigDecimal(movimientoMicroSip.getContabilidad())).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal stockTotal = numArticulosDisponibles.subtract(contaMovimiento);

                            double nuevoStock = stockTotal.doubleValue();  
                            //no validamos el stock, no importando que lleve valores negativos.
                            //if (nuevoStock<0){
                                //msgError+="<ul>El numero de articulos disponibles no son suficientes para efectuar la operación.<br/>No. de Articulos disponibles: " + (almacenExists!=null?almacenExists.getExistencia():0);
                            //}else{
                                almacenExists.setExistencia(nuevoStock);
                            //}

                            //Almacén 2 Entrada

                            //if (msgError.equals("")){
                                //Busca concepto en almacén destino                    

                                try{
                                    almacenExists2  = new ExistenciaAlmacenBO(conn).getExistenciaProductoAlmacen(movimientoMicroSip.getIdAlmacenDestino(), movimientoMicroSip.getIdConcepto());
                                    if(almacenExists2==null){
                                        nuevo2 = true;
                                        almacenExists2 = new ExistenciaAlmacen();
                                        almacenExists2.setIdAlmacen(movimientoMicroSip.getIdAlmacenDestino());
                                        almacenExists2.setIdConcepto(movimientoMicroSip.getIdConcepto());
                                        almacenExists2.setExistencia(0);
                                        almacenExists2.setEstatus(1);
                                    }
                                }catch(Exception e){                    
                                    //out.print("<!--ERROR-->No se pudo consultar el registro. Informe del error al administrador del sistema: " + e.toString());
                                    System.out.println("Almacen o concepto no encontrado");
                                    e.printStackTrace();
                                }   
                                BigDecimal numArticulosDisponiblesDestino = (new BigDecimal(almacenExists2!=null?almacenExists2.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                                BigDecimal contaMovimientoDestino = (new BigDecimal(movimientoMicroSip.getContabilidad())).setScale(2, RoundingMode.HALF_UP);
                                BigDecimal stockTotalDestino = numArticulosDisponiblesDestino.add(contaMovimientoDestino);

/*                                almacenExists2.setExistencia(stockTotalDestino.doubleValue());

                                //Registra Mov                      
                                movimientoDto2.setIdEmpresa(idEmpresa);
                                movimientoDto2.setTipoMovimiento(movimientoMicroSip.getTipoMovimiento());
                                movimientoDto2.setNombreProducto(movimientoMicroSip.getNombreProducto());
                                movimientoDto2.setContabilidad(movimientoMicroSip.getContabilidad()); 
                                movimientoDto2.setIdAlmacen(movimientoMicroSip.getIdAlmacen());                
                                movimientoDto2.setConceptoMovimiento("(ENTRADA) "+ movimientoMicroSip.getConceptoMovimiento());                
                                movimientoDto2.setFechaRegistro(movimientoMicroSip.getFechaRegistro());
                                movimientoDto2.setIdConcepto(movimientoMicroSip.getIdConcepto());
                                movimientoDto2.setIdAlmacenDestino(movimientoMicroSip.getIdAlmacenDestino()); 
                                movimientoDto2.setIdProveedor(movimientoMicroSip.getIdProveedor());
                                movimientoDto2.setOrdenCompra(movimientoMicroSip.getOrdenCompra());
                                movimientoDto2.setNumeroGuia(movimientoMicroSip.getNumeroGuia());
                            //}
                        }
                        MovimientoPk movimientoPk = new MovimientoPk();
                        movimientoPk.setIdMovimiento(0);
                        //if (msgError.equals("")){
                            try{
                                /* Actualizamos existencia en almacen*/
/*                                if(nuevo){
                                    new ExistenciaAlmacenDaoImpl().insert(almacenExists); 
                                }else{
                                    new ExistenciaAlmacenDaoImpl().update(almacenExists.createPk(), almacenExists);
                                }

                                /**
                                * Realizamos el insert de movimiento despues de modificar existencia
                                */
/*                               movimientosDaoImpl.update(movimientoMicroSip.createPk(), movimientoMicroSip);

                               if(movimientoMicroSip.getTipoMovimiento().equals("TRASPASO")){
                                   if(nuevo2){
                                        new ExistenciaAlmacenDaoImpl().insert(almacenExists2); 
                                   }else{
                                        new ExistenciaAlmacenDaoImpl().update(almacenExists2.createPk(), almacenExists2);
                                   }
                                   movimientosDaoImpl.insert(movimientoDto2);
                               }
                               //out.print("<!--EXITO-->Registro guardado satisfactoriamente");
                            }catch(Exception ex){
                                //out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                                ex.printStackTrace();
                            } 
                        /*}else{
                            out.print("<!--ERROR-->"+msgError);
                        }*/
                            /////////
                            /**
                            * FIN
                            **/
                        
                        //movimientosDaoImpl.update(movimientoMicroSip.createPk(), movimientoMicroSip ); -------------------------
/*                        contadorRegistrosActualizados++;
*/                    }catch(Exception e2){
                         mensajeMovimiento += " Error al actualizar movimiento con ID: "+movimientoMicrosip.getIdMovimientoMicrosip() + "; ERROR: " + e2.getMessage() + " , ";
                         e2.printStackTrace();
                    }
        
                }
            }
            
            if(mensajeMovimiento.trim().equals("")){
                controlBean.setExito(true);
            }
            controlBean.setMensajeError(mensajeMovimiento);
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
