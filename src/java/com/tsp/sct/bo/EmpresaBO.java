/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Cliente;
import com.tsp.sct.dao.dto.Concepto;
import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.dao.dto.EmpresaPermisoAplicacion;
import com.tsp.sct.dao.dto.EmpresaPk;
import com.tsp.sct.dao.dto.TpvTipoServicio;
import com.tsp.sct.dao.dto.Ubicacion;
import com.tsp.sct.dao.dto.UbicacionPk;
import com.tsp.sct.dao.dto.Usuarios;
import com.tsp.sct.dao.exceptions.EmpresaDaoException;
import com.tsp.sct.dao.jdbc.ClienteDaoImpl;
import com.tsp.sct.dao.jdbc.ConceptoDaoImpl;
import com.tsp.sct.dao.jdbc.EmpresaDaoImpl;
import com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl;
import com.tsp.sct.dao.jdbc.TpvTipoServicioDaoImpl;
import com.tsp.sct.dao.jdbc.UbicacionDaoImpl;
import com.tsp.sct.mail.TspMailBO;
import com.tsp.sct.util.Encrypter;
import com.tsp.sct.util.GenericValidator;
import com.tsp.sct.util.StringManage;
import com.tsp.sgfens.sesion.ComprobanteFiscalSesion;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author ISCesarMartinez
 */
