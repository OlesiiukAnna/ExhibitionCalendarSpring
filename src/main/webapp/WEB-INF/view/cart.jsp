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
    <title>Exhibition Calendar | Cart</title>
    <jsp:include page="parts/head-bootstrap.jsp"/>
</head>
<body>
<header>
    <jsp:include page="parts/header.jsp"/>
</header>

<div class="container">
    <h1><fmt:message key="headline.cart"/></h1>
    <c:if test="${ticketsOrdered}">
        <p class="text-success" role="alert">
            <fmt:message key="message.ticketsOrdered"/>
        </p>
    </c:if>
    <c:if test="${isInvalidData}">
        <p class="text-danger" role="alert">
            <fmt:message key="message.dataIsInvalid"/>
        </p>
    </c:if>
    <c:if test="${areTicketsRunOut}">
        <p class="text-danger" role="alert">
            <fmt:message key="message.ticketsRunOut"/>
        </p>
    </c:if>
    <c:if test="${isExhibitionNotFound}">
        <p class="text-danger" role="alert">
            <fmt:message key="message.exhibitionNotFound"/>
        </p>
    </c:if>
    <c:if test="${isUserInNotExists}">
        <p class="text-success" role="alert">
            <fmt:message key="message.userInNotExists"/>
        </p>
    </c:if>
    <c:choose>
        <c:when test="${empty sessionScope.dataForTicketsOrder}">
            <p class="text-info">
                <fmt:message key="info.noTickets"/>
            </p>
            <a href="${pageContext.request.contextPath}/exhibitions" class="btn btn-info" role="button">
                <fmt:message key="info.lookForExhibitions"/>
            </a>
        </c:when>
        <c:otherwise>
            <div class="jumbotron">
                <p>
                    <fmt:message key="info.ticketTypesRates"/>
                </p>
            </div>
            <div class="table-responsive">
                <form method="post" action="${pageContext.request.contextPath}/cart-save">
                    <table class="table table-bordered table-hover">
                        <thead>
                        <tr class="active">
                            <th><fmt:message key="table.cart.ticketCartId"/></th>
                            <th><fmt:message key="table.cart.exhibitionName"/></th>
                            <th><fmt:message key="table.cart.exhibitionHallId"/></th>
                            <th><fmt:message key="table.cart.wantedVisitDate"/></th>
                            <th><fmt:message key="table.cart.ticketPrice"/></th>

                            <th><fmt:message key="table.cart.ticketType"/></th>
                            <th><fmt:message key="table.cart.deleteTicketFromCart"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="wantedTicket" items="${sessionScope.dataForTicketsOrder}">
                            <tr>
                                <td>${wantedTicket.idInCart}</td>
                                <td>${wantedTicket.exhibitionName}</td>
                                <td>${wantedTicket.exhibitionHallId}</td>
                                <td>${wantedTicket.wantedVisitDate}</td>
                                <td>${wantedTicket.ticketPrice}</td>
                                <input type="number" hidden name="cart-ticket-id" value="${wantedTicket.idInCart}">
                                <td>
                                    <div class="form-group">
                                        <label for="InputTicketType" class="control-label"></label>
                                        <select id="InputTicketType" name="ticket-type">
                                            <c:forEach var="ticketType" items="${sessionScope.ticketTypes}">
                                                <option value="${ticketType.name()}">
                                                    <c:out value="${ticketType.name()}"/>
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/cart-delete-ticket?ticket-id-to-delete=${wantedTicket.idInCart}"
                                       class="btn btn-warning btn-block" role="button">
                                        <fmt:message key="ref.delete"/></a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <button type="submit" class="btn btn-primary"><fmt:message key="ref.confirm"/></button>
                </form>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
