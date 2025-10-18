<%--
  Created by IntelliJ IDEA.
  User: tunge
  Date: 10/18/2025
  Time: 4:46 PM
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
                            <jsp:param name="activePage" value="addCar"/>
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

                        <!-- ADD CAR -->

                        <form action="${pageContext.request.contextPath}/owner/addCar" method="post" enctype="multipart/form-data">
                            <label>Brand:</label>
                            <input type="text" name="brand" required>

                            <label>Model:</label>
                            <input type="text" name="model" required>

                            <label>Year:</label>
                            <input type="number" name="year" required>

                            <label>License Plate:</label>
                            <input type="text" name="licensePlate" required>

                            <label>Capacity:</label>
                            <input type="number" name="capacity" required>

                            <label>Transmission:</label>
                            <input type="text" name="transmission" required>

                            <label>Fuel Type:</label>
                            <input type="text" name="fuelType" required>

                            <label>Price per Day:</label>
                            <input type="number" step="0.01" name="pricePerDay" required>

                            <label>Description:</label>
                            <textarea name="description"></textarea>

                            <label>Location:</label>
                            <input type="text" name="location">

                            <label>Car Type:</label>
                            <select name="typeId">
                                <c:forEach var="t" items="${carTypes}">
                                    <option value="${t.typeId}">${t.name}</option>
                                </c:forEach>
                            </select>

                            <label>Car Image:</label>
                            <input type="file" name="carImage" accept="image/*" required>

                            <button type="submit">Add Car</button>
                        </form>

                    </div>
                </div>
            </div>
        </section>
    </div>

    <!-- FOOTER -->
    <jsp:include page="../common/carOwner/_footer_scriptsOwner.jsp"/>
</div>

</body>
</html>
