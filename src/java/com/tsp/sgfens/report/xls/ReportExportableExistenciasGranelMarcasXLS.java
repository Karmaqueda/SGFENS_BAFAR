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
public class ReportExportableExistenciasGranelMarcasXLS extends ReportExportable {
    
    private Date fecha = null;
    private List<ExistenciasGranelMarcaData> listaExistenciasGranelMarcaData;

    public ReportExportableExistenciasGranelMarcasXLS(Connection conn) {
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

            Label lblAux = new Label(col, row, "Reporte Marcas (Existencias)", formatArial16_blue_bold);
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
        int maxSubReportByRow = 7;//maximo de sub-reportes de forma horizontal
        int nReportOnRow = 1;
        //sub-reporte
        {
            int colsSubReport = 3;
            int rowAux = 0; //para no afectar a la variable row general
            int colAux = 0;//

            boolean combinarTituloConAnterior = false;
            for (ExistenciasGranelMarcaData marcaData : getListaExistenciasGranelMarcaData()){
            
                for (ExistenciaGranelProducto productoData : marcaData.getListaExistenciaGranelProducto()){
                    
                    rowAux = row + 1;
                    rowAux = addSubReport(hojaCatalogo, rowAux, colAux, formatArial10_bold_border, formatArial10, productoData, marcaData.getNombreMarca(), combinarTituloConAnterior, colsSubReport);

                    //System.out.println("rowAux: " + rowAux);
                    if (rowAux > rowMax)
                        rowMax = rowAux;

                    nReportOnRow++; //aumentamos numero de sub reporte en fila
                    if (nReportOnRow > maxSubReportByRow){
                        //si se alcanzo el maximo de sub-reportes por fila,
                        nReportOnRow = 1; //reiniciamos contador de sub-reporte en fila
                        row  = rowMax + 1; //movemos al mximo de renglon usado mas 2 celdas
                    }

                    //despues de cada sub-reporte movemos la columna
                    colAux = ((colsSubReport + 1) * (nReportOnRow-1));
                    
                    //posiblemente existan mas productos de las misma marca, así que intentamos combinar Titulo
                    combinarTituloConAnterior = true;
                }
                
                //es un cambio de marca, por lo tanto ya no necesitamos que se combinen las celdas de titulo
                combinarTituloConAnterior = false;
            }
            
        }

        libro.write();
        libro.close();
        //}

        return bos;

    }
    
