
package com.tsp.workflow.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tsp.workflow.ws package. 
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

    private final static QName _GetDatosGeneralProveedor_QNAME = new QName("http://ws.workflow.tsp.com/", "getDatosGeneralProveedor");
    private final static QName _GetComprobantes_QNAME = new QName("http://ws.workflow.tsp.com/", "getComprobantes");
    private final static QName _EditComprobante_QNAME = new QName("http://ws.workflow.tsp.com/", "editComprobante");
    private final static QName _GetDatosGeneralProveedorResponse_QNAME = new QName("http://ws.workflow.tsp.com/", "getDatosGeneralProveedorResponse");
    private final static QName _GetFlujoComprobantes_QNAME = new QName("http://ws.workflow.tsp.com/", "getFlujoComprobantes");
    private final static QName _EditComprobanteResponse_QNAME = new QName("http://ws.workflow.tsp.com/", "editComprobanteResponse");
    private final static QName _GetFlujoComprobantesResponse_QNAME = new QName("http://ws.workflow.tsp.com/", "getFlujoComprobantesResponse");
    private final static QName _GetComprobantesResponse_QNAME = new QName("http://ws.workflow.tsp.com/", "getComprobantesResponse");
    private final static QName _SetComprobanteResponse_QNAME = new QName("http://ws.workflow.tsp.com/", "setComprobanteResponse");
    private final static QName _SetComprobante_QNAME = new QName("http://ws.workflow.tsp.com/", "setComprobante");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tsp.workflow.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetComprobantesResponse }
     * 
     */
    public GetComprobantesResponse createGetComprobantesResponse() {
        return new GetComprobantesResponse();
    }

    /**
     * Create an instance of {@link SetComprobanteResponse }
     * 
     */
    public SetComprobanteResponse createSetComprobanteResponse() {
        return new SetComprobanteResponse();
    }

    /**
     * Create an instance of {@link SetComprobante }
     * 
     */
    public SetComprobante createSetComprobante() {
        return new SetComprobante();
    }

    /**
     * Create an instance of {@link EditComprobanteResponse }
     * 
     */
    public EditComprobanteResponse createEditComprobanteResponse() {
        return new EditComprobanteResponse();
    }

    /**
     * Create an instance of {@link GetFlujoComprobantesResponse }
     * 
     */
    public GetFlujoComprobantesResponse createGetFlujoComprobantesResponse() {
        return new GetFlujoComprobantesResponse();
    }

    /**
     * Create an instance of {@link GetDatosGeneralProveedorResponse }
     * 
     */
    public GetDatosGeneralProveedorResponse createGetDatosGeneralProveedorResponse() {
        return new GetDatosGeneralProveedorResponse();
    }

    /**
     * Create an instance of {@link EditComprobante }
     * 
     */
    public EditComprobante createEditComprobante() {
        return new EditComprobante();
    }

    /**
     * Create an instance of {@link GetFlujoComprobantes }
     * 
     */
    public GetFlujoComprobantes createGetFlujoComprobantes() {
        return new GetFlujoComprobantes();
    }

    /**
     * Create an instance of {@link GetDatosGeneralProveedor }
     * 
     */
    public GetDatosGeneralProveedor createGetDatosGeneralProveedor() {
        return new GetDatosGeneralProveedor();
    }

    /**
     * Create an instance of {@link GetComprobantes }
     * 
     */
    public GetComprobantes createGetComprobantes() {
        return new GetComprobantes();
    }

    /**
     * Create an instance of {@link AccesoRequest }
     * 
     */
    public AccesoRequest createAccesoRequest() {
        return new AccesoRequest();
    }

    /**
     * Create an instance of {@link EditaComprobanteRequest }
     * 
     */
    public EditaComprobanteRequest createEditaComprobanteRequest() {
        return new EditaComprobanteRequest();
    }

    /**
     * Create an instance of {@link RegistraComprobanteRequest }
     * 
     */
    public RegistraComprobanteRequest createRegistraComprobanteRequest() {
        return new RegistraComprobanteRequest();
    }

    /**
     * Create an instance of {@link WsItemArea }
     * 
     */
    public WsItemArea createWsItemArea() {
        return new WsItemArea();
    }

    /**
     * Create an instance of {@link ConsultaComprobanteRequest }
     * 
     */
    public ConsultaComprobanteRequest createConsultaComprobanteRequest() {
        return new ConsultaComprobanteRequest();
    }

    /**
     * Create an instance of {@link WsItemComprobante }
     * 
     */
    public WsItemComprobante createWsItemComprobante() {
        return new WsItemComprobante();
    }

    /**
     * Create an instance of {@link WsItemArchivoRequest }
     * 
     */
    public WsItemArchivoRequest createWsItemArchivoRequest() {
        return new WsItemArchivoRequest();
    }

    /**
     * Create an instance of {@link WsItemBitacoraComprobante }
     * 
     */
    public WsItemBitacoraComprobante createWsItemBitacoraComprobante() {
        return new WsItemBitacoraComprobante();
    }

    /**
     * Create an instance of {@link WsResponse }
     * 
     */
    public WsResponse createWsResponse() {
        return new WsResponse();
    }

    /**
     * Create an instance of {@link RegistraComprobanteResponse }
     * 
     */
    public RegistraComprobanteResponse createRegistraComprobanteResponse() {
        return new RegistraComprobanteResponse();
    }

    /**
     * Create an instance of {@link WsItemCliente }
     * 
     */
    public WsItemCliente createWsItemCliente() {
        return new WsItemCliente();
    }

    /**
     * Create an instance of {@link ConsultaFlujoComprobanteResponse }
     * 
     */
    public ConsultaFlujoComprobanteResponse createConsultaFlujoComprobanteResponse() {
        return new ConsultaFlujoComprobanteResponse();
    }

    /**
     * Create an instance of {@link WsItemProveedor }
     * 
     */
    public WsItemProveedor createWsItemProveedor() {
        return new WsItemProveedor();
    }

    /**
     * Create an instance of {@link ConsultaComprobanteResponse }
     * 
     */
    public ConsultaComprobanteResponse createConsultaComprobanteResponse() {
        return new ConsultaComprobanteResponse();
    }

    /**
     * Create an instance of {@link ConsultaDatosGralProveedorResponse }
     * 
     */
    public ConsultaDatosGralProveedorResponse createConsultaDatosGralProveedorResponse() {
        return new ConsultaDatosGralProveedorResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDatosGeneralProveedor }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.workflow.tsp.com/", name = "getDatosGeneralProveedor")
    public JAXBElement<GetDatosGeneralProveedor> createGetDatosGeneralProveedor(GetDatosGeneralProveedor value) {
        return new JAXBElement<GetDatosGeneralProveedor>(_GetDatosGeneralProveedor_QNAME, GetDatosGeneralProveedor.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetComprobantes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.workflow.tsp.com/", name = "getComprobantes")
    public JAXBElement<GetComprobantes> createGetComprobantes(GetComprobantes value) {
        return new JAXBElement<GetComprobantes>(_GetComprobantes_QNAME, GetComprobantes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EditComprobante }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.workflow.tsp.com/", name = "editComprobante")
    public JAXBElement<EditComprobante> createEditComprobante(EditComprobante value) {
        return new JAXBElement<EditComprobante>(_EditComprobante_QNAME, EditComprobante.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDatosGeneralProveedorResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.workflow.tsp.com/", name = "getDatosGeneralProveedorResponse")
    public JAXBElement<GetDatosGeneralProveedorResponse> createGetDatosGeneralProveedorResponse(GetDatosGeneralProveedorResponse value) {
        return new JAXBElement<GetDatosGeneralProveedorResponse>(_GetDatosGeneralProveedorResponse_QNAME, GetDatosGeneralProveedorResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFlujoComprobantes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.workflow.tsp.com/", name = "getFlujoComprobantes")
    public JAXBElement<GetFlujoComprobantes> createGetFlujoComprobantes(GetFlujoComprobantes value) {
        return new JAXBElement<GetFlujoComprobantes>(_GetFlujoComprobantes_QNAME, GetFlujoComprobantes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EditComprobanteResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.workflow.tsp.com/", name = "editComprobanteResponse")
    public JAXBElement<EditComprobanteResponse> createEditComprobanteResponse(EditComprobanteResponse value) {
        return new JAXBElement<EditComprobanteResponse>(_EditComprobanteResponse_QNAME, EditComprobanteResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFlujoComprobantesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.workflow.tsp.com/", name = "getFlujoComprobantesResponse")
    public JAXBElement<GetFlujoComprobantesResponse> createGetFlujoComprobantesResponse(GetFlujoComprobantesResponse value) {
        return new JAXBElement<GetFlujoComprobantesResponse>(_GetFlujoComprobantesResponse_QNAME, GetFlujoComprobantesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetComprobantesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.workflow.tsp.com/", name = "getComprobantesResponse")
    public JAXBElement<GetComprobantesResponse> createGetComprobantesResponse(GetComprobantesResponse value) {
        return new JAXBElement<GetComprobantesResponse>(_GetComprobantesResponse_QNAME, GetComprobantesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetComprobanteResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.workflow.tsp.com/", name = "setComprobanteResponse")
    public JAXBElement<SetComprobanteResponse> createSetComprobanteResponse(SetComprobanteResponse value) {
        return new JAXBElement<SetComprobanteResponse>(_SetComprobanteResponse_QNAME, SetComprobanteResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetComprobante }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.workflow.tsp.com/", name = "setComprobante")
    public JAXBElement<SetComprobante> createSetComprobante(SetComprobante value) {
        return new JAXBElement<SetComprobante>(_SetComprobante_QNAME, SetComprobante.class, null, value);
    }

}
