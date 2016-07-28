/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.pdf.cfd.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.tsp.sct.bo.ImagenPersonalBO;
import com.tsp.sct.dao.dto.ComprobanteFiscal;
import com.tsp.sct.pdf.PdfITextUtil;
import java.awt.Color;
import java.io.File;
import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import mx.bigdata.sat.cfdi.schema.v32.TimbreFiscalDigital;

/**
 *
 * @author ISC César Ulises Martínez García
 */
public class Cfdi32ToPdfEventExt extends PdfPageEventHelper{
        protected PdfTemplate total;
        protected PdfPTable tablaEncabezado=null; /* Tabla que contiene la imagen del encabezado */
        protected PdfPTable tablaPie=null; /* Tabla que contiene la imagen del pie de pagina */
        protected Document document=null;

        //private X509Certificate certificadoPAC;
        
        private Connection conn = null;

        public Cfdi32ToPdfEventExt(com.lowagie.text.Document document, mx.bigdata.sat.cfdi.schema.v32.Comprobante comprobanteCFDI, Font font, Font fontBold,TimbreFiscalDigital timbreFiscalDigital, X509Certificate certificadoPAC, ComprobanteFiscal comprobanteFiscalDto)throws Exception{
            PdfITextUtil obj = new PdfITextUtil();

            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");

            this.document=document;
            //Image imgCedula = null;

            Font LETRASIETE = new Font(Font.HELVETICA,7,Font.NORMAL);
            Font LETRASIETE_BOLD = new Font(Font.HELVETICA,7,Font.BOLD);

             /*
             * ENCABEZADO
             */
            PdfPTable tEmisor = new PdfPTable(3);
            tEmisor.setTotalWidth(550);
            tEmisor.setWidths(new int[]{150,240,160});
            tEmisor.setLockedWidth(true);

            Image imgLogo = null;
            
            try{
                if (comprobanteFiscalDto!=null){
                    File archivoImagen = new ImagenPersonalBO(this.conn).getFileImagenPersonalByEmpresa(comprobanteFiscalDto.getIdEmpresa());
                    imgLogo = Image.getInstance(archivoImagen.getPath());
                    imgLogo.scaleToFit(100, 100);
                }
            }catch(Exception e){
                System.out.println("No se pudo agregar el logo al reporte. " + e.getMessage());
            }

                /*
                 * El logo es opcional
                 */

                PdfPTable tLogo = new PdfPTable(1);
                tLogo.setTotalWidth(150);
                tLogo.setLockedWidth(true);

                obj.agregaCeldaImagen(tLogo, imgLogo, new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE,100,new int[0], 1);
                obj.agregaTabla(tEmisor, tLogo, new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);

                PdfPTable tAux = new PdfPTable(1);
                tAux.setTotalWidth(240);
                tAux.setLockedWidth(true);

                    obj.agregaCelda(tAux,font, "Emisor:", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                    obj.agregaCelda(tAux,fontBold, "" + comprobanteCFDI.getEmisor().getNombre() + " ", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                    obj.agregaCelda(tAux,LETRASIETE, "" + (comprobanteCFDI.getEmisor().getDomicilioFiscal().getCalle()!=null?comprobanteCFDI.getEmisor().getDomicilioFiscal().getCalle():"") + " " + (comprobanteCFDI.getEmisor().getDomicilioFiscal().getNoExterior()!=null?comprobanteCFDI.getEmisor().getDomicilioFiscal().getNoExterior():"") + " " + (comprobanteCFDI.getEmisor().getDomicilioFiscal().getNoInterior()!=null?comprobanteCFDI.getEmisor().getDomicilioFiscal().getNoInterior():""), new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                    obj.agregaCelda(tAux,LETRASIETE, "" + (comprobanteCFDI.getEmisor().getDomicilioFiscal().getColonia()!=null?comprobanteCFDI.getEmisor().getDomicilioFiscal().getColonia():"") + " ", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                    obj.agregaCelda(tAux,LETRASIETE, "" + (comprobanteCFDI.getEmisor().getDomicilioFiscal().getMunicipio()!=null?comprobanteCFDI.getEmisor().getDomicilioFiscal().getMunicipio():"") + ", " + (comprobanteCFDI.getEmisor().getDomicilioFiscal().getEstado()!=null?comprobanteCFDI.getEmisor().getDomicilioFiscal().getEstado():"") + ", C.P. " + comprobanteCFDI.getEmisor().getDomicilioFiscal().getCodigoPostal(), new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                    obj.agregaCelda(tAux,fontBold, "" + comprobanteCFDI.getEmisor().getRfc() + " ", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);

                obj.agregaTabla(tEmisor, tAux, new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);


                    PdfPTable tFecha = new PdfPTable(1);
                    tFecha.setTotalWidth(160);
                    tFecha.setLockedWidth(true);

                        obj.agregaCelda(tFecha,font,Color.lightGray, "Folio fiscal (UUID)", new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tFecha,fontBold, "" + timbreFiscalDigital.getUUID(), new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        
                        obj.agregaCelda(tFecha,font
                                , " " + (comprobanteCFDI.getSerie()!=null?(!comprobanteCFDI.getSerie().equals("")?"Serie: "+comprobanteCFDI.getSerie():" "):" ") 
                                + " " + (comprobanteCFDI.getFolio()!=null?(!comprobanteCFDI.getFolio().equals("")?"Folio: "+comprobanteCFDI.getFolio():" "):" ")
                                , new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        
                        obj.agregaCelda(tFecha,font,Color.lightGray, "No. de Serie del Certificado del PAC", new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tFecha,fontBold, "" + timbreFiscalDigital.getNoCertificadoSAT(), new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tFecha,font,Color.lightGray, "Fecha y hora de certificación", new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tFecha,fontBold, "" + formatoFecha.format(timbreFiscalDigital.getFechaTimbrado()) + "T" + formatoHora.format(timbreFiscalDigital.getFechaTimbrado()), new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        //obj.agregaCelda(tFecha,font, "Lugar y fecha de emisión", new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tFecha,font,Color.lightGray, "Fecha de emisión", new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        if(comprobanteCFDI.getEmisor().getExpedidoEn()!=null)
                            obj.agregaCelda(tFecha,fontBold, "" + comprobanteCFDI.getEmisor().getExpedidoEn().getMunicipio() + ", " + comprobanteCFDI.getEmisor().getExpedidoEn().getEstado() + ", \n" + formatoFecha.format(timbreFiscalDigital.getFechaTimbrado()) + "T" + formatoHora.format(timbreFiscalDigital.getFechaTimbrado()), new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        else
                            obj.agregaCelda(tFecha, fontBold, " " + formatoFecha.format(timbreFiscalDigital.getFechaTimbrado()) + "T" + formatoHora.format(timbreFiscalDigital.getFechaTimbrado()), new boolean[]{false, false, false, false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0], 1);

                    obj.agregaTabla(tEmisor, tFecha, new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
            tablaEncabezado = tEmisor;

            /*
             * PIE DE PÁGINA
             */
            PdfPTable tPie = new PdfPTable(1);
            tPie.setTotalWidth(500);
            //tPie.setWidths(new int[]{100,450});
            tPie.setLockedWidth(true);

                PdfPTable tDatosFactura = new PdfPTable(1);
                tDatosFactura.setTotalWidth(500);
                tDatosFactura.setLockedWidth(true);

                    obj.agregaCelda(tDatosFactura,font, "Este documento es una representación impresa de un CFDI", new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                    obj.agregaCelda(tDatosFactura,font, "" + comprobanteCFDI.getFormaDePago(), new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                    obj.agregaCelda(tDatosFactura,fontBold,Color.lightGray, "No. de serie del certificado del emisor", new boolean[]{true,true,true,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                    obj.agregaCelda(tDatosFactura,font, comprobanteCFDI.getNoCertificado(), new boolean[]{true,true,false,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                    
                    
                    try{
                         //BigInteger bi = certificadoPAC.getSerialNumber();
                         //String  numeroCertificado = new String(bi.toByteArray());
                         String  numeroCertificado = timbreFiscalDigital.getNoCertificadoSAT();
                        obj.agregaCelda(tDatosFactura,fontBold, "Datos del PAC:", new boolean[]{true,true,true,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                        //.obj.agregaCelda(tDatosFactura,font, "Razón social: TSP, S.A. DE C.V.", new boolean[]{true,true,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                        obj.agregaCelda(tDatosFactura,font, "No. de certificado: " +  numeroCertificado, new boolean[]{true,true,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                        //obj.agregaCelda(tDatosFactura,font, "Fecha de autorización: 2011-XX-XXT18:00:00    No. de autorización: 70XXX", new boolean[]{true,true,false,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                    }catch(Exception ex){
                        System.out.println("No se pudo recuperar la información del certificado PAC para plasmarlo en el PDF.");
                    }
                     
                    //obj.agregaCelda(tDatosFactura,fontBold, "Datos del PAC:", new boolean[]{true,true,true,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                    //obj.agregaCelda(tDatosFactura,font, "Razón social: CERTUS APLICACIONES DIGITALES, S.A. DE C.V.", new boolean[]{true,true,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                    //obj.agregaCelda(tDatosFactura,font, "RFC: CAD100607RY8  No. de certificado: 00001000000101861159", new boolean[]{true,true,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                    //obj.agregaCelda(tDatosFactura,font, "Fecha de autorización: 2011-01-27T18:00:00    No. de autorización: 70476", new boolean[]{true,true,false,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);

                obj.agregaTabla(tPie, tDatosFactura, new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);

            tablaPie = tPie;

        }
        @Override
        public void onStartPage(PdfWriter writer, Document document){
            //SE IMPRIME EL ENCABEZADO
            PdfContentByte cb = writer.getDirectContent();
            tablaEncabezado.writeSelectedRows(0, -1,document.left(), document.top()+120, cb);

            tablaPie.writeSelectedRows(0, -1, document.left()+50, 80, cb);
        }
        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            total = writer.getDirectContent().createTemplate(100, 100);
            total.setBoundingBox(new Rectangle(-20, -20, 100, 100));
        }
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();
            String text = writer.getPageNumber() + " de ";
            float textBase = document.bottom() - 50;
            cb.beginText();
            try{
                cb.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false), 9);
            }catch(Exception e){}
            cb.setTextMatrix(document.right()-30, textBase);
            cb.showText(text);
            cb.endText();
            cb.addTemplate(total, document.right()-10, textBase);
            cb.restoreState();
        }
        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            total.beginText();
            try{
                total.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false), 9);
            }catch(Exception e){}
            total.setTextMatrix(0, 0);
            total.showText(String.valueOf(writer.getPageNumber() - 1));
            total.endText();
        }


}
