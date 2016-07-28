package com.tsp.sct.util;


import java.util.List;
//import javax.smartcardio.*;
import javacard.framework.*;
import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardNotPresentException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import com.tsp.sct.util.codec.*;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 578
 */
public class comSCR  {
    
    
    
    private static String ReaderName;
    private static CommandAPDU getData;
    private ATR atr;
    public int AIDLength = 0;
    public int xLength = 0;
    
    public static byte[] SELECT_VISA= { //Leer directorio PSE
                (byte) 0x00, //CLA
                (byte) 0xA4, //INS
                (byte) 0x04, //P1
                (byte) 0x00, //P2
                (byte) 0x0E, //LC   --> Tamaño Data In
                (byte) 0x31, //DATA IN
                (byte) 0x50,
                (byte) 0x41,
                (byte) 0x59,
                (byte) 0x2E,
                (byte) 0x53,
                (byte) 0x59,
                (byte) 0x53,
                (byte) 0x2E,
                (byte) 0x44,
                (byte) 0x44,
                (byte) 0x46,
                (byte) 0x30,
                (byte) 0x31,
                //(byte) 0x00 //LE
    };
    
    public static byte[] READ_RECORD= { //Obtener AID
                (byte) 0x00, //CLA
                (byte) 0xB2, //INS
                (byte) 0x01, //P1
                (byte) 0x0C, //P2
                (byte) 0x00, //LC   --> Tamaño Data In
                //(byte) 0x00 //LE
    };
    
    
    
    public static byte[] SELECT_VISAELECTRON= { //Leer directorio PSE
                (byte) 0x00, //CLA
                (byte) 0xA4, //INS
                (byte) 0x04, //P1
                (byte) 0x00, //P2
                (byte) 0x07, //LC   --> Tamaño Data In
                (byte) 0xA0, //DATA IN
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x03,
                (byte) 0x20,
                (byte) 0x10
                
    };
    
    public static byte[] SELECT_MC= { //Leer directorio PSE
                (byte) 0x00, //CLA
                (byte) 0xA4, //INS
                (byte) 0x04, //P1
                (byte) 0x00, //P2
                (byte) 0x07, //LC   --> Tamaño Data In
                (byte) 0xA0, //DATA IN
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x03,
                (byte) 0x20,
                (byte) 0x10
                
    };
    
    
    
   
    
      
  
    List<CardTerminal> terminals = null;
    Card card = null;
    CardTerminal terminal = null;
       
    

    public comSCR() {           
        
    }
    
    
    //Listamos Terminales Conectadas
    public List<CardTerminal> listarTerminales () throws CardException, javax.smartcardio.CardException{  
        
        TerminalFactory factory = TerminalFactory.getDefault();
        terminals = factory.terminals().list();       
        
        return terminals;
        
    }
    
    
    public void elegirTerminal(int numTerminal){        
        terminal = terminals.get(numTerminal);        
        //return terminal;
    }
    
    
    public boolean conectarConTarjeta() throws CardException, NullPointerException, javax.smartcardio.CardException {        
        try{
            card = terminal.connect("T=0");            
            getATR();            
            return true;
        }catch(CardNotPresentException e){
            System.out.println("Inserte la tarjeta en el lector"); 
            return false;
        }catch(NullPointerException e) {
            System.out.println(e.getStackTrace());
            return false;
        }
    }
     
    public ResponseAPDU transmitir(byte[] capdu) throws CardException, javax.smartcardio.CardException{
              
        CardChannel channel = card.getBasicChannel(); 
        CommandAPDU GetData = new CommandAPDU(capdu);                 
        ResponseAPDU r = channel.transmit(GetData);        
        ResponseAPDU response = r;
        
        return response;        
    }
    
    
     /*public ResponseAPDU transmitir(int cla, int ins, int p1, int p2, byte[] data) throws CardException, javax.smartcardio.CardException{
              
        CardChannel channel = card.getBasicChannel(); 
        CommandAPDU GetData = new CommandAPDU(cla, ins, p1,  p2, data);  
        ResponseAPDU r = channel.transmit(GetData);        
        ResponseAPDU response = r;
        
        return response;        
    }*/
    
    
    
