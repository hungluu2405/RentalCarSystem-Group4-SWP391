<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../common/customer/_head.jsp"/>
    <style>
        /* CSS Cũ Giữ Nguyên */
        .tab-container {
            display: flex;
            gap: 30px;
            border-bottom: 2px solid #eaeaea;
            margin-top: 10px;
            margin-bottom: 20px;
        }

        .tab-btn {
            background: none;
            border: none;
            font-weight: 600;
            font-size: 16px;
            padding: 8px 0;
            cursor: pointer;
            color: #222;
            transition: all 0.3s ease;
            position: relative;
        }

        .tab-btn.active {
            color: #00b074;
        }

        .tab-btn.active::after {
            content: "";
            position: absolute;
            bottom: -2px;
            left: 0;
            height: 3px;
            width: 100%;
            background-color: #00b074;
            border-radius: 5px;
        }

        .tab-content {
            display: none;
        }

        .tab-content.active {
            display: block;
        }

        .badge {
            padding: 5px 10px;
            border-radius: 10px;
            font-size: 12px;
        }

        /* Màu sắc trạng thái */
        .bg-warning {
            background-color: #FFD54F;
            color: #222;
        }

        .bg-info-dark {
            background-color: #5bc0de;
            color: white;
        }

        .bg-primary-dark {
            background-color: #007bff;
            color: white;
        }

        .bg-returning {
            background-color: #ff9800;
            color: white;
        }

        .bg-success {
            background-color: #66BB6A;
            color: white;
        }

        .bg-danger {
            background-color: #dc3545;
            color: white;
        }

        /* ========== PAGINATION CSS ========== */
        .pagination-container {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-top: 25px;
            gap: 15px;
        }

        .pagination {
            display: flex;
            list-style: none;
            padding: 0;
            margin: 0;
            gap: 5px;
        }

        .pagination li {
            display: inline-block;
        }

        .pagination a {
            padding: 8px 14px;
            border: 1px solid #ddd;
            background-color: white;
            color: #333;
            text-decoration: none;
            border-radius: 5px;
            transition: all 0.3s;
            font-weight: 500;
        }

        .pagination a:hover {
            background-color: #00b074;
            color: white;
            border-color: #00b074;
        }

        .pagination .active a {
            background-color: #00b074;
            color: white;
            border-color: #00b074;
            cursor: default;
        }

        .pagination .disabled a {
            color: #ccc;
            cursor: not-allowed;
            pointer-events: none;
        }

        .pagination-info {
            color: #666;
            font-size: 14px;
        }

        /* ========== CSS CHO MODAL ========== */
        .owner-modal {
            display: none;
            position: fixed;
            z-index: 9999;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            animation: fadeIn 0.3s;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
            }
            to {
                opacity: 1;
            }
        }

        .owner-modal-content {
            background-color: white;
            margin: 10% auto;
            padding: 0;
            border-radius: 12px;
            width: 90%;
            max-width: 450px;
            box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);
            animation: slideDown 0.3s;
        }

        @keyframes slideDown {
            from {
                transform: translateY(-50px);
                opacity: 0;
            }
            to {
                transform: translateY(0);
                opacity: 1;
            }
        }

        .owner-modal-header {
            padding: 20px 25px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 12px 12px 0 0;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .owner-modal-header h3 {
            margin: 0;
            font-size: 20px;
        }

        .owner-modal-close {
            font-size: 28px;
            cursor: pointer;
            color: white;
            transition: transform 0.2s;
        }

        .owner-modal-close:hover {
            transform: scale(1.2);
        }

        .owner-modal-body {
            padding: 30px;
        }

        .owner-info-item {
            margin-bottom: 20px;
            padding-bottom: 15px;
            border-bottom: 1px solid #eee;
        }

        .owner-info-item:last-child {
            border-bottom: none;
        }

        .owner-info-label {
            font-size: 12px;
            color: #888;
            text-transform: uppercase;
            font-weight: 600;
            letter-spacing: 0.5px;
            margin-bottom: 5px;
        }

        .owner-info-value {
            font-size: 16px;
            color: #333;
            font-weight: 600;
        }

        .owner-avatar {
            width: 100px;
            height: 100px;
            border-radius: 50%;
            object-fit: cover;
            border: 4px solid #f0f0f0;
            margin: 0 auto 20px;
            display: block;
        }

        .car-name-link {
            color: #0d6efd;
            cursor: pointer;
            text-decoration: none;
            font-weight: 700;
            display: inline-flex;
            align-items: center;
            gap: 5px;
        }

        .car-name-link:hover {
            text-decoration: underline;
            color: #0a58ca;
        }

        .loading-spinner {
            text-align: center;
            padding: 40px;
        }

        .loading-spinner i {
            font-size: 32px;
            color: #667eea;
        }

        /* ========== RATING STARS CSS ========== */
        .star {
            font-size: 30px;
            color: #ccc;
            cursor: pointer;
            transition: color 0.2s;
        }

        .star.selected,
        .star:hover {
            color: #FFD700;
        }

        /* Rate link styling */
        .car-rate-link {
            color: #28a745;
            cursor: pointer;
            text-decoration: none;
            font-weight: 700;
            display: inline-flex;
            align-items: center;
            gap: 5px;
        }

        .car-rate-link:hover {
            text-decoration: underline;
            color: #218838;
        }

        .invoice-actions-cell {

            display: flex;

            gap: 5px;

            justify-content: center;

            align-items: center;

        }


        .invoice-actions-cell .btn {

            padding: 5px 10px;

            border-radius: 4px;

            transition: all 0.2s;

        }


        .invoice-actions-cell .btn:hover {

            transform: translateY(-2px);

            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);

        }


        .btn-outline-primary {

            border: 1px solid #007bff;

            color: #007bff;

            background: transparent;

        }


        .btn-outline-primary:hover {

            background-color: #007bff;

            color: white;

        }


        .btn-outline-success {

            border: 1px solid #28a745;

            color: #28a745;

            background: transparent;

        }


        .btn-outline-success:hover {

            background-color: #28a745;

            color: white;

        }

        .filter-buttons-container {
            display: flex;
            gap: 12px;
            align-items: center;
        }

        .btn-apply-filter {
            background: linear-gradient(135deg, #34D399 0%, #10B981 100%);
            color: white;
            border: none;
            padding: 10px 28px;
            border-radius: 8px;
            font-weight: 600;
            font-size: 14px;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 2px 8px rgba(16, 185, 129, 0.3);
        }

        .btn-apply-filter:hover {
            background: linear-gradient(135deg, #10B981 0%, #059669 100%);
            box-shadow: 0 4px 12px rgba(16, 185, 129, 0.4);
            transform: translateY(-2px);
        }

        .btn-apply-filter:active {
            transform: translateY(0);
        }

        .btn-clear-filter {
            background: transparent;
            color: #6B7280;
            border: none;
            padding: 10px 0;
            font-weight: 600;
            font-size: 14px;
            cursor: pointer;
            transition: all 0.2s ease;
            text-decoration: none;
        }

        .btn-clear-filter:hover {
            color: #1F2937;
            text-decoration: underline;
        }
    </style>
</head>

<body>
<div id="wrapper">

    <jsp:include page="../common/customer/_header.jsp"/>

    <div class="no-bottom no-top zebra" id="content">
        <section id="subheader" class="jarallax text-light">
            <img src="${pageContext.request.contextPath}/images/background/14.jpg" class="jarallax-img" alt="">
            <div class="center-y relative text-center">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center">
                            <h1>Chuyến của tôi</h1>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <section id="section-cars" class="bg-gray-100">
            <div class="container">
                <div class="row">
                    <div class="col-lg-3 mb30">
                        <jsp:include page="../common/customer/_sidebar.jsp">
                            <jsp:param name="activePage" value="orders"/>
                        </jsp:include>
                    </div>

                    <div class="col-lg-9">
                        <div class="row mb25">
                            <div class="col-lg-4 col-6 mb25">
                                <div class="card padding30 text-center rounded-5">
                                    <i class="fa fa-calendar-check-o fa-2x text-success mb10"></i>
                                    <span class="h2 mb0">${upcoming}</span><br>Chuyến Sắp Tới
                                </div>
                            </div>

                            <div class="col-lg-4 col-6 mb25">
                                <div class="card padding30 text-center rounded-5">
                                    <i class="fa fa-calendar fa-2x text-success mb10"></i>
                                    <span class="h2 mb0">${total}</span><br>Tổng Số Chuyến
                                </div>
                            </div>

                            <div class="col-lg-4 col-6 mb25">
                                <div class="card padding30 text-center rounded-5">
                                    <i class="fa fa-calendar-times-o fa-2x text-danger mb10"></i>
                                    <span class="h2 mb0">${cancelled}</span><br>Số Chuyến Hủy
                                </div>
                            </div>
                        </div>

                        <div class="card padding30 rounded-5 mb25">
                            <h4>Chuyến Của Tôi</h4>

                            <div class="tab-container">
                                <button class="tab-btn ${tab == 'current' || empty tab ? 'active' : ''}"
                                        onclick="window.location.href='${pageContext.request.contextPath}/customer/customerOrder?tab=current&page=1'">
                                    Chuyến Hiện Tại
                                </button>
                                <button class="tab-btn ${tab == 'history' ? 'active' : ''}"
                                        onclick="window.location.href='${pageContext.request.contextPath}/customer/customerOrder?tab=history&page=1'">
                                    Lịch Sử Chuyến
                                </button>
                            </div>

                            <div class="filter-section" style="background: #f8f9fa; padding: 20px; border-radius: 8px; margin-bottom: 20px;">
                                <form method="get" action="${pageContext.request.contextPath}/customer/customerOrder" id="filterForm">
                                    <input type="hidden" name="tab" value="${tab}">

                                    <div class="row">
                                        <!-- Status Filter -->
                                        <div class="col-md-3 mb-3">
                                            <label style="font-weight: 600; font-size: 13px; color: #666; margin-bottom: 5px;">
                                                <i class="fa fa-filter"></i> Status
                                            </label>
                                            <select name="filterStatus" class="form-control form-control-sm">
                                                <option value="All" ${filterStatus == null || filterStatus == 'All' ? 'selected' : ''}>Tất Cả Trạng Thái</option>
                                                <c:if test="${tab == 'current' || empty tab}">
                                                    <option value="Pending" ${filterStatus == 'Pending' ? 'selected' : ''}>Chờ Duyệt</option>
                                                    <option value="Approved" ${filterStatus == 'Approved' ? 'selected' : ''}>Được Chấp nhận</option>
                                                    <option value="Paid" ${filterStatus == 'Paid' ? 'selected' : ''}>Đã Thanh Toán</option>
                                                    <option value="Returning" ${filterStatus == 'Returning' ? 'selected' : ''}>Đang Trả</option>
                                                </c:if>
                                                <c:if test="${tab == 'history'}">
                                                    <option value="Completed" ${filterStatus == 'Completed' ? 'selected' : ''}>Đã Hoàn Tất</option>
                                                    <option value="Cancelled" ${filterStatus == 'Cancelled' ? 'selected' : ''}>Đã Hủy</option>
                                                    <option value="Rejected" ${filterStatus == 'Rejected' ? 'selected' : ''}>Bị Từ Chối</option>
                                                </c:if>
                                            </select>
                                        </div>

                                        <!-- Car Name Search -->
                                        <div class="col-md-3 mb-3">
                                            <label style="font-weight: 600; font-size: 13px; color: #666; margin-bottom: 5px;">
                                                <i class="fa fa-car"></i> Tên Xe
                                            </label>
                                            <input type="text" name="carName" class="form-control form-control-sm"
                                                   placeholder="Tìm Kiếm Theo Tên Xe..." value="${carName}">
                                        </div>

                                        <!-- Price Range -->
                                        <div class="col-md-3 mb-3">
                                            <label style="font-weight: 600; font-size: 13px; color: #666; margin-bottom: 5px;">
                                                <i class="fa fa-money"></i> Khoảng Tiền
                                            </label>
                                            <select name="priceRange" class="form-control form-control-sm">
                                                <option value="" ${priceRange == null || priceRange == '' ? 'selected' : ''}>Tất Cả</option>
                                                <option value="under1m" ${priceRange == 'under1m' ? 'selected' : ''}>Dưới 1 triệu</option>
                                                <option value="1m-3m" ${priceRange == '1m-3m' ? 'selected' : ''}>1 triệu - 3 triệu</option>
                                                <option value="3m-5m" ${priceRange == '3m-5m' ? 'selected' : ''}>3 triệu - 5 triệu</option>
                                                <option value="over5m" ${priceRange == 'over5m' ? 'selected' : ''}>Trên 5 triệu</option>
                                            </select>
                                        </div>

                                        <!-- Buttons -->
                                        <div class="col-md-3 mb-3" style="display: flex; align-items: flex-end; gap: 5px;">
                                            <div class="filter-buttons-container">
                                                <a href="#" onclick="clearFilters(); return false;" class="btn-clear-filter">
                                                    Xóa bộ lọc
                                                </a>
                                                <button type="submit" class="btn-apply-filter">
                                                    Áp dụng
                                                </button>
                                            </div>
                                    </div>

                                    <!-- Date Range Filters (Collapsible) -->
                                    <div class="row" style="margin-top: 10px;">
                                        <div class="col-12">
                                            <a href="#" onclick="toggleAdvancedFilters(); return false;" style="font-size: 13px; color: #00b074;">
                                                <i class="fa fa-caret-down" id="advancedFilterIcon"></i> Lọc Nâng Cao
                                            </a>
                                        </div>
                                    </div>

                                    <div id="advancedFilters" style="display: none; margin-top: 15px; padding-top: 15px; border-top: 1px solid #dee2e6;">
                                        <div class="row">
                                            <!-- Simplified Date Range -->
                                            <div class="col-md-12 mb-3">
                                                <label style="font-weight: 600; font-size: 13px; color: #666; margin-bottom: 5px;">
                                                    <i class="fa fa-calendar"></i> Khoảng Thời Gian Thuê
                                                </label>
                                                <div class="row">
                                                    <div class="col-6">
                                                        <input type="date" name="dateFrom" class="form-control form-control-sm"
                                                               value="${dateFrom}" placeholder="From Date">
                                                        <small class="text-muted">Từ</small>
                                                    </div>
                                                    <div class="col-6">
                                                        <input type="date" name="dateTo" class="form-control form-control-sm"
                                                               value="${dateTo}" placeholder="To Date">
                                                        <small class="text-muted">Đến</small>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                            <!-- ========== TABLE ========== -->
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th>Tên Xe</th>
                                        <th>Địa Điểm </th>
                                        <th>Thời gian lấy xe</th>
                                        <th>Thời Gian trả xe</th>
                                        <th>Giá Tiền</th>
                                        <th>Hóa Đơn</th>
                                        <th>Trạng Thái</th>
                                        <c:if test="${tab == 'current' || empty tab}">
                                            <th>Thao Tác</th>
                                        </c:if>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:choose>
                                        <c:when test="${empty bookings}">
                                            <tr>
                                                <td colspan="8" class="text-center" style="padding: 40px;">
                                                    <i class="fa fa-inbox fa-3x"
                                                       style="color: #ccc; margin-bottom: 15px;"></i>
                                                    <p style="color: #999;">No bookings found</p>
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="order" items="${bookings}">
                                                <tr>
                                                    <td>
                                                        <c:choose>

                                                            <c:when test="${tab == 'history' && order.status == 'Completed'}">
                                                                <a class="car-rate-link"
                                                                   data-booking-id="${order.bookingId}"
                                                                   data-car-name="${order.carName}">
                                                                    <c:out value="${order.carName}"/>
                                                                    <i class="fa fa-star"></i>
                                                                </a>
                                                            </c:when>


                                                            <c:when test="${tab == 'history'}">
                                                                <c:out value="${order.carName}"/>
                                                            </c:when>


                                                            <c:otherwise>
                                                                <a class="car-name-link"
                                                                   onclick="showOwnerInfo(${order.bookingId}, '${order.carName}')">
                                                                    <c:out value="${order.carName}"/>
                                                                    <i class="fa fa-info-circle"></i>
                                                                </a>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td><c:out value="${order.location}"/></td>
                                                    <td><c:out value="${order.startDate}"/> ${order.pickupTime}</td>
                                                    <td><c:out value="${order.endDate}"/> ${order.dropoffTime}</td>
                                                    <td style="white-space: nowrap;">
                                                        <fmt:formatNumber value="${order.totalPrice}"
                                                                          type="number"
                                                                          groupingUsed="true"
                                                                          minFractionDigits="0"
                                                                          maxFractionDigits="0"/> ₫
                                                    </td>
                                                    <td>

                                                        <c:choose>

                                                            <c:when test="${order.status == 'Paid' || order.status == 'Returning' || order.status == 'Completed'}">

                                                                <div class="invoice-actions-cell">

                                                                    <a href="${pageContext.request.contextPath}/customer/download-invoice?bookingId=${order.bookingId}"

                                                                       class="btn btn-sm btn-outline-primary"

                                                                       target="_blank"

                                                                       title="Xem hóa đơn">

                                                                        <i class="fa fa-file-text-o"></i>

                                                                    </a>

                                                                    <a href="${pageContext.request.contextPath}/customer/download-invoice?bookingId=${order.bookingId}&action=download"

                                                                       class="btn btn-sm btn-outline-success"

                                                                       download="invoice-${order.bookingId}.html"

                                                                       title="Tải hóa đơn">

                                                                        <i class="fa fa-download"></i>

                                                                    </a>

                                                                </div>

                                                            </c:when>

                                                            <c:otherwise>

                                                                <span style="color: #ccc;">-</span>

                                                            </c:otherwise>

                                                        </c:choose>

                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${order.status == 'Pending'}">
                                                                <span class="badge bg-warning text-dark">
                                                                    <i class="fa fa-clock-o"></i> Chờ Duyệt
                                                                </span>
                                                            </c:when>
                                                            <c:when test="${order.status == 'Approved'}">
                                                                <span class="badge bg-info-dark">
                                                                    <i class="fa fa-check"></i> Được Chấp Nhận
                                                                </span>
                                                            </c:when>
                                                            <c:when test="${order.status == 'Paid'}">
                                                                <span class="badge bg-primary-dark">
                                                                    <i class="fa fa-credit-card"></i> Đã Thanh Toán
                                                                </span>
                                                            </c:when>
                                                            <c:when test="${order.status == 'Returning'}">
                                                                <span class="badge bg-returning">
                                                                    <i class="fa fa-undo"></i> Đang Trả Xe
                                                                </span>
                                                            </c:when>
                                                            <c:when test="${order.status == 'Completed'}">
                                                                <span class="badge bg-success">
                                                                    <i class="fa fa-check-circle"></i> Đã Hoàn Tất
                                                                </span>
                                                            </c:when>
                                                            <c:when test="${order.status == 'Cancelled'}">
                                                                <span class="badge bg-danger">
                                                                    <i class="fa fa-times-circle"></i> Đã hủy
                                                                </span>
                                                            </c:when>
                                                            <c:when test="${order.status == 'Rejected'}">
                                                                <span class="badge bg-danger">
                                                                    <i class="fa fa-ban"></i> Bị Từ Chối
                                                                </span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-info text-dark">${order.status}</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <c:if test="${tab == 'current' || empty tab}">
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${order.status == 'Pending'}">
                                                                    <a href="${pageContext.request.contextPath}/customer/cancelBooking?bookingId=${order.bookingId}"
                                                                       onclick="return confirm('Are you sure you want to cancel this booking?');"
                                                                       class="btn btn-sm btn-danger">
                                                                        <i class="fa fa-times"></i> Hủy
                                                                    </a>
                                                                </c:when>
                                                                <c:when test="${order.status == 'Approved'}">
                                                                    <a href="${pageContext.request.contextPath}/customer/create-payment?bookingId=${order.bookingId}"
                                                                       class="btn btn-sm btn-info">
                                                                        <i class="fa fa-credit-card"></i> Thanh Toán
                                                                    </a>
                                                                </c:when>
                                                                <c:when test="${order.status == 'Paid'}">
                                                                    <a href="${pageContext.request.contextPath}/customer/returnCar?bookingId=${order.bookingId}"
                                                                       onclick="return confirm('Request to return this car? Owner will need to confirm.');"
                                                                       class="btn btn-sm btn-success">
                                                                        <i class="fa fa-undo"></i> Trả Xe
                                                                    </a>
                                                                </c:when>
                                                                <c:when test="${order.status == 'Returning'}">
                                                                    <span class="text-warning" style="font-size: 13px;">
                                                                        <i class="fa fa-clock-o"></i> Vui Lòng Chờ Chủ Xe...
                                                                    </span>
                                                                </c:when>
                                                            </c:choose>
                                                        </td>
                                                    </c:if>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                    </tbody>
                                </table>
                            </div>

                            <!-- ========== PAGINATION ========== -->
                            <c:if test="${totalPages > 1}">
                                <div class="pagination-container">
                                    <ul class="pagination">
                                        <!-- Previous Button -->
                                        <li class="${currentPage == 1 ? 'disabled' : ''}">
                                            <a href="${pageContext.request.contextPath}/customer/customerOrder?tab=${tab}&page=${currentPage - 1}&filterStatus=${filterStatus}&dateFrom=${dateFrom}&dateTo=${dateTo}&carName=${carName}&priceRange=${priceRange}">
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
                                                        <a href="${pageContext.request.contextPath}/customer/customerOrder?tab=${tab}&page=${i}&filterStatus=${filterStatus}&dateFrom=${dateFrom}&dateTo=${dateTo}&carName=${carName}&priceRange=${priceRange}">
                                                                ${i}
                                                        </a>
                                                    </li>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>

                                        <!-- Next Button -->
                                        <li class="${currentPage == totalPages ? 'disabled' : ''}">
                                            <a href="${pageContext.request.contextPath}/customer/customerOrder?tab=${tab}&page=${currentPage + 1}&filterStatus=${filterStatus}&dateFrom=${dateFrom}&dateTo=${dateTo}&carName=${carName}&priceRange=${priceRange}">
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
            </div>
        </section>
    </div>

    <jsp:include page="../common/customer/_footer_scripts.jsp"/>
</div>

<!-- ========== OWNER INFO MODAL ========== -->
<div id="ownerModal" class="owner-modal">
    <div class="owner-modal-content">
        <div class="owner-modal-header">
            <h3>Owner Contact Information</h3>
            <span class="owner-modal-close" onclick="closeOwnerModal()">&times;</span>
        </div>
        <div class="owner-modal-body" id="ownerInfoContent">
            <!-- Content loaded by JS -->
        </div>
    </div>
</div>

<!-- ========== RATE & REVIEW MODAL ========== -->
<div id="rateModal" class="owner-modal">
    <div class="owner-modal-content">
        <div class="owner-modal-header" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
            <h3>Đánh Giá Chuyến Đi</h3>
            <span class="owner-modal-close" onclick="closeRateModal()">&times;</span>
        </div>
        <div class="owner-modal-body">
            <div id="rateCarName"
                 style="font-weight:700; text-align:center; margin-bottom:15px; font-size:18px; color:#333;"></div>

            <!-- 5 Stars -->
            <div style="text-align:center; margin-bottom:20px;">
                <span class="star" data-value="1">&#9733;</span>
                <span class="star" data-value="2">&#9733;</span>
                <span class="star" data-value="3">&#9733;</span>
                <span class="star" data-value="4">&#9733;</span>
                <span class="star" data-value="5">&#9733;</span>
            </div>

            <!-- Feedback -->
            <textarea id="feedbackText"
                      class="form-control"
                      rows="4"
                      placeholder="Share your experience with this car..."></textarea>

            <div style="text-align:center; margin-top:20px;">
                <button class="btn btn-success" id="submitRatingBtn">
                    <i class="fa fa-paper-plane"></i> Gửi
                </button>
            </div>
        </div>
    </div>
</div>

<!-- ========== JAVASCRIPT ========== -->
<script>
    // ========== OWNER INFO MODAL FUNCTIONS ==========
    function showOwnerInfo(bookingId, carName) {
        const modal = document.getElementById('ownerModal');
        const content = document.getElementById('ownerInfoContent');

        content.innerHTML = `
            <div class="loading-spinner">
                <i class="fa fa-spinner fa-spin"></i>
                <p style="margin-top: 15px; color: #666;">Loading owner information...</p>
            </div>
        `;
        modal.style.display = 'block';

        fetch('${pageContext.request.contextPath}/customer/owner-info?bookingId=' + bookingId)
            .then(response => {
                if (!response.ok) throw new Error('Network error');
                return response.json();
            })
            .then(data => {
                const avatarUrl = data.avatar && data.avatar.trim() !== ''
                    ? '${pageContext.request.contextPath}' + data.avatar
                    : '${pageContext.request.contextPath}/images/profile/default.jpg';

                content.innerHTML = `
                    <div style="text-align: center;">
                        <img src="` + avatarUrl + `"
                             class="owner-avatar"
                             onerror="this.src='${pageContext.request.contextPath}/images/profile/default.jpg'">
                    </div>
                    <div class="owner-info-item">
                        <div class="owner-info-label">
                            <i class="fa fa-car"></i> Car
                        </div>
                        <div class="owner-info-value">` + carName + `</div>
                    </div>
                    <div class="owner-info-item">
                        <div class="owner-info-label">
                            <i class="fa fa-user"></i> Owner Name
                        </div>
                        <div class="owner-info-value">` + (data.fullName || 'N/A') + `</div>
                    </div>
                    <div class="owner-info-item">
                        <div class="owner-info-label">
                            <i class="fa fa-phone"></i> Phone Number
                        </div>
                        <div class="owner-info-value">` + (data.phone || 'N/A') + `</div>
                    </div>
                `;
            })
            .catch(error => {
                console.error('Error:', error);
                content.innerHTML = `
                    <div style="text-align: center; padding: 40px; color: #dc3545;">
                        <i class="fa fa-exclamation-circle" style="font-size: 48px; margin-bottom: 15px;"></i>
                        <p style="font-weight: 600;">Failed to load owner information</p>
                        <p style="color: #666; font-size: 14px;">Please try again later</p>
                        <button onclick="closeOwnerModal()"
                                style="margin-top: 20px; padding: 10px 30px; background: #6c757d;
                                       color: white; border: none; border-radius: 8px; cursor: pointer;">
                            Close
                        </button>
                    </div>
                `;
            });
    }

    function closeOwnerModal() {
        document.getElementById('ownerModal').style.display = 'none';
    }

    // ========== RATE & REVIEW MODAL FUNCTIONS ==========
    let selectedRating = 0;
    let currentBookingId = null;

    // Event listener cho car rate links
    document.addEventListener("click", function (e) {
        if (e.target.closest('.car-rate-link')) {
            e.preventDefault();
            const link = e.target.closest('.car-rate-link');
            currentBookingId = link.dataset.bookingId;
            const carName = link.dataset.carName;

            document.getElementById("rateCarName").innerText = carName;
            document.getElementById("rateModal").style.display = "block";
            document.getElementById("feedbackText").value = "";
            selectedRating = 0;
            document.querySelectorAll(".star").forEach(star => star.classList.remove("selected"));
        }
    });

    // Star selection
    document.querySelectorAll(".star").forEach(star => {
        star.addEventListener("click", function () {
            selectedRating = this.dataset.value;
            document.querySelectorAll(".star").forEach(s => s.classList.remove("selected"));
            for (let i = 0; i < selectedRating; i++) {
                document.querySelectorAll(".star")[i].classList.add("selected");
            }
        });
    });

    // Submit rating
    document.getElementById("submitRatingBtn").addEventListener("click", function () {
        const feedback = document.getElementById("feedbackText").value.trim();

        if (selectedRating === 0) {
            alert("Vui lòng đánh giá từ 1 đến 5 sao!");
            return;
        }

        fetch("${pageContext.request.contextPath}/customer/rateCar", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                bookingId: currentBookingId,
                rating: selectedRating,
                feedback: feedback
            })
        })
            .then(response => {
                if (!response.ok) throw new Error("Network error");
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    alert("Cảm ơn đánh giá của bạn!");
                    closeRateModal();
                } else {
                    alert(data.message || "Failed to submit feedback. Please try again.");
                }
            })
            .catch(err => {
                console.error(err);
                alert("Error while submitting feedback!");
            });
    });

    function toggleAdvancedFilters() {
        const filters = document.getElementById('advancedFilters');
        const icon = document.getElementById('advancedFilterIcon');
        if (filters.style.display === 'none') {
            filters.style.display = 'block';
            icon.className = 'fa fa-caret-up';
        } else {
            filters.style.display = 'none';
            icon.className = 'fa fa-caret-down';
        }
    }

    function clearFilters() {
        window.location.href = '${pageContext.request.contextPath}/customer/customerOrder?tab=${tab}&page=1';
    }

    function closeRateModal() {
        document.getElementById("rateModal").style.display = "none";
    }

    // ========== GLOBAL MODAL CLOSE HANDLERS ==========
    window.onclick = function (event) {
        const ownerModal = document.getElementById('ownerModal');
        const rateModal = document.getElementById('rateModal');

        if (event.target == ownerModal) {
            closeOwnerModal();
        }
        if (event.target == rateModal) {
            closeRateModal();
        }
    }

    document.addEventListener('keydown', function (event) {
        if (event.key === 'Escape') {
            closeOwnerModal();
            closeRateModal();
        }
    });
</script>
</body>
</html>