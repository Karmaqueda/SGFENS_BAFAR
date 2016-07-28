/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CallCenter;
import com.tsp.sct.dao.dto.Cliente;
import com.tsp.sct.dao.dto.ComprobanteFiscal;
import com.tsp.sct.dao.dto.Empleado;
import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.dao.dto.EmpresaPermisoAplicacion;
import com.tsp.sct.dao.dto.SgfensCobranzaAbono;
import com.tsp.sct.dao.dto.SgfensPedido;
import com.tsp.sct.dao.dto.SgfensPedidoProducto;
import com.tsp.sct.dao.dto.SmsDispositivoMovil;
import com.tsp.sct.dao.dto.SmsEmpresaConfig;
import com.tsp.sct.dao.dto.SmsEnvioDetalle;
import com.tsp.sct.dao.dto.SmsEnvioError;
import com.tsp.sct.dao.dto.SmsEnvioLote;
import com.tsp.sct.dao.dto.SmsPlantilla;
import com.tsp.sct.dao.dto.Usuarios;
import com.tsp.sct.dao.jdbc.EmpresaDaoImpl;
import com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl;
import com.tsp.sct.dao.jdbc.SmsDispositivoMovilDaoImpl;
import com.tsp.sct.dao.jdbc.SmsEmpresaConfigDaoImpl;
import com.tsp.sct.dao.jdbc.SmsEnvioDetalleDaoImpl;
import com.tsp.sct.dao.jdbc.SmsEnvioErrorDaoImpl;
import com.tsp.sct.dao.jdbc.SmsEnvioLoteDaoImpl;
import com.tsp.sct.mail.TspMailBO;
import com.tsp.sct.util.GenericMethods;
import com.tsp.sct.util.GenericValidator;
import com.tsp.sct.util.StringManage;
import com.tsp.sgfens.sesion.SmsEnvioDestinatariosSesion;
import com.tsp.sgfens.sesion.SmsEnvioSesion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ISCesar
 */
public class SmsEnvioLoteBO {
    private SmsEnvioLote smsEnvioLote = null;

    public SmsEnvioLote getSmsEnvioLote() {
        return smsEnvioLote;
    }

