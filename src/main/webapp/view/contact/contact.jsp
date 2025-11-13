
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zxx">


<!-- Mirrored from www.madebydesignesia.com/themes/rentaly/contact.html by HTTrack Website Copier/3.x [XR&CO'2014], Sat, 20 Sep 2025 11:28:19 GMT -->
<head>
    <title>Rentaly - Multipurpose Vehicle Car Rental Website Template</title>
    <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="image/gif" sizes="16x16">
    <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta content="Rentaly - Multipurpose Vehicle Car Rental Website Template" name="description">
    <meta content="" name="keywords">
    <meta content="" name="author">
    <!-- CSS Files
    ================================================== -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css"
          id="bootstrap">
    <link href="${pageContext.request.contextPath}/css/mdb.min.css" rel="stylesheet" type="text/css" id="mdb">
    <link href="${pageContext.request.contextPath}/css/plugins.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/coloring.css" rel="stylesheet" type="text/css">
    <!-- color scheme -->
    <link id="colors" href="${pageContext.request.contextPath}/css/colors/scheme-01.css" rel="stylesheet"
          type="text/css">
</head>

<body>
<div id="wrapper">

    <!-- page preloader begin -->
    <div id="de-preloader"></div>
    <!-- page preloader close -->

    <!-- header begin -->
    <jsp:include page="../common/customer/_header.jsp"/>
    <!-- header close -->
    <!-- content begin -->
    <div class="no-bottom no-top" id="content">
        <div id="top"></div>

        <!-- section begin -->
        <section id="subheader" class="jarallax text-light">
            <img src="${pageContext.request.contextPath}/images/background/subheader.jpg" class="jarallax-img" alt="">
            <div class="center-y relative text-center">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center">
                            <h1>Liên Hệ Chúng Tôi</h1>
                        </div>
                        <div class="clearfix"></div>
                    </div>
                </div>
            </div>
        </section>
        <!-- section close -->


        <section aria-label="section">
            <div class="container">
                <div class="row g-custom-x">

                    <div class="col-lg-8 mb-sm-30">

                        <h3>Bạn Có Câu Hỏi Nào Không?</h3>

                        <form name="contactForm" id="contact_form" class="form-border" method="post"
                              action="<%= request.getContextPath() %>/contact">

                            <div class="row">
                                <div class="col-md-4 mb10">
                                    <div class="field-set">
                                        <input type="text" name="name" id="name" class="form-control"
                                               placeholder="Tên Của Bạn" required>
                                    </div>
                                </div>
                                <div class="col-md-4 mb10">
                                    <div class="field-set">
                                        <input type="text" name="email" id="email" class="form-control"
                                               placeholder="Email" required>
                                    </div>
                                </div>
                                <div class="col-md-4 mb10">
                                    <div class="field-set">
                                        <input type="text" name="phone" id="phone" class="form-control"
                                               placeholder="Số Điện Thoại" required>
                                    </div>
                                </div>
                            </div>

                            <div class="field-set mb20">
                                <textarea name="message" id="message" class="form-control" placeholder="Câu Hỏi Của Bạn Dành Cho Chúng Tôi"
                                          required></textarea>
                            </div>

                            <div id='submit' class="mt20">
                                <input type='submit' id='send_message' value='Gửi Câu Hỏi' class="btn-main">


                            </div>
                            <p style="color: green;">${msg}</p>
                            <div id="success_message" class='success'>
                                Yêu cầu hỗ trợ của bạn đã được gửi thành công.Vui lòng chờ admin liên hệ hỗ trợ.
                            </div>
                            <div id="error_message" class='error'>
                                Sorry there was an error sending your form.
                            </div>
                        </form>
                    </div>

                    <div class="col-lg-4">

                        <div class="de-box mb30">
                            <h4>Ha Noi Office</h4>
                            <address class="s1">
                                <span><i
                                        class="id-color fa fa-map-marker fa-lg"></i>Thach Hoa, Thach That, Ha Noi</span>
                                <span><i class="id-color fa fa-phone fa-lg"></i>1900 1080</span>
                                <span><i class="id-color fa fa-envelope-o fa-lg"></i><a
                                        href="mailto:contact@example.com">rentalyhanoi@gmail.com</a></span>
                            </address>
                        </div>


                        <div class="de-box mb30">
                            <h4>Ho Chi Minh Office</h4>
                            <address class="s1">
                                <span><i class="fa fa-map-marker fa-lg"></i>7 Nguyen Duc Canh, Q3, TP.HCM</span>
                                <span><i class="fa fa-phone fa-lg"></i>1900 1081</span>
                                <span><i class="fa fa-envelope-o fa-lg"></i><a href="mailto:contact@example.com">rentalysaigon@gmail.com</a></span>
                            </address>
                        </div>

                    </div>

                </div>
            </div>

        </section>

    </div>
    <!-- content close -->

    <a href="#" id="back-to-top"></a>

    <!-- footer begin -->
    <jsp:include page="../common/customer/_footer_scripts.jsp"/>
    <!-- footer close -->

</div>


<!-- Javascript Files
================================================== -->
<script src="${pageContext.request.contextPath}/js/plugins.js"></script>
<script src="${pageContext.request.contextPath}/js/designesia.js"></script>
<script>
    const baseUrl = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/validation-contact.js"></script>
</body>


<!-- Mirrored from www.madebydesignesia.com/themes/rentaly/contact.html by HTTrack Website Copier/3.x [XR&CO'2014], Sat, 20 Sep 2025 11:28:20 GMT -->
</html>