<!-- Nuevo producto -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="row">
	<div class="text-left col-xs-6">
		<h1>Nuevo producto</h1>
		<hr />
	</div>
	<div class="text-right col-xs-6">
		<div class="btn-group" role="group" aria-label="...">
			<a href="/tp_final/productos" class="btn btn-warning  btn-md"> 
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

<form:form method="POST" action="/tp_final/productos/nuevo"
	modelAttribute="product">

	<div class="form-group">
		<form:label path="codigo">
			C&oacute;digo del producto: <span class="text-danger">*</span>
		</form:label>
		<form:input path="codigo" cssClass="form-control" />
	</div>
	<div>
		<form:errors path="codigo" element="p" cssClass="alert alert-danger" ></form:errors>
		
		<c:if test="${producto_existe == false}">
			<p class="alert alert-danger">El c&oacute;digo ingresado no corresponde a ning&uacute;n producto existente.</p>
		</c:if>
		
	</div>

	<div class="form-group">
		<form:label path="descripcion">
			Descripci&oacute;n: <span class="text-danger">*</span>
		</form:label>
		<form:input path="descripcion" cssClass="form-control" />
	</div>
	<div>
		<form:errors path="descripcion" element="p" cssClass="alert alert-danger" ></form:errors>
	</div>

	

		<div class="row">
			<div class="col-xs-6">
				<div class="form-group">
					<form:label path="precio">
				Precio: <span class="text-danger">*</span>
					</form:label>
					<form:input path="precio" cssClass="form-control"  />
				</div>
				<div>
					<form:errors path="precio" element="p" cssClass="alert alert-danger" ></form:errors>					
				</div>
			</div>

			<div class="col-xs-6">
				<div class="form-group">
					<form:label path="cantidad">Cantidad: <span
							class="text-danger">*</span>
					</form:label>
					<form:input path="cantidad" cssClass="form-control" />
				</div>
				<div>
					<form:errors path="cantidad" element="p" cssClass="alert alert-danger" ></form:errors>					
				</div>
			</div>

		</div>





	<button type="submit" class="btn btn-primary">
		<span class="glyphicon glyphicon-save" aria-hidden="true"></span>
		Guardar
	</button>

</form:form>