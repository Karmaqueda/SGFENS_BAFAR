package com.tsp.sct.bo;

import java.sql.Connection;

import com.tsp.sct.dao.dto.MigracionSctEvc;
import com.tsp.sct.dao.jdbc.EmergenciaCreditoDaoImpl;
import com.tsp.sct.dao.jdbc.MigracionSctEvcDaoImpl;



public class MigracionBO {
	
	
	private Connection conn = null;

	private MigracionSctEvc migracion = null;

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}
	 		
	
	public MigracionSctEvc getMigracion() {
		return migracion;
	}

	public void setMigracion(MigracionSctEvc migracion) {
		this.migracion = migracion;
	}
	
	

	public MigracionBO(Connection conn) {
		super();
		this.conn = conn;
	}

	public MigracionBO(int idEmpresa, Connection conn){
        this.conn = conn;
        try{
        	MigracionSctEvcDaoImpl MigracionSctEvcDaoImpl = new MigracionSctEvcDaoImpl(this.conn);
            this.migracion = MigracionSctEvcDaoImpl.findByPrimaryKey(idEmpresa);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
	
	
	public MigracionBO(String idEmpresa, Connection conn){
        this.conn = conn;
        try{
        	MigracionSctEvcDaoImpl MigracionSctEvcDaoImpl = new MigracionSctEvcDaoImpl(this.conn);
            this.migracion = MigracionSctEvcDaoImpl.findByPrimaryKey(Integer.parseInt(idEmpresa));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
	
	
	

}
