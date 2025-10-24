<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">

    <head>
        <jsp:include page="../common/customer/_head.jsp"/>
        <%--    <title>${car.model} - Vehicle Fleet</title>--%>
        <%--    <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="image/gif" sizes="16x16">--%>
        <%--    <meta content="text/html;charset=utf-8" http-equiv="Content-Type">--%>
        <%--    <meta content="width=device-width, initial-scale=1.0" name="viewport">--%>

        <%--    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css">--%>
        <%--    <link href="${pageContext.request.contextPath}/css/mdb.min.css" rel="stylesheet" type="text/css">--%>
        <%--    <link href="${pageContext.request.contextPath}/css/plugins.css" rel="stylesheet" type="text/css">--%>
        <%--    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">--%>
        <%--    <link href="${pageContext.request.contextPath}/css/coloring.css" rel="stylesheet" type="text/css">--%>
        <%--    <link id="colors" href="${pageContext.request.contextPath}/css/colors/scheme-01.css" rel="stylesheet"--%>
        <%--          type="text/css">--%>
    </head>

    <body>
        <div id="wrapper">
            <!-- Header -->
            <%--    <header class="transparent scroll-light has-topbar">--%>
            <jsp:include page="../common/customer/_header.jsp"/>
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

            <section id="section-car-details" class="py-5">
                <div class="container-fluid px-5">
                    <div class="row g-5 align-items-start">

                        <div class="col-lg-5">
                            <div id="slider-carousel" class="owl-carousel" style="max-width:100%; margin:auto;">
                                <c:forEach var="img" items="${car.images}">
                                    <div class="item text-center">
                                        <img src="${pageContext.request.contextPath}/${img.imageUrl}"
                                             alt="Ảnh xe ${car.brand} ${car.model}"
                                             class="rounded shadow-sm"
                                             style="max-width:100%; height:auto; object-fit:contain;">
                                    </div>
                                </c:forEach>
                            </div>

                            <div class="mt-4 pt-3">
                                <h3 class="mb-3 fw-semibold">Description</h3>
                                <p style="font-size:1.05rem; line-height:1.6; text-align:justify;">
                                    ${car.description}
                                </p>
                            </div>
                        </div>

                        <div class="col-lg-3">
                            <h3 class="fw-bold mb-3">${car.model}</h3>

                            <div class="de-price text-center border rounded p-3 bg-white shadow-sm mb-4">
                                <span class="text-muted">Rental Price/Day</span>
                                <h2 class="text-success mt-2">
                                    <fmt:formatNumber value="${car.pricePerDay}" type="currency" currencyCode="VND" maxFractionDigits="0"/>
                                </h2>
                            </div>

                            <h4 class="text-secondary mb-3">Specifications</h4>
                            <div class="de-spec p-3 rounded border bg-light shadow-sm">
                                <div class="d-row mb-2"><span class="d-title">Type:</span><span class="d-value">${car.carTypeName}</span></div>
                                <div class="d-row mb-2"><span class="d-title">Seat:</span><span class="d-value">${car.capacity}</span></div>
                                <div class="d-row mb-2"><span class="d-title">Transmission:</span><span class="d-value">${car.transmission}</span></div>
                                <div class="d-row"><span class="d-title">Fuel:</span><span class="d-value">${car.fuelType}</span></div>
                            </div>
                        </div>

                        <div class="col-lg-4">

                            <h5 class="fw-bold mb-3">Booking this car</h5>

                            <form action="${pageContext.request.contextPath}/booking" method="post"
                                  class="booking-form p-3 rounded shadow-sm bg-light border">

                                <input type="hidden" name="carId" value="${car.carId}"/>
                                <input type="hidden" id="calculatedDiscount" name="calculatedDiscount" value="0">
                                <input type="hidden" id="appliedPromoCode" name="appliedPromoCode" value="">
                                <input type="hidden" id="finalCalculatedPrice" name="finalCalculatedPrice" value="${car.pricePerDay}">
                                <input type="hidden" id="originalPrice" name="originalPrice" value="${car.pricePerDay}">

                                <div class="form-group mb-2">
                                    <label class="mb-1 small">Pickup Date & Time</label>
                                    <div class="input-group-date-time">
                                        <input type="date" name="startDate" class="form-control form-control-sm" required>
                                        <select name="pickupTime" class="form-select form-select-sm" required>
                                            <option value="" disabled selected>-- Time --</option>
                                            <c:forEach var="hour" begin="6" end="22">
                                                <option value="${hour < 10 ? '0' : ''}${hour}:00">${hour < 10 ? '0' : ''}${hour}:00</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group mb-2">
                                    <label class="mb-1 small">Return Date & Time</label>
                                    <div class="input-group-date-time">
                                        <input type="date" name="endDate" class="form-control form-control-sm" required>
                                        <select name="dropoffTime" class="form-select form-select-sm" required>
                                            <option value="" disabled selected>-- Time --</option>
                                            <c:forEach var="hour" begin="6" end="22">
                                                <option value="${hour < 10 ? '0' : ''}${hour}:00">${hour < 10 ? '0' : ''}${hour}:00</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group mb-3">
                                    <label class="mb-1 small">The Location</label>
                                    <div class="p-2 border rounded bg-white small">
                                        <i class="fa fa-map-marker text-success me-2"></i>
                                        ${car.location}
                                    </div>
                                    <small class="text-muted">The car can only be picked up and returned at this fixed address.</small>
                                </div>

                                <div class="form-group mb-3">
                                    <h6 class="fw-bold mb-2">Discount</h6>
                                    <div class="input-group input-group-sm">
                                        <input type="text" id="promoCode" name="promoCode" class="form-control"
                                               placeholder="Enter your discount ...">
                                        <button type="button" id="applyPromo" class="btn btn-success btn-sm">Apply</button>
                                    </div>
                                    <small id="promoMessage" class="text-danger mt-2 d-block"></small>
                                </div>

                                <div class="border rounded p-3 bg-white mb-3">
                                    <p class="mb-1 d-flex justify-content-between small">
                                        <span>Car Rental Fee:</span>
                                        <span id="priceValue" data-total="${car.pricePerDay}">
                                <fmt:formatNumber value="${car.pricePerDay}" type="number" maxFractionDigits="0"/>
                            </span>₫
                                    </p>
                                    <p class="mb-1 d-flex justify-content-between small text-danger">
                                        <span>Discount:</span>
                                        <span id="discount">0</span>₫
                                    </p>
                                    <hr class="my-2">
                                    <div class="d-flex justify-content-between fw-bold">
                                        <span>Total :</span>
                                        <span id="finalPrice" class="text-success">
                                <fmt:formatNumber value="${car.pricePerDay}" type="number" maxFractionDigits="0"/>
                            </span>₫
                                    </div>
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

    <script>
        // Lấy giá thuê gốc mỗi ngày từ input ẩn (giá này không bao giờ thay đổi)
        const ORIGINAL_PRICE_PER_DAY = parseFloat(document.getElementById("originalPrice").value);
        let appliedPromo = null;

        // Tính tổng tiền dựa trên số ngày
        function calculateTotal() {
            const startDate = document.querySelector('input[name="startDate"]').value;
            const endDate = document.querySelector('input[name="endDate"]').value;

            // LUÔN LUÔN DÙNG GIÁ GỐC KHÔNG ĐỔI
            const pricePerDay = ORIGINAL_PRICE_PER_DAY;

            if (!startDate || !endDate) {
                // Nếu thiếu ngày, trả về giá 1 ngày
                return pricePerDay;
            }

            const start = new Date(startDate);
            const end = new Date(endDate);

            // 1. Kiểm tra ngày hợp lệ
            if (end < start) {
                document.getElementById("promoMessage").innerHTML = "❌ Ngày trả xe không được trước ngày nhận!";
                document.getElementById("promoMessage").className = "text-danger mt-2 d-block";
                return pricePerDay;
            }

            const timeDiff = end - start;
            // 2. TÍNH LẠI SỐ NGÀY: làm tròn lên để tính cả ngày đầu tiên và đảm bảo thuê ít nhất 1 ngày.
            let days = Math.ceil(timeDiff / (1000 * 60 * 60 * 24));

            // Đảm bảo số ngày thuê tối thiểu là 1 nếu ngày hợp lệ
            if (days <= 0 && timeDiff >= 0) {
                days = 1;
            }

            const newTotal = days * pricePerDay;

            // Cập nhật hiển thị phí thuê xe (KHÔNG GHI ĐÈ data-total hoặc giá gốc)
            document.getElementById("priceValue").textContent = newTotal.toLocaleString('vi-VN');

            // Xóa thông báo lỗi nếu ngày đã hợp lệ
            if (document.getElementById("promoMessage").innerHTML.includes("Ngày trả xe")) {
                document.getElementById("promoMessage").innerHTML = "";
            }

            return newTotal;
        }

        // Cập nhật giá khi thay đổi ngày
        function updatePriceOnDateChange() {
            const newTotal = calculateTotal();

            // Nếu có mã khuyến mãi đã áp dụng, tính lại
            if (appliedPromo) {
                applyPromoCode(appliedPromo.code, newTotal);
            } else {
                resetPromoDisplay(newTotal);
            }
        }

        // Áp dụng mã khuyến mãi (Giữ nguyên logic API)
        function applyPromoCode(code, total = calculateTotal()) {
            const msg = document.getElementById("promoMessage");
            const contextPath = "${pageContext.request.contextPath}";

            if (!code) {
                msg.innerHTML = "⚠️ Vui lòng nhập mã khuyến mãi!";
                msg.className = "text-danger mt-2 d-block";
                return;
            }

            // Tạm thời hiển thị đang xử lý và xóa lỗi ngày nếu có
            msg.innerHTML = "Đang kiểm tra mã...";
            msg.className = "text-info mt-2 d-block";


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
                            appliedPromo = {code: code, rate: data.rate};
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
        document.addEventListener("DOMContentLoaded", function () {
            // Áp mã khi click nút
            document.getElementById("applyPromo").addEventListener("click", function () {
                const code = document.getElementById("promoCode").value.trim();
                applyPromoCode(code);
            });

            // Tính toán lại khi thay đổi ngày
            document.querySelector('input[name="startDate"]').addEventListener("change", updatePriceOnDateChange);
            document.querySelector('input[name="endDate"]').addEventListener("change", updatePriceOnDateChange);

            // Enter để áp mã
            document.getElementById("promoCode").addEventListener("keypress", function (e) {
                if (e.key === "Enter") {
                    e.preventDefault();
                    document.getElementById("applyPromo").click();
                }
            });

            // Khởi tạo giá trị ban đầu nếu các trường ngày đã được điền sẵn
            updatePriceOnDateChange();
        });
    </script>
</body>
</html>git