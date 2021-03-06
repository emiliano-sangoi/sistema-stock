<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="tp_final.model.Order" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, shrink-to-fit=no, initial-scale=1">
<meta name="description" content="Tp Final curso de Java 2017">
<meta name="author" content="Emiliano Sangoi">

<title><tiles:getAsString name="title" /></title>


<link rel="stylesheet" type="text/css"
	href="${ pageContext.request.contextPath }/assets/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="${ pageContext.request.contextPath }/assets/css/simple-sidebar.css">
<link rel="stylesheet" type="text/css"
	href="${ pageContext.request.contextPath }/assets/css/custom-style.css">
<link rel="stylesheet" type="text/css"
	href="${ pageContext.request.contextPath }/assets/css/spaces.min.css">		

<!-- Bootstrap CSS Core -->
<!-- Latest compiled and minified CSS -->
<!-- <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous"> -->

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>

	<div id="" class="container">

		<!-- Page Content -->
		<tiles:insertAttribute name="page-content">
		</tiles:insertAttribute>


	</div>
	
	<!--  JS -->

	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	
	<!-- Bootstrap CSS Core - Latest compiled and minified JavaScript -->
	<!-- <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script> -->
	<script
		src="${ pageContext.request.contextPath }/assets/js/bootstrap.min.js">		
	</script>
	
	<script src="${ pageContext.request.contextPath }/assets/js/custom.js"></script>

</body>
</html>