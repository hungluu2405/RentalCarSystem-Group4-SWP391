<%--
  Created by IntelliJ IDEA.
  User: nguye
  Date: 10/16/2025
  Time: 10:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<ul class="sidebar navbar-nav">

    <li class="nav-item <%= "account".equals(request.getAttribute("activePage")) ? "active" : "" %>">
        <a class="nav-link" href="accountDB">
            <i class="fas fa-id-card"></i>
            <span>Tài khoản người dùng</span></a>
    </li>

    <li class="nav-item <%= "car".equals(request.getAttribute("activePage")) ? "active" : "" %>">
        <a class="nav-link" href="CarDB">
            <i class="fas fa-car"></i>
            <span>Danh sách xe</span></a>
    </li>
    <li class="nav-item <%= "booking".equals(request.getAttribute("activePage")) ? "active" : "" %>">
        <a class="nav-link" href="bookingDB">
            <i class="fas fa-fw fa-table"></i>
            <span>Lịch sử đặt hàng</span></a>
    </li>
    <li class="nav-item <%= "report".equals(request.getAttribute("activePage")) ? "active" : "" %>">
        <a class="nav-link" href="reportDB">
            <i class="fas fa-chart-bar"></i>
            <span>Đơn đã thanh toán</span></a>
    </li>

    <li class="nav-item <%= "support".equals(request.getAttribute("activePage")) ? "active" : "" %>">
        <a class="nav-link" href="contactDB">
            <i class="fas fa-envelope fa-fw"></i>
            <span>Liên hệ</span></a>
    </li>

    <li class="nav-item <%= "promotion".equals(request.getAttribute("activePage")) ? "active" : "" %>">
        <a class="nav-link" href="promotionDB">
            <i class="fas fa-pound-sign"></i>
            <span>Khuyến mãi</span></a>
    </li>
</ul>
