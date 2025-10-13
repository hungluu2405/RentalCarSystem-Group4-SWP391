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
        <title>Rentaly - Create an Account</title>
        <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="image/gif" sizes="16x16">
        <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="Rentaly - Multipurpose Vehicle Car Rental Website Template" name="description">
        <meta content="" name="keywords">
        <meta content="" name="author">

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
                                <div class="de-flex-col header-col-mid">
                                    <ul id="mainmenu">
                                        <li><a class="menu-item" href="${pageContext.request.contextPath}/index.jsp">Home</a></li>
                                        <li><a class="menu-item" href="${pageContext.request.contextPath}/cars.jsp">Cars</a></li>
                                        <li><a class="menu-item" href="${pageContext.request.contextPath}/quick-booking.jsp">Booking</a></li>
                                        <li><a class="menu-item" href="${pageContext.request.contextPath}/account-dashboard.jsp">My Account</a></li>
                                        <li><a class="menu-item" href="${pageContext.request.contextPath}/about.jsp">About</a></li>
                                        <li><a class="menu-item" href="${pageContext.request.contextPath}/contact.jsp">Contact</a></li>
                                    </ul>
                                </div>
                                <div class="de-flex-col">
                                    <div class="menu_side_area">
                                        <a href="${pageContext.request.contextPath}/login" class="btn-main">Sign In</a>
                                        <span id="menu-btn"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </header>

            <div class="no-bottom no-top" id="content">
                <div id="top"></div>

                <section id="subheader" class="jarallax text-light">
                    <img src="${pageContext.request.contextPath}/images/background/subheader.jpg" class="jarallax-img" alt="">
                    <div class="center-y relative text-center">
                        <div class="container">
                            <div class="row">
                                <div class="col-md-12 text-center">
                                    <h1>Register</h1>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>

                <section aria-label="section">
                    <div class="container">
                        <div class="row">
                            <div class="col-md-10 offset-md-1">
                                <div class="padding40 rounded-3 shadow-soft" data-bgcolor="#ffffff">
                                    <div class="text-center">
                                        <h3>Create Your Account</h3>
                                        <p>Please fill in the information below to create your account.</p>
                                    </div>
                                    <div class="spacer-10"></div>
                                    <p style="color: red; font-weight: bold;" class="text-center">${requestScope.error}</p>

                                    <!-- giữ nguyên form đăng ký -->
                                    <form name="registerForm" class="form-border" method="post" action='${pageContext.request.contextPath}/register'>
                                        <div class="row">
                                            <div class="col-lg-6">
                                                <h5>Personal Details</h5>
                                                <div class="field-set">
                                                    <label>Full Name <span class="text-danger">*</span></label>
                                                    <input type='text' name='full_name' class="form-control" required>
                                                </div>
                                                <div class="field-set">
                                                    <label>Phone Number</label>
                                                    <input type='tel' name='phone' class="form-control">
                                                </div>
                                                <div class="field-set">
                                                    <label>Date of Birth</label>
                                                    <input type='date' name='dob' class="form-control">
                                                </div>
                                                <div class="field-set">
                                                    <label>Gender</label>
                                                    <select name="gender" class="form-control">
                                                        <option value="">-- Select --</option>
                                                        <option value="Male">Male</option>
                                                        <option value="Female">Female</option>
                                                        <option value="Other">Other</option>
                                                    </select>
                                                </div>
                                                <div class="field-set">
                                                    <label>Driver's License Number</label>
                                                    <input type='text' name='driver_license_number' class="form-control">
                                                </div>
                                            </div>

                                            <div class="col-lg-6">
                                                <h5>Account & Address</h5>
                                                <div class="field-set">
                                                    <label>Email Address <span class="text-danger">*</span></label>
                                                    <input type='email' name='email' class="form-control" required>
                                                </div>
                                                <div class="field-set">
                                                    <label>Password <span class="text-danger">*</span></label>
                                                    <input type='password' name='password' class="form-control" required>
                                                </div>
                                                <div class="field-set">
                                                    <label>Re-enter Password <span class="text-danger">*</span></label>
                                                    <input type='password' name='re_password' class="form-control" required>
                                                </div>
                                                <div class="field-set">
                                                    <label>Role <span class="text-danger">*</span></label>
                                                    <select name="role_id" class="form-control" required>
                                                        <option value="">-- Select Role --</option>
                                                        <option value="3">Customer</option>
                                                        <option value="2">Car Owner</option>
                                                    </select>
                                                </div>

                                                <div class="field-set">
                                                    <label>Address Line <span class="text-danger">*</span></label>
                                                    <input type='text' name='address_line' class="form-control" required>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <div class="field-set">
                                                            <label>City / Province <span class="text-danger">*</span></label>
                                                            <input type='text' name='city' class="form-control" required>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="field-set">
                                                            <label>Country <span class="text-danger">*</span></label>
                                                            <input type='text' name='country' value="Vietnam" class="form-control" required>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="field-set">
                                                    <label>Postal Code</label>
                                                    <input type='text' name='postal_code' class="form-control">
                                                </div>
                                            </div>

                                            <div class="col-lg-12 text-center mt-4">
                                                <input type='submit' value='Create Account' class="btn-main">
                                                <div class="mt-3">
                                                    <span>Already have an account? <a href="${pageContext.request.contextPath}/login">Sign In</a></span>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                    <!-- end giữ nguyên form -->
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            </div>

            <footer class="text-light"></footer>
        </div>

        <script src="${pageContext.request.contextPath}/js/plugins.js"></script>
        <script src="${pageContext.request.contextPath}/js/designesia.js"></script>
    </body>
</html>
