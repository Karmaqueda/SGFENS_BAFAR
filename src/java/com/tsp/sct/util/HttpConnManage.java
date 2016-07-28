/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author ISCesar
 */
public class HttpConnManage {
    public static String postHTTPS(String url, HttpsURLConnection httpsConnection, String bodyContent,
            int maxTimeOut, String acceptEncoding, String contentType ) throws Exception{
        String responseStr = "";
        
        try{
            httpsConnection.setRequestMethod("POST");
            if (acceptEncoding==null){
                httpsConnection.setRequestProperty("Accept-Encoding", "gzip,deflate");
            }else{
                httpsConnection.setRequestProperty("Accept-Encoding", acceptEncoding);
            }
            if (contentType==null){
                httpsConnection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            }else{
                httpsConnection.setRequestProperty("Content-Type", contentType);
            }
            httpsConnection.setRequestProperty("Accept", "text/html, multipart/related");
            httpsConnection.setRequestProperty("Content-Length", "" + Integer.toString(bodyContent.getBytes("UTF-8").length));
            
            httpsConnection.setDoOutput(true);
            
            // give it N seconds to respond
            if (maxTimeOut>0)
                httpsConnection.setReadTimeout(maxTimeOut*1000);

            //Send request
            OutputStream reqStream = httpsConnection.getOutputStream();
            reqStream.write(bodyContent.getBytes("UTF-8"));
            reqStream.flush();  

            StringBuilder response;
            //Leemos respuesta
            boolean exito;
            if (httpsConnection.getResponseCode()==HttpsURLConnection.HTTP_OK){
                //En caso de conexión exitosa	
                InputStream is = decompressStream(httpsConnection, httpsConnection.getInputStream());
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF8"));
                String line;
                response = new StringBuilder(); 
                while((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                exito = true;
            }else{
                //En caso de fallo en la conexion
                InputStream is = decompressStream(httpsConnection, httpsConnection.getErrorStream());
                if (is!=null){
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is,"UTF8"));
                    String line;
                    response = new StringBuilder(); 
                    response.append(httpsConnection.getResponseCode()).append(" ");
                    response.append(httpsConnection.getResponseMessage()).append(" ");
                    while((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                    rd.close();
                }else{
                    response = new StringBuilder();
                    response.append("Error conexión HTTP ");
                    response.append(httpsConnection.getResponseCode()).append(" ");
                    response.append(httpsConnection.getResponseMessage());
                }
                exito = false;
            }
            responseStr = response.toString();
            
            if (!exito)
                throw new Exception("El servidor ("+url+") ha respondido: " + responseStr);
            
        }catch(Exception ex){
            throw new Exception("Error al intentar conexión." + ex.toString());
        }
        
        return responseStr;
    }
    
    public static String getHTTPS(String url, HttpsURLConnection httpsConnection, int maxTimeOut) throws Exception{
        String responseStr = "";
        
        try{
            httpsConnection.setRequestMethod("GET");
            httpsConnection.setRequestProperty("Accept", "text/html, multipart/related");
            
            httpsConnection.setDoOutput(true);
            
            // give it N seconds to respond
            if (maxTimeOut>0)
                httpsConnection.setReadTimeout(maxTimeOut*1000);

            StringBuilder response;
            //Leemos respuesta
            boolean exito;
            if (httpsConnection.getResponseCode()==HttpsURLConnection.HTTP_OK){
                //En caso de conexión exitosa	
                InputStream is = decompressStream(httpsConnection, httpsConnection.getInputStream());
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF8"));
                String line;
                response = new StringBuilder(); 
                while((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                exito = true;
            }else{
                //En caso de fallo en la conexion
                InputStream is = decompressStream(httpsConnection, httpsConnection.getErrorStream());
                if (is!=null){
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is,"UTF8"));
                    String line;
                    response = new StringBuilder(); 
                    response.append(httpsConnection.getResponseCode()).append(" ");
                    response.append(httpsConnection.getResponseMessage()).append(" ");
                    while((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                    rd.close();
                }else{
                    response = new StringBuilder();
                    response.append("Error conexión HTTP ");
                    response.append(httpsConnection.getResponseCode()).append(" ");
                    response.append(httpsConnection.getResponseMessage());
                }
                exito = false;
            }
            responseStr = response.toString();
            
            if (!exito)
                throw new Exception("El servidor ("+url+") ha respondido: " + responseStr);
            
        }catch(Exception ex){
            throw new Exception("Error al intentar conexión." + ex.toString());
        }
        
        return responseStr;
    }
    
    public static InputStream decompressStream(HttpsURLConnection connection, InputStream input){
        if ("gzip".equals(connection.getContentEncoding()) || "zip".equals(connection.getContentEncoding()) || "application/x-gzip-compressed".equals(connection.getContentEncoding())) {
            try {
                input = new GZIPInputStream(input);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return input;
    }
    
    public static String postHTTP(String url, HttpURLConnection httpConnection, String bodyContent,
            int maxTimeOut, String acceptEncoding, String contentType ) throws Exception{
        String responseStr = "";
        
        try{
            httpConnection.setRequestMethod("POST");
            if (acceptEncoding==null){
                httpConnection.setRequestProperty("Accept-Encoding", "gzip,deflate");
            }else{
                httpConnection.setRequestProperty("Accept-Encoding", acceptEncoding);
            }
            if (contentType==null){
                httpConnection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            }else{
                httpConnection.setRequestProperty("Content-Type", contentType);
            }
            int length = bodyContent.getBytes("UTF-8").length;
            httpConnection.setRequestProperty("Accept", "text/html, multipart/related");
            httpConnection.setRequestProperty("Content-Length", "" + Integer.toString(length));
            httpConnection.setFixedLengthStreamingMode(length);
            
            httpConnection.setDoOutput(true);
            
            // give it N seconds to respond
            if (maxTimeOut>0)
                httpConnection.setReadTimeout(maxTimeOut*1000);

            //Send request
            OutputStream reqStream = httpConnection.getOutputStream();
            reqStream.write(bodyContent.getBytes("UTF-8"));
            reqStream.flush();  

            StringBuilder response;
            //Leemos respuesta
            boolean exito;
            if (httpConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                //En caso de conexión exitosa	
                InputStream is = decompressStream(httpConnection, httpConnection.getInputStream());
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF8"));
                String line;
                response = new StringBuilder(); 
                while((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                exito = true;
            }else{
                //En caso de fallo en la conexion
                InputStream is = decompressStream(httpConnection, httpConnection.getErrorStream());
                if (is!=null){
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is,"UTF8"));
                    String line;
                    response = new StringBuilder(); 
                    response.append(httpConnection.getResponseCode()).append(" ");
                    response.append(httpConnection.getResponseMessage()).append(" ");
                    while((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                    rd.close();
                }else{
                    response = new StringBuilder();
                    response.append("Error conexión HTTP ");
                    response.append(httpConnection.getResponseCode()).append(" ");
                    response.append(httpConnection.getResponseMessage());
                }
                exito = false;
            }
            responseStr = response.toString();
            
            if (!exito)
                throw new Exception("El servidor ("+url+") ha respondido: " + responseStr);
            
        }catch(Exception ex){
            throw new Exception("Error al intentar conexión." + ex.toString());
        }
        
        return responseStr;
    }
    
    /**
     * 
     * @param url
     * @param httpConnection
     * @param params usar:
     *      <p>Map<String,Object> params = new LinkedHashMap<>();</p>
     *      <p>params.put("param1", "valor");</p>
     *      <p>params.put("param2", valorUObjeto);</p>
     * @param maxTimeOut
     * @param acceptEncoding
     * @param contentType
     * @return
     * @throws Exception 
     */
    public static String postHTTP(String url, HttpURLConnection httpConnection, Map<String,Object> params,
            int maxTimeOut, String acceptEncoding, String contentType ) throws Exception{
        String responseStr = "";
        
        try{
            String bodyContent;
            {
                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String,Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                bodyContent = postData.toString();
            }
            httpConnection.setRequestMethod("POST");
            if (acceptEncoding==null){
                httpConnection.setRequestProperty("Accept-Encoding", "gzip,deflate");
            }else{
                httpConnection.setRequestProperty("Accept-Encoding", acceptEncoding);
            }
            if (contentType==null){
                httpConnection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            }else{
                httpConnection.setRequestProperty("Content-Type", contentType);
            }
            int length = bodyContent.getBytes("UTF-8").length;
            httpConnection.setRequestProperty("Accept", "text/html, multipart/related");
            httpConnection.setRequestProperty("Content-Length", "" + Integer.toString(length));
            httpConnection.setFixedLengthStreamingMode(length);
            
            httpConnection.setDoOutput(true);
            
            // give it N seconds to respond
            if (maxTimeOut>0)
                httpConnection.setReadTimeout(maxTimeOut*1000);

            //Send request
            OutputStream reqStream = httpConnection.getOutputStream();
            reqStream.write(bodyContent.getBytes("UTF-8"));
            reqStream.flush();  

            StringBuilder response;
            //Leemos respuesta
            boolean exito;
            if (httpConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                //En caso de conexión exitosa	
                InputStream is = decompressStream(httpConnection, httpConnection.getInputStream());
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF8"));
                String line;
                response = new StringBuilder(); 
                while((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                exito = true;
            }else{
                //En caso de fallo en la conexion
                InputStream is = decompressStream(httpConnection, httpConnection.getErrorStream());
                if (is!=null){
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is,"UTF8"));
                    String line;
                    response = new StringBuilder(); 
                    response.append(httpConnection.getResponseCode()).append(" ");
                    response.append(httpConnection.getResponseMessage()).append(" ");
                    while((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                    rd.close();
                }else{
                    response = new StringBuilder();
                    response.append("Error conexión HTTP ");
                    response.append(httpConnection.getResponseCode()).append(" ");
                    response.append(httpConnection.getResponseMessage());
                }
                exito = false;
            }
            responseStr = response.toString();
            
            if (!exito)
                throw new Exception("El servidor ("+url+") ha respondido: " + responseStr);
            
        }catch(Exception ex){
            throw new Exception("Error al intentar conexión." + ex.toString());
        }
        
        return responseStr;
    }
    
    public static String getHTTP(String url, HttpURLConnection httpConnection, int maxTimeOut) throws Exception{
        String responseStr = "";
        
        try{
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("Accept", "text/html, multipart/related");
            
            httpConnection.setDoOutput(true);
            
            // give it N seconds to respond
            if (maxTimeOut>0)
                httpConnection.setReadTimeout(maxTimeOut*1000);

            StringBuilder response;
            //Leemos respuesta
            boolean exito;
            if (httpConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                //En caso de conexión exitosa	
                InputStream is = decompressStream(httpConnection, httpConnection.getInputStream());
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF8"));
                String line;
                response = new StringBuilder(); 
                while((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                exito = true;
            }else{
                //En caso de fallo en la conexion
                InputStream is = decompressStream(httpConnection, httpConnection.getErrorStream());
                if (is!=null){
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is,"UTF8"));
                    String line;
                    response = new StringBuilder(); 
                    response.append(httpConnection.getResponseCode()).append(" ");
                    response.append(httpConnection.getResponseMessage()).append(" ");
                    while((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                    rd.close();
                }else{
                    response = new StringBuilder();
                    response.append("Error conexión HTTP ");
                    response.append(httpConnection.getResponseCode()).append(" ");
                    response.append(httpConnection.getResponseMessage());
                }
                exito = false;
            }
            responseStr = response.toString();
            
            if (!exito)
                throw new Exception("El servidor ("+url+") ha respondido: " + responseStr);
            
        }catch(Exception ex){
            throw new Exception("Error al intentar conexión." + ex.toString());
        }
        
        return responseStr;
    }
    
    public static InputStream decompressStream(HttpURLConnection connection, InputStream input){
        if ("gzip".equals(connection.getContentEncoding()) || "zip".equals(connection.getContentEncoding()) || "application/x-gzip-compressed".equals(connection.getContentEncoding())) {
            try {
                input = new GZIPInputStream(input);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return input;
    }
}
