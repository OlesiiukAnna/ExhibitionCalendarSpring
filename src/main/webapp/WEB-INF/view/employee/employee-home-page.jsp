<%--
  Created by IntelliJ IDEA.
  User: olesiiuk
  Date: 23.05.20
  Time: 20:29
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang}"/>
<fmt:setBundle basename="messages"/>
<html lang="${param.lang}">
<head>
    <title>Exhibition Calendar | Employee Home Page</title>
    <jsp:include page="../parts/head-bootstrap.jsp"/>
</head>
<body>
<header>
    <jsp:include page="../parts/header.jsp"/>
    <jsp:include page="../parts/employee-navbar.jsp"/>
</header>
<jsp:include page="../parts/home-page-body.jsp"/>
</body>
</html>
