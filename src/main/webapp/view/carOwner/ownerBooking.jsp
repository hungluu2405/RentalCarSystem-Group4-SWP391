<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../common/carOwner/_headOwner.jsp"/>
    <title>Car Owner Dashboard</title>
    <style>
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
            background-color: #007bff;
            color: #fff;
        }

        .tab-content {
            display: none;
        }

        .tab-content.active {
            display: block;
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
                        <div class="col-md-12 text-center"><h1>Car Owner Booking Dashboard</h1></div>
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
                                    <span class="text-gray">Total Cars</span>
                                </div>
                            </div>
                            <div class="col-lg-3 col-6 mb25">
                                <div class="card padding30 rounded-5 text-center">
                                    <div class="symbol mb40">
                                        <i class="fa id-color fa-2x fa-calendar"></i>
                                    </div>
                                    <span class="h1 mb0">${totalBookings}</span><br>
                                    <span class="text-gray">Total Bookings</span>
                                </div>
                            </div>
                            <div class="col-lg-3 col-6 mb25">
                                <div class="card padding30 rounded-5 text-center">
                                    <div class="symbol mb40">
                                        <i class="fa id-color fa-2x fa-toggle-on"></i>
                                    </div>
                                    <span class="h1 mb0">${activeBookings}</span><br>
                                    <span class="text-gray">Accepted Bookings</span>
                                </div>
                            </div>
                            <div class="col-lg-3 col-6 mb25">
                                <div class="card padding30 rounded-5 text-center">
                                    <div class="symbol mb40">
                                        <i class="fa id-color fa-2x fa-ban"></i>
                                    </div>
                                    <span class="h1 mb0">${cancelledBookings}</span><br>
                                    <span class="text-gray">Cancelled Bookings</span>
                                </div>
                            </div>
                        </div>

                        <!-- Tabs -->
                        <div class="tab-container">
                            <button class="tab-btn active" id="tabPending">Pending Orders</button>
                            <button class="tab-btn" id="tabActive">Active Orders</button>
                            <button class="tab-btn" id="tabHistory">History Orders</button>
                        </div>


                        <!-- === TAB 1: Pending Orders === -->
                        <div id="pendingOrders" class="tab-content active">
                            <div class="table-responsive">
                                <table class="table align-middle text-center">
                                    <thead class="table-light">
                                    <tr>
                                        <th>Car</th>
                                        <th>Customer</th>
                                        <th>Phone</th>
                                        <th>Pickup</th>
                                        <th>Return car</th>
                                        <th>Start</th>
                                        <th>End</th>
                                        <th>Total</th>
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="order" items="${allBookings}">
                                        <c:set var="isPending" value="${order.status == 'Pending'}"/>
                                        <c:set var="isHistory"
                                               value="${order.status == 'Paid' || order.status == 'Approved'}"/>
                                        <c:set var="isHistory"
                                               value="${order.status == 'Completed' || order.status == 'Rejected'}"/>

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
                                                                class="btn btn-success btn-sm">Accept
                                                        </button>
                                                        <button type="submit" name="action" value="reject"
                                                                class="btn btn-danger btn-sm">Reject
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
                        <div id="activeOrders" class="tab-content">
                            <div class="table-responsive">
                                <table class="table align-middle text-center">
                                    <thead class="table-light">
                                    <tr>
                                        <th>Car</th>
                                        <th>Customer</th>
                                        <th>Phone</th>
                                        <th>Pickup</th>
                                        <th>Return car</th>
                                        <th>Start</th>
                                        <th>End</th>
                                        <th>Total</th>
                                        <th>Status</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="order" items="${allBookings}">
                                        <c:if test="${order.status == 'Paid' || order.status == 'Approved'}">
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
                                                            <span class="badge bg-primary">Approved</span>
                                                        </c:when>
                                                        <c:when test="${order.status == 'Completed'}">
                                                            <span class="badge bg-success">Completed</span>
                                                        </c:when>
                                                        <c:when test="${order.status == 'Rejected'}">
                                                            <span class="badge bg-danger">Rejected</span>
                                                        </c:when>
                                                        <c:when test="${order.status == 'Paid'}">
                                                            <span class="badge bg-info text-dark">Paid</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary">${order.status}</span>
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

                        <!-- === TAB 3: Order History === -->
                        <div id="historyOrders" class="tab-content">
                            <div class="table-responsive">
                                <table class="table align-middle text-center">
                                    <thead class="table-light">
                                    <tr>
                                        <th>Car</th>
                                        <th>Customer</th>
                                        <th>Phone</th>
                                        <th>Pickup</th>
                                        <th>Return car</th>
                                        <th>Start</th>
                                        <th>End</th>
                                        <th>Total</th>
                                        <th>Status</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="order" items="${allBookings}">
                                        <c:if test="${order.status == 'Completed' || order.status == 'Rejected'}">
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
                                                            <span class="badge bg-primary">Approved</span>
                                                        </c:when>
                                                        <c:when test="${order.status == 'Completed'}">
                                                            <span class="badge bg-success">Completed</span>
                                                        </c:when>
                                                        <c:when test="${order.status == 'Rejected'}">
                                                            <span class="badge bg-danger">Rejected</span>
                                                        </c:when>
                                                        <c:when test="${order.status == 'Paid'}">
                                                            <span class="badge bg-info text-dark">Paid</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary">${order.status}</span>
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
        </section>

        <!-- FOOTER -->
        <jsp:include page="../common/carOwner/_footer_scriptsOwner.jsp"/>

        <!-- SCRIPT: Tab Switching -->
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const tabPending = document.getElementById("tabPending");
                const tabActive = document.getElementById("tabActive");
                const tabHistory = document.getElementById("tabHistory");

                const pendingOrders = document.getElementById("pendingOrders");
                const activeOrders = document.getElementById("activeOrders");
                const historyOrders = document.getElementById("historyOrders");

                // Tab 1: Pending
                tabPending.addEventListener("click", function () {
                    tabPending.classList.add("active");
                    tabActive.classList.remove("active");
                    tabHistory.classList.remove("active");

                    pendingOrders.classList.add("active");
                    activeOrders.classList.remove("active");
                    historyOrders.classList.remove("active");
                });

                // Tab 2: Active
                tabActive.addEventListener("click", function () {
                    tabActive.classList.add("active");
                    tabPending.classList.remove("active");
                    tabHistory.classList.remove("active");

                    activeOrders.classList.add("active");
                    pendingOrders.classList.remove("active");
                    historyOrders.classList.remove("active");
                });

                // Tab 3: History
                tabHistory.addEventListener("click", function () {
                    tabHistory.classList.add("active");
                    tabPending.classList.remove("active");
                    tabActive.classList.remove("active");

                    historyOrders.classList.add("active");
                    pendingOrders.classList.remove("active");
                    activeOrders.classList.remove("active");
                });
            });
        </script>


    </div>
</body>
</html>


