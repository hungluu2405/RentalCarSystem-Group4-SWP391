<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">

<head>
    <title>${car.model} - Vehicle Fleet</title>
    <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="image/gif" sizes="16x16">
    <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">

    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/mdb.min.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/plugins.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/coloring.css" rel="stylesheet" type="text/css">
    <link id="colors" href="${pageContext.request.contextPath}/css/colors/scheme-01.css" rel="stylesheet"
          type="text/css">
</head>

<body>
<div id="wrapper">
    <!-- Header -->
    <header class="transparent scroll-light has-topbar">
        <%-- giữ nguyên header cũ --%>
    </header>

    <!-- Content -->
    <div class="no-bottom no-top zebra" id="content">
        <div id="top"></div>

        <!-- Banner -->
        <section id="subheader" class="jarallax text-light">
            <img src="${pageContext.request.contextPath}/images/background/2.jpg" class="jarallax-img" alt="">
            <div class="center-y relative text-center">
                <div class="container">
                    <h1>Vehicle Fleet</h1>
                </div>
            </div>
        </section>

        <!-- Car Details -->
        <section id="section-car-details">
            <div class="container">
                <div class="row g-5">

                    <!-- 🟩 Cột hình ảnh -->
                    <div class="col-lg-6">
                        <div id="slider-carousel" class="owl-carousel">
                            <c:forEach var="img" items="${car.images}">
                                <div class="item">
                                    <img src="${pageContext.request.contextPath}/${img.imageUrl}"
                                         alt="Ảnh xe ${car.brand} ${car.model}">
                                </div>
                            </c:forEach>
                        </div>

                        <!-- 🟩 Description moved here -->
                        <div class="mt-4 border-top pt-3">
                            <h3 class="mb-3" style="font-weight:600;">Description</h3>
                            <p style="font-size:1.1rem;line-height:1.6;text-align:justify;">
                                ${car.description}
                            </p>
                        </div>
                    </div>

                    <!-- 🟦 Cột thông tin kỹ thuật -->
                    <div class="col-lg-2">
                        <h3>${car.model}</h3>
                        <div class="spacer-10"></div>
                        <h4>Specifications</h4>
                        <div class="de-spec">
                            <div class="d-row"><span class="d-title">Type</span><span
                                    class="d-value">${car.carTypeName}</span></div>
                            <div class="d-row"><span class="d-title">Seat</span><span
                                    class="d-value">${car.capacity}</span></div>
                            <div class="d-row"><span class="d-title">Transmission</span><span
                                    class="d-value">${car.transmission}</span></div>
                            <div class="d-row"><span class="d-title">Fuel</span><span
                                    class="d-value">${car.fuelType}</span></div>
                        </div>
                    </div>

                    <!-- 🟧 Cột giá và form đặt xe -->
                    <div class="col-lg-4">
                        <div class="de-price text-center">
                            Rental Price/Day
                            <h2><fmt:formatNumber value="${car.pricePerDay}" type="currency" currencyCode="VND"
                                                  maxFractionDigits="0"/></h2>
                        </div>
                        <div class="spacer-30"></div>

                        <form action="${pageContext.request.contextPath}/booking" method="post"
                              class="booking-form p-3 rounded shadow-sm bg-light">
                            <input type="hidden" name="carId" value="${car.carId}"/>

                            <!-- CÁC INPUT ẨN MỚI THÊM -->
                            <input type="hidden" id="calculatedDiscount" name="calculatedDiscount" value="0">
                            <input type="hidden" id="appliedPromoCode" name="appliedPromoCode" value="">
                            <input type="hidden" id="finalCalculatedPrice" name="finalCalculatedPrice" value="${car.pricePerDay}">
                            <input type="hidden" id="originalPrice" name="originalPrice" value="${car.pricePerDay}">

                            <!-- Thời gian nhận -->
                            <div class="form-group mb-3">
                                <label class="fw-bold mb-1">Ngày nhận xe</label>
                                <input type="date" name="startDate" class="form-control" required>
                            </div>

                            <div class="form-group mb-3">
                                <label class="fw-bold mb-1">Giờ nhận xe</label>
                                <select name="pickupTime" class="form-select" required>
                                    <option value="" disabled selected>-- Chọn giờ nhận --</option>
                                    <c:forEach var="hour" begin="6" end="22">
                                        <option value="${hour < 10 ? '0' : ''}${hour}:00">${hour < 10 ? '0' : ''}${hour}:00</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <!-- Thời gian trả -->
                            <div class="form-group mb-3">
                                <label class="fw-bold mb-1">Ngày trả xe</label>
                                <input type="date" name="endDate" class="form-control" required>
                            </div>

                            <div class="form-group mb-3">
                                <label class="fw-bold mb-1">Giờ trả xe</label>
                                <select name="dropoffTime" class="form-select" required>
                                    <option value="" disabled selected>-- Chọn giờ trả --</option>
                                    <c:forEach var="hour" begin="6" end="22">
                                        <option value="${hour < 10 ? '0' : ''}${hour}:00">${hour < 10 ? '0' : ''}${hour}:00</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group mb-3">
                                <label class="fw-bold mb-1">Địa điểm nhận & trả xe</label>
                                <div class="p-3 border rounded bg-light">
                                    <i class="fa fa-map-marker text-success me-2"></i>
                                    ${car.location}
                                </div>
                                <small class="text-muted">Xe chỉ nhận & trả tại địa chỉ cố định này.</small>
                            </div>

                            <div class="form-group mb-3">
                                <label class="fw-bold mb-1">Mã khuyến mãi</label>
                                <div class="input-group">
                                    <input type="text" id="promoCode" name="promoCode" class="form-control"
                                           placeholder="Nhập mã khuyến mãi...">
                                    <button type="button" id="applyPromo" class="btn btn-success">Áp dụng</button>
                                </div>
                                <small id="promoMessage" class="text-danger mt-2 d-block"></small>
                            </div>

                            <div class="border rounded p-3 bg-white mb-3">
                                <p>Phí thuê xe:
                                    <span id="priceValue" data-total="${car.pricePerDay}">
                                            <fmt:formatNumber value="${car.pricePerDay}" type="number"
                                                              maxFractionDigits="0"/>
                                        </span>₫
                                </p>
                                <p>Giảm giá: <span id="discount">0</span>₫</p>
                                <h5 class="fw-bold text-success">Tổng cộng:
                                    <span id="finalPrice">
                                            <fmt:formatNumber value="${car.pricePerDay}" type="number"
                                                              maxFractionDigits="0"/>
                                        </span>₫
                                </h5>
                            </div>

                            <button type="submit" class="btn-main btn-fullwidth">Đặt xe ngay</button>

                            <c:if test="${not empty error}">
                                <div class="alert alert-danger mt-3">${error}</div>
                            </c:if>
                            <c:if test="${not empty message}">
                                <div class="alert alert-success mt-3">${message}</div>
                            </c:if>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </div>

    <a href="#" id="back-to-top"></a>

    <!-- Footer -->
    <footer class="text-light">
        <div class="container">
            <div class="row g-custom-x">
                <div class="col-lg-3">
                    <div class="widget">
                        <h5>About Rentaly</h5>
                        <p>Where quality meets affordability. We provide top-notch vehicles at minimum expense to ensure
                            your journey is smooth and enjoyable.</p>
                    </div>
                </div>

                <div class="col-lg-3">
                    <div class="widget">
                        <h5>Contact Info</h5>
                        <address class="s1">
                            <span><i class="id-color fa fa-map-marker fa-lg"></i>08 W 36th St, New York, NY 10001</span>
                            <span><i class="id-color fa fa-phone fa-lg"></i>+1 333 9296</span>
                            <span><i class="id-color fa fa-envelope-o fa-lg"></i><a href="mailto:contact@example.com">contact@example.com</a></span>
                            <span><i class="id-color fa fa-file-pdf-o fa-lg"></i><a
                                    href="#">Download Brochure</a></span>
                        </address>
                    </div>
                </div>

                <div class="col-lg-3">
                    <h5>Quick Links</h5>
                    <div class="widget">
                        <ul>
                            <li><a href="#">About</a></li>
                            <li><a href="#">Blog</a></li>
                            <li><a href="#">Careers</a></li>
                            <li><a href="#">News</a></li>
                            <li><a href="#">Partners</a></li>
                        </ul>
                    </div>
                </div>

                <div class="col-lg-3">
                    <div class="widget">
                        <h5>Social Network</h5>
                        <div class="social-icons">
                            <a href="#"><i class="fa fa-facebook fa-lg"></i></a>
                            <a href="#"><i class="fa fa-twitter fa-lg"></i></a>
                            <a href="#"><i class="fa fa-linkedin fa-lg"></i></a>
                            <a href="#"><i class="fa fa-pinterest fa-lg"></i></a>
                            <a href="#"><i class="fa fa-rss fa-lg"></i></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="subfooter">
            <div class="container">
                <div class="row">
                    <div class="col-md-12">
                        <div class="de-flex">
                            <div class="de-flex-col">
                                <a href="#">© 2025 Rentaly by Designesia</a>
                            </div>
                            <ul class="menu-simple">
                                <li><a href="#">Terms &amp; Conditions</a></li>
                                <li><a href="#">Privacy Policy</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </footer>
