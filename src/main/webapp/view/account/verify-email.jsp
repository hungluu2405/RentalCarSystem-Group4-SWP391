<%
    if (session.getAttribute("user") != null) {
        response.sendRedirect(request.getContextPath() + "/home");
        return;
    }
%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Rentaly - Xác minh email</title>
    <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="image/gif" sizes="16x16">
    <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta content="Rentaly - Hệ thống thuê xe đa năng" name="description">

    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/mdb.min.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/plugins.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/coloring.css" rel="stylesheet" type="text/css">
    <link id="colors" href="${pageContext.request.contextPath}/css/colors/scheme-01.css" rel="stylesheet" type="text/css">
</head>
<body>
    <div id="wrapper">
        <div id="de-preloader"></div>
        <header class="transparent scroll-light has-topbar"></header>

        <div class="no-bottom no-top" id="content">
            <div id="top"></div>
            <section id="section-hero" aria-label="section" class="jarallax">
                <img src="${pageContext.request.contextPath}/images/background/2.jpg" class="jarallax-img" alt="">
                <div class="v-center">
                    <div class="container">
                        <div class="row align-items-center">
                            <div class="col-lg-4 offset-lg-4">
                                <div class="padding40 rounded-3 shadow-soft" data-bgcolor="#ffffff">
                                    <h4>Kiểm tra email của bạn</h4>
                                    <p>Một mã xác minh gồm 6 chữ số đã được gửi tới địa chỉ <strong>${param.email}</strong>.  
                                    Vui lòng nhập mã bên dưới để xác nhận tài khoản.</p>
                                    
                                    <div class="spacer-10"></div>
                                    <p style="color:red;">${requestScope.error}</p>
                                    <p style="color:green;">${requestScope.message}</p>

                                    <form method="post" action="${pageContext.request.contextPath}/verify-email" class="form-border">
                                        <input type="hidden" name="email" value="${param.email}">
                                        <div class="field-set mb-3">
                                            <input type="text" name="code" class="form-control" placeholder="Nhập mã 6 chữ số" required maxlength="6" pattern="[0-9]*" inputmode="numeric">
                                        </div>
                                        <div id="submit">
                                            <input type="submit" value="Xác minh tài khoản" class="btn-main color-2 btn-fullwidth rounded-3">
                                        </div>
                                    </form>

                                    <!-- ✅ Khu vực gửi lại mã -->
                                    <div id="resendSection" class="mt-3 text-center">
                                        <p id="timer">Bạn có thể gửi lại mã sau <span id="countdown">60</span> giây.</p>
                                        <form id="resendForm" method="post"
                                              action="${pageContext.request.contextPath}/resend-code"
                                              class="mt-3"
                                              style="display:none;">
                                            <input type="hidden" name="email" value="${param.email}">
                                            <input type="hidden" name="type" value="verify">
                                            <input type="submit" value="Gửi lại mã" class="btn-main btn-fullwidth rounded-3">
                                        </form>
                                    </div>

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

        <a href="#" id="back-to-top"></a>
    </div>

    <script src="${pageContext.request.contextPath}/js/plugins.js"></script>
    <script src="${pageContext.request.contextPath}/js/designesia.js"></script>

    <!-- ✅ Script đếm ngược gửi lại mã -->
    <script>
        let seconds = 60;
        const countdown = document.getElementById("countdown");
        const timerText = document.getElementById("timer");
        const resendForm = document.getElementById("resendForm");

        const timer = setInterval(() => {
            seconds--;
            countdown.textContent = seconds;
            if (seconds <= 0) {
                clearInterval(timer);
                timerText.style.display = "none";
                resendForm.style.display = "block";
            }
        }, 1000);
    </script>
</body>
</html>
