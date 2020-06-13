<%--
  Created by IntelliJ IDEA.
  User: olesiiuk
  Date: 23.05.20
  Time: 20:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang}"/>
<fmt:setBundle basename="messages"/>
<div class="navbar navbar-inverse navbar-static-top">
    <div class="container">
        <div class="navbar-header">
            <button class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">
                <b style="color: whitesmoke">
                    <fmt:message key="menu.logo"/>
                </b>
            </a>
        </div>
        <div class="navbar-collapse collapse">
            <c:choose>
                <c:when test="${sessionScope.userInSystem}">
                    <div class="navbar-form navbar-right">
                        <div class="form-group">
                            <form action="/cart">
                                <button type="submit" class="btn btn-success"><fmt:message key="ref.cart"/></button>
                            </form>
                        </div>
                        <c:choose>
                            <c:when test="${sessionScope.user.role eq 'EMPLOYEE'}">
                                <div class="form-group">
                                    <form method="get" action="/employee-home-page">
                                        <button type="submit" class="btn btn-primary">
                                            <fmt:message key="ref.homePage"/>
                                        </button>
                                    </form>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="form-group">
                                    <form method="get" action="/visitor-home-page">
                                        <button type="submit" class="btn btn-primary">
                                            <fmt:message key="ref.homePage"/>
                                        </button>
                                    </form>
                                </div>
                            </c:otherwise>
                        </c:choose>
                        <div class="form-group">
                            <form action="/logout">
                                <button type="submit" class="btn btn-info">
                                    <fmt:message key="ref.logout"/>
                                </button>
                            </form>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="navbar-form navbar-right">
                        <form action="/login" method="post">
                            <div class="form-group">
                                <input type="email" placeholder="<fmt:message key="placeholder.email"/>"
                                       class="form-control" name="email">
                            </div>
                            <div class="form-group">
                                <input type="password" placeholder="<fmt:message key="placeholder.password"/>"
                                       class="form-control" name="password">
                            </div>
                            <button type="submit" class="btn btn-success"><fmt:message key="ref.login"/></button>
                        </form>
                        <a href="/signup"><fmt:message key="ref.signup"/></a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
