<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Rentaly - Đặt lại mật khẩu</title>
    <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="image/gif" sizes="16x16">
    <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta content="Rentaly - Hệ thống thuê xe đa năng" name="description">

    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" id="bootstrap">
    <link href="${pageContext.request.contextPath}/css/mdb.min.css" rel="stylesheet" type="text/css" id="mdb">
    <link href="${pageContext.request.contextPath}/css/plugins.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/coloring.css" rel="stylesheet" type="text/css">
    <link id="colors" href="${pageContext.request.contextPath}/css/colors/scheme-01.css" rel="stylesheet" type="text/css">
</head>

<body>
<div id="wrapper">
    <header class="transparent scroll-light has-topbar">
        <div id="topbar" class="topbar-dark text-light">
            <div class="container">
                <div class="topbar-left xs-hide">
                    <div class="topbar-widget">
                        <div class="topbar-widget"><a href="#"><i class="fa fa-phone"></i>+208 333 9296</a></div>
                        <div class="topbar-widget"><a href="#"><i class="fa fa-envelope"></i>contact@rentaly.com</a></div>
                        <div class="topbar-widget"><a href="#"><i class="fa fa-clock-o"></i>Thứ 2 - Thứ 6: 08:00 - 18:00</a></div>
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
                                <h4>Đặt lại mật khẩu</h4>
                                <p>Vui lòng nhập mật khẩu mới cho tài khoản của bạn.</p>
                                <div class="spacer-10"></div>
                                <p style="color:red;">${requestScope.error}</p>

                                <form id="form_reset" class="form-border" method="post" action="${pageContext.request.contextPath}/reset-password">
                                    <div class="field-set mb-3">
                                        <input type="password" name="password" class="form-control" placeholder="Mật khẩu mới" required>
                                    </div>
                                    <div class="field-set mb-3">
                                        <input type="password" name="re_password" class="form-control" placeholder="Nhập lại mật khẩu mới" required>
                                    </div>
                                    <div id="submit">
                                        <input type="submit" value="Xác nhận đặt lại mật khẩu" class="btn-main btn-fullwidth rounded-3">
                                    </div>
                                </form>

                                <div class="mt-3 text-center">
                                    <a href="${pageContext.request.contextPath}/login" class="text-decoration-none">Quay lại đăng nhập</a>
                                </div>
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