</div>

<script src="${pageContext.request.contextPath}/js/plugins.js"></script>
<script src="${pageContext.request.contextPath}/js/designesia.js"></script>

<!-- 🟦 Script tính toán giá và áp mã khuyến mãi -->
<script>
    // Biến toàn cục
    let currentTotal = parseFloat(document.getElementById("priceValue").dataset.total);
    let appliedPromo = null;

    // Tính tổng tiền dựa trên số ngày
    function calculateTotal() {
        const startDate = document.querySelector('input[name="startDate"]').value;
        const endDate = document.querySelector('input[name="endDate"]').value;

        if (!startDate || !endDate) {
            return currentTotal;
        }

        const start = new Date(startDate);
        const end = new Date(endDate);
        const timeDiff = end - start;
        const days = Math.ceil(timeDiff / (1000 * 60 * 60 * 24));

        if (days <= 0) {
            return currentTotal;
        }

        const pricePerDay = parseFloat(document.getElementById("priceValue").dataset.total);
        const newTotal = days * pricePerDay;

        // Cập nhật hiển thị phí thuê xe
        document.getElementById("priceValue").textContent = newTotal.toLocaleString('vi-VN');
        document.getElementById("priceValue").dataset.total = newTotal;

        return newTotal;
    }

    // Cập nhật giá khi thay đổi ngày
    function updatePriceOnDateChange() {
        const newTotal = calculateTotal();
        currentTotal = newTotal;

        // Nếu có mã khuyến mãi, tính lại
        if (appliedPromo) {
            applyPromoCode(appliedPromo.code, newTotal);
        } else {
            resetPromoDisplay(newTotal);
        }
    }

    // Áp dụng mã khuyến mãi
    function applyPromoCode(code, total = currentTotal) {
        const msg = document.getElementById("promoMessage");
        const contextPath = "${pageContext.request.contextPath}";

        if (!code) {
            msg.innerHTML = "⚠️ Vui lòng nhập mã khuyến mãi!";
            msg.className = "text-danger mt-2 d-block";
            return;
        }

        const url = contextPath + "/check-promo?code=" + encodeURIComponent(code) + "&total=" + total;

        fetch(url)
            .then(response => response.json())
            .then(data => {
                console.log("Response data:", data);

                if (data.error) {
                    msg.innerHTML = "❌ " + data.error;
                    msg.className = "text-danger mt-2 d-block";
                    appliedPromo = null;
                    resetPromoDisplay(total);
                } else if (data.success) {
                    msg.innerHTML = `✅ Áp dụng mã <b>${code}</b> giảm ${data.rate}%`;
                    msg.className = "text-success mt-2 d-block";
                    appliedPromo = { code: code, rate: data.rate };
                    updatePriceDisplay(data.discount, data.finalPrice, total);
                }
            })
            .catch(error => {
                console.error("Fetch error:", error);
                msg.innerHTML = "❌ Có lỗi khi áp mã, vui lòng thử lại!";
                msg.className = "text-danger mt-2 d-block";
            });
    }

    // Cập nhật hiển thị giá
    function updatePriceDisplay(discount, finalPrice, originalTotal) {
        document.getElementById("discount").textContent = discount.toLocaleString('vi-VN');
        document.getElementById("finalPrice").textContent = finalPrice.toLocaleString('vi-VN');

        // Cập nhật các input ẩn
        document.getElementById("calculatedDiscount").value = discount;
        document.getElementById("appliedPromoCode").value = appliedPromo ? appliedPromo.code : "";
        document.getElementById("finalCalculatedPrice").value = finalPrice;
    }

    // Reset hiển thị
    function resetPromoDisplay(total) {
        document.getElementById("discount").textContent = "0";
        document.getElementById("finalPrice").textContent = total.toLocaleString('vi-VN');

        // Reset các input ẩn
        document.getElementById("calculatedDiscount").value = 0;
        document.getElementById("appliedPromoCode").value = "";
        document.getElementById("finalCalculatedPrice").value = total;
    }

    // Event listeners
    document.addEventListener("DOMContentLoaded", function() {
        // Áp mã khi click nút
        document.getElementById("applyPromo").addEventListener("click", function() {
            const code = document.getElementById("promoCode").value.trim();
            applyPromoCode(code);
        });

        // Tính toán lại khi thay đổi ngày
        document.querySelector('input[name="startDate"]').addEventListener("change", updatePriceOnDateChange);
        document.querySelector('input[name="endDate"]').addEventListener("change", updatePriceOnDateChange);

        // Enter để áp mã
        document.getElementById("promoCode").addEventListener("keypress", function(e) {
            if (e.key === "Enter") {
                e.preventDefault();
                document.getElementById("applyPromo").click();
            }
        });
    });
</script>
</body>
</html>