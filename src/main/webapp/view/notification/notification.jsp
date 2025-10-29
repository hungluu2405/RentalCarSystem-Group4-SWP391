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
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" id="bootstrap">
    <link href="${pageContext.request.contextPath}/css/mdb.min.css" rel="stylesheet" type="text/css" id="mdb">
    <link href="${pageContext.request.contextPath}/css/plugins.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/coloring.css" rel="stylesheet" type="text/css">
    <!-- color scheme -->
    <link id="colors" href="${pageContext.request.contextPath}/css/colors/scheme-01.css" rel="stylesheet" type="text/css">
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
                            <h1>Notification</h1>
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
                    <!-- add here -->
                    <div class="container" style="max-width:860px;margin:24px auto">
    <h2>Thông báo</h2>

    <c:if test="${empty sessionScope.user}">
      <p>You need <a href="${pageContext.request.contextPath}/view/account/login.jsp">Log in</a> to see notification.</p>
    </c:if>

    <c:if test="${not empty sessionScope.user}">
      <c:choose>
        <c:when test="${empty requestScope.notifications}">
          <p>There is no notification now!</p>
        </c:when>
        <c:otherwise>
          <c:forEach items="${requestScope.notifications}" var="n">
            <div class="notify-item" style="display:flex;gap:12px;padding:12px 0;border-bottom:1px solid #eee">
              <div class="notify-icon"><i class="fa-regular fa-bell"></i></div>
              <div class="notify-content">
                <div class="notify-title">${n.title}</div>
                <div class="notify-text">${n.message}</div>
                <div class="notify-time">${n.createdAt}</div>
                <c:if test="${not empty n.link}">
                  <div style="margin-top:6px">
                    <a href="${n.link}">Back to home</a>
                  </div>
                </c:if>
              </div>
            </div>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </c:if>
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

<div id="selector">
    <div id="demo-rtl" class="sc-opt">
        <div class="sc-icon">RTL</div><span class="sc-val">Click to Enable</span>
    </div>
</div>

<!-- Javascript Files
================================================== -->
<script src="${pageContext.request.contextPath}/js/plugins.js"></script>
<script src="${pageContext.request.contextPath}/js/designesia.js"></script>
<script src="${pageContext.request.contextPath}/js/validation-contact.js"></script>
</body>


</html>