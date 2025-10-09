<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Lấy tham số 'activePage' được truyền vào từ trang cha --%>
<c:set var="activePage" value="${param.activePage}" />

<div class="card padding30 rounded-5">
    <div class="profile_avatar">
        <div class="profile_img">
            <img src="${pageContext.request.contextPath}/images/profile/1.jpg" alt="">
        </div>
        <div class="profile_name">
            <h4>
                ${sessionScope.user.userProfile.fullName}
                <span class="profile_username text-gray">${sessionScope.user.email}</span>
            </h4>
        </div>
    </div>
    <div class="spacer-20"></div>
    <ul class="menu-col">
        <li>
            <a href="${pageContext.request.contextPath}/customer/customerDashboard" class="${activePage == 'dashboard' ? 'active' : ''}">
                <i class="fa fa-home"></i>Dashboard
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/customer/profile" class="${activePage == 'profile' ? 'active' : ''}">
                <i class="fa fa-user"></i>My Profile
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/customer/orders" class="${activePage == 'orders' ? 'active' : ''}">
                <i class="fa fa-calendar"></i>My Orders
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/logout">
                <i class="fa fa-sign-out"></i>Sign Out
            </a>
        </li>
    </ul>
</div>