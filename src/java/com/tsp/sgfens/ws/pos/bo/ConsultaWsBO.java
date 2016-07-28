/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.pos.bo;

import com.tsp.sct.bo.BitacoraCreditosOperacionBO;
import com.tsp.sct.bo.ClienteBO;
import com.tsp.sct.bo.ConceptoBO;
import com.tsp.sct.bo.EmpleadoBO;
import com.tsp.sct.bo.EmpleadoInventarioRepartidorBO;
import com.tsp.sct.bo.EmpresaBO;
import com.tsp.sct.bo.ExistenciaAlmacenBO;
import com.tsp.sct.bo.MarcaBO;
import com.tsp.sct.bo.SGClienteVendedorBO;
import com.tsp.sct.dao.dto.*;
import com.tsp.sct.dao.jdbc.*;
import com.tsp.sct.util.DateManage;
import com.tsp.sct.util.Encrypter;
import com.tsp.sct.util.FileManage;
import com.tsp.sct.util.StringManage;
import com.tsp.sgfens.ws.pos.request.*;
import com.tsp.sgfens.ws.pos.response.*;
import com.tsp.sgfens.ws.response.WsItemCliente;
import com.tsp.sgfens.ws.response.WsItemConcepto;
import com.tsp.sgfens.ws.response.WsItemEmpleado;
import com.tsp.sgfens.ws.response.WsItemSucursal;
import com.tsp.sgfens.ws.response.WsItemUbicacionFiscal;
import com.tsp.sgfens.ws.response.WsListaClientes;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ISCesar poseidon24@hotmail.com
 * @since 5/06/2015 5/06/2015 10:32:14 AM
 */
public class ConsultaWsBO {

    private Connection conn = null;

