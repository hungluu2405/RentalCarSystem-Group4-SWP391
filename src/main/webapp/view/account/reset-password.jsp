
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zxx">
    <head>
    <head>
        <title>Rentaly</title> <%-- Bạn có thể thay đổi title cho từng trang --%>
        <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="image/gif" sizes="16x16">
        <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="Rentaly - Multipurpose Vehicle Car Rental Website Template" name="description">

        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" id="bootstrap">
        <link href="${pageContext.request.contextPath}/css/mdb.min.css" rel="stylesheet" type="text/css" id="mdb">
        <link href="${pageContext.request.contextPath}/css/plugins.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/css/coloring.css" rel="stylesheet" type="text/css">
        <link id="colors" href="${pageContext.request.contextPath}/css/colors/scheme-01.css" rel="stylesheet" type="text/css">
    </head>
</head>
<body>
    <div id="wrapper">
        <header class="transparent scroll-light has-topbar">
            <div id="topbar" class="topbar-dark text-light">
                <div class="container">
                    <div class="topbar-left xs-hide">
                        <div class="topbar-widget">
                            <div class="topbar-widget"><a href="#"><i class="fa fa-phone"></i>+208 333 9296</a></div>
                            <div class="topbar-widget"><a href="#"><i class="fa fa-envelope"></i>contact@rentaly.com</a>
                            </div>
                            <div class="topbar-widget"><a href="#"><i class="fa fa-clock-o"></i>Mon - Fri 08.00 -
                                    18.00</a></div>
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
                                <div class="de-flex-col">
                                    <!-- logo begin -->
                                    <div id="logo">
                                        <a href="${pageContext.request.contextPath}/home">
                                            <img class="logo-1" src="images/logo-light.png" alt="">
                                            <img class="logo-2" src="images/logo.png" alt="">
                                        </a>
                                    </div>
                                    <!-- logo close -->
                                </div>
                            </div>
                            <div class="de-flex-col header-col-mid">
                                <ul id="mainmenu">
                                    <li><a class="menu-item" href="index.html">Home</a>
                                        <ul>
                                            <li><a class="menu-item new" href="02_dark-index-1.html">Homepage 1 Dark</a>
                                            </li>
                                            <li><a class="menu-item new" href="02_dark-index-2.html">Homepage 2 Dark</a>
                                            </li>
                                            <li><a class="menu-item" href="index.html">Homepage Main</a></li>
                                            <li><a class="menu-item" href="index-2.html">Homepage 2</a></li>
                                            <li><a class="menu-item" href="index-3.html">Homepage 3</a></li>
                                            <li><a class="menu-item" href="index-4.html">Homepage 4</a></li>
                                            <li><a class="menu-item" href="index-5.html">Homepage 5</a></li>
                                            <li><a class="menu-item" href="index-6.html">Homepage 6</a></li>
                                        </ul>
                                    </li>
                                    <li><a class="menu-item" href="cars.html">Cars</a>
                                        <ul>
                                            <li><a class="menu-item" href="cars.html">Cars List 1</a></li>
                                            <li><a class="menu-item" href="02_dark-cars.html">Cars List 1 Dark</a></li>
                                            <li><a class="menu-item" href="cars-list.html">Cars List 2</a></li>
                                            <li><a class="menu-item" href="02_dark-cars-list.html">Cars List 2 Dark</a>
                                            </li>
                                            <li><a class="menu-item" href="car-single.html">Cars Single</a></li>
                                            <li><a class="menu-item" href="02_dark-car-single.html">Cars Single Dark</a>
                                            </li>
                                        </ul>
                                    </li>
                                    <li><a class="menu-item" href="quick-booking.html">Booking</a>
                                        <ul>
                                            <li><a class="menu-item new" href="quick-booking.html">Quick Booking</a>
                                            </li>
                                            <li><a class="menu-item" href="booking.html">Booking</a></li>
                                        </ul>
                                    </li>
                                    <li><a class="menu-item" href="account-dashboard.html">My Account</a>
                                        <ul>
                                            <li><a class="menu-item" href="account-dashboard.html">Dashboard</a></li>
                                            <li><a class="menu-item" href="account-profile.html">My Profile</a></li>
                                            <li><a class="menu-item" href="account-booking.html">My Orders</a></li>
                                            <li><a class="menu-item" href="account-favorite.html">My Favorite Cars</a>
                                            </li>
                                        </ul>
                                    </li>
                                    <li><a class="menu-item" href="#">Pages</a>
                                        <ul>
                                            <li><a class="menu-item" href="about.html">About Us</a></li>
                                            <li><a class="menu-item" href="contact.html">Contact</a></li>
                                            <li><a class="menu-item" href="login.html">Login</a></li>
                                            <li><a class="menu-item" href="register.html">Register</a></li>
                                            <li><a class="menu-item" href="404.html">Page 404</a></li>
                                        </ul>
                                    </li>
                                    <li><a class="menu-item" href="#">News</a>
                                        <ul>
                                            <li><a class="menu-item" href="news-standart-right-sidebar.html">News
                                                    Standard</a>
                                                <ul>
                                                    <li><a class="menu-item"
                                                           href="news-standart-right-sidebar.html">Right Sidebar</a>
                                                    </li>
                                                    <li><a class="menu-item" href="news-standart-left-sidebar.html">Left
                                                            Sidebar</a></li>
                                                    <li><a class="menu-item" href="news-standart-no-sidebar.html">No
                                                            Sidebar</a></li>
                                                </ul>
                                            </li>
                                            <li><a class="menu-item" href="news-grid-right-sidebar.html">News Grid</a>
                                                <ul>
                                                    <li><a class="menu-item" href="news-grid-right-sidebar.html">Right
                                                            Sidebar</a></li>
                                                    <li><a class="menu-item" href="news-grid-left-sidebar.html">Left
                                                            Sidebar</a></li>
                                                    <li><a class="menu-item" href="news-grid-no-sidebar.html">No
                                                            Sidebar</a></li>
                                                </ul>
                                            </li>
                                        </ul>
                                    </li>
                                    <li><a class="menu-item" href="#">Elements</a>
                                        <ul>
                                            <li><a class="menu-item" href="preloader.html">Preloader</a></li>
                                            <li><a class="menu-item" href="icon-boxes.html">Icon Boxes</a></li>
                                            <li><a class="menu-item" href="badge.html">Badge</a></li>
                                            <li><a class="menu-item" href="counters.html">Counters</a></li>
                                            <li><a class="menu-item" href="gallery-popup.html">Gallery Popup</a></li>
                                            <li><a class="menu-item" href="icons-elegant.html">Icons Elegant</a></li>
                                            <li><a class="menu-item" href="icons-etline.html">Icons Etline</a></li>
                                            <li><a class="menu-item" href="icons-font-awesome.html">Icons Font
                                                    Awesome</a></li>
                                            <li><a class="menu-item" href="map.html">Map</a></li>
                                            <li><a class="menu-item" href="modal.html">Modal</a></li>
                                            <li><a class="menu-item" href="popover.html">Popover</a></li>
                                            <li><a class="menu-item" href="tabs.html">Tabs</a></li>
                                            <li><a class="menu-item" href="tooltips.html">Tooltips</a></li>
                                        </ul>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </header>
        <div class="no-bottom no-top" id="content">
            <div id="top"></div>
            <section id="section-hero" aria-label="section" class="jarallax">
                <img src="${pageContext.request.contextPath}/images/background/2.jpg" class="jarallax-img" alt="">
                <div class="v-center">
                    <div class="container">
                        <div class="row align-items-center">
                            <div class="col-lg-4 offset-lg-4">
                                <div class="padding40 rounded-3 shadow-soft" data-bgcolor="#ffffff">
                                    <h4>Set a New Password</h4>
                                    <div class="spacer-10"></div>
                                    <p style="color:red;">${requestScope.error}</p>
                                    <form id="form_reset" class="form-border" method="post" action="${pageContext.request.contextPath}/reset-password">
                                        <div class="field-set mb-3">
                                            <input type="password" name="password" class="form-control" placeholder="New Password" required>
                                        </div>
                                        <div class="field-set mb-3">
                                            <input type="password" name="re_password" class="form-control" placeholder="Confirm New Password" required>
                                        </div>
                                        <div id="submit">
                                            <input type="submit" value="Reset Password" class="btn-main btn-fullwidth rounded-3">                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/plugins.js"></script>
    <script src="${pageContext.request.contextPath}/js/designesia.js"></script>
</body>
</html>