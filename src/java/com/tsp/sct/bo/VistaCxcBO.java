/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.VistaCxc;
import com.tsp.sct.dao.jdbc.VistaCxcDaoImpl;
import com.tsp.sct.util.DateManage;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author ISCesarMartinez
 */
public class VistaCxcBO {
    private VistaCxc vistaCxc  = null;

    public VistaCxc getVistaCxc() {
        return vistaCxc;
    }

    public void setVistaCxc(VistaCxc vistaCxc) {
        this.vistaCxc = vistaCxc;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public VistaCxcBO(Connection conn){
        this.conn = conn;
    }    
    
    /**
     * Realiza una búsqueda por ID VistaCxc en busca de
     * coincidencias
     * @param idComprobanteFiscal ID Del ComprobanteFiscal para filtrar, -1 para omitir filtro
     * @param idPedido ID Del CxcValeAzul para filtrar, -1 para omitir filtro
     * @param idEmpresa ID de la Empresa a filtrar vistaCxc, -1 para evitar filtro
     * @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO VistaCxc
     */
    public VistaCxc[] findCxc(int idComprobanteFiscal, int idPedido, int idEmpresa, int minLimit,int maxLimit, boolean filtroConAdeudos, String filtroBusqueda) {
        VistaCxc[] cxcDto = new VistaCxc[0];
        VistaCxcDaoImpl cxcDao = new VistaCxcDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            
            if (filtroConAdeudos){
                sqlFiltro += "IF(ID_PEDIDO IS NOT NULL, "
                        + "     (TOTAL-SALDO_PAGADO)>0, "
                        + "     IF ( (SELECT COUNT(A1.ID_COBRANZA_ABONO) FROM sgfens_cobranza_abono A1 WHERE A1.ID_COMPROBANTE_FISCAL = C_ID_COMPROBANTE_FISCAL)>0,"
                        + "             ( IMPORTE_NETO - (SELECT SUM(A.MONTO_ABONO) FROM sgfens_cobranza_abono A WHERE A.ID_COMPROBANTE_FISCAL = C_ID_COMPROBANTE_FISCAL))>0,"
                        + "              IMPORTE_NETO>0 "
                        + "         )"
                        + " ) AND ";
            }
            
            if (idComprobanteFiscal>0){
                sqlFiltro +=" C_ID_COMPROBANTE_FISCAL=" + idComprobanteFiscal + " AND ";
            }else if (idPedido>0){
                sqlFiltro +=" ID_PEDIDO=" + idPedido + " AND ";
            }else{
                //sqlFiltro +=" (C_ID_COMPROBANTE_FISCAL>0 OR ID_PEDIDO>0) AND ";
            }
            if (idEmpresa>0){
                sqlFiltro += " ( C_ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ") ";
                sqlFiltro += " OR ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ") )";
            }else{
                sqlFiltro +=" (C_ID_EMPRESA>0 OR ID_EMPRESA>0) ";
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
            String orderByEspecial = " IF(C_ID_COMPROBANTE_FISCAL IS NOT NULL, DATEDIFF(FECHA_PAGO,NOW()), DATEDIFF(FECHA_TENTATIVA_PAGO,NOW()) ) ASC " 
                                    //+ " , IF(C_ID_COMPROBANTE_FISCAL IS NOT NULL, (total - cf_importe_pagado), (importe - importe_pagado) ) DESC "
                                    ;
            
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
     * con los archivos que corresponden  al arreglo de CxCComprobantes Fiscales y Pedidos que se recibe
     * @param idsComprobanteFiscal Arreglo de Strings con los ID de los Comprobantes Fiscales de CxC que se descargaran
     * @param idsPedido Arreglo de Strings con los ID de los Vales Azules que se descargaran
     * @param xml Indica si se quiere descargar los archivos XML correspondientes de Comprobante Fiscal
     * @param pdf Indica si se quiere descargar los archivos PDF correspondientes de Comprobante Fiscal
     * @return ByteArrayOutputStream con el archivo zip generado
     */
    public ByteArrayOutputStream getZipFromFiles(String[] idsComprobanteFiscal, String[] idsPedido, boolean xml, boolean pdf){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if(idsComprobanteFiscal.length>0 || idsPedido.length>0){
            ComprobanteFiscalBO compFBO;
            SGPedidoBO sGPedidoBO;
            
            ZipOutputStream zipOut = new ZipOutputStream(baos);

            //Primero agregamos todos los CxC Comprobante Fiscales
            for (String itemId : idsComprobanteFiscal) {
                if (StringManage.getValidString(itemId).equals(""))
                    continue;
                try {
                    compFBO = new ComprobanteFiscalBO(Integer.parseInt(itemId), conn);
                    if (compFBO.getComprobanteFiscal()!=null){
                        if (xml){ //XML
                            //Obtenemos la ruta del archivo
                            File fileXML = compFBO.getComprobanteFiscalXML();

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
                        if (pdf){ //XML
                            //Obtenemos la ruta del archivo
                            File filePDF = compFBO.getComprobanteFiscalPDF();

                            FileInputStream in = new FileInputStream(filePDF);

                            //Agregamos la referencia de una entrada al ZIP
                            ZipEntry entry = new ZipEntry(filePDF.getName());
                            zipOut.putNextEntry(entry);

                            //buffer de lectura
                            byte[] b = new byte[(int)filePDF.length()];

                            //Leeemos el archivo y lo copiamos al zip
                            int len = 0;
                            while ((len = in.read(b)) != -1) {
                                    zipOut.write(b, 0, len);
                            }
                            //cerramos la entrada
                            zipOut.closeEntry();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            
            //Después agregamos todos los CxC Pedidos
            for (String itemId : idsPedido) {
                if (StringManage.getValidString(itemId).equals(""))
                    continue;
                try {
                    sGPedidoBO = new SGPedidoBO(Integer.parseInt(itemId), conn);
                    if (sGPedidoBO.getPedido()!=null){
                        //Obtenemos la ruta del archivo
                        ByteArrayOutputStream baosTemp = sGPedidoBO.toPdf(null);
                        String nombreArchivo = "Pretoriano_ID" + sGPedidoBO.getPedido().getIdPedido()+ "_Pedido.pdf";

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
    
    /**
     * Calcula el Saldo restante por Pagar total
     * de todos los pedidos y comprobantes fiscales con estatus diferente de Cancelado
     * @return Saldo Total por cobrar
     */
    public double getTotalPorCobrar(int idEmpresa){
        double saldoTotalPorCobrar = 0;
        
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT (SUM(IF(cxc.ID_PEDIDO>0, cxc.TOTAL, cxc.IMPORTE_NETO)) - SUM(Abonos) ) AS 'saldo_general' "
                        + " FROM vista_cxc AS cxc "
                        + " LEFT JOIN ("
                        + "	SELECT id_comprobante_fiscal, id_pedido, SUM(MONTO_ABONO) Abonos "
                        + "	 FROM sgfens_cobranza_abono "
                        + "	 GROUP BY id_comprobante_fiscal, id_pedido "
                        + " ) AS A ON (IF(cxc.ID_PEDIDO>0, a.ID_PEDIDO = cxc.ID_PEDIDO, a.ID_COMPROBANTE_FISCAL = cxc.C_ID_COMPROBANTE_FISCAL)) "
                        + " WHERE IF(cxc.ID_PEDIDO>0, cxc.ID_ESTATUS_PEDIDO !=3, cxc.C_ID_ESTATUS<4)"
                        + "     AND IF(cxc.ID_PEDIDO>0, cxc.ID_EMPRESA="+idEmpresa+", cxc.C_ID_EMPRESA="+idEmpresa+") ");

            if (rs.next()){
                saldoTotalPorCobrar = rs.getDouble(1);
            }
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
                
        return saldoTotalPorCobrar;
    }
    
    
    public List<ProyeccionFinancieraData> dataForChartProyeccionFinanciera(int idEmpresa, Date filtroFechaMin, Date filtroFechaMax){
        List<ProyeccionFinancieraData> listData = new ArrayList<ProyeccionFinancieraData>();
        
        String buscar_fechamin="";
        String buscar_fechamax="";
        try{
            buscar_fechamin = DateManage.formatDateToSQL(filtroFechaMin);
        }catch(Exception ex){}
        try{
            buscar_fechamax = DateManage.formatDateToSQL(filtroFechaMax);
        }catch(Exception ex){}
        
        String strWhereRangoFechasC = "";
        String strWhereRangoFechasP = "";
        if (filtroFechaMin!=null && filtroFechaMax!=null){
            strWhereRangoFechasC=" (c.fecha BETWEEN '" +buscar_fechamin + "' AND '" + buscar_fechamax + "') ";
            strWhereRangoFechasP=" (p.fecha BETWEEN '" +buscar_fechamin + "' AND '" + buscar_fechamax + "')";
        }
        if (filtroFechaMin!=null && filtroFechaMax==null){
            strWhereRangoFechasC=" (c.fecha  >= '"+buscar_fechamin+"')";
            strWhereRangoFechasP=" (p.fecha  >= '"+buscar_fechamin+"') ";
        }
        if (filtroFechaMin==null && filtroFechaMax!=null){
            strWhereRangoFechasC=" (c.fecha  <= '"+buscar_fechamax+"')";
            strWhereRangoFechasP=" (p.fecha  <= '"+buscar_fechamax+"')";
        }
        
        if (!strWhereRangoFechasC.trim().equals("")){
            strWhereRangoFechasC = " AND " + strWhereRangoFechasC;
        }
        if (!strWhereRangoFechasP.trim().equals("")){
            strWhereRangoFechasP = " AND " + strWhereRangoFechasP;
        }
        
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            String sqlQuery = " SELECT c.Dia, c.Mes, c.Anio, c.Cobrar, p.Pagar, c.fecha "
                    +" FROM vista_cobro_tentativo c " 
                    +"    LEFT JOIN vista_pago_tentativo p ON (c.dia = p.dia AND c.mes = p.mes AND c.anio = p.anio) " 
                    +" WHERE c.cxc_id_empresa=" + idEmpresa
                    +"	" + strWhereRangoFechasC //AND c.fecha BETWEEN '2015-04-25' AND '2015-05-25' " 
                    +" UNION ALL " 
                    +" SELECT p.Dia, p.Mes, p.Anio, c.Cobrar, p.Pagar, p.fecha " 
                    +" FROM vista_cobro_tentativo c " 
                    +"    RIGHT JOIN vista_pago_tentativo p ON (c.dia = p.dia AND c.mes = p.mes AND c.anio = p.anio) " 
                    +" WHERE c.cobrar IS NULL AND p.cxp_id_empresa=" + idEmpresa
                    +"	" + strWhereRangoFechasP//AND p.fecha BETWEEN '2015-04-25' AND '2015-05-25' " 
                    +" ORDER BY fecha";
            rs = stmt.executeQuery(sqlQuery);

            while (rs.next()){
                ProyeccionFinancieraData data = new ProyeccionFinancieraData();
                int dia = rs.getInt(1);
                int mes = rs.getInt(2);
                int anio = rs.getInt(3);
                double cobrar = rs.getDouble(4);
                double pagar = rs.getDouble(5);
                Date fecha = rs.getDate(6);
                
                data.setDia(dia);
                data.setMes(mes);
                data.setAnio(anio);
                data.setCobrar(cobrar);
                data.setPagar(pagar);
                data.setFechaCompuesta(fecha);
                
                data.setUtilidad(cobrar-pagar);
                
                listData.add(data);
            }
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
        
        return listData;
    }
    
    
    public List<ProyeccionFinancieraRangoData> dataForChartProyeccionFinanciera(int idEmpresa, Date filtroFechaBase, int semanasAtras, int semanasAdelante){
        List<ProyeccionFinancieraRangoData> data = new ArrayList<ProyeccionFinancieraRangoData>();
        
        List<DateManage.ParDate> rangosFechas = DateManage.obtenPrimerUltimoDiaSemanas(filtroFechaBase, semanasAtras, semanasAdelante);
        for (DateManage.ParDate rangoF : rangosFechas){
            System.out.println(">>>> Consultando para rango: " + rangoF.getDiaA() + "  -  " + rangoF.getDiaB());
            
            ProyeccionFinancieraRangoData pfrt = new ProyeccionFinancieraRangoData();
            pfrt.setParDias(rangoF);
            
            Statement stmt = null;
            ResultSet rs = null;
            
            String buscar_fechamin="";
            String buscar_fechamax="";
            try{
                buscar_fechamin = DateManage.formatDateToSQL(rangoF.getDiaA());
            }catch(Exception ex){}
            try{
                buscar_fechamax = DateManage.formatDateToSQL(rangoF.getDiaB());
            }catch(Exception ex){}
            
            //Total por Cobrar de Facturas
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT SUM(importe_neto) FROM comprobante_fiscal WHERE id_empresa="+idEmpresa+" AND (CAST(FECHA_PAGO AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')");

                if (rs.next())
                    pfrt.setCobrarFacturas(rs.getDouble(1));
                
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                if (rs != null) { try {rs.close();} catch (SQLException sqlEx) {} rs = null; }
                //if (stmt != null) { try { stmt.close(); } catch (SQLException sqlEx) {} stmt = null; }
            }
            
            //Total por Cobrar de Pedidos
            try {
                //stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT SUM(TOTAL) FROM sgfens_pedido WHERE id_empresa="+idEmpresa+" AND (CAST(FECHA_TENTATIVA_PAGO AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')");

                if (rs.next())
                    pfrt.setCobrarPedidos(rs.getDouble(1));
                
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                if (rs != null) { try {rs.close();} catch (SQLException sqlEx) {} rs = null; }
                //if (stmt != null) { try { stmt.close(); } catch (SQLException sqlEx) {} stmt = null; }
            }
            
            //Total por Pagar de Facturas
            try {
                //stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT SUM(TOTAL) FROM cxp_comprobante_fiscal WHERE id_empresa="+idEmpresa+" AND (CAST(FECHA_TENTATIVA_PAGO AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')");

                if (rs.next())
                    pfrt.setPagarFacturas(rs.getDouble(1));
                
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                if (rs != null) { try {rs.close();} catch (SQLException sqlEx) {} rs = null; }
                //if (stmt != null) { try { stmt.close(); } catch (SQLException sqlEx) {} stmt = null; }
            }
            
            //Total por Pagar de Vales Azules
            try {
                //stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT SUM(IMPORTE) FROM cxp_vale_azul WHERE id_empresa="+idEmpresa+" AND (CAST(FECHA_TENTATIVA_PAGO AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')");

                if (rs.next())
                    pfrt.setPagarValesAzules(rs.getDouble(1));
                
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                if (rs != null) { try {rs.close();} catch (SQLException sqlEx) {} rs = null; }
                if (stmt != null) { try { stmt.close(); } catch (SQLException sqlEx) {} stmt = null; }
            }
            
            //Total CxC
            pfrt.setCobrarTotal(pfrt.getCobrarFacturas() + pfrt.getCobrarPedidos());
            
            // Total CxP
            pfrt.setPagarTotal(pfrt.getPagarFacturas() + pfrt.getPagarValesAzules());
            
            //Total Utilidades =  Total CxC - Total CxP
            pfrt.setUtilidadTotal(pfrt.getCobrarTotal() - pfrt.getPagarTotal());
            
            data.add(pfrt);
        }
        
        return data;
    }
    
    public class ProyeccionFinancieraData{
        
        public int dia;
        public int mes;
        public int anio;
        public Date fechaCompuesta;
        public double cobrar;
        public double pagar;
        public double utilidad;

        public int getDia() {
            return dia;
        }

        public void setDia(int dia) {
            this.dia = dia;
        }

        public int getMes() {
            return mes;
        }

        public void setMes(int mes) {
            this.mes = mes;
        }

        public int getAnio() {
            return anio;
        }

        public void setAnio(int anio) {
            this.anio = anio;
        }

        public Date getFechaCompuesta() {
            return fechaCompuesta;
        }

        public void setFechaCompuesta(Date fechaCompuesta) {
            this.fechaCompuesta = fechaCompuesta;
        }

        public double getCobrar() {
            return cobrar;
        }

        public void setCobrar(double cobrar) {
            this.cobrar = cobrar;
        }

        public double getPagar() {
            return pagar;
        }

        public void setPagar(double pagar) {
            this.pagar = pagar;
        }

        public double getUtilidad() {
            return utilidad;
        }

        public void setUtilidad(double utilidad) {
            this.utilidad = utilidad;
        }
        
    }
    
    public class ProyeccionFinancieraRangoData{
        
        public DateManage.ParDate parDias;
        
        public double cobrarFacturas;
        public double cobrarPedidos;
        public double cobrarTotal;
        
        public double pagarFacturas;
        public double pagarValesAzules;
        public double pagarTotal;
        
        public double utilidadTotal;

        public DateManage.ParDate getParDias() {
            return parDias;
        }

        public void setParDias(DateManage.ParDate parDias) {
            this.parDias = parDias;
        }

        public double getCobrarFacturas() {
            return cobrarFacturas;
        }

        public void setCobrarFacturas(double cobrarFacturas) {
            this.cobrarFacturas = cobrarFacturas;
        }

        public double getCobrarPedidos() {
            return cobrarPedidos;
        }

        public void setCobrarPedidos(double cobrarPedidos) {
            this.cobrarPedidos = cobrarPedidos;
        }

        public double getCobrarTotal() {
            return cobrarTotal;
        }

        public void setCobrarTotal(double cobrarTotal) {
            this.cobrarTotal = cobrarTotal;
        }

        public double getPagarFacturas() {
            return pagarFacturas;
        }

        public void setPagarFacturas(double pagarFacturas) {
            this.pagarFacturas = pagarFacturas;
        }

        public double getPagarValesAzules() {
            return pagarValesAzules;
        }

        public void setPagarValesAzules(double pagarValesAzules) {
            this.pagarValesAzules = pagarValesAzules;
        }

        public double getPagarTotal() {
            return pagarTotal;
        }

        public void setPagarTotal(double pagarTotal) {
            this.pagarTotal = pagarTotal;
        }

        public double getUtilidadTotal() {
            return utilidadTotal;
        }

        public void setUtilidadTotal(double utilidadTotal) {
            this.utilidadTotal = utilidadTotal;
        }
        
    }
    
}
