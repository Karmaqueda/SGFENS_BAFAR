
package com.sap.bafar.ws.consultadispersion;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.sap.bafar.ws.consultadispersion package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.sap.bafar.ws.consultadispersion
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ZfmtrDisperResponse }
     * 
     */
    public ZfmtrDisperResponse createZfmtrDisperResponse() {
        return new ZfmtrDisperResponse();
    }

    /**
     * Create an instance of {@link TableOfBdcmsgcoll }
     * 
     */
    public TableOfBdcmsgcoll createTableOfBdcmsgcoll() {
        return new TableOfBdcmsgcoll();
    }

    /**
     * Create an instance of {@link TableOfZcmlOrdPago }
     * 
     */
    public TableOfZcmlOrdPago createTableOfZcmlOrdPago() {
        return new TableOfZcmlOrdPago();
    }

    /**
     * Create an instance of {@link ZfmtrDisper }
     * 
     */
    public ZfmtrDisper createZfmtrDisper() {
        return new ZfmtrDisper();
    }

    /**
     * Create an instance of {@link Bdcmsgcoll }
     * 
     */
    public Bdcmsgcoll createBdcmsgcoll() {
        return new Bdcmsgcoll();
    }

    /**
     * Create an instance of {@link ZcmlOrdPago }
     * 
     */
    public ZcmlOrdPago createZcmlOrdPago() {
        return new ZcmlOrdPago();
    }

}
