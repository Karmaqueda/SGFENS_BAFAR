package com.tsp.sgfens.report;

import com.tsp.sct.bo.UsuarioBO;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 17-dic-2012 
 */
public class ReportExportable {
    
    protected ArrayList<HashMap> dataList = null;
    protected ArrayList<HashMap> fieldList = null;
    protected UsuarioBO user = null;
    protected File fileImageLogo = null;
    protected Connection conn = null;

    public ArrayList<HashMap> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<HashMap> dataList) {
        this.dataList = dataList;
    }

    public ArrayList<HashMap> getFieldList() {
        return fieldList;
    }

    public void setFieldList(ArrayList<HashMap> fieldList) {
        this.fieldList = fieldList;
    }

    public File getFileImageLogo() {
        return fileImageLogo;
    }

    public void setFileImageLogo(File fileImageLogo) {
        this.fileImageLogo = fileImageLogo;
    }

    public UsuarioBO getUser() {
        return user;
    }

    public void setUser(UsuarioBO user) {
        this.user = user;
    }

   public String getTitle(int REPORT){
       return ReportBO.getTitle(REPORT);
   }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
}
