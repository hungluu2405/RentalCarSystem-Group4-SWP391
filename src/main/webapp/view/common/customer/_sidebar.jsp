<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="activePage" value="${activePage != null ? activePage : param.activePage}" />


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
                <i class="fa fa-user"></i> Thông Tin Của Tôi
            </a>
        </li>


        <li>
            <a href="${pageContext.request.contextPath}/customer/customerOrder"
               class="${activePage == 'orders' ? 'active' : ''}">
                <i class="fa fa-home"></i> Chuyến Của Tôi
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/customer/myCoupon"
               class="${activePage == 'myCoupon' ? 'active' : ''}">
                <i class="fa fa-calendar"></i> Quà Tặng
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/customer/favourite/list"
               class="${activePage == 'favourites' ? 'active' : ''}">
                <i class="fa fa-star"></i> Xe Yêu Thích
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/customer/license"
               class="${activePage == 'license' ? 'active' : ''}">
                <i class="fa fa-id-card"></i> Xác Minh Bằng Lái Xe
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/logout">
                <i class="fa fa-sign-out"></i> Đăng Xuất
            </a>
        </li>
    </ul>
</div>
