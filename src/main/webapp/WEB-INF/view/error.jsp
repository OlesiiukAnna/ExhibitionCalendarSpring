<%--
  Created by IntelliJ IDEA.
  User: olesiiuk
  Date: 23.05.20
  Time: 20:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang}"/>
<fmt:setBundle basename="messages"/>
<html lang="${param.lang}">
<head>
    <title>Exhibition Calendar | Error</title>
    <jsp:include page="parts/head-bootstrap.jsp"/>
</head>
<body>
<header>
    <jsp:include page="parts/header.jsp"/>
</header>
<div class="container">
    <div class="row">
        <div class="col-md-offset-4 col-md-4">
            <div class="col align-self-center">
                <h1><fmt:message key="headline.error"/></h1>
                <p><fmt:message key="info.error.text"/></p>
            </div>
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary-info" role="button">
                <fmt:message key="ref.goToMainPage"/>
            </a>
        </div>
    </div>
</div>
</body>
</html>
