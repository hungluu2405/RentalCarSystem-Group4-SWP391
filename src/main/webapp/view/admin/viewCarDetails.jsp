<%--
  Created by IntelliJ IDEA.
  User: tunge
  Date: 10/23/2025
  Time: 11:00 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../common/carOwner/_headOwner.jsp"/>
    <title>Manage Car Detail</title>
    <style>
        .car-form {
            max-width: 800px;
            margin: 40px auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 12px;
            background: #fff;
        }
        .car-form input, .car-form textarea, .car-form select {
            width: 100%;
            padding: 10px;
            border-radius: 8px;
            border: 1px solid #ccc;
            margin-bottom: 15px;
            font-size: 15px;
        }
        .car-actions {
            display: flex;
            justify-content: space-between;
        }
        .btn-update {
            background-color: #007bff;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 8px;
            cursor: pointer;
        }
        .btn-delete {
            background-color: #dc3545;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 8px;
            cursor: pointer;
        }
        .car-image {
            width: 100%;
            max-height: 300px;
            object-fit: cover;
            border-radius: 10px;
            margin-bottom: 15px;
        }
    </style>
</head>

<body>
<div id="wrapper">
    <div id="de-preloader"></div>

    <!-- HEADER -->
    <jsp:include page="../common/carOwner/_headerOwner.jsp"/>

    <div class="no-bottom no-top zebra" id="content">
        <div id="top"></div>

        <!-- TITLE SECTION -->
        <section id="subheader" class="jarallax text-light">
            <img src="${pageContext.request.contextPath}/images/background/14.jpg" class="jarallax-img" alt="">
            <div class="center-y relative text-center">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center"><h1>Manage My Car Detail</h1></div>
                    </div>
                </div>
            </div>
        </section>

        <section id="section-cars" class="bg-gray-100">
            <div class="container">
                <div class="row">





                    <!-- MAIN CONTENT -->
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger text-center mb-3" role="alert">
                                ${error}
                        </div>
                    </c:if>
                        <div class="card padding40 rounded-5 shadow-sm">
                            <h3 class="mb-4"><i class="fa fa-car"></i>   Car Information</h3>

                            <form method="post"
                                  action="${pageContext.request.contextPath}/owner/manageCarDetail"
                                  enctype="multipart/form-data"
                                  class="p-4 bg-white rounded shadow-sm">

                                <input type="hidden" name="carId" value="${car.carId}">

                                <input type="hidden" name="oldImageUrl" value="${car.imageUrl}">

                                <!-- IMAGE -->
                                <div class="text-center mb-4">
                                    <img id="carPreview"
                                         src="${pageContext.request.contextPath}/${car.imageUrl}"
                                         alt="Car Image"
                                         style="max-width: 100%; max-height: 300px; border-radius: 12px; object-fit: contain; box-shadow: 0 2px 6px rgba(0,0,0,0.15);">
                                    <p class="fw-bold mt-3">Car Image</p>

                                </div>

                                <script>
                                    function previewCarImage(event) {
                                        const preview = document.getElementById('carPreview');
                                        const file = event.target.files[0];
                                        preview.src = file ? URL.createObjectURL(file)
                                            : "${pageContext.request.contextPath}/${car.imageUrl}";
                                    }
                                </script>

                                <!-- GRID LAYOUT -->
                                <div class="row">
                                    <!-- Brand -->
                                    <div class="col-md-6 mb-3">
                                        <label>Brand</label>
                                        <input type="text" name="brand" value="${car.brand}" class="form-control" required>
                                    </div>

                                    <!-- Model -->
                                    <div class="col-md-6 mb-3">
                                        <label>Model</label>
                                        <input type="text" name="model" value="${car.model}" class="form-control" required>
                                    </div>

                                    <!-- Fuel Type -->
                                    <div class="col-md-6 mb-3">
                                        <label>Fuel Type</label>
                                        <input type="text" name="model" value="${car.fuelType}" class="form-control" required>
                                    </div>

                                    <!-- Transmission -->
                                    <div class="col-md-6 mb-3">
                                        <label>Transmission</label>
                                        <input type="text" name="model" value="${car.transmission}" class="form-control" required>
                                    </div>

                                    <!-- Car Type -->
                                    <div class="col-md-6 mb-3">
                                        <label>Car Type</label>
                                        <input type="text" name="model" value="${car.carTypeName}" class="form-control" required>

                                    </div>

                                    <!-- Capacity -->
                                    <div class="col-md-3 mb-3">
                                        <label>Capacity</label>
                                        <input type="number" name="capacity" value="${car.capacity}" class="form-control" required>
                                    </div>

                                    <!-- Price -->
                                    <div class="col-md-3 mb-3">
                                        <label>Price Per Day</label>
                                        <input type="number" step="0.01" name="pricePerDay" value="${car.pricePerDay}" class="form-control" required>
                                    </div>

                                    <!-- Location -->
                                    <div class="col-md-6 mb-3">
                                        <label>Location</label>
                                        <input type="text" name="location" value="${car.location}" class="form-control" required>
                                    </div>

                                    <!-- Year -->
                                    <div class="col-md-3 mb-3">
                                        <label>Year</label>
                                        <input type="number" name="year" value="${car.year}" class="form-control" required>
                                    </div>

                                    <!-- LicensePlate -->
                                    <div class="col-md-3 mb-3">
                                        <label>License Plate</label>
                                        <input type="text" name="licensePlate" value="${car.licensePlate}" class="form-control" required>
                                    </div>

                                    <!-- Availability -->
                                    <div class="form-check form-switch">
                                        <input type="checkbox"
                                               class="form-check-input"
                                               id="availabilitySwitch"
                                               name="availability"
                                               value="1"
                                               <c:if test="${car.availability == 1}">checked</c:if>>
                                        <label class="form-check-label" for="availabilitySwitch">
                                            <span id="availabilityLabel">
                                              <c:choose>
                                                  <c:when test="${car.availability == 1}">Available</c:when>
                                                  <c:otherwise>Not Available</c:otherwise>
                                              </c:choose>
                                            </span>
                                        </label>
                                    </div>

                                    <script>
                                        // Thay đổi label khi gạt công tắc
                                        const toggle = document.getElementById('availabilitySwitch');
                                        const label = document.getElementById('availabilityLabel');
                                        toggle.addEventListener('change', function () {
                                            label.textContent = this.checked ? 'Available' : 'Not Available';
                                        });
                                    </script>
                                    <input type="hidden" name="availability" value="0">
                                    <input type="checkbox"
                                           class="form-check-input"
                                           id="availabilitySwitch"
                                           name="availability"
                                           value="1"
                                           <c:if test="${car.availability == 1}">checked</c:if>>


                                    <!-- Description -->
                                    <div class="col-md-12 mb-3">
                                        <label>Description</label>
                                        <textarea name="description" rows="4" class="form-control">${car.description}</textarea>
                                    </div>
                                </div>
                            </form>
                                <!-- BUTTONS -->
                            <form method="post" action="${pageContext.request.contextPath}/admin/manageCar" onsubmit="return confirm('Are you sure you want to delete this car ?');">
                                <input type="hidden" name="deleteId" value="${car.carId}" />
                                <button type="submit" class="btn btn-danger">Delete</button>
                                <a href="${pageContext.request.contextPath}/carDB" class="btn btn-secondary">Back to list</a>
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

