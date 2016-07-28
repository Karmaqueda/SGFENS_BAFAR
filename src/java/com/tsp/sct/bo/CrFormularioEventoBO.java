/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.google.gson.Gson;
import com.tsp.sct.cr.CrConsultaDispersionSapBafarResponse;
import com.tsp.sct.cr.CrCreaInterlocutorSapBafarResponse;
import com.tsp.sct.cr.CrOrdenPago;
import com.tsp.sct.cr.CrTablaAmortizacionDetalle;
import com.tsp.sct.cr.CrUtilConectaSAPBafar;
import com.tsp.sct.cr.RevisionesCr;
import com.tsp.sct.dao.dto.Cliente;
import com.tsp.sct.dao.dto.CrFormularioEvento;
import com.tsp.sct.dao.dto.CrFrmEventoSolicitud;
import com.tsp.sct.dao.dto.CrGrupoFormulario;
import com.tsp.sct.dao.dto.CrSolicitudBitacora;
import com.tsp.sct.dao.jdbc.CrFormularioEventoDaoImpl;
import com.tsp.sct.dao.jdbc.CrFrmEventoSolicitudDaoImpl;
import com.tsp.sct.dao.jdbc.CrSolicitudBitacoraDaoImpl;
import com.tsp.sct.util.DateManage;
import com.tsp.sct.util.GenericMethods;
import com.tsp.sct.util.StringManage;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesar
 */
public class CrFormularioEventoBO {
    private CrFormularioEvento crFormularioEvento = null;

    public CrFormularioEvento getCrFormularioEvento() {
        return crFormularioEvento;
    }

