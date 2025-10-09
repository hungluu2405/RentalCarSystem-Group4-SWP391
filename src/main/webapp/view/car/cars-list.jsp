<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zxx">
    <head>
        <title>Rentaly - Cars List</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="image/gif" sizes="16x16">
        <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
        <style>
            /* Layout danh sách xe */
            .de-item-list {
                display: flex;
                flex-direction: column;
                height: 100%;
                border: 1px solid #e0e0e0;
                border-radius: 10px;
                overflow: hidden;
                transition: transform 0.3s;
                background: #fff;
            }
            .de-item-list:hover {
                transform: translateY(-5px);
                box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            }
            .d-img img {
                width: 100%;
                height: 200px;
                object-fit: cover;
            }
            .d-info {
                padding: 15px;
                flex-grow: 1;
            }
            .d-text h4 {
                font-size: 1.2rem;
                margin-bottom: 10px;
            }
            .d-atr-group ul {
                list-style: none;
                padding: 0;
                display: flex;
                flex-wrap: wrap;
                gap: 10px;
            }
            .d-atr-group ul li {
                flex: 1 1 45%;
                background: #f7f7f7;
                padding: 8px;
                border-radius: 5px;
                font-size: 0.9rem;
            }
            .d-price {
                padding: 15px;
                text-align: center;
                font-weight: bold;
            }

            .btn-main {
                background-color: #6CC84C; /* xanh lá tươi */
                color: white;
                border-radius: 8px;
                padding: 8px 16px;
                text-decoration: none;
                transition: all 0.3s ease;
            }

            .btn-main:hover {
                background-color: #57A83A; /* xanh lá đậm khi hover */
            }


            /* User menu */
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
    </head>
    <body>
        <div id="wrapper">
            <header class="transparent scroll-light has-topbar">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="de-flex sm-pt10">
                                <div class="de-flex-col">
                                    <div id="logo">
                                        <a href="${pageContext.request.contextPath}/home">
                                            <img class="logo-1" src="${pageContext.request.contextPath}/images/logo.png" alt="">
                                        </a>
                                    </div>
                                </div>
                                <div class="de-flex-col header-col-mid">
                                    <ul id="mainmenu">
                                        <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                                        <li><a href="${pageContext.request.contextPath}/cars">Cars</a></li>
                                        <li><a href="${pageContext.request.contextPath}/account-profile">My Account</a></li>
                                        <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
                                    </ul>
                                </div>
                                <div class="de-flex-col">
                                    <div class="menu_side_area">
                                        <c:choose>
                                            <c:when test="${not empty sessionScope.user}">
                                                <div id="myUserMenu" class="my-user-menu">
                                                    <button id="myUserBtn" class="my-user-btn" type="button" title="Tài khoản">
                                                        <i class="fa fa-user"></i>
                                                    </button>
                                                    <div class="my-user-dropdown">
                                                        <a class="menu-item" href="${pageContext.request.contextPath}/account-profile">Tài khoản</a>
                                                        <a class="menu-item" href="${pageContext.request.contextPath}/change-password">Đổi mật khẩu</a>
                                                        <a class="menu-item" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                                                    </div>
                                                </div>
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

            <div class="no-bottom no-top zebra" id="content">
                <section id="subheader" class="jarallax text-light">
                    <img src="${pageContext.request.contextPath}/images/background/2.jpg" class="jarallax-img" alt="">
                    <div class="center-y relative text-center">
                        <div class="container">
                            <h1>Cars</h1>
                        </div>
                    </div>
                </section>

                <section id="section-cars">
                    <div class="container">
                        <div class="row">
                            <!-- Filter sidebar -->
                            <div class="col-lg-3 mb-4">
                                <div class="item_filter_group">
                                    <h4>Vehicle Type</h4>
                                    <div class="de_form">
                                        <div class="de_checkbox">
                                            <input id="vehicle_type_1" type="checkbox" value="Car">
                                            <label for="vehicle_type_1">Car</label>
                                        </div>
                                        <div class="de_checkbox">
                                            <input id="vehicle_type_2" type="checkbox" value="Van">
                                            <label for="vehicle_type_2">Van</label>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Car list -->
                            <div class="col-lg-9">
                                <div class="row g-3">
                                    <c:forEach var="car" items="${carList}">
                                        <div class="col-md-6 col-lg-4 d-flex">
                                            <div class="de-item-list">
                                                <div class="d-img">
                                                    <img src="${pageContext.request.contextPath}/images/cars/${car.imageUrl}" class="img-fluid" alt="${car.model}">
                                                </div>
                                                <div class="d-info">
                                                    <div class="d-text">
                                                        <h4>${car.brand} ${car.model}</h4>
                                                        <div class="d-atr-group">
                                                            <ul class="d-atr">
                                                                <li><span>Seats:</span> ${car.capacity}</li>
                                                                <li><span>Transmission:</span> ${car.transmission}</li>
                                                                <li><span>Fuel:</span> ${car.fuelType}</li>
                                                                <li><span>Type:</span> ${car.carTypeName}</li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="d-price">
                                                    Daily rate from <span>$${car.pricePerDay}</span><br>
                                                    <a class="btn-main" href="car-single.jsp?id=${car.carId}">Rent Now</a>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            </div>

            <footer class="text-light">
                <div class="container text-center">
                    <p>Copyright 2025 - Rentaly</p>
                </div>
            </footer>
        </div>

        <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/plugins.js"></script>
        <script src="${pageContext.request.contextPath}/js/designesia.js"></script>

        <script>
            // User menu toggle với delay giống /home
            (function () {
                var menu = document.getElementById('myUserMenu');
                if (menu) {
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
                        }, 250); // Delay 250ms giống /home
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
                }
            })();
        </script>

    </body>
</html>
