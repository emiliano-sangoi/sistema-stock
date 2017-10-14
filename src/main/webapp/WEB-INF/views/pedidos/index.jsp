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

<div class="table-responsive">
		<table class="table table-hover table-stripped">
			<thead>
				<th>#</th>
				<th>C�digo</th>
				<th>Fecha de creaci�n</th>
				<th>Hora</th>
				<th>Estado</th>
				<th>Operaciones</th>
			</thead>					
			<tbody class="">
			
			<c:forEach var="pedido" items="${pedidos}" varStatus="status">
				
				<tr>
					<td>${status.count}</td>
					<td>${pedido.id}</td>
					<td>
						<fmt:formatDate value="${pedido.date}" pattern="dd/MM/yyyy"/>
					</td>
					<td>
						<fmt:formatDate value="${pedido.date}" pattern="HH:mm:ss a"/>
					</td>
					<td class="${pedido.isOpen() ? 'bg-warning' : 'bg-success' }">
						${pedido.status}						
					</td>		
					<td>
						<a href="/tp_final/pedidos/confirmar/${pedido.id}" class="btn btn-xs btn-success ${pedido.isOpen() ? '' : 'disabled' }" >
							<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
							Confirmar recepci�n
						</a>
						<a href="/tp_final/pedidos/modificar/${pedido.id}" class="btn btn-xs btn-primary ${pedido.isOpen() ? '' : 'disabled' }" >
							<span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
							Editar
						</a>
						<a href="" class="btn btn-xs btn-danger btn-eliminar-recurso" 
							data-alerta-recurso-id="${pedido.id}" 
							data-alerta-recurso-desc="${pedido.id}" 
							data-alerta-action="/tp_final/pedidos/borrar/${pedido.id}" >
												
							<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
							Eliminar
						</a>
					</td>
				</tr>
			
			</c:forEach>

			</tbody>
		</table>
	</div>
