<%-- 
    Document   : guiaDinamicaTest
    Created on : 30/06/2015, 12:24:53 PM
    Author     : leonardo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.0/jquery.min.js"></script>
        <script type='text/javascript'>
        
        //<![CDATA[
        $(document).ready(function(){
          //$("#the_lights").fadeTo(1,0);
          $("#turnoff").click(function () {
            $("#the_lights").css({'display' : 'block'});
            $("#the_lights").fadeTo("slow",1);
          });
          $("#soft").click(function () {
            document.getElementById("the_lights").style.display="block";
            $("#the_lights").fadeTo("slow",0.8);
          });
          $("#turnon").click(function () {
            //document.getElementById("the_lights").style.display="block";
            $("#the_lights").fadeTo("slow",0);
            document.getElementById("the_lights").style.display="none";
          });
        });
        //]]>

        </script>

        <style>
        #the_lights{
        background-color: #000;
        display: none;
        height: 100%;
        left: 0;
        position: absolute;
        top: 0;
        width: 100%;
        }
        #standout{
        background-color: white;
        padding: 5px;
        position: relative;
        z-index: 1000;
        }
        </style>
        
    </head>
    <body>
        <h1>Hello World!</h1>
        
        <div id = "standout">
        <div id="turnoff"> LUCES NO </div>
        <div id="soft"> A MEDIA LUZ </div>
        <div id="turnon"> LUCES SI </div>
        <embed src="http://www.youtube.com/v/9X7pwsDWRJM&amp;hl=es_ES&amp;fs=1&amp;" type="application/x-shockwave-flash" allowscriptaccess="always" allowfullscreen="true" width="425" height="344">
        </div>
        
        <div id='the_lights'></div>
    </body>
</html>
