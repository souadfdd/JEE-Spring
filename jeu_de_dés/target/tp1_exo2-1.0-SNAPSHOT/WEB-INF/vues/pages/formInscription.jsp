<html lang="en">
<head>
	<meta charset="utf-8">
	<meta name="viewport"
		  content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<meta name="description" content="">
	<meta name="author" content="">
	<link href="${pageContext.request.contextPath}/style/signin.css" rel="stylesheet">
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
	<title>App Game</title>
	<style>
		form{
			box-shadow: rgba(0, 0, 0, 0.25) 0px 54px 55px, rgba(0, 0, 0, 0.12) 0px -12px 30px, rgba(0, 0, 0, 0.12) 0px 4px 6px, rgba(0, 0, 0, 0.17) 0px 12px 13px, rgba(0, 0, 0, 0.09) 0px -3px 5px;
		}
		span{
			color:red;
		}
	</style>

</head>
<body>
<h1  class="m-5" style="color:green;text-align:center ;font-family: 'Noto Serif', serif;">Inscription</h1>
<div class="container " style="font-family: 'Noto Serif', serif;">
	<div class="row align-items-center justify-content-center align-items-center">
		<div class="col-6 con">
			<form  action="${pageContext.request.contextPath}/UserManagementServlet" method="POST" class="border border-2 rounded p-5 border-success ">
				<div class="form-group m-3">
					<label  class="mb-1">Nom<span class="requis">*</label>
					<input type="text" class="form-control p-2 "  placeholder="Nom" name="nom" size="20" maxlength="60">
				</div>
				<div class="form-group m-3">
					<label  class="mb-1">Prenom<span class="requis">*</label>
					<input type="text" class="form-control"  placeholder="prenom" name="prenom" size="20" maxlength="60">
				</div>
				<div class="form-group m-3">
					<label  class="mb-1">Nom d'utilisateur<span class="requis">*</label>
					<input type="text" class="form-control"  placeholder="login" name="login">
				</div>
				<div class="form-group m-3">
					<label for="inputPassword">Le mot de passe<span class="requis">*</label>
					<input type="password" class="form-control" id="inputPassword" placeholder="Password" name="password">
				</div>
				<button type="submit" class="btn m-3" style="color:white; background-color: green;">Envoyer</button>
			</form>
		</div>
	</div>
</div>
</body>

</html>