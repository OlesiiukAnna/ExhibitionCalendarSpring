<%--
  Created by IntelliJ IDEA.
  User: olesiiuk
  Date: 23.05.20
  Time: 20:18
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang}"/>
<fmt:setBundle basename="messages"/>
<div class="container">
    <div class="btn-group btn-block">
        <div class="row">
            <div class="col-md-3">
                <a href="/employee/tickets" class="btn btn-link btn-block" role="button">
                    <fmt:message key="ref.allTickets"/>
                </a>
            </div>
            <div class="col-md-3">
                <a href="/employee/register-new-exhibition" class="btn btn-link btn-block" role="button">
                    <fmt:message key="ref.registerNewExhibition"/>
                </a>
            </div>
            <div class="col-md-3">
                <a href="/employee/register-new-exhibition-hall" class="btn btn-link btn-block" role="button">
                    <fmt:message key="ref.registerNewExhibitionHall"/>
                </a>
            </div>
            <div class="col-md-3">
                <a href="/employee/register-new-employee" class="btn btn-link btn-block" role="button">
                    <fmt:message key="ref.registerNewEmployee"/>
                </a>
            </div>
            <hr>
        </div>
    </div>
</div>
<hr>
