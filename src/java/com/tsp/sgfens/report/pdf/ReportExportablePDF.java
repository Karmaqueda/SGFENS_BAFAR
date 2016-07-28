package com.tsp.sgfens.report.pdf;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tsp.sgfens.report.ReportBO;
import com.tsp.sgfens.report.ReportExportable;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 17-dic-2012 
 */
public class ReportExportablePDF extends ReportExportable {

    public ReportExportablePDF() {
    }

    public ReportExportablePDF(Connection conn) {
        this.conn = conn;
    }
    
    /**
     * Devuelve el ByteArrayOutputStream generado acorde al reporte y a los parámetros
     * 
     * @param int report
     * @param String params
     * 
     * @return ByteArrayOutputStream 
     */
    public ByteArrayOutputStream generarReporte(int report, String params, String parametrosExtra,String infoTitle) throws Exception{

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ReportBO repBO = new ReportBO(this.conn);
        repBO.setUsuarioBO(this.user);

        ArrayList<HashMap> dataList = null;
        ArrayList<HashMap> fieldList = null;
        ArrayList<BigDecimal> totalList = new ArrayList<BigDecimal>();
        
        if(this.dataList!=null)
            dataList = this.dataList;
        else
            dataList = repBO.getDataReport(report, params, parametrosExtra);
        
        if(this.fieldList!=null)
            fieldList = this.fieldList;
        else
            fieldList = repBO.getFieldList(report);

        if(dataList.size() > 0){

            com.lowagie.text.Document doc = new com.lowagie.text.Document(PageSize.LETTER);
            PDFUtilBO pdfBO = new PDFUtilBO();
            Font letraOcho = new Font(Font.HELVETICA,5,Font.NORMAL);
            Font letraNueveBold = new Font(Font.HELVETICA,6,Font.BOLD);
            Font letraBigBold = new Font(Font.HELVETICA,8,Font.BOLD);
            Font letraNueve = new Font(Font.HELVETICA,9,Font.NORMAL);
            
            try{

                PdfWriter writer = PdfWriter.getInstance(doc, baos);
                EventPDF eventPDF = new EventPDF(doc,this.user,report,fileImageLogo,infoTitle);
                eventPDF.setConn(this.conn);
                //eventPDF.setFileImageLogo(fileImageLogo);
                writer.setPageEvent(eventPDF);
                doc.setMargins(5, 5, 120, 10);
                doc.open();

                PdfPTable mainTable = new PdfPTable(fieldList.size());
                mainTable.setTotalWidth(600);
                mainTable.setLockedWidth(true);

                /* CONTENIDO */

                /*
                 * CABECERA
                 * 
                 * Por cada dato en la cabecera, se pinta una celda,
                 *
                 */
                for(int i = 0; i < fieldList.size(); i ++){
                    
                    pdfBO.agregaCelda(mainTable, letraNueve, Color.lightGray, (String)fieldList.get(i).get("label"),
                            new boolean[]{true,true,true,true} , Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 15, new int[]{1,1,1,1}, 1);
                    
                    totalList.add(BigDecimal.ZERO);
                }

                /*
                 * CUERPO
                 * 
                 * Para cada grupo de datos obtenido 
                 * se va pintando el dato correspondiente a la cabecera
                 */
                for(int i = 0; i < dataList.size(); i ++){

                    for(int j = 0; j < fieldList.size(); j ++){
                        //Pintamos celda
                        String valor = (String)dataList.get(i).get((String)fieldList.get(j).get("field"));
                        //pdfBO.addCell(mainTable, letraOcho, null, (String)dataList.get(i).get((String)fieldList.get(j).get("field")),
                        pdfBO.addCell(mainTable, letraNueve, null, valor,
                                new boolean[]{true,true,true,true} , Element.ALIGN_JUSTIFIED, Element.ALIGN_MIDDLE, 15, new int[]{5,5,5,5}, 1);
                        
                        //Si el tipo de campo es Integer o Decimal, lo sumamos a la lista de totales
                        int tipoCampo = 0;
                        try{ tipoCampo = Integer.parseInt(fieldList.get(j).get("type").toString()); }catch(Exception ex){}
                        if (tipoCampo==ReportBO.DATA_INT || tipoCampo==ReportBO.DATA_DECIMAL){
                            if(valor!=null){
                                BigDecimal actual = totalList.get(j).add(new BigDecimal(valor));
                                totalList.set(j, actual);
                            }
                        }
                        
                    }

                }
                
                if (repBO.isTotalIntegerFields()
                        || repBO.isTotalDecimalFields()){
                    /**
                     * TOTALES
                     * 
                     * Opcional, para campos Enteros y/o Decimales
                     */
                    int i = 0;
                    for (HashMap fieldList1 : fieldList) {
                        int tipoCampo = 0;
                        try{ tipoCampo = Integer.parseInt(fieldList1.get("type").toString()); }catch(Exception ex){}
                        
                        if (tipoCampo==ReportBO.DATA_INT && repBO.isTotalIntegerFields()){
                            pdfBO.agregaCelda(mainTable, letraBigBold, Color.lightGray, "" + totalList.get(i).intValue() , new boolean[]{true,true,true,true}, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, 15, new int[]{1,1,1,1}, 1);
                        }else if (tipoCampo==ReportBO.DATA_DECIMAL && repBO.isTotalDecimalFields()){
                            pdfBO.agregaCelda(mainTable, letraBigBold, Color.lightGray, totalList.get(i).setScale(2, RoundingMode.HALF_UP).toPlainString() , new boolean[]{true,true,true,true}, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, 15, new int[]{1,1,1,1}, 1);
                        }else{
                            pdfBO.agregaCelda(mainTable, letraNueveBold, Color.lightGray, "", new boolean[]{true,true,true,true}, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, 15, new int[]{1,1,1,1}, 1);
                        }
                        i++;
                    }
                }

                /* CONTENIDO */

                doc.add(mainTable);
                mainTable.flushContent();

            }catch(Exception ex){
                ex.printStackTrace();
                throw new Exception("No se ha podido generar el formato PDF: " + ex.toString());
            }finally{
                if(doc.isOpen())
                    doc.close();
            }

        }else{

            baos = null;
            
        }

        return baos;
    }

}
