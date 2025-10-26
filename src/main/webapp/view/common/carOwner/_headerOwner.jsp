<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
    /* CSS CŨ CỦA BẠN */
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


    .menu_side_area, .de-flex-col, .de-flex {
        overflow: visible !important;
    }

    .my-user-menu {
        position: relative;
        display: inline-block;
        vertical-align: middle;
        margin-right: 15px;
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
        opacity: 0; /* ẨN */
        visibility: hidden; /* ẨN */
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

    /* Quy tắc hiển thị */
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
                                <img class="logo-1" src="${pageContext.request.contextPath}/images/logo-light.png"
                                     alt="">
                                <img class="logo-2" src="${pageContext.request.contextPath}/images/logo.png" alt="">
                            </a>
                        </div>
                    </div>

                    <div class="de-flex-col header-col-mid">
                    </div>

                    <div class="de-flex-col">
                        <div class="menu_side_area">

                            <c:choose>
                                <c:when test="${not empty sessionScope.user}">
                                    <div id="myUserMenu" class="my-user-menu">
                                        <button id="myUserBtn" class="my-user-btn" type="button" aria-haspopup="true"
                                                aria-expanded="false" title="Tài khoản">
                                            <i class="fa fa-user" aria-hidden="true"></i>
                                        </button>

                                        <div class="my-user-dropdown" role="menu" aria-labelledby="myUserBtn">


                                            <a class="menu-item"
                                               href="${pageContext.request.contextPath}/owner/profile"
                                               role="menuitem">My Account</a>


                                            <a class="menu-item"
                                               href="${pageContext.request.contextPath}/change-password"
                                               role="menuitem">Change Password</a>
                                            <a class="menu-item" href="${pageContext.request.contextPath}/logout"
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
            if (hideTimeout)
                clearTimeout(hideTimeout);
            hideTimeout = setTimeout(function () {
                menu.classList.remove('open');
                btn.setAttribute('aria-expanded', 'false');
            }, 250);
        });

    });
</script>