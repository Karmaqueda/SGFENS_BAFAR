/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.cfdi;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.tsp.interconecta.ws.WsGenericResp;
import com.tsp.sct.bo.InvocaWsNetEnvia;
import com.tsp.sct.config.Configuration;
import com.tsp.sct.dao.dto.Cliente;
import com.tsp.sct.dao.dto.ComprobanteFiscal;
import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.pdf.CFDI32toPDF;
import com.tsp.sct.pdf.PdfITextUtil;
import com.tsp.sct.pdf.Translator;
import com.tsp.sct.pdf.cfd.util.CfdTransform;
import com.tsp.sct.pdf.cfd.util.Cfdi32ToPdfEventExt;
import com.tsp.sct.util.FileManage;
import com.tsp.sct.util.StringManage;
import java.awt.Color;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.bigdata.sat.cfdi.CFDv32;
import mx.bigdata.sat.cfdi.TFDv1_v32;
import mx.bigdata.sat.cfdi.schema.v32.*;
import mx.bigdata.sat.complementos.schema.implocal.ImpuestosLocales;
import mx.bigdata.sat.security.KeyLoader;

/**
 *
 * @author ISC César Ulises Martínez García
 */
public class Cfd32BO {

    Comprobante comprobanteFiscal = null;
    CFDv32 cfd = null;
    TimbreFiscalDigital timbreFiscalDigital =null;
    TFDv1_v32 tFDv1 =null;
    String validaErrorMsg="";
    
    ComprobanteFiscal comprobanteFiscalDto = null;
    
    Configuration configuration = new Configuration();

    public Cfd32BO() {
    }
    
    public Cfd32BO(Comprobante comprobanteFiscal) throws Exception {
        this.comprobanteFiscal = comprobanteFiscal;
        this.cfd = new CFDv32(comprobanteFiscal, "mx.bigdata.sat.complementos.schema.nomina", "mx.bigdata.sat.complementos.schema.implocal");
    }
    
    public void sellarComprobante(String rutaCerEmisor, String rutaKeyEmisor, String passwordKeyEmisor) throws Exception {

        PrivateKey key = KeyLoader.loadPKCS8PrivateKey(new FileInputStream(rutaKeyEmisor),
                passwordKeyEmisor);
        X509Certificate cert = KeyLoader.loadX509Certificate(new FileInputStream(rutaCerEmisor));
        this.comprobanteFiscal = this.cfd.sellarComprobante(key, cert);
        this.cfd.validar();
        this.cfd.verificar();

        //For DEBUG: Damos salida al contenido sellado por consola
        //this.cFDv3.guardar(System.out);
    }
    
    /**
     * Método que genera el timbre Fiscal digital usando el certificado y llave privada
     * del PAC
     * @throws FileNotFoundException
     * @throws Exception
     */
    public void timbrarComprobante() throws Exception {
//Editado 02/10/2013, conexion a CMM
// no se timbra con certificados fisicos en local
        /*
         * //Proceso de timbrado con Certificado físico en Local
        String rutaCerPAC = configuration.getPacRutaCertificado();
        String rutaCerKey = configuration.getPacRutaLlavePrivada();
        String passwordKeyPAC = configuration.getPacPasswordLlavePrivada();

        java.security.PrivateKey keyPAC = null;
        X509Certificate certPAC = null;
        try {
            keyPAC = KeyLoader.loadPKCS8PrivateKey(new FileInputStream(rutaCerKey), passwordKeyPAC);
            certPAC = KeyLoader.loadX509Certificate(new FileInputStream(rutaCerPAC));
        } catch (Exception e) {
            throw new Exception("No se pudo recuperar el certificado del PAC para timbrar el Comprobante.");
        }

        TFDv1_v32 tfd = new TFDv1_v32(this.cfd, certPAC); // Crea un TDF a partir del CFDI
        tfd.timbrar(keyPAC); // Timbra el CDF
        tfd.verificar(); // Verifica el TDF
        this.tFDv1 = tfd;

        //For DEBUG: Damos salida al contenido timbrado por consola
        //tFDv1.guardar(System.out);
        this.timbreFiscalDigital = tfd.getTimbre();
        */

//Agregado 02/10/2013, conexion a CMM
        timbrarComprobanteCMM();
        
    }
    
