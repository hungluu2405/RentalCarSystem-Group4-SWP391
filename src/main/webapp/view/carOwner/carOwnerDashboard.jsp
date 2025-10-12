<%--
  Created by IntelliJ IDEA.
  User: tunge
  Date: 10/9/2025
  Time: 11:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../common/carOwner/_headOwner.jsp"/>
    <title>Car Owner Dashboard</title>
</head>

<body>
<div id="wrapper">
    <div id="de-preloader"></div>
    <jsp:include page="../common/carOwner/_headerOwner.jsp"/>
    <!-- MAIN WRAPPER -->
    <%--    <div class="container-fluid py-5">--%>
    <%--        <div class="container">--%>

    <%--            <!-- SIDEBAR -->--%>
    <%--            <div class="row">--%>

    <div class="no-bottom no-top zebra" id="content">
        <div id="top"></div>
        <section id="subheader" class="jarallax text-light">
            <img src="${pageContext.request.contextPath}/images/background/14.jpg" class="jarallax-img"
                 alt="">
            <div class="center-y relative text-center">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center"><h1>Car Owner Dashboard</h1></div>
                    </div>
                </div>
            </div>
        </section>

        <section id="section-cars" class="bg-gray-100">
            <div class="container">
                <div class="row">
                    <%-- Sidebar --%>
                    <div class="col-lg-3 mb30">
                        <%-- Sửa lại đuôi file thành .jspf --%>
                        <jsp:include page="../common/carOwner/_sidebarOwner.jsp">
                            <jsp:param name="activePage" value="dashboard"/>
                        </jsp:include>
                    </div>

                    <!-- MAIN CONTENT -->
                    <div class="col-lg-9">
                        <div class="row">
                            <!-- STATISTICS -->

                            <div class="col-lg-3 col-6 mb25">
                                <div class="card padding30 rounded-5">
                                    <div class="symbol mb40">
                                        <i class="fa id-color fa-2x fa-car"></i>
                                    </div>
                                    <span class="h1 mb0">${totalCars}</span><span class="text-gray">Total Cars</span>
                                </div>
                            </div>

                            <div class="col-lg-3 col-6 mb25">
                                <div class="card padding30 rounded-5">
                                    <div class="symbol mb40">
                                        <i class="fa id-color fa-2x fa-calendar"></i>
                                    </div>
                                    <span class="h1 mb0">${totalBookings}</span><span class="text-gray">Total Bookings</span>
                                </div>
                            </div>

                            <div class="col-lg-3 col-6 mb25">
                                <div class="card padding30 rounded-5">
                                    <div class="symbol mb40">
                                        <i class="fa id-color fa-2x fa-toggle-on"></i>
                                    </div>
                                    <span class="h1 mb0">${activeBookings}</span><span class="text-gray">Active booking</span>
                                </div>
                            </div>

                            <div class="col-lg-3 col-6 mb25">
                                <div class="card padding30 rounded-5">
                                    <div class="symbol mb40">
                                        <i class="fa id-color fa-2x fa-ban"></i>
                                    </div>
                                    <span class="h1 mb0">${cancelledBookings}</span><span class="text-gray">Cancel booking</span>
                                </div>
                            </div>
                        </div>

                        <!-- RECENT BOOKINGS -->
                        <h5 class="fw-bold mb-3 text-secondary">My Recent Bookings</h5>
                        <div class="table-responsive">
                            <table class="table table-bordered align-middle text-center">
                                <thead class="table-light">
                                <tr>
                                    <th>Booking ID</th>
                                    <th>Image</th>
                                    <th>Car's name</th>
                                    <th>Customer</th>
                                    <th>Pick Up</th>
                                    <th>Drop Off</th>
                                    <th>Start Date</th>
                                    <th>End Date</th>
                                    <th>Price</th>
                                    <th>Status</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${not empty recentBookings}">
                                        <c:forEach var="b" items="${recentBookings}">
                                            <tr>
                                                <td>#${b.bookingId}</td>
                                                <td>
                                                    <img src="${b.car.imageUrl}" alt="${b.car.name}"
                                                         class="car-image"
                                                         style="width: 80px; height: 50px; object-fit: cover; border-radius: 6px;"/>
                                                </td>
                                                <td>${b.carName}</td>
                                                <td>${b.customerName}</td>
                                                <td>${b.pickUpLocation}</td>
                                                <td>${b.dropOffLocation}</td>
                                                <td>${b.startDate}</td>
                                                <td>${b.endDate}</td>
                                                <td>${b.price}</td>
                                                <td>
                                                    <div class="badge rounded-pill
                                                            ${b.status eq 'Completed' ? 'bg-success' : 
                                                              b.status eq 'Cancelled' ? 'bg-danger' : 'bg-warning'}">
                                                            ${b.status}
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="10" class="text-muted">No recent bookings found.</td>
                                        </tr>
                                    </c:otherwise>

                                </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <%--    </div> --%>
        </section>
    </div>

    <!-- FOOTER -->
    <jsp:include page="../common/carOwner/_footer_scriptsOwner.jsp"/>

</body>
</html>

