<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<fmt:setLocale value="vi_VN" />
<!DOCTYPE html>
<html lang="vi">
<head>
    <jsp:include page="../common/carOwner/_headOwner.jsp"/>
    <title>Thông tin chủ xe - ${owner.fullName}</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

</head>

<body>
<div id="wrapper">
    <jsp:include page="../common/carOwner/_headerOwner.jsp"/>

    <div class="no-bottom no-top zebra" id="content">
        <div id="top"></div>

        <!-- ========== HEADER ========== -->
        <section id="subheader" class="jarallax text-light">
            <img src="${pageContext.request.contextPath}/images/background/14.jpg" class="jarallax-img" alt="">
            <div class="center-y relative text-center">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center">
                            <h1>Chủ xe: ${owner.fullName}</h1>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- ========== THÔNG TIN CHỦ XE ========== -->
        <section class="bg-gray-100 py-5">
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-lg-8">
                        <div class="card card-profile text-center p-4 shadow-sm">
                            <div class="profile-avatar-container mb-3">
                                <img src="${pageContext.request.contextPath}/${owner.profileImage != null ? owner.profileImage : 'images/default-avatar.png'}"
                                     alt="${owner.fullName}" class="profile-avatar rounded-circle"
                                     style="width:150px; height:150px; object-fit:cover;">
                            </div>

                            <h2 class="fw-semibold mb-3">${owner.fullName}</h2>

                            <div class="text-start px-5">
                                <p><strong>Số điện thoại:</strong> ${owner.phone}</p>
                                <p><strong>Ngày sinh:</strong> ${owner.dob}</p>
                                <p><strong>Giới tính:</strong> ${owner.gender}</p>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- ========== DANH SÁCH XE CỦA CHỦ XE ========== -->
        <section id="section-cars" class="py-5">
            <div class="container">
                <h2 class="text-center mb-4">Xe của ${owner.fullName}</h2>
                <div class="row">
                    <c:if test="${empty carList}">
                        <div class="col-12 text-center">
                            <div class="alert alert-warning">Chủ xe hiện chưa đăng xe nào.</div>
                        </div>
                    </c:if>

                    <c:forEach var="car" items="${carList}">
                        <div class="col-lg-4 col-md-6 mb30">
                            <div class="de-item">
                                <div class="d-img">
                                    <c:choose>
                                        <c:when test="${not empty car.imageUrl}">
                                            <img src="${pageContext.request.contextPath}/${car.imageUrl}"
                                                 class="img-fluid"
                                                 alt="${car.model}">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/images/default.jpg"
                                                 class="img-fluid"
                                                 alt="No Image">
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <div class="d-info">
                                    <div class="d-text">
                                        <h4>${car.brand} ${car.model}</h4>

                                        <div class="row g-2 my-3">
                                            <div class="col-6"><i class="fa fa-users text-muted me-2"></i>${car.capacity} chỗ</div>
                                            <div class="col-6"><i class="fa fa-cogs text-muted me-2"></i>${car.transmission}</div>
                                            <div class="col-6"><i class="fa fa-gas-pump text-muted me-2"></i>${car.fuelType}</div>
                                            <div class="col-6"><i class="fa fa-car text-muted me-2"></i>${car.carTypeName}</div>
                                        </div>

                                        <hr class="my-2">
                                        <div class="d-flex justify-content-between align-items-center">
                                            <div>
                                                <span class="fs-6 text-muted">Giá thuê từ</span>
                                                <h5 class="fw-bold mb-0 text-success">
                                                    <fmt:formatNumber value="${car.pricePerDay}" type="number" groupingUsed="true"
                                                                      minFractionDigits="0" maxFractionDigits="0"/> ₫/ngày
                                                </h5>
                                            </div>
                                            <a class="btn-main"
                                               href="${pageContext.request.contextPath}/car-single?id=${car.carId}">
                                                Rent Now
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <section class="py-5">
                    <div class="container">
                        <h4 class="mb-4 text-center">Tất cả đánh giá từ khách hàng</h4>

                        <!-- Bộ lọc -->
                        <form method="get" class="row g-2 mb-4 justify-content-center">
                            <input type="hidden" name="id" value="${owner.userId}">
                            <div class="col-md-3">
                                <select name="rating" class="form-select">
                                    <option value="">Tất cả sao</option>
                                    <c:forEach var="r" begin="1" end="5">
                                        <option value="${r}" ${ratingFilter == r ? 'selected' : ''}>${r} sao</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-3">
                                <input type="text" name="car" class="form-control" placeholder="Tìm theo tên xe"
                                       value="${carFilter}">
                            </div>
                            <div class="col-md-2">
                                <button class="btn-main w-100" type="submit">Lọc</button>
                            </div>
                        </form>

                        <!-- Danh sách review -->
                        <c:if test="${empty reviewList}">
                            <div class="alert alert-warning text-center">Chưa có đánh giá nào.</div>
                        </c:if>

                        <c:forEach var="r" items="${reviewList}">
                            <div class="card mb-3 shadow-sm">
                                <div class="card-body">
                                    <div class="d-flex align-items-center mb-2">
                                        <img src="${pageContext.request.contextPath}/${empty r.profileImage ? 'images/default-avatar.png' : r.profileImage}"
                                             alt="${r.fullName}"
                                             class="rounded-circle me-3 shadow-sm"
                                             style="width:48px; height:48px; object-fit:cover;">

                                        <div>
                                            <h6 class="mb-0 fw-semibold">${r.fullName}</h6>
                                            <small class="text-muted">${r.carModel}</small><br>
                                            <small class="text-muted">
                                                    ${fn:replace(r.review.createdAt, 'T', ' ')}
                                            </small>
                                        </div>
                                    </div>

                                    <div class="mb-2">
                                        <c:forEach begin="1" end="${r.review.rating}">
                                            <i class="fa fa-star text-warning"></i>
                                        </c:forEach>
                                    </div>

                                    <p class="mb-0" style="color:#374151;">
                                        <c:choose>
                                            <c:when test="${not empty r.review.comment}">
                                                ${r.review.comment}
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-muted fst-italic">Người dùng không để lại bình luận.</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                </div>
                            </div>
                        </c:forEach>

                        <!-- ✅ Phân trang có nút Trước / Sau -->
                        <c:if test="${totalPages > 1}">
                            <nav class="mt-4">
                                <ul class="pagination justify-content-center">

                                    <!-- Nút Trang Trước -->
                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a class="page-link"
                                           href="?id=${owner.userId}&page=${currentPage - 1}&rating=${ratingFilter}&car=${carFilter}">
                                            &laquo; Trước
                                        </a>
                                    </li>

                                    <!-- Danh sách trang -->
                                    <c:forEach var="i" begin="1" end="${totalPages}">
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link"
                                               href="?id=${owner.userId}&page=${i}&rating=${ratingFilter}&car=${carFilter}">
                                                    ${i}
                                            </a>
                                        </li>
                                    </c:forEach>

                                    <!-- Nút Trang Sau -->
                                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                        <a class="page-link"
                                           href="?id=${owner.userId}&page=${currentPage + 1}&rating=${ratingFilter}&car=${carFilter}">
                                            Sau &raquo;
                                        </a>
                                    </li>

                                </ul>
                            </nav>
                        </c:if>




                        <!-- Nút quay lại -->
                        <div class="text-center mt-4">
                            <c:set var="backCarId" value="${not empty carId ? carId : sessionScope.lastViewedCarId}" />
                            <a href="${pageContext.request.contextPath}/car-single?id=${backCarId}" class="btn-main">
                                ← Quay lại xe đã chọn
                            </a>
                        </div>
                    </div>
                </section>
            </div>

            <jsp:include page="../common/carOwner/_footer_scriptsOwner.jsp"/>
    </div>
</body>
</html>
