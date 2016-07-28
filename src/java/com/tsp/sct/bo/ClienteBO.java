/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Cliente;
import com.tsp.sct.dao.exceptions.ClienteDaoException;
import com.tsp.sct.dao.jdbc.ClienteDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;

import com.tsp.sct.util.DateManage;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesarMartinez
 */
public class ClienteBO {
    private Cliente cliente  = null;

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public ClienteBO(Connection conn){
        this.conn = conn;
    }
    
    public ClienteBO(int idCliente, Connection conn){
        this.conn = conn;
        try{
            ClienteDaoImpl ClienteDaoImpl = new ClienteDaoImpl(this.conn);
            this.cliente = ClienteDaoImpl.findByPrimaryKey(idCliente);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Cliente findClientebyId(int idCliente) throws Exception{
        Cliente Cliente = null;
        
        try{
            ClienteDaoImpl ClienteDaoImpl = new ClienteDaoImpl(this.conn);
            Cliente = ClienteDaoImpl.findByPrimaryKey(idCliente);
            if (Cliente==null){
                throw new Exception("No se encontro ningun Cliente que corresponda con los parámetros específicados.");
            }
            if (Cliente.getIdCliente()<=0){
                throw new Exception("No se encontro ningun Cliente que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Cliente del usuario. Error: " + e.getMessage());
        }
        
        return Cliente;
    }
    
    /**
     * Recupera de Base de Datos el registro de Cliente 
     * por defecto Genérico para la empresa. 
     * 
     * <p>
     * 
     * En caso de no encontrar uno, lo intenta crear.
     * 
     * @param idEmpresa ID de la empresa
     * @return Cliente DTO de objeto Cliente
     * @throws Exception en caso de que no exista el cliente genérico para la empresa y no puedo crearse.
     */
    public Cliente getClienteGenericoByEmpresa(int idEmpresa) throws Exception{
        Cliente cliente = null;
        
        try{
            ClienteDaoImpl clienteDaoImpl = new ClienteDaoImpl(this.conn);
            Cliente[] clientesGenerico = clienteDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND GENERICO = 1", new Object[0]);
            if (clientesGenerico.length>0){
                cliente = clientesGenerico[0];
            }else{
                //Como no se encontro ningun genérico, se crea para la empresa
                return crearClienteGenericoByEmpresa(idEmpresa);
            }
        }catch(ClienteDaoException e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creado un Cliente Génerico. " + e.toString());
        }
        
        return cliente;
    }
    
    /**
     * Realiza una búsqueda por ID Cliente en busca de
     * coincidencias
     * @param idCliente ID Del Cliente para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar clienteDto, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Cliente
     */
    public Cliente[] findClientes(int idCliente, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        Cliente[] clienteDto = new Cliente[0];
        ClienteDaoImpl clienteDao = new ClienteDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCliente>0){
                sqlFiltro ="ID_CLIENTE=" + idCliente + " AND ";
            }else{
                sqlFiltro ="ID_CLIENTE>0 AND";
            }
            if (idEmpresa>0){
                //sqlFiltro +=" ID_EMPRESA=" + idEmpresa;
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
            
            clienteDto = clienteDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY RAZON_SOCIAL ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return clienteDto;
    }
    
    public String getClientesByIdHTMLCombo(int idEmpresa, int idSeleccionado, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            Cliente[] clientesDto = findClientes(-1, idEmpresa, 0, 0, filtroBusqueda);
            
            for (Cliente itemCliente:clientesDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado==itemCliente.getIdCliente())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+itemCliente.getIdCliente()+"' "
                            + selectedStr
                            + "title='"+itemCliente.getNombreCliente()+"'>"
                            + itemCliente.getRazonSocial() + (StringManage.getValidString(itemCliente.getClave()).length()>0?(", Clave: "+itemCliente.getClave()):"")
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
     * Crea el registro de un cliente Genérico para la empresa indicada.
     * 
     * @param idEmpresa ID de la empresa
     * @return Cliente Objeto DTO de Cliente
     * @throws Exception En caso de no poder crear el registro. 
     */
    public Cliente crearClienteGenericoByEmpresa(int idEmpresa) throws Exception{
        Cliente clienteDto = new Cliente();

        try{
            ClienteDaoImpl clientesDaoImpl = new ClienteDaoImpl(this.conn);
            
            //Calculamos ID siguiente en tabla de clientes
            int idClienteNuevo;
            Cliente ultimoRegistroClientes = clientesDaoImpl.findLast();
            idClienteNuevo = ultimoRegistroClientes.getIdCliente() + 1;
            
            clienteDto.setIdCliente(idClienteNuevo);
            clienteDto.setIdEmpresa(idEmpresa);
            clienteDto.setIdEstatus(2); //Inactivo
            clienteDto.setGenerico(1); // Genérico de la empresa
            clienteDto.setRfcCliente("XAXX010101000");
            clienteDto.setNombreCliente("Genérico");
            clienteDto.setApellidoPaternoCliente("Genérico");
            clienteDto.setApellidoMaternoCliente("Genérico");
            clienteDto.setRazonSocial("Cliente Genérico");
            clienteDto.setCalle("N/A");
            clienteDto.setNumero("");
            clienteDto.setNumeroInterior("");
            clienteDto.setColonia("");
            clienteDto.setCodigoPostal("");
            clienteDto.setPais("México");
            clienteDto.setEstado("");
            clienteDto.setMunicipio("");
            clienteDto.setLada("");
            clienteDto.setTelefono("");
            clienteDto.setExtension("");
            clienteDto.setCelular("");
            clienteDto.setCorreo("");
            clienteDto.setContacto("Sin contacto");
            clienteDto.setLatitud("0");
            clienteDto.setLongitud("0");
            
            clientesDaoImpl.insert(clienteDto);
        }catch(Exception ex){
            clienteDto = null;
            ex.printStackTrace();
            throw new Exception("No se pudo crear el registro de Cliente Genérico para la empresa con ID: " + idEmpresa + ". " + ex.toString());
        }
        
        return clienteDto;
    }
    
    /**
    * Genera un folio con los siguientes parametros:
    * 
    * letras CL, id_empresa a 3 dígitos, 1 guion, "CON" (de Consola), 1 guion, l número d folio
    * de acuerdo a fecha y hora
    * 
    * p. ejem:  CL001-CON-20130131100101111
    * 
    * @param idEmpresa
    * @param idEmpleado
    * @return
    */
    public static String generaFolioMovil(int idEmpresa){
        String folio = "";
        String empresa = StringManage.getExactString(""+idEmpresa, 3, '0', StringManage.FILL_DIRECTION_LEFT);
        //String empleado = StringManage.getExactString(""+idEmpleado, 4, '0', StringManage.FILL_DIRECTION_LEFT);

        //int folioConsecutivo = new Random().nextInt(100);
        try{Thread.sleep(100);}catch(Exception ex){}
        String folioConsecutivo = DateManage.getDateHourString();

        folio = "CL" + empresa + "-CON-" + folioConsecutivo;

        return folio;
    }
    
    public int getCantidadByCliente(String filtroBusqueda){
        int cantidad = 0;
        try{
            //Connection conne = ResourceManager.getConnection();
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(ID_CLIENTE) as cantidad FROM CLIENTE WHERE " + filtroBusqueda);
            if(rs.next()){
                cantidad = rs.getInt("cantidad");
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return cantidad;
    }
    
    
    public Cliente[] findClientebyIdEmpleado(int idEmpleado) throws Exception{
        Cliente[] Cliente = null;
        
        try{
            ClienteDaoImpl ClienteDaoImpl = new ClienteDaoImpl(this.conn);
            Cliente = ClienteDaoImpl.findWhereIdVendedorConsignaEquals(idEmpleado);
            if (Cliente==null){
                throw new Exception("No se encontro ningun Cliente que corresponda con los parámetros específicados.");
            }            
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Cliente del usuario. Error: " + e.getMessage());
        }
        
        return Cliente;
    }
    
    public Cliente getClienteByRazonSocial(long idEmpresa, String razonSocial) throws Exception{
        Cliente cliente = null;
        
        
        try{
            ClienteDaoImpl ConceptoDaoImpl = new ClienteDaoImpl(this.conn);   
            
            cliente = ConceptoDaoImpl.findByDynamicWhere(" RAZON_SOCIAL = '" + razonSocial +"' AND "
                    + " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")", new Object[0])[0];
            
            if (cliente==null){
                throw new Exception("La empresa no tiene creado un producto con los datos especificados");
            }
        }catch(ClienteDaoException e){
            //e.printStackTrace();
            throw new Exception("La empresa no tiene creado un producto con los datos especificados");
        }
        
        return cliente;
    }
    
}
