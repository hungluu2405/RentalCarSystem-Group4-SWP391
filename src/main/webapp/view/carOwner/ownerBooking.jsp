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
                                    <span class="text-gray">Active Bookings</span>
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

                        <!-- PENDING BOOKINGS -->
                        <h5 class="fw-bold mt-5 mb-3 text-secondary">Pending Booking Requests</h5>
                        <div class="table-responsive mb-5">
                            <table class="table table-bordered align-middle text-center">
                                <thead class="table-light">
                                <tr>
                                    <th>Booking ID</th>
                                    <th>Car Name</th>
                                    <th>Customer</th>
                                    <th>Phone</th>
                                    <th>Location</th>
                                    <th>Pickup Time</th>
                                    <th>Dropoff Time</th>
                                    <th>Start Date</th>
                                    <th>End Date</th>
                                    <th>Total Price ($)</th>
                                    <th>Status</th>
                                    <th>Action</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${not empty pendingBookings}">
                                        <c:forEach var="b" items="${pendingBookings}">
                                            <tr>
                                                <td>#${b.bookingId}</td>
                                                <td>${b.carName}</td>
                                                <td>${b.customerProfile.fullName}</td>
                                                <td>${b.customerProfile.phone}</td>
                                                <td>${b.location}</td>
                                                <td>${b.pickupTime}</td>
                                                <td>${b.dropoffTime}</td>
                                                <td>${b.startDate}</td>
                                                <td>${b.endDate}</td>
                                                <td>${b.totalPrice}</td>
                                                <td>
                                                    <div class="badge
                                                    ${b.status eq 'Pending' ? 'bg-warning text-dark' :
                                                      b.status eq 'Accepted' ? 'bg-success' :
                                                      b.status eq 'Rejected' ? 'bg-danger' :
                                                      b.status eq 'Changed' ? 'bg-info text-dark' :
                                                      'bg-secondary'}">
                                                            ${b.status}
                                                    </div>
                                                </td>

                                                <!-- ACTION BUTTONS -->
                                                <td class="action-cell">
                                                    <c:choose>
                                                        <%-- Pending: Accept / Reject --%>
                                                        <c:when test="${b.status eq 'Pending'}">
                                                            <form method="post" action="${pageContext.request.contextPath}/owner/ownerBooking"
                                                                  class="d-flex justify-content-center gap-2">
                                                                <input type="hidden" name="bookingId" value="${b.bookingId}">
                                                                <button type="submit" name="action" value="accept" class="btn btn-success btn-sm">Accept</button>
                                                                <button type="submit" name="action" value="reject" class="btn btn-danger btn-sm">Reject</button>
                                                            </form>
                                                        </c:when>

                                                        <%-- Accepted or Rejected: show Changed --%>
                                                        <c:when test="${b.status eq 'Accepted' || b.status eq 'Rejected'}">
                                                            <form method="post" action="${pageContext.request.contextPath}/owner/ownerBooking"
                                                                  class="d-flex justify-content-center">
                                                                <input type="hidden" name="bookingId" value="${b.bookingId}">
                                                                <button type="submit" name="action" value="changed" class="btn btn-info btn-sm">Changed</button>
                                                            </form>
                                                        </c:when>

                                                        <%-- Changed: show Change action again --%>
                                                        <c:when test="${b.status eq 'Changed'}">
                                                            <button class="btn btn-primary btn-sm change-btn" data-id="${b.bookingId}">
                                                                Change action again
                                                            </button>
                                                        </c:when>

                                                        <c:otherwise>
                                                            <span class="text-muted">N/A</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>


                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="12" class="text-muted">No pending booking requests.</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>



                    </div>
                </div>
            </div>
        </section>
    </div>

    <!-- FOOTER -->
    <jsp:include page="../common/carOwner/_footer_scriptsOwner.jsp"/>
</div>
<!-- ========== SCRIPT CHO NÚT CHANGE ACTION AGAIN ========== -->
<script>
    document.addEventListener("DOMContentLoaded", function() {
        document.querySelectorAll(".change-btn").forEach(btn => {
            btn.addEventListener("click", function(e) {
                e.preventDefault();
                const row = btn.closest("tr");
                const actionCell = row.querySelector(".action-cell");

                // Hiển lại 2 nút Accept / Reject
                actionCell.innerHTML = `
                <form method="post" action="${pageContext.request.contextPath}/owner/ownerBooking"
                      class="d-flex justify-content-center gap-2">
                    <input type="hidden" name="bookingId" value="${btn.dataset.id}">
                    <button type="submit" name="action" value="accept" class="btn btn-success btn-sm">Accept</button>
                    <button type="submit" name="action" value="reject" class="btn btn-danger btn-sm">Reject</button>
                </form>
            `;
            });
        });
    });
</script>

</body>
</html>
