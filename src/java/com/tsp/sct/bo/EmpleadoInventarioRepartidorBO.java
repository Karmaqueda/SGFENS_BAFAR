package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Concepto;
import com.tsp.sct.dao.dto.EmpleadoInventarioRepartidor;
import com.tsp.sct.dao.dto.ExistenciaAlmacen;
import com.tsp.sct.dao.dto.Movimiento;
import com.tsp.sct.dao.jdbc.EmpleadoInventarioRepartidorDaoImpl;
import com.tsp.sct.dao.jdbc.ExistenciaAlmacenDaoImpl;
import com.tsp.sct.dao.jdbc.MovimientoDaoImpl;
import com.tsp.sct.util.GenericMethods;
import com.tsp.sct.util.StringManage;
import com.tsp.sgfens.sesion.FormatoMovimientosSesion;
import com.tsp.sgfens.sesion.MovimientoSesion;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author leonardo
 */
public class EmpleadoInventarioRepartidorBO {
    
    private EmpleadoInventarioRepartidor empleadoInventarioRepartidor = null;

    public EmpleadoInventarioRepartidor getEmpleadoInventarioRepartidor() {
        return empleadoInventarioRepartidor;
    }

    public void setEmpleadoInventarioRepartidor(EmpleadoInventarioRepartidor empleadoInventarioRepartidor) {
        this.empleadoInventarioRepartidor = empleadoInventarioRepartidor;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public EmpleadoInventarioRepartidorBO(Connection conn){
        this.conn = conn;
    }
       
    
    public EmpleadoInventarioRepartidorBO(int idEmpleadoInventarioRepartidor, Connection conn){
        this.conn = conn;
        try{
            EmpleadoInventarioRepartidorDaoImpl EmpleadoInventarioRepartidorDaoImpl = new EmpleadoInventarioRepartidorDaoImpl(this.conn);
            this.empleadoInventarioRepartidor = EmpleadoInventarioRepartidorDaoImpl.findByPrimaryKey(idEmpleadoInventarioRepartidor);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public EmpleadoInventarioRepartidor findEmpleadoInventarioRepartidorId(int idEmpleadoInventarioRepartidor) throws Exception{
        EmpleadoInventarioRepartidor EmpleadoInventarioRepartidor = null;
        
        try{
            EmpleadoInventarioRepartidorDaoImpl EmpleadoInventarioRepartidorDaoImpl = new EmpleadoInventarioRepartidorDaoImpl(this.conn);
            EmpleadoInventarioRepartidor = EmpleadoInventarioRepartidorDaoImpl.findByPrimaryKey(idEmpleadoInventarioRepartidor);
            if (EmpleadoInventarioRepartidor==null){
                throw new Exception("No se encontro ningun Inventario que corresponda con los parámetros específicados.");
            }
            if (EmpleadoInventarioRepartidor.getIdInventario()<=0){
                throw new Exception("No se encontro ningun Inventario que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Inventario del usuario. Error: " + e.getMessage());
        }
        
        return EmpleadoInventarioRepartidor;
    }
    
    /*public EmpleadoInventarioRepartidor getEmpleadoInventarioRepartidorGenericoByEmpresa(int idEmpresa) throws Exception{
        EmpleadoInventarioRepartidor empleadoInventarioRepartidor = null;
        
        try{
            EmpleadoInventarioRepartidorDaoImpl empleadoInventarioRepartidorDaoImpl = new EmpleadoInventarioRepartidorDaoImpl(this.conn);
            empleadoInventarioRepartidor = empleadoInventarioRepartidorDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (empleadoInventarioRepartidor==null){
                throw new Exception("La empresa no tiene creada algun EmpleadoInventarioRepartidor");
            }
        }catch(EmpleadoInventarioRepartidorDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada algun EmpleadoInventarioRepartidor");
        }
        
        return empleadoInventarioRepartidor;
    }*/
    
    /**
     * Realiza una búsqueda por ID EmpleadoInventarioRepartidor en busca de
     * coincidencias
     * @param idInventario ID Del inventario para filtrar, -1 para mostrar todos los registros
     * @param idEmpleado ID del empleado a filtrar inventario, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public EmpleadoInventarioRepartidor[] findEmpleadoInventarioRepartidors(int idEmpleadoInventarioRepartidor, int idEmpleado, int minLimit,int maxLimit, String filtroBusqueda) {
        EmpleadoInventarioRepartidor[] empleadoInventarioRepartidorDto = new EmpleadoInventarioRepartidor[0];
        EmpleadoInventarioRepartidorDaoImpl empleadoInventarioRepartidorDao = new EmpleadoInventarioRepartidorDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idEmpleadoInventarioRepartidor>0){
                sqlFiltro ="ID_INVENTARIO=" + idEmpleadoInventarioRepartidor + " AND ID_ESTATUS = 1 AND ";
            }else{
                sqlFiltro ="ID_INVENTARIO>0 AND ID_ESTATUS = 1 AND ";
            }
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
            
            empleadoInventarioRepartidorDto = empleadoInventarioRepartidorDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_INVENTARIO ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return empleadoInventarioRepartidorDto;
    }
    
    /**
     * Realiza una búsqueda por ID EmpleadoInventarioRepartidor en busca de la cantidad de
     * coincidencias
     * @param idEmpleadoInventarioRepartidor ID Del inventario para filtrar, -1 para mostrar todos los registros
     * @param idEmpleado ID del empleado a filtrar inventario, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public int findCantidadEmpleadoInventarioRepartidors(int idEmpleadoInventarioRepartidor, int idEmpleado, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        EmpleadoInventarioRepartidorDaoImpl empleadoInventarioRepartidorDao = new EmpleadoInventarioRepartidorDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idEmpleadoInventarioRepartidor>0){
                sqlFiltro ="ID_INVENTARIO=" + idEmpleadoInventarioRepartidor + " AND ID_ESTATUS = 1 AND ";
            }else{
                sqlFiltro ="ID_INVENTARIO>0 AND ID_ESTATUS = 1 AND ";
            }
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(ID_INVENTARIO) as cantidad FROM " + empleadoInventarioRepartidorDao.getTableName() +  " WHERE " + 
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
     * Realiza una búsqueda por ID EmpleadoInventarioRepartidor en busca de
     * coincidencias
     * @param idEmpleadoInventarioRepartidor ID Del inventario para filtrar, -1 para mostrar todos los registros
     * @param idEmpleado ID del empleado a filtrar inventario, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @param orderBy Cadena con los ordenamientos requeridos, enviar vacio o nulo en caso de no aplicar. P. ej: CANTIDAD DESC
     * @return DTO Marca
     */
    public EmpleadoInventarioRepartidor[] findEmpleadoInventarioRepartidorsOrderBy(int idEmpleadoInventarioRepartidor, int idEmpleado, int minLimit,int maxLimit, String filtroBusqueda, String orderBy) {
        EmpleadoInventarioRepartidor[] empleadoInventarioRepartidorDto = new EmpleadoInventarioRepartidor[0];
        EmpleadoInventarioRepartidorDaoImpl empleadoInventarioRepartidorDao = new EmpleadoInventarioRepartidorDaoImpl(this.conn);
        try {
            String sqlQuery = "SELECT empleado_inventario_repartidor.*, concepto.NOMBRE_DESENCRIPTADO, concepto.IDENTIFICACION, concepto.FECHA_ALTA";
            String sqlFrom = "FROM empleado_inventario_repartidor, concepto";
            String sqlFiltro="empleado_inventario_repartidor.ID_CONCEPTO = concepto.ID_CONCEPTO AND";
            String sqlOrderBy = "ID_INVENTARIO ASC";
            
            if (idEmpleadoInventarioRepartidor>0){
                sqlFiltro +=" ID_INVENTARIO=" + idEmpleadoInventarioRepartidor + " AND empleado_inventario_repartidor.ID_ESTATUS = 1 AND ";
            }else{
                sqlFiltro +=" ID_INVENTARIO>0 AND empleado_inventario_repartidor.ID_ESTATUS = 1 AND ";
            }
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
            
            empleadoInventarioRepartidorDto = empleadoInventarioRepartidorDao.findByDynamicSelect(
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
        
        return empleadoInventarioRepartidorDto;
    }
    
    /**
     * Realiza una búsqueda por ID EmpleadoInventarioRepartidor en busca de
     * coincidencias busca activos e inactivos
     * @param idInventario ID Del inventario para filtrar, -1 para mostrar todos los registros
     * @param idEmpleado ID del empleado a filtrar inventario, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public EmpleadoInventarioRepartidor[] findEmpleadoInventarioRepartidorsAllConceptos(int idEmpleadoInventarioRepartidor, int idEmpleado, int minLimit,int maxLimit, String filtroBusqueda) {
        EmpleadoInventarioRepartidor[] empleadoInventarioRepartidorDto = new EmpleadoInventarioRepartidor[0];
        EmpleadoInventarioRepartidorDaoImpl empleadoInventarioRepartidorDao = new EmpleadoInventarioRepartidorDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idEmpleadoInventarioRepartidor>0){
                sqlFiltro ="ID_INVENTARIO=" + idEmpleadoInventarioRepartidor + "  AND ";
            }else{
                sqlFiltro ="ID_INVENTARIO>0  AND ";
            }
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
            
            empleadoInventarioRepartidorDto = empleadoInventarioRepartidorDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_INVENTARIO ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return empleadoInventarioRepartidorDto;
    }
    
    
    public void asignarDevolverInventarioRepartidor(int idEmpresa, int idEmpleado, FormatoMovimientosSesion formatoMovimientoSesion, boolean activarInventarioInicial) throws Exception {
        String msgError = "";
        
        ExistenciaAlmacenBO existenciaAlmacenBO = new ExistenciaAlmacenBO(conn);
        ConceptoBO conceptoBO = new ConceptoBO(conn);
        EmpleadoInventarioRepartidorDaoImpl empleadoInventarioRepartidorDao = new EmpleadoInventarioRepartidorDaoImpl(conn);
        
        int idAlmacen = 0;
        try { 
            idAlmacen = formatoMovimientoSesion.getAlmacen().getIdAlmacen(); 
        }catch(Exception ex){
            msgError += "<ul>El Almacen no se específico en el objeto de Sesion.";
        }
        
        if (formatoMovimientoSesion.getTipoMovimiento()==FormatoMovimientosSesion.TIPO_MOV_EMP_CARGA){
            //validacion de existencias en almacen para CARGA (Asignación a Empleado, SALIDA de Almacen)
            
            for (MovimientoSesion movSesion : formatoMovimientoSesion.getListaMovimiento()){
                try{
                    ExistenciaAlmacen existenciaAlmacen  = existenciaAlmacenBO.getExistenciaProductoAlmacen(idAlmacen, movSesion.getIdProducto());
                    Concepto conceptoDto = conceptoBO.findConceptobyId(movSesion.getIdProducto());

                    if(existenciaAlmacen==null){ 
                        //no hay relacion de almacen - concepto
                        msgError += "<ul>El almacen '" + formatoMovimientoSesion.getAlmacen().getNombre() + "' no tiene existencia del producto '" + conceptoDto.getNombreDesencriptado() + "'.";
                    }else if (existenciaAlmacen.getExistencia() < movSesion.getCantidad()){
                        //no hay existencia suficiente en almacen para salida
                        msgError += "<ul>El almacen '" + formatoMovimientoSesion.getAlmacen().getNombre() + "' no tiene existencia suficiente del producto '" + conceptoDto.getNombreDesencriptado() + "'. "
                                + " Existencia en almacen: " + existenciaAlmacen.getExistencia();
                    }

                }catch(Exception e){                    
                    msgError += "No se pudo consultar un registro. " + GenericMethods.exceptionStackTraceToString(e);
                    e.printStackTrace();
                }
            }
            
        }else{
            //validacion de existencias Inventario de Empleado para DEVOLUCION (Retorno desde Empleado, ENTRADA a Almacen)
            
            for (MovimientoSesion movSesion : formatoMovimientoSesion.getListaMovimiento()){
                try{
                    Concepto conceptoDto = conceptoBO.findConceptobyId(movSesion.getIdProducto());
                    EmpleadoInventarioRepartidor[] empleadoInventarios = empleadoInventarioRepartidorDao.findByDynamicWhere("ID_EMPLEADO = "+idEmpleado+" AND ID_CONCEPTO = "+conceptoDto.getIdConcepto(), null);
                    EmpleadoInventarioRepartidor empleadoInventario = null;
                    if (empleadoInventarios.length>0){
                        empleadoInventario = empleadoInventarios[0];
                    }

                    if(empleadoInventario==null){ 
                        //no hay relacion de empleado - concepto
                        msgError += "<ul>El Empleado no tiene existencia del producto '" + conceptoDto.getNombreDesencriptado() + "', por lo cual no puede hacer devoluciones a almacen.";
                    }else if (empleadoInventario.getCantidad() < movSesion.getCantidad()){
                        //no hay existencia suficiente en empleado para devolucion
                        msgError += "<ul>El Empleado no tiene existencia suficiente del producto '" + conceptoDto.getNombreDesencriptado() + "'. "
                                + " Existencia actual en stock Empleado: " + empleadoInventario.getCantidad();
                    }

                }catch(Exception e){                    
                    msgError += "No se pudo consultar un registro. " + GenericMethods.exceptionStackTraceToString(e);
                    e.printStackTrace();
                }
            }
            
        }
            
        //procedemos a guardar si no hubo error
        if (msgError.equals("")){

            MovimientoDaoImpl movimientosDao = new MovimientoDaoImpl(conn);
            ExistenciaAlmacenDaoImpl existenciaAlmacenDao = new ExistenciaAlmacenDaoImpl(conn);

            Date fechaRegistroMovimientos = new Date();

            for (MovimientoSesion movSesion : formatoMovimientoSesion.getListaMovimiento()){
                try{
                    Concepto conceptoDto = conceptoBO.findConceptobyId(movSesion.getIdProducto());
                    
                    // 1 - Creamos o Actualizamos el registro de relacion Empleado-Concepto (inventario empleado)
                    {
                        EmpleadoInventarioRepartidor[] empleadoInventarios = empleadoInventarioRepartidorDao.findByDynamicWhere("ID_EMPLEADO = "+idEmpleado+" AND ID_CONCEPTO = "+conceptoDto.getIdConcepto(), null);
                        EmpleadoInventarioRepartidor empleadoInventario;
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
                        if (formatoMovimientoSesion.getTipoMovimiento()==FormatoMovimientosSesion.TIPO_MOV_EMP_CARGA){
                            //SUMA a inventario personal de Empleado
                            empleadoInventario.setCantidad(empleadoInventario.getCantidad() + movSesion.getCantidad());
                        }else{ //devolucion o merma
                            //RESTA a inventario personal de Empleado
                            empleadoInventario.setCantidad(empleadoInventario.getCantidad() - movSesion.getCantidad());
                        }
                        empleadoInventario.setIdEstatus(1);
                        //empleadoInventario.setPeso(movSesion.getPeso());
                        empleadoInventario.setExistenciaGranel(empleadoInventario.getCantidad() * empleadoInventario.getPeso());

                        if (!edicion){
                            empleadoInventarioRepartidorDao.insert(empleadoInventario);
                        }else{
                            empleadoInventarioRepartidorDao.update(empleadoInventario.createPk(), empleadoInventario);
                        }
                    }

                    // 2 - Creamos movimiento de almacen
                    {
                        String tipoMovimiento = "";
                        String conceptoMovimiento = "";
                        if (formatoMovimientoSesion.getTipoMovimiento()==FormatoMovimientosSesion.TIPO_MOV_EMP_CARGA){
                            tipoMovimiento = "SALIDA";
                            conceptoMovimiento = "Asignación de Producto a Vendedor";
                        }else if (formatoMovimientoSesion.getTipoMovimiento()==FormatoMovimientosSesion.TIPO_MOV_EMP_DEVOLUCION){
                            tipoMovimiento = "ENTRADA";
                            conceptoMovimiento = "Retorno de Producto desde Vendedor";
                        }else if (formatoMovimientoSesion.getTipoMovimiento()==FormatoMovimientosSesion.TIPO_MOV_EMP_MERMA){
                            tipoMovimiento = "ENTRADA";
                            conceptoMovimiento = "MERMA desde Vendedor";
                        }
                        Movimiento movimientoDto = new Movimiento(); 
                        movimientoDto.setIdEmpresa(idEmpresa);
                        movimientoDto.setTipoMovimiento(tipoMovimiento);
                        movimientoDto.setNombreProducto(conceptoDto.getNombreDesencriptado());
                        movimientoDto.setContabilidad(movSesion.getCantidad());
                        movimientoDto.setIdProveedor(-1);
                        movimientoDto.setOrdenCompra("");
                        movimientoDto.setNumeroGuia("");                             
                        movimientoDto.setIdAlmacen(idAlmacen);                
                        movimientoDto.setConceptoMovimiento(conceptoMovimiento);                
                        movimientoDto.setFechaRegistro(fechaRegistroMovimientos);
                        movimientoDto.setIdConcepto(conceptoDto.getIdConcepto());
                        movimientoDto.setIdEmpleado(idEmpleado);                                    
                            //Insertamos registro
                        movimientosDao.insert(movimientoDto);
                    }

                    // 3 - Actualizamos existencia en Almacen
                    {
                        ExistenciaAlmacen existenciaAlmacen  = existenciaAlmacenBO.getExistenciaProductoAlmacen(idAlmacen, movSesion.getIdProducto()); 
                        boolean edicion = false;
                        if (existenciaAlmacen!=null){
                            //Ya existe relacion almacen-concepto
                            edicion = true;
                        }else{
                            //No existe relacion almacen-concepto, creamos nueva
                            existenciaAlmacen = new ExistenciaAlmacen();
                            existenciaAlmacen.setIdAlmacen(idAlmacen);
                            existenciaAlmacen.setIdConcepto(conceptoDto.getIdConcepto());
                            existenciaAlmacen.setExistencia(0);
                            existenciaAlmacen.setEstatus(1);
                            existenciaAlmacen.setExistenciaPeso(0);
                        }
                        BigDecimal numArticulosDisponibles = (new BigDecimal(existenciaAlmacen.getExistencia())).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal contaMovimiento = (new BigDecimal(movSesion.getCantidad())).setScale(2, RoundingMode.HALF_UP);
                        
                        BigDecimal nuevoStockBigD = BigDecimal.ZERO;
                        if (formatoMovimientoSesion.getTipoMovimiento()==FormatoMovimientosSesion.TIPO_MOV_EMP_CARGA){
                            //restamos a stock
                            nuevoStockBigD = numArticulosDisponibles.subtract(contaMovimiento);
                        }else if (formatoMovimientoSesion.getTipoMovimiento()==FormatoMovimientosSesion.TIPO_MOV_EMP_DEVOLUCION){
                            //sumamos a stock
                            nuevoStockBigD = numArticulosDisponibles.add(contaMovimiento);
                        }else if (formatoMovimientoSesion.getTipoMovimiento()==FormatoMovimientosSesion.TIPO_MOV_EMP_MERMA){
                            //sumamos a merma
                            throw new Exception("Tipo de Movimiento MERMA no implementado.");
                        }
                        
                        double nuevoStock = nuevoStockBigD.doubleValue();
                        existenciaAlmacen.setExistencia(nuevoStock);
                        
                        if (!edicion){
                            existenciaAlmacenDao.insert(existenciaAlmacen);
                        }else{
                            existenciaAlmacenBO.updateBD(existenciaAlmacen);
                        }
                    }

                }catch(Exception ex){
                    msgError += "Error inesperado al asignar Producto con ID " + movSesion.getIdProducto() + ", codigo barras : '"+StringManage.getValidString(movSesion.getCodigoBarras())+"': " + GenericMethods.exceptionStackTraceToString(ex);
                    ex.printStackTrace();
                }
            }
            
            if (msgError.equals("") && activarInventarioInicial){
                //Activación de Registro Inventario Inicial
                InventarioInicialVendedorBO inventarioInicialVendedorBO = new InventarioInicialVendedorBO(conn);
                try{
                    inventarioInicialVendedorBO.registraInventarioInicial(idEmpleado, idEmpresa);
                }catch(Exception ex){
                    msgError += "Error al Registrar Inventario Inicial: " + GenericMethods.exceptionStackTraceToString(ex);
                    ex.printStackTrace();
                }
            }
        }
        
        if (!msgError.equals(""))
            throw new Exception(msgError);
        
    }
    
}
