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
	
<!-- 	Font awesome	 -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"/>

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


	<div id="wrapper" class="">


		<!-- Sidebar -->
		<tiles:insertAttribute name="sidebar">
		</tiles:insertAttribute>


		<!-- Page Content -->
		<div id="page-content-wrapper">
			<div class="container-fluid">	
				<div class="mgs-container">
					<c:if test="${ not empty flashMsgText }">
						<div class="alert alert-${ flashMsgResult } alert-dismissable flash-msg">
							<p>
								<a href="#" class="close" data-dismiss="alert"
									aria-label="close">&times;</a>
									 <strong>${ flashMsgText }</strong> <span class="adv-msg"></span>
							</p>
						</div>
					</c:if>

				</div>			
									
				</div>
				<div class="row">
					<div class="col-lg-12">

						<!-- Page Content -->
						<tiles:insertAttribute name="page-content">
						</tiles:insertAttribute>

					</div>
				</div>
			</div>

		</div>
		<!-- /#page-content-wrapper -->
	
	</div>
	
	<!-- /#wrapper -->

	<!-- 	Templates -->
	<div class="alert alert-danger alert-dismissable hidden" id="advertencia_elim_registro">
  		<p>
  			<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
  			<strong>Advertencia!</strong> <span class="adv-msg"></span> 			
  		</p>
  		<br/>
  		<p class="text-left">
  			<a href="" class="btn btn-danger adv-btn-confirmar">Confirmar</a>
  		</p>
	</div>


	<!--  JS -->

	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	
	<script>
		$(document).ready(function(){
			// importar codigos para identificar la operaciones:
			ACTION_NUEVO_ITEM = ${Order.ACTION_NUEVO_ITEM};
			ACTION_GUARDAR_PEDIDO = ${Order.ACTION_GUARDAR_PEDIDO};
			ACTION_DEFAULT = ${Order.ACTION_DEFAULT};			
			
			$(".flash-msg").delay(5000).fadeOut();
			
		});			
	</script>

	<!-- Bootstrap CSS Core - Latest compiled and minified JavaScript -->
	<!-- <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script> -->
	<script src="${ pageContext.request.contextPath }/assets/js/bootstrap.min.js"></script>	
	<script src="${ pageContext.request.contextPath }/assets/js/custom.js"></script>
	
	
	

	<!-- Menu Toggle Script -->
	<script>
	    $("#menu-toggle").click(function(e) {    	    	
	        e.preventDefault();
	        $("#wrapper").toggleClass("toggled");
	    });
    
    
	    /**
	    	Muestra una advertencia antes de eliminar un producto.
	    */    
	    $('.btn-eliminar-recurso').click(function(e) {
	    	e.preventDefault();
	    	var elem = $(this);
	    	
	    	var alerta_recurso_id = elem.data('alerta-recurso-id');
	    	var alerta_recurso_desc = elem.data('alerta-recurso-desc');
	    	var alerta_action = elem.data("alerta-action");    	
	    		
	   		//buscar template y actualizar contenido:
	        var tpl = $('#advertencia_elim_registro').clone();
	       	tpl.removeClass('hidden').find('.adv-msg').html(" El elemento <strong>'" + alerta_recurso_desc + "'</strong>" + " se borrarra definitivamente. Esta seguro que desea realizar esta acci�n?");
	       		
	       	//actualizar url con el id seleccionado:
	       	var a = tpl.find('.adv-btn-confirmar');
	       	a.attr('href', alerta_action);
	       	
	       	//Mostrar mensaje
	       	$('.mgs-container').html('').append(tpl).hide().fadeIn();
	    		    		    	    	    	   
	    });       
    
    </script>

</body>
</html>


