/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.util;

import com.tsp.sct.config.Configuration;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author ISC César Ulises Martínez García
 */
public class FileManage {

    

    /**
     * Método para crear un objeto tipo java.io.File a partir de un arreglo de bytes
     * @param bytesFile Los bytes que representan al archivo
     * @param pathFile Ruta en donde se grabara el archivo
     * @param nameFile Nombre del archivo
     * @return Objeto tipo File creado
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static File createFileFromByteArray(byte[] bytesFile, String pathFile, String nameFile) throws FileNotFoundException, IOException{
        
        File path = new File(pathFile);
        path.mkdirs();

        File newTempFile = new File(pathFile+nameFile);
        FileOutputStream fos = new FileOutputStream(newTempFile);
        fos.write(bytesFile);
        fos.flush();
        fos.close();

        return newTempFile;

    }



    /**
     * Método para crear un objeto tipo java.io.File a partir de un arreglo de bytes
     * @param contentFile El string con el contenido del archivo
     * @param pathFile Ruta en donde se grabara el archivo
     * @param nameFile Nombre del archivo
     * @return Objeto tipo File creado
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static File createFileFromString(String contentFile, String pathFile, String nameFile) throws FileNotFoundException, IOException{

        File path = new File(pathFile);
        path.mkdirs();
        
        File newTempFile = new File(pathFile+nameFile);

         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newTempFile), "UTF8"));
        bw.write(contentFile);
        bw.flush();
        bw.close();

        return newTempFile;

    }

    /**
     * Obtiene el arreglo de bytes de un Archivo
     * @param fileToRead Archivo a leer
     * @return Arreglo de bytes
     * @throws FileNotFoundException
     * @throws IOException
     * @throws Exception
     */
    public static byte[] getBytesFromFile(File fileToRead) throws FileNotFoundException, IOException, Exception {
        byte[] bytes = null;

        FileInputStream fis = new FileInputStream(fileToRead);
        bytes = new byte[(int)fileToRead.length()];
        fis.read(bytes);
        fis.close();

        return bytes;
    }
    
    /**
     * Realiza una copia de un archivo, indicando la ruta de origen
     * y la ruta de destino
     * @param archivoOrigen Ruta absoluta del archivo Origen
     * @param archivoDestino Ruta absoluta del archivo Destino
     * @return true en caso de ocurrir una copia correcta, false en caso contrario
     */
    public static boolean copyFile(String archivoOrigen, String archivoDestino){
        boolean exito = false;
        
        try{
            File origen = new File(archivoOrigen);
            File destino = new File(archivoDestino);
            
            InputStream in = new FileInputStream(origen);
            OutputStream out = new FileOutputStream(destino);
            
            try {
                int c;
                while( (c = in.read() ) != -1)
                        out.write(c);
                
                exito = true;
            }catch(Exception ex){
                exito = false;
            }finally{
                in.close();
                out.close();
            }
        }catch(Exception e){
            exito = false;
        }
        
        return exito;
    }
    
    /**
     * Busca un archivo con la coincidencia con alguna parte de su nombre
     * después lo elimina
     * P. ejem: si nameLike es "ABC" y en el repositorio se encuentra el archivo
     *          miArchivo_ABC_123944.xml  , lo eliminara
     * @param directoryToExplore Ruta del directorio a explorar en busca del archivo
     * @param nameLike Cadena a buscar dentro del nombre del archivo
     * @return true en caso de encontrar y borrar un archivo, false en caso contrario
     */
    public static boolean findAndDeleteFileNameLike(String directoryToExplore, String nameLike) {
        boolean exito = false;

        try {
            File path = new File(directoryToExplore);
            if (!path.isDirectory()) {
                System.out.println("La carpeta específicada "
                        + "para hacer la busqueda del archivo no es válida.");
            }

            File[] archivos = path.listFiles();
            for (int i = 0; i < archivos.length; i++) {
                File archivo = archivos[i];

                //Compara contra el nombre del archivo, busca la cadena en su nombre
                if (archivo.getName().contains(nameLike)) {
                    //Intentamos repetidamente el borrado del archivo hasta que sea exitoso
                    for (int j=0;j<1000;j++){
                        if (archivo.delete()){
                            exito=true;
                            break;
                        }
                    }
                    break;
                }
            }

        } catch (Exception e) {
            exito = false;
        }

        return exito;
    }
    
    /**
     * Método para crear un objeto tipo java.io.File a partir de ByteArrayOutputStream
     * @param baos El objeto ByteArrayOutputStream con el contenido del archivo
     * @param pathFile Ruta en donde se grabara el archivo
     * @param nameFile Nombre del archivo
     * @return Objeto tipo File creado
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static File createFileFromByteArrayOutputStream(ByteArrayOutputStream baos, String pathFile, String nameFile) throws FileNotFoundException, IOException {

        File newFile = new File(pathFile + nameFile);
        FileOutputStream fos = new FileOutputStream(newFile);
        baos.writeTo(fos);
        
        fos.flush();
        fos.close();

        return newFile;

    }
    
    /**
     * Para los archivos que contienen texto tales como XML, txt, html, etc.
     * Se puede recuperar su contenido en formato Cadena con codificación UTF8
     * con este método.
     * @return contenido del archivo expresado en formato cadena UTF8
     */
    public static String getStringFromFile(File fileToRead){
        String contentTextArchivo=null;
        try {
             contentTextArchivo = new String(getBytesFromFile(fileToRead), "UTF8");
        }catch(Exception e){
                
        }
        return contentTextArchivo;
    }
    
    public static String getParentPathString(File file){
        Path path = Paths.get(file.getAbsolutePath());
        return path.getParent().toString();
    }

}
