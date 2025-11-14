<%--
  Created by IntelliJ IDEA.
  User: tunge
  Date: 10/9/2025
  Time: 11:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Lấy tham số 'activePage' được truyền vào từ trang cha --%>
<c:if test="${empty activePage}">
    <c:set var="activePage" value="${param.activePage}" />
</c:if>

<div class="card padding30 rounded-5">
    <div class="profile_avatar">
        <div class="profile_img">
            <img src="${pageContext.request.contextPath}${sessionScope.user.userProfile.profileImage != null ? sessionScope.user.userProfile.profileImage : '/images/profile/default.jpg'}"
                 alt="Profile Image"
                 class="rounded-circle"
                 width="120" height="120">
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
            <a href="${pageContext.request.contextPath}/owner/profile" class="${activePage == 'profile' ? 'active' : ''}">
                <i class="fa fa-user"></i> Thông tin của tôi
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/owner/ownerBooking"
               class="${activePage == 'ownerBooking' ? 'active' : ''}">
                <i class="fa fa-home"></i> Quản lí đơn thuê xe
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/owner/addCar" class="${activePage == 'addCar' ? 'active' : ''}">
                <i class="fa fa-car"></i> Thêm xe mới
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/owner/manageMyCar" class="${activePage == 'manageMyCar' ? 'active' : ''}">
                <i class="fa fa-cogs"></i> Gara xe của tôi
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/owner/rental-calendar" class="${activePage == 'rental-calendar' ? 'active' : ''}">
                <i class="fa fa-calendar"></i> Lịch thuê xe
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/owner/license" class="${activePage == 'license' ? 'active' : ''}">
                <i class="fa fa-id-card"></i> Xác minh bằng lái xe
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/logout">
                <i class="fa fa-sign-out"></i> Đăng xuất
            </a>
        </li>
    </ul>
</div>