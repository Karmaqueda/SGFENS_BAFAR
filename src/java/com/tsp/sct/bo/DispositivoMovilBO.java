/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.DispositivoMovil;
import com.tsp.sct.dao.exceptions.DispositivoMovilDaoException;
import com.tsp.sct.dao.jdbc.DispositivoMovilDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class DispositivoMovilBO {
    
    private DispositivoMovil dispositivoMovil = null;

    public DispositivoMovil getDispositivoMovil() {
        return dispositivoMovil;
    }

    public void setDispositivoMovil(DispositivoMovil dispositivoMovil) {
        this.dispositivoMovil = dispositivoMovil;
    }

    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public DispositivoMovilBO(Connection conn){
        this.conn = conn;
    }
    
    public DispositivoMovilBO(int idDispositivoMovil, Connection conn){
        this.conn = conn;
         try{
            DispositivoMovilDaoImpl DispositivoMovilDaoImpl = new DispositivoMovilDaoImpl(this.conn);
            this.dispositivoMovil = DispositivoMovilDaoImpl.findByPrimaryKey(idDispositivoMovil);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public DispositivoMovil findDispositivoMovilbyId(int idDispositivoMovil) throws Exception{
        DispositivoMovil dispositivoMovil = null;
        
        try{
            DispositivoMovilDaoImpl dispositivoMovilDaoImpl = new DispositivoMovilDaoImpl(this.conn);
            dispositivoMovil = dispositivoMovilDaoImpl.findByPrimaryKey(idDispositivoMovil);
            if (dispositivoMovil==null){
                throw new Exception("No se encontro ningun Dispositivo Movil que corresponda con los parámetros específicados.");
            }
            if (dispositivoMovil.getIdDispositivo()<=0){
                throw new Exception("No se encontro ningun Dispositivo Movil que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de un Servicio. Error: " + e.getMessage());
        }
        
        return dispositivoMovil;
    }
    
    public DispositivoMovil getDispositivoMovilGenericoByEmpresa(int idEmpresa) throws Exception{
        DispositivoMovil dispositivoMovil = null;
        
        try{
            DispositivoMovilDaoImpl dispositivoMovilDaoImpl = new DispositivoMovilDaoImpl(this.conn);
            dispositivoMovil = dispositivoMovilDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (dispositivoMovil==null){
                throw new Exception("La empresa no tiene creado algun Dispositivo Movil");
            }
        }catch(DispositivoMovilDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creado algun Dispositivo Movil");
        }
        
        return dispositivoMovil;
    }
    
    /**
     * Realiza una búsqueda por ID Servicio en busca de
     * coincidencias
     * @param idDispositivoMovil ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar DispositivoMovil, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Servicio
     */
    public DispositivoMovil[] findDispositivosMoviles(int idDispositivoMovil, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        DispositivoMovil[] dispositivoMovilDto = new DispositivoMovil[0];
        DispositivoMovilDaoImpl dispositivoMovilDao = new DispositivoMovilDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idDispositivoMovil>0){
                sqlFiltro ="ID_DISPOSITIVO=" + idDispositivoMovil + " AND ID_ESTATUS = 1 AND ";
            }else{
                sqlFiltro ="ID_DISPOSITIVO>0 AND ID_ESTATUS = 1 AND ";
            }
            if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            }else{
                sqlFiltro +=" ID_EMPRESA>0";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            dispositivoMovilDto = dispositivoMovilDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY IMEI ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return dispositivoMovilDto;
    }
    
    public String getDispositivosMovilesByIdHTMLCombo(int idEmpresa, long idSeleccionado){
        String strHTMLCombo ="";
        try{
            DispositivoMovil[] DispositivosMovilesDto = findDispositivosMoviles(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (DispositivoMovil dispositivoMovilItem:DispositivosMovilesDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==dispositivoMovilItem.getIdDispositivo())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+dispositivoMovilItem.getIdDispositivo()+"' "
                            + selectedStr
                            + "title='"+dispositivoMovilItem.getImei()+"'>"
                            + ""+dispositivoMovilItem.getAliasTelefono()+", IMEI: "  +  dispositivoMovilItem.getImei()
                            +"</option>";
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return strHTMLCombo;
    }
            
}