    public void setCrFormularioEvento(CrFormularioEvento crFormularioEvento) {
        this.crFormularioEvento = crFormularioEvento;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CrFormularioEventoBO(Connection conn){
        this.conn = conn;
    }
    
     public CrFormularioEventoBO(int idCrFormularioEvento, Connection conn){        
        this.conn = conn; 
        try{
           CrFormularioEventoDaoImpl CrFormularioEventoDaoImpl = new CrFormularioEventoDaoImpl(this.conn);
            this.crFormularioEvento = CrFormularioEventoDaoImpl.findByPrimaryKey(idCrFormularioEvento);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public CrFormularioEvento findCrFormularioEventobyId(int idCrFormularioEvento) throws Exception{
        CrFormularioEvento CrFormularioEvento = null;
        
        try{
            CrFormularioEventoDaoImpl CrFormularioEventoDaoImpl = new CrFormularioEventoDaoImpl(this.conn);
            CrFormularioEvento = CrFormularioEventoDaoImpl.findByPrimaryKey(idCrFormularioEvento);
            if (CrFormularioEvento==null){
                throw new Exception("No se encontro ningun CrFormularioEvento que corresponda con los parámetros específicados.");
            }
            if (CrFormularioEvento.getIdFormularioEvento()<=0){
                throw new Exception("No se encontro ningun CrFormularioEvento que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CrFormularioEvento del usuario. Error: " + e.getMessage());
        }
        
        return CrFormularioEvento;
    }
    
    /**
     * Realiza una búsqueda por ID CrFormularioEvento en busca de
     * coincidencias
     * @param idCrFormularioEvento ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CrFormularioEvento
     */
    public CrFormularioEvento[] findCrFormularioEventos(int idCrFormularioEvento, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CrFormularioEvento[] crFormularioEventoDto = new CrFormularioEvento[0];
        CrFormularioEventoDaoImpl crFormularioEventoDao = new CrFormularioEventoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCrFormularioEvento>0){
                sqlFiltro ="id_formulario_evento=" + idCrFormularioEvento + " AND ";
            }else{
                sqlFiltro ="id_formulario_evento>0 AND";
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
            
            crFormularioEventoDto = crFormularioEventoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY fecha_hr_creacion desc"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return crFormularioEventoDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID CrFormularioEvento y otros filtros
     * @param idCrFormularioEvento ID Del CrFormularioEvento para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return int cantidad de registros coincidentes
     */
    public int findCantidadCrFormularioEventos(int idCrFormularioEvento, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        CrFormularioEventoDaoImpl crFormularioEventoDao = new CrFormularioEventoDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrFormularioEvento>0){
                sqlFiltro ="id_formulario_evento=" + idCrFormularioEvento + " AND ";
            }else{
                sqlFiltro ="id_formulario_evento>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_formulario_evento) as cantidad FROM " + crFormularioEventoDao.getTableName() +  " WHERE " + 
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
        
    public String getCrFormularioEventosByIdHTMLCombo(int idEmpresa, int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            CrFormularioEvento[] crFormularioEventosDto = findCrFormularioEventos(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (CrFormularioEvento crFormularioEvento : crFormularioEventosDto){
                try{
                    String selectedStr="";
                    
                    String nombreGrupoFormulario = "?";
                    String nombreCliente = " ? ";
                    
                    try{
                        CrGrupoFormulario crGrupoFormulario = new CrGrupoFormularioBO(crFormularioEvento.getIdGrupoFormulario(), this.conn).getCrGrupoFormulario();
                        nombreGrupoFormulario = crGrupoFormulario!=null? crGrupoFormulario.getNombre() :  "?";
                    }catch(Exception ex){}
                    try{
                        Cliente cliente = new ClienteBO(crFormularioEvento.getIdGrupoFormulario(), this.conn).getCliente();
                        nombreCliente = cliente!=null? StringManage.getValidString(cliente.getApellidoPaternoCliente()) + " " + StringManage.getValidString(cliente.getNombreCliente()) :  "?";
                    }catch(Exception ex){}

                    if (idSeleccionado == crFormularioEvento.getIdFormularioEvento())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+crFormularioEvento.getIdFormularioEvento()+"' "
                            + selectedStr
                            + "title='"+crFormularioEvento.getIdGrupoFormulario()+"'>"
                            + nombreGrupoFormulario + " - " + nombreCliente + " - " + DateManage.formatDateToNormal(crFormularioEvento.getFechaHrCreacion())
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
    * Genera un folio con los siguientes parametros:
    * 
    * letras FE, id_empresa a 3 dígitos, 1 guion, "CON" (de Consola), 1 guion, l número d folio
    * de acuerdo a fecha y hora
    * 
    * p. ejem:  FE001-CON-20130131100101111
    * 
    * @param idEmpresa
    * @param idEmpleado
    * @return
    */
    public static String generaFolioMovil(int idEmpresa){
        String folio = "";
        String empresa = StringManage.getExactString(""+idEmpresa, 3, '0', StringManage.FILL_DIRECTION_LEFT);
        
        try{Thread.sleep(100);}catch(Exception ex){}
        String folioConsecutivo = DateManage.getDateHourString();

        folio = "FE" + empresa + "-CON-" + folioConsecutivo;

        return folio;
    }
    
    public CrFrmEventoSolicitud getFrmEventoSolicitud(){
        if (this.crFormularioEvento==null)
            return null;
        return getFrmEventoSolicitud(this.crFormularioEvento.getIdFormularioEvento());
    }
    public CrFrmEventoSolicitud getFrmEventoSolicitud(int idFormularioEvento){
        CrFrmEventoSolicitud crFrmEventoSolicitud = null;
        CrFrmEventoSolicitudDaoImpl crFrmEventoSolicitudDaoImpl = new CrFrmEventoSolicitudDaoImpl(this.conn);
        
        try{
            crFrmEventoSolicitud = crFrmEventoSolicitudDaoImpl.findWhereIdFormularioEventoEquals(idFormularioEvento)[0];
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return crFrmEventoSolicitud;
    }
    
    public CrSolicitudBitacora[] getSolicitudBitacora(){
        if (this.crFormularioEvento==null)
            return null;
        return getSolicitudBitacora(this.crFormularioEvento.getIdFormularioEvento());
    }
    
    public CrSolicitudBitacora[] getSolicitudBitacora(int idFormularioEvento){
        CrSolicitudBitacora[] crSolicitudBitacoras = new CrSolicitudBitacora[0];
        CrSolicitudBitacoraDaoImpl crSolicitudBitacoraDao = new CrSolicitudBitacoraDaoImpl(this.conn);
        
        try{
            crSolicitudBitacoras = crSolicitudBitacoraDao.findWhereIdFormularioEventoEquals(idFormularioEvento);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return crSolicitudBitacoras;
    }
    
    /**
     * Para obtener el evento de bitacora cuando que corresponde al Evento indicado
     * y al estatus requerido.
     * @param idFormularioEvento
     * @param idEstatus
     * @return 
     */
    public CrSolicitudBitacora getSolicitudBitacoraPorEvento(int idFormularioEvento, int idEstatus){
        CrSolicitudBitacora crSolicitudBitacora = null;
        CrSolicitudBitacoraBO crSolicitudBitacoraBO = new CrSolicitudBitacoraBO(this.conn);
        crSolicitudBitacoraBO.setOrderBy(" ORDER BY fecha_hr_creacion DESC ");
                
        CrSolicitudBitacora[] bitacoraDeSolicitudPorFecha = crSolicitudBitacoraBO.findCrSolicitudBitacoras(-1, -1, 0, 1, " AND id_formulario_evento=" + idFormularioEvento + " AND id_estado_solicitud=" + idEstatus);
        if (bitacoraDeSolicitudPorFecha.length>0){
            crSolicitudBitacora = bitacoraDeSolicitudPorFecha[0];
        }
        
        return crSolicitudBitacora;
    }
    
    /**
     * Para obtener el evento de bitacora cuando se Levanto oficialmente la solicitud.
     * Estatus 2, por revisar
     * Para poder recuperar datos como: Fecha de solicitud
     * @param idFormularioEvento
     * @return 
     */
    public CrSolicitudBitacora getSolicitudBitacoraEventoSolicitado(int idFormularioEvento){        
        return getSolicitudBitacoraPorEvento(idFormularioEvento, 2);
    }
    
    /**
     * Para obtener el evento de bitacora cuando se Aprobado por parte
     * de Mesa de Control y tambien por Verificador.
     * Estatus 6, Aprobado
     * Para poder recuperar datos como: Fecha de Suscripcion (aprobacion)
     * @param idFormularioEvento
     * @return 
     */
    public CrSolicitudBitacora getSolicitudBitacoraEventoAprobado(int idFormularioEvento){
        return getSolicitudBitacoraPorEvento(idFormularioEvento, 6);
    }
    
    public CrCreaInterlocutorSapBafarResponse registraSolicitudEnSAP(int idCrFormularioEvento, UsuarioBO user) throws Exception{
        return registraSolicitudEnSAP(findCrFormularioEventobyId(idCrFormularioEvento), user);
    }
    
    public CrCreaInterlocutorSapBafarResponse registraSolicitudEnSAP(CrFormularioEvento crFormularioEvento, UsuarioBO user) throws Exception {
        CrCreaInterlocutorSapBafarResponse resp = null;
        if (crFormularioEvento != null) {
            CrUtilConectaSAPBafar crUtilConectaSAPBafar = new CrUtilConectaSAPBafar(this.conn);
            // recuperamos informacion de solicitud completa y enviamos a SAP
            try{
                resp = crUtilConectaSAPBafar.registraSolicitudCreditoSAP(crFormularioEvento);
            }catch(Exception ex){
                ex.printStackTrace();
            }

            if (!resp.isError()) {
                try {
                    CrFormularioEventoBO crFormularioEventoBO = new CrFormularioEventoBO(this.conn);
                    crFormularioEventoBO.setCrFormularioEvento(crFormularioEvento);
                    CrFrmEventoSolicitud crFrmEventoSolicitud = crFormularioEventoBO.getFrmEventoSolicitud();
                    CrFrmEventoSolicitudDaoImpl crFrmEventoSolicitudDao = new CrFrmEventoSolicitudDaoImpl(this.conn);

                    // Datos que provienen directamente de SAP
                    crFrmEventoSolicitud.setSapBp(resp.getBusinessPartner());
                    crFrmEventoSolicitud.setSapNoContrato(resp.getNoContrato());
                    BigDecimal sumaAmortizacionTotalPago = BigDecimal.ZERO;
                    if (resp.getCrTablaAmortizacion() != null) {
                         //Guardamos objeto de tabla amortizaicon (lista con detalles)
                        // en una cadena con formato JSON
                        Gson gson = new Gson();  
                        String tablaAmortizacionJson = gson.toJson(resp.getCrTablaAmortizacion());
                        crFrmEventoSolicitud.setSapTablaAmortizacion(tablaAmortizacionJson);
                        
                        try{
                            for (CrTablaAmortizacionDetalle tAmortDet : resp.getCrTablaAmortizacion().getListaTablaAmortizacionDetalles()){
                                sumaAmortizacionTotalPago = sumaAmortizacionTotalPago.add(tAmortDet.getTotalPago());
                            }
                            crFrmEventoSolicitud.setSapMontoTotalPagar(sumaAmortizacionTotalPago.doubleValue());
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }

                    //Datos generados por nuestro propio sistema que SAP no retorna
                    crFrmEventoSolicitud.setSapFechaApertura(resp.getFechaInicioCredito());
                    crFrmEventoSolicitud.setSapFechaAmortizacion(resp.getFechaPrimerPago());
                    crFrmEventoSolicitud.setSapFechaFinCredito(resp.getFechaFinCredito());
                    crFrmEventoSolicitud.setSapInfPlazoContrato(resp.getNoCuotasPlazo() + " " + resp.getTipoPlazo().getUnidad());
                    crFrmEventoSolicitud.setSapInfFechaCorte("5 días anteriores a la fecha de pago");
                    crFrmEventoSolicitud.setSapInfFechaPago("-");
                    crFrmEventoSolicitud.setMontoSolicitado(resp.getMontoSolicitado());
                    crFrmEventoSolicitud.setMontoAprobado(resp.getMontoAprobado());
                    crFrmEventoSolicitud.setFechaSolicitado(resp.getFechaSolicitud());
                    crFrmEventoSolicitud.setFechaAprobado(resp.getFechaSuscripcion());
                    crFrmEventoSolicitud.setPlazoMeses(resp.getPlazoMeses());
                    crFrmEventoSolicitud.setPlazoVencimiento(resp.getNoCuotasPlazo());
                    crFrmEventoSolicitud.setTipoVencimiento(resp.getTipoPlazo().name());
                    crFrmEventoSolicitud.setCuotaRegular(resp.getCuotaRegular());
                    
                    crFrmEventoSolicitudDao.update(crFrmEventoSolicitud.createPk(), crFrmEventoSolicitud);

                    try {
                        // Cambiamos de estatus
                        int idEstatusDestino = CrEstadoSolicitudBO.S_IMPRESION_LIB;// 7 = Imprimibles liberados
                        RevisionesCr revisionesCr = new RevisionesCr();
                        revisionesCr.almacenaRegistroSolicitudBitacora(this.conn, crFormularioEvento.getIdFormularioEvento(), 0, user.getUser().getIdEmpresa(), user.getUser().getIdUsuarios(), idEstatusDestino, "Interlocutor y Contrato creado en SAP exitosamente.", false);

                    } catch (Exception ex) {
                        throw new Exception("Error durante la actualización del estatus del crédito con los datos retornados por SAP (Creación BP -> Imprimibles Liberados). Detalle: " + GenericMethods.exceptionStackTraceToString(ex));
                    }
                } catch (Exception ex) {
                    throw new Exception("Error durante la actualización de la solicitud en BD con los datos retornados por SAP."
                            + " BP: " + resp.getBusinessPartner()
                            + " No. contrato: " + resp.getNoContrato()
                            + " Error: " + GenericMethods.exceptionStackTraceToString(ex));
                }
            } else {
                throw new Exception("Error en SAP durante la creación:"
                        + " Num Error: " + resp.getNumError() + " ."
                        + " Error: " + resp.getMsgError());
            }

        } else {
            throw new Exception("Solicitud no existente en BD (Formulario Evento no indicado).");
        }
        return resp;
    }
        
    
    public CrConsultaDispersionSapBafarResponse consultaDispersionEnSAPUnico(CrFormularioEvento crFormularioEvento, UsuarioBO user) throws Exception{
        
        CrConsultaDispersionSapBafarResponse resp = null;
        
        if (crFormularioEvento != null) {            
            // recuperamos informacion de SAP
            this.setCrFormularioEvento(crFormularioEvento);
            CrFrmEventoSolicitud crFrmEventoSolicitud = getFrmEventoSolicitud();
            String sapNoContrato = StringManage.getValidString(crFrmEventoSolicitud.getSapNoContrato());
            
            //invocamos servicio SAP
            CrUtilConectaSAPBafar crUtilConectaSAPBafar = new CrUtilConectaSAPBafar(this.conn);
            resp = crUtilConectaSAPBafar.consultaDispersionSAP(sapNoContrato, null, null);

            if (!resp.isError()) {
                try {
                    // Creamos registro de Orden de Pago
                    if (resp.getListaOrdenesPago().size()>0){
                        //Guardamos objeto de Orden de Pago proveniente de SAP
                        // en una cadena con formato JSON
                        CrOrdenPago crOrdenPago = resp.getListaOrdenesPago().get(0);
                        Gson gson = new Gson();  
                        String ordenPagoJson = gson.toJson(crOrdenPago);
                             
                        crFrmEventoSolicitud.setSapOrdenPago(ordenPagoJson);
                        
                        CrFrmEventoSolicitudDaoImpl crFrmEventoSolicitudDao = new CrFrmEventoSolicitudDaoImpl(this.conn);
                        crFrmEventoSolicitudDao.update(crFrmEventoSolicitud.createPk(), crFrmEventoSolicitud);
                    }
                    
                    // Creamos registro de Cliente Credito Final en BD
                    CrCredClienteBO crCredClienteBO = new CrCredClienteBO(conn);
                    crCredClienteBO.crearCrClienteDeSolicitudCredito(crFormularioEvento, user);
                    
                    try {
                        // Cambiamos de estatus
                        int idEstatusDestino = CrEstadoSolicitudBO.S_DISPERSADA;// 8 = Dispersada
                        RevisionesCr revisionesCr = new RevisionesCr();
                        revisionesCr.almacenaRegistroSolicitudBitacora(this.conn, crFormularioEvento.getIdFormularioEvento(), 0, user.getUser().getIdEmpresa(), user.getUser().getIdUsuarios(), idEstatusDestino, "Dispersion reportada como ejecutada por parte de SAP.", false);

                    } catch (Exception ex) {
                        throw new Exception("Error durante la actualización del estatus del crédito con los datos retornados por SAP. Detalle: " + GenericMethods.exceptionStackTraceToString(ex));
                    }
                    
                } catch (Exception ex) {
                    throw new Exception("Error durante la actualización de la solicitud y/o creación del cliente final en BD."
                            + " Error: " + GenericMethods.exceptionStackTraceToString(ex));
                }
            } else {
                throw new Exception("Error en SAP durante la consulta de dispersión:"
                        + " Num Error: " + resp.getNumError() + " ."
                        + " Error: " + resp.getMsgError());
            }

        } else {
            throw new Exception("Solicitud no existente en BD (Formulario Evento no indicado).");
        }
        
                
        return resp;
    }
    
}
