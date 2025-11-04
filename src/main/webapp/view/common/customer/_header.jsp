<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="util.TimeAgoUtil" %>

<!-- ===== THÊM FONT AWESOME 6 ===== -->
<%--<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">--%>

<style>
    /* === USER ICON & BUTTONS === */
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

    /* === OVERFLOW FIX === */
    .menu_side_area, .de-flex-col, .de-flex {
        overflow: visible !important;
    }

    /* === MENU SIDE AREA FLEX === */
    .menu_side_area {
        display: flex;
        align-items: center;
        gap: 10px;
    }

    /* === NOTIFICATION MENU === */
    .notification-menu {
        position: relative;
        display: inline-block;
        vertical-align: middle;
    }

    .btn-bell {
        position: relative;
        background: transparent;
        border: none;
        cursor: pointer;
        color: #fff;
        font-size: 20px;  /* ← TĂNG SIZE */
        padding: 6px 8px;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .btn-bell:hover {
        color: #32cd32;
    }

    /* Badge số lượng thông báo */
    .notification-badge {
        position: absolute;
        top: -5px;
        right: -5px;  /* ← SỬA VỊ TRÍ */
        background-color: #ff4d4d;
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
        position: absolute;
        top: calc(100% + 6px);
        right: 0;
        width: 300px;
        background: #fff;
        border-radius: 8px;
        box-shadow: 0 6px 18px rgba(0, 0, 0, 0.12);
        padding: 0;
        z-index: 9998;
        opacity: 0;
        visibility: hidden;
        transform: translateY(-6px);
        transition: opacity .12s ease, transform .12s ease, visibility .12s;
        pointer-events: none;
    }

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
        max-height: 300px;
        overflow-y: auto;
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
        background-color: #e6f7ff;
        border-left: 3px solid #007bff;
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

    .notification-dropdown .empty-state {
        text-align: center;
        padding: 20px 15px;
        color: #999;
        font-style: italic;
    }

    /* === USER MENU === */
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

    .my-user-menu.open .my-user-dropdown {
        opacity: 1;
        visibility: visible;
        transform: translateY(0);
        pointer-events: auto;
    }
</style>

<header class="transparent scroll-light has-topbar">
    <div id="topbar" class="topbar-dark text-light">
        <div class="container">
            <div class="topbar-left xs-hide">
                <div class="topbar-widget">
                    <div class="topbar-widget"><a href="#"><i class="fa fa-phone"></i>+208 333 9296</a></div>
                    <div class="topbar-widget"><a href="#"><i class="fa fa-envelope"></i>contact@rentaly.com</a></div>
                    <div class="topbar-widget"><a href="#"><i class="fa fa-clock-o"></i>Mon - Fri 08.00 - 18.00</a>
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
                        <div id="logo">
                            <a href="${pageContext.request.contextPath}/home">
                                <img class="logo-1" src="${pageContext.request.contextPath}/images/logo-light.png" alt="">
                                <img class="logo-2" src="${pageContext.request.contextPath}/images/logo.png" alt="">
                            </a>
                        </div>
                    </div>

                    <div class="de-flex-col header-col-mid"></div>

                    <div class="de-flex-col">
                        <div class="menu_side_area">
                            <c:choose>
                                <c:when test="${not empty sessionScope.user}">

                                    <!-- ===== NOTIFICATION BELL ===== -->
                                    <div id="notificationMenu" class="notification-menu">
                                        <button id="notificationBtn" class="btn-bell" type="button"
                                                aria-haspopup="true" aria-expanded="false" title="Notifications">
                                            <i class="fa fa-bell"></i>
                                            <c:if test="${sessionScope.unreadNotificationCount > 0}">
                                                <span class="notification-badge">${sessionScope.unreadNotificationCount}</span>
                                            </c:if>
                                        </button>

                                        <div class="notification-dropdown" role="menu" aria-labelledby="notificationBtn">
                                            <div class="dropdown-header">
                                                <h4>Notifications</h4>
                                                <a href="${pageContext.request.contextPath}/mark-all-read">Mark All Read</a>
                                            </div>

                                            <ul id="notification-list">
                                                <c:choose>
                                                    <c:when test="${not empty sessionScope.latestNotifications}">
                                                        <c:forEach var="noti" items="${sessionScope.latestNotifications}">
                                                            <li class="${!noti.read ? 'unread' : ''}">
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

                                    <!-- ===== USER MENU ===== -->
                                    <div id="myUserMenu" class="my-user-menu">
                                        <button id="myUserBtn" class="my-user-btn" type="button"
                                                aria-haspopup="true" aria-expanded="false" title="Account">
                                            <i class="fa fa-user" aria-hidden="true"></i>
                                        </button>

                                        <div class="my-user-dropdown" role="menu" aria-labelledby="myUserBtn">
                                            <a class="menu-item"
                                               href="${pageContext.request.contextPath}/customer/profile"
                                               role="menuitem">My Account</a>
                                            <a class="menu-item"
                                               href="${pageContext.request.contextPath}/change-password"
                                               role="menuitem">Change Password</a>
                                            <a class="menu-item"
                                               href="${pageContext.request.contextPath}/logout"
                                               role="menuitem">Log Out</a>
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

<script>
    document.addEventListener('DOMContentLoaded', function () {
        var menu = document.getElementById('myUserMenu');
        var btn = document.getElementById('myUserBtn');
        var hideTimeout = null;

        if (!menu || !btn) return;

        btn.addEventListener('click', function (e) {
            e.stopPropagation();
            var isOpen = menu.classList.contains('open');
            if (isOpen) {
                menu.classList.remove('open');
                btn.setAttribute('aria-expanded', 'false');
            } else {
                var notifMenu = document.getElementById('notificationMenu');
                if (notifMenu) {
                    notifMenu.classList.remove('open');
                    document.getElementById('notificationBtn').setAttribute('aria-expanded', 'false');
                }
                menu.classList.add('open');
                btn.setAttribute('aria-expanded', 'true');
            }
        });

        document.addEventListener('click', function (e) {
            if (menu && !menu.contains(e.target)) {
                menu.classList.remove('open');
                btn.setAttribute('aria-expanded', 'false');
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
            if (hideTimeout) clearTimeout(hideTimeout);
            hideTimeout = setTimeout(function () {
                menu.classList.remove('open');
                btn.setAttribute('aria-expanded', 'false');
            }, 250);
        });

        var notifMenu = document.getElementById('notificationMenu');
        var notifBtn = document.getElementById('notificationBtn');

        if (!notifMenu || !notifBtn) return;

        function toggleNotificationDropdown(e) {
            if (e) e.stopPropagation();
            var isOpen = notifMenu.classList.contains('open');
            if (isOpen) {
                notifMenu.classList.remove('open');
                notifBtn.setAttribute('aria-expanded', 'false');
            } else {
                menu.classList.remove('open');
                btn.setAttribute('aria-expanded', 'false');
                notifMenu.classList.add('open');
                notifBtn.setAttribute('aria-expanded', 'true');
            }
        }

        notifBtn.addEventListener('click', toggleNotificationDropdown);

        document.addEventListener('click', function (e) {
            if (!notifMenu.contains(e.target)) {
                notifMenu.classList.remove('open');
                notifBtn.setAttribute('aria-expanded', 'false');
            }
        });
    });
</script>