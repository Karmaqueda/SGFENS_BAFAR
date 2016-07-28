/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.config.Configuration;
import com.tsp.sct.dao.dto.BancoOperacion;
import com.tsp.sct.dao.dto.BancoOperacionPk;
import com.tsp.sct.dao.dto.DatosAccesoPagoLinea;
import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.dao.dto.EmpresaPermisoAplicacion;
import com.tsp.sct.dao.jdbc.BancoOperacionDaoImpl;
import com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl;
import com.tsp.sct.mail.TspMailBO;
import com.tsp.sct.util.Base64;
import com.tsp.sct.util.DateManage;
import com.tsp.sct.util.FileManage;
import com.tsp.sct.util.TicketPDF;
import com.tsp.ws.GpWsCard;
import com.tsp.ws.GpWsRequest;
import com.tsp.ws.GpWsResponse;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ISCesarMartinez
 */
public class BancoOperacionBO {
    
    private BancoOperacion bancoOperacion = null;

    public BancoOperacion getBancoOperacion() {
        return bancoOperacion;
    }

    public void setBancoOperacion(BancoOperacion bancoOperacion) {
        this.bancoOperacion = bancoOperacion;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public BancoOperacionBO(Connection conn){
        this.conn = conn;
    }
    
    
     public BancoOperacionBO(long idBancoOperacion, Connection conn){
        this.conn = conn; 
        try{
            BancoOperacionDaoImpl BancoOperacionDaoImpl = new BancoOperacionDaoImpl(this.conn);
            this.bancoOperacion = BancoOperacionDaoImpl.findByPrimaryKey((int)idBancoOperacion);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
     
    public BancoOperacionBO(int idBancoOperacion, Connection conn){
        this.conn = conn;
        try{
            BancoOperacionDaoImpl BancoOperacionDaoImpl = new BancoOperacionDaoImpl(this.conn);
            this.bancoOperacion = BancoOperacionDaoImpl.findByPrimaryKey(idBancoOperacion);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public BancoOperacion findBancoOperacionbyId(int idBancoOperacion) throws Exception{
        BancoOperacion bancoOperacion = null;
        
        try{
            BancoOperacionDaoImpl bancoOperacionDaoImpl = new BancoOperacionDaoImpl(this.conn);
            bancoOperacion = bancoOperacionDaoImpl.findByPrimaryKey(idBancoOperacion);
            if (bancoOperacion==null){
                throw new Exception("No se encontro ningun BancoOperacion que corresponda con los parámetros específicados.");
            }
            if (bancoOperacion.getIdOperacionBancaria()<=0){
                throw new Exception("No se encontro ningun BancoOperacion que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del BancoOperacion del usuario. Error: " + e.getMessage());
        }
        
        return bancoOperacion;
    }
    
    public BancoOperacion findBancoOperacionbyOrderId(String ordenIdBancoOperacion) throws Exception{
        BancoOperacion bancoOperacion = null;
        
        try{
            BancoOperacionDaoImpl bancoOperacionDaoImpl = new BancoOperacionDaoImpl(this.conn);
            BancoOperacion[] bancoOperacionList = bancoOperacionDaoImpl.findWhereBancoOrderIdEquals(ordenIdBancoOperacion);
            bancoOperacion = bancoOperacionList[0];
            if (bancoOperacion==null){
                throw new Exception("No se encontro ningun BancoOperacion que corresponda con los parámetros específicados.");
            }
            if (bancoOperacion.getIdOperacionBancaria()<=0){
                throw new Exception("No se encontro ningun BancoOperacion que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del BancoOperacion del usuario. Error: " + e.getMessage());
        }
        
        return bancoOperacion;
    }
    
    public void updateBD(BancoOperacion dto){
        try{
            new BancoOperacionDaoImpl(this.conn).update(dto.createPk(), dto);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public int idTablaBancoOperacion = 0; //ID de la tabla BancoOperacion insertado
    
    public GpWsResponse pagoLinea(int idUsuario, GpWsCard tarjeta, int idEmpresa, String nombreTitular){
        boolean insertado = false;
		//RECUPERAMOS LOS DATOS DE ACCESO PARA PODER PAGAR:
		DatosAccesoPagoLinea datosAccesoPagoLinea = new DatosAccesoPagoLinea();
		//datosAccesoPagoLinea = getDatosAccesoPagoLineaService().findObject(getLoginAction().getUsuario().getEmpresa().getIdEmpresa());
		//cambio por el registro por usuario los datos de pago en linea y acceso del usuario
                DatosAccesoPagoLineaBO accesoPagoLineaBO = new DatosAccesoPagoLineaBO(this.conn);
                accesoPagoLineaBO.DatosAccesoPagoLineaxEmpresaBO(idEmpresa);
                datosAccesoPagoLinea = accesoPagoLineaBO.getDatosAccesoPagoLinea();		
		GpWsRequest request = new GpWsRequest();
                
                /*GpWsCard tarjeta = new GpWsCard();        
                tarjeta.setNumber("5256780541862394");
                tarjeta.setCvv2Val("472");
                tarjeta.setExpires("04/17");        
                tarjeta.setTotal("0.4");       */
                
		request.setGpWsCard(tarjeta);//DATOS DE LA TARJETA SETEADOS
		request.setUser(datosAccesoPagoLinea.getUsuario());
		request.setPassword(datosAccesoPagoLinea.getPass());
		request.setPin(datosAccesoPagoLinea.getPin());
                
		//COMANTAMOS ESTE CODIGO PARA QUE NO SE REALICE LA TRANSACCIÓN POR MEDIO DEL WEB SERVICE
                //Se ejecuta la transacción
		GpWsResponse resultado = pay(request);
        //Impresión de la respuesta
        String strRes="Petición procesada exitosamente?: "+(resultado.isProcesado()?"Si":"No")+"<br/>";
        strRes+="Pago aprobado?: "+(resultado.isAprobado()?"Si":"No")+"<br/>";
        strRes+=""+resultado.getText()+"<br/><br/>";
        strRes+="Status: "+resultado.getStatus()+"<br/>";
	    strRes+="TimeIn: "+resultado.getTimeIn()+"<br/>";
	    strRes+="TimeOut: "+resultado.getTimeOut()+"<br/>";
	    strRes+="AuthCode: "+resultado.getAuthCode()+"<br/>";
	    strRes+="Total: "+resultado.getTotal()+"<br/>";
	    strRes+="Text: "+resultado.getText()+"<br/>";
	    strRes+="MaxSev: "+resultado.getMaxSev()+"<br/>";
	    strRes+="OrderId: "+resultado.getOrderId()+"<br/>";
	    strRes+="ProcReturnCode: "+resultado.getProcReturnCode()+"<br/>";
	    strRes+="ProcReturnMsg: "+resultado.getProcReturnMsg()+"<br/>";
	    strRes+="CcErrCode: "+resultado.getCcErrCode()+"<br/>";
	    strRes+="CcReturnMsg: "+resultado.getCcReturnMsg()+"<br/>";
	    strRes+="Cvv2Resp: "+resultado.getCvv2Resp()+"<br/>";
	    strRes+="E1: "+resultado.getE1()+"<br/>";
	    strRes+="E2: "+resultado.getE2()+"<br/>";
	    strRes+="E3: "+resultado.getE3()+"<br/>";
	
	    System.out.println(strRes);
	    
	    ///////DESCOMENTAR CUANDO YA ESTE ACTIVO EL WS DE PAGO EN LINEA
	    //PARA LOS MENSAJES DEL TIPO ALERT
	    
	    if(resultado.isAprobado()==true){
	    	System.out.println("Transacción Exitosa: "+resultado.getText());
	    }	    
	    if(resultado.isAprobado()==false){
	    	if(resultado.getText()!=null)
	    		System.out.println("Mesaje porque fue rechazada: "+resultado.getText());
	    	else
	    		System.out.println("Tarjeta Rechazada");	    	
	    }
	    
	    //SI LA TRANSACCION FUE EXITOSA ALMACENAMOS LOS DATOS EN LA BASE DE DATOS    
	    if(resultado.isAprobado()==true){
                BancoOperacion banco = new BancoOperacion();
	    	//Banco banco = new Banco();
	    	banco.setBancoAuth(resultado.getAuthCode());
	    	banco.setBancoOperFecha(resultado.getTimeIn());
	    	banco.setBancoOrderId(resultado.getOrderId());
	    	banco.setNoTarjeta(tarjeta.getNumber());
	    	banco.setMonto(Double.parseDouble(tarjeta.getTotal()));
	    	banco.setIdEstatus(1);
	    	banco.setIdEmpresa(idEmpresa);
	    	banco.setBancoOperType(resultado.getOperType());
	    	banco.setBancoOperIssuingBank(resultado.getIssuingBank());
                banco.setNombreTitular(nombreTitular);
                BancoOperacionDaoImpl bancoOperacionDaoImpl = new BancoOperacionDaoImpl(this.conn);
                try{
                    BancoOperacionPk bancoOperacionPk = bancoOperacionDaoImpl.insert(banco);
                    insertado = true;
                    idTablaBancoOperacion = bancoOperacionPk.getIdOperacionBancaria();
                }catch (Exception e){
                    System.out.println("ERROR PARA GENERAR EL INSERT EN LA TABLA BANCO_OPERACION!!!!!!!!!");
                    insertado = false;
                }
	    }	    
            return resultado;
	    ///////DESCOMENTAR HASTA AKI Y COMENTAR EL CODIGO SIGUIENTE, CUANDO YA ESTE ACTIVO EL WS DE PAGO EN LINEA	  
    }
    
    public GpWsResponse cancelacionLinea(int idUsuario, GpWsCard tarjeta, int idEmpresa){
	boolean insertado = false;	
                //RECUPERAMOS LOS DATOS DE ACCESO PARA PODER CANCELAR:
		DatosAccesoPagoLinea datosAccesoPagoLinea = new DatosAccesoPagoLinea();				
                DatosAccesoPagoLineaBO accesoPagoLineaBO = new DatosAccesoPagoLineaBO(this.conn);
                accesoPagoLineaBO.DatosAccesoPagoLineaxEmpresaBO(idEmpresa);
                datosAccesoPagoLinea = accesoPagoLineaBO.getDatosAccesoPagoLinea();		
		GpWsRequest request = new GpWsRequest();
                
                request.setGpWsCard(tarjeta);//DATOS DE LA TARJETA SETEADOS
		request.setUser(datosAccesoPagoLinea.getUsuario());
		request.setPassword(datosAccesoPagoLinea.getPass());
		request.setPin(datosAccesoPagoLinea.getPin());

                //Se ejecuta la transacción
		GpWsResponse resultado = cancel(request);
                
            //Impresión de la respuesta
            String strRes="Petición procesada exitosamente?: "+(resultado.isProcesado()?"Si":"No")+"<br/>";
            strRes+="Pago aprobado?: "+(resultado.isAprobado()?"Si":"No")+"<br/>";
            strRes+=""+resultado.getText()+"<br/><br/>";
            strRes+="Status: "+resultado.getStatus()+"<br/>";
	    strRes+="TimeIn: "+resultado.getTimeIn()+"<br/>";
	    strRes+="TimeOut: "+resultado.getTimeOut()+"<br/>";
	    strRes+="AuthCode: "+resultado.getAuthCode()+"<br/>";
	    strRes+="Total: "+resultado.getTotal()+"<br/>";
	    strRes+="Text: "+resultado.getText()+"<br/>";
	    strRes+="MaxSev: "+resultado.getMaxSev()+"<br/>";
	    strRes+="OrderId: "+resultado.getOrderId()+"<br/>";
	    strRes+="ProcReturnCode: "+resultado.getProcReturnCode()+"<br/>";
	    strRes+="ProcReturnMsg: "+resultado.getProcReturnMsg()+"<br/>";
	    strRes+="CcErrCode: "+resultado.getCcErrCode()+"<br/>";
	    strRes+="CcReturnMsg: "+resultado.getCcReturnMsg()+"<br/>";
	    strRes+="Cvv2Resp: "+resultado.getCvv2Resp()+"<br/>";
	    strRes+="E1: "+resultado.getE1()+"<br/>";
	    strRes+="E2: "+resultado.getE2()+"<br/>";
	    strRes+="E3: "+resultado.getE3()+"<br/>";
	
	    System.out.println(strRes);
	    
            if(resultado.isAprobado()==true){
	    	System.out.println("Transacción Exitosa: "+resultado.getText());
	    }	    
	    if(resultado.isAprobado()==false){
	    	if(resultado.getText()!=null)
	    		System.out.println("Mesaje porque fue rechazada: "+resultado.getText());
	    	else
	    		System.out.println("Tarjeta Rechazada");	    	
	    }	    
	    
	    //SI LA TRANSACCION FUE EXITOSA ALMACENAMOS LOS DATOS EN LA BASE DE DATOS   
            
            if(resultado.isAprobado()==true){
                //BUSCAMOS SI EL ORDER ID EXISTE, SI SI, OBTENERMOS SUS DATOS Y ACTUALIZAMOS ESE REGITRO PARA CANCELARLO
                try{
                BancoOperacion banco = new BancoOperacion();
	    	BancoOperacionBO bancoOperacionBO = new BancoOperacionBO(this.conn);
                banco = bancoOperacionBO.findBancoOperacionbyOrderId(tarjeta.getOrderId());
                
	    	banco.setBancoAuth(resultado.getAuthCode());//ACTUALIZAMOS SU CONDIGO DE AUTORIZACION DEL BANCO
                banco.setIdEstatus(2);//ACTUALIZAMOS EL ESTATUS
	    	banco.setBancoOperFecha(resultado.getTimeOut());
	    	banco.setBancoOrderId(resultado.getOrderId());
	    	banco.setNoTarjeta(tarjeta.getNumber());
	    	banco.setMonto(Double.parseDouble(tarjeta.getTotal()));
	    	
	    	banco.setIdEmpresa(idEmpresa);
	    	banco.setBancoOperType(resultado.getOperType());
	    	banco.setBancoOperIssuingBank(resultado.getIssuingBank());
                BancoOperacionDaoImpl bancoOperacionDaoImpl = new BancoOperacionDaoImpl(this.conn);                
                    //bancoOperacionDaoImpl.insert(banco);
                bancoOperacionDaoImpl.update(banco.createPk(),banco);
                    insertado = true;
                }catch (Exception e){
                    System.out.println("ERROR PARA GENERAR ");
                    insertado = false;
                }
	    }
            return resultado;
	}
    
    /**
     * Realiza una búsqueda por ID BancoOperacion en busca de
     * coincidencias
     * @param idMarca ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public BancoOperacion[] findBancoOperaciones(int idBancoOperacion, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        BancoOperacion[] bancoOperacionDto = new BancoOperacion[0];
        BancoOperacionDaoImpl bancoOperacionDao = new BancoOperacionDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idBancoOperacion>0){
                sqlFiltro ="ID_OPERACION_BANCARIA=" + idBancoOperacion + " AND ";
            }else{
                sqlFiltro ="ID_OPERACION_BANCARIA>0 AND";
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
            
            bancoOperacionDto = bancoOperacionDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_OPERACION_BANCARIA DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return bancoOperacionDto;
    }
    
    
    public void enviarTicket(String destinatario, long idOperacionBancaria, int idEmpresa) throws Exception{
        //Validamos si es clienete de EVC        
        EmpresaPermisoAplicacion empresaPermisoDto = null;
        try{
            EmpresaPermisoAplicacionDaoImpl  empresaPermisoDao = new EmpresaPermisoAplicacionDaoImpl();
            empresaPermisoDto = empresaPermisoDao.findWhereIdEmpresaEquals(idEmpresa)[0];
        }catch(Exception e){e.printStackTrace();}    
            
                
        try {
            TspMailBO mailBO = new TspMailBO();
            
            if(empresaPermisoDto.getAccesoSgfensPretomovil()==1){
                mailBO.setConfigurationMovilpyme();
                mailBO.addMessageMovilpyme("<b>¡Buen día!</b> "
                        + "<br/>¡Saludos!"
                        + "<br/>El presente correo contiene adjunto el Ticket del comprobante bancario. "
                        ,1);
            } else{
                mailBO.setConfiguration();   
                mailBO.addMessage("<b>¡Buen día!</b> "
                        + "<br/>¡Saludos!"
                        + "<br/>El presente correo contiene adjunto el Ticket del comprobante bancario. "
                        ,1);
            }            
            
            System.out.println("CORREOS DESTINO: "+destinatario);
            
            //mailBO.addTo(destinatario, destinatario);

            String[] listaDestinatarios;
            if (destinatario!=null){
                listaDestinatarios = destinatario.split(",");

                for (String dest : listaDestinatarios) {
                    try {
                        if (!"".equals(dest.trim()))
                            mailBO.addTo(dest, dest);
                    } catch (Exception e) {
                        System.out.println("No se pudo agregar el destinatario: " + dest + e.getMessage());
                    }
                }
            }

            //mailBO.addMessage("¡Saludos! <br/> Correo generado automaticamente para el envio del Ticket del comprobante bancario",1);
            
            
            /////convertimos el archivo a padf:
            //DeHTMLaPDF dhtmlpdf = new DeHTMLaPDF();
            //String rutaPDF = dhtmlpdf.convertidor(contenido);
            //File pdf = new File(rutaPDF);
            //adjuntos.add(pdf);
            
            TicketPDF ticketPDF = new TicketPDF();
            String archivoPDF = ticketPDF.armaPDF(idEmpresa, idOperacionBancaria);
            //Thread.sleep(8000);
            File pdf = new File(archivoPDF);            
            List<File> archivosAdjuntar = new ArrayList<File>();
            archivosAdjuntar.add(pdf);
            /////
            
            //En caso de tener adjuntos, los agregamos
            if (archivosAdjuntar!=null){
                if(archivosAdjuntar.size()>0){
                for (File adjunto:archivosAdjuntar){
                    System.out.println("------------------------------ADJUNTANDO PDF");
                    mailBO.addFile(adjunto.getAbsolutePath(), adjunto.getName());
                }}
            }
            
            mailBO.send("Comprobante de Pago");
            //BORRAMOS EL ARCHIVO
            /*
            Thread.sleep(12000);
            try{
                File pdfBorrar = new File(archivoPDF);
                pdfBorrar.delete();
            }catch(Exception e){System.out.println("No se pudo borrar el archivo, error: "+e.getMessage());}
              */      
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al intentar enviar los correos de notificación. " + e.getMessage());
        }
    }
    
    public String getPrintableCardNumber(){
        String printableCardNumber ="XXXX XXXX XXXX ";
        try{
           printableCardNumber += this.bancoOperacion.getNoTarjeta().substring(12);
        }catch(Exception ex){}
        return printableCardNumber;
    }
    
    public File crearArchivoImagenExtraFromBase64(long idEmpresa, String imagenBase64, boolean isIdentificacion) {
        File archivoImagen = null;

        if (imagenBase64 != null) {
            if (imagenBase64.trim().length() > 0) {

                String rfcEmpresaMatriz = "";
                try {
                    Empresa empresaMatrizDto = new EmpresaBO(this.conn).getEmpresaMatriz(idEmpresa);
                    rfcEmpresaMatriz = empresaMatrizDto.getRfc();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }

                try {
                    Configuration appConfig = new Configuration();
                    String ubicacionImagenesAbonos = appConfig.getApp_content_path() + rfcEmpresaMatriz + "/abonos/images/";

                    //Convertimos bytes en base64 a una imagen y la almacenamos en servidor
                    byte[] bytesImagen = Base64.decode(imagenBase64);

                    String nombreArchivoImagen = "";
                    if (isIdentificacion) {
                        nombreArchivoImagen = "img_identificacion_" + DateManage.getDateHourString() + ".jpg";
                    } else {
                        nombreArchivoImagen = "img_tdc_" + DateManage.getDateHourString() + ".jpg";
                    }

                    archivoImagen = FileManage.createFileFromByteArray(bytesImagen, ubicacionImagenesAbonos, nombreArchivoImagen);

                    if (!archivoImagen.exists()) {
                        archivoImagen = null;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    archivoImagen = null;
                }
            }
        }

        return archivoImagen;
    }

    /*Metodo para el envio a pago*/
     private static GpWsResponse pay(com.tsp.ws.GpWsRequest gpWsRequest) {
        com.tsp.ws.GpWs_Service service = new com.tsp.ws.GpWs_Service();
        com.tsp.ws.GpWs port = service.getGpWsPort();
        return port.pay(gpWsRequest);
    }

     /*Método para la cancelacion de una transacción*/
    private static GpWsResponse cancel(com.tsp.ws.GpWsRequest gpWsRequest) {
        com.tsp.ws.GpWs_Service service = new com.tsp.ws.GpWs_Service();
        com.tsp.ws.GpWs port = service.getGpWsPort();
        return port.cancel(gpWsRequest);
    }
    
}
