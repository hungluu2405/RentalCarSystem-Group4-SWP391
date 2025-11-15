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
    <title>Rentaly - Xác minh mã</title>
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
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>

<body>
<div id="wrapper">
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
                                <h4>Nhập mã xác minh</h4>
                                <p>Một mã gồm 6 chữ số đã được gửi đến email của bạn. Vui lòng nhập mã bên dưới để tiếp tục.</p>
                                <div class="spacer-10"></div>
                                <p style="color:red;">${requestScope.error}</p>
                                <p style="color:green;">${requestScope.message}</p>

                                <form id="form_verify" class="form-border" method="post" action="${pageContext.request.contextPath}/verify-code">
                                    <input type="hidden" name="email" value="${param.email}">
                                    <div class="field-set mb-3">
                                        <input type="text" name="code" class="form-control" placeholder="Nhập mã 6 chữ số" required maxlength="6" pattern="[0-9]*" inputmode="numeric">
                                    </div>
                                    <div id="submit">
                                        <input type="submit" value="Xác minh" class="btn-main btn-fullwidth rounded-3">
                                    </div>
                                </form>

                                <!-- ✅ Khu vực gửi lại mã -->
                                <div id="resendSection" class="mt-3 text-center">
                                    <p id="timer">Bạn có thể gửi lại mã sau <span id="countdown">60</span> giây.</p>
                                    <form id="resendForm" method="post"
                                          action="${pageContext.request.contextPath}/resend-code"
                                          style="display:none;">
                                        <input type="hidden" name="email" value="${param.email}">
                                        <input type="hidden" name="type" value="reset">
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
</div>

<script src="${pageContext.request.contextPath}/js/plugins.js"></script>
<script src="${pageContext.request.contextPath}/js/designesia.js"></script>

<!-- ✅ Script đếm ngược 60 giây -->
<script>
    let seconds = 60;
    const countdown = document.getElementById("countdown");
    const timerText = document.getElementById("timer");
    const resendForm = document.getElementById("resendForm");

    const interval = setInterval(() => {
        seconds--;
        countdown.textContent = seconds;
        if (seconds <= 0) {
            clearInterval(interval);
            timerText.style.display = "none";
            resendForm.style.display = "block";
        }
    }, 1000);
</script>
</body>
</html>