<%-- 
    Document   : plantillaSGFENS
    Created on : 25-oct-2012, 12:13:49
    Author     : ISCesarMartinez poseidon24@hotmail.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user==null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
}else{
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../include/keyWordSEO.jsp" />

        <title><jsp:include page="../include/titleApp.jsp" /></title>
        
        <jsp:include page="../include/skinCSS.jsp" />

        <jsp:include page="../include/jsFunctions.jsp"/>
        
    </head>
    <body>
        <div class="content_wrapper">
            
            <jsp:include page="../include/header.jsp" flush="true"/>
            
            <jsp:include page="../include/leftContent.jsp"/>
            
            <!-- Inicio de Contenido -->
            <div id="content">
                
                <div class="inner">
                    <h1>Titulo Página</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>
                    
                    <!--Menu de accesos rápidos (preferentemente solo para inicio)-->
                    <jsp:include page="../include/menu.jsp"/>
                    
                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <br class="clear"/>
			<!-- Begin graph window -->
			<div class="onecolumn">
				<div class="header">
					<span>Charts</span>
					<div class="switch" style="width:200px">
						<table width="200px" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
								<td>
									<input type="button" id="chart_bar" name="chart_bar" class="left_switch active" value="Bar" style="width:50px"/>
								</td>
								<td>
									<input type="button" id="chart_area" name="chart_area" class="middle_switch" value="Area" style="width:50px"/>
								</td>
								<td>
									<input type="button" id="chart_pie" name="chart_pie" class="middle_switch" value="Pie" style="width:50px"/>
								</td>
								<td>
									<input type="button" id="chart_line" name="chart_line" class="right_switch" value="Line" style="width:50px"/>
								</td>
							</tr>
						</tbody>
						</table>
					</div>
				</div>
				<br class="clear"/>
				<div class="content">
					<div id="chart_wrapper" class="chart_wrapper"></div>
					<br class="clear"/>
					<div class="alert_info">
						<p>
							<img src="images/icon_info.png" alt="success" class="mid_align"/>
							Click on each cell to change its value and graph.
						</p>
					</div>
					<br class="clear"/>
					<form id="form_data" name="form_data" action="" method="post">
						<table id="graph_data" class="data" rel="bar" cellpadding="0" cellspacing="0" width="100%">
						<caption>2009/2010 Sales by industry (Million)</caption>
						<thead>
							<tr>
								<td class="no_input">&nbsp;</td>
								<th>Accounting</th>
								<th>Banking</th>
								<th>Beauty Care</th>
								<th>Insurance</th>
								<th>Internet</th>
								<th>Media</th>
								<th>Telecomm</th>
								<th>Transportation</th>
							</tr>
						</thead>
						
						<tbody>
							<tr>
								<th>2009</th>
								<td>10</td>
								<td>3</td>
								<td>4</td>
								<td>9</td>
								<td>14</td>
								<td>2</td>
								<td>7</td>
								<td>12</td>
							</tr>
							
							<tr>
								<th>2010</th>
								<td>12</td>
								<td>5</td>
								<td>5</td>
								<td>6</td>
								<td>11</td>
								<td>7</td>
								<td>9</td>
								<td>16</td>
							</tr>
							
						</tbody>
						</table>
						<div id="chart_wrapper" class="chart_wrapper"></div>
					<!-- End bar chart table-->
					</form>
				</div>
			</div>
			<!-- End graph window -->
			
			
			<!-- Begin one column window -->
			<div class="onecolumn">
				<div class="header">
					<span>List data</span>
				</div>
				<br class="clear"/>
				<div class="content">
					<form id="form_data" name="form_data" action="" method="post">
						<table class="data" width="100%" cellpadding="0" cellspacing="0">
							<thead>
								<tr>
									<th style="width:10px">
										<input type="checkbox" id="check_all" name="check_all"/>
									</th>
									<th style="width:30%">Column 1</th>
									<th style="width:20%">Column 2</th>
									<th style="width:30%">Column 3</th>
									<th style="width:15%">Column 4</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>
										<input type="checkbox"/>
									</td>
									<td>Maecenas lacinia orci at neque</td>
									<td><a href="">Sit amet</a></td>
									<td>Consectetur adipiscing</td>
									<td>
										<a href=""><img src="images/icon_edit.png" alt="edit" class="help" title="Edit"/></a>
										<a href=""><img src="images/icon_delete.png" alt="delete" class="help" title="Delete"/></a>
									</td>
								</tr>
								<tr>
									<td>
										<input type="checkbox"/>
									</td>
									<td>Maecenas lacinia orci at neque</td>
									<td><a href="">Sit amet</a></td>
									<td>Consectetur adipiscing</td>
									<td>
										<a href=""><img src="images/icon_edit.png" alt="edit" class="help" title="Edit"/></a>
										<a href=""><img src="images/icon_delete.png" alt="delete" class="help" title="Delete"/></a>
									</td>
								</tr>
								<tr>
									<td>
										<input type="checkbox"/>
									</td>
									<td>Maecenas lacinia orci at neque</td>
									<td><a href="">Sit amet</a></td>
									<td>Consectetur adipiscing</td>
									<td>
										<a href=""><img src="images/icon_edit.png" alt="edit" class="help" title="Edit"/></a>
										<a href=""><img src="images/icon_delete.png" alt="delete" class="help" title="Delete"/></a>
									</td>
								</tr>
								<tr>
									<td>
										<input type="checkbox"/>
									</td>
									<td>Maecenas lacinia orci at neque</td>
									<td><a href="">Sit amet</a></td>
									<td>Consectetur adipiscing</td>
									<td>
										<a href=""><img src="images/icon_edit.png" alt="edit" class="help" title="Edit"/></a>
										<a href=""><img src="images/icon_delete.png" alt="delete" class="help" title="Delete"/></a>
									</td>
								</tr>
							</tbody>
						</table>
						<div id="chart_wrapper" class="chart_wrapper"></div>
					<!-- End bar chart table-->
					</form>
					
					<!-- Begin pagination -->
						<div class="pagination">
							<a href="#">«</a>
							<a href="#" class="active">1</a>
							<a href="#">2</a>
							<a href="#">3</a>
							<a href="#">4</a>
							<a href="#">5</a>
							<a href="#">6</a>
							<a href="#">»</a>
						</div>
					<!-- End pagination -->
					
				</div>
			</div>
			<!-- End one column window -->
			
			
			<!-- Begin two column window -->
			
			<!-- Begin left column window -->
			<div class="twocolumn">
				<div class="column_left">
					<div class="header">
						<span>Form elements</span>
					</div>
					<br class="clear"/>
					<div class="content">
						<div class="alert_warning" style="margin-top:0">
							<p>
								<img src="images/icon_warning.png" alt="success" class="mid_align"/>
								Warning Notification.
							</p>
						</div>
						<div class="alert_info">
							<p>
								<img src="images/icon_info.png" alt="success" class="mid_align"/>
								Information Notification.
							</p>
						</div>
						<div class="alert_success">
							<p>
								<img src="images/icon_accept.png" alt="success" class="mid_align"/>
								Success Notification.
							</p>
						</div>
						<div class="alert_error">
							<p>
								<img src="images/icon_error.png" alt="delete" class="mid_align"/>
								Error Notification.
							</p>
						</div>
						<br class="clear"/>
						
						<h2>Headline</h2><br/>
						<p>
							<label>Text input label:</label><br/>
							<input type="text" id="text_input1" style="width:300px"/>
						</p>
						<br/>
						<p>
							<label>Date input label:</label><br/>
							<input type="text" id="datepicker" name="datepicker" style="width:300px"/>
						</p>
						<br/>
						<p>
							<label>Textarea label:</label><br/>
							<textarea rows="5" cols="35"></textarea>
						</p>
						<br/>
						<p>
							<select>
            	      			<optgroup label="Apple">
            	      				<option>iPad</option>
            	      				<option>iPhone</option>
            	      				<option>Macbook</option>
            	      			</optgroup>
            	      			<optgroup label="Microsoft">
            	      				<option>Window 7</option>
            	      				<option>Zune</option>
            	      				<option>XBox 360</option>
            	      			</optgroup>
            	    		</select>
						</p>
						<br/>
						<p>
							<input type="checkbox" class="checkbox" checked="checked" id="cbdemo1"> <label for="cbdemo1">Checkbox label</label>
							<input type="radio" checked="checked" class="radio"> <label>Radio button label</label> 
						</p>
						<br/><br/>
						<p>
							<input type="button" id="btn_modal" value="Open modal window"/>
						</p>
					</div>
				</div>
				<!-- End left column window -->
				
				<!-- Begin right column window -->
				<div class="column_right">
					<div class="header">
						<span>Text style and photos</span>
					</div>
					<br class="clear"/>
					<div class="content">
						<h3>Headline</h3>
						<ul class="style">
            	  			<li>Cupidatat non</li>
            	 			<li>Officia deserunt mollit</li>
            	  			<li>Velit esse cillum</li>
            			</ul>
            			<h4>Headline</h4>
						<ol class="style">
            	  			<li>Cupidatat non</li>
            	 			<li>Officia deserunt mollit</li>
            	  			<li>Velit esse cillum</li>
            			</ol>
            			<p>
							Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed in porta lectus. Maecenas dignissim enim quis ipsum 
							mattis aliquet. Maecenas id velit et elit gravida bibendum. Duis nec rutrum lorem. Donec egestas metus a risus 
							euismod ultricies. Maecenas lacinia orci at neque commodo commodo. Donec egestas metus a risus 
							euismod ultricies. 
						</p>
						<span class="quote">
							&quot;This is a blockquote - Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam vel enim sed arcu tempor ornare id sed est. Nunc quis justo at&quot;
						</span>
						
						<!-- Begin media modal window -->
						<ul class="media_photos">
	  		  				<li>
	  		  					  <a rel="slide" href="gallery/1.jpg" title="Kobe, Osaka and Kyoto JAPAN">
	  		  					  	<img src="gallery/1_t.jpg" alt="Kobe, Osaka and Kyoto JAPAN"/>
	  		  					  </a>
	  		  				</li>
	  		  				
	  		  				<li>
	  		  					  <a rel="slide" href="gallery/2.jpg" title="Ji's wedding">
	  		  					  	<img src="gallery/2_t.jpg" alt="Ji's wedding"/>
	  		  					  </a>
	  		  				</li>
	  		  				
	  		  				<li>
	  		  					  <a rel="slide" href="gallery/3.jpg" title="BANGKOK-CHIENGKARN">
	  		  					  	<img src="gallery/3_t.jpg" alt="BANGKOK-CHIENGKARN"/>
	  		  					  </a>
	  		  				</li>
	  		  				<li>
	  		  					  <a rel="slide" href="gallery/1.jpg" title="Kobe, Osaka and Kyoto JAPAN">
	  		  					  	<img src="gallery/1_t.jpg" alt="Kobe, Osaka and Kyoto JAPAN"/>
	  		  					  </a>
	  		  				</li>
	  		  				
	  		  				<li>
	  		  					  <a rel="slide" href="gallery/2.jpg" title="Ji's wedding">
	  		  					  	<img src="gallery/2_t.jpg" alt="Ji's wedding"/>
	  		  					  </a>
	  		  				</li>
	  		  				
	  		  				<li>
	  		  					  <a rel="slide" href="gallery/3.jpg" title="BANGKOK-CHIENGKARN">
	  		  					  	<img src="gallery/3_t.jpg" alt="BANGKOK-CHIENGKARN"/>
	  		  					  </a>
	  		  				</li>
	  					</ul>
	  					<!-- End media modal window -->
					</div>
				</div>
				<!-- End right column window -->
			</div>
			<!-- End two column window -->
			
			
			<br class="clear"/>
			
			
			<!-- Begin one column tab content window -->
			<div class="onecolumn">
				<div class="header">
					<span>Tab contents</span>
					<div class="switch" style="width:150px">
						<table width="150px" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
								<td>
									<input type="button" id="tab1" name="tab1" class="left_switch" value="Tab1" style="width:50px"/>
								</td>
								<td>
									<input type="button" id="tab2" name="tab2" class="middle_switch" value="Tab2" style="width:50px"/>
								</td>
								<td>
									<input type="button" id="tab3" name="tab3" class="right_switch active" value="Tab3" style="width:50px"/>
								</td>
							</tr>
						</tbody>
						</table>
					</div>
				</div>
				<br class="clear"/>
				<div class="content">
					<div id="tab1_content" class="tab_content hide">
						<h4>Headline of tab 1</h4>
						<ol class="style">
            	  			<li>Cupidatat non</li>
            	 			<li>Officia deserunt mollit</li>
            	  			<li>Velit esse cillum</li>
            			</ol>
            			<p>
							Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed in porta lectus. Maecenas dignissim enim quis ipsum 
							mattis aliquet. Maecenas id velit et elit gravida bibendum. Duis nec rutrum lorem. Donec egestas metus a risus 
							euismod ultricies. Maecenas lacinia orci at neque commodo commodo. Donec egestas metus a risus 
							euismod ultricies. 
						</p>
					</div>
					<div id="tab2_content" class="tab_content hide">
						<h4>Headline of tab 2</h4>
            			<p>
							Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed in porta lectus. Maecenas dignissim enim quis ipsum 
							mattis aliquet. Maecenas id velit et elit gravida bibendum. Duis nec rutrum lorem. Donec egestas metus a risus 
							euismod ultricies. Maecenas lacinia orci at neque commodo commodo. Donec egestas metus a risus 
							euismod ultricies. 
						</p>
						<ul class="media_photos">
	  		  				<li>
	  		  					  <a rel="slide" href="gallery/1.jpg" title="Kobe, Osaka and Kyoto JAPAN">
	  		  					  	<img src="gallery/1_t.jpg" alt="Kobe, Osaka and Kyoto JAPAN"/>
	  		  					  </a>
	  		  				</li>
	  		  				
	  		  				<li>
	  		  					  <a rel="slide" href="gallery/2.jpg" title="Ji's wedding">
	  		  					  	<img src="gallery/2_t.jpg" alt="Ji's wedding"/>
	  		  					  </a>
	  		  				</li>
	  		  				
	  		  				<li>
	  		  					  <a rel="slide" href="gallery/3.jpg" title="BANGKOK-CHIENGKARN">
	  		  					  	<img src="gallery/3_t.jpg" alt="BANGKOK-CHIENGKARN"/>
	  		  					  </a>
	  		  				</li>
	  		  				<li>
	  		  					  <a rel="slide" href="gallery/1.jpg" title="Kobe, Osaka and Kyoto JAPAN">
	  		  					  	<img src="gallery/1_t.jpg" alt="Kobe, Osaka and Kyoto JAPAN"/>
	  		  					  </a>
	  		  				</li>
	  		  				
	  		  				<li>
	  		  					  <a rel="slide" href="gallery/2.jpg" title="Ji's wedding">
	  		  					  	<img src="gallery/2_t.jpg" alt="Ji's wedding"/>
	  		  					  </a>
	  		  				</li>
	  		  				
	  		  				<li>
	  		  					  <a rel="slide" href="gallery/3.jpg" title="BANGKOK-CHIENGKARN">
	  		  					  	<img src="gallery/3_t.jpg" alt="BANGKOK-CHIENGKARN"/>
	  		  					  </a>
	  		  				</li>
	  					</ul>
	  					<br class="clear"/>
					</div>
					<div id="tab3_content" class="tab_content">
						<div class="alert_success">
							<p>
								<img src="images/icon_accept.png" alt="success" class="mid_align"/>
								Successfully display tab 3
							</p>
						</div>
            			<p>
							Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed in porta lectus. Maecenas dignissim enim quis ipsum 
							mattis aliquet. Maecenas id velit et elit gravida bibendum. Duis nec rutrum lorem. Donec egestas metus a risus 
							euismod ultricies. Maecenas lacinia orci at neque commodo commodo. Donec egestas metus a risus 
							euismod ultricies. 
						</p>
					</div>
				</div>
			</div>
			<!-- End one column tab content window -->
			
			
			<!-- Begin three column window -->
			<div class="onecolumn" style="padding-bottom:20px">
				<div class="header">
					<span>Three column window</span>
				</div>
				<br class="clear"/>
				<div class="content">
					<div class="alert_info">
						<p>
							<img src="images/icon_info.png" alt="success" class="mid_align"/>
							Drag one of three inner window and drop to another to sort it.
						</p>
					</div>
					<br class="clear"/>
					<h4>Try with three column window.</h4>
					<p>
						Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed in porta lectus. Maecenas dignissim enim quis ipsum mattis aliquet. Maecenas id velit et elit gravida bibendum. Duis nec rutrum lorem. Donec egestas metus a risus euismod ultricies. Maecenas lacinia orci at neque commodo commodo. Donec egestas metus a risus euismod ultricies.
					</p>
					<div id="threecolumn" class="threecolumn">
						<div class="threecolumn_each">
							<div class="header">
								<span>Column1</span>
							</div>
							<br class="clear"/>
							<div class="content">
								Your contents go here.
							</div>
						</div>
						<div class="threecolumn_each">
							<div class="header">
								<span>Column2</span>
							</div>
							<br class="clear"/>
							<div class="content">
								Your contents go here.
							</div>
						</div>
						<div class="threecolumn_each">
							<div class="header">
								<span>Column3</span>
							</div>
							<br class="clear"/>
							<div class="content">
								Your contents go here.
							</div>
						</div>
					</div>
				</div>
				<br class="clear"/>
			</div>
			<!-- End three column window -->
			

			<!-- Begin one column wysiwyg window -->
			<div class="onecolumn">
				<div class="header">
					<span>WYSIWYG textarea</span>
				</div>
				<br class="clear"/>
				<div class="content">
					<textarea id="wysiwyg"></textarea>
				</div>
			</div>
			<!-- End one column wysiwyg window -->
                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    
                </div>
                
                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>


    </body>
</html>
<%}%>