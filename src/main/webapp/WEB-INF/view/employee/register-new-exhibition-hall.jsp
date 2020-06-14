<%--
  Created by IntelliJ IDEA.
  User: olesiiuk
  Date: 23.05.20
  Time: 20:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang}"/>
<fmt:setBundle basename="messages"/>
<html lang="${param.lang}">
<head>
    <title>Exhibition Calendar | Register new exhibition hall</title>
    <jsp:include page="../parts/head-bootstrap.jsp"/>
</head>
<body>
<header>
    <jsp:include page="../parts/header.jsp"/>
    <jsp:include page="../parts/employee-navbar.jsp"/>
</header>
<div class="container">
    <div class="row">
        <div class="col-md-offset-3 col-md-6">
            <h1><fmt:message key="headline.registerNewExhibitionHall"/></h1>

            <c:if test="${hallRegistered}">
                <p class="text-success" role="alert">
                    <fmt:message key="message.exhibitionHallWasRegisteredSuccessfully"/>
                </p>
            </c:if>
            <c:if test="${isInvalidData}">
                <p class="text-danger" role="alert">
                    <fmt:message key="message.dataIsInvalid"/>
                </p>
            </c:if>
            <c:if test="${hallNotRegistered}">
                <p class="text-danger" role="alert">
                    <fmt:message key="message.exhibitionHallIsExists"/>
                </p>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/employee/register-new-exhibition-hall" role="form" class="form-horizontal">
                <div class="form-group">
                    <label for="InputExhibitionHallName" class="control-label">
                        <fmt:message key="form.exhibitionHall.name"/>
                    </label>
                    <input type="text" name="hall-name" class="form-control" id="InputExhibitionHallName"
                           placeholder="<fmt:message key="placeholder.exhibitionHall.name"/>">
                </div>
                <div class="form-group">
                    <label for="description" class="control-label">
                        <fmt:message key="form.exhibitionHall.capacity"/>
                    </label>
                    <input type="number" name="hall-capacity" min="0" class="form-control" id="description"
                           placeholder="<fmt:message key="placeholder.exhibitionHall.capacity"/>">
                </div>
                <button type="submit" class="btn btn-primary">
                    <fmt:message key="ref.registerNewExhibitionHall"/>
                </button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
