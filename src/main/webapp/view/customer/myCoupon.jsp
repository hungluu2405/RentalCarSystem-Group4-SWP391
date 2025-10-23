<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../common/customer/_head.jsp"/>
    <title>My Favorite Cars | Rentaly</title>
</head>

<body>
<div id="wrapper">

    <%-- Header chung (menu, topbar, logo, v.v.) --%>
    <jsp:include page="../common/customer/_header.jsp"/>

    <!-- Nội dung chính -->
    <div class="no-bottom no-top zebra" id="content">
        <div id="top"></div>

        <!-- Banner trên cùng -->
        <section id="subheader" class="jarallax text-light">
            <img src="${pageContext.request.contextPath}/images/background/14.jpg" class="jarallax-img" alt="">
            <div class="center-y relative text-center">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center">
                            <h1>My Favorite Cars</h1>
                        </div>
                        <div class="clearfix"></div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Phần thân trang -->
        <section id="section-settings" class="bg-gray-100">
            <div class="container">
                <div class="row">

                    <div class="col-lg-3 mb30">
                        <jsp:include page="../common/customer/_sidebar.jsp">
                            <jsp:param name="activePage" value="myCoupon"/>
                        </jsp:include>
                    </div>

                    <!-- Nội dung bên phải -->
                    <div class="col-lg-9">

                        <!-- Item 1 -->
                        <div class="de-item-list no-border mb30">
                            <div class="d-img">
                                <img src="${pageContext.request.contextPath}/images/cars/jeep-renegade.jpg" class="img-fluid" alt="">
                            </div>
                            <div class="d-info">
                                <div class="d-text">
                                    <h4>Jeep Renegade</h4>
                                    <div class="d-atr-group">
                                        <ul class="d-atr">
                                            <li><span>Seats:</span>4</li>
                                            <li><span>Luggage:</span>2</li>
                                            <li><span>Doors:</span>4</li>
                                            <li><span>Fuel:</span>Petrol</li>
                                            <li><span>Horsepower:</span>500</li>
                                            <li><span>Engine:</span>3000</li>
                                            <li><span>Drive:</span>4x4</li>
                                            <li><span>Type:</span>Hatchback</li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div class="d-price">
                                Daily rate from <span>$265</span>
                                <a class="btn-main" href="#">Rent Now</a>
                            </div>
                            <div class="clearfix"></div>
                        </div>

                        <!-- Item 2 -->
                        <div class="de-item-list no-border mb30">
                            <div class="d-img">
                                <img src="${pageContext.request.contextPath}/images/cars/bmw-m5.jpg" class="img-fluid" alt="">
                            </div>
                            <div class="d-info">
                                <div class="d-text">
                                    <h4>BMW M2</h4>
                                    <div class="d-atr-group">
                                        <ul class="d-atr">
                                            <li><span>Seats:</span>4</li>
                                            <li><span>Luggage:</span>2</li>
                                            <li><span>Doors:</span>4</li>
                                            <li><span>Fuel:</span>Petrol</li>
                                            <li><span>Horsepower:</span>500</li>
                                            <li><span>Engine:</span>3000</li>
                                            <li><span>Drive:</span>4x4</li>
                                            <li><span>Type:</span>Hatchback</li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div class="d-price">
                                Daily rate from <span>$244</span>
                                <a class="btn-main" href="#">Rent Now</a>
                            </div>
                            <div class="clearfix"></div>
                        </div>

                        <!-- Item 3 -->
                        <div class="de-item-list no-border mb30">
                            <div class="d-img">
                                <img src="${pageContext.request.contextPath}/images/cars/ferrari-enzo.jpg" class="img-fluid" alt="">
                            </div>
                            <div class="d-info">
                                <div class="d-text">
                                    <h4>Ferrari Enzo</h4>
                                    <div class="d-atr-group">
                                        <ul class="d-atr">
                                            <li><span>Seats:</span>4</li>
                                            <li><span>Luggage:</span>2</li>
                                            <li><span>Doors:</span>4</li>
                                            <li><span>Fuel:</span>Petrol</li>
                                            <li><span>Horsepower:</span>500</li>
                                            <li><span>Engine:</span>3000</li>
                                            <li><span>Drive:</span>4x4</li>
                                            <li><span>Type:</span>Hatchback</li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div class="d-price">
                                Daily rate from <span>$167</span>
                                <a class="btn-main" href="#">Rent Now</a>
                            </div>
                            <div class="clearfix"></div>
                        </div>

                    </div>
                </div>
            </div>
        </section>
    </div>

    <%-- Footer scripts chung --%>
    <jsp:include page="../common/customer/_footer_scripts.jsp"/>

</div>
</body>
</html>
