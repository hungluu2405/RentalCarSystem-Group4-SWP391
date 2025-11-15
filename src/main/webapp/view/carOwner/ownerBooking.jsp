<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../common/carOwner/_headOwner.jsp"/>
    <title>Car Owner Dashboard</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI',
            'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', sans-serif;
            -webkit-font-smoothing: antialiased;
            -moz-osx-font-smoothing: grayscale;
        }

        h1, h2, h3, h4, h5, h6 {
            font-family: 'Inter', sans-serif;
            font-weight: 700;
        }

        p, span, div {
            font-family: 'Inter', sans-serif;
        }
        .tab-container {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
        }

        .tab-btn {
            padding: 10px 20px;
            border: none;
            background-color: #f0f0f0;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
            transition: 0.2s;
        }

        .tab-btn.active {
            background-color: #28a745;
            color: #fff;
        }

        .tab-content {
            display: none;
        }

        .tab-content.active {
            display: block;
        }
        .pagination {
            display: flex;
            justify-content: center;
            padding: 20px 0;
            list-style: none;
        }

        .pagination li {
            margin: 0 5px;
        }

        .pagination li a {
            display: block;
            padding: 8px 14px;
            border: 1px solid #ddd;
            border-radius: 6px;
            text-decoration: none;
            font-size: 14px;
            transition: 0.2s;
        }

        .pagination li a:hover {
            background-color: #f0f0f0;
        }

        .pagination li.active a {
            background-color: #28a745;
            color: #fff;
            border-color: #28a745;
        }
        .pagination-container {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
        }



    </style>
</head>

