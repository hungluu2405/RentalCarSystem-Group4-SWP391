<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/home"); 
        return;
    }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zxx">

    <head>
        <title>Change Password - Rentaly</title>
        <link rel="icon" href="images/icon.png" type="image/gif" sizes="16x16">
        <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
        <meta content="width=device-width, initial-scale=1.0" name="viewport">

        <!-- CSS -->
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
        <link href="css/mdb.min.css" rel="stylesheet" type="text/css">
        <link href="css/plugins.css" rel="stylesheet" type="text/css">
        <link href="css/style.css" rel="stylesheet" type="text/css">
        <link href="css/coloring.css" rel="stylesheet" type="text/css">
        <link id="colors" href="css/colors/scheme-01.css" rel="stylesheet" type="text/css">
    </head>

    <body>
        <div id="wrapper">
            <header class="transparent scroll-light has-topbar">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="de-flex sm-pt10">
                                <div class="de-flex-col">
                                    <div id="logo">
                                        <a href="${pageContext.request.contextPath}/home">
                                            <img class="logo-1" src="images/logo-light.png" alt="">
                                            <img class="logo-2" src="images/logo.png" alt="">
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </header>

            <div class="no-bottom no-top" id="content">
                <section id="section-hero" class="jarallax">
                    <img src="images/background/2.jpg" class="jarallax-img" alt="">
                    <div class="v-center">
                        <div class="container">
                            <div class="row align-items-center">
                                <div class="col-lg-4 offset-lg-4">
                                    <div class="padding40 rounded-3 shadow-soft" data-bgcolor="#ffffff">
                                        <h4>Đổi mật khẩu</h4>
                                        <div class="spacer-10"></div>
                                        <p style="color:red;">${requestScope.error}</p>
                                        <p style="color:green;">${requestScope.message}</p>

                                        <form class="form-border" method="post" action="${pageContext.request.contextPath}/change-password">
                                            <div class="field-set">
                                                <input type="password" name="oldPassword" class="form-control"
                                                       placeholder="Old Password" required>
                                            </div>
                                            <div class="field-set">
                                                <input type="password" name="newPassword" class="form-control"
                                                       placeholder="New Password" required>
                                            </div>
                                            <div class="field-set">
                                                <input type="password" name="confirmPassword" class="form-control"
                                                       placeholder="Confirm New Password" required>
                                            </div>
                                            <div id="submit">
                                                <input type="submit" value="Change Password"
                                                       class="btn-main btn-fullwidth rounded-3">
                                            </div>
                                        </form>
                                        <div class="spacer-10"></div>
                                        <div class="text-center">
                                            <a href="${pageContext.request.contextPath}/customer/profile">Quay lại hồ sơ</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            </div>

            <script src="${pageContext.request.contextPath}/js/plugins.js"></script>
            <script src="${pageContext.request.contextPath}/js/designesia.js"></script>
        </div>
    </body>
</html>

