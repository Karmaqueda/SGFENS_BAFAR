/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SgfensClienteVendedor;
import com.tsp.sct.dao.jdbc.SgfensClienteVendedorDaoImpl;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ISCesarMartinez
 */
public class SGClienteVendedorBO {
    
    private SgfensClienteVendedor clienteVendedor = null;

    public SgfensClienteVendedor getClienteVendedor() {
        return clienteVendedor;
    }

    public void setClienteVendedor(SgfensClienteVendedor clienteVendedor) {
        this.clienteVendedor = clienteVendedor;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SGClienteVendedorBO(Connection conn){
        this.conn = conn;
    }
    
    public SGClienteVendedorBO(int idCliente, Connection conn){
        this.conn = conn;
        try{
            this.clienteVendedor = new SgfensClienteVendedorDaoImpl(this.conn).findByPrimaryKey(idCliente);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
}
