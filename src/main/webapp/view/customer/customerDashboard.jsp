<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zxx">
<head>
    <%-- Sửa lại đuôi file thành .jspf --%>
    <jsp:include page="../common/customer/_head.jsp"/>
</head>

<body>
<div id="wrapper">
    <div id="de-preloader"></div>

    <%-- Sửa lại đuôi file thành .jspf --%>
    <jsp:include page="../common/customer/_header.jsp"/>

    <div class="no-bottom no-top zebra" id="content">
        <div id="top"></div>
        <section id="subheader" class="jarallax text-light">
            <img src="${pageContext.request.contextPath}/images/background/14.jpg" class="jarallax-img" alt="">
            <div class="center-y relative text-center">
                <div class="container"><div class="row"><div class="col-md-12 text-center"><h1>Dashboard</h1></div></div></div>
            </div>
        </section>

        <section id="section-cars" class="bg-gray-100">
            <div class="container">
                <div class="row">
                    <%-- Sidebar --%>
                    <div class="col-lg-3 mb30">
                        <%-- Sửa lại đuôi file thành .jspf --%>
                        <jsp:include page="../common/customer/_sidebar.jsp">
                            <jsp:param name="activePage" value="dashboard"/>
                        </jsp:include>
                    </div>

                    <%-- Main Content --%>
                    <div class="col-lg-9">
                        <div class="row">
                            <div class="col-lg-4 col-6 mb25">
                                <div class="card padding30 rounded-5"><div class="symbol mb40"><i class="fa id-color fa-2x fa-calendar-check-o"></i></div><span class="h1 mb0">${upcoming}</span><span class="text-gray">Upcoming Orders</span></div>
                            </div>
                            <div class="col-lg-4 col-6 mb25">
                                <div class="card padding30 rounded-5"><div class="symbol mb40"><i class="fa id-color fa-2x fa-calendar"></i></div><span class="h1 mb0">${total}</span><span class="text-gray">Total Orders</span></div>
                            </div>
                            <div class="col-lg-4 col-6 mb25">
                                <div class="card padding30 rounded-5"><div class="symbol mb40"><i class="fa id-color fa-2x fa-calendar-times-o"></i></div><span class="h1 mb0">${cancelled}</span><span class="text-gray">Cancel Orders</span></div>
                            </div>
                        </div>

                        <div class="card padding30 rounded-5 mb25">
                            <h4>My Recent Orders</h4>
                            <div class="table-responsive">
                                <table class="table de-table">
                                    <thead>
                                    <tr>
<th scope="col"><span class="fs-12 text-gray">Order ID</span></th>
                                        <th scope="col"><span class="fs-12 text-gray">Car Name</span></th>
                                        <th scope="col"><span class="fs-12 text-gray">Pick Up Location</span></th>
                                        <th scope="col"><span class="fs-12 text-gray">Drop Off Location</span></th>
                                        <th scope="col"><span class="fs-12 text-gray">Pick Up Date</span></th>
                                        <th scope="col"><span class="fs-12 text-gray">Return Date</span></th>
                                        <th scope="col"><span class="fs-12 text-gray">Status</span></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:choose>
                                        <c:when test="${not empty recentBookings}">
                                            <c:forEach var="booking" items="${recentBookings}">
                                                <tr>
                                                    <td><div class="badge bg-gray-100 text-dark">#${booking.bookingId}</div></td>
                                                    <td><span class="bold">${booking.carName}</span></td>
                                                    <td>${booking.pickUpLocation}</td>
                                                    <td>${booking.dropOffLocation}</td>
                                                    <td>${booking.startDate}</td>
                                                    <td>${booking.endDate}</td>
                                                    <td><div class="badge rounded-pill bg-warning">${booking.status}</div></td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="7" class="text-center">No recent orders found.</td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <div class="card padding30 rounded-5">
                            <h4>My Favorites</h4>
                            <div class="spacer-10"></div>
                            <div class="de-item-list no-border mb30">
                                <div class="d-img">
                                    <img src="${pageContext.request.contextPath}/images/cars/jeep-renegade.jpg" class="img-fluid" alt="">
                                </div>
<div class="d-info">
                                    <div class="d-text">
                                        <h4>Jeep Renegade</h4>
                                    </div>
                                </div>
                                <div class="d-price">
                                    Daily rate from <span>$265</span>
                                    <a class="btn-main" href="#">Rent Now</a>
                                </div>
                                <div class="clearfix"></div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </section>
    </div>

    <%-- Sửa lại đuôi file thành .jspf --%>
    <jsp:include page="../common/customer/_footer_scripts.jsp"/>

</div>
</body>
</html>