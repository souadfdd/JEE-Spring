<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<div class="collapse navbar-collapse" id="navbarNav">
	<ul class="navbar-nav">
		<li class="nav-item"><a class="nav-link active"
			aria-current="page"
			href="${pageContext.request.contextPath}/admin/showAdminHome">Home</a></li>
		<li class="nav-item"><a class="nav-link"
			href="${pageContext.request.contextPath}/admin/showForm">Add
				Person</a></li>
		<li class="nav-item"><a class="nav-link"
			href="${pageContext.request.contextPath}/admin/managePersons">Manage
				Persons</a></li>

		<li class="nav-item"><a class="nav-link"
			href="${pageContext.request.contextPath}/admin/createAccounts">Create Accounts</a></li>
		<li class="nav-item"><a class="nav-link"
			href="${pageContext.request.contextPath}/admin/manageAccounts">List
				Accounts</a></li>
		<li class="nav-item"><f:form
				action="${pageContext.request.contextPath}/logout" method="POST">

				<button type="submit" class="btn btn-link">logout</button>

			</f:form></li>


	</ul>
</div>