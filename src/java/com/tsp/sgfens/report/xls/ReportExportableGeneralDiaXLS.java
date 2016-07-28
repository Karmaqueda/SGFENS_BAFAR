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
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.*;
import jxl.write.*;
import jxl.format.Colour;
import jxl.format.Alignment;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 22/01/2015
 */
public class ReportExportableGeneralDiaXLS extends ReportExportable {
    
    private Connection conn = null;
    
    private Date fecha = null;
    private String zonaRuta = "";
    private String nombreEmpleado = "";
    private Date rangoFechaMin = null;
    private Date rangoFechaMax = null;
    
    private List<ReportGenericData> contadoData;
    private List<ReportGenericData> creditoData;
    private List<ReportGenericData> cobroData;
    private List<ReportGenericData> abonosData;
    private List<ReportGenericData> chequesData;
    private List<ReportGenericData> depositosData;
    private List<ReportGenericData> erroresData;
    private List<ReportGenericData> efectivoData;
    private List<ReportGenericData> faltantesData;
    private List<ReportGenericData> devolucionesData;
    private List<ReportGenericData> gastosData;
    
    //totales
    private double totalSub1234 = 0;
    private List<ReportGenericData> subtotalesVenta;
    private List<ReportGenericData> subtotalesCobranza;
    private List<ReportGenericData> totalesBalanza;

    public ReportExportableGeneralDiaXLS(Connection conn) {
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

        ByteArrayOutputStream bos= new ByteArrayOutputStream();

        //if(dataList.size() > 0){

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

        WritableCellFormat formatArial10_bold_border = new WritableCellFormat(fontArial10_bold);
        formatArial10_bold_border.setAlignment(Alignment.CENTRE);
        formatArial10_bold_border.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        formatArial10_bold_border.setBackground(Colour.GREY_50_PERCENT);

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

            Label lblAux = new Label(col, row, "Reporte General Día", formatArial16_blue_bold);
            hojaCatalogo.addCell(lblAux);
            row++;

        }

        {
            int col = 0;
            int rIni = row;
            Label lblAux = new Label(col, row, "FECHA: " + DateManage.dateTimeToStringEspanol(fecha), formatArial8_bold);
            hojaCatalogo.addCell(lblAux);
            row++;

            int rFin = row;


            //simulamos borde alrededor de sección
            /*
            hojaCatalogo.mergeCells(0, rIni, 8, rIni); //col1, row1, col2, row2
            hojaCatalogo.mergeCells(0, rFin, 8, rFin); //col1, row1, col2, row2
            lblAux = new Label(0,rIni," ", formatBordeAbajo);
            hojaCatalogo.addCell(lblAux);
            lblAux = new Label(0,rFin," ", formatBordeArriba);
            hojaCatalogo.addCell(lblAux);
            */
        }

        row++;

