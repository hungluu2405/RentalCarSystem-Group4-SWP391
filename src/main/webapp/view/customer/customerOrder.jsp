<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../common/customer/_head.jsp"/>
    <style>
        /* CSS Giữ Nguyên, chỉ thêm các lớp màu mới */
        .tab-container { display: flex; gap: 30px; border-bottom: 2px solid #eaeaea; margin-top: 10px; margin-bottom: 20px; }
        .tab-btn { background: none; border: none; font-weight: 600; font-size: 16px; padding: 8px 0; cursor: pointer; color: #222; transition: all 0.3s ease; position: relative; }
        .tab-btn.active { color: #00b074; }
        .tab-btn.active::after { content: ""; position: absolute; bottom: -2px; left: 0; height: 3px; width: 100%; background-color: #00b074; border-radius: 5px; }
        .tab-content { display: none; }
        .tab-content.active { display: block; }
        .badge { padding: 5px 10px; border-radius: 10px; font-size: 12px; }

        /* Cập nhật/Thêm màu sắc cho trạng thái */
        .bg-warning { background-color: #FFD54F; color: #222; } /* Pending */
        .bg-info-dark { background-color: #5bc0de; color: white; } /* Approved */
        .bg-primary-dark { background-color: #007bff; color: white; } /* PAID */
        .bg-success { background-color: #66BB6A; color: white; } /* COMPLETED */
        .bg-danger { background-color: #dc3545; color: white; } /* CANCELLED/REJECTED */
    </style>
</head>

<body>
<div id="wrapper">

    <jsp:include page="../common/customer/_header.jsp"/>

    <div class="no-bottom no-top zebra" id="content">
        <%-- PHẦN HEADER VÀ SIDEBAR GIỮ NGUYÊN --%>
        <section id="subheader" class="jarallax text-light">
            <img src="${pageContext.request.contextPath}/images/background/14.jpg" class="jarallax-img" alt="">
            <div class="center-y relative text-center">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center">
                            <h1>My Orders</h1>
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

                    <%-- Nội dung bên phải --%>
                    <div class="col-lg-9">
                        <%-- Thống kê các đơn hàng (Giữ nguyên) --%>
                        <div class="row mb25">
                            <div class="col-lg-4 col-6 mb25">
                                <div class="card padding30 text-center rounded-5">
                                    <i class="fa fa-calendar-check-o fa-2x text-success mb10"></i>
                                    <span class="h2 mb0">${upcoming}</span><br>Upcoming Orders
                                </div>
                            </div>

                            <div class="col-lg-4 col-6 mb25">
                                <div class="card padding30 text-center rounded-5">
                                    <i class="fa fa-calendar fa-2x text-success mb10"></i>
                                    <span class="h2 mb0">${total}</span><br>Total Orders
                                </div>
                            </div>

                            <div class="col-lg-4 col-6 mb25">
                                <div class="card padding30 text-center rounded-5">
                                    <i class="fa fa-calendar-times-o fa-2x text-danger mb10"></i>
                                    <span class="h2 mb0">${cancelled}</span><br>Cancel Orders
                                </div>
                            </div>
                        </div>

                        <div class="card padding30 rounded-5 mb25">
                            <h4>My Orders</h4>

                            <div class="tab-container">
                                <button class="tab-btn active" id="tabCurrent">Current Trips</button>
                                <button class="tab-btn" id="tabHistory">Trip History</button>
                            </div>

                            <div id="currentTrips" class="tab-content active">
                                <div class="table-responsive">
                                    <table class="table">
                                        <thead>
                                        <tr>
                                            <th>Car Name</th>
                                            <th>Location</th>
                                            <th>Pick Up Date</th>
                                            <th>Return Date</th>
                                            <th>Price</th>
                                            <th>Status</th>
                                            <th>Actions</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="order" items="${allBookings}">
                                            <c:set var="status" value="${order.status}"/>
                                            <c:set var="isCurrent" value="${status == 'Pending' || status == 'Approved' || status == 'Paid'}"/>

                                            <c:if test="${isCurrent}">
                                                <tr>
                                                    <td><strong><c:out value="${order.carName}"/></strong></td>
                                                    <td><c:out value="${order.location}"/></td>
                                                    <td><c:out value="${order.startDate}"/> ${order.pickupTime}</td>
                                                    <td><c:out value="${order.endDate}"/> ${order.dropoffTime}</td>
                                                    <td><c:out value="${order.totalPrice}"/></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${status == 'Pending'}">
                                                                <span class="badge bg-warning text-dark">Pending</span>
                                                            </c:when>
                                                            <c:when test="${status == 'Approved'}">
                                                                <span class="badge bg-info-dark">Approved</span>
                                                            </c:when>
                                                            <c:when test="${status == 'Paid'}">
                                                                <span class="badge bg-primary-dark">Paid</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-info text-dark">${status}</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:if test="${status == 'Pending'}">
                                                            <a href="${pageContext.request.contextPath}/customer/cancelBooking?bookingId=${order.bookingId}"
                                                               onclick="return confirm('Are you sure you want to cancel this booking?');"
                                                               class="btn btn-sm btn-danger">Cancel</a>
                                                        </c:if>
                                                        <c:if test="${status == 'Approved'}">
                                                            <a href="${pageContext.request.contextPath}/customer/create-payment?bookingId=${order.bookingId}" class="btn btn-sm btn-info">
                                                                Payment
                                                            </a>
                                                        </c:if>
                                                        <c:if test="${status == 'Paid'}">
                                                            <a href="${pageContext.request.contextPath}/customer/returnCar?bookingId=${order.bookingId}"
                                                               onclick="return confirm('Complete This Rental?');"
                                                               class="btn btn-sm btn-success">Return Car</a>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                            <div id="tripHistory" class="tab-content">
                                <div class="table-responsive">
                                    <table class="table">
                                        <thead>
                                        <tr>
                                            <th>Car Name</th>
                                            <th>Location</th>
                                            <th>Pick Up Date</th>
                                            <th>Return Date</th>
                                            <th>Price</th>
                                            <th>Status</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="order" items="${allBookings}">
                                            <c:set var="status" value="${order.status}"/>
                                            <%-- Đơn hàng lịch sử: Completed, Rejected, Cancelled --%>
                                            <c:set var="isHistory" value="${status == 'Completed' || status == 'Rejected' || status == 'Cancelled'}"/>

                                            <c:if test="${isHistory}">
                                                <tr>
                                                    <td><strong><c:out value="${order.carName}"/></strong></td>
                                                    <td><c:out value="${order.location}"/></td>
                                                    <td><c:out value="${order.startDate}"/> ${order.pickupTime}</td>
                                                    <td><c:out value="${order.endDate}"/> ${order.dropoffTime}</td>
                                                    <td><c:out value="${order.totalPrice}"/></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${status == 'Completed'}">
                                                                <span class="badge bg-success">Completed</span>
                                                            </c:when>
                                                            <c:when test="${status == 'Cancelled'}">
                                                                <span class="badge bg-danger">Cancelled</span>
                                                            </c:when>
                                                            <c:when test="${status == 'Rejected'}">
                                                                <span class="badge bg-danger">Rejected</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-danger">${status}</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>

    <jsp:include page="../common/customer/_footer_scripts.jsp"/>
</div>

<script>
    // ==== JS toggle tab (Giữ nguyên) ====
    document.addEventListener("DOMContentLoaded", function () {
        const tabCurrent = document.getElementById("tabCurrent");
        const tabHistory = document.getElementById("tabHistory");
        const currentTrips = document.getElementById("currentTrips");
        const tripHistory = document.getElementById("tripHistory");

        // Logic để kiểm tra active tab trên URL (nếu có) hoặc mặc định
        const urlParams = new URLSearchParams(window.location.search);
        const tabParam = urlParams.get('tab');

        // Hàm kích hoạt tab
        function activateTab(tabId) {
            const currentTripDiv = document.getElementById("currentTrips");
            const historyTripDiv = document.getElementById("tripHistory");

            if (tabId === 'history') {
                tabHistory.classList.add("active");
                tabCurrent.classList.remove("active");
                historyTripDiv.classList.add("active");
                currentTripDiv.classList.remove("active");
            } else {
                // Mặc định là current
                tabCurrent.classList.add("active");
                tabHistory.classList.remove("active");
                currentTripDiv.classList.add("active");
                historyTripDiv.classList.remove("active");
            }
        }

        // Khởi tạo trạng thái tab khi tải trang
        activateTab(tabParam);

        // Event listeners
        tabCurrent.addEventListener("click", function () {
            window.location.href = window.location.pathname + "?tab=current";
        });

        tabHistory.addEventListener("click", function () {
            window.location.href = window.location.pathname + "?tab=history";
        });
    });
</script>
</body>
</html>