<%--
  Created by IntelliJ IDEA.
  User: olesiiuk
  Date: 23.05.20
  Time: 20:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang}"/>
<fmt:setBundle basename="messages"/>
<html lang="${param.lang}">
<head>
    <title>Exhibition Calendar | Exhibition Halls</title>
    <jsp:include page="parts/head-bootstrap.jsp"/>
</head>
<body>
<header>
    <jsp:include page="parts/header.jsp"/>
</header>
<div class="container">
    <h1><fmt:message key="headline.exhibitionHalls"/></h1>
    <div class="row">
        <c:forEach var="exhibitionHall" items="${exhibitionHalls}">
            <div class="col-md-4">
                <form method="get" action="exhibition-hall">
                    <input type="number" hidden name="exhibition-hall-id" value="${exhibitionHall.id}"/>
                    <button type="submit" class="btn btn-primary btn-block">
                        <c:out value="${exhibitionHall.name}"/>
                    </button>
                </form>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
