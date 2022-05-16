<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<title>Application</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6"
	crossorigin="anonymous">


</head>
<body>
	<div class="container">



		<div>
			<h3>Application Web Java avec Spring</h3>
			<p>Cette application a été developpée dans un cadre pédagogique à l'ENSAH Al Hoceima par <a target="_blank" href="https://boudaa.github.io/"> Tarik BOUDAA</a> </p>
			<p>Elle se base sur une architecture JEE en couches. Elle est implémentée avec les outils suivants: </p>
			<ul>
			<li>Spring</li>
			<li>Spring MVC</li>
			<li>Hibernate</li>
			<li>Spring Security</li>
			<li>MariaDB</li>
			<li>Passay</li>
			<li>Log4j</li>
			</ul>
			
		</div>
		<div>
		

		<p>Vous pouvez accéder à l'application 
		<a href="${pageContext.request.contextPath}/showMyLoginPage"> en cliquant ici </a>  </p>


		</div>


	</div>
</body>
</html>