<body>
<div id="wrapper">
    <div id="de-preloader"></div>
    <jsp:include page="../common/carOwner/_headerOwner.jsp"/>

    <div class="no-bottom no-top zebra" id="content">
        <div id="top"></div>
        <section id="subheader" class="jarallax text-light">
            <img src="${pageContext.request.contextPath}/images/background/14.jpg" class="jarallax-img" alt="">
            <div class="center-y relative text-center">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center"><h1>Trang quản lý đơn thuê xe</h1></div>
                    </div>
                </div>
            </div>
        </section>

        <section id="section-cars" class="bg-gray-100">
            <div class="container">
                <div class="row">
                    <!-- SIDEBAR -->
                    <div class="col-lg-3 mb30">
                        <jsp:include page="../common/carOwner/_sidebarOwner.jsp">
                            <jsp:param name="activePage" value="ownerBooking"/>
                        </jsp:include>
                    </div>

                    <!-- MAIN CONTENT -->
                    <div class="col-lg-9">
                        <!-- STATISTICS -->
                        <div class="row">
                            <div class="col-lg-3 col-6 mb25">
                                <div class="card padding30 rounded-5 text-center">
                                    <div class="symbol mb40">
                                        <i class="fa id-color fa-2x fa-car"></i>
                                    </div>
                                    <span class="h1 mb0">${totalCars}</span><br>
                                    <span class="text-gray">Tổng số xe</span>
                                </div>
                            </div>
                            <div class="col-lg-3 col-6 mb25">
                                <div class="card padding30 rounded-5 text-center">
                                    <div class="symbol mb40">
                                        <i class="fa id-color fa-2x fa-calendar"></i>
                                    </div>
                                    <span class="h1 mb0">${totalBookings}</span><br>
                                    <span class="text-gray">Tổng số chuyến</span>
                                </div>
                            </div>
                            <div class="col-lg-3 col-6 mb25">
                                <div class="card padding30 rounded-5 text-center">
                                    <div class="symbol mb40">
                                        <i class="fa id-color fa-2x fa-toggle-on"></i>
                                    </div>
                                    <span class="h1 mb0">${activeBookings}</span><br>
                                    <span class="text-gray">Tổng số chuyến đã chấp nhận</span>
                                </div>
                            </div>
                            <div class="col-lg-3 col-6 mb25">
                                <div class="card padding30 rounded-5 text-center">
                                    <div class="symbol mb40">
                                        <i class="fa id-color fa-2x fa-ban"></i>
                                    </div>
                                    <span class="h1 mb0">${cancelledBookings}</span><br>
                                    <span class="text-gray">Tổng số chuyến đã từ chối</span>
                                </div>
                            </div>
                        </div>

                        <!-- Tabs -->
                        <div class="tab-container">
                                <button class="tab-btn ${tab == 'pending' ? 'active' : ''}" id="tabPending">Chuyến chờ xử lí</button>
                                <button class="tab-btn ${tab == 'active' ? 'active' : ''}" id="tabActive">Chuyến được chấp nhận</button>
                                <button class="tab-btn ${tab == 'history' ? 'active' : ''}" id="tabHistory">Lịch sử chuyến</button>

                        </div>


                        <!-- === TAB 1: Pending Orders === -->
                        <div id="pendingOrders" class="tab-content ${tab == 'pending' ? 'active' : ''}">

                        <div class="table-responsive">
                                <table class="table align-middle text-center">
                                    <thead class="table-light">
                                    <tr>
                                        <th>Tên xe</th>
                                        <th>Khách hàng</th>
                                        <th>Số điện thoại</th>
                                        <th>Thời gian lấy xe</th>
                                        <th>Thời gian trả xe</th>
                                        <th>Ngày lấy xe</th>
                                        <th>Ngày trả xe</th>
                                        <th>Giá tiền</th>
                                        <th>Trạng thái</th>
                                        <th>Thao tác</th>
                                    </tr>
                                    </thead>
                                    <tbody>

                                    <c:if test="${empty bookings}">
                                        <tr>
                                            <td colspan="10" class="text-center py-4 text-muted">
                                                Bạn hiện không có đơn đặt thuê xe nào.
                                            </td>
                                        </tr>
                                    </c:if>

                                    <c:forEach var="order" items="${bookings}">
                                        <c:set var="isPending" value="${order.status == 'Pending'}"/>
                                        <c:set var="isHistory"
                                               value="${order.status == 'Paid' || order.status == 'Approved'}"/>
                                        <c:set var="isHistory"
                                               value="${order.status == 'Completed' || order.status == 'Rejected' || order.status == 'Cancelled'}"/>

                                        <c:if test="${isPending}">
                                            <tr>
                                                <td>${order.carName}</td>
                                                <td>${order.customerProfile.fullName}</td>
                                                <td>${order.customerProfile.phone}</td>
                                                <td>${order.pickupTime}</td>
                                                <td>${order.dropoffTime}</td>
                                                <td>${order.startDate}</td>
                                                <td>${order.endDate}</td>
                                                <td style="white-space: nowrap;"><fmt:formatNumber value="${order.totalPrice}"
                                                                                                   type="number" groupingUsed="true"
                                                                                                   minFractionDigits="0" maxFractionDigits="0"/> ₫
                                                </td>
                                                <td>
                                                    <span class="badge bg-warning text-dark">${order.status}</span>
                                                </td>
                                                <td>
                                                    <form method="post"
                                                          action="${pageContext.request.contextPath}/owner/ownerBooking"
                                                          class="d-flex justify-content-center gap-2">
                                                        <input type="hidden" name="bookingId"
                                                               value="${order.bookingId}">
                                                        <button type="submit" name="action" value="accept"
                                                                class="btn btn-success btn-sm">Chấp nhận
                                                        </button>
                                                        <button type="submit" name="action" value="reject"
                                                                class="btn btn-danger btn-sm">Từ chối
                                                        </button>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </c:forEach>
                                    </tbody>
                                </table>


                            </div>
                        </div>

                        <!-- === TAB 2: Order Active === -->
                        <div id="activeOrders" class="tab-content ${tab == 'active' ? 'active' : ''}">

                        <div class="table-responsive">
                                <table class="table align-middle text-center">
                                    <thead class="table-light">
                                    <tr>
                                        <th>Tên xe</th>
                                        <th>Khách hàng</th>
                                        <th>Số điện thoại</th>
                                        <th>Thời gian lấy xe</th>
                                        <th>Thời gian trả xe</th>
                                        <th>Ngày lấy xe</th>
                                        <th>Ngày trả xe</th>
                                        <th>Giá tiền</th>
                                        <th>Trạng thái</th>
                                        <th>Thao tác</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="order" items="${bookings}">
                                            <tr>
                                                <td>${order.carName}</td>
                                                <td>${order.customerProfile.fullName}</td>
                                                <td>${order.customerProfile.phone}</td>
                                                <td>${order.pickupTime}</td>
                                                <td>${order.dropoffTime}</td>
                                                <td>${order.startDate}</td>
                                                <td>${order.endDate}</td>
                                                <td style="white-space: nowrap;"><fmt:formatNumber value="${order.totalPrice}"
                                                                                                   type="number" groupingUsed="true"
                                                                                                   minFractionDigits="0" maxFractionDigits="0"/> ₫
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${order.status == 'Approved'}">
                                                            <span class="badge bg-primary">Được chấp nhận</span>
                                                        </c:when>
                                                        <c:when test="${order.status == 'Completed'}">
                                                            <span class="badge bg-success">Đã hoàn thành</span>
                                                        </c:when>
                                                        <c:when test="${order.status == 'Rejected'}">
                                                            <span class="badge bg-danger">Đã từ chối</span>
                                                        </c:when>
                                                        <c:when test="${order.status == 'Paid'}">
                                                            <span class="badge bg-info text-dark">Đã thanh toán</span>
                                                        </c:when>
                                                        <c:when test="${order.status == 'Returning'}">
                                                            <span class="badge bg-warning text-dark">Đang trả xe</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary">${order.status}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:if test="${order.status == 'Returning'}">
                                                        <form method="post"
                                                              action="${pageContext.request.contextPath}/owner/ownerBooking"
                                                              class="d-inline">
                                                            <input type="hidden" name="bookingId" value="${order.bookingId}">
                                                            <div class="d-flex gap-2">
                                                                <button type="submit" name="action" value="confirmReturn"
                                                                        onclick="return confirm('Bạn chắc chắn đã nhận được xe?');"
                                                                        class="btn btn-success btn-sm d-inline-flex align-items-center">
                                                                <i class="fa fa-check me-1"></i> Chấp nhận trả xe
                                                            </button>
                                                            </div>
                                                        </form>
                                                    </c:if>
                                                </td>
                                            </tr>
                                    </c:forEach>

                                    </tbody>
                                </table>


                            </div>
                        </div>

                        <!-- === TAB 3: Order History === -->
                        <div id="historyOrders" class="tab-content ${tab == 'history' ? 'active' : ''}">

                        <div class="table-responsive">
                                <table class="table align-middle text-center">
                                    <thead class="table-light">
                                    <tr>
                                        <th>Tên xe</th>
                                        <th>Khách hàng</th>
                                        <th>Số điện thoại</th>
                                        <th>Thời gian lấy xe</th>
                                        <th>Thời gian trả xe</th>
                                        <th>Ngày lấy xe</th>
                                        <th>Ngày trả xe</th>
                                        <th>Giá tiền</th>
                                        <th>Trạng thái</th>
