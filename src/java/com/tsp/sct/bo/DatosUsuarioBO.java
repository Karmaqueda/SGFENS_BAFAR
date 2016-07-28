/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.DatosUsuario;
import com.tsp.sct.dao.exceptions.DatosUsuarioDaoException;
import com.tsp.sct.dao.jdbc.DatosUsuarioDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 * @date 18-Enero-2013
 */
public class DatosUsuarioBO {
 private DatosUsuario datosUsuario = null;

    public DatosUsuario getDatosUsuario() {
        return datosUsuario;
    }

    public void setDatosUsuario(DatosUsuario datosUsuario) {
        this.datosUsuario = datosUsuario;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public DatosUsuarioBO(Connection conn){
        this.conn =conn;
    }
    
    public DatosUsuarioBO(int idDatosUsuario, Connection conn){        
        this.conn = conn;
        try{
            DatosUsuarioDaoImpl DatosUsuarioDaoImpl = new DatosUsuarioDaoImpl(this.conn);
            this.datosUsuario = DatosUsuarioDaoImpl.findByPrimaryKey(idDatosUsuario);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public DatosUsuario findDatosUsuariobyId(int idDatosUsuario) throws Exception{
        DatosUsuario DatosUsuario = null;
        
        try{
            DatosUsuarioDaoImpl DatosUsuarioDaoImpl = new DatosUsuarioDaoImpl(this.conn);
            DatosUsuario = DatosUsuarioDaoImpl.findByPrimaryKey(idDatosUsuario);
            if (DatosUsuario==null){
                throw new Exception("No se encontro ningun Datos Usuario que corresponda con los parámetros específicados.");
            }
            if (DatosUsuario.getIdDatosUsuario()<=0){
                throw new Exception("No se encontro ningun Datos Usuario que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de Datos Usuario del usuario. Error: " + e.getMessage());
        }
        
        return DatosUsuario;
    }
    
    public DatosUsuario getDatosUsuarioGenericoByEmpresa(int idEmpresa) throws Exception{
        DatosUsuario datosUsuario = null;
        
        try{
            DatosUsuarioDaoImpl datosUsuarioDaoImpl = new DatosUsuarioDaoImpl(this.conn);
            datosUsuario = datosUsuarioDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (datosUsuario==null){
                throw new Exception("La empresa no tiene creada alguna DatosUsuario");
            }
        }catch(DatosUsuarioDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada alguna DatosUsuario");
        }
        
        return datosUsuario;
    }
    
    /**
     * Realiza una búsqueda por ID DatosUsuario en busca de
     * coincidencias
     * @param idDatosUsuario ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar datosUsuarios, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO DatosUsuario
     */
    public DatosUsuario[] findDatosUsuarios(long idDatosUsuario, long idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        DatosUsuario[] datosUsuarioDto = new DatosUsuario[0];
        DatosUsuarioDaoImpl datosUsuarioDao = new DatosUsuarioDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idDatosUsuario>0){
                sqlFiltro ="ID_REGION=" + idDatosUsuario + " AND ";
            }else{
                sqlFiltro ="ID_REGION>0 AND";
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
            
            datosUsuarioDto = datosUsuarioDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NOMBRE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return datosUsuarioDto;
    }
    
    /**
     * Realiza una búsqueda por ID DatosUsuario en busca de
     * coincidencias
     * @param idDatosUsuario ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar datosUsuarios, -1 para evitar filtro     
     * @return String de cada una de las datosUsuarios
     */
    
        public String getDatosUsuariosByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            DatosUsuario[] datosUsuariosDto = findDatosUsuarios(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (DatosUsuario datosUsuario:datosUsuariosDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==datosUsuario.getIdDatosUsuario())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+datosUsuario.getIdDatosUsuario()+"' "
                            + selectedStr
                            + "title='"+datosUsuario.getNombre()+"'>"
                            + datosUsuario.getNombre()
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
