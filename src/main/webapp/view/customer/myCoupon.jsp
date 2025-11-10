<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../common/customer/_head.jsp"/>
    <title>My Coupons | Rentaly</title>
</head>

<body>
<div id="wrapper">

    <%-- Header chung --%>
    <jsp:include page="../common/customer/_header.jsp"/>

    <div class="no-bottom no-top zebra" id="content">
        <div id="top"></div>

        <section id="subheader" class="jarallax text-light">
            <img src="${pageContext.request.contextPath}/images/background/14.jpg" class="jarallax-img" alt="">
            <div class="center-y relative text-center">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center">
                            <h1>My Coupons</h1>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <section id="section-settings" class="bg-gray-100">
            <div class="container">
                <div class="row">

                    <div class="col-lg-3 mb30">
                        <jsp:include page="../common/customer/_sidebar.jsp">
                            <jsp:param name="activePage" value="myCoupon"/>
                        </jsp:include>
                    </div>

                    <div class="col-lg-9">
                        <h3 class="mb-4 text-center">🏷️ Mã Giảm Giá Khả Dụng Của Bạn</h3>

                        <c:choose>
                            <c:when test="${not empty availableCoupons}">
                                <c:forEach var="promo" items="${availableCoupons}">
                                    <div class="de-item-list no-border mb30 coupon-item">

                                        <div class="d-info">
                                            <div class="d-text">
                                                <h4>Coupon: <span class="text-primary">${promo.code}</span></h4>
                                                <ul class="d-atr">
                                                    <li>
                                                        <span>Khuyến Mãi:</span>
                                                        <c:choose>
                                                            <c:when test="${promo.discountType == 'PERCENT'}">
                                                                <b class="text-success">${promo.discountRate}%</b>
                                                            </c:when>
                                                            <c:when test="${promo.discountType == 'FIXED'}">
                                                                <b class="text-success">
                                                                    <fmt:formatNumber value="${promo.discountRate}" type="currency" currencySymbol="₫"/>
                                                                </b>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <b class="text-muted">${promo.discountRate}</b>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </li>
                                                    <li>
                                                        <span>Expires:</span>
                                                        <b class="text-danger">
                                                            <fmt:formatDate value="${promo.endDate}" pattern="dd/MM/yyyy"/>
                                                        </b>
                                                    </li>
                                                    <li>
                                                        <span>Mô Tả:</span> ${promo.description}
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>

                                        <div class="d-price coupon-action-area">
                                            <a href="${pageContext.request.contextPath}/cars?promo=${promo.code}"
                                               class="btn-main btn-apply-coupon">
                                                Áp Dụng
                                            </a>
                                        </div>

                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-info text-center mt-5" role="alert">
                                    🎉 Bạn hiện không có mã giảm giá nào khả dụng.
                                </div>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </div>
            </div>
        </section>
    </div>

    <%-- Footer scripts chung --%>
    <jsp:include page="../common/customer/_footer_scripts.jsp"/>

</div>
</body>
</html>
