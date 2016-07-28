/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.servlet;

import com.tsp.sct.bo.ClienteBO;
import com.tsp.sct.bo.ComprobanteFiscalBO;
import com.tsp.sct.bo.EmpresaBO;
import com.tsp.sct.bo.TipoComprobanteBO;
import com.tsp.sct.cfdi.Cfd32BO;
import com.tsp.sct.config.Configuration;
import com.tsp.sct.dao.dto.Cliente;
import com.tsp.sct.dao.dto.ComprobanteFiscal;
import com.tsp.sct.dao.dto.Empresa;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ISCesarMartinez
 */
public class ServletCfdi32Pdf extends HttpServlet {

    private Connection conn = null;
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, Exception {
        ServletOutputStream os = response.getOutputStream();
        // setting some response headers
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control",
            "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        // setting the content type
        response.setContentType("application/pdf");
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //Recuperamos el valor encriptado
        int idComprobanteFiscal =-1;
        try { idComprobanteFiscal = Integer.parseInt( request.getParameter("idComprobanteFiscal")); }catch(Exception ex){}
        //si se envía ruta de archivo XML en lugar de ID Comprobante Fiscal (CxP)
        String rutaNombreArchivo  = request.getParameter("ruta_archivo")!=null?java.net.URLDecoder.decode(request.getParameter("ruta_archivo"), "UTF-8"):"";
        
        ComprobanteFiscalBO comprobanteFiscalBO = null;
        ComprobanteFiscal comprobanteFiscalDto = null;
        
        
        String rutaArchivo="";
        try{
            if (idComprobanteFiscal>0){
                Configuration appConfig = new Configuration();

                comprobanteFiscalBO = new ComprobanteFiscalBO(idComprobanteFiscal,this.conn);
                comprobanteFiscalDto = comprobanteFiscalBO.getComprobanteFiscal();

                Empresa empresaDto = new EmpresaBO(comprobanteFiscalDto.getIdEmpresa(),this.conn).getEmpresa();
                Cliente clienteDto = new ClienteBO(comprobanteFiscalDto.getIdCliente(),this.conn).getCliente();

                String archivoXML = comprobanteFiscalDto.getArchivoCfd();

                rutaArchivo = appConfig.getApp_content_path() + empresaDto.getRfc() + "/cfd/emitidos/"
                        +TipoComprobanteBO.getTipoComprobanteNombreCarpeta(comprobanteFiscalDto.getIdTipoComprobante()) 
                        +"/" + clienteDto.getRfcCliente()
                        + "/" + archivoXML;
            }else if (!rutaNombreArchivo.equals("")){
                rutaArchivo = rutaNombreArchivo;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        File archivoXML = new File(rutaArchivo);

        /**
         * attachment - since we don't want to open
         * it in the browser, but with Adobe Acrobat, and set the
         * default file name to use.
         */
        response.setHeader("Content-disposition",
                  "attachment; filename=" +
                 // fileFacturaBO.getTspFile().getNameFile().replace(".xml", "").replace(".XML", "") 
                "Pretoriano_" + archivoXML.getName().replace(".xml", "").replace(".XML", "")
                + "_PreviewCFDI.pdf" );

        Cfd32BO cfd3BO = null;
        try {
            cfd3BO = new Cfd32BO(archivoXML);
            cfd3BO.setComprobanteFiscalDto(comprobanteFiscalDto);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        try {
            baos = cfd3BO.toPdf();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            // the contentlength
            response.setContentLength(baos.size());
            // write ByteArrayOutputStream to the ServletOutputStream
            baos.writeTo(os);
            os.flush();
            os.close();
            
        } finally {
            baos.close();
            //respOut.close();
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet para mostrar Preview CFDI 3.2";
    }// </editor-fold>
}
