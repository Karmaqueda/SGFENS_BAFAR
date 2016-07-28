package com.tsp.sgfens.report.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.tsp.sct.bo.EmpresaBO;
import com.tsp.sct.bo.UsuarioBO;
import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.util.DateManage;
import com.tsp.sct.util.StringManage;
import com.tsp.sgfens.report.ReportBO;
import java.io.File;
import java.sql.Connection;
import java.util.Date;
/**
 *
 * @author luis morales
 */
public class EventPDF extends PdfPageEventHelper{

    protected PdfTemplate total;
    protected PdfPTable tablaEncabezado = null;
    protected PdfPTable tablaPie = null;
    protected Document document = null;
    protected File fileImageLogo = null;
    
    private Connection conn = null;

    public EventPDF(Document document, UsuarioBO user, int report, File fileImageLogo){
        
        this.document = document;
        
        PDFUtilBO pdfBO = new PDFUtilBO();
        Font letraDiez = new Font(Font.HELVETICA,10,Font.NORMAL);
        Font letraOcho = new Font(Font.HELVETICA,8,Font.NORMAL);

        PdfPTable tEncabezado = new PdfPTable(4);
        tEncabezado.setTotalWidth(550);
        try{
            tEncabezado.setWidths(new int[]{150,240,10,150});
        }catch(Exception ex){}
        tEncabezado.setLockedWidth(true);

        
        /**
        * Logo
        */
        Image imgLogo = null;
        try{
            imgLogo = Image.getInstance(fileImageLogo.getPath());
            imgLogo.scaleToFit(70, 70);
        }catch(Exception e){
            System.out.println("No se pudo agregar el logo al reporte. " + e.getMessage());
        }
        
        PdfPCell cellImage = new PdfPCell(new Phrase(" "));
        if(imgLogo!=null)
            cellImage = new PdfPCell(imgLogo);
        
        pdfBO.addImageCell(tEncabezado, imgLogo, new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE,new int[0], 1);
        
        
        PdfPTable tAux = new PdfPTable(1);
        tAux.setTotalWidth(240);
        tAux.setLockedWidth(true);

         //PARA OBTENER LA SUCURSAL O MATRIZ
        Empresa empresa = new Empresa();
        if (user!=null){
            EmpresaBO empBO = new EmpresaBO(user.getUser().getIdEmpresa(), this.conn);
            empresa = empBO.getEmpresa();
        }
        
        pdfBO.addCell(tAux,letraDiez, null,StringManage.getValidString(empresa.getNombreComercial()), 
                new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);        
        pdfBO.addCell(tAux,letraDiez, null,this.getTitle(report), 
                new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
        pdfBO.addCell(tAux,letraOcho,null, (user!=null&&user.getDatosUsuario()!=null?"Consultado por: \n\t" + user.getDatosUsuario().getNombre() + " " + user.getDatosUsuario().getApellidoPat():""), 
                new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
        pdfBO.addCell(tAux, letraOcho, null, " ",
                            new boolean[]{false,false,false,false} , Element.ALIGN_CENTER, Element.ALIGN_TOP, 10, new int[]{1,1,1,1}, 1);
        pdfBO.addCell(tAux,letraOcho,null, "Fecha y Hora Consulta: \n\t\t"+ DateManage.dateTimeToStringEspanol(new Date()), 
                new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
        
        //Agregamos tabla auxiliar con datos de impresion
        pdfBO.addTable(tEncabezado, tAux, new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],2);
        
        //Añadimos una celda vacía para que sean 4 columnas y se imprima correctamente
        pdfBO.addCell(tEncabezado, letraOcho, null, " ",
                            new boolean[]{false,false,false,false} , Element.ALIGN_CENTER, Element.ALIGN_TOP, 10, new int[]{1,1,1,1}, 1);
        
        
        /*
        pdfBO.addCell(tEncabezado, letraDiez, null, this.getTitle(report),
                            new boolean[]{false,false,false,false} , Element.ALIGN_LEFT, Element.ALIGN_TOP, 15, new int[]{1,1,1,1}, 3);
//        pdfBO.addCell(tEncabezado, letraOcho, null, " ",
//                            new boolean[]{false,false,false,false} , Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 10, new int[]{1,1,1,1}, 4);
        
        pdfBO.addCell(tEncabezado, letraOcho, null, " ",
                            new boolean[]{false,false,false,false} , Element.ALIGN_LEFT, Element.ALIGN_TOP, 10, new int[]{1,1,1,1}, 1);
        pdfBO.addCell(tEncabezado, letraOcho, null, (user!=null&&user.getDatosUsuario()!=null?"Impreso por: " + user.getDatosUsuario().getNombre() + " " + user.getDatosUsuario().getApellidoPat():""),
                            new boolean[]{false,false,false,false} , Element.ALIGN_LEFT, Element.ALIGN_TOP, 10, new int[]{1,1,1,1}, 3);
        pdfBO.addCell(tEncabezado, letraOcho, null, " ",
                            new boolean[]{false,false,false,false} , Element.ALIGN_LEFT, Element.ALIGN_TOP, 10, new int[]{1,1,1,1}, 1);
        pdfBO.addCell(tEncabezado, letraOcho, null, "Fecha y Hora Impresión: "+ DateManage.dateTimeToStringEspanol(new Date()),
                            new boolean[]{false,false,false,false} , Element.ALIGN_LEFT, Element.ALIGN_TOP, 10, new int[]{1,1,1,1}, 3);
        */
        tablaEncabezado = tEncabezado;

        PdfPTable tPie = new PdfPTable(1);
        tPie.setTotalWidth(600);
        tPie.setLockedWidth(true);
        
        pdfBO.addCell(tPie, letraDiez, null, "Reporte",
                            new boolean[]{true,true,true,true} , Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{1,1,1,1}, 1);

        tablaPie = tPie;
    }

    private String getTitle(int REPORT){
        return ReportBO.getTitle(REPORT);
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document dcmnt) {
        total.beginText();
        try{
            total.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false), 9);
        }catch(Exception e){}
        total.setTextMatrix(0, 0);
        total.showText(String.valueOf(writer.getPageNumber() - 1));
        total.endText();
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        cb.saveState();
        String text = writer.getPageNumber() + " de ";
        float textBase = document.bottomMargin();//bottom();
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
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(100, 100);
        total.setBoundingBox(new Rectangle(-20, -20, 100, 100));
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        tablaEncabezado.writeSelectedRows(0, -1,document.left(), document.top() + 100, cb);
//        tablaPie.writeSelectedRows(0, -1, document.left()+50, 80, cb);
    }

