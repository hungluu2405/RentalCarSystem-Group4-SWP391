<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<fmt:setLocale value="vi_VN" />


<!DOCTYPE html>
<html lang="vi">


<head>
    <jsp:include page="../common/customer/_head.jsp"/>
    <style>
        .btn-main:disabled {
            opacity: 0.7;
            cursor: not-allowed;
        }


        .btn-main .fa-spinner {
            margin-right: 8px;
        }
    </style>
</head>


<body>
<div id="wrapper">
    <jsp:include page="../common/customer/_header.jsp"/>


    <div class="no-bottom no-top zebra" id="content">
        <div id="top"></div>


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
                            <h3 class="mb-3 fw-semibold">Mô Tả</h3>
                            <p style="font-size:1.05rem; line-height:1.6; text-align:justify;">
                                ${car.description}
                            </p>
                        </div>


                        <!-- ========== THÔNG TIN CHỦ XE ========== -->
                        <div class="mt-4 pt-4 border-top pt-3">
                            <h3 class="fw-semibold mb-3" style="font-size:1.75rem;">Chủ xe</h3>
                            <c:if test="${not empty ownerProfile}">
                                <div class="d-flex align-items-center">
                                    <a href="${pageContext.request.contextPath}/carOwner/info?id=${car.ownerId}"
                                       class="d-flex align-items-center text-decoration-none text-dark">
                                        <img src="${pageContext.request.contextPath}/${ownerProfile.profileImage != null ? ownerProfile.profileImage : 'images/default-avatar.png'}"
                                             alt="${ownerProfile.fullName}"
                                             class="rounded-circle shadow-sm me-3"
                                             style="width:64px; height:64px; object-fit:cover;">
                                        <div>
                                            <h6 class="mb-1">${ownerProfile.fullName}</h6>
                                            <small class="text-muted">Xem thông tin chủ xe</small>
                                        </div>
                                    </a>
                                </div>
                            </c:if>
                        </div>




                    </div>


                    <div class="col-lg-3">
                        <h3 class="fw-bold mb-3">${car.model}</h3>


                        <!-- ✅ VND FORMAT -->
                        <div class="de-price text-center border rounded p-3 bg-white shadow-sm mb-4">
                            <span class="text-muted">Price/Day</span>
                            <h2 class="text-success mt-2">
                                <fmt:formatNumber value="${car.pricePerDay}"
                                                  type="number"
                                                  groupingUsed="true"
                                                  minFractionDigits="0"
                                                  maxFractionDigits="0"/> ₫
                            </h2>
                        </div>


                        <h4 class="text-secondary mb-3">Đặc Điểm</h4>
                        <div class="de-spec p-3 rounded border bg-light shadow-sm">
                            <div class="d-row mb-2"><span class="d-title">Loại:</span><span class="d-value">${car.carTypeName}</span></div>
                            <div class="d-row mb-2"><span class="d-title">Số ghế:</span><span class="d-value">${car.capacity}</span></div>
                            <div class="d-row mb-2"><span class="d-title">Truyền Động:</span><span class="d-value">${car.transmission}</span></div>
                            <div class="d-row"><span class="d-title">Nhiên Liệu:</span><span class="d-value">${car.fuelType}</span></div>
                        </div>
                    </div>


                    <div class="col-lg-4">
                        <h5 class="fw-bold mb-3">Thuê Xe Này</h5>


                        <form action="${pageContext.request.contextPath}/booking" method="post"
                              class="booking-form p-3 rounded shadow-sm bg-light border">


                            <input type="hidden" name="carId" value="${car.carId}"/>
                            <input type="hidden" id="calculatedDiscount" name="calculatedDiscount" value="${input_calculatedDiscount != null ? input_calculatedDiscount : 0}">
                            <input type="hidden" id="appliedPromoCode" name="appliedPromoCode" value="${input_appliedPromoCode}">
                            <input type="hidden" id="finalCalculatedPrice" name="finalCalculatedPrice" value="${input_finalCalculatedPrice != null ? input_finalCalculatedPrice : car.pricePerDay}">
                            <input type="hidden" id="originalPrice" name="originalPrice" value="${car.pricePerDay}">


                            <c:if test="${not empty error}">
                                <div class="alert alert-danger mt-3">${error}</div>
                            </c:if>


                            <div class="form-group mb-2">
                                <label class="mb-1 small">Thời Gian Nhận Xe</label>
                                <div class="input-group-date-time">
                                    <input type="date" name="startDate" class="form-control form-control-sm" required
                                           value="${input_startDate}">
                                    <select name="pickupTime" class="form-select form-select-sm" required>
                                        <option value="" disabled ${empty input_pickupTime ? 'selected' : ''}>-- Giờ --</option>
                                        <c:forEach var="hour" begin="6" end="22">
                                            <c:set var="timeValue" value="${hour < 10 ? '0' : ''}${hour}:00"/>
                                            <option value="${timeValue}" ${input_pickupTime == timeValue ? 'selected' : ''}>${timeValue}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>


                            <div class="form-group mb-2">
                                <label class="mb-1 small">Thời Gian Trả Xe</label>
                                <div class="input-group-date-time">
                                    <input type="date" name="endDate" class="form-control form-control-sm" required
                                           value="${input_endDate}">
                                    <select name="dropoffTime" class="form-select form-select-sm" required>
                                        <option value="" disabled ${empty input_dropoffTime ? 'selected' : ''}>-- Giờ --</option>
                                        <c:forEach var="hour" begin="6" end="22">
                                            <c:set var="timeValue" value="${hour < 10 ? '0' : ''}${hour}:00"/>
                                            <option value="${timeValue}" ${input_dropoffTime == timeValue ? 'selected' : ''}>${timeValue}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>


                            <div class="form-group mb-3">
                                <label class="mb-1 small">Địa Điểm</label>
                                <div class="p-2 border rounded bg-white small">
                                    <i class="fa fa-map-marker text-success me-2"></i>
                                    <input type="hidden" name="location" value="${input_location != null ? input_location : car.location}">
                                    ${car.location}
                                </div>
                                <small class="text-muted">
                                    Xe chỉ có thể được nhận và trả tại địa chỉ cố định này.</small>
                            </div>


                            <div class="form-group mb-3">
                                <h6 class="fw-bold mb-2">Khuyến Mãi</h6>
                                <div class="input-group input-group-sm">
                                    <input type="text" id="promoCode" name="promoCode" class="form-control"
                                           placeholder="Enter your discount ..." value="${input_appliedPromoCode}">
                                    <button type="button" id="applyPromo" class="btn btn-success btn-sm">Áp Dụng</button>
                                </div>
                                <small id="promoMessage" class="text-danger mt-2 d-block">
                                    <c:if test="${not empty input_appliedPromoCode && empty error}">
                                        ✅ Coupon applied: ${input_appliedPromoCode}
                                    </c:if>
                                </small>
                            </div>


                            <!-- ✅ VND FORMAT -->
                            <div class="border rounded p-3 bg-white mb-3">
                                <p class="mb-1 d-flex justify-content-between small">
                                    <span>Đơn Giá Thuê:</span>
                                    <span id="priceValue" data-total="${car.pricePerDay}">
                                       <fmt:formatNumber value="${car.pricePerDay}"
                                                         type="number"
                                                         groupingUsed="true"
                                                         minFractionDigits="0"
                                                         maxFractionDigits="0"/> ₫
                                   </span>
                                </p>
                                <p class="mb-1 d-flex justify-content-between small text-danger">
                                    <span>Khuyến Mãi:</span>
                                    <span id="discount">
                                       <fmt:formatNumber value="${input_calculatedDiscount != null ? input_calculatedDiscount : 0}"
                                                         type="number"
                                                         groupingUsed="true"
                                                         minFractionDigits="0"
                                                         maxFractionDigits="0"/> ₫
                                   </span>
                                </p>
                                <hr class="my-2">
                                <div class="d-flex justify-content-between fw-bold">
                                    <span>Thành Tiền:</span>
                                    <span id="finalPrice" class="text-success">
                                       <fmt:formatNumber value="${input_finalCalculatedPrice != null ? input_finalCalculatedPrice : car.pricePerDay}"
                                                         type="number"
                                                         groupingUsed="true"
                                                         minFractionDigits="0"
                                                         maxFractionDigits="0"/> ₫
                                   </span>
                                </div>
                            </div>


                            <button type="submit" class="btn-main btn-fullwidth">Booking Now</button>


                        </form>
                    </div>
                </div>
            </div>
        </section>


        <!-- ========== ĐÁNH GIÁ CỦA KHÁCH HÀNG ========== -->
        <section class="py-5">
            <div class="container">
                <h4 class="mb-4 text-center">Đánh giá của khách hàng</h4>


                <!-- Bộ lọc -->
                <form method="get" class="row g-2 mb-4 justify-content-center">
                    <input type="hidden" name="id" value="${car.carId}">
                    <div class="col-md-3">
                        <select name="rating" class="form-select" onchange="this.form.submit()">
                            <option value="">Tất cả sao</option>
                            <c:forEach var="r" begin="1" end="5">
                                <option value="${r}" ${ratingFilter == r ? 'selected' : ''}>${r} sao</option>
                            </c:forEach>
                        </select>
                    </div>
                </form>


                <!-- Danh sách review -->
                <c:choose>
                    <c:when test="${empty reviews}">
                        <div class="alert alert-warning text-center">
                            Chưa có đánh giá nào cho xe này.
                        </div>
                    </c:when>


                    <c:otherwise>
                        <div class="row justify-content-center">
                            <div class="col-lg-8 col-md-10">
                                <c:forEach var="r" items="${reviews}">
                                    <div class="card mb-3 shadow-sm border-0" style="border-radius:14px;">
                                        <div class="card-body">
                                            <div class="d-flex align-items-center mb-2">
                                                <img src="${pageContext.request.contextPath}/${empty r.profileImage ? 'images/default-avatar.png' : r.profileImage}"
                                                     alt="${r.fullName}"
                                                     class="rounded-circle me-3 shadow-sm"
                                                     style="width:50px; height:50px; object-fit:cover;">
                                                <div>
                                                    <h6 class="mb-0 fw-semibold">${r.fullName}</h6>
                                                    <small class="text-muted">
                                                            ${fn:replace(r.review.createdAt, 'T', ' ')}
                                                    </small>
                                                </div>
                                            </div>


                                            <div class="mb-2">
                                                <c:forEach begin="1" end="${r.review.rating}">
                                                    <i class="fa fa-star text-warning"></i>
                                                </c:forEach>
                                                <c:forEach begin="${r.review.rating + 1}" end="5">
                                                    <i class="fa fa-star text-muted"></i>
                                                </c:forEach>
                                            </div>


                                            <p class="mb-0" style="color:#374151;">
                                                <c:choose>
                                                    <c:when test="${not empty r.review.comment}">
                                                        ${r.review.comment}
                                                    </c:when>
                                                    <c:otherwise>
                                                       <span class="text-muted fst-italic">
                                                           Người dùng không để lại bình luận.
                                                       </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>


                        <!-- ✅ Phân trang -->
                        <c:if test="${totalPages > 1}">
                            <nav class="mt-4">
                                <ul class="pagination justify-content-center">


                                    <!-- Nút Trang Trước -->
                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a class="page-link"
                                           href="?id=${car.carId}&page=${currentPage - 1}&rating=${ratingFilter}">
                                            &laquo; Trước
                                        </a>
                                    </li>


                                    <!-- Danh sách trang -->
                                    <c:forEach var="i" begin="1" end="${totalPages}">
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link"
                                               href="?id=${car.carId}&page=${i}&rating=${ratingFilter}">
                                                    ${i}
                                            </a>
                                        </li>
                                    </c:forEach>


                                    <!-- Nút Trang Sau -->
                                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                        <a class="page-link"
                                           href="?id=${car.carId}&page=${currentPage + 1}&rating=${ratingFilter}">
                                            Sau &raquo;
                                        </a>
                                    </li>


                                </ul>
                            </nav>
                        </c:if>


                    </c:otherwise>
                </c:choose>
            </div>
        </section>


    </div>


    <a href="#" id="back-to-top"></a>


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
                            <span><i class="id-color fa fa-file-pdf-o fa-lg"></i><a href="#">Download Brochure</a></span>
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
    const ORIGINAL_PRICE_PER_DAY = parseFloat(document.getElementById("originalPrice").value) || 0;
    let appliedPromo = ${not empty input_appliedPromoCode ? '{"code": "' + input_appliedPromoCode + '", "rate": 0}' : 'null'};


    // =============== CALCULATION ===============
    function calculateTotal() {
        const startDate = document.querySelector('input[name="startDate"]').value;
        const endDate = document.querySelector('input[name="endDate"]').value;
        const pickupTime = document.querySelector('select[name="pickupTime"]').value;
        const dropoffTime = document.querySelector('select[name="dropoffTime"]').value;


        if (!startDate || !endDate || !pickupTime || !dropoffTime) return ORIGINAL_PRICE_PER_DAY;


        const [sy, sm, sd] = startDate.split('-').map(Number);
        const [ey, em, ed] = endDate.split('-').map(Number);
        const [sh, smin] = pickupTime.split(':').map(Number);
        const [eh, emin] = dropoffTime.split(':').map(Number);


        const start = new Date(sy, sm - 1, sd, sh, smin || 0, 0);
        const end = new Date(ey, em - 1, ed, eh, emin || 0, 0);


        const diffMs = end.getTime() - start.getTime();
        const diffHours = diffMs / (1000 * 60 * 60);


        if (diffHours < 24) {
            showWarning("⚠️ Minimum rental period is 24 hours!");
            return ORIGINAL_PRICE_PER_DAY;
        } else {
            hideWarning();
        }


        // Pricing logic
        const fullDays = Math.floor(diffHours / 24);
        const remaining = diffHours % 24;


        let total = fullDays * ORIGINAL_PRICE_PER_DAY;
        const hourlyRate = ORIGINAL_PRICE_PER_DAY / 24;


        if (remaining <= 1) {
            // free
        } else if (remaining > 1 && remaining <= 6) {
            total += (remaining - 1) * hourlyRate * 1.2;
        } else {
            total += ORIGINAL_PRICE_PER_DAY;
        }


        console.log("💰 calculateTotal():", {
            startDate, endDate, pickupTime, dropoffTime,
            diffHours, fullDays, remaining, total
        });


        return total;
    }


    // =============== UI UPDATES ===============
    function showWarning(msg) {
        const el = document.getElementById("promoMessage");
        el.innerHTML = msg;
        el.className = "text-danger mt-2 d-block";
    }


    function hideWarning() {
        const el = document.getElementById("promoMessage");
        if (el && el.textContent.includes("Minimum rental")) {
            el.innerHTML = "";
            el.className = "d-none";
        }
    }


    // ✅ ĐỔI: formatUSD() → formatVND()
    function formatVND(amount) {
        return Math.round(amount).toLocaleString('vi-VN') + ' ₫';
    }


    function updateDisplay(total) {
        console.log("🔄 updateDisplay() - total:", total);
        document.getElementById("priceValue").textContent = formatVND(total);
        document.getElementById("discount").textContent = formatVND(0);
        document.getElementById("finalPrice").textContent = formatVND(total);
        document.getElementById("finalCalculatedPrice").value = Math.round(total);
    }


    // =============== PROMO CODE ===============


    function applyPromoCode(code) {
        const total = calculateTotal();


        const msg = document.getElementById("promoMessage");
        const contextPath = "${pageContext.request.contextPath}";


        console.log("🎫 applyPromoCode() - code:", code, "total:", total);


        if (!code) {
            msg.innerHTML = "⚠️ Please enter a promo code!";
            msg.className = "text-danger mt-2 d-block";
            return;
        }


        msg.innerHTML = "Checking code...";
        msg.className = "text-info mt-2 d-block";


        document.getElementById("priceValue").textContent = formatVND(total);


        fetch(contextPath + "/check-promo?code=" + encodeURIComponent(code) + "&total=" + total)
            .then(res => res.json())
            .then(data => {
                console.log("📥 Promo API response:", data);


                if (data.error) {
                    msg.innerHTML = "❌ " + data.error;
                    msg.className = "text-danger mt-2 d-block";
                    appliedPromo = null;
                    resetPromoDisplay(total);
                } else if (data.success) {
                    msg.innerHTML = `✅ Coupon <b>${code}</b> applied: -${data.rate}%`;
                    msg.className = "text-success mt-2 d-block";
                    appliedPromo = { code: code, rate: data.rate };
                    updatePriceDisplay(data.discount, data.finalPrice);
                }
            })
            .catch(err => {
                console.error("❌ Promo API error:", err);
                msg.innerHTML = "❌ Could not verify promo code.";
                msg.className = "text-danger mt-2 d-block";
            });
    }


    function updatePriceDisplay(discount, finalPrice) {
        console.log("💵 updatePriceDisplay() - discount:", discount, "finalPrice:", finalPrice);
        document.getElementById("discount").textContent = formatVND(discount);
        document.getElementById("finalPrice").textContent = formatVND(finalPrice);
        document.getElementById("calculatedDiscount").value = Math.round(discount);
        document.getElementById("appliedPromoCode").value = appliedPromo ? appliedPromo.code : "";
        document.getElementById("finalCalculatedPrice").value = Math.round(finalPrice);
    }


    function resetPromoDisplay(total) {
        console.log("🔄 resetPromoDisplay() - total:", total);
        document.getElementById("discount").textContent = formatVND(0);
        document.getElementById("finalPrice").textContent = formatVND(total);
        document.getElementById("calculatedDiscount").value = "0";
        document.getElementById("appliedPromoCode").value = "";
        document.getElementById("finalCalculatedPrice").value = Math.round(total);
    }


    // =============== EVENT HANDLING ===============


    function updatePriceOnChange() {
        console.log("🔄 Date/Time changed");
        const total = calculateTotal();


        if (appliedPromo && appliedPromo.code) {
            console.log("🔄 Re-applying promo:", appliedPromo.code);
            applyPromoCode(appliedPromo.code);
        } else {
            updateDisplay(total);
        }
    }


    document.addEventListener("DOMContentLoaded", function () {
        console.log("✅ DOM loaded - ORIGINAL_PRICE_PER_DAY:", ORIGINAL_PRICE_PER_DAY);


        // Listen to date/time changes
        const elements = [
            'input[name="startDate"]',
            'input[name="endDate"]',
            'select[name="pickupTime"]',
            'select[name="dropoffTime"]'
        ];


        elements.forEach(sel => {
            const el = document.querySelector(sel);
            if (el) {
                el.addEventListener("change", updatePriceOnChange);
            }
        });


        // Apply promo button
        document.getElementById("applyPromo").addEventListener("click", function () {
            const code = document.getElementById("promoCode").value.trim();
            applyPromoCode(code);
        });


        // Enter key on promo input
        document.getElementById("promoCode").addEventListener("keypress", function (e) {
            if (e.key === "Enter") {
                e.preventDefault();
                document.getElementById("applyPromo").click();
            }
        });


        // Initial load
        setTimeout(() => {
            const total = calculateTotal();
            if (appliedPromo && appliedPromo.code) {
                applyPromoCode(appliedPromo.code);
            } else {
                updateDisplay(total);
            }
        }, 100);
        // =============== LOADING SPINNER + DISABLE BUTTON ===============
        const bookingForm = document.querySelector('.booking-form');
        const submitButton = bookingForm.querySelector('button[type="submit"]');
        const originalButtonText = submitButton.innerHTML;


        bookingForm.addEventListener('submit', function(e) {
            // Disable button
            submitButton.disabled = true;


            // Show spinner
            submitButton.innerHTML = '<i class="fa fa-spinner fa-spin"></i> Creating booking...';
        });
    });


    // Enable button lại nếu có lỗi từ server
    <c:if test="${not empty error}">
    window.addEventListener('load', function() {
        const btn = document.querySelector('.booking-form button[type="submit"]');
        if (btn) {
            btn.disabled = false;
            btn.innerHTML = 'Booking Now';
        }
    });
    </c:if>
</script>


</body>
</html>
