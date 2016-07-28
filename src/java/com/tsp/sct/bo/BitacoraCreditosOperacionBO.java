/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.BitacoraCreditosOperacion;
import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.dao.dto.EmpresaPermisoAplicacion;
import com.tsp.sct.dao.dto.Usuarios;
import com.tsp.sct.dao.jdbc.BitacoraCreditosOperacionDaoImpl;
import com.tsp.sct.dao.jdbc.EmpresaDaoImpl;
import com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl;
import com.tsp.sct.util.DateManage;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author ISCesarMartinez
 */
public class BitacoraCreditosOperacionBO {
    private BitacoraCreditosOperacion bitacoraCreditosOperacion  = null;
    
    //Consumos de Consola
    public static final int CONSUMO_ACCION_VALIDAR_XML = 2;
    public static final int CONSUMO_ACCION_LOGIN = 5;
    public static final int CONSUMO_ACCION_REGISTRO_EMPLEADO = 2;
    public static final int CONSUMO_ACCION_REGISTRO_USUARIO_CONSOLA = 2;
    public static final int CONSUMO_ACCION_REGISTRO_CLIENTE = 2;
    public static final int CONSUMO_ACCION_REGISTRO_PROSPECTO = 2;
    public static final int CONSUMO_ACCION_REGISTRO_DISP_MOVIL = 1;
    
    //Consumos WS Moviles EVC
    public static final int CONSUMO_ACCION_WS_LOGIN = 2;
    public static final int CONSUMO_ACCION_WS_REGISTRO_CLIENTE = 1;
    public static final int CONSUMO_ACCION_WS_REGISTRO_PROSPECTO = 1;
    public static final int CONSUMO_ACCION_WS_REGISTRO_VISITA = 1;
    public static final int CONSUMO_ACCION_WS_REGISTRO_COTIZACION = 1;
    public static final int CONSUMO_ACCION_WS_REGISTRO_DEVOLUCION = 1;
    public static final int CONSUMO_ACCION_WS_REGISTRO_VENTA_RAPIDA = 1;
    public static final int CONSUMO_ACCION_WS_REGISTRO_PEDIDO_LEVANTAR = 1;        
    public static final int CONSUMO_ACCION_WS_REGISTRO_PEDIDO_COBRARSURTIR = 1;
    public static final int CONSUMO_ACCION_WS_REGISTRO_ENTREGA = 1;
    public static final int CONSUMO_ACCION_WS_REGISTRO_COBRANZA = 1;
    
    public static final String MSG_ACCION_LOGIN_1 = "Estimado cliente, actualmente ya no cuenta con créditos disponibles para ingresar al sistema, por lo que su cuenta será suspendida en los próximos días, por el momento puede ingresar al sistema. No pierda  sus datos registrados en la consola, recuerde que pude adquirir su paquete de créditos de operación, con su ejecutivo o comunicándose al 01 55 84889034, en MovilPyme estamos para servirle.";
    public static final String MSG_ACCION_LOGIN_2 = "Estimado cliente, actualmente ya no cuenta con créditos disponibles para ingresar al sistema, por lo que su cuenta ha sido suspendida, no pierda sus datos registrados en la consola, recuerde que pude adquirir su paquete de créditos de operación, con su ejecutivo o comunicándose al 01 55 84889034, en MovilPyme estamos para servirle.";
    

    public BitacoraCreditosOperacion getBitacoraCreditosOperacion() {
        return bitacoraCreditosOperacion;
    }

