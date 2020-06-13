<%--
  Created by IntelliJ IDEA.
  User: olesiiuk
  Date: 23.05.20
  Time: 20:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang}"/>
<fmt:setBundle basename="messages"/>
<div class="container">
    <div class="row">
        <div class="col-md-10">
            <h1><fmt:message key="headline.greeting"/> <c:out value="${sessionScope.user.firstName}"/></h1>
        </div>
        <div class="col-md-2">
            <a href="/delete-user" class="btn btn-outline-warning" style="color: coral" role="button">
                <fmt:message key="ref.deleteAccount"/>
            </a>
        </div>
        <hr>
    </div>
    <h2><fmt:message key="headline.yourTickets"/></h2>
    <c:choose>
        <c:when test="${empty myTickets}">
            <p class="text-info">
                <fmt:message key="info.noTickets"/>
            </p>
        </c:when>
        <c:otherwise>
            <div class="table-responsive">
                <table class="table table-bordered table-hover">
                    <thead>
                    <tr class="active">
                        <th><fmt:message key="table.homepage.ticketNumber"/></th>
                        <th><fmt:message key="table.homepage.exhibitionName"/></th>
                        <th><fmt:message key="table.homepage.orderDate"/></th>
                        <th><fmt:message key="table.homepage.visitDate"/></th>
                        <th><fmt:message key="table.homepage.ticketType"/></th>
                        <th><fmt:message key="table.homepage.ticketPrice"/></th>
                        <th><fmt:message key="table.homepage.isTicketPaid"/></th>
                        <th><fmt:message key="table.homepage.deleteTicket"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="ticket" items="${myTickets}">
                        <tr>
                            <td>${ticket.id}</td>
                            <td>
                                <c:if test="${ticketExhibitions.containsKey(ticket.exhibitionId)}">
                                    <c:out value="${ticketExhibitions.get(ticket.exhibitionId).name}"/>
                                </c:if>
                            </td>
                            <td>${ticket.orderDate}</td>
                            <td>${ticket.visitDate}</td>
                            <td>${ticket.ticketType}</td>
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
                                    <c:choose>
                                        <c:when test="${sessionScope.user.role eq 'EMPLOYEE'}">
                                            <div class="form-group">
                                                <form method="post" action="/employee-home-page">
                                                    <input type="hidden" name="ticket-to-delete" value="${ticket.id}">
                                                    <button type="submit" class="btn btn-warning btn-block">
                                                        <fmt:message key="ref.delete"/>
                                                    </button>
                                                </form>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="form-group">
                                                <form method="post" action="/visitor-home-page">
                                                    <input type="hidden" name="ticket-to-delete" value="${ticket.id}">
                                                    <button type="submit" class="btn btn-warning btn-block">
                                                        <fmt:message key="ref.delete"/>
                                                    </button>
                                                </form>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</div>
