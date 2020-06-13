<%--
  Created by IntelliJ IDEA.
  User: olesiiuk
  Date: 23.05.20
  Time: 20:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang}"/>
<fmt:setBundle basename="messages"/>
<html lang="${param.lang}">
<head>
    <title>Exhibition Calendar | New employee registration</title>
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
            <h1><fmt:message key="headline.registerNewEmployee"/></h1>

            <c:if test="${isEmployeeExists}">
                <p class="text-danger" role="alert">
                    <fmt:message key="message.userIsExists"/>
                </p>
            </c:if>

            <c:if test="${isInvalidData}">
                <p class="text-danger" role="alert">
                    <fmt:message key="message.dataIsInvalid"/>
                </p>
            </c:if>

            <c:if test="${employeeRegistered}">
                <p class="text-success" role="alert">
                    <fmt:message key="message.employeeWasRegisteredSuccessfully"/>
                </p>
            </c:if>

            <p class="text-info"><fmt:message key="info.registerNewEmployee"/></p>

            <form method="post" action="/employee/register-new-employee" role="form" class="form-horizontal">
                <div class="form-group">
                    <label for="InputEmail" class="control-label"><fmt:message key="form.email"/></label>
                    <input type="email" name="employee-email" class="form-control" id="InputEmail"
                           placeholder="<fmt:message key="placeholder.email"/>">
                </div>
                <div class="form-group">
                    <label for="InputPassword" class="control-label"><fmt:message key="form.password"/></label>
                    <input type="password" name="employee-password" class="form-control" id="InputPassword"
                           placeholder="<fmt:message key="placeholder.password"/>">
                </div>
                <div class="form-group">
                    <label for="InputFirstName" class="control-label"><fmt:message key="form.firstName"/></label>
                    <input type="text" name="employee-first-name" class="form-control" id="InputFirstName"
                           placeholder="<fmt:message key="placeholder.firstName"/>">
                </div>
                <div class="form-group">
                    <label for="InputLastName" class="control-label"><fmt:message key="form.lastName"/></label>
                    <input type="text" name="employee-last-name" class="form-control" id="InputLastName"
                           placeholder="<fmt:message key="placeholder.lastName"/>">
                </div>
                <div class="form-group">
                    <label for="InputPhone" class="control-label"><fmt:message key="form.phone"/></label>
                    <input type="text" name="employee-phone" class="form-control" id="InputPhone"
                           placeholder="<fmt:message key="placeholder.phone"/>">
                </div>
                <input type="hidden" name="employee-role" value="EMPLOYEE">
                <button type="submit" class="btn btn-primary"><fmt:message key="ref.submit"/></button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
