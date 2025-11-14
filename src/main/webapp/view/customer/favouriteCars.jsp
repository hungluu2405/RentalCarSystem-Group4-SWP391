<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="vi_VN"/>

<!DOCTYPE html>

<html lang="en">

<head>

    <jsp:include page="../common/customer/_head.jsp"/>

    <title>Rentaly - Xe Yêu Thích</title>



    <style>

        /* Style cho nút remove favourite */

        .remove-favourite-btn {

            position: absolute;

            top: 10px;

            right: 10px;

            background: white;

            border: none;

            width: 36px;

            height: 36px;

            border-radius: 50%;

            display: flex;

            align-items: center;

            justify-content: center;

            cursor: pointer;

            transition: all 0.3s;

            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);

            z-index: 10;

        }



        .remove-favourite-btn:hover {

            transform: scale(1.1);

            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);

        }



        .remove-favourite-btn i {

            font-size: 16px;

            color: #ffc107;

        }



        .de-item-list {

            position: relative;

        }



        .de-item-list .d-img {

            position: relative;

        }



        /* Empty state styling */

        .empty-state-container {

            text-align: center;

            padding: 80px 20px;

        }



        .empty-state-container i {

            font-size: 80px;

            color: #ddd;

            margin-bottom: 20px;

        }



        .empty-state-container h3 {

            font-size: 24px;

            font-weight: 600;

            color: #666;

            margin-bottom: 15px;

        }



        .empty-state-container p {

            color: #999;

            margin-bottom: 30px;

            font-size: 16px;

        }

    </style>

</head>



<body>

