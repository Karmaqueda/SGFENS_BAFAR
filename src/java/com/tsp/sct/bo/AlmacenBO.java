/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Almacen;
import com.tsp.sct.dao.exceptions.AlmacenDaoException;
import com.tsp.sct.dao.jdbc.AlmacenDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Leonardo
 */
public class AlmacenBO {
    private Almacen almacen = null;

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public AlmacenBO(Connection conn){
        this.conn = conn;
    }
    
     public AlmacenBO(int idAlmacen, Connection conn){        
        this.conn = conn; 
        try{
           AlmacenDaoImpl AlmacenDaoImpl = new AlmacenDaoImpl(this.conn);
            this.almacen = AlmacenDaoImpl.findByPrimaryKey(idAlmacen);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Almacen findMarcabyId(int idAlmacen) throws Exception{
        Almacen Almacen = null;
        
        try{
            AlmacenDaoImpl AlmacenDaoImpl = new AlmacenDaoImpl(this.conn);
            Almacen = AlmacenDaoImpl.findByPrimaryKey(idAlmacen);
            if (Almacen==null){
                throw new Exception("No se encontro ningun Almacen que corresponda con los parámetros específicados.");
            }
            if (Almacen.getIdAlmacen()<=0){
                throw new Exception("No se encontro ningun Almacen que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Almacen del usuario. Error: " + e.getMessage());
        }
        
        return Almacen;
    }
    
    public Almacen getAlmacenGenericoByEmpresa(int idEmpresa) throws Exception{
        Almacen almacen = null;
        
        try{
            AlmacenDaoImpl almacenDaoImpl = new AlmacenDaoImpl(this.conn);
            almacen = almacenDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (almacen==null){
                throw new Exception("La empresa no tiene creada algun Almacen");
            }
        }catch(AlmacenDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada algun Almacen");
        }
        
        return almacen;
    }
    
    /**
     * Realiza una búsqueda por ID Almacen en busca de
     * coincidencias
     * @param idMarca ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public Almacen[] findAlmacens(int idAlmacen, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        Almacen[] almacenDto = new Almacen[0];
        AlmacenDaoImpl almacenDao = new AlmacenDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idAlmacen>0){
                sqlFiltro ="ID_ALMACEN=" + idAlmacen + " AND ";
            }else{
                sqlFiltro ="ID_ALMACEN>0 AND";
            }
            if (idEmpresa>0){                
                //sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA= " + idEmpresa + ")";
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
            
            almacenDto = almacenDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY isPrincipal desc"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return almacenDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID Almacen y otros filtros
     * @param idAlmacen ID Del Almacen para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public int findCantidadAlmacens(int idAlmacen, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        try {
            String sqlFiltro;
            if (idAlmacen>0){
                sqlFiltro ="ID_ALMACEN=" + idAlmacen + " AND ";
            }else{
                sqlFiltro ="ID_ALMACEN>0 AND";
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
            
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(ID_ALMACEN) as cantidad FROM ALMACEN WHERE " + 
                    sqlFiltro
                    + sqlLimit);
            if(rs.next()){
                cantidad = rs.getInt("cantidad");
            }
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return cantidad;
    }
    
    /**
     * Realiza una búsqueda por ID Almacen en busca de
     * coincidencias
     * @param idMarca ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro     
     * @return String de cada una de las marcas
     */
    
        public String getAlmacenesByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            Almacen[] almacenesDto = findAlmacens(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (Almacen almacen:almacenesDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==almacen.getIdAlmacen())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+almacen.getIdAlmacen()+"' "
                            + selectedStr
                            + "title='"+almacen.getNombre()+"'>"
                            + almacen.getNombre()
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
    
        
    public String getAlmacenesByIdHTMLCombo(int idEmpresa, int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            Almacen[] almacenesDto = findAlmacens(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (Almacen almacen:almacenesDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==almacen.getIdAlmacen())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+almacen.getIdAlmacen()+"' "
                            + selectedStr
                            + "title='"+almacen.getNombre()+"'>"
                            + almacen.getNombre()
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
    
    
    
        public String getAlmacenesByIdHTMLComboPrincipal(int idEmpresa, int idSeleccionado,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            Almacen[] almacenesDto = findAlmacens(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (Almacen almacen:almacenesDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (almacen.getIsPrincipal()==1)
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+almacen.getIdAlmacen()+"' "
                            + selectedStr
                            + "title='"+almacen.getNombre()+"'>"
                            + almacen.getNombre()
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
        
        
    public Almacen findAlmacenbyId(int idAlmacen) throws Exception{
        Almacen Almacen = null;
        
        try{
            AlmacenDaoImpl AlmacenDaoImpl = new AlmacenDaoImpl(this.conn);
            Almacen = AlmacenDaoImpl.findByPrimaryKey(idAlmacen);
            if (Almacen==null){
                throw new Exception("No se encontro ningun Almacen que corresponda con los parámetros específicados.");
            }
            if (Almacen.getIdAlmacen()<=0){
                throw new Exception("No se encontro ningun Almacen que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Almacen del usuario. Error: " + e.getMessage());
        }
        
        return Almacen;
    }    
        
    
    public Almacen getAlmacenPrincipalByEmpresa(int idEmpresa) throws Exception{
        Almacen almacen = null;
        
        try{
            AlmacenDaoImpl almacenDaoImpl = new AlmacenDaoImpl(this.conn);
            almacen = almacenDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1 AND isPrincipal = 1 ", new Object[0])[0];
            if (almacen==null){
                throw new Exception("La empresa no tiene creada algun Almacen principal");
            }
        }catch(AlmacenDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada algun Almacen");
        }
        
        return almacen;
    }
}
