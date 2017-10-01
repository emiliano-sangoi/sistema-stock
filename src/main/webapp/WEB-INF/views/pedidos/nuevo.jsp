<!-- Nuevo pedido -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="func" uri="http://java.sun.com/jsp/jstl/functions" %>


<div class="row">
	<div class="text-left col-xs-6">
		<h1>Nuevo pedido</h1>
		<hr />
	</div>
	<div class="text-right col-xs-6">
		<div class="btn-group" role="group" aria-label="...">
			<a href="/tp_final/pedidos" class="btn btn-warning  btn-md"> 
			<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
				Cancelar
			</a>
			<a href="#menu-toggle" class="btn btn-default  btn-md"
				id="menu-toggle"> <span
				class="glyphicon glyphicon-menu-hamburger" aria-hidden="true"></span>
				Menu principal
			</a>
		</div>
	</div>
</div>

<p class="lead">A continuación, defina los productos incluidos en el pedido.</p>


<form:form method="POST" action="/tp_final/pedidos/nuevo" 
	modelAttribute="order" id="idFormPedido"	
	data-proximo="${func:length(order.products) }">	
	
	<div class="well">
		
		<div class="row">
			<div class="col-xs-12 col-sm-1">				
				<h4 class="text-uppercase text-right">#</h4>
			</div>	
			<div class="col-xs-12 col-sm-5">
				<h4 class="text-uppercase">C&oacute;digo del producto: <span class="text-danger">*</span></h4>
				<hr/>
			</div>	
			<div class="col-xs-12 col-sm-4">
				<h4 class="text-uppercase">Cantidad: <span class="text-danger">*</span></h4>
				<hr/>
			</div>			
		</div>
	
		<div id="items-container">
		
			<form:input path="codOp" type="hidden" value="${order.codOp}" id="input-op" name="input-op" />
		
			<c:forEach var="item" items="${order.products}" varStatus="idx">
				<div class="row" >	
											
					<div class="col-xs-12 col-sm-1 negrita text-right">
						${idx.index + 1}
					</div>	
				
					<div class="col-xs-12 col-sm-5">
						<div class="form-group">
		                 	
		                 	<form:select path="products[${idx.index}].id" cssClass="form-control">
    							<form:options items="${productos}" 
    							itemValue="codigo" 
    							itemLabel="label" />
							</form:select>
							
							<div class="mt-xxxs">
		                		<form:errors path="products[${idx.index}].id" css="item-cod-prod" element="p" cssClass="alert alert-danger"  />
		                	</div>
		                 	
		                 	
		            	</div>			
					</div>		 
				 	<div class="col-xs-12 col-sm-4">
				 		<div class="form-group">
		                	<form:input path="products[${idx.index}].count" cssClass="form-control item-cant-prod" type="number" min="0" /> 
		                	<div class="mt-xxxs">
		                		<form:errors path="products[${idx.index}].count" element="p" cssClass="alert alert-danger item-cant-prod" />
		                	</div>	                 	
		            	</div>
				 	</div>
				 	<div class="col-xs-12 col-sm-1 text-right">
				 		<button class="btn btn-sm btn-danger borrar-item" data-idx="${idx.index}">
				 			<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
				 			Quitar
				 		</button>				 		
				 	</div>
				</div>
				
		    </c:forEach>

		<div class="row">
		
			<div class="col-xs-12 col-sm-offset-1 col-sm-10">
			    <c:if test="${ not empty errorItemsDuplicados }">
			    	<div class="alert alert-danger">
			    		${ errorItemsDuplicados }
			    	</div>
			    </c:if>		    			    
			</div>
				
		</div>
		
    
    </div>	
	
	<button class="btn btn-sm btn-success agregar-item">
 		<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
 			Agregar item
 	</button>
	


	<button type="submit" class="btn btn-sm btn-primary guardar-pedido">
		<span class="glyphicon glyphicon-save" aria-hidden="true"></span>
		Generar pedido
	</button>

</form:form>