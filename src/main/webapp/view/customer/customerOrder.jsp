<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../common/customer/_head.jsp"/>
    <style>
        /* ==== Tabs ==== */
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

        .bg-warning {
            background-color: #FFD54F;
        }

        .bg-success {
            background-color: #66BB6A;
            color: white;
        }
    </style>
</head>

<body>
<div id="wrapper">

    <%-- Header --%>
    <jsp:include page="../common/customer/_header.jsp"/>

    <div class="no-bottom no-top zebra" id="content">
        <%-- === PHẦN DASHBOARD HEADER (ảnh nền + chữ “Dashboard”) === --%>
        <section id="subheader" class="jarallax text-light">
            <img src="${pageContext.request.contextPath}/images/background/14.jpg" class="jarallax-img" alt="">
            <div class="center-y relative text-center">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center">
                            <h1>Dashboard</h1>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <%-- === PHẦN CHÍNH === --%>
        <section id="section-cars" class="bg-gray-100">
            <div class="container">
                <div class="row">

                    <%-- Sidebar bên trái --%>
                    <div class="col-lg-3 mb30">
                        <jsp:include page="../common/customer/_sidebar.jsp">
                            <jsp:param name="activePage" value="orders"/>
                        </jsp:include>
                    </div>

                    <%-- Nội dung bên phải --%>
                    <div class="col-lg-9">

                        <%-- Thống kê các đơn hàng --%>
                        <div class="row mb25">
                            <div class="col-lg-4 col-6 mb25">
                                <div class="card padding30 text-center rounded-5">
                                    <div class="symbol mb10">
                                        <i class="fa fa-calendar-check-o fa-2x text-success"></i>
                                    </div>
                                    <span class="h2 mb0">${upcoming}</span><br>
                                    <span>Upcoming Orders</span>
                                </div>
                            </div>

                            <div class="col-lg-4 col-6 mb25">
                                <div class="card padding30 text-center rounded-5">
                                    <div class="symbol mb10">
                                        <i class="fa fa-calendar fa-2x text-success"></i>
                                    </div>
                                    <span class="h2 mb0">${total}</span><br>
                                    <span>Total Orders</span>
                                </div>
                            </div>

                            <div class="col-lg-4 col-6 mb25">
                                <div class="card padding30 text-center rounded-5">
                                    <div class="symbol mb10">
                                        <i class="fa fa-calendar-times-o fa-2x text-danger"></i>
                                    </div>
                                    <span class="h2 mb0">${cancelled}</span><br>
                                    <span>Cancel Orders</span>
                                </div>
                            </div>
                        </div>

                        <%-- ==== My Orders + Tabs ==== --%>
                        <div class="card padding30 rounded-5 mb25">
                            <h4>My Orders</h4>

                            <!-- Tabs -->
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
                                            <th>Actions</th> <%-- Thêm cột hành động --%>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <%-- Vòng lặp JSTL --%>
                                        <c:forEach var="order" items="${allBookings}">
                                            <c:set var="isCurrent" value="${order.status == 'Pending' || order.status == 'Approved'}"/>
                                            <c:set var="isHistory" value="${order.status == 'Completed' || order.status == 'Rejected' || order.status == 'Cancelled'}"/>

                                            <%-- Lọc đơn hàng hiện tại (Current Trips) --%>
                                            <c:if test="${isCurrent}">
                                                <tr>
                                                    <td><strong><c:out value="${order.carName}"/></strong></td>
                                                    <td><c:out value="${order.location}"/></td>
                                                    <td><c:out value="${order.startDate}"/> ${order.pickupTime}</td>
                                                    <td><c:out value="${order.endDate}"/> ${order.dropoffTime}</td>
                                                    <td><c:out value="${order.totalPrice}"/></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${order.status == 'Pending'}">
                                                                <span class="badge bg-warning text-dark">Pending</span>
                                                            </c:when>
                                                            <c:when test="${order.status == 'Approved'}">
                                                                <span class="badge bg-primary text-white">Approved</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-info text-dark">${order.status}</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:if test="${order.status == 'Pending'}">
                                                            <a href="${pageContext.request.contextPath}/customer/cancelBooking?bookingId=${order.bookingId}"
                                                               onclick="return confirm('Are you sure you want to cancel this booking?');"
                                                               class="btn btn-sm btn-danger">Cancel</a>
                                                        </c:if>
                                                        <c:if test="${order.status == 'Approved'}">
                                                            <a href="#" class="btn btn-sm btn-info">Payment</a>
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
                                        <%-- Vòng lặp JSTL --%>
                                        <c:forEach var="order" items="${allBookings}">
                                            <%-- Lọc đơn hàng lịch sử (Trip History) --%>
                                            <c:if test="${order.status == 'Completed' || order.status == 'Rejected' || order.status == 'Cancelled'}">
                                                <tr>

                                                    <td><strong><c:out value="${order.carName}"/></strong></td>

                                                    <td><c:out value="${order.location}"/></td>
                                                    <td><c:out value="${order.startDate}"/> ${order.pickupTime}</td>
                                                    <td><c:out value="${order.endDate}"/> ${order.dropoffTime}</td>
                                                    <td><c:out value="${order.totalPrice}"/></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${order.status == 'Completed'}">
                                                                <span class="badge bg-success">Completed</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-danger text-white">${order.status}</span>
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



                    </div> <%-- end col-lg-9 --%>
                </div>
            </div>
        </section>
    </div>

    <%-- Footer scripts --%>
    <jsp:include page="../common/customer/_footer_scripts.jsp"/>

</div>

<script>
    // ==== JS toggle tab ====
    document.addEventListener("DOMContentLoaded", function () {
        const tabCurrent = document.getElementById("tabCurrent");
        const tabHistory = document.getElementById("tabHistory");
        const currentTrips = document.getElementById("currentTrips");
        const tripHistory = document.getElementById("tripHistory");

        tabCurrent.addEventListener("click", function () {
            tabCurrent.classList.add("active");
            tabHistory.classList.remove("active");
            currentTrips.classList.add("active");
            tripHistory.classList.remove("active");
        });

        tabHistory.addEventListener("click", function () {
            tabHistory.classList.add("active");
            tabCurrent.classList.remove("active");
            tripHistory.classList.add("active");
            currentTrips.classList.remove("active");
        });
    });
</script>

</body>
</html>
