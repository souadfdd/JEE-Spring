<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<title>Authentification form</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6"
	crossorigin="anonymous">


</head>
<body>
	<div class="container">



		<div>
			<h3>Authentification Form</h3>
		</div>
		<div>



			<f:form
				action="${pageContext.request.contextPath}/authenticateTheUser"
				method="POST">
				<c:if test="${param.error!=null}">
					<div class="alert alert-danger col-6">Please verify your login or password</div>
				</c:if>

				<c:if test="${param.logout != null}">

					<div class="alert alert-success col-6" >You are logged out of the system</div>

				</c:if>
				<div class="col-6">
					<label>Login</label> <input name="username" type="text"
						class="form-control" placeholder="Login" /> <label>Password</label>
					<input name="password" type="password" class="form-control"
						placeholder="Password" />

					<div style="text-align: center; margin-top:10px">
						<button type="submit" class="btn btn-primary">Se connecter</button>
					</div>
				</div>







			</f:form>
		</div>


	</div>
</body>
</html>