<%-- 
    Document   : catProductos_ajax
    Created on : 22/11/2012, 10:10:01 AM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.bo.ZonaHorariaBO"%>
<%@page import="com.tsp.sct.util.GenericMethods"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.dao.dto.Categoria"%>
<%@page import="com.tsp.sct.dao.jdbc.ClientePrecioConceptoDaoImpl"%>
<%@page import="com.tsp.sct.bo.ClientePrecioConceptoBO"%>
<%@page import="com.tsp.sct.dao.dto.ClientePrecioConcepto"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.ExistenciaAlmacenDaoImpl"%>
<%@page import="java.math.RoundingMode"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.tsp.sct.dao.dto.ExistenciaAlmacen"%>
<%@page import="com.tsp.sct.dao.dto.ConceptoPk"%>
<%@page import="com.tsp.sct.dao.jdbc.MovimientoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Movimiento"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.tsp.sct.bo.CategoriaBO"%>
<%@page import="com.tsp.sct.dao.jdbc.ImpuestoPorConceptoDaoImpl"%>
<%@page import="com.tsp.sct.bo.ImpuestoPorConceptoBO"%>
<%@page import="com.tsp.sct.dao.dto.ImpuestoPorConcepto"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.PretoCaracteristicasConsola"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.ConceptoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";

    int idEmpresa = user.getUser().getIdEmpresa();

    /*
     * Parámetros
     */
    int idConcepto = -1;
    String nombreConcepto = "";
    String descripcion = "";
    float precioConcepto = -1;
    String skuConcepto = "";
    int idCategoriaConcepto = -1;
    int idSubCategoriaConcepto = -1;
    int idSubCategoria2Concepto = -1;
    int idSubCategoria3Concepto = -1;
    int idSubCategoria4Concepto = -1;
    int idMarcaConcepto = -1;
    int idEmbalajeConcepto = -1;
    int idImpuestoConcepto = -1;
    int idImpuestoConceptoB = -1;
    //double precioCompraConcepto = 0;
    float precioCompraConcepto = 0;
    String loteConcepto = "";
    double stockConcepto = 0;
    String descrCortaConcepto = "";
    int idAlmacenConcepto = -1;
    double stockMinimoConcepto = 0;
    short avisoStockConcepto = -1;
    String detalleConcepto = "";
    double volumenConcepto = 0;
    double pesoConcepto = 0;
    String observacionesConcepto = "";
    double precioDocenaConcepto = 0;
    double precioMedioMayoreoConcepto = 0;
    double precioMayoreoConcepto = 0;
    double precioEspecialConcepto = 0;
    int estatus = 2;//deshabilitado
    double maxMenudeo = 0;
    double minMedioMayoreo = 0;
    double maxMedioMayoreo = 0;
    double minMayoreo = 0;
    double precioMinimoVenta = 0;
    String claveConcepto = "";
    double desglosePiezas = 1;
    double stockInicial = 0;
    int idAlmacenInicial = 0;
    int materiaPrima = 0;
    int chkCodigoBDefecto = 0;

    /*
     * Recepción de valores
     */
    mode = request.getParameter("mode") != null ? request.getParameter("mode") : "";
    try {
        idConcepto = Integer.parseInt(request.getParameter("idConcepto"));
    } catch (NumberFormatException ex) {
    }
    nombreConcepto = request.getParameter("nombreConcepto") != null ? new String(request.getParameter("nombreConcepto").getBytes("ISO-8859-1"), "UTF-8") : "";
    descripcion = request.getParameter("descripcion") != null ? new String(request.getParameter("descripcion").getBytes("ISO-8859-1"), "UTF-8") : "";
    try {
        precioConcepto = Float.parseFloat(request.getParameter("precioConcepto") != null ? new String(request.getParameter("precioConcepto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (NumberFormatException ex) {
    }
    try {
        precioDocenaConcepto = Double.parseDouble(request.getParameter("precioDocenaConcepto") != null ? new String(request.getParameter("precioDocenaConcepto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (NumberFormatException ex) {
    }
    try {
        precioMedioMayoreoConcepto = Double.parseDouble(request.getParameter("precioMedioMayoreoConcepto") != null ? new String(request.getParameter("precioMedioMayoreoConcepto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (NumberFormatException ex) {
    }
    try {
        precioMayoreoConcepto = Double.parseDouble(request.getParameter("precioMayoreoConcepto") != null ? new String(request.getParameter("precioMayoreoConcepto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (NumberFormatException ex) {
    }
    try {
        precioEspecialConcepto = Double.parseDouble(request.getParameter("precioEspecialConcepto") != null ? new String(request.getParameter("precioEspecialConcepto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (NumberFormatException ex) {
    }
    skuConcepto = request.getParameter("skuConcepto") != null ? new String(request.getParameter("skuConcepto").getBytes("ISO-8859-1"), "UTF-8") : "";
    try {
        idCategoriaConcepto = Integer.parseInt(request.getParameter("categoriaConcepto"));
    } catch (NumberFormatException ex) {
    }
    try {
        idSubCategoriaConcepto = Integer.parseInt(request.getParameter("subCategoriaConcepto"));
    } catch (NumberFormatException ex) {
    }
    try {
        idSubCategoria2Concepto = Integer.parseInt(request.getParameter("subCategoria2Concepto"));
    } catch (NumberFormatException ex) {
    }
    try {
        idSubCategoria3Concepto = Integer.parseInt(request.getParameter("subCategoria3Concepto"));
    } catch (NumberFormatException ex) {
    }
    try {
        idSubCategoria4Concepto = Integer.parseInt(request.getParameter("subCategoria4Concepto"));
    } catch (NumberFormatException ex) {
    }
    try {
        idMarcaConcepto = Integer.parseInt(request.getParameter("marcaConcepto"));
    } catch (NumberFormatException ex) {
    }
    try {
        idEmbalajeConcepto = Integer.parseInt(request.getParameter("embalajeConcepto"));
    } catch (NumberFormatException ex) {
    }
    try {
        idImpuestoConcepto = Integer.parseInt(request.getParameter("impuestoConcepto"));
    } catch (NumberFormatException ex) {
    }
    try {
        idImpuestoConceptoB = Integer.parseInt(request.getParameter("idImpuestoConceptoB"));
    } catch (NumberFormatException ex) {
    }
    try {
        //precioCompraConcepto = Double.parseDouble(request.getParameter("precioCompraConcepto")!=null?new String(request.getParameter("precioCompraConcepto").getBytes("ISO-8859-1"),"UTF-8"):"");
        precioCompraConcepto = Float.parseFloat(request.getParameter("precioCompraConcepto") != null ? new String(request.getParameter("precioCompraConcepto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (NumberFormatException ex) {
    }
    loteConcepto = request.getParameter("loteConcepto") != null ? new String(request.getParameter("loteConcepto").getBytes("ISO-8859-1"), "UTF-8") : "";
    try {
        stockConcepto = Double.parseDouble(request.getParameter("stockConcepto") != null ? new String(request.getParameter("stockConcepto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (NumberFormatException ex) {
    }
    descrCortaConcepto = request.getParameter("descrCortaConcepto") != null ? new String(request.getParameter("descrCortaConcepto").getBytes("ISO-8859-1"), "UTF-8") : "Sin descripción";
    try {
        idAlmacenConcepto = Integer.parseInt(request.getParameter("almacenConcepto"));
    } catch (NumberFormatException ex) {
    }
    try {
        stockMinimoConcepto = Double.parseDouble(request.getParameter("stockMinimoConcepto") != null ? new String(request.getParameter("stockMinimoConcepto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (NumberFormatException ex) {
    }
    try {
        avisoStockConcepto = Short.parseShort(request.getParameter("avisoStockConcepto") != null ? new String(request.getParameter("avisoStockConcepto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (NumberFormatException ex) {
    }
    detalleConcepto = request.getParameter("detalleConcepto") != null ? new String(request.getParameter("detalleConcepto").getBytes("ISO-8859-1"), "UTF-8") : "";
    try {
        volumenConcepto = Double.parseDouble(request.getParameter("volumenConcepto") != null ? new String(request.getParameter("volumenConcepto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (NumberFormatException ex) {
    }
    try {
        pesoConcepto = Double.parseDouble(request.getParameter("pesoConcepto") != null ? new String(request.getParameter("pesoConcepto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (NumberFormatException ex) {
    }
    Date fecha_caducidad = null;
    try {
        fecha_caducidad = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fecha_caducidad"));
    } catch (Exception e) {
    }
    observacionesConcepto = request.getParameter("observacionesConcepto") != null ? new String(request.getParameter("observacionesConcepto").getBytes("ISO-8859-1"), "UTF-8") : "";

    try {
        estatus = Integer.parseInt(request.getParameter("estatus"));
    } catch (NumberFormatException ex) {
    }

    try {
        maxMenudeo = Double.parseDouble(request.getParameter("maxPrecioMenudeoConcepto") != null ? new String(request.getParameter("maxPrecioMenudeoConcepto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (NumberFormatException ex) {
    }
    try {
        minMedioMayoreo = Double.parseDouble(request.getParameter("minPrecioMedioConcepto") != null ? new String(request.getParameter("minPrecioMedioConcepto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (NumberFormatException ex) {
    }
    try {
        maxMedioMayoreo = Double.parseDouble(request.getParameter("maxPrecioMedioConcepto") != null ? new String(request.getParameter("maxPrecioMedioConcepto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (NumberFormatException ex) {
    }
    try {
        minMayoreo = Double.parseDouble(request.getParameter("minPrecioMayoreoConcepto") != null ? new String(request.getParameter("minPrecioMayoreoConcepto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (NumberFormatException ex) {
    }
    try {
        precioMinimoVenta = Double.parseDouble(request.getParameter("precioMinimoVenta") != null ? new String(request.getParameter("precioMinimoVenta").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (NumberFormatException ex) {
    }

    String nombreArchivoImagen = request.getParameter("nombreArchivoImagen") != null ? new String(request.getParameter("nombreArchivoImagen").getBytes("ISO-8859-1"), "UTF-8") : "";
    String caracteristicasConcepto = request.getParameter("caracteristicasConcepto") != null ? new String(request.getParameter("caracteristicasConcepto").getBytes("ISO-8859-1"), "UTF-8") : "";
    String rfcEmpresa = request.getParameter("rfcEmpresa") != null ? new String(request.getParameter("rfcEmpresa").getBytes("ISO-8859-1"), "UTF-8") : "";

    double porcentajeComision = 0;
    double montoComision = 0;
    try {
        porcentajeComision = Double.parseDouble(request.getParameter("porcentajeComision") != null ? new String(request.getParameter("porcentajeComision").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (NumberFormatException ex) {
    }

    try {
        montoComision = Double.parseDouble(request.getParameter("montoComision") != null ? new String(request.getParameter("montoComision").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (NumberFormatException ex) {
    }

    claveConcepto = request.getParameter("claveConcepto") != null ? new String(request.getParameter("claveConcepto").getBytes("ISO-8859-1"), "UTF-8") : "";

    try {
        desglosePiezas = Double.parseDouble(request.getParameter("desglosePiezas") != null ? new String(request.getParameter("desglosePiezas").getBytes("ISO-8859-1"), "UTF-8") : "1");
    } catch (NumberFormatException ex) {
    }

    try {
        stockInicial = Double.parseDouble(request.getParameter("stockInicial") != null ? new String(request.getParameter("stockInicial").getBytes("ISO-8859-1"), "UTF-8") : "0");
    } catch (NumberFormatException ex) {
    }
    try {
        idAlmacenInicial = Integer.parseInt(request.getParameter("idAlmacen") != null ? new String(request.getParameter("idAlmacen").getBytes("ISO-8859-1"), "UTF-8") : "0");
    } catch (NumberFormatException ex) {
    }
    
    //precios a granel:
    float precioConceptoGranelUnitario = 0;
    try {
        precioConceptoGranelUnitario = Float.parseFloat(request.getParameter("precioConceptoGranelUnitario") != null ? new String(request.getParameter("precioConceptoGranelUnitario").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (NumberFormatException ex) {}
    float precioConceptoGranelMedio = 0;
    try {
        precioConceptoGranelMedio = Float.parseFloat(request.getParameter("precioConceptoGranelMedio") != null ? new String(request.getParameter("precioConceptoGranelMedio").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (NumberFormatException ex) {}
    float precioConceptoGranelMayoreo = 0;
    try {
        precioConceptoGranelMayoreo = Float.parseFloat(request.getParameter("precioConceptoGranelMayoreo") != null ? new String(request.getParameter("precioConceptoGranelMayoreo").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (NumberFormatException ex) {}
    float precioConceptoGranelEspecial = 0;
    try {
        precioConceptoGranelEspecial = Float.parseFloat(request.getParameter("precioConceptoGranelEspecial") != null ? new String(request.getParameter("precioConceptoGranelEspecial").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (NumberFormatException ex) {}
    
    try {
        materiaPrima = Integer.parseInt(request.getParameter("materia_prima"));
    } catch (NumberFormatException ex) {
    }
    try {
        chkCodigoBDefecto = Integer.parseInt(request.getParameter("chk_codigob_defecto"));
    } catch (NumberFormatException ex) {
    }

    /*
     * Validaciones del servidor
     */
    System.out.println("--------MODO: " + mode);

    String msgError = "";
    if (!mode.equals("2") && !mode.equals("3") && !mode.equals("combos") && !mode.equals("addConceptoSesion")
            && !mode.equals("removeConceptoSesion") && !mode.equals("precioEspecialCliente") && !mode.equals("selectClientesPrecio")
            && !mode.equals("desactivarConcepto") && !mode.equals("cambiarOrdenamiento")
            && !mode.equals("desencriptarConceptos") ) {

        GenericValidator gc = new GenericValidator();
        if (!gc.isValidString(nombreConcepto, 1, 100)) {
            msgError += "<ul>El dato 'nombre' es requerido. Maximo 100 caracteres. ";
        }
        if (!mode.equals("express")){
            if (!gc.isValidString(descripcion, 1, 2000)) {
                msgError += "<ul>El dato 'descripción' es requerido. Maximo 2000 caracteres. ";
            }
        }
        if (precioConcepto <= 0) {
            msgError += "<ul>El dato 'Precio Unitario' es requerido. ";
        }
        if (precioDocenaConcepto < 0) {
            msgError += "<ul>El dato 'Precio Docena' debe ser mayor a cero. ";
        }
        if (precioMedioMayoreoConcepto < 0) {
            msgError += "<ul>El dato 'Precio Medio Mayoreo' debe ser mayor a cero. ";
        }
        if (precioMayoreoConcepto < 0) {
            msgError += "<ul>El dato 'Precio Mayoreo' debe ser mayor a cero. ";
        }
        if (precioEspecialConcepto < 0) {
            msgError += "<ul>El dato 'Precio Especial' es requerido. ";
        }
        if (idConcepto <= 0 && !mode.equals("") && !mode.equals("express")) {
            msgError += "<ul>El dato ID 'Concepto' es requerido. ";
        }
        if (precioMinimoVenta < 0) {
            msgError += "<ul>El dato 'Precio Minimo de Venta' debe ser mayor a cero. ";
        }

        if (precioMedioMayoreoConcepto > 0 && precioMayoreoConcepto == 0) {
            if (maxMenudeo >= minMedioMayoreo || maxMenudeo >= maxMedioMayoreo
                    || minMedioMayoreo > maxMedioMayoreo) {
                msgError += "<ul>Revise los limites de precio Medio Mayoreo .";
            }
        }
        if (precioMedioMayoreoConcepto == 0 && precioMayoreoConcepto > 0) {
            if (maxMenudeo >= minMayoreo) {
                msgError += "<ul>Revise los limites de precio Mayoreo. ";
            }
        }

        if (precioMedioMayoreoConcepto > 0 && precioMayoreoConcepto > 0) {
            if (maxMenudeo >= minMedioMayoreo || maxMenudeo >= maxMedioMayoreo
                    || minMedioMayoreo > maxMedioMayoreo
                    || minMedioMayoreo >= minMayoreo || maxMedioMayoreo >= minMayoreo) {
                msgError += "<ul>Revise los limites de precios. ";
            }
        }

        if (!gc.isValidString(claveConcepto, 0, 2000)) {
            msgError += "<ul>El dato 'Clave' es requerido. Maximo 10 caracteres. ";
        }

        if (desglosePiezas <= 0) {
            msgError += "<ul>El dato 'Desglose de piezas' es requerido. ";
        }
        if (stockInicial < 0) {
            msgError += "<ul>El dato 'Stock Inicial' debe ser mayor a 0. ";
        } else if (stockInicial > 0) {
            if (idAlmacenInicial <= 0) {
                msgError += "<ul>Seleccione un 'Almacén' Destino para el Stock Inicial.";
            }
        }

    }
    if (msgError.equals("")) {
        if (idConcepto > 0) {
            if (mode.equals("1")) {
                /*
                 * Editar
                 */
                ConceptoBO conceptoBO = new ConceptoBO(idConcepto, user.getConn());
                Concepto conceptoDto = conceptoBO.getConcepto();

                conceptoDto.setIdEstatus(estatus);
                conceptoDto.setNombre(conceptoBO.encripta(nombreConcepto));
                conceptoDto.setDescripcion(descripcion);
                conceptoDto.setPrecio(precioConcepto);
                conceptoDto.setPrecioDocena(precioDocenaConcepto);
                conceptoDto.setPrecioMedioMayoreo(precioMedioMayoreoConcepto);
                conceptoDto.setPrecioMayoreo(precioMayoreoConcepto);
                conceptoDto.setPrecioEspecial(precioEspecialConcepto);
                conceptoDto.setMaxMenudeo(maxMenudeo);
                conceptoDto.setMinMedioMayoreo(minMedioMayoreo);
                conceptoDto.setMaxMedioMayoreo(maxMedioMayoreo);
                conceptoDto.setMinMayoreo(minMayoreo);
                conceptoDto.setIdentificacion(skuConcepto);
                conceptoDto.setIdCategoria(idCategoriaConcepto);
                conceptoDto.setIdSubcategoria(idSubCategoriaConcepto);
                conceptoDto.setIdSubcategoria2(idSubCategoria2Concepto);
                conceptoDto.setIdSubcategoria3(idSubCategoria3Concepto);
                conceptoDto.setIdSubcategoria4(idSubCategoria4Concepto);
                conceptoDto.setIdMarca(idMarcaConcepto);
                conceptoDto.setIdEmbalaje(idEmbalajeConcepto);
                conceptoDto.setIdImpuesto(idImpuestoConcepto);
                conceptoDto.setPrecioCompra(precioCompraConcepto);
                conceptoDto.setNumeroLote(loteConcepto);
                conceptoDto.setDescripcionCorta(descrCortaConcepto);
                conceptoDto.setIdAlmacen(idAlmacenConcepto);
                conceptoDto.setStockMinimo(stockMinimoConcepto);
                conceptoDto.setStockAvisoMin(avisoStockConcepto);
                conceptoDto.setDetalle(detalleConcepto);
                conceptoDto.setVolumen(volumenConcepto);
                conceptoDto.setPeso(pesoConcepto);
                conceptoDto.setObservaciones(observacionesConcepto);
                conceptoDto.setPrecioMinimoVenta(precioMinimoVenta);
                conceptoDto.setFechaCaducidad(fecha_caducidad);
                conceptoDto.setNombreDesencriptado(nombreConcepto);

                Configuration appConfig = new Configuration();
                String rutaImagen = appConfig.getApp_content_path() + rfcEmpresa + "/" + "ImagenConcepto/";
                if (!nombreArchivoImagen.trim().equals("")) {
                    conceptoDto.setRutaImagen(rutaImagen + nombreArchivoImagen);
                    conceptoDto.setImagenNombreArchivo(nombreArchivoImagen);
                    conceptoDto.setImagenCarpetaArchivo(rutaImagen);
                }
                conceptoDto.setCaracteristiscas(caracteristicasConcepto);

                conceptoDto.setComisionPorcentaje(porcentajeComision);
                conceptoDto.setComisionMonto(montoComision);

                conceptoDto.setSincronizacionMicrosip(2);

                conceptoDto.setClave(claveConcepto);
                conceptoDto.setDesglosePiezas(desglosePiezas);
                
                conceptoDto.setPrecioUnitarioGranel(precioConceptoGranelUnitario);
                conceptoDto.setPrecioMedioGranel(precioConceptoGranelMedio);
                conceptoDto.setPrecioMayoreoGranel(precioConceptoGranelMayoreo);
                conceptoDto.setPrecioEspecialGranel(precioConceptoGranelEspecial);
                
                conceptoDto.setMateriaPrima(materiaPrima);
                
                if (chkCodigoBDefecto==1){
                    String codigoBarrasDefecto = conceptoDto.getIdConcepto() + "-" + conceptoDto.getIdEmpresa();
                    conceptoDto.setIdentificacion(codigoBarrasDefecto);
                }

                try {
                    new ConceptoDaoImpl(user.getConn()).update(conceptoDto.createPk(), conceptoDto);

                    //en caso de tener Sub-Productos (p. ej. A Granel) se les actualiza cierta información
                    // como rangos de precios y precios especiales
                    conceptoBO.actualizaDatosSubProductos(conceptoDto);
                            
                    out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
                } catch (Exception ex) {
                    out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                    ex.printStackTrace();
                }

            } else if (mode.equals("2")) {
                System.out.println("___________ID DEL IMPUESTO: " + idImpuestoConceptoB);
                if (idImpuestoConceptoB > 0) {
                    System.out.println("___________ID DEL IMPUESTO: " + idImpuestoConceptoB);
                    //VALIDAMOS SI EL IMPUESTO YA ESTA RELACIONADO:
                    ImpuestoPorConcepto[] impuestoPorConceptos = new ImpuestoPorConcepto[0];
                    ImpuestoPorConceptoBO conceptoBO = new ImpuestoPorConceptoBO(user.getConn());
                    impuestoPorConceptos = conceptoBO.findImpuestoPorConceptos(0, 0, 0, 0, " AND ID_IMPUESTO = " + idImpuestoConceptoB + " AND ID_CONCEPTO = " + idConcepto);

                    if (impuestoPorConceptos.length <= 0) {
                        //AGREGAMOS LOS IMPUESTOS A LA TABLA DE IMPUESTOS POR CONCETO:
                        ImpuestoPorConcepto ipc = new ImpuestoPorConcepto();
                        ipc.setIdConcepto(idConcepto);
                        ipc.setIdImpuesto(idImpuestoConceptoB);
                        ipc.setIdEstatus(1);
                        try {
                            ImpuestoPorConceptoDaoImpl impuestoPorConceptoDaoImpl = new ImpuestoPorConceptoDaoImpl(user.getConn());
                            impuestoPorConceptoDaoImpl.insert(ipc);
                            out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
                        } catch (Exception ex) {
                            out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                        }
                    } else {
                        for (ImpuestoPorConcepto porConcepto : impuestoPorConceptos) {
                            porConcepto.setIdEstatus(1);
                            try {
                                new ImpuestoPorConceptoDaoImpl(user.getConn()).update(porConcepto.createPk(), porConcepto);
                            } catch (Exception ex) {
                                out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                            }
                        }
                    }
                    //EL CONCEPTO TIENE YA UN IMPUESTO; CAMBIAMOS ID_IMPUESTO_X_CONCEPTO EN TABLA CONCEPTO:
                    ConceptoBO conceptoBO2 = new ConceptoBO(idConcepto, user.getConn());
                    Concepto conceptoDto = conceptoBO2.getConcepto();
                    conceptoDto.setImpuestoXConcepto(1);//0 Excepto de impuesto, 1 Con impuesto relacionado.

                    try {
                        new ConceptoDaoImpl(user.getConn()).update(conceptoDto.createPk(), conceptoDto);
                        out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
                    } catch (Exception ex) {
                        out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                        ex.printStackTrace();
                    }
                }
            } else if (mode.equals("3")) {//ELIMINAR IMPUESTO POR CONCEPTO:
                int idImpuesto = -1;
                try {
                    idImpuesto = Integer.parseInt(request.getParameter("idImpuesto"));
                } catch (NumberFormatException ex) {
                }

                System.out.println("------------VALORES: ID IMPUESTO: " + idImpuesto + ", ID CONCEPTO: " + idConcepto);

                ImpuestoPorConcepto[] impuestoPorConceptos = new ImpuestoPorConcepto[0];
                ImpuestoPorConcepto impuesto = new ImpuestoPorConcepto();
                ImpuestoPorConceptoBO conceptoBO = new ImpuestoPorConceptoBO(user.getConn());
                impuestoPorConceptos = conceptoBO.findImpuestoPorConceptos(0, 0, 0, 0, " AND ID_IMPUESTO = " + idImpuesto + " AND ID_CONCEPTO = " + idConcepto);

                System.out.println("------TAMAÑO DEL ARRAY: " + impuestoPorConceptos.length);

                if (impuestoPorConceptos.length > 0) {
                    impuesto = impuestoPorConceptos[0];
                    impuesto.setIdEstatus(2);
                    try {
                        new ImpuestoPorConceptoDaoImpl(user.getConn()).update(impuesto.createPk(), impuesto);
                    } catch (Exception ex) {
                    }
                }
                //VALIDAMOS SI EL CONCEPTO TIENE AUN IMPUESTOS RELACIONADOS:
                ImpuestoPorConcepto[] impuestoPorConceptos2 = new ImpuestoPorConcepto[0];
                impuestoPorConceptos2 = conceptoBO.findImpuestoPorConceptos(0, 0, 0, 0, " AND ID_CONCEPTO = " + idConcepto + " AND ID_ESTATUS != 2 ");
                if (impuestoPorConceptos2.length <= 0) {
                    Concepto concepto = new Concepto();
                    concepto = new ConceptoBO(idConcepto, user.getConn()).getConcepto();
                    concepto.setImpuestoXConcepto(0);
                    new ConceptoDaoImpl(user.getConn()).update(concepto.createPk(), concepto);
                }

                out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
            } else if (mode.equals("addConceptoSesion")) {   // agrega concepto a sesion             

                //Obtenemos la sesion actual
                HttpSession sesion = request.getSession();
                ArrayList<Concepto> carrito;
                //Si no existe la sesion creamos al carrito de cmoras
                if (sesion.getAttribute("carrito") == null) {
                    carrito = new ArrayList<Concepto>();
                } else {
                    carrito = (ArrayList<Concepto>) sesion.getAttribute("carrito");
                }

                ConceptoBO conceptoBO = new ConceptoBO(idConcepto, user.getConn());
                Concepto conceptoDto = conceptoBO.getConcepto();
                if (!carrito.contains(conceptoDto)) { //Evitamos repetición de elmentos
                    carrito.add(conceptoDto);

                    //Actualizamos la sesion del carrito de compras
                    sesion.setAttribute("carrito", carrito);

                    //System.out.println("car --->" + carrito.size());
                    out.print("<!--EXITO-->" + carrito.size());

                } else {
                    out.print("<!--ERROR-->El concepto ya existe en el Inventario.");
                }

            } else if (mode.equals("removeConceptoSesion")) {   // quita concepto a sesion             

                //Obtenemos la sesion actual
                HttpSession sesion = request.getSession();
                ArrayList<Concepto> carrito = null;
                //Si no existe la sesion creamos al carrito de cmoras
                if (sesion.getAttribute("carrito") != null) {
                    carrito = (ArrayList<Concepto>) sesion.getAttribute("carrito");
                }

                ConceptoBO conceptoBO = new ConceptoBO(idConcepto, user.getConn());
                Concepto conceptoDto = conceptoBO.getConcepto();
                if (carrito.contains(conceptoDto)) { //Evitamos repetición de elmentos
                    carrito.remove(conceptoDto);

                    //Actualizamos la sesion del carrito de compras
                    sesion.setAttribute("carrito", carrito);

                    //System.out.println("car --->" + carrito.size());
                    out.print("<!--EXITO-->Concepto Eliminado Correctamente");

                } else {
                    out.print("<!--ERROR-->El concepto no existe en el Camión.");
                }

            } else if (mode.equals("precioEspecialCliente")) {

                double precioEspecialCliente = 0;
                String listaClientes = "";
                String tipoPrecio = "";

                try {
                    precioEspecialCliente = Double.parseDouble(request.getParameter("precioEspecialCliente") != null ? new String(request.getParameter("precioEspecialCliente").getBytes("ISO-8859-1"), "UTF-8") : "0");
                } catch (NumberFormatException ex) {
                }
                listaClientes = request.getParameter("listaClientes") != null ? new String(request.getParameter("listaClientes").getBytes("ISO-8859-1"), "UTF-8") : "";
                tipoPrecio = request.getParameter("tipoPrecio") != null ? new String(request.getParameter("tipoPrecio").getBytes("ISO-8859-1"), "UTF-8") : "";

                if (precioEspecialCliente <= 0) {
                    msgError += "<ul>El dato 'Precio' debe ser mayor a cero. ";
                }
                if (idConcepto <= 0 && (!mode.equals(""))) {
                    msgError += "<ul>El dato ID 'Concepto' es requerido. ";
                }
                if (tipoPrecio.equals("")) {
                    msgError += "<ul>Acción no Valida.";
                }

                if (msgError.equals("")) {

                    String CadenaConTokens = listaClientes;
                    String extracionDeTokens = "";
                    StringTokenizer tokens = new StringTokenizer(CadenaConTokens, ",");
                        
                    String filtroTipoPrecio = "";
                    

                    if(tipoPrecio.equals("Especial")){
                        filtroTipoPrecio = " PRECIO_ESPECIAL_CLIENTE " ;                       
                    }else if(tipoPrecio.equals("Unitario")){
                        filtroTipoPrecio = " PRECIO_UNITARIO_CLIENTE  " ;                        
                    }else if(tipoPrecio.equals("Medio Mayoreo")){
                        filtroTipoPrecio = " PRECIO_MEDIO_CLIENTE " ;                      
                    }else if(tipoPrecio.equals("Mayoreo")){
                        filtroTipoPrecio = " PRECIO_MAYOREO_CLIENTE " ;                        
                    }else if(tipoPrecio.equals("Docena")){
                        filtroTipoPrecio = " PRECIO_DOCENA_CLIENTE " ;                        
                    }else if(tipoPrecio.equals("UnitarioGranel")){
                        filtroTipoPrecio = " PRECIO_UNITARIO_GRANEL_CLIENTE  " ;
                    }else if(tipoPrecio.equals("MedioGranel")){
                        filtroTipoPrecio = " PRECIO_MEDIO_GRANEL_CLIENTE  " ;
                    }else if(tipoPrecio.equals("MayoreoGranel")){
                        filtroTipoPrecio = " PRECIO_MAYOREO_GRANEL_CLIENTE  " ;
                    }else if(tipoPrecio.equals("EspecialGranel")){
                        filtroTipoPrecio = " PRECIO_ESPECIAL_GRANEL_CLIENTE  " ;
                    }
                    
                    //desactivamos todos los existentes
                    ClientePrecioConceptoBO clientPrecioEspecialBO = new ClientePrecioConceptoBO(user.getConn());
                    ClientePrecioConcepto[] clientePrecioConceptoDto = clientPrecioEspecialBO.findClienteConceptos(-1, idConcepto, idEmpresa, -1, -1, " AND ID_ESTATUS = 1 AND " + filtroTipoPrecio +" = "+ precioEspecialCliente);
                    ClientePrecioConcepto clientPrecioEspecial = null;

                    if (tipoPrecio.equals("Especial")) {
                        for(ClientePrecioConcepto item: clientePrecioConceptoDto){
                            
                            clientPrecioEspecial = new ClientePrecioConceptoBO(item.getIdCliente(), idConcepto, user.getConn()).getClientePrecioConcepto();
                            clientPrecioEspecial.setPrecioEspecialCliente(0);

                            try {
                                new ClientePrecioConceptoDaoImpl(user.getConn()).update(clientPrecioEspecial.createPk(), clientPrecioEspecial);
                                //out.print("<!--EXITO-->Registro actualizado satisfactoriamente.<br/>");
                            } catch (Exception e) {
                                e.printStackTrace();
                                msgError += "Ocurrio un error al Actualizar el registro: " + e.toString();

                            }

                        }

                    }else if(tipoPrecio.equals("Unitario")){
                        for(ClientePrecioConcepto item: clientePrecioConceptoDto){
                            clientPrecioEspecial = new ClientePrecioConceptoBO(item.getIdCliente(), idConcepto, user.getConn()).getClientePrecioConcepto();
                            clientPrecioEspecial.setPrecioUnitarioCliente(0);

                            try {
                                new ClientePrecioConceptoDaoImpl(user.getConn()).update(clientPrecioEspecial.createPk(), clientPrecioEspecial);
                                //out.print("<!--EXITO-->Registro actualizado satisfactoriamente.<br/>");
                            } catch (Exception e) {
                                e.printStackTrace();
                                msgError += "Ocurrio un error al Actualizar el registro: " + e.toString();

                            }

                        }                    
                    }else if (tipoPrecio.equals("Medio Mayoreo")) {
                        for(ClientePrecioConcepto item: clientePrecioConceptoDto){
                            clientPrecioEspecial = new ClientePrecioConceptoBO(item.getIdCliente(), idConcepto, user.getConn()).getClientePrecioConcepto();
                            clientPrecioEspecial.setPrecioMedioCliente(0);

                            try {
                                new ClientePrecioConceptoDaoImpl(user.getConn()).update(clientPrecioEspecial.createPk(), clientPrecioEspecial);
                                //out.print("<!--EXITO-->Registro actualizado satisfactoriamente.<br/>");
                            } catch (Exception e) {
                                e.printStackTrace();
                                msgError += "Ocurrio un error al Actualizar el registro: " + e.toString();

                            }

                        }

                    }else if (tipoPrecio.equals("Mayoreo")) {
                       
                        for(ClientePrecioConcepto item: clientePrecioConceptoDto){
                            
                            clientPrecioEspecial = new ClientePrecioConceptoBO(item.getIdCliente(), idConcepto, user.getConn()).getClientePrecioConcepto();
                            clientPrecioEspecial.setPrecioMayoreoCliente(0);

                            try {
                                new ClientePrecioConceptoDaoImpl(user.getConn()).update(clientPrecioEspecial.createPk(), clientPrecioEspecial);
                                //out.print("<!--EXITO-->Registro actualizado satisfactoriamente.<br/>");
                            } catch (Exception e) {
                                e.printStackTrace();
                                msgError += "Ocurrio un error al Actualizar el registro: " + e.toString();

                            }

                        }

                    }else if (tipoPrecio.equals("Docena")) {
                        for(ClientePrecioConcepto item: clientePrecioConceptoDto){
                            clientPrecioEspecial = new ClientePrecioConceptoBO(item.getIdCliente(), idConcepto, user.getConn()).getClientePrecioConcepto();
                            clientPrecioEspecial.setPrecioDocenaCliente(0);

                            try {
                                new ClientePrecioConceptoDaoImpl(user.getConn()).update(clientPrecioEspecial.createPk(), clientPrecioEspecial);
                                //out.print("<!--EXITO-->Registro actualizado satisfactoriamente.<br/>");
                            } catch (Exception e) {
                                e.printStackTrace();
                                msgError += "Ocurrio un error al Actualizar el registro: " + e.toString();

                            }

                        }

                    }else if (tipoPrecio.equals("UnitarioGranel")) {
                        for(ClientePrecioConcepto item: clientePrecioConceptoDto){
                            
                            clientPrecioEspecial = new ClientePrecioConceptoBO(item.getIdCliente(), idConcepto, user.getConn()).getClientePrecioConcepto();
                            clientPrecioEspecial.setPrecioUnitarioGranelCliente(0);

                            try {
                                new ClientePrecioConceptoDaoImpl(user.getConn()).update(clientPrecioEspecial.createPk(), clientPrecioEspecial);
                                //out.print("<!--EXITO-->Registro actualizado satisfactoriamente.<br/>");
                            } catch (Exception e) {
                                e.printStackTrace();
                                msgError += "Ocurrio un error al Actualizar el registro: " + e.toString();

                            }

                        }

                    }else if (tipoPrecio.equals("MedioGranel")) {
                        for(ClientePrecioConcepto item: clientePrecioConceptoDto){
                            
                            clientPrecioEspecial = new ClientePrecioConceptoBO(item.getIdCliente(), idConcepto, user.getConn()).getClientePrecioConcepto();
                            clientPrecioEspecial.setPrecioMedioGranelCliente(0);

                            try {
                                new ClientePrecioConceptoDaoImpl(user.getConn()).update(clientPrecioEspecial.createPk(), clientPrecioEspecial);
                                //out.print("<!--EXITO-->Registro actualizado satisfactoriamente.<br/>");
                            } catch (Exception e) {
                                e.printStackTrace();
                                msgError += "Ocurrio un error al Actualizar el registro: " + e.toString();

                            }

                        }

                    }else if (tipoPrecio.equals("MayoreoGranel")) {
                        for(ClientePrecioConcepto item: clientePrecioConceptoDto){
                            
                            clientPrecioEspecial = new ClientePrecioConceptoBO(item.getIdCliente(), idConcepto, user.getConn()).getClientePrecioConcepto();
                            clientPrecioEspecial.setPrecioMayoreoGranelCliente(0);

                            try {
                                new ClientePrecioConceptoDaoImpl(user.getConn()).update(clientPrecioEspecial.createPk(), clientPrecioEspecial);
                                //out.print("<!--EXITO-->Registro actualizado satisfactoriamente.<br/>");
                            } catch (Exception e) {
                                e.printStackTrace();
                                msgError += "Ocurrio un error al Actualizar el registro: " + e.toString();

                            }

                        }

                    }else if (tipoPrecio.equals("EspecialGranel")) {
                        for(ClientePrecioConcepto item: clientePrecioConceptoDto){
                            
                            clientPrecioEspecial = new ClientePrecioConceptoBO(item.getIdCliente(), idConcepto, user.getConn()).getClientePrecioConcepto();
                            clientPrecioEspecial.setPrecioEspecialGranelCliente(0);

                            try {
                                new ClientePrecioConceptoDaoImpl(user.getConn()).update(clientPrecioEspecial.createPk(), clientPrecioEspecial);
                                //out.print("<!--EXITO-->Registro actualizado satisfactoriamente.<br/>");
                            } catch (Exception e) {
                                e.printStackTrace();
                                msgError += "Ocurrio un error al Actualizar el registro: " + e.toString();

                            }

                        }

                    }
                    
                    //Actualiza e inserta
                    if(!CadenaConTokens.equals("")){                    
                                           
                        while (tokens.hasMoreTokens()) {
                            extracionDeTokens = tokens.nextToken().intern().trim();
                            //System.out.println("TOKENS: "+extracionDeTokens);
                            int idCliente = 0;
                            clientPrecioEspecial = null;
                            if (!extracionDeTokens.equals("") && extracionDeTokens != null) {

                                try {
                                    idCliente = Integer.parseInt(extracionDeTokens);
                                } catch (Exception e) {
                                    System.out.println("No se pudo parsear idCliente");
                                    e.printStackTrace();
                                }

                                clientPrecioEspecial = new ClientePrecioConceptoBO(idCliente, idConcepto, user.getConn()).getClientePrecioConcepto();

                                if (tipoPrecio.equals("Especial")) {
                                    

                                    if (clientPrecioEspecial != null) {//ya existe
                                        clientPrecioEspecial.setPrecioEspecialCliente(precioEspecialCliente);
                                        clientPrecioEspecial.setIdEstatus(1);

                                        try {
                                            new ClientePrecioConceptoDaoImpl(user.getConn()).update(clientPrecioEspecial.createPk(), clientPrecioEspecial);
                                            //out.print("<!--EXITO-->Registro actualizado satisfactoriamente.<br/>");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            msgError += "Ocurrio un error al Actualizar el registro: " + e.toString();

                                        }

                                    } else {//nuevo
                                        clientPrecioEspecial = new ClientePrecioConcepto();
                                        clientPrecioEspecial.setIdCliente(idCliente);
                                        clientPrecioEspecial.setIdConcepto(idConcepto);
                                        clientPrecioEspecial.setPrecioEspecialCliente(precioEspecialCliente);
                                        clientPrecioEspecial.setIdEstatus(1);

                                        try {
                                            new ClientePrecioConceptoDaoImpl(user.getConn()).insert(clientPrecioEspecial);
                                            //out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            msgError += "Ocurrio un error al guardar el registro: " + e.toString();
                                        }

                                    }

                                }else if (tipoPrecio.equals("Unitario")) {
                                    
                                    if (clientPrecioEspecial != null) {//ya existe
                                        clientPrecioEspecial.setPrecioUnitarioCliente(precioEspecialCliente);
                                        clientPrecioEspecial.setIdEstatus(1);

                                        try {
                                            new ClientePrecioConceptoDaoImpl(user.getConn()).update(clientPrecioEspecial.createPk(), clientPrecioEspecial);
                                            //out.print("<!--EXITO-->Registro actualizado satisfactoriamente.<br/>");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            msgError += "Ocurrio un error al Actualizar el registro: " + e.toString();

                                        }

                                    } else {//nuevo
                                        clientPrecioEspecial = new ClientePrecioConcepto();
                                        clientPrecioEspecial.setIdCliente(idCliente);
                                        clientPrecioEspecial.setIdConcepto(idConcepto);
                                        clientPrecioEspecial.setPrecioUnitarioCliente(precioEspecialCliente);
                                        clientPrecioEspecial.setIdEstatus(1);

                                        try {
                                            new ClientePrecioConceptoDaoImpl(user.getConn()).insert(clientPrecioEspecial);
                                            //out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            msgError += "Ocurrio un error al guardar el registro: " + e.toString();
                                        }

                                    }
                                    
                                }if (tipoPrecio.equals("Medio Mayoreo")) {
                                    

                                    if (clientPrecioEspecial != null) {//ya existe
                                        clientPrecioEspecial.setPrecioMedioCliente(precioEspecialCliente);
                                        clientPrecioEspecial.setIdEstatus(1);

                                        try {
                                            new ClientePrecioConceptoDaoImpl(user.getConn()).update(clientPrecioEspecial.createPk(), clientPrecioEspecial);
                                            //out.print("<!--EXITO-->Registro actualizado satisfactoriamente.<br/>");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            msgError += "Ocurrio un error al Actualizar el registro: " + e.toString();

                                        }

                                    } else {//nuevo
                                        clientPrecioEspecial = new ClientePrecioConcepto();
                                        clientPrecioEspecial.setIdCliente(idCliente);
                                        clientPrecioEspecial.setIdConcepto(idConcepto);
                                        clientPrecioEspecial.setPrecioMedioCliente(precioEspecialCliente);
                                        clientPrecioEspecial.setIdEstatus(1);

                                        try {
                                            new ClientePrecioConceptoDaoImpl(user.getConn()).insert(clientPrecioEspecial);
                                            //out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            msgError += "Ocurrio un error al guardar el registro: " + e.toString();
                                        }

                                    }

                                }else if (tipoPrecio.equals("Mayoreo")) {
                                    

                                    if (clientPrecioEspecial != null) {//ya existe
                                        clientPrecioEspecial.setPrecioMayoreoCliente(precioEspecialCliente);
                                        clientPrecioEspecial.setIdEstatus(1);

                                        try {
                                            new ClientePrecioConceptoDaoImpl(user.getConn()).update(clientPrecioEspecial.createPk(), clientPrecioEspecial);
                                            //out.print("<!--EXITO-->Registro actualizado satisfactoriamente.<br/>");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            msgError += "Ocurrio un error al Actualizar el registro: " + e.toString();

                                        }

                                    } else {//nuevo
                                        clientPrecioEspecial = new ClientePrecioConcepto();
                                        clientPrecioEspecial.setIdCliente(idCliente);
                                        clientPrecioEspecial.setIdConcepto(idConcepto);
                                        clientPrecioEspecial.setPrecioMayoreoCliente(precioEspecialCliente);
                                        clientPrecioEspecial.setIdEstatus(1);

                                        try {
                                            new ClientePrecioConceptoDaoImpl(user.getConn()).insert(clientPrecioEspecial);
                                            //out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            msgError += "Ocurrio un error al guardar el registro: " + e.toString();
                                        }

                                    }

                                }else if (tipoPrecio.equals("Docena")) {
                                    

                                    if (clientPrecioEspecial != null) {//ya existe
                                        clientPrecioEspecial.setPrecioDocenaCliente(precioEspecialCliente);
                                        clientPrecioEspecial.setIdEstatus(1);

                                        try {
                                            new ClientePrecioConceptoDaoImpl(user.getConn()).update(clientPrecioEspecial.createPk(), clientPrecioEspecial);
                                            //out.print("<!--EXITO-->Registro actualizado satisfactoriamente.<br/>");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            msgError += "Ocurrio un error al Actualizar el registro: " + e.toString();

                                        }

                                    } else {//nuevo
                                        clientPrecioEspecial = new ClientePrecioConcepto();
                                        clientPrecioEspecial.setIdCliente(idCliente);
                                        clientPrecioEspecial.setIdConcepto(idConcepto);
                                        clientPrecioEspecial.setPrecioDocenaCliente(precioEspecialCliente);
                                        clientPrecioEspecial.setIdEstatus(1);

                                        try {
                                            new ClientePrecioConceptoDaoImpl(user.getConn()).insert(clientPrecioEspecial);
                                            //out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            msgError += "Ocurrio un error al guardar el registro: " + e.toString();
                                        }

                                    }

                                }else if (tipoPrecio.equals("UnitarioGranel")) {
                                    

                                    if (clientPrecioEspecial != null) {//ya existe
                                        clientPrecioEspecial.setPrecioUnitarioGranelCliente(precioEspecialCliente);
                                        clientPrecioEspecial.setIdEstatus(1);

                                        try {
                                            new ClientePrecioConceptoDaoImpl(user.getConn()).update(clientPrecioEspecial.createPk(), clientPrecioEspecial);
                                            //out.print("<!--EXITO-->Registro actualizado satisfactoriamente.<br/>");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            msgError += "Ocurrio un error al Actualizar el registro: " + e.toString();

                                        }

                                    } else {//nuevo
                                        clientPrecioEspecial = new ClientePrecioConcepto();
                                        clientPrecioEspecial.setIdCliente(idCliente);
                                        clientPrecioEspecial.setIdConcepto(idConcepto);
                                        clientPrecioEspecial.setPrecioUnitarioGranelCliente(precioEspecialCliente);
                                        clientPrecioEspecial.setIdEstatus(1);

                                        try {
                                            new ClientePrecioConceptoDaoImpl(user.getConn()).insert(clientPrecioEspecial);
                                            //out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            msgError += "Ocurrio un error al guardar el registro: " + e.toString();
                                        }

                                    }

                                }else if (tipoPrecio.equals("MedioGranel")) {
                                    

                                    if (clientPrecioEspecial != null) {//ya existe
                                        clientPrecioEspecial.setPrecioMedioGranelCliente(precioEspecialCliente);
                                        clientPrecioEspecial.setIdEstatus(1);

                                        try {
                                            new ClientePrecioConceptoDaoImpl(user.getConn()).update(clientPrecioEspecial.createPk(), clientPrecioEspecial);
                                            //out.print("<!--EXITO-->Registro actualizado satisfactoriamente.<br/>");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            msgError += "Ocurrio un error al Actualizar el registro: " + e.toString();

                                        }

                                    } else {//nuevo
                                        clientPrecioEspecial = new ClientePrecioConcepto();
                                        clientPrecioEspecial.setIdCliente(idCliente);
                                        clientPrecioEspecial.setIdConcepto(idConcepto);
                                        clientPrecioEspecial.setPrecioMedioGranelCliente(precioEspecialCliente);
                                        clientPrecioEspecial.setIdEstatus(1);

                                        try {
                                            new ClientePrecioConceptoDaoImpl(user.getConn()).insert(clientPrecioEspecial);
                                            //out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            msgError += "Ocurrio un error al guardar el registro: " + e.toString();
                                        }

                                    }

                                }else if (tipoPrecio.equals("MayoreoGranel")) {
                                    

                                    if (clientPrecioEspecial != null) {//ya existe
                                        clientPrecioEspecial.setPrecioMayoreoGranelCliente(precioEspecialCliente);
                                        clientPrecioEspecial.setIdEstatus(1);

                                        try {
                                            new ClientePrecioConceptoDaoImpl(user.getConn()).update(clientPrecioEspecial.createPk(), clientPrecioEspecial);
                                            //out.print("<!--EXITO-->Registro actualizado satisfactoriamente.<br/>");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            msgError += "Ocurrio un error al Actualizar el registro: " + e.toString();

                                        }

                                    } else {//nuevo
                                        clientPrecioEspecial = new ClientePrecioConcepto();
                                        clientPrecioEspecial.setIdCliente(idCliente);
                                        clientPrecioEspecial.setIdConcepto(idConcepto);
                                        clientPrecioEspecial.setPrecioMayoreoGranelCliente(precioEspecialCliente);
                                        clientPrecioEspecial.setIdEstatus(1);

                                        try {
                                            new ClientePrecioConceptoDaoImpl(user.getConn()).insert(clientPrecioEspecial);
                                            //out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            msgError += "Ocurrio un error al guardar el registro: " + e.toString();
                                        }

                                    }

                                }else if (tipoPrecio.equals("EspecialGranel")) {
                                    

                                    if (clientPrecioEspecial != null) {//ya existe
                                        clientPrecioEspecial.setPrecioEspecialGranelCliente(precioEspecialCliente);
                                        clientPrecioEspecial.setIdEstatus(1);

                                        try {
                                            new ClientePrecioConceptoDaoImpl(user.getConn()).update(clientPrecioEspecial.createPk(), clientPrecioEspecial);
                                            //out.print("<!--EXITO-->Registro actualizado satisfactoriamente.<br/>");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            msgError += "Ocurrio un error al Actualizar el registro: " + e.toString();

                                        }

                                    } else {//nuevo
                                        clientPrecioEspecial = new ClientePrecioConcepto();
                                        clientPrecioEspecial.setIdCliente(idCliente);
                                        clientPrecioEspecial.setIdConcepto(idConcepto);
                                        clientPrecioEspecial.setPrecioEspecialGranelCliente(precioEspecialCliente);
                                        clientPrecioEspecial.setIdEstatus(1);

                                        try {
                                            new ClientePrecioConceptoDaoImpl(user.getConn()).insert(clientPrecioEspecial);
                                            //out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            msgError += "Ocurrio un error al guardar el registro: " + e.toString();
                                        }

                                    }

                                }

                            }

                        }
                    }
                    
                    if(msgError.equals("")){//SI llega aqui todo lo hizo bien
                        out.print("<!--EXITO-->Registro actualizado satisfactoriamente.<br/>");
                    }else{
                        out.print("<!--ERROR-->" + msgError); 
                    }
                     
                    
                } else {
                    out.print("<!--ERROR-->" + msgError);
                }

            }else if(mode.equals("selectClientesPrecio")){
                
                double precio = 0;
                String tipoPrecio = "";
                try {
                    precio = Double.parseDouble(request.getParameter("precio") != null ? new String(request.getParameter("precio").getBytes("ISO-8859-1"), "UTF-8") : "0");
                } catch (NumberFormatException ex) {}
                tipoPrecio = request.getParameter("tipoPrecio") != null ? new String(request.getParameter("tipoPrecio").getBytes("ISO-8859-1"), "UTF-8") : "";
                
                String precioSelect = "";
                if (tipoPrecio.equals("Especial")) {
                    precioSelect  = "PRECIO_ESPECIAL_CLIENTE";
                }else if(tipoPrecio.equals("Unitario")){
                    precioSelect  = "PRECIO_UNITARIO_CLIENTE";
                }else if(tipoPrecio.equals("Medio Mayoreo")){
                    precioSelect  = "PRECIO_MEDIO_CLIENTE";
                }else if(tipoPrecio.equals("Mayoreo")){
                    precioSelect  = "PRECIO_MAYOREO_CLIENTE";
                }else if(tipoPrecio.equals("Docena")){
                    precioSelect  = "PRECIO_DOCENA_CLIENTE";
                }else if(tipoPrecio.equals("UnitarioGranel")){
                    precioSelect = " PRECIO_UNITARIO_GRANEL_CLIENTE  " ;
                }else if(tipoPrecio.equals("MedioGranel")){
                    precioSelect = " PRECIO_MEDIO_GRANEL_CLIENTE  " ;
                }else if(tipoPrecio.equals("MayoreoGranel")){
                    precioSelect = " PRECIO_MAYOREO_GRANEL_CLIENTE  " ;
                }else if(tipoPrecio.equals("EspecialGranel")){
                    precioSelect = " PRECIO_ESPECIAL_GRANEL_CLIENTE  " ;
                }else{
                    precioSelect  = "PRECIO_ESPECIAL_CLIENTE";
                }
                
                ClientePrecioConceptoBO clientPrecioEspecialBO = new ClientePrecioConceptoBO(user.getConn());
                ClientePrecioConcepto[] preciosEspecialesDto = new ClientePrecioConcepto[0];
               

                preciosEspecialesDto = clientPrecioEspecialBO.findClienteConceptos(-1, idConcepto, idEmpresa, -1, -1, " AND ID_ESTATUS = 1 AND "+ precioSelect+" = " + precio);
               
                    if (msgError.equals("")){
                        if (preciosEspecialesDto.length>0){
                             out.print("<!--EXITO-->");
        
                            for (ClientePrecioConcepto cli : preciosEspecialesDto){
                                Cliente item2 = new ClienteBO(cli.getIdCliente(), user.getConn()).getCliente();
                                            
                                String nombreCliente = ""; 
                                
                                
                                if(nombreCliente.equals("")){
                                    nombreCliente = item2.getRazonSocial();
                                }
                                if(nombreCliente.equals("")){
                                    nombreCliente = item2.getNombreComercial();
                                }
                                
                                
                                if(nombreCliente.equals("")){
                                    if(item2.getNombreCliente()!=null){
                                        nombreCliente += item2.getNombreCliente();
                                    }
                                    if((item2.getApellidoPaternoCliente()!=null)&&(!item2.getApellidoPaternoCliente().toUpperCase().equals("NULL"))&&(!item2.getApellidoPaternoCliente().toUpperCase().equals("CAMPO POR LLENAR"))){
                                        nombreCliente += " " + item2.getApellidoPaternoCliente();
                                    }
                                    if((item2.getApellidoMaternoCliente()!=null)&&(!item2.getApellidoMaternoCliente().toUpperCase().equals("NULL"))&&(!item2.getApellidoMaternoCliente().toUpperCase().equals("CAMPO POR LLENAR"))){
                                        nombreCliente += " " + item2.getApellidoPaternoCliente();
                                    }
                                }
                                
                            %>
                                <option value="<%=item2.getIdCliente()%>"><%=nombreCliente%></option>
                            <%
                            }
                            
                            
                       }else{
                        out.print("<!--ERROR-->Sin clientes.");
                       }
                    }
                
            
            }else if(mode.equals("desactivarConcepto")){
                try{
                    ConceptoBO conceptoBO = new ConceptoBO(idConcepto, user.getConn());
                    Concepto conceptoDto = conceptoBO.getConcepto();
                    if (conceptoDto!=null){
                        conceptoDto.setIdEstatus(2);//deshabilitado
                        new ConceptoDaoImpl(user.getConn()).update(conceptoDto.createPk(), conceptoDto);
                    }else{
                        msgError += "El concepto con ID " + idConcepto +" no fue localizado en base de datos.";
                    }
                }catch(Exception ex){
                    msgError += GenericMethods.exceptionStackTraceToString(ex);
                }
                if (msgError.equals("")){
                    out.print("<!--EXITO-->Registro Actualizado Satisfactoriamente");
                }else{
                    out.print("<!--ERROR-->Encontramos un problema al intentar desactivar el concepto: <br/>" + msgError);
                }
            }else {
                out.print("<!--ERROR-->Acción no válida.");
            }

        } else if (mode.equals("combos")) {

            String idElement = request.getParameter("idElement") != null ? new String(request.getParameter("idElement").getBytes("ISO-8859-1"), "UTF-8") : "0";

            out.print(new CategoriaBO(user.getConn()).getCategoriasByIdHTMLCombo(idEmpresa, -1, " AND id_categoria_padre =" + idElement));
        } else if (mode.equals("cambiarOrdenamiento")) {

            int idOrdenamiento = -1;
            try {
                idOrdenamiento = Integer.parseInt(request.getParameter("idOrdenamiento"));
            } catch (NumberFormatException ex) {
            }
            
            // Asignamos ordenamiento por defecto
            ConceptoBO.Ordenamiento ordenamiento = ConceptoBO.Ordenamiento.ALFABETICO_A_Z;
            //Buscamos el ordenamiento requerido y lo asignamos a sesion
            for (ConceptoBO.Ordenamiento orden : ConceptoBO.Ordenamiento.values()){
                if (orden.getId()==idOrdenamiento)
                    ordenamiento = orden;
            }
            
            user.setOrdenamientoConceptos(ordenamiento);

            out.print("<!--EXITO-->Ordenamiento modificado en sesion.");
            
        }else if (mode.equals("desencriptarConceptos")){
            // desencripta en BD el nombre del concepto, ('nombre' --> 'nombre_desencriptado')
            boolean todos = false; // indica si se desencriptan todos los conceptos de la empresa o solo los que no tengan valor en 'nombre_desencriptado'
            String strTodos = request.getParameter("todos") != null ? new String(request.getParameter("todos").getBytes("ISO-8859-1"), "UTF-8") : "";
            if (strTodos.equals("true")){
                todos = true;
            }
            
            try {
                ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
                conceptoBO.desencriptaActualizaNombresBD(idEmpresa, todos);
                out.print("<!--EXITO-->Actualización de Nombres de Conceptos terminada.");
            }catch(Exception ex){
                out.print("<!--ERROR-->Error al actualizar información de nombres de conceptos (desencriptación): " + GenericMethods.exceptionStackTraceToString(ex));
            }
            
        }else if (mode.equals("express")) {

            /*
             *  Nuevo
             */

            try {

                /**
                 * Creamos el registro de Cliente
                 */
                ConceptoBO conceptoBO = new ConceptoBO(idConcepto, user.getConn());
                Concepto conceptoDto = new Concepto();
                ConceptoDaoImpl conceptosDaoImpl = new ConceptoDaoImpl(user.getConn());

                conceptoDto.setIdEstatus(estatus);
                conceptoDto.setNombre(conceptoBO.encripta(nombreConcepto));
                conceptoDto.setDescripcion(nombreConcepto);
                conceptoDto.setIdEmpresa(idEmpresa);

                conceptoDto.setPrecio(precioConcepto);
                conceptoDto.setPrecioDocena(precioDocenaConcepto);
                conceptoDto.setPrecioMedioMayoreo(precioMedioMayoreoConcepto);

                conceptoDto.setPrecioMayoreo(precioMayoreoConcepto);
                conceptoDto.setPrecioEspecial(precioEspecialConcepto);
                conceptoDto.setMaxMenudeo(maxMenudeo);
                conceptoDto.setMinMedioMayoreo(minMedioMayoreo);
                conceptoDto.setMaxMedioMayoreo(maxMedioMayoreo);
                conceptoDto.setMinMayoreo(minMayoreo);
                conceptoDto.setIdentificacion(skuConcepto);
                conceptoDto.setIdCategoria(idCategoriaConcepto);
                conceptoDto.setIdSubcategoria(idSubCategoriaConcepto);
                conceptoDto.setIdSubcategoria2(idSubCategoria2Concepto);
                conceptoDto.setIdSubcategoria3(idSubCategoria3Concepto);
                conceptoDto.setIdSubcategoria4(idSubCategoria4Concepto);
                conceptoDto.setIdMarca(idMarcaConcepto);
                conceptoDto.setIdEmbalaje(idEmbalajeConcepto);
                conceptoDto.setIdImpuesto(idImpuestoConcepto);
                conceptoDto.setPrecioCompra(precioCompraConcepto);
                conceptoDto.setNumeroLote(loteConcepto);
                conceptoDto.setDescripcionCorta(descrCortaConcepto);
                conceptoDto.setIdAlmacen(idAlmacenConcepto);
                conceptoDto.setStockMinimo(stockMinimoConcepto);
                conceptoDto.setStockAvisoMin(avisoStockConcepto);
                conceptoDto.setDetalle(detalleConcepto);
                conceptoDto.setVolumen(volumenConcepto);
                conceptoDto.setPeso(pesoConcepto);
                conceptoDto.setObservaciones(observacionesConcepto);
                try{
                    conceptoDto.setFechaAlta(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                }catch(Exception e){
                    conceptoDto.setFechaAlta(new Date());
                }
                conceptoDto.setPrecioMinimoVenta(precioMinimoVenta);
                conceptoDto.setFechaCaducidad(fecha_caducidad);
                conceptoDto.setNombreDesencriptado(nombreConcepto);

                Configuration appConfig = new Configuration();
                String rutaImagen = appConfig.getApp_content_path() + rfcEmpresa + "/" + "ImagenConcepto/";
                if (!nombreArchivoImagen.trim().equals("")) {
                    conceptoDto.setRutaImagen(rutaImagen + nombreArchivoImagen);
                    conceptoDto.setImagenNombreArchivo(nombreArchivoImagen);
                    conceptoDto.setImagenCarpetaArchivo(rutaImagen);
                }
                conceptoDto.setCaracteristiscas(caracteristicasConcepto);
                conceptoDto.setComisionPorcentaje(porcentajeComision);
                conceptoDto.setComisionMonto(montoComision);
                conceptoDto.setClave(claveConcepto);
                conceptoDto.setDesglosePiezas(desglosePiezas);
                
                conceptoDto.setPrecioUnitarioGranel(precioConceptoGranelUnitario);
                conceptoDto.setPrecioMedioGranel(precioConceptoGranelMedio);
                conceptoDto.setPrecioMayoreoGranel(precioConceptoGranelMayoreo);
                conceptoDto.setPrecioEspecialGranel(precioConceptoGranelEspecial);
                
                conceptoDto.setMateriaPrima(materiaPrima);

                /**
                 * Realizamos el insert
                 */
                ConceptoPk conceptoInsertado = conceptosDaoImpl.insert(conceptoDto);
                
                if (chkCodigoBDefecto==1){
                    String codigoBarrasDefecto = conceptoDto.getIdConcepto() + "-" + conceptoDto.getIdEmpresa();
                    conceptoDto.setIdentificacion(codigoBarrasDefecto);
                    conceptosDaoImpl.update(conceptoDto.createPk(), conceptoDto);
                }

                if (stockInicial > 0) {

                    //Obtenemos dto concepto insertado
                    Concepto conceptoUltimo = new ConceptoBO(conceptoInsertado.getIdConcepto(), user.getConn()).getConcepto();
                    try {
                        Encrypter encryDesen = new Encrypter();
                        conceptoUltimo.setNombre(encryDesen.decodeString(conceptoUltimo.getNombre()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    /**
                     * Insertamos existencia en almacen correspondiente*
                     */
                    ExistenciaAlmacen almacenExists = new ExistenciaAlmacen();
                    almacenExists.setIdAlmacen(idAlmacenInicial);
                    almacenExists.setIdConcepto(conceptoUltimo.getIdConcepto());
                    almacenExists.setExistencia(stockInicial);
                    almacenExists.setEstatus(1);

                    new ExistenciaAlmacenDaoImpl().insert(almacenExists);

                    /**
                     * Insertamos movimiento entrada inicial*
                     */
                    Movimiento movimientoDto = new Movimiento();
                    MovimientoDaoImpl movimientosDaoImpl = new MovimientoDaoImpl(user.getConn());

                    movimientoDto.setIdEmpresa(idEmpresa);
                    movimientoDto.setTipoMovimiento("ENTRADA");
                    movimientoDto.setNombreProducto(conceptoUltimo.getNombre());
                    movimientoDto.setContabilidad(stockInicial);
                    movimientoDto.setIdProveedor(-1);
                    movimientoDto.setOrdenCompra("");
                    movimientoDto.setNumeroGuia("");
                    movimientoDto.setIdAlmacen(idAlmacenInicial);
                    movimientoDto.setConceptoMovimiento("Alta de Producto");
                    try{
                        movimientoDto.setFechaRegistro(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                    }catch(Exception e){
                        movimientoDto.setFechaRegistro(new Date());
                    }
                    
                    movimientoDto.setIdConcepto(conceptoUltimo.getIdConcepto());

                    movimientosDaoImpl.insert(movimientoDto);

                }

                out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");

            } catch (Exception e) {
                e.printStackTrace();
                msgError += "Ocurrio un error al guardar el registro: " + e.toString();
            }
        

        }else {
            /*
             *  Nuevo
             */

            try {

                /**
                 * Creamos el registro de Cliente
                 */
                ConceptoBO conceptoBO = new ConceptoBO(idConcepto, user.getConn());
                Concepto conceptoDto = new Concepto();
                ConceptoDaoImpl conceptosDaoImpl = new ConceptoDaoImpl(user.getConn());

                conceptoDto.setIdEstatus(estatus);
                conceptoDto.setNombre(conceptoBO.encripta(nombreConcepto));
                conceptoDto.setDescripcion(descripcion);
                conceptoDto.setIdEmpresa(idEmpresa);

                conceptoDto.setPrecio(precioConcepto);
                conceptoDto.setPrecioDocena(precioDocenaConcepto);
                conceptoDto.setPrecioMedioMayoreo(precioMedioMayoreoConcepto);
                conceptoDto.setPrecioMayoreo(precioMayoreoConcepto);
                conceptoDto.setPrecioEspecial(precioEspecialConcepto);
                conceptoDto.setMaxMenudeo(maxMenudeo);
                conceptoDto.setMinMedioMayoreo(minMedioMayoreo);
                conceptoDto.setMaxMedioMayoreo(maxMedioMayoreo);
                conceptoDto.setMinMayoreo(minMayoreo);
                conceptoDto.setIdentificacion(skuConcepto);
                conceptoDto.setIdCategoria(idCategoriaConcepto);
                conceptoDto.setIdSubcategoria(idSubCategoriaConcepto);
                conceptoDto.setIdSubcategoria2(idSubCategoria2Concepto);
                conceptoDto.setIdSubcategoria3(idSubCategoria3Concepto);
                conceptoDto.setIdSubcategoria4(idSubCategoria4Concepto);
                conceptoDto.setIdMarca(idMarcaConcepto);
                conceptoDto.setIdEmbalaje(idEmbalajeConcepto);
                conceptoDto.setIdImpuesto(idImpuestoConcepto);
                conceptoDto.setPrecioCompra(precioCompraConcepto);
                conceptoDto.setNumeroLote(loteConcepto);
                conceptoDto.setDescripcionCorta(descrCortaConcepto);
                conceptoDto.setIdAlmacen(idAlmacenConcepto);
                conceptoDto.setStockMinimo(stockMinimoConcepto);
                conceptoDto.setStockAvisoMin(avisoStockConcepto);
                conceptoDto.setDetalle(detalleConcepto);
                conceptoDto.setVolumen(volumenConcepto);
                conceptoDto.setPeso(pesoConcepto);
                conceptoDto.setObservaciones(observacionesConcepto);
                try{
                    conceptoDto.setFechaAlta(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                }catch(Exception e){
                    conceptoDto.setFechaAlta(new Date());
                }
                conceptoDto.setPrecioMinimoVenta(precioMinimoVenta);
                conceptoDto.setFechaCaducidad(fecha_caducidad);
                conceptoDto.setNombreDesencriptado(nombreConcepto);

                Configuration appConfig = new Configuration();
                String rutaImagen = appConfig.getApp_content_path() + rfcEmpresa + "/" + "ImagenConcepto/";
                if (!nombreArchivoImagen.trim().equals("")) {
                    conceptoDto.setRutaImagen(rutaImagen + nombreArchivoImagen);
                    conceptoDto.setImagenNombreArchivo(nombreArchivoImagen);
                    conceptoDto.setImagenCarpetaArchivo(rutaImagen);
                }
                conceptoDto.setCaracteristiscas(caracteristicasConcepto);
                conceptoDto.setComisionPorcentaje(porcentajeComision);
                conceptoDto.setComisionMonto(montoComision);
                conceptoDto.setClave(claveConcepto);
                conceptoDto.setDesglosePiezas(desglosePiezas);
                
                conceptoDto.setPrecioUnitarioGranel(precioConceptoGranelUnitario);
                conceptoDto.setPrecioMedioGranel(precioConceptoGranelMedio);
                conceptoDto.setPrecioMayoreoGranel(precioConceptoGranelMayoreo);
                conceptoDto.setPrecioEspecialGranel(precioConceptoGranelEspecial);
                
                conceptoDto.setMateriaPrima(materiaPrima);

                /**
                 * Realizamos el insert
                 */
                ConceptoPk conceptoInsertado = conceptosDaoImpl.insert(conceptoDto);
                
                if (chkCodigoBDefecto==1){
                    String codigoBarrasDefecto = conceptoDto.getIdConcepto() + "-" + conceptoDto.getIdEmpresa();
                    conceptoDto.setIdentificacion(codigoBarrasDefecto);
                    conceptosDaoImpl.update(conceptoDto.createPk(), conceptoDto);
                }

                if (stockInicial > 0) {

                    //Obtenemos dto concepto insertado
                    Concepto conceptoUltimo = new ConceptoBO(conceptoInsertado.getIdConcepto(), user.getConn()).getConcepto();
                    try {
                        Encrypter encryDesen = new Encrypter();
                        conceptoUltimo.setNombre(encryDesen.decodeString(conceptoUltimo.getNombre()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    /**
                     * Insertamos existencia en almacen correspondiente*
                     */
                    ExistenciaAlmacen almacenExists = new ExistenciaAlmacen();
                    almacenExists.setIdAlmacen(idAlmacenInicial);
                    almacenExists.setIdConcepto(conceptoUltimo.getIdConcepto());
                    almacenExists.setExistencia(stockInicial);
                    almacenExists.setEstatus(1);

                    new ExistenciaAlmacenDaoImpl().insert(almacenExists);

                    /**
                     * Insertamos movimiento entrada inicial*
                     */
                    Movimiento movimientoDto = new Movimiento();
                    MovimientoDaoImpl movimientosDaoImpl = new MovimientoDaoImpl(user.getConn());

                    movimientoDto.setIdEmpresa(idEmpresa);
                    movimientoDto.setTipoMovimiento("ENTRADA");
                    movimientoDto.setNombreProducto(conceptoUltimo.getNombre());
                    movimientoDto.setContabilidad(stockInicial);
                    movimientoDto.setIdProveedor(-1);
                    movimientoDto.setOrdenCompra("");
                    movimientoDto.setNumeroGuia("");
                    movimientoDto.setIdAlmacen(idAlmacenInicial);
                    movimientoDto.setConceptoMovimiento("Alta de Producto");
                    try{
                        movimientoDto.setFechaRegistro(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                    }catch(Exception e){
                        movimientoDto.setFechaRegistro(new Date());
                    }
                    movimientoDto.setIdConcepto(conceptoUltimo.getIdConcepto());

                    movimientosDaoImpl.insert(movimientoDto);

                }

                out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");

            } catch (Exception e) {
                e.printStackTrace();
                msgError += "Ocurrio un error al guardar el registro: " + e.toString();
            }
        }
    } else {
        out.print("<!--ERROR-->" + msgError);
    }

%>