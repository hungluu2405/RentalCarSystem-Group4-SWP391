<%--
  Created by IntelliJ IDEA.
  User: nguye
  Date: 10/16/2025
  Time: 10:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<ul class="navbar-nav ml-auto ml-md-0">

    <li class="nav-item dropdown no-arrow <%= "support".equals(request.getAttribute("activePage")) ? "active" : "" %>">
        <a class="nav-link position-relative" href="${pageContext.request.contextPath}/contactDB">
            <i class="fas fa-envelope fa-fw" style="font-size: 18px; color: white;"></i>

            <c:if test="${totalContacts > 0}">
                <span class="badge badge-danger badge-counter"
                      style="position: absolute; top: 2px; right: 3px; font-size: 0.5rem; padding: 3px 6px;">
                        ${totalContacts}
                </span>

            </c:if>
        </a>
    </li>


    <li class="nav-item dropdown no-arrow">
        <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-toggle="dropdown"
           aria-haspopup="true" aria-expanded="false">
            <i class="fas fa-user-circle fa-fw" style="font-size: 20px;"></i>
        </a>
        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="userDropdown">
            <a class="dropdown-item" href="${pageContext.request.contextPath}/promotionCreate">Tạo khuyến mãi nhanh</a>
            <a class="dropdown-item" href="${pageContext.request.contextPath}/bookingDB">Xem đơn đặt</a>
            <div class="dropdown-divider"></div>
            <a class="dropdown-item" href="#" data-toggle="modal" data-target="#logoutModal">Đăng xuất</a>
        </div>
    </li>
</ul>
