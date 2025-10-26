<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="dao.implement.CarDAO, model.CarViewModel, java.util.List" %>



<%
    CarDAO carDAO = new CarDAO();

    String name = request.getParameter("name");
    String brand = request.getParameter("brand");
    String typeParam = request.getParameter("type");
    String capacity = request.getParameter("capacity");
    String fuel = request.getParameter("fuel");
    String price = request.getParameter("price");

    int currentPage = 1;
    int pageSize = 9; // Giảm pageSize để phù hợp với layout 3 cột
    if(request.getParameter("page") != null){
        try { currentPage = Integer.parseInt(request.getParameter("page")); } catch(Exception e){ currentPage = 1; }
    }

    List<CarViewModel> carList = carDAO.findCars(name, brand, typeParam, capacity, fuel, price, currentPage, pageSize);
    int totalCars = carDAO.countCars(name, brand, typeParam, capacity, fuel, price);
    int totalPages = (int)Math.ceil(totalCars*1.0 / pageSize);


    List<String> brands = carDAO.getAllBrands();
    List<String> types = carDAO.getAllTypes();
    List<Integer> capacities = carDAO.getAllCapacities();
    List<String> fuels = carDAO.getAllFuelTypes();

    request.setAttribute("carList", carList);
    request.setAttribute("brands", brands);
    request.setAttribute("types", types);
    request.setAttribute("capacities", capacities);
    request.setAttribute("fuels", fuels);
    request.setAttribute("currentPage", currentPage);
    request.setAttribute("totalPages", totalPages);
