
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.servlet;

import com.tsp.sct.bo.CatalogoGastosBO;
import com.tsp.sct.bo.ClienteBO;
import com.tsp.sct.bo.ComprobanteFiscalBO;
import com.tsp.sct.bo.ConceptoBO;
import com.tsp.sct.bo.CuentaEfectivoBO;
import com.tsp.sct.bo.EmpleadoBO;
import com.tsp.sct.bo.EmpresaBO;
import com.tsp.sct.bo.FoliosBO;
import com.tsp.sct.bo.GastosEvcBO;
import com.tsp.sct.bo.ImagenPersonalBO;
import com.tsp.sct.bo.RegionBO;
import com.tsp.sct.bo.SGCobranzaAbonoBO;
import com.tsp.sct.bo.SGCobranzaRegistroDepositoBancoBO;
import com.tsp.sct.bo.SGPedidoBO;
import com.tsp.sct.bo.SgfensPedidoDevolucionCambioBO;
import com.tsp.sct.bo.UsuarioBO;
import com.tsp.sct.bo.UsuariosBO;
import com.tsp.sct.config.Configuration;
import com.tsp.sct.dao.dto.Cliente;
import com.tsp.sct.dao.dto.ComprobanteFiscal;
import com.tsp.sct.dao.dto.Concepto;
import com.tsp.sct.dao.dto.CuentaEfectivo;
import com.tsp.sct.dao.dto.DatosUsuario;
import com.tsp.sct.dao.dto.Empleado;
import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.dao.dto.Folios;
import com.tsp.sct.dao.dto.GastosEvc;
import com.tsp.sct.dao.dto.ImagenPersonal;
import com.tsp.sct.dao.dto.Region;
import com.tsp.sct.dao.dto.SgfensCobranzaAbono;
import com.tsp.sct.dao.dto.SgfensCobranzaRegistroDepositoBanco;
import com.tsp.sct.dao.dto.SgfensPedido;
import com.tsp.sct.dao.dto.SgfensPedidoDevolucionCambio;
import com.tsp.sct.dao.dto.SgfensPedidoProducto;
import com.tsp.sct.dao.dto.Usuarios;
import com.tsp.sct.dao.jdbc.DatosUsuarioDaoImpl;
import com.tsp.sct.dao.jdbc.EmpleadoDaoImpl;
import com.tsp.sct.dao.jdbc.EmpresaDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensPedidoProductoDaoImpl;
import com.tsp.sct.dao.jdbc.UsuariosDaoImpl;
import com.tsp.sct.reports.dto.TablaContado;
import com.tsp.sct.util.DateManage;
import com.tsp.sct.util.FileManage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;


/**
 *
 * @author HpPyme
 */
public class ServletReporteGeneralDia extends HttpServlet {
    
    
    
    private Empresa empresa = null;
    Map<String, Object> parametros = new HashMap<String, Object>();    
    private ImagenPersonal ip = null;
    private String representacionImpresa = "";
    Configuration appConfig = new Configuration();
    private List<TablaContado> listMain = null;
    private List<TablaContado> listContado = null;
    private List<TablaContado> listCredito = null;
    private List<TablaContado> listAbonosCredito = null;
    private List<TablaContado> listCheques = null;
    private List<TablaContado> listDepositos = null;
    private List<TablaContado> listCuentaEfectivo = null;
    private List<TablaContado> listGastos = null;
    private List<TablaContado> listCambiosDev = null;
    private List<TablaContado> listCambiosDevEntregas = null;  
    String pathReports = appConfig.getApp_content_path()+"/reportesTmp/";
    Object nothing = null;
    

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        /*try (PrintWriter out = response.getWriter()) {
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ServletReporteGeneralDia</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ServletReporteGeneralDia at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }*/
        
        int idEmpresa = -1;
        try{
            idEmpresa = Integer.parseInt(request.getParameter("idEmpresa"));
        }catch(NumberFormatException e){}
        
        int idVendedor = -1;
        try{
            idVendedor = Integer.parseInt(request.getParameter("idVendedor"));
        }catch(NumberFormatException e){}
        
        String buscar_fechamin = "";
        String buscar_fechamax = "";
        Date fechaMin=null;
        Date fechaMax=null;        

