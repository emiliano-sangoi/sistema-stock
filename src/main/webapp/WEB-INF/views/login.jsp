<!-- 
	https://www.mkyong.com/spring-security/spring-security-form-login-example/ 
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="container">
<header class="well">
	<h1 class="text-center">Sistema de Gesti&oacute;n de Stock</h1>
</header>
<div class="row">
	<div class="col-sm-6">
		<h2>Bienvenido!!!</h2>
		<br/>
		<p class="lead">Para ingresar utilice 
			<span class="negrita bg-ni-idea"><em>admin</em></span>
			como nombre de usuario y contraseña.
		
		</p>
	</div>
	<div class="col-sm-6">
		<c:url value="/login" var="loginVar" />
		<form action="${loginVar}" method="post" id="formLogin" class="bg-success">
			<div class="form-group">
				<label for="username">Nombre de usuario:</label> 
				<input type="text" required="true"
					class="form-control" name="username" placeholder="Ingrese el nombre de usuario">
			</div>
			<div class="form-group">
				<label for="password">Contraseña:</label> 
				<input type="password" required="true"
					class="form-control" name="password" placeholder="Ingrese la contraseña">
			</div>

			<c:if test="${param.error != null}">
				<p class="alert alert-danger">Usuario y/o contraseña incorrectos</p>
			</c:if>
			<button type="submit" class="btn btn-primary">			
			Ingresar
			</button>
		</form>

	</div>
</div>
</div>

