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
                        <div class="col-md-12 text-center"><h1>Thêm xe mới</h1></div>
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
                        <div class="card padding40 rounded-5 shadow-sm">
                            <h3 class="mb-4"><i class="fa fa-plus-circle"></i>Thêm xe mới</h3>

                            <!-- Hiển thị thông báo lỗi (nếu có) -->
                            <c:if test="${not empty errorMessage}">
                                <div class="alert alert-danger text-center mb-3">
                                        ${errorMessage}
                                </div>
                            </c:if>

                            <form action="${pageContext.request.contextPath}/owner/addCar" method="post" enctype="multipart/form-data" class="p-4 bg-white rounded shadow-sm">

                                    <!-- 🔧 HIỂN THỊ ẢNH -->
                                    <div class="text-center mb-4">
                                        <c:choose>
                                            <%-- Nếu có ảnh tạm, hiển thị lại --%>
                                            <c:when test="${not empty tempImagePath}">
                                                <img id="previewImage"
                                                     src="${pageContext.request.contextPath}/${tempImagePath}"
                                                     alt="Car Preview"
                                                     class="img-fluid rounded shadow-sm"
                                                     style="max-width: 300px;">
                                            </c:when>

                                            <%-- Nếu chưa có ảnh tạm, dùng ảnh mặc định --%>
                                            <c:otherwise>
                                                <img id="previewImage"
                                                     src="${pageContext.request.contextPath}/images/default-car.png"
                                                     alt="Car Preview"
                                                     class="img-fluid rounded shadow-sm"
                                                     style="max-width: 300px;">
                                            </c:otherwise>
                                        </c:choose>

                                        <div class="mt-3">
                                            <label for="carImage" class="form-label fw-bold">Tải ảnh xe</label>
                                            <input type="file" id="carImage" name="carImage" accept="image/*"
                                                   class="form-control" onchange="previewFile(this)">
                                            <input type="hidden" name="tempImagePath" value="${tempImagePath}">
                                        </div>
                                    </div>


                                    <div class="row g-3">
                                    <!-- BRAND -->
                                    <div class="col-md-6">
                                        <label class="form-label"><i class="fa fa-industry text-primary"></i>Tên hãng xe</label>
                                        <input type="text" name="brand" class="form-control" placeholder="Nhập tên hãng xe..." value="${brand}" required>
                                    </div>

                                    <!-- TRANSMISSION -->
                                    <div class="col-md-6">
                                        <label class="form-label"><i class="fa fa-cogs text-primary"></i> Truyền động cơ</label>
                                        <select name="transmission" class="form-select" required>
                                            <option value="">Chọn truyền động cơ</option>
                                            <c:forEach var="t" items="${transmissions}">
                                                <option value="${t}" <c:if test="${t == transmission}">selected</c:if>>${t}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <!-- MODEL -->
                                    <div class="col-md-6">
                                        <label class="form-label"><i class="fa fa-car text-primary"></i>Tên mẫu </label>
                                        <input type="text" name="model" class="form-control" placeholder="Nhập tên mẫu xe..." value="${model}" required>
                                    </div>

                                    <!-- FUEL TYPE -->
                                    <div class="col-md-6">
                                        <label class="form-label"><i class="fa fa-gas-pump text-primary"></i>Loại nhiên liệu</label>
                                        <select name="fuelType" class="form-select" required>
                                            <option value="">Chọn loại nhiên liệu...</option>
                                            <c:forEach var="f" items="${fuelTypes}">
                                                <option value="${f}" <c:if test="${f == fuelType}">selected</c:if>>${f}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <!-- YEAR -->
                                    <div class="col-md-6">
                                        <label class="form-label"><i class="fa fa-calendar text-primary"></i>Năm sản xuất</label>
                                        <input type="number" name="year" class="form-control" placeholder="Nhập năm sản xuất..." value="${year}" required>
                                    </div>

                                    <!-- PRICE -->
                                    <div class="col-md-6">
                                        <label class="form-label">
                                            <i class="fa fa-money-bill-wave text-primary"></i> Giá thuê trên ngày(VND)
                                        </label>
                                        <input type="text" id="pricePerDayDisplay" class="form-control"
                                               placeholder="Nhập giá thuê trên ngày...(VND)" value="${pricePerDay}" required>
                                        <input type="hidden" name="pricePerDay" id="pricePerDay" value="${pricePerDay}">

                                    </div>

                                    <script>
                                        const displayInput = document.getElementById('pricePerDayDisplay');
                                        const hiddenInput = document.getElementById('pricePerDay');

                                        // Hàm định dạng số có dấu . hoặc , ngăn cách hàng nghìn
                                        function formatNumber(value) {
                                            return value.replace(/\B(?=(\d{3})+(?!\d))/g, '.'); // hoặc dùng ',' nếu muốn
                                        }

                                        displayInput.addEventListener('input', function (e) {
                                            let rawValue = e.target.value.replace(/[^\d]/g, ''); // bỏ ký tự không phải số
                                            e.target.value = formatNumber(rawValue);             // hiển thị có dấu .
                                            hiddenInput.value = rawValue;                        // lưu giá trị thật
                                        });
                                    </script>

                                    <!-- CAPACITY -->
                                    <div class="col-md-6">
                                        <label class="form-label"><i class="fa fa-users text-primary"></i> Số ghế</label>
                                        <input type="number" name="capacity" class="form-control" placeholder="Nhập số ghế..." value="${capacity}" required>
                                    </div>

                                    <!-- LICENSE PLATE -->
                                    <div class="col-md-6">
                                        <label class="form-label"><i class="fa fa-id-card text-primary"></i> Biển số xe</label>
                                        <input type="text" name="licensePlate" class="form-control" placeholder="Nhập số ghế xe...(format: 29A-123.45)" value="${licensePlate}" required>
                                    </div>

                                    <!-- CAR TYPE -->
                                    <div class="col-md-6">
                                        <label class="form-label"><i class="fa fa-tags text-primary"></i> Loại xe</label>
                                        <select name="typeId" class="form-select" required>
                                            <option value="">Chọn loại xe...</option>
                                            <c:forEach var="c" items="${carTypes}">
                                                <option value="${c.typeId}" <c:if test="${c.typeId == typeId}">selected</c:if>>${c.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <!-- LOCATION -->
                                    <div class="col-md-6">
                                        <label class="form-label"><i class="fa fa-map-location-dot text-primary"></i> Địa chỉ nhận trả xe</label>
                                        <input type="text" name="location" class="form-control" placeholder="Nhập địa chỉ..." value="${location}">
                                    </div>

                                    <!-- DESCRIPTION -->
                                    <div class="col-12">
                                        <label class="form-label"><i class="fa fa-align-left text-primary"></i> Mô tả</label>
                                        <textarea name="description" class="form-control" rows="4"
                                                  placeholder="Nhập mô tả của xe...">${description}</textarea>

                                    </div>

                                    <!-- SUBMIT BUTTON -->
                                    <div class="col-12 text-center mt-4">
                                        <button type="submit" class="btn btn-primary px-4 py-2">
                                            <i class="fa fa-plus-circle"></i> Thêm xe mới
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