        int rowMax = row; //para ir llevando un identificador del sub reporte que tenga mas renglones
        //sub-reporte
        {
            int rowAux = row; //para no afectar a la variable row general
            int colAux = 0;//

            int colsSubReportContado = 5;
            int rowsReportContado = 120; // + 3 : cabecera titulo, cabecera sub titulos, totales
            rowAux = addSubReportContado(hojaCatalogo, rowAux, colAux, formatArial10_bold_border, formatArial10, colsSubReportContado, rowsReportContado);
            if (rowAux > rowMax) rowMax = rowAux;
            
            rowAux = row;
            int colsSubReportCredito = 6;
            //int rowsReportCredito = 28;//+3 : cabecera titulo, cabecera sub titulos, totales
            int rowsReportCredito = 60;//+3 : cabecera titulo, cabecera sub titulos, totales
            colAux += colsSubReportContado + 1;
            rowAux = addSubReportCredito(hojaCatalogo, rowAux, colAux, formatArial10_bold_border, formatArial10, colsSubReportCredito, rowsReportCredito);
            if (rowAux > rowMax) rowMax = rowAux;
            
            //int rowsReportCobro = 28;//+2 : cabecera titulo, totales
            int rowsReportCobro = 60;//+2 : cabecera titulo, totales
            rowAux = addSubReportCobro(hojaCatalogo, rowAux, colAux, formatArial10_bold_border, formatArial10, colsSubReportCredito, rowsReportCobro);
            if (rowAux > rowMax) rowMax = rowAux;
            
            //int rowsReportAbono = 28;//+2 : cabecera titulo, totales
            int rowsReportAbono = 60;//+2 : cabecera titulo, totales
            rowAux = addSubReportAbonos(hojaCatalogo, rowAux, colAux, formatArial10_bold_border, formatArial10, colsSubReportCredito, rowsReportAbono);
            if (rowAux > rowMax) rowMax = rowAux;
            
            rowAux = addSubReportTotalAbonos(hojaCatalogo, rowAux, colAux, formatArial10_bold_border, formatArial10, colsSubReportCredito);
            if (rowAux > rowMax) rowMax = rowAux;
            
            int rowAuxDownReports = rowMax;
            
            // Sub Reportes Abajo Izquierda
            colAux = 0;
            int rowAuxDownLeft = rowAuxDownReports;
            int rowsReportCheques = 5;//+2 : cabecera titulo, totales
            rowAuxDownLeft = addSubReportCheques(hojaCatalogo, rowAuxDownLeft, colAux, formatArial10_bold_border, formatArial10, colsSubReportContado, rowsReportCheques);
            if (rowAuxDownLeft > rowMax) rowMax = rowAuxDownLeft;
            
            int rowsReportErrores = 11;//+2 : cabecera titulo, totales
            rowAuxDownLeft = addSubReportErrores(hojaCatalogo, rowAuxDownLeft, colAux, formatArial10_bold_border, formatArial10, colsSubReportContado, rowsReportErrores);
            if (rowAuxDownLeft > rowMax) rowMax = rowAuxDownLeft;
            
            //int rowsReportFaltantes = 5;//+2 : cabecera titulo, totales
            int rowsReportFaltantes = 10;//+2 : cabecera titulo, totales
            rowAuxDownLeft = addSubReportFaltantes(hojaCatalogo, rowAuxDownLeft, colAux, formatArial10_bold_border, formatArial10, colsSubReportContado, rowsReportFaltantes);
            if (rowAuxDownLeft > rowMax) rowMax = rowAuxDownLeft;
            
            //int rowsReportDevoluciones = 15;//+2 : cabecera titulo, totales
            int rowsReportDevoluciones = 35;//+2 : cabecera titulo, totales
            rowAuxDownLeft = addSubReportDevoluciones(hojaCatalogo, rowAuxDownLeft, colAux, formatArial10_bold_border, formatArial10, colsSubReportContado, rowsReportDevoluciones);
            if (rowAuxDownLeft > rowMax) rowMax = rowAuxDownLeft;
            // Fin Sub Reportes Abajo Izquierda
            
            // Sub Reportes Abajo Centro
            colAux = 6;
            int rowAuxDownCenter = rowAuxDownReports;
            int colsReportsDownCenter = 3;
            
            int rowsReportDepositos = 5;//+2 : cabecera titulo, totales
            rowAuxDownCenter = addSubReportDepositos(hojaCatalogo, rowAuxDownCenter, colAux, formatArial10_bold_border, formatArial10, colsReportsDownCenter, rowsReportDepositos);
            if (rowAuxDownCenter > rowMax) rowMax = rowAuxDownCenter;
            
            int rowsReportEfectivo = 11;//+2 : cabecera titulo, totales
            rowAuxDownCenter = addSubReportEfectivo(hojaCatalogo, rowAuxDownCenter, colAux, formatArial10_bold_border, formatArial10, colsReportsDownCenter, rowsReportEfectivo);
            if (rowAuxDownCenter > rowMax) rowMax = rowAuxDownCenter;
            
            int rowsReportGastos = 10;//+2 : cabecera titulo, totales
            rowAuxDownCenter = addSubReportGastos(hojaCatalogo, rowAuxDownCenter, colAux, formatArial10_bold_border, formatArial10, colsReportsDownCenter, rowsReportGastos);
            if (rowAuxDownCenter > rowMax) rowMax = rowAuxDownCenter;
            
            rowAuxDownCenter = addSubReportSubTotalSecciones1234(hojaCatalogo, rowAuxDownCenter, colAux, formatArial10_bold_border, formatArial10, colsReportsDownCenter);
            if (rowAuxDownCenter > rowMax) rowMax = rowAuxDownCenter;
            //Fin Sub Reportes Abajo Centro
            
            // Sub Reportes Abajo Derecha
            colAux = 10;
            int rowAuxDownRight = rowAuxDownReports;
            int colsReportsDownRight = 2;
            
            rowAuxDownRight++;
            rowAuxDownRight = addSubReportSubTotalVenta(hojaCatalogo, rowAuxDownRight, colAux, formatArial10_bold_border, formatArial10, colsReportsDownRight);
            if (rowAuxDownRight > rowMax) rowMax = rowAuxDownRight;
            rowAuxDownRight++;
            
            rowAuxDownRight = addSubReportSubTotalContadoCobro(hojaCatalogo, rowAuxDownRight, colAux, formatArial10_bold_border, formatArial10, colsReportsDownRight);
            if (rowAuxDownRight > rowMax) rowMax = rowAuxDownRight;
            rowAuxDownRight++;
            
            rowAuxDownRight = addSubReportSubTotalBalanza(hojaCatalogo, rowAuxDownRight, colAux, formatArial10_bold_border, formatArial10, colsReportsDownRight);
            if (rowAuxDownRight > rowMax) rowMax = rowAuxDownRight;
            rowAuxDownRight++;
                    
            rowAuxDownRight = addSubReportDiaZona(hojaCatalogo, rowAuxDownRight, colAux, formatArial10_bold_border, formatArial10, colsReportsDownRight);
            if (rowAuxDownRight > rowMax) rowMax = rowAuxDownRight;
            
            //fin Sub Reportes Abajo Derecha
        }

        libro.write();
        libro.close();
        //}

