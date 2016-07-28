/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.servlet;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.infosoft.fechas.FormateadorDeFechas;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
import com.tsp.sct.beans.FacturaDescripcion;
import com.tsp.sct.beans.Impuesto;
import com.tsp.sct.bo.CertificadoDigitalBO;
import com.tsp.sct.bo.ClienteBO;
import com.tsp.sct.bo.ComprobanteDescripcionPersonalizadaBO;
import com.tsp.sct.bo.ComprobanteFiscalBO;
import com.tsp.sct.bo.DatosPersonalizadosBO;
import com.tsp.sct.bo.EmpresaBO;
import com.tsp.sct.bo.ImagenPersonalBO;
import com.tsp.sct.bo.NominaComprobanteDescripcionConcepDeducPerceHorasIncapaBO;
import com.tsp.sct.bo.NominaDepartamentoBO;
import com.tsp.sct.bo.NominaEmpleadoBO;
import com.tsp.sct.bo.NominaPeriodicidadPagoBO;
import com.tsp.sct.bo.NominaPuestoBO;
import com.tsp.sct.bo.NominaRegistroPatronalBO;
import com.tsp.sct.bo.TipoComprobanteBO;
import com.tsp.sct.bo.UbicacionBO;
import com.tsp.sct.bo.UsuarioBO;
import com.tsp.sct.config.Configuration;
import com.tsp.sct.dao.dto.Aduana;
import com.tsp.sct.dao.dto.CertificadoDigital;
import com.tsp.sct.dao.dto.Cliente;
import com.tsp.sct.dao.dto.ComprobanteDescripcion;
import com.tsp.sct.dao.dto.ComprobanteDescripcionPersonalizada;
import com.tsp.sct.dao.dto.ComprobanteFiscal;
import com.tsp.sct.dao.dto.ComprobanteImpuesto;
import com.tsp.sct.dao.dto.ComprobanteImpuestoXConcepto;
import com.tsp.sct.dao.dto.DatosPersonalizados;
import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.dao.dto.Folios;
import com.tsp.sct.dao.dto.FormaPago;
import com.tsp.sct.dao.dto.ImagenPersonal;
import com.tsp.sct.dao.dto.ImpuestoPorConcepto;
import com.tsp.sct.dao.dto.NominaComprobanteDescripcion;
import com.tsp.sct.dao.dto.NominaComprobanteDescripcionPercepcionDeduccion;
import com.tsp.sct.dao.dto.NominaDepartamento;
import com.tsp.sct.dao.dto.NominaEmpleado;
import com.tsp.sct.dao.dto.NominaPeriodicidadPago;
import com.tsp.sct.dao.dto.NominaPuesto;
import com.tsp.sct.dao.dto.NominaRegistroPatronal;
import com.tsp.sct.dao.dto.TipoDeMoneda;
import com.tsp.sct.dao.dto.TipoPago;
import com.tsp.sct.dao.dto.Ubicacion;
import com.tsp.sct.dao.jdbc.AduanaDaoImpl;
import com.tsp.sct.dao.jdbc.ComprobanteDescripcionDaoImpl;
import com.tsp.sct.dao.jdbc.ComprobanteDescripcionPersonalizadaDaoImpl;
import com.tsp.sct.dao.jdbc.ComprobanteImpuestoDaoImpl;
import com.tsp.sct.dao.jdbc.ComprobanteImpuestoXConceptoDaoImpl;
import com.tsp.sct.dao.jdbc.DatosPersonalizadosDaoImpl;
import com.tsp.sct.dao.jdbc.EmpresaDaoImpl;
import com.tsp.sct.dao.jdbc.FoliosDaoImpl;
import com.tsp.sct.dao.jdbc.FormaPagoDaoImpl;
import com.tsp.sct.dao.jdbc.ImpuestoDaoImpl;
import com.tsp.sct.dao.jdbc.ImpuestoPorConceptoDaoImpl;
import com.tsp.sct.dao.jdbc.NominaComprobanteDescripcionDaoImpl;
import com.tsp.sct.dao.jdbc.NominaComprobanteDescripcionPercepcionDeduccionDaoImpl;
import com.tsp.sct.dao.jdbc.NominaRegistroPatronalDaoImpl;
import com.tsp.sct.dao.jdbc.TipoDeMonedaDaoImpl;
import com.tsp.sct.dao.jdbc.TipoPagoDaoImpl;
import com.tsp.sct.util.DateManage;
import com.tsp.sct.util.FacturacionUtil;
import com.tsp.sct.util.FileManage;
import com.tsp.sct.util.FormatUtil;
import com.tsp.sct.util.StringManage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author Leonardo
 */

public class GeneradorPDFs2 extends HttpServlet {
    
    private int idComprobanteFiscal;
    //private final int ID_COMPROBANTE_FACTURACBB = 13;
    private final int ID_COMPROBANTE_FACTURA = 1;
    private final int ID_COMPROBANTE_FACTURA_CARTA_PORTE = 2;
    private final int ID_COMPROBANTE_FACTURA_CON_IMPUESTOS = 3;
    private final int ID_COMPROBANTE_FACTURA_NOMINA = 40;
    
    private final int ID_COMPROBANTE_CARTA_PORTE = 1000;
    
    private static final int ID_FACTURA_COTIZACION = 2;
    
    private static final String VERSION_TIMBRADO = "1.0";
    
    private ComprobanteFiscalBO bO = null;
    private ComprobanteFiscal cf = null;
    private ComprobanteDescripcion[] cds = null;
    private FacturaDescripcion facturaDescripcion = null;
    private ComprobanteDescripcion comprobanteDescripcion = null;
    List<FacturaDescripcion> resultado = null;
    List<NominaComprobanteDescripcionConcepDeducPerceHorasIncapaBO> resultadoNomina = null;
    private NominaComprobanteDescripcionConcepDeducPerceHorasIncapaBO nominaCompDesc = null;
    //private NominaComprobanteDescripcionPercepcionDeduccion nominaPercepcionDeduccion = null;
    private NominaComprobanteDescripcionPercepcionDeduccion[] nominaPercepcionDeduccionList = null;
    private ComprobanteImpuesto[] ci = null;
    private List<Impuesto> listImpuestos = null;
    private Cliente cliente = null;
    private Cliente remitente = null;
    private Cliente consignatario = null;
    private NominaEmpleado nomEmpleado = null;
    private Folios folio = null;
    private Empresa empresa = null;
    private Empresa empresaPadre = null;
    private Ubicacion ubicacion = null;
    private Ubicacion ubicacionEmpresaPadre = null;
    private ImagenPersonal ip = null;
    //private Cbb codigo = null;
    private TipoDeMoneda tdm = null;
    private TipoPago tp = null;
    private FormaPago fp = null;
    private CertificadoDigital certificadoDigital = null;
    
    private NominaDepartamento nomDepartamento = null;
    private NominaPuesto nomPuesto = null;
    private NominaComprobanteDescripcion nomComDes = null;
    
    private String rfcReceptor = "";
    private String representacionImpresa = ""; //VARIABLE PARA VER QUE NOMBRE DE REPRESENTACION IMPRESA SALDRA (JASPER); ESTOS PUEDEN SER: Factura_carta , Nomina_carta, Factura_carta_ImpXconc, Factura_carta_RfcEmisor , Nomina_carta_RfcEmisor, Factura_carta_ImpXconc_RfcEmisor
    
    List<ComprobanteDescripcionPersonalizadaBO.CampoPersonalizado> camposPersonalizados = new ArrayList<ComprobanteDescripcionPersonalizadaBO.CampoPersonalizado>();
    
    private NominaRegistroPatronal[] registroPatronalsDto = null;
    private NominaRegistroPatronal registroPatronalEmpresaEnSesion = null;
    
