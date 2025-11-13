<%@ page import="dao.implement.CarDAO" %>
<%@ page import="model.CarViewModel" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../common/carOwner/_headOwner.jsp"/>
    <title>Manage My Cars</title>

    <style>
        body {
            background-color: #f7f7f7;
        }

        .dashboard-content {
            background-color: #fff;
            border-radius: 10px;
            padding: 25px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        .car-card {
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 20px;
            margin-bottom: 25px;
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }

        .car-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }

        .car-card img {
            width: 250px;
            height: 160px;
            border-radius: 10px;
            object-fit: cover;
            margin-right: 25px;
            border: 2px solid #eee;
        }

        .car-info {
            flex-grow: 1;
            color: #333;
        }

        .car-info h3 {
            font-weight: 700;
            color: #0d1b2a;
            margin-bottom: 10px;
        }

        .car-info p {
            margin: 2px 0;
        }

        .car-price {
            text-align: right;
            min-width: 150px;
        }

        .car-price h4 {
            font-size: 1.5rem;
            font-weight: 700;
            margin-bottom: 10px;
            color: #111;
        }

        .btn-view {
            background: #00b300;
            color: white;
            border: none;
            padding: 8px 16px;
            border-radius: 5px;
            font-weight: 600;
            text-decoration: none;
            display: inline-block;
        }

        .btn-view:hover {
            background: #009900;
            text-decoration: none;
            color: white;
        }

        h3.fw-bold {
            margin-bottom: 30px;
        }



        @media (max-width: 992px) {

            .car-card {
                display: flex;
                align-items: flex-start;
                gap: 20px;
                margin-bottom: 30px;
                padding: 10px;
                border-bottom: 1px solid #ddd;
            }

            .car-image-wrapper {
                width: 240px;              /* Chiều rộng cố định */
                height: 180px;             /* Chiều cao cố định */
                overflow: hidden;
                border-radius: 12px;
                flex-shrink: 0;
                background-color: #f5f5f5;
            }

            .car-image {
                width: 100%;
                height: 100%;
                object-fit: cover;         /* Cắt cho vừa khung mà không méo ảnh */
                display: block;
            }

        }
        .availability-dot {
            display: inline-block;
            width: 12px;
            height: 12px;
            border-radius: 50%;
            margin-left: 8px;
            background-color: transparent;
            border: 2px solid #999;
            vertical-align: middle;
        }

        .availability-dot.active {
            background-color: #00cc00;
            border-color: #00cc00;
        }

        @media (max-width: 992px) {
            .car-card {
                display: flex;
                align-items: flex-start;
                gap: 20px;
                margin-bottom: 30px;
                padding: 10px;
                border-bottom: 1px solid #ddd;
            }
        }

    </style>
</head>

<body>
<div id="wrapper">
    <div id="de-preloader"></div>

    <!-- Header -->
    <jsp:include page="../common/carOwner/_headerOwner.jsp"/>

    <div class="no-bottom no-top zebra" id="content">
        <div id="top"></div>

        <!-- Banner -->
        <section id="subheader" class="jarallax text-light">
            <img src="${pageContext.request.contextPath}/images/background/14.jpg" class="jarallax-img" alt="">
            <div class="center-y relative text-center">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center">
                            <h1>Quản lí xe của tôi</h1>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- MAIN SECTION -->
        <section id="section-cars" class="bg-gray-100 py-5">
            <div class="container">
                <div class="row">
                    <!-- Sidebar -->
                    <div class="col-lg-3 mb30">
                        <jsp:include page="../common/carOwner/_sidebarOwner.jsp">
                            <jsp:param name="activePage" value="manageMyCar"/>
                        </jsp:include>
                    </div>

                    <!-- MAIN CONTENT -->
                    <div class="col-lg-9">

                        <!-- STATISTICS SECTION -->
                        <div class="row mb-4">
                            <div class="col-lg-4 col-md-6 mb-3">
                                <div class="card padding30 rounded-5 text-center">
                                    <div class="symbol mb40">
                                        <i class="fa id-color fa-2x fa-car"></i>
                                    </div>
                                    <span class="h1 mb0">${totalCars}</span><br>
                                    <span class="text-gray">Tổng số xe của tôi </span>
                                </div>
                            </div>

                            <div class="col-lg-4 col-md-6 mb-3">
                                <div class="card padding30 rounded-5 text-center">
                                    <div class="symbol mb40">
                                        <i class="fa id-color fa-2x fa-check-circle"></i>
                                    </div>
                                    <span class="h1 mb0">${availableCars}</span><br>
                                    <span class="text-gray">Xe sẵn sàng để thuê</span>
                                </div>
                            </div>

                            <div class="col-lg-4 col-md-6 mb-3">
                                <div class="card padding30 rounded-5 text-center">
                                    <div class="symbol mb40">
                                        <i class="fa id-color fa-2x fa-ban"></i>
                                    </div>
                                    <span class="h1 mb0">${unavailableCars}</span><br>
                                    <span class="text-gray">Xe chưa sẵn sàng để thuê</span>
                                </div>
                            </div>
                        </div>

                        <div class="dashboard-content">
                            <h3 class="fw-bold mb-3 text-secondary">
                                <i class="fa fa-car"></i> Gara của tôi
                            </h3>

                            <c:if test="${empty carList}">
                                <p>Bạn chưa có xe nào trong gara.</p>
                            </c:if>

                            <c:forEach var="car" items="${carList}">
                                <div class="car-card">
                                    <div class="car-image-wrapper">
                                        <img src="${pageContext.request.contextPath}/${car.imageUrl}" alt="${car.model}" class="car-image">
                                    </div>

                                        <div class="car-info">
                                        <h3>
                                                ${car.brand} ${car.model}
                                            <c:choose>
                                                <c:when test="${car.availability == 1}">
                                                    <span class="availability-dot active"></span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="availability-dot"></span>
                                                </c:otherwise>
                                            </c:choose>
                                        </h3>

                                        <p><strong>Số ghế:</strong> ${car.capacity}</p>
                                        <p><strong>Nhiên liệu:</strong> ${car.fuelType}</p>
                                        <p><strong>Truyền động:</strong> ${car.transmission}</p>
                                        <p><strong>Loại xe:</strong> ${car.carTypeName}</p>
                                        <p><strong>Địa chỉ:</strong> ${car.location}</p>
                                    </div>

                                    <div class="car-price">
                                        <p>Daily rate from</p>
                                        <h4><fmt:formatNumber value="${car.pricePerDay}"
                                                              type="number" groupingUsed="true"
                                                              minFractionDigits="0" maxFractionDigits="0"/> ₫</h4>
                                        <a href="${pageContext.request.contextPath}/owner/manageCarDetail?carId=${car.carId}"
                                           class="btn-view">Xem chi tiết</a>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>

    <!-- Footer -->
    <jsp:include page="../common/carOwner/_footer_scriptsOwner.jsp"/>
</div>
</body>
</html>