<%--                                        <th>Thao tác</th>--%>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="order" items="${bookings}">
                                            <tr>
                                                <td>${order.carName}</td>
                                                <td>${order.customerProfile.fullName}</td>
                                                <td>${order.customerProfile.phone}</td>
                                                <td>${order.pickupTime}</td>
                                                <td>${order.dropoffTime}</td>
                                                <td>${order.startDate}</td>
                                                <td>${order.endDate}</td>
                                                <td style="white-space: nowrap;"><fmt:formatNumber value="${order.totalPrice}"
                                                                                                   type="number" groupingUsed="true"
                                                                                                   minFractionDigits="0" maxFractionDigits="0"/> ₫
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${order.status == 'Approved'}">
                                                            <span class="badge bg-primary">Đã chấp nhận</span>
                                                        </c:when>
                                                        <c:when test="${order.status == 'Completed'}">
                                                            <span class="badge bg-success">Đã hoàn thành</span>
                                                        </c:when>
                                                        <c:when test="${order.status == 'Rejected'}"> Cancelled
                                                            <span class="badge bg-danger">Đã từ chối </span>
                                                        </c:when>
                                                        <c:when test="${order.status == 'Cancelled'}">
                                                            <span class="badge bg-secondary">Khách hàng từ chối </span>
                                                        </c:when>
                                                        <c:when test="${order.status == 'Paid'}">
                                                            <span class="badge bg-info text-dark">Đã thanh toán</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary">${order.status}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                    </c:forEach>

                                    </tbody>
                                </table>

                            </div>
                        </div>

                        <c:if test="${totalPages > 1}">
                            <div class="pagination-container">
                                <ul class="pagination">
                                    <!-- Previous Button -->
                                    <li class="${currentPage == 1 ? 'disabled' : ''}">
                                        <a href="${pageContext.request.contextPath}/owner/ownerBooking?tab=${tab}&page=${currentPage - 1}">
                                            <i class="fa fa-chevron-left"></i>
                                        </a>
                                    </li>

                                    <!-- Page Numbers -->
                                    <c:forEach begin="1" end="${totalPages}" var="i">
                                        <c:choose>
                                            <c:when test="${currentPage == i}">
                                                <li class="active"><a href="#">${i}</a></li>
                                            </c:when>
                                            <c:otherwise>
                                                <li>
                                                    <a href="${pageContext.request.contextPath}/owner/ownerBooking?tab=${tab}&page=${i}">
                                                            ${i}
                                                    </a>
                                                </li>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>

                                    <!-- Next Button -->
                                    <li class="${currentPage == totalPages ? 'disabled' : ''}">
                                        <a href="${pageContext.request.contextPath}/owner/ownerBooking?tab=${tab}&page=${currentPage + 1}">
                                            <i class="fa fa-chevron-right"></i>
                                        </a>
                                    </li>
                                </ul>

                                <div class="pagination-info">
                                    Page ${currentPage} of ${totalPages}
                                </div>
                            </div>
                        </c:if>
                    </div>

                </div>
            </div>
        </section>

        <!-- FOOTER -->
        <jsp:include page="../common/carOwner/_footer_scriptsOwner.jsp"/>

    </div>
</body>
</html>

        <!-- SCRIPT: Tab Switching -->
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const tabs = document.querySelectorAll(".tab-btn");
                const contents = document.querySelectorAll(".tab-content");

                tabs.forEach((tab) => {
                    tab.addEventListener("click", function () {
                        const targetId = this.id.replace("tab", "").toLowerCase(); // -> pending, active, history

                        // đổi class active của nút
                        tabs.forEach((t) => t.classList.remove("active"));
                        this.classList.add("active");

                        // đổi class active của nội dung
                        contents.forEach((c) => c.classList.remove("active"));
                        document.getElementById(targetId + "Orders").classList.add("active");

                        // cập nhật URL để gọi lại servlet
                        const url = new URL(window.location.href);
                        url.searchParams.set("tab", targetId);
                        url.searchParams.set("page", 1); // reset về trang đầu
                        window.location.href = url.toString(); // tải lại trang với tab mới
                    });
                });
            });
        </script>


    </div>
</body>
</html>