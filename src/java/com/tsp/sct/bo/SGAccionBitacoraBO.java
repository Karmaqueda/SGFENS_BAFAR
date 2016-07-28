/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.dao.dto.MigracionSctEvc;
import com.tsp.sct.dao.dto.SgfensAccionBitacora;
import com.tsp.sct.dao.dto.SgfensTopic;
import com.tsp.sct.dao.dto.Usuarios;
import com.tsp.sct.dao.exceptions.SgfensAccionBitacoraDaoException;
import com.tsp.sct.dao.exceptions.SgfensTopicDaoException;
import com.tsp.sct.dao.jdbc.MigracionSctEvcDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensAccionBitacoraDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensTopicDaoImpl;
import com.tsp.sct.dao.jdbc.UsuariosDaoImpl;
import java.sql.Connection;
import java.util.Date;

/**
 *
 * @author ISC César Ulises Martínez García
 */
public class SGAccionBitacoraBO {
    public static final int ACCION_LOGIN =1;
    public static final int ACCION_LOGOUT =2;
    public static final int ACCION_NAVEGACION =3;
    public static final int ACCION_DESCARGA =4;

    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SGAccionBitacoraBO(Connection conn) {
        this.conn = conn;
    }
    
    public static String getAccionBitacoraName(int idTipoAccionBitacora){
        String name="";
        switch (idTipoAccionBitacora){
            case ACCION_LOGIN:
                return "Login";
            case ACCION_LOGOUT:
                return "Logout";
            case ACCION_NAVEGACION:
                return "Navegación";
            case ACCION_DESCARGA:
                return "Descarga";
            default:
                name ="Tipo de Acción Indefinida";
        }
        return name;
    }

    /**
     * Inserta un evento de Login en la bitacora de acciones
     * @param idUser identificador unico del usuario que efectua la acción
     * @param comentarios Comentarios adicionales
     * @return true en caso de inserción exitosa, false en caso contrario
     */
    public boolean insertAccionLogin(long idUser, String comentarios){
        boolean exito=false;
        SgfensAccionBitacora accionBitacoraDto = new SgfensAccionBitacora();
        SgfensAccionBitacoraDaoImpl accionBitacoraDaoImpl = new SgfensAccionBitacoraDaoImpl(this.conn);

        try{
            if (comentarios.equals(""))
                comentarios="Inicio de sesión";

            accionBitacoraDto.setIdTipoBitacoraAccionTipo(ACCION_LOGIN);
            accionBitacoraDto.setIdUser(idUser);
            accionBitacoraDto.setFechaHoraBitacoraAccion(new Date());
            accionBitacoraDto.setComentariosBitacoraAccion(comentarios);

            accionBitacoraDaoImpl.insert(accionBitacoraDto);
            exito=true;
        }catch(Exception e){

        }
        
        
        try{ //guarda login en migracion
            Usuarios usu = new UsuariosDaoImpl().findByPrimaryKey((int)idUser);
            Empresa emp = new EmpresaBO(usu.getIdEmpresa(), this.conn).getEmpresa();
            
            MigracionSctEvc migraEmp = null;
            MigracionSctEvcDaoImpl migraDao = new MigracionSctEvcDaoImpl();


            migraEmp = new MigracionBO(emp.getIdEmpresa(), conn).getMigracion();							


            if(migraEmp!=null){
                    migraEmp.setFechaAccesoPreto(new Date());		
                    migraDao.update(migraEmp.createPk(), migraEmp);

            }else{
                    migraEmp = new MigracionSctEvc();
                    migraEmp.setIdEmpresa(usu.getIdEmpresa());
                    migraEmp.setFechaAccesoPreto(new Date());
                    migraDao.insert(migraEmp);
            }            
            
        }catch(Exception e){
            System.out.println("No se pudo guardar login en Migracion"); // No afecta flujo normal
        }
        
        return exito;
    }

