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
    <title>Exhibition Calendar | Register new exhibition</title>
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
            <h1><fmt:message key="headline.registerNewExhibition"/></h1>
            <c:if test="${exhibitionRegistered}">
                <p class="text-success" role="alert">
                    <fmt:message key="message.exhibitionRegisteredSuccessfully"/>
                </p>
            </c:if>
            <c:if test="${isDatesWrong}">
                <p class="text-danger" role="alert">
                    <fmt:message key="message.wrongDates"/>
                </p>
            </c:if>
            <c:if test="${isInvalidData}">
                <p class="text-danger" role="alert">
                    <fmt:message key="message.dataIsInvalid"/>
                </p>
            </c:if>

            <form method="post" action="/employee/register-new-exhibition" role="form" class="form-horizontal">
                <div class="form-group">
                    <label for="InputExhibitionName" class="control-label">
                        <fmt:message key="form.exhibition.name"/>
                    </label>
                    <input type="text" name="exhibition-name" class="form-control" id="InputExhibitionName"
                           placeholder="<fmt:message key="placeholder.exhibition.name"/>">
                </div>
                <div class="form-group">
                    <label for="description" class="control-label">
                        <fmt:message key="form.description"/>
                    </label>
                    <textarea name="exhibition-description" rows="5" cols="30" class="form-control"
                              id="description" placeholder="<fmt:message key="placeholder.description"/>"></textarea>
                </div>
                <div class="form-group">
                    <label for="InputBeginDate" class="control-label">
                        <fmt:message key="form.exhibition.beginDate"/>
                    </label>
                    <input type="date" name="exhibition-begin-date" class="form-control" id="InputBeginDate">
                </div>
                <div class="form-group">
                    <label for="InputEndDate" class="control-label">
                        <fmt:message key="form.exhibition.endDate"/>
                    </label>
                    <input type="date" name="exhibition-end-date" class="form-control" id="InputEndDate">
                </div>
                <div class="form-group">
                    <label for="InputFullTicketPrice" class="control-label">
                        <fmt:message key="form.exhibition.fullTicketPrice"/>
                    </label>
                    <input type="number" name="exhibition-full-ticket-price" min="0"
                           class="form-control" id="InputFullTicketPrice"
                           placeholder="<fmt:message key="placeholder.exhibition.fullTicketPrice"/>">
                </div>

                <div class="form-group">
                    <label for="ExhibitionHall" class="control-label">
                        <fmt:message key="form.exhibition.chooseAHall"/>
                    </label>
                    <select id="ExhibitionHall" name="exhibition-hall-id">
                        <c:forEach var="exhibitionHall" items="${exhibitionHalls}">
                            <option value="${exhibitionHall.id}">
                                <c:out value="${exhibitionHall.name}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <button type="submit" class="btn btn-primary">
                    <fmt:message key="ref.registerNewExhibition"/>
                </button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
