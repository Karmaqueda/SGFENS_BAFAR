/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.bo;

import com.google.gson.Gson;
import com.tsp.sct.bo.EmpleadoBO;
import com.tsp.sct.bo.PaqueteBO;
import com.tsp.sct.dao.dto.Paquete;
import com.tsp.sct.dao.dto.PaqueteRelacionConcepto;
import com.tsp.sct.dao.jdbc.PaqueteDaoImpl;
import com.tsp.sct.dao.jdbc.PaqueteRelacionConceptoDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.util.StringManage;
import com.tsp.sgfens.ws.request.EmpleadoDtoRequest;
import com.tsp.sgfens.ws.response.ConsultaPaquetesResponse;
import com.tsp.sgfens.ws.response.WsItemPaquete;
import com.tsp.sgfens.ws.response.WsItemPaqueteConcepto;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MOVILPYME
 */
public class PaquetesWsBO {
    private final Gson gson = new Gson();
    private Connection conn = null;
    
    public Connection getConn() {
        if (this.conn==null){
            try {
                this.conn = ResourceManager.getConnection();
            } catch (SQLException ex) {}
        }else{
            try {
                if (this.conn.isClosed()){
                    this.conn = ResourceManager.getConnection();
                }
            } catch (SQLException ex) {}
        }
        return this.conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public ConsultaPaquetesResponse getPaquetesByEmpleado(String empleadoDtoRequestJSON){
        ConsultaPaquetesResponse response;
        
        EmpleadoDtoRequest empleadoDtoRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            
            response = this.getPaquetesByEmpleado(empleadoDtoRequest);
        }catch(Exception ex){
            response = new ConsultaPaquetesResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return response;
    }
    public ConsultaPaquetesResponse getPaquetesByEmpleado(EmpleadoDtoRequest empleadoDtoRequest){
        ConsultaPaquetesResponse response = new ConsultaPaquetesResponse();
        try{
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                 int idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                 
                 if(idEmpresa > 0){
                    response.setError(false);
                    response.setWsItemPaquete(this.getListaPaquete(idEmpresa, ""));
                 }
                 
                 //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            }else{
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        }catch(Exception ex){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al consultar pedidos del usuario. " + ex.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return response;
    }
    
    private List<WsItemPaquete> getListaPaquete(int idEmpresa, String filtroAdicional){
        List<WsItemPaquete> listaPaquetes = new ArrayList<WsItemPaquete>();
        
        PaqueteBO paqueteBO = new PaqueteBO(getConn());
        try{
            String filtroBusqueda = "";
             if (StringManage.getValidString(filtroAdicional).length()>0)
                filtroBusqueda += " " + filtroAdicional;
            Paquete[] paqueteDtoArray = paqueteBO.findPaquetes(-1, idEmpresa, 0, 0, filtroAdicional);
            
            PaqueteDaoImpl paqueteDaoImpl = new PaqueteDaoImpl(getConn());
            PaqueteRelacionConceptoDaoImpl paqueteRelacionConceptoDaoimpl = new PaqueteRelacionConceptoDaoImpl(getConn());
            
            for(Paquete paqueteDto : paqueteDtoArray){
                WsItemPaquete wsItemPaquete = new WsItemPaquete();
                
                wsItemPaquete.setDescripcion(paqueteDto.getDescripcion());
                wsItemPaquete.setIdEmpresa(paqueteDto.getIdEmpresa());
                wsItemPaquete.setIdEstatus(paqueteDto.getIdEstatus());
                wsItemPaquete.setIdPaquete(paqueteDto.getIdPaquete());
                wsItemPaquete.setNombre(paqueteDto.getNombre());
                
                {//Conceptos
                    PaqueteRelacionConcepto[] paqueteConceptoDtoArray = paqueteRelacionConceptoDaoimpl.findWhereIdPaqueteEquals(paqueteDto.getIdPaquete());
                    
                    for(PaqueteRelacionConcepto paqueteConceptoDto : paqueteConceptoDtoArray){
                        WsItemPaqueteConcepto wsItemPaqueteConcepto = new WsItemPaqueteConcepto();
                        wsItemPaqueteConcepto.setCantidad(paqueteConceptoDto.getCantidad());
                        wsItemPaqueteConcepto.setIdConcepto(paqueteConceptoDto.getIdConcepto());
                        wsItemPaqueteConcepto.setIdEstatus(paqueteConceptoDto.getIdEstatus());
                        wsItemPaqueteConcepto.setIdPaquete(paqueteConceptoDto.getIdPaquete());
                        wsItemPaqueteConcepto.setIdPaqueteConcepto(paqueteConceptoDto.getIdPaqueteRelacionConcepto());
                        wsItemPaqueteConcepto.setPrecio(paqueteConceptoDto.getPrecio());
                        
                        wsItemPaquete.getWsItemPaqueteConcepto().add(wsItemPaqueteConcepto);
                    }
                }
                listaPaquetes.add(wsItemPaquete);
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        return listaPaquetes;
    }
}