    //private NominaPeriodicidadPago nomPeriodicidadPago = null;

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("EEEEEEEEEEEEEENNNNNNNNNNTRO A SERVLET");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();        
        try {
           //  TODO output your page here. You may use following sample code. 
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet GeneradorPDFs</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GeneradorPDFs at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        try {            
			idComprobanteFiscal = Integer.parseInt(request.getParameter("idComprobanteFiscal"));                        
                        System.out.println("IDENTIIFICADOR DE COMPROBANTE: "+idComprobanteFiscal);
                        String accion = request.getParameter("accion");
                        System.out.println("ACCION: "+accion);                       
                
                        //generamos la carga inicial:
                        getFactura();                        
                        
                        if("correo".equals(accion)){//para ver si lo que retorna es de correo o de vista previa
                            byte[] bytes = getFactura(request);
                            getFacturaCorreo(bytes);
                            
                            UsuarioBO bO = new UsuarioBO();
                            ComprobanteFiscalBO cfbo = new ComprobanteFiscalBO(idComprobanteFiscal, bO.getConn());
                            cfbo.generacionEnvioArhivosCorreo();
                            try{
                                bO.getConn().close();
                            }catch(Exception e){}
                                    
                            
                            response.setContentType("text/html;charset=UTF-8");
                            PrintWriter out = response.getWriter();
                            try {
                                //  TODO output your page here. You may use following sample code. 
                                 out.println("<html>");
                                 out.println("<head>");
                                 out.println("<title>Servlet GeneradorPDFs</title>");            
                                 out.println("</head>");
                                 out.println("<body>");
                                 //out.println("<h1>Correos enviados " + request.getContextPath() + "</h1>");
                                 out.println("<h1>Correos enviados </h1>");
                                 out.println("</body>");
                                 out.println("</html>");
                             } finally {            
                                 out.close();
                             }
                            
                        }else{//para servlet de tipo vista previa
                            System.out.println("--------------------***************:  entro a IMPRIMIR PDF");
				byte[] bytes = getFactura(request);
				response.setContentType("application/pdf");
				response.setContentLength(bytes.length);
				ServletOutputStream outs = response.getOutputStream();
				outs.write(bytes, 0, bytes.length);

				ServletOutputStream out = response.getOutputStream();
				//List listaPariticipantes = new ArrayList();
				response.addHeader("content-disposition", "attachment;filename=C:/pack/PDFparaFactura" + outs + ".pdf");
				outs.flush();
				outs.close();
                                
                                try{
                                //ESCRIBIMOS EN DISCO EL ARCHIVO PDF:
                                getFacturaCorreo(bytes);
                                }catch(Exception e){System.out.println("--------------------***************:NO SE PUDO ALMACENAR EL PDF");e.printStackTrace();};
                        }    
                        
                    } catch (Exception e) {
                    e.printStackTrace();
		}
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    public void getFacturaCorreo(byte[] bytes){
        try{
            ///**Para escribir el pdf en el disco:
            //System.out.println("ESCRIBIMOS LOS BYTES COMO UN ARCHIVO.... ");
            Configuration appConfig = new Configuration();
            //PARA SABER EL NOMBRE DEL XML Y PONERSELO AL PDF
            String nombreArchivo = cf.getArchivoCfd();
            String soloNombreArchivo;
            StringTokenizer tokens = new StringTokenizer(nombreArchivo,".");   
            soloNombreArchivo = tokens.nextToken().intern(); 
            
            UsuarioBO ubo = new UsuarioBO();            
            Cliente clienteDto = null;            
            
            String rfcDestinatario = "";
            if(cf.getIdTipoComprobante() == TipoComprobanteBO.TIPO_NOMINA){
                NominaEmpleado nomEmpleadoDto = new NominaEmpleadoBO(ubo.getConn(), cf.getIdCliente()).getNominaEmpleado();
                rfcDestinatario = nomEmpleadoDto.getRfc();                
            }else{
                clienteDto = new ClienteBO(cf.getIdCliente(), ubo.getConn()).getCliente();
                rfcDestinatario = clienteDto.getRfcCliente();
            }

            String rutaArchivoEnc = appConfig.getApp_content_path() + empresa.getRfc() + "/cfd/emitidos/"
            +TipoComprobanteBO.getTipoComprobanteNombreCarpeta(cf.getIdTipoComprobante()) 
            +"/" + rfcDestinatario
            + "/"; 
            
            ubo.getConn().close();
            //rutaArchivoEnc = java.net.URLEncoder.encode(rutaArchivoEnc, "UTF-8");
            
            //System.out.println("ruta de escritura de pdf: "+rutaArchivoEnc);

            FileManage fileManage = new FileManage();
            fileManage.createFileFromByteArray(bytes, rutaArchivoEnc, soloNombreArchivo+".pdf");                                
            ///**
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    private void getFactura() throws ServletException, IOException{
        /////////////Negocio de la factura
        try{
            boolean impuestoPorComprobante = false;//VARIABLE PARA VER SI HACEMOS LOS CALCULOS DE LOS IMUESTOS POR CONCEPTO O POR COMPROBANTE
                //creamos la conexión de la base de datos:
                UsuarioBO usuario = new UsuarioBO();
                cf = new ComprobanteFiscal();
                bO = new ComprobanteFiscalBO(idComprobanteFiscal, usuario.getConn());
                cf =  bO.getComprobanteFiscal();
                
                //Obtenemos la empresa
                empresa = new Empresa();
                EmpresaDaoImpl empresaDaoImpl = new EmpresaDaoImpl(usuario.getConn());
                empresa = empresaDaoImpl.findByPrimaryKey(cf.getIdEmpresa());
                
                //obtenemos la empresa padre:
                try{
                empresaPadre = new EmpresaBO(usuario.getConn()).getEmpresaMatriz(cf.getIdEmpresa());
                        //empresaDaoImpl.findWhereIdEmpresaPadreEquals(empresa.getIdEmpresaPadre())[0];
                }catch(Exception e){System.out.println("--- ERROR AL OBTENER EMPRESA PADRE");}
                                
                //Obtenermos la ubicacion de la empresa
                ubicacion = new Ubicacion();
                UbicacionBO ubicacionBO = new UbicacionBO(empresa.getIdUbicacionFiscal(), usuario.getConn());
                ubicacion = ubicacionBO.getUbicacion();
                
                //Obtenermos la ubicacion de la empresa padre
                try{
                    ubicacionBO = new UbicacionBO(empresaPadre.getIdUbicacionFiscal(), usuario.getConn());
                    ubicacionEmpresaPadre = ubicacionBO.getUbicacion();
                }catch(Exception e){System.out.println("--- ERROR AL OBTENER UBICACION EMPRESA PADRE");}
                
                cds = new ComprobanteDescripcion[0];     
                listImpuestos = new ArrayList<Impuesto>();
                //todos los conceptos
                ComprobanteDescripcionDaoImpl comprobanteDescripcionDaoImpl = new ComprobanteDescripcionDaoImpl(usuario.getConn());
                cds = comprobanteDescripcionDaoImpl.findWhereIdComprobanteFiscalEquals(idComprobanteFiscal);
                
                if(cf.getIdTipoComprobante() == TipoComprobanteBO.TIPO_NOMINA){//si el comprobante es de nomina:
                    resultadoNomina = new ArrayList<NominaComprobanteDescripcionConcepDeducPerceHorasIncapaBO>();
                    
                    //obtenemos los datos de la tabla de Comprobante_Descripcion:
                    comprobanteDescripcion = new ComprobanteDescripcion();
                    if(cds != null){
                        for(int y = 0; y < cds.length; y++){
                            nominaCompDesc = new NominaComprobanteDescripcionConcepDeducPerceHorasIncapaBO();
                            comprobanteDescripcion = cds[y];
                            nominaCompDesc.setIdTipoConcepto(1);
                            nominaCompDesc.setIdComprobanteDescripcion(comprobanteDescripcion.getIdComprobanteDescripcion());
                            nominaCompDesc.setNombre(comprobanteDescripcion.getNombreConcepto());
                            nominaCompDesc.setDescripcion(comprobanteDescripcion.getDescripcion());
                            nominaCompDesc.setPrecio(comprobanteDescripcion.getPrecioUnitario());
                            nominaCompDesc.setIdentificacion(comprobanteDescripcion.getIdentificacion());                           
                            resultadoNomina.add(nominaCompDesc);
                        }
                    }
                    
                    //obtenemos los datos de la tabla de nomina_Comprobante_Descripcion_Percepcion_deduccion:
                    nominaPercepcionDeduccionList = new NominaComprobanteDescripcionPercepcionDeduccion[0];
                    NominaComprobanteDescripcionPercepcionDeduccionDaoImpl percepcionDeduccionDaoImpl = new NominaComprobanteDescripcionPercepcionDeduccionDaoImpl(usuario.getConn());
                    nominaPercepcionDeduccionList = percepcionDeduccionDaoImpl.findWhereIdCromprobanteFiscalEquals(idComprobanteFiscal);
                    if(nominaPercepcionDeduccionList != null){
                        for(NominaComprobanteDescripcionPercepcionDeduccion nomPercepcionDeduccion : nominaPercepcionDeduccionList){
                            nominaCompDesc = new NominaComprobanteDescripcionConcepDeducPerceHorasIncapaBO();                            
                            if(nomPercepcionDeduccion.getIdPercepcionDeduccion()==1){//validamos si es percepcion
                                nominaCompDesc.setIdTipoConcepto(2);//mandamos percepcion
                                nominaCompDesc.setIdComprobanteDdescripcionPercepcion(nomPercepcionDeduccion.getIdNominaComprobanteDescripcion());
                                nominaCompDesc.setNombreDescripcionPercepcion(nomPercepcionDeduccion.getConceptoDescripcion());
                                nominaCompDesc.setClavePercepcion(nomPercepcionDeduccion.getClavePatron());
                                nominaCompDesc.setImporteGravadoPercepcion(nomPercepcionDeduccion.getImporteGravado());
                                nominaCompDesc.setImporteExentoPercepcion(nomPercepcionDeduccion.getImporteExcepto());
                                resultadoNomina.add(nominaCompDesc);
                            }else if(nomPercepcionDeduccion.getIdPercepcionDeduccion()==2){//validamos si es deduccion
                                nominaCompDesc.setIdTipoConcepto(3);//mandamos deduccion
                                nominaCompDesc.setIdComprobanteDdescripcionDeduccion(nomPercepcionDeduccion.getIdNominaComprobanteDescripcion());
                                nominaCompDesc.setNombreDescripcionDeduccion(nomPercepcionDeduccion.getConceptoDescripcion());
                                nominaCompDesc.setClaveDeduccion(nomPercepcionDeduccion.getClavePatron());
                                nominaCompDesc.setImporteGravadoDeduccion(nomPercepcionDeduccion.getImporteGravado());
                                nominaCompDesc.setImporteExentoDeduccion(nomPercepcionDeduccion.getImporteExcepto());
                                resultadoNomina.add(nominaCompDesc);
                            }   
                        }
                        
                    }
                    
                    //obtenemos los datos de la tabla de nomina_comprobante_descricion_incapacida
                    
                    //obtenemos los datos de la tabla de nomina_comprobante_descripcion_horas_extra
                    
                    //recperamos al empleado
                    nomEmpleado = new NominaEmpleadoBO(usuario.getConn(), cf.getIdCliente()).getNominaEmpleado();
                    
                    nomDepartamento = new NominaDepartamentoBO(nomEmpleado.getIdDepartamento(), usuario.getConn()).getNominaDepartamento();
                    nomPuesto = new NominaPuestoBO(nomEmpleado.getIdPuesto(), usuario.getConn()).getNominaPuesto();
                    //nomPeriodicidadPago = new NominaPeriodicidadPagoBO(nomEmpleado.get, null)
                    NominaComprobanteDescripcion[] nomComDesList = new NominaComprobanteDescripcionDaoImpl().findWhereIdCromprobanteFiscalEquals(cf.getIdComprobanteFiscal());
                    nomComDes = nomComDesList[0];
                    
                    rfcReceptor = nomEmpleado.getRfc();//este para pasar quien es el receptor y generar el codigo de barras bidimensional (QR)
                    
                    //cargamos los datos del registro patronal:
                    try{
                        NominaRegistroPatronalBO registroPatronalBO = new NominaRegistroPatronalBO(usuario.getConn());
                        registroPatronalsDto = registroPatronalBO.findNominaRegistroPatronal(0, cf.getIdEmpresa() , 0, 0, "");
                    }catch(Exception e){}
                    try{
                        registroPatronalEmpresaEnSesion = new NominaRegistroPatronalDaoImpl(usuario.getConn()).findWhereIdEmpresaEquals(cf.getIdEmpresa())[0];
                    }catch(Exception e){}
                                        
                }else{//si es otro tipo de comprobante fiscal
                    resultado = new ArrayList<FacturaDescripcion>();                                
                    comprobanteDescripcion = new ComprobanteDescripcion();

                    Aduana[] aduanas = null;
                    AduanaDaoImpl aduanaDaoImpl = new AduanaDaoImpl(usuario.getConn());
                    
                    if(cds != null){
                        for(int y = 0; y < cds.length; y++){
                            facturaDescripcion = new FacturaDescripcion();
                            comprobanteDescripcion = cds[y];
                            facturaDescripcion.setIdFacturaDescripcion(comprobanteDescripcion.getIdComprobanteDescripcion());
                            facturaDescripcion.setIdFactura(comprobanteDescripcion.getIdComprobanteFiscal());
                            facturaDescripcion.setCantidad(comprobanteDescripcion.getCantidad());
                            facturaDescripcion.setIdConcepto(comprobanteDescripcion.getIdConcepto());
                            //facturaDescripcion.setDescripcion(comprobanteDescripcion.getDescripcion());
                                //RECUPERAMOS LOS DATOS DE LA INFORMACION ADUANERA DEL CONCEPTO
                                try{
                                    aduanas = null;
                                    aduanas = aduanaDaoImpl.findWhereIdComprobanteDescripcionEquals(comprobanteDescripcion.getIdComprobanteDescripcion());
                                    if(aduanas != null){
                                        String aduanasString = "\n";
                                        for(Aduana adu : aduanas){
                                            System.out.println("////// Cargando aduana . . . ");
                                            aduanasString += "\n " + (adu.getAduana()!=null?"Aduana: "+adu.getAduana():"") + " " + (adu.getNumDocumento()!=null?", Num. "+adu.getNumDocumento():"") + " " + (adu.getFechaExpedicion()!=null?", Fecha: "+DateManage.formatDateToNormal(adu.getFechaExpedicion()):"");
                                        }
                                        facturaDescripcion.setDescripcion(comprobanteDescripcion.getDescripcion() + aduanasString);
                                    }
                                }catch(Exception e){
                                    System.out.println("////// Exception :");
                                    e.printStackTrace();
                                    facturaDescripcion.setDescripcion(comprobanteDescripcion.getDescripcion());
                                }
                            facturaDescripcion.setPrecio(comprobanteDescripcion.getPrecioUnitario());
                            facturaDescripcion.setUnidad(comprobanteDescripcion.getUnidad());
                            facturaDescripcion.setIdentificacion(comprobanteDescripcion.getIdentificacion());
                            facturaDescripcion.setMontoDescuento(comprobanteDescripcion.getDescuentoMonto());
                            facturaDescripcion.setPorcentajeDescuento(comprobanteDescripcion.getDescuentoPorcentaje());
                            /*if(comprobanteDescripcion.getNombreConcepto()==null){                            
                                facturaDescripcion.setNombreConcepto(comprobanteDescripcion.getNombreConcepto());
                            }else{
                                facturaDescripcion.setNombreConcepto(comprobanteDescripcion.getNombreConcepto());
                            }*/
                            resultado.add(facturaDescripcion);
                            
                            if(comprobanteDescripcion.getIdImpuesto()>0 && (cf.getIdTipoComprobante()==TipoComprobanteBO.TIPO_FACTURA_IMPUESTO_X_CONCEPTO ) ){                            
                                System.out.println("______IMPUESTOS POR CONCEPTO . . .");
                                calculoDeImpuestos(usuario);
                            }else{
                                impuestoPorComprobante = true;
                            }
                            }
                    }
                    
                    //Obtenemos el cliente
                    //cliente = new Cliente();
                    cliente = new ClienteBO(cf.getIdCliente(), usuario.getConn()).getCliente();
                    rfcReceptor = cliente.getRfcCliente();
                }
                
                if(impuestoPorComprobante){
                    System.out.println("________- IMPUESTOS POR COMPROBANTE");
                    //todos los impuestos
                    ci = new ComprobanteImpuesto[0];
                    ComprobanteImpuestoDaoImpl comprobanteImpuestoDaoImpl = new ComprobanteImpuestoDaoImpl(usuario.getConn());
                    ci = comprobanteImpuestoDaoImpl.findWhereIdComprobanteFiscalEquals(idComprobanteFiscal);
                    ComprobanteImpuesto comImpuestos = new ComprobanteImpuesto();
                    com.tsp.sct.dao.dto.Impuesto impuestosSCT = new com.tsp.sct.dao.dto.Impuesto();
                    ImpuestoDaoImpl daoImpl = new ImpuestoDaoImpl(usuario.getConn());

                    //listImpuestos = new ArrayList<Impuesto>();
                    Impuesto impPDF = null;
                    if(ci != null){
                        for(int x = 0 ; x < ci.length ; x++){
                            impPDF = new Impuesto();
                            comImpuestos = ci[x];
                            impuestosSCT = daoImpl.findByPrimaryKey(comImpuestos.getIdImpuesto());

                            impPDF.setIdImpuesto(impuestosSCT.getIdImpuesto());
                            impPDF.setIdEmpresa(impuestosSCT.getIdEmpresa());
                            impPDF.setNombre(impuestosSCT.getNombre());
                            impPDF.setDescripcion(impuestosSCT.getDescripcion());
                            impPDF.setPorcentaje(impuestosSCT.getPorcentaje());
                            impPDF.setTrasladado(impuestosSCT.isTrasladadoNull());
                            //Calculo de totales:
                            //impPDF.setTotal(impuestosSCT.getPorcentaje() * .01 * (cf.getImporteSubtotal() - cf.getDescuento()));
                            impPDF.setTotal(impuestosSCT.getPorcentaje() * .01 * (cf.getImporteSubtotal() - (cf.getDescuento()*0.01*cf.getImporteSubtotal())));                            
                            impPDF.setIdEstatus(impuestosSCT.getIdEstatus());
                            impPDF.setImpuestoLocal(impuestosSCT.isImpuestoLocalNull());
                            listImpuestos.add(impPDF);
                        }
                    }         
                }
                
                
                //folio y serie                
                /*folio = new Folios();
                if(cf.getFolioGenerado()!=""){
                    FoliosDaoImpl foliosDaoImpl = new FoliosDaoImpl();                
                    folio = foliosDaoImpl.findByPrimaryKey(cf.getIdFolio());
                }*/                
			
                //Recuperamos los datos de la imagen personal
                ip = new ImagenPersonal();
                ImagenPersonalBO imagenPersonalBO = new ImagenPersonalBO(usuario.getConn());
                try{
                    ip = imagenPersonalBO.findImagenPersonalByEmpresa(empresa.getIdEmpresa());
                }catch(Exception e){ip= null;}
                
                //Recuperamos el CBB
                /*codigo = new Cbb();
                CbbBO cbbBO = new CbbBO();
                codigo = cbbBO.findCbbByEmpresa(empresa.getIdEmpresa());*/
                
                //recuperamos el tipo de moneda
                tdm = new TipoDeMoneda();
                TipoDeMonedaDaoImpl tipoDeMonedaDaoImpl = new TipoDeMonedaDaoImpl(usuario.getConn());
                tdm = tipoDeMonedaDaoImpl.findByPrimaryKey(cf.getIdTipoMoneda());
                
                //recuperamos el tipo de Pago
                tp = new TipoPago();
                TipoPagoDaoImpl tipoPagoDaoImpl = new TipoPagoDaoImpl(usuario.getConn());
                tp = tipoPagoDaoImpl.findByPrimaryKey(cf.getIdTipoPago());
                
                //recuperamos la forma de Pago
                fp = new FormaPago();
                FormaPagoDaoImpl formaPagoDaoImpl = new FormaPagoDaoImpl(usuario.getConn());
                fp = formaPagoDaoImpl.findByPrimaryKey(cf.getIdFormaPago());
                
                //OBTENERMOS CERTIFICADO:
                CertificadoDigitalBO cdbo = new CertificadoDigitalBO(usuario.getConn());                
                certificadoDigital = cdbo.findCertificadoByEmpresa(cf.getIdEmpresa());
                
                //OBTENEMOS SERIE:                
                if(cf.getIdFolio()>0)
                    folio = new FoliosDaoImpl(usuario.getConn()).findByPrimaryKey(cf.getIdFolio());
                     
                //RECOLECTAMOS DATOS PERSONALIZADOS DE COMPROBANTE
                camposPersonalizados = new ArrayList<ComprobanteDescripcionPersonalizadaBO.CampoPersonalizado>();
                try{
                    ComprobanteDescripcionPersonalizadaDaoImpl cdpDao = new ComprobanteDescripcionPersonalizadaDaoImpl(usuario.getConn());
                    ComprobanteDescripcionPersonalizada[] cdp = cdpDao.findWhereIdComprobanteFiscalEquals(cf.getIdComprobanteFiscal());
                    if (cdp.length>0){
                        String cadenaConTokensDP =  cdp[0].getDatosDePersonalizacion();
                        if (StringManage.getValidString(cadenaConTokensDP).length()>0){
                            String[] tokensdp = cadenaConTokensDP.split("\\|");
                            DatosPersonalizados[] listdpDto = new DatosPersonalizadosBO(usuario.getConn()).findDatosPersonalizados(-1, empresaPadre.getIdEmpresa() , 0, 0, "");
                            int i = 0;
                            for (DatosPersonalizados dpDto : listdpDto) {
                                ComprobanteDescripcionPersonalizadaBO.CampoPersonalizado cp
                                        = new ComprobanteDescripcionPersonalizadaBO.CampoPersonalizado();
                                try{                                    
                                    cp.setVariable(dpDto.getVariable());
                                    switch (dpDto.getTipo()){
                                        case 1:
                                        case 2:
                                        case 3:
                                            cp.setTexto(true);
                                            break;
                                        case 4:
                                            cp.setDecimal(true);
                                            break;
                                        case 5:
                                            cp.setFecha(true);
                                            break;
                                    }
                                    
                                    cp.setValor(tokensdp[i]);
                                }catch(Exception e){
                                    cp.setValor(null);
                                }finally{
                                    camposPersonalizados.add(cp);
                                    i++;
                                }
                            }
                        }else {
                            System.out.println("-No hay datos personalizados para ésta factura");
                        }
                    }else{
                        System.out.println("-No hay datos personalizados para ésta factura");
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                
                /////////////
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private byte[] getFactura(HttpServletRequest request) {
		ServletContext context = getServletContext();
		String cotizacion = "";
                	
		try {                    
			if (cf.getIdFolio() == -1) {
				cotizacion = "COTIZACION";
			} else {				
                                if (cf.getIdTipoComprobante() == ID_COMPROBANTE_FACTURA) {
                                    cotizacion =  "FACTURA";
                                } else if(cf.getIdTipoComprobante() == TipoComprobanteBO.TIPO_FACTURA){
                                    cotizacion = "FACTURA";
                                } else if(cf.getIdTipoComprobante() == TipoComprobanteBO.TIPO_RECIBO_HONORARIOS){
                                    cotizacion = "RECIBO HONORARIOS";
                                } else if(cf.getIdTipoComprobante() == TipoComprobanteBO.TIPO_RECIBO_ARRENDAMIENTO){
                                    cotizacion = "RECIBO ARRENDAMIENTO";
                                } else if(cf.getIdTipoComprobante() == TipoComprobanteBO.TIPO_NOTA_CREDITO){
                                     cotizacion = "NOTA CREDITO";
                                } else if(cf.getIdTipoComprobante() == TipoComprobanteBO.TIPO_NOTA_DEBITO){
                                    cotizacion = "NOTA DEBITO";
                                }
			}
			Map<String, Object> parametros = new HashMap<String, Object>();

			// DATOS DEL CLIENTE

			parametros.put("TIPO_PAGO", tp.getDescTipoPago());
                        if(tp.getNumeroCuenta()!=null)
                        if(!tp.getNumeroCuenta().trim().equals(""))
                            parametros.put("NUM_CUENTA", tp.getNumeroCuenta());
                        
                        parametros.put("LUGAR_FECHA_EXPEDICION", ubicacion.getColonia() + ", "+ubicacion.getMunicipio() + ", "+ubicacion.getEstado() +", "+cf.getFechaCaptura());
                        
			parametros.put("REGIMEN_FISCAL", empresa.getRegimenFiscal());
                        
                        if(cf.getIdFolio()>0){
                            parametros.put("FOLIO_GENERADO", cf.getFolioGenerado());
                            parametros.put("SERIE_FOLIO", folio.getSerie());
                        }                            
                        
			parametros.put("Empresa", String.valueOf(empresa.getIdEmpresa()));
                        
                        
                         if(cf.getIdTipoComprobante() == TipoComprobanteBO.TIPO_NOMINA){
                            representacionImpresa = "Nomina_carta";                             
                                     
                            parametros.put("CLIENTE_NOMBRE",nomEmpleado.getNombre() != null ? (nomEmpleado.getNombre()+" "+nomEmpleado.getApellidoPaterno()+" "+nomEmpleado.getApellidoMaterno()) : "");
                            parametros.put("CLIENTE_RFC",nomEmpleado.getRfc() != null ? nomEmpleado.getRfc() : "");
                            parametros.put("CLIENTE_TEL", nomEmpleado.getTelefono() != null ? nomEmpleado.getTelefono() : "");
                            parametros.put("CLIENTE_CORREO", nomEmpleado.getCorreo() != null ? nomEmpleado.getCorreo() : "");
                           // parametros.put("CLIENTE_CONTACTO", nomEmpleado.getContacto() != null ? nomEmpleado.getContacto() : "");
                            /*parametros.put("CLIENTE_DIRECCION",(nomEmpleado.getCalle() != null ? nomEmpleado.getCalle() : "")
                                                                            + (nomEmpleado.getNumero() != null ? " No. " + nomEmpleado.getNumero(): "")
                                                                            // +
                                                                            // (factura.getCliente().getNumeroInterior()!=null?
                                                                            // " INTERIOR " +
                                                                            // factura.getCliente().getNumeroInterior():
                                                                            // "")
                                                                            + (nomEmpleado.getNumeroInterior() != null ? " "
                                                                                            + nomEmpleado.getNumeroInterior()
                                                                                            : "")
                                                                            + (nomEmpleado.getCodigoPostal() != null ? " C.P. "
                                                                                            + nomEmpleado.getCodigoPostal()
                                                                                            : "")
                                                                            + (nomEmpleado.getColonia() != null ? " COL. "
                                                                                            + nomEmpleado.getColonia()
                                                                                            : "")
                                                                            + " "
                                                                            + (nomEmpleado.getPais() != null ? nomEmpleado.getPais() + ","
                                                                                            : "")
                                                                            + " "
                                                                            + (nomEmpleado.getEstado() != null ? nomEmpleado.getEstado() + ","
                                                                                            : "")
                                                                            + " "
                                                                            + (nomEmpleado.getMunicipio() != null ? nomEmpleado.getMunicipio() : ""));*/
                            try{
                            parametros.put("NOM_DEPARTAMENTO", nomDepartamento.getNombre());
                            }catch(Exception e){}
                            try{
                            parametros.put("NOM_NO_EMPLEADO", nomEmpleado.getNumEmpleado());
                            }catch(Exception e){}
                            //parametros.put("NOM_NOM_PUESTO", nomPuesto.getNombre());
                            try{
                            parametros.put("NOM_NO_AFILIACION", nomEmpleado.getNumSeguroSocial());
                            }catch(Exception e){}
                            try{
                            parametros.put("NOM_PERIODO_PAGO", nomComDes.getFechaInicialPago() + " AL " + nomComDes.getFechaFinPago());
                            }catch(Exception e){}
                            try{
                            parametros.put("NOM_DIAS_PAGADOS", nomComDes.getNumDiasPagados());
                            }catch(Exception e){}
                            try{
                            parametros.put("NOM_SALARIO_DIARIO", nomEmpleado.getSalarioDiarioIntegrado());
                            }catch(Exception e){}
                            try{
                            parametros.put("NOM_PAGO_NETO", FormatUtil.doubleToStringPuntoComas(cf.getImporteNeto()));
                            }catch(Exception e){}
                            try{
                            parametros.put("NOM_RFC", nomEmpleado.getRfc());
                            }catch(Exception e){}
                            try{
                            parametros.put("NOM_NOM_PUESTO", (nomEmpleado.getNombre() != null ? (nomEmpleado.getNombre()+" "+nomEmpleado.getApellidoPaterno()+" "+nomEmpleado.getApellidoMaterno()) : "") + ", "+ nomPuesto.getNombre());
                            }catch(Exception e){
                                try{
                                parametros.put("NOM_NOM_PUESTO", (nomEmpleado.getNombre() != null ? (nomEmpleado.getNombre()+" "+nomEmpleado.getApellidoPaterno()+" "+nomEmpleado.getApellidoMaterno()) : ""));
                                }catch(Exception e2){}
                            }
                            try{                                
                                parametros.put("NOM_NOM_SOLO", (nomEmpleado.getNombre() != null ? (nomEmpleado.getNombre()+" "+nomEmpleado.getApellidoPaterno()+" "+nomEmpleado.getApellidoMaterno()) : ""));        
                            }catch(Exception e){}
                            try{
                                parametros.put("NOM_PUESTO_SOLO", nomPuesto.getNombre());
                            }catch(Exception e){}
                            
                            //enviamos el registro patronal al jasper:
                            String registroPatron = "";
                            if(registroPatronalsDto != null){                                
                                for(NominaRegistroPatronal reg : registroPatronalsDto){
                                    if(!registroPatron.trim().equals("")){
                                        registroPatron += ", ";
                                    }
                                    registroPatron += reg.getRegistroPatronal();
                                }                            
                            }
                            try{
                                if(!registroPatron.trim().equals("")){
                                    parametros.put("NOM_REGISTRO_PATRON", "Registro Patronal: "+registroPatron);
                                }
                            }catch(Exception e){}
                            try{
                                if(registroPatronalEmpresaEnSesion!=null){
                                    parametros.put("NOM_REGISTRO_PATRON_UNICO", "Registro Patronal: "+registroPatronalEmpresaEnSesion.getRegistroPatronal());
                                }       
                            }catch(Exception e){}
                            try{                                
                                if(nomComDes.getIsrImpuestoPorcentaje() > 0){
                                    parametros.put("NOM_ISR_PORCENTAJE", "%: " +  nomComDes.getIsrImpuestoPorcentaje());
                                }
                                if(nomComDes.getIsrImpuestoPorcentaje() > 0){
                                    parametros.put("NOM_ISR_MONTO", "MONTO: " +  nomComDes.getIsrMontoImpuesto());
                                }
                            }catch(Exception e){}
                            
                            
                        }else if(cf.getIdTipoComprobante() == ID_COMPROBANTE_CARTA_PORTE){
                            representacionImpresa = "CartaPorte_carta";
                            
                                 //DATOS DEL REMITENTE
                               if(!remitente.getRazonSocial().trim().equals(""))
                                   parametros.put("RAZON_REMITENTE", remitente.getRazonSocial());
                               else
                                   parametros.put("RAZON_REMITENTE", remitente.getNombreCliente() + " " + remitente.getApellidoPaternoCliente() + " " + remitente.getApellidoMaternoCliente());

                               parametros.put("RFC_REMITENTE", remitente.getRfcCliente());
                               parametros.put("DOMICILIO_REMITENTE",(remitente.getCalle() != null ? remitente.getCalle() : "")
                                                                               + (remitente.getNumero() != null ? " No. " + remitente.getNumero(): "")
                                                                               // +
                                                                               // (factura.getCliente().getNumeroInterior()!=null?
                                                                               // " INTERIOR " +
                                                                               // factura.getCliente().getNumeroInterior():
                                                                               // "")
                                                                               + (remitente.getNumeroInterior() != null ? " "
                                                                                               + remitente.getNumeroInterior()
                                                                                               : "")
                                                                               + (remitente.getCodigoPostal() != null ? " C.P. "
                                                                                               + remitente.getCodigoPostal()
                                                                                               : "")
                                                                               + (remitente.getColonia() != null ? " COL. "
                                                                                               + remitente.getColonia()
                                                                                               : "")
                                                                               + " "
                                                                               + (remitente.getPais() != null ? remitente.getPais() + ","
                                                                                               : "")
                                                                               + " "
                                                                               + (remitente.getEstado() != null ? remitente.getEstado() + ","
                                                                                               : "")
                                                                               + " "
                                                                               + (remitente.getMunicipio() != null ? remitente.getMunicipio() : ""));
                               
                               /*if(!cf.getOrigenMercancia().trim().equals(""))
                                    parametros.put("ORIGEN_MERCA", cf.getOrigenMercancia());*/

                               //DATOS DEL CONSIGNATARIO
                               if(!consignatario.getRazonSocial().trim().equals(""))
                                   parametros.put("RAZON_DESTINATARIO", consignatario.getRazonSocial());
                               else
                                   parametros.put("RAZON_DESTINATARIO", consignatario.getNombreCliente() + " " + consignatario.getApellidoPaternoCliente() + " " + consignatario.getApellidoMaternoCliente());

                               parametros.put("RFC_DESTINATARIO", consignatario.getRfcCliente());
                               parametros.put("DOMICILIO_DESTINATARIO",(consignatario.getCalle() != null ? consignatario.getCalle() : "")
                                                                               + (consignatario.getNumero() != null ? " No. " + consignatario.getNumero(): "")
                                                                               // +
                                                                               // (factura.getCliente().getNumeroInterior()!=null?
                                                                               // " INTERIOR " +
                                                                               // factura.getCliente().getNumeroInterior():
                                                                               // "")
                                                                               + (consignatario.getNumeroInterior() != null ? " "
                                                                                               + consignatario.getNumeroInterior()
                                                                                               : "")
                                                                               + (consignatario.getCodigoPostal() != null ? " C.P. "
                                                                                               + consignatario.getCodigoPostal()
                                                                                               : "")
                                                                               + (consignatario.getColonia() != null ? " COL. "
                                                                                               + consignatario.getColonia()
                                                                                               : "")
                                                                               + " "
                                                                               + (consignatario.getPais() != null ? consignatario.getPais() + ","
                                                                                               : "")
                                                                               + " "
                                                                               + (consignatario.getEstado() != null ? consignatario.getEstado() + ","
                                                                                               : "")
                                                                               + " "
                                                                               + (consignatario.getMunicipio() != null ? consignatario.getMunicipio() : ""));
                               
                               /*if(!cf.getDestinoMercancia().trim().equals(""))
                                    parametros.put("DESTINO_MERCA", cf.getDestinoMercancia());*/
                         
                         } else{// (cf.getIdTipoComprobante()==TipoComprobanteBO.TIPO_FACTURA){
                            
                            if(cf.getIdTipoComprobante()==TipoComprobanteBO.TIPO_FACTURA_IMPUESTO_X_CONCEPTO)
                                representacionImpresa = "Factura_carta_ImpXconc";
                            else
                                representacionImpresa = "Factura_carta";
                            
                            parametros.put("CLIENTE_NOMBRE",cliente.getRazonSocial() != null ? cliente.getRazonSocial() : (cliente.getNombreCliente() + " "+cliente.getApellidoPaternoCliente() + " " + cliente.getApellidoMaternoCliente()) );
                            parametros.put("CLIENTE_RFC",cliente.getRfcCliente() != null ? cliente.getRfcCliente() : "");
                            parametros.put("CLIENTE_TEL", cliente.getTelefono() != null ? cliente.getTelefono() : "");
                            parametros.put("CLIENTE_CORREO", cliente.getCorreo() != null ? cliente.getCorreo() : "");
                            parametros.put("CLIENTE_CONTACTO", cliente.getContacto() != null ? cliente.getContacto()
                                            : "");
                            parametros.put("CLIENTE_DIRECCION",(cliente.getCalle() != null ? cliente.getCalle() : "")
                                                                            + (cliente.getNumero() != null ? " No. " + cliente.getNumero(): "")
                                                                            // +
                                                                            // (factura.getCliente().getNumeroInterior()!=null?
                                                                            // " INTERIOR " +
                                                                            // factura.getCliente().getNumeroInterior():
                                                                            // "")
                                                                            + (cliente.getNumeroInterior() != null ? " "
                                                                                            + cliente.getNumeroInterior()
                                                                                            : "")
                                                                            + (cliente.getCodigoPostal() != null ? " C.P. "
                                                                                            + cliente.getCodigoPostal()
                                                                                            : "")
                                                                            + (cliente.getColonia() != null ? " COL. "
                                                                                            + cliente.getColonia()
                                                                                            : "")
                                                                            + " "
                                                                            + (cliente.getPais() != null ? cliente.getPais() + ","
                                                                                            : "")
                                                                            + " "
                                                                            + (cliente.getEstado() != null ? cliente.getEstado() + ","
                                                                                            : "")
                                                                            + " "
                                                                            + (cliente.getMunicipio() != null ? cliente.getMunicipio() : ""));
                            parametros.put("CLIENTE_DIRECCION_REFERENCIA", StringManage.getValidString(cliente.getReferencia()));

                        }
                        
                        

			// DATOS DE EMPRESA
			parametros.put("EMPRESA_RAZON", empresa.getRazonSocial());
			parametros.put("EMPRESA_RFC", empresa.getRfc());

			if (ubicacion.getNumInt() != null) {
				parametros.put("EMPRESA_DIRECCION", ubicacion.getCalle()
						+ " "
						+ ubicacion.getNumExt()
						+ " "
						+ ubicacion.getNumInt()
						+ ", COL. "
						+ ubicacion.getColonia());
			} else
				parametros.put("EMPRESA_DIRECCION", ubicacion.getCalle()
						+ " "
						+ ubicacion.getNumExt()
						+ " COL. "
						+ ubicacion.getColonia());

			parametros.put("EMPRESA_UBICACION", ubicacion.getPais()
							+ " "
							+ ubicacion.getEstado()
							+ " DEL. "
							+ ubicacion.getMunicipio()
							+ " C.P. "
							+ ubicacion.getCodigoPostal());

                        //***DATOS EMPRESA MATRIZ, esto para proas que quieren salga direccion de empresa sucursal y empresa matriz:			
			if (ubicacionEmpresaPadre.getNumInt() != null) {
				parametros.put("EMPRESA_DIRECCION_2", ubicacionEmpresaPadre.getCalle() + " " + ubicacionEmpresaPadre.getNumExt()
						+ " " + ubicacionEmpresaPadre.getNumInt() + ", COL. " + ubicacionEmpresaPadre.getColonia());
			} else
				parametros.put("EMPRESA_DIRECCION_2", ubicacionEmpresaPadre.getCalle() + " " + ubicacionEmpresaPadre.getNumExt()
						+ ", COL. " + ubicacionEmpresaPadre.getColonia());

			parametros.put("EMPRESA_UBICACION_2", ubicacionEmpresaPadre.getPais() + ", " + ubicacionEmpresaPadre.getEstado()
							//+ " DEL. "
							+ ", " + ubicacionEmpresaPadre.getMunicipio() + " C.P. " + ubicacionEmpresaPadre.getCodigoPostal());
			//***
                        
			// COMPROBANTE FISCAL
			if (cf.getIdEstatus() != ID_FACTURA_COTIZACION) {
				// EN CASO DE QUE NO SE TRATE DE LA COTIZACION DE UNA FACTURA				
				try {
					/*if (folio.getSerie().intern() == "TSPBD") {
						parametros.put("SERIE_FOLIO", "");
						parametros.put("FOLIO_GENERADO", "");
					} else {
						parametros.put("SERIE_FOLIO", folio.getSerie());						
						parametros.put("FOLIO_GENERADO", cf.getFolioGenerado());						
					}*/
				} catch (Exception e) {
					/*try {
						parametros.put("SERIE_FOLIO", "");
						parametros.put("FOLIO_GENERADO", cf.getFolioGenerado());
					} catch (Exception e2) {
						parametros.put("SERIE_FOLIO", "");
						parametros.put("FOLIO_GENERADO", "");
					}*/
				}
			}

			// DecimalFormat formateador1 = new
			// DecimalFormat("#,##0.00;(#,##0.00)");
			// DecimalFormat formateador1 = new
			// DecimalFormat("###,###,###,###,###,###,###.##");

			parametros.put("TIPO_MONEDA", tdm.getClave());
			parametros.put("COTIZACION", cotizacion != null ? cotizacion : "");
			parametros.put("TOTAL", FormatUtil.doubleToStringPuntoComas(cf.getImporteNeto()));			
			parametros.put("SUBTOTAL", FormatUtil.doubleToStringPuntoComas(cf.getImporteSubtotal()));
			BigDecimal subtotalDec = new BigDecimal(cf.getImporteSubtotal());
			parametros.put("SUBTOTAL_DEC", subtotalDec);
                        
                        if(cf.getDescuento()==0){
				parametros.put("DESCUENTO_PORCENTAJE", null);
				parametros.put("DESCUENTO", null);
			}
			else{
				//VERIFICAMOS SI HAY UN MOTIVO DE DESCUENTO
				if(cf.getMotivoDescuento() != null){
                                    if(!cf.getMotivoDescuento().trim().equals("")){
					parametros.put("DESCUENTO_PORCENTAJE", cf.getMotivoDescuento()+" "+Double.toString(cf.getDescuento())+ "%");
                                    }else{
                                        parametros.put("DESCUENTO_PORCENTAJE", "DESCUENTO "+Double.toString(cf.getDescuento())+ "%");
                                    }
				}else{
					parametros.put("DESCUENTO_PORCENTAJE", "DESCUENTO "+Double.toString(cf.getDescuento())+ "%");
				}				
				parametros.put("DESCUENTO", "$"+FormatUtil.doubleToStringPuntoComas(cf.getDescuento()*0.01*cf.getImporteSubtotal()));				
			}
                        
			parametros.put("IMPORTE_LETRA", FacturacionUtil.importeLetra(cf.getImporteNeto(), tdm.getClave()));
			parametros.put("COMENTARIOS", cf.getComentarios());
			if (cf.getIdTipoComprobante() == ID_COMPROBANTE_FACTURA) {
                            parametros.put("TIPO_RECIBO", "FACTURA");
			} else if(cf.getIdTipoComprobante() == ID_COMPROBANTE_FACTURA_CARTA_PORTE){
                            parametros.put("TIPO_RECIBO", "CARTA PORTE");
                        }
                        
			// SI LA EMPRESA TIENE UN LOGO SE VA A CARGAR EL PROPIO//                        
			if (ip != null) {
				parametros.put("LOGO","C:/SystemaDeArchivos/"+ empresa.getRfc()+ "/"+ ip.getNombreImagen());
			} else {// SI LA EMPRESA NO TIENE UN LOGO PROPIO SE CARGA EL LOGO DE FACTURA EN SEGUNDOS
				parametros.put("LOGO", request.getSession().getServletContext().getRealPath("/images/error.png"));
			}
			if (cf.getFechaCaptura() != null) {
                            try{
				parametros.put("FECHA_IMPRESION", FormateadorDeFechas.getFecha(cf.getFechaTimbrado().getTime(),FormateadorDeFechas.FORMATO_DDMMYYYYHHMMSS));
                            }catch(Exception e){
                                parametros.put("FECHA_IMPRESION", FormateadorDeFechas.getFecha(cf.getFechaCaptura().getTime(),FormateadorDeFechas.FORMATO_DDMMYYYYHHMMSS));
                            }
			} else {
				parametros.put("FECHA_IMPRESION", "");
			}

			// DETALLE DE IMPUESTOS
			//parametros.put("IMPUESTO_TOTAL",String.valueOf(cf.getImpuestos()));
//			List<Impuesto> listImpuestos = (List<Impuesto>) factura.getImpuestos();
                                                
			System.setProperty("jasper.reports.compile.class.path",context.getRealPath("/WEB-INF/lib/jasperreports-3.7.4.jar") + context.getRealPath("/WEB-INF/"));
			                        
                        JasperReport reporteImpuestos = (JasperReport) JRLoader.loadObject(context.getRealPath("/reportes/FacturaImpuestos.jasper"));
                        parametros.put("REPORTE_IMPUESTOS", reporteImpuestos);
                        parametros.put("DATOS_IMPUESTOS",new JRBeanCollectionDataSource(listImpuestos));
			

			//File reportFile;

			// DETALLE DE FACTURA						
			DecimalFormat formateador = new DecimalFormat("#00.000000;(-#00.000000)");
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
			dfs.setDecimalSeparator('.');
			formateador.setDecimalFormatSymbols(dfs);
			// ----------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
						
                        QRCodeWriter qrWriter = new QRCodeWriter();
			try {
				Hashtable params = new Hashtable();
				params.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.H);
				File fileTemp = new File("C:/temp/qrTemp"+ new Date().getTime() + ".gif");
				String dato = "?re=" + empresa.getRfc() + "&rr="+ rfcReceptor + "&tt="+ formateador.format(cf.getImporteNeto())+ "&id=" + cf.getUuid();
				MatrixToImageWriter.writeToFile(qrWriter.encode(dato, BarcodeFormat.QR_CODE, 625, 625),"gif", fileTemp);
				parametros.put("UUID", cf.getUuid());
				parametros.put("QRCODE", fileTemp.getAbsolutePath());
			} catch (WriterException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
                        
                        
                        parametros.put("NO_CERTIFICADO", certificadoDigital.getNoCertificado());
                        parametros.put("CONDICIONES_PAGO",fp.getDescFormaPago());
                        if(cf.getNoCertificadoSat() != null){
                            if(!cf.getNoCertificadoSat().trim().equals(""))
                                parametros.put("NO_CERTIFICADO_SAT", cf.getNoCertificadoSat());
                        }
                        try{
                        if(!cf.getCondicionesPago().trim().equals("")){
                            parametros.put("CONDICIONES_DE_PAGO",cf.getCondicionesPago());
                        }}catch(Exception e0){}
                        
                        parametros.put("COMENTARIOS", cf.getComentarios());
                        parametros.put("SELLO_DIGITAL", cf.getSelloDigital());
                        parametros.put("SELLO_SAT", cf.getSelloSat());                        
                        
                        try{
                        parametros.put("CADENA_ORIGINAL","||"+ VERSION_TIMBRADO+ "|"+ cf.getUuid()+ "|"
                                + FormateadorDeFechas.getFecha(cf.getFechaTimbrado().getTime(),FormateadorDeFechas.FORMATO_AAAAMMDDTHHMMSSS)
                                + "|" + cf.getSelloDigital()+ "|" + cf.getNoCertificadoSat() + "||");
                        }catch(Exception e){
                        parametros.put("CADENA_ORIGINAL","||"+ VERSION_TIMBRADO+ "|"+ cf.getUuid()+ "|"
                                + FormateadorDeFechas.getFecha(cf.getFechaCaptura().getTime(),FormateadorDeFechas.FORMATO_AAAAMMDDTHHMMSSS)
                                + "|" + cf.getSelloDigital()+ "|" + cf.getNoCertificadoSat() + "||");
                        }
                        
                        //DATOS PERSONALIZADOS
                         // TODO
                         for ( ComprobanteDescripcionPersonalizadaBO.CampoPersonalizado cp : camposPersonalizados){
                            if(cp.isDecimal())
                                parametros.put(cp.getVariable(), new BigDecimal(cp.getValor()));
                            else
                                parametros.put(cp.getVariable(), cp.getValor());
                         }
                        //DATOS PERSONALIZADOS - FIN
                        
			System.setProperty("jasper.reports.compile.class.path",context.getRealPath("/WEB-INF/lib/jasperreports-3.7.4.jar") + System.getProperty("path.separator") + context.getRealPath("/WEB-INF/"));

			
			
				//MAPEAMOS LOS DATOS AL Detail DEL JRXML (REPRESENTACION IMPRESA)
                                JRBeanCollectionDataSource ds = null;
                                if(cf.getIdTipoComprobante() == TipoComprobanteBO.TIPO_NOMINA){//si el comprobante es de nomina:
                                    ds = new JRBeanCollectionDataSource(resultadoNomina);
                                }else{
                                    ds = new JRBeanCollectionDataSource(resultado);
                                }
                              
                                //VALIDAMOS SI TIENE REPRESENTACION IMPRESA PERSONALIZADA:
                                String representacionAuxiliar = representacionImpresa; //para ayuda por si no esta el personalizado recuperar nombre del pdf generico
                                try{
                                    representacionImpresa += "_"+empresa.getRfc();                                                                   
                                    System.out.println("________________PDF PERSONALIZADO. . . "+representacionImpresa+".jasper");
                                    return JasperRunManager.runReportToPdf(context.getRealPath("/reportes/"+representacionImpresa+".jasper") ,parametros, ds);
                                }catch(Exception e){
                                    representacionImpresa = representacionAuxiliar;                                    
                                    System.out.println("________________PDF generico. . . "+representacionImpresa+".jasper");
                                    return JasperRunManager.runReportToPdf(context.getRealPath("/reportes/"+representacionImpresa+".jasper") ,parametros, ds);
                                }
                                

		} catch (Exception e) {
			e.printStackTrace();
		} 
                /*finally {
			ConnectionManager.closeConnection(conn);
		}*/

		return null;
	}
    
        public void calculoDeImpuestos(UsuarioBO usuario){
        ///*SI LA FACTURA ES DE TIPO CARTA PORTE o FACTURA POR IMPUESTOS POR CONCEPTO, RECUPERAMOS LOS IMPUESTOS POR CONCEPTO:
                        
                        //if(comprobanteDescripcion.getIdImpuesto()>0 && (cf.getIdTipoComprobante()==TipoComprobanteBO.TIPO_CARTA
                            try{
                                //obtenemos los impuestos involucrados en el concepto:
                                System.out.println("////BUSCANDO IMPUESTOS POR CONCEPTO . . , BUSNCANDO POR ID DE CONCEPTO = "+comprobanteDescripcion.getIdConcepto());
                                ImpuestoPorConcepto[] ipcList = new ImpuestoPorConceptoDaoImpl(usuario.getConn()).findWhereIdConceptoEquals(comprobanteDescripcion.getIdConcepto());
                                System.out.println("////NUMERO DE IMPUESTOS ENCONTRADOS: "+ipcList.length);
                                //obtenemos el impuesto en question de la tabla comprobante impesto:
                                for(ImpuestoPorConcepto imxCon : ipcList){                                     
                                    //com.fens.linea.dto.Impuesto impuestoSCT = new ImpuestoDaoImpl(usuario.getConn()).findByPrimaryKey(imxCon.getIdImpuesto());
                                    System.out.println("////buscando id de impuesto: "+imxCon.getIdImpuesto());
                                    ComprobanteImpuestoXConcepto comprobanteImpuesto = new ComprobanteImpuestoXConceptoDaoImpl(usuario.getConn()).findWhereIdImpuestoEquals(imxCon.getIdImpuesto())[0];
                                    
                                    //varificamos por id de impuesto
                                    BigDecimal montoImpuesto = BigDecimal.ZERO;
                                    //hacemos los calculos necesarios:
                                    montoImpuesto = BigDecimal.valueOf( (comprobanteImpuesto.getPorcentaje() * 0.01 * ((comprobanteDescripcion.getPrecioUnitario() * comprobanteDescripcion.getCantidad()) - comprobanteDescripcion.getDescuentoMonto()) ) );

                                    //verifico si ya esta en la lista el impuesto, si es que no lo agrego:
                                    boolean impuestoYaCargado = false;
                                    if(listImpuestos.size() > 0){
                                        for(Impuesto impEnLista : listImpuestos){
                                            if(impEnLista.getIdImpuesto() == comprobanteImpuesto.getIdImpuesto()){
                                                System.out.println("_____RECALCULANDO MONTO DE IMPUESTO . . . ");
                                                //SI ES QUE YA ESTA EN LA LISTA SUMAMOS EL MONTO
                                                BigDecimal totalCalculo = BigDecimal.valueOf(impEnLista.getTotal()).add(montoImpuesto);
                                                impEnLista.setTotal(totalCalculo.doubleValue());
                                                System.out.println(":::::::::::::::::::::MONTO TOTAL DE IMPUESTO: "+impEnLista.getTotal());
                                                impuestoYaCargado = true;
                                            }
                                        }
                                            if(!impuestoYaCargado){
                                                System.out.println("_____AGREGANDO IMPUESTO");
                                                //SI ES QUE AUN NO ESTA EN LA LISTA LE CARGAMOS EL MONTO
                                                //comparo si ya esta agregado el impuesto y realizo sus cuentas:
                                                Impuesto impPDF = new Impuesto();
                                                impPDF.setIdImpuesto(comprobanteImpuesto.getIdImpuesto());
                                                impPDF.setIdEmpresa(empresa.getIdEmpresa());
                                                impPDF.setNombre(comprobanteImpuesto.getNombre());
                                                impPDF.setDescripcion(comprobanteImpuesto.getDescripcion());
                                                impPDF.setPorcentaje(comprobanteImpuesto.getPorcentaje());
                                                //impPDF.setTrasladado(comprobanteImpuesto.isTrasladadoNull());
                                                impPDF.setTrasladado((comprobanteImpuesto.getTrasladado())==1?true:false);
                                                //Calculo de totales: ESTE CALCULO SE HACE EN LA CONDICION "IF" DE MAS ABAJO, PARA VER SI SE AGREGA AL TOTAL O SE LE SUMA AL TOTAL, DEPENDIENDO SI ESTA EN LA LISTA O NO
                                                //impPDF.setTotal(impuestosSCT.getPorcentaje() * .01 * (cf.getImporteSubtotal() - (cf.getDescuento()*0.01*cf.getImporteSubtotal())));
                                                impPDF.setIdEstatus(imxCon.getIdEstatus());
                                                //impPDF.setImpuestoLocal(comprobanteImpuesto.isImpuestoLocalNull());
                                                impPDF.setImpuestoLocal(comprobanteImpuesto.getImpuestoLocal()==1?true:false);
                                                impPDF.setTotal(montoImpuesto.doubleValue());
                                                listImpuestos.add(impPDF);
                                            }
                                        
                                    }else{
                                        System.out.println("_____AGREGANDO PRIMER IMPUESTO");
                                        //SI ES QUE AUN NO ESTA EN LA LISTA LE CARGAMOS EL MONTO
                                        //comparo si ya esta agregado el impuesto y realizo sus cuentas:
                                        Impuesto impPDF = new Impuesto();
                                        impPDF.setIdImpuesto(comprobanteImpuesto.getIdImpuesto());
                                        impPDF.setIdEmpresa(empresa.getIdEmpresa());
                                        impPDF.setNombre(comprobanteImpuesto.getNombre());
                                        impPDF.setDescripcion(comprobanteImpuesto.getDescripcion());
                                        impPDF.setPorcentaje(comprobanteImpuesto.getPorcentaje());
                                        
                                        impPDF.setTrasladado((comprobanteImpuesto.getTrasladado())==1?true:false);
                                        //Calculo de totales: ESTE CALCULO SE HACE EN LA CONDICION "IF" DE MAS ABAJO, PARA VER SI SE AGREGA AL TOTAL O SE LE SUMA AL TOTAL, DEPENDIENDO SI ESTA EN LA LISTA O NO
                                        //impPDF.setTotal(impuestosSCT.getPorcentaje() * .01 * (cf.getImporteSubtotal() - (cf.getDescuento()*0.01*cf.getImporteSubtotal())));
                                        impPDF.setIdEstatus(imxCon.getIdEstatus());
                                        //impPDF.setImpuestoLocal(comprobanteImpuesto.isImpuestoLocalNull());
                                        impPDF.setImpuestoLocal(comprobanteImpuesto.getImpuestoLocal()==1?true:false);
                                        impPDF.setTotal(montoImpuesto.doubleValue());
                                        listImpuestos.add(impPDF);
                                    }

                                }
                            }catch(Exception e){e.printStackTrace();}                                              
                        ///**
    }
        
}