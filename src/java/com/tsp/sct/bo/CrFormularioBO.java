/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CrFormulario;
import com.tsp.sct.dao.dto.CrFormularioCampo;
import com.tsp.sct.dao.dto.CrGrupoFormulario;
import com.tsp.sct.dao.jdbc.CrFormularioCampoDaoImpl;
import com.tsp.sct.dao.jdbc.CrFormularioDaoImpl;
import com.tsp.sct.util.StringManage;
import com.tsp.sgfens.sesion.CrFormularioCampoSesion;
import com.tsp.sgfens.sesion.CrFormularioDisenoSesion;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author ISCesar
 */
public class CrFormularioBO {
    private CrFormulario crFormulario = null;
    private String orderBy = null;

    public CrFormulario getCrFormulario() {
        return crFormulario;
    }

    public void setCrFormulario(CrFormulario crFormulario) {
        this.crFormulario = crFormulario;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    
    public CrFormularioBO(Connection conn){
        this.conn = conn;
    }
    
     public CrFormularioBO(int idCrFormulario, Connection conn){        
        this.conn = conn; 
        try{
           CrFormularioDaoImpl CrFormularioDaoImpl = new CrFormularioDaoImpl(this.conn);
            this.crFormulario = CrFormularioDaoImpl.findByPrimaryKey(idCrFormulario);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public CrFormulario findCrFormulariobyId(int idCrFormulario) throws Exception{
        CrFormulario CrFormulario = null;
        
        try{
            CrFormularioDaoImpl CrFormularioDaoImpl = new CrFormularioDaoImpl(this.conn);
            CrFormulario = CrFormularioDaoImpl.findByPrimaryKey(idCrFormulario);
            if (CrFormulario==null){
                throw new Exception("No se encontro ningun CrFormulario que corresponda con los parámetros específicados.");
            }
            if (CrFormulario.getIdFormulario()<=0){
                throw new Exception("No se encontro ningun CrFormulario que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CrFormulario del usuario. Error: " + e.getMessage());
        }
        
        return CrFormulario;
    }
    
    /**
     * Realiza una búsqueda por ID CrFormulario en busca de
     * coincidencias
     * @param idCrFormulario ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CrFormulario
     */
    public CrFormulario[] findCrFormularios(int idCrFormulario, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CrFormulario[] crFormularioDto = new CrFormulario[0];
        CrFormularioDaoImpl crFormularioDao = new CrFormularioDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCrFormulario>0){
                sqlFiltro ="id_formulario=" + idCrFormulario + " AND ";
            }else{
                sqlFiltro ="id_formulario>0 AND";
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
            
            crFormularioDto = crFormularioDao.findByDynamicWhere( 
                    sqlFiltro
                    + " " + (StringManage.getValidString(orderBy).length()>0? orderBy :  " ORDER BY id_formulario desc")
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return crFormularioDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID CrFormulario y otros filtros
     * @param idCrFormulario ID Del CrFormulario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return int cantidad de registros coincidentes
     */
    public int findCantidadCrFormularios(int idCrFormulario, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        CrFormularioDaoImpl crFormularioDao = new CrFormularioDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrFormulario>0){
                sqlFiltro ="id_formulario=" + idCrFormulario + " AND ";
            }else{
                sqlFiltro ="id_formulario>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_formulario) as cantidad FROM " + crFormularioDao.getTableName() +  " WHERE " + 
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
        
    public String getCrFormulariosByIdHTMLCombo(int idEmpresa, int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            CrFormulario[] crFormularioesDto = findCrFormularios(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (CrFormulario crFormulario : crFormularioesDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado == crFormulario.getIdFormulario())
                        selectedStr = " selected ";

                    CrGrupoFormulario crGrupoFormulario = new CrGrupoFormularioBO(crFormulario.getIdGrupoFormulario(), this.conn).getCrGrupoFormulario();
                    strHTMLCombo += "<option value='"+crFormulario.getIdFormulario()+"' "
                            + selectedStr
                            + "title='"+crFormulario.getDescripcion()+"'>"
                            + (crGrupoFormulario!=null?crGrupoFormulario.getNombre():"") + " - " + crFormulario.getNombre()
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
     * Almacena los valores del diseño de un formulario, desde Sesion hacia Base de datos
     * @param crFormularioDisenoSesion Objeto de sesion para diseño de Formularios
     * @param user Objeto usuario en sesion
     * @throws Exception En caso de errores inesperados
     */
    public void guardarFormularioSesion(CrFormularioDisenoSesion crFormularioDisenoSesion, UsuarioBO user) throws Exception{
        if (crFormularioDisenoSesion!=null){
            CrFormularioCampoBO crFormularioCampoBO = new CrFormularioCampoBO(this.conn);
            CrFormularioCampo[] crFormularioCampos = new CrFormularioCampo[0];
            if (crFormularioDisenoSesion.getIdFormulario()>0){
                // Ya existe, haremos actualización de su información
                crFormularioCampos = crFormularioCampoBO.findCrFormularioCampos(-1, -1, 0, 0, " AND id_formulario="+crFormularioDisenoSesion.getIdFormulario());
            }else{
                throw new Exception("No se específico a que Formulario corresponde el diseño.");
            }
            CrFormulario crFormulario = new CrFormularioBO(crFormularioDisenoSesion.getIdFormulario(), this.conn).getCrFormulario();
            
            // primero desactivamos todos los campos actuales que existan
            CrFormularioCampoDaoImpl crFormularioCampoDao = new CrFormularioCampoDaoImpl(this.conn);
            for (CrFormularioCampo campoBD : crFormularioCampos){
                campoBD.setIdEstatus(2); //desactivado
                crFormularioCampoDao.update(campoBD.createPk(), campoBD);                
            }
            crFormularioCampos = null; //liberamos array de objetos
            
            //ordenamos lista de campos por atributo Orden
            java.util.Collections.sort(crFormularioDisenoSesion.getListaCampoSesion()); 
            // Luego actualizamos o insertamos los campos desde sesion hacia BD
            int ordenFinal = 1;
            for (CrFormularioCampoSesion campoSesion : crFormularioDisenoSesion.getListaCampoSesion()){
                CrFormularioCampo campoBDNuevoActualizado;
                // Primero buscamos si ya existe en BD
                if (campoSesion.getIdFormularioCampo()>0){
                    campoBDNuevoActualizado = crFormularioCampoBO.findCrFormularioCampobyId(campoSesion.getIdFormularioCampo());
                }else{
                    // es registro nuevo
                    campoBDNuevoActualizado = new CrFormularioCampo();
                    campoBDNuevoActualizado.setIdFormulario(crFormularioDisenoSesion.getIdFormulario());
                    campoBDNuevoActualizado.setIdTipoCampo(campoSesion.getIdTipoCampo());
                    campoBDNuevoActualizado.setIdEmpresa(user.getUser().getIdEmpresa());
                }
                
                // Asignamos valores a DTO
                campoBDNuevoActualizado.setOrdenFormulario(ordenFinal);
                //campoBDNuevoActualizado.setNoSeccion(1);
                campoBDNuevoActualizado.setEtiqueta(StringManage.getValidString(campoSesion.getEtiqueta()));
                campoBDNuevoActualizado.setDescripcion(StringManage.getValidString(campoSesion.getDescripcion()));
                campoBDNuevoActualizado.setValorDefecto(StringManage.getValidString(campoSesion.getValorDefecto()));
                campoBDNuevoActualizado.setValorSugerencia(StringManage.getValidString(campoSesion.getSugerencia()));
                campoBDNuevoActualizado.setIsRequerido(campoSesion.isIsRequerido()?1:0);
                campoBDNuevoActualizado.setIdFormularioValidacion(campoSesion.getIdFormularioValidacion());
                campoBDNuevoActualizado.setOpciones(campoSesion.getOpcionesTexto());
                if (StringManage.getValidString(campoSesion.getVariableFormula()).length()>0){
                    campoBDNuevoActualizado.setVariableFormula(campoSesion.getVariableFormula());
                }else{
                    campoBDNuevoActualizado.setVariableFormula(null);
                }
                campoBDNuevoActualizado.setIdEstatus(1); //activamos siempre
                
                // Actualizamos o Insertamos 
                if (campoBDNuevoActualizado.getIdFormularioCampo()>0){
                    // si ya cuenta con Llave primaria, es edicion
                    crFormularioCampoDao.update(campoBDNuevoActualizado.createPk(), campoBDNuevoActualizado);
                }else{
                    // si no, es creación de registro nuevo
                    crFormularioCampoDao.insert(campoBDNuevoActualizado);
                }
                ordenFinal++;
            }
            
            crFormulario.setFechaHrUltimaEdicion(new Date());
            crFormulario.setIdUsuarioEdicion(user.getUser().getIdUsuarios());
            try{
                new CrFormularioDaoImpl(this.conn).update(crFormulario.createPk(), crFormulario);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }else{
            throw new Exception("Formulario en Sesión NULO, no se puede almacenar a base de datos.");
        }
    }
    
    public CrFormularioDisenoSesion recuperarFormularioSesion(int idFormulario) throws Exception{
        CrFormularioDisenoSesion crFormularioDisenoSesion = new CrFormularioDisenoSesion(this.conn);
        
        CrFormularioCampoBO crFormularioCampoBO = new CrFormularioCampoBO(this.conn);
        CrFormularioCampo[] crFormularioCampos = new CrFormularioCampo[0];
        if (idFormulario>0){
            // Ya existe, haremos actualización de su información
            crFormularioCampoBO.setOrderBy(" ORDER BY no_seccion ASC, orden_formulario ASC ");
            crFormularioCampos = crFormularioCampoBO.findCrFormularioCampos(-1, -1, 0, 0, " AND id_formulario="+idFormulario);
        }else{
            throw new Exception("No se específico a que Formulario corresponde el diseño a recuperar.");
        }
        
        crFormularioDisenoSesion.setIdFormulario(idFormulario);
        int orden = 1;
        for (CrFormularioCampo campoBD : crFormularioCampos){
            CrFormularioCampoSesion crFormularioCampoSesion = new CrFormularioCampoSesion();
            
            crFormularioCampoSesion.setIdentificadorCampoSesion(crFormularioDisenoSesion.getUltimoIdentificadorCampoSesion() + 1 );
            crFormularioCampoSesion.setOrden(orden);
            crFormularioCampoSesion.setIdTipoCampo(campoBD.getIdTipoCampo());
            crFormularioCampoSesion.setIdFormularioCampo(campoBD.getIdFormularioCampo());
            crFormularioCampoSesion.setNombreIdCampo("campo_"+orden);
            crFormularioCampoSesion.setEtiqueta(campoBD.getEtiqueta());
            crFormularioCampoSesion.setDescripcion(campoBD.getDescripcion());
            crFormularioCampoSesion.setSugerencia(campoBD.getValorSugerencia());
            crFormularioCampoSesion.setIsRequerido(campoBD.getIsRequerido()==1);
            crFormularioCampoSesion.setIdFormularioValidacion(campoBD.getIdFormularioValidacion());
            crFormularioCampoSesion.setOpciones(campoBD.getOpciones());
            crFormularioCampoSesion.setValorDefecto(campoBD.getValorDefecto());
            crFormularioCampoSesion.setVariableFormula(campoBD.getVariableFormula());
                    
            crFormularioDisenoSesion.getListaCampoSesion().add(crFormularioCampoSesion);
            
            orden++;
        }
        
        return crFormularioDisenoSesion;
    }
        
}
