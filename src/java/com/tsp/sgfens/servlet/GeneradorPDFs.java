/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.servlet;

import com.infosoft.fechas.FormateadorDeFechas;
import com.tsp.sct.beans.FacturaDescripcion;
import com.tsp.sct.beans.Impuesto;
import com.tsp.sct.bo.CbbBO;
import com.tsp.sct.bo.ComprobanteFiscalBO;
import com.tsp.sct.bo.ImagenPersonalBO;
import com.tsp.sct.bo.UbicacionBO;
import com.tsp.sct.dao.dto.Cbb;
import com.tsp.sct.dao.dto.Cliente;
import com.tsp.sct.dao.dto.ComprobanteDescripcion;
import com.tsp.sct.dao.dto.ComprobanteFiscal;
import com.tsp.sct.dao.dto.ComprobanteImpuesto;
import com.tsp.sct.dao.dto.Concepto;
import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.dao.dto.Folios;
import com.tsp.sct.dao.dto.ImagenPersonal;
import com.tsp.sct.dao.dto.TipoDeMoneda;
import com.tsp.sct.dao.dto.TipoPago;
import com.tsp.sct.dao.dto.Ubicacion;
import com.tsp.sct.dao.jdbc.ClienteDaoImpl;
import com.tsp.sct.dao.jdbc.ComprobanteDescripcionDaoImpl;
import com.tsp.sct.dao.jdbc.ComprobanteImpuestoDaoImpl;
import com.tsp.sct.dao.jdbc.EmpresaDaoImpl;
import com.tsp.sct.dao.jdbc.FoliosDaoImpl;
import com.tsp.sct.dao.jdbc.ImpuestoDaoImpl;
import com.tsp.sct.dao.jdbc.TipoDeMonedaDaoImpl;
import com.tsp.sct.dao.jdbc.TipoPagoDaoImpl;
import com.tsp.sct.util.FacturacionUtil;
import com.tsp.sct.util.FormatUtil;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;       
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author Leonardo
 */
public class GeneradorPDFs extends HttpServlet {
    
