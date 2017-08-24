<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>        
        <meta charset="utf-8">
    	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    	<meta name="viewport" content="width=device-width, initial-scale=1">        
        
        <title><tiles:getAsString name="title" /></title>
        <style type="text/css">
		    span.error {
				color: red;
				}
			div.errors {
				background-color: #ffcccc;
				border: 2px solid red;
			}
			label.error {
				color: red;
			}
			input.error {
				background-color: #ffcccc;
			}
		</style>
		
		<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/assets/css/custom-style.css">



		
		<!-- Bootstrap CSS Core -->
		<!-- Latest compiled and minified CSS -->
		<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
		
		<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
		<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
		<!--[if lt IE 9]>
		      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
		      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
		    <![endif]-->
</head>
    <body>
    
    	<div class="container">	     		     
	     	
	        <div class="header-tile">
	            <tiles:insertAttribute name="header" />
	        </div>
	
	        <div class="content-tile">
	            <tiles:insertAttribute name="body" />
	        </div>
	
	        <div class="footer-tile">
	            <tiles:insertAttribute name="footer" />
	        </div>   
        
        </div>
        
		<!--  JS -->
		
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        
        <!-- Bootstrap CSS Core - Latest compiled and minified JavaScript -->
		<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
        
         
    </body>
</html>


