<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="activePage" value="${param.activePage}" />

<div class="card padding30 rounded-5 text-center">
    <div class="profile_avatar mb-3">
        <div class="profile_img mb-2">
            <img src="${pageContext.request.contextPath}${sessionScope.user.userProfile.profileImage != null ? sessionScope.user.userProfile.profileImage : '/images/profile/default.jpg'}"
                 alt="Profile Image"
                 class="rounded-circle"
                 width="120" height="120">
        </div>
        <div class="profile_name">
            <h4 class="mb-0">${sessionScope.user.userProfile.fullName != null ? sessionScope.user.userProfile.fullName : 'Unknown User'}</h4>
            <span class="profile_username text-gray">${sessionScope.user.email}</span>
        </div>
    </div>

    <hr>

    <ul class="menu-col list-unstyled text-start">
        <li>
            <a href="${pageContext.request.contextPath}/customer/profile"
               class="${activePage == 'profile' ? 'active' : ''}">
                <i class="fa fa-user"></i> My Profile
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/customer/customerOrder"
               class="${activePage == 'orders' ? 'active' : ''}">
                <i class="fa fa-home"></i> My Orders
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/customer/myCoupon"
               class="${activePage == 'myCoupon' ? 'active' : ''}">
                <i class="fa fa-calendar"></i> My Coupon
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/logout">
                <i class="fa fa-sign-out"></i> Sign Out
            </a>
        </li>
    </ul>
</div>
