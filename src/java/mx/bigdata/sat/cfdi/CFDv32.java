/*
 *  Copyright 2010 BigData.mx
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package mx.bigdata.sat.cfdi;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import mx.bigdata.sat.cfdi.schema.v32.Comprobante;
import mx.bigdata.sat.common.CFD;
import mx.bigdata.sat.common.NamespacePrefixMapperImpl;
import mx.bigdata.sat.common.URIResolverImpl;
import mx.bigdata.sat.security.KeyLoader;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;

public final class CFDv32 implements CFD {

  private static final String XSLT = "/xslt/v32/cadenaoriginal_3_2.xslt";
  
  private static final String XSD = "/xsd/v32/cfdv32.xsd";

  private static final String XSD_TFD = "/xsd/v32/TimbreFiscalDigital.xsd";

  //Complemento Detallista
  private static final String XSD_DETALLISTA = "/xsd/v32/complementos/detallista.xsd";
  //Complemento Divisas
  private static final String XSD_DIVISAS = "/xsd/v32/complementos/divisas.xsd";
  //Complemento Donatarias 1.1
  private static final String XSD_DONAT11 = "/xsd/v32/complementos/donat11.xsd";
  //Complemento Estado de Cuenta Bancario
  private static final String XSD_ECB = "/xsd/v32/complementos/ecb.xsd";
  //Complemento ECC
  private static final String XSD_ECC = "/xsd/v32/complementos/ecc.xsd";
  //Complemento Instituciones Educativas Privadas
  private static final String XSD_IEDU = "/xsd/v32/complementos/iedu.xsd";
  //Complemento Impuestos Locales
  private static final String XSD_IMPLOCAL = "/xsd/v32/complementos/implocal.xsd";
  //Complemento Leyendas Fiscales
  private static final String XSD_LEYENDASFISC = "/xsd/v32/complementos/leyendasFisc.xsd";
  //Complemento PF Integrante Coordinado
  private static final String XSD_PFIC = "/xsd/v32/complementos/pfic.xsd";
  //Complemento Por Cuenta de Terceros 1.1
  private static final String XSD_TERCEROS11 = "/xsd/v32/complementos/terceros11.xsd";
  //Complemento Venta de Vehiculos
  private static final String XSD_VENTAVEHICULOS = "/xsd/v32/complementos/ventavehiculos.xsd";
  //Complemento Turista Pasajero Extranjero
  private static final String XSD_TPE = "/xsd/v32/complementos/TuristaPasajeroExtranjero.xsd";
  //Complemento Nomina
  private static final String XSD_NOMINA = "/xsd/v32/complementos/nomina11.xsd";
  

  private static final String XML_HEADER = 
    "\ufeff<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

  private static final String BASE_CONTEXT = "mx.bigdata.sat.cfdi.schema.v32";
  
  private final static Joiner JOINER = Joiner.on(':');
      
  private final JAXBContext context;

  public static final ImmutableMap<String, String> PREFIXES = 
    ImmutableMap.of("http://www.w3.org/2001/XMLSchema-instance","xsi", 
                    "http://www.sat.gob.mx/cfd/3", "cfdi", 
                    "http://www.sat.gob.mx/TimbreFiscalDigital", "tfd",
                    "http://www.sat.gob.mx/implocal", "implocal",
                    "http://www.sat.gob.mx/nomina", "nomina");

  private final Map<String, String> localPrefixes = Maps.newHashMap(PREFIXES);
  
  private TransformerFactory tf;

  final Comprobante document;

  public CFDv32(InputStream in, String... contexts) throws Exception {
    this.context = getContext(contexts);
    this.document = load(in);
  }

  public CFDv32(Comprobante comprobante, String... contexts) throws Exception {
    this.context = getContext(contexts);
    this.document = copy(comprobante);
  }

  public void addNamespace(String uri, String prefix) {
    localPrefixes.put(uri, prefix);
  }

  public void setTransformerFactory(TransformerFactory tf) {
    this.tf = tf;   
    tf.setURIResolver(new URIResolverImpl()); 
  }

  public void sellar(PrivateKey key, X509Certificate cert) throws Exception {
    document.setFecha(new Date());
    cert.checkValidity(); 
    String signature = getSignature(key);
    document.setSello(signature);
    byte[] bytes = cert.getEncoded();
    Base64 b64 = new Base64(-1);
    String certStr = b64.encodeToString(bytes);
    document.setCertificado(certStr);
    BigInteger bi = cert.getSerialNumber();
    document.setNoCertificado(new String(bi.toByteArray()));
  }
  
  public Comprobante sellarComprobante(PrivateKey key, X509Certificate cert) 
    throws Exception {
    sellar(key, cert);
    return getComprobante();
  }

  public void validar() throws Exception {
    validar(null);
  }

  public void validar(ErrorHandler handler) throws Exception {
    SchemaFactory sf =
      SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Source[] schemas = new Source[] {
      new StreamSource(getClass().getResourceAsStream(XSD)),
      new StreamSource(getClass().getResourceAsStream(XSD_TFD)),
      new StreamSource(getClass().getResourceAsStream(XSD_DETALLISTA)),  //Esquema de Detallista
      new StreamSource(getClass().getResourceAsStream(XSD_DIVISAS)),  //Esquema de Divisas
      new StreamSource(getClass().getResourceAsStream(XSD_DONAT11)),  //Esquema de Donatarias 1.1
      new StreamSource(getClass().getResourceAsStream(XSD_ECB)),  //Esquema de ECB
      new StreamSource(getClass().getResourceAsStream(XSD_ECC)),  //Esquema de ECC
      new StreamSource(getClass().getResourceAsStream(XSD_IEDU)),  //Esquema de IEDU
      new StreamSource(getClass().getResourceAsStream(XSD_IMPLOCAL)),  //Esquema de Impuestos Locales
      new StreamSource(getClass().getResourceAsStream(XSD_LEYENDASFISC)),  //Esquema de Leyendas Fiscales
      new StreamSource(getClass().getResourceAsStream(XSD_PFIC)),  //Esquema de PF Integrado Coordinado
      new StreamSource(getClass().getResourceAsStream(XSD_TERCEROS11)),  //Esquema Por Cuenta de Terceros 1.1
      new StreamSource(getClass().getResourceAsStream(XSD_VENTAVEHICULOS)),  //Esquema de Venta Vehicular
      new StreamSource(getClass().getResourceAsStream(XSD_TPE)),  //Esquema Turista Pasajero Extranjero
      new StreamSource(getClass().getResourceAsStream(XSD_NOMINA))  //Esquema Nomina
    };
    Schema schema = sf.newSchema(schemas);
    Validator validator = schema.newValidator();
    if (handler != null) {
      validator.setErrorHandler(handler);
    }
    
    //Validamos documento sin addendas
    ///-----------------------------------
    /*Comprobante auxDocumentAddenda = document;
    auxDocumentAddenda.setAddenda(null);
    validator.validate(new JAXBSource(context, auxDocumentAddenda));*/
    ///-----------------------------------
    validator.validate(new JAXBSource(context, document));
    
  }

  public void verificar() throws Exception {
    String certStr = document.getCertificado();
    Base64 b64 = new Base64();
    byte[] cbs = b64.decode(certStr);
    X509Certificate cert = KeyLoader
      .loadX509Certificate(new ByteArrayInputStream(cbs)); 
    String sigStr = document.getSello();
    byte[] signature = b64.decode(sigStr); 
    byte[] bytes = getOriginalBytes();
    Signature sig = Signature.getInstance("SHA1withRSA");
    sig.initVerify(cert);
    sig.update(bytes);
    boolean bool = sig.verify(signature);
    if (!bool) {
      throw new Exception("Invalid signature");
    }    
  }

  public void guardar(OutputStream out) throws Exception {
    Marshaller m = context.createMarshaller();
    m.setProperty("com.sun.xml.bind.namespacePrefixMapper",
                  new NamespacePrefixMapperImpl(localPrefixes));
    m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, 
                  "http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv32.xsd "
                  + getComplementosNameSpaceAndSchema()
                  + "http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/TimbreFiscalDigital/TimbreFiscalDigital.xsd "
                  );

    byte[] xmlHeaderBytes = XML_HEADER.getBytes("UTF8");
    out.write(xmlHeaderBytes); 
    m.marshal(document, out);
  }
  
  public static String getComplementosNameSpaceAndSchema(){
      String complementosNameSpaceAndSchema="";
      complementosNameSpaceAndSchema = "http://www.sat.gob.mx/ecb http://www.sat.gob.mx/sitio_internet/cfd/ecb/ecb.xsd "
                  + "http://www.sat.gob.mx/ecc http://www.sat.gob.mx/sitio_internet/cfd/ecc/ecc.xsd "
                  + "http://www.sat.gob.mx/detallista http://www.sat.gob.mx/sitio_internet/cfd/detallista/detallista.xsd "
                  + "http://www.sat.gob.mx/implocal http://www.sat.gob.mx/sitio_internet/cfd/implocal/implocal.xsd "
                  + "http://www.sat.gob.mx/donat http://www.sat.gob.mx/sitio_internet/cfd/donat/donat11.xsd "
                  + "http://www.sat.gob.mx/divisas  http://www.sat.gob.mx/sitio_internet/cfd/divisas/divisas.xsd "
                  + "http://www.sat.gob.mx/iedu http://www.sat.gob.mx/sitio_internet/cfd/iedu/iedu.xsd "
                  + "http://www.sat.gob.mx/leyendasFiscales http://www.sat.gob.mx/sitio_internet/cfd/leyendasFiscales/leyendasFisc.xsd "
                  + "http://www.sat.gob.mx/pfic http://www.sat.gob.mx/sitio_internet/cfd/pfic/pfic.xsd "
                  + "http://www.sat.gob.mx/terceros http://www.sat.gob.mx/sitio_internet/cfd/terceros/terceros11.xsd "
                  + "http://www.sat.gob.mx/TuristaPasajeroExtranjero http://www.sat.gob.mx/sitio_internet/cfd/TuristaPasajeroExtranjero/TuristaPasajeroExtranjero.xsd "
                  + "http://www.sat.gob.mx/ventavehiculos http://www.sat.gob.mx/sitio_internet/cfd/ventavehiculos/ventavehiculos.xsd "
                  + "http://www.sat.gob.mx/nomina http://www.sat.gob.mx/sitio_internet/cfd/nomina/nomina11.xsd ";
      return complementosNameSpaceAndSchema;
  }

  public String getCadenaOriginal() throws Exception {
    byte[] bytes = getOriginalBytes();
    return new String(bytes, "UTF-8");
  }
  
  /**
   * Hace el cálculo del Hash con SHA-1 de la Cadena Original
   * @param message
   * @return String con el Hash SHA-1 de la cadena original del comprobante fiscal
   * @throws UnsupportedEncodingException
   * @throws NoSuchAlgorithmException 
   */
    public String getHash() throws UnsupportedEncodingException, NoSuchAlgorithmException, Exception{
        String hash = "";
        MessageDigest md;
        byte[] buffer, digest;

        String message = getCadenaOriginal();
        buffer = message.getBytes("UTF-8");
        md = MessageDigest.getInstance("SHA1");
        md.update(buffer);
        digest = md.digest();

        for(byte aux : digest) {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) hash += "0";
            hash += Integer.toHexString(b);
        }

        return hash;
    }

  public static Comprobante newComprobante(InputStream in) throws Exception {
    return load(in);
  }

  byte[] getOriginalBytes() throws Exception {
    JAXBSource in = new JAXBSource(context, document);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Result out = new StreamResult(baos);
    TransformerFactory factory = tf;
    if (factory == null) {
      factory = TransformerFactory.newInstance();
      factory.setURIResolver(new URIResolverImpl());
    }     
    Transformer transformer = factory
      .newTransformer(new StreamSource(getClass().getResourceAsStream(XSLT)));
    transformer.transform(in, out);
    return baos.toByteArray();
  }
    
  String getSignature(PrivateKey key) throws Exception {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    byte[] bytes = getOriginalBytes();
    Signature sig = Signature.getInstance("SHA1withRSA");
    sig.initSign(key);
    sig.update(bytes);
    byte[] signed = sig.sign();
    Base64 b64 = new Base64(-1);
    return b64.encodeToString(signed);
  }

  Comprobante getComprobante() throws Exception {
    return copy(document);
  }

  // Defensive deep-copy
  private Comprobante copy(Comprobante comprobante) throws Exception {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    DocumentBuilder db = dbf.newDocumentBuilder(); 
    Document doc = db.newDocument();
    Marshaller m = context.createMarshaller();
    m.marshal(comprobante, doc);
    Unmarshaller u = context.createUnmarshaller();
    return (Comprobante) u.unmarshal(doc);
  }
  
  private static JAXBContext getContext(String[] contexts) throws Exception {
    List<String> ctx = Lists.asList(BASE_CONTEXT, contexts);
    return JAXBContext.newInstance(JOINER.join(ctx));
  }

  private static Comprobante load(InputStream source, String... contexts) 
    throws Exception {
    JAXBContext context = getContext(contexts);
    try {
      Unmarshaller u = context.createUnmarshaller();
      return (Comprobante) u.unmarshal(source);
    } finally {
      source.close();
    }
  }

  static void dump(String title, byte[] bytes, PrintStream out) {
    out.printf("%s: ", title);
    for (byte b : bytes) {
      out.printf("%02x ", b & 0xff);
    }
    out.println();
  }
  
  public String getHashCadenaOriginal(){
    String hash = null;
      
    try {
        hash = getHash(getCadenaOriginal());
    } catch (Exception ex) {
        //Logger.getLogger(CFDv32.class.getName()).log(Level.SEVERE, null, ex);
    }
      
    return hash;
  }
  
  private String getHash(String message) throws UnsupportedEncodingException, NoSuchAlgorithmException{
        String hash = "";
        MessageDigest md;
        byte[] buffer, digest;
        
        buffer = message.getBytes("UTF-8");
        md = MessageDigest.getInstance("SHA1");
        md.update(buffer);
        digest = md.digest();

        for(byte aux : digest) {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) hash += "0";
            hash += Integer.toHexString(b);
        }
        
        return hash;
    }
}