        {
            try{
                fechaMin = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("q_fh_min"));
                buscar_fechamin = DateManage.formatDateToSQL(fechaMin);
            }catch(Exception e){}
            try{
                fechaMax = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("q_fh_max"));
                buscar_fechamax = DateManage.formatDateToSQL(fechaMax);
            }catch(Exception e){}
            
        }
        
        String tipo = request.getParameter("tipo")!=null? new String(request.getParameter("tipo").getBytes("ISO-8859-1"),"UTF-8") :"pdf";
                 
        
        List<JasperPrint> reportPrint = generaReporteGralDiaPdf(idEmpresa,idVendedor,buscar_fechamin,buscar_fechamax,"");         
        
        if (tipo.equals("pdf")) {
             this.exportPdf(response,reportPrint);
        }else if (tipo.equals("xls")) {
           this.exportXls(response, reportPrint);
        }
       

        
        
    }
    
    public List<JasperPrint> generaReporteGralDiaPdf(int idEmpresa,int idUsuario,String fechaMin, String fechaMax ,String filtroBusqueda){
        try{
             ByteArrayOutputStream baos = null;
             //creamos la conexión de la base de datos:
             UsuarioBO usuario = new UsuarioBO();
             representacionImpresa = "Reporte_GralDelDia";  //plantilla jasper
             ServletContext context = getServletContext();
             Empresa empresa = new EmpresaBO(idEmpresa, null).getEmpresa();
             
             
             
            SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(usuario.getConn());
            SgfensCobranzaAbono[] cobranzaAbonoDto = new SgfensCobranzaAbono[0];
            SGPedidoBO pedidoBO  = new SGPedidoBO(usuario.getConn());
            ComprobanteFiscalBO facturaBO = new ComprobanteFiscalBO(usuario.getConn());
            SgfensPedido[] pedidoDtos = null;
            SgfensPedido pedidoDto = null;
            ComprobanteFiscal facturaDto = null;
            FoliosBO foliosBO = new FoliosBO(usuario.getConn());
            ClienteBO clienteBO = new ClienteBO(usuario.getConn());
            Cliente clienteDto = null;
            String strWhereRangoFechas="";
            Object nothing = null;
            Usuarios usuarioDto = null;
            SGCobranzaRegistroDepositoBancoBO sgfensCobranzaRegistroDepositoBancoBO = new SGCobranzaRegistroDepositoBancoBO(usuario.getConn());
            SgfensCobranzaRegistroDepositoBanco[] sgfensCobranzaRegistroDepositoBancosDto = new SgfensCobranzaRegistroDepositoBanco[0];
            SgfensPedidoDevolucionCambioBO sgfensPedidoDevolucionCambioBO = new SgfensPedidoDevolucionCambioBO(usuario.getConn());
            SgfensPedidoDevolucionCambio[] sgfensPedidoDevolucionCambiosDto = new SgfensPedidoDevolucionCambio[0];
            CuentaEfectivoBO cuentaEfectivoBO = new CuentaEfectivoBO(usuario.getConn());
            CuentaEfectivo[] cuentaEfectivoDto = new CuentaEfectivo[0];
            GastosEvcBO gastosEvcBO =  new GastosEvcBO(usuario.getConn());
            GastosEvc[] gastosEvcDto = new GastosEvc[0];
                        
             
             //Obtenemos la empresa
            empresa = new Empresa();
            EmpresaDaoImpl empresaDaoImpl = new EmpresaDaoImpl(usuario.getConn());
            empresa = empresaDaoImpl.findByPrimaryKey(idEmpresa);
            
            
            ip = new ImagenPersonal();
            ImagenPersonalBO imagenPersonalBO = new ImagenPersonalBO(usuario.getConn());
            try{
                ip = imagenPersonalBO.findImagenPersonalByEmpresa(empresa.getIdEmpresa());
            }catch(Exception e){ip= null;}

            // SI LA EMPRESA TIENE UN LOGO SE VA A CARGAR EL PROPIO//                        
            if (ip != null) {
                    parametros.put("LOGO","C:/SystemaDeArchivos/"+ empresa.getRfc()+ "/"+ ip.getNombreImagen());
            }else{
                parametros.put("LOGO",null);
            }               
            parametros.put("EMPRESA_RAZON",empresa.getNombreComercial());   
              
            if(idUsuario>0){
                try{
                                       
                 
                    usuarioDto = new UsuariosDaoImpl(usuario.getConn()).findByPrimaryKey(idUsuario);        
                    
                  
                    DatosUsuario userReport = new DatosUsuarioDaoImpl(usuario.getConn()).findByPrimaryKey(usuarioDto.getIdDatosUsuario());
                    String nombreUser= "";
                    if(userReport!=null){
                        nombreUser += userReport.getNombre()!=null?userReport.getNombre():"";
                        nombreUser += " ";
                        nombreUser += userReport.getApellidoPat()!=null?userReport.getApellidoPat():"";
                        nombreUser += " ";
                        nombreUser += userReport.getApellidoMat()!=null?userReport.getApellidoMat():"";
                            
                    }
                    
                    parametros.put("EMPLEADO",nombreUser);
                    
                    Empleado empleadoDto = new EmpleadoBO(usuario.getConn()).findEmpleadoByUsuario(idUsuario);
                    Region regionDto = null;
                    
                    if(empleadoDto!=null && empleadoDto.getIdRegion()>0){       
                        
                        regionDto = new RegionBO(empleadoDto.getIdRegion(),usuario.getConn()).getRegion();                     
                    }                    
                    
                    
                    parametros.put("ZONA",regionDto!=null?regionDto.getNombre():"GENERAL");   
                    
                    
                }catch(Exception e){
                    e.printStackTrace();
                    parametros.put("EMPLEADO","GENERAL"); 
                    parametros.put("REGION","GENERAL"); 
                }
            }else{
                parametros.put("EMPLEADO","GENERAL");  
                parametros.put("REGION","GENERAL");   
            }
            
            
            
            /*
            
                       
            */
            JRBeanCollectionDataSource ds0 = null;
            JasperDesign jasperDesignReport0  = null;

            listMain = new ArrayList<TablaContado>();
            listMain.add((TablaContado) nothing);
            
           ds0 = new JRBeanCollectionDataSource(listMain); //Cambiar est aline apor otra colección  
           JRBeanCollectionDataSource report0DataSource = new JRBeanCollectionDataSource(listMain);
           //InputStream inputStreamReport0 = new FileInputStream(context.getRealPath("/reportes/Reporte_GralDelDia.jrxml"));
           InputStream inputStreamReport0 = new FileInputStream(context.getRealPath("reportes/Reporte_GralDelDia.jrxml"));
           jasperDesignReport0 = JRXmlLoader.load(inputStreamReport0);
           JasperReport jasperReportReport0 = JasperCompileManager.compileReport(jasperDesignReport0);
            
            
           
          
           
            /***
             * Report Pedidos Contado
             */
            filtroBusqueda = "";
            strWhereRangoFechas ="";
            double totalContado = 0;
            
                   

            {                

                /*Filtro por rango de fechas*/
                if (!fechaMin.equals("") && !fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA_PEDIDO AS DATE) BETWEEN '"+fechaMin+"' AND '"+fechaMax+"')";                    
                }
                if (!fechaMin.equals("") && fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA_PEDIDO AS DATE)  >= '"+fechaMin+"')";                   
                }
                if (fechaMin.equals("") && !fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA_PEDIDO AS DATE)  <= '"+fechaMax+"')";                    
                }
            }
            
            if (!strWhereRangoFechas.trim().equals("")){
                filtroBusqueda += " AND " + strWhereRangoFechas;
            }
                        
            
            if (idUsuario>0){
                filtroBusqueda += " AND ID_USUARIO_VENDEDOR='" + idUsuario +"' ";               
            }

           filtroBusqueda += " AND TOTAL = SALDO_PAGADO AND ID_ESTATUS_PEDIDO <>3 "; //Solo pagados de contado
            
           System.out.println(" --------------Contado------------------");
           JRBeanCollectionDataSource ds1 = null;
           JasperDesign jasperDesignReport1  = null;
           
           
            pedidoDtos = pedidoBO.findPedido(-1, idEmpresa, -1, -1, filtroBusqueda);
           
            
            
            try{
                listContado = new ArrayList<TablaContado>();
                TablaContado tablaContado = null;
                int i = 1;              
                listContado.add((TablaContado) nothing); //Registro Control
                for(SgfensPedido pedido:pedidoDtos){
                    
                      
                    //Si tiene factura
                    Folios folioDto = null;
                    try{
                        facturaDto = facturaBO.findComprobanteFiscal(pedido.getIdComprobanteFiscal(),idEmpresa , -1, -1, "")[0];
                    }catch(Exception e){e.printStackTrace();}
                    if(facturaDto!=null){
                        try{
                            if(facturaDto.getIdFolio()>0){
                                folioDto =  foliosBO.findFolios(facturaDto.getIdFolio(), -1, -1, -1, "")[0];
                            }
                        }catch(Exception e){e.printStackTrace();}
                        
                    }
                    try{
                        clienteDto = clienteBO.findClientebyId(pedido.getIdCliente());
                        
                    }catch(Exception e){e.printStackTrace();};
                    String nombreCliente = "";
                    if (clienteDto!=null){
                        nombreCliente = clienteDto.getRazonSocial();
                    }
                    
                    tablaContado = new TablaContado();
                    tablaContado.setNumeroSecuencial(i);
                    tablaContado.setFolioPedido(pedido!=null?pedido.getFolioPedido():"");
                    tablaContado.setSerieFactura(folioDto!=null?folioDto.getSerie():"");
                    tablaContado.setFolioFactura(facturaDto!=null?facturaDto.getFolioGenerado():"");
                    tablaContado.setNombreCliente(nombreCliente);
                    tablaContado.setMonto(pedido.getTotal());

                    listContado.add(tablaContado);
                    i++;
                    
                    totalContado += pedido.getTotal();
                    
                }
             
                
                 parametros.put("TOTAL_CONTADO",totalContado);   
                 parametros.put("NUM_ROWS_CONTADO",pedidoDtos.length);   
                
                //Si la lista esta vacia agregamos un elemnto null para mostrarla
                if(listContado.size()<=0){
                    //listContado.add((TablaContado) nothing);
                }
              
                
                //New code
                System.setProperty("jasper.reports.compile.class.path",context.getRealPath("/WEB-INF/lib/jasperreports-3.7.4.jar") + context.getRealPath("/WEB-INF/"));			                        
                JasperReport reporteContado = (JasperReport) JRLoader.loadObject(context.getRealPath("/reportes/tablaContado.jasper"));
                parametros.put("REPORTE_CONTADO", reporteContado);
                parametros.put("DATOS_CONTADO",new JRBeanCollectionDataSource(listContado));    
                

                 //MAPEAMOS LOS DATOS AL Detail DEL JRXML (REPRESENTACION IMPRESA)
                   
                  ds1 = new JRBeanCollectionDataSource(listContado); //Cambiar est aline apor otra colección  
                  JRBeanCollectionDataSource report1DataSource = new JRBeanCollectionDataSource(listContado);
                  InputStream inputStreamReport1 = new FileInputStream(context.getRealPath("/reportes/tablaContado.jrxml"));
                  jasperDesignReport1 = JRXmlLoader.load(inputStreamReport1);
                  JasperReport jasperReportReport1 = JasperCompileManager.compileReport(jasperDesignReport1);    
                   
            }catch(Exception e){e.printStackTrace();}

            /***
             * Fin Report Contado
             */
               
               
             /***
             * Report Credito
             */
            
            filtroBusqueda = "";
            strWhereRangoFechas ="";
            double totalCredito = 0;

            {
                

                /*Filtro por rango de fechas*/
                if (!fechaMin.equals("") && !fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA_PEDIDO AS DATE) BETWEEN '"+fechaMin+"' AND '"+fechaMax+"')";                    
                }
                if (!fechaMin.equals("") && fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA_PEDIDO AS DATE)  >= '"+fechaMin+"')";                   
                }
                if (fechaMin.equals("") && !fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA_PEDIDO AS DATE)  <= '"+fechaMax+"')";                    
                }
            }
            
            if (!strWhereRangoFechas.trim().equals("")){
                filtroBusqueda += " AND " + strWhereRangoFechas;
            }
                        
            
            if (idUsuario>0){
                filtroBusqueda += " AND ID_USUARIO_VENDEDOR='" + idUsuario +"' ";               
            }

           filtroBusqueda += " AND TOTAL > SALDO_PAGADO AND ID_ESTATUS_PEDIDO <> 3 "; //Solo pagados de contado
           
           System.out.println(" --------------Credito------------------");
           
           
           JRBeanCollectionDataSource ds2 = null;
           JasperDesign jasperDesignReport2  = null;
           
           pedidoDtos = pedidoBO.findPedido(-1, idEmpresa, -1, -1, filtroBusqueda);
            //cobranzaAbonoDto = cobranzaAbonoBO.findCobranzaAbono(-1, idEmpresa , 0, 0, filtroBusqueda);
            
            
            try{
                listCredito = new ArrayList<TablaContado>();
                TablaContado tablaContado = null;
                int i = 1;              
                listCredito.add((TablaContado) nothing); //Registro Control
                for(SgfensPedido pedido:pedidoDtos){
                    
                      
                    //Si tiene factura
                    Folios folioDto = null;
                    try{
                        facturaDto = facturaBO.findComprobanteFiscal(idEmpresa, -1, -1, -1, "")[0];
                    }catch(Exception e){e.printStackTrace();}
                    if(facturaDto!=null){
                        try{
                            if(facturaDto.getIdFolio()>0){
                                folioDto =  foliosBO.findFolios(facturaDto.getIdFolio(), -1, -1, -1, "")[0];
                            }
                        }catch(Exception e){e.printStackTrace();}
                        
                    }
                    try{
                        clienteDto = clienteBO.findClientebyId(pedido.getIdCliente());
                        
                    }catch(Exception e){e.printStackTrace();};
                    String nombreCliente = "";
                    if (clienteDto!=null){
                        nombreCliente = clienteDto.getRazonSocial();
                    }
                    
                    tablaContado = new TablaContado();
                    tablaContado.setNumeroSecuencial(i);
                    tablaContado.setFolioPedido(pedido!=null?pedido.getFolioPedido():"");
                    tablaContado.setSerieFactura(folioDto!=null?folioDto.getSerie():"");
                    tablaContado.setFolioFactura(facturaDto!=null?facturaDto.getFolioGenerado():"");
                    tablaContado.setNombreCliente(nombreCliente);
                    tablaContado.setMonto(pedido.getTotal()-pedido.getSaldoPagado());

                    listCredito.add(tablaContado);
                    i++;
                    
                    totalCredito += (pedido.getTotal()-pedido.getSaldoPagado());
                }
                
                parametros.put("TOTAL_CREDITO",totalCredito);  
                parametros.put("NUM_ROWS_CREDITO",pedidoDtos.length);  
                          
                //Si la lista esta vacia agregamos un elemnto null para mostrarla
                if(listCredito.size()<=0){
                    //listCredito.add((TablaContado) nothing);
                }
               
                //New code
                System.setProperty("jasper.reports.compile.class.path",context.getRealPath("/WEB-INF/lib/jasperreports-3.7.4.jar") + context.getRealPath("/WEB-INF/"));			                        
                JasperReport reporteCredito = (JasperReport) JRLoader.loadObject(context.getRealPath("/reportes/tablaCredito.jasper"));
                parametros.put("REPORTE_CREDITO", reporteCredito);
                parametros.put("DATOS_CREDITO",new JRBeanCollectionDataSource(listCredito)); 
                
            
             //MAPEAMOS LOS DATOS AL Detail DEL JRXML (REPRESENTACION IMPRESA)
               ds2 = new JRBeanCollectionDataSource(listCredito); //Cambiar est aline apor otra colección 
               JRBeanCollectionDataSource report2DataSource = new JRBeanCollectionDataSource(listCredito);
               InputStream inputStreamReport2 = new FileInputStream(context.getRealPath("/reportes/tablaCredito.jrxml"));
               jasperDesignReport2 = JRXmlLoader.load(inputStreamReport2);
               JasperReport jasperReportReport2 = JasperCompileManager.compileReport(jasperDesignReport2);
               
               
           }catch(Exception e){e.printStackTrace();}
           
            /***
             * Fin Report Credito
             */
            
            
            /***
             * Report Abonos Credito
             */
            
            filtroBusqueda = "";
            strWhereRangoFechas ="";
            double totalAbonosCredito = 0;

            {               

                /*Filtro por rango de fechas*/
               if (!fechaMin.equals("") && !fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA_ABONO AS DATE) BETWEEN '"+fechaMin+"' AND '"+fechaMax+"')";                    
                }
                if (!fechaMin.equals("") && fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA_ABONO AS DATE)  >= '"+fechaMin+"')";                   
                }
                if (fechaMin.equals("") && !fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA_ABONO AS DATE)  <= '"+fechaMax+"')";                    
                }
            }
            
            if (!strWhereRangoFechas.trim().equals("")){
                filtroBusqueda += " AND " + strWhereRangoFechas;
            }
                        
            
            if (idUsuario>0){
                filtroBusqueda += " AND ID_USUARIO_VENDEDOR='" + idUsuario +"' ";               
            }

           filtroBusqueda += " AND ID_ESTATUS <>2 AND ID_COBRANZA_METODO_PAGO <> 10 "; 
          
            
           System.out.println(" --------------Abonos Credito------------------");
           
           
           JRBeanCollectionDataSource ds3 = null;
           JasperDesign jasperDesignReport3  = null;
           SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");      
           String fechaPedido = null;
           String fechaCobro = null;
          
           cobranzaAbonoDto = cobranzaAbonoBO.findCobranzaAbono(-1, idEmpresa , 0, 0, filtroBusqueda);
            
            
            try{
                listAbonosCredito = new ArrayList<TablaContado>();
                TablaContado tablaContado = null;
                int i = 1;              
                listAbonosCredito.add((TablaContado) nothing); //Registro Control
                for(SgfensCobranzaAbono cob:cobranzaAbonoDto){
                    
                    try{   
                        pedidoDto = pedidoBO.findPedidobyId(cob.getIdPedido());
                        fechaPedido = formateador.format(pedidoDto.getFechaPedido());
                        fechaCobro  = formateador.format(cob.getFechaAbono());
                    }catch(Exception e){e.printStackTrace();}
                    //Si tiene factura
                    Folios folioDto = null;
                    try{
                        facturaDto = facturaBO.findComprobanteFiscal(idEmpresa, -1, -1, -1, "")[0];
                    }catch(Exception e){e.printStackTrace();}
                    if(facturaDto!=null){
                        try{
                            if(facturaDto.getIdFolio()>0){
                                folioDto =  foliosBO.findFolios(facturaDto.getIdFolio(), -1, -1, -1, "")[0];
                            }
                        }catch(Exception e){e.printStackTrace();}
                        
                    }
                    try{
                        clienteDto = clienteBO.findClientebyId(pedidoDto.getIdCliente());
                        
                    }catch(Exception e){e.printStackTrace();};
                    String nombreCliente = "";
                    if (clienteDto!=null){
                        nombreCliente = clienteDto.getRazonSocial();
                    }
                    
                    tablaContado = new TablaContado();
                    tablaContado.setNumeroSecuencial(i);
                    tablaContado.setFolioPedido(pedidoDto!=null?pedidoDto.getFolioPedido():"");
                    tablaContado.setSerieFactura(folioDto!=null?folioDto.getSerie():"");
                    tablaContado.setFolioFactura(facturaDto!=null?facturaDto.getFolioGenerado():"");
                    tablaContado.setNombreCliente(nombreCliente);
                    tablaContado.setMonto(cob.getMontoAbono());

                    if(fechaPedido!=null && fechaCobro!=null){
                        if(!fechaPedido.equals(fechaCobro)){
                            listAbonosCredito.add(tablaContado);
                            i++;                    
                            totalAbonosCredito += cob.getMontoAbono();
                         }   
                    }
                        
                    
                }
                        
                parametros.put("TOTAL_ABONOSCREDITO",totalAbonosCredito); 
                parametros.put("NUM_ROWS_ABONOSCREDITO",(listAbonosCredito.size()-1)); //en este caso es de la list por el if de fecha abono a credito
                
                //Si la lista esta vacia agregamos un elemnto null para mostrarla
                if(listAbonosCredito.size()<=0){
                    //listAbonosCredito.add((TablaContado) nothing);
                }
                
                
                //New code
                System.setProperty("jasper.reports.compile.class.path",context.getRealPath("/WEB-INF/lib/jasperreports-3.7.4.jar") + context.getRealPath("/WEB-INF/"));			                        
                JasperReport reporteAbonoCredito = (JasperReport) JRLoader.loadObject(context.getRealPath("/reportes/tablaAbonosCredito.jasper"));
                parametros.put("REPORTE_ABONOS_CREDITO", reporteAbonoCredito);
                parametros.put("DATOS_ABONOS_CREDITO",new JRBeanCollectionDataSource(listAbonosCredito));
            
             //MAPEAMOS LOS DATOS AL Detail DEL JRXML (REPRESENTACION IMPRESA)
               ds3 = new JRBeanCollectionDataSource(listAbonosCredito); //Cambiar est aline apor otra colección 
               JRBeanCollectionDataSource report3DataSource = new JRBeanCollectionDataSource(listAbonosCredito);
               InputStream inputStreamReport3 = new FileInputStream(context.getRealPath("/reportes/tablaAbonosCredito.jrxml"));
               jasperDesignReport3 = JRXmlLoader.load(inputStreamReport3);
               JasperReport jasperReportReport3 = JasperCompileManager.compileReport(jasperDesignReport3);
               
               
           }catch(Exception e){e.printStackTrace();}
           
            /***
             * Fin Report Abonos  Credito
             */
        
            
            
            
            /***
             * Report Abonos Cheque
             */
            
            filtroBusqueda = "";
            strWhereRangoFechas ="";
            double totalCheque = 0;

            {               

                /*Filtro por rango de fechas*/
               if (!fechaMin.equals("") && !fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA_ABONO AS DATE) BETWEEN '"+fechaMin+"' AND '"+fechaMax+"')";                    
                }
                if (!fechaMin.equals("") && fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA_ABONO AS DATE)  >= '"+fechaMin+"')";                   
                }
                if (fechaMin.equals("") && !fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA_ABONO AS DATE)  <= '"+fechaMax+"')";                    
                }
            }
            
            if (!strWhereRangoFechas.trim().equals("")){
                filtroBusqueda += " AND " + strWhereRangoFechas;
            }
                        
            
            if (idUsuario>0){
                filtroBusqueda += " AND ID_USUARIO_VENDEDOR='" + idUsuario +"' ";               
            }

           filtroBusqueda += " AND ID_ESTATUS <>2 "; 
           filtroBusqueda += " AND ID_COBRANZA_METODO_PAGO = 3 "; //Solo pagados con cheque o vale
            
           System.out.println(" --------------Abonos Cheque------------------");
           
           
           JRBeanCollectionDataSource ds4 = null;
           JasperDesign jasperDesignReport4  = null;
            
           cobranzaAbonoDto = cobranzaAbonoBO.findCobranzaAbono(-1, idEmpresa , 0, 0, filtroBusqueda);
           
            try{
                listCheques = new ArrayList<TablaContado>();
                TablaContado tablaContado = null;
                int i = 1;              
                listCheques.add((TablaContado) nothing); //Registro Control
                
                for(SgfensCobranzaAbono cob:cobranzaAbonoDto){
                    
                    try{   
                        pedidoDto = pedidoBO.findPedidobyId(cob.getIdPedido());
                    }catch(Exception e){e.printStackTrace();}
                    //Si tiene factura
                    Folios folioDto = null;
                    try{
                        facturaDto = facturaBO.findComprobanteFiscal(idEmpresa, -1, -1, -1, "")[0];
                    }catch(Exception e){e.printStackTrace();}
                    if(facturaDto!=null){
                        try{
                            if(facturaDto.getIdFolio()>0){
                                folioDto =  foliosBO.findFolios(facturaDto.getIdFolio(), -1, -1, -1, "")[0];
                            }
                        }catch(Exception e){e.printStackTrace();}
                        
                    }
                    try{
                        clienteDto = clienteBO.findClientebyId(pedidoDto.getIdCliente());
                        
                    }catch(Exception e){e.printStackTrace();};
                    String nombreCliente = "";
                    if (clienteDto!=null){
                        nombreCliente = clienteDto.getRazonSocial();
                    }
                    
                    
                    
                    tablaContado = new TablaContado();
                    tablaContado.setNumeroSecuencial(i);
                    tablaContado.setFolioPedido(pedidoDto!=null?pedidoDto.getFolioPedido():"");
                    tablaContado.setSerieFactura(folioDto!=null?folioDto.getSerie():"");
                    tablaContado.setFolioFactura(facturaDto!=null?facturaDto.getFolioGenerado():"");
                    tablaContado.setNombreCliente(nombreCliente);
                    tablaContado.setNumeroReferencia(cob.getIdentificadorOperacion());
                    tablaContado.setNombreBanco("");
                    tablaContado.setMonto(cob.getMontoAbono());

                    listCheques.add(tablaContado);                     
                    i++;
                    
                    totalCheque += cob.getMontoAbono();
                }
                        
                  parametros.put("TOTAL_CHEQUE",totalCheque); 
                  parametros.put("NUM_ROWS_CHEQUE",cobranzaAbonoDto.length); 
                
                //Si la lista esta vacia agregamos un elemnto null para mostrarla
                if(listCheques.size()<=0){
                    //listAbonosCredito.add((TablaContado) nothing);
                }
                
                //New code
                System.setProperty("jasper.reports.compile.class.path",context.getRealPath("/WEB-INF/lib/jasperreports-3.7.4.jar") + context.getRealPath("/WEB-INF/"));			                        
                JasperReport reporteCheques = (JasperReport) JRLoader.loadObject(context.getRealPath("/reportes/tablaCheques.jasper"));
                parametros.put("REPORTE_CHEQUES", reporteCheques);
                parametros.put("DATOS_CHEQUES",new JRBeanCollectionDataSource(listCheques));
                 
                
            
             //MAPEAMOS LOS DATOS AL Detail DEL JRXML (REPRESENTACION IMPRESA)
               ds4 = new JRBeanCollectionDataSource(listCheques); //Cambiar est aline apor otra colección 
               JRBeanCollectionDataSource report4DataSource = new JRBeanCollectionDataSource(listCheques);
               InputStream inputStreamReport4 = new FileInputStream(context.getRealPath("/reportes/tablaCheques.jrxml"));
               jasperDesignReport4 = JRXmlLoader.load(inputStreamReport4);
               JasperReport jasperReportReport4 = JasperCompileManager.compileReport(jasperDesignReport4);
               
               
           }catch(Exception e){e.printStackTrace();}
           
            /***
             * Fin Report cheque
             */
            
            
            
            
            
            
            
            
            /***
             * Report Depositos
             */
            
            filtroBusqueda = "";
            strWhereRangoFechas ="";
            double totalDepositos = 0;

            {               

                /*Filtro por rango de fechas*/
               if (!fechaMin.equals("") && !fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA_DEPOSITO AS DATE) BETWEEN '"+fechaMin+"' AND '"+fechaMax+"')";                    
                }
                if (!fechaMin.equals("") && fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA_DEPOSITO AS DATE)  >= '"+fechaMin+"')";                   
                }
                if (fechaMin.equals("") && !fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA_DEPOSITO AS DATE)  <= '"+fechaMax+"')";                    
                }
            }
            
            if (!strWhereRangoFechas.trim().equals("")){
                filtroBusqueda += " AND " + strWhereRangoFechas;
            }
                        
            
             if (idUsuario>0){
                 Empleado empConsulta = null;
                try{
                    empConsulta = new EmpleadoDaoImpl(usuario.getConn()).findWhereIdUsuariosEquals(idUsuario)[0];
                }catch(Exception e){
                    System.out.println("No existe vendedor con ese id de usuario");
                    e.printStackTrace();
                }
                if(empConsulta!=null){
                    filtroBusqueda += " AND ID_VENDEDOR='" + empConsulta.getIdEmpleado() +"' ";   
               }  
            }

               
            
           System.out.println(" --------------Depositos-----------------");
           
           
           JRBeanCollectionDataSource ds5 = null;
           JasperDesign jasperDesignReport5  = null;
           
          
           sgfensCobranzaRegistroDepositoBancosDto = sgfensCobranzaRegistroDepositoBancoBO.findSgfensCobranzaRegistroDepositoBancos(-1, idEmpresa, 0, 0, filtroBusqueda);
            
            
            try{
                listDepositos = new ArrayList<TablaContado>();
                TablaContado tablaContado = null;
                int i = 1;              
                listDepositos.add((TablaContado) nothing); //Registro Control
                
                for(SgfensCobranzaRegistroDepositoBanco deposito:sgfensCobranzaRegistroDepositoBancosDto){                    
                    
                    
                    tablaContado = new TablaContado();
                    tablaContado.setNumeroSecuencial(i);                    
                    tablaContado.setNombreBanco("");
                    tablaContado.setNumeroReferencia(deposito.getNumReferenciaBoucher());
                    tablaContado.setMonto(deposito.getMonto());

                    listDepositos.add(tablaContado);                     
                    i++;
                    
                    totalDepositos += deposito.getMonto();
                }
                        
                parametros.put("TOTAL_DEPOSITOS",totalDepositos);
                parametros.put("NUM_ROWS_DEPOSITOS",sgfensCobranzaRegistroDepositoBancosDto.length); 
                
                //Si la lista esta vacia agregamos un elemnto null para mostrarla
                if(listDepositos.size()<=0){
                    //listDepositos.add((TablaContado) nothing);
                }
                
                
                //New code
                System.setProperty("jasper.reports.compile.class.path",context.getRealPath("/WEB-INF/lib/jasperreports-3.7.4.jar") + context.getRealPath("/WEB-INF/"));			                        
                JasperReport reporteDepositos = (JasperReport) JRLoader.loadObject(context.getRealPath("/reportes/tablaDepositos.jasper"));
                parametros.put("REPORTE_DEPOSITOS", reporteDepositos);
                parametros.put("DATOS_DEPOSITOS",new JRBeanCollectionDataSource(listDepositos));
            
             //MAPEAMOS LOS DATOS AL Detail DEL JRXML (REPRESENTACION IMPRESA)
               ds5 = new JRBeanCollectionDataSource(listDepositos); //Cambiar est aline apor otra colección 
               JRBeanCollectionDataSource report5DataSource = new JRBeanCollectionDataSource(listDepositos);
               InputStream inputStreamReport5 = new FileInputStream(context.getRealPath("/reportes/tablaDepositos.jrxml"));
               jasperDesignReport5 = JRXmlLoader.load(inputStreamReport5);
               JasperReport jasperReportReport5 = JasperCompileManager.compileReport(jasperDesignReport5);
               
               
           }catch(Exception e){e.printStackTrace();}
           
            /***
             * Fin Report Depositos
             */
            
            
            /***
             * Report Cuenta Efectivo
             */
            
            filtroBusqueda = "";
            strWhereRangoFechas ="";            
        

            {               

                /*Filtro por rango de fechas*/
               if (!fechaMin.equals("") && !fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA_HORA AS DATE) BETWEEN '"+fechaMin+"' AND '"+fechaMax+"')";                    
                }
                if (!fechaMin.equals("") && fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA_HORA AS DATE)  >= '"+fechaMin+"')";                   
                }
                if (fechaMin.equals("") && !fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA_HORA AS DATE)  <= '"+fechaMax+"')";                    
                }
            }
            
            if (!strWhereRangoFechas.trim().equals("")){
                filtroBusqueda += " AND " + strWhereRangoFechas;
            }
            

            if (idUsuario>0){
                 Empleado empConsulta = null;
                try{
                    empConsulta = new EmpleadoDaoImpl(usuario.getConn()).findWhereIdUsuariosEquals(idUsuario)[0];
                }catch(Exception e){
                    System.out.println("No existe vendedor con ese id de usuario");
                    e.printStackTrace();
                }
                if(empConsulta!=null){
                    filtroBusqueda += " AND ID_EMPLEADO='" + empConsulta.getIdEmpleado() +"' ";   
               }  
            }
               
            
           System.out.println(" --------------Cuenta Efectivo-----------------");
           
           
           JRBeanCollectionDataSource ds6 = null;
           JasperDesign jasperDesignReport6  = null;   
          
            cuentaEfectivoDto = cuentaEfectivoBO.findCuentaEfectivo(-1, -1, -1, filtroBusqueda);

            int billete1000 = 0;
            int billete500 = 0;
            int billete200 = 0;
            int billete100 = 0;
            int billete50 = 0;
            int billete20 = 0;
            int moneda20 = 0;
            int moneda10 = 0;
            int moneda5 = 0;
            int moneda2 = 0;
            int moneda1 = 0;
            int moneda0_50 = 0;
            int moneda0_20 = 0;
            int moneda0_10 = 0;
            double totalbillete1000 = 0;
            double totalbillete500 = 0;
            double totalbillete200 = 0;
            double totalbillete100 = 0;
            double totalbillete50 = 0;
            double totalbillete20 = 0;
            double totalmoneda20 = 0;
            double totalmoneda10 = 0;
            double totalmoneda5 = 0;
            double totalmoneda2 = 0;
            double totalmoneda1 = 0;
            double totalmoneda0_50 = 0;
            double totalmoneda0_20 = 0;
            double totalmoneda0_10 = 0;
            double totalGralEfectivo = 0;
            
            for(CuentaEfectivo efe:cuentaEfectivoDto){
                
                billete1000 += efe.getBillete1000();
                billete500 += efe.getBillete500();
                billete200 += efe.getBillete200();
                billete100 += efe.getBillete100();
                billete50 += efe.getBillete50();
                billete20 += efe.getBillete20();
                moneda20 += efe.getMoneda20();
                moneda10 += efe.getMoneda10();
                moneda5 += efe.getMoneda5();
                moneda2 += efe.getMoneda2();
                moneda1 += efe.getMoneda1();
                moneda0_50 += efe.getMoneda050();
                moneda0_20 += efe.getMoneda020();
                moneda0_10 += efe.getMoneda010();                
                
                
            }
                totalbillete1000 = billete1000 * 1000;
                totalbillete500 = billete500 * 500;
                totalbillete200 = billete200 * 200;
                totalbillete100 = billete100 * 100;
                totalbillete50 = billete50 * 50;
                totalbillete20 = billete20 * 20;
                totalmoneda20 = moneda20 * 20;
                totalmoneda10 = moneda10 * 10;
                totalmoneda5 = moneda5 * 5;
                totalmoneda2 = moneda2 * 2;
                totalmoneda1 = moneda1 * 1;
                totalmoneda0_50 = moneda0_50 * 0.50;
                totalmoneda0_20 = moneda0_20 * 0.20;
                totalmoneda0_10 = moneda0_10 * 0.10;
                
                
                totalGralEfectivo = totalbillete1000 + totalbillete500 + totalbillete200 + totalbillete100 + totalbillete50 + totalbillete20
                                 + totalmoneda20 + totalmoneda10 + totalmoneda5 + totalmoneda2 + totalmoneda1 + totalmoneda0_50 + totalmoneda0_20 +totalmoneda0_10;
                
                parametros.put("TOTAL_EFECTIVO",totalGralEfectivo);   
            
            try{
                listCuentaEfectivo = new ArrayList<TablaContado>();
                TablaContado tablaContado = null;
                          
                listCuentaEfectivo.add((TablaContado) nothing); //Registro Control
                
                tablaContado = new TablaContado();
                tablaContado.setNumeroReferencia("$ 1,000.00");
                tablaContado.setCantidad(billete1000);
                tablaContado.setMonto(totalbillete1000);
                listCuentaEfectivo.add(tablaContado);
                tablaContado = new TablaContado();
                tablaContado.setNumeroReferencia("$ 500.00");
                tablaContado.setCantidad(billete500);
                tablaContado.setMonto(totalbillete500);
                listCuentaEfectivo.add(tablaContado);
                tablaContado = new TablaContado();
                tablaContado.setNumeroReferencia("$ 200.00");
                tablaContado.setCantidad(billete200);
                tablaContado.setMonto(totalbillete200);
                listCuentaEfectivo.add(tablaContado);
                tablaContado = new TablaContado();
                tablaContado.setNumeroReferencia("$ 100.00");
                tablaContado.setCantidad(billete100);
                tablaContado.setMonto(totalbillete100);
                listCuentaEfectivo.add(tablaContado);
                tablaContado = new TablaContado();
                tablaContado.setNumeroReferencia("$ 50.00");
                tablaContado.setCantidad(billete50);
                tablaContado.setMonto(totalbillete50);
                listCuentaEfectivo.add(tablaContado);
                tablaContado = new TablaContado();
                tablaContado.setNumeroReferencia("$ 20.00");
                tablaContado.setCantidad(billete20);
                tablaContado.setMonto(totalbillete20);
                listCuentaEfectivo.add(tablaContado);
                tablaContado = new TablaContado();
                tablaContado.setNumeroReferencia("$ 20.00");
                tablaContado.setCantidad(moneda20);
                tablaContado.setMonto(totalmoneda20);
                listCuentaEfectivo.add(tablaContado);
                tablaContado = new TablaContado();
                tablaContado.setNumeroReferencia("$ 10.00");
                tablaContado.setCantidad(moneda10);
                 tablaContado.setMonto(totalmoneda10);
                listCuentaEfectivo.add(tablaContado);
                tablaContado = new TablaContado();
                tablaContado.setNumeroReferencia("$ 5.00");
                tablaContado.setCantidad(moneda5);
                 tablaContado.setMonto(totalmoneda5);
                listCuentaEfectivo.add(tablaContado);
                tablaContado = new TablaContado();
                tablaContado.setNumeroReferencia("$ 2.00");
                tablaContado.setCantidad(moneda2);
                 tablaContado.setMonto(totalmoneda2);
                listCuentaEfectivo.add(tablaContado);
                tablaContado = new TablaContado();
                tablaContado.setNumeroReferencia("$ 1.00");
                tablaContado.setCantidad(moneda1);
                tablaContado.setMonto(totalmoneda1);
                listCuentaEfectivo.add(tablaContado);
                tablaContado = new TablaContado();
                tablaContado.setNumeroReferencia("$ 0.50");
                tablaContado.setCantidad(moneda0_50);
                tablaContado.setMonto(totalmoneda0_50);
                listCuentaEfectivo.add(tablaContado);
                tablaContado = new TablaContado();
                tablaContado.setNumeroReferencia("$ 0.20");
                tablaContado.setCantidad(moneda0_20);
                tablaContado.setMonto(totalmoneda0_20);
                listCuentaEfectivo.add(tablaContado);
                tablaContado = new TablaContado();
                tablaContado.setNumeroReferencia("$ 0.10");
                tablaContado.setCantidad(moneda0_10);
                tablaContado.setMonto(totalmoneda0_10);
                listCuentaEfectivo.add(tablaContado);
                        
                
                //Si la lista esta vacia agregamos un elemnto null para mostrarla
                if(listCuentaEfectivo.size()<=0){
                    //listCuentaEfectivo.add((TablaContado) nothing);
                }
                
                //New code
                System.setProperty("jasper.reports.compile.class.path",context.getRealPath("/WEB-INF/lib/jasperreports-3.7.4.jar") + context.getRealPath("/WEB-INF/"));			                        
                JasperReport reporteEfectivo = (JasperReport) JRLoader.loadObject(context.getRealPath("/reportes/tablaContarDinero.jasper"));
                parametros.put("REPORTE_EFECTIVO", reporteEfectivo);
                parametros.put("DATOS_EFECTIVO",new JRBeanCollectionDataSource(listCuentaEfectivo));
            
             //MAPEAMOS LOS DATOS AL Detail DEL JRXML (REPRESENTACION IMPRESA)
               ds6 = new JRBeanCollectionDataSource(listCuentaEfectivo); //Cambiar est aline apor otra colección 
               JRBeanCollectionDataSource report6DataSource = new JRBeanCollectionDataSource(listCuentaEfectivo);
               InputStream inputStreamReport6 = new FileInputStream(context.getRealPath("/reportes/tablaContarDinero.jrxml"));
               jasperDesignReport6 = JRXmlLoader.load(inputStreamReport6);
               JasperReport jasperReportReport6 = JasperCompileManager.compileReport(jasperDesignReport6);
               
               
           }catch(Exception e){e.printStackTrace();}
           
            /***
             * Fin Report efectivo
             */
            
            
            
            /***
             * Report Gastos
             */
            
            filtroBusqueda = "";
            strWhereRangoFechas ="";
            double totalgastos = 0;
            String conceptoGasto = "";

            {               

                /*Filtro por rango de fechas*/
               if (!fechaMin.equals("") && !fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA AS DATE) BETWEEN '"+fechaMin+"' AND '"+fechaMax+"')";                    
                }
                if (!fechaMin.equals("") && fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA AS DATE)  >= '"+fechaMin+"')";                   
                }
                if (fechaMin.equals("") && !fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA AS DATE)  <= '"+fechaMax+"')";                    
                }
            }
            
            if (!strWhereRangoFechas.trim().equals("")){
                filtroBusqueda += " AND " + strWhereRangoFechas;
            }
                        
            
             if (idUsuario>0){
                 Empleado empConsulta = null;
                try{
                    empConsulta = new EmpleadoDaoImpl(usuario.getConn()).findWhereIdUsuariosEquals(idUsuario)[0];
                }catch(Exception e){
                    System.out.println("No existe vendedor con ese id de usuario");
                    e.printStackTrace();
                }
                if(empConsulta!=null){
                    filtroBusqueda += " AND ID_EMPLEADO='" + empConsulta.getIdEmpleado() +"' ";   
               }  
            }
                        

               
            
           System.out.println(" --------------Gastos----------------");
           
           
           JRBeanCollectionDataSource ds7 = null;
           JasperDesign jasperDesignReport7  = null;   
          
           gastosEvcDto = gastosEvcBO.findGastosEvc(-1, idEmpresa, 0, 0, filtroBusqueda);
            
            try{
                listGastos = new ArrayList<TablaContado>();
                TablaContado tablaContado = null;
                int i = 1;                        
                listGastos.add((TablaContado) nothing); //Registro Control 
                
                for(GastosEvc gasto:gastosEvcDto){               
                
                try{
                    conceptoGasto = new CatalogoGastosBO(gasto.getIdConcepto(),usuario.getConn()).getCatalogoGastos().getNombre() ;
                }catch(Exception e){}
                    
                    tablaContado = new TablaContado();
                    tablaContado.setNumeroSecuencial(i);
                    tablaContado.setNumeroReferencia(conceptoGasto);
                    tablaContado.setTxtLibre(gasto.getComentario());
                    tablaContado.setMonto(gasto.getMonto());
                    
                    listGastos.add(tablaContado);   
                    i++;
                    
                    totalgastos += gasto.getMonto();
                
                }       
                
                parametros.put("TOTAL_GASTOS",totalgastos);
                parametros.put("NUM_ROWS_GASTOS",gastosEvcDto.length); 
                
                
                //Si la lista esta vacia agregamos un elemnto null para mostrarla
                if(listGastos.size()<=0){
                    //listGastos.add((TablaContado) nothing);
                }
            
                //New code
                System.setProperty("jasper.reports.compile.class.path",context.getRealPath("/WEB-INF/lib/jasperreports-3.7.4.jar") + context.getRealPath("/WEB-INF/"));			                        
                JasperReport reporteGastos = (JasperReport) JRLoader.loadObject(context.getRealPath("/reportes/tablaGastos.jasper"));
                parametros.put("REPORTE_GASTOS", reporteGastos);
                parametros.put("DATOS_GASTOS",new JRBeanCollectionDataSource(listGastos));
                
             //MAPEAMOS LOS DATOS AL Detail DEL JRXML (REPRESENTACION IMPRESA)
               ds7 = new JRBeanCollectionDataSource(listGastos); //Cambiar est aline apor otra colección 
               JRBeanCollectionDataSource report7DataSource = new JRBeanCollectionDataSource(listGastos);
               InputStream inputStreamReport7 = new FileInputStream(context.getRealPath("/reportes/tablaGastos.jrxml"));
               jasperDesignReport7 = JRXmlLoader.load(inputStreamReport7);
               JasperReport jasperReportReport7 = JasperCompileManager.compileReport(jasperDesignReport7);
               
               
           }catch(Exception e){e.printStackTrace();}
           
            /***
             * Fin Report Gastos
             */
            
            
            
             
               
             /***
             * Report Cambios y Devoluciones (Devuletos)
             */
            
            filtroBusqueda = "";
            strWhereRangoFechas ="";
            double totalDevueltos = 0 ;

            {
                

                /*Filtro por rango de fechas*/
                if (!fechaMin.equals("") && !fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA AS DATE) BETWEEN '"+fechaMin+"' AND '"+fechaMax+"')";                    
                }
                if (!fechaMin.equals("") && fechaMax.equals("")){
                    strWhereRangoFechas="(FECHA  >= '"+fechaMin+"')";                   
                }
                if (fechaMin.equals("") && !fechaMax.equals("")){
                    strWhereRangoFechas="(FECHA  <= '"+fechaMax+"')";                    
                }
            }
            
            if (!strWhereRangoFechas.trim().equals("")){
                filtroBusqueda += " AND " + strWhereRangoFechas;
            }
                        
            
            
            if (idUsuario>0){
                 Empleado empConsulta = null;
                try{
                    empConsulta = new EmpleadoDaoImpl(usuario.getConn()).findWhereIdUsuariosEquals(idUsuario)[0];
                }catch(Exception e){
                    System.out.println("No existe vendedor con ese id de usuario");
                    e.printStackTrace();
                }
                if(empConsulta!=null){
                    filtroBusqueda += " AND ID_EMPLEADO='" + empConsulta.getIdEmpleado() +"' ";   
               }  
            }
            
                     
           
           System.out.println(" --------------Devoluciones y cambios (Devueltos)------------------");
           
           
           JRBeanCollectionDataSource ds8 = null;
           JasperDesign jasperDesignReport8  = null;
           
           
           sgfensPedidoDevolucionCambiosDto = sgfensPedidoDevolucionCambioBO.findSgfensPedidoDevolucionCambios(-1, idEmpresa, -1, -1, filtroBusqueda);          
            
            
            
            try{
                listCambiosDev = new ArrayList<TablaContado>();
                TablaContado tablaContado = null;
                int i = 1;              
                listCambiosDev.add((TablaContado) nothing); //Registro Control
                
                for(SgfensPedidoDevolucionCambio item:sgfensPedidoDevolucionCambiosDto){
                    
                    
                    try{   
                        pedidoDto = pedidoBO.findPedidobyId(item.getIdPedido());
                    }catch(Exception e){e.printStackTrace();}
                    
                    double precioVenta = 0;
                    try{   
                        SgfensPedidoProductoDaoImpl pedConDao = new SgfensPedidoProductoDaoImpl(usuario.getConn());
                        String qry = " ID_PEDIDO = "+ item.getIdPedido() + " AND ID_CONCEPTO = " + item.getIdConcepto();
                        SgfensPedidoProducto pedidoProdDto = pedConDao.findByDynamicWhere(qry, null)[0];
                        precioVenta = pedidoProdDto.getPrecioUnitario();
                    }catch(Exception e){}
                    
                    try{
                        clienteDto = clienteBO.findClientebyId(pedidoDto.getIdCliente());
                        
                    }catch(Exception e){e.printStackTrace();};
                    String nombreCliente = "";
                    if (clienteDto!=null){
                        nombreCliente = clienteDto.getRazonSocial();
                    }
                    
                    Concepto con = null;
                    ConceptoBO conBO =  null;
                    try{
                        conBO = new ConceptoBO(item.getIdConcepto(), usuario.getConn());
                        con = conBO.getConcepto();                                                        
                    }catch(Exception e){}                   
                    
                    
                    double desglose = 0;
                    
                    try{
                        desglose = con.getDesglosePiezas() * (item.getAptoParaVenta()+item.getNoAptoParaVenta());
                    }catch(Exception e){e.printStackTrace();}
                    
                    double monto = precioVenta*(item.getAptoParaVenta()+item.getNoAptoParaVenta());
                    
                    tablaContado = new TablaContado();
                    tablaContado.setNumeroSecuencial(i);  
                    tablaContado.setNumeroReferencia(item.getIdTipo()==1?"Devolución":item.getIdTipo()==2?"Cambio":"");
                    tablaContado.setNombreCliente(nombreCliente);
                    tablaContado.setProducto(con!=null?conBO.desencripta(con.getNombre()):"");
                    tablaContado.setPiezas(desglose);
                    tablaContado.setCantidad(item.getAptoParaVenta()+item.getNoAptoParaVenta());                  
                    tablaContado.setMonto(monto);                    

                    listCambiosDev.add(tablaContado);
                    i++;
                    
                    totalDevueltos += monto;
                }
                
                parametros.put("TOTAL_DEVUELTOS",totalDevueltos);
                parametros.put("NUM_ROWS_DEVUELTOS",sgfensPedidoDevolucionCambiosDto.length); 

                
                          
                //Si la lista esta vacia agregamos un elemnto null para mostrarla
                if(listCambiosDev.size()<=0){
                    //listCambiosDev.add((TablaContado) nothing);
                }
               
                
                //New code
                System.setProperty("jasper.reports.compile.class.path",context.getRealPath("/WEB-INF/lib/jasperreports-3.7.4.jar") + context.getRealPath("/WEB-INF/"));			                        
                JasperReport reporteDev = (JasperReport) JRLoader.loadObject(context.getRealPath("/reportes/tablaCambiosDev.jasper"));
                parametros.put("REPORTE_DEV", reporteDev);
                parametros.put("DATOS_DEV",new JRBeanCollectionDataSource(listCambiosDev));
            
             //MAPEAMOS LOS DATOS AL Detail DEL JRXML (REPRESENTACION IMPRESA)
               ds8 = new JRBeanCollectionDataSource(listCambiosDev); //Cambiar est aline apor otra colección 
               JRBeanCollectionDataSource report8DataSource = new JRBeanCollectionDataSource(listCambiosDev);
               InputStream inputStreamReport8 = new FileInputStream(context.getRealPath("/reportes/tablaCambiosDev.jrxml"));
               jasperDesignReport8 = JRXmlLoader.load(inputStreamReport8);
               JasperReport jasperReportReport8 = JasperCompileManager.compileReport(jasperDesignReport8);
               
               
           }catch(Exception e){e.printStackTrace();}
           
            /***
             * Fin Report  Cambios y Devoluciones (Devueltos)
             */
            
            /***
             * Report Cambios y Devoluciones (Entregas)
             */
            
            filtroBusqueda = "";
            strWhereRangoFechas ="";
            double totalEntregados = 0;

            {
                

                /*Filtro por rango de fechas*/
                if (!fechaMin.equals("") && !fechaMax.equals("")){
                    strWhereRangoFechas="(CAST(FECHA AS DATE) BETWEEN '"+fechaMin+"' AND '"+fechaMax+"')";                    
                }
                if (!fechaMin.equals("") && fechaMax.equals("")){
                    strWhereRangoFechas="(FECHA  >= '"+fechaMin+"')";                   
                }
                if (fechaMin.equals("") && !fechaMax.equals("")){
                    strWhereRangoFechas="(FECHA  <= '"+fechaMax+"')";                    
                }
            }
            
            if (!strWhereRangoFechas.trim().equals("")){
                filtroBusqueda += " AND " + strWhereRangoFechas;
            }
                        
            
            
            if (idUsuario>0){
                 Empleado empConsulta = null;
                try{
                    empConsulta = new EmpleadoDaoImpl(usuario.getConn()).findWhereIdUsuariosEquals(idUsuario)[0];
                }catch(Exception e){
                    System.out.println("No existe vendedor con ese id de usuario");
                    e.printStackTrace();
                }
                if(empConsulta!=null){
                    filtroBusqueda += " AND ID_EMPLEADO='" + empConsulta.getIdEmpleado() +"' ";   
               }  
            }
            
           filtroBusqueda += " AND ID_CONCEPTO_ENTREGADO > 0";
           
           System.out.println(" --------------Devoluciones y cambios (Entregados)------------------");
           
           
           JRBeanCollectionDataSource ds9 = null;
           JasperDesign jasperDesignReport9  = null;
           
           
           sgfensPedidoDevolucionCambiosDto = sgfensPedidoDevolucionCambioBO.findSgfensPedidoDevolucionCambios(-1, idEmpresa, -1, -1, filtroBusqueda);          
            
                        
            try{
                listCambiosDevEntregas = new ArrayList<TablaContado>();
                TablaContado tablaContado = null;
                int i = 1;              
                listCambiosDevEntregas.add((TablaContado) nothing); //Registro Control
                
                for(SgfensPedidoDevolucionCambio item:sgfensPedidoDevolucionCambiosDto){                    
                    
                                        
                    try{
                        clienteDto = clienteBO.findClientebyId(pedidoDto.getIdCliente());
                        
                    }catch(Exception e){e.printStackTrace();};
                    String nombreCliente = "";
                    if (clienteDto!=null){
                        nombreCliente = clienteDto.getRazonSocial();
                    }
                    
                    Concepto con = null;
                    ConceptoBO conBO =  null;
                    try{
                        conBO = new ConceptoBO(item.getIdConceptoEntregado(), usuario.getConn());
                        con = conBO.getConcepto();                                                        
                    }catch(Exception e){}                   
                    
                    
                    double precioVenta = 0;
                    try{   
                        SgfensPedidoProductoDaoImpl pedConDao = new SgfensPedidoProductoDaoImpl(usuario.getConn());
                        String qry = " ID_PEDIDO = "+ item.getIdPedido() + " AND ID_CONCEPTO = " + item.getIdConcepto();
                        SgfensPedidoProducto pedidoProdDto = pedConDao.findByDynamicWhere(qry, null)[0];
                        //calculamoso precio unitario    --> El resultado sera 0 si se marcaron como no aptos para venta
                       precioVenta =  (((item.getAptoParaVenta()*pedidoProdDto.getPrecioUnitario())+item.getMontoResultante())/item.getCantidadDevuelta());
                        
                      
                    }catch(Exception e){e.printStackTrace();}
                    
                    
                    double desglose = 0;
                    
                    try{
                        desglose = con.getDesglosePiezas() * item.getCantidadDevuelta();
                    }catch(Exception e){e.printStackTrace();}
                    
                    tablaContado = new TablaContado();
                    tablaContado.setNumeroSecuencial(i);  
                    tablaContado.setNumeroReferencia(item.getIdTipo()==1?"Devolución":item.getIdTipo()==2?"Cambio":"");
                    tablaContado.setNombreCliente(nombreCliente);
                    tablaContado.setProducto(con!=null?conBO.desencripta(con.getNombre()):"");
                    tablaContado.setPiezas(desglose);
                    tablaContado.setCantidad(item.getCantidadDevuelta());                  
                    tablaContado.setMonto(precioVenta * item.getCantidadDevuelta());                    

                    listCambiosDevEntregas.add(tablaContado);
                    i++;
                    
                    totalEntregados += (precioVenta * item.getCantidadDevuelta());
                }
                
                parametros.put("TOTAL_ENTREGADOS",totalEntregados);
                parametros.put("NUM_ROWS_ENTREGADOS",sgfensPedidoDevolucionCambiosDto.length);
                          
                //Si la lista esta vacia agregamos un elemnto null para mostrarla
                if(listCambiosDevEntregas.size()<=0){
                    //listCambiosDevEntregas.add((TablaContado) nothing);
                }
               
            
                //New code
                System.setProperty("jasper.reports.compile.class.path",context.getRealPath("/WEB-INF/lib/jasperreports-3.7.4.jar") + context.getRealPath("/WEB-INF/"));			                        
                JasperReport reporteDev = (JasperReport) JRLoader.loadObject(context.getRealPath("/reportes/tablaCambiosDevEntregas.jasper"));
                parametros.put("REPORTE_ENTREGADOS", reporteDev);
                parametros.put("DATOS_ENTREGADOS",new JRBeanCollectionDataSource(listCambiosDevEntregas));
                
             //MAPEAMOS LOS DATOS AL Detail DEL JRXML (REPRESENTACION IMPRESA)
               ds9 = new JRBeanCollectionDataSource(listCambiosDevEntregas); //Cambiar est aline apor otra colección 
               JRBeanCollectionDataSource report9DataSource = new JRBeanCollectionDataSource(listCambiosDevEntregas);
               InputStream inputStreamReport9 = new FileInputStream(context.getRealPath("/reportes/tablaCambiosDevEntregas.jrxml"));
               jasperDesignReport9 = JRXmlLoader.load(inputStreamReport9);
               JasperReport jasperReportReport9 = JasperCompileManager.compileReport(jasperDesignReport9);
               
               
           }catch(Exception e){e.printStackTrace();}
           
            /***
             * Fin Report Entregas
             */
            
            
            //Total gral
            double totalGeneral = 0;            
            totalGeneral =  totalContado + totalCredito + totalAbonosCredito - totalgastos ;
            
             parametros.put("TOTAL_GENERAL",totalGeneral);
            
            
            System.setProperty("jasper.reports.compile.class.path","/WEB-INF/lib/jasperreports-3.7.4.jar" + System.getProperty("path.separator") + context.getRealPath("/WEB-INF/"));

                  

            //REPRESENTACION IMPRESA PERSONALIZADA:
          
            try{
                                
                
                JasperReport jreport0 = JasperCompileManager.compileReport(jasperDesignReport0);
                JasperPrint jprint0 = JasperFillManager.fillReport(jreport0,parametros,ds0);
                                
                JasperReport jreport1 = JasperCompileManager.compileReport(jasperDesignReport1);
                JasperPrint jprint1 = JasperFillManager.fillReport(jreport1,parametros,ds1);
                
                JasperReport jreport2 = JasperCompileManager.compileReport(jasperDesignReport2);
                JasperPrint jprint2 = JasperFillManager.fillReport(jreport2,parametros,ds2);
                
                JasperReport jreport3 = JasperCompileManager.compileReport(jasperDesignReport3);
                JasperPrint jprint3 = JasperFillManager.fillReport(jreport3,parametros,ds3);
                
                JasperReport jreport4 = JasperCompileManager.compileReport(jasperDesignReport4);
                JasperPrint jprint4 = JasperFillManager.fillReport(jreport4,parametros,ds4);
                
                JasperReport jreport5 = JasperCompileManager.compileReport(jasperDesignReport5);
                JasperPrint jprint5 = JasperFillManager.fillReport(jreport5,parametros,ds5);
                
                JasperReport jreport6 = JasperCompileManager.compileReport(jasperDesignReport6);
                JasperPrint jprint6 = JasperFillManager.fillReport(jreport6,parametros,ds6);
                
                JasperReport jreport7 = JasperCompileManager.compileReport(jasperDesignReport7);
                JasperPrint jprint7 = JasperFillManager.fillReport(jreport7,parametros,ds7);                
                
                JasperReport jreport8 = JasperCompileManager.compileReport(jasperDesignReport8);
                JasperPrint jprint8 = JasperFillManager.fillReport(jreport8,parametros,ds8);
                
                JasperReport jreport9 = JasperCompileManager.compileReport(jasperDesignReport9);
                JasperPrint jprint9 = JasperFillManager.fillReport(jreport9,parametros,ds9);
                
                List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();
                // Your code to get Jasperreport objects                
                jasperPrints.add(jprint0);         
                /*jasperPrints.add(jprint1);                
                jasperPrints.add(jprint2);
                jasperPrints.add(jprint3);
                jasperPrints.add(jprint4);
                jasperPrints.add(jprint5);
                jasperPrints.add(jprint6);
                jasperPrints.add(jprint7);
                jasperPrints.add(jprint8);
                jasperPrints.add(jprint9);*/
                
           

                              
                
                
                //reportGralPDF = FileManage.createFileFromByteArray(baos.toByteArray(),
                //                                 (appConfig.getApp_content_path()+"/reportesTmp") + File.separator , 
                //                                 "pruebReportGral.pdf");
                
                //FileManage fileManage = new FileManage();
                //fileManage.createFileFromByteArray(bytesFinal, (appConfig.getApp_content_path()+"/reportesTmp/"), "pruebReportGral"+".pdf");
                System.out.println("Exito Jasper......");
                //return bytesFinal;
                
                return jasperPrints;
                
                
                
            }catch(Exception e){
                e.printStackTrace();
            }
             
        }catch(Exception e){e.printStackTrace();}
        
        
        return null;
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
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

    private void exportPdf(HttpServletResponse response ,  List<JasperPrint> jasperPrints) {
        
        JRPdfExporter exporter = new JRPdfExporter();
        //Create new FileOutputStream or you can use Http Servlet Response.getOutputStream() to get Servlet output stream
        // Or if you want bytes create ByteArrayOutputStream
        ByteArrayOutputStream outSt = new ByteArrayOutputStream();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrints);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outSt);
        try {
            exporter.exportReport();
        } catch (JRException ex) {
            ex.printStackTrace();
        }
        byte[] bytesFinal = outSt.toByteArray();        
        
        
        ByteArrayOutputStream bPDF = new ByteArrayOutputStream(bytesFinal.length); 
        bPDF.write(bytesFinal, 0, bytesFinal.length);
        
        
        if (bPDF!=null){
            ServletOutputStream outputStream = null;
            try {
                response.setContentType("application/pdf");
                response.setContentLength(bPDF.size());
                response.setHeader("Content-Disposition", "attachment; filename=reporte.pdf");
                outputStream = response.getOutputStream();
                bPDF.writeTo(outputStream);
                outputStream.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }else{
            try (PrintWriter out = response.getWriter()) {
                out.println("<script> alert('El reporte que se intenta descargar esta vacío. Intente con otro filtro de busqueda.'); "
                        + " window.close(); </script>");
                out.println("<h1>El reporte que se intenta descargar esta vacío. Intente de nuevo.</h1>");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        
    }
    
    private void exportXls(HttpServletResponse response ,  List<JasperPrint> jasperPrints) throws IOException {        
      
     
     JRXlsExporter exporterXLS = new JRXlsExporter(); 
     ByteArrayOutputStream outSt = new ByteArrayOutputStream();
     //String[] sheetNames ={"Cobros de contado","Ventas a crédito","Abonos","Cheques","Depósitos","Cuenta de efectivo","Gastos","Devoluciones y Cambios (Recibidos)","Devoluciones y Cambios (Entregados)"};
     String[] sheetNames ={"Reporte Gral del día"};
 
     exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, jasperPrints);
     exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, outSt);
     exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
     exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
     exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
     exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
     exporterXLS.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.FALSE);   
     exporterXLS.setParameter(JRXlsExporterParameter.SHEET_NAMES,sheetNames);
     exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
     exporterXLS.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);

     
        try {
            exporterXLS.exportReport();
        } catch (JRException ex) {
            ex.printStackTrace();
        }

        byte[] bytesFinal = outSt.toByteArray();      
        
        
        ByteArrayOutputStream bPDF = new ByteArrayOutputStream(bytesFinal.length); 
        bPDF.write(bytesFinal, 0, bytesFinal.length);
        
        if (bPDF!=null){
            ServletOutputStream outputStream = null;
            try {
                response.setContentType("application/vnd.ms-excel");
                response.setContentLength(bPDF.size());
                 response.setHeader("Content-disposition", "attachment; filename=reporte.xls");
                outputStream = response.getOutputStream();
                bPDF.writeTo(outputStream);
                outputStream.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }else{
            try (PrintWriter out = response.getWriter()) {
                out.println("<script> alert('El reporte que se intenta descargar esta vacío. Intente con otro filtro de busqueda.'); "
                        + " window.close(); </script>");
                out.println("<h1>El reporte que se intenta descargar esta vacío. Intente de nuevo.</h1>");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        
        
    }
    
}