        return bos;

    }
    
    private int addSubReportContado(WritableSheet hojaCatalogo, int rowAux, int colAux, WritableCellFormat formato1, WritableCellFormat formato2,
            int colsSubReport, int rowsLimit) throws Exception{
        
        Label lblAux;
        jxl.write.Number numAux;
        
        //Cabecera Sub-reporte
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + colsSubReport) - 1, rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "CONTADO", formato1);
        hojaCatalogo.addCell(lblAux);
        rowAux++;

        //cabecera sub titulos
        lblAux = new Label(colAux, rowAux, "No.", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 1, rowAux, "Clave", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 2, rowAux, "Factura", formato1);
        hojaCatalogo.addCell(lblAux);
        
        lblAux = new Label(colAux + 3, rowAux, "Clientes", formato1);
        hojaCatalogo.addCell(lblAux);
        
        lblAux = new Label(colAux + 4, rowAux, "Importe", formato1);
        hojaCatalogo.addCell(lblAux);

        rowAux++;

        //detalle
        double totalColImporte = 0;
        for (int i=0; i < rowsLimit; i++){
            
            numAux = new jxl.write.Number(colAux, rowAux, i+1, formato2);
            hojaCatalogo.addCell(numAux);
            
            if (i < getContadoData().size()){
                lblAux = new Label(colAux + 1, rowAux, "" + getContadoData().get(i).getDataText1() , formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 2, rowAux, "" + getContadoData().get(i).getDataText2(), formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 3, rowAux, ""+getContadoData().get(i).getDataText3(), formato2);
                hojaCatalogo.addCell(lblAux);

                numAux = new jxl.write.Number(colAux + 4, rowAux, getContadoData().get(i).getDataNum1(), formato2);
                hojaCatalogo.addCell(numAux);

                totalColImporte += getContadoData().get(i).getDataNum1();
            }else{
                lblAux = new Label(colAux + 1, rowAux, "" , formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 2, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 3, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 4, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);
            }
            
            rowAux++;
        }
        
        //Totales
        lblAux = new Label(colAux, rowAux, "", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 1, rowAux, "", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 2, rowAux, "", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 3, rowAux, "", formato1);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 4, rowAux, totalColImporte, formato1);
        hojaCatalogo.addCell(numAux);

        rowAux++;
        
        return rowAux;
    }
    
    private int addSubReportCredito(WritableSheet hojaCatalogo, int rowAux, int colAux, WritableCellFormat formato1, WritableCellFormat formato2,
            int colsSubReport, int rowsLimit) throws Exception{
        
        Label lblAux;
        jxl.write.Number numAux;
        
        //Cabecera Sub-reporte
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + colsSubReport) - 1, rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "CRÉDITO", formato1);
        hojaCatalogo.addCell(lblAux);
        rowAux++;

        //cabecera sub titulos
        //lblAux = new Label(colAux, rowAux, "No.", formato1);
        //hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux, rowAux, "Clave", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 1, rowAux, "Factura", formato1);
        hojaCatalogo.addCell(lblAux);
        
        hojaCatalogo.mergeCells(colAux + 2, rowAux, (colAux + 4), rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux + 2, rowAux, "Clientes", formato1);
        hojaCatalogo.addCell(lblAux);
        
        lblAux = new Label(colAux + 5, rowAux, "Importe", formato1);
        hojaCatalogo.addCell(lblAux);

        rowAux++;

        //detalle
        double totalColImporte = 0;
        for (int i=0; i < rowsLimit; i++){
            //numAux = new jxl.write.Number(colAux, rowAux, i+1, formato2);
            //hojaCatalogo.addCell(numAux);

            if (i < getCreditoData().size()){
                lblAux = new Label(colAux, rowAux, "" + getCreditoData().get(i).getDataText1(), formato2);
                hojaCatalogo.addCell(lblAux);
                
                lblAux = new Label(colAux + 1, rowAux, "" + getCreditoData().get(i).getDataText2() , formato2);
                hojaCatalogo.addCell(lblAux);

                hojaCatalogo.mergeCells(colAux + 2, rowAux, (colAux + 4), rowAux); //col1, row1, col2, row2
                lblAux = new Label(colAux + 2, rowAux, "" + getCreditoData().get(i).getDataText3(), formato2);
                hojaCatalogo.addCell(lblAux);

                numAux = new jxl.write.Number(colAux + 5, rowAux, getCreditoData().get(i).getDataNum1(), formato2);
                hojaCatalogo.addCell(numAux);

                totalColImporte += getCreditoData().get(i).getDataNum1();
            }else{
                lblAux = new Label(colAux, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 1, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                hojaCatalogo.mergeCells(colAux + 2, rowAux, (colAux + 4), rowAux); //col1, row1, col2, row2
                lblAux = new Label(colAux + 2, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                numAux = new jxl.write.Number(colAux + 5, rowAux, 0, formato2);
                hojaCatalogo.addCell(numAux);
            }
            
            rowAux++;
        }
        
        //Totales
        //lblAux = new Label(colAux, rowAux, "", formato1);
        //hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux, rowAux, "", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 1, rowAux, "", formato1);
        hojaCatalogo.addCell(lblAux);

        hojaCatalogo.mergeCells(colAux + 2, rowAux, (colAux + 4), rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux + 2, rowAux, "", formato1);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 5, rowAux, totalColImporte, formato1);
        hojaCatalogo.addCell(numAux);

        rowAux++;
        
        return rowAux;
    }
    
    private int addSubReportCobro(WritableSheet hojaCatalogo, int rowAux, int colAux, WritableCellFormat formato1, WritableCellFormat formato2,
            int colsSubReport, int rowsLimit) throws Exception{
        
        Label lblAux;
        jxl.write.Number numAux;
        
        //Cabecera Sub-reporte
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + colsSubReport) - 1, rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "COBRO", formato1);
        hojaCatalogo.addCell(lblAux);
        rowAux++;

        //cabecera sub titulos
        //lblAux = new Label(colAux, rowAux, "No.", formato1);
        //hojaCatalogo.addCell(lblAux);
        /*
        lblAux = new Label(colAux, rowAux, "Clave", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 1, rowAux, "Factura", formato1);
        hojaCatalogo.addCell(lblAux);
        
        lblAux = new Label(colAux + 2, rowAux, "Clientes", formato1);
        hojaCatalogo.addCell(lblAux);
        
        lblAux = new Label(colAux + 3, rowAux, "Importe", formato1);
        hojaCatalogo.addCell(lblAux);

        rowAux++;
        */
        //detalle
        double totalColImporte = 0;
        for (int i=0; i < rowsLimit; i++){
            //numAux = new jxl.write.Number(colAux, rowAux, i+1, formato2);
            //hojaCatalogo.addCell(numAux);

            if (i < getCobroData().size()){
                lblAux = new Label(colAux, rowAux, "" + getCobroData().get(i).getDataText1(), formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 1, rowAux, "" + getCobroData().get(i).getDataText2(), formato2);
                hojaCatalogo.addCell(lblAux);

                hojaCatalogo.mergeCells(colAux + 2, rowAux, (colAux + 4), rowAux); //col1, row1, col2, row2
                lblAux = new Label(colAux + 2, rowAux, "" + getCobroData().get(i).getDataText3(), formato2);
                hojaCatalogo.addCell(lblAux);

                numAux = new jxl.write.Number(colAux + 5, rowAux, getCobroData().get(i).getDataNum1(), formato2);
                hojaCatalogo.addCell(numAux);

                totalColImporte += getCobroData().get(i).getDataNum1();
            }else{
                lblAux = new Label(colAux, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 1, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                hojaCatalogo.mergeCells(colAux + 2, rowAux, (colAux + 4), rowAux); //col1, row1, col2, row2
                lblAux = new Label(colAux + 2, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                numAux = new jxl.write.Number(colAux + 5, rowAux, 0, formato2);
                hojaCatalogo.addCell(numAux);
            }
            
            rowAux++;
        }
        
        //Totales
        //lblAux = new Label(colAux, rowAux, "", formato1);
        //hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux, rowAux, "", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 1, rowAux, "", formato1);
        hojaCatalogo.addCell(lblAux);

        hojaCatalogo.mergeCells(colAux + 2, rowAux, (colAux + 4), rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux + 2, rowAux, "", formato1);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 5, rowAux, totalColImporte, formato1);
        hojaCatalogo.addCell(numAux);

        rowAux++;
        
        return rowAux;
    }
    
    private int addSubReportAbonos(WritableSheet hojaCatalogo, int rowAux, int colAux, WritableCellFormat formato1, WritableCellFormat formato2,
            int colsSubReport, int rowsLimit) throws Exception{
        
        Label lblAux;
        jxl.write.Number numAux;
        
        //Cabecera Sub-reporte
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + colsSubReport) - 1, rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "ABONOS", formato1);
        hojaCatalogo.addCell(lblAux);
        rowAux++;

        //cabecera sub titulos
        //lblAux = new Label(colAux, rowAux, "No.", formato1);
        //hojaCatalogo.addCell(lblAux);
        /*
        lblAux = new Label(colAux, rowAux, "Clave", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 1, rowAux, "Factura", formato1);
        hojaCatalogo.addCell(lblAux);
        
        lblAux = new Label(colAux + 2, rowAux, "Clientes", formato1);
        hojaCatalogo.addCell(lblAux);
        
        lblAux = new Label(colAux + 3, rowAux, "Importe", formato1);
        hojaCatalogo.addCell(lblAux);

        rowAux++;
        */
        
        //detalle
        double totalColImporte = 0;
        for (int i=0; i < rowsLimit; i++){
            //numAux = new jxl.write.Number(colAux, rowAux, i+1, formato2);
            //hojaCatalogo.addCell(numAux);
            if (i < getAbonosData().size()){
                lblAux = new Label(colAux, rowAux, "" + getAbonosData().get(i).getDataText1(), formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 1, rowAux, "" +getAbonosData().get(i).getDataText2(), formato2);
                hojaCatalogo.addCell(lblAux);

                hojaCatalogo.mergeCells(colAux + 2, rowAux, (colAux + 4), rowAux); //col1, row1, col2, row2
                lblAux = new Label(colAux + 2, rowAux, ""+ getAbonosData().get(i).getDataText3(), formato2);
                hojaCatalogo.addCell(lblAux);

                numAux = new jxl.write.Number(colAux + 5, rowAux, getAbonosData().get(i).getDataNum1(), formato2);
                hojaCatalogo.addCell(numAux);

                totalColImporte += getAbonosData().get(i).getDataNum1();
            }else{
                lblAux = new Label(colAux, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 1, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                hojaCatalogo.mergeCells(colAux + 2, rowAux, (colAux + 4), rowAux); //col1, row1, col2, row2
                lblAux = new Label(colAux + 2, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                numAux = new jxl.write.Number(colAux + 5, rowAux, 0, formato2);
                hojaCatalogo.addCell(numAux);
            }
            
            rowAux++;
        }
        
        //Totales
        //lblAux = new Label(colAux, rowAux, "", formato1);
        //hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux, rowAux, "", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 1, rowAux, "", formato1);
        hojaCatalogo.addCell(lblAux);

        hojaCatalogo.mergeCells(colAux + 2, rowAux, (colAux + 4), rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux + 2, rowAux, "", formato1);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 5, rowAux, totalColImporte, formato1);
        hojaCatalogo.addCell(numAux);

        rowAux++;
        
        return rowAux;
    }
    
    private int addSubReportTotalAbonos(WritableSheet hojaCatalogo, int rowAux, int colAux, WritableCellFormat formato1, WritableCellFormat formato2,
            int colsSubReport) throws Exception{
        
        Label lblAux;
        jxl.write.Number numAux;

        //renglon con celdas vacias
        //lblAux = new Label(colAux, rowAux, "", formato1);
        //hojaCatalogo.addCell(lblAux);
        lblAux = new Label(colAux, rowAux, "", formato2);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 1, rowAux, "", formato2);
        hojaCatalogo.addCell(lblAux);
        
        hojaCatalogo.mergeCells(colAux + 2, rowAux, (colAux + 4), rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux + 2, rowAux, "", formato2);
        hojaCatalogo.addCell(lblAux);
        
        lblAux = new Label(colAux + 5, rowAux, "", formato2);
        hojaCatalogo.addCell(lblAux);

        rowAux++;
        
        //Totales
        //lblAux = new Label(colAux, rowAux, "", formato1);
        //hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux, rowAux, "", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 1, rowAux, "", formato1);
        hojaCatalogo.addCell(lblAux);

        hojaCatalogo.mergeCells(colAux + 2, rowAux, (colAux + 4), rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux + 2, rowAux, "", formato1);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 5, rowAux, 0, formato1);
        hojaCatalogo.addCell(numAux);

        rowAux++;
        
        return rowAux;
    }

    // Abajo Columna Izquierda
    private int addSubReportCheques(WritableSheet hojaCatalogo, int rowAux, int colAux, WritableCellFormat formato1, WritableCellFormat formato2,
            int colsSubReport, int rowsLimit) throws Exception{
        
        Label lblAux;
        jxl.write.Number numAux;
        
        //Cabecera Sub-reporte
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + colsSubReport) - 1, rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "CHEQUES", formato1);
        hojaCatalogo.addCell(lblAux);
        rowAux++;

        //cabecera sub titulos
        lblAux = new Label(colAux, rowAux, "", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 1, rowAux, "Fecha", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 2, rowAux, "No.", formato1);
        hojaCatalogo.addCell(lblAux);
        
        lblAux = new Label(colAux + 3, rowAux, "Banco", formato1);
        hojaCatalogo.addCell(lblAux);
        
        lblAux = new Label(colAux + 4, rowAux, "$", formato1);
        hojaCatalogo.addCell(lblAux);

        rowAux++;

        //detalle
        double totalColImporte = 0;
        for (int i=0; i < rowsLimit; i++){
            if (i < getChequesData().size()){
                lblAux = new Label(colAux, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 1, rowAux, "" + getChequesData().get(i).getDataText1(), formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 2, rowAux, "" + getChequesData().get(i).getDataText2(), formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 3, rowAux, "" + getChequesData().get(i).getDataText3(), formato2);
                hojaCatalogo.addCell(lblAux);

                numAux = new jxl.write.Number(colAux + 4, rowAux, getChequesData().get(i).getDataNum1(), formato2);
                hojaCatalogo.addCell(numAux);

                totalColImporte += getChequesData().get(i).getDataNum1();
            }else{
                lblAux = new Label(colAux, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 1, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 2, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 3, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                numAux = new jxl.write.Number(colAux + 4, rowAux, 0, formato2);
                hojaCatalogo.addCell(numAux);
            }
            
            rowAux++;
        }
        
        //Totales
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + 3) , rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "TOTAL 1", formato1);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 4, rowAux, totalColImporte, formato1);
        hojaCatalogo.addCell(numAux);

        rowAux++;
        
        return rowAux;
    }
    
    private int addSubReportErrores(WritableSheet hojaCatalogo, int rowAux, int colAux, WritableCellFormat formato1, WritableCellFormat formato2,
            int colsSubReport, int rowsLimit) throws Exception{
        
        Label lblAux;
        jxl.write.Number numAux;
        
        //Cabecera Sub-reporte
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + colsSubReport) - 1, rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "ERRORES - CANCELACIONES", formato1);
        hojaCatalogo.addCell(lblAux);
        rowAux++;

        //cabecera sub titulos
        lblAux = new Label(colAux, rowAux, "", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 1, rowAux, "Pedido", formato1);
        hojaCatalogo.addCell(lblAux);

        hojaCatalogo.mergeCells(colAux  +2 , rowAux, (colAux + 3), rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux + 2, rowAux, "Cliente", formato1);
        hojaCatalogo.addCell(lblAux);
        
        lblAux = new Label(colAux + 4, rowAux, "$", formato1);
        hojaCatalogo.addCell(lblAux);

        rowAux++;

        //detalle
        double totalColImporte = 0;
        for (int i=0; i < rowsLimit; i++){
            if (i < getErroresData().size()){
                lblAux = new Label(colAux, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 1, rowAux, "" + getErroresData().get(i).getDataText1(), formato2);
                hojaCatalogo.addCell(lblAux);

                hojaCatalogo.mergeCells(colAux  +2 , rowAux, (colAux + 3), rowAux); //col1, row1, col2, row2
                lblAux = new Label(colAux + 2, rowAux, "" + getErroresData().get(i).getDataText2(), formato2);
                hojaCatalogo.addCell(lblAux);

                numAux = new jxl.write.Number(colAux + 4, rowAux, getErroresData().get(i).getDataNum1(), formato2);
                hojaCatalogo.addCell(numAux);

                totalColImporte += getErroresData().get(i).getDataNum1();
            }else{
                lblAux = new Label(colAux, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 1, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                hojaCatalogo.mergeCells(colAux  +2 , rowAux, (colAux + 3), rowAux); //col1, row1, col2, row2
                lblAux = new Label(colAux + 2, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                numAux = new jxl.write.Number(colAux + 4, rowAux, 0, formato2);
                hojaCatalogo.addCell(numAux);
            }
            
            rowAux++;
        }
        
        //Totales
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + 3) , rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "A", formato1);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 4, rowAux, totalColImporte, formato1);
        hojaCatalogo.addCell(numAux);

        rowAux++;
        
        return rowAux;
    }
    
    private int addSubReportFaltantes(WritableSheet hojaCatalogo, int rowAux, int colAux, WritableCellFormat formato1, WritableCellFormat formato2,
            int colsSubReport, int rowsLimit) throws Exception{
        
        Label lblAux;
        jxl.write.Number numAux;
        
        //Cabecera Sub-reporte
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + colsSubReport) - 1, rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "FALTANTES", formato1);
        hojaCatalogo.addCell(lblAux);
        rowAux++;

        //cabecera sub titulos
        lblAux = new Label(colAux, rowAux, "", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 1, rowAux, "Cantidad", formato1);
        hojaCatalogo.addCell(lblAux);

        hojaCatalogo.mergeCells(colAux  +2 , rowAux, (colAux + 3), rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux + 2, rowAux, "", formato1);
        hojaCatalogo.addCell(lblAux);
        
        lblAux = new Label(colAux + 4, rowAux, "Total", formato1);
        hojaCatalogo.addCell(lblAux);

        rowAux++;

        //detalle
        double totalColImporte = 0;
        for (int i=0; i < rowsLimit; i++){
            if (i < getFaltantesData().size()){
                lblAux = new Label(colAux, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 1, rowAux, "" + getFaltantesData().get(i).getDataText1(), formato2);
                hojaCatalogo.addCell(lblAux);

                hojaCatalogo.mergeCells(colAux  +2 , rowAux, (colAux + 3), rowAux); //col1, row1, col2, row2
                lblAux = new Label(colAux + 2, rowAux, ""+ getFaltantesData().get(i).getDataText2(), formato2);
                hojaCatalogo.addCell(lblAux);

                numAux = new jxl.write.Number(colAux + 4, rowAux, getFaltantesData().get(i).getDataNum1(), formato2);
                hojaCatalogo.addCell(numAux);

                totalColImporte += getFaltantesData().get(i).getDataNum1();
            }else{
                lblAux = new Label(colAux, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 1, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                hojaCatalogo.mergeCells(colAux  +2 , rowAux, (colAux + 3), rowAux); //col1, row1, col2, row2
                lblAux = new Label(colAux + 2, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                numAux = new jxl.write.Number(colAux + 4, rowAux, 0, formato2);
                hojaCatalogo.addCell(numAux);
            }
            
            rowAux++;
        }
        
        //Totales
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + 3) , rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "B", formato1);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 4, rowAux, totalColImporte, formato1);
        hojaCatalogo.addCell(numAux);

        rowAux++;
        
        return rowAux;
    }
    
    private int addSubReportDevoluciones(WritableSheet hojaCatalogo, int rowAux, int colAux, WritableCellFormat formato1, WritableCellFormat formato2,
            int colsSubReport, int rowsLimit) throws Exception{
        
        Label lblAux;
        jxl.write.Number numAux;
        
        //Cabecera Sub-reporte
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + colsSubReport) - 1, rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "DEVOLUCIONES", formato1);
        hojaCatalogo.addCell(lblAux);
        rowAux++;

        //cabecera sub titulos
        lblAux = new Label(colAux, rowAux, "", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 1, rowAux, "Cliente", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 2, rowAux, "Piezas", formato1);
        hojaCatalogo.addCell(lblAux);
        
        lblAux = new Label(colAux + 3, rowAux, "Producto     Kg.", formato1);
        hojaCatalogo.addCell(lblAux);
        
        lblAux = new Label(colAux + 4, rowAux, "Total", formato1);
        hojaCatalogo.addCell(lblAux);

        rowAux++;

        //detalle
        double totalColImporte = 0;
        for (int i=0; i < rowsLimit; i++){
            if (i < getDevolucionesData().size()){
                lblAux = new Label(colAux, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 1, rowAux, "" + getDevolucionesData().get(i).getDataText1(), formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 2, rowAux, "" + getDevolucionesData().get(i).getDataText2(), formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 3, rowAux, "" + getDevolucionesData().get(i).getDataText3(), formato2);
                hojaCatalogo.addCell(lblAux);

                numAux = new jxl.write.Number(colAux + 4, rowAux, getDevolucionesData().get(i).getDataNum1(), formato2);
                hojaCatalogo.addCell(numAux);

                totalColImporte += getDevolucionesData().get(i).getDataNum1();
            }else{
                lblAux = new Label(colAux, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 1, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 2, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 3, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                numAux = new jxl.write.Number(colAux + 4, rowAux, 0, formato2);
                hojaCatalogo.addCell(numAux);
            }
            
            rowAux++;
        }
        
        //Totales
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + 3) , rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "C", formato1);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 4, rowAux, totalColImporte, formato1);
        hojaCatalogo.addCell(numAux);

        rowAux++;
        
        return rowAux;
    }
    //Fin Abajo Columna Izquierda
    
    
    //Abajo Columna Centro
    
    private int addSubReportDepositos(WritableSheet hojaCatalogo, int rowAux, int colAux, WritableCellFormat formato1, WritableCellFormat formato2,
            int colsSubReport, int rowsLimit) throws Exception{
        
        Label lblAux;
        jxl.write.Number numAux;
        
        //Cabecera Sub-reporte
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + colsSubReport) - 1, rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "DEPOSITOS", formato1);
        hojaCatalogo.addCell(lblAux);
        rowAux++;

        //cabecera sub titulos
        lblAux = new Label(colAux, rowAux, "Banco", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 1, rowAux, "", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 2, rowAux, "$", formato1);
        hojaCatalogo.addCell(lblAux);

        rowAux++;

        //detalle
        double totalColImporte = 0;
        for (int i=0; i < rowsLimit; i++){
            if (i < getDepositosData().size()){
                lblAux = new Label(colAux, rowAux, "" + getDepositosData().get(i).getDataText1(), formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 1, rowAux, "" + getDepositosData().get(i).getDataText2(), formato2);
                hojaCatalogo.addCell(lblAux);

                numAux = new jxl.write.Number(colAux + 2, rowAux, getDepositosData().get(i).getDataNum1(), formato2);
                hojaCatalogo.addCell(numAux);

                totalColImporte += getDepositosData().get(i).getDataNum1();
            }else{
                lblAux = new Label(colAux, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 1, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                numAux = new jxl.write.Number(colAux + 2, rowAux, 0, formato2);
                hojaCatalogo.addCell(numAux);
            }
            
            rowAux++;
        }
        
        //Totales
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + 1) , rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "TOTAL 2", formato1);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 2, rowAux, totalColImporte, formato1);
        hojaCatalogo.addCell(numAux);

        rowAux++;
        
        return rowAux;
    }
    
    private int addSubReportEfectivo(WritableSheet hojaCatalogo, int rowAux, int colAux, WritableCellFormat formato1, WritableCellFormat formato2,
            int colsSubReport, int rowsLimit) throws Exception{
        
        Label lblAux;
        jxl.write.Number numAux;
        
        //Cabecera Sub-reporte
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + colsSubReport) - 1, rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "EFECTIVO", formato1);
        hojaCatalogo.addCell(lblAux);
        rowAux++;

        //cabecera sub titulos
        lblAux = new Label(colAux, rowAux, "Denominación", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 1, rowAux, "Cant.", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 2, rowAux, "$", formato1);
        hojaCatalogo.addCell(lblAux);

        rowAux++;

        //detalle
        double totalColImporte = 0;
        for (int i=0; i < rowsLimit; i++){
            if (i < getEfectivoData().size()){
                lblAux = new Label(colAux, rowAux, getEfectivoData().get(i).getDataText1(), formato2);
                hojaCatalogo.addCell(lblAux);

                numAux = new jxl.write.Number(colAux + 1, rowAux, getEfectivoData().get(i).getDataInt1(), formato2);
                hojaCatalogo.addCell(numAux);

                numAux = new jxl.write.Number(colAux + 2, rowAux, getEfectivoData().get(i).getDataNum2(), formato2);
                hojaCatalogo.addCell(numAux);

                totalColImporte += getEfectivoData().get(i).getDataNum2();
            }else{
                lblAux = new Label(colAux, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                numAux = new jxl.write.Number(colAux + 1, rowAux, 0, formato2);
                hojaCatalogo.addCell(numAux);

                numAux = new jxl.write.Number(colAux + 2, rowAux, 0, formato2);
                hojaCatalogo.addCell(numAux);
            }
            
            rowAux++;
        }
        
        //Totales
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + 1) , rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "TOTAL 3", formato1);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 2, rowAux, totalColImporte, formato1);
        hojaCatalogo.addCell(numAux);

        rowAux++;
        
        return rowAux;
    }
    
    private int addSubReportGastos(WritableSheet hojaCatalogo, int rowAux, int colAux, WritableCellFormat formato1, WritableCellFormat formato2,
            int colsSubReport, int rowsLimit) throws Exception{
        
        Label lblAux;
        jxl.write.Number numAux;
        
        //Cabecera Sub-reporte
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + colsSubReport) - 1, rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "GASTOS", formato1);
        hojaCatalogo.addCell(lblAux);
        rowAux++;

        //cabecera sub titulos
        lblAux = new Label(colAux, rowAux, "Concepto", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 1, rowAux, "Comentario", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 2, rowAux, "$", formato1);
        hojaCatalogo.addCell(lblAux);

        rowAux++;

        //detalle
        double totalColImporte = 0;
        for (int i=0; i < rowsLimit; i++){
            if (i < getGastosData().size()){
                lblAux = new Label(colAux, rowAux, "" + getGastosData().get(i).getDataText1(), formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 1, rowAux, "" + getGastosData().get(i).getDataText2(), formato2);
                hojaCatalogo.addCell(lblAux);

                numAux = new jxl.write.Number(colAux + 2, rowAux, getGastosData().get(i).getDataNum1(), formato2);
                hojaCatalogo.addCell(numAux);

                totalColImporte += getGastosData().get(i).getDataNum1();
            }else{
                lblAux = new Label(colAux, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                lblAux = new Label(colAux + 1, rowAux, "", formato2);
                hojaCatalogo.addCell(lblAux);

                numAux = new jxl.write.Number(colAux + 2, rowAux, 0, formato2);
                hojaCatalogo.addCell(numAux);
            }
            
            rowAux++;
        }
        
        //Totales
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + 1) , rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "TOTAL 4", formato1);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 2, rowAux, totalColImporte, formato1);
        hojaCatalogo.addCell(numAux);

        rowAux++;
        
        return rowAux;
    }
    
    private int addSubReportSubTotalSecciones1234(WritableSheet hojaCatalogo, int rowAux, int colAux, WritableCellFormat formato1, WritableCellFormat formato2,
            int colsSubReport) throws Exception{
        
        Label lblAux;
        jxl.write.Number numAux;
        
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + 1) , rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "Total  1,2,3 y 4", formato1);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 2, rowAux, totalSub1234, formato1);
        hojaCatalogo.addCell(numAux);
        
        rowAux++;

        // Nombre:           |   _____________________
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + 2) , rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "" + nombreEmpleado , formato2);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 3, rowAux, "", formato2);
        hojaCatalogo.addCell(lblAux);

        hojaCatalogo.mergeCells(colAux + 4, rowAux, (colAux + 5) , rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux + 4, rowAux, "_________________________", formato2);
        hojaCatalogo.addCell(lblAux);

        rowAux++;
        
        // Nombre:           |   FIRMA
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + 2) , rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "NOMBRE", formato2);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 3, rowAux, "", formato2);
        hojaCatalogo.addCell(lblAux);

        hojaCatalogo.mergeCells(colAux + 4, rowAux, (colAux + 5) , rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux + 4, rowAux, "FIRMA", formato2);
        hojaCatalogo.addCell(lblAux);
        
        rowAux++;
        
        // Leyenda (celda mezclada 2 x 6 )
        String leyenda = "DECLARO BAJO PROTESTA DE  DECIR VERDAD QUE LOS DATOS ASENTADOS EN ESTE CONTROL SON CIERTOS";
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + 5) , rowAux+1); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, leyenda, formato2);
        hojaCatalogo.addCell(lblAux);
        
        rowAux++;// doble, por que se mezclaron 2 rows
        rowAux++;
        
        return rowAux;
    }
    //Fin Abajo Columna Centro
    
    
    // Abajo Columna Derecha
    
    private int addSubReportSubTotalVenta(WritableSheet hojaCatalogo, int rowAux, int colAux, WritableCellFormat formato1, WritableCellFormat formato2,
            int colsSubReport) throws Exception{
        
        Label lblAux;
        jxl.write.Number numAux;

        // Contado   |   0
        lblAux = new Label(colAux, rowAux, "CONTADO", formato2);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 1, rowAux, getSubtotalesVenta().size()>0?getSubtotalesVenta().get(0).getDataNum1():0, formato2);
        hojaCatalogo.addCell(numAux);

        rowAux++;
        
        // Crédito   |   0
        lblAux = new Label(colAux, rowAux, "CRÉDITO", formato2);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 1, rowAux, getSubtotalesVenta().size()>0?getSubtotalesVenta().get(0).getDataNum2():0, formato2);
        hojaCatalogo.addCell(numAux);

        rowAux++;
        
        // TOTAL VENTA   |   0
        lblAux = new Label(colAux, rowAux, "VENTA", formato1);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 1, rowAux, getSubtotalesVenta().size()>0?getSubtotalesVenta().get(0).getDataNum3():0, formato1);
        hojaCatalogo.addCell(numAux);

        rowAux++;
        
        return rowAux;
    }
    
    private int addSubReportSubTotalContadoCobro(WritableSheet hojaCatalogo, int rowAux, int colAux, WritableCellFormat formato1, WritableCellFormat formato2,
            int colsSubReport) throws Exception{
        
        Label lblAux;
        jxl.write.Number numAux;

        // Contado   |   0
        lblAux = new Label(colAux, rowAux, "CONTADO", formato2);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 1, rowAux, getSubtotalesCobranza().size()>0? getSubtotalesCobranza().get(0).getDataNum1():0, formato2);
        hojaCatalogo.addCell(numAux);

        rowAux++;
        
        // Cobro   |   0
        lblAux = new Label(colAux, rowAux, "COBRO", formato2);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 1, rowAux, getSubtotalesCobranza().size()>0? getSubtotalesCobranza().get(0).getDataNum2():0, formato2);
        hojaCatalogo.addCell(numAux);

        rowAux++;
        
        // TOTAL   |   0
        lblAux = new Label(colAux, rowAux, "TOTAL", formato1);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 1, rowAux, getSubtotalesCobranza().size()>0? getSubtotalesCobranza().get(0).getDataNum3():0, formato1);
        hojaCatalogo.addCell(numAux);

        rowAux++;
        
        return rowAux;
    }
    
    private int addSubReportSubTotalBalanza(WritableSheet hojaCatalogo, int rowAux, int colAux, WritableCellFormat formato1, WritableCellFormat formato2,
            int colsSubReport) throws Exception{
        
        Label lblAux;
        jxl.write.Number numAux;

        // ENTREGA   |   0
        lblAux = new Label(colAux, rowAux, "ENTREGA", formato2);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 1, rowAux, getTotalesBalanza().size()>0? getTotalesBalanza().get(0).getDataNum1():0, formato2);
        hojaCatalogo.addCell(numAux);

        rowAux++;
        
        // FALTANTE   |   0
        lblAux = new Label(colAux, rowAux, "FALTANTE", formato2);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 1, rowAux, getTotalesBalanza().size()>0? getTotalesBalanza().get(0).getDataNum2():0, formato2);
        hojaCatalogo.addCell(numAux);

        rowAux++;
        
        // SOBRANTE   |   0
        lblAux = new Label(colAux, rowAux, "SOBRANTE", formato2);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 1, rowAux, getTotalesBalanza().size()>0? getTotalesBalanza().get(0).getDataNum3():0, formato2);
        hojaCatalogo.addCell(numAux);
        
        rowAux++;
        
        // TOTAL   |   0
        lblAux = new Label(colAux, rowAux, "TOTAL", formato1);
        hojaCatalogo.addCell(lblAux);

        numAux = new jxl.write.Number(colAux + 1, rowAux, getTotalesBalanza().size()>0? getTotalesBalanza().get(0).getDataNum4():0, formato1);
        hojaCatalogo.addCell(numAux);
        
        rowAux++;
        
        return rowAux;
    }
    
    private int addSubReportDiaZona(WritableSheet hojaCatalogo, int rowAux, int colAux, WritableCellFormat formato1, WritableCellFormat formato2,
            int colsSubReport) throws Exception{
        
        Label lblAux;
        jxl.write.Number numAux;

        // DIA
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + 1), rowAux + 2); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "" + DateManage.formatDate(fecha, "EEEE dd 'de' MMMM"), formato2);
        hojaCatalogo.addCell(lblAux);

        rowAux = rowAux + 4;
        
        // FALTANTE   |   0
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + 1), rowAux + 2); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, "ZONA: " + zonaRuta, formato2);
        hojaCatalogo.addCell(lblAux);

        rowAux = rowAux + 4;
        
        return rowAux;
    }
    //Fin Abajo Columna Derecha
    
    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getZonaRuta() {
        return zonaRuta;
    }

    public void setZonaRuta(String zonaRuta) {
        this.zonaRuta = zonaRuta;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public Date getRangoFechaMin() {
        return rangoFechaMin;
    }

    public void setRangoFechaMin(Date rangoFechaMin) {
        this.rangoFechaMin = rangoFechaMin;
    }

    public Date getRangoFechaMax() {
        return rangoFechaMax;
    }

    public void setRangoFechaMax(Date rangoFechaMax) {
        this.rangoFechaMax = rangoFechaMax;
    }

    public double getTotalSub1234() {
        return totalSub1234;
    }

    public void setTotalSub1234(double totalSub1234) {
        this.totalSub1234 = totalSub1234;
    }

    public List<ReportGenericData> getContadoData() {
        if (contadoData==null) contadoData = new ArrayList<ReportGenericData>();
        return contadoData;
    }

    public List<ReportGenericData> getCreditoData() {
        if (creditoData==null) creditoData = new ArrayList<ReportGenericData>();
        return creditoData;
    }

    public List<ReportGenericData> getCobroData() {
        if (cobroData==null) cobroData = new ArrayList<ReportGenericData>();
        return cobroData;
    }

    public List<ReportGenericData> getAbonosData() {
        if (abonosData==null) abonosData = new ArrayList<ReportGenericData>();
        return abonosData;
    }

    public List<ReportGenericData> getChequesData() {
        if (chequesData==null) chequesData = new ArrayList<ReportGenericData>();
        return chequesData;
    }

    public List<ReportGenericData> getDepositosData() {
        if (depositosData==null) depositosData = new ArrayList<ReportGenericData>();
        return depositosData;
    }

    public List<ReportGenericData> getErroresData() {
        if (erroresData==null) erroresData = new ArrayList<ReportGenericData>();
        return erroresData;
    }

    public List<ReportGenericData> getEfectivoData() {
        if (efectivoData==null) efectivoData = new ArrayList<ReportGenericData>();
        return efectivoData;
    }

    public List<ReportGenericData> getFaltantesData() {
        if (faltantesData==null) faltantesData = new ArrayList<ReportGenericData>();
        return faltantesData;
    }

    public List<ReportGenericData> getDevolucionesData() {
        if (devolucionesData==null) devolucionesData = new ArrayList<ReportGenericData>();
        return devolucionesData;
    }

    public List<ReportGenericData> getGastosData() {
        if (gastosData==null) gastosData = new ArrayList<ReportGenericData>();
        return gastosData;
    }

    public List<ReportGenericData> getSubtotalesVenta() {
        if (subtotalesVenta==null) subtotalesVenta = new ArrayList<ReportGenericData>();
        return subtotalesVenta;
    }

    public List<ReportGenericData> getSubtotalesCobranza() {
        if (subtotalesCobranza==null) subtotalesCobranza = new ArrayList<ReportGenericData>();
        return subtotalesCobranza;
    }

    public List<ReportGenericData> getTotalesBalanza() {
        if (totalesBalanza==null) totalesBalanza = new ArrayList<ReportGenericData>();
        return totalesBalanza;
    }

    
    public static class ReportGenericData {
        
        private String dataText1 = "";
        private String dataText2 = "";
        private String dataText3 = "";
        
        private double dataNum1;
        private double dataNum2;
        private double dataNum3;
        private double dataNum4;
        
        private int dataInt1;

        public String getDataText1() {
            return dataText1;
        }

        public void setDataText1(String dataText1) {
            this.dataText1 = dataText1;
        }

        public String getDataText2() {
            return dataText2;
        }

        public void setDataText2(String dataText2) {
            this.dataText2 = dataText2;
        }

        public String getDataText3() {
            return dataText3;
        }

        public void setDataText3(String dataText3) {
            this.dataText3 = dataText3;
        }

        public double getDataNum1() {
            return dataNum1;
        }

        public void setDataNum1(double dataNum1) {
            this.dataNum1 = dataNum1;
        }

        public double getDataNum2() {
            return dataNum2;
        }

        public void setDataNum2(double dataNum2) {
            this.dataNum2 = dataNum2;
        }

        public double getDataNum3() {
            return dataNum3;
        }

        public void setDataNum3(double dataNum3) {
            this.dataNum3 = dataNum3;
        }

        public double getDataNum4() {
            return dataNum4;
        }

        public void setDataNum4(double dataNum4) {
            this.dataNum4 = dataNum4;
        }

        public int getDataInt1() {
            return dataInt1;
        }

        public void setDataInt1(int dataInt1) {
            this.dataInt1 = dataInt1;
        }        
        
    }
    
}