    public void desconectarTarjeta() throws CardException, javax.smartcardio.CardException{
        try{
            card.disconnect(false);
        }catch(NullPointerException e){
            System.out.println("No hay una tarjeta presente en el lector");
        }
    }
    
    
    
    
     // convert Hex to ASCII  	
    public  String hexToASCII(String hexa){ 
        int[] text = new int [hexa.length() / 2]; 
        int j = 0; 
        String ascii = ""; 	 	 
        StringBuilder s = new StringBuilder(hexa); 
 	 	 
        // remove all space within hexa String  	
        for (int i=0; i < s.length(); i++)  	 	
            if (s.charAt(i) == ' ') 
                 s.deleteCharAt(i); 
 	 	 
      // assign new non-space string back to hexa   
        hexa = s.toString(); 
 	 	 
 	for (int i = 0; i < hexa.length(); i += 2) 
            text[j++] = Integer.parseInt(hexa.substring(i, i + 2), 16); 
 	 	 
            for (int i=0; i<text.length; i++) 
            { 
                    ascii += (char)text[i]; 
                    // print ascii text to screen too! 
                    //System.out.print((char)text[i] + " "); 
            } 
 	 	 
         return ascii; 
    } 
 
 	// convert Hex to Decimal (number)  	
    public  int hexToDec(String hex) { 
	 return Integer.parseInt(hex, 16); 
    } 
    
    // convert Hex to Binary (number), restore zero at front too  	
    public String hexToBin(String hex){ 
	 String s = Integer.toBinaryString(hexToDec(hex));  	 	 
 	 	// restore zero  
         while (s.length() < 8) 
             s = "0" + s; 
 	 	 
	 return s; 
    }     
    
    
     public String bytesToHexString(byte[] bytes){
        StringBuilder sb = new StringBuilder(); 
        for(byte b : bytes){
            sb.append(String.format("%02x", b&0xff));
        } 
        return sb.toString(); 
    } 
     
    public static String firstHex(int dec) {
           String numeroHex=""; 
           if(dec<16) {
               switch(dec) { 
                  case 10:
                      numeroHex="A"; 
                      break; 
                  case 11:
                      numeroHex="B"; 
                      break; 
                  case 12:
                      numeroHex="C"; 
                      break; 
                  case 13:
                      numeroHex="D"; 
                      break; 
                  case 14:
                      numeroHex="E"; 
                      break; 
                  case 15:
                      numeroHex="F"; 
                      break; 
                  default: 
                      numeroHex=Integer.toString(dec); 
                      break; 
              } 
              return numeroHex; 
          } else { 
              return "false";
          } 
      }
    
       
      public  String toHex(int dec) { 
           int cociente=16, residuo=0; 
           String numeroHex="", numeroHex1=""; 
           if(dec<16)
            { 
                numeroHex=firstHex(dec); 
            } else  
              { 
                do  
                { 
                 cociente=dec/16; 
                 residuo=dec%16; 
                 dec=cociente; 
                 numeroHex1=firstHex(residuo); 
                 numeroHex=numeroHex1+numeroHex; 
                 dec=cociente;          
                } while (dec>=16); 
          numeroHex1=firstHex(dec); 
          numeroHex=numeroHex1+numeroHex; 
          } 
          return numeroHex; 
      }
    
     

    private void getATR() {
        atr = card.getATR();                     
        //-System.out.println("ATR: " + bytesToHexString(atr.getBytes()));
        System.out.println("ATR CARD!!");
    }
    
