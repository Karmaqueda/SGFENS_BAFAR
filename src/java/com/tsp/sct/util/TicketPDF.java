/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//REFERENCIA : http://hubertorodriguez.blogspot.mx/2012/09/creacion-de-documentos-pdf-con-itext-en.html

package com.tsp.sct.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.tsp.sct.bo.BancoOperacionBO;
import com.tsp.sct.bo.EmpresaBO;
import com.tsp.sct.bo.UbicacionBO;
import com.tsp.sct.config.Configuration;
import com.tsp.sct.dao.dto.BancoOperacion;
import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.dao.dto.SgfensCobranzaAbono;
import com.tsp.sct.dao.dto.Ubicacion;
import com.tsp.sct.dao.jdbc.SgfensCobranzaAbonoDaoImpl;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.Date;

/**
 *
 * @author Leonardo
 */
public class TicketPDF {
    private Connection conn = null;
    public String armaPDF(int idEmpresa, long idOperacionBancaria){
        
            BancoOperacionBO bancoOperacionBO = new BancoOperacionBO(idOperacionBancaria, this.conn);
            BancoOperacion bancoOperacionDto = bancoOperacionBO.getBancoOperacion();   

            EmpresaBO empresaBO = new EmpresaBO(idEmpresa,this.conn);
            Empresa empresa = empresaBO.getEmpresa();

            UbicacionBO ubicacionBO = new UbicacionBO(empresa.getIdUbicacionFiscal(),this.conn);
            Ubicacion ubicacion = ubicacionBO.getUbicacion();
            Configuration appConfig = new Configuration();
            String archivoPDF  = "";
            
            //GENERAMOS AL PDF
            try{
                // Se crea el OutputStream para el fichero donde queremos dejar el pdf.
                //archivoPDF = appConfig.getApp_content_path()+bancoOperacionDto.getBancoOrderId()+(new Date().getTime())+".pdf";
                archivoPDF = appConfig.getApp_content_path()
                        + empresa.getRfc() + "/abonos/"
                        +bancoOperacionDto.getBancoOrderId()+(new Date().getTime())+".pdf";
                FileOutputStream archivo = new FileOutputStream(archivoPDF);
                // Se crea el documento
                //Document documento = new Document();            
                Rectangle pagesize = new Rectangle(249.5f, 595.28f);//(216f, 720f);
                Document documento = new Document(pagesize, 20f, 20f, 20f, 20f);
                // Se asocia el documento al OutputStream y se indica que el espaciado entre
                // lineas sera de 20. Esta llamada debe hacerse antes de abrir el documento
                PdfWriter.getInstance(documento,archivo).setInitialLeading(20);            
                // Se abre el documento.
                documento.open();
                documento.add(alineaCentro("www.movilpyme.com"));                
                //documento.add(alineaCentro("VENTA\n"));
                //documento.add(alineaCentro("BANORTE ("+ appConfig.getNumAfiliacionBanco()+ ")"));
                documento.add(alineaCentro(empresa.getNombreComercial()));
                                
                documento.add(alineaCentro("El cargo en su estado de cuenta aparecerá como:"));
                documento.add(alineaCentro("Venta"));
                documento.add(alineaCentroNegrita("OPERACIONES EN LÍNEA"));
                documento.add(alineaCentroNegrita("7274483"));
                
                if(ubicacion.getNumInt()!=null){
                    if(!ubicacion.getNumInt().equals("")){
                        documento.add(alineaCentro(ubicacion.getCalle()+" "+ubicacion.getNumExt()+", INT. "+ubicacion.getNumInt()));                    
                    }else{
                        documento.add(alineaCentro(ubicacion.getCalle()+" "+ubicacion.getNumExt()));                    
                    }
                }else{
                    documento.add(alineaCentro(ubicacion.getCalle()+" "+ubicacion.getNumExt()));                    
                }
                documento.add(alineaCentro("COL. "+ubicacion.getColonia()+" , "+ubicacion.getMunicipio()));
                documento.add(alineaCentro(ubicacion.getPais()+", "+ubicacion.getEstado()+", CP. "+ubicacion.getCodigoPostal()));
                documento.add(alineaCentro("-COPIA-"));
                
                documento.add(alineaCentro("\n"));//salto de linea
                documento.add(alineaCentroNegrita(bancoOperacionDto.getBancoOperFecha()));
                documento.add(alineaCentro("\n"));//salto de linea
                if(bancoOperacionDto.getIdEstatus()==1){
                    documento.add(alineaCentroNegrita("APROBADA  " + bancoOperacionDto.getBancoAuth()));
                }else{
                    documento.add(alineaCentroNegrita("CANCELACION APROBADA  " + bancoOperacionDto.getBancoAuth()));
                }
                documento.add(alineaCentro("\n"));//salto de linea
                
                documento.add(normal("Número de Tarjeta:\t************"+bancoOperacionDto.getNoTarjeta().substring(12) ));
                documento.add(negrita("Importe:\t\t$"+bancoOperacionDto.getMonto()));
                documento.add(normal("ARQC:\t\t"+ StringManage.getValidString(bancoOperacionDto.getDataArqc()) ));
                documento.add(normal("AID:\t\t"+StringManage.getValidString(bancoOperacionDto.getDataAid())));
                documento.add(normal("TSI:\t\t"+StringManage.getValidString(bancoOperacionDto.getDataTsi())));
                documento.add(normal("REF:\t\t"+StringManage.getValidString(bancoOperacionDto.getDataRef())));
                
                
                /*
                documento.add(normal("NUMERO DE TARJETA"));
                documento.add(normal("XXXX XXXX XXXX "+ bancoOperacionDto.getNoTarjeta().substring(12)));
                if(bancoOperacionDto.getBancoOperIssuingBank()!=null && bancoOperacionDto.getBancoOperType()!= null){
                    documento.add(normal(bancoOperacionDto.getBancoOperIssuingBank()+", "+bancoOperacionDto.getBancoOperType()));
                }
                if(bancoOperacionDto.getIdEstatus()==1){
                    documento.add(alineaCentroNegrita("APROBADA"));
                }else{
                    documento.add(alineaCentroNegrita("CANCELACION APROBADA"));
                }
                
                
                documento.add(negrita("AUT. "+bancoOperacionDto.getBancoAuth()));
                documento.add(negrita("IMPORTE $"+bancoOperacionDto.getMonto()));
                documento.add(normal("Fecha: "+bancoOperacionDto.getBancoOperFecha()));
                */
                
                SgfensCobranzaAbono[] abonos = new SgfensCobranzaAbono[0];
                SgfensCobranzaAbono abono = null;
                SgfensCobranzaAbonoDaoImpl abonoDaoImpl = new SgfensCobranzaAbonoDaoImpl();
                File fileImagen = null;
                try{
                    abonos = abonoDaoImpl.findWhereIdOperacionBancariaEquals(bancoOperacionDto.getIdOperacionBancaria());
                    System.out.println("*******TAMAÑO DE LA LIST: "+abonos.length);
                    if(abonos != null){           
                        if(abonos.length > 0){            
                        abono = new SgfensCobranzaAbono();
                        abono = abonos[0]; 
                        /*String archivoImagen= ubicacionImagenesProspectos+abono.getNombreArchivoImgFirma();
                        fileImagen = new File(archivoImagen);
                        System.out.println("-------------------------RUTA ARCHIVO: "+fileImagen.getPath()+fileImagen.getName());
                        System.out.println("-------------------------RUTA ARCHIVO: "+fileImagen.getPath());
                        */
                    }}
                }catch(Exception e){e.printStackTrace();}
                if(abono != null){
                    if(abono.getNombreArchivoImgFirma() != null){
                        documento.add(alineaCentro("Firma:"));
                        String ubicacionImagenesProspectos = appConfig.getApp_content_path() + empresa.getRfc() +"/abonos/images/"+abono.getNombreArchivoImgFirma();
                        Image foto = Image.getInstance(ubicacionImagenesProspectos);
                        foto.scaleToFit(100, 100);
                        foto.setAlignment(Chunk.ALIGN_MIDDLE);
                        documento.add(foto);
                        documento.add(alineaCentro(bancoOperacionDto.getNombreTitular()));
                    }else{
                        documento.add(alineaCentro("Firma: _________________________"));
                        documento.add(alineaCentro(bancoOperacionDto.getNombreTitular()));                        
                    }
                }else{
                    documento.add(alineaCentro("Firma: _________________________"));
                    documento.add(alineaCentro(bancoOperacionDto.getNombreTitular()));                        
                }
                documento.add(new Paragraph(" "));  
                documento.add(normal5("Por este pagaré me obligo incondicionalmente a pagar a la orden del banco emisor el importe total de este título en los términos de contrato suscrito para el uso de esta tarjeta de crédito. En el caso de operaciones con tarjeta de débito, expresamente reconozco y acepto que este recibo es el comprobante de la operación realizada, misma que me consigna en el presente pagare y tendrá pleno valor probatorio y fuerza legal, en virtud de lo que firme personalmente y/o digite mi número de identificación personal como firma electrónica, el cual es exclusivo de mi responsabilidad manifestando plena conformidad al respecto. El presente pagaré es negociable únicamente con instituciones bancarias."));
                
                documento.close();
            }catch(Exception e){e.printStackTrace();}                                                  
        return archivoPDF;        
    }
    
    public Paragraph alineaCentro(String dato){
        Paragraph parrafo = new Paragraph(dato, FontFactory.getFont("Verdana", 7));
        parrafo.setAlignment(Paragraph.ALIGN_CENTER);//el 1 es para centrar
        return parrafo;
    }
    
    public Paragraph alineaCentroNegrita(String dato){
        Paragraph parrafo = new Paragraph(dato, FontFactory.getFont("Verdana", 7, Font.BOLD, BaseColor.BLACK));
        parrafo.setAlignment(1);//el 1 es para centrar
        return parrafo;
    }
    
    public Paragraph negrita(String dato){
        Paragraph parrafo = new Paragraph(dato, FontFactory.getFont("Verdana", 7, Font.BOLD, BaseColor.BLACK));        
        return parrafo;
    }
    
    public Paragraph normal(String dato){
        Paragraph parrafo = new Paragraph(dato, FontFactory.getFont("Verdana", 7));        
        return parrafo;
    }
    
    public Paragraph normal5(String dato){
        Paragraph parrafo = new Paragraph(dato, FontFactory.getFont("Verdana", 5));   
        parrafo.setAlignment(Paragraph.ALIGN_JUSTIFIED);
        return parrafo;
    }
    
}