    /**
     * Inserta un evento de Logout en la bitacora de acciones
     * @param idUser identificador unico del usuario que efectua la acción
     * @param comentarios Comentarios adicionales
     * @return true en caso de inserción exitosa, false en caso contrario
     */
    public boolean insertAccionLogout(long idUser, String comentarios){
        boolean exito=false;
        SgfensAccionBitacora accionBitacoraDto = new SgfensAccionBitacora();
        SgfensAccionBitacoraDaoImpl accionBitacoraDaoImpl = new SgfensAccionBitacoraDaoImpl(this.conn);

        try{
            if (comentarios.equals(""))
                comentarios="Cierre de sesión";

            accionBitacoraDto.setIdTipoBitacoraAccionTipo(ACCION_LOGOUT);
            accionBitacoraDto.setIdUser(idUser);
            accionBitacoraDto.setFechaHoraBitacoraAccion(new Date());
            accionBitacoraDto.setComentariosBitacoraAccion(comentarios);

            accionBitacoraDaoImpl.insert(accionBitacoraDto);
            exito=true;
        }catch(Exception e){

        }
        return exito;
    }

    /**
     * Inserta un evento de Descarga en la bitacora de acciones
     * @param idUser identificador unico del usuario que efectua la acción
     * @param comentarios Comentarios adicionales
     * @return true en caso de inserción exitosa, false en caso contrario
     */
    public boolean insertAccionDescarga(long idUser, String comentarios){
        boolean exito=false;
        SgfensAccionBitacora accionBitacoraDto = new SgfensAccionBitacora();
        SgfensAccionBitacoraDaoImpl accionBitacoraDaoImpl = new SgfensAccionBitacoraDaoImpl(this.conn);

        try{
            if (comentarios.equals(""))
                comentarios="Descarga de archivo";

            accionBitacoraDto.setIdTipoBitacoraAccionTipo(ACCION_DESCARGA);
            accionBitacoraDto.setIdUser(idUser);
            accionBitacoraDto.setFechaHoraBitacoraAccion(new Date());
            accionBitacoraDto.setComentariosBitacoraAccion(comentarios);

            accionBitacoraDaoImpl.insert(accionBitacoraDto);
            exito=true;
        }catch(Exception e){

        }
        return exito;
    }

    public boolean insertAccionNavegacion(long idUser, String comentarios, String strPathTopic){
       long idTopic = (long)0;
       try {
            SgfensTopicDaoImpl topicDao = new SgfensTopicDaoImpl(this.conn);
            SgfensTopic[] topics = topicDao.findWhereUrlTopicEquals(strPathTopic);
            if (topics.length>0){
                idTopic = topics[0].getIdTopic();
            }
        } catch (SgfensTopicDaoException ex) {
            System.out.println(ex.getMessage());
        }

       return insertAccionNavegacion(idUser, comentarios, idTopic);
    }

    /**
     * Inserta un evento de navegación en la bitácora de acciones
     * @param idUser identificador único del usuario que efectúa la acción
     * @param comentarios Comentarios adicionales
     * @return true en caso de inserción exitosa, false en caso contrario
     */
    public boolean insertAccionNavegacion(long idUser, String comentarios, long idTopic){
        boolean exito=false;
        SgfensAccionBitacora accionBitacoraDto = new SgfensAccionBitacora();
        SgfensAccionBitacoraDaoImpl accionBitacoraDaoImpl = new SgfensAccionBitacoraDaoImpl(this.conn);

        try{
            if (comentarios.equals(""))
                comentarios="Navegacion en el sistema";

            accionBitacoraDto.setIdTipoBitacoraAccionTipo(ACCION_NAVEGACION);
            accionBitacoraDto.setIdUser(idUser);
            accionBitacoraDto.setFechaHoraBitacoraAccion(new Date());
            accionBitacoraDto.setComentariosBitacoraAccion(comentarios);
            accionBitacoraDto.setIdTopicNavegacion(idTopic);

            accionBitacoraDaoImpl.insert(accionBitacoraDto);
            exito=true;
        }catch(Exception e){

        }
        return exito;
    }

