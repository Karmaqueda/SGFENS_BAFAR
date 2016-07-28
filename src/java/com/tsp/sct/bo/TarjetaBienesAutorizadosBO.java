/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

/**
 *
 * @author Leonardo
 */
public class TarjetaBienesAutorizadosBO {
    
    private String prefijo;

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }
        
    /*public TarjetaBienesAutorizadosBO(String prefijo) {
        this.prefijo = prefijo;
    } */   
    
    public String validacion (String numPrefijo){
        String tipoTarjeta = "";
        
        if(numPrefijo.equals("448790")){
            tipoTarjeta = "AFIRME|CREDITO";
        }else if(numPrefijo.equals("512048")){
            tipoTarjeta = "AFIRME|CREDITO";
        }else if(numPrefijo.equals("420199")){
            tipoTarjeta = "BAJIO|CREDITO";
        }else if(numPrefijo.equals("403703")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("403707")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("405306")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("405930")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("407458")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("407559")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("410852")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("420713")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("435741")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("439120")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("441541")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("441545")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("441549")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("451331")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("451332")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("451333")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("454057")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("454069")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("454492")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("455255")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("479461")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("480790")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("485942")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("485943")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("490176")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("490178")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("491271")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("491272")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("491501")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("491502")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("493164")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("493165")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("494388")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("498460")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("504563")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("512709")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("512795")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("512809")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("512823")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("517712")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("517721")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("517771")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("517795")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("518004")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("518851")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("518853")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("518899")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("520213")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("520416")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("520608")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("520694")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("520698")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("522130")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("524711")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("525424")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("525678")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("526489")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("528804")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("528805")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("528843")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("528851")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("528866")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("528875")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("528877")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("529001")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("529088")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("529091")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("529093")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("530056")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("530756")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("540533")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("542537")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("544672")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("545039")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("547075")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("547093")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("547112")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("547370")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("547380")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("547514")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("548234")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("549138")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("554492")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("554628")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("558784")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("559209")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("854809")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("854819")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("854858")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("854859")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("854897")){
            tipoTarjeta = "BANAMEX|DEBITO";
        }else if(numPrefijo.equals("854899")){
            tipoTarjeta = "BANAMEX|CREDITO";
        }else if(numPrefijo.equals("427213")){
            tipoTarjeta = "BANCA MIFEL|CREDITO";
        }else if(numPrefijo.equals("460766")){
            tipoTarjeta = "BANCO AUTOFIN MEXICO|DEBITO";
        }else if(numPrefijo.equals("421364")){
            tipoTarjeta = "BANCO AZTECA|DEBITO";
        }else if(numPrefijo.equals("476202")){
            tipoTarjeta = "BANCO AZTECA|CREDITO";
        }else if(numPrefijo.equals("476203")){
            tipoTarjeta = "BANCO AZTECA|CREDITO";
        }else if(numPrefijo.equals("476204")){
            tipoTarjeta = "BANCO AZTECA|CREDITO";
        }else if(numPrefijo.equals("477831")){
            tipoTarjeta = "BANCO AZTECA|CREDITO";
        }else if(numPrefijo.equals("639484")){
            tipoTarjeta = "BANCO BANSEFI|DEBITO";
        }else if(numPrefijo.equals("444888")){
            tipoTarjeta = "BANCO WALMART ADELANTE|DEBITO";
        }else if(numPrefijo.equals("444889")){
            tipoTarjeta = "BANCO WALMART ADELANTE|DEBITO";
        }else if(numPrefijo.equals("530113")){
            tipoTarjeta = "BANCO WALMART ADELANTE|DEBITO";
        }else if(numPrefijo.equals("539975")){
            tipoTarjeta = "BANCO WALMART ADELANTE|DEBITO";
        }else if(numPrefijo.equals("409851")){
            tipoTarjeta = "BANCOMER|DEBITO";
        }else if(numPrefijo.equals("409852")){
            tipoTarjeta = "BANCOMER|DEBITO";
        }else if(numPrefijo.equals("410180")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("410181")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("415231")){
            tipoTarjeta = "BANCOMER|DEBITO";
        }else if(numPrefijo.equals("441310")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("441311")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("441312")){
            tipoTarjeta = "BANCOMER|DEBITO";
        }else if(numPrefijo.equals("441313")){
            tipoTarjeta = "BANCOMER|DEBITO";
        }else if(numPrefijo.equals("441314")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("455500")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("455503")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("455504")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("455505")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("455506")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("455507")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("455509")){
            tipoTarjeta = "BANCOMER|DEBITO";
        }else if(numPrefijo.equals("455510")){
            tipoTarjeta = "BANCOMER|DEBITO";
        }else if(numPrefijo.equals("455513")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("455514")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("455515")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("455519")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("455525")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("455526")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("455527")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("455529")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("455540")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("455545")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("493160")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("493161")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("493162")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("494398")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("542010")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("542977")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("544053")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("544551")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("547077")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("547086")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("547095")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("547155")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("547156")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("547492")){
            tipoTarjeta = "BANCOMER|CREDITO";
        }else if(numPrefijo.equals("491343")){
            tipoTarjeta = "BANJERCITO|CREDITO";
        }else if(numPrefijo.equals("491346")){
            tipoTarjeta = "BANJERCITO|CREDITO";
        }else if(numPrefijo.equals("547080")){
            tipoTarjeta = "BANJERCITO|CREDITO";
        }else if(numPrefijo.equals("491341")){
            tipoTarjeta = "BANORTE|CREDITO";
        }else if(numPrefijo.equals("491366")){
            tipoTarjeta = "BANORTE|CREDITO";
        }else if(numPrefijo.equals("491369")){
            tipoTarjeta = "BANORTE|CREDITO";
        }else if(numPrefijo.equals("491371")){
            tipoTarjeta = "BANORTE|CREDITO";
        }else if(numPrefijo.equals("491375")){
            tipoTarjeta = "BANORTE|CREDITO";
        }else if(numPrefijo.equals("491376")){
            tipoTarjeta = "BANORTE|CREDITO";
        }else if(numPrefijo.equals("491586")){
            tipoTarjeta = "BANORTE|CREDITO";
        }else if(numPrefijo.equals("493157")){
            tipoTarjeta = "BANORTE|CREDITO";
        }else if(numPrefijo.equals("493172")){
            tipoTarjeta = "BANORTE|CREDITO";
        }else if(numPrefijo.equals("493173")){
            tipoTarjeta = "BANORTE|CREDITO";
        }else if(numPrefijo.equals("544548")){
            tipoTarjeta = "BANORTE|CREDITO";
        }else if(numPrefijo.equals("544549")){
            tipoTarjeta = "BANORTE|CREDITO";
        }else if(numPrefijo.equals("544550")){
            tipoTarjeta = "BANORTE|CREDITO";
        }else if(numPrefijo.equals("547050")){
            tipoTarjeta = "BANORTE|CREDITO";
        }else if(numPrefijo.equals("547078")){
            tipoTarjeta = "BANORTE|CREDITO";
        }else if(numPrefijo.equals("547079")){
            tipoTarjeta = "BANORTE|CREDITO";
        }else if(numPrefijo.equals("547096")){
            tipoTarjeta = "BANORTE|CREDITO";
        }else if(numPrefijo.equals("547097")){
            tipoTarjeta = "BANORTE|CREDITO";
        }else if(numPrefijo.equals("5470")){
            tipoTarjeta = "BANORTE|CREDITO";
        }else if(numPrefijo.equals("474175")){
            tipoTarjeta = "BANREGIO|CREDITO";
        }else if(numPrefijo.equals("491566")){
            tipoTarjeta = "BANORTE|DEBITO";            
        }else if(numPrefijo.equals("435769")){
            tipoTarjeta = "BANSI|DEBITO";            
        }else if(numPrefijo.equals("439185")){
            tipoTarjeta = "CREDOMATICO DE MEXICO S.A. DE C.V.|CREDITO";
        }else if(numPrefijo.equals("439186")){
            tipoTarjeta = "CREDOMATICO DE MEXICO S.A. DE C.V.|CREDITO";
        }else if(numPrefijo.equals("439187")){
            tipoTarjeta = "CREDOMATICO DE MEXICO S.A. DE C.V.|CREDITO";
        }else if(numPrefijo.equals("416502")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("421316")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("426513")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("426514")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("430967")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("452421")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("456880")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("474176")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("491089")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("491279")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("491280")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("491281")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("491282")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("491283")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("491284")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("491294")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("491295")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("493166")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("522174")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("540956")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("541278")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("541286")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("541290")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("547068")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("547074")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("547092")){
            tipoTarjeta = "HSBC|CREDITO";
        }else if(numPrefijo.equals("404360")){
            tipoTarjeta = "INBURSA|CREDITO";
        }else if(numPrefijo.equals("431479")){
            tipoTarjeta = "INBURSA|CREDITO";
        }else if(numPrefijo.equals("465828")){
            tipoTarjeta = "INBURSA|DEBITO";
        }else if(numPrefijo.equals("465829")){
            tipoTarjeta = "INBURSA|DEBITO";
        }else if(numPrefijo.equals("473701")){
            tipoTarjeta = "INBURSA|CREDITO";
        }else if(numPrefijo.equals("462974")){
            tipoTarjeta = "INBURSA|CREDITO";
        }else if(numPrefijo.equals("545094")){
            tipoTarjeta = "INTERNACIONALES|DEBITO";
        }else if(numPrefijo.equals("601326")){
            tipoTarjeta = "INTERNACIONALES|DEBITO";
        }else if(numPrefijo.equals("421811")){
            tipoTarjeta = "INVEX|CREDITO";
        }else if(numPrefijo.equals("421812")){
            tipoTarjeta = "INVEX|CREDITO";
        }else if(numPrefijo.equals("463186")){
            tipoTarjeta = "INVEX|CREDITO";
        }else if(numPrefijo.equals("463187")){
            tipoTarjeta = "INVEX|CREDITO";
        }else if(numPrefijo.equals("492143")){
            tipoTarjeta = "INVEX|DEBITO";
        }else if(numPrefijo.equals("402318")){
            tipoTarjeta = "IXE|CREDITO";
        }else if(numPrefijo.equals("425981")){
            tipoTarjeta = "IXE|CREDITO";
        }else if(numPrefijo.equals("425982")){
            tipoTarjeta = "IXE|CREDITO";
        }else if(numPrefijo.equals("425984")){
            tipoTarjeta = "IXE|DEBITO";
        }else if(numPrefijo.equals("433454")){
            tipoTarjeta = "IXE|DEBITO";
        }else if(numPrefijo.equals("512745")){
            tipoTarjeta = "IXE|CREDITO";
        }else if(numPrefijo.equals("407399")){
            tipoTarjeta = "PROSA|CREDITO";
        }else if(numPrefijo.equals("426808")){
            tipoTarjeta = "PROSA|DEBITO";
        }else if(numPrefijo.equals("512029")){
            tipoTarjeta = "PROSA BANCA AFIRME|CREDITO";
        }else if(numPrefijo.equals("539672")){
            tipoTarjeta = "PROSA BANCO AFIRME|CREDITO";
        }else if(numPrefijo.equals("400605")){
            tipoTarjeta = "PROSA BANCO FACIL S.A.|CREDITO";
        }else if(numPrefijo.equals("517844")){
            tipoTarjeta = "PROSA CREDOMATIC MEXICO|CREDITO";
        }else if(numPrefijo.equals("524385")){
            tipoTarjeta = "PROSA CREDOMATIC MEXICO|CREDITO";
        }else if(numPrefijo.equals("525797")){
            tipoTarjeta = "PROSA CREDOMATIC MEXICO|CREDITO";
        }else if(numPrefijo.equals("406129")){
            tipoTarjeta = "PROSA SPIRA MEXICO SA DE CV|CREDITO";
        }else if(numPrefijo.equals("446130")){
            tipoTarjeta = "PROSA SPIRA MEXICO SA DE CV|CREDITO";
        }else if(numPrefijo.equals("446137")){
            tipoTarjeta = "PROSA SPIRA MEXICO SA DE CV|CREDITO";
        }else if(numPrefijo.equals("446138")){
            tipoTarjeta = "PROSA SPIRA MEXICO SA DE CV|CREDITO";
        }else if(numPrefijo.equals("446139")){
            tipoTarjeta = "PROSA SPIRA MEXICO SA DE CV|CREDITO";
        }else if(numPrefijo.equals("454747")){
            tipoTarjeta = "SANTANDER SERFIN|CREDITO";
        }else if(numPrefijo.equals("491327")){
            tipoTarjeta = "SANTANDER SERFIN|CREDITO";
        }else if(numPrefijo.equals("491512")){
            tipoTarjeta = "SANTANDER SERFIN|CREDITO";
        }else if(numPrefijo.equals("491572")){
            tipoTarjeta = "SANTANDER SERFIN|CREDITO";
        }else if(numPrefijo.equals("491573")){
            tipoTarjeta = "SANTANDER SERFIN|CREDITO";
        }else if(numPrefijo.equals("493135")){
            tipoTarjeta = "SANTANDER SERFIN|CREDITO";
        }else if(numPrefijo.equals("493136")){
            tipoTarjeta = "SANTANDER SERFIN|CREDITO";
        }else if(numPrefijo.equals("494133")){
            tipoTarjeta = "SANTANDER SERFIN|CREDITO";
        }else if(numPrefijo.equals("494134")){
            tipoTarjeta = "SANTANDER SERFIN|CREDITO";
        }else if(numPrefijo.equals("547146")){
            tipoTarjeta = "SANTANDER SERFIN|CREDITO";
        }else if(numPrefijo.equals("540845")){
            tipoTarjeta = "SANTANDER SERFIN|CREDITO";
        }else if(numPrefijo.equals("545307")){
            tipoTarjeta = "SANTANDER SERFIN|CREDITO";
        }else if(numPrefijo.equals("547015")){
            tipoTarjeta = "SANTANDER SERFIN|CREDITO";
        }else if(numPrefijo.equals("547046")){
            tipoTarjeta = "SANTANDER SERFIN|CREDITO";
        }else if(numPrefijo.equals("547157")){
            tipoTarjeta = "SANTANDER SERFIN|CREDITO";
        }else if(numPrefijo.equals("547484")){
            tipoTarjeta = "SANTANDER SERFIN|CREDITO";
        }else if(numPrefijo.equals("554900")){
            tipoTarjeta = "SANTANDER SERFIN|CREDITO";
        }else if(numPrefijo.equals("557905")){
            tipoTarjeta = "SANTANDER SERFIN|DEBITO";
        }else if(numPrefijo.equals("491871")){
            tipoTarjeta = "SCOTIABANK INVERLAT|CREDITO";
        }else if(numPrefijo.equals("491872")){
            tipoTarjeta = "SCOTIABANK INVERLAT|CREDITO";
        }else if(numPrefijo.equals("491873")){
            tipoTarjeta = "SCOTIABANK INVERLAT|CREDITO";
        }else if(numPrefijo.equals("544204")){
            tipoTarjeta = "SCOTIABANK INVERLAT|CREDITO";
        }else if(numPrefijo.equals("545375")){
            tipoTarjeta = "SCOTIABANK INVERLAT|CREDITO";
        }else if(numPrefijo.equals("547407")){
            tipoTarjeta = "SCOTIABANK INVERLAT|CREDITO";
        }else if(numPrefijo.equals("552263")){
            tipoTarjeta = "SCOTIABANK INVERLAT|CREDITO";
        }else if(numPrefijo.equals("537830")){
            tipoTarjeta = "WALMART|MASTER CARD";
        }else{
            tipoTarjeta = "invalida";
        }
        return tipoTarjeta;
    }
    
    
}
