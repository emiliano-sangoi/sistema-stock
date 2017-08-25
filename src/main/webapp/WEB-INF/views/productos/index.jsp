<!-- Listado de productos -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="row">
	<div class="text-left col-xs-6">
		<h1>Productos</h1>
	</div>
	<div class="text-right col-xs-6">

		<div class="btn-group" role="group" aria-label="...">
			<a href="#menu-toggle" class="btn btn-default  btn-md" id="menu-toggle"> 
				<span class="glyphicon glyphicon-menu-hamburger" aria-hidden="true"></span> 
				Menu principal
			</a> 
			<a href="/tp_final/productos/nuevo" class="btn btn-success btn-md"> 
				<span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 
				Nuevo producto
			</a>
		</div>




	</div>
</div>

<hr />
<p class="lead">Listado de productos existentes</p>

<div class="table-responsive">
		<table class="table table-hover table-stripped">
			<thead>
				<th>Codigo</th>
				<th>Descripción</th>
				<th>Precio</th>
				<th>Cantidad</th>
				<th>Operaciones</th>
			</thead>					
			<tbody class="">
			
			<c:forEach var="producto" items="${productos}" varStatus="status">
				
				<tr>
					<td>${producto.codigo}</td>
					<td>${producto.descripcion}</td>
					<td>$ ${producto.precio}</td>
					<td>${producto.cantidad}</td>
					<td>
						<a href="" class="btn btn-xs btn-primary" >
							<span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
							Editar
						</a>
						<a href="" class="btn btn-xs btn-danger">
							<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
							Eliminar
						</a>
					</td>
				</tr>
			
			</c:forEach>

			</tbody>
		</table>
	</div>
