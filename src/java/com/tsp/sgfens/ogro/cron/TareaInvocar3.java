/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ogro.cron;

import com.tsp.sct.bo.ClienteBO;
import com.tsp.sct.bo.EmpleadoBO;
import com.tsp.sct.bo.SGClienteVendedorBO;
import com.tsp.sct.dao.dto.Cliente;
import com.tsp.sct.dao.dto.Empleado;
import com.tsp.sct.dao.dto.EmpleadoAgenda;
import com.tsp.sct.dao.dto.SgfensAsignacionEmpleados;
import com.tsp.sct.dao.dto.SgfensClienteVendedor;
import com.tsp.sct.dao.dto.SgfensPedido;
import com.tsp.sct.dao.jdbc.EmpleadoAgendaDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensAsignacionEmpleadosDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensClienteVendedorDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensPedidoDaoImpl;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author 578
 */
public class TareaInvocar3 implements Job{
    
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
  //Metodo que se ejecutara cada cierto tiempo que lo programemos despues
  public void execute(JobExecutionContext jec) throws JobExecutionException {
    //Aca pueden poner la tarea o el job que desean automatizar
    //Por ejemplo enviar correo, revisar ciertos datos, etc
    SimpleDateFormat formato = new SimpleDateFormat("hh:mm:ss");
    System.out.println( "Tarea invocada a la hora: " + formato.format(new Date()));
    
    ///**invocamos la actualizacion y descarga:
     System.out.println("TAREAS AUTOMATICAS ");
     
    ///**
     
     //---------------------------------------- INCIA ASIGNACION CLIENTES ------------------------>
     try{
         
         System.out.println("____Actualiza Asignaciones______");
         
        String filtroAgenda =" ID_USUARIO_VENDEDOR_REASIGNADO > 0 AND FECHA_LIMITE_REASIGANCION IS NOT NULL ";
        SgfensClienteVendedorDaoImpl clienteVendedorDaoImpl = new SgfensClienteVendedorDaoImpl();
        SgfensClienteVendedor[] clienteVendedorDtos = clienteVendedorDaoImpl.findByDynamicWhere(filtroAgenda, null);

        Calendar c = Calendar.getInstance(); //OBTENGO LA FECHA DE HOY 
        Date hoy = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(hoy); 
        hoy = sdf.parse(date);
        
        for(SgfensClienteVendedor cliVend: clienteVendedorDtos ){
            
            if(cliVend.getFechaLimiteReasigancion().before(hoy)){
                
                cliVend.setIdUsuarioVendedorReasignado(0);
                cliVend.setFechaLimiteReasigancion(null);
                
                try{
                    new SgfensClienteVendedorDaoImpl(this.conn).update(cliVend.createPk(),cliVend);                                        
                }catch(Exception e){
                    e.printStackTrace();
                }
                
                System.out.println("____Actualización de  Asignaciones correcta______");
            }
            
        }
        
     }catch(Exception e){e.printStackTrace();}
     
     //---------------------------------------- FIN ASIGNACION CLIENTES ------------------------>
     
     
     
     
     
     //---------------------------------------- INCIA TAREAS AUTOMATICAS ------------------------>
     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            
     try{       
            ClienteBO clienteBO = new ClienteBO(this.conn);               
            Cliente[] clientesDto = null;
            String filtro ="";
            
            String domingo = "DOM";
            String lunes = "LUN";
            String martes = "MAR";
            String miercoles = "MIE";
            String jueves = "JUE";
            String viernes = "VIE";
            String sabado = "SAB";
            
            EmpleadoBO empleadoBO = new EmpleadoBO(this.conn);    
            
            String filtroAgenda =" ID_USUARIO_VENDEDOR_REASIGNADO > 0 AND FECHA_LIMITE_REASIGANCION IS NOT NULL ";
            SgfensClienteVendedorDaoImpl clienteVendedorDaoImpl = new SgfensClienteVendedorDaoImpl();
            SgfensClienteVendedor[] clienteVendedorDtos = clienteVendedorDaoImpl.findByDynamicWhere(filtroAgenda, null);
            
            for(SgfensClienteVendedor cliVend: clienteVendedorDtos ){//todos los empleados con clientes compartidos
                
             /*Empleado empOrigen = empleadoBO.findEmpleadoByUsuario(cliVend.getIdUsuarioVendedor());
             Empleado empDestino = empleadoBO.findEmpleadoByUsuario(cliVend.getIdUsuarioVendedorReasignado());*/
                    
                    if(domingo.trim().equals("DOM")){                        
                        filtro = " AND  ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR="+ cliVend.getIdUsuarioVendedor() +")"
                                + " AND DIAS_VISITA LIKE '%DOM%' AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR_REASIGNADO > 0 ) ";
                        clientesDto = clienteBO.findClientes(-1, -1, -1, -1, filtro);
                        for(Cliente item: clientesDto){
                           
                                    //Creamos tareas para cada cliente 
                                    
                                    Empleado empleadoTarea =  empleadoBO.findEmpleadoByUsuario(cliVend.getIdUsuarioVendedorReasignado());
                                    String nombreCliente = item.getNombreCliente()!=null?item.getNombreCliente():"" + " " + item.getApellidoPaternoCliente()!=null?item.getApellidoPaternoCliente():"" + " " + item.getApellidoMaternoCliente()!=null?item.getApellidoMaternoCliente():"";                                    
                                    
                                    EmpleadoAgendaDaoImpl empleadoAgendaDao = new EmpleadoAgendaDaoImpl();
                                    EmpleadoAgenda[] emAgendas = empleadoAgendaDao.findByDynamicWhere(" ID_ESTATUS <> 2 AND ID_CLIENTE = " + item.getIdCliente() + " AND FECHA_PROGRAMADA = '" + df.format(new Date()) + "'", null);
                                    
                                    Date fechaProg = null;                                    
                                    Calendar c = Calendar.getInstance(); //OBTENGO LA FECHA DE HOY 
                                    Date hoy = c.getTime();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String date = sdf.format(hoy); 
                                    hoy = sdf.parse(date);
                                    
                                    if(!cliVend.getFechaLimiteReasigancion().before(hoy)&&hoy.getDay() == 0){  
                                         
                                        if(emAgendas.length == 0){
                                            EmpleadoAgenda emAgenda = new EmpleadoAgenda();
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());


                                            try{
                                                new EmpleadoAgendaDaoImpl(this.conn).insert(emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                        }else{                                       

                                            EmpleadoAgenda emAgenda = emAgendas[0];
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());

                                            try{
                                                new EmpleadoAgendaDaoImpl(this.conn).update(emAgenda.createPk(),emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }

                                        }
                                    }

                                    
                        }
                    }   
                    if(lunes.trim().equals("LUN")){
                       filtro = " AND  ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR="+ cliVend.getIdUsuarioVendedor() +")"
                                + " AND DIAS_VISITA LIKE '%LUN%' AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR_REASIGNADO > 0 ) ";
                        clientesDto = clienteBO.findClientes(-1, -1, -1, -1, filtro);
                        for(Cliente item: clientesDto){                                    
                                    
                                    //Creamos tareas para cada cliente 
                                    
                                    Empleado empleadoTarea =  empleadoBO.findEmpleadoByUsuario(cliVend.getIdUsuarioVendedorReasignado());
                                    String nombreCliente = item.getNombreCliente()!=null?item.getNombreCliente():"" + " " + item.getApellidoPaternoCliente()!=null?item.getApellidoPaternoCliente():"" + " " + item.getApellidoMaternoCliente()!=null?item.getApellidoMaternoCliente():"";                                    
                                    
                                    EmpleadoAgendaDaoImpl empleadoAgendaDao = new EmpleadoAgendaDaoImpl();
                                    EmpleadoAgenda[] emAgendas = empleadoAgendaDao.findByDynamicWhere(" ID_ESTATUS <> 2 AND ID_CLIENTE = " + item.getIdCliente() + " AND FECHA_PROGRAMADA = '" + df.format(new Date()) + "'", null);
                                    
                                    Date fechaProg = null;                                    
                                    Calendar c = Calendar.getInstance(); //OBTENGO LA FECHA DE HOY 
                                    Date hoy = c.getTime();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String date = sdf.format(hoy); 
                                    hoy = sdf.parse(date);
                                    
                                    if(!cliVend.getFechaLimiteReasigancion().before(hoy)&&hoy.getDay() == 1){  
                                    
                                        if(emAgendas.length == 0){
                                            EmpleadoAgenda emAgenda = new EmpleadoAgenda();
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());


                                            try{
                                                new EmpleadoAgendaDaoImpl(this.conn).insert(emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                        }else{                                       

                                            EmpleadoAgenda emAgenda = emAgendas[0];
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());

                                            try{
                                                new EmpleadoAgendaDaoImpl(this.conn).update(emAgenda.createPk(),emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                        }
                    }  
                    if(martes.trim().equals("MAR")){
                        filtro = " AND  ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR="+ cliVend.getIdUsuarioVendedor() +")"
                                + " AND DIAS_VISITA LIKE '%MAR%' AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR_REASIGNADO > 0 ) ";
                        clientesDto = clienteBO.findClientes(-1, -1, -1, -1, filtro);
                        for(Cliente item: clientesDto){                              
                                    
                                    //Creamos tareas para cada cliente 
                                    
                                    Empleado empleadoTarea =  empleadoBO.findEmpleadoByUsuario(cliVend.getIdUsuarioVendedorReasignado());
                                    String nombreCliente = item.getNombreCliente()!=null?item.getNombreCliente():"" + " " + item.getApellidoPaternoCliente()!=null?item.getApellidoPaternoCliente():"" + " " + item.getApellidoMaternoCliente()!=null?item.getApellidoMaternoCliente():"";                                    
                                    
                                    EmpleadoAgendaDaoImpl empleadoAgendaDao = new EmpleadoAgendaDaoImpl();
                                    EmpleadoAgenda[] emAgendas = empleadoAgendaDao.findByDynamicWhere(" ID_ESTATUS <> 2 AND ID_CLIENTE = " + item.getIdCliente() + " AND FECHA_PROGRAMADA = '" + df.format(new Date()) + "'", null);
                                    
                                    Date fechaProg = null;                                    
                                    Calendar c = Calendar.getInstance(); //OBTENGO LA FECHA DE HOY 
                                    Date hoy = c.getTime();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String date = sdf.format(hoy); 
                                    hoy = sdf.parse(date);
                                    
                                    if(!cliVend.getFechaLimiteReasigancion().before(hoy)&&hoy.getDay() == 2){  
                                        
                                        if(emAgendas.length == 0){
                                            EmpleadoAgenda emAgenda = new EmpleadoAgenda();
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());


                                            try{
                                                new EmpleadoAgendaDaoImpl(this.conn).insert(emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                        }else{                                       

                                            EmpleadoAgenda emAgenda = emAgendas[0];
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());

                                            try{
                                                new EmpleadoAgendaDaoImpl(this.conn).update(emAgenda.createPk(),emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                                    
                        }
                    }           
                    if(miercoles.trim().equals("MIE")){
                       filtro = " AND  ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR="+ cliVend.getIdUsuarioVendedor() +")"
                                + " AND DIAS_VISITA LIKE '%MIE%' AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR_REASIGNADO > 0 ) ";
                        clientesDto = clienteBO.findClientes(-1, -1, -1, -1, filtro);
                        for(Cliente item: clientesDto){                                
                                    
                                    //Creamos tareas para cada cliente 
                                    
                                    Empleado empleadoTarea =  empleadoBO.findEmpleadoByUsuario(cliVend.getIdUsuarioVendedorReasignado());
                                    String nombreCliente = item.getNombreCliente()!=null?item.getNombreCliente():"" + " " + item.getApellidoPaternoCliente()!=null?item.getApellidoPaternoCliente():"" + " " + item.getApellidoMaternoCliente()!=null?item.getApellidoMaternoCliente():"";                                    
                                    
                                    EmpleadoAgendaDaoImpl empleadoAgendaDao = new EmpleadoAgendaDaoImpl();
                                    EmpleadoAgenda[] emAgendas = empleadoAgendaDao.findByDynamicWhere(" ID_ESTATUS <> 2 AND ID_CLIENTE = " + item.getIdCliente() + " AND FECHA_PROGRAMADA = '" + df.format(new Date()) + "'", null);
                                    
                                    Date fechaProg = null;                                    
                                    Calendar c = Calendar.getInstance(); //OBTENGO LA FECHA DE HOY 
                                    Date hoy = c.getTime();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String date = sdf.format(hoy); 
                                    hoy = sdf.parse(date);
                                    
                                    if(!cliVend.getFechaLimiteReasigancion().before(hoy)&&hoy.getDay() == 3){  
                                        
                                        if(emAgendas.length == 0){
                                            EmpleadoAgenda emAgenda = new EmpleadoAgenda();
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());


                                            try{
                                                new EmpleadoAgendaDaoImpl(this.conn).insert(emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                        }else{                                       

                                            EmpleadoAgenda emAgenda = emAgendas[0];
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());

                                            try{
                                                new EmpleadoAgendaDaoImpl(this.conn).update(emAgenda.createPk(),emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                                    
                        }
                    }            
                    if(jueves.trim().equals("JUE")){
                       filtro = " AND  ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR="+ cliVend.getIdUsuarioVendedor() +")"
                                + " AND DIAS_VISITA LIKE '%JUE%' AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR_REASIGNADO > 0 ) ";
                        clientesDto = clienteBO.findClientes(-1, -1, -1, -1, filtro);
                        for(Cliente item: clientesDto){
                           
                                    //Creamos tareas para cada cliente 
                                    
                                    Empleado empleadoTarea =  empleadoBO.findEmpleadoByUsuario(cliVend.getIdUsuarioVendedorReasignado());
                                    String nombreCliente = item.getNombreCliente()!=null?item.getNombreCliente():"" + " " + item.getApellidoPaternoCliente()!=null?item.getApellidoPaternoCliente():"" + " " + item.getApellidoMaternoCliente()!=null?item.getApellidoMaternoCliente():"";                                    
                                    
                                    EmpleadoAgendaDaoImpl empleadoAgendaDao = new EmpleadoAgendaDaoImpl();
                                    EmpleadoAgenda[] emAgendas = empleadoAgendaDao.findByDynamicWhere(" ID_ESTATUS <> 2 AND ID_CLIENTE = " + item.getIdCliente() + " AND FECHA_PROGRAMADA = '" + df.format(new Date()) + "'", null);
                                    
                                    Date fechaProg = null;                                    
                                    Calendar c = Calendar.getInstance(); //OBTENGO LA FECHA DE HOY 
                                    Date hoy = c.getTime();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String date = sdf.format(hoy); 
                                    hoy = sdf.parse(date);
                                    
                                    if(!cliVend.getFechaLimiteReasigancion().before(hoy)&&hoy.getDay() == 4){  
                                        
                                        if(emAgendas.length == 0){
                                            EmpleadoAgenda emAgenda = new EmpleadoAgenda();
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());


                                            try{
                                                new EmpleadoAgendaDaoImpl(this.conn).insert(emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                        }else{                                       

                                            EmpleadoAgenda emAgenda = emAgendas[0];
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());

                                            try{
                                                new EmpleadoAgendaDaoImpl(this.conn).update(emAgenda.createPk(),emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                                    
                        }
                    }            
                    if(viernes.trim().equals("VIE")){
                       filtro = " AND  ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR="+ cliVend.getIdUsuarioVendedor() +")"
                                + " AND DIAS_VISITA LIKE '%VIE%' AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR_REASIGNADO > 0 AND FECHA_LIMITE_REASIGANCION = DATE('"+cliVend.getFechaLimiteReasigancion()+"') ) ";
                        clientesDto = clienteBO.findClientes(-1, -1, -1, -1, filtro);
                        for(Cliente item: clientesDto){
                            
                                    
                                    //Creamos tareas para cada cliente 
                                    
                                    Empleado empleadoTarea =  empleadoBO.findEmpleadoByUsuario(cliVend.getIdUsuarioVendedorReasignado());
                                    String nombreCliente = item.getNombreCliente()!=null?item.getNombreCliente():"" + " " + item.getApellidoPaternoCliente()!=null?item.getApellidoPaternoCliente():"" + " " + item.getApellidoMaternoCliente()!=null?item.getApellidoMaternoCliente():"";                                    
                                    
                                    EmpleadoAgendaDaoImpl empleadoAgendaDao = new EmpleadoAgendaDaoImpl();
                                    EmpleadoAgenda[] emAgendas = empleadoAgendaDao.findByDynamicWhere(" ID_ESTATUS <> 2 AND ID_CLIENTE = " + item.getIdCliente() + " AND FECHA_PROGRAMADA = '" + df.format(new Date()) + "'", null);
                                    
                                    Date fechaProg = null;                                    
                                    Calendar c = Calendar.getInstance(); //OBTENGO LA FECHA DE HOY 
                                    Date hoy = c.getTime();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String date = sdf.format(hoy); 
                                    hoy = sdf.parse(date);
                                            
                                    
                                    if((!cliVend.getFechaLimiteReasigancion().before(hoy))&&hoy.getDay() == 5){  
                                        
                                        if(emAgendas.length == 0){
                                            EmpleadoAgenda emAgenda = new EmpleadoAgenda();
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());


                                            try{
                                                new EmpleadoAgendaDaoImpl(this.conn).insert(emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                        }else{                                       

                                            EmpleadoAgenda emAgenda = emAgendas[0];
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());

                                            try{
                                                new EmpleadoAgendaDaoImpl(this.conn).update(emAgenda.createPk(),emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                                    
                        }
                    }            
                    if(sabado.trim().equals("SAB")){
                        filtro = " AND  ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR="+ cliVend.getIdUsuarioVendedor() +")"
                                + " AND DIAS_VISITA LIKE '%SAB%' AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR_REASIGNADO > 0 ) ";
                        clientesDto = clienteBO.findClientes(-1, -1, -1, -1, filtro);
                        for(Cliente item: clientesDto){
                           
                                    //Creamos tareas para cada cliente 
                                    
                                    Empleado empleadoTarea =  empleadoBO.findEmpleadoByUsuario(cliVend.getIdUsuarioVendedorReasignado());
                                    String nombreCliente = item.getNombreCliente()!=null?item.getNombreCliente():"" + " " + item.getApellidoPaternoCliente()!=null?item.getApellidoPaternoCliente():"" + " " + item.getApellidoMaternoCliente()!=null?item.getApellidoMaternoCliente():"";                                    
                                    
                                    EmpleadoAgendaDaoImpl empleadoAgendaDao = new EmpleadoAgendaDaoImpl();
                                    EmpleadoAgenda[] emAgendas = empleadoAgendaDao.findByDynamicWhere(" ID_ESTATUS <> 2 AND ID_CLIENTE = " + item.getIdCliente() + " AND FECHA_PROGRAMADA = '" + df.format(new Date()) + "'", null);
                                    
                                    Date fechaProg = null;                                    
                                    Calendar c = Calendar.getInstance(); //OBTENGO LA FECHA DE HOY 
                                    Date hoy = c.getTime();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String date = sdf.format(hoy); 
                                    hoy = sdf.parse(date);
                                    
                                    if(!cliVend.getFechaLimiteReasigancion().before(hoy)&&hoy.getDay() == 6){  
                                        
                                        if(emAgendas.length == 0){
                                            EmpleadoAgenda emAgenda = new EmpleadoAgenda();
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());


                                            try{
                                                new EmpleadoAgendaDaoImpl(this.conn).insert(emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                        }else{                                       

                                            EmpleadoAgenda emAgenda = emAgendas[0];
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());

                                            try{
                                                new EmpleadoAgendaDaoImpl(this.conn).update(emAgenda.createPk(),emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                        }

                    }
                    
                    //if(tipoAsignacion.equals("CLIENTES")) {                    
                       
                       filtro = " AND  ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR="+ cliVend.getIdUsuarioVendedor() +")"
                                + " AND DIAS_VISITA = '' AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR_REASIGNADO > 0 AND FECHA_LIMITE_REASIGANCION = DATE('"+cliVend.getFechaLimiteReasigancion()+"') ) ";
                                
                        clientesDto = clienteBO.findClientes(-1, -1, -1, -1, filtro);
                        for(Cliente item: clientesDto){                             
                                    
                                    //Creamos tareas para cada cliente 
                                    
                                    Empleado empleadoTarea =  empleadoBO.findEmpleadoByUsuario(cliVend.getIdUsuarioVendedorReasignado());
                                    String nombreCliente = item.getNombreCliente()!=null?item.getNombreCliente():"" + " " + item.getApellidoPaternoCliente()!=null?item.getApellidoPaternoCliente():"" + " " + item.getApellidoMaternoCliente()!=null?item.getApellidoMaternoCliente():"";                                    
                                    
                                    EmpleadoAgendaDaoImpl empleadoAgendaDao = new EmpleadoAgendaDaoImpl();
                                    EmpleadoAgenda[] emAgendas = empleadoAgendaDao.findByDynamicWhere(" ID_ESTATUS <> 2 AND ID_CLIENTE = " + item.getIdCliente() + " AND FECHA_PROGRAMADA = '" + df.format(new Date()) + "'", null);
                                    
                                    Date fechaProg = null;                                    
                                    Calendar c = Calendar.getInstance(); //OBTENGO LA FECHA DE HOY 
                                    Date hoy = c.getTime();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String date = sdf.format(hoy); 
                                    hoy = sdf.parse(date);
                                    
                                    if(!cliVend.getFechaLimiteReasigancion().before(hoy)){
                                        
                                        if(emAgendas.length == 0){
                                            EmpleadoAgenda emAgenda = new EmpleadoAgenda();
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());


                                            try{
                                                new EmpleadoAgendaDaoImpl(this.conn).insert(emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                        }else{                                       

                                            EmpleadoAgenda emAgenda = emAgendas[0];
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());

                                            try{
                                                new EmpleadoAgendaDaoImpl(this.conn).update(emAgenda.createPk(),emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                                    
                        }
                    //} 
            }

                    System.out.print("Tareas creadas satisfactoriamente");
     
     }catch(Exception e){
         e.printStackTrace();
     }
     
     
      //---------------------------------------- FIN TAREAS AUTOMATICAS ------------------------>
     
     
     //---------------------------------------- INCIA TERMINO DE RE-ASIGNACION DE PEDIDOS ------------------------>
     try{
         
        System.out.println("____Actualiza Re-asignaciones de Pedidos______");
         
        Calendar c = Calendar.getInstance(); //OBTENGO LA FECHA DE HOY 
        Date hoy = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(hoy); 
        hoy = sdf.parse(date);
         
        //String filtroAgenda =" ID_USUARIO_VENDEDOR_REASIGNADO > 0 AND FECHA_LIMITE_REASIGANCION IS NOT NULL ";
        //extraemos a unicamente a los registros de hasta el día de hoy.
        String filtroAgenda =" ID_USUARIO_VENDEDOR_REASIGNADO > 0 AND FECHA_LIMITE_REASIGANCION <= '"+sdf.format(hoy)+"'";
        
        SgfensPedidoDaoImpl pedidosDaoImpl = new SgfensPedidoDaoImpl(this.conn);
        SgfensPedido[] pedidosDtos = pedidosDaoImpl.findByDynamicWhere(filtroAgenda, null);

        
        
        for(SgfensPedido pedido: pedidosDtos ){
            
            if(pedido.getFechaLimiteReasigancion().before(hoy)){
                
                pedido.setIdUsuarioVendedorReasignado(0);
                pedido.setFechaLimiteReasigancion(null);
                
                try{
                    new SgfensPedidoDaoImpl(this.conn).update(pedido.createPk(),pedido);                                        
                }catch(Exception e){
                    e.printStackTrace();
                }
                
                System.out.println("____Actualización de  Re-asignaciones de Pedidos correcta______");
            }
            
        }
        
     }catch(Exception e){e.printStackTrace();}
     
     //---------------------------------------- FIN RE-ASIGNACION DE PEDIDOS ------------------------>
      
     //---------------------------------------- INCIA TERMINO DE ASIGNACION DE VENDEDORES ------------------------>
     try{
         
        System.out.println("____Actualiza asignaciones de Vendedores______");
         
        Calendar c = Calendar.getInstance(); //OBTENGO LA FECHA DE HOY 
        Date hoy = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(hoy); 
        hoy = sdf.parse(date);
         
        //String filtroAgenda =" ID_USUARIO_VENDEDOR_REASIGNADO > 0 AND FECHA_LIMITE_REASIGANCION IS NOT NULL ";
        //extraemos a unicamente a los registros de hasta el día de hoy.
        String filtroasignacion =" ID_ESTATUS = 1 AND FECHA_LIMITE_ASIGANCION <= '"+sdf.format(hoy)+"'";
        
        SgfensAsignacionEmpleadosDaoImpl asignacionDaoImpl = new SgfensAsignacionEmpleadosDaoImpl(this.conn);
        SgfensAsignacionEmpleados[] asignacionDtos = asignacionDaoImpl.findByDynamicWhere(filtroasignacion, null);

        
        
        for(SgfensAsignacionEmpleados asignacionEmpleado: asignacionDtos ){
            
            if(asignacionEmpleado.getFechaLimiteAsigancion().before(hoy)){
                
                asignacionEmpleado.setIdEstatus(0);
                
                try{
                    new SgfensAsignacionEmpleadosDaoImpl(this.conn).update(asignacionEmpleado.createPk(),asignacionEmpleado);                                        
                }catch(Exception e){
                    e.printStackTrace();
                }
                
                System.out.println("____Actualización de  Asignaciones de Vendedores es correcta______");
            }
            
        }
        
     }catch(Exception e){e.printStackTrace();}
     //---------------------------------------- FIN ASIGNACION DE VENDEDORES ------------------------>
     
  }
    
}