public class EmpresaBO {
    private Empresa empresa = null;
    private Ubicacion ubicacion = null;

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public EmpresaBO(int idEmpresa, Connection conn){
        this.conn = conn;
        try{
            EmpresaDaoImpl EmpresaDaoImpl = new EmpresaDaoImpl(this.conn);
            this.empresa = EmpresaDaoImpl.findByPrimaryKey(idEmpresa);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public EmpresaBO(Connection conn){
        this.conn = conn;
    }
    
    public Empresa findEmpresabyId(long idEmpresa) throws Exception{
        Empresa empresa = null;
        
        try{
            EmpresaDaoImpl empresaDaoImpl = new EmpresaDaoImpl(this.conn);
            empresa = empresaDaoImpl.findByPrimaryKey((int)idEmpresa);
            if (empresa==null){
                throw new Exception("No se encontro ninguna empresa que corresponda al usuario según los parámetros específicados.");
            }
            if (empresa.getIdEmpresa()<=0){
                throw new Exception("No se encontro ninguna empresa que corresponda al usuario según los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de Empresa del usuario. Error: " + e.getMessage());
        }
        
        return empresa;
    }
    
     public Empresa getEmpresaGenericoByEmpresa(int idEmpresa) throws Exception{
        Empresa empresa = null;
        
        try{
            EmpresaDaoImpl empresaDaoImpl = new EmpresaDaoImpl(this.conn);
            empresa = empresaDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (empresa==null){
                throw new Exception("La empresa no tiene creada alguna Sucursal");
            }
        }catch(EmpresaDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada alguna Sucursal");
        }
        
        return empresa;
    }
     
     /**
     * Realiza una búsqueda por ID Empresa en busca de
     * coincidencias
     * @param idEmpresaSucursal ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public Empresa[] findEmpresas(int idEmpresaBuscada, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        Empresa[] marcaDto = new Empresa[0];
        EmpresaDaoImpl marcaDao = new EmpresaDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idEmpresaBuscada>0){
                sqlFiltro ="ID_EMPRESA_PADRE=" + idEmpresaBuscada + " AND ";
            }else{
                sqlFiltro ="ID_EMPRESA_PADRE>0 AND";
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
            
            marcaDto = marcaDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NOMBRE_COMERCIAL ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return marcaDto;
    }
    
    
    /**
     * Expresa si la empresa y sucursales tienen acceso al aplicativo
     * SGFENS - Pretoriano Soft.
     * @return true en caso de tener acceso, false en caso contrario
     */
    public boolean haveAccessApp(int idEmpresa){
        boolean haveAccess = false;
        
        try{
            idEmpresa = getEmpresaMatriz(idEmpresa).getIdEmpresa();
            
            EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(this.conn).findByPrimaryKey(idEmpresa);
            if (empresaPermisoAplicacionDto!=null){
                if (empresaPermisoAplicacionDto.getAccesoSgfensPretoriano()==(short)1)
                    haveAccess = true;
                if (empresaPermisoAplicacionDto.getAccesoSgfensPretoriano()==(short)2)
                    haveAccess = true;
                if (empresaPermisoAplicacionDto.getAccesoSgfensPretoriano()==(short)3)
                    haveAccess = true;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return haveAccess;
    }

    public String getEmpresasByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";        

        try{
            Empresa[] Empresa = findEmpresas(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (Empresa empresaItem:Empresa){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==empresaItem.getIdEmpresa())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+empresaItem.getIdEmpresa()+"' "
                            + selectedStr
                            + "title='"+empresaItem.getRazonSocial()+"'>"
                            +  empresaItem.getRazonSocial() + " - " + empresaItem.getNombreComercial()
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
    
    public String getEmpresasByIdHTMLCombo(int idEmpresa, int idSeleccionado, String filtroBusqueda){
        String strHTMLCombo ="";        

        try{
            //Empresa[] Empresa = findEmpresas(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            Empresa[] Empresa = findEmpresas(-1, idEmpresa, 0, 0, filtroBusqueda);
            
            for (Empresa empresaItem:Empresa){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==empresaItem.getIdEmpresa())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+empresaItem.getIdEmpresa()+"' "
                            + selectedStr
                            + "title='"+empresaItem.getRazonSocial()+"'>"
                            +  empresaItem.getRazonSocial() + " - " + empresaItem.getNombreComercial()
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
     * Recupera la Empresa Matriz que corresponda a la empresa indicada
     * En caso de ser la propia empresa matriz se retorna la misma.
     */
    public Empresa getEmpresaMatriz(long idEmpresa) throws Exception{
        Empresa empresaDto = findEmpresabyId(idEmpresa);
        
        if ((empresaDto.getIdEmpresa() != empresaDto.getIdEmpresaPadre())
               && empresaDto.getIdEmpresaPadre()>0 ){
            //Si el ID Empresa Padre es mayor a 0 y diferente del ID de empresa actual
            // indica que es una sucursal, por lo tanto buscamos su matriz
            empresaDto = findEmpresabyId(empresaDto.getIdEmpresaPadre());
        }
        
        return empresaDto;
    }
    
    /**
     * Retorna los datos de configuración TPV asignados a la empresa
     * SGFENS - Pretoriano Soft.
     * @return TpvTipoServicio
     */
    public TpvTipoServicio getTPVTipoServicio(int idEmpresa){
        TpvTipoServicio tpvTipoServicio = null;
        
        try{
            int idEmpresaPadre = getEmpresaMatriz(idEmpresa).getIdEmpresa();
            EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(this.conn).findByPrimaryKey(idEmpresaPadre);
            if (empresaPermisoAplicacionDto!=null){
                if (empresaPermisoAplicacionDto.getIdTpvTipoServicio()>0){
                    tpvTipoServicio = new  TpvTipoServicioDaoImpl(this.conn).findByPrimaryKey(empresaPermisoAplicacionDto.getIdTpvTipoServicio());
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return tpvTipoServicio;
    }
    
    /**
     * Resta en 1 la cantidad de Folios disponibles de la empresa sucursal
     * @return int Cantidad de Folios actualizada
     * @throws Exception Si se cuenta con 0 folios disponibles, no es posible restar. Tambien puede lanzarse una excepción al intentar actualizar en BD.
     */
    
    public ComprobanteFiscalSesion comprobanteFiscalSesion = null;
    public float montoTotalComprobante = 0;
    
    public int restaFolioCredito() throws Exception{
        int foliosActuales = 0;
        int foliosDisponibles = this.empresa.getFoliosDisponibles();
        
        if (foliosDisponibles>0){
            //Restamos 1 folio
            foliosActuales = foliosDisponibles - 1;
            
            //Actualizamos registro de sucursal - empresa
            this.empresa.setFoliosDisponibles(foliosActuales);
            new EmpresaDaoImpl(this.conn).update(this.empresa.createPk(), this.empresa);
            
        }else{
            //verificamos si la empresa tiene permiso de utilizar créditos de emergencia:
            EmpresaPermisoAplicacion empresaPermiso = null;
            boolean accesoCreditosEmergencia = false;
            
            empresaPermiso = new EmpresaPermisoAplicacionDaoImpl(conn).findByPrimaryKey(empresa.getIdEmpresaPadre());
            if(empresaPermiso != null){
                System.out.println("+++++++++++++++++++++++++++++++++++ Se encontro registro en Empresa Permiso Aplicacion...");
                if(empresaPermiso.getAccesoCreditosEmergencia() == 1){
                        accesoCreditosEmergencia = true;
                        System.out.println("+++++++++++++++++++++++++++++++++++ Tiene permisos para usar creditos de emergencia...");
                }else{
                        accesoCreditosEmergencia = false;
                        System.out.println("+++++++++++++++++++++++++++++++++++ NO Tiene permisos para usar creditos de emergencia...");
                }
            }else{
                    System.out.println("+++++++++++++++++++++++++++++++++++ NO EXISTE Y NO Tiene permisos para usar creditos de emergencia...");
                    accesoCreditosEmergencia = false;
            }
            
            if(accesoCreditosEmergencia == true){
                System.out.println("++++++++++++++++++++ PROCESO DE GUARDADO, ACTUALIZACION DE CREDITOS DE EMERGENCIA . . . ");
                EmergenciaCreditosInsertsActualizacionesBO actualizacionesBO = new EmergenciaCreditosInsertsActualizacionesBO(conn);
                actualizacionesBO.actualizaCreditosRegistros(empresaPermiso, comprobanteFiscalSesion, empresa, montoTotalComprobante);
                System.out.println("++++++++++++++++++++ . . . FIN DE PROCESO DE GUARDADO, ACTUALIZACION DE CREDITOS DE EMERGENCIA");
            }else{
                throw new Exception("La empresa/sucursal no cuenta con Créditos (folios) Disponibles para facturar. Contacte a su administrador para adquirir un nuevo paquete de Folios.");
            }
            
            
            //throw new Exception("La empresa/sucursal no cuenta con Créditos (folios) Disponibles para facturar. Contacte a su administrador para adquirir un nuevo paquete de Folios.");            
        }
        
        return foliosActuales;
    }
    
    /**
     * Suma en 1 la cantidad de Folios disponibles de la empresa sucursal
     * @return int Cantidad de Folios actualizada
     * @throws Exception Puede lanzarse una excepción al intentar actualizar en BD.
     */
    public int sumaFolioCredito() throws EmpresaDaoException {
        int foliosActuales = 0;
        int foliosDisponibles = this.empresa.getFoliosDisponibles();
        
        if (foliosDisponibles>0){
            //Sumamos 1 folio
            foliosActuales = foliosDisponibles + 1;
        }else{
            foliosActuales = 1;
        }
        
        //Actualizamos registro de sucursal - empresa
        this.empresa.setFoliosDisponibles(foliosActuales);
        new EmpresaDaoImpl(this.conn).update(this.empresa.createPk(), this.empresa);
        
        return foliosActuales;
    }
    
     //metodoo para ver cuantos usuarios tiene la empresa 
    public int cuentaUsuariosEmpresa(){
        Empresa[] empresas = findEmpresas(0, 0, 0, 0, " AND ID_VENDEDOR = -1 AND ID_FRANQUICIATARIO = -1 ");
        //VEMOS A QUE EMPRESA LO METEMOS, PARA ESO CONTAMOS EL NUMERO DE USUARIOS QUE TIENE PARA VER DONDE LA METEMOS:
        int idEmpresaSeleccionada = -1;
        for(Empresa emp : empresas){
            Usuarios[] users = new UsuariosBO().getUsuariosByEmpresa(emp.getIdEmpresa());            
            if(users.length < 5){
                idEmpresaSeleccionada = emp.getIdEmpresa();
            }
        }
        return idEmpresaSeleccionada;        
    }
    
    //Metodo para crear una empresa nueva, esto para los usuario del EvcDemo
    public int creaEmpresa(){
        try {                
                //REALIZAMOS EL INSERT DE LA UBICACION FISCAL
                Ubicacion ubicacionDto = new Ubicacion();
                UbicacionDaoImpl ubicacionesDaoImpl = new UbicacionDaoImpl(this.getConn());
                Ubicacion ultimoRegistroUbicacion = ubicacionesDaoImpl.findLast();
                int idubicacionUltima = ultimoRegistroUbicacion.getIdUbicacion() + 1;
                
                ubicacionDto.setIdUbicacion(idubicacionUltima);
                ubicacionDto.setCalle("PRADO");
                ubicacionDto.setNumExt("123");
                ubicacionDto.setNumInt("2B");
                ubicacionDto.setColonia("MODELO");
                ubicacionDto.setCodigoPostal("03320");
                ubicacionDto.setPais("MEXICO");
                ubicacionDto.setEstado("DISTRITO FEDERAL");
                ubicacionDto.setMunicipio("COYOACAN");
                
                 /**
                 * Realizamos el insert
                 */
                UbicacionPk ubicacionPk = ubicacionesDaoImpl.insert(ubicacionDto);
                        
                /**
                * Creamos el registro de Empresa
                */
                Empresa empresaDto = new Empresa();
                EmpresaDaoImpl empresasDaoImpl = new EmpresaDaoImpl(this.getConn());
                
                int idEmpresaNuevo;

                Empresa ultimoRegistroEmpresas = empresasDaoImpl.findLast();
                idEmpresaNuevo = ultimoRegistroEmpresas.getIdEmpresa() + 1;
                
                empresaDto.setIdEmpresa(idEmpresaNuevo);
                empresaDto.setIdEmpresaPadre(idEmpresaNuevo);
                empresaDto.setIdUbicacionFiscal(ubicacionPk.getIdUbicacion());
                empresaDto.setIdTipoEmpresa(1);
                empresaDto.setRfc("AAA010101AAA");
                empresaDto.setRazonSocial("PRUEBAS EVC");
                empresaDto.setNombreComercial("EVC MOVIL");
                empresaDto.setFoliosDisponibles(10);
                empresaDto.setRegimenFiscal("REGIMEN GENERAL DE LEY");
                empresaDto.setIdEstatus(1);
                empresaDto.setFechaRegistro(new Date());
                empresaDto.setIdFranquiciatario(-1);//SERA -1 PARA CONTROL DE QUE FUE CREADO AUTOMÁTICAMENTE PARA ASIGNAR LOS USUARIOS Y EMPLEADOS A ESTA EMPRESA
                empresaDto.setIdVendedor(-1);//SERA -1 PARA CONTROL DE QUE FUE CREADO AUTOMÁTICAMENTE PARA ASIGNAR LOS USUARIOS Y EMPLEADOS A ESTA EMPRESA
                
                /**
                 * Realizamos el insert de empresa:
                 */
                EmpresaPk empresaPk =  empresasDaoImpl.insert(empresaDto);
                                
                /**
                 * Verificamos en qué sistema se da de alta, asi sabremos si activamos en preto o no
                 */
                /////*              
               
                EmpresaPermisoAplicacion pretoriano = new EmpresaPermisoAplicacion();
                pretoriano.setIdEmpresa(empresaPk.getIdEmpresa());
                pretoriano.setAccesoSgfensPretoriano((short)3);                
                pretoriano.setIdTpvTipoServicio(5);
                pretoriano.setIdPretoCaracteristicaProductoAdquirido(5);
                pretoriano.setRevisionCantidadProducto(0);
                pretoriano.setAccesoSgfensNomina(0);
                pretoriano.setAccesoSgfensFacturacion(1);
                pretoriano.setAccesoSgfensCobrotarjeta(1);
                pretoriano.setAccesoSgfensPretomovil(1);
                pretoriano.setAccesoSgfensNumLicenciasMoviles(5);

                new EmpresaPermisoAplicacionDaoImpl(this.getConn()).insert(pretoriano);
                
                //**crear 5 clientes y 5 conceptos
                //5 conceptos
                try{
                ConceptoDaoImpl cdi = new ConceptoDaoImpl(this.getConn());
                for(int x = 0 ; x < 5; x++){
                    Concepto concepto = new Concepto();
                    concepto.setIdEmpresa(empresaPk.getIdEmpresa());
                    if(x == 0){
                        concepto.setNombre(encripta("Pepita Rusa 35g"));
                        concepto.setDescripcion("Pepita Rusa 35g");
                        concepto.setPrecio(6);
                    }else if(x == 1){
                        concepto.setNombre(encripta("Cacahuate Enchilado 80g"));
                        concepto.setDescripcion("Cacahuate Enchilado 80g");
                        concepto.setPrecio(6);
                    }else if(x == 2){
                        concepto.setNombre(encripta("Goma 80g"));
                        concepto.setDescripcion("Goma 80g");
                        concepto.setPrecio(6);
                    }else if(x == 3){
                        concepto.setNombre(encripta("Pistache 45g"));
                        concepto.setDescripcion("Pistache 45g");
                        concepto.setPrecio((float)10.5);
                    }else if(x == 4){
                        concepto.setNombre(encripta("Tubo Surtido 120g"));
                        concepto.setDescripcion("Tubo Surtido 120g");
                        concepto.setPrecio((float)8.5);
                    } 
                    concepto.setIdEstatus(1);
                    concepto.setIdentificacion(null);
                    concepto.setIdCategoria(0);
                    concepto.setPrecioCompra(0);
                    concepto.setNumArticulosDisponibles(10);                
                    cdi.insert(concepto);
                }
                }catch(Exception e){}
                //3 clientes
                try{
                ClienteDaoImpl clienteDaoImpl = new ClienteDaoImpl(this.getConn());
                
                    Cliente cli = new Cliente();
                    cli.setIdEmpresa(empresaPk.getIdEmpresa());
                    cli.setRfcCliente("XAXA010101000");
                    cli.setNombreCliente("Misael");
                    cli.setApellidoMaternoCliente("Ruiz");
                    cli.setApellidoMaternoCliente("Romero");
                    cli.setRazonSocial("Abarrotes Kia alma");
                    cli.setCalle("Calle 4");
                    cli.setNumero("12");
                    cli.setNumeroInterior(null);
                    cli.setColonia("Sta Rosa Xochiac");
                    cli.setCodigoPostal("01830");
                    cli.setPais("México");
                    cli.setEstado("DF");
                    cli.setMunicipio("Alvaro Obregón");
                    cli.setIdEstatus(1);                    
                    clienteDaoImpl.insert(cli);
                    
                    Cliente cli2 = new Cliente();
                    cli2.setIdEmpresa(empresaPk.getIdEmpresa());
                    cli2.setRfcCliente("XAXA010101000");
                    cli2.setNombreCliente("Aurora");
                    cli2.setApellidoMaternoCliente("Valentines");
                    cli2.setApellidoMaternoCliente("Rojas");
                    cli2.setRazonSocial("Abarrotes Loreto");
                    cli2.setCalle("5 de febrero");
                    cli2.setNumero("1069");
                    cli2.setNumeroInterior(null);
                    cli2.setColonia("Américas unidas");
                    cli2.setCodigoPostal("01759");
                    cli2.setPais("México");
                    cli2.setEstado("DF");
                    cli2.setMunicipio("Miguel Hidalgo");
                    cli2.setIdEstatus(1);                    
                    clienteDaoImpl.insert(cli2);
                }catch(Exception e){}
                //**
               
                
            return empresaPk.getIdEmpresa();
            
            }catch(Exception e){
                e.printStackTrace();                
            } 
        return -1;
    }
    
    //metodo para encriptar los productos que se crean para los usuarios del EvcDemo.
    public String encripta(String dato) throws IOException{
         Encrypter encripter = new Encrypter();
         return encripter.encodeString2(dato);        
    }
    
    
    public Empresa getEmpresaByRazonSocial(String nombreComercial) throws Exception {
        Empresa empresa = null;

        try {
            EmpresaDaoImpl empresaDaoImpl = new EmpresaDaoImpl(this.conn);
            empresa = empresaDaoImpl.findByDynamicWhere(" RAZON_SOCIAL ='" + nombreComercial + "'", new Object[0])[0];
            if (empresa == null) {
                throw new Exception("La empresa no Existe");
            }
        } catch (EmpresaDaoException e) {
            e.printStackTrace();
            throw new Exception("La empresa no Existe");
        }

        return empresa;
    }
    
    public boolean restaCreditosSMSMatriz(long idEmpresa, int creditosSmsRequeridos){
        boolean exito = false;
        try{
            //Restamos créditos a cuenta de empresa matriz
            Empresa empresaMatriz = getEmpresaMatriz(idEmpresa);
            int saldoCreditoSms = empresaMatriz.getCreditosSms() - creditosSmsRequeridos;
            if (saldoCreditoSms<0)
                saldoCreditoSms = 0;
            
            //actualizamos en BD
            empresaMatriz.setCreditosSms(saldoCreditoSms);
            new EmpresaDaoImpl(conn).update(empresaMatriz.createPk(), empresaMatriz);
            
            if (saldoCreditoSms<=0){
                enviaNotificacionEmpresaSinCreditosSMS(empresaMatriz);
            }else{
                exito = true;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }  
        return exito;
    }
    
    public void enviaNotificacionEmpresaSinCreditosSMS(Empresa empresaMatriz){
        ParametrosBO parametrosBO = new ParametrosBO(getConn());
        if (parametrosBO.getParametroInt("app.sms.notificarCreditosAgotados")<=0){
        //Si no esta activada la configuración general de notificar en caso de créditos Agotados
            // se omite este metodo
            return;
        }
        
        //enviar notificacion sin Creditos SMS
        try {
            TspMailBO mailBO = new TspMailBO();

            mailBO.setConfigurationMovilpyme();

            GenericValidator genericValidator = new GenericValidator();
            ArrayList<String> destinatarios = new ArrayList<String>();
            ArrayList<String> destinatariosBCC = new ArrayList<String>();

            try{
                UsuariosBO usuariosBO = new UsuariosBO(conn);
                Usuarios[] desarrolladores = usuariosBO.findUsuariosByRol(empresaMatriz.getIdEmpresa(), RolesBO.ROL_DESARROLLO);
                Usuarios[] administradores = usuariosBO.findUsuariosByRol(empresaMatriz.getIdEmpresa(), RolesBO.ROL_ADMINISTRADOR);

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

            //Destinatarios fijos CC , de movilpyme Modulo SMS
            try{
                String correos = parametrosBO.getParametroString("app.sms.destFijosMail");

                String[] dests = correos.split(",");
                for (String dest : dests ){
                    destinatariosBCC.add(StringManage.getValidString(dest));
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }

            //Agregamos todos los correos
            for (String destinatario : destinatarios){
                try{                           
                    if(genericValidator.isEmail(destinatario)){
                        try{
                            mailBO.addTo(destinatario, destinatario);
                        }catch(Exception ex){}
                    }
                }catch(Exception ex){}
            }
            for (String destinatario : destinatariosBCC){
                try{                           
                    if(genericValidator.isEmail(destinatario)){
                        try{
                            mailBO.addCC(destinatario, "Administrador SMS");
                        }catch(Exception ex){}
                    }
                }catch(Exception ex){}
            }

            String msg = "<b>Buen día!</b><br/> ";

            msg += "<br/>Estimado Cliente '" + empresaMatriz.getRazonSocial() + "' (ID Empresa Matriz: " + empresaMatriz.getIdEmpresa() + ") le informamos que los créditos de envío SMS " 
                    + (empresaMatriz.getCreditosSms()>0?"estan por terminar":"se han terminado")  + ", lo invitamos a adquirir más para que no pierda contacto con sus clientes.<br/>";
            
            msg += "<br/><b>Creditos SMS disponibles:" + empresaMatriz.getCreditosSms() + "</b>";

            mailBO.addMessageMovilpyme(msg ,1);

            mailBO.send("Cliente sin créditos SMS");

        } catch (Exception ex) {
            System.out.print("Ocurrio un error al intentar enviar el correo: " + ex.toString());
            ex.printStackTrace();
        } 
    }
}
