/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.report.xls;

import com.tsp.sct.util.DateManage;
import com.tsp.sgfens.report.ReportBO;
import com.tsp.sgfens.report.ReportExportable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import jxl.*;
import jxl.write.*;
import jxl.write.Number;
import jxl.format.Colour;
import jxl.format.Alignment;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 07/12/2015
 */
public class ReportExportableXLSProspeccionTelcel extends ReportExportable {
    
    private Connection conn = null;
    
    private String nombreVendedor = "";
    private String region = "";
    private String plaza = "";
    private Date dateMin = null;
    private Date dateMax =  null;

    public ReportExportableXLSProspeccionTelcel(Connection conn) {
        this.conn = conn;
    }
    
    /**
     * Devuelve el ByteArrayOutputStream generado acorde al tipo de reporte y parámetros recibidos
     * 
     * @return ByteArrayOutputStream  Contenido de archivo XLS (MS Excel)
     */
    public ByteArrayOutputStream generarReporte() throws IOException, WriteException, Exception{

        ReportBO repBO = new ReportBO(this.conn);
        repBO.setUsuarioBO(this.user);

        ArrayList<HashMap> dataList = null;
        ArrayList<HashMap> fieldList = null;
        ArrayList<BigDecimal> totalList = new ArrayList<BigDecimal>();
        
        if(this.dataList!=null)
            dataList = this.dataList;
        
        if(this.fieldList!=null)
            fieldList = this.fieldList;

        ByteArrayOutputStream bos= new ByteArrayOutputStream();

        if(dataList.size() > 0){

            WritableWorkbook libro = Workbook.createWorkbook(bos);
            WritableFont fontArial16 = new WritableFont(WritableFont.ARIAL, 16, WritableFont.NO_BOLD, false);
            fontArial16.setColour(Colour.BLUE);
            WritableFont fontArial16_bold = new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD, false);
            fontArial16_bold.setColour(Colour.BLUE);
            
            WritableFont fontArial10 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false);
            fontArial10.setColour(Colour.BLACK);
            WritableFont fontArial10_bold = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false);
            fontArial10_bold.setColour(Colour.BLACK);
            
            WritableFont fontArial8 = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD, false);
            fontArial8.setColour(Colour.BLACK);
            WritableFont fontArial8_bold = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD, false);
            fontArial8_bold.setColour(Colour.BLACK);
            
            WritableFont fontArial6 = new WritableFont(WritableFont.ARIAL, 6, WritableFont.NO_BOLD, false);
            fontArial6.setColour(Colour.BLACK);
            WritableFont fontArial6_bold = new WritableFont(WritableFont.ARIAL, 6, WritableFont.BOLD, false);
            fontArial6_bold.setColour(Colour.BLACK);
            
            WritableCellFormat formatArial16_blue_bold = new WritableCellFormat(fontArial16_bold);
            //format1.setBackground(Colour.GREY_80_PERCENT);
            formatArial16_blue_bold.setAlignment(Alignment.CENTRE);
            
            WritableCellFormat formatArial10_bold = new WritableCellFormat(fontArial10_bold);
            formatArial10_bold.setAlignment(Alignment.LEFT);
            
            WritableCellFormat formatArial10 = new WritableCellFormat(fontArial10);
            formatArial10.setAlignment(Alignment.LEFT);
            formatArial10.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            
            WritableCellFormat formatArial8_bold = new WritableCellFormat(fontArial8_bold);
            formatArial8_bold.setAlignment(Alignment.LEFT);
            
            WritableCellFormat formatArial8 = new WritableCellFormat(fontArial8);
            formatArial8.setAlignment(Alignment.LEFT);
            
            WritableCellFormat formatArial6_bold = new WritableCellFormat(fontArial6_bold);
            formatArial6_bold.setAlignment(Alignment.CENTRE);
            
            WritableCellFormat formatArial6_bold_border = new WritableCellFormat(fontArial6_bold);
            formatArial6_bold_border.setAlignment(Alignment.CENTRE);
            formatArial6_bold_border.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            
            WritableCellFormat formatBordeArriba = new WritableCellFormat(fontArial8);
            formatBordeArriba.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.THIN);
            
            WritableCellFormat formatBordeAbajo = new WritableCellFormat(fontArial8);
            formatBordeAbajo.setBorder(jxl.format.Border.BOTTOM, jxl.format.BorderLineStyle.THIN);
            
            WritableCellFormat formatLogo = new WritableCellFormat(fontArial10);
            WritableSheet hojaCatalogo = libro.createSheet("Reporte", 0);
            //CellView view = new CellView();
            //view.setAutosize(true);
            
            /* CONTENIDO */
            int row = 0;
            
            //Titulo
            hojaCatalogo.mergeCells(0, 0, 8, 0); //col1, row1, col2, row2
             
            {
                int col = 0;
                
                Label lblAux = new Label(col, row, "REPORTE DIARIO DE PROSPECCION", formatArial16_blue_bold);
                hojaCatalogo.addCell(lblAux);
                row++;
                
                lblAux = new Label(col, row, "Radiomóvil Dipsa, S.A. de C.V.", formatArial10_bold);
                hojaCatalogo.addCell(lblAux);
                
                //Logo aquí
                CellView vistaLogo = new CellView();
                vistaLogo.setSize(1);
                jxl.write.WritableImage imageLogo = new WritableImage(8, 1, 1, 1, this.fileImageLogo );
                hojaCatalogo.addImage(imageLogo);
                row++;
                
                col = 0;
                lblAux = new Label(col, row, "Dirección de Unidades Regionales", formatArial10_bold);
                hojaCatalogo.addCell(lblAux);
                row++;
                
                lblAux = new Label(col, row, "Gerencia de Ventas Corporativas R 1-8", formatArial10_bold);
                hojaCatalogo.addCell(lblAux);
                row++;
                row++;
            }
            
            {
                int col = 0;
                Label lblAux = new Label(col, row, "REGION       : " + region, formatArial8_bold);
                hojaCatalogo.addCell(lblAux);
                row++;
                
                lblAux = new Label(col, row, "PLAZA        : " + plaza, formatArial8_bold);
                hojaCatalogo.addCell(lblAux);
                row++;
                
                lblAux = new Label(col, row, "VENDEDOR  : " + nombreVendedor, formatArial8_bold);
                hojaCatalogo.addCell(lblAux);
                
                if (dateMin!=null || dateMax!=null){
                    col=5;
                    String fMin = DateManage.formatDate(dateMin, "dd");
                    String fMax = DateManage.formatDate(dateMax, "dd 'DE' MMMM 'DEL' yyyy");
                    
                    String rango = "              SEMANA DEL ";
                    if (fMin!=null){
                        rango += fMin + " ";
                    }else{
                        rango += "___ ";
                    }
                    
                    if (fMax!=null){
                        rango += "AL " + fMax;
                    }else{
                        rango += "AL___  DE __________ DEL 200__";
                    }
                    
                    lblAux = new Label(col, row, rango.toUpperCase(), formatArial8_bold);
                    hojaCatalogo.addCell(lblAux);
                }
                row++;
                
                col=0;
                lblAux = new Label(col, row, "FECHA         : " + DateManage.dateTimeToStringEspanol(new Date()), formatArial8_bold);
                hojaCatalogo.addCell(lblAux);
                row++;
                
                
                //simulamos borde alrededor de sección
                hojaCatalogo.mergeCells(0, 4, 8, 4); //col1, row1, col2, row2
                hojaCatalogo.mergeCells(0, 9, 8, 9); //col1, row1, col2, row2
                lblAux = new Label(0,4," ", formatBordeAbajo);
                hojaCatalogo.addCell(lblAux);
                lblAux = new Label(0,9," ", formatBordeArriba);
                hojaCatalogo.addCell(lblAux);
            }
            
            /*
             * CABECERA
             *
             * Por cada dato en la cabecera, se pinta una celda,
             *
             */
            row = 10;
            for(int col = 0; col < fieldList.size(); col ++){
                
                //Label header = new Label(i, 0, (String)fieldList.get(i).get("label"), formatCabecera);
                Label header = new Label(col, row, (String)fieldList.get(col).get("label"), formatArial6_bold_border);
                hojaCatalogo.addCell(header);
                //hojaCatalogo.setColumnView(col, view);
                
            }
            row++;

            /*
             * CUERPO
             *
             * Para cada grupo de datos obtenido
             * se va pintando el dato correspondiente a la cabecera
             */
            for(int i = 0; i < dataList.size(); i ++){
                
                for(int col = 0; col < fieldList.size(); col ++){
                     String valor = (String)dataList.get(i).get((String)fieldList.get(col).get("field"));
                     
                     Label contentTxt = new Label(col, row, valor, formatArial10);
                     hojaCatalogo.addCell(contentTxt);
                     //hojaCatalogo.setColumnView(col, view);
                    
                }
                
                row++;
            }
            
            //Totales
            {
                int col = 0;
                Label lblTotales = new Label(col, row, "TOTALES", formatArial10_bold);
                hojaCatalogo.addCell(lblTotales);
                //hojaCatalogo.setColumnView(col, view);
                
                col = 6;
                Label lblTotalesLlamadas = new Label(col, row, "0", formatArial10);
                hojaCatalogo.addCell(lblTotalesLlamadas);
                
                col = 7;
                Label lblTotalesCorreos = new Label(col, row, "0", formatArial10);
                hojaCatalogo.addCell(lblTotalesCorreos);
                
                col = 8;
                Number lblTotalesVisitas = new Number(col, row, dataList.size(), formatArial10);
                hojaCatalogo.addCell(lblTotalesVisitas);

                row++;
            }
            
            {
                row = row + 2;
                int col = 0;
                
                Label lblAux1 = new Label(col, row, "CLASIFICACION :", formatArial6_bold);
                hojaCatalogo.addCell(lblAux1);
                col++;
                
                Label lblAux2 = new Label(col, row, " CT = CLIENTE CORPORATIVO TELCEL", formatArial8);
                hojaCatalogo.addCell(lblAux2);
                col++;
                
                Label lblAux3 = new Label(col, row, "P = PROSPECTO", formatArial8);
                hojaCatalogo.addCell(lblAux3);
                col++; col++;
                
                Label lblAux4 = new Label(col, row, "   GENERALES : NOMBRE DEL CONTACTO", formatArial8);
                hojaCatalogo.addCell(lblAux4);
                
            }
            
            {
                row++;
                int col = 1;
                
                Label lblAux2 = new Label(col, row, " CA = CLIENTE ACTUAL TELCEL", formatArial8);
                hojaCatalogo.addCell(lblAux2);
                col++;
                
                Label lblAux3 = new Label(col, row, "EC = EX-CLIENTE", formatArial8);
                hojaCatalogo.addCell(lblAux3);
                col++; col++;
                
                Label lblAux4 = new Label(col, row, "                            TELEFONO", formatArial8);
                hojaCatalogo.addCell(lblAux4);
            }
            
            {
                row++;
                int col = 1;
                
                Label lblAux2 = new Label(col, row, " CC = CLIENTE DE LA COMPETENCIA", formatArial8);
                hojaCatalogo.addCell(lblAux2);
                col = 4;
                
                Label lblAux4 = new Label(col, row, "                            PUESTO", formatArial8);
                hojaCatalogo.addCell(lblAux4);
            }
            
            {
                row++;
                row++;
                int col = 0;
                
                Label lblAux2 = new Label(col, row, "'Nota: Marque con una \"x\" si la actividad es llamada, correo o visita de prospección.", formatArial8);
                hojaCatalogo.addCell(lblAux2);
                col = 8;
                
                Label lblAux4 = new Label(col, row, "F-00.09.03.01.04-001", formatArial8);
                hojaCatalogo.addCell(lblAux4);
            }
            
            libro.write();
            libro.close();
        }

        return bos;

    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPlaza() {
        return plaza;
    }

    public void setPlaza(String plaza) {
        this.plaza = plaza;
    }

    public Date getDateMin() {
        return dateMin;
    }

    public void setDateMin(Date dateMin) {
        this.dateMin = dateMin;
    }

    public Date getDateMax() {
        return dateMax;
    }

    public void setDateMax(Date dateMax) {
        this.dateMax = dateMax;
    }
    
}
