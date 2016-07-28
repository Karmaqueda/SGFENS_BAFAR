/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.pdf;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tsp.sct.pdf.cfd.util.CfdTransform;
import com.tsp.sct.pdf.cfd.util.CfdiToPdfEventExt;
import java.io.ByteArrayOutputStream;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.util.Iterator;
import mx.bigdata.sat.cfdi.CFDv3;
import mx.bigdata.sat.cfdi.schema.Comprobante;
import mx.bigdata.sat.cfdi.schema.TimbreFiscalDigital;

/**
 *
 * @author ISCesarMartinez
 */
public class CFDItoPDF {
    
    Comprobante comprobanteFiscal = null;
    CFDv3 cfd = null;
    TimbreFiscalDigital timbreFiscalDigital =null; 
    
    private X509Certificate certificadoPAC = null;
    
    /**
     * Método para crear un archivo PDF a partir de un nuevo comprobante CFDI
     * @param comprobanteCFDI El Objeto Comprobante CFDI a transformar
     * @return ByteArrayOutputStream con el contenido del PDF
     * @throws Exception
     */
    public ByteArrayOutputStream toPdf() throws Exception{

        PdfITextUtil obj = new PdfITextUtil();
        CfdTransform transform = new CfdTransform();

        com.lowagie.text.Document document = new com.lowagie.text.Document(PageSize.LETTER);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Font LETRAOCHO = new Font(Font.HELVETICA,9,Font.NORMAL);
        Font LETRAOCHO_BOLD = new Font(Font.HELVETICA,9,Font.BOLD);
        Font LETRASIETE = new Font(Font.HELVETICA,7,Font.NORMAL);
        Font LETRASIETE_BOLD = new Font(Font.HELVETICA,7,Font.BOLD);

        String msgError = "";

        try{
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            CfdiToPdfEventExt event = new CfdiToPdfEventExt(document, comprobanteFiscal, LETRAOCHO, LETRAOCHO_BOLD,timbreFiscalDigital);
            
            event.setCertificadoPAC(this.certificadoPAC);
            
            writer.setPageEvent(event);
            document.setMargins(10, 10, 150, 150);
            document.open();

            PdfPTable tabla = new PdfPTable(1);
            tabla.setTotalWidth(550);
            tabla.setLockedWidth(true);

            PdfPTable tAux = new PdfPTable(1);

                    //SALTO DE LÍNEA
                    obj.agregaCelda(tabla,LETRAOCHO, " ", new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);

                    /*
                     * CUERPO
                     */
                    PdfPTable tCliente = new PdfPTable(2);
                    tCliente.setTotalWidth(550);
                    tCliente.setLockedWidth(true);

                        obj.agregaCelda(tCliente,LETRAOCHO_BOLD, "Cliente", new boolean[]{false,false,false,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],2);

                        tAux = new PdfPTable(1);
                        tAux.setTotalWidth(275);
                        tAux.setLockedWidth(true);

                            obj.agregaCelda(tAux,LETRAOCHO, "Nombre o razón social: " + (comprobanteFiscal.getReceptor().getNombre()!=null?comprobanteFiscal.getReceptor().getNombre():""), new boolean[]{false,true,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                            obj.agregaCelda(tAux,LETRAOCHO, "R.F.C. del cliente: " + comprobanteFiscal.getReceptor().getRfc(), new boolean[]{false,true,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);

                        obj.agregaTabla(tCliente, tAux, new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);

                        obj.agregaCelda(tCliente,LETRAOCHO, ""+
                                "DOMICILIO: Calle: " + (comprobanteFiscal.getReceptor().getDomicilio().getCalle()!=null?comprobanteFiscal.getReceptor().getDomicilio().getCalle():"") + " " +
                                (comprobanteFiscal.getReceptor().getDomicilio().getNoExterior()!=null?comprobanteFiscal.getReceptor().getDomicilio().getNoExterior():"") + " " + (comprobanteFiscal.getReceptor().getDomicilio().getNoInterior()!=null?comprobanteFiscal.getReceptor().getDomicilio().getNoInterior():"") +
                                " Col: " + (comprobanteFiscal.getReceptor().getDomicilio().getColonia()!=null?comprobanteFiscal.getReceptor().getDomicilio().getColonia():"") +
                                " \nDeleg./Municipio: " + (comprobanteFiscal.getReceptor().getDomicilio().getMunicipio()!=null?comprobanteFiscal.getReceptor().getDomicilio().getMunicipio():"") + " Entidad: " + (comprobanteFiscal.getReceptor().getDomicilio().getEstado()!=null?comprobanteFiscal.getReceptor().getDomicilio().getEstado():"") +
                                " C.P. " + comprobanteFiscal.getReceptor().getDomicilio().getCodigoPostal(), new boolean[]{false,true,false,true}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);

                    obj.agregaTabla(tabla, tCliente, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);

                    //SALTO DE LÍNEA
                    obj.agregaCelda(tabla,LETRAOCHO, " ", new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);

                    PdfPTable tConceptos = new PdfPTable(6);
                    tConceptos.setTotalWidth(550);
                    tConceptos.setLockedWidth(true);

                        obj.agregaCelda(tConceptos,LETRASIETE_BOLD, "IDENTIFICADOR", new boolean[]{false,true,false,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tConceptos,LETRASIETE_BOLD, "DESCRIPCIÓN", new boolean[]{false,true,false,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tConceptos,LETRASIETE_BOLD, "CANTIDAD", new boolean[]{false,true,false,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tConceptos,LETRASIETE_BOLD, "UNIDAD", new boolean[]{false,true,false,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tConceptos,LETRASIETE_BOLD, "PRECIO UNITARIO", new boolean[]{false,true,false,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tConceptos,LETRASIETE_BOLD, "IMPORTE", new boolean[]{false,false,false,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);

                        Iterator<mx.bigdata.sat.cfdi.schema.Comprobante.Conceptos.Concepto> cfdiConceptoItera = comprobanteFiscal.getConceptos().getConcepto().iterator();

                        while(cfdiConceptoItera.hasNext()){

                            mx.bigdata.sat.cfdi.schema.Comprobante.Conceptos.Concepto conceptoCFDI = cfdiConceptoItera.next();

                            obj.agregaCelda(tConceptos,LETRAOCHO, "" + (conceptoCFDI.getNoIdentificacion()!=null?transform.formatoPdfSAT(conceptoCFDI.getNoIdentificacion()):"") + " ", new boolean[]{false,true,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                            obj.agregaCelda(tConceptos,LETRAOCHO, "" + transform.formatoPdfSAT(conceptoCFDI.getDescripcion()) + " ", new boolean[]{false,true,false,false}, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 0, new int[0],1);
                            obj.agregaCelda(tConceptos,LETRAOCHO, "" + conceptoCFDI.getCantidad() + " ", new boolean[]{false,true,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                            obj.agregaCelda(tConceptos,LETRAOCHO, "" + (conceptoCFDI.getUnidad()!=null?conceptoCFDI.getUnidad():"") + " ", new boolean[]{false,true,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                            obj.agregaCelda(tConceptos,LETRAOCHO, "" + new DecimalFormat("$###,###,###,##0.00").format(conceptoCFDI.getValorUnitario().doubleValue()) + " ", new boolean[]{false,true,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, 0, new int[0],1);
                            obj.agregaCelda(tConceptos,LETRAOCHO, "" + new DecimalFormat("$###,###,###,##0.00").format(conceptoCFDI.getImporte().doubleValue()) + " ", new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, 0, new int[0],1);

                        }

                        //obj.agregaCelda(tConceptos,LETRAOCHO, " ", new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, 10, new int[0],6);

                    obj.agregaTabla(tabla, tConceptos, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);

                    //SALTO DE LÍNEA
                    obj.agregaCelda(tabla,LETRAOCHO, " ", new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);

                    PdfPTable tTotal = new PdfPTable(2);
                    tTotal.setTotalWidth(550);
                    tTotal.setWidths(new int[]{400,150});
                    tTotal.setLockedWidth(true);

                        obj.agregaCelda(tTotal,LETRAOCHO_BOLD, "TOTAL CON LETRA", new boolean[]{false,true,false,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tTotal,LETRAOCHO_BOLD, "TOTAL", new boolean[]{true,false,false,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);

                        Translator numALetra = new Translator();
                        String totalConLetra = "";
                        try{
                            totalConLetra = numALetra.getStringOfNumber(comprobanteFiscal.getTotal().floatValue());
                        }catch(Exception e){}

                        obj.agregaCelda(tTotal,LETRAOCHO, "" + totalConLetra, new boolean[]{false,true,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                        tAux = new PdfPTable(2);
                        tAux.setTotalWidth(150);

                            obj.agregaCelda(tAux,LETRASIETE_BOLD, "SUBTOTAL" , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                            obj.agregaCelda(tAux,LETRAOCHO, "" + new DecimalFormat("$###,###,###,##0.00").format(comprobanteFiscal.getSubTotal().doubleValue()), new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 0, new int[0],1);

                            obj.agregaCelda(tAux,LETRASIETE_BOLD, "DESCUENTO" , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                            obj.agregaCelda(tAux,LETRAOCHO, "" + new DecimalFormat("$###,###,###,##0.00").format(comprobanteFiscal.getDescuento()!=null?comprobanteFiscal.getDescuento().doubleValue():new Double(0)) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 0, new int[0],1);

                            /*
                             * Buscamos los impuestos trasladados IVA y IEPS de la factura
                             */
                            if(comprobanteFiscal.getImpuestos().getTraslados()!=null){
                                Iterator<mx.bigdata.sat.cfdi.schema.Comprobante.Impuestos.Traslados.Traslado> iteraTraCFDI = comprobanteFiscal.getImpuestos().getTraslados().getTraslado().iterator();
                                while(iteraTraCFDI.hasNext()){
                                    mx.bigdata.sat.cfdi.schema.Comprobante.Impuestos.Traslados.Traslado trasladoCDFI = iteraTraCFDI.next();
                                    obj.agregaCelda(tAux,LETRASIETE_BOLD, trasladoCDFI.getImpuesto() , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                                    obj.agregaCelda(tAux,LETRAOCHO, "" + new DecimalFormat("$###,###,###,##0.00").format(trasladoCDFI.getImporte().doubleValue()) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 0, new int[0],1);
                                }
                            }

                            /*
                             * Buscamos las retenciones de la factura
                             */
                            if(comprobanteFiscal.getImpuestos().getRetenciones()!=null){
                                Iterator<mx.bigdata.sat.cfdi.schema.Comprobante.Impuestos.Retenciones.Retencion> iteraRetCFDI = comprobanteFiscal.getImpuestos().getRetenciones().getRetencion().iterator();
                                while(iteraRetCFDI.hasNext()){
                                    mx.bigdata.sat.cfdi.schema.Comprobante.Impuestos.Retenciones.Retencion retencionCDFI = iteraRetCFDI.next();
                                    obj.agregaCelda(tAux,LETRASIETE_BOLD, retencionCDFI.getImpuesto() , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                                    obj.agregaCelda(tAux,LETRAOCHO, "" + new DecimalFormat("$###,###,###,##0.00").format(retencionCDFI.getImporte().doubleValue()) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 0, new int[0],1);
                                }
                            }

                            obj.agregaCelda(tAux,LETRAOCHO_BOLD, "TOTAL" , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM, 50, new int[0],1);
                            obj.agregaCelda(tAux,LETRAOCHO_BOLD, "" + new DecimalFormat("$###,###,###,##0.00").format(comprobanteFiscal.getTotal()) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_BOTTOM, 50, new int[0],1);

                        obj.agregaTabla(tTotal, tAux, new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);

                    obj.agregaTabla(tabla, tTotal, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);

                    //SALTO DE LÍNEA
                    obj.agregaCelda(tabla,LETRAOCHO, " ", new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);

                    if (comprobanteFiscal.getImpuestos().getTotalImpuestosRetenidos()!=null){
                        if (comprobanteFiscal.getImpuestos().getTotalImpuestosRetenidos().doubleValue()>0) {
                            /*
                             * Ponemos la leyenda personalizada que se grabó como observaciones
                             */
                            obj.agregaCelda(tabla,LETRAOCHO, "IMPUESTO RETENIDO DE CONFORMIDAD DE ACUERDO A LA LEY DEL IMPUESTO AL VALOR AGREGADO", new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                            //SALTO DE LÍNEA
                            obj.agregaCelda(tabla,LETRAOCHO, " ", new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        }
                    }

                    PdfPTable tSelloDigital = new PdfPTable(1);
                    tSelloDigital.setTotalWidth(550);
                    tSelloDigital.setLockedWidth(true);

                        obj.agregaCelda(tSelloDigital,LETRAOCHO_BOLD, "Sello Digital del emisor:", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                        obj.agregaCelda(tSelloDigital,LETRAOCHO, "" + this.timbreFiscalDigital.getSelloCFD() + " ", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);

                    obj.agregaTabla(tabla, tSelloDigital, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);

                    PdfPTable tSelloDigitalPSGECFD = new PdfPTable(1);
                    tSelloDigitalPSGECFD.setTotalWidth(550);
                    tSelloDigitalPSGECFD.setLockedWidth(true);

                        obj.agregaCelda(tSelloDigitalPSGECFD,LETRAOCHO_BOLD, "Sello del SAT:", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                        obj.agregaCelda(tSelloDigitalPSGECFD,LETRAOCHO, "" + this.timbreFiscalDigital.getSelloSAT() + " ", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);

                    obj.agregaTabla(tabla, tSelloDigitalPSGECFD, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);

                    /*
                     * Código de barras y cadena original
                     */
                    //SALTO DE LÍNEA
                    obj.agregaCelda(tabla,LETRAOCHO, " ", new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);

                    PdfPTable tCodigoDeBarrasYCadena = new PdfPTable(2);
                    tCodigoDeBarrasYCadena.setTotalWidth(550);
                    tCodigoDeBarrasYCadena.setWidths(new int[]{100,450});
                    tCodigoDeBarrasYCadena.setLockedWidth(true);

                    /***
                     * Obtenemos el Codigo de Barras Bidimensional
                     */
                    com.lowagie.text.Image imgCodigoDeBarras = getCBBiTextImage();

                    obj.agregaCeldaImagen(tCodigoDeBarrasYCadena, imgCodigoDeBarras, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP,150,new int[0], 1);

                    PdfPTable tCadenaOriginal = new PdfPTable(1);
                    tCadenaOriginal.setTotalWidth(450);

                        obj.agregaCelda(tCadenaOriginal,LETRAOCHO_BOLD, "Cadena original del complemento de certificación digital del SAT:", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                        obj.agregaCelda(tCadenaOriginal,LETRAOCHO, "" + this.cfd.getCadenaOriginal() + " ", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                    obj.agregaTabla(tCodigoDeBarrasYCadena, tCadenaOriginal, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);


                    obj.agregaTabla(tabla, tCodigoDeBarrasYCadena, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);

            document.add(tabla);
            tabla.flushContent();

        }catch(Exception ex){
            msgError = "No se ha podido generar el Comprobante Fiscal Digital en formato PDF:<br/>" + ex.getMessage();
        }finally{
            if(document.isOpen())
                document.close();
        }

        if (!msgError.equals("")) {
            throw new Exception(msgError);
        }

        return baos;
    }

    /**
     * Método para obtener el Código de Barras Bidimensional en formato iText Image
     * @return iText Image con el Código de Barras Bidimensional (CBB)
     */
    public com.lowagie.text.Image getCBBiTextImage() throws WriterException{
        com.lowagie.text.Image iTextImageCBB = null;
        try {
            iTextImageCBB = com.lowagie.text.Image.getInstance(getCBBBufferedImage(), null);
        } catch (Exception e){
        }
        return iTextImageCBB;
    }

    /**
     * Genera y retorna un buffer de Imagen con el Codigo de Barras Bidimensional
     * @return java.awt.image.BufferedImage con el CBB
     * @throws WriterException
     */
    public java.awt.image.BufferedImage getCBBBufferedImage() throws WriterException{
        java.awt.image.BufferedImage imageBufferedCBB = null;

        String stringCBB = "";

        /**
         * RFC del Emisor, a 12/13 posiciones, precedido por el texto ”?re=”
         */
        stringCBB += "?re=" + this.comprobanteFiscal.getEmisor().getRfc();
        /**
         * RFC del Receptor, a 12/13 posiciones, precedido por el texto “&rr=”
         */
        stringCBB += "&rr=" + this.comprobanteFiscal.getReceptor().getRfc();
        /**
         * Total del comprobante a 17 posiciones (10 para los enteros,
         * 1 para carácter “.”, 6 para los decimales), precedido por el texto “&tt=”
         */
        stringCBB += "&tt=" + new DecimalFormat("0000000000.000000").format(this.comprobanteFiscal.getTotal().doubleValue());
        /**
         * UUID del comprobante, precedido por el texto “&id=”
         */
        stringCBB += "&id=" + this.timbreFiscalDigital.getUUID();
        

        QRCodeWriter qrWriter = new QRCodeWriter();

        imageBufferedCBB = MatrixToImageWriter.toBufferedImage(qrWriter.encode(stringCBB, BarcodeFormat.QR_CODE, 120, 120));

        return imageBufferedCBB;
    }

    /**
     * Método para obtener un Objeto awt.Image del Codigo de Barras Bidimensional
     * del Comprobante
     * @return java.awt.Image Imagen que contiene el Codigo de Barras Bidimensional
     */
    public java.awt.Image getCBBImage(){
        java.awt.Image imageCBB = null;

        java.awt.image.BufferedImage imageBufferedCBB = null;
        try {
            imageBufferedCBB = getCBBBufferedImage();
            imageCBB = imageBufferedCBB.getScaledInstance(-1, -1, java.awt.Image.SCALE_REPLICATE);
        } catch (WriterException ex) {
            System.out.println(ex);
        }
                
        return imageCBB;
    }

    public CFDv3 getCfd() {
        return cfd;
    }

    public void setCfd(CFDv3 cfd) {
        this.cfd = cfd;
    }

    public Comprobante getComprobanteFiscal() {
        return comprobanteFiscal;
    }

    public void setComprobanteFiscal(Comprobante comprobanteFiscal) {
        this.comprobanteFiscal = comprobanteFiscal;
    }

    public TimbreFiscalDigital getTimbreFiscalDigital() {
        return timbreFiscalDigital;
    }

    public void setTimbreFiscalDigital(TimbreFiscalDigital timbreFiscalDigital) {
        this.timbreFiscalDigital = timbreFiscalDigital;
    }
    
    public X509Certificate getCertificadoPAC() {
        return certificadoPAC;
    }

    public void setCertificadoPAC(X509Certificate certificadoPAC) {
        this.certificadoPAC = certificadoPAC;
    }
}
