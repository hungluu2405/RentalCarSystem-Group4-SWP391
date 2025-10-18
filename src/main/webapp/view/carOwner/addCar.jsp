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

                        <!-- ADD CAR -->
                            <form action="${pageContext.request.contextPath}/owner/addCar" method="post" enctype="multipart/form-data" class="p-4 bg-white rounded shadow-sm">
                                <div class="text-center mb-4">
                                    <img id="previewImage" src="${pageContext.request.contextPath}/images/default-car.png"
                                         alt="Car Preview" class="img-fluid rounded shadow-sm" style="max-width: 300px;">
                                    <div class="mt-3">
                                        <label for="carImage" class="form-label fw-bold">Upload Car Image</label>
                                        <input type="file" id="carImage" name="carImage" accept="image/*" class="form-control" required
                                               onchange="previewFile(this)">
                                    </div>
                                </div>

                                <div class="row g-3">
                                    <!-- BRAND -->
                                    <div class="col-md-6">
                                        <label class="form-label"><i class="fa fa-industry text-primary"></i> Brand</label>
                                        <input type="text" name="brand" class="form-control" placeholder="Enter car brand..." required>
                                    </div>

                                    <!-- TRANSMISSION -->
                                    <div class="col-md-6">
                                        <label class="form-label"><i class="fa fa-cogs text-primary"></i> Transmission</label>
                                        <select name="transmission" class="form-select" required>
                                            <option value="">Select transmission...</option>
                                            <c:forEach var="t" items="${transmissions}">
                                                <option value="${t}">${t}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <!-- MODEL -->
                                    <div class="col-md-6">
                                        <label class="form-label"><i class="fa fa-car text-primary"></i> Model</label>
                                        <input type="text" name="model" class="form-control" placeholder="Enter car model..." required>
                                    </div>

                                    <!-- FUEL TYPE -->
                                    <div class="col-md-6">
                                        <label class="form-label"><i class="fa fa-gas-pump text-primary"></i> Fuel Type</label>
                                        <select name="fuelType" class="form-select" required>
                                            <option value="">Select fuel type...</option>
                                            <c:forEach var="f" items="${fuelTypes}">
                                                <option value="${f}">${f}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <!-- YEAR -->
                                    <div class="col-md-6">
                                        <label class="form-label"><i class="fa fa-calendar text-primary"></i> Year</label>
                                        <input type="number" name="year" class="form-control" placeholder="Enter manufacturing year..." required>
                                    </div>

                                    <!-- PRICE -->
                                    <div class="col-md-6">
                                        <label class="form-label"><i class="fa fa-dollar-sign text-primary"></i> Price per Day</label>
                                        <input type="number" step="0.01" name="pricePerDay" class="form-control" placeholder="Enter price per day..." required>
                                    </div>

                                    <!-- CAPACITY -->
                                    <div class="col-md-6">
                                        <label class="form-label"><i class="fa fa-users text-primary"></i> Capacity</label>
                                        <input type="number" name="capacity" class="form-control" placeholder="Enter car capacity..." required>
                                    </div>

                                    <!-- LICENSE PLATE -->
                                    <div class="col-md-6">
                                        <label class="form-label"><i class="fa fa-id-card text-primary"></i> License Plate</label>
                                        <input type="text" name="licensePlate" class="form-control" placeholder="Enter license plate..." required>
                                    </div>

                                    <!-- CAR TYPE -->
                                    <div class="col-md-6">
                                        <label class="form-label"><i class="fa fa-tags text-primary"></i> Car Type</label>
                                        <select name="typeId" class="form-select" required>
                                            <option value="">Select car type...</option>
                                            <c:forEach var="c" items="${carTypes}">
                                                <option value="${c.typeId}">${c.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <!-- LOCATION -->
                                    <div class="col-md-6">
                                        <label class="form-label"><i class="fa fa-map-marker-alt text-primary"></i> Location</label>
                                        <input type="text" name="location" class="form-control" placeholder="Enter car location...">
                                    </div>

                                    <!-- DESCRIPTION -->
                                    <div class="col-12">
                                        <label class="form-label"><i class="fa fa-align-left text-primary"></i> Description</label>
                                        <textarea name="description" class="form-control" rows="4" placeholder="Enter description about the car..."></textarea>
                                    </div>

                                    <!-- SUBMIT BUTTON -->
                                    <div class="col-12 text-center mt-4">
                                        <button type="submit" class="btn btn-primary px-4 py-2">
                                            <i class="fa fa-plus-circle"></i> Add Car
                                        </button>
                                    </div>
                                </div>
                            </form>


                        <script>
                            // Hàm hiển thị ảnh xem trước khi chọn file
                            function previewFile(input) {
                                const file = input.files[0];
                                const preview = document.getElementById('previewImage');
                                if (file) {
                                    const reader = new FileReader();
                                    reader.onload = function (e) {
                                        preview.src = e.target.result;
                                    };
                                    reader.readAsDataURL(file);
                                }
                            }
                        </script>


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