    /**
     * Timbra el comprobante Actual del objeto
     * mediante el web service CMM
     * @throws Exception Excepción en caso de error en el servicio de timbrado
     */
    private void timbrarComprobanteCMM() throws Exception{
        byte[] XMLBytesContent = null; 
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            this.cfd.guardar(baos);
            XMLBytesContent = baos.toByteArray();
            
                baos.flush();
                baos.close();
        }catch(Exception ex){
            throw new Exception("No se pudo obtener el contenido del XML para enviar a servicio de timbrado: " + ex.toString());
        }
        
        InvocaWsNetEnvia invocaEnvioCMM = new InvocaWsNetEnvia();
        //registramos contribuyente previamente
        invocaEnvioCMM.otorgarAccesoContribuyente(
                configuration.getPac_ws_timbrado_user(), //usuario de CMM
                configuration.getPac_ws_timbrado_pass(), //password de CMM
                StringManage.getValidString(this.comprobanteFiscal.getEmisor().getRfc()),  //RFC emisor
                StringManage.getValidString(this.comprobanteFiscal.getEmisor().getNombre())); //Razon social emisor
        
        //enviamos a timbrar
        WsGenericResp resp = null;
        try{
            resp = invocaEnvioCMM.enviarWS(
                configuration.getPac_ws_timbrado_user(),             //usuario de CMM
                configuration.getPac_ws_timbrado_pass(),             //password de CMM
                null,           
                null, 
                "",
                XMLBytesContent);
        }catch(Exception ex){
            throw new Exception("Error al invocar servicio de timbrado: " + ex.toString());
        }
        
