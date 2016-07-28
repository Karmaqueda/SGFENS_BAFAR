/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.NominaTipoJornada;
import com.tsp.sct.dao.exceptions.NominaTipoJornadaDaoException;
import com.tsp.sct.dao.jdbc.NominaTipoJornadaDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class NominaTipoJornadaBO {
    
    private NominaTipoJornada nominaTipoJornada = null;

    public NominaTipoJornada getNominaTipoJornada() {
        return nominaTipoJornada;
    }

    public void setNominaTipoJornada(NominaTipoJornada nominaTipoJornada) {
        this.nominaTipoJornada = nominaTipoJornada;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public NominaTipoJornadaBO(Connection conn){
        this.conn = conn;
    }
       
    
    public NominaTipoJornadaBO(int idNominaTipoJornada){        
        try{
            NominaTipoJornadaDaoImpl NominaTipoJornadaDaoImpl = new NominaTipoJornadaDaoImpl(this.conn);
            this.nominaTipoJornada = NominaTipoJornadaDaoImpl.findByPrimaryKey(idNominaTipoJornada);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public NominaTipoJornada findMarcabyId(int idNominaTipoJornada) throws Exception{
        NominaTipoJornada NominaTipoJornada = null;
        
        try{
            NominaTipoJornadaDaoImpl NominaTipoJornadaDaoImpl = new NominaTipoJornadaDaoImpl(this.conn);
            NominaTipoJornada = NominaTipoJornadaDaoImpl.findByPrimaryKey(idNominaTipoJornada);
            if (NominaTipoJornada==null){
                throw new Exception("No se encontro ninguna Jornada que corresponda con los parámetros específicados.");
            }
            if (NominaTipoJornada.getIdTipoJornada()<=0){
                throw new Exception("No se encontro ninguna Jornada que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la Jornada del usuario. Error: " + e.getMessage());
        }
        
        return NominaTipoJornada;
    }
    
    public NominaTipoJornada getNominaTipoJornadaGenericoByEmpresa(int idEmpresa) throws Exception{
        NominaTipoJornada nominaTipoJornada = null;
        
        try{
            NominaTipoJornadaDaoImpl nominaTipoJornadaDaoImpl = new NominaTipoJornadaDaoImpl(this.conn);
            nominaTipoJornada = nominaTipoJornadaDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (nominaTipoJornada==null){
                throw new Exception("La empresa no tiene creada alguna Jornada");
            }
        }catch(NominaTipoJornadaDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada alguna Jornada");
        }
        
        return nominaTipoJornada;
    }
    
    /**
     * Realiza una búsqueda por ID NominaTipoJornada en busca de
     * coincidencias
     * @param idJornada ID De la Jornada para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public NominaTipoJornada[] findNominaTipoJornadas(int idNominaTipoJornada, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        NominaTipoJornada[] nominaTipoJornadaDto = new NominaTipoJornada[0];
        NominaTipoJornadaDaoImpl nominaTipoJornadaDao = new NominaTipoJornadaDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idNominaTipoJornada>0){
                sqlFiltro ="ID_TIPO_JORNADA=" + idNominaTipoJornada + " AND ";
            }else{
                sqlFiltro ="ID_TIPO_JORNADA>0 AND";
            }
            if (idEmpresa>0){                
                sqlFiltro += " ( (ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")) OR ID_EMPRESA = 0 )";
            }else{
                sqlFiltro +=" ID_EMPRESA>-1";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            nominaTipoJornadaDto = nominaTipoJornadaDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NOMBRE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return nominaTipoJornadaDto;
    }
    
    /**
     * Realiza una búsqueda por ID NominaTipoJornada en busca de
     * coincidencias
     * @param idNominaTipoJornada ID De la Jornada para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar nominaTipoJornadas, -1 para evitar filtro     
     * @return String de cada uno de los nominaTipoJornadas
     */
    
    //public String getNominaTipoJornadasByIdHTMLCombo(int idEmpresa, int idSeleccionado){
    public String getNominaTipoJornadasByIdHTMLCombo(int idEmpresa, String idSeleccionado){
        String strHTMLCombo ="";

        try{
            NominaTipoJornada[] nominaTipoJornadasDto = findNominaTipoJornadas(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (NominaTipoJornada nominaTipoJornada:nominaTipoJornadasDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado.equals(nominaTipoJornada.getNombre()))
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+nominaTipoJornada.getNombre()+"' "
                            + selectedStr
                            + "title='"+nominaTipoJornada.getDescripcion()+"'>"
                            + nominaTipoJornada.getNombre()
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
