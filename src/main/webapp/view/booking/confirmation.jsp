<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="/view/common/customer/_head.jsp"/>

    <title>Booking Confirmation - Rentaly</title>
    <style>
        .container-content {
            max-width: 1200px;
            margin: 0 auto;
            padding: 30px 15px;
        }
        .success-box {
            background: #d1fae5;
            border: 2px solid #10b981;
            padding: 30px;
            border-radius: 8px;
            text-align: center;
            margin-bottom: 30px;
        }
        .success-icon {
            width: 80px;
            height: 80px;
            background: #10b981;
            border-radius: 50%;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 40px;
            margin-bottom: 15px;
        }
        .info-section {
            background: white;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.05);
        }
        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #f0f0f0;
        }
        .info-row:last-child {
            border-bottom: none;
        }
        .info-label {
            font-weight: 500;
            color: #6b7280;
        }
        .info-value {
            font-weight: 600;
            color: #1f2937;
            text-align: right;
        }
        .total-price-row {
            border-top: 2px solid #10b981;
            padding-top: 15px !important;
            margin-top: 10px;
            font-size: 1.2em;
            font-weight: bold;
            color: #10b981;
        }
        .btn-home {
            background: #10b981;
            color: white;
            padding: 12px 30px;
            text-decoration: none;
            border-radius: 5px;
            display: inline-block;
            margin-top: 20px;
            transition: background 0.3s;
        }
        .btn-home:hover {
            background: #059669;
            color: white;
        }
    </style>
</head>
<body>
<div id="wrapper">

    <jsp:include page="/view/common/customer/_header.jsp"/>

    <div class="no-bottom no-top" id="content">
        <div id="top"></div>

        <section id="subheader" class="jarallax text-light"
                 style="background-image: url('${pageContext.request.contextPath}/images/background/2.jpg');">
            <div class="center-y relative text-center">
                <div class="container-content">
                    <h1>Xác Nhận Thuê Xe</h1>
                </div>
            </div>
        </section>

        <section style="padding: 60px 0; background: #f5f5f5;">
            <div class="container-content container">

                <c:choose>
                    <c:when test="${booking == null}">
                        <div class="alert alert-danger container-content">
                            <h3>❌ Không Tìm Được Chuyến </h3>
                            <p>Please try again.</p>
                            <a href="${pageContext.request.contextPath}/home" class="btn-home">Quay Về Trang Chủ</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <%-- ✅ SET BIẾN DISCOUNT (nếu null thì = 0) --%>
                        <c:set var="discountValue" value="${discount != null ? discount : 0}" />
                        <c:set var="rentalFee" value="${booking.totalPrice + discountValue}" />

                        <div class="success-box">
                            <div class="success-icon">✓</div>
                            <h2 style="color: #10b981; margin: 0;">Thuê Xe Thành Công!</h2>
                            <p style="margin: 10px 0 0 0; color: #059669;">Vui Lòng Đợi Chủ Xe Duyệt</p>
                        </div>

                        <div class="row">
                            <!-- LEFT COLUMN -->
                            <div class="col-md-6">
                                <!-- Customer Information -->
                                <div class="info-section">
                                    <h4>👤 Thông Tin Khách Hàng</h4>
                                    <div class="info-row">
                                        <span class="info-label">Họ Và Tên:</span>
                                        <span class="info-value">${u.fullName}</span>
                                    </div>
                                    <div class="info-row">
                                        <span class="info-label">Số Điện Thoại:</span>
                                        <span class="info-value">${u.phone}</span>
                                    </div>
                                </div>

                                <!-- Booking Details -->
                                <div class="info-section">
                                    <h4>📅 Thông Tin Chuyến</h4>
                                    <div class="info-row">
                                        <span class="info-label">Thời Gian Lấy Xe:</span>
                                        <span class="info-value">
                                            ${booking.startDate} ${booking.pickupTime}
                                        </span>
                                    </div>
                                    <div class="info-row">
                                        <span class="info-label">Thời Gian Trả Xe:</span>
                                        <span class="info-value">
                                            ${booking.endDate} ${booking.dropoffTime}
                                        </span>
                                    </div>
                                    <div class="info-row">
                                        <span class="info-label">Địa Điểm:</span>
                                        <span class="info-value">
                                            <c:choose>
                                                <c:when test="${not empty booking.location}">
                                                    ${booking.location}
                                                </c:when>
                                                <c:otherwise>
                                                    ${car.location}
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <!-- RIGHT COLUMN -->
                            <div class="col-md-6">
                                <!-- Car Details -->
                                <c:if test="${car != null}">
                                    <div class="info-section">
                                        <h4>🚗 Thông Tin Xe</h4>
                                        <div class="info-row">
                                            <span class="info-label">Xe:</span>
                                            <span class="info-value">${car.model}</span>
                                        </div>
                                        <div class="info-row">
                                            <span class="info-label">Biển Số Xe:</span>
                                            <span class="info-value">${car.licensePlate}</span>
                                        </div>
                                    </div>
                                </c:if>

                                <!-- Payment Summary -->
                                <div class="info-section">
                                    <h4>💰 Thông Tin Thanh Toán</h4>

                                    <!-- Rental Fee (GIÁ GỐC) -->
                                    <div class="info-row">
                                        <span class="info-label">Đơn Giá Thuê:</span>
                                        <span class="info-value">
                                              <fmt:formatNumber value="${rentalFee}"
                                                                type="number" groupingUsed="true"
                                                                minFractionDigits="0" maxFractionDigits="0"/> ₫
                                        </span>
                                    </div>

                                    <!-- Discount (CHỈ HIỂN THỊ NẾU > 0) -->
                                    <c:if test="${discountValue > 0}">
                                        <div class="info-row" style="color: #dc2626;">
                                            <span class="info-label">Khuyến Mãi:</span>
                                            <span class="info-value">
                                                  -<fmt:formatNumber value="${discountValue}"
                                                                    type="number" groupingUsed="true"
                                                                    minFractionDigits="0" maxFractionDigits="0"/> ₫
                                            </span>
                                        </div>
                                    </c:if>

                                    <!-- Total Price (GIÁ CUỐI CÙNG) -->
                                    <div class="info-row total-price-row">
                                        <span class="info-label">Thành Tiền:</span>
                                        <span class="info-value">
                                            <fmt:formatNumber value="${booking.totalPrice}"
                                                              type="number" groupingUsed="true"
                                                              minFractionDigits="0" maxFractionDigits="0"/> ₫
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div style="text-align: center;">
                            <a href="${pageContext.request.contextPath}/home" class="btn-home">
                                🏠 Quay Về Trang Chủ
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
    </div>

    <a href="#" id="back-to-top"></a>


</div>

<jsp:include page="/view/common/customer/_footer_scripts.jsp"/>
</body>
</html>