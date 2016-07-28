/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.report.xls;

import com.tsp.sct.bo.EmpresaBO;
import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sgfens.report.ReportBO;
import com.tsp.sgfens.report.ReportExportable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import jxl.*;
import jxl.write.*;
import jxl.write.Number;
import jxl.format.Colour;
import jxl.format.Alignment;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 17-dic-2012 
 */
public class ReportExportableXLS extends ReportExportable {

    public ReportExportableXLS() {
    }
    
    public ReportExportableXLS(Connection conn) {
        this.conn = conn;
    }

    /**
     * Devuelve el ByteArrayOutputStream generado acorde al tipo de reporte y parámetros recibidos
     * 
     * @param int report
     * @param String params
     * 
     * @return ByteArrayOutputStream  Contenido de archivo XLS (MS Excel)
     */
    public ByteArrayOutputStream generarReporte(int report, String params, String parametrosExtra , String infoTitle) throws IOException, WriteException, Exception{

        ReportBO repBO = new ReportBO(this.conn);
        repBO.setUsuarioBO(this.user);

        ArrayList<HashMap> dataList = null;
        ArrayList<HashMap> fieldList = null;
        ArrayList<BigDecimal> totalList = new ArrayList<BigDecimal>();
        
        if(this.dataList!=null)
            dataList = this.dataList;
        else
            dataList = repBO.getDataReport(report, params , parametrosExtra );
        
        if(this.fieldList!=null)
            fieldList = this.fieldList;
        else
            fieldList = repBO.getFieldList(report);

        ByteArrayOutputStream bos= new ByteArrayOutputStream();

        if(dataList.size() > 0){

            WritableWorkbook libro = Workbook.createWorkbook(bos);
            WritableFont times14font = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, true);
            times14font.setColour(Colour.WHITE);
            WritableFont times11font = new WritableFont(WritableFont.ARIAL, 9, WritableFont.NO_BOLD, true);
            times11font.setColour(Colour.BLACK);
            WritableCellFormat formatCabecera = new WritableCellFormat(times14font);
            formatCabecera.setBackground(Colour.GREY_80_PERCENT);
            formatCabecera.setAlignment(Alignment.LEFT);
            WritableCellFormat formatContent = new WritableCellFormat(times11font);
            formatContent.setAlignment(Alignment.LEFT);
            WritableCellFormat formatLogo = new WritableCellFormat(times14font);
            WritableSheet hojaCatalogo = libro.createSheet("Reporte", 0);
            CellView view = new CellView();
            view.setAutosize(true);
            boolean cabecera=true;
            
            /* CONTENIDO */

            /**
             * Logo
             */
            CellView vistaLogo = new CellView();
            vistaLogo.setSize(5);
            jxl.write.WritableImage imageLogo = new WritableImage(0, 0, 5, 5, this.fileImageLogo );
            hojaCatalogo.addImage(imageLogo);
            for(int i = 0; i < 5; i ++){
                for(int j = 0; j < 5; j ++){
                    Label logoCell = new Label(j, i, "_", formatLogo);
                    hojaCatalogo.addCell(logoCell);
                    hojaCatalogo.setColumnView(j, view);
                }   
            }

                
            /**
             * 
             */
            
            
            /*
             * CABECERA
             *
             * Por cada dato en la cabecera, se pinta una celda,
             *
             */
            int k=6;
            for(int i = 0; i < fieldList.size(); i ++){
                
                //Label header = new Label(i, 0, (String)fieldList.get(i).get("label"), formatCabecera);
                Label header = new Label(k, 6, (String)fieldList.get(i).get("label"), formatCabecera);
                hojaCatalogo.addCell(header);
                hojaCatalogo.setColumnView(k, view);
                
                totalList.add(BigDecimal.ZERO);
                
                k++;
            }

            /*
             * CUERPO
             *
             * Para cada grupo de datos obtenido
             * se va pintando el dato correspondiente a la cabecera
             */
            k=7;
            for(int i = 1; i <= dataList.size(); i ++){
                
                int m=6;
                //for(int j = 0; j < fieldList.size(); j ++){
                for(int j = 0; j < fieldList.size(); j ++){

                     Label contentTxt = null;
                     Number contentNum = null;
                     String valor = (String)dataList.get(i-1).get((String)fieldList.get(j).get("field"));
                     //contentTxt = new Label(m, k, (String)dataList.get(i-1).get((String)fieldList.get(j).get("field")), formatContent);
                     contentTxt = new Label(m, k, valor, formatContent);
                     
                     hojaCatalogo.addCell(contentTxt!=null?contentTxt:contentNum);
                     hojaCatalogo.setColumnView(m, view);

                     //Si el tipo de campo es Integer o Decimal, lo sumamos a la lista de totales
                    int tipoCampo = 0;
                    try{ tipoCampo = Integer.parseInt(fieldList.get(j).get("type").toString()); }catch(Exception ex){}
                    if (tipoCampo==ReportBO.DATA_INT || tipoCampo==ReportBO.DATA_DECIMAL){
                        if(valor!=null){
                            BigDecimal actual = totalList.get(j).add(new BigDecimal(valor));
                            totalList.set(j, actual);
                        }
                    }
                    
                     m++;
                }
                k++;
            }
            
            
            if (repBO.isTotalIntegerFields()
                        || repBO.isTotalDecimalFields()){
                /**
                 * TOTALES
                 * 
                 * Opcional, para campos Enteros y/o Decimales
                 */
                int i = 0;
                int m=6;
                for (HashMap fieldList1 : fieldList) {
                    int tipoCampo = 0;
                    try{ tipoCampo = Integer.parseInt(fieldList1.get("type").toString()); }catch(Exception ex){}

                    if (tipoCampo==ReportBO.DATA_INT && repBO.isTotalIntegerFields()){
                        Label contentTxt = new Label(m, k, "" + totalList.get(i).intValue(), formatCabecera);
                        hojaCatalogo.addCell(contentTxt);
                        hojaCatalogo.setColumnView(m, view);
                    }else if (tipoCampo==ReportBO.DATA_DECIMAL && repBO.isTotalDecimalFields()){
                        Label contentTxt = new Label(m, k, totalList.get(i).setScale(2, RoundingMode.HALF_UP).toPlainString(), formatCabecera);
                        hojaCatalogo.addCell(contentTxt);
                        hojaCatalogo.setColumnView(m, view);
                    }else{
                        Label contentTxt = new Label(m, k, "", formatCabecera);
                        hojaCatalogo.addCell(contentTxt);
                        hojaCatalogo.setColumnView(m, view);
                    }
                    m++;
                    i++;
                }
                k++;
            }
            
            /**
             * Titulo
             */
            
             //PARA OBTENER LA SUCURSAL O MATRIZ
            Empresa empresa = new Empresa();
            EmpresaBO empBO = new EmpresaBO(user.getUser().getIdEmpresa(), this.conn);
            empresa = empBO.getEmpresa();
        
            hojaCatalogo.mergeCells(6, 1, 8, 3);
            Label titleCell = new Label(6, 1, empresa.getNombreComercial() +" , "+this.getTitle(report), formatContent);
            hojaCatalogo.addCell(titleCell);
            
            //hojaCatalogo.mergeCells(7, 1, 8, 3);
            Label titleCellExtra = new Label(6, 4, infoTitle , formatContent);
            hojaCatalogo.addCell(titleCellExtra);
            
            libro.write();
            libro.close();
        }

        return bos;

    }
}
