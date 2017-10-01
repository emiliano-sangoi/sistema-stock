<!--  
	Listado de pedidos
 -->
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="row">
	<div class="text-left col-xs-6">
		<h1>Pedidos</h1>
	</div>
	<div class="text-right col-xs-6">

		<div class="btn-group" role="group" aria-label="...">
			<a href="#menu-toggle" class="btn btn-default  btn-md" id="menu-toggle"> 
				<span class="glyphicon glyphicon-menu-hamburger" aria-hidden="true"></span> 
				Menu principal
			</a> 
			<a href="/tp_final/pedidos/nuevo" class="btn btn-success btn-md"> 
				<span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 
				Nuevo pedido
			</a>
		</div>

	</div>
</div>

<hr />
<p class="lead">Listado de pedidos existentes</p>

<div class="mgs-container"></div>

<div class="table-responsive">
		<table class="table table-hover table-stripped">
			<thead>
				<th>#</th>
				<th>Fecha de creación</th>
				<th>Estado</th>
				<th>Operaciones</th>
			</thead>					
			<tbody class="">
			
			<c:forEach var="pedido" items="${pedidos}" varStatus="status">
				
				<tr>
					<td>${status.count}</td>
					<td>
						<fmt:formatDate value="${pedido.date}" pattern="dd/MM/yyyy HH:mm:ss a"/>
					</td>
					<td>${pedido.status}</td>		
					<td>
						<a href="" class="btn btn-xs btn-success disabled" >
							<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
							Cambiar estado
						</a>
						<a href="/tp_final/pedidos/modificar/${pedido.id}" class="btn btn-xs btn-primary" >
							<span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
							Editar
						</a>
						<a href="" class="btn btn-xs btn-danger btn-eliminar disabled">
							<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
							Eliminar
						</a>
					</td>
				</tr>
			
			</c:forEach>

			</tbody>
		</table>
	</div>
