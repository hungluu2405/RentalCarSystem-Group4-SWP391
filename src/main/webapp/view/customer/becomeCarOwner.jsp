<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../common/carOwner/_headOwner.jsp"/>
    <title>Become Car Owner</title>
    <style>
        body {
            background-color: #f4f6f8;
        }

        .add-car-section {
            background: #fff;
            border-radius: 16px;
            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
            padding: 40px 50px;
            margin: 40px auto;
            max-width: 1100px;
        }

        .add-car-header {
            text-align: center;
            font-weight: 700;
            color: #2c3e50;
            font-size: 28px;
            margin-bottom: 30px;
        }

        .upload-section {
            text-align: center;
            border: 2px dashed #cfd8dc;
            border-radius: 12px;
            padding: 20px;
            transition: all 0.3s ease;
        }

        .upload-section:hover {
            border-color: #007bff;
            background: #f0f8ff;
        }

        .upload-section img {
            width: 100%;
            max-width: 280px;
            border-radius: 12px;
            margin-bottom: 15px;
            object-fit: cover;
        }

        .form-control, .form-select {
            border-radius: 10px;
            border: 1px solid #d0d0d0;
            transition: 0.3s ease;
        }

        .form-control:focus, .form-select:focus {
            border-color: #007bff;
            box-shadow: 0 0 6px rgba(0, 123, 255, 0.3);
        }

        .btn-submit {
            background: linear-gradient(90deg, #007bff, #00bfff);
            color: white;
            font-weight: 600;
            border-radius: 12px;
            border: none;
            padding: 12px 40px;
            transition: all 0.3s ease;
        }

        .btn-submit:hover {
            background: linear-gradient(90deg, #0062cc, #0099cc);
            transform: translateY(-2px);
        }
    </style>
</head>

<body>
<div id="wrapper">
    <jsp:include page="../common/carOwner/_headerOwner.jsp"/>

    <div id="content" class="no-bottom no-top zebra">
        <div id="top"></div>

        <!-- Banner -->
        <section id="subheader" class="jarallax text-light">
            <img src="${pageContext.request.contextPath}/images/background/14.jpg" class="jarallax-img" alt="">
            <div class="center-y relative text-center">
                <div class="container">
                    <h1>Become Car Owner</h1>
                </div>
            </div>
        </section>

        <!-- FORM -->
        <section id="section-addcar">
            <div class="container">
                <div class="add-car-section">
                    <h2 class="add-car-header">Be our partner to get more offer!</h2>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger text-center mb-3">${errorMessage}</div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/customer/becomeCarOwner"
                          method="post" enctype="multipart/form-data">
                        <div class="row">
                            <!-- Upload ảnh -->
                            <div class="col-md-5">
                                <div class="upload-section">
                                    <img id="previewImage"
                                         src="${pageContext.request.contextPath}/images/default-car.png"
                                         alt="Car Preview">
                                    <h6>Upload Car Image</h6>
                                    <input type="file" name="carImage" id="carImage" class="form-control mt-2"
                                           accept="image/*"
                                           onchange="previewFile(event)" required>
                                </div>
                            </div>

                            <!-- Thông tin xe -->
                            <div class="col-md-7">
                                <div class="row g-3">
                                    <div class="col-md-6">
                                        <label>Brand</label>
                                        <input type="text" class="form-control" name="brand"
                                               placeholder="Enter car brand..." required>
                                    </div>

                                    <!-- Transmission -->
                                    <div class="col-md-6">
                                        <label class="form-label"><i class="fa fa-cogs text-primary"></i> Transmission</label>
                                        <select name="transmission" class="form-select" required>
                                            <option value="">Select transmission...</option>
                                            <c:forEach var="t" items="${transmissions}">
                                                <option value="${t}">${t}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <!-- Fuel Type -->
                                    <div class="col-md-6">
                                        <label class="form-label"><i class="fa fa-gas-pump text-primary"></i> Fuel Type</label>
                                        <select name="fuelType" class="form-select" required>
                                            <option value="">Select fuel type...</option>
                                            <c:forEach var="f" items="${fuelTypes}">
                                                <option value="${f}">${f}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <!-- Car Type -->
                                    <div class="col-md-6">
                                        <label class="form-label"><i class="fa fa-tags text-primary"></i> Car Type</label>
                                        <select name="typeId" class="form-select" required>
                                            <option value="">Select car type...</option>
                                            <c:forEach var="c" items="${carTypes}">
                                                <option value="${c.typeId}">${c.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>


                                    <div class="col-md-6">
                                        <label>Year</label>
                                        <input type="number" class="form-control" name="year"
                                               placeholder="Enter manufacturing year..." required>
                                    </div>

                                    <div class="col-md-6">
                                        <label>Price/Day ($)</label>
                                        <input type="text" class="form-control" name="pricePerDay"
                                               placeholder="Enter price per day..." required>
                                    </div>

                                    <div class="col-md-6">
                                        <label>Capacity</label>
                                        <input type="number" class="form-control" name="capacity"
                                               placeholder="Enter car capacity..." required>
                                    </div>

                                    <div class="col-md-6">
                                        <label>License Plate</label>
                                        <input type="text" class="form-control" name="licensePlate"
                                               placeholder="Enter license plate..." required>
                                    </div>

                                    <div class="col-md-6">
                                        <label>Car Type</label>
                                        <select name="typeId" class="form-select" required>
                                            <option value="">Select car type...</option>
                                            <c:forEach var="c" items="${carTypes}">
                                                <option value="${c.typeId}">${c.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="col-md-6">
                                        <label>Location</label>
                                        <input type="text" class="form-control" name="location"
                                               placeholder="Enter car location...">
                                    </div>

                                    <div class="col-12">
                                        <label>Description</label>
                                        <textarea name="description" class="form-control" rows="3"
                                                  placeholder="Enter description about the car..."></textarea>
                                    </div>
                                </div>

                                <div class="text-center mt-4">
                                    <button type="submit" class="btn-submit">Submit</button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </section>
    </div>

    <jsp:include page="../common/carOwner/_footer_scriptsOwner.jsp"/>
</div>

<script>
    function previewFile(event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = e => document.getElementById('previewImage').src = e.target.result;
            reader.readAsDataURL(file);
        }
    }
</script>
</body>
</html>
