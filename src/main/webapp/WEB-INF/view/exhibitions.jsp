<%--
  Created by IntelliJ IDEA.
  User: olesiiuk
  Date: 23.05.20
  Time: 20:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang}"/>
<fmt:setBundle basename="messages"/>
<html lang="${param.lang}">
<head>
    <title>Exhibition Calendar | Exhibitions</title>
    <jsp:include page="parts/head-bootstrap.jsp"/>
</head>
<body>
<header>
    <jsp:include page="parts/header.jsp"/>
</header>
<div class="container">
    <h1><fmt:message key="headline.exhibitions"/></h1>
    <jsp:include page="parts/cart-view-for-pages.jsp"/>
</div>

<div class="container">
    <div class="row row-flex">
        <c:forEach var="exhibition" items="${exhibitions}">
            <div class="col-md-4">
                <table class="table table-bordered table-hover">
                    <tr>
                        <td colspan="2"><h3>${exhibition.name}</h3></td>
                    </tr>
                    <tr>
                        <td><fmt:message key="table.exhibition.description"/></td>
                        <td>${exhibition.description}</td>
                    </tr>
                    <tr>
                        <td><fmt:message key="table.exhibition.beginDate"/></td>
                        <td>${exhibition.beginDate}</td>
                    </tr>
                    <tr>
                        <td><fmt:message key="table.exhibition.endDate"/></td>
                        <td>${exhibition.endDate}</td>
                    </tr>
                    <tr>
                        <td><fmt:message key="table.exhibition.fullTicketPrice"/></td>
                        <td>${exhibition.fullTicketPrice}</td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <div class="form-group">
                                <form method="get" action="/exhibition-hall">
                                    <input type="number" hidden name="exhibition-hall-id"
                                           value="${exhibition.exhibitionHallId}"/>
                                    <button type="submit" class="btn btn-link btn-block">
                                        <fmt:message key="ref.allExhibitionsInAHall"/>
                                    </button>
                                </form>
                            </div>
                        </td>
                    </tr>
                    <c:choose>
                        <c:when test="${sessionScope.userInSystem}">
                            <div class="form-group">
                                <form method="post" action="/exhibitions" role="form">
                                    <label><input type="number" hidden name="exhibition-id"
                                                  value="${exhibition.id}"/></label>
                                    <label><input type="text" hidden name="exhibition-name"
                                                  value="${exhibition.name}"/></label>
                                    <label><input type="number" hidden name="exhibition-hall-id"
                                                  value="${exhibition.exhibitionHallId}"/></label>
                                    <label><input type="number" hidden name="full-ticket-price"
                                                  value="${exhibition.fullTicketPrice}"/></label>
                                    <tr>
                                        <td><fmt:message key="table.exhibition.wantedVisitDate"/></td>
                                        <td>
                                            <label for="InputWantedDate" class="control-label"></label>
                                            <input type="date" name="visit-date" min="${exhibition.beginDate}"
                                                   max="${exhibition.endDate}"
                                                   class="form-control" id="InputWantedDate"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><fmt:message key="table.exhibition.wantedTicketsNumber"/></td>
                                        <td>
                                            <label for="InputTicketNumber" class="control-label"></label>
                                            <input type="number" name="wanted-tickets-quantity" min="1" max="6"
                                                   class="form-control" id="InputTicketNumber"
                                                   placeholder="<fmt:message key="placeholder.ticketsNumber"/>">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            <button type="submit" class="btn btn-success btn-block">
                                                <fmt:message key="ref.addToCart"/>
                                            </button>
                                        </td>
                                    </tr>
                                </form>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="2">
                                    <fmt:message key="table.exhibition.loginMessage1"/>
                                    <a href="/login"><fmt:message key="ref.login"/></a>
                                    <fmt:message key="table.exhibition.loginMessage2"/>
                                    <a href="/signup"><fmt:message key="ref.signup"/></a>
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </table>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