    public File getFileImageLogo() {
        return fileImageLogo;
    }

    public void setFileImageLogo(File fileImageLogo) {
        this.fileImageLogo = fileImageLogo;
    }
    
    public EventPDF(Document document, UsuarioBO user, int report, File fileImageLogo, String infoTitle){
        
        this.document = document;
        
        PDFUtilBO pdfBO = new PDFUtilBO();
        Font letraDiez = new Font(Font.HELVETICA,10,Font.NORMAL);
        Font letraOcho = new Font(Font.HELVETICA,8,Font.NORMAL);
        Font letraSeis = new Font(Font.HELVETICA,6,Font.NORMAL);

        PdfPTable tEncabezado = new PdfPTable(4);
        tEncabezado.setTotalWidth(550);
        try{
            tEncabezado.setWidths(new int[]{150,240,10,150});
        }catch(Exception ex){}
        tEncabezado.setLockedWidth(true);

        
        /**
        * Logo
        */
        Image imgLogo = null;
        try{
            imgLogo = Image.getInstance(fileImageLogo.getPath());
            imgLogo.scaleToFit(70, 70);
        }catch(Exception e){
            System.out.println("No se pudo agregar el logo al reporte. " + e.getMessage());
        }
        
        PdfPCell cellImage = new PdfPCell(new Phrase(" "));
        if(imgLogo!=null)
            cellImage = new PdfPCell(imgLogo);
        
        pdfBO.addImageCell(tEncabezado, imgLogo, new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE,new int[0], 1);
        
        
        PdfPTable tAux = new PdfPTable(1);
        tAux.setTotalWidth(240);
        tAux.setLockedWidth(true);

         //PARA OBTENER LA SUCURSAL O MATRIZ
        Empresa empresa = new Empresa();
        if (user!=null){
            EmpresaBO empBO = new EmpresaBO(user.getUser().getIdEmpresa(), this.conn);
            empresa = empBO.getEmpresa();
        }
        
        pdfBO.addCell(tAux,letraDiez, null,StringManage.getValidString(empresa.getNombreComercial()), 
                new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);        
        pdfBO.addCell(tAux,letraDiez, null,this.getTitle(report), 
                new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
        pdfBO.addCell(tAux,letraOcho,null, (user!=null&&user.getDatosUsuario()!=null?"Consultado por: \n\t" + user.getDatosUsuario().getNombre() + " " + user.getDatosUsuario().getApellidoPat():""), 
                new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);        
        pdfBO.addCell(tAux, letraOcho, null, " ",
                            new boolean[]{false,false,false,false} , Element.ALIGN_CENTER, Element.ALIGN_TOP, 10, new int[]{1,1,1,1}, 1);
        pdfBO.addCell(tAux,letraOcho,null, "Fecha y Hora Consulta: \n\t\t"+ DateManage.dateTimeToStringEspanol(new Date()), 
                new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
        pdfBO.addCell(tAux,letraSeis,null,infoTitle, 
                new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
        
        //Agregamos tabla auxiliar con datos de impresion
        pdfBO.addTable(tEncabezado, tAux, new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],2);
        
        //Añadimos una celda vacía para que sean 4 columnas y se imprima correctamente
        pdfBO.addCell(tEncabezado, letraOcho, null, " ",
                            new boolean[]{false,false,false,false} , Element.ALIGN_CENTER, Element.ALIGN_TOP, 10, new int[]{1,1,1,1}, 1);
        
        
        /*
        pdfBO.addCell(tEncabezado, letraDiez, null, this.getTitle(report),
                            new boolean[]{false,false,false,false} , Element.ALIGN_LEFT, Element.ALIGN_TOP, 15, new int[]{1,1,1,1}, 3);
//        pdfBO.addCell(tEncabezado, letraOcho, null, " ",
//                            new boolean[]{false,false,false,false} , Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 10, new int[]{1,1,1,1}, 4);
        
        pdfBO.addCell(tEncabezado, letraOcho, null, " ",
                            new boolean[]{false,false,false,false} , Element.ALIGN_LEFT, Element.ALIGN_TOP, 10, new int[]{1,1,1,1}, 1);
        pdfBO.addCell(tEncabezado, letraOcho, null, (user!=null&&user.getDatosUsuario()!=null?"Impreso por: " + user.getDatosUsuario().getNombre() + " " + user.getDatosUsuario().getApellidoPat():""),
                            new boolean[]{false,false,false,false} , Element.ALIGN_LEFT, Element.ALIGN_TOP, 10, new int[]{1,1,1,1}, 3);
        pdfBO.addCell(tEncabezado, letraOcho, null, " ",
                            new boolean[]{false,false,false,false} , Element.ALIGN_LEFT, Element.ALIGN_TOP, 10, new int[]{1,1,1,1}, 1);
        pdfBO.addCell(tEncabezado, letraOcho, null, "Fecha y Hora Impresión: "+ DateManage.dateTimeToStringEspanol(new Date()),
                            new boolean[]{false,false,false,false} , Element.ALIGN_LEFT, Element.ALIGN_TOP, 10, new int[]{1,1,1,1}, 3);
        */
        tablaEncabezado = tEncabezado;

        PdfPTable tPie = new PdfPTable(1);
        tPie.setTotalWidth(600);
        tPie.setLockedWidth(true);
        
        pdfBO.addCell(tPie, letraDiez, null, "Reporte",
                            new boolean[]{true,true,true,true} , Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{1,1,1,1}, 1);

        tablaPie = tPie;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    
}
