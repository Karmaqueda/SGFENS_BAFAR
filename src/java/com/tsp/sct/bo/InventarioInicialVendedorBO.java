/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Concepto;
import com.tsp.sct.dao.dto.Empleado;
import com.tsp.sct.dao.dto.EmpleadoInventarioRepartidor;
import com.tsp.sct.dao.dto.InventarioHistoricoVendedor;
import com.tsp.sct.dao.dto.InventarioInicialVendedor;
import com.tsp.sct.dao.dto.SgfensPedido;
import com.tsp.sct.dao.dto.SgfensPedidoDevolucionCambio;
import com.tsp.sct.dao.dto.SgfensPedidoProducto;
import com.tsp.sct.dao.exceptions.InventarioInicialVendedorDaoException;
import com.tsp.sct.dao.jdbc.InventarioHistoricoVendedorDaoImpl;
import com.tsp.sct.dao.jdbc.InventarioInicialVendedorDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensPedidoProductoDaoImpl;
import com.tsp.sct.util.StringManage;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author 578
 */
public class InventarioInicialVendedorBO {
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public InventarioInicialVendedorBO(Connection conn) {
        this.conn = conn;
    }
    
    /**
     * Realiza una búsqueda por ID EmpleadoInventarioInicial en busca de
     * coincidencias     
     * @param idEmpleado ID del empleado a filtrar inventario, -1 para evitar filtro
     * @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public InventarioInicialVendedor[] findInventarioInicialByIdEmpleado(int idEmpleado, int minLimit,int maxLimit, String filtroBusqueda) {
        InventarioInicialVendedor[] empleadoInventarioInicialDto = new InventarioInicialVendedor[0];
        InventarioInicialVendedorDaoImpl empleadoInventarioInicialDao = new InventarioInicialVendedorDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            
            if (idEmpleado>0){                
                sqlFiltro += " ID_EMPLEADO = " + idEmpleado + " ";
            }else{
                sqlFiltro +=" ID_EMPLEADO>0 ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            empleadoInventarioInicialDto = empleadoInventarioInicialDao.findByDynamicWhere( 
                    sqlFiltro
                    //+ " AND CANTIDAD > 0 "
                    + " ORDER BY CANTIDAD DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return empleadoInventarioInicialDto;
    }
    
    /**
     * Realiza una búsqueda por ID EmpleadoInventarioInicial en busca del numero de
     * coincidencias     
     * @param idEmpleado ID del empleado a filtrar inventario, -1 para evitar filtro
     * @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public int findCantidadInventarioInicialByIdEmpleado(int idEmpleado, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        InventarioInicialVendedorDaoImpl empleadoInventarioInicialDao = new InventarioInicialVendedorDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            
            if (idEmpleado>0){                
                sqlFiltro += " ID_EMPLEADO = " + idEmpleado + " ";
            }else{
                sqlFiltro +=" ID_EMPLEADO>0 ";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as cantidad FROM " + empleadoInventarioInicialDao.getTableName() +  " WHERE " + 
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
    
    /**
     * Realiza una búsqueda por ID EmpleadoInventarioInicial en busca de
     * coincidencias     
     * @param idEmpleado ID del empleado a filtrar inventario, -1 para evitar filtro
     * @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @param orderBy Cadena con los ordenamientos requeridos, enviar vacio o nulo en caso de no aplicar. P. ej: CANTIDAD DESC
     * @return DTO Marca
     */
    public InventarioInicialVendedor[] findInventarioInicialByIdEmpleadoOrderBy(int idEmpleado, int minLimit,int maxLimit, String filtroBusqueda, String orderBy) {
        InventarioInicialVendedor[] empleadoInventarioInicialDto = new InventarioInicialVendedor[0];
        InventarioInicialVendedorDaoImpl empleadoInventarioInicialDao = new InventarioInicialVendedorDaoImpl(this.conn);
        try {
            String sqlQuery = "SELECT inventario_inicial_vendedor.*, concepto.NOMBRE_DESENCRIPTADO, concepto.IDENTIFICACION, concepto.FECHA_ALTA";
            String sqlFrom = "FROM inventario_inicial_vendedor, concepto";
            String sqlFiltro="inventario_inicial_vendedor.ID_CONCEPTO = concepto.ID_CONCEPTO AND";
            String sqlOrderBy = "CANTIDAD DESC";
            
            if (idEmpleado>0){                
                sqlFiltro += " ID_EMPLEADO = " + idEmpleado + " ";
            }else{
                sqlFiltro +=" ID_EMPLEADO>0 ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            if (StringManage.getValidString(orderBy).length()>0)
                sqlOrderBy = orderBy;
            
            empleadoInventarioInicialDto = empleadoInventarioInicialDao.findByDynamicSelect(
                      sqlQuery + " "
                    + sqlFrom + " "
                    + " WHERE "  + sqlFiltro
                    + " ORDER BY " + sqlOrderBy
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return empleadoInventarioInicialDto;
    }
    
    public void registraInventarioInicial(int idEmpleado, int idEmpresa) throws InventarioInicialVendedorDaoException, Exception{        
        
        EmpleadoInventarioRepartidorBO empleadoInventarioRepartidorBO  =  new EmpleadoInventarioRepartidorBO(conn);
        EmpleadoInventarioRepartidor[] inventarioActual =  empleadoInventarioRepartidorBO.findEmpleadoInventarioRepartidorsAllConceptos(-1, idEmpleado, -1, -1, "  AND ID_ESTATUS != 2  ");
        
        if(inventarioActual.length>0){                
           
            InventarioInicialVendedor InventarioInicialVendedorDto = null;
            InventarioInicialVendedorDaoImpl inventarioInicialVendedorDaoImpl = new InventarioInicialVendedorDaoImpl(conn);
            
            /////*****almacenamos una copia del inventario inicial anterior, esto con el fin de tener una copia del inventario historico del vendedor:
            InventarioInicialVendedor[] inventarioInicialVendedorCopia = new InventarioInicialVendedor[0];
            try{
                inventarioInicialVendedorCopia = inventarioInicialVendedorDaoImpl.findWhereIdEmpleadoEquals(idEmpleado);
                if(inventarioInicialVendedorCopia != null && inventarioInicialVendedorCopia.length > 0){//si existen registros los almacenamos en la tabla INVENTARIO_HISTORICO_VENDEDOR
                    InventarioHistoricoVendedor inventarioInicialCopia = null;
                    InventarioHistoricoVendedorDaoImpl historicoVendedorDaoImpl = new InventarioHistoricoVendedorDaoImpl(conn);
                    Date fechaFinalCorte = null;      
                    
                    try{
                        fechaFinalCorte = ZonaHorariaBO.DateZonaHorariaByIdEmpresa(conn, new Date(), (int)idEmpresa).getTime();
                    }catch(Exception e){
                        fechaFinalCorte = new Date();
                    }
                    
                    ////////------VARIABLES DEL MISMO JSP LLAMADO catEmpleado_CorteConceptos.jsp //debe estar identico para que cuadren los resultados,
                    //del inventario final.
                        SGPedidoBO pedidosBO = new SGPedidoBO(conn);
                        SGPedidoProductoBO partidasPedidoBO = new SGPedidoProductoBO(conn);
                        SGPedidoDevolucionesCambioBO camDevBO = new SGPedidoDevolucionesCambioBO(conn);

                        double cantidadVendida = 0;
                        double cantidadDevoluciones = 0;
                        double cantidadMerma = 0;
                        double cantidadCambios = 0;
                        double invFinal = 0;
                        double totalProdsVendidos = 0;
                        double totalProdsMerma = 0;
                        double totalProdsVendidosDinero = 0;
                        double totalProdsMermaDinero = 0;
                        String fechaInicial = "";

                        BigDecimal cantidadEntregadoPorPedidos = new BigDecimal("0.0");////
                        SgfensPedidoProducto[] sgfensPedidoProducto = null;////
                        SgfensPedidoProductoDaoImpl sgfensPedidoProductoDaoImpl = new SgfensPedidoProductoDaoImpl(conn);////                                        
                        double cantidadMismoProducto = 0;////
                        double cantidadDistitoProducto = 0;////
                    
                    ////////------
                    //Obtenemos empleado
                    Empleado empleadoDto = new Empleado();
                    try{
                        EmpleadoBO empleadoBO = new EmpleadoBO(conn);     
                        empleadoDto = empleadoBO.findEmpleadobyId(idEmpleado);
                    }catch(Exception e){} 
                    
                    for(InventarioInicialVendedor item : inventarioInicialVendedorCopia){                        
                        
                        ////////------ codigo que se encuentra en el jsp de catEmpleado_CorteConceptos.jsp, debe ser el mismo, esto para obtener el inventario final del vendedor con el que termino para tenerlo en el historico
                        //Limpiamos variables
                        cantidadVendida = 0 ;
                        cantidadDevoluciones = 0;
                        cantidadMerma = 0;
                        cantidadCambios = 0;
                        invFinal = 0;
                        fechaInicial = item.getFechaRegistro().toString();

                        cantidadEntregadoPorPedidos = new BigDecimal("0.0");////
                        sgfensPedidoProducto = null;////
                        cantidadMismoProducto = 0;////
                        cantidadDistitoProducto = 0;////

                        try{////
                            sgfensPedidoProducto = sgfensPedidoProductoDaoImpl.findWhereIdConceptoEquals(item.getIdConcepto());}catch(Exception e){}////
                        if(sgfensPedidoProducto != null){////
                            for(SgfensPedidoProducto productos : sgfensPedidoProducto){////
                                cantidadEntregadoPorPedidos = cantidadEntregadoPorPedidos.add(new BigDecimal(productos.getCantidadEntregada()));////
                            }////
                        }////



                        try{                                                    
                            Concepto concepto = new ConceptoBO(item.getIdConcepto(),conn).getConcepto();


                            //Ventas
                            //Obtenemos Pedidos del vendedor
                            System.out.println("******PEDIDOS*******");
                            String filtroPedidosVendedor = " AND ID_USUARIO_VENDEDOR ='" + empleadoDto.getIdUsuarios() + "'  AND FECHA_PEDIDO >= '" +item.getFechaRegistro() + "' AND (ID_ESTATUS_PEDIDO <> 3 AND ID_ESTATUS_PEDIDO <> 1) ";                                                                                                                                                        
                            SgfensPedido[] pedidos = pedidosBO.findPedido(-1, idEmpresa, -1, -1, filtroPedidosVendedor);

                            if(pedidos.length>0){

                                for(SgfensPedido pedido : pedidos){
                                    //Obtenemos partidas del pedido
                                    System.out.println("******PRODUCTOS*******");
                                    SgfensPedidoProducto[] partidasPedido = partidasPedidoBO.findByIdConcepto(item.getIdConcepto(), -1, -1," ID_PEDIDO="+pedido.getIdPedido() );
                                    if(partidasPedido.length>0){
                                        for(SgfensPedidoProducto partida:partidasPedido ){
                                            //Sumamos cantidades

                                            cantidadVendida += partida.getCantidadEntregada();
                                            totalProdsVendidosDinero += (partida.getCantidadEntregada()* partida.getPrecioUnitario());
                                        }
                                    }  
                                }                                                       

                            }   


                            //Obtenemos devoluciones y cambios del pedido

                            System.out.println("******DEVOLUCIONES Y CAMBIOS*******");
                            String filtroDevs  = " AND FECHA  >= '"+ item.getFechaRegistro()+"' ";
                            SgfensPedidoDevolucionCambio[] devoluciones = camDevBO.findCambioDevByEmpleado(conn, empleadoDto.getIdEmpleado(), filtroDevs);

                            if(devoluciones.length>0){
                                for(SgfensPedidoDevolucionCambio dev:devoluciones){

                                     //Precio de venta del pedido 
                                    SgfensPedidoProducto[] conceptoPedidoDto = partidasPedidoBO.findByIdPedido(dev.getIdPedido(), -1, -1, -1, " AND ID_CONCEPTO="+dev.getIdConcepto());

                                    //if(conceptoPedidoDto.length>0){

                                        if(item.getIdConcepto()==dev.getIdConcepto()){

                                            //cantidadDevoluciones += dev.getAptoParaVenta();
                                            cantidadMerma += dev.getNoAptoParaVenta();
                                            try{
                                                totalProdsMermaDinero += ( conceptoPedidoDto[0].getPrecioUnitario());
                                            }catch(Exception e){
                                                totalProdsMermaDinero += 0;
                                            }
                                            if(dev.getIdTipo() == 2){////cambio
                                                if(item.getIdConcepto() == dev.getIdConceptoEntregado()){////
                                                    cantidadMismoProducto += (dev.getAptoParaVenta() + dev.getNoAptoParaVenta());////
                                                }else{////
                                                    cantidadDistitoProducto += (dev.getAptoParaVenta() + dev.getNoAptoParaVenta());////
                                                }///
                                            }else if(dev.getIdTipo() == 1){//devolucion                                                                        
                                                   cantidadDevoluciones = cantidadDevoluciones + dev.getAptoParaVenta() + dev.getNoAptoParaVenta();                                                                       
                                            }

                                        }/*else{
                                            if(dev.getIdTipo() == 1){//devolucion                                                                        
                                                   cantidadDevoluciones = cantidadDevoluciones + dev.getAptoParaVenta() + dev.getNoAptoParaVenta();                                                                       
                                            } 
                                        }*/     

                                        if(item.getIdConcepto()==dev.getIdConceptoEntregado()){
                                            if(dev.getCantidadDevuelta()>0){
                                                cantidadCambios += dev.getCantidadDevuelta();
                                            }
                                        }
                                   // }                                                          

                                }
                            }

                            //cantidadVendida += cantidadMismoProducto;////


                            //invFinal = item.getCantidad() - cantidadVendida + cantidadDevoluciones - cantidadCambios + cantidadMerma;
                            //invFinal = item.getCantidad() - cantidadVendida + cantidadDevoluciones - cantidadCambios;
                            //invFinal = item.getCantidad() - cantidadVendida + (cantidadDevoluciones !=0? cantidadDevoluciones : cantidadMerma) - cantidadMismoProducto + cantidadDistitoProducto;
                            if(cantidadMerma != 0){
                                //invFinal = item.getCantidad() - cantidadVendida + (cantidadMerma) - cantidadCambios;
                                //////invFinal = item.getCantidad() - cantidadVendida + (cantidadMerma) - cantidadCambios;
                                //invFinal = item.getCantidad() - cantidadVendida - cantidadCambios + cantidadMismoProducto;                                                        
                                //invFinal = item.getCantidad() - cantidadVendida - cantidadCambios;
                                invFinal = item.getCantidad() - (cantidadVendida + cantidadCambios) + cantidadDevoluciones + cantidadMismoProducto + cantidadDistitoProducto;
                            }else{
                                //invFinal = item.getCantidad() - cantidadVendida - cantidadMismoProducto + cantidadDistitoProducto - cantidadCambios;
                                /////////invFinal = item.getCantidad() - cantidadVendida - cantidadCambios + cantidadMismoProducto;                                                        
                                invFinal = item.getCantidad() - (cantidadVendida + cantidadCambios) + cantidadDevoluciones + cantidadMismoProducto + cantidadDistitoProducto;
                            }

                            totalProdsVendidos += cantidadVendida + cantidadCambios;
                            totalProdsMerma += cantidadMerma;
                        }catch(Exception ex){
                                ex.printStackTrace();
                        }
                        ////////------
                        
                        inventarioInicialCopia = new InventarioHistoricoVendedor();
                        inventarioInicialCopia.setIdEmpleado(item.getIdEmpleado());
                        inventarioInicialCopia.setIdConcepto(item.getIdConcepto());
                        inventarioInicialCopia.setCantidadAsignada(item.getCantidad());
                        inventarioInicialCopia.setCantidadTerminno(invFinal);
                        try{                        
                            inventarioInicialCopia.setFechaRegistro(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(conn, new Date(), (int)idEmpresa).getTime());
                        }catch(Exception e){
                            inventarioInicialCopia.setFechaRegistro(new Date());
                        }
                        inventarioInicialCopia.setFechaInicialCorte(item.getFechaRegistro());
                        inventarioInicialCopia.setFechaFinalCorte(fechaFinalCorte);
                        historicoVendedorDaoImpl.insert(inventarioInicialCopia);
                    }
                }
            }catch(Exception e){}
            /////*****
            
            //Eliminamos partidas existentes del empleado   
            inventarioInicialVendedorDaoImpl.delete(" WHERE ID_EMPLEADO = " + idEmpleado );
            
            for(EmpleadoInventarioRepartidor item : inventarioActual){
                
                InventarioInicialVendedorDto = new InventarioInicialVendedor();
                
                try{
                    
                    //Seteamos valores
                    InventarioInicialVendedorDto.setIdEmpleado(item.getIdEmpleado());
                    InventarioInicialVendedorDto.setIdConcepto(item.getIdConcepto());
                    try{
                        InventarioInicialVendedorDto.setFechaRegistro(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(conn, new Date(), (int)idEmpresa).getTime());
                    }catch(Exception e){
                        InventarioInicialVendedorDto.setFechaRegistro(new Date());
                    }
                    if(item.getIdEstatus()==1){
                         InventarioInicialVendedorDto.setCantidad(item.getCantidad());
                    }else{
                        InventarioInicialVendedorDto.setCantidad(0);
                    }
                    InventarioInicialVendedorDto.setFechaHrUltAdicion(null);
                    InventarioInicialVendedorDto.setObservacionUltAdicion(null);
                   
                    
                    //Insertamos
                    inventarioInicialVendedorDaoImpl.insert(InventarioInicialVendedorDto);
                    
                }catch(Exception e){
                    e.printStackTrace();
                    throw new Exception("No se puede almacenar la tarea." + e.toString() ) ;
                }                
                
            }            
           //"<!--EXITO-->Inventario Inicial registrado satisfactoriamente."
        }else{
            throw new Exception("El vendedor no tiene inventario asignado actualmente." );
        }
    }
    
    public InventarioInicialVendedor adicionInventarioInicial(int idEmpleado, int idEmpresa, int idConcepto, double cantidad) throws Exception{
        InventarioInicialVendedor[] inventarioInicialVendedorBusqueda = findInventarioInicialByIdEmpleado(idEmpleado, 0, 0, " AND ID_CONCEPTO=" + idConcepto);
        InventarioInicialVendedorDaoImpl inventarioInicialVendedorDao = new InventarioInicialVendedorDaoImpl(conn);
        InventarioInicialVendedor inventarioInicialVendedor;
        
        Date ultimoCambio = null;
        try{
            ultimoCambio = ZonaHorariaBO.DateZonaHorariaByIdEmpresa(conn, new Date(), (int)idEmpresa).getTime();
        }catch(Exception e){
            ultimoCambio = new Date();
        }
        
        boolean edicion = false;
        if (inventarioInicialVendedorBusqueda.length>0){
            //si ya existe ese concepto en el inventario inicial, sumamos cantidad
            edicion = true;
            inventarioInicialVendedor = inventarioInicialVendedorBusqueda[0];
            
            inventarioInicialVendedor.setCantidad(inventarioInicialVendedor.getCantidad() + cantidad);
        }else{
            // si no existe ese concepto en el inventario inicial, creamos nuevo registro
            inventarioInicialVendedor = new InventarioInicialVendedor();
            inventarioInicialVendedor.setIdEmpleado(idEmpleado);
            inventarioInicialVendedor.setIdConcepto(idConcepto);
            inventarioInicialVendedor.setFechaRegistro(ultimoCambio);
            inventarioInicialVendedor.setCantidad(cantidad);
        }
        inventarioInicialVendedor.setFechaHrUltAdicion(ultimoCambio);
        inventarioInicialVendedor.setObservacionUltAdicion("");
        
        if (edicion){
            inventarioInicialVendedorDao.update(inventarioInicialVendedor);
        }else{
            inventarioInicialVendedorDao.insert(inventarioInicialVendedor);
        }
     
        return inventarioInicialVendedor;
    }
}
