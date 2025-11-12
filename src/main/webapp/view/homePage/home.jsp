<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="util.TimeAgoUtil" %>
<fmt:setLocale value="en_US" />
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
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css"
          id="bootstrap">
    <link href="${pageContext.request.contextPath}/css/mdb.min.css" rel="stylesheet" type="text/css" id="mdb">
    <link href="${pageContext.request.contextPath}/css/plugins.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/coloring.css" rel="stylesheet" type="text/css">
    <link id="colors" href="${pageContext.request.contextPath}/css/colors/scheme-01.css" rel="stylesheet"
          type="text/css">
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

        /* === CSS MỚI CHO NOTIFICATION DROPDOWN === */

        .notification-menu {
            position: relative;
            display: inline-block;
            vertical-align: middle;
        }

        .btn-bell {
            position: relative; /* Quan trọng cho badge */
        }

        /* Badge số lượng thông báo */
        .notification-badge {
            position: absolute;
            top: -5px;
            right: 0px;
            background-color: #ff4d4d; /* Màu đỏ */
            color: white;
            border-radius: 50%;
            padding: 2px 6px;
            font-size: 10px;
            line-height: 1;
            font-weight: bold;
            min-width: 18px;
            text-align: center;
        }

        .notification-dropdown {
            /* Copy style từ my-user-dropdown */
            position: absolute;
            top: calc(100% + 6px);
            right: 0;
            width: 300px; /* Đặt độ rộng cố định */
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 6px 18px rgba(0, 0, 0, 0.12);
            padding: 0; /* Xóa padding ngoài */
            z-index: 9998; /* Đảm bảo nó nằm dưới user dropdown */
            opacity: 0;
            visibility: hidden;
            transform: translateY(-6px);
            transition: opacity .12s ease, transform .12s ease, visibility .12s;
            pointer-events: none;
        }

        /* Hiện dropdown khi có class 'open' */
        .notification-menu.open .notification-dropdown {
            opacity: 1;
            visibility: visible;
            transform: translateY(0);
            pointer-events: auto;
        }

        .notification-dropdown .dropdown-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 15px;
            border-bottom: 1px solid #eee;
        }

        .notification-dropdown .dropdown-header h4 {
            margin: 0;
            font-size: 16px;
            font-weight: bold;
            color: #333;
        }

        .notification-dropdown .dropdown-header a {
            font-size: 12px;
            color: #007bff;
            text-decoration: none;
        }

        .notification-dropdown #notification-list {
            list-style: none;
            padding: 0;
            margin: 0;
            max-height: 300px; /* Giới hạn chiều cao */
            overflow-y: auto; /* Thêm scrollbar */
        }

        .notification-dropdown #notification-list li {
            padding: 10px 15px;
            border-bottom: 1px solid #f6f6f6;
            transition: background-color 0.1s;
        }

        .notification-dropdown #notification-list li:hover {
            background: #f9f9f9;
        }

        .notification-dropdown #notification-list li a {
            display: block;
            text-decoration: none;
            color: #333;
        }

        .notification-dropdown #notification-list li.unread {
            background-color: #e6f7ff; /* Màu nền cho thông báo chưa đọc */
            border-left: 3px solid #007bff; /* Đánh dấu màu xanh */
        }

        .notification-dropdown #notification-list li strong {
            font-size: 14px;
            display: block;
            margin-bottom: 2px;
        }

        .notification-dropdown #notification-list li p {
            font-size: 13px;
            margin: 0;
            line-height: 1.4;
            color: #555;
        }

        .notification-dropdown #notification-list li .time {
            display: block;
            font-size: 11px;
            color: #999;
            margin-top: 5px;
        }

        .notification-dropdown .dropdown-footer {
            display: block;
            padding: 10px 0;
            text-align: center;
            border-top: 1px solid #eee;
            color: #007bff;
            text-decoration: none;
            font-size: 14px;
        }

        .notification-dropdown .empty-state {
            text-align: center;
            padding: 20px 15px;
            color: #999;
            font-style: italic;
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
                        <div class="topbar-widget"><a href="#"><i class="fa fa-envelope"></i>contact@rentaly.com</a>
                        </div>
                        <div class="topbar-widget"><a href="#"><i class="fa fa-clock-o"></i>T2 - CN 06.00 - 22.00</a>
                        </div>
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
                                        <img class="logo-1"
                                             src="${pageContext.request.contextPath}/images/logo-light.png" alt="">
                                        <img class="logo-2" src="${pageContext.request.contextPath}/images/logo.png"
                                             alt="">
                                    </a>
                                </div>
                            </div>
                        </div>
                        <div class="de-flex-col header-col-mid">
                            <ul id="mainmenu">

                                <li><a class="menu-item" href="${pageContext.request.contextPath}/cars">Danh sách xe</a>

                                </li>
                                <li><a class="menu-item"
                                       href="${pageContext.request.contextPath}/view/contact/contact.jsp">Liên hệ</a>
                                </li>
                                <li><c:if test="${empty sessionScope.user or (sessionScope.user.roleId != 1 and sessionScope.user.roleId != 2)}">
                                <li>
                                    <a class="menu-item" href="${pageContext.request.contextPath}/customer/becomeCarOwner">
                                        Trở Thành Chủ Xe
                                    </a>
                                </li>
                                </c:if>
                                </li>


                        </div>
                        <div class="de-flex-col">
                            <div class="menu_side_area">

                                <c:choose>
                                    <c:when test="${not empty sessionScope.user}">
                                        <!-- ICON CHUÔNG -->
                                        <div id="notificationMenu" class="notification-menu">
                                            <button id="notificationBtn" class="btn-bell" type="button"
                                                    aria-haspopup="true" aria-expanded="false" title="Notifications">
                                                <i class="fa-solid fa-bell"></i>
                                                    <%-- ✅ THÊM: LOGIC HIỂN THỊ BADGE (COUNT) --%>
                                                <c:if test="${sessionScope.unreadNotificationCount > 0}">
                                                    <span class="notification-badge">${sessionScope.unreadNotificationCount}</span>
                                                </c:if>
                                            </button>

                                            <div class="notification-dropdown" role="menu"
                                                 aria-labelledby="notificationBtn">
                                                <div class="dropdown-header">
                                                    <h4>Notifications</h4>
                                                        <%-- ✅ THÊM: NÚT ĐÁNH DẤU TẤT CẢ ĐÃ ĐỌC (Cần Servlet xử lý) --%>
                                                    <a href="${pageContext.request.contextPath}/mark-all-read">Mark All
                                                        Read</a>
                                                </div>


                                                <ul id="notification-list">
                                                    <c:choose>
                                                        <c:when test="${not empty sessionScope.latestNotifications}">
                                                            <c:forEach var="noti"
                                                                       items="${sessionScope.latestNotifications}">
                                                                <%-- ✅ SỬA 1: DÙNG !noti.read ĐỂ GÁN CLASS "unread" --%>
                                                                <li class="${!noti.read ? 'unread' : ''}">

                                                                        <%-- ✅ SỬA 2: LINK TỚI SERVLET XỬ LÝ MARK AS READ --%>
                                                                    <a href="${pageContext.request.contextPath}/mark-read?id=${noti.notificationId}&redirectUrl=${noti.linkUrl}">
                                                                        <strong>${noti.title}</strong>
                                                                        <p>${noti.content}</p>

                                                                        <span class="time">${TimeAgoUtil.formatTimeAgo(noti.createdAt)}</span>
                                                                    </a>
                                                                </li>
                                                            </c:forEach>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <li class="empty-state">There are no new notifications.</li>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </ul>

                                            </div>
                                        </div>

                                        <!-- USER MENU -->
                                        <div id="myUserMenu" class="my-user-menu">
                                            <button id="myUserBtn" class="my-user-btn" type="button"
                                                    aria-haspopup="true" aria-expanded="false" title="Tài khoản">
                                                <i class="fa fa-user" aria-hidden="true"></i>
                                            </button>

                                            <div class="my-user-dropdown" role="menu" aria-labelledby="myUserBtn">


                                                <c:choose>
                                                    <%-- Giả sử: 1 = Admin --%>
                                                    <c:when test="${sessionScope.user.roleId == 1}">
                                                        <a class="menu-item"
                                                           href="${pageContext.request.contextPath}/accountDB"
                                                           role="menuitem">Admin Account</a>
                                                    </c:when>

                                                    <%-- Giả sử: 2 = Car Owner --%>
                                                    <c:when test="${sessionScope.user.roleId == 2}">
                                                        <a class="menu-item"
                                                           href="${pageContext.request.contextPath}/owner/profile"
                                                           role="menuitem">Tài Khoản Của Tôi</a>
                                                    </c:when>

                                                    <%-- Giả sử: 3 = Customer --%>
                                                    <c:when test="${sessionScope.user.roleId == 3}">
                                                        <a class="menu-item"
                                                           href="${pageContext.request.contextPath}/customer/profile"
                                                           role="menuitem">Tài Khoản Của Tôi</a>
                                                    </c:when>

                                                    <%-- Trường hợp mặc định nếu không khớp role nào --%>
                                                    <c:otherwise>
                                                        <a class="menu-item"
                                                           href="${pageContext.request.contextPath}/home"
                                                           role="menuitem">Trang chủ</a>
                                                    </c:otherwise>
                                                </c:choose>

                                                    <%-- Các link còn lại thì giữ nguyên --%>
                                                <a class="menu-item"
                                                   href="${pageContext.request.contextPath}/change-password"
                                                   role="menuitem">Đổi Mật Khẩu</a>
                                                <a class="menu-item" href="${pageContext.request.contextPath}/logout"
                                                   role="menuitem">Đăng Xuất</a>
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
                                                color: #fff; /* màu trắng đồng nhất với header */
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
                                                box-shadow: 0 6px 18px rgba(0, 0, 0, 0.12);
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
                                        <a href="${pageContext.request.contextPath}/login" class="btn-main">Đăng nhập</a>
                                        <a href="${pageContext.request.contextPath}/register" class="btn-main">Đăng ký</a>
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
                        <h1 class="mb-2">Tìm xe <span class="id-color">dễ dàng</span>? Vì bạn đang ở đúng nơi!
                        </h1>
                        <div class="spacer-single"></div>
                    </div>

                    <div class="col-lg-12">
                        <div class="spacer-single sm-hide"></div>

                        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

                        <div class="form-wrapper-center">
                            <div class="p-4 rounded-3 shadow-soft" data-bgcolor="#ffffff">

                                <%-- ✅ Hiển thị thông báo lỗi (flash) --%>
                                <c:if test="${not empty sessionScope.flashErrorMessage}">
                                    <div class="alert alert-danger text-center fw-bold" role="alert"
                                         style="margin-bottom: 15px; display: flex; justify-content: center; font-size: 0.95rem;">
                                        <span>${sessionScope.flashErrorMessage}</span>
                                    </div>
                                </c:if>

                                <form action="${pageContext.request.contextPath}/cars" method="get"
                                      class="main-search-form-simplified">

                                    <div class="search-form-grid-simplified">

                                        <div class="input-group-simplified location-group-simplified">
                                            <label for="location">Địa điểm nhận và trả xe</label>
                                            <input type="text" id="autocomplete_location" name="location"
                                                   placeholder="Chọn địa điểm tìm xe" class="form-control" required
                                                   value="${sessionScope.flashForm_location != null ? sessionScope.flashForm_location : location}">
                                        </div>

                                        <div class="input-group-simplified">
                                            <label for="pickupDate">Ngày nhận</label>
                                            <input type="date" id="pickupDate" name="startDate" class="form-control" required
                                                   value="${sessionScope.flashForm_startDate != null ? sessionScope.flashForm_startDate : startDate}">
                                        </div>

                                        <div class="input-group-simplified">
                                            <label for="pickupTime">Giờ nhận</label>
                                            <select id="pickupTime" name="pickupTime" class="form-control" required>
                                                <option ${empty pickupTime and empty sessionScope.flashForm_pickupTime ? 'selected' : ''} disabled value="">
                                                    Chọn thời gian
                                                </option>
                                                <c:forEach var="hour" begin="6" end="22">
                                                    <c:set var="timeValue" value="${hour lt 10 ? '0' : ''}${hour}:00"/>
                                                    <option value="${timeValue}"
                                                        ${timeValue eq pickupTime or timeValue eq sessionScope.flashForm_pickupTime ? 'selected' : ''}>
                                                            ${timeValue}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>

                                        <div class="input-group-simplified">
                                            <label for="returnDate">Ngày trả xe</label>
                                            <input type="date" id="returnDate" name="endDate" class="form-control" required
                                                   value="${sessionScope.flashForm_endDate != null ? sessionScope.flashForm_endDate : endDate}">
                                        </div>

                                        <div class="input-group-simplified">
                                            <label for="returnTime">Thời gian trả xe</label>
                                            <select id="returnTime" name="dropoffTime" class="form-control" required>
                                                <option ${empty dropoffTime and empty sessionScope.flashForm_dropoffTime ? 'selected' : ''} disabled value="">
                                                    Chọn thời gian
                                                </option>
                                                <c:forEach var="hour" begin="6" end="22">
                                                    <c:set var="timeValue" value="${hour lt 10 ? '0' : ''}${hour}:00"/>
                                                    <option value="${timeValue}"
                                                        ${timeValue eq dropoffTime or timeValue eq sessionScope.flashForm_dropoffTime ? 'selected' : ''}>
                                                            ${timeValue}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>

                                        <div class="search-button-group-simplified">
                                            <button type="submit" id='send_message' value='Find a Vehicle' class="btn-search-final">
                                                Tìm kiếm
                                            </button>
                                        </div>
                                    </div>
                                </form>

                                <%-- ✅ Xóa flash dữ liệu sau khi hiển thị (flash message) --%>
                                <%
                                    session.removeAttribute("flashErrorMessage");
                                %>

                            </div>
                        </div>
                    </div>

                    <div class="spacer-double"></div>

                    <div class="row">
                        <div class="col-lg-12 text-light">
                            <div class="container-timeline">
                                <ul>
                                    <li>
                                        <h4>Chọn địa điểm nhận xe</h4>
                                        <p>Khám phá hành trình tuyệt vời của riêng bạn với hệ thống xe đa dạng, phù hợp mọi nhu cầu và điểm đến.</p>
                                    </li>
                                    <li>
                                        <h4>Chọn ngày giờ nhận và trả xe</h4>
                                        <p>Chọn địa điểm và thời gian thuận tiện nhất. Chúng tôi sẽ giúp bạn bắt đầu chuyến đi dễ dàng và linh hoạt.</p>
                                    </li>
                                    <li>
                                        <h4>Thanh toán dễ dàng</h4>
                                        <p>Đặt xe nhanh chóng, thanh toán an toàn. Mở ra hành trình mới đầy tự tin và hứng khởi.</p>
                                    </li>
                                    <li>
                                        <h4>Nhận xe và tận hưởng</h4>
                                        <p>Hãy thư giãn và tận hưởng chuyến đi. Mọi chi tiết đã có chúng tôi lo.</p>
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
                        <span class="d-item-txt">Mui trần</span>
                        <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                        <span class="d-item-txt">Sedan</span>
                        <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                        <span class="d-item-txt">Thể Thao</span>
                        <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                        <span class="d-item-txt">Coupe</span>
                        <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                        <span class="d-item-txt">Xe Bán Tải</span>
                        <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                        <span class="d-item-txt">Station Wagon</span>
                        <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                        <span class="d-item-txt">Xe Tải</span>
                        <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                        <span class="d-item-txt">Minivans</span>
                        <span class="d-item-display">
                                    <i class="d-item-dot"></i>
                                </span>
                        <span class="d-item-txt">Xe Điện</span>
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
                        <span class="subtitle">Xe Dành Cho Bạn</span>
                        <h2>Ưu điểm của Rentaly</h2>
                        <p>Khám phá thế giới của sự tiện lợi, an toàn và cá nhân hóa, mở ra hành trình trọn vẹn với những trải nghiệm di chuyển mượt mà và đáng nhớ.</p>
                        <div class="spacer-20"></div>
                    </div>
                    <div class="clearfix"></div>
                    <div class="col-lg-3">
                        <div class="box-icon s2 p-small mb20 wow fadeInRight" data-wow-delay=".5s">
                            <i class="fa bg-color fa-trophy"></i>
                            <div class="d-inner">
                                <h4>Dịch vụ hạng nhất</h4>
                                <p>Nơi sang trọng hòa quyện cùng sự tận tâm, mang đến những khoảnh khắc khó quên và vượt xa mọi kỳ vọng của bạn.</p>
                            </div>
                        </div>
                        <div class="box-icon s2 p-small mb20 wow fadeInL fadeInRight" data-wow-delay=".75s">
                            <i class="fa bg-color fa-road"></i>
                            <div class="d-inner">
                                <h4>Hỗ trợ 24/7 trên mọi hành trình</h4>
                                <p>Luôn sẵn sàng đồng hành khi bạn cần, giúp bạn yên tâm lăn bánh với sự tự tin và an toàn tuyệt đối.</p>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-6">
                        <img src="${pageContext.request.contextPath}/images/misc/car.png" alt=""
                             class="img-fluid wow fadeInUp">
                    </div>

                    <div class="col-lg-3">
                        <div class="box-icon s2 d-invert p-small mb20 wow fadeInL fadeInLeft" data-wow-delay="1s">
                            <i class="fa bg-color fa-tag"></i>
                            <div class="d-inner">
                                <h4>Chất lượng với chi phí tối ưu</h4>
                                <p>Mang đến dịch vụ hoàn hảo với chi phí hợp lý, giúp bạn tận hưởng chất lượng cao mà vẫn tiết kiệm tối đa.</p>
                            </div>
                        </div>
                        <div class="box-icon s2 d-invert p-small mb20 wow fadeInL fadeInLeft" data-wow-delay="1.25s">
                            <i class="fa bg-color fa-map-pin"></i>
                            <div class="d-inner">
                                <h4>Đón – Trả xe miễn phí</h4>
                                <p>Trải nghiệm tiện ích đón trả xe tận nơi hoàn toàn miễn phí, mang lại sự thoải mái và tiện lợi tuyệt đối cho chuyến đi của bạn.</p>
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
                        <span class="subtitle">Tận hưởng chuyến đi của bạn</span>
                        <h2>Dòng xe của chúng tôi</h2>
                        <p>Biến ước mơ lái xe của bạn thành hiện thực với dàn xe đa dạng, hiện đại và sang trọng, sẵn sàng đồng hành cùng bạn trên mọi hành trình đáng nhớ.</p>

                        <!-- Nút xem tất cả xe -->
                        <div class="spacer-10"></div>
                        <a href="${pageContext.request.contextPath}/cars" class="btn-main">Xem danh sách xe</a>
                        </br>
                        </br>

                        <span class="subtitle">Xe bán chạy nhất trong hệ thống</span>
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
                                             alt=" ${car.model}">
                                    </div>
                                    <div class="d-info">
                                        <div class="d-text">
                                            <h4> ${car.model}</h4>
                                            <div class="d-atr-group">
                                                <span class="d-atr"><img
                                                        src="${pageContext.request.contextPath}/images/icons/1-green.svg"
                                                        alt="">${car.capacity}</span>
                                                <span class="d-atr"><img
                                                        src="${pageContext.request.contextPath}/images/icons/3-green.svg"
                                                        alt="">${car.transmission}</span>
                                                <span class="d-atr"><img
                                                        src="${pageContext.request.contextPath}/images/icons/4-green.svg"
                                                        alt="">${car.carTypeName}</span>
                                            </div>
                                            <div class="d-price">
                                                <h3 class="fw-bold mb-0">
                                                    $<fmt:formatNumber value="${car.pricePerDay}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
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
                        <h1>Hãy bắt đầu hành trình của bạn</h1>
                        <div class="spacer-20"></div>
                    </div>
                    <div class="col-md-3">
                        <i class="fa fa-trophy de-icon mb20"></i>
                        <h4>Dịch vụ hạng nhất</h4>
                        <p>Nơi sang trọng hòa quyện cùng sự tận tâm, mang đến những khoảnh khắc khó quên và vượt xa mọi kỳ vọng của bạn.</p>
                    </div>
                    <div class="col-md-3">
                        <i class="fa fa-road de-icon mb20"></i>
                        <h4>Hỗ trợ 24/7 trên mọi hành trình</h4>
                        <p>Luôn sẵn sàng đồng hành khi bạn cần, giúp bạn yên tâm lăn bánh với sự tự tin và an toàn tuyệt đối.</p>
                    </div>
                    <div class="col-md-3">
                        <i class="fa fa-map-pin de-icon mb20"></i>
                        <h4>Đón – Trả xe miễn phí</h4>
                        <p>Trải nghiệm tiện ích đón trả xe tận nơi hoàn toàn miễn phí, mang lại sự thoải mái và tiện lợi tuyệt đối cho chuyến đi của bạn.</p>
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
                                <h4>Dịch vụ tuyệt vời! Thuê xe chuyên nghiệp!</h4>
                                <blockquote>
                                    Tôi đã sử dụng Rentaly cho nhu cầu thuê xe của mình hơn 5 năm nay. Tôi chưa bao giờ gặp bất kỳ vấn đề nào với dịch vụ của họ. Bộ phận hỗ trợ khách hàng luôn phản hồi nhanh và rất tận tâm. Tôi chắc chắn sẽ giới thiệu Rentaly cho bất kỳ ai đang tìm kiếm dịch vụ thuê xe uy tín.
                                    <span class="by">Thùy Linh</span>
                                </blockquote>
                            </div>
                            <img src="images/testimonial/anh5.jpg" class="img-fluid" alt="">
                        </div>
                    </div>


                    <div class="col-md-4">
                        <div class="de-image-text">
                            <div class="d-text">
                                <div class="d-quote id-color"><i class="fa fa-quote-right"></i></div>
                                <h4>Thuê xe dễ dàng – Dịch vụ tận tâm, uy tín!</h4>
                                <blockquote>
                                    Chúng tôi đã sử dụng Rentaly cho các chuyến đi của mình trong nhiều năm và luôn hài lòng với dịch vụ mà họ mang lại. Đội ngũ hỗ trợ khách hàng cực kỳ chuyên nghiệp, luôn sẵn sàng giúp đỡ khi có bất kỳ vấn đề nào. Giá thuê xe cũng rất cạnh tranh so với thị trường.
                                    <span class="by">Bằng Kiều</span>
                                </blockquote>
                            </div>
                            <img src="images/testimonial/anh6.jpg" class="img-fluid" alt="">
                        </div>
                    </div>

                    <div class="col-md-4">
                        <div class="de-image-text">
                            <div class="d-text">
                                <div class="d-quote id-color"><i class="fa fa-quote-right"></i></div>
                                <h4>Tận hưởng dịch vụ hoàn hảo – Thuê xe không lo nghĩ!</h4>
                                <blockquote>
                                    Rentaly được các chuyên gia trong ngành đánh giá cao là giải pháp thuê xe đáng tin cậy nhất. Với nhiều năm kinh nghiệm, chúng tôi mang đến dịch vụ thuê xe nhanh chóng, an toàn và chuyên nghiệp.
                                    <span class="by">Tuấn Vũ Mạnh</span>
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
                        <span class="subtitle">Bạn đang thắc mắc?</h2></span>
                        <h2>Liên hệ ngay!</h2>
                        <div class="spacer-20"></div>
                    </div>
                </div>
                <div class="row g-custom-x">
                    <div class="col-md-6 wow fadeInUp">
                        <div class="accordion secondary">
                            <div class="accordion-section">
                                <div class="accordion-section-title" data-tab="#accordion-1">
                                    Bắt đầu thế nào với Car Rental?
                                </div>
                                <div class="accordion-section-content" id="accordion-1">
                                    <p>Rất đơn giản! Bạn chỉ cần tạo tài khoản, chọn chiếc xe mình muốn, đặt ngày thuê và xác nhận.
                                        Ngay sau đó, bạn sẽ nhận được email xác nhận với đầy đủ thông tin chi tiết và hướng dẫn nhận xe.</p>
                                </div>
                                <div class="accordion-section-title" data-tab="#accordion-2">
                                    Tôi thanh toán online được chứ?
                                </div>
                                <div class="accordion-section-content" id="accordion-2">
                                    <p>Hoàn toàn có thể! Bạn có thể thanh toán trực tuyến nhanh chóng bằng thẻ tín dụng hoặc thẻ ghi nợ.
                                        Thanh toán online giúp đơn đặt xe của bạn được xác nhận ngay lập tức.
                                        Một số địa điểm còn hỗ trợ trả sau hoặc đặt cọc trước – thanh toán khi nhận xe.</p>
                                </div>
                                <div class="accordion-section-title" data-tab="#accordion-3">
                                    Tôi có thể thuê những loại xe nào?
                                </div>
                                <div class="accordion-section-content" id="accordion-3">
                                    <p>Tùy theo nhu cầu và hành trình của bạn, Rentaly có nhiều lựa chọn:<br>
                                        Xe nhỏ hoặc tiết kiệm nhiên liệu: Phù hợp cho di chuyển trong thành phố.<br>
                                        SUV hoặc Minivan: Dành cho chuyến đi gia đình hoặc cần nhiều không gian hành lý.<br>
                                        Xe sang hoặc cao cấp: Dành cho công việc, sự kiện hay dịp đặc biệt.<br>
                                        Bạn có thể lọc xe theo kích thước, giá hoặc mục đích sử dụng khi đặt.</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-6 wow fadeInUp">
                        <div class="accordion secondary">
                            <div class="accordion-section">
                                <div class="accordion-section-title" data-tab="#accordion-b-4">
                                    Hệ thống bảo mật thế nào?
                                </div>
                                <div class="accordion-section-content" id="accordion-b-4">
                                    <p>Rentaly sẽ giữ một khoản đặt cọc tạm thời trên thẻ thanh toán của bạn khi bắt đầu thuê xe.
                                        Khoản tiền này nhằm đảm bảo cho các chi phí phát sinh như hư hại, vi phạm giao thông, hoặc phụ phí khác.
                                        Khoản đặt cọc sẽ được hoàn trả đầy đủ khi bạn trả xe trong tình trạng tốt.</p>
                                </div>
                                <div class="accordion-section-title" data-tab="#accordion-b-5">
                                    Tôi có thể hủy đặt xe không?
                                </div>
                                <div class="accordion-section-content" id="accordion-b-5">
                                    <p>Rất tiếc, hiện bạn không thể hủy hoặc chỉnh sửa đặt xe online sau khi đã xác nhận.
                                        Nếu muốn thay đổi hoặc hủy, vui lòng liên hệ sớm với chúng tôi — phí hủy có thể áp dụng tùy thời điểm bạn thực hiện.</p>
                                </div>
                                <div class="accordion-section-title" data-tab="#accordion-b-6">
                                    Liệu tôi có thể thuê dài ngày không?
                                </div>
                                <div class="accordion-section-content" id="accordion-b-6">
                                    <p>Sau khi đặt xe và hợp đồng đã được xác nhận, thời gian thuê sẽ không thể kéo dài thêm.
                                        Nếu bạn muốn thuê lâu hơn, chỉ cần tạo một đơn đặt xe mới với ngày mong muốn — chúng tôi sẽ hỗ trợ nhanh nhất có thể.</p>
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
                            <span class="subtitle text-white">Liên hệ để được tư vấn thêm</span>
                            <h2 class="s2">Khách hàng của Rentaly sẽ được chăm sóc miễn phí, tận tâm</h2>
                        </div>

                        <div class="col-lg-4 text-lg-center text-sm-center">
                            <div class="phone-num-big">
                                <i class="fa fa-phone"></i>
                                <span class="pnb-text">
                                            Liên hệ ngay
                                        </span>
                                <span class="pnb-num">
                                            033 5821918
                                        </span>
                            </div>
                            <a href="${pageContext.request.contextPath}/view/contact/contact.jsp" class="btn-main">Liên hệ</a>
                        </div>
                    </div>
                </div>
            </div>
        </section>

    </div>
    <!-- content close -->
    <a href="#" id="back-to-top"></a>
    <!-- footer begin -->
    <jsp:include page="../common/customer/_footer_scripts.jsp"/>
    <!-- footer close -->
</div>


<!-- Javascript Files
================================================== -->
<%--<script src="js/plugins.js"></script>--%>
<%--<script src="js/designesia.js"></script>--%>
<%--<script src="https://maps.googleapis.com/maps/api/js?key=${googleApiKey}&libraries=places"></script>--%>

<script>
    (function () {
        var menu = document.getElementById('notificationMenu');
        var btn = document.getElementById('notificationBtn');

        // Hàm đóng/mở dropdown
        function toggleNotificationDropdown(e) {
            if (e) e.stopPropagation();
            var isOpen = menu.classList.contains('open');
            if (isOpen) {
                menu.classList.remove('open');
                btn.setAttribute('aria-expanded', 'false');
            } else {
                // Đóng User Menu nếu nó đang mở
                document.getElementById('myUserMenu').classList.remove('open');
                document.getElementById('myUserBtn').setAttribute('aria-expanded', 'false');

                menu.classList.add('open');
                btn.setAttribute('aria-expanded', 'true');
            }
        }

        btn.addEventListener('click', toggleNotificationDropdown);

        // Đóng dropdown khi click ra ngoài
        document.addEventListener('click', function (e) {
            if (!menu.contains(e.target)) {
                menu.classList.remove('open');
                btn.setAttribute('aria-expanded', 'false');
            }
        });
    })();
</script>
</body>


<!-- Mirrored from www.madebydesignesia.com/themes/rentaly/index.html by HTTrack Website Copier/3.x [XR&CO'2014], Sat, 20 Sep 2025 10:55:35 GMT -->
</html>