<div id="wrapper">



    <!-- page preloader begin -->

    <div id="de-preloader"></div>

    <!-- page preloader close -->



    <jsp:include page="../common/customer/_header.jsp"/>



    <div class="no-bottom no-top zebra" id="content">

        <div id="top"></div>



        <!-- section begin -->

        <section id="subheader" class="jarallax text-light">

            <img src="${pageContext.request.contextPath}/images/background/14.jpg" class="jarallax-img" alt="">

            <div class="center-y relative text-center">

                <div class="container">

                    <div class="row">

                        <div class="col-md-12 text-center">

                            <h1>Xe Yêu Thích Của Tôi</h1>

                        </div>

                        <div class="clearfix"></div>

                    </div>

                </div>

            </div>

        </section>

        <!-- section close -->



        <section id="section-settings" class="bg-gray-100">

            <div class="container">

                <div class="row">

                    <!-- Sidebar -->

                    <div class="col-lg-3 mb30">

                        <jsp:include page="../common/customer/_sidebar.jsp">

                            <jsp:param name="activePage" value="favourites"/>

                        </jsp:include>

                    </div>



                    <!-- Main Content -->

                    <div class="col-lg-9">

                        <c:choose>

                            <%-- Trường hợp có xe yêu thích --%>

                            <c:when test="${not empty favouriteCars}">

                                <c:forEach var="car" items="${favouriteCars}">

                                    <div class="de-item-list no-border mb30">

                                        <div class="d-img">

                                                <%-- Hình ảnh xe --%>

                                            <c:set var="imageUrl" value="${carImagesMap[car.carId]}"/>

                                            <c:if test="${empty imageUrl}">

                                                <c:set var="imageUrl" value="/images/cars/default-car.jpg"/>

                                            </c:if>

                                            <img src="${pageContext.request.contextPath}${imageUrl}"

                                                 class="img-fluid" alt="${car.brand} ${car.model}">



                                                <%-- Nút xóa yêu thích --%>

                                            <button class="remove-favourite-btn"

                                                    data-car-id="${car.carId}"

                                                    onclick="removeFavourite(${car.carId})"

                                                    title="Xóa khỏi yêu thích">

                                                <i class="fas fa-star"></i>

                                            </button>

                                        </div>



                                        <div class="d-info">

                                            <div class="d-text">

                                                <h4>${car.brand} ${car.model}</h4>

                                                <div class="d-atr-group">

                                                    <ul class="d-atr">

                                                        <li><span>Số chỗ:</span>${car.capacity}</li>

                                                        <li><span>Truyền động:</span>${car.transmission}</li>

                                                        <li><span>Nhiên liệu:</span>${car.fuelType}</li>

                                                        <li><span>Năm SX:</span>${car.year}</li>

                                                        <li><span>Loại xe:</span>${car.typeName != null ? car.typeName : 'N/A'}</li>

                                                        <li><span>Vị trí:</span>${car.location}</li>

                                                        <li>

                                                            <span>Trạng thái:</span>

                                                            <c:choose>

                                                                <c:when test="${car.availability}">

                                                                    <span class="text-success">Sẵn sàng</span>

                                                                </c:when>

                                                                <c:otherwise>

                                                                    <span class="text-danger">Không khả dụng</span>

                                                                </c:otherwise>

                                                            </c:choose>

                                                        </li>

                                                    </ul>

                                                </div>

                                            </div>

                                        </div>



                                        <div class="d-price">

                                            Giá thuê theo ngày

                                            <span>

                                                <fmt:formatNumber value="${car.pricePerDay}"

                                                                  type="number"

                                                                  groupingUsed="true"

                                                                  minFractionDigits="0"

                                                                  maxFractionDigits="0"/> ₫

                                            </span>

                                            <a class="btn-main" href="${pageContext.request.contextPath}/car-single?id=${car.carId}">

                                                Xem Chi Tiết

                                            </a>

                                        </div>

                                        <div class="clearfix"></div>

                                    </div>

                                </c:forEach>

                            </c:when>



                            <%-- Trường hợp chưa có xe yêu thích --%>

                            <c:otherwise>

                                <div class="card padding40 rounded-5">

                                    <div class="empty-state-container">

                                        <i class="far fa-star"></i>

                                        <h3>Chưa Có Xe Yêu Thích</h3>

                                        <p>Bạn chưa đánh dấu xe nào là yêu thích.<br>

                                            Hãy khám phá và chọn những chiếc xe ưng ý nhất!</p>

                                        <a href="${pageContext.request.contextPath}/cars" class="btn-main">

                                            <i class="fa fa-car me-2"></i> Khám Phá Xe

                                        </a>

                                    </div>

                                </div>

                            </c:otherwise>

                        </c:choose>

                    </div>

                </div>

            </div>

        </section>

    </div>

    <!-- content close -->



    <a href="#" id="back-to-top"></a>



    <!-- footer begin -->

    <footer class="text-light">

        <div class="container">

            <div class="row g-custom-x">

                <div class="col-lg-3">

                    <div class="widget">

                        <h5>About Rentaly</h5>

                        <p>Nơi chất lượng gặp gỡ sự tiết kiệm. Chúng tôi hiểu rằng một chuyến đi trọn vẹn không chỉ cần xe tốt mà còn phải thoải mái và hợp lý về chi phí. Vì thế, Rentaly luôn nỗ lực mang đến cho bạn những chiếc xe chất lượng cao với mức giá tối ưu nhất, giúp bạn tận hưởng hành trình êm ái, an toàn và không lo về chi phí.</p>

                    </div>

                </div>



                <div class="col-lg-3">

                    <div class="widget">

                        <h5>Contact Info</h5>

                        <address class="s1">

                            <span><i class="id-color fa fa-map-marker fa-lg"></i>VR9V+HGF, ĐT427B, Hòa Bình, Thường Tín, Hà Nội, Việt Nam</span>

                            <span><i class="id-color fa fa-phone fa-lg"></i>+84 33 5821918</span>

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

    <!-- footer close -->

</div>



<!-- Javascript Files -->

<script src="${pageContext.request.contextPath}/js/plugins.js"></script>

<script src="${pageContext.request.contextPath}/js/designesia.js"></script>



<script>

    /**

     * Xóa xe khỏi danh sách yêu thích

     */

    function removeFavourite(carId) {

        if (!confirm('Bạn có chắc muốn xóa xe này khỏi danh sách yêu thích?')) {

            return;

        }



        fetch('${pageContext.request.contextPath}/customer/favourite/remove?carId=' + carId, {

            method: 'POST',

            headers: {

                'Content-Type': 'application/json'

            }

        })

            .then(response => response.json())

            .then(data => {

                if (data.success) {

                    // Reload trang để cập nhật danh sách

                    location.reload();

                } else {

                    alert(data.message || 'Không thể xóa xe khỏi danh sách yêu thích');

                }

            })

            .catch(error => {

                console.error('Error:', error);

                alert('Đã xảy ra lỗi. Vui lòng thử lại sau.');
            });
    }
</script>
</body>
</html>