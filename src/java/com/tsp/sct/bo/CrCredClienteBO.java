/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.cr.CrUtilConectaSAPBafar;
import com.tsp.sct.dao.dto.CrCredCliente;
import com.tsp.sct.dao.dto.CrFormularioEvento;
import com.tsp.sct.dao.dto.CrFrmEventoSolicitud;
import com.tsp.sct.dao.jdbc.CrCredClienteDaoImpl;
import com.tsp.sct.util.GenericMethods;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author ISCesar
 */
public class CrCredClienteBO {
    private CrCredCliente crCredCliente = null;
    private String orderBy = null;

    public CrCredCliente getCrCredCliente() {
        return crCredCliente;
    }

    public void setCrCredCliente(CrCredCliente crCredCliente) {
        this.crCredCliente = crCredCliente;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CrCredClienteBO(Connection conn){
        this.conn = conn;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    
    public CrCredClienteBO(int idCrCredCliente, Connection conn){        
        this.conn = conn; 
        try{
           CrCredClienteDaoImpl CrCredClienteDaoImpl = new CrCredClienteDaoImpl(this.conn);
            this.crCredCliente = CrCredClienteDaoImpl.findByPrimaryKey(idCrCredCliente);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public CrCredCliente findCrCredClientebyId(int idCrCredCliente) throws Exception{
        CrCredCliente CrCredCliente = null;
        
        try{
            CrCredClienteDaoImpl CrCredClienteDaoImpl = new CrCredClienteDaoImpl(this.conn);
            CrCredCliente = CrCredClienteDaoImpl.findByPrimaryKey(idCrCredCliente);
            if (CrCredCliente==null){
                throw new Exception("No se encontro ningun CrCredCliente que corresponda con los parámetros específicados.");
            }
            if (CrCredCliente.getIdCredCliente()<=0){
                throw new Exception("No se encontro ningun CrCredCliente que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CrCredCliente del usuario. Error: " + e.getMessage());
        }
        
        return CrCredCliente;
    }
    
    /**
     * Realiza una búsqueda por ID CrCredCliente en busca de
     * coincidencias
     * @param idCrCredCliente ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CrCredCliente
     */
    public CrCredCliente[] findCrCredClientes(int idCrCredCliente, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CrCredCliente[] crCredClienteDto = new CrCredCliente[0];
        CrCredClienteDaoImpl crCredClienteDao = new CrCredClienteDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrCredCliente>0){
                sqlFiltro ="id_cred_cliente=" + idCrCredCliente + " AND ";
            }else{
                sqlFiltro ="id_cred_cliente>0 AND";
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
            
            crCredClienteDto = crCredClienteDao.findByDynamicWhere( 
                    sqlFiltro
                    + " " + (StringManage.getValidString(orderBy).length()>0? orderBy : "ORDER BY id_cred_cliente desc")
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return crCredClienteDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID CrCredCliente y otros filtros
     * @param idCrCredCliente ID Del CrCredCliente para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return int cantidad de registros coincidentes
     */
    public int findCantidadCrCredClientes(int idCrCredCliente, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        CrCredClienteDaoImpl crCredClienteDao = new CrCredClienteDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrCredCliente>0){
                sqlFiltro ="id_cred_cliente=" + idCrCredCliente + " AND ";
            }else{
                sqlFiltro ="id_cred_cliente>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_cred_cliente) as cantidad FROM " + crCredClienteDao.getTableName() +  " WHERE " + 
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
        
    public String getCrCredClientesByIdHTMLCombo(int idEmpresa, int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            CrCredCliente[] crCredClienteesDto = findCrCredClientes(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (CrCredCliente crCredCliente : crCredClienteesDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado == crCredCliente.getIdCredCliente())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+crCredCliente.getIdCredCliente()+"' "
                            + selectedStr
                            + "title='BP: "+crCredCliente.getIdClienteSTercero()+"'>"
                            + crCredCliente.getIdCreditoSTercero() + " - " 
                            + getNombreCompleto(crCredCliente, false)
                            +"</option>";
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return strHTMLCombo;
    } 
    
    /**
     * Obtiene el nombre completo del CredCliente
     * @param apellidosPrimero indica si van primero los apellidos y despues los nombres
     * @return Cadena con el nombre completo concatenado ordenado apellidos de acuerdo a parametro
     */
    public String getNombreCompleto(boolean apellidosPrimero){
        if (this.crCredCliente==null){
            return null;
        }
        
        return getNombreCompleto(this.crCredCliente, apellidosPrimero);
    }
    
    /**
     * Obtiene el nombre completo del CredCliente
     * @param crCredCliente Objeto CrCredCliente del cual se obtendra el nombre concatenado
     * @param apellidosPrimero indica si van primero los apellidos y despues los nombres
     * @return Cadena con el nombre completo concatenado ordenado apellidos de acuerdo a parametro
     */
    public String getNombreCompleto(CrCredCliente crCredCliente, boolean apellidosPrimero){
        String nombreCompleto = "";
        
        String nombres = StringManage.getValidString(crCredCliente.getNombrePrimer());
        nombres += (StringManage.getValidString(crCredCliente.getNombreSegundo()).length()>0 ? " " + StringManage.getValidString(crCredCliente.getNombreSegundo()) : "" );
        
        String apellidos = StringManage.getValidString(crCredCliente.getApellidoPaterno());
        apellidos += (StringManage.getValidString(crCredCliente.getApellidoMaterno()).length()>0 ? " " + StringManage.getValidString(crCredCliente.getApellidoMaterno()) : "" );
        
        if (apellidosPrimero){
            nombreCompleto = apellidos + " " + nombres;
        }else{
            nombreCompleto = nombres + " " + apellidos;
        }
        
        return nombreCompleto;
    }
    
    /**
     * Crea un registro de CrCredCliente en BD a partir de la solicitud
     * que origino el crédito.
     * @param crFormularioEvento Objeto Evento de solicitud
     * @param user Objeto usuario en sesion
     * @return Objeto CrCredCliente creado en BD
     * @throws Exception 
     */
    public CrCredCliente crearCrClienteDeSolicitudCredito(CrFormularioEvento crFormularioEvento, UsuarioBO user) throws Exception{
        crCredCliente = null;
        
        try{
            CrFormularioEventoBO crFormularioEventoBO = new CrFormularioEventoBO(conn);
            crFormularioEventoBO.setCrFormularioEvento(crFormularioEvento);
            CrFrmEventoSolicitud crFrmEventoSolicitud = crFormularioEventoBO.getFrmEventoSolicitud();
            CrCredClienteBO credClienteBO = new CrCredClienteBO(conn);
            
            // primero buscamos si el registro ya fue creado previamente por BP y No Contrato
            CrCredCliente[] coincidenciasPrevias = credClienteBO.findCrCredClientes(-1, user.getUser().getIdEmpresa(), 0, 1, 
                    " AND id_cliente_s_tercero = '" + crFrmEventoSolicitud.getSapBp() + "'"
                    + " AND id_credito_s_tercero= '" + crFrmEventoSolicitud.getSapNoContrato()+ "'");
            
            if (coincidenciasPrevias.length>0){
                crCredCliente = coincidenciasPrevias[0];
                return crCredCliente;
            }
            
            //Extraemos información de solicitud original (formularios dinamicos)
            CrUtilConectaSAPBafar crUtilConectaSAPBafar = new CrUtilConectaSAPBafar(conn);
            crUtilConectaSAPBafar.analizaSolicitud(crFormularioEvento);
            
            int idEmpresa = user.getUser().getIdEmpresa();
            Date ahora = ZonaHorariaBO.DateZonaHorariaByIdEmpresa(conn, new Date(), idEmpresa).getTime();
            
            crCredCliente = new CrCredCliente();
            crCredCliente.setIdFormularioEvento(crFormularioEvento.getIdFormularioEvento());
            crCredCliente.setIdEstatus(1);
            crCredCliente.setIdEmpresa(idEmpresa);
            crCredCliente.setFechaHrCreacion(ahora);
            //crCredCliente.setFechaHrEdicion(null);
            crCredCliente.setIdUsuarioEdicion(user.getUser().getIdUsuarios());
            crCredCliente.setRfc(crUtilConectaSAPBafar.getValorVariable("SOL_RFC", 15, false, false));
            crCredCliente.setNombrePrimer(crUtilConectaSAPBafar.getValorVariable("SOL_NOMBRE_PRIMER", 50, false, false));
            crCredCliente.setNombreSegundo(crUtilConectaSAPBafar.getValorVariable("SOL_NOMBRE_SEGUNDO", 50, false, false));
            crCredCliente.setApellidoPaterno(crUtilConectaSAPBafar.getValorVariable("SOL_APELLIDO_PAT", 50, false, false));
            crCredCliente.setApellidoMaterno(crUtilConectaSAPBafar.getValorVariable("SOL_APELLIDO_MAT", 50, false, false));
            crCredCliente.setLatitud(crFormularioEvento.getLatitud());
            crCredCliente.setLongitud(crFormularioEvento.getLongitud());
            crCredCliente.setCorreosElectronicos(crUtilConectaSAPBafar.getValorVariable("SOL_EMAIL", 200, false, false));
            crCredCliente.setIdClienteSTercero(crFrmEventoSolicitud.getSapBp());
            crCredCliente.setIdCreditoSTercero(crFrmEventoSolicitud.getSapNoContrato());
            crCredCliente.setFechaInicioCredito(crFrmEventoSolicitud.getSapFechaApertura());
            crCredCliente.setFechaFinCredito(crFrmEventoSolicitud.getSapFechaFinCredito());
            crCredCliente.setPlazoMeses(crFrmEventoSolicitud.getPlazoMeses());
            crCredCliente.setPlazoVencimiento(crFrmEventoSolicitud.getPlazoVencimiento());
            crCredCliente.setVencimiento(crFrmEventoSolicitud.getTipoVencimiento());
            crCredCliente.setMontoPrestado(crFrmEventoSolicitud.getMontoAprobado());
            // igual a aprobado por que no se ha registrado ningun pago
            crCredCliente.setMontoAdeudado(crFrmEventoSolicitud.getMontoAprobado());
            // --- Datos que se asignaran a partir de web service de consulta en SAP
            crCredCliente.setDiasMora(0);
            crCredCliente.setSaldoVencido(0);
            crCredCliente.setImportePagarVencimiento(0);
            //crCredCliente.setFechaHoraAgenda(null);
            crCredCliente.setNumPagosRealizadosVencimiento(0);
            crCredCliente.setSumaMontoPagadoVencimiento(0);
            crCredCliente.setCuotasDevengadas(0);
            crCredCliente.setCuotasPagadas(0);
            crCredCliente.setCuotasVencidas(0);
            // --- FIN Datos que se asignaran a partir de web service de consulta en SAP
                
            CrCredClienteDaoImpl crCredClienteDao = new CrCredClienteDaoImpl(conn);
            crCredClienteDao.insert(crCredCliente);
        }catch(Exception ex){
            throw new Exception("Error inesperado al crear registro de Cliente a partir de solicitud: " + GenericMethods.exceptionStackTraceToString(ex));
        }
        
        return crCredCliente;
    }
        
}
