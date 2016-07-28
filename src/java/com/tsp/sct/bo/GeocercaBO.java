/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Geocerca;
import com.tsp.sct.dao.exceptions.GeocercaDaoException;
import com.tsp.sct.dao.jdbc.GeocercaDaoImpl;
import com.tsp.sct.mail.TspMailBO;
import java.sql.Connection;
import java.util.StringTokenizer;

/**
 *
 * @author Leonardo
 */
public class GeocercaBO {
    
    private Geocerca geocerca = null;

    public Geocerca getGeocerca() {
        return geocerca;
    }

    public void setGeocerca(Geocerca geocerca) {
        this.geocerca = geocerca;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public GeocercaBO(){
        
    }
    
    public GeocercaBO(Connection conn){
        this.conn = conn;
    }
    
    public GeocercaBO(int idGeocerca, Connection conn){       
        this.conn = conn;
        try{
            GeocercaDaoImpl GeocercaDaoImpl = new GeocercaDaoImpl(this.conn);
            this.geocerca = GeocercaDaoImpl.findByPrimaryKey(idGeocerca);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Geocerca findGeocercabyId(int idGeocerca) throws Exception{
        Geocerca Geocerca = null;
        
        try{
            GeocercaDaoImpl GeocercaDaoImpl = new GeocercaDaoImpl(this.conn);
            Geocerca = GeocercaDaoImpl.findByPrimaryKey(idGeocerca);
            if (Geocerca==null){
                throw new Exception("No se encontro ninguna Geocerca que corresponda con los parámetros específicados.");
            }
            if (Geocerca.getIdGeocerca()<=0){
                throw new Exception("No se encontro ninguna Geocerca que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la Geocerca del usuario. Error: " + e.getMessage());
        }
        
        return Geocerca;
    }
    
    public Geocerca getGeocercaGenericoByEmpresa(int idEmpresa) throws Exception{
        Geocerca geocerca = null;
        
        try{
            GeocercaDaoImpl geocercaDaoImpl = new GeocercaDaoImpl(this.conn);
            geocerca = geocercaDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (geocerca==null){
                throw new Exception("La empresa no tiene creada alguna Geocerca");
            }
        }catch(GeocercaDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada alguna Geocerca");
        }
        
        return geocerca;
    }
    
    /**
     * Realiza una búsqueda por ID Geocerca en busca de
     * coincidencias
     * @param idGeocerca ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar geocercas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Geocerca
     */
    public Geocerca[] findGeocercas(int idGeocerca, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        Geocerca[] geocercaDto = new Geocerca[0];
        GeocercaDaoImpl geocercaDao = new GeocercaDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idGeocerca>0){
                sqlFiltro ="ID_GEOCERCA=" + idGeocerca + " AND ";
            }else{
                sqlFiltro ="ID_GEOCERCA>0 AND";
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
            
            geocercaDto = geocercaDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_GEOCERCA DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return geocercaDto;
    }
    
    /**
     * Realiza una búsqueda por ID Geocerca en busca de
     * coincidencias
     * @param idGeocerca ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar geocercas, -1 para evitar filtro     
     * @return String de cada una de las geocercas
     */
    
        public String getGeocercasByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            Geocerca[] geocercasDto = findGeocercas(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (Geocerca geocerca:geocercasDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";
                    String tipoGeocerca = "";
                    tipoGeocerca = (geocerca.getTipoGeocerca()==1)?"Circulo":(geocerca.getTipoGeocerca()==2)?"Rectangulo":(geocerca.getTipoGeocerca()==3)?"Poligono":"Otra"; 
                    if (idSeleccionado==geocerca.getIdGeocerca())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+geocerca.getIdGeocerca()+"' "
                            + selectedStr
                            + "title='"+geocerca.getCoordenadas()+"'>"
                            + geocerca.getIdGeocerca()+ ", " + tipoGeocerca
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
        
            
    /**
     * Envia un correo usando la plantilla básica para Reestablecimiento
     * @param usuario
     * @param contenidoMail
     * @return
     */
    public boolean mensajeCorreoGeocerca(String contenidoMail, String correos){
        boolean exito = false;
        try {
            TspMailBO mail = new TspMailBO();
            mail.setConfigurationMovilpyme();
            try {                
                StringTokenizer tokens = new StringTokenizer(correos,",");
		while (tokens.hasMoreTokens()) {
                    String correoContacto = tokens.nextToken().intern().trim();
                    mail.addTo(correoContacto, correoContacto);
		}
                
            }catch(Exception e){}
            mail.setFrom(mail.getUSER(), mail.getFROM_NAME());
            mail.addMessageMovilpyme(contenidoMail,1);
            mail.send("Empleados Fuera de Geocercas. ");
            exito=true;
        } catch (Exception ex) {
            System.out.println("No se pudo enviar el correo de Reestablecimiento. Error: "+ ex.getMessage());
            exito=false;
        }

        return exito;
    }
}
