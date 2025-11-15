<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>

<html lang="vi">

<head>
    <jsp:include page="../common/customer/_head.jsp"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        .btn-main:disabled {
            opacity: 0.7;
            cursor: not-allowed;
        }

        .btn-main .fa-spinner {
            margin-right: 8px;
        }

        /* ============================================

           MODAL OVERLAY & POPUP

           ============================================ */
        .promo-modal-overlay {

            display: none;

            position: fixed;

            top: 0;

            left: 0;

            width: 100%;

            height: 100%;

            background: rgba(0, 0, 0, 0.6);

            backdrop-filter: blur(4px);

            z-index: 99999 !important;

            animation: fadeIn 0.2s ease-out;

        }


        .promo-modal-overlay.show {

            display: flex !important;

            align-items: center;

            justify-content: center;

        }


        @keyframes fadeIn {

            from {
                opacity: 0;
            }

            to {
                opacity: 1;
            }

        }


        .promo-modal {

            background: white;

            border-radius: 16px;

            width: 90%;

            max-width: 600px;

            max-height: 80vh;

            display: flex;

            flex-direction: column;

            animation: slideUp 0.3s ease-out;

            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);

            position: relative;

            z-index: 100000 !important;

        }


        @keyframes slideUp {

            from {

                opacity: 0;

                transform: translateY(50px);

            }

            to {

                opacity: 1;

                transform: translateY(0);

            }

        }


        /* Modal Header */

        .promo-modal-header {

            padding: 20px 24px;

            border-bottom: 1px solid #e5e7eb;

            display: flex;

            justify-content: space-between;

            align-items: center;

        }


        .promo-modal-header h3 {

            margin: 0;

            font-size: 20px;

            font-weight: 600;

            color: #1f2937;

        }


        .promo-modal-close {

            background: none;

            border: none;

            font-size: 24px;

            color: #6b7280;

            cursor: pointer;

            width: 32px;

            height: 32px;

            display: flex;

            align-items: center;

            justify-content: center;

            border-radius: 50%;

            transition: all 0.2s;

        }


        .promo-modal-close:hover {

            background: #f3f4f6;

            color: #1f2937;

        }


        /* Modal Body */

        .promo-modal-body {

            padding: 16px 24px;

            overflow-y: auto;

            flex: 1;

        }


        .promo-modal-body::-webkit-scrollbar {

            width: 6px;

        }


        .promo-modal-body::-webkit-scrollbar-track {

            background: #f3f4f6;

        }


        .promo-modal-body::-webkit-scrollbar-thumb {

            background: #d1d5db;

            border-radius: 10px;

        }


        /* Promo Input Section */

        .promo-input-section {

            margin-bottom: 20px;

            padding: 16px;

            background: #f9fafb;

            border-radius: 12px;

        }


        .promo-input-section label {

            display: block;

            font-size: 14px;

            font-weight: 500;

            color: #374151;

            margin-bottom: 8px;

        }


        .promo-input-group {

            display: flex;

            gap: 8px;

        }


        .promo-input-group input {

            flex: 1;

            padding: 10px 14px;

            border: 1px solid #d1d5db;

            border-radius: 8px;

            font-size: 14px;

        }


        .promo-input-group input:focus {

            outline: none;

            border-color: #10b981;

            box-shadow: 0 0 0 3px rgba(16, 185, 129, 0.1);

        }


        .promo-input-group button {

            padding: 10px 20px;

            background: #10b981;

            color: white;

            border: none;

            border-radius: 8px;

            font-weight: 500;

            cursor: pointer;

            transition: all 0.2s;

        }


        .promo-input-group button:hover {

            background: #059669;

        }


        /* Promo Card */

        .promo-card {

            background: white;

            border: 1px solid #e5e7eb;

            border-radius: 12px;

            padding: 16px;

            margin-bottom: 12px;

            cursor: pointer;

            transition: all 0.2s;

            display: flex;

            align-items: center;

            gap: 16px;

        }


        .promo-card:hover {

            border-color: #10b981;

            box-shadow: 0 4px 12px rgba(16, 185, 129, 0.1);

            transform: translateY(-2px);

        }


        .promo-card.disabled {

            opacity: 0.5;

            cursor: not-allowed;

            background: #f9fafb;

        }


        .promo-card.disabled:hover {

            transform: none;

            box-shadow: none;

            border-color: #e5e7eb;

        }


        .promo-icon {

            width: 48px;

            height: 48px;

            border-radius: 50%;

            background: linear-gradient(135deg, #10b981 0%, #059669 100%);

            display: flex;

            align-items: center;

            justify-content: center;

            flex-shrink: 0;

        }


        .promo-icon.disabled {

            background: #d1d5db;

        }


        .promo-icon i {

            color: white;

            font-size: 20px;

        }


        .promo-info {

            flex: 1;

            min-width: 0;

        }


        .promo-code-title {

            font-size: 16px;

            font-weight: 700;

            color: #1f2937;

            margin-bottom: 4px;

        }


        .promo-description {

            font-size: 13px;

            color: #6b7280;

            margin-bottom: 6px;

        }


        .promo-expiry-info {

            font-size: 12px;

            color: #ef4444;

            display: flex;

            align-items: center;

            gap: 4px;

        }


        .promo-expiry-info.expired {

            color: #9ca3af;

        }


        .promo-not-available {

            font-size: 12px;

            color: #9ca3af;

            display: flex;

            align-items: center;

            gap: 4px;

        }


        .promo-action button {

            padding: 8px 20px;

            background: #10b981;

            color: white;

            border: none;

            border-radius: 8px;

            font-size: 14px;

            font-weight: 500;

            cursor: pointer;

            transition: all 0.2s;

        }


        .promo-action button:hover {

            background: #059669;

            transform: scale(1.05);

        }


        .promo-action button:disabled {

            background: #d1d5db;

            cursor: not-allowed;

            transform: none;

        }


        /* Empty State */

        .promo-empty-state {

            text-align: center;

            padding: 40px 20px;

        }


        .promo-empty-state i {

            font-size: 64px;

            color: #d1d5db;

            margin-bottom: 16px;

        }


        .promo-empty-state h4 {

            font-size: 16px;

            color: #6b7280;

            margin-bottom: 8px;

        }


        .promo-empty-state p {

            font-size: 14px;

            color: #9ca3af;

        }


        /* Loading */

        .promo-loading {

            text-align: center;

            padding: 40px 20px;

        }


        .promo-loading i {

            font-size: 32px;

            color: #10b981;

        }


        /* Responsive */

        @media (max-width: 768px) {

            .promo-modal {

                width: 95%;

                max-height: 90vh;

            }


            .promo-card {

                flex-direction: column;

                align-items: flex-start;

            }


            .promo-action {

                width: 100%;

            }


            .promo-action button {

                width: 100%;

            }

        }

        .favourite-star-inline {

            display: inline-flex;

            align-items: center;

            justify-content: center;

            background: white;

            border: 2px solid #ffc107;

            width: 46px;

            height: 46px;

            border-radius: 50%;

            cursor: pointer;

            transition: all 0.3s;

            margin-left: 15px;

            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

        }



        .favourite-star-inline:hover {

            transform: scale(1.1);

            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);

        }



        .favourite-star-inline i {

            font-size: 20px;

            color: #ffc107;

            transition: all 0.3s;

        }



        .favourite-star-inline.is-favourite i {

            color: #ffc107;

        }



        .favourite-star-inline:not(.is-favourite) i {

            color: #ddd;

        }



        .car-title-with-star {

            display: flex;

            align-items: center;

            flex-wrap: wrap;

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

                        <div class="car-title-with-star mb-3">

                            <h3 class="fw-bold mb-0">${car.model}</h3>



                            <%-- Nút Favourite Star (chỉ hiển thị cho Customer đã login) --%>

                            <c:if test="${not empty sessionScope.user && sessionScope.user.roleId == 3}">

                                <button class="favourite-star-inline ${isFavourite ? 'is-favourite' : ''}"

                                        data-car-id="${car.carId}"

                                        onclick="toggleFavourite(${car.carId}, this)">

                                    <i class="${isFavourite ? 'fas' : 'far'} fas fa-star"></i>

                                </button>

                            </c:if>

                        </div>


                        <!-- ✅ VND FORMAT -->

                        <div class="de-price text-center border rounded p-3 bg-white shadow-sm mb-4">

                            <span class="text-muted">Giá theo ngày</span>

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

                            <div class="d-row mb-2"><span class="d-title">Loại:</span><span
                                    class="d-value">${car.carTypeName}</span></div>

                            <div class="d-row mb-2"><span class="d-title">Số ghế:</span><span
                                    class="d-value">${car.capacity}</span></div>

                            <div class="d-row mb-2"><span class="d-title">Truyền Động:</span><span
                                    class="d-value">${car.transmission}</span></div>

                            <div class="d-row"><span class="d-title">Nhiên Liệu:</span><span
                                    class="d-value">${car.fuelType}</span></div>

                        </div>

                    </div>


                    <div class="col-lg-4">

                        <h5 class="fw-bold mb-3">Thuê Xe Này</h5>


                        <form action="${pageContext.request.contextPath}/booking" method="post"

                              class="booking-form p-3 rounded shadow-sm bg-light border">


                            <input type="hidden" name="carId" value="${car.carId}"/>

                            <input type="hidden" id="calculatedDiscount" name="calculatedDiscount"
                                   value="${input_calculatedDiscount != null ? input_calculatedDiscount : 0}">

                            <input type="hidden" id="appliedPromoCode" name="appliedPromoCode"
                                   value="${input_appliedPromoCode}">

                            <input type="hidden" id="finalCalculatedPrice" name="finalCalculatedPrice"
                                   value="${input_finalCalculatedPrice != null ? input_finalCalculatedPrice : car.pricePerDay}">

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

                                        <option value="" disabled ${empty input_pickupTime ? 'selected' : ''}>-- Giờ
                                            --
                                        </option>

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

                                        <option value="" disabled ${empty input_dropoffTime ? 'selected' : ''}>-- Giờ
                                            --
                                        </option>

                                        <c:forEach var="hour" begin="6" end="22">

                                            <c:set var="timeValue" value="${hour < 10 ? '0' : ''}${hour}:00"/>

                                            <option value="${timeValue}" ${input_dropoffTime == timeValue ? 'selected' : ''}>${timeValue}</option>

                                        </c:forEach>

                                    </select>

                                </div>

                            </div>


                            <div class="form-group mb-2">

                                <label class="mb-1 small">Địa Điểm</label>

                                <div class="p-2 border rounded bg-white small">

                                    <i class="fa fa-map-marker text-success me-2"></i>

                                    <input type="hidden" name="location"
                                           value="${input_location != null ? input_location : car.location}">

                                    ${car.location}

                                </div>

                                <small class="text-muted">

                                    Xe chỉ có thể được nhận và trả tại địa chỉ cố định này.</small>

                            </div>


                            <div class="form-group mb-3">

                                <h6 class="fw-bold mb-2">Khuyến Mãi</h6>


                                <!-- Input hiển thị mã đã chọn -->

                                <div class="input-group input-group-sm">


                                    <button type="button" id="openPromoModal" class="btn btn-success btn-sm">

                                        <i class="fa fa-ticket"></i> Chọn mã

                                    </button>

                                    <button type="button" id="removePromo" class="btn btn-danger btn-sm"
                                            style="display: none;">

                                        <i class="fa fa-times"></i>

                                    </button>

                                </div>


                                <small id="promoMessage" class="mt-2 d-block">

                                    <c:if test="${not empty input_appliedPromoCode && empty error}">

                                        <span class="text-success">✅ Mã giảm giá: ${input_appliedPromoCode}</span>

                                    </c:if>

                                </small>

                            </div>


                            <!-- ✅ VND FORMAT - PHẦN GIÁ -->

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

           <fmt:formatNumber
                   value="${input_finalCalculatedPrice != null ? input_finalCalculatedPrice : car.pricePerDay}"

                   type="number"

                   groupingUsed="true"

                   minFractionDigits="0"

                   maxFractionDigits="0"/> ₫

       </span>

                                </div>

                            </div>


                            <button type="submit" class="btn-main btn-fullwidth">Thuê Ngay</button>


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

                <form method="get" action="${pageContext.request.contextPath}/car-single" class="row g-2 mb-4 justify-content-center">

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

                                                            ${r.review.formattedCreatedAt}
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

                        <p>Nơi chất lượng gặp gỡ sự tiết kiệm. Chúng tôi hiểu rằng một chuyến đi trọn vẹn không chỉ cần
                            xe tốt mà còn phải thoải mái và hợp lý về chi phí. Vì thế, Rentaly luôn nỗ lực mang đến cho
                            bạn những chiếc xe chất lượng cao với mức giá tối ưu nhất, giúp bạn tận hưởng hành trình êm
                            ái, an toàn và không lo về chi phí.</p>

                    </div>

                </div>


                <div class="col-lg-3">

                    <div class="widget">

                        <h5>Contact Info</h5>

                        <address class="s1">

                            <span><i class="id-color fa fa-map-marker fa-lg"></i>VR9V+HGF, ĐT427B, Hòa Bình, Thường Tín, Hà Nội, Việt Nam</span>

                            <span><i class="id-color fa fa-phone fa-lg"></i>+84 33 5821918</span>

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


<!-- ✅ MODAL CHỈ CẦN Ở ĐÂY - MỘT LẦN DUY NHẤT -->

<div class="promo-modal-overlay" id="promoModalOverlay">

    <div class="promo-modal">

        <!-- Header -->

        <div class="promo-modal-header">

            <h3>Mã khuyến mãi</h3>

            <button type="button" class="promo-modal-close" id="closePromoModal">

                <i class="fa fa-times"></i>

            </button>

        </div>


        <!-- Body -->

        <div class="promo-modal-body">

            <!-- Manual Input Section -->

            <div class="promo-input-section">

                <label>Nhập mã khuyến mãi:</label>

                <div class="promo-input-group">

                    <input type="text" id="manualPromoInput" placeholder="Nhập mã khuyến mãi">

                    <button type="button" onclick="applyManualPromo()">Áp dụng</button>

                </div>

            </div>


            <!-- Promo List -->

            <div id="promoListContainer">

                <div class="promo-loading">

                    <i class="fa fa-spinner fa-spin"></i>

                    <p class="mt-2 mb-0">Đang tải mã khuyến mãi...</p>

                </div>

            </div>

        </div>

    </div>

</div>

<!-- ✅ HẾT MODAL -->


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

        el.innerHTML = '<span class="text-danger">' + msg + '</span>';

        el.className = "mt-2 d-block";

    }


    function hideWarning() {

        const el = document.getElementById("promoMessage");

        if (el && el.textContent.includes("Minimum rental")) {

            el.innerHTML = "";

            el.className = "mt-2 d-block";

        }

    }


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

            msg.innerHTML = '<span class="text-danger">⚠️ Vui lòng nhập mã khuyến mãi!</span>';

            msg.className = "mt-2 d-block";

            return;

        }


        msg.innerHTML = '<span class="text-info">Đang kiểm tra mã...</span>';

        msg.className = "mt-2 d-block";


        document.getElementById("priceValue").textContent = formatVND(total);


        fetch(contextPath + "/check-promo?code=" + encodeURIComponent(code) + "&total=" + total)

            .then(res => res.json())

            .then(data => {

                console.log("📥 Promo API response:", data);


                if (data.error) {

                    msg.innerHTML = '<span class="text-danger">❌ ' + data.error + '</span>';

                    msg.className = "mt-2 d-block";

                    appliedPromo = null;

                    resetPromoDisplay(total);

                } else if (data.success) {

                    msg.innerHTML = '<span class="text-success">✅ Mã <b>' + code + '</b> đã áp dụng: -' + data.rate + '%</span>';

                    msg.className = "mt-2 d-block";

                    appliedPromo = {code: code, rate: data.rate};

                    updatePriceDisplay(data.discount, data.finalPrice);

                }

            })

            .catch(err => {

                console.error("❌ Promo API error:", err);

                msg.innerHTML = '<span class="text-danger">❌ Không thể xác minh mã khuyến mãi.</span>';

                msg.className = "mt-2 d-block";

            });

    }


    function updatePriceDisplay(discount, finalPrice) {

        console.log("💵 updatePriceDisplay() - discount:", discount, "finalPrice:", finalPrice);

        document.getElementById("discount").textContent = formatVND(discount);

        document.getElementById("finalPrice").textContent = formatVND(finalPrice);

        document.getElementById("calculatedDiscount").value = Math.round(discount);

        document.getElementById("appliedPromoCode").value = appliedPromo ? appliedPromo.code : "";

        document.getElementById("finalCalculatedPrice").value = Math.round(finalPrice);


        // Show remove button

        const removeBtn = document.getElementById("removePromo");

        if (removeBtn && appliedPromo && appliedPromo.code) {

            removeBtn.style.display = "inline-block";

        }

    }


    function resetPromoDisplay(total) {

        console.log("🔄 resetPromoDisplay() - total:", total);

        document.getElementById("discount").textContent = formatVND(0);

        document.getElementById("finalPrice").textContent = formatVND(total);

        document.getElementById("calculatedDiscount").value = "0";

        document.getElementById("appliedPromoCode").value = "";

        document.getElementById("finalCalculatedPrice").value = Math.round(total);


        const removeBtn = document.getElementById("removePromo");

        if (removeBtn) {

            removeBtn.style.display = "none";

        }

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


    // ============================================

    // PROMO MODAL FUNCTIONS (NEW)

    // ============================================

    let availablePromotions = [];


    // Open Modal

    function openPromoModal() {

        const overlay = document.getElementById('promoModalOverlay');

        if (overlay) {

            overlay.classList.add('show');

            loadPromotions();

        }

    }


    // Close Modal

    function closePromoModal() {

        const overlay = document.getElementById('promoModalOverlay');

        if (overlay) {

            overlay.classList.remove('show');

        }

    }


    // ============================================

    // LOAD PROMOTIONS FROM API

    // ============================================

    function loadPromotions() {

        const container = document.getElementById('promoListContainer');

        const contextPath = "${pageContext.request.contextPath}";


        if (!container) return;


        container.innerHTML = '<div class="promo-loading"><i class="fa fa-spinner fa-spin"></i><p class="mt-2 mb-0">Đang tải mã khuyến mãi...</p></div>';


        fetch(contextPath + "/api/promotions")

            .then(res => res.json())

            .then(data => {

                console.log("📥 Promotions loaded:", data);


                if (data.success && data.promotions && data.promotions.length > 0) {

                    availablePromotions = data.promotions;

                    renderPromotions(data.promotions);

                } else {

                    renderEmptyState();

                }

            })

            .catch(err => {

                console.error("❌ Error loading promotions:", err);

                renderErrorState();

            });

    }


    // ============================================

    // RENDER PROMOTIONS

    // ============================================

    function renderPromotions(promotions) {

        const container = document.getElementById('promoListContainer');

        if (!container) return;


        let html = '';

        promotions.forEach(function (promo, index) {

            const endDate = new Date(promo.endDate);

            const today = new Date();

            const isExpired = endDate < today;

            const isAvailable = promo.active && !isExpired;


            const formattedEndDate = endDate.toLocaleDateString('vi-VN');


            let discountText = '';

            if (promo.discountType === 'percent') {

                discountText = 'Giảm ' + promo.discountRate + '%';

            } else {

                discountText = 'Giảm ' + formatVND(promo.discountRate);

            }


            const cardClass = isAvailable ? '' : 'disabled';

            const iconClass = isAvailable ? '' : 'disabled';


            html += '<div class="promo-card ' + cardClass + '" onclick="' + (isAvailable ? 'selectPromo(' + index + ')' : '') + '">';

            html += '<div class="promo-icon ' + iconClass + '">';

            html += '<i class="fa fa-ticket"></i>';

            html += '</div>';

            html += '<div class="promo-info">';

            html += '<div class="promo-code-title">' + promo.code + '</div>';

            html += '<div class="promo-description">' + discountText;

            if (promo.description) {

                html += '. ' + promo.description;

            }

            html += '</div>';


            if (isExpired) {

                html += '<div class="promo-expiry-info expired"><i class="fa fa-clock-o"></i> Đã hết hạn</div>';

            } else {

                html += '<div class="promo-expiry-info"><i class="fa fa-clock-o"></i> Hết hạn: ' + formattedEndDate + '</div>';

            }


            if (!isAvailable && !isExpired) {

                html += '<div class="promo-not-available"><i class="fa fa-info-circle"></i> Mã không khả dụng</div>';

            }


            html += '</div>';

            html += '<div class="promo-action">';

            html += '<button type="button" ' + (!isAvailable ? 'disabled' : '') + '>Áp dụng</button>';

            html += '</div>';

            html += '</div>';

        });


        container.innerHTML = html;

    }


    // ============================================

    // SELECT PROMOTION

    // ============================================

    function selectPromo(index) {

        const promo = availablePromotions[index];


        if (!promo) return;


        console.log("🎫 Selected promo:", promo.code);


        const promoCodeInput = document.getElementById("promoCode");

        if (promoCodeInput) {

            promoCodeInput.value = promo.code;

        }


        const removeBtn = document.getElementById("removePromo");

        if (removeBtn) {

            removeBtn.style.display = "inline-block";

        }


        closePromoModal();

        applyPromoCode(promo.code);

    }


    // ============================================

    // APPLY MANUAL PROMO

    // ============================================

    function applyManualPromo() {

        const manualInput = document.getElementById('manualPromoInput');

        if (!manualInput) return;


        const manualCode = manualInput.value.trim();


        if (!manualCode) {

            alert('Vui lòng nhập mã khuyến mãi!');

            return;

        }


        const promoCodeInput = document.getElementById("promoCode");

        if (promoCodeInput) {

            promoCodeInput.value = manualCode;

        }


        const removeBtn = document.getElementById("removePromo");

        if (removeBtn) {

            removeBtn.style.display = "inline-block";

        }


        closePromoModal();

        applyPromoCode(manualCode);

    }


    // ============================================

    // RENDER EMPTY/ERROR STATES

    // ============================================

    function renderEmptyState() {

        const container = document.getElementById('promoListContainer');

        if (!container) return;


        container.innerHTML = '<div class="promo-empty-state"><i class="fa fa-ticket"></i><h4>Không có mã khuyến mãi</h4><p>Hiện tại không có mã khuyến mãi nào khả dụng</p></div>';

    }


    function renderErrorState() {

        const container = document.getElementById('promoListContainer');

        if (!container) return;


        container.innerHTML = '<div class="promo-empty-state"><i class="fa fa-exclamation-triangle text-warning"></i><h4>Không thể tải mã khuyến mãi</h4><p>Vui lòng thử lại sau</p></div>';

    }
    function toggleFavourite(carId, buttonElement) {

        // Prevent default behavior

        event.preventDefault();

        event.stopPropagation();



        // Check if user is logged in

        const isLoggedIn = ${not empty sessionScope.user};

        if (!isLoggedIn) {

            alert('Vui lòng đăng nhập để sử dụng tính năng này');

            window.location.href = '${pageContext.request.contextPath}/account/login';

            return;

        }



        // Determine if car is currently favourite

        const isFavourite = buttonElement.classList.contains('is-favourite');

        const url = isFavourite

            ? '${pageContext.request.contextPath}/customer/favourite/remove'

            : '${pageContext.request.contextPath}/customer/favourite/add';



        // Show loading state

        buttonElement.disabled = true;



        // Send request

        fetch(url + '?carId=' + carId, {

            method: 'POST',

            headers: {

                'Content-Type': 'application/json'

            }

        })

            .then(response => response.json())

            .then(data => {

                if (data.success) {

                    // Toggle button state

                    buttonElement.classList.toggle('is-favourite');



                    // Toggle icon (far = empty star, fas = filled star)

                    const icon = buttonElement.querySelector('i');

                    if (icon.classList.contains('far')) {

                        icon.classList.remove('far');

                        icon.classList.add('fas');

                    } else {

                        icon.classList.remove('fas');

                        icon.classList.add('far');

                    }



                    // Show success message (optional)

                    // console.log(data.message);

                } else {

                    alert(data.message || 'Có lỗi xảy ra. Vui lòng thử lại.');

                }

            })

            .catch(error => {

                console.error('Error:', error);

                alert('Có lỗi xảy ra. Vui lòng thử lại sau.');

            })

            .finally(() => {

                buttonElement.disabled = false;

            });

    }


    // ============================================

    // DOM CONTENT LOADED

    // ============================================

    document.addEventListener("DOMContentLoaded", function () {

        console.log("✅ DOM loaded - ORIGINAL_PRICE_PER_DAY:", ORIGINAL_PRICE_PER_DAY);


        // Listen to date/time changes

        const elements = [

            'input[name="startDate"]',

            'input[name="endDate"]',

            'select[name="pickupTime"]',

            'select[name="dropoffTime"]'

        ];


        elements.forEach(function (sel) {

            const el = document.querySelector(sel);

            if (el) {

                el.addEventListener("change", updatePriceOnChange);

            }

        });


        // Open promo modal button

        const openModalBtn = document.getElementById('openPromoModal');

        if (openModalBtn) {

            openModalBtn.addEventListener('click', openPromoModal);

        }


        // Close promo modal button

        const closeModalBtn = document.getElementById('closePromoModal');

        if (closeModalBtn) {

            closeModalBtn.addEventListener('click', closePromoModal);

        }


        // Close when clicking overlay

        const modalOverlay = document.getElementById('promoModalOverlay');

        if (modalOverlay) {

            modalOverlay.addEventListener('click', function (e) {

                if (e.target === this) {

                    closePromoModal();

                }

            });

        }


        // Close with ESC key

        document.addEventListener('keydown', function (e) {

            if (e.key === 'Escape') {

                closePromoModal();

            }

        });


        // Manual promo input - Enter key

        const manualPromoInput = document.getElementById('manualPromoInput');

        if (manualPromoInput) {

            manualPromoInput.addEventListener('keypress', function (e) {

                if (e.key === 'Enter') {

                    e.preventDefault();

                    applyManualPromo();

                }

            });

        }


        // Remove promo button

        const removePromoBtn = document.getElementById("removePromo");

        if (removePromoBtn) {

            removePromoBtn.addEventListener("click", function () {

                console.log("🗑️ Removing promo code");


                const promoCodeInput = document.getElementById("promoCode");

                if (promoCodeInput) {

                    promoCodeInput.value = "";

                }


                appliedPromo = null;

                this.style.display = "none";


                const total = calculateTotal();

                resetPromoDisplay(total);


                const msg = document.getElementById("promoMessage");

                if (msg) {

                    msg.innerHTML = "";

                    msg.className = "mt-2 d-block";

                }

            });

        }


        // Initial load

        setTimeout(function () {

            const total = calculateTotal();

            if (appliedPromo && appliedPromo.code) {

                applyPromoCode(appliedPromo.code);

            } else {

                updateDisplay(total);

            }


            const promoInput = document.getElementById("promoCode");

            const removeBtn = document.getElementById("removePromo");

            if (promoInput && removeBtn && promoInput.value.trim() !== "") {

                removeBtn.style.display = "inline-block";

            }

        }, 100);


        // =============== LOADING SPINNER + DISABLE BUTTON ===============

        const bookingForm = document.querySelector('.booking-form');

        if (bookingForm) {

            const submitButton = bookingForm.querySelector('button[type="submit"]');

            if (submitButton) {

                bookingForm.addEventListener('submit', function (e) {

                    submitButton.disabled = true;

                    submitButton.innerHTML = '<i class="fa fa-spinner fa-spin"></i> Creating booking...';

                });

            }

        }

    });


    // Enable button lại nếu có lỗi từ server

    <c:if test="${not empty error}">

    window.addEventListener('load', function () {

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