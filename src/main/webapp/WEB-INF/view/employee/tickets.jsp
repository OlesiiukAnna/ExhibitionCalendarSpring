<%--
  Created by IntelliJ IDEA.
  User: olesiiuk
  Date: 23.05.20
  Time: 20:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang}"/>
<fmt:setBundle basename="messages"/>
<html lang="${param.lang}">
<head>
    <title>Exhibition Calendar | Tickets</title>
    <jsp:include page="../parts/head-bootstrap.jsp"/>
</head>
<body>
<header>
    <jsp:include page="../parts/header.jsp"/>
    <jsp:include page="../parts/employee-navbar.jsp"/>
</header>
<div class="container">
    <h1><fmt:message key="headline.allTickets"/></h1>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <form method="post" action="tickets">
                <thead>
                <tr class="active">
                    <th><fmt:message key="table.allTickets.ticketNumber"/></th>
                    <th><fmt:message key="table.allTickets.orderDate"/></th>
                    <th><fmt:message key="table.allTickets.visitDate"/></th>
                    <th><fmt:message key="table.allTickets.ticketType"/></th>
                    <th><fmt:message key="table.allTickets.visitorEmail"/></th>
                    <th><fmt:message key="table.allTickets.exhibitionId"/></th>
                    <th><fmt:message key="table.allTickets.exhibitionName"/></th>
                    <th><fmt:message key="table.allTickets.ticketPrice"/></th>
                    <th><fmt:message key="table.allTickets.isTicketPaid"/></th>
                    <th>
                        <button type="submit" class="btn btn-primary btn-block">
                            <fmt:message key="ref.confirm"/>
                        </button>
                    </th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="ticket" items="${tickets}">
                    <tr>
                        <td>${ticket.id}</td>
                        <td>${ticket.orderDate}</td>
                        <td>${ticket.visitDate}</td>
                        <td>${ticket.ticketType}</td>
                        <td>
                            <c:if test="${ticketOwners.containsKey(ticket.userId)}">
                                <c:out value="${ticketOwners.get(ticket.userId).email}"/>
                            </c:if>
                        </td>
                        <td> ${ticket.exhibitionId}</td>
                        <td>
                            <c:if test="${ticketExhibitions.containsKey(ticket.exhibitionId)}">
                                <c:out value="${ticketExhibitions.get(ticket.exhibitionId).name}"/>
                            </c:if>
                        </td>
                        <td>${ticket.ticketPrice}</td>
                        <c:choose>
                            <c:when test="${ticket.paid}">
                                <td class="success">${ticket.paid}</td>
                            </c:when>
                            <c:otherwise>
                                <td class="warning">${ticket.paid}</td>
                            </c:otherwise>
                        </c:choose>
                        <td>
                            <c:if test="${ticket.paid == false}">

                                <label class="btn btn-info btn-block">
                                    <input type="checkbox" value="${ticket.id}"
                                           id="ticket" name="ticketIdsToConfirm">
                                    <fmt:message key="ref.setPaid"/>
                                </label>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </form>
        </table>
    </div>
</div>
</body>
</html>
