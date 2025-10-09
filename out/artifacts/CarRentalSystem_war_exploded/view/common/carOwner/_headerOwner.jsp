<%--
  Created by IntelliJ IDEA.
  User: tunge
  Date: 10/9/2025
  Time: 11:21 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
                            <li><a class="menu-item" href="${pageContext.request.contextPath}/booking">Booking</a></li>
                            <li><a class="menu-item" href="#">My Account</a>
                                <ul>
                                    <li><a class="menu-item" href="${pageContext.request.contextPath}/customer/customerDashboard">Dashboard</a></li>
                                    <li><a class="menu-item" href="${pageContext.request.contextPath}/customer/profile">My Profile</a></li>
                                    <li><a class="menu-item" href="${pageContext.request.contextPath}/customer/orders">My Orders</a></li>
                                </ul>
                            </li>
                            <li><a class="menu-item" href="${pageContext.request.contextPath}/login">Login</a></li>
                        </ul>
                    </div>
                    <div class="de-flex-col">
                        <div class="menu_side_area">
                            <span id="menu-btn"></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</header>