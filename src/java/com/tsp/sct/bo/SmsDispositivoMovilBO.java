/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SmsDispositivoMovil;
import com.tsp.sct.dao.jdbc.SmsDispositivoMovilDaoImpl;
import com.tsp.sct.mail.TspMailBO;
import com.tsp.sct.util.DateManage;
import com.tsp.sct.util.Encrypter;
import com.tsp.sct.util.GenericValidator;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ISCesar
 */
public class SmsDispositivoMovilBO {
    private SmsDispositivoMovil smsDispositivoMovil = null;
    
    public static int ID_ESTATUS_ACTIVO = 1;
    public static int ID_ESTATUS_INACTIVO = 2;
    public static int ID_ESTATUS_DESCONECTADO = 3;

    public SmsDispositivoMovil getSmsDispositivoMovil() {
        return smsDispositivoMovil;
    }

    public void setSmsDispositivoMovil(SmsDispositivoMovil smsDispositivoMovil) {
        this.smsDispositivoMovil = smsDispositivoMovil;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SmsDispositivoMovilBO(Connection conn){
        this.conn = conn;
    }
    
     public SmsDispositivoMovilBO(int idSmsDispositivoMovil, Connection conn){        
        this.conn = conn; 
        try{
           SmsDispositivoMovilDaoImpl SmsDispositivoMovilDaoImpl = new SmsDispositivoMovilDaoImpl(this.conn);
            this.smsDispositivoMovil = SmsDispositivoMovilDaoImpl.findByPrimaryKey(idSmsDispositivoMovil);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public SmsDispositivoMovil findSmsDispositivoMovilbyId(int idSmsDispositivoMovil) throws Exception{
        SmsDispositivoMovil SmsDispositivoMovil = null;
        
        try{
            SmsDispositivoMovilDaoImpl SmsDispositivoMovilDaoImpl = new SmsDispositivoMovilDaoImpl(this.conn);
            SmsDispositivoMovil = SmsDispositivoMovilDaoImpl.findByPrimaryKey(idSmsDispositivoMovil);
            if (SmsDispositivoMovil==null){
                throw new Exception("No se encontro ningun SmsDispositivoMovil que corresponda con los parámetros específicados.");
            }
            if (SmsDispositivoMovil.getIdSmsDispositivoMovil()<=0){
                throw new Exception("No se encontro ningun SmsDispositivoMovil que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del SmsDispositivoMovil del usuario. Error: " + e.getMessage());
        }
        
        return SmsDispositivoMovil;
    }
    
    /**
     * Realiza una búsqueda por ID SmsDispositivoMovil en busca de
     * coincidencias
     * @param idSmsDispositivoMovil ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public SmsDispositivoMovil[] findSmsDispositivoMovils(int idSmsDispositivoMovil, int minLimit,int maxLimit, String filtroBusqueda) {
        SmsDispositivoMovil[] smsDispositivoMovilDto = new SmsDispositivoMovil[0];
        SmsDispositivoMovilDaoImpl smsDispositivoMovilDao = new SmsDispositivoMovilDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSmsDispositivoMovil>0){
                sqlFiltro ="id_sms_dispositivo_movil=" + idSmsDispositivoMovil + " ";
            }else{
                sqlFiltro ="id_sms_dispositivo_movil>0 ";
            }
            /*
            if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            }else{
                sqlFiltro +=" ID_EMPRESA>0";
            }
            */
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            smsDispositivoMovilDto = smsDispositivoMovilDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY id_sms_dispositivo_movil desc"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return smsDispositivoMovilDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID SmsDispositivoMovil y otros filtros
     * @param idSmsDispositivoMovil ID Del SmsDispositivoMovil para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public int findCantidadSmsDispositivoMovils(int idSmsDispositivoMovil, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        SmsDispositivoMovilDaoImpl smsDispositivoMovilDao = new SmsDispositivoMovilDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idSmsDispositivoMovil>0){
                sqlFiltro ="id_sms_dispositivo_movil=" + idSmsDispositivoMovil + " ";
            }else{
                sqlFiltro ="id_sms_dispositivo_movil>0 ";
            }
            /*
            if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            }else{
                sqlFiltro +=" ID_EMPRESA>0";
            }
            */
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_sms_dispositivo_movil) as cantidad FROM " + smsDispositivoMovilDao.getTableName() +  " WHERE " + 
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
        
    public String getSmsDispositivoMovilesByIdHTMLCombo(int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            SmsDispositivoMovil[] smsDispositivoMovilesDto = findSmsDispositivoMovils(-1, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (SmsDispositivoMovil smsDispositivoMovil : smsDispositivoMovilesDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado == smsDispositivoMovil.getIdSmsDispositivoMovil())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+smsDispositivoMovil.getIdSmsDispositivoMovil()+"' "
                            + selectedStr
                            + "title='"+smsDispositivoMovil.getIdSmsDispositivoMovil()+"'>"
                            + "(" + smsDispositivoMovil.getIdSmsDispositivoMovil() + ") " + smsDispositivoMovil.getAlias()
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
     * Método para validar el Login de un dispositivo movil de SMS comparando el nick y password
     * contra los almacenados en la base de datos
     * @param userName Login, nick del empleado
     * @param pwdUser Password, contraseña del empleado
     * @return Boolean que indica si fue exitoso o no
     */
    public boolean login(String userName, String pwdUser) throws Exception {

        boolean validate = false;
        
        try {

            SmsDispositivoMovil[] smsDispositivoMovilValidated = new SmsDispositivoMovilDaoImpl(this.conn).findWhereUsuarioEquals(userName);
            if (smsDispositivoMovilValidated.length==0) {
                this.smsDispositivoMovil = null;
            }else {
                for (SmsDispositivoMovil smsDispositivoMovilValidated1 : smsDispositivoMovilValidated) {
                    Encrypter encriptacion =  new Encrypter();
                    encriptacion.setMd5(true);
                    if (smsDispositivoMovilValidated1.getPassword().equals(encriptacion.encodeString2(pwdUser))) {
                        this.smsDispositivoMovil = smsDispositivoMovilValidated1;
                        
                        if (smsDispositivoMovil.getIdEstatus()==2){
                            //Si el usuario esta inhabilitado
                            throw  new Exception("El Registro de Dispositivo esta deshabilitado.");
                        }                        
                        
                        validate = true;
                        break;
                    }
                }
            }

        } catch (Exception ex) {
            System.out.println("Error al intentar validar el acceso de un empleado" + ex.getMessage());
            ex.printStackTrace();
            //return false;
            throw  new Exception(ex.getMessage());
        }
        
        return validate;
    }
    
    public boolean enviarCorreoDispositivosSinConexion(List<SmsDispositivoMovil> listaDispositivosSinConexion){
        boolean enviado;
        
        try {
            TspMailBO mailBO = new TspMailBO();

            mailBO.setConfigurationMovilpyme();

            GenericValidator genericValidator = new GenericValidator();
            ArrayList<String> destinatarios = new ArrayList<String>();

            try{
                ParametrosBO parametrosBO = new ParametrosBO(this.conn);
                String correos = parametrosBO.getParametroString("app.sms.destFijosMail");
                
                String[] dests = correos.split(",");
                for (String dest : dests ){
                    destinatarios.add(StringManage.getValidString(dest));
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }

            //Agregamos todos los correos
            for (String destinatario : destinatarios){
                try{                           
                    if(genericValidator.isEmail(destinatario)){
                        try{
                            mailBO.addTo(destinatario, "Administrador SMS");
                        }catch(Exception ex){}
                    }
                }catch(Exception ex){}
            }

            String msg = "<b>Buen día!</b><br/> ";
            
            msg += "<br/>Los siguientes Dispositivos Móviles de módulo SMS aparentemente estan sin conexión. <br/><br/>";
            
            //detalles de la asignación
            msg+= "<table border='1'>";
            msg+= "<tr>";
            msg+= " <td>ID</td><td>Alias</td><td>Ultima Conexión</td><td>Pct. Pila</td>";
            msg+= "</tr>";
            for (SmsDispositivoMovil movil : listaDispositivosSinConexion){                
                msg+= "<tr>";
                msg+= " <td>" + movil.getIdSmsDispositivoMovil()+ "</td><td>" + movil.getAlias()+ "</td><td>" + DateManage.formatDateTimeToNormalMinutes(movil.getFechaHrUltimaCom()) + "</td><td>"+ movil.getPctPila() + "</td>";
                msg+= "</tr>";
            }
            msg+= "</table>";
            
            msg += "<br/>Se sugiere que los revise de forma física para descartar: <br/><ul>";
            msg += "    <li>Bateria agotada.</li>";
            msg += "    <li>Aplicativo no iniciado.</li>";
            msg += "    <li>Red movil no disponible/Fallas en la Red movil.</li>";
            msg += "    <li>Movil sin credito.</li>";
            msg += "    <li>Falla en el aplicativo.</li>";
            msg += "</ul>";

            mailBO.addMessageMovilpyme(msg ,1);

            mailBO.send("Dispositivos SMS sin conexion");

            enviado = true;
        } catch (Exception ex) {
            enviado = false;
            System.out.print("Ocurrio un error al intentar enviar el correo: " + ex.toString());
            ex.printStackTrace();
        }            
        
        return enviado;
    }
        
}