    private int addSubReport(WritableSheet hojaCatalogo, int rowAux, int colAux, WritableCellFormat formato1, WritableCellFormat formato2, 
            ExistenciaGranelProducto data, String nombreMarca, boolean combinarTituloConAnterior , int colsSubReport) throws Exception{
        
        Label lblAux;
        //Cabecera Titulo Sub-Reporte (Marca)
        if ( (colAux - 4)>=0 && combinarTituloConAnterior ){
            //combinamos para que se siga preservando el titulo de marca que ya se tiene
            hojaCatalogo.mergeCells(colAux - 4 , rowAux, (colAux + colsSubReport) - 1, rowAux); //col1, row1, col2, row2
        }else{
            hojaCatalogo.mergeCells(colAux, rowAux, (colAux + colsSubReport) - 1, rowAux); //col1, row1, col2, row2
            lblAux = new Label(colAux, rowAux, nombreMarca, formato1);
            hojaCatalogo.addCell(lblAux);
        }
        rowAux++;
        
        //Cabecera Sub-reporte (Nombre producto)
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + colsSubReport) - 1, rowAux); //col1, row1, col2, row2
        lblAux = new Label(colAux, rowAux, data.getNombreProducto(), formato1);
        hojaCatalogo.addCell(lblAux);
        rowAux++;

        //cabecera sub titulos
        lblAux = new Label(colAux, rowAux, "PZAS", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 1, rowAux, "PESO", formato1);
        hojaCatalogo.addCell(lblAux);

        lblAux = new Label(colAux + 2, rowAux, "TOTAL", formato1);
        hojaCatalogo.addCell(lblAux);

        rowAux++;

        //detalle
        int cProducto = 1;
        jxl.write.Number numAux;
        for (ExistenciaAlmacenRepartidor detailData : data.getListaAlmacenRepartidor()){
            numAux = new jxl.write.Number(colAux, rowAux, detailData.getPiezas(), formato2);
            hojaCatalogo.addCell(numAux);

            numAux = new jxl.write.Number(colAux + 1, rowAux, detailData.getPeso(), formato2);
            hojaCatalogo.addCell(numAux);

            numAux = new jxl.write.Number(colAux + 2, rowAux, detailData.getTotalVendido(), formato2);
            hojaCatalogo.addCell(numAux);

            cProducto++;
            rowAux++;
        }
        
        //Totales
        numAux = new jxl.write.Number(colAux, rowAux, data.getTotalPiezas(), formato1);
        hojaCatalogo.addCell(numAux);

        numAux = new jxl.write.Number(colAux + 1 , rowAux, data.getTotalPeso(), formato1);
        hojaCatalogo.addCell(numAux);

        numAux = new jxl.write.Number(colAux + 2 , rowAux, data.getTotalTotalVendido(), formato1);
        hojaCatalogo.addCell(numAux);

        rowAux++;
        
        //Dato division de totalVendido / totalPeso
        double divTotal = 0;
        try{ 
            if (data.getTotalPeso()>0)
                divTotal = data.getTotalTotalVendido() / data.getTotalPeso(); 
        }catch(Exception ex){}
        hojaCatalogo.mergeCells(colAux, rowAux, (colAux + colsSubReport)-1, rowAux); //col1, row1, col2, row2
        //Formula frmCell = new Formula(colAux, rowAux, "", formato1);
        numAux = new jxl.write.Number(colAux, rowAux, divTotal, formato1);
        hojaCatalogo.addCell(numAux);
        rowAux++;
        
        return rowAux;
    }

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

    public List<ExistenciasGranelMarcaData> getListaExistenciasGranelMarcaData() {
        if (listaExistenciasGranelMarcaData==null)
            listaExistenciasGranelMarcaData = new ArrayList<ExistenciasGranelMarcaData>();
        return listaExistenciasGranelMarcaData;
    }

    
    public static class ExistenciasGranelMarcaData {
        private String nombreMarca =  "";
        
        private List<ExistenciaGranelProducto> listaExistenciaGranelProducto;

        public String getNombreMarca() {
            return nombreMarca;
        }

        public void setNombreMarca(String nombreMarca) {
            this.nombreMarca = nombreMarca;
        }

        public List<ExistenciaGranelProducto> getListaExistenciaGranelProducto() {
            if (listaExistenciaGranelProducto == null)
                listaExistenciaGranelProducto = new ArrayList<ExistenciaGranelProducto>();
            return listaExistenciaGranelProducto;
        }
        
    }
    
    public static class ExistenciaGranelProducto {
        
        private String nombreProducto = "";
        private int idProducto;
        private List<ExistenciaAlmacenRepartidor> listaAlmacenRepartidor;
        private double totalPiezas;
        private double totalPeso;
        private double totalTotalVendido;

        public String getNombreProducto() {
            return nombreProducto;
        }

        public void setNombreProducto(String nombreProducto) {
            this.nombreProducto = nombreProducto;
        }

        public int getIdProducto() {
            return idProducto;
        }

        public void setIdProducto(int idProducto) {
            this.idProducto = idProducto;
        }

        public List<ExistenciaAlmacenRepartidor> getListaAlmacenRepartidor() {
            if (listaAlmacenRepartidor==null)
                listaAlmacenRepartidor = new ArrayList<ExistenciaAlmacenRepartidor>();
            return listaAlmacenRepartidor;
        }

        public double getTotalPiezas() {
            return totalPiezas;
        }

        public void setTotalPiezas(double totalPiezas) {
            this.totalPiezas = totalPiezas;
        }

        public double getTotalPeso() {
            return totalPeso;
        }

        public void setTotalPeso(double totalPeso) {
            this.totalPeso = totalPeso;
        }

        public double getTotalTotalVendido() {
            return totalTotalVendido;
        }

        public void setTotalTotalVendido(double totalTotalVendido) {
            this.totalTotalVendido = totalTotalVendido;
        }
        
    }
    
    public static class ExistenciaAlmacenRepartidor {
        private double piezas;
        private double peso;
        private double totalVendido;

        public double getPiezas() {
            return piezas;
        }

        public void setPiezas(double piezas) {
            this.piezas = piezas;
        }

        public double getPeso() {
            return peso;
        }

        public void setPeso(double peso) {
            this.peso = peso;
        }

        public double getTotalVendido() {
            return totalVendido;
        }

        public void setTotalVendido(double totalVendido) {
            this.totalVendido = totalVendido;
        }
        
    }
    
}
