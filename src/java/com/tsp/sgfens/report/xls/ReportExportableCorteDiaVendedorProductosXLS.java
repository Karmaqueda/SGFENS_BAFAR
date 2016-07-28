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
import java.util.List;

import jxl.*;
import jxl.write.*;
import jxl.format.Colour;
import jxl.format.Alignment;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 14/01/2015
 */
public class ReportExportableCorteDiaVendedorProductosXLS extends ReportExportable {
        
    private Date fecha = null;
    private String nombreVendedor = "";
    private String zona = "";
    private List<CorteDiaMarcaData> listaCorteDiaMarcaData;

    public ReportExportableCorteDiaVendedorProductosXLS(Connection conn) {
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

            Label lblAux = new Label(col, row, "Reporte Corte del Día Productos", formatArial16_blue_bold);
            hojaCatalogo.addCell(lblAux);
            row++;

        }

        {
            int col = 0;
            int rIni = row;
            Label lblAux = new Label(col, row, "FECHA: " + DateManage.dateTimeToStringEspanol(fecha), formatArial8_bold);
            hojaCatalogo.addCell(lblAux);
            row++;

            lblAux = new Label(col, row, "VENDEDOR: " + nombreVendedor, formatArial8_bold);
            hojaCatalogo.addCell(lblAux);
            row++;

            lblAux = new Label(col, row, "ZONA: " + zona, formatArial8_bold);
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
        int maxSubReportByRow = 3;//maximo de sub-reportes de forma horizontal
        int nReportOnRow = 1;
        //sub-reporte
        {
            int colsSubReport = 4;
            int rowAux = row; //para no afectar a la variable row general
            int colAux = 0;//

            for (CorteDiaMarcaData marcaData : getListaCorteDiaMarcaData()){

                rowAux = row;
                rowAux = addSubReport(hojaCatalogo, rowAux, colAux, formatArial10_bold_border, formatArial10, marcaData, colsSubReport);

                //System.out.println("rowAux: " + rowAux);
                if (rowAux > rowMax)
                    rowMax = rowAux;

                nReportOnRow++; //aumentamos numero de sub reporte en fila
                if (nReportOnRow > maxSubReportByRow){
                    //si se alcanzo el maximo de sub-reportes por fila,
                    nReportOnRow = 1; //reiniciamos contador de sub-reporte en fila
                    row  = rowMax + 2; //movemos al mximo de renglon usado mas 2 celdas
                }

                //despues de cada sub-reporte movemos la columna
                colAux = ((colsSubReport + 1) * (nReportOnRow-1));
            }
            
        }

        libro.write();
        libro.close();
        //}

        return bos;

    }
    
    private int addSubReport(WritableSheet hojaCatalogo, int rowAux, int colAux, WritableCellFormat formato1, WritableCellFormat formato2, 
            CorteDiaMarcaData data, int colsSubReport) throws Exception{
        //Cabecera Sub-reporte
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + colsSubReport)-1, rowAux); //col1, row1, col2, row2
        Label lblAux = new Label(colAux, rowAux, data.getNombreMarca(), formato1);
        hojaCatalogo.addCell(lblAux);
        rowAux++;

        //cabecera sub titulos
        lblAux = new Label(colAux, rowAux, "Producto", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 1, rowAux, "Salida", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 2, rowAux, "Venta $", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 3, rowAux, "Entrada", formato1);
        hojaCatalogo.addCell(lblAux);

        rowAux++;

        //detalle
        int cProducto = 1;
        for (CorteDiaProductoData detailData : data.getListaCorteDiaProductoData()){
            lblAux = new Label(colAux, rowAux, "" + detailData.getNombreProducto(), formato2);
            hojaCatalogo.addCell(lblAux);

            lblAux = new Label(colAux + 1, rowAux, "" + detailData.getSalida() , formato2);
            hojaCatalogo.addCell(lblAux);

            lblAux = new Label(colAux + 2, rowAux, "" + detailData.getVenta(), formato2);
            hojaCatalogo.addCell(lblAux);

            lblAux = new Label(colAux + 3, rowAux, "" + detailData.getEntrada(), formato2);
            hojaCatalogo.addCell(lblAux);

            cProducto++;
            rowAux++;
        }
        
        return rowAux;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public List<CorteDiaMarcaData> getListaCorteDiaMarcaData() {
        if (listaCorteDiaMarcaData==null)
            listaCorteDiaMarcaData = new ArrayList<CorteDiaMarcaData>();
        return listaCorteDiaMarcaData;
    }

    
    public static class CorteDiaMarcaData {
        private String nombreMarca =  "";
        
        private List<CorteDiaProductoData> listaCorteDiaProductoData;

        public String getNombreMarca() {
            return nombreMarca;
        }

        public void setNombreMarca(String nombreMarca) {
            this.nombreMarca = nombreMarca;
        }

        public List<CorteDiaProductoData> getListaCorteDiaProductoData() {
            if (listaCorteDiaProductoData == null)
                listaCorteDiaProductoData = new ArrayList<CorteDiaProductoData>();
            return listaCorteDiaProductoData;
        }
        
    }
    
    public static class CorteDiaProductoData {
        
        private String nombreProducto = "";
        private double salida;
        private double venta;
        private double entrada;

        public String getNombreProducto() {
            return nombreProducto;
        }

        public void setNombreProducto(String nombreProducto) {
            this.nombreProducto = nombreProducto;
        }

        public double getSalida() {
            return salida;
        }

        public void setSalida(double salida) {
            this.salida = salida;
        }

        public double getVenta() {
            return venta;
        }

        public void setVenta(double venta) {
            this.venta = venta;
        }

        public double getEntrada() {
            return entrada;
        }

        public void setEntrada(double entrada) {
            this.entrada = entrada;
        }
        
    }
    
}
