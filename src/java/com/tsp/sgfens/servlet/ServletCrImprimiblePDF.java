/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.servlet;

import com.tsp.sct.bo.CrDocImprimibleBO;
import com.tsp.sct.bo.UsuarioBO;
import com.tsp.sct.dao.dto.CrDocImprimible;
import com.tsp.sct.util.DateManage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ISCesar
 */
public class ServletCrImprimiblePDF extends HttpServlet {

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
        ServletOutputStream os = response.getOutputStream();
        // setting some response headers
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        // setting the content type
        response.setContentType("application/pdf");
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int idCrDocImprimible =-1;
        try { idCrDocImprimible = Integer.parseInt( request.getParameter("idCrDocImprimible")); }catch(Exception ex){}

        int idCrFormularioEvento =-1;
        try { idCrFormularioEvento = Integer.parseInt( request.getParameter("idCrFormularioEvento")); }catch(Exception ex){}

        /**
         * Recuperamos usuario en sesion
         */
        HttpSession session = request.getSession(true);
        UsuarioBO user = (UsuarioBO)session.getAttribute("user");
        
        CrDocImprimibleBO crDocImprimibleBO = new CrDocImprimibleBO(idCrDocImprimible, user.getConn());        
        CrDocImprimible crDocImprimible = crDocImprimibleBO.getCrDocImprimible();

        /**
         * attachment - since we don't want to open
         * it in the browser, but with Adobe Acrobat, and set the
         * default file name to use.
         */
        response.setHeader("Content-disposition",
                  "attachment; filename=" +
                "Cr_" + crDocImprimible.getTipoImprimible()
                    + "_"+DateManage.getDateHourString()+".pdf" );
        
        try {
            baos = crDocImprimibleBO.compilaPDF(idCrFormularioEvento);
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

}
