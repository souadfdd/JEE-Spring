<%--
  Created by IntelliJ IDEA.
  User: Administrateur
  Date: 02/03/2022
  Time: 22:30
  To change this template use File | Settings | File Templates.
--%>

<%@page import="com.app.Message" %>
<html>
<head>
    <title>Title</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">

</head>
<body >
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/back/bestScore">Meilleurs
        scores</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link " href="${pageContext.request.contextPath}/back/ReinitGameServlet">Reinitialiser le jeu </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/DeconnectServlet">Se deconnecter</a>
            </li>
        </ul>
    </div>
</nav>

<p class="m-2"><strong>Bonjour</strong>
    <span  style="color:#534340;font-family: 'Noto Serif'"><c:out value="${sessionScope.gameState.user.nom}" /><br></span>
</p>

<h1 style="color:green;font-family: 'Noto Serif', serif;" class="m-3">Jeu de dé</h1>
<form method="post" action="${pageContext.request.contextPath}/back/GameServlet" class="m-2">
    <p>Entrer le numero du cle   lancer puis cliquer sur le button</p>
    <div class="form-group m-1">
        <label>Numero de cle </label>
        <input type="text" name="cle"  class="form-control w-25">
    </div>
    <input type="submit" value="lancer le joue" class="btn m-1" style="color:white; background-color: green;">
</form>
<fieldset class="border border-2 rounded p-5 border-success w-25 m-1">
    <legend>Resultat</legend>
    <p><c:out value="| Votre score ${sessionScope.gameState.user.score} |Meilleaur score :${sessionScope.gameState.user.bestScore}"></c:out></p>
    <%--        <p style="color:green;"><c:out value=":${ sessionScope.gameState.user.Score } " /></p>--%>
</fieldset>
<div>
    <ul>

        <c:forEach items="${sessionScope.gameState.messages}" var="msg">


            <c:choose>
                <c:when test="${msg.type == Message.WARN}">
                    <li style="color:yellow">${msg.text}</li>
                </c:when>
                <c:when test="${msg.type == Message.INFO}">
                    <li style="color:green">${msg.text}</li>
                </c:when>
                <c:when test="${msg.type == Message.ERROR}">
                    <li style="color:red">${msg.text}</li>
                </c:when>
                <c:otherwise>
                    <li >${msg.text}</li>
                </c:otherwise>
            </c:choose>

        </c:forEach>
    </ul>
</div>



</body>
</html>
