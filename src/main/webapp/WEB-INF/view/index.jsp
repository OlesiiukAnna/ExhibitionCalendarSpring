<%--
  Created by IntelliJ IDEA.
  User: olesiiuk
  Date: 23.05.20
  Time: 17:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang}"/>
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html lang="${param.lang}">
<head>
    <title>Exhibition Calendar</title>
    <jsp:include page="parts/head-bootstrap.jsp"/>
</head>
<body>
<header>
    <jsp:include page="parts/header.jsp"/>
</header>

<div class="jumbotron">
    <div class="container">
        <h1><fmt:message key="headline.welcome"/></h1>
        <p>
            <fmt:message key="info.greeting"/>
        </p>
    </div>
</div>

<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <h2><fmt:message key="headline.exhibitions"/></h2>
            <a href="${pageContext.request.contextPath}/exhibitions"><fmt:message key="ref.exhibitions"/></a>
        </div>
        <div class="col-md-6">
            <h2><fmt:message key="headline.exhibitionHalls"/></h2>
            <a href="${pageContext.request.contextPath}/exhibition-halls"><fmt:message key="ref.exhibitionHalls"/></a>
        </div>
    </div>
</div>
</body>
</html>
