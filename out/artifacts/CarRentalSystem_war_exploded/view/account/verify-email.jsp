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
    <title>Rentaly - Verify Your Email</title>
    <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="image/gif" sizes="16x16">
    <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    
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
        <header class="transparent scroll-light has-topbar">
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
                                    <h4>Check Your Email</h4>
                                    <p>We've sent a 6-digit verification code to <strong>${param.email}</strong>. Please enter the code below.</p>
                                    <div class="spacer-10"></div>
                                    <p style="color:red;">${requestScope.error}</p>
                                    <form method="post" action="${pageContext.request.contextPath}/verify-email" class="form-border">
                                        <input type="hidden" name="email" value="${param.email}">
                                        <div class="field-set mb-3">
                                            <input type="text" name="code" class="form-control" placeholder="6-Digit Code" required maxlength="6" pattern="[0-9]*" inputmode="numeric">
                                        </div>
                                        <div id="submit">
                                            <input type="submit" value="Verify Account" class="btn-main color-2 btn-fullwidth rounded-3">
                                        </div>
                                    </form>
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
</body>
</html>