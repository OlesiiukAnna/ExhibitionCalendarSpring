<%--
  Created by IntelliJ IDEA.
  User: olesiiuk
  Date: 23.05.20
  Time: 20:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang}"/>
<fmt:setBundle basename="messages"/>
<c:if test="${sessionScope.userInSystem}">
    <h3><fmt:message key="headline.cart"/>:</h3>
    <p><fmt:message key="message.ticketsQuantity"/>
        <c:out value="${sessionScope.dataForTicketsOrder.size()}"/>
    </p>
    <div class="row">
        <c:forEach var="desiredExhibition" items="${sessionScope.dataForTicketsOrder}">
            <div class="col-md-2">
                <p><c:out value="${desiredExhibition.exhibitionName}"/></p>
                <p><c:out value="${desiredExhibition.wantedVisitDate}"/></p>
                <p><fmt:message key="message.InfoFullPrice"/> <c:out value="${desiredExhibition.ticketPrice}"/></p>
            </div>
        </c:forEach>
    </div>
    <p>
        <a href="cart" class="btn btn-outline-success" role="button" style="color: seagreen">
            <fmt:message key="ref.goToCart"/>
        </a>
    </p>
</c:if>
