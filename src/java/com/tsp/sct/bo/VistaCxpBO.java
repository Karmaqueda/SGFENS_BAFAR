/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.VistaCxp;
import com.tsp.sct.dao.jdbc.VistaCxpDaoImpl;
import com.tsp.sct.util.StringManage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.sql.SQLException;

/**
 *
 * @author ISCesarMartinez
 */
public class VistaCxpBO {
    private VistaCxp vistaCxp  = null;

    public VistaCxp getVistaCxp() {
        return vistaCxp;
    }

    public void setVistaCxp(VistaCxp vistaCxp) {
        this.vistaCxp = vistaCxp;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public VistaCxpBO(Connection conn){
        this.conn = conn;
    }    
    
    /**
     * Realiza una búsqueda por ID VistaCxp en busca de
     * coincidencias
     * @param idCxpComprobanteFiscal ID Del CxpComprobanteFiscal para filtrar, -1 para omitir filtro
     * @param idCxpValeAzul ID Del CxpValeAzul para filtrar, -1 para omitir filtro
     * @param idEmpresa ID de la Empresa a filtrar vistaCxp, -1 para evitar filtro
     * @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO VistaCxp
     */
    public VistaCxp[] findCxp(int idCxpComprobanteFiscal, int idCxpValeAzul, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        VistaCxp[] cxcDto = new VistaCxp[0];
        VistaCxpDaoImpl cxcDao = new VistaCxpDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCxpComprobanteFiscal>0){
                sqlFiltro ="ID_CXP_COMPROBANTE_FISCAL=" + idCxpComprobanteFiscal + " AND ";
            }else if (idCxpValeAzul>0){
                sqlFiltro ="ID_CXP_VALE_AZUL=" + idCxpValeAzul + " AND ";
            }else{
                sqlFiltro ="(ID_CXP_COMPROBANTE_FISCAL>0 OR ID_CXP_VALE_AZUL>0) AND ";
            }
            if (idEmpresa>0){
                sqlFiltro += " ( CF_ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ") ";
                sqlFiltro += " OR ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ") )";
            }else{
                sqlFiltro +=" (CF_ID_EMPRESA>0 OR ID_EMPRESA>0) ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            //Ordenamiento para poder obtener la siguiente forma:
            // Primero todos los que la Fecha de Pago expiro o esta por vencer (orden por días de mora)
            // Después por saldo por pagar (orden por saldo mora)
            String orderByEspecial = " IF(ID_CXP_COMPROBANTE_FISCAL IS NOT NULL, DATEDIFF(CF_FECHA_TENTATIVA_PAGO,NOW()), DATEDIFF(FECHA_TENTATIVA_PAGO,NOW()) ) ASC, " 
                                    + " IF(ID_CXP_COMPROBANTE_FISCAL IS NOT NULL, (total - cf_importe_pagado), (importe - importe_pagado) ) DESC ";
            
            cxcDto = cxcDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY " + orderByEspecial
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return cxcDto;
    }
    
    /**
     * Metodo para crer un ByteArrayOutputStream creando un zip
     * con los archivos que corresponden  al arreglo de CxPComprobantes Fiscales y Vales Azules que se recibe
     * @param idsComprobanteFiscal Arreglo de Strings con los ID de los Comprobantes Fiscales de CxP que se descargaran
     * @param idsValeAzul Arreglo de Strings con los ID de los Vales Azules que se descargaran
     * @param xml Indica si se quiere descargar los archivos XML correspondientes de Comprobante Fiscal
     * @param pdf Indica si se quiere descargar los archivos PDF correspondientes de Comprobante Fiscal
     * @return ByteArrayOutputStream con el archivo zip generado
     */
    public ByteArrayOutputStream getZipFromFiles(String[] idsComprobanteFiscal, String[] idsValeAzul, boolean xml, boolean pdf){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if(idsComprobanteFiscal.length>0){
            CxpComprobanteFiscalBO compFBO;
            ValidacionXmlBO validacionXmlBO;
            CxpValeAzulBO valeAzulBO;
            ZipOutputStream zipOut = new ZipOutputStream(baos);

            //Primero agregamos todos los CxP Comprobante Fiscales
            for (String itemId : idsComprobanteFiscal) {
                if (StringManage.getValidString(itemId).equals(""))
                    continue;
                try {
                    compFBO = new CxpComprobanteFiscalBO(Integer.parseInt(itemId), conn);
                    if (compFBO.getCxpComprobanteFiscal()!=null){
                        validacionXmlBO = new ValidacionXmlBO(compFBO.getCxpComprobanteFiscal().getIdValidacion(), conn);

                        if (xml){ //XML
                            //Obtenemos la ruta del archivo
                            File fileXML = validacionXmlBO.getComprobanteFile();

                            FileInputStream in = new FileInputStream(fileXML);

                            //Agregamos la referencia de una entrada al ZIP
                            ZipEntry entry = new ZipEntry(fileXML.getName());
                            zipOut.putNextEntry(entry);

                            //buffer de lectura
                            byte[] b = new byte[(int)fileXML.length()];

                            //Leeemos el archivo y lo copiamos al zip
                            int len = 0;
                            while ((len = in.read(b)) != -1) {
                                    zipOut.write(b, 0, len);
                            }
                            //cerramos la entrada
                            zipOut.closeEntry();
                        }
                        if (pdf){ //PDf
                            //
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            
            //Después agregamos todos los CxP Vales Azules
            for (String itemId : idsValeAzul) {
                if (StringManage.getValidString(itemId).equals(""))
                    continue;
                try {
                    valeAzulBO = new CxpValeAzulBO(Integer.parseInt(itemId), conn);
                    if (valeAzulBO.getCxpValeAzul()!=null){
                        //Obtenemos la ruta del archivo
                        ByteArrayOutputStream baosTemp = valeAzulBO.toPdf();
                        String nombreArchivo = "Pretoriano_" + valeAzulBO.getCxpValeAzul().getIdCxpValeAzul()+ "_ValeAzul.pdf";

                        //Agregamos la referencia de una entrada al ZIP
                        ZipEntry entry = new ZipEntry(nombreArchivo);
                        byte[] b = baosTemp.toByteArray();
                        entry.setSize(b.length);
                        zipOut.putNextEntry(entry);

                        //Leeemos el archivo y lo copiamos al zip
                        zipOut.write(b);
                        //cerramos la entrada
                        zipOut.closeEntry();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            
            try {
                //cerramos el ZIP
                zipOut.close();
            } catch (IOException ex) {
            }
        }

        return baos;
    }
    
    public VistaCxPTotalPorPagar getTotalesPorPagar(int idEmpresa){
        VistaCxPTotalPorPagar bean = new VistaCxPTotalPorPagar();
        
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT SUM(total - cf_importe_pagado) AS 'suma_cf', SUM(importe - importe_pagado) AS 'suma_va'" 
                        + " FROM vista_cxp "
                        + " WHERE ( (total - cf_importe_pagado)>0 OR (importe - importe_pagado)>0 )"
                        + "  AND (cf_id_empresa IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")"
                        + "     OR id_empresa IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")"
                        + "  ) "
                        + "  AND cf_id_estatus!=2 AND id_estatus!=2  ");

            if (rs.next()){
                bean.setTotalComprobantesFiscales( rs.getDouble(1) );
                bean.setTotalValesAzules(rs.getDouble(2) );
            }
    // Now do something with the ResultSet ....
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {}
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {} // ignore
                stmt = null;
            }
        }
                
        return bean;
    }
    
    public class VistaCxPTotalPorPagar {
        private double totalComprobantesFiscales;
        private double totalValesAzules;

        public double getTotalComprobantesFiscales() {
            return totalComprobantesFiscales;
        }

        public void setTotalComprobantesFiscales(double totalComprobantesFiscales) {
            this.totalComprobantesFiscales = totalComprobantesFiscales;
        }

        public double getTotalValesAzules() {
            return totalValesAzules;
        }

        public void setTotalValesAzules(double totalValesAzules) {
            this.totalValesAzules = totalValesAzules;
        } 
        
    }
    
}