    public Connection getConn() {
        if (this.conn==null){
            try {
                this.conn= ResourceManager.getConnection();
            } catch (SQLException ex) {}
        }else{
            try {
                if (this.conn.isClosed()){
                    this.conn = ResourceManager.getConnection();
                }
            } catch (SQLException ex) {}
        }
        return this.conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    /**
     * Verifica si los datos de acceso del empleado son válidos
     * @param empleadoDtoRequest
     * @return AccionCatalogoResponse Respuesta compuesta por objeto complejo
     */
    public LoginEmpleadoResponse loginByEmpleado(PosEmpleadoDtoRequest empleadoDtoRequest) {
        LoginEmpleadoResponse response = new LoginEmpleadoResponse();
         
        try {
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {  
                Empleado empleadoDto = empleadoBO.getEmpleado();
                WsItemEmpleado wsItemEmpleado= new WsItemEmpleado();
                wsItemEmpleado.setApellidoMaterno(empleadoDto.getApellidoMaterno());
                wsItemEmpleado.setApellidoPaterno(empleadoDto.getApellidoPaterno());
                wsItemEmpleado.setCorreoElectronico(empleadoDto.getCorreoElectronico());
                //wsItemEmpleado.setIdDispositivo(empleadoDto.getIdDispositivo());
                wsItemEmpleado.setIdEmpleado(empleadoDto.getIdEmpleado());
                wsItemEmpleado.setIdEmpresa(empleadoDto.getIdEmpresa());
                wsItemEmpleado.setIdEstatus(empleadoDto.getIdEstatus());
                wsItemEmpleado.setIdSucursal(empleadoDto.getIdSucursal());
                wsItemEmpleado.setLatitud(empleadoDto.getLatitud());
                wsItemEmpleado.setLongitud(empleadoDto.getLongitud());
                wsItemEmpleado.setNombre(empleadoDto.getNombre());
                wsItemEmpleado.setNumEmpleado(empleadoDto.getNumEmpleado());
                wsItemEmpleado.setTelefonoLocal(empleadoDto.getTelefonoLocal());
                wsItemEmpleado.setIdMovilEmpleadoRol(empleadoDto.getIdMovilEmpleadoRol());
                wsItemEmpleado.setPermisoVentaRapida(empleadoDto.getPermisoVentaRapida());
                wsItemEmpleado.setPorcentajeComision(empleadoDto.getPorcentajeComision());
                response.setWsItemEmpleado(wsItemEmpleado);

                //recuperamos datos de la empresa a la q pertenece el empleado
                EmpresaBO empresaBO = new EmpresaBO(getConn());
                Empresa empresaDto = empresaBO.findEmpresabyId(empleadoDto.getIdEmpresa());

                WsItemSucursal wsItemSucursal = new WsItemSucursal();
                WsItemUbicacionFiscal wsItemUbicacionFiscal = null;
                Ubicacion wsUbicacion;

                wsItemSucursal.setIdEmpresa((int)empresaDto.getIdEmpresa());
                wsItemSucursal.setIdEmpresaPadre((int)empresaDto.getIdEmpresaPadre());
                wsItemSucursal.setIdTipoEmpresa((int)empresaDto.getIdTipoEmpresa());
                wsItemSucursal.setRfc(empresaDto.getRfc());
                wsItemSucursal.setRazonSocial(empresaDto.getRazonSocial());
                wsItemSucursal.setNombreComercial(empresaDto.getNombreComercial()); 
                //wsItemSucursal.setLatitud(empresaDto.getLatitud());
                //wsItemSucursal.setLongitud(empresaDto.getLongitud());
                wsItemSucursal.setFoliosDisponibles(empresaDto.getFoliosDisponibles());
                wsItemSucursal.setRegimenFiscal(empresaDto.getRegimenFiscal());
                wsItemSucursal.setMensajePersonalizadoVisita(empresaDto.getMensajePersonalizadoVisita());
                wsItemSucursal.setPrintNombreComercial(empresaDto.getPrintNombreComercial());
                wsItemSucursal.setPrintRazonSocial(empresaDto.getPrintRazonSocial());
                wsItemSucursal.setPrintTkProds(empresaDto.getPrintTkProds());
                //Facturacion y Cobro tarjeta 
                try{
                    EmpresaPermisoAplicacionDaoImpl aplicacionDaoImpl = new EmpresaPermisoAplicacionDaoImpl(getConn());
                    EmpresaPermisoAplicacion permisosEmpresa = aplicacionDaoImpl.findByPrimaryKey(empleadoDto.getIdEmpresa());
                    if(permisosEmpresa.getAccesoSgfensFacturacion() == 1){
                        wsItemSucursal.setFactura_activa(1);
                    }else{
                        wsItemSucursal.setFactura_activa(2);
                    }
                    if(permisosEmpresa.getAccesoSgfensCobrotarjeta()== 1){
                        wsItemSucursal.setCobro_tarjeta_activa(1);
                    }else{
                        wsItemSucursal.setCobro_tarjeta_activa(2);
                    }
                    wsItemSucursal.setTipoConsumoServicio(permisosEmpresa.getTipoConsumoServicio());
                    wsItemSucursal.setRfcPorNipCodigo(permisosEmpresa.getRfcPorNipCodigo());
                }catch(Exception ex){
                    wsItemSucursal.setFactura_activa(1);
                    wsItemSucursal.setCobro_tarjeta_activa(1);
                }

                //cargamos la ubicacion fiscal de esa sucursal                            
                wsUbicacion = new UbicacionDaoImpl(getConn()).findByPrimaryKey(empresaDto.getIdUbicacionFiscal());

                if (wsUbicacion!=null){
                    wsItemUbicacionFiscal = new WsItemUbicacionFiscal();

                    wsItemUbicacionFiscal.setCalle(wsUbicacion.getCalle());
                    wsItemUbicacionFiscal.setCodigoPostal(wsUbicacion.getCodigoPostal());
                    wsItemUbicacionFiscal.setColonia(wsUbicacion.getColonia());
                    wsItemUbicacionFiscal.setEstado(wsUbicacion.getEstado());
                    wsItemUbicacionFiscal.setIdUbicacionFiscal(wsUbicacion.getIdUbicacion());
                    wsItemUbicacionFiscal.setMunicipio(wsUbicacion.getMunicipio());
                    wsItemUbicacionFiscal.setNumExt(wsUbicacion.getNumExt());
                    wsItemUbicacionFiscal.setNumInt(wsUbicacion.getNumInt());
                    wsItemUbicacionFiscal.setPais(wsUbicacion.getPais());
                }

                wsItemSucursal.setUbicacionFiscal(wsItemUbicacionFiscal);

                //Consumo de Creditos Operacion
                try{
                    // Si el ultimo acceso del usuario fue en día distinto
                    // se le descuentan creditos por login
                    // Si ya hizo login el mismo día no se le descuenta
                    if (!DateManage.isOnDate(new Date(), empleadoBO.getUsuarioBO().getFechaAccesoAnterior())){
                        BitacoraCreditosOperacionBO bcoBO = new BitacoraCreditosOperacionBO(getConn());
                        bcoBO.registraDescuento(empleadoBO.getUsuarioBO().getUser(), 
                                BitacoraCreditosOperacionBO.CONSUMO_ACCION_WS_LOGIN, 
                                null, 0, 0, 0, 
                                "Login WS Punto de Venta", null, true);
                    }
                    Empresa empresaMatriz = empresaBO.getEmpresaMatriz(empleadoDto.getIdEmpresa());
                    wsItemSucursal.setCreditosOperacionDisponibles(empresaMatriz.getCreditosOperacion());
                }catch(Exception ex){
                    ex.printStackTrace();
                }

                response.setWsItemSucursal(wsItemSucursal);

                response.setError(false);
            } else {
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("Datos de acceso inválidos.");
            }
        } catch (Exception e) {
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error al verificar login del empleado: " + e.toString());
            e.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
         
        return response;
    }

    public ConsultaClientesResponse getCatalogoClientesByEmpleado(PosEmpleadoDtoRequest posEmpleadoDtoRequest) {
        int idEmpresa;
        ConsultaClientesResponse consultaResponse = new ConsultaClientesResponse();
        try {
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(posEmpleadoDtoRequest.getEmpleadoUsuario(), posEmpleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                //Si se encontro el registro buscamos su catalogo de clientes
                if (idEmpresa > 0) {
                    consultaResponse.setError(false);
                    //Filtramos con Estatus Activo y que esten asignados a este empleado
                    consultaResponse.setListaClientes(this.getListaClientes(idEmpresa, 
                            " AND (ID_ESTATUS = 1 OR ID_ESTATUS = 3)"));
                }
                
            } else {
                consultaResponse.setError(true);
                consultaResponse.setNumError(901);
                consultaResponse.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        } catch (Exception e) {
            consultaResponse.setError(true);
            consultaResponse.setNumError(902);
            consultaResponse.setMsgError("Error inesperado al consultar catalogos del usuario. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }

        return consultaResponse;
    }    
    
    /**
     * Obtiene el catalogo de Conceptos de una empresa, haciendo login por empleado
     * @param empleadoDtoRequest Datos de autenticacion
     */
    public ConsultaConceptosResponse getCatalogoConceptosByEmpleado(PosEmpleadoDtoRequest posEmpleadoDtoRequest) {
        int idEmpresa;
        int idEmpleado;
        ConsultaConceptosResponse consultaConceptosResponse = new ConsultaConceptosResponse();
        try {
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(posEmpleadoDtoRequest.getEmpleadoUsuario(), posEmpleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                //Si se encontro el registro buscamos su catalogo de productos
                if (idEmpresa > 0) {
                    consultaConceptosResponse.setError(false);
                    consultaConceptosResponse.setListaConceptos(this.getListaConceptos(idEmpresa, idEmpleado));
                }
                
            } else {
                consultaConceptosResponse.setError(true);
                consultaConceptosResponse.setNumError(901);
                consultaConceptosResponse.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        } catch (Exception e) {
            consultaConceptosResponse.setError(true);
            consultaConceptosResponse.setNumError(902);
            consultaConceptosResponse.setMsgError("Error inesperado al consultar catalogos del usuario. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }

        return consultaConceptosResponse;
    }
    
    /**
     * Consulta los recursos digitales de un producto
     * tales como su imagen o video asociados.
     */
    public ConsultaConceptosResponse getMediaByConcepto(PosEmpleadoDtoRequest posEmpleadoDtoRequest, ConceptoDtoRequest conceptoDtoRequest){
        int idEmpresa;
        ConsultaConceptosResponse consultaConceptosResponse = new ConsultaConceptosResponse();
        try{
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(posEmpleadoDtoRequest.getEmpleadoUsuario(), posEmpleadoDtoRequest.getEmpleadoPassword())) {
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                //Si se encontro el registro buscamos su catalogo de productos
                if (idEmpresa > 0) {
                    consultaConceptosResponse.setError(false);
                    ConceptoBO conceptoBO = new ConceptoBO(getConn());
                    Concepto concepto = conceptoBO.findConceptobyId(conceptoDtoRequest.getIdConcepto());
                    WsItemConcepto itemConcepto = new WsItemConcepto();
                    ArrayList<WsItemConcepto> list = new ArrayList<WsItemConcepto>();
                    itemConcepto.setIdConcepto(concepto.getIdConcepto());
                    if(concepto.getRutaImagen() != null){
                        if(!concepto.getRutaImagen().trim().equals("")){
                            try{
                                itemConcepto.setImagen(FileManage.getBytesFromFile(new File(concepto.getRutaImagen())));
                            }catch(Exception ex){
                                consultaConceptosResponse.setError(true);
                                consultaConceptosResponse.setNumError(902);
                                consultaConceptosResponse.setMsgError("Error al convertir la imagen. " + ex.getLocalizedMessage());
                            }
                        }
                    }
                    if(concepto.getRutaVideo() != null){
                        if(!concepto.getRutaVideo().trim().equals("")){
                            try{
                                itemConcepto.setVideo(FileManage.getBytesFromFile(new File(concepto.getRutaVideo())));
                            }catch(Exception ex){
                                consultaConceptosResponse.setError(true);
                                consultaConceptosResponse.setNumError(902);
                                consultaConceptosResponse.setMsgError("Error al convertir el video. " + ex.getLocalizedMessage());
                            }
                        }
                    }
                    
                    list.add(itemConcepto);
                    consultaConceptosResponse.setListaConceptos(list);
                }
                
            }else{
                consultaConceptosResponse.setError(true);
                consultaConceptosResponse.setNumError(901);
                consultaConceptosResponse.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        }catch(Exception ex){
            consultaConceptosResponse.setError(true);
            consultaConceptosResponse.setNumError(902);
            consultaConceptosResponse.setMsgError("Error inesperado al consultar Recursos de concepto. " + ex.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return consultaConceptosResponse;
    }


    /**
     * Consulta los Clientes de una empresa.
     * @param idEmpresa
     * @return 
     */
    private ArrayList<WsItemCliente> getListaClientes(int idEmpresa, String filtroBusqueda){//int idEmpresa) {
        WsListaClientes listaClientes = new WsListaClientes();
        //Buscamos los clientes definidos para el usuario/empresa
        ClienteDaoImpl clienteDao = new ClienteDaoImpl(getConn());
        ClienteBO clienteBO = new ClienteBO(getConn());
        Cliente[] arrayClienteDto;

        try {
            arrayClienteDto = clienteBO.findClientes(-1, idEmpresa, 0, 0, filtroBusqueda);
            if (arrayClienteDto.length > 0) {
                //Llenamos la lista de clientes del objeto respuesta
                for (Cliente item : arrayClienteDto) {
                    try {
                        if (item.getIdEstatus()!=2){
                            WsItemCliente wsItemCliente = new WsItemCliente();

                            wsItemCliente.setIdCliente(item.getIdCliente());
                            wsItemCliente.setRfcCliente(item.getRfcCliente());
                            wsItemCliente.setNombreCliente(item.getNombreCliente());
                            wsItemCliente.setApellidoPaternoCliente(item.getApellidoPaternoCliente());
                            wsItemCliente.setApellidoMaternoCliente(item.getApellidoMaternoCliente());
                            wsItemCliente.setRazonSocial(item.getRazonSocial());
                            wsItemCliente.setCalle(item.getCalle());
                            wsItemCliente.setNumero(item.getNumero());
                            wsItemCliente.setNumeroInterior(item.getNumeroInterior());
                            wsItemCliente.setColonia(item.getColonia());
                            wsItemCliente.setCodigoPostal(item.getCodigoPostal());
                            wsItemCliente.setPais(item.getPais());
                            wsItemCliente.setEstado(item.getEstado());
                            wsItemCliente.setMunicipio(item.getMunicipio());
                            wsItemCliente.setLada(item.getLada());
                            wsItemCliente.setTelefono(item.getTelefono());
                            wsItemCliente.setExtension(item.getExtension());
                            wsItemCliente.setCelular(item.getCelular());
                            wsItemCliente.setCorreo(item.getCorreo());
                            wsItemCliente.setContacto(item.getContacto());
                            wsItemCliente.setLatitud(item.getLatitud());
                            wsItemCliente.setLongitud(item.getLongitud());
                            if (StringManage.getValidString(item.getFolioClienteMovil()).length()>0){
                                wsItemCliente.setFolioClienteMovil(item.getFolioClienteMovil());
                            }else{
                                //Si no tiene un folio Movil, indica que fue creado en consola
                                // entonces asignamos uno, para que funcione adecuadamente el update desde movil
                                String folioMovilConsolaAsignado = ClienteBO.generaFolioMovil(idEmpresa);
                                try{
                                    //Actualizamos en BD servidor
                                    item.setFolioClienteMovil(folioMovilConsolaAsignado);
                                    clienteDao.update(item.createPk(), item);
                                    //Asignamos a objeto de respuesta
                                    wsItemCliente.setFolioClienteMovil(folioMovilConsolaAsignado);
                                }catch(Exception ex){
                                    ex.printStackTrace();
                                }
                            }
                            wsItemCliente.setDiasVisita(item.getDiasVisita());
                            wsItemCliente.setSaldoCliente(item.getSaldoCliente());
                            
                            //Consultamos la fecha limite de la reasignacion
                            SGClienteVendedorBO clienteVendedorBO = new SGClienteVendedorBO(item.getIdCliente(), getConn());
                            if (clienteVendedorBO.getClienteVendedor()!=null)
                                if(clienteVendedorBO.getClienteVendedor().getIdUsuarioVendedorReasignado() > 0){
                                    wsItemCliente.setFechaLimiteReasignacion(clienteVendedorBO.getClienteVendedor().getFechaLimiteReasigancion());
                                }
                            
                            wsItemCliente.setPerioricidad(item.getPerioricidad());
                            wsItemCliente.setFechaUltimaVisita(item.getFechaUltimaVisita());

                            listaClientes.addItem(wsItemCliente);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
        }

        return listaClientes.getLista();
    }
    
    /**
     * Consulta un listado de Conceptos por empresa
     * @param idEmpresa ID de la empresa que se va a consultar, en caso de ser sucursal se recuperan tambien lo correspondiente a la empresa padre
     * @return Lista de objetos WsItemConcepto
     */
    private List<WsItemConcepto> getListaConceptos(int idEmpresa, int idEmpleado) {

        List<WsItemConcepto> wsListaConceptos = new ArrayList<WsItemConcepto>();
        Encrypter encriptacion = new Encrypter();
        ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(getConn());
        double stockGral = 0;

        //Buscamos todos los Conceptos de la Empresa
        Concepto[] arrayConceptoDto;
        ConceptoBO conceptoBO = new ConceptoBO(getConn());
        ConceptoDaoImpl conceptoDao = new ConceptoDaoImpl(getConn());

        try {
            arrayConceptoDto = conceptoBO.findConceptos(-1, idEmpresa, 0, 0, " AND ID_ESTATUS = 1");
            if (arrayConceptoDto.length > 0) {
                //Llenamos la lista de conceptos del objeto respuesta
                for (Concepto itemConcepto : arrayConceptoDto) {
                    WsItemConcepto wsItemConcepto = new WsItemConcepto();
                    String nombreConcepto = StringManage.getValidString(itemConcepto.getNombreDesencriptado());
                    stockGral = 0;
                    
                    double cantidadInventarioEmpleado = itemConcepto.getNumArticulosDisponibles();
                    //Buscamos inventario específico de empleado
                    EmpleadoInventarioRepartidorBO empleadoInventarioRepartidorBO = new EmpleadoInventarioRepartidorBO(getConn());
                    EmpleadoInventarioRepartidor[] empleadoInventarios 
                            =  empleadoInventarioRepartidorBO.findEmpleadoInventarioRepartidors(-1, idEmpleado, 0, 0, " AND ID_CONCEPTO = "+itemConcepto.getIdConcepto());
                    for(EmpleadoInventarioRepartidor ei: empleadoInventarios){
                        cantidadInventarioEmpleado += ei.getCantidad();
                    }

                    wsItemConcepto.setIdConcepto(itemConcepto.getIdConcepto());
                    try {
                        if (StringManage.getValidString(nombreConcepto).length()<=0
                                && StringManage.getValidString(itemConcepto.getNombre()).length()>0 )
                            nombreConcepto = encriptacion.decodeString(itemConcepto.getNombre());
                    } catch (Exception e) {
                        System.out.println("############ Error en el sistema al intentar desencriptar el nombre de un concepto." + e.getMessage());
                    }
                    
                    //Buscamos la marca a la que pertenece
                    String nombreMarca = "";
                    try{
                        if (itemConcepto.getIdMarca()>0){
                            MarcaBO marcaBO = new MarcaBO(itemConcepto.getIdMarca(), getConn());
                            nombreMarca = marcaBO.getMarca()!=null?StringManage.getValidString(marcaBO.getMarca().getNombre()):"";
                        }
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    
                    try{
                        stockGral = exisAlmBO.getExistenciaGeneralByEmpresaProducto(itemConcepto.getIdEmpresa(), itemConcepto.getIdConcepto());
                    }catch(Exception e){
                        System.out.println("############ Error en el sistema al intentar buscar existencia de un concepto." + e.getMessage());
                    }
                    
                    BigDecimal precioBigD = (new BigDecimal(itemConcepto.getPrecio())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal stockBigD = (new BigDecimal(stockGral)).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal precioCompraBigD = (new BigDecimal(itemConcepto.getPrecioCompra())).setScale(2, RoundingMode.HALF_UP);
                    
                    BigDecimal precioDocenaBigD = (new BigDecimal(itemConcepto.getPrecioDocena())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal precioMedioMayoreoBigD = (new BigDecimal(itemConcepto.getPrecioMedioMayoreo())).setScale(2, RoundingMode.HALF_UP);                    
                    BigDecimal precioMayoreoBigD = (new BigDecimal(itemConcepto.getPrecioMayoreo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal precioEspecialBigD = (new BigDecimal(itemConcepto.getPrecioEspecial())).setScale(2, RoundingMode.HALF_UP);
                    
                    BigDecimal maxMenudeoBigD = (new BigDecimal(itemConcepto.getMaxMenudeo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal minMedioMayoreoBigD = (new BigDecimal(itemConcepto.getMinMedioMayoreo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal maxMedioMayoreoBigD = (new BigDecimal(itemConcepto.getMaxMedioMayoreo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal minMayoreoBigD = (new BigDecimal(itemConcepto.getMinMayoreo())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal descuentoMontoBigD = (new BigDecimal(itemConcepto.getDescuentoMonto())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal descuentoPorcentajeBigD = (new BigDecimal(itemConcepto.getDescuentoPorcentaje())).setScale(2, RoundingMode.HALF_UP);
                    
                    BigDecimal precioMinimoVentaBigD = (new BigDecimal(itemConcepto.getPrecioMinimoVenta())).setScale(2, RoundingMode.HALF_UP);
                    
                    wsItemConcepto.setNombre(nombreConcepto);
                    wsItemConcepto.setDescripcion(itemConcepto.getDescripcion());
                    wsItemConcepto.setPrecio(precioBigD.doubleValue());
                    wsItemConcepto.setIdentificacion(itemConcepto.getIdentificacion());
                    wsItemConcepto.setIdCategoria(itemConcepto.getIdCategoria());
                    wsItemConcepto.setIdAlmacen(itemConcepto.getIdAlmacen());
                    //Stock de empleado repartidor (empleado movil-camioneta)
                    wsItemConcepto.setStock(cantidadInventarioEmpleado);
                    //Stock de almacen general
                    wsItemConcepto.setStockAlmacen(stockBigD.doubleValue());
                    
                    wsItemConcepto.setMarca(nombreMarca);
                    wsItemConcepto.setPrecioCompra(precioCompraBigD.doubleValue());
                    
                    wsItemConcepto.setPrecioDocena(precioDocenaBigD.doubleValue());
                    wsItemConcepto.setPrecioMedioMayoreo(precioMedioMayoreoBigD.doubleValue());
                    wsItemConcepto.setPrecioMayoreo(precioMayoreoBigD.doubleValue());
                    wsItemConcepto.setPrecioEspecial(precioEspecialBigD.doubleValue());
                    wsItemConcepto.setMaxMenudeo(maxMenudeoBigD.doubleValue());
                    wsItemConcepto.setMinMedioMayoreo(minMedioMayoreoBigD.doubleValue());
                    wsItemConcepto.setMaxMedioMayoreo(maxMedioMayoreoBigD.doubleValue());
                    wsItemConcepto.setMinMayoreo(minMayoreoBigD.doubleValue());
                    
                    wsItemConcepto.setDescuentoMonto(descuentoMontoBigD.doubleValue());
                    wsItemConcepto.setDescuentoPorcentaje(descuentoPorcentajeBigD.doubleValue());
                    
                    wsItemConcepto.setPrecioMinimoVenta(precioMinimoVentaBigD.doubleValue());
                    
                    if(itemConcepto.getCaracteristiscas() != null){
                        wsItemConcepto.setCaracteristicas(itemConcepto.getCaracteristiscas());
                    }
                    
                    BigDecimal montoComision = (new BigDecimal(itemConcepto.getComisionMonto())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal porcentajeComision = (new BigDecimal(itemConcepto.getComisionPorcentaje())).setScale(2, RoundingMode.HALF_UP);                            
                    wsItemConcepto.setMontoComision(montoComision.doubleValue());
                    wsItemConcepto.setPorcentajeComision(porcentajeComision.doubleValue());
                    
                    if (StringManage.getValidString(itemConcepto.getFolioConceptoMovil()).length()>0){
                        wsItemConcepto.setFolioConceptoMovil(itemConcepto.getFolioConceptoMovil());
                    }else{
                        //Si no tiene un folio Movil, indica que fue creado en consola
                        // entonces asignamos uno, para que funcione adecuadamente el update desde externo
                        String folioMovilConsolaAsignado = ConceptoBO.generaFolioMovil(idEmpresa);
                        try{
                            //Actualizamos en BD servidor
                            itemConcepto.setFolioConceptoMovil(folioMovilConsolaAsignado);
                            conceptoDao.update(itemConcepto.createPk(), itemConcepto);
                            //Asignamos a objeto de respuesta
                            wsItemConcepto.setFolioConceptoMovil(folioMovilConsolaAsignado);
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                    
                    wsListaConceptos.add(wsItemConcepto);
                    
                    
                }
            }
        } catch (Exception e) {
        }
        return wsListaConceptos;
    }
}