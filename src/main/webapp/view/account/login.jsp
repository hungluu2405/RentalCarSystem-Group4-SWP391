<!DOCTYPE html>
<html lang="zxx">


<!-- Mirrored from www.madebydesignesia.com/themes/rentaly/login.html by HTTrack Website Copier/3.x [XR&CO'2014], Sat, 20 Sep 2025 11:28:20 GMT -->

<head>
    <title>Rentaly - Multipurpose Vehicle Car Rental Website Template</title>
    <link rel="icon" href="images/icon.png" type="image/gif" sizes="16x16">
    <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta content="Rentaly - Multipurpose Vehicle Car Rental Website Template" name="description">
    <meta content="" name="keywords">
    <meta content="" name="author">
    <!-- CSS Files
    ================================================== -->
    <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" id="bootstrap">
    <link href="css/mdb.min.css" rel="stylesheet" type="text/css" id="mdb">
    <link href="css/plugins.css" rel="stylesheet" type="text/css">
    <link href="css/style.css" rel="stylesheet" type="text/css">
    <link href="css/coloring.css" rel="stylesheet" type="text/css">
    <!-- color scheme -->
    <link id="colors" href="css/colors/scheme-01.css" rel="stylesheet" type="text/css">
</head>

<body>
<div id="wrapper">

    <!-- page preloader begin -->
    <div id="de-preloader"></div>
    <!-- page preloader close -->

    <!-- header begin -->
    <header class="transparent scroll-light has-topbar">
        <div id="topbar" class="topbar-dark text-light">
            <div class="container">
                <div class="topbar-left xs-hide">
                    <div class="topbar-widget">
                        <div class="topbar-widget"><a href="#"><i class="fa fa-phone"></i>+208 333 9296</a></div>
                        <div class="topbar-widget"><a href="#"><i class="fa fa-envelope"></i>contact@rentaly.com</a>
                        </div>
                        <div class="topbar-widget"><a href="#"><i class="fa fa-clock-o"></i>Mon - Fri 08.00 -
                            18.00</a></div>
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
                                <!-- logo begin -->
                                <div id="logo">
                                    <a href="${pageContext.request.contextPath}/home">
                                        <img class="logo-1" src="images/logo-light.png" alt="">
                                        <img class="logo-2" src="images/logo.png" alt="">
                                    </a>
                                </div>
                                <!-- logo close -->
                            </div>
                        </div>
                        <div class="de-flex-col header-col-mid">
                            <ul id="mainmenu">
                                <li><a class="menu-item" href="${pageContext.request.contextPath}/home">Home</a>
                                </li>
                                <li><a class="menu-item" href="${pageContext.request.contextPath}/cars">Car</a>

                                </li>
                                <li><a class="menu-item"
                                       href="${pageContext.request.contextPath}/view/contact/contact.jsp">Contact</a>
                                </li>
                                <li><a class="menu-item" href="${pageContext.request.contextPath}/about.html">About
                                    Us</a></li>

                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </header>
    <!-- header close -->
    <div class="no-bottom no-top" id="content">
        <div id="top"></div>
        <section id="section-hero" aria-label="section" class="jarallax">
            <img src="${pageContext.request.contextPath}/images/background/2.jpg" class="jarallax-img" alt="">
            <div class="v-center">
                <div class="container">
                    <div class="row align-items-center">
                        <div class="col-lg-4 offset-lg-4">
                            <div class="padding40 rounded-3 shadow-soft" data-bgcolor="#ffffff">
                                <h4>Login</h4>
                                <div class="spacer-10"></div>

                                <%-- Hi?n th? th�ng b�o ??ng k� th�nh c�ng --%>
                                <% if ("success".equals(request.getParameter("register"))) { %>
                                <p style="color:green;">Registration successful! You can now sign in.</p>
                                <% } %>

                                <% if ("success".equals(request.getParameter("reset"))) { %>
                                <p style="color:green;">Password has been reset successfully. Please log in.</p>
                                <% } %>

                                <%-- Hi?n th? th�ng b�o l?i ??ng nh?p --%>
                                <p style="color:red;">${requestScope.error}</p>

                                <form id="form_login" class="form-border" method="post"
                                      action="${pageContext.request.contextPath}/login">

                                    <div class="field-set mb-3">
                                        <input type="text" name="loginKey" id="loginKey" class="form-control"
                                               placeholder="Email or Username" value='${formData.loginKey}' required/>
                                    </div>

                                    <div class="field-set">
                                        <input type="password" name="password" id="password" class="form-control"
                                               placeholder="Your Password"  required/>
                                    </div>

                                    <div class="text-end mt-2">
                                        <a href="${pageContext.request.contextPath}/forgot-password">Forgot
                                            Password?</a>
                                    </div>
                                    <div class="spacer-10"></div>

                                    <div id="submit">
                                        <input type="submit" value="Sign In" class="btn-main btn-fullwidth rounded-3"/>
                                    </div>
                                </form>

                                <div class="mt-3 text-center">
                                    <span>Don't have an account? <a href="${pageContext.request.contextPath}/register">Sign Up</a></span>
                                </div>

                                <div class="spacer-10"></div>

                                <div class="title-line">Or&nbsp;sign&nbsp;in&nbsp;with</div>
                                <div style="display: flex; justify-content: center; margin-top: 10px;">
                                    <a href="${pageContext.request.contextPath}/login-google"
                                       style="display: flex; align-items: center; justify-content: center;
                                       position: relative; width: 200px; height: 40px;
                                       background: #fff; border: 1px solid #dadce0; border-radius: 6px;
                                       color: #3c4043; font-weight: 500; text-decoration: none;">
                                        <img src="${pageContext.request.contextPath}/images/svg/google_icon.svg" alt=""
                                             style="width: 20px; height: 20px; margin-right: 10px;">
                                        Google
                                    </a>
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
</body>
</html>