    public void setSmsEnvioLote(SmsEnvioLote smsEnvioLote) {
        this.smsEnvioLote = smsEnvioLote;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SmsEnvioLoteBO(Connection conn){
        this.conn = conn;
    }
    
     public SmsEnvioLoteBO(int idSmsEnvioLote, Connection conn){        
        this.conn = conn; 
        try{
           SmsEnvioLoteDaoImpl SmsEnvioLoteDaoImpl = new SmsEnvioLoteDaoImpl(this.conn);
            this.smsEnvioLote = SmsEnvioLoteDaoImpl.findByPrimaryKey(idSmsEnvioLote);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public SmsEnvioLote findSmsEnvioLotebyId(int idSmsEnvioLote) throws Exception{
        SmsEnvioLote SmsEnvioLote = null;
        
        try{
            SmsEnvioLoteDaoImpl SmsEnvioLoteDaoImpl = new SmsEnvioLoteDaoImpl(this.conn);
            SmsEnvioLote = SmsEnvioLoteDaoImpl.findByPrimaryKey(idSmsEnvioLote);
            if (SmsEnvioLote==null){
                throw new Exception("No se encontro ningun SmsEnvioLote que corresponda con los parámetros específicados.");
            }
            if (SmsEnvioLote.getIdSmsEnvioLote()<=0){
                throw new Exception("No se encontro ningun SmsEnvioLote que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del SmsEnvioLote del usuario. Error: " + e.getMessage());
        }
        
        return SmsEnvioLote;
    }
    
    /**
     * Realiza una búsqueda por ID SmsEnvioLote en busca de
     * coincidencias
     * @param idSmsEnvioLote ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public SmsEnvioLote[] findSmsEnvioLotes(int idSmsEnvioLote, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SmsEnvioLote[] smsEnvioLoteDto = new SmsEnvioLote[0];
        SmsEnvioLoteDaoImpl smsEnvioLoteDao = new SmsEnvioLoteDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSmsEnvioLote>0){
                sqlFiltro ="id_sms_envio_lote=" + idSmsEnvioLote + " AND ";
            }else{
                sqlFiltro ="id_sms_envio_lote>0 AND";
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
            
            smsEnvioLoteDto = smsEnvioLoteDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY id_sms_envio_lote desc"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return smsEnvioLoteDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID SmsEnvioLote y otros filtros
     * @param idSmsEnvioLote ID Del SmsEnvioLote para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public int findCantidadSmsEnvioLotes(int idSmsEnvioLote, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        SmsEnvioLoteDaoImpl smsEnvioLoteDao = new SmsEnvioLoteDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idSmsEnvioLote>0){
                sqlFiltro ="id_sms_envio_lote=" + idSmsEnvioLote + " AND ";
            }else{
                sqlFiltro ="id_sms_envio_lote>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_sms_envio_lote) as cantidad FROM " + smsEnvioLoteDao.getTableName() +  " WHERE " + 
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
     * Busca un valor unico entre registros SmsEnvioLote
     * @param idSmsEnvioLote ID De la SmsEnvioLote para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar cobranzaAbono, -1 para evitar filtro
     * @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @param selectSql Cadena correspondiente a estatuto SELECT del dato unico que se desea recuperar, puede ser un COUNT o SUM
     * @return Strig valor unico recuperado de query
     */
    public String findGroupValorUnicoEnvioLote(int idSmsEnvioLote, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda, 
            String selectSql) {
        String valor = "";
        SmsEnvioLoteDaoImpl smsEnvioLoteDao = new SmsEnvioLoteDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idSmsEnvioLote>0){
                sqlFiltro ="id_sms_envio_lote=" + idSmsEnvioLote + " AND ";
            }else{
                sqlFiltro ="id_sms_envio_lote>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT " + selectSql + " FROM " + smsEnvioLoteDao.getTableName() + " WHERE " + 
                    sqlFiltro
                    + sqlLimit);
            if(rs.next()){
                valor = rs.getString(1);
            }
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return valor;
    }
        
    public String getSmsEnvioLoteesByIdHTMLCombo(int idEmpresa, int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            SmsEnvioLote[] smsEnvioLoteesDto = findSmsEnvioLotes(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (SmsEnvioLote smsEnvioLote : smsEnvioLoteesDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado == smsEnvioLote.getIdSmsEnvioLote())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+smsEnvioLote.getIdSmsEnvioLote()+"' "
                            + selectedStr
                            + "title='"+smsEnvioLote.getIdSmsEnvioLote()+"'>"
                            + "(" + smsEnvioLote.getIdSmsEnvioLote() + ") " + smsEnvioLote.getAsunto()
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
     * Genera una agrupacion por Lote con sus correspondientes detalles
     * desde un arreglo de SmsEnvioDetalles
     * @param smsEnvioDetalles arreglo de SmsEnvioDetalles
     * @return Lista de Agrupaciones por lote
     */
    public List<AgrupacionLoteDetalles> agrupaDetallesPorLote(SmsEnvioDetalle[] smsEnvioDetalles){
        List<AgrupacionLoteDetalles> agrupaciones = new ArrayList<AgrupacionLoteDetalles>();
        
        try{
            for (SmsEnvioDetalle detalle : smsEnvioDetalles){
                
                boolean coincidenciaPreviaLote = false;
                for (AgrupacionLoteDetalles agrupacionLote : agrupaciones){
                    if (detalle.getIdSmsEnvioLote() == agrupacionLote.getSmsEnvioLote().getIdSmsEnvioLote()){
                    //coincide con una agrupacion ya existente
                        coincidenciaPreviaLote = true;
                        agrupacionLote.getListaDetalles().add(detalle);
                    }
                }
                
                if (!coincidenciaPreviaLote){
                // no coincidio con ninguna agrupacion previa
                    //creamos una nueva agrupacion
                    AgrupacionLoteDetalles agrupacionLoteDetalles = new AgrupacionLoteDetalles();
                    // a esa agrupacion, buscamos su Lote padre
                    SmsEnvioLote envioLote = findSmsEnvioLotebyId(detalle.getIdSmsEnvioLote());
                    agrupacionLoteDetalles.setSmsEnvioLote(envioLote);
                    //a esa agrupacion le agregamos el detalle de la iteracion actual
                    agrupacionLoteDetalles.getListaDetalles().add(detalle);
                    
                    agrupaciones.add(agrupacionLoteDetalles);
                }
                
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return agrupaciones;
    }
    
    public boolean enviarCorreoLotesConError(List<AgrupacionLoteDetalles> listaAgrupacionLoteDetalles){
        boolean enviado;
        
        try {
            TspMailBO mailBO = new TspMailBO();

            mailBO.setConfigurationMovilpyme();

            GenericValidator genericValidator = new GenericValidator();
            ParametrosBO parametrosBO = new ParametrosBO(getConn());
            ArrayList<String> destinatarios = new ArrayList<String>();

            try{
                String correos = parametrosBO.getParametroString("app.sms.destFijosMail");
                
                String[] dests = correos.split(",");
                for (String dest : dests ){
                    destinatarios.add(StringManage.getValidString(dest));
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }

            //Agregamos todos los correos
            for (String destinatario : destinatarios){
                try{                           
                    if(genericValidator.isEmail(destinatario)){
                        try{
                            mailBO.addTo(destinatario, "Administrador SMS");
                        }catch(Exception ex){}
                    }
                }catch(Exception ex){}
            }
            
            int maxIntentosFallidos = parametrosBO.getParametroInt("app.sms.maxIntentosFallidos");

            String msg = "<b>Buen día!</b><br/> ";
            
            msg += "<br/>Se detectaron los siguientes Lotes que contienen Mensajes con mas de " + maxIntentosFallidos + " intentos fallidos de envío: <br/><br/>";
            
            //detalles de la asignación
            msg+= "<table border='1'>";
            msg+= "<tr>";
            msg+= " <td>Empresa</td><td>ID Lote</td><td>Asunto</td><td>No. Mensajes con Error</td><td>Detalle Errores</td>";
            msg+= "</tr>";
            for (AgrupacionLoteDetalles agrupacionLoteDetalles : listaAgrupacionLoteDetalles){    
                String empresa = "-De Sistema-";
                String idLote = ""+agrupacionLoteDetalles.getSmsEnvioLote().getIdSmsEnvioLote();
                String asunto = "" + StringManage.getValidString(agrupacionLoteDetalles.getSmsEnvioLote().getAsunto());
                String noMensajesError = "" + agrupacionLoteDetalles.getListaDetalles().size();
                String detalleErrores = "";
                
                //recuperamos informacion de empresa emisora
                if (agrupacionLoteDetalles.getSmsEnvioLote().getIdEmpresa()>0){
                    Empresa empresaDto = new EmpresaDaoImpl(conn).findByPrimaryKey(agrupacionLoteDetalles.getSmsEnvioLote().getIdEmpresa());
                    empresa =  empresaDto.getNombreComercial();
                    if (StringManage.getValidString(empresa).length()<=0)
                        empresa = StringManage.getValidString(empresaDto.getRazonSocial());
                }
                List<Integer> listaIDsDetalles = new ArrayList<Integer>();
                for (SmsEnvioDetalle detalle : agrupacionLoteDetalles.getListaDetalles()){
                    listaIDsDetalles.add(detalle.getIdSmsEnvioDetalle());
                }
                String idsDetallesConcat = GenericMethods.concatInts(listaIDsDetalles, ',');
                SmsEnvioError[] erroresDetalles = new SmsEnvioErrorDaoImpl(conn).findByDynamicWhere(" id_estatus=1 AND id_sms_envio_detalle IN ("  + idsDetallesConcat + ")", null);
                for (SmsEnvioError error : erroresDetalles){
                    String aliasMovilSMS = "-Consola/Cron-";
                    if (error.getIdSmsDispositivo()>0){
                        SmsDispositivoMovil smsDispositivoMovilDto = new SmsDispositivoMovilDaoImpl(conn).findByPrimaryKey(error.getIdSmsDispositivo());
                        if (smsDispositivoMovilDto!=null)
                            aliasMovilSMS = "Movil SMS '" + smsDispositivoMovilDto.getAlias() + "' (" + smsDispositivoMovilDto.getNumeroCelular()  +")";
                    }
                    detalleErrores += "<p>" + aliasMovilSMS + " : " + StringManage.getValidString(error.getDescError()) + "</p>";
                }                
                
                msg+= "<tr>";
                msg+= " <td>" +empresa + "</td><td>" + idLote + "</td><td>" + asunto + "</td><td>"+ noMensajesError + "</td><td>"+ detalleErrores + "</td>";
                msg+= "</tr>";
            }
            msg+= "</table>";
            
            msg += "<br/>Se sugiere que los revise de manera minuciosa para descartar: <br/><ul>";
            msg += "    <li>Red movil no disponible/Fallas en la Red movil de los dispositivos de envío.</li>";
            msg += "    <li>Movil sin credito en los dispositivos de envío.</li>";
            msg += "    <li>Mensaje con contenido no permitido, caracteres o simbolos extraños.</li>";
            msg += "</ul>";

            mailBO.addMessageMovilpyme(msg ,1);

            mailBO.send("SMS incidentes en envios");

            enviado = true;
        } catch (Exception ex) {
            enviado = false;
            System.out.print("Ocurrio un error al intentar enviar el correo: " + ex.toString());
            ex.printStackTrace();
        }            
        
        return enviado;
    }
    
    public boolean enviarCorreoLoteProgramadoEnviado(int idSmsEnvioLote, int idSmsPlantilla, int cantidadEnviados){
        boolean enviado;
        
        //enviar notificacion sin Creditos SMS
        try {
            TspMailBO mailBO = new TspMailBO();

            mailBO.setConfigurationMovilpyme();

            GenericValidator genericValidator = new GenericValidator();
            ArrayList<String> destinatarios = new ArrayList<String>();
            //ArrayList<String> destinatariosBCC = new ArrayList<String>();
            int idEmpresa = 0;
            
            try{
                SmsEnvioLote smsEnvioLoteDto = findSmsEnvioLotebyId(idSmsEnvioLote);
                idEmpresa = smsEnvioLoteDto.getIdEmpresa();
                if (idEmpresa > 0){ // si es un lote creado por una empresa
                    Empresa empresaMatriz = new EmpresaBO(conn).getEmpresaMatriz(idEmpresa);
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
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }

            try{
                if (idEmpresa <= 0) { //si es un lote creado por movilpyme desde consola ventas
                    ParametrosBO parametrosBO = new ParametrosBO(getConn());
                    String correos = parametrosBO.getParametroString("app.sms.destFijosMail");

                    String[] dests = correos.split(",");
                    for (String dest : dests ){
                        destinatarios.add(StringManage.getValidString(dest));
                    }
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
            /*
            for (String destinatario : destinatariosBCC){
                try{                           
                    if(genericValidator.isEmail(destinatario)){
                        try{
                            mailBO.addCC(destinatario, "Administrador SMS");
                        }catch(Exception ex){}
                    }
                }catch(Exception ex){}
            }
            */
            
            SmsPlantilla smsPlantilla  =null;
            if (idSmsPlantilla>0){
                smsPlantilla = new SmsPlantillaBO(idSmsPlantilla, conn).getSmsPlantilla();
            }

            String msg = "<b>Buen día!</b><br/> ";

            msg += "<br/>Se han enviado " + cantidadEnviados +" mensajes correspondiente al Lote SMS de envío con ID " + idSmsEnvioLote + "<br/>";
            if (smsPlantilla != null)
                msg += "<br/>Utilizando la plantilla con ID " + smsPlantilla.getIdSmsPlantilla() + ", con asunto: '" + smsPlantilla.getAsunto() + "'<br/>";

            mailBO.addMessageMovilpyme(msg ,1);

            mailBO.send("SMS Lote programado Enviado");

            enviado = true;
        } catch (Exception ex) {
            enviado = false;
            System.out.print("Ocurrio un error al intentar enviar el correo: " + ex.toString());
            ex.printStackTrace();
        }          
        
        return enviado;
    }
    
    public SmsEnvioLote crearSmsNotificacionSistema(TipoNotificaSistema tipoNotificaSistema, int idEmpresa, int idObjetoDto){
        SmsEnvioLote smsEnvioLoteResultado = null;
        
        try{
            boolean registrar = false;
            
            String nombreDestinatario = "";
            String numeroCelularDestinatario = "";
            String asunto = "";
            String contenidoSMS = "";
            int idCliente = 0;
            int idEmpleado = 0;
            
            GenericValidator gc = new GenericValidator();
            SmsEmpresaConfig smsEmpresaConfigDto = null; 
            Empresa empresaMatrizDto = null;
            EmpresaPermisoAplicacion empresaPermisoAplicacion = null;
            EmpresaBO empresaBO = new EmpresaBO(conn);
            if (idEmpresa>0) {
                empresaMatrizDto = empresaBO.getEmpresaMatriz(idEmpresa);
                empresaPermisoAplicacion = new EmpresaPermisoAplicacionDaoImpl(conn).findByPrimaryKey(empresaMatrizDto.getIdEmpresa());
                try{
                    smsEmpresaConfigDto = new SmsEmpresaConfigDaoImpl(conn).findWhereIdEmpresaEquals(empresaMatrizDto.getIdEmpresa())[0];
                }catch(Exception ex){}
            }
            
            if (empresaPermisoAplicacion==null || empresaPermisoAplicacion.getAccesoModuloSms()==0){
            //La empresa no tiene contratado modulo SMS
                //throw new Exception("La Empresa ID " + (empresaMatrizDto!=null? empresaMatrizDto.getIdEmpresa() : "") + " no tiene activado modulo SMS.");
                return null;
            }
            
            contenidoSMS += (empresaMatrizDto!=null ? empresaMatrizDto.getNombreComercial() + " informa: " : "");
            
            if (tipoNotificaSistema ==  TipoNotificaSistema.PEDIDO_NUEVO){
                SgfensPedido pedido = new SGPedidoBO(conn).findPedidobyId(idObjetoDto);
                double adeudo = pedido.getTotal() - pedido.getSaldoPagado();
                int cantidadProductos = new SGPedidoProductoBO(conn).findCantidadByIdPedido(pedido.getIdPedido(), -1, 0, 0, "");
                
                asunto = "Nuevo Pedido ID " + idObjetoDto;
                contenidoSMS += "Se ha registrado una venta por un monto de " + pedido.getTotal() + ", correspondiente a la compra de " + cantidadProductos + " productos, se adeuda un total de " + adeudo + ".";
                
                idCliente = pedido.getIdCliente();
                
                if (smsEmpresaConfigDto==null || (smsEmpresaConfigDto.getMensajeVenta()==1 || smsEmpresaConfigDto.getMensajeVenta()==3)){
                // si esta permitido el envio de este tipo de mensajes, 1 = Activado por Cliente, 3 = Activado y Forzado por Movilpyme
                    registrar = true;
                    String mensajeLibre = smsEmpresaConfigDto!=null? StringManage.getValidString(smsEmpresaConfigDto.getMensajeVentaLibre()) : "";
                    if (smsEmpresaConfigDto!=null){
                    //si se tiene configuracion especifica, y se decidio enviar mensaje libre, o mix: fijo + libre
                        if (smsEmpresaConfigDto.getMensajeVentaFijo()==0 && StringManage.getValidString(mensajeLibre).length()>0){
                            contenidoSMS = mensajeLibre;
                        }else if (smsEmpresaConfigDto.getMensajeVentaFijo()==2){
                            contenidoSMS =  contenidoSMS + mensajeLibre;
                        }
                    }
                }else{
                    registrar = false;
                }
            }else if (tipoNotificaSistema ==  TipoNotificaSistema.ABONO_NUEVO){
                SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(idObjetoDto, conn);
                SgfensCobranzaAbono abono = cobranzaAbonoBO.getCobranzaAbono();
                double adeudoOriginal = 0;
                double adeudoActual = 0;
                if (abono.getIdPedido()>0){
                    SgfensPedido pedido = new SGPedidoBO(conn).findPedidobyId(abono.getIdPedido());
                    adeudoOriginal = pedido.getTotal();
                    adeudoActual = cobranzaAbonoBO.getSaldoAdeudoPedido(abono.getIdPedido()).doubleValue(); //pedido.getTotal() - pedido.getSaldoPagado();
                    
                    idCliente = pedido.getIdCliente();
                }else if (abono.getIdComprobanteFiscal()>0){
                    ComprobanteFiscal comprobanteFiscal = new ComprobanteFiscalBO(abono.getIdComprobanteFiscal(), conn).getComprobanteFiscal();
                    adeudoOriginal = comprobanteFiscal.getImporteNeto();
                    adeudoActual = cobranzaAbonoBO.getSaldoAdeudoComprobanteFiscal(abono.getIdComprobanteFiscal()).doubleValue();
                    
                    idCliente = comprobanteFiscal.getIdCliente();
                }
                
                asunto = "Nueva Cobranza ID " + idObjetoDto;
                contenidoSMS += "Se ha registrado un pago por un monto de " + abono.getMontoAbono() + ", se adeuda un total de " + adeudoActual + " de un monto inicial de " + adeudoOriginal + ".";
                
                if (smsEmpresaConfigDto==null || (smsEmpresaConfigDto.getMensajeAbono()==1 || smsEmpresaConfigDto.getMensajeAbono()==3)){
                // si esta permitido el envio de mensajes, 1 = Activado por Cliente, 3 = Activado y Forzado por Movilpyme
                    registrar = true;
                    String mensajeLibre = smsEmpresaConfigDto!=null? StringManage.getValidString(smsEmpresaConfigDto.getMensajeAbonoLibre()) : "";
                    if (smsEmpresaConfigDto!=null){
                    //si se tiene configuracion especifica, y se decidio enviar mensaje libre, o mix: fijo + libre
                        if (smsEmpresaConfigDto.getMensajeAbonoFijo()==0 && StringManage.getValidString(mensajeLibre).length()>0){
                            contenidoSMS = mensajeLibre;
                        }else if (smsEmpresaConfigDto.getMensajeAbonoFijo()==2){
                            contenidoSMS =  contenidoSMS + mensajeLibre;
                        }
                    }
                }else{
                    registrar = false;
                }
            }else if (tipoNotificaSistema ==  TipoNotificaSistema.FACTURA_NUEVO){
                ComprobanteFiscal comprobanteFiscal = new ComprobanteFiscalBO(idObjetoDto, conn).getComprobanteFiscal();
                
                asunto = "Nueva Factura ID " + idObjetoDto;
                contenidoSMS += "Se ha creado la factura ID " + idObjetoDto + ". UUID " + comprobanteFiscal.getUuid() + " y se ha enviado una notificación al correo registrado de cliente.";
                
                idCliente = comprobanteFiscal.getIdCliente();
                
                if (smsEmpresaConfigDto==null || (smsEmpresaConfigDto.getMensajeFactura()==1 || smsEmpresaConfigDto.getMensajeFactura()==3)){
                // si esta permitido el envio de mensajes, 1 = Activado por Cliente, 3 = Activado y Forzado por Movilpyme
                    registrar = true;
                    String mensajeLibre = smsEmpresaConfigDto!=null? StringManage.getValidString(smsEmpresaConfigDto.getMensajeFacturaLibre()) : "";
                    if (smsEmpresaConfigDto!=null){
                    //si se tiene configuracion especifica, y se decidio enviar mensaje libre, o mix: fijo + libre
                        if (smsEmpresaConfigDto.getMensajeFacturaFijo()==0 && StringManage.getValidString(mensajeLibre).length()>0){
                            contenidoSMS = mensajeLibre;
                        }else if (smsEmpresaConfigDto.getMensajeFacturaFijo()==2){
                            contenidoSMS =  contenidoSMS + mensajeLibre;
                        }
                    }
                }else{
                    registrar = false;
                }
            }else if (tipoNotificaSistema ==  TipoNotificaSistema.FACTURA_CANCELAR){
                ComprobanteFiscal comprobanteFiscal = new ComprobanteFiscalBO(idObjetoDto, conn).getComprobanteFiscal();
                
                asunto = "Factura Cancelada ID " + idObjetoDto;
                contenidoSMS += "Se ha cancelado la factura ID " + idObjetoDto + ". UUID " + comprobanteFiscal.getUuid() + " y se ha enviado una notificación al correo registrado de cliente.";
                
                idCliente = comprobanteFiscal.getIdCliente();
                
                if (smsEmpresaConfigDto==null || (smsEmpresaConfigDto.getMensajeCancelaFac()==1 || smsEmpresaConfigDto.getMensajeCancelaFac()==3)){
                // si esta permitido el envio de mensajes, 1 = Activado por Cliente, 3 = Activado y Forzado por Movilpyme
                    registrar = true;
                    String mensajeLibre = smsEmpresaConfigDto!=null? StringManage.getValidString(smsEmpresaConfigDto.getMensajeCancelaFacLibre()) : "";
                    if (smsEmpresaConfigDto!=null){
                    //si se tiene configuracion especifica, y se decidio enviar mensaje libre, o mix: fijo + libre
                        if (smsEmpresaConfigDto.getMensajeCancelaFacFijo()==0 && StringManage.getValidString(mensajeLibre).length()>0){
                            contenidoSMS = mensajeLibre;
                        }else if (smsEmpresaConfigDto.getMensajeCancelaFacFijo()==2){
                            contenidoSMS =  contenidoSMS + mensajeLibre;
                        }
                    }
                }else{
                    registrar = false;
                }
            }else if (tipoNotificaSistema ==  TipoNotificaSistema.CALLCENTER_NUEVO 
                    || tipoNotificaSistema ==  TipoNotificaSistema.CALLCENTER_CAMBIO
                    || tipoNotificaSistema ==  TipoNotificaSistema.CALLCENTER_CIERRE){
                
                String accionCallCenter = "Registrado";
                if (tipoNotificaSistema ==  TipoNotificaSistema.CALLCENTER_CAMBIO)
                    accionCallCenter = "Actualizado";
                if (tipoNotificaSistema ==  TipoNotificaSistema.CALLCENTER_CIERRE)
                    accionCallCenter = "Cerrado";
                
                CallCenter callCenterDto = new CallCenterBO(conn).findCallCenterbyId(idObjetoDto);
                        
                asunto = "CallCenter Ticket " + accionCallCenter + " ID " + idObjetoDto;
                contenidoSMS += "Se ha " + accionCallCenter + " el ticket de servicio folio " + callCenterDto.getNumeroTicket()+ ", referente a " + StringManage.getValidString(callCenterDto.getDescripcion());
                
                idCliente = callCenterDto.getIdCliente();
                
                if (smsEmpresaConfigDto==null || (smsEmpresaConfigDto.getMensajeCallcenter()==1 || smsEmpresaConfigDto.getMensajeCallcenter()==3)){
                // si esta permitido el envio de mensajes, 1 = Activado por Cliente, 3 = Activado y Forzado por Movilpyme
                    registrar = true;
                    String mensajeLibre = smsEmpresaConfigDto!=null? StringManage.getValidString(smsEmpresaConfigDto.getMensajeCallcenterLibre()) : "";
                    if (smsEmpresaConfigDto!=null){
                    //si se tiene configuracion especifica, y se decidio enviar mensaje libre, o mix: fijo + libre
                        if (smsEmpresaConfigDto.getMensajeCallcenterFijo()==0 && StringManage.getValidString(mensajeLibre).length()>0){
                            contenidoSMS = mensajeLibre;
                        }else if (smsEmpresaConfigDto.getMensajeCallcenterFijo()==2){
                            contenidoSMS =  contenidoSMS + mensajeLibre;
                        }
                    }
                }else{
                    registrar = false;
                }
            }
            
            //De acuerdo a tipo de registro, recuperamos datos de destinatario
            if (idCliente>0){
                Cliente clienteDto = new ClienteBO(conn).findClientebyId(idCliente);
                numeroCelularDestinatario = clienteDto.getCelular();
                nombreDestinatario = StringManage.getValidString(clienteDto.getRazonSocial());
                if (nombreDestinatario.length()<=0)
                    nombreDestinatario = StringManage.getValidString(clienteDto.getNombreCliente() + " " + clienteDto.getApellidoPaternoCliente());
            }else if (idEmpleado>0){
                Empleado empleadoDto = new EmpleadoBO(conn).findEmpleadobyId(idEmpleado);
                numeroCelularDestinatario = empleadoDto.getTelefonoLocal();
                nombreDestinatario = StringManage.getValidString(empleadoDto.getNombre()  + " " + empleadoDto.getApellidoPaterno());
            }
            
            if (!gc.isCelularMexico(numeroCelularDestinatario)
                    || StringManage.getValidString(contenidoSMS).length()<=0){
                //no se tiene un numero valido o un contenido valido a enviar
                registrar = false;
            }
            
            if (registrar){
                SmsEnvioSesion smsEnvioSesion = new SmsEnvioSesion();
                smsEnvioSesion.setConn(conn);
                smsEnvioSesion.setAsunto(asunto);
                smsEnvioSesion.setContenido(contenidoSMS);
                smsEnvioSesion.setEnvioInmediato(true);
                smsEnvioSesion.setFechaHoraEnvioProgramado(null);
                SmsEnvioDestinatariosSesion destinatariosSesion = new SmsEnvioDestinatariosSesion();
                    destinatariosSesion.setIdCliente(idCliente);
                    destinatariosSesion.setIdEmpleado(idEmpleado);
                    destinatariosSesion.setNombre(nombreDestinatario);
                    destinatariosSesion.setNumCelular(numeroCelularDestinatario);
                smsEnvioSesion.getListaDestinatarios().add(destinatariosSesion);
                
                int creditosRequeridos = smsEnvioSesion.calculaCreditosSMS();
                
                if (empresaMatrizDto!=null && (empresaMatrizDto.getCreditosSms() < creditosRequeridos)){
                // La empresa no tiene suficientes creditos para el envio del mensaje
                    empresaBO.enviaNotificacionEmpresaSinCreditosSMS(empresaMatrizDto);
                    throw new Exception("La Empresa " + empresaMatrizDto.getRazonSocial() + " (ID: " + empresaMatrizDto.getIdEmpresa() + ") no tiene suficientes creditos SMS para completar la operacion.");
                }
                
                { // TODO: resumir en metodo generico que sirva en JSP y aqui para almacenar desde sesion
                    SmsEnvioLoteDaoImpl smsEnvioLoteDao = new SmsEnvioLoteDaoImpl(conn);
                    SmsEnvioDetalleDaoImpl smsEnvioDetalleDao = new SmsEnvioDetalleDaoImpl(conn);

                    smsEnvioLoteResultado = new SmsEnvioLote();
                    smsEnvioLoteResultado.setIdEmpresa(idEmpresa);
                    smsEnvioLoteResultado.setIdEstatus(1);
                    smsEnvioLoteResultado.setIdSmsPlantilla(smsEnvioSesion.getIdPlantilla());
                    smsEnvioLoteResultado.setFechaHrCaptura(new Date());
                    //smsEnvioLoteResultado.setFechaHrProgramaEnvio(null);
                    smsEnvioLoteResultado.setCantidadDestinatarios(smsEnvioSesion.getListaDestinatarios().size());
                    smsEnvioLoteResultado.setCantidadCreditosSms(creditosRequeridos);
                    smsEnvioLoteResultado.setEnvioInmediato(1);
                    smsEnvioLoteResultado.setIsSmsSistema(1);//Si es auto-creado (ventas, cobranza, factura, etc.)
                    smsEnvioLoteResultado.setIdUsuarioPretoriano(0);
                    //smsEnvioLoteResultado.setIdUsuarioVentas(0);//Solo para sistema de Ventas
                    // se usara un Dispositivo Movil especifico si la Empresa matriz tiene uno configurado
                    smsEnvioLoteResultado.setIdSmsDispositivoMovil(empresaMatrizDto!=null?empresaMatrizDto.getIdSmsDispositivoMovil():0);
                    smsEnvioLoteResultado.setAsunto(smsEnvioSesion.getAsunto());
                    smsEnvioLoteResultado.setMensaje(smsEnvioSesion.creaContenidoSMS());//contenidoSMS);

                    try{
                        smsEnvioLoteDao.insert(smsEnvioLoteResultado);

                        //Después creamos los Detalles (mensajes especificos para cada destinatario) del lote
                        try{
                            SmsEnvioDetalle smsEnvioDetalleDto = new SmsEnvioDetalle();
                            smsEnvioDetalleDto.setIdSmsEnvioLote(smsEnvioLoteResultado.getIdSmsEnvioLote());
                            smsEnvioDetalleDto.setFechaHrCreacion(smsEnvioLoteResultado.getFechaHrCaptura());
                            //smsEnvioDetalleDto.setFechaHrEnvio(null);//no se ha enviado, por lo tanto se deja en null
                            smsEnvioDetalleDto.setEnviado(0);
                            smsEnvioDetalleDto.setIntentosFallidos(0);
                            smsEnvioDetalleDto.setAsunto(smsEnvioLoteResultado.getAsunto());
                            //En lugar de guardar el mismo mensaje en todos los detalles, marcamos que lo herede del Lote padre
                            //smsEnvioDetalleDto.setMensaje(smsEnvioSesion.creaContenidoSMS());
                            smsEnvioDetalleDto.setHeredarMensajeLote(1);
                            smsEnvioDetalleDto.setNumPartesSms(smsEnvioSesion.calculaPartesMensaje());
                            smsEnvioDetalleDto.setNumeroCelular(numeroCelularDestinatario);
                            smsEnvioDetalleDto.setDestIdCliente(idCliente);
                            //smsEnvioDetalleDto.setDestIdProspecto();
                            smsEnvioDetalleDto.setDestIdEmpleado(idEmpleado);
                            //smsEnvioDetalleDto.setDestIdEmpresa();
                            //smsEnvioDetalleDto.setDestIdSmsAgendaDest();
                            smsEnvioDetalleDto.setIdEmpresa(smsEnvioLoteResultado.getIdEmpresa());
                            smsEnvioDetalleDto.setIdEstatus(1);

                            smsEnvioDetalleDao.insert(smsEnvioDetalleDto);
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }

                        //Restamos créditos a cuenta de empresa matriz
                        if (empresaMatrizDto!=null){
                            //Restamos créditos a cuenta de empresa matriz
                            empresaBO.restaCreditosSMSMatriz(empresaMatrizDto.getIdEmpresa(), creditosRequeridos);
                        }

                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
            
        }catch(Exception ex){
            smsEnvioLoteResultado = null;
            ex.printStackTrace();
        }
        
        return smsEnvioLoteResultado;
    }
    
    public static enum TipoNotificaSistema {
        PEDIDO_NUEVO,
        ABONO_NUEVO,
        FACTURA_NUEVO,
        FACTURA_CANCELAR,
        CALLCENTER_NUEVO,
        CALLCENTER_CAMBIO,
        CALLCENTER_CIERRE;
    }
    
    public static class AgrupacionLoteDetalles {
        
        private SmsEnvioLote smsEnvioLote;
        private List<SmsEnvioDetalle> listaDetalles;

        public SmsEnvioLote getSmsEnvioLote() {
            return smsEnvioLote;
        }

        public void setSmsEnvioLote(SmsEnvioLote smsEnvioLote) {
            this.smsEnvioLote = smsEnvioLote;
        }

        public List<SmsEnvioDetalle> getListaDetalles() {
            if (listaDetalles == null)
                listaDetalles = new ArrayList<SmsEnvioDetalle>();
            return listaDetalles;
        }

        public void setListaDetalles(List<SmsEnvioDetalle> listaDetalles) {
            this.listaDetalles = listaDetalles;
        }
        
    }
        
}
