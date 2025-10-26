<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zxx">

    <head>
        <meta charset="UTF-8">
        <title>Rentaly - Multipurpose Vehicle Car Rental Website Template</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="image/gif" sizes="16x16">
        <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="Rentaly - Multipurpose Vehicle Car Rental Website Template" name="description">
        <meta content="" name="keywords">
        <meta content="" name="author">
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" id="bootstrap">
        <link href="${pageContext.request.contextPath}/css/mdb.min.css" rel="stylesheet" type="text/css" id="mdb">
        <link href="${pageContext.request.contextPath}/css/plugins.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/css/coloring.css" rel="stylesheet" type="text/css">
        <link id="colors" href="${pageContext.request.contextPath}/css/colors/scheme-01.css" rel="stylesheet" type="text/css">
        <style>
            /* === CSS CŨ (USER ICON & MAIN BUTTONS) === */
            .user-icon {
                display: flex;
                justify-content: center;
                align-items: center;
                width: 45px;
                height: 45px;
                border-radius: 50%;
                background-color: #6c63ff;
                transition: all 0.3s ease;
            }

            .user-icon a {
                color: white;
                font-size: 20px;
                text-decoration: none;
            }

            .user-icon:hover {
                background-color: #574bff;
                transform: scale(1.05);
            }

            .btn-main {
                background-color: #6c63ff;
                color: white;
                border-radius: 20px;
                padding: 8px 16px;
                text-decoration: none;
                transition: all 0.3s ease;
            }

            .btn-main:hover {
                background-color: #574bff;
            }

            /* === CSS MỚI & SỬA LỖI FORM TÌM KIẾM TỐI GIẢN (CĂN CHỈNH CUỐI CÙNG) === */

            /* 1. QUY TẮC CĂN GIỮA VÀ GIỚI HẠN ĐỘ RỘNG (Mở rộng tối đa) */
            .search-container-wrapper {
                max-width: 95%; /* Giữ nguyên 95% để gần sát mép */
                margin: 0 auto;
            }

            /* Điều chỉnh padding theme cũ và BO TRÒN GÓC */
            .p-4.rounded-3.shadow-soft {
                padding: 15px !important;
                border-radius: 10px !important;
            }

            /* 2. CẤU TRÚC FLEXBOX CHÍNH */
            .main-search-form-simplified {
                padding: 0;
            }

            .search-form-grid-simplified {
                display: flex;
                gap: 10px; /* Giảm gap */
                align-items: flex-end; /* 🚨 SỬA LẠI: Đảm bảo căn chỉnh các input ở cuối */
                padding: 5px;
            }

            /* 3. ĐỊNH DẠNG INPUT GROUPS (Rất quan trọng để làm gọn) */
            .input-group-simplified {
                flex-grow: 1;
                min-width: 100px;
                text-align: left;
                padding-top: 0;
                margin-bottom: 0; /* Xóa margin đáy nếu có */
            }

            /* Kẻ dọc cho các cột giữa */
            .input-group-simplified:not(.search-button-group-simplified):not(.location-group-simplified) {
                border-right: 1px solid #eee;
                padding-right: 20px;
            }

            .location-group-simplified {
                flex-grow: 2;
                min-width: 200px;
                border-right: 1px solid #eee;
                padding-right: 20px;
            }

            /* 4. STYLE CHỮ VÀ INPUT (Làm gọn) */
            .input-group-simplified label {
                font-weight: 500;
                display: block;
                margin-bottom: 0; /* 🚨 SỬA LẠI: Xóa margin giữa label và input */
                color: #555;
                font-size: 13px;
            }

            .input-group-simplified input, .input-group-simplified select {
                border: none;
                padding: 4px 0; /* 🚨 SỬA LẠI: Giảm padding dọc để input mỏng hơn */
                width: 100%;
                font-size: 15px;
                font-weight: bold;
                color: #000;
                border-bottom: 1px solid transparent; /* Đảm bảo không có border dưới */
                cursor: pointer;
                /* THÊM QUY TẮC QUAN TRỌNG: Loại bỏ background/border theme cũ */
                background-color: transparent !important;
            }

            .input-group-simplified select {
                appearance: none;
                -webkit-appearance: none;
                -moz-appearance: none;
            }

            /* 5. NÚT TÌM KIẾM (Kéo giãn toàn bộ chiều cao) */
            .btn-search-final {
                background: #4DC0B5;
                color: white;
                padding: 0 20px; /* Xóa padding dọc, dùng height để kiểm soát */
                border: none;
                border-radius: 5px;
                cursor: pointer;
                font-weight: bold;
                min-width: 100px;
                height: 50px; /* 🚨 SỬA LẠI: Đặt height cố định để nó bằng chiều cao của hàng input */
                display: flex;
                align-items: center;
                justify-content: center;
            }
        </style>
    </head>

    <body onload="initialize()">
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
                                        <div id="logo">
                                            <a href="${pageContext.request.contextPath}/home">
                                                <img class="logo-1" src="${pageContext.request.contextPath}/images/logo-light.png" alt="">
                                                <img class="logo-2" src="${pageContext.request.contextPath}/images/logo.png" alt="">
                                            </a>
                                        </div>
                                    </div>
                                </div>
                                <div class="de-flex-col header-col-mid">
                                    <ul id="mainmenu">

                                        <li><a class="menu-item" href="${pageContext.request.contextPath}/cars">Cars</a>

                                        </li>
                                        <li><a class="menu-item" href="${pageContext.request.contextPath}/view/contact/contact.jsp">Contact</a>
                                        </li>
                                                <li><a class="menu-item" href="${pageContext.request.contextPath}/about.html">About Us</a></li>


                                </div>
                                <div class="de-flex-col">
                                    <div class="menu_side_area">
                                        
                                        <c:choose>
                                            <c:when test="${not empty sessionScope.user}">
                                                <!-- ICON CHUÔNG -->
                                                <a href="${pageContext.request.contextPath}/view/notification/notification.jsp"
                                                   class="btn-bell" title="Thông báo">
                                                  <i class="fa-solid fa-bell"></i>
                                                </a>

                                                <!-- USER MENU -->
                                                <div id="myUserMenu" class="my-user-menu">
                                                    <button id="myUserBtn" class="my-user-btn" type="button" aria-haspopup="true" aria-expanded="false" title="Tài khoản">
                                                        <i class="fa fa-user" aria-hidden="true"></i>
                                                    </button>

                                                    <div class="my-user-dropdown" role="menu" aria-labelledby="myUserBtn">


                                                        <c:choose>
                                                            <%-- Giả sử: 1 = Admin --%>
                                                            <c:when test="${sessionScope.user.roleId == 1}">
                                                                <a class="menu-item" href="${pageContext.request.contextPath}/accountDB" role="menuitem">Admin Account</a>
                                                            </c:when>

                                                            <%-- Giả sử: 2 = Car Owner --%>
                                                            <c:when test="${sessionScope.user.roleId == 2}">
                                                                <a class="menu-item" href="${pageContext.request.contextPath}/owner/profile" role="menuitem">My Account</a>
                                                            </c:when>

                                                            <%-- Giả sử: 3 = Customer --%>
                                                            <c:when test="${sessionScope.user.roleId == 3}">
                                                                <a class="menu-item" href="${pageContext.request.contextPath}/customer/profile" role="menuitem">My Account</a>
                                                            </c:when>

                                                            <%-- Trường hợp mặc định nếu không khớp role nào --%>
                                                            <c:otherwise>
                                                                <a class="menu-item" href="${pageContext.request.contextPath}/home" role="menuitem">Trang chủ</a>
                                                            </c:otherwise>
                                                        </c:choose>

                                                        <%-- Các link còn lại thì giữ nguyên --%>
                                                        <a class="menu-item" href="${pageContext.request.contextPath}/change-password" role="menuitem">Change Password</a>
                                                        <a class="menu-item" href="${pageContext.request.contextPath}/logout" role="menuitem">Log Out</a>
                                                    </div>
                                                </div>

                                                <style>
                                                    .menu_side_area {
                                                    display: flex;
                                                    align-items: center;
                                                    gap: 10px; /* khoảng cách giữa icon */
                                                  }

                                                  .btn-bell, .my-user-btn {
                                                    background: transparent;
                                                    border: none;
                                                    cursor: pointer;
                                                    color: #fff;  /* màu trắng đồng nhất với header */
                                                    font-size: 18px;
                                                    padding: 6px 8px;
                                                    display: flex;
                                                    align-items: center;
                                                    justify-content: center;
                                                  }

                                                  .btn-bell:hover, .my-user-btn:hover {
                                                    color: #32cd32; /* xanh lá khi hover */
}
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
                <section id="section-hero" aria-label="section" class="jarallax">
                    <img src="${pageContext.request.contextPath}/images/background/dep.jpg" class="jarallax-img" alt="">
                    <div class="container">
                        <div class="row align-items-center">
                            <div class="col-lg-12 text-light">
                                <div class="spacer-double"></div>
                                <div class="spacer-double"></div>
                                <h1 class="mb-2">Looking for a <span class="id-color">vehicle</span>? You're at the right place.</h1>
                                <div class="spacer-single"></div>
                            </div>

                            <div class="col-lg-12">
                                <div class="spacer-single sm-hide"></div>

                                <div class="form-wrapper-center">
                                    <div class="p-4 rounded-3 shadow-soft" data-bgcolor="#ffffff">

                                        <form action="${pageContext.request.contextPath}/cars" method="get" class="main-search-form-simplified">

                                            <div class="search-form-grid-simplified">

                                                <div class="input-group-simplified location-group-simplified">
                                                    <label for="location">Pickup and return location</label>
                                                    <input type="text" id="autocomplete_location" name="location" placeholder="Chọn địa điểm tìm xe" class="form-control" required>
                                                </div>

                                                <div class="input-group-simplified">
                                                    <label for="pickupDate">Pickup Date</label>
                                                    <input type="date" id="pickupDate" name="startDate" class="form-control" required>
                                                </div>

                                                <div class="input-group-simplified">
                                                    <label for="pickupTime">Pickup Time</label>
                                                    <select id="pickupTime" name="pickupTime" class="form-control" required>
                                                        <option selected disabled value="">Select Time</option>
                                                        <c:forEach var="hour" begin="6" end="22">
                                                            <option value="${hour < 10 ? '0' : ''}${hour}:00">${hour < 10 ? '0' : ''}${hour}:00</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>

                                                <div class="input-group-simplified">
                                                    <label for="returnDate">Return Date</label>
                                                    <input type="date" id="returnDate" name="endDate" class="form-control" required>
                                                </div>

                                                <div class="input-group-simplified">
                                                    <label for="returnTime">Return Time</label>
                                                    <select id="returnTime" name="dropoffTime" class="form-control" required>
                                                        <option selected disabled value="">Select Time</option>
                                                        <c:forEach var="hour" begin="6" end="22">
                                                            <option value="${hour < 10 ? '0' : ''}${hour}:00">${hour < 10 ? '0' : ''}${hour}:00</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>

                                                <div class="search-button-group-simplified">
                                                    <button type="submit" id='send_message' value='Find a Vehicle' class="btn-search-final">Search</button>
                                                </div>

                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>

                            <div class="spacer-double"></div>

                            <div class="row">
                                <div class="col-lg-12 text-light">
                                    <div class="container-timeline">
                                        <ul>
                                            <li>
                                                <h4>Choose a vehicle</h4>
                                                <p>Unlock unparalleled adventures and memorable journeys with our vast fleet of vehicles tailored to suit every need, taste, and destination.</p>
                                            </li>
                                            <li>
                                                <h4>Pick location &amp; date</h4>
                                                <p>Pick your ideal location and date, and let us take you on a journey filled with convenience, flexibility, and unforgettable experiences.</p>
                                            </li>
                                            <li>
                                                <h4>Make a booking</h4>
                                                <p>Secure your reservation with ease, unlocking a world of possibilities and embarking on your next adventure with confidence.</p>
                                            </li>
                                            <li>
                                                <h4>Sit back &amp; relax</h4>
                                                <p>Hassle-free convenience as we take care of every detail, allowing you to unwind and embrace a journey filled comfort.</p>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>

                <section aria-label="section" class="pt40 pb40 text-light" data-bgcolor="#111111">
                    <div class="wow fadeInRight d-flex">
                        <div class="de-marquee-list">
                            <div class="d-item">
                                <span class="d-item-txt">SUV</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                                <span class="d-item-txt">Hatchback</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                                <span class="d-item-txt">Crossover</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                                <span class="d-item-txt">Convertible</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                                <span class="d-item-txt">Sedan</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                                <span class="d-item-txt">Sports Car</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                                <span class="d-item-txt">Coupe</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                                <span class="d-item-txt">Minivan</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                                <span class="d-item-txt">Station Wagon</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                                <span class="d-item-txt">Truck</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                                <span class="d-item-txt">Minivans</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                                <span class="d-item-txt">Exotic Cars</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                            </div>
                        </div>

                        <div class="de-marquee-list">
                            <div class="d-item">
                                <span class="d-item-txt">SUV</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                                <span class="d-item-txt">Hatchback</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                                <span class="d-item-txt">Crossover</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                                <span class="d-item-txt">Convertible</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                                <span class="d-item-txt">Sedan</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                                <span class="d-item-txt">Sports Car</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                                <span class="d-item-txt">Coupe</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                                <span class="d-item-txt">Minivan</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                                <span class="d-item-txt">Station Wagon</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                                <span class="d-item-txt">Truck</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                                <span class="d-item-txt">Minivans</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                                <span class="d-item-txt">Exotic Cars</span>
                                <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                            </div>
                        </div>
                    </div>
                </section>

                <section aria-label="section">
                    <div class="container">
                        <div class="row align-items-center">
                            <div class="col-lg-6 offset-lg-3 text-center">
                                <span class="subtitle">Why Choose Us</span>
                                <h2>Our Features</h2>
                                <p>Discover a world of convenience, safety, and customization, paving the way for unforgettable adventures and seamless mobility solutions.</p>
                                <div class="spacer-20"></div>
                            </div>
                            <div class="clearfix"></div>
                            <div class="col-lg-3">
                                <div class="box-icon s2 p-small mb20 wow fadeInRight" data-wow-delay=".5s">
                                    <i class="fa bg-color fa-trophy"></i>
                                    <div class="d-inner">
                                        <h4>First class services</h4>
                                        Where luxury meets exceptional care, creating unforgettable moments and exceeding your every expectation.
                                    </div>
                                </div>
                                <div class="box-icon s2 p-small mb20 wow fadeInL fadeInRight" data-wow-delay=".75s">
                                    <i class="fa bg-color fa-road"></i>
                                    <div class="d-inner">
                                        <h4>24/7 road assistance</h4>
                                        Reliable support when you need it most, keeping you on the move with confidence and peace of mind.
                                    </div>
                                </div>
                            </div>

                            <div class="col-lg-6">
                                <img src="${pageContext.request.contextPath}/images/misc/car.png" alt="" class="img-fluid wow fadeInUp">
                            </div>

                            <div class="col-lg-3">
                                <div class="box-icon s2 d-invert p-small mb20 wow fadeInL fadeInLeft" data-wow-delay="1s">
                                    <i class="fa bg-color fa-tag"></i>
                                    <div class="d-inner">
                                        <h4>Quality at Minimum Expense</h4>
                                        Unlocking affordable brilliance with elevating quality while minimizing costs for maximum value.
                                    </div>
                                </div>
                                <div class="box-icon s2 d-invert p-small mb20 wow fadeInL fadeInLeft" data-wow-delay="1.25s">
                                    <i class="fa bg-color fa-map-pin"></i>
                                    <div class="d-inner">
                                        <h4>Free Pick-Up & Drop-Off</h4>
                                        Enjoy free pickup and drop-off services, adding an extra layer of ease to your car rental experience.
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>



                <section id="section-cars">
                    <div class="container">
                        <div class="row align-items-center">
                            <div class="col-lg-6 offset-lg-3 text-center">
                                <span class="subtitle">Enjoy Your Ride</span>
                                <h2>Our Vehicle Fleet</h2>
                                <p>Driving your dreams to reality with an exquisite fleet of versatile vehicles for unforgettable journeys.</p>

                                <!-- Nút xem tất cả xe -->
                                <div class="spacer-10"></div>
                                <a href="${pageContext.request.contextPath}/cars" class="btn-main">View All Cars</a>

                                <div class="spacer-20"></div>
                            </div>

                            <div id="items-carousel" class="owl-carousel wow fadeIn">

                                <%-- Lặp qua danh sách xe nổi bật --%>
                                <c:forEach var="car" items="${topBookedCars}">
                                    <div class="col-lg-12">
                                        <div class="de-item mb30">
                                            <div class="d-img">
                                                <img src="${pageContext.request.contextPath}/${not empty car.imageUrl ? car.imageUrl : 'images/cars/default.jpg'}"
                                                     class="img-fluid"
                                                     alt="${car.brand} ${car.model}">
                                            </div>
                                            <div class="d-info">
                                                <div class="d-text">
                                                    <h4>${car.brand} ${car.model}</h4>
                                                    <div class="d-atr-group">
                                                        <span class="d-atr"><img src="${pageContext.request.contextPath}/images/icons/1-green.svg" alt="">${car.capacity}</span>
                                                        <span class="d-atr"><img src="${pageContext.request.contextPath}/images/icons/3-green.svg" alt="">${car.transmission}</span>
                                                        <span class="d-atr"><img src="${pageContext.request.contextPath}/images/icons/4-green.svg" alt="">${car.carTypeName}</span>
                                                    </div>
                                                    <div class="d-price">
                                                        <h3 class="fw-bold mb-0">
                                                            <fmt:formatNumber value="${car.pricePerDay}" type="currency" currencyCode="VND" maxFractionDigits="0"/>
                                                        </h3>
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
                        </div>
                    </div>
                </section>


                <section class="text-light jarallax" aria-label="section">
                    <img src="${pageContext.request.contextPath}/images/background/6.jpg" alt="" class="jarallax-img">
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-3">
                                <h1>Let's Your Adventure Begin</h1>
                                <div class="spacer-20"></div>
                            </div>
                            <div class="col-md-3">
                                <i class="fa fa-trophy de-icon mb20"></i>
                                <h4>First Class Services</h4>
                                <p>Where luxury meets exceptional care, creating unforgettable moments and exceeding your every expectation.</p>
                            </div>
                            <div class="col-md-3">
                                <i class="fa fa-road de-icon mb20"></i>
                                <h4>24/7 road assistance</h4>
                                <p>Reliable support when you need it most, keeping you on the move with confidence and peace of mind.</p>
                            </div>
                            <div class="col-md-3">
                                <i class="fa fa-map-pin de-icon mb20"></i>
                                <h4>Free Pick-Up & Drop-Off</h4>
                                <p>Enjoy free pickup and drop-off services, adding an extra layer of ease to your car rental experience.</p>
                            </div>
                        </div>
                    </div>
                </section>


                <section id="section-testimonials" class="no-top no-bottom">
                    <div class="container-fluid">
                        <div class="row g-2 p-2 align-items-center">

                            <div class="col-md-4">
                                <div class="de-image-text">
                                    <div class="d-text">
                                        <div class="d-quote id-color"><i class="fa fa-quote-right"></i></div>
                                        <h4>Excellent Service! Car Rent Service!</h4>
                                        <blockquote>
                                            I have been using Rentaly for my Car Rental needs for over 5 years now. I have never had any problems with their service. Their customer support is always responsive and helpful. I would recommend Rentaly to anyone looking for a reliable Car Rental provider.
                                            <span class="by">Thuy Linh</span>
                                        </blockquote>
                                    </div>
                                    <img src="images/testimonial/anh5.jpg" class="img-fluid" alt="">
                                </div>
                            </div>


                            <div class="col-md-4">
                                <div class="de-image-text">
                                    <div class="d-text">
                                        <div class="d-quote id-color"><i class="fa fa-quote-right"></i></div>
                                        <h4>Excellent Service! Car Rent Service!</h4>
                                        <blockquote>
                                            We have been using Rentaly for our trips needs for several years now and have always been happy with their service. Their customer support is Excellent Service! and they are always available to help with any issues we have. Their prices are also very competitive.
                                            <span class="by">Bang Kieu</span>
                                        </blockquote>
                                    </div>
                                    <img src="images/testimonial/anh6.jpg" class="img-fluid" alt="">
                                </div>
                            </div>

                            <div class="col-md-4">
                                <div class="de-image-text">
                                    <div class="d-text">
                                        <div class="d-quote id-color"><i class="fa fa-quote-right"></i></div>
                                        <h4>Excellent Service! Car Rent Service!</h4>
                                        <blockquote>
                                            Endorsed by industry experts, Rentaly is the Car Rental solution you can trust. With years of experience in the field, we provide fast, reliable and secure Car Rental services.
                                            <span class="by">Tuan Vu Manh</span>
                                        </blockquote>
                                    </div>
                                    <img src="images/testimonial/anh7.jpg" class="img-fluid" alt="">
                                </div>
                            </div>

                        </div>
                    </div>
                </section>

                <section id="section-faq">
                    <div class="container">
                        <div class="row">
                            <div class="col text-center">
                                <span class="subtitle">Do You Have</h2></span>
                                <h2>Any Questions?</h2>
                                <div class="spacer-20"></div>
                            </div>
                        </div>
                        <div class="row g-custom-x">
                            <div class="col-md-6 wow fadeInUp">
                                <div class="accordion secondary">
                                    <div class="accordion-section">
                                        <div class="accordion-section-title" data-tab="#accordion-1">
                                            How do I get started with Car Rental?
                                        </div>
                                        <div class="accordion-section-content" id="accordion-1">
                                            <p>Getting started is easy! Simply create an account, choose your preferred car, select the rental dates, and confirm your booking. You’ll receive a confirmation email with all the details and pickup instructions.</p>
                                        </div>
                                        <div class="accordion-section-title" data-tab="#accordion-2">
                                            Can I pay online?
                                        </div>
                                        <div class="accordion-section-content" id="accordion-2">
                                            <p>Yes, you can conveniently pay online using a credit or debit card. Online payment ensures your reservation is confirmed instantly. Some locations may also allow partial payment or pay-at-pickup options.</p>
                                        </div>
                                        <div class="accordion-section-title" data-tab="#accordion-3">
                                            What kind of Car Rental do I need?
                                        </div>
                                        <div class="accordion-section-content" id="accordion-3">
                                            <p>It depends on your trip and preferences:</br>
                                                Economy or Compact: Best for city travel and fuel efficiency.</br>
                                                SUV or Minivan: Great for family trips or extra luggage space.</br>
                                                Luxury or Premium: Perfect for business or special occasions.</br>
                                                You can filter cars by size, price, or purpose when booking.</p>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-6 wow fadeInUp">
                                <div class="accordion secondary">
                                    <div class="accordion-section">
                                        <div class="accordion-section-title" data-tab="#accordion-b-4">
                                            What is a rental car security deposit?
                                        </div>
                                        <div class="accordion-section-content" id="accordion-b-4">
                                            <p>A security deposit is a temporary hold placed on your payment card at the start of your rental. It covers potential damages, traffic fines, or extra charges. The hold is released once you return the car in good condition.</p>
                                        </div>
                                        <div class="accordion-section-title" data-tab="#accordion-b-5">
                                            Can I cancel or modify my reservation?
                                        </div>
                                        <div class="accordion-section-content" id="accordion-b-5">
                                            <p>No, you can't cancel or modify your reservation online before the pickup time. Cancellation fees may apply depending on how close to the pickup time you make the change.</p>
                                        </div>
                                        <div class="accordion-section-title" data-tab="#accordion-b-6">
                                            Is it possible to extend my rental period?
                                        </div>
                                        <div class="accordion-section-content" id="accordion-b-6">
                                            <p>No. Once your rental agreement is confirmed, the rental period cannot be extended. You will need to create a new booking if you wish to rent the car for a longer duration.</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>

                <section id="section-call-to-action" class="bg-color-2 pt60 pb60 text-light">
                    <div class="container">
                        <div class="container">
                            <div class="row">

                                <div class="col-lg-4 offset-lg-2">
                                    <span class="subtitle text-white">Call us for further information</span>
                                    <h2 class="s2">Rentaly customer care is here to help you anytime.</h2>
                                </div>

                                <div class="col-lg-4 text-lg-center text-sm-center">
                                    <div class="phone-num-big">
                                        <i class="fa fa-phone"></i>
                                        <span class="pnb-text">
                                            Call Us Now
                                        </span>
                                        <span class="pnb-num">
                                            1 200 333 800
                                        </span>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/view/contact/contact.jsp" class="btn-main">Contact Us</a>
                                </div>
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
                                        <a>
                                            Copyright 2025 - Rentaly by Designesia
                                        </a>
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


        <!-- Javascript Files
        ================================================== -->
        <script src="js/plugins.js"></script>
        <script src="js/designesia.js"></script>
        <script src="https://maps.googleapis.com/maps/api/js?key=${googleApiKey}&libraries=places"></script>


    </body>


    <!-- Mirrored from www.madebydesignesia.com/themes/rentaly/index.html by HTTrack Website Copier/3.x [XR&CO'2014], Sat, 20 Sep 2025 10:55:35 GMT -->
</html>