     /**
     * Regresa un arreglo de objetos SgfensAccionBitacora con los datos
     * de los registro de bitácora que corresponden a un usuario y a cierto tipo de Acción
     * @param  int idUser Identificador único del Usuario
     * @param int[] idTipoAccion Arreglo de identificadores para filtrar por tipo de Acción.
     *          Si se requiere obtener la consulta sin este filtro basta con enviar un
     *          arreglo vacío: new int[0]
     * @return Arreglo de objetos SgfensAccionBitacora
     */
    public SgfensAccionBitacora[] getBitacoraByUser(int idUser, int[] idTipoAccion){
        SgfensAccionBitacoraDaoImpl bitacoraDao = new SgfensAccionBitacoraDaoImpl(this.conn);
        SgfensAccionBitacora[] bitacora =  new SgfensAccionBitacora[0];
        try {
            String strWhereTipoAccion ="";
            int i = 1;
            //construimos parte del "where" de la consulta con el arreglo de status como filtro
            for (int idtipoAccionItem : idTipoAccion) {
                strWhereTipoAccion += " id_tipo_bitacora_accion_tipo=  " + idtipoAccionItem;
                strWhereTipoAccion += (idTipoAccion.length > 1 && i < idTipoAccion.length) ? " OR" : "";
                i++;
            }
            if (!strWhereTipoAccion.equals("")){
                strWhereTipoAccion = " AND ("+strWhereTipoAccion+")";
            }
            bitacora = bitacoraDao.findByDynamicWhere("id_user = "+idUser+" "+strWhereTipoAccion+" ORDER BY fecha_hora_bitacora_accion DESC", new Object[0]);
        } catch (SgfensAccionBitacoraDaoException ex) {
            System.out.println(ex.getMessage());
            //Logger.getLogger(SgfensAccionBitacoraBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bitacora;
    }

    /**
     * Regresa un arreglo de objetos SgfensAccionBitacora con los datos
     * de los registro de bitácora que corresponden A cierto tipo de Accion
     * @param  int idUser Identificador único del Usuario
     * @param int[] idTipoAccion Arreglo de identificadores para filtrar por tipo de Accion
     * @return Arreglo de objetos SgfensAccionBitacora
     */
    public SgfensAccionBitacora[] getBitacoraByTipoAccion(int[] idTipoAccion){
        SgfensAccionBitacoraDaoImpl bitacoraDao = new SgfensAccionBitacoraDaoImpl(this.conn);
        SgfensAccionBitacora[] bitacora =  new SgfensAccionBitacora[0];
        try {
            String strWhereTipoAccion ="";
            int i = 1;
            //construimos parte del "where" de la consulta con el arreglo de status como filtro
            for (int idtipoAccionItem : idTipoAccion) {
                strWhereTipoAccion += " id_tipo_bitacora_accion_tipo=  " + idtipoAccionItem;
                strWhereTipoAccion += (idTipoAccion.length > 1 && i < idTipoAccion.length) ? " OR" : "";
                i++;
            }
            bitacora = bitacoraDao.findByDynamicWhere(strWhereTipoAccion+" ORDER BY fecha_hora_bitacora_accion DESC", new Object[0]);
        } catch (SgfensAccionBitacoraDaoException ex) {
            System.out.println(ex.getMessage());
            //Logger.getLogger(SgfensAccionBitacoraBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bitacora;
    }
    
    /**
     * Realiza una búsqueda por ID SgfensAccionBitacora en busca de
     * coincidencias
     * @param idSgfensAccionBitacora ID De la accionBitacora para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar accionBitacora, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensAccionBitacora
     */
    public SgfensAccionBitacora[] findAccionBitacora(int idSgfensAccionBitacora, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SgfensAccionBitacora[] accionBitacoraDto = new SgfensAccionBitacora[0];
        SgfensAccionBitacoraDaoImpl accionBitacoraDao = new SgfensAccionBitacoraDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSgfensAccionBitacora>0){
                sqlFiltro ="ID_BITACORA_ACCION=" + idSgfensAccionBitacora + " AND ";
            }else{
                sqlFiltro ="ID_BITACORA_ACCION>0 AND";
            }
            if (idEmpresa>0){
                sqlFiltro += " ID_USER IN (SELECT ID_USUARIOS AS 'ID_USER' FROM USUARIOS WHERE ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + "))";
            }else{
                sqlFiltro +=" ID_USER>0";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            accionBitacoraDto = accionBitacoraDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY fecha_hora_bitacora_accion DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return accionBitacoraDto;
    }

}
