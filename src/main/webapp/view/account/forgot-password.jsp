<%
    if (session.getAttribute("user") != null) {
        response.sendRedirect(request.getContextPath() + "/home"); 
        return;
    }
%>
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
                                    <h4>Forgot Your Password?</h4>
                                    <p>Enter your email address and we'll send you a code to reset your password.</p>
                                    <div class="spacer-10"></div>
                                    <p style="color:red;">${requestScope.error}</p>
                                    <p style="color:green;">${requestScope.message}</p>
                                    <form id="form_forgot" class="form-border" method="post" action="${pageContext.request.contextPath}/forgot-password">
                                        <div class="field-set mb-3">
                                            <input type="email" name="email" class="form-control" placeholder="Your Email Address" required>
                                        </div>
                                        <div id="submit">
                                            <input type="submit" value="Send Reset Code" class="btn-main btn-fullwidth rounded-3">                                        </div>
                                    </form>
                                    <div class="mt-3 text-center">
                                        <span>Remember your password? <a href="${pageContext.request.contextPath}/login">Back to Login</a></span>
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