    public void setBitacoraCreditosOperacion(BitacoraCreditosOperacion bitacoraCreditosOperacion) {
        this.bitacoraCreditosOperacion = bitacoraCreditosOperacion;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public BitacoraCreditosOperacionBO(Connection conn){
        this.conn = conn;
    }
    
    public BitacoraCreditosOperacionBO(int idBitacoraCreditosOperacion, Connection conn){        
        this.conn = conn;
        try{
            BitacoraCreditosOperacionDaoImpl BitacoraCreditosOperacionDaoImpl = new BitacoraCreditosOperacionDaoImpl(this.conn);
            this.bitacoraCreditosOperacion = BitacoraCreditosOperacionDaoImpl.findByPrimaryKey(idBitacoraCreditosOperacion);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public BitacoraCreditosOperacion findById(int idBitacoraCreditosOperacion) throws Exception{
        BitacoraCreditosOperacion bitacoraCreditosOperacion = null;
        
        try{
            BitacoraCreditosOperacionDaoImpl cxcDaoImpl = new BitacoraCreditosOperacionDaoImpl(this.conn);
            bitacoraCreditosOperacion = cxcDaoImpl.findByPrimaryKey(idBitacoraCreditosOperacion);
            if (bitacoraCreditosOperacion==null){
                throw new Exception("No se encontro ningun bitacoraCreditosOperacion que corresponda según los parámetros específicados.");
            }
            if (bitacoraCreditosOperacion.getIdBitacoraCreditosOperacion()<=0){
                throw new Exception("No se encontro ningun bitacoraCreditosOperacion que corresponda según los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de BitacoraCreditosOperacion del usuario. Error: " + e.getMessage());
        }
        
        return bitacoraCreditosOperacion;
    }
    
    
    /**
     * Realiza una búsqueda por ID BitacoraCreditosOperacion en busca de
     * coincidencias
     * @param idBitacoraCreditosOperacion ID Del BitacoraCreditosOperacion para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar bitacoraCreditosOperacion, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO BitacoraCreditosOperacion
     */
    public BitacoraCreditosOperacion[] findBitacoraCreditosOperacion(int idBitacoraCreditosOperacion, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        BitacoraCreditosOperacion[] cxcDto = new BitacoraCreditosOperacion[0];
        BitacoraCreditosOperacionDaoImpl cxcDao = new BitacoraCreditosOperacionDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idBitacoraCreditosOperacion>0){
                sqlFiltro ="ID_BITACORA_CREDITOS_OPERACION=" + idBitacoraCreditosOperacion + " AND ";
            }else{
                sqlFiltro ="ID_BITACORA_CREDITOS_OPERACION>0 AND";
            }
            if (idEmpresa>0){
                //sqlFiltro +=" ID_EMPRESA=" + idEmpresa;
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
            
            cxcDto = cxcDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_BITACORA_CREDITOS_OPERACION DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return cxcDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID BitacoraCreditosOperacion y otros filtros
     * @param idBitacoraCreditosOperacion ID Del BitacoraCreditosOperacion para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar bitacoraCreditosOperacion, -1 para evitar filtro
     * @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return Cantidad de registros que coinciden
     */
    public int findCantidadBitacoraCreditosOperacion(int idBitacoraCreditosOperacion, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        try {
            String sqlFiltro;
            if (idBitacoraCreditosOperacion>0){
                sqlFiltro ="ID_BITACORA_CREDITOS_OPERACION=" + idBitacoraCreditosOperacion + " AND ";
            }else{
                sqlFiltro ="ID_BITACORA_CREDITOS_OPERACION>0 AND";
            }
            if (idEmpresa>0){
                //sqlFiltro +=" ID_EMPRESA=" + idEmpresa;
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(ID_BITACORA_CREDITOS_OPERACION) as cantidad FROM bitacora_creditos_operacion WHERE " + 
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
     * Consulta la suma por campo CANTIDAD de acuerdo a coincidencias por ID BitacoraCreditosOperacion y otros filtros
     * @param idBitacoraCreditosOperacion ID Del BitacoraCreditosOperacion para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar bitacoraCreditosOperacion, -1 para evitar filtro
     * @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return Cantidad de registros que coinciden
     */
    public int findSumaCantidadBitacoraCreditosOperacion(int idBitacoraCreditosOperacion, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int suma = 0;
        try {
            String sqlFiltro;
            if (idBitacoraCreditosOperacion>0){
                sqlFiltro ="ID_BITACORA_CREDITOS_OPERACION=" + idBitacoraCreditosOperacion + " AND ";
            }else{
                sqlFiltro ="ID_BITACORA_CREDITOS_OPERACION>0 AND";
            }
            if (idEmpresa>0){
                //sqlFiltro +=" ID_EMPRESA=" + idEmpresa;
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
            ResultSet rs = stmt.executeQuery("SELECT SUM(CANTIDAD) as suma FROM bitacora_creditos_operacion WHERE " + 
                    sqlFiltro
                    + sqlLimit);
            if(rs.next()){
                suma = rs.getInt("suma");
            }
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return suma;
    }
    
    /**
     * Registra el descuento de Creditos de Operacion
     * @param usuarioDto Requerido, es el registro del usuario que genera la acción
     * @param cantidad Requerido, Cantidad de Creditos a descontar (consumidos)
     * @param fechaHoraAccion Opcional, Fecha y Hora de accion, enviar NULL para aplicar fecha y hr actual
     * @param idCliente Opcional, ID del cliente al que aplica la acción en caso de pedidos, cobranza, etc., 0 en caso de no aplicar
     * @param idProspecto Opcional, ID del prospecto al que aplica la acción en caso de cotizaciones, etc., 0 en caso de no aplicar
     * @param montoOperacion Opcional, Monto en pesos relacionado con la acción de Descuento, 0 si no aplica
     * @param comentariosAdicionales Opcional, Comentarios adicionales del Descuento, descripcion del consumo
     * @param folioMovil Opcional, Folio Movil unico en caso de que el descuento se genere desde movil
     * @param aplicarEnMatriz Requerido, Flag para indicar si se descuenta de la empresa matriz o solo es para registrar en la bitacora (ya fueron descontados previamente)
     *          True en caso de requerir que se aplique en los creditos de la matriz, False en caso de solo requerir el registro en bitacora
     * @return BitacoraCreditosOperacion registro creado
     * @throws Exception En caso de no enviar datos requeridos válidos o en base de datos
     */
    public BitacoraCreditosOperacion registraDescuento(Usuarios usuarioDto, int cantidad, 
            Date fechaHoraAccion, int idCliente,int idProspecto, double montoOperacion,
            String comentariosAdicionales, String folioMovil, boolean aplicarEnMatriz) throws Exception{
        BitacoraCreditosOperacion bcoDto = new BitacoraCreditosOperacion();
        
        try{
            if (usuarioDto==null)
                throw new Exception("No se específico que usuario realizo la acción");
            if (cantidad<=0)
                throw new Exception("No se específico una cantidad válida de descuento o consumo.");
        
            Empresa empresaMatriz = 
                new EmpresaBO(conn).getEmpresaMatriz(usuarioDto.getIdEmpresa());
            if (empresaMatriz!=null){
                EmpresaPermisoAplicacion permiso = new EmpresaPermisoAplicacionDaoImpl(conn).findByPrimaryKey(empresaMatriz.getIdEmpresa());
                if (permiso.getTipoConsumoServicio()!=1) //Si no tiene tipo de consumo por créditos de operacion pre-pago
                    return null; // regresa sin hacer nada
            }
            
            bcoDto.setIdEmpresa(usuarioDto.getIdEmpresa());
            bcoDto.setIdEstatus(1);
            bcoDto.setTipo(2); //Consumo,Descuento
            bcoDto.setIdUserRegistra(usuarioDto.getIdUsuarios());
            bcoDto.setCantidad(cantidad);
            bcoDto.setFechaHora(fechaHoraAccion!=null ? fechaHoraAccion : new Date());
            if (idCliente>0)
                bcoDto.setIdCliente(idCliente);
            if (idProspecto>0)
                bcoDto.setIdProspecto(idProspecto);
            if (montoOperacion>0)
                bcoDto.setMontoOperacion(montoOperacion);
            if (StringManage.getValidString(comentariosAdicionales).length()>0)
                bcoDto.setComentarios(StringManage.getValidString(comentariosAdicionales));
            if (StringManage.getValidString(folioMovil).length()>0)
                bcoDto.setFolioMovil(StringManage.getValidString(folioMovil));
            
            BitacoraCreditosOperacionDaoImpl bcoDao = new BitacoraCreditosOperacionDaoImpl(conn);
            bcoDao.insert(bcoDto);
            
            if (aplicarEnMatriz && empresaMatriz!=null){
                //Si se eligio que se aplique en la empresa matriz, aplicamos el descuento
                empresaMatriz.setCreditosOperacion(empresaMatriz.getCreditosOperacion() - cantidad);
                new EmpresaDaoImpl(conn).update(empresaMatriz.createPk(), empresaMatriz);
            }
            
        }catch(Exception ex){
            throw new Exception("Error al registrar Descuento de Créditos: " + ex.toString());
        }
        
        return bcoDto;
    }
    
    /**
     * Registra el Abono de Creditos de Operacion
     * @param usuarioDto Requerido, es el registro del usuario que genera la acción
     * @param cantidad Requerido, Cantidad de Creditos a Abonar (comprados)
     * @param fechaHoraAccion Opcional, Fecha y Hora de accion, enviar NULL para aplicar fecha y hr actual
     * @param montoOperacion Opcional, Monto en pesos relacionado con la acción de Compra
     * @param comentariosAdicionales Opcional, Comentarios adicionales del Abono, descripcion de la compra
     * @param folioMovil Opcional, Folio Movil unico en caso de que el Abono se genere desde movil
     * @return BitacoraCreditosOperacion registro creado
     * @throws Exception En caso de no enviar datos requeridos válidos o en base de datos
     */
    public BitacoraCreditosOperacion registraAbono(Usuarios usuarioDto, int cantidad, 
            Date fechaHoraAccion, double montoOperacion,
            String comentariosAdicionales, String folioMovil, boolean aplicarEnMatriz) throws Exception{
        BitacoraCreditosOperacion bcoDto = new BitacoraCreditosOperacion();
        
        try{
            if (usuarioDto==null)
                throw new Exception("No se específico que usuario realizo la acción");
            if (cantidad<=0)
                throw new Exception("No se específico una cantidad válida de Abono o Compra.");
            if (montoOperacion<0)
                throw new Exception("No se específico el monto en pesos de la compra de créditos.");
        
            Empresa empresaMatriz = 
                new EmpresaBO(conn).getEmpresaMatriz(usuarioDto.getIdEmpresa());
            if (empresaMatriz!=null){
                EmpresaPermisoAplicacion permiso = new EmpresaPermisoAplicacionDaoImpl(conn).findByPrimaryKey(empresaMatriz.getIdEmpresa());
                if (permiso.getTipoConsumoServicio()!=1) //Si no tiene tipo de consumo por créditos de operacion pre-pago
                    return null; // regresa sin hacer nada
            }
            
            bcoDto.setIdEmpresa(usuarioDto.getIdEmpresa());
            bcoDto.setIdEstatus(1);
            bcoDto.setTipo(1); //Abono, Suma
            bcoDto.setIdUserRegistra(usuarioDto.getIdUsuarios());
            bcoDto.setCantidad(cantidad);
            bcoDto.setFechaHora(fechaHoraAccion!=null ? fechaHoraAccion : new Date());
            if (montoOperacion>0)
                bcoDto.setMontoOperacion(montoOperacion);
            if (StringManage.getValidString(comentariosAdicionales).length()>0)
                bcoDto.setComentarios(StringManage.getValidString(comentariosAdicionales));
            if (StringManage.getValidString(folioMovil).length()>0)
                bcoDto.setFolioMovil(StringManage.getValidString(folioMovil));
            
            BitacoraCreditosOperacionDaoImpl bcoDao = new BitacoraCreditosOperacionDaoImpl(conn);
            bcoDao.insert(bcoDto);
            
            if (aplicarEnMatriz && empresaMatriz!=null){
                //Si se eligio que se aplique en la empresa matriz, aplicamos el descuento
                empresaMatriz.setCreditosOperacion(empresaMatriz.getCreditosOperacion() + cantidad);
                new EmpresaDaoImpl(conn).update(empresaMatriz.createPk(), empresaMatriz);
            }
        }catch(Exception ex){
            throw new Exception("Error al registrar Abono de Créditos: " + ex.toString());
        }
        
        return bcoDto;
    }
    
    public int operacionLoginConsola(UsuarioBO user){
        int accion = 0; // no aplica o no hacer nada, continuar con login habitual
        try{
            Empresa empresaMatriz = 
                    new EmpresaBO(conn).getEmpresaMatriz(user.getUser().getIdEmpresa());
            if (empresaMatriz!=null){
                EmpresaPermisoAplicacion permiso = new EmpresaPermisoAplicacionDaoImpl(conn).findByPrimaryKey(empresaMatriz.getIdEmpresa());
                if (permiso.getTipoConsumoServicio()==1){ //Si tiene tipo de consumo por créditos de operacion pre-pago
                    
                    //Solo si tiene menos de 3 intentos de login sin creditos se registra
                    if (empresaMatriz.getIntentosLoginNoCreditos()<=3){
                        // Si el ultimo acceso del usuario fue en día distinto
                        // se le descuentan creditos por login
                        // Si ya hizo login el mismo día no se le descuenta
                        if (!DateManage.isOnDate(new Date(), user.getFechaAccesoAnterior())){
                            registraDescuento(user.getUser(), 
                                        CONSUMO_ACCION_LOGIN, 
                                        null, 0, 0, 0, 
                                        "Login Consola", null, true);
                        }
                    }
                    
                    //Recuperamos datos actualizados despues de posible descuento
                    empresaMatriz = new EmpresaBO(conn).getEmpresaMatriz(user.getUser().getIdEmpresa());
                    
                    //si la empresa ya no tiene creditos disponibles, 
                    // se suma uno al contador de login sin creditos
                    if (empresaMatriz.getCreditosOperacion()<=0){
                        empresaMatriz.setIntentosLoginNoCreditos(
                                empresaMatriz.getIntentosLoginNoCreditos() + 1);
                    }else{
                        // de lo contrario reseteamos el No. de intentos sin creditos
                        empresaMatriz.setIntentosLoginNoCreditos(0);
                    }
                    
                    //Actualizamos en BD datos de empresa matriz
                    try{
                        new EmpresaDaoImpl(conn).update(empresaMatriz.createPk(), empresaMatriz);
                    }catch(Exception ex){}
                    
                    //Casos especiales en caso de ya no tener creditos de operacion
                    if (empresaMatriz.getCreditosOperacion()<=0
                            && empresaMatriz.getIntentosLoginNoCreditos()<=3){
                        accion = 1; //permitir login, pero mostrar mensaje de advertencia
                    }else if (empresaMatriz.getCreditosOperacion()<=0
                            && empresaMatriz.getIntentosLoginNoCreditos()>3) {
                        accion = 2; //permitir login, mostrar advertencia, ocultar menu
                    }
                }else{
                    accion  = 0;
                }
            }else{
                accion = 0;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return accion;
    }
    
    public String getListaAccionesByIdHTMLCombo(long idEmpresa, long idSeleccionado, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            String sqlFiltro = "";
            if (idEmpresa>0){
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            }else{
                sqlFiltro +=" ID_EMPRESA>0";
            }
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            BitacoraCreditosOperacion[] registrosDto 
                    = new BitacoraCreditosOperacionDaoImpl(getConn())
                            .findByDynamicWhere(sqlFiltro 
                                    + " GROUP BY COMENTARIOS", null);
            
            for (BitacoraCreditosOperacion item : registrosDto){
                try{
                    String selectedStr="";

                    //if (idSeleccionado==itemEmpleado.getIdEmpleado())
                    //    selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+item.getComentarios()+"' "
                            + selectedStr
                            + "title='"+item.getComentarios()+"'>"
                            + item.getComentarios()
                            + "</option>";
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return strHTMLCombo;
    }
    
}
