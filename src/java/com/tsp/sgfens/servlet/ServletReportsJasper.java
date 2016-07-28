/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.servlet;

import com.tsp.sct.bo.CategoriaBO;
import com.tsp.sct.bo.ConceptoBO;
import com.tsp.sct.bo.EmpresaBO;
import com.tsp.sct.bo.ExistenciaAlmacenBO;
import com.tsp.sct.bo.ImagenPersonalBO;
import com.tsp.sct.bo.MarcaBO;
import com.tsp.sct.bo.UsuarioBO;
import com.tsp.sct.config.Configuration;
import com.tsp.sct.dao.dto.Categoria;
import com.tsp.sct.dao.dto.Concepto;
import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.dao.dto.ImagenPersonal;
import com.tsp.sct.dao.dto.Marca;
import com.tsp.sct.dao.exceptions.EmpresaDaoException;
import com.tsp.sct.dao.jdbc.EmpresaDaoImpl;
import com.tsp.sct.dao.jdbc.MarcaDaoImpl;
import com.tsp.sct.reports.dto.ReporteMarcaCategoria;
import com.tsp.sct.util.FileManage;
import com.tsp.sct.util.GenericUtil;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @author HpPyme
 */
public class ServletReportsJasper extends HttpServlet {
    
    Configuration appConfig = new Configuration();
    Map<String, Object> parametros = new HashMap<String, Object>();     
    String pathReports = appConfig.getApp_content_path()+"/reportesTmp/";
    public static final int REPORTE_MARCA_CATEGORIA = 1;    
    UsuarioBO usuario = new UsuarioBO();
    GenericUtil utils = new GenericUtil(); 
    ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(usuario.getConn());
    private ImagenPersonal ip = null;
    List<ReporteMarcaCategoria> listMarcaCats = null;
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
             TODO output your page here. You may use following sample code. 
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ServletReportsJasper</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ServletReportsJasper at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
                      
        }   */  
        
        int tipoReporte = -1;
        String fitroBusqueda ="";

        try{
            tipoReporte = Integer.parseInt(request.getParameter("tipoReporte"));//Tipo reporte
        }catch(NumberFormatException e){}

        int idEmpresa = -1;
        try{
            idEmpresa = Integer.parseInt(request.getParameter("idEmpresa"));
        }catch(NumberFormatException e){}
        
        int idMarca = -1;
        try{
            idMarca = Integer.parseInt(request.getParameter("idMarca"));
        }catch(NumberFormatException e){}
        
        int idCategoria = -1;
        try{
            idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
        }catch(NumberFormatException e){}
        
        String tipo = request.getParameter("tipo")!=null? new String(request.getParameter("tipo").getBytes("ISO-8859-1"),"UTF-8") :"pdf"; //formato reporte


        List<JasperPrint> reportPrint = generaReporte(tipoReporte,idEmpresa,idMarca,idCategoria,fitroBusqueda);     
        
        if (tipo.equals("pdf")) {
             this.exportPdf(response,reportPrint);
        }else if (tipo.equals("xls")) {
           this.exportXls(response, reportPrint);
        }
        
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

    private List<JasperPrint> generaReporte(int REPORTE,int idEmpresa, int idMarca, int idCategoria, String fitroBusqueda) {
        
               
        ServletContext context = getServletContext();
        Empresa empresa = new EmpresaBO(idEmpresa,usuario.getConn()).getEmpresa();       
      


        ip = new ImagenPersonal();
        ImagenPersonalBO imagenPersonalBO = new ImagenPersonalBO(usuario.getConn());
        try{
            ip = imagenPersonalBO.findImagenPersonalByEmpresa(empresa.getIdEmpresa());
        }catch(Exception e){ip= null;}

        // SI LA EMPRESA TIENE UN LOGO SE VA A CARGAR EL PROPIO//                        
        if (ip != null) {
                parametros.put("LOGO","C:/SystemaDeArchivos/"+ empresa.getRfc()+ "/"+ ip.getNombreImagen());
        }               
        parametros.put("EMPRESA_RAZON",empresa.getNombreComercial());    
               
               
                   listMarcaCats = new ArrayList<ReporteMarcaCategoria>();
                   MarcaBO marcaBO = new  MarcaBO(usuario.getConn());
                   Marca[] marcasDtos =  null;
                   CategoriaBO catsBO = new CategoriaBO(usuario.getConn());
                   Categoria[] catsDtos =  null;
                   ConceptoBO conceptoBO =  new  ConceptoBO(usuario.getConn());
                   Concepto[] conceptoDtos = null;
                   
                   JRBeanCollectionDataSource ds = null;
                   JasperDesign jasperDesignReport1  = null;
                   
                   
                   try{
                       
                        marcasDtos = marcaBO.findMarcas(idMarca, idEmpresa, -1, -1, "");
                        
                        
                   }catch(Exception e){e.printStackTrace();}
                   
                   for(Marca marca:marcasDtos){                          
                     
                       catsDtos = catsBO.findCategorias(idCategoria, idEmpresa, -1, -1, " AND id_categoria_padre = -1 ");
                       
                       for(Categoria cat:catsDtos){
                           
                           conceptoDtos = conceptoBO.findConceptos(-1, idEmpresa, -1, -1, " AND ID_MARCA = " +  marca.getIdMarca() + " AND id_categoria=" + cat.getIdCategoria());
                                   
                           for(Concepto concept : conceptoDtos){                               
                               
                               double stockGral = 0;
                               try{
                                   

                                    if (concept.getIdConcepto() > 0) {                                       
                                        //Obtenemos stock gral del prod
                                        stockGral = exisAlmBO.getExistenciaGeneralByEmpresaProducto(idEmpresa, concept.getIdConcepto());
                                    }
                               }catch(Exception e){e.printStackTrace();}
                               
                               
                               ReporteMarcaCategoria report = new ReporteMarcaCategoria();
                               report.setNombreMarca(marca.getNombre());      
                               report.setNombreCategoria(cat.getNombreCategoria());
                               report.setNombreProducto(concept.getNombreDesencriptado());
                               report.setPiezas(stockGral);
                               report.setPeso(concept.getPeso());
                               report.setTotal(stockGral*concept.getPrecio());
                               listMarcaCats.add(report);
                           }
                           
                       }
                       
                       
                   }
                   
                   parametros.put("NUM_ROWS",listMarcaCats.size());
                    try{         
                        
                        //MAPEAMOS LOS DATOS AL Detail DEL JRXML (REPRESENTACION IMPRESA)
                   
                       ds = new JRBeanCollectionDataSource(listMarcaCats); //Cambiar est aline apor otra colección  
                       JRBeanCollectionDataSource report1DataSource = new JRBeanCollectionDataSource(listMarcaCats);
                       InputStream inputStreamReport1 = new FileInputStream(context.getRealPath("/reportes/reporteMarcaTipo.jrxml"));
                       jasperDesignReport1 = JRXmlLoader.load(inputStreamReport1);
                       JasperReport jasperReportReport1 = JasperCompileManager.compileReport(jasperDesignReport1);
                        
                       
                       System.setProperty("jasper.reports.compile.class.path","/WEB-INF/lib/jasperreports-3.7.4.jar" + System.getProperty("path.separator") + context.getRealPath("/WEB-INF/"));
                        
                       JasperReport jreport1 = JasperCompileManager.compileReport(jasperDesignReport1);
                       JasperPrint jprint1 = JasperFillManager.fillReport(jreport1,parametros,ds);
                       
                       
                       List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();
                       
                       jasperPrints.add(jprint1);  
                       System.out.println("Exito......");
                
                
                       return jasperPrints;
                       
                        
                       // byte[] bytes = JasperRunManager.runReportToPdf(context.getRealPath("/reportes/reporteMarcaTipo.jasper"),parametros, ds);
                       
                       
                       
                        
                        
                       //return jprint;
                       /* FileManage fileManage = new FileManage();
                        fileManage.createFileFromByteArray(bytes, "C:/SystemaDeArchivos/" , "marcas"+".pdf");*/
                              
                    }catch(Exception e){e.printStackTrace();}            
        
        
        return null;
        
    }

    
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
     String[] sheetNames ={"Cobros de contado","Ventas a crédito","Abonos","Cheques","Depósitos","Cuenta de efectivo","Gastos","Devoluciones y Cambios (Recibidos)","Devoluciones y Cambios (Entregados)"};
 
     exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, jasperPrints);
     exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, outSt);
     exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
     exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
     exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
     exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
     exporterXLS.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
     exporterXLS.setParameter(JRXlsExporterParameter.SHEET_NAMES,sheetNames);
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
