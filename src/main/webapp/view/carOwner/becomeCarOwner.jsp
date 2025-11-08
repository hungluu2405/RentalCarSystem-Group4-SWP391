<!DOCTYPE html>
<html lang="zxx">
<head>
    <title>Rentaly - Contact Us</title>
    <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="image/gif" sizes="16x16">
    <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta content="Rentaly - Multipurpose Vehicle Car Rental Website Template" name="description">

    <!-- CSS Files -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" id="bootstrap">
    <link href="${pageContext.request.contextPath}/css/mdb.min.css" rel="stylesheet" type="text/css" id="mdb">
    <link href="${pageContext.request.contextPath}/css/plugins.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/coloring.css" rel="stylesheet" type="text/css">
    <link id="colors" href="${pageContext.request.contextPath}/css/colors/scheme-01.css" rel="stylesheet" type="text/css">
</head>

<body>
<div id="wrapper">

    <!-- Preloader -->
    <div id="de-preloader"></div>

    <!-- Header -->
    <jsp:include page="../common/customer/_header.jsp"/>
    <!-- Header close -->

    <div class="no-bottom no-top" id="content">
        <div id="top"></div>

        <!-- Contact Us Banner -->
        <section id="subheader" class="jarallax text-light">
            <img src="${pageContext.request.contextPath}/images/background/subheader.jpg" class="jarallax-img" alt="">
            <div class="center-y relative text-center">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center">
                            <h1>Become Car Owner</h1>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Add Car Form Section -->
        <section id="section-addcar" class="bg-gray-100 py-5">
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-lg-10">
                        <div class="card padding40 rounded-5 shadow-sm">
                            <h3 class="mb-4 text-center"><i class=""></i> Become a Rentaly partner to get the best deals</h3>

                            <!-- Hiển thị thông báo lỗi (nếu có) -->
<%--                            <c:if test="${not empty errorMessage}">--%>
<%--                                <div class="alert alert-danger text-center mb-3">--%>
<%--                                        ${errorMessage}--%>
<%--                                </div>--%>
<%--                            </c:if>--%>

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
                                        <label class="form-label"><i class="fa fa-money-bill-wave text-primary"></i> Price/Day $</label>
                                        <input type="text" id="pricePerDayDisplay" class="form-control" placeholder="Enter price per day..." required>
                                        <input type="hidden" name="pricePerDay" id="pricePerDay">
                                    </div>

                                    <script>
                                        const displayInput = document.getElementById('pricePerDayDisplay');
                                        const hiddenInput = document.getElementById('pricePerDay');
                                        function formatNumber(value) {
                                            return value.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
                                        }
                                        displayInput.addEventListener('input', function (e) {
                                            let rawValue = e.target.value.replace(/[^\d]/g, '');
                                            e.target.value = formatNumber(rawValue);
                                            hiddenInput.value = rawValue;
                                        });
                                    </script>

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
                                        <label class="form-label"><i class="fa fa-map-location-dot text-primary"></i> Location</label>
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
                                            <i class="fa fa-plus-circle"></i> Register Now
                                        </button>
                                    </div>
                                </div>
                            </form>

                            <script>
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
            </div>
        </section>
        <!-- End Add Car Form Section -->
    </div>

    <!-- Footer -->
    <jsp:include page="../common/customer/_footer_scripts.jsp"/>
</div>

<!-- JavaScript -->
<script src="${pageContext.request.contextPath}/js/plugins.js"></script>
<script src="${pageContext.request.contextPath}/js/designesia.js"></script>
<script>
    const baseUrl = '${pageContext.request.contextPath}';
</script>
</body>
</html>
