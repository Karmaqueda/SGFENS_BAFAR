
package com.tsp.ws.lco;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tsp.ws.lco package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _FindByCertificadoResponse_QNAME = new QName("http://lco.ws.tsp.com/", "findByCertificadoResponse");
    private final static QName _FindByRfcAndCertificadoResponse_QNAME = new QName("http://lco.ws.tsp.com/", "findByRfcAndCertificadoResponse");
    private final static QName _FindByRFCResponse_QNAME = new QName("http://lco.ws.tsp.com/", "findByRFCResponse");
    private final static QName _FindByCertificado_QNAME = new QName("http://lco.ws.tsp.com/", "findByCertificado");
    private final static QName _FindByRFC_QNAME = new QName("http://lco.ws.tsp.com/", "findByRFC");
    private final static QName _FindByRfcAndCertificado_QNAME = new QName("http://lco.ws.tsp.com/", "findByRfcAndCertificado");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tsp.ws.lco
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FindByRfcAndCertificado }
     * 
     */
    public FindByRfcAndCertificado createFindByRfcAndCertificado() {
        return new FindByRfcAndCertificado();
    }

    /**
     * Create an instance of {@link FindByCertificadoResponse }
     * 
     */
    public FindByCertificadoResponse createFindByCertificadoResponse() {
        return new FindByCertificadoResponse();
    }

    /**
     * Create an instance of {@link FindByCertificado }
     * 
     */
    public FindByCertificado createFindByCertificado() {
        return new FindByCertificado();
    }

    /**
     * Create an instance of {@link FindByRFC }
     * 
     */
    public FindByRFC createFindByRFC() {
        return new FindByRFC();
    }

    /**
     * Create an instance of {@link FindByRFCResponse }
     * 
     */
    public FindByRFCResponse createFindByRFCResponse() {
        return new FindByRFCResponse();
    }

    /**
     * Create an instance of {@link FindByRfcAndCertificadoResponse }
     * 
     */
    public FindByRfcAndCertificadoResponse createFindByRfcAndCertificadoResponse() {
        return new FindByRfcAndCertificadoResponse();
    }

    /**
     * Create an instance of {@link Lco }
     * 
     */
    public Lco createLco() {
        return new Lco();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindByCertificadoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://lco.ws.tsp.com/", name = "findByCertificadoResponse")
    public JAXBElement<FindByCertificadoResponse> createFindByCertificadoResponse(FindByCertificadoResponse value) {
        return new JAXBElement<FindByCertificadoResponse>(_FindByCertificadoResponse_QNAME, FindByCertificadoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindByRfcAndCertificadoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://lco.ws.tsp.com/", name = "findByRfcAndCertificadoResponse")
    public JAXBElement<FindByRfcAndCertificadoResponse> createFindByRfcAndCertificadoResponse(FindByRfcAndCertificadoResponse value) {
        return new JAXBElement<FindByRfcAndCertificadoResponse>(_FindByRfcAndCertificadoResponse_QNAME, FindByRfcAndCertificadoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindByRFCResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://lco.ws.tsp.com/", name = "findByRFCResponse")
    public JAXBElement<FindByRFCResponse> createFindByRFCResponse(FindByRFCResponse value) {
        return new JAXBElement<FindByRFCResponse>(_FindByRFCResponse_QNAME, FindByRFCResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindByCertificado }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://lco.ws.tsp.com/", name = "findByCertificado")
    public JAXBElement<FindByCertificado> createFindByCertificado(FindByCertificado value) {
        return new JAXBElement<FindByCertificado>(_FindByCertificado_QNAME, FindByCertificado.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindByRFC }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://lco.ws.tsp.com/", name = "findByRFC")
    public JAXBElement<FindByRFC> createFindByRFC(FindByRFC value) {
        return new JAXBElement<FindByRFC>(_FindByRFC_QNAME, FindByRFC.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindByRfcAndCertificado }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://lco.ws.tsp.com/", name = "findByRfcAndCertificado")
    public JAXBElement<FindByRfcAndCertificado> createFindByRfcAndCertificado(FindByRfcAndCertificado value) {
        return new JAXBElement<FindByRfcAndCertificado>(_FindByRfcAndCertificado_QNAME, FindByRfcAndCertificado.class, null, value);
    }

}
