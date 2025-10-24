<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- KHAI BÁO BEAN CHO IDE --%>
<jsp:useBean id="car" class="model.CarViewModel" scope="request" />
<jsp:useBean id="booking" class="model.Booking" scope="request" />

<!DOCTYPE html>
<html lang="vi">
<head>

    <jsp:include page="/view/common/customer/_head.jsp"/>
    <title>Xác nhận đặt xe - Rentaly</title>
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

        <section id="subheader" class="jarallax text-light" style="background-image: url('${pageContext.request.contextPath}/images/background/2.jpg');">
            <div class="center-y relative text-center">
                <div class="container-content">
                    <h1>Booking Confirmation</h1>
                </div>
            </div>
        </section>

        <section style="padding: 60px 0; background: #f5f5f5;">
            <div class="container-content container">

                <c:choose>
                    <c:when test="${booking == null}">
                        <div class="alert alert-danger container-content">
                            <h3> Không tìm thấy thông tin booking!</h3>
                            <p>Vui lòng thử lại.</p>
                            <a href="${pageContext.request.contextPath}/home" class="btn-home">Về trang chủ</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="success-box">
                            <div class="success-icon">✓</div>
                            <h2 style="color: #10b981; margin: 0;">Booking Successfully!</h2>
                            <p style="margin: 10px 0 0 0; color: #059669;">Please waiting car owner response.</p>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <div class="info-section">
                                    <h4>👤 Customer Information</h4>
                                    <div class="info-row">
                                        <span class="info-label">Name:</span>
                                        <span class="info-value">${u.fullName}</span>
                                    </div>
                                    <div class="info-row">
                                        <span class="info-label">Phone Number:</span>
                                        <span class="info-value">${u.phone}</span>
                                    </div>
                                </div>

                                <div class="info-section">
                                    <h4>📅 Booking Details</h4>
                                    <div class="info-row">
                                        <span class="info-label">Pickup Date:</span>
                                        <span class="info-value">

                                            ${booking.startDate} ${booking.pickupTime}
                                        </span>
                                    </div>
                                    <div class="info-row">
                                        <span class="info-label">Return Date:</span>
                                        <span class="info-value">

                                            ${booking.endDate} ${booking.dropoffTime}
                                        </span>
                                    </div>
                                    <div class="info-row">
                                        <span class="info-label">Location:</span>
                                        <span class="info-value">${car.location}</span>
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <c:if test="${car != null}">
                                    <div class="info-section">
                                        <h4>🚗 Car Detail</h4>
                                        <div class="info-row">
                                            <span class="info-label">Car:</span>
                                            <span class="info-value">${car.model}</span>
                                        </div>
                                        <div class="info-row">
                                            <span class="info-label">License Plate: </span>
                                            <span class="info-value">${car.licensePlate}</span>
                                        </div>
                                    </div>
                                </c:if>

                                <div class="info-section">
                                    <h4>💰 Payment</h4>
                                    <div class="info-row">
                                        <span class="info-label">Rental Fee:</span>
                                        <span class="info-value">
                                                <fmt:formatNumber value="${booking.totalPrice + (discount != null ? discount : 0)}"
                                                                  type="number" maxFractionDigits="0"/>₫
                                            </span>
                                    </div>
                                    <c:if test="${not empty discount && discount > 0}">
                                        <div class="info-row" style="color: #10b981;">
                                            <span class="info-label">Discount:</span>
                                            <span class="info-value">
                                                    -<fmt:formatNumber value="${discount}" type="number" maxFractionDigits="0"/>₫
                                                </span>
                                        </div>
                                    </c:if>
                                    <div class="info-row total-price-row">
                                        <span class="info-label" style="font-size: 1em;">Total Price:</span>
                                        <span class="info-value" style="font-size: 1.2em;">
                                                <fmt:formatNumber value="${booking.totalPrice}" type="number" maxFractionDigits="0"/>₫
                                            </span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div style="text-align: center;">
                            <a href="${pageContext.request.contextPath}/home" class="btn-home">
                                🏠 Back to home
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
    </div>

    <a href="#" id="back-to-top"></a>



<%-- INCLUDE SCRIPT CHUNG --%>
<jsp:include page="/view/common/customer/_footer_scripts.jsp"/>
</body>
</html>