    public String obtenerAID_V(ResponseAPDU rapdu) {
         //obtenemos AID apartir tag 4F XX --> Tamaño AID
        String dataOut = "";
        dataOut = bytesToHexString(rapdu.getData()).toUpperCase();;
        
        AIDLength = hexToDec(dataOut.substring(dataOut.indexOf("4F") + 2, dataOut.indexOf("4F") + 4 )); // obtengo el num despues del tag 4F --> Tamaño del AID
        //System.out.println("AID Length:  " + AIDLength);
        String aid = dataOut.substring(dataOut.indexOf("4F") + 4, dataOut.indexOf("4F") + 4 + AIDLength * 2); // Recupero el AID (Tamaño * 2) --> 1 nibble por digito
        
        //-System.out.println("AID:  " + aid);
        
        return aid;
    }
    
    
    public String obtenerAID_VE(ResponseAPDU rapdu) {
         //obtenemos AID apartir tag 84 XX --> Tamaño AID
        String dataOut = "";
        dataOut = bytesToHexString(rapdu.getData()).toUpperCase();;
        
        AIDLength = hexToDec(dataOut.substring(dataOut.indexOf("84") + 2, dataOut.indexOf("84") + 4 )); // obtengo el num despues del tag 84 --> Tamaño del AID
        //System.out.println("AID Length:  " + AIDLength);
        String aid = dataOut.substring(dataOut.indexOf("84") + 4, dataOut.indexOf("84") + 4 + AIDLength * 2); // Recupero el AID (Tamaño * 2) --> 1 nibble por digito
        
        //-System.out.println("AID:  " + aid);
        
        return aid;
    }
    
    
    /*public String obtenerAIP(ResponseAPDU rapdu) {
         //obtenemos AIP apartir tag 4F XX --> Tamaño AID
        String dataOut = "";
        dataOut = bytesToHexString(rapdu.getData()).toUpperCase();;
        
        AIDLength = hexToDec(dataOut.substring(dataOut.indexOf("4F") + 2, dataOut.indexOf("4F") + 4 )); // obtengo el num despues del tag 4F --> Tamaño del AID
        //System.out.println("AID Length:  " + AIDLength);
        String aid = dataOut.substring(dataOut.indexOf("4F") + 4, dataOut.indexOf("4F") + 4 + AIDLength * 2); // Recupero el AID (Tamaño * 2) --> 1 nibble por digito
        
        System.out.println("AID:  " + aid);
        
        return aid;
    }*/
    
    
    public byte[] convertStringToByteArray(String word) {
        
        String stringToConvert = word;
        
        byte[] theByteArray = stringToConvert.getBytes();
        
        //System.out.println(theByteArray.length);
        
        return theByteArray;
        
    }
    
    
    
    public byte[] hexStringToByteArray(String s) {
       int len = s.length();
       byte[] data = new byte[len / 2];
       for (int i = 0; i < len; i += 2){
           data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
           + Character.digit(s.charAt(i+1), 16));
       }
       return data;
  }
    
    
    public String obtener16Digitos(ResponseAPDU rapdu) {
         
        String dataOut = "";
        dataOut = bytesToHexString(rapdu.getData()).toUpperCase();;
        
        xLength = 8;  //bytes respuesta  2digitos hex = 1 byte        
        String digitos16 = dataOut.substring(dataOut.indexOf("57") + 4, dataOut.indexOf("57") + 4 + xLength * 2); 
        
        //System.out.println("16 Digitos:  " + digitos16);
        
        return digitos16;
    }
    
    public String obtenerfechaVencimiento(ResponseAPDU rapdu) {
        
        String dataOut = "";
        dataOut = bytesToHexString(rapdu.getData()).toUpperCase();;
        
        xLength = 17;        
        String digitos16 = dataOut.substring(dataOut.indexOf("57") + 4  + xLength, dataOut.indexOf("57") + 4 + (xLength+4)); 
        
        //System.out.println("Fecha Vencimiento:  " + digitos16);
        
        return digitos16;
    }
    
    
        
    
}
