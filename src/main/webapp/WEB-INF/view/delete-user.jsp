<%--
  Created by IntelliJ IDEA.
  User: olesiiuk
  Date: 23.05.20
  Time: 20:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang}"/>
<fmt:setBundle basename="messages"/>
<html lang="${param.lang}">
<head>
    <title>Exhibition Calendar | Delete Profile</title>
    <jsp:include page="parts/head-bootstrap.jsp"/>
</head>
<body>
<header>
    <jsp:include page="parts/header.jsp"/>
</header>
<div class="container">
    <div class="row">
        <div class="col-md-offset-3 col-md-6">
            <h1><fmt:message key="headline.deleteUser"/></h1>
            <c:if test="${wrongInput}">
                <p class="text-danger" role="alert">
                    <fmt:message key="message.wrongInput"/>
                </p>
            </c:if>

            <form method="post" action="/delete-user" role="form" class="form-horizontal">
                <div class="form-group">
                    <label for="InputEmail" class="control-label"><fmt:message key="form.email"/></label>
                    <input type="email" name="email" class="form-control" id="InputEmail"
                           placeholder="<fmt:message key="placeholder.email"/>">
                </div>
                <div class="form-group">
                    <label for="InputPassword1" class="control-label"><fmt:message key="form.password"/></label>
                    <input type="password" name="password1" class="form-control" id="InputPassword1"
                           placeholder="<fmt:message key="placeholder.password"/>">
                </div>
                <div class="form-group">
                    <label for="InputPassword2" class="control-label"><fmt:message key="form.password"/></label>
                    <input type="password" name="password2" class="form-control" id="InputPassword2"
                           placeholder="<fmt:message key="placeholder.password"/>">
                </div>
                <button type="submit" class="btn btn-danger"><fmt:message key="ref.delete"/></button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
