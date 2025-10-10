<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">

    <head>
        <title>${car.brand} ${car.model} - Vehicle Fleet</title>
        <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="image/gif" sizes="16x16">
        <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
        <meta content="width=device-width, initial-scale=1.0" name="viewport">

        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/css/mdb.min.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/css/plugins.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/css/coloring.css" rel="stylesheet" type="text/css">
        <link id="colors" href="${pageContext.request.contextPath}/css/colors/scheme-01.css" rel="stylesheet" type="text/css">
    </head>

    <body>
        <div id="wrapper">

            <!-- Preloader -->
            <div id="de-preloader"></div>

            <!-- Header -->
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
                            <div class="social-icons">
                                <a href="#"><i class="fa fa-facebook fa-lg"></i></a>
                                <a href="#"><i class="fa fa-twitter fa-lg"></i></a>
                                <a href="#"><i class="fa fa-youtube fa-lg"></i></a>
                                <a href="#"><i class="fa fa-pinterest fa-lg"></i></a>
                                <a href="#"><i class="fa fa-instagram fa-lg"></i></a>
                            </div>
                        </div>
                        <div class="clearfix"></div>
                    </div>
                </div>

                <div class="container">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="de-flex sm-pt10">
                                <div class="de-flex-col">
                                    <div id="logo">
                                        <a href="${pageContext.request.contextPath}/home">
                                            <img class="logo-1" src="${pageContext.request.contextPath}/images/logo-light.png" alt="">
                                            <img class="logo-2" src="${pageContext.request.contextPath}/images/logo.png" alt="">
                                        </a>
                                    </div>
                                </div>

                                <div class="de-flex-col header-col-mid">
                                    <ul id="mainmenu">
                                        <li><a class="menu-item" href="${pageContext.request.contextPath}/home">Home</a></li>
                                        <li><a class="menu-item" href="${pageContext.request.contextPath}/cars">Cars</a></li>
                                        <li><a class="menu-item" href="${pageContext.request.contextPath}/booking/list">Booking</a></li>
                                        <li><a class="menu-item" href="${pageContext.request.contextPath}/account">My Account</a></li>
                                        <li><a class="menu-item" href="${pageContext.request.contextPath}/contact">Contact</a></li>
                                    </ul>
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
                                                        <a class="menu-item" href="${pageContext.request.contextPath}/account-profile" role="menuitem">Profile</a>
                                                        <a class="menu-item" href="${pageContext.request.contextPath}/change-password" role="menuitem">Change Password</a>
                                                        <a class="menu-item" href="${pageContext.request.contextPath}/logout" role="menuitem">Sign out</a>
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
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </header>

            <!-- Content -->
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

                <section id="section-car-details">
                    <div class="container">
                        <div class="row g-5">
                            <div class="col-lg-6">
                                <div id="slider-carousel" class="owl-carousel">
                                    <c:forEach var="img" items="${car.images}">
                                        <div class="item">
                                            <img src="${pageContext.request.contextPath}/${img.imageUrl}" alt="Ảnh xe ${car.brand} ${car.model}">
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>

                            <div class="col-lg-3">
                                <h3>${car.brand} ${car.model}</h3>
                                <p>${car.description}</p>
                                <div class="spacer-10"></div>
                                <h4>Specifications</h4>
                                <div class="de-spec">
                                    <div class="d-row">
                                        <span class="d-title">Type</span>
                                        <span class="d-value">${car.carTypeName}</span>
                                    </div>
                                    <div class="d-row">
                                        <span class="d-title">Seat</span>
                                        <span class="d-value">${car.capacity}</span>
                                    </div>
                                    <div class="d-row">
                                        <span class="d-title">Transmission</span>
                                        <span class="d-value">${car.transmission}</span>
                                    </div>
                                    <div class="d-row">
                                        <span class="d-title">Fuel</span>
                                        <span class="d-value">${car.fuelType}</span>
                                    </div>
                                </div>
                            </div>

                            <div class="col-lg-3">
                                <div class="de-price text-center">
                                    Rental Price/Day
                                    <h3>
                                        <fmt:formatNumber value="${car.pricePerDay}" type="currency" currencyCode="VND" maxFractionDigits="0"/>
                                    </h3>
                                </div>
                                <div class="spacer-30"></div>
                                
                                    
                                    <a href="${pageContext.request.contextPath}/make-booking?carId=${car.carId}" class="btn-main btn-fullwidth">Rent Now</a>
                                    
                                </div>
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
                                <p>Where quality meets affordability. We provide top-notch vehicles at minimum expense to ensure your journey is smooth and enjoyable.</p>
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

    </body>
</html>