        if (resp!=null){
            if (resp.isIsError()){
                //Si ocurrio un error
                throw new Exception("Error al intentar timbrado. " + 
                        resp.getNumError() + " : " + resp.getErrorMessage());
            }
            
            //Si todo esta correcto obtenemos archivo timbrado
            ByteArrayInputStream bin = new ByteArrayInputStream(resp.getXML());
            Cfd32BO timbradoTemp = new Cfd32BO(bin);
            this.tFDv1 = timbradoTemp.gettFDv1();
            this.timbreFiscalDigital = timbradoTemp.getTimbreFiscalDigital();
            bin.close();
        }
    }
    
    /**
     * Almacena el archivo XML a la ruta correspondiente segun Empresa, Cliente(receptor) y 
     * la carpeta que corresponda al tipo de comprobante generado.
     * @param empresaDto Empresa generadora de la factura (emisor)
     * @param clienteDto Cliente receptor
     * @param nombreCarpetaTipoComprobante Nombre de la carpeta generica donde se depositara por tipo de comprobante.
     *                                  Para factura p. ejem: "facturas"
     * @return File objeto java file con el contenido del archivo.
     * @throws FileNotFoundException
     * @throws Exception 
     */
    public File guardarComprobante(Empresa empresaDto, Cliente clienteDto, String nombreCarpetaTipoComprobante) throws FileNotFoundException, Exception {
        //byte[] xmlTimbradoStr;

        String rutaRepositorio = configuration.getApp_content_path() + empresaDto.getRfc() + "/cfd/emitidos/"+nombreCarpetaTipoComprobante+"/" + clienteDto.getRfcCliente();
        String nombreArchivo = "CFD_" + empresaDto.getRfc() + "_" + (new Date()).getTime() + ".xml";

        //Nos aseguramos que el Folder Exista
        File carpetaRepositorio = new File(rutaRepositorio);
        carpetaRepositorio.mkdirs();

        //Volcamos el contenido del CFDI en un archivo XML
        File archivoComprobante = new File(rutaRepositorio + "/" + nombreArchivo);
        FileOutputStream fos = new FileOutputStream(archivoComprobante);
        fos.flush();

        tFDv1.guardar(fos);
        
        fos.close();

        //Obtenemos en forma de String el contenido del Archivo CFDI
        //xmlTimbradoStr = FileManage.getBytesFromFile(archivoComprobante);

        return archivoComprobante;
    }
    
    public File guardarRepresentacionImpresa(Empresa empresaDto, Cliente clienteDto, String nombreCarpetaTipoComprobante) throws FileNotFoundException, Exception {
        
        File  archivoRepresentacionImpresa = null;
                
        String rutaRepositorio = configuration.getApp_content_path() + empresaDto.getRfc() + "/cfd/emitidos/"+nombreCarpetaTipoComprobante+"/" + clienteDto.getRfcCliente();
        String nombreArchivo = "CFD_" + empresaDto.getRfc() + "_" + (new Date()).getTime() + ".pdf";

        //Nos aseguramos que el Folder Exista
        File carpetaRepositorio = new File(rutaRepositorio);
        carpetaRepositorio.mkdirs();

        //Guardamos archivo PDF
        archivoRepresentacionImpresa = new File(rutaRepositorio + "/" + nombreArchivo);
        FileOutputStream fos = new FileOutputStream(archivoRepresentacionImpresa);

        this.toPdf().writeTo(fos);
        
        fos.flush();
        fos.close();

        return archivoRepresentacionImpresa;
    }
    
    public void copiarCFDIenvioAsincrono(File archivoXMLComprobante) throws Exception {
//Editado 02/10/2013, conexion a CMM
//no se copia a repositorio de Envio Asincrono
        /*
        try {
            boolean exitoCopiaArchivo = false;
            String rutaArchivoOrigen = archivoXMLComprobante.getAbsolutePath();
            String rutaArchivoDestino = configuration.getRutaRepositorio_EnvioCFDI32() + "/Pretoriano_" + archivoXMLComprobante.getName();
            System.out.println("Origen:  " + rutaArchivoOrigen + "\n Destino:  " + rutaArchivoDestino);
            while (!exitoCopiaArchivo) {
                exitoCopiaArchivo = FileManage.copyFile(rutaArchivoOrigen, rutaArchivoDestino);
            }
        } catch (Exception e) {
            throw new Exception("No se pudo almacenar el Comprobante Fiscal para su presentación al SAT. " + e.getMessage());
        }
        */
    }

    public Cfd32BO(File file) throws Exception{
            Comprobante comp = CFDv32.newComprobante(new FileInputStream(file));
            CFDv32 cfd = new CFDv32(comp, "mx.bigdata.sat.complementos.schema.nomina", "mx.bigdata.sat.complementos.schema.implocal");
            cfd.validar(); // Valida el XML, que todos los elementos estén presentes
            cfd.verificar(); // Verifica un CFD ya firmado

            //Asginamos a los valores locales
            this.comprobanteFiscal = comp;
            this.cfd = cfd;
            //Extraemos el timbreFiscal Digital
            this.timbreFiscalDigital = extractTFD(comp);
            
            try{
                this.tFDv1 = new TFDv1_v32(cfd, timbreFiscalDigital, null);
            }catch(Exception ex){
                ex.printStackTrace();
            }
    }
    
    public Cfd32BO(InputStream inputStream) throws Exception{
            Comprobante comp = CFDv32.newComprobante(inputStream);
            CFDv32 cfd = new CFDv32(comp, "mx.bigdata.sat.complementos.schema.nomina", "mx.bigdata.sat.complementos.schema.implocal");
            cfd.validar(); // Valida el XML, que todos los elementos estén presentes
            cfd.verificar(); // Verifica un CFD ya firmado

            //Asginamos a los valores locales
            this.comprobanteFiscal = comp;
            this.cfd = cfd;
            //Extraemos el timbreFiscal Digital
            this.timbreFiscalDigital = extractTFD(comp);
            
            try{
                this.tFDv1 = new TFDv1_v32(cfd, timbreFiscalDigital, null);
            }catch(Exception ex){
                ex.printStackTrace();
            }
    }

    /**
     * Valida de forma extenuante el comprobante, tanto la estructura,
     * los datos y se verifica el firmado del CFDI
     * @param file
     * @return
     */
    public boolean validarCFD(File file){
        try {
            Comprobante comp = CFDv32.newComprobante(new FileInputStream(file));
            CFDv32 cfd = new CFDv32(comp, "mx.bigdata.sat.complementos.schema.nomina", "mx.bigdata.sat.complementos.schema.implocal");
            //CFDv3 cfd = new CFDv3(new FileInputStream(file)); // Crea un CFD a partir de un InputStream
            //Key key = KeyLoader.loadPKCS8PrivateKey(new FileInputStream(keyfile),  password);
            //Certificate cert = KeyLoader.loadX509Certificate(new FileInputStream(certFile));
            //Comprobante sellado = cfd.sellarComprobante(key, cert); // Firma el CFD y obtiene un Comprobante sellado
            cfd.validar(); // Valida el XML, que todos los elementos estén presentes
            cfd.verificar(); // Verifica un CFD ya firmado

            //Asignamos a las variables locales
            this.cfd = cfd;
            this.comprobanteFiscal = comp;
            
            this.timbreFiscalDigital = extractTFD(comp);
            if (this.timbreFiscalDigital==null)
                throw new Exception("El comprobante no contiene un Timbre Fiscal Digital de PAC");
        } catch(Exception e){
            validaErrorMsg = e.getMessage();
            return false;
        }
        
        return true;
    }

    /**
     * Método para extraer el TimbreFiscalDigital de un Comprobante version CFDI 3.2
     * @param comp Comprobante
     * @return TimbreFiscalDigital obtenido del Comprobante
     */
    public TimbreFiscalDigital extractTFD(Comprobante comp){
        TimbreFiscalDigital tf = null;
        //Datos de TimbreFiscalDigital (Complemento CFDv3)
        if (comp.getComplemento()!=null){
            for (Object itemComplemento : comp.getComplemento().getAny()){
                try{
                    //Intentamos convertir el objeto que se itera a Timbre Fiscal
                    tf = (TimbreFiscalDigital) itemComplemento;
                    /* Una vez que obtuvimos todos los datos necesarios del
                       timbre fiscal terminamos el ciclo */
                    break;
                }catch(Exception e){}
            }
        }
        return tf;
    }

    /**
     * Método para crear el archivo Pdf a partir del comprobante CFDI actual
     * @return ByteArrayOutputStream con el contenido del PDF
     * @throws Exception
     */
    public ByteArrayOutputStream toPdf() throws Exception{
        return toPdf(comprobanteFiscal);
    }

    /**
     * Método para crear un archivo PDF a partir de un nuevo comprobante CFDI
     * @param comprobanteCFDI El Objeto Comprobante CFDI a transformar
     * @return ByteArrayOutputStream con el contenido del PDF
     * @throws Exception
     */
    public ByteArrayOutputStream toPdf(Comprobante comprobanteCFDI) throws Exception{

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
            
           //Declaracion de objetos Complementos
            ImpuestosLocales implocal = null;
            //...otras declaraciones de complementos aquí
            
            //Recuperamos complementos
            Comprobante.Complemento comp = comprobanteCFDI.getComplemento();
            if (comp != null) {
              java.util.List<Object> listComplementos = comp.getAny();
              for (Object o : listComplementos) {
                
                //Impuestos Locales
                if (o instanceof ImpuestosLocales) {
                  implocal = (ImpuestosLocales) o;
                }
                
                //...otros complementos aquí
                
              }
            }

            
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            Cfdi32ToPdfEventExt event = new Cfdi32ToPdfEventExt(document, comprobanteCFDI, LETRAOCHO, LETRAOCHO_BOLD,timbreFiscalDigital,null, comprobanteFiscalDto);
            writer.setPageEvent(event);
            document.setMargins(10, 10, 150, 80);
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

                        obj.agregaCelda(tCliente,LETRAOCHO_BOLD,Color.lightGray, "Cliente", new boolean[]{false,false,false,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],2);

                        tAux = new PdfPTable(1);
                        tAux.setTotalWidth(275);
                        tAux.setLockedWidth(true);

                            obj.agregaCelda(tAux,LETRAOCHO, "Nombre o razón social: " + (comprobanteCFDI.getReceptor().getNombre()!=null?comprobanteCFDI.getReceptor().getNombre():""), new boolean[]{false,true,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                            obj.agregaCelda(tAux,LETRAOCHO, "R.F.C. del cliente: " + comprobanteCFDI.getReceptor().getRfc(), new boolean[]{false,true,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);

                        obj.agregaTabla(tCliente, tAux, new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);

                        obj.agregaCelda(tCliente,LETRAOCHO, ""+
                                "DOMICILIO: Calle: " + (comprobanteCFDI.getReceptor().getDomicilio().getCalle()!=null?comprobanteCFDI.getReceptor().getDomicilio().getCalle():"") + " " +
                                (comprobanteCFDI.getReceptor().getDomicilio().getNoExterior()!=null?comprobanteCFDI.getReceptor().getDomicilio().getNoExterior():"") + " " + (comprobanteCFDI.getReceptor().getDomicilio().getNoInterior()!=null?comprobanteCFDI.getReceptor().getDomicilio().getNoInterior():"") +
                                " Col: " + (comprobanteCFDI.getReceptor().getDomicilio().getColonia()!=null?comprobanteCFDI.getReceptor().getDomicilio().getColonia():"") +
                                " \nDeleg./Municipio: " + (comprobanteCFDI.getReceptor().getDomicilio().getMunicipio()!=null?comprobanteCFDI.getReceptor().getDomicilio().getMunicipio():"") + " Entidad: " + (comprobanteCFDI.getReceptor().getDomicilio().getEstado()!=null?comprobanteCFDI.getReceptor().getDomicilio().getEstado():"") +
                                " C.P. " + comprobanteCFDI.getReceptor().getDomicilio().getCodigoPostal(), new boolean[]{false,true,false,true}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);

                    obj.agregaTabla(tabla, tCliente, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);

                    //SALTO DE LÍNEA
                    obj.agregaCelda(tabla,LETRAOCHO, " ", new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);

                    PdfPTable tConceptos = new PdfPTable(6);
                    tConceptos.setTotalWidth(550);
                    tConceptos.setLockedWidth(true);

                        obj.agregaCelda(tConceptos,LETRASIETE_BOLD,Color.lightGray, "IDENTIFICADOR", new boolean[]{false,true,false,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tConceptos,LETRASIETE_BOLD, Color.lightGray, "DESCRIPCIÓN", new boolean[]{false,true,false,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tConceptos,LETRASIETE_BOLD, Color.lightGray,"CANTIDAD", new boolean[]{false,true,false,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tConceptos,LETRASIETE_BOLD, Color.lightGray, "UNIDAD", new boolean[]{false,true,false,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tConceptos,LETRASIETE_BOLD, Color.lightGray,"PRECIO UNITARIO", new boolean[]{false,true,false,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tConceptos,LETRASIETE_BOLD, Color.lightGray,"IMPORTE", new boolean[]{false,false,false,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);

                        Iterator<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Conceptos.Concepto> cfdiConceptoItera = comprobanteCFDI.getConceptos().getConcepto().iterator();

                        while(cfdiConceptoItera.hasNext()){

                            mx.bigdata.sat.cfdi.schema.v32.Comprobante.Conceptos.Concepto conceptoCFDI = cfdiConceptoItera.next();

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

                        obj.agregaCelda(tTotal,LETRAOCHO_BOLD,Color.lightGray, "TOTAL CON LETRA", new boolean[]{false,true,false,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);
                        obj.agregaCelda(tTotal,LETRAOCHO_BOLD,Color.lightGray, "TOTAL", new boolean[]{true,false,false,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);

                        Translator numALetra = new Translator();
                        String totalConLetra = "";
                        try{
                            if (comprobanteCFDI.getMoneda()!=null){
                                if (!"".equals(comprobanteCFDI.getMoneda().trim())){
                                    numALetra.setNombreMoneda(comprobanteCFDI.getMoneda().trim());
                                }
                            }
                            //totalConLetra = numALetra.getStringOfNumber(comprobanteCFDI.getTotal().floatValue());
                            totalConLetra = numALetra.getStringOfNumber(comprobanteCFDI.getTotal());
                        }catch(Exception e){}

                        obj.agregaCelda(tTotal,LETRAOCHO, "" + totalConLetra, new boolean[]{false,true,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                        tAux = new PdfPTable(2);
                        tAux.setTotalWidth(150);

                            obj.agregaCelda(tAux,LETRASIETE_BOLD, "SUBTOTAL" , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                            obj.agregaCelda(tAux,LETRAOCHO, "" + new DecimalFormat("$###,###,###,##0.00").format(comprobanteCFDI.getSubTotal().doubleValue()), new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 0, new int[0],1);

                            obj.agregaCelda(tAux,LETRASIETE_BOLD, "DESCUENTO" , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                            obj.agregaCelda(tAux,LETRAOCHO, "" + new DecimalFormat("$###,###,###,##0.00").format(comprobanteCFDI.getDescuento()!=null?comprobanteCFDI.getDescuento().doubleValue():new Double(0)) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 0, new int[0],1);

                            /*
                             * Buscamos los impuestos trasladados IVA y IEPS de la factura
                             */
                            if(comprobanteCFDI.getImpuestos().getTraslados()!=null){
                                Iterator<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado> iteraTraCFDI = comprobanteCFDI.getImpuestos().getTraslados().getTraslado().iterator();
                                while(iteraTraCFDI.hasNext()){
                                    mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado trasladoCDFI = iteraTraCFDI.next();
                                    obj.agregaCelda(tAux,LETRASIETE_BOLD, trasladoCDFI.getImpuesto() + " " + trasladoCDFI.getTasa()+"%", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                                    obj.agregaCelda(tAux,LETRAOCHO, "" + new DecimalFormat("$###,###,###,##0.00").format(trasladoCDFI.getImporte().doubleValue()) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 0, new int[0],1);
                                }
                            }

                            /*
                             * Buscamos las retenciones de la factura
                             */
                            if(comprobanteCFDI.getImpuestos().getRetenciones()!=null){
                                Iterator<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion> iteraRetCFDI = comprobanteCFDI.getImpuestos().getRetenciones().getRetencion().iterator();
                                while(iteraRetCFDI.hasNext()){
                                    mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion retencionCDFI = iteraRetCFDI.next();
                                    double tasa=0;
                                    BigDecimal subTotal = comprobanteCFDI.getSubTotal().setScale(2, RoundingMode.HALF_UP);
                                    try{
                                        if (comprobanteCFDI.getDescuento()!=BigDecimal.ZERO){
                                            subTotal = subTotal.subtract(comprobanteCFDI.getDescuento());
                                        }
                                    }catch(Exception e){}
                                    tasa = (retencionCDFI.getImporte().doubleValue()/subTotal.doubleValue())*100;
                                    BigDecimal tasabd = new BigDecimal(tasa).setScale(2, RoundingMode.HALF_UP);
                                    obj.agregaCelda(tAux,LETRASIETE_BOLD, "RETENCIÓN " + retencionCDFI.getImpuesto() + " " + tasabd + "%" , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                                    obj.agregaCelda(tAux,LETRAOCHO, "" + new DecimalFormat("$###,###,###,##0.00").format(retencionCDFI.getImporte().doubleValue()) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 0, new int[0],1);
                                }
                            }

                            /*
                             * Impuestos Locales (complemento) , en caso de existir en comprobante
                             */
                            if (implocal!=null){
                                
                                obj.agregaCelda(tAux,LETRASIETE_BOLD, ">>Impuestos Locales<<", new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],2);
                                 
                                /*
                                obj.agregaCelda(tAux,LETRASIETE_BOLD, "_ ", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                                obj.agregaCelda(tAux,LETRAOCHO, "" + new DecimalFormat("$###,###,###,##0.00").format(implocal.getTotaldeTraslados().doubleValue()) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 0, new int[0],1);
                                */
                                
                                if(implocal.getTrasladosLocales()!=null){
                                    if(implocal.getTrasladosLocales().size()>0){
                                        //Traslados
                                        for (ImpuestosLocales.TrasladosLocales trasladoLocal:implocal.getTrasladosLocales()){
                                            obj.agregaCelda(tAux,LETRASIETE_BOLD, trasladoLocal.getImpLocTrasladado() + " " + trasladoLocal.getTasadeTraslado()+"%", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                                            obj.agregaCelda(tAux,LETRAOCHO, "" + new DecimalFormat("$###,###,###,##0.00").format(trasladoLocal.getImporte().doubleValue()) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 0, new int[0],1);
                                        }                                        
                                    }
                                }
                                
                                if(implocal.getRetencionesLocales()!=null){
                                    if(implocal.getRetencionesLocales().size()>0){                                        
                                        //Retenciones
                                        for (ImpuestosLocales.RetencionesLocales retencionLocal:implocal.getRetencionesLocales()){
                                            obj.agregaCelda(tAux,LETRASIETE_BOLD, "RETENCION " + retencionLocal.getImpLocRetenido() + " " + retencionLocal.getTasadeRetencion()+"%", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                                            obj.agregaCelda(tAux,LETRAOCHO, "" + new DecimalFormat("$###,###,###,##0.00").format(retencionLocal.getImporte().doubleValue()) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_TOP, 0, new int[0],1);
                                        }
                                        
                                    }
                                }
                                
                            }
                            
                            obj.agregaCelda(tAux,LETRAOCHO_BOLD, "TOTAL" , new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM, 50, new int[0],1);
                            obj.agregaCelda(tAux,LETRAOCHO_BOLD, "" + new DecimalFormat("$###,###,###,##0.00").format(comprobanteCFDI.getTotal()) , new boolean[]{false,false,false,false}, Element.ALIGN_RIGHT, Element.ALIGN_BOTTOM, 50, new int[0],1);

                        obj.agregaTabla(tTotal, tAux, new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);

                    obj.agregaTabla(tabla, tTotal, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);

                    //SALTO DE LÍNEA
                    obj.agregaCelda(tabla,LETRAOCHO, " ", new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);

                    PdfPTable tSelloDigital = new PdfPTable(1);
                    tSelloDigital.setTotalWidth(550);
                    tSelloDigital.setLockedWidth(true);

                        obj.agregaCelda(tSelloDigital,LETRAOCHO_BOLD, "Sello Digital del emisor:", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                        obj.agregaCelda(tSelloDigital,LETRAOCHO, "" + this.getTimbreFiscalDigital().getSelloCFD() + " ", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);

                    obj.agregaTabla(tabla, tSelloDigital, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);

                    /*
                    PdfPTable tSelloDigitalPSGECFD = new PdfPTable(1);
                    tSelloDigitalPSGECFD.setTotalWidth(550);
                    tSelloDigitalPSGECFD.setLockedWidth(true);

                        obj.agregaCelda(tSelloDigitalPSGECFD,LETRAOCHO_BOLD, "Sello del SAT:", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                        obj.agregaCelda(tSelloDigitalPSGECFD,LETRAOCHO, "" + this.getTimbreFiscalDigital().getSelloSAT() + " ", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);

                    obj.agregaTabla(tabla, tSelloDigitalPSGECFD, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                    */
                    
                    /*
                     * Código de barras y cadena original
                     */
                    //SALTO DE LÍNEA
                    obj.agregaCelda(tabla,LETRAOCHO, " ", new boolean[]{false,false,false,false}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 0, new int[0],1);

                    PdfPTable tCodigoDeBarrasYCadena = new PdfPTable(2);
                    tCodigoDeBarrasYCadena.setTotalWidth(550);
                    tCodigoDeBarrasYCadena.setWidths(new int[]{150,400});
                    tCodigoDeBarrasYCadena.setLockedWidth(true);

                    /***
                     * Obtenemos el Codigo de Barras Bidimensional
                     */
                    com.lowagie.text.Image imgCodigoDeBarras = getCBBiTextImage();

                    obj.agregaCeldaImagen(tCodigoDeBarrasYCadena, imgCodigoDeBarras, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE,130,new int[0], 1);

                    PdfPTable tCadenaOriginal = new PdfPTable(1);
                    tCadenaOriginal.setTotalWidth(400);

                        obj.agregaCelda(tCadenaOriginal,LETRAOCHO_BOLD, "Cadena original del complemento de certificación digital del SAT:", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                        //obj.agregaCelda(tCadenaOriginal,LETRAOCHO, "" + this.cfd.getCadenaOriginal() + " ", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                        obj.agregaCelda(tCadenaOriginal,LETRAOCHO, "" + this.gettFDv1()!=null?this.gettFDv1().getCadenaOriginal():"", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                        obj.agregaCelda(tCadenaOriginal,LETRAOCHO_BOLD, "Sello del SAT:", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                        obj.agregaCelda(tCadenaOriginal,LETRAOCHO, "" + this.getTimbreFiscalDigital().getSelloSAT() + " ", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                    obj.agregaTabla(tCodigoDeBarrasYCadena, tCadenaOriginal, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[]{1,1,1,1},1);


                    obj.agregaTabla(tabla, tCodigoDeBarrasYCadena, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                    
                    /*
                     * Comentarios
                     */
                    if (comprobanteFiscalDto!=null){
                        
                        PdfPTable tableComentarios = new PdfPTable(1);
                        tableComentarios.setTotalWidth(550);
                        tableComentarios.setLockedWidth(true);

                        obj.agregaCelda(tableComentarios,LETRAOCHO_BOLD, "Comentarios:", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);
                        obj.agregaCelda(tableComentarios,LETRAOCHO, "" + (comprobanteFiscalDto.getComentarios()!=null?comprobanteFiscalDto.getComentarios():"") + " ", new boolean[]{false,false,false,false}, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0, new int[0],1);

                        obj.agregaTabla(tabla, tableComentarios, new boolean[]{true,true,true,true}, Element.ALIGN_CENTER, Element.ALIGN_TOP, 0, new int[0],1);
                    }

            document.add(tabla);
            tabla.flushContent();

        }catch(Exception ex){
            ex.printStackTrace();
            msgError = "No se ha podido generar el Comprobante Fiscal Digital en formato PDF:<br/>" + ex.toString();
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

        } catch (BadElementException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
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
        stringCBB += "&id=" + this.getTimbreFiscalDigital().getUUID();
        

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
            Logger.getLogger(Cfd32BO.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return imageCBB;
    }


    public CFDv32 getCfd() {
        return cfd;
    }

    public void setCfd(CFDv32 cfd) {
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

    public String getValidaErrorMsg() {
        return validaErrorMsg;
    }

    public void setValidaErrorMsg(String validaErrorMsg) {
        this.validaErrorMsg = validaErrorMsg;
    }

    public TFDv1_v32 gettFDv1() {
        return tFDv1;
    }

    public void settFDv1(TFDv1_v32 tFDv1) {
        this.tFDv1 = tFDv1;
    }

    public ComprobanteFiscal getComprobanteFiscalDto() {
        return comprobanteFiscalDto;
    }

    public void setComprobanteFiscalDto(ComprobanteFiscal comprobanteFiscalDto) {
        this.comprobanteFiscalDto = comprobanteFiscalDto;
    }

}