%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <title>Rentaly - Cars List</title>
        <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="image/gif" sizes="16x16">
        <meta content="width=device-width, initial-scale=1.0" name="viewport">

        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" id="bootstrap">
        <link href="${pageContext.request.contextPath}/css/mdb.min.css" rel="stylesheet" type="text/css" id="mdb">
        <link href="${pageContext.request.contextPath}/css/plugins.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/css/coloring.css" rel="stylesheet" type="text/css">
        <link id="colors" href="${pageContext.request.contextPath}/css/colors/scheme-01.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

    </head>

    <body>
        <div id="wrapper">
            <div id="de-preloader"></div>
            <header class="transparent scroll-light has-topbar">
                <div id="topbar" class="topbar-dark text-light">
                    <div class="container">
                        <div class="topbar-left xs-hide">
                            <div class="topbar-widget">
                                <div class="topbar-widget"><a href="#"><i class="fa fa-phone"></i>+208 333 9296</a></div>
                                <div class="topbar-widget"><a href="#"><i class="fa fa-envelope"></i>contact@rentaly.com</a></div>
                                <div class="topbar-widget"><a href="#"><i class="fa fa-clock-o"></i>Mon - Fri 08.00 - 18.00</a></div>
                            </div>
                        </div>
                        <div class="topbar-right">

                        </div>
                        <div class="clearfix"></div>
                    </div>
                </div>
                <div class="container">
                    <div class="row">


                        <div class="col-md-12 mt-2">
                            <div class="de-flex sm-pt10">
                                <div class="de-flex-col">
                                    <div class="de-flex-col">
                                        <div id="logo">
                                            <a href="${pageContext.request.contextPath}/home">
                                                <img class="logo-1" src="${pageContext.request.contextPath}/images/logo-light.png" alt="">
                                                <img class="logo-2" src="${pageContext.request.contextPath}/images/logo.png" alt="">
                                            </a>
                                        </div>
                                    </div>
                                </div>
                                <div class="de-flex-col header-col-mid">

                                </div>
                                <div class="de-flex-col">
                                    <div class="menu_side_area">
                                        <c:choose>
                                            <c:when test="${not empty sessionScope.user}">
                                                <!-- USER MENU -->
                                                <div id="myUserMenu" class="my-user-menu">
                                                    <button id="myUserBtn" class="my-user-btn" type="button" aria-haspopup="true" aria-expanded="false" title="Tài khoản">
                                                        <i class="fa fa-user" aria-hidden="true"></i>
                                                    </button>

                                                    <div class="my-user-dropdown" role="menu" aria-labelledby="myUserBtn">
                                                        <c:choose>
                                                            <%-- Giả sử: 1 = Admin --%>
                                                            <c:when test="${sessionScope.user.roleId == 1}">
                                                                <a class="menu-item" href="${pageContext.request.contextPath}/admin/dashboard" role="menuitem">Tài khoản Admin</a>
                                                            </c:when>

                                                            <%-- Giả sử: 2 = Car Owner --%>
                                                            <c:when test="${sessionScope.user.roleId == 2}">
                                                                <a class="menu-item" href="${pageContext.request.contextPath}/owner/profile" role="menuitem">Tài khoản Chủ xe</a>
                                                            </c:when>

                                                            <%-- Giả sử: 3 = Customer --%>
                                                            <c:when test="${sessionScope.user.roleId == 3}">
                                                                <a class="menu-item" href="${pageContext.request.contextPath}/customer/customerDashboard" role="menuitem">Tài khoản của tôi</a>
                                                            </c:when>

                                                            <%-- Trường hợp mặc định nếu không khớp role nào --%>
                                                            <c:otherwise>
                                                                <a class="menu-item" href="${pageContext.request.contextPath}/home" role="menuitem">Trang chủ</a>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <a class="menu-item" href="${pageContext.request.contextPath}/change-password" role="menuitem">Change Password</a>
                                                        <a class="menu-item" href="${pageContext.request.contextPath}/logout" role="menuitem">Sign Out</a>
                                                    </div>
                                                </div>

                                                <style>
                                                    .menu_side_area, .de-flex-col, .de-flex {
                                                        overflow: visible !important;
                                                    }

                                                    .my-user-menu {
                                                        position: relative;
                                                        display: inline-block;
                                                        vertical-align: middle;
                                                    }

                                                    .my-user-btn {
                                                        width: 44px;
                                                        height: 44px;
                                                        border-radius: 50%;
                                                        border: none;
                                                        background: #f2f2f2;
                                                        display: flex;
                                                        align-items: center;
                                                        justify-content: center;
                                                        cursor: pointer;
                                                        padding: 0;
                                                    }

                                                    .my-user-btn:focus {
                                                        outline: 2px solid #6ea8ff;
                                                        outline-offset: 2px;
                                                    }

                                                    .my-user-btn i {
                                                        font-size: 18px;
                                                        color: #333;
                                                    }

                                                    .my-user-dropdown {
                                                        position: absolute;
                                                        top: calc(100% + 6px);
                                                        right: 0;
                                                        min-width: 160px;
                                                        background: #fff;
                                                        border-radius: 8px;
                                                        box-shadow: 0 6px 18px rgba(0,0,0,0.12);
                                                        padding: 6px 0;
                                                        z-index: 9999;
                                                        opacity: 0;
                                                        visibility: hidden;
                                                        transform: translateY(-6px);
                                                        transition: opacity .12s ease, transform .12s ease, visibility .12s;
                                                        pointer-events: none;
                                                    }

                                                    .my-user-dropdown .menu-item {
                                                        display: block;
                                                        padding: 10px 14px;
                                                        color: #333;
                                                        text-decoration: none;
                                                        font-size: 14px;
                                                        white-space: nowrap;
                                                    }

                                                    .my-user-dropdown .menu-item:hover {
                                                        background: #f6f6f6;
                                                    }

                                                    .my-user-menu.open .my-user-dropdown,
                                                    .my-user-menu:hover .my-user-dropdown {
                                                        opacity: 1;
                                                        visibility: visible;
                                                        transform: translateY(0);
                                                        pointer-events: auto;
                                                    }
                                                </style>

                                                <script>
                                                    (function () {
                                                        var menu = document.getElementById('myUserMenu');
                                                        var btn = document.getElementById('myUserBtn');
                                                        var hideTimeout = null;

                                                        btn.addEventListener('click', function (e) {
                                                            e.stopPropagation();
                                                            var isOpen = menu.classList.contains('open');
                                                            if (isOpen) {
                                                                menu.classList.remove('open');
                                                                btn.setAttribute('aria-expanded', 'false');
                                                            } else {
                                                                menu.classList.add('open');
                                                                btn.setAttribute('aria-expanded', 'true');
                                                            }
                                                        });

                                                        menu.addEventListener('mouseenter', function () {
                                                            if (hideTimeout) {
                                                                clearTimeout(hideTimeout);
                                                                hideTimeout = null;
                                                            }
                                                            menu.classList.add('open');
                                                            btn.setAttribute('aria-expanded', 'true');
                                                        });

                                                        menu.addEventListener('mouseleave', function () {
                                                            if (hideTimeout)
                                                                clearTimeout(hideTimeout);
                                                            hideTimeout = setTimeout(function () {
                                                                menu.classList.remove('open');
                                                                btn.setAttribute('aria-expanded', 'false');
                                                            }, 250);
                                                        });

                                                        document.addEventListener('click', function (e) {
                                                            if (!menu.contains(e.target)) {
                                                                menu.classList.remove('open');
                                                                btn.setAttribute('aria-expanded', 'false');
                                                            }
                                                        });

                                                        document.addEventListener('keydown', function (e) {
                                                            if (e.key === 'Escape') {
                                                                menu.classList.remove('open');
                                                                btn.setAttribute('aria-expanded', 'false');
                                                            }
                                                        });
                                                    })();
                                                </script>
                                            </c:when>




                                            <c:otherwise>
                                                <a href="${pageContext.request.contextPath}/login" class="btn-main">Sign In</a>
                                                <a href="${pageContext.request.contextPath}/register" class="btn-main">Sign Up</a>
                                            </c:otherwise>
                                        </c:choose>
                                        <span id="menu-btn"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </header>
            <div class="no-bottom no-top" id="content">
                <div id="top"></div>

                <section id="subheader" class="jarallax text-light">
                    <img src="${pageContext.request.contextPath}/images/background/2.jpg" class="jarallax-img" alt="">
                    <div class="center-y relative text-center">
                        <div class="container">
                            <div class="row">
                                <div class="col-md-12 text-center">
                                    <h1>Cars List</h1>
                                </div>
                                <div class="clearfix"></div>
                            </div>
                        </div>
                    </div>
                </section>

                <section id="section-cars">
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-3">
                                <div class="item_filter_group">
                                    <h4>Filter Cars</h4>
                                    <div class="p-3" data-bgcolor="#f5f5f5" style="border-radius: 5px;">
                                        <form action="${pageContext.request.contextPath}/cars" method="GET">
                                            <div class="mb-3">
                                                <label class="form-label">Car Name</label>
                                                <input type="text" name="name" class="form-control" value="${param.name}">
                                            </div>

                                            <div class="mb-3">
                                                <label class="form-label">Brand</label>
                                                <select name="brand" class="form-control">
                                                    <option value="">All Brands</option>
                                                    <c:forEach var="b" items="${brands}">
                                                        <option value="${b}" ${b==param.brand?'selected':''}>${b}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>

                                            <div class="mb-3">
                                                <label class="form-label">Type</label>
                                                <select name="type" class="form-control">
                                                    <option value="">All Types</option>
                                                    <c:forEach var="t" items="${typeList}">
                                                        <c:set var="parts" value="${fn:split(t, ':')}" />
                                                        <option value="${parts[0]}" ${parts[0]==param.type ? 'selected' : ''}>
                                                            ${parts[1]}
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </div>


                                            <div class="mb-3">
                                                <label class="form-label">Seats</label>
                                                <select name="capacity" class="form-control">
                                                    <option value="">Any</option>
                                                    <c:forEach var="c" items="${capacities}">
                                                        <option value="${c}" ${c.toString()==param.capacity?'selected':''}>${c}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>

                                            <div class="mb-3">
                                                <label class="form-label">Fuel</label>
                                                <select name="fuel" class="form-control">
                                                    <option value="">Any</option>
                                                    <c:forEach var="f" items="${fuels}">
                                                        <option value="${f}" ${f==param.fuel?'selected':''}>${f}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>

                                            <div class="mb-3">
                                                <label class="form-label">Max Price (VND/day)</label>
                                                <input type="number" name="price" class="form-control" value="${param.price}" min="0">
                                            </div>

                                            <button type="submit" class="btn-main w-100">Apply Filter</button>
                                        </form>
                                    </div>
                                </div>
                            </div>

                            <div class="col-lg-9">
                                <div class="row">
                                    <c:if test="${empty carList}">
                                        <div class="col-12">
                                            <div class="alert alert-warning text-center">No cars found matching your criteria.</div>
                                        </div>
                                    </c:if>

                                    <c:forEach var="car" items="${carList}">
                                        <div class="col-lg-4 col-md-6 mb30">
                                            <div class="de-item">
                                                <div class="d-img">
                                                    <c:choose>
                                                        <c:when test="${not empty car.images}">
                                                            <img src="${pageContext.request.contextPath}/${car.images[0].imageUrl}"
                                                                 class="img-fluid"
                                                                 alt="Ảnh xe ${car.model}">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <img src="${pageContext.request.contextPath}/default.jpg"
                                                                 class="img-fluid"
                                                                 alt="No Image">
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>

                                                <div class="d-info">
                                                    <div class="d-text">
                                                        <%-- DÒNG TIÊU ĐỀ XE --%>
                                                        <h4>${car.model}</h4>

                                                        <%-- LƯỚI THUỘC TÍNH 2X2 --%>
                                                        <div class="row g-2 my-3">
                                                            <div class="col-6">
                                                                <i class="fa fa-users text-muted me-2"></i>${car.capacity} Seats
                                                            </div>
                                                            <div class="col-6">
                                                                <i class="fa fa-cogs text-muted me-2"></i>${car.transmission}
                                                            </div>
                                                            <div class="col-6">
                                                                <i class="fa fa-gas-pump text-muted me-2"></i>${car.fuelType}
                                                            </div>
                                                            <div class="col-6">
                                                                <i class="fa fa-car text-muted me-2"></i>${car.carTypeName}
                                                            </div>
                                                        </div>

                                                        <%-- DẤU GẠCH NGANG PHÂN CÁCH --%>
                                                        <hr class="my-2">

                                                        <%-- PHẦN GIÁ TIỀN VÀ NÚT BẤM --%>
                                                        <div class="d-flex justify-content-between align-items-center">
                                                            <div>
                                                                <span class="fs-6 text-muted">Daily rate from</span>
                                                                <h5 class="fw-bold mb-0">
                                                                    <fmt:formatNumber value="${car.pricePerDay}" type="currency" currencyCode="VND" maxFractionDigits="0"/>
                                                                </h5>
                                                            </div>
                                                            <a class="btn-main" href="${pageContext.request.contextPath}/car-single?id=${car.carId}">Rent Now</a>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>

                                <nav aria-label="Page navigation" class="mt-4">
                                    <ul class="pagination justify-content-center">

                                        <!-- Nút về đầu -->
                                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                            <a class="page-link"
                                               href="cars?page=1&name=${param.name}&brand=${param.brand}&type=${param.type}&capacity=${param.capacity}&fuel=${param.fuel}&price=${param.price}">«</a>
                                        </li>

                                        <c:choose>
                                            <c:when test="${currentPage < 3}">
                                                <c:forEach var="i" begin="1" end="${totalPages < 3 ? totalPages : 3}">
                                                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                                                        <a class="page-link"
                                                           href="cars?page=${i}&name=${param.name}&brand=${param.brand}&type=${param.type}&capacity=${param.capacity}&fuel=${param.fuel}&price=${param.price}">
                                                            ${i}
                                                        </a>
                                                    </li>
                                                </c:forEach>
                                                <c:if test="${totalPages > 3}">
                                                    <li class="page-item disabled"><span class="page-link">...</span></li>
                                                    <li class="page-item">
                                                        <a class="page-link"
                                                           href="cars?page=${totalPages}&name=${param.name}&brand=${param.brand}&type=${param.type}&capacity=${param.capacity}&fuel=${param.fuel}&price=${param.price}">
                                                            ${totalPages}
                                                        </a>
                                                    </li>
                                                </c:if>
                                            </c:when>

                                            <c:otherwise>
                                                <li class="page-item disabled"><span class="page-link">...</span></li>

                                                <c:set var="start" value="${currentPage - 1}" />
                                                <c:set var="end" value="${currentPage + 1}" />
                                                <c:if test="${end > totalPages}">
                                                    <c:set var="end" value="${totalPages}" />
                                                </c:if>

                                                <c:forEach var="i" begin="${start}" end="${end}">
                                                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                                                        <a class="page-link"
                                                           href="cars?page=${i}&name=${param.name}&brand=${param.brand}&type=${param.type}&capacity=${param.capacity}&fuel=${param.fuel}&price=${param.price}">
                                                            ${i}
                                                        </a>
                                                    </li>
                                                </c:forEach>

                                                <c:if test="${end < totalPages}">
                                                    <li class="page-item disabled"><span class="page-link">...</span></li>
                                                    <li class="page-item">
                                                        <a class="page-link"
                                                           href="cars?page=${totalPages}&name=${param.name}&brand=${param.brand}&type=${param.type}&capacity=${param.capacity}&fuel=${param.fuel}&price=${param.price}">
                                                            ${totalPages}
                                                        </a>
                                                    </li>
                                                </c:if>
                                            </c:otherwise>
                                        </c:choose>

                                        <!-- Nút đến cuối -->
                                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                            <a class="page-link"
                                               href="cars?page=${totalPages}&name=${param.name}&brand=${param.brand}&type=${param.type}&capacity=${param.capacity}&fuel=${param.fuel}&price=${param.price}">»</a>
                                        </li>
                                    </ul>

                                    <!-- Ô nhập số trang -->
                                    <div class="d-flex justify-content-center mt-2">
                                        <form method="get" action="cars" class="d-flex">
                                            <input type="hidden" name="name" value="${param.name}">
                                            <input type="hidden" name="brand" value="${param.brand}">
                                            <input type="hidden" name="type" value="${param.type}">
                                            <input type="hidden" name="capacity" value="${param.capacity}">
                                            <input type="hidden" name="fuel" value="${param.fuel}">
                                            <input type="hidden" name="price" value="${param.price}">
                                            <input type="number" name="page" min="1" max="${totalPages}"
                                                   class="form-control form-control-sm" placeholder="Trang..." style="width:80px;">
                                            <button type="submit" class="btn btn-primary btn-sm ms-2">Go</button>
                                        </form>
                                    </div>
                                </nav>



                            </div>
                        </div>
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
                                <p>Where quality meets affordability. We understand the importance of a smooth and enjoyable journey without the burden of excessive costs. That's why we have meticulously crafted our offerings to provide you with top-notch vehicles at minimum expense.</p>
                            </div>
                        </div>
                        <div class="col-lg-3">
                            <div class="widget">
                                <h5>Contact Info</h5>
                                <address class="s1">
                                    <span><i class="id-color fa fa-map-marker fa-lg"></i>08 W 36th St, New York, NY 10001</span>
                                    <span><i class="id-color fa fa-phone fa-lg"></i>+1 333 9296</span>
                                    <span><i class="id-color fa fa-envelope-o fa-lg"></i><a href="mailto:contact@example.com">contact@example.com</a></span>
                                </address>
                            </div>
                        </div>
                        <div class="col-lg-3">
                            <h5>Quick Links</h5>
                            <div class="row">
                                <div class="col-lg-6">
                                    <div class="widget">
                                        <ul>
                                            <li><a href="#">About</a></li>
                                            <li><a href="#">Blog</a></li>
                                            <li><a href="#">Careers</a></li>
                                            <li><a href="#">News</a></li>
                                        </ul>
                                    </div>
                                </div>
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
                                        <a href="#">
                                            Copyright 2025 - Rentaly by Designesia
                                        </a>
                                    </div>
                                    <ul class="menu-simple">
                                        <li><a href="#">Terms & Conditions</a></li>
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
        <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>

    </body>
</html>