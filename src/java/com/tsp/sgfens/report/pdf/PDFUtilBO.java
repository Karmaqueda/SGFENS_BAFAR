
package com.tsp.sgfens.report.pdf;

import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import java.awt.Color;

/**
 *
 * @author luis morales
 *
 * Utilidad para la generación de reportes en PDF
 */
public class PDFUtilBO {

    /**
     * Añade una nueva celda a la tabla especificada
     *
     * @param PdfPTable table - Tabla a la cual se añadirá la celda
     * @param Font font - Tipo de letra del texto
     * @param Image image - Imagen que contendrá la celda
     * @param String text - Texto que contrendrá la celda
     * @param boolean border[] - Bordes de la celda {IZQ,DER,ARR,ABA}
     * @param int hAlignment - Alineación horizontal
     * @param int vAlignment - Alineación vertical
     * @param float minHeight - Altura mínima
     * @param int [] padding - Espacio entre celdas {IZQ,DER,ARR,ABA}
     * @param int colspan - Columnas que abarcará la celda
     */
    public void addCell(PdfPTable table,Font font, Image image, String text, boolean border[], 
            int hAlignment, int vAlignment, float minHeight, int[] padding, int colspan){

        PdfPCell cell = new PdfPCell(new Phrase(text,font));
        if(image!=null)
            cell = new PdfPCell(image);
        if(border.length>0){
            int bLeft = 0;
            int bRight = 0;
            int bTop = 0;
            int bBottom = 0;

            boolean anyBorder = false;
            if(border[0]){
                bLeft = (PdfPCell.LEFT);
                anyBorder = true;
            }
            if(border.length>1 && border[1]){
                bRight = (PdfPCell.RIGHT);
                anyBorder = true;
            }
            if(border.length>2 && border[2]){
                bTop = (PdfPCell.TOP);
                anyBorder = true;
            }
            if(border.length>3 && border[3]){
                bBottom = (PdfPCell.BOTTOM);
                anyBorder = true;
            }
            if(!anyBorder)
                cell.setBorder(PdfPCell.NO_BORDER);
            else
                cell.setBorder(bLeft | bRight | bTop | bBottom);
        }else
            cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(hAlignment);
        cell.setVerticalAlignment(vAlignment);
        if(minHeight>0)
            cell.setMinimumHeight(minHeight);
        if(colspan>0)
            cell.setColspan(colspan);
        if(padding.length>0){
            cell.setPaddingLeft(padding[0]);
            if(padding.length>1){
                cell.setPaddingRight(padding[1]);
                if(padding.length>2){
                    cell.setPaddingTop(padding[2]);
                    if(padding.length>3)
                        cell.setPaddingBottom(padding[3]);
                }
            }
        }
        table.addCell(cell);

    }

    /**
     * Añade una tabla a una tabla existente
     *
     * @param PdfPTable mainTable - Tabla a la cual se añadirá la celda
     * @param PdfPTable tableToSet - Tabla a añadir
     * @param boolean border[] - Bordes de la celda {IZQ,DER,ARR,ABA}
     * @param int hAlignment - Alineación horizontal
     * @param int vAlignment - Alineación vertical
     * @param float minHeight - Altura mínima
     * @param int [] padding - Espacio entre celdas {IZQ,DER,ARR,ABA}
     * @param int colspan - Columnas que abarcará la tabla
     */
    public void addTable(PdfPTable mainTable, PdfPTable tableToSet, boolean border[], 
            int hAlignment, int vAlignment, float minHeight, int[] padding,int colspan){

        PdfPCell cell = new PdfPCell(tableToSet);
        if(border.length>0){
            int bLeft = 0;
            int bRight = 0;
            int bTop = 0;
            int bBottom = 0;

            boolean anyBorder = false;
            if(border[0]){
                bLeft = (PdfPCell.LEFT);
                anyBorder = true;
            }
            if(border.length>1 && border[1]){
                bRight = (PdfPCell.RIGHT);
                anyBorder = true;
            }
            if(border.length>2 && border[2]){
                bTop = (PdfPCell.TOP);
                anyBorder = true;
            }
            if(border.length>3 && border[3]){
                bBottom = (PdfPCell.BOTTOM);
                anyBorder = true;
            }
            if(!anyBorder)
                cell.setBorder(PdfPCell.NO_BORDER);
            else
                cell.setBorder(bLeft | bRight | bTop | bBottom);
        }else
            cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(hAlignment);
        cell.setVerticalAlignment(vAlignment);
        if(minHeight>0)
            cell.setMinimumHeight(minHeight);
        if(colspan>0)
            cell.setColspan(colspan);
        if(padding.length>0){
            cell.setPaddingLeft(padding[0]);
            if(padding.length>1){
                cell.setPaddingRight(padding[1]);
                if(padding.length>2){
                    cell.setPaddingTop(padding[2]);
                    if(padding.length>3)
                        cell.setPaddingBottom(padding[3]);
                }
            }
        }
        mainTable.addCell(cell);
        
    }
    
    public void addImageCell(PdfPTable table, Image image, boolean border[], int hAlignment, int vAlignment, int[] padding, int colspan){
        PdfPCell cell = new PdfPCell(new Phrase(" "));
        if(image!=null)
            cell = new PdfPCell(image);
        if(border.length>0){
            int bLeft = 0;
            int bRight = 0;
            int bTop = 0;
            int bBottom = 0;

            boolean anyBorder = false;
            if(border[0]){
                bLeft = (PdfPCell.LEFT);
                anyBorder = true;
            }
            if(border.length>1 && border[1]){
                bRight = (PdfPCell.RIGHT);
                anyBorder = true;
            }
            if(border.length>2 && border[2]){
                bTop = (PdfPCell.TOP);
                anyBorder = true;
            }
            if(border.length>3 && border[3]){
                bBottom = (PdfPCell.BOTTOM);
                anyBorder = true;
            }
            if(!anyBorder)
                cell.setBorder(PdfPCell.NO_BORDER);
            else
                cell.setBorder(bLeft | bRight | bTop | bBottom);
        }else
            cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(hAlignment);
        cell.setVerticalAlignment(vAlignment);
        if(colspan>0)
            cell.setColspan(colspan);
        if(padding.length>0){
            cell.setPaddingLeft(padding[0]);
            if(padding.length>1){
                cell.setPaddingRight(padding[1]);
                if(padding.length>2){
                    cell.setPaddingTop(padding[2]);
                    if(padding.length>3)
                        cell.setPaddingBottom(padding[3]);
                }
            }
        }
        table.addCell(cell);
    }
    
    public void agregaCelda(PdfPTable table,Font font, Color backgroundColor, String text, boolean border[], int hAlignment, int vAlignment, float minHeight, int[] padding, int colspan){
        PdfPCell cell = new PdfPCell(new Phrase(text,font));
        if(border.length>0){
            int bLeft = 0;
            int bRight = 0;
            int bTop = 0;
            int bBottom = 0;

            boolean anyBorder = false;
            if(border[0]){
                bLeft = (PdfPCell.LEFT);
                anyBorder = true;
            }
            if(border.length>1 && border[1]){
                bRight = (PdfPCell.RIGHT);
                anyBorder = true;
            }
            if(border.length>2 && border[2]){
                bTop = (PdfPCell.TOP);
                anyBorder = true;
            }
            if(border.length>3 && border[3]){
                bBottom = (PdfPCell.BOTTOM);
                anyBorder = true;
            }
            if(!anyBorder)
                cell.setBorder(PdfPCell.NO_BORDER);
            else
                cell.setBorder(bLeft | bRight | bTop | bBottom);
        }else
            cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(hAlignment);
        cell.setVerticalAlignment(vAlignment);
        if(minHeight>0)
            cell.setMinimumHeight(minHeight);
        if(colspan>0)
            cell.setColspan(colspan);
        if(padding.length>0){
            cell.setPaddingLeft(padding[0]);
            if(padding.length>1){
                cell.setPaddingRight(padding[1]);
                if(padding.length>2){
                    cell.setPaddingTop(padding[2]);
                    if(padding.length>3)
                        cell.setPaddingBottom(padding[3]);
                }
            }
        }
        if (backgroundColor!=null)
            cell.setBackgroundColor(backgroundColor);
        table.addCell(cell);
    }

}