    private int idComprobanteFiscal;
    private final int ID_COMPROBANTE_FACTURACBB = 13;
    private static final int ID_FACTURA_COTIZACION = 2;
    private ComprobanteFiscalBO bO = null;
    private ComprobanteFiscal cf = null;
    private ComprobanteDescripcion[] cds = null;
    private FacturaDescripcion facturaDescripcion = null;
    private ComprobanteDescripcion comprobanteDescripcion = null;
    List<FacturaDescripcion> resultado = null;
    private ComprobanteImpuesto[] ci = null;
    private List<Impuesto> listImpuestos = null;
    private Cliente cliente = null;
    private Folios folio = null;
    private Empresa empresa = null;
    private Ubicacion ubicacion = null;
    private ImagenPersonal ip = null;
    private Cbb codigo = null;
    private TipoDeMoneda tdm = null;
    private TipoPago tp = null;
    private Connection conn = null;

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("EEEEEEEEEEEEEENNNNNNNNNNTRO A SERVLET");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();        
        try {
           //  TODO output your page here. You may use following sample code. 
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet GeneradorPDFs</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GeneradorPDFs at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        try {
			idComprobanteFiscal = Integer.parseInt(request.getParameter("idComprobanteFiscal"));                        
                        System.out.println("IDENTIIFICADOR DE COMPROBANTE: "+idComprobanteFiscal);
                        String accion = request.getParameter("accion");
                        System.out.println("ACCION: "+accion);
                        
                /////////////Negocio de la factura
                cf = new ComprobanteFiscal();
                bO = new ComprobanteFiscalBO(idComprobanteFiscal,this.conn);
                cf =  bO.getComprobanteFiscal();
                
                cds = new ComprobanteDescripcion[0];                
                //todos los conceptos
                ComprobanteDescripcionDaoImpl comprobanteDescripcionDaoImpl = new ComprobanteDescripcionDaoImpl();
                cds = comprobanteDescripcionDaoImpl.findWhereIdComprobanteFiscalEquals(idComprobanteFiscal);
                
                resultado = new ArrayList<FacturaDescripcion>();                                
                comprobanteDescripcion = new ComprobanteDescripcion();                
                
                if(cds != null){
                    for(int y = 0; y < cds.length; y++){
                        facturaDescripcion = new FacturaDescripcion();
                        comprobanteDescripcion = cds[y];
                        facturaDescripcion.setIdFacturaDescripcion(comprobanteDescripcion.getIdComprobanteDescripcion());
                        facturaDescripcion.setIdFactura(comprobanteDescripcion.getIdComprobanteFiscal());
                        facturaDescripcion.setCantidad(comprobanteDescripcion.getCantidad());
                        facturaDescripcion.setIdConcepto(comprobanteDescripcion.getIdConcepto());
                        facturaDescripcion.setDescripcion(comprobanteDescripcion.getDescripcion());
                        facturaDescripcion.setPrecio(comprobanteDescripcion.getPrecioUnitario());
                        facturaDescripcion.setUnidad(comprobanteDescripcion.getUnidad());
                        facturaDescripcion.setIdentificacion(comprobanteDescripcion.getIdentificacion());
                        if(comprobanteDescripcion.getNombreConcepto()==null){                            
                            facturaDescripcion.setNombreConcepto(comprobanteDescripcion.getNombreConcepto());
                        }else{
                            facturaDescripcion.setNombreConcepto(comprobanteDescripcion.getNombreConcepto());
                        }
                        resultado.add(facturaDescripcion);
                    }
                }
                
                //todos los impuestos
                ci = new ComprobanteImpuesto[0];
                ComprobanteImpuestoDaoImpl comprobanteImpuestoDaoImpl = new ComprobanteImpuestoDaoImpl();
                ci = comprobanteImpuestoDaoImpl.findWhereIdComprobanteFiscalEquals(idComprobanteFiscal);
                ComprobanteImpuesto comImpuestos = new ComprobanteImpuesto();
                com.tsp.sct.dao.dto.Impuesto impuestosSCT = new com.tsp.sct.dao.dto.Impuesto();
                ImpuestoDaoImpl daoImpl = new ImpuestoDaoImpl();
                
                listImpuestos = new ArrayList<Impuesto>();
                Impuesto impPDF = null;
                if(ci != null){
                    for(int x = 0 ; x < ci.length ; x++){
                        impPDF = new Impuesto();
                        comImpuestos = ci[x];
                        impuestosSCT = daoImpl.findByPrimaryKey(comImpuestos.getIdImpuesto());

                        impPDF.setIdImpuesto(impuestosSCT.getIdImpuesto());
                        impPDF.setIdEmpresa(impuestosSCT.getIdEmpresa());
                        impPDF.setNombre(impuestosSCT.getNombre());
                        impPDF.setDescripcion(impuestosSCT.getDescripcion());
                        impPDF.setPorcentaje(impuestosSCT.getPorcentaje());
                        impPDF.setTrasladado(impuestosSCT.isTrasladadoNull());
                        //Calculo de totales:
                        impPDF.setTotal(impuestosSCT.getPorcentaje() * .01 * (cf.getImporteSubtotal() - cf.getDescuento()));
                        impPDF.setIdEstatus(impuestosSCT.getIdEstatus());
                        impPDF.setImpuestoLocal(impuestosSCT.isImpuestoLocalNull());
                        listImpuestos.add(impPDF);
                    }
                }
                
                //Obtenemos el cliente
                cliente = new Cliente();
                ClienteDaoImpl clienteDaoImpl = new ClienteDaoImpl();
                cliente = clienteDaoImpl.findByPrimaryKey(cf.getIdCliente());
                
                //folio y serie                
                folio = new Folios();
                if(cf.getFolioGenerado()!=""){
                    FoliosDaoImpl foliosDaoImpl = new FoliosDaoImpl();                
                    folio = foliosDaoImpl.findByPrimaryKey(cf.getIdFolio());
                }
                
                //Obtenemos la empresa
                empresa = new Empresa();
                EmpresaDaoImpl empresaDaoImpl = new EmpresaDaoImpl();
                empresa = empresaDaoImpl.findByPrimaryKey(cf.getIdEmpresa());
                
                //Obtenermos la ubicacion de la empresa
                ubicacion = new Ubicacion();
                UbicacionBO ubicacionBO = new UbicacionBO(empresa.getIdUbicacionFiscal(),this.conn);
                ubicacion = ubicacionBO.getUbicacion();
			
                //Recuperamos los datos de la imagen personal
                ip = new ImagenPersonal();
                ImagenPersonalBO imagenPersonalBO = new ImagenPersonalBO(this.conn);
                try{
                    ip = imagenPersonalBO.findImagenPersonalByEmpresa(empresa.getIdEmpresa());
                }catch(Exception e){ip= null;}
                
                //Recuperamos el CBB
                codigo = new Cbb();
                CbbBO cbbBO = new CbbBO(this.conn);
                codigo = cbbBO.findCbbByEmpresa(empresa.getIdEmpresa());
                
                //recuperamos el tipo de moneda
                tdm = new TipoDeMoneda();
                TipoDeMonedaDaoImpl tipoDeMonedaDaoImpl = new TipoDeMonedaDaoImpl();
                tdm = tipoDeMonedaDaoImpl.findByPrimaryKey(cf.getIdTipoMoneda());
                
                //recuperamos el tipo de Pago
                tp = new TipoPago();
                TipoPagoDaoImpl tipoPagoDaoImpl = new TipoPagoDaoImpl();
                tp = tipoPagoDaoImpl.findByPrimaryKey(cf.getIdTipoPago());
                
                /////////////
                        
                        //if ("imprimeFacturaCBB".equals(accion)){ 
                            System.out.println("--------------------***************:  entro a IMPRIMIR PDF");
				byte[] bytes = getFacturaCBB(request);
				response.setContentType("application/pdf");
				response.setContentLength(bytes.length);
				ServletOutputStream outs = response.getOutputStream();
				outs.write(bytes, 0, bytes.length);

				ServletOutputStream out = response.getOutputStream();
				//List listaPariticipantes = new ArrayList();
				response.addHeader("content-disposition", "attachment;filename=C:/pack/PDFparaFactura" + outs + ".pdf");
				outs.flush();
				outs.close();
			//}
                    } catch (Exception e) {
                    e.printStackTrace();
		}
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    private byte[] getFacturaCBB(HttpServletRequest request) {
		ServletContext context = getServletContext();
		String cotizacion = "";
                	
		try {
			if (cf.getIdFolio() == -1) {
				cotizacion = "COTIZACION";
			} else {
				if (cf.getIdTipoComprobante() == ID_COMPROBANTE_FACTURACBB) {					
					cotizacion = "FACTURACBB";
				}
			}
			Map<String, Object> parametros = new HashMap<String, Object>();

			// DATOS DEL CLIENTE

			parametros.put("TIPO_PAGO", tp.getDescTipoPago());
			parametros.put("REGIMEN", empresa.getRegimenFiscal());
			// parametros.put("FOLIO_GENERADO", factura.getNumeroFactura());
			String fol = "" + folio.getSerie() + " "+ cf.getFolioGenerado();
			parametros.put("FOLIO", fol);
			parametros.put("SECOFI", folio.getSecofi());
			parametros.put("FECHA_APROBACION", folio.getFechaVigencia());

			parametros.put("Empresa", String.valueOf(empresa.getIdEmpresa()));
			parametros.put("CLIENTE_NOMBRE",cliente.getNombreCliente() != null ? cliente.getNombreCliente() : "");
			parametros.put("CLIENTE_RFC",cliente.getRfcCliente() != null ? cliente.getRfcCliente() : "");
			parametros.put("CLIENTE_TEL", cliente.getTelefono() != null ? cliente.getTelefono() : "");
			parametros.put("CLIENTE_CORREO", cliente.getCorreo() != null ? cliente.getCorreo() : "");
			parametros.put("CLIENTE_CONTACTO", cliente.getContacto() != null ? cliente.getContacto()
					: "");
			parametros.put("CLIENTE_DIRECCION",(cliente.getCalle() != null ? cliente.getCalle() : "")
									+ (cliente.getNumero() != null ? " No. " + cliente.getNumero(): "")
									// +
									// (factura.getCliente().getNumeroInterior()!=null?
									// " INTERIOR " +
									// factura.getCliente().getNumeroInterior():
									// "")
									+ (cliente.getNumeroInterior() != null ? " "
											+ cliente.getNumeroInterior()
											: "")
									+ (cliente.getCodigoPostal() != null ? " C.P. "
											+ cliente.getCodigoPostal()
											: "")
									+ (cliente.getColonia() != null ? " COL. "
											+ cliente.getColonia()
											: "")
									+ " "
									+ (cliente.getPais() != null ? cliente.getPais() + ","
											: "")
									+ " "
									+ (cliente.getEstado() != null ? cliente.getEstado() + ","
											: "")
									+ " "
									+ (cliente.getMunicipio() != null ? cliente.getMunicipio() : ""));

			// DATOS DE EMPRESA
			parametros.put("EMPRESA_RAZON", empresa.getRazonSocial());
			parametros.put("EMPRESA_RFC", empresa.getRfc());

			if (ubicacion.getNumInt() != null) {
				parametros.put("EMPRESA_DIRECCION", ubicacion.getCalle()
						+ " "
						+ ubicacion.getNumExt()
						+ " "
						+ ubicacion.getNumInt()
						+ ", COL. "
						+ ubicacion.getColonia());
			} else
				parametros.put("EMPRESA_DIRECCION", ubicacion.getCalle()
						+ " "
						+ ubicacion.getNumExt()
						+ " COL. "
						+ ubicacion.getColonia());

			parametros.put("EMPRESA_UBICACION", ubicacion.getPais()
							+ " "
							+ ubicacion.getEstado()
							+ " DEL. "
							+ ubicacion.getMunicipio()
							+ " C.P. "
							+ ubicacion.getCodigoPostal());

			// COMPROBANTE FISCAL
			if (cf.getIdEstatus() != ID_FACTURA_COTIZACION) {
				// EN CASO DE QUE NO SE TRATE DE LA COTIZACION DE UNA FACTURA				
				try {
					if (folio.getSerie().intern() == "TSPBD") {
						parametros.put("SERIE_FOLIO", "");
						parametros.put("FOLIO_GENERADO", "");
					} else {
						parametros.put("SERIE_FOLIO", folio.getSerie());						
						parametros.put("FOLIO_GENERADO", cf.getFolioGenerado());						
					}
				} catch (Exception e) {
					try {
						parametros.put("SERIE_FOLIO", "");
						parametros.put("FOLIO_GENERADO", cf.getFolioGenerado());
					} catch (Exception e2) {
						parametros.put("SERIE_FOLIO", "");
						parametros.put("FOLIO_GENERADO", "");
					}
				}
			}

			// DecimalFormat formateador1 = new
			// DecimalFormat("#,##0.00;(#,##0.00)");
			// DecimalFormat formateador1 = new
			// DecimalFormat("###,###,###,###,###,###,###.##");

			parametros.put("TIPO_MONEDA", tdm.getClave());
			parametros.put("COTIZACION", cotizacion != null ? cotizacion : "");
			parametros.put("TOTAL", FormatUtil.doubleToStringPuntoComas(cf.getImporteNeto()));			
			parametros.put("SUBTOTAL", FormatUtil.doubleToStringPuntoComas(cf.getImporteSubtotal()));
			BigDecimal subtotalDec = new BigDecimal(cf.getImporteSubtotal());
			parametros.put("SUBTOTAL_DEC", subtotalDec);
			parametros.put("IMPORTE_LETRA", FacturacionUtil.importeLetra(cf.getImporteNeto(), tdm.getClave()));
			parametros.put("COMENTARIOS", cf.getComentarios());
			if (cf.getIdTipoComprobante() == ID_COMPROBANTE_FACTURACBB) {
				parametros.put("TIPO_RECIBO", "FACTURA");
			} else {
                            parametros.put("TIPO_RECIBO", "FACTURA");
                        }
			// SI LA EMPRESA TIENE UN LOGO SE VA A CARGAR EL PROPIO//			
			if (ip != null) {
				parametros.put("LOGO","C:/SystemaDeArchivos/"+ empresa.getRfc()+ "/"+ ip.getNombreImagen());
			} else {// SI LA EMPRESA NO TIENE UN LOGO PROPIO SE CARGA EL LOGO DE FACTURA EN SEGUNDOS
				parametros.put("LOGO", request.getSession().getServletContext().getRealPath("/images/error.png"));
			}
			if (cf.getFechaImpresion() != null) {
				parametros.put("FECHA_IMPRESION", FormateadorDeFechas.getFecha(cf.getFechaImpresion().getTime(),FormateadorDeFechas.FORMATO_DDMMYYYYHHMMSS));
			} else {
				parametros.put("FECHA_IMPRESION", "");
			}

			// DETALLE DE IMPUESTOS
			parametros.put("IMPUESTO_TOTAL",String.valueOf(cf.getImpuestos()));
//			List<Impuesto> listImpuestos = (List<Impuesto>) factura.getImpuestos();
			System.setProperty("jasper.reports.compile.class.path",context.getRealPath("/WEB-INF/lib/jasperreports-3.7.4.jar") + context.getRealPath("/WEB-INF/"));
			try {
				//JasperReport reporteImpuestos = (JasperReport) JRLoader.loadObject(context.getRealPath("/reportes/FacturaImpuestos_"+ empresa.getRfc() + ".jasper"));
                                JasperReport reporteImpuestos = (JasperReport) JRLoader.loadObject("C:/Users/Administrator/Desktop/SCT/apache-tomcat-6.0.29_PRETORIANO/webapps/SGFENS/reportes/FacturaImpuestos_"+ empresa.getRfc() + ".jasper");
				parametros.put("REPORTE_IMPUESTOS", reporteImpuestos);
				parametros.put("DATOS_IMPUESTOS",new JRBeanCollectionDataSource(listImpuestos));
			} catch (Exception e) {
				System.out.println("-- REPORTE JASPER DEFAULT; ERROR: "+ e.getMessage());
				JasperReport reporteImpuestos = (JasperReport) JRLoader.loadObject("C:/Users/Administrator/Desktop/SCT/apache-tomcat-6.0.29_PRETORIANO/webapps/SGFENS/reportes/FacturaImpuestos.jasper");
				parametros.put("REPORTE_IMPUESTOS", reporteImpuestos);
				parametros.put("DATOS_IMPUESTOS",new JRBeanCollectionDataSource(listImpuestos));
			}

			File reportFile;

			// DETALLE DE FACTURA
			System.out.println("#### RFC Empresa: " + empresa.getRfc());
			//List<FacturaDescripcion> resultado = new ArrayList<FacturaDescripcion>();
			System.out.println("Tipo de comprobante ::::::::::::: "+ cf.getIdTipoComprobante());
			if (cf.getIdTipoComprobante() == ID_COMPROBANTE_FACTURACBB) {
				// reportFile = new
				// File(context.getRealPath("/reportes/Factura_CBB.jasper"));
				System.out.println("Prueba enrique----------------------------------->>>>>>>>>>>>>>>>>>>>>>>>1");
				// JASPER PERSONALIZADO EN BASE AL RFC
				try {
					System.out.println("Prueba enrique----------------------------------->>>>>>>>>>>>>>>>>>>>>>>>2");
					System.out.println("Imprimiendo el tipo de comprobante. ------- :");
					//reportFile = new File(context.getRealPath("/reportes/Factura_CBB_"+ empresa.getRfc() + ".jasper"));
                                        reportFile = new File("C:/Users/Administrator/Desktop/SCT/apache-tomcat-6.0.29_PRETORIANO/webapps/SGFENS/reportes/Factura_CBB_"+ empresa.getRfc() + ".jasper");
				} catch (Exception e) {
					System.out.println("Prueba enrique----------------------------------->>>>>>>>>>>>>>>>>>>>>>>>Catch");
					System.out.println("-- REPORTE JASPER DEFAULT; ERROR: "+ e.getMessage());
					//reportFile = new File(context.getRealPath("/reportes/Factura_CBB.jasper"));
                                        reportFile = new File("C:/Users/Administrator/Desktop/SCT/apache-tomcat-6.0.29_PRETORIANO/webapps/SGFENS/reportes/Factura_CBB.jasper");
                                        
				}
				// reportFile = new
				// File(context.getRealPath("/reportes/Factura_carta.jasper"));
				System.setProperty("jasper.reports.compile.class.path",context.getRealPath("/WEB-INF/lib/jasperreports-3.7.4.jar")
								+ context.getRealPath("/WEB-INF/"));
			}			
			DecimalFormat formateador = new DecimalFormat("#00.000000;(-#00.000000)");
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
			dfs.setDecimalSeparator('.');
			formateador.setDecimalFormatSymbols(dfs);
			// ----------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			//CodigoBBDAO codigo = new CodigoBBDAO();			
			parametros.put("QRCODE", "C:/SystemaDeArchivos/"+ empresa.getRfc()+ "/"+ codigo.getNombreCbb());
			System.setProperty("jasper.reports.compile.class.path",context.getRealPath("/WEB-INF/lib/jasperreports-3.7.4.jar") + System.getProperty("path.separator") + context.getRealPath("/WEB-INF/"));

			System.out.println("Prueba enrique----------------------------------->>>>>>>>>>>>>>>>>>>>>>>>4");
			if (cf.getIdTipoComprobante() == ID_COMPROBANTE_FACTURACBB) {
				//reportFile = new File(context.getRealPath("/reportes/Factura_CBB.jasper"));
                                reportFile = new File("C:/Users/Administrator/Desktop/SCT/apache-tomcat-6.0.29_PRETORIANO/webapps/SGFENS/reportes/Factura_CBB.jasper");
				System.out.println("Prueba enrique----------------------------------->>>>>>>>>>>>>>>>>>>>>>>>5");
				JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(resultado);
				System.out.println("Prueba enrique----------------------------------->>>>>>>>>>>>>>>>>>>>>>>>6");
				//return JasperRunManager.runReportToPdf(reportFile.getPath(),parametros, ds);
                                return JasperRunManager.runReportToPdf("C:/Users/Administrator/Desktop/SCT/apache-tomcat-6.0.29_PRETORIANO/webapps/SGFENS/reportes/Factura_CBB.jasper" ,parametros, ds);
			}			
			// JRBeanCollectionDataSource ds = new
			// JRBeanCollectionDataSource(resultado);
			// System.out.println("Prueba enrique----------------------------------->>>>>>>>>>>>>>>>>>>>>>>>6");
			return null;

		} catch (Exception e) {
			e.printStackTrace();
		} 
                /*finally {
			ConnectionManager.closeConnection(conn);
		}*/

		return